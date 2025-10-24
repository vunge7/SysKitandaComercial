/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ItemSalarioExtraJpaController;
import entity.ItemSalarioExtra;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ItemSalarioExtraDao extends ItemSalarioExtraJpaController
{

    public ItemSalarioExtraDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<ItemSalarioExtra> getAllItemSalarioExtraByAnoAndMesAndIdFuncionario( int ano, int idMes, int fkMasterTable, int idFuncionario )
    {
        System.out.println( "ID FUNCIONÁRIO: " + idFuncionario );
        EntityManager em = getEntityManager();
        String sql = " SELECT item.*"
                + " FROM item_salario_extra item, master_table m "
                + " WHERE YEAR(data_hora) = ? "
                + " AND MONTH(data_hora)  = ? "
                + " AND fk_tb_funcionario = ? "
                + " AND m.fk_master_table = ? "
                + " AND item.fk_master_table = m.pk_master_table"
                + " ORDER BY m.designcao ASC";

        Query query = em.createNativeQuery( sql, ItemSalarioExtra.class );
        query.setParameter( 1, ano );
        query.setParameter( 2, idMes );
        query.setParameter( 3, idFuncionario );
        query.setParameter( 4, fkMasterTable );

        // System.out.println( sql );
        List<ItemSalarioExtra> list = query.getResultList();

        return list;

    }

    public static List<ItemSalarioExtra> getAllByData( Date data_abertura, Date data_fecho, int fkMasterTable, int idFuncionario , EntityManager em)
    {
        System.out.println( "ID FUNCIONÁRIO: " + idFuncionario );
//        EntityManager em = getEntityManager();
        String sql = " SELECT item.*"
                + " FROM item_salario_extra item, master_table m "
                + " WHERE DATE(data_hora) BETWEEN ? AND ? "
                + " AND fk_tb_funcionario = ? "
                + " AND m.fk_master_table = ? "
                + " AND item.fk_master_table = m.pk_master_table"
                + " ORDER BY m.designcao ASC";

        Query query = em.createNativeQuery( sql, ItemSalarioExtra.class );
        query.setParameter( 1, data_abertura );
        query.setParameter( 2, data_fecho );
        query.setParameter( 3, idFuncionario );
        query.setParameter( 4, fkMasterTable );

        // System.out.println( sql );
        List<ItemSalarioExtra> list = query.getResultList();

        return list;

    }

    public boolean existeItemSalarioExtraByAnoAndMesAndIdFuncionario( int ano, int idMes, int fkMasterTable, int idFuncionario )
    {
        System.out.println( "ID FUNCIONÁRIO: " + idFuncionario );
        EntityManager em = getEntityManager();
        String sql = " SELECT item.*"
                + " FROM item_salario_extra item, master_table m "
                + " WHERE YEAR(data_hora) = ? "
                + " AND MONTH(data_hora)  = ? "
                + " AND fk_tb_funcionario = ? "
                + " AND m.fk_master_table = ? "
                + " AND item.fk_master_table = m.pk_master_table"
                + " ORDER BY m.designcao ASC";

        Query query = em.createNativeQuery( sql, ItemSalarioExtra.class );
        query.setParameter( 1, ano );
        query.setParameter( 2, idMes );
        query.setParameter( 3, idFuncionario );
        query.setParameter( 4, fkMasterTable );

        return !query.getResultList().isEmpty();

    }

}
