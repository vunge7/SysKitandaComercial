package util;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
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

public class DefinicoesUtil
{

    private static final Logger LOGGER = Logger.getLogger( DefinicoesUtil.class.getName() );
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ExecutorService EXEC = Executors.newFixedThreadPool( 4 );

    // =========================
    //  PASSWORD
    // =========================
    public static String gerarSenha()
    {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz=+\\-/*";
        StringBuilder sb = new StringBuilder( 8 );
        for ( int i = 0; i < 8; i++ )
        {
            sb.append( chars.charAt( RANDOM.nextInt( chars.length() ) ) );
        }
        return sb.toString();
    }

    // =========================
    //  SCRIPT PRINCIPAL
    // =========================
    public static String gerarScript( String bdOrigem, String bdDestino, BDConexao conexao )
    {
        validarSchema( bdOrigem, conexao );
        validarSchema( bdDestino, conexao );

        StringBuilder out = new StringBuilder();
        File file = prepararFicheiro();

        out.append( "USE `" ).append( bdDestino ).append( "`;\n\n" );

        Vector<String> tabelasOrigem = getTables( bdOrigem, conexao );
        Vector<String> tabelasDestino = getTables( bdDestino, conexao );

        for ( String tabela : tabelasOrigem )
        {
            if ( !tabelasDestino.contains( tabela ) )
            {
                out.append( gerarCreateTable( tabela, bdOrigem, conexao ) ).append( "\n\n" );
            }
            else
            {
                out.append( gerarAlteracoesTabela( tabela, bdOrigem, bdDestino, conexao ) );
            }
        }

        MetodosUtil.escreverNoDocumento( out.toString(), file );
        JOptionPane.showMessageDialog( null, "Sincronização concluída!", "Info", JOptionPane.INFORMATION_MESSAGE );

        return out.toString();
    }

    // =========================
    //  CREATE TABLE
    // =========================
    private static String gerarCreateTable( String tabela, String bd, BDConexao conexao )
    {
        Vector<EstruturaTabela> cols = getEstrutura( tabela, bd, conexao );

        StringBuilder sql = new StringBuilder();
        sql.append( "DROP TABLE IF EXISTS `" ).append( tabela ).append( "`;\n" );
        sql.append( "CREATE TABLE `" ).append( tabela ).append( "` (\n" );

        List<String> pk = new ArrayList<>();

        for ( int i = 0; i < cols.size(); i++ )
        {
            EstruturaTabela c = cols.get( i );
            sql.append( "  `" ).append( c.field ).append( "` " ).append( c.type );

            if ( "NO".equalsIgnoreCase( c.nulo ) )
            {
                sql.append( " NOT NULL" );
            }

            sql.append( buildDefaultClause( c.type, c.defaultValue ) );

            if ( c.extra != null && c.extra.toLowerCase().contains( "auto_increment" ) )
            {
                sql.append( " AUTO_INCREMENT" );
            }

            if ( "PRI".equalsIgnoreCase( c.key ) )
            {
                pk.add( c.field );
            }

            sql.append( ",\n" );
        }

        if ( !pk.isEmpty() )
        {
            sql.append( "  PRIMARY KEY (`" ).append( String.join( "`,`", pk ) ).append( "`)\n" );
        }
        else
        {
            sql.setLength( sql.length() - 2 );
            sql.append( "\n" );
        }

        sql.append( ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" );
        return sql.toString();
    }

    // =========================
    //  ALTER TABLE
    // =========================
    private static String gerarAlteracoesTabela( String tabela, String bd1, String bd2, BDConexao conexao )
    {
        StringBuilder out = new StringBuilder();

        Vector<EstruturaTabela> e1 = getEstrutura( tabela, bd1, conexao );
        Vector<EstruturaTabela> e2 = getEstrutura( tabela, bd2, conexao );

        for ( int i = 0; i < e1.size(); i++ )
        {
            EstruturaTabela c1 = e1.get( i );
            EstruturaTabela c2 = find( c1.field, e2 );

            if ( c2 == null )
            {
                out.append(
                        "ALTER TABLE `" ).append( tabela ).append( "` ADD COLUMN `"
                ).append( c1.field ).append( "` " ).append( c1.type )
                        .append( buildDefaultClause( c1.type, c1.defaultValue ) )
                        .append( ";\n" );
            }
            else if ( !equivalentes( c1, c2 ) )
            {
                out.append(
                        "ALTER TABLE `" ).append( tabela ).append( "` CHANGE COLUMN `"
                ).append( c1.field ).append( "` `" ).append( c1.field ).append( "` " )
                        .append( c1.type )
                        .append( buildDefaultClause( c1.type, c1.defaultValue ) )
                        .append( ";\n" );
            }
        }

        return out.toString();
    }

    // =========================
    //  DEFAULT (MySQL 5)
    // =========================
    private static String buildDefaultClause( String tipo, String valor )
    {
        if ( valor == null || valor.trim().isEmpty() )
        {
            return "";
        }

        String t = tipo.toLowerCase();
        String v = valor.trim().toUpperCase();

        if ( t.startsWith( "timestamp" ) )
        {
            if ( "CURRENT_TIMESTAMP".equals( v ) )
            {
                return " DEFAULT CURRENT_TIMESTAMP";
            }
            return "";
        }

        if ( t.startsWith( "datetime" ) )
        {
            return "";
        }

        return " DEFAULT '" + valor.replace( "'", "''" ) + "'";
    }

    // =========================
    //  ESTRUTURA
    // =========================
    private static Vector<EstruturaTabela> getEstrutura( String tabela, String bd, BDConexao conexao )
    {
        Vector<EstruturaTabela> v = new Vector<>();
        String sql = "SHOW COLUMNS FROM `" + tabela + "` FROM `" + bd + "`";

        try ( Connection c = conexao.getConnection(); PreparedStatement ps = c.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                EstruturaTabela e = new EstruturaTabela();
                e.field = rs.getString( 1 );
                e.type = rs.getString( 2 );
                e.nulo = rs.getString( 3 );
                e.key = rs.getString( 4 );
                e.defaultValue = rs.getString( 5 );
                e.extra = rs.getString( 6 );
                v.add( e );
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro estrutura " + tabela, ex );
        }
        return v;
    }

    private static EstruturaTabela find( String field, Vector<EstruturaTabela> v )
    {
        for ( EstruturaTabela e : v )
        {
            if ( e.field.equals( field ) )
            {
                return e;
            }
        }
        return null;
    }

    private static boolean equivalentes( EstruturaTabela a, EstruturaTabela b )
    {
        return Objects.equals( a.type, b.type )
                && Objects.equals( a.defaultValue, b.defaultValue );
    }

    // =========================
    //  SCHEMAS / TABLES
    // =========================
    private static Vector<String> getTables( String bd, BDConexao conexao )
    {
        Vector<String> v = new Vector<>();
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=?";

        try ( Connection c = conexao.getConnection(); PreparedStatement ps = c.prepareStatement( sql ) )
        {
            ps.setString( 1, bd );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    v.add( rs.getString( 1 ) );
                }
            }
        }
        catch ( SQLException ex )
        {
            LOGGER.log( Level.SEVERE, "Erro getTables", ex );
        }
        return v;
    }

    private static void validarSchema( String bd, BDConexao conexao )
    {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=?";
        try ( Connection c = conexao.getConnection(); PreparedStatement ps = c.prepareStatement( sql ) )
        {
            ps.setString( 1, bd );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() && rs.getInt( 1 ) == 0 )
                {
                    throw new IllegalArgumentException( "Schema inválido: " + bd );
                }
            }
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    private static File prepararFicheiro()
    {
        File dir = new File( CAMINHO_SCRIP_TO_UPDATE );
        dir.mkdirs();
        return new File( dir, FILE_TO_UPDATE );
    }

    // =========================
    //  EXEC BOTÃO
    // =========================
    public static void executarComBotao( JButton b, Runnable r )
    {
        b.setEnabled( false );
        EXEC.submit( () ->
        {
            try
            {
                r.run();
            }
            finally
            {
                SwingUtilities.invokeLater( () -> b.setEnabled( true ) );
            }
        } );
    }

    // =========================
    //  INNER CLASS
    // =========================
    private static class EstruturaTabela
    {

        String field;
        String type;
        String nulo;
        String key;
        String defaultValue;
        String extra;
    }
}
