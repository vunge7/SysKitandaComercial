/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbFuncaoJpaController;
import entity.TbFuncao;
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
public class FuncaoDao extends TbFuncaoJpaController
{

    public FuncaoDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public Vector<String> buscaTodosExtra()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c.designacao FROM TbFuncao c ORDER BY c.designacao ASC " );
        Vector<String> result = (Vector) query.getResultList();

//           result.add(0,"-- Seleccione --");
        return result;
    }

    public List<TbFuncao> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM TbFuncao p" );
        return query.getResultList();
    }

    public List<String> buscaTodosSeleccione()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.designacao FROM TbFuncao s" );
        List<String> resutl = query.getResultList();
        resutl.add( 0, SELECCIONE );
        return resutl;
    }

    public List<TbFuncao> buscaTodosFuncaoes()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM TbFuncao p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkFuncao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM TbFuncao a WHERE a.pkFuncao = :pkFuncao" )
                .setParameter( "pkFuncao", pkFuncao );

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
        Query query = em.createQuery( "SELECT a.pkFuncao FROM TbFuncao a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", descricao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public TbFuncao getFuncaoByDesignacao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbFuncao  c WHERE c.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<TbFuncao> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbFuncao( 0 );
    }

    public boolean exist_funcao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM TbFuncao s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

}
