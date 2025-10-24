/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.PrevioAviso;
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
public class PrevioAvisoJpaController implements Serializable
{

    public PrevioAvisoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( PrevioAviso previoAviso )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = previoAviso.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                previoAviso.setFkFuncionario( fkFuncionario );
            }
            TbUsuario fkUsuario = previoAviso.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                previoAviso.setFkUsuario( fkUsuario );
            }
            em.persist( previoAviso );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPrevioAvisoList().add( previoAviso );
                fkFuncionario = em.merge( fkFuncionario );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getPrevioAvisoList().add( previoAviso );
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

    public void edit( PrevioAviso previoAviso ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            PrevioAviso persistentPrevioAviso = em.find( PrevioAviso.class, previoAviso.getPkPrevioAviso() );
            TbFuncionario fkFuncionarioOld = persistentPrevioAviso.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = previoAviso.getFkFuncionario();
            TbUsuario fkUsuarioOld = persistentPrevioAviso.getFkUsuario();
            TbUsuario fkUsuarioNew = previoAviso.getFkUsuario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                previoAviso.setFkFuncionario( fkFuncionarioNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                previoAviso.setFkUsuario( fkUsuarioNew );
            }
            previoAviso = em.merge( previoAviso );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getPrevioAvisoList().remove( previoAviso );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getPrevioAvisoList().add( previoAviso );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getPrevioAvisoList().remove( previoAviso );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getPrevioAvisoList().add( previoAviso );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = previoAviso.getPkPrevioAviso();
                if ( findPrevioAviso( id ) == null )
                {
                    throw new NonexistentEntityException( "The previoAviso with id " + id + " no longer exists." );
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
            PrevioAviso previoAviso;
            try
            {
                previoAviso = em.getReference( PrevioAviso.class, id );
                previoAviso.getPkPrevioAviso();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The previoAviso with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = previoAviso.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPrevioAvisoList().remove( previoAviso );
                fkFuncionario = em.merge( fkFuncionario );
            }
            TbUsuario fkUsuario = previoAviso.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getPrevioAvisoList().remove( previoAviso );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( previoAviso );
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

    public List<PrevioAviso> findPrevioAvisoEntities()
    {
        return findPrevioAvisoEntities( true, -1, -1 );
    }

    public List<PrevioAviso> findPrevioAvisoEntities( int maxResults, int firstResult )
    {
        return findPrevioAvisoEntities( false, maxResults, firstResult );
    }

    private List<PrevioAviso> findPrevioAvisoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( PrevioAviso.class ) );
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

    public PrevioAviso findPrevioAviso( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( PrevioAviso.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getPrevioAvisoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrevioAviso> rt = cq.from( PrevioAviso.class );
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
