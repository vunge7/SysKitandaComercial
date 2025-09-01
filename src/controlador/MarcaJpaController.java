/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Marca;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Modelo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class MarcaJpaController implements Serializable
{

    public MarcaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Marca marca )
    {
        if ( marca.getModeloList() == null )
        {
            marca.setModeloList( new ArrayList<Modelo>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Modelo> attachedModeloList = new ArrayList<Modelo>();
            for ( Modelo modeloListModeloToAttach : marca.getModeloList() )
            {
                modeloListModeloToAttach = em.getReference( modeloListModeloToAttach.getClass(), modeloListModeloToAttach.getPkModelo() );
                attachedModeloList.add( modeloListModeloToAttach );
            }
            marca.setModeloList( attachedModeloList );
            em.persist( marca );
            for ( Modelo modeloListModelo : marca.getModeloList() )
            {
                Marca oldFkMarcaOfModeloListModelo = modeloListModelo.getFkMarca();
                modeloListModelo.setFkMarca( marca );
                modeloListModelo = em.merge( modeloListModelo );
                if ( oldFkMarcaOfModeloListModelo != null )
                {
                    oldFkMarcaOfModeloListModelo.getModeloList().remove( modeloListModelo );
                    oldFkMarcaOfModeloListModelo = em.merge( oldFkMarcaOfModeloListModelo );
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

    public void edit( Marca marca ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca persistentMarca = em.find( Marca.class, marca.getPkMarca() );
            List<Modelo> modeloListOld = persistentMarca.getModeloList();
            List<Modelo> modeloListNew = marca.getModeloList();
            List<String> illegalOrphanMessages = null;
            for ( Modelo modeloListOldModelo : modeloListOld )
            {
                if ( !modeloListNew.contains( modeloListOldModelo ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Modelo " + modeloListOldModelo + " since its fkMarca field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<Modelo> attachedModeloListNew = new ArrayList<Modelo>();
            for ( Modelo modeloListNewModeloToAttach : modeloListNew )
            {
                modeloListNewModeloToAttach = em.getReference( modeloListNewModeloToAttach.getClass(), modeloListNewModeloToAttach.getPkModelo() );
                attachedModeloListNew.add( modeloListNewModeloToAttach );
            }
            modeloListNew = attachedModeloListNew;
            marca.setModeloList( modeloListNew );
            marca = em.merge( marca );
            for ( Modelo modeloListNewModelo : modeloListNew )
            {
                if ( !modeloListOld.contains( modeloListNewModelo ) )
                {
                    Marca oldFkMarcaOfModeloListNewModelo = modeloListNewModelo.getFkMarca();
                    modeloListNewModelo.setFkMarca( marca );
                    modeloListNewModelo = em.merge( modeloListNewModelo );
                    if ( oldFkMarcaOfModeloListNewModelo != null && !oldFkMarcaOfModeloListNewModelo.equals( marca ) )
                    {
                        oldFkMarcaOfModeloListNewModelo.getModeloList().remove( modeloListNewModelo );
                        oldFkMarcaOfModeloListNewModelo = em.merge( oldFkMarcaOfModeloListNewModelo );
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
                Integer id = marca.getPkMarca();
                if ( findMarca( id ) == null )
                {
                    throw new NonexistentEntityException( "The marca with id " + id + " no longer exists." );
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
            Marca marca;
            try
            {
                marca = em.getReference( Marca.class, id );
                marca.getPkMarca();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The marca with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Modelo> modeloListOrphanCheck = marca.getModeloList();
            for ( Modelo modeloListOrphanCheckModelo : modeloListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Marca (" + marca + ") cannot be destroyed since the Modelo " + modeloListOrphanCheckModelo + " in its modeloList field has a non-nullable fkMarca field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( marca );
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

    public List<Marca> findMarcaEntities()
    {
        return findMarcaEntities( true, -1, -1 );
    }

    public List<Marca> findMarcaEntities( int maxResults, int firstResult )
    {
        return findMarcaEntities( false, maxResults, firstResult );
    }

    private List<Marca> findMarcaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Marca.class ) );
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

    public Marca findMarca( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Marca.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getMarcaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Marca> rt = cq.from( Marca.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
