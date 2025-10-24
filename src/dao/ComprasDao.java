/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.ComprasJpaController;
import controlador.ComprasJpaController;
import entity.TbCliente;
import entity.TbProduto;
import entity.Compras;
import entity.ItemCompras;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class ComprasDao extends ComprasJpaController
{

    public ComprasDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<Compras> getProdutosPorComprar()
    {

        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT DISTINCT s.codProdutoCodigo FROM TbStock  s WHERE s.codArmazem.codigo = :codigo_armazem")  
//        Query query = em.createQuery("SELECT s.codFact FROM Compras s");  
        Query query = em.createQuery( "SELECT t FROM Compras t WHERE t.autorizado <> 'true' ORDER BY t.pkCompra ASC" );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<String> buscaTodasFactExceptoAlgumas()
    {
        EntityManager em = getEntityManager();
//            Query query = em.createQuery("SELECT s.designacao FROM Documento s  where s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7");
//            Query query = em.createQuery ( "SELECT v.codFact  FROM Compras  v WHERE V.fkDocumento.pkDocumento < "+DVML.DOC_FACTURA_PROFORMA_PP);
        Query query = em.createQuery( "SELECT v.codFact  FROM Compras  v WHERE V.fkDocumento.pkDocumento !=1 and V.fkDocumento.pkDocumento !=2 V.fkDocumento.pkDocumento !=3 and V.fkDocumento.pkDocumento !=4 and V.fkDocumento.pkDocumento !=5 and V.fkDocumento.pkDocumento !=6 and V.fkDocumento.pkDocumento !=7" );
        List<String> lista = query.getResultList();

        lista.add( 0, "--Seleccione--" );
        return lista;
    }

    public List<String> buscaTodasCompras()
    {
        EntityManager em = getEntityManager();
//            Query query = em.createQuery("SELECT s.designacao FROM Documento s  where s.pkDocumento != 4 and  s.pkDocumento != 5 and s.pkDocumento != 6 and s.pkDocumento != 7");
//            Query query = em.createQuery ( "SELECT v.codFact  FROM Compras  v WHERE V.fkDocumento.pkDocumento < "+DVML.DOC_FACTURA_PROFORMA_PP);
        Query query = em.createQuery( "SELECT v.codFact  FROM Compras  v WHERE V.fkDocumento.pkDocumento !=3 and V.fkDocumento.pkDocumento !=4 and V.fkDocumento.pkDocumento !=5 and V.fkDocumento.pkDocumento !=6 and V.fkDocumento.pkDocumento !=7" );
        List<String> lista = query.getResultList();

        return lista;
    }

    public void alterar_status_venda( int cod_venda, String status )
    {
        try
        {
            Compras venda = findCompras( cod_venda );
//            venda.setStatusEliminado(status);
            edit( venda );
        }
        catch ( Exception e )
        {
            System.out.println( "Falha ao alterar o status da venda" );
        }

    }

    public List<Compras> getFacturasEliminadas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.statusEliminado = 'true' ORDER BY v.codigo ASC" );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public Compras getFacturasEliminadaByCodVenda( int cod_venda )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.statusEliminado = 'true' AND v.codigo = :cod_venda" )
                .setParameter( "cod_venda", cod_venda );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return null;
    }

    public List<Compras> getAllVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
                .setParameter( "id_usuario", id_usuario )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public List<Compras> getAllVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public List<Compras> getAllVenda( Date data_inicio, Date data_fim, int idDocumento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "idDocumento", idDocumento );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public List<TbCliente> getAllClienteVenda( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
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
        Query query = em.createQuery( "SELECT DISTINCT v.codigoCliente FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.fkDocumento.pkDocumento = :idDocumento" )
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

    public List<Compras> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false' AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public List<Compras> getAllVendaByBetweenDataAndArmazemAndDocumento( Date data_inicio, Date data_fim, int pk_armazem, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM tb_venda "
                + "WHERE  DATE(dataVenda) BETWEEN ? AND ? "
                + "AND status_eliminado = 'false' "
                + "AND credito = 'false' "
                + "AND idArmazemFK = ? "
                + "AND fk_documento = ? ", Compras.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_armazem );
        query.setParameter( 4, pk_documento );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return result;
    }

    public List<Compras> getAllVendaByBetweenDataAndArmazem( Date data_inicio, Date data_fim, int pk_armazem, int pk_usuario, int pk_documento )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.credito = 'false' AND v.idArmazemFK.codigo = :pk_armazem AND v.codigoUsuario.codigo = :pk_usuario AND v.fkDocumento.pkDocumento = :pk_documento" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_usuario", pk_usuario )
                .setParameter( "pk_documento", pk_documento );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public List<Compras> getAllVendaByBetweenDataAndArmazemInverso( Date data_inicio, Date data_fim, int pk_armazem )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'   AND v.idArmazemFK.codigo = :pk_armazem ORDER BY v.codigo DESC" )
                .setParameter( "pk_armazem", pk_armazem )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public double getTotalVendaByUsuario( Integer id_usuario, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  SUM(v.totalVenda) AS total FROM Compras  v WHERE v.codigoUsuario.codigo = :id_usuario AND v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'" )
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

    public int getLastCompra()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkCompra)  FROM Compras  v" );

        List<Integer> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }

        return 0;

    }

    public Compras findLastRow()
    {
        return findCompras( getLastCompra() );

//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT  v  FROM Compras  v ORDER BY v.pkCompra DESC " );
//
//        List<Compras> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result.get( 0 );
//        }
//
//        return null;
    }

    public int getLastSolicitacao( int pk_documento )
    {
//
        System.out.println( "ID DOCUMENTO: " + pk_documento );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT  MAX(v.pkCompra)  FROM Compras  v WHERE v.fkDocumento.pkDocumento = :pk_documento" )
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
            }

        }

        return 0;

    }

    public List<Compras> getBuscaTodasVendasEntreDatas_e_Banco( int idBanco, Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.idBanco.idBanco = :idBanco" )
                .setParameter( "idBanco", idBanco )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras venda = new Compras( 0 );
        result.add( venda );

        return result;
    }

    public double getAllEncomenda()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM Compras  v WHERE  v.statusEliminado = 'false' AND v.credito = 'true'  " );

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
        Query query = em.createQuery( "SELECT SUM(V.totalGeral) AS TOTAL  FROM Compras  v WHERE   v.statusEliminado = 'false' AND v.credito = 'true'   AND v.codigoCliente.codigo = :pk_cliente" )
                .setParameter( "pk_cliente", pk_cliente );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;
    }

    public Object[] listarVendaDoDocumento()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM Compras  v WHERE V.codFact LIKE '%FR%' ORDER BY v.codFact DESC" );
        ;

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return ( Object[] ) documentos.toArray();
        }

        return null;
    }

    public Object[] listarVendaDoDocumentoValidas()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM Compras  v WHERE V.codFact LIKE '%FR%'  OR V.codFact LIKE '%FT%'   AND v.codFact NOT IN :FATURAS_INVALIDAS ORDER BY v.codFact DESC" );

        Query queryFaturasInvalidas = em.createQuery( "SELECT DISTINCT v.refCodFact  FROM Notas  v WHERE v.estado = :TOTALMENTE_RETIFICADA OR v.estado = :ANULADA" );

        queryFaturasInvalidas.setParameter( "TOTALMENTE_RETIFICADA", TOTALMENTE_RETIFICADO.toString() );
        queryFaturasInvalidas.setParameter( "ANULADA", ANULADO.toString() );

        List<String> resultList = queryFaturasInvalidas.getResultList();

        if ( resultList.isEmpty() )
        {
            resultList.add( "0" );
        }

        query.setParameter( "FATURAS_INVALIDAS", resultList );

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return ( Object[] ) documentos.toArray();
        }

        return null;
    }

    public Compras findByCodFact( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public Compras findByCodFact( String codFact, int tipo_documento )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT AND v.fkDocumento.pkDocumento = :TIPO_DOC" );
        query.setParameter( "COD_FACT", codFact );
        query.setParameter( "TIPO_DOC", tipo_documento );

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public Compras findByCodFactura( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT" );
        query.setParameter( "COD_FACT", codFact );

        List<Compras> documentos = query.getResultList();
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
        Query query = em.createQuery( "SELECT  DISTINCT v.codigoProduto FROM TbItemVenda  v WHERE v.codigoVenda.dataVenda BETWEEN :data_inicio AND :data_fim AND v.codigoVenda.statusEliminado = 'false' " )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );
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

    public List<Compras> getAllVendaExceptoFacturaProformaAndRecibo( Date data_inicio, Date data_fim )
    {
        ArrayList<Integer> listaDocAExcluir = new ArrayList<>();

        listaDocAExcluir.add( DVML.DOC_FACTURA_PROFORMA_PP );
        listaDocAExcluir.add( DVML.DOC_RECIBO_RC );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento NOT IN :DOC_A_EXCLUIR" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "DOC_A_EXCLUIR", listaDocAExcluir );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return result;
    }

    public List<Compras> getAllRecibo( Date data_inicio, Date data_fim )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento =  :DOC_RECIBO" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "DOC_RECIBO", DVML.DOC_RECIBO_RC );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        return result;
    }

