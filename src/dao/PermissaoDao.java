/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbPermissaoJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
public class PermissaoDao extends TbPermissaoJpaController{
    
    public PermissaoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
//    
//     public int getIdByDescricao (String descricao) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT  p.idPermissao FROM TbPermissao p WHERE p.descricao = :descricao")
//                    .setParameter("descricao", descricao);
//            
//            List<Integer> result = query.getResultList();
//            
//            if( result!= null )
//                return  result.get(0);
//            return 0;
//            
//    }
     
     
      public long getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.idPermissao FROM TbPermissao s WHERE s.descricao = :decricao")
                    .setParameter("decricao", descricao);
            
            List<Long> result = query.getResultList();
            
            if( !result.isEmpty() ){
                System.out.println("ID " + result.get(0) );
                return   result.get(0)   ;
            }
            return 0;
            
    }
    
    
}
