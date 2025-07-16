/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import controlador.TbEntradaVasilhameJpaController;
import entity.TbEntradaVasilhame;
import entity.TbEntradaVasilhame;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class EntradaVasilhameDao extends  TbEntradaVasilhameJpaController{

    public EntradaVasilhameDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public int getLastEntradaVasilhame(){
           
        EntityManager em = getEntityManager();
        
        Query query = em.createQuery("SELECT  MAX (e.pkEntradaVasilhame) FROM TbEntradaVasilhame e");
        List<Integer> result = query.getResultList();
          
        em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
    }
     
     
     
    public List<TbEntradaVasilhame> getAllEntradaVasilhameByIdArmazem(int idArmazem, Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEntradaVasilhame v WHERE    v.dataEntrada BETWEEN :data_inicio AND :data_fim AND       v.fkAmazem.codigo = :idArmazem")
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("idArmazem", idArmazem);
        List<TbEntradaVasilhame> result = query.getResultList();
        em.close();
        if( !result.isEmpty() )
                return result;
        return null;
    }
//    
//    
//    public List<TbEntradaVasilhame> getAllEntradaVasilhameByIdUsuario(int idUsuario, Date data_inicio, Date data_fim)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT f FROM TbEntradaVasilhame f WHERE    f.dataEntradaVasilhame BETWEEN :data_inicio AND :data_fim AND       f.fkUsuario.pkUsuario = :pkUsuario")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pkUsuario", idUsuario);
//        List<TbEntradaVasilhame> result = query.getResultList();
//        em.close();
//        if( !result.isEmpty() )
//                return result;
//        return null;
//    }
//    
//    
//    
//    
//    
//    public List<TbEntradaVasilhame> getAllEntradaVasilhameByIdCliente(Date data_inicio, Date data_fim)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT f FROM TbEntradaVasilhame f WHERE    f.dataEntradaVasilhame BETWEEN :data_inicio AND :data_fim")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim);
//                
//        List<TbEntradaVasilhame> result = query.getResultList();
//        em.close();
//        if( !result.isEmpty() )
//                return result;
//        return null;
//    }
//
//    
    
}
