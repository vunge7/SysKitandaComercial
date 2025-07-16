/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbItemPermissao;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import entity.TbPermissao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemPermissaoJpaController implements Serializable
{

    public TbItemPermissaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemPermissao tbItemPermissao )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario idUsuario = tbItemPermissao.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario = em.getReference( idUsuario.getClass(), idUsuario.getCodigo() );
                tbItemPermissao.setIdUsuario( idUsuario );
            }
            TbPermissao idPermissao = tbItemPermissao.getIdPermissao();
            if ( idPermissao != null )
            {
                idPermissao = em.getReference( idPermissao.getClass(), idPermissao.getIdPermissao() );
                tbItemPermissao.setIdPermissao( idPermissao );
            }
            em.persist( tbItemPermissao );
            if ( idUsuario != null )
            {
                idUsuario.getTbItemPermissaoList().add( tbItemPermissao );
                idUsuario = em.merge( idUsuario );
            }
            if ( idPermissao != null )
            {
                idPermissao.getTbItemPermissaoList().add( tbItemPermissao );
                idPermissao = em.merge( idPermissao );
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

    public void edit( TbItemPermissao tbItemPermissao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemPermissao persistentTbItemPermissao = em.find( TbItemPermissao.class, tbItemPermissao.getIdItemPermissao() );
            TbUsuario idUsuarioOld = persistentTbItemPermissao.getIdUsuario();
            TbUsuario idUsuarioNew = tbItemPermissao.getIdUsuario();
            TbPermissao idPermissaoOld = persistentTbItemPermissao.getIdPermissao();
            TbPermissao idPermissaoNew = tbItemPermissao.getIdPermissao();
            if ( idUsuarioNew != null )
            {
                idUsuarioNew = em.getReference( idUsuarioNew.getClass(), idUsuarioNew.getCodigo() );
                tbItemPermissao.setIdUsuario( idUsuarioNew );
            }
            if ( idPermissaoNew != null )
            {
                idPermissaoNew = em.getReference( idPermissaoNew.getClass(), idPermissaoNew.getIdPermissao() );
                tbItemPermissao.setIdPermissao( idPermissaoNew );
            }
            tbItemPermissao = em.merge( tbItemPermissao );
            if ( idUsuarioOld != null && !idUsuarioOld.equals( idUsuarioNew ) )
            {
                idUsuarioOld.getTbItemPermissaoList().remove( tbItemPermissao );
                idUsuarioOld = em.merge( idUsuarioOld );
            }
            if ( idUsuarioNew != null && !idUsuarioNew.equals( idUsuarioOld ) )
            {
                idUsuarioNew.getTbItemPermissaoList().add( tbItemPermissao );
                idUsuarioNew = em.merge( idUsuarioNew );
            }
            if ( idPermissaoOld != null && !idPermissaoOld.equals( idPermissaoNew ) )
            {
                idPermissaoOld.getTbItemPermissaoList().remove( tbItemPermissao );
                idPermissaoOld = em.merge( idPermissaoOld );
            }
            if ( idPermissaoNew != null && !idPermissaoNew.equals( idPermissaoOld ) )
            {
                idPermissaoNew.getTbItemPermissaoList().add( tbItemPermissao );
                idPermissaoNew = em.merge( idPermissaoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Long id = tbItemPermissao.getIdItemPermissao();
                if ( findTbItemPermissao( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemPermissao with id " + id + " no longer exists." );
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
            TbItemPermissao tbItemPermissao;
            try
            {
                tbItemPermissao = em.getReference( TbItemPermissao.class, id );
                tbItemPermissao.getIdItemPermissao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemPermissao with id " + id + " no longer exists.", enfe );
            }
            TbUsuario idUsuario = tbItemPermissao.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario.getTbItemPermissaoList().remove( tbItemPermissao );
                idUsuario = em.merge( idUsuario );
            }
            TbPermissao idPermissao = tbItemPermissao.getIdPermissao();
            if ( idPermissao != null )
            {
                idPermissao.getTbItemPermissaoList().remove( tbItemPermissao );
                idPermissao = em.merge( idPermissao );
            }
            em.remove( tbItemPermissao );
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

    public List<TbItemPermissao> findTbItemPermissaoEntities()
    {
        return findTbItemPermissaoEntities( true, -1, -1 );
    }

    public List<TbItemPermissao> findTbItemPermissaoEntities( int maxResults, int firstResult )
    {
        return findTbItemPermissaoEntities( false, maxResults, firstResult );
    }

    private List<TbItemPermissao> findTbItemPermissaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemPermissao.class ) );
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

    public TbItemPermissao findTbItemPermissao( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemPermissao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemPermissaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemPermissao> rt = cq.from( TbItemPermissao.class );
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
