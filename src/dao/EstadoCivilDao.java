/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbEstadoCivilJpaController;
import entity.TbEstadoCivil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */


public class EstadoCivilDao extends TbEstadoCivilJpaController {

    public EstadoCivilDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <TbEstadoCivil> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM TbEstadoCivil p");
            return query.getResultList();
    }
     
    public List <TbEstadoCivil> buscaTodosEstadoCiviles (){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM TbEstadoCivil p  ORDER BY p.designacao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long pkEstadoCivil) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.designacao FROM TbEstadoCivil a WHERE a.pkEstadoCivil = :pkEstadoCivil")
                    .setParameter("pkEstadoCivil", pkEstadoCivil);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.pkEstadoCivil FROM TbEstadoCivil a WHERE a.designacao = :designacao")
                    .setParameter("designacao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
    
    public boolean exist_estadoCivil (String designacao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM TbEstadoCivil s WHERE s.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
     
}
