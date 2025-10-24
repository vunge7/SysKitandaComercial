/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AccessoArmazem;
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
public class ArmazensAccessoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ArmazensAccessoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        AccessoArmazem armazem = ( AccessoArmazem ) object;
        String INSERT = "INSERT INTO accesso_armazem( designacao , "
                + ")"
                + " VALUES("
                //                + "'" + armazem.getDesignacao() + "' , "
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_accesso_armazem )
    {
        String DELETE = "DELETE FROM accesso_armazem WHERE pk_accesso_armazem = " + pk_accesso_armazem;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<AccessoArmazem> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM accesso_armazem ORDER BY pk_accesso_armazem ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<AccessoArmazem> lista_armazem = new ArrayList<>();
        AccessoArmazem accessoArmazem;
        try
        {

            while ( result.next() )
            {
                accessoArmazem = new AccessoArmazem();
//                accessoArmazem.setDesignacao(result.getString( "designacao" ) );
                lista_armazem.add( accessoArmazem );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_armazem;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int pk_accesso_armazem )
    {

        String FIND__BY_CODIGO = "SELECT * FROM accesso_armazem WHERE pk_accesso_armazem = " + pk_accesso_armazem;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AccessoArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new AccessoArmazem();
//                armazem.setCodigo(result.getInt( "pk_accesso_armazem" ) );
//                armazem.setDesignacao(result.getString("designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public AccessoArmazem getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_accesso_armazem) as maximo_id, a.*  FROM accesso_armazem a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AccessoArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new AccessoArmazem();
//                armazem.setCodigo(result.getInt( "maximo_id" ) );
//                armazem.setDesignacao(result.getString("designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public AccessoArmazem getArmazemByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM accesso_armazem a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AccessoArmazem armazem = null;
        try
        {

            if ( result.next() )
            {
                armazem = new AccessoArmazem();
//                armazem.setCodigo(result.getInt( "pk_accesso_armazem" ) );
//                armazem.setDesignacao(result.getString("designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return armazem;

    }

    public Vector<String> getAllArmazemExceptoEconomatoByIdUSuario( int idUsuario )
    {

        String query = "SELECT a.designacao AS designacao FROM accesso_armazem ac "
                + " INNER JOIN tb_armazem a ON a.codigo = ac.fk_armazem"
                + " WHERE ac.fk_usuario = " + idUsuario +" ORDER BY a.designacao";
        System.out.println( query );
        ResultSet result = conexao.executeQuery( query );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "designacao" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return vector;
    }
}
