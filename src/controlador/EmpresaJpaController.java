/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entity.Empresa;
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
public class EmpresaJpaController implements Serializable
{

    public EmpresaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Empresa empresa )
    {
        if ( empresa.getTbFuncionarioList() == null )
        {
            empresa.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : empresa.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            empresa.setTbFuncionarioList( attachedTbFuncionarioList );
            em.persist( empresa );
            for ( TbFuncionario tbFuncionarioListTbFuncionario : empresa.getTbFuncionarioList() )
            {
                Empresa oldFkEmpresaOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkEmpresa();
                tbFuncionarioListTbFuncionario.setFkEmpresa( empresa );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkEmpresaOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkEmpresaOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkEmpresaOfTbFuncionarioListTbFuncionario = em.merge( oldFkEmpresaOfTbFuncionarioListTbFuncionario );
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

    public void edit( Empresa empresa ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find( Empresa.class, empresa.getPkEmpresa() );
            List<TbFuncionario> tbFuncionarioListOld = persistentEmpresa.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = empresa.getTbFuncionarioList();
            List<String> illegalOrphanMessages = null;
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbFuncionario " + tbFuncionarioListOldTbFuncionario + " since its fkEmpresa field is not nullable." );
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
            empresa.setTbFuncionarioList( tbFuncionarioListNew );
            empresa = em.merge( empresa );
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    Empresa oldFkEmpresaOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkEmpresa();
                    tbFuncionarioListNewTbFuncionario.setFkEmpresa( empresa );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkEmpresaOfTbFuncionarioListNewTbFuncionario != null && !oldFkEmpresaOfTbFuncionarioListNewTbFuncionario.equals( empresa ) )
                    {
                        oldFkEmpresaOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkEmpresaOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkEmpresaOfTbFuncionarioListNewTbFuncionario );
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
                Integer id = empresa.getPkEmpresa();
                if ( findEmpresa( id ) == null )
                {
                    throw new NonexistentEntityException( "The empresa with id " + id + " no longer exists." );
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
            Empresa empresa;
            try
            {
                empresa = em.getReference( Empresa.class, id );
                empresa.getPkEmpresa();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The empresa with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbFuncionario> tbFuncionarioListOrphanCheck = empresa.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListOrphanCheckTbFuncionario : tbFuncionarioListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This Empresa (" + empresa + ") cannot be destroyed since the TbFuncionario " + tbFuncionarioListOrphanCheckTbFuncionario + " in its tbFuncionarioList field has a non-nullable fkEmpresa field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( empresa );
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

    public List<Empresa> findEmpresaEntities()
    {
        return findEmpresaEntities( true, -1, -1 );
    }

    public List<Empresa> findEmpresaEntities( int maxResults, int firstResult )
    {
        return findEmpresaEntities( false, maxResults, firstResult );
    }

    private List<Empresa> findEmpresaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Empresa.class ) );
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

    public Empresa findEmpresa( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Empresa.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getEmpresaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from( Empresa.class );
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
