/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;


import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author Toshiba
 */
public class VendaUsuarioUtil {
    
    private String numero_fact,cliente_nome, data_venda, total_venda;

    public VendaUsuarioUtil() {
    }

    
    
    public VendaUsuarioUtil(String numero_fact, String cliente_nome, String data_venda, String total_venda) 
    {
        this.numero_fact = numero_fact;
        this.cliente_nome = cliente_nome;
        this.data_venda = data_venda;
        this.total_venda = total_venda;
    }

    public String getNumero_fact() {
        return numero_fact;
    }

    public void setNumero_fact(String numero_fact) {
        this.numero_fact = numero_fact;
    }

    public String getCliente_nome() {
        return cliente_nome;
    }

    public void setCliente_nome(String cliente_nome) {
        this.cliente_nome = cliente_nome;
    }

    public String getData_venda() {
        return data_venda;
    }

    public void setData_venda(String data_venda) {
        this.data_venda = data_venda;
    }

    public String getTotal_venda() {
        return total_venda;
    }

    public void setTotal_venda(String total_venda) {
        this.total_venda = total_venda;
    }
    
    
    public static void main(String[] args) {
        new VendaUsuarioUtil();
    }
    
    
}
