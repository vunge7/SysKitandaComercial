/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorios;


import java.sql.Connection;
import entity.TbFuncionario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import util.SalarioUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.eclipse.persistence.internal.jpa.parsing.SomeNode;

/**
 *
 * @author DALLAS
 */
public class FolhaSalario
{

    private List<SalarioUtil> lista = new ArrayList();
    private TbFuncionario funcionario;
    private String periodo = "", formaPagamento = "";

    public FolhaSalario( List<SalarioUtil> salario_util, TbFuncionario funcionario, String periodo )
    {

        lista = salario_util;
        this.periodo = periodo;
        this.funcionario = funcionario;
        mostrar();

    }

    public FolhaSalario( List<SalarioUtil> salario_util, TbFuncionario funcionario, String periodo, String formaPagamento )
    {

        lista = salario_util;
        this.periodo = periodo;
        this.funcionario = funcionario;
        mostrar();

    }

    private double getTotalValores()
    {
        double soma_valores = 0;
        for ( SalarioUtil salarioUtil : lista )
        {

            if ( salarioUtil.getValor() != 0 )
            {

                try
                {
                    soma_valores = soma_valores + salarioUtil.getValor();
                }
                catch ( Exception e )
                {
                    soma_valores = soma_valores + 0;
                }

            }
        }
        return soma_valores;
    }

    private double getTotalDesconto()
    {
        double soma_desconto = 0;
        String desconto_transformado = "";
        for ( SalarioUtil salarioUtil : lista )
        {
            // soma_valores = soma_valores +  Double.parseDouble( salarioUtil.getValor() ) ;
            if ( salarioUtil.getDesconto() != 0 )
            {

                //   System.out.println("Salario Transformado: " +salario_transformado.replace(".", "").trim());
                try
                {
                    soma_desconto = soma_desconto + salarioUtil.getDesconto();
                }
                catch ( Exception e )
                {
                    soma_desconto = soma_desconto + 0;
                }

            }
        }

        return soma_desconto;
    }

    public void mostrar()
    {

        //Meus Parametros
        HashMap hashMap = new HashMap();
        hashMap.put( "SOMA_VALORES", getTotalValores() );
        hashMap.put( "SOMA_DESCONTO", getTotalDesconto() );
        hashMap.put( "NOME_COMPLETO", funcionario.getNome() );
        hashMap.put( "FUNCAO", funcionario.getFkFuncao().getDesignacao() );
        hashMap.put( "DEPARTAMENTO", funcionario.getFkDepartamento().getDesignacao() );
        hashMap.put( "PERIODO", this.periodo );
        hashMap.put( "CAMBIO", 160d );
        if ( !formaPagamento.equals( "" ) )
        {
            hashMap.put( "FORMA_PAGAMENTO", "Forma de Pagamento: " + this.formaPagamento );
        }

        System.out.println( "SOMA_VALORES: " + getTotalValores() );
        System.out.println( "SOMA_DESCONTO " + getTotalDesconto() );
        String caminho = getCaminho();
        //Minha Coleção de Dados 
        try
        {

            JasperReport report = JasperCompileManager.compileReport( caminho );

            JasperPrint print = JasperFillManager.fillReport( report, hashMap, new JRBeanCollectionDataSource( lista ) );

            JasperViewer jasperViewer = new JasperViewer( print, false );
            jasperViewer.setVisible( true );

        }
        catch ( JRException ex )
        {
            ex.printStackTrace();
        }

    }

    private String getCaminho()
    {

        return "relatorios/rh_recibo_salario.jrxml";

    }

    public static void main( String[] args )
    {

    }

}
