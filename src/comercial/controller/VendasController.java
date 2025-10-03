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
import entity.TbBanco;
import entity.TbCliente;
import entity.TbVenda;
import entity.TbUsuario;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;
import visao.FicheiroSAFTVisao;
import static visao.FicheiroSAFTVisao.jProgressBar1;

/**
 *
 * @author Martinho Luis & Domingos Dala Vunge
 */
public class VendasController implements EntidadeFactory
{

    private BDConexao conexao;

    public VendasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbVenda venda = (TbVenda) object;
        String INSERT = "INSERT INTO tb_venda( dataVenda , total_venda , performance , credito , valor_entregue , troco , "
                + " hora , nome_cliente , status_eliminado , desconto_total  , total_iva , total_geral , cod_fact , assinatura , "
                + " hash_cod , obs , ref_cod_fact , total_por_extenso  , desconto_comercial , desconto_financeiro , "
                + " total_incidencia , local_carga , local_descarga , codigo_usuario , codigo_cliente , idArmazemFK , "
                + " fk_documento , fk_ano_economico , fk_cambio , dataVencimento  , cliente_nif , total_incidencia_isento , ref_data_fact , "
                + " nome_consumidor_final , referencia , matricula  , modelo , num_chassi , num_motor , kilometro , nome_motorista ,"
                + " marca_carro , cor_carro , n_doc_motorista , total_retencao  , gorjeta "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( venda.getDataVenda() ) + "' , "
                + venda.getTotalVenda() + " , "
                + "'" + venda.getPerformance() + "' , "
                + "'" + venda.getCredito() + "' , "
                + venda.getValorEntregue() + " , "
                + venda.getTroco() + " , "
                + "'" + MetodosUtil.getHoraBanco( venda.getHora() ) + "' , "
                + "'" + venda.getNomeCliente() + "' , "
                + "'" + venda.getStatusEliminado() + "' , "
                + venda.getDescontoTotal() + " , "
                + venda.getTotalIva() + " , "
                + venda.getTotalGeral() + " , "
                + "'" + venda.getCodFact() + "' , "
                + "'" + venda.getAssinatura() + "' , "
                + "'" + venda.getHashCod() + "' , "
                + "'" + venda.getObs() + "' , "
                + "'" + venda.getRefCodFact() + "' , "
                + "'" + venda.getTotalPorExtenso() + "' , "
                + venda.getDescontoComercial() + " , "
                + venda.getDescontoFinanceiro() + " , "
                + venda.getTotalIncidencia() + " , "
                + "'" + venda.getLocalCarga() + "' , "
                + "'" + venda.getLocalDescarga() + "' , "
                //                + venda.getIdBanco().getIdBanco() + ", "
                + venda.getCodigoUsuario().getCodigo() + ", "
                + venda.getCodigoCliente().getCodigo() + ", "
                + venda.getIdArmazemFK().getCodigo() + ", "
                + venda.getFkDocumento().getPkDocumento() + ", "
                + venda.getFkAnoEconomico().getPkAnoEconomico() + ", "
                + venda.getFkCambio().getPkCambio() + ", "
                + "'" + MetodosUtil.getDataBanco( venda.getDataVencimento() ) + "' , "
                + "'" + venda.getClienteNif() + "' , "
                + venda.getTotalIncidenciaIsento() + " , "
                + "'" + MetodosUtil.getDataBancoFull( venda.getRefDataFact() ) + "' , "
                //                + "'" + venda.getCont() + "' , "
                + "'" + venda.getNomeConsumidorFinal() + "' , "
                + "'" + venda.getReferencia() + "' , "
                + "'" + venda.getMatricula() + "' , "
                + "'" + venda.getModelo() + "' , "
                + "'" + venda.getNumChassi() + "' , "
                + "'" + venda.getNumMotor() + "' , "
                + "'" + venda.getKilometro() + "' , "
                + "'" + venda.getNomeMotorista() + "' , "
                + "'" + venda.getMarcaCarro() + "' , "
                + "'" + venda.getCorCarro() + "' , "
                + "'" + venda.getNDocMotorista() + "' , "
                + venda.getTotalRetencao() + " , "
                + venda.getGorjeta()
                + " ) ";

