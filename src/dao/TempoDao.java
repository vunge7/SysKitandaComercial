/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbTempoJpaController;
import entity.TbTempo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TempoDao extends TbTempoJpaController{

    public TempoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    
     public List<TbTempo> getAllTempo()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM  TbTempo a  ORDER BY a.idTempoPK DESC");
            
        List<TbTempo> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
     
     public boolean exist_preofessor(long idFuncionario)
     {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT t FROM TbTempo t WHERE   t.idFuncionarioFK.idFuncionario = :idFuncionario ")
                .setParameter("idFuncionario", idFuncionario);
        return  !query.getResultList().isEmpty();
  
     }
    
     public int getTempoByIdProfessor(long idFuncionario)
     {
         
         EntityManager em = getEntityManager();
         Query query = em.createQuery("SELECT t FROM TbTempo t WHERE   t.idFuncionarioFK.idFuncionario = :idFuncionario ")
                .setParameter("idFuncionario", idFuncionario);
         List<TbTempo>  result =  query.getResultList();
        
         if (!result.isEmpty() ) {
             return result.get(0).getTempoDado();
         } else {
             return 0;
         }
  
     }
     
     
     
     
     
}
