/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import comercial.controller.ExtratoContaClienteController;
import controlador.TbVendaJpaController;
import entity.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.DVML;
import static util.DVML.ESTADO_NOTA.*;
import util.DVML.ESTADO_NOTA;
import util.MetodosUtil;

/**
 *
 * @author Toshiba
 */
public class VendaDao extends TbVendaJpaController
{

    public VendaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<String> buscaTodasFactExceptoAlgumas()
    {
        EntityManager em = getEntityManager();
//            Query query = em.createQuery("SELECT s.designacao FROM Documento s  where s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7");
//            Query query = em.createQuery ( "SELECT v.codFact  FROM TbVenda  v WHERE V.fkDocumento.pkDocumento < "+DVML.DOC_FACTURA_PROFORMA_PP);
        Query query = em.createQuery( "SELECT v.codFact  FROM TbVenda  v WHERE V.fkDocumento.pkDocumento !=3 and V.fkDocumento.pkDocumento !=4 and V.fkDocumento.pkDocumento !=5 and V.fkDocumento.pkDocumento !=6 and V.fkDocumento.pkDocumento !=7" );
        List<String> lista = query.getResultList();

        lista.add( 0, "--Seleccione--" );
        return lista;
    }

    public void alterar_status_venda( int cod_venda, String status )
    {
        try
        {
            TbVenda venda = findTbVenda( cod_venda );
            venda.setStatusEliminado( status );
            edit( venda );
        }
        catch ( Exception e )
        {
            System.out.println( "Falha ao alterar o status da venda" );
        }

    }

