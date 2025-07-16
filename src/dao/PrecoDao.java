/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbPrecoJpaController;

import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Dallas
 */
public class PrecoDao extends TbPrecoJpaController
{

    public PrecoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public TbPreco getPrecoByDescricao( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbPreco  p WHERE p.designacao = :nome" )
                .setParameter( "nome", nome );

        List<TbPreco> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbPreco getPrecoByIdProduto( int idProduto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbPreco  p WHERE p.fkProduto.codigo = :idProduto" )
                .setParameter( "idProduto", idProduto );

        List<TbPreco> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public TbPreco getLastPrecoByIdProduto( int idProduto )
    {
        return findTbPreco( getUltimoIdPrecoByIdProduto( idProduto ) );
    }

    public Integer getUltimoIdPrecoByIdProduto( int idProduto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.pkPreco) FROM TbPreco p WHERE p.fkProduto.codigo = :idProduto" )
                .setParameter( "idProduto", idProduto );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public void procedimento_salvar_preco( TbPreco preco )
    {

        try
        {
            create( preco );
            //    JOptionPane.showMessageDialog(null, "Da");
        }
        catch ( Exception e )
        {
        }

    }

    private void transferencia_preco_compra_de_produto_and_preco_venda_de_stock_para_preco()
    {
//          EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em; 
//          ProdutoDao produtoDao = new ProdutoDao(emf);
//          StockDao stockDao = new StockDao(emf);
//          UsuarioDao usuarioDao = new UsuarioDao(emf);
//          List<TbProduto> list = produtoDao.fi;
//          TbPreco preco;
//          TbProduto produto;
//          TbStock stock;        
//          double percentagem_ganho = 0, diferenca = 0;
//          
//          for (int i = 0; i < list.size(); i++)           
//          { 
//              produto = list.get(i);
//              if ( stockDao.exist_produto_stock(   produto.getCodigo()    , 1)    ) 
//              {
//                    
//                     stock = stockDao.get_stock_by_id_produto_and_id_armazem( produto.getCodigo() , 1);                              
//                     preco = new TbPreco();         
//                     preco.setData( new Date() );
//                     preco.setHora( new Date() );
//                     preco.setPrecoCompra(  produto.getPreco()  );
//                     preco.setPrecoVenda(  stock.getPrecoVenda() );
//                     preco.setFkProduto(  produto );
//                     preco.setFkUsuario( usuarioDao.findTbUsuario(15));                     
//                     diferenca =  preco.getPrecoVenda() - preco.getPrecoCompra();                     
//                     //percentagem_ganho = (100*diferenca) / (2*preco.getPrecoCompra() );                     
//                     percentagem_ganho = (100*diferenca) / (2*preco.getPrecoCompra() );                     
//                    // preco.setPercentagemGanho(percentagem_ganho*2);
//                     preco.setPercentagemGanho(0.0);
//                     try {
//                           create(preco);
//                  } catch (Exception e) {
//                      e.printStackTrace();
//                  }
//                     
//              }
//  
//            
//          }
//    
//    
    }

    public Integer getUltimoIdPrecoByIdProduto( int idProduto, double qtd )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.pkPreco) FROM TbPreco p WHERE   p.fkProduto.codigo = :idProduto AND  :qtd BETWEEN p.qtdBaixo AND p.qtdAlto " )
                .setParameter( "idProduto", idProduto )
                .setParameter( "qtd", qtd );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public Integer getUltimoIdPrecoGrosso( int idProduto, long qtd )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.pkPreco) FROM TbPreco p WHERE   p.fkProduto.codigo = :idProduto AND p.qtdAlto = :qtd  " )
                .setParameter( "idProduto", idProduto )
                .setParameter( "qtd", qtd );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public Integer getUltimoIdPrecoRetalho( int idProduto, long qtd )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(p.pkPreco) FROM TbPreco p WHERE   p.fkProduto.codigo = :idProduto AND p.qtdAlto < :qtd  " )
                .setParameter( "idProduto", idProduto )
                .setParameter( "qtd", qtd );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public boolean exist_preco_produto( int idProduto )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbPreco s WHERE  s.fkProduto.codigo = :codigo " )
                .setParameter( "codigo", idProduto );

        List<TbStock> result = query.getResultList();
        em.close();

        return !result.isEmpty();

    }

    public static void main( String[] args )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        PrecoDao precoDao = new PrecoDao( emf );

//          TbPreco preco =    precoDao.findTbPreco(precoDao.getUltimoIdPrecoByIdProduto(5));
//          
//          System.out.println("TbPreco Compra " +preco.getPrecoCompra());
//          System.out.println("Percentagem Ganho " +preco.getPercentagemGanho()   +"%");
//          System.out.println("TbPreco TbVenda " +preco.getPrecoVenda());
        precoDao.transferencia_preco_compra_de_produto_and_preco_venda_de_stock_para_preco();
    }

    public List<TbPreco> getAllPrecos( )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbPreco  p GROUP BY p.fkProduto.codigo" );

        return  query.getResultList();
        
    }
    
         public int getIdByDescricao1 (String designacao) 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.pkPrato FROM Pratos s WHERE s.designacao = :designacao")
                    .setParameter("designacao", designacao);
            
            List result = query.getResultList();
            
            if( result!= null )
            {
              
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            }
            return 0;
            
    }
    
    public int getIdByDouble( Double precoVenda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkPreco FROM TbPreco s WHERE s.precoVenda = :precoVenda" )
                .setParameter( "precoVenda", precoVenda );

        List result = query.getResultList();

        if ( result != null )
        {

            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }
}
