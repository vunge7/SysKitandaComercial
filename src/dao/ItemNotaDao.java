/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbItemVendaJpaController;
import entity.ItensNota;
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
public class ItemNotaDao extends TbItemVendaJpaController
{

    public ItemNotaDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public List<TbItemVenda> getAllItemVendasByIdVendaReciclagem ( int id_venda )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM TbItemVenda  v WHERE v.codigoVenda.codigo = :id_venda" )
                .setParameter ( "id_venda", id_venda );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbItemVenda itemVenda = new TbItemVenda ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<TbItemVenda> getAllItemVendasByIdVenda ( int id_venda )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM TbItemVenda  v WHERE v.codigoVenda.codigo = :id_venda AND v.codigoVenda.statusEliminado = 'false'" )
                .setParameter ( "id_venda", id_venda );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbItemVenda itemVenda = new TbItemVenda ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<TbItemVenda> getAllVenda ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM TbItemVenda  v WHERE  v.codigoVenda.statusEliminado = 'false' AND v.codigo > 32898" );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbItemVenda itemVenda = new TbItemVenda ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<TbProduto> getAllProdutosVendidos ( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  v.codigoProduto AS TOTAL FROM TbItemVenda  v WHERE v.codigoVenda.statusEliminado = 'false' AND v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda     BETWEEN :data_inicio AND :data_fim   AND  v.codigoVenda.idArmazemFK.codigo = :id_armazem  GROUP BY v.codigoProduto" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem );

        List<TbProduto> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;

    }

    public List<TbProduto> getAllProdutosVendidos ( Date data_inicio, Date data_fim, int id_armazem, int id_fornecedor )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  v.codigoProduto AS TOTAL FROM TbItemVenda  v WHERE v.codigoVenda.statusEliminado = 'false' AND v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda     BETWEEN :data_inicio AND :data_fim   AND  v.codigoVenda.idArmazemFK.codigo = :id_armazem  AND v.codigoProduto.codFornecedores.codigo = :id_fornecedor GROUP BY v.codigoProduto" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem )
                .setParameter ( "id_fornecedor", id_fornecedor );

        List<TbProduto> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;

    }

    public List<RelatorioDiarioUtil> getRelatorioDiaio ( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        StockDao stockDao = new StockDao ( emf );

        List<TbProduto> lista_produto = getAllProdutosVendidos ( data_inicio, data_fim, id_armazem );

        List<RelatorioDiarioUtil> lista_relatorio_diario = new ArrayList<> ();

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

        relatorioDiarioUtil = new RelatorioDiarioUtil ();
        lista_relatorio_diario.add ( relatorioDiarioUtil );

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

    public List<RelatorioDiarioUtil> getRelatorioDiaio ( Date data_inicio, Date data_fim, int id_armazem, int id_fornecedor )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        StockDao stockDao = new StockDao ( emf );

        List<TbProduto> lista_produto = getAllProdutosVendidos ( data_inicio, data_fim, id_armazem, id_fornecedor );

        List<RelatorioDiarioUtil> lista_relatorio_diario = new ArrayList<> ();

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

        relatorioDiarioUtil = new RelatorioDiarioUtil ();
        lista_relatorio_diario.add ( relatorioDiarioUtil );

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

    public long getQuantidadeByIdProduto ( int codigo_produto, Date data_inicio, Date data_fim, int pk_armazem )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT SUM(v.quantidade) AS TOTAL FROM TbItemVenda  v WHERE v.codigoVenda.statusEliminado = 'false' AND  v.codigoProduto.codigo = :codigo_produto  AND v.codigoVenda.idArmazemFK.codigo = :pk_armazem AND  v.codigoVenda.performance = 'false' AND v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim   ORDER BY v.codigoProduto.codigo" )
                .setParameter ( "codigo_produto", codigo_produto )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "pk_armazem", pk_armazem );

        List<Long> result = query.getResultList ();
        em.close ();
        return result.get ( 0 );

    }

