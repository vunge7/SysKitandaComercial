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
import entity.TbItemPedidos;
import java.util.ArrayList;
import java.util.List;
import entity.TbItemVenda;
import entity.TbLugares;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbLugaresJpaController implements Serializable
{

    public TbLugaresJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbLugares tbLugares )
    {
        if ( tbLugares.getTbItemPedidosList() == null )
        {
            tbLugares.setTbItemPedidosList( new ArrayList<TbItemPedidos>() );
        }
        if ( tbLugares.getTbItemVendaList() == null )
        {
            tbLugares.setTbItemVendaList( new ArrayList<TbItemVenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbItemPedidos> attachedTbItemPedidosList = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListTbItemPedidosToAttach : tbLugares.getTbItemPedidosList() )
            {
                tbItemPedidosListTbItemPedidosToAttach = em.getReference( tbItemPedidosListTbItemPedidosToAttach.getClass(), tbItemPedidosListTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosList.add( tbItemPedidosListTbItemPedidosToAttach );
            }
            tbLugares.setTbItemPedidosList( attachedTbItemPedidosList );
            List<TbItemVenda> attachedTbItemVendaList = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListTbItemVendaToAttach : tbLugares.getTbItemVendaList() )
            {
                tbItemVendaListTbItemVendaToAttach = em.getReference( tbItemVendaListTbItemVendaToAttach.getClass(), tbItemVendaListTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaList.add( tbItemVendaListTbItemVendaToAttach );
            }
            tbLugares.setTbItemVendaList( attachedTbItemVendaList );
            em.persist( tbLugares );
            for ( TbItemPedidos tbItemPedidosListTbItemPedidos : tbLugares.getTbItemPedidosList() )
            {
                TbLugares oldFkLugaresOfTbItemPedidosListTbItemPedidos = tbItemPedidosListTbItemPedidos.getFkLugares();
                tbItemPedidosListTbItemPedidos.setFkLugares( tbLugares );
                tbItemPedidosListTbItemPedidos = em.merge( tbItemPedidosListTbItemPedidos );
                if ( oldFkLugaresOfTbItemPedidosListTbItemPedidos != null )
                {
                    oldFkLugaresOfTbItemPedidosListTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListTbItemPedidos );
                    oldFkLugaresOfTbItemPedidosListTbItemPedidos = em.merge( oldFkLugaresOfTbItemPedidosListTbItemPedidos );
                }
            }
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbLugares.getTbItemVendaList() )
            {
                TbLugares oldFkLugaresOfTbItemVendaListTbItemVenda = tbItemVendaListTbItemVenda.getFkLugares();
                tbItemVendaListTbItemVenda.setFkLugares( tbLugares );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
                if ( oldFkLugaresOfTbItemVendaListTbItemVenda != null )
                {
                    oldFkLugaresOfTbItemVendaListTbItemVenda.getTbItemVendaList().remove( tbItemVendaListTbItemVenda );
                    oldFkLugaresOfTbItemVendaListTbItemVenda = em.merge( oldFkLugaresOfTbItemVendaListTbItemVenda );
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

    public void edit( TbLugares tbLugares ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbLugares persistentTbLugares = em.find( TbLugares.class, tbLugares.getPkLugares() );
            List<TbItemPedidos> tbItemPedidosListOld = persistentTbLugares.getTbItemPedidosList();
            List<TbItemPedidos> tbItemPedidosListNew = tbLugares.getTbItemPedidosList();
            List<TbItemVenda> tbItemVendaListOld = persistentTbLugares.getTbItemVendaList();
            List<TbItemVenda> tbItemVendaListNew = tbLugares.getTbItemVendaList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemPedidos tbItemPedidosListOldTbItemPedidos : tbItemPedidosListOld )
            {
                if ( !tbItemPedidosListNew.contains( tbItemPedidosListOldTbItemPedidos ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemPedidos " + tbItemPedidosListOldTbItemPedidos + " since its fkLugares field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbItemPedidos> attachedTbItemPedidosListNew = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidosToAttach : tbItemPedidosListNew )
            {
                tbItemPedidosListNewTbItemPedidosToAttach = em.getReference( tbItemPedidosListNewTbItemPedidosToAttach.getClass(), tbItemPedidosListNewTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosListNew.add( tbItemPedidosListNewTbItemPedidosToAttach );
            }
            tbItemPedidosListNew = attachedTbItemPedidosListNew;
            tbLugares.setTbItemPedidosList( tbItemPedidosListNew );
            List<TbItemVenda> attachedTbItemVendaListNew = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListNewTbItemVendaToAttach : tbItemVendaListNew )
            {
                tbItemVendaListNewTbItemVendaToAttach = em.getReference( tbItemVendaListNewTbItemVendaToAttach.getClass(), tbItemVendaListNewTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaListNew.add( tbItemVendaListNewTbItemVendaToAttach );
            }
            tbItemVendaListNew = attachedTbItemVendaListNew;
            tbLugares.setTbItemVendaList( tbItemVendaListNew );
            tbLugares = em.merge( tbLugares );
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidos : tbItemPedidosListNew )
            {
                if ( !tbItemPedidosListOld.contains( tbItemPedidosListNewTbItemPedidos ) )
                {
                    TbLugares oldFkLugaresOfTbItemPedidosListNewTbItemPedidos = tbItemPedidosListNewTbItemPedidos.getFkLugares();
                    tbItemPedidosListNewTbItemPedidos.setFkLugares( tbLugares );
                    tbItemPedidosListNewTbItemPedidos = em.merge( tbItemPedidosListNewTbItemPedidos );
                    if ( oldFkLugaresOfTbItemPedidosListNewTbItemPedidos != null && !oldFkLugaresOfTbItemPedidosListNewTbItemPedidos.equals( tbLugares ) )
                    {
                        oldFkLugaresOfTbItemPedidosListNewTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListNewTbItemPedidos );
                        oldFkLugaresOfTbItemPedidosListNewTbItemPedidos = em.merge( oldFkLugaresOfTbItemPedidosListNewTbItemPedidos );
                    }
                }
            }
            for ( TbItemVenda tbItemVendaListOldTbItemVenda : tbItemVendaListOld )
            {
                if ( !tbItemVendaListNew.contains( tbItemVendaListOldTbItemVenda ) )
                {
                    tbItemVendaListOldTbItemVenda.setFkLugares( null );
                    tbItemVendaListOldTbItemVenda = em.merge( tbItemVendaListOldTbItemVenda );
                }
            }
            for ( TbItemVenda tbItemVendaListNewTbItemVenda : tbItemVendaListNew )
            {
                if ( !tbItemVendaListOld.contains( tbItemVendaListNewTbItemVenda ) )
                {
                    TbLugares oldFkLugaresOfTbItemVendaListNewTbItemVenda = tbItemVendaListNewTbItemVenda.getFkLugares();
                    tbItemVendaListNewTbItemVenda.setFkLugares( tbLugares );
                    tbItemVendaListNewTbItemVenda = em.merge( tbItemVendaListNewTbItemVenda );
                    if ( oldFkLugaresOfTbItemVendaListNewTbItemVenda != null && !oldFkLugaresOfTbItemVendaListNewTbItemVenda.equals( tbLugares ) )
                    {
                        oldFkLugaresOfTbItemVendaListNewTbItemVenda.getTbItemVendaList().remove( tbItemVendaListNewTbItemVenda );
                        oldFkLugaresOfTbItemVendaListNewTbItemVenda = em.merge( oldFkLugaresOfTbItemVendaListNewTbItemVenda );
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
                Integer id = tbLugares.getPkLugares();
                if ( findTbLugares( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbLugares with id " + id + " no longer exists." );
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
            TbLugares tbLugares;
            try
            {
                tbLugares = em.getReference( TbLugares.class, id );
                tbLugares.getPkLugares();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbLugares with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemPedidos> tbItemPedidosListOrphanCheck = tbLugares.getTbItemPedidosList();
            for ( TbItemPedidos tbItemPedidosListOrphanCheckTbItemPedidos : tbItemPedidosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbLugares (" + tbLugares + ") cannot be destroyed since the TbItemPedidos " + tbItemPedidosListOrphanCheckTbItemPedidos + " in its tbItemPedidosList field has a non-nullable fkLugares field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbItemVenda> tbItemVendaList = tbLugares.getTbItemVendaList();
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbItemVendaList )
            {
                tbItemVendaListTbItemVenda.setFkLugares( null );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
            }
            em.remove( tbLugares );
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

    public List<TbLugares> findTbLugaresEntities()
    {
        return findTbLugaresEntities( true, -1, -1 );
    }

    public List<TbLugares> findTbLugaresEntities( int maxResults, int firstResult )
    {
        return findTbLugaresEntities( false, maxResults, firstResult );
    }

    private List<TbLugares> findTbLugaresEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbLugares.class ) );
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

    public TbLugares findTbLugares( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbLugares.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbLugaresCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbLugares> rt = cq.from( TbLugares.class );
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
