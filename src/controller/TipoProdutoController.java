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
import modelo.TipoProdutoModelo;
import modelo.UsuarioModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TipoProdutoController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public TipoProdutoController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
   
      
    public boolean operacao (int operacao, TipoProdutoModelo tipoProdutoModelo)
    {    
        
        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_TIPO_PRODUTO('");
        sql.append(tipoProdutoModelo.getDesignacao());
        sql.append("',");
        sql.append(tipoProdutoModelo.getCodigo());
        sql.append(",");
        sql.append(operacao);
       
        sql.append(")");
      
        System.out.println(sql);
        
        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    public int getCodigo(String desiganacao) throws SQLException
    {
        sql = new StringBuffer();
        
        sql.append("CALL DM_PROCEDIMENTO_GET_ID_BY_NAME_T_PRODUTO('");
        sql.append(desiganacao);
        sql.append("')");
        
        ResultSet rs = conexao.executeQuery(sql.toString());
        
        int codigo = 0;
        
        if(rs.next())
               codigo = rs.getInt("codigo");
        
        return codigo;
        
       
    }
    
    public TipoProdutoModelo getTipoProdutoModelo (int codigo) throws SQLException
    {   
      
        TipoProdutoModelo modelo = new TipoProdutoModelo();
        ResultSet rs = null;

        sql = new StringBuffer();
        sql.append(" CALL DM_PROCEDIMENTO_GET_TIPO_PRODUTO(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        if (rs.next()) {
            modelo.setDesignacao(rs.getString("designacao"));
           
        }
        return modelo;
    }
    
}

