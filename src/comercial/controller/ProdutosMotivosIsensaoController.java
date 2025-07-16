/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.ProdutosMotivosIsensao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProdutosMotivosIsensaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ProdutosMotivosIsensaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ProdutosMotivosIsensao mesas = ( ProdutosMotivosIsensao ) object;
        String INSERT = "INSERT INTO produtos_motivos_isensao( regime , descricao, codigo_regime"
                + ")"
                + " VALUES("
                //                + mesas.getValor() + ", "
                //                + "'" + MetodosUtil.getDataBancoFull( mesas.getDataHora() ) + "', "
                //                + mesas.getFkMoeda().getPkMoeda()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_cambio )
    {
        String DELETE = "DELETE FROM produtos_motivos_isensao WHERE pk_cambio = " + pk_cambio;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ProdutosMotivosIsensao> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM produtos_motivos_isensao";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ProdutosMotivosIsensao> list = new ArrayList<>();
        ProdutosMotivosIsensao produtos_motivos_isensao;
        try
        {
            while ( result.next() )
            {
                produtos_motivos_isensao = new ProdutosMotivosIsensao();
                produtos_motivos_isensao.setPkProdutosMotivosIsensao( result.getInt( "pk_produtos_motivos_isensao" ) );
                produtos_motivos_isensao.setRegime( result.getString( "regime" ) );
                produtos_motivos_isensao.setDescricao( result.getString( "descricao" ) );
                produtos_motivos_isensao.setCodigoRegime( result.getString( "codigo_regime" ) );
                list.add( produtos_motivos_isensao );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Vector<String> getVector()
    {

        Vector<String> vector = new Vector<>();

        return vector;

    }

    @Override
    public Object findById( int id )
    {

        String FIND_BY_CODIGO = "SELECT * FROM produtos_motivos_isensao WHERE pk_produtos_motivos_isensao = " + id;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ProdutosMotivosIsensao produtos_motivos_isensao = null;
        try
        {
            if ( result.next() )
            {
                produtos_motivos_isensao = new ProdutosMotivosIsensao();
                produtos_motivos_isensao.setPkProdutosMotivosIsensao( result.getInt( "pk_produtos_motivos_isensao" ) );
                produtos_motivos_isensao.setRegime( result.getString( "regime" ) );
                produtos_motivos_isensao.setDescricao( result.getString( "descricao" ) );
                produtos_motivos_isensao.setCodigoRegime( result.getString( "codigo_regime" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produtos_motivos_isensao;

    }

    public ProdutosMotivosIsensao getRegime( String regime )
    {

        String FIND_BY_CODIGO = "SELECT * FROM produtos_motivos_isensao WHERE  regime = '" + regime + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ProdutosMotivosIsensao produtos_motivos_isensao = null;
        try
        {
            if ( result.next() )
            {
                produtos_motivos_isensao = new ProdutosMotivosIsensao();
                produtos_motivos_isensao.setPkProdutosMotivosIsensao( result.getInt( "pk_produtos_motivos_isensao" ) );
                produtos_motivos_isensao.setRegime( result.getString( "regime" ) );
                produtos_motivos_isensao.setDescricao( result.getString( "descricao" ) );
                produtos_motivos_isensao.setCodigoRegime( result.getString( "codigo_regime" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produtos_motivos_isensao;

    }

}
