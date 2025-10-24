/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbArmazemJpaController;
import entity.TbArmazem;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Dallas
 */
public class ArmazemDao extends TbArmazemJpaController
{

    public ArmazemDao()
    {
        this( UtilDao.emf );
    }

    public ArmazemDao( EntityManagerFactory emf )
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
    
    public List<TbArmazem> getArmazens1()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TbArmazem a WHERE a.codigo > 1 " );

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
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }
    public Vector<String> buscaTodos5()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a  " );
        Vector<String> result = ( Vector ) query.getResultList();
           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodosExceptoEconomato()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo <> 1    " );
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos_1()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo = 1 " );
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos_2()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo = 2  " );
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos_3()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo = 3  " );
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos3()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a ORDER BY a.designacao DESC " );
        Vector<String> result = ( Vector ) query.getResultList();
        //result.add(0, "--Seleccione--");
        return result;
    }
    public Vector<String> buscaTodos_4()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a ORDER BY a.designacao DESC " );
        Vector<String> result = ( Vector ) query.getResultList();
        result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos_5()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo = 4  " );
        Vector<String> result = ( Vector ) query.getResultList();
//           result.add(0, "--Seleccione--");
        return result;
    }

    public Vector<String> buscaTodos2()
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT a.designacao FROM TbArmazem a WHERE a.codigo < 2 " );
        Vector<String> result = ( Vector ) query.getResultList();
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
        Vector<String> result = ( Vector ) query.getResultList();
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
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a.idArmazemFK FROM TbVenda  a WHERE  a.fkDocumento.pkDocumento IN :LISTA_DE_DOCS" );
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

}
