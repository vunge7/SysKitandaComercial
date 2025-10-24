/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.NonexistentEntityException;
import entity.Grupo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbProduto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class GrupoJpaController1 implements Serializable
{

    public GrupoJpaController1( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Grupo grupo )
    {
        if ( grupo.getTbProdutoList() == null )
        {
            grupo.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : grupo.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            grupo.setTbProdutoList( attachedTbProdutoList );
            em.persist( grupo );
            for ( TbProduto tbProdutoListTbProduto : grupo.getTbProdutoList() )
            {
                Grupo oldFkGrupoOfTbProdutoListTbProduto = tbProdutoListTbProduto.getFkGrupo();
                tbProdutoListTbProduto.setFkGrupo( grupo );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldFkGrupoOfTbProdutoListTbProduto != null )
                {
                    oldFkGrupoOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldFkGrupoOfTbProdutoListTbProduto = em.merge( oldFkGrupoOfTbProdutoListTbProduto );
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

    public void edit( Grupo grupo ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find( Grupo.class, grupo.getPkGrupo() );
            List<TbProduto> tbProdutoListOld = persistentGrupo.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = grupo.getTbProdutoList();
            List<TbProduto> attachedTbProdutoListNew = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListNewTbProdutoToAttach : tbProdutoListNew )
            {
                tbProdutoListNewTbProdutoToAttach = em.getReference( tbProdutoListNewTbProdutoToAttach.getClass(), tbProdutoListNewTbProdutoToAttach.getCodigo() );
                attachedTbProdutoListNew.add( tbProdutoListNewTbProdutoToAttach );
            }
            tbProdutoListNew = attachedTbProdutoListNew;
            grupo.setTbProdutoList( tbProdutoListNew );
            grupo = em.merge( grupo );
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    tbProdutoListOldTbProduto.setFkGrupo( null );
                    tbProdutoListOldTbProduto = em.merge( tbProdutoListOldTbProduto );
                }
            }
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    Grupo oldFkGrupoOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getFkGrupo();
                    tbProdutoListNewTbProduto.setFkGrupo( grupo );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldFkGrupoOfTbProdutoListNewTbProduto != null && !oldFkGrupoOfTbProdutoListNewTbProduto.equals( grupo ) )
                    {
                        oldFkGrupoOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldFkGrupoOfTbProdutoListNewTbProduto = em.merge( oldFkGrupoOfTbProdutoListNewTbProduto );
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
                Integer id = grupo.getPkGrupo();
                if ( findGrupo( id ) == null )
                {
                    throw new NonexistentEntityException( "The grupo with id " + id + " no longer exists." );
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
            Grupo grupo;
            try
            {
                grupo = em.getReference( Grupo.class, id );
                grupo.getPkGrupo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The grupo with id " + id + " no longer exists.", enfe );
            }
            List<TbProduto> tbProdutoList = grupo.getTbProdutoList();
            for ( TbProduto tbProdutoListTbProduto : tbProdutoList )
            {
                tbProdutoListTbProduto.setFkGrupo( null );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
            }
            em.remove( grupo );
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

    public List<Grupo> findGrupoEntities()
    {
        return findGrupoEntities( true, -1, -1 );
    }

    public List<Grupo> findGrupoEntities( int maxResults, int firstResult )
    {
        return findGrupoEntities( false, maxResults, firstResult );
    }

    private List<Grupo> findGrupoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Grupo.class ) );
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

    public Grupo findGrupo( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Grupo.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getGrupoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from( Grupo.class );
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
