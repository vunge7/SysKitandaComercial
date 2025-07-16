/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.ProdutoModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProdutoController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public ProdutoController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
   
   
    public boolean exist(String descricao) throws SQLException
    {
          ResultSet rs = null;
          StringBuffer sql = new StringBuffer();
          
          sql.append("CALL DM_EXIST_PRDUTO('");
          sql.append(descricao);
          sql.append("')");
          
          rs = conexao.executeQuery(sql.toString());
          
          return rs.next();
          
          
    }
    
    public boolean operacao (int operacao, ProdutoModelo produtoModelo, boolean stocavel)
    {    
        
        sql = new StringBuffer();
       
        
        
        
//       $designacao VARCHAR(45) ,
//     $preco FLOAT ,
//
//     $cod_Tipo_Produto INT,
//     $cod_fornecedores INT,
//     $data_fabrico DATE,
//     $data_expiracao DATE,
//     $data_entrada DATE,
//     codBarra BIGINT,
//     $status VARCHAR(10),
//
//     $Codigo INT,
//     $operacao INT
//        
        
        
        sql.append(" CALL DM_PROCEDIMENTO_PRODUTO('");
        sql.append(produtoModelo.getDesignacao());
        sql.append("',");
        sql.append(produtoModelo.getPreco());
        sql.append(",");
        sql.append(produtoModelo.getCod_Tipo_Produto());
        sql.append(",");
        sql.append(produtoModelo.getCod_fornecedores());
        sql.append(",'");
        sql.append(produtoModelo.getData_fabrico());
        sql.append("','");
        sql.append(produtoModelo.getData_expiracao());
        sql.append("','");
        sql.append(produtoModelo.getData_entrada());
        sql.append("','");
        sql.append(produtoModelo.getCod_barra());
        sql.append("','");
        sql.append(produtoModelo.getStatus());
        sql.append("','");
        sql.append( stocavel(stocavel) );
        sql.append("',");
        
        sql.append(produtoModelo.getCodigo());
        sql.append(",");
        sql.append(operacao);
        sql.append(")");
        
        
        System.out.println(sql);

        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    public String stocavel(boolean stocavel)
    {
        if(stocavel) return "true";
        else return "false";
            
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

  
    
    
    public ProdutoModelo getProduto (int codigo) throws SQLException
    {   
      
        ProdutoModelo modelo = new ProdutoModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_PRODUTO(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        
        if (rs.next()) {
            modelo.setDesignacao(rs.getString("designacao"));
            modelo.setStatus(rs.getString("status"));
            modelo.setData_fabrico(rs.getString("data_fabrico"));
            modelo.setData_expiracao(rs.getString("data_expiracao"));
            modelo.setCod_barra(rs.getString("codBarra"));
            modelo.setData_entrada(rs.getString("data_entrada"));
            modelo.setPreco(rs.getFloat("preco"));
            modelo.setCodigo(rs.getInt("Codigo"));
            modelo.setCod_Tipo_Produto(rs.getInt("cod_Tipo_Produto"));
            modelo.setCod_fornecedores(rs.getInt("cod_fornecedores"));
            modelo.setStocavel(rs.getString("stocavel"));
        }
        return modelo;
    }
    
    
    
    
    public ProdutoModelo getProdutoByCodigoBarra (long codigo_barra) throws SQLException
    {   
      
        ProdutoModelo modelo = new ProdutoModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append(" CALL DVML_PROCEDIMENTO_GET_PRODUTO_BY_CODIGO_BARRA(");
        sql.append(codigo_barra);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        
        if (rs.next()) {
            modelo.setDesignacao(rs.getString("designacao"));
            modelo.setStatus(rs.getString("status"));
            modelo.setData_fabrico(rs.getString("data_fabrico"));
            modelo.setData_expiracao(rs.getString("data_expiracao"));
            modelo.setCod_barra(rs.getString("codBarra"));
            modelo.setData_entrada(rs.getString("data_entrada"));
            modelo.setPreco(rs.getFloat("preco"));
            modelo.setCodigo(rs.getInt("Codigo"));
            modelo.setCod_Tipo_Produto(rs.getInt("cod_Tipo_Produto"));
            modelo.setCod_fornecedores(rs.getInt("cod_fornecedores"));
            modelo.setStocavel(rs.getString("stocavel"));
        }
        return modelo;
    }
}

