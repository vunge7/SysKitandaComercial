/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbFornecedorJpaController;
import entity.TbCliente;
import entity.TbFornecedor;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala 
 */
public class FornecedorDao extends TbFornecedorJpaController{

public static TbFornecedor findByNome ( String nome )
    {
        Query query = UtilDao.getEntityManager1 ().createQuery ( "SELECT DISTINCT a FROM TbFornecedor  a WHERE a.nome = :NOME " );
        query.setParameter ( "NOME", nome );

        List<TbFornecedor> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }

        return null;
    }



    
    public FornecedorDao() {
        this ( JPAEntityMannagerFactoryUtil.em );
    }
    
    public FornecedorDao(EntityManagerFactory emf) {
        super(emf);
    }
    
     public Vector <String> buscaTodos () {    
         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT f.nome FROM TbFornecedor f  ");
           Vector<String>  result  =   ( Vector )query.getResultList();                    
           return  result;
    }
     public Vector <String> buscaTodos5 () {    
         
           EntityManager em = getEntityManager();
           Query query = em.createQuery ("SELECT f.nome FROM TbFornecedor f  ");
           Vector<String>  result  =   ( Vector )query.getResultList();  
           result.add(0, "");
           return  result;
    }
     
    public TbFornecedor getFornecedorByDescricao(String descricao) {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbFornecedor  a WHERE a.nome = :designacao")
                .setParameter("designacao", descricao );
               
        List<TbFornecedor> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return new TbFornecedor(0);
        
    }
    
     public boolean existe_fornecedor (String nome) {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c.codigo FROM TbFornecedor c WHERE c.nome = :nome")
                    .setParameter("nome", nome);
            return  !query.getResultList().isEmpty();
            
    } 
     
    public List<TbFornecedor> getAllFornecedorOrdenado()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbFornecedor c ORDER BY c.nome ASC");
               
        List<TbFornecedor> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }

    
     public List<TbFornecedor> getFornecedorLIKE_Nome(String nome) {

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM TbFornecedor p WHERE p.nome LIKE :nome ORDER BY p.nome")
                .setParameter("nome", "%" + nome + "%");

        List<TbFornecedor> result = query.getResultList();
        em.close();

        if (result != null) {
            return result;
        }
        return null;
    }
     
    public TbFornecedor getFornecedorByNome( String nome )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT c FROM TbFornecedor  c WHERE c.nome = :nome" )
                .setParameter( "nome", nome );

        List<TbFornecedor> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new TbFornecedor( 0 );
    }
    
      public int getIdByDescricao (String nome)  {
         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.codigo FROM TbFornecedor s WHERE s.nome = :nome")
                    .setParameter("nome", nome);
            
            List result = query.getResultList();
            
            if( result!= null )
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            return 0;
            
    }
     
      
    public static List<TbFornecedor> buscarFornecedoresQueForneceramAoArmazem ( String desigancaoArmazem )
    {
        Query query = UtilDao.getEntityManager1 ().createQuery ( "SELECT DISTINCT a.fkFornecedor  FROM Compras  a WHERE a.idArmazemFK.designacao = :DESIGNACAO_ARMAZEM " );
        query.setParameter ( "DESIGNACAO_ARMAZEM", desigancaoArmazem );

        List<TbFornecedor> result = query.getResultList ();

        if (  ! result.isEmpty () )
        {
            return result;
        }

        return null;
    }
}
