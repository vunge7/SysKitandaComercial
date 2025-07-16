/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.RetencaoJpaController;
import entity.Retencao;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
public class RetencaoDao extends RetencaoJpaController
{

    public RetencaoDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }
    
     public Vector <String > buscaTodos () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT i.taxa FROM Retencao i  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
//           result.add(0, "--SELECIONE--");
           return  result;
     }

    public Retencao findFirst ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e FROM Retencao  e ORDER BY e.pkRetencao  " );

        List<Retencao> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return null;

    }
    
    public Retencao findLast ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e FROM Retencao  e ORDER BY e.pkRetencao DESC " );

        List<Retencao> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return null;

    }
    
    public Retencao getRetencaoByTaxa(Double taxa)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Retencao a WHERE a.taxa = :taxa")
                .setParameter("taxa", taxa );
               
        List<Retencao> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
    
    public Retencao getImpostoByDescricao(String descricao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Retencao a WHERE a.descricao = :descricao")
                .setParameter("descricao", descricao );
               
        List<Retencao> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }

}
