/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ItemComprasJpaController;
import controlador.ItemComprasJpaController;
import entity.Compras;
import entity.ItemCompras;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.RelatorioDiarioUtil;

/**
 *
 * @author Toshiba
 */
public class ItemComprasDao extends ItemComprasJpaController
{

    public ItemComprasDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<ItemCompras> getAllItemVendasByIdVendaReciclagem( int id_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM ItemCompras  v WHERE v.codigoVenda.codigo = :id_venda" )
                .setParameter( "id_venda", id_venda );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );
        return result;
    }

    public List<ItemCompras> getAllItemVendasByIdVenda( int id_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM ItemCompras  v WHERE v.codigoVenda.codigo = :id_venda AND v.codigoVenda.statusEliminado = 'false'" )
                .setParameter( "id_venda", id_venda );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );
        return result;
    }

    public List<ItemCompras> getAllVenda()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM ItemCompras  v WHERE  v.codigoVenda.statusEliminado = 'false' AND v.codigo > 32898" );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );
        return result;
    }

    public List<TbProduto> getAllProdutosVendidos( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  v.codigoProduto AS TOTAL FROM ItemCompras  v WHERE v.codigoVenda.statusEliminado = 'false' AND v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda     BETWEEN :data_inicio AND :data_fim   AND  v.codigoVenda.idArmazemFK.codigo = :id_armazem  GROUP BY v.codigoProduto" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "id_armazem", id_armazem );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public List<TbProduto> getAllProdutosVendidos( Date data_inicio, Date data_fim, int id_armazem, int id_fornecedor )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  v.codigoProduto AS TOTAL FROM ItemCompras  v WHERE v.codigoVenda.statusEliminado = 'false' AND v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda     BETWEEN :data_inicio AND :data_fim   AND  v.codigoVenda.idArmazemFK.codigo = :id_armazem  AND v.codigoProduto.codFornecedores.codigo = :id_fornecedor GROUP BY v.codigoProduto" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "id_armazem", id_armazem )
                .setParameter( "id_fornecedor", id_fornecedor );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public List<RelatorioDiarioUtil> getRelatorioDiaio( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        StockDao stockDao = new StockDao( emf );

        List<TbProduto> lista_produto = getAllProdutosVendidos( data_inicio, data_fim, id_armazem );

        List<RelatorioDiarioUtil> lista_relatorio_diario = new ArrayList<>();

        RelatorioDiarioUtil relatorioDiarioUtil;
        TbProduto produto;
        double total_venda = 0;

//        for ( int i = 0; i < lista_produto.size (); i ++ )
//        {
//            relatorioDiarioUtil = new RelatorioDiarioUtil ();
//            produto = lista_produto.get ( i );
//
//            relatorioDiarioUtil.setCodigo_produto ( String.valueOf ( produto.getCodigo () ) );
//            relatorioDiarioUtil.setDesigancao ( produto.getDesignacao () );
//            relatorioDiarioUtil.setQuantidade ( String.valueOf ( getQuantidadeByIdProduto ( produto.getCodigo (), data_inicio, data_fim, id_armazem ) ) );
//            relatorioDiarioUtil.setPreco_unitario ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) );
//
//            double total = Integer.parseInt ( relatorioDiarioUtil.getQuantidade () ) * Double.parseDouble ( relatorioDiarioUtil.getPreco_unitario () );
//
//            total_venda = total_venda + total;
//
//            relatorioDiarioUtil.setPreco_unitario ( MetodosUtil.getValor ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) ) );
//            relatorioDiarioUtil.setTotal_venda ( MetodosUtil.getValor ( String.valueOf ( total ) ) );
//
//            lista_relatorio_diario.add ( relatorioDiarioUtil );
//
//        }
        relatorioDiarioUtil = new RelatorioDiarioUtil();
        lista_relatorio_diario.add( relatorioDiarioUtil );

//        relatorioDiarioUtil = new RelatorioDiarioUtil();        
//        relatorioDiarioUtil.setQuantidade("RESUMO");
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
//        
//        relatorioDiarioUtil = new RelatorioDiarioUtil();        
//        relatorioDiarioUtil.setQuantidade("TOTAL GERAL");
//        relatorioDiarioUtil.setTotal_venda(  MetodosUtil.getValor( String.valueOf( total_venda  ))  );
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
        total_venda = 0;

        return lista_relatorio_diario;

    }

    public List<RelatorioDiarioUtil> getRelatorioDiaio( Date data_inicio, Date data_fim, int id_armazem, int id_fornecedor )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        StockDao stockDao = new StockDao( emf );

        List<TbProduto> lista_produto = getAllProdutosVendidos( data_inicio, data_fim, id_armazem, id_fornecedor );

        List<RelatorioDiarioUtil> lista_relatorio_diario = new ArrayList<>();

        RelatorioDiarioUtil relatorioDiarioUtil;
        TbProduto produto;
        double total_venda = 0;

