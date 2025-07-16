/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import dao.UsuarioDao;
import entity.TbSexo;
import entity.TbStatus;
import entity.TbTipoUsuario;
import entity.TbUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import modelo.UsuarioModelo;
import util.BDConexao;
import static util.JPAEntityMannagerFactoryUtil.em;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class UsuariosController implements EntidadeFactory
{

    private BDConexao conexao;
    private StringBuffer sql;

    public UsuariosController( BDConexao conexao )
    {
        this.conexao = conexao;
    }
    
    public boolean exist ( String userName, String senha ) throws SQLException
    {
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer ();

        sql.append ( "CALL DM_EXIST_LOGIN('" );
        sql.append ( userName );
        sql.append ( "','" );
        sql.append ( senha );
        sql.append ( "')" );

        rs = conexao.executeQuery ( sql.toString () );

        return rs.next ();

    }

    public boolean isAdmintrador ( String userName, String senha ) throws SQLException
    {
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_usuario WHERE userName = '" + userName.toUpperCase () + "' AND senha = '" + senha + "' AND idTipoUsuario = 1   ";
        //String sql = "SELECT * FROM tb_usuario WHERE userName = '" +userName +"' AND senha = '" +senha +"' AND idTipoUsuario = 1";

        System.out.println ( sql );
        rs = conexao.executeQuery ( sql );

        if ( rs.next () )
        {
            return true;
        }

        return false;

    }

    public boolean exist_senha ( String senha ) throws SQLException
    {
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_usuario WHERE senha = '" + senha + "'";

        rs = conexao.executeQuery ( sql );

        if ( rs.next () )
        {
            return true;
        }

        return false;

    }

    public TbUsuario createNewUsuarioInstance ( int operacao, UsuarioModelo usuarioModelo )
    {
        String encriptedPass = new UsuarioDao (em ).encriptPass ( usuarioModelo.getSenha ());
        sql = new StringBuffer ();
        sql.append ( " CALL DM_PROCEDIMENTO_USUARIO('" );
        sql.append ( usuarioModelo.getNome () );
        sql.append ( "','" );

        sql.append ( encriptedPass );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getStatus () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getDataNascimento () );
        sql.append ( "','" );

        sql.append ( usuarioModelo.getTelefone () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getEmail () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getEndereco () );
        sql.append ( "'," );

        sql.append ( usuarioModelo.getCodigo_sexo () );
        sql.append ( ",'" );

        sql.append ( usuarioModelo.getUserName () );
        sql.append ( "'," );

        sql.append ( usuarioModelo.getIdTipousuario () );
        sql.append ( "," );

        sql.append ( usuarioModelo.getCodigo () );
        sql.append ( "," );

        sql.append ( operacao );
        sql.append ( ")" );

        System.out.println ( sql );
        conexao.executeUpdate ( sql.toString () );

        return new UsuarioDao ( em ).getLastInsertedRow ();

    }

    public boolean operacao ( int operacao, UsuarioModelo usuarioModelo )
    {
        sql = new StringBuffer ();
        sql.append ( " CALL DM_PROCEDIMENTO_USUARIO('" );
        sql.append ( usuarioModelo.getNome () );
        sql.append ( "','" );

        sql.append ( usuarioModelo.getSenha () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getStatus () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getDataNascimento () );
        sql.append ( "','" );

        sql.append ( usuarioModelo.getTelefone () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getEmail () );
        sql.append ( "','" );
        sql.append ( usuarioModelo.getEndereco () );
        sql.append ( "'," );

        sql.append ( usuarioModelo.getCodigo_sexo () );
        sql.append ( ",'" );

        sql.append ( usuarioModelo.getUserName () );
        sql.append ( "'," );

        sql.append ( usuarioModelo.getIdTipousuario () );
        sql.append ( "," );

        sql.append ( usuarioModelo.getCodigo () );
        sql.append ( "," );

        sql.append ( operacao );
        sql.append ( ")" );

        System.out.println ( sql );

        return conexao.executeUpdate ( sql.toString () );

    }

    public int getCodigo ( String nome ) throws SQLException
    {
        sql = new StringBuffer ();

        sql.append ( "CALL DM_PROCEDIMENTO_GET_ID_BY_NAME('" );
        sql.append ( nome );
        sql.append ( "')" );

        ResultSet rs = conexao.executeQuery ( sql.toString () );

        int codigo = 0;

        if ( rs.next () )
        {
            codigo = rs.getInt ( "codigo" );
        }

        return codigo;

    }

    public UsuarioModelo getUsuario ( int codigo ) throws SQLException
    {

        UsuarioModelo modelo = new UsuarioModelo ();
        ResultSet rs = null;

        sql = new StringBuffer ();
        sql.append ( " CALL DM_PROCEDIMENTO_GET_USERS(" );
        sql.append ( codigo );
        sql.append ( ")" );

        System.out.println ( sql );

        rs = conexao.executeQuery ( sql.toString () );

        if ( rs.next () )
        {
            modelo.setNome ( rs.getString ( "nome" ) );
            modelo.setSenha ( rs.getString ( "senha" ) );
            modelo.setStatus ( rs.getString ( "status" ) );
            modelo.setDataNascimento ( rs.getString ( "dataNascimento" ) );
            modelo.setTelefone ( rs.getString ( "telefone" ) );
            modelo.setEmail ( rs.getString ( "email" ) );
            modelo.setEndereco ( rs.getString ( "endereco" ) );
            modelo.setUserName ( rs.getString ( "userName" ) );
            modelo.setCodigo_sexo ( rs.getInt ( "codigo_sexo" ) );
            modelo.setIdTipousuario ( rs.getInt ( "idTipoUsuario" ) );
        }
        return modelo;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbUsuario clientes = ( TbUsuario ) object;
        String INSERT = "INSERT INTO tb_usuario( nome , senha , status , dataNascimento , telefone, email, endereco, "
                + ")"
                + " VALUES("
                + "'" + clientes.getNome() + "' , "
                //                + "'" + clientes.getMorada() + "' , "
                + "'" + clientes.getTelefone() + "' , "
                //                + "'" + clientes.getNif() + "' , "
                + "'" + clientes.getEmail()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
                TbUsuario usuario = ( TbUsuario ) object;
        PreparedStatement stmt;
        String UPDATE = "UPDATE tb_usuario SET "
                + " nome = ?, "
                + " senha = ? ,   "
                + " status = ? , "
                + " dataNascimento = ?,  "
                + " telefone = ? , "
                + " email = ? , "
                + " endereco = ?  , "
                + " codigo_sexo = ?  , "
                + " userName = ?, "
                + " idTipoUsuario = ? , "
                + " sobreNome = ?, "
                + " idStatus = ?, "
                + " dataUltimoAcesso = ? "
                + " WHERE codigo = ?";
        try
        {
            System.out.println( UPDATE );

            stmt = conexao.getConnection().prepareStatement( UPDATE );

            int cod = 1;
            System.out.println( "Nome UsuarioController: "+usuario.getNome() );
            stmt.setString(cod++, usuario.getNome() );
            stmt.setString(cod++, usuario.getSenha() );
            stmt.setString(cod++, usuario.getStatus() );
            stmt.setString(cod++, MetodosUtil.getDataBanco(usuario.getDataNascimento() ) );
            stmt.setString(cod++, usuario.getTelefone() );
            stmt.setString(cod++, usuario.getEmail() );
            stmt.setString(cod++, usuario.getEndereco() );
            stmt.setInt(cod++, usuario.getCodigoSexo().getCodigo() );
            stmt.setString(cod++, usuario.getUserName() );
            stmt.setInt(cod++, usuario.getIdTipoUsuario().getIdTipoUsuario() );
            stmt.setString(cod++, usuario.getSobreNome() );
            stmt.setInt(cod++, usuario.getIdStatus().getIdStatus() );
            stmt.setString(cod++, MetodosUtil.getDataBanco(usuario.getDataUltimoAcesso() ) );
            stmt.setInt(cod++, usuario.getCodigo() );
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
        String DELETE = "DELETE FROM tb_usuario WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbUsuario> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_usuario ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbUsuario> lista_clientes = new ArrayList<>();
        TbUsuario usuario;
        try
        {

            while ( result.next() )
            {
                usuario = new TbUsuario();
                usuario.setCodigo( result.getInt( "codigo" ) );
                usuario.setNome( result.getString( "nome" ) );
                usuario.setSenha( result.getString( "senha" ) );
                usuario.setStatus( result.getString( "status" ) );
                usuario.setDataNascimento( result.getDate( "dataNascimento" ) );
                usuario.setTelefone( result.getString( "telefone" ) );
                usuario.setEmail( result.getString( "email" ) );
                usuario.setEndereco( result.getString( "endereco" ) );
                usuario.setCodigoSexo( new TbSexo( result.getInt( "codigo_sexo" ) ) );
                usuario.setUserName( result.getString( "userName" ) );
                usuario.setIdStatus( new TbStatus( result.getInt( "idStatus" ) ) );
                usuario.setDataUltimoAcesso( result.getDate( "dataUltimoAcesso" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_clientes;
    }

    public TbUsuario getUsuarioByCodigo( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_usuario WHERE codigo = " + codigo + "";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbUsuario usuario = null;
        try
        {

            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario.setCodigo( result.getInt( "codigo" ) );
                usuario.setNome( result.getString( "nome" ) );
                usuario.setIdTipoUsuario( new TbTipoUsuario( result.getInt( "idTipoUsuario" ) ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return usuario;

    }

    @Override
    public Vector<String> getVector()
    {
        String FIND_ALL = "SELECT nome FROM tb_usuario ORDER BY codigo ASC";
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
        String FIND_ALL = "SELECT nome FROM tb_usuario  WHERE codigo <> 1 ORDER BY codigo ASC";
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
        String FIND_ALL = "SELECT nome FROM tb_usuario  WHERE  nome LIKE '%" + prefixo + "%'  ORDER BY codigo ASC";
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

        String FIND__BY_CODIGO = "SELECT * FROM tb_usuario WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbUsuario usuario = null;
        try
        {

            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario.setCodigo( result.getInt( "codigo" ) );
                usuario.setNome( result.getString( "nome" ) );
                usuario.setSenha( result.getString( "senha" ) );
                usuario.setStatus( result.getString( "status" ) );
                usuario.setDataNascimento( result.getDate( "dataNascimento" ) );
                usuario.setTelefone( result.getString( "telefone" ) );
                usuario.setEmail( result.getString( "email" ) );
                usuario.setEndereco( result.getString( "endereco" ) );
                usuario.setCodigoSexo( new TbSexo( result.getInt( "codigo_sexo" ) ) );
                usuario.setUserName( result.getString( "userName" ) );
                usuario.setIdStatus( new TbStatus( result.getInt( "idStatus" ) ) );
                usuario.setDataUltimoAcesso( result.getDate( "dataUltimoAcesso" ) );
                usuario.setIdTipoUsuario( new TbTipoUsuario(result.getInt( "idTipoUsuario" )));

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return usuario;

    }
    
    public Object findByName( String nome )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_usuario WHERE nome = '" + nome + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbUsuario usuario = null;
        try
        {

            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario.setCodigo( result.getInt( "codigo" ) );
                usuario.setNome( result.getString( "nome" ) );
                usuario.setSenha( result.getString( "senha" ) );
                usuario.setStatus( result.getString( "status" ) );
                usuario.setDataNascimento( result.getDate( "dataNascimento" ) );
                usuario.setTelefone( result.getString( "telefone" ) );
                usuario.setEmail( result.getString( "email" ) );
                usuario.setEndereco( result.getString( "endereco" ) );
                usuario.setCodigoSexo( new TbSexo( result.getInt( "codigo_sexo" ) ) );
                usuario.setUserName( result.getString( "userName" ) );
                usuario.setIdStatus( new TbStatus( result.getInt( "idStatus" ) ) );
                usuario.setDataUltimoAcesso( result.getDate( "dataUltimoAcesso" ) );
                usuario.setIdTipoUsuario( new TbTipoUsuario(result.getInt( "idTipoUsuario" )));

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return usuario;

    }

    public TbUsuario getLastUsuario()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, c.*  FROM tb_usuario c";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbUsuario usuario = null;
        try
        {

            if ( result.next() )
            {
                usuario = new TbUsuario();
                usuario.setCodigo( result.getInt( "codigo" ) );
                usuario.setNome( result.getString( "nome" ) );
                usuario.setSenha( result.getString( "senha" ) );
                usuario.setStatus( result.getString( "status" ) );
                usuario.setDataNascimento( result.getDate( "dataNascimento" ) );
                usuario.setTelefone( result.getString( "telefone" ) );
                usuario.setEmail( result.getString( "email" ) );
                usuario.setEndereco( result.getString( "endereco" ) );
                usuario.setCodigoSexo( new TbSexo( result.getInt( "codigo_sexo" ) ) );
                usuario.setUserName( result.getString( "userName" ) );
                usuario.setIdStatus( new TbStatus( result.getInt( "idStatus" ) ) );
                usuario.setDataUltimoAcesso( result.getDate( "dataUltimoAcesso" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return usuario;

    }

    public TbUsuario getClienteByNome( String nome )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM tb_usuario a WHERE nome = '" + nome + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbUsuario armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new TbUsuario();
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

}
