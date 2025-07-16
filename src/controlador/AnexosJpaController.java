/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.Anexos;
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
public class AnexosJpaController implements Serializable
{

    public AnexosJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Anexos anexos )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = anexos.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                anexos.setFkFuncionario( fkFuncionario );
            }
            TbUsuario fkUsuario = anexos.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                anexos.setFkUsuario( fkUsuario );
            }
            em.persist( anexos );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getAnexosList().add( anexos );
                fkFuncionario = em.merge( fkFuncionario );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getAnexosList().add( anexos );
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

    public void edit( Anexos anexos ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Anexos persistentAnexos = em.find( Anexos.class, anexos.getPkAnexos() );
            TbFuncionario fkFuncionarioOld = persistentAnexos.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = anexos.getFkFuncionario();
            TbUsuario fkUsuarioOld = persistentAnexos.getFkUsuario();
            TbUsuario fkUsuarioNew = anexos.getFkUsuario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                anexos.setFkFuncionario( fkFuncionarioNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                anexos.setFkUsuario( fkUsuarioNew );
            }
            anexos = em.merge( anexos );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getAnexosList().remove( anexos );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getAnexosList().add( anexos );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getAnexosList().remove( anexos );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getAnexosList().add( anexos );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = anexos.getPkAnexos();
                if ( findAnexos( id ) == null )
                {
                    throw new NonexistentEntityException( "The anexos with id " + id + " no longer exists." );
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
            Anexos anexos;
            try
            {
                anexos = em.getReference( Anexos.class, id );
                anexos.getPkAnexos();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The anexos with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = anexos.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getAnexosList().remove( anexos );
                fkFuncionario = em.merge( fkFuncionario );
            }
            TbUsuario fkUsuario = anexos.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getAnexosList().remove( anexos );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( anexos );
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

    public List<Anexos> findAnexosEntities()
    {
        return findAnexosEntities( true, -1, -1 );
    }

    public List<Anexos> findAnexosEntities( int maxResults, int firstResult )
    {
        return findAnexosEntities( false, maxResults, firstResult );
    }

    private List<Anexos> findAnexosEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Anexos.class ) );
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

    public Anexos findAnexos( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Anexos.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAnexosCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anexos> rt = cq.from( Anexos.class );
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
