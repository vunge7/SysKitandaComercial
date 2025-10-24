/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import dao.VendaDao;
import entity.Compras;
import entity.Documento;
import entity.ExtratoContaFornecedor;
import entity.ExtratoContaFornecedor;
import entity.Notas;
import entity.TbCliente;
import entity.TbFornecedor;
import entity.TbUsuario;
import entity.TbVenda;
import comercial.controller.FornecedoresController;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class ExtratoContaFornecedorController implements EntidadeFactory
{

    private static BDConexao conexao;

    public ExtratoContaFornecedorController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ExtratoContaFornecedor ecf = ( ExtratoContaFornecedor ) object;
        String INSERT = "INSERT INTO extrato_conta_fornecedor ( "
                + " documento,referencia ,descricao , data_hora,  saldo_anterior, debito, credito, saldo_actual , "
                + " tipo_extrato, fonecedor_nome, fornecedor_id, user_name, user_id"
                + ")"
                + " VALUES("
                + "'" + ecf.getDocumento() + "' , "
                + "'" + ecf.getReferencia() + "' , "
                + "'" + ecf.getDescricao() + "' , "
                //                + "'" + MetodosUtil.getDataBancoFull( ecCliente.getDataHora() ) + "', "
                + "'" + MetodosUtil.getDataBanco( ecf.getDataHora() ) + "', "
                + ecf.getSaldoAnterior() + ", "
                + ecf.getDebito() + ", "
                + ecf.getCredito() + ", "
                + ecf.getSaldoActual() + ", "
                + "'" + ecf.getTipoExtrato() + "' , "
                + "'" + ecf.getFonecedorNome() + "' , "
                + ecf.getFornecedorId() + ", "
                + "'" + ecf.getUserName() + "' , "
                + ecf.getUserId()
                + " ) ";

        System.out.println( INSERT );

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
    public List<ExtratoContaFornecedor> listarTodos()
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM extrato_conta_fornecedor ORDER BY pk_extrato_conta_fornecedor ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ExtratoContaFornecedor> lista_extrato_conta_fornecedor = new ArrayList<>();
        ExtratoContaFornecedor extrato_conta_fornecedor;
        try
        {

            while ( result.next() )
            {
                extrato_conta_fornecedor = new ExtratoContaFornecedor();
                lista_extrato_conta_fornecedor.add( getExtratoAlojamentoByResultSet( extrato_conta_fornecedor, result ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_extrato_conta_fornecedor;
    }

    public List<ExtratoContaFornecedor> listarTodosByData1AndData2( int idFornecedor, Date data_1, Date data_2 )
    {
        System.out.println( "cheguei aqui." );
        String FIND_ALL = "SELECT * FROM extrato_conta_fornecedor WHERE   fornecedor_id =   " + idFornecedor + "  AND   DATE(data_hora) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "' ";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ExtratoContaFornecedor> lista_extrato_conta_fornecedor = new ArrayList<>();
        ExtratoContaFornecedor extrato_conta_fornecedor;
        try
        {

            while ( result.next() )
            {
                extrato_conta_fornecedor = new ExtratoContaFornecedor();
                lista_extrato_conta_fornecedor.add( getExtratoAlojamentoByResultSet( extrato_conta_fornecedor, result ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_extrato_conta_fornecedor;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM extrato_conta_fornecedor WHERE pk_extrato_conta_fornecedor = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ExtratoContaFornecedor extrato_conta_fornecedor = null;

        try
        {
            if ( result.next() )
            {
                return getExtratoAlojamentoByResultSet( extrato_conta_fornecedor, result );
            }

        }
        catch ( SQLException e )
        {
        }

        return extrato_conta_fornecedor;

    }

    public ExtratoContaFornecedor getExtratoContaFornecedor( long codigo )
    {

        String FIND_BY_CODIGO = "SELECT * FROM extrato_conta_fornecedor WHERE pk_extrato_conta_fornecedor = " + codigo;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ExtratoContaFornecedor extrato_conta_fornecedor = new ExtratoContaFornecedor();

        try
        {
            if ( result.next() )
            {
                extrato_conta_fornecedor = getExtratoAlojamentoByResultSet( extrato_conta_fornecedor, result );
            }

        }
        catch ( SQLException e )
        {
        }

        return extrato_conta_fornecedor;

    }

    private ExtratoContaFornecedor getExtratoAlojamentoByResultSet( ExtratoContaFornecedor extrato_conta_fornecedor, ResultSet result )
    {
        try
        {
            extrato_conta_fornecedor.setPkExtratoContaFornecedor( result.getLong( "pk_extrato_conta_fornecedor" ) );
            extrato_conta_fornecedor.setDocumento( result.getString( "documento" ) );
            extrato_conta_fornecedor.setReferencia( result.getString( "referencia" ) );
            extrato_conta_fornecedor.setDescricao( result.getString( "descricao" ) );
            extrato_conta_fornecedor.setDataHora( result.getDate( "data_hora" ) );
            extrato_conta_fornecedor.setSaldoAnterior( result.getBigDecimal( "saldo_anterior" ) );
            extrato_conta_fornecedor.setDebito( result.getBigDecimal( "debito" ) );
            extrato_conta_fornecedor.setCredito( result.getBigDecimal( "credito" ) );
            extrato_conta_fornecedor.setSaldoActual( result.getBigDecimal( "saldo_actual" ) );
            extrato_conta_fornecedor.setTipoExtrato( result.getString( "tipo_extrato" ) );
            extrato_conta_fornecedor.setFonecedorNome( result.getString( "fonecedor_nome" ) );
            extrato_conta_fornecedor.setFornecedorId( result.getInt( "fornecedor_id" ) );
            extrato_conta_fornecedor.setUserName( result.getString( "user_name" ) );
            extrato_conta_fornecedor.setUserId( result.getInt( "user_id" ) );

        }
        catch ( Exception e )
        {
        }

        return extrato_conta_fornecedor;
    }

    public BigDecimal getSaldoActualByIdFornecedor( int id_fornecedor )
    {
        ExtratoContaFornecedor extrato = getLastExtratoContaFornecedorByIdCliente( id_fornecedor );
        if ( !Objects.isNull( extrato.getPkExtratoContaFornecedor() ) )
        {
            System.out.println( "SALDO ACTUAL " + extrato.getSaldoActual().doubleValue() );
            return extrato.getSaldoActual();
        }
        return new BigDecimal( "0.00" );

    }

    public ExtratoContaFornecedor getLastExtratoContaFornecedorByIdCliente( int id_cliente )
    {

        String FIND_LAST_BY_ID_CLIENTE = "SELECT MAX(pk_extrato_conta_fornecedor) AS maximo_id  FROM extrato_conta_fornecedor WHERE fornecedor_id = " + id_cliente;
        ResultSet result = conexao.executeQuery( FIND_LAST_BY_ID_CLIENTE );
        ExtratoContaFornecedor extrato_conta_fornecedor = new ExtratoContaFornecedor();
        try
        {

            if ( result.next() )
            {
                long pk_extrato_conta_fornecedor = result.getLong( "maximo_id" );
                extrato_conta_fornecedor = getExtratoContaFornecedor( pk_extrato_conta_fornecedor );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return extrato_conta_fornecedor;

    }

    public static ExtratoContaFornecedor preparExtratoConta()
    {
        return null;
    }

    public static void main( String[] args )
    {
//        List<Notas> listarAllNotaCreditoByCodFact = new NotasController( BDConexao.getInstancia() ).listarAllNotaCreditoByCodFact( "FT 2021/38", DVML.DOC_NOTA_CREDITO_NC );
//
//        System.out.println( "Cod Nota: " + listarAllNotaCreditoByCodFact.get( 0 ).getCodNota() );
        limpar_dados( BDConexao.getInstancia() );
    }

    public static boolean registro_movimento_conta_fornecedor( Object object, BDConexao conexao )
    {

        if ( object instanceof Compras )
        {
            Compras compra = ( Compras ) object;
            try
            {
                System.err.println( "ENTREI AQUI NO EXTRATO......" );
                ExtratoContaFornecedorController extratoController = new ExtratoContaFornecedorController( conexao );
                ExtratoContaFornecedor extratoContaFornecedor = new ExtratoContaFornecedor();

                TbFornecedor fornecedor = ( TbFornecedor ) new FornecedoresController( conexao ).findById( compra.getFkFornecedor().getCodigo() );
                TbUsuario usuario = ( TbUsuario ) new UsuariosController( conexao ).findById( compra.getCodigoUsuario().getCodigo() );
                Documento documento_object = new DocumentosController( conexao ).findDocumentoById( compra.getFkDocumento().getPkDocumento() );

                int fornecedor_id = fornecedor.getCodigo();
                String documento = compra.getDocVosso();
                String referencia = compra.getDocVossoNumero();
                String descricao = "";
                Date data_hora = new Date();

//                String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( data_hora );
                System.out.println( "Date Venda: " + data_hora.toString() );
                BigDecimal saldo_anterior = extratoController.getSaldoActualByIdFornecedor( fornecedor_id ); // o saldo anterior é o saldo actual anterior ao movimento
                BigDecimal debito;
                BigDecimal credito;
                BigDecimal saldo_actual = new BigDecimal( 0.00 );
                String tipo_extrato = ( documento.equals( "V/Factura" ) ? "CR" : "DB" );
                String fornecedor_nome = fornecedor.getNome();
                String user_name = usuario.getNome();
                int user_id = usuario.getCodigo();
                System.out.println( "TIPO EXTRATO: "+tipo_extrato );

                if ( documento.equals( "V/Factura" ) )
                {
                    credito = new BigDecimal( compra.getTotalCompra() ).setScale( 2, RoundingMode.CEILING );
                    debito = new BigDecimal( 0.00 );
                    saldo_actual = saldo_anterior.add( credito );
                    extratoContaFornecedor.setSaldoActual( saldo_actual.setScale( 2, RoundingMode.CEILING ) );

                }
                else if ( documento.equals( "V/Factura-Recibo" ) )
                {
                    System.err.println( "CHEGUEI AQUI NA V/FR" );
                    
                    debito = new BigDecimal( 0d ).setScale( 2, RoundingMode.CEILING );
                    credito = new BigDecimal( 0.00 );
                    saldo_actual = saldo_anterior.subtract( debito );
                    extratoContaFornecedor.setSaldoActual( saldo_actual.setScale( 2, RoundingMode.CEILING ) );
                }
                else
                {
                    debito = new BigDecimal( 0.0 );
                    credito = new BigDecimal( 0.0 );
                }

                extratoContaFornecedor.setDocumento( documento );
                extratoContaFornecedor.setReferencia( referencia );
                extratoContaFornecedor.setDescricao( descricao );
                extratoContaFornecedor.setDataHora( data_hora );
                extratoContaFornecedor.setSaldoAnterior( saldo_anterior );
                extratoContaFornecedor.setDebito( debito );
                extratoContaFornecedor.setCredito( credito );
                extratoContaFornecedor.setSaldoActual( saldo_actual );
                extratoContaFornecedor.setTipoExtrato( tipo_extrato );
                extratoContaFornecedor.setFonecedorNome( fornecedor_nome );
                extratoContaFornecedor.setFornecedorId( fornecedor_id );
                extratoContaFornecedor.setUserName( user_name );
                extratoContaFornecedor.setUserId( user_id );

                extratoController.salvar( extratoContaFornecedor );
                return true;

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        else if ( object instanceof Object)
        {
            
        }
        return false;

    }

    public static boolean limpar_dados( BDConexao conexao )
    {
        String sql = "TRUNCATE extrato_conta_fornecedor";
        return conexao.executeUpdate( sql );

    }

}
