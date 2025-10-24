/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.TbFuncao;
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
public class TbFuncaoJpaController implements Serializable
{

    public TbFuncaoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbFuncao tbFuncao )
    {
        if ( tbFuncao.getTbFuncionarioList() == null )
        {
            tbFuncao.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbFuncao.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbFuncao.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( tbFuncao );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncao.getTbFuncionarioList() )
            {
                TbFuncao oldFkFuncaoOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkFuncao();
                tbFuncionarioListTbFuncionario.setFkFuncao( tbFuncao );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkFuncaoOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkFuncaoOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkFuncaoOfTbFuncionarioListTbFuncionario = em.merge( oldFkFuncaoOfTbFuncionarioListTbFuncionario );
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

    public void edit( TbFuncao tbFuncao ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncao persistentTbFuncao = em.find( TbFuncao.class, tbFuncao.getPkFuncao() );
            List<TbFuncionario> tbFuncionarioListOld = persistentTbFuncao.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbFuncao.getTbFuncionarioList();
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbFuncao.setTbFuncionarioList( tbFuncionarioListNew );
            tbFuncao = em.merge( tbFuncao );
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkFuncao( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbFuncao oldFkFuncaoOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkFuncao();
                    tbFuncionarioListNewTbFuncionario.setFkFuncao( tbFuncao );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkFuncaoOfTbFuncionarioListNewTbFuncionario != null && !oldFkFuncaoOfTbFuncionarioListNewTbFuncionario.equals( tbFuncao ) )
                    {
                        oldFkFuncaoOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkFuncaoOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkFuncaoOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = tbFuncao.getPkFuncao();
                if ( findTbFuncao( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbFuncao with id " + id + " no longer exists." );
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
            TbFuncao tbFuncao;
            try
            {
                tbFuncao = em.getReference( TbFuncao.class, id );
                tbFuncao.getPkFuncao();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbFuncao with id " + id + " no longer exists.", enfe );
            }
            List<TbFuncionario> tbFuncionarioList = tbFuncao.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkFuncao( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            em.remove( tbFuncao );
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

    public List<TbFuncao> findTbFuncaoEntities()
    {
        return findTbFuncaoEntities( true, -1, -1 );
    }

    public List<TbFuncao> findTbFuncaoEntities( int maxResults, int firstResult )
    {
        return findTbFuncaoEntities( false, maxResults, firstResult );
    }

    private List<TbFuncao> findTbFuncaoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbFuncao.class ) );
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

    public TbFuncao findTbFuncao( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbFuncao.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbFuncaoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbFuncao> rt = cq.from( TbFuncao.class );
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
