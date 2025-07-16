/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.LinhaFichaTecnica;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Martinho Luis
 */
public class LinhaFichaTecnicaController implements EntidadeFactory
{

    private BDConexao conexao;

    public LinhaFichaTecnicaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        LinhaFichaTecnica linha_ficha_tecnica = (LinhaFichaTecnica) object;
        String INSERT = "INSERT INTO linha_ficha_tecnica( igrendiente_id , igrendiente_designacao , unidade , "
                + " preco_unitario , qtd_bruto , qtd_liquido , factor_correcao  , custo_total , unidade_compra , ficha_tecnica_id "
                + ")"
                + " VALUES("
                + linha_ficha_tecnica.getIgrendienteId() + " , "
                + "'" + linha_ficha_tecnica.getIgrendienteDesignacao() + "' , "
                + "'" + linha_ficha_tecnica.getUnidade() + "' , "
                + linha_ficha_tecnica.getPrecoUnitario() + " , "
                + linha_ficha_tecnica.getQtdBruto() + " , "
                + linha_ficha_tecnica.getQtdLiquido() + " , "
                + linha_ficha_tecnica.getFactorCorrecao() + " , "
                + linha_ficha_tecnica.getCustoTotal() + " , "
                + linha_ficha_tecnica.getUnidade_compra() + " , "
                + linha_ficha_tecnica.getFichaTecnicaId()
                + " ) ";

