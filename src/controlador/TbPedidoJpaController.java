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
import entity.TbMesas;
import entity.TbItemPedidos;
import entity.TbPedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbPedidoJpaController implements Serializable
{

    public TbPedidoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbPedido tbPedido )
    {
        if ( tbPedido.getTbItemPedidosList() == null )
        {
            tbPedido.setTbItemPedidosList( new ArrayList<TbItemPedidos>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMesas fkMesas = tbPedido.getFkMesas();
            if ( fkMesas != null )
            {
                fkMesas = em.getReference( fkMesas.getClass(), fkMesas.getPkMesas() );
                tbPedido.setFkMesas( fkMesas );
            }
            List<TbItemPedidos> attachedTbItemPedidosList = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListTbItemPedidosToAttach : tbPedido.getTbItemPedidosList() )
            {
                tbItemPedidosListTbItemPedidosToAttach = em.getReference( tbItemPedidosListTbItemPedidosToAttach.getClass(), tbItemPedidosListTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosList.add( tbItemPedidosListTbItemPedidosToAttach );
            }
            tbPedido.setTbItemPedidosList( attachedTbItemPedidosList );
            em.persist( tbPedido );
            if ( fkMesas != null )
            {
                fkMesas.getTbPedidoList().add( tbPedido );
                fkMesas = em.merge( fkMesas );
            }
            for ( TbItemPedidos tbItemPedidosListTbItemPedidos : tbPedido.getTbItemPedidosList() )
            {
                TbPedido oldFkPedidosOfTbItemPedidosListTbItemPedidos = tbItemPedidosListTbItemPedidos.getFkPedidos();
                tbItemPedidosListTbItemPedidos.setFkPedidos( tbPedido );
                tbItemPedidosListTbItemPedidos = em.merge( tbItemPedidosListTbItemPedidos );
                if ( oldFkPedidosOfTbItemPedidosListTbItemPedidos != null )
                {
                    oldFkPedidosOfTbItemPedidosListTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListTbItemPedidos );
                    oldFkPedidosOfTbItemPedidosListTbItemPedidos = em.merge( oldFkPedidosOfTbItemPedidosListTbItemPedidos );
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

    public void edit( TbPedido tbPedido ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPedido persistentTbPedido = em.find( TbPedido.class, tbPedido.getPkPedido() );
            TbMesas fkMesasOld = persistentTbPedido.getFkMesas();
            TbMesas fkMesasNew = tbPedido.getFkMesas();
            List<TbItemPedidos> tbItemPedidosListOld = persistentTbPedido.getTbItemPedidosList();
            List<TbItemPedidos> tbItemPedidosListNew = tbPedido.getTbItemPedidosList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemPedidos tbItemPedidosListOldTbItemPedidos : tbItemPedidosListOld )
            {
                if ( !tbItemPedidosListNew.contains( tbItemPedidosListOldTbItemPedidos ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemPedidos " + tbItemPedidosListOldTbItemPedidos + " since its fkPedidos field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkMesasNew != null )
            {
                fkMesasNew = em.getReference( fkMesasNew.getClass(), fkMesasNew.getPkMesas() );
                tbPedido.setFkMesas( fkMesasNew );
            }
            List<TbItemPedidos> attachedTbItemPedidosListNew = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidosToAttach : tbItemPedidosListNew )
            {
                tbItemPedidosListNewTbItemPedidosToAttach = em.getReference( tbItemPedidosListNewTbItemPedidosToAttach.getClass(), tbItemPedidosListNewTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosListNew.add( tbItemPedidosListNewTbItemPedidosToAttach );
            }
            tbItemPedidosListNew = attachedTbItemPedidosListNew;
            tbPedido.setTbItemPedidosList( tbItemPedidosListNew );
            tbPedido = em.merge( tbPedido );
            if ( fkMesasOld != null && !fkMesasOld.equals( fkMesasNew ) )
            {
                fkMesasOld.getTbPedidoList().remove( tbPedido );
                fkMesasOld = em.merge( fkMesasOld );
            }
            if ( fkMesasNew != null && !fkMesasNew.equals( fkMesasOld ) )
            {
                fkMesasNew.getTbPedidoList().add( tbPedido );
                fkMesasNew = em.merge( fkMesasNew );
            }
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidos : tbItemPedidosListNew )
            {
                if ( !tbItemPedidosListOld.contains( tbItemPedidosListNewTbItemPedidos ) )
                {
                    TbPedido oldFkPedidosOfTbItemPedidosListNewTbItemPedidos = tbItemPedidosListNewTbItemPedidos.getFkPedidos();
                    tbItemPedidosListNewTbItemPedidos.setFkPedidos( tbPedido );
                    tbItemPedidosListNewTbItemPedidos = em.merge( tbItemPedidosListNewTbItemPedidos );
                    if ( oldFkPedidosOfTbItemPedidosListNewTbItemPedidos != null && !oldFkPedidosOfTbItemPedidosListNewTbItemPedidos.equals( tbPedido ) )
                    {
                        oldFkPedidosOfTbItemPedidosListNewTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListNewTbItemPedidos );
                        oldFkPedidosOfTbItemPedidosListNewTbItemPedidos = em.merge( oldFkPedidosOfTbItemPedidosListNewTbItemPedidos );
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
                Integer id = tbPedido.getPkPedido();
                if ( findTbPedido( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbPedido with id " + id + " no longer exists." );
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
            TbPedido tbPedido;
            try
            {
                tbPedido = em.getReference( TbPedido.class, id );
                tbPedido.getPkPedido();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbPedido with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemPedidos> tbItemPedidosListOrphanCheck = tbPedido.getTbItemPedidosList();
            for ( TbItemPedidos tbItemPedidosListOrphanCheckTbItemPedidos : tbItemPedidosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbPedido (" + tbPedido + ") cannot be destroyed since the TbItemPedidos " + tbItemPedidosListOrphanCheckTbItemPedidos + " in its tbItemPedidosList field has a non-nullable fkPedidos field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbMesas fkMesas = tbPedido.getFkMesas();
            if ( fkMesas != null )
            {
                fkMesas.getTbPedidoList().remove( tbPedido );
                fkMesas = em.merge( fkMesas );
            }
            em.remove( tbPedido );
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

    public List<TbPedido> findTbPedidoEntities()
    {
        return findTbPedidoEntities( true, -1, -1 );
    }

    public List<TbPedido> findTbPedidoEntities( int maxResults, int firstResult )
    {
        return findTbPedidoEntities( false, maxResults, firstResult );
    }

    private List<TbPedido> findTbPedidoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbPedido.class ) );
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

    public TbPedido findTbPedido( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbPedido.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbPedidoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbPedido> rt = cq.from( TbPedido.class );
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
