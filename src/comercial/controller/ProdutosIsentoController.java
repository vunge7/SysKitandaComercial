/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.ProdutoIsento;
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
public class ProdutosIsentoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ProdutosIsentoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ProdutoIsento item = ( ProdutoIsento ) object;
        String INSERT = "INSERT INTO produto_isento( fk_produto ,fk_produtos_motivos_isensao"
                + ")"
                + " VALUES("
                + item.getFkProduto().getCodigo() + ", "
                + item.getFkProdutosMotivosIsensao().getPkProdutosMotivosIsensao()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_produto_isento )
    {
        String DELETE = "DELETE FROM produto_isento WHERE pk_produto_isento = " + pk_produto_isento;
        return conexao.executeUpdate( DELETE );
    }
    public boolean eliminarByIdProduto( int codProduto )
    {
        String DELETE = "DELETE FROM produto_isento WHERE fk_produto = " + codProduto;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ProdutoIsento> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM produto_isento ORDER BY pk_produto_isento ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ProdutoIsento> list = new ArrayList<>();
        ProdutoIsento produto_isento;
        try
        {
            while ( result.next() )
            {
                produto_isento = new ProdutoIsento();
//                produto_isento.setValor( result.getDouble( "valor" ) );
//                produto_isento.setDataHora( result.getDate( "data_hora" ) );
//                produto_isento.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
                list.add( produto_isento );

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
    public Object findById( int pk_produto_isento )
    {

        String FIND_BY_CODIGO = "SELECT * FROM produto_isento WHERE pk_produto_isento = " + pk_produto_isento;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ProdutoIsento produto_isento = null;
        try
        {
            if ( result.next() )
            {
                produto_isento = new ProdutoIsento();
//                produto_isento.setValor( result.getDouble( "valor" ) );
//                produto_isento.setDataHora( result.getDate( "data_hora" ) );
//                produto_isento.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return produto_isento;

    }

    public String getRegimeIsensaoByIdProduto( int pkProduto )
    {
        String FIND_BY_CODIGO = "SELECT m.regime AS regime FROM produto_isento p "
                + "INNER JOIN produtos_motivos_isensao m ON m.pk_produtos_motivos_isensao = p.fk_produtos_motivos_isensao WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        String regime = "";
        try
        {
            if ( result.next() )
            {
                regime = result.getString( "regime" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }
    
    public String getRetensaoByIdProduto( int pkProduto )
    {
        String FIND_BY_CODIGO = "SELECT fk_retencao AS retencao FROM servico_retencao s "
                + "WHERE s.fk_produto = " + pkProduto;
//                + "INNER JOIN produtos_motivos_isensao m ON m.pk_produtos_motivos_isensao = p.fk_produtos_motivos_isensao WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        String regime = "";
        try
        {
            if ( result.next() )
            {
                regime = result.getString( "retencao" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }
    public String getRetensaoByIdProduto1( int pkProduto )
    {
        String FIND_BY_CODIGO = "SELECT m.retencao AS retencao FROM servico_retencao p "
                + "WHERE s.fk_produto = " + pkProduto;
//                + "INNER JOIN produtos_motivos_isensao m ON m.pk_produtos_motivos_isensao = p.fk_produtos_motivos_isensao WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        String regime = "";
        try
        {
            if ( result.next() )
            {
                regime = result.getString( "retencao" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }

}
