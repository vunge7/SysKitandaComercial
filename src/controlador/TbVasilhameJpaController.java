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
import entity.TbProduto;
import entity.TbItemEntradaVasilhame;
import entity.TbVasilhame;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbVasilhameJpaController implements Serializable
{

    public TbVasilhameJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbVasilhame tbVasilhame )
    {
        if ( tbVasilhame.getTbItemEntradaVasilhameList() == null )
        {
            tbVasilhame.setTbItemEntradaVasilhameList( new ArrayList<TbItemEntradaVasilhame>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem fkArmazem = tbVasilhame.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem = em.getReference( fkArmazem.getClass(), fkArmazem.getCodigo() );
                tbVasilhame.setFkArmazem( fkArmazem );
            }
            TbProduto fkProduto = tbVasilhame.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                tbVasilhame.setFkProduto( fkProduto );
            }
            List<TbItemEntradaVasilhame> attachedTbItemEntradaVasilhameList = new ArrayList<TbItemEntradaVasilhame>();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach : tbVasilhame.getTbItemEntradaVasilhameList() )
            {
                tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach = em.getReference( tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach.getClass(), tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach.getPkItemEntradaVasilhame() );
                attachedTbItemEntradaVasilhameList.add( tbItemEntradaVasilhameListTbItemEntradaVasilhameToAttach );
            }
            tbVasilhame.setTbItemEntradaVasilhameList( attachedTbItemEntradaVasilhameList );
            em.persist( tbVasilhame );
            if ( fkArmazem != null )
            {
                fkArmazem.getTbVasilhameList().add( tbVasilhame );
                fkArmazem = em.merge( fkArmazem );
            }
            if ( fkProduto != null )
            {
                fkProduto.getTbVasilhameList().add( tbVasilhame );
                fkProduto = em.merge( fkProduto );
            }
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListTbItemEntradaVasilhame : tbVasilhame.getTbItemEntradaVasilhameList() )
            {
                TbVasilhame oldFkVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame = tbItemEntradaVasilhameListTbItemEntradaVasilhame.getFkVasilhame();
                tbItemEntradaVasilhameListTbItemEntradaVasilhame.setFkVasilhame( tbVasilhame );
                tbItemEntradaVasilhameListTbItemEntradaVasilhame = em.merge( tbItemEntradaVasilhameListTbItemEntradaVasilhame );
                if ( oldFkVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame != null )
                {
                    oldFkVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhameListTbItemEntradaVasilhame );
                    oldFkVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame = em.merge( oldFkVasilhameOfTbItemEntradaVasilhameListTbItemEntradaVasilhame );
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

    public void edit( TbVasilhame tbVasilhame ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbVasilhame persistentTbVasilhame = em.find( TbVasilhame.class, tbVasilhame.getPkVasilhame() );
            TbArmazem fkArmazemOld = persistentTbVasilhame.getFkArmazem();
            TbArmazem fkArmazemNew = tbVasilhame.getFkArmazem();
            TbProduto fkProdutoOld = persistentTbVasilhame.getFkProduto();
            TbProduto fkProdutoNew = tbVasilhame.getFkProduto();
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListOld = persistentTbVasilhame.getTbItemEntradaVasilhameList();
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListNew = tbVasilhame.getTbItemEntradaVasilhameList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListOldTbItemEntradaVasilhame : tbItemEntradaVasilhameListOld )
            {
                if ( !tbItemEntradaVasilhameListNew.contains( tbItemEntradaVasilhameListOldTbItemEntradaVasilhame ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEntradaVasilhame " + tbItemEntradaVasilhameListOldTbItemEntradaVasilhame + " since its fkVasilhame field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkArmazemNew != null )
            {
                fkArmazemNew = em.getReference( fkArmazemNew.getClass(), fkArmazemNew.getCodigo() );
                tbVasilhame.setFkArmazem( fkArmazemNew );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                tbVasilhame.setFkProduto( fkProdutoNew );
            }
            List<TbItemEntradaVasilhame> attachedTbItemEntradaVasilhameListNew = new ArrayList<TbItemEntradaVasilhame>();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach : tbItemEntradaVasilhameListNew )
            {
                tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach = em.getReference( tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach.getClass(), tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach.getPkItemEntradaVasilhame() );
                attachedTbItemEntradaVasilhameListNew.add( tbItemEntradaVasilhameListNewTbItemEntradaVasilhameToAttach );
            }
            tbItemEntradaVasilhameListNew = attachedTbItemEntradaVasilhameListNew;
            tbVasilhame.setTbItemEntradaVasilhameList( tbItemEntradaVasilhameListNew );
            tbVasilhame = em.merge( tbVasilhame );
            if ( fkArmazemOld != null && !fkArmazemOld.equals( fkArmazemNew ) )
            {
                fkArmazemOld.getTbVasilhameList().remove( tbVasilhame );
                fkArmazemOld = em.merge( fkArmazemOld );
            }
            if ( fkArmazemNew != null && !fkArmazemNew.equals( fkArmazemOld ) )
            {
                fkArmazemNew.getTbVasilhameList().add( tbVasilhame );
                fkArmazemNew = em.merge( fkArmazemNew );
            }
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getTbVasilhameList().remove( tbVasilhame );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getTbVasilhameList().add( tbVasilhame );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListNewTbItemEntradaVasilhame : tbItemEntradaVasilhameListNew )
            {
                if ( !tbItemEntradaVasilhameListOld.contains( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame ) )
                {
                    TbVasilhame oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame = tbItemEntradaVasilhameListNewTbItemEntradaVasilhame.getFkVasilhame();
                    tbItemEntradaVasilhameListNewTbItemEntradaVasilhame.setFkVasilhame( tbVasilhame );
                    tbItemEntradaVasilhameListNewTbItemEntradaVasilhame = em.merge( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
                    if ( oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame != null && !oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame.equals( tbVasilhame ) )
                    {
                        oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame.getTbItemEntradaVasilhameList().remove( tbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
                        oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame = em.merge( oldFkVasilhameOfTbItemEntradaVasilhameListNewTbItemEntradaVasilhame );
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
                Integer id = tbVasilhame.getPkVasilhame();
                if ( findTbVasilhame( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbVasilhame with id " + id + " no longer exists." );
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
            TbVasilhame tbVasilhame;
            try
            {
                tbVasilhame = em.getReference( TbVasilhame.class, id );
                tbVasilhame.getPkVasilhame();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbVasilhame with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemEntradaVasilhame> tbItemEntradaVasilhameListOrphanCheck = tbVasilhame.getTbItemEntradaVasilhameList();
            for ( TbItemEntradaVasilhame tbItemEntradaVasilhameListOrphanCheckTbItemEntradaVasilhame : tbItemEntradaVasilhameListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbVasilhame (" + tbVasilhame + ") cannot be destroyed since the TbItemEntradaVasilhame " + tbItemEntradaVasilhameListOrphanCheckTbItemEntradaVasilhame + " in its tbItemEntradaVasilhameList field has a non-nullable fkVasilhame field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbArmazem fkArmazem = tbVasilhame.getFkArmazem();
            if ( fkArmazem != null )
            {
                fkArmazem.getTbVasilhameList().remove( tbVasilhame );
                fkArmazem = em.merge( fkArmazem );
            }
            TbProduto fkProduto = tbVasilhame.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getTbVasilhameList().remove( tbVasilhame );
                fkProduto = em.merge( fkProduto );
            }
            em.remove( tbVasilhame );
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

    public List<TbVasilhame> findTbVasilhameEntities()
    {
        return findTbVasilhameEntities( true, -1, -1 );
    }

    public List<TbVasilhame> findTbVasilhameEntities( int maxResults, int firstResult )
    {
        return findTbVasilhameEntities( false, maxResults, firstResult );
    }

    private List<TbVasilhame> findTbVasilhameEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbVasilhame.class ) );
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

    public TbVasilhame findTbVasilhame( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbVasilhame.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbVasilhameCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbVasilhame> rt = cq.from( TbVasilhame.class );
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
