/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.GrupoJpaController;
import controlador.FamiliaJpaController;
import controlador.TbFuncaoJpaController;
import entity.Grupo;
import entity.Familia;
import entity.TbFuncao;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */


public class FamiliaDao extends FamiliaJpaController {

    public FamiliaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List <Familia> buscaTodasFamilia () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Familia p");
            return query.getResultList();
    }
    
     
     public Vector <String > buscaFamiliasSelecione () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Familia p");
           Vector<String>  result  =  (Vector)query.getResultList();
           
           result.add(0,"-- Seleccione --");
         
           return  result;
    }
          
     public List <Familia> buscaTodasFamiliaServico () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Familia p p.pkFamilia == 1");
            return query.getResultList();
    }
     
     public List <Familia> buscaTodasFamiliaSemServico () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Familia p WHERE p.pkFamilia <> 1");
            return query.getResultList();
    }
    
      public List <Familia> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Familia p ORDER BY p.designacao ASC");
            return query.getResultList();
    }
     
    public List <Familia> buscaTodasFamilias (){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM Familia p  ORDER BY p.designacao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long pkGrupo) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.designacao FROM Familia a WHERE a.pkGrupo = :pkGrupo")
                    .setParameter("pkGrupo", pkGrupo);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.pkFamilia FROM Familia a WHERE a.designacao = :designacao")
                    .setParameter("designacao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
    
    public boolean exist_familia (String designacao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM Familia s WHERE s.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
    
        public Familia getFamiliaByDescricao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Familia a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao);

        List<Familia> result = query.getResultList();
        em.close();

        if (! result.isEmpty ()) {
            return result.get(0);
        }
        return null;

    }
        
    
    

     
}
