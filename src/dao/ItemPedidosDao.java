/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
//import controlador.ItemPedidosJpaController;
//import entity.TbItemPedidos;
import controlador.TbItemPedidosJpaController;
import entity.TbItemPedidos;
import entity.TbProduto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Toshiba
 */
public class ItemPedidosDao extends TbItemPedidosJpaController
{

    public ItemPedidosDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbItemPedidos> buscaTodosItemPedidos( int pk_pedido )
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.fkPedidos.pkPedido = :pk_pedido AND p.statusConvertido = FALSE ORDER BY p.fkLugares.designacao ASC" )
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.fkPedidos.pkPedido = :pk_pedido AND p.statusConvertido = FALSE" )
                .setParameter( "pk_pedido", pk_pedido );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

    public List<TbItemPedidos> buscaTodosItemPedidosRecolha( int pk_pedido )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.fkPedidos.pkPedido = :pk_pedido AND p.statusConvertido = FALSE" )
                .setParameter( "pk_pedido", pk_pedido );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

    public List<TbItemPedidos> buscaTodosItemPedidos2( int pkItemPedidos )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.pkItemPedidos = :pkItemPedidos AND p.statusConvertido = FALSE ORDER BY p.fkLugares.designacao ASC" )
                .setParameter( "pkItemPedidos", pkItemPedidos );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

    public int getLastVItemPedido( int pkPedido )
    {
        System.out.println( "Ultimo Pedido" + pkPedido );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(i.pkItemPedidos)  FROM TbItemPedidos  i WHERE i.fkPedidos.pkPedido = :pkPedido" )
                .setParameter( "pkPedido", pkPedido );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public List<TbItemPedidos> buscaTodosItemPedidos( int pk_pedido, int lugar )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.fkPedidos.pkPedido = :pk_pedido   AND p.fkLugares.pkLugares = :lugar  AND p.statusConvertido = false ORDER BY p.fkLugares.designacao ASC" )
                .setParameter( "pk_pedido", pk_pedido )
                .setParameter( "lugar", lugar );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

    public int getLastPedidoByDefignacaoMesaLugarFALSE( String mesa, String lugar )
    {

        System.out.println( "DESIGNACAO " + mesa );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.pkItemPedidos) FROM TbItemPedidos m WHERE m.fkPedidos.fkMesas.designacao = :mesa AND m.fkLugares.designacao = :lugar AND m.fkPedidos.statusPedido = FALSE" )
                .setParameter( "mesa", mesa )
                .setParameter( "lugar", lugar );

        List<Integer> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            try
            {
                return lista.get( 0 );
            }
            catch ( Exception e )
            {
            }

        }
        return 0;
    }

    public List<TbItemPedidos> getAllPedidosCozinha()
    {
        EntityManager em = getEntityManager();
        //Query query = em.createQuery ("SELECT p FROM TbItemPedidos p WHERE p.statusEnviado = true   AND p.statusEfectuado = false  AND  p.fkProdutos.codTipoProduto.codigo = 2  OR p.fkProdutos.codTipoProduto.codigo = 1  ORDER BY p.pkItemPedidos ASC");
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.statusEnviado = true  AND p.statusEfectuado = false  AND  p.fkProdutos.stocavel = false ORDER BY p.pkItemPedidos ASC" );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

    public List<TbItemPedidos> getAllPedidosProdutos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbItemPedidos p WHERE p.statusEnviado = true   AND p.statusEfectuado = false  AND  p.fkProdutos.codTipoProduto.codigo > 2 ORDER BY p.pkItemPedidos ASC" );

        List<TbItemPedidos> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista;

        }
        else
        {
            return null;
        }

    }

