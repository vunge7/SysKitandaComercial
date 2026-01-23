/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.Referencias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class ReferenciasController implements EntidadeFactory
{

    private BDConexao conexao;

    public ReferenciasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Referencias referencias = (Referencias) object;
        String INSERT = "INSERT INTO referencias( cod_barra , produto_id , obs , data_referencia , usuario_id"
                + ")"
                + " VALUES("
                + "'" + referencias.getCodBarra()+ "' , "
                + referencias.getProdutoId() + " , "
                + "'" + referencias.getObs() + "' , "
                + "'" + referencias.getDataReferencia() + "' , "
                + referencias.getUsuarioId()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }
    
    public boolean salvar(Referencias referencias) {

    String sql = "INSERT INTO referencias "
               + "(cod_barra, produto_id, obs, data_referencia, usuario_id) "
               + "VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {

        ps.setString(1, referencias.getCodBarra());
        ps.setInt(2, referencias.getProdutoId());

        if (referencias.getObs() != null) {
            ps.setString(3, referencias.getObs());
        } else {
            ps.setNull(3, Types.VARCHAR);
        }

        if (referencias.getDataReferencia() != null) {
            ps.setDate(4, new java.sql.Date(referencias.getDataReferencia().getTime()));
        } else {
            ps.setNull(4, Types.DATE);
        }

        ps.setInt(5, referencias.getUsuarioId());

        ps.executeUpdate();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public Vector<String> listarTodosDaVenda( Date data_1, Date data_2 )
    {
        String FIND_ALL = "SELECT "
                + "	c.nome AS nome FROM tb_venda v, referencias c "
                + " WHERE "
                + "	v.codigo_cliente = c.codigo "
                + " AND DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'"
                + " AND v.status_eliminado = 'false' "
                + " AND v.fk_documento = 2 "
                + " GROUP BY c.nome "
                + " ORDER BY c.nome";

        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_cliente = new Vector<>();
        Referencias cliente;
        try
        {
            while ( result.next() )
            {
                System.out.println( "Nome: " + result.getString( "nome" ) );
                lista_cliente.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_cliente;
    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM referencias WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Referencias> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM referencias ORDER BY id ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Referencias> lista_referencias = new ArrayList<>();
        Referencias referencias;
        try
        {

            while ( result.next() )
            {
                
                referencias = new Referencias();
                referencias.setCodBarra(result.getString( "cod_barra" ) );
                referencias.setProdutoId(result.getInt( "produto_id" ) );
                referencias.setObs(result.getString( "obs" ) );
                referencias.setDataReferencia(  result.getDate( "data_referencia" ) );
                referencias.setUsuarioId(   result.getInt( "usuario_id" ) );
                lista_referencias.add( referencias );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_referencias;
    }

    public Vector<Referencias> listarTodos2()
    {
        String FIND_ALL = "SELECT * FROM referencias ORDER BY cod_barra ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<Referencias> lista_referencias = new Vector<>();
        Referencias referencias;

        try
        {
            while ( result.next() )
            {
                referencias = new Referencias();
                referencias.setCodBarra(result.getString( "cod_barra" ) );
                referencias.setProdutoId(result.getInt( "produto_id" ) );
                referencias.setObs(result.getString( "obs" ) );
                referencias.setDataReferencia(  result.getDate( "data_referencia" ) );
                referencias.setUsuarioId(   result.getInt( "usuario_id" ) );
                lista_referencias.add( referencias );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_referencias;
    }

    public Referencias findByCodigo( int codigo )
    {

        String FIND_BY_NOME = "SELECT * FROM referencias WHERE id = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_NOME );
        Referencias referencias = null;
        try
        {

            if ( result.next() )
            {
                referencias = new Referencias();
                referencias.setCodBarra(result.getString( "cod_barra" ) );
                referencias.setProdutoId(result.getInt( "produto_id" ) );
                referencias.setObs(result.getString( "obs" ) );
                referencias.setDataReferencia(  result.getDate( "data_referencia" ) );
                referencias.setUsuarioId(   result.getInt( "usuario_id" ) );
    

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return referencias;

    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT cod_barra FROM referencias ORDER BY cod_barra";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "cod_barra" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        
        return vector;
    }
    
    public List<Referencias> listarTodos1() {
    String FIND_ALL = "SELECT * FROM referencias ORDER BY id ASC";
    List<Referencias> lista_referencias = new ArrayList<>();

    try (ResultSet result = conexao.executeQuery(FIND_ALL)) {

        while (result.next()) {
            Referencias ref = new Referencias();

            // ID
            ref.setId(result.getInt("id"));

            // Produto ID
            int produtoId = result.getInt("produto_id");
            if (result.wasNull()) produtoId = 0; // ou null se campo Integer
            ref.setProdutoId(produtoId);

            // Usuario ID
            int usuarioId = result.getInt("usuario_id");
            if (result.wasNull()) usuarioId = 0; // ou null se campo Integer
            ref.setUsuarioId(usuarioId);

            // Outros campos
            ref.setCodBarra(result.getString("cod_barra"));
            ref.setObs(result.getString("obs"));
            ref.setDataReferencia(result.getDate("data_referencia"));

            lista_referencias.add(ref);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erro ao listar referências: " + e.getMessage(),
                                      "Erro", JOptionPane.ERROR_MESSAGE);
    }

    return lista_referencias;
}


    @Override
    public Object findById( int codigo )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
