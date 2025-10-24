/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.TbTipoUsuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbTipoUsuarioJpaController1 implements Serializable
{

    public TbTipoUsuarioJpaController1( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbTipoUsuario tbTipoUsuario )
    {
        if ( tbTipoUsuario.getTbUsuarioList() == null )
        {
            tbTipoUsuario.setTbUsuarioList( new ArrayList<TbUsuario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbUsuario> attachedTbUsuarioList = new ArrayList<TbUsuario>();
            for ( TbUsuario tbUsuarioListTbUsuarioToAttach : tbTipoUsuario.getTbUsuarioList() )
            {
                tbUsuarioListTbUsuarioToAttach = em.getReference( tbUsuarioListTbUsuarioToAttach.getClass(), tbUsuarioListTbUsuarioToAttach.getCodigo() );
                attachedTbUsuarioList.add( tbUsuarioListTbUsuarioToAttach );
            }
            tbTipoUsuario.setTbUsuarioList( attachedTbUsuarioList );
            em.persist( tbTipoUsuario );
            for ( TbUsuario tbUsuarioListTbUsuario : tbTipoUsuario.getTbUsuarioList() )
            {
                TbTipoUsuario oldIdTipoUsuarioOfTbUsuarioListTbUsuario = tbUsuarioListTbUsuario.getIdTipoUsuario();
                tbUsuarioListTbUsuario.setIdTipoUsuario( tbTipoUsuario );
                tbUsuarioListTbUsuario = em.merge( tbUsuarioListTbUsuario );
                if ( oldIdTipoUsuarioOfTbUsuarioListTbUsuario != null )
                {
                    oldIdTipoUsuarioOfTbUsuarioListTbUsuario.getTbUsuarioList().remove( tbUsuarioListTbUsuario );
                    oldIdTipoUsuarioOfTbUsuarioListTbUsuario = em.merge( oldIdTipoUsuarioOfTbUsuarioListTbUsuario );
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

    public void edit( TbTipoUsuario tbTipoUsuario ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipoUsuario persistentTbTipoUsuario = em.find( TbTipoUsuario.class, tbTipoUsuario.getIdTipoUsuario() );
            List<TbUsuario> tbUsuarioListOld = persistentTbTipoUsuario.getTbUsuarioList();
            List<TbUsuario> tbUsuarioListNew = tbTipoUsuario.getTbUsuarioList();
            List<String> illegalOrphanMessages = null;
            for ( TbUsuario tbUsuarioListOldTbUsuario : tbUsuarioListOld )
            {
                if ( !tbUsuarioListNew.contains( tbUsuarioListOldTbUsuario ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbUsuario " + tbUsuarioListOldTbUsuario + " since its idTipoUsuario field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbUsuario> attachedTbUsuarioListNew = new ArrayList<TbUsuario>();
            for ( TbUsuario tbUsuarioListNewTbUsuarioToAttach : tbUsuarioListNew )
            {
                tbUsuarioListNewTbUsuarioToAttach = em.getReference( tbUsuarioListNewTbUsuarioToAttach.getClass(), tbUsuarioListNewTbUsuarioToAttach.getCodigo() );
                attachedTbUsuarioListNew.add( tbUsuarioListNewTbUsuarioToAttach );
            }
            tbUsuarioListNew = attachedTbUsuarioListNew;
            tbTipoUsuario.setTbUsuarioList( tbUsuarioListNew );
            tbTipoUsuario = em.merge( tbTipoUsuario );
            for ( TbUsuario tbUsuarioListNewTbUsuario : tbUsuarioListNew )
            {
                if ( !tbUsuarioListOld.contains( tbUsuarioListNewTbUsuario ) )
                {
                    TbTipoUsuario oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario = tbUsuarioListNewTbUsuario.getIdTipoUsuario();
                    tbUsuarioListNewTbUsuario.setIdTipoUsuario( tbTipoUsuario );
                    tbUsuarioListNewTbUsuario = em.merge( tbUsuarioListNewTbUsuario );
                    if ( oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario != null && !oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario.equals( tbTipoUsuario ) )
                    {
                        oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario.getTbUsuarioList().remove( tbUsuarioListNewTbUsuario );
                        oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario = em.merge( oldIdTipoUsuarioOfTbUsuarioListNewTbUsuario );
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
                Integer id = tbTipoUsuario.getIdTipoUsuario();
                if ( findTbTipoUsuario( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbTipoUsuario with id " + id + " no longer exists." );
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
            TbTipoUsuario tbTipoUsuario;
            try
            {
                tbTipoUsuario = em.getReference( TbTipoUsuario.class, id );
                tbTipoUsuario.getIdTipoUsuario();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbTipoUsuario with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbUsuario> tbUsuarioListOrphanCheck = tbTipoUsuario.getTbUsuarioList();
            for ( TbUsuario tbUsuarioListOrphanCheckTbUsuario : tbUsuarioListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbTipoUsuario (" + tbTipoUsuario + ") cannot be destroyed since the TbUsuario " + tbUsuarioListOrphanCheckTbUsuario + " in its tbUsuarioList field has a non-nullable idTipoUsuario field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbTipoUsuario );
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

    public List<TbTipoUsuario> findTbTipoUsuarioEntities()
    {
        return findTbTipoUsuarioEntities( true, -1, -1 );
    }

    public List<TbTipoUsuario> findTbTipoUsuarioEntities( int maxResults, int firstResult )
    {
        return findTbTipoUsuarioEntities( false, maxResults, firstResult );
    }

    private List<TbTipoUsuario> findTbTipoUsuarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbTipoUsuario.class ) );
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

    public TbTipoUsuario findTbTipoUsuario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbTipoUsuario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbTipoUsuarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbTipoUsuario> rt = cq.from( TbTipoUsuario.class );
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
