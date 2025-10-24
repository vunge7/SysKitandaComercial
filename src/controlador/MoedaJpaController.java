/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Cambio;
import entity.Moeda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class MoedaJpaController implements Serializable
{

    public MoedaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Moeda moeda )
    {
        if ( moeda.getCambioList() == null )
        {
            moeda.setCambioList( new ArrayList<Cambio>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cambio> attachedCambioList = new ArrayList<Cambio>();
            for ( Cambio cambioListCambioToAttach : moeda.getCambioList() )
            {
                cambioListCambioToAttach = em.getReference( cambioListCambioToAttach.getClass(), cambioListCambioToAttach.getPkCambio() );
                attachedCambioList.add( cambioListCambioToAttach );
            }
            moeda.setCambioList( attachedCambioList );
            em.persist( moeda );
            for ( Cambio cambioListCambio : moeda.getCambioList() )
            {
                Moeda oldFkMoedaOfCambioListCambio = cambioListCambio.getFkMoeda();
                cambioListCambio.setFkMoeda( moeda );
                cambioListCambio = em.merge( cambioListCambio );
                if ( oldFkMoedaOfCambioListCambio != null )
                {
                    oldFkMoedaOfCambioListCambio.getCambioList().remove( cambioListCambio );
                    oldFkMoedaOfCambioListCambio = em.merge( oldFkMoedaOfCambioListCambio );
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

    public void edit( Moeda moeda ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Moeda persistentMoeda = em.find( Moeda.class, moeda.getPkMoeda() );
            List<Cambio> cambioListOld = persistentMoeda.getCambioList();
            List<Cambio> cambioListNew = moeda.getCambioList();
            List<String> illegalOrphanMessages = null;
            for ( Cambio cambioListOldCambio : cambioListOld )
            {
                if ( !cambioListNew.contains( cambioListOldCambio ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Cambio " + cambioListOldCambio + " since its fkMoeda field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<Cambio> attachedCambioListNew = new ArrayList<Cambio>();
            for ( Cambio cambioListNewCambioToAttach : cambioListNew )
            {
                cambioListNewCambioToAttach = em.getReference( cambioListNewCambioToAttach.getClass(), cambioListNewCambioToAttach.getPkCambio() );
                attachedCambioListNew.add( cambioListNewCambioToAttach );
            }
            cambioListNew = attachedCambioListNew;
            moeda.setCambioList( cambioListNew );
            moeda = em.merge( moeda );
            for ( Cambio cambioListNewCambio : cambioListNew )
            {
                if ( !cambioListOld.contains( cambioListNewCambio ) )
                {
                    Moeda oldFkMoedaOfCambioListNewCambio = cambioListNewCambio.getFkMoeda();
                    cambioListNewCambio.setFkMoeda( moeda );
                    cambioListNewCambio = em.merge( cambioListNewCambio );
                    if ( oldFkMoedaOfCambioListNewCambio != null && !oldFkMoedaOfCambioListNewCambio.equals( moeda ) )
                    {
                        oldFkMoedaOfCambioListNewCambio.getCambioList().remove( cambioListNewCambio );
                        oldFkMoedaOfCambioListNewCambio = em.merge( oldFkMoedaOfCambioListNewCambio );
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
                Integer id = moeda.getPkMoeda();
                if ( findMoeda( id ) == null )
                {
                    throw new NonexistentEntityException( "The moeda with id " + id + " no longer exists." );
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
            Moeda moeda;
            try
            {
                moeda = em.getReference( Moeda.class, id );
                moeda.getPkMoeda();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The moeda with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Cambio> cambioListOrphanCheck = moeda.getCambioList();
            for ( Cambio cambioListOrphanCheckCambio : cambioListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Moeda (" + moeda + ") cannot be destroyed since the Cambio " + cambioListOrphanCheckCambio + " in its cambioList field has a non-nullable fkMoeda field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( moeda );
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

    public List<Moeda> findMoedaEntities()
    {
        return findMoedaEntities( true, -1, -1 );
    }

    public List<Moeda> findMoedaEntities( int maxResults, int firstResult )
    {
        return findMoedaEntities( false, maxResults, firstResult );
    }

    private List<Moeda> findMoedaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Moeda.class ) );
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

    public Moeda findMoeda( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Moeda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getMoedaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Moeda> rt = cq.from( Moeda.class );
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
