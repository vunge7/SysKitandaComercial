/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EncomendaModelo {
    
    private Integer idEncomenda, idCliente, idUsuario;
    private double total_encomenda;
    private boolean status_entrega;
    private String data_encomenda, data_entrega_prevista, obs;

    public EncomendaModelo() {
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
     * @return the idCliente
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the total_encomenda
     */
    public double getTotal_encomenda() {
        return total_encomenda;
    }

    /**
     * @param total_encomenda the total_encomenda to set
     */
    public void setTotal_encomenda(double total_encomenda) {
        this.total_encomenda = total_encomenda;
    }

    /**
     * @return the status_entrega
     */
    public boolean isStatus_entrega() {
        return status_entrega;
    }

    /**
     * @param status_entrega the status_entrega to set
     */
    public void setStatus_entrega(boolean status_entrega) {
        this.status_entrega = status_entrega;
    }

    /**
     * @return the data_encomenda
     */
    public String getData_encomenda() {
        return data_encomenda;
    }

    /**
     * @param data_encomenda the data_encomenda to set
     */
    public void setData_encomenda(String data_encomenda) {
        this.data_encomenda = data_encomenda;
    }

    /**
     * @return the data_entrega_prevista
     */
    public String getData_entrega_prevista() {
        return data_entrega_prevista;
    }

    /**
     * @param data_entrega_prevista the data_entrega_prevista to set
     */
    public void setData_entrega_prevista(String data_entrega_prevista) {
        this.data_entrega_prevista = data_entrega_prevista;
    }

    /**
     * @return the idUsuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return the obs
     */
    public String getObs() {
        return obs;
    }

    /**
     * @param obs the obs to set
     */
    public void setObs(String obs) {
        this.obs = obs;
    }
    
    
    
}
