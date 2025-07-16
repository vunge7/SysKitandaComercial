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
import entity.TbBanco;
import entity.TbUsuario;
import entity.TransferenciaBancaria;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TransferenciaBancariaJpaController implements Serializable
{

    public TransferenciaBancariaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TransferenciaBancaria transferenciaBancaria )
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco fkBancoDestino = transferenciaBancaria.getFkBancoDestino();
            if ( fkBancoDestino != null )
            {
                fkBancoDestino = em.getReference( fkBancoDestino.getClass(), fkBancoDestino.getIdBanco() );
                transferenciaBancaria.setFkBancoDestino( fkBancoDestino );
            }
            TbBanco fkBancoOrigem = transferenciaBancaria.getFkBancoOrigem();
            if ( fkBancoOrigem != null )
            {
                fkBancoOrigem = em.getReference( fkBancoOrigem.getClass(), fkBancoOrigem.getIdBanco() );
                transferenciaBancaria.setFkBancoOrigem( fkBancoOrigem );
            }
            TbUsuario fkUsuario = transferenciaBancaria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                transferenciaBancaria.setFkUsuario( fkUsuario );
            }
            em.persist( transferenciaBancaria );
            if ( fkBancoDestino != null )
            {
                fkBancoDestino.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkBancoDestino = em.merge( fkBancoDestino );
            }
            if ( fkBancoOrigem != null )
            {
                fkBancoOrigem.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkBancoOrigem = em.merge( fkBancoOrigem );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkUsuario = em.merge( fkUsuario );
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

    public void edit( TransferenciaBancaria transferenciaBancaria ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TransferenciaBancaria persistentTransferenciaBancaria = em.find( TransferenciaBancaria.class, transferenciaBancaria.getPkTransferencia() );
            TbBanco fkBancoDestinoOld = persistentTransferenciaBancaria.getFkBancoDestino();
            TbBanco fkBancoDestinoNew = transferenciaBancaria.getFkBancoDestino();
            TbBanco fkBancoOrigemOld = persistentTransferenciaBancaria.getFkBancoOrigem();
            TbBanco fkBancoOrigemNew = transferenciaBancaria.getFkBancoOrigem();
            TbUsuario fkUsuarioOld = persistentTransferenciaBancaria.getFkUsuario();
            TbUsuario fkUsuarioNew = transferenciaBancaria.getFkUsuario();
            if ( fkBancoDestinoNew != null )
            {
                fkBancoDestinoNew = em.getReference( fkBancoDestinoNew.getClass(), fkBancoDestinoNew.getIdBanco() );
                transferenciaBancaria.setFkBancoDestino( fkBancoDestinoNew );
            }
            if ( fkBancoOrigemNew != null )
            {
                fkBancoOrigemNew = em.getReference( fkBancoOrigemNew.getClass(), fkBancoOrigemNew.getIdBanco() );
                transferenciaBancaria.setFkBancoOrigem( fkBancoOrigemNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                transferenciaBancaria.setFkUsuario( fkUsuarioNew );
            }
            transferenciaBancaria = em.merge( transferenciaBancaria );
            if ( fkBancoDestinoOld != null && !fkBancoDestinoOld.equals( fkBancoDestinoNew ) )
            {
                fkBancoDestinoOld.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkBancoDestinoOld = em.merge( fkBancoDestinoOld );
            }
            if ( fkBancoDestinoNew != null && !fkBancoDestinoNew.equals( fkBancoDestinoOld ) )
            {
                fkBancoDestinoNew.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkBancoDestinoNew = em.merge( fkBancoDestinoNew );
            }
            if ( fkBancoOrigemOld != null && !fkBancoOrigemOld.equals( fkBancoOrigemNew ) )
            {
                fkBancoOrigemOld.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkBancoOrigemOld = em.merge( fkBancoOrigemOld );
            }
            if ( fkBancoOrigemNew != null && !fkBancoOrigemNew.equals( fkBancoOrigemOld ) )
            {
                fkBancoOrigemNew.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkBancoOrigemNew = em.merge( fkBancoOrigemNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTransferenciaBancariaList().add( transferenciaBancaria );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = transferenciaBancaria.getPkTransferencia();
                if ( findTransferenciaBancaria( id ) == null )
                {
                    throw new NonexistentEntityException( "The transferenciaBancaria with id " + id + " no longer exists." );
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
            TransferenciaBancaria transferenciaBancaria;
            try
            {
                transferenciaBancaria = em.getReference( TransferenciaBancaria.class, id );
                transferenciaBancaria.getPkTransferencia();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The transferenciaBancaria with id " + id + " no longer exists.", enfe );
            }
            TbBanco fkBancoDestino = transferenciaBancaria.getFkBancoDestino();
            if ( fkBancoDestino != null )
            {
                fkBancoDestino.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkBancoDestino = em.merge( fkBancoDestino );
            }
            TbBanco fkBancoOrigem = transferenciaBancaria.getFkBancoOrigem();
            if ( fkBancoOrigem != null )
            {
                fkBancoOrigem.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkBancoOrigem = em.merge( fkBancoOrigem );
            }
            TbUsuario fkUsuario = transferenciaBancaria.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTransferenciaBancariaList().remove( transferenciaBancaria );
                fkUsuario = em.merge( fkUsuario );
            }
            em.remove( transferenciaBancaria );
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

    public List<TransferenciaBancaria> findTransferenciaBancariaEntities()
    {
        return findTransferenciaBancariaEntities( true, -1, -1 );
    }

    public List<TransferenciaBancaria> findTransferenciaBancariaEntities( int maxResults, int firstResult )
    {
        return findTransferenciaBancariaEntities( false, maxResults, firstResult );
    }

    private List<TransferenciaBancaria> findTransferenciaBancariaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TransferenciaBancaria.class ) );
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

    public TransferenciaBancaria findTransferenciaBancaria( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TransferenciaBancaria.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTransferenciaBancariaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransferenciaBancaria> rt = cq.from( TransferenciaBancaria.class );
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