//        for ( int i = 0; i < lista_produto.size (); i ++ )
//        {
//            relatorioDiarioUtil = new RelatorioDiarioUtil ();
//            produto = lista_produto.get ( i );
//
//            relatorioDiarioUtil.setCodigo_produto ( String.valueOf ( produto.getCodigo () ) );
//            relatorioDiarioUtil.setDesigancao ( produto.getDesignacao () );
//            relatorioDiarioUtil.setQuantidade ( String.valueOf ( getQuantidadeByIdProduto ( produto.getCodigo (), data_inicio, data_fim, id_armazem ) ) );
//            relatorioDiarioUtil.setPreco_unitario ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) );
//
//            double total = Integer.parseInt ( relatorioDiarioUtil.getQuantidade () ) * Double.parseDouble ( relatorioDiarioUtil.getPreco_unitario () );
//
//            total_venda = total_venda + total;
//
//            relatorioDiarioUtil.setPreco_unitario ( MetodosUtil.getValor ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) ) );
//            relatorioDiarioUtil.setTotal_venda ( MetodosUtil.getValor ( String.valueOf ( total ) ) );
//
//            lista_relatorio_diario.add ( relatorioDiarioUtil );
//
//        }
        relatorioDiarioUtil = new RelatorioDiarioUtil();
        lista_relatorio_diario.add( relatorioDiarioUtil );

//        relatorioDiarioUtil = new RelatorioDiarioUtil();        
//        relatorioDiarioUtil.setQuantidade("RESUMO");
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
//        
//        relatorioDiarioUtil = new RelatorioDiarioUtil();        
//        relatorioDiarioUtil.setQuantidade("TOTAL GERAL");
//        relatorioDiarioUtil.setTotal_venda(  MetodosUtil.getValor( String.valueOf( total_venda  ))  );
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
        total_venda = 0;

        return lista_relatorio_diario;

    }

    public long getQuantidadeByIdProduto( int codigo_produto, Date data_inicio, Date data_fim, int pk_armazem )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(v.quantidade) AS TOTAL FROM ItemCompras  v WHERE v.codigoVenda.statusEliminado = 'false' AND  v.codigoProduto.codigo = :codigo_produto  AND v.codigoVenda.idArmazemFK.codigo = :pk_armazem AND  v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim   ORDER BY v.codigoProduto.codigo" )
                .setParameter( "codigo_produto", codigo_produto )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_armazem", pk_armazem );

        List<Long> result = query.getResultList();
        em.close();
        return result.get( 0 );

    }

