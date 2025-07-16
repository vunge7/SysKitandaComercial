/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbArmazem;
import entity.TbEntradaVasilhame;
import entity.TbUsuario;
import entity.TbItemEntradaVasilhame;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbEntradaVasilhameJpaController implements Serializable
{

    public TbEntradaVasilhameJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEntradaVasilhame tbEntradaVasilhame )
    {
        if ( tbEntradaVasilhame.getTbItemEntradaVasilhameList() == null )
        {
            tbEntradaVasilhame.setTbItemEntradaVasilhameList( new ArrayList<TbItemEntradaVasilhame>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem fkAmazem = tbEntradaVasilhame.getFkAmazem();
            if ( fkAmazem != null )
            {
                fkAmazem = em.getReference( fkAmazem.getClass(), fkAmazem.getCodigo() );
                tbEntradaVasilhame.setFkAmazem( fkAmazem );
            }
            TbUsuario fkUsuario = tbEntradaVasilhame.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbEntradaVasilhame.setFkUsuario( fkUsuario );
            }
            List<TbItemEntradaVasilhame> attachedTbItemEntradaVasilhameList = new ArrayList<TbItemEntradaVasilhame>();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach : tbEntradaVasilhame.getTbItemEntradaVasilhameList() )
            {
                tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach = em.getReference( tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach.getClass(), tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach.getPkItemEntradaVasilhame() );
                attachedTbItemEntradaVasilhameList.add( tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach );
            }
            tbEntradaVasilhame.setTbItemEntradaVasilhameList( attachedTbItemEntradaVasilhameList );
            em.persist( tbEntradaVasilhame );
            if ( fkAmazem != null )
            {
                fkAmazem.getTbEntradaVasilhameList().add( tbEntradaVasilhame );
                fkAmazem = em.merge( fkAmazem );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbEntradaVasilhameList().add( tbEntradaVasilhame );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListTbItemEntradaVasilhame : tbEntradaVasilhame.getTbItemEntradaVasilhameList() )
            {
                TbEntradaVasilhame oldFkEntradaVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame = tbItemEntradaVasilhameListTbItemEntradaVasilhame.getFkEntradaVasilhame();
                tbItemEntradaVasilhameListTbItemEntradaVasilhame.setFkEntradaVasilhame( tbEntradaVasilhame );
                tbItemEntradaVasilhameListTbItemEntradaVasilhame = em.merge( tbItemEntradaVasilhameListTbItemEntradaVasilhame );
                if ( oldFkEntradaVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame != null )
                {
                    oldFkEntradaVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhameListTbItemEntradaVasilhame );
                    oldFkEntradaVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame = em.merge( oldFkEntradaVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame );
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

    public void edit( TbEntradaVasilhame tbEntradaVasilhame ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEntradaVasilhame persistentTbEntradaVasilhame = em.find( TbEntradaVasilhame.class, tbEntradaVasilhame.getPkEntradaVasilhame() );
            TbArmazem fkAmazemOld = persistentTbEntradaVasilhame.getFkAmazem();
            TbArmazem fkAmazemNew = tbEntradaVasilhame.getFkAmazem();
            TbUsuario fkUsuarioOld = persistentTbEntradaVasilhame.getFkUsuario();
            TbUsuario fkUsuarioNew = tbEntradaVasilhame.getFkUsuario();
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListOld = persistentTbEntradaVasilhame.getTbItemEntradaVasilhameList();
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListNew = tbEntradaVasilhame.getTbItemEntradaVasilhameList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListOldTbItemEntradaVasilhame : tbItemEntradaVasilhameListOld )
            {
                if ( !tbItemEntradaVasilhameListNew.contains( tbItemEntradaVasilhameListOldTbItemEntradaVasilhame ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEntradaVasilhame " + tbItemEntradaVasilhameListOldTbItemEntradaVasilhame + " since its fkEntradaVasilhame field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkAmazemNew != null )
            {
                fkAmazemNew = em.getReference( fkAmazemNew.getClass(), fkAmazemNew.getCodigo() );
                tbEntradaVasilhame.setFkAmazem( fkAmazemNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbEntradaVasilhame.setFkUsuario( fkUsuarioNew );
            }
            List<TbItemEntradaVasilhame> attachedTbItemEntradaVasilhameListNew = new ArrayList<TbItemEntradaVasilhame>();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach : tbItemEntradaVasilhameListNew )
            {
                tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach = em.getReference( tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach.getClass(), tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach.getPkItemEntradaVasilhame() );
                attachedTbItemEntradaVasilhameListNew.add( tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach );
            }
            tbItemEntradaVasilhameListNew = attachedTbItemEntradaVasilhameListNew;
            tbEntradaVasilhame.setTbItemEntradaVasilhameList( tbItemEntradaVasilhameListNew );
            tbEntradaVasilhame = em.merge( tbEntradaVasilhame );
            if ( fkAmazemOld != null && !fkAmazemOld.equals( fkAmazemNew ) )
            {
                fkAmazemOld.getTbEntradaVasilhameList().remove( tbEntradaVasilhame );
                fkAmazemOld = em.merge( fkAmazemOld );
            }
            if ( fkAmazemNew != null && !fkAmazemNew.equals( fkAmazemOld ) )
            {
                fkAmazemNew.getTbEntradaVasilhameList().add( tbEntradaVasilhame );
                fkAmazemNew = em.merge( fkAmazemNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbEntradaVasilhameList().remove( tbEntradaVasilhame );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbEntradaVasilhameList().add( tbEntradaVasilhame );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListNewTbItemEntradaVasilhame : tbItemEntradaVasilhameListNew )
            {
                if ( !tbItemEntradaVasilhameListOld.contains( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame ) )
                {
                    TbEntradaVasilhame oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame = tbItemEntradaVasilhameListNewTbItemEntradaVasilhame.getFkEntradaVasilhame();
                    tbItemEntradaVasilhameListNewTbItemEntradaVasilhame.setFkEntradaVasilhame( tbEntradaVasilhame );
                    tbItemEntradaVasilhameListNewTbItemEntradaVasilhame = em.merge( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
                    if ( oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame != null && !oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame.equals( tbEntradaVasilhame ) )
                    {
                        oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
                        oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame = em.merge( oldFkEntradaVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
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
                Integer id = tbEntradaVasilhame.getPkEntradaVasilhame();
                if ( findTbEntradaVasilhame( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEntradaVasilhame with id " + id + " no longer exists." );
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

    public void destroy( Integer id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEntradaVasilhame tbEntradaVasilhame;
            try
            {
                tbEntradaVasilhame = em.getReference( TbEntradaVasilhame.class, id );
                tbEntradaVasilhame.getPkEntradaVasilhame();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEntradaVasilhame with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListOrphanCheck = tbEntradaVasilhame.getTbItemEntradaVasilhameList();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListOrphanCheckTbItemEntradaVasilhame : tbItemEntradaVasilhameListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbEntradaVasilhame (" + tbEntradaVasilhame + ") cannot be destroyed since the TbItemEntradaVasilhame " + tbItemEntradaVasilhameListOrphanCheckTbItemEntradaVasilhame + " in its tbItemEntradaVasilhameList field has a non-nullable fkEntradaVasilhame field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbArmazem fkAmazem = tbEntradaVasilhame.getFkAmazem();
            if ( fkAmazem != null )
            {
                fkAmazem.getTbEntradaVasilhameList().remove( tbEntradaVasilhame );
                fkAmazem = em.merge( fkAmazem );
            }
            TbUsuario fkUsuario = tbEntradaVasilhame.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbEntradaVasilhameList().remove( tbEntradaVasilhame );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbEntradaVasilhame );
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

    public List<TbEntradaVasilhame> findTbEntradaVasilhameEntities()
    {
        return findTbEntradaVasilhameEntities( true, -1, -1 );
    }

    public List<TbEntradaVasilhame> findTbEntradaVasilhameEntities( int maxResults, int firstResult )
    {
        return findTbEntradaVasilhameEntities( false, maxResults, firstResult );
    }

    private List<TbEntradaVasilhame> findTbEntradaVasilhameEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEntradaVasilhame.class ) );
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

    public TbEntradaVasilhame findTbEntradaVasilhame( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEntradaVasilhame.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEntradaVasilhameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEntradaVasilhame> rt = cq.from( TbEntradaVasilhame.class );
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
