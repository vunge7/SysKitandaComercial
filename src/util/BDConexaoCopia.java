package util;


import java.sql.Connection;
/*----------------------------------------------
 *project: SGC
 *fle:	BDConexao.java
 *Desenvolvido por: Domingos Dala Vunge
 *----------------------------------------------*/

import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import modelo.*;

public class BDConexaoCopia {

    private Connection connection;
    private Statement statement;
    private static String ip_address = "localhost";
    private static final String JDBC_URL = "jdbc:mysql://"  +ip_address +":3306/gestao_noticia";    
    private static final String USER_NAME = "root", PASS_WORD = "DoV90x?#";
    public static int tipoUser = 0;

    
    public BDConexaoCopia() 
    {
        
        try {
            //Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USER_NAME, PASS_WORD);
             statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Falhou a Conexao com a Base de Dados");
        }
        
    }

    public static BDConexaoCopia getBDConetion() {
        return new BDConexaoCopia();
    }
    
    // metodo criado para a conexao a utilizar na geracao dos relatorios
    public Vector<ProdutoModelo> getAllProdutosLOCALHOST_BY_ID_CATEGORIA(int idCategoria) {
       
        String sql = "SELECT * FROM tb_produto WHERE cod_Tipo_Produto = " +idCategoria;
        ResultSet rs = executeQuery(sql);
        Vector<ProdutoModelo> vector = new Vector();

        ProdutoModelo produtoModelo;
        
        try {
            while (rs.next()) {
                
                produtoModelo = new ProdutoModelo();
                produtoModelo.setCodigo( rs.getInt("codigo")   );
                produtoModelo.setDesignacao( rs.getString("designacao")   );
                produtoModelo.setPreco( rs.getFloat("preco")   );
                produtoModelo.setCod_Tipo_Produto( rs.getInt("cod_Tipo_Produto")   );
                produtoModelo.setCod_fornecedores( rs.getInt("cod_fornecedores")   );
                produtoModelo.setData_fabrico( rs.getString("data_fabrico")   );
                produtoModelo.setData_expiracao( rs.getString("data_expiracao")   );
                produtoModelo.setCod_barra( rs.getString("codBarra")   );
                produtoModelo.setData_entrada( rs.getString("data_entrada")   );
                produtoModelo.setStocavel( rs.getString("stocavel")   );
                produtoModelo.setStatus(  rs.getString("status")   );
              
                vector.add( produtoModelo );
            }
            
            /*Adicionar  Reservas */
            for (int i = 0; i < 40; i++) {
                
                ProdutoModelo produto_reserva = new ProdutoModelo(0, idCategoria, 1, "Reserva Neste  " +(i+1), "2014-01-01", "2014-01-01", "0", 0);
              produto_reserva.setData_entrada("2014-01-01");
              produto_reserva.setStatus("Activo");
              produto_reserva.setStocavel("true");
                vector.add(produto_reserva);
                
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return vector;
        }

        return vector;
        
    }
       
    
     
   
    
    
    public static Connection conectar()//throws Exception
    {
        Connection mtdoConectar = null;

        try {
           // Class.forName(JDBC_DRIVER);
            mtdoConectar = DriverManager.getConnection(JDBC_URL, USER_NAME, PASS_WORD);

        } catch (SQLException sqlex) {
            showMessage("Erro ao Estabelecer a Conexao:" + sqlex.getMessage());
        }

        return mtdoConectar;
    }
    

    public static com.mysql.jdbc.Connection getConnection() {
       
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return (com.mysql.jdbc.Connection) DriverManager.getConnection( JDBC_URL , USER_NAME, PASS_WORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void finalized() {
        close();
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);

            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Falha ao Carregar os Dados");
        }

        return resultSet;
    }

    public boolean executeUpdate(String query) {
        ResultSet result = null;
        try {
            statement.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Falha ao Actualizar a BD");

            return false;
        }
        return true;
    }

    
    
      
    public static Vector<ItemPermissaoModelo> getItemPermissaoByIdUser(int idUser) throws SQLException 
    {
        String query = "SELECT * FROM tb_item_permissao  WHERE idUsuario = " +idUser;
        
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;   
        
        ItemPermissaoModelo itemPermissaoModelo;
   
        Vector<ItemPermissaoModelo> vector = new Vector<ItemPermissaoModelo>();
        
        try {
            
                conexao = BDConexaoCopia.getConnection();
                comando = conexao.prepareStatement(query);
                resultado = comando.executeQuery();
                
            while (resultado.next()) {
                
                itemPermissaoModelo = new ItemPermissaoModelo();
                
                itemPermissaoModelo.setIdUsuario( resultado.getInt("idUsuario") );
                System.out.println("CODIGO ITEM " +resultado.getInt("idPermissao") );
                itemPermissaoModelo.setIdPermissao(resultado.getInt("idPermissao") );
                vector.add(itemPermissaoModelo);
            }
            conexao.close();
            return vector;

        } catch (SQLException ex) {
            
            //int idUsuario, int idMatricula, int idTurma, int idMes, int idAno, String obs, String dataConfirmacao
            return null;
        }catch (Exception e){
                e.printStackTrace();
                return null;
        }
      
    }
    
    
      public static int getCodigoPermissao(String descricao ) {
        
        String sql = "SELECT idPermissao FROM tb_permissao WHERE descricao = '"+descricao+"'";
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;   
        

        try 
        {
             conexao = BDConexaoCopia.getConnection();
             comando = conexao.prepareStatement(sql);
             resultado = comando.executeQuery();
          
             System.out.println(sql);
            if (resultado.next()) {
                return resultado.getInt("idPermissao");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
       
      public  int getCodigoPermissaoPublico(String descricao ) {
        
        String sql = "SELECT idPermissao FROM tb_permissao WHERE descricao = '"+descricao+"'";
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;   
        

        try 
        {
             conexao = connection;
             comando = conexao.prepareStatement(sql);
             resultado = comando.executeQuery();
          
             System.out.println(sql);
            if (resultado.next()) {
                return resultado.getInt("idPermissao");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
      
       
        //devolve o codigo de aluno matriculado
    public static String getDesignacaoPermissao(int idPermissao ) {
        
        String sql = "SELECT descricao FROM tb_permissao WHERE idPermissao = " +idPermissao;
        
        ResultSet resultado = null;
        PreparedStatement comando = null;
        Connection conexao = null;   
        

        try 
        {
             conexao = BDConexaoCopia.getConnection();
             comando = conexao.prepareStatement(sql);
             resultado = comando.executeQuery();
          
            if (resultado.next()) {
               
                return resultado.getString("descricao");
            }
        } catch (SQLException ex) {
            return null;
        }

        return null;
    }
    
    
    
    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException sqlException) {
            //sqlException.printStackTrace();
            showMessage("Erro ao Fechar a Conexao");
        }
    }

    
    public static void showMessage(String mensagen) 
    {
            JOptionPane.showMessageDialog(null, mensagen);
    
    }
    
    
    public Vector getElementosLike(String tabela, String campo, String prefixo) {
        String sql = "SELECT " + campo + " FROM " + tabela + " WHERE(" + campo + " LIKE '" + prefixo + "%') order by " + campo;

        ResultSet rs = executeQuery(sql);
        Vector vector = new Vector();

        try {
            while (rs.next()) {
                vector.add(rs.getString(campo));
            }
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
    
    
     public Vector getElementosProdutoStockLike(String desigancao) {
       
         String sql = "SELECT p.designacao  FROM tb_stock s, tb_produto p WHERE s.cod_produto_codigo = p.codigo  AND p.designacao LIKE '" + desigancao  +"%'";

        ResultSet rs = executeQuery(sql);
        Vector vector = new Vector();

        try {
            while (rs.next()) {
                vector.add(rs.getString("p.designacao"));
            }
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
    
     
     
      public Vector getElementosProdutoStock(int idTipoProduto) {
       
         String sql = "SELECT p.designacao  FROM tb_stock s, tb_produto p, tb_tipo_produto tp WHERE s.cod_produto_codigo = p.codigo  AND tp.codigo = p.cod_Tipo_Produto AND  p.cod_Tipo_Produto = " + idTipoProduto;

        ResultSet rs = executeQuery(sql);
        Vector vector = new Vector();

        try {
            while (rs.next()) {
                vector.add(rs.getString("p.designacao"));
            }
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
     
     
     
     public Vector<ItemVendaModelo> getAllItemVendaByCodigo(Integer cod_venda) {
       
         String sql = "SELECT * FROM tb_item_venda WHERE codigo_venda = " +cod_venda;

        ResultSet rs = executeQuery(sql);
        Vector<ItemVendaModelo> vector = new Vector();

        ItemVendaModelo itemVendaModelo;
        
        try {
            while (rs.next()) {
                
                itemVendaModelo = new ItemVendaModelo();

                itemVendaModelo.setCodigo_venda(rs.getInt("codigo_venda"));
                
                itemVendaModelo.setCodigo_produto(rs.getInt("codigo_produto"));
                itemVendaModelo.setTotal(rs.getInt("total"));
                itemVendaModelo.setQuantidade(rs.getInt("quantidade"));
                
                vector.add( itemVendaModelo );
            }
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
     
     
    
     
     public Vector getElementos(String tabela, String campo, boolean tem_status) {
        String sql = "";

        if (tem_status) 
        {
            sql = "SELECT " + campo + " FROM " + tabela + " where Status = 'Activo' Order By " + campo;
        } else {
            sql = "SELECT " + campo + " FROM " + tabela + " Order By " + campo;
        }
        ResultSet rs = executeQuery(sql);

        Vector vector = new Vector();

        try {
            while (rs.next()) {
                vector.add(rs.getString(campo).trim());
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }

      //devolve o codigo de uma determinada tabela
    public static int getCodigo(String tabela, String descricao) {
        String sql = "SELECT codigo FROM " + tabela + " WHERE(designacao = '" + descricao.trim() + "')";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
            public  int getCodigoPublico1(String tabela, String descricao) {
        
        String sql = "SELECT codigo FROM " + tabela + " WHERE(nome = '" + descricao.trim() + "')";

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
    
       //devolve o codigo de uma determinada tabela
    public  int getCodigoPublico(String tabela, String descricao) {
        
        String sql = "SELECT codigo FROM " + tabela + " WHERE(designacao = '" + descricao.trim() + "')";

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }

    
      //devolve o codigo de uma determinada tabela
    public static int getCodigoTipousuario(String descricao) {
        String sql = "SELECT idTipoUsuario FROM tb_tipo_usuario WHERE(descricao = '" + descricao.trim() + "')";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("idTipoUsuario");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
      //devolve o codigo de uma determinada tabela
    public static double getTotalVendas() {
        
        String sql = "SELECT SUM(total_venda) FROM tb_venda  WHERE credito <> true";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getFloat("SUM(total_venda)");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
        //devolve o codigo de uma determinada tabela
    public static double getTotalVendasCreditos() {
        
        String sql = "SELECT SUM(total_venda) FROM tb_venda   WHERE credito <> false ";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getFloat("SUM(total_venda)");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
    
    
      //devolve o codigo de uma determinada tabela
    public static int getQuantidade_minima(int codigo, int codigo_armzem) {
        
        String sql = "SELECT quant_baixa FROM tb_stock WHERE cod_produto_codigo = " +codigo + " AND cod_armazem = " + codigo_armzem;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quant_baixa");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    

       //devolve o codigo de uma determinada tabela
    public  int getQuantidade_minima_publico(int codigo, int id_armazem) {
        
        String sql = "SELECT quant_baixa FROM tb_stock WHERE cod_produto_codigo = " +codigo +" AND cod_armazem = " + id_armazem;

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quant_baixa");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
       //devolve o codigo de uma determinada tabela
    public static int getQuantidade_Total(String data_inicio, String data_fim) {
        
        
//        String sql = "SELECT  SUM(t.quantidade) AS SOMA_QUANTIDADE " 
//                   + "FROM tb_item_venda t , tb_venda v , tb_produto p WHERE  t.codigo_venda = v.codigo  AND p.codigo = t.codigo_produto "
//                   + "AND  v.dataVenda BETWEEN '" +data_inicio 
//                   + "'  AND '"   +data_fim +"' " 
//                   + " GROUP BY  t.codigo_produto";

       String sql ="SELECT t.quantidade FROM tb_item_venda t, tb_venda v, tb_produto p "
                  + "WHERE t.codigo_venda = v.codigo "
                  + " AND p.codigo = t.codigo_produto "
                  + " AND  v.dataVenda BETWEEN '" +data_inicio +  "' AND '" +data_fim +"'";
        
        System.out.println(sql);
        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        Integer soma_total = 0;
        
        try {
            while (rs.next()) {
                soma_total = soma_total + rs.getInt("t.quantidade");
            }
            
        } catch (SQLException ex) {
            return soma_total;
        }

        return soma_total;
    }
    
    
    
       //devolve o codigo de uma determinada tabela
    public static double getQuantidadeTotalGeral(String data_inicio, String data_fim) {
        
//        String sql = "SELECT  SUM(t.total) AS SOMA_TOTAL" 
//                   + "FROM tb_item_venda t , tb_venda v , tb_produto p WHERE t.codigo_venda = v.codigo AND p.codigo = t.codigo_produto "
//                   + "AND  v.dataVenda BETWEEN '" +data_inicio 
//                   + "' AND '"   +data_fim +"' " 
//                   + " GROUP BY t.codigo_produto";
        
        String sql ="SELECT t.total FROM tb_item_venda t, tb_venda v, tb_produto p "
                  + "WHERE t.codigo_venda = v.codigo "
                  + " AND p.codigo = t.codigo_produto "
                  + " AND  v.dataVenda BETWEEN '" +data_inicio +  "' AND '" +data_fim +"'";

        System.out.println(sql);
        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        Integer soma_total = 0;
        
        try {
            while (rs.next()) {
                soma_total = soma_total + rs.getInt("t.total");
            }
            
        } catch (SQLException ex) {
            return soma_total;
        }

        return soma_total;
    }
    
    
    
      //devolve o codigo de uma determinada tabela
    public static int getQuantidade_critica(int codigo, int codigo_armzem) {
        
        String sql = "SELECT quant_critica FROM tb_stock WHERE cod_produto_codigo = " +codigo + " AND cod_armazem = " +codigo_armzem;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quant_critica");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
        //devolve o codigo de uma determinada tabela
    public  int getQuantidade_critica_public(int codigo, int codigo_armzem) {
        
        String sql = "SELECT quant_critica FROM tb_stock WHERE cod_produto_codigo = "+codigo + " AND cod_armazem = " +codigo_armzem;

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quant_critica");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
       //devolve o codigo de uma determinada tabela
    public static int getQuantidade_Existente(int codigo, int codigo_armazem) {
        
        String sql = "SELECT quantidade_existente FROM tb_stock WHERE cod_produto_codigo = "+codigo  +" AND cod_armazem = "+codigo_armazem;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quantidade_existente");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    
    
       //devolve o codigo de uma determinada tabela
    public static boolean existe_produto_armazem(int codigo, int codigo_armazem) {
        
        String sql = "SELECT cod_produto_codigo FROM tb_stock WHERE cod_produto_codigo = "+codigo  +" AND cod_armazem = "+codigo_armazem;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }

        return false;
    }
    
    
    
       //devolve o codigo de uma determinada tabela
    public static boolean existe_produto_stock(int codigo) {
        
        String sql = "SELECT cod_produto_codigo FROM tb_stock WHERE cod_produto_codigo = "+codigo;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }

        return false;
    }
    
            public static String getDescrisoByCodigoCliente(String tabela, String campo ,int codigo) {
        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( codigo = " + codigo +")";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getString(campo);
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }
    
    
       //devolve o codigo de uma determinada tabela
    public  int getQuantidade_Existente_Publico(int codigo, int codigo_armazem) {
        
        String sql = "SELECT quantidade_existente FROM tb_stock WHERE cod_produto_codigo = "+codigo +" AND cod_armazem = "+codigo_armazem;

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quantidade_existente");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
       //devolve o campo de uma determinada tabela em funcao do codigo
    public static String getDescrisoByCodigo(String tabela, String campo ,int codigo) {
        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( codigo = " + codigo +")";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getString(campo);
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }
    
    
    
     //devolve o campo de uma determinada tabela em funcao do codigo
    public  String getDescrisaoArmazemByCodigo( int codigo) {
        
        String sql = "SELECT  designacao FROM tb_armazem WHERE( codigo = " + codigo +")";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getString("designacao");
            }
        } catch (SQLException ex) {
            return "";
        }
        return "";
    }
    
    
    
    
    
         //devolve o campo de uma determinada tabela em funcao do codigo
    public  String getDescrisoByCodigoPublico(String tabela, String campo ,int codigo) {
        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( codigo = " + codigo +")";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getString(campo);
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }
    
      //devolve o campo de uma determinada tabela em funcao do codigo
    public static String getDescrisoTipoUsuarioByCodigo(int codigo) {
        
        
        String sql = "SELECT descricao FROM tb_tipo_usuario  WHERE( idTipoUsuario = " + codigo +")";

        System.err.println(sql);
        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getString("descricao");
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }
    
    
    
       //devolve o campo de uma determinada tabela em funcao do codigo
    public static int getCodigoByCodigo(String tabela, String campo , String campo_proucurado , int codigo) {
        
        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( " +campo_proucurado  + " = "  + codigo +" ) ";

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt(campo);
            }
        } catch (SQLException ex) {
            return 0;
        }
        return 0;
    }
    
       //devolve o campo de uma determinada tabela em funcao do codigo
    public  int getCodigoByCodigoPublico( String tabela, String campo , String campo_proucurado , int codigo ) {
        
        String sql = "SELECT " + campo +" FROM " + tabela + " WHERE( " +campo_proucurado  + " = "  + codigo +" ) ";

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt(campo);
            }
        } catch (SQLException ex) {
            return 0;
        }
        return 0;
    }
    
    
    
       //devolve o campo de uma determinada tabela em funcao do codigo
    public  int getQuaantidadeExistentePublicoArmazem( int codigo, int codigo_armzem ) {
        
        String sql = "SELECT quantidade_existente FROM tb_stock WHERE( cod_produto_codigo = "  + codigo +" AND cod_armazem = "  + codigo_armzem + " ) ";
        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("quantidade_existente");
            }
        } catch (SQLException ex) {
            return 0;
        }
        return 0;
    }
    
    
    
       //devolve o campo de uma determinada tabela em funcao do codigo
    public  int getCodigoStockByCodArmazem(   int codigo_produto, int codigo_armazem ) {
        
        String sql = "SELECT  codigo FROM   tb_stock  WHERE(    cod_produto_codigo = "  + codigo_produto +  " AND  cod_armazem = "  +codigo_armazem  + " ) ";

        ResultSet rs = executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
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
    public  double getPrecoByCodigoProduto(int codigo_produto){
        
        String sql = "SELECT preco_venda  FROM tb_stock WHERE cod_produto_codigo = " +codigo_produto;

        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getDouble("preco_venda");
            }
        } catch (SQLException ex) {
            return 0;
        }
        return 0;
    }
    
    
    
     public static int getCodigoByName(String tabela, String campoProcurado, String descricao) {
         
        String sql = "SELECT codigo FROM " + tabela + " WHERE(" + campoProcurado + " = '" + descricao.trim() + "')";
        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try 
        {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
     
      public  int getCodigoByNamePublico(String tabela, String campoProcurado, String descricao) {
         
        String sql = "SELECT codigo FROM " + tabela + " WHERE(" + campoProcurado + " = '" + descricao.trim() + "')";
        ResultSet rs = executeQuery(sql);

        try 
        {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
     
     
        public static int getCodigoUSuario( String user_name, String senha) {
         
        String sql = "SELECT codigo FROM  tb_usuario WHERE( userName = '" + user_name.trim() + "' AND senha = '" +senha +"' )";
       
        System.out.println(sql);
        ResultSet rs = new BDConexaoCopia().executeQuery(sql);

        try 
        {
            if (rs.next()) {
                return rs.getInt("codigo");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
     //select nome form tb_produto where 
     
    public Vector getElementos2(String tabela, String coluna, String campo, int codigo  ) {
        String sql = "";

        
        sql = "SELECT " + coluna + " FROM " + tabela + " WHERE " + campo + " = "  +codigo   +" Order By " + coluna;
        
        ResultSet rs = executeQuery(sql);

        Vector vector = new Vector();

        try {
            while (rs.next()) {
                vector.add(rs.getString(coluna).trim());
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
     
     
    
      public Vector<ExtratoProdutoModelo> getExtratosProduto(int codigo_produto, String data_proucura) {
        String sql = "";

        sql = "SELECT  u.nome , SUM(t.quantidade)" 
             +" FROM tb_item_venda t, tb_venda v, tb_usuario u, tb_produto p " 
             +" WHERE t.codigo_produto = " +codigo_produto 
             +" AND t.codigo_venda = v.codigo" 
             +" AND u.codigo = v.codigo_usuario "
             +" AND t.codigo_produto = p.codigo " 
             +" AND v.dataVenda like '" +data_proucura  +"%'" +
             "  GROUP BY v.codigo_usuario";
     
        ResultSet rs = executeQuery(sql);

        double preco = getPrecoProduto(codigo_produto);
        //double preco = getPrecoByCodigoProduto(codigo_produto);
        
        ExtratoProdutoModelo extratoProdutoModelo;
        Vector<ExtratoProdutoModelo> vector = new Vector();

        try {
            while (rs.next()) {
                
                 extratoProdutoModelo =  new ExtratoProdutoModelo();
                
                 extratoProdutoModelo.setNome_usurario( rs.getString("u.nome") );
                 extratoProdutoModelo.setQuant_total(  rs.getInt("SUM(t.quantidade)")  );
                 
                 extratoProdutoModelo.setQuant_valor( rs.getInt("SUM(t.quantidade)") * preco  );
                
                vector.add( extratoProdutoModelo );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        } catch (SQLException ex) {
            return vector;
        }

        return vector;
    }
     
      
      
        public Vector<EntradaModelo> getEntradaByIntervaloDeData(int idArmazem, String data_1, String data_2)
        {
        String sql = "";

        
        
            System.out.println(" ID ENTRADA " +idArmazem     );
            System.out.println(" DATA 1 " +data_1     );
            System.out.println(" DATA 2 " +data_2     );
        
        
       sql = "SELECT * FROM tb_entrada WHERE  data_entrada BETWEEN '" +data_1 +" ' AND ' " +data_2 + " ' "; 
       // sql = "SELECT * FROM tb_entrada"; 
            
     
        ResultSet rs = executeQuery(sql);

      
        EntradaModelo entradaModelo;
        Vector<EntradaModelo> vector = new Vector();

        try {
            while (rs.next()) {
                
                 entradaModelo =  new EntradaModelo();
                
                 entradaModelo.setIdEntrada(rs.getInt("idEntrada") );
                 entradaModelo.setData_entrada( rs.getString("data_entrada") );
                 entradaModelo.setIdArmazemFK( rs.getInt("idArmazemFK") );
                 entradaModelo.setIdProduto( rs.getInt("idProduto") );
                 entradaModelo.setQuantidade( rs.getInt("quantidade") );
                 entradaModelo.setIdUsuario(rs.getInt("idUsuario") );
                 
                 System.out.println("ENTRADA " +entradaModelo.getData_entrada());
                
               
                
                vector.add( entradaModelo );
            }
            //JOptionPane.showMessageDialog(null, "Passou Aqui Tabela = " + tabela + "\nCampo = " + campo);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return vector;
        }

        
           // System.out.println(" PRIMEIRO REGISTRO " + vector.get(0).getIdEntrada());
        
        
        return vector;
    }
    
     public static Connection getConexao() throws SQLException {
             return  DriverManager.getConnection(JDBC_URL, USER_NAME, PASS_WORD);
    }
    
        
       private void getEntradaByIntervalodeDeData(Date data_1)
       {
       
       }
        
        
      
   public boolean prouduto_stocavel(String stocavel){
                    
             return stocavel.equals("true");
               
   
   
   }
      
      
   
    public  double getPrecoProduto(int codProduto){
       
//        //
//        try {
//            BDConexaoCopia conexao1 = new BDConexaoCopia();
//             
//            ProdutoModelo produtoModelo = new ProdutoController( conexao1 ).getProduto(codProduto);
//            
//            if(prouduto_stocavel(produtoModelo.getStocavel()))
//                      return getPrecoByCodigoProduto(codProduto);
//            else return produtoModelo.getPreco();
//   
//            
//        } catch (Exception ex) {
//               Logger.getLogger(BDConexaoCopia.class.getName()).log(Level.SEVERE, null, ex);
//                  
//               return 0;   
//        }
//   
//    
        return 0;       
    }
    
     
     
     
     
    
   
    public static void main(String args[])
    {
        BDConexaoCopia conexao = new BDConexaoCopia();
        
//         Vector<ExtratoProdutoModelo> vector = conexao.getExtratosProduto(5, "2014-01-17");
//        
//         
//         for (ExtratoProdutoModelo extratoProdutoModelo : vector) {
//                  
//             System.err.println("NOME USUARIO "  +extratoProdutoModelo.getNome_usurario());
//             System.err.println("QUANTIDADE  "  +extratoProdutoModelo.getQuant_total());
//             System.err.println("VALOR  "  +extratoProdutoModelo.getQuant_total() * 10);
//             System.err.println("----------------------------------------------------------");
//        }
//        
        
        
        
    }
    
    

}
