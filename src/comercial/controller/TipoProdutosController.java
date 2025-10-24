/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Familia;
import entity.TbTipoProduto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TipoProdutosController implements EntidadeFactory
{

    private BDConexao conexao;

    public TipoProdutosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
//    public boolean salvar(Object object) {
//        TbTipoProduto mesas = (TbTipoProduto) object;
//        String INSERT = "INSERT INTO tb_tipo_produto( designacao "
//                + ")"
//                + " VALUES("
//                + "'" + mesas.getDesignacao() + "'"
//                + " ) ";
//
//        return conexao.executeUpdate(INSERT);
//
//    }

    public boolean salvar( Object object )
    {

        TbTipoProduto tipo_produto = (TbTipoProduto) object;

        String INSERT = "INSERT INTO tb_tipo_produto( "
                + " designacao, fk_familia "
                + ")"
                + " VALUES("
                + "'" + tipo_produto.getDesignacao() + "', "
                + "" + tipo_produto.getFkFamilia().getPkFamilia() + ""
                //                + "'" + tipo_produto.getArea() + "'"
                + " ) ";

        System.err.println( "Query" + INSERT );

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbTipoProduto tipo_produto = (TbTipoProduto) object;
        PreparedStatement stmt;
        String UPDATE = "UPDATE tb_tipo_produto SET "
                + " designacao = ? , "
                + " fk_familia = ? "
                + " WHERE codigo = ?";
        try
        {
            System.out.println( UPDATE );

            stmt = conexao.getConnection().prepareStatement( UPDATE );

            int cod = 1;
            stmt.setString( cod++, tipo_produto.getDesignacao() );
            stmt.setInt( cod++, tipo_produto.getFkFamilia().getPkFamilia() );
            stmt.setInt( cod++, tipo_produto.getCodigo() );
            stmt.executeUpdate();
            stmt.close();
            return true;

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_tipo_produto WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbTipoProduto> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM tb_tipo_produto ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbTipoProduto> lista = new ArrayList<>();
        TbTipoProduto tipoProduto;
        try
        {
            while ( result.next() )
            {
                tipoProduto = new TbTipoProduto();
                setResultSetTipoProduto( result, tipoProduto );
                lista.add( tipoProduto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<TbTipoProduto> buscaTodasCategoriasByArea()
    {
//        String areas = DVML.getAreaByIdArea( area );
        String FIND_ALL = "SELECT * FROM tb_tipo_produto ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbTipoProduto> lista_tipo = new ArrayList<>();
        TbTipoProduto tipo_produto;
        try
        {
            while ( result.next() )
            {
//                System.out.println( result.getString( "area" ) );
                tipo_produto = new TbTipoProduto();
//                lista_tipo.add( getTipoProdutoResultSet( tipo_produto, result ) );
                tipo_produto.setCodigo( result.getInt( "codigo" ) );
                tipo_produto.setDesignacao( result.getString( "designacao" ) );
                tipo_produto.setFkFamilia( new Familia( result.getInt( "fk_familia" ) ) );
//                tipo_produto.setArea( result.getString( "area" ) );

                lista_tipo.add( tipo_produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_tipo;
    }

    public List<String> buscaTodasCategoriaNaoIngredientes()
    {
//        String areas = DVML.getAreaByIdArea( area );
        String FIND_ALL = "select t.designacao from tb_produto p  "
                + "inner join tb_tipo_produto t on t.codigo = p.cod_Tipo_Produto "
                + "where p.fk_grupo  <> 2 "
                + "group  by t.designacao ";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<String> lista_tipo = new ArrayList<>();
        try
        {
            while ( result.next() )
            {
//
                lista_tipo.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_tipo;
    }

    public List<TbTipoProduto> buscaTodasCategoriasByAreaExcept( int idCategoria )
    {
//        String areas = DVML.getAreaByIdArea( area );
        String FIND_ALL = "SELECT * FROM tb_tipo_produto   WHERE codigo <> " + idCategoria + "  ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbTipoProduto> lista_tipo = new ArrayList<>();
        TbTipoProduto tipo_produto;
        try
        {
            while ( result.next() )
            {
//                System.out.println( result.getString( "area" ) );
                tipo_produto = new TbTipoProduto();
//                lista_tipo.add( getTipoProdutoResultSet( tipo_produto, result ) );
                tipo_produto.setCodigo( result.getInt( "codigo" ) );
                tipo_produto.setDesignacao( result.getString( "designacao" ) );
                tipo_produto.setFkFamilia( new Familia( result.getInt( "fk_familia" ) ) );
//                tipo_produto.setArea( result.getString( "area" ) );

                lista_tipo.add( tipo_produto );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_tipo;
    }

    public TbTipoProduto getTipoProdutoByCodigo( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT *  FROM tb_tipo_produto  WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbTipoProduto tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new TbTipoProduto();
                setResultSetTipoProduto( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT designacao FROM tb_tipo_produto ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }
    
    public Vector<String> getVector1()
    {
        String FIND_ALL = "SELECT designacao FROM tb_tipo_produto WHERE fk_familia <> 1 ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

    public Vector<String> getVectorDesignacao()
    {
        String FIND_ALL = "SELECT designacao FROM tb_tipo_produto ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        lista.add( 0, "--Seleccione--" );
        return lista;

    }

    public Vector<String> getVectorByIdFamilia( int idFamilia )
    {
        String FIND_ALL = "SELECT designacao FROM tb_tipo_produto WHERE fk_familia = " + idFamilia + " ORDER BY designacao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista = new Vector<>();
        try
        {
            while ( result.next() )
            {
                lista.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;

    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_tipo_produto WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbTipoProduto tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new TbTipoProduto();
                setResultSetTipoProduto( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public TbTipoProduto getTipoFamiliaByDesignacao( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT *  FROM tb_tipo_produto  WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbTipoProduto tipoProduto = null;
        try
        {
            if ( result.next() )
            {
                tipoProduto = new TbTipoProduto();
                setResultSetTipoProduto( result, tipoProduto );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return tipoProduto;

    }

    public boolean exist_tipo_produto( String designacao )
    {
        String sql = "SELECT tp.codigo FROM tb_tipo_produto tp WHERE tp.designacao = '" + designacao + "'";
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

    private void setResultSetTipoProduto( ResultSet rs, TbTipoProduto tipoProduto )
    {
        try
        {
            tipoProduto.setCodigo( rs.getInt( "codigo" ) );
            tipoProduto.setDesignacao( rs.getString( "designacao" ) );
            tipoProduto.setFkFamilia( new Familia( rs.getInt( "fk_familia" ) ) );
        }
        catch ( SQLException e )
        {
        }
    }

}
