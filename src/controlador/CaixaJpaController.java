/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Caixa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ItemCaixa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class CaixaJpaController implements Serializable
{

    public CaixaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Caixa caixa )
    {
        if ( caixa.getItemCaixaList() == null )
        {
            caixa.setItemCaixaList( new ArrayList<ItemCaixa>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ItemCaixa> attachedItemCaixaList = new ArrayList<ItemCaixa>();
            for ( ItemCaixa itemCaixaListItemCaixaToAttach : caixa.getItemCaixaList() )
            {
                itemCaixaListItemCaixaToAttach = em.getReference( itemCaixaListItemCaixaToAttach.getClass(), itemCaixaListItemCaixaToAttach.getPkItemCaixa() );
                attachedItemCaixaList.add( itemCaixaListItemCaixaToAttach );
            }
            caixa.setItemCaixaList( attachedItemCaixaList );
            em.persist( caixa );
            for ( ItemCaixa itemCaixaListItemCaixa : caixa.getItemCaixaList() )
            {
                Caixa oldFkCaixaOfItemCaixaListItemCaixa = itemCaixaListItemCaixa.getFkCaixa();
                itemCaixaListItemCaixa.setFkCaixa( caixa );
                itemCaixaListItemCaixa = em.merge( itemCaixaListItemCaixa );
                if ( oldFkCaixaOfItemCaixaListItemCaixa != null )
                {
                    oldFkCaixaOfItemCaixaListItemCaixa.getItemCaixaList().remove( itemCaixaListItemCaixa );
                    oldFkCaixaOfItemCaixaListItemCaixa = em.merge( oldFkCaixaOfItemCaixaListItemCaixa );
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

    public void edit( Caixa caixa ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Caixa persistentCaixa = em.find( Caixa.class, caixa.getPkCaixa() );
            List<ItemCaixa> itemCaixaListOld = persistentCaixa.getItemCaixaList();
            List<ItemCaixa> itemCaixaListNew = caixa.getItemCaixaList();
            List<String> illegalOrphanMessages = null;
            for ( ItemCaixa itemCaixaListOldItemCaixa : itemCaixaListOld )
            {
                if ( !itemCaixaListNew.contains( itemCaixaListOldItemCaixa ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemCaixa " + itemCaixaListOldItemCaixa + " since its fkCaixa field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<ItemCaixa> attachedItemCaixaListNew = new ArrayList<ItemCaixa>();
            for ( ItemCaixa itemCaixaListNewItemCaixaToAttach : itemCaixaListNew )
            {
                itemCaixaListNewItemCaixaToAttach = em.getReference( itemCaixaListNewItemCaixaToAttach.getClass(), itemCaixaListNewItemCaixaToAttach.getPkItemCaixa() );
                attachedItemCaixaListNew.add( itemCaixaListNewItemCaixaToAttach );
            }
            itemCaixaListNew = attachedItemCaixaListNew;
            caixa.setItemCaixaList( itemCaixaListNew );
            caixa = em.merge( caixa );
            for ( ItemCaixa itemCaixaListNewItemCaixa : itemCaixaListNew )
            {
                if ( !itemCaixaListOld.contains( itemCaixaListNewItemCaixa ) )
                {
                    Caixa oldFkCaixaOfItemCaixaListNewItemCaixa = itemCaixaListNewItemCaixa.getFkCaixa();
                    itemCaixaListNewItemCaixa.setFkCaixa( caixa );
                    itemCaixaListNewItemCaixa = em.merge( itemCaixaListNewItemCaixa );
                    if ( oldFkCaixaOfItemCaixaListNewItemCaixa != null && !oldFkCaixaOfItemCaixaListNewItemCaixa.equals( caixa ) )
                    {
                        oldFkCaixaOfItemCaixaListNewItemCaixa.getItemCaixaList().remove( itemCaixaListNewItemCaixa );
                        oldFkCaixaOfItemCaixaListNewItemCaixa = em.merge( oldFkCaixaOfItemCaixaListNewItemCaixa );
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
                Integer id = caixa.getPkCaixa();
                if ( findCaixa( id ) == null )
                {
                    throw new NonexistentEntityException( "The caixa with id " + id + " no longer exists." );
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
            Caixa caixa;
            try
            {
                caixa = em.getReference( Caixa.class, id );
                caixa.getPkCaixa();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The caixa with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemCaixa> itemCaixaListOrphanCheck = caixa.getItemCaixaList();
            for ( ItemCaixa itemCaixaListOrphanCheckItemCaixa : itemCaixaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Caixa (" + caixa + ") cannot be destroyed since the ItemCaixa " + itemCaixaListOrphanCheckItemCaixa + " in its itemCaixaList field has a non-nullable fkCaixa field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( caixa );
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

    public List<Caixa> findCaixaEntities()
    {
        return findCaixaEntities( true, -1, -1 );
    }

    public List<Caixa> findCaixaEntities( int maxResults, int firstResult )
    {
        return findCaixaEntities( false, maxResults, firstResult );
    }

    private List<Caixa> findCaixaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Caixa.class ) );
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

    public Caixa findCaixa( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Caixa.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getCaixaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caixa> rt = cq.from( Caixa.class );
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