//    public List<TbItemVenda> getAll_ItensVendidos(Date data_inicio, Date data_fim, int pk_armazem)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT i FROM TbItemVenda  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND i.codigoVenda.idArmazemFK.codigo = :pk_armazem")             
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pk_armazem", pk_armazem);
//               
//        List<TbItemVenda> result = query.getResultList();
//        em.close();
//       
//        if( !result.isEmpty() )
//                return result;
//        
//        TbItemVenda itemVenda = new TbItemVenda(0);
//        result.add(itemVenda);
//        
//        return result;
//    }
    public double lucro ( Date data_inicio, Date data_fim )
    {

        List<TbItemVenda> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim );
        double soma = 0, diferenca = 0, desconto = 0;
        TbItemVenda itemVenda;
        for ( int i = 0; i < lista_venda.size (); i ++ )
        {

            itemVenda = lista_venda.get ( i );

            try
            {

                desconto = itemVenda.getCodigoVenda ().getDescontoTotal ().doubleValue();
                diferenca = ( itemVenda.getFkPreco ().getPrecoVenda ().doubleValue() * itemVenda.getQuantidade ()
                        - itemVenda.getFkPreco ().getPrecoCompra ().doubleValue() * itemVenda.getQuantidade () );

                diferenca = diferenca - ( diferenca * desconto ) / 100;

            }
            catch ( Exception e )
            {
            }

            System.out.println ( "DIFERENÇA : " + diferenca );
            soma = soma + diferenca;
            diferenca = 0;
            desconto = 0;
        }

        return soma;

    }

    public double lucro ( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_usuario )
    {

        List<TbItemVenda> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem, id_documento, id_usuario );
        double soma = 0, lucro = 0, desconto = 0, iva = 0;
        TbItemVenda itemVenda;
        for ( int i = 0; i < lista_venda.size (); i ++ )
        {

            itemVenda = lista_venda.get ( i );

            try
            {

                desconto = itemVenda.getDesconto();
                lucro = ( itemVenda.getFkPreco ().getPrecoVenda ().doubleValue() * itemVenda.getQuantidade ()
                        - itemVenda.getFkPreco ().getPrecoCompra ().doubleValue() * itemVenda.getQuantidade () );

                lucro = ( lucro - ( lucro * desconto ) / 100 );

            }
            catch ( Exception e )
            {
            }

            System.out.println ( "DIFERENÇA : " + lucro );
            soma = soma + lucro;
            lucro = 0;
            desconto = 0;
        }

        return soma;

    }
//    public double lucro ( Date data_inicio, Date data_fim, int id_armazem, int id_documento)
//    {
//
//        List<ItensNota> lista_venda = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem, id_documento );
//        double soma = 0, lucro = 0, desconto = 0, iva = 0;
//        ItensNota itensNota;
//        for ( int i = 0; i < lista_venda.size (); i ++ )
//        {
//
//            itensNota = lista_venda.get ( i );
//
//            try
//            {
//
//                desconto = itensNota.get;
//                lucro = ( itensNota.getFkPreco ().getPrecoVenda () * itensNota.getQuantidade ()
//                        - itensNota.getFkPreco ().getPrecoCompra () * itensNota.getQuantidade () );
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
//        List<TbItemVenda> lista_venda = getAll_ItensVendidosUsuario(data_inicio, data_fim, id_armazem, id_usuario );
//        double soma = 0, diferenca = 0, desconto = 0;
//        TbItemVenda itemVenda;
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

    public List<TbVenda> getAllVenda ( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i.codigoVenda FROM TbItemVenda  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' GROUP BY i.codigoVenda.codigo" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbVenda venda = new TbVenda ( 0 );
        result.add ( venda );

        return result;
    }

    public boolean exist_factura ( int id_venda )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM TbItemVenda  v WHERE v.codigoVenda.codigo = :id_venda AND v.codigoVenda.statusEliminado = 'false'" )
                .setParameter ( "id_venda", id_venda );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        return  ! result.isEmpty ();

    }

    public static void main ( String[] args )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ItemNotaDao itemVendaDao = new ItemNotaDao ( emf );
        VendaDao vendaDao = new VendaDao ( emf );
        //sPrecoDao precoDao = new PrecoDao(emf);

        //List<TbItemVenda> list = itemVendaDao.getAllVenda();
        System.out.println ( "LUCRO GERAL: " + itemVendaDao.lucro(new Date (), new Date (), 1 , 1, 1) );

