/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbItemEntradaVasilhameJpaController;
import entity.TbEntradaVasilhame;
import entity.TbItemEntradaVasilhame;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class ItemEntradaVasilhameDao extends TbItemEntradaVasilhameJpaController{

    public ItemEntradaVasilhameDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    
    public List<TbItemEntradaVasilhame> getAllEntradaVasilhameByIdArmazem(int idEntradaVasilhame)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbItemEntradaVasilhame i WHERE           i.fkEntradaVasilhame.pkEntradaVasilhame = :idEntradaVasilhame")
                .setParameter("idEntradaVasilhame", idEntradaVasilhame);
        
        List<TbItemEntradaVasilhame> result = query.getResultList();
        em.close();
        if( !result.isEmpty() )
                return result;
        return null;
    }
    
}
