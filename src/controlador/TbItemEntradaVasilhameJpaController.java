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
import entity.TbEntradaVasilhame;
import entity.TbItemEntradaVasilhame;
import entity.TbVasilhame;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemEntradaVasilhameJpaController implements Serializable
{

    public TbItemEntradaVasilhameJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemEntradaVasilhame tbItemEntradaVasilhame )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEntradaVasilhame fkEntradaVasilhame = tbItemEntradaVasilhame.getFkEntradaVasilhame();
            if ( fkEntradaVasilhame != null )
            {
                fkEntradaVasilhame = em.getReference( fkEntradaVasilhame.getClass(), fkEntradaVasilhame.getPkEntradaVasilhame() );
                tbItemEntradaVasilhame.setFkEntradaVasilhame( fkEntradaVasilhame );
            }
            TbVasilhame fkVasilhame = tbItemEntradaVasilhame.getFkVasilhame();
            if ( fkVasilhame != null )
            {
                fkVasilhame = em.getReference( fkVasilhame.getClass(), fkVasilhame.getPkVasilhame() );
                tbItemEntradaVasilhame.setFkVasilhame( fkVasilhame );
            }
            em.persist( tbItemEntradaVasilhame );
            if ( fkEntradaVasilhame != null )
            {
                fkEntradaVasilhame.getTbItemEntradaVasilhameList().add( tbItemEntradaVasilhame );
                fkEntradaVasilhame = em.merge( fkEntradaVasilhame );
            }
            if ( fkVasilhame != null )
            {
                fkVasilhame.getTbItemEntradaVasilhameList().add( tbItemEntradaVasilhame );
                fkVasilhame = em.merge( fkVasilhame );
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

    public void edit( TbItemEntradaVasilhame tbItemEntradaVasilhame ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemEntradaVasilhame persistentTbItemEntradaVasilhame = em.find( TbItemEntradaVasilhame.class, tbItemEntradaVasilhame.getPkItemEntradaVasilhame() );
            TbEntradaVasilhame fkEntradaVasilhameOld = persistentTbItemEntradaVasilhame.getFkEntradaVasilhame();
            TbEntradaVasilhame fkEntradaVasilhameNew = tbItemEntradaVasilhame.getFkEntradaVasilhame();
            TbVasilhame fkVasilhameOld = persistentTbItemEntradaVasilhame.getFkVasilhame();
            TbVasilhame fkVasilhameNew = tbItemEntradaVasilhame.getFkVasilhame();
            if ( fkEntradaVasilhameNew != null )
            {
                fkEntradaVasilhameNew = em.getReference( fkEntradaVasilhameNew.getClass(), fkEntradaVasilhameNew.getPkEntradaVasilhame() );
                tbItemEntradaVasilhame.setFkEntradaVasilhame( fkEntradaVasilhameNew );
            }
            if ( fkVasilhameNew != null )
            {
                fkVasilhameNew = em.getReference( fkVasilhameNew.getClass(), fkVasilhameNew.getPkVasilhame() );
                tbItemEntradaVasilhame.setFkVasilhame( fkVasilhameNew );
            }
            tbItemEntradaVasilhame = em.merge( tbItemEntradaVasilhame );
            if ( fkEntradaVasilhameOld != null && !fkEntradaVasilhameOld.equals( fkEntradaVasilhameNew ) )
            {
                fkEntradaVasilhameOld.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhame );
                fkEntradaVasilhameOld = em.merge( fkEntradaVasilhameOld );
            }
            if ( fkEntradaVasilhameNew != null && !fkEntradaVasilhameNew.equals( fkEntradaVasilhameOld ) )
            {
                fkEntradaVasilhameNew.getTbItemEntradaVasilhameList().add( tbItemEntradaVasilhame );
                fkEntradaVasilhameNew = em.merge( fkEntradaVasilhameNew );
            }
            if ( fkVasilhameOld != null && !fkVasilhameOld.equals( fkVasilhameNew ) )
            {
                fkVasilhameOld.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhame );
                fkVasilhameOld = em.merge( fkVasilhameOld );
            }
            if ( fkVasilhameNew != null && !fkVasilhameNew.equals( fkVasilhameOld ) )
            {
                fkVasilhameNew.getTbItemEntradaVasilhameList().add( tbItemEntradaVasilhame );
                fkVasilhameNew = em.merge( fkVasilhameNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemEntradaVasilhame.getPkItemEntradaVasilhame();
                if ( findTbItemEntradaVasilhame( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemEntradaVasilhame with id " + id + " no longer exists." );
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
            TbItemEntradaVasilhame tbItemEntradaVasilhame;
            try
            {
                tbItemEntradaVasilhame = em.getReference( TbItemEntradaVasilhame.class, id );
                tbItemEntradaVasilhame.getPkItemEntradaVasilhame();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemEntradaVasilhame with id " + id + " no longer exists.", enfe );
            }
            TbEntradaVasilhame fkEntradaVasilhame = tbItemEntradaVasilhame.getFkEntradaVasilhame();
            if ( fkEntradaVasilhame != null )
            {
                fkEntradaVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhame );
                fkEntradaVasilhame = em.merge( fkEntradaVasilhame );
            }
            TbVasilhame fkVasilhame = tbItemEntradaVasilhame.getFkVasilhame();
            if ( fkVasilhame != null )
            {
                fkVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhame );
                fkVasilhame = em.merge( fkVasilhame );
            }
            em.remove( tbItemEntradaVasilhame );
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

    public List<TbItemEntradaVasilhame> findTbItemEntradaVasilhameEntities()
    {
        return findTbItemEntradaVasilhameEntities( true, -1, -1 );
    }

    public List<TbItemEntradaVasilhame> findTbItemEntradaVasilhameEntities( int maxResults, int firstResult )
    {
        return findTbItemEntradaVasilhameEntities( false, maxResults, firstResult );
    }

    private List<TbItemEntradaVasilhame> findTbItemEntradaVasilhameEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemEntradaVasilhame.class ) );
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

    public TbItemEntradaVasilhame findTbItemEntradaVasilhame( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemEntradaVasilhame.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemEntradaVasilhameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemEntradaVasilhame> rt = cq.from( TbItemEntradaVasilhame.class );
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
