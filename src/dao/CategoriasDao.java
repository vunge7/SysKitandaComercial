/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.ModeloJpaController;
import controlador.TbTipoProdutoJpaController;
import entity.Modelo;
import entity.TbTipoProduto;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class CategoriasDao extends TbTipoProdutoJpaController {

    public CategoriasDao(EntityManagerFactory emf) {
        super(emf);
    }

    public Vector<String> buscaTodosExtra() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbTipoProduto c ORDER BY c.designacao ASC ");
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbTipoProduto> buscaTodos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p.designacao FROM TbTipoProduto p");
        return query.getResultList();
    }

    public Vector<String> buscaTodo() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbTipoProduto c ORDER BY c.designacao ASC ");
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbTipoProduto> buscaTodasCategorias() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM TbTipoProduto p ORDER BY p.fkFamilia.designacao");
        return query.getResultList();
    }

    public String getDescricaoById(long codigo) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a.designacao FROM TbTipoProduto a WHERE a.codigo = :codigo")
                .setParameter("codigo", codigo);

        List list = query.getResultList();

        if (list != null) {
            return String.valueOf(list.get(0));
        }
        return "";
    }

    public List<TbTipoProduto> getDescricaoByIdFamilia(int pkFamilia) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a.designacao FROM TbTipoProduto a WHERE a.fkFamilia.pkFamilia = :pkFamilia")
                .setParameter("pkFamilia", pkFamilia);

        //List list = query.getResultList();
        return query.getResultList();
    }

    public int getIdByDescricao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a.codigo FROM TbTipoProduto a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao);

        List result = query.getResultList();

        if (result != null) {

            return Integer.parseInt(String.valueOf(result.get(0)));
        }
        return 0;

    }

    public boolean exist_categoria(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM TbTipoProduto s WHERE s.designacao = :designacao")
                .setParameter("designacao", designacao);

        List result = query.getResultList();

        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<TbTipoProduto> getDescricaoByIdFamilias(int pk_familia) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a.designacao FROM TbTipoProduto a WHERE a.fkFamilia.pkFamilia = :pkFamilia")
                .setParameter("pk_familia", pk_familia);

        //List list = query.getResultList();
        return query.getResultList();
    }

    public TbTipoProduto getCategoriasByDesignacao(String designacao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbTipoProduto a WHERE a.designacao = :designacao")
                .setParameter("designacao", designacao);

        List<TbTipoProduto> result = query.getResultList();
        em.close();
        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }
}
