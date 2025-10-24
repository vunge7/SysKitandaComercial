/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbPedidoJpaController;
import entity.TbPedido;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;

/**
 *
 * @author Dallas
 */
public class PedidoDao extends TbPedidoJpaController
{

    public PedidoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> getAllPedido()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.dataPedido FROM TbPedido  c" );

        Vector<String> result = (Vector) query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<TbPedido> buscaTodosPedido()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM TbPedido m" );
        return query.getResultList();
    }

    public int getLastPedidoByDefignacaoMesa( String mesa )
    {

        System.out.println( "DESIGNACAO " + mesa );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.pkPedido) FROM TbPedido m WHERE m.fkMesas.designacao = :mesa AND m.statusPedido = TRUE" )
                .setParameter( "mesa", mesa );

        List<Integer> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista.get( 0 );

        }
        return 0;
    }

    public int getLastPedidoByDefignacaoMesaSemStatus( String mesa )
    {

        System.out.println( "DESIGNACAO " + mesa );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.pkPedido) FROM TbPedido m WHERE m.fkMesas.designacao = :mesa" )
                .setParameter( "mesa", mesa );

        List<Integer> lista = query.getResultList();

        try
        {
            if ( !lista.isEmpty() )
            {
                return lista.get( 0 );
            }

        }
        catch ( Exception e )
        {
            return 0;
        }

        return 0;

    }

    public int getUltimPedidoAgora( int pkMesas )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(a.pkPedido) FROM TbPedido a WHERE a.fkMesas.pkMesas = :pkMesas " )
                .setParameter( pkMesas, pkMesas );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public int getUltimoPedido()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.pkPedido) FROM TbPedido p " );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            try
            {
                return result.get( 0 );
            }
            catch ( Exception e )
            {
                return 0;
            }

        }
        return 0;

    }

    public int getLastPedidoByDefignacaoMesaFALSE( String mesa )
    {

        System.out.println( "DESIGNACAO " + mesa );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.pkPedido) FROM TbPedido m WHERE m.fkMesas.designacao = :mesa AND m.statusPedido = FALSE" )
                .setParameter( "mesa", mesa );

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

    public int getLastPedidoByDefignacaoMesaLugarFALSE( int pkLugares )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT MAX(m.pkItemPedidos) FROM TbItemPedidos m WHERE m.fkPedidos.fkMesas.designacao = :mesa AND m.fkLugares.designacao = :lugar AND m.fkPedidos.statusPedido = FALSE" )
        Query query = em.createQuery( "SELECT MAX(m.pkItemPedidos) FROM TbItemPedidos m WHERE  m.fkLugares.pkLugares = :pkLugares AND m.fkPedidos.statusPedido = FALSE" )
                .setParameter( "pkLugares", pkLugares );

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

    public int getLastPedidoByMesa( String mesa )
    {

        System.out.println( "DESIGNACAO " + mesa );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(m.pkPedido) FROM TbPedido m WHERE m.fkMesas.designacao = :mesa" )
                .setParameter( "mesa", mesa );

        List<Integer> lista = query.getResultList();

        if ( !lista.isEmpty() )
        {
            return lista.get( 0 );

        }
        return 0;
    }

    public List<TbPedido> getAll()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbPedido  c" );

        List<TbPedido> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }
//     
//    public TbPedido getPedidoByDescricao(String descricao)
//    {
//           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT c FROM TbPedido  ccao")
//                .setParameter("designacao", descricao );
//               
//        List<TbPedido> result = query.getResultList();
//        em.close();
//       
//        if( result!=null )
//                return result.get(0);
//        return null;
//    }

