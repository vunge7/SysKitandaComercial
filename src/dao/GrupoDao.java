/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.GrupoJpaController;
import controlador.TbFuncaoJpaController;
import entity.Grupo;
import entity.TbFuncao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */


public class GrupoDao extends GrupoJpaController {

    public GrupoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <Grupo> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Grupo p");
            return query.getResultList();
    }
     
    public List <Grupo> buscaTodosGrupos (){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM Grupo p  ORDER BY p.designacao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long pkGrupo) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.designacao FROM Grupo a WHERE a.pkGrupo = :pkGrupo")
                    .setParameter("pkGrupo", pkGrupo);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.pkGrupo FROM Grupo a WHERE a.designacao = :designacao")
                    .setParameter("designacao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
    
    public boolean exist_grupo (String designacao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM Grupo s WHERE s.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
    
    public Grupo getGrupoByDescricao(String designacao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Grupo  a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao );
               
        List<Grupo> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
     
}
