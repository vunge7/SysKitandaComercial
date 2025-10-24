/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ItemReciboRhJpaController;
import controlador.TbItemVendaJpaController;
import entity.ItemReciboRh;
import entity.TbItemVenda;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.RelatorioDiarioUtil;

/**
 *
 * @author Toshiba
 */
public class ItemReciboRhDao extends ItemReciboRhJpaController
{

    public ItemReciboRhDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }


    public List<ItemReciboRh> getAllItemReciboRhByIdReciboRh ( int pkReciboRh )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM ItemReciboRh  i WHERE i.fkReciboRh.pkReciboRh = :pkReciboRh" )
                .setParameter ( "pkReciboRh", pkReciboRh );

        List<ItemReciboRh> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        ItemReciboRh itemReciboRh = new ItemReciboRh ( 0 );
        result.add ( itemReciboRh );
        return result;
    }

    public List<ItemReciboRh> getAllRecibos ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM ItemReciboRh  i " );

        List<ItemReciboRh> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        ItemReciboRh itemReciboRh = new ItemReciboRh ( 0 );
        result.add ( itemReciboRh );
        return result;
    }



}
