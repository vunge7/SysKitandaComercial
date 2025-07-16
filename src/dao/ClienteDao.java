/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbClienteJpaController;
import entity.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.DVML;
import static util.DVML.NUMBER_NIF_GENERICO;

/**
 *
 * @author Dallas
 */
public class ClienteDao extends TbClienteJpaController
{

    public ClienteDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public Vector<String> buscaTodos ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.nome FROM TbCliente c ORDER BY c.nome ASC " );
        Vector<String> result = ( Vector ) query.getResultList ();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbCliente> getAllCliente ()
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.nome FROM TbCliente  c" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<String> buscaTodosClientesExceptoConsumidorFinal ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.nome FROM TbCliente c WHERE c.codigo <> 1" );
        List<String> lista = query.getResultList ();

        lista.add ( 0, "--Seleccione--" );
        return lista;
    }
    
    public List<String> buscaTodosClientesExceptoConsumidorFinal1 ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.nome FROM TbCliente c WHERE c.codigo <> 1" );
        List<String> lista = query.getResultList ();

        lista.add ( 0, "--Todos Clientes--" );
        return lista;
    }
    public List<String> buscaTodosClientesInicialSelecione ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.nome FROM TbCliente c" );
        List<String> lista = query.getResultList ();

        lista.add ( 0, "--Seleccione o Cliente--" );
        return lista;
    }

    public List<TbCliente> getAllClienteOrdenado ()
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbCliente c ORDER BY c.nome ASC" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public TbCliente getClienteByNome ( String nome )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbCliente  c WHERE c.nome = :nome" )
                .setParameter ( "nome", nome );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        return new TbCliente ( 0 );
    }

