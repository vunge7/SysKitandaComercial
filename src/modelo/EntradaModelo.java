/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EntradaModelo {
    
    private int idEntrada, idProduto, idUsuario, quantidade, idArmazemFK;
    private String data_entrada;

    public EntradaModelo() {
    }

    /**
     * @return the idEntrada
     */
    public int getIdEntrada() {
        return idEntrada;
    }

    /**
     * @param idEntrada the idEntrada to set
     */
    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    /**
     * @return the idProduto
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * @param idProduto the idProduto to set
     */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * @return the idUsuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the data_entrada
     */
    public String getData_entrada() {
        return data_entrada;
    }

    /**
     * @param data_entrada the data_entrada to set
     */
    public void setData_entrada(String data_entrada) {
        this.data_entrada = data_entrada;
    }

    /**
     * @return the idArmazemFK
     */
    public int getIdArmazemFK() {
        return idArmazemFK;
    }

    /**
     * @param idArmazemFK the idArmazemFK to set
     */
    public void setIdArmazemFK(int idArmazemFK) {
        this.idArmazemFK = idArmazemFK;
    }
    
    
    
    
    
}
