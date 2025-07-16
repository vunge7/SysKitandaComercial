/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbSexoJpaController;
import entity.TbSexo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class SexoDao extends TbSexoJpaController{

    public SexoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
       public List <TbSexo > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descrisao FROM TbSexo s");
            return query.getResultList();
    }
     
      public List <String> buscaTodosSexos() 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descrisao FROM TbSexo s ");
            
            List<String> result = query.getResultList();
            result.add(0, "--Selecione--");
            return result;
    }
     
     public String  getDescricaoByIdSexo(long idSexo) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descrisao FROM TbSexo s WHERE s.idSexo = :idSexo")
                    .setParameter("idSexo", idSexo);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.idSexo FROM TbSexo s WHERE s.descrisao = :decricao")
                    .setParameter("decricao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
}
