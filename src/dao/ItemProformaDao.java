/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbItemProFormaJpaController;
import entity.TbItemProForma;
import entity.TbItemVenda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 * 
 * 
 */


public class ItemProformaDao extends TbItemProFormaJpaController
{

    public ItemProformaDao(EntityManagerFactory emf) {
        super(emf);
    }

    
    public List<TbItemProForma> getAllItemProformaByIdProforma(int id_proforma)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemProForma  v WHERE v.fkProForma.pkProForma = :id_proforma")
                .setParameter("id_proforma", id_proforma);
               
        List<TbItemProForma> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbItemProForma itemProForma = new TbItemProForma(0);
        result.add(itemProForma);        
        return result;
    }
    
  
    public boolean exist_proforma(int id_proforma)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbItemProForma  v WHERE v.fkProForma.pkProForma = :id_proforma")
                .setParameter("id_proforma", id_proforma);
               
        List<TbItemVenda> result = query.getResultList();
        em.close();
       
        return !result.isEmpty();
      
    }
    
    
   
}
