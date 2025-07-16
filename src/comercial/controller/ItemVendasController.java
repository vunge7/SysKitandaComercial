/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import entity.TbUsuario;
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
 * @author Martinho Luis
 */
public class ItemVendasController implements EntidadeFactory
{

    private BDConexao conexao;

    public ItemVendasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbItemVenda itemVenda = ( TbItemVenda ) object;
        String INSERT = "INSERT INTO tb_item_venda( quantidade , valor_iva , motivo_isensao , desconto , total , codigo_venda , "
                + " codigo_produto , fk_preco , codigo_isensao , fk_lugares  , fk_mesas , data_servico , valor_retencao "
                + ")"
                + " VALUES("
                + itemVenda.getQuantidade() + " , "
                + itemVenda.getValorIva() + " , "
                + "'" + itemVenda.getMotivoIsensao() + "' , "
                + itemVenda.getDesconto() + " , "
                + itemVenda.getTotal() + " , "
                + "'" + itemVenda.getCodigoVenda().getCodigo() + "' , "
                + "'" + itemVenda.getCodigoProduto().getCodigo() + "' , "
                + "'" + itemVenda.getFkPreco().getPkPreco() + "' , "
                + "'" + itemVenda.getCodigoIsensao() + "' , "
                + "'" + itemVenda.getFkLugares().getPkLugares() + "' , "
                + "'" + itemVenda.getFkMesas().getPkMesas() + "' , "
                + "'" + MetodosUtil.getDataBanco( itemVenda.getDataServico() ) + "' , "
                + itemVenda.getValorRetencao()
                + " ) ";

