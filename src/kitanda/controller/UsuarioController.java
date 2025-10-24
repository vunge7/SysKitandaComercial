/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.controller;


import java.sql.Connection;
import hotel.controller.*;
import entity.TbCliente;
import entity.TbProfissao;
import entity.TbSexo;
import entity.TbStatus;
import entity.TbTipoUsuario;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class UsuarioController implements EntidadeFactory
{

    private BDConexao conexao;

    public UsuarioController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        return true;
    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_usuario WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbUsuario> listarTodos()
    {
        String FIND_ALL = "SELECT * FROM tb_usuario ORDER BY nome ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbUsuario> lista_cliente = new ArrayList<>();
        TbUsuario usuario;
        try
        {
            while ( result.next() )
            {
                usuario = new TbUsuario();
                lista_cliente.add( getUsuarioResultSet( usuario, result ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_cliente;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_usuario WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbUsuario usuario = null;
        try
        {
            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario = getUsuarioResultSet( usuario, result );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return usuario;

    }

    public TbUsuario findUsuarioById( int codigo, ResultSet resultSet )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_usuario WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbUsuario usuario = null;
        try
        {
            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario = getUsuarioResultSet( usuario, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return usuario;

    }

    private TbUsuario getUsuarioResultSet( TbUsuario usuario, ResultSet result )
    {
        try
        {
            usuario.setCodigo( result.getInt( "codigo" ) );
            usuario.setNome( result.getString( "nome" ) );
            usuario.setTelefone( result.getString( "status" ) );
            usuario.setSenha( result.getString( "senha" ) );
            usuario.setDataNascimento( result.getDate( "dataNascimento" ) );
            usuario.setTelefone( result.getString( "telefone" ) );
            usuario.setTelefone( result.getString( "email" ) );
            usuario.setEndereco( result.getString( "endereco" ) );
            usuario.setCodigoSexo( new TbSexo( result.getInt( "codigo_sexo" ) ) );
            usuario.setUserName( result.getString( "userName" ) );
            usuario.setIdTipoUsuario( new TbTipoUsuario( result.getInt( "idTipoUsuario" ) ) );
            usuario.setSobreNome( result.getString( "sobreNome" ) );
            usuario.setIdStatus( new TbStatus( result.getInt( "idStatus" ) ) );
            usuario.setDataUltimoAcesso( result.getDate( "dataUltimoAcesso" ) );

        }
        catch ( Exception e )
        {
        }

        return usuario;
    }

    public static void main( String[] args )
    {
    }

}
