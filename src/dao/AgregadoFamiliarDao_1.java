/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.AgregadoFamiliarJpaController;
import controlador.exceptions.NonexistentEntityException;
import entity.AgregadoFamiliar;
import entity.TbItemSubsidiosFuncionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AgregadoFamiliarDao_1 extends AgregadoFamiliarJpaController{

    public AgregadoFamiliarDao_1(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<AgregadoFamiliar> getAllAgregadosFamiliarByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM  AgregadoFamiliar i WHERE i.fkFuncionario.idFuncionario = :idFuncionario ORDER BY i.nomeFilho ASC" )
                .setParameter( "idFuncionario", idFuncionario );

        List<AgregadoFamiliar> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }
    
    public boolean removerAllAgregadoFamiliar( int idFuncionario )
    {

        List<AgregadoFamiliar> itens = getAllAgregadosFamiliarByIdFuncionario( idFuncionario );

        if ( itens != null )
        {
            for ( AgregadoFamiliar iten : itens )
            {
                try
                {
                    destroy( iten.getPkAgregadoFamiliar());
                }
                catch ( NonexistentEntityException e )
                {
                    return false;
                }
            }
        }

        return true;

    }
    
    

}
