/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;
import static util.DVML.CAMINHO_SCRIP_TO_UPDATE;
import static util.DVML.FILE_TO_UPDATE;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DefinicoesUtil
{

    public static void main( String[] args )
    {
        BDConexao conexao = new BDConexao();
        String bd_fd = "kitanda_db";
        String bd_fb = "kitanda_db_indiano";
//        System.out.println( gerarScript( bd_fd, bd_fb, conexao ) );

        if ( existeTabela( "tb_venda", bd_fb, conexao ) == 0 )
        {
            System.out.println( "Não existe" );
        }
        else
        {
            System.out.println( "Existe" );
        }

    }

    public static String gerarSenha()
    {
        int qtdeMaximaCaracteres = 8;
        String[] caracteres =
        {
            "0", "1", "b", "2", "4", "5", "6", "7", "8",
            "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z",
            "=", "+", "-", "/", "*"

//            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
//            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
//            "X", "Y", "Z",
        };

        StringBuilder senha = new StringBuilder();

        for ( int i = 0; i < qtdeMaximaCaracteres; i++ )
        {
            int posicao = ( int ) ( Math.random() * caracteres.length );
            senha.append( caracteres[ posicao ] );
        }
        return senha.toString();

    }

    public static int numeroDeTabelas( String bd, BDConexao conexao )
    {
        int number = 0;
        String sql = "SELECT count(*) AS TOTALNUMBEROFTABLES "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_SCHEMA = '" + bd + "'";
        try
        {
            ResultSet rs = conexao.executeQuery( sql );

            if ( rs.next() )
            {
                number = rs.getInt( 1 );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return number;

    }

    public static Vector<String> getTables( String bd, BDConexao conexao )
    {
        Vector<String> vector_tabelas = new Vector<>();

        String sql = "SELECT TABLE_NAME "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_SCHEMA = '" + bd + "'";
        try
        {
            ResultSet rs = conexao.executeQuery( sql );

//          rs.getMetaData(
            while ( rs.next() )
            {
                String tabela = rs.getString( 1 );
                vector_tabelas.add( tabela );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return vector_tabelas;
    }

    public static Vector<String> tabelasEmFalta( String bd_1, String bd_2, BDConexao conexao )
    {

        Vector<String> tabelasEmFalta = new Vector<>();

        Vector<String> tablesBD_1 = getTables( bd_1, conexao );
        Vector<String> tablesBD_2 = getTables( bd_2, conexao );

        boolean existe = false;
        for ( int i = 0; i < tablesBD_1.size(); i++ )
        {
            String elementAt1 = tablesBD_1.elementAt( i );
            existe = false;
            for ( int j = 0; j < tablesBD_2.size(); j++ )
            {
                String elementAt2 = tablesBD_2.elementAt( j );

                if ( elementAt1.equals( elementAt2 ) )
                {
                    existe = true;
                    break;
                }

            }
            if ( !existe )
            {
                tabelasEmFalta.add( elementAt1 );
            }

        }

        return tabelasEmFalta;

    }

    private static File gerarFicheiro()
    {

        File path = new File( CAMINHO_SCRIP_TO_UPDATE );
        File file = null;
        //ELIMINAR O FICHEIRO SAFT CASO EXISTA
        if ( !path.exists() )
        {
            try
            {

                path.mkdirs();
                file = new File( CAMINHO_SCRIP_TO_UPDATE + "/" + FILE_TO_UPDATE );
                file.createNewFile();
                System.out.println( "Script criado com successo." );
            }
            catch ( IOException e )
            {
                System.err.println( "Falha ao criar o ficheiro  " + e.getLocalizedMessage() );
            }

        }
        else if ( path.exists() )
        {
            path.delete();

            try
            {
                file = new File( CAMINHO_SCRIP_TO_UPDATE + "/" + FILE_TO_UPDATE );
                file.createNewFile();
            }
            catch ( IOException e )
            {
            }

        }
        return file;

    }

    public static String gerarScript( String bd_1, String bd_2, BDConexao conexao )
    {

        StringBuilder buffer = new StringBuilder();
        File file = gerarFicheiro();
        Vector<Vector<ColunasFalta>> vectorColunasEmFalta = new Vector<>();
        Vector<Vector<ColunasUpdate>> vectorColunasUpdate = new Vector<>();
        Vector<String> tablesBD_1 = getTables( bd_1, conexao );
        Vector<String> tables = tabelasEmFalta( bd_1, bd_2, conexao );

        String out = "";

        out += "USE `" + bd_2 + "`;\n\n";

        System.out.println( "TMANHO 1 = " + numeroDeTabelas( bd_1, conexao ) );
        System.out.println( "TMANHO 2 = " + numeroDeTabelas( bd_2, conexao ) );
//        if ( numeroDeTabelas( bd_1, conexao ) == numeroDeTabelas( bd_2, conexao ) )
        if (true )
        {

            int size = tablesBD_1.size();

            for ( int i = 0; i < size; i++ )
            {
                String tabela = tablesBD_1.elementAt( i );

                String sql1 = getSql( tabela, bd_1 );
                String sql2 = getSql( tabela, bd_2 );

                if ( existeTabela( tabela, bd_2, conexao ) != 0 )
                {
                    Vector<EstruturaTabela> e1 = getLinhasTabela( sql1, conexao ); //Estrutura da tabela na BD 1.
                    Vector<EstruturaTabela> e2 = getLinhasTabela( sql2, conexao ); //Estrutura da tabela na BD 2.

                    Vector<ColunasFalta> cf = getColunasEmFaltas( tabela, e1, e2 ); //Retorna as colunas em falta.
                    Vector<ColunasUpdate> cu = getColunasUpdate( tabela, e1, e2 ); // Retorna as colunas por actualizar.

                    vectorColunasEmFalta.add( cf );
                    vectorColunasUpdate.add( cu );

                }

            }

            /**
             * ADIÇÃO COLUNAS EM FATLAS.
             */
            out += gerarScriptColumnAdd( bd_2, vectorColunasEmFalta );
            System.out.println( out );

            /**
             * ACTUALIZAR AS COLUNAS.
             */
            out += gerarScriptColumnUpdate( bd_2, vectorColunasUpdate );
            System.out.println( out );

            /**
             * GERAÇÃO DE SCRIPTS DA TABELA EM FALTA.
             */
            for ( int i = 0; i < tables.size(); i++ )
            {
                String elementAt = tables.elementAt( i );
                String sql = getSql( elementAt, bd_1 );
                Vector<EstruturaTabela> e = getLinhasTabela( sql, conexao );
                out += gerarScriptTable( elementAt, e ) + "\n\n";
            }

        }

        buffer.append( out );
        MetodosUtil.escreverNoDocumento( buffer.toString(), file );

        return out;

    }

    private static String gerarScriptTable( String tabela, Vector<EstruturaTabela> e )
    {

        EstruturaTabela chave_primaria = e.get( 0 );

        String sql = "DROP TABLE IF EXISTS `" + tabela + "`; \n"
                + "CREATE TABLE `" + tabela + "` ( \n";

        sql += "    `" + chave_primaria.getField() + "` " + chave_primaria.getType() + " NOT NULL AUTO_INCREMENT, \n";
        for ( int i = 1; i < e.size(); i++ )
        {
            EstruturaTabela elementAt = e.elementAt( i );
            sql += "    `" + elementAt.getField() + "` " + elementAt.getType() + " " + ( ( elementAt.getNulo().equals( "YES" ) ) ? " DEFAULT NULL" : " NOT NULL" ) + " , \n";
        }

        sql += "  PRIMARY KEY (`" + chave_primaria.getField() + "`)\n";
//        sql += ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";
        sql += ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";
//
        System.out.println( sql );
        return sql;

    }

    private static String gerarScriptColumnAdd( String bd, Vector<Vector<ColunasFalta>> v )
    {
        String out = "";
        for ( int i = 0; i < v.size(); i++ )
        {
            Vector<ColunasFalta> get = v.get( i );
            if ( !Objects.isNull( get ) )
            {

                for ( int j = 0; j < get.size(); j++ )
                {
                    ColunasFalta elementAt = get.elementAt( j );
                    out += addColumnAfter( bd, elementAt.getTabela(), elementAt.getAnterior(), elementAt.getNome(), elementAt.getTipo(), elementAt.getValorDefault() ) + "\n";
                }
            }
        }

        return out;
    }

    private static String gerarScriptColumnUpdate( String bd, Vector<Vector<ColunasUpdate>> v )
    {
        String out = "";
        for ( int i = 0; i < v.size(); i++ )
        {
            Vector<ColunasUpdate> get = v.get( i );
            if ( !Objects.isNull( get ) )
            {

                for ( int j = 0; j < get.size(); j++ )
                {
                    ColunasUpdate elementAt = get.elementAt( j );
                    out += updateColumn( bd, elementAt.getTabela(),
                            elementAt.getNome(),
                            elementAt.getNewType(),
                            elementAt.getValorDefault() ) + "\n";
                }
            }
        }
        return out;
    }

    private static String addColumnAfter( String bd, String tabela, String coluna_anterior, String coluna, String tipo, String padraDefault )
    {

        String padrao = Objects.isNull( padraDefault ) ? "" : " DEFAULT '" + padraDefault + "' ";

        String sql = "ALTER TABLE `" + bd + "`.`" + tabela + "` "
                + "ADD COLUMN `" + coluna + "`  " + tipo + " NULL  " + padrao + "AFTER `" + coluna_anterior + "`;";

        return sql;

    }

    private static String updateColumn( String bd, String tabela, String coluna, String novoTipo, String valorDefault )
    {

        /*
            
         */
        String padrao = ( novoTipo.contentEquals( "int" )
                || novoTipo.contentEquals( "double" )
                || novoTipo.contentEquals( "decimal" ) )
                ? //                valorDefault : "'" + valorDefault + "'";
                "0.0" : "'" + valorDefault + "'";

        String sql = "ALTER TABLE `" + bd + "`.`" + tabela + "` \n"
                //                + "CHANGE COLUMN `" + coluna + "` `" + coluna + "` " + novoTipo + " NULL DEFAULT " + padrao + ";";
                + "CHANGE COLUMN `" + coluna + "` `" + coluna + "` " + novoTipo + ";";

        return sql;
    }

    private static Vector<EstruturaTabela> getLinhasTabela( String sql, BDConexao conexao )
    {

        ResultSet rs = conexao.executeQuery( sql );
        Vector<EstruturaTabela> extruturaTabela = new Vector<>();

        try
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
                extruturaTabela.add( e );

            }
        }
        catch ( SQLException e )
        {
        }

        return extruturaTabela;
    }

    private static String getSql( String tabela, String bd )
    {
//        System.out.println( "SHOW COLUMNS FROM " + tabela + "  FROM " + bd + "");

        return "SHOW COLUMNS FROM " + tabela + "  FROM " + bd + "";

    }

    private static void mostrarEstrtura( Vector<EstruturaTabela> e )
    {

        for ( int i = 0; i < e.size(); i++ )
        {
            EstruturaTabela elementAt = e.elementAt( i );
            System.out.println( "Field: " + elementAt.getField() + "                Type: " + elementAt.getType() + "                Null: " + elementAt.getNulo() + "                Key: " + elementAt.getKey() );

        }
        System.out.println( "\n\n" );

    }

    private static Vector<ColunasFalta> getColunasEmFaltas( String tabela, Vector<EstruturaTabela> e1, Vector<EstruturaTabela> e2 )
    {

        Vector<ColunasFalta> cf = new Vector<>();
        boolean encontrou;
        for ( int i = 0; i < e1.size(); i++ )
        {
            EstruturaTabela elementA1 = e1.elementAt( i );
            encontrou = false;
            for ( int j = 0; j < e2.size(); j++ )
            {
                EstruturaTabela elementA2 = e2.elementAt( j );

                if ( elementA1.getField().equals( elementA2.getField() ) )
                {
                    encontrou = true;
                    break;
                }

            }

            if ( !encontrou && i > 1 )
            {
                ColunasFalta colunasFalta = new ColunasFalta();
                colunasFalta.setTabela( tabela );
                colunasFalta.setNome( elementA1.getField() );
                colunasFalta.setTipo( elementA1.getType() );

                colunasFalta.setAnterior( e1.get( i - 1 ).field );
                colunasFalta.setValorDefault( e1.get( i ).padrao_default );

                cf.add( colunasFalta );
            }

        }

        return cf;

    }

    private static Vector<ColunasUpdate> getColunasUpdate( String tabela, Vector<EstruturaTabela> e1, Vector<EstruturaTabela> e2 )
    {

        Vector<ColunasUpdate> cf = new Vector<>();
        boolean encontrou;
        String typeOld;
        for ( int i = 0; i < e1.size(); i++ )
        {
            typeOld = "";
            EstruturaTabela elementA1 = e1.elementAt( i );
            encontrou = false;
            for ( int j = 0; j < e2.size(); j++ )
            {
                EstruturaTabela elementA2 = e2.elementAt( j );

                if ( elementA1.getField().equals( elementA2.getField() ) )
                {

                    if ( !elementA1.getType().equals( elementA2.getType() ) )
                    {
                        System.out.println( "diferente" );
                        typeOld = elementA2.getType();
                        encontrou = true;
                        break;
                    }

                }

            }

            if ( encontrou )
            {
                ColunasUpdate cu = new ColunasUpdate();
                cu.setTabela( tabela );
                cu.setNome( elementA1.getField() );
                cu.setNewType( elementA1.getType() );
                cu.setOldType( typeOld );
                cu.setValorDefault( elementA1.getPadrao_default() );

                cf.add( cu );
            }

        }

        return cf;

    }

    private static class EstruturaTabela
    {

        private String field;
        private String type;
        private String nulo;
        private String key;
        private String padrao_default;
        private String extra;

        public EstruturaTabela()
        {
        }

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

        @Override
        public String toString()
        {
            return "EstruturaTabela{" + "field=" + field + "\n type=" + type + "\n nulo=" + nulo + "\n key=" + key + "\n padrao_default=" + padrao_default + "\n extra=" + extra + '}';
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

    private static int existeTabela( String tabela, String bd, BDConexao conexao )
    {
        String sql = "select count(table_name) as n "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE table_schema = '" + bd + "' and table_name like '" + tabela + "'";

        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "n" );
            }
        }
        catch ( SQLException e )
        {

        }
        return -1;
    }

    private void sort( final JTable jTable, final JTextField field, TableRowSorter trs, int[] indexs )
    {
//         field.addKeyListener( new KeyAdapter()
//        {
//            @Override
//            public void keyReleased( KeyEvent e )
//            {
//                trs.setRowFilter( RowFilter.regexFilter( "(?i)" + field.getText(), indexs ) );
//            }
//
//            
//
//            
//
//        } );
//
//        trs = new TableRowSorter( jTable.getModel() );
//        jTable.setRowSorter( trs );

    }

}
