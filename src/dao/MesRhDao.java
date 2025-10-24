/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
//import controlador.MesJpaController;
import controlador.TbMesRhJpaController;
//import entity.Mes;
import entity.TbMesRh;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MesRhDao extends TbMesRhJpaController{

    public MesRhDao(EntityManagerFactory emf) {
        super(emf);
    }
    
       public List <TbMesRh > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descricao FROM TbMesRh s");
            return query.getResultList();
    }
     
     
     public String  getDescricaoByIdMes(long pk_mes_rh) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descricao FROM TbMesRh s WHERE s.pk_mes_rh = :pk_mes_rh")
                    .setParameter("pk_mes_rh", pk_mes_rh);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.pkMesRh FROM TbMesRh s WHERE s.descricao = :decricao")
                    .setParameter("decricao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
}
