/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbPais;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProvincia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbPaisJpaController implements Serializable
{

    public TbPaisJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbPais tbPais )
    {
        if ( tbPais.getTbProvinciaList() == null )
        {
            tbPais.setTbProvinciaList( new ArrayList<TbProvincia>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbProvincia> attachedTbProvinciaList = new ArrayList<TbProvincia>();
            for ( TbProvincia tbProvinciaListTbProvinciaToAttach : tbPais.getTbProvinciaList() )
            {
                tbProvinciaListTbProvinciaToAttach = em.getReference( tbProvinciaListTbProvinciaToAttach.getClass(), tbProvinciaListTbProvinciaToAttach.getIdProvincia() );
                attachedTbProvinciaList.add( tbProvinciaListTbProvinciaToAttach );
            }
            tbPais.setTbProvinciaList( attachedTbProvinciaList );
            em.persist( tbPais );
            for ( TbProvincia tbProvinciaListTbProvincia : tbPais.getTbProvinciaList() )
            {
                TbPais oldIdPaisOfTbProvinciaListTbProvincia = tbProvinciaListTbProvincia.getIdPais();
                tbProvinciaListTbProvincia.setIdPais( tbPais );
                tbProvinciaListTbProvincia = em.merge( tbProvinciaListTbProvincia );
                if ( oldIdPaisOfTbProvinciaListTbProvincia != null )
                {
                    oldIdPaisOfTbProvinciaListTbProvincia.getTbProvinciaList().remove( tbProvinciaListTbProvincia );
                    oldIdPaisOfTbProvinciaListTbProvincia = em.merge( oldIdPaisOfTbProvinciaListTbProvincia );
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

    public void edit( TbPais tbPais ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPais persistentTbPais = em.find( TbPais.class, tbPais.getIdPais() );
            List<TbProvincia> tbProvinciaListOld = persistentTbPais.getTbProvinciaList();
            List<TbProvincia> tbProvinciaListNew = tbPais.getTbProvinciaList();
            List<TbProvincia> attachedTbProvinciaListNew = new ArrayList<TbProvincia>();
            for ( TbProvincia tbProvinciaListNewTbProvinciaToAttach : tbProvinciaListNew )
            {
                tbProvinciaListNewTbProvinciaToAttach = em.getReference( tbProvinciaListNewTbProvinciaToAttach.getClass(), tbProvinciaListNewTbProvinciaToAttach.getIdProvincia() );
                attachedTbProvinciaListNew.add( tbProvinciaListNewTbProvinciaToAttach );
            }
            tbProvinciaListNew = attachedTbProvinciaListNew;
            tbPais.setTbProvinciaList( tbProvinciaListNew );
            tbPais = em.merge( tbPais );
            for ( TbProvincia tbProvinciaListOldTbProvincia : tbProvinciaListOld )
            {
                if ( !tbProvinciaListNew.contains( tbProvinciaListOldTbProvincia ) )
                {
                    tbProvinciaListOldTbProvincia.setIdPais( null );
                    tbProvinciaListOldTbProvincia = em.merge( tbProvinciaListOldTbProvincia );
                }
            }
            for ( TbProvincia tbProvinciaListNewTbProvincia : tbProvinciaListNew )
            {
                if ( !tbProvinciaListOld.contains( tbProvinciaListNewTbProvincia ) )
                {
                    TbPais oldIdPaisOfTbProvinciaListNewTbProvincia = tbProvinciaListNewTbProvincia.getIdPais();
                    tbProvinciaListNewTbProvincia.setIdPais( tbPais );
                    tbProvinciaListNewTbProvincia = em.merge( tbProvinciaListNewTbProvincia );
                    if ( oldIdPaisOfTbProvinciaListNewTbProvincia != null && !oldIdPaisOfTbProvinciaListNewTbProvincia.equals( tbPais ) )
                    {
                        oldIdPaisOfTbProvinciaListNewTbProvincia.getTbProvinciaList().remove( tbProvinciaListNewTbProvincia );
                        oldIdPaisOfTbProvinciaListNewTbProvincia = em.merge( oldIdPaisOfTbProvinciaListNewTbProvincia );
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
                Integer id = tbPais.getIdPais();
                if ( findTbPais( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbPais with id " + id + " no longer exists." );
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
            TbPais tbPais;
            try
            {
                tbPais = em.getReference( TbPais.class, id );
                tbPais.getIdPais();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbPais with id " + id + " no longer exists.", enfe );
            }
            List<TbProvincia> tbProvinciaList = tbPais.getTbProvinciaList();
            for ( TbProvincia tbProvinciaListTbProvincia : tbProvinciaList )
            {
                tbProvinciaListTbProvincia.setIdPais( null );
                tbProvinciaListTbProvincia = em.merge( tbProvinciaListTbProvincia );
            }
            em.remove( tbPais );
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

    public List<TbPais> findTbPaisEntities()
    {
        return findTbPaisEntities( true, -1, -1 );
    }

    public List<TbPais> findTbPaisEntities( int maxResults, int firstResult )
    {
        return findTbPaisEntities( false, maxResults, firstResult );
    }

    private List<TbPais> findTbPaisEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbPais.class ) );
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

    public TbPais findTbPais( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbPais.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbPaisCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPais> rt = cq.from( TbPais.class );
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
