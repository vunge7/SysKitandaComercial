/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.TbSexo;
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
 * @author lenovo
 */
public class TbSexoJpaController implements Serializable
{

    public TbSexoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbSexo tbSexo )
    {
        if ( tbSexo.getTbUsuarioList() == null )
        {
            tbSexo.setTbUsuarioList( new ArrayList<TbUsuario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbUsuario> attachedTbUsuarioList = new ArrayList<TbUsuario>();
            for ( TbUsuario tbUsuarioListTbUsuarioToAttach : tbSexo.getTbUsuarioList() )
            {
                tbUsuarioListTbUsuarioToAttach = em.getReference( tbUsuarioListTbUsuarioToAttach.getClass(), tbUsuarioListTbUsuarioToAttach.getCodigo() );
                attachedTbUsuarioList.add( tbUsuarioListTbUsuarioToAttach );
            }
            tbSexo.setTbUsuarioList( attachedTbUsuarioList );
            em.persist( tbSexo );
            for ( TbUsuario tbUsuarioListTbUsuario : tbSexo.getTbUsuarioList() )
            {
                TbSexo oldCodigoSexoOfTbUsuarioListTbUsuario = tbUsuarioListTbUsuario.getCodigoSexo();
                tbUsuarioListTbUsuario.setCodigoSexo( tbSexo );
                tbUsuarioListTbUsuario = em.merge( tbUsuarioListTbUsuario );
                if ( oldCodigoSexoOfTbUsuarioListTbUsuario != null )
                {
                    oldCodigoSexoOfTbUsuarioListTbUsuario.getTbUsuarioList().remove( tbUsuarioListTbUsuario );
                    oldCodigoSexoOfTbUsuarioListTbUsuario = em.merge( oldCodigoSexoOfTbUsuarioListTbUsuario );
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

    public void edit( TbSexo tbSexo ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSexo persistentTbSexo = em.find( TbSexo.class, tbSexo.getCodigo() );
            List<TbUsuario> tbUsuarioListOld = persistentTbSexo.getTbUsuarioList();
            List<TbUsuario> tbUsuarioListNew = tbSexo.getTbUsuarioList();
            List<String> illegalOrphanMessages = null;
            for ( TbUsuario tbUsuarioListOldTbUsuario : tbUsuarioListOld )
            {
                if ( !tbUsuarioListNew.contains( tbUsuarioListOldTbUsuario ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbUsuario " + tbUsuarioListOldTbUsuario + " since its codigoSexo field is not nullable." );
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
            tbSexo.setTbUsuarioList( tbUsuarioListNew );
            tbSexo = em.merge( tbSexo );
            for ( TbUsuario tbUsuarioListNewTbUsuario : tbUsuarioListNew )
            {
                if ( !tbUsuarioListOld.contains( tbUsuarioListNewTbUsuario ) )
                {
                    TbSexo oldCodigoSexoOfTbUsuarioListNewTbUsuario = tbUsuarioListNewTbUsuario.getCodigoSexo();
                    tbUsuarioListNewTbUsuario.setCodigoSexo( tbSexo );
                    tbUsuarioListNewTbUsuario = em.merge( tbUsuarioListNewTbUsuario );
                    if ( oldCodigoSexoOfTbUsuarioListNewTbUsuario != null && !oldCodigoSexoOfTbUsuarioListNewTbUsuario.equals( tbSexo ) )
                    {
                        oldCodigoSexoOfTbUsuarioListNewTbUsuario.getTbUsuarioList().remove( tbUsuarioListNewTbUsuario );
                        oldCodigoSexoOfTbUsuarioListNewTbUsuario = em.merge( oldCodigoSexoOfTbUsuarioListNewTbUsuario );
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
                Integer id = tbSexo.getCodigo();
                if ( findTbSexo( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbSexo with id " + id + " no longer exists." );
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
            TbSexo tbSexo;
            try
            {
                tbSexo = em.getReference( TbSexo.class, id );
                tbSexo.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbSexo with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbUsuario> tbUsuarioListOrphanCheck = tbSexo.getTbUsuarioList();
            for ( TbUsuario tbUsuarioListOrphanCheckTbUsuario : tbUsuarioListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbSexo (" + tbSexo + ") cannot be destroyed since the TbUsuario " + tbUsuarioListOrphanCheckTbUsuario + " in its tbUsuarioList field has a non-nullable codigoSexo field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( tbSexo );
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

    public List<TbSexo> findTbSexoEntities()
    {
        return findTbSexoEntities( true, -1, -1 );
    }

    public List<TbSexo> findTbSexoEntities( int maxResults, int firstResult )
    {
        return findTbSexoEntities( false, maxResults, firstResult );
    }

    private List<TbSexo> findTbSexoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbSexo.class ) );
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

    public TbSexo findTbSexo( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbSexo.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbSexoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbSexo> rt = cq.from( TbSexo.class );
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
