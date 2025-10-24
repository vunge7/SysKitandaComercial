/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ModeloJpaController;
import entity.Modelo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ModeloDao extends ModeloJpaController
{

    public ModeloDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<Modelo> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM Modelo p" );
        return query.getResultList();
    }

    public List<Modelo> buscaTodosModelos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM Modelo p ORDER BY p.fkMarca.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkModelo )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM Modelo a WHERE a.pkModelo = :pkModelo" )
                .setParameter( "pkModelo", pkModelo );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public List<Modelo> getDescricaoByIdMarca( int pkMarca )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM Modelo a WHERE a.fkMarca.pkMarca = :pkMarca" )
                .setParameter( "pkMarca", pkMarca );

        //List list = query.getResultList();
        return query.getResultList();
    }

    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.pkModelo FROM Modelo a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( result != null )
        {
            System.err.println( "ID MARCA " + result.get( 0 ) );
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public boolean exist_modelo( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM Modelo s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public Modelo getModeloByDescricao( String descricao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM Modelo  a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List<Modelo> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;

    }

   
}
