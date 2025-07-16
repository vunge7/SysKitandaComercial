/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbCliente;
import entity.TbDesconto;
import entity.TbProduto;
import entity.TbUsuario;
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
public class DescontosController implements EntidadeFactory
{

    private BDConexao conexao;

    public DescontosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbDesconto mesas = ( TbDesconto ) object;
        String INSERT = "INSERT INTO tb_desconto( descricao "
                + ")"
                + " VALUES("
                + "'" + mesas.getData() + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int idDesconto )
    {
        String DELETE = "DELETE FROM tb_desconto WHERE idDesconto = " + idDesconto;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbDesconto> listarTodos()
    {

//        String FIND_ALL = "SELECT * FROM tb_desconto ORDER BY idDesconto ASC";
        List<TbDesconto> lista_tb_decontos = new ArrayList<>();

        return lista_tb_decontos;
    }

    @Override
    public Vector<String> getVector()
    {
//        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_mesas = new Vector<>();

        return lista_mesas;

    }

    @Override
    public Object findById( int idDesconto )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_desconto WHERE idDesconto = " + idDesconto;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbDesconto tb_desconto = null;

        return tb_desconto;

    }

    public TbDesconto get_desconto_cliente_produto( int id_cliente, int id_produto )
    {

        String sql = "SELECT * FROM tb_desconto WHERE fk_cliente = " + id_cliente + " AND fk_produto = " + id_produto;
        System.out.println( "SQL: " +sql );
        ResultSet result = conexao.executeQuery( sql );
        TbDesconto desconto = null;

        try
        {
            if ( result.next() )
            {
                desconto = new TbDesconto();
                desconto.setData( result.getDate( "data" ) );
                desconto.setHora( result.getTime( "hora" ) );
                desconto.setQuantidade( result.getInt( "quantidade" ) );
                desconto.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
                desconto.setFkCliente( new TbCliente( result.getInt( "fk_cliente" ) ) );
                desconto.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
                desconto.setValor( result.getDouble( "valor" ) );

            }
        }
        catch ( SQLException e )
        {
        }

        return desconto;

    }

}