        System.err.println( INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        LinhaFichaTecnica linha_ficha_tecnica = (LinhaFichaTecnica) object;
        String query = "UPDATE linha_ficha_tecnica SET qtd_liquido =  " + linha_ficha_tecnica.getQtdLiquido() + ", qtd_bruto = " + linha_ficha_tecnica.getQtdBruto()
                + " WHERE id = " + linha_ficha_tecnica.getId();
        return conexao.executeUpdate( query );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM linha_ficha_tecnica WHERE id = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<LinhaFichaTecnica> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM linha_ficha_tecnica ORDER BY id ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<LinhaFichaTecnica> lista_item = new ArrayList<>();
        LinhaFichaTecnica linha_ficha_tecnica;
        try
        {

            while ( result.next() )
            {
                linha_ficha_tecnica = new LinhaFichaTecnica();
                linha_ficha_tecnica.setId( result.getInt( "id" ) );
                linha_ficha_tecnica.setIgrendienteId( result.getInt( "igrendiente_id" ) );
                linha_ficha_tecnica.setIgrendienteDesignacao( result.getString( "igrendiente_designacao" ) );
                linha_ficha_tecnica.setUnidade( result.getString( "unidade" ) );
                linha_ficha_tecnica.setPrecoUnitario( result.getBigDecimal( "preco_unitario" ) );
                linha_ficha_tecnica.setQtdBruto( result.getDouble( "qtd_bruto" ) );
                linha_ficha_tecnica.setQtdLiquido( result.getDouble( "qtd_liquido" ) );

                linha_ficha_tecnica.setFactorCorrecao( result.getDouble( "factor_correcao" ) );
                linha_ficha_tecnica.setCustoTotal( result.getBigDecimal( "custo_total" ) );
                linha_ficha_tecnica.setUnidade_compra( result.getDouble( "unidade_compra" ) );
                linha_ficha_tecnica.setFichaTecnicaId( result.getInt( "ficha_tecnica_id" ) );

                lista_item.add( linha_ficha_tecnica );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item;
    }

    public boolean existeLinhaFichaTecnicaByCodFichaTecnica( int codigo )
    {

        String FIND_ALL = "SELECT * FROM linha_ficha_tecnica where ficha_tecnica_id = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<LinhaFichaTecnica> lista_item = new ArrayList<>();
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

        String FIND__BY_CODIGO = "SELECT * FROM linha_ficha_tecnica WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        LinhaFichaTecnica linha_ficha_tecnica = null;
        try
        {

            if ( result.next() )
            {
                linha_ficha_tecnica = new LinhaFichaTecnica();
                linha_ficha_tecnica.setId( result.getInt( "id" ) );
                linha_ficha_tecnica.setIgrendienteId( result.getInt( "igrendiente_id" ) );
                linha_ficha_tecnica.setIgrendienteDesignacao( result.getString( "igrendiente_designacao" ) );
                linha_ficha_tecnica.setUnidade( result.getString( "unidade" ) );
                linha_ficha_tecnica.setPrecoUnitario( result.getBigDecimal( "preco_unitario" ) );
                linha_ficha_tecnica.setQtdBruto( result.getDouble( "qtd_bruto" ) );
                linha_ficha_tecnica.setQtdLiquido( result.getDouble( "qtd_liquido" ) );

                linha_ficha_tecnica.setFactorCorrecao( result.getDouble( "factor_correcao" ) );
                linha_ficha_tecnica.setCustoTotal( result.getBigDecimal( "custo_total" ) );
                linha_ficha_tecnica.setUnidade_compra( result.getDouble( "unidade_compra" ) );
                linha_ficha_tecnica.setFichaTecnicaId( result.getInt( "ficha_tecnica_id" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linha_ficha_tecnica;

    }

    public LinhaFichaTecnica getLastLinhaFichaTecnica()
    {

        String FIND__BY_CODIGO = "SELECT MAX(id) as maximo_id, i.*  FROM linha_ficha_tecnica l";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        LinhaFichaTecnica linha_ficha_tecnica = null;
        try
        {
            if ( result.next() )
            {
                linha_ficha_tecnica = new LinhaFichaTecnica();
                linha_ficha_tecnica.setId( result.getInt( "id" ) );
                linha_ficha_tecnica.setIgrendienteId( result.getInt( "igrendiente_id" ) );
                linha_ficha_tecnica.setIgrendienteDesignacao( result.getString( "igrendiente_designacao" ) );
                linha_ficha_tecnica.setUnidade( result.getString( "unidade" ) );
                linha_ficha_tecnica.setPrecoUnitario( result.getBigDecimal( "preco_unitario" ) );
                linha_ficha_tecnica.setQtdBruto( result.getDouble( "qtd_bruto" ) );
                linha_ficha_tecnica.setQtdLiquido( result.getDouble( "qtd_liquido" ) );

                linha_ficha_tecnica.setFactorCorrecao( result.getDouble( "factor_correcao" ) );
                linha_ficha_tecnica.setCustoTotal( result.getBigDecimal( "custo_total" ) );
                linha_ficha_tecnica.setUnidade_compra( result.getDouble( "unidade_compra" ) );
                linha_ficha_tecnica.setFichaTecnicaId( result.getInt( "ficha_tecnica_id" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return linha_ficha_tecnica;

    }

    public List<LinhaFichaTecnica> listarTodosByCodigoFichaTecnica( int id )
    {
        String FIND_ALL = "SELECT l.* FROM linha_ficha_tecnica l "
                + " WHERE l.ficha_tecnica_id =" + id;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<LinhaFichaTecnica> lista_linha_ficha = new ArrayList<>();
        LinhaFichaTecnica linha_ficha_tecnica;
        try
        {
            while ( result.next() )
            {
                linha_ficha_tecnica = new LinhaFichaTecnica();
                linha_ficha_tecnica.setId( result.getInt( "id" ) );
                linha_ficha_tecnica.setIgrendienteId( result.getInt( "igrendiente_id" ) );
                linha_ficha_tecnica.setIgrendienteDesignacao( result.getString( "igrendiente_designacao" ) );
                linha_ficha_tecnica.setUnidade( result.getString( "unidade" ) );
                linha_ficha_tecnica.setPrecoUnitario( result.getBigDecimal( "preco_unitario" ) );
                linha_ficha_tecnica.setQtdBruto( result.getDouble( "qtd_bruto" ) );
                linha_ficha_tecnica.setQtdLiquido( result.getDouble( "qtd_liquido" ) );
                linha_ficha_tecnica.setFactorCorrecao( result.getDouble( "factor_correcao" ) );
                linha_ficha_tecnica.setCustoTotal( result.getBigDecimal( "custo_total" ) );
                linha_ficha_tecnica.setUnidade_compra( result.getDouble( "unidade_compra" ) );
                linha_ficha_tecnica.setFichaTecnicaId( result.getInt( "ficha_tecnica_id" ) );
                lista_linha_ficha.add( linha_ficha_tecnica );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_linha_ficha;
    }

    public boolean eliminarAllItensByIdFicha( int id )
    {
        String DELETE = "DELETE FROM linha_ficha_tecnica WHERE ficha_tecnica_id = " + id;
        return conexao.executeUpdate( DELETE );
    }

}
