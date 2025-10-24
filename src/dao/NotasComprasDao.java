/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.NotasComprasJpaController;
import controlador.NotasJpaController;
import entity.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;

/**
 *
 * @author Toshiba
 */
public class NotasComprasDao extends NotasComprasJpaController
{

    public NotasComprasDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public void alterar_status_venda( int cod_venda, String status )
    {
        try
        {
            NotasCompras nota = findNotasCompras(cod_venda );
            nota.setStatusEliminado( status );
            edit( nota );
        }
        catch ( Exception e )
        {
            System.out.println( "Falha ao alterar o status da nota" );
        }

    }

    public List<NotasCompras> getFacturasEliminadas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.statusEliminado = 'true' ORDER BY v.pkNota ASC" );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public NotasCompras getFacturasEliminadaByCodVenda( int cod_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.statusEliminado = 'true' AND v.pkNota = :cod_venda" )
                .setParameter( "cod_venda", cod_venda );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public List<NotasCompras> getAllVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
                .setParameter( "id_usuario", id_usuario )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

//    public List<Notas> getAllNotas( Date data_inicio, Date data_fim )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT v FROM Notas  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' " )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//        List<Notas> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//
//        return result;
//    }
    public List<NotasCompras> getAllNotas( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );
        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return result;
    }

    public List<NotasCompras> getAllVenda( Date data_inicio, Date data_fim, int idDocumento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "idDocumento", idDocumento );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

    public List<TbCliente> getAllClienteVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbCliente> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbCliente cliente = new TbCliente( 0 );
        result.add( cliente );

        return result;
    }

    public List<TbCliente> getAllClienteVenda( Date data_inicio, Date data_fim, int idDocumento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "idDocumento", idDocumento );

        List<TbCliente> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbCliente cliente = new TbCliente( 0 );
        result.add( cliente );

        return result;
    }

    public List<TbProduto> getAllProdutosVenda( Date data_inicio, Date data_fim, int idDocumento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  DISTINCT v.fkProduto FROM NotasItem  v WHERE v.fkNota.dataNota BETWEEN :data_inicio AND :data_fim AND v.fkNota.statusEliminado = 'false' AND v.fkNota.fkDocumento.pkDocumento = :idDocumento" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "idDocumento", idDocumento );

        List<TbProduto> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbProduto produto = new TbProduto( 0 );
        result.add( produto );

        return result;
    }

    public List<NotasCompras> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

//    public List<Notas> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT v FROM Notas  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem AND v.fkDocumento.pkDocumento = :pk_documento" )
//                .setParameter( "pk_armazem", pk_armazem )
//                .setParameter( "pk_documento", pk_documento )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//
//        List<Notas> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//
//        Notas nota = new Notas( 0 );
//        result.add( nota );
//
//        return result;
//    }
    
    public List<NotasCompras> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem)
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT v FROM Notas  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem AND v.fkDocumento.pkDocumento = :pk_documento" )
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.estado = 'ANULADO' AND v.idArmazemFK.codigo = :pk_armazem" )
                .setParameter( "pk_armazem", pk_armazem )
//                .setParameter( "pk_documento", pk_documento )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

    public List<NotasCompras> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem, int pk_usuario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem AND v.codigoUsuario.codigo = :pk_usuario" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_usuario", pk_usuario );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

    public List<NotasCompras> getAllVendaByBetweenDataAndArmazemInverso( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Notas  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem ORDER BY v.pkNota DESC" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

    public double getTotalVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  SUM(v.totalVenda) AS total FROM Notas  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
                .setParameter( "id_usuario", id_usuario )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;
    }

    public int getLastNota()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkNota)  FROM Notas  v" );

        List<Integer> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() && result.get( 0 ) != null )
        {
            return result.get( 0 );
        }

        return 0;
    }

    public List<NotasCompras> getBuscaTodasVendasEntreDatas_e_Banco( int idBanco, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM NotasCompras  v WHERE v.dataNota BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.idBanco.idBanco = :idBanco" )
                .setParameter( "idBanco", idBanco )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<NotasCompras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        NotasCompras nota = new NotasCompras( 0 );
        result.add( nota );

        return result;
    }

    public double getAllEncomenda()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM Notas  v WHERE  v.statusEliminado = 'false' AND v.credito = 'true'  " );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;
    }

    public double getAllEncomendaByCliente( int pk_cliente )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM Notas  v WHERE   v.statusEliminado = 'false' AND v.credito = 'true'   AND v.codigoCliente.codigo = :pk_cliente" )
                .setParameter( "pk_cliente", pk_cliente );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;
    }

    public String getHashAnterior( String invoiceNo, BDConexao conexao )
    {
        String cod_fact_anteiror = getCodFactAnteiror( invoiceNo, conexao );
        System.out.println( "NotasCodFacAnterior: " + cod_fact_anteiror );
        try
        {
            return findByCodFact( cod_fact_anteiror ).getHashCod();

        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public String getCodFactAnteiror( String cod_fact, BDConexao conexao )
    {
        String sql = "SELECT cod_nota FROM kitanda_db.notas WHERE cod_nota <  '" + cod_fact + "' ORDER BY cod_nota DESC LIMIT 1";
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getString( "cod_nota" );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(NotasComprasDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return "";

    }

    /*
    public TbVenda findByCodFact( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Notas  v WHERE v.codNota = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }
     */

    public NotasCompras findByCodFact( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM NotasCompras  v WHERE v.codNota = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<NotasCompras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

}
