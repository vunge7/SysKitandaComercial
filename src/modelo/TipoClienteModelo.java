/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

/**
 *
 * @author Martinho
 */
public class TipoClienteModelo {
    private int codigo;
    private String nome;
    private String morada;
    private String telefone;

    public TipoClienteModelo() {
    }

    public TipoClienteModelo(int codigo, String nome, String morada, String telefone) {
        this.codigo = codigo;
        this.nome = nome;
        this.morada = morada;
        this.telefone = telefone;
    }

//    public TipoClienteModelo(String nome) {
//        this.nome = nome;
//    }
    

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
     * @param nome the designacao to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setMorada(String morada) {
        this.morada = morada;
    }

    /**
     * @return the morada
     */
    public String getMorada() {
        return morada;
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
    
    
    
    
}
 