//
//    public List<Compras> getAllVendaExceptoFacturaProformaAndRecibo(Date data_inicio, Date data_fim) {
//
//        int factura_proforma = DVML.DOC_FACTURA_PROFORMA_PP;
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento <> :pk_docummento")
//                .setParameter("data_inicio", data_inicio)
//                .setParameter("data_fim", data_fim)
//                .setParameter("pk_docummento", factura_proforma);
//
//        List<Compras> result = query.getResultList();
//        em.close();
//
//        if (!result.isEmpty()) {
//            return result;
//        }
//
//        return result;
//    }
    public List<Compras> getAllFacturaProforma( Date data_inicio, Date data_fim )
    {

        int factura_proforma = DVML.DOC_FACTURA_PROFORMA_PP;
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.dataVenda BETWEEN :data_inicio AND :data_fim AND v.statusEliminado = 'false'  AND v.fkDocumento.pkDocumento = :pk_docummento" )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim )
                .setParameter( "pk_docummento", factura_proforma );

        List<Compras> result = query.getResultList();
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
        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM Compras v WHERE v.codFact NOT IN :IMPRESSORAS_INALTERAVEIS  " );

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

        List<Compras> documentos = query.getResultList();
        System.err.println( "" );

        if ( !documentos.isEmpty() )
        {
            return ( Object[] ) documentos.toArray();
        }

        return null;
    }

    public boolean existe_recibo( String cod_fact )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v.codigo FROM Compras v WHERE v.statusRecibo = TRUE AND v.codFact = :cod_fact" )
                .setParameter( "cod_fact", cod_fact );
        return !query.getResultList().isEmpty();

    }

    public boolean actualiza_status_recibo( String cod_fact, BDConexao conexao )
    {

        String sql = "UPDATE tb_venda SET status_recibo = true WHERE cod_fact = '" + cod_fact + "'";
        return conexao.executeUpdate( sql );

    }

    public String getCodFactAnteiror( String cod_fact, BDConexao conexao )
    {
        String sql = "SELECT cod_fact FROM kitanda_db.tb_venda WHERE cod_fact <  '" + cod_fact + "' ORDER BY cod_fact DESC LIMIT 1";
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
            Logger.getLogger( ComprasDao.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return "";

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

        Query query = em.createQuery( "SELECT DISTINCT v.codFact  FROM Compras v WHERE (v.codigoCliente.nome != :CONSUMIDOR_FINAL ) AND ( v.codFact NOT IN :IMPRESSORAS_INALTERAVEIS) AND ( v.fkDocumento.pkDocumento IN :DOCUMENTOS_SELECIONADOS )  " );

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

        List<Compras> resultList = query.getResultList();
        System.err.println( "" );

        if ( !resultList.isEmpty() )
        {
            return ( Object[] ) resultList.toArray();
        }

        return null;
    }

    public List<Compras> buscar_dcompras_by_idFornecedor( int codigo )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT i FROM Compras i WHERE i.fkFornecedor.codigo = :codigo" )
                .setParameter( "codigo", codigo );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }

        Compras compras = new Compras( 0 );
        result.add( compras );
        return result;

    }

    public List<Compras> getAllComprasApuradas()
    {

        int doc_nota_encomenda = DVML.DOC_RECIBO_SO;
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.fkDocumento.pkDocumento = :pk_docummento AND v.autorizado = true AND v.encomendado = false" )
                .setParameter( "pk_docummento", doc_nota_encomenda );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<Compras> getAllEncomendas()
    {

        int doc_nota_encomenda = DVML.DOC_NOTA_ENCOMENDA_NE;
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v FROM Compras  v WHERE v.fkDocumento.pkDocumento = :pk_docummento AND v.despachoEncomenda = false" )
                .setParameter( "pk_docummento", doc_nota_encomenda );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

////    public List<Compras> getAllComprasByBetweenDataAndDocumento( Date data_inicio, Date data_fim, int pk_documento )
////    {
////
////        EntityManager em = getEntityManager();
////
////        Query query = em.createNativeQuery( "SELECT * FROM compras "
////                + "WHERE  DATE(data_compra) BETWEEN ? AND ? "
////                + " AND fk_documento = ? ", Compras.class );
////
////        query.setParameter( 1, data_inicio );
////        query.setParameter( 2, data_fim );
////        query.setParameter( 3, pk_documento );
////
////        List<Compras> result = query.getResultList();
////        em.close();
////
////        if ( !result.isEmpty() )
////        {
////            return result;
////        }
////        return null;
////    }
    public Compras getCoprasByCodFact( String codFact )
    {

        System.out.println( "CodFact" + codFact );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT m FROM Compras m WHERE m.codFact = :codFact" )
                .setParameter( "codFact", codFact );

        List<Compras> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return new Compras( 0 );

    }

    public int getComprasByNome( String codFact )
    {

        System.out.println( "NOME ESTUDANTE " + codFact );
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM Compras a WHERE a.codFact = :codFact" )
                .setParameter( "codFact", codFact );

        List<Compras> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getPkCompra();
        }
        return 0;

    }

    public int getIdByDescricao( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT s.pkCompra FROM Compras s WHERE s.codFact = :codFact" )
                .setParameter( "codFact", codFact );

        List<Compras> result = query.getResultList();
        em.close();
        if ( !result.isEmpty() )
        {
            return result.get( 0 ).getPkCompra();
        }
        return 0;

    }

//    public List <Compras> buscaTodosByPedido(boolean autorizado) 
//    {         
//            EntityManager em = getEntityManager();
//            Query query = em.createQuery ("SELECT t FROM Compras t   WHERE t.autorizado = :autorizado  ORDER BY t.pkCompra ASC")
//                    .setParameter("autorizado", autorizado);
//            
//            return query.getResultList();
//            
//    }
    public List<ItemCompras> buscaTodosByPedido( boolean autorizado )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT t FROM ItemCompras t   WHERE t.fkCompra.autorizado = :autorizado" )
                .setParameter( "autorizado", autorizado );

        return query.getResultList();

    }

    public List<Compras> getAllComprasByBetweenDataAndDocumento( String data_inicio, String data_fim, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM compras "
                + " WHERE  DATE(data_compra) BETWEEN ? AND ? "
                + " AND fk_documento = ?  AND status_eliminado = 'false' ", Compras.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_documento );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<Compras> getAllComprasByBetweenDataAndDocumento( String data_inicio, String data_fim, int pk_documento, int pk_fornecedor )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM compras "
                + " WHERE  DATE(data_compra) BETWEEN ? AND ? "
                + " AND fk_documento = ? AND fk_fornecedor = ? AND status_eliminado = 'false'", Compras.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_documento );
        query.setParameter( 4, pk_fornecedor );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<Compras> getAllNotasComprasByBetweenDataAndDocumento( String data_inicio, String data_fim, int pk_documento )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM compras "
                + " WHERE  DATE(data_compra) BETWEEN ? AND ? "
                + " AND fk_documento = ?  AND status_eliminado = 'true' ", Compras.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_documento );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public List<Compras> getAllNotasComprasByBetweenDataAndDocumento( String data_inicio, String data_fim, int pk_documento, int pk_fornecedor )
    {

        EntityManager em = getEntityManager();

        Query query = em.createNativeQuery( "SELECT * FROM compras "
                + " WHERE  DATE(data_compra) BETWEEN ? AND ? "
                + " AND fk_documento = ? AND fk_fornecedor = ? AND status_eliminado = 'true'", Compras.class );

        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, pk_documento );
        query.setParameter( 4, pk_fornecedor );

        List<Compras> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;
    }

    public boolean existe_codFact( String cod_fact )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT v.pkCompra FROM Compras v WHERE v.statusRecibo = TRUE  AND v.statusEliminado = 'false' AND v.codFact = :cod_fact" )
                .setParameter( "cod_fact", cod_fact );
        return !query.getResultList().isEmpty();

    }

    public static Compras findByCodFacts( String codFact )
    {
        Query query = UtilDao.getEntityManager1().createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT " );
        query.setParameter( "COD_FACT", codFact );

        List<Compras> documentos = query.getResultList();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

    public Compras findByCodFactReemprensao( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT AND V.statusEliminado = 'false' " );
        query.setParameter( "COD_FACT", codFact );

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }
    
    public Compras findByCodFactReemprensaoCompras( String codFact )
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT DISTINCT v  FROM Compras  v WHERE V.codFact = :COD_FACT " );
        query.setParameter( "COD_FACT", codFact );

        List<Compras> documentos = query.getResultList();
        em.close();

        if ( !documentos.isEmpty() )
        {
            return documentos.get( 0 );
        }

        return null;
    }

//    public Compras findByCodFactReemprensaoGeral( String codFact )
//    {
//        EntityManager em = getEntityManager();
////        Query query = em.createQuery("SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :COD_FACT AND v.statusEliminado = 'false'");
//        Query query = em.createQuery( "SELECT DISTINCT v  FROM TbVenda  v WHERE v.codFact = :COD_FACT" );
//        query.setParameter( "COD_FACT", codFact );
//
//        List<TbVenda> documentos = query.getResultList();
//        em.close();
//
//        if ( !documentos.isEmpty() )
//        {
//            return documentos.get( 0 );
//        }
//
//        return null;
//    }

}
