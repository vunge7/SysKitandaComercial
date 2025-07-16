/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TipoProdutoModelo {
    
    private int codigo;
    private String designacao;

    public TipoProdutoModelo() {
    }

    public TipoProdutoModelo(int codigo, String designacao) {
        this.codigo = codigo;
        this.designacao = designacao;
    }

    public TipoProdutoModelo(String designacao) {
        this.designacao = designacao;
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
     * @return the designacao
     */
    public String getDesignacao() {
        return designacao;
    }

    /**
     * @param designacao the designacao to set
     */
    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }
    
    
    
    
}
