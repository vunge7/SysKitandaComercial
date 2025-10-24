/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.EntradaModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EntradaController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public EntradaController(BDConexao conexao) {
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
    
    public boolean operacao (int operacao, EntradaModelo entradaModelo, boolean stocavel)
    {    
        
        sql = new StringBuffer();

        sql.append(" CALL DM_PROCEDIMENTO_ENTRADA('");
        sql.append(entradaModelo.getData_entrada());
        sql.append("',");
        sql.append(entradaModelo.getIdProduto());
        sql.append(",");
        sql.append(entradaModelo.getQuantidade());
        sql.append(",");
        sql.append(entradaModelo.getIdUsuario());
        sql.append(",");
        sql.append(entradaModelo.getIdEntrada());
         sql.append(",");
        sql.append(entradaModelo.getIdArmazemFK());
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
    
    
    public EntradaModelo getEntrada (int codigo)
    {   
      
        EntradaModelo modelo = new EntradaModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_ENTRADA(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        
        try {
            
        
        rs = conexao.executeQuery(sql.toString());

        if (rs.next()) {
            
            modelo.setIdEntrada(rs.getInt("idEntrada"));
            modelo.setQuantidade( rs.getInt("quantidade"));
            modelo.setIdProduto(rs.getInt("idProduto"));
            modelo.setIdUsuario(rs.getInt("idUsuario"));
            modelo.setData_entrada( rs.getString("data_entrada"));
            modelo.setIdArmazemFK( rs.getInt("idArmazemFK"));
           
          
        }
     
        
        
        
        
        } catch (Exception e) {
        }
        
        
              return modelo;
     
    }
    
    
}

