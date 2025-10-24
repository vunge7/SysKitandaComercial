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
import entity.TbBanco;
import entity.TbConta;
import entity.TbFuncionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbContaJpaController implements Serializable
{

    public TbContaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbConta tbConta )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco idBancoFK = tbConta.getIdBancoFK();
            if ( idBancoFK != null )
            {
                idBancoFK = em.getReference( idBancoFK.getClass(), idBancoFK.getIdBanco() );
                tbConta.setIdBancoFK( idBancoFK );
            }
            TbFuncionario idFuncionarioFK = tbConta.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbConta.setIdFuncionarioFK( idFuncionarioFK );
            }
            em.persist( tbConta );
            if ( idBancoFK != null )
            {
                idBancoFK.getTbContaList().add( tbConta );
                idBancoFK = em.merge( idBancoFK );
            }
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbContaList().add( tbConta );
                idFuncionarioFK = em.merge( idFuncionarioFK );
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

    public void edit( TbConta tbConta ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbConta persistentTbConta = em.find( TbConta.class, tbConta.getIdContaPK() );
            TbBanco idBancoFKOld = persistentTbConta.getIdBancoFK();
            TbBanco idBancoFKNew = tbConta.getIdBancoFK();
            TbFuncionario idFuncionarioFKOld = persistentTbConta.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbConta.getIdFuncionarioFK();
            if ( idBancoFKNew != null )
            {
                idBancoFKNew = em.getReference( idBancoFKNew.getClass(), idBancoFKNew.getIdBanco() );
                tbConta.setIdBancoFK( idBancoFKNew );
            }
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbConta.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            tbConta = em.merge( tbConta );
            if ( idBancoFKOld != null && !idBancoFKOld.equals( idBancoFKNew ) )
            {
                idBancoFKOld.getTbContaList().remove( tbConta );
                idBancoFKOld = em.merge( idBancoFKOld );
            }
            if ( idBancoFKNew != null && !idBancoFKNew.equals( idBancoFKOld ) )
            {
                idBancoFKNew.getTbContaList().add( tbConta );
                idBancoFKNew = em.merge( idBancoFKNew );
            }
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbContaList().remove( tbConta );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbContaList().add( tbConta );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbConta.getIdContaPK();
                if ( findTbConta( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbConta with id " + id + " no longer exists." );
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
            TbConta tbConta;
            try
            {
                tbConta = em.getReference( TbConta.class, id );
                tbConta.getIdContaPK();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbConta with id " + id + " no longer exists.", enfe );
            }
            TbBanco idBancoFK = tbConta.getIdBancoFK();
            if ( idBancoFK != null )
            {
                idBancoFK.getTbContaList().remove( tbConta );
                idBancoFK = em.merge( idBancoFK );
            }
            TbFuncionario idFuncionarioFK = tbConta.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbContaList().remove( tbConta );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            em.remove( tbConta );
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

    public List<TbConta> findTbContaEntities()
    {
        return findTbContaEntities( true, -1, -1 );
    }

    public List<TbConta> findTbContaEntities( int maxResults, int firstResult )
    {
        return findTbContaEntities( false, maxResults, firstResult );
    }

    private List<TbConta> findTbContaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbConta.class ) );
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

    public TbConta findTbConta( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbConta.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbContaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbConta> rt = cq.from( TbConta.class );
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
