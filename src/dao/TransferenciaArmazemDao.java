/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TransferenciaArmazemJpaController;
import entity.TransferenciaArmazem;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TransferenciaArmazemDao extends TransferenciaArmazemJpaController
{

    public TransferenciaArmazemDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TransferenciaArmazem> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TransferenciaArmazem.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TransferenciaArmazem> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public int getLastTransferencia()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkTransferenciaArmazem)  FROM TransferenciaArmazem  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public TransferenciaArmazem findLastRow()
    {
        return findTransferenciaArmazem( getLastTransferencia() );

    }

}
