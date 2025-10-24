/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbMunicipio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProvincia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbMunicipioJpaController implements Serializable
{

    public TbMunicipioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbMunicipio tbMunicipio )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProvincia idProvincia = tbMunicipio.getIdProvincia();
            if ( idProvincia != null )
            {
                idProvincia = em.getReference( idProvincia.getClass(), idProvincia.getIdProvincia() );
                tbMunicipio.setIdProvincia( idProvincia );
            }
            em.persist( tbMunicipio );
            if ( idProvincia != null )
            {
                idProvincia.getTbMunicipioList().add( tbMunicipio );
                idProvincia = em.merge( idProvincia );
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

    public void edit( TbMunicipio tbMunicipio ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMunicipio persistentTbMunicipio = em.find( TbMunicipio.class, tbMunicipio.getIdMunicipio() );
            TbProvincia idProvinciaOld = persistentTbMunicipio.getIdProvincia();
            TbProvincia idProvinciaNew = tbMunicipio.getIdProvincia();
            if ( idProvinciaNew != null )
            {
                idProvinciaNew = em.getReference( idProvinciaNew.getClass(), idProvinciaNew.getIdProvincia() );
                tbMunicipio.setIdProvincia( idProvinciaNew );
            }
            tbMunicipio = em.merge( tbMunicipio );
            if ( idProvinciaOld != null && !idProvinciaOld.equals( idProvinciaNew ) )
            {
                idProvinciaOld.getTbMunicipioList().remove( tbMunicipio );
                idProvinciaOld = em.merge( idProvinciaOld );
            }
            if ( idProvinciaNew != null && !idProvinciaNew.equals( idProvinciaOld ) )
            {
                idProvinciaNew.getTbMunicipioList().add( tbMunicipio );
                idProvinciaNew = em.merge( idProvinciaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbMunicipio.getIdMunicipio();
                if ( findTbMunicipio( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbMunicipio with id " + id + " no longer exists." );
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
            TbMunicipio tbMunicipio;
            try
            {
                tbMunicipio = em.getReference( TbMunicipio.class, id );
                tbMunicipio.getIdMunicipio();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbMunicipio with id " + id + " no longer exists.", enfe );
            }
            TbProvincia idProvincia = tbMunicipio.getIdProvincia();
            if ( idProvincia != null )
            {
                idProvincia.getTbMunicipioList().remove( tbMunicipio );
                idProvincia = em.merge( idProvincia );
            }
            em.remove( tbMunicipio );
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

    public List<TbMunicipio> findTbMunicipioEntities()
    {
        return findTbMunicipioEntities( true, -1, -1 );
    }

    public List<TbMunicipio> findTbMunicipioEntities( int maxResults, int firstResult )
    {
        return findTbMunicipioEntities( false, maxResults, firstResult );
    }

    private List<TbMunicipio> findTbMunicipioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbMunicipio.class ) );
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

    public TbMunicipio findTbMunicipio( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbMunicipio.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbMunicipioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbMunicipio> rt = cq.from( TbMunicipio.class );
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
