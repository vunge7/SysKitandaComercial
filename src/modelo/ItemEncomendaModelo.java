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
public class ItemEncomendaModelo {
    private Integer idEncomenda, idPrdouto, qauntidade;
    private double total;

    public ItemEncomendaModelo() {
    }

    /**
     * @return the idEncomenda
     */
    public Integer getIdEncomenda() {
        return idEncomenda;
    }

    /**
     * @param idEncomenda the idEncomenda to set
     */
    public void setIdEncomenda(Integer idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    /**
     * @return the idPrdouto
     */
    public Integer getIdPrdouto() {
        return idPrdouto;
    }

    /**
     * @param idPrdouto the idPrdouto to set
     */
    public void setIdPrdouto(Integer idPrdouto) {
        this.idPrdouto = idPrdouto;
    }

    /**
     * @return the qauntidade
     */
    public Integer getQauntidade() {
        return qauntidade;
    }

    /**
     * @param qauntidade the qauntidade to set
     */
    public void setQauntidade(Integer qauntidade) {
        this.qauntidade = qauntidade;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }
    
    
    
    
}
