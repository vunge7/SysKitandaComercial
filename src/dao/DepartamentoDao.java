/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbDepartamentoJpaController;
import entity.TbDepartamento;
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
public class DepartamentoDao extends TbDepartamentoJpaController
{

    public DepartamentoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> buscaTodosExtra()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM TbDepartamento c ORDER BY c.designacao ASC " );
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbDepartamento> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbDepartamento p" );
        return query.getResultList();
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM TbDepartamento s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public List<TbDepartamento> buscaTodosDepartamentoes()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbDepartamento p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkDepartamento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbDepartamento a WHERE a.pkDepartamento = :pkDepartamento" )
                .setParameter( "pkDepartamento", pkDepartamento );

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
        Query query = em.createQuery( "SELECT a.pkDepartamento FROM TbDepartamento a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public TbDepartamento getDepartamentoByDesignacao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbDepartamento c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<TbDepartamento> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbDepartamento( 0 );
    }

    public boolean exist_departamento( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbDepartamento s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
