/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import dao.VendaDao;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbCliente;
import entity.TbUsuario;
import entity.TbVenda;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;
import visao.FicheiroSAFTVisao;
import static visao.FicheiroSAFTVisao.jProgressBar1;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AmortizacaoDividaController
{

    private BDConexao conexao;

    public AmortizacaoDividaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    public List<TbVenda> listarTodosByData1AndData2AndTipoDocumento( Date data_1, Date data_2, int pk_documento )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE fk_documento = " + pk_documento + " AND DATE(dataVenda) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {
            while ( result.next() )
            {
                venda = new TbVenda();
                System.out.println( result.getString( "cod_fact" ) );
                venda = getVendaResultSet( venda, result );
                lista_venda.add( venda );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_venda;
    }

    public List<TbVenda> getAllVendasClientes( int pk_documento )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE fk_documento = " + pk_documento;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {
            while ( result.next() )
            {
                venda = new TbVenda();
                System.out.println( result.getString( "cod_fact" ) );
                venda = getVendaResultSet( venda, result );
                lista_venda.add( venda );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_venda;
    }

    /**
     *
     * @param cod_fact
     * @decricao busca todos os recibos de uma determnada factura.
     * @return
     */
    public List<TbVenda> listarAllReciboByCodFact( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE ref_cod_fact = '" + cod_fact + "' AND fk_documento = " + DVML.DOC_RECIBO_RC;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {
            while ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                lista_venda.add( getVendaResultSet( venda, result ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_venda;
    }

    private TbVenda getVendaResultSet( TbVenda venda, ResultSet result )
    {
        try
        {
            venda.setCodigo( result.getInt( "codigo" ) );
            venda.setDataVenda( result.getDate( "dataVenda" ) );
            venda.setTotalVenda( result.getBigDecimal( "total_venda" ) );
            venda.setPerformance( result.getString( "performance" ) );
            venda.setCredito( result.getString( "credito" ) );
            venda.setValorEntregue( result.getBigDecimal( "valor_entregue" ) );
            venda.setTroco( result.getBigDecimal( "troco" ) );
            venda.setHora( result.getTime( "hora" ) );
            venda.setNomeCliente( result.getString( "nome_cliente" ) );
            venda.setStatusEliminado( result.getString( "status_eliminado" ) );
            venda.setDescontoTotal( result.getBigDecimal( "desconto_total" ) );
            venda.setTotalIva( result.getBigDecimal( "total_iva" ) );
            venda.setTotalGeral( result.getBigDecimal( "total_geral" ) );
            venda.setCodFact( result.getString( "cod_fact" ) );
            venda.setAssinatura( result.getString( "assinatura" ) );
            venda.setHashCod( result.getString( "hash_cod" ) );
            venda.setObs( result.getString( "obs" ) );
            venda.setRefCodFact( result.getString( "ref_cod_fact" ) );
            venda.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
            venda.setStatusRecibo( result.getBoolean( "status_recibo" ) );
            venda.setDescontoComercial( result.getBigDecimal( "desconto_comercial" ) );
            venda.setDescontoFinanceiro( result.getBigDecimal( "desconto_financeiro" ) );
            venda.setTotalIncidencia( result.getBigDecimal( "total_incidencia" ) );
            venda.setLocalCarga( result.getString( "local_carga" ) );
            venda.setLocalDescarga( result.getString( "local_descarga" ) );
            venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
            venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
            venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
            venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
            venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
            venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
            venda.setDataVencimento( result.getDate( "dataVencimento" ) );
            venda.setClienteNif( result.getString( "cliente_nif" ) );
            venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
            venda.setRefCodFact( result.getString( "ref_data_fact" ) );
            venda.setCont( result.getInt( "cont" ) );
            venda.setNomeConsumidorFinal( result.getString( "nome_consumidor_final" ) );
            venda.setReferencia( result.getString( "referencia" ) );
            venda.setMatricula( result.getString( "matricula" ) );
            venda.setModelo( result.getString( "modelo" ) );
            venda.setNumChassi( result.getString( "num_chassi" ) );
            venda.setNumMotor( result.getString( "num_motor" ) );
            venda.setKilometro( result.getString( "kilometro" ) );
            venda.setNomeMotorista( result.getString( "nome_motorista" ) );
            venda.setMarcaCarro( result.getString( "marca_carro" ) );
            venda.setCorCarro( result.getString( "cor_carro" ) );
            venda.setNDocMotorista( result.getString( "n_doc_motorista" ) );

        }
        catch ( Exception e )
        {
        }

        return venda;
    }

    public static boolean actualizar_hash( List<TbVenda> list, BDConexao conexao )
    {

        for ( int i = 0; i < list.size(); i++ )
        {
            TbVenda venda = list.get( i );

            BigDecimal totalGross = MetodosUtil.getGrossTotal( venda.getTbItemVendaList() );
            System.out.println( "InvoiceNo: " + venda.getCodFact() );
            String hash = MetodosUtil.criptografia_hash( venda, totalGross.doubleValue(), conexao );

            String sql = "UPDATE tb_venda set hash_cod = '" + hash + "' WHERE codigo = " + venda.getCodigo();

            conexao.executeUpdate( sql );

        }

        return true;
    }

    public static boolean actualizar_hash( List<TbVenda> list, VendaDao vendaDao, BDConexao conexao )
    {

//        for ( int i = 0; i < list.size(); i++ )
//        {
//            TbVenda venda = list.get( i );
//
//            double totalGross = MetodosUtil.getGrossTotal( venda.getTbItemVendaList() );
//            System.out.println( "InvoiceNo: " + venda.getCodFact() );
//            String hash = MetodosUtil.criptografia_hash( venda, totalGross, conexao );
//
//            String sql = "UPDATE tb_venda set hash_cod = '" + hash + "' WHERE codigo = " + venda.getCodigo();
//
//            conexao.executeUpdate( sql );
//
//        }
//
//        return true;
        jProgressBar1.setMaximum( list.size() );
        jProgressBar1.setValue( 0 );

        Thread t = new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                boolean efectuado = true;

                for ( int i = 0, j = 1; i < list.size(); i++ )
                {
                    try
                    {
//                        Thread.sleep( 50 );
                        Thread.sleep( 100 );
                        TbVenda venda = list.get( i );
                        BigDecimal totalGross = MetodosUtil.getGrossTotal( venda.getTbItemVendaList() );
                        System.out.println( "InvoiceNo: " + venda.getCodFact() );
                        String hash = MetodosUtil.criptografia_hash( venda, totalGross.doubleValue(), conexao );

                        String sql = "UPDATE tb_venda set hash_cod = '" + hash + "' WHERE codigo = " + venda.getCodigo();
                        conexao.executeUpdate( sql );
                        jProgressBar1.setValue( j++ );

                    }
                    catch ( InterruptedException ex )
                    {
                        Logger.getLogger( AmortizacaoDividaController.class.getName() ).log( Level.SEVERE, null, ex );
                    }

                }
                if ( efectuado )
                {

                    try
                    {
                        FicheiroSAFTVisao.txtStatus.setText( "A escrever no ficheiro, aguarde!..." );
                        FicheiroSAFTVisao.gerarFcheiroSAFT();
                        this.finalize();
                    }
                    catch ( Throwable ex )
                    {
                        Logger.getLogger( AmortizacaoDividaController.class.getName() ).log( Level.SEVERE, null, ex );
                    }

                }

            }
        } );

        t.start();

        return true;

    }

    public List<TbVenda> findVendasByClientes( int codigo )
    {

        String FIND_ALL = "SELECT v.* FROM tb_venda v INNER JOIN amortizacao_divida a "
                + "ON a.fk_venda = v.codigo WHERE v.codigo_cliente = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {

            while ( result.next() )
            {
                venda = new TbVenda();
                venda.setCodigo( result.getInt( "codigo" ) );
                venda.setDataVenda( result.getDate( "dataVenda" ) );
                venda.setTotalVenda( result.getBigDecimal( "total_venda" ) );
                venda.setPerformance( result.getString( "performance" ) );
                venda.setCredito( result.getString( "credito" ) );
                venda.setValorEntregue( result.getBigDecimal( "valor_entregue" ) );
                venda.setTroco( result.getBigDecimal( "troco" ) );
                venda.setHora( result.getTime( "hora" ) );
                venda.setNomeCliente( result.getString( "nome_cliente" ) );
                venda.setStatusEliminado( result.getString( "status_eliminado" ) );
                venda.setDescontoTotal( result.getBigDecimal( "desconto_total" ) );
                venda.setTotalIva( result.getBigDecimal( "total_iva" ) );
                venda.setTotalGeral( result.getBigDecimal( "total_geral" ) );
                venda.setCodFact( result.getString( "cod_fact" ) );
                venda.setAssinatura( result.getString( "assinatura" ) );
                venda.setHashCod( result.getString( "hash_cod" ) );
                venda.setObs( result.getString( "obs" ) );
                venda.setRefCodFact( result.getString( "ref_cod_fact" ) );
                venda.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
                venda.setStatusRecibo( result.getBoolean( "status_recibo" ) );
                venda.setDescontoComercial( result.getBigDecimal( "desconto_comercial" ) );
                venda.setDescontoFinanceiro( result.getBigDecimal( "desconto_financeiro" ) );
                venda.setTotalIncidencia( result.getBigDecimal( "total_incidencia" ) );
                venda.setLocalCarga( result.getString( "local_carga" ) );
                venda.setLocalDescarga( result.getString( "local_descarga" ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefCodFact( result.getString( "ref_data_fact" ) );
                venda.setCont( result.getInt( "cont" ) );
                venda.setNomeConsumidorFinal( result.getString( "nome_consumidor_final" ) );
                venda.setReferencia( result.getString( "referencia" ) );
                venda.setMatricula( result.getString( "matricula" ) );
                venda.setModelo( result.getString( "modelo" ) );
                venda.setNumChassi( result.getString( "num_chassi" ) );
                venda.setNumMotor( result.getString( "num_motor" ) );
                venda.setKilometro( result.getString( "kilometro" ) );
                venda.setNomeMotorista( result.getString( "nome_motorista" ) );
                venda.setMarcaCarro( result.getString( "marca_carro" ) );
                venda.setCorCarro( result.getString( "cor_carro" ) );
                venda.setNDocMotorista( result.getString( "n_doc_motorista" ) );
                venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );

                lista_venda.add( venda );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_venda;
    }

    public List<TbVenda> listarAllClientesDevedores( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE ref_cod_fact = '" + cod_fact + "' AND fk_documento = " + DVML.DOC_RECIBO_RC;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {
            while ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                lista_venda.add( getVendaResultSet( venda, result ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_venda;
    }

    public double getValorPendente( int fk_venda )
    {

        String query = "SELECT a.valor_pendente FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
                + " WHERE a.pk_amortizacao_divida = ( SELECT MAX(pk_amortizacao_divida) FROM amortizacao_divida WHERE fk_venda = " + fk_venda + ")";
        ResultSet rs = conexao.executeQuery( query );
        System.out.println( query );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "valor_pendente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public double getValorPendenteByCodFact( String codFact )
    {

        String query = "SELECT a.valor_pendente FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
                + " WHERE a.pk_amortizacao_divida = ( SELECT MAX(pk_amortizacao_divida) FROM amortizacao_divida WHERE v.cod_fact = '" + codFact + "')";
//        String query = "SELECT a.valor_pendente FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
//                + " WHERE a.pk_amortizacao_divida = ( SELECT MAX(pk_amortizacao_divida) FROM amortizacao_divida WHERE v.cod_fact = '" + codFact + "')" ;
        ResultSet rs = conexao.executeQuery( query );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "valor_pendente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public double getValorAtribuido( int fk_venda )
    {

        String query = "SELECT SUM(a.valor_pago) AS TOTAL FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
                + " WHERE fk_venda = " + fk_venda;

//        String query = "SELECT a.valor_entregue FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
//                + " WHERE a.pk_amortizacao_divida = ( SELECT SUM(valor_entregue) AS TOTAL FROM amortizacao_divida WHERE fk_venda = " + fk_venda + ")" ;
//        
        ResultSet rs = conexao.executeQuery( query );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public double getValorAtribuidoByCodFact( String cod_fact )
    {

        String query = "SELECT SUM(a.valor_pago) AS TOTAL FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
                + " WHERE v.cod_fact = '" + cod_fact + "'";
//
//        String query = "SELECT a.valor_entregue FROM amortizacao_divida a inner join tb_venda v on v.codigo = a.fk_venda"
//                + " WHERE a.pk_amortizacao_divida = ( SELECT SUM(valor_entregue) AS TOTAL FROM amortizacao_divida WHERE fk_venda = " + fk_venda + ")" ;
        ResultSet rs = conexao.executeQuery( query );
        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public static double getQtdItensByIdPedidoTradicional( int idProduto, BDConexao conexao )
    {
        String sql = "SELECT SUM(qtd) AS soma_qtd FROM tb_item_pedidos WHERE fk_produtos = " + idProduto + " AND " + " status_convertido = 'false' GROUP BY fk_produtos";

        System.out.println( sql );
        ResultSet result = conexao.executeQuery( sql );

        try
        {
            if ( result.next() )
            {
                return result.getDouble( "soma_qtd" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0.0;
    }

}
