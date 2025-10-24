/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ServicoRetencaoJpaController;
import entity.*;
import java.util.List;
import javax.persistence.*;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ServicoRetencaoDao extends ServicoRetencaoJpaController
{

    public ServicoRetencaoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public double getTaxaByIdProduto( int pkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.fkRetencao.taxa FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter( "pkProduto", pkProduto );
        List<Double> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;
    }

    public boolean exist_retencao( int pkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto AND s.fkRetencao.taxa <> 0" )
                .setParameter( "pkProduto", pkProduto );
        return !query.getResultList().isEmpty();
    }

    public double getUltimaTaxaByIdProduto( int pkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.fkRetencao.taxa FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto ORDER BY s.fkRetencao.pkRetencao DESC" )
                .setParameter( "pkProduto", pkProduto );
        List<Double> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;
    }

    public boolean exist( int pkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter( "pkProduto", pkProduto );
        return !query.getResultList().isEmpty();
    }

    public boolean remove( int pkProduto )
    {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        {
            Query query = em.createQuery( "DELETE FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto" )
                    .setParameter( "pkProduto", pkProduto );
            int result = query.executeUpdate();

            System.err.println( "APLICAR IMPOSTO : " + result );
            System.err.println( "pkProduto: " + pkProduto );

        }
        transaction.commit();

        return true;
    }

    public String getServicoRetencaoByIdProduto( int pkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.fkRetencao.taxa FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter( "pkProduto", pkProduto );
        List<String> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            //return "IVA-Regime de Não Sujeição";
            return result.get( 0 );
        }
        return "";

    }

    public void aplicarRetencao( int fkProduto, int fkRetencao )
    {
        System.err.println( "Aplicar Retencao: " + fkProduto );

        remove( fkProduto );

        if ( !exist_retencao( fkProduto ) )
        {
            ServicoRetencao servico_retencao = new ServicoRetencao();
            servico_retencao.setFkProduto( new TbProduto( fkProduto ) );
            servico_retencao.setFkRetencao( new Retencao( fkRetencao ) );

            //APLICAR RETENCAO
            create( servico_retencao );

            //NÃO APLICAR RETENCAO
        }

    }

}
