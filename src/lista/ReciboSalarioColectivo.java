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
 * @author Domingos Dala Vunge
 */
public class ReciboSalarioColectivo
{


    private int ano;
    private String periodo;
    public ReciboSalarioColectivo( int ano, String periodo ) throws SQLException
    {
        this.ano = ano;
        this.periodo = periodo;

        mostrar();

    }

    public void mostrar() throws SQLException
    {

        Connection connection = BDConexao.getConexao();
        HashMap hashMap = new HashMap();

        hashMap.put( "ANO", this.ano );
        hashMap.put( "PERIODO", this.periodo );

        //obter o path do relatorio
        String relatorio =  DVML.CAMINHO_REPORT + "master_recibo_salarial.jasper";

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
        ReciboSalarioColectivo reciboEntradas = new ReciboSalarioColectivo( 2020, "Setembro" );
    }

}
