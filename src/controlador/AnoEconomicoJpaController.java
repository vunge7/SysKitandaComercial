/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.AnoEconomico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.NotasCompras;
import java.util.ArrayList;
import java.util.List;
import entity.Notas;
import entity.Compras;
import entity.TbVenda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class AnoEconomicoJpaController implements Serializable
{

    public AnoEconomicoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( AnoEconomico anoEconomico )
    {
        if ( anoEconomico.getNotasComprasList() == null )
        {
            anoEconomico.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( anoEconomico.getNotasList() == null )
        {
            anoEconomico.setNotasList( new ArrayList<Notas>() );
        }
        if ( anoEconomico.getComprasList() == null )
        {
            anoEconomico.setComprasList( new ArrayList<Compras>() );
        }
        if ( anoEconomico.getTbVendaList() == null )
        {
            anoEconomico.setTbVendaList( new ArrayList<TbVenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : anoEconomico.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            anoEconomico.setNotasComprasList( attachedNotasComprasList );
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : anoEconomico.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            anoEconomico.setNotasList( attachedNotasList );
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for ( Compras comprasListComprasToAttach : anoEconomico.getComprasList() )
            {
                comprasListComprasToAttach = em.getReference( comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getPkCompra() );
                attachedComprasList.add( comprasListComprasToAttach );
            }
            anoEconomico.setComprasList( attachedComprasList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : anoEconomico.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            anoEconomico.setTbVendaList( attachedTbVendaList );
            em.persist( anoEconomico );
            for ( NotasCompras notasComprasListNotasCompras : anoEconomico.getNotasComprasList() )
            {
                AnoEconomico oldFkAnoEconomicoOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getFkAnoEconomico();
                notasComprasListNotasCompras.setFkAnoEconomico( anoEconomico );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldFkAnoEconomicoOfNotasComprasListNotasCompras != null )
                {
                    oldFkAnoEconomicoOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldFkAnoEconomicoOfNotasComprasListNotasCompras = em.merge( oldFkAnoEconomicoOfNotasComprasListNotasCompras );
                }
            }
            for ( Notas notasListNotas : anoEconomico.getNotasList() )
            {
                AnoEconomico oldFkAnoEconomicoOfNotasListNotas = notasListNotas.getFkAnoEconomico();
                notasListNotas.setFkAnoEconomico( anoEconomico );
                notasListNotas = em.merge( notasListNotas );
                if ( oldFkAnoEconomicoOfNotasListNotas != null )
                {
                    oldFkAnoEconomicoOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldFkAnoEconomicoOfNotasListNotas = em.merge( oldFkAnoEconomicoOfNotasListNotas );
                }
            }
            for ( Compras comprasListCompras : anoEconomico.getComprasList() )
            {
                AnoEconomico oldFkAnoEconomicoOfComprasListCompras = comprasListCompras.getFkAnoEconomico();
                comprasListCompras.setFkAnoEconomico( anoEconomico );
                comprasListCompras = em.merge( comprasListCompras );
                if ( oldFkAnoEconomicoOfComprasListCompras != null )
                {
                    oldFkAnoEconomicoOfComprasListCompras.getComprasList().remove( comprasListCompras );
                    oldFkAnoEconomicoOfComprasListCompras = em.merge( oldFkAnoEconomicoOfComprasListCompras );
                }
            }
            for ( TbVenda tbVendaListTbVenda : anoEconomico.getTbVendaList() )
            {
                AnoEconomico oldFkAnoEconomicoOfTbVendaListTbVenda = tbVendaListTbVenda.getFkAnoEconomico();
                tbVendaListTbVenda.setFkAnoEconomico( anoEconomico );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldFkAnoEconomicoOfTbVendaListTbVenda != null )
                {
                    oldFkAnoEconomicoOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldFkAnoEconomicoOfTbVendaListTbVenda = em.merge( oldFkAnoEconomicoOfTbVendaListTbVenda );
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

    public void edit( AnoEconomico anoEconomico ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AnoEconomico persistentAnoEconomico = em.find( AnoEconomico.class, anoEconomico.getPkAnoEconomico() );
            List<NotasCompras> notasComprasListOld = persistentAnoEconomico.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = anoEconomico.getNotasComprasList();
            List<Notas> notasListOld = persistentAnoEconomico.getNotasList();
            List<Notas> notasListNew = anoEconomico.getNotasList();
            List<Compras> comprasListOld = persistentAnoEconomico.getComprasList();
            List<Compras> comprasListNew = anoEconomico.getComprasList();
            List<TbVenda> tbVendaListOld = persistentAnoEconomico.getTbVendaList();
            List<TbVenda> tbVendaListNew = anoEconomico.getTbVendaList();
            List<String> illegalOrphanMessages = null;
            for ( NotasCompras notasComprasListOldNotasCompras : notasComprasListOld )
            {
                if ( !notasComprasListNew.contains( notasComprasListOldNotasCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain NotasCompras " + notasComprasListOldNotasCompras + " since its fkAnoEconomico field is not nullable." );
                }
            }
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its fkAnoEconomico field is not nullable." );
                }
            }
            for ( TbVenda tbVendaListOldTbVenda : tbVendaListOld )
            {
                if ( !tbVendaListNew.contains( tbVendaListOldTbVenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its fkAnoEconomico field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<NotasCompras> attachedNotasComprasListNew = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNewNotasComprasToAttach : notasComprasListNew )
            {
                notasComprasListNewNotasComprasToAttach = em.getReference( notasComprasListNewNotasComprasToAttach.getClass(), notasComprasListNewNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasListNew.add( notasComprasListNewNotasComprasToAttach );
            }
            notasComprasListNew = attachedNotasComprasListNew;
            anoEconomico.setNotasComprasList( notasComprasListNew );
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            anoEconomico.setNotasList( notasListNew );
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for ( Compras comprasListNewComprasToAttach : comprasListNew )
            {
                comprasListNewComprasToAttach = em.getReference( comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getPkCompra() );
                attachedComprasListNew.add( comprasListNewComprasToAttach );
            }
            comprasListNew = attachedComprasListNew;
            anoEconomico.setComprasList( comprasListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            anoEconomico.setTbVendaList( tbVendaListNew );
            anoEconomico = em.merge( anoEconomico );
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    AnoEconomico oldFkAnoEconomicoOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getFkAnoEconomico();
                    notasComprasListNewNotasCompras.setFkAnoEconomico( anoEconomico );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldFkAnoEconomicoOfNotasComprasListNewNotasCompras != null && !oldFkAnoEconomicoOfNotasComprasListNewNotasCompras.equals( anoEconomico ) )
                    {
                        oldFkAnoEconomicoOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldFkAnoEconomicoOfNotasComprasListNewNotasCompras = em.merge( oldFkAnoEconomicoOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    AnoEconomico oldFkAnoEconomicoOfNotasListNewNotas = notasListNewNotas.getFkAnoEconomico();
                    notasListNewNotas.setFkAnoEconomico( anoEconomico );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldFkAnoEconomicoOfNotasListNewNotas != null && !oldFkAnoEconomicoOfNotasListNewNotas.equals( anoEconomico ) )
                    {
                        oldFkAnoEconomicoOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldFkAnoEconomicoOfNotasListNewNotas = em.merge( oldFkAnoEconomicoOfNotasListNewNotas );
                    }
                }
            }
            for ( Compras comprasListOldCompras : comprasListOld )
            {
                if ( !comprasListNew.contains( comprasListOldCompras ) )
                {
                    comprasListOldCompras.setFkAnoEconomico( null );
                    comprasListOldCompras = em.merge( comprasListOldCompras );
                }
            }
            for ( Compras comprasListNewCompras : comprasListNew )
            {
                if ( !comprasListOld.contains( comprasListNewCompras ) )
                {
                    AnoEconomico oldFkAnoEconomicoOfComprasListNewCompras = comprasListNewCompras.getFkAnoEconomico();
                    comprasListNewCompras.setFkAnoEconomico( anoEconomico );
                    comprasListNewCompras = em.merge( comprasListNewCompras );
                    if ( oldFkAnoEconomicoOfComprasListNewCompras != null && !oldFkAnoEconomicoOfComprasListNewCompras.equals( anoEconomico ) )
                    {
                        oldFkAnoEconomicoOfComprasListNewCompras.getComprasList().remove( comprasListNewCompras );
                        oldFkAnoEconomicoOfComprasListNewCompras = em.merge( oldFkAnoEconomicoOfComprasListNewCompras );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    AnoEconomico oldFkAnoEconomicoOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getFkAnoEconomico();
                    tbVendaListNewTbVenda.setFkAnoEconomico( anoEconomico );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldFkAnoEconomicoOfTbVendaListNewTbVenda != null && !oldFkAnoEconomicoOfTbVendaListNewTbVenda.equals( anoEconomico ) )
                    {
                        oldFkAnoEconomicoOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldFkAnoEconomicoOfTbVendaListNewTbVenda = em.merge( oldFkAnoEconomicoOfTbVendaListNewTbVenda );
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
                Integer id = anoEconomico.getPkAnoEconomico();
                if ( findAnoEconomico( id ) == null )
                {
                    throw new NonexistentEntityException( "The anoEconomico with id " + id + " no longer exists." );
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
            AnoEconomico anoEconomico;
            try
            {
                anoEconomico = em.getReference( AnoEconomico.class, id );
                anoEconomico.getPkAnoEconomico();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The anoEconomico with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<NotasCompras> notasComprasListOrphanCheck = anoEconomico.getNotasComprasList();
            for ( NotasCompras notasComprasListOrphanCheckNotasCompras : notasComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This AnoEconomico (" + anoEconomico + ") cannot be destroyed since the NotasCompras " + notasComprasListOrphanCheckNotasCompras + " in its notasComprasList field has a non-nullable fkAnoEconomico field." );
            }
            List<Notas> notasListOrphanCheck = anoEconomico.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This AnoEconomico (" + anoEconomico + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable fkAnoEconomico field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = anoEconomico.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This AnoEconomico (" + anoEconomico + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable fkAnoEconomico field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<Compras> comprasList = anoEconomico.getComprasList();
            for ( Compras comprasListCompras : comprasList )
            {
                comprasListCompras.setFkAnoEconomico( null );
                comprasListCompras = em.merge( comprasListCompras );
            }
            em.remove( anoEconomico );
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

    public List<AnoEconomico> findAnoEconomicoEntities()
    {
        return findAnoEconomicoEntities( true, -1, -1 );
    }

    public List<AnoEconomico> findAnoEconomicoEntities( int maxResults, int firstResult )
    {
        return findAnoEconomicoEntities( false, maxResults, firstResult );
    }

    private List<AnoEconomico> findAnoEconomicoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( AnoEconomico.class ) );
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

    public AnoEconomico findAnoEconomico( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( AnoEconomico.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getAnoEconomicoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnoEconomico> rt = cq.from( AnoEconomico.class );
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
