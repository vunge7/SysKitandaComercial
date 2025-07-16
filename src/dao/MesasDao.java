/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbMesasJpaController;
import entity.TbMesas;
import entity.TbTipoProduto;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Dallas
 */
public class MesasDao extends TbMesasJpaController
{

    public MesasDao(  )
    {
        super( em );
    }

    public MesasDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> getAllMesas()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM TbMesas  c" );

        Vector<String> result = ( Vector ) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbMesas> buscaTodasMesas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbMesas m" );
        return query.getResultList();
    }

    public List<TbMesas> getAll()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbMesas  c ORDER BY c.designacao ASC" );

        List<TbMesas> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public TbMesas getMesasByDescricao( String descricao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbMesas  c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List<TbMesas> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;
    }

    public boolean comparar( String x )
    {
        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT s FROM TbMesas s WHERE s.designacao = :x" )
                .setParameter( "x", x );

        List<TbMesas> result = query.getResultList();

        return !result.isEmpty();

    }

    public TbTipoProduto getTipoProdutoByDescricao( String descricao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM TipoProduto  a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List<TbTipoProduto> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;

    }

    public Vector<String> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT tp.designacao FROM TbMesas tp  " );
        Vector<String> result = ( Vector ) query.getResultList();
        return result;
    }

    public Vector<TbMesas> buscaTodasMesasProdutos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT tp FROM TbMesas tp WHERE tp.designacao <> 'Serviços'   ORDER BY tp.designacao   ASC" );
        Vector<TbMesas> result = ( Vector ) query.getResultList();
        return result;
    }

    public boolean exist_descricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbMesas e WHERE e.designacao = :designacao " )
                .setParameter( "designacao", designacao );

        List<TbMesas> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }

        return false;

    }

    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbMesas m WHERE m.designacao = :designacao " )
                .setParameter( "designacao", designacao );

        List<TbMesas> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getPkMesas();
        }

        return 0;

    }

}
