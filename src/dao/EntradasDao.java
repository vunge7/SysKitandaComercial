/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.EntradaTesourariaJpaController;
//import controlador.PaisJpaController;
import entity.EntradaTesouraria;
//import controlador.TbPais;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EntradasDao extends EntradaTesourariaJpaController {

    public EntradasDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <EntradaTesouraria> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM EntradaTesouraria p ORDER BY p");
            return query.getResultList();
    }
     
      public List <EntradaTesouraria> buscaTodasEntradas () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM EntradaTesouraria p");
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
      
      
      
     public List<EntradaTesouraria> getAllEntradasByDataInicioDataFim(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();        
        Query query = em.createQuery("SELECT r FROM EntradaTesouraria r WHERE r.dataEntrada BETWEEN :data_inicio AND :data_fim  GROUP BY r.pkEntradaTesouraria")               
               
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
       
        List<EntradaTesouraria> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
}
