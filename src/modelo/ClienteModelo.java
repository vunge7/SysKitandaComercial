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
public class ClienteModelo {
    
    private int codigo;
    private String nome;
    private float credito, saldo;

    public ClienteModelo() {
    }

    public ClienteModelo(int codigo, String nome, float credito, float saldo) {
        this.codigo = codigo;
        this.nome = nome;
        this.credito = credito;
        this.saldo = saldo;
                
    }

    public ClienteModelo(String nome, float credito, float saldo) {
        this.nome = nome;
        this.credito = credito;
         this.saldo = saldo;
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
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the credito
     */
    public float getCredito() {
        return credito;
    }

    /**
     * @param credito the credito to set
     */
    public void setCredito(float credito) {
        this.credito = credito;
    }

    /**
     * @return the saldo
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
        
}
