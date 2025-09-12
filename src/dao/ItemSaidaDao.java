/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbItemSaidasJpaController;
import entity.TbItemSaidas;
import entity.TbProduto;
import entity.TbSaidasProdutos;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Toshiba
 */
public class ItemSaidaDao extends TbItemSaidasJpaController
{

    public ItemSaidaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbItemSaidas> getAllItemSaidasByIdSaidaReciclagem( int pkSaidasProdutos )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.pkSaidasProdutos = :pkSaidasProdutos" )
                .setParameter( "pkSaidasProdutos", pkSaidasProdutos );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbItemSaidas itemSaidas = new TbItemSaidas( 0 );
        result.add( itemSaidas );
        return result;
    }

    public List<TbItemSaidas> getAllItemVendasByIdVenda( int pkSaidasProdutos )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.pkSaidasProdutos = :pkSaidasProdutos AND v.fkSaidasProdutos.statusEliminado = 'false'" )
                .setParameter( "pkSaidasProdutos", pkSaidasProdutos );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbItemSaidas itemSaidas = new TbItemSaidas( 0 );
        result.add( itemSaidas );
        return result;
    }

    public List<TbItemSaidas> getAllVenda()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbItemSaidas  v WHERE  v.fkSaidasProdutos.statusEliminado = 'false' AND v.codigo > 32898" );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbItemSaidas itemSaidas = new TbItemSaidas( 0 );
        result.add( itemSaidas );
        return result;
    }

    public List<TbProduto> getAllProdutosSaidos( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  v.codigoProduto AS TOTAL FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.statusEliminado = 'false' AND v.fkSaidasProdutos.dataSaida BETWEEN :data_inicio AND :data_fim  AND  v.fkSaidasProdutos.idArmazemFK.codigo = :id_armazem  GROUP BY v.fkProdutos" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "id_armazem", id_armazem );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public long getQuantidadeByIdProduto( int codigo_produto, Date data_inicio, Date data_fim, int pk_armazem )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(v.quantidade) AS TOTAL FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.statusEliminado = 'false' AND  v.fkProdutos.codigo = :codigo  AND v.fkSaidasProdutos.idArmazemFK.codigo = :pk_armazem AND v.fkSaidasProdutos.dataSaida BETWEEN :data_inicio AND :data_fim   ORDER BY v.fkProdutos.codigo" )
                .setParameter( "codigo_produto", codigo_produto )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_armazem", pk_armazem );

        List<Long> result = query.getResultList();
        em.close();
        return result.get( 0 );

    }

    public List<TbItemSaidas> getAll_ItensSaidos( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM TbItemSaidas  i WHERE i.fkSaidasProdutos.pkSaidasProdutos BETWEEN :data_inicio AND :data_fim AND i.fkSaidasProdutos.statusEliminado = 'false' AND i.fkSaidasProdutos.idArmazemFK.codigo = :pk_armazem" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_armazem", pk_armazem );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbItemSaidas itemSaidas = new TbItemSaidas( 0 );
        result.add( itemSaidas );

        return result;
    }

    public List<TbItemSaidas> getAllItemSaidasByIdSaida( int pkSaidasProdutos )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.pkSaidasProdutos = :pkSaidasProdutos AND v.fkSaidasProdutos.statusEliminado = 'false'" )
                .setParameter( "pkSaidasProdutos", pkSaidasProdutos );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbSaidasProdutos> getAllVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i.fkSaidasProdutos FROM TbItemSaidas  i WHERE i.fkSaidasProdutos.dataSaida BETWEEN :data_inicio AND :data_fim AND i.fkSaidasProdutos.statusEliminado = 'false' GROUP BY i.fkSaidasProdutos.pkSaidasProdutos" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbSaidasProdutos> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos( 0 );
        result.add( saidasProdutos );

        return result;
    }

    public boolean exist_factura( int id_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbItemSaidas  v WHERE v.fkSaidasProdutos.pkSaidasProdutos = :id_venda AND v.fkSaidasProdutos.statusEliminado = 'false'" )
                .setParameter( "id_venda", id_venda );

        List<TbItemSaidas> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public void deleteAllItemSaidasByIdSaidaReciclagem( int pkSaidasProdutos )
    {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try
        {
            tx.begin();

            Query query = em.createQuery(
                    "DELETE FROM TbItemSaidas v WHERE v.fkSaidasProdutos.pkSaidasProdutos = :pkSaidasProdutos"
            );
            query.setParameter( "pkSaidasProdutos", pkSaidasProdutos );
            query.executeUpdate();

            tx.commit();
        }
        catch ( Exception e )
        {
            if ( tx.isActive() )
            {
                tx.rollback();
            }
            throw e;
        }
        finally
        {
            em.close();
        }
    }

    public static void main( String[] args )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;

        ItemSaidaDao itemVendaDao = new ItemSaidaDao( emf );
        VendaDao vendaDao = new VendaDao( emf );
        PrecoDao precoDao = new PrecoDao( emf );

//        List<TbItemSaidas> list = itemSaidaDao.getAllVenda();
//        
//        for (int i = 0; i < list.size(); i++) 
//        {
//            
//            TbItemSaidas itemSaidas = list.get(i);            
////            itemSaidas.setFkPreco(    precoDao.getLastPrecoByIdProduto(  itemVenda.getCodigoProduto().getCodigo()  )   );            
//            try {
//                itemVendaDao.edit(itemVenda);
//            } catch (Exception e) {
//            }
//           
//            
//            }
    }

}
