/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbContaJpaController;
//import entity.Aluno;
import entity.TbConta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ContaDao extends TbContaJpaController{

    public ContaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
       public List <TbConta > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s. FROM TbConta s");
            return query.getResultList();
    }
     
     
     public String  getDescricaoByIdConta(long idConta) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.descricao FROM TbConta s WHERE s.idConta = :idConta")
                    .setParameter("idConta", idConta);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.idConta FROM TbConta s WHERE s.descricao = :decricao")
                    .setParameter("decricao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
     
     public List<TbConta> getAllContas(){
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM  TbConta i  ORDER BY i.idFuncionarioFK.nome ASC");
            
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
     
     public List<TbConta> getAllContasByIdEmpresa(int pkEmpresa){
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM  TbConta i WHERE i.idFuncionarioFK.fkEmpresa.pkEmpresa =:pkEmpresa  ORDER BY i.idFuncionarioFK.nome ASC")
            .setParameter("pkEmpresa", pkEmpresa);
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
     
     
     
    public boolean exist_relacao_funcionario_conta(int idFuncionario, int idBanco)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbConta i  WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario AND i.idBancoFK.idBanco = :idBanco")
          
                .setParameter("idFuncionario", idFuncionario)
                .setParameter("idBanco", idBanco );
            
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
        {               
                return true;
        }       
        return false;
        
    }
    
    
    
    public boolean exist_relacao_funcionario_conta_para_alem(int idFuncionario, int idBanco, int idConta)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbConta i  WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario AND i.idBancoFK.idBanco = :idBanco AND i.idContaPK <> :idConta")
          
                .setParameter("idFuncionario", idFuncionario)
                .setParameter("idBanco", idBanco )
                .setParameter("idConta", idConta );
            
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
        {               
                return true;
        }       
        return false;
        
    }
    
    public List<TbConta> getContaLIKENome(String  nome ){
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbConta a WHERE  a.idFuncionarioFK.nome LIKE :nome ORDER BY a.idFuncionarioFK.nome")
                .setParameter("nome",nome +"%");
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
    
     public TbConta getContaByIdFuncionario(int  idFuncionario ){
           
         System.err.println("ID FUNCIONARIO: " +idFuncionario);
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbConta a WHERE  a.idFuncionarioFK.idFuncionario = :idFuncionario")
                .setParameter("idFuncionario",idFuncionario);
        List<TbConta> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        return null;
    }
    
    
}
