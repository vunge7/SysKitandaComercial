/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.DetalhesDocumento;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class DetalhesDocumentoController implements EntidadeFactory
{

    private BDConexao conexao;

    public DetalhesDocumentoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        DetalhesDocumento detalhesDocumento = ( DetalhesDocumento ) object;
        String INSERT = "INSERT INTO detalhes_documento( "
                + " ar_condicionado , "
                + " bateria , "
                + " camara , "
                + " canicos , "
                + " chave_roda , "
                + " cinto_seguranca , "
                + " colete , "
                + " elevador , "
                + " extintor , "
                + " farois , "
                + " guincho , "
                + " isqueiro , "
                + " limpa_parabrisa , "
                + " livrete , "
                + " titulo , "
                + " longos , "
                + " macaco , "
                + " manipulo_ext , "
                + " manipulo_int , "
                + " painel , "
                + " piscas , "
                + " porcas_jantes , "
                + " presenca , "
                + " radio , "
                + " reflectores , "
                + " retrovisores , "
                + " sensor , "
                + " sobresalente , "
                + " stop , "
                + " tampas_jantes , "
                + " tampoes , "
                + " tapetes , "
                + " tejadilho , "
                + " triangulo , "
                + " vidros , "
                + " notas , "
                + " data_entrada , "
                + " cod_venda"
                + ")"
                + " VALUES("
                + "'" + detalhesDocumento.getArCondicionado() + "' , "
                + "'" + detalhesDocumento.getBateria() + "' , "
                + "'" + detalhesDocumento.getCamara() + "' , "
                + "'" + detalhesDocumento.getCanicos() + "' , "
                + "'" + detalhesDocumento.getChaveRoda() + "' , "
                + "'" + detalhesDocumento.getCintoSeguranca() + "' , "
                + "'" + detalhesDocumento.getColete() + "' , "
                + "'" + detalhesDocumento.getElevador() + "' , "
                + "'" + detalhesDocumento.getExtintor() + "' , "
                + "'" + detalhesDocumento.getFarois() + "' , "
                + "'" + detalhesDocumento.getGuincho() + "' , "
                + "'" + detalhesDocumento.getIsqueiro() + "' , "
                + "'" + detalhesDocumento.getLimpaParabrisa() + "' , "
                + "'" + detalhesDocumento.getLivrete() + "' , "
                + "'" + detalhesDocumento.getTitulo() + "' , "
                + "'" + detalhesDocumento.getLongos() + "' , "
                + "'" + detalhesDocumento.getMacaco() + "' , "
                + "'" + detalhesDocumento.getManipuloExt() + "' , "
                + "'" + detalhesDocumento.getManipuloInt() + "' , "
                + "'" + detalhesDocumento.getPainel() + "' , "
                + "'" + detalhesDocumento.getPiscas() + "' , "
                + "'" + detalhesDocumento.getPorcasJantes() + "' , "
                + "'" + detalhesDocumento.getPresenca() + "' , "
                + "'" + detalhesDocumento.getRadio() + "' , "
                + "'" + detalhesDocumento.getReflectores() + "' , "
                + "'" + detalhesDocumento.getRetrovisores() + "' , "
                + "'" + detalhesDocumento.getSensor() + "' , "
                + "'" + detalhesDocumento.getSobresalente() + "' , "
                + "'" + detalhesDocumento.getStop() + "' , "
                + "'" + detalhesDocumento.getTampasJantes() + "' , "
                + "'" + detalhesDocumento.getTampoes() + "' , "
                + "'" + detalhesDocumento.getTapetes() + "' , "
                + "'" + detalhesDocumento.getTejadilho() + "' , "
                + "'" + detalhesDocumento.getTriangulo() + "' , "
                + "'" + detalhesDocumento.getVidros() + "' , "
                + "'" + detalhesDocumento.getNotas() + "' , "
                + "'" + MetodosUtil.getDataBancoFull( detalhesDocumento.getDataEntrada() ) + "' , "
                + detalhesDocumento.getCodVenda()
                + " ) ";

        System.out.println( INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_detalhes_documento )
    {
        String DELETE = "DELETE FROM detalhes_documento WHERE pk_detalhes_documento = " + pk_detalhes_documento;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<DetalhesDocumento> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM detalhes_documento ORDER BY pk_detalhes_documento ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<DetalhesDocumento> lista_detalhesDocumento = new ArrayList<>();
        DetalhesDocumento detalhesDocumento;
        try
        {

            while ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
                lista_detalhesDocumento.add( detalhesDocumento );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_detalhesDocumento;
    }

    @Override
    public Vector<String> getVector()
    {
        {
            String FIND_ALL = "SELECT designacao FROM detalhes_documento ORDER BY pk_detalhes_documento ASC";
            ResultSet result = conexao.executeQuery( FIND_ALL );
            Vector<String> lista_armazens = new Vector<>();
            try
            {

                while ( result.next() )
                {

                    lista_armazens.add( result.getString( "designacao" ) );

                }

            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            return lista_armazens;
        }
    }

    public Vector<String> getVector( int id )
    {
        String FIND_ALL = "SELECT designacao FROM detalhes_documento  WHERE pk_detalhes_documento = " + id;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> list = new Vector<>();
        try
        {
            while ( result.next() )
            {
                list.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;
    }

    public Vector<String> getVector2()
    {

//        String FIND_ALL = "SELECT designacao FROM detalhes_documento  WHERE pk_detalhes_documento < 2";
        String FIND_ALL = "SELECT designacao FROM detalhes_documento  WHERE pk_detalhes_documento > 1";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> list = new Vector<>();
        DetalhesDocumento detalhesDocumento;
        try
        {
            while ( result.next() )
            {
                list.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Object findById( int pk_detalhes_documento )
    {

        String FIND__BY_CODIGO = "SELECT * FROM detalhes_documento WHERE pk_detalhes_documento = " + pk_detalhes_documento;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
//                detalhesDocumento.setCodigo( result.getInt( "pk_detalhes_documento" ) );
//                detalhesDocumento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

    public DetalhesDocumento getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_detalhes_documento) as maximo_id, a.*  FROM detalhes_documento a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

    public DetalhesDocumento getArmazemByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM detalhes_documento a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
//                detalhesDocumento.setCodigo( result.getInt( "pk_detalhes_documento" ) );
//                detalhesDocumento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

    public Object findByName( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM detalhes_documento WHERE designacao = '" + designacao + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
//                detalhesDocumento.setCodigo( result.getInt( "pk_detalhes_documento" ) );
//                detalhesDocumento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

    public DetalhesDocumento findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM detalhes_documento WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
//                detalhesDocumento.setCodigo( result.getInt( "pk_detalhes_documento" ) );
//                detalhesDocumento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

    public DetalhesDocumento findByCodigo( int pk_detalhes_documento )
    {

        String FIND__BY_CODIGO = "SELECT * FROM detalhes_documento WHERE pk_detalhes_documento = " + pk_detalhes_documento + "";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        DetalhesDocumento detalhesDocumento = null;
        try
        {

            if ( result.next() )
            {
                detalhesDocumento = new DetalhesDocumento();
//                detalhesDocumento.setCodigo( result.getInt( "pk_detalhes_documento" ) );
//                detalhesDocumento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return detalhesDocumento;

    }

}
