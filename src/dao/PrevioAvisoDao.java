/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.PedidoFeriaJpaController;
import controlador.PrevioAvisoJpaController;
import entity.PedidoFeria;
import entity.PrevioAviso;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PrevioAvisoDao extends PrevioAvisoJpaController
{

    public PrevioAvisoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<PrevioAviso> getAllFaltaByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  PrevioAviso a  WHERE a.fkFuncionario.idFuncionario = :idFuncionario ORDER BY a.pkPedidoFeria DESC" )
                .setParameter( "idFuncionario", idFuncionario );

        List<PrevioAviso> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public Long getDiasUteisFeriasGozadosByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT SUM(a.diasFerias) AS TOTAL FROM  PedidoFeria a  WHERE a.fkFuncionario.idFuncionario = :idFuncionario GROUP BY a.fkFuncionario.idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List<Long> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0l;

    }

    public int getLastPedidoByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkPedidoFeria)  FROM PedidoFeria  v WHERE v.fkFuncionario.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            try
            {
                return result.get( 0 );
            }
            catch ( Exception e )
            {
            }
        }

        return 0;

    }
    
    public int getUltimoPrevioAviso()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT MAX(p.pkPrevioAviso) FROM PrevioAviso p");
      
        List<Integer> result = query.getResultList();
         em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }

//    public Date getLastDataFimPedidoByFuncionario( int idFuncionario )
//    {
//        PedidoFeria pedidoFeria = findPedidoFeria( getLastPedidoByIdFuncionario( idFuncionario ) );
//        Date dataFim = null;
//        try
//        {
//            dataFim = pedidoFeria.getDataFim();
//        }
//        catch ( Exception e )
//        {
//        }
//        return dataFim;
//    }

}
