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
public class FornecedorModelo {
    
    private int codigo;
    private String nome, telefone, email, site, enderreco;

    public FornecedorModelo() {
    }


    
    public FornecedorModelo(int codigo, String nome, String telefone, String email, String site, String enderreco) {
        this.codigo = codigo;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.site = site;
        this.enderreco = enderreco;
    }

    public FornecedorModelo(String nome, String telefone, String email, String site, String enderreco) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.site = site;
        this.enderreco = enderreco;
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
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the enderreco
     */
    public String getEndereco() {
        return enderreco;
    }

    /**
     * @param enderreco the enderreco to set
     */
    public void setEnderreco(String enderreco) {
        this.enderreco = enderreco;
    }
    
    
    
    
    
    
    
    
    
    
    
}
