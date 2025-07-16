/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.LinhaTransferenciaJpaController;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LinhaTransferenciaDao extends LinhaTransferenciaJpaController
{

    public LinhaTransferenciaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

}
