/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.FechoPeriodoJpaController;
//import controller.AnoJpaController;
import entity.FechoPeriodo;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FechoPeriodoDao extends FechoPeriodoJpaController
{

    public FechoPeriodoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public FechoPeriodo getFechoPeriodoByIdAnoAndPerido( int idAno, int idPeriodo )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM FechoPeriodo s WHERE s.fkAno.idAno = :idAno AND s.fkPeriodo.pkMesRh = :idPeriodo" )
                .setParameter( "idAno", idAno )
                .setParameter( "idPeriodo", idPeriodo );

        List<FechoPeriodo> list = query.getResultList();
        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return null;

    }

    public boolean existePeriodosAnoEconomico( int idAno )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM FechoPeriodo s WHERE s.fkAno.idAno = :idAno" )
                .setParameter( "idAno", idAno );

        return !query.getResultList().isEmpty();

    }

    public String getAbertura( Date dataAbertura )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM FechoPeriodo a WHERE a.dataAbertura = :dataAbertura" )
                .setParameter( "dataAbertura", dataAbertura );

        List<FechoPeriodo> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getDataAbertura().toString();
        }
        return "";

    }
    
    public String getFecho( Date dataFecho )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM FechoPeriodo a WHERE a.dataFecho = :dataFecho" )
                .setParameter( "dataFecho", dataFecho );

        List<FechoPeriodo> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getDataFecho().toString();
        }
        return "";

    }

    private int getLastIdPeriodo( int idAno )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT MAX(s.pkFechoPeriodo) FROM FechoPeriodo s WHERE s.fkAno.idAno = :idAno" )
                .setParameter( "idAno", idAno );

        List<Integer> list = query.getResultList();
        if ( !list.isEmpty() )
        {
            return list.get( 0 );
        }
        return 0;

    }

    public Vector<Vector> getFonteDados( int idAno )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM FechoPeriodo s WHERE s.fkAno.idAno = :idAno" )
                .setParameter( "idAno", idAno );

        List<FechoPeriodo> list = query.getResultList();

        Iterator<FechoPeriodo> iterator = list.iterator();
        Vector<Vector> fonte = new Vector<>();
        Vector vector;
        while ( iterator.hasNext() )
        {
            FechoPeriodo object = iterator.next();
            vector = new Vector();
            vector.add( object.getFkPeriodo().getDescricao() );
            vector.add( MetodosUtil.getDataBanco( object.getDataAbertura() ) );
            try
            {
                vector.add( MetodosUtil.getDataBanco( object.getDataFecho() ) );

            }
            catch ( Exception e )
            {
                vector.add( "00-00-0000 " );
            }

            fonte.add( vector );
        }

        return fonte;
    }

    public FechoPeriodo getLastFechoPeriodo( int idAno )
    {
        return findFechoPeriodo( getLastIdPeriodo( idAno ) );

    }

    public boolean existe_fecho( FechoPeriodo fechoPeriodo )
    {
        try
        {
            return fechoPeriodo.getDataFecho() != null;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    
    
    public boolean existe_abertura( FechoPeriodo fechoPeriodo )
    {
        try
        {
            return fechoPeriodo.getDataAbertura() != null;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public boolean existe_periodo( int idAno, int idPeriodo )
    {
        return getFechoPeriodoByIdAnoAndPerido( idAno, idPeriodo ) != null;
    }

}
