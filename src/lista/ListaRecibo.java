/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;

/**
 *
 * @author MartinhoLuis / Domingos Dala Vunge
 */
public class ListaRecibo
{

    private int idFuncionario;
    private Integer ano;
    private String periodo;
    private String segundaVia = null;

    public ListaRecibo( Integer ano, String periodo, int idFuncionario ) throws SQLException
    {

        this.idFuncionario = idFuncionario;
        this.ano = ano;
        this.periodo = periodo;
        mostrar();

    }

    public ListaRecibo( Integer ano, String periodo, int idFuncionario, String segundaVia ) throws SQLException
    {

        this.idFuncionario = idFuncionario;
        this.ano = ano;
        this.periodo = periodo;
        this.segundaVia = segundaVia;
        mostrar();

    }

    public void mostrar() throws SQLException
    {

        Connection connection = BDConexao.getConexao();
        HashMap hashMap = new HashMap();

        hashMap.put( "ANO", this.ano );
        hashMap.put( "PERIODO", this.periodo );
        hashMap.put( "ID_FUNCIONARIO", this.idFuncionario );
        hashMap.put( "SEGUNDA_VIA", segundaVia );

        //obter o path do relatorio
        //String relatorio = "relatorios/recibo_individual.jasper";
        String relatorio = DVML.CAMINHO_REPORT + "sub_recibo_salarial.jasper";

        File file = new File( relatorio ).getAbsoluteFile();
        String obterCaminho = file.getAbsolutePath();

        try
        {

            //JasperFillManager.fillReport(obterCaminho, hashMap, connection);
            JasperFillManager.fillReport( obterCaminho, hashMap, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );

            if ( jasperPrint.getPages().size() >= 1 )
            {
                JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                jasperViewer.setVisible( true );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Atenção\nNão existe recibo relacionado com este numero!" );
            }

        }
        catch ( JRException jex )
        {
            jex.printStackTrace();
            JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR O RECIBO!" );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, "ERRO AO TENTAR MOSTRAR O RECIBO!" );
        }

    }

    public static void main( String[] args ) throws SQLException
    {
        ListaRecibo reciboEntradas = new ListaRecibo( 2021, "Abril", 1 );
    }

}
