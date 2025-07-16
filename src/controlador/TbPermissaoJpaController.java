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
import entity.TbItemPermissao;
import entity.TbPermissao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbPermissaoJpaController implements Serializable
{

    public TbPermissaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbPermissao tbPermissao )
    {
        if ( tbPermissao.getTbItemPermissaoList() == null )
        {
            tbPermissao.setTbItemPermissaoList( new ArrayList<TbItemPermissao>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbItemPermissao> attachedTbItemPermissaoList = new ArrayList<TbItemPermissao>();
            for ( TbItemPermissao tbItemPermissaoListTbItemPermissaoToAttach : tbPermissao.getTbItemPermissaoList() )
            {
                tbItemPermissaoListTbItemPermissaoToAttach = em.getReference( tbItemPermissaoListTbItemPermissaoToAttach.getClass(), tbItemPermissaoListTbItemPermissaoToAttach.getIdItemPermissao() );
                attachedTbItemPermissaoList.add( tbItemPermissaoListTbItemPermissaoToAttach );
            }
            tbPermissao.setTbItemPermissaoList( attachedTbItemPermissaoList );
            em.persist( tbPermissao );
            for ( TbItemPermissao tbItemPermissaoListTbItemPermissao : tbPermissao.getTbItemPermissaoList() )
            {
                TbPermissao oldIdPermissaoOfTbItemPermissaoListTbItemPermissao = tbItemPermissaoListTbItemPermissao.getIdPermissao();
                tbItemPermissaoListTbItemPermissao.setIdPermissao( tbPermissao );
                tbItemPermissaoListTbItemPermissao = em.merge( tbItemPermissaoListTbItemPermissao );
                if ( oldIdPermissaoOfTbItemPermissaoListTbItemPermissao != null )
                {
                    oldIdPermissaoOfTbItemPermissaoListTbItemPermissao.getTbItemPermissaoList().remove( tbItemPermissaoListTbItemPermissao );
                    oldIdPermissaoOfTbItemPermissaoListTbItemPermissao = em.merge( oldIdPermissaoOfTbItemPermissaoListTbItemPermissao );
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

    public void edit( TbPermissao tbPermissao ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPermissao persistentTbPermissao = em.find( TbPermissao.class, tbPermissao.getIdPermissao() );
            List<TbItemPermissao> tbItemPermissaoListOld = persistentTbPermissao.getTbItemPermissaoList();
            List<TbItemPermissao> tbItemPermissaoListNew = tbPermissao.getTbItemPermissaoList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemPermissao tbItemPermissaoListOldTbItemPermissao : tbItemPermissaoListOld )
            {
                if ( !tbItemPermissaoListNew.contains( tbItemPermissaoListOldTbItemPermissao ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemPermissao " + tbItemPermissaoListOldTbItemPermissao + " since its idPermissao field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbItemPermissao> attachedTbItemPermissaoListNew = new ArrayList<TbItemPermissao>();
            for ( TbItemPermissao tbItemPermissaoListNewTbItemPermissaoToAttach : tbItemPermissaoListNew )
            {
                tbItemPermissaoListNewTbItemPermissaoToAttach = em.getReference( tbItemPermissaoListNewTbItemPermissaoToAttach.getClass(), tbItemPermissaoListNewTbItemPermissaoToAttach.getIdItemPermissao() );
                attachedTbItemPermissaoListNew.add( tbItemPermissaoListNewTbItemPermissaoToAttach );
            }
            tbItemPermissaoListNew = attachedTbItemPermissaoListNew;
            tbPermissao.setTbItemPermissaoList( tbItemPermissaoListNew );
            tbPermissao = em.merge( tbPermissao );
            for ( TbItemPermissao tbItemPermissaoListNewTbItemPermissao : tbItemPermissaoListNew )
            {
                if ( !tbItemPermissaoListOld.contains( tbItemPermissaoListNewTbItemPermissao ) )
                {
                    TbPermissao oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao = tbItemPermissaoListNewTbItemPermissao.getIdPermissao();
                    tbItemPermissaoListNewTbItemPermissao.setIdPermissao( tbPermissao );
                    tbItemPermissaoListNewTbItemPermissao = em.merge( tbItemPermissaoListNewTbItemPermissao );
                    if ( oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao != null && !oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao.equals( tbPermissao ) )
                    {
                        oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao.getTbItemPermissaoList().remove( tbItemPermissaoListNewTbItemPermissao );
                        oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao = em.merge( oldIdPermissaoOfTbItemPermissaoListNewTbItemPermissao );
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
                Long id = tbPermissao.getIdPermissao();
                if ( findTbPermissao( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbPermissao with id " + id + " no longer exists." );
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

    public void destroy( Long id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPermissao tbPermissao;
            try
            {
                tbPermissao = em.getReference( TbPermissao.class, id );
                tbPermissao.getIdPermissao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbPermissao with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemPermissao> tbItemPermissaoListOrphanCheck = tbPermissao.getTbItemPermissaoList();
            for ( TbItemPermissao tbItemPermissaoListOrphanCheckTbItemPermissao : tbItemPermissaoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbPermissao (" + tbPermissao + ") cannot be destroyed since the TbItemPermissao " + tbItemPermissaoListOrphanCheckTbItemPermissao + " in its tbItemPermissaoList field has a non-nullable idPermissao field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbPermissao );
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

    public List<TbPermissao> findTbPermissaoEntities()
    {
        return findTbPermissaoEntities( true, -1, -1 );
    }

    public List<TbPermissao> findTbPermissaoEntities( int maxResults, int firstResult )
    {
        return findTbPermissaoEntities( false, maxResults, firstResult );
    }

    private List<TbPermissao> findTbPermissaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbPermissao.class ) );
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

    public TbPermissao findTbPermissao( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbPermissao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbPermissaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPermissao> rt = cq.from( TbPermissao.class );
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
