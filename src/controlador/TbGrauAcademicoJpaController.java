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
import entity.TbFuncionario;
import entity.TbGrauAcademico;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbGrauAcademicoJpaController implements Serializable
{

    public TbGrauAcademicoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbGrauAcademico tbGrauAcademico )
    {
        if ( tbGrauAcademico.getTbFuncionarioList() == null )
        {
            tbGrauAcademico.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbGrauAcademico.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbGrauAcademico.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbGrauAcademico );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbGrauAcademico.getTbFuncionarioList() )
            {
                TbGrauAcademico oldFkGrauAcademicoOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkGrauAcademico();
                tbFuncionarioListTbFuncionario.setFkGrauAcademico( tbGrauAcademico );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkGrauAcademicoOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkGrauAcademicoOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkGrauAcademicoOfTbFuncionarioListTbFuncionario = em.merge( oldFkGrauAcademicoOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbGrauAcademico tbGrauAcademico ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbGrauAcademico persistentTbGrauAcademico = em.find( TbGrauAcademico.class, tbGrauAcademico.getPkGrauAcademico() );
            List<TbFuncionario> tbFuncionarioListOld = persistentTbGrauAcademico.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbGrauAcademico.getTbFuncionarioList();
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbGrauAcademico.setTbFuncionarioList( tbFuncionarioListNew );
            tbGrauAcademico = em.merge( tbGrauAcademico );
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkGrauAcademico( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbGrauAcademico oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkGrauAcademico();
                    tbFuncionarioListNewTbFuncionario.setFkGrauAcademico( tbGrauAcademico );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario != null && !oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario.equals( tbGrauAcademico ) )
                    {
                        oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkGrauAcademicoOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbGrauAcademico.getPkGrauAcademico();
                if ( findTbGrauAcademico( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbGrauAcademico with id " + id + " no longer exists." );
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
            TbGrauAcademico tbGrauAcademico;
            try
            {
                tbGrauAcademico = em.getReference( TbGrauAcademico.class, id );
                tbGrauAcademico.getPkGrauAcademico();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbGrauAcademico with id " + id + " no longer exists.", enfe );
            }
            List<TbFuncionario> tbFuncionarioList = tbGrauAcademico.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkGrauAcademico( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbGrauAcademico );
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

    public List<TbGrauAcademico> findTbGrauAcademicoEntities()
    {
        return findTbGrauAcademicoEntities( true, -1, -1 );
    }

    public List<TbGrauAcademico> findTbGrauAcademicoEntities( int maxResults, int firstResult )
    {
        return findTbGrauAcademicoEntities( false, maxResults, firstResult );
    }

    private List<TbGrauAcademico> findTbGrauAcademicoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbGrauAcademico.class ) );
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

    public TbGrauAcademico findTbGrauAcademico( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbGrauAcademico.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbGrauAcademicoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbGrauAcademico> rt = cq.from( TbGrauAcademico.class );
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
