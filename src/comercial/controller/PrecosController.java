/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbPreco;
import entity.TbProduto;
import entity.TbUsuario;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class PrecosController implements EntidadeFactory
{

    private BDConexao conexao;

    public PrecosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbPreco preco = (TbPreco) object;
        String INSERT = "INSERT INTO tb_preco( data  ,  preco_compra , percentagem_ganho , fk_produto , fk_usuario , preco_venda , qtd_baixo , qtd_alto , preco_anterior , retalho "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBanco( preco.getData() ) + "',"
                //                + "'" + MetodosUtil.getHoraBanco( preco.getHora() ) + "',"
                + preco.getPrecoCompra() + " , "
                + preco.getPercentagemGanho() + " , "
                + preco.getFkProduto().getCodigo() + ", "
                + preco.getFkUsuario().getCodigo() + ", "
                + preco.getPrecoVenda() + " , "
                + "'" + preco.getQtdBaixo() + "' , "
                + "'" + preco.getQtdAlto() + "' , "
                + preco.getPrecoAnterior() + " , "
                + preco.getRetalho()
                + " ) ";

        System.out.println( "QUERY " + INSERT );
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
        String DELETE = "DELETE FROM tb_preco WHERE pk_preco = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbPreco> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_preco ORDER BY pk_preco ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbPreco> lista_preco = new ArrayList<>();
        TbPreco preco;
        try
        {

            while ( result.next() )
            {

                preco = new TbPreco();
                setResultSetPreco( result, preco );
                lista_preco.add( preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_preco;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_preco WHERE pk_preco = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbPreco preco = null;
        try
        {

            if ( result.next() )
            {
                preco = new TbPreco();
                setResultSetPreco( result, preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return preco;

    }

    public TbPreco getLastPreco( int idProduto )
    {

        String sql = "SELECT * FROM tb_preco WHERE pk_preco = (SELECT MAX(pk_preco)  FROM tb_preco  WHERE fk_produto = " + idProduto + ")";
        ResultSet result = conexao.executeQuery( sql );
        TbPreco preco = null;
        try
        {

            if ( result.next() )
            {
                preco = new TbPreco();
                setResultSetPreco( result, preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return preco;

    }

    public TbPreco getLastIdPrecoByIdProduto( int idProduto, double qtd )
    {

        String sql = "SELECT * FROM tb_preco WHERE pk_preco = ( "
                + "	SELECT MAX(pk_preco) as maximo_id  "
                + "	FROM tb_preco  "
                + "	WHERE fk_produto = " + idProduto + " AND " + qtd + " BETWEEN qtd_baixo AND qtd_alto "
                + ")";
        ResultSet result = conexao.executeQuery( sql );
        TbPreco preco = null;
        try
        {

            if ( result.next() )
            {
                preco = new TbPreco();
                setResultSetPreco( result, preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return preco;

    }

    public TbPreco getLastIdPrecoByIdProdutos( int idProduto, double qtd )
    {

//        String FIND__BY_CODIGO = "SELECT MAX(pk_preco) as maximo_id, p.*  FROM tb_preco p WHERE p.fk_produto = " + idProduto + " AND " + qtd + "BETWEEN p.qtd_baixo AND p.qtd_alto";
        String FIND__BY_CODIGO = "SELECT * FROM tb_preco WHERE pk_preco = ( "
                + "	SELECT MAX(pk_preco) as maximo_id  "
                + "	FROM tb_preco  "
                + "	WHERE fk_produto = " + idProduto + " AND " + qtd + " BETWEEN qtd_baixo AND qtd_alto "
                + ")";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbPreco preco = null;
        try
        {

            if ( result.next() )
            {
                preco = new TbPreco();
                setResultSetPreco( result, preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return preco;

    }

    public TbPreco getLastIdPrecoByIdProdutos( int idProduto )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_preco WHERE pk_preco = (SELECT MAX(pk_preco)  FROM tb_preco  WHERE fk_produto = " + idProduto + ")";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbPreco preco = null;
        try
        {

            if ( result.next() )
            {
                preco = new TbPreco();
                setResultSetPreco( result, preco );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return preco;

    }

    public static int getLastIdPrecoByIdProdutoIntAndQTD( int idProduto, double qtd, BDConexao conexao )
    {

        String sql = "SELECT MAX(pk_preco) AS MAXIMO FROM tb_preco p WHERE p.fk_produto = " + idProduto + " AND " + qtd + " BETWEEN p.qtd_baixo AND p.qtd_alto";

        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getInt( "MAXIMO" );
            }
        }
        catch ( SQLException ex )
        {

        }
        return 0;

    }

    public static int getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto( int fk_produto, int qtd_baixo, BDConexao conexao )
    {

//        String sql = "SELECT MAX(pk_preco) AS MAXIMO FROM tb_preco p WHERE p.fk_produto = " + fk_produto + " AND qtd_alto = " + qtd_alto;
        String sql = "SELECT MAX(pk_preco) AS MAXIMO FROM tb_preco p WHERE p.fk_produto = " + fk_produto + " AND p.qtd_baixo = " + qtd_baixo;
        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getInt( "MAXIMO" );
            }
        }
        catch ( SQLException ex )
        {

        }
        return 0;

    }

    private void setResultSetPreco( ResultSet result, TbPreco preco )
    {
        try
        {
            preco.setPkPreco( result.getInt( "pk_preco" ) );
            preco.setData( result.getDate( "data" ) );
            preco.setHora( result.getDate( "hora" ) );
            preco.setPrecoCompra( result.getBigDecimal( "preco_compra" ) );
            preco.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
            preco.setFkProduto( new TbProduto( result.getInt( "fk_produto" ) ) );
            preco.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
            preco.setPrecoVenda( result.getBigDecimal( "preco_venda" ) );
            preco.setQtdBaixo( result.getInt( "qtd_baixo" ) );
            preco.setQtdAlto( result.getInt( "qtd_alto" ) );
            preco.setPrecoAnterior( result.getDouble( "preco_anterior" ) );
            preco.setRetalho( result.getBoolean( "retalho" ) );
        }
        catch ( Exception e )
        {
        }

    }

    /**
     * Retorna o preço médio dos últimos 5 registros de um produto com base no
     * preco_compra
     *
     * @param idProduto o código do produto
     * @return preço médio dos últimos 5 registros como BigDecimal, ou
     * BigDecimal.ZERO se não houver registros
     */
    public BigDecimal getPrecoMedioUltimos10( int idProduto, BigDecimal precoCompraAtual )
    {
        String sql = "SELECT AVG(preco_compra) AS pm FROM ("
                + "    SELECT preco_compra FROM tb_preco "
                + "    WHERE fk_produto = " + idProduto + " AND qtd_alto = 5"
                + "    ORDER BY pk_preco DESC LIMIT 4" // pega 4 do banco
                + ") AS ultimos";

        ResultSet result = conexao.executeQuery( sql );
        BigDecimal soma = precoCompraAtual != null ? precoCompraAtual : BigDecimal.ZERO;
        int count = precoCompraAtual != null ? 1 : 0;

        try
        {
            if ( result.next() )
            {
                BigDecimal ultimo = result.getBigDecimal( "pm" );
                if ( ultimo != null )
                {
                    // média ponderando o valor atual + últimos 4 do banco
                    soma = soma.add( ultimo.multiply( BigDecimal.valueOf( 4 ) ) );
                    count += 4;
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return count > 0 ? soma.divide( BigDecimal.valueOf( count ), 2, RoundingMode.HALF_UP ) : BigDecimal.ZERO;
    }

}