//    public List<ItemCompras> getAll_ItensVendidos(Date data_inicio, Date data_fim, int pk_armazem)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT i FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND i.codigoVenda.idArmazemFK.codigo = :pk_armazem")             
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pk_armazem", pk_armazem);
//               
//        List<ItemCompras> result = query.getResultList();
//        em.close();
//       
//        if( !result.isEmpty() )
//                return result;
//        
//        ItemCompras itemVenda = new ItemCompras(0);
//        result.add(itemVenda);
//        
//        return result;
//    }
//    public double lucro ( Date data_inicio, Date data_fim )
//    {
//
//        List<ItemCompras> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim );
//        double soma = 0, diferenca = 0, desconto = 0;
//        ItemCompras itemVenda;
//        for ( int i = 0; i < lista_venda.size (); i ++ )
//        {
//
//            itemVenda = lista_venda.get ( i );
//
//            try
//            {
//
//                desconto = itemVenda.getCodigoVenda ().getDescontoTotal ();
//                diferenca = ( itemVenda.getFkPreco ().getPrecoVenda () * itemVenda.getQuantidade ()
//                        - itemVenda.getFkPreco ().getPrecoCompra () * itemVenda.getQuantidade () );
//
//                diferenca = diferenca - ( diferenca * desconto ) / 100;
//
//            }
//            catch ( Exception e )
//            {
//            }
//
//            System.out.println ( "DIFERENÇA : " + diferenca );
//            soma = soma + diferenca;
//            diferenca = 0;
//            desconto = 0;
//        }
//
//        return soma;
//
//    }
//
//    public double lucro ( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_usuario )
//    {
//
//        List<ItemCompras> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem, id_documento, id_usuario );
//        double soma = 0, lucro = 0, desconto = 0, iva = 0;
//        ItemCompras itemVenda;
//        for ( int i = 0; i < lista_venda.size (); i ++ )
//        {
//
//            itemVenda = lista_venda.get ( i );
//
//            try
//            {
//
//                desconto = itemVenda.getDesconto();
//                lucro = ( itemVenda.getFkPreco ().getPrecoVenda () * itemVenda.getQuantidade ()
//                        - itemVenda.getFkPreco ().getPrecoCompra () * itemVenda.getQuantidade () );
//
//                lucro = ( lucro - ( lucro * desconto ) / 100 );
//
//            }
//            catch ( Exception e )
//            {
//            }
//
//            System.out.println ( "DIFERENÇA : " + lucro );
//            soma = soma + lucro;
//            lucro = 0;
//            desconto = 0;
//        }
//
//        return soma;
//
//    }
//    public double lucro ( Date data_inicio, Date data_fim, int id_armazem, int id_documento)
//    {
//
//        List<ItemCompras> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem, id_documento );
//        double soma = 0, lucro = 0, desconto = 0, iva = 0;
//        ItemCompras itemVenda;
//        for ( int i = 0; i < lista_venda.size (); i ++ )
//        {
//
//            itemVenda = lista_venda.get ( i );
//
//            try
//            {
//
//                desconto = itemVenda.getDesconto();
//                lucro = ( itemVenda.getFkPreco ().getPrecoVenda () * itemVenda.getQuantidade ()
//                        - itemVenda.getFkPreco ().getPrecoCompra () * itemVenda.getQuantidade () );
//
//                lucro = ( lucro - ( lucro * desconto ) / 100 );
//
//            }
//            catch ( Exception e )
//            {
//            }
//
//            System.out.println ( "DIFERENÇA : " + lucro );
//            soma = soma + lucro;
//            lucro = 0;
//            desconto = 0;
//        }
//
//        return soma;
//
//    }
//    public double lucroUsuario ( Date data_inicio, Date data_fim, int id_armazem, int id_usuario )
//    {
//
//        List<ItemCompras> lista_venda = getAll_ItensVendidosUsuario(data_inicio, data_fim, id_armazem, id_usuario );
//        double soma = 0, diferenca = 0, desconto = 0;
//        ItemCompras itemVenda;
//        for ( int i = 0; i < lista_venda.size (); i ++ )
//        {
//
//            itemVenda = lista_venda.get ( i );
//
//            try
//            {
//
//                desconto = itemVenda.getCodigoVenda ().getDescontoTotal ();
//                diferenca = ( itemVenda.getFkPreco ().getPrecoVenda () * itemVenda.getQuantidade ()
//                        - itemVenda.getFkPreco ().getPrecoCompra () * itemVenda.getQuantidade () );
//
//                diferenca = diferenca - ( diferenca * desconto ) / 100;
//
//            }
//            catch ( Exception e )
//            {
//            }
//
//            System.out.println ( "DIFERENÇA : " + diferenca );
//            soma = soma + diferenca;
//            diferenca = 0;
//            desconto = 0;
//        }
//
//        return soma;
//
//    }
    public List<TbVenda> getAllVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i.codigoVenda FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' GROUP BY i.codigoVenda.codigo" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public boolean exist_factura( int id_venda )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM ItemCompras  v WHERE v.codigoVenda.codigo = :id_venda AND v.codigoVenda.statusEliminado = 'false'" )
                .setParameter( "id_venda", id_venda );

        List<ItemCompras> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public static void main( String[] args )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ItemComprasDao itemVendaDao = new ItemComprasDao( emf );
        VendaDao vendaDao = new VendaDao( emf );
        //sPrecoDao precoDao = new PrecoDao(emf);

        //List<ItemCompras> list = itemVendaDao.getAllVenda();
