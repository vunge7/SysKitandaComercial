/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Domingos Dala Vunge
 */
public class VendaModelo {
    
    
    
    
    
    private int codigo, codigo_usuario, codigo_cliente, idArmazemFK;
    private String  performance, nome_cliente;
    private   Date dataVenda;
    private Time hora;
    private double total_venda;
    private double valor_entregue, troco;

    public VendaModelo() {
    }

    public VendaModelo(int codigo, int codigo_usuario, int codigo_cliente, Date dataVenda, Time hora, double total_venda, double valor_entregue, double troco) {
        this.codigo = codigo;
        this.codigo_usuario = codigo_usuario;
        this.codigo_cliente = codigo_cliente;
        this.dataVenda = dataVenda;
        this.hora = hora;
        this.total_venda = total_venda;
        this.valor_entregue = valor_entregue;
        this.troco = troco;
    }

    public VendaModelo(int codigo_usuario, int codigo_cliente, Date dataVenda, Time hora, double total_venda, double valor_entregue, double troco, int idArmzemFK) {
        
        this.codigo_usuario = codigo_usuario;
        this.codigo_cliente = codigo_cliente;
        this.dataVenda = dataVenda;
        this.hora = hora;
        this.total_venda = total_venda;
        this.valor_entregue = valor_entregue;
        this.troco = troco;
        this.idArmazemFK = idArmzemFK;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
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
     * @return the codigo_usuario
     */
    public int getCodigo_usuario() {
        return codigo_usuario;
    }

    /**
     * @param codigo_usuario the codigo_usuario to set
     */
    public void setCodigo_usuario(int codigo_usuario) {
        this.codigo_usuario = codigo_usuario;
    }

    /**
     * @return the codigo_cliente
     */
    public int getCodigo_cliente() {
        return codigo_cliente;
    }

    /**
     * @param codigo_cliente the codigo_cliente to set
     */
    public void setCodigo_cliente(int codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    /**
     * @return the dataVenda
     */
    public Date getDataVenda() {
        return dataVenda;
    }

    /**
     * @param dataVenda the dataVenda to set
     */
    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

     /**
     * @return the hora
     */
    public Time getHora() {
        return hora;
    }

    /**
     * @param hora the dataVenda to set
     */
    public void setHora(Time hora) {
        this.hora = hora;
    }

    
    
    /**
     * @return the total_venda
     */
    public double getTotal_venda() {
        return total_venda;
    }

    /**
     * @param total_venda the total_venda to set
     */
    public void setTotal_venda(double total_venda) {
        this.total_venda = total_venda;
    }

    /**
     * @return the valor_entregue
     */
    public double getValor_entregue() {
        return valor_entregue;
    }

    /**
     * @param valor_entregue the valor_entregue to set
     */
    public void setValor_entregue(double valor_entregue) {
        this.valor_entregue = valor_entregue;
    }

    /**
     * @return the troco
     */
    public double getTroco() {
        return troco;
    }

    /**
     * @param troco the troco to set
     */
    public void setTroco(double troco) {
        this.troco = troco;
    }

    /**
     * @return the performance
     */
    public String getPerformance() {
        return performance;
    }

    /**
     * @param performance the performance to set
     */
    public void setPerformance(String performance) {
        this.performance = performance;
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
