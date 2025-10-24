/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Familia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbTipoProduto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class FamiliaJpaController implements Serializable
{

    public FamiliaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Familia familia )
    {
        if ( familia.getTbTipoProdutoList() == null )
        {
            familia.setTbTipoProdutoList( new ArrayList<TbTipoProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbTipoProduto> attachedTbTipoProdutoList = new ArrayList<TbTipoProduto>();
            for ( TbTipoProduto tbTipoProdutoListTbTipoProdutoToAttach : familia.getTbTipoProdutoList() )
            {
                tbTipoProdutoListTbTipoProdutoToAttach = em.getReference( tbTipoProdutoListTbTipoProdutoToAttach.getClass(), tbTipoProdutoListTbTipoProdutoToAttach.getCodigo() );
                attachedTbTipoProdutoList.add( tbTipoProdutoListTbTipoProdutoToAttach );
            }
            familia.setTbTipoProdutoList( attachedTbTipoProdutoList );
            em.persist( familia );
            for ( TbTipoProduto tbTipoProdutoListTbTipoProduto : familia.getTbTipoProdutoList() )
            {
                Familia oldFkFamiliaOfTbTipoProdutoListTbTipoProduto = tbTipoProdutoListTbTipoProduto.getFkFamilia();
                tbTipoProdutoListTbTipoProduto.setFkFamilia( familia );
                tbTipoProdutoListTbTipoProduto = em.merge( tbTipoProdutoListTbTipoProduto );
                if ( oldFkFamiliaOfTbTipoProdutoListTbTipoProduto != null )
                {
                    oldFkFamiliaOfTbTipoProdutoListTbTipoProduto.getTbTipoProdutoList().remove( tbTipoProdutoListTbTipoProduto );
                    oldFkFamiliaOfTbTipoProdutoListTbTipoProduto = em.merge( oldFkFamiliaOfTbTipoProdutoListTbTipoProduto );
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

    public void edit( Familia familia ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia persistentFamilia = em.find( Familia.class, familia.getPkFamilia() );
            List<TbTipoProduto> tbTipoProdutoListOld = persistentFamilia.getTbTipoProdutoList();
            List<TbTipoProduto> tbTipoProdutoListNew = familia.getTbTipoProdutoList();
            List<String> illegalOrphanMessages = null;
            for ( TbTipoProduto tbTipoProdutoListOldTbTipoProduto : tbTipoProdutoListOld )
            {
                if ( !tbTipoProdutoListNew.contains( tbTipoProdutoListOldTbTipoProduto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbTipoProduto " + tbTipoProdutoListOldTbTipoProduto + " since its fkFamilia field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbTipoProduto> attachedTbTipoProdutoListNew = new ArrayList<TbTipoProduto>();
            for ( TbTipoProduto tbTipoProdutoListNewTbTipoProdutoToAttach : tbTipoProdutoListNew )
            {
                tbTipoProdutoListNewTbTipoProdutoToAttach = em.getReference( tbTipoProdutoListNewTbTipoProdutoToAttach.getClass(), tbTipoProdutoListNewTbTipoProdutoToAttach.getCodigo() );
                attachedTbTipoProdutoListNew.add( tbTipoProdutoListNewTbTipoProdutoToAttach );
            }
            tbTipoProdutoListNew = attachedTbTipoProdutoListNew;
            familia.setTbTipoProdutoList( tbTipoProdutoListNew );
            familia = em.merge( familia );
            for ( TbTipoProduto tbTipoProdutoListNewTbTipoProduto : tbTipoProdutoListNew )
            {
                if ( !tbTipoProdutoListOld.contains( tbTipoProdutoListNewTbTipoProduto ) )
                {
                    Familia oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto = tbTipoProdutoListNewTbTipoProduto.getFkFamilia();
                    tbTipoProdutoListNewTbTipoProduto.setFkFamilia( familia );
                    tbTipoProdutoListNewTbTipoProduto = em.merge( tbTipoProdutoListNewTbTipoProduto );
                    if ( oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto != null && !oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto.equals( familia ) )
                    {
                        oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto.getTbTipoProdutoList().remove( tbTipoProdutoListNewTbTipoProduto );
                        oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto = em.merge( oldFkFamiliaOfTbTipoProdutoListNewTbTipoProduto );
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
                Integer id = familia.getPkFamilia();
                if ( findFamilia( id ) == null )
                {
                    throw new NonexistentEntityException( "The familia with id " + id + " no longer exists." );
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
            Familia familia;
            try
            {
                familia = em.getReference( Familia.class, id );
                familia.getPkFamilia();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The familia with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbTipoProduto> tbTipoProdutoListOrphanCheck = familia.getTbTipoProdutoList();
            for ( TbTipoProduto tbTipoProdutoListOrphanCheckTbTipoProduto : tbTipoProdutoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Familia (" + familia + ") cannot be destroyed since the TbTipoProduto " + tbTipoProdutoListOrphanCheckTbTipoProduto + " in its tbTipoProdutoList field has a non-nullable fkFamilia field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( familia );
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

    public List<Familia> findFamiliaEntities()
    {
        return findFamiliaEntities( true, -1, -1 );
    }

    public List<Familia> findFamiliaEntities( int maxResults, int firstResult )
    {
        return findFamiliaEntities( false, maxResults, firstResult );
    }

    private List<Familia> findFamiliaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Familia.class ) );
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

    public Familia findFamilia( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Familia.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFamiliaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Familia> rt = cq.from( Familia.class );
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
