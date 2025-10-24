/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.definicoes;


import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TestePesquisaUtil
{

    public static void main( String[] args )
    {
        String campo = "Angola";
        boolean contains = campo.contains( "angola");
        Vector<String> lista = new Vector<>();
        lista.add( "Domingos Dala Vunge" );
        lista.add( "Isabel Samba Vunge" );
        lista.add( "Antónia Dala Vunge" );
        lista.add( "Manuel Dala Vunge" );

        Iterator iterator = lista.iterator();
        while ( iterator.hasNext() )
        {
            Object next = iterator.next();
            if ( next.toString().contains( "Samba"))
            {
                System.out.println( "Valor: " +next.toString());   
            }
        }

    }

}
