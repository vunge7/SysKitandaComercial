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
public class RelatorioDiarioUtil {
    
    private String codigo_produto , desigancao, preco_unitario, quantidade,  total_venda;

    public RelatorioDiarioUtil() {
    }

    public String getCodigo_produto() {
        return codigo_produto;
    }

    public void setCodigo_produto(String codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    public String getDesigancao() {
        return desigancao;
    }

    public void setDesigancao(String desigancao) {
        this.desigancao = desigancao;
    }

    public String getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(String preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public String getTotal_venda() {
        return total_venda;
    }

    public void setTotal_venda(String total_venda) {
        this.total_venda = total_venda;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    
    public static void main(String[] args) {
        new RelatorioDiarioUtil();
    }
    
  
    
}
