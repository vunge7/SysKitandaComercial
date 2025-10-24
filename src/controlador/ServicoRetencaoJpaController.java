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
import entity.TbProduto;
import entity.Retencao;
import entity.ServicoRetencao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ServicoRetencaoJpaController implements Serializable
{

    public ServicoRetencaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ServicoRetencao servicoRetencao )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProduto fkProduto = servicoRetencao.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                servicoRetencao.setFkProduto( fkProduto );
            }
            Retencao fkRetencao = servicoRetencao.getFkRetencao();
            if ( fkRetencao != null )
            {
                fkRetencao = em.getReference( fkRetencao.getClass(), fkRetencao.getPkRetencao() );
                servicoRetencao.setFkRetencao( fkRetencao );
            }
            em.persist( servicoRetencao );
            if ( fkProduto != null )
            {
                fkProduto.getServicoRetencaoList().add( servicoRetencao );
                fkProduto = em.merge( fkProduto );
            }
            if ( fkRetencao != null )
            {
                fkRetencao.getServicoRetencaoList().add( servicoRetencao );
                fkRetencao = em.merge( fkRetencao );
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

    public void edit( ServicoRetencao servicoRetencao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ServicoRetencao persistentServicoRetencao = em.find( ServicoRetencao.class, servicoRetencao.getPkServicoRetencao() );
            TbProduto fkProdutoOld = persistentServicoRetencao.getFkProduto();
            TbProduto fkProdutoNew = servicoRetencao.getFkProduto();
            Retencao fkRetencaoOld = persistentServicoRetencao.getFkRetencao();
            Retencao fkRetencaoNew = servicoRetencao.getFkRetencao();
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                servicoRetencao.setFkProduto( fkProdutoNew );
            }
            if ( fkRetencaoNew != null )
            {
                fkRetencaoNew = em.getReference( fkRetencaoNew.getClass(), fkRetencaoNew.getPkRetencao() );
                servicoRetencao.setFkRetencao( fkRetencaoNew );
            }
            servicoRetencao = em.merge( servicoRetencao );
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getServicoRetencaoList().remove( servicoRetencao );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getServicoRetencaoList().add( servicoRetencao );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            if ( fkRetencaoOld != null && !fkRetencaoOld.equals( fkRetencaoNew ) )
            {
                fkRetencaoOld.getServicoRetencaoList().remove( servicoRetencao );
                fkRetencaoOld = em.merge( fkRetencaoOld );
            }
            if ( fkRetencaoNew != null && !fkRetencaoNew.equals( fkRetencaoOld ) )
            {
                fkRetencaoNew.getServicoRetencaoList().add( servicoRetencao );
                fkRetencaoNew = em.merge( fkRetencaoNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = servicoRetencao.getPkServicoRetencao();
                if ( findServicoRetencao( id ) == null )
                {
                    throw new NonexistentEntityException( "The servicoRetencao with id " + id + " no longer exists." );
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
            ServicoRetencao servicoRetencao;
            try
            {
                servicoRetencao = em.getReference( ServicoRetencao.class, id );
                servicoRetencao.getPkServicoRetencao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The servicoRetencao with id " + id + " no longer exists.", enfe );
            }
            TbProduto fkProduto = servicoRetencao.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getServicoRetencaoList().remove( servicoRetencao );
                fkProduto = em.merge( fkProduto );
            }
            Retencao fkRetencao = servicoRetencao.getFkRetencao();
            if ( fkRetencao != null )
            {
                fkRetencao.getServicoRetencaoList().remove( servicoRetencao );
                fkRetencao = em.merge( fkRetencao );
            }
            em.remove( servicoRetencao );
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

    public List<ServicoRetencao> findServicoRetencaoEntities()
    {
        return findServicoRetencaoEntities( true, -1, -1 );
    }

    public List<ServicoRetencao> findServicoRetencaoEntities( int maxResults, int firstResult )
    {
        return findServicoRetencaoEntities( false, maxResults, firstResult );
    }

    private List<ServicoRetencao> findServicoRetencaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ServicoRetencao.class ) );
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

    public ServicoRetencao findServicoRetencao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ServicoRetencao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getServicoRetencaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServicoRetencao> rt = cq.from( ServicoRetencao.class );
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
