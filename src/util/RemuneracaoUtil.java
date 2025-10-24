/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
/**
 *
 * @author Domingos Dala Vunge
 */
public class RemuneracaoUtil
{
    private String nomeFuncionario;
    private String categoria;
    private String ninss;
    private double base;
    private double diasTrabalhado;
    private double adicional;
    private double salararioIliquido;
    private double inss3;
    private double inss8;
    private double irt;
    private double percetamgem;
    private double salarioLiquido;

    public RemuneracaoUtil()
    {
    }

    public String getNomeFuncionario()
    {
        return nomeFuncionario;
    }

    public void setNomeFuncionario( String nomeFuncionario )
    {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria( String categoria )
    {
        this.categoria = categoria;
    }

    public String getNinss()
    {
        return ninss;
    }

    public void setNinss( String ninss )
    {
        this.ninss = ninss;
    }

    public double getBase()
    {
        return base;
    }

    public void setBase( double base )
    {
        this.base = base;
    }

    public double getDiasTrabalhado()
    {
        return diasTrabalhado;
    }

    public void setDiasTrabalhado( double diasTrabalhado )
    {
        this.diasTrabalhado = diasTrabalhado;
    }

    public double getAdicional()
    {
        return adicional;
    }

    public void setAdicional( double adicional )
    {
        this.adicional = adicional;
    }

    public double getSalararioIliquido()
    {
        return salararioIliquido;
    }

    public void setSalararioIliquido( double salararioIliquido )
    {
        this.salararioIliquido = salararioIliquido;
    }

    public double getInss3()
    {
        return inss3;
    }

    public void setInss3( double inss3 )
    {
        this.inss3 = inss3;
    }

    public double getInss8()
    {
        return inss8;
    }

    public void setInss8( double inss8 )
    {
        this.inss8 = inss8;
    }

    public double getIrt()
    {
        return irt;
    }

    public void setIrt( double irt )
    {
        this.irt = irt;
    }

    public double getPercetamgem()
    {
        return percetamgem;
    }

    public void setPercetamgem( double percetamgem )
    {
        this.percetamgem = percetamgem;
    }

    public double getSalarioLiquido()
    {
        return salarioLiquido;
    }

    public void setSalarioLiquido( double salarioLiquido )
    {
        this.salarioLiquido = salarioLiquido;
    }
    
    
    public static void main( String[] args )
    {
        new RemuneracaoUtil();
    }
    
    
    
    
    
}
