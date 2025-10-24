/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbEspecialidadeJpaController;
import entity.TbEspecialidade;
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
public class EspecialidadeDao extends TbEspecialidadeJpaController
{

    public EspecialidadeDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> buscaTodosExtra()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM TbEspecialidade c ORDER BY c.designacao ASC " );
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbEspecialidade> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbEspecialidade p" );
        return query.getResultList();
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM TbEspecialidade s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public List<TbEspecialidade> buscaTodosEspecialidadees()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbEspecialidade p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkEspecialidade )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbEspecialidade a WHERE a.pkEspecialidade = :pkEspecialidade" )
                .setParameter( "pkEspecialidade", pkEspecialidade );

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
        Query query = em.createQuery( "SELECT a.pkEspecialidade FROM TbEspecialidade a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public TbEspecialidade getEspecialidadeByDesignacao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbEspecialidade c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<TbEspecialidade> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbEspecialidade( 0 );
    }

    public boolean exist_especialidade( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbEspecialidade s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
