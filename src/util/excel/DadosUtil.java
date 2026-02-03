/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.excel;


import java.sql.Connection;
import java.math.BigDecimal;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DadosUtil
{

    private String codManual;
    private String categoria;
    private String designacao;
    private BigDecimal precoVenda;
    private String codBarra;
    private double stock_actual;
    private BigDecimal precoCompra;
    
    
    /**
     * DADOS CLIENTE
     */
    private String nomeCliente;
    private String moradaliente;
    private String telefoneCliente;
    private String nifCliente;
    private String emailCliente;
    
    /**
     * DADOS FORNECEDOR
     */
    private String nomeFornecedor;
    private String moradaFornecedor;
    private String telefoneFornecedor;
    private String nifFornecedor;
    private String emailFornecedor;
    

    public DadosUtil()
    {
    }
    
        public double getStock_actual()
    {
        return stock_actual;
    }

    public void setStock_actual( double stock_actual )
    {
        this.stock_actual = stock_actual;
    }

        public BigDecimal getPrecoCompra()
    {
        return precoCompra;
    }

    public void setPrecoCompra( BigDecimal precoCompra )
    {
        this.precoCompra = precoCompra;
    }
    

    public String getCodManual()
    {
        return codManual;
    }

    public void setCodManual( String codManual )
    {
        this.codManual = codManual;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria( String categoria )
    {
        this.categoria = categoria;
    }

    public String getDesignacao()
    {
        return designacao;
    }

    public void setDesignacao( String designacao )
    {
        this.designacao = designacao;
    }

    public BigDecimal getPrecoVenda()
    {
        return precoVenda;
    }

    public void setPrecoVenda( BigDecimal precoVenda )
    {
        this.precoVenda = precoVenda;
    }
    
        
    public String getCodBarra()
    {
        return codBarra;
    }

    public void setCodBarra( String codBarra )
    {
        this.codBarra = codBarra;
    }

    public String getNomeCliente()
    {
        return nomeCliente;
    }

    public void setNomeCliente( String nomeCliente )
    {
        this.nomeCliente = nomeCliente;
    }

    public String getMoradaliente()
    {
        return moradaliente;
    }

    public void setMoradaliente( String moradaliente )
    {
        this.moradaliente = moradaliente;
    }

    public String getTelefoneCliente()
    {
        return telefoneCliente;
    }

    public void setTelefoneCliente( String telefoneCliente )
    {
        this.telefoneCliente = telefoneCliente;
    }

    public String getNifCliente()
    {
        return nifCliente;
    }

    public void setNifCliente( String nifCliente )
    {
        this.nifCliente = nifCliente;
    }

    public String getEmailCliente()
    {
        return emailCliente;
    }

    public void setEmailCliente( String emailCliente )
    {
        this.emailCliente = emailCliente;
    }

    public String getNomeFornecedor()
    {
        return nomeFornecedor;
    }

    public void setNomeFornecedor( String nomeFornecedor )
    {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getMoradaFornecedor()
    {
        return moradaFornecedor;
    }

    public void setMoradaFornecedor( String moradaFornecedor )
    {
        this.moradaFornecedor = moradaFornecedor;
    }

    public String getTelefoneFornecedor()
    {
        return telefoneFornecedor;
    }

    public void setTelefoneFornecedor( String telefoneFornecedor )
    {
        this.telefoneFornecedor = telefoneFornecedor;
    }

    public String getNifFornecedor()
    {
        return nifFornecedor;
    }

    public void setNifFornecedor( String nifFornecedor )
    {
        this.nifFornecedor = nifFornecedor;
    }

    public String getEmailFornecedor()
    {
        return emailFornecedor;
    }

    public void setEmailFornecedor( String emailFornecedor )
    {
        this.emailFornecedor = emailFornecedor;
    }
    
    
    
    
    

}
