/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.AnoEconomico;
import entity.TbArmazem;
import entity.Documento;
import entity.NotasCompras;
import entity.TbUsuario;
import entity.TbBanco;
import entity.TbFornecedor;
import entity.NotasItemCompras;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class NotasComprasJpaController implements Serializable
{

    public NotasComprasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( NotasCompras notasCompras )
    {
        if ( notasCompras.getNotasItemComprasList() == null )
        {
            notasCompras.setNotasItemComprasList( new ArrayList<NotasItemCompras>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            AnoEconomico fkAnoEconomico = notasCompras.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico = em.getReference( fkAnoEconomico.getClass(), fkAnoEconomico.getPkAnoEconomico() );
                notasCompras.setFkAnoEconomico( fkAnoEconomico );
            }
            TbArmazem idArmazemFK = notasCompras.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                notasCompras.setIdArmazemFK( idArmazemFK );
            }
            Documento fkDocumento = notasCompras.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento = em.getReference( fkDocumento.getClass(), fkDocumento.getPkDocumento() );
                notasCompras.setFkDocumento( fkDocumento );
            }
            TbUsuario codigoUsuario = notasCompras.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario = em.getReference( codigoUsuario.getClass(), codigoUsuario.getCodigo() );
                notasCompras.setCodigoUsuario( codigoUsuario );
            }
            TbBanco idBanco = notasCompras.getIdBanco();
            if ( idBanco != null )
            {
                idBanco = em.getReference( idBanco.getClass(), idBanco.getIdBanco() );
                notasCompras.setIdBanco( idBanco );
            }
            TbFornecedor fkFornecedor = notasCompras.getFkFornecedor();
            if ( fkFornecedor != null )
            {
                fkFornecedor = em.getReference( fkFornecedor.getClass(), fkFornecedor.getCodigo() );
                notasCompras.setFkFornecedor( fkFornecedor );
            }
            List<NotasItemCompras> attachedNotasItemComprasList = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNotasItemComprasToAttach : notasCompras.getNotasItemComprasList() )
            {
                notasItemComprasListNotasItemComprasToAttach = em.getReference( notasItemComprasListNotasItemComprasToAttach.getClass(), notasItemComprasListNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasList.add( notasItemComprasListNotasItemComprasToAttach );
            }
            notasCompras.setNotasItemComprasList( attachedNotasItemComprasList );
            em.persist( notasCompras );
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getNotasComprasList().add( notasCompras );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            if ( idArmazemFK != null )
            {
                idArmazemFK.getNotasComprasList().add( notasCompras );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( fkDocumento != null )
            {
                fkDocumento.getNotasComprasList().add( notasCompras );
                fkDocumento = em.merge( fkDocumento );
            }
            if ( codigoUsuario != null )
            {
                codigoUsuario.getNotasComprasList().add( notasCompras );
                codigoUsuario = em.merge( codigoUsuario );
            }
            if ( idBanco != null )
            {
                idBanco.getNotasComprasList().add( notasCompras );
                idBanco = em.merge( idBanco );
            }
            if ( fkFornecedor != null )
            {
                fkFornecedor.getNotasComprasList().add( notasCompras );
                fkFornecedor = em.merge( fkFornecedor );
            }
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : notasCompras.getNotasItemComprasList() )
            {
                NotasCompras oldFkNotaComprasOfNotasItemComprasListNotasItemCompras = notasItemComprasListNotasItemCompras.getFkNotaCompras();
                notasItemComprasListNotasItemCompras.setFkNotaCompras( notasCompras );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
                if ( oldFkNotaComprasOfNotasItemComprasListNotasItemCompras != null )
                {
                    oldFkNotaComprasOfNotasItemComprasListNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNotasItemCompras );
                    oldFkNotaComprasOfNotasItemComprasListNotasItemCompras = em.merge( oldFkNotaComprasOfNotasItemComprasListNotasItemCompras );
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

    public void edit( NotasCompras notasCompras ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            NotasCompras persistentNotasCompras = em.find( NotasCompras.class, notasCompras.getPkNotaCompras() );
            AnoEconomico fkAnoEconomicoOld = persistentNotasCompras.getFkAnoEconomico();
            AnoEconomico fkAnoEconomicoNew = notasCompras.getFkAnoEconomico();
            TbArmazem idArmazemFKOld = persistentNotasCompras.getIdArmazemFK();
            TbArmazem idArmazemFKNew = notasCompras.getIdArmazemFK();
            Documento fkDocumentoOld = persistentNotasCompras.getFkDocumento();
            Documento fkDocumentoNew = notasCompras.getFkDocumento();
            TbUsuario codigoUsuarioOld = persistentNotasCompras.getCodigoUsuario();
            TbUsuario codigoUsuarioNew = notasCompras.getCodigoUsuario();
            TbBanco idBancoOld = persistentNotasCompras.getIdBanco();
            TbBanco idBancoNew = notasCompras.getIdBanco();
            TbFornecedor fkFornecedorOld = persistentNotasCompras.getFkFornecedor();
            TbFornecedor fkFornecedorNew = notasCompras.getFkFornecedor();
            List<NotasItemCompras> notasItemComprasListOld = persistentNotasCompras.getNotasItemComprasList();
            List<NotasItemCompras> notasItemComprasListNew = notasCompras.getNotasItemComprasList();
            if ( fkAnoEconomicoNew != null )
            {
                fkAnoEconomicoNew = em.getReference( fkAnoEconomicoNew.getClass(), fkAnoEconomicoNew.getPkAnoEconomico() );
                notasCompras.setFkAnoEconomico( fkAnoEconomicoNew );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                notasCompras.setIdArmazemFK( idArmazemFKNew );
            }
            if ( fkDocumentoNew != null )
            {
                fkDocumentoNew = em.getReference( fkDocumentoNew.getClass(), fkDocumentoNew.getPkDocumento() );
                notasCompras.setFkDocumento( fkDocumentoNew );
            }
            if ( codigoUsuarioNew != null )
            {
                codigoUsuarioNew = em.getReference( codigoUsuarioNew.getClass(), codigoUsuarioNew.getCodigo() );
                notasCompras.setCodigoUsuario( codigoUsuarioNew );
            }
            if ( idBancoNew != null )
            {
                idBancoNew = em.getReference( idBancoNew.getClass(), idBancoNew.getIdBanco() );
                notasCompras.setIdBanco( idBancoNew );
            }
            if ( fkFornecedorNew != null )
            {
                fkFornecedorNew = em.getReference( fkFornecedorNew.getClass(), fkFornecedorNew.getCodigo() );
                notasCompras.setFkFornecedor( fkFornecedorNew );
            }
            List<NotasItemCompras> attachedNotasItemComprasListNew = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNewNotasItemComprasToAttach : notasItemComprasListNew )
            {
                notasItemComprasListNewNotasItemComprasToAttach = em.getReference( notasItemComprasListNewNotasItemComprasToAttach.getClass(), notasItemComprasListNewNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasListNew.add( notasItemComprasListNewNotasItemComprasToAttach );
            }
            notasItemComprasListNew = attachedNotasItemComprasListNew;
            notasCompras.setNotasItemComprasList( notasItemComprasListNew );
            notasCompras = em.merge( notasCompras );
            if ( fkAnoEconomicoOld != null && !fkAnoEconomicoOld.equals( fkAnoEconomicoNew ) )
            {
                fkAnoEconomicoOld.getNotasComprasList().remove( notasCompras );
                fkAnoEconomicoOld = em.merge( fkAnoEconomicoOld );
            }
            if ( fkAnoEconomicoNew != null && !fkAnoEconomicoNew.equals( fkAnoEconomicoOld ) )
            {
                fkAnoEconomicoNew.getNotasComprasList().add( notasCompras );
                fkAnoEconomicoNew = em.merge( fkAnoEconomicoNew );
            }
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getNotasComprasList().remove( notasCompras );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getNotasComprasList().add( notasCompras );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( fkDocumentoOld != null && !fkDocumentoOld.equals( fkDocumentoNew ) )
            {
                fkDocumentoOld.getNotasComprasList().remove( notasCompras );
                fkDocumentoOld = em.merge( fkDocumentoOld );
            }
            if ( fkDocumentoNew != null && !fkDocumentoNew.equals( fkDocumentoOld ) )
            {
                fkDocumentoNew.getNotasComprasList().add( notasCompras );
                fkDocumentoNew = em.merge( fkDocumentoNew );
            }
            if ( codigoUsuarioOld != null && !codigoUsuarioOld.equals( codigoUsuarioNew ) )
            {
                codigoUsuarioOld.getNotasComprasList().remove( notasCompras );
                codigoUsuarioOld = em.merge( codigoUsuarioOld );
            }
            if ( codigoUsuarioNew != null && !codigoUsuarioNew.equals( codigoUsuarioOld ) )
            {
                codigoUsuarioNew.getNotasComprasList().add( notasCompras );
                codigoUsuarioNew = em.merge( codigoUsuarioNew );
            }
            if ( idBancoOld != null && !idBancoOld.equals( idBancoNew ) )
            {
                idBancoOld.getNotasComprasList().remove( notasCompras );
                idBancoOld = em.merge( idBancoOld );
            }
            if ( idBancoNew != null && !idBancoNew.equals( idBancoOld ) )
            {
                idBancoNew.getNotasComprasList().add( notasCompras );
                idBancoNew = em.merge( idBancoNew );
            }
            if ( fkFornecedorOld != null && !fkFornecedorOld.equals( fkFornecedorNew ) )
            {
                fkFornecedorOld.getNotasComprasList().remove( notasCompras );
                fkFornecedorOld = em.merge( fkFornecedorOld );
            }
            if ( fkFornecedorNew != null && !fkFornecedorNew.equals( fkFornecedorOld ) )
            {
                fkFornecedorNew.getNotasComprasList().add( notasCompras );
                fkFornecedorNew = em.merge( fkFornecedorNew );
            }
            for ( NotasItemCompras notasItemComprasListOldNotasItemCompras : notasItemComprasListOld )
            {
                if ( !notasItemComprasListNew.contains( notasItemComprasListOldNotasItemCompras ) )
                {
                    notasItemComprasListOldNotasItemCompras.setFkNotaCompras( null );
                    notasItemComprasListOldNotasItemCompras = em.merge( notasItemComprasListOldNotasItemCompras );
                }
            }
            for ( NotasItemCompras notasItemComprasListNewNotasItemCompras : notasItemComprasListNew )
            {
                if ( !notasItemComprasListOld.contains( notasItemComprasListNewNotasItemCompras ) )
                {
                    NotasCompras oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras = notasItemComprasListNewNotasItemCompras.getFkNotaCompras();
                    notasItemComprasListNewNotasItemCompras.setFkNotaCompras( notasCompras );
                    notasItemComprasListNewNotasItemCompras = em.merge( notasItemComprasListNewNotasItemCompras );
                    if ( oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras != null && !oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras.equals( notasCompras ) )
                    {
                        oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNewNotasItemCompras );
                        oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras = em.merge( oldFkNotaComprasOfNotasItemComprasListNewNotasItemCompras );
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
                Integer id = notasCompras.getPkNotaCompras();
                if ( findNotasCompras( id ) == null )
                {
                    throw new NonexistentEntityException( "The notasCompras with id " + id + " no longer exists." );
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
            NotasCompras notasCompras;
            try
            {
                notasCompras = em.getReference( NotasCompras.class, id );
                notasCompras.getPkNotaCompras();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The notasCompras with id " + id + " no longer exists.", enfe );
            }
            AnoEconomico fkAnoEconomico = notasCompras.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getNotasComprasList().remove( notasCompras );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            TbArmazem idArmazemFK = notasCompras.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getNotasComprasList().remove( notasCompras );
                idArmazemFK = em.merge( idArmazemFK );
            }
            Documento fkDocumento = notasCompras.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento.getNotasComprasList().remove( notasCompras );
                fkDocumento = em.merge( fkDocumento );
            }
            TbUsuario codigoUsuario = notasCompras.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario.getNotasComprasList().remove( notasCompras );
                codigoUsuario = em.merge( codigoUsuario );
            }
            TbBanco idBanco = notasCompras.getIdBanco();
            if ( idBanco != null )
            {
                idBanco.getNotasComprasList().remove( notasCompras );
                idBanco = em.merge( idBanco );
            }
            TbFornecedor fkFornecedor = notasCompras.getFkFornecedor();
            if ( fkFornecedor != null )
            {
                fkFornecedor.getNotasComprasList().remove( notasCompras );
                fkFornecedor = em.merge( fkFornecedor );
            }
            List<NotasItemCompras> notasItemComprasList = notasCompras.getNotasItemComprasList();
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : notasItemComprasList )
            {
                notasItemComprasListNotasItemCompras.setFkNotaCompras( null );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
            }
            em.remove( notasCompras );
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

    public List<NotasCompras> findNotasComprasEntities()
    {
        return findNotasComprasEntities( true, -1, -1 );
    }

    public List<NotasCompras> findNotasComprasEntities( int maxResults, int firstResult )
    {
        return findNotasComprasEntities( false, maxResults, firstResult );
    }

    private List<NotasCompras> findNotasComprasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( NotasCompras.class ) );
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

    public NotasCompras findNotasCompras( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( NotasCompras.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getNotasComprasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NotasCompras> rt = cq.from( NotasCompras.class );
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
