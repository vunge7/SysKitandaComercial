/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.PagamentoSubsidioFeriaNatalJpaController;
import entity.PagamentoSubsidioFeriaNatal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PagamentoSubsidioFeriaNatalDao extends PagamentoSubsidioFeriaNatalJpaController
{

    public PagamentoSubsidioFeriaNatalDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<PagamentoSubsidioFeriaNatal> getAllPagamentoSubsidioFeriaNatalsByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  PagamentoSubsidioFeriaNatal a  WHERE a.fkFuncionario.idFuncionario = :idFuncionario ORDER BY a.pkPagamentoSubsidioFeriaNatal DESC" )
                .setParameter( "idFuncionario", idFuncionario );

        List<PagamentoSubsidioFeriaNatal> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public int getLastPedidoByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkPagamentoSubsidioFeriaNatal)  FROM PagamentoSubsidioFeriaNatal  v WHERE v.fkFuncionario.idFuncionario = :idFuncionario" )
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

    public boolean existe_pedido_feria( int idFuncionario, Date data_1, Date data_2 , String tipo_susbsidio)
    {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery( "SELECT * FROM pagamento_subsidio_feria_natal WHERE DATE(data_hora) BETWEEN ? AND ? AND fk_funcionario = ? AND tipo_subsideo = ?",
                PagamentoSubsidioFeriaNatal.class );

        query.setParameter( 1, data_1 );
        query.setParameter( 2, data_2 );
        query.setParameter( 3, idFuncionario );
        query.setParameter( 4, tipo_susbsidio );

        return !query.getResultList().isEmpty();

    }

}
