/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbAcertoJpaController;
import entity.TbAcerto;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
public class AcertoDao extends TbAcertoJpaController{

    public AcertoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public List<TbAcerto> getAllEntradasByIdArmazem(int id_armazem,  Date data_inicio, Date data_fim)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  e AS TOTAL FROM TbAcerto  e WHERE e.idArmazemFK.codigo = :id_armazem  AND e.dataEntrada BETWEEN :data_inicio AND :data_fim ")              
                .setParameter("id_armazem", id_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbAcerto> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
    
}
