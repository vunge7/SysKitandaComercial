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
import entity.ProdutoIsento;
import entity.ProdutosMotivosIsensao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ProdutosMotivosIsensaoJpaController implements Serializable
{

    public ProdutosMotivosIsensaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ProdutosMotivosIsensao produtosMotivosIsensao )
    {
        if ( produtosMotivosIsensao.getProdutoIsentoList() == null )
        {
            produtosMotivosIsensao.setProdutoIsentoList( new ArrayList<ProdutoIsento>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProdutoIsento> attachedProdutoIsentoList = new ArrayList<ProdutoIsento>();
            for ( ProdutoIsento produtoIsentoListProdutoIsentoToAttach : produtosMotivosIsensao.getProdutoIsentoList() )
            {
                produtoIsentoListProdutoIsentoToAttach = em.getReference( produtoIsentoListProdutoIsentoToAttach.getClass(), produtoIsentoListProdutoIsentoToAttach.getPkProdutoIsento() );
                attachedProdutoIsentoList.add( produtoIsentoListProdutoIsentoToAttach );
            }
            produtosMotivosIsensao.setProdutoIsentoList( attachedProdutoIsentoList );
            em.persist( produtosMotivosIsensao );
            for ( ProdutoIsento produtoIsentoListProdutoIsento : produtosMotivosIsensao.getProdutoIsentoList() )
            {
                ProdutosMotivosIsensao oldFkProdutosMotivosIsensaoOfProdutoIsentoListProdutoIsento = produtoIsentoListProdutoIsento.getFkProdutosMotivosIsensao();
                produtoIsentoListProdutoIsento.setFkProdutosMotivosIsensao( produtosMotivosIsensao );
                produtoIsentoListProdutoIsento = em.merge( produtoIsentoListProdutoIsento );
                if ( oldFkProdutosMotivosIsensaoOfProdutoIsentoListProdutoIsento != null )
                {
                    oldFkProdutosMotivosIsensaoOfProdutoIsentoListProdutoIsento.getProdutoIsentoList().remove( produtoIsentoListProdutoIsento );
                    oldFkProdutosMotivosIsensaoOfProdutoIsentoListProdutoIsento = em.merge( oldFkProdutosMotivosIsensaoOfProdutoIsentoListProdutoIsento );
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

    public void edit( ProdutosMotivosIsensao produtosMotivosIsensao ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutosMotivosIsensao persistentProdutosMotivosIsensao = em.find( ProdutosMotivosIsensao.class, produtosMotivosIsensao.getPkProdutosMotivosIsensao() );
            List<ProdutoIsento> produtoIsentoListOld = persistentProdutosMotivosIsensao.getProdutoIsentoList();
            List<ProdutoIsento> produtoIsentoListNew = produtosMotivosIsensao.getProdutoIsentoList();
            List<String> illegalOrphanMessages = null;
            for ( ProdutoIsento produtoIsentoListOldProdutoIsento : produtoIsentoListOld )
            {
                if ( !produtoIsentoListNew.contains( produtoIsentoListOldProdutoIsento ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ProdutoIsento " + produtoIsentoListOldProdutoIsento + " since its fkProdutosMotivosIsensao field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<ProdutoIsento> attachedProdutoIsentoListNew = new ArrayList<ProdutoIsento>();
            for ( ProdutoIsento produtoIsentoListNewProdutoIsentoToAttach : produtoIsentoListNew )
            {
                produtoIsentoListNewProdutoIsentoToAttach = em.getReference( produtoIsentoListNewProdutoIsentoToAttach.getClass(), produtoIsentoListNewProdutoIsentoToAttach.getPkProdutoIsento() );
                attachedProdutoIsentoListNew.add( produtoIsentoListNewProdutoIsentoToAttach );
            }
            produtoIsentoListNew = attachedProdutoIsentoListNew;
            produtosMotivosIsensao.setProdutoIsentoList( produtoIsentoListNew );
            produtosMotivosIsensao = em.merge( produtosMotivosIsensao );
            for ( ProdutoIsento produtoIsentoListNewProdutoIsento : produtoIsentoListNew )
            {
                if ( !produtoIsentoListOld.contains( produtoIsentoListNewProdutoIsento ) )
                {
                    ProdutosMotivosIsensao oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento = produtoIsentoListNewProdutoIsento.getFkProdutosMotivosIsensao();
                    produtoIsentoListNewProdutoIsento.setFkProdutosMotivosIsensao( produtosMotivosIsensao );
                    produtoIsentoListNewProdutoIsento = em.merge( produtoIsentoListNewProdutoIsento );
                    if ( oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento != null && !oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento.equals( produtosMotivosIsensao ) )
                    {
                        oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento.getProdutoIsentoList().remove( produtoIsentoListNewProdutoIsento );
                        oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento = em.merge( oldFkProdutosMotivosIsensaoOfProdutoIsentoListNewProdutoIsento );
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
                Integer id = produtosMotivosIsensao.getPkProdutosMotivosIsensao();
                if ( findProdutosMotivosIsensao( id ) == null )
                {
                    throw new NonexistentEntityException( "The produtosMotivosIsensao with id " + id + " no longer exists." );
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
            ProdutosMotivosIsensao produtosMotivosIsensao;
            try
            {
                produtosMotivosIsensao = em.getReference( ProdutosMotivosIsensao.class, id );
                produtosMotivosIsensao.getPkProdutosMotivosIsensao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The produtosMotivosIsensao with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ProdutoIsento> produtoIsentoListOrphanCheck = produtosMotivosIsensao.getProdutoIsentoList();
            for ( ProdutoIsento produtoIsentoListOrphanCheckProdutoIsento : produtoIsentoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This ProdutosMotivosIsensao (" + produtosMotivosIsensao + ") cannot be destroyed since the ProdutoIsento " + produtoIsentoListOrphanCheckProdutoIsento + " in its produtoIsentoList field has a non-nullable fkProdutosMotivosIsensao field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( produtosMotivosIsensao );
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

    public List<ProdutosMotivosIsensao> findProdutosMotivosIsensaoEntities()
    {
        return findProdutosMotivosIsensaoEntities( true, -1, -1 );
    }

    public List<ProdutosMotivosIsensao> findProdutosMotivosIsensaoEntities( int maxResults, int firstResult )
    {
        return findProdutosMotivosIsensaoEntities( false, maxResults, firstResult );
    }

    private List<ProdutosMotivosIsensao> findProdutosMotivosIsensaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ProdutosMotivosIsensao.class ) );
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

    public ProdutosMotivosIsensao findProdutosMotivosIsensao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ProdutosMotivosIsensao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getProdutosMotivosIsensaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutosMotivosIsensao> rt = cq.from( ProdutosMotivosIsensao.class );
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
