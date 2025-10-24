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
public class StockModelo {
    
    
    private int codigo, codigo_produto, cod_armazem,  quantidade_existente, quantidadeCritica, quantidadeBaixa, quantidade_antiga;
    private String dataEntrada, status;
    private double preco_venda, preco_venda_fabrica; 

    public StockModelo() {
    }

    public StockModelo(int codigo, int codigo_produto, int cod_armazem,   String dataEntrada, int quantidade_existente,  int quantidade_antiga, String status, double preco_venda, int quantidadeCritica, int quantidadeBaixa,  double preco_venda_fabrica ) {
        this.codigo = codigo;
        this.codigo_produto = codigo_produto;
        this.dataEntrada = dataEntrada;
        this.quantidade_existente = quantidade_existente;
        this.quantidade_antiga = quantidade_antiga;
        this.quantidadeCritica = quantidadeCritica;
        this.quantidadeBaixa = quantidadeBaixa;
        this.status = status;
        this.preco_venda = preco_venda; 
        this.preco_venda_fabrica = preco_venda_fabrica; 
        this.cod_armazem = cod_armazem;
        
    }

    public StockModelo(int codigo_produto, int cod_armazem,   String dataEntrada, int quantidade_existente,  int quantidade_antiga, String status, double preco_venda, int quantidadeCritica, int quantidadeBaixa, double  preco_venda_fabrica ) {
        this.codigo_produto = codigo_produto;
        this.dataEntrada = dataEntrada;
       this.quantidade_existente = quantidade_existente;
        this.quantidade_antiga = quantidade_antiga;
        this.quantidadeCritica = quantidadeCritica;
        this.quantidadeBaixa = quantidadeBaixa;
        this.status = status;
        this.preco_venda = preco_venda; 
        this.preco_venda_fabrica = preco_venda_fabrica;
        this.cod_armazem = cod_armazem;
    }

   
    
    
    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the codigo_produto
     */
    public int getCodigo_produto() {
        return codigo_produto;
    }

    /**
     * @param codigo_produto the codigo_produto to set
     */
    public void setCodigo_produto(int codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    /**
     * @return the dataEntrada
     */
    public String getDataEntrada() {
        return dataEntrada;
    }

    /**
     * @param dataEntrada the dataEntrada to set
     */
    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    /**
     * @return the quantidade_existente
     */
    public int getQuantidade() {
        return getQuantidade_existente();
    }

    /**
     * @param quantidade_existente the quantidade_existente to set
     */
    public void setQuantidade(int quantidade) {
        this.setQuantidade_existente(quantidade);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public double getPreco_venda_fabrica() {
        return preco_venda_fabrica;
    }

    public void setPreco_venda_fabrica(double preco_venda_fabrica) {
        this.preco_venda_fabrica = preco_venda_fabrica;
    }

    /**
     * @return the preco_venda
     */
    public double getPreco_venda() {
        return preco_venda;
    }

    /**
     * @param preco_venda the preco_venda to set
     */
    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    /**
     * @return the quantidadeCritica
     */
    public int getQuantidadeCritica() {
        return quantidadeCritica;
    }

    /**
     * @param quantidadeCritica the quantidadeCritica to set
     */
    public void setQuantidadeCritica(int quantidadeCritica) {
        this.quantidadeCritica = quantidadeCritica;
    }

    /**
     * @return the quantidadeBaixa
     */
    public int getQuantidadeBaixa() {
        return quantidadeBaixa;
    }

    /**
     * @param quantidadeBaixa the quantidadeBaixa to set
     */
    public void setQuantidadeBaixa(int quantidadeBaixa) {
        this.quantidadeBaixa = quantidadeBaixa;
    }

    /**
     * @return the quantidade_existente
     */
    public int getQuantidade_existente() {
        return quantidade_existente;
    }

    /**
     * @param quantidade_existente the quantidade_existente to set
     */
    public void setQuantidade_existente(int quantidade_existente) {
        this.quantidade_existente = quantidade_existente;
    }

    /**
     * @return the quantidade_antiga
     */
    public int getQuantidade_antiga() {
        return quantidade_antiga;
    }

    /**
     * @param quantidade_antiga the quantidade_antiga to set
     */
    public void setQuantidade_antiga(int quantidade_antiga) {
        this.quantidade_antiga = quantidade_antiga;
    }

    /**
     * @return the cod_armazem
     */
    public int getCod_armazem() {
        return cod_armazem;
    }

    /**
     * @param cod_armazem the cod_armazem to set
     */
    public void setCod_armazem(int cod_armazem) {
        this.cod_armazem = cod_armazem;
    }
    
    
    

}
