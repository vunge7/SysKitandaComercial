/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.controller;

import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.Notas;
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
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class NotasController
{

    private BDConexao conexao;

    public NotasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    public List<Notas> listarTodosByData1AndData2AndTipoDocumento( Date data_1, Date data_2, int pk_documento )
    {
        String FIND_ALL = "SELECT * FROM notas WHERE   fk_documento = " + pk_documento + " DATE(dataNota) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Notas> lista_venda = new ArrayList<>();
        Notas nota;
        try
        {
            while ( result.next() )
            {
                nota = new Notas();
                lista_venda.add( getNotaResultSet( nota, result ) );
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
     * @param cod_documebto
     * @decricao busca todos os recibos de uma determnada factura.
     * @return
     */
    public List<Notas> listarAllNotaCreditoByCodFact( String cod_fact, int cod_documebto )
    {
        String FIND_ALL = "SELECT * FROM notas WHERE ref_cod_fact = '" + cod_fact + "' AND fk_documento = " + cod_documebto;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<Notas> lista_nota = new ArrayList<>();
        Notas nota;
        try
        {
            while ( result.next() )
            {
                nota = new Notas();
                lista_nota.add( getNotaResultSet( nota, result ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_nota;
    }

    private Notas getNotaResultSet( Notas nota, ResultSet result )
    {
        try
        {
            nota.setPkNota( result.getInt( "pk_nota" ) );
            nota.setTipoNota( result.getString( "tipo_nota" ) );
            nota.setCodNota( result.getString( "cod_nota" ) );
            nota.setRefCodNota( result.getString( "ref_cod_nota" ) );
            nota.setDataNota( result.getDate( "dataNota" ) );
            nota.setPerformance( result.getString( "performance" ) );
            nota.setCredito( result.getString( "credito" ) );
            nota.setValorEntregue( result.getDouble( "valor_entregue" ) );
            nota.setTroco( result.getDouble( "troco" ) );
            nota.setHora( result.getTime( "hora" ) );
            nota.setNomeCliente( result.getString( "nome_cliente" ) );
            nota.setStatusEliminado( result.getString( "status_eliminado" ) );
            nota.setDescontoTotal( result.getDouble( "desconto_total" ) );
            nota.setTotalIva( result.getDouble( "total_iva" ) );
            nota.setTotalGeral( result.getDouble( "total_geral" ) );
            nota.setCodFact( result.getString( "cod_fact" ) );
            nota.setAssinatura( result.getString( "assinatura" ) );
            nota.setHashCod( result.getString( "hash_cod" ) );
            nota.setObs( result.getString( "obs" ) );
            nota.setRefCodFact( result.getString( "ref_cod_fact" ) );
            nota.setTotalPorExtenso( result.getString( "total_por_extenso" ) );
            nota.setDescontoComercial( result.getDouble( "desconto_comercial" ) );
            nota.setTotalIncidencia( result.getDouble( "total_incidencia" ) );
            nota.setLocalCarga( result.getString( "local_carga" ) );
            nota.setLocalDescarga( result.getString( "local_descarga" ) );
            nota.setCodigoUsuario( new TbUsuario( result.getInt( "codigo_usuario" ) ) );
            nota.setCodigoCliente( new TbCliente( result.getInt( "codigo_cliente" ) ) );
            nota.setIdArmazemFK( new TbArmazem( result.getInt( "idArmazemFK" ) ) );
            nota.setFkDocumento( new Documento( result.getInt( "fk_documento" ) ) );
            nota.setFkAnoEconomico( new AnoEconomico( result.getInt( "fk_ano_economico" ) ) );
            nota.setFkCambio( new Cambio( result.getInt( "fk_cambio" ) ) );
            nota.setClienteNif( result.getString( "cliente_nif" ) );
            nota.setTotalIncidenciaIsento( result.getDouble( "total_incidencia_isento" ) );
            nota.setTotalVenda( new BigDecimal(result.getDouble( "total_venda" ) ));
            nota.setMotivo( result.getString( "motivo" ) );

        }
        catch ( Exception e )
        {
        }

        return nota;
    }

    public static boolean actualizar_hash( List<Notas> list, BDConexao conexao )
    {

        for ( int i = 0; i < list.size(); i++ )
        {
            Notas notas = list.get( i );

            double totalGross = MetodosUtil.getGetGrossTotalNotas( notas.getNotasItemList() );
            System.out.println( "Invoice No: " + notas.getCodNota() );
            String hash = MetodosUtil.criptografia_hash( notas, totalGross, conexao );

            String sql = "UPDATE notas set hash_cod = '" + hash + "' WHERE pk_nota = " + notas.getPkNota();

            conexao.executeUpdate( sql );

        }

        return true;
    }

}
