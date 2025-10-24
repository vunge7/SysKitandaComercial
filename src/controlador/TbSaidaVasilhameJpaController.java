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
import entity.TbArmazem;
import entity.TbSaidaVasilhame;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbSaidaVasilhameJpaController implements Serializable
{

    public TbSaidaVasilhameJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbSaidaVasilhame tbSaidaVasilhame )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem fkArmazem = tbSaidaVasilhame.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem = em.getReference( fkArmazem.getClass(), fkArmazem.getCodigo() );
                tbSaidaVasilhame.setFkArmazem( fkArmazem );
            }
            TbUsuario fkUsuario = tbSaidaVasilhame.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbSaidaVasilhame.setFkUsuario( fkUsuario );
            }
            em.persist( tbSaidaVasilhame );
            if ( fkArmazem != null )
            {
                fkArmazem.getTbSaidaVasilhameList().add( tbSaidaVasilhame );
                fkArmazem = em.merge( fkArmazem );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbSaidaVasilhameList().add( tbSaidaVasilhame );
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

    public void edit( TbSaidaVasilhame tbSaidaVasilhame ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSaidaVasilhame persistentTbSaidaVasilhame = em.find( TbSaidaVasilhame.class, tbSaidaVasilhame.getPkSaidaVasilhame() );
            TbArmazem fkArmazemOld = persistentTbSaidaVasilhame.getFkArmazem();
            TbArmazem fkArmazemNew = tbSaidaVasilhame.getFkArmazem();
            TbUsuario fkUsuarioOld = persistentTbSaidaVasilhame.getFkUsuario();
            TbUsuario fkUsuarioNew = tbSaidaVasilhame.getFkUsuario();
            if ( fkArmazemNew != null )
            {
                fkArmazemNew = em.getReference( fkArmazemNew.getClass(), fkArmazemNew.getCodigo() );
                tbSaidaVasilhame.setFkArmazem( fkArmazemNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbSaidaVasilhame.setFkUsuario( fkUsuarioNew );
            }
            tbSaidaVasilhame = em.merge( tbSaidaVasilhame );
            if ( fkArmazemOld != null && !fkArmazemOld.equals( fkArmazemNew ) )
            {
                fkArmazemOld.getTbSaidaVasilhameList().remove( tbSaidaVasilhame );
                fkArmazemOld = em.merge( fkArmazemOld );
            }
            if ( fkArmazemNew != null && !fkArmazemNew.equals( fkArmazemOld ) )
            {
                fkArmazemNew.getTbSaidaVasilhameList().add( tbSaidaVasilhame );
                fkArmazemNew = em.merge( fkArmazemNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbSaidaVasilhameList().remove( tbSaidaVasilhame );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbSaidaVasilhameList().add( tbSaidaVasilhame );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbSaidaVasilhame.getPkSaidaVasilhame();
                if ( findTbSaidaVasilhame( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbSaidaVasilhame with id " + id + " no longer exists." );
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
            TbSaidaVasilhame tbSaidaVasilhame;
            try
            {
                tbSaidaVasilhame = em.getReference( TbSaidaVasilhame.class, id );
                tbSaidaVasilhame.getPkSaidaVasilhame();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbSaidaVasilhame with id " + id + " no longer exists.", enfe );
            }
            TbArmazem fkArmazem = tbSaidaVasilhame.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem.getTbSaidaVasilhameList().remove( tbSaidaVasilhame );
                fkArmazem = em.merge( fkArmazem );
            }
            TbUsuario fkUsuario = tbSaidaVasilhame.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbSaidaVasilhameList().remove( tbSaidaVasilhame );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbSaidaVasilhame );
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

    public List<TbSaidaVasilhame> findTbSaidaVasilhameEntities()
    {
        return findTbSaidaVasilhameEntities( true, -1, -1 );
    }

    public List<TbSaidaVasilhame> findTbSaidaVasilhameEntities( int maxResults, int firstResult )
    {
        return findTbSaidaVasilhameEntities( false, maxResults, firstResult );
    }

    private List<TbSaidaVasilhame> findTbSaidaVasilhameEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbSaidaVasilhame.class ) );
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

    public TbSaidaVasilhame findTbSaidaVasilhame( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbSaidaVasilhame.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbSaidaVasilhameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbSaidaVasilhame> rt = cq.from( TbSaidaVasilhame.class );
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
