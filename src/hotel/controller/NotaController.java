/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.controller;


import java.sql.Connection;
import entity.Notas;
import java.util.List;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class NotaController
{
    //
    public static boolean actualizar_hash( List<Notas> list, BDConexao conexao )
    {

        for ( int i = 0; i < list.size(); i++ )
        {
            Notas notas = list.get( i );
            
            double totalGross =        MetodosUtil.getGetGrossTotalNotas(notas.getNotasItemList());
            System.out.println( "Invoice No: " +notas.getCodNota() );
            String hash = MetodosUtil.criptografia_hash( notas, totalGross, conexao );
            
            String sql = "UPDATE notas set hash_cod = '" + hash +"' WHERE pk_nota = " +notas.getPkNota();
            
            conexao.executeUpdate( sql );
            
        }
        
        return true;
    }

}
