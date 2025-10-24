/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.Grupo;
import entity.Modelo;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.Compras;
import entity.ItemCompras;
import entity.TbTipoProduto;
import entity.TbVenda;
import entity.TbUsuario;
import entity.Unidade;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class ComprasController implements EntidadeFactory
{

    private BDConexao conexao;

    public ComprasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
//    public boolean salvar( Object object )
//    {
//
//        Compras compras = ( Compras ) object;
//        String INSERT = "INSERT INTO compras( data_compra, total_compra, nome_fornecedor, hash_cod, cod_fact, ref_cod_fact, total_iva, assinatura, total_por_extenso, codigo_usuario, "
//                + " desconto_comercial , desconto_financeiro , desconto_total , total_incidencia , obs  , total_geral , valor_entregue , troco , total_incidencia_isento , data_limite_levantamento ,"
//                + " fk_documento , fk_fornecedor , idArmazemFK , fornecedor_nif , fk_ano_economico  , autorizado , despacho_encomenda , encomendado , status_eliminado , status_recibo "
//                + ")"
//                + " VALUES("
//                + "'" + MetodosUtil.getDataBancoFull( compras.getDataCompra() ) + "' , "
//                + compras.getTotalCompra() + " , "
//                + "'" + compras.getNomeFornecedor() + "' , "
//                + "'" + compras.getHashCod() + "' , "
//                + "'" + compras.getCodFact() + "' , "
//                + "'" + compras.getRefCodFact() + "' , "
//                + compras.getTotalIva() + " , "
//                + "'" + compras.getAssinatura() + "' , "
//                + "'" + compras.getTotalPorExtenso() + "' , "
//                + compras.getCodigoUsuario().getCodigo() + ", "
//                + compras.getDescontoComercial() + " , "
//                + compras.getDescontoFinanceiro() + " , "
//                + compras.getDescontoTotal() + " , "
//                + compras.getTotalIncidencia() + " , "
//                + compras.getObs() + " , "
//                + compras.getTotalGeral() + " , "
//                + compras.getValorEntregue() + " , "
//                + compras.getTroco() + " , "
//                + compras.getTotalIncidenciaIsento() + " , "
//                + "'" + MetodosUtil.getDataBanco( compras.getDataLimiteLevantamento() ) + "' , "
//                + compras.getFkDocumento().getPkDocumento() + ", "
//                + compras.getFkFornecedor().getCodigo() + ", "
//                + compras.getIdArmazemFK().getCodigo() + ", "
//                + "'" + compras.getFornecedorNif() + "' , "
//                + compras.getFkAnoEconomico().getPkAnoEconomico() + ", "
//                + compras.getAutorizado() + " , "
//                + compras.getDespachoEncomenda() + " , "
//                + compras.getEncomendado() + " , "
//                + "'" + compras.getStatusEliminado() + "' , "
//                + compras.getStatusRecibo()
//                + " ) ";
//        System.out.println( "INSERT :" + INSERT );
//        return conexao.executeUpdate( INSERT );
//
//    }

    public boolean salvar( Object object )
    {

        Compras compras = ( Compras ) object;
        String INSERT = "INSERT INTO compras( data_compra, total_compra, nome_fornecedor, hash_cod, cod_fact, ref_cod_fact, total_iva, assinatura, total_por_extenso, codigo_usuario, "
                + " desconto_comercial , desconto_financeiro , desconto_total , total_incidencia , obs  , total_geral , valor_entregue , troco , total_incidencia_isento , data_limite_levantamento ,"
                + " fk_documento , fk_fornecedor , idArmazemFK , fornecedor_nif , fk_ano_economico  , autorizado , despacho_encomenda , encomendado , status_eliminado , status_recibo "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( compras.getDataCompra() ) + "' , "
                + compras.getTotalCompra() + " , "
                + "'" + compras.getNomeFornecedor() + "' , "
                + "'" + compras.getHashCod() + "' , "
                + "'" + compras.getCodFact() + "' , "
                + "'" + compras.getRefCodFact() + "' , "
                + compras.getTotalIva() + " , "
                + "'" + compras.getAssinatura() + "' , "
                + "'" + compras.getTotalPorExtenso() + "' , "
                + compras.getCodigoUsuario().getCodigo() + ", "
                + compras.getDescontoComercial() + " , "
                + compras.getDescontoFinanceiro() + " , "
                + compras.getDescontoTotal() + " , "
                + compras.getTotalIncidencia() + " , "
                + compras.getObs() + " , "
                + compras.getTotalGeral() + " , "
                + compras.getValorEntregue() + " , "
                + compras.getTroco() + " , "
                + compras.getTotalIncidenciaIsento() + " , "
                + "'" + MetodosUtil.getDataBanco( compras.getDataLimiteLevantamento() ) + "' , "
                + compras.getFkDocumento().getPkDocumento() + ", "
                + compras.getFkFornecedor().getCodigo() + ", "
                + compras.getIdArmazemFK().getCodigo() + ", "
                + "'" + compras.getFornecedorNif() + "' , "
                + compras.getFkAnoEconomico().getPkAnoEconomico() + ", "
                + compras.getAutorizado() + " , "
                + compras.getDespachoEncomenda() + " , "
                + compras.getEncomendado() + " , "
                + "'" + compras.getStatusEliminado() + "' , "
                + compras.getStatusRecibo()
                + " ) ";
        System.out.println( "INSERT :" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    public boolean salvar_encomenda_to_compra( Object object )
    {

        Compras compras = ( Compras ) object;
        String INSERT = "INSERT INTO compras( data_compra, total_compra, nome_fornecedor, hash_cod, cod_fact, ref_cod_fact, total_iva, assinatura, total_por_extenso, codigo_usuario, "
                + " desconto_comercial , desconto_financeiro , desconto_total , total_incidencia , obs  , total_geral , valor_entregue , troco , total_incidencia_isento , data_limite_levantamento ,"
                + " fk_documento , fk_fornecedor , idArmazemFK , fornecedor_nif , fk_ano_economico  , autorizado , despacho_encomenda , encomendado , status_eliminado , status_recibo,"
                + " doc_vosso, doc_vosso_numero, doc_vosso_data, doc_vosso_data_vencimento, valor_pago, valor_por_pagar "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( compras.getDataCompra() ) + "' , "
                + compras.getTotalCompra() + " , "
                + "'" + compras.getNomeFornecedor() + "' , "
                + "'" + compras.getHashCod() + "' , "
                + "'" + compras.getCodFact() + "' , "
                + "'" + compras.getRefCodFact() + "' , "
                + compras.getTotalIva() + " , "
                + "'" + compras.getAssinatura() + "' , "
                + "'" + compras.getTotalPorExtenso() + "' , "
                + compras.getCodigoUsuario().getCodigo() + ", "
                + compras.getDescontoComercial() + " , "
                + compras.getDescontoFinanceiro() + " , "
                + compras.getDescontoTotal() + " , "
                + compras.getTotalIncidencia() + " , "
                + compras.getObs() + " , "
                + compras.getTotalGeral() + " , "
                + compras.getValorEntregue() + " , "
                + compras.getTroco() + " , "
                + compras.getTotalIncidenciaIsento() + " , "
                + "'" + MetodosUtil.getDataBanco( compras.getDataLimiteLevantamento() ) + "' , "
                + compras.getFkDocumento().getPkDocumento() + ", "
                + compras.getFkFornecedor().getCodigo() + ", "
                + compras.getIdArmazemFK().getCodigo() + ", "
                + "'" + compras.getFornecedorNif() + "' , "
                + compras.getFkAnoEconomico().getPkAnoEconomico() + ", "
                + compras.getAutorizado() + " , "
                + compras.getDespachoEncomenda() + " , "
                + compras.getEncomendado() + " , "
                + "'" + compras.getStatusEliminado() + "' , "
                + compras.getStatusRecibo() + ", "
                + "'" + compras.getDocVosso() + "' , "
                + "'" + compras.getDocVossoNumero() + "' , "
                + "'" + MetodosUtil.getDataBanco( compras.getDocVossoData() ) + "' , "
                + "'" + MetodosUtil.getDataBanco( compras.getDocVossoDataVencimento() ) + "',  "
                + compras.getValorPago() + ", "
                + compras.getValorPorPagar()
                + " ) ";
        System.out.println( "INSERT :" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM compras WHERE pk_compra = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

//    public List<Compras> getAllEncomendas()
//    {
//        int doc_nota_encomenda = DVML.DOC_NOTA_ENCOMENDA_NE;
//        String FIND_ALL = "SELECT * FROM Compras WHERE fkDocumento.pkDocumento = " + doc_nota_encomenda + " AND v.despachoEncomenda = false";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//        List<Compras> lista_compra = new ArrayList<>();
//        Compras compras;
//        try
//        {
//            while ( result.next() )
//            {
////                System.out.println( result.getString( "cod_fact" ) );
//                compras = new Compras();
//                lista_compra.add( getComprasResultSet( compras, result ) );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//
//        return lista_compra;
//    }
    @Override
    public List<Compras> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM compras ORDER BY pk_compra ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Compras> lista_compras = new ArrayList<>();
        Compras compras;
        try
        {

            while ( result.next() )
            {
                compras = new Compras();
                compras.setPkCompra( result.getInt( "pk_compra" ) );
                compras.setDataCompra( result.getDate( "data_compra" ) );
                compras.setTotalCompra( result.getDouble( "total_compra" ) );
                compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
                compras.setHashCod( result.getString( "hash_cod" ) );
                compras.setCodFact( result.getString( "cod_fact" ) );
                compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
                compras.setTotalIva( result.getDouble( "total_iva" ) );
                compras.setAssinatura( result.getString( "assinatura" ) );
                compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
                compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
                compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
                compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
                compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
                compras.setObs( result.getString( "obs" ) );
                compras.setTotalGeral( result.getDouble( "total_geral" ) );
                compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
                compras.setTroco( result.getDouble( "troco" ) );
                compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
                compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
                compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
                compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
                compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                compras.setAutorizado( result.getBoolean( "autorizado" ) );
                compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
                compras.setEncomendado( result.getBoolean( "encomendado" ) );
                compras.setStatusEliminado( result.getString( "status_eliminado" ) );
                compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
                compras.setDocVosso( result.getString( "doc_vosso" ) );
                compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
                compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
                compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
                compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
                compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );

                lista_compras.add( compras );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_compras;
    }

    private Compras getComprasResultSet( Compras compras, ResultSet result )
    {
        try
        {
            compras.setPkCompra( result.getInt( "pk_compra" ) );
            compras.setDataCompra( result.getDate( "data_compra" ) );
            compras.setTotalCompra( result.getDouble( "total_compra" ) );
            compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
            compras.setHashCod( result.getString( "hash_cod" ) );
            compras.setCodFact( result.getString( "cod_fact" ) );
            compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
            compras.setTotalIva( result.getDouble( "total_iva" ) );
            compras.setAssinatura( result.getString( "assinatura" ) );
            compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
            compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
            compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
            compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
            compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
            compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
            compras.setObs( result.getString( "obs" ) );
            compras.setTotalGeral( result.getDouble( "total_geral" ) );
            compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
            compras.setTroco( result.getDouble( "troco" ) );
            compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
            compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
            compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
            compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
            compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
            compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
            compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
            compras.setAutorizado( result.getBoolean( "autorizado" ) );
            compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
            compras.setEncomendado( result.getBoolean( "encomendado" ) );
            compras.setStatusEliminado( result.getString( "status_eliminado" ) );
            compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
            compras.setDocVosso( result.getString( "doc_vosso" ) );
            compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
            compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
            compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
            compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
            compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );

        }
        catch ( Exception e )
        {
        }

        return compras;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM compras WHERE pk_compra = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Compras compras = null;
        try
        {

            if ( result.next() )
            {
                compras = new Compras();
                compras.setDataCompra( result.getDate( "data_compra" ) );
                compras.setTotalCompra( result.getDouble( "total_compra" ) );
                compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
                compras.setHashCod( result.getString( "hash_cod" ) );
                compras.setCodFact( result.getString( "cod_fact" ) );
                compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
                compras.setTotalIva( result.getDouble( "total_iva" ) );
                compras.setAssinatura( result.getString( "assinatura" ) );
                compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
                compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
                compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
                compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
                compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
                compras.setObs( result.getString( "obs" ) );
                compras.setTotalGeral( result.getDouble( "total_geral" ) );
                compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
                compras.setTroco( result.getDouble( "troco" ) );
                compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
                compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
                compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
                compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
                compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                compras.setAutorizado( result.getBoolean( "autorizado" ) );
                compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
                compras.setEncomendado( result.getBoolean( "encomendado" ) );
                compras.setStatusEliminado( result.getString( "status_eliminado" ) );
                compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
                compras.setDocVosso( result.getString( "doc_vosso" ) );
                compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
                compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
                compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
                compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
                compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return compras;

    }

    public int getUltimaContagemByIdDocumentoAndAnoEconomico( int pk_documento, int pk_ano_economico, BDConexao conexao )
    {
        try
        {
            Integer codCompra = getLastComprasByDocAndAno( pk_documento, pk_ano_economico, conexao );
            System.out.println( "codCompra: " + codCompra );
            String codFact = MetodosUtil.getCodFactCompra( codCompra, conexao );
            System.out.println( "LastCodFactCompra: " + codFact );

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

    public List<Compras> listComprasByDates( Date data_1, Date data_2 )
    {
        List<Compras> list = new ArrayList<>();

        String query = "SELECT * FROM compras WHERE DATE(data_compra) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";

        ResultSet rs = conexao.executeQuery( query );

        try
        {
            while ( rs.next() )
            {
                Compras c = new Compras();
                setComprasResultSet( c, rs );
                list.add( c );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( ComprasController.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return list;
    }

    private Compras setComprasResultSet( Compras compras, ResultSet result )
    {
        try
        {
            compras.setPkCompra( result.getInt( "pk_compra" ) );
            compras.setDataCompra( result.getDate( "data_compra" ) );
            compras.setTotalCompra( result.getDouble( "total_compra" ) );
            compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
            compras.setHashCod( result.getString( "hash_cod" ) );
            compras.setCodFact( result.getString( "cod_fact" ) );
            compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
            compras.setTotalIva( result.getDouble( "total_iva" ) );
            compras.setAssinatura( result.getString( "assinatura" ) );
            compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
            compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
            compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
            compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
            compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
            compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
            compras.setObs( result.getString( "obs" ) );
            compras.setTotalGeral( result.getDouble( "total_geral" ) );
            compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
            compras.setTroco( result.getDouble( "troco" ) );
            compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
            compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
            compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
            compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
            compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
            compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
            compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
            compras.setAutorizado( result.getBoolean( "autorizado" ) );
            compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
            compras.setEncomendado( result.getBoolean( "encomendado" ) );
            compras.setStatusEliminado( result.getString( "status_eliminado" ) );
            compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
            compras.setDocVosso( result.getString( "doc_vosso" ) );
            compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
            compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
            compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
            compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
            compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );

        }
        catch ( Exception e )
        {
        }

        return compras;
    }

    public Integer getLastComprasByDocAndAno( int pk_documento, int pk_ano_economico, BDConexao conexao )
    {

        System.err.println( "PK_DOCUMENTO: " + pk_documento );
        System.err.println( "PK_ANO_ECONOMICO: " + pk_ano_economico );
        String query = "SELECT MAX(pk_compra) AS last_id FROM compras WHERE fk_documento = " + pk_documento + " AND fk_ano_economico = " + pk_ano_economico;

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

        }

        return 0;

    }

    public int getUltimaContagemByIdDocumentoAndAnoEconomico( int pk_documento, int pk_ano_economico )
    {
        try
        {
            Integer codCompra = getLastCodigoCompra( pk_documento, pk_ano_economico );
            System.out.println( "LastCodCompra: " + codCompra );
            String codFact = MetodosUtil.getCodFactCompra( codCompra, conexao );
            System.out.println( "LastCodFact: " + codFact );

            if ( codFact != null )
            {
                String cod = codFact.split( "/" )[ 1 ];
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

    public Integer getLastCodigoCompra( int pk_documento, int pk_ano_economico )
    {
        System.err.println( "PK_DOCUMENTO: " + pk_documento );
        System.err.println( "PK_ANO_ECONOMICO: " + pk_ano_economico );
        String query = "SELECT MAX(pk_compra) AS last_id FROM compras WHERE fk_documento = " + pk_documento + " AND fk_ano_economico = " + pk_ano_economico;

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
        }

        return 0;

    }

    public Compras getLastCompras()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_compra) as maximo_id, c.*  FROM compras c";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Compras compras = null;
        try
        {

            if ( result.next() )
            {
                compras = new Compras();
                compras.setPkCompra( result.getInt( "maximo_id" ) );
                compras.setDataCompra( result.getDate( "data_compra" ) );
                compras.setTotalCompra( result.getDouble( "total_compra" ) );
                compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
                compras.setHashCod( result.getString( "hash_cod" ) );
                compras.setCodFact( result.getString( "cod_fact" ) );
                compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
                compras.setTotalIva( result.getDouble( "total_iva" ) );
                compras.setAssinatura( result.getString( "assinatura" ) );
                compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
                compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
                compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
                compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
                compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
                compras.setObs( result.getString( "obs" ) );
                compras.setTotalGeral( result.getDouble( "total_geral" ) );
                compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
                compras.setTroco( result.getDouble( "troco" ) );
                compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
                compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
                compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
                compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
                compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                compras.setAutorizado( result.getBoolean( "autorizado" ) );
                compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
                compras.setEncomendado( result.getBoolean( "encomendado" ) );
                compras.setStatusEliminado( result.getString( "status_eliminado" ) );
                compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
                compras.setDocVosso( result.getString( "doc_vosso" ) );
                compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
                compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
                compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
                compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
                compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return compras;

    }

    public Compras findByCodFact( String codigo )
    {

        String FIND_BY_COD_FACT = "SELECT * FROM compras WHERE cod_fact = '" + codigo + "'";
        ResultSet result = conexao.executeQuery( FIND_BY_COD_FACT );
        Compras compras = null;
        try
        {

            if ( result.next() )
            {
                compras = new Compras();
                compras.setDataCompra( result.getDate( "data_compra" ) );
                compras.setTotalCompra( result.getDouble( "total_compra" ) );
                compras.setNomeFornecedor( result.getString( "nome_fornecedor" ) );
                compras.setHashCod( result.getString( "hash_cod" ) );
                compras.setCodFact( result.getString( "cod_fact" ) );
                compras.setRefCodFact( result.getString( "ref_cod_fact" ) );
                compras.setTotalIva( result.getDouble( "total_iva" ) );
                compras.setAssinatura( result.getString( "assinatura" ) );
                compras.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
                compras.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                compras.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
                compras.setDescontoFinanceiro( result.getDouble( "desconto_financeiro" ) );
                compras.setDescontoTotal( result.getDouble( "desconto_total" ) );
                compras.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
                compras.setObs( result.getString( "obs" ) );
                compras.setTotalGeral( result.getDouble( "total_geral" ) );
                compras.setValorEntregue( result.getDouble( "valor_entregue" ) );
                compras.setTroco( result.getDouble( "troco" ) );
                compras.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
                compras.setDataLimiteLevantamento( result.getDate( "data_limite_levantamento" ) );
                compras.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                compras.setFkFornecedor( new TbFornecedor( result.getInt( "fk_fornecedor" ) ) );
                compras.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                compras.setFornecedorNif( result.getString( "fornecedor_nif" ) );
                compras.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                compras.setAutorizado( result.getBoolean( "autorizado" ) );
                compras.setDespachoEncomenda( result.getBoolean( "despacho_encomenda" ) );
                compras.setEncomendado( result.getBoolean( "encomendado" ) );
                compras.setStatusEliminado( result.getString( "status_eliminado" ) );
                compras.setStatusRecibo( result.getBoolean( "status_recibo" ) );
                compras.setDocVosso( result.getString( "doc_vosso" ) );
                compras.setDocVossoNumero( result.getString( "doc_vosso_numero" ) );
                compras.setDocVossoData( result.getDate( "doc_vosso_data" ) );
                compras.setDocVossoDataVencimento( result.getDate( "doc_vosso_data_vencimento" ) );
                compras.setValorPago( result.getBigDecimal( "valor_pago" ) );
                compras.setValorPorPagar( result.getBigDecimal( "valor_por_pagar" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return compras;

    }

    public List<Compras> getAllEncomendas()
    {

        String FIND_ALL = "SELECT c.* FROM compras c INNER JOIN documento d ON c.fk_documento = d.pk_documento AND c.despacho_encomenda = false";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Compras> lista_compras = new ArrayList<>();
        Compras compras;
        try
        {

            while ( result.next() )
            {
                compras = new Compras();
                compras = getComprasResultSet( compras, result );
                lista_compras.add( compras );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_compras;
    }

    public List<Compras> getAllEncomendasByIdFornecedor( int idFornecedor )
    {
        String FIND_ALL = "SELECT * FROM compras  WHERE fk_fornecedor  = " + idFornecedor
                + " AND fk_documento = " + DVML.DOC_NOTA_ENCOMENDA_NE
                + " AND despacho_encomenda = 'false'"
                + " ORDER BY pk_compra ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Compras> lista_encomendas = new ArrayList<>();
        Compras compras;
        try
        {
            while ( result.next() )
            {
                compras = new Compras();
                compras = getComprasResultSet( compras, result );
                lista_encomendas.add( compras );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_encomendas;
    }

    public List<Compras> getAllComrpasByIdFornecedor( int idFornecedor )
    {
        String FIND_ALL = "SELECT * FROM compras  WHERE fk_fornecedor  = " + idFornecedor
                + " AND fk_documento = " + DVML.DOC_COMPRA_CO + " AND valor_por_pagar > 0";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Compras> lista_encomendas = new ArrayList<>();
        Compras compras;
        try
        {
            while ( result.next() )
            {
                compras = new Compras();
                compras = getComprasResultSet( compras, result );
                lista_encomendas.add( compras );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista_encomendas;
    }

    public boolean actualizarEncomenda( String codFact, BDConexao conexao )
    {
        String DELETE = "UPDATE compras SET despacho_encomenda = " + true + "  WHERE cod_fact = '" + codFact + "'";
        return conexao.executeUpdate( DELETE );
    }

    public boolean actualizarValores( String codFact, BigDecimal valor, BigDecimal valorPago, BigDecimal valorPorPagar )
    {
        valorPago = valorPago.add( valor );
        valorPorPagar = valorPorPagar.subtract( valor );
        String sql = "UPDATE compras SET valor_pago = " + valorPago + ",  valor_por_pagar = " + valorPorPagar + " WHERE cod_fact = '" + codFact + "'";

        return conexao.executeUpdate( sql );

    }

    public static boolean actualizar_hash( List<Compras> list, ItemComprasController itemComprasController, BDConexao conexao )
    {

        for ( int i = 0; i < list.size(); i++ )
        {
            Compras compra = list.get( i );

            List<ItemCompras> lista_itens_compra = itemComprasController.listarTodosByIdCompra( compra.getPkCompra() );
            BigDecimal totalGross = MetodosUtil.getGrossTotalCompra( lista_itens_compra );
            System.out.println( "Invoice No: " + compra.getCodFact().replaceAll( "CO", "FT" ) );
            String hash = MetodosUtil.criptografia_hash( compra, totalGross.doubleValue(), conexao );
            String sql = "UPDATE compras set hash_cod = '" + hash + "' WHERE pk_compra = " + compra.getPkCompra();

            conexao.executeUpdate( sql );

        }

        return true;
    }

}
