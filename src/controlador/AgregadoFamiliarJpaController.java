/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.AgregadoFamiliar;
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
public class AgregadoFamiliarJpaController implements Serializable
{

    public AgregadoFamiliarJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( AgregadoFamiliar agregadoFamiliar )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = agregadoFamiliar.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                agregadoFamiliar.setFkFuncionario( fkFuncionario );
            }
            em.persist( agregadoFamiliar );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getAgregadoFamiliarList().add( agregadoFamiliar );
                fkFuncionario = em.merge( fkFuncionario );
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

    public void edit( AgregadoFamiliar agregadoFamiliar ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AgregadoFamiliar persistentAgregadoFamiliar = em.find( AgregadoFamiliar.class, agregadoFamiliar.getPkAgregadoFamiliar() );
            TbFuncionario fkFuncionarioOld = persistentAgregadoFamiliar.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = agregadoFamiliar.getFkFuncionario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                agregadoFamiliar.setFkFuncionario( fkFuncionarioNew );
            }
            agregadoFamiliar = em.merge( agregadoFamiliar );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getAgregadoFamiliarList().remove( agregadoFamiliar );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getAgregadoFamiliarList().add( agregadoFamiliar );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = agregadoFamiliar.getPkAgregadoFamiliar();
                if ( findAgregadoFamiliar( id ) == null )
                {
                    throw new NonexistentEntityException( "The agregadoFamiliar with id " + id + " no longer exists." );
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
            AgregadoFamiliar agregadoFamiliar;
            try
            {
                agregadoFamiliar = em.getReference( AgregadoFamiliar.class, id );
                agregadoFamiliar.getPkAgregadoFamiliar();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The agregadoFamiliar with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = agregadoFamiliar.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getAgregadoFamiliarList().remove( agregadoFamiliar );
                fkFuncionario = em.merge( fkFuncionario );
            }
            em.remove( agregadoFamiliar );
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

    public List<AgregadoFamiliar> findAgregadoFamiliarEntities()
    {
        return findAgregadoFamiliarEntities( true, -1, -1 );
    }

    public List<AgregadoFamiliar> findAgregadoFamiliarEntities( int maxResults, int firstResult )
    {
        return findAgregadoFamiliarEntities( false, maxResults, firstResult );
    }

    private List<AgregadoFamiliar> findAgregadoFamiliarEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( AgregadoFamiliar.class ) );
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

    public AgregadoFamiliar findAgregadoFamiliar( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( AgregadoFamiliar.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAgregadoFamiliarCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AgregadoFamiliar> rt = cq.from( AgregadoFamiliar.class );
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
