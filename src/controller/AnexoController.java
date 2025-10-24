/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.sql.Connection;
import entity.Anexos;
import entity.TbFuncionario;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AnexoController implements EntidadeFactory
{

    private BDConexao conexao;

    public AnexoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {

        Anexos anexo = ( Anexos ) object;
        String INSERT = "INSERT INTO anexos( nome_ficheiro, caminho, descricao, fk_funcionario, fk_usuario )"
                + " VALUES("
                + "'" + anexo.getNomeFicheiro() + "', "
                + "'" + anexo.getCaminhoFicheiro() + "', "
                + "'" + anexo.getDescricao() + "', "
                + anexo.getFkFuncionario() + ", "
                + anexo.getFkUsuario()
                + " )";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        Anexos anexo = ( Anexos ) object;

        String UPDATE = "UPDATE anexos SET "
                + " nome_ficheiro = '" + anexo.getNomeFicheiro() + "', "
                + " caminho = '" + anexo.getCaminhoFicheiro() + "', "
                + " descricao = '" + anexo.getDescricao() + "', "
                + " fk_funcionario = '" + anexo.getFkFuncionario() + "'"
                + " fk_usuario = '" + anexo.getFkUsuario() + "'"
                + " WHERE pk_anexos = " + anexo.getPkAnexos();

        return conexao.executeUpdate( UPDATE );

    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM anexos WHERE pk_anexo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<Anexos> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM anexos";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Anexos> lista_agenda = new ArrayList<>();
        Anexos anexo;
        try
        {

            while ( result.next() )
            {
                anexo = new Anexos();
                anexo.setPkAnexos( result.getInt( "pk_anexo" ) );
                anexo.setNomeFicheiro( result.getString( "nome_ficheiro" ) );
                anexo.setCaminhoFicheiro( result.getString( "caminho" ) );
                anexo.setDescricao( result.getString( "descricao" ) );
//                stock.setCodArmazem( new TbArmazem( result.getInt( "cod_armazem" ) ) );
                anexo.setFkFuncionario(new TbFuncionario( result.getInt( "fk_funcionario" ) ));
                anexo.setFkUsuario(new TbUsuario( result.getInt( "fk_usuario" ) ) );

                lista_agenda.add( anexo );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_agenda;
    }

    public List<Anexos> listarTodosByIdFuncionario( int idFuncionario )
    {

        String FIND_ALL = "SELECT * FROM anexos WHERE fk_funcionario = " + idFuncionario;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Anexos> lista_agenda = new ArrayList<>();
        Anexos anexo;
        try
        {

            while ( result.next() )
            {
                anexo = new Anexos();
                anexo.setPkAnexos(result.getInt( "pk_anexos" ) );
                anexo.setNomeFicheiro( result.getString( "nome_ficheiro" ) );
                anexo.setCaminhoFicheiro( result.getString( "caminho" ) );
                anexo.setDescricao( result.getString( "descricao" ) );
                anexo.setFkFuncionario(new TbFuncionario( result.getInt( "fk_funcionario" ) ));
                anexo.setFkUsuario(new TbUsuario( result.getInt( "fk_usuario" ) ));

                lista_agenda.add( anexo );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_agenda;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM anexos WHERE pk_anexo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Anexos anexo = null;
        try
        {

            if ( result.next() )
            {
                anexo = new Anexos();
                anexo.setPkAnexos( result.getInt( "pk_anexo" ) );
                anexo.setNomeFicheiro( result.getString( "nome_ficheiro" ) );
                anexo.setCaminhoFicheiro( result.getString( "caminho" ) );
                anexo.setDescricao( result.getString( "descricao" ) );
                anexo.setFkFuncionario(new TbFuncionario( result.getInt( "fk_funcionario" ) ));
                anexo.setFkUsuario(new TbUsuario( result.getInt( "fk_usuario" ) ));
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return anexo;

    }

    public int lastID()
    {
        String FIND_ALL = "SELECT MAX(pk_anexo) AS MAX_ID FROM anexos";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        try
        {

            if ( result.next() )
            {
                return result.getInt( "MAX_ID" );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return 0;
    }

    public static void main( String[] args )
    {
    }

}
