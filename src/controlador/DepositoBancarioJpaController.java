/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.DepositoBancario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbBanco;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class DepositoBancarioJpaController implements Serializable
{

    public DepositoBancarioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( DepositoBancario depositoBancario )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco fkBanco = depositoBancario.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco = em.getReference( fkBanco.getClass(), fkBanco.getIdBanco() );
                depositoBancario.setFkBanco( fkBanco );
            }
            TbUsuario fkUsuario = depositoBancario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                depositoBancario.setFkUsuario( fkUsuario );
            }
            em.persist( depositoBancario );
            if ( fkBanco != null )
            {
                fkBanco.getDepositoBancarioList().add( depositoBancario );
                fkBanco = em.merge( fkBanco );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getDepositoBancarioList().add( depositoBancario );
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

    public void edit( DepositoBancario depositoBancario ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            DepositoBancario persistentDepositoBancario = em.find( DepositoBancario.class, depositoBancario.getPkDeposito() );
            TbBanco fkBancoOld = persistentDepositoBancario.getFkBanco();
            TbBanco fkBancoNew = depositoBancario.getFkBanco();
            TbUsuario fkUsuarioOld = persistentDepositoBancario.getFkUsuario();
            TbUsuario fkUsuarioNew = depositoBancario.getFkUsuario();
            if ( fkBancoNew != null )
            {
                fkBancoNew = em.getReference( fkBancoNew.getClass(), fkBancoNew.getIdBanco() );
                depositoBancario.setFkBanco( fkBancoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                depositoBancario.setFkUsuario( fkUsuarioNew );
            }
            depositoBancario = em.merge( depositoBancario );
            if ( fkBancoOld != null && !fkBancoOld.equals( fkBancoNew ) )
            {
                fkBancoOld.getDepositoBancarioList().remove( depositoBancario );
                fkBancoOld = em.merge( fkBancoOld );
            }
            if ( fkBancoNew != null && !fkBancoNew.equals( fkBancoOld ) )
            {
                fkBancoNew.getDepositoBancarioList().add( depositoBancario );
                fkBancoNew = em.merge( fkBancoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getDepositoBancarioList().remove( depositoBancario );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getDepositoBancarioList().add( depositoBancario );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = depositoBancario.getPkDeposito();
                if ( findDepositoBancario( id ) == null )
                {
                    throw new NonexistentEntityException( "The depositoBancario with id " + id + " no longer exists." );
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
            DepositoBancario depositoBancario;
            try
            {
                depositoBancario = em.getReference( DepositoBancario.class, id );
                depositoBancario.getPkDeposito();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The depositoBancario with id " + id + " no longer exists.", enfe );
            }
            TbBanco fkBanco = depositoBancario.getFkBanco();
            if ( fkBanco != null )
            {
                fkBanco.getDepositoBancarioList().remove( depositoBancario );
                fkBanco = em.merge( fkBanco );
            }
            TbUsuario fkUsuario = depositoBancario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getDepositoBancarioList().remove( depositoBancario );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( depositoBancario );
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

    public List<DepositoBancario> findDepositoBancarioEntities()
    {
        return findDepositoBancarioEntities( true, -1, -1 );
    }

    public List<DepositoBancario> findDepositoBancarioEntities( int maxResults, int firstResult )
    {
        return findDepositoBancarioEntities( false, maxResults, firstResult );
    }

    private List<DepositoBancario> findDepositoBancarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( DepositoBancario.class ) );
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

    public DepositoBancario findDepositoBancario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( DepositoBancario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getDepositoBancarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DepositoBancario> rt = cq.from( DepositoBancario.class );
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
