/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.MasterTableJpaController;
//import controller.AnoJpaController;
import entity.MasterTable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MasterTableDao extends MasterTableJpaController
{

    public MasterTableDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<MasterTable> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM MasterTable s WHERE s.fkMasterTable.pkMasterTable = 0" );
        return query.getResultList();
    }

    public List<String> buscaTodosByIdMaster( int idMasterTable )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designcao FROM MasterTable s WHERE s.fkMasterTable.pkMasterTable = :idMasterTable" )
                .setParameter( "idMasterTable", idMasterTable );
        List<String> result = query.getResultList();
        result.add( 0, DVML.SELECCIONE );
        return result;
    }

    public int getIdByDescricao( String designcao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkMasterTable FROM MasterTable s WHERE s.designcao = :designcao" )
                .setParameter( "designcao", designcao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;
    }
}
