/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.GrupoJpaController;
import controlador.MarcaJpaController;
import controlador.TbFuncaoJpaController;
import entity.Grupo;
import entity.Marca;
import entity.TbFuncao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MarcaDao extends MarcaJpaController
{

    public MarcaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<Marca> buscaTodos()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM Marca p" );
        return query.getResultList();
    }

    public List<Marca> buscaTodasMarcas1()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM Marca p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public List<Marca> buscaTodasMarcas()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p.designacao FROM Marca p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public List<Marca> buscaAllEntity()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT p FROM Marca p  ORDER BY p.designacao" );
        return query.getResultList();
    }

    public String getDescricaoById( long pkGrupo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.designacao FROM Marca a WHERE a.pkGrupo = :pkGrupo" )
                .setParameter( "pkGrupo", pkGrupo );

        List list = query.getResultList();

        if ( list != null )
        {
            return String.valueOf( list.get( 0 ) );
        }
        return "";
    }

    public int getIdByDescricao( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a.pkMarca FROM Marca a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( result != null )
        {
            return Integer.parseInt( String.valueOf( result.get( 0 ) ) );
        }
        return 0;

    }

    public boolean exist_marca( String designacao )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s FROM Marca s WHERE s.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return true;
        }
        return false;
    }

    public Marca getMarcaByDescricao( String designacao )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM Marca a WHERE a.designacao = :designacao" )
                .setParameter( "designacao", designacao );

        List<Marca> result = query.getResultList();
        em.close();

        if ( result != null )
        {
            return result.get( 0 );
        }
        return null;

    }

}
