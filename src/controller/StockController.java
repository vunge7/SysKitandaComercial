/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.StockModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class StockController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public StockController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
   
    
    public boolean operacao (int operacao, StockModelo stockModelo)
    { 
    
        sql = new StringBuffer();

        
        
        sql.append(" CALL DM_PROCEDIMENTO_STOCK(");
        
        sql.append(stockModelo.getCodigo_produto());
        sql.append(",'");
        sql.append(stockModelo.getDataEntrada());
        sql.append("',");
        sql.append(stockModelo.getQuantidade_existente());
        sql.append(",");
        sql.append(stockModelo.getQuantidade_antiga());
        sql.append(",'");
        sql.append(stockModelo.getStatus());
        sql.append("',");
        sql.append(stockModelo.getPreco_venda());
        sql.append(",");
        sql.append(stockModelo.getPreco_venda_fabrica());
        sql.append(",");
        sql.append(stockModelo.getQuantidadeCritica());
        sql.append(",");
        sql.append(stockModelo.getQuantidadeBaixa());
        sql.append(",");
        sql.append(stockModelo.getCodigo() );
        sql.append(",");
        sql.append(stockModelo.getCod_armazem());
        sql.append(",");
        sql.append(operacao);
        sql.append(")");
        
        actualizar_preco_venda( stockModelo.getCodigo_produto() ,  stockModelo.getPreco_venda() );
        
     

        return conexao.executeUpdate(sql.toString());
       
    }
   
    
    
     
     public  void actualizar_preco_venda(int codigo, double preco_venda) {
         
        String sql = "UPDATE tb_produto SET preco_venda =  " +preco_venda +" WHERE codigo = " +codigo;
        
        conexao.executeUpdate(sql);
       
    }
     
    
    public StockModelo getStockProduto (int codigo, int cod_armazem) throws SQLException
    {   
      
        StockModelo modelo = new StockModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_STOCK(");
        sql.append(codigo);
        sql.append(",");
         sql.append(cod_armazem);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        if (rs.next() ) 
        {
            
            modelo.setCodigo_produto(rs.getInt("cod_produto_codigo"));
            modelo.setStatus(rs.getString("status"));
            modelo.setDataEntrada(rs.getString("dataEntrada"));
            modelo.setQuantidade_existente(rs.getInt("quantidade_existente"));       
            modelo.setQuantidade_antiga(rs.getInt("quantidade_antiga"));       
            modelo.setPreco_venda(rs.getFloat("preco_venda"));       
            modelo.setPreco_venda_fabrica(rs.getFloat("preco_venda_fabrica"));       
            modelo.setQuantidadeCritica(rs.getInt("quant_critica"));       
            modelo.setQuantidadeBaixa(rs.getInt("quant_baixa"));       
            modelo.setCod_armazem( rs.getInt("cod_armazem") );       
      
                    
        }
        return modelo;
    }
    
    
    
    public boolean exist(int codigo) throws SQLException
    {
          ResultSet rs = null;
          StringBuffer sql = new StringBuffer();
          
          sql.append("CALL DM_EXIST_PRDUTO_STOCK(");
          sql.append(codigo);
          sql.append(")");
          
          rs = conexao.executeQuery(sql.toString());
          
          return rs.next();
          
          
    }
    
    
    
    public boolean exist_armazem(int codigo, int cod_armazem) throws SQLException
    {
          ResultSet rs = null;
          StringBuffer sql = new StringBuffer();
          
          sql.append("CALL DM_EXIST_PRDUTO_ARMAZEM_STOCK(");
          sql.append( codigo );
          sql.append(",");
          sql.append( cod_armazem );
          sql.append(")");
          
          rs = conexao.executeQuery(sql.toString());
          
          return rs.next();
          
          
    }
    
}

