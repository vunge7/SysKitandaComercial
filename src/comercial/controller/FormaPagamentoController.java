/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.FormaPagamento;
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
public class FormaPagamentoController implements EntidadeFactory
{

    private BDConexao conexao;

    public FormaPagamentoController( BDConexao conexao )
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
        return false;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        return false;
    }

    @Override
    public List<FormaPagamento> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM forma_pagamento ORDER BY pk_forma_pagamento ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<FormaPagamento> lista_transferencia = new ArrayList<>();
        FormaPagamento formaPagamento;
        try
        {
            while ( result.next() )
            {
                formaPagamento = new FormaPagamento();
                formaPagamento.setPkFormaPagamento( result.getInt( "pk_forma_pagamento" ) );
                formaPagamento.setDesignacao( result.getString( "designacao" ) );
                lista_transferencia.add( formaPagamento );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_transferencia;
    }

    public List<FormaPagamento> listarTodosExeceptoOrdemSacEGorjet()
    {

        String FIND_ALL = "SELECT * FROM forma_pagamento WHERE pk_forma_pagamento IN(1,2,4)";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<FormaPagamento> lista_transferencia = new ArrayList<>();
        FormaPagamento formaPagamento;
        try
        {
            while ( result.next() )
            {
                formaPagamento = new FormaPagamento();
                formaPagamento.setPkFormaPagamento( result.getInt( "pk_forma_pagamento" ) );
                formaPagamento.setDesignacao( result.getString( "designacao" ) );
                lista_transferencia.add( formaPagamento );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_transferencia;
    }

    @Override
    public Vector<String> getVector()
    {
        {
            String FIND_ALL = "SELECT designacao FROM forma_pagamento ORDER BY pk_forma_pagamento ASC";
            ResultSet result = conexao.executeQuery( FIND_ALL );
            Vector<String> lista_transferencia = new Vector<>();
            try
            {
                while ( result.next() )
                {
                    lista_transferencia.add( result.getString( "designacao" ) );
                }
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            return lista_transferencia;
        }
    }

    public FormaPagamento findByDescrisao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM forma_pagamento WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FormaPagamento formaPagamento = null;
        try
        {

            if ( result.next() )
            {
                formaPagamento = new FormaPagamento();
                formaPagamento.setPkFormaPagamento( result.getInt( "pk_forma_pagamento" ) );
                formaPagamento.setDesignacao( result.getString( "designacao" ) );
                formaPagamento.setFkContaAssociada( result.getInt( "fk_conta_associada" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return formaPagamento;

    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM forma_pagamento WHERE pk_forma_pagamento = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        FormaPagamento formaPagamento = null;
        try
        {

            if ( result.next() )
            {
                formaPagamento = new FormaPagamento();
                formaPagamento.setPkFormaPagamento( result.getInt( "pk_forma_pagamento" ) );
                formaPagamento.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return formaPagamento;

    }

    public FormaPagamento findByCodigo( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT * FROM forma_pagamento f WHERE pk_forma_pagamento = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        FormaPagamento formaPagamento = null;
        try
        {

            if ( result.next() )
            {
                formaPagamento = new FormaPagamento();
                formaPagamento.setPkFormaPagamento( result.getInt( "pk_forma_pagamento" ) );
                formaPagamento.setDesignacao( result.getString( "designacao" ) );
                formaPagamento.setFkContaAssociada( result.getInt( "fk_conta_associada" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return formaPagamento;

    }

    public boolean adicionarContaAssociada( FormaPagamento f )
    {
        String sql = "UPDATE forma_pagamento SET fk_conta_associada = " + f.getFkContaAssociada()
                + " WHERE pk_forma_pagamento = " + f.getPkFormaPagamento();

        return conexao.executeUpdate( sql );

    }

    public boolean existeContaAssociada( FormaPagamento f )
    {
        String sql = "SELECT * FROM forma_pagamento  "
                + " WHERE fk_conta_associada = " + f.getFkContaAssociada() + " AND fk_conta_associada <> 0";

        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
        }
        return false;

    }

}
