/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.excel;


import java.sql.Connection;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class GerarExcelUtil
{

    private String path;
    private File ficheiro;
    private String[] colunas;
    private Vector<Vector> dados;

    public GerarExcelUtil()
    {
    }

    public GerarExcelUtil( File ficheiro, String[] colunas, Vector<Vector> dados )
    {
        this.ficheiro = ficheiro;
        this.colunas = colunas;
        this.dados = dados;

    }

    public String getPath()
    {
        return path;
    }

    public void setPath( String path )
    {
        this.path = path;
    }

    public File getFicheiro()
    {
        return ficheiro;
    }

    public void setFicheiro( File ficheiro )
    {
        this.ficheiro = ficheiro;
    }

    public void criarPlanilhaExcel() throws WriteException, IOException
    {

        //criando o arquivo excel
        WritableWorkbook workbook = Workbook.createWorkbook( ficheiro );
        //criando uma nova planilha
        WritableSheet sheet = workbook.createSheet( "1ª Folha", 0 );

        /**
         * Criando as colunas
         */
        for ( int i = 0; i < colunas.length; i++ )
        {
            sheet.addCell( new Label( i, 0, colunas[ i ].toUpperCase() ) );
        }

        /**
         * Criando as linhas
         */
        for ( int x = 0; x < dados.size(); x++ )
        {


            Vector linha = dados.get( x );

            for ( int j = 0; j < linha.size(); j++ )
            {

                sheet.addCell( new Label( j, x + 1, ( String ) linha.get( j ) ) );
            }
        }

        workbook.write();
        workbook.close();

    }

    public static void abrir_anexo_ficheiro()
    {
        try
        {
            File myFile = new File( DVML.PATH_DOCS_UTIL + "Export.xls" );
            Desktop.getDesktop().open( myFile );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( GerarExcelUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    public static void main( String[] args ) throws WriteException, IOException
    {
        File file = new File( DVML.PATH_DOCS_UTIL + "Export.xls" );
        //GerarExcelUtil gerarExcelUtil = new GerarExcelUtil(file);
//        gerarExcelUtil.criarPlanilhaExcel(null);

    }
}
