/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ImpostoJpaController;
import entity.Imposto;
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
public class ImpostoDao extends ImpostoJpaController
{

    public ImpostoDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }
    
     public Vector <String > buscaTodos () 
     {         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT i.taxa FROM Imposto i ORDER BY i.taxa DESC ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           result.add(0, "--SELECIONE--");
           return  result;
     }

    public Imposto findFirst ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e FROM Imposto  e ORDER BY e.pkImposto  " );

        List<Imposto> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return null;

    }
    
    public Imposto findALL ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT  e FROM Imposto  e " );

        List<Imposto> result = query.getResultList ();
        em.close ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }
        
        return null;

    }
    
    public Imposto getImpostoByTaxa(Double taxa)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Imposto a WHERE a.taxa = :taxa")
                .setParameter("taxa", taxa );
               
        List<Imposto> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }
    
    public Imposto getImpostoByDescricao(String descricao)
    {           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM Imposto a WHERE a.descricao = :descricao")
                .setParameter("descricao", descricao );
               
        List<Imposto> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return null;
        
    }

}
