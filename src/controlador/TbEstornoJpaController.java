/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbArmazem;
import entity.TbEstorno;
import entity.TbUsuario;
import entity.TbItemEstorno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbEstornoJpaController implements Serializable
{

    public TbEstornoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbEstorno tbEstorno )
    {
        if ( tbEstorno.getTbItemEstornoList() == null )
        {
            tbEstorno.setTbItemEstornoList( new ArrayList<TbItemEstorno>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem idArmazemFK = tbEstorno.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                tbEstorno.setIdArmazemFK( idArmazemFK );
            }
            TbUsuario fkUsuario = tbEstorno.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbEstorno.setFkUsuario( fkUsuario );
            }
            List<TbItemEstorno> attachedTbItemEstornoList = new ArrayList<TbItemEstorno>();
            for ( TbItemEstorno tbItemEstornoListTbItemEstornoToAttach : tbEstorno.getTbItemEstornoList() )
            {
                tbItemEstornoListTbItemEstornoToAttach = em.getReference( tbItemEstornoListTbItemEstornoToAttach.getClass(), tbItemEstornoListTbItemEstornoToAttach.getCodigo() );
                attachedTbItemEstornoList.add( tbItemEstornoListTbItemEstornoToAttach );
            }
            tbEstorno.setTbItemEstornoList( attachedTbItemEstornoList );
            em.persist( tbEstorno );
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbEstornoList().add( tbEstorno );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbEstornoList().add( tbEstorno );
                fkUsuario = em.merge( fkUsuario );
            }
            for ( TbItemEstorno tbItemEstornoListTbItemEstorno : tbEstorno.getTbItemEstornoList() )
            {
                TbEstorno oldFkEstornoOfTbItemEstornoListTbItemEstorno = tbItemEstornoListTbItemEstorno.getFkEstorno();
                tbItemEstornoListTbItemEstorno.setFkEstorno( tbEstorno );
                tbItemEstornoListTbItemEstorno = em.merge( tbItemEstornoListTbItemEstorno );
                if ( oldFkEstornoOfTbItemEstornoListTbItemEstorno != null )
                {
                    oldFkEstornoOfTbItemEstornoListTbItemEstorno.getTbItemEstornoList().remove( tbItemEstornoListTbItemEstorno );
                    oldFkEstornoOfTbItemEstornoListTbItemEstorno = em.merge( oldFkEstornoOfTbItemEstornoListTbItemEstorno );
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

    public void edit( TbEstorno tbEstorno ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbEstorno persistentTbEstorno = em.find( TbEstorno.class, tbEstorno.getPkEstorno() );
            TbArmazem idArmazemFKOld = persistentTbEstorno.getIdArmazemFK();
            TbArmazem idArmazemFKNew = tbEstorno.getIdArmazemFK();
            TbUsuario fkUsuarioOld = persistentTbEstorno.getFkUsuario();
            TbUsuario fkUsuarioNew = tbEstorno.getFkUsuario();
            List<TbItemEstorno> tbItemEstornoListOld = persistentTbEstorno.getTbItemEstornoList();
            List<TbItemEstorno> tbItemEstornoListNew = tbEstorno.getTbItemEstornoList();
            List<String> illegalOrphanMessages = null;
            for ( TbItemEstorno tbItemEstornoListOldTbItemEstorno : tbItemEstornoListOld )
            {
                if ( !tbItemEstornoListNew.contains( tbItemEstornoListOldTbItemEstorno ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEstorno " + tbItemEstornoListOldTbItemEstorno + " since its fkEstorno field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                tbEstorno.setIdArmazemFK( idArmazemFKNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbEstorno.setFkUsuario( fkUsuarioNew );
            }
            List<TbItemEstorno> attachedTbItemEstornoListNew = new ArrayList<TbItemEstorno>();
            for ( TbItemEstorno tbItemEstornoListNewTbItemEstornoToAttach : tbItemEstornoListNew )
            {
                tbItemEstornoListNewTbItemEstornoToAttach = em.getReference( tbItemEstornoListNewTbItemEstornoToAttach.getClass(), tbItemEstornoListNewTbItemEstornoToAttach.getCodigo() );
                attachedTbItemEstornoListNew.add( tbItemEstornoListNewTbItemEstornoToAttach );
            }
            tbItemEstornoListNew = attachedTbItemEstornoListNew;
            tbEstorno.setTbItemEstornoList( tbItemEstornoListNew );
            tbEstorno = em.merge( tbEstorno );
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getTbEstornoList().remove( tbEstorno );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getTbEstornoList().add( tbEstorno );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbEstornoList().remove( tbEstorno );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbEstornoList().add( tbEstorno );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            for ( TbItemEstorno tbItemEstornoListNewTbItemEstorno : tbItemEstornoListNew )
            {
                if ( !tbItemEstornoListOld.contains( tbItemEstornoListNewTbItemEstorno ) )
                {
                    TbEstorno oldFkEstornoOfTbItemEstornoListNewTbItemEstorno = tbItemEstornoListNewTbItemEstorno.getFkEstorno();
                    tbItemEstornoListNewTbItemEstorno.setFkEstorno( tbEstorno );
                    tbItemEstornoListNewTbItemEstorno = em.merge( tbItemEstornoListNewTbItemEstorno );
                    if ( oldFkEstornoOfTbItemEstornoListNewTbItemEstorno != null && !oldFkEstornoOfTbItemEstornoListNewTbItemEstorno.equals( tbEstorno ) )
                    {
                        oldFkEstornoOfTbItemEstornoListNewTbItemEstorno.getTbItemEstornoList().remove( tbItemEstornoListNewTbItemEstorno );
                        oldFkEstornoOfTbItemEstornoListNewTbItemEstorno = em.merge( oldFkEstornoOfTbItemEstornoListNewTbItemEstorno );
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
                Integer id = tbEstorno.getPkEstorno();
                if ( findTbEstorno( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbEstorno with id " + id + " no longer exists." );
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
            TbEstorno tbEstorno;
            try
            {
                tbEstorno = em.getReference( TbEstorno.class, id );
                tbEstorno.getPkEstorno();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbEstorno with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbItemEstorno> tbItemEstornoListOrphanCheck = tbEstorno.getTbItemEstornoList();
            for ( TbItemEstorno tbItemEstornoListOrphanCheckTbItemEstorno : tbItemEstornoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbEstorno (" + tbEstorno + ") cannot be destroyed since the TbItemEstorno " + tbItemEstornoListOrphanCheckTbItemEstorno + " in its tbItemEstornoList field has a non-nullable fkEstorno field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbArmazem idArmazemFK = tbEstorno.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbEstornoList().remove( tbEstorno );
                idArmazemFK = em.merge( idArmazemFK );
            }
            TbUsuario fkUsuario = tbEstorno.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbEstornoList().remove( tbEstorno );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( tbEstorno );
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

    public List<TbEstorno> findTbEstornoEntities()
    {
        return findTbEstornoEntities( true, -1, -1 );
    }

    public List<TbEstorno> findTbEstornoEntities( int maxResults, int firstResult )
    {
        return findTbEstornoEntities( false, maxResults, firstResult );
    }

    private List<TbEstorno> findTbEstornoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbEstorno.class ) );
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

    public TbEstorno findTbEstorno( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbEstorno.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbEstornoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbEstorno> rt = cq.from( TbEstorno.class );
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
