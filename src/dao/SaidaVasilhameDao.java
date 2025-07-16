/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import controlador.TbSaidaVasilhameJpaController;
import entity.TbSaidaVasilhame;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class SaidaVasilhameDao extends  TbSaidaVasilhameJpaController{

    public SaidaVasilhameDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public int getLastSaidaVasilhame(){
           
        EntityManager em = getEntityManager();
        
        Query query = em.createQuery("SELECT  MAX (e.pkSaidaVasilhame) FROM TbSaidaVasilhame e");
        List<Integer> result = query.getResultList();
          
        em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
    }
     
     
          
    public List<TbSaidaVasilhame> getAllSaidaVasilhameByIdArmazem(int idArmazem, Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidaVasilhame v WHERE    v.dataSaida BETWEEN :data_inicio AND :data_fim AND  v.fkArmazem.codigo = :idArmazem")
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("idArmazem", idArmazem);
        List<TbSaidaVasilhame> result = query.getResultList();
        em.close();
        if( !result.isEmpty() )
                return result;
        return null;
    }
     
     
//    public List<TbVenda> getAllVendaByIdCliente(int idCliente, Date data_inicio, Date data_fim)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT f FROM TbVenda f WHERE    f.dataVenda BETWEEN :data_inicio AND :data_fim AND       f.fkCliente.tbCliente = :pkCliente")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pkCliente", idCliente);
//        List<TbVenda> result = query.getResultList();
//        em.close();
//        if( !result.isEmpty() )
//                return result;
//        return null;
//    }
//    
//    
//    public List<TbVenda> getAllVendaByIdUsuario(int idUsuario, Date data_inicio, Date data_fim)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT f FROM TbVenda f WHERE    f.dataVenda BETWEEN :data_inicio AND :data_fim AND       f.fkUsuario.pkUsuario = :pkUsuario")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pkUsuario", idUsuario);
//        List<TbVenda> result = query.getResultList();
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
//    public List<TbVenda> getAllVendaByIdCliente(Date data_inicio, Date data_fim)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT f FROM TbVenda f WHERE    f.dataVenda BETWEEN :data_inicio AND :data_fim")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim);
//                
//        List<TbVenda> result = query.getResultList();
//        em.close();
//        if( !result.isEmpty() )
//                return result;
//        return null;
//    }
//
//    
    
}
