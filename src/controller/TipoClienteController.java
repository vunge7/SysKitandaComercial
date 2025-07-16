/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.FornecedorModelo;
import modelo.TipoClienteModelo;
import modelo.TipoProdutoModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Martinho Luis
 */
public class TipoClienteController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public TipoClienteController(BDConexao conexao) {
        this.conexao = conexao;
    }


      
    public boolean operacao (int operacao, TipoClienteModelo tipoClienteModelo)
    {  

        
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_CLIENTE('");
        sql.append(tipoClienteModelo.getNome());
        sql.append("','");
        sql.append(tipoClienteModelo.getMorada());
        sql.append("','");
        sql.append(tipoClienteModelo.getTelefone());
        sql.append("',");
        sql.append(tipoClienteModelo.getCodigo());
        sql.append(",");
        sql.append(operacao);
        sql.append(")");
      
        System.out.println(sql);
        
        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    public int getCodigo(String nome) throws SQLException
    {
        sql = new StringBuffer();
        
        sql.append("CALL DM_PROCEDIMENTO_GET_CLIENTE('");
        sql.append(nome);
        sql.append("')");
        
        ResultSet rs = conexao.executeQuery(sql.toString());
        
        int codigo = 0;
        
        if(rs.next())
               codigo = rs.getInt("codigo");
        
        return codigo;
        
       
    }
    

    
    public TipoClienteModelo getTipoClienteModelo (int codigo) throws SQLException
    {   
      
        TipoClienteModelo modelo = new TipoClienteModelo();
        ResultSet rs = null;

        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_CLIENTE(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        if (rs.next()) {
            modelo.setNome(rs.getString("nome"));
            modelo.setMorada(rs.getString("morada"));
            modelo.setTelefone(rs.getString("telefone"));
           
        }
        return modelo;
    }
    
}

