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
import entity.TbMesRh;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbMesRhJpaController implements Serializable
{

    public TbMesRhJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbMesRh tbMesRh )
    {
        if ( tbMesRh.getFechoPeriodoList() == null )
        {
            tbMesRh.setFechoPeriodoList( new ArrayList<FechoPeriodo>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FechoPeriodo> attachedFechoPeriodoList = new ArrayList<FechoPeriodo>();
            for ( FechoPeriodo fechoPeriodoListFechoPeriodoToAttach : tbMesRh.getFechoPeriodoList() )
            {
                fechoPeriodoListFechoPeriodoToAttach = em.getReference( fechoPeriodoListFechoPeriodoToAttach.getClass(), fechoPeriodoListFechoPeriodoToAttach.getPkFechoPeriodo() );
                attachedFechoPeriodoList.add( fechoPeriodoListFechoPeriodoToAttach );
            }
            tbMesRh.setFechoPeriodoList( attachedFechoPeriodoList );
            em.persist( tbMesRh );
            for ( FechoPeriodo fechoPeriodoListFechoPeriodo : tbMesRh.getFechoPeriodoList() )
            {
                TbMesRh oldFkPeriodoOfFechoPeriodoListFechoPeriodo = fechoPeriodoListFechoPeriodo.getFkPeriodo();
                fechoPeriodoListFechoPeriodo.setFkPeriodo( tbMesRh );
                fechoPeriodoListFechoPeriodo = em.merge( fechoPeriodoListFechoPeriodo );
                if ( oldFkPeriodoOfFechoPeriodoListFechoPeriodo != null )
                {
                    oldFkPeriodoOfFechoPeriodoListFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListFechoPeriodo );
                    oldFkPeriodoOfFechoPeriodoListFechoPeriodo = em.merge( oldFkPeriodoOfFechoPeriodoListFechoPeriodo );
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

    public void edit( TbMesRh tbMesRh ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMesRh persistentTbMesRh = em.find( TbMesRh.class, tbMesRh.getPkMesRh() );
            List<FechoPeriodo> fechoPeriodoListOld = persistentTbMesRh.getFechoPeriodoList();
            List<FechoPeriodo> fechoPeriodoListNew = tbMesRh.getFechoPeriodoList();
            List<String> illegalOrphanMessages = null;
            for ( FechoPeriodo fechoPeriodoListOldFechoPeriodo : fechoPeriodoListOld )
            {
                if ( !fechoPeriodoListNew.contains( fechoPeriodoListOldFechoPeriodo ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain FechoPeriodo " + fechoPeriodoListOldFechoPeriodo + " since its fkPeriodo field is not nullable." );
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
            tbMesRh.setFechoPeriodoList( fechoPeriodoListNew );
            tbMesRh = em.merge( tbMesRh );
            for ( FechoPeriodo fechoPeriodoListNewFechoPeriodo : fechoPeriodoListNew )
            {
                if ( !fechoPeriodoListOld.contains( fechoPeriodoListNewFechoPeriodo ) )
                {
                    TbMesRh oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo = fechoPeriodoListNewFechoPeriodo.getFkPeriodo();
                    fechoPeriodoListNewFechoPeriodo.setFkPeriodo( tbMesRh );
                    fechoPeriodoListNewFechoPeriodo = em.merge( fechoPeriodoListNewFechoPeriodo );
                    if ( oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo != null && !oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo.equals( tbMesRh ) )
                    {
                        oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListNewFechoPeriodo );
                        oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo = em.merge( oldFkPeriodoOfFechoPeriodoListNewFechoPeriodo );
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
                Integer id = tbMesRh.getPkMesRh();
                if ( findTbMesRh( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbMesRh with id " + id + " no longer exists." );
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
            TbMesRh tbMesRh;
            try
            {
                tbMesRh = em.getReference( TbMesRh.class, id );
                tbMesRh.getPkMesRh();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbMesRh with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<FechoPeriodo> fechoPeriodoListOrphanCheck = tbMesRh.getFechoPeriodoList();
            for ( FechoPeriodo fechoPeriodoListOrphanCheckFechoPeriodo : fechoPeriodoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbMesRh (" + tbMesRh + ") cannot be destroyed since the FechoPeriodo " + fechoPeriodoListOrphanCheckFechoPeriodo + " in its fechoPeriodoList field has a non-nullable fkPeriodo field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbMesRh );
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

    public List<TbMesRh> findTbMesRhEntities()
    {
        return findTbMesRhEntities( true, -1, -1 );
    }

    public List<TbMesRh> findTbMesRhEntities( int maxResults, int firstResult )
    {
        return findTbMesRhEntities( false, maxResults, firstResult );
    }

    private List<TbMesRh> findTbMesRhEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbMesRh.class ) );
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

    public TbMesRh findTbMesRh( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbMesRh.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbMesRhCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbMesRh> rt = cq.from( TbMesRh.class );
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
