/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbEstadoCivil;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbFuncionario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbEstadoCivilJpaController implements Serializable
{

    public TbEstadoCivilJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEstadoCivil tbEstadoCivil )
    {
        if ( tbEstadoCivil.getTbFuncionarioList() == null )
        {
            tbEstadoCivil.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbEstadoCivil.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbEstadoCivil.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbEstadoCivil );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbEstadoCivil.getTbFuncionarioList() )
            {
                TbEstadoCivil oldFkEstadoCivilOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkEstadoCivil();
                tbFuncionarioListTbFuncionario.setFkEstadoCivil( tbEstadoCivil );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkEstadoCivilOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkEstadoCivilOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkEstadoCivilOfTbFuncionarioListTbFuncionario = em.merge( oldFkEstadoCivilOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbEstadoCivil tbEstadoCivil ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEstadoCivil persistentTbEstadoCivil = em.find( TbEstadoCivil.class, tbEstadoCivil.getPkEstadoCivil() );
            List<TbFuncionario> tbFuncionarioListOld = persistentTbEstadoCivil.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbEstadoCivil.getTbFuncionarioList();
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbEstadoCivil.setTbFuncionarioList( tbFuncionarioListNew );
            tbEstadoCivil = em.merge( tbEstadoCivil );
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkEstadoCivil( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbEstadoCivil oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkEstadoCivil();
                    tbFuncionarioListNewTbFuncionario.setFkEstadoCivil( tbEstadoCivil );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario != null && !oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario.equals( tbEstadoCivil ) )
                    {
                        oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkEstadoCivilOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbEstadoCivil.getPkEstadoCivil();
                if ( findTbEstadoCivil( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEstadoCivil with id " + id + " no longer exists." );
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
            TbEstadoCivil tbEstadoCivil;
            try
            {
                tbEstadoCivil = em.getReference( TbEstadoCivil.class, id );
                tbEstadoCivil.getPkEstadoCivil();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEstadoCivil with id " + id + " no longer exists.", enfe );
            }
            List<TbFuncionario> tbFuncionarioList = tbEstadoCivil.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkEstadoCivil( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbEstadoCivil );
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

    public List<TbEstadoCivil> findTbEstadoCivilEntities()
    {
        return findTbEstadoCivilEntities( true, -1, -1 );
    }

    public List<TbEstadoCivil> findTbEstadoCivilEntities( int maxResults, int firstResult )
    {
        return findTbEstadoCivilEntities( false, maxResults, firstResult );
    }

    private List<TbEstadoCivil> findTbEstadoCivilEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEstadoCivil.class ) );
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

    public TbEstadoCivil findTbEstadoCivil( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEstadoCivil.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEstadoCivilCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEstadoCivil> rt = cq.from( TbEstadoCivil.class );
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
