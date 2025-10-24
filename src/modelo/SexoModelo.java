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
public class SexoModelo {
    
    private int codigo;
    private String desgnacao;

    
    
    public SexoModelo() {
    }
    
    
    public SexoModelo(int codigo, String desgnacao) {
        this.codigo = codigo;
        this.desgnacao = desgnacao;
    }

    public SexoModelo(String desgnacao) {
        this.desgnacao = desgnacao;
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
     * @return the desgnacao
     */
    public String getDesgnacao() {
        return desgnacao;
    }

    /**
     * @param desgnacao the desgnacao to set
     */
    public void setDesgnacao(String desgnacao) {
        this.desgnacao = desgnacao;
    }
 
    
}
