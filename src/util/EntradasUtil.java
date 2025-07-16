/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Date;

/**
 *
 * @author macpro
 */
public class EntradasUtil {
    
    private String descricao;
    private Double depositos;
    private Double transferencia_destino;
    private Double valor_vendas;
    private Double entrada_caixa;
    private Date data;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getDepositos() {
        return depositos;
    }

    public void setDepositos(Double depositos) {
        this.depositos = depositos;
    }

    public Double getTransferencia_destino() {
        return transferencia_destino;
    }

    public void setTransferencia_destino(Double transferencia_destino) {
        this.transferencia_destino = transferencia_destino;
    }

    public Double getValor_vendas() {
        return valor_vendas;
    }

    public void setValor_vendas(Double valor_vendas) {
        this.valor_vendas = valor_vendas;
    }

    public Double getEntrada_caixa() {
        return entrada_caixa;
    }

    public void setEntrada_caixa(Double entrada_caixa) {
        this.entrada_caixa = entrada_caixa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    


    
}
