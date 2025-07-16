/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.FechoPeriodo;
import entity.TbAno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbAnoJpaController implements Serializable
{

    public TbAnoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbAno tbAno )
    {
        if ( tbAno.getFechoPeriodoList() == null )
        {
            tbAno.setFechoPeriodoList( new ArrayList<FechoPeriodo>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FechoPeriodo> attachedFechoPeriodoList = new ArrayList<FechoPeriodo>();
            for ( FechoPeriodo fechoPeriodoListFechoPeriodoToAttach : tbAno.getFechoPeriodoList() )
            {
                fechoPeriodoListFechoPeriodoToAttach = em.getReference( fechoPeriodoListFechoPeriodoToAttach.getClass(), fechoPeriodoListFechoPeriodoToAttach.getPkFechoPeriodo() );
                attachedFechoPeriodoList.add( fechoPeriodoListFechoPeriodoToAttach );
            }
            tbAno.setFechoPeriodoList( attachedFechoPeriodoList );
            em.persist( tbAno );
            for ( FechoPeriodo fechoPeriodoListFechoPeriodo : tbAno.getFechoPeriodoList() )
            {
                TbAno oldFkAnoOfFechoPeriodoListFechoPeriodo = fechoPeriodoListFechoPeriodo.getFkAno();
                fechoPeriodoListFechoPeriodo.setFkAno( tbAno );
                fechoPeriodoListFechoPeriodo = em.merge( fechoPeriodoListFechoPeriodo );
                if ( oldFkAnoOfFechoPeriodoListFechoPeriodo != null )
                {
                    oldFkAnoOfFechoPeriodoListFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListFechoPeriodo );
                    oldFkAnoOfFechoPeriodoListFechoPeriodo = em.merge( oldFkAnoOfFechoPeriodoListFechoPeriodo );
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

    public void edit( TbAno tbAno ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbAno persistentTbAno = em.find( TbAno.class, tbAno.getIdAno() );
            List<FechoPeriodo> fechoPeriodoListOld = persistentTbAno.getFechoPeriodoList();
            List<FechoPeriodo> fechoPeriodoListNew = tbAno.getFechoPeriodoList();
            List<String> illegalOrphanMessages = null;
            for ( FechoPeriodo fechoPeriodoListOldFechoPeriodo : fechoPeriodoListOld )
            {
                if ( !fechoPeriodoListNew.contains( fechoPeriodoListOldFechoPeriodo ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain FechoPeriodo " + fechoPeriodoListOldFechoPeriodo + " since its fkAno field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<FechoPeriodo> attachedFechoPeriodoListNew = new ArrayList<FechoPeriodo>();
            for ( FechoPeriodo fechoPeriodoListNewFechoPeriodoToAttach : fechoPeriodoListNew )
            {
                fechoPeriodoListNewFechoPeriodoToAttach = em.getReference( fechoPeriodoListNewFechoPeriodoToAttach.getClass(), fechoPeriodoListNewFechoPeriodoToAttach.getPkFechoPeriodo() );
                attachedFechoPeriodoListNew.add( fechoPeriodoListNewFechoPeriodoToAttach );
            }
            fechoPeriodoListNew = attachedFechoPeriodoListNew;
            tbAno.setFechoPeriodoList( fechoPeriodoListNew );
            tbAno = em.merge( tbAno );
            for ( FechoPeriodo fechoPeriodoListNewFechoPeriodo : fechoPeriodoListNew )
            {
                if ( !fechoPeriodoListOld.contains( fechoPeriodoListNewFechoPeriodo ) )
                {
                    TbAno oldFkAnoOfFechoPeriodoListNewFechoPeriodo = fechoPeriodoListNewFechoPeriodo.getFkAno();
                    fechoPeriodoListNewFechoPeriodo.setFkAno( tbAno );
                    fechoPeriodoListNewFechoPeriodo = em.merge( fechoPeriodoListNewFechoPeriodo );
                    if ( oldFkAnoOfFechoPeriodoListNewFechoPeriodo != null && !oldFkAnoOfFechoPeriodoListNewFechoPeriodo.equals( tbAno ) )
                    {
                        oldFkAnoOfFechoPeriodoListNewFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListNewFechoPeriodo );
                        oldFkAnoOfFechoPeriodoListNewFechoPeriodo = em.merge( oldFkAnoOfFechoPeriodoListNewFechoPeriodo );
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
                Integer id = tbAno.getIdAno();
                if ( findTbAno( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbAno with id " + id + " no longer exists." );
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

    public void destroy( Integer id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbAno tbAno;
            try
            {
                tbAno = em.getReference( TbAno.class, id );
                tbAno.getIdAno();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbAno with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<FechoPeriodo> fechoPeriodoListOrphanCheck = tbAno.getFechoPeriodoList();
            for ( FechoPeriodo fechoPeriodoListOrphanCheckFechoPeriodo : fechoPeriodoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbAno (" + tbAno + ") cannot be destroyed since the FechoPeriodo " + fechoPeriodoListOrphanCheckFechoPeriodo + " in its fechoPeriodoList field has a non-nullable fkAno field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbAno );
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

    public List<TbAno> findTbAnoEntities()
    {
        return findTbAnoEntities( true, -1, -1 );
    }

    public List<TbAno> findTbAnoEntities( int maxResults, int firstResult )
    {
        return findTbAnoEntities( false, maxResults, firstResult );
    }

    private List<TbAno> findTbAnoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbAno.class ) );
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

    public TbAno findTbAno( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbAno.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbAnoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbAno> rt = cq.from( TbAno.class );
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
