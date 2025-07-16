/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbGrauAcademicoJpaController;
import entity.TbGrauAcademico;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.DVML.SELECCIONE;

/**
 *
 * @author Domingos Dala Vunge
 */
public class GrauAcademicoDao extends TbGrauAcademicoJpaController
{

    public GrauAcademicoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> buscaTodosExtra()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM TbGrauAcademico c ORDER BY c.designacao ASC " );
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbGrauAcademico> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbGrauAcademico p" );
        return query.getResultList();
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM TbGrauAcademico s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public List<TbGrauAcademico> buscaTodosGrauAcademicoes()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbGrauAcademico p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkGrauAcademico )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbGrauAcademico a WHERE a.pkGrauAcademico = :pkGrauAcademico" )
                .setParameter( "pkGrauAcademico", pkGrauAcademico );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public int getIdByDescricao( String descricao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.pkGrauAcademico FROM TbGrauAcademico a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public TbGrauAcademico getGrauAcademicoByDesignacao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbGrauAcademico c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<TbGrauAcademico> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbGrauAcademico( 0 );
    }

    public boolean exist_grauAcademico( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbGrauAcademico s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
