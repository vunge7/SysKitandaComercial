/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ItemCaixaJpaController;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ItemCaixaDao extends ItemCaixaJpaController
{

    public ItemCaixaDao( EntityManagerFactory emf )
    {
        super( emf );
    }
    
}
