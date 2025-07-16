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
public class ClienteController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public ClienteController(BDConexao conexao) {
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
    
    public boolean operacao (int operacao,ClienteModelo clienteModelo)
    {    
        
        sql = new StringBuffer();
        
       
        sql.append(" CALL DM_PROCEDIMENTO_CLIENTE('");
        sql.append(clienteModelo.getNome() );
        sql.append("',");
        sql.append(clienteModelo.getCredito());
        sql.append(",");
         sql.append(clienteModelo.getCodigo());
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

  
    public ClienteModelo getCliente (int codigo) throws SQLException
    {   
      
        ClienteModelo clienteModelo = new ClienteModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append("CALL DM_PROCEDIMENTO_GET_CLIENTE(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        
        if (rs.next()) {
            
            clienteModelo.setNome(rs.getString("nome"));
            //clienteModelo.setCredito(rs.getFloat("credito"));
            
        }
        return  clienteModelo;
    }
    
}

