/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.ProdutoIsento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ProdutosMotivosIsensao;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ProdutoIsentoJpaController implements Serializable
{

    public ProdutoIsentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ProdutoIsento produtoIsento )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutosMotivosIsensao fkProdutosMotivosIsensao = produtoIsento.getFkProdutosMotivosIsensao();
            if ( fkProdutosMotivosIsensao != null )
            {
                fkProdutosMotivosIsensao = em.getReference( fkProdutosMotivosIsensao.getClass(), fkProdutosMotivosIsensao.getPkProdutosMotivosIsensao() );
                produtoIsento.setFkProdutosMotivosIsensao( fkProdutosMotivosIsensao );
            }
            TbProduto fkProduto = produtoIsento.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                produtoIsento.setFkProduto( fkProduto );
            }
            em.persist( produtoIsento );
            if ( fkProdutosMotivosIsensao != null )
            {
                fkProdutosMotivosIsensao.getProdutoIsentoList().add( produtoIsento );
                fkProdutosMotivosIsensao = em.merge( fkProdutosMotivosIsensao );
            }
            if ( fkProduto != null )
            {
                fkProduto.getProdutoIsentoList().add( produtoIsento );
                fkProduto = em.merge( fkProduto );
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

    public void edit( ProdutoIsento produtoIsento ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutoIsento persistentProdutoIsento = em.find( ProdutoIsento.class, produtoIsento.getPkProdutoIsento() );
            ProdutosMotivosIsensao fkProdutosMotivosIsensaoOld = persistentProdutoIsento.getFkProdutosMotivosIsensao();
            ProdutosMotivosIsensao fkProdutosMotivosIsensaoNew = produtoIsento.getFkProdutosMotivosIsensao();
            TbProduto fkProdutoOld = persistentProdutoIsento.getFkProduto();
            TbProduto fkProdutoNew = produtoIsento.getFkProduto();
            if ( fkProdutosMotivosIsensaoNew != null )
            {
                fkProdutosMotivosIsensaoNew = em.getReference( fkProdutosMotivosIsensaoNew.getClass(), fkProdutosMotivosIsensaoNew.getPkProdutosMotivosIsensao() );
                produtoIsento.setFkProdutosMotivosIsensao( fkProdutosMotivosIsensaoNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                produtoIsento.setFkProduto( fkProdutoNew );
            }
            produtoIsento = em.merge( produtoIsento );
            if ( fkProdutosMotivosIsensaoOld != null && !fkProdutosMotivosIsensaoOld.equals( fkProdutosMotivosIsensaoNew ) )
            {
                fkProdutosMotivosIsensaoOld.getProdutoIsentoList().remove( produtoIsento );
                fkProdutosMotivosIsensaoOld = em.merge( fkProdutosMotivosIsensaoOld );
            }
            if ( fkProdutosMotivosIsensaoNew != null && !fkProdutosMotivosIsensaoNew.equals( fkProdutosMotivosIsensaoOld ) )
            {
                fkProdutosMotivosIsensaoNew.getProdutoIsentoList().add( produtoIsento );
                fkProdutosMotivosIsensaoNew = em.merge( fkProdutosMotivosIsensaoNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getProdutoIsentoList().remove( produtoIsento );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getProdutoIsentoList().add( produtoIsento );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = produtoIsento.getPkProdutoIsento();
                if ( findProdutoIsento( id ) == null )
                {
                    throw new NonexistentEntityException( "The produtoIsento with id " + id + " no longer exists." );
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
            ProdutoIsento produtoIsento;
            try
            {
                produtoIsento = em.getReference( ProdutoIsento.class, id );
                produtoIsento.getPkProdutoIsento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The produtoIsento with id " + id + " no longer exists.", enfe );
            }
            ProdutosMotivosIsensao fkProdutosMotivosIsensao = produtoIsento.getFkProdutosMotivosIsensao();
            if ( fkProdutosMotivosIsensao != null )
            {
                fkProdutosMotivosIsensao.getProdutoIsentoList().remove( produtoIsento );
                fkProdutosMotivosIsensao = em.merge( fkProdutosMotivosIsensao );
            }
            TbProduto fkProduto = produtoIsento.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getProdutoIsentoList().remove( produtoIsento );
                fkProduto = em.merge( fkProduto );
            }
            em.remove( produtoIsento );
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

    public List<ProdutoIsento> findProdutoIsentoEntities()
    {
        return findProdutoIsentoEntities( true, -1, -1 );
    }

    public List<ProdutoIsento> findProdutoIsentoEntities( int maxResults, int firstResult )
    {
        return findProdutoIsentoEntities( false, maxResults, firstResult );
    }

    private List<ProdutoIsento> findProdutoIsentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ProdutoIsento.class ) );
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

    public ProdutoIsento findProdutoIsento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ProdutoIsento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getProdutoIsentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutoIsento> rt = cq.from( ProdutoIsento.class );
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
