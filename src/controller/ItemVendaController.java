/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.ClienteModelo;
import modelo.ItemVendaModelo;
import modelo.ProdutoModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ItemVendaController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public ItemVendaController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
   
   
    
    
    public boolean operacao (int operacao, ItemVendaModelo itemVendaModelo)
    {    
        
        sql = new StringBuffer();
       
        /*
         *  
     $codvenda INTEGER ,
     $codproduto INTEGER,
     $total DOUBLE,
     $quantidade  INTEGER,
     $Codigo INT,
     $operacao INT
 
         */
           
        sql.append(" CALL DM_PROCEDIMENTO_ITEM_VENDA(");
        sql.append(itemVendaModelo.getCodigo_venda() );
        sql.append(",");
        sql.append(itemVendaModelo.getCodigo_produto());
        
        sql.append(",");
        sql.append(itemVendaModelo.getFk_preco());
      
        sql.append(",");
        sql.append(itemVendaModelo.getTotal());
      
        sql.append(",");
         
        sql.append(itemVendaModelo.getQuantidade()       );
                 
        sql.append(",");
        sql.append(itemVendaModelo.getCodigo());
        sql.append(",");

        sql.append(operacao);
        sql.append(")");

        System.out.println(sql);

        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    
    
    public int getCodigo(String tabela, String campo_proucurado, String descrisao) throws SQLException
    {
        sql = new StringBuffer();
        
        
        sql.append("CALL DM_PROCEDIMENTO_GET_ID_BY_NAME_GERAL('");
        sql.append(tabela);
        sql.append("','");
        sql.append(campo_proucurado);
        sql.append("','");
         sql.append(descrisao);
        sql.append("')");
        
        ResultSet rs = conexao.executeQuery(sql.toString());
        
        int codigo = 0;
        
        if(rs.next())
               codigo = rs.getInt("codigo");
        
        return codigo;
        
       
    }

  
    public ItemVendaModelo getCliente (int codigo) throws SQLException
    {   
      
        
        
        /*
         * $codvenda VARCHAR(45) ,
     $codproduto VARCHAR(45) ,
     $Codigo INT,
     $operacao INT
         * 
         */
        ItemVendaModelo itemVendaModelo = new ItemVendaModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append("CALL DM_PROCEDIMENTO_ITEM_VENDA(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        
        if (rs.next()) {
    
         
            
            
          
          
            
            itemVendaModelo.setCodigo_venda(rs.getInt("codigo_venda"));
            itemVendaModelo.setCodigo_produto(rs.getInt("codigo_produto"));
            itemVendaModelo.setTotal(rs.getInt("total"));
            itemVendaModelo.setQuantidade(rs.getInt("quantidade"));
                      
        }
        return itemVendaModelo;
    }
    
}

