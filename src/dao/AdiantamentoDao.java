/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbAdiantamentoJpaController;
import entity.TbAdiantamento;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AdiantamentoDao extends TbAdiantamentoJpaController {

    public AdiantamentoDao(EntityManagerFactory emf) {
        super(emf);
    }

    public List<TbAdiantamento> getAllAdiantamento() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM  TbAdiantamento a  ORDER BY a.idAdiantamentoFK DESC");

        List<TbAdiantamento> result = query.getResultList();
        em.close();

        if (!result.isEmpty()) {
            return result;
        }
        return null;

    }
    public List<TbAdiantamento> getAllAdiantamentoByIdEmpresa(int pkEmpresa) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM  TbAdiantamento a WHERE a.idFuncionarioFK.fkEmpresa.pkEmpresa = :pkEmpresa  ORDER BY a.idAdiantamentoFK DESC")
            .setParameter("pkEmpresa", pkEmpresa);
        List<TbAdiantamento> result = query.getResultList();
        em.close();

        if (!result.isEmpty()) {
            return result;
        }
        return null;

    }

    public double getNumeroAdiantamentosByIdFuncionario(int idFuncionario, Date data_inicio, Date data_fim) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT SUM(a.valorAdiantado) FROM  TbAdiantamento a   WHERE a.idFuncionarioFK.idFuncionario = :idFuncionario  AND a.data BETWEEN :data_inicio AND :data_fim  GROUP BY a.idFuncionarioFK")
                .setParameter("idFuncionario", idFuncionario)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);

        List<Double> result = query.getResultList();
        em.close();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return 0;

    }

    public int getUltimoAdiantamento() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(s.idAdiantamentoFK) FROM TbAdiantamento s");
        List<Integer> result = query.getResultList();
        em.close();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return 0;

    }

public List<TbAdiantamento> getAllAdiantamentosByDataInicioDataFim(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();        
        Query query = em.createQuery("SELECT r FROM TbAdiantamento r WHERE r.data BETWEEN :data_inicio AND :data_fim  GROUP BY r.idAdiantamentoFK")               
               
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
       
        List<TbAdiantamento> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }

}
