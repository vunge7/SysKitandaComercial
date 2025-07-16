/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.NotasItemJpaController;
import entity.NotasItem;
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
public class NotasItemDao extends NotasItemJpaController
{

    public NotasItemDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public List<NotasItem> getAllItemVendasByIdVendaReciclagem ( int id_nota )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM NotasItem  v WHERE v.fkNota.pkNota = :id_nota" )
                .setParameter ( "id_nota", id_nota );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<NotasItem> getAllItemVendasByIdVenda ( int id_nota )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM NotasItem  v WHERE v.fkNota.pkNota = :id_nota AND v.fkNota.statusEliminado = 'false'" )
                .setParameter ( "id_nota", id_nota );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<NotasItem> getAllVenda ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM NotasItem  v WHERE  v.fkNota.statusEliminado = 'false' AND v.pkNotasItem > 32898" );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );
        return result;
    }

    public List<TbProduto> getAllProdutosVendidos ( Date data_inicio, Date data_fim, int id_armazem )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  v.fkProduto AS TOTAL FROM NotasItem  v WHERE v.fkNota.statusEliminado = 'false' AND v.fkNota.performance = 'false' AND v.fkNota.dataNota     BETWEEN :data_inicio AND :data_fim   AND  v.fkNota.idArmazemFK.codigo = :id_armazem  GROUP BY v.fkProduto" )
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
        Query query = em.createQuery ( "SELECT  v.fkProduto AS TOTAL FROM NotasItem  v WHERE v.fkNota.statusEliminado = 'false' AND v.fkNota.performance = 'false' AND v.fkNota.dataNota     BETWEEN :data_inicio AND :data_fim   AND  v.fkNota.idArmazemFK.codigo = :id_armazem  AND v.fkProduto.codFornecedores.codigo = :id_fornecedor GROUP BY v.fkProduto" )
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
        double total_nota = 0;

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
//            total_nota = total_nota + total;
//
//            relatorioDiarioUtil.setPreco_unitario ( MetodosUtil.getValor ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) ) );
//            relatorioDiarioUtil.setTotal_nota ( MetodosUtil.getValor ( String.valueOf ( total ) ) );
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
//        relatorioDiarioUtil.setTotal_nota(  MetodosUtil.getValor( String.valueOf( total_nota  ))  );
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
        total_nota = 0;

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
        double total_nota = 0;

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
//            total_nota = total_nota + total;
//
//            relatorioDiarioUtil.setPreco_unitario ( MetodosUtil.getValor ( String.valueOf ( stockDao.getStockByDescricao ( produto.getCodigo () ).getPrecoVenda () ) ) );
//            relatorioDiarioUtil.setTotal_nota ( MetodosUtil.getValor ( String.valueOf ( total ) ) );
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
//        relatorioDiarioUtil.setTotal_nota(  MetodosUtil.getValor( String.valueOf( total_nota  ))  );
//        lista_relatorio_diario.add(relatorioDiarioUtil);
//        
        total_nota = 0;

        return lista_relatorio_diario;

    }

    public long getQuantidadeByIdProduto ( int codigo_produto, Date data_inicio, Date data_fim, int pk_armazem )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT SUM(v.quantidade) AS TOTAL FROM NotasItem  v WHERE v.fkNota.statusEliminado = 'false' AND  v.fkProduto.codigo = :codigo_produto  AND v.fkNota.idArmazemFK.codigo = :pk_armazem AND  v.fkNota.performance = 'false' AND v.fkNota.dataNota BETWEEN :data_inicio AND :data_fim   ORDER BY v.fkProduto.codigo" )
                .setParameter ( "codigo_produto", codigo_produto )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "pk_armazem", pk_armazem );

        List<Long> result = query.getResultList ();
        em.close ();
        return result.get ( 0 );

    }

