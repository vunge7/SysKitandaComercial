/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.CaixaJpaController;
import entity.Caixa;
import java.sql.ResultSet;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class CaixaDao extends CaixaJpaController
{

    public CaixaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public int getLastCaixa()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkCaixa)  FROM Caixa  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public boolean existeCaixas()
    {

        return ( findCaixaEntities().size() ) > 0;
    }

    public int getCaixaAnteriror()
    {
        return getLastCaixa();
    }

    public boolean existe_abertura( Date date, BDConexao conexao )
    {

//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT o.pkCaixa  FROM Caixa  o WHERE o.dataAbertura = :data_abertura")                           
//                .setParameter("data_abertura", date);   
//        
//        List<Integer> result = query.getResultList();
//        em.close();       
        String query = "SELECT * FROM caixa WHERE   DATE(data_abertura) =   '" + MetodosUtil.getDataBanco( date ) + "'";

        System.out.println( query );
        ResultSet executeQuery = conexao.executeQuery( query );

        try
        {
            return executeQuery.next();
        }
        catch ( Exception e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public boolean existe_fecho( Date date, BDConexao conexao )
    {

//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT o.pkCaixa  FROM Caixa  o WHERE o.dataAbertura = :data_abertura")                           
//                .setParameter("data_abertura", date);   
//        
//        List<Integer> result = query.getResultList();
//        em.close();       
        String query = "SELECT * FROM caixa WHERE   DATE(data_fecho) =   '" + MetodosUtil.getDataBanco( date ) + "'";

        System.out.println( query );
        ResultSet executeQuery = conexao.executeQuery( query );

        try
        {
            return executeQuery.next();
        }
        catch ( Exception e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public boolean existe_fecho( Caixa caixa, BDConexao conexao )
    {

//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT o.pkCaixa  FROM Caixa  o WHERE o.dataAbertura = :data_abertura")                           
//                .setParameter("data_abertura", date);   
//        
//        List<Integer> result = query.getResultList();
//        em.close();       
        String query = "SELECT * FROM caixa WHERE   DATE(data_fecho) =   '" + MetodosUtil.getDataBancoFull( caixa.getDataFecho() ) + "'";

        System.out.println( query );
        ResultSet executeQuery = conexao.executeQuery( query );

        try
        {
            return executeQuery.next();
        }
        catch ( Exception e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public Caixa caixa_actual()
    {
        return findCaixa( getLastCaixa() );
    }

    public boolean existe_fecho()
    {
        try
        {
            Caixa caixa_actual = findCaixa( getLastCaixa() );
            return !Objects.isNull( caixa_actual.getDataFecho() );
        }
        catch ( Exception e )
        {
            return false;
        }

    }

    public boolean existe_abertura()
    {
        try
        {
            Caixa caixa_actual = findCaixa( getLastCaixa() );
            return !Objects.isNull( caixa_actual.getDataAbertura() );
        }
        catch ( Exception e )
        {
            return false;
        }

    }

}
