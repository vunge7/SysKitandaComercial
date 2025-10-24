/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbStatus;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStatus;
import entity.TbVenda;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javafx.animation.Animation.Status;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class StatusController implements EntidadeFactory
{

    private BDConexao conexao;

    public StatusController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbStatus status = ( TbStatus ) object;
        String INSERT = "INSERT INTO tb_status( descrisao , "
                + ")"
                + " VALUES("
                + "'" + status.getDescrisao() + "' , "
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
        String DELETE = "DELETE FROM tb_status WHERE idStatus = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbStatus> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_status ORDER BY idStatus ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbStatus> lista_status = new ArrayList<>();
        TbStatus status;
        try
        {

            while ( result.next() )
            {
                status = new TbStatus();
                status.setDescrisao( result.getString( "descrisao" ) );
                lista_status.add( status );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_status;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_status WHERE idStatus = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStatus status = null;
        try
        {

            if ( result.next() )
            {
                status = new TbStatus();
                status.setIdStatus( result.getInt( "idStatus" ) );
                status.setDescrisao( result.getString( "descrisao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return status;

    }

    public TbStatus getSatusByDesignacao( String descrisao )
    {

        String FIND_BY_CODIGO = "SELECT *  FROM tb_status  WHERE descrisao = '" + descrisao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbStatus status = null;
        try
        {
            if ( result.next() )
            {
                status = new TbStatus();
                setResultSetStatus( result, status );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return status;

    }

    public Object findByDesignacao( String descrisao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_status WHERE descrisao = '" + descrisao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStatus status = null;
        try
        {

            if ( result.next() )
            {
                status = new TbStatus();
                status.setIdStatus( result.getInt( "idStatus" ) );
                status.setDescrisao( result.getString( "descrisao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return status;

    }

    public TbStatus getLastMesa()
    {

        String FIND__BY_CODIGO = "SELECT MAX(idStatus) as maximo_id, m.*  FROM tb_status m";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbStatus status = null;
        try
        {

            if ( result.next() )
            {
                status = new TbStatus();
                status.setIdStatus( result.getInt( "maximo_id" ) );
                status.setDescrisao( result.getString( "descrisao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return status;

    }

    private void setResultSetStatus( ResultSet rs, TbStatus status )
    {
        try
        {
            status.setIdStatus(rs.getInt( "idStatus" ) );
            status.setDescrisao(rs.getString( "descrisao" ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
