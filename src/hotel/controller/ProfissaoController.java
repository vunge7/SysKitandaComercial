/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.controller;

import entity.TbProfissao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProfissaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ProfissaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbProfissao profissao = (TbProfissao) object;
        String INSERT = "INSERT INTO tb_profissao( descricao ) VALUES("
                + "'" + profissao.getDescricao() + "')";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbProfissao profissao = (TbProfissao) object;
        String UPDATE = "UPDATE tb_profissao SET "
                + " descricao = '" + profissao.getDescricao() + "' "
                + " WHERE idProfissao = " + profissao.getIdProfissao();

        return conexao.executeUpdate( UPDATE );

    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM profissao WHERE pk_profissao = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbProfissao> listarTodos()
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM tb_profissao ORDER BY descricao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbProfissao> lista_profissao = new ArrayList<>();
        TbProfissao profissao;
        try
        {
            while ( result.next() )
            {
                profissao = new TbProfissao();
                profissao.setIdProfissao( result.getInt( "idProfissao" ) );
//                profissao.setDescricao( result.getString( "descricao" ) );
                lista_profissao.add( profissao );

            }

        } catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_profissao;
    }

    @Override
    public Vector<String> getVector()
    {
        List<TbProfissao> listarTodos = listarTodos();
        Vector<String> vector = new Vector<>();

        vector.add( 0, "--Seleccione--" );
        if ( !Objects.isNull( listarTodos ) )
        {
            for ( int i = 0; i < listarTodos.size(); i++ )
            {
                TbProfissao get = listarTodos.get( i );
//                vector.addElement( get.getDescricao() );

            }
        }
        return vector;

    }

    @Override
    public Object findById( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT * FROM tb_profissao WHERE idProfissao = " +codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbProfissao profissao = null;
        try
        {
            if ( result.next() )
            {
                profissao = new TbProfissao();
                profissao.setIdProfissao( result.getInt( "idProfissao" ) );
//                profissao.setDescricao( result.getString( "descricao" ) );
            }

        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return profissao;
    }
    
    public Object findByDesignacao( String designacao )
    {
        String FIND_BY_CODIGO = "SELECT * FROM tb_profissao WHERE descricao  = '" +designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbProfissao profissao = null;
        try
        {
            if ( result.next() )
            {
                profissao = new TbProfissao();
                profissao.setIdProfissao( result.getInt( "idProfissao" ) );
//                profissao.setDescricao( result.getString( "descricao" ) );
            }

        } catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return profissao;
    }

}
