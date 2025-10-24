/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.TbClientesEncomenda;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbEncomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbClientesEncomendaJpaController implements Serializable
{

    public TbClientesEncomendaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbClientesEncomenda tbClientesEncomenda )
    {
        if ( tbClientesEncomenda.getTbEncomendaList() == null )
        {
            tbClientesEncomenda.setTbEncomendaList( new ArrayList<TbEncomenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbEncomenda> attachedTbEncomendaList = new ArrayList<TbEncomenda>();
            for ( TbEncomenda tbEncomendaListTbEncomendaToAttach : tbClientesEncomenda.getTbEncomendaList() )
            {
                tbEncomendaListTbEncomendaToAttach = em.getReference( tbEncomendaListTbEncomendaToAttach.getClass(), tbEncomendaListTbEncomendaToAttach.getIdEncomenda() );
                attachedTbEncomendaList.add( tbEncomendaListTbEncomendaToAttach );
            }
            tbClientesEncomenda.setTbEncomendaList( attachedTbEncomendaList );
            em.persist( tbClientesEncomenda );
            for ( TbEncomenda tbEncomendaListTbEncomenda : tbClientesEncomenda.getTbEncomendaList() )
            {
                TbClientesEncomenda oldIdClienteOfTbEncomendaListTbEncomenda = tbEncomendaListTbEncomenda.getIdCliente();
                tbEncomendaListTbEncomenda.setIdCliente( tbClientesEncomenda );
                tbEncomendaListTbEncomenda = em.merge( tbEncomendaListTbEncomenda );
                if ( oldIdClienteOfTbEncomendaListTbEncomenda != null )
                {
                    oldIdClienteOfTbEncomendaListTbEncomenda.getTbEncomendaList().remove( tbEncomendaListTbEncomenda );
                    oldIdClienteOfTbEncomendaListTbEncomenda = em.merge( oldIdClienteOfTbEncomendaListTbEncomenda );
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

    public void edit( TbClientesEncomenda tbClientesEncomenda ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbClientesEncomenda persistentTbClientesEncomenda = em.find( TbClientesEncomenda.class, tbClientesEncomenda.getIdCliente() );
            List<TbEncomenda> tbEncomendaListOld = persistentTbClientesEncomenda.getTbEncomendaList();
            List<TbEncomenda> tbEncomendaListNew = tbClientesEncomenda.getTbEncomendaList();
            List<String> illegalOrphanMessages = null;
            for ( TbEncomenda tbEncomendaListOldTbEncomenda : tbEncomendaListOld )
            {
                if ( !tbEncomendaListNew.contains( tbEncomendaListOldTbEncomenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbEncomenda " + tbEncomendaListOldTbEncomenda + " since its idCliente field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbEncomenda> attachedTbEncomendaListNew = new ArrayList<TbEncomenda>();
            for ( TbEncomenda tbEncomendaListNewTbEncomendaToAttach : tbEncomendaListNew )
            {
                tbEncomendaListNewTbEncomendaToAttach = em.getReference( tbEncomendaListNewTbEncomendaToAttach.getClass(), tbEncomendaListNewTbEncomendaToAttach.getIdEncomenda() );
                attachedTbEncomendaListNew.add( tbEncomendaListNewTbEncomendaToAttach );
            }
            tbEncomendaListNew = attachedTbEncomendaListNew;
            tbClientesEncomenda.setTbEncomendaList( tbEncomendaListNew );
            tbClientesEncomenda = em.merge( tbClientesEncomenda );
            for ( TbEncomenda tbEncomendaListNewTbEncomenda : tbEncomendaListNew )
            {
                if ( !tbEncomendaListOld.contains( tbEncomendaListNewTbEncomenda ) )
                {
                    TbClientesEncomenda oldIdClienteOfTbEncomendaListNewTbEncomenda = tbEncomendaListNewTbEncomenda.getIdCliente();
                    tbEncomendaListNewTbEncomenda.setIdCliente( tbClientesEncomenda );
                    tbEncomendaListNewTbEncomenda = em.merge( tbEncomendaListNewTbEncomenda );
                    if ( oldIdClienteOfTbEncomendaListNewTbEncomenda != null && !oldIdClienteOfTbEncomendaListNewTbEncomenda.equals( tbClientesEncomenda ) )
                    {
                        oldIdClienteOfTbEncomendaListNewTbEncomenda.getTbEncomendaList().remove( tbEncomendaListNewTbEncomenda );
                        oldIdClienteOfTbEncomendaListNewTbEncomenda = em.merge( oldIdClienteOfTbEncomendaListNewTbEncomenda );
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
                Long id = tbClientesEncomenda.getIdCliente();
                if ( findTbClientesEncomenda( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbClientesEncomenda with id " + id + " no longer exists." );
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

    public void destroy( Long id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbClientesEncomenda tbClientesEncomenda;
            try
            {
                tbClientesEncomenda = em.getReference( TbClientesEncomenda.class, id );
                tbClientesEncomenda.getIdCliente();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbClientesEncomenda with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbEncomenda> tbEncomendaListOrphanCheck = tbClientesEncomenda.getTbEncomendaList();
            for ( TbEncomenda tbEncomendaListOrphanCheckTbEncomenda : tbEncomendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbClientesEncomenda (" + tbClientesEncomenda + ") cannot be destroyed since the TbEncomenda " + tbEncomendaListOrphanCheckTbEncomenda + " in its tbEncomendaList field has a non-nullable idCliente field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbClientesEncomenda );
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

    public List<TbClientesEncomenda> findTbClientesEncomendaEntities()
    {
        return findTbClientesEncomendaEntities( true, -1, -1 );
    }

    public List<TbClientesEncomenda> findTbClientesEncomendaEntities( int maxResults, int firstResult )
    {
        return findTbClientesEncomendaEntities( false, maxResults, firstResult );
    }

    private List<TbClientesEncomenda> findTbClientesEncomendaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbClientesEncomenda.class ) );
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

    public TbClientesEncomenda findTbClientesEncomenda( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbClientesEncomenda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbClientesEncomendaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbClientesEncomenda> rt = cq.from( TbClientesEncomenda.class );
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
