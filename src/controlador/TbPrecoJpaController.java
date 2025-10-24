/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProduto;
import entity.TbUsuario;
import entity.TbItemProForma;
import java.util.ArrayList;
import java.util.List;
import entity.TbItemVenda;
import entity.NotasItem;
import entity.TbPreco;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbPrecoJpaController implements Serializable
{

    public TbPrecoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbPreco tbPreco )
    {
        if ( tbPreco.getTbItemProFormaList() == null )
        {
            tbPreco.setTbItemProFormaList( new ArrayList<TbItemProForma>() );
        }
        if ( tbPreco.getTbItemVendaList() == null )
        {
            tbPreco.setTbItemVendaList( new ArrayList<TbItemVenda>() );
        }
        if ( tbPreco.getNotasItemList() == null )
        {
            tbPreco.setNotasItemList( new ArrayList<NotasItem>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProduto fkProduto = tbPreco.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto = em.getReference( fkProduto.getClass(), fkProduto.getCodigo() );
                tbPreco.setFkProduto( fkProduto );
            }
            TbUsuario fkUsuario = tbPreco.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbPreco.setFkUsuario( fkUsuario );
            }
            List<TbItemProForma> attachedTbItemProFormaList = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListTbItemProFormaToAttach : tbPreco.getTbItemProFormaList() )
            {
                tbItemProFormaListTbItemProFormaToAttach = em.getReference( tbItemProFormaListTbItemProFormaToAttach.getClass(), tbItemProFormaListTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaList.add( tbItemProFormaListTbItemProFormaToAttach );
            }
            tbPreco.setTbItemProFormaList( attachedTbItemProFormaList );
            List<TbItemVenda> attachedTbItemVendaList = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListTbItemVendaToAttach : tbPreco.getTbItemVendaList() )
            {
                tbItemVendaListTbItemVendaToAttach = em.getReference( tbItemVendaListTbItemVendaToAttach.getClass(), tbItemVendaListTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaList.add( tbItemVendaListTbItemVendaToAttach );
            }
            tbPreco.setTbItemVendaList( attachedTbItemVendaList );
            List<NotasItem> attachedNotasItemList = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNotasItemToAttach : tbPreco.getNotasItemList() )
            {
                notasItemListNotasItemToAttach = em.getReference( notasItemListNotasItemToAttach.getClass(), notasItemListNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemList.add( notasItemListNotasItemToAttach );
            }
            tbPreco.setNotasItemList( attachedNotasItemList );
            em.persist( tbPreco );
            if ( fkProduto != null )
            {
                fkProduto.getTbPrecoList().add( tbPreco );
                fkProduto = em.merge( fkProduto );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbPrecoList().add( tbPreco );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( TbItemProForma tbItemProFormaListTbItemProForma : tbPreco.getTbItemProFormaList() )
            {
                TbPreco oldFkPrecoOfTbItemProFormaListTbItemProForma = tbItemProFormaListTbItemProForma.getFkPreco();
                tbItemProFormaListTbItemProForma.setFkPreco( tbPreco );
                tbItemProFormaListTbItemProForma = em.merge( tbItemProFormaListTbItemProForma );
                if ( oldFkPrecoOfTbItemProFormaListTbItemProForma != null )
                {
                    oldFkPrecoOfTbItemProFormaListTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListTbItemProForma );
                    oldFkPrecoOfTbItemProFormaListTbItemProForma = em.merge( oldFkPrecoOfTbItemProFormaListTbItemProForma );
                }
            }
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbPreco.getTbItemVendaList() )
            {
                TbPreco oldFkPrecoOfTbItemVendaListTbItemVenda = tbItemVendaListTbItemVenda.getFkPreco();
                tbItemVendaListTbItemVenda.setFkPreco( tbPreco );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
                if ( oldFkPrecoOfTbItemVendaListTbItemVenda != null )
                {
                    oldFkPrecoOfTbItemVendaListTbItemVenda.getTbItemVendaList().remove( tbItemVendaListTbItemVenda );
                    oldFkPrecoOfTbItemVendaListTbItemVenda = em.merge( oldFkPrecoOfTbItemVendaListTbItemVenda );
                }
            }
            for ( NotasItem notasItemListNotasItem : tbPreco.getNotasItemList() )
            {
                TbPreco oldFkPrecoOfNotasItemListNotasItem = notasItemListNotasItem.getFkPreco();
                notasItemListNotasItem.setFkPreco( tbPreco );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
                if ( oldFkPrecoOfNotasItemListNotasItem != null )
                {
                    oldFkPrecoOfNotasItemListNotasItem.getNotasItemList().remove( notasItemListNotasItem );
                    oldFkPrecoOfNotasItemListNotasItem = em.merge( oldFkPrecoOfNotasItemListNotasItem );
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

    public void edit( TbPreco tbPreco ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPreco persistentTbPreco = em.find( TbPreco.class, tbPreco.getPkPreco() );
            TbProduto fkProdutoOld = persistentTbPreco.getFkProduto();
            TbProduto fkProdutoNew = tbPreco.getFkProduto();
            TbUsuario fkUsuarioOld = persistentTbPreco.getFkUsuario();
            TbUsuario fkUsuarioNew = tbPreco.getFkUsuario();
            List<TbItemProForma> tbItemProFormaListOld = persistentTbPreco.getTbItemProFormaList();
            List<TbItemProForma> tbItemProFormaListNew = tbPreco.getTbItemProFormaList();
            List<TbItemVenda> tbItemVendaListOld = persistentTbPreco.getTbItemVendaList();
            List<TbItemVenda> tbItemVendaListNew = tbPreco.getTbItemVendaList();
            List<NotasItem> notasItemListOld = persistentTbPreco.getNotasItemList();
            List<NotasItem> notasItemListNew = tbPreco.getNotasItemList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemProForma tbItemProFormaListOldTbItemProForma : tbItemProFormaListOld )
            {
                if ( !tbItemProFormaListNew.contains( tbItemProFormaListOldTbItemProForma ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemProForma " + tbItemProFormaListOldTbItemProForma + " since its fkPreco field is not nullable." );
                }
            }
            for ( TbItemVenda tbItemVendaListOldTbItemVenda : tbItemVendaListOld )
            {
                if ( !tbItemVendaListNew.contains( tbItemVendaListOldTbItemVenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemVenda " + tbItemVendaListOldTbItemVenda + " since its fkPreco field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkProdutoNew != null )
            {
                fkProdutoNew = em.getReference( fkProdutoNew.getClass(), fkProdutoNew.getCodigo() );
                tbPreco.setFkProduto( fkProdutoNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbPreco.setFkUsuario( fkUsuarioNew );
            }
            List<TbItemProForma> attachedTbItemProFormaListNew = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListNewTbItemProFormaToAttach : tbItemProFormaListNew )
            {
                tbItemProFormaListNewTbItemProFormaToAttach = em.getReference( tbItemProFormaListNewTbItemProFormaToAttach.getClass(), tbItemProFormaListNewTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaListNew.add( tbItemProFormaListNewTbItemProFormaToAttach );
            }
            tbItemProFormaListNew = attachedTbItemProFormaListNew;
            tbPreco.setTbItemProFormaList( tbItemProFormaListNew );
            List<TbItemVenda> attachedTbItemVendaListNew = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListNewTbItemVendaToAttach : tbItemVendaListNew )
            {
                tbItemVendaListNewTbItemVendaToAttach = em.getReference( tbItemVendaListNewTbItemVendaToAttach.getClass(), tbItemVendaListNewTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaListNew.add( tbItemVendaListNewTbItemVendaToAttach );
            }
            tbItemVendaListNew = attachedTbItemVendaListNew;
            tbPreco.setTbItemVendaList( tbItemVendaListNew );
            List<NotasItem> attachedNotasItemListNew = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNewNotasItemToAttach : notasItemListNew )
            {
                notasItemListNewNotasItemToAttach = em.getReference( notasItemListNewNotasItemToAttach.getClass(), notasItemListNewNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemListNew.add( notasItemListNewNotasItemToAttach );
            }
            notasItemListNew = attachedNotasItemListNew;
            tbPreco.setNotasItemList( notasItemListNew );
            tbPreco = em.merge( tbPreco );
            if ( fkProdutoOld != null && !fkProdutoOld.equals( fkProdutoNew ) )
            {
                fkProdutoOld.getTbPrecoList().remove( tbPreco );
                fkProdutoOld = em.merge( fkProdutoOld );
            }
            if ( fkProdutoNew != null && !fkProdutoNew.equals( fkProdutoOld ) )
            {
                fkProdutoNew.getTbPrecoList().add( tbPreco );
                fkProdutoNew = em.merge( fkProdutoNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbPrecoList().remove( tbPreco );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbPrecoList().add( tbPreco );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( TbItemProForma tbItemProFormaListNewTbItemProForma : tbItemProFormaListNew )
            {
                if ( !tbItemProFormaListOld.contains( tbItemProFormaListNewTbItemProForma ) )
                {
                    TbPreco oldFkPrecoOfTbItemProFormaListNewTbItemProForma = tbItemProFormaListNewTbItemProForma.getFkPreco();
                    tbItemProFormaListNewTbItemProForma.setFkPreco( tbPreco );
                    tbItemProFormaListNewTbItemProForma = em.merge( tbItemProFormaListNewTbItemProForma );
                    if ( oldFkPrecoOfTbItemProFormaListNewTbItemProForma != null && !oldFkPrecoOfTbItemProFormaListNewTbItemProForma.equals( tbPreco ) )
                    {
                        oldFkPrecoOfTbItemProFormaListNewTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListNewTbItemProForma );
                        oldFkPrecoOfTbItemProFormaListNewTbItemProForma = em.merge( oldFkPrecoOfTbItemProFormaListNewTbItemProForma );
                    }
                }
            }
            for ( TbItemVenda tbItemVendaListNewTbItemVenda : tbItemVendaListNew )
            {
                if ( !tbItemVendaListOld.contains( tbItemVendaListNewTbItemVenda ) )
                {
                    TbPreco oldFkPrecoOfTbItemVendaListNewTbItemVenda = tbItemVendaListNewTbItemVenda.getFkPreco();
                    tbItemVendaListNewTbItemVenda.setFkPreco( tbPreco );
                    tbItemVendaListNewTbItemVenda = em.merge( tbItemVendaListNewTbItemVenda );
                    if ( oldFkPrecoOfTbItemVendaListNewTbItemVenda != null && !oldFkPrecoOfTbItemVendaListNewTbItemVenda.equals( tbPreco ) )
                    {
                        oldFkPrecoOfTbItemVendaListNewTbItemVenda.getTbItemVendaList().remove( tbItemVendaListNewTbItemVenda );
                        oldFkPrecoOfTbItemVendaListNewTbItemVenda = em.merge( oldFkPrecoOfTbItemVendaListNewTbItemVenda );
                    }
                }
            }
            for ( NotasItem notasItemListOldNotasItem : notasItemListOld )
            {
                if ( !notasItemListNew.contains( notasItemListOldNotasItem ) )
                {
                    notasItemListOldNotasItem.setFkPreco( null );
                    notasItemListOldNotasItem = em.merge( notasItemListOldNotasItem );
                }
            }
            for ( NotasItem notasItemListNewNotasItem : notasItemListNew )
            {
                if ( !notasItemListOld.contains( notasItemListNewNotasItem ) )
                {
                    TbPreco oldFkPrecoOfNotasItemListNewNotasItem = notasItemListNewNotasItem.getFkPreco();
                    notasItemListNewNotasItem.setFkPreco( tbPreco );
                    notasItemListNewNotasItem = em.merge( notasItemListNewNotasItem );
                    if ( oldFkPrecoOfNotasItemListNewNotasItem != null && !oldFkPrecoOfNotasItemListNewNotasItem.equals( tbPreco ) )
                    {
                        oldFkPrecoOfNotasItemListNewNotasItem.getNotasItemList().remove( notasItemListNewNotasItem );
                        oldFkPrecoOfNotasItemListNewNotasItem = em.merge( oldFkPrecoOfNotasItemListNewNotasItem );
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
                Integer id = tbPreco.getPkPreco();
                if ( findTbPreco( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbPreco with id " + id + " no longer exists." );
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
            TbPreco tbPreco;
            try
            {
                tbPreco = em.getReference( TbPreco.class, id );
                tbPreco.getPkPreco();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbPreco with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemProForma> tbItemProFormaListOrphanCheck = tbPreco.getTbItemProFormaList();
            for ( TbItemProForma tbItemProFormaListOrphanCheckTbItemProForma : tbItemProFormaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbPreco (" + tbPreco + ") cannot be destroyed since the TbItemProForma " + tbItemProFormaListOrphanCheckTbItemProForma + " in its tbItemProFormaList field has a non-nullable fkPreco field." );
            }
            List<TbItemVenda> tbItemVendaListOrphanCheck = tbPreco.getTbItemVendaList();
            for ( TbItemVenda tbItemVendaListOrphanCheckTbItemVenda : tbItemVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbPreco (" + tbPreco + ") cannot be destroyed since the TbItemVenda " + tbItemVendaListOrphanCheckTbItemVenda + " in its tbItemVendaList field has a non-nullable fkPreco field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbProduto fkProduto = tbPreco.getFkProduto();
            if ( fkProduto != null )
            {
                fkProduto.getTbPrecoList().remove( tbPreco );
                fkProduto = em.merge( fkProduto );
            }
            TbUsuario fkUsuario = tbPreco.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbPrecoList().remove( tbPreco );
                fkUsuario = em.merge( fkUsuario );
            }
            List<NotasItem> notasItemList = tbPreco.getNotasItemList();
            for ( NotasItem notasItemListNotasItem : notasItemList )
            {
                notasItemListNotasItem.setFkPreco( null );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
            }
            em.remove( tbPreco );
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

    public List<TbPreco> findTbPrecoEntities()
    {
        return findTbPrecoEntities( true, -1, -1 );
    }

    public List<TbPreco> findTbPrecoEntities( int maxResults, int firstResult )
    {
        return findTbPrecoEntities( false, maxResults, firstResult );
    }

    private List<TbPreco> findTbPrecoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbPreco.class ) );
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

    public TbPreco findTbPreco( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbPreco.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbPrecoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPreco> rt = cq.from( TbPreco.class );
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
