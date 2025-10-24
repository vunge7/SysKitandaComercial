/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.FornecedorModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FornecedorController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public FornecedorController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
   
   
    public boolean exist(String userName, String senha) throws SQLException
    {
          ResultSet rs = null;
          StringBuffer sql = new StringBuffer();
          
          sql.append("CALL DM_EXIST_LOGIN('");
          sql.append(userName);
          sql.append("','");
          sql.append(senha);
          sql.append("')");
          
          rs = conexao.executeQuery(sql.toString());
          
          return rs.next();
          
          
    }
    
    public boolean operacao (int operacao, FornecedorModelo fornecedorModelo)
    {    
        
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_FORNECEDOR('");
        sql.append(fornecedorModelo.getNome());
        sql.append("','");
        sql.append(fornecedorModelo.getTelefone());
        sql.append("','");
        sql.append(fornecedorModelo.getEmail());
        sql.append("','");
        sql.append(fornecedorModelo.getSite());
        sql.append("','");
        sql.append(fornecedorModelo.getEndereco());
        sql.append("',");
        sql.append(fornecedorModelo.getCodigo());
        sql.append(",");
        sql.append(operacao);
        sql.append(")");
        
        
        
       
        
        System.out.println(sql);
        
        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    
    
    public int getCodigo(String nome) throws SQLException
    {
        sql = new StringBuffer();
        
        sql.append("CALL DM_PROCEDIMENTO_GET_ID_BY_NAME_FORNECEDOR('");
        sql.append(nome);
        sql.append("')");
        
        ResultSet rs = conexao.executeQuery(sql.toString());
        
        int codigo = 0;
        
        if(rs.next())
               codigo = rs.getInt("codigo");
        
        return codigo;
        
       
    }
    
    
    
    public FornecedorModelo getFornecedor (int codigo) throws SQLException
    {   
      
        FornecedorModelo modelo = new FornecedorModelo();
        ResultSet rs = null;

        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_FORNECEDOR(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        if (rs.next()) {
            modelo.setNome(rs.getString("nome"));
            modelo.setTelefone(rs.getString("telefone"));
            modelo.setEmail(rs.getString("email"));
            modelo.setSite(rs.getString("site"));
            modelo.setTelefone(rs.getString("telefone"));
            modelo.setEnderreco(rs.getString("endereco"));
            
        }
        return modelo;
    }
    
}

