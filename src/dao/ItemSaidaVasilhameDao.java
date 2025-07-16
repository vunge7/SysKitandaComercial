/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import controlador.TbItemSaidaVasilhameJpaController;
import entity.TbItemSaidaVasilhame;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class ItemSaidaVasilhameDao extends TbItemSaidaVasilhameJpaController{

    public ItemSaidaVasilhameDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    
     public List<TbItemSaidaVasilhame> getAllSaidaVasilhameByIdArmazem(int idSaidaVasilhame)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbItemSaidaVasilhame i WHERE  i.fkSaidaVazilhame.pkSaidaVasilhame = :idSaidaVasilhame")
                .setParameter("idSaidaVasilhame", idSaidaVasilhame);
        
        List<TbItemSaidaVasilhame> result = query.getResultList();
        em.close();
        if( !result.isEmpty() )
                return result;
        return null;
    }
}