        System.err.println( INSERT );
        return conexao.executeUpdate( INSERT );

    }

    public boolean salvarLavandaria( Object object )
    {
        TbItemVenda itemVenda = ( TbItemVenda ) object;
        String INSERT = "INSERT INTO tb_item_venda( quantidade , valor_iva , motivo_isensao , desconto , total , codigo_venda , "
                + " codigo_produto , fk_preco , codigo_isensao , fk_lugares  , fk_mesas , data_servico , valor_retencao, obs, data_entrega, posicao "
                + ")"
                + " VALUES("
                + itemVenda.getQuantidade() + " , "
                + itemVenda.getValorIva() + " , "
                + "'" + itemVenda.getMotivoIsensao() + "' , "
                + itemVenda.getDesconto() + " , "
                + itemVenda.getTotal() + " , "
                + "'" + itemVenda.getCodigoVenda().getCodigo() + "' , "
                + "'" + itemVenda.getCodigoProduto().getCodigo() + "' , "
                + "'" + itemVenda.getFkPreco().getPkPreco() + "' , "
                + "'" + itemVenda.getCodigoIsensao() + "' , "
                + "'" + itemVenda.getFkLugares().getPkLugares() + "' , "
                + "'" + itemVenda.getFkMesas().getPkMesas() + "' , "
                + "'" + MetodosUtil.getDataBanco( itemVenda.getDataServico() ) + "' , "
                + itemVenda.getValorRetencao() + ", "
                + "'" + itemVenda.getObs() + "' , "
                + "'" + MetodosUtil.getDataBanco( itemVenda.getDataEntrega() ) + "',   "
                + itemVenda.getPosicao()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbItemVenda itemVenda = ( TbItemVenda ) object;
        String query = "UPDATE tb_item_venda SET quantidade =  " + itemVenda.getQuantidade() + ", total = " + itemVenda.getTotal()
                + " WHERE codigo = " + itemVenda.getCodigo();
        return conexao.executeUpdate( query );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_item_venda WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbItemVenda> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_item_venda ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        TbItemVenda item_venda;
        try
        {

            while ( result.next() )
            {
                item_venda = new TbItemVenda();
                item_venda.setCodigo( result.getInt( "codigo" ) );
                item_venda.setQuantidade( result.getInt( "quantidade" ) );
                item_venda.setValorIva( result.getDouble( "total_venda" ) );
                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_venda.setDesconto( result.getDouble( "desconto" ) );
                item_venda.setTotal( result.getBigDecimal( "total" ) );
                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
                item_venda.setDataServico( result.getDate( "data_servico" ) );
                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );

                lista_item_venda.add( item_venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_venda;
    }

    public boolean existeItemByCodVenda( int codigo )
    {

        String FIND_ALL = "SELECT * FROM tb_item_venda where codigo_venda = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        try
        {
            if ( result.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_item_venda WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbItemVenda item_venda = null;
        try
        {

            if ( result.next() )
            {
                item_venda = new TbItemVenda();
                item_venda.setCodigo( result.getInt( "codigo" ) );
                item_venda.setQuantidade( result.getInt( "quantidade" ) );
                item_venda.setValorIva( result.getDouble( "total_venda" ) );
                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_venda.setDesconto( result.getDouble( "desconto" ) );
                item_venda.setTotal( result.getBigDecimal( "total" ) );
                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
                item_venda.setDataServico( result.getDate( "data_servico" ) );
                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_venda;

    }

    public TbItemVenda getLastVenda()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, i.*  FROM tb_item_venda i";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbItemVenda item_venda = null;
        try
        {
            if ( result.next() )
            {
                item_venda = new TbItemVenda();
                item_venda.setCodigo( result.getInt( "maximo_id" ) );
                item_venda.setQuantidade( result.getInt( "quantidade" ) );
                item_venda.setValorIva( result.getDouble( "total_venda" ) );
                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_venda.setDesconto( result.getDouble( "desconto" ) );
                item_venda.setTotal( result.getBigDecimal( "total" ) );
                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
                item_venda.setDataServico( result.getDate( "data_servico" ) );
                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_venda;

    }

    public List<TbItemVenda> listarTodosByCodFact( String codFact, boolean status )
    {

        String FIND_ALL = "SELECT i.* FROM tb_item_venda i "
                + " INNER JOIN tb_venda v ON v.codigo = i.codigo_venda"
                + " WHERE v.cod_fact = '" + codFact + "' AND i.status_entrega = " + status;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        TbItemVenda item_venda;
        try
        {

            while ( result.next() )
            {
                item_venda = new TbItemVenda();
                item_venda.setCodigo( result.getInt( "codigo" ) );
                item_venda.setQuantidade( result.getInt( "quantidade" ) );
                item_venda.setValorIva( result.getDouble( "valor_iva" ) );
                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_venda.setDesconto( result.getDouble( "desconto" ) );
                item_venda.setTotal( result.getBigDecimal( "total" ) );
                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
                item_venda.setDataServico( result.getDate( "data_servico" ) );
                item_venda.setDataEntrega( result.getDate( "data_entrega" ) );
                item_venda.setObs( result.getString( "obs" ) );
                item_venda.setStatusEntrega( result.getBoolean( "status_entrega" ) );
                item_venda.setPosicao( result.getInt( "posicao" ) );
                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );
                lista_item_venda.add( item_venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_venda;
    }

    public List<TbItemVenda> listarTodosByCodFactByData( Date data_inicio, Date data_fim, boolean status, int pk_documento )
    {
        String FIND_ALL = "SELECT v.cod_fact FROM tb_item_venda i "
                + " INNER JOIN tb_venda v ON v.codigo = i.codigo_venda"
                + " WHERE DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND i.status_entrega = " + status + " AND v.fk_documento = " + pk_documento;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        TbItemVenda item_venda;
        try
        {

            while ( result.next() )
            {
                item_venda = new TbItemVenda();
//                item_venda.setCodigo( result.getInt( "codigo" ) );
//                item_venda.setQuantidade( result.getInt( "quantidade" ) );
//                item_venda.setValorIva( result.getDouble( "valor_iva" ) );
//                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) ); 
//                item_venda.setDesconto( result.getDouble( "desconto" ) );
//                item_venda.setTotal( result.getBigDecimal( "total" ) );
//                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
//                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
//                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
//                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
//                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
//                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
//                item_venda.setDataServico( result.getDate( "data_servico" ) );
//                item_venda.setDataEntrega( result.getDate( "data_entrega" ) );
//                item_venda.setObs( result.getString( "obs" ) );
//                item_venda.setStatusEntrega( result.getBoolean( "status_entrega" ) );
//                item_venda.setPosicao( result.getInt( "posicao" ) );
//                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );
                lista_item_venda.add( item_venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_venda;
    }

    public List<TbItemVenda> listarTodosByCodFactByData1( Date data_inicio, Date data_fim, boolean status, int pk_documento )
    {
        String FIND_ALL = " SELECT distinct v.cod_fact FROM tb_item_venda i INNER JOIN tb_venda v ON v.codigo = i.codigo_venda "
                + " WHERE DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND i.status_entrega = " + status + " AND v.fk_documento = " + pk_documento;
//        String FIND_ALL = "SELECT v.cod_fact FROM tb_item_venda i "
//                + " INNER JOIN tb_venda v ON v.codigo = i.codigo_venda"
//                + " WHERE DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND i.status_entrega = " + status + " AND v.fk_documento = " + pk_documento;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        TbItemVenda item_venda;
        try
        {

            while ( result.next() )
            {
                item_venda = new TbItemVenda();
//                item_venda.setCodigo( result.getInt( "codigo" ) );
//                item_venda.setQuantidade( result.getInt( "quantidade" ) );
//                item_venda.setValorIva( result.getDouble( "valor_iva" ) );
//                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) ); 
//                item_venda.setDesconto( result.getDouble( "desconto" ) );
//                item_venda.setTotal( result.getBigDecimal( "total" ) );
//                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
//                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
//                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
//                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
//                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
//                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
//                item_venda.setDataServico( result.getDate( "data_servico" ) );
//                item_venda.setDataEntrega( result.getDate( "data_entrega" ) );
//                item_venda.setObs( result.getString( "obs" ) );
//                item_venda.setStatusEntrega( result.getBoolean( "status_entrega" ) );
//                item_venda.setPosicao( result.getInt( "posicao" ) );
//                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );
                lista_item_venda.add( item_venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_venda;
    }

    public List<TbItemVenda> listarTodosByCodigoVenda( int codigoVenda )
    {

        String FIND_ALL = "SELECT i.* FROM tb_item_venda i "
                + " INNER JOIN tb_venda v ON v.codigo = i.codigo_venda"
                + " WHERE v.codigo =" + codigoVenda;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbItemVenda> lista_item_venda = new ArrayList<>();
        TbItemVenda item_venda;
        try
        {

            while ( result.next() )
            {
                item_venda = new TbItemVenda();
                item_venda.setCodigo( result.getInt( "codigo" ) );
                item_venda.setQuantidade( result.getInt( "quantidade" ) );
                item_venda.setValorIva( result.getDouble( "valor_iva" ) );
                item_venda.setMotivoIsensao( result.getString( "motivo_isensao" ) );
                item_venda.setDesconto( result.getDouble( "desconto" ) );
                item_venda.setTotal( result.getBigDecimal( "total" ) );
                item_venda.setCodigoVenda( new TbVenda( result.getInt( "codigo_venda" ) ) );
                item_venda.setCodigoProduto( new TbProduto( result.getInt( "codigo_produto" ) ) );
                item_venda.setFkPreco( new TbPreco( result.getInt( "fk_preco" ) ) );
                item_venda.setCodigoIsensao( result.getString( "codigo_isensao" ) );
                item_venda.setFkLugares( new TbLugares( result.getInt( "fk_lugares" ) ) );
                item_venda.setFkMesas( new TbMesas( result.getInt( "fk_mesas" ) ) );
                item_venda.setDataServico( result.getDate( "data_servico" ) );
                item_venda.setDataEntrega( result.getDate( "data_entrega" ) );
                item_venda.setObs( result.getString( "obs" ) );
                item_venda.setStatusEntrega( result.getBoolean( "status_entrega" ) );
                item_venda.setPosicao( result.getInt( "posicao" ) );
                item_venda.setValorRetencao( result.getDouble( "valor_retencao" ) );
                lista_item_venda.add( item_venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_venda;
    }

    public boolean actualizarEntrega( int cod )
    {
        String query = "UPDATE tb_item_venda SET status_entrega =  true "
                + " WHERE codigo = " + cod;
        return conexao.executeUpdate( query );
    }

}
