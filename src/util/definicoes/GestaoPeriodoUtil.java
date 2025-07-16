/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.definicoes;

import entity.FechoPeriodo;
import java.util.Vector;


/**
 *
 * @author Domiungos Dala Vunge
 */
public class GestaoPeriodoUtil
{

    public static Vector<String> getAllPeriodoEmFalta( FechoPeriodo last_periodo )
    {

        int idLastPeriodo = last_periodo.getFkPeriodo().getPkMesRh();
        Vector<String> vector = new Vector<>();
        switch ( idLastPeriodo )
        {

            case 0:
                vector.add("Janeiro");
                vector.add("Fevereiro");
                vector.add("Março");
                vector.add("Abril");
                vector.add("Abril");
                vector.add("Abril");
                vector.add("Abril");
                vector.add("Abril");
            case 1:

        }

        return vector;

    }

   
    public static void construirTabela( Vector<Vector> fonte )
    {
        String[] campos =
        {
            "Período" ,"Data de Abertura" ,"Data do Fecho"
        };
        Class[] classes_campos =
        {
            String.class ,String.class ,String.class
        };
        Boolean[] editable_cells =
        {
            false ,false ,false
        };
       // Tabela tabela = new Tabela(tabela_fecho_periodo ,campos ,classes_campos ,editable_cells ,fonte);

    }

}
