/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.PagamentoSubsidioFeriaNatal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbFuncionario;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class PagamentoSubsidioFeriaNatalJpaController implements Serializable
{

    public PagamentoSubsidioFeriaNatalJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatal )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = pagamentoSubsidioFeriaNatal.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                pagamentoSubsidioFeriaNatal.setFkFuncionario( fkFuncionario );
            }
            TbUsuario fkUsuario = pagamentoSubsidioFeriaNatal.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                pagamentoSubsidioFeriaNatal.setFkUsuario( fkUsuario );
            }
            em.persist( pagamentoSubsidioFeriaNatal );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPagamentoSubsidioFeriaNatalList().add( pagamentoSubsidioFeriaNatal );
                fkFuncionario = em.merge( fkFuncionario );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getPagamentoSubsidioFeriaNatalList().add( pagamentoSubsidioFeriaNatal );
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

    public void edit( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatal ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            PagamentoSubsidioFeriaNatal persistentPagamentoSubsidioFeriaNatal = em.find( PagamentoSubsidioFeriaNatal.class, pagamentoSubsidioFeriaNatal.getPkPagamentoSubsidioFeriaNatal() );
            TbFuncionario fkFuncionarioOld = persistentPagamentoSubsidioFeriaNatal.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = pagamentoSubsidioFeriaNatal.getFkFuncionario();
            TbUsuario fkUsuarioOld = persistentPagamentoSubsidioFeriaNatal.getFkUsuario();
            TbUsuario fkUsuarioNew = pagamentoSubsidioFeriaNatal.getFkUsuario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                pagamentoSubsidioFeriaNatal.setFkFuncionario( fkFuncionarioNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                pagamentoSubsidioFeriaNatal.setFkUsuario( fkUsuarioNew );
            }
            pagamentoSubsidioFeriaNatal = em.merge( pagamentoSubsidioFeriaNatal );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatal );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getPagamentoSubsidioFeriaNatalList().add( pagamentoSubsidioFeriaNatal );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatal );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getPagamentoSubsidioFeriaNatalList().add( pagamentoSubsidioFeriaNatal );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = pagamentoSubsidioFeriaNatal.getPkPagamentoSubsidioFeriaNatal();
                if ( findPagamentoSubsidioFeriaNatal( id ) == null )
                {
                    throw new NonexistentEntityException( "The pagamentoSubsidioFeriaNatal with id " + id + " no longer exists." );
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
            PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatal;
            try
            {
                pagamentoSubsidioFeriaNatal = em.getReference( PagamentoSubsidioFeriaNatal.class, id );
                pagamentoSubsidioFeriaNatal.getPkPagamentoSubsidioFeriaNatal();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The pagamentoSubsidioFeriaNatal with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = pagamentoSubsidioFeriaNatal.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatal );
                fkFuncionario = em.merge( fkFuncionario );
            }
            TbUsuario fkUsuario = pagamentoSubsidioFeriaNatal.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatal );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( pagamentoSubsidioFeriaNatal );
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

    public List<PagamentoSubsidioFeriaNatal> findPagamentoSubsidioFeriaNatalEntities()
    {
        return findPagamentoSubsidioFeriaNatalEntities( true, -1, -1 );
    }

    public List<PagamentoSubsidioFeriaNatal> findPagamentoSubsidioFeriaNatalEntities( int maxResults, int firstResult )
    {
        return findPagamentoSubsidioFeriaNatalEntities( false, maxResults, firstResult );
    }

    private List<PagamentoSubsidioFeriaNatal> findPagamentoSubsidioFeriaNatalEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( PagamentoSubsidioFeriaNatal.class ) );
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

    public PagamentoSubsidioFeriaNatal findPagamentoSubsidioFeriaNatal( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( PagamentoSubsidioFeriaNatal.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getPagamentoSubsidioFeriaNatalCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PagamentoSubsidioFeriaNatal> rt = cq.from( PagamentoSubsidioFeriaNatal.class );
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
