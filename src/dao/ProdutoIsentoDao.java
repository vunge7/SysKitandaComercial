/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ProdutoIsentoJpaController;
import entity.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProdutoIsentoDao extends ProdutoIsentoJpaController
{

    public ProdutoIsentoDao ( )
    {
        super ( em );
    }

    public ProdutoIsentoDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public String getRegimeIsensaoByIdProduto ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s.fkProdutosMotivosIsensao.regime FROM ProdutoIsento s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        List<String> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            //return "IVA-Regime de Não Sujeição";
            return result.get ( 0 );
        }
        return "";

    }

    public ProdutoIsento findByIdProduto ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s FROM ProdutoIsento s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        List<ProdutoIsento> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            //return "IVA-Regime de Não Sujeição";
            return result.get ( 0 );
        }

        return null;
    }

    public String getCodigoRegimeByIdProduto ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s.fkProdutosMotivosIsensao.codigoRegime FROM ProdutoIsento s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        List<String> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        return "";

    }

    public boolean exist ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s FROM ProdutoIsento s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        return  ! query.getResultList ().isEmpty ();
    }

    public void aplicarIsencao ( int fkProduto, int fkProdutoMotivoIsensao )
    {
        ProdutoIsento produtoIsento = new ProdutoIsento ();
        produtoIsento.setFkProduto ( new TbProduto ( fkProduto ) );
        produtoIsento.setFkProdutosMotivosIsensao ( new ProdutosMotivosIsensao ( fkProdutoMotivoIsensao ) );

        if (  ! exist ( fkProduto ) )
        {

            //APLICAR ISENCAO
            create ( produtoIsento );

            //NÃO APLICAR IVA
        }
        else
        {
            ProdutoIsento findByIdProduto = findByIdProduto ( fkProduto );
            produtoIsento.setPkProdutoIsento ( findByIdProduto.getPkProdutoIsento () );
            
            try
            {
                edit ( produtoIsento );
            }
            catch ( Exception ex )
            {
                Logger.getLogger ( ProdutoIsentoDao.class.getName() ).log ( Level.SEVERE, null, ex );
            }
        }

        new ProdutoImpostoDao ( em ).remove ( fkProduto );
    }

    public boolean remove ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        EntityTransaction transaction = em.getTransaction ();

        transaction.begin ();
        {
            Query query = em.createQuery ( "DELETE FROM ProdutoIsento s WHERE s.fkProduto.codigo = :pkProduto" )
                    .setParameter ( "pkProduto", pkProduto );
            int result = query.executeUpdate ();

            System.err.println ( "REMOVER ISENÇÃO: " + result );
            System.err.println ( "pkProduto: " + pkProduto );
        }
        transaction.commit ();

        return true;
    }

}
