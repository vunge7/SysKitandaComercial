/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbArmazem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Martinho Luis
 */
public class ArmazensController implements EntidadeFactory
{

    private BDConexao conexao;

    public ArmazensController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbArmazem armazem = ( TbArmazem ) object;
        String INSERT = "INSERT INTO tb_armazem( designacao , "
                + ")"
                + " VALUES("
                + "'" + armazem.getDesignacao() + "' , "
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
        String DELETE = "DELETE FROM tb_armazem WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbArmazem> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_armazem ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbArmazem> lista_armazem = new ArrayList<>();
        TbArmazem armazem;
        try
        {

            while ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setDesignacao( result.getString( "designacao" ) );
                lista_armazem.add( armazem );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_armazem;
    }

    @Override
    public Vector<String> getVector()
    {
        {
            String FIND_ALL = "SELECT designacao FROM tb_armazem ORDER BY codigo ASC";
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
    
    public Vector<String> getVectorArmazemVenda()
    {
        {
            String FIND_ALL = "SELECT designacao FROM tb_armazem ORDER BY codigo DESC";
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
        String FIND_ALL = "SELECT designacao FROM tb_armazem  WHERE codigo = " + id;
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

//        String FIND_ALL = "SELECT designacao FROM tb_armazem  WHERE codigo < 2";
        String FIND_ALL = "SELECT designacao FROM tb_armazem  WHERE codigo > 1";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> list = new Vector<>();
        TbArmazem armazem;
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
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_armazem WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public TbArmazem getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, a.*  FROM tb_armazem a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "maximo_id" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public TbArmazem getArmazemByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM tb_armazem a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public Object findByName( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_armazem WHERE designacao = '" + designacao + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public TbArmazem findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_armazem WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public TbArmazem findByCodigo( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_armazem WHERE codigo = " + codigo + "";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbArmazem();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }
    
    public int getCodigoPorDesignacao(String nomeArmazem) {
    int codigo = -1;
    String sql = "SELECT codigo FROM tb_armazem WHERE designacao = ?";

    try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
        ps.setString(1, nomeArmazem);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                codigo = rs.getInt("codigo");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return codigo;
}


}
