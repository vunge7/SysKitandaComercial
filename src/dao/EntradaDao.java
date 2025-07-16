/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbEntradaJpaController;
import entity.TbEntrada;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */

public class EntradaDao extends TbEntradaJpaController{

    public EntradaDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List<TbEntrada> getAllEntradasByIdArmazem(int id_armazem,  Date data_inicio, Date data_fim)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  e AS TOTAL FROM TbEntrada  e WHERE e.idArmazemFK.codigo = :id_armazem  AND e.dataEntrada BETWEEN :data_inicio AND :data_fim ")              
                .setParameter("id_armazem", id_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEntrada> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
     
       public List<TbEntrada> getAllEntradasByIdArmazem(int id_armazem, int id_fornecedor,  Date data_inicio, Date data_fim)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  e AS TOTAL FROM TbEntrada  e WHERE e.idArmazemFK.codigo = :id_armazem  AND e.idProduto.codFornecedores.codigo = :id_fornecedor AND e.dataEntrada BETWEEN :data_inicio AND :data_fim ")              
                .setParameter("id_armazem", id_armazem)
                .setParameter("id_fornecedor", id_fornecedor)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEntrada> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
    
}
