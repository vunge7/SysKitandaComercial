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
public class SaidasUtil {
    private String descricao;
    private Double saidas_caixa;
    private Double levantamento;
    private Double transferencia_origem;
    private Date data;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getSaidas_caixa() {
        return saidas_caixa;
    }

    public void setSaidas_caixa(Double saidas_caixa) {
        this.saidas_caixa = saidas_caixa;
    }

    public Double getLevantamento() {
        return levantamento;
    }

    public void setLevantamento(Double levantamento) {
        this.levantamento = levantamento;
    }

    public Double getTransferenciaOrigem() {
        return transferencia_origem;
    }

    public void setTransferenciaOrigem(Double transferencia_origem) {
        this.transferencia_origem = transferencia_origem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
    
    
}
