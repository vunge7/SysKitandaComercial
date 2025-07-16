/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Modalidade;
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
public class ModalidadeJpaController implements Serializable
{

    public ModalidadeJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Modalidade modalidade )
    {
        if ( modalidade.getTbFuncionarioList() == null )
        {
            modalidade.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : modalidade.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            modalidade.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( modalidade );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : modalidade.getTbFuncionarioList() )
            {
                Modalidade oldFkModalidadeOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkModalidade();
                tbFuncionarioListTbFuncionario.setFkModalidade( modalidade );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkModalidadeOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkModalidadeOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkModalidadeOfTbFuncionarioListTbFuncionario = em.merge( oldFkModalidadeOfTbFuncionarioListTbFuncionario );
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

    public void edit( Modalidade modalidade ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidade persistentModalidade = em.find( Modalidade.class, modalidade.getPkModalidade() );
            List<TbFuncionario> tbFuncionarioListOld = persistentModalidade.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = modalidade.getTbFuncionarioList();
            List<String> illegalOrphanMessages = null;
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbFuncionario " + tbFuncionarioListOldTbFuncionario + " since its fkModalidade field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            modalidade.setTbFuncionarioList( tbFuncionarioListNew );
            modalidade = em.merge( modalidade );
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    Modalidade oldFkModalidadeOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkModalidade();
                    tbFuncionarioListNewTbFuncionario.setFkModalidade( modalidade );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkModalidadeOfTbFuncionarioListNewTbFuncionario != null && !oldFkModalidadeOfTbFuncionarioListNewTbFuncionario.equals( modalidade ) )
                    {
                        oldFkModalidadeOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkModalidadeOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkModalidadeOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = modalidade.getPkModalidade();
                if ( findModalidade( id ) == null )
                {
                    throw new NonexistentEntityException( "The modalidade with id " + id + " no longer exists." );
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
            Modalidade modalidade;
            try
            {
                modalidade = em.getReference( Modalidade.class, id );
                modalidade.getPkModalidade();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The modalidade with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbFuncionario> tbFuncionarioListOrphanCheck = modalidade.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListOrphanCheckTbFuncionario : tbFuncionarioListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Modalidade (" + modalidade + ") cannot be destroyed since the TbFuncionario " + tbFuncionarioListOrphanCheckTbFuncionario + " in its tbFuncionarioList field has a non-nullable fkModalidade field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( modalidade );
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

    public List<Modalidade> findModalidadeEntities()
    {
        return findModalidadeEntities( true, -1, -1 );
    }

    public List<Modalidade> findModalidadeEntities( int maxResults, int firstResult )
    {
        return findModalidadeEntities( false, maxResults, firstResult );
    }

    private List<Modalidade> findModalidadeEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Modalidade.class ) );
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

    public Modalidade findModalidade( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Modalidade.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getModalidadeCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modalidade> rt = cq.from( Modalidade.class );
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
