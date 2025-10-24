/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbTipoProdutoJpaController;
import entity.TbTipoProduto;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class CategoriaDao extends TbTipoProdutoJpaController {

    public CategoriaDao(EntityManagerFactory emf) {
        super(emf);
    }

    public List<TbTipoProduto> getAllCategoria() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbTipoProduto  c");

        List<TbTipoProduto> result = query.getResultList();
        em.close();

        if (result != null) {
            return result;
        }
        return null;
    }

    public Vector<String> buscaTodos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbTipoProduto c ORDER BY c.designacao ASC ");
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public TbTipoProduto getCategoriaByDescricao(String descricao) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbTipoProduto  c WHERE c.designacao = :designacao")
                .setParameter("designacao", descricao);

        List<TbTipoProduto> result = query.getResultList();
        em.close();

        if (result != null) {
            return result.get(0);
        }
        return null;
    }

    public boolean comparar(String x) {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT s FROM TbTipoProduto s WHERE s.designacao = :x")
                .setParameter("x", x);

        List<TbTipoProduto> result = query.getResultList();

        return !result.isEmpty();

    }

}
