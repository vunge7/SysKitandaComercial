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
import entity.TbItemSubsidiosFuncionario;
import entity.TbSubsidios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbSubsidiosJpaController implements Serializable
{

    public TbSubsidiosJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbSubsidios tbSubsidios )
    {
        if ( tbSubsidios.getTbItemSubsidiosFuncionarioList() == null )
        {
            tbSubsidios.setTbItemSubsidiosFuncionarioList( new ArrayList<TbItemSubsidiosFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbItemSubsidiosFuncionario> attachedTbItemSubsidiosFuncionarioList = new ArrayList<TbItemSubsidiosFuncionario>();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach : tbSubsidios.getTbItemSubsidiosFuncionarioList() )
            {
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach = em.getReference( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach.getClass(), tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach.getIdItemSubsidiosFuncionario() );
                attachedTbItemSubsidiosFuncionarioList.add( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach );
            }
            tbSubsidios.setTbItemSubsidiosFuncionarioList( attachedTbItemSubsidiosFuncionarioList );
            em.persist( tbSubsidios );
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario : tbSubsidios.getTbItemSubsidiosFuncionarioList() )
            {
                TbSubsidios oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.getIdSubsidioFK();
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.setIdSubsidioFK( tbSubsidios );
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
                if ( oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario != null )
                {
                    oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
                    oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
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

    public void edit( TbSubsidios tbSubsidios ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbSubsidios persistentTbSubsidios = em.find( TbSubsidios.class, tbSubsidios.getIdSubsidios() );
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioListOld = persistentTbSubsidios.getTbItemSubsidiosFuncionarioList();
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioListNew = tbSubsidios.getTbItemSubsidiosFuncionarioList();
            List<TbItemSubsidiosFuncionario> attachedTbItemSubsidiosFuncionarioListNew = new ArrayList<TbItemSubsidiosFuncionario>();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach : tbItemSubsidiosFuncionarioListNew )
            {
                tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach = em.getReference( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach.getClass(), tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach.getIdItemSubsidiosFuncionario() );
                attachedTbItemSubsidiosFuncionarioListNew.add( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach );
            }
            tbItemSubsidiosFuncionarioListNew = attachedTbItemSubsidiosFuncionarioListNew;
            tbSubsidios.setTbItemSubsidiosFuncionarioList( tbItemSubsidiosFuncionarioListNew );
            tbSubsidios = em.merge( tbSubsidios );
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioListOld )
            {
                if ( !tbItemSubsidiosFuncionarioListNew.contains( tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario ) )
                {
                    tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario.setIdSubsidioFK( null );
                    tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario );
                }
            }
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioListNew )
            {
                if ( !tbItemSubsidiosFuncionarioListOld.contains( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario ) )
                {
                    TbSubsidios oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.getIdSubsidioFK();
                    tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.setIdSubsidioFK( tbSubsidios );
                    tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
                    if ( oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario != null && !oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.equals( tbSubsidios ) )
                    {
                        oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
                        oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = em.merge( oldIdSubsidioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
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
                Integer id = tbSubsidios.getIdSubsidios();
                if ( findTbSubsidios( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbSubsidios with id " + id + " no longer exists." );
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
            TbSubsidios tbSubsidios;
            try
            {
                tbSubsidios = em.getReference( TbSubsidios.class, id );
                tbSubsidios.getIdSubsidios();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbSubsidios with id " + id + " no longer exists.", enfe );
            }
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList = tbSubsidios.getTbItemSubsidiosFuncionarioList();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioList )
            {
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.setIdSubsidioFK( null );
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
            }
            em.remove( tbSubsidios );
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

    public List<TbSubsidios> findTbSubsidiosEntities()
    {
        return findTbSubsidiosEntities( true, -1, -1 );
    }

    public List<TbSubsidios> findTbSubsidiosEntities( int maxResults, int firstResult )
    {
        return findTbSubsidiosEntities( false, maxResults, firstResult );
    }

    private List<TbSubsidios> findTbSubsidiosEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbSubsidios.class ) );
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

    public TbSubsidios findTbSubsidios( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbSubsidios.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbSubsidiosCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbSubsidios> rt = cq.from( TbSubsidios.class );
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
