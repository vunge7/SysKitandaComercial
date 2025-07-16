/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbOperacaoSistemaJpaController;
import entity.TbOperacaoSistema;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
//import controller.OperacaoSistemaJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */

public class OperacaoSistemaDao extends TbOperacaoSistemaJpaController{

    public OperacaoSistemaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public long getLastIdOperacao() {
                
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  MAX(o.pkOperacaoSistema)  FROM TbOperacaoSistema  o");                
        List<Long> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);       
        return 0;
    
    }
    
    public boolean existe_feicho(Date date) {
            
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  o.pkOperacaoSistema  FROM TbOperacaoSistema  o WHERE o.dataFeicho = :data_feicho")
                         
                .setParameter("data_feicho", date);                
        List<Integer> result = query.getResultList();
        em.close();       
        return !result.isEmpty();
    
    }
    
    public boolean existe_abertura(Date date) {
                         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT o.pkOperacaoSistema  FROM TbOperacaoSistema  o WHERE o.dataAbertura = :data_abertura")                           
                .setParameter("data_abertura", date);   
        
        List<Integer> result = query.getResultList();
        em.close();       
        return !result.isEmpty();
    
    }
    
   
}
