/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbBancoJpaController;
import entity.TbBanco;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BancoDao extends TbBancoJpaController {

    public BancoDao() {
        this(em);
    }

    public BancoDao(EntityManagerFactory emf) {
        super(emf);
    }

    public static List<String> buscaTodos() {
        Query query = UtilDao.getEntityManager1 ().createQuery("SELECT c.descrisao FROM TbBanco c ORDER BY c.descrisao");
        List<String> lista = query.getResultList();

//            lista.add(0, "--Seleccione--");
        return lista;
    }

    public List<String> buscaTodosCaixaLocal() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.descrisao FROM TbBanco c WHERE c.idBanco = 1");
        List<String> lista = query.getResultList();

        return lista;
    }

    public List<String> buscaTodosExceptoCaixaLocal1() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.descrisao FROM TbBanco c WHERE c.idBanco <> 1");
        List<String> lista = query.getResultList();

        lista.add(0, "--Seleccione--");
        return lista;
    }

    public List<String> buscaTodosOutro() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.descrisao FROM TbBanco c");
        List<String> lista = query.getResultList();

        lista.add(0, "-- Seleccione   --");
        lista.add(1, "-- Todos Bancos --");
        return lista;
    }

    public List<TbBanco> buscaTodosBancos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbBanco c");
        return query.getResultList();
    }

    public List<TbBanco> buscaTodosBancos(int idBanco) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbBanco c  WHERE c.idBanco = :idBanco")
                .setParameter("idBanco", idBanco);
        return query.getResultList();
    }

    public String getDescricaoByIdBanco(long idBanco) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.descrisao FROM TbBanco c WHERE c.idBanco = :idBanco")
                .setParameter("idBanco", idBanco);

        List list = query.getResultList();

        if (list != null) {
            return String.valueOf(list.get(0));
        }
        return "";
    }

    public int getIdByDescricao(String descricao) {
        System.out.println("DESCRICAO : " + descricao);
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbBanco c WHERE c.descrisao = :decricao")
                .setParameter("decricao", descricao);

        List<TbBanco> result = query.getResultList();

        if (result != null) {
            return result.get(0).getIdBanco();
        }
        return 0;

    }

    public double getTotalPropinaByBank(int idBanco, Date data_inicio, Date data_fim) {

        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT    SUM(p.total)  FROM Propinas p WHERE p.idBanco.idBanco = :idBanco   AND p.dataPropina BETWEEN  :data_inicio AND :data_fim GROUP BY  p.idBanco.idBanco ")
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);

        List<Double> result = query.getResultList();
        em.close();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return 0;

    }

    public double getTotalPagamentosDiversosByBank(int idBanco, Date data_inicio, Date data_fim) {

        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT    SUM(p.totalPagar)  FROM PagamentoDiversos p WHERE p.idBancoFK.idBanco = :idBanco   AND   p.dataPagamento BETWEEN   :data_inicio AND :data_fim  GROUP BY  p.idBancoFK.idBanco ")
                .setParameter("idBanco", idBanco)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);

        List<Double> result = query.getResultList();
        em.close();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return 0;

    }

    public Integer getCodigoBanco() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT b.idBanco FROM TbBanco b");

        List<Integer> result = query.getResultList();
        em.close();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return 0;

    }

    public boolean exist_banco(String descricao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM TbBanco s WHERE s.descrisao = :descrisao")
                .setParameter("descrisao", descricao);

        List result = query.getResultList();

        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }

}
