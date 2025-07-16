/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

//import controller.TipoUserJpaController;
import controlador.TbTipoUsuarioJpaController;
import entity.TbTipoUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TipoUsuarioDao extends TbTipoUsuarioJpaController{

    public TipoUsuarioDao(EntityManagerFactory emf) {
        super(emf);
    }
    
       public List <TbTipoUsuario > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descricao FROM TbTipoUsuario s");
            return query.getResultList();
    }
     
     
     public String  getDescricaoByIdTipoUser(long idTipoUsuario) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descricao FROM TbTipoUsuario s WHERE s.idTipoUsuario = :idTipoUsuario")
                    .setParameter("idTipoUsuario", idTipoUsuario);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.idTipoUsuario FROM TbTipoUsuario s WHERE s.descricao = :descricao")
                    .setParameter("descricao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
}
