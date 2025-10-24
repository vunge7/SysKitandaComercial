/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbProfissaoJpaController;
import entity.TbProfissao;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProfissaoDao extends TbProfissaoJpaController{

    public ProfissaoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public Vector <String > buscaTodos () 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p.descricao FROM TbProfissao p");
           Vector<String>  result  =  (Vector)query.getResultList();
           
           result.add(0,"-- Seleccione --");
         
           return  result;
    }
     
     public List <TbProfissao > buscaTodasProfissaos() 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT p FROM TbProfissao p");
            return query.getResultList();
    }
     
     
     public String  getDescricaoById(long idProfissao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descricao FROM TbProfissao a WHERE a.idProfissao = :idProfissao")
                    .setParameter("idProfissao", idProfissao);
            
            List   list = query.getResultList();
            
            if( list!=null){
                    return  String.valueOf( list.get(0) );
            }
            return "";
    }
    
     
     public  List <TbProfissao >  getDescricaoByIdProfissao(int idPais) 
     {         
            
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.descricao FROM TbProfissao a WHERE a.idPais.idPais = :idPais")
                    .setParameter("idPais", idPais);
            
            //List list = query.getResultList();
            
            return query.getResultList();
    }
     
     
     public int getIdByDescricao (String descricao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.idProfissao FROM TbProfissao a WHERE a.descricao = :descricao")
                    .setParameter("descricao", descricao);
            
            List result = query.getResultList();
            
            if( result!= null )
            {
                System.err.println("ID provicnia " +result.get(0) );
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            }
            return 0;
            
    }
     
     public boolean exist_profissao (String descrisao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s FROM TbProfissao s WHERE s.descricao = :descricao")
                    .setParameter("descricao", descrisao);
            
            List result = query.getResultList();
            
            if( !result.isEmpty() )
                return  true;
            return false;
    }
}
