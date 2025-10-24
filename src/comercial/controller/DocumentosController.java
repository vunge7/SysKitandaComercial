/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Documento;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.BDConexaoOperacoes;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class DocumentosController implements EntidadeFactory
{

    private BDConexao conexao;

    public DocumentosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Documento documento = (Documento) object;
        String INSERT = "INSERT INTO documento( designacao , abreviacao , cod_ultimo_doc , descricao_ultimo_doc , ultima_data "
                + ")"
                + " VALUES("
                + "'" + documento.getDesignacao() + "' , "
                + "'" + documento.getAbreviacao() + "' , "
                + "'" + documento.getCodUltimoDoc() + "' , "
                + "'" + documento.getDescricaoUltimoDoc() + "' , "
                + "'" + MetodosUtil.getDataBanco( documento.getUltimaData() )
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM documento WHERE pk_documento = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Documento> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM documento ORDER BY pk_documento ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Documento> lista_documento = new ArrayList<>();
        Documento documento;
        try
        {

            while ( result.next() )
            {
                documento = new Documento();
                documento.setDesignacao( result.getString( "designacao" ) );
                documento.setAbreviacao( result.getString( "abreviacao" ) );
                documento.setCodUltimoDoc( result.getInt( "cod_ultimo_doc" ) );
                documento.setDescricaoUltimoDoc( result.getString( "descricao_ultimo_doc" ) );
                documento.setUltimaData( result.getDate( "ultima_data" ) );
                lista_documento.add( documento );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_documento;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM documento WHERE pk_documento IN(1,2,3,7,13)";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        lista.add( 0, "-- Seleccione --" );
        return lista;
    }

    public Vector<String> getVectorRecolha()
    {
        String FIND_ALL = "SELECT designacao FROM documento WHERE pk_documento IN(1)";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        lista.add( 0, "-- Seleccione --" );
        return lista;
    }

    public Vector<String> getVectorMesas()
    {
        String FIND_ALL = "SELECT designacao FROM documento WHERE pk_documento IN(1,2,13)";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        lista.add( 0, "-- Seleccione --" );
        return lista;
    }

    public Vector<String> getVector2()
    {
        {
//            String FIND_ALL = "SELECT designacao FROM documento ORDER BY pk_documento ASC";
            String FIND_ALL = "SELECT designacao FROM documento WHERE pk_documento = 9";
            ResultSet result = conexao.executeQuery( FIND_ALL );
            Vector<String> lista_documento = new Vector<>();
            try
            {

                while ( result.next() )
                {

                    lista_documento.add( result.getString( "designacao" ) );

                }

            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            return lista_documento;
        }
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM documento WHERE pk_documento = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Documento documento = null;
        try
        {

            if ( result.next() )
            {
                documento = new Documento();
                documento.setPkDocumento( result.getInt( "pk_documento" ) );
                documento.setDesignacao( result.getString( "designacao" ) );
                documento.setAbreviacao( result.getString( "abreviacao" ) );
                documento.setCodUltimoDoc( result.getInt( "cod_ultimo_doc" ) );
                documento.setDescricaoUltimoDoc( result.getString( "descricao_ultimo_doc" ) );
                documento.setUltimaData( result.getDate( "ultima_data" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return documento;

    }

    public Documento getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(documento) as maximo_id, d.*  FROM documento d";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Documento documento = null;
        try
        {

            if ( result.next() )
            {
                documento = new Documento();
                documento.setPkDocumento( result.getInt( "maximo_id" ) );
                documento.setDesignacao( result.getString( "designacao" ) );
                documento.setAbreviacao( result.getString( "abreviacao" ) );
                documento.setCodUltimoDoc( result.getInt( "cod_ultimo_doc" ) );
                documento.setDescricaoUltimoDoc( result.getString( "descricao_ultimo_doc" ) );
                documento.setUltimaData( result.getDate( "ultima_data" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return documento;

    }

    public Documento getDocumentoByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM documento a WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Documento documento = null;
        try
        {

            if ( result.next() )
            {
                documento = new Documento();
                documento.setPkDocumento( result.getInt( "pk_documento" ) );
                documento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return documento;

    }

    public Vector<String> getVector3()
    {
        {
//            String FIND_ALL = "SELECT designacao FROM documento ORDER BY pk_documento ASC";
            String FIND_ALL = "SELECT designacao FROM documento WHERE pk_documento = 10";
            ResultSet result = conexao.executeQuery( FIND_ALL );
            Vector<String> lista_documento = new Vector<>();
            try
            {

                while ( result.next() )
                {

                    lista_documento.add( result.getString( "designacao" ) );

                }

            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            return lista_documento;
        }
    }

    public Documento findDocumentoById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM documento WHERE pk_documento = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        Documento documento = null;
        try
        {
            if ( result.next() )
            {
                documento = new Documento();
                documento = getDocumentoResultSet( documento, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return documento;

    }

    private Documento getDocumentoResultSet( Documento documento, ResultSet result )
    {
        try
        {
            documento.setPkDocumento( result.getInt( "pk_documento" ) );
            documento.setDesignacao( result.getString( "designacao" ) );
            documento.setAbreviacao( result.getString( "abreviacao" ) );
            documento.setCodUltimoDoc( result.getInt( "cod_ultimo_doc" ) );
            documento.setUltimaData( result.getDate( "ultima_data" ) );

        }
        catch ( Exception e )
        {
        }

        return documento;
    }

   public static void start(BDConexao conexao) {
        try {
            Connection conn = conexao.getConnectionAtiva();
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
                System.out.println("[TRANSAÇÃO] 🔹 Iniciada");
            }
        } catch (SQLException e) {
            System.err.println("[TRANSAÇÃO] ❌ Erro ao iniciar: " + e.getMessage());
        }
    }

    public static void commit(BDConexao conexao) {
        try {
            Connection conn = conexao.getConnectionAtiva();
            if (!conn.getAutoCommit()) {
                conn.commit();
                conn.setAutoCommit(true);
                System.out.println("[TRANSAÇÃO] ✅ Commit concluído");
            }
        } catch (SQLException e) {
            System.err.println("[TRANSAÇÃO] ❌ Erro ao fazer commit: " + e.getMessage());
        }
    }

    public static void rollback(BDConexao conexao) {
        try {
            Connection conn = conexao.getConnectionAtiva();
            if (!conn.getAutoCommit()) {
                conn.rollback();
                conn.setAutoCommit(true);
                System.out.println("[TRANSAÇÃO] 🔄 Rollback efetuado");
            }
        } catch (SQLException e) {
            System.err.println("[TRANSAÇÃO] ❌ Erro ao fazer rollback: " + e.getMessage());
        }
    }
    
 
//    public static void startTransactionOperacoes( BDConexaoOperacoes conexao )
//    {
//        try
//        {
//            conexao.getConnectionAtiva().setAutoCommit( false ); // ← aqui é onde realmente funciona
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void commitTransactionOperacoes( BDConexaoOperacoes conexao )
//    {
//        try
//        {
//            conexao.getConnection().commit();
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void rollBackTransactionOperacoes( BDConexaoOperacoes conexao )
//    {
//        try
//        {
//            conexao.getConnection().rollback();
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//    }
}
