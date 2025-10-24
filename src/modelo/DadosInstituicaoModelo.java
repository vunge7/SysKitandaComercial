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
public class DadosInstituicaoModelo {
    
    private int idDadosInstituicaoModelo;
    private String nome, telefone, enderecos, email,nif;

    public DadosInstituicaoModelo() {
    }

   
  
    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the enderecos
     */
    public String getEnderecos() {
        return enderecos;
    }

    /**
     * @param enderecos the enderecos to set
     */
    public void setEnderecos(String enderecos) {
        this.enderecos = enderecos;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param nif the nif to set
     */
    public void setNif(String nif) {
        this.nif = nif;
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
     * @return the idDadosInstituicaoModelo
     */
    public int getIdDadosInstituicaoModelo() {
        return idDadosInstituicaoModelo;
    }

    /**
     * @param idDadosInstituicaoModelo the idDadosInstituicaoModelo to set
     */
    public void setIdDadosInstituicaoModelo(int idDadosInstituicaoModelo) {
        this.idDadosInstituicaoModelo = idDadosInstituicaoModelo;
    }
    
    
    
    
}
