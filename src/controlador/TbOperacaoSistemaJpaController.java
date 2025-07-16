/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbOperacaoSistema;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbOperacaoSistemaJpaController implements Serializable
{

    public TbOperacaoSistemaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbOperacaoSistema tbOperacaoSistema )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = tbOperacaoSistema.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbOperacaoSistema.setFkUsuario( fkUsuario );
            }
            em.persist( tbOperacaoSistema );
            if ( fkUsuario != null )
            {
                fkUsuario.getTbOperacaoSistemaList().add( tbOperacaoSistema );
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

    public void edit( TbOperacaoSistema tbOperacaoSistema ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbOperacaoSistema persistentTbOperacaoSistema = em.find( TbOperacaoSistema.class, tbOperacaoSistema.getPkOperacaoSistema() );
            TbUsuario fkUsuarioOld = persistentTbOperacaoSistema.getFkUsuario();
            TbUsuario fkUsuarioNew = tbOperacaoSistema.getFkUsuario();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbOperacaoSistema.setFkUsuario( fkUsuarioNew );
            }
            tbOperacaoSistema = em.merge( tbOperacaoSistema );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbOperacaoSistemaList().remove( tbOperacaoSistema );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbOperacaoSistemaList().add( tbOperacaoSistema );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Long id = tbOperacaoSistema.getPkOperacaoSistema();
                if ( findTbOperacaoSistema( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbOperacaoSistema with id " + id + " no longer exists." );
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

    public void destroy( Long id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbOperacaoSistema tbOperacaoSistema;
            try
            {
                tbOperacaoSistema = em.getReference( TbOperacaoSistema.class, id );
                tbOperacaoSistema.getPkOperacaoSistema();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbOperacaoSistema with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = tbOperacaoSistema.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbOperacaoSistemaList().remove( tbOperacaoSistema );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbOperacaoSistema );
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

    public List<TbOperacaoSistema> findTbOperacaoSistemaEntities()
    {
        return findTbOperacaoSistemaEntities( true, -1, -1 );
    }

    public List<TbOperacaoSistema> findTbOperacaoSistemaEntities( int maxResults, int firstResult )
    {
        return findTbOperacaoSistemaEntities( false, maxResults, firstResult );
    }

    private List<TbOperacaoSistema> findTbOperacaoSistemaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbOperacaoSistema.class ) );
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

    public TbOperacaoSistema findTbOperacaoSistema( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbOperacaoSistema.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbOperacaoSistemaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbOperacaoSistema> rt = cq.from( TbOperacaoSistema.class );
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
