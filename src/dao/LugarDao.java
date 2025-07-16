/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import controlador.TbLugaresJpaController;
import entity.TbLugares;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Dallas
 */
public class LugarDao extends TbLugaresJpaController
{

    public LugarDao(  )
    {
        this( em );
    }

    public LugarDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbLugares> buscaTodosLugares()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbLugares m" );
        return query.getResultList();
    }

    public boolean exist_descricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbLugares c WHERE c.designacao = :designacao " )
                .setParameter( "designacao", designacao );

        List<TbLugares> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }

        return false;

    }

    public List<TbLugares> getModelosByNome( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbLugares c WHERE c.designacao LIKE :designacao" )
                .setParameter( "designacao", designacao + "%" );

        List<TbLugares> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public Vector<String> getAllLugares()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m.designacao FROM TbLugares m  ORDER BY m.designacao" );

        List<String> result = query.getResultList();
        result.add( 0, "--SELECIONE--" );

        if ( !result.isEmpty() )
        {
            return ( Vector ) result;
        }
        return null;

    }
//         public Vector<String> getConsumoByDescricao(String descricao)
//    {
//          EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT tc FROM Consumo tc WHERE tc.descricao = :descricao")
//             .setParameter("descricao", descricao);      
//        List<String> result = query.getResultList();
//
//        result.add(0, "--SELECIONE--");
//   
//        if (!result.isEmpty()) {
//          return (Vector)result;
//        }
//        return null;
//
//    }

    public TbLugares getLugaresByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT tc FROM TbLugares tc WHERE m.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<TbLugares> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;

    }

    public Vector<String> getAllLugaresByMarcas( int pkMesas )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m.designacao FROM TbLugares m  WHERE m.fkMesas.pkMesas = :pkMesas  ORDER BY m.designacao" )
                .setParameter( "pkMesas", pkMesas );

        List<String> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return ( Vector ) result;
        }
        return null;

    }

    public int getIdByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbLugares m WHERE m.designacao = :designacao " )
                .setParameter( "designacao", designacao );

        List<TbLugares> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getPkLugares();
        }

        return 0;

    }

    public List<TbLugares> getAllModeloByMarca( int pkMesas )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbLugares s WHERE s.fkMesas.pkMesas = :pkMesas ORDER BY s.designacao ASC" )
                .setParameter( "pkMesas", pkMesas );
        List<TbLugares> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;
        }
        return null;

    }

    public List<TbLugares> getAllLugarByMesa( int pkMesas )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbLugares s WHERE s.fkMesas.pkMesas = :pkMesas ORDER BY s.designacao ASC" )
                .setParameter( "pkMesas", pkMesas );
        List<TbLugares> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;
        }
        return null;

    }

}
