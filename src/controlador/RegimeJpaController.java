/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.Regime;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbFornecedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class RegimeJpaController implements Serializable
{

    public RegimeJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Regime regime )
    {
        if ( regime.getTbFornecedorList() == null )
        {
            regime.setTbFornecedorList( new ArrayList<TbFornecedor>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbFornecedor> attachedTbFornecedorList = new ArrayList<TbFornecedor>();
            for ( TbFornecedor tbFornecedorListTbFornecedorToAttach : regime.getTbFornecedorList() )
            {
                tbFornecedorListTbFornecedorToAttach = em.getReference( tbFornecedorListTbFornecedorToAttach.getClass(), tbFornecedorListTbFornecedorToAttach.getCodigo() );
                attachedTbFornecedorList.add( tbFornecedorListTbFornecedorToAttach );
            }
            regime.setTbFornecedorList( attachedTbFornecedorList );
            em.persist( regime );
            for ( TbFornecedor tbFornecedorListTbFornecedor : regime.getTbFornecedorList() )
            {
                Regime oldFkRegimeOfTbFornecedorListTbFornecedor = tbFornecedorListTbFornecedor.getFkRegime();
                tbFornecedorListTbFornecedor.setFkRegime( regime );
                tbFornecedorListTbFornecedor = em.merge( tbFornecedorListTbFornecedor );
                if ( oldFkRegimeOfTbFornecedorListTbFornecedor != null )
                {
                    oldFkRegimeOfTbFornecedorListTbFornecedor.getTbFornecedorList().remove( tbFornecedorListTbFornecedor );
                    oldFkRegimeOfTbFornecedorListTbFornecedor = em.merge( oldFkRegimeOfTbFornecedorListTbFornecedor );
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

    public void edit( Regime regime ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Regime persistentRegime = em.find( Regime.class, regime.getPkRegime() );
            List<TbFornecedor> tbFornecedorListOld = persistentRegime.getTbFornecedorList();
            List<TbFornecedor> tbFornecedorListNew = regime.getTbFornecedorList();
            List<TbFornecedor> attachedTbFornecedorListNew = new ArrayList<TbFornecedor>();
            for ( TbFornecedor tbFornecedorListNewTbFornecedorToAttach : tbFornecedorListNew )
            {
                tbFornecedorListNewTbFornecedorToAttach = em.getReference( tbFornecedorListNewTbFornecedorToAttach.getClass(), tbFornecedorListNewTbFornecedorToAttach.getCodigo() );
                attachedTbFornecedorListNew.add( tbFornecedorListNewTbFornecedorToAttach );
            }
            tbFornecedorListNew = attachedTbFornecedorListNew;
            regime.setTbFornecedorList( tbFornecedorListNew );
            regime = em.merge( regime );
            for ( TbFornecedor tbFornecedorListOldTbFornecedor : tbFornecedorListOld )
            {
                if ( !tbFornecedorListNew.contains( tbFornecedorListOldTbFornecedor ) )
                {
                    tbFornecedorListOldTbFornecedor.setFkRegime( null );
                    tbFornecedorListOldTbFornecedor = em.merge( tbFornecedorListOldTbFornecedor );
                }
            }
            for ( TbFornecedor tbFornecedorListNewTbFornecedor : tbFornecedorListNew )
            {
                if ( !tbFornecedorListOld.contains( tbFornecedorListNewTbFornecedor ) )
                {
                    Regime oldFkRegimeOfTbFornecedorListNewTbFornecedor = tbFornecedorListNewTbFornecedor.getFkRegime();
                    tbFornecedorListNewTbFornecedor.setFkRegime( regime );
                    tbFornecedorListNewTbFornecedor = em.merge( tbFornecedorListNewTbFornecedor );
                    if ( oldFkRegimeOfTbFornecedorListNewTbFornecedor != null && !oldFkRegimeOfTbFornecedorListNewTbFornecedor.equals( regime ) )
                    {
                        oldFkRegimeOfTbFornecedorListNewTbFornecedor.getTbFornecedorList().remove( tbFornecedorListNewTbFornecedor );
                        oldFkRegimeOfTbFornecedorListNewTbFornecedor = em.merge( oldFkRegimeOfTbFornecedorListNewTbFornecedor );
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
                Integer id = regime.getPkRegime();
                if ( findRegime( id ) == null )
                {
                    throw new NonexistentEntityException( "The regime with id " + id + " no longer exists." );
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
            Regime regime;
            try
            {
                regime = em.getReference( Regime.class, id );
                regime.getPkRegime();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The regime with id " + id + " no longer exists.", enfe );
            }
            List<TbFornecedor> tbFornecedorList = regime.getTbFornecedorList();
            for ( TbFornecedor tbFornecedorListTbFornecedor : tbFornecedorList )
            {
                tbFornecedorListTbFornecedor.setFkRegime( null );
                tbFornecedorListTbFornecedor = em.merge( tbFornecedorListTbFornecedor );
            }
            em.remove( regime );
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

    public List<Regime> findRegimeEntities()
    {
        return findRegimeEntities( true, -1, -1 );
    }

    public List<Regime> findRegimeEntities( int maxResults, int firstResult )
    {
        return findRegimeEntities( false, maxResults, firstResult );
    }

    private List<Regime> findRegimeEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Regime.class ) );
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

    public Regime findRegime( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Regime.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getRegimeCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Regime> rt = cq.from( Regime.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
