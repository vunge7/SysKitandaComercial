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
import entity.Familia;
import entity.TbProduto;
import entity.TbTipoProduto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbTipoProdutoJpaController implements Serializable
{

    public TbTipoProdutoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbTipoProduto tbTipoProduto )
    {
        if ( tbTipoProduto.getTbProdutoList() == null )
        {
            tbTipoProduto.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia fkFamilia = tbTipoProduto.getFkFamilia();
            if ( fkFamilia != null )
            {
                fkFamilia = em.getReference( fkFamilia.getClass(), fkFamilia.getPkFamilia() );
                tbTipoProduto.setFkFamilia( fkFamilia );
            }
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : tbTipoProduto.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            tbTipoProduto.setTbProdutoList( attachedTbProdutoList );
            em.persist( tbTipoProduto );
            if ( fkFamilia != null )
            {
                fkFamilia.getTbTipoProdutoList().add( tbTipoProduto );
                fkFamilia = em.merge( fkFamilia );
            }
            for ( TbProduto tbProdutoListTbProduto : tbTipoProduto.getTbProdutoList() )
            {
                TbTipoProduto oldCodTipoProdutoOfTbProdutoListTbProduto = tbProdutoListTbProduto.getCodTipoProduto();
                tbProdutoListTbProduto.setCodTipoProduto( tbTipoProduto );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldCodTipoProdutoOfTbProdutoListTbProduto != null )
                {
                    oldCodTipoProdutoOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldCodTipoProdutoOfTbProdutoListTbProduto = em.merge( oldCodTipoProdutoOfTbProdutoListTbProduto );
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

    public void edit( TbTipoProduto tbTipoProduto ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbTipoProduto persistentTbTipoProduto = em.find( TbTipoProduto.class, tbTipoProduto.getCodigo() );
            Familia fkFamiliaOld = persistentTbTipoProduto.getFkFamilia();
            Familia fkFamiliaNew = tbTipoProduto.getFkFamilia();
            List<TbProduto> tbProdutoListOld = persistentTbTipoProduto.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = tbTipoProduto.getTbProdutoList();
            if ( fkFamiliaNew != null )
            {
                fkFamiliaNew = em.getReference( fkFamiliaNew.getClass(), fkFamiliaNew.getPkFamilia() );
                tbTipoProduto.setFkFamilia( fkFamiliaNew );
            }
            List<TbProduto> attachedTbProdutoListNew = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListNewTbProdutoToAttach : tbProdutoListNew )
            {
                tbProdutoListNewTbProdutoToAttach = em.getReference( tbProdutoListNewTbProdutoToAttach.getClass(), tbProdutoListNewTbProdutoToAttach.getCodigo() );
                attachedTbProdutoListNew.add( tbProdutoListNewTbProdutoToAttach );
            }
            tbProdutoListNew = attachedTbProdutoListNew;
            tbTipoProduto.setTbProdutoList( tbProdutoListNew );
            tbTipoProduto = em.merge( tbTipoProduto );
            if ( fkFamiliaOld != null && !fkFamiliaOld.equals( fkFamiliaNew ) )
            {
                fkFamiliaOld.getTbTipoProdutoList().remove( tbTipoProduto );
                fkFamiliaOld = em.merge( fkFamiliaOld );
            }
            if ( fkFamiliaNew != null && !fkFamiliaNew.equals( fkFamiliaOld ) )
            {
                fkFamiliaNew.getTbTipoProdutoList().add( tbTipoProduto );
                fkFamiliaNew = em.merge( fkFamiliaNew );
            }
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    tbProdutoListOldTbProduto.setCodTipoProduto( null );
                    tbProdutoListOldTbProduto = em.merge( tbProdutoListOldTbProduto );
                }
            }
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    TbTipoProduto oldCodTipoProdutoOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getCodTipoProduto();
                    tbProdutoListNewTbProduto.setCodTipoProduto( tbTipoProduto );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldCodTipoProdutoOfTbProdutoListNewTbProduto != null && !oldCodTipoProdutoOfTbProdutoListNewTbProduto.equals( tbTipoProduto ) )
                    {
                        oldCodTipoProdutoOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldCodTipoProdutoOfTbProdutoListNewTbProduto = em.merge( oldCodTipoProdutoOfTbProdutoListNewTbProduto );
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
                Integer id = tbTipoProduto.getCodigo();
                if ( findTbTipoProduto( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbTipoProduto with id " + id + " no longer exists." );
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
            TbTipoProduto tbTipoProduto;
            try
            {
                tbTipoProduto = em.getReference( TbTipoProduto.class, id );
                tbTipoProduto.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbTipoProduto with id " + id + " no longer exists.", enfe );
            }
            Familia fkFamilia = tbTipoProduto.getFkFamilia();
            if ( fkFamilia != null )
            {
                fkFamilia.getTbTipoProdutoList().remove( tbTipoProduto );
                fkFamilia = em.merge( fkFamilia );
            }
            List<TbProduto> tbProdutoList = tbTipoProduto.getTbProdutoList();
            for ( TbProduto tbProdutoListTbProduto : tbProdutoList )
            {
                tbProdutoListTbProduto.setCodTipoProduto( null );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
            }
            em.remove( tbTipoProduto );
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

    public List<TbTipoProduto> findTbTipoProdutoEntities()
    {
        return findTbTipoProdutoEntities( true, -1, -1 );
    }

    public List<TbTipoProduto> findTbTipoProdutoEntities( int maxResults, int firstResult )
    {
        return findTbTipoProdutoEntities( false, maxResults, firstResult );
    }

    private List<TbTipoProduto> findTbTipoProdutoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbTipoProduto.class ) );
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

    public TbTipoProduto findTbTipoProduto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbTipoProduto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbTipoProdutoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbTipoProduto> rt = cq.from( TbTipoProduto.class );
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
