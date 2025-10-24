/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbAnoJpaController;
//import controller.AnoJpaController;
import entity.TbAno;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AnoDao extends TbAnoJpaController
{

    public AnoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

//    public List<TbAno> buscaTodos()
//    {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s.descrisao FROM TbAno s" );
//        return query.getResultList();
//    }
    
    public List<TbAno> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descrisao FROM TbAno s ORDER BY s.idAno DESC" );
        return query.getResultList();
    }

    public String getDescricaoByIdAno( long idAno )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descrisao FROM TbAno s WHERE s.idAno = :idAno" )
                .setParameter( "idAno", idAno );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public int getIdByDescricao( String descricao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.idAno FROM TbAno s WHERE s.descrisao = :decricao" )
                .setParameter( "decricao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }
}
