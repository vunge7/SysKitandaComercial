/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

import java.util.Vector;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 20/abr/2026
 * @lastModified 20/abr/2026
 */
public class AutoInvoiceConfiguration
{

    private int numeroMaximoFactura;
    private int numeroMaximoLinha;
    private int numeroMaximoQtd;
    private Vector<Integer> listaProdutoVenda;
    private double limiteFacturacaoGeralMes;
    private int diaComeco;
    private double contTotalGeralMes;

    private int anoEconomico;
    private int anoEconomicoId;
    private int documentoId;
    private int mesId;
    private int userId;
    private int clienteId;

    private int limiteDiaMes;
    private int armazemId;

    public AutoInvoiceConfiguration()
    {
    }

    public int getAnoEconomicoId()
    {
        return anoEconomicoId;
    }

    public int getLimiteDiaMes()
    {
        return limiteDiaMes;
    }

    public void setLimiteDiaMes( int limiteDiaMes )
    {
        this.limiteDiaMes = limiteDiaMes;
    }

    public void setAnoEconomicoId( int anoEconomicoId )
    {
        this.anoEconomicoId = anoEconomicoId;
    }

    public int getAnoEconomico()
    {
        return anoEconomico;
    }

    public int getClienteId()
    {
        return clienteId;
    }

    public void setClienteId( int clienteId )
    {
        this.clienteId = clienteId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public void setAnoEconomico( int anoEconomico )
    {
        this.anoEconomico = anoEconomico;
    }

    public int getMesId()
    {
        return mesId;
    }

    public int getArmazemId()
    {
        return armazemId;
    }

    public void setArmazemId( int armazemId )
    {
        this.armazemId = armazemId;
    }
    

    public void setMesId( int mesId )
    {
        this.mesId = mesId;
    }

    public int getDocumentoId()
    {
        return documentoId;
    }

    public void setDocumentoId( int documentoId )
    {
        this.documentoId = documentoId;
    }

    public int getNumeroMaximoFactura()
    {
        return numeroMaximoFactura;
    }

    public void setNumeroMaximoFactura( int numeroMaximoFactura )
    {
        this.numeroMaximoFactura = numeroMaximoFactura;
    }

    public int getNumeroMaximoLinha()
    {
        return numeroMaximoLinha;
    }

    public void setNumeroMaximoLinha( int numeroMaximoLinha )
    {
        this.numeroMaximoLinha = numeroMaximoLinha;
    }

    public int getNumeroMaximoQtd()
    {
        return numeroMaximoQtd;
    }

    public void setNumeroMaximoQtd( int numeroMaximoQtd )
    {
        this.numeroMaximoQtd = numeroMaximoQtd;
    }

    public Vector<Integer> getListaProdutoVenda()
    {
        return listaProdutoVenda;
    }

    public void setListaProdutoVenda( Vector<Integer> listaProdutoVenda )
    {
        this.listaProdutoVenda = listaProdutoVenda;
    }

    public double getLimiteFacturacaoGeralMes()
    {
        return limiteFacturacaoGeralMes;
    }

    public void setLimiteFacturacaoGeralMes( double limiteFacturacaoGeralMes )
    {
        this.limiteFacturacaoGeralMes = limiteFacturacaoGeralMes;
    }

    public int getDiaComeco()
    {
        return diaComeco;
    }

    public void setDiaComeco( int diaComeco )
    {
        this.diaComeco = diaComeco;
    }

    public double getContTotalGeralMes()
    {
        return contTotalGeralMes;
    }

    public void setContTotalGeralMes( double contTotalGeralMes )
    {
        this.contTotalGeralMes = contTotalGeralMes;
    }

}
