/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbProvinciaJpaController;
import entity.TbProvincia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProvinciaDao extends TbProvinciaJpaController{

    public ProvinciaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List <TbProvincia > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.descrisao FROM TbProvincia p");
            return query.getResultList();
    }
     
     public List <TbProvincia > buscaTodasProvincias() 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM TbProvincia p ORDER BY p.idPais.descrisao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long idProvincia) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descrisao FROM TbProvincia a WHERE a.idProvincia = :idProvincia")
                    .setParameter("idProvincia", idProvincia);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     
     public  List <TbProvincia >  getDescricaoByIdPais(int idPais) 
     {         
            
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descrisao FROM TbProvincia a WHERE a.idPais.idPais = :idPais")
                    .setParameter("idPais", idPais);
            
            //List list = query.getResultList();
            
            return query.getResultList();
    }
     
     
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.idProvincia FROM TbProvincia a WHERE a.descrisao = :descrisao")
                    .setParameter("descrisao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
            {
                System.err.println("ID provicnia " +result.get(0) );
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            }
            return 0;
            
    }
     
     public boolean exist_provincia (String descrisao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM TbProvincia s WHERE s.descrisao = :descrisao")
                    .setParameter("descrisao", descrisao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
}
