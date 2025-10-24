/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbEspecialidade;
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
public class TbEspecialidadeJpaController implements Serializable
{

    public TbEspecialidadeJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEspecialidade tbEspecialidade )
    {
        if ( tbEspecialidade.getTbFuncionarioList() == null )
        {
            tbEspecialidade.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbEspecialidade.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbEspecialidade.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbEspecialidade );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbEspecialidade.getTbFuncionarioList() )
            {
                TbEspecialidade oldFkEspecialidadeOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkEspecialidade();
                tbFuncionarioListTbFuncionario.setFkEspecialidade( tbEspecialidade );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkEspecialidadeOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkEspecialidadeOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkEspecialidadeOfTbFuncionarioListTbFuncionario = em.merge( oldFkEspecialidadeOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbEspecialidade tbEspecialidade ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEspecialidade persistentTbEspecialidade = em.find( TbEspecialidade.class, tbEspecialidade.getPkEspecialidade() );
            List<TbFuncionario> tbFuncionarioListOld = persistentTbEspecialidade.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbEspecialidade.getTbFuncionarioList();
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbEspecialidade.setTbFuncionarioList( tbFuncionarioListNew );
            tbEspecialidade = em.merge( tbEspecialidade );
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkEspecialidade( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbEspecialidade oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkEspecialidade();
                    tbFuncionarioListNewTbFuncionario.setFkEspecialidade( tbEspecialidade );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario != null && !oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario.equals( tbEspecialidade ) )
                    {
                        oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkEspecialidadeOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbEspecialidade.getPkEspecialidade();
                if ( findTbEspecialidade( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEspecialidade with id " + id + " no longer exists." );
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
            TbEspecialidade tbEspecialidade;
            try
            {
                tbEspecialidade = em.getReference( TbEspecialidade.class, id );
                tbEspecialidade.getPkEspecialidade();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEspecialidade with id " + id + " no longer exists.", enfe );
            }
            List<TbFuncionario> tbFuncionarioList = tbEspecialidade.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkEspecialidade( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbEspecialidade );
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

    public List<TbEspecialidade> findTbEspecialidadeEntities()
    {
        return findTbEspecialidadeEntities( true, -1, -1 );
    }

    public List<TbEspecialidade> findTbEspecialidadeEntities( int maxResults, int firstResult )
    {
        return findTbEspecialidadeEntities( false, maxResults, firstResult );
    }

    private List<TbEspecialidade> findTbEspecialidadeEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEspecialidade.class ) );
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

    public TbEspecialidade findTbEspecialidade( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEspecialidade.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEspecialidadeCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEspecialidade> rt = cq.from( TbEspecialidade.class );
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
