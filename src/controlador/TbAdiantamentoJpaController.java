/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.TbAdiantamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbFuncionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbAdiantamentoJpaController implements Serializable
{

    public TbAdiantamentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbAdiantamento tbAdiantamento )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario idFuncionarioFK = tbAdiantamento.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbAdiantamento.setIdFuncionarioFK( idFuncionarioFK );
            }
            em.persist( tbAdiantamento );
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbAdiantamentoList().add( tbAdiantamento );
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

    public void edit( TbAdiantamento tbAdiantamento ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbAdiantamento persistentTbAdiantamento = em.find( TbAdiantamento.class, tbAdiantamento.getIdAdiantamentoFK() );
            TbFuncionario idFuncionarioFKOld = persistentTbAdiantamento.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbAdiantamento.getIdFuncionarioFK();
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbAdiantamento.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            tbAdiantamento = em.merge( tbAdiantamento );
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbAdiantamentoList().remove( tbAdiantamento );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbAdiantamentoList().add( tbAdiantamento );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbAdiantamento.getIdAdiantamentoFK();
                if ( findTbAdiantamento( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbAdiantamento with id " + id + " no longer exists." );
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
            TbAdiantamento tbAdiantamento;
            try
            {
                tbAdiantamento = em.getReference( TbAdiantamento.class, id );
                tbAdiantamento.getIdAdiantamentoFK();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbAdiantamento with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario idFuncionarioFK = tbAdiantamento.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbAdiantamentoList().remove( tbAdiantamento );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            em.remove( tbAdiantamento );
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

    public List<TbAdiantamento> findTbAdiantamentoEntities()
    {
        return findTbAdiantamentoEntities( true, -1, -1 );
    }

    public List<TbAdiantamento> findTbAdiantamentoEntities( int maxResults, int firstResult )
    {
        return findTbAdiantamentoEntities( false, maxResults, firstResult );
    }

    private List<TbAdiantamento> findTbAdiantamentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbAdiantamento.class ) );
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

    public TbAdiantamento findTbAdiantamento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbAdiantamento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbAdiantamentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbAdiantamento> rt = cq.from( TbAdiantamento.class );
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
