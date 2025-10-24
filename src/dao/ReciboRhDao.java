/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ReciboRhJpaController;
import controlador.TbSalarioJpaController;
import entity.ReciboRh;
import entity.TbSalario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ReciboRhDao extends ReciboRhJpaController
{

    public ReciboRhDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<ReciboRh> getAllSalario()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  ReciboRh a  ORDER BY a.idSalarioFK DESC" );

        List<ReciboRh> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public List<ReciboRh> getAllSalarioByIdEmpresa( int pkEmpresa )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  ReciboRh a WHERE a.idFuncionarioFK.fkEmpresa.pkEmpresa =:pkEmpresa  ORDER BY a.idSalarioFK DESC" )
                .setParameter( "pkEmpresa", pkEmpresa );
        List<ReciboRh> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public boolean exist_preofessor( long idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM ReciboRh s WHERE   s.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        return !query.getResultList().isEmpty();

    }

//     public double getSalarioByIdFuncionario(long idFuncionario)
//     {
//         
//         EntityManager em = getEntityManager();
//         Query query = em.createQuery("SELECT t FROM ReciboRh t WHERE  t.idFuncionarioFK.idFuncionario = :idFuncionario")
//                .setParameter("idFuncionario", idFuncionario);
//         List<ReciboRh>  result =  query.getResultList();
//        
//         if (!result.isEmpty() ) {
//             return result.get(0).getValorTempo();
//         } else {
//             return 0;
//         }
//  
//     }
    public List<ReciboRh> getSalarioLIKENome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM ReciboRh a WHERE  a.idFuncionarioFK.nome LIKE :nome ORDER BY a.idFuncionarioFK.nome" )
                .setParameter( "nome", nome + "%" );
        List<ReciboRh> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public List<ReciboRh> getSalarioByIdFuncionarios( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM ReciboRh a WHERE  a.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );
        List<ReciboRh> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result;
        }
        return null;
    }

    public int getLastRecibo()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkReciboRh)  FROM ReciboRh  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public boolean existeReciboSalarioFuncionario( int idFuncionario, int idAno, int idPeriodo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM ReciboRh a WHERE  a.fkFuncionario.idFuncionario = :idFuncionario AND a.fkFechoPeriodo.fkAno.idAno = :idAno AND a.fkFechoPeriodo.fkPeriodo.pkMesRh = :idPeriodo" )
                .setParameter( "idFuncionario", idFuncionario )
                .setParameter( "idAno", idAno )
                .setParameter( "idPeriodo", idPeriodo );
        List<ReciboRh> result = query.getResultList();
        em.close();
        return !result.isEmpty();

    }
    
//    public boolean existeReciboSalarioFuncionario( int idFuncionario, int idAno, int idPeriodo )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT a FROM ReciboRh a WHERE  a.fkFuncionario.idFuncionario = :idFuncionario AND a.fkFechoPeriodo.fkAno.idAno = :idAno AND a.fkFechoPeriodo.fkPeriodo.pkMesRh = :idPeriodo" )
//                .setParameter( "idFuncionario", idFuncionario )
//                .setParameter( "idAno", idAno )
//                .setParameter( "idPeriodo", idPeriodo );
//        List<ReciboRh> result = query.getResultList();
//        em.close();
//        return !result.isEmpty();
//
//    }
    
    
    

}
