/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.RegimeJpaController;
import entity.Regime;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class RegimeDao extends RegimeJpaController{

    public RegimeDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public Vector<String> getAllCategoria()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM Regime  c");
               
        Vector<String> result =  (Vector) query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }

     
      public List<Regime> getAll()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Regime  c ORDER BY c.designacao ASC");
               
        List<Regime> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
     
    public Regime getCategoriaByDescricao(String descricao)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Regime  c WHERE c.designacao = :designacao")
                .setParameter("designacao", descricao );
               
        List<Regime> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
    
    
    public boolean exist (String x)
    {
            EntityManager em = getEntityManager();
            
            Query query = em.createQuery("SELECT s FROM Regime s WHERE s.designacao = :x")
                    .setParameter("x", x);
            
            List<Regime> result = query.getResultList();
            
            return  !result.isEmpty();   
        
    
    
    }
//    
//        public TbFornecedor getFornecedorByDescricao(String descricao) {
//        
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT a FROM TbFornecedor  a WHERE a.nome = :designacao")
//                .setParameter("designacao", descricao );
//               
//        List<TbFornecedor> result = query.getResultList();
//        em.close();
//       
//        if( result!=null )
//                return result.get(0);
//        return new TbFornecedor(0);
//        
//    }
    
    public Regime getRegimeByDescricao(String descricao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Regime  a WHERE a.designacao = :designacao")
                .setParameter("designacao", descricao );
               
        List<Regime> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty () )
                return result.get(0);
        return null;
        
    }
    
     public Vector <String > buscaTodos () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.designacao FROM Regime tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           result.add(0, "--SELECIONE--");
           return  result;
     }
     
     public Vector <String > buscaTodosSelecione () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp.designacao FROM Regime tp  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           return  result;
     }
     
   
     

    public Vector <Regime > buscaTodasCategoriasProdutos() 
    {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT tp FROM Regime tp WHERE tp.designacao <> 'Serviços'   ORDER BY tp.designacao   ASC");
           Vector<Regime>  result  =   ( Vector )query.getResultList();                    
           return  result;
    }
    
}
