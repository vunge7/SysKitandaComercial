/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbPaisJpaController;
import entity.TbPais;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PaisDao extends TbPaisJpaController {

    public PaisDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <TbPais> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.descrisao FROM TbPais p");
            return query.getResultList();
    }
     
      public List <TbPais> buscaTodosPaises () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM TbPais p  ORDER BY p.descrisao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long idPais) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descrisao FROM TbPais a WHERE a.idPais = :idPais")
                    .setParameter("idPais", idPais);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.idPais FROM TbPais a WHERE a.descrisao = :descrisao")
                    .setParameter("descrisao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
    
     public boolean exist_pais (String descrisao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM TbPais s WHERE s.descrisao = :descrisao")
                    .setParameter("descrisao", descrisao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
}
