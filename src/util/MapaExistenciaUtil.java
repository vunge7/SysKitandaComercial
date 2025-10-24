/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
/**
 *
 * @author Domingos Dala Vunge
 */

public class MapaExistenciaUtil {
    private int cod_produto, existencia_anterior,qtd_entrada, qtd_vendida, qtd_actual;
    private String designacao;
    private double preco_venda, total, desconto;

    public MapaExistenciaUtil() {
    }

    public int getCod_produto() {
        return cod_produto;
    }

    public void setCod_produto(int cod_produto) {
        this.cod_produto = cod_produto;
    }

    public int getExistencia_anterior() {
        return existencia_anterior;
    }

    public void setExistencia_anterior(int existencia_anterior) {
        this.existencia_anterior = existencia_anterior;
    }

    public int getQtd_entrada() {
        return qtd_entrada;
    }

    public void setQtd_entrada(int qtd_entrada) {
        this.qtd_entrada = qtd_entrada;
    }

    public int getQtd_vendida() {
        return qtd_vendida;
    }

    public void setQtd_vendida(int qtd_vendida) {
        this.qtd_vendida = qtd_vendida;
    }

    public int getQtd_actual() {
        return qtd_actual;
    }

    public void setQtd_actual(int qtd_actual) {
        this.qtd_actual = qtd_actual;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }
    
    
    
    public static void main(String[] args) {
        new MapaExistenciaUtil();
    }
    
}
