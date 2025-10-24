/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import controlador.TbEstornoJpaController;
import entity.TbEstorno;
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
public class EstornoDao extends TbEstornoJpaController{

    public EstornoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List <TbEstorno> buscaTodos() 
    {         
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT t FROM TbEstorno t ORDER BY t.pkEstorno DESC");
            
            return query.getResultList();
            
    }
    
     public List<TbEstorno> getAllSaidasByIdArmazem(int id_armazem,  Date data_inicio, Date data_fim)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  e AS TOTAL FROM TbEstorno  e WHERE e.idArmazemFK.codigo = :id_armazem  AND e.dataSaida BETWEEN :data_inicio AND :data_fim ")              
                .setParameter("id_armazem", id_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();       

        if ( !result.isEmpty()) {
            return result;
        }
        return  null;
       
    }
     
     public List<TbEstorno> getAllSaida1(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbEstorno i WHERE i.pkEstorno = :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);

            
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }
     
     public List<TbEstorno> getAllSaidaByIdArmazem(int codigo)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbEstorno i  WHERE i.idArmazemFK.codigo =  :codigo")
                .setParameter("codigo", codigo);
            
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }
     
    public List<TbEstorno> getAllSaidaById(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbEstorno i  WHERE i.pkEstorno =  :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);
            
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
       
    }

    public boolean exis_saida( int pkEstorno)
    {
         
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM TbEstorno r WHERE r.pkEstorno = :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);
       return !query.getResultList().isEmpty();
       
    }
    
    public void eliminar_saidas_by_idSaida( int idSaida)
    {
        List<TbEstorno>  list = getAllSaidaByIdArmazem(idSaida);

        for (int i = 0; i < list.size(); i++) {                 
              try {   
                  destroy( list.get(i).getPkEstorno() );
              } catch (Exception ex) {
                  ex.printStackTrace();
                  Logger.getLogger(EstornoDao.class.getName()).log(Level.SEVERE, null, ex);
              }           
        }
    }
    
     public void eliminar_saidas_by_idSaida1(int idSaida)
    {
        List<TbEstorno>  list = getAllSaida1(idSaida);

        for (int i = 0; i < list.size(); i++) {                 
              try {   
                  destroy( list.get(i).getPkEstorno() );
              } catch (Exception ex) {
                  ex.printStackTrace();
                  Logger.getLogger(EstornoDao.class.getName()).log(Level.SEVERE, null, ex);
              }           
        }
    }
     
    public List<TbEstorno> getAllItemSaidas(int pkEstorno)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM TbEstorno i  WHERE i.pkEstorno =  :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);
            
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
        
    }
    
    public List<TbEstorno> getAllSaidaByBetweenData(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
     
    public List<TbEstorno> getAllSaidaByBetweenDataAndArmazem(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
     
    public TbEstorno getSaidasByArmazen(int codigo, Date data_inicio, Date data_fim)
    {   
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT p FROM TbEstorno p  WHERE p.idArmazemFK.codigo = :codigo AND p.dataSaida BETWEEN :data_inicio AND :data_fim")
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim)
                .setParameter("codigo", codigo);
        
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if ( !result.isEmpty() )
            return result.get(0);
        else return null;
                
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
        public void alterar_status_estorno(int cod_estorno, String status)
    {       
        try {                
                 TbEstorno estorno = findTbEstorno(cod_estorno);
                 estorno.setStatusEliminado(status);                            
                 edit(estorno);
        } catch (Exception e) {
                System.out.println("Falha ao alterar o status da estorno");
        }

    }
    
    public List<TbEstorno> getFacturasEliminadas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.statusEliminado = 'true' ORDER BY v.pkEstorno ASC");
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        return null;
    }
    
     
    public TbEstorno getFacturasEliminadaByCodTbEstorno(int pkEstorno)
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.statusEliminado = 'true' AND v.pkEstorno = :pkEstorno")
                .setParameter("pkEstorno", pkEstorno);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
        return null;
    }
    
    
    public List<TbEstorno> getAllTbEstornoByUsuario(Integer id_usuario , Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.fkUsuario.codigo = :id_usuario AND v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")
                .setParameter("id_usuario", id_usuario)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    
    public List<TbEstorno> getAllTbEstorno(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    
    public List<TbEstorno> getAllTbEstornoByBetweenDataAndArmazem(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    public List<TbEstorno> getAllTbEstornoByBetweenDataAndArmazemInverso(Date data_inicio, Date data_fim, int pk_armazem)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem ORDER BY v.pkTbEstorno DESC")             
                .setParameter("pk_armazem", pk_armazem)
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    
    
    
    
    public double getTotalTbEstornoByUsuario(Integer id_usuario , Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT  SUM(v.totalSaidasProdutos) AS total FROM TbEstorno  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataSaidasProdutos BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")
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
        Query query = em.createQuery("SELECT  MAX(v.pkEstorno)  FROM TbEstorno  v");
                
        List<Integer> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result.get(0);
       
        return 0;
    
    }
    
     public int getIdSaidasProd (int pkEstorno) 
     {         
         
           System.err.println("Codigo: " +pkEstorno);
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT s.pkSaidasProdutos FROM TbEstorno s WHERE s.pkEstorno = :pkEstorno")
                    .setParameter("pkEstorno", pkEstorno);
            
            List result = query.getResultList();
            
            if( result!= null )
            {
              
                return  Integer.parseInt(  String.valueOf( result.get(0) ) )  ;
            }
            return 0;
            
    }

    
     public List<TbEstorno> getBuscaTodasTbEstornosEntreDatas(Date data_inicio, Date data_fim)
    {
           
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT v FROM TbEstorno  v WHERE v.dataSaida BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'")             
                .setParameter("data_inicio", data_inicio)
                .setParameter("data_fim", data_fim);
               
        List<TbEstorno> result = query.getResultList();
        em.close();
       
        if( !result.isEmpty() )
                return result;
        
        TbEstorno estorno = new TbEstorno(0);
        result.add(estorno);
        
        return result;
    }
    
    
    
}
