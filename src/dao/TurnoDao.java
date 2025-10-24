/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;


import java.sql.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import controlador.TurnoJpaController;
import entity.Turno;
import java.util.Date;


/**
 *
 * @author Domingos Dala Vunge & Martinho Canhongo Luís
 */
public class TurnoDao extends TurnoJpaController{

    public TurnoDao(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List <Turno> buscaTodosTurno(Date date){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m FROM Turno m");
            return query.getResultList();
    }
    
    public Date getHoraAberturaTurno(int pk_turno, String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m.horaAbertura FROM Turno m WHERE m.pkTurno = :pk_turno AND m.turno = :designacao")
                    .setParameter("pk_turno", pk_turno)
                    .setParameter("designacao", designacao);
            return (Date)query.getSingleResult();
    }
    
    public Date getHoraFechoTurno(int pk_turno, String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m.horaAbertura FROM Turno m WHERE m.pkTurno = :pk_turno AND m.turno = :designacao")
                    .setParameter("pk_turno", pk_turno)
                    .setParameter("designacao", designacao);
            return (Date)query.getSingleResult();
    }
    
    public int last_turno_by_designacao_primeiro_segundo_turno(String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT MAX(m.pkTurno) FROM Turno m WHERE m.turno = :designacao")
                    .setParameter("designacao", designacao);
            return (int)query.getSingleResult();
    }
    
    
    public long last_turno_by_designacao(String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT MAX(m.pkTurno) FROM Turno m WHERE m.turno = :designacao")
                    .setParameter("designacao", designacao);
            return (long)query.getSingleResult();
    }
    
    
     public boolean existe_abertura_by_data(Date date , String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m FROM Turno m WHERE m.turno = :designacao AND m.data = :data")
                    .setParameter("designacao", designacao)
                    .setParameter("data", date);
            return  !query.getResultList().isEmpty();
    }
     
    
     public boolean existe_fecho_by_data(Date date , String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m FROM Turno m WHERE m.turno = :designacao AND m.data = :data AND m.horaFecho <> null")
                    .setParameter("designacao", designacao)
                    .setParameter("data", date);
            return  !query.getResultList().isEmpty();
    }
    
     
     
     public Turno getTurnoByData(Date date , String designacao){ 
        
            EntityManager em = getEntityManager();
            Query query = em.createQuery ("SELECT m FROM Turno m WHERE m.turno = :designacao AND m.data = :data AND m.horaAbertura <> null")
                    .setParameter("designacao", designacao)
                    .setParameter("data", date);
            return  (Turno)query.getSingleResult();
    }
      
    
    
}
