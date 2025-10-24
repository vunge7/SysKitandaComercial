/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.FichaTecnica;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis & Domingos Dala Vunge
 */
public class FichaTecnicaController implements EntidadeFactory
{

    private BDConexao conexao;

    public FichaTecnicaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        FichaTecnica ficha_tecnica = (FichaTecnica) object;
        String INSERT = "INSERT INTO ficha_tecnica("
                + " produto_id , "
                + " data_criacao , "
                + " usuario_id_criacao , "
                + " data_actualizacao , "
                + " usuario_id_actualizacao , "
                + " custo_produto , "
                + " percentagem_ganho ,"
                + " custo_venda , "
                + " qtd_composto , "
                + " prato , "
                + " photo "
                + ")"
                + " VALUES("
                + ficha_tecnica.getProdutoId() + " , "
                + "'" + MetodosUtil.getDataBancoFull( ficha_tecnica.getDataCriacao() ) + "' , "
                + ficha_tecnica.getUsuarioIdCriacao() + " , "
                + "'" + MetodosUtil.getDataBancoFull( ficha_tecnica.getDataActualizacao() ) + "' , "
                + ficha_tecnica.getUsuarioIdActualizacao() + " , "
                + ficha_tecnica.getCustoProduto() + " , "
                + ficha_tecnica.getPercentagemGanho() + " , "
                + ficha_tecnica.getCustoVenda() + " , "
                + ficha_tecnica.getQtdComposto() + " , "
                + "'" + ficha_tecnica.getPrato() + "' , "
                //                + Arrays.toString(ficha_tecnica.getPhoto())
                + ficha_tecnica.getPhoto()
                + " ) ";

