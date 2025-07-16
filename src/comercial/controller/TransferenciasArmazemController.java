/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TransferenciaArmazem;
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
public class TransferenciasArmazemController implements EntidadeFactory
{

    private BDConexao conexao;

    public TransferenciasArmazemController( BDConexao conexao )
    {
        this.conexao = conexao;
    }
//pk_transferencia_armazem  + venda.getIdArmazemFK().getCodigo() + ", "

    @Override
    public boolean salvar( Object object )
    {
        TransferenciaArmazem transferenciaArmazem = ( TransferenciaArmazem ) object;
        String INSERT = "INSERT INTO transferencia_armazem( data_hora , fk_usuario , nome_usuario "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( transferenciaArmazem.getDataHora() ) + "' , "
                + transferenciaArmazem.getFkUsuario() + ", "
                + "'" + transferenciaArmazem.getNomeUsuario() + "'"
                + " ) ";

        System.out.println( "INSERT " + INSERT );
        return conexao.executeUpdate( INSERT );

    }

//        public boolean salvar( Object object )
//    {
//        TbCliente clientes = ( TbCliente ) object;
//        String INSERT = "INSERT INTO tb_cliente( nome , morada , telefone , nif , email"
//                + ")"
//                + " VALUES("
//                + "'" + clientes.getNome() + "' , "
//                + "'" + clientes.getMorada() + "' , "
//                + "'" + clientes.getTelefone() + "' , "
//                + "'" + clientes.getNif() + "' , "
//                + "'" + clientes.getEmail()
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }
    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM transferencia_armazem WHERE pk_transferencia_armazem = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TransferenciaArmazem> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM transferencia_armazem ORDER BY pk_transferencia_armazem ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TransferenciaArmazem> lista_transferencia = new ArrayList<>();
        TransferenciaArmazem transferenciaArmazem;
        try
        {

            while ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );
                lista_transferencia.add( transferenciaArmazem );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_transferencia;
    }

    @Override
    public Vector<String> getVector()
    {
        {
            String FIND_ALL = "SELECT designacao FROM transferencia_armazem ORDER BY pk_transferencia_armazem ASC";
            ResultSet result = conexao.executeQuery( FIND_ALL );
            Vector<String> lista_transferencia = new Vector<>();
            try
            {

                while ( result.next() )
                {

                    lista_transferencia.add( result.getString( "designacao" ) );

                }

            }
            catch ( SQLException e )
            {
                e.printStackTrace();
            }

            return lista_transferencia;
        }
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM transferencia_armazem WHERE pk_transferencia_armazem = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

    public Integer findId( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM transferencia_armazem WHERE pk_transferencia_armazem = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "noe_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return 0;

    }

    public Integer getLastTransferencia()
    {
        String query = "SELECT MAX(pk_transferencia_armazem) AS last_id FROM transferencia_armazem";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer last_id = resultSet.getInt( "last_id" );
                return last_id;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }

        return 0;

    }

//    public Integer getLastTransferencia()
//    {
//        String query = "SELECT MAX(pk_transferencia_armazem) AS last_id FROM transferencia_armazem";
//
//        System.out.println( query );
//        ResultSet resultSet = conexao.executeQuery( query );
//
//        try
//        {
//            if ( resultSet.next() )
//            {
//                Integer last_id = resultSet.getInt( "last_id" );
//                return last_id;
//            }
//        }
//        catch ( SQLException ex )
//        {
//            ex.printStackTrace();
//        }
//
//        return 0;
//
//    }
    public Object findByName( String designacao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM transferencia_armazem WHERE designacao = '" + designacao + "' ";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

    public TransferenciaArmazem findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM transferencia_armazem WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

    public TransferenciaArmazem getLastArmazem()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_transferencia_armazem) as maximo_id, a.*  FROM transferencia_armazem a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

    public TransferenciaArmazem getLastLugar()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_transferencia_armazem) as maximo_id, a.*  FROM transferencia_armazem a";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

    public TransferenciaArmazem getArmazemByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT *  FROM transferencia_armazem a WHERE a.designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TransferenciaArmazem transferenciaArmazem = null;
        try
        {

            if ( result.next() )
            {
                transferenciaArmazem = new TransferenciaArmazem();
                transferenciaArmazem.setPkTransferenciaArmazem( result.getInt( "pk_transferencia_armazem" ) );
                transferenciaArmazem.setDataHora( result.getDate( "data_hora" ) );
                transferenciaArmazem.setFkUsuario( result.getInt( "fk_usuario" ) );
                transferenciaArmazem.setNomeUsuario( result.getString( "nome_usuario" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return transferenciaArmazem;

    }

//    public int getLastTransferencia() {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT  MAX(v.pkTransferenciaArmazem)  FROM TransferenciaArmazem  v");
//
//        List<Integer> result = query.getResultList();
//        em.close();
//
//        if (!result.isEmpty()) {
//            return result.get(0);
//        }
//
//        return 0;
//
//    }
//    public Integer getLastTransferencia() {
//        String query = "SELECT MAX(pk_transferencia_armazem) AS last_id FROM transferencia_armazem";
//
//        System.out.println(query);
//        ResultSet resultSet = conexao.executeQuery(query);
//
//        try {
//            if (resultSet.next()) {
//                Integer last_id = resultSet.getInt("last_id");
//                return last_id;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return 0;
//
//    }
    public TransferenciaArmazem findLastRow()
    {
        return ( TransferenciaArmazem ) findById( getLastTransferencia() );

    }

}
