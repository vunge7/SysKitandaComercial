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
import entity.TbClientesEncomenda;
import entity.TbEncomenda;
import entity.TbUsuario;
import entity.TbItemEncomenda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbEncomendaJpaController implements Serializable
{

    public TbEncomendaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEncomenda tbEncomenda )
    {
        if ( tbEncomenda.getTbItemEncomendaList() == null )
        {
            tbEncomenda.setTbItemEncomendaList( new ArrayList<TbItemEncomenda>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbClientesEncomenda idCliente = tbEncomenda.getIdCliente();
            if ( idCliente != null )
            {
                idCliente = em.getReference( idCliente.getClass(), idCliente.getIdCliente() );
                tbEncomenda.setIdCliente( idCliente );
            }
            TbUsuario idUsuario = tbEncomenda.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario = em.getReference( idUsuario.getClass(), idUsuario.getCodigo() );
                tbEncomenda.setIdUsuario( idUsuario );
            }
            List<TbItemEncomenda> attachedTbItemEncomendaList = new ArrayList<TbItemEncomenda>();
            for ( TbItemEncomenda tbItemEncomendaListTbItemEncomendaToAttach : tbEncomenda.getTbItemEncomendaList() )
            {
                tbItemEncomendaListTbItemEncomendaToAttach = em.getReference( tbItemEncomendaListTbItemEncomendaToAttach.getClass(), tbItemEncomendaListTbItemEncomendaToAttach.getCodigo() );
                attachedTbItemEncomendaList.add( tbItemEncomendaListTbItemEncomendaToAttach );
            }
            tbEncomenda.setTbItemEncomendaList( attachedTbItemEncomendaList );
            em.persist( tbEncomenda );
            if ( idCliente != null )
            {
                idCliente.getTbEncomendaList().add( tbEncomenda );
                idCliente = em.merge( idCliente );
            }
            if ( idUsuario != null )
            {
                idUsuario.getTbEncomendaList().add( tbEncomenda );
                idUsuario = em.merge( idUsuario );
            }
            for ( TbItemEncomenda tbItemEncomendaListTbItemEncomenda : tbEncomenda.getTbItemEncomendaList() )
            {
                TbEncomenda oldIdEncomendaOfTbItemEncomendaListTbItemEncomenda = tbItemEncomendaListTbItemEncomenda.getIdEncomenda();
                tbItemEncomendaListTbItemEncomenda.setIdEncomenda( tbEncomenda );
                tbItemEncomendaListTbItemEncomenda = em.merge( tbItemEncomendaListTbItemEncomenda );
                if ( oldIdEncomendaOfTbItemEncomendaListTbItemEncomenda != null )
                {
                    oldIdEncomendaOfTbItemEncomendaListTbItemEncomenda.getTbItemEncomendaList().remove( tbItemEncomendaListTbItemEncomenda );
                    oldIdEncomendaOfTbItemEncomendaListTbItemEncomenda = em.merge( oldIdEncomendaOfTbItemEncomendaListTbItemEncomenda );
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

    public void edit( TbEncomenda tbEncomenda ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEncomenda persistentTbEncomenda = em.find( TbEncomenda.class, tbEncomenda.getIdEncomenda() );
            TbClientesEncomenda idClienteOld = persistentTbEncomenda.getIdCliente();
            TbClientesEncomenda idClienteNew = tbEncomenda.getIdCliente();
            TbUsuario idUsuarioOld = persistentTbEncomenda.getIdUsuario();
            TbUsuario idUsuarioNew = tbEncomenda.getIdUsuario();
            List<TbItemEncomenda> tbItemEncomendaListOld = persistentTbEncomenda.getTbItemEncomendaList();
            List<TbItemEncomenda> tbItemEncomendaListNew = tbEncomenda.getTbItemEncomendaList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemEncomenda tbItemEncomendaListOldTbItemEncomenda : tbItemEncomendaListOld )
            {
                if ( !tbItemEncomendaListNew.contains( tbItemEncomendaListOldTbItemEncomenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEncomenda " + tbItemEncomendaListOldTbItemEncomenda + " since its idEncomenda field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idClienteNew != null )
            {
                idClienteNew = em.getReference( idClienteNew.getClass(), idClienteNew.getIdCliente() );
                tbEncomenda.setIdCliente( idClienteNew );
            }
            if ( idUsuarioNew != null )
            {
                idUsuarioNew = em.getReference( idUsuarioNew.getClass(), idUsuarioNew.getCodigo() );
                tbEncomenda.setIdUsuario( idUsuarioNew );
            }
            List<TbItemEncomenda> attachedTbItemEncomendaListNew = new ArrayList<TbItemEncomenda>();
            for ( TbItemEncomenda tbItemEncomendaListNewTbItemEncomendaToAttach : tbItemEncomendaListNew )
            {
                tbItemEncomendaListNewTbItemEncomendaToAttach = em.getReference( tbItemEncomendaListNewTbItemEncomendaToAttach.getClass(), tbItemEncomendaListNewTbItemEncomendaToAttach.getCodigo() );
                attachedTbItemEncomendaListNew.add( tbItemEncomendaListNewTbItemEncomendaToAttach );
            }
            tbItemEncomendaListNew = attachedTbItemEncomendaListNew;
            tbEncomenda.setTbItemEncomendaList( tbItemEncomendaListNew );
            tbEncomenda = em.merge( tbEncomenda );
            if ( idClienteOld != null && !idClienteOld.equals( idClienteNew ) )
            {
                idClienteOld.getTbEncomendaList().remove( tbEncomenda );
                idClienteOld = em.merge( idClienteOld );
            }
            if ( idClienteNew != null && !idClienteNew.equals( idClienteOld ) )
            {
                idClienteNew.getTbEncomendaList().add( tbEncomenda );
                idClienteNew = em.merge( idClienteNew );
            }
            if ( idUsuarioOld != null && !idUsuarioOld.equals( idUsuarioNew ) )
            {
                idUsuarioOld.getTbEncomendaList().remove( tbEncomenda );
                idUsuarioOld = em.merge( idUsuarioOld );
            }
            if ( idUsuarioNew != null && !idUsuarioNew.equals( idUsuarioOld ) )
            {
                idUsuarioNew.getTbEncomendaList().add( tbEncomenda );
                idUsuarioNew = em.merge( idUsuarioNew );
            }
            for ( TbItemEncomenda tbItemEncomendaListNewTbItemEncomenda : tbItemEncomendaListNew )
            {
                if ( !tbItemEncomendaListOld.contains( tbItemEncomendaListNewTbItemEncomenda ) )
                {
                    TbEncomenda oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda = tbItemEncomendaListNewTbItemEncomenda.getIdEncomenda();
                    tbItemEncomendaListNewTbItemEncomenda.setIdEncomenda( tbEncomenda );
                    tbItemEncomendaListNewTbItemEncomenda = em.merge( tbItemEncomendaListNewTbItemEncomenda );
                    if ( oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda != null && !oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda.equals( tbEncomenda ) )
                    {
                        oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda.getTbItemEncomendaList().remove( tbItemEncomendaListNewTbItemEncomenda );
                        oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda = em.merge( oldIdEncomendaOfTbItemEncomendaListNewTbItemEncomenda );
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
                Long id = tbEncomenda.getIdEncomenda();
                if ( findTbEncomenda( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEncomenda with id " + id + " no longer exists." );
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

    public void destroy( Long id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEncomenda tbEncomenda;
            try
            {
                tbEncomenda = em.getReference( TbEncomenda.class, id );
                tbEncomenda.getIdEncomenda();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEncomenda with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemEncomenda> tbItemEncomendaListOrphanCheck = tbEncomenda.getTbItemEncomendaList();
            for ( TbItemEncomenda tbItemEncomendaListOrphanCheckTbItemEncomenda : tbItemEncomendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbEncomenda (" + tbEncomenda + ") cannot be destroyed since the TbItemEncomenda " + tbItemEncomendaListOrphanCheckTbItemEncomenda + " in its tbItemEncomendaList field has a non-nullable idEncomenda field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbClientesEncomenda idCliente = tbEncomenda.getIdCliente();
            if ( idCliente != null )
            {
                idCliente.getTbEncomendaList().remove( tbEncomenda );
                idCliente = em.merge( idCliente );
            }
            TbUsuario idUsuario = tbEncomenda.getIdUsuario();
            if ( idUsuario != null )
            {
                idUsuario.getTbEncomendaList().remove( tbEncomenda );
                idUsuario = em.merge( idUsuario );
            }
            em.remove( tbEncomenda );
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

    public List<TbEncomenda> findTbEncomendaEntities()
    {
        return findTbEncomendaEntities( true, -1, -1 );
    }

    public List<TbEncomenda> findTbEncomendaEntities( int maxResults, int firstResult )
    {
        return findTbEncomendaEntities( false, maxResults, firstResult );
    }

    private List<TbEncomenda> findTbEncomendaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEncomenda.class ) );
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

    public TbEncomenda findTbEncomenda( Long id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEncomenda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEncomendaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEncomenda> rt = cq.from( TbEncomenda.class );
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
