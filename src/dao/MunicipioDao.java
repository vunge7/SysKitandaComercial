/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbMunicipioJpaController;
import entity.TbMunicipio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MunicipioDao extends  TbMunicipioJpaController {

    public MunicipioDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <TbMunicipio > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m.descrisao FROM TbMunicipio m");
            return query.getResultList();
    }
     
       public List <TbMunicipio > buscaTodosMunicipios () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m FROM TbMunicipio m   ORDER BY m.idProvincia.descrisao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long idMunicipio) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descrisao FROM TbMunicipio a WHERE a.idMunicipio = :idMunicipio")
                    .setParameter("idMunicipio", idMunicipio);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
     
     public  List <TbMunicipio >  getDescricaoByIdProvincia(long idProvincia) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descrisao FROM TbMunicipio a WHERE a.idProvincia.idProvincia = :idProvincia")
                    .setParameter("idProvincia", idProvincia);
            
            return query.getResultList();
  
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.idMunicipio FROM TbMunicipio a WHERE a.descrisao = :descrisao")
                    .setParameter("descrisao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
     
     
     
      public boolean exist_municipio (String descrisao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM TbMunicipio s WHERE s.descrisao = :descrisao")
                    .setParameter("descrisao", descrisao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
}
