/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AccessoArmazem;
import entity.TbArmazem;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class AcessoStockController implements EntidadeFactory
{

    private BDConexao conexao;

    public AcessoStockController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        AccessoArmazem accessoArmazem = ( AccessoArmazem ) object;
        String INSERT = "INSERT INTO accesso_armazem( fk_usuario , fk_armazem , data_time "
                + ")"
                + " VALUES("
                + accessoArmazem.getFkUsuario().getCodigo() + ", "
                + accessoArmazem.getFkArmazem().getCodigo() + ", "
                + "'" + MetodosUtil.getDataBancoFull( accessoArmazem.getDataTime()) + "'"
                + " ) ";

        System.out.println( "Modo INSERT" + INSERT );
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
        String DELETE = "DELETE FROM accesso_armazem WHERE pk_accesso_armazem = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<AccessoArmazem> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM accesso_armazem ORDER BY pk_accesso_armazem ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<AccessoArmazem> lista_accesso_armazem = new ArrayList<>();
        AccessoArmazem accesso_armazem;
        try
        {

            while ( result.next() )
            {
                accesso_armazem = new AccessoArmazem();
                accesso_armazem.setPkAccessoArmazem(result.getInt( "pk_accesso_armazem" ) );
                accesso_armazem.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
                accesso_armazem.setFkArmazem( new TbArmazem( result.getInt( "fk_armazem" ) ) );
                accesso_armazem.setDataTime(result.getDate( "data_time" ) );

                lista_accesso_armazem.add( accesso_armazem );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_accesso_armazem;
    }
    
    public Vector<String> getAllArmazemByIdUSuario( int cod_usuario ){
        {
//            String FIND_ALL = "SELECT designacao FROM documento ORDER BY pk_documento ASC";
//        ( "SELECT a.fkArmazem.designacao FROM AccessoArmazem a WHERE a.fkUsuario.codigo = :cod_usuario" )
//"SELECT s.* FROM tb_stock s INNER JOIN tb_produto p ON p.codigo = s.cod_produto_codigo WHERE s.cod_armazem = " + idArmazem + " AND p.designacao LIKE '%" + designacao + "%' ";
            String FIND_ALL = "SELECT a.fkArmazem.designacao FROM accesso_armazem a WHERE a.fkUsuario.codigo = " + cod_usuario;
            ResultSet result = conexao.executeQuery(FIND_ALL);
            Vector<String> lista_documento = new Vector<>();
            try {

                while (result.next()) {

                    lista_documento.add(result.getString("designacao"));

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return lista_documento;
        }
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM accesso_armazem WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AccessoArmazem accesso_armazem = null;
        try
        {

            if ( result.next() )
            {
                accesso_armazem = new AccessoArmazem();
                accesso_armazem.setPkAccessoArmazem(result.getInt( "pk_accesso_armazem" ) );
                accesso_armazem.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
                accesso_armazem.setFkArmazem( new TbArmazem( result.getInt( "fk_armazem" ) ) );
                accesso_armazem.setDataTime(result.getDate( "data_time" ) );


            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return accesso_armazem;

    }

    public AccessoArmazem getLast()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_accesso_armazem) as maximo_id, v.*  FROM accesso_armazem v";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        AccessoArmazem accesso_armazem = null;
        try
        {

            if ( result.next() )
            {
                accesso_armazem = new AccessoArmazem();
                accesso_armazem.setPkAccessoArmazem( result.getInt( "maximo_id" ) );
                accesso_armazem.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
                accesso_armazem.setFkArmazem( new TbArmazem( result.getInt( "fk_armazem" ) ) );
                accesso_armazem.setDataTime(result.getDate( "data_time" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return accesso_armazem;

    }

    private AccessoArmazem getAcessoaResultSet( AccessoArmazem accesso_armazem, ResultSet result )
    {
        try
        {
                accesso_armazem.setPkAccessoArmazem( result.getInt( "pk_accesso_armazem" ) );
                accesso_armazem.setFkUsuario( new TbUsuario( result.getInt( "fk_usuario" ) ) );
                accesso_armazem.setFkArmazem( new TbArmazem( result.getInt( "fk_armazem" ) ) );
                accesso_armazem.setDataTime(result.getDate( "data_time" ) );
            
        }
        catch ( Exception e )
        {
        }

        return accesso_armazem;
    }


}
