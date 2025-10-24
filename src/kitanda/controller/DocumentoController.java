/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.controller;


import java.sql.Connection;
import hotel.controller.*;
import entity.Documento;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DocumentoController
{

    private BDConexao conexao;

    public DocumentoController( BDConexao conexao )
    {
        this.conexao = conexao;
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

    public static void main( String[] args )
    {
//        Documento documento = new DocumentoController( BDConexao.getInstancia() ).findDocumentoById( 1 );
//        System.out.println( "Documento: " +documento.getDesignacao() );
    }

}
