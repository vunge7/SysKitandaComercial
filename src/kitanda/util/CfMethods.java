/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.util;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import static kitanda.util.CfConstantes.*;
import util.DVML;
//import winmarketpro1.Util.Extenso;

/**
 *
 * @author tagif
 */
public class CfMethods
{

    public static final String PU_NAME = "WinMarket_Contsys_NormalPU";
    public static String MOEDA = "AOA";

    public static Dimension getScreenSize()
    {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static EntityManager getEM()
    {
        return getEMF().createEntityManager();
    }

    public static EntityManagerFactory getEMF()
    {
        return Persistence.createEntityManagerFactory( PU_NAME );
    }

    public static String formatarComoMoeda( double valor )
    {
        return formatarComoMoeda( valor, MOEDA );
//        return formatarComoMoeda( valor);
    }

    public static String formatarComoMoedaPreco( double valor )
    {
        return formatarComoMoedaPreco( valor, MOEDA );
//        return formatarComoMoeda( valor);
    }

    public static String formatarComoMoeda( BigDecimal valor )
    {
        return formatarComoMoeda( valor.doubleValue(), MOEDA );
    }

    public static String formatarComoMoeda( double valor, String moeda )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###,###.##### " + moeda );

        decimalFormat.setGroupingSize( 3 );
        decimalFormat.setMinimumIntegerDigits( 1 );
        System.err.println( "VALOR: " + valor );
        decimalFormat.setMinimumFractionDigits( 2 );
        decimalFormat.setDecimalSeparatorAlwaysShown( true );
        decimalFormat.setRoundingMode( RoundingMode.HALF_EVEN );

        return decimalFormat.format( valor );
    }

    public static String formatarComoMoedaPreco( double valor, String moeda )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###,###.###### " + moeda );

        decimalFormat.setGroupingSize( 3 );
        decimalFormat.setMinimumIntegerDigits( 1 );
        System.err.println( "VALOR: " + valor );
        decimalFormat.setMinimumFractionDigits( 6 );
        decimalFormat.setDecimalSeparatorAlwaysShown( true );
        decimalFormat.setRoundingMode( RoundingMode.HALF_EVEN );

        return decimalFormat.format( valor );
    }

    public static Double parseMoedaFormatada( String valor )
    {
        Double d = null;
//
        System.err.println( "valor: " + valor );
        try
        {
            DecimalFormat decimalFormat = new DecimalFormat( "###,###.##### " + MOEDA );

            decimalFormat.setGroupingSize( 3 );
            decimalFormat.setMinimumIntegerDigits( 1 );

            decimalFormat.setMinimumFractionDigits( 6 );
            decimalFormat.setDecimalSeparatorAlwaysShown( true );
            d = decimalFormat.parse( valor ).doubleValue();

        }
        catch ( ParseException ex )
        {
            Logger.getLogger( CfMethods.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return d;
    }

    public static BigDecimal parseMoedaFormatadaBigDecimal( String valorFormatado )
    {
        String normalizado = valorFormatado.replace( ".", "" ).replace( ",", "." );
        return new BigDecimal( normalizado );
    }

    public static String formatarComoNumeroParticipantes( int valor )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###### Participantes" );

        decimalFormat.setGroupingSize( 3 );
        decimalFormat.setMinimumIntegerDigits( 1 );
        decimalFormat.setMaximumFractionDigits( 0 );

        return decimalFormat.format( valor );
    }

    public static int getTimeField( int field, String hourMinute )
    {
        if ( field >= 0 && field < hourMinute.split( ":" ).length )
        {
            String fieldValueText = hourMinute.trim().split( ":" )[ field ];
            return Integer.parseInt( fieldValueText );
        }

        return -1;
    }

    public static String formatarComoConsumado( boolean consumado )
    {
        return ( consumado ) ? "SIM" : "NÃO";
    }

    public static String formatarComoPorcoes( double porcoes )
    {
        return String.format( "%." + DVML.CASAS_DECIMAIS + "f", porcoes );
    }

//    public static String formatarComoPorcoes ( double porcoes )
//    {
//        return String.format ( "%.2f Poções", porcoes );
//    }
    public static Boolean converterParaConsumado( String consumado )
    {
        if ( consumado.equalsIgnoreCase( "PAGO" ) )
        {
            return true;
        }
        if ( consumado.equalsIgnoreCase( "PENDENTE" ) )
        {
            return false;
        }

        return null;
    }

    public static String formatarComoTempo( int horas, int minutos )
    {

        DecimalFormat decimalFormat = new DecimalFormat( "##" );
        decimalFormat.setMinimumIntegerDigits( 2 );

        return decimalFormat.format( horas ) + ":" + decimalFormat.format( minutos );
    }

    public static Date fullDateParse( String date )
    {
        try
        {
            Date parse = new SimpleDateFormat( DD_MM_YYYY_HH_MM_SS ).parse( date );
            return parse;
        }
        catch ( ParseException ex )
        {
            return null;
        }
    }

    public static String fullDateToText( Date date )
    {
        return new SimpleDateFormat( DD_MM_YYYY_HH_MM_SS ).format( date );
    }

    public static String simpleDateToText( Date date )
    {
        return new SimpleDateFormat( DD_MM_YY_HHMMSS ).format( date );
    }

    public static String shortDateToText( Date date )
    {
        return new SimpleDateFormat( DD_MM_YYYY_SEPERADOR_HIFFEN ).format( date );
    }

    public static String dateToSQLDateFormat( Date date )
    {
        return new SimpleDateFormat( YYYY_MM_DD_HH_MM_SS ).format( date );
    }

    public static File getFile( String dirRelPath, String fileName )
    {
        File result = null;
        try
        {
            URL resource = new CfMethods().getClass().getResource( dirRelPath + fileName );
            if ( resource != null )
            {
                result = new File( resource.toURI() );
            }
        }
        catch ( URISyntaxException ex )
        {
            Logger.getLogger( CfMethods.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return result;
    }

    public static Date genDate( int... data )
    {
        Calendar instance = Calendar.getInstance( PORTUGAL_LOCALE );

        if ( data.length > 0 )
        {
            instance.set( Calendar.YEAR, data[ 0 ] );
        }
        if ( data.length > 1 )
        {
            instance.set( Calendar.MONTH, data[ 0 ] );
        }
        if ( data.length > 2 )
        {
            instance.set( Calendar.DATE, data[ 0 ] );
        }

        return instance.getTime();
    }

    public static BigDecimal parseMoedaSegura( String texto )
    {
        if ( texto == null || texto.trim().isEmpty() )
        {
            return BigDecimal.ZERO;
        }

        // Remove tudo que não for número, vírgula, ponto ou sinal negativo
        String valorLimpo = texto.replaceAll( "[^0-9,.-]", "" ).replace( ",", "." );

        try
        {
            return new BigDecimal( valorLimpo );
        }
        catch ( NumberFormatException ex )
        {
            System.err.println( "Erro ao converter valor monetário: \"" + texto + "\" -> \"" + valorLimpo + "\"" );
            return BigDecimal.ZERO;
        }
    }

//    public static String comverterNumeroPraTexto(double valor)
//    {
//        return new Extenso(new BigDecimal(valor)).toString();
//    }
}
