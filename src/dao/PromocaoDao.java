/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.PromocaoJpaController;

import entity.Promocao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Dallas
 */
public class PromocaoDao extends PromocaoJpaController{

    public PromocaoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    
    
    public Promocao getPromocaoByDescricao(String nome)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM Promocao  p WHERE p.designacao = :nome")
                .setParameter("nome", nome );
               
        List<Promocao> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
   public Promocao getPromocao(int id){
        return findPromocao(id);
   
   }
     
    public Integer getUltimoIdPromocao(boolean retalho_grosso) {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.pkPromocao) FROM Promocao p WHERE   p.retalhoGroso = :retalho_grosso")
                .setParameter("retalho_grosso", retalho_grosso);
            
        List<Integer> result = query.getResultList();
         em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }
    
    
     public Integer getUltimoIdPromocao() {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.pkPromocao) FROM Promocao p");
            
        List<Integer> result = query.getResultList();
         em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }
    
//    public Promocao getUltimoIdPromocao() {
//        
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT MAX(p.pkPromocao) FROM Promocao p");
//        List<Integer> result = query.getResultList();
//        em.close();
//        if( !result.isEmpty() )
//                findPromocao(result.get(0));
//        return new Promocao(0);
//        
//    }
    
    public static void main(String[] args) {
          EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
          PromocaoDao precoDao = new PromocaoDao(emf);
    
    }
  
}
