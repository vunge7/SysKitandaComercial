/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.Amortizacao;
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
public class AmortizacaoJpaController implements Serializable
{

    public AmortizacaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Amortizacao amortizacao )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = amortizacao.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                amortizacao.setFkUsuario( fkUsuario );
            }
            TbVenda fkVenda = amortizacao.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda = em.getReference( fkVenda.getClass(), fkVenda.getCodigo() );
                amortizacao.setFkVenda( fkVenda );
            }
            em.persist( amortizacao );
            if ( fkUsuario != null )
            {
                fkUsuario.getAmortizacaoList().add( amortizacao );
                fkUsuario = em.merge( fkUsuario );
            }
            if ( fkVenda != null )
            {
                fkVenda.getAmortizacaoList().add( amortizacao );
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

    public void edit( Amortizacao amortizacao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Amortizacao persistentAmortizacao = em.find( Amortizacao.class, amortizacao.getPkAmortizacao() );
            TbUsuario fkUsuarioOld = persistentAmortizacao.getFkUsuario();
            TbUsuario fkUsuarioNew = amortizacao.getFkUsuario();
            TbVenda fkVendaOld = persistentAmortizacao.getFkVenda();
            TbVenda fkVendaNew = amortizacao.getFkVenda();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                amortizacao.setFkUsuario( fkUsuarioNew );
            }
            if ( fkVendaNew != null )
            {
                fkVendaNew = em.getReference( fkVendaNew.getClass(), fkVendaNew.getCodigo() );
                amortizacao.setFkVenda( fkVendaNew );
            }
            amortizacao = em.merge( amortizacao );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getAmortizacaoList().remove( amortizacao );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getAmortizacaoList().add( amortizacao );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            if ( fkVendaOld != null && !fkVendaOld.equals( fkVendaNew ) )
            {
                fkVendaOld.getAmortizacaoList().remove( amortizacao );
                fkVendaOld = em.merge( fkVendaOld );
            }
            if ( fkVendaNew != null && !fkVendaNew.equals( fkVendaOld ) )
            {
                fkVendaNew.getAmortizacaoList().add( amortizacao );
                fkVendaNew = em.merge( fkVendaNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = amortizacao.getPkAmortizacao();
                if ( findAmortizacao( id ) == null )
                {
                    throw new NonexistentEntityException( "The amortizacao with id " + id + " no longer exists." );
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
            Amortizacao amortizacao;
            try
            {
                amortizacao = em.getReference( Amortizacao.class, id );
                amortizacao.getPkAmortizacao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The amortizacao with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = amortizacao.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getAmortizacaoList().remove( amortizacao );
                fkUsuario = em.merge( fkUsuario );
            }
            TbVenda fkVenda = amortizacao.getFkVenda();
            if ( fkVenda != null )
            {
                fkVenda.getAmortizacaoList().remove( amortizacao );
                fkVenda = em.merge( fkVenda );
            }
            em.remove( amortizacao );
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

    public List<Amortizacao> findAmortizacaoEntities()
    {
        return findAmortizacaoEntities( true, -1, -1 );
    }

    public List<Amortizacao> findAmortizacaoEntities( int maxResults, int firstResult )
    {
        return findAmortizacaoEntities( false, maxResults, firstResult );
    }

    private List<Amortizacao> findAmortizacaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Amortizacao.class ) );
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

    public Amortizacao findAmortizacao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Amortizacao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAmortizacaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Amortizacao> rt = cq.from( Amortizacao.class );
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
