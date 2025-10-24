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
public class TipoProdutoDao extends TbTipoProdutoJpaController {

    public TipoProdutoDao(EntityManagerFactory emf) {
        super(emf);
    }

    public Vector<String> getAllCategoria() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c.designacao FROM TbTipoProduto  c");

        Vector<String> result = (Vector) query.getResultList();
        em.close();

        if (result != null) {
            return result;
        }
        return null;
    }

    public List<TbTipoProduto> getAll() {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbTipoProduto  c ORDER BY c.designacao ASC");

        List<TbTipoProduto> result = query.getResultList();
        em.close();

        if (result != null) {
            return result;
        }
        return null;
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

    public boolean exist(String x) {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT s FROM TbTipoProduto s WHERE s.designacao = :x")
                .setParameter("x", x);

        List<TbTipoProduto> result = query.getResultList();

        return !result.isEmpty();

    }

    public TbTipoProduto getTipoProdutoByDescricao(String descricao) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbTipoProduto  a WHERE a.designacao = :designacao")
                .setParameter("designacao", descricao);

        List<TbTipoProduto> result = query.getResultList();
        em.close();
        

        if (result != null) {
//            return result.get(0);
            return result.get(0);
        }
        return null;

    }

    public Vector<String> buscaTodos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT tp.designacao FROM TbTipoProduto tp  ");
        Vector<String> result = (Vector) query.getResultList();
        return result;
    }

    public Vector<TbTipoProduto> buscaTodasCategoriasProdutos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT tp FROM TbTipoProduto tp WHERE tp.fkFamilia.pkFamilia <> 1 ORDER BY tp.designacao   ASC");
        Vector<TbTipoProduto> result = (Vector) query.getResultList();
        return result;
    }
    
//    public Vector<TbTipoProduto> buscaTodasCategoriasProdutos() {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT tp FROM TbTipoProduto tp WHERE tp.designacao <> 'Serviços'   ORDER BY tp.designacao   ASC");
//        Vector<TbTipoProduto> result = (Vector) query.getResultList();
//        return result;
//    }
    
     public  List <TbTipoProduto >  getDescricaoByIdFamilias(int pkFamilia) 
     {         
            
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.designacao FROM TbTipoProduto a WHERE a.fkFamilia.pkFamilia = :pkFamilia")
                    .setParameter("pkFamilia", pkFamilia);
            
            //List list = query.getResultList();
            
            return query.getResultList();
    }
     
          public int getIdByDescricao (String designacao) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT a.codigo FROM TbTipoProduto a WHERE a.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }


}
