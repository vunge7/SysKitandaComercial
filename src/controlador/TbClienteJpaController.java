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
import entity.Notas;
import entity.TbCliente;
import java.util.ArrayList;
import java.util.List;
import entity.TbProForma;
import entity.TbDesconto;
import entity.TbVenda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbClienteJpaController implements Serializable
{

    public TbClienteJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbCliente tbCliente )
    {
        if ( tbCliente.getNotasList() == null )
        {
            tbCliente.setNotasList( new ArrayList<Notas>() );
        }
        if ( tbCliente.getTbProFormaList() == null )
        {
            tbCliente.setTbProFormaList( new ArrayList<TbProForma>() );
        }
        if ( tbCliente.getTbDescontoList() == null )
        {
            tbCliente.setTbDescontoList( new ArrayList<TbDesconto>() );
        }
        if ( tbCliente.getTbVendaList() == null )
        {
            tbCliente.setTbVendaList( new ArrayList<TbVenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : tbCliente.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            tbCliente.setNotasList( attachedNotasList );
            List<TbProForma> attachedTbProFormaList = new ArrayList<TbProForma>();
            for ( TbProForma tbProFormaListTbProFormaToAttach : tbCliente.getTbProFormaList() )
            {
                tbProFormaListTbProFormaToAttach = em.getReference( tbProFormaListTbProFormaToAttach.getClass(), tbProFormaListTbProFormaToAttach.getPkProForma() );
                attachedTbProFormaList.add( tbProFormaListTbProFormaToAttach );
            }
            tbCliente.setTbProFormaList( attachedTbProFormaList );
            List<TbDesconto> attachedTbDescontoList = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListTbDescontoToAttach : tbCliente.getTbDescontoList() )
            {
                tbDescontoListTbDescontoToAttach = em.getReference( tbDescontoListTbDescontoToAttach.getClass(), tbDescontoListTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoList.add( tbDescontoListTbDescontoToAttach );
            }
            tbCliente.setTbDescontoList( attachedTbDescontoList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : tbCliente.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            tbCliente.setTbVendaList( attachedTbVendaList );
            em.persist( tbCliente );
            for ( Notas notasListNotas : tbCliente.getNotasList() )
            {
                TbCliente oldCodigoClienteOfNotasListNotas = notasListNotas.getCodigoCliente();
                notasListNotas.setCodigoCliente( tbCliente );
                notasListNotas = em.merge( notasListNotas );
                if ( oldCodigoClienteOfNotasListNotas != null )
                {
                    oldCodigoClienteOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldCodigoClienteOfNotasListNotas = em.merge( oldCodigoClienteOfNotasListNotas );
                }
            }
            for ( TbProForma tbProFormaListTbProForma : tbCliente.getTbProFormaList() )
            {
                TbCliente oldFkClienteOfTbProFormaListTbProForma = tbProFormaListTbProForma.getFkCliente();
                tbProFormaListTbProForma.setFkCliente( tbCliente );
                tbProFormaListTbProForma = em.merge( tbProFormaListTbProForma );
                if ( oldFkClienteOfTbProFormaListTbProForma != null )
                {
                    oldFkClienteOfTbProFormaListTbProForma.getTbProFormaList().remove( tbProFormaListTbProForma );
                    oldFkClienteOfTbProFormaListTbProForma = em.merge( oldFkClienteOfTbProFormaListTbProForma );
                }
            }
            for ( TbDesconto tbDescontoListTbDesconto : tbCliente.getTbDescontoList() )
            {
                TbCliente oldFkClienteOfTbDescontoListTbDesconto = tbDescontoListTbDesconto.getFkCliente();
                tbDescontoListTbDesconto.setFkCliente( tbCliente );
                tbDescontoListTbDesconto = em.merge( tbDescontoListTbDesconto );
                if ( oldFkClienteOfTbDescontoListTbDesconto != null )
                {
                    oldFkClienteOfTbDescontoListTbDesconto.getTbDescontoList().remove( tbDescontoListTbDesconto );
                    oldFkClienteOfTbDescontoListTbDesconto = em.merge( oldFkClienteOfTbDescontoListTbDesconto );
                }
            }
            for ( TbVenda tbVendaListTbVenda : tbCliente.getTbVendaList() )
            {
                TbCliente oldCodigoClienteOfTbVendaListTbVenda = tbVendaListTbVenda.getCodigoCliente();
                tbVendaListTbVenda.setCodigoCliente( tbCliente );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldCodigoClienteOfTbVendaListTbVenda != null )
                {
                    oldCodigoClienteOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldCodigoClienteOfTbVendaListTbVenda = em.merge( oldCodigoClienteOfTbVendaListTbVenda );
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

    public void edit( TbCliente tbCliente ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbCliente persistentTbCliente = em.find( TbCliente.class, tbCliente.getCodigo() );
            List<Notas> notasListOld = persistentTbCliente.getNotasList();
            List<Notas> notasListNew = tbCliente.getNotasList();
            List<TbProForma> tbProFormaListOld = persistentTbCliente.getTbProFormaList();
            List<TbProForma> tbProFormaListNew = tbCliente.getTbProFormaList();
            List<TbDesconto> tbDescontoListOld = persistentTbCliente.getTbDescontoList();
            List<TbDesconto> tbDescontoListNew = tbCliente.getTbDescontoList();
            List<TbVenda> tbVendaListOld = persistentTbCliente.getTbVendaList();
            List<TbVenda> tbVendaListNew = tbCliente.getTbVendaList();
            List<String> illegalOrphanMessages = null;
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its codigoCliente field is not nullable." );
                }
            }
            for ( TbProForma tbProFormaListOldTbProForma : tbProFormaListOld )
            {
                if ( !tbProFormaListNew.contains( tbProFormaListOldTbProForma ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbProForma " + tbProFormaListOldTbProForma + " since its fkCliente field is not nullable." );
                }
            }
            for ( TbDesconto tbDescontoListOldTbDesconto : tbDescontoListOld )
            {
                if ( !tbDescontoListNew.contains( tbDescontoListOldTbDesconto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbDesconto " + tbDescontoListOldTbDesconto + " since its fkCliente field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its codigoCliente field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            tbCliente.setNotasList( notasListNew );
            List<TbProForma> attachedTbProFormaListNew = new ArrayList<TbProForma>();
            for ( TbProForma tbProFormaListNewTbProFormaToAttach : tbProFormaListNew )
            {
                tbProFormaListNewTbProFormaToAttach = em.getReference( tbProFormaListNewTbProFormaToAttach.getClass(), tbProFormaListNewTbProFormaToAttach.getPkProForma() );
                attachedTbProFormaListNew.add( tbProFormaListNewTbProFormaToAttach );
            }
            tbProFormaListNew = attachedTbProFormaListNew;
            tbCliente.setTbProFormaList( tbProFormaListNew );
            List<TbDesconto> attachedTbDescontoListNew = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListNewTbDescontoToAttach : tbDescontoListNew )
            {
                tbDescontoListNewTbDescontoToAttach = em.getReference( tbDescontoListNewTbDescontoToAttach.getClass(), tbDescontoListNewTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoListNew.add( tbDescontoListNewTbDescontoToAttach );
            }
            tbDescontoListNew = attachedTbDescontoListNew;
            tbCliente.setTbDescontoList( tbDescontoListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            tbCliente.setTbVendaList( tbVendaListNew );
            tbCliente = em.merge( tbCliente );
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    TbCliente oldCodigoClienteOfNotasListNewNotas = notasListNewNotas.getCodigoCliente();
                    notasListNewNotas.setCodigoCliente( tbCliente );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldCodigoClienteOfNotasListNewNotas != null && !oldCodigoClienteOfNotasListNewNotas.equals( tbCliente ) )
                    {
                        oldCodigoClienteOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldCodigoClienteOfNotasListNewNotas = em.merge( oldCodigoClienteOfNotasListNewNotas );
                    }
                }
            }
            for ( TbProForma tbProFormaListNewTbProForma : tbProFormaListNew )
            {
                if ( !tbProFormaListOld.contains( tbProFormaListNewTbProForma ) )
                {
                    TbCliente oldFkClienteOfTbProFormaListNewTbProForma = tbProFormaListNewTbProForma.getFkCliente();
                    tbProFormaListNewTbProForma.setFkCliente( tbCliente );
                    tbProFormaListNewTbProForma = em.merge( tbProFormaListNewTbProForma );
                    if ( oldFkClienteOfTbProFormaListNewTbProForma != null && !oldFkClienteOfTbProFormaListNewTbProForma.equals( tbCliente ) )
                    {
                        oldFkClienteOfTbProFormaListNewTbProForma.getTbProFormaList().remove( tbProFormaListNewTbProForma );
                        oldFkClienteOfTbProFormaListNewTbProForma = em.merge( oldFkClienteOfTbProFormaListNewTbProForma );
                    }
                }
            }
            for ( TbDesconto tbDescontoListNewTbDesconto : tbDescontoListNew )
            {
                if ( !tbDescontoListOld.contains( tbDescontoListNewTbDesconto ) )
                {
                    TbCliente oldFkClienteOfTbDescontoListNewTbDesconto = tbDescontoListNewTbDesconto.getFkCliente();
                    tbDescontoListNewTbDesconto.setFkCliente( tbCliente );
                    tbDescontoListNewTbDesconto = em.merge( tbDescontoListNewTbDesconto );
                    if ( oldFkClienteOfTbDescontoListNewTbDesconto != null && !oldFkClienteOfTbDescontoListNewTbDesconto.equals( tbCliente ) )
                    {
                        oldFkClienteOfTbDescontoListNewTbDesconto.getTbDescontoList().remove( tbDescontoListNewTbDesconto );
                        oldFkClienteOfTbDescontoListNewTbDesconto = em.merge( oldFkClienteOfTbDescontoListNewTbDesconto );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    TbCliente oldCodigoClienteOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getCodigoCliente();
                    tbVendaListNewTbVenda.setCodigoCliente( tbCliente );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldCodigoClienteOfTbVendaListNewTbVenda != null && !oldCodigoClienteOfTbVendaListNewTbVenda.equals( tbCliente ) )
                    {
                        oldCodigoClienteOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldCodigoClienteOfTbVendaListNewTbVenda = em.merge( oldCodigoClienteOfTbVendaListNewTbVenda );
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
                Integer id = tbCliente.getCodigo();
                if ( findTbCliente( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbCliente with id " + id + " no longer exists." );
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
            TbCliente tbCliente;
            try
            {
                tbCliente = em.getReference( TbCliente.class, id );
                tbCliente.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbCliente with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Notas> notasListOrphanCheck = tbCliente.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbCliente (" + tbCliente + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable codigoCliente field." );
            }
            List<TbProForma> tbProFormaListOrphanCheck = tbCliente.getTbProFormaList();
            for ( TbProForma tbProFormaListOrphanCheckTbProForma : tbProFormaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbCliente (" + tbCliente + ") cannot be destroyed since the TbProForma " + tbProFormaListOrphanCheckTbProForma + " in its tbProFormaList field has a non-nullable fkCliente field." );
            }
            List<TbDesconto> tbDescontoListOrphanCheck = tbCliente.getTbDescontoList();
            for ( TbDesconto tbDescontoListOrphanCheckTbDesconto : tbDescontoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbCliente (" + tbCliente + ") cannot be destroyed since the TbDesconto " + tbDescontoListOrphanCheckTbDesconto + " in its tbDescontoList field has a non-nullable fkCliente field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = tbCliente.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbCliente (" + tbCliente + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable codigoCliente field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbCliente );
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

    public List<TbCliente> findTbClienteEntities()
    {
        return findTbClienteEntities( true, -1, -1 );
    }

    public List<TbCliente> findTbClienteEntities( int maxResults, int firstResult )
    {
        return findTbClienteEntities( false, maxResults, firstResult );
    }

    private List<TbCliente> findTbClienteEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbCliente.class ) );
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

    public TbCliente findTbCliente( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbCliente.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbClienteCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbCliente> rt = cq.from( TbCliente.class );
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
