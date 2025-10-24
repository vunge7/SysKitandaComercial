/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbItemProForma;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbProForma;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemProFormaJpaController implements Serializable
{

    public TbItemProFormaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemProForma tbItemProForma )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPreco fkPreco = tbItemProForma.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco = em.getReference( fkPreco.getClass(), fkPreco.getPkPreco() );
                tbItemProForma.setFkPreco( fkPreco );
            }
            TbProduto fkProduto = tbItemProForma.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                tbItemProForma.setFkProduto( fkProduto );
            }
            TbProForma fkProForma = tbItemProForma.getFkProForma();
            if ( fkProForma != null )
            {
                fkProForma = em.getReference( fkProForma.getClass(), fkProForma.getPkProForma() );
                tbItemProForma.setFkProForma( fkProForma );
            }
            em.persist( tbItemProForma );
            if ( fkPreco != null )
            {
                fkPreco.getTbItemProFormaList().add( tbItemProForma );
                fkPreco = em.merge( fkPreco );
            }
            if ( fkProduto != null )
            {
                fkProduto.getTbItemProFormaList().add( tbItemProForma );
                fkProduto = em.merge( fkProduto );
            }
            if ( fkProForma != null )
            {
                fkProForma.getTbItemProFormaList().add( tbItemProForma );
                fkProForma = em.merge( fkProForma );
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

    public void edit( TbItemProForma tbItemProForma ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemProForma persistentTbItemProForma = em.find( TbItemProForma.class, tbItemProForma.getPkItemProForma() );
            TbPreco fkPrecoOld = persistentTbItemProForma.getFkPreco();
            TbPreco fkPrecoNew = tbItemProForma.getFkPreco();
            TbProduto fkProdutoOld = persistentTbItemProForma.getFkProduto();
            TbProduto fkProdutoNew = tbItemProForma.getFkProduto();
            TbProForma fkProFormaOld = persistentTbItemProForma.getFkProForma();
            TbProForma fkProFormaNew = tbItemProForma.getFkProForma();
            if ( fkPrecoNew != null )
            {
                fkPrecoNew = em.getReference( fkPrecoNew.getClass(), fkPrecoNew.getPkPreco() );
                tbItemProForma.setFkPreco( fkPrecoNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                tbItemProForma.setFkProduto( fkProdutoNew );
            }
            if ( fkProFormaNew != null )
            {
                fkProFormaNew = em.getReference( fkProFormaNew.getClass(), fkProFormaNew.getPkProForma() );
                tbItemProForma.setFkProForma( fkProFormaNew );
            }
            tbItemProForma = em.merge( tbItemProForma );
            if ( fkPrecoOld != null && !fkPrecoOld.equals( fkPrecoNew ) )
            {
                fkPrecoOld.getTbItemProFormaList().remove( tbItemProForma );
                fkPrecoOld = em.merge( fkPrecoOld );
            }
            if ( fkPrecoNew != null && !fkPrecoNew.equals( fkPrecoOld ) )
            {
                fkPrecoNew.getTbItemProFormaList().add( tbItemProForma );
                fkPrecoNew = em.merge( fkPrecoNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getTbItemProFormaList().remove( tbItemProForma );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getTbItemProFormaList().add( tbItemProForma );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            if ( fkProFormaOld != null && !fkProFormaOld.equals( fkProFormaNew ) )
            {
                fkProFormaOld.getTbItemProFormaList().remove( tbItemProForma );
                fkProFormaOld = em.merge( fkProFormaOld );
            }
            if ( fkProFormaNew != null && !fkProFormaNew.equals( fkProFormaOld ) )
            {
                fkProFormaNew.getTbItemProFormaList().add( tbItemProForma );
                fkProFormaNew = em.merge( fkProFormaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemProForma.getPkItemProForma();
                if ( findTbItemProForma( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemProForma with id " + id + " no longer exists." );
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
            TbItemProForma tbItemProForma;
            try
            {
                tbItemProForma = em.getReference( TbItemProForma.class, id );
                tbItemProForma.getPkItemProForma();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemProForma with id " + id + " no longer exists.", enfe );
            }
            TbPreco fkPreco = tbItemProForma.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco.getTbItemProFormaList().remove( tbItemProForma );
                fkPreco = em.merge( fkPreco );
            }
            TbProduto fkProduto = tbItemProForma.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getTbItemProFormaList().remove( tbItemProForma );
                fkProduto = em.merge( fkProduto );
            }
            TbProForma fkProForma = tbItemProForma.getFkProForma();
            if ( fkProForma != null )
            {
                fkProForma.getTbItemProFormaList().remove( tbItemProForma );
                fkProForma = em.merge( fkProForma );
            }
            em.remove( tbItemProForma );
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

    public List<TbItemProForma> findTbItemProFormaEntities()
    {
        return findTbItemProFormaEntities( true, -1, -1 );
    }

    public List<TbItemProForma> findTbItemProFormaEntities( int maxResults, int firstResult )
    {
        return findTbItemProFormaEntities( false, maxResults, firstResult );
    }

    private List<TbItemProForma> findTbItemProFormaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemProForma.class ) );
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

    public TbItemProForma findTbItemProForma( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemProForma.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemProFormaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemProForma> rt = cq.from( TbItemProForma.class );
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
