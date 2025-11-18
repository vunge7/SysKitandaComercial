package util;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;

import static util.DVML.CAMINHO_SCRIP_TO_UPDATE;
import static util.DVML.FILE_TO_UPDATE;

/**
 * Classe DefinicoesUtil — refatorada para Java 8. - Fechamento correto de
 * resources - Validação de schema (whitelist) - Mensagens: quando já
 * sincronizado e quando sincronização concluída - Geração de script de
 * atualização
 *
 * Obs: Assumo que BDConexao tem getConnection() retornando java.sql.Connection
 * e que MetodosUtil.escreverNoDocumento(String, File) existe no projecto.
 */
public class DefinicoesUtil
{

    private static final Logger LOGGER = Logger.getLogger( DefinicoesUtil.class.getName() );
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    // escape da barra invertida para Java String (funciona em Java 8)
    private static final String PASSWORD_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz=+\\\\-/*";
    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private static final ExecutorService EXEC = Executors.newFixedThreadPool( 4 );

    // --- MAIN DE TESTE ---
    public static void main( String[] args )
    {
        BDConexao conexao = BDConexao.getInstancia();
        String bd_fd = "kitanda_db";
        String bd_fb = "kitanda_db_indiano";

        try
        {
            String script = gerarScript( bd_fd, bd_fb, conexao );
            System.out.println( script );
        }
        catch ( Exception ex )
        {
            LOGGER.log( Level.SEVERE, "Falha ao gerar script", ex );
        }

        int existe = existeTabela( "tb_venda", bd_fb, conexao );
        System.out.println( existe == 0 ? "Não existe" : "Existe" );

        EXEC.shutdown();
    }

    // --- Gerador de senha seguro ---
    public static String gerarSenha()
    {
        StringBuilder sb = new StringBuilder( DEFAULT_PASSWORD_LENGTH );
        for ( int i = 0; i < DEFAULT_PASSWORD_LENGTH; i++ )
        {
            int idx = SECURE_RANDOM.nextInt( PASSWORD_CHARS.length() );
            sb.append( PASSWORD_CHARS.charAt( idx ) );
        }
        return sb.toString();
    }

    // --- Contagem de tabelas ---
    public static int numeroDeTabelas( String bd, BDConexao conexao )
    {
        if ( !isValidSchemaName( bd, conexao ) )
        {
            LOGGER.warning( "Schema não válido: " + bd );
            return 0;
        }
        String sql = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";
        int number = 0;
        try ( Connection conn = conexao.getConnection(); PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setString( 1, bd );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    number = rs.getInt( 1 );
                }
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro em numeroDeTabelas", ex );
        }
        return number;
    }

    // --- Lista de tabelas de um schema ---
    public static Vector<String> getTables( String bd, BDConexao conexao )
    {
        Vector<String> vector_tabelas = new Vector<>();
        if ( !isValidSchemaName( bd, conexao ) )
        {
            LOGGER.warning( "getTables: schema inválido: " + bd );
            return vector_tabelas;
        }

        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";
        try ( Connection conn = conexao.getConnection(); PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setString( 1, bd );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    vector_tabelas.add( rs.getString( 1 ) );
                }
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro em getTables", ex );
        }
        return vector_tabelas;
    }

    // --- Tabelas em falta (bd_1 - bd_2) ---
    public static Vector<String> tabelasEmFalta( String bd_1, String bd_2, BDConexao conexao )
    {
        Vector<String> tabelasEmFalta = new Vector<>();
        Vector<String> tablesBD_1 = getTables( bd_1, conexao );
        Vector<String> tablesBD_2 = getTables( bd_2, conexao );

        for ( String tabela : tablesBD_1 )
        {
            if ( !tablesBD_2.contains( tabela ) )
            {
                tabelasEmFalta.add( tabela );
            }
        }
        return tabelasEmFalta;
    }

    // --- Geração/abertura segura do ficheiro ---
    private static File gerarFicheiro()
    {
        File dir = new File( CAMINHO_SCRIP_TO_UPDATE );
        if ( !dir.exists() )
        {
            if ( !dir.mkdirs() )
            {
                LOGGER.severe( "Falha ao criar diretório: " + dir.getAbsolutePath() );
                return null;
            }
        }
        File file = new File( dir, FILE_TO_UPDATE );
        try
        {
            if ( file.exists() )
            {
                // criar backup em vez de apagar
                File backup = new File( dir, FILE_TO_UPDATE + ".bak" );
                if ( !file.renameTo( backup ) )
                {
                    LOGGER.warning( "Não foi possível renomear ficheiro existente para backup: " + backup.getAbsolutePath() );
                }
            }
            if ( file.createNewFile() )
            {
                LOGGER.info( "Script criado com sucesso: " + file.getAbsolutePath() );
            }
            else
            {
                LOGGER.info( "Ficheiro pronto para uso: " + file.getAbsolutePath() );
            }
        }
        catch ( IOException e )
        {
            LOGGER.log( Level.SEVERE, "Falha ao criar o ficheiro: ", e );
            return null;
        }
        return file;
    }

    // --- Gerar script principal ---
    public static String gerarScript( String bd_1, String bd_2, BDConexao conexao )
    {
        if ( !isValidSchemaName( bd_1, conexao ) || !isValidSchemaName( bd_2, conexao ) )
        {
            throw new IllegalArgumentException( "Schema inválido" );
        }

        StringBuilder out = new StringBuilder();
        File file = gerarFicheiro();
        if ( file == null )
        {
            throw new IllegalStateException( "Não foi possível criar ficheiro de output" );
        }

        Vector<Vector<ColunasFalta>> vectorColunasEmFalta = new Vector<>();
        Vector<Vector<ColunasUpdate>> vectorColunasUpdate = new Vector<>();
        Vector<String> tablesBD_1 = getTables( bd_1, conexao );
        Vector<String> tables = tabelasEmFalta( bd_1, bd_2, conexao );

        out.append( "USE `" ).append( bd_2 ).append( "`;\n\n" );

        LOGGER.info( "TMANHO 1 = " + numeroDeTabelas( bd_1, conexao ) );
        LOGGER.info( "TMANHO 2 = " + numeroDeTabelas( bd_2, conexao ) );

        // preencher vetores de colunas faltando / update
        for ( String tabela : tablesBD_1 )
        {
            if ( existeTabela( tabela, bd_2, conexao ) != 0 )
            {
                String sql1 = getSql( tabela, bd_1 );
                String sql2 = getSql( tabela, bd_2 );

                Vector<EstruturaTabela> e1 = getLinhasTabela( sql1, conexao ); // estrutura tabela BD1
                Vector<EstruturaTabela> e2 = getLinhasTabela( sql2, conexao ); // estrutura tabela BD2

                Vector<ColunasFalta> cf = getColunasEmFaltas( tabela, e1, e2 );
                Vector<ColunasUpdate> cu = getColunasUpdate( tabela, e1, e2 );

                vectorColunasEmFalta.add( cf );
                vectorColunasUpdate.add( cu );
            }
            else
            {
                // tabela não existe em bd_2 -> será tratada em 'tables' (tabelas em falta)
            }
        }

        // Verificar se não há nada para fazer
        boolean semTabelasFaltando = tables.isEmpty();
        boolean semColunasFaltando = true;
        for ( Vector<ColunasFalta> v : vectorColunasEmFalta )
        {
            if ( v != null && !v.isEmpty() )
            {
                semColunasFaltando = false;
                break;
            }
        }
        boolean semColunasParaAtualizar = true;
        for ( Vector<ColunasUpdate> v : vectorColunasUpdate )
        {
            if ( v != null && !v.isEmpty() )
            {
                semColunasParaAtualizar = false;
                break;
            }
        }

        if ( semTabelasFaltando && semColunasFaltando && semColunasParaAtualizar )
        {
            // escreve no ficheiro e informa o utilizador
            MetodosUtil.escreverNoDocumento( "-- AS BASES JÁ ESTÃO TOTALMENTE SINCRONIZADAS --\n", file );
            LOGGER.info( "As bases já estão completamente sincronizadas!" );
            JOptionPane.showMessageDialog( null, "As bases já estão totalmente sincronizadas!", "Informação", JOptionPane.INFORMATION_MESSAGE );
            return "-- AS BASES JÁ ESTÃO TOTALMENTE SINCRONIZADAS --\n";
        }

        // ADIÇÃO DE COLUNAS
        out.append( gerarScriptColumnAdd( bd_2, vectorColunasEmFalta ) );
        // UPDATE DE COLUNAS
        out.append( gerarScriptColumnUpdate( bd_2, vectorColunasUpdate ) );
        // TABELAS EM FALTA (criar)
        for ( int i = 0; i < tables.size(); i++ )
        {
            String elementAt = tables.elementAt( i );
            String sql = getSql( elementAt, bd_1 );
            Vector<EstruturaTabela> e = getLinhasTabela( sql, conexao );
            out.append( gerarScriptTable( elementAt, e ) ).append( "\n\n" );
        }

        // Escrever no ficheiro (usa MetodosUtil existente)
        MetodosUtil.escreverNoDocumento( out.toString(), file );

        // informar que sincronização terminou
        LOGGER.info( "SINCRONIZAÇÃO COMPLETA!" );
        JOptionPane.showMessageDialog( null, "Sincronização concluída com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE );

        return out.toString();
    }

    // --- Geração de SQL para criação de tabela com base na estrutura ---
    private static String gerarScriptTable( String tabela, Vector<EstruturaTabela> cols )
    {
        if ( cols == null || cols.isEmpty() )
        {
            return "-- Sem informação para tabela " + tabela + "\n";
        }
        StringBuilder sql = new StringBuilder();
        sql.append( "DROP TABLE IF EXISTS `" ).append( tabela ).append( "`; \n" );
        sql.append( "CREATE TABLE `" ).append( tabela ).append( "` ( \n" );

        List<String> pkCols = new ArrayList<>();
        for ( int i = 0; i < cols.size(); i++ )
        {
            EstruturaTabela col = cols.get( i );
            sql.append( "    `" ).append( col.getField() ).append( "` " ).append( col.getType() );
            if ( col.getNulo() == null || col.getNulo().equalsIgnoreCase( "NO" ) )
            {
                sql.append( " NOT NULL" );
            }
            else
            {
                sql.append( " DEFAULT NULL" );
            }
            if ( col.getPadrao_default() != null && !col.getPadrao_default().isEmpty() )
            {
                sql.append( " DEFAULT '" ).append( col.getPadrao_default().replace( "'", "''" ) ).append( "'" );
            }
            if ( col.getExtra() != null && col.getExtra().toLowerCase().contains( "auto_increment" ) )
            {
                sql.append( " AUTO_INCREMENT" );
            }
            if ( i < cols.size() - 1 )
            {
                sql.append( ",\n" );
            }

            if ( "PRI".equalsIgnoreCase( col.getKey() ) )
            {
                pkCols.add( col.getField() );
            }
        }

        if ( !pkCols.isEmpty() )
        {
            sql.append( ",\n    PRIMARY KEY (`" ).append( String.join( "`,`", pkCols ) ).append( "`)\n" );
        }
        else
        {
            sql.append( "\n" );
        }

        sql.append( ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" );
        return sql.toString();
    }

    // --- Geração de scripts para adicionar colunas ---
    private static String gerarScriptColumnAdd( String bd, Vector<Vector<ColunasFalta>> v )
    {
        StringBuilder out = new StringBuilder();
        for ( Vector<ColunasFalta> vec : v )
        {
            if ( vec == null )
            {
                continue;
            }
            for ( ColunasFalta cf : vec )
            {
                out.append( addColumnAfter( bd, cf.getTabela(), cf.getAnterior(), cf.getNome(), cf.getTipo(), cf.getValorDefault() ) )
                        .append( "\n" );
            }
        }
        return out.toString();
    }

    // --- Geração de scripts para alterar colunas ---
    private static String gerarScriptColumnUpdate( String bd, Vector<Vector<ColunasUpdate>> v )
    {
        StringBuilder out = new StringBuilder();
        for ( Vector<ColunasUpdate> vec : v )
        {
            if ( vec == null )
            {
                continue;
            }
            for ( ColunasUpdate cu : vec )
            {
                out.append( updateColumn( bd, cu.getTabela(), cu.getNome(), cu.getNewType(), cu.getValorDefault() ) )
                        .append( "\n" );
            }
        }
        return out.toString();
    }

    private static String addColumnAfter( String bd, String tabela, String coluna_anterior, String coluna, String tipo, String padraDefault )
    {
        String padrao = Objects.isNull( padraDefault ) ? "" : " DEFAULT '" + padraDefault.replace( "'", "''" ) + "' ";
        String afterPart = ( coluna_anterior == null || coluna_anterior.isEmpty() ) ? "" : " AFTER `" + coluna_anterior + "`";
        String sql = String.format( "ALTER TABLE `%s`.`%s` ADD COLUMN `%s` %s NULL %s%s;", bd, tabela, coluna, tipo, padrao, afterPart );
        return sql;
    }

    private static String updateColumn( String bd, String tabela, String coluna, String novoTipo, String valorDefault )
    {
        String defaultPart = ( valorDefault == null || valorDefault.isEmpty() ) ? "" : " DEFAULT '" + valorDefault.replace( "'", "''" ) + "'";
        String sql = String.format( "ALTER TABLE `%s`.`%s` CHANGE COLUMN `%s` `%s` %s%s;", bd, tabela, coluna, coluna, novoTipo, defaultPart );
        return sql;
    }

    // --- Recuperar estrutura de coluna (usando SHOW COLUMNS FROM ... FROM schema) ---
    private static Vector<EstruturaTabela> getLinhasTabela( String sql, BDConexao conexao )
    {
        Vector<EstruturaTabela> estruturaTabela = new Vector<>();
        // OBS: sql deve ser como: SHOW COLUMNS FROM tabela FROM schema
        try ( Connection conn = conexao.getConnection(); PreparedStatement ps = conn.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {

            while ( rs.next() )
            {
                EstruturaTabela e = new EstruturaTabela();
                e.setField( rs.getString( 1 ) );
                e.setType( rs.getString( 2 ) );
                e.setNulo( rs.getString( 3 ) );
                e.setKey( rs.getString( 4 ) );
                e.setPadrao_default( rs.getString( 5 ) );
                e.setExtra( rs.getString( 6 ) );
                estruturaTabela.add( e );
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro getLinhasTabela: " + sql, ex );
        }
        return estruturaTabela;
    }

    private static String getSql( String tabela, String bd )
    {
        return "SHOW COLUMNS FROM " + tabela + " FROM " + bd;
    }

    private static Vector<ColunasFalta> getColunasEmFaltas( String tabela, Vector<EstruturaTabela> e1, Vector<EstruturaTabela> e2 )
    {
        Vector<ColunasFalta> cf = new Vector<>();
        for ( int i = 0; i < e1.size(); i++ )
        {
            EstruturaTabela a1 = e1.get( i );
            boolean encontrou = false;
            for ( int j = 0; j < e2.size(); j++ )
            {
                EstruturaTabela a2 = e2.get( j );
                if ( a1.getField().equals( a2.getField() ) )
                {
                    encontrou = true;
                    break;
                }
            }
            if ( !encontrou )
            {
                ColunasFalta col = new ColunasFalta();
                col.setTabela( tabela );
                col.setNome( a1.getField() );
                col.setTipo( a1.getType() );
                col.setAnterior( i > 0 ? e1.get( i - 1 ).getField() : "" );
                col.setValorDefault( a1.getPadrao_default() );
                cf.add( col );
            }
        }
        return cf;
    }

    private static Vector<ColunasUpdate> getColunasUpdate( String tabela, Vector<EstruturaTabela> e1, Vector<EstruturaTabela> e2 )
    {
        Vector<ColunasUpdate> cf = new Vector<>();
        for ( int i = 0; i < e1.size(); i++ )
        {
            EstruturaTabela a1 = e1.get( i );
            for ( int j = 0; j < e2.size(); j++ )
            {
                EstruturaTabela a2 = e2.get( j );
                if ( a1.getField().equals( a2.getField() ) )
                {
                    if ( !Objects.equals( a1.getType(), a2.getType() ) || !Objects.equals( a1.getPadrao_default(), a2.getPadrao_default() ) )
                    {
                        ColunasUpdate cu = new ColunasUpdate();
                        cu.setTabela( tabela );
                        cu.setNome( a1.getField() );
                        cu.setNewType( a1.getType() );
                        cu.setOldType( a2.getType() );
                        cu.setValorDefault( a1.getPadrao_default() );
                        cf.add( cu );
                    }
                    break;
                }
            }
        }
        return cf;
    }

    private static int existeTabela( String tabela, String bd, BDConexao conexao )
    {
        if ( !isValidSchemaName( bd, conexao ) )
        {
            LOGGER.warning( "existeTabela: schema inválido: " + bd );
            return -1;
        }
        String sql = "SELECT COUNT(1) AS n FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ? AND table_name = ?";
        try ( Connection conn = conexao.getConnection(); PreparedStatement ps = conn.prepareStatement( sql ) )
        {
            ps.setString( 1, bd );
            ps.setString( 2, tabela );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getInt( "n" );
                }
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro existeTabela", ex );
        }
        return -1;
    }

    // --- Sort utility (esqueleto) ---
    private void sort( final javax.swing.JTable jTable, final javax.swing.JTextField field, TableRowSorter trs, int[] indexs )
    {
        // Implementação deixada intencionalmente simples — pode ser ativada quando necessário
    }

    // --- executarComBotao com ExecutorService ---
    public static void executarComBotao( final JButton botao, final Runnable acao )
    {
        botao.setEnabled( false );
        EXEC.submit( () ->
        {
            try
            {
                acao.run();
            }
            catch ( Exception ex )
            {
                LOGGER.log( Level.SEVERE, "Erro na ação executada pelo botão", ex );
            }
            finally
            {
                SwingUtilities.invokeLater( () -> botao.setEnabled( true ) );
            }
        } );
    }

    // --- Validação de nomes de schema/tabela (usando whitelist) ---
    private static boolean isValidSchemaName( String schema, BDConexao conexao )
    {
        if ( schema == null || schema.trim().isEmpty() )
        {
            return false;
        }
        Vector<String> schemas = getSchemas( conexao );
        return schemas.contains( schema );
    }

    private static Vector<String> getSchemas( BDConexao conexao )
    {
        Vector<String> list = new Vector<>();
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA";
        try ( Connection conn = conexao.getConnection(); PreparedStatement ps = conn.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                list.add( rs.getString( 1 ) );
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.WARNING, "Não foi possível obter schemas", ex );
        }
        return list;
    }

    // --- Classes internas ---
    private static class EstruturaTabela
    {

        private String field;
        private String type;
        private String nulo;
        private String key;
        private String padrao_default;
        private String extra;

        public String getField()
        {
            return field;
        }

        public void setField( String field )
        {
            this.field = field;
        }

        public String getType()
        {
            return type;
        }

        public void setType( String type )
        {
            this.type = type;
        }

        public String getNulo()
        {
            return nulo;
        }

        public void setNulo( String nulo )
        {
            this.nulo = nulo;
        }

        public String getKey()
        {
            return key;
        }

        public void setKey( String key )
        {
            this.key = key;
        }

        public String getPadrao_default()
        {
            return padrao_default;
        }

        public void setPadrao_default( String padrao_default )
        {
            this.padrao_default = padrao_default;
        }

        public String getExtra()
        {
            return extra;
        }

        public void setExtra( String extra )
        {
            this.extra = extra;
        }
    }

    private static class ColunasFalta
    {

        private String tabela;
        private String nome;
        private String tipo;
        private String anterior;
        private String valorDefault;

        public ColunasFalta()
        {
        }

        public String getAnterior()
        {
            return anterior;
        }

        public void setAnterior( String anterior )
        {
            this.anterior = anterior;
        }

        public String getTabela()
        {
            return tabela;
        }

        public void setTabela( String tabela )
        {
            this.tabela = tabela;
        }

        public String getNome()
        {
            return nome;
        }

        public void setNome( String nome )
        {
            this.nome = nome;
        }

        public String getTipo()
        {
            return tipo;
        }

        public void setTipo( String tipo )
        {
            this.tipo = tipo;
        }

        public String getValorDefault()
        {
            return valorDefault;
        }

        public void setValorDefault( String valorDefault )
        {
            this.valorDefault = valorDefault;
        }
    }

    private static class ColunasUpdate
    {

        private String tabela;
        private String nome;
        private String oldType;
        private String newType;
        private String valorDefault;

        public ColunasUpdate()
        {
        }

        public String getTabela()
        {
            return tabela;
        }

        public void setTabela( String tabela )
        {
            this.tabela = tabela;
        }

        public String getNome()
        {
            return nome;
        }

        public void setNome( String nome )
        {
            this.nome = nome;
        }

        public String getValorDefault()
        {
            return valorDefault;
        }

        public void setValorDefault( String valorDefault )
        {
            this.valorDefault = valorDefault;
        }

        public String getOldType()
        {
            return oldType;
        }

        public void setOldType( String oldType )
        {
            this.oldType = oldType;
        }

        public String getNewType()
        {
            return newType;
        }

        public void setNewType( String newType )
        {
            this.newType = newType;
        }
    }

}
