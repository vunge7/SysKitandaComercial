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
import entity.Documento;
import entity.TbFornecedor;
import entity.AnoEconomico;
import entity.Compras;
import entity.TbUsuario;
import entity.TbArmazem;
import entity.ItemCompras;
import java.util.ArrayList;
import java.util.List;
import entity.NotasItemCompras;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ComprasJpaController implements Serializable
{

    public ComprasJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Compras compras )
    {
        if ( compras.getItemComprasList() == null )
        {
            compras.setItemComprasList( new ArrayList<ItemCompras>() );
        }
        if ( compras.getNotasItemComprasList() == null )
        {
            compras.setNotasItemComprasList( new ArrayList<NotasItemCompras>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento fkDocumento = compras.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento = em.getReference( fkDocumento.getClass(), fkDocumento.getPkDocumento() );
                compras.setFkDocumento( fkDocumento );
            }
            TbFornecedor fkFornecedor = compras.getFkFornecedor();
            if ( fkFornecedor != null )
            {
                fkFornecedor = em.getReference( fkFornecedor.getClass(), fkFornecedor.getCodigo() );
                compras.setFkFornecedor( fkFornecedor );
            }
            AnoEconomico fkAnoEconomico = compras.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico = em.getReference( fkAnoEconomico.getClass(), fkAnoEconomico.getPkAnoEconomico() );
                compras.setFkAnoEconomico( fkAnoEconomico );
            }
            TbUsuario codigoUsuario = compras.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario = em.getReference( codigoUsuario.getClass(), codigoUsuario.getCodigo() );
                compras.setCodigoUsuario( codigoUsuario );
            }
            TbArmazem idArmazemFK = compras.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                compras.setIdArmazemFK( idArmazemFK );
            }
            List<ItemCompras> attachedItemComprasList = new ArrayList<ItemCompras>();
            for ( ItemCompras itemComprasListItemComprasToAttach : compras.getItemComprasList() )
            {
                itemComprasListItemComprasToAttach = em.getReference( itemComprasListItemComprasToAttach.getClass(), itemComprasListItemComprasToAttach.getPkItmCompras() );
                attachedItemComprasList.add( itemComprasListItemComprasToAttach );
            }
            compras.setItemComprasList( attachedItemComprasList );
            List<NotasItemCompras> attachedNotasItemComprasList = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNotasItemComprasToAttach : compras.getNotasItemComprasList() )
            {
                notasItemComprasListNotasItemComprasToAttach = em.getReference( notasItemComprasListNotasItemComprasToAttach.getClass(), notasItemComprasListNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasList.add( notasItemComprasListNotasItemComprasToAttach );
            }
            compras.setNotasItemComprasList( attachedNotasItemComprasList );
            em.persist( compras );
            if ( fkDocumento != null )
            {
                fkDocumento.getComprasList().add( compras );
                fkDocumento = em.merge( fkDocumento );
            }
            if ( fkFornecedor != null )
            {
                fkFornecedor.getComprasList().add( compras );
                fkFornecedor = em.merge( fkFornecedor );
            }
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getComprasList().add( compras );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            if ( codigoUsuario != null )
            {
                codigoUsuario.getComprasList().add( compras );
                codigoUsuario = em.merge( codigoUsuario );
            }
            if ( idArmazemFK != null )
            {
                idArmazemFK.getComprasList().add( compras );
                idArmazemFK = em.merge( idArmazemFK );
            }
            for ( ItemCompras itemComprasListItemCompras : compras.getItemComprasList() )
            {
                Compras oldFkCompraOfItemComprasListItemCompras = itemComprasListItemCompras.getFkCompra();
                itemComprasListItemCompras.setFkCompra( compras );
                itemComprasListItemCompras = em.merge( itemComprasListItemCompras );
                if ( oldFkCompraOfItemComprasListItemCompras != null )
                {
                    oldFkCompraOfItemComprasListItemCompras.getItemComprasList().remove( itemComprasListItemCompras );
                    oldFkCompraOfItemComprasListItemCompras = em.merge( oldFkCompraOfItemComprasListItemCompras );
                }
            }
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : compras.getNotasItemComprasList() )
            {
                Compras oldFkComprasOfNotasItemComprasListNotasItemCompras = notasItemComprasListNotasItemCompras.getFkCompras();
                notasItemComprasListNotasItemCompras.setFkCompras( compras );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
                if ( oldFkComprasOfNotasItemComprasListNotasItemCompras != null )
                {
                    oldFkComprasOfNotasItemComprasListNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNotasItemCompras );
                    oldFkComprasOfNotasItemComprasListNotasItemCompras = em.merge( oldFkComprasOfNotasItemComprasListNotasItemCompras );
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

    public void edit( Compras compras ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Compras persistentCompras = em.find( Compras.class, compras.getPkCompra() );
            Documento fkDocumentoOld = persistentCompras.getFkDocumento();
            Documento fkDocumentoNew = compras.getFkDocumento();
            TbFornecedor fkFornecedorOld = persistentCompras.getFkFornecedor();
            TbFornecedor fkFornecedorNew = compras.getFkFornecedor();
            AnoEconomico fkAnoEconomicoOld = persistentCompras.getFkAnoEconomico();
            AnoEconomico fkAnoEconomicoNew = compras.getFkAnoEconomico();
            TbUsuario codigoUsuarioOld = persistentCompras.getCodigoUsuario();
            TbUsuario codigoUsuarioNew = compras.getCodigoUsuario();
            TbArmazem idArmazemFKOld = persistentCompras.getIdArmazemFK();
            TbArmazem idArmazemFKNew = compras.getIdArmazemFK();
            List<ItemCompras> itemComprasListOld = persistentCompras.getItemComprasList();
            List<ItemCompras> itemComprasListNew = compras.getItemComprasList();
            List<NotasItemCompras> notasItemComprasListOld = persistentCompras.getNotasItemComprasList();
            List<NotasItemCompras> notasItemComprasListNew = compras.getNotasItemComprasList();
            List<String> illegalOrphanMessages = null;
            for ( ItemCompras itemComprasListOldItemCompras : itemComprasListOld )
            {
                if ( !itemComprasListNew.contains( itemComprasListOldItemCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemCompras " + itemComprasListOldItemCompras + " since its fkCompra field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkDocumentoNew != null )
            {
                fkDocumentoNew = em.getReference( fkDocumentoNew.getClass(), fkDocumentoNew.getPkDocumento() );
                compras.setFkDocumento( fkDocumentoNew );
            }
            if ( fkFornecedorNew != null )
            {
                fkFornecedorNew = em.getReference( fkFornecedorNew.getClass(), fkFornecedorNew.getCodigo() );
                compras.setFkFornecedor( fkFornecedorNew );
            }
            if ( fkAnoEconomicoNew != null )
            {
                fkAnoEconomicoNew = em.getReference( fkAnoEconomicoNew.getClass(), fkAnoEconomicoNew.getPkAnoEconomico() );
                compras.setFkAnoEconomico( fkAnoEconomicoNew );
            }
            if ( codigoUsuarioNew != null )
            {
                codigoUsuarioNew = em.getReference( codigoUsuarioNew.getClass(), codigoUsuarioNew.getCodigo() );
                compras.setCodigoUsuario( codigoUsuarioNew );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                compras.setIdArmazemFK( idArmazemFKNew );
            }
            List<ItemCompras> attachedItemComprasListNew = new ArrayList<ItemCompras>();
            for ( ItemCompras itemComprasListNewItemComprasToAttach : itemComprasListNew )
            {
                itemComprasListNewItemComprasToAttach = em.getReference( itemComprasListNewItemComprasToAttach.getClass(), itemComprasListNewItemComprasToAttach.getPkItmCompras() );
                attachedItemComprasListNew.add( itemComprasListNewItemComprasToAttach );
            }
            itemComprasListNew = attachedItemComprasListNew;
            compras.setItemComprasList( itemComprasListNew );
            List<NotasItemCompras> attachedNotasItemComprasListNew = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNewNotasItemComprasToAttach : notasItemComprasListNew )
            {
                notasItemComprasListNewNotasItemComprasToAttach = em.getReference( notasItemComprasListNewNotasItemComprasToAttach.getClass(), notasItemComprasListNewNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasListNew.add( notasItemComprasListNewNotasItemComprasToAttach );
            }
            notasItemComprasListNew = attachedNotasItemComprasListNew;
            compras.setNotasItemComprasList( notasItemComprasListNew );
            compras = em.merge( compras );
            if ( fkDocumentoOld != null && !fkDocumentoOld.equals( fkDocumentoNew ) )
            {
                fkDocumentoOld.getComprasList().remove( compras );
                fkDocumentoOld = em.merge( fkDocumentoOld );
            }
            if ( fkDocumentoNew != null && !fkDocumentoNew.equals( fkDocumentoOld ) )
            {
                fkDocumentoNew.getComprasList().add( compras );
                fkDocumentoNew = em.merge( fkDocumentoNew );
            }
            if ( fkFornecedorOld != null && !fkFornecedorOld.equals( fkFornecedorNew ) )
            {
                fkFornecedorOld.getComprasList().remove( compras );
                fkFornecedorOld = em.merge( fkFornecedorOld );
            }
            if ( fkFornecedorNew != null && !fkFornecedorNew.equals( fkFornecedorOld ) )
            {
                fkFornecedorNew.getComprasList().add( compras );
                fkFornecedorNew = em.merge( fkFornecedorNew );
            }
            if ( fkAnoEconomicoOld != null && !fkAnoEconomicoOld.equals( fkAnoEconomicoNew ) )
            {
                fkAnoEconomicoOld.getComprasList().remove( compras );
                fkAnoEconomicoOld = em.merge( fkAnoEconomicoOld );
            }
            if ( fkAnoEconomicoNew != null && !fkAnoEconomicoNew.equals( fkAnoEconomicoOld ) )
            {
                fkAnoEconomicoNew.getComprasList().add( compras );
                fkAnoEconomicoNew = em.merge( fkAnoEconomicoNew );
            }
            if ( codigoUsuarioOld != null && !codigoUsuarioOld.equals( codigoUsuarioNew ) )
            {
                codigoUsuarioOld.getComprasList().remove( compras );
                codigoUsuarioOld = em.merge( codigoUsuarioOld );
            }
            if ( codigoUsuarioNew != null && !codigoUsuarioNew.equals( codigoUsuarioOld ) )
            {
                codigoUsuarioNew.getComprasList().add( compras );
                codigoUsuarioNew = em.merge( codigoUsuarioNew );
            }
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getComprasList().remove( compras );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getComprasList().add( compras );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            for ( ItemCompras itemComprasListNewItemCompras : itemComprasListNew )
            {
                if ( !itemComprasListOld.contains( itemComprasListNewItemCompras ) )
                {
                    Compras oldFkCompraOfItemComprasListNewItemCompras = itemComprasListNewItemCompras.getFkCompra();
                    itemComprasListNewItemCompras.setFkCompra( compras );
                    itemComprasListNewItemCompras = em.merge( itemComprasListNewItemCompras );
                    if ( oldFkCompraOfItemComprasListNewItemCompras != null && !oldFkCompraOfItemComprasListNewItemCompras.equals( compras ) )
                    {
                        oldFkCompraOfItemComprasListNewItemCompras.getItemComprasList().remove( itemComprasListNewItemCompras );
                        oldFkCompraOfItemComprasListNewItemCompras = em.merge( oldFkCompraOfItemComprasListNewItemCompras );
                    }
                }
            }
            for ( NotasItemCompras notasItemComprasListOldNotasItemCompras : notasItemComprasListOld )
            {
                if ( !notasItemComprasListNew.contains( notasItemComprasListOldNotasItemCompras ) )
                {
                    notasItemComprasListOldNotasItemCompras.setFkCompras( null );
                    notasItemComprasListOldNotasItemCompras = em.merge( notasItemComprasListOldNotasItemCompras );
                }
            }
            for ( NotasItemCompras notasItemComprasListNewNotasItemCompras : notasItemComprasListNew )
            {
                if ( !notasItemComprasListOld.contains( notasItemComprasListNewNotasItemCompras ) )
                {
                    Compras oldFkComprasOfNotasItemComprasListNewNotasItemCompras = notasItemComprasListNewNotasItemCompras.getFkCompras();
                    notasItemComprasListNewNotasItemCompras.setFkCompras( compras );
                    notasItemComprasListNewNotasItemCompras = em.merge( notasItemComprasListNewNotasItemCompras );
                    if ( oldFkComprasOfNotasItemComprasListNewNotasItemCompras != null && !oldFkComprasOfNotasItemComprasListNewNotasItemCompras.equals( compras ) )
                    {
                        oldFkComprasOfNotasItemComprasListNewNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNewNotasItemCompras );
                        oldFkComprasOfNotasItemComprasListNewNotasItemCompras = em.merge( oldFkComprasOfNotasItemComprasListNewNotasItemCompras );
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
                Integer id = compras.getPkCompra();
                if ( findCompras( id ) == null )
                {
                    throw new NonexistentEntityException( "The compras with id " + id + " no longer exists." );
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
            Compras compras;
            try
            {
                compras = em.getReference( Compras.class, id );
                compras.getPkCompra();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The compras with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemCompras> itemComprasListOrphanCheck = compras.getItemComprasList();
            for ( ItemCompras itemComprasListOrphanCheckItemCompras : itemComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Compras (" + compras + ") cannot be destroyed since the ItemCompras " + itemComprasListOrphanCheckItemCompras + " in its itemComprasList field has a non-nullable fkCompra field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            Documento fkDocumento = compras.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento.getComprasList().remove( compras );
                fkDocumento = em.merge( fkDocumento );
            }
            TbFornecedor fkFornecedor = compras.getFkFornecedor();
            if ( fkFornecedor != null )
            {
                fkFornecedor.getComprasList().remove( compras );
                fkFornecedor = em.merge( fkFornecedor );
            }
            AnoEconomico fkAnoEconomico = compras.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getComprasList().remove( compras );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            TbUsuario codigoUsuario = compras.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario.getComprasList().remove( compras );
                codigoUsuario = em.merge( codigoUsuario );
            }
            TbArmazem idArmazemFK = compras.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getComprasList().remove( compras );
                idArmazemFK = em.merge( idArmazemFK );
            }
            List<NotasItemCompras> notasItemComprasList = compras.getNotasItemComprasList();
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : notasItemComprasList )
            {
                notasItemComprasListNotasItemCompras.setFkCompras( null );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
            }
            em.remove( compras );
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

    public List<Compras> findComprasEntities()
    {
        return findComprasEntities( true, -1, -1 );
    }

    public List<Compras> findComprasEntities( int maxResults, int firstResult )
    {
        return findComprasEntities( false, maxResults, firstResult );
    }

    private List<Compras> findComprasEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Compras.class ) );
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

    public Compras findCompras( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Compras.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getComprasCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compras> rt = cq.from( Compras.class );
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
