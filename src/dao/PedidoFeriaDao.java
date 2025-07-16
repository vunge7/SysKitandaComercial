/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.PedidoFeriaJpaController;
import entity.PedidoFeria;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PedidoFeriaDao extends PedidoFeriaJpaController
{

    public PedidoFeriaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<PedidoFeria> getAllPedidoFeriasByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  PedidoFeria a  WHERE a.fkFuncionario.idFuncionario = :idFuncionario ORDER BY a.pkPedidoFeria DESC" )
                .setParameter( "idFuncionario", idFuncionario );

        List<PedidoFeria> result = query.getResultList();
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

    public Date getLastDataFimPedidoByFuncionario( int idFuncionario )
    {
        PedidoFeria pedidoFeria = findPedidoFeria( getLastPedidoByIdFuncionario( idFuncionario ) );
        Date dataFim = null;
        try
        {
            dataFim = pedidoFeria.getDataFim();
        }
        catch ( Exception e )
        {
        }
        return dataFim;
    }

    public boolean existe_pedido_feria(int idFuncionario)
    {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery( "SELECT * FROM pedido_feria WHERE data_inicio BETWEEN ? AND ? AND fk_funcionario = ?", PedidoFeria.class )
                .setParameter( 1, "2020-01-01" ) .
                setParameter( 2, "2021-03-31" ).
                setParameter( 3, idFuncionario );
        
        return !query.getResultList().isEmpty();
        
    }

    
}
