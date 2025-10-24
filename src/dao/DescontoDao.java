/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbDescontoJpaController;
import entity.TbDesconto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author mac
 */
public class DescontoDao extends TbDescontoJpaController
{
    
    public DescontoDao( EntityManagerFactory emf )
    {
        super( emf );
    }
    
    public List<TbDesconto> getAllDescontoByIdCliente( int id_cliente )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbDesconto  e WHERE e.fkCliente.codigo = :id_cliente ORDER BY e.fkProduto.designacao ASC" )
                .setParameter( "id_cliente", id_cliente );
        
        List<TbDesconto> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
        
    }
    
    public boolean exist_produto_cliente( int id_cliente, int id_produto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbDesconto  e WHERE e.fkCliente.codigo = :id_cliente AND e.fkProduto.codigo = :id_produto" )
                .setParameter( "id_cliente", id_cliente )
                .setParameter( "id_produto", id_produto );
        
        List<TbDesconto> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
        
    }
    
    public TbDesconto get_desconto_cliente_produto( int id_cliente, int id_produto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbDesconto  e WHERE e.fkCliente.codigo = :id_cliente AND e.fkProduto.codigo = :id_produto" )
                .setParameter( "id_cliente", id_cliente )
                .setParameter( "id_produto", id_produto );
        
        List<TbDesconto> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbDesconto();
        
    }
    
    public TbDesconto get_desconto_cliente_produto( int id_cliente, int id_produto, double qtd )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbDesconto  e WHERE e.fkCliente.codigo = :id_cliente AND e.fkProduto.codigo = :id_produto AND e.quantidade >= :qtd" )
                .setParameter( "id_cliente", id_cliente )
                .setParameter( "id_produto", id_produto )
                .setParameter( "qtd", qtd );
        
        List<TbDesconto> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        TbDesconto desconto = new TbDesconto();
        desconto.setValor( 0d );
        return desconto;
        
    }
    
    public TbDesconto get_desconto_fornecedor_produto( int id_fornecedor, int id_produto )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT e FROM TbDesconto  e WHERE e.fk.codigo = :id_fornecedor AND e.fkProduto.codigo = :id_produto" )
                .setParameter( "id_fornecedor", id_fornecedor )
                .setParameter( "id_produto", id_produto );
        
        List<TbDesconto> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbDesconto();
        
    }
    
}