//    public boolean comparar (String x)
//    {
//            EntityManager em = getEntityManager();
//            
//            Query query = em.createQuery("SELECT s FROM TbPedido s WHERE s.designacao = :x")
//                    .setParameter("x", x);
//            
//            List<TbPedido> result = query.getResultList();
//            
//            return  !result.isEmpty();   
//        
//    
//    
//    }
//    
//    public TipoProduto getTipoProdutoByDescricao(String descricao)
//    {           
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT a FROM TipoProduto  a WHERE a.designacao = :designacao")
//                .setParameter("designacao", descricao );
//               
//        List<TipoProduto> result = query.getResultList();
//        em.close();
//       
//        if( result!=null )
//                return result.get(0);
//        return null;
//        
//    }
//    
//       public Vector <String > buscaTodos () 
//     {         
//           EntityManager em = getEntityManager();
//           Query query = em.createQuery ("SELECT tp.designacao FROM TbPedido tp  ");
//           Vector<String>  result  =   ( Vector )query.getResultList();                    
//           return  result;
//    }
//     
//   
//     
//
//    public Vector <TbPedido > buscaTodasPedidoProdutos() 
//    {         
//           EntityManager em = getEntityManager();
//           Query query = em.createQuery ("SELECT tp FROM TbPedido tp WHERE tp.designacao <> 'Serviços'   ORDER BY tp.designacao   ASC");
//           Vector<TbPedido>  result  =   ( Vector )query.getResultList();                    
//           return  result;
//    }
//    
//    public boolean exist_descricao(String designacao)
//    {
//             EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT e FROM TbPedido e WHERE e.designacao = :designacao ")
//                    .setParameter("designacao", designacao);
//            
//            List<TbPedido> result = query.getResultList();
//         
//            if( !result.isEmpty() )
//            {
//                 return true;
//            }
//               
//            return false;
//    
//    }
//    
//    public int getIdByDescricao(String designacao)
//    {
//             EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT m FROM TbPedido m WHERE m.designacao = :designacao ")
//                    .setParameter("designacao", designacao);
//            
//            List<TbPedido> result = query.getResultList();
//         
//            if( !result.isEmpty() )
//            {
//                 return result.get(0).getPkPedido();
//            }     
//               
//            return 0;
//    
//    }
// 
    public List<TbPedido> getAllPedidosNovos()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT q FROM TbPedido q WHERE q.statusPedido = false" );

        List<TbPedido> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public Integer criarComProcedimentos( TbPedido pedido, BDConexao conexao )
    {
        String insertQuery = String.format( "select PEDIDO_CRIAR ( '%s', '%s', %d, %d ) as ID",
                new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( pedido.getDataPedido() ),
                new SimpleDateFormat( "hh:mm:ss" ).format( pedido.getHoraPedido() ),
                pedido.getFkMesas().getPkMesas(),
                pedido.getStatusPedido() ? 1 : 0
        );

        ResultSet resultSet = conexao.executeQuery( insertQuery );

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

    public Integer criarComProcedimentos1( TbPedido pedido, BDConexao conexao )
    {
        String insertQuery = String.format( "select PEDIDO_CRIAR ( '%s', '%s', %d ) as ID",
                new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" ).format( pedido.getDataPedido() ),
                new SimpleDateFormat( "hh:mm:ss" ).format( pedido.getHoraPedido() ),
                pedido.getStatusPedido() ? 1 : 0
        );

        ResultSet resultSet = conexao.executeQuery( insertQuery );

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

    public static boolean eliminarPedido( TbPedido pedido, BDConexao conexao )
    {
        String sql_item = "DELETE FROM tb_item_pedidos WHERE fk_pedidos = " + pedido.getPkPedido();
        String sql_pedido = "DELETE FROM tb_pedido WHERE pk_pedido = " + pedido.getPkPedido();

        try
        {
            if ( conexao.executeUpdate( sql_item ) )
            {
                if ( conexao.executeUpdate( sql_pedido ) )
                {
                    return true;
                }
                return true;
            }
        }
        catch ( Exception e )
        {
        }

        return false;
    }

    public static boolean eliminarPedidoByLugar( TbPedido pedido, int idLugar, BDConexao conexao )
    {
        String sql_item = "DELETE FROM tb_item_pedidos WHERE fk_pedidos = " + pedido.getPkPedido() + " AND fk_lugares = " + idLugar;

        try
        {
            if ( conexao.executeUpdate( sql_item ) )
            {

                return true;
            }
        }
        catch ( Exception e )
        {
        }

        return false;
    }

}
