/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.SaidasTesouraria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbBanco;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class SaidasTesourariaJpaController implements Serializable
{

    public SaidasTesourariaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( SaidasTesouraria saidasTesouraria )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco fkBanco = saidasTesouraria.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco = em.getReference( fkBanco.getClass(), fkBanco.getIdBanco() );
                saidasTesouraria.setFkBanco( fkBanco );
            }
            TbUsuario fkUsuario = saidasTesouraria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                saidasTesouraria.setFkUsuario( fkUsuario );
            }
            em.persist( saidasTesouraria );
            if ( fkBanco != null )
            {
                fkBanco.getSaidasTesourariaList().add( saidasTesouraria );
                fkBanco = em.merge( fkBanco );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getSaidasTesourariaList().add( saidasTesouraria );
                fkUsuario = em.merge( fkUsuario );
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

    public void edit( SaidasTesouraria saidasTesouraria ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            SaidasTesouraria persistentSaidasTesouraria = em.find( SaidasTesouraria.class, saidasTesouraria.getPkSaidasTesouraria() );
            TbBanco fkBancoOld = persistentSaidasTesouraria.getFkBanco();
            TbBanco fkBancoNew = saidasTesouraria.getFkBanco();
            TbUsuario fkUsuarioOld = persistentSaidasTesouraria.getFkUsuario();
            TbUsuario fkUsuarioNew = saidasTesouraria.getFkUsuario();
            if ( fkBancoNew != null )
            {
                fkBancoNew = em.getReference( fkBancoNew.getClass(), fkBancoNew.getIdBanco() );
                saidasTesouraria.setFkBanco( fkBancoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                saidasTesouraria.setFkUsuario( fkUsuarioNew );
            }
            saidasTesouraria = em.merge( saidasTesouraria );
            if ( fkBancoOld != null && !fkBancoOld.equals( fkBancoNew ) )
            {
                fkBancoOld.getSaidasTesourariaList().remove( saidasTesouraria );
                fkBancoOld = em.merge( fkBancoOld );
            }
            if ( fkBancoNew != null && !fkBancoNew.equals( fkBancoOld ) )
            {
                fkBancoNew.getSaidasTesourariaList().add( saidasTesouraria );
                fkBancoNew = em.merge( fkBancoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getSaidasTesourariaList().remove( saidasTesouraria );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getSaidasTesourariaList().add( saidasTesouraria );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = saidasTesouraria.getPkSaidasTesouraria();
                if ( findSaidasTesouraria( id ) == null )
                {
                    throw new NonexistentEntityException( "The saidasTesouraria with id " + id + " no longer exists." );
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
            SaidasTesouraria saidasTesouraria;
            try
            {
                saidasTesouraria = em.getReference( SaidasTesouraria.class, id );
                saidasTesouraria.getPkSaidasTesouraria();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The saidasTesouraria with id " + id + " no longer exists.", enfe );
            }
            TbBanco fkBanco = saidasTesouraria.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco.getSaidasTesourariaList().remove( saidasTesouraria );
                fkBanco = em.merge( fkBanco );
            }
            TbUsuario fkUsuario = saidasTesouraria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getSaidasTesourariaList().remove( saidasTesouraria );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( saidasTesouraria );
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

    public List<SaidasTesouraria> findSaidasTesourariaEntities()
    {
        return findSaidasTesourariaEntities( true, -1, -1 );
    }

    public List<SaidasTesouraria> findSaidasTesourariaEntities( int maxResults, int firstResult )
    {
        return findSaidasTesourariaEntities( false, maxResults, firstResult );
    }

    private List<SaidasTesouraria> findSaidasTesourariaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( SaidasTesouraria.class ) );
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

    public SaidasTesouraria findSaidasTesouraria( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( SaidasTesouraria.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getSaidasTesourariaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SaidasTesouraria> rt = cq.from( SaidasTesouraria.class );
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
