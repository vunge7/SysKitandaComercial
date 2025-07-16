/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbProFormaJpaController;
import entity.TbProForma;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 * 
 */

public class ProFormaDao extends TbProFormaJpaController{

    public ProFormaDao(EntityManagerFactory emf) {
        super(emf);
    }
   
    public List<TbProForma> getAllProforma(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbProForma  v WHERE v.data BETWEEN :data_inicio AND :data_fim")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbProForma> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbProForma proForma = new TbProForma(0);
        result.add(proForma);
        
        return result;
    }
    
    public int getLastProforma()
    {
                
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  MAX(v.pkProForma)  FROM TbProForma  v");
                
        List<Integer> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
       
        return 0;
    
    }

    
}
