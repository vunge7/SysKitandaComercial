/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;


import java.sql.Connection;
/**
 *
 * @author Toshiba
 */
public class ProdutosUtil {
    
    private String cod_servico , designacao, custo, moeda, qtd;

    public ProdutosUtil() {
    } 

    public String getCod_servico() {
        return cod_servico;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public void setCod_servico(String cod_servico) {
        this.cod_servico = cod_servico;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public String getCusto() {
        return custo;
    }

    public void setCusto(String custo) {
        this.custo = custo;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }
    
    
    public static void main(String[] args) {
        new ProdutosUtil();
    }
}
