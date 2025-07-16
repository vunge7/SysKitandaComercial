/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbItemVenda;
import entity.TbMesas;
import java.util.ArrayList;
import java.util.List;
import entity.TbPedido;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbMesasJpaController implements Serializable
{

    public TbMesasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbMesas tbMesas )
    {
        if ( tbMesas.getTbItemVendaList() == null )
        {
            tbMesas.setTbItemVendaList( new ArrayList<TbItemVenda>() );
        }
        if ( tbMesas.getTbPedidoList() == null )
        {
            tbMesas.setTbPedidoList( new ArrayList<TbPedido>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbItemVenda> attachedTbItemVendaList = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListTbItemVendaToAttach : tbMesas.getTbItemVendaList() )
            {
                tbItemVendaListTbItemVendaToAttach = em.getReference( tbItemVendaListTbItemVendaToAttach.getClass(), tbItemVendaListTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaList.add( tbItemVendaListTbItemVendaToAttach );
            }
            tbMesas.setTbItemVendaList( attachedTbItemVendaList );
            List<TbPedido> attachedTbPedidoList = new ArrayList<TbPedido>();
            for ( TbPedido tbPedidoListTbPedidoToAttach : tbMesas.getTbPedidoList() )
            {
                tbPedidoListTbPedidoToAttach = em.getReference( tbPedidoListTbPedidoToAttach.getClass(), tbPedidoListTbPedidoToAttach.getPkPedido() );
                attachedTbPedidoList.add( tbPedidoListTbPedidoToAttach );
            }
            tbMesas.setTbPedidoList( attachedTbPedidoList );
            em.persist( tbMesas );
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbMesas.getTbItemVendaList() )
            {
                TbMesas oldFkMesasOfTbItemVendaListTbItemVenda = tbItemVendaListTbItemVenda.getFkMesas();
                tbItemVendaListTbItemVenda.setFkMesas( tbMesas );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
                if ( oldFkMesasOfTbItemVendaListTbItemVenda != null )
                {
                    oldFkMesasOfTbItemVendaListTbItemVenda.getTbItemVendaList().remove( tbItemVendaListTbItemVenda );
                    oldFkMesasOfTbItemVendaListTbItemVenda = em.merge( oldFkMesasOfTbItemVendaListTbItemVenda );
                }
            }
            for ( TbPedido tbPedidoListTbPedido : tbMesas.getTbPedidoList() )
            {
                TbMesas oldFkMesasOfTbPedidoListTbPedido = tbPedidoListTbPedido.getFkMesas();
                tbPedidoListTbPedido.setFkMesas( tbMesas );
                tbPedidoListTbPedido = em.merge( tbPedidoListTbPedido );
                if ( oldFkMesasOfTbPedidoListTbPedido != null )
                {
                    oldFkMesasOfTbPedidoListTbPedido.getTbPedidoList().remove( tbPedidoListTbPedido );
                    oldFkMesasOfTbPedidoListTbPedido = em.merge( oldFkMesasOfTbPedidoListTbPedido );
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

    public void edit( TbMesas tbMesas ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMesas persistentTbMesas = em.find( TbMesas.class, tbMesas.getPkMesas() );
            List<TbItemVenda> tbItemVendaListOld = persistentTbMesas.getTbItemVendaList();
            List<TbItemVenda> tbItemVendaListNew = tbMesas.getTbItemVendaList();
            List<TbPedido> tbPedidoListOld = persistentTbMesas.getTbPedidoList();
            List<TbPedido> tbPedidoListNew = tbMesas.getTbPedidoList();
            List<TbItemVenda> attachedTbItemVendaListNew = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListNewTbItemVendaToAttach : tbItemVendaListNew )
            {
                tbItemVendaListNewTbItemVendaToAttach = em.getReference( tbItemVendaListNewTbItemVendaToAttach.getClass(), tbItemVendaListNewTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaListNew.add( tbItemVendaListNewTbItemVendaToAttach );
            }
            tbItemVendaListNew = attachedTbItemVendaListNew;
            tbMesas.setTbItemVendaList( tbItemVendaListNew );
            List<TbPedido> attachedTbPedidoListNew = new ArrayList<TbPedido>();
            for ( TbPedido tbPedidoListNewTbPedidoToAttach : tbPedidoListNew )
            {
                tbPedidoListNewTbPedidoToAttach = em.getReference( tbPedidoListNewTbPedidoToAttach.getClass(), tbPedidoListNewTbPedidoToAttach.getPkPedido() );
                attachedTbPedidoListNew.add( tbPedidoListNewTbPedidoToAttach );
            }
            tbPedidoListNew = attachedTbPedidoListNew;
            tbMesas.setTbPedidoList( tbPedidoListNew );
            tbMesas = em.merge( tbMesas );
            for ( TbItemVenda tbItemVendaListOldTbItemVenda : tbItemVendaListOld )
            {
                if ( !tbItemVendaListNew.contains( tbItemVendaListOldTbItemVenda ) )
                {
                    tbItemVendaListOldTbItemVenda.setFkMesas( null );
                    tbItemVendaListOldTbItemVenda = em.merge( tbItemVendaListOldTbItemVenda );
                }
            }
            for ( TbItemVenda tbItemVendaListNewTbItemVenda : tbItemVendaListNew )
            {
                if ( !tbItemVendaListOld.contains( tbItemVendaListNewTbItemVenda ) )
                {
                    TbMesas oldFkMesasOfTbItemVendaListNewTbItemVenda = tbItemVendaListNewTbItemVenda.getFkMesas();
                    tbItemVendaListNewTbItemVenda.setFkMesas( tbMesas );
                    tbItemVendaListNewTbItemVenda = em.merge( tbItemVendaListNewTbItemVenda );
                    if ( oldFkMesasOfTbItemVendaListNewTbItemVenda != null && !oldFkMesasOfTbItemVendaListNewTbItemVenda.equals( tbMesas ) )
                    {
                        oldFkMesasOfTbItemVendaListNewTbItemVenda.getTbItemVendaList().remove( tbItemVendaListNewTbItemVenda );
                        oldFkMesasOfTbItemVendaListNewTbItemVenda = em.merge( oldFkMesasOfTbItemVendaListNewTbItemVenda );
                    }
                }
            }
            for ( TbPedido tbPedidoListOldTbPedido : tbPedidoListOld )
            {
                if ( !tbPedidoListNew.contains( tbPedidoListOldTbPedido ) )
                {
                    tbPedidoListOldTbPedido.setFkMesas( null );
                    tbPedidoListOldTbPedido = em.merge( tbPedidoListOldTbPedido );
                }
            }
            for ( TbPedido tbPedidoListNewTbPedido : tbPedidoListNew )
            {
                if ( !tbPedidoListOld.contains( tbPedidoListNewTbPedido ) )
                {
                    TbMesas oldFkMesasOfTbPedidoListNewTbPedido = tbPedidoListNewTbPedido.getFkMesas();
                    tbPedidoListNewTbPedido.setFkMesas( tbMesas );
                    tbPedidoListNewTbPedido = em.merge( tbPedidoListNewTbPedido );
                    if ( oldFkMesasOfTbPedidoListNewTbPedido != null && !oldFkMesasOfTbPedidoListNewTbPedido.equals( tbMesas ) )
                    {
                        oldFkMesasOfTbPedidoListNewTbPedido.getTbPedidoList().remove( tbPedidoListNewTbPedido );
                        oldFkMesasOfTbPedidoListNewTbPedido = em.merge( oldFkMesasOfTbPedidoListNewTbPedido );
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
                Integer id = tbMesas.getPkMesas();
                if ( findTbMesas( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbMesas with id " + id + " no longer exists." );
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

    public void destroy( Integer id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbMesas tbMesas;
            try
            {
                tbMesas = em.getReference( TbMesas.class, id );
                tbMesas.getPkMesas();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbMesas with id " + id + " no longer exists.", enfe );
            }
            List<TbItemVenda> tbItemVendaList = tbMesas.getTbItemVendaList();
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbItemVendaList )
            {
                tbItemVendaListTbItemVenda.setFkMesas( null );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
            }
            List<TbPedido> tbPedidoList = tbMesas.getTbPedidoList();
            for ( TbPedido tbPedidoListTbPedido : tbPedidoList )
            {
                tbPedidoListTbPedido.setFkMesas( null );
                tbPedidoListTbPedido = em.merge( tbPedidoListTbPedido );
            }
            em.remove( tbMesas );
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

    public List<TbMesas> findTbMesasEntities()
    {
        return findTbMesasEntities( true, -1, -1 );
    }

    public List<TbMesas> findTbMesasEntities( int maxResults, int firstResult )
    {
        return findTbMesasEntities( false, maxResults, firstResult );
    }

    private List<TbMesas> findTbMesasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbMesas.class ) );
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

    public TbMesas findTbMesas( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbMesas.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbMesasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbMesas> rt = cq.from( TbMesas.class );
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
