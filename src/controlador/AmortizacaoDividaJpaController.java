/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.AmortizacaoDivida;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import entity.TbVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class AmortizacaoDividaJpaController implements Serializable
{

    public AmortizacaoDividaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( AmortizacaoDivida amortizacaoDivida )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = amortizacaoDivida.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                amortizacaoDivida.setFkUsuario( fkUsuario );
            }
            TbVenda fkVenda = amortizacaoDivida.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda = em.getReference( fkVenda.getClass(), fkVenda.getCodigo() );
                amortizacaoDivida.setFkVenda( fkVenda );
            }
            em.persist( amortizacaoDivida );
            if ( fkUsuario != null )
            {
                fkUsuario.getAmortizacaoDividaList().add( amortizacaoDivida );
                fkUsuario = em.merge( fkUsuario );
            }
            if ( fkVenda != null )
            {
                fkVenda.getAmortizacaoDividaList().add( amortizacaoDivida );
                fkVenda = em.merge( fkVenda );
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

    public void edit( AmortizacaoDivida amortizacaoDivida ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AmortizacaoDivida persistentAmortizacaoDivida = em.find( AmortizacaoDivida.class, amortizacaoDivida.getPkAmortizacaoDivida() );
            TbUsuario fkUsuarioOld = persistentAmortizacaoDivida.getFkUsuario();
            TbUsuario fkUsuarioNew = amortizacaoDivida.getFkUsuario();
            TbVenda fkVendaOld = persistentAmortizacaoDivida.getFkVenda();
            TbVenda fkVendaNew = amortizacaoDivida.getFkVenda();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                amortizacaoDivida.setFkUsuario( fkUsuarioNew );
            }
            if ( fkVendaNew != null )
            {
                fkVendaNew = em.getReference( fkVendaNew.getClass(), fkVendaNew.getCodigo() );
                amortizacaoDivida.setFkVenda( fkVendaNew );
            }
            amortizacaoDivida = em.merge( amortizacaoDivida );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getAmortizacaoDividaList().remove( amortizacaoDivida );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getAmortizacaoDividaList().add( amortizacaoDivida );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            if ( fkVendaOld != null && !fkVendaOld.equals( fkVendaNew ) )
            {
                fkVendaOld.getAmortizacaoDividaList().remove( amortizacaoDivida );
                fkVendaOld = em.merge( fkVendaOld );
            }
            if ( fkVendaNew != null && !fkVendaNew.equals( fkVendaOld ) )
            {
                fkVendaNew.getAmortizacaoDividaList().add( amortizacaoDivida );
                fkVendaNew = em.merge( fkVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = amortizacaoDivida.getPkAmortizacaoDivida();
                if ( findAmortizacaoDivida( id ) == null )
                {
                    throw new NonexistentEntityException( "The amortizacaoDivida with id " + id + " no longer exists." );
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
            AmortizacaoDivida amortizacaoDivida;
            try
            {
                amortizacaoDivida = em.getReference( AmortizacaoDivida.class, id );
                amortizacaoDivida.getPkAmortizacaoDivida();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The amortizacaoDivida with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = amortizacaoDivida.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getAmortizacaoDividaList().remove( amortizacaoDivida );
                fkUsuario = em.merge( fkUsuario );
            }
            TbVenda fkVenda = amortizacaoDivida.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda.getAmortizacaoDividaList().remove( amortizacaoDivida );
                fkVenda = em.merge( fkVenda );
            }
            em.remove( amortizacaoDivida );
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

    public List<AmortizacaoDivida> findAmortizacaoDividaEntities()
    {
        return findAmortizacaoDividaEntities( true, -1, -1 );
    }

    public List<AmortizacaoDivida> findAmortizacaoDividaEntities( int maxResults, int firstResult )
    {
        return findAmortizacaoDividaEntities( false, maxResults, firstResult );
    }

    private List<AmortizacaoDivida> findAmortizacaoDividaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( AmortizacaoDivida.class ) );
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

    public AmortizacaoDivida findAmortizacaoDivida( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( AmortizacaoDivida.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAmortizacaoDividaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AmortizacaoDivida> rt = cq.from( AmortizacaoDivida.class );
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
