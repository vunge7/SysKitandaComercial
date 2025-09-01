/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.TbLocal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProduto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbLocalJpaController implements Serializable
{

    public TbLocalJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbLocal tbLocal )
    {
        if ( tbLocal.getTbProdutoList() == null )
        {
            tbLocal.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : tbLocal.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            tbLocal.setTbProdutoList( attachedTbProdutoList );
            em.persist( tbLocal );
            for ( TbProduto tbProdutoListTbProduto : tbLocal.getTbProdutoList() )
            {
                TbLocal oldCodLocalOfTbProdutoListTbProduto = tbProdutoListTbProduto.getCodLocal();
                tbProdutoListTbProduto.setCodLocal( tbLocal );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldCodLocalOfTbProdutoListTbProduto != null )
                {
                    oldCodLocalOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldCodLocalOfTbProdutoListTbProduto = em.merge( oldCodLocalOfTbProdutoListTbProduto );
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

    public void edit( TbLocal tbLocal ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLocal persistentTbLocal = em.find( TbLocal.class, tbLocal.getCodigo() );
            List<TbProduto> tbProdutoListOld = persistentTbLocal.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = tbLocal.getTbProdutoList();
            List<String> illegalOrphanMessages = null;
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbProduto " + tbProdutoListOldTbProduto + " since its codLocal field is not nullable." );
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
            tbLocal.setTbProdutoList( tbProdutoListNew );
            tbLocal = em.merge( tbLocal );
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    TbLocal oldCodLocalOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getCodLocal();
                    tbProdutoListNewTbProduto.setCodLocal( tbLocal );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldCodLocalOfTbProdutoListNewTbProduto != null && !oldCodLocalOfTbProdutoListNewTbProduto.equals( tbLocal ) )
                    {
                        oldCodLocalOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldCodLocalOfTbProdutoListNewTbProduto = em.merge( oldCodLocalOfTbProdutoListNewTbProduto );
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
                Integer id = tbLocal.getCodigo();
                if ( findTbLocal( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbLocal with id " + id + " no longer exists." );
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
            TbLocal tbLocal;
            try
            {
                tbLocal = em.getReference( TbLocal.class, id );
                tbLocal.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbLocal with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbProduto> tbProdutoListOrphanCheck = tbLocal.getTbProdutoList();
            for ( TbProduto tbProdutoListOrphanCheckTbProduto : tbProdutoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbLocal (" + tbLocal + ") cannot be destroyed since the TbProduto " + tbProdutoListOrphanCheckTbProduto + " in its tbProdutoList field has a non-nullable codLocal field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbLocal );
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

    public List<TbLocal> findTbLocalEntities()
    {
        return findTbLocalEntities( true, -1, -1 );
    }

    public List<TbLocal> findTbLocalEntities( int maxResults, int firstResult )
    {
        return findTbLocalEntities( false, maxResults, firstResult );
    }

    private List<TbLocal> findTbLocalEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbLocal.class ) );
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

    public TbLocal findTbLocal( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbLocal.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbLocalCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbLocal> rt = cq.from( TbLocal.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
