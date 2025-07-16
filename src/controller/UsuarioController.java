/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.UsuarioDao;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.UsuarioModelo;
import util.BDConexao;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class UsuarioController
{

    private BDConexao conexao;
    private StringBuffer sql;

    public UsuarioController ( BDConexao conexao )
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

    public static void main ( String[] args ) throws SQLException
    {
        BDConexao conexao = new BDConexao ();
        UsuarioController usuarioController = new UsuarioController ( conexao );
        if ( usuarioController.isAdmintrador ( "root", "root" ) )
        {
            System.err.println ( "ADMINISTRADOR" );
        }
    }

}
