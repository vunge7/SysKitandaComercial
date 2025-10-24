/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbItemEstornoJpaController;
import controlador.TbItemVendaJpaController;
import entity.TbEstorno;
import entity.TbItemEstorno;
import entity.TbItemVenda;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbSaidasProdutos;
import entity.TbVenda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.RelatorioDiarioUtil;

/**
 *
 * @author Toshiba
 */
public class ItemEstornoDao extends TbItemEstornoJpaController
{

    public ItemEstornoDao(EntityManagerFactory emf) {
        super(emf);
    }

    
    
    
      public List<TbItemEstorno> getAllItemSaidasByIdSaidaReciclagem(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemEstorno  v WHERE v.fkEstorno.pkEstorno = :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemEstorno itemSaidas = new TbItemEstorno(0);
        result.add(itemSaidas);        
        return result;
    }
    
    
    public List<TbItemEstorno> getAllItemVendasByIdVenda(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemEstorno  v WHERE v.fkEstorno.pkEstorno = :pkEstorno AND v.fkEstorno = 'false'")
                .setParameter("pkEstorno", pkEstorno);
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemEstorno itemSaidas = new TbItemEstorno(0);
        result.add(itemSaidas);        
        return result;
    }
    
    
     public List<TbItemEstorno> getAllVenda()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemEstorno  v WHERE  v.fkEstorno.statusEliminado = 'false' AND v.codigo > 32898");
               
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemEstorno itemSaidas = new TbItemEstorno(0);
        result.add(itemSaidas);        
        return result;
    }
    
    
    
    
    public List<TbProduto> getAllProdutosSaidos(Date data_inicio, Date data_fim, int id_armazem)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  v.codigoProduto AS TOTAL FROM TbItemEstorno  v WHERE v.fkEstorno.statusEliminado = 'false' AND v.fkEstorno.dataEstorno BETWEEN :data_inicio AND :data_fim  AND  v.fkEstorno.idArmazemFK.codigo = :codigo  GROUP BY v.fkProdutos")              
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("id_armazem", id_armazem);
               
        List<TbProduto> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
    
    
    public long getQuantidadeByIdProduto(int codigo_produto , Date data_inicio, Date data_fim, int pk_armazem)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT SUM(v.quantidade) AS TOTAL FROM TbItemEstorno  v WHERE v.fkEstorno.statusEliminado = 'false' AND  v.fkProdutos.codigo = :codigo  AND v.fkEstorno.idArmazemFK = :pk_armazem AND v.fkEstorno.dataEstorno BETWEEN :data_inicio AND :data_fim   ORDER BY v.fkProdutos.codigo")
                .setParameter("codigo_produto", codigo_produto)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("pk_armazem", pk_armazem);
               
        List<Long> result = query.getResultList();
        em.close();       
        return result.get(0);
       
    }
    
    
    
    public List<TbItemEstorno> getAll_ItensSaidos(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbItemEstorno  i WHERE i.fkEstorno.pkEstorno BETWEEN :data_inicio AND :data_fim AND i.fkEstorno.statusEliminado = 'false' AND i.fkEstorno.idArmazemFK.codigo = :pk_armazem")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("pk_armazem", pk_armazem);
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemEstorno itemSaidas = new TbItemEstorno(0);
        result.add(itemSaidas);
        
        return result;
    }
    
    
    
    public List<TbItemEstorno> getAllItemSaidasByIdSaidaa(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemEstorno  v WHERE v.fkEstorno.pkEstorno = :pkEstorno AND v.fkEstorno.statusEliminado = 'false'")
                .setParameter("pkEstorno", pkEstorno);
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemEstorno itemSaidas = new TbItemEstorno(0);
        result.add(itemSaidas);        
        return result;
    }
    
    
    
    public List<TbEstorno> getAllVenda(Date data_inicio, Date data_fim) {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i.fk FROM TbItemEstorno  i WHERE i.fkEstorno.dataEstorno BETWEEN :data_inicio AND :data_fim AND i.fkEstorno.statusEliminado = 'false' GROUP BY i.fkEstorno.pkEstorno")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    
    
    public boolean exist_factura(int id_venda)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemEstorno  v WHERE v.fkEstorno.pkEstorno = :id_venda AND v.fkEstorno.statusEliminado = 'false'")
                .setParameter("id_venda", id_venda);
               
        List<TbItemEstorno> result = query.getResultList();
        em.close();
       
        return !result.isEmpty();
      
    }
    
    
    public static void main(String[] args) 
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        
        
        ItemEstornoDao itemVendaDao = new ItemEstornoDao(emf);
        VendaDao vendaDao = new VendaDao(emf);
        PrecoDao precoDao = new PrecoDao(emf);
        
//        List<TbItemEstorno> list = itemSaidaDao.getAllVenda();
       
      
//        
//        for (int i = 0; i < list.size(); i++) 
//        {
//            
//            TbItemEstorno itemSaidas = list.get(i);            
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
