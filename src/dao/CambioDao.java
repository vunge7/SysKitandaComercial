/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.CambioJpaController;
import entity.AnoEconomico;
import entity.Cambio;
//import controller.CambioJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class CambioDao extends CambioJpaController {

    public CambioDao(EntityManagerFactory emf) {
        super(emf);
    }

    public Cambio getLastObject(int pk_moeda){
        return findCambio(getLastCambioByIdMoeda(pk_moeda));
    }
    
    public int getLastCambioByIdMoeda(int pk_moeda) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  MAX(c.pkCambio)  FROM Cambio  c WHERE c.fkMoeda.pkMoeda = :pk_moeda")
                .setParameter("pk_moeda", pk_moeda);

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() ) {
            return result.get(0);
        }

        return 0;

    }
    
    

}
