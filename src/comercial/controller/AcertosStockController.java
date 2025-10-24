/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AcertoStock;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AcertosStockController implements EntidadeFactory
{
    private final BDConexao conexao;
    public AcertosStockController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        AcertoStock acertoStock = ( AcertoStock ) object;
        String INSERT = "INSERT INTO acerto_stock( "
                + " data_hora, cod_produto, designacao_produto ,"
                + " cod_usuario , nome_usuario, qtd_anterior, qtd_acerto, qtd_depois,  "
                + " cod_armazem, designcao_armazem, motivo_acerto "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull(acertoStock.getDataHora() ) + "' , "
                + acertoStock.getCodProduto() + ", "
                + "'" + acertoStock.getDesignacaoProduto() + "', "
                + acertoStock.getCodUsuario() + ", "
                + "'" + acertoStock.getNomeUsuario() + "', "
                + acertoStock.getQtdAnterior() + ", "
                + acertoStock.getQtdAcerto() + ", "
                + acertoStock.getQtdDepois() + ", "
                + acertoStock.getCodArmazem() + ", "
                + "'" + acertoStock.getDesigncaoArmazem() + "', "
                + "'" +acertoStock.getMotivoAcerto() + "'"
                + " ) ";
        System.err.println( INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        AcertoStock acertoStock = ( AcertoStock ) object;
        PreparedStatement stmt;
        String UPDATE = "UPDATE acerto_stock SET "
                + " data_hora = ?, "
                + " cod_produto = ? ,   "
                + " designacao_produto = ? , "
                + " cod_usuario = ?,  "
                + " nome_usuario = ? , "
                + " qtd_anterior = ? , "
                + " qtd_acerto = ?  , "
                + " qtd_depois = ?, "
                + " cod_armazem = ? , "
                + " designcao_armazem = ?, "
                + " motivo_acerto = ?, "
                + " WHERE pk_acerto_stock = ?";
        try
        {
            System.out.println( UPDATE );

            stmt = conexao.getConnection().prepareStatement( UPDATE );

            int cod = 1;
            stmt.setString( cod++, MetodosUtil.getDataBancoFull( acertoStock.getDataHora() ) );
            stmt.setInt( cod++, acertoStock.getCodProduto() );
            stmt.setString( cod++, acertoStock.getDesignacaoProduto() );
            stmt.setInt( cod++, acertoStock.getCodUsuario() );
            stmt.setString( cod++, acertoStock.getNomeUsuario() );
            stmt.setDouble( cod++, acertoStock.getQtdAnterior() );
            stmt.setDouble( cod++, acertoStock.getQtdAcerto() );
            stmt.setDouble( cod++, acertoStock.getQtdDepois() );
            stmt.setInt( cod++, acertoStock.getCodArmazem() );
            stmt.setString( cod++, acertoStock.getDesigncaoArmazem() );
            stmt.setString( cod++, acertoStock.getMotivoAcerto() );
            stmt.setLong( cod++, acertoStock.getPkAcertoStock() );

            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM acerto_stock WHERE pk_acerto_stock = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<AcertoStock> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM acerto_stock ORDER BY pk_acerto_stock ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<AcertoStock> lista_acertoStock = new ArrayList<>();
        AcertoStock acertoStock;
        try
        {
            while ( result.next() )
            {
                acertoStock = new AcertoStock();
                setResultAcertoStock( result, acertoStock );
                lista_acertoStock.add( acertoStock );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_acertoStock;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM acerto_stock ORDER BY codigo ASC";
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

        String sql = "SELECT * FROM acerto_stock WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( sql );
        AcertoStock acertoStock = null;

        try
        {
            if ( result.next() )
            {
                acertoStock = new AcertoStock();
                setResultAcertoStock( result, acertoStock );
            }
        }
        catch ( SQLException e )
        {
        }
        return acertoStock;

    }

    private void setResultAcertoStock( ResultSet result, AcertoStock acertoStock )
    {
        try
        {

            acertoStock.setPkAcertoStock( result.getLong( "pk_acerto_stock" ) );
            acertoStock.setDataHora( result.getDate( "data_hora" ) );
            acertoStock.setCodProduto( result.getInt( "cod_produto" ) );
            acertoStock.setDesignacaoProduto( result.getString( "designacao_produto" ) );
            acertoStock.setCodUsuario( result.getInt( "cod_usuario" ) );
            acertoStock.setNomeUsuario( result.getString( "nome_usuario" ) );
            acertoStock.setQtdAnterior( result.getDouble( "qtd_anterior" ) );
            acertoStock.setQtdAcerto( result.getDouble( "qtd_acerto" ) );
            acertoStock.setQtdDepois( result.getDouble( "qtd_depois" ) );
            acertoStock.setCodArmazem( result.getInt( "cod_armazem" ) );
            acertoStock.setDesignacaoProduto( result.getString( "designcao_armazem" ) );
            acertoStock.setMotivoAcerto(result.getString( "motivo_acerto" ) );

        }
        catch ( SQLException e )
        {
        }

    }

    public static void main( String[] args )
    {
        BDConexao conexao = BDConexao.getInstancia();
        AcertosStockController pc = new AcertosStockController( conexao );
    }

}
