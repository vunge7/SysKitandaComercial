/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import entity.ItemSalarioExtra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.MasterTable;
import entity.TbFuncionario;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class ItemSalarioExtraJpaController implements Serializable
{

    public ItemSalarioExtraJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( ItemSalarioExtra itemSalarioExtra )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            MasterTable fkMasterTable = itemSalarioExtra.getFkMasterTable();
            if ( fkMasterTable != null )
            {
                fkMasterTable = em.getReference( fkMasterTable.getClass(), fkMasterTable.getPkMasterTable() );
                itemSalarioExtra.setFkMasterTable( fkMasterTable );
            }
            TbFuncionario fkTbFuncionario = itemSalarioExtra.getFkTbFuncionario();
            if ( fkTbFuncionario != null )
            {
                fkTbFuncionario = em.getReference( fkTbFuncionario.getClass(), fkTbFuncionario.getIdFuncionario() );
                itemSalarioExtra.setFkTbFuncionario( fkTbFuncionario );
            }
            TbUsuario fkTbUsuario = itemSalarioExtra.getFkTbUsuario();
            if ( fkTbUsuario != null )
            {
                fkTbUsuario = em.getReference( fkTbUsuario.getClass(), fkTbUsuario.getCodigo() );
                itemSalarioExtra.setFkTbUsuario( fkTbUsuario );
            }
            em.persist( itemSalarioExtra );
            if ( fkMasterTable != null )
            {
                fkMasterTable.getItemSalarioExtraList().add( itemSalarioExtra );
                fkMasterTable = em.merge( fkMasterTable );
            }
            if ( fkTbFuncionario != null )
            {
                fkTbFuncionario.getItemSalarioExtraList().add( itemSalarioExtra );
                fkTbFuncionario = em.merge( fkTbFuncionario );
            }
            if ( fkTbUsuario != null )
            {
                fkTbUsuario.getItemSalarioExtraList().add( itemSalarioExtra );
                fkTbUsuario = em.merge( fkTbUsuario );
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

    public void edit( ItemSalarioExtra itemSalarioExtra ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemSalarioExtra persistentItemSalarioExtra = em.find( ItemSalarioExtra.class, itemSalarioExtra.getPkItemSalarioExtra() );
            MasterTable fkMasterTableOld = persistentItemSalarioExtra.getFkMasterTable();
            MasterTable fkMasterTableNew = itemSalarioExtra.getFkMasterTable();
            TbFuncionario fkTbFuncionarioOld = persistentItemSalarioExtra.getFkTbFuncionario();
            TbFuncionario fkTbFuncionarioNew = itemSalarioExtra.getFkTbFuncionario();
            TbUsuario fkTbUsuarioOld = persistentItemSalarioExtra.getFkTbUsuario();
            TbUsuario fkTbUsuarioNew = itemSalarioExtra.getFkTbUsuario();
            if ( fkMasterTableNew != null )
            {
                fkMasterTableNew = em.getReference( fkMasterTableNew.getClass(), fkMasterTableNew.getPkMasterTable() );
                itemSalarioExtra.setFkMasterTable( fkMasterTableNew );
            }
            if ( fkTbFuncionarioNew != null )
            {
                fkTbFuncionarioNew = em.getReference( fkTbFuncionarioNew.getClass(), fkTbFuncionarioNew.getIdFuncionario() );
                itemSalarioExtra.setFkTbFuncionario( fkTbFuncionarioNew );
            }
            if ( fkTbUsuarioNew != null )
            {
                fkTbUsuarioNew = em.getReference( fkTbUsuarioNew.getClass(), fkTbUsuarioNew.getCodigo() );
                itemSalarioExtra.setFkTbUsuario( fkTbUsuarioNew );
            }
            itemSalarioExtra = em.merge( itemSalarioExtra );
            if ( fkMasterTableOld != null && !fkMasterTableOld.equals( fkMasterTableNew ) )
            {
                fkMasterTableOld.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkMasterTableOld = em.merge( fkMasterTableOld );
            }
            if ( fkMasterTableNew != null && !fkMasterTableNew.equals( fkMasterTableOld ) )
            {
                fkMasterTableNew.getItemSalarioExtraList().add( itemSalarioExtra );
                fkMasterTableNew = em.merge( fkMasterTableNew );
            }
            if ( fkTbFuncionarioOld != null && !fkTbFuncionarioOld.equals( fkTbFuncionarioNew ) )
            {
                fkTbFuncionarioOld.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkTbFuncionarioOld = em.merge( fkTbFuncionarioOld );
            }
            if ( fkTbFuncionarioNew != null && !fkTbFuncionarioNew.equals( fkTbFuncionarioOld ) )
            {
                fkTbFuncionarioNew.getItemSalarioExtraList().add( itemSalarioExtra );
                fkTbFuncionarioNew = em.merge( fkTbFuncionarioNew );
            }
            if ( fkTbUsuarioOld != null && !fkTbUsuarioOld.equals( fkTbUsuarioNew ) )
            {
                fkTbUsuarioOld.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkTbUsuarioOld = em.merge( fkTbUsuarioOld );
            }
            if ( fkTbUsuarioNew != null && !fkTbUsuarioNew.equals( fkTbUsuarioOld ) )
            {
                fkTbUsuarioNew.getItemSalarioExtraList().add( itemSalarioExtra );
                fkTbUsuarioNew = em.merge( fkTbUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = itemSalarioExtra.getPkItemSalarioExtra();
                if ( findItemSalarioExtra( id ) == null )
                {
                    throw new NonexistentEntityException( "The itemSalarioExtra with id " + id + " no longer exists." );
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
            ItemSalarioExtra itemSalarioExtra;
            try
            {
                itemSalarioExtra = em.getReference( ItemSalarioExtra.class, id );
                itemSalarioExtra.getPkItemSalarioExtra();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The itemSalarioExtra with id " + id + " no longer exists.", enfe );
            }
            MasterTable fkMasterTable = itemSalarioExtra.getFkMasterTable();
            if ( fkMasterTable != null )
            {
                fkMasterTable.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkMasterTable = em.merge( fkMasterTable );
            }
            TbFuncionario fkTbFuncionario = itemSalarioExtra.getFkTbFuncionario();
            if ( fkTbFuncionario != null )
            {
                fkTbFuncionario.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkTbFuncionario = em.merge( fkTbFuncionario );
            }
            TbUsuario fkTbUsuario = itemSalarioExtra.getFkTbUsuario();
            if ( fkTbUsuario != null )
            {
                fkTbUsuario.getItemSalarioExtraList().remove( itemSalarioExtra );
                fkTbUsuario = em.merge( fkTbUsuario );
            }
            em.remove( itemSalarioExtra );
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

    public List<ItemSalarioExtra> findItemSalarioExtraEntities()
    {
        return findItemSalarioExtraEntities( true, -1, -1 );
    }

    public List<ItemSalarioExtra> findItemSalarioExtraEntities( int maxResults, int firstResult )
    {
        return findItemSalarioExtraEntities( false, maxResults, firstResult );
    }

    private List<ItemSalarioExtra> findItemSalarioExtraEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( ItemSalarioExtra.class ) );
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

    public ItemSalarioExtra findItemSalarioExtra( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( ItemSalarioExtra.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getItemSalarioExtraCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemSalarioExtra> rt = cq.from( ItemSalarioExtra.class );
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
