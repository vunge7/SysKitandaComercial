/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import entity.TbUsuario;
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
public class MesasController implements EntidadeFactory
{

    private BDConexao conexao;

    public MesasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbMesas mesas = ( TbMesas ) object;
        String INSERT = "INSERT INTO tb_mesas( designacao , "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "' , "
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
        String DELETE = "DELETE FROM tb_mesas WHERE pk_mesas = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbMesas> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_mesas ORDER BY pk_mesas ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbMesas> lista_mesas = new ArrayList<>();
        TbMesas mesas;
        try
        {

            while ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setDesignacao( result.getString( "designacao" ) );
                lista_mesas.add( mesas );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_mesas;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE pk_mesas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public Object findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public TbMesas getLastMesa()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_mesas) as maximo_id, m.*  FROM tb_mesas m";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "maximo_id" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

}
