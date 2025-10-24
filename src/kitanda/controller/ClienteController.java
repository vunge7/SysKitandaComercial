/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.controller;


import java.sql.Connection;
import kitanda.controller.*;
//import entity.Nacionalidade;
import entity.TbCliente;
import entity.TbProfissao;
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
public class ClienteController implements EntidadeFactory
{

    private BDConexao conexao;

    public ClienteController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbCliente cliente = ( TbCliente ) object;
        String INSERT = "INSERT INTO tb_cliente( nome, morada , telefone , nif , email"
                + ")"
                + " VALUES("
                + "'" + cliente.getNome() + "' , "
                + "'" + cliente.getMorada() + "' , "
                + "'" + cliente.getTelefone() + "' , "
                + "'" + cliente.getNif() + "' , "
                + "'" + cliente.getEmail() + "' "
                + " ) ";
//                + "'" + cliente.getBiPassport() + "' , "
//                + cliente.getStatusEliminado() + " , "
//                + "'" + MetodosUtil.getDataBanco( cliente.getDataNasc() ) + "' , "
//                + "'" + cliente.getLocalNasc() + "' , "
//                + "'" + cliente.getSexo() + "' , "
//                + cliente.getFkNacionalidade().getIdNacionalidade() + " , "
//                + cliente.getFkProfissao().getIdProfissao() + " , "
//                + "'" + cliente.getTipoCliente() + "' "
//                + " ) ";

        return conexao.executeUpdate( INSERT );

    }
//    public boolean salvar( Object object )
//    {
//        TbCliente cliente = ( TbCliente ) object;
//        String INSERT = "INSERT INTO tb_cliente( nome, morada , telefone , nif , email, bi_passport ,"
//                + " status_eliminado, dataNasc, localNasc , sexo , fk_nacionalidade , fk_profissao, tipo_cliente "
//                + ")"
//                + " VALUES("
//                + "'" + cliente.getNome() + "' , "
//                + "'" + cliente.getMorada() + "' , "
//                + "'" + cliente.getTelefone() + "' , "
//                + "'" + cliente.getNif() + "' , "
//                + "'" + cliente.getEmail() + "' , "
//                + "'" + cliente.getBiPassport() + "' , "
//                + cliente.getStatusEliminado() + " , "
//                + "'" + MetodosUtil.getDataBanco( cliente.getDataNasc() ) + "' , "
//                + "'" + cliente.getLocalNasc() + "' , "
//                + "'" + cliente.getSexo() + "' , "
//                + cliente.getFkNacionalidade().getIdNacionalidade() + " , "
//                + cliente.getFkProfissao().getIdProfissao() + " , "
//                + "'" + cliente.getTipoCliente() + "' "
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    @Override
    public boolean actualizar( Object object )
    {
        TbCliente cliente = ( TbCliente ) object;
        String UPDATE = "UPDATE tb_cliente SET "
                + " nome = '" + cliente.getNome() + "' , "
                + " morada = '" + cliente.getMorada() + "' , "
                + " telefone = '" + cliente.getTelefone() + "' , "
                + " nif = '" + cliente.getNif() + "' , "
                + " email = '" + cliente.getEmail() + "' "
                + " WHERE codigo = " + cliente.getCodigo();
//                + " status_eliminado = " + cliente.getStatusEliminado() + " , "
//                + " dataNasc = '" + MetodosUtil.getDataBanco( cliente.getDataNasc() ) + "' , "
//                + " localNasc = '" + cliente.getLocalNasc() + "' , "
//                + " sexo = '" + cliente.getSexo() + "' , "
//                + " fk_nacionalidade = " + cliente.getFkNacionalidade().getIdNacionalidade() + " , "
//                + " fk_profissao = " + cliente.getFkProfissao().getIdProfissao() + " , "
//                + " tipo_cliente = '" + cliente.getTipoCliente() + "' "
//                + " WHERE codigo = " + cliente.getCodigo();

        return conexao.executeUpdate( UPDATE );

    }
