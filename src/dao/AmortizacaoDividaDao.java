/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.AmortizacaoDividaJpaController;
import controlador.TbArmazemJpaController;
import entity.*;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;

/**
 *
 * @author Dallas
 */
public class AmortizacaoDividaDao extends AmortizacaoDividaJpaController
{

    public AmortizacaoDividaDao()
    {
        this( UtilDao.emf );
    }

    public AmortizacaoDividaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbArmazem> getAllArmazem()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem  a" );

        List<TbArmazem> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbArmazem> getArmazens()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbArmazem a" );

        List<TbArmazem> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public Vector<String> buscaTodos1()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a  " );
        Vector<String> result = (Vector) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public TbArmazem getArmazemByDescricao( String descricao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbArmazem  a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List<TbArmazem> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;
    }

//         public List <String> buscaTodos () 
//     {         
//            EntityManager em = getEntityManager1();
//            Query query = em.createQuery ("SELECT c.descrisao FROM TbBanco c");
//            List<String>  lista=  query.getResultList();
//            
//            lista.add(0, "--Seleccione--");
//            return lista;
//    }
    public Vector<String> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a  " );
        Vector<String> result = (Vector) query.getResultList();
        //result.add(0, "--Seleccione--");
        return result;
    }

    public boolean exist_descricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbArmazem e WHERE e.designacao = :designacao " )
                .setParameter( "designacao", designacao );

        List<TbArmazem> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }

        return false;

    }

    public Object[] buscaDaVenda( String codigoDaFactura )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.idArmazemFK.designacao FROM TbVenda  a WHERE a.codFact = :FACTURA" )
                .setParameter( "FACTURA", codigoDaFactura );

        List<String> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.toArray();
        }
        return null;
    }

    public static List<TbArmazem> buscarArmazensComVenda()
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a.idArmazemFK FROM TbVenda  a " );

        List<TbArmazem> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public static List<TbArmazem> buscarArmazensComVendaAPrazo( Integer... tiposDoc )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a.idArmazemFK FROM TbVenda  a WHERE a.fkDocumento.pkDocumento IN :LISTA_DE_DOCS" );
        query.setParameter( "LISTA_DE_DOCS", Arrays.asList( tiposDoc ) );
        List<TbArmazem> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public static TbArmazem findByDesigncao( String designacao )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a.idArmazemFK FROM TbVenda  a WHERE a.idArmazemFK.designacao = :DESIGNACAO_ARMAZEM " );
        query.setParameter( "DESIGNACAO_ARMAZEM", designacao );

        List<TbArmazem> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return null;
    }

    public static List<AmortizacaoDivida> findByRefDoc( String refDoc )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT a FROM AmortizacaoDivida  a WHERE a.fkVenda.codFact = :REF_DOC " );
        query.setParameter( "REF_DOC", refDoc );

        List<AmortizacaoDivida> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public static Double findTotalAmortizadoByRefDoc( String refDoc )
    {
        TbVenda tbVenda = VendaDao.findByRefCod( refDoc );

        String fkVenda = Objects.isNull( tbVenda ) ? "0" : String.valueOf( tbVenda.getCodigo() );
        Query query = UtilDao.getEntityManager1().createNativeQuery( String.format( "select sum(valor_entregue-troco) as soma from amortizacao_divida where fk_venda like %s group by fk_venda", fkVenda ) );
        query.setParameter( "REF_DOC", refDoc );

        List<Double> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0.0;
    }

    public static boolean eliminarAmortizacaoByCodVenda( int cod_venda, BDConexao conexao )
    {
        String sql = "DELETE FROM amortizacao_divida WHERE fk_venda = " + cod_venda;
        return conexao.executeUpdate( sql );

    }

}
