/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos dala Vunge
 */
public class ExtratoProdutoModelo {
    
    private String nome_usurario;
    private int quant_total;
    private double quant_valor;

    public ExtratoProdutoModelo() {
    }

    public ExtratoProdutoModelo(String nome_usurario, int quant_total, double quant_valor) {
        this.nome_usurario = nome_usurario;
        this.quant_total = quant_total;
        this.quant_valor = quant_valor;
    }

    /**
     * @return the nome_usurario
     */
    public String getNome_usurario() {
        return nome_usurario;
    }

    /**
     * @param nome_usurario the nome_usurario to set
     */
    public void setNome_usurario(String nome_usurario) {
        this.nome_usurario = nome_usurario;
    }

    /**
     * @return the quant_total
     */
    public int getQuant_total() {
        return quant_total;
    }

    /**
     * @param quant_total the quant_total to set
     */
    public void setQuant_total(int quant_total) {
        this.quant_total = quant_total;
    }

    /**
     * @return the quant_valor
     */
    public double getQuant_valor() {
        return quant_valor;
    }

    /**
     * @param quant_valor the quant_valor to set
     */
    public void setQuant_valor(double quant_valor) {
        this.quant_valor = quant_valor;
    }
    
    public static void main(String[] args) {
        new ExtratoProdutoModelo();
    }
    
           
    
    
    
    
}
