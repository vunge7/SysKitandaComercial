/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.Regime;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbFornecedor;
import entity.TbFornecedor;
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
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class FornecedoresController implements EntidadeFactory
{

    private BDConexao conexao;

    public FornecedoresController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbFornecedor fornecedores = ( TbFornecedor ) object;
        String INSERT = "INSERT INTO tb_fornecedor( nome , email , telefone , site , endereco , nif , fk_regime"
                + ")"
                + " VALUES("
                + "'" + fornecedores.getNome() + "' , "
                + "'" + fornecedores.getEmail() + "' , "
                + "'" + fornecedores.getTelefone() + "' , "
                + "'" + fornecedores.getSite() + "' , "
                + "'" + fornecedores.getEndereco() + "' , "
                + "'" + fornecedores.getNif() + "' , "
                + fornecedores.getFkRegime().getPkRegime()
                + " ) ";

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
        String DELETE = "DELETE FROM tb_fornecedor WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbFornecedor> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_fornecedor ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbFornecedor> lista_fornecedores = new ArrayList<>();
        TbFornecedor fornecedores;
        try
        {

            while ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setCodigo( result.getInt( "codigo" ) );
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );
                lista_fornecedores.add( fornecedores );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_fornecedores;
    }

    public Vector<String> getVector1()
    {
        String FIND_ALL = "SELECT nome FROM tb_fornecedor WHERE codigo = 1 ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_fornecedores = new Vector<>();
        try
        {

            while ( result.next() )
            {

                lista_fornecedores.add( result.getString( "nome" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_fornecedores;
    }

    public TbFornecedor findByCod( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_fornecedor WHERE codigo = " + codigo + "";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbFornecedor fornecedor = null;

        try
        {
            if ( result.next() )
            {
                fornecedor = new TbFornecedor();
                setResultFornecedor( result, fornecedor );
            }
        }
        catch ( SQLException e )
        {
        }
        return fornecedor;

    }

    private void setResultFornecedor( ResultSet result, TbFornecedor fornecedores )
    {
        try
        {

            fornecedores.setCodigo( result.getInt( "codigo" ) );
            fornecedores.setNome( result.getString( "nome" ) );
            fornecedores.setEmail( result.getString( "email" ) );
            fornecedores.setTelefone( result.getString( "telefone" ) );
            fornecedores.setSite( result.getString( "site" ) );
            fornecedores.setEndereco( result.getString( "endereco" ) );
            fornecedores.setNif( result.getString( "nif" ) );
            fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );

        }
        catch ( SQLException e )
        {
        }

    }

    public List<TbFornecedor> listarTodos( String nome )
    {

        String FIND_ALL = "SELECT * FROM tb_fornecedor ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbFornecedor> lista_fornecedores = new ArrayList<>();
        TbFornecedor fornecedores;
        try
        {

            while ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );
                lista_fornecedores.add( fornecedores );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_fornecedores;
    }

    public boolean existe_fornecedor( String nome )
    {
        String sql = "SELECT f.codigo FROM tb_fornecedor f WHERE f.nome = '" + nome + "'";
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }

        return false;
    }

//    public Vector<String> getVector( String nome )
//    {
//        List<TbFornecedor> listarTodos = listarTodos( nome );
//        Vector<String> vector = new Vector<>();
//        vector.add( 0, "--Seleccione--" );
//        if ( !Objects.isNull( listarTodos ) )
//        {
//
//            for ( int i = 0; i < listarTodos.size(); i++ )
//            {
//                TbFornecedor get = listarTodos.get( i );
//                vector.addElement( get.getNome() );
//
//            }
//        }
//        return vector;
//    }
    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT nome FROM tb_fornecedor ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_fornecedores = new Vector<>();
        try
        {

            while ( result.next() )
            {

                lista_fornecedores.add( result.getString( "nome" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_fornecedores;
    }

    @Override
//    public Object findById( int codigo )
//    {
//
//        String FIND_BY_CODIGO = "SELECT * FROM tb_fornecedor WHERE codigo = " + codigo;
//        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
//        TbFornecedor fornecedores = null;
//        try
//        {
//
//            if ( result.next() )
//            {
//                fornecedores = new TbFornecedor();
//                fornecedores.setNome( result.getString( "nome" ) );
//                fornecedores.setEmail( result.getString( "email" ) );
//                fornecedores.setTelefone( result.getString( "telefone" ) );
//                fornecedores.setSite( result.getString( "site" ) );
//                fornecedores.setEndereco( result.getString( "endereco" ) );
//                fornecedores.setNif( result.getString( "nif" ) );
//                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );
//
//            }
//
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//        return fornecedores;
//
//    }

    public Object findById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_fornecedor WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbFornecedor fornecedores = null;
        try
        {

            if ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setCodigo( result.getInt( "codigo" ) );
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return fornecedores;

    }

    public Object findByName( String nome )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_fornecedor WHERE nome = '" + nome + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbFornecedor fornecedores = null;
        try
        {

            if ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setCodigo( result.getInt( "codigo" ) );
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return fornecedores;

    }

    public List<TbFornecedor> IniciaisFornecedores( String prefixo )
    {

        String FIND_ALL = "SELECT nome FROM tb_fornecedor WHERE  nome LIKE '%" + prefixo + "%'  ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbFornecedor> lista_fornecedores = new ArrayList<>();
        TbFornecedor fornecedores;
        try
        {

            while ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );
                lista_fornecedores.add( fornecedores );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_fornecedores;
    }

    public TbFornecedor getLastFornecedor()
    {

        String FIND_BY_CODIGO = "SELECT MAX(codigo) as maximo_id, f.*  FROM tb_fornecedor f";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbFornecedor fornecedores = null;
        try
        {

            if ( result.next() )
            {
                fornecedores = new TbFornecedor();
                fornecedores.setCodigo( result.getInt( "maximo_id" ) );
                fornecedores.setNome( result.getString( "nome" ) );
                fornecedores.setEmail( result.getString( "email" ) );
                fornecedores.setTelefone( result.getString( "telefone" ) );
                fornecedores.setSite( result.getString( "site" ) );
                fornecedores.setEndereco( result.getString( "endereco" ) );
                fornecedores.setNif( result.getString( "nif" ) );
                fornecedores.setFkRegime( new Regime( result.getInt( "fk_regime" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return fornecedores;

    }

}
