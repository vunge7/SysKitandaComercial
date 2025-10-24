/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.DepositoBancarioJpaController;
import controlador.DepositoBancarioJpaController;
import controlador.TransferenciaBancariaJpaController;
import entity.DepositoBancario;
import entity.DepositoBancario;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DepositoBancariaDao extends DepositoBancarioJpaController{

    public DepositoBancariaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List <String> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c.descricao FROM DepositoBancario c");
            List<String>  lista=  query.getResultList();
            
            lista.add(0, "--Seleccione--");
            return lista;
    }
  
     
//      public List <String> buscaTodosExceptoCaixaLocal() 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT c.descricao FROM DepositoBancario c WHERE c.idBanco <> 3");
//            List<String>  lista=  query.getResultList();
//            
//            lista.add(0, "--Seleccione--");
//            return lista;
//    }
  
     
      public List <String> buscaTodosOutro () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c.descricao FROM DepositoBancario c");
            List<String>  lista=  query.getResultList();
            
            lista.add(0, "-- Seleccione   --");
            lista.add(1, "-- Todos Bancos --");
            return lista;
    }
     
    public List <DepositoBancario > buscaTodosBancos () 
    {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c FROM DepositoBancario c");
            return query.getResultList();
    }
     
    
    
    
     public List <DepositoBancario > buscaTodosBancos (int idBanco) 
    {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c FROM DepositoBancario c  WHERE c.idBanco = :idBanco")
                   .setParameter("idBanco", idBanco);
            return query.getResultList();
    }
     
    
    
     
     public String  getDescricaoByIdBanco(long idBanco) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c.descricao FROM DepositoBancario c WHERE c.pkDeposito = :idBanco")
                    .setParameter("idBanco", idBanco);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     
     
     public int getIdByDescricao (String descricao) 
     {         
         System.out.println("DESCRICAO : " +descricao);
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c FROM DepositoBancario c WHERE c.descricao = :decricao")
                    .setParameter("decricao", descricao);
            
            List<DepositoBancario> result = query.getResultList();
            
            if( result!= null )
                return  result.get(0).getPkDeposito();
            return 0;
            
    }
     
     
    public double getTotalPropinaByBank(int idBanco, Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        
        Query query = em.createQuery("SELECT    SUM(p.total)  FROM Propinas p WHERE p.idBanco.idBanco = :idBanco   AND p.dataPropina BETWEEN  :data_inicio AND :data_fim GROUP BY  p.idBanco.idBanco ")
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
 
        List<Double> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }
     
     
    
    public double getTotalPagamentosDiversosByBank(int idBanco , Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        
        Query query = em.createQuery("SELECT    SUM(p.totalPagar)  FROM PagamentoDiversos p WHERE p.idBancoFK.idBanco = :idBanco   AND   p.dataPagamento BETWEEN   :data_inicio AND :data_fim  GROUP BY  p.idBancoFK.idBanco ")
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
 
        List<Double> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    } 
    
    public List<DepositoBancario> getBuscaTodosDepositosBancariosEntreDatas_e_Banco(int idBanco, Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM DepositoBancario  v WHERE v.data BETWEEN :data_inicio AND :data_fim AND v.fkBanco.idBanco = :idBanco")             
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<DepositoBancario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        DepositoBancario depositoBancario = new DepositoBancario(0);
        result.add(depositoBancario);
        
        return result;
    }
    
    public List<DepositoBancario> getAllDepositosByDataInicioDataFim(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();        
        Query query = em.createQuery("SELECT r FROM DepositoBancario r WHERE r.data BETWEEN :data_inicio AND :data_fim  GROUP BY r.pkDeposito")               
               
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
       
        List<DepositoBancario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
     
     
     
     public boolean exist_banco (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM DepositoBancario s WHERE s.descricao = :descricao")
                    .setParameter("descricao", descricao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
     
   
     
    
}
