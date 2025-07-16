/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

//import controlador.StatusJpaController;
import controlador.TbStatusJpaController;
import entity.TbStatus;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.DVML.SELECCIONE;

/**
 *
 * @author Domingos Dala Vunge
 */
public class StatusDao extends TbStatusJpaController
{

    public StatusDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbStatus> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.descrisao FROM TbStatus c WHERE c.idStatus <> 5" );
        return query.getResultList();
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descrisao FROM TbStatus s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public String getDescricaoByIdStatus( int idStatus )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.descrisao FROM TbStatus c WHERE c.idStatus = :idStatus" )
                .setParameter( "idStatus", idStatus );

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
        Query query = em.createQuery( "SELECT c.idStatus FROM TbStatus c WHERE c.descrisao = :decricao" )
                .setParameter( "decricao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }
}
