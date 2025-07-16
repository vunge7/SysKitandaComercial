/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import modelo.PagamentoVendaCreditoModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class PagamentoVendaCreditoController {
    
    private static String insert = "INSERT INTO tb_pagamento_credito(data_pagamento, valor, codigo_venda), "
            + " VALUES (?,?,?) ";
    
    
    private Connection conexao = null;
    private PreparedStatement comando = null;

    public PagamentoVendaCreditoController() {
    }
    
    
    public boolean salvar(PagamentoVendaCreditoModelo pagamentoVendaCreditoModelo){
        
        
        try {
            //abre a conexao
            conexao = BDConexao.getConnection();
            
            comando = conexao.prepareStatement(insert);
            //(data_pagamento, valor, codigo_venda)
            comando.setString(1, pagamentoVendaCreditoModelo.getData_pagamento());
            comando.setDouble(2, pagamentoVendaCreditoModelo.getValor());
            comando.setInt(3, pagamentoVendaCreditoModelo.getCodigo_venda());
            
            comando.execute();
            //fecha a conexao
            conexao.close();
            
            
        } catch (Exception e) {
            return false;
        }
       
        
        return true;
    
    
    
    }
    
}
