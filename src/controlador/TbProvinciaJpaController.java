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
import entity.TbPais;
import entity.TbMunicipio;
import entity.TbProvincia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbProvinciaJpaController implements Serializable
{

    public TbProvinciaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbProvincia tbProvincia )
    {
        if ( tbProvincia.getTbMunicipioList() == null )
        {
            tbProvincia.setTbMunicipioList( new ArrayList<TbMunicipio>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbPais idPais = tbProvincia.getIdPais();
            if ( idPais != null )
            {
                idPais = em.getReference( idPais.getClass(), idPais.getIdPais() );
                tbProvincia.setIdPais( idPais );
            }
            List<TbMunicipio> attachedTbMunicipioList = new ArrayList<TbMunicipio>();
            for ( TbMunicipio tbMunicipioListTbMunicipioToAttach : tbProvincia.getTbMunicipioList() )
            {
                tbMunicipioListTbMunicipioToAttach = em.getReference( tbMunicipioListTbMunicipioToAttach.getClass(), tbMunicipioListTbMunicipioToAttach.getIdMunicipio() );
                attachedTbMunicipioList.add( tbMunicipioListTbMunicipioToAttach );
            }
            tbProvincia.setTbMunicipioList( attachedTbMunicipioList );
            em.persist( tbProvincia );
            if ( idPais != null )
            {
                idPais.getTbProvinciaList().add( tbProvincia );
                idPais = em.merge( idPais );
            }
            for ( TbMunicipio tbMunicipioListTbMunicipio : tbProvincia.getTbMunicipioList() )
            {
                TbProvincia oldIdProvinciaOfTbMunicipioListTbMunicipio = tbMunicipioListTbMunicipio.getIdProvincia();
                tbMunicipioListTbMunicipio.setIdProvincia( tbProvincia );
                tbMunicipioListTbMunicipio = em.merge( tbMunicipioListTbMunicipio );
                if ( oldIdProvinciaOfTbMunicipioListTbMunicipio != null )
                {
                    oldIdProvinciaOfTbMunicipioListTbMunicipio.getTbMunicipioList().remove( tbMunicipioListTbMunicipio );
                    oldIdProvinciaOfTbMunicipioListTbMunicipio = em.merge( oldIdProvinciaOfTbMunicipioListTbMunicipio );
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

    public void edit( TbProvincia tbProvincia ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProvincia persistentTbProvincia = em.find( TbProvincia.class, tbProvincia.getIdProvincia() );
            TbPais idPaisOld = persistentTbProvincia.getIdPais();
            TbPais idPaisNew = tbProvincia.getIdPais();
            List<TbMunicipio> tbMunicipioListOld = persistentTbProvincia.getTbMunicipioList();
            List<TbMunicipio> tbMunicipioListNew = tbProvincia.getTbMunicipioList();
            if ( idPaisNew != null )
            {
                idPaisNew = em.getReference( idPaisNew.getClass(), idPaisNew.getIdPais() );
                tbProvincia.setIdPais( idPaisNew );
            }
            List<TbMunicipio> attachedTbMunicipioListNew = new ArrayList<TbMunicipio>();
            for ( TbMunicipio tbMunicipioListNewTbMunicipioToAttach : tbMunicipioListNew )
            {
                tbMunicipioListNewTbMunicipioToAttach = em.getReference( tbMunicipioListNewTbMunicipioToAttach.getClass(), tbMunicipioListNewTbMunicipioToAttach.getIdMunicipio() );
                attachedTbMunicipioListNew.add( tbMunicipioListNewTbMunicipioToAttach );
            }
            tbMunicipioListNew = attachedTbMunicipioListNew;
            tbProvincia.setTbMunicipioList( tbMunicipioListNew );
            tbProvincia = em.merge( tbProvincia );
            if ( idPaisOld != null && !idPaisOld.equals( idPaisNew ) )
            {
                idPaisOld.getTbProvinciaList().remove( tbProvincia );
                idPaisOld = em.merge( idPaisOld );
            }
            if ( idPaisNew != null && !idPaisNew.equals( idPaisOld ) )
            {
                idPaisNew.getTbProvinciaList().add( tbProvincia );
                idPaisNew = em.merge( idPaisNew );
            }
            for ( TbMunicipio tbMunicipioListOldTbMunicipio : tbMunicipioListOld )
            {
                if ( !tbMunicipioListNew.contains( tbMunicipioListOldTbMunicipio ) )
                {
                    tbMunicipioListOldTbMunicipio.setIdProvincia( null );
                    tbMunicipioListOldTbMunicipio = em.merge( tbMunicipioListOldTbMunicipio );
                }
            }
            for ( TbMunicipio tbMunicipioListNewTbMunicipio : tbMunicipioListNew )
            {
                if ( !tbMunicipioListOld.contains( tbMunicipioListNewTbMunicipio ) )
                {
                    TbProvincia oldIdProvinciaOfTbMunicipioListNewTbMunicipio = tbMunicipioListNewTbMunicipio.getIdProvincia();
                    tbMunicipioListNewTbMunicipio.setIdProvincia( tbProvincia );
                    tbMunicipioListNewTbMunicipio = em.merge( tbMunicipioListNewTbMunicipio );
                    if ( oldIdProvinciaOfTbMunicipioListNewTbMunicipio != null && !oldIdProvinciaOfTbMunicipioListNewTbMunicipio.equals( tbProvincia ) )
                    {
                        oldIdProvinciaOfTbMunicipioListNewTbMunicipio.getTbMunicipioList().remove( tbMunicipioListNewTbMunicipio );
                        oldIdProvinciaOfTbMunicipioListNewTbMunicipio = em.merge( oldIdProvinciaOfTbMunicipioListNewTbMunicipio );
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
                Integer id = tbProvincia.getIdProvincia();
                if ( findTbProvincia( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbProvincia with id " + id + " no longer exists." );
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
            TbProvincia tbProvincia;
            try
            {
                tbProvincia = em.getReference( TbProvincia.class, id );
                tbProvincia.getIdProvincia();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbProvincia with id " + id + " no longer exists.", enfe );
            }
            TbPais idPais = tbProvincia.getIdPais();
            if ( idPais != null )
            {
                idPais.getTbProvinciaList().remove( tbProvincia );
                idPais = em.merge( idPais );
            }
            List<TbMunicipio> tbMunicipioList = tbProvincia.getTbMunicipioList();
            for ( TbMunicipio tbMunicipioListTbMunicipio : tbMunicipioList )
            {
                tbMunicipioListTbMunicipio.setIdProvincia( null );
                tbMunicipioListTbMunicipio = em.merge( tbMunicipioListTbMunicipio );
            }
            em.remove( tbProvincia );
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

    public List<TbProvincia> findTbProvinciaEntities()
    {
        return findTbProvinciaEntities( true, -1, -1 );
    }

    public List<TbProvincia> findTbProvinciaEntities( int maxResults, int firstResult )
    {
        return findTbProvinciaEntities( false, maxResults, firstResult );
    }

    private List<TbProvincia> findTbProvinciaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbProvincia.class ) );
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

    public TbProvincia findTbProvincia( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbProvincia.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbProvinciaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbProvincia> rt = cq.from( TbProvincia.class );
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
