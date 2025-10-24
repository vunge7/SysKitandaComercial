/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.ProductosJpaController;
import controlador.TbFuncaoJpaController;
import entity.Productos;
import entity.Productos;
import entity.TbFuncao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */


public class ProductosDao extends ProductosJpaController {

    public ProductosDao(EntityManagerFactory emf) {
        super(emf);
    }
    
      public List <Productos> buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.designacao FROM Productos p");
            return query.getResultList();
    }
     
    public List <Productos> buscaTodosProductoss (){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM Productos p  ORDER BY p.designacao");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long pkProductos) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.designacao FROM Productos a WHERE a.pkProductos = :pkProductos")
                    .setParameter("pkProductos", pkProductos);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.pkProductos FROM Productos a WHERE a.designacao = :designacao")
                    .setParameter("designacao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
    
    public boolean exist_grupo (String designacao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM Productos s WHERE s.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
    
    public Productos getProductosByDescricao(String designacao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Productos  a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao );
               
        List<Productos> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
     
}