//    public List<TbVenda> getAllVendasByClientes ( int codigo )
//    {
//
//        EntityManager em = getEntityManager ();
//        Query query = em.createQuery ( "SELECT s FROM TbVenda s WHERE s.codigoCliente.codigo = :codigo AND s.credito = 'true' AND s.statusEliminado = 'false'  ORDER BY s.codigo" )
//                .setParameter ( "codigo", codigo );
//        List<TbVenda> lista = query.getResultList ();
//
//        if (  ! lista.isEmpty () )
//        {
//            return lista;
//        }
//        return null;
//
//    }

    public List<TbCliente> buscaTodosClientes ()
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbCliente c ORDER BY c.nome ASC" );
        return query.getResultList ();
    }

    public int getIdByDescricao ( String nome )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.codigo FROM TbCliente c WHERE c.nome = :nome" )
                .setParameter ( "nome", nome );

        List result = query.getResultList ();

        if ( result != null )
        {

            return Integer.parseInt ( String.valueOf ( result.get ( 0 ) ) );
        }
        return 0;

    }

    public boolean existe_cliente ( String nome )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c.codigo FROM TbCliente c WHERE c.nome = :nome" )
                .setParameter ( "nome", nome );
        return  ! query.getResultList ().isEmpty ();

    }

    public List<TbCliente> getClienteLIKE_Nome ( String nome )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT p FROM TbCliente p WHERE p.nome LIKE :nome ORDER BY p.nome" )
                .setParameter ( "nome", "%" + nome + "%" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public boolean isClienteComVendasEfectuadas ( int pk_cliente, BDConexao conexao )
    {

        String sql = "SELECT * FROM tb_venda WHERE  codigo_cliente = " + pk_cliente;
        ResultSet rs = conexao.executeQuery ( sql );

        try
        {
            return rs.next ();
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
        }
        return false;

    }

    public boolean nif_existente ( String nif, BDConexao conexao )
    {

        String sql = "SELECT * FROM tb_cliente WHERE  nif = '" + nif + "'";
        ResultSet rs = conexao.executeQuery ( sql );

        try
        {
            return rs.next ();
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
        }
        return false;

    }

    public TbCliente getClienteNIFGenerico ( int idCliente )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbCliente  c WHERE c.codigo = :idCliente AND c.nif = :NIF_GENERICO" )
                .setParameter ( "idCliente", idCliente )
                .setParameter ( "NIF_GENERICO", DVML.NUMBER_NIF_GENERICO );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        return null;
    }

    public TbCliente getClienteNIFVazio ( int idCliente )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbCliente  c WHERE c.codigo = :idCliente AND c.nif = :VAZIO" )
                .setParameter ( "idCliente", idCliente )
                .setParameter ( "VAZIO", "" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        return null;
    }

    public boolean isClienteNIfGenerico ( int idCliente )
    {
        return getClienteNIFGenerico ( idCliente ) != null;
    }

    public boolean isClienteNIFVazio ( int idCliente )
    {
        return getClienteNIFVazio ( idCliente ) != null;
    }

    public static TbCliente getClienteOrberByNomeTradicional ( Integer cod_cliente, BDConexao conexao )
    {

        //String sql = "SELECT * FROM tb_cliente WHERE codigo = " +cod_cliente";
        String sql = String.format ( "SELECT * FROM tb_cliente  WHERE codigo = %d ", cod_cliente );
        ResultSet rs = conexao.executeQuery ( sql );
        System.out.println ( sql );
        TbCliente cliente = null;

        try
        {

            if ( rs.next () )
            {

                cliente = new TbCliente ();
                cliente.setCodigo ( rs.getInt ( "codigo" ) );
                cliente.setNome ( rs.getString ( "nome" ) );
                cliente.setMorada ( rs.getString ( "morada" ) );
                cliente.setTelefone ( rs.getString ( "telefone" ) );
                cliente.setNif ( rs.getString ( "nif" ) );
                cliente.setEmail ( rs.getString ( "email" ) );
//                cliente.setDescontoPercentual(rs.getDouble("desconto_percentual"));
//                cliente.setDataInicio(rs.getDate("data_inicio"));
//                cliente.setDataFim(rs.getDate("data_fim"));

            }
            return cliente;
            //conexao.close();
        }
        catch ( SQLException ex )
        {
            //conexao.close();
            ex.printStackTrace ();

            return null;
        }

    }

    public List<TbCliente> getClienteByNomes ( String nome )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT a.nome FROM TbCliente a WHERE  a.nome LIKE :nome ORDER BY a.nome" )
                .setParameter ( "nome", nome + "%" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        TbCliente clientes = new TbCliente ( 0 );
        // clientes.setNome("--Selecctione--");
        if ( result != null )
        {
            return result;
        }
        else
        {
            result.add ( clientes );
            return result;
        }

    }

    public List<TbCliente> getClientesByPhoneNumber ( String telefone )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT q.nome FROM TbCliente q WHERE q.telefone LIKE :telefone" )
                .setParameter ( "telefone", telefone + "%" );

        List<TbCliente> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;
    }

    public static TbCliente getClienteByNifOrberByNome ( String nif, BDConexao conexao )
    {

        //String sql = "SELECT * FROM tb_cliente WHERE codigo = " +cod_cliente";
        String sql = String.format ( "SELECT * FROM tb_cliente  WHERE nif = %s ", nif );
        ResultSet rs = conexao.executeQuery ( sql );
        System.out.println ( sql );
        TbCliente cliente = null;

        try
        {

            if ( rs.next () )
            {

                cliente = new TbCliente ();
                cliente.setCodigo ( rs.getInt ( "codigo" ) );
                cliente.setNome ( rs.getString ( "nome" ) );
                cliente.setMorada ( rs.getString ( "morada" ) );
                cliente.setTelefone ( rs.getString ( "telefone" ) );
                cliente.setNif ( rs.getString ( "nif" ) );
                cliente.setEmail ( rs.getString ( "email" ) );
//                cliente.setDescontoPercentual(rs.getDouble("desconto_percentual"));
//                cliente.setDataInicio(rs.getDate("data_inicio"));
//                cliente.setDataFim(rs.getDate("data_fim"));

            }
            return cliente;
            //conexao.close();
        }
        catch ( SQLException ex )
        {
            //conexao.close();
            ex.printStackTrace ();

            return null;
        }

    }

    public static List<TbCliente> buscarClientesQueCompraramNoArmazem ( String desigancaoArmazem )
    {
        Query query = UtilDao.getEntityManager1 ().createQuery ( "SELECT DISTINCT a.codigoCliente FROM TbVenda  a WHERE a.idArmazemFK.designacao = :DESIGNACAO_ARMAZEM " );
        query.setParameter ( "DESIGNACAO_ARMAZEM", desigancaoArmazem );

        List<TbCliente> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        return null;
    }

    public static List<TbCliente> buscarClientesQueCompraramAPrazoNoArmazem ( String desigancaoArmazem, Integer ... tiposDoc )
    {
        System.err.println ( "desigancaoArmazem: "+desigancaoArmazem );
        System.err.println ( "tiposDoc: "+tiposDoc[0] );
        Query query = UtilDao.getEntityManager1 ().createQuery ( "SELECT DISTINCT a.codigoCliente FROM TbVenda  a WHERE a.idArmazemFK.designacao = :DESIGNACAO_ARMAZEM AND a.fkDocumento.pkDocumento IN :LISTA_DE_DOCS" );
        query.setParameter ( "DESIGNACAO_ARMAZEM", desigancaoArmazem );
        query.setParameter ( "LISTA_DE_DOCS", Arrays.asList ( tiposDoc ) );

        List<TbCliente> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        return null;
    }

    public static TbCliente findByNome ( String nome )
    {
        Query query = UtilDao.getEntityManager1 ().createQuery ( "SELECT DISTINCT a FROM TbCliente  a WHERE a.nome = :NOME " );
        query.setParameter ( "NOME", nome );

        List<TbCliente> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }

        return null;
    }
    
    public List<TbVenda> getAllVendasByClientes ( int codigo )
    {

        EntityManager em = getEntityManager ();
//        Query query = em.createQuery ( "SELECT s FROM TbVenda s WHERE s.codigoCliente.codigo = :codigo AND s.credito = 'true' AND s.statusEliminado = 'false'  ORDER BY s.codigo" )
        Query query = em.createQuery ( "SELECT s FROM TbVenda s WHERE s.codigoCliente.codigo = :codigo AND s.fkDocumento.pkDocumento = 2 AND s.statusEliminado = 'false'  ORDER BY s.codigo" )
                .setParameter ( "codigo", codigo );
        List<TbVenda> lista = query.getResultList ();

        if (  ! lista.isEmpty () )
        {
            return lista;
        }
        return null;

    }

}
