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
import entity.TbFuncionario;
import entity.TbItemSubsidiosFuncionario;
import entity.TbSubsidios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbItemSubsidiosFuncionarioJpaController implements Serializable
{

    public TbItemSubsidiosFuncionarioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionario )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario idFuncionarioFK = tbItemSubsidiosFuncionario.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK = em.getReference( idFuncionarioFK.getClass(), idFuncionarioFK.getIdFuncionario() );
                tbItemSubsidiosFuncionario.setIdFuncionarioFK( idFuncionarioFK );
            }
            TbSubsidios idSubsidioFK = tbItemSubsidiosFuncionario.getIdSubsidioFK();
            if ( idSubsidioFK != null )
            {
                idSubsidioFK = em.getReference( idSubsidioFK.getClass(), idSubsidioFK.getIdSubsidios() );
                tbItemSubsidiosFuncionario.setIdSubsidioFK( idSubsidioFK );
            }
            em.persist( tbItemSubsidiosFuncionario );
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbItemSubsidiosFuncionarioList().add( tbItemSubsidiosFuncionario );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            if ( idSubsidioFK != null )
            {
                idSubsidioFK.getTbItemSubsidiosFuncionarioList().add( tbItemSubsidiosFuncionario );
                idSubsidioFK = em.merge( idSubsidioFK );
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

    public void edit( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionario ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbItemSubsidiosFuncionario persistentTbItemSubsidiosFuncionario = em.find( TbItemSubsidiosFuncionario.class, tbItemSubsidiosFuncionario.getIdItemSubsidiosFuncionario() );
            TbFuncionario idFuncionarioFKOld = persistentTbItemSubsidiosFuncionario.getIdFuncionarioFK();
            TbFuncionario idFuncionarioFKNew = tbItemSubsidiosFuncionario.getIdFuncionarioFK();
            TbSubsidios idSubsidioFKOld = persistentTbItemSubsidiosFuncionario.getIdSubsidioFK();
            TbSubsidios idSubsidioFKNew = tbItemSubsidiosFuncionario.getIdSubsidioFK();
            if ( idFuncionarioFKNew != null )
            {
                idFuncionarioFKNew = em.getReference( idFuncionarioFKNew.getClass(), idFuncionarioFKNew.getIdFuncionario() );
                tbItemSubsidiosFuncionario.setIdFuncionarioFK( idFuncionarioFKNew );
            }
            if ( idSubsidioFKNew != null )
            {
                idSubsidioFKNew = em.getReference( idSubsidioFKNew.getClass(), idSubsidioFKNew.getIdSubsidios() );
                tbItemSubsidiosFuncionario.setIdSubsidioFK( idSubsidioFKNew );
            }
            tbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionario );
            if ( idFuncionarioFKOld != null && !idFuncionarioFKOld.equals( idFuncionarioFKNew ) )
            {
                idFuncionarioFKOld.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionario );
                idFuncionarioFKOld = em.merge( idFuncionarioFKOld );
            }
            if ( idFuncionarioFKNew != null && !idFuncionarioFKNew.equals( idFuncionarioFKOld ) )
            {
                idFuncionarioFKNew.getTbItemSubsidiosFuncionarioList().add( tbItemSubsidiosFuncionario );
                idFuncionarioFKNew = em.merge( idFuncionarioFKNew );
            }
            if ( idSubsidioFKOld != null && !idSubsidioFKOld.equals( idSubsidioFKNew ) )
            {
                idSubsidioFKOld.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionario );
                idSubsidioFKOld = em.merge( idSubsidioFKOld );
            }
            if ( idSubsidioFKNew != null && !idSubsidioFKNew.equals( idSubsidioFKOld ) )
            {
                idSubsidioFKNew.getTbItemSubsidiosFuncionarioList().add( tbItemSubsidiosFuncionario );
                idSubsidioFKNew = em.merge( idSubsidioFKNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbItemSubsidiosFuncionario.getIdItemSubsidiosFuncionario();
                if ( findTbItemSubsidiosFuncionario( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbItemSubsidiosFuncionario with id " + id + " no longer exists." );
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
            TbItemSubsidiosFuncionario tbItemSubsidiosFuncionario;
            try
            {
                tbItemSubsidiosFuncionario = em.getReference( TbItemSubsidiosFuncionario.class, id );
                tbItemSubsidiosFuncionario.getIdItemSubsidiosFuncionario();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbItemSubsidiosFuncionario with id " + id + " no longer exists.", enfe );
            }
            TbFuncionario idFuncionarioFK = tbItemSubsidiosFuncionario.getIdFuncionarioFK();
            if ( idFuncionarioFK != null )
            {
                idFuncionarioFK.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionario );
                idFuncionarioFK = em.merge( idFuncionarioFK );
            }
            TbSubsidios idSubsidioFK = tbItemSubsidiosFuncionario.getIdSubsidioFK();
            if ( idSubsidioFK != null )
            {
                idSubsidioFK.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionario );
                idSubsidioFK = em.merge( idSubsidioFK );
            }
            em.remove( tbItemSubsidiosFuncionario );
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

    public List<TbItemSubsidiosFuncionario> findTbItemSubsidiosFuncionarioEntities()
    {
        return findTbItemSubsidiosFuncionarioEntities( true, -1, -1 );
    }

    public List<TbItemSubsidiosFuncionario> findTbItemSubsidiosFuncionarioEntities( int maxResults, int firstResult )
    {
        return findTbItemSubsidiosFuncionarioEntities( false, maxResults, firstResult );
    }

    private List<TbItemSubsidiosFuncionario> findTbItemSubsidiosFuncionarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbItemSubsidiosFuncionario.class ) );
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

    public TbItemSubsidiosFuncionario findTbItemSubsidiosFuncionario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbItemSubsidiosFuncionario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbItemSubsidiosFuncionarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbItemSubsidiosFuncionario> rt = cq.from( TbItemSubsidiosFuncionario.class );
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
