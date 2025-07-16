/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.controller;

import hotel.controller.*;
import dao.VendaDao;
import entity.Documento;
import entity.ExtratoContaCliente;
import entity.ExtratoContaCliente;
import entity.Notas;
import entity.TbCliente;
import entity.TbUsuario;
import entity.TbVenda;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JOptionPane;
import kitanda.util.CfConstantes;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ExtratoContaClienteController implements EntidadeFactory
{

    private static BDConexao conexao;

    public ExtratoContaClienteController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ExtratoContaCliente ecCliente = ( ExtratoContaCliente ) object;
        String INSERT = "INSERT INTO extrato_conta_cliente ( "
                + " documento,referencia ,descricao , data_hora,  saldo_anterior, debito, credito, saldo_actual , "
                + " tipo_extrato, cliente_nome, cliente_id, user_name, usuario_id"
                + ")"
                + " VALUES("
                + "'" + ecCliente.getDocumento() + "' , "
                + "'" + ecCliente.getReferencia() + "' , "
                + "'" + ecCliente.getDescricao() + "' , "
                //                + "'" + MetodosUtil.getDataBancoFull( ecCliente.getDataHora() ) + "', "
                + "'" + MetodosUtil.getDataBanco( ecCliente.getDataHora() ) + "', "
                + ecCliente.getSaldoAnterior() + ", "
                + ecCliente.getDebito() + ", "
                + ecCliente.getCredito() + ", "
                + ecCliente.getSaldoActual() + ", "
                + "'" + ecCliente.getTipoExtrato() + "' , "
                + "'" + ecCliente.getClienteNome() + "' , "
                + ecCliente.getClienteId() + ", "
                + "'" + ecCliente.getUserName() + "' , "
                + ecCliente.getUsuarioId()
                + " ) ";

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
        return true;
    }

    @Override
    public List<ExtratoContaCliente> listarTodos()
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM extrato_conta_cliente ORDER BY pk_extrato_conta_cliente ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ExtratoContaCliente> lista_extrato_conta_cliente = new ArrayList<>();
        ExtratoContaCliente extrato_conta_cliente;
        try
        {

            while ( result.next() )
            {
                extrato_conta_cliente = new ExtratoContaCliente();
                lista_extrato_conta_cliente.add( getExtratoAlojamentoByResultSet( extrato_conta_cliente, result ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_extrato_conta_cliente;
    }

    public List<ExtratoContaCliente> listarTodosByData1AndData2( Date data_1, Date data_2 )
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM extrato_conta_cliente WHERE DATE(data_hora) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + " ORDER BY pk_extrato_conta_cliente ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ExtratoContaCliente> lista_extrato_conta_cliente = new ArrayList<>();
        ExtratoContaCliente extrato_conta_cliente;
        try
        {

            while ( result.next() )
            {
                extrato_conta_cliente = new ExtratoContaCliente();
                lista_extrato_conta_cliente.add( getExtratoAlojamentoByResultSet( extrato_conta_cliente, result ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_extrato_conta_cliente;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM extrato_conta_cliente WHERE pk_extrato_conta_cliente = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ExtratoContaCliente extrato_conta_cliente = null;

        try
        {
            if ( result.next() )
            {
                return getExtratoAlojamentoByResultSet( extrato_conta_cliente, result );
            }

        }
        catch ( SQLException e )
        {
        }

        return extrato_conta_cliente;

    }

    public ExtratoContaCliente getExtratoContaCliente( long codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM extrato_conta_cliente WHERE pk_extrato_conta_cliente = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ExtratoContaCliente extrato_conta_cliente = new ExtratoContaCliente();

        try
        {
            if ( result.next() )
            {
                extrato_conta_cliente = getExtratoAlojamentoByResultSet( extrato_conta_cliente, result );
            }

        }
        catch ( SQLException e )
        {
        }

        return extrato_conta_cliente;

    }

    private ExtratoContaCliente getExtratoAlojamentoByResultSet( ExtratoContaCliente extrato_conta_cliente, ResultSet result )
    {
        try
        {
            extrato_conta_cliente.setPkExtratoContaCliente( result.getLong( "pk_extrato_conta_cliente" ) );
            extrato_conta_cliente.setDocumento( result.getString( "documento" ) );
            extrato_conta_cliente.setReferencia( result.getString( "referencia" ) );
            extrato_conta_cliente.setDescricao( result.getString( "descricao" ) );
            extrato_conta_cliente.setDataHora( result.getDate( "data_hora" ) );
            extrato_conta_cliente.setSaldoAnterior( result.getBigDecimal( "saldo_anterior" ) );
            extrato_conta_cliente.setDebito( result.getBigDecimal( "debito" ) );
            extrato_conta_cliente.setCredito( result.getBigDecimal( "credito" ) );
            extrato_conta_cliente.setSaldoActual( result.getBigDecimal( "saldo_actual" ) );
            extrato_conta_cliente.setTipoExtrato( result.getString( "tipo_extrato" ) );
            extrato_conta_cliente.setClienteNome( result.getString( "cliente_nome" ) );
            extrato_conta_cliente.setClienteId( result.getInt( "cliente_id" ) );
            extrato_conta_cliente.setUserName( result.getString( "user_name" ) );
            extrato_conta_cliente.setUsuarioId( result.getInt( "usuario_id" ) );

        }
        catch ( Exception e )
        {
        }

        return extrato_conta_cliente;
    }

    public BigDecimal getSaldoActualByIdCliente( int id_cliente )
    {
        ExtratoContaCliente extrato = getLastExtratoContaClienteByIdCliente( id_cliente );
        if ( !Objects.isNull( extrato.getPkExtratoContaCliente() ) )
        {
            System.out.println( "SALDO ACTUAL " + extrato.getSaldoActual().doubleValue() );
            return extrato.getSaldoActual();
        }
        return new BigDecimal( "0.00" );

    }

    public ExtratoContaCliente getLastExtratoContaClienteByIdCliente( int id_cliente )
    {

        String FIND_LAST_BY_ID_CLIENTE = "SELECT MAX(pk_extrato_conta_cliente) AS maximo_id  FROM extrato_conta_cliente WHERE cliente_id = " + id_cliente;
        ResultSet result = conexao.executeQuery( FIND_LAST_BY_ID_CLIENTE );
        ExtratoContaCliente extrato_conta_cliente = new ExtratoContaCliente();
        try
        {

            if ( result.next() )
            {
                long pk_extrato_conta_cliente = result.getLong( "maximo_id" );
                extrato_conta_cliente = getExtratoContaCliente( pk_extrato_conta_cliente );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return extrato_conta_cliente;

    }

    public static ExtratoContaCliente preparExtratoConta()
    {
        return null;
    }

    public static void main( String[] args )
    {
//        List<Notas> listarAllNotaCreditoByCodFact = new NotasController( new BDConexao() ).listarAllNotaCreditoByCodFact( "FT 2021/38", DVML.DOC_NOTA_CREDITO_NC );
//
//        System.out.println( "Cod Nota: " + listarAllNotaCreditoByCodFact.get( 0 ).getCodNota() );
        limpar_dados( new BDConexao() );
    }

    public static boolean registro_movimento_conta_cliente( Object object, BDConexao conexao )
    {

        if ( object instanceof TbVenda )
        {
            TbVenda venda = ( TbVenda ) object;
            try
            {
                ExtratoContaClienteController extratoController = new ExtratoContaClienteController( conexao );
                ExtratoContaCliente extratoContaCliente = new ExtratoContaCliente();

                TbCliente cliente = ( TbCliente ) new ClienteController( conexao ).findById( venda.getCodigoCliente().getCodigo() );
                TbUsuario usuario = ( TbUsuario ) new UsuarioController( conexao ).findById( venda.getCodigoUsuario().getCodigo() );
                Documento documento_object = new DocumentoController( conexao ).findDocumentoById( venda.getFkDocumento().getPkDocumento() );

                int cliente_id = cliente.getCodigo();
                String documento = documento_object.getDesignacao();

                String referencia = venda.getCodFact();
                String descricao = "";
                Date data_hora = venda.getDataVenda();

//                String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( data_hora );
                System.out.println( "Date Venda: " + data_hora.toString() );
                BigDecimal saldo_anterior = extratoController.getSaldoActualByIdCliente( cliente_id ); // o saldo anterior é o saldo actual anterior ao movimento
                BigDecimal debito;
                BigDecimal credito;
                BigDecimal saldo_actual = new BigDecimal( 0.00 );
                String tipo_extrato = ( documento.equals( "Factura" ) ? "DB" : "CR" );
                String cliente_nome = cliente.getNome();
                String user_name = usuario.getNome();
                int usuario_id = usuario.getCodigo();

                if ( documento.equals( "Factura" ) )
                { 
                    debito = new BigDecimal( venda.getTotalVenda().doubleValue() ).setScale( 2, RoundingMode.CEILING );
                    credito = new BigDecimal( 0.00 );
                    saldo_actual = saldo_anterior.add( debito );
                    extratoContaCliente.setSaldoActual( saldo_actual.setScale( 2, RoundingMode.CEILING ) );

                }
                else if ( documento.equals( "Recibo" ) )
                {
                    credito = new BigDecimal( venda.getValorEntregue().doubleValue() ).setScale( 2, RoundingMode.CEILING );
                    debito = new BigDecimal( 0.00 );
                    saldo_actual = saldo_anterior.subtract( credito );
                    extratoContaCliente.setSaldoActual( saldo_actual.setScale( 2, RoundingMode.CEILING ) );
                }
                else
                {
                    debito = new BigDecimal( 0.0 );
                    credito = new BigDecimal( 0.0 );
                }

                extratoContaCliente.setDocumento( documento );
                extratoContaCliente.setReferencia( referencia );
                extratoContaCliente.setDescricao( descricao );
                extratoContaCliente.setDataHora( data_hora );
                extratoContaCliente.setSaldoAnterior( saldo_anterior );
                extratoContaCliente.setDebito( debito );
                extratoContaCliente.setCredito( credito );
                extratoContaCliente.setSaldoActual( saldo_actual );
                extratoContaCliente.setTipoExtrato( tipo_extrato );
                extratoContaCliente.setClienteNome( cliente_nome );
                extratoContaCliente.setClienteId( cliente_id );
                extratoContaCliente.setUserName( user_name );
                extratoContaCliente.setUsuarioId( usuario_id );

                extratoController.salvar( extratoContaCliente );
                return true;

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        else if ( object instanceof Notas )
        {
            Notas notas = ( Notas ) object;
            try
            {
                ExtratoContaClienteController extratoController = new ExtratoContaClienteController( conexao );
                ExtratoContaCliente extratoContaCliente = new ExtratoContaCliente();

                TbCliente cliente = ( TbCliente ) new ClienteController( conexao ).findById( notas.getCodigoCliente().getCodigo() );
                TbUsuario usuario = ( TbUsuario ) new UsuarioController( conexao ).findById( notas.getCodigoUsuario().getCodigo() );
                Documento documento_object = new DocumentoController( conexao ).findDocumentoById( notas.getFkDocumento().getPkDocumento() );

                int cliente_id = cliente.getCodigo();
                String documento = documento_object.getDesignacao();
                String referencia = notas.getCodNota();
                String descricao = notas.getMotivo() + " (" + notas.getRefCodFact() + ")";
                Date data_hora = notas.getDataNota();
                BigDecimal saldo_anterior = extratoController.getSaldoActualByIdCliente( cliente_id ); // o saldo anterior é o saldo actual anterior ao movimento
                BigDecimal debito;
                BigDecimal credito;
                BigDecimal saldo_actual = new BigDecimal( 0.00 );
                String tipo_extrato = ( "CR" );
                String cliente_nome = cliente.getNome();
                String user_name = usuario.getNome();
                int usuario_id = usuario.getCodigo();

                credito = VendaDao.getTotalVendaByCodFact( notas.getRefCodFact(), conexao ).setScale( 2, RoundingMode.CEILING );
                debito = new BigDecimal( 0.00 );
                saldo_actual = saldo_anterior.subtract( credito );
                extratoContaCliente.setSaldoActual( saldo_actual.setScale( 2, RoundingMode.CEILING ) );

                extratoContaCliente.setDocumento( documento );
                extratoContaCliente.setReferencia( referencia );
                extratoContaCliente.setDescricao( descricao );
                extratoContaCliente.setDataHora( data_hora );
                extratoContaCliente.setSaldoAnterior( saldo_anterior );
                extratoContaCliente.setDebito( debito );
                extratoContaCliente.setCredito( credito );
                extratoContaCliente.setSaldoActual( saldo_actual );
                extratoContaCliente.setTipoExtrato( tipo_extrato );
                extratoContaCliente.setClienteNome( cliente_nome );
                extratoContaCliente.setClienteId( cliente_id );
                extratoContaCliente.setUserName( user_name );
                extratoContaCliente.setUsuarioId( usuario_id );

                extratoController.salvar( extratoContaCliente );
                return true;

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        return false;

    }

    public static boolean limpar_dados( BDConexao conexao )
    {
        String sql = "TRUNCATE extrato_conta_cliente";
        return conexao.executeUpdate( sql );

    }

    public static boolean gerarExtratoAutomatico( Date data_1, Date data_2 )
    {
        VendaController vendaController = new VendaController( conexao );
        NotasController notasController = new NotasController( conexao );
        List<TbVenda> lista_factura = vendaController.listarTodosByData1AndData2AndTipoDocumento( data_1, data_2, DVML.DOC_FACTURA_FT );
        List<TbVenda> lista_recibo;
        List<Notas> lista_nota;
        int cont = 1, tamanho = lista_factura.size();

        limpar_dados( conexao );
        //verifica se existe factura dentro do intervalo
        if ( !Objects.isNull( lista_factura ) )
        {
            for ( TbVenda factura : lista_factura )
            {
                System.out.println( cont + " de " + tamanho );
                cont++;
                //adiciona a factura no extrato
                registro_movimento_conta_cliente( factura, conexao );
                //busca todas os recibos desta factura
                lista_recibo = vendaController.listarAllReciboByCodFact( factura.getCodFact() );
                //verifica se existem recibos desta factura
                if ( !Objects.isNull( lista_recibo ) )
                {
                    for ( TbVenda recibo : lista_recibo )
                    {
                        //registra o recibo no extrato
                        registro_movimento_conta_cliente( recibo, conexao );
                        //verifica se existe nota de credito desta recibo
                        lista_nota = notasController.listarAllNotaCreditoByCodFact( recibo.getCodFact(), DVML.DOC_NOTA_CREDITO_NC );
                        if ( !Objects.isNull( lista_nota ) )
                        {
                            for ( Notas notas : lista_nota )
                            {
                                registro_movimento_conta_cliente( notas, conexao );
                            }
                        }
                    }
                }

                /**
                 * Caso a factura ter uma nota de credito ou seja anulada.
                 */
                lista_nota = notasController.listarAllNotaCreditoByCodFact( factura.getCodFact(), DVML.DOC_NOTA_CREDITO_NC );

                if ( !Objects.isNull( lista_nota ) )
                {

                    for ( Notas notas : lista_nota )
                    {
                        System.out.println( "Cheguei aqui na nota de credito da factura: " + notas.getRefCodFact() );
                        registro_movimento_conta_cliente( notas, conexao );
                    }
                }

            }
            JOptionPane.showMessageDialog( null, "Operacao efectuada com sucesso!..." );

        }

        return true;
    }

}
