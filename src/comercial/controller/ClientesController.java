/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class ClientesController implements EntidadeFactory
{

    private BDConexao conexao;

    public ClientesController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbCliente clientes = (TbCliente) object;
        String INSERT = "INSERT INTO tb_cliente( nome , morada , telefone , nif , email"
                + ")"
                + " VALUES("
                + "'" + clientes.getNome() + "' , "
                + "'" + clientes.getMorada() + "' , "
                + "'" + clientes.getTelefone() + "' , "
                + "'" + clientes.getNif() + "' , "
                + "'" + clientes.getEmail() + "'"
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    public Vector<String> listarTodosDaVenda( Date data_1, Date data_2 )
    {
        String FIND_ALL = "SELECT "
                + "	c.nome AS nome FROM tb_venda v, tb_cliente c "
                + " WHERE "
                + "	v.codigo_cliente = c.codigo "
                + " AND DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'"
                + " AND v.status_eliminado = 'false' "
                + " AND v.fk_documento = 2 "
                + " GROUP BY c.nome "
                + " ORDER BY c.nome";

        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> lista_cliente = new Vector<>();
        TbCliente cliente;
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
        String DELETE = "DELETE FROM tb_cliente WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbCliente> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_cliente ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbCliente> lista_clientes = new ArrayList<>();
        TbCliente clientes;
        try
        {

            while ( result.next() )
            {
                clientes = new TbCliente();
                clientes.setNome( result.getString( "nome" ) );
                clientes.setMorada( result.getString( "morada" ) );
                clientes.setTelefone( result.getString( "telefone" ) );
                clientes.setNif( result.getString( "nif" ) );
                clientes.setEmail( result.getString( "email" ) );
                lista_clientes.add( clientes );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_clientes;
    }

    public TbCliente findByCodigo( int codigo )
    {

        String FIND_BY_NOME = "SELECT * FROM tb_cliente WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_NOME );
        TbCliente cliente = null;
        try
        {

            if ( result.next() )
            {
                cliente = new TbCliente();
                cliente.setCodigo( result.getInt( "codigo" ) );
                cliente.setNome( result.getString( "nome" ) );
                cliente.setMorada( result.getString( "morada" ) );
                cliente.setTelefone( result.getString( "telefone" ) );
                cliente.setNif( result.getString( "nif" ) );
                cliente.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return cliente;

    }

    public TbCliente findByNome( String nome )
    {

        String FIND_BY_NOME = "SELECT * FROM tb_cliente WHERE nome = '" + nome + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_NOME );
        TbCliente cliente = null;
        try
        {

            if ( result.next() )
            {
                cliente = new TbCliente();
                cliente.setCodigo( result.getInt( "codigo" ) );
                cliente.setNome( result.getString( "nome" ) );
                cliente.setMorada( result.getString( "morada" ) );
                cliente.setTelefone( result.getString( "telefone" ) );
                cliente.setNif( result.getString( "nif" ) );
                cliente.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return cliente;

    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT nome FROM tb_cliente ORDER BY nome";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        vector.add( 0, "--Seleccione o Cliente--" );

        return vector;
    }

    public Vector<String> getVectorExecptoConsumidorFinal()
    {
        String FIND_ALL = "SELECT nome FROM tb_cliente  WHERE codigo <> 1 ORDER BY nome ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        vector.add( 0, "--Seleccione o Cliente--" );

        return vector;
    }

    public Vector<String> getVectorByIinciais( String prefixo )
    {
        String FIND_ALL = "SELECT nome FROM tb_cliente  WHERE  nome LIKE '%" + prefixo + "%'  ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return vector;
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_cliente WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbCliente clientes = null;
        try
        {

            if ( result.next() )
            {
                clientes = new TbCliente();
                clientes.setCodigo( result.getInt( "codigo" ) );
                clientes.setNome( result.getString( "nome" ) );
                clientes.setMorada( result.getString( "morada" ) );
                clientes.setTelefone( result.getString( "telefone" ) );
                clientes.setNif( result.getString( "nif" ) );
                clientes.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return clientes;

    }

    public TbCliente getClienteByNifOrberByNome( String nif )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_cliente WHERE nif LIKE '%" + nif + "%'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbCliente clientes = null;
        try
        {

            if ( result.next() )
            {
                clientes = new TbCliente();
                clientes.setCodigo( result.getInt( "codigo" ) );
                clientes.setNome( result.getString( "nome" ) );
                clientes.setMorada( result.getString( "morada" ) );
                clientes.setTelefone( result.getString( "telefone" ) );
                clientes.setNif( result.getString( "nif" ) );
                clientes.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return clientes;

    }

    public TbCliente getClienteByNifOrberByTelefone( String telefone )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_cliente WHERE telefone LIKE '%" + telefone + "%'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbCliente clientes = null;
        try
        {

            if ( result.next() )
            {
                clientes = new TbCliente();
                clientes.setCodigo( result.getInt( "codigo" ) );
                clientes.setNome( result.getString( "nome" ) );
                clientes.setMorada( result.getString( "morada" ) );
                clientes.setTelefone( result.getString( "telefone" ) );
                clientes.setNif( result.getString( "nif" ) );
                clientes.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return clientes;

    }

    public TbCliente getLastCliente()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, c.*  FROM tb_cliente c";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbCliente clientes = null;
        try
        {

            if ( result.next() )
            {
                clientes = new TbCliente();
                clientes.setCodigo( result.getInt( "maximo_id" ) );
                clientes.setNome( result.getString( "nome" ) );
                clientes.setMorada( result.getString( "morada" ) );
                clientes.setTelefone( result.getString( "telefone" ) );
                clientes.setNif( result.getString( "nif" ) );
                clientes.setEmail( result.getString( "email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return clientes;

    }

    public TbCliente getClienteByNome( String nome )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM tb_cliente a WHERE nome = '" + nome + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbCliente armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbCliente();
                armazem.setCodigo( result.getInt( "codigo" ) );
                armazem.setNome( result.getString( "nome" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public boolean existeClienteNome( String nome, Connection conexao )
    {
        String sql = "SELECT COUNT(*) FROM tb_cliente WHERE nome = ?";
        try ( PreparedStatement stmt = conexao.prepareStatement( sql ) )
        {
            stmt.setString( 1, nome );

            try ( ResultSet rs = stmt.executeQuery() )
            {
                if ( rs.next() )
                {
                    int count = rs.getInt( 1 );
                    return count > 0; // true se já existe
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeClienteNIF( String nif, Connection conexao )
    {
        String sql = "SELECT COUNT(*) FROM tb_cliente WHERE nif = ?";
        try ( PreparedStatement stmt = conexao.prepareStatement( sql ) )
        {

            stmt.setString( 1, nif );

            try ( ResultSet rs = stmt.executeQuery() )
            {
                if ( rs.next() )
                {
                    int count = rs.getInt( 1 );
                    return count > 0; // true se já existe
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existeClienteNIFParaOutroCliente( String nif, int codigo, Connection connection )
    {
        String sql = "SELECT COUNT(*) FROM tb_cliente WHERE nif = ? AND codigo <> ?";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ) )
        {
            stmt.setString( 1, nif );
            stmt.setInt( 2, codigo );

            try ( ResultSet rs = stmt.executeQuery() )
            {
                if ( rs.next() )
                {
                    int count = rs.getInt( 1 );
                    return count > 0; // true se já existe em outro cliente
                }
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return false;
    }

}
