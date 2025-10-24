/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbValidadeJpaController;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Dallas
 */
public class ValidadeDao extends TbValidadeJpaController{

    public ValidadeDao(EntityManagerFactory emf) {
        super(emf);
    }
    public static void main( String[] args )
    {
        System.err.println( "teste" );
    }
    
}
