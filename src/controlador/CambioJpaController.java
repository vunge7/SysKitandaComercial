/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Cambio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Moeda;
import entity.Notas;
import java.util.ArrayList;
import java.util.List;
import entity.TbVenda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class CambioJpaController implements Serializable
{

    public CambioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Cambio cambio )
    {
        if ( cambio.getNotasList() == null )
        {
            cambio.setNotasList( new ArrayList<Notas>() );
        }
        if ( cambio.getTbVendaList() == null )
        {
            cambio.setTbVendaList( new ArrayList<TbVenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Moeda fkMoeda = cambio.getFkMoeda();
            if ( fkMoeda != null )
            {
                fkMoeda = em.getReference( fkMoeda.getClass(), fkMoeda.getPkMoeda() );
                cambio.setFkMoeda( fkMoeda );
            }
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : cambio.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            cambio.setNotasList( attachedNotasList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : cambio.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            cambio.setTbVendaList( attachedTbVendaList );
            em.persist( cambio );
            if ( fkMoeda != null )
            {
                fkMoeda.getCambioList().add( cambio );
                fkMoeda = em.merge( fkMoeda );
            }
            for ( Notas notasListNotas : cambio.getNotasList() )
            {
                Cambio oldFkCambioOfNotasListNotas = notasListNotas.getFkCambio();
                notasListNotas.setFkCambio( cambio );
                notasListNotas = em.merge( notasListNotas );
                if ( oldFkCambioOfNotasListNotas != null )
                {
                    oldFkCambioOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldFkCambioOfNotasListNotas = em.merge( oldFkCambioOfNotasListNotas );
                }
            }
            for ( TbVenda tbVendaListTbVenda : cambio.getTbVendaList() )
            {
                Cambio oldFkCambioOfTbVendaListTbVenda = tbVendaListTbVenda.getFkCambio();
                tbVendaListTbVenda.setFkCambio( cambio );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldFkCambioOfTbVendaListTbVenda != null )
                {
                    oldFkCambioOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldFkCambioOfTbVendaListTbVenda = em.merge( oldFkCambioOfTbVendaListTbVenda );
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

    public void edit( Cambio cambio ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Cambio persistentCambio = em.find( Cambio.class, cambio.getPkCambio() );
            Moeda fkMoedaOld = persistentCambio.getFkMoeda();
            Moeda fkMoedaNew = cambio.getFkMoeda();
            List<Notas> notasListOld = persistentCambio.getNotasList();
            List<Notas> notasListNew = cambio.getNotasList();
            List<TbVenda> tbVendaListOld = persistentCambio.getTbVendaList();
            List<TbVenda> tbVendaListNew = cambio.getTbVendaList();
            List<String> illegalOrphanMessages = null;
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its fkCambio field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its fkCambio field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkMoedaNew != null )
            {
                fkMoedaNew = em.getReference( fkMoedaNew.getClass(), fkMoedaNew.getPkMoeda() );
                cambio.setFkMoeda( fkMoedaNew );
            }
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            cambio.setNotasList( notasListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            cambio.setTbVendaList( tbVendaListNew );
            cambio = em.merge( cambio );
            if ( fkMoedaOld != null && !fkMoedaOld.equals( fkMoedaNew ) )
            {
                fkMoedaOld.getCambioList().remove( cambio );
                fkMoedaOld = em.merge( fkMoedaOld );
            }
            if ( fkMoedaNew != null && !fkMoedaNew.equals( fkMoedaOld ) )
            {
                fkMoedaNew.getCambioList().add( cambio );
                fkMoedaNew = em.merge( fkMoedaNew );
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    Cambio oldFkCambioOfNotasListNewNotas = notasListNewNotas.getFkCambio();
                    notasListNewNotas.setFkCambio( cambio );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldFkCambioOfNotasListNewNotas != null && !oldFkCambioOfNotasListNewNotas.equals( cambio ) )
                    {
                        oldFkCambioOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldFkCambioOfNotasListNewNotas = em.merge( oldFkCambioOfNotasListNewNotas );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    Cambio oldFkCambioOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getFkCambio();
                    tbVendaListNewTbVenda.setFkCambio( cambio );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldFkCambioOfTbVendaListNewTbVenda != null && !oldFkCambioOfTbVendaListNewTbVenda.equals( cambio ) )
                    {
                        oldFkCambioOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldFkCambioOfTbVendaListNewTbVenda = em.merge( oldFkCambioOfTbVendaListNewTbVenda );
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
                Integer id = cambio.getPkCambio();
                if ( findCambio( id ) == null )
                {
                    throw new NonexistentEntityException( "The cambio with id " + id + " no longer exists." );
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
            Cambio cambio;
            try
            {
                cambio = em.getReference( Cambio.class, id );
                cambio.getPkCambio();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The cambio with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Notas> notasListOrphanCheck = cambio.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Cambio (" + cambio + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable fkCambio field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = cambio.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Cambio (" + cambio + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable fkCambio field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            Moeda fkMoeda = cambio.getFkMoeda();
            if ( fkMoeda != null )
            {
                fkMoeda.getCambioList().remove( cambio );
                fkMoeda = em.merge( fkMoeda );
            }
            em.remove( cambio );
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

    public List<Cambio> findCambioEntities()
    {
        return findCambioEntities( true, -1, -1 );
    }

    public List<Cambio> findCambioEntities( int maxResults, int firstResult )
    {
        return findCambioEntities( false, maxResults, firstResult );
    }

    private List<Cambio> findCambioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Cambio.class ) );
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

    public Cambio findCambio( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Cambio.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getCambioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cambio> rt = cq.from( Cambio.class );
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
