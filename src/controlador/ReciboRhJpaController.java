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
import entity.FechoPeriodo;
import entity.FormaPagamento;
import entity.TbFuncionario;
import entity.ItemReciboRh;
import entity.ReciboRh;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ReciboRhJpaController implements Serializable
{

    public ReciboRhJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ReciboRh reciboRh )
    {
        if ( reciboRh.getItemReciboRhList() == null )
        {
            reciboRh.setItemReciboRhList( new ArrayList<ItemReciboRh>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            FechoPeriodo fkFechoPeriodo = reciboRh.getFkFechoPeriodo();
            if ( fkFechoPeriodo != null )
            {
                fkFechoPeriodo = em.getReference( fkFechoPeriodo.getClass(), fkFechoPeriodo.getPkFechoPeriodo() );
                reciboRh.setFkFechoPeriodo( fkFechoPeriodo );
            }
            FormaPagamento fkFormaPagamento = reciboRh.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento = em.getReference( fkFormaPagamento.getClass(), fkFormaPagamento.getPkFormaPagamento() );
                reciboRh.setFkFormaPagamento( fkFormaPagamento );
            }
            TbFuncionario fkFuncionario = reciboRh.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario = em.getReference( fkFuncionario.getClass(), fkFuncionario.getIdFuncionario() );
                reciboRh.setFkFuncionario( fkFuncionario );
            }
            List<ItemReciboRh> attachedItemReciboRhList = new ArrayList<ItemReciboRh>();
            for ( ItemReciboRh itemReciboRhListItemReciboRhToAttach : reciboRh.getItemReciboRhList() )
            {
                itemReciboRhListItemReciboRhToAttach = em.getReference( itemReciboRhListItemReciboRhToAttach.getClass(), itemReciboRhListItemReciboRhToAttach.getPkItemReciboRh() );
                attachedItemReciboRhList.add( itemReciboRhListItemReciboRhToAttach );
            }
            reciboRh.setItemReciboRhList( attachedItemReciboRhList );
            em.persist( reciboRh );
            if ( fkFechoPeriodo != null )
            {
                fkFechoPeriodo.getReciboRhList().add( reciboRh );
                fkFechoPeriodo = em.merge( fkFechoPeriodo );
            }
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getReciboRhList().add( reciboRh );
                fkFormaPagamento = em.merge( fkFormaPagamento );
            }
            if ( fkFuncionario != null )
            {
                fkFuncionario.getReciboRhList().add( reciboRh );
                fkFuncionario = em.merge( fkFuncionario );
            }
            for ( ItemReciboRh itemReciboRhListItemReciboRh : reciboRh.getItemReciboRhList() )
            {
                ReciboRh oldFkReciboRhOfItemReciboRhListItemReciboRh = itemReciboRhListItemReciboRh.getFkReciboRh();
                itemReciboRhListItemReciboRh.setFkReciboRh( reciboRh );
                itemReciboRhListItemReciboRh = em.merge( itemReciboRhListItemReciboRh );
                if ( oldFkReciboRhOfItemReciboRhListItemReciboRh != null )
                {
                    oldFkReciboRhOfItemReciboRhListItemReciboRh.getItemReciboRhList().remove( itemReciboRhListItemReciboRh );
                    oldFkReciboRhOfItemReciboRhListItemReciboRh = em.merge( oldFkReciboRhOfItemReciboRhListItemReciboRh );
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

    public void edit( ReciboRh reciboRh ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ReciboRh persistentReciboRh = em.find( ReciboRh.class, reciboRh.getPkReciboRh() );
            FechoPeriodo fkFechoPeriodoOld = persistentReciboRh.getFkFechoPeriodo();
            FechoPeriodo fkFechoPeriodoNew = reciboRh.getFkFechoPeriodo();
            FormaPagamento fkFormaPagamentoOld = persistentReciboRh.getFkFormaPagamento();
            FormaPagamento fkFormaPagamentoNew = reciboRh.getFkFormaPagamento();
            TbFuncionario fkFuncionarioOld = persistentReciboRh.getFkFuncionario();
            TbFuncionario fkFuncionarioNew = reciboRh.getFkFuncionario();
            List<ItemReciboRh> itemReciboRhListOld = persistentReciboRh.getItemReciboRhList();
            List<ItemReciboRh> itemReciboRhListNew = reciboRh.getItemReciboRhList();
            List<String> illegalOrphanMessages = null;
            for ( ItemReciboRh itemReciboRhListOldItemReciboRh : itemReciboRhListOld )
            {
                if ( !itemReciboRhListNew.contains( itemReciboRhListOldItemReciboRh ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemReciboRh " + itemReciboRhListOldItemReciboRh + " since its fkReciboRh field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkFechoPeriodoNew != null )
            {
                fkFechoPeriodoNew = em.getReference( fkFechoPeriodoNew.getClass(), fkFechoPeriodoNew.getPkFechoPeriodo() );
                reciboRh.setFkFechoPeriodo( fkFechoPeriodoNew );
            }
            if ( fkFormaPagamentoNew != null )
            {
                fkFormaPagamentoNew = em.getReference( fkFormaPagamentoNew.getClass(), fkFormaPagamentoNew.getPkFormaPagamento() );
                reciboRh.setFkFormaPagamento( fkFormaPagamentoNew );
            }
            if ( fkFuncionarioNew != null )
            {
                fkFuncionarioNew = em.getReference( fkFuncionarioNew.getClass(), fkFuncionarioNew.getIdFuncionario() );
                reciboRh.setFkFuncionario( fkFuncionarioNew );
            }
            List<ItemReciboRh> attachedItemReciboRhListNew = new ArrayList<ItemReciboRh>();
            for ( ItemReciboRh itemReciboRhListNewItemReciboRhToAttach : itemReciboRhListNew )
            {
                itemReciboRhListNewItemReciboRhToAttach = em.getReference( itemReciboRhListNewItemReciboRhToAttach.getClass(), itemReciboRhListNewItemReciboRhToAttach.getPkItemReciboRh() );
                attachedItemReciboRhListNew.add( itemReciboRhListNewItemReciboRhToAttach );
            }
            itemReciboRhListNew = attachedItemReciboRhListNew;
            reciboRh.setItemReciboRhList( itemReciboRhListNew );
            reciboRh = em.merge( reciboRh );
            if ( fkFechoPeriodoOld != null && !fkFechoPeriodoOld.equals( fkFechoPeriodoNew ) )
            {
                fkFechoPeriodoOld.getReciboRhList().remove( reciboRh );
                fkFechoPeriodoOld = em.merge( fkFechoPeriodoOld );
            }
            if ( fkFechoPeriodoNew != null && !fkFechoPeriodoNew.equals( fkFechoPeriodoOld ) )
            {
                fkFechoPeriodoNew.getReciboRhList().add( reciboRh );
                fkFechoPeriodoNew = em.merge( fkFechoPeriodoNew );
            }
            if ( fkFormaPagamentoOld != null && !fkFormaPagamentoOld.equals( fkFormaPagamentoNew ) )
            {
                fkFormaPagamentoOld.getReciboRhList().remove( reciboRh );
                fkFormaPagamentoOld = em.merge( fkFormaPagamentoOld );
            }
            if ( fkFormaPagamentoNew != null && !fkFormaPagamentoNew.equals( fkFormaPagamentoOld ) )
            {
                fkFormaPagamentoNew.getReciboRhList().add( reciboRh );
                fkFormaPagamentoNew = em.merge( fkFormaPagamentoNew );
            }
            if ( fkFuncionarioOld != null && !fkFuncionarioOld.equals( fkFuncionarioNew ) )
            {
                fkFuncionarioOld.getReciboRhList().remove( reciboRh );
                fkFuncionarioOld = em.merge( fkFuncionarioOld );
            }
            if ( fkFuncionarioNew != null && !fkFuncionarioNew.equals( fkFuncionarioOld ) )
            {
                fkFuncionarioNew.getReciboRhList().add( reciboRh );
                fkFuncionarioNew = em.merge( fkFuncionarioNew );
            }
            for ( ItemReciboRh itemReciboRhListNewItemReciboRh : itemReciboRhListNew )
            {
                if ( !itemReciboRhListOld.contains( itemReciboRhListNewItemReciboRh ) )
                {
                    ReciboRh oldFkReciboRhOfItemReciboRhListNewItemReciboRh = itemReciboRhListNewItemReciboRh.getFkReciboRh();
                    itemReciboRhListNewItemReciboRh.setFkReciboRh( reciboRh );
                    itemReciboRhListNewItemReciboRh = em.merge( itemReciboRhListNewItemReciboRh );
                    if ( oldFkReciboRhOfItemReciboRhListNewItemReciboRh != null && !oldFkReciboRhOfItemReciboRhListNewItemReciboRh.equals( reciboRh ) )
                    {
                        oldFkReciboRhOfItemReciboRhListNewItemReciboRh.getItemReciboRhList().remove( itemReciboRhListNewItemReciboRh );
                        oldFkReciboRhOfItemReciboRhListNewItemReciboRh = em.merge( oldFkReciboRhOfItemReciboRhListNewItemReciboRh );
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
                Integer id = reciboRh.getPkReciboRh();
                if ( findReciboRh( id ) == null )
                {
                    throw new NonexistentEntityException( "The reciboRh with id " + id + " no longer exists." );
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
            ReciboRh reciboRh;
            try
            {
                reciboRh = em.getReference( ReciboRh.class, id );
                reciboRh.getPkReciboRh();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The reciboRh with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemReciboRh> itemReciboRhListOrphanCheck = reciboRh.getItemReciboRhList();
            for ( ItemReciboRh itemReciboRhListOrphanCheckItemReciboRh : itemReciboRhListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This ReciboRh (" + reciboRh + ") cannot be destroyed since the ItemReciboRh " + itemReciboRhListOrphanCheckItemReciboRh + " in its itemReciboRhList field has a non-nullable fkReciboRh field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            FechoPeriodo fkFechoPeriodo = reciboRh.getFkFechoPeriodo();
            if ( fkFechoPeriodo != null )
            {
                fkFechoPeriodo.getReciboRhList().remove( reciboRh );
                fkFechoPeriodo = em.merge( fkFechoPeriodo );
            }
            FormaPagamento fkFormaPagamento = reciboRh.getFkFormaPagamento();
            if ( fkFormaPagamento != null )
            {
                fkFormaPagamento.getReciboRhList().remove( reciboRh );
                fkFormaPagamento = em.merge( fkFormaPagamento );
            }
            TbFuncionario fkFuncionario = reciboRh.getFkFuncionario();
            if ( fkFuncionario != null )
            {
                fkFuncionario.getReciboRhList().remove( reciboRh );
                fkFuncionario = em.merge( fkFuncionario );
            }
            em.remove( reciboRh );
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

    public List<ReciboRh> findReciboRhEntities()
    {
        return findReciboRhEntities( true, -1, -1 );
    }

    public List<ReciboRh> findReciboRhEntities( int maxResults, int firstResult )
    {
        return findReciboRhEntities( false, maxResults, firstResult );
    }

    private List<ReciboRh> findReciboRhEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ReciboRh.class ) );
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

    public ReciboRh findReciboRh( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ReciboRh.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getReciboRhCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReciboRh> rt = cq.from( ReciboRh.class );
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