//        for (int i = 0; i < list.size(); i++) 
//        {
//            
//            TbItemVenda itemVenda = list.get(i);            
//            itemVenda.setFkPreco(    precoDao.getLastPrecoByIdProduto(  itemVenda.getCodigoProduto().getCodigo()  )   );            
//            try {
//                itemVendaDao.edit(itemVenda);
//            } catch (Exception e) {
//            }
//           
//            
//       }
    }

    public List<TbItemVenda> getAll_ItensVendidos ( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM TbItemVenda  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' " )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbItemVenda itemVenda = new TbItemVenda ( 0 );
        result.add ( itemVenda );

        return result;
    }

    public List<ItensNota> getAll_ItensVendidos ( Date data_inicio, Date data_fim, int id_armazem, int id_documento )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM ItensNota  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.idArmazemFK.codigo = :id_armazem AND i.fkNota.fkDocumento.pkDocumento = :id_documento" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem )
                .setParameter ( "id_documento", id_documento );

        List<ItensNota> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        ItensNota itemVenda = new ItensNota ( 0 );
        result.add ( itemVenda );

        return result;
    }
    
    public List<TbItemVenda> getAll_ItensVendidos ( Date data_inicio, Date data_fim, int id_armazem, int id_documento, int id_usuario )
    {

        EntityManager em = getEntityManager ();
      //  Query query = em.createQuery ( "SELECT i FROM TbItemVenda  i WHERE  i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.fkDocumento.pkDocumento = 1" )
        Query query = em.createQuery ( "SELECT i FROM TbItemVenda  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.fkDocumento.pkDocumento = :id_documento AND i.codigoVenda.codigoUsuario.codigo = :id_usuario" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem )
                .setParameter ( "id_documento", id_documento )
                .setParameter ( "id_usuario", id_usuario );

        List<TbItemVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbItemVenda itemVenda = new TbItemVenda ( 0 );
        result.add ( itemVenda );

        return result;
    }

//    public List<TbItemVenda> getAll_ItensVendidosUsuario ( Date data_inicio, Date data_fim, int id_armazem, int id_usuario )
//    {
//
//        EntityManager em = getEntityManager ();
//        Query query = em.createQuery ( "SELECT i FROM TbItemVenda  i WHERE i.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND i.codigoVenda.statusEliminado = 'false' AND  i.codigoVenda.credito = 'false' AND i.codigoVenda.idArmazemFK.codigo = :id_armazem AND i.codigoVenda.codigoUsuario.codigo = :id_usuario" )
//                .setParameter ( "data_inicio", data_inicio )
//                .setParameter ( "data_fim", data_fim )
//                .setParameter ( "id_armazem", id_armazem )
//                .setParameter ( "id_usuario", id_usuario );
//
//        List<TbItemVenda> result = query.getResultList ();
//        em.close ();
//
//        if (  ! result.isEmpty () )
//        {
//            return result;
//        }
//
//        TbItemVenda itemVenda = new TbItemVenda ( 0 );
//        result.add ( itemVenda );
//
//        return result;
//    }

    public List<TbItemVenda> itens_venda_by_id ( int codigo )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT p FROM TbItemVenda p WHERE p.codigoVenda.codigo = :codigo" )
                .setParameter ( "codigo", codigo );
        return query.getResultList ();

    }

    public TbItemVenda getByIdVendaAndProductName ( Integer codigoVenda, String codProdutoSelecionado )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v AS TOTAL FROM TbItemVenda  v WHERE v.codigoVenda.codigo = :CODIGO_VENDA AND  v.codigoProduto.designacao = :DESIGNACAO_PRODUTO" )
                .setParameter ( "CODIGO_VENDA", codigoVenda )
                .setParameter ( "DESIGNACAO_PRODUTO", codProdutoSelecionado );

        List<TbItemVenda> result = query.getResultList ();

        if ( result.isEmpty () )
        {
            return null;
        }

        return result.get ( 0 );
    }

    public TbItemVenda getlItemVendasByIdVendaAndPrduto ( Integer fkVenda, Integer fkProduto )
    {
         EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v AS TOTAL FROM TbItemVenda  v WHERE v.codigoVenda.codigo = :CODIGO_VENDA AND  v.codigoProduto.codigo = :CODIGO_PRODUTO" )
                .setParameter ( "CODIGO_VENDA", fkVenda )
                .setParameter ( "CODIGO_PRODUTO", fkProduto );

        List<TbItemVenda> result = query.getResultList ();

        if ( result.isEmpty () )
        {
            return null;
        }

        return result.get ( 0 );
    }

}
