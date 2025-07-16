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
import entity.NotasCompras;
import java.util.ArrayList;
import java.util.List;
import entity.Notas;
import entity.Compras;
import entity.Documento;
import entity.TbVenda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class DocumentoJpaController implements Serializable
{

    public DocumentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Documento documento )
    {
        if ( documento.getNotasComprasList() == null )
        {
            documento.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( documento.getNotasList() == null )
        {
            documento.setNotasList( new ArrayList<Notas>() );
        }
        if ( documento.getComprasList() == null )
        {
            documento.setComprasList( new ArrayList<Compras>() );
        }
        if ( documento.getTbVendaList() == null )
        {
            documento.setTbVendaList( new ArrayList<TbVenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : documento.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            documento.setNotasComprasList( attachedNotasComprasList );
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : documento.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            documento.setNotasList( attachedNotasList );
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for ( Compras comprasListComprasToAttach : documento.getComprasList() )
            {
                comprasListComprasToAttach = em.getReference( comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getPkCompra() );
                attachedComprasList.add( comprasListComprasToAttach );
            }
            documento.setComprasList( attachedComprasList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : documento.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            documento.setTbVendaList( attachedTbVendaList );
            em.persist( documento );
            for ( NotasCompras notasComprasListNotasCompras : documento.getNotasComprasList() )
            {
                Documento oldFkDocumentoOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getFkDocumento();
                notasComprasListNotasCompras.setFkDocumento( documento );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldFkDocumentoOfNotasComprasListNotasCompras != null )
                {
                    oldFkDocumentoOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldFkDocumentoOfNotasComprasListNotasCompras = em.merge( oldFkDocumentoOfNotasComprasListNotasCompras );
                }
            }
            for ( Notas notasListNotas : documento.getNotasList() )
            {
                Documento oldFkDocumentoOfNotasListNotas = notasListNotas.getFkDocumento();
                notasListNotas.setFkDocumento( documento );
                notasListNotas = em.merge( notasListNotas );
                if ( oldFkDocumentoOfNotasListNotas != null )
                {
                    oldFkDocumentoOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldFkDocumentoOfNotasListNotas = em.merge( oldFkDocumentoOfNotasListNotas );
                }
            }
            for ( Compras comprasListCompras : documento.getComprasList() )
            {
                Documento oldFkDocumentoOfComprasListCompras = comprasListCompras.getFkDocumento();
                comprasListCompras.setFkDocumento( documento );
                comprasListCompras = em.merge( comprasListCompras );
                if ( oldFkDocumentoOfComprasListCompras != null )
                {
                    oldFkDocumentoOfComprasListCompras.getComprasList().remove( comprasListCompras );
                    oldFkDocumentoOfComprasListCompras = em.merge( oldFkDocumentoOfComprasListCompras );
                }
            }
            for ( TbVenda tbVendaListTbVenda : documento.getTbVendaList() )
            {
                Documento oldFkDocumentoOfTbVendaListTbVenda = tbVendaListTbVenda.getFkDocumento();
                tbVendaListTbVenda.setFkDocumento( documento );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldFkDocumentoOfTbVendaListTbVenda != null )
                {
                    oldFkDocumentoOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldFkDocumentoOfTbVendaListTbVenda = em.merge( oldFkDocumentoOfTbVendaListTbVenda );
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

    public void edit( Documento documento ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento persistentDocumento = em.find( Documento.class, documento.getPkDocumento() );
            List<NotasCompras> notasComprasListOld = persistentDocumento.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = documento.getNotasComprasList();
            List<Notas> notasListOld = persistentDocumento.getNotasList();
            List<Notas> notasListNew = documento.getNotasList();
            List<Compras> comprasListOld = persistentDocumento.getComprasList();
            List<Compras> comprasListNew = documento.getComprasList();
            List<TbVenda> tbVendaListOld = persistentDocumento.getTbVendaList();
            List<TbVenda> tbVendaListNew = documento.getTbVendaList();
            List<String> illegalOrphanMessages = null;
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its fkDocumento field is not nullable." );
                }
            }
            for ( Compras comprasListOldCompras : comprasListOld )
            {
                if ( !comprasListNew.contains( comprasListOldCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Compras " + comprasListOldCompras + " since its fkDocumento field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its fkDocumento field is not nullable." );
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
            documento.setNotasComprasList( notasComprasListNew );
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            documento.setNotasList( notasListNew );
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for ( Compras comprasListNewComprasToAttach : comprasListNew )
            {
                comprasListNewComprasToAttach = em.getReference( comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getPkCompra() );
                attachedComprasListNew.add( comprasListNewComprasToAttach );
            }
            comprasListNew = attachedComprasListNew;
            documento.setComprasList( comprasListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            documento.setTbVendaList( tbVendaListNew );
            documento = em.merge( documento );
            for ( NotasCompras notasComprasListOldNotasCompras : notasComprasListOld )
            {
                if ( !notasComprasListNew.contains( notasComprasListOldNotasCompras ) )
                {
                    notasComprasListOldNotasCompras.setFkDocumento( null );
                    notasComprasListOldNotasCompras = em.merge( notasComprasListOldNotasCompras );
                }
            }
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    Documento oldFkDocumentoOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getFkDocumento();
                    notasComprasListNewNotasCompras.setFkDocumento( documento );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldFkDocumentoOfNotasComprasListNewNotasCompras != null && !oldFkDocumentoOfNotasComprasListNewNotasCompras.equals( documento ) )
                    {
                        oldFkDocumentoOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldFkDocumentoOfNotasComprasListNewNotasCompras = em.merge( oldFkDocumentoOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    Documento oldFkDocumentoOfNotasListNewNotas = notasListNewNotas.getFkDocumento();
                    notasListNewNotas.setFkDocumento( documento );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldFkDocumentoOfNotasListNewNotas != null && !oldFkDocumentoOfNotasListNewNotas.equals( documento ) )
                    {
                        oldFkDocumentoOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldFkDocumentoOfNotasListNewNotas = em.merge( oldFkDocumentoOfNotasListNewNotas );
                    }
                }
            }
            for ( Compras comprasListNewCompras : comprasListNew )
            {
                if ( !comprasListOld.contains( comprasListNewCompras ) )
                {
                    Documento oldFkDocumentoOfComprasListNewCompras = comprasListNewCompras.getFkDocumento();
                    comprasListNewCompras.setFkDocumento( documento );
                    comprasListNewCompras = em.merge( comprasListNewCompras );
                    if ( oldFkDocumentoOfComprasListNewCompras != null && !oldFkDocumentoOfComprasListNewCompras.equals( documento ) )
                    {
                        oldFkDocumentoOfComprasListNewCompras.getComprasList().remove( comprasListNewCompras );
                        oldFkDocumentoOfComprasListNewCompras = em.merge( oldFkDocumentoOfComprasListNewCompras );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    Documento oldFkDocumentoOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getFkDocumento();
                    tbVendaListNewTbVenda.setFkDocumento( documento );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldFkDocumentoOfTbVendaListNewTbVenda != null && !oldFkDocumentoOfTbVendaListNewTbVenda.equals( documento ) )
                    {
                        oldFkDocumentoOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldFkDocumentoOfTbVendaListNewTbVenda = em.merge( oldFkDocumentoOfTbVendaListNewTbVenda );
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
                Integer id = documento.getPkDocumento();
                if ( findDocumento( id ) == null )
                {
                    throw new NonexistentEntityException( "The documento with id " + id + " no longer exists." );
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
            Documento documento;
            try
            {
                documento = em.getReference( Documento.class, id );
                documento.getPkDocumento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The documento with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Notas> notasListOrphanCheck = documento.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Documento (" + documento + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable fkDocumento field." );
            }
            List<Compras> comprasListOrphanCheck = documento.getComprasList();
            for ( Compras comprasListOrphanCheckCompras : comprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Documento (" + documento + ") cannot be destroyed since the Compras " + comprasListOrphanCheckCompras + " in its comprasList field has a non-nullable fkDocumento field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = documento.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Documento (" + documento + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable fkDocumento field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<NotasCompras> notasComprasList = documento.getNotasComprasList();
            for ( NotasCompras notasComprasListNotasCompras : notasComprasList )
            {
                notasComprasListNotasCompras.setFkDocumento( null );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
            }
            em.remove( documento );
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

    public List<Documento> findDocumentoEntities()
    {
        return findDocumentoEntities( true, -1, -1 );
    }

    public List<Documento> findDocumentoEntities( int maxResults, int firstResult )
    {
        return findDocumentoEntities( false, maxResults, firstResult );
    }

    private List<Documento> findDocumentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Documento.class ) );
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

    public Documento findDocumento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Documento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getDocumentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from( Documento.class );
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
