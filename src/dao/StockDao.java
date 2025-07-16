/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbStockJpaController;
import entity.TbProduto;
import entity.TbStock;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Toshiba
 */
public class StockDao extends TbStockJpaController
{

    public static List<TbStock> findByProdutoWhitDesignacaoLike( int pkArmazem, String designacao )
    {
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT s FROM TbStock  s WHERE s.codArmazem.codigo = :PK_ARMAZEM AND s.codProdutoCodigo.designacao LIKE :DESIGNACAO ORDER BY s.codProdutoCodigo.designacao ASC" );
        query.setParameter( "PK_ARMAZEM", pkArmazem );
        query.setParameter( "DESIGNACAO", "%" + designacao + "%" );

        List<TbStock> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public static List<TbStock> findAllStock()
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT s FROM TbStock  s ORDER BY s.codProdutoCodigo.designacao ASC" );

        List<TbStock> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public StockDao()
    {
        this( em );
    }

    public StockDao( EntityManagerFactory emf )
    {
        super( emf );
    }

//    public boolean exist_produto_stock(int codigo_produto, int idArmazem)
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :idArmazem")
//                .setParameter("idArmazem", idArmazem)
//                .setParameter("codigo_produto", codigo_produto);
//                
//        
//        List<TbStock> result = query.getResultList();
//        em.close();
//        
//        return !result.isEmpty();
//    
//    }
    public boolean exist_produto_stock( int codigo, int idArmazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :idArmazem")
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public List<TbStock> getAll()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbStock  c ORDER BY c.codProdutoCodigo.designacao ASC" );

        List<TbStock> result = query.getResultList();
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
        Query query = em.createQuery( "SELECT p FROM TbProduto p WHERE  p.codBarra = :codBarra" )
                .setParameter( "codBarra", codBarra );

        List<TbProduto> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public TbStock getStockByCodBarra( String codigo_barra, int codigo )
    {
        System.out.println( "COD BARRA " + codigo_barra );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codBarra = :codigo_barra AND s.codArmazem.codigo = :codigo" )
                .setParameter( "codigo_barra", codigo_barra )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }
    public TbStock getStockByCodBarraSemArmazem( String codigo_barra )
    {
        System.out.println( "COD BARRA " + codigo_barra );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codBarra = :codigo_barra" )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }
    
    public TbStock getStockByCodInterno( int codigo, int idAmazem )
    {
        System.out.println( "COD BARRA " + codigo );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codigo = :codigo AND s.codArmazem.codigo = :idAmazem AND s.codProdutoCodigo.stocavel = true" )
                .setParameter( "codigo", codigo )
                .setParameter( "idAmazem", idAmazem );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbStock getStockByCodManual( String codigo_manual, int idAmazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codigoManual = :codigo_manual AND s.codArmazem.codigo = :idAmazem" )
                .setParameter( "codigo_manual", codigo_manual )
                .setParameter( "idAmazem", idAmazem );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStock( 0 );
    }

    public boolean exist_produto_stock( long codBarra )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbStock p WHERE  p.codProdutoCodigo.codBarra = :codBarra" )
                .setParameter( "codBarra", codBarra );

        List<TbProduto> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public TbStock get_stock_by_id_produto_and_id_armazem( int codigo_produto, int idArmazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo_produto", codigo_produto );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        // TbStock stock = new TbStock(0);
        return null;

    }
    public TbStock get_stock_by_id_produto_and_sem_armazem( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        // TbStock stock = new TbStock(0);
        return null;

    }
    public TbStock get_stock_by_id_produto_1( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo AND s.codArmazem.codigo = 1" )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        // TbStock stock = new TbStock(0);
        return null;

    }
    
    public TbStock get_stock_by_id_produto_2( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codigo = :codigo AND s.codArmazem.codigo = 2" )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        // TbStock stock = new TbStock(0);
        return null;

    }

    public void repor_qtd( double qtd, TbStock stock )
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

    public void retirar_qtd( double qtd, TbStock stock )
    {
        try
        {

            TbStock stock_local = findTbStock( stock.getCodigo() );

            double qtd_actualizar = stock_local.getQuantidadeExistente() - qtd;
            stock_local.setQuantidadeExistente( qtd_actualizar );
            System.out.println( "QUANTIDADE ACTUAL" + stock_local.getQuantidadeExistente() );
            edit( stock_local );

        }
        catch ( Exception e )
        {
        }

    }

    public Vector<TbStock> getProdutosByTipo( String categoria )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbStock  p WHERE p.codProdutoCodigo.codTipoProduto.designacao = :categoria  AND p.codProdutoCodigo.stocavel = 'true'  ORDER BY p.codProdutoCodigo.designacao" )
                .setParameter( "categoria", categoria );

        Vector<TbStock> result = (Vector) query.getResultList();
        em.close();

        TbStock stock = new TbStock();
        stock.setCodigo( 0 );
        if ( result != null )
        {
            return result;
        }

        return null;
    }

    public TbStock getStockByDescricao( int codigo_produto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codigo = :codigo_produto" )
                .setParameter( "codigo_produto", codigo_produto );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public static TbStock getStockByDescricao( int codigo_produto, int codigo_armazem )
    {

        Query query = UtilDao.getEntityManager1().createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codigo = :codigo_produto AND s.codArmazem.codigo = :codigo_armazem" )
                .setParameter( "codigo_produto", codigo_produto )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbStock> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public List<TbProduto> getAllProdutosByIdArmazem( int codigo_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem" )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

//    Query query = em.createQuery("SELECT c FROM TbStock  c ORDER BY c.codProdutoCodigo.designacao ASC");
    public List<TbStock> getProdutosPorIdArmazem( int codigo_armazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem AND s.codProdutoCodigo.status = 'Activo' ORDER BY s.codProdutoCodigo.designacao ASC" )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }
    
    public List<TbStock> getProdutosImprimirPrecos()
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = em.createQuery( "SELECT s FROM TbStock  s ORDER BY s.codProdutoCodigo.designacao ASC" );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<TbStock> getProdutosPorComprar()
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.quantCritica >= s.quantidadeExistente AND s.codProdutoCodigo.stocavel <> 'false' ORDER BY s.codProdutoCodigo.designacao ASC" );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<TbStock> getProdutosPorComprarByIdArmazem( int codigo_armazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem AND s.quantCritica >= s.quantidadeExistente ORDER BY s.codProdutoCodigo.designacao ASC" )
                .setParameter( "codigo_armazem", codigo_armazem );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public TbStock getStockByCodigoManual( String codigo_manual, int idAmazem )
    {
        System.out.println( "COD Manual " + codigo_manual );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbStock  s WHERE s.codProdutoCodigo.codigoManual = :codigo_manual AND s.codArmazem.codigo = :idAmazem" )
                .setParameter( "codigo_manual", codigo_manual )
                .setParameter( "idAmazem", idAmazem );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStock( 0 );
    }

    public List<TbStock> getStockLIKE_Nome( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbStock p WHERE p.codProdutoCodigo.designacao LIKE :designacao ORDER BY p.codProdutoCodigo.designacao ASC" )
                .setParameter( "designacao", "%" + designacao + "%" );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public TbStock getStockByCodigo( long codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbStock m WHERE m.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStock( 0 );

    }

    public TbStock getStocByCodigo( int codigo )
    {

        System.out.println( "Codigo" + codigo );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbStock m WHERE m.codProdutoCodigo.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<TbStock> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbStock( 0 );

    }

    public List<TbStock> get_all_stock_by_id_tipo_produto_and_id_armazem( int codigo_tipo_produto, int idArmazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s FROM TbStock s, TbProduto p WHERE  s.codProdutoCodigo.codTipoProduto.codigo = :codigo_tipo_produto AND s.codArmazem.codigo = :idArmazem || p.codTipoProduto.codigo = :codigo_tipo_produto " )
        Query query = em.createQuery( "SELECT s FROM TbStock s WHERE  s.codProdutoCodigo.codTipoProduto.codigo = :codigo_tipo_produto AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo_tipo_produto", codigo_tipo_produto );

        List<TbStock> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        // TbStock stock = new TbStock(0);
        return null;

    }
    public List<TbProduto> get_all_produtos_and_id_armazem( int codigo_tipo_produto, int idArmazem )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s FROM TbStock s, TbProduto p WHERE  s.codProdutoCodigo.codTipoProduto.codigo = :codigo_tipo_produto AND s.codArmazem.codigo = :idArmazem || p.codTipoProduto.codigo = :codigo_tipo_produto " )
        Query query = em.createQuery( "SELECT s.codProdutoCodigo FROM TbStock s WHERE  s.codProdutoCodigo.codTipoProduto.codigo = :codigo_tipo_produto AND s.codArmazem.codigo = :idArmazem" )
                .setParameter( "idArmazem", idArmazem )
                .setParameter( "codigo_tipo_produto", codigo_tipo_produto );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        // TbStock stock = new TbStock(0);
        return null;

    }

}
