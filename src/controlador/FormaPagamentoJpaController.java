/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.FormaPagamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ReciboRh;
import java.util.ArrayList;
import java.util.List;
import entity.ItemCaixa;
import entity.FormaPagamentoItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class FormaPagamentoJpaController implements Serializable
{

    public FormaPagamentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( FormaPagamento formaPagamento )
    {
        if ( formaPagamento.getReciboRhList() == null )
        {
            formaPagamento.setReciboRhList( new ArrayList<ReciboRh>() );
        }
        if ( formaPagamento.getItemCaixaList() == null )
        {
            formaPagamento.setItemCaixaList( new ArrayList<ItemCaixa>() );
        }
        if ( formaPagamento.getFormaPagamentoItemList() == null )
        {
            formaPagamento.setFormaPagamentoItemList( new ArrayList<FormaPagamentoItem>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ReciboRh> attachedReciboRhList = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListReciboRhToAttach : formaPagamento.getReciboRhList() )
            {
                reciboRhListReciboRhToAttach = em.getReference( reciboRhListReciboRhToAttach.getClass(), reciboRhListReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhList.add( reciboRhListReciboRhToAttach );
            }
            formaPagamento.setReciboRhList( attachedReciboRhList );
            List<ItemCaixa> attachedItemCaixaList = new ArrayList<ItemCaixa>();
            for ( ItemCaixa itemCaixaListItemCaixaToAttach : formaPagamento.getItemCaixaList() )
            {
                itemCaixaListItemCaixaToAttach = em.getReference( itemCaixaListItemCaixaToAttach.getClass(), itemCaixaListItemCaixaToAttach.getPkItemCaixa() );
                attachedItemCaixaList.add( itemCaixaListItemCaixaToAttach );
            }
            formaPagamento.setItemCaixaList( attachedItemCaixaList );
            List<FormaPagamentoItem> attachedFormaPagamentoItemList = new ArrayList<FormaPagamentoItem>();
            for ( FormaPagamentoItem formaPagamentoItemListFormaPagamentoItemToAttach : formaPagamento.getFormaPagamentoItemList() )
            {
                formaPagamentoItemListFormaPagamentoItemToAttach = em.getReference( formaPagamentoItemListFormaPagamentoItemToAttach.getClass(), formaPagamentoItemListFormaPagamentoItemToAttach.getPkFormaPagamentoItem() );
                attachedFormaPagamentoItemList.add( formaPagamentoItemListFormaPagamentoItemToAttach );
            }
            formaPagamento.setFormaPagamentoItemList( attachedFormaPagamentoItemList );
            em.persist( formaPagamento );
            for ( ReciboRh reciboRhListReciboRh : formaPagamento.getReciboRhList() )
            {
                FormaPagamento oldFkFormaPagamentoOfReciboRhListReciboRh = reciboRhListReciboRh.getFkFormaPagamento();
                reciboRhListReciboRh.setFkFormaPagamento( formaPagamento );
                reciboRhListReciboRh = em.merge( reciboRhListReciboRh );
                if ( oldFkFormaPagamentoOfReciboRhListReciboRh != null )
                {
                    oldFkFormaPagamentoOfReciboRhListReciboRh.getReciboRhList().remove( reciboRhListReciboRh );
                    oldFkFormaPagamentoOfReciboRhListReciboRh = em.merge( oldFkFormaPagamentoOfReciboRhListReciboRh );
                }
            }
            for ( ItemCaixa itemCaixaListItemCaixa : formaPagamento.getItemCaixaList() )
            {
                FormaPagamento oldFkFormaPagamentoOfItemCaixaListItemCaixa = itemCaixaListItemCaixa.getFkFormaPagamento();
                itemCaixaListItemCaixa.setFkFormaPagamento( formaPagamento );
                itemCaixaListItemCaixa = em.merge( itemCaixaListItemCaixa );
                if ( oldFkFormaPagamentoOfItemCaixaListItemCaixa != null )
                {
                    oldFkFormaPagamentoOfItemCaixaListItemCaixa.getItemCaixaList().remove( itemCaixaListItemCaixa );
                    oldFkFormaPagamentoOfItemCaixaListItemCaixa = em.merge( oldFkFormaPagamentoOfItemCaixaListItemCaixa );
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListFormaPagamentoItem : formaPagamento.getFormaPagamentoItemList() )
            {
                FormaPagamento oldFkFormaPagamentoOfFormaPagamentoItemListFormaPagamentoItem = formaPagamentoItemListFormaPagamentoItem.getFkFormaPagamento();
                formaPagamentoItemListFormaPagamentoItem.setFkFormaPagamento( formaPagamento );
                formaPagamentoItemListFormaPagamentoItem = em.merge( formaPagamentoItemListFormaPagamentoItem );
                if ( oldFkFormaPagamentoOfFormaPagamentoItemListFormaPagamentoItem != null )
                {
                    oldFkFormaPagamentoOfFormaPagamentoItemListFormaPagamentoItem.getFormaPagamentoItemList().remove( formaPagamentoItemListFormaPagamentoItem );
                    oldFkFormaPagamentoOfFormaPagamentoItemListFormaPagamentoItem = em.merge( oldFkFormaPagamentoOfFormaPagamentoItemListFormaPagamentoItem );
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

    public void edit( FormaPagamento formaPagamento ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FormaPagamento persistentFormaPagamento = em.find( FormaPagamento.class, formaPagamento.getPkFormaPagamento() );
            List<ReciboRh> reciboRhListOld = persistentFormaPagamento.getReciboRhList();
            List<ReciboRh> reciboRhListNew = formaPagamento.getReciboRhList();
            List<ItemCaixa> itemCaixaListOld = persistentFormaPagamento.getItemCaixaList();
            List<ItemCaixa> itemCaixaListNew = formaPagamento.getItemCaixaList();
            List<FormaPagamentoItem> formaPagamentoItemListOld = persistentFormaPagamento.getFormaPagamentoItemList();
            List<FormaPagamentoItem> formaPagamentoItemListNew = formaPagamento.getFormaPagamentoItemList();
            List<String> illegalOrphanMessages = null;
            for ( ReciboRh reciboRhListOldReciboRh : reciboRhListOld )
            {
                if ( !reciboRhListNew.contains( reciboRhListOldReciboRh ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ReciboRh " + reciboRhListOldReciboRh + " since its fkFormaPagamento field is not nullable." );
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListOldFormaPagamentoItem : formaPagamentoItemListOld )
            {
                if ( !formaPagamentoItemListNew.contains( formaPagamentoItemListOldFormaPagamentoItem ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain FormaPagamentoItem " + formaPagamentoItemListOldFormaPagamentoItem + " since its fkFormaPagamento field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<ReciboRh> attachedReciboRhListNew = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListNewReciboRhToAttach : reciboRhListNew )
            {
                reciboRhListNewReciboRhToAttach = em.getReference( reciboRhListNewReciboRhToAttach.getClass(), reciboRhListNewReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhListNew.add( reciboRhListNewReciboRhToAttach );
            }
            reciboRhListNew = attachedReciboRhListNew;
            formaPagamento.setReciboRhList( reciboRhListNew );
            List<ItemCaixa> attachedItemCaixaListNew = new ArrayList<ItemCaixa>();
            for ( ItemCaixa itemCaixaListNewItemCaixaToAttach : itemCaixaListNew )
            {
                itemCaixaListNewItemCaixaToAttach = em.getReference( itemCaixaListNewItemCaixaToAttach.getClass(), itemCaixaListNewItemCaixaToAttach.getPkItemCaixa() );
                attachedItemCaixaListNew.add( itemCaixaListNewItemCaixaToAttach );
            }
            itemCaixaListNew = attachedItemCaixaListNew;
            formaPagamento.setItemCaixaList( itemCaixaListNew );
            List<FormaPagamentoItem> attachedFormaPagamentoItemListNew = new ArrayList<FormaPagamentoItem>();
            for ( FormaPagamentoItem formaPagamentoItemListNewFormaPagamentoItemToAttach : formaPagamentoItemListNew )
            {
                formaPagamentoItemListNewFormaPagamentoItemToAttach = em.getReference( formaPagamentoItemListNewFormaPagamentoItemToAttach.getClass(), formaPagamentoItemListNewFormaPagamentoItemToAttach.getPkFormaPagamentoItem() );
                attachedFormaPagamentoItemListNew.add( formaPagamentoItemListNewFormaPagamentoItemToAttach );
            }
            formaPagamentoItemListNew = attachedFormaPagamentoItemListNew;
            formaPagamento.setFormaPagamentoItemList( formaPagamentoItemListNew );
            formaPagamento = em.merge( formaPagamento );
            for ( ReciboRh reciboRhListNewReciboRh : reciboRhListNew )
            {
                if ( !reciboRhListOld.contains( reciboRhListNewReciboRh ) )
                {
                    FormaPagamento oldFkFormaPagamentoOfReciboRhListNewReciboRh = reciboRhListNewReciboRh.getFkFormaPagamento();
                    reciboRhListNewReciboRh.setFkFormaPagamento( formaPagamento );
                    reciboRhListNewReciboRh = em.merge( reciboRhListNewReciboRh );
                    if ( oldFkFormaPagamentoOfReciboRhListNewReciboRh != null && !oldFkFormaPagamentoOfReciboRhListNewReciboRh.equals( formaPagamento ) )
                    {
                        oldFkFormaPagamentoOfReciboRhListNewReciboRh.getReciboRhList().remove( reciboRhListNewReciboRh );
                        oldFkFormaPagamentoOfReciboRhListNewReciboRh = em.merge( oldFkFormaPagamentoOfReciboRhListNewReciboRh );
                    }
                }
            }
            for ( ItemCaixa itemCaixaListOldItemCaixa : itemCaixaListOld )
            {
                if ( !itemCaixaListNew.contains( itemCaixaListOldItemCaixa ) )
                {
                    itemCaixaListOldItemCaixa.setFkFormaPagamento( null );
                    itemCaixaListOldItemCaixa = em.merge( itemCaixaListOldItemCaixa );
                }
            }
            for ( ItemCaixa itemCaixaListNewItemCaixa : itemCaixaListNew )
            {
                if ( !itemCaixaListOld.contains( itemCaixaListNewItemCaixa ) )
                {
                    FormaPagamento oldFkFormaPagamentoOfItemCaixaListNewItemCaixa = itemCaixaListNewItemCaixa.getFkFormaPagamento();
                    itemCaixaListNewItemCaixa.setFkFormaPagamento( formaPagamento );
                    itemCaixaListNewItemCaixa = em.merge( itemCaixaListNewItemCaixa );
                    if ( oldFkFormaPagamentoOfItemCaixaListNewItemCaixa != null && !oldFkFormaPagamentoOfItemCaixaListNewItemCaixa.equals( formaPagamento ) )
                    {
                        oldFkFormaPagamentoOfItemCaixaListNewItemCaixa.getItemCaixaList().remove( itemCaixaListNewItemCaixa );
                        oldFkFormaPagamentoOfItemCaixaListNewItemCaixa = em.merge( oldFkFormaPagamentoOfItemCaixaListNewItemCaixa );
                    }
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListNewFormaPagamentoItem : formaPagamentoItemListNew )
            {
                if ( !formaPagamentoItemListOld.contains( formaPagamentoItemListNewFormaPagamentoItem ) )
                {
                    FormaPagamento oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem = formaPagamentoItemListNewFormaPagamentoItem.getFkFormaPagamento();
                    formaPagamentoItemListNewFormaPagamentoItem.setFkFormaPagamento( formaPagamento );
                    formaPagamentoItemListNewFormaPagamentoItem = em.merge( formaPagamentoItemListNewFormaPagamentoItem );
                    if ( oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem != null && !oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem.equals( formaPagamento ) )
                    {
                        oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem.getFormaPagamentoItemList().remove( formaPagamentoItemListNewFormaPagamentoItem );
                        oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem = em.merge( oldFkFormaPagamentoOfFormaPagamentoItemListNewFormaPagamentoItem );
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
                Integer id = formaPagamento.getPkFormaPagamento();
                if ( findFormaPagamento( id ) == null )
                {
                    throw new NonexistentEntityException( "The formaPagamento with id " + id + " no longer exists." );
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
            FormaPagamento formaPagamento;
            try
            {
                formaPagamento = em.getReference( FormaPagamento.class, id );
                formaPagamento.getPkFormaPagamento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The formaPagamento with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ReciboRh> reciboRhListOrphanCheck = formaPagamento.getReciboRhList();
            for ( ReciboRh reciboRhListOrphanCheckReciboRh : reciboRhListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This FormaPagamento (" + formaPagamento + ") cannot be destroyed since the ReciboRh " + reciboRhListOrphanCheckReciboRh + " in its reciboRhList field has a non-nullable fkFormaPagamento field." );
            }
            List<FormaPagamentoItem> formaPagamentoItemListOrphanCheck = formaPagamento.getFormaPagamentoItemList();
            for ( FormaPagamentoItem formaPagamentoItemListOrphanCheckFormaPagamentoItem : formaPagamentoItemListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This FormaPagamento (" + formaPagamento + ") cannot be destroyed since the FormaPagamentoItem " + formaPagamentoItemListOrphanCheckFormaPagamentoItem + " in its formaPagamentoItemList field has a non-nullable fkFormaPagamento field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<ItemCaixa> itemCaixaList = formaPagamento.getItemCaixaList();
            for ( ItemCaixa itemCaixaListItemCaixa : itemCaixaList )
            {
                itemCaixaListItemCaixa.setFkFormaPagamento( null );
                itemCaixaListItemCaixa = em.merge( itemCaixaListItemCaixa );
            }
            em.remove( formaPagamento );
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

    public List<FormaPagamento> findFormaPagamentoEntities()
    {
        return findFormaPagamentoEntities( true, -1, -1 );
    }

    public List<FormaPagamento> findFormaPagamentoEntities( int maxResults, int firstResult )
    {
        return findFormaPagamentoEntities( false, maxResults, firstResult );
    }

    private List<FormaPagamento> findFormaPagamentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( FormaPagamento.class ) );
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

    public FormaPagamento findFormaPagamento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( FormaPagamento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getFormaPagamentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FormaPagamento> rt = cq.from( FormaPagamento.class );
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
