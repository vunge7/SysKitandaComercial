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
import entity.MasterTable;
import entity.ItemSalarioExtra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class MasterTableJpaController implements Serializable
{

    public MasterTableJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( MasterTable masterTable )
    {
        if ( masterTable.getItemSalarioExtraList() == null )
        {
            masterTable.setItemSalarioExtraList( new ArrayList<ItemSalarioExtra>() );
        }
        if ( masterTable.getMasterTableList() == null )
        {
            masterTable.setMasterTableList( new ArrayList<MasterTable>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            MasterTable fkMasterTable = masterTable.getFkMasterTable();
            if ( fkMasterTable != null )
            {
                fkMasterTable = em.getReference( fkMasterTable.getClass(), fkMasterTable.getPkMasterTable() );
                masterTable.setFkMasterTable( fkMasterTable );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraList = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtraToAttach : masterTable.getItemSalarioExtraList() )
            {
                itemSalarioExtraListItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListItemSalarioExtraToAttach.getClass(), itemSalarioExtraListItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraList.add( itemSalarioExtraListItemSalarioExtraToAttach );
            }
            masterTable.setItemSalarioExtraList( attachedItemSalarioExtraList );
            List<MasterTable> attachedMasterTableList = new ArrayList<MasterTable>();
            for ( MasterTable masterTableListMasterTableToAttach : masterTable.getMasterTableList() )
            {
                masterTableListMasterTableToAttach = em.getReference( masterTableListMasterTableToAttach.getClass(), masterTableListMasterTableToAttach.getPkMasterTable() );
                attachedMasterTableList.add( masterTableListMasterTableToAttach );
            }
            masterTable.setMasterTableList( attachedMasterTableList );
            em.persist( masterTable );
            if ( fkMasterTable != null )
            {
                fkMasterTable.getMasterTableList().add( masterTable );
                fkMasterTable = em.merge( fkMasterTable );
            }
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtra : masterTable.getItemSalarioExtraList() )
            {
                MasterTable oldFkMasterTableOfItemSalarioExtraListItemSalarioExtra = itemSalarioExtraListItemSalarioExtra.getFkMasterTable();
                itemSalarioExtraListItemSalarioExtra.setFkMasterTable( masterTable );
                itemSalarioExtraListItemSalarioExtra = em.merge( itemSalarioExtraListItemSalarioExtra );
                if ( oldFkMasterTableOfItemSalarioExtraListItemSalarioExtra != null )
                {
                    oldFkMasterTableOfItemSalarioExtraListItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListItemSalarioExtra );
                    oldFkMasterTableOfItemSalarioExtraListItemSalarioExtra = em.merge( oldFkMasterTableOfItemSalarioExtraListItemSalarioExtra );
                }
            }
            for ( MasterTable masterTableListMasterTable : masterTable.getMasterTableList() )
            {
                MasterTable oldFkMasterTableOfMasterTableListMasterTable = masterTableListMasterTable.getFkMasterTable();
                masterTableListMasterTable.setFkMasterTable( masterTable );
                masterTableListMasterTable = em.merge( masterTableListMasterTable );
                if ( oldFkMasterTableOfMasterTableListMasterTable != null )
                {
                    oldFkMasterTableOfMasterTableListMasterTable.getMasterTableList().remove( masterTableListMasterTable );
                    oldFkMasterTableOfMasterTableListMasterTable = em.merge( oldFkMasterTableOfMasterTableListMasterTable );
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

    public void edit( MasterTable masterTable ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            MasterTable persistentMasterTable = em.find( MasterTable.class, masterTable.getPkMasterTable() );
            MasterTable fkMasterTableOld = persistentMasterTable.getFkMasterTable();
            MasterTable fkMasterTableNew = masterTable.getFkMasterTable();
            List<ItemSalarioExtra> itemSalarioExtraListOld = persistentMasterTable.getItemSalarioExtraList();
            List<ItemSalarioExtra> itemSalarioExtraListNew = masterTable.getItemSalarioExtraList();
            List<MasterTable> masterTableListOld = persistentMasterTable.getMasterTableList();
            List<MasterTable> masterTableListNew = masterTable.getMasterTableList();
            List<String> illegalOrphanMessages = null;
            for ( ItemSalarioExtra itemSalarioExtraListOldItemSalarioExtra : itemSalarioExtraListOld )
            {
                if ( !itemSalarioExtraListNew.contains( itemSalarioExtraListOldItemSalarioExtra ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemSalarioExtra " + itemSalarioExtraListOldItemSalarioExtra + " since its fkMasterTable field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkMasterTableNew != null )
            {
                fkMasterTableNew = em.getReference( fkMasterTableNew.getClass(), fkMasterTableNew.getPkMasterTable() );
                masterTable.setFkMasterTable( fkMasterTableNew );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraListNew = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtraToAttach : itemSalarioExtraListNew )
            {
                itemSalarioExtraListNewItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListNewItemSalarioExtraToAttach.getClass(), itemSalarioExtraListNewItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraListNew.add( itemSalarioExtraListNewItemSalarioExtraToAttach );
            }
            itemSalarioExtraListNew = attachedItemSalarioExtraListNew;
            masterTable.setItemSalarioExtraList( itemSalarioExtraListNew );
            List<MasterTable> attachedMasterTableListNew = new ArrayList<MasterTable>();
            for ( MasterTable masterTableListNewMasterTableToAttach : masterTableListNew )
            {
                masterTableListNewMasterTableToAttach = em.getReference( masterTableListNewMasterTableToAttach.getClass(), masterTableListNewMasterTableToAttach.getPkMasterTable() );
                attachedMasterTableListNew.add( masterTableListNewMasterTableToAttach );
            }
            masterTableListNew = attachedMasterTableListNew;
            masterTable.setMasterTableList( masterTableListNew );
            masterTable = em.merge( masterTable );
            if ( fkMasterTableOld != null && !fkMasterTableOld.equals( fkMasterTableNew ) )
            {
                fkMasterTableOld.getMasterTableList().remove( masterTable );
                fkMasterTableOld = em.merge( fkMasterTableOld );
            }
            if ( fkMasterTableNew != null && !fkMasterTableNew.equals( fkMasterTableOld ) )
            {
                fkMasterTableNew.getMasterTableList().add( masterTable );
                fkMasterTableNew = em.merge( fkMasterTableNew );
            }
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtra : itemSalarioExtraListNew )
            {
                if ( !itemSalarioExtraListOld.contains( itemSalarioExtraListNewItemSalarioExtra ) )
                {
                    MasterTable oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra = itemSalarioExtraListNewItemSalarioExtra.getFkMasterTable();
                    itemSalarioExtraListNewItemSalarioExtra.setFkMasterTable( masterTable );
                    itemSalarioExtraListNewItemSalarioExtra = em.merge( itemSalarioExtraListNewItemSalarioExtra );
                    if ( oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra != null && !oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra.equals( masterTable ) )
                    {
                        oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListNewItemSalarioExtra );
                        oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra = em.merge( oldFkMasterTableOfItemSalarioExtraListNewItemSalarioExtra );
                    }
                }
            }
            for ( MasterTable masterTableListOldMasterTable : masterTableListOld )
            {
                if ( !masterTableListNew.contains( masterTableListOldMasterTable ) )
                {
                    masterTableListOldMasterTable.setFkMasterTable( null );
                    masterTableListOldMasterTable = em.merge( masterTableListOldMasterTable );
                }
            }
            for ( MasterTable masterTableListNewMasterTable : masterTableListNew )
            {
                if ( !masterTableListOld.contains( masterTableListNewMasterTable ) )
                {
                    MasterTable oldFkMasterTableOfMasterTableListNewMasterTable = masterTableListNewMasterTable.getFkMasterTable();
                    masterTableListNewMasterTable.setFkMasterTable( masterTable );
                    masterTableListNewMasterTable = em.merge( masterTableListNewMasterTable );
                    if ( oldFkMasterTableOfMasterTableListNewMasterTable != null && !oldFkMasterTableOfMasterTableListNewMasterTable.equals( masterTable ) )
                    {
                        oldFkMasterTableOfMasterTableListNewMasterTable.getMasterTableList().remove( masterTableListNewMasterTable );
                        oldFkMasterTableOfMasterTableListNewMasterTable = em.merge( oldFkMasterTableOfMasterTableListNewMasterTable );
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
                Integer id = masterTable.getPkMasterTable();
                if ( findMasterTable( id ) == null )
                {
                    throw new NonexistentEntityException( "The masterTable with id " + id + " no longer exists." );
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
            MasterTable masterTable;
            try
            {
                masterTable = em.getReference( MasterTable.class, id );
                masterTable.getPkMasterTable();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The masterTable with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemSalarioExtra> itemSalarioExtraListOrphanCheck = masterTable.getItemSalarioExtraList();
            for ( ItemSalarioExtra itemSalarioExtraListOrphanCheckItemSalarioExtra : itemSalarioExtraListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This MasterTable (" + masterTable + ") cannot be destroyed since the ItemSalarioExtra " + itemSalarioExtraListOrphanCheckItemSalarioExtra + " in its itemSalarioExtraList field has a non-nullable fkMasterTable field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            MasterTable fkMasterTable = masterTable.getFkMasterTable();
            if ( fkMasterTable != null )
            {
                fkMasterTable.getMasterTableList().remove( masterTable );
                fkMasterTable = em.merge( fkMasterTable );
            }
            List<MasterTable> masterTableList = masterTable.getMasterTableList();
            for ( MasterTable masterTableListMasterTable : masterTableList )
            {
                masterTableListMasterTable.setFkMasterTable( null );
                masterTableListMasterTable = em.merge( masterTableListMasterTable );
            }
            em.remove( masterTable );
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

    public List<MasterTable> findMasterTableEntities()
    {
        return findMasterTableEntities( true, -1, -1 );
    }

    public List<MasterTable> findMasterTableEntities( int maxResults, int firstResult )
    {
        return findMasterTableEntities( false, maxResults, firstResult );
    }

    private List<MasterTable> findMasterTableEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( MasterTable.class ) );
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

    public MasterTable findMasterTable( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( MasterTable.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getMasterTableCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MasterTable> rt = cq.from( MasterTable.class );
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