//    public boolean actualizar( Object object )
//    {
//        TbCliente cliente = ( TbCliente ) object;
//        String UPDATE = "UPDATE tb_cliente SET "
//                + " nome = '" + cliente.getNome() + "' , "
//                + " morada = '" + cliente.getMorada() + "' , "
//                + " telefone = '" + cliente.getTelefone() + "' , "
//                + " nif = '" + cliente.getNif() + "' , "
//                + " email = '" + cliente.getEmail() + "' , "
//                + " status_eliminado = " + cliente.getStatusEliminado() + " , "
//                + " dataNasc = '" + MetodosUtil.getDataBanco( cliente.getDataNasc() ) + "' , "
//                + " localNasc = '" + cliente.getLocalNasc() + "' , "
//                + " sexo = '" + cliente.getSexo() + "' , "
//                + " fk_nacionalidade = " + cliente.getFkNacionalidade().getIdNacionalidade() + " , "
//                + " fk_profissao = " + cliente.getFkProfissao().getIdProfissao() + " , "
//                + " tipo_cliente = '" + cliente.getTipoCliente() + "' "
//                + " WHERE codigo = " + cliente.getCodigo();
//
//        return conexao.executeUpdate( UPDATE );
//
//    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_cliente WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbCliente> listarTodos()
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM tb_cliente ORDER BY nome ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbCliente> lista_cliente = new ArrayList<>();
        TbCliente cliente;
        try
        {

            while ( result.next() )
            {
                cliente = new TbCliente();
                cliente.setCodigo( result.getInt( "codigo" ) );
                cliente.setNome( result.getString( "nome" ) );
                cliente.setMorada( result.getString( "morada" ) );
                cliente.setTelefone( result.getString( "telefone" ) );
                cliente.setNif( result.getString( "nif" ) );
                cliente.setEmail( result.getString( "email" ) );
//                cliente.setBiPassport( result.getString( "bi_passport" ) );
//                cliente.setStatusEliminado( result.getBoolean( "status_eliminado" ) );
//                cliente.setDataNasc( result.getDate( "dataNasc" ) );
//                cliente.setLocalNasc( result.getString( "localNasc" ) );
//                cliente.setSexo( result.getString( "sexo" ) );
//                cliente.setFkNacionalidade( new Nacionalidade( result.getInt( "fk_nacionalidade" ) ) );
//                cliente.setFkProfissao( new TbProfissao( result.getInt( "fk_profissao" ) ) );
//                cliente.setTipoCliente( result.getString( "tipo_cliente" ) );

                lista_cliente.add( cliente );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_cliente;
    }

    public List<TbCliente> listarTodos( String tipo_cliente )
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM tb_cliente WHERE  tipo_cliente ='" + tipo_cliente + "' ORDER BY nome ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbCliente> lista_cliente = new ArrayList<>();
        TbCliente cliente;
        try
        {

            while ( result.next() )
            {
                cliente = new TbCliente();
                cliente.setCodigo( result.getInt( "codigo" ) );
                cliente.setNome( result.getString( "nome" ) );
                cliente.setMorada( result.getString( "morada" ) );
                cliente.setTelefone( result.getString( "telefone" ) );
                cliente.setNif( result.getString( "nif" ) );
                cliente.setEmail( result.getString( "email" ) );
//                cliente.setBiPassport( result.getString( "bi_passport" ) );
//                cliente.setStatusEliminado( result.getBoolean( "status_eliminado" ) );
//                cliente.setDataNasc( result.getDate( "dataNasc" ) );
//                cliente.setLocalNasc( result.getString( "localNasc" ) );
//                cliente.setSexo( result.getString( "sexo" ) );
//                cliente.setFkNacionalidade( new Nacionalidade( result.getInt( "fk_nacionalidade" ) ) );
//                cliente.setFkProfissao( new TbProfissao( result.getInt( "fk_profissao" ) ) );
//                cliente.setTipoCliente( result.getString( "tipo_cliente" ) );

                lista_cliente.add( cliente );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_cliente;
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
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_cliente WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
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
//                cliente.setBiPassport( result.getString( "bi_passport" ) );
//                cliente.setStatusEliminado( result.getBoolean( "status_eliminado" ) );
//                cliente.setDataNasc( result.getDate( "dataNasc" ) );
//                cliente.setLocalNasc( result.getString( "localNasc" ) );
//                cliente.setSexo( result.getString( "sexo" ) );
//                cliente.setFkNacionalidade( new Nacionalidade( result.getInt( "fk_nacionalidade" ) ) );
//                cliente.setFkProfissao( new TbProfissao( result.getInt( "fk_profissao" ) ) );
//                cliente.setTipoCliente( result.getString( "tipo_cliente" ) );

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

        String FIND_BY_NOME = "SELECT * FROM tb_cliente WHERE nome = '" + nome +"'";
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
//                cliente.setBiPassport( result.getString( "bi_passport" ) );
//                cliente.setStatusEliminado( result.getBoolean( "status_eliminado" ) );
//                cliente.setDataNasc( result.getDate( "dataNasc" ) );
//                cliente.setLocalNasc( result.getString( "localNasc" ) );
//                cliente.setSexo( result.getString( "sexo" ) );
//                cliente.setFkNacionalidade( new Nacionalidade( result.getInt( "fk_nacionalidade" ) ) );
//                cliente.setFkProfissao( new TbProfissao( result.getInt( "fk_profissao" ) ) );
//                cliente.setTipoCliente( result.getString( "tipo_cliente" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return cliente;

    }

    public Vector<String> getVector( String tipo_cliente )
    {
        List<TbCliente> listarTodos = listarTodos( tipo_cliente );
        Vector<String> vector = new Vector<>();
        vector.add( 0, "--Seleccione--" );
        if ( !Objects.isNull( listarTodos ) )
        {

            for ( int i = 0; i < listarTodos.size(); i++ )
            {
                TbCliente get = listarTodos.get( i );
                vector.addElement( get.getNome() );

            }
        }
        return vector;
    }
    public static void main( String[] args )
    {
    }

}
