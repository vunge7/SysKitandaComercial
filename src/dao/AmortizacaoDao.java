/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.AmortizacaoJpaController;
import entity.Amortizacao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AmortizacaoDao extends AmortizacaoJpaController
{

    public AmortizacaoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public void alterar_status_amortizacao( int cod_amortizacao, String status )
    {
        try
        {
            Amortizacao amortizacao = findAmortizacao( cod_amortizacao );
            edit( amortizacao );
        }
        catch ( Exception e )
        {
            System.out.println( "Falha ao alterar o status da amortizacao" );
        }

    }

    public List<Amortizacao> getAllAmortizacaoByFactura( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Amortizacao v WHERE v.fkVenda.codFact = :codFact" )
                .setParameter( "codFact", codFact );

        List<Amortizacao> result = query.getResultList();

        return result;

    }
//    public List<Amortizacao> getAllAmortizacaoByFactura( int pk_factura )
//    {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT v FROM Amortizacao v WHERE v.fkVenda.codigo = :pk_factura" )
//                .setParameter( "pk_factura", pk_factura );
//
//        List<Amortizacao> result = query.getResultList();
//
//        return result;
//
//    }

}
