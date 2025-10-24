/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ProdutoImpostoJpaController;
import entity.*;
import java.util.List;
import javax.persistence.*;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProdutoImpostoDao extends ProdutoImpostoJpaController
{

    public ProdutoImpostoDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public double getTaxaByIdProduto ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s.fkImposto.taxa FROM ProdutoImposto s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        List<Double> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return 0;
    }
    
    public double getTaxaByCodBarraProduto ( String codBarra )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s.fkImposto.taxa FROM ProdutoImposto s WHERE s.fkProduto.codBarra = :codBarra" )
                .setParameter ( "codBarra", codBarra );
        List<Double> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return 0;
    }
    
    public double getTaxaByCodInternoProduto ( int codigo )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s.fkImposto.taxa FROM ProdutoImposto s WHERE s.fkProduto.codigo = :codigo" )
                .setParameter ( "codigo", codigo );
        List<Double> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return 0;
    }

    public boolean exist ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s FROM ProdutoImposto s WHERE s.fkProduto.codigo = :pkProduto" )
                .setParameter ( "pkProduto", pkProduto );
        return  ! query.getResultList ().isEmpty ();
    }
    public boolean exist1 ( String codBarra )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT s FROM ProdutoImposto s WHERE s.fkProduto.codBarra = :codBarra" )
                .setParameter ( "codBarra", codBarra );
        return  ! query.getResultList ().isEmpty ();
    }
    


    public boolean remove ( int pkProduto )
    {
        EntityManager em = getEntityManager ();
        EntityTransaction transaction = em.getTransaction ();

        transaction.begin ();
        {
            Query query = em.createQuery ( "DELETE FROM ProdutoImposto s WHERE s.fkProduto.codigo = :pkProduto" )
                    .setParameter ( "pkProduto", pkProduto );
            int result = query.executeUpdate ();

            System.err.println ( "@APAGAR IMPOSTO : " + result );
            System.err.println ( "pkProduto: " + pkProduto );

        }
        transaction.commit ();
        
        return true;
    }
    
    

    public void aplicarIva ( int fkProduto, int fkImposto )
    {
        System.err.println ( "@Aplicar Iva: " + fkProduto );

        if (  ! exist ( fkProduto ) )
        {
            ProdutoImposto produtoImposto = new ProdutoImposto ();
            produtoImposto.setFkProduto ( new TbProduto ( fkProduto ) );
            produtoImposto.setFkImposto ( new Imposto ( fkImposto ) );

            //APLICAR ISENCAO
            create ( produtoImposto );

            //NÃO APLICAR IVA
        }
        new ProdutoImpostoDao(em ).remove ( fkProduto );
    }
   
    public void aplicarIvaAct ( int fkProduto, int fkImposto )
    {
        System.err.println ( "Aplicar Iva: " + fkProduto );

//        if (  ! exist ( fkProduto ) )
//        {
            ProdutoImposto produtoImposto = new ProdutoImposto ();
            produtoImposto.setFkProduto ( new TbProduto ( fkProduto ) );
            produtoImposto.setFkImposto ( new Imposto ( fkImposto ) );

            //APLICAR ISENCAO
            try
            {
                edit(produtoImposto );
                
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            //NÃO APLICAR IVA
//        }
//        new ProdutoIsentoDao ( em ).remove ( fkProduto );
    }

}
