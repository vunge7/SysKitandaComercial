/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbUsuarioJpaController;
import entity.TbCliente;
import entity.TbTipoUsuario;
import entity.TbUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class UsuarioDao extends TbUsuarioJpaController{

    public UsuarioDao(EntityManagerFactory emf) {
        super(emf);
    }
    public UsuarioDao() {
        this(UtilDao.emf);
    }
    
    public List<TbUsuario> getAllUsuarios()
    {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT u.nome FROM TbUsuario u ORDER BY u.nome ");
            
            return query.getResultList();
 
    }
    
    public List<TbUsuario> getAllUsuaioByIniciaisNome(String  nome ) {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a.nome FROM TbUsuario a WHERE a.nome LIKE :nome")
                .setParameter("nome",nome +"%");
       
        List<TbUsuario> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
    
     public boolean exist_aluno_nome_sobrenome(String nome, String sobre_nome)
     {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT a FROM TbUsuario a WHERE a.nome = :nome AND a.sobreNome = :sobreNome")
                .setParameter("nome", nome).setParameter("sobreNome", sobre_nome);
        return  !query.getResultList().isEmpty();
  
     }
    
     public Integer getUsuarioByNome1(String nome)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u.codigo FROM TbUsuario u  WHERE u.nome = :nome")
                .setParameter("nome", nome);
      
        List<Integer> result = query.getResultList();
         em.close();
        if( !result.isEmpty() )
                return result.get(0);
        return 0;
        
    }
    
    
    public boolean exist_usuario(String user_name, String senha)
    {
        
//        System.out.println("USER NAME: " +user_name);
//        System.out.println("SENHA: " +senha);
//        
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM TbUsuario u WHERE   u.userName = :user_name AND u.senha = :senha ")
                .setParameter("user_name", user_name)
                .setParameter("senha", senha);
        
        List<TbUsuario> result = query.getResultList();
        em.close();
        
        return !result.isEmpty();
    
    }
    
    
    public TbUsuario getUsuario(String user_name, String senha)
    {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM TbUsuario u WHERE   u.userName = :user_name AND u.senha = :senha ")
                .setParameter("user_name", user_name)
                .setParameter("senha", senha);
        
        List<TbUsuario> result = query.getResultList();
        em.close();
        
        if ( !result.isEmpty()) {
            return result.get(0);
        }
        
        TbUsuario usuario = new TbUsuario();
        usuario.setIdTipoUsuario( new TbTipoUsuario(0));
        return usuario;
    
    }
    
    
    
    public List<TbUsuario> getAllUsuario()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbUsuario  c");
               
        List<TbUsuario> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result;
        return null;
    }
    
    public TbUsuario getUsuarioByNome(String nome)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM TbUsuario  c WHERE c.userName = :nome")
                .setParameter("nome", nome);
               
        List<TbUsuario> result = query.getResultList();
        em.close();
       
        if( result!=null )
                return result.get(0);
        return new TbUsuario(0);
    }
    
    
    public List<String> getTodosUsuario(){
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery("SELECT u.nome FROM TbUsuario u ORDER BY u.nome ");
            return query.getResultList();
 
    }
    
     
     public List <TbCliente > buscaTodosClientes() 
     {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT c FROM TbCliente c");
            return query.getResultList();
     }
     
      public TbUsuario getLastInsertedRow ()
    {
        EntityManager em = getEntityManager ();
        Query query = em.createQuery ( "SELECT c FROM TbUsuario c ORDER BY c.codigo DESC" );

        List resultList = query.getResultList ();

        if (  ! resultList.isEmpty () )
        {
            return ( TbUsuario ) resultList.get ( 0 );
        }

        return null;
    }

    public TbUsuario getUsuariowithEncriptedPass ( String user_name, String senha )
    {
        EntityManager em = getEntityManager ();
        Query query = em.createNativeQuery ( "SELECT * FROM tb_usuario u WHERE   u.userName = ? AND u.senha =  encript_pass( ? )", TbUsuario.class );
        
        query.setParameter ( 1, user_name );
        query.setParameter ( 2, senha );

        List<TbUsuario> result = query.getResultList ();
    

        if (  ! result.isEmpty () )
        {
            return result.get ( 0 );
        }

        return null;

    }

    public String encriptPass ( String senha )
    {
        System.err.println ( "Senha a encriptar: "+senha );
        EntityManager em = getEntityManager ();
        Query query = em.createNativeQuery ( "SELECT   encript_pass( ? ) " );
        
        query.setParameter ( 1, senha );
        

        List result = query.getResultList ();
    

        if (  ! result.isEmpty () )
        {
            return ( String ) result.get ( 0 );
        }

        return null;

    }
    
    
    
    
}
