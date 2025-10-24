/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Imposto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ProdutoImposto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ImpostoJpaController implements Serializable
{

    public ImpostoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Imposto imposto )
    {
        if ( imposto.getProdutoImpostoList() == null )
        {
            imposto.setProdutoImpostoList( new ArrayList<ProdutoImposto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProdutoImposto> attachedProdutoImpostoList = new ArrayList<ProdutoImposto>();
            for ( ProdutoImposto produtoImpostoListProdutoImpostoToAttach : imposto.getProdutoImpostoList() )
            {
                produtoImpostoListProdutoImpostoToAttach = em.getReference( produtoImpostoListProdutoImpostoToAttach.getClass(), produtoImpostoListProdutoImpostoToAttach.getPkProdutoImposto() );
                attachedProdutoImpostoList.add( produtoImpostoListProdutoImpostoToAttach );
            }
            imposto.setProdutoImpostoList( attachedProdutoImpostoList );
            em.persist( imposto );
            for ( ProdutoImposto produtoImpostoListProdutoImposto : imposto.getProdutoImpostoList() )
            {
                Imposto oldFkImpostoOfProdutoImpostoListProdutoImposto = produtoImpostoListProdutoImposto.getFkImposto();
                produtoImpostoListProdutoImposto.setFkImposto( imposto );
                produtoImpostoListProdutoImposto = em.merge( produtoImpostoListProdutoImposto );
                if ( oldFkImpostoOfProdutoImpostoListProdutoImposto != null )
                {
                    oldFkImpostoOfProdutoImpostoListProdutoImposto.getProdutoImpostoList().remove( produtoImpostoListProdutoImposto );
                    oldFkImpostoOfProdutoImpostoListProdutoImposto = em.merge( oldFkImpostoOfProdutoImpostoListProdutoImposto );
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

    public void edit( Imposto imposto ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Imposto persistentImposto = em.find( Imposto.class, imposto.getPkImposto() );
            List<ProdutoImposto> produtoImpostoListOld = persistentImposto.getProdutoImpostoList();
            List<ProdutoImposto> produtoImpostoListNew = imposto.getProdutoImpostoList();
            List<String> illegalOrphanMessages = null;
            for ( ProdutoImposto produtoImpostoListOldProdutoImposto : produtoImpostoListOld )
            {
                if ( !produtoImpostoListNew.contains( produtoImpostoListOldProdutoImposto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ProdutoImposto " + produtoImpostoListOldProdutoImposto + " since its fkImposto field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<ProdutoImposto> attachedProdutoImpostoListNew = new ArrayList<ProdutoImposto>();
            for ( ProdutoImposto produtoImpostoListNewProdutoImpostoToAttach : produtoImpostoListNew )
            {
                produtoImpostoListNewProdutoImpostoToAttach = em.getReference( produtoImpostoListNewProdutoImpostoToAttach.getClass(), produtoImpostoListNewProdutoImpostoToAttach.getPkProdutoImposto() );
                attachedProdutoImpostoListNew.add( produtoImpostoListNewProdutoImpostoToAttach );
            }
            produtoImpostoListNew = attachedProdutoImpostoListNew;
            imposto.setProdutoImpostoList( produtoImpostoListNew );
            imposto = em.merge( imposto );
            for ( ProdutoImposto produtoImpostoListNewProdutoImposto : produtoImpostoListNew )
            {
                if ( !produtoImpostoListOld.contains( produtoImpostoListNewProdutoImposto ) )
                {
                    Imposto oldFkImpostoOfProdutoImpostoListNewProdutoImposto = produtoImpostoListNewProdutoImposto.getFkImposto();
                    produtoImpostoListNewProdutoImposto.setFkImposto( imposto );
                    produtoImpostoListNewProdutoImposto = em.merge( produtoImpostoListNewProdutoImposto );
                    if ( oldFkImpostoOfProdutoImpostoListNewProdutoImposto != null && !oldFkImpostoOfProdutoImpostoListNewProdutoImposto.equals( imposto ) )
                    {
                        oldFkImpostoOfProdutoImpostoListNewProdutoImposto.getProdutoImpostoList().remove( produtoImpostoListNewProdutoImposto );
                        oldFkImpostoOfProdutoImpostoListNewProdutoImposto = em.merge( oldFkImpostoOfProdutoImpostoListNewProdutoImposto );
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
                Integer id = imposto.getPkImposto();
                if ( findImposto( id ) == null )
                {
                    throw new NonexistentEntityException( "The imposto with id " + id + " no longer exists." );
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
            Imposto imposto;
            try
            {
                imposto = em.getReference( Imposto.class, id );
                imposto.getPkImposto();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The imposto with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ProdutoImposto> produtoImpostoListOrphanCheck = imposto.getProdutoImpostoList();
            for ( ProdutoImposto produtoImpostoListOrphanCheckProdutoImposto : produtoImpostoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Imposto (" + imposto + ") cannot be destroyed since the ProdutoImposto " + produtoImpostoListOrphanCheckProdutoImposto + " in its produtoImpostoList field has a non-nullable fkImposto field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( imposto );
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

    public List<Imposto> findImpostoEntities()
    {
        return findImpostoEntities( true, -1, -1 );
    }

    public List<Imposto> findImpostoEntities( int maxResults, int firstResult )
    {
        return findImpostoEntities( false, maxResults, firstResult );
    }

    private List<Imposto> findImpostoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Imposto.class ) );
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

    public Imposto findImposto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Imposto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getImpostoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Imposto> rt = cq.from( Imposto.class );
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
