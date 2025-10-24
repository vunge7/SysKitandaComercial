/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.controller;


import java.sql.Connection;
import entity.TbCliente;
import entity.TbMesas;
import entity.TbProfissao;
import entity.TbSexo;
import entity.TbStatus;
import entity.TbTipoUsuario;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
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
        return true;
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
        String FIND_ALL = "SELECT * FROM tb_mesas ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbMesas> lista_mesa = new ArrayList<>();
        TbMesas mesas;
        try
        {
            while ( result.next() )
            {
                mesas = new TbMesas();
                lista_mesa.add( getMesasResultSet( mesas, result ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_mesa;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_mesas WHERE pk_mesas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbMesas mesas = null;
        try
        {
            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas = getMesasResultSet( mesas, result );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return mesas;

    }

    public TbMesas findUsuarioById( int codigo, ResultSet resultSet )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_mesas WHERE pk_mesas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbMesas mesas = null;
        try
        {
            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas = getMesasResultSet( mesas, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    private TbMesas getMesasResultSet( TbMesas mesas, ResultSet result )
    {
        try
        {
            mesas.setPkMesas(result.getInt( "pk_mesas" ) );
            mesas.setDesignacao(result.getString( "designacao" ) );


        }
        catch ( Exception e )
        {
        }

        return mesas;
    }

    public static void main( String[] args )
    {
    }

}
