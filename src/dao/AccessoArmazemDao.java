/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.AccessoArmazemJpaController;
import entity.AccessoArmazem;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AccessoArmazemDao extends AccessoArmazemJpaController
{

    public AccessoArmazemDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<AccessoArmazem> getAllAccessoByIdUSuario( int cod_usuario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM AccessoArmazem a WHERE a.fkUsuario.codigo = :cod_usuario" )
                .setParameter( "cod_usuario", cod_usuario );

        List<AccessoArmazem> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public Vector<String> getAllArmazemByIdUSuario( int cod_usuario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.fkArmazem.designacao FROM AccessoArmazem a WHERE a.fkUsuario.codigo = :cod_usuario" )
                .setParameter( "cod_usuario", cod_usuario );

        List<String> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return ( Vector ) result;
        }
        return null;
    }
    
    public Vector<String> getAllArmazemExceptoEconomatoByIdUSuario( int cod_usuario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.fkArmazem.designacao FROM AccessoArmazem a WHERE a.fkUsuario.codigo = :cod_usuario AND a.fkArmazem.codigo > 1 " )
                .setParameter( "cod_usuario", cod_usuario );

        List<String> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return ( Vector ) result;
        }
        return null;
    }

    public boolean existe_accesso( int cod_usuario, int id_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM AccessoArmazem a WHERE a.fkUsuario.codigo = :cod_usuario AND a.fkArmazem.codigo = :id_armazem" )
                .setParameter( "cod_usuario", cod_usuario )
                .setParameter( "id_armazem", id_armazem );

        List<AccessoArmazem> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
