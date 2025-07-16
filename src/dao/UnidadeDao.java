/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.UnidadeJpaController;
import entity.Unidade;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class UnidadeDao extends UnidadeJpaController{

    public UnidadeDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public Vector<String> getAllCategoria()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.descricao FROM Unidade  c");
               
        Vector<String> result =  (Vector) query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }

     
      public List<Unidade> getAll()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Unidade  c ORDER BY c.descricao ASC");
               
        List<Unidade> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
     
    public Unidade getCategoriaByDescricao(String descricao)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Unidade  c WHERE c.descricao = :descricao")
                .setParameter("descricao", descricao );
               
        List<Unidade> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
    
    
    public boolean exist (String x)
    {
            EntityManager em = getEntityManager();
            
            Query query = em.createQuery("SELECT s FROM Unidade s WHERE s.descricao = :x")
                    .setParameter("x", x);
            
            List<Unidade> result = query.getResultList();
            
            return  !result.isEmpty();   
        
    
    
    }
    
    public Unidade getUnidadeByDescricao(String descricao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Unidade  a WHERE a.descricao = :descricao")
                .setParameter("descricao", descricao );
               
        List<Unidade> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
    
     public Vector <String > buscaTodos () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.descricao FROM Unidade tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           result.add(0, "--SELECIONE--");
           return  result;
     }
     
     public Vector <String > buscaTodosSelecione () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.descricao FROM Unidade tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           return  result;
     }
     
   
     

    public Vector <Unidade > buscaTodasCategoriasProdutos() 
    {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp FROM Unidade tp WHERE tp.descricao <> 'Serviços'   ORDER BY tp.descricao   ASC");
           Vector<Unidade>  result  =   ( Vector )query.getResultList();                    
           return  result;
    }
    

    
}
