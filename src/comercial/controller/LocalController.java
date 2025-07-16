/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbLocal;
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
public class LocalController implements EntidadeFactory
{

    private BDConexao conexao;

    public LocalController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbLocal mesas = ( TbLocal ) object;
        String INSERT = "INSERT INTO tb_local( designacao "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "'"
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
        String DELETE = "DELETE FROM tb_local WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbLocal> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_local ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbLocal> lista_mesas = new ArrayList<>();
        TbLocal mesas;
        try
        {

            while ( result.next() )
            {
                mesas = new TbLocal();
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
        String FIND_ALL = "SELECT * FROM tb_local ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_mesas.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_mesas;

    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_local WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbLocal mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbLocal();
                mesas.setCodigo(result.getInt( "codigo" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public TbLocal getTbLocalByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM tb_local a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbLocal armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbLocal();
                armazem.setCodigo(result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

}
