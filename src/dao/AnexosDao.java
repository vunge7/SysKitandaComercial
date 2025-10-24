/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.AnexosJpaController;
import entity.Anexos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AnexosDao extends AnexosJpaController
{

    public AnexosDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<String> getAnexoByIdFuncionario( int idFuncionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nomeFicheiro FROM Anexos s WHERE s.fkFuncionario.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List<String> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list;
        }
        return null;
    }
    public Anexos getAnexoByIdFuncionarioAndNome( int idFuncionario, String nomeFicheiro )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM Anexos s WHERE s.fkFuncionario.idFuncionario = :idFuncionario AND s.nomeFicheiro = :nomeFicheiro" )
                .setParameter( "idFuncionario", idFuncionario )
                .setParameter( "nomeFicheiro", nomeFicheiro );

        List<Anexos> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return null;
    }

    

    public int getLastId()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(s.idAno) FROM Ano s" );
        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;
    }

}