        System.out.println( "Modo INSERT" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbVenda venda = (TbVenda) object;

        String sql = "UPDATE tb_venda SET "
                + " total_por_extenso  = '" + venda.getTotalPorExtenso() + "'"
                + " , desconto_comercial = " + venda.getDescontoComercial()
                + ", desconto_financeiro  = " + venda.getDescontoFinanceiro()
                + ", total_venda = " + venda.getTotalVenda()
                + ", total_geral = " + venda.getTotalGeral()
                + ", codigo_usuario = " + venda.getCodigoUsuario().getCodigo()
                + " WHERE cod_fact = '" + venda.getCodFact() + "'";
        System.out.println( sql );
        return conexao.executeUpdate( sql );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_venda WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbVenda> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_venda ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbVenda> lista_venda = new ArrayList<>();
        TbVenda venda;
        try
        {

            while ( result.next() )
            {
                venda = new TbVenda();
                venda.setCodigo( result.getInt( "maximo_id" ) );
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
                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
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

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_venda WHERE codigo = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbVenda venda = null;
        try
        {

            if ( result.next() )
            {
                venda = new TbVenda();
                venda.setCodigo( result.getInt( "codigo" ) );
                venda.setDataVenda( result.getTimestamp( "dataVenda" ) );
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
                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
                venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return venda;

    }

    public void actualizar_hash_and_assinatura( int id_venda, double gross_total ) throws SQLException
    {
        TbVenda venda = (TbVenda) findById( id_venda );

        String hash = MetodosUtil.criptografia_hash( venda, gross_total, conexao );
        String assinatura = MetodosUtil.assinatura_doc( hash );

        String sql = "UPDATE tb_venda v SET v.hash_cod = '" + hash + "', v.assinatura = '" + assinatura + "' WHERE v.codigo = " + id_venda;

        conexao.executeUpdate( sql );
    }

    public Integer salvarRetornaID( TbVenda venda ) throws SQLException
    {
        String sql = "INSERT INTO tb_venda ("
                + "dataVenda, total_venda, performance, credito, valor_entregue, troco, hora, nome_cliente, status_eliminado, desconto_total, "
                + "total_iva, total_geral, cod_fact, assinatura, hash_cod, obs, ref_cod_fact, total_por_extenso, status_recibo, desconto_comercial, "
                + "desconto_financeiro, total_incidencia, local_carga, local_descarga, idBanco, codigo_usuario, codigo_cliente, idArmazemFK, fk_documento, fk_ano_economico, "
                + "fk_cambio, dataVencimento, cliente_nif, total_incidencia_isento, ref_data_fact, cont, nome_consumidor_final, referencia, matricula, modelo, "
                + "num_chassi, num_motor, kilometro, nome_motorista, marca_carro, cor_carro, n_doc_motorista, total_retencao, gorjeta "
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conexao.getConnection1().prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );

//        stmt.setDate( 1, new java.sql.Date( venda.getDataVenda().getTime() ) );
        stmt.setTimestamp( 1, new java.sql.Timestamp( venda.getDataVenda().getTime() ) );

        stmt.setBigDecimal( 2, venda.getTotalVenda() );
        stmt.setString( 3, venda.getPerformance() );
        stmt.setString( 4, venda.getCredito() );
        stmt.setBigDecimal( 5, venda.getValorEntregue() );
        stmt.setBigDecimal( 6, venda.getTroco() );

        stmt.setTime( 7, new java.sql.Time( venda.getHora().getTime() ) );
        stmt.setString( 8, venda.getNomeCliente() );
        stmt.setString( 9, venda.getStatusEliminado() );
        stmt.setBigDecimal( 10, venda.getDescontoTotal() );
        stmt.setBigDecimal( 11, venda.getTotalIva() );
        stmt.setBigDecimal( 12, venda.getTotalGeral() );
        stmt.setString( 13, venda.getCodFact() );
        stmt.setString( 14, venda.getAssinatura() );
        stmt.setString( 15, venda.getHashCod() );
        stmt.setString( 16, venda.getObs() );
        stmt.setString( 17, venda.getRefCodFact() );
        stmt.setString( 18, venda.getTotalPorExtenso() );
        stmt.setBoolean( 19, venda.getStatusRecibo() );
        stmt.setBigDecimal( 20, venda.getDescontoComercial() );
        stmt.setBigDecimal( 21, venda.getDescontoFinanceiro() );
        stmt.setBigDecimal( 22, venda.getTotalIncidencia() );
        stmt.setString( 23, venda.getLocalCarga() );
        stmt.setString( 24, venda.getLocalDescarga() );

        stmt.setInt( 25, venda.getIdBanco() != null ? venda.getIdBanco().getIdBanco() : 0 );
        stmt.setInt( 26, venda.getCodigoUsuario() != null ? venda.getCodigoUsuario().getCodigo() : 0 );
        stmt.setInt( 27, venda.getCodigoCliente() != null ? venda.getCodigoCliente().getCodigo() : 0 );
        stmt.setInt( 28, venda.getIdArmazemFK() != null ? venda.getIdArmazemFK().getCodigo() : 0 );
        stmt.setInt( 29, venda.getFkDocumento() != null ? venda.getFkDocumento().getPkDocumento() : 0 );
        stmt.setInt( 30, venda.getFkAnoEconomico() != null ? venda.getFkAnoEconomico().getPkAnoEconomico() : 0 );
        stmt.setInt( 31, venda.getFkCambio() != null ? venda.getFkCambio().getPkCambio() : 0 );

        stmt.setDate( 32, venda.getDataVencimento() != null ? new java.sql.Date( venda.getDataVencimento().getTime() ) : null );
        stmt.setString( 33, venda.getClienteNif() );
        stmt.setBigDecimal( 34, venda.getTotalIncidenciaIsento() );
        stmt.setDate( 35, venda.getRefDataFact() != null ? new java.sql.Date( venda.getRefDataFact().getTime() ) : null );
        stmt.setInt( 36, venda.getCont() );
        stmt.setString( 37, venda.getNomeConsumidorFinal() );
        stmt.setString( 38, venda.getReferencia() );
        stmt.setString( 39, venda.getMatricula() );
        stmt.setString( 40, venda.getModelo() );
        stmt.setString( 41, venda.getNumChassi() );
        stmt.setString( 42, venda.getNumMotor() );
        stmt.setString( 43, venda.getKilometro() );
        stmt.setString( 44, venda.getNomeMotorista() );
        stmt.setString( 45, venda.getMarcaCarro() );
        stmt.setString( 46, venda.getCorCarro() );
        stmt.setString( 47, venda.getNDocMotorista() );
        stmt.setBigDecimal( 48, venda.getTotalRetencao() );
        stmt.setBigDecimal( 49, venda.getGorjeta() );

        int resultado = stmt.executeUpdate();
        if ( resultado > 0 )
        {
            ResultSet rs = stmt.getGeneratedKeys();
            if ( rs.next() )
            {
                return rs.getInt( 1 ); // Código gerado
            }
        }

        return null;
    }

    public List<TbVenda> findVendasByClientes( int codigo )
    {

        String FIND_ALL = "SELECT v.* FROM tb_venda v INNER JOIN amortizacao_divida a ON a.fk_venda = v.codigo WHERE v.codigo_cliente = " + codigo;
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
                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
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

    public List<TbVenda> findVendasByClientesDoc( int codigo )
    {

//        String FIND_ALL = "SELECT DISTINCT v.* FROM tb_venda v WHERE v.codigo = (SELECT MAX(v.codigo) FROM tb_venda v WHERE v.codigo_cliente = " + codigo + ") AND fk_documento = " + DVML.DOC_FACTURA_FT;
//        String FIND_ALL = "SELECT DISTINCT v.* FROM tb_venda v WHERE v.codigo = (SELECT MAX(v.codigo) FROM tb_venda v WHERE v.codigo_cliente = " + codigo + ")";
//        String FIND_ALL = "SELECT DISTINCT v.codigo, total_venda, cod_fact, codigo_cliente FROM tb_venda v WHERE v.codigo = (SELECT MAX(v.codigo) FROM tb_venda v WHERE v.codigo_cliente = " + codigo + " AND fk_documento = " + DVML.DOC_FACTURA_FT;
        String FIND_ALL = "SELECT v.* FROM tb_venda v WHERE v.codigo_cliente = " + codigo + " AND status_eliminado = 'false' AND fk_documento = " + DVML.DOC_FACTURA_FT;
//        String FIND_ALL = "SELECT v.* FROM tb_venda v WHERE v.codigo_cliente = " + codigo + " AND fk_documento = " + DVML.DOC_FACTURA_FT;
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

    public TbVenda getLastVenda()
    {

        String FIND__BY_CODIGO = "   SELECT * FROM tb_venda WHERE codigo = ( SELECT MAX(codigo) as maximo_id   FROM tb_venda v ) ";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbVenda venda = null;
        try
        {

            if ( result.next() )
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
                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
                venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return venda;

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
            venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
            venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
            venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
            venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
            venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
            venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
            venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
            venda.setDataVencimento( result.getDate( "dataVencimento" ) );
            venda.setClienteNif( result.getString( "cliente_nif" ) );
            venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
            venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
            venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
            venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );
            venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );

        }
        catch ( Exception e )
        {
        }

        return venda;
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

    public boolean actualizarCodFac( String codFact, int codigo )
    {
        String sql = "UPDATE tb_venda SET cod_fact = '" + codFact + "' WHERE codigo = " + codigo;
        return conexao.executeUpdate( sql );
    }

    public Integer getLastCodigoVenda( int pk_documento, int pk_ano_economico )
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
        }

        return 0;

    }

    public int getUltimaContagemByIdDocumentoAndAnoEconomico( int pk_documento, int pk_ano_economico )
    {
        try
        {
            Integer codVenda = getLastCodigoVenda( pk_documento, pk_ano_economico );
            System.out.println( "LastCodVenda: " + codVenda );
            String codFact = MetodosUtil.getCodFact( codVenda, conexao );
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

    public List<TbVenda> listarTodosIdDocumentoAndIdAnoEconomico( int pk_documento, int pk_ano_economico )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE fk_documento = " + pk_documento + " AND fk_ano_economico = " + pk_ano_economico;
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

    public boolean actualizarContagem()
    {
        List<TbVenda> list = listarTodosIdDocumentoAndIdAnoEconomico(
                DVML.DOC_FACTURA_RECIBO_FR, 5 );

        int cod = 1;
        String codFact;
        boolean sucesso = true;

        for ( TbVenda documento : list )
        {
            codFact = "FR 2024/" + cod;
            actualizarCodFac( codFact, documento.getCodigo() );
            cod++;
        }

        return sucesso;
    }

    public TbVenda getVendaByCodFact( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );

        TbVenda venda = null;
        try
        {
            if ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                getVendaResultSet( venda, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return venda;
    }
//            public TbVenda getVendaByCodFact( String cod_fact )
//    {
//        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
//        ResultSet result = conexao.executeQuery( FIND_ALL );
//       
//        TbVenda venda = null;
//        try
//        {
//            if ( result.next() )
//            {
//                System.out.println( result.getString( "cod_fact" ) );
//                venda = new TbVenda();
//                getVendaResultSet( venda, result );
//            }
//        }
//        catch ( SQLException e )
//        {
//            e.printStackTrace();
//        }
//
//        return venda;
//    }

    public TbVenda findByCodFact( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "' AND fk_documento = " + DVML.DOC_GUIA_TRANSPORTE_GT;
        ResultSet result = conexao.executeQuery( FIND_ALL );

        TbVenda venda = null;
        try
        {
            if ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                getVendaResultSet( venda, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return venda;
    }

    public TbVenda findByCodFactX( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );

        TbVenda venda = null;
        try
        {
            if ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                getVendaResultSet( venda, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return venda;
    }

    public TbVenda findByCodFactDoc( String cod_fact, int pk_documento )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "' AND fk_documento = " + pk_documento;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        System.out.println( "Query" + FIND_ALL );

        TbVenda venda = null;
        try
        {
            if ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                getVendaResultSet( venda, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return venda;
    }

    public TbVenda findByCodFactAndDoc( String cod_fact, int pk_documento )
    {

        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "' AND fk_documento = " + pk_documento;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        System.out.println( "Query" + FIND_ALL );
        TbVenda venda = null;
        try
        {

            if ( result.next() )
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
                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
                venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return venda;

    }

    public TbVenda getVendaByCodFactFT( String cod_fact )
    {
        String FIND_ALL = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );

        TbVenda venda = null;
        try
        {
            if ( result.next() )
            {
                System.out.println( result.getString( "cod_fact" ) );
                venda = new TbVenda();
                getVendaResultSet( venda, result );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return venda;
    }

    public static boolean actualizar_hash_new( List<TbVenda> list, BDConexao conexao )
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
                        Logger.getLogger( VendasController.class.getName() ).log( Level.SEVERE, null, ex );
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
                        Logger.getLogger( VendasController.class.getName() ).log( Level.SEVERE, null, ex );
                    }

                }

            }
        } );

        t.start();

        return true;

    }

    public TbVenda getVendas( int codigo )
    {
//        String FIND__BY_CODIGO = "SELECT cod_fact as cod_fact, v.*  FROM tb_venda v WHERE cod_fact = '" + cod_factura + "'";
//        String FIND__BY_CODIGO = "SELECT * FROM tb_venda WHERE cod_fact = '" + cod_factura + "'";
        String FIND_BY_CODIGO = "SELECT v.codigo FROM tb_venda v WHERE codigo = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbVenda venda = null;
        try
        {

            if ( result.next() )
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
//                venda.setIdBanco( new TbBanco( result.getInt( "idBanco" ) ) );
                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                venda.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                venda.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
                venda.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
                venda.setDataVencimento( result.getDate( "dataVencimento" ) );
                venda.setClienteNif( result.getString( "cliente_nif" ) );
                venda.setTotalIncidenciaIsento( result.getBigDecimal( "total_incidencia_isento" ) );
                venda.setRefDataFact( result.getDate( "ref_data_fact" ) );
//                venda.setCont( result.getInt( "cont" ) );
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
                venda.setTotalRetencao( result.getBigDecimal( "total_retencao" ) );
                venda.setGorjeta( result.getBigDecimal( "gorjeta" ) );
//                venda.setAreaVenda( result.getString( "area_venda" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return venda;

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

    public List<TbCliente> listarAllClientesLavandariaEntregas()
    {
        String FIND_ALL = "select c.codigo , c.nome, c.telefone  "
                + "from tb_item_venda i "
                + "inner join tb_venda v on v.codigo = i.codigo_venda  "
                + "inner join tb_cliente c on c.codigo  = v.codigo_cliente  "
                + "where i.status_entrega = 'false' "
                + "and v.fk_documento  in(1, 2) "
                + "group by c.nome "
                + "order by c.nome asc ";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbCliente> lista_clientes = new ArrayList<>();
        TbCliente cliente;
        try
        {
            while ( result.next() )
            {
                int codigo = result.getInt( "codigo" );
                String nome = result.getString( "nome" );
                String telefone = result.getString( "telefone" );

                cliente = new TbCliente();
                cliente.setCodigo( codigo );
                cliente.setNome( nome );
                cliente.setTelefone( telefone );

                lista_clientes.add( cliente );

            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_clientes;
    }

    public List<RelatorioRecolhaLavandaria> relatorioRecolhaLavandariaByIdCliente( int idCliente )
    {
        String query = "select   "
                + "	v.cod_fact as codFact , "
                + "	date(v.dataVenda) as dataVenda , "
                + "	( "
                + "         getNumeroPecasLavandaria(false, v.cod_fact) "
                + "	) as pecas_por_entregar, "
                + "	( "
                + "         getNumeroPecasLavandaria(true, v.cod_fact) "
                + "	) as pecas_entregas "
                + "from tb_venda v  "
                + "inner join tb_cliente c on c.codigo  = v.codigo_cliente "
                + "where  v.fk_documento  in(1, 2)"
                + "and  v.codigo_cliente  = " + idCliente;
        //+ "  having pecas_por_entregar > 0";
        ResultSet result = conexao.executeQuery( query );
        List<RelatorioRecolhaLavandaria> lista = new ArrayList<>();
        RelatorioRecolhaLavandaria rrl;
        try
        {
            while ( result.next() )
            {
                String codFact = result.getString( "codFact" );
                Date data = result.getDate( "dataVenda" );
                int pecasPorEntregar = result.getInt( "pecas_por_entregar" );
                int pecasEntregas = result.getInt( "pecas_entregas" );
                rrl = new RelatorioRecolhaLavandaria();
                rrl.setCodFact( codFact );
                rrl.setData( data );
                rrl.setPecasPorEntregar( pecasPorEntregar );
                rrl.setPecasEntregue( pecasEntregas );

                lista.add( rrl );

            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PecasEntregues> relatorioPecasEntreguesByCodFact( String refCodFact )
    {
        String query = "select "
                + "	p.codigo  as codigo, "
                + "	p.designacao as designacao ,"
                + "	v.cod_fact as doc, "
                + "	u.nome as nome_usuario "
                + "from tb_item_venda i "
                + "inner join tb_venda v on v.codigo = i.codigo_venda "
                + "inner join tb_produto p on p.codigo = i.codigo_produto "
                + "inner join tb_usuario u on u.codigo = v.codigo_usuario "
                + "where i.status_entrega = 0 "
                + "and v.ref_cod_fact  = '" + refCodFact + "'";
        //+ "  having pecas_por_entregar > 0";

        System.out.println( query );
        ResultSet result = conexao.executeQuery( query );
        List<PecasEntregues> lista = new ArrayList<>();
        PecasEntregues pe;
        try
        {
            while ( result.next() )
            {
                int codServico = result.getInt( "codigo" );
                String designacao = result.getString( "designacao" );
                String codFact = result.getString( "doc" );
                String usuario = result.getString( "nome_usuario" );

                pe = new PecasEntregues();
                pe.setCodServico( codServico );
                pe.setDesignacao( designacao );
                pe.setDoc( codFact );
                pe.setUsuario( usuario );

                lista.add( pe );

            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PecasPorEntregar> relatorioPorEntregarByCodFact( String codFact )
    {
        String query = " select "
                + "	 p.codigo  as codigo, "
                + "	 p.designacao as designacao , "
                + "	 i.obs as obs "
                + " from tb_item_venda i "
                + " inner join tb_venda v on v.codigo = i.codigo_venda "
                + " inner join tb_produto p on p.codigo = i.codigo_produto "
                + " inner join tb_usuario u on u.codigo = v.codigo_usuario "
                + " where i.status_entrega = 0 "
                + " and v.cod_fact  = '" + codFact + "'";
        //+ "  having pecas_por_entregar > 0";
        ResultSet result = conexao.executeQuery( query );
        List<PecasPorEntregar> lista = new ArrayList<>();
        PecasPorEntregar pe;
        try
        {
            while ( result.next() )
            {
                int codServico = result.getInt( "codigo" );
                String designacao = result.getString( "designacao" );
                String obs = result.getString( "obs" );

                pe = new PecasPorEntregar();
                pe.setCodServico( codServico );
                pe.setDesignacao( designacao );
                pe.setObs( obs );

                lista.add( pe );

                //BONIFICADOS
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista;
    }

    public List<TbUsuario> listarUsuario( Date data )
    {
        String FIND_ALL = "select u.codigo as codigo, u.nome as nome "
                + "from tb_venda v "
                + "inner join tb_usuario u on u.codigo = v.codigo_usuario "
                + "where date(v.dataVenda) =  '" + MetodosUtil.getDataBanco( data ) + "' "
                + "and v.status_eliminado = 'false' "
                + "and v.fk_documento = 1 "
                + "GROUP by u.codigo ";
        System.out.println( FIND_ALL );
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbUsuario> listaUsuario = new ArrayList<>();
        try
        {
            while ( result.next() )
            {
                int codigo = result.getInt( "codigo" );
                String nome = result.getString( "nome" );

                TbUsuario usuario = new TbUsuario();
                usuario.setCodigo( codigo );
                usuario.setNome( nome );

                listaUsuario.add( usuario );

            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return listaUsuario;
    }

    public class RelatorioRecolhaLavandaria
    {

        String codFact;
        Date data;
        int pecasEntregue;
        int pecasPorEntregar;

        public RelatorioRecolhaLavandaria()
        {
        }

        public String getCodFact()
        {
            return codFact;
        }

        public void setCodFact( String codFact )
        {
            this.codFact = codFact;
        }

        public Date getData()
        {
            return data;
        }

        public void setData( Date data )
        {
            this.data = data;
        }

        public int getPecasEntregue()
        {
            return pecasEntregue;
        }

        public void setPecasEntregue( int pecasEntregue )
        {
            this.pecasEntregue = pecasEntregue;
        }

        public int getPecasPorEntregar()
        {
            return pecasPorEntregar;
        }

        public void setPecasPorEntregar( int pecasPorEntregar )
        {
            this.pecasPorEntregar = pecasPorEntregar;
        }

    }

    public class PecasEntregues
    {

        int codServico;
        String designacao;
        String doc;
        String usuario;

        public PecasEntregues()
        {
        }

        public int getCodServico()
        {
            return codServico;
        }

        public void setCodServico( int codServico )
        {
            this.codServico = codServico;
        }

        public String getDesignacao()
        {
            return designacao;
        }

        public void setDesignacao( String designacao )
        {
            this.designacao = designacao;
        }

        public String getDoc()
        {
            return doc;
        }

        public void setDoc( String doc )
        {
            this.doc = doc;
        }

        public String getUsuario()
        {
            return usuario;
        }

        public void setUsuario( String usuario )
        {
            this.usuario = usuario;
        }

    }

    public class PecasPorEntregar
    {

        int codServico;
        String designacao;
        String obs;

        public PecasPorEntregar()
        {
        }

        public int getCodServico()
        {
            return codServico;
        }

        public void setCodServico( int codServico )
        {
            this.codServico = codServico;
        }

        public String getDesignacao()
        {
            return designacao;
        }

        public void setDesignacao( String designacao )
        {
            this.designacao = designacao;
        }

        public String getObs()
        {
            return obs;
        }

        public void setObs( String obs )
        {
            this.obs = obs;
        }

    }

    public TbVenda findByCodFactReemprensao( String codFact )
    {
        TbVenda venda = null;
        String sql = "SELECT * FROM tb_venda WHERE cod_fact = ?"; // ajuste conforme o nome real da tabela

        BDConexao bd = new BDConexao(); // cria a conexão

        try ( Connection conn = bd.getConnection1(); PreparedStatement ps = conn.prepareStatement( sql ) )
        {

            ps.setString( 1, codFact ); // define o parâmetro

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    venda = new TbVenda();

                    // Mapeando os campos do ResultSet para o objeto TbVenda
                    venda.setCodigo( rs.getInt( "codigo" ) ); // exemplo
                    venda.setCodFact( rs.getString( "cod_fact" ) );
                    venda.setDataVenda( rs.getDate( "dataVenda" ) );
                    venda.setTotalVenda( rs.getBigDecimal( "total_venda" ) );
                    // mapeie todos os outros campos que existirem
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace(); // ou use logging
        }

        return venda;
    }

//   public List<TbVenda> getAllFRVendaByBetweenDataAndArmazemAndDocumento(
//        Date data_inicio, Date data_fim, int codigo_usuario) {
//
//    List<TbVenda> vendas = new ArrayList<>();
//    String sql = "SELECT * FROM tb_venda "
//               + "WHERE DATE(dataVenda) BETWEEN ? AND ? "
//               + "AND status_eliminado = false "
//               + "AND credito = false "
//               + "AND codigo_usuario = ?"; // novo filtro
//
//    BDConexao bd = new BDConexao();
//
//    try (Connection conn = bd.getConnection1();
//         PreparedStatement ps = conn.prepareStatement(sql)) {
//
//        // Definindo os parâmetros do PreparedStatement
//        ps.setDate(1, new java.sql.Date(data_inicio.getTime()));
//        ps.setDate(2, new java.sql.Date(data_fim.getTime()));
//        ps.setInt(3, codigo_usuario);
//
//        try (ResultSet result = ps.executeQuery()) {
//            while (result.next()) {
//                TbVenda venda = new TbVenda();
//
//                // Mapear todos os campos necessários da tabela para a entidade
//                venda.setCodigo(result.getInt("codigo"));
//                venda.setCodFact(result.getString("cod_fact"));
//                venda.setDataVenda(result.getDate("dataVenda"));
//                venda.setHora(result.getTime("hora"));
//
////                venda.setHora(result.getDate("hora"));
//                venda.setTotalVenda(result.getBigDecimal("total_venda"));
//                venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
//                venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
//                venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
//                venda.setCodigoCliente(new TbCliente( result.getInt( "codigo_cliente" ) ) );
//                // adicione outros campos conforme necessário
//
//                vendas.add(venda);
//            }
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace(); // ou use logger apropriado
//    }
//
//    return vendas;
//}
    public List<TbVenda> getAllFRVendaByBetweenDataAndArmazemAndDocumentos(
            Date data_inicio, Date data_fim, int codigo_usuario )
    {

        List<TbVenda> vendas = new ArrayList<>();

        String sql = "SELECT v.*, c.nome AS nome_cliente "
                + "FROM tb_venda v "
                + "JOIN tb_cliente c ON v.codigo_cliente = c.codigo "
                + "WHERE DATE(v.dataVenda) BETWEEN ? AND ? "
                + "AND v.status_eliminado = 'false' "
                + "AND v.credito = false "
                + "AND v.fk_documento = 1 "
                + "AND v.codigo_usuario = ? ORDER BY codigo ASC";

        BDConexao bd = new BDConexao();

        try ( Connection conn = bd.getConnection1(); PreparedStatement ps = conn.prepareStatement( sql ) )
        {

            // Definindo os parâmetros do PreparedStatement
            ps.setDate( 1, new java.sql.Date( data_inicio.getTime() ) );
            ps.setDate( 2, new java.sql.Date( data_fim.getTime() ) );
            ps.setInt( 3, codigo_usuario );
//        ps.setInt(4, pk_documento);

            try ( ResultSet result = ps.executeQuery() )
            {
                while ( result.next() )
                {
                    TbVenda venda = new TbVenda();

                    // Mapear todos os campos da tabela tb_venda
                    venda.setCodigo( result.getInt( "codigo" ) );
                    venda.setCodFact( result.getString( "cod_fact" ) );
                    venda.setDataVenda( result.getDate( "dataVenda" ) );
                    venda.setHora( result.getTime( "hora" ) );
                    venda.setTotalVenda( result.getBigDecimal( "total_venda" ) );
                    venda.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
                    venda.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
                    venda.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
                    // Cliente com ID e nome
                    TbCliente cliente = new TbCliente( result.getInt( "codigo_cliente" ) );
                    cliente.setNome( result.getString( "nome_cliente" ) );
                    venda.setCodigoCliente( cliente );

                    vendas.add( venda );
                }
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace(); // ou usa logger apropriado
        }

        return vendas;
    }

    public static void main( String[] args )
    {
        BDConexao conexao = new BDConexao();
        VendasController v = new VendasController( conexao );
        v.actualizarContagem();
    }

}
