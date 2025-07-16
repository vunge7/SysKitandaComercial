/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.DocumentoJpaController;
import java.util.Date;
//import controller.DocumentoJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DocumentoDao extends DocumentoJpaController
{

    public DocumentoDao()
    {
        super( em );
    }

    public DocumentoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

//     public List<String> buscaTodos()
//    {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s.designacao FROM Documento s" );
//        List<String> result = query.getResultList();
//        result.add( 0, "--Seleccione--" );
//        return result;
//    }
    public List<String> buscaTodos()
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento != 1 and s.pkDocumento != 2 and s.pkDocumento != 3  and s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7" );
        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7 and s.pkDocumento != 8 and s.pkDocumento != 9 and s.pkDocumento != 10 and s.pkDocumento != 11" );
        List<String> result = query.getResultList();
        result.add( 0, "--Seleccione--" );
        return result;
    }

    public List<String> buscaTodos6()
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento != 1 and s.pkDocumento != 2 and s.pkDocumento != 3  and s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7" );
        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento = 6" );
        List<String> result = query.getResultList();
//        result.add( 0, "--Seleccione--" );
        return result;
    }

    public List<String> buscaTodosSolicitacoesCompras()
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento != 1 and s.pkDocumento != 2 and s.pkDocumento != 3  and s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7" );
        Query query = em.createQuery( "SELECT s.designacao FROM Documento s  where s.pkDocumento = 8" );
        List<String> result = query.getResultList();
        result.add( 0, "--Seleccione--" );
        return result;
    }

    public List<String> buscaCreditoDebito()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM Documento s where s.pkDocumento = 4 or  s.pkDocumento = 5" );
        List<String> result = query.getResultList();
        result.add( 0, "--Seleccione--" );
        return result;
    }

    public String getDescricaoByIdDocumento( int pkDocumento )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM Documento s WHERE s.pkDocumento = :pkDocumento" )
                .setParameter( "pkDocumento", pkDocumento );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkDocumento FROM Documento s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );
        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public Date getMAxDataDoc()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(s.ultimaData) FROM Documento s" );
        List<Date> list = query.getResultList();

        if ( list != null )
        {
            return list.get( 0 );
        }
        return null;

    }

    public static void startTransaction( BDConexao conexao )
    {
        conexao.executeUpdate( "SET autocommit = 0" );
        conexao.executeUpdate( "START TRANSACTION" );

    }

    public static void commitTransaction( BDConexao conexao )
    {

        conexao.executeUpdate( "COMMIT" );

    }

    public static void rollBackTransaction( BDConexao conexao )
    {

        conexao.executeUpdate( "ROLLBACK" );

    }

}
