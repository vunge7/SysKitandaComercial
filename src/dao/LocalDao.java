/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbLocalJpaController;
import entity.TbLocal;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class LocalDao extends TbLocalJpaController{

    public LocalDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public Vector<String> getAllCategoria()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbLocal  c");
               
        Vector<String> result =  (Vector) query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }

     
      public List<TbLocal> getAll()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbLocal  c ORDER BY c.designacao ASC");
               
        List<TbLocal> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
     
    public TbLocal getCategoriaByDescricao(String descricao)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbLocal  c WHERE c.designacao = :designacao")
                .setParameter("designacao", descricao );
               
        List<TbLocal> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
    
    
    public boolean exist (String x)
    {
            EntityManager em = getEntityManager();
            
            Query query = em.createQuery("SELECT s FROM TbLocal s WHERE s.designacao = :x")
                    .setParameter("x", x);
            
            List<TbLocal> result = query.getResultList();
            
            return  !result.isEmpty();   
        
    
    
    }
    
    public TbLocal getLocalByDescricao(String descricao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbLocal  a WHERE a.designacao = :designacao")
                .setParameter("designacao", descricao );
               
        List<TbLocal> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
    
     public Vector <String > buscaTodos () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.designacao FROM TbLocal tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
//           result.add(0, "--SELECIONE--");
           return  result;
     }
     
     public Vector <String > buscaTodosSelecione () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.designacao FROM TbLocal tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           return  result;
     }
     
   
     

    public Vector <TbLocal > buscaTodasCategoriasProdutos() 
    {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp FROM TbLocal tp WHERE tp.designacao <> 'Serviços'   ORDER BY tp.designacao   ASC");
           Vector<TbLocal>  result  =   ( Vector )query.getResultList();                    
           return  result;
    }
    
}
