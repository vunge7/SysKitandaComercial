/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbDepartamento;
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
public class TbDepartamentoJpaController implements Serializable
{

    public TbDepartamentoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbDepartamento tbDepartamento )
    {
        if ( tbDepartamento.getTbFuncionarioList() == null )
        {
            tbDepartamento.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbDepartamento.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbDepartamento.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbDepartamento );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbDepartamento.getTbFuncionarioList() )
            {
                TbDepartamento oldFkDepartamentoOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkDepartamento();
                tbFuncionarioListTbFuncionario.setFkDepartamento( tbDepartamento );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkDepartamentoOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkDepartamentoOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkDepartamentoOfTbFuncionarioListTbFuncionario = em.merge( oldFkDepartamentoOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbDepartamento tbDepartamento ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbDepartamento persistentTbDepartamento = em.find( TbDepartamento.class, tbDepartamento.getPkDepartamento() );
            List<TbFuncionario> tbFuncionarioListOld = persistentTbDepartamento.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbDepartamento.getTbFuncionarioList();
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbDepartamento.setTbFuncionarioList( tbFuncionarioListNew );
            tbDepartamento = em.merge( tbDepartamento );
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkDepartamento( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbDepartamento oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkDepartamento();
                    tbFuncionarioListNewTbFuncionario.setFkDepartamento( tbDepartamento );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario != null && !oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario.equals( tbDepartamento ) )
                    {
                        oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkDepartamentoOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbDepartamento.getPkDepartamento();
                if ( findTbDepartamento( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbDepartamento with id " + id + " no longer exists." );
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
            TbDepartamento tbDepartamento;
            try
            {
                tbDepartamento = em.getReference( TbDepartamento.class, id );
                tbDepartamento.getPkDepartamento();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbDepartamento with id " + id + " no longer exists.", enfe );
            }
            List<TbFuncionario> tbFuncionarioList = tbDepartamento.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkDepartamento( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbDepartamento );
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

    public List<TbDepartamento> findTbDepartamentoEntities()
    {
        return findTbDepartamentoEntities( true, -1, -1 );
    }

    public List<TbDepartamento> findTbDepartamentoEntities( int maxResults, int firstResult )
    {
        return findTbDepartamentoEntities( false, maxResults, firstResult );
    }

    private List<TbDepartamento> findTbDepartamentoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbDepartamento.class ) );
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

    public TbDepartamento findTbDepartamento( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbDepartamento.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbDepartamentoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbDepartamento> rt = cq.from( TbDepartamento.class );
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
