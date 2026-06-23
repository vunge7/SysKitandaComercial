/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.plu;

import java.math.BigDecimal;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 9/dez/2025
 * @lastModified 9/dez/2025
 */
public class LeitorDePeso
{

    /**
     * Extrai e calcula o peso (em Kg) de um código de barras interno de 13
     * dígitos no formato EAN-13, validando especificamente o prefixo '28'.
     *
     * @param codBarra O código de barras de 13 dígitos.
     * @return O peso do produto em quilogramas (double), ou -1.0 se o formato
     * for inválido.
     */
    public static double calcularPeso( String codBarra )
    {
        // 1. Validação de Comprimento
        if ( codBarra == null || codBarra.length() != 13 )
        {
            System.err.println( "Erro: O código de barras deve ter 13 dígitos." );
            return -1.0;
        }

        // 2. Validação do Prefixo
        if ( !codBarra.startsWith( "28" ) )
        {
            System.err.println( "Erro: O código de barras deve começar com '28' para ser um código de peso." );
            return -1.0;
        }

        // Estrutura do Código: 28 XXXXX 01030 2
        // Prefixo  Produto Peso/Preço Dígito Controlo
        try
        {
            // Os 5 dígitos do peso/preço estão tipicamente da 8ª à 12ª posição (índices 7 a 11)
            // Código de Barras: 2 | 800008 | 01030 | 2
            // Posição:         1   2..7     8..12  13
            // Índice Java:     0   1..6     7..11  12
            String strPeso = codBarra.substring( 7, 12 );
            int valorCodificado = Integer.parseInt( strPeso );

            // Assume-se que o valor codificado é em gramas ou unidades (ex: 1030 = 1.030 Kg)
            double pesoEmKg = ( double ) valorCodificado / 1000.0;

            return pesoEmKg;

        }
        catch ( NumberFormatException e )
        {
            System.err.println( "Erro ao converter a porção do peso para número: " + e.getMessage() );
            return -1.0;
        }
    }

    public static BigDecimal calcularPesoBD( String codBarra )
    {
        // posições 8 a 11 → peso
        String pesoStr = codBarra.substring( 8, 12 ); // ex: "0155"

        return new BigDecimal( pesoStr )
                .movePointLeft( 3 ); // 0.155 EXATO
    }

    public static void main( String[] args )
    {
//        String codValido = "2800008010302"; // Começa com 28 -> Válido
        String codValido = "2800019002655"; // Começa com 28 -> Válido

        System.out.println( "Peso do código " + codValido + ": " + calcularPeso( codValido ) + " kg" );
        System.out.println( "---" );
//        System.out.println( "Peso do código " + codInvalido1 + ": " + calcularPeso( codInvalido1 ) + " kg" );
//        System.out.println( "---" );
//        System.out.println( "Peso do código " + codInvalido2 + ": " + calcularPeso( codInvalido2 ) + " kg" );
    }
}