//    public List<NotasItem> getAll_ItensVendidos(Date data_inicio, Date data_fim, int pk_armazem)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT i FROM NotasItem  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.statusEliminado = 'false' AND i.fkNota.idArmazemFK.codigo = :pk_armazem")             
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pk_armazem", pk_armazem);
//               
//        List<NotasItem> result = query.getResultList();
//        em.close();
//       
//        if( !result.isEmpty() )
//                return result;
//        
//        NotasItem itemVenda = new NotasItem(0);
//        result.add(itemVenda);
//        
//        return result;
//    }
    public double lucro ( Date data_inicio, Date data_fim )
    {

        List<NotasItem> lista_nota = getAll_ItensVendidos ( data_inicio, data_fim );
        double soma = 0, diferenca = 0, desconto = 0;
        NotasItem itemVenda;
        for ( int i = 0; i < lista_nota.size (); i ++ )
        {

            itemVenda = lista_nota.get ( i );

            try
            {

                desconto = itemVenda.getFkVenda ().getDescontoTotal().doubleValue();
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

    public double lucro ( Date data_inicio, Date data_fim, int id_armazem )
    {

        List<NotasItem> lista_nota = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem );
        double soma = 0, lucro = 0, desconto = 0;
        NotasItem itemVenda;
        for ( int i = 0; i < lista_nota.size (); i ++ )
        {

            itemVenda = lista_nota.get ( i );

            try
            {

                desconto = itemVenda.getFkVenda ().getDescontoTotal().doubleValue();
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

    public double lucro ( Date data_inicio, Date data_fim, int id_armazem, int id_usuario )
    {

        List<NotasItem> lista_nota = getAll_ItensVendidos ( data_inicio, data_fim, id_armazem, id_usuario );
        double soma = 0, diferenca = 0, desconto = 0;
        NotasItem itemVenda;
        for ( int i = 0; i < lista_nota.size (); i ++ )
        {

            itemVenda = lista_nota.get ( i );

            try
            {

                desconto = itemVenda.getFkVenda ().getDescontoTotal().doubleValue();
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

    public List<TbVenda> getAllVenda ( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i.fkNota FROM NotasItem  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.statusEliminado = 'false' GROUP BY i.fkNota.pkNota" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        TbVenda nota = new TbVenda ( 0 );
        result.add ( nota );

        return result;
    }

    public boolean exist_factura ( int id_nota )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v FROM NotasItem  v WHERE v.fkNota.pkNota = :id_nota AND v.fkNota.statusEliminado = 'false'" )
                .setParameter ( "id_nota", id_nota );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        return  ! result.isEmpty ();

    }

    public static void main ( String[] args )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        NotasItemDao itemVendaDao = new NotasItemDao ( emf );
        VendaDao notaDao = new VendaDao ( emf );
        //sPrecoDao precoDao = new PrecoDao(emf);

        //List<NotasItem> list = itemVendaDao.getAllVenda();
        System.out.println ( "LUCRO GERAL: " + itemVendaDao.lucro ( new Date (), new Date (), 1 ) );

//        for (int i = 0; i < list.size(); i++) 
//        {
//            
//            NotasItem itemVenda = list.get(i);            
//            itemVenda.setFkPreco(    precoDao.getLastPrecoByIdProduto(  itemVenda.getCodigoProduto().getCodigo()  )   );            
//            try {
//                itemVendaDao.edit(itemVenda);
//            } catch (Exception e) {
//            }
//           
//            
//       }
    }

    public List<NotasItem> getAll_ItensVendidos ( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM NotasItem  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.statusEliminado = 'false' AND  i.fkNota.credito = 'false' " )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );

        return result;
    }

    public List<NotasItem> getAll_ItensVendidos ( Date data_inicio, Date data_fim, int id_armazem )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM NotasItem  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.statusEliminado = 'false' AND  i.fkNota.credito = 'false' AND i.fkNota.idArmazemFK.codigo = :id_armazem" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );

        return result;
    }

    public List<NotasItem> getAll_ItensVendidos ( Date data_inicio, Date data_fim, int id_armazem, int id_usuario )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT i FROM NotasItem  i WHERE i.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND i.fkNota.statusEliminado = 'false' AND  i.fkNota.credito = 'false' AND i.fkNota.idArmazemFK.codigo = :id_armazem AND i.fkNota.pkNotaUsuario.codigo = :id_usuario" )
                .setParameter ( "data_inicio", data_inicio )
                .setParameter ( "data_fim", data_fim )
                .setParameter ( "id_armazem", id_armazem )
                .setParameter ( "id_usuario", id_usuario );

        List<NotasItem> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        NotasItem itemVenda = new NotasItem ( 0 );
        result.add ( itemVenda );

        return result;
    }

    public List<NotasItem> itens_nota_by_id ( int codigo )
    {

        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT p FROM NotasItem p WHERE p.fkNota.pkNota = :codigo" )
                .setParameter ( "codigo", codigo );
        return query.getResultList ();

    }

    public NotasItem getByIdVendaAndProductName ( Integer fkNota, String codProdutoSelecionado )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT v AS TOTAL FROM NotasItem  v WHERE v.fkNota.pkNota = :CODIGO_VENDA AND  v.fkProduto.designacao = :DESIGNACAO_PRODUTO" )
                .setParameter ( "CODIGO_VENDA", fkNota )
                .setParameter ( "DESIGNACAO_PRODUTO", codProdutoSelecionado );

        List<NotasItem> result = query.getResultList ();

        if ( result.isEmpty () )
        {
            return null;
        }

        return result.get ( 0 );
    }

}
