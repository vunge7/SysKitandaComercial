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
import entity.TbCliente;
import entity.TbUsuario;
import entity.TbItemProForma;
import entity.TbProForma;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbProFormaJpaController implements Serializable
{

    public TbProFormaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbProForma tbProForma )
    {
        if ( tbProForma.getTbItemProFormaList() == null )
        {
            tbProForma.setTbItemProFormaList( new ArrayList<TbItemProForma>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbCliente fkCliente = tbProForma.getFkCliente();
            if ( fkCliente != null )
            {
                fkCliente = em.getReference( fkCliente.getClass(), fkCliente.getCodigo() );
                tbProForma.setFkCliente( fkCliente );
            }
            TbUsuario fkUsuario = tbProForma.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbProForma.setFkUsuario( fkUsuario );
            }
            List<TbItemProForma> attachedTbItemProFormaList = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListTbItemProFormaToAttach : tbProForma.getTbItemProFormaList() )
            {
                tbItemProFormaListTbItemProFormaToAttach = em.getReference( tbItemProFormaListTbItemProFormaToAttach.getClass(), tbItemProFormaListTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaList.add( tbItemProFormaListTbItemProFormaToAttach );
            }
            tbProForma.setTbItemProFormaList( attachedTbItemProFormaList );
            em.persist( tbProForma );
            if ( fkCliente != null )
            {
                fkCliente.getTbProFormaList().add( tbProForma );
                fkCliente = em.merge( fkCliente );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbProFormaList().add( tbProForma );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( TbItemProForma tbItemProFormaListTbItemProForma : tbProForma.getTbItemProFormaList() )
            {
                TbProForma oldFkProFormaOfTbItemProFormaListTbItemProForma = tbItemProFormaListTbItemProForma.getFkProForma();
                tbItemProFormaListTbItemProForma.setFkProForma( tbProForma );
                tbItemProFormaListTbItemProForma = em.merge( tbItemProFormaListTbItemProForma );
                if ( oldFkProFormaOfTbItemProFormaListTbItemProForma != null )
                {
                    oldFkProFormaOfTbItemProFormaListTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListTbItemProForma );
                    oldFkProFormaOfTbItemProFormaListTbItemProForma = em.merge( oldFkProFormaOfTbItemProFormaListTbItemProForma );
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

    public void edit( TbProForma tbProForma ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProForma persistentTbProForma = em.find( TbProForma.class, tbProForma.getPkProForma() );
            TbCliente fkClienteOld = persistentTbProForma.getFkCliente();
            TbCliente fkClienteNew = tbProForma.getFkCliente();
            TbUsuario fkUsuarioOld = persistentTbProForma.getFkUsuario();
            TbUsuario fkUsuarioNew = tbProForma.getFkUsuario();
            List<TbItemProForma> tbItemProFormaListOld = persistentTbProForma.getTbItemProFormaList();
            List<TbItemProForma> tbItemProFormaListNew = tbProForma.getTbItemProFormaList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemProForma tbItemProFormaListOldTbItemProForma : tbItemProFormaListOld )
            {
                if ( !tbItemProFormaListNew.contains( tbItemProFormaListOldTbItemProForma ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemProForma " + tbItemProFormaListOldTbItemProForma + " since its fkProForma field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkClienteNew != null )
            {
                fkClienteNew = em.getReference( fkClienteNew.getClass(), fkClienteNew.getCodigo() );
                tbProForma.setFkCliente( fkClienteNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbProForma.setFkUsuario( fkUsuarioNew );
            }
            List<TbItemProForma> attachedTbItemProFormaListNew = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListNewTbItemProFormaToAttach : tbItemProFormaListNew )
            {
                tbItemProFormaListNewTbItemProFormaToAttach = em.getReference( tbItemProFormaListNewTbItemProFormaToAttach.getClass(), tbItemProFormaListNewTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaListNew.add( tbItemProFormaListNewTbItemProFormaToAttach );
            }
            tbItemProFormaListNew = attachedTbItemProFormaListNew;
            tbProForma.setTbItemProFormaList( tbItemProFormaListNew );
            tbProForma = em.merge( tbProForma );
            if ( fkClienteOld != null && !fkClienteOld.equals( fkClienteNew ) )
            {
                fkClienteOld.getTbProFormaList().remove( tbProForma );
                fkClienteOld = em.merge( fkClienteOld );
            }
            if ( fkClienteNew != null && !fkClienteNew.equals( fkClienteOld ) )
            {
                fkClienteNew.getTbProFormaList().add( tbProForma );
                fkClienteNew = em.merge( fkClienteNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbProFormaList().remove( tbProForma );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbProFormaList().add( tbProForma );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( TbItemProForma tbItemProFormaListNewTbItemProForma : tbItemProFormaListNew )
            {
                if ( !tbItemProFormaListOld.contains( tbItemProFormaListNewTbItemProForma ) )
                {
                    TbProForma oldFkProFormaOfTbItemProFormaListNewTbItemProForma = tbItemProFormaListNewTbItemProForma.getFkProForma();
                    tbItemProFormaListNewTbItemProForma.setFkProForma( tbProForma );
                    tbItemProFormaListNewTbItemProForma = em.merge( tbItemProFormaListNewTbItemProForma );
                    if ( oldFkProFormaOfTbItemProFormaListNewTbItemProForma != null && !oldFkProFormaOfTbItemProFormaListNewTbItemProForma.equals( tbProForma ) )
                    {
                        oldFkProFormaOfTbItemProFormaListNewTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListNewTbItemProForma );
                        oldFkProFormaOfTbItemProFormaListNewTbItemProForma = em.merge( oldFkProFormaOfTbItemProFormaListNewTbItemProForma );
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
                Integer id = tbProForma.getPkProForma();
                if ( findTbProForma( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbProForma with id " + id + " no longer exists." );
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
            TbProForma tbProForma;
            try
            {
                tbProForma = em.getReference( TbProForma.class, id );
                tbProForma.getPkProForma();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbProForma with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemProForma> tbItemProFormaListOrphanCheck = tbProForma.getTbItemProFormaList();
            for ( TbItemProForma tbItemProFormaListOrphanCheckTbItemProForma : tbItemProFormaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProForma (" + tbProForma + ") cannot be destroyed since the TbItemProForma " + tbItemProFormaListOrphanCheckTbItemProForma + " in its tbItemProFormaList field has a non-nullable fkProForma field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbCliente fkCliente = tbProForma.getFkCliente();
            if ( fkCliente != null )
            {
                fkCliente.getTbProFormaList().remove( tbProForma );
                fkCliente = em.merge( fkCliente );
            }
            TbUsuario fkUsuario = tbProForma.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbProFormaList().remove( tbProForma );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbProForma );
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

    public List<TbProForma> findTbProFormaEntities()
    {
        return findTbProFormaEntities( true, -1, -1 );
    }

    public List<TbProForma> findTbProFormaEntities( int maxResults, int firstResult )
    {
        return findTbProFormaEntities( false, maxResults, firstResult );
    }

    private List<TbProForma> findTbProFormaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbProForma.class ) );
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

    public TbProForma findTbProForma( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbProForma.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbProFormaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbProForma> rt = cq.from( TbProForma.class );
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
