/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbItemSubsidiosFuncionarioJpaController;
import controlador.exceptions.NonexistentEntityException;
import entity.TbItemSubsidiosFuncionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Dallas
 */
public class ItemSubsidioFuncionarioDao extends TbItemSubsidiosFuncionarioJpaController{

    public ItemSubsidioFuncionarioDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<TbItemSubsidiosFuncionario> getAllItemSubsidiosFuncionarioes()
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM  TbItemSubsidiosFuncionario i  ORDER BY i.idFuncionarioFK.nome ASC");
            
        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    
    public List<TbItemSubsidiosFuncionario> getAllItemSubsidiosFuncionarioesByIdEmpresa(int pkEmpresa)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM  TbItemSubsidiosFuncionario i WHERE i.idFuncionarioFK.fkEmpresa.pkEmpresa =:pkEmpresa ORDER BY i.idFuncionarioFK.nome ASC")
            .setParameter("pkEmpresa", pkEmpresa);
        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    public List<TbItemSubsidiosFuncionario> getAllItemSubsidiosByIdProfessor(int idFuncionario)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM  TbItemSubsidiosFuncionario i WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario")
                .setParameter("idFuncionario", idFuncionario);
            
        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    public boolean exist_relacao_professor_disciplina(int idFuncionario, int idSubsidios)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbItemSubsidiosFuncionario i  WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario AND i.idSubsidioFK.idSubsidios = :idSubsidios")
          
                .setParameter("idFuncionario", idFuncionario)
                .setParameter("idSubsidios", idSubsidios);
            
        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
        {               
                return true;
        }       
        return false;
        
    }
    
    
    public TbItemSubsidiosFuncionario getItemSubsidioProfessorByIdProfessorAndIdSubsidio(int idFuncionario, int idSubsidio)
    {
        System.out.println("ID SUBISIDO : " +idSubsidio);   
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbItemSubsidiosFuncionario i  WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario AND i.idSubsidioFK.idSubsidios = :idSubsidio")
          
                .setParameter("idFuncionario", idFuncionario)
                .setParameter("idSubsidio", idSubsidio);
            
        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
        {               
                return result.get(0);
        }       
        return new TbItemSubsidiosFuncionario(0);
        
    }
    
    public List<TbItemSubsidiosFuncionario> getAllItemSubsidiosByIdFuncionario( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM  TbItemSubsidiosFuncionario i WHERE i.idFuncionarioFK.idFuncionario = :idFuncionario" )
                .setParameter( "idFuncionario", idFuncionario );

        List<TbItemSubsidiosFuncionario> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }
    
     public boolean remover_all_subsidios( int idFuncionario )
    {

        List<TbItemSubsidiosFuncionario> itens = getAllItemSubsidiosByIdFuncionario( idFuncionario );

        if ( itens != null )
        {
            for ( TbItemSubsidiosFuncionario iten : itens )
            {
                try
                {
                    destroy( iten.getIdItemSubsidiosFuncionario() );
                }
                catch ( NonexistentEntityException e )
                {
                    return false;
                }
            }
        }

        return true;

    }
    
    
    

}
