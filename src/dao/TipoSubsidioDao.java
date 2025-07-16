/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbSubsidiosJpaController;
import entity.TbSubsidios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TipoSubsidioDao extends TbSubsidiosJpaController{

    public TipoSubsidioDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    
     public List<TbSubsidios> getAllSubsidios()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM  TbSubsidios a  ORDER BY a.descricao  ASC");
            
        List<TbSubsidios> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
     
     public boolean exist_subsidio(long idSubsidios)
     {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM TbSubsidios s WHERE   s.idSubsidios = :idSubsidios")
                .setParameter("idSubsidios", idSubsidios);
        return  !query.getResultList().isEmpty();
  
     }
    

     public boolean exist_subsidio(String descricao)
     {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM TbSubsidios s WHERE   s.descricao = :descricao")
                .setParameter("descricao", descricao);
        return  !query.getResultList().isEmpty();
  
     }

    
     
}