//     public Vector <String > buscaTodos () 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p.designacao FROM Produtos p");
//            Vector<String>  result  =  (Vector)query.getResultList();
//           
//           result.add(0,"-- SELECIONE --");         
//           return  result;
//    }
//     public List <Produtos > buscaTodosProdutos() 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p FROM Produtos p");
//            return query.getResultList();
//    }
//     
//      public List <Produtos > buscaTodosProdutosByIdProduto(int pkTipoProduto) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p FROM Produtos p WHERE p.fkTipoProduto.pkTipoProdutos = :pkTipoProduto")
//                    .setParameter("pkTipoProduto", pkTipoProduto);
//            return query.getResultList();
//    }
//     public String  getDescricaoById(long idProduto) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p.designacao FROM Produtos p WHERE p.pkProduto = :idProduto")
//                    .setParameter("idProduto", idProduto);
//            
//            List   list = query.getResultList();
//            
//            if( list!=null){
//                    return  String.valueOf( list.get(0) );
//            }
//            return "";
//    }
//    
//     
//     public Produtos  getProdutoByDescricao(String designacao) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p FROM Produtos p WHERE p.designacao = :designacao")
//                    .setParameter("designacao", designacao);
//            
//            List<Produtos>   list = query.getResultList();
//            
//            if( !list.isEmpty() ){
//                    return  list.get(0) ;
//            }
//            return new Produtos(0);
//    }
//    
//     
//     public  List <Produtos >  getDescricaoByIdTipoProduto(int idTipoProduto) 
//     {         
//          System.out.println("cheguei aqui Dallas :"    + idTipoProduto);
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p.designacao FROM Produtos p WHERE p.fkTipoProduto.pkTipoProdutos = :idTipoProduto")
//                    .setParameter("idTipoProduto", idTipoProduto);
//     
//            return query.getResultList();
//    }
//     
//    public List<Produtos> getAllProdutosByIdTipoProduto(int idtipoProduto)            
//    {
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery("SELECT p FROM Produtos");
//            
//            
//            
//            List<Produtos> lista = query.getResultList();
//            
//            if ( !lista.isEmpty() ) {
//               return lista;
//        }
//            
//            
//            return null;
//    
//    
//    }
//     
//   
//     
//   
//     public boolean exist_produto (String nome_produto) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p FROM Produtos p WHERE p.designacao = :nome_produto")
//                    .setParameter("nome_produto", nome_produto);
//            
//            List result = query.getResultList();
//            
//            if( !result.isEmpty() )
//                return  true;
//            return false;
//    }
//     
//     
//     
//     
//    public List<String> getAllProdutos()
//    {
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery("SELECT p.designacao FROM Produtos p  ORDER BY p.designacao ");            
//            List<String>  lista =    query.getResultList();          
//            lista.add(0, "-- Seleccione --");            
//            return lista;
//            
// 
//    }
//
//    public List<Produtos> getProdutoByDesgnacao(String  designacao )
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT p.designacao FROM Produtos p WHERE  p.designacao LIKE :designacao ORDER BY p.designacao")
//                .setParameter("designacao",designacao +"%");       
//        List<Produtos> result = query.getResultList();
//        em.close();       
//        if( result!=null )
//                return result;
//        return null;
//    }
//     
//    
//    public int getIdProdutoByNome(String designacao)
//    {
//           
//        System.out.println("NOME DO PRODUTO " +designacao);
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT p FROM Produtos p WHERE p.designacao = :designacao")
//                .setParameter("designacao", designacao);
//      
//        List<Produtos> result = query.getResultList();
//         em.close();
//        if( !result.isEmpty() )
//                return result.get(0).getPkProduto();
//        return 0;
//        
//    }
//    
//    public List<Produtos> getAllProdutosByTipoProdutos(int pkTipoPrato) {
//        
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT s FROM Produtos s WHERE s.fkTipoProduto.pkTipoProdutos = :pkTipoProdutos ORDER BY s.designacao ASC")
//                .setParameter("pkTipoPrato", pkTipoPrato);       
//        List<Produtos> lista = query.getResultList();
//        
//        if ( !lista.isEmpty() ) {
//               return lista;
//        }
//        return null;
//        
//    }
//    
//         
//    public int getIdByDescricao (String designacao) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT f.pkProduto FROM Produtos f WHERE f.designacao = :designacao")
//                    .setParameter("designacao", designacao);
//            
//            List result = query.getResultList();
//            
//            if( result!= null )
//            {
//              
//                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
//            }
//            return 0;
//            
//    }
//    
//    
//      public  Vector <String>  getDesigncaoByIdTipoProduto(int idTipoProduto) 
//     {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT p.designacao FROM Produtos p WHERE p.fkTipoProduto.pkTipoProdutos = :idTipoProduto")
//                    .setParameter("idTipoProduto", idTipoProduto);
//              Vector<String>  result  =  (Vector)query.getResultList();
//            return result;
//    }
    public List<TbItemPedidos> getAllPedidosNovos()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT q FROM TbItemPedidos q" );
        //Query query = em.createQuery("SELECT q FROM TbItemPedidos q WHERE q.statusPedido = false");

        List<TbItemPedidos> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public int getLastItem()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(i.pkItemPedidos)  FROM TbItemPedidos  i" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public int getQunatidades( int id_produto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  SUM(i.qtd) AS QTD FROM TbItemPedidos i GROUP BY i.fkProdutos.codigo" )
                .setParameter( "id_produto", id_produto );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public long getAllQtd( int id_produto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  SUM(i.qtd)  FROM TbItemPedidos  i WHERE i.fkProdutos.codigo = :id_produto AND i.statusConvertido = false GROUP BY i.fkProdutos.codigo" )
                .setParameter( "id_produto", id_produto );

        List<Long> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public long getQtdItensByIdPedido( int pk_pedido )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT COUNT(i) AS QTD FROM TbItemPedidos i WHERE i.fkPedidos.pkPedido = :pk_pedido AND i.statusConvertido = false" )
                .setParameter( "pk_pedido", pk_pedido );

        List<Long> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista.get( 0 );
        }
        return 0;
    }

    public static double getQtdItensByIdPedidoTradicional( int idProduto, BDConexao conexao )
    {
        String sql = "SELECT SUM(qtd) AS soma_qtd FROM tb_item_pedidos WHERE fk_produtos = " + idProduto + " AND " + " status_convertido = 'false' GROUP BY fk_produtos";

        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getDouble( "soma_qtd" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0.0;
    }

    public Integer criarComProcedimento( TbItemPedidos itemPedido, BDConexao conexao )
    {
        String inserirVendaQuery = String.format( "select ITEM_PEDIDO_CRIAR ( %d, %d, '%s', %d, %d, '%s', %d, %d, '%s', '%s', '%s' ) as ID",
                itemPedido.getFkLugares().getPkLugares(),
                itemPedido.getFkProdutos().getCodigo(),
                itemPedido.getQtd(),
                itemPedido.getFkPedidos().getPkPedido(),
                itemPedido.getStatusConvertido() ? 1 : 0,
                String.valueOf( itemPedido.getTotalItem() ),
                itemPedido.getStatusEnviado() ? 1 : 0,
                itemPedido.getStatusEfectuado() ? 1 : 0,
                MetodosUtil.getDataBancoFull( itemPedido.getDataEntrega() ),
                itemPedido.getObs(),
                itemPedido.getPreco()
        );
        System.err.println( "Query: " + inserirVendaQuery );
        ResultSet resultSet = conexao.executeQuery( inserirVendaQuery );

        try
        {
            if ( resultSet.next() )
            {
                return resultSet.getInt( "ID" );

            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;

    }

    public Integer criarComProcedimentoLugar( TbItemPedidos itemPedido, BDConexao conexao )
    {
        String inserirVendaQuery = String.format( "select ITEM_PEDIDO_CRIAR ( %d, %d, '%s', %d, %d, '%s', %d, %d, '%s', '%s', '%s' ) as ID",
                1,
                itemPedido.getFkProdutos().getCodigo(),
                itemPedido.getQtd(),
                itemPedido.getFkPedidos().getPkPedido(),
                itemPedido.getStatusConvertido() ? 1 : 0,
                String.valueOf( itemPedido.getTotalItem() ),
                itemPedido.getStatusEnviado() ? 1 : 0,
                itemPedido.getStatusEfectuado() ? 1 : 0,
                MetodosUtil.getDataBancoFull( itemPedido.getDataEntrega() ),
                itemPedido.getObs(),
                itemPedido.getPreco()
        );
        System.err.println( "Query: " + inserirVendaQuery );
        ResultSet resultSet = conexao.executeQuery( inserirVendaQuery );

        try
        {
            if ( resultSet.next() )
            {
                return resultSet.getInt( "ID" );

            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;

    }

    public static int getLastIdItemPedidoByIdPedido( int idPedido, BDConexao conexao )
    {
        int last_id = 0;
        String query = "SELECT MAX(pk_item_pedidos) AS last_id FROM tb_item_pedidos WHERE fk_pedios = " + idPedido;

        ResultSet rs = conexao.executeQuery( query );

        try
        {
            if ( rs.next() )
            {
                last_id = rs.getInt( "last_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return last_id;

    }

    public static void alterar_item_pedidos( TbItemPedidos item, BDConexao conexao )
    {
        String sql = "UPDATE tb_item_pedidos SET fk_pedidos = " + item.getFkPedidos().getPkPedido() + " WHERE pk_item_pedidos = " + item.getPkItemPedidos();
        conexao.executeUpdate( sql );
    }

    public static void alterar_item_pedidios_obs( int id, String obs, BDConexao conexao )
    {
        String sql = "UPDATE tb_item_pedidos SET obs = '" + obs + "' WHERE pk_item_pedidos = " + id;
        conexao.executeUpdate( sql );
    }

    public static void alterar_item_pedidios_data_entrega( int id, Date data, BDConexao conexao )
    {
        String sql = "UPDATE tb_item_pedidos SET data_entrega = '" + MetodosUtil.getDataBancoFull( data ) + "' WHERE pk_item_pedidos = " + id;
        conexao.executeUpdate( sql );
    }

    public static void deletar_item_perdido( int id, int idPedido, int idLugar, BDConexao conexao )
    {
        String sql = "DELETE FROM tb_item_pedidos WHERE fk_produtos = " + id + " AND fk_pedidos = " + idPedido + " AND fk_lugares = " + idLugar;
        conexao.executeUpdate( sql );
    }

    public static boolean existeProdutoIemPedidos( int idProduto, BDConexao conexao )
    {
        String sql = "SELECT * FROM tb_item_pedidos WHERE fk_produtos = " + idProduto;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return true;
            }

        }
        catch ( Exception e )
        {
            System.err.println( "Error: " + e.getLocalizedMessage() );
        }

        return false;
    }

}
