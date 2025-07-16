/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.LevantamentoBancario;
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
public class LevantamentoBancarioJpaController implements Serializable
{

    public LevantamentoBancarioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( LevantamentoBancario levantamentoBancario )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco fkBanco = levantamentoBancario.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco = em.getReference( fkBanco.getClass(), fkBanco.getIdBanco() );
                levantamentoBancario.setFkBanco( fkBanco );
            }
            TbUsuario fkUsuario = levantamentoBancario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                levantamentoBancario.setFkUsuario( fkUsuario );
            }
            em.persist( levantamentoBancario );
            if ( fkBanco != null )
            {
                fkBanco.getLevantamentoBancarioList().add( levantamentoBancario );
                fkBanco = em.merge( fkBanco );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getLevantamentoBancarioList().add( levantamentoBancario );
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

    public void edit( LevantamentoBancario levantamentoBancario ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            LevantamentoBancario persistentLevantamentoBancario = em.find( LevantamentoBancario.class, levantamentoBancario.getPkLevantamento() );
            TbBanco fkBancoOld = persistentLevantamentoBancario.getFkBanco();
            TbBanco fkBancoNew = levantamentoBancario.getFkBanco();
            TbUsuario fkUsuarioOld = persistentLevantamentoBancario.getFkUsuario();
            TbUsuario fkUsuarioNew = levantamentoBancario.getFkUsuario();
            if ( fkBancoNew != null )
            {
                fkBancoNew = em.getReference( fkBancoNew.getClass(), fkBancoNew.getIdBanco() );
                levantamentoBancario.setFkBanco( fkBancoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                levantamentoBancario.setFkUsuario( fkUsuarioNew );
            }
            levantamentoBancario = em.merge( levantamentoBancario );
            if ( fkBancoOld != null && !fkBancoOld.equals( fkBancoNew ) )
            {
                fkBancoOld.getLevantamentoBancarioList().remove( levantamentoBancario );
                fkBancoOld = em.merge( fkBancoOld );
            }
            if ( fkBancoNew != null && !fkBancoNew.equals( fkBancoOld ) )
            {
                fkBancoNew.getLevantamentoBancarioList().add( levantamentoBancario );
                fkBancoNew = em.merge( fkBancoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getLevantamentoBancarioList().remove( levantamentoBancario );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getLevantamentoBancarioList().add( levantamentoBancario );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = levantamentoBancario.getPkLevantamento();
                if ( findLevantamentoBancario( id ) == null )
                {
                    throw new NonexistentEntityException( "The levantamentoBancario with id " + id + " no longer exists." );
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
            LevantamentoBancario levantamentoBancario;
            try
            {
                levantamentoBancario = em.getReference( LevantamentoBancario.class, id );
                levantamentoBancario.getPkLevantamento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The levantamentoBancario with id " + id + " no longer exists.", enfe );
            }
            TbBanco fkBanco = levantamentoBancario.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco.getLevantamentoBancarioList().remove( levantamentoBancario );
                fkBanco = em.merge( fkBanco );
            }
            TbUsuario fkUsuario = levantamentoBancario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getLevantamentoBancarioList().remove( levantamentoBancario );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( levantamentoBancario );
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

    public List<LevantamentoBancario> findLevantamentoBancarioEntities()
    {
        return findLevantamentoBancarioEntities( true, -1, -1 );
    }

    public List<LevantamentoBancario> findLevantamentoBancarioEntities( int maxResults, int firstResult )
    {
        return findLevantamentoBancarioEntities( false, maxResults, firstResult );
    }

    private List<LevantamentoBancario> findLevantamentoBancarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( LevantamentoBancario.class ) );
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

    public LevantamentoBancario findLevantamentoBancario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( LevantamentoBancario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getLevantamentoBancarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LevantamentoBancario> rt = cq.from( LevantamentoBancario.class );
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
