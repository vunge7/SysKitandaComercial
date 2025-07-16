/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.FechoContrato;
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
public class FechoContratoJpaController implements Serializable
{

    public FechoContratoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( FechoContrato fechoContrato )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = fechoContrato.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                fechoContrato.setFkFuncionario( fkFuncionario );
            }
            TbUsuario fkUsuario = fechoContrato.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                fechoContrato.setFkUsuario( fkUsuario );
            }
            em.persist( fechoContrato );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getFechoContratoList().add( fechoContrato );
                fkFuncionario = em.merge( fkFuncionario );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getFechoContratoList().add( fechoContrato );
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

    public void edit( FechoContrato fechoContrato ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FechoContrato persistentFechoContrato = em.find( FechoContrato.class, fechoContrato.getPkFechoContrato() );
            TbFuncionario fkFuncionarioOld = persistentFechoContrato.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = fechoContrato.getFkFuncionario();
            TbUsuario fkUsuarioOld = persistentFechoContrato.getFkUsuario();
            TbUsuario fkUsuarioNew = fechoContrato.getFkUsuario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                fechoContrato.setFkFuncionario( fkFuncionarioNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                fechoContrato.setFkUsuario( fkUsuarioNew );
            }
            fechoContrato = em.merge( fechoContrato );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getFechoContratoList().remove( fechoContrato );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getFechoContratoList().add( fechoContrato );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getFechoContratoList().remove( fechoContrato );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getFechoContratoList().add( fechoContrato );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = fechoContrato.getPkFechoContrato();
                if ( findFechoContrato( id ) == null )
                {
                    throw new NonexistentEntityException( "The fechoContrato with id " + id + " no longer exists." );
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
            FechoContrato fechoContrato;
            try
            {
                fechoContrato = em.getReference( FechoContrato.class, id );
                fechoContrato.getPkFechoContrato();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The fechoContrato with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = fechoContrato.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getFechoContratoList().remove( fechoContrato );
                fkFuncionario = em.merge( fkFuncionario );
            }
            TbUsuario fkUsuario = fechoContrato.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getFechoContratoList().remove( fechoContrato );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( fechoContrato );
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

    public List<FechoContrato> findFechoContratoEntities()
    {
        return findFechoContratoEntities( true, -1, -1 );
    }

    public List<FechoContrato> findFechoContratoEntities( int maxResults, int firstResult )
    {
        return findFechoContratoEntities( false, maxResults, firstResult );
    }

    private List<FechoContrato> findFechoContratoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( FechoContrato.class ) );
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

    public FechoContrato findFechoContrato( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( FechoContrato.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFechoContratoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FechoContrato> rt = cq.from( FechoContrato.class );
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
