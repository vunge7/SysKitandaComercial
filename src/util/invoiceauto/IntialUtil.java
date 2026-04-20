/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.invoiceauto;

import java.math.BigDecimal;
import util.BDConexao;

//import java.sql.Connection;
/**
 *
 * @author Domingos Dala Vunge
 */
public class IntialUtil
{

    public static void main( String[] args )
    {
        FacturaService facturaService = new FacturaService( new BDConexao() );
        facturaService.gerarFacturasComTotalMensal( 2026,1, new BigDecimal( 50000 ) );
    }

}
