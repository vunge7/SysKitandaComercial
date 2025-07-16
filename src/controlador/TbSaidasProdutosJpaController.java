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
import entity.TbUsuario;
import entity.TbItemSaidas;
import entity.TbSaidasProdutos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbSaidasProdutosJpaController implements Serializable
{

    public TbSaidasProdutosJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbSaidasProdutos tbSaidasProdutos )
    {
        if ( tbSaidasProdutos.getTbItemSaidasList() == null )
        {
            tbSaidasProdutos.setTbItemSaidasList( new ArrayList<TbItemSaidas>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem idArmazemFK = tbSaidasProdutos.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                tbSaidasProdutos.setIdArmazemFK( idArmazemFK );
            }
            TbUsuario fkUsuario = tbSaidasProdutos.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbSaidasProdutos.setFkUsuario( fkUsuario );
            }
            List<TbItemSaidas> attachedTbItemSaidasList = new ArrayList<TbItemSaidas>();
            for ( TbItemSaidas tbItemSaidasListTbItemSaidasToAttach : tbSaidasProdutos.getTbItemSaidasList() )
            {
                tbItemSaidasListTbItemSaidasToAttach = em.getReference( tbItemSaidasListTbItemSaidasToAttach.getClass(), tbItemSaidasListTbItemSaidasToAttach.getCodigo() );
                attachedTbItemSaidasList.add( tbItemSaidasListTbItemSaidasToAttach );
            }
            tbSaidasProdutos.setTbItemSaidasList( attachedTbItemSaidasList );
            em.persist( tbSaidasProdutos );
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbSaidasProdutosList().add( tbSaidasProdutos );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbSaidasProdutosList().add( tbSaidasProdutos );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( TbItemSaidas tbItemSaidasListTbItemSaidas : tbSaidasProdutos.getTbItemSaidasList() )
            {
                TbSaidasProdutos oldFkSaidasProdutosOfTbItemSaidasListTbItemSaidas = tbItemSaidasListTbItemSaidas.getFkSaidasProdutos();
                tbItemSaidasListTbItemSaidas.setFkSaidasProdutos( tbSaidasProdutos );
                tbItemSaidasListTbItemSaidas = em.merge( tbItemSaidasListTbItemSaidas );
                if ( oldFkSaidasProdutosOfTbItemSaidasListTbItemSaidas != null )
                {
                    oldFkSaidasProdutosOfTbItemSaidasListTbItemSaidas.getTbItemSaidasList().remove( tbItemSaidasListTbItemSaidas );
                    oldFkSaidasProdutosOfTbItemSaidasListTbItemSaidas = em.merge( oldFkSaidasProdutosOfTbItemSaidasListTbItemSaidas );
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

    public void edit( TbSaidasProdutos tbSaidasProdutos ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSaidasProdutos persistentTbSaidasProdutos = em.find( TbSaidasProdutos.class, tbSaidasProdutos.getPkSaidasProdutos() );
            TbArmazem idArmazemFKOld = persistentTbSaidasProdutos.getIdArmazemFK();
            TbArmazem idArmazemFKNew = tbSaidasProdutos.getIdArmazemFK();
            TbUsuario fkUsuarioOld = persistentTbSaidasProdutos.getFkUsuario();
            TbUsuario fkUsuarioNew = tbSaidasProdutos.getFkUsuario();
            List<TbItemSaidas> tbItemSaidasListOld = persistentTbSaidasProdutos.getTbItemSaidasList();
            List<TbItemSaidas> tbItemSaidasListNew = tbSaidasProdutos.getTbItemSaidasList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemSaidas tbItemSaidasListOldTbItemSaidas : tbItemSaidasListOld )
            {
                if ( !tbItemSaidasListNew.contains( tbItemSaidasListOldTbItemSaidas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemSaidas " + tbItemSaidasListOldTbItemSaidas + " since its fkSaidasProdutos field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                tbSaidasProdutos.setIdArmazemFK( idArmazemFKNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbSaidasProdutos.setFkUsuario( fkUsuarioNew );
            }
            List<TbItemSaidas> attachedTbItemSaidasListNew = new ArrayList<TbItemSaidas>();
            for ( TbItemSaidas tbItemSaidasListNewTbItemSaidasToAttach : tbItemSaidasListNew )
            {
                tbItemSaidasListNewTbItemSaidasToAttach = em.getReference( tbItemSaidasListNewTbItemSaidasToAttach.getClass(), tbItemSaidasListNewTbItemSaidasToAttach.getCodigo() );
                attachedTbItemSaidasListNew.add( tbItemSaidasListNewTbItemSaidasToAttach );
            }
            tbItemSaidasListNew = attachedTbItemSaidasListNew;
            tbSaidasProdutos.setTbItemSaidasList( tbItemSaidasListNew );
            tbSaidasProdutos = em.merge( tbSaidasProdutos );
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getTbSaidasProdutosList().remove( tbSaidasProdutos );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getTbSaidasProdutosList().add( tbSaidasProdutos );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbSaidasProdutosList().remove( tbSaidasProdutos );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbSaidasProdutosList().add( tbSaidasProdutos );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( TbItemSaidas tbItemSaidasListNewTbItemSaidas : tbItemSaidasListNew )
            {
                if ( !tbItemSaidasListOld.contains( tbItemSaidasListNewTbItemSaidas ) )
                {
                    TbSaidasProdutos oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas = tbItemSaidasListNewTbItemSaidas.getFkSaidasProdutos();
                    tbItemSaidasListNewTbItemSaidas.setFkSaidasProdutos( tbSaidasProdutos );
                    tbItemSaidasListNewTbItemSaidas = em.merge( tbItemSaidasListNewTbItemSaidas );
                    if ( oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas != null && !oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas.equals( tbSaidasProdutos ) )
                    {
                        oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas.getTbItemSaidasList().remove( tbItemSaidasListNewTbItemSaidas );
                        oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas = em.merge( oldFkSaidasProdutosOfTbItemSaidasListNewTbItemSaidas );
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
                Integer id = tbSaidasProdutos.getPkSaidasProdutos();
                if ( findTbSaidasProdutos( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbSaidasProdutos with id " + id + " no longer exists." );
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
            TbSaidasProdutos tbSaidasProdutos;
            try
            {
                tbSaidasProdutos = em.getReference( TbSaidasProdutos.class, id );
                tbSaidasProdutos.getPkSaidasProdutos();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbSaidasProdutos with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemSaidas> tbItemSaidasListOrphanCheck = tbSaidasProdutos.getTbItemSaidasList();
            for ( TbItemSaidas tbItemSaidasListOrphanCheckTbItemSaidas : tbItemSaidasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbSaidasProdutos (" + tbSaidasProdutos + ") cannot be destroyed since the TbItemSaidas " + tbItemSaidasListOrphanCheckTbItemSaidas + " in its tbItemSaidasList field has a non-nullable fkSaidasProdutos field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbArmazem idArmazemFK = tbSaidasProdutos.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbSaidasProdutosList().remove( tbSaidasProdutos );
                idArmazemFK = em.merge( idArmazemFK );
            }
            TbUsuario fkUsuario = tbSaidasProdutos.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbSaidasProdutosList().remove( tbSaidasProdutos );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbSaidasProdutos );
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

    public List<TbSaidasProdutos> findTbSaidasProdutosEntities()
    {
        return findTbSaidasProdutosEntities( true, -1, -1 );
    }

    public List<TbSaidasProdutos> findTbSaidasProdutosEntities( int maxResults, int firstResult )
    {
        return findTbSaidasProdutosEntities( false, maxResults, firstResult );
    }

    private List<TbSaidasProdutos> findTbSaidasProdutosEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbSaidasProdutos.class ) );
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

    public TbSaidasProdutos findTbSaidasProdutos( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbSaidasProdutos.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbSaidasProdutosCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbSaidasProdutos> rt = cq.from( TbSaidasProdutos.class );
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
