/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbVasilhameJpaController;

import entity.TbVasilhame;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class VasilhameDao extends TbVasilhameJpaController{

    public VasilhameDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public TbVasilhame getVasilhameByDescricao(String nome)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM TbVasilhame  p WHERE p.descricao = :nome")
                .setParameter("nome", nome );
               
        List<TbVasilhame> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
    
    
    
    
    
    public TbVasilhame getVasilhameByCodigoAndIdArmazem(int idVasilhame, int idArmazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbVasilhame  v WHERE v.pkVasilhame = :idVasilhame AND v.fkArmazem.codigo = :idArmazem")
                .setParameter("idVasilhame", idVasilhame )
                .setParameter("idArmazem", idArmazem );
               
        List<TbVasilhame> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
    }
   
    
    
     public Vector<String> getVasilhameByCategoria(String categoria)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p.nome FROM TbVasilhame  p WHERE p.fkCategoria.descricao = :categoria")
                .setParameter("categoria", categoria );
               
        Vector<String> result =  (Vector)query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
     
     
    public boolean exist_vasilhame(int codigo_produto, int idArmazem)
    {
        

        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbVasilhame v WHERE   v.fkProduto.codigo = :codigo_produto AND v.fkArmazem.codigo = :idArmazem")
                .setParameter("idArmazem", idArmazem)
                .setParameter("codigo_produto", codigo_produto);
                
        
        List<TbVasilhame> result = query.getResultList();
        em.close();
        
        return !result.isEmpty();
    
    }
    
    
    
    
    public TbVasilhame getVasilhameByIdProdutoAndIdArmazem(int codigo_produto, int idArmazem)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbVasilhame v WHERE   v.fkProduto.codigo = :codigo_produto AND v.fkArmazem.codigo = :idArmazem")
                .setParameter("idArmazem", idArmazem)
                .setParameter("codigo_produto", codigo_produto);
                
        
        List<TbVasilhame> result = query.getResultList();
        em.close();
        
         if(  !result.isEmpty() )
             return result.get(0);
         else return null;
    
    }
    

    

    public boolean exist_vasilhame(String vasilhame)
    {
        

        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbVasilhame v WHERE   v.descricao = :vasilhame")
                .setParameter("vasilhame", vasilhame);
                
        
        List<TbVasilhame> result = query.getResultList();
        em.close();
        
        return !result.isEmpty();
    
    }

    public List<TbVasilhame> getAllVasilhames()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v.descricao FROM TbVasilhame  v");
               
        List<TbVasilhame> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
    
    
    
    
     public List<TbVasilhame> getAllVasilhamesByCodigoProduto(int codigo)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v.descricao FROM TbVasilhame  v WHERE v.fkProduto.codigo = :codigo")
                .setParameter("codigo", codigo);
               
        List<TbVasilhame> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
   
     
}
