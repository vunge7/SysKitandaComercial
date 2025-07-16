/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbItemVenda;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemVendaJpaController implements Serializable
{

    public TbItemVendaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemVenda tbItemVenda )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLugares fkLugares = tbItemVenda.getFkLugares();
            if ( fkLugares != null )
            {
                fkLugares = em.getReference( fkLugares.getClass(), fkLugares.getPkLugares() );
                tbItemVenda.setFkLugares( fkLugares );
            }
            TbMesas fkMesas = tbItemVenda.getFkMesas();
            if ( fkMesas != null )
            {
                fkMesas = em.getReference( fkMesas.getClass(), fkMesas.getPkMesas() );
                tbItemVenda.setFkMesas( fkMesas );
            }
            TbPreco fkPreco = tbItemVenda.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco = em.getReference( fkPreco.getClass(), fkPreco.getPkPreco() );
                tbItemVenda.setFkPreco( fkPreco );
            }
            TbProduto codigoProduto = tbItemVenda.getCodigoProduto();
            if ( codigoProduto != null )
            {
                codigoProduto = em.getReference( codigoProduto.getClass(), codigoProduto.getCodigo() );
                tbItemVenda.setCodigoProduto( codigoProduto );
            }
            TbVenda codigoVenda = tbItemVenda.getCodigoVenda();
            if ( codigoVenda != null )
            {
                codigoVenda = em.getReference( codigoVenda.getClass(), codigoVenda.getCodigo() );
                tbItemVenda.setCodigoVenda( codigoVenda );
            }
            em.persist( tbItemVenda );
            if ( fkLugares != null )
            {
                fkLugares.getTbItemVendaList().add( tbItemVenda );
                fkLugares = em.merge( fkLugares );
            }
            if ( fkMesas != null )
            {
                fkMesas.getTbItemVendaList().add( tbItemVenda );
                fkMesas = em.merge( fkMesas );
            }
            if ( fkPreco != null )
            {
                fkPreco.getTbItemVendaList().add( tbItemVenda );
                fkPreco = em.merge( fkPreco );
            }
            if ( codigoProduto != null )
            {
                codigoProduto.getTbItemVendaList().add( tbItemVenda );
                codigoProduto = em.merge( codigoProduto );
            }
            if ( codigoVenda != null )
            {
                codigoVenda.getTbItemVendaList().add( tbItemVenda );
                codigoVenda = em.merge( codigoVenda );
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

    public void edit( TbItemVenda tbItemVenda ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemVenda persistentTbItemVenda = em.find( TbItemVenda.class, tbItemVenda.getCodigo() );
            TbLugares fkLugaresOld = persistentTbItemVenda.getFkLugares();
            TbLugares fkLugaresNew = tbItemVenda.getFkLugares();
            TbMesas fkMesasOld = persistentTbItemVenda.getFkMesas();
            TbMesas fkMesasNew = tbItemVenda.getFkMesas();
            TbPreco fkPrecoOld = persistentTbItemVenda.getFkPreco();
            TbPreco fkPrecoNew = tbItemVenda.getFkPreco();
            TbProduto codigoProdutoOld = persistentTbItemVenda.getCodigoProduto();
            TbProduto codigoProdutoNew = tbItemVenda.getCodigoProduto();
            TbVenda codigoVendaOld = persistentTbItemVenda.getCodigoVenda();
            TbVenda codigoVendaNew = tbItemVenda.getCodigoVenda();
            if ( fkLugaresNew != null )
            {
                fkLugaresNew = em.getReference( fkLugaresNew.getClass(), fkLugaresNew.getPkLugares() );
                tbItemVenda.setFkLugares( fkLugaresNew );
            }
            if ( fkMesasNew != null )
            {
                fkMesasNew = em.getReference( fkMesasNew.getClass(), fkMesasNew.getPkMesas() );
                tbItemVenda.setFkMesas( fkMesasNew );
            }
            if ( fkPrecoNew != null )
            {
                fkPrecoNew = em.getReference( fkPrecoNew.getClass(), fkPrecoNew.getPkPreco() );
                tbItemVenda.setFkPreco( fkPrecoNew );
            }
            if ( codigoProdutoNew != null )
            {
                codigoProdutoNew = em.getReference( codigoProdutoNew.getClass(), codigoProdutoNew.getCodigo() );
                tbItemVenda.setCodigoProduto( codigoProdutoNew );
            }
            if ( codigoVendaNew != null )
            {
                codigoVendaNew = em.getReference( codigoVendaNew.getClass(), codigoVendaNew.getCodigo() );
                tbItemVenda.setCodigoVenda( codigoVendaNew );
            }
            tbItemVenda = em.merge( tbItemVenda );
            if ( fkLugaresOld != null && !fkLugaresOld.equals( fkLugaresNew ) )
            {
                fkLugaresOld.getTbItemVendaList().remove( tbItemVenda );
                fkLugaresOld = em.merge( fkLugaresOld );
            }
            if ( fkLugaresNew != null && !fkLugaresNew.equals( fkLugaresOld ) )
            {
                fkLugaresNew.getTbItemVendaList().add( tbItemVenda );
                fkLugaresNew = em.merge( fkLugaresNew );
            }
            if ( fkMesasOld != null && !fkMesasOld.equals( fkMesasNew ) )
            {
                fkMesasOld.getTbItemVendaList().remove( tbItemVenda );
                fkMesasOld = em.merge( fkMesasOld );
            }
            if ( fkMesasNew != null && !fkMesasNew.equals( fkMesasOld ) )
            {
                fkMesasNew.getTbItemVendaList().add( tbItemVenda );
                fkMesasNew = em.merge( fkMesasNew );
            }
            if ( fkPrecoOld != null && !fkPrecoOld.equals( fkPrecoNew ) )
            {
                fkPrecoOld.getTbItemVendaList().remove( tbItemVenda );
                fkPrecoOld = em.merge( fkPrecoOld );
            }
            if ( fkPrecoNew != null && !fkPrecoNew.equals( fkPrecoOld ) )
            {
                fkPrecoNew.getTbItemVendaList().add( tbItemVenda );
                fkPrecoNew = em.merge( fkPrecoNew );
            }
            if ( codigoProdutoOld != null && !codigoProdutoOld.equals( codigoProdutoNew ) )
            {
                codigoProdutoOld.getTbItemVendaList().remove( tbItemVenda );
                codigoProdutoOld = em.merge( codigoProdutoOld );
            }
            if ( codigoProdutoNew != null && !codigoProdutoNew.equals( codigoProdutoOld ) )
            {
                codigoProdutoNew.getTbItemVendaList().add( tbItemVenda );
                codigoProdutoNew = em.merge( codigoProdutoNew );
            }
            if ( codigoVendaOld != null && !codigoVendaOld.equals( codigoVendaNew ) )
            {
                codigoVendaOld.getTbItemVendaList().remove( tbItemVenda );
                codigoVendaOld = em.merge( codigoVendaOld );
            }
            if ( codigoVendaNew != null && !codigoVendaNew.equals( codigoVendaOld ) )
            {
                codigoVendaNew.getTbItemVendaList().add( tbItemVenda );
                codigoVendaNew = em.merge( codigoVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemVenda.getCodigo();
                if ( findTbItemVenda( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemVenda with id " + id + " no longer exists." );
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
            TbItemVenda tbItemVenda;
            try
            {
                tbItemVenda = em.getReference( TbItemVenda.class, id );
                tbItemVenda.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemVenda with id " + id + " no longer exists.", enfe );
            }
            TbLugares fkLugares = tbItemVenda.getFkLugares();
            if ( fkLugares != null )
            {
                fkLugares.getTbItemVendaList().remove( tbItemVenda );
                fkLugares = em.merge( fkLugares );
            }
            TbMesas fkMesas = tbItemVenda.getFkMesas();
            if ( fkMesas != null )
            {
                fkMesas.getTbItemVendaList().remove( tbItemVenda );
                fkMesas = em.merge( fkMesas );
            }
            TbPreco fkPreco = tbItemVenda.getFkPreco();
            if ( fkPreco != null )
            {
                fkPreco.getTbItemVendaList().remove( tbItemVenda );
                fkPreco = em.merge( fkPreco );
            }
            TbProduto codigoProduto = tbItemVenda.getCodigoProduto();
            if ( codigoProduto != null )
            {
                codigoProduto.getTbItemVendaList().remove( tbItemVenda );
                codigoProduto = em.merge( codigoProduto );
            }
            TbVenda codigoVenda = tbItemVenda.getCodigoVenda();
            if ( codigoVenda != null )
            {
                codigoVenda.getTbItemVendaList().remove( tbItemVenda );
                codigoVenda = em.merge( codigoVenda );
            }
            em.remove( tbItemVenda );
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

    public List<TbItemVenda> findTbItemVendaEntities()
    {
        return findTbItemVendaEntities( true, -1, -1 );
    }

    public List<TbItemVenda> findTbItemVendaEntities( int maxResults, int firstResult )
    {
        return findTbItemVendaEntities( false, maxResults, firstResult );
    }

    private List<TbItemVenda> findTbItemVendaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemVenda.class ) );
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

    public TbItemVenda findTbItemVenda( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemVenda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemVendaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemVenda> rt = cq.from( TbItemVenda.class );
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
