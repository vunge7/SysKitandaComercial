/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


import java.sql.Connection;
/**
 *
 * @author Domingos Dala Vunge
 */
public class PagamentoVendaCreditoModelo {
    private int coidigo, codigo_venda;
    private String data_pagamento;
    private double valor;

    public PagamentoVendaCreditoModelo() {
    }

    public PagamentoVendaCreditoModelo(int coidigo, int codigo_venda, String data_pagamento, double valor) {
        this.coidigo = coidigo;
        this.codigo_venda = codigo_venda;
        this.data_pagamento = data_pagamento;
        this.valor = valor;
    }

    public PagamentoVendaCreditoModelo(int codigo_venda, String data_pagamento, double valor) {
        this.codigo_venda = codigo_venda;
        this.data_pagamento = data_pagamento;
        this.valor = valor;
    }

    /**
     * @return the coidigo
     */
    public int getCoidigo() {
        return coidigo;
    }

    /**
     * @param coidigo the coidigo to set
     */
    public void setCoidigo(int coidigo) {
        this.coidigo = coidigo;
    }

    /**
     * @return the codigo_venda
     */
    public int getCodigo_venda() {
        return codigo_venda;
    }

    /**
     * @param codigo_venda the codigo_venda to set
     */
    public void setCodigo_venda(int codigo_venda) {
        this.codigo_venda = codigo_venda;
    }

    /**
     * @return the data_pagamento
     */
    public String getData_pagamento() {
        return data_pagamento;
    }

    /**
     * @param data_pagamento the data_pagamento to set
     */
    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }

    /**
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    
    
    
}
