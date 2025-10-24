/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbBalancoJpaController;
//import controller.BalancoJpaController;
import entity.TbBalanco;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BalancoDao extends TbBalancoJpaController{

    public BalancoDao(EntityManagerFactory emf) {
        super(emf);
    }
     
    public List<TbBalanco> getAllBalancoByData(Date data_inicio, Date data_fim) {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT b FROM TbBalanco  b WHERE  b.dataBalanco BETWEEN :data_inicio AND :data_fim ORDER BY b.dataBalanco ASC")
             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbBalanco> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbBalanco balanco = new TbBalanco();
        result.add(balanco);
        
        return result;
    }
     
    
   
}
