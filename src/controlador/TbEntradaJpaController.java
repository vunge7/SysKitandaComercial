/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import entity.TbProduto;
import entity.TbArmazem;
import entity.TbEntrada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbEntradaJpaController implements Serializable
{

    public TbEntradaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEntrada tbEntrada )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario idUsuario = tbEntrada.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario = em.getReference( idUsuario.getClass(), idUsuario.getCodigo() );
                tbEntrada.setIdUsuario( idUsuario );
            }
            TbProduto idProduto = tbEntrada.getIdProduto();
            if ( idProduto != null )
            {
                idProduto = em.getReference( idProduto.getClass(), idProduto.getCodigo() );
                tbEntrada.setIdProduto( idProduto );
            }
            TbArmazem idArmazemFK = tbEntrada.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                tbEntrada.setIdArmazemFK( idArmazemFK );
            }
            em.persist( tbEntrada );
            if ( idUsuario != null )
            {
                idUsuario.getTbEntradaList().add( tbEntrada );
                idUsuario = em.merge( idUsuario );
            }
            if ( idProduto != null )
            {
                idProduto.getTbEntradaList().add( tbEntrada );
                idProduto = em.merge( idProduto );
            }
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbEntradaList().add( tbEntrada );
                idArmazemFK = em.merge( idArmazemFK );
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

    public void edit( TbEntrada tbEntrada ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEntrada persistentTbEntrada = em.find( TbEntrada.class, tbEntrada.getIdEntrada() );
            TbUsuario idUsuarioOld = persistentTbEntrada.getIdUsuario();
            TbUsuario idUsuarioNew = tbEntrada.getIdUsuario();
            TbProduto idProdutoOld = persistentTbEntrada.getIdProduto();
            TbProduto idProdutoNew = tbEntrada.getIdProduto();
            TbArmazem idArmazemFKOld = persistentTbEntrada.getIdArmazemFK();
            TbArmazem idArmazemFKNew = tbEntrada.getIdArmazemFK();
            if ( idUsuarioNew != null )
            {
                idUsuarioNew = em.getReference( idUsuarioNew.getClass(), idUsuarioNew.getCodigo() );
                tbEntrada.setIdUsuario( idUsuarioNew );
            }
            if ( idProdutoNew != null )
            {
                idProdutoNew = em.getReference( idProdutoNew.getClass(), idProdutoNew.getCodigo() );
                tbEntrada.setIdProduto( idProdutoNew );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                tbEntrada.setIdArmazemFK( idArmazemFKNew );
            }
            tbEntrada = em.merge( tbEntrada );
            if ( idUsuarioOld != null && !idUsuarioOld.equals( idUsuarioNew ) )
            {
                idUsuarioOld.getTbEntradaList().remove( tbEntrada );
                idUsuarioOld = em.merge( idUsuarioOld );
            }
            if ( idUsuarioNew != null && !idUsuarioNew.equals( idUsuarioOld ) )
            {
                idUsuarioNew.getTbEntradaList().add( tbEntrada );
                idUsuarioNew = em.merge( idUsuarioNew );
            }
            if ( idProdutoOld != null && !idProdutoOld.equals( idProdutoNew ) )
            {
                idProdutoOld.getTbEntradaList().remove( tbEntrada );
                idProdutoOld = em.merge( idProdutoOld );
            }
            if ( idProdutoNew != null && !idProdutoNew.equals( idProdutoOld ) )
            {
                idProdutoNew.getTbEntradaList().add( tbEntrada );
                idProdutoNew = em.merge( idProdutoNew );
            }
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getTbEntradaList().remove( tbEntrada );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getTbEntradaList().add( tbEntrada );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbEntrada.getIdEntrada();
                if ( findTbEntrada( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEntrada with id " + id + " no longer exists." );
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
            TbEntrada tbEntrada;
            try
            {
                tbEntrada = em.getReference( TbEntrada.class, id );
                tbEntrada.getIdEntrada();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEntrada with id " + id + " no longer exists.", enfe );
            }
            TbUsuario idUsuario = tbEntrada.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario.getTbEntradaList().remove( tbEntrada );
                idUsuario = em.merge( idUsuario );
            }
            TbProduto idProduto = tbEntrada.getIdProduto();
            if ( idProduto != null )
            {
                idProduto.getTbEntradaList().remove( tbEntrada );
                idProduto = em.merge( idProduto );
            }
            TbArmazem idArmazemFK = tbEntrada.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbEntradaList().remove( tbEntrada );
                idArmazemFK = em.merge( idArmazemFK );
            }
            em.remove( tbEntrada );
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

    public List<TbEntrada> findTbEntradaEntities()
    {
        return findTbEntradaEntities( true, -1, -1 );
    }

    public List<TbEntrada> findTbEntradaEntities( int maxResults, int firstResult )
    {
        return findTbEntradaEntities( false, maxResults, firstResult );
    }

    private List<TbEntrada> findTbEntradaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEntrada.class ) );
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

    public TbEntrada findTbEntrada( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEntrada.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEntradaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEntrada> rt = cq.from( TbEntrada.class );
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
