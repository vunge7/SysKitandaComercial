/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.EntradaTesourariaJpaController;
//import controlador.PaisJpaController;
import controlador.SaidasTesourariaJpaController;
import entity.EntradaTesouraria;
//import entity.TbPais;
import entity.SaidasTesouraria;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class SaidaDao extends SaidasTesourariaJpaController {

    public SaidaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <SaidasTesouraria> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM SaidasTesouraria p ORDER BY p");
            return query.getResultList();
    }
     
      public List <SaidasTesouraria> buscaTodasSaidas () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM SaidasTesouraria p");
            return query.getResultList();
    }
     
     
//     public String  getDescricaoById(long idPais) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT a.descrisao FROM TbPais a WHERE a.idPais = :idPais")
//                    .setParameter("idPais", idPais);
//            
//            List   list = query.getResultList();
//            
//            if( list!=null){
//                    return  String.valueOf( list.get(0) );
//            }
//            return "";
//    }
//    
//     public int getIdByDescricao (String descricao) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT a.idPais FROM TbPais a WHERE a.descrisao = :descrisao")
//                    .setParameter("descrisao", descricao);
//            
//            List result = query.getResultList();
//            
//            if( result!= null )
//                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
//            return 0;
//            
//    }
//    
//     public boolean exist_pais (String descrisao) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT s FROM TbPais s WHERE s.descrisao = :descrisao")
//                    .setParameter("descrisao", descrisao);
//            
//            List result = query.getResultList();
//            
//            if( !result.isEmpty() )
//                return  true;
//            return false;
//    }
      
      
    public List<SaidasTesouraria> getAllSaidasByDataInicioDataFim(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();        
        Query query = em.createQuery("SELECT r FROM SaidasTesouraria r WHERE r.dataSaida BETWEEN :data_inicio AND :data_fim  GROUP BY r.pkSaidasTesouraria")               
               
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
       
        List<SaidasTesouraria> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    public List<SaidasTesouraria> getBuscaTodasSaidasBancariasEntreDatas_e_Banco(int idBanco, Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM SaidasTesouraria  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.fkBanco.idBanco = :idBanco")             
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<SaidasTesouraria> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        SaidasTesouraria saidasTesouraria = new SaidasTesouraria(0);
        result.add(saidasTesouraria);
        
        return result;
    }
    
     public int getUltimaSaida()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(s.pkSaidasTesouraria) FROM SaidasTesouraria s");
        List<Integer> result = query.getResultList();
         em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }
      
      
}
