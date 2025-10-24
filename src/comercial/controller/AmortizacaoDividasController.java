/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AmortizacaoDivida;
import entity.Grupo;
import entity.TbUsuario;
import entity.TbVenda;
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
public class AmortizacaoDividasController implements EntidadeFactory
{

    private BDConexao conexao;

    public AmortizacaoDividasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        return false;

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_amortizacao_divida )
    {
        String DELETE = "DELETE FROM amortizacao_divida WHERE pk_amortizacao_divida = " + pk_amortizacao_divida;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<AmortizacaoDivida> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM amortizacao_divida ORDER BY pk_amortizacao_divida ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<AmortizacaoDivida> lista_amortizacao_divida = new ArrayList<>();
        AmortizacaoDivida amortizacao_divida;
        try
        {
            while ( result.next() )
            {
                amortizacao_divida = new AmortizacaoDivida();
                setResultSetAmortizacaoDivida( result, amortizacao_divida );
                lista_amortizacao_divida.add( amortizacao_divida );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_amortizacao_divida;
    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT * FROM amortizacao_divida ORDER BY pk_amortizacao_divida ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_amortizacao_divida = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista_amortizacao_divida.add( result.getString( "descricao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_amortizacao_divida;

    }

    @Override
    public Object findById( int pk_amortizacao_divida )
    {

        String FIND_BY_CODIGO = "SELECT * FROM amortizacao_divida WHERE pk_amortizacao_divida = " + pk_amortizacao_divida;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        AmortizacaoDivida amortizacao_divida = null;
        try
        {

            if ( result.next() )
            {
                amortizacao_divida = new AmortizacaoDivida();
                setResultSetAmortizacaoDivida( result, amortizacao_divida );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return amortizacao_divida;

    }

    public List<AmortizacaoDivida> findAllByCodRef( String refCodFact )
    {
        String query = "SELECT * FROM amortizacao_divida WHERE ref_cod_fact = '" + refCodFact + "'";
        ResultSet result = conexao.executeQuery( query );
        AmortizacaoDivida amortizacao_divida;
        List<AmortizacaoDivida> lista = new ArrayList<>();
        try
        {
            if ( result.next() )
            {
                amortizacao_divida = new AmortizacaoDivida();
                setResultSetAmortizacaoDivida( result, amortizacao_divida );
                lista.add( amortizacao_divida );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

    private void setResultSetAmortizacaoDivida( ResultSet rs, AmortizacaoDivida amortizacao_divida )
    {
        try
        {
            amortizacao_divida.setPkAmortizacaoDivida( rs.getInt( "pk_amortizacao_divida" ) );
            amortizacao_divida.setData( rs.getDate( "data" ) );
            amortizacao_divida.setValorPendente( rs.getDouble( "valor_pendente" ) );
            amortizacao_divida.setValorEntregue( rs.getDouble( "valor_entregue" ) );
            amortizacao_divida.setTroco( rs.getDouble( "troco" ) );
            amortizacao_divida.setObs( rs.getString( "obs" ) );
            amortizacao_divida.setDesconto( rs.getDouble( "desconto" ) );
            amortizacao_divida.setFkVenda( new TbVenda( rs.getInt( "fk_venda" ) ) );
            amortizacao_divida.setFkUsuario( new TbUsuario( rs.getInt( "fk_usuario" ) ) );
            amortizacao_divida.setRefCodFact( rs.getString( "ref_cod_fact" ) );
            amortizacao_divida.setTotalVendaFact( rs.getDouble( "total_venda_fact" ) );
            amortizacao_divida.setValorPago( rs.getBigDecimal( "valor_pago" ) );
            amortizacao_divida.setNetTotal( rs.getBigDecimal( "net_total" ) );
            amortizacao_divida.setTax( rs.getBigDecimal( "tax" ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
