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
import entity.Regime;
import entity.NotasCompras;
import java.util.ArrayList;
import java.util.List;
import entity.Compras;
import entity.TbFornecedor;
import entity.TbProduto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbFornecedorJpaController implements Serializable
{

    public TbFornecedorJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbFornecedor tbFornecedor )
    {
        if ( tbFornecedor.getNotasComprasList() == null )
        {
            tbFornecedor.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( tbFornecedor.getComprasList() == null )
        {
            tbFornecedor.setComprasList( new ArrayList<Compras>() );
        }
        if ( tbFornecedor.getTbProdutoList() == null )
        {
            tbFornecedor.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Regime fkRegime = tbFornecedor.getFkRegime();
            if ( fkRegime != null )
            {
                fkRegime = em.getReference( fkRegime.getClass(), fkRegime.getPkRegime() );
                tbFornecedor.setFkRegime( fkRegime );
            }
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : tbFornecedor.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            tbFornecedor.setNotasComprasList( attachedNotasComprasList );
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for ( Compras comprasListComprasToAttach : tbFornecedor.getComprasList() )
            {
                comprasListComprasToAttach = em.getReference( comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getPkCompra() );
                attachedComprasList.add( comprasListComprasToAttach );
            }
            tbFornecedor.setComprasList( attachedComprasList );
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : tbFornecedor.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            tbFornecedor.setTbProdutoList( attachedTbProdutoList );
            em.persist( tbFornecedor );
            if ( fkRegime != null )
            {
                fkRegime.getTbFornecedorList().add( tbFornecedor );
                fkRegime = em.merge( fkRegime );
            }
            for ( NotasCompras notasComprasListNotasCompras : tbFornecedor.getNotasComprasList() )
            {
                TbFornecedor oldFkFornecedorOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getFkFornecedor();
                notasComprasListNotasCompras.setFkFornecedor( tbFornecedor );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldFkFornecedorOfNotasComprasListNotasCompras != null )
                {
                    oldFkFornecedorOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldFkFornecedorOfNotasComprasListNotasCompras = em.merge( oldFkFornecedorOfNotasComprasListNotasCompras );
                }
            }
            for ( Compras comprasListCompras : tbFornecedor.getComprasList() )
            {
                TbFornecedor oldFkFornecedorOfComprasListCompras = comprasListCompras.getFkFornecedor();
                comprasListCompras.setFkFornecedor( tbFornecedor );
                comprasListCompras = em.merge( comprasListCompras );
                if ( oldFkFornecedorOfComprasListCompras != null )
                {
                    oldFkFornecedorOfComprasListCompras.getComprasList().remove( comprasListCompras );
                    oldFkFornecedorOfComprasListCompras = em.merge( oldFkFornecedorOfComprasListCompras );
                }
            }
            for ( TbProduto tbProdutoListTbProduto : tbFornecedor.getTbProdutoList() )
            {
                TbFornecedor oldCodFornecedoresOfTbProdutoListTbProduto = tbProdutoListTbProduto.getCodFornecedores();
                tbProdutoListTbProduto.setCodFornecedores( tbFornecedor );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldCodFornecedoresOfTbProdutoListTbProduto != null )
                {
                    oldCodFornecedoresOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldCodFornecedoresOfTbProdutoListTbProduto = em.merge( oldCodFornecedoresOfTbProdutoListTbProduto );
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

    public void edit( TbFornecedor tbFornecedor ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFornecedor persistentTbFornecedor = em.find( TbFornecedor.class, tbFornecedor.getCodigo() );
            Regime fkRegimeOld = persistentTbFornecedor.getFkRegime();
            Regime fkRegimeNew = tbFornecedor.getFkRegime();
            List<NotasCompras> notasComprasListOld = persistentTbFornecedor.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = tbFornecedor.getNotasComprasList();
            List<Compras> comprasListOld = persistentTbFornecedor.getComprasList();
            List<Compras> comprasListNew = tbFornecedor.getComprasList();
            List<TbProduto> tbProdutoListOld = persistentTbFornecedor.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = tbFornecedor.getTbProdutoList();
            List<String> illegalOrphanMessages = null;
            for ( NotasCompras notasComprasListOldNotasCompras : notasComprasListOld )
            {
                if ( !notasComprasListNew.contains( notasComprasListOldNotasCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain NotasCompras " + notasComprasListOldNotasCompras + " since its fkFornecedor field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain Compras " + comprasListOldCompras + " since its fkFornecedor field is not nullable." );
                }
            }
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbProduto " + tbProdutoListOldTbProduto + " since its codFornecedores field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkRegimeNew != null )
            {
                fkRegimeNew = em.getReference( fkRegimeNew.getClass(), fkRegimeNew.getPkRegime() );
                tbFornecedor.setFkRegime( fkRegimeNew );
            }
            List<NotasCompras> attachedNotasComprasListNew = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNewNotasComprasToAttach : notasComprasListNew )
            {
                notasComprasListNewNotasComprasToAttach = em.getReference( notasComprasListNewNotasComprasToAttach.getClass(), notasComprasListNewNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasListNew.add( notasComprasListNewNotasComprasToAttach );
            }
            notasComprasListNew = attachedNotasComprasListNew;
            tbFornecedor.setNotasComprasList( notasComprasListNew );
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for ( Compras comprasListNewComprasToAttach : comprasListNew )
            {
                comprasListNewComprasToAttach = em.getReference( comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getPkCompra() );
                attachedComprasListNew.add( comprasListNewComprasToAttach );
            }
            comprasListNew = attachedComprasListNew;
            tbFornecedor.setComprasList( comprasListNew );
            List<TbProduto> attachedTbProdutoListNew = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListNewTbProdutoToAttach : tbProdutoListNew )
            {
                tbProdutoListNewTbProdutoToAttach = em.getReference( tbProdutoListNewTbProdutoToAttach.getClass(), tbProdutoListNewTbProdutoToAttach.getCodigo() );
                attachedTbProdutoListNew.add( tbProdutoListNewTbProdutoToAttach );
            }
            tbProdutoListNew = attachedTbProdutoListNew;
            tbFornecedor.setTbProdutoList( tbProdutoListNew );
            tbFornecedor = em.merge( tbFornecedor );
            if ( fkRegimeOld != null && !fkRegimeOld.equals( fkRegimeNew ) )
            {
                fkRegimeOld.getTbFornecedorList().remove( tbFornecedor );
                fkRegimeOld = em.merge( fkRegimeOld );
            }
            if ( fkRegimeNew != null && !fkRegimeNew.equals( fkRegimeOld ) )
            {
                fkRegimeNew.getTbFornecedorList().add( tbFornecedor );
                fkRegimeNew = em.merge( fkRegimeNew );
            }
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    TbFornecedor oldFkFornecedorOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getFkFornecedor();
                    notasComprasListNewNotasCompras.setFkFornecedor( tbFornecedor );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldFkFornecedorOfNotasComprasListNewNotasCompras != null && !oldFkFornecedorOfNotasComprasListNewNotasCompras.equals( tbFornecedor ) )
                    {
                        oldFkFornecedorOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldFkFornecedorOfNotasComprasListNewNotasCompras = em.merge( oldFkFornecedorOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Compras comprasListNewCompras : comprasListNew )
            {
                if ( !comprasListOld.contains( comprasListNewCompras ) )
                {
                    TbFornecedor oldFkFornecedorOfComprasListNewCompras = comprasListNewCompras.getFkFornecedor();
                    comprasListNewCompras.setFkFornecedor( tbFornecedor );
                    comprasListNewCompras = em.merge( comprasListNewCompras );
                    if ( oldFkFornecedorOfComprasListNewCompras != null && !oldFkFornecedorOfComprasListNewCompras.equals( tbFornecedor ) )
                    {
                        oldFkFornecedorOfComprasListNewCompras.getComprasList().remove( comprasListNewCompras );
                        oldFkFornecedorOfComprasListNewCompras = em.merge( oldFkFornecedorOfComprasListNewCompras );
                    }
                }
            }
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    TbFornecedor oldCodFornecedoresOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getCodFornecedores();
                    tbProdutoListNewTbProduto.setCodFornecedores( tbFornecedor );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldCodFornecedoresOfTbProdutoListNewTbProduto != null && !oldCodFornecedoresOfTbProdutoListNewTbProduto.equals( tbFornecedor ) )
                    {
                        oldCodFornecedoresOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldCodFornecedoresOfTbProdutoListNewTbProduto = em.merge( oldCodFornecedoresOfTbProdutoListNewTbProduto );
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
                Integer id = tbFornecedor.getCodigo();
                if ( findTbFornecedor( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbFornecedor with id " + id + " no longer exists." );
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
            TbFornecedor tbFornecedor;
            try
            {
                tbFornecedor = em.getReference( TbFornecedor.class, id );
                tbFornecedor.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbFornecedor with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<NotasCompras> notasComprasListOrphanCheck = tbFornecedor.getNotasComprasList();
            for ( NotasCompras notasComprasListOrphanCheckNotasCompras : notasComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFornecedor (" + tbFornecedor + ") cannot be destroyed since the NotasCompras " + notasComprasListOrphanCheckNotasCompras + " in its notasComprasList field has a non-nullable fkFornecedor field." );
            }
            List<Compras> comprasListOrphanCheck = tbFornecedor.getComprasList();
            for ( Compras comprasListOrphanCheckCompras : comprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFornecedor (" + tbFornecedor + ") cannot be destroyed since the Compras " + comprasListOrphanCheckCompras + " in its comprasList field has a non-nullable fkFornecedor field." );
            }
            List<TbProduto> tbProdutoListOrphanCheck = tbFornecedor.getTbProdutoList();
            for ( TbProduto tbProdutoListOrphanCheckTbProduto : tbProdutoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFornecedor (" + tbFornecedor + ") cannot be destroyed since the TbProduto " + tbProdutoListOrphanCheckTbProduto + " in its tbProdutoList field has a non-nullable codFornecedores field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            Regime fkRegime = tbFornecedor.getFkRegime();
            if ( fkRegime != null )
            {
                fkRegime.getTbFornecedorList().remove( tbFornecedor );
                fkRegime = em.merge( fkRegime );
            }
            em.remove( tbFornecedor );
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

    public List<TbFornecedor> findTbFornecedorEntities()
    {
        return findTbFornecedorEntities( true, -1, -1 );
    }

    public List<TbFornecedor> findTbFornecedorEntities( int maxResults, int firstResult )
    {
        return findTbFornecedorEntities( false, maxResults, firstResult );
    }

    private List<TbFornecedor> findTbFornecedorEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbFornecedor.class ) );
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

    public TbFornecedor findTbFornecedor( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbFornecedor.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbFornecedorCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbFornecedor> rt = cq.from( TbFornecedor.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
