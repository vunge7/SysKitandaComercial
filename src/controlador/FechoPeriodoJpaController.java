/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.FechoPeriodo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbAno;
import entity.TbMesRh;
import entity.TbUsuario;
import entity.ReciboRh;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class FechoPeriodoJpaController implements Serializable
{

    public FechoPeriodoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( FechoPeriodo fechoPeriodo )
    {
        if ( fechoPeriodo.getReciboRhList() == null )
        {
            fechoPeriodo.setReciboRhList( new ArrayList<ReciboRh>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbAno fkAno = fechoPeriodo.getFkAno();
            if ( fkAno != null )
            {
                fkAno = em.getReference( fkAno.getClass(), fkAno.getIdAno() );
                fechoPeriodo.setFkAno( fkAno );
            }
            TbMesRh fkPeriodo = fechoPeriodo.getFkPeriodo();
            if ( fkPeriodo != null )
            {
                fkPeriodo = em.getReference( fkPeriodo.getClass(), fkPeriodo.getPkMesRh() );
                fechoPeriodo.setFkPeriodo( fkPeriodo );
            }
            TbUsuario fkUsuario = fechoPeriodo.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                fechoPeriodo.setFkUsuario( fkUsuario );
            }
            List<ReciboRh> attachedReciboRhList = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListReciboRhToAttach : fechoPeriodo.getReciboRhList() )
            {
                reciboRhListReciboRhToAttach = em.getReference( reciboRhListReciboRhToAttach.getClass(), reciboRhListReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhList.add( reciboRhListReciboRhToAttach );
            }
            fechoPeriodo.setReciboRhList( attachedReciboRhList );
            em.persist( fechoPeriodo );
            if ( fkAno != null )
            {
                fkAno.getFechoPeriodoList().add( fechoPeriodo );
                fkAno = em.merge( fkAno );
            }
            if ( fkPeriodo != null )
            {
                fkPeriodo.getFechoPeriodoList().add( fechoPeriodo );
                fkPeriodo = em.merge( fkPeriodo );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getFechoPeriodoList().add( fechoPeriodo );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( ReciboRh reciboRhListReciboRh : fechoPeriodo.getReciboRhList() )
            {
                FechoPeriodo oldFkFechoPeriodoOfReciboRhListReciboRh = reciboRhListReciboRh.getFkFechoPeriodo();
                reciboRhListReciboRh.setFkFechoPeriodo( fechoPeriodo );
                reciboRhListReciboRh = em.merge( reciboRhListReciboRh );
                if ( oldFkFechoPeriodoOfReciboRhListReciboRh != null )
                {
                    oldFkFechoPeriodoOfReciboRhListReciboRh.getReciboRhList().remove( reciboRhListReciboRh );
                    oldFkFechoPeriodoOfReciboRhListReciboRh = em.merge( oldFkFechoPeriodoOfReciboRhListReciboRh );
                }
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

    public void edit( FechoPeriodo fechoPeriodo ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FechoPeriodo persistentFechoPeriodo = em.find( FechoPeriodo.class, fechoPeriodo.getPkFechoPeriodo() );
            TbAno fkAnoOld = persistentFechoPeriodo.getFkAno();
            TbAno fkAnoNew = fechoPeriodo.getFkAno();
            TbMesRh fkPeriodoOld = persistentFechoPeriodo.getFkPeriodo();
            TbMesRh fkPeriodoNew = fechoPeriodo.getFkPeriodo();
            TbUsuario fkUsuarioOld = persistentFechoPeriodo.getFkUsuario();
            TbUsuario fkUsuarioNew = fechoPeriodo.getFkUsuario();
            List<ReciboRh> reciboRhListOld = persistentFechoPeriodo.getReciboRhList();
            List<ReciboRh> reciboRhListNew = fechoPeriodo.getReciboRhList();
            if ( fkAnoNew != null )
            {
                fkAnoNew = em.getReference( fkAnoNew.getClass(), fkAnoNew.getIdAno() );
                fechoPeriodo.setFkAno( fkAnoNew );
            }
            if ( fkPeriodoNew != null )
            {
                fkPeriodoNew = em.getReference( fkPeriodoNew.getClass(), fkPeriodoNew.getPkMesRh() );
                fechoPeriodo.setFkPeriodo( fkPeriodoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                fechoPeriodo.setFkUsuario( fkUsuarioNew );
            }
            List<ReciboRh> attachedReciboRhListNew = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListNewReciboRhToAttach : reciboRhListNew )
            {
                reciboRhListNewReciboRhToAttach = em.getReference( reciboRhListNewReciboRhToAttach.getClass(), reciboRhListNewReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhListNew.add( reciboRhListNewReciboRhToAttach );
            }
            reciboRhListNew = attachedReciboRhListNew;
            fechoPeriodo.setReciboRhList( reciboRhListNew );
            fechoPeriodo = em.merge( fechoPeriodo );
            if ( fkAnoOld != null && !fkAnoOld.equals( fkAnoNew ) )
            {
                fkAnoOld.getFechoPeriodoList().remove( fechoPeriodo );
                fkAnoOld = em.merge( fkAnoOld );
            }
            if ( fkAnoNew != null && !fkAnoNew.equals( fkAnoOld ) )
            {
                fkAnoNew.getFechoPeriodoList().add( fechoPeriodo );
                fkAnoNew = em.merge( fkAnoNew );
            }
            if ( fkPeriodoOld != null && !fkPeriodoOld.equals( fkPeriodoNew ) )
            {
                fkPeriodoOld.getFechoPeriodoList().remove( fechoPeriodo );
                fkPeriodoOld = em.merge( fkPeriodoOld );
            }
            if ( fkPeriodoNew != null && !fkPeriodoNew.equals( fkPeriodoOld ) )
            {
                fkPeriodoNew.getFechoPeriodoList().add( fechoPeriodo );
                fkPeriodoNew = em.merge( fkPeriodoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getFechoPeriodoList().remove( fechoPeriodo );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getFechoPeriodoList().add( fechoPeriodo );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( ReciboRh reciboRhListOldReciboRh : reciboRhListOld )
            {
                if ( !reciboRhListNew.contains( reciboRhListOldReciboRh ) )
                {
                    reciboRhListOldReciboRh.setFkFechoPeriodo( null );
                    reciboRhListOldReciboRh = em.merge( reciboRhListOldReciboRh );
                }
            }
            for ( ReciboRh reciboRhListNewReciboRh : reciboRhListNew )
            {
                if ( !reciboRhListOld.contains( reciboRhListNewReciboRh ) )
                {
                    FechoPeriodo oldFkFechoPeriodoOfReciboRhListNewReciboRh = reciboRhListNewReciboRh.getFkFechoPeriodo();
                    reciboRhListNewReciboRh.setFkFechoPeriodo( fechoPeriodo );
                    reciboRhListNewReciboRh = em.merge( reciboRhListNewReciboRh );
                    if ( oldFkFechoPeriodoOfReciboRhListNewReciboRh != null && !oldFkFechoPeriodoOfReciboRhListNewReciboRh.equals( fechoPeriodo ) )
                    {
                        oldFkFechoPeriodoOfReciboRhListNewReciboRh.getReciboRhList().remove( reciboRhListNewReciboRh );
                        oldFkFechoPeriodoOfReciboRhListNewReciboRh = em.merge( oldFkFechoPeriodoOfReciboRhListNewReciboRh );
                    }
                }
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = fechoPeriodo.getPkFechoPeriodo();
                if ( findFechoPeriodo( id ) == null )
                {
                    throw new NonexistentEntityException( "The fechoPeriodo with id " + id + " no longer exists." );
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
            FechoPeriodo fechoPeriodo;
            try
            {
                fechoPeriodo = em.getReference( FechoPeriodo.class, id );
                fechoPeriodo.getPkFechoPeriodo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The fechoPeriodo with id " + id + " no longer exists.", enfe );
            }
            TbAno fkAno = fechoPeriodo.getFkAno();
            if ( fkAno != null )
            {
                fkAno.getFechoPeriodoList().remove( fechoPeriodo );
                fkAno = em.merge( fkAno );
            }
            TbMesRh fkPeriodo = fechoPeriodo.getFkPeriodo();
            if ( fkPeriodo != null )
            {
                fkPeriodo.getFechoPeriodoList().remove( fechoPeriodo );
                fkPeriodo = em.merge( fkPeriodo );
            }
            TbUsuario fkUsuario = fechoPeriodo.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getFechoPeriodoList().remove( fechoPeriodo );
                fkUsuario = em.merge( fkUsuario );
            }
            List<ReciboRh> reciboRhList = fechoPeriodo.getReciboRhList();
            for ( ReciboRh reciboRhListReciboRh : reciboRhList )
            {
                reciboRhListReciboRh.setFkFechoPeriodo( null );
                reciboRhListReciboRh = em.merge( reciboRhListReciboRh );
            }
            em.remove( fechoPeriodo );
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

    public List<FechoPeriodo> findFechoPeriodoEntities()
    {
        return findFechoPeriodoEntities( true, -1, -1 );
    }

    public List<FechoPeriodo> findFechoPeriodoEntities( int maxResults, int firstResult )
    {
        return findFechoPeriodoEntities( false, maxResults, firstResult );
    }

    private List<FechoPeriodo> findFechoPeriodoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( FechoPeriodo.class ) );
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

    public FechoPeriodo findFechoPeriodo( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( FechoPeriodo.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFechoPeriodoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FechoPeriodo> rt = cq.from( FechoPeriodo.class );
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
