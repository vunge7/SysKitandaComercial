/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.PedidoFeria;
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
public class PedidoFeriaJpaController implements Serializable
{

    public PedidoFeriaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( PedidoFeria pedidoFeria )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario fkFuncionario = pedidoFeria.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                pedidoFeria.setFkFuncionario( fkFuncionario );
            }
            TbUsuario fkUsuario = pedidoFeria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                pedidoFeria.setFkUsuario( fkUsuario );
            }
            em.persist( pedidoFeria );
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPedidoFeriaList().add( pedidoFeria );
                fkFuncionario = em.merge( fkFuncionario );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getPedidoFeriaList().add( pedidoFeria );
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

    public void edit( PedidoFeria pedidoFeria ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoFeria persistentPedidoFeria = em.find( PedidoFeria.class, pedidoFeria.getPkPedidoFeria() );
            TbFuncionario fkFuncionarioOld = persistentPedidoFeria.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = pedidoFeria.getFkFuncionario();
            TbUsuario fkUsuarioOld = persistentPedidoFeria.getFkUsuario();
            TbUsuario fkUsuarioNew = pedidoFeria.getFkUsuario();
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                pedidoFeria.setFkFuncionario( fkFuncionarioNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                pedidoFeria.setFkUsuario( fkUsuarioNew );
            }
            pedidoFeria = em.merge( pedidoFeria );
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getPedidoFeriaList().remove( pedidoFeria );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getPedidoFeriaList().add( pedidoFeria );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getPedidoFeriaList().remove( pedidoFeria );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getPedidoFeriaList().add( pedidoFeria );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = pedidoFeria.getPkPedidoFeria();
                if ( findPedidoFeria( id ) == null )
                {
                    throw new NonexistentEntityException( "The pedidoFeria with id " + id + " no longer exists." );
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
            PedidoFeria pedidoFeria;
            try
            {
                pedidoFeria = em.getReference( PedidoFeria.class, id );
                pedidoFeria.getPkPedidoFeria();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The pedidoFeria with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario fkFuncionario = pedidoFeria.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getPedidoFeriaList().remove( pedidoFeria );
                fkFuncionario = em.merge( fkFuncionario );
            }
            TbUsuario fkUsuario = pedidoFeria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getPedidoFeriaList().remove( pedidoFeria );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( pedidoFeria );
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

    public List<PedidoFeria> findPedidoFeriaEntities()
    {
        return findPedidoFeriaEntities( true, -1, -1 );
    }

    public List<PedidoFeria> findPedidoFeriaEntities( int maxResults, int firstResult )
    {
        return findPedidoFeriaEntities( false, maxResults, firstResult );
    }

    private List<PedidoFeria> findPedidoFeriaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( PedidoFeria.class ) );
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

    public PedidoFeria findPedidoFeria( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( PedidoFeria.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getPedidoFeriaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PedidoFeria> rt = cq.from( PedidoFeria.class );
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
