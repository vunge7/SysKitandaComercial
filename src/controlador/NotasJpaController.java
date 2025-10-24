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
import entity.TbBanco;
import entity.TbArmazem;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbCliente;
import entity.TbUsuario;
import entity.ItensNota;
import entity.Notas;
import java.util.ArrayList;
import java.util.List;
import entity.NotasItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class NotasJpaController implements Serializable
{

    public NotasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Notas notas )
    {
        if ( notas.getItensNotaList() == null )
        {
            notas.setItensNotaList( new ArrayList<ItensNota>() );
        }
        if ( notas.getNotasItemList() == null )
        {
            notas.setNotasItemList( new ArrayList<NotasItem>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco idBanco = notas.getIdBanco();
            if ( idBanco != null )
            {
                idBanco = em.getReference( idBanco.getClass(), idBanco.getIdBanco() );
                notas.setIdBanco( idBanco );
            }
            TbArmazem idArmazemFK = notas.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                notas.setIdArmazemFK( idArmazemFK );
            }
            AnoEconomico fkAnoEconomico = notas.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico = em.getReference( fkAnoEconomico.getClass(), fkAnoEconomico.getPkAnoEconomico() );
                notas.setFkAnoEconomico( fkAnoEconomico );
            }
            Cambio fkCambio = notas.getFkCambio();
            if ( fkCambio != null )
            {
                fkCambio = em.getReference( fkCambio.getClass(), fkCambio.getPkCambio() );
                notas.setFkCambio( fkCambio );
            }
            Documento fkDocumento = notas.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento = em.getReference( fkDocumento.getClass(), fkDocumento.getPkDocumento() );
                notas.setFkDocumento( fkDocumento );
            }
            TbCliente codigoCliente = notas.getCodigoCliente();
            if ( codigoCliente != null )
            {
                codigoCliente = em.getReference( codigoCliente.getClass(), codigoCliente.getCodigo() );
                notas.setCodigoCliente( codigoCliente );
            }
            TbUsuario codigoUsuario = notas.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario = em.getReference( codigoUsuario.getClass(), codigoUsuario.getCodigo() );
                notas.setCodigoUsuario( codigoUsuario );
            }
            List<ItensNota> attachedItensNotaList = new ArrayList<ItensNota>();
            for ( ItensNota itensNotaListItensNotaToAttach : notas.getItensNotaList() )
            {
                itensNotaListItensNotaToAttach = em.getReference( itensNotaListItensNotaToAttach.getClass(), itensNotaListItensNotaToAttach.getPkItensNota() );
                attachedItensNotaList.add( itensNotaListItensNotaToAttach );
            }
            notas.setItensNotaList( attachedItensNotaList );
            List<NotasItem> attachedNotasItemList = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNotasItemToAttach : notas.getNotasItemList() )
            {
                notasItemListNotasItemToAttach = em.getReference( notasItemListNotasItemToAttach.getClass(), notasItemListNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemList.add( notasItemListNotasItemToAttach );
            }
            notas.setNotasItemList( attachedNotasItemList );
            em.persist( notas );
            if ( idBanco != null )
            {
                idBanco.getNotasList().add( notas );
                idBanco = em.merge( idBanco );
            }
            if ( idArmazemFK != null )
            {
                idArmazemFK.getNotasList().add( notas );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getNotasList().add( notas );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            if ( fkCambio != null )
            {
                fkCambio.getNotasList().add( notas );
                fkCambio = em.merge( fkCambio );
            }
            if ( fkDocumento != null )
            {
                fkDocumento.getNotasList().add( notas );
                fkDocumento = em.merge( fkDocumento );
            }
            if ( codigoCliente != null )
            {
                codigoCliente.getNotasList().add( notas );
                codigoCliente = em.merge( codigoCliente );
            }
            if ( codigoUsuario != null )
            {
                codigoUsuario.getNotasList().add( notas );
                codigoUsuario = em.merge( codigoUsuario );
            }
            for ( ItensNota itensNotaListItensNota : notas.getItensNotaList() )
            {
                Notas oldFkNotaOfItensNotaListItensNota = itensNotaListItensNota.getFkNota();
                itensNotaListItensNota.setFkNota( notas );
                itensNotaListItensNota = em.merge( itensNotaListItensNota );
                if ( oldFkNotaOfItensNotaListItensNota != null )
                {
                    oldFkNotaOfItensNotaListItensNota.getItensNotaList().remove( itensNotaListItensNota );
                    oldFkNotaOfItensNotaListItensNota = em.merge( oldFkNotaOfItensNotaListItensNota );
                }
            }
            for ( NotasItem notasItemListNotasItem : notas.getNotasItemList() )
            {
                Notas oldFkNotaOfNotasItemListNotasItem = notasItemListNotasItem.getFkNota();
                notasItemListNotasItem.setFkNota( notas );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
                if ( oldFkNotaOfNotasItemListNotasItem != null )
                {
                    oldFkNotaOfNotasItemListNotasItem.getNotasItemList().remove( notasItemListNotasItem );
                    oldFkNotaOfNotasItemListNotasItem = em.merge( oldFkNotaOfNotasItemListNotasItem );
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

    public void edit( Notas notas ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Notas persistentNotas = em.find( Notas.class, notas.getPkNota() );
            TbBanco idBancoOld = persistentNotas.getIdBanco();
            TbBanco idBancoNew = notas.getIdBanco();
            TbArmazem idArmazemFKOld = persistentNotas.getIdArmazemFK();
            TbArmazem idArmazemFKNew = notas.getIdArmazemFK();
            AnoEconomico fkAnoEconomicoOld = persistentNotas.getFkAnoEconomico();
            AnoEconomico fkAnoEconomicoNew = notas.getFkAnoEconomico();
            Cambio fkCambioOld = persistentNotas.getFkCambio();
            Cambio fkCambioNew = notas.getFkCambio();
            Documento fkDocumentoOld = persistentNotas.getFkDocumento();
            Documento fkDocumentoNew = notas.getFkDocumento();
            TbCliente codigoClienteOld = persistentNotas.getCodigoCliente();
            TbCliente codigoClienteNew = notas.getCodigoCliente();
            TbUsuario codigoUsuarioOld = persistentNotas.getCodigoUsuario();
            TbUsuario codigoUsuarioNew = notas.getCodigoUsuario();
            List<ItensNota> itensNotaListOld = persistentNotas.getItensNotaList();
            List<ItensNota> itensNotaListNew = notas.getItensNotaList();
            List<NotasItem> notasItemListOld = persistentNotas.getNotasItemList();
            List<NotasItem> notasItemListNew = notas.getNotasItemList();
            List<String> illegalOrphanMessages = null;
            for ( ItensNota itensNotaListOldItensNota : itensNotaListOld )
            {
                if ( !itensNotaListNew.contains( itensNotaListOldItensNota ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItensNota " + itensNotaListOldItensNota + " since its fkNota field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idBancoNew != null )
            {
                idBancoNew = em.getReference( idBancoNew.getClass(), idBancoNew.getIdBanco() );
                notas.setIdBanco( idBancoNew );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                notas.setIdArmazemFK( idArmazemFKNew );
            }
            if ( fkAnoEconomicoNew != null )
            {
                fkAnoEconomicoNew = em.getReference( fkAnoEconomicoNew.getClass(), fkAnoEconomicoNew.getPkAnoEconomico() );
                notas.setFkAnoEconomico( fkAnoEconomicoNew );
            }
            if ( fkCambioNew != null )
            {
                fkCambioNew = em.getReference( fkCambioNew.getClass(), fkCambioNew.getPkCambio() );
                notas.setFkCambio( fkCambioNew );
            }
            if ( fkDocumentoNew != null )
            {
                fkDocumentoNew = em.getReference( fkDocumentoNew.getClass(), fkDocumentoNew.getPkDocumento() );
                notas.setFkDocumento( fkDocumentoNew );
            }
            if ( codigoClienteNew != null )
            {
                codigoClienteNew = em.getReference( codigoClienteNew.getClass(), codigoClienteNew.getCodigo() );
                notas.setCodigoCliente( codigoClienteNew );
            }
            if ( codigoUsuarioNew != null )
            {
                codigoUsuarioNew = em.getReference( codigoUsuarioNew.getClass(), codigoUsuarioNew.getCodigo() );
                notas.setCodigoUsuario( codigoUsuarioNew );
            }
            List<ItensNota> attachedItensNotaListNew = new ArrayList<ItensNota>();
            for ( ItensNota itensNotaListNewItensNotaToAttach : itensNotaListNew )
            {
                itensNotaListNewItensNotaToAttach = em.getReference( itensNotaListNewItensNotaToAttach.getClass(), itensNotaListNewItensNotaToAttach.getPkItensNota() );
                attachedItensNotaListNew.add( itensNotaListNewItensNotaToAttach );
            }
            itensNotaListNew = attachedItensNotaListNew;
            notas.setItensNotaList( itensNotaListNew );
            List<NotasItem> attachedNotasItemListNew = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNewNotasItemToAttach : notasItemListNew )
            {
                notasItemListNewNotasItemToAttach = em.getReference( notasItemListNewNotasItemToAttach.getClass(), notasItemListNewNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemListNew.add( notasItemListNewNotasItemToAttach );
            }
            notasItemListNew = attachedNotasItemListNew;
            notas.setNotasItemList( notasItemListNew );
            notas = em.merge( notas );
            if ( idBancoOld != null && !idBancoOld.equals( idBancoNew ) )
            {
                idBancoOld.getNotasList().remove( notas );
                idBancoOld = em.merge( idBancoOld );
            }
            if ( idBancoNew != null && !idBancoNew.equals( idBancoOld ) )
            {
                idBancoNew.getNotasList().add( notas );
                idBancoNew = em.merge( idBancoNew );
            }
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getNotasList().remove( notas );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getNotasList().add( notas );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( fkAnoEconomicoOld != null && !fkAnoEconomicoOld.equals( fkAnoEconomicoNew ) )
            {
                fkAnoEconomicoOld.getNotasList().remove( notas );
                fkAnoEconomicoOld = em.merge( fkAnoEconomicoOld );
            }
            if ( fkAnoEconomicoNew != null && !fkAnoEconomicoNew.equals( fkAnoEconomicoOld ) )
            {
                fkAnoEconomicoNew.getNotasList().add( notas );
                fkAnoEconomicoNew = em.merge( fkAnoEconomicoNew );
            }
            if ( fkCambioOld != null && !fkCambioOld.equals( fkCambioNew ) )
            {
                fkCambioOld.getNotasList().remove( notas );
                fkCambioOld = em.merge( fkCambioOld );
            }
            if ( fkCambioNew != null && !fkCambioNew.equals( fkCambioOld ) )
            {
                fkCambioNew.getNotasList().add( notas );
                fkCambioNew = em.merge( fkCambioNew );
            }
            if ( fkDocumentoOld != null && !fkDocumentoOld.equals( fkDocumentoNew ) )
            {
                fkDocumentoOld.getNotasList().remove( notas );
                fkDocumentoOld = em.merge( fkDocumentoOld );
            }
            if ( fkDocumentoNew != null && !fkDocumentoNew.equals( fkDocumentoOld ) )
            {
                fkDocumentoNew.getNotasList().add( notas );
                fkDocumentoNew = em.merge( fkDocumentoNew );
            }
            if ( codigoClienteOld != null && !codigoClienteOld.equals( codigoClienteNew ) )
            {
                codigoClienteOld.getNotasList().remove( notas );
                codigoClienteOld = em.merge( codigoClienteOld );
            }
            if ( codigoClienteNew != null && !codigoClienteNew.equals( codigoClienteOld ) )
            {
                codigoClienteNew.getNotasList().add( notas );
                codigoClienteNew = em.merge( codigoClienteNew );
            }
            if ( codigoUsuarioOld != null && !codigoUsuarioOld.equals( codigoUsuarioNew ) )
            {
                codigoUsuarioOld.getNotasList().remove( notas );
                codigoUsuarioOld = em.merge( codigoUsuarioOld );
            }
            if ( codigoUsuarioNew != null && !codigoUsuarioNew.equals( codigoUsuarioOld ) )
            {
                codigoUsuarioNew.getNotasList().add( notas );
                codigoUsuarioNew = em.merge( codigoUsuarioNew );
            }
            for ( ItensNota itensNotaListNewItensNota : itensNotaListNew )
            {
                if ( !itensNotaListOld.contains( itensNotaListNewItensNota ) )
                {
                    Notas oldFkNotaOfItensNotaListNewItensNota = itensNotaListNewItensNota.getFkNota();
                    itensNotaListNewItensNota.setFkNota( notas );
                    itensNotaListNewItensNota = em.merge( itensNotaListNewItensNota );
                    if ( oldFkNotaOfItensNotaListNewItensNota != null && !oldFkNotaOfItensNotaListNewItensNota.equals( notas ) )
                    {
                        oldFkNotaOfItensNotaListNewItensNota.getItensNotaList().remove( itensNotaListNewItensNota );
                        oldFkNotaOfItensNotaListNewItensNota = em.merge( oldFkNotaOfItensNotaListNewItensNota );
                    }
                }
            }
            for ( NotasItem notasItemListOldNotasItem : notasItemListOld )
            {
                if ( !notasItemListNew.contains( notasItemListOldNotasItem ) )
                {
                    notasItemListOldNotasItem.setFkNota( null );
                    notasItemListOldNotasItem = em.merge( notasItemListOldNotasItem );
                }
            }
            for ( NotasItem notasItemListNewNotasItem : notasItemListNew )
            {
                if ( !notasItemListOld.contains( notasItemListNewNotasItem ) )
                {
                    Notas oldFkNotaOfNotasItemListNewNotasItem = notasItemListNewNotasItem.getFkNota();
                    notasItemListNewNotasItem.setFkNota( notas );
                    notasItemListNewNotasItem = em.merge( notasItemListNewNotasItem );
                    if ( oldFkNotaOfNotasItemListNewNotasItem != null && !oldFkNotaOfNotasItemListNewNotasItem.equals( notas ) )
                    {
                        oldFkNotaOfNotasItemListNewNotasItem.getNotasItemList().remove( notasItemListNewNotasItem );
                        oldFkNotaOfNotasItemListNewNotasItem = em.merge( oldFkNotaOfNotasItemListNewNotasItem );
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
                Integer id = notas.getPkNota();
                if ( findNotas( id ) == null )
                {
                    throw new NonexistentEntityException( "The notas with id " + id + " no longer exists." );
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
            Notas notas;
            try
            {
                notas = em.getReference( Notas.class, id );
                notas.getPkNota();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The notas with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItensNota> itensNotaListOrphanCheck = notas.getItensNotaList();
            for ( ItensNota itensNotaListOrphanCheckItensNota : itensNotaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Notas (" + notas + ") cannot be destroyed since the ItensNota " + itensNotaListOrphanCheckItensNota + " in its itensNotaList field has a non-nullable fkNota field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbBanco idBanco = notas.getIdBanco();
            if ( idBanco != null )
            {
                idBanco.getNotasList().remove( notas );
                idBanco = em.merge( idBanco );
            }
            TbArmazem idArmazemFK = notas.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getNotasList().remove( notas );
                idArmazemFK = em.merge( idArmazemFK );
            }
            AnoEconomico fkAnoEconomico = notas.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getNotasList().remove( notas );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            Cambio fkCambio = notas.getFkCambio();
            if ( fkCambio != null )
            {
                fkCambio.getNotasList().remove( notas );
                fkCambio = em.merge( fkCambio );
            }
            Documento fkDocumento = notas.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento.getNotasList().remove( notas );
                fkDocumento = em.merge( fkDocumento );
            }
            TbCliente codigoCliente = notas.getCodigoCliente();
            if ( codigoCliente != null )
            {
                codigoCliente.getNotasList().remove( notas );
                codigoCliente = em.merge( codigoCliente );
            }
            TbUsuario codigoUsuario = notas.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario.getNotasList().remove( notas );
                codigoUsuario = em.merge( codigoUsuario );
            }
            List<NotasItem> notasItemList = notas.getNotasItemList();
            for ( NotasItem notasItemListNotasItem : notasItemList )
            {
                notasItemListNotasItem.setFkNota( null );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
            }
            em.remove( notas );
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

    public List<Notas> findNotasEntities()
    {
        return findNotasEntities( true, -1, -1 );
    }

    public List<Notas> findNotasEntities( int maxResults, int firstResult )
    {
        return findNotasEntities( false, maxResults, firstResult );
    }

    private List<Notas> findNotasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Notas.class ) );
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

    public Notas findNotas( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Notas.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getNotasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notas> rt = cq.from( Notas.class );
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
