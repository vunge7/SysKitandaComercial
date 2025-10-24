/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.LinhaTransferencia;
import entity.TransferenciaArmazem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TransferenciaArmazemJpaController implements Serializable
{

    public TransferenciaArmazemJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TransferenciaArmazem transferenciaArmazem )
    {
        if ( transferenciaArmazem.getLinhaTransferenciaList() == null )
        {
            transferenciaArmazem.setLinhaTransferenciaList( new ArrayList<LinhaTransferencia>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LinhaTransferencia> attachedLinhaTransferenciaList = new ArrayList<LinhaTransferencia>();
            for ( LinhaTransferencia linhaTransferenciaListLinhaTransferenciaToAttach : transferenciaArmazem.getLinhaTransferenciaList() )
            {
                linhaTransferenciaListLinhaTransferenciaToAttach = em.getReference( linhaTransferenciaListLinhaTransferenciaToAttach.getClass(), linhaTransferenciaListLinhaTransferenciaToAttach.getPkLinhaTransferencia() );
                attachedLinhaTransferenciaList.add( linhaTransferenciaListLinhaTransferenciaToAttach );
            }
            transferenciaArmazem.setLinhaTransferenciaList( attachedLinhaTransferenciaList );
            em.persist( transferenciaArmazem );
            for ( LinhaTransferencia linhaTransferenciaListLinhaTransferencia : transferenciaArmazem.getLinhaTransferenciaList() )
            {
                TransferenciaArmazem oldFkTransferenciaArmazemOfLinhaTransferenciaListLinhaTransferencia = linhaTransferenciaListLinhaTransferencia.getFkTransferenciaArmazem();
                linhaTransferenciaListLinhaTransferencia.setFkTransferenciaArmazem( transferenciaArmazem );
                linhaTransferenciaListLinhaTransferencia = em.merge( linhaTransferenciaListLinhaTransferencia );
                if ( oldFkTransferenciaArmazemOfLinhaTransferenciaListLinhaTransferencia != null )
                {
                    oldFkTransferenciaArmazemOfLinhaTransferenciaListLinhaTransferencia.getLinhaTransferenciaList().remove( linhaTransferenciaListLinhaTransferencia );
                    oldFkTransferenciaArmazemOfLinhaTransferenciaListLinhaTransferencia = em.merge( oldFkTransferenciaArmazemOfLinhaTransferenciaListLinhaTransferencia );
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

    public void edit( TransferenciaArmazem transferenciaArmazem ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TransferenciaArmazem persistentTransferenciaArmazem = em.find( TransferenciaArmazem.class, transferenciaArmazem.getPkTransferenciaArmazem() );
            List<LinhaTransferencia> linhaTransferenciaListOld = persistentTransferenciaArmazem.getLinhaTransferenciaList();
            List<LinhaTransferencia> linhaTransferenciaListNew = transferenciaArmazem.getLinhaTransferenciaList();
            List<String> illegalOrphanMessages = null;
            for ( LinhaTransferencia linhaTransferenciaListOldLinhaTransferencia : linhaTransferenciaListOld )
            {
                if ( !linhaTransferenciaListNew.contains( linhaTransferenciaListOldLinhaTransferencia ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain LinhaTransferencia " + linhaTransferenciaListOldLinhaTransferencia + " since its fkTransferenciaArmazem field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<LinhaTransferencia> attachedLinhaTransferenciaListNew = new ArrayList<LinhaTransferencia>();
            for ( LinhaTransferencia linhaTransferenciaListNewLinhaTransferenciaToAttach : linhaTransferenciaListNew )
            {
                linhaTransferenciaListNewLinhaTransferenciaToAttach = em.getReference( linhaTransferenciaListNewLinhaTransferenciaToAttach.getClass(), linhaTransferenciaListNewLinhaTransferenciaToAttach.getPkLinhaTransferencia() );
                attachedLinhaTransferenciaListNew.add( linhaTransferenciaListNewLinhaTransferenciaToAttach );
            }
            linhaTransferenciaListNew = attachedLinhaTransferenciaListNew;
            transferenciaArmazem.setLinhaTransferenciaList( linhaTransferenciaListNew );
            transferenciaArmazem = em.merge( transferenciaArmazem );
            for ( LinhaTransferencia linhaTransferenciaListNewLinhaTransferencia : linhaTransferenciaListNew )
            {
                if ( !linhaTransferenciaListOld.contains( linhaTransferenciaListNewLinhaTransferencia ) )
                {
                    TransferenciaArmazem oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia = linhaTransferenciaListNewLinhaTransferencia.getFkTransferenciaArmazem();
                    linhaTransferenciaListNewLinhaTransferencia.setFkTransferenciaArmazem( transferenciaArmazem );
                    linhaTransferenciaListNewLinhaTransferencia = em.merge( linhaTransferenciaListNewLinhaTransferencia );
                    if ( oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia != null && !oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia.equals( transferenciaArmazem ) )
                    {
                        oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia.getLinhaTransferenciaList().remove( linhaTransferenciaListNewLinhaTransferencia );
                        oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia = em.merge( oldFkTransferenciaArmazemOfLinhaTransferenciaListNewLinhaTransferencia );
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
                Integer id = transferenciaArmazem.getPkTransferenciaArmazem();
                if ( findTransferenciaArmazem( id ) == null )
                {
                    throw new NonexistentEntityException( "The transferenciaArmazem with id " + id + " no longer exists." );
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
            TransferenciaArmazem transferenciaArmazem;
            try
            {
                transferenciaArmazem = em.getReference( TransferenciaArmazem.class, id );
                transferenciaArmazem.getPkTransferenciaArmazem();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The transferenciaArmazem with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<LinhaTransferencia> linhaTransferenciaListOrphanCheck = transferenciaArmazem.getLinhaTransferenciaList();
            for ( LinhaTransferencia linhaTransferenciaListOrphanCheckLinhaTransferencia : linhaTransferenciaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TransferenciaArmazem (" + transferenciaArmazem + ") cannot be destroyed since the LinhaTransferencia " + linhaTransferenciaListOrphanCheckLinhaTransferencia + " in its linhaTransferenciaList field has a non-nullable fkTransferenciaArmazem field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            em.remove( transferenciaArmazem );
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

    public List<TransferenciaArmazem> findTransferenciaArmazemEntities()
    {
        return findTransferenciaArmazemEntities( true, -1, -1 );
    }

    public List<TransferenciaArmazem> findTransferenciaArmazemEntities( int maxResults, int firstResult )
    {
        return findTransferenciaArmazemEntities( false, maxResults, firstResult );
    }

    private List<TransferenciaArmazem> findTransferenciaArmazemEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TransferenciaArmazem.class ) );
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

    public TransferenciaArmazem findTransferenciaArmazem( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TransferenciaArmazem.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTransferenciaArmazemCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransferenciaArmazem> rt = cq.from( TransferenciaArmazem.class );
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
