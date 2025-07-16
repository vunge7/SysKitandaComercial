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
import entity.TbProduto;
import entity.Unidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class UnidadeJpaController implements Serializable
{

    public UnidadeJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Unidade unidade )
    {
        if ( unidade.getTbProdutoList() == null )
        {
            unidade.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : unidade.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            unidade.setTbProdutoList( attachedTbProdutoList );
            em.persist( unidade );
            for ( TbProduto tbProdutoListTbProduto : unidade.getTbProdutoList() )
            {
                Unidade oldCodUnidadeOfTbProdutoListTbProduto = tbProdutoListTbProduto.getCodUnidade();
                tbProdutoListTbProduto.setCodUnidade( unidade );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldCodUnidadeOfTbProdutoListTbProduto != null )
                {
                    oldCodUnidadeOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldCodUnidadeOfTbProdutoListTbProduto = em.merge( oldCodUnidadeOfTbProdutoListTbProduto );
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

    public void edit( Unidade unidade ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Unidade persistentUnidade = em.find( Unidade.class, unidade.getPkUnidade() );
            List<TbProduto> tbProdutoListOld = persistentUnidade.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = unidade.getTbProdutoList();
            List<String> illegalOrphanMessages = null;
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbProduto " + tbProdutoListOldTbProduto + " since its codUnidade field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbProduto> attachedTbProdutoListNew = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListNewTbProdutoToAttach : tbProdutoListNew )
            {
                tbProdutoListNewTbProdutoToAttach = em.getReference( tbProdutoListNewTbProdutoToAttach.getClass(), tbProdutoListNewTbProdutoToAttach.getCodigo() );
                attachedTbProdutoListNew.add( tbProdutoListNewTbProdutoToAttach );
            }
            tbProdutoListNew = attachedTbProdutoListNew;
            unidade.setTbProdutoList( tbProdutoListNew );
            unidade = em.merge( unidade );
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    Unidade oldCodUnidadeOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getCodUnidade();
                    tbProdutoListNewTbProduto.setCodUnidade( unidade );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldCodUnidadeOfTbProdutoListNewTbProduto != null && !oldCodUnidadeOfTbProdutoListNewTbProduto.equals( unidade ) )
                    {
                        oldCodUnidadeOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldCodUnidadeOfTbProdutoListNewTbProduto = em.merge( oldCodUnidadeOfTbProdutoListNewTbProduto );
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
                Integer id = unidade.getPkUnidade();
                if ( findUnidade( id ) == null )
                {
                    throw new NonexistentEntityException( "The unidade with id " + id + " no longer exists." );
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
            Unidade unidade;
            try
            {
                unidade = em.getReference( Unidade.class, id );
                unidade.getPkUnidade();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The unidade with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbProduto> tbProdutoListOrphanCheck = unidade.getTbProdutoList();
            for ( TbProduto tbProdutoListOrphanCheckTbProduto : tbProdutoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Unidade (" + unidade + ") cannot be destroyed since the TbProduto " + tbProdutoListOrphanCheckTbProduto + " in its tbProdutoList field has a non-nullable codUnidade field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( unidade );
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

    public List<Unidade> findUnidadeEntities()
    {
        return findUnidadeEntities( true, -1, -1 );
    }

    public List<Unidade> findUnidadeEntities( int maxResults, int firstResult )
    {
        return findUnidadeEntities( false, maxResults, firstResult );
    }

    private List<Unidade> findUnidadeEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Unidade.class ) );
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

    public Unidade findUnidade( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Unidade.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getUnidadeCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Unidade> rt = cq.from( Unidade.class );
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
