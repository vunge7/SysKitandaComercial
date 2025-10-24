/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.MoedaJpaController;
import entity.Moeda;
//import controller.MoedaJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MoedaDao extends MoedaJpaController {

    public MoedaDao(EntityManagerFactory emf) {
        super(emf);
    }

    public List<String> buscaTodos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s.designacao FROM Moeda s");
        List<String>  result = query.getResultList();
        result.add(0,"--Seleccione--");
        return result;
    }

    public String getDescricaoByIdMoeda(int pkMoeda) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s.designacao FROM Moeda s WHERE s.pkMoeda = :pkMoeda")
                .setParameter("pkMoeda", pkMoeda);

        List list = query.getResultList();

        if (list != null) {
            return String.valueOf(list.get(0));
        }
        return "";
    }

    public int getIdByDescricao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s.pkMoeda FROM Moeda s WHERE s.designacao = :designacao")
                .setParameter("designacao", designacao);
        List result = query.getResultList();

        if (result != null) {
            return Integer.parseInt(String.valueOf(result.get(0)));
        }
        return 0;

    }

    public Moeda getByDescricao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM Moeda s WHERE s.designacao = :designacao")
                .setParameter("designacao", designacao);
        List result = query.getResultList();

        if (!result.isEmpty ()) {
            return ( Moeda ) result.get(0);
        }
        return null;

    }
    
        public Moeda getByDesignacao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM Moeda s WHERE s.designacao = :designacao")
                .setParameter("designacao", designacao);
        List result = query.getResultList();

        if (!result.isEmpty ()) {
            return ( Moeda ) result.get(0);
        }
        return null;

    }

}
