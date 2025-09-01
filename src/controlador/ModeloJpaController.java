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
import entity.Marca;
import entity.Modelo;
import entity.TbProduto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class ModeloJpaController implements Serializable
{

    public ModeloJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( Modelo modelo )
    {
        if ( modelo.getTbProdutoList() == null )
        {
            modelo.setTbProdutoList( new ArrayList<TbProduto>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca fkMarca = modelo.getFkMarca();
            if ( fkMarca != null )
            {
                fkMarca = em.getReference( fkMarca.getClass(), fkMarca.getPkMarca() );
                modelo.setFkMarca( fkMarca );
            }
            List<TbProduto> attachedTbProdutoList = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListTbProdutoToAttach : modelo.getTbProdutoList() )
            {
                tbProdutoListTbProdutoToAttach = em.getReference( tbProdutoListTbProdutoToAttach.getClass(), tbProdutoListTbProdutoToAttach.getCodigo() );
                attachedTbProdutoList.add( tbProdutoListTbProdutoToAttach );
            }
            modelo.setTbProdutoList( attachedTbProdutoList );
            em.persist( modelo );
            if ( fkMarca != null )
            {
                fkMarca.getModeloList().add( modelo );
                fkMarca = em.merge( fkMarca );
            }
            for ( TbProduto tbProdutoListTbProduto : modelo.getTbProdutoList() )
            {
                Modelo oldFkModeloOfTbProdutoListTbProduto = tbProdutoListTbProduto.getFkModelo();
                tbProdutoListTbProduto.setFkModelo( modelo );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
                if ( oldFkModeloOfTbProdutoListTbProduto != null )
                {
                    oldFkModeloOfTbProdutoListTbProduto.getTbProdutoList().remove( tbProdutoListTbProduto );
                    oldFkModeloOfTbProdutoListTbProduto = em.merge( oldFkModeloOfTbProdutoListTbProduto );
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

    public void edit( Modelo modelo ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Modelo persistentModelo = em.find( Modelo.class, modelo.getPkModelo() );
            Marca fkMarcaOld = persistentModelo.getFkMarca();
            Marca fkMarcaNew = modelo.getFkMarca();
            List<TbProduto> tbProdutoListOld = persistentModelo.getTbProdutoList();
            List<TbProduto> tbProdutoListNew = modelo.getTbProdutoList();
            if ( fkMarcaNew != null )
            {
                fkMarcaNew = em.getReference( fkMarcaNew.getClass(), fkMarcaNew.getPkMarca() );
                modelo.setFkMarca( fkMarcaNew );
            }
            List<TbProduto> attachedTbProdutoListNew = new ArrayList<TbProduto>();
            for ( TbProduto tbProdutoListNewTbProdutoToAttach : tbProdutoListNew )
            {
                tbProdutoListNewTbProdutoToAttach = em.getReference( tbProdutoListNewTbProdutoToAttach.getClass(), tbProdutoListNewTbProdutoToAttach.getCodigo() );
                attachedTbProdutoListNew.add( tbProdutoListNewTbProdutoToAttach );
            }
            tbProdutoListNew = attachedTbProdutoListNew;
            modelo.setTbProdutoList( tbProdutoListNew );
            modelo = em.merge( modelo );
            if ( fkMarcaOld != null && !fkMarcaOld.equals( fkMarcaNew ) )
            {
                fkMarcaOld.getModeloList().remove( modelo );
                fkMarcaOld = em.merge( fkMarcaOld );
            }
            if ( fkMarcaNew != null && !fkMarcaNew.equals( fkMarcaOld ) )
            {
                fkMarcaNew.getModeloList().add( modelo );
                fkMarcaNew = em.merge( fkMarcaNew );
            }
            for ( TbProduto tbProdutoListOldTbProduto : tbProdutoListOld )
            {
                if ( !tbProdutoListNew.contains( tbProdutoListOldTbProduto ) )
                {
                    tbProdutoListOldTbProduto.setFkModelo( null );
                    tbProdutoListOldTbProduto = em.merge( tbProdutoListOldTbProduto );
                }
            }
            for ( TbProduto tbProdutoListNewTbProduto : tbProdutoListNew )
            {
                if ( !tbProdutoListOld.contains( tbProdutoListNewTbProduto ) )
                {
                    Modelo oldFkModeloOfTbProdutoListNewTbProduto = tbProdutoListNewTbProduto.getFkModelo();
                    tbProdutoListNewTbProduto.setFkModelo( modelo );
                    tbProdutoListNewTbProduto = em.merge( tbProdutoListNewTbProduto );
                    if ( oldFkModeloOfTbProdutoListNewTbProduto != null && !oldFkModeloOfTbProdutoListNewTbProduto.equals( modelo ) )
                    {
                        oldFkModeloOfTbProdutoListNewTbProduto.getTbProdutoList().remove( tbProdutoListNewTbProduto );
                        oldFkModeloOfTbProdutoListNewTbProduto = em.merge( oldFkModeloOfTbProdutoListNewTbProduto );
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
                Integer id = modelo.getPkModelo();
                if ( findModelo( id ) == null )
                {
                    throw new NonexistentEntityException( "The modelo with id " + id + " no longer exists." );
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
            Modelo modelo;
            try
            {
                modelo = em.getReference( Modelo.class, id );
                modelo.getPkModelo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The modelo with id " + id + " no longer exists.", enfe );
            }
            Marca fkMarca = modelo.getFkMarca();
            if ( fkMarca != null )
            {
                fkMarca.getModeloList().remove( modelo );
                fkMarca = em.merge( fkMarca );
            }
            List<TbProduto> tbProdutoList = modelo.getTbProdutoList();
            for ( TbProduto tbProdutoListTbProduto : tbProdutoList )
            {
                tbProdutoListTbProduto.setFkModelo( null );
                tbProdutoListTbProduto = em.merge( tbProdutoListTbProduto );
            }
            em.remove( modelo );
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

    public List<Modelo> findModeloEntities()
    {
        return findModeloEntities( true, -1, -1 );
    }

    public List<Modelo> findModeloEntities( int maxResults, int firstResult )
    {
        return findModeloEntities( false, maxResults, firstResult );
    }

    private List<Modelo> findModeloEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( Modelo.class ) );
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

    public Modelo findModelo( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( Modelo.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getModeloCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modelo> rt = cq.from( Modelo.class );
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
