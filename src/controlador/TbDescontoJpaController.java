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
import entity.TbCliente;
import entity.TbDesconto;
import entity.TbProduto;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbDescontoJpaController implements Serializable
{

    public TbDescontoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbDesconto tbDesconto )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbCliente fkCliente = tbDesconto.getFkCliente();
            if ( fkCliente != null )
            {
                fkCliente = em.getReference( fkCliente.getClass(), fkCliente.getCodigo() );
                tbDesconto.setFkCliente( fkCliente );
            }
            TbProduto fkProduto = tbDesconto.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                tbDesconto.setFkProduto( fkProduto );
            }
            TbUsuario fkUsuario = tbDesconto.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbDesconto.setFkUsuario( fkUsuario );
            }
            em.persist( tbDesconto );
            if ( fkCliente != null )
            {
                fkCliente.getTbDescontoList().add( tbDesconto );
                fkCliente = em.merge( fkCliente );
            }
            if ( fkProduto != null )
            {
                fkProduto.getTbDescontoList().add( tbDesconto );
                fkProduto = em.merge( fkProduto );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbDescontoList().add( tbDesconto );
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

    public void edit( TbDesconto tbDesconto ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbDesconto persistentTbDesconto = em.find( TbDesconto.class, tbDesconto.getIdDesconto() );
            TbCliente fkClienteOld = persistentTbDesconto.getFkCliente();
            TbCliente fkClienteNew = tbDesconto.getFkCliente();
            TbProduto fkProdutoOld = persistentTbDesconto.getFkProduto();
            TbProduto fkProdutoNew = tbDesconto.getFkProduto();
            TbUsuario fkUsuarioOld = persistentTbDesconto.getFkUsuario();
            TbUsuario fkUsuarioNew = tbDesconto.getFkUsuario();
            if ( fkClienteNew != null )
            {
                fkClienteNew = em.getReference( fkClienteNew.getClass(), fkClienteNew.getCodigo() );
                tbDesconto.setFkCliente( fkClienteNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                tbDesconto.setFkProduto( fkProdutoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbDesconto.setFkUsuario( fkUsuarioNew );
            }
            tbDesconto = em.merge( tbDesconto );
            if ( fkClienteOld != null && !fkClienteOld.equals( fkClienteNew ) )
            {
                fkClienteOld.getTbDescontoList().remove( tbDesconto );
                fkClienteOld = em.merge( fkClienteOld );
            }
            if ( fkClienteNew != null && !fkClienteNew.equals( fkClienteOld ) )
            {
                fkClienteNew.getTbDescontoList().add( tbDesconto );
                fkClienteNew = em.merge( fkClienteNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getTbDescontoList().remove( tbDesconto );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getTbDescontoList().add( tbDesconto );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbDescontoList().remove( tbDesconto );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbDescontoList().add( tbDesconto );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbDesconto.getIdDesconto();
                if ( findTbDesconto( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbDesconto with id " + id + " no longer exists." );
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
            TbDesconto tbDesconto;
            try
            {
                tbDesconto = em.getReference( TbDesconto.class, id );
                tbDesconto.getIdDesconto();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbDesconto with id " + id + " no longer exists.", enfe );
            }
            TbCliente fkCliente = tbDesconto.getFkCliente();
            if ( fkCliente != null )
            {
                fkCliente.getTbDescontoList().remove( tbDesconto );
                fkCliente = em.merge( fkCliente );
            }
            TbProduto fkProduto = tbDesconto.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getTbDescontoList().remove( tbDesconto );
                fkProduto = em.merge( fkProduto );
            }
            TbUsuario fkUsuario = tbDesconto.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbDescontoList().remove( tbDesconto );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbDesconto );
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

    public List<TbDesconto> findTbDescontoEntities()
    {
        return findTbDescontoEntities( true, -1, -1 );
    }

    public List<TbDesconto> findTbDescontoEntities( int maxResults, int firstResult )
    {
        return findTbDescontoEntities( false, maxResults, firstResult );
    }

    private List<TbDesconto> findTbDescontoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbDesconto.class ) );
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

    public TbDesconto findTbDesconto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbDesconto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbDescontoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbDesconto> rt = cq.from( TbDesconto.class );
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
