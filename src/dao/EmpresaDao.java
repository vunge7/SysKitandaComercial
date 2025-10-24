/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.EmpresaJpaController;
import entity.Empresa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class EmpresaDao extends EmpresaJpaController{

    public EmpresaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List<Empresa> getAllEmpresa()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.nome FROM Empresa  c");
               
        List<Empresa> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }

     
    public Empresa getEmpresaByDescricao(String nome)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Empresa  c WHERE c.nome = :nome")
                .setParameter("nome", nome );
               
        List<Empresa> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
    
    
    public boolean comparar (String x)
    {
            EntityManager em = getEntityManager();
            
            Query query = em.createQuery("SELECT s FROM Empresa s WHERE s.nome = :x")
                    .setParameter("x", x);
            
            List<Empresa> result = query.getResultList();
            
            return  !result.isEmpty();   
        
    
    
    }
    
        public int getIdByDescricao (String nome) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.pkEmpresa FROM Empresa a WHERE a.nome = :nome")
                    .setParameter("nome", nome);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }

    
}
