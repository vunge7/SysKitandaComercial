/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import controlador.TbSaidasProdutosJpaController;
import entity.TbEstorno;
import entity.TbSaidasProdutos;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Toshiba
 */
public class SaidasProdutosDao extends TbSaidasProdutosJpaController{

    public SaidasProdutosDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List <TbSaidasProdutos> buscaTodos() 
    {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT t FROM TbSaidasProdutos t ORDER BY t.pkSaidasProdutos DESC");
            
            return query.getResultList();
            
    }
    
     public List<TbSaidasProdutos> getAllSaidasByIdArmazem(int id_armazem,  Date data_inicio, Date data_fim)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  e AS TOTAL FROM TbSaidasProdutos  e WHERE e.idArmazemFK.codigo = :id_armazem  AND e.dataSaida BETWEEN :data_inicio AND :data_fim ")              
                .setParameter("id_armazem", id_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
     
     public List<TbSaidasProdutos> getAllSaida1(int pkSaidasProdutos)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbSaidasProdutos i WHERE i.pkSaidasProdutos = :pkSaidasProdutos")
                .setParameter("pkSaidasProdutos", pkSaidasProdutos);

            
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }
     
     public List<TbSaidasProdutos> getAllSaidaByIdArmazem(int codigo)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbSaidasProdutos i  WHERE i.idArmazemFK.codigo =  :codigo")
                .setParameter("codigo", codigo);
            
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }
     
    public List<TbSaidasProdutos> getAllSaidaById(int pkSaidasProdutos)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbSaidasProdutos i  WHERE i.pkSaidasProdutos =  :pkSaidasProdutos")
                .setParameter("pkSaidasProdutos", pkSaidasProdutos);
            
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }

    public boolean exis_saida( int pkSaidasProdutos)
    {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM TbSaidasProdutos r WHERE r.pkSaidasProdutos = :pkSaidasProdutos")
                .setParameter("pkSaidasProdutos", pkSaidasProdutos);
       return !query.getResultList().isEmpty();
       
    }
    
    public void eliminar_saidas_by_idSaida( int idSaida)
    {
        List<TbSaidasProdutos>  list = getAllSaidaByIdArmazem(idSaida);

        for (int i = 0; i < list.size(); i++) {                 
              try {   
                  destroy( list.get(i).getPkSaidasProdutos() );
              } catch (Exception ex) {
                  ex.printStackTrace();
                  Logger.getLogger(SaidasProdutosDao.class.getName()).log(Level.SEVERE, null, ex);
              }           
        }
    }
    
     public void eliminar_saidas_by_idSaida1(int idSaida)
    {
        List<TbSaidasProdutos>  list = getAllSaida1(idSaida);

        for (int i = 0; i < list.size(); i++) {                 
              try {   
                  destroy( list.get(i).getPkSaidasProdutos() );
              } catch (Exception ex) {
                  ex.printStackTrace();
                  Logger.getLogger(SaidasProdutosDao.class.getName()).log(Level.SEVERE, null, ex);
              }           
        }
    }
     
    public List<TbSaidasProdutos> getAllItemSaidas(int pkSaidasProdutos)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbSaidasProdutos i  WHERE i.pkSaidasProdutos =  :pkSaidasProdutos")
                .setParameter("pkSaidasProdutos", pkSaidasProdutos);
            
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    public List<TbEstorno> getAllEstornoByBetweenData(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataEstorno BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno saidasProdutos = new TbEstorno(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    public List<TbSaidasProdutos> getAllSaidaByBetweenData(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
     
    public List<TbSaidasProdutos> getAllSaidaByBetweenDataAndArmazem(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
     
    public TbSaidasProdutos getSaidasByArmazen(int codigo, Date data_inicio, Date data_fim)
    {   
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM TbSaidasProdutos p  WHERE p.idArmazemFK.codigo = :codigo AND p.dataSaida BETWEEN :data_inicio AND :data_fim")
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("codigo", codigo);
        
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if ( !result.isEmpty() )
            return result.get(0);
        else return null;
                
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
        public void alterar_status_saidasProdutos(int cod_saidasProdutos, String status)
    {       
        try {                
                 TbSaidasProdutos saidasProdutos = findTbSaidasProdutos(cod_saidasProdutos);
                 saidasProdutos.setStatusEliminado(status);                            
                 edit(saidasProdutos);
        } catch (Exception e) {
                System.out.println("Falha ao alterar o status da saidasProdutos");
        }

    }
    
    public List<TbSaidasProdutos> getFacturasEliminadas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.statusEliminado = 'true' ORDER BY v.pkTbSaidasProdutos ASC");
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
    }
    
     
    public TbSaidasProdutos getFacturasEliminadaByCodTbSaidasProdutos(int pkTbSaidasProdutos)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.statusEliminado = 'true' AND v.pkSaidasProdutos = :pkSaidasProdutos")
                .setParameter("pkTbSaidasProdutos", pkTbSaidasProdutos);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        return null;
    }
    
    
    public List<TbSaidasProdutos> getAllTbSaidasProdutosByUsuario(Integer id_usuario , Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.fkUsuario.codigo = :id_usuario AND v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")
                .setParameter("id_usuario", id_usuario)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    
    public List<TbSaidasProdutos> getAllTbSaidasProdutos(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    
    public List<TbSaidasProdutos> getAllTbSaidasProdutosByBetweenDataAndArmazem(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    public List<TbSaidasProdutos> getAllTbSaidasProdutosByBetweenDataAndArmazemInverso(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem ORDER BY v.pkTbSaidasProdutos DESC")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    
    
    
    
    public double getTotalTbSaidasProdutosByUsuario(Integer id_usuario , Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  SUM(v.totalSaidasProdutos) AS total FROM TbSaidasProdutos  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataSaidasProdutos BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")
                .setParameter("id_usuario", id_usuario)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<Double> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        
       
        return 0;
    }
    
    
    
    public Integer getLastSaidasProdutos()
    {
                
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  MAX(v.pkSaidasProdutos)  FROM TbSaidasProdutos  v");
                
        List<Integer> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
       
        return 0;
    
    }
    
     public int getIdSaidasProd (int pkSaidasProdutos) 
     {         
         
           System.err.println("Codigo: " +pkSaidasProdutos);
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.pkSaidasProdutos FROM TbSaidasProdutos s WHERE s.pkSaidasProdutos = :pkSaidasProdutos")
                    .setParameter("pkSaidasProdutos", pkSaidasProdutos);
            
            List result = query.getResultList();
            
            if( result!= null )
            {
              
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            }
            return 0;
            
    }

    
     public List<TbSaidasProdutos> getBuscaTodasTbSaidasProdutossEntreDatas(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbSaidasProdutos  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbSaidasProdutos> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbSaidasProdutos saidasProdutos = new TbSaidasProdutos(0);
        result.add(saidasProdutos);
        
        return result;
    }
    
    
    
}
