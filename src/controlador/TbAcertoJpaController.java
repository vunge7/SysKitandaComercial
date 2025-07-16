/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbAcerto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbArmazem;
import entity.TbProduto;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbAcertoJpaController implements Serializable
{

    public TbAcertoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbAcerto tbAcerto )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem idArmazemFK = tbAcerto.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                tbAcerto.setIdArmazemFK( idArmazemFK );
            }
            TbProduto idProduto = tbAcerto.getIdProduto();
            if ( idProduto != null )
            {
                idProduto = em.getReference( idProduto.getClass(), idProduto.getCodigo() );
                tbAcerto.setIdProduto( idProduto );
            }
            TbUsuario idUsuario = tbAcerto.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario = em.getReference( idUsuario.getClass(), idUsuario.getCodigo() );
                tbAcerto.setIdUsuario( idUsuario );
            }
            em.persist( tbAcerto );
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbAcertoList().add( tbAcerto );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( idProduto != null )
            {
                idProduto.getTbAcertoList().add( tbAcerto );
                idProduto = em.merge( idProduto );
            }
            if ( idUsuario != null )
            {
                idUsuario.getTbAcertoList().add( tbAcerto );
                idUsuario = em.merge( idUsuario );
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

    public void edit( TbAcerto tbAcerto ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbAcerto persistentTbAcerto = em.find( TbAcerto.class, tbAcerto.getIdAcerto() );
            TbArmazem idArmazemFKOld = persistentTbAcerto.getIdArmazemFK();
            TbArmazem idArmazemFKNew = tbAcerto.getIdArmazemFK();
            TbProduto idProdutoOld = persistentTbAcerto.getIdProduto();
            TbProduto idProdutoNew = tbAcerto.getIdProduto();
            TbUsuario idUsuarioOld = persistentTbAcerto.getIdUsuario();
            TbUsuario idUsuarioNew = tbAcerto.getIdUsuario();
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                tbAcerto.setIdArmazemFK( idArmazemFKNew );
            }
            if ( idProdutoNew != null )
            {
                idProdutoNew = em.getReference( idProdutoNew.getClass(), idProdutoNew.getCodigo() );
                tbAcerto.setIdProduto( idProdutoNew );
            }
            if ( idUsuarioNew != null )
            {
                idUsuarioNew = em.getReference( idUsuarioNew.getClass(), idUsuarioNew.getCodigo() );
                tbAcerto.setIdUsuario( idUsuarioNew );
            }
            tbAcerto = em.merge( tbAcerto );
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getTbAcertoList().remove( tbAcerto );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getTbAcertoList().add( tbAcerto );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( idProdutoOld != null && !idProdutoOld.equals( idProdutoNew ) )
            {
                idProdutoOld.getTbAcertoList().remove( tbAcerto );
                idProdutoOld = em.merge( idProdutoOld );
            }
            if ( idProdutoNew != null && !idProdutoNew.equals( idProdutoOld ) )
            {
                idProdutoNew.getTbAcertoList().add( tbAcerto );
                idProdutoNew = em.merge( idProdutoNew );
            }
            if ( idUsuarioOld != null && !idUsuarioOld.equals( idUsuarioNew ) )
            {
                idUsuarioOld.getTbAcertoList().remove( tbAcerto );
                idUsuarioOld = em.merge( idUsuarioOld );
            }
            if ( idUsuarioNew != null && !idUsuarioNew.equals( idUsuarioOld ) )
            {
                idUsuarioNew.getTbAcertoList().add( tbAcerto );
                idUsuarioNew = em.merge( idUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbAcerto.getIdAcerto();
                if ( findTbAcerto( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbAcerto with id " + id + " no longer exists." );
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
            TbAcerto tbAcerto;
            try
            {
                tbAcerto = em.getReference( TbAcerto.class, id );
                tbAcerto.getIdAcerto();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbAcerto with id " + id + " no longer exists.", enfe );
            }
            TbArmazem idArmazemFK = tbAcerto.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbAcertoList().remove( tbAcerto );
                idArmazemFK = em.merge( idArmazemFK );
            }
            TbProduto idProduto = tbAcerto.getIdProduto();
            if ( idProduto != null )
            {
                idProduto.getTbAcertoList().remove( tbAcerto );
                idProduto = em.merge( idProduto );
            }
            TbUsuario idUsuario = tbAcerto.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario.getTbAcertoList().remove( tbAcerto );
                idUsuario = em.merge( idUsuario );
            }
            em.remove( tbAcerto );
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

    public List<TbAcerto> findTbAcertoEntities()
    {
        return findTbAcertoEntities( true, -1, -1 );
    }

    public List<TbAcerto> findTbAcertoEntities( int maxResults, int firstResult )
    {
        return findTbAcertoEntities( false, maxResults, firstResult );
    }

    private List<TbAcerto> findTbAcertoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbAcerto.class ) );
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

    public TbAcerto findTbAcerto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbAcerto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbAcertoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbAcerto> rt = cq.from( TbAcerto.class );
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
