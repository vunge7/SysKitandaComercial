/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbSubsidiosJpaController;
import entity.TbSubsidios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.DVML.*;


/**
 *
 * @author Domingos Dala Vunge
 */
public class SubsidioDao extends TbSubsidiosJpaController
{

    public SubsidioDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbSubsidios> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descricao FROM TbSubsidios s" );
        return query.getResultList();
    }
    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descricao FROM TbSubsidios s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public String getDescricaoByIdSubsidios( long idSubsidios )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.descricao FROM TbSubsidios s WHERE s.idSubsidios = :idSubsidios" )
                .setParameter( "idSubsidios", idSubsidios );

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
        Query query = em.createQuery( "SELECT s.idSubsidios FROM TbSubsidios s WHERE s.descricao = :decricao" )
                .setParameter( "decricao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }
}
