/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 14/jan/2026
 * @lastModified 14/jan/2026
 */
import entity.Series;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

public class SeriesController implements EntidadeFactory
{

    private BDConexao conexao;

    public SeriesController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    // =====================================================
    // INSERT
    // =====================================================
    @Override
    public boolean salvar( Object object )
    {
        Series s = ( Series ) object;

        String INSERT = "INSERT INTO series (designacao, fk_documento, fk_ano_economico) VALUES ("
                + "'" + s.getDesignacao() + "', "
                + s.getFkDocumento() + ", "
                + s.getFkAnoEconomico()
                + ")";

        System.out.println( "INSERT: " + INSERT );
        return conexao.executeUpdate( INSERT );
    }

    // =====================================================
    // UPDATE
    // =====================================================
    @Override
    public boolean actualizar( Object object )
    {
        Series s = ( Series ) object;

        String UPDATE = "UPDATE series SET "
                + "designacao = '" + s.getDesignacao() + "', "
                + "fk_documento = " + s.getFkDocumento() + ", "
                + "fk_ano_economico = " + s.getFkAnoEconomico()
                + " WHERE id = " + s.getId();

        System.out.println( "UPDATE: " + UPDATE );
        return conexao.executeUpdate( UPDATE );
    }

    // =====================================================
    // DELETE
    // =====================================================
    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM series WHERE id = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================
    @Override
    public List<Series> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM series ORDER BY id ASC";
        ResultSet rs = conexao.executeQuery( FIND_ALL );

        List<Series> lista = new ArrayList<>();

        try
        {
            while ( rs.next() )
            {
                Series s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );

                lista.add( s );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // FIND BY ID
    // =====================================================
    @Override
    public Series findById( int codigo )
    {
        String FIND = "SELECT * FROM series WHERE id = " + codigo;
        ResultSet rs = conexao.executeQuery( FIND );

        Series s = null;

        try
        {
            if ( rs.next() )
            {
                s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return s;
    }

    // =====================================================
    // FIND BY DESIGNAÇÃO
    // =====================================================
    public Series findByDesignacao( String designacao )
    {
        String FIND = "SELECT * FROM series WHERE designacao = '" + designacao + "'";
        ResultSet rs = conexao.executeQuery( FIND );

        Series s = null;

        try
        {
            if ( rs.next() )
            {
                s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return s;
    }

    // =====================================================
    // LISTAR POR DOCUMENTO
    // =====================================================
    public List<Series> listarPorDocumento( int fkDocumento )
    {
        String FIND = "SELECT * FROM series WHERE fk_documento = " + fkDocumento;
        ResultSet rs = conexao.executeQuery( FIND );

        List<Series> lista = new ArrayList<>();

        try
        {
            while ( rs.next() )
            {
                Series s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );
                lista.add( s );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // LISTAR POR ANO ECONÓMICO
    // =====================================================
    public List<Series> listarPorAnoEconomico( int fkAno )
    {
        String FIND = "SELECT * FROM series WHERE fk_ano_economico = " + fkAno;
        ResultSet rs = conexao.executeQuery( FIND );

        List<Series> lista = new ArrayList<>();

        try
        {
            while ( rs.next() )
            {
                Series s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );
                lista.add( s );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // VECTOR PARA COMBOBOX
    // =====================================================
    @Override
    public Vector<String> getVector()
    {
        String sql = "SELECT designacao FROM series ORDER BY designacao ASC";
        ResultSet rs = conexao.executeQuery( sql );

        Vector<String> lista = new Vector<>();

        try
        {
            while ( rs.next() )
            {
                lista.add( rs.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
// FIND BY DOCUMENTO + ANO ECONÓMICO
// =====================================================
    public Series findByDocumentoEAno( int fkDocumento, int fkAnoEconomico )
    {
        String SQL = "SELECT * FROM series "
                + "WHERE fk_documento = " + fkDocumento
                + " AND fk_ano_economico = " + fkAnoEconomico;

        System.out.println( "SQL: " + SQL );

        ResultSet rs = conexao.executeQuery( SQL );

        Series s = null;

        try
        {
            if ( rs.next() )
            {
                s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return s;
    }

    // =====================================================
// LISTAR SERIES POR DOCUMENTO + ANO ECONÓMICO
// =====================================================
    public List<Series> listarPorDocumentoEAno( int fkDocumento, int fkAnoEconomico )
    {
        String SQL = "SELECT * FROM series "
                + "WHERE fk_documento = " + fkDocumento
                + " AND fk_ano_economico = " + fkAnoEconomico
                + " ORDER BY designacao ASC";

        System.out.println( "SQL: " + SQL );

        ResultSet rs = conexao.executeQuery( SQL );

        List<Series> lista = new ArrayList<>();

        try
        {
            while ( rs.next() )
            {
                Series s = new Series();
                s.setId( rs.getInt( "id" ) );
                s.setDesignacao( rs.getString( "designacao" ) );
                s.setFkDocumento( rs.getInt( "fk_documento" ) );
                s.setFkAnoEconomico( rs.getInt( "fk_ano_economico" ) );

                lista.add( s );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

}
