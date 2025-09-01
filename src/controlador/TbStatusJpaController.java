/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import java.util.ArrayList;
import java.util.List;
import entity.TbFuncionario;
import entity.TbStatus;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbStatusJpaController implements Serializable
{

    public TbStatusJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbStatus tbStatus )
    {
        if ( tbStatus.getTbUsuarioList() == null )
        {
            tbStatus.setTbUsuarioList( new ArrayList<TbUsuario>() );
        }
        if ( tbStatus.getTbFuncionarioList() == null )
        {
            tbStatus.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbUsuario> attachedTbUsuarioList = new ArrayList<TbUsuario>();
            for ( TbUsuario tbUsuarioListTbUsuarioToAttach : tbStatus.getTbUsuarioList() )
            {
                tbUsuarioListTbUsuarioToAttach = em.getReference( tbUsuarioListTbUsuarioToAttach.getClass(), tbUsuarioListTbUsuarioToAttach.getCodigo() );
                attachedTbUsuarioList.add( tbUsuarioListTbUsuarioToAttach );
            }
            tbStatus.setTbUsuarioList( attachedTbUsuarioList );
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbStatus.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbStatus.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbStatus );
            for ( TbUsuario tbUsuarioListTbUsuario : tbStatus.getTbUsuarioList() )
            {
                TbStatus oldIdStatusOfTbUsuarioListTbUsuario = tbUsuarioListTbUsuario.getIdStatus();
                tbUsuarioListTbUsuario.setIdStatus( tbStatus );
                tbUsuarioListTbUsuario = em.merge( tbUsuarioListTbUsuario );
                if ( oldIdStatusOfTbUsuarioListTbUsuario != null )
                {
                    oldIdStatusOfTbUsuarioListTbUsuario.getTbUsuarioList().remove( tbUsuarioListTbUsuario );
                    oldIdStatusOfTbUsuarioListTbUsuario = em.merge( oldIdStatusOfTbUsuarioListTbUsuario );
                }
            }
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbStatus.getTbFuncionarioList() )
            {
                TbStatus oldIdStatusFKOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getIdStatusFK();
                tbFuncionarioListTbFuncionario.setIdStatusFK( tbStatus );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldIdStatusFKOfTbFuncionarioListTbFuncionario != null )
                {
                    oldIdStatusFKOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldIdStatusFKOfTbFuncionarioListTbFuncionario = em.merge( oldIdStatusFKOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbStatus tbStatus ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbStatus persistentTbStatus = em.find( TbStatus.class, tbStatus.getIdStatus() );
            List<TbUsuario> tbUsuarioListOld = persistentTbStatus.getTbUsuarioList();
            List<TbUsuario> tbUsuarioListNew = tbStatus.getTbUsuarioList();
            List<TbFuncionario> tbFuncionarioListOld = persistentTbStatus.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbStatus.getTbFuncionarioList();
            List<TbUsuario> attachedTbUsuarioListNew = new ArrayList<TbUsuario>();
            for ( TbUsuario tbUsuarioListNewTbUsuarioToAttach : tbUsuarioListNew )
            {
                tbUsuarioListNewTbUsuarioToAttach = em.getReference( tbUsuarioListNewTbUsuarioToAttach.getClass(), tbUsuarioListNewTbUsuarioToAttach.getCodigo() );
                attachedTbUsuarioListNew.add( tbUsuarioListNewTbUsuarioToAttach );
            }
            tbUsuarioListNew = attachedTbUsuarioListNew;
            tbStatus.setTbUsuarioList( tbUsuarioListNew );
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbStatus.setTbFuncionarioList( tbFuncionarioListNew );
            tbStatus = em.merge( tbStatus );
            for ( TbUsuario tbUsuarioListOldTbUsuario : tbUsuarioListOld )
            {
                if ( !tbUsuarioListNew.contains( tbUsuarioListOldTbUsuario ) )
                {
                    tbUsuarioListOldTbUsuario.setIdStatus( null );
                    tbUsuarioListOldTbUsuario = em.merge( tbUsuarioListOldTbUsuario );
                }
            }
            for ( TbUsuario tbUsuarioListNewTbUsuario : tbUsuarioListNew )
            {
                if ( !tbUsuarioListOld.contains( tbUsuarioListNewTbUsuario ) )
                {
                    TbStatus oldIdStatusOfTbUsuarioListNewTbUsuario = tbUsuarioListNewTbUsuario.getIdStatus();
                    tbUsuarioListNewTbUsuario.setIdStatus( tbStatus );
                    tbUsuarioListNewTbUsuario = em.merge( tbUsuarioListNewTbUsuario );
                    if ( oldIdStatusOfTbUsuarioListNewTbUsuario != null && !oldIdStatusOfTbUsuarioListNewTbUsuario.equals( tbStatus ) )
                    {
                        oldIdStatusOfTbUsuarioListNewTbUsuario.getTbUsuarioList().remove( tbUsuarioListNewTbUsuario );
                        oldIdStatusOfTbUsuarioListNewTbUsuario = em.merge( oldIdStatusOfTbUsuarioListNewTbUsuario );
                    }
                }
            }
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setIdStatusFK( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbStatus oldIdStatusFKOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getIdStatusFK();
                    tbFuncionarioListNewTbFuncionario.setIdStatusFK( tbStatus );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldIdStatusFKOfTbFuncionarioListNewTbFuncionario != null && !oldIdStatusFKOfTbFuncionarioListNewTbFuncionario.equals( tbStatus ) )
                    {
                        oldIdStatusFKOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldIdStatusFKOfTbFuncionarioListNewTbFuncionario = em.merge( oldIdStatusFKOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbStatus.getIdStatus();
                if ( findTbStatus( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbStatus with id " + id + " no longer exists." );
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
            TbStatus tbStatus;
            try
            {
                tbStatus = em.getReference( TbStatus.class, id );
                tbStatus.getIdStatus();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbStatus with id " + id + " no longer exists.", enfe );
            }
            List<TbUsuario> tbUsuarioList = tbStatus.getTbUsuarioList();
            for ( TbUsuario tbUsuarioListTbUsuario : tbUsuarioList )
            {
                tbUsuarioListTbUsuario.setIdStatus( null );
                tbUsuarioListTbUsuario = em.merge( tbUsuarioListTbUsuario );
            }
            List<TbFuncionario> tbFuncionarioList = tbStatus.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setIdStatusFK( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbStatus );
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

    public List<TbStatus> findTbStatusEntities()
    {
        return findTbStatusEntities( true, -1, -1 );
    }

    public List<TbStatus> findTbStatusEntities( int maxResults, int firstResult )
    {
        return findTbStatusEntities( false, maxResults, firstResult );
    }

    private List<TbStatus> findTbStatusEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbStatus.class ) );
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

    public TbStatus findTbStatus( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbStatus.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbStatusCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbStatus> rt = cq.from( TbStatus.class );
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
