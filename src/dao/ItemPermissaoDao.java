/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbItemPermissaoJpaController;
import entity.TbItemPermissao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */

public class ItemPermissaoDao extends TbItemPermissaoJpaController{
    
    public ItemPermissaoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public boolean deletar_items (int idUsuario, String modulo) {         
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT i FROM TbItemPermissao i WHERE i.idUsuario.codigo = :idUsuario AND i.modulo = :modulo")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("modulo", modulo);

            List<TbItemPermissao> result = query.getResultList();
            em.close();
            
            for (TbItemPermissao object : result) {
                            
                    try {
                         destroy(  object.getIdItemPermissao()  );
                    } catch (Exception e) {
                        return false;
                    }                       
            }           
           return true;
       
    }
    
    public List<TbItemPermissao> getAllPermissoesByIdUsuarioAndModulo (int idUsuario, String modulo) {         
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT i FROM TbItemPermissao i WHERE i.idUsuario.codigo = :idUsuario AND i.modulo = :modulo")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("modulo", modulo);

            List<TbItemPermissao> result = query.getResultList();
            em.close();
            
           if (!result.isEmpty() ) {
                   return result; 
           }
           
           return null;
       
    }
    
    
}
