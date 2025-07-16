/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.ProdutoImposto;
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
public class ProdutosImpostoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ProdutosImpostoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ProdutoImposto item = ( ProdutoImposto ) object;
        System.out.println( "ID IMPOSTO CONTROLLER: " + item.getFkImposto().getPkImposto() );
        String INSERT = "INSERT INTO produto_imposto( fk_produto , fk_imposto"
                + ")"
                + " VALUES("
                + item.getFkProduto().getCodigo() + ", "
                + item.getFkImposto().getPkImposto()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codProduto )
    {
        String DELETE = "DELETE FROM produto_imposto WHERE fk_produto = " + codProduto;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ProdutoImposto> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM produto_imposto ORDER BY pk_cambio ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ProdutoImposto> list = new ArrayList<>();
        ProdutoImposto produto_imposto;
        try
        {
            while ( result.next() )
            {
                produto_imposto = new ProdutoImposto();
//                produto_imposto.setValor( result.getDouble( "valor" ) );
//                produto_imposto.setDataHora( result.getDate( "data_hora" ) );
//                produto_imposto.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
                list.add( produto_imposto );

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
    public Object findById( int pk_cambio )
    {

        String FIND_BY_CODIGO = "SELECT * FROM produto_imposto WHERE pk_cambio = " + pk_cambio;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ProdutoImposto produto_imposto = null;
        try
        {
            if ( result.next() )
            {
                produto_imposto = new ProdutoImposto();
//                produto_imposto.setValor( result.getDouble( "valor" ) );
//                produto_imposto.setDataHora( result.getDate( "data_hora" ) );
//                produto_imposto.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produto_imposto;

    }

    public double getTaxaByIdProduto( int pkProduto )
    {
        String FIND_BY_CODIGO = "SELECT i.taxa AS taxa FROM produto_imposto p "
                + "INNER JOIN imposto i ON i.pk_imposto = p.fk_imposto WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        double taxa = 0;
        try
        {
            if ( result.next() )
            {
                taxa = result.getDouble( "taxa" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return taxa;

    }

    public boolean existeProdutoImposto( int codProduto )
    {
        String query = "SELECT * FROM produto_imposto  WHERE fk_produto  = " + codProduto;
        ResultSet rs = conexao.executeQuery( query );
        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }
        return false;

    }

}
