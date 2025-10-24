package util;


import java.sql.Connection;
/*----------------------------------------------
 *project: SGC
 *fle:	BDConexao.java
 *Desenvolvido por: Domingos Dala Vunge
 *----------------------------------------------*/
import controller.ProdutoController;
import dao.ArmazemDao;
import dao.ProdutoDao;
import entity.TbStock;
import modelo.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDConexao
{

    private static volatile BDConexao instancia;
    private volatile Connection connection;
    private static boolean monitorAtivo = false;

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
    private static ProdutoDao produtoDao = new ProdutoDao( emf );

    static
    {
        Runtime.getRuntime().addShutdownHook( new Thread( () ->
        {
            try
            {
                if ( instancia != null )
                {
                    instancia.close();
                    System.out.println( "[BDConexao] 📴 Fechando conexão no encerramento da aplicação." );
                }
            }
            catch ( Exception ignored )
            {
            }
        } ) );
    }

    private static final ScheduledExecutorService monitorExecutor
            = Executors.newSingleThreadScheduledExecutor( r ->
            {
                Thread t = new Thread( r, "BDConexao-Monitor" );
                t.setDaemon( true );
                return t;
            } );

    // 🔹 Construtor privado
    private BDConexao()
    {
        conectar();
        if ( !monitorAtivo )
        {
            iniciarMonitoramento();
            monitorAtivo = true;
        }
    }

    // ===========================================================
    // Singleton (seguro para multithread)
    // ===========================================================
    public static BDConexao getInstancia()
    {
//        if ( instancia == null )
//        {
//            synchronized ( BDConexao.class )
//            {
//                if ( instancia == null )
//                {
//                    instancia = BDConexao.getInstancia();
//                }
//            }
//        }
        if ( instancia == null )
        {
            synchronized ( BDConexao.class )
            {
                if ( instancia == null )
                {
                    instancia = new BDConexao(); // ✅ Correto
                }
            }
        }

        return instancia;
    }

    // ===========================================================
    // Conectar ao MySQL
    // ===========================================================
    private synchronized Connection conectar()
    {
        try
        {
            if ( connection != null && !connection.isClosed() && connection.isValid( 2 ) )
            {
                return connection;
            }

            Vector<String> info = leituraFicheiroSeguro();
            if ( info == null || info.isEmpty() )
            {
                System.err.println( "[BDConexao] ⚠ Ficheiro de credenciais vazio!" );
                showMessage( "Erro: ficheiro de credenciais não encontrado!" );
                return null;
            }

            String ip = info.get( 0 ).trim();
            String porta = (info.size() > 1 ? info.get( 1 ).trim() : "3306");

            String url = "jdbc:mysql://" + ip + ":" + porta + "/kitanda_db"
                    //            String url = "jdbc:mysql://" + ip + ":" + porta + "/kitanda_db"
                    + "?zeroDateTimeBehavior=convertToNull"
                    + "&useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&autoReconnect=true"
                    + "&connectTimeout=5000"
                    + "&socketTimeout=30000"
                    + "&characterEncoding=UTF-8";

            connection = DriverManager.getConnection( url, "root", "DoV90x?#" );

            System.out.println( "[BDConexao] ✅ Conectado a: " + ip + ":" + porta );
            return connection;

        }
        catch ( SQLException ex )
        {
            System.err.println( "[BDConexao] ❌ Falha ao conectar: " + ex.getMessage() );
            return null;
        }
    }

    // ===========================================================
    // Monitor automático
    // ===========================================================
    private void iniciarMonitoramento()
    {
        monitorExecutor.scheduleAtFixedRate( () ->
        {
            try
            {
                if ( connection == null || connection.isClosed() || !connection.isValid( 2 ) )
                {
                    System.out.println( "[BDConexao] ⚠ Conexão perdida. Tentando reconectar..." );
                    reconectar();
                }
            }
            catch ( SQLException e )
            {
                System.err.println( "[BDConexao] Erro ao verificar conexão: " + e.getMessage() );
                reconectar();
            }
        }, 10, 30, TimeUnit.SECONDS );
    }

    // ===========================================================
    // Recriar conexão
    // ===========================================================
    private synchronized void reconectar()
    {
        try
        {
            if ( connection != null && !connection.isClosed() )
            {
                connection.close();
            }
        }
        catch ( Exception ignored )
        {
        }
        conectar();
    }

    // ===========================================================
    // Garantir conexão ativa
    // ===========================================================
    public synchronized Connection getConnectionAtiva()
    {
        try
        {
            if ( connection == null || connection.isClosed() || !connection.isValid( 2 ) )
            {
                reconectar();
            }
        }
        catch ( SQLException e )
        {
            reconectar();
        }
        return connection;
    }

    // ===========================================================
    // EXECUTE QUERY (seguro e isolado)
    // ===========================================================
    public synchronized ResultSet executeQuery( String query )
    {
        int tentativas = 2;
        while ( tentativas-- > 0 )
        {
            try
            {
                Connection conn = getConnectionAtiva();
                Statement st = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
                ResultSet rs = st.executeQuery( query );
                return rs;

            }
            catch ( SQLException ex )
            {
                if ( ex.getMessage().contains( "Communications link failure" )
                        || ex.getMessage().contains( "statement closed" ) )
                {
                    System.out.println( "[BDConexao] 🔄 Tentando repetir query após falha..." );
                    reconectar();
                    continue;
                }
                showMessage( "Erro ao executar consulta: " + ex.getMessage() );
                break;
            }
        }
        return null;
    }

    // ===========================================================
    // EXECUTE UPDATE (seguro e isolado)
    // ===========================================================
    public synchronized boolean executeUpdate( String query )
    {
        int tentativas = 2;
        while ( tentativas-- > 0 )
        {
            try
            {
                Connection conn = getConnectionAtiva();
                try ( Statement st = conn.createStatement() )
                {
                    st.executeUpdate( query );
                }
                return true;

            }
            catch ( SQLException ex )
            {
                if ( ex.getMessage().contains( "Communications link failure" )
                        || ex.getMessage().contains( "statement closed" ) )
                {
                    System.out.println( "[BDConexao] 🔄 Repetindo update após falha..." );
                    reconectar();
                    continue;
                }
                showMessage( "Erro ao atualizar dados: " + ex.getMessage() );
                break;
            }
        }
        return false;
    }

    // ===========================================================
    // PREPARED STATEMENT
    // ===========================================================
    public synchronized PreparedStatement getPreparedStatement( String sql ) throws SQLException
    {
        return getConnectionAtiva().prepareStatement( sql );
    }

    public static PreparedStatement prepareStatement( String sql ) throws SQLException
    {
        return getInstancia().getPreparedStatement( sql );
    }

    // ===========================================================
    // FECHAR CONEXÃO
    // ===========================================================
    public synchronized void close()
    {
        try
        {
            if ( connection != null && !connection.isClosed() )
            {
                connection.close();
            }
            System.out.println( "[BDConexao] 🔒 Conexão encerrada." );
        }
        catch ( SQLException ex )
        {
            showMessage( "Erro ao fechar conexão: " + ex.getMessage() );
        }
    }

    // ===========================================================
    // LER FICHEIRO DE CREDENCIAIS
    // ===========================================================
    private Vector<String> leituraFicheiroSeguro()
    {
        Vector<String> dados = new Vector<>();
        File arquivo = new File( "credencial" + File.separator + "file.txt" );

        if ( !arquivo.exists() )
        {
            String basePath = new File( "" ).getAbsolutePath();
            arquivo = new File( basePath + File.separator + "credencial" + File.separator + "file.txt" );
        }

        if ( !arquivo.exists() )
        {
            System.err.println( "[BDConexao] ⚠ Ficheiro não encontrado em: " + arquivo.getAbsolutePath() );
            return dados;
        }

        try ( BufferedReader br = new BufferedReader( new FileReader( arquivo ) ) )
        {
            String linha;
            while ( ( linha = br.readLine() ) != null )
            {
                if ( !linha.trim().isEmpty() )
                {
                    dados.add( linha.trim() );
                }
            }
        }
        catch ( IOException e )
        {
            System.err.println( "[BDConexao] ⚠ Erro ao ler ficheiro: " + e.getMessage() );
        }

        return dados;
    }

    // ===========================================================
    // Métodos compatíveis antigos
    // ===========================================================
    public static BDConexao getBDConetion()
    {
        return getInstancia();
    }

    public static Connection getConexao() throws SQLException
    {
        return getInstancia().getConnectionAtiva();
    }

//    public static com.mysql.jdbc.Connection getConnection() {
//    public static java.sql.Connection getConnection()
//    {
//        try
//        {
//            return (com.mysql.jdbc.Connection) getInstancia().getConnectionAtiva();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    public static com.mysql.jdbc.Connection getConnection() {
//    try {
//        return (com.mysql.jdbc.Connection) getInstancia().getConnectionAtiva();
//    } catch (Exception e) {
//        e.printStackTrace();
//        return null;
//    }
//}
    public static java.sql.Connection getConnection()
    {
        try
        {
            return getInstancia().getConnectionAtiva();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }
    
    

    // ===========================================================
    // Helper para mensagens Swing
    // ===========================================================
    public static void showMessage( String mensagem )
    {
        SwingUtilities.invokeLater( ()
                -> JOptionPane.showMessageDialog( null, mensagem )
        );
    }

//    public static void showMessage( String mensagen )
//    {
//        JOptionPane.showMessageDialog( null, mensagen );
//
//    }
    // metodo criado para a conexao a utilizar na geracao dos relatorios
    public Vector<ProdutoModelo> getAllProdutosLOCALHOST_BY_ID_CATEGORIA( int idCategoria )
    {

        String sql = "SELECT * FROM tb_produto WHERE cod_Tipo_Produto = " + idCategoria;
        ResultSet rs = executeQuery( sql );
        Vector<ProdutoModelo> vector = new Vector();

        ProdutoModelo produtoModelo;

        try
        {
            while ( rs.next() )
            {

                produtoModelo = new ProdutoModelo();
                produtoModelo.setCodigo( rs.getInt( "codigo" ) );
                produtoModelo.setDesignacao( rs.getString( "designacao" ) );
                produtoModelo.setPreco( rs.getFloat( "preco" ) );
                produtoModelo.setCod_Tipo_Produto( rs.getInt( "cod_Tipo_Produto" ) );
                produtoModelo.setCod_fornecedores( rs.getInt( "cod_fornecedores" ) );
                produtoModelo.setData_fabrico( rs.getString( "data_fabrico" ) );
                produtoModelo.setData_expiracao( rs.getString( "data_expiracao" ) );
                produtoModelo.setCod_barra( rs.getString( "codBarra" ) );
                produtoModelo.setData_entrada( rs.getString( "data_entrada" ) );
                produtoModelo.setStocavel( rs.getString( "stocavel" ) );
                produtoModelo.setStatus( rs.getString( "status" ) );

                vector.add( produtoModelo );
            }

            /*Adicionar  Reservas */
            for ( int i = 0; i < 40; i++ )
            {
                ProdutoModelo produto_reserva = new ProdutoModelo( 0, idCategoria, 1, "Reserva Neste  " + ( i + 1 ), "2014-01-01", "2014-01-01", "0", 0 );
                produto_reserva.setData_entrada( "2014-01-01" );
                produto_reserva.setStatus( "Activo" );
                produto_reserva.setStocavel( "true" );
                vector.add( produto_reserva );
            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return vector;
        }

        return vector;

    }

    public static Vector<ItemPermissaoModelo> getItemPermissaoByIdUser( int idUser ) throws SQLException
    {

        String query = "SELECT * FROM tb_item_permissao  WHERE idUsuario = " + idUser;
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;

        ItemPermissaoModelo itemPermissaoModelo;

        Vector<ItemPermissaoModelo> vector = new Vector<ItemPermissaoModelo>();

        try
        {

            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement( query );
            resultado = comando.executeQuery();

            while ( resultado.next() )
            {

                itemPermissaoModelo = new ItemPermissaoModelo();

                itemPermissaoModelo.setIdUsuario( resultado.getInt( "idUsuario" ) );
                System.out.println( "CODIGO ITEM " + resultado.getInt( "idPermissao" ) );
                itemPermissaoModelo.setIdPermissao( resultado.getInt( "idPermissao" ) );
                vector.add( itemPermissaoModelo );
            }
            conexao.close();
            return vector;

        }
        catch ( SQLException ex )
        {

            //int idUsuario, int idMatricula, int idTurma, int idMes, int idAno, String obs, String dataConfirmacao
            return null;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }

    }

    public static int getCodigoPermissao( String descricao )
    {

        String sql = "SELECT idPermissao FROM tb_permissao WHERE descricao = '" + descricao + "'";
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;

        try
        {
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement( sql );
            resultado = comando.executeQuery();

            System.out.println( sql );
            if ( resultado.next() )
            {
                return resultado.getInt( "idPermissao" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    public int getCodigoPermissaoPublico( String descricao )
    {

        String sql = "SELECT idPermissao FROM tb_permissao WHERE descricao = '" + descricao + "'";
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;

        try
        {
            conexao = connection;
            comando = conexao.prepareStatement( sql );
            resultado = comando.executeQuery();

            System.out.println( sql );
            if ( resultado.next() )
            {
                return resultado.getInt( "idPermissao" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de aluno matriculado
    public static String getDesignacaoPermissao( int idPermissao )
    {

        String sql = "SELECT descricao FROM tb_permissao WHERE idPermissao = " + idPermissao;

        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;

        try
        {
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement( sql );
            resultado = comando.executeQuery();

            if ( resultado.next() )
            {

                return resultado.getString( "descricao" );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }

        return null;
    }

    public Vector getElementosLike( String tabela, String campo, String prefixo )
    {
        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE(" + campo + " LIKE '" + prefixo + "%') order by " + campo;

        ResultSet rs = executeQuery( sql );
        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( campo ) );
            }
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector getElementosLikeManual( String tabela, String apresenta, String campo, String prefixo )
    {
        String sql = "SELECT " + apresenta + " FROM " + tabela + " WHERE(" + campo + " LIKE '" + prefixo + "%') order by " + campo;

        ResultSet rs = executeQuery( sql );
        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( apresenta ) );
            }
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector getElementosProdutoStockLike( String desigancao )
    {

        String sql = "SELECT p.designacao  FROM tb_stock s, tb_produto p WHERE s.cod_produto_codigo = p.codigo  AND p.designacao LIKE '" + desigancao + "%'";

        ResultSet rs = executeQuery( sql );
        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( "p.designacao" ) );
            }
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector getElementosProdutoStock( int idTipoProduto )
    {

        String sql = "SELECT p.designacao  FROM tb_stock s, tb_produto p, tb_tipo_produto tp WHERE s.cod_produto_codigo = p.codigo  AND tp.codigo = p.cod_Tipo_Produto AND  p.cod_Tipo_Produto = " + idTipoProduto;

        ResultSet rs = executeQuery( sql );
        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( "p.designacao" ) );
            }
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector<ItemVendaModelo> getAllItemVendaByCodigo( Integer cod_venda )
    {

        String sql = "SELECT * FROM tb_item_venda WHERE codigo_venda = " + cod_venda;

        ResultSet rs = executeQuery( sql );
        Vector<ItemVendaModelo> vector = new Vector();

        ItemVendaModelo itemVendaModelo;

        try
        {
            while ( rs.next() )
            {

                itemVendaModelo = new ItemVendaModelo();

                itemVendaModelo.setCodigo_venda( rs.getInt( "codigo_venda" ) );

                itemVendaModelo.setCodigo_produto( rs.getInt( "codigo_produto" ) );
                itemVendaModelo.setTotal( rs.getInt( "total" ) );
                itemVendaModelo.setQuantidade( rs.getInt( "quantidade" ) );

                vector.add( itemVendaModelo );
            }
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector getElementos( String tabela, String campo, boolean tem_status )
    {
        String sql = "";

        if ( tem_status )
        {
            sql = "SELECT " + campo + " FROM " + tabela + " where Status = 'Activo' Order By " + campo;
        }
        else
        {
            sql = "SELECT " + campo + " FROM " + tabela + " Order By " + campo;
        }
        ResultSet rs = executeQuery( sql );

        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( campo ).trim() );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    //devolve o codigo de uma determinada tabela
    public static int getCodigo( String tabela, String descricao )
    {
        String sql = "SELECT codigo FROM " + tabela + " WHERE(designacao = '" + descricao.trim() + "')";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    public int getCodigoPublico1( String tabela, String descricao )
    {

        String sql = "SELECT codigo FROM " + tabela + " WHERE(nome = '" + descricao.trim() + "')";

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public int getCodigoPublico( String tabela, String descricao )
    {

        String sql = "SELECT codigo FROM " + tabela + " WHERE(designacao = '" + descricao.trim() + "')";
        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static int getCodigoTipousuario( String descricao )
    {
        String sql = "SELECT idTipoUsuario FROM tb_tipo_usuario WHERE(descricao = '" + descricao.trim() + "')";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "idTipoUsuario" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static double getTotalVendas()
    {

        String sql = "SELECT SUM(total_venda) FROM tb_venda  WHERE credito <> true";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getFloat( "SUM(total_venda)" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static double getTotalVendasCreditos()
    {

        String sql = "SELECT SUM(total_venda) FROM tb_venda   WHERE credito <> false ";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getFloat( "SUM(total_venda)" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static int getQuantidade_minima( int codigo, int codigo_armzem )
    {

        String sql = "SELECT quant_baixa FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armzem;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quant_baixa" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public double getQuantidade_minima_publico( int codigo, int id_armazem )
    {

        String sql = "SELECT quant_baixa FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + id_armazem;

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quant_baixa" );
            }
        }
        catch ( SQLException ex )
        {
            return 0d;
        }

        return 0d;
    }

    //devolve o codigo de uma determinada tabela
    public static int getQuantidade_Total( String data_inicio, String data_fim )
    {

//        String sql = "SELECT  SUM(t.quantidade) AS SOMA_QUANTIDADE " 
//                   + "FROM tb_item_venda t , tb_venda v , tb_produto p WHERE  t.codigo_venda = v.codigo  AND p.codigo = t.codigo_produto "
//                   + "AND  v.dataVenda BETWEEN '" +data_inicio 
//                   + "'  AND '"   +data_fim +"' " 
//                   + " GROUP BY  t.codigo_produto";
        String sql = "SELECT t.quantidade FROM tb_item_venda t, tb_venda v, tb_produto p "
                + "WHERE t.codigo_venda = v.codigo "
                + " AND p.codigo = t.codigo_produto "
                + " AND  v.dataVenda BETWEEN '" + data_inicio + "' AND '" + data_fim + "'";

        System.out.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        Integer soma_total = 0;

        try
        {
            while ( rs.next() )
            {
                soma_total = soma_total + rs.getInt( "t.quantidade" );
            }

        }
        catch ( SQLException ex )
        {
            return soma_total;
        }

        return soma_total;
    }

    //devolve o codigo de uma determinada tabela
    public static double getQuantidadeTotalGeral( String data_inicio, String data_fim )
    {

//        String sql = "SELECT  SUM(t.total) AS SOMA_TOTAL" 
//                   + "FROM tb_item_venda t , tb_venda v , tb_produto p WHERE t.codigo_venda = v.codigo AND p.codigo = t.codigo_produto "
//                   + "AND  v.dataVenda BETWEEN '" +data_inicio 
//                   + "' AND '"   +data_fim +"' " 
//                   + " GROUP BY t.codigo_produto";
        String sql = "SELECT t.total FROM tb_item_venda t, tb_venda v, tb_produto p "
                + "WHERE t.codigo_venda = v.codigo "
                + " AND p.codigo = t.codigo_produto "
                + " AND  v.dataVenda BETWEEN '" + data_inicio + "' AND '" + data_fim + "'";

        System.out.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        Integer soma_total = 0;

        try
        {
            while ( rs.next() )
            {
                soma_total = soma_total + rs.getInt( "t.total" );
            }

        }
        catch ( SQLException ex )
        {
            return soma_total;
        }

        return soma_total;
    }

    //devolve o codigo de uma determinada tabela
    public static int getQuantidade_critica( int codigo, int codigo_armzem )
    {

        String sql = "SELECT quant_critica FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armzem;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quant_critica" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public int getQuantidade_critica_public( int codigo, int codigo_armzem )
    {

        String sql = "SELECT quant_critica FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armzem;

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quant_critica" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static int getQuantidade_Existente( int codigo, int codigo_armazem )
    {

        String sql = "SELECT quantidade_existente FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armazem;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }

        return 0;
    }

    //devolve o codigo de uma determinada tabela
    public static boolean existe_produto_armazem( int codigo, int codigo_armazem )
    {

        String sql = "SELECT cod_produto_codigo FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armazem;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( SQLException ex )
        {
            return false;
        }

        return false;
    }

    //devolve o codigo de uma determinada tabela
    public static TbStock getStockByIdProdutoAndIdArmazem( int codigo_produto, int codigo_armazem, BDConexao conexao )
    {

        String sql = "SELECT * FROM tb_stock WHERE cod_produto_codigo = " + codigo_produto + " AND cod_armazem = " + codigo_armazem;

        ResultSet rs = conexao.executeQuery( sql );
        TbStock stock = null;
        try
        {
            if ( rs.next() )
            {

                stock = new TbStock();
                stock.setCodigo( rs.getInt( "codigo" ) );
                stock.setCodArmazem( armazemDao.findTbArmazem( rs.getInt( "cod_armazem" ) ) );
                stock.setCodProdutoCodigo( produtoDao.findTbProduto( rs.getInt( "cod_produto_codigo" ) ) );
                stock.setQuantidadeExistente( rs.getDouble( "quantidade_existente" ) );
                stock.setQuantBaixa( rs.getInt( "quant_baixa" ) );
                stock.setQuantCritica( rs.getInt( "quant_critica" ) );

            }
        }
        catch ( SQLException ex )
        {
            return stock;
        }

        return stock;
    }

    //devolve o codigo de uma determinada tabela
    public static boolean existe_produto_stock( int codigo )
    {

        String sql = "SELECT cod_produto_codigo FROM tb_stock WHERE cod_produto_codigo = " + codigo;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( SQLException ex )
        {
            return false;
        }

        return false;
    }

    public static String getDescrisoByCodigoCliente( String tabela, String campo, int codigo )
    {
        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE( codigo = " + codigo + ")";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( campo );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }
        return null;
    }

    //devolve o codigo de uma determinada tabela
    public double getQuantidade_Existente_Publico( int codigo, int codigo_armazem )
    {

        String sql = "SELECT quantidade_existente FROM tb_stock WHERE cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armazem;

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            return 0d;
        }

        return 0d;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public static String getDescrisoByCodigo( String tabela, String campo, int codigo )
    {
        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE( codigo = " + codigo + ")";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( campo );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }
        return null;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getDescrisaoArmazemByCodigo( int codigo )
    {

        String sql = "SELECT  designacao FROM tb_armazem WHERE( codigo = " + codigo + ")";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "designacao" );
            }
        }
        catch ( SQLException ex )
        {
            return "";
        }
        return "";
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public String getDescrisoByCodigoPublico( String tabela, String campo, int codigo )
    {
        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE( codigo = " + codigo + ")";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( campo );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }
        return null;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public static String getDescrisoTipoUsuarioByCodigo( int codigo )
    {

        String sql = "SELECT descricao FROM tb_tipo_usuario  WHERE( idTipoUsuario = " + codigo + ")";

        System.err.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "descricao" );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }
        return null;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public static int getCodigoByCodigo( String tabela, String campo, String campo_proucurado, int codigo )
    {

        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE( " + campo_proucurado + " = " + codigo + " ) ";

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( campo );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public int getCodigoByCodigoPublico( String tabela, String campo, String campo_proucurado, int codigo )
    {

        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE( " + campo_proucurado + " = " + codigo + " ) ";

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( campo );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public int getQuaantidadeExistentePublicoArmazem( int codigo, int codigo_armzem )
    {

        String sql = "SELECT quantidade_existente FROM tb_stock WHERE( cod_produto_codigo = " + codigo + " AND cod_armazem = " + codigo_armzem + " ) ";
        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public int getCodigoStockByCodArmazem( int codigo_produto, int codigo_armazem )
    {

        String sql = "SELECT  codigo FROM   tb_stock  WHERE(    cod_produto_codigo = " + codigo_produto + " AND  cod_armazem = " + codigo_armazem + " ) ";

        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

//        //devolve o campo de uma determinada tabela em funcao do codigo
//    public  int getCodigoByCodigoPublico(String tabela, String campo , String campo_proucurado , int codigo) {
//        
//        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( " +campo_proucurado  + " = "  + codigo +" ) ";
//
//        ResultSet rs = executeQuery(sql);
//
//        try {
//            if (rs.next()) {
//                return rs.getInt(campo);
//            }
//        } catch (SQLException ex) {
//            return 0;
//        }
//        return 0;
//    }
//    
//    
    //devolve o campo de uma determinada tabela em funcao do codigo
    public double getPrecoByCodigoProduto( int codigo_produto )
    {

        String sql = "SELECT preco_venda  FROM tb_stock WHERE cod_produto_codigo = " + codigo_produto;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "preco_venda" );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    public static int getCodigoByName( String tabela, String campoProcurado, String descricao )
    {

        String sql = "SELECT codigo FROM " + tabela + " WHERE(" + campoProcurado + " = '" + descricao.trim() + "')";
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( 1 );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public int getCodigoByNamePublico( String tabela, String campoProcurado, String descricao )
    {

        String sql = "SELECT codigo FROM " + tabela + " WHERE(" + campoProcurado + " = '" + descricao.trim() + "')";
        ResultSet rs = executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( 1 );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static int getCodigoUSuario( String user_name, String senha )
    {

        String sql = "SELECT codigo FROM  tb_usuario WHERE( userName = '" + user_name.trim() + "' AND senha = encript_pass('" + senha + "') )";

        System.out.println( sql );
        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "codigo" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
    //select nome form tb_produto where 

    public Vector getElementos2( String tabela, String coluna, String campo, int codigo )
    {
        String sql = "";

        sql = "SELECT " + coluna + " FROM " + tabela + " WHERE " + campo + " = " + codigo + " Order By " + coluna;

        ResultSet rs = executeQuery( sql );

        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( coluna ).trim() );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector<ExtratoProdutoModelo> getExtratosProduto( int codigo_produto, String data_proucura )
    {
        String sql = "";

        sql = "SELECT  u.nome , SUM(t.quantidade)"
                + " FROM tb_item_venda t, tb_venda v, tb_usuario u, tb_produto p "
                + " WHERE t.codigo_produto = " + codigo_produto
                + " AND t.codigo_venda = v.codigo"
                + " AND u.codigo = v.codigo_usuario "
                + " AND t.codigo_produto = p.codigo "
                + " AND v.dataVenda like '" + data_proucura + "%'"
                + "  GROUP BY v.codigo_usuario";

        ResultSet rs = executeQuery( sql );

        double preco = getPrecoProduto( codigo_produto );
        //double preco = getPrecoByCodigoProduto(codigo_produto);

        ExtratoProdutoModelo extratoProdutoModelo;
        Vector<ExtratoProdutoModelo> vector = new Vector();

        try
        {
            while ( rs.next() )
            {

                extratoProdutoModelo = new ExtratoProdutoModelo();

                extratoProdutoModelo.setNome_usurario( rs.getString( "u.nome" ) );
                extratoProdutoModelo.setQuant_total( rs.getInt( "SUM(t.quantidade)" ) );

                extratoProdutoModelo.setQuant_valor( rs.getInt( "SUM(t.quantidade)" ) * preco );

                vector.add( extratoProdutoModelo );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

    public Vector<EntradaModelo> getEntradaByIntervaloDeData( int idArmazem, String data_1, String data_2 )
    {
        String sql = "";

        System.out.println( " ID ENTRADA " + idArmazem );
        System.out.println( " DATA 1 " + data_1 );
        System.out.println( " DATA 2 " + data_2 );

        sql = "SELECT * FROM tb_entrada WHERE  data_entrada BETWEEN '" + data_1 + " ' AND ' " + data_2 + " ' ";
        // sql = "SELECT * FROM tb_entrada"; 

        ResultSet rs = executeQuery( sql );

        EntradaModelo entradaModelo;
        Vector<EntradaModelo> vector = new Vector();

        try
        {
            while ( rs.next() )
            {

                entradaModelo = new EntradaModelo();

                entradaModelo.setIdEntrada( rs.getInt( "idEntrada" ) );
                entradaModelo.setData_entrada( rs.getString( "data_entrada" ) );
                entradaModelo.setIdArmazemFK( rs.getInt( "idArmazemFK" ) );
                entradaModelo.setIdProduto( rs.getInt( "idProduto" ) );
                entradaModelo.setQuantidade( rs.getInt( "quantidade" ) );
                entradaModelo.setIdUsuario( rs.getInt( "idUsuario" ) );

                System.out.println( "ENTRADA " + entradaModelo.getData_entrada() );

                vector.add( entradaModelo );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return vector;
        }

        // System.out.println(" PRIMEIRO REGISTRO " + vector.get(0).getIdEntrada());
        return vector;
    }
//
//    private void getEntradaByIntervalodeDeData( Date data_1 )
//    {
//
//    }

    public boolean prouduto_stocavel( String stocavel )
    {

        return stocavel.equals( "true" );

    }

    public double getPrecoProduto( int codProduto )
    {

        //
        try
        {

            ProdutoModelo produtoModelo = new ProdutoController( BDConexao.getInstancia() ).getProduto( codProduto );

            if ( prouduto_stocavel( produtoModelo.getStocavel() ) )
            {
                return getPrecoByCodigoProduto( codProduto );
            }
            else
            {
                return produtoModelo.getPreco();
            }

        }
        catch ( SQLException ex )
        {
            Logger.getLogger( BDConexao.class.getName() ).log( Level.SEVERE, null, ex );

            return 0;
        }

    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public double getQtdExistenteStock( int idProduto, int idArmazem )
    {

        String sql = "SELECT   s.quantidade_existente FROM  tb_stock s WHERE  s.cod_produto_codigo = " + idProduto + "  AND  cod_armazem = " + idArmazem;
        ResultSet rs = this.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( 1 );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    public double getQtdCriticaStock( int idProduto, int idArmazem )
    {

        String sql = "SELECT   s.quant_critica FROM  tb_stock s WHERE  s.cod_produto_codigo = " + idProduto + "  AND  cod_armazem = " + idArmazem;
        ResultSet rs = this.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( 1 );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    //devolve o codigo de aluno matriculado
    public static String getDesignacaoPermissao1( int idPermissao )
    {

        String sql = "SELECT descricao FROM tb_permissao WHERE idPermissao = " + idPermissao;

        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;

        try
        {
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement( sql );
            resultado = comando.executeQuery();

            if ( resultado.next() )
            {

                return resultado.getString( "descricao" );
            }
        }
        catch ( SQLException ex )
        {
            return null;
        }

        return null;
    }

    //devolve o campo de uma determinada tabela em funcao do codigo
    public int getQtdBaixaStock( int idProduto, int idArmazem )
    {

        String sql = "SELECT   s.quant_baixa FROM  tb_stock s WHERE  s.cod_produto_codigo = " + idProduto + "  AND  cod_armazem = " + idArmazem;

        ResultSet rs = BDConexao.getInstancia().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( 1 );
            }
        }
        catch ( SQLException ex )
        {
            return 0;
        }
        return 0;
    }

    public static void main( String args[] )
    {
        BDConexao conexao = BDConexao.getInstancia();

//        
    }

    public Vector getElementosEceptoDiversos( String tabela, String campo, boolean tem_status )
    {
        String sql = "";

        if ( tem_status )
        {
            sql = "SELECT " + campo + " FROM " + tabela + " where Status = 'Activo' and codigo <> 1  Order By " + campo;
        }
        else
        {
            sql = "SELECT " + campo + " FROM " + tabela + " where  codigo <> 1  Order By " + campo;
        }
        ResultSet rs = executeQuery( sql );

        Vector vector = new Vector();

        try
        {
            while ( rs.next() )
            {
                vector.add( rs.getString( campo ).trim() );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        }
        catch ( SQLException ex )
        {
            return vector;
        }

        return vector;
    }

}