        System.out.println( "Modo INSERT" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        FichaTecnica ficha_tecnica = (FichaTecnica) object;

        String sql = "UPDATE ficha_tecnica SET "
                + " produto_id  = '" + ficha_tecnica.getProdutoId()
                + " data_criacao = '" + MetodosUtil.getDataBancoFull( ficha_tecnica.getDataCriacao() ) + "'"
                + " , usuario_id_criacao = " + ficha_tecnica.getUsuarioIdCriacao()
                + " data_actualizacao = '" + MetodosUtil.getDataBancoFull( ficha_tecnica.getDataActualizacao() ) + "'"
                + ", usuario_id_actualizacao = " + ficha_tecnica.getUsuarioIdActualizacao()
                + ", custo_produto = " + ficha_tecnica.getCustoProduto()
                + ", percentagem_ganho = " + ficha_tecnica.getPercentagemGanho()
                + ", custo_venda = " + ficha_tecnica.getCustoVenda()
                + ", qtd_composto = " + ficha_tecnica.getQtdComposto()
                + ", prato = " + ficha_tecnica.getPrato()
                + ", photo = " + ficha_tecnica.getPhoto()
                + " WHERE id = ?";
        System.out.println( sql );
        return conexao.executeUpdate( sql );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM ficha_tecnica WHERE id = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<FichaTecnica> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM ficha_tecnica ORDER BY id ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<FichaTecnica> lista_ficha_tecnica = new ArrayList<>();
        FichaTecnica ficha_tecnica;
        try
        {
//        String INSERT = "INSERT INTO ficha_tecnica( produto_id , data_criacao , usuario_id_criacao , data_actualizacao , "
//                + " usuario_id_actualizacao , custo_produto , percentagem_ganho , custo_venda , photo "
            while ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );

                lista_ficha_tecnica.add( ficha_tecnica );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_ficha_tecnica;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ficha_tecnica WHERE id = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {

            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public Object findByIdProduto( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ficha_tecnica WHERE produto_id = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {
            System.out.println( "CODIGO DO PRODUTO SELECIONADO: " + codigo );
            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public FichaTecnica findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id WHERE p.designacao = '" + designacao + "'";
//        String FIND__BY_CODIGO = "SELECT * FROM tb_produto WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha = null;

        try
        {
            if ( result.next() )
            {
                ficha = new FichaTecnica();
                setResultFicha( result, ficha );
            }
        }
        catch ( SQLException e )
        {
        }
        return ficha;

    }

    public FichaTecnica findByCodigo( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id WHERE f.produto_id = " + codigo;
//        String FIND__BY_CODIGO = "SELECT * FROM tb_produto WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha = null;

        try
        {
            if ( result.next() )
            {
                ficha = new FichaTecnica();
                setResultFicha( result, ficha );
            }
        }
        catch ( SQLException e )
        {
        }
        return ficha;

    }

    public Vector<String> getVectorFichaTecnica()
    {
        String FIND_ALL = "SELECT p.designacao FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id ORDER BY p.designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "p.designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }
//    public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
//    {
//        String FIND_ALL = "SELECT p.designacao FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id WHERE  cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//        Vector<String> lista = new Vector<>();
//        try
//        {
//            while ( result.next() )
//            {
//                lista.add( result.getString( "p.designacao" ) );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return lista;
//
//    }

//    public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
//    {
//        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE  cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//        Vector<String> lista_mesas = new Vector<>();
//        try
//        {
//            while ( result.next() )
//            {
//                lista_mesas.add( result.getString( "designacao" ) );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return lista_mesas;
//
//    }
    public Vector<String> getVectorFichaTecnicaCategoria()
    {
        String FIND_ALL = "SELECT tp.designacao FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto GROUP BY tp.designacao ORDER BY p.designacao ASC";

        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "tp.designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

    public FichaTecnica getTipoFamiliaByDesignacao( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT tp.designacao FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto WHERE tp.designacao ='" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {
            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
    {
        String FIND_ALL = "SELECT p.designacao FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id INNER JOIN tb_tipo_produto tp ON tp.codigo = p.cod_Tipo_Produto WHERE  p.cod_Tipo_Produto = " + idTipoProduto + " ORDER BY p.designacao ASC";

        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "p.designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

//        public Vector<String> getVectorByIdTipoProduto( int idTipoProduto )
//    {
//        String FIND_ALL = "SELECT designacao FROM tb_produto WHERE  cod_Tipo_Produto = " + idTipoProduto + " ORDER BY designacao ASC";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//        Vector<String> lista_mesas = new Vector<>();
//        try
//        {
//            while ( result.next() )
//            {
//                lista_mesas.add( result.getString( "designacao" ) );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return lista_mesas;
//
//    }
    public FichaTecnica getLastFichaTecnica()
    {

        String FIND__BY_CODIGO = "   SELECT * FROM ficha_tecnica WHERE id = ( SELECT MAX(id) as maximo_id   FROM ficha_tecnica f ) ";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {

            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    private FichaTecnica getFichaTecnicaResultSet( FichaTecnica ficha_tecnica, ResultSet result )
    {
        try
        {
            ficha_tecnica.setId( result.getInt( "id" ) );
            ficha_tecnica.setPrato( result.getString( "prato" ) );
            ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
            ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
            ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
            ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
            ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
            ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
            ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
            ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
            ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
            ficha_tecnica.setPhoto( result.getBytes( "photo" ) );

        }
        catch ( Exception e )
        {
        }

        return ficha_tecnica;
    }

    public List<FichaTecnica> listarTodasFichasTecnicasByData1AndData2( Date data_1, Date data_2 )
    {
        String FIND_ALL = "SELECT * FROM ficha_tecnica WHERE DATE(data_actualizacao) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<FichaTecnica> lista_ficha_tecnica = new ArrayList<>();
        FichaTecnica ficha_tecnica;
        try
        {
            while ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica = getFichaTecnicaResultSet( ficha_tecnica, result );
                lista_ficha_tecnica.add( ficha_tecnica );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_ficha_tecnica;
    }

    /**
     *
     * @param cod_fact
     * @decricao busca todos os recibos de uma determnada factura.
     * @return
     */
    public FichaTecnica getFichas( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT f.id FROM ficha_tecnica f WHERE id = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {

            if ( result.next() )
            {

                ficha_tecnica = new FichaTecnica();
                getFichaTecnicaResultSet( ficha_tecnica, result );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public FichaTecnica getFichaByRefeicao( int codRefeicao )
    {
        String FIND_BY_CODIGO = "SELECT * FROM ficha_tecnica f WHERE produto_id = " + codRefeicao;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {
            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                getFichaTecnicaResultSet( ficha_tecnica, result );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public FichaTecnica getLastFicha()
    {
        String LAST_ID = "SELECT * FROM ficha_tecnica WHERE id = (SELECT MAX(id) as codigo FROM ficha_tecnica)";
        ResultSet result = conexao.executeQuery( LAST_ID );
        FichaTecnica ficha_tecnica = null;

        try
        {
            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );
            }
        }
        catch ( SQLException e )
        {
        }

        return ficha_tecnica;

    }

    public boolean existeFichaTecnicaRefeicao( int codigo )
    {
        String sql = "SELECT *  FROM ficha_tecnica WHERE produto_id = " + codigo;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        return false;

    }

    private void setResultFicha( ResultSet result, FichaTecnica ficha_tecnica )
    {
        try
        {

//                ficha_tecnica = new FichaTecnica();
            ficha_tecnica.setId( result.getInt( "id" ) );
            ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
            ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
            ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
            ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
            ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
            ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
            ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
            ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
            ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
            ficha_tecnica.setPhoto( result.getBytes( "photo" ) );

        }
        catch ( SQLException e )
        {
        }

    }

    public FichaTecnica getStockFichaTecnicaByIdProduto( int idProduto )
    {

        String FIND__BY_CODIGO = "SELECT * FROM ficha_tecnica f INNER JOIN tb_produto p ON p.codigo = f.produto_id WHERE f.produto_id = " + idProduto;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FichaTecnica ficha_tecnica = null;
        try
        {

            if ( result.next() )
            {
                ficha_tecnica = new FichaTecnica();
                ficha_tecnica.setId( result.getInt( "id" ) );
                ficha_tecnica.setProdutoId( result.getInt( "produto_id" ) );
                ficha_tecnica.setDataCriacao( result.getDate( "data_criacao" ) );
                ficha_tecnica.setUsuarioIdCriacao( result.getInt( "usuario_id_criacao" ) );
                ficha_tecnica.setDataActualizacao( result.getDate( "data_actualizacao" ) );
                ficha_tecnica.setUsuarioIdActualizacao( result.getInt( "usuario_id_actualizacao" ) );
                ficha_tecnica.setCustoProduto( result.getBigDecimal( "custo_produto" ) );
                ficha_tecnica.setPercentagemGanho( result.getBigDecimal( "percentagem_ganho" ) );
                ficha_tecnica.setCustoVenda( result.getBigDecimal( "custo_venda" ) );
                ficha_tecnica.setQtdComposto( result.getDouble( "qtd_composto" ) );
                ficha_tecnica.setPhoto( result.getBytes( "photo" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return ficha_tecnica;

    }

    public static void main( String[] args )
    {
        BDConexao conexao = BDConexao.getInstancia();
        FichaTecnicaController f = new FichaTecnicaController( conexao );

    }

}