//        System.out.println ( "LUCRO GERAL: " + itemVendaDao.lucro(new Date (), new Date (), 1 , 1, 1) );
//        for (int i = 0; i < list.size(); i++) 
//        {
//            
//            ItemCompras itemVenda = list.get(i);            
//            itemVenda.setFkPreco(    precoDao.getLastPrecoByIdProduto(  itemVenda.getCodigoProduto().getCodigo()  )   );            
//            try {
//                itemVendaDao.edit(itemVenda);
//            } catch (Exception e) {
//            }
//           
//            
//       }
    }

    public List<ItemCompras> getAll_ItensVendidos( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );

        return result;
    }

    public List<ItemCompras> getAll_ItensVendidos( Date data_inicio, Date data_fim, int id_armazem, int id_documento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.fkDocumento.pkDocumento = :id_documento" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "id_armazem", id_armazem )
                .setParameter( "id_documento", id_documento );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );

        return result;
    }

    public List<ItemCompras> getAll_ItensVendidos( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_usuario )
    {

        EntityManager em = getEntityManager();
        //  Query query = em.createQuery ( "SELECT i FROM ItemCompras  i WHERE  i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.fkDocumento.pkDocumento = 1" )
        Query query = em.createQuery( "SELECT i FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.fkDocumento.pkDocumento = :id_documento AND i.codigoVenda.codigoUsuario.codigo = :id_usuario" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "id_armazem", id_armazem )
                .setParameter( "id_documento", id_documento )
                .setParameter( "id_usuario", id_usuario );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemVenda = new ItemCompras( 0 );
        result.add( itemVenda );

        return result;
    }

