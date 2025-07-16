/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbProdutoJpaController;

import entity.TbProduto;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Dallas
 */
public class ProdutoDao extends TbProdutoJpaController
{

    public ProdutoDao()
    {
        this( em );
    }

    public ProdutoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public TbProduto getProdutoByDescricao( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.designacao = :nome" )
                .setParameter( "nome", nome );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public Vector<String> buscaTodos()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbProduto p " );
        Vector<String> result = ( Vector ) query.getResultList();
        return result;
    }

    public List<TbProduto> getProdutosExistentes()
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
        Query query = em.createQuery( "SELECT s FROM TbProduto s WHERE s.status = 'Activo' ORDER BY s.designacao ASC" );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public static TbProduto getProdutoByCodigoBarra( String codigo_barra )
    {

        Query query = UtilDao.getEntityManager1().createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra" )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public int getUltimaCodProduto()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.codigo) FROM TbProduto p" );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public static TbProduto findProdutoByCodigoBarra( String codigo_barra )
    {

        Query query = UtilDao.getEntityManager1().createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra" )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public static TbProduto findProdutoByCodigoBarraExcepto( int codigo, String codigo_barra )
    {

        Query query = UtilDao.getEntityManager1().createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra AND p.codigo = :codigo" )
                .setParameter( "codigo_barra", codigo_barra )
                .setParameter( "codigo", codigo );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    //existe um codigo de barra do produto que nao seja ele mesmo.
    public boolean existProdutoByCodigoBarraExcepto( int codigo, String codigo_barra )
    {

        Query query = getEntityManager().createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra AND p.codigo <> :codigo" )
                .setParameter( "codigo_barra", codigo_barra )
                .setParameter( "codigo", codigo );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public boolean existProdutoByCodigoBarra( String codigo_barra )
    {

        Query query = getEntityManager().createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra " )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public boolean existProdutoByDesignacaoExcepto( int codigo, String designacao )
    {

        Query query = getEntityManager().createQuery( "SELECT p FROM TbProduto  p WHERE p.designacao = :designacao AND p.codigo <> :codigo" )
                .setParameter( "designacao", designacao )
                .setParameter( "codigo", codigo );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public static TbProduto findProdutoByDesignacao( String designacao )
    {

        Query query = UtilDao.getEntityManager1().createQuery( "SELECT p FROM TbProduto  p WHERE p.designacao = :DESIGNACAO" )
                .setParameter( "DESIGNACAO", designacao );

        List<TbProduto> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbProduto getProdutoByCodigoProduto( Integer cod_produto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codigo = :cod_produto AND p.status = 'Activo'" )
                .setParameter( "cod_produto", cod_produto );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public TbProduto getProdutoDesctivoByCodigoProduto( Integer cod_produto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codigo = :cod_produto AND p.status = 'Desactivo'" )
                .setParameter( "cod_produto", cod_produto );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public TbProduto getProdutoByCodigoMnual( String codigo_manual )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codigoManual = :codigo_manual" )
                .setParameter( "codigo_manual", codigo_manual );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public boolean exist_designacao_produto( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.designacao = :nome" )
                .setParameter( "nome", nome );
        List<TbProduto> result = query.getResultList();
        em.close();
        return !result.isEmpty();

    }

    public Vector<String> getProdutoByCategoria( String categoria )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbProduto  p WHERE p.codTipoProduto.designacao = :categoria" )
                .setParameter( "categoria", categoria );
        Vector<String> result = ( Vector ) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public Integer getUltimoProduto()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.codigo) FROM TbProduto m" );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public Vector<TbProduto> getServicosByTipo( String categoria )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codTipoProduto.designacao = :categoria AND p.stocavel = 'false'" )
                .setParameter( "categoria", categoria );

        Vector<TbProduto> result = ( Vector ) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public Vector<TbProduto> getServicosByTipos( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codTipoProduto.codigo = :codigo " )
                .setParameter( "codigo", codigo );

        Vector<TbProduto> result = ( Vector ) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public Vector<TbProduto> getProdutosByTipo( String categoria )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codTipoProduto.designacao = :categoria AND p.stocavel = 'true' AND p.status = 'Activo'" )
                .setParameter( "categoria", categoria );

        Vector<TbProduto> result = ( Vector ) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public Vector<String> getAllDesingnacaoProduto()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbProduto p ORDER BY p.designacao" );
        Vector<String> lista = ( Vector ) query.getResultList();
        return lista;

    }

    public List<TbProduto> buscaTodosProdutos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT tp FROM TbProduto tp" );
        return query.getResultList();
    }

    public List<TbProduto> buscaTodosProdutosComCampoVazios()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT tp FROM TbProduto tp WHERE tp.designacao = '' " );
        return query.getResultList();
    }

    public List<TbProduto> getAll()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto p ORDER BY p.designacao ASC" );
        Vector<TbProduto> lista = ( Vector ) query.getResultList();
        return lista;

    }

    public boolean exist_produto( long codBarra )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto p WHERE p.codBarra = :codBarra" )
                .setParameter( "codBarra", codBarra );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public TbProduto getProdutoByCodigosManual( String codigo_manual )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codigoManual = :codigo_manual" )
                .setParameter( "codigo_manual", codigo_manual );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbProduto produto = new TbProduto( 0 );
        return produto;
    }

    public List<TbProduto> getAllByVenda( String codigoFactura )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT p.codigoProduto FROM TbItemVenda  p WHERE p.codigoVenda.codFact = :CODIGO_FACTURA" )
                .setParameter( "CODIGO_FACTURA", codigoFactura );
        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public List<TbProduto> getAllMedicosByEspecialidade()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbProduto s" );
        List<TbProduto> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;
        }
        return null;

    }

