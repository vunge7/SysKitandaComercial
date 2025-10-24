/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.Retencao;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ServicoRetencao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class RetencaoJpaController implements Serializable
{

    public RetencaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Retencao retencao )
    {
        if ( retencao.getServicoRetencaoList() == null )
        {
            retencao.setServicoRetencaoList( new ArrayList<ServicoRetencao>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ServicoRetencao> attachedServicoRetencaoList = new ArrayList<ServicoRetencao>();
            for ( ServicoRetencao servicoRetencaoListServicoRetencaoToAttach : retencao.getServicoRetencaoList() )
            {
                servicoRetencaoListServicoRetencaoToAttach = em.getReference( servicoRetencaoListServicoRetencaoToAttach.getClass(), servicoRetencaoListServicoRetencaoToAttach.getPkServicoRetencao() );
                attachedServicoRetencaoList.add( servicoRetencaoListServicoRetencaoToAttach );
            }
            retencao.setServicoRetencaoList( attachedServicoRetencaoList );
            em.persist( retencao );
            for ( ServicoRetencao servicoRetencaoListServicoRetencao : retencao.getServicoRetencaoList() )
            {
                Retencao oldFkRetencaoOfServicoRetencaoListServicoRetencao = servicoRetencaoListServicoRetencao.getFkRetencao();
                servicoRetencaoListServicoRetencao.setFkRetencao( retencao );
                servicoRetencaoListServicoRetencao = em.merge( servicoRetencaoListServicoRetencao );
                if ( oldFkRetencaoOfServicoRetencaoListServicoRetencao != null )
                {
                    oldFkRetencaoOfServicoRetencaoListServicoRetencao.getServicoRetencaoList().remove( servicoRetencaoListServicoRetencao );
                    oldFkRetencaoOfServicoRetencaoListServicoRetencao = em.merge( oldFkRetencaoOfServicoRetencaoListServicoRetencao );
                }
            }
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void edit( Retencao retencao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Retencao persistentRetencao = em.find( Retencao.class, retencao.getPkRetencao() );
            List<ServicoRetencao> servicoRetencaoListOld = persistentRetencao.getServicoRetencaoList();
            List<ServicoRetencao> servicoRetencaoListNew = retencao.getServicoRetencaoList();
            List<ServicoRetencao> attachedServicoRetencaoListNew = new ArrayList<ServicoRetencao>();
            for ( ServicoRetencao servicoRetencaoListNewServicoRetencaoToAttach : servicoRetencaoListNew )
            {
                servicoRetencaoListNewServicoRetencaoToAttach = em.getReference( servicoRetencaoListNewServicoRetencaoToAttach.getClass(), servicoRetencaoListNewServicoRetencaoToAttach.getPkServicoRetencao() );
                attachedServicoRetencaoListNew.add( servicoRetencaoListNewServicoRetencaoToAttach );
            }
            servicoRetencaoListNew = attachedServicoRetencaoListNew;
            retencao.setServicoRetencaoList( servicoRetencaoListNew );
            retencao = em.merge( retencao );
            for ( ServicoRetencao servicoRetencaoListOldServicoRetencao : servicoRetencaoListOld )
            {
                if ( !servicoRetencaoListNew.contains( servicoRetencaoListOldServicoRetencao ) )
                {
                    servicoRetencaoListOldServicoRetencao.setFkRetencao( null );
                    servicoRetencaoListOldServicoRetencao = em.merge( servicoRetencaoListOldServicoRetencao );
                }
            }
            for ( ServicoRetencao servicoRetencaoListNewServicoRetencao : servicoRetencaoListNew )
            {
                if ( !servicoRetencaoListOld.contains( servicoRetencaoListNewServicoRetencao ) )
                {
                    Retencao oldFkRetencaoOfServicoRetencaoListNewServicoRetencao = servicoRetencaoListNewServicoRetencao.getFkRetencao();
                    servicoRetencaoListNewServicoRetencao.setFkRetencao( retencao );
                    servicoRetencaoListNewServicoRetencao = em.merge( servicoRetencaoListNewServicoRetencao );
                    if ( oldFkRetencaoOfServicoRetencaoListNewServicoRetencao != null && !oldFkRetencaoOfServicoRetencaoListNewServicoRetencao.equals( retencao ) )
                    {
                        oldFkRetencaoOfServicoRetencaoListNewServicoRetencao.getServicoRetencaoList().remove( servicoRetencaoListNewServicoRetencao );
                        oldFkRetencaoOfServicoRetencaoListNewServicoRetencao = em.merge( oldFkRetencaoOfServicoRetencaoListNewServicoRetencao );
                    }
                }
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = retencao.getPkRetencao();
                if ( findRetencao( id ) == null )
                {
                    throw new NonexistentEntityException( "The retencao with id " + id + " no longer exists." );
                }
            }
            throw ex;
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void destroy( Integer id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Retencao retencao;
            try
            {
                retencao = em.getReference( Retencao.class, id );
                retencao.getPkRetencao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The retencao with id " + id + " no longer exists.", enfe );
            }
            List<ServicoRetencao> servicoRetencaoList = retencao.getServicoRetencaoList();
            for ( ServicoRetencao servicoRetencaoListServicoRetencao : servicoRetencaoList )
            {
                servicoRetencaoListServicoRetencao.setFkRetencao( null );
                servicoRetencaoListServicoRetencao = em.merge( servicoRetencaoListServicoRetencao );
            }
            em.remove( retencao );
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public List<Retencao> findRetencaoEntities()
    {
        return findRetencaoEntities( true, -1, -1 );
    }

    public List<Retencao> findRetencaoEntities( int maxResults, int firstResult )
    {
        return findRetencaoEntities( false, maxResults, firstResult );
    }

    private List<Retencao> findRetencaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Retencao.class ) );
            Query q = em.createQuery( cq );
            if ( !all )
            {
                q.setMaxResults( maxResults );
                q.setFirstResult( firstResult );
            }
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    public Retencao findRetencao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Retencao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getRetencaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Retencao> rt = cq.from( Retencao.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( ( Long ) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