//    public List<ItemCompras> getAll_ItensVendidosUsuario ( Date data_inicio, Date data_fim, int id_armazem, int id_usuario )
//    {
//
//        EntityManager em = getEntityManager ();
//        Query query = em.createQuery ( "SELECT i FROM ItemCompras  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.codigoUsuario.codigo = :id_usuario" )
//                .setParameter ( "data_inicio", data_inicio )
//                .setParameter ( "data_fim", data_fim )
//                .setParameter ( "id_armazem", id_armazem )
//                .setParameter ( "id_usuario", id_usuario );
//
//        List<ItemCompras> result = query.getResultList ();
//        em.close ();
//
//        if (  ! result.isEmpty () )
//        {
//            return result;
//        }
//
//        ItemCompras itemVenda = new ItemCompras ( 0 );
//        result.add ( itemVenda );
//
//        return result;
//    }
    public List<ItemCompras> itens_venda_by_id( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM ItemCompras p WHERE p.codigoVenda.codigo = :codigo" )
                .setParameter( "codigo", codigo );
        return query.getResultList();

    }

    public ItemCompras getByIdVendaAndProductName( Integer codigoVenda, String codProdutoSelecionado )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v AS TOTAL FROM ItemCompras  v WHERE v.codigoVenda.codigo = :CODIGO_VENDA AND  v.codigoProduto.designacao = :DESIGNACAO_PRODUTO" )
                .setParameter( "CODIGO_VENDA", codigoVenda )
                .setParameter( "DESIGNACAO_PRODUTO", codProdutoSelecionado );

        List<ItemCompras> result = query.getResultList();

        if ( result.isEmpty() )
        {
            return null;
        }

        return result.get( 0 );
    }

    public ItemCompras getlItemVendasByIdVendaAndPrduto( Integer fkVenda, Integer fkProduto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v AS TOTAL FROM ItemCompras  v WHERE v.codigoVenda.codigo = :CODIGO_VENDA AND  v.codigoProduto.codigo = :CODIGO_PRODUTO" )
                .setParameter( "CODIGO_VENDA", fkVenda )
                .setParameter( "CODIGO_PRODUTO", fkProduto );

        List<ItemCompras> result = query.getResultList();

        if ( result.isEmpty() )
        {
            return null;
        }

        return result.get( 0 );
    }

    public List<ItemCompras> buscar_dcompras_by_idFornecedor( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM ItemCompras i WHERE i.fkCompra.fkFornecedor.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<ItemCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        ItemCompras itemCompras = new ItemCompras( 0 );
        result.add( itemCompras );
        return result;

    }

    public List<ItemCompras> getComprasByDescricao( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM ItemCompras p WHERE p.fkCompra.codFact = :codFact" )
                .setParameter( "codFact", codFact );

        List<ItemCompras> list = query.getResultList();
        em.close();
        if ( !list.isEmpty() )
        {
            return list;
        }
        ItemCompras itemCompras = new ItemCompras( 0 );
        list.add( itemCompras );
        return list;
    }

    public List<ItemCompras> buscaTodosByPedido( boolean autorizado )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT t FROM ItemCompras t   WHERE t.fkCompra.autorizado = :autorizado" )
                .setParameter( "autorizado", autorizado );

        return query.getResultList();

    }

    public boolean elimina_itens( List<ItemCompras> itemCompras )
    {

        System.err.println( "TAMANHO ITEM COMPRA: " + itemCompras.size() );
        for ( int i = 0; i < itemCompras.size(); i++ )
        {
            ItemCompras get = itemCompras.get( i );

            try
            {
                destroy( get.getPkItmCompras() );

            }
            catch ( Exception e )
            {
                e.printStackTrace();
                // getEntityManager().getTransaction().rollback();
                return false;
            }

        }
        return true;

    }

    public List<ItemCompras> getItensByEncomendas( int pkCompra )
    {

        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT s FROM ItemCompras s WHERE s.fkCompra.pkCompra = :pkCompra" )
                .setParameter( "pkCompra", pkCompra );

        List<ItemCompras> list = query.getResultList();

        System.out.println( "TAMANHO ITEM COMPRA: " + list.size() );
        if ( !list.isEmpty() )
        {
            return list;
        }
        return null;

    }

    public double getPrecoCompraMedio( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_produto )
    {
//        System.out.println( "Data Inicio: " + data_inicio.toString() );
//        System.out.println( "Data Fim: " + data_fim.toString() );
//        System.out.println( "ID Armazem: " + id_armazem );
//        System.out.println( "Cod Documento: " + id_documento );
//        System.out.println( "Id Produto: " + id_produto );
        double precoCompraMedio = 0d;
        //busca todos as compras efectuadas por um determinado produto
        List<ItemCompras> listaItemCompra = getAllCompraByIdProduto( data_inicio, data_fim, id_armazem, id_documento, id_produto );

        if ( !Objects.isNull( listaItemCompra ) )
        {
            int N = listaItemCompra.size();
            
            for ( ItemCompras itemCompras : listaItemCompra )
            {
                precoCompraMedio += itemCompras.getPrecoCompra();
            }

            precoCompraMedio = ( ( precoCompraMedio ) / N );
        }

        return precoCompraMedio;

    }

    public List<ItemCompras> getAllCompraByIdProduto( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_produto )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT i FROM ItemCompras i WHERE i.fkCompra.dataCompra BETWEEN :data_inicio AND :data_fim AND i.fkCompra.idArmazemFK.codigo = :id_armazem AND i.fkCompra.fkDocumento.pkDocumento = :id_documento AND i.fkProduto.codigo = :id_produto" )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim )
//                .setParameter( "id_armazem", id_armazem )
//                .setParameter( "id_documento", id_documento )
//                .setParameter( "id_produto", id_produto );
//
//        List<ItemCompras> result = query.getResultList();
//        em.close();
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }

        Query query = em.createNativeQuery( "SELECT i.* FROM item_compras i , compras c WHERE "
                + " DATE(c.data_compra) BETWEEN ? AND ? "
                + " AND c.idArmazemFK = ? "
                + " AND c.fk_documento = ? "
                + " AND i.fk_produto = ? "
                + " AND i.fk_compra = c.pk_compra", ItemCompras.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, id_armazem );
        query.setParameter( 4, id_documento );
        query.setParameter( 5, id_produto );
        
        return query.getResultList();

    }

}