    public List<TbVenda> getFacturasEliminadas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.statusEliminado = 'true' ORDER BY v.codigo ASC" );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public TbVenda getFacturasEliminadaByCodVenda( int cod_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.statusEliminado = 'true' AND v.codigo = :cod_venda" )
                .setParameter( "cod_venda", cod_venda );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public List<TbVenda> getAllVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
                .setParameter( "id_usuario", id_usuario )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public List<TbVenda> getAllVendasAmoritzClientes( int pk_documento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery( "SELECT * FROM tb_venda v, tb_cliente c, amortizacao_divida a WHERE v.codigo_cliente = c.codigo AND v.status_eliminado = 'false' AND v.fk_documento = 2 AND a.total_venda_fact > a.valor_pago GROUP BY c.nome ORDER BY c.nome ASC", TbVenda.class )
                //        Query query = em.createNativeQuery("SELECT * FROM tb_venda v, tb_cliente c WHERE v.codigo_cliente = c.codigo", TbVenda.class)
                .setParameter( "pk_documento", pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public List<TbVenda> getAllVendasClientes( int pk_documento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery( "SELECT * FROM tb_venda v, tb_cliente c WHERE v.codigo_cliente = c.codigo AND v.status_eliminado = 'false' AND v.fk_documento = 2 GROUP BY c.nome ORDER BY c.nome ASC", TbVenda.class )
                //        Query query = em.createNativeQuery("SELECT * FROM tb_venda v, tb_cliente c WHERE v.codigo_cliente = c.codigo", TbVenda.class)
                .setParameter( "pk_documento", pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public List<TbVenda> getAllVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public List<TbVenda> getAllVenda( Date data_inicio, Date data_fim, int idDocumento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "idDocumento", idDocumento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

//    public List<TbCliente> getAllClienteVenda( Date data_inicio, Date data_fim )
//    {
//
//        EntityManager em = getEntityManager();
////        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
//        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim " )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//
//        List<TbCliente> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//
//        TbCliente cliente = new TbCliente( 0 );
//        result.add( cliente );
//
//        return result;
//    }
    public List<TbCliente> getAllClienteVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
//        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim" )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );

        String sql = "SELECT DISTINCT c.* FROM tb_venda v , tb_cliente c where  v.codigo_cliente = c.codigo AND  DATE(dataVenda)      between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND fk_documento IN(1,2,6)";

        Query query = em.createNativeQuery( sql, TbCliente.class );
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
        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento" )
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
        Query query = em.createQuery( "SELECT  DISTINCT v.codigoProduto FROM TbItemVenda  v WHERE v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND v.codigoVenda.statusEliminado = 'false' AND v.codigoVenda.fkDocumento.pkDocumento = :idDocumento" )
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

    public List<TbVenda> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

//    public List<TbVenda> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
//    {
//
//        EntityManager em = getEntityManager();
//
//        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
//                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
//                + "AND status_eliminado = 'false' "
//                + "AND credito = 'false' "
//                + "AND idArmazemFK = ? "
//                + "AND fk_documento = ? ", TbVenda.class );
//
//        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
//        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
//        query.setParameter( 3, pk_armazem );
//        query.setParameter( 4, pk_documento );
//
//        List<TbVenda> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//        return result;
//    }
    public List<TbVenda> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                //                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getAllFRVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getAllFRVendaByBetweenDataAndArmazemAndDocumentoRecolha( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                //                + "ORDER BY cod_fact DESC "
                + "AND fk_documento = ? ORDER BY cod_fact DESC ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public static Integer criarVendaComProceduAnulacao( TbVenda venda, BDConexao conexao )
    {
        System.err.println( "Total: " + venda.getTotalVenda() );

        String inserirVendaQuery = String.format( "select VENDA_CRIAR ( "
                + "%d, '%s', %s, '%s', "
                + "'%s', %s, %s, '%s',"
                + " '%s', '%s',  %s, %s,"
                + " %s, '%s',  '%s', '%s',"
                + " '%s', '%s',  '%s', %d,"
                + " %s, %s,  %s, '%s',"
                + " '%s', %d, '%d',"
                + " %d,  %d, %d,  %d,"
                + " '%s',  '%s', %s, '%s', '%s', '%s') "
//                + " '%s',  '%s', %s, '%s', '%s', '%s', %s, %s , '%s') "
                + "as ID",
                0,
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVenda() ),
                venda.getTotalVenda(),
                venda.getPerformance(),
                venda.getCredito(),
                venda.getValorEntregue(),
                venda.getTroco(),
                new SimpleDateFormat( "HH:mm:ss" ).format( venda.getHora() ),
                venda.getNomeCliente(),
                venda.getStatusEliminado(),
                venda.getDescontoTotal(),
                venda.getTotalIva(),
                venda.getTotalGeral(),
                venda.getCodFact(),
                venda.getAssinatura(),
                venda.getHashCod(),
                venda.getObs(),
                venda.getRefCodFact(),
                venda.getTotalPorExtenso(),
                venda.getStatusRecibo(),
                venda.getDescontoComercial(),
                venda.getDescontoFinanceiro(),
                venda.getTotalIncidencia(),
                venda.getLocalCarga(),
                venda.getLocalDescarga(),
                venda.getCodigoUsuario().getCodigo(),
                venda.getCodigoCliente().getCodigo(),
                venda.getIdArmazemFK().getCodigo(),
                venda.getFkDocumento().getPkDocumento(),
                venda.getFkAnoEconomico().getPkAnoEconomico(),
                venda.getFkCambio().getPkCambio(),
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVencimento() ),
                venda.getClienteNif(),
                venda.getTotalIncidenciaIsento(),
                ( venda.getRefDataFact() != null ? new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getRefDataFact() ) : new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) ),
                venda.getNomeConsumidorFinal(),
                venda.getReferencia()
        );

        System.out.println( inserirVendaQuery );
        ResultSet resultSet = conexao.executeQuery( inserirVendaQuery );

        try
        {
            if ( resultSet.next() )
            {
                return resultSet.getInt( "ID" );

            }
        }
        catch ( SQLException ex )
        {

            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;

    }
    public static Integer criarVendaComProceduAnulacao2( TbVenda venda, BDConexao conexao )
    {
        System.err.println( "Total: " + venda.getTotalVenda() );

        String inserirVendaQuery = String.format( "select NOTA_VENDA_CRIAR ( "
                + "%d, '%s', %s, '%s', "
                + "'%s', %s, %s, '%s',"
                + " '%s', '%s',  %s, %s,"
                + " %s, '%s',  '%s', '%s',"
                + " '%s', '%s',  '%s', %d,"
                + " %s, %s,  %s, '%s',"
                + " '%s', %d, '%d',"
                + " %d,  %d, %d,  %d,"
                + " '%s',  '%s', %s, '%s', '%s', '%s') "
//                + " '%s',  '%s', %s, '%s', '%s', '%s', %s, %s , '%s') "
                + "as ID",
                0,
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVenda() ),
                venda.getTotalVenda(),
                venda.getPerformance(),
                venda.getCredito(),
                venda.getValorEntregue(),
                venda.getTroco(),
                new SimpleDateFormat( "HH:mm:ss" ).format( venda.getHora() ),
                venda.getNomeCliente(),
                venda.getStatusEliminado(),
                venda.getDescontoTotal(),
                venda.getTotalIva(),
                venda.getTotalGeral(),
                venda.getCodFact(),
                venda.getAssinatura(),
                venda.getHashCod(),
                venda.getObs(),
                venda.getRefCodFact(),
                venda.getTotalPorExtenso(),
                venda.getStatusRecibo(),
                venda.getDescontoComercial(),
                venda.getDescontoFinanceiro(),
                venda.getTotalIncidencia(),
                venda.getLocalCarga(),
                venda.getLocalDescarga(),
                venda.getCodigoUsuario().getCodigo(),
                venda.getCodigoCliente().getCodigo(),
                venda.getIdArmazemFK().getCodigo(),
                venda.getFkDocumento().getPkDocumento(),
                venda.getFkAnoEconomico().getPkAnoEconomico(),
                venda.getFkCambio().getPkCambio(),
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVencimento() ),
                venda.getClienteNif(),
                venda.getTotalIncidenciaIsento(),
                ( venda.getRefDataFact() != null ? new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getRefDataFact() ) : new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) ),
                venda.getNomeConsumidorFinal(),
                venda.getReferencia()
        );

        System.out.println( inserirVendaQuery );
        ResultSet resultSet = conexao.executeQuery( inserirVendaQuery );

        try
        {
            if ( resultSet.next() )
            {
                return resultSet.getInt( "ID" );

            }
        }
        catch ( SQLException ex )
        {

            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;

    }

    public List<TbVenda> getAllFRVendaByBetweenDataAndArmazemAndDocumentoRecolha1( Date data_inicio, Date data_fim, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                //                + "ORDER BY cod_fact DESC "
                + "AND fk_documento = ? ORDER BY cod_fact DESC ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getAllFTVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem, int pk_usuario, int pk_documento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem AND v.codigoUsuario.codigo = :pk_usuario AND v.fkDocumento.pkDocumento = :pk_documento" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_usuario", pk_usuario )
                .setParameter( "pk_documento", pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public List<TbVenda> getAllVendaByBetweenDataAndArmazemInverso( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem ORDER BY v.codigo DESC" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public double getTotalVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  SUM(v.totalVenda) AS total FROM TbVenda  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
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

    public static double getTotalRetencaoByRefCod( String codFact )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT v.totalRetencao FROM TbVenda v WHERE v.codFact = :codFact " );
        query.setParameter( "COD_FACT", codFact );

//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT  SUM(v.totalRetencao) AS totalRetencao FROM TbVenda  v WHERE v.codFact = " )
////      Query query = em.createQuery( "SELECT  SUM(v.totalRetencao) AS totalRetencao FROM TbVenda  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
//                .setParameter( "codFact", codFact );
        List<Double> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0.0;
    }

    public int getLastVenda()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.codigo)  FROM TbVenda  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public int getLastVenda( int pk_documento, int pk_ano_economico )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.codigo)  FROM TbVenda  v WHERE v.fkDocumento.pkDocumento = :pk_documento AND v.fkAnoEconomico.pkAnoEconomico = :pk_ano_economico" )
                .setParameter( "pk_documento", pk_documento )
                .setParameter( "pk_ano_economico", pk_ano_economico );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            try
            {
                return result.get( 0 );
            }
            catch ( Exception e )
            {
                return 0;
            }
        }

        return 0;

    }

    public int getLastVenda( int pk_documento )
    {

        System.out.println( "ID DOCUMENTO: " + pk_documento );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.codigo)  FROM TbVenda  v WHERE v.fkDocumento.pkDocumento = :pk_documento" )
                .setParameter( "pk_documento", pk_documento );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            try
            {
                return result.get( 0 );
            }
            catch ( Exception e )
            {
                return 0;
            }

        }

        return 0;

    }

    public List<TbVenda> getBuscaTodasVendasEntreDatas_e_Banco( int idBanco, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.idBanco.idBanco = :idBanco" )
                .setParameter( "idBanco", idBanco )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        TbVenda venda = new TbVenda( 0 );
        result.add( venda );

        return result;
    }

    public double getAllEncomenda()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM TbVenda  v WHERE  v.statusEliminado = 'false' AND v.credito = 'true'  " );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;
    }

