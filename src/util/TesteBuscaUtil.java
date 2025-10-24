/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TesteBuscaUtil
{

    public static void main( String[] args )
    {
       
        
        List<String> lista_2 = new ArrayList<>();
        List<String> lista_1 = new ArrayList<>();
        lista_1.add( "Manuel Dala Vunge" );
        lista_1.add( "Isabel Calunga Samba" );
        lista_1.add( "Domingos Dala Vunge" );
        lista_1.add( "Ester Costa Mateus Zua" );

        lista_2.addAll( lista_1 );
        busca( lista_1, "Dala" );
        mostrar_dados( lista_1 );
        System.out.println( "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" );
        mostrar_dados( lista_2 );
    }

    private static void busca( List<String> list, String valor )
    {

        Iterator<String> it = list.iterator();
        while ( it.hasNext() )
        {
            String next = it.next();
            if ( !next.contains( valor ) )
            {
                it.remove();
            }
        }
    }

    private static void mostrar_dados( List<String> lista )
    {
        Iterator iterator = lista.iterator();
        while ( iterator.hasNext() )
        {
            Object next = iterator.next();
            System.out.println( " Valor: " + next );

        }

    }

}