    public List<TbProduto> getProdutos()
    {
        EntityManager em = getEntityManager();
//        Date data_actual = new Date();
        Query query = em.createQuery( "SELECT s FROM TbProduto s" );

        List<TbProduto> list = query.getResultList();

        if ( !list.isEmpty() )
        {
            return list;
        }
        list.add( new TbProduto( 0 ) );
        return list;

    }

    public int getIdByDescricao( String designacao )
    {
        System.out.println( "DESIGNACAO: " +designacao );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.codigo FROM TbProduto s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( result != null )
        {

            try
            {
                 return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
           
        }
        return 0;

    }

    public String getNomeByIdFuncionario( long idFuncionario )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.nome FROM TbFuncionario s WHERE s.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public String getDescricaoById( int codigo )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM TbProduto s WHERE s.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List result = query.getResultList();

        if ( result != null )
        {

            return String.valueOf( result.get( 0 ) );
        }
        return "";

    }

    public List<TbProduto> getDescricaoByIdTipoProduto( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbProduto a WHERE a.codTipoProduto.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        //List list = query.getResultList();
        return query.getResultList();
    }

    public List<TbProduto> getAllProdutosExpirados()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto p WHERE p.dataExpiracao < :dataActual " )
                .setParameter( "dataActual", new Date() );
        return query.getResultList();

    }

    public boolean produtoExpirado( int codProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto p WHERE p.dataExpiracao < :dataActual  AND p.codigo = :codProduto" )
                .setParameter( "dataActual", new Date() )
                .setParameter( "codProduto", codProduto );
        List<TbProduto> list = query.getResultList();

        return !list.isEmpty();

    }

    public TbProduto getStockByCodBarra( String codigo_barra )
    {
        System.out.println( "COD BARRA " + codigo_barra );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codBarra = :codigo_barra AND p.status = 'Activo'" )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbProduto getStockByCodigoManual( String codigo_manual )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbProduto  p WHERE p.codigoManual = :codigo_manual" )
                .setParameter( "codigo_manual", codigo_manual );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbProduto getStockByCodBarra1( String codigo_barra )
    {
        System.out.println( "COD BARRA " + codigo_barra );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbProduto  s WHERE s.codProdutoCodigo.codBarra = :codigo_barra" )
                .setParameter( "codigo_barra", codigo_barra );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbProduto getStockByCodInterno1( int codigo )
    {
        System.out.println( "COD BARRA " + codigo );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbProduto  s WHERE s.codProdutoCodigo.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

}
