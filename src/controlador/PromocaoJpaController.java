/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.Promocao;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class PromocaoJpaController implements Serializable
{

    public PromocaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Promocao promocao )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario fkUsuario = promocao.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                promocao.setFkUsuario( fkUsuario );
            }
            em.persist( promocao );
            if ( fkUsuario != null )
            {
                fkUsuario.getPromocaoList().add( promocao );
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

    public void edit( Promocao promocao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Promocao persistentPromocao = em.find( Promocao.class, promocao.getPkPromocao() );
            TbUsuario fkUsuarioOld = persistentPromocao.getFkUsuario();
            TbUsuario fkUsuarioNew = promocao.getFkUsuario();
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                promocao.setFkUsuario( fkUsuarioNew );
            }
            promocao = em.merge( promocao );
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getPromocaoList().remove( promocao );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getPromocaoList().add( promocao );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = promocao.getPkPromocao();
                if ( findPromocao( id ) == null )
                {
                    throw new NonexistentEntityException( "The promocao with id " + id + " no longer exists." );
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
            Promocao promocao;
            try
            {
                promocao = em.getReference( Promocao.class, id );
                promocao.getPkPromocao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The promocao with id " + id + " no longer exists.", enfe );
            }
            TbUsuario fkUsuario = promocao.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getPromocaoList().remove( promocao );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( promocao );
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

    public List<Promocao> findPromocaoEntities()
    {
        return findPromocaoEntities( true, -1, -1 );
    }

    public List<Promocao> findPromocaoEntities( int maxResults, int firstResult )
    {
        return findPromocaoEntities( false, maxResults, firstResult );
    }

    private List<Promocao> findPromocaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Promocao.class ) );
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

    public Promocao findPromocao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Promocao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getPromocaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Promocao> rt = cq.from( Promocao.class );
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
