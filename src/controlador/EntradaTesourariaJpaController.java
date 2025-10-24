/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.EntradaTesouraria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import entity.TbBanco;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class EntradaTesourariaJpaController implements Serializable
{

    public EntradaTesourariaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( EntradaTesouraria entradaTesouraria )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = entradaTesouraria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                entradaTesouraria.setFkUsuario( fkUsuario );
            }
            TbBanco fkBanco = entradaTesouraria.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco = em.getReference( fkBanco.getClass(), fkBanco.getIdBanco() );
                entradaTesouraria.setFkBanco( fkBanco );
            }
            em.persist( entradaTesouraria );
            if ( fkUsuario != null )
            {
                fkUsuario.getEntradaTesourariaList().add( entradaTesouraria );
                fkUsuario = em.merge( fkUsuario );
            }
            if ( fkBanco != null )
            {
                fkBanco.getEntradaTesourariaList().add( entradaTesouraria );
                fkBanco = em.merge( fkBanco );
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

    public void edit( EntradaTesouraria entradaTesouraria ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            EntradaTesouraria persistentEntradaTesouraria = em.find( EntradaTesouraria.class, entradaTesouraria.getPkEntradaTesouraria() );
            TbUsuario fkUsuarioOld = persistentEntradaTesouraria.getFkUsuario();
            TbUsuario fkUsuarioNew = entradaTesouraria.getFkUsuario();
            TbBanco fkBancoOld = persistentEntradaTesouraria.getFkBanco();
            TbBanco fkBancoNew = entradaTesouraria.getFkBanco();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                entradaTesouraria.setFkUsuario( fkUsuarioNew );
            }
            if ( fkBancoNew != null )
            {
                fkBancoNew = em.getReference( fkBancoNew.getClass(), fkBancoNew.getIdBanco() );
                entradaTesouraria.setFkBanco( fkBancoNew );
            }
            entradaTesouraria = em.merge( entradaTesouraria );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getEntradaTesourariaList().remove( entradaTesouraria );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getEntradaTesourariaList().add( entradaTesouraria );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            if ( fkBancoOld != null && !fkBancoOld.equals( fkBancoNew ) )
            {
                fkBancoOld.getEntradaTesourariaList().remove( entradaTesouraria );
                fkBancoOld = em.merge( fkBancoOld );
            }
            if ( fkBancoNew != null && !fkBancoNew.equals( fkBancoOld ) )
            {
                fkBancoNew.getEntradaTesourariaList().add( entradaTesouraria );
                fkBancoNew = em.merge( fkBancoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = entradaTesouraria.getPkEntradaTesouraria();
                if ( findEntradaTesouraria( id ) == null )
                {
                    throw new NonexistentEntityException( "The entradaTesouraria with id " + id + " no longer exists." );
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
            EntradaTesouraria entradaTesouraria;
            try
            {
                entradaTesouraria = em.getReference( EntradaTesouraria.class, id );
                entradaTesouraria.getPkEntradaTesouraria();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The entradaTesouraria with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = entradaTesouraria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getEntradaTesourariaList().remove( entradaTesouraria );
                fkUsuario = em.merge( fkUsuario );
            }
            TbBanco fkBanco = entradaTesouraria.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco.getEntradaTesourariaList().remove( entradaTesouraria );
                fkBanco = em.merge( fkBanco );
            }
            em.remove( entradaTesouraria );
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

    public List<EntradaTesouraria> findEntradaTesourariaEntities()
    {
        return findEntradaTesourariaEntities( true, -1, -1 );
    }

    public List<EntradaTesouraria> findEntradaTesourariaEntities( int maxResults, int firstResult )
    {
        return findEntradaTesourariaEntities( false, maxResults, firstResult );
    }

    private List<EntradaTesouraria> findEntradaTesourariaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( EntradaTesouraria.class ) );
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

    public EntradaTesouraria findEntradaTesouraria( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( EntradaTesouraria.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getEntradaTesourariaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntradaTesouraria> rt = cq.from( EntradaTesouraria.class );
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
