/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.excel;


import java.sql.Connection;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import util.DVML;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AbrirExcelUtil
{

    private static final String fileName = DVML.CAMINHO_DOCUMENTO + "/novo_file.xls";

    public static void main( String[] args )
    {
        ler();
    }

    private static void ler()
    {

        try
        {
            /* pega o arquiivo do Excel */
            Workbook workbook = Workbook.getWorkbook( new File( fileName ) );

            /* pega a primeira planilha dentro do arquivo XLS */
            Sheet sheet = workbook.getSheet( 0 );

            for ( int i = 0; i < sheet.getRows(); i++ )
            {

                Cell a1 = sheet.getCell( 1, i );

                System.out.println( "conteudo: " + a1.getContents() );

            }

        }
        catch ( IOException | IndexOutOfBoundsException | BiffException e )
        {
            e.printStackTrace();
        }

    }

}