//    public double getAllEncomendaByCliente( int pk_cliente )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM TbVenda  v WHERE   v.statusEliminado = 'false' AND v.credito = 'true'   AND v.codigoCliente.codigo = :pk_cliente" )
//                .setParameter( "pk_cliente", pk_cliente );
//
//        List<Double> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result.get( 0 );
//        }
//        return 0;
//    }
    public Object[] listarVendaDoDocumento()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM TbVenda  v WHERE V.codFact LIKE '%FR%' ORDER BY v.codFact DESC" );
        ;

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return (Object[]) documentos.toArray();
        }

        return null;
    }

    public Object[] listarVendaDoDocumentoValidas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM TbVenda  v WHERE V.codFact LIKE '%FR%'  OR V.codFact LIKE '%FT%'   AND v.codFact NOT IN :FATURAS_INVALIDAS ORDER BY v.codFact DESC" );

        Query queryFaturasInvalidas = em.createQuery( "SELECT DISTINCT v.refCodFact  FROM Notas  v WHERE v.estado = :TOTALMENTE_RETIFICADA OR v.estado = :ANULADA" );

        queryFaturasInvalidas.setParameter( "TOTALMENTE_RETIFICADA", TOTALMENTE_RETIFICADO.toString() );
        queryFaturasInvalidas.setParameter( "ANULADA", ANULADO.toString() );

        List<String> resultList = queryFaturasInvalidas.getResultList();

        if ( resultList.isEmpty() )
        {
            resultList.add( "0" );
        }

        query.setParameter( "FATURAS_INVALIDAS", resultList );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return (Object[]) documentos.toArray();
        }

        return null;
    }

    public static TbVenda findByCodFact( String codFact )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :COD_FACT " );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactReemprensao1( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :codFact AND v.statusEliminado = 'ANULADO' " );
        query.setParameter( "codFact", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactReemprensao( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT " );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> vendas = query.getResultList();
        em.close();

        if ( !vendas.isEmpty() )
        {
            return vendas.get( 0 );
        }

        return null;
    }

//    public TbVenda findByCodFactReemprensao(String codFact) {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT AND V.statusEliminado = 'ANULADO' ");
//        query.setParameter("COD_FACT", codFact);
//
//        List<TbVenda> documentos = query.getResultList();
//        em.close();
//
//        if (!documentos.isEmpty()) {
//            return documentos.get(0);
//        }
//
//        return null;
//    }
    public TbVenda findByCodFactReemprensaoNota( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT AND V.statusEliminado = 'true' " );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactReemprensaoNota1( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :codFact AND V.statusEliminado = 'ANULADO' " );
        query.setParameter( "codFact", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFact( String codFact, int tipo_documento )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT AND v.fkDocumento.pkDocumento = :TIPO_DOC" );
        query.setParameter( "COD_FACT", codFact );
        query.setParameter( "TIPO_DOC", tipo_documento );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactura( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public List<TbProduto> getAllProdutosVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT  DISTINCT v.codigoProduto FROM TbItemVenda  v WHERE v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND v.codigoVenda.statusEliminado = 'false' " )
//        Query query = em.createQuery( "SELECT  DISTINCT v.codigoProduto FROM TbItemVenda  v WHERE v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim " )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );

        String sql = "SELECT DISTINCT p.* FROM tb_item_venda i, tb_produto p , tb_venda v WHERE i.codigo_produto = p.codigo AND i.codigo_venda =  v.codigo AND DATE(v.dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND v.fk_documento IN(1,2)";
        Query query = em.createNativeQuery( sql, TbProduto.class );

        List<TbProduto> result = query.getResultList();

        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

//        TbProduto produto = new TbProduto(0);
//        result.add(produto);
        return result;
    }

    public List<TbVenda> getAllVendaExceptoFacturaProformaAndRecibo( Date data_inicio, Date data_fim )
    {
        ArrayList<Integer> listaDocAExcluir = new ArrayList<>();

        listaDocAExcluir.add( DVML.DOC_FACTURA_PROFORMA_PP );
        listaDocAExcluir.add( DVML.DOC_RECIBO_RC );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento NOT IN :DOC_A_EXCLUIR" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "DOC_A_EXCLUIR", listaDocAExcluir );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public List<TbVenda> getAllRecibo( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();

        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND fk_documento IN(6)";

        System.out.println( sql );
        Query query = em.createNativeQuery( sql, TbVenda.class );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

//
//    public List<TbVenda> getAllVendaExceptoFacturaProformaAndRecibo(Date data_inicio, Date data_fim) {
//
//        int factura_proforma = DVML.DOC_FACTURA_PROFORMA_PP;
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento <> :pk_docummento")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pk_docummento", factura_proforma);
//
//        List<TbVenda> result = query.getResultList();
//        em.close();
//
//        if (!result.isEmpty()) {
//            return result;
//        }
//
//        return result;
//    }
    public List<TbVenda> getAllFacturaProforma( Date data_inicio, Date data_fim )
    {

        int factura_proforma = DVML.DOC_FACTURA_PROFORMA_PP;
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento = :pk_docummento" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_docummento", factura_proforma );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return result;
    }

    public Object[] listarVendasRetificaveis()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM TbVenda v WHERE v.codFact NOT IN :IMPRESSORAS_INALTERAVEIS  " );

        Query queryFaturasAnuladasRetificadas = em.createQuery( "SELECT t.refCodFact FROM Notas t WHERE t.estado = :NOTA_ANULADA OR t.estado = :NOTA_TOTALMENTE_ALTERADA" );
        queryFaturasAnuladasRetificadas.setParameter( "NOTA_ANULADA", ESTADO_NOTA.ANULADO.toString() );
        queryFaturasAnuladasRetificadas.setParameter( "NOTA_TOTALMENTE_ALTERADA", ESTADO_NOTA.TOTALMENTE_RETIFICADO.toString() );
        List resultList = queryFaturasAnuladasRetificadas.getResultList();
        System.err.println( "faturasAnuladasRetificadas: " + resultList );
        query.setParameter( "IMPRESSORAS_INALTERAVEIS", resultList );

        if ( resultList.isEmpty() )
        {
            resultList.add( "0" );
        }

        List<TbVenda> documentos = query.getResultList();
        System.err.println( "" );

        if ( !documentos.isEmpty() )
        {
            return (Object[]) documentos.toArray();
        }

        return null;
    }

    public boolean existe_codFact( String cod_fact )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v.codigo FROM TbVenda v WHERE v.statusRecibo = TRUE  AND v.statusEliminado = 'false' AND v.codFact = :cod_fact" )
                .setParameter( "cod_fact", cod_fact );
        return !query.getResultList().isEmpty();

    }

    public boolean existe_codFact( String cod_fact, BDConexao conexao )
    {
        try
        {
            //"SELECT v.codigo FROM TbVenda v WHERE v.statusRecibo = TRUE  AND v.statusEliminado = 'false' AND v.codFact = :cod_fact"
            String query = "SELECT * FROM tb_venda WHERE status_recibo = true AND status_eliminado = 'false' AND cod_fact = '" + cod_fact + "' ";
            System.err.println( query );
            ResultSet resultSet = conexao.executeQuery( query );
            return resultSet.next();
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return false;

    }

    public boolean actualiza_status_recibo( String cod_fact, BDConexao conexao )
    {

        String sql = "UPDATE tb_venda SET status_recibo = true WHERE cod_fact = '" + cod_fact + "'";
        return conexao.executeUpdate( sql );

    }

    public String getCodFactAnteiror( String cod_fact, BDConexao conexao )
    {
        String sql = "SELECT cod_fact FROM tb_venda WHERE cod_fact <  '" + cod_fact + "' ORDER BY cod_fact DESC LIMIT 1";
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getString( "cod_fact" );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return "";

    }

    public String getCodFactAnteiror( String cod_fact, int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
        String sql = "SELECT cod_fact FROM tb_venda WHERE fk_documento = " + pk_documento + " AND  fk_ano_economico = " + pk_ano_economico + "   AND cod_fact <  '" + cod_fact + "' ORDER BY cod_fact DESC LIMIT 1";
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getString( "cod_fact" );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return "";

    }

    public String getHashAnterior( String cod_fact, int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
        String cod_fact_anteiror = getCodFactAnteiror( cod_fact, pk_documento, pk_ano_economico, conexao );
        try
        {
            return findByCodFact( cod_fact_anteiror ).getHashCod();
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public String getHashAnterior( String cod_fact, BDConexao conexao )
    {
        String cod_fact_anteiror = getCodFactAnteiror( cod_fact, conexao );
        try
        {
            return findByCodFact( cod_fact_anteiror ).getHashCod();
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    public Object[] listarDocumentoRetificaveisComOsSeguintesPKs( Integer... documentosSelecionados )
    {
        ArrayList<Integer> documentos = new ArrayList<>();

        for ( Integer documentosSelecionado : documentosSelecionados )
        {
            documentos.add( documentosSelecionado );
        }
        if ( documentos.isEmpty() )
        {
            documentos.add( 0 );
        }

        EntityManager em = getEntityManager();

        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM TbVenda v WHERE (v.codigoCliente.nome != :CONSUMIDOR_FINAL ) AND ( v.codFact NOT IN :IMPRESSORAS_INALTERAVEIS) AND ( v.fkDocumento.pkDocumento IN :DOCUMENTOS_SELECIONADOS )  " );

        Query queryFaturasAnuladasRetificadas = em.createQuery( "SELECT t.refCodFact FROM Notas t WHERE t.estado = :NOTA_ANULADA OR t.estado = :NOTA_TOTALMENTE_ALTERADA" );
        queryFaturasAnuladasRetificadas.setParameter( "NOTA_ANULADA", ESTADO_NOTA.ANULADO.toString() );
        queryFaturasAnuladasRetificadas.setParameter( "NOTA_TOTALMENTE_ALTERADA", ESTADO_NOTA.TOTALMENTE_RETIFICADO.toString() );
        List documentosInalteraveis = queryFaturasAnuladasRetificadas.getResultList();
        System.err.println( "faturasAnuladasRetificadas: " + documentosInalteraveis );
        query.setParameter( "IMPRESSORAS_INALTERAVEIS", documentosInalteraveis );
        query.setParameter( "DOCUMENTOS_SELECIONADOS", documentos );
        query.setParameter( "CONSUMIDOR_FINAL", DVML._CLIENTE_CONSUMIDOR_FINAL );

        if ( documentosInalteraveis.isEmpty() )
        {
            documentosInalteraveis.add( "0" );
        }

        List<TbVenda> resultList = query.getResultList();
        System.err.println( "" );

        if ( !resultList.isEmpty() )
        {
            return (Object[]) resultList.toArray();
        }

        return null;
    }

//    public int getUltimaContagemByIdDocumentoAndAnoEconomico( int pk_documento, int pk_ano_economico )
//    {
//        try
//        {
//
//            TbVenda venda_local = findTbVenda( getLastVenda( pk_documento, pk_ano_economico ) );
//
//            if ( venda_local != null )
//            {
//                String cod = venda_local.getCodFact().split( "/" )[1];
//                //System.out.println( "Codigo Ordem Documento: " + cod );
//                return Integer.parseInt( cod );
//            }
//            else
//            {
//                return 0;
//            }
//        }
//        catch ( Exception e )
//        {
//            // e.printStackTrace();
//            System.out.println( "Codigo Ordem Documento: " + 0 );
//        }
//
//        return 0;
//
//    }
    public Integer getLastCodigoVenda( int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
        System.err.println( "PK_DOCUMENTO: " + pk_documento );
        System.err.println( "PK_ANO_ECONOMICO: " + pk_ano_economico );
        String query = "SELECT MAX(codigo) AS last_id FROM tb_venda WHERE fk_documento = " + pk_documento + " AND fk_ano_economico = " + pk_ano_economico;

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer last_id = resultSet.getInt( "last_id" );
                return last_id;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0;

    }

    public int getUltimaContagemByIdDocumentoAndAnoEconomico( int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
        try
        {
            Integer codVenda = getLastCodigoVenda( pk_documento, pk_ano_economico, conexao );
            System.out.println( "LastCodVenda: " + codVenda );
            String codFact = MetodosUtil.getCodFact( codVenda, conexao );
            System.out.println( "LastCodFact: " + codFact );

            if ( codFact != null )
            {
                String cod = codFact.split( "/" )[ 1 ];
                //System.out.println( "Codigo Ordem Documento: " + cod );
                return Integer.parseInt( cod );
            }
            else
            {
                return 0;
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.out.println( "Codigo Ordem Documento: " + 0 );
        }

        return 0;

    }

    public static Integer criarVendaComProcedu( TbVenda venda, BDConexao conexao )
    {
        System.err.println( "Total: " + venda.getTotalVenda() );

        /**
         * codigo int(11), dataVenda datetime, total_venda double , performance
         * varchar(45) credito varchar(45), valor_entregue double , troco
         * double, hora time nome_cliente varchar(45) , status_eliminado
         * varchar(45), desconto_total double, total_iva double, total_geral
         * double , cod_fact varchar(100), assinatura varchar(100), hash_cod
         * varchar(1000), obs varchar(200), ref_cod_fact varchar(100),
         * total_por_extenso varchar(200), status_recibo tinyint(4),
         * desconto_comercial double, desconto_financeiro double,
         * total_incidencia double, local_carga varchar(75), local_descarga
         * varchar(75), idBanco int(11), codigo_usuario int(11) , codigo_cliente
         * int(11) , idArmazemFK int(10) , fk_documento int(11) ,
         * fk_ano_economico int(11) , fk_cambio int(11) , dataVencimento date,
         * cliente_nif varchar(75), total_incidencia_isento double,
         * ref_data_fact datetime, nome_consumidor_final varchar(45), referencia
         * varchar(45), matricula varchar(45), modelo varchar(45), num_chassi
         * varchar(45), num_motor varchar(45), kilometro varchar(45),
         * nome_motorista varchar(100), marca_carro varchar(75), cor_carro
         * varchar(45), n_doc_motorista varchar(75)
         */
        System.out.println( "(DAO )Total Geral (Ilíquido): " + venda.getTotalGeral() );
        System.out.println( "(DAO) Total Geral (Líquido ) " + venda.getTotalVenda() );
//        String inserirVendaQuery = String.format( "select VENDA_CRIAR ( "
//                + "%d, '%s', %s , '%s', "    //1     // %d int e boolean, '%s' date, %s double, '%s' varchar 
//                + "'%s', %s, %s, '%s',"      //2
//                + " '%s', '%s',  %s, %s ,"   //3
//                + " %s, '%s',  '%s', '%s',"  //4
//                + " '%s', '%s',  '%s', %d,"  //5
//                + " %s, %s,  %s, '%s',"      //6
//                //+ " '%s', %d,  %d, %d,"    //7 ultimo era '%d'
//                + " '%s', %d,  '%d', %d,"    //7 ultimo era '%d'
//                + " %d,  %d, %d,  %d,"       //8   o ultimo era %d
////                + " '%s',  '%s', %s,  '%s', "//9
//                + " '%s',  %s, '%s',  '%s', "  //9
//                + "'%s', '%s', '%s', '%s', " //10
//                + "'%s', '%s', '%s', '%s', " //11
//                + "'%s', '%s', '%s', %s  ) " //12
////                + "'%s', '%s', %s  ) " //12
//                + "as ID",
//SEGUNDO
//                String inserirVendaQuery = String.format( "select VENDA_CRIAR ( "
//                + "%d, '%s', %s, '%s', "
//                + "'%s', %s, %s, '%s',"
//                + " '%s', '%s',  %s, %s,"
//                + " %s, '%s',  '%s', '%s',"
//                + " '%s', '%s',  '%s', %d,"
//                + " %s, %s,  %s, '%s',"
//                + " '%s',  %d, '%d',"
//                + " %d,  %d, %d,  %d,"
//                + " '%s',  '%s', %s,  '%s', "
//                + "'%s', '%s', '%s', '%s', "
//                + "'%s', '%s', '%s', '%s', "
//                + "'%s', '%s', '%s', %s  ) "
//                + "as ID",

//                String inserirVendaQuery = String.format( "select VENDA_CRIAR ( "
//                + "%d, '%s', %s , '%s', "
//                + "'%s', %s, %s, '%s',"
//                + " '%s', '%s',  %s, %s ,"
//                + " %s, '%s',  '%s', '%s',"
//                + " '%s', '%s',  '%s', %d,"
//                + " %s, %s,  %s, '%s',"
//                + " '%s', %d,  %d, '%d',"
//                + " %d,  %d, %d,  %d,"
//                + " '%s',  '%s', %s,  '%s', "
//                + "'%s', '%s', '%s', '%s', "
//                + "'%s', '%s', '%s', '%s', "
//                + "'%s', '%s', '%s', %s  ) "
//                + "as ID",
//                0,  //1
//                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVenda() ),
//                venda.getTotalVenda(),
//                venda.getPerformance(),
//                
//                venda.getCredito(),  //2
//                venda.getValorEntregue(),
//                venda.getTroco(),
//                new SimpleDateFormat( "HH:mm:ss" ).format( venda.getHora() ),
//                
//                venda.getNomeCliente(), //3     // %d int e boolean, '%s' varchar e date, %s double, 
//                venda.getStatusEliminado(),
//                venda.getDescontoTotal(),
//                venda.getTotalIva(),
//                
//                venda.getTotalGeral(), //4             
//                venda.getCodFact(),
//                venda.getAssinatura(),
//                venda.getHashCod(),
//                
//                venda.getObs(), //5 
//                venda.getRefCodFact(),
//                venda.getTotalPorExtenso(),
//                venda.getStatusRecibo(),
//                
//                venda.getDescontoComercial(), //6              
//                venda.getDescontoFinanceiro(),
//                venda.getTotalIncidencia(),
//                venda.getLocalCarga(),
//                
//                venda.getLocalDescarga(), //7
//                venda.getCodigoUsuario().getCodigo(), 
//                venda.getCodigoCliente().getCodigo(),
//                venda.getIdArmazemFK().getCodigo(),
//                
//                venda.getFkDocumento().getPkDocumento(), //8 // %d int e boolean, '%s' varchar e date, %s double,
//                venda.getFkAnoEconomico().getPkAnoEconomico(),
//                venda.getFkCambio().getPkCambio(),
//                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVencimento() ),
//                
//                venda.getClienteNif(),  //9   " '%s',  '%s', %s,  '%s', "//9  o correcto seria  " '%s',  %s, '%s',  '%s', "//9
//                venda.getTotalIncidenciaIsento(),
//                ( venda.getRefDataFact() != null ? new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getRefDataFact() ) : new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) ),
//                venda.getNomeConsumidorFinal(),
//                
//                venda.getReferencia(), //10  + "'%s', '%s', '%s', '%s', " //10
//                venda.getMatricula(),
//                venda.getModelo(),
//                venda.getNumChassi(),
//                
//                venda.getNumMotor(), //11  + "'%s', '%s', '%s', '%s', " //11
//                venda.getKilometro(),
//                venda.getNomeMotorista(),
//                venda.getMarcaCarro(),
//                
//                venda.getCorCarro(), //12
//                venda.getNDocMotorista(),
//                venda.getTotalRetencao()
//        );
        String inserirVendaQuery = String.format( "select VENDA_CRIAR ( "
                + "%d, '%s', %s , '%s', "
                + "'%s', %s, %s, '%s',"
                + " '%s', '%s',  %s, %s ,"
                + " %s, '%s',  '%s', '%s',"
                + " '%s', '%s',  '%s', %d,"
                + " %s, %s,  %s, '%s',"
                + " '%s', %d, '%d',"
                + " %d,  %d, %d,  %d,"
                + " '%s',  '%s', %s,  '%s', "
                + "'%s', '%s', '%s', '%s', "
                + "'%s', '%s', '%s', '%s', "
                + "'%s', '%s', '%s', %s, %s ) "
                + "as ID",
                0,
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVenda() ),
                venda.getTotalVenda(),
                venda.getPerformance(),
                venda.getCredito(),
                venda.getValorEntregue(),
                venda.getTroco(),
                new SimpleDateFormat( "HH:mm:ss" ).format( venda.getHora() ),
                venda.getNomeCliente(),
                venda.getStatusEliminado(),
                venda.getDescontoTotal(),
                venda.getTotalIva(),
                venda.getTotalGeral(),
                venda.getCodFact(),
                venda.getAssinatura(),
                venda.getHashCod(),
                venda.getObs(),
                venda.getRefCodFact(),
                venda.getTotalPorExtenso(),
                venda.getStatusRecibo(),
                venda.getDescontoComercial(),
                venda.getDescontoFinanceiro(),
                venda.getTotalIncidencia(),
                venda.getLocalCarga(),
                venda.getLocalDescarga(),
                //                venda.getIdBanco().getIdBanco(),
                venda.getCodigoUsuario().getCodigo(),
                venda.getCodigoCliente().getCodigo(),
                venda.getIdArmazemFK().getCodigo(),
                venda.getFkDocumento().getPkDocumento(),
                venda.getFkAnoEconomico().getPkAnoEconomico(),
                venda.getFkCambio().getPkCambio(),
                new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getDataVencimento() ),
                venda.getClienteNif(),
                venda.getTotalIncidenciaIsento(),
                ( venda.getRefDataFact() != null ? new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( venda.getRefDataFact() ) : new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() ) ),
                venda.getNomeConsumidorFinal(),
                venda.getReferencia(),
                venda.getMatricula(),
                venda.getModelo(),
                venda.getNumChassi(),
                venda.getNumMotor(),
                venda.getKilometro(),
                venda.getNomeMotorista(),
                venda.getMarcaCarro(),
                venda.getCorCarro(),
                venda.getNDocMotorista(),
                venda.getTotalRetencao(),
                venda.getGorjeta()
        );
        System.out.println( inserirVendaQuery );
        ResultSet resultSet = conexao.executeQuery( inserirVendaQuery );

        try
        {
            if ( resultSet.next() )
            {

                int ID = resultSet.getInt( "ID" );
                //registra o movimento no extrato do cliente
                if ( venda.getFkDocumento().getPkDocumento().equals( DVML.DOC_FACTURA_FT ) || venda.getFkDocumento().getPkDocumento().equals( DVML.DOC_RECIBO_RC ) )
                {
                    System.err.println( "EXTRATO DE CONTA DO CLIENTE" );
                    ExtratoContaClienteController.registro_movimento_conta_cliente( venda, conexao );

                }
                return ID;
            }
        }
        catch ( SQLException ex )
        {

            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;

    }

//    public static List<TbVenda> findFaturasCliente( String clienteSlecionado, Integer... tiposDoc )
//    {
//        System.err.println( "clienteSlecionado: " + clienteSlecionado );
//        System.err.println( "tiposDoc: " + tiposDoc[ 0 ] );
//        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a FROM TbVenda  a WHERE a.codigoCliente.nome = :NOME_CLIENTE AND a.fkDocumento.pkDocumento IN :LISTA_DE_DOCS" );
//        query.setParameter( "NOME_CLIENTE", clienteSlecionado );
//        query.setParameter( "LISTA_DE_DOCS", Arrays.asList( tiposDoc ) );
//        List<TbVenda> result = query.getResultList();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//
//        return null;
//    }
    public static List<TbVenda> findFaturasCliente( String clienteSlecionado, Integer... tiposDoc )
    {
        System.err.println( "clienteSlecionado: " + clienteSlecionado );
        System.err.println( "tiposDoc: " + tiposDoc[ 0 ] );
//        Query query = UtilDao.getEntityManager1().createQuery("SELECT DISTINCT a FROM TbVenda  a WHERE a.codigoCliente.nome = :NOME_CLIENTE AND a.fkDocumento.pkDocumento IN :LISTA_DE_DOCS");
//        query.setParameter("NOME_CLIENTE", clienteSlecionado);
//        query.setParameter("LISTA_DE_DOCS", Arrays.asList(tiposDoc));
//        List<TbVenda> result = query.getResultList();
//
//        if (!result.isEmpty()) {
//            return result;
//        }

        String queryString = "select "
                + "	v.* "
                + " from tb_venda v, tb_cliente c, documento d "
                + " where c.nome = '" + clienteSlecionado + "' "
                + " and v.codigo_cliente = c.codigo "
                + " and v.fk_documento = d.pk_documento "
                + " and ( getValorPendente(v.codigo) >  0  or getValorPendente(v.codigo) = -1 )"
                + " and d.pk_documento = " + DVML.DOC_FACTURA_FT + " AND v.status_eliminado = 'false'";
        EntityManager em = UtilDao.getEntityManager1();

        System.out.println( queryString );
        Query query = em.createNativeQuery( queryString );

        List<Object[]> result = query.getResultList();
        List<TbVenda> facturas = new ArrayList<>();
        for ( int i = 0; i < result.size(); i++ )
        {
            Object[] get = result.get( i );
            System.out.println( "OBJECT = " + get[ 13 ] );
            TbVenda venda = new TbVenda();
            venda.setCodFact( get[ 13 ].toString() );
            facturas.add( venda );

        }

        return facturas;
    }

    public static TbVenda findByRefCod( String refCod )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT a FROM TbVenda  a WHERE a.codFact  = :REF_COD " );
        query.setParameter( "REF_COD", refCod );
        List<TbVenda> result = query.getResultList();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return null;
    }

    public List<TbCliente> getAllClienteDevedoresFactura()
    {

        EntityManager em = getEntityManager();
        //Query query = em.createQuery( "SELECT v.codigoCliente FROM TbVenda  v WHERE  v.statusEliminado = 'false'  AND  v.credito = 'true' GROUP BY v.codigoCliente " );
        Query query = em.createQuery( "SELECT v.codigoCliente FROM TbVenda  v WHERE  v.statusEliminado = 'false'  AND  v.fkDocumento.pkDocumento = 2 GROUP BY v.codigoCliente " );

        List<TbCliente> result = query.getResultList();
        em.close();

        return result;
    }

    public boolean existeItensVenda( Integer last_venda, BDConexao conexao ) throws SQLException
    {
        String query = "SELECT * FROM tb_item_venda WHERE codigo_venda = " + last_venda;
        ResultSet resultSet = conexao.executeQuery( query );

        return resultSet.next();

    }

    public double getAllEncomendaByCliente( int pk_cliente )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM TbVenda  v WHERE   v.statusEliminado = 'false' AND v.credito = 'true'   AND v.codigoCliente.codigo = :pk_cliente" )
                .setParameter( "pk_cliente", pk_cliente );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;
    }

    public Double getValorRealDiario( int idBanco, Date data_1, Date data_2, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + idBanco );
        String query = "SELECT SUM(total_venda) AS TOTAL  FROM tb_venda WHERE  idBanco = " + idBanco + " AND dataVenda  BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "' AND '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Double getValorRealDiario( BDConexao conexao )
    {
        String query = "SELECT SUM(total_venda) AS TOTAL  FROM tb_venda WHERE DATE(dataVenda) = '"
                + MetodosUtil.getDataBanco( new Date() ) + "'";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Double getValorRealDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT SUM(total_venda) AS TOTAL  FROM tb_venda WHERE dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Double total_real = resultSet.getDouble( "TOTAL" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0d;

    }

    public Integer getNumeroVendasDiario( BDConexao conexao )
    {
        String query = "SELECT COUNT(codigo) AS NUMERO_VENDAS  FROM tb_venda WHERE DATE(dataVenda) = '"
                + MetodosUtil.getDataBanco( new Date() ) + "'";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer total_real = resultSet.getInt( "NUMERO_VENDAS" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0;

    }

    public Integer getNumeroVendasDiario( BDConexao conexao, Date data_1, Date data_2 )
    {
        String query = "SELECT COUNT(codigo) AS NUMERO_VENDAS  FROM tb_venda WHERE dataVenda BETWEEN '"
                + MetodosUtil.getDataBancoFull( data_1 ) + "'  AND  '" + MetodosUtil.getDataBancoFull( data_2 ) + "'";

        System.out.println( query );
        ResultSet resultSet = conexao.executeQuery( query );

        try
        {
            if ( resultSet.next() )
            {
                Integer total_real = resultSet.getInt( "NUMERO_VENDAS" );
                return total_real;
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return 0;

    }

    public List<TbVenda> getAllVendaExceptoFacturaProformaAndReciboAlterado( Date data_inicio, Date data_fim )
    {
        ArrayList<Integer> listaDocAExcluir = new ArrayList<>();

        listaDocAExcluir.add( DVML.DOC_FACTURA_PROFORMA_PP );
        listaDocAExcluir.add( DVML.DOC_RECIBO_RC );

        EntityManager em = getEntityManager();

//        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE DATE(v.dataVenda) BETWEEN :data_inicio AND :data_fim   AND v.fkDocumento.pkDocumento IN(1,2)" )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//                .setParameter( "DOC_A_EXCLUIR", listaDocAExcluir );
        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND fk_documento IN(1,2)";
//        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND fk_documento IN(6)";

        System.out.println( sql );
        Query query = em.createNativeQuery( sql, TbVenda.class );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public static int getLastVendaByTipoDocumento( int pk_documento, BDConexao conexao )
    {

        String sql = "SELECT MAX(codigo) AS MAXIMO FROM tb_venda WHERE fk_documento = " + pk_documento;

        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getInt( "MAXIMO" );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return 0;

    }

    public String getHashAnterior( int codigo_venda, String cod_fact, int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
//        String cod_fact_anteiror = getCodFactAnteiror( cod_fact, pk_documento, pk_ano_economico, conexao );
        String cod_fact_anteiror = getCodFactAnteirorRectificado( codigo_venda, pk_documento, pk_ano_economico, conexao );
        System.out.println( "COD FACT ANTERIOR: " + cod_fact_anteiror );
        try
        {
            return findByCodFact( cod_fact_anteiror ).getHashCod();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return "";
        }
    }

    public String getHashAnterior2( int codigo_venda, String cod_fact, int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
//        String cod_fact_anteiror = getCodFactAnteiror( cod_fact, pk_documento, pk_ano_economico, conexao );
        String cod_fact_anteiror = getCodFactAnteirorRectificado( codigo_venda, pk_documento, pk_ano_economico, conexao );
        System.out.println( "COD FACT ANTERIOR: " + cod_fact_anteiror );
        try
        {
            return findByCodFact2( cod_fact_anteiror ).getHashCod();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCodFactAnteirorRectificado( int codigo, int pk_documento, int pk_ano_economico, BDConexao conexao )
    {

        String sql = "SELECT cod_fact FROM tb_venda WHERE fk_documento = " + pk_documento + " AND  fk_ano_economico = " + pk_ano_economico + "   AND codigo <  " + codigo + " ORDER BY codigo DESC LIMIT 1";

        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getString( "cod_fact" );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return "";

    }
//            if ( !result.isEmpty() )
//        {
//            return result.get( 0 );
//        }
//        return 0;

    public static BigDecimal getTotalPagoByCodFact( String cod_fact, BDConexao conexao )
    {

//        String sql = "SELECT sum(valor_entregue) as TOTAL_PAGO FROM tb_venda WHERE ref_cod_fact = '" + cod_fact + "' and fk_documento = 6";
        String sql = "SELECT getTotalPago('" + cod_fact + "') AS TOTAL_PAGO";
        ResultSet result = conexao.executeQuery( sql );

        BigDecimal total_pago = new BigDecimal( 0d );
        try
        {
//            if(!result.isEmpty()){
//                return result.get( 0 );
//            }
            if ( result.next() )
            {
                total_pago = result.getBigDecimal( "TOTAL_PAGO" );

            }

//            else{
//                
//                return result.getBigDecimal( 0);
//            }
        }
        catch ( SQLException e )
        {
        }

        return total_pago;
    }

    public static BigDecimal getTotalVendaByCodFact( String cod_fact, BDConexao conexao )
    {

        String sql = "SELECT total_venda  FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );
        BigDecimal total_venda = new BigDecimal( 0d );

        try
        {
            if ( result.next() )
            {
                total_venda = result.getBigDecimal( "total_venda" );
            }

        }
        catch ( SQLException e )
        {
        }

        return total_venda;
    }

    public static Double getTotalRetencaoByCodFact( String cod_fact, BDConexao conexao )
    {

        String sql = "SELECT total_retencao  FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );
        Double total_retencao = new Double( 0.0 );

        try
        {
            if ( result.next() )
            {
                total_retencao = result.getDouble( "total_retencao" );
            }

        }
        catch ( SQLException e )
        {
        }

        return total_retencao;
    }

//    public String getCodFactAnteirorRectificado( int codigo, int pk_documento, int pk_ano_economico, BDConexao conexao )
//    {
//
//        String sql = "SELECT cod_fact FROM tb_venda WHERE fk_documento = " + pk_documento + " AND  fk_ano_economico = " + pk_ano_economico + "   AND codigo <  " + codigo + " ORDER BY codigo DESC LIMIT 1";
//
//        System.out.println( sql );
//        ResultSet result = conexao.executeQuery( sql );
//
//        try
//        {
//            if ( result.next() )
//            {
//                return result.getString( "cod_fact" );
//            }
//        }
//        catch ( SQLException ex )
//        {
//            Logger.getLogger( VendaDao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
//        return "";
//
//    }
//    public static BigDecimal getTotalVendaByCodFact( String cod_fact, BDConexao conexao )
//    {
//
//        String sql = "SELECT total_venda  FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
//        System.out.println( sql );
//        ResultSet result = conexao.executeQuery( sql );
//        BigDecimal total_venda = new BigDecimal( 0d );
//
//        try
//        {
//            if ( result.next() )
//            {
//                total_venda = result.getBigDecimal( "total_venda" );
//            }
//
//        }
//        catch ( SQLException e )
//        {
//        }
//
//        return total_venda;
//    }
    public List<TbVenda> getAllVendaByBetweenDataAndArmazemAndDocumentoAndCliente( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento, int pk_cliente )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? "
                + "AND codigo_cliente = ? ",
                TbVenda.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );
        query.setParameter( 5, pk_cliente );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getAllNotasByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = '" + DVML.ESTADO_DOCUMENTO_ANULADO + "' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public static TbVenda findByCodFact2( String codFact )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT " );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactReemprensaoGeral( String codFact )
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :COD_FACT AND v.statusEliminado = 'false'");
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public TbVenda findByCodFactReemprensaoNC( String codFact )
    {
        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT v  FROM TbVenda  v WHERE V.codFact = :COD_FACT AND V.statusEliminado = 'false' ");
        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :codFact AND v.statusEliminado = 'ANULADO' " );
        query.setParameter( "codFact", codFact );

        List<TbVenda> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public List<TbVenda> getAllVendasGeraisByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem )
    {
        ArrayList<Integer> listaDocAExcluir = new ArrayList<>();

        listaDocAExcluir.add( DVML.DOC_FACTURA_PROFORMA_PP );
        listaDocAExcluir.add( DVML.DOC_FACTURA_CONSULTA_MESA );

        EntityManager em = getEntityManager();

//        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE DATE(v.dataVenda) BETWEEN :data_inicio AND :data_fim   AND v.fkDocumento.pkDocumento IN(1,2)" )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//                .setParameter( "DOC_A_EXCLUIR", listaDocAExcluir );
        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + " AND pkArmazem = " + pk_armazem + "' AND fk_documento IN(1,2,5,6)";

        System.out.println( sql );
        Query query = em.createNativeQuery( sql, TbVenda.class );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public List<TbVenda> getAllVendasGeraisByBetweenDataAndArmazemAndDocumentoAndCliente( Date data_inicio, Date data_fim, int pk_armazem, int pk_cliente )
    {
        ArrayList<Integer> listaDocAExcluir = new ArrayList<>();

        listaDocAExcluir.add( DVML.DOC_FACTURA_PROFORMA_PP );
        listaDocAExcluir.add( DVML.DOC_FACTURA_CONSULTA_MESA );

        EntityManager em = getEntityManager();

//        Query query = em.createQuery( "SELECT v FROM TbVenda  v WHERE DATE(v.dataVenda) BETWEEN :data_inicio AND :data_fim   AND v.fkDocumento.pkDocumento IN(1,2)" )
//                .setParameter( "data_inicio", data_inicio )
//                .setParameter( "data_fim", data_fim );
//                .setParameter( "DOC_A_EXCLUIR", listaDocAExcluir );
//        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco(data_inicio) + "' AND '" + MetodosUtil.getDataBanco(data_fim) + "' AND fk_documento IN(1,2,5,6)";
//                String query = "SELECT MAX(codigo) AS last_id FROM tb_venda WHERE fk_documento = " + pk_documento + " AND fk_ano_economico = " + pk_ano_economico;
        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + " AND pkArmazem = " + pk_armazem + " AND pkCliente = " + pk_cliente + "' AND fk_documento IN(1,2,5,6)";

        System.out.println( sql );
        Query query = em.createNativeQuery( sql, TbVenda.class );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public List<TbVenda> getAllVendaForUpdateHash( Date data_inicio, Date data_fim )
    {
        EntityManager em = getEntityManager();
        String sql = "SELECT * FROM tb_venda where DATE(dataVenda) between '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND '" + MetodosUtil.getDataBanco( data_fim ) + "' AND fk_documento IN(1, 2, 3, 4, 5, 13)";

        System.out.println( sql );
        Query query = em.createNativeQuery( sql, TbVenda.class );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return null;
    }

    public List<TbVenda> getAllVendaAreaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                //                + "AND area_venda = '" + area + "' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", TbVenda.class );

        query.setParameter( 1, MetodosUtil.getDataBanco( data_inicio ) );
        query.setParameter( 2, MetodosUtil.getDataBanco( data_fim ) );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<TbVenda> getVendaByCodFact( String CodFact )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE cod_fact = ? ", TbVenda.class );

        query.setParameter( 1, CodFact );

        List<TbVenda> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

}
