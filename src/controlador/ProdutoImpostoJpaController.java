/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Imposto;
import entity.ProdutoImposto;
import entity.TbProduto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ProdutoImpostoJpaController implements Serializable
{

    public ProdutoImpostoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ProdutoImposto produtoImposto )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Imposto fkImposto = produtoImposto.getFkImposto();
            if ( fkImposto != null )
            {
                fkImposto = em.getReference( fkImposto.getClass(), fkImposto.getPkImposto() );
                produtoImposto.setFkImposto( fkImposto );
            }
            TbProduto fkProduto = produtoImposto.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                produtoImposto.setFkProduto( fkProduto );
            }
            em.persist( produtoImposto );
            if ( fkImposto != null )
            {
                fkImposto.getProdutoImpostoList().add( produtoImposto );
                fkImposto = em.merge( fkImposto );
            }
            if ( fkProduto != null )
            {
                fkProduto.getProdutoImpostoList().add( produtoImposto );
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

    public void edit( ProdutoImposto produtoImposto ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutoImposto persistentProdutoImposto = em.find( ProdutoImposto.class, produtoImposto.getPkProdutoImposto() );
            Imposto fkImpostoOld = persistentProdutoImposto.getFkImposto();
            Imposto fkImpostoNew = produtoImposto.getFkImposto();
            TbProduto fkProdutoOld = persistentProdutoImposto.getFkProduto();
            TbProduto fkProdutoNew = produtoImposto.getFkProduto();
            if ( fkImpostoNew != null )
            {
                fkImpostoNew = em.getReference( fkImpostoNew.getClass(), fkImpostoNew.getPkImposto() );
                produtoImposto.setFkImposto( fkImpostoNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                produtoImposto.setFkProduto( fkProdutoNew );
            }
            produtoImposto = em.merge( produtoImposto );
            if ( fkImpostoOld != null && !fkImpostoOld.equals( fkImpostoNew ) )
            {
                fkImpostoOld.getProdutoImpostoList().remove( produtoImposto );
                fkImpostoOld = em.merge( fkImpostoOld );
            }
            if ( fkImpostoNew != null && !fkImpostoNew.equals( fkImpostoOld ) )
            {
                fkImpostoNew.getProdutoImpostoList().add( produtoImposto );
                fkImpostoNew = em.merge( fkImpostoNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getProdutoImpostoList().remove( produtoImposto );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getProdutoImpostoList().add( produtoImposto );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = produtoImposto.getPkProdutoImposto();
                if ( findProdutoImposto( id ) == null )
                {
                    throw new NonexistentEntityException( "The produtoImposto with id " + id + " no longer exists." );
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
            ProdutoImposto produtoImposto;
            try
            {
                produtoImposto = em.getReference( ProdutoImposto.class, id );
                produtoImposto.getPkProdutoImposto();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The produtoImposto with id " + id + " no longer exists.", enfe );
            }
            Imposto fkImposto = produtoImposto.getFkImposto();
            if ( fkImposto != null )
            {
                fkImposto.getProdutoImpostoList().remove( produtoImposto );
                fkImposto = em.merge( fkImposto );
            }
            TbProduto fkProduto = produtoImposto.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getProdutoImpostoList().remove( produtoImposto );
                fkProduto = em.merge( fkProduto );
            }
            em.remove( produtoImposto );
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

    public List<ProdutoImposto> findProdutoImpostoEntities()
    {
        return findProdutoImpostoEntities( true, -1, -1 );
    }

    public List<ProdutoImposto> findProdutoImpostoEntities( int maxResults, int firstResult )
    {
        return findProdutoImpostoEntities( false, maxResults, firstResult );
    }

    private List<ProdutoImposto> findProdutoImpostoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ProdutoImposto.class ) );
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

    public ProdutoImposto findProdutoImposto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ProdutoImposto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getProdutoImpostoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutoImposto> rt = cq.from( ProdutoImposto.class );
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
