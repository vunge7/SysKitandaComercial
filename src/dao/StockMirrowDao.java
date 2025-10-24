/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbStockMirrowJpaController;
import entity.TbProduto;
import entity.TbStockMirrow;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
public class StockMirrowDao extends TbStockMirrowJpaController
{

    public StockMirrowDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public boolean exist_produto_stock( int codigo, int idArmazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT s FROM TbStockMirrow s WHERE  s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :idArmazem")
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow s WHERE  s.codProdutoCodigo.codigo = :codigo AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo", codigo );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public List<TbStockMirrow> getAll()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbStockMirrow  c ORDER BY c.codProdutoCodigo.designacao ASC" );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public boolean exist_cod_barra_produto( long codBarra )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM Produto p WHERE  p.codBarra = :codBarra" )
                .setParameter( "codBarra", codBarra );

        List<TbProduto> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public TbStockMirrow getTbStockMirrowByCodBarra( long codigo_barra, int idAmazem )
    {
        System.out.println( "COD BARRA " + codigo_barra );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow  s WHERE s.codProdutoCodigo.codBarra = :codigo_barra AND s.codArmazem.codigo = :idAmazem" )
                .setParameter( "codigo_barra", codigo_barra )
                .setParameter( "idAmazem", idAmazem );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStockMirrow( 0 );
    }

    public TbStockMirrow getTbStockMirrowByCodManual( String codigo_manual, int idAmazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow  s WHERE s.codProdutoCodigo.codigoManual = :codigo_manual AND s.codArmazem.codigo = :idAmazem" )
                .setParameter( "codigo_manual", codigo_manual )
                .setParameter( "idAmazem", idAmazem );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStockMirrow( 0 );
    }

    public boolean exist_produto_stock( long codBarra )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbStockMirrow p WHERE  p.codProdutoCodigo.codBarra = :codBarra" )
                .setParameter( "codBarra", codBarra );

        List<TbProduto> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public TbStockMirrow get_stock_by_id_produto_and_id_armazem( int codigo_produto, int idArmazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow s WHERE  s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo_produto", codigo_produto );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        // TbStockMirrow stock = new TbStockMirrow(0);
        return new TbStockMirrow( 0 );

    }

    public void repor_qtd( double qtd, TbStockMirrow stock )
    {
        try
        {
            double qtd_actualizar = stock.getQuantidadeExistente() + qtd;
            stock.setQuantidadeExistente(  stock.getQuantidadeExistente() +  qtd );
            edit( stock );
        }
        catch ( Exception e )
        {
        }

    }

    public void retirar_qtd( double qtd, TbStockMirrow stock )
    {
        try
        {

            TbStockMirrow stock_local = findTbStockMirrow( stock.getCodigo() );

            double qtd_actualizar = stock_local.getQuantidadeExistente() - qtd;
            stock_local.setQuantidadeExistente( qtd_actualizar );
            System.out.println( "QUANTIDADE ACTUAL" + stock_local.getQuantidadeExistente() );
            edit( stock_local );

        }
        catch ( Exception e )
        {
        }

    }

    public Vector<TbStockMirrow> getProdutosByTipo( String categoria )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbStockMirrow  p WHERE p.codProdutoCodigo.codTipoProduto.designacao = :categoria  AND p.codProdutoCodigo.stocavel = 'true'  ORDER BY p.codProdutoCodigo.designacao" )
                .setParameter( "categoria", categoria );

        Vector<TbStockMirrow> result = (Vector) query.getResultList();
        em.close();

        TbStockMirrow stock = new TbStockMirrow();
        stock.setCodigo( 0 );
        if ( result != null )
        {
            return result;
        }

        return null;
    }

    public TbStockMirrow getTbStockMirrowByDescricao( int codigo_produto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow  s WHERE s.codProdutoCodigo.codigo = :codigo_produto" )
                .setParameter( "codigo_produto", codigo_produto );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbStockMirrow getTbStockMirrowByDescricao( int codigo_produto, int codigo_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow  s WHERE s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :codigo_armazem" )
                .setParameter( "codigo_produto", codigo_produto )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public List<TbProduto> getAllProdutosByIdArmazem( int codigo_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT s.codProdutoCodigo FROM TbStockMirrow  s WHERE s.codArmazem.codigo = :codigo_armazem" )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<TbStockMirrow> getProdutosPorIdArmazem( int codigo_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStockMirrow  s WHERE s.codArmazem.codigo = :codigo_armazem ORDER BY s.codProdutoCodigo.designacao ASC" )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbStockMirrow> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

}
