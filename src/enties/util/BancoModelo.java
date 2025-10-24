/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package enties.util;


import java.sql.Connection;
/**
 *
 * @author Domingos Dala Vunge
 */
public class BancoModelo {
    private String descricao, numero;
    private double total;

    public BancoModelo(String descricao, String numero, double total) {
        this.descricao = descricao;
        this.numero = numero;
        this.total = total;
    }

    public String getDescrisao() {
        return descricao;
    }

    public void setDescricao(String descrisao) {
        this.descricao = descrisao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    public static void main(String[] args) {
        new BancoModelo("", "", 0 );
    }
    
}
