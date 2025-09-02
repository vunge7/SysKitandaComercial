/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.Connection;
import comercial.controller.AmortizacaoDividasController;
import comercial.controller.ArmazensAccessoController;
import comercial.controller.ArmazensController;
import comercial.controller.CaixasController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.ItemCaixaController;
import comercial.controller.LogController;
import comercial.controller.PrecosController;
import comercial.controller.UsuariosController;
import comercial.controller.VendasController;
//import com.sun.media.sound.InvalidFormatException;
import controlador.DbBackupScheduleJpaController;
import dao.*;
import entity.AmortizacaoDivida;
import entity.AnoEconomico;
import entity.Caixa;
import entity.Compras;
import entity.DbBackupSchedule;
import entity.Documento;
import entity.Empresa;
import entity.FechoPeriodo;
import entity.FormaPagamento;
import entity.ItemCaixa;
import entity.ItemCompras;
import entity.Notas;
import entity.NotasItem;
import entity.TbArmazem;
import entity.TbItemSubsidiosFuncionario;
import entity.TbPreco;
import entity.Promocao;
import entity.TbDadosInstituicao;
import entity.TbFalta;
import entity.TbFuncionario;
import entity.TbItemVenda;
import entity.TbProduto;
import entity.TbStock;
import entity.TbStockMirrow;
import entity.TbUsuario;
import entity.TbValidade;
import entity.TbVenda;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
//import javax.swing.text.Document;
//import org.apache.poi.xwpf.usermodel.Document;
import javax.swing.text.NumberFormatter;
import kitanda.util.CfConstantes;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import lista.NLExporToPdfForSandEmailReport;
import lista.ReconciliacaoCaixaReport;
import modelo.Log;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rh.visao.RecibosVisao;
import static rh.visao.RecibosVisao.getIdAno;
import static rh.visao.RecibosVisao.getIdPeriodo;
import tesouraria.novo.util.AnyReport;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import static util.DVML.*;
import static util.JPAEntityMannagerFactoryUtil.em;
import visao.BDBackupJFrame;
import visao.CompraVisao;
import visao.LoginVisao;
import visao.RootVisao;
import java.math.BigDecimal;
import java.math.RoundingMode;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MetodosUtil
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ValidadeDao validadeDao = new ValidadeDao( emf );
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
    private static final FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private static final FechoPeriodoDao fechoPeriodoDao = new FechoPeriodoDao( emf );
    private static StockMirrowDao stockMirrowDao = new StockMirrowDao( emf );
    private static StockDao stockDao = new StockDao( emf );
    private static final FaltaDao faltaDao = new FaltaDao( emf );
//    private static BancoDao bancoDao = new BancoDao( emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao( emf );
    private static ProdutoImpostoDao produtoImpostoDao = new ProdutoImpostoDao( emf );
    private static ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao( emf );
    private static List<String> lista_alienas = new ArrayList<>();

    private static String criptografia_hash_venda( TbVenda venda, double parmGrossTotal, BDConexao conexao )
    {

        String final_hash = "";
        try
        {
            System.out.println( "Cheguei no invoice date" );
            String invoiceDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD ).format( venda.getDataVenda() );
            String systemEntryDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD_T_HH_MM_SS ).format( venda.getDataVenda() );
            String invoiceNo = venda.getCodFact();
            System.err.println( "InvoiceNO: " + invoiceNo );

//            double grossoTotal = venda.getTotalGeral () + getTotalVendaIVASemIncluirDesconto ( venda.getTbItemVendaList () );
            String grossoTotal = getValorCasasDecimais( parmGrossTotal, CASAS_DECIMAIS );
            System.err.println( "Venda Codigo Antes: " + venda.getCodigo() );
            int cod_venda = ( Objects.isNull( venda.getCodigo() ) || venda.getCodigo() == 0 ) ? VendaDao.getLastVendaByTipoDocumento( venda.getFkDocumento().getPkDocumento(), conexao ) + 1 : venda.getCodigo();
            System.err.println( "Venda Codigo Depois: " + venda.getCodigo() );
//            String hash = new VendaDao( emf ).getHashAnterior( cod_venda, invoiceNo, venda.getFkDocumento().getPkDocumento(), venda.getFkAnoEconomico().getPkAnoEconomico(), conexao ).trim();
            String hash = new VendaDao( emf ).getHashAnterior2( cod_venda, invoiceNo, venda.getFkDocumento().getPkDocumento(), venda.getFkAnoEconomico().getPkAnoEconomico(), conexao ).trim();
            System.out.println( "COD HASH ANTERIOR: " + hash );

            String mensagem = String.format( "%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );

            System.out.println( "Parm GrossTotal: " + parmGrossTotal );
            System.out.println( "grossTotal na mensagem: " + grossoTotal );
            System.err.println( "CodFact Anterior: " + hash );
            System.err.println( "HashAnterior: " + hash );
            System.err.println( "Mensagem: " + mensagem );

            //1º PASSO: Guardar a mensagem a assinar
            File file = new File( PATH + "Registo.txt" );
            MetodosUtil.escreverNoDocumento( mensagem, file, "" );

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC( assinatura, true );

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC( encoding, true );

            Scanner scanner = new Scanner( new File( PATH + "Registo.b64" ) );

            if ( scanner.hasNext() )
            {
                final_hash = scanner.nextLine();
            }

        }
        catch ( FileNotFoundException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return final_hash;
    }

//    public static void inserir_imagem_docx( String texto, String ficheiro_docx, String ficheiro_imagem ) throws FileNotFoundException, IOException, InvalidFormatException
//    {
//
//        CustomXWPFDocument document = new CustomXWPFDocument();
//        XWPFParagraph paragraph = document.createParagraph();
//        XWPFRun run = paragraph.createRun();
//        run.setText( texto );
//        run.setFontSize( 13 );
//        run.addBreak();
//        run.addBreak();
//        FileOutputStream fos = new FileOutputStream( new File( ficheiro_docx ) );
//        try
//        {
//            String id = document.addPictureData( new FileInputStream( new File( ficheiro_imagem ) ), Document.PICTURE_TYPE_PNG );
//            document.createPicture( id, document.getNextPicNameNumber( Document.PICTURE_TYPE_PNG ), 600, 600 );
//        }
//        catch ( Exception e )
//        {
//        }
//        document.write( fos );
//        fos.flush();
//        fos.close();
//
//    }
//    public static BigDecimal getGrossTotalCompra( List<ItemCompras> list )
//    {
//        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
//        BigDecimal qtd;
//
//        total_iva = new BigDecimal( 0 );
//        for ( ItemCompras linha : list )
//        {
//            if ( linha.getValorIva().doubleValue() > 0 )
//            {
//                preco = new BigDecimal( linha.getPrecoCompra() );
//                qtd = new BigDecimal( linha.getQuantidade() );
//                sub_total_iliquido = preco.multiply( qtd );
//                BigDecimal valorIvaLinha = new BigDecimal( linha.getValorIva().doubleValue() );
////                taxa = ( linha.getValorIva() / 100 );
//                taxa = valorIvaLinha.divide( new BigDecimal( 100 ) );
////                total_iva  =   total_iva +  sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
//                BigDecimal valorImposto = sub_total_iliquido.multiply( taxa );
//                total_iva = total_iva.add( sub_total_iliquido.add( valorImposto ) );
//            }
//            else
//            {
//                preco = new BigDecimal( linha.getPrecoCompra() );
//                qtd = new BigDecimal( linha.getQuantidade() );
//                sub_total_iliquido = preco.multiply( qtd );
//                total_iva = total_iva.add( sub_total_iliquido );
//
//            }
//
//        }
//
//        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
//        return total_iva;
//    }
    private static String criptografia_hash_venda2( TbVenda venda, double parmGrossTotal, BDConexao conexao )
    {

        String final_hash = "";
        try
        {
            String invoiceDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD ).format( venda.getDataVenda() );
            String systemEntryDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD_T_HH_MM_SS ).format( venda.getDataVenda() );
            String invoiceNo = venda.getCodFact();
            System.err.println( "InvoiceNO: " + invoiceNo );
//            double grossoTotal = venda.getTotalGeral () + getTotalVendaIVASemIncluirDesconto ( venda.getTbItemVendaList () );
            String grossoTotal = getValorCasasDecimais( parmGrossTotal, CASAS_DECIMAIS );
//            String hash = new VendaDao( emf ).getHashAnterior( invoiceNo, venda.getFkDocumento().getPkDocumento(), venda.getFkAnoEconomico().getPkAnoEconomico(), conexao ).trim();

            System.err.println( "InvoiceNO: " + invoiceNo );

            int cod_venda = ( Objects.isNull( venda.getCodigo() ) || venda.getCodigo() == 0 ) ? VendaDao.getLastVendaByTipoDocumento( venda.getFkDocumento().getPkDocumento(), conexao ) + 1 : venda.getCodigo();
            String hash = new VendaDao( emf ).getHashAnterior( cod_venda, invoiceNo, venda.getFkDocumento().getPkDocumento(), venda.getFkAnoEconomico().getPkAnoEconomico(), conexao ).trim();
            System.out.println( "COD HASH ANTERIOR: " + hash );

            /*
               String mensagem = String.format ( "%s,%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );
            System.err.println ( "MENSAGEM: " +mensagem );
            //1º PASSO: Guardar a mensagem a assinar
            String guardar_mensagem_camando = "cmd /c echo " + mensagem + " > \"C:\\OpenSSL-Win64\\bin\\Registo1.txt\"";

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo1.txt
            String assinar_mensagem_camando = "cmd /c openssl dgst -sha1 -sign \"C:\\OpenSSL-Win64\\bin\\ChavePrivada.txt\" -out \"C:\\OpenSSL-Win64\\bin\\Registo1.sha1\" \"C:\\OpenSSL-Win64\\bin\\Registo1.txt\"";

            //3º PASSO(*): Seguidamente e necessario efetuar o encoding para base 64 do ficheiro Registo1.shal
            String codificar_assinatura_mensagem_camando = "cmd /c openssl enc -base64 -in \"C:\\OpenSSL-Win64\\bin\\Registo1.sha1\" -out \"C:\\OpenSSL-Win64\\bin\\Registo1.b64\" -A";

            rodarComandoWindows ( guardar_mensagem_camando, true );
            rodarComandoWindows ( assinar_mensagem_camando, true );
            rodarComandoWindows ( codificar_assinatura_mensagem_camando, true );

            Scanner scanner = new Scanner ( new File ( "C:\\OpenSSL-Win64\\bin\\Registo1.b64" ) );

            if ( scanner.hasNext () )
            {
                final_hash = scanner.nextLine ();
            }
            
           
            
            
            
        }
        catch ( FileNotFoundException ex )
        {
        }
         
            
       
       return final_hash;
            
             */
            String mensagem = String.format( "%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );

            System.out.println( "Parm GrossTotal: " + parmGrossTotal );
            System.err.println( "HashAnterior: " + hash );
            System.err.println( "Mensagem: " + mensagem );

            //1º PASSO: Guardar a mensagem a assinar
            File file = new File( PATH + "Registo.txt" );
            MetodosUtil.escreverNoDocumento( mensagem, file, "" );

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC( assinatura, true );

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC( encoding, true );

            Scanner scanner = new Scanner( new File( PATH + "Registo.b64" ) );

            if ( scanner.hasNext() )
            {
                final_hash = scanner.nextLine();
            }

        }
        catch ( FileNotFoundException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return final_hash;
    }

    private static String criptografia_hash_nota( Notas notas, double parmGrossTotal, BDConexao conexao )
    {
        String final_hash = "";
        try
        {
            String invoiceDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD ).format( notas.getDataNota() );
            String systemEntryDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD_T_HH_MM_SS ).format( notas.getDataNota() );
            String invoiceNo = notas.getCodNota();
            System.err.println( "InvoiceNO: " + invoiceNo );
//            double grossoTotal = venda.getTotalGeral () + getTotalVendaIVASemIncluirDesconto ( venda.getTbItemVendaList () );
            String grossoTotal = getValorCasasDecimais( parmGrossTotal, CASAS_DECIMAIS );
            int pk_nota = ( Objects.isNull( notas.getPkNota() ) || notas.getPkNota() == 0 ) ? NotasDao.getLastNota( notas.getFkDocumento().getPkDocumento(), conexao ) + 1 : notas.getPkNota();
            String hash = new NotasDao( emf ).getHashAnterior( pk_nota, conexao ).trim();

//            System.err.println( "HashAnterior: " + hash );
//
//            String mensagem = String.format( "%s,%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );
//            System.err.println( "MENSAGEM: " + mensagem );
//            //1º PASSO: Guardar a mensagem a assinar
//            String guardar_mensagem_camando = "cmd /c echo " + mensagem + " > \"C:\\OpenSSL-Win64\\bin\\Registo1.txt\"";
//
//            //2º PASSO: Assinar a mensagem contida no ficheiro Registo1.txt
//            String assinar_mensagem_camando = "cmd /c openssl dgst -sha1 -sign \"C:\\OpenSSL-Win64\\bin\\ChavePrivada.txt\" -out \"C:\\OpenSSL-Win64\\bin\\Registo1.sha1\" \"C:\\OpenSSL-Win64\\bin\\Registo1.txt\"";
//
//            //3º PASSO(*): Seguidamente e necessario efetuar o encoding para base 64 do ficheiro Registo1.shal
//            String codificar_assinatura_mensagem_camando = "cmd /c openssl enc -base64 -in \"C:\\OpenSSL-Win64\\bin\\Registo1.sha1\" -out \"C:\\OpenSSL-Win64\\bin\\Registo1.b64\" -A";
//
//            rodarComandoWindows( guardar_mensagem_camando, true );
//            rodarComandoWindows( assinar_mensagem_camando, true );
//            rodarComandoWindows( codificar_assinatura_mensagem_camando, true );
//
//            Scanner scanner = new Scanner( new File( "C:\\OpenSSL-Win64\\bin\\Registo1.b64" ) );
//
//            if ( scanner.hasNext() )
//            {
//                final_hash = scanner.nextLine();
//            }
            String mensagem = String.format( "%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );

            System.out.println( "GrossTotal: " + parmGrossTotal );
            System.err.println( "HashAnterior: " + hash );
            System.err.println( "Mensagem: " + mensagem );
            System.err.println( "\n" );
            //1º PASSO: Guardar a mensagem a assinar
            File file = new File( PATH + "Registo.txt" );
            MetodosUtil.escreverNoDocumento( mensagem, file, "" );

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC( assinatura, true );

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC( encoding, true );

            Scanner scanner = new Scanner( new File( PATH + "Registo.b64" ) );

            if ( scanner.hasNext() )
            {
                final_hash = scanner.nextLine();
            }

        }
        catch ( FileNotFoundException ex )
        {
        }

        return final_hash;
    }

    private static String criptografia_hash_compras( Compras compras, double parmGrossTotal, BDConexao conexao )
    {

        String final_hash = "";
        try
        {
            String invoiceDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD ).format( compras.getDataCompra() );
            String systemEntryDate = new SimpleDateFormat( CfConstantes.YYYY_MM_DD_T_HH_MM_SS ).format( compras.getDataCompra() );
            String invoiceNo = compras.getCodFact();
            System.err.println( "InvoiceNO: " + invoiceNo );
            String grossoTotal = getValorCasasDecimais( parmGrossTotal, CASAS_DECIMAIS );
            String hash = new ComprasDao( emf ).getHashAnterior( invoiceNo, conexao ).trim();
            String mensagem = String.format( "%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash );
            System.out.println( "Parm GrossTotal: " + parmGrossTotal );
            System.err.println( "HashAnterior: " + hash );
            System.err.println( "Mensagem: " + mensagem );

            //1º PASSO: Guardar a mensagem a assinar
            File file = new File( PATH + "Registo.txt" );
            MetodosUtil.escreverNoDocumento( mensagem, file, "" );

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC( assinatura, true );

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC( encoding, true );

            Scanner scanner = new Scanner( new File( PATH + "Registo.b64" ) );

            if ( scanner.hasNext() )
            {
                final_hash = scanner.nextLine();
            }

        }
        catch ( FileNotFoundException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return final_hash;
    }

    public static Process rodarComandoMAC( String comando, boolean waitFor )
    {
        Process process_rodar_bat_Stream = null;

        try
        {
            Runtime terminal = Runtime.getRuntime();
            //System.err.println( "Espaço de memória: " + ( terminal.freeMemory() / 1024 ) );

            process_rodar_bat_Stream = terminal.exec( comando );
            InputStream inputStream = process_rodar_bat_Stream.getInputStream();

            if ( waitFor )
            {
                process_rodar_bat_Stream.waitFor();
            }

            exibirSaida( inputStream );
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( BDBackupJFrame.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( InterruptedException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( BDBackupJFrame.class.getName() ).log( Level.SEVERE, null, ex );
        }
        finally
        {
            return process_rodar_bat_Stream;
        }

    }

    public static double getQtdInItemPedidos( BDConexao conexao, int id_produto )
    {
        String sql = "SELECT SUM(qtd) AS TOTAL FROM  tb_item_pedidos WHERE  fk_produtos = " + id_produto + " AND status_convertido = false";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0d;
        }

        return 0d;
    }

    public static boolean esvaziaBaseDados( BDConexao conexao )
    {
        String sql = "call esvazia_banco_dados()";
        return conexao.executeUpdate( sql );
    }

    public static String getDataBancoFull( Date date )
    {
        return ( date.getYear() + 1900 ) + "-" + ( date.getMonth() + 1 ) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }

    public static String getDataBancoFull2( Date date )
    {
        return date.getDate() + "-"
                + ( date.getMonth() + 1 ) + "-"
                + ( date.getYear() + 1900 )
                + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }

    public static void desligarPc()
    {

        try
        {
            System.err.println( "O seu computadro será desligado." );
            Runtime.getRuntime().exec( "shutdown -s" );
        }
        catch ( IOException ex )
        {

            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public MetodosUtil()
    {

    }

    public static String formatarComoPercentagem( Double taxa )
    {
        return String.format( "%.2f %%", taxa );
    }

    public Vector getPeriodo( int mesesPagar )
    {

        Vector vector = new Vector();

        switch ( mesesPagar )
        {

            case 0:
            case 1:
            {

                vector.add( "Janeiro" );
                vector.add( "Fevereiro" );
                vector.add( "Marco" );
                vector.add( "Abril" );
                vector.add( "Maio" );
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;
            case 2:
            {
                vector.add( "Fevereiro" );
                vector.add( "Marco" );
                vector.add( "Abril" );
                vector.add( "Maio" );
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;
            case 3:
            {
                vector.add( "Marco" );
                vector.add( "Abril" );
                vector.add( "Maio" );
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );
            }
            break;
            case 4:
            {
                vector.add( "Abril" );
                vector.add( "Maio" );
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;
            case 5:
            {
                vector.add( "Maio" );
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );
            }
            break;
            case 6:
            {
                vector.add( "Junho" );
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;

            case 7:
            {
                vector.add( "Julho" );
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );
                return vector;
            }
            case 8:
            {
                vector.add( "Agosto" );
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;
            case 9:
            {
                vector.add( "Setembro" );
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            break;
            case 10:
            {
                vector.add( "Outubro" );
                vector.add( "Novembro" );
                vector.add( "Dezembro" );

            }
            case 11:
            {
                vector.add( "Novembro" );
                vector.add( "Dezembro" );
            }
            case 12:
            {
                vector.add( "Dezembro" );

            }

            default:
                vector.add( "" );

        }

        return vector;

    }

    public static void esvaziar_tabela( JTable tabela )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount( 0 );
    }

    //CRIACAO 
    public int getMesPagar( String mesesPagar )
    {

        Vector vector = new Vector();

        switch ( mesesPagar )
        {

            case "Janeiro":
            {
                return 1;
            }
            case "Fevereiro":
            {
                return 2;
            }
            case "Marco":
            {
                return 3;
            }
            case "Abril":
            {
                return 4;
            }
            case "Maio":
            {
                return 5;
            }
            case "Junho":
            {
                return 6;
            }
            case "Julho":
            {
                return 7;
            }

            case "Agosto":
            {
                return 8;
            }
            case "Setembro":
            {
                return 9;
            }
            case "Outubro":
            {
                return 10;
            }
            case "Novembro":
            {
                return 11;
            }
            case "Dezembro":
            {
                return 12;
            }

            default:
                return 0;

        }

    }

    public static String operacao( String saldo_aluno, String valor_entregue, String valor_pagar )
    {

        NumberFormat format = NumberFormat.getInstance();
        double v1 = Double.parseDouble( saldo_aluno );
        double v2 = Double.parseDouble( valor_entregue );
        double v3 = Double.parseDouble( valor_pagar );

        System.out.println( "SALDO ALUNO: " + saldo_aluno );
        System.out.println( "VALOR ENTREGUE:  " + valor_entregue );
        System.out.println( "VALOR PAGAR:  " + valor_pagar );

        double res = ( 0 + v2 ) - v3;

        System.out.println( "resultado " + res );

        return format.format( res );

    }

    public boolean permitir_licenca()
    {

        if ( validadeDao.findTbValidade( 1 ).getDataInicio().equals( validadeDao.findTbValidade( 1 ).getDataFim() ) )
        {
            return false;
        }
        else if ( getDiasAnos( validadeDao.findTbValidade( 1 ).getDataInicio(), validadeDao.findTbValidade( 1 ).getDataFim() ) <= 30 )
        {

            int diferenca_dia = getDiasAnos( validadeDao.findTbValidade( 1 ).getDataInicio(), validadeDao.findTbValidade( 1 ).getDataFim() );
            if ( diferenca_dia == 0 )
            {
                return false;
            }
            JOptionPane.showMessageDialog( null, "Faltam " + diferenca_dia + "  dias para a licença terminar \nContacte-nos: 923409284/940537124", "AVISO", JOptionPane.WARNING_MESSAGE );

        }
        return true;

    }

    private int getDiasAnos( Date data_inicio, Date data_fim )
    {

        int dia_mes_entrada = getDias( data_inicio.getMonth(), data_inicio.getYear() );
        int diferenca_entrada = dia_mes_entrada - data_inicio.getDate() + 1;//1 - porque o ultimo dia  conta.

        int dia_actual = 0;
        int dias_intervalo = 0;
        int total_dias = 0;

        if ( data_fim.getMonth() == data_inicio.getMonth() && data_fim.getDate() == data_inicio.getDate() )
        {
            total_dias = 1;

        }
        else if ( data_inicio.getMonth() == data_fim.getMonth() )
        {
            dia_actual = 0;
            total_dias = data_fim.getDate() - data_inicio.getDate() + 1;
        }
        else
        {

            dia_actual = data_fim.getDate();
            dias_intervalo = soma_dia_intervalo( data_inicio, data_fim );

            System.err.println( "DIA entrada " + data_inicio.getDate() );

            System.err.println( "Diferença entrada " + diferenca_entrada );
            System.err.println( "Diferença saida " + dia_actual );
            System.err.println( "Dias do intervalo" + dias_intervalo );
            //total_dias  = diferenca_entrada +    dias_intervalo + dia_actual - 1;
            total_dias = diferenca_entrada + dias_intervalo + dia_actual;

        }

        //contagem_datas(data_inicio, data_fim);
        return total_dias - 1;

    }

    private int getDias( int mes, int ano )
    {

        switch ( mes )
        {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
            {
                return getDiaBissesto( 1900 + ano );
            }
        }

    }

    private int soma_dia_intervalo( Date data_entrada, Date data_saida )
    {
        int soma_dia = 0;
        for ( int i = data_entrada.getMonth() + 1; i < data_saida.getMonth(); i++ )
        {
            soma_dia = soma_dia + getDias( i, data_entrada.getYear() );

        }
        return soma_dia;

    }

    private int getDiaBissesto( int ano )
    {
        switch ( ano )
        {
            case 2016:
            case 2020:
            case 2024:
            case 2028:
            case 2032:
                return 29;
            default:
                return 28;
        }

    }

    public void actualizar_data_inicio()
    {
        TbValidade validade = validadeDao.findTbValidade( 1 );
        validade.setDataInicio( new Date() );

        try
        {
            validadeDao.edit( validade );
        }
        catch ( Exception e )
        {
        }

    }

//    public static String valorPorExtenso(double vlr) {
//        if (vlr == 0) {
//            return ("zero");
//        }
//
//        long inteiro = (long) Math.abs(vlr); // parte inteira do valor
//        double resto = vlr - inteiro;       // parte fracionária do valor
//
//        String vlrS = String.valueOf(inteiro);
//        if (vlrS.length() > 15) {
//            return ("Erro: valor superior a 999 trilhões.");
//        }
//
//        String s = "", saux, vlrP;
//        String centavos = String.valueOf((int) Math.round(resto * 100));
//
//        String[] unidade = {"", " Um", "Dois", "Três", "Quatro", "Cinco",
//            "Seis", "Sete", "Oito", "Nove", "Dez", "Onze",
//            "Doze", "Treze", "Catorze", "Quinze", "Dezasseis",
//            "Dezassete", "Dezoito", "Dezanove"};
//        String[] centena = {"", "Cento", "Duzentos", "Trezentos",
//            "Quatrocentos", "Quinhentos", "Seiscentos",
//            "Setecentos", "Oitocentos", "Novecentos"};
//        String[] dezena = {"", "", "Vinte", "Trinta", "Quarenta", "Cinquenta",
//            "Sessenta", "Setenta", "Oitenta", "Noventa"};
//        String[] qualificaS = {"", "Mil", "Milhão", "Bilhão", "Trilhão"};
//        String[] qualificaP = {"", "Mil", "Milhões", "Bilhões", "Trilhões"};
//
//// definindo o extenso da parte inteira do valor
//        int n, unid, dez, cent, tam, i = 0;
//        boolean umReal = false, tem = false;
//        while (!vlrS.equals("0")) {
//            tam = vlrS.length();
//// retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
//// 1a. parte = 789 (centena)
//// 2a. parte = 456 (mil)
//// 3a. parte = 123 (milhões)
//            if (tam > 3) {
//                vlrP = vlrS.substring(tam - 3, tam);
//                vlrS = vlrS.substring(0, tam - 3);
//            } else { // última parte do valor
//                vlrP = vlrS;
//                vlrS = "0";
//            }
//            if (!vlrP.equals("000")) {
//                saux = "";
//                if (vlrP.equals("100")) {
//                    saux = "Cem";
//                } else {
//                    n = Integer.parseInt(vlrP, 10);  // para n = 371, tem-se:
//                    cent = n / 100;                  // cent = 3 (centena trezentos)
//                    dez = (n % 100) / 10;            // dez  = 7 (dezena setenta)
//                    unid = (n % 100) % 10;           // unid = 1 (unidade um)
//                    if (cent != 0) {
//                        saux = centena[cent];
//                    }
//                    if ((n % 100) <= 19) {
//                        if (saux.length() != 0) {
//                            saux = saux + unidade[n % 100];
//                        } else {
//                            saux = unidade[n % 100];
//                        }
//                    } else {
//                        if (saux.length() != 0) {
//                            saux = saux + " e " + dezena[dez];
//                        } else {
//                            saux = dezena[dez];
//                        }
//                        if (unid != 0) {
//                            if (saux.length() != 0) {
//                                saux = saux + " e " + unidade[unid];
//                            } else {
//                                saux = unidade[unid];
//                            }
//                        }
//                    }
//                }
//                if (vlrP.equals("1") || vlrP.equals("001")) {
//                    if (i == 0) // 1a. parte do valor (um real)
//                    {
//                        umReal = true;
//                    } else {
//                        saux = saux + " " + qualificaS[i];
//                    }
//                } else if (i != 0) {
//                    saux = saux + " " + qualificaP[i];
//                }
//                if (s.length() != 0) {
//                    s = saux + ", " + s;
//                } else {
//                    s = saux;
//                }
//            }
//            if (((i == 0) || (i == 1)) && s.length() != 0) {
//                tem = true; // tem centena ou mil no valor
//            }
//            i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
//        }
//
//        if (s.length() != 0) {
//            if (umReal) {
//                s = s + " Kwanzas.";
//            } else if (tem) {
//                s = s + " Kwanzas.";
//            } else {
//                s = s + " de Kwanzas.";
//            }
//        }
//
//// definindo o extenso dos centavos do valor
//        if (!centavos.equals("0")) { // valor com centavos
//            if (s.length() != 0) // se não é valor somente com centavos
//            {
//                s = s + " e ";
//            }
//            if (centavos.equals("1")) {
//                s = s + "Um Centimo";
//            } else {
//                n = Integer.parseInt(centavos, 10);
//                if (n <= 19) {
//                    s = s + unidade[n];
//                } else {             // para n = 37, tem-se:
//                    unid = n % 10;   // unid = 37 % 10 = 7 (unidade sete)
//                    dez = n / 10;    // dez  = 37 / 10 = 3 (dezena trinta)
//                    s = s + dezena[dez];
//                    if (unid != 0) {
//                        s = s + " e " + unidade[unid];
//                    }
//                }
//                s = s + " Centimos";
//            }
//        }
//        return (s);
//    }
    public static String valorPorExtenso( double vlr, String moeda )
    {
        if ( vlr == 0 )
        {
            return ( "zero" );
        }

        long inteiro = (long) Math.abs( vlr ); // parte inteira do valor
        double resto = vlr - inteiro;       // parte fracionária do valor

        String vlrS = String.valueOf( inteiro );
        if ( vlrS.length() > 15 )
        {
            return ( "Erro: valor superior a 999 trilhões." );
        }

        String s = "", saux, vlrP;
        String centavos = String.valueOf( (int) Math.round( resto * 100 ) );

        String[] unidade
                =
                {
                    "", " Um", "Dois", "Três", "Quatro", "Cinco",
                    "Seis", "Sete", "Oito", "Nove", "Dez", "Onze",
                    "Doze", "Treze", "Catorze", "Quinze", "Dezasseis",
                    "Dezassete", "Dezoito", "Dezanove"
                };
        String[] centena
                =
                {
                    "", "Cento ", "Duzentos ", "Trezentos ",
                    "Quatrocentos ", "Quinhentos ", "Seiscentos ",
                    "Setecentos ", "Oitocentos ", "Novecentos "
                };
        String[] dezena
                =
                {
                    "", "", "Vinte", "Trinta", "Quarenta", "Cinquenta",
                    "Sessenta", "Setenta", "Oitenta", "Noventa"
                };
        String[] qualificaS
                =
                {
                    "", "Mil", "Milhão", "Bilhão", "Trilhão"
                };
        String[] qualificaP
                =
                {
                    "", "Mil", "Milhões", "Bilhões", "Trilhões"
                };

// definindo o extenso da parte inteira do valor
        int n, unid, dez, cent, tam, i = 0;
        boolean umReal = false, tem = false;
        while ( !vlrS.equals( "0" ) )
        {
            tam = vlrS.length();
// retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
// 1a. parte = 789 (centena)
// 2a. parte = 456 (mil)
// 3a. parte = 123 (milhões)
            if ( tam > 3 )
            {
                vlrP = vlrS.substring( tam - 3, tam );
                vlrS = vlrS.substring( 0, tam - 3 );
            }
            else
            { // última parte do valor
                vlrP = vlrS;
                vlrS = "0";
            }
            if ( !vlrP.equals( "000" ) )
            {
                saux = "";
                if ( vlrP.equals( "100" ) )
                {
                    saux = "Cem";
                }
                else
                {
                    n = Integer.parseInt( vlrP, 10 );  // para n = 371, tem-se:
                    cent = n / 100;                  // cent = 3 (centena trezentos)
                    dez = ( n % 100 ) / 10;            // dez  = 7 (dezena setenta)
                    unid = ( n % 100 ) % 10;           // unid = 1 (unidade um)
                    if ( cent != 0 )
                    {
                        saux = centena[ cent ];
                    }
                    if ( ( n % 100 ) <= 19 )
                    {
                        if ( saux.length() != 0 )
                        {
                            saux = saux + unidade[ n % 100 ];
                        }
                        else
                        {
                            saux = unidade[ n % 100 ];
                        }
                    }
                    else
                    {
                        if ( saux.length() != 0 )
                        {
                            saux = saux + " e " + dezena[ dez ];
                        }
                        else
                        {
                            saux = dezena[ dez ];
                        }
                        if ( unid != 0 )
                        {
                            if ( saux.length() != 0 )
                            {
                                saux = saux + " e " + unidade[ unid ];
                            }
                            else
                            {
                                saux = unidade[ unid ];
                            }
                        }
                    }
                }
                if ( vlrP.equals( "1" ) || vlrP.equals( "001" ) )
                {
                    if ( i == 0 ) // 1a. parte do valor (um real)
                    {
                        umReal = true;
                    }
                    else
                    {
                        saux = saux + " " + qualificaS[ i ];
                    }
                }
                else if ( i != 0 )
                {
                    saux = saux + " " + qualificaP[ i ];
                }
                if ( s.length() != 0 )
                {
                    s = saux + ", " + s;
                }
                else
                {
                    s = saux;
                }
            }
            if ( ( ( i == 0 ) || ( i == 1 ) ) && s.length() != 0 )
            {
                tem = true; // tem centena ou mil no valor
            }
            i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
        }

        if ( s.length() != 0 )
        {
            if ( umReal )
            {
                s = s + " " + moeda;
            }
            else if ( tem )
            {
                s = s + " " + moeda + "(s)";
            }
            else
            {
                s = s + " de " + moeda + "(s)";
            }
        }

// definindo o extenso dos centavos do valor
        if ( !centavos.equals( "0" ) )
        { // valor com centavos
            if ( s.length() != 0 ) // se não é valor somente com centavos
            {
                s = s + " e ";
            }
            if ( centavos.equals( "1" ) )
            {
                s = s + "Um Centimo";
            }
            else
            {
                n = Integer.parseInt( centavos, 10 );
                if ( n <= 19 )
                {
                    s = s + unidade[ n ];
                }
                else
                {             // para n = 37, tem-se:
                    unid = n % 10;   // unid = 37 % 10 = 7 (unidade sete)
                    dez = n / 10;    // dez  = 37 / 10 = 3 (dezena trinta)
                    s = s + dezena[ dez ];
                    if ( unid != 0 )
                    {
                        s = s + " e " + unidade[ unid ];
                    }
                }
                s = s + " Centimos";
            }
        }
        return ( s );
    }

    public static String valorPorExtensoBigDecima( BigDecimal valor, String moeda )
    {
        if ( valor.compareTo( BigDecimal.ZERO ) == 0 )
        {
            return "zero";
        }

        // Arredonda para 2 casas decimais
        valor = valor.setScale( 2, RoundingMode.HALF_UP );

        BigDecimal parteInteiraBD = valor.setScale( 0, RoundingMode.DOWN );
        BigDecimal parteFracionariaBD = valor.subtract( parteInteiraBD ).multiply( BigDecimal.valueOf( 100 ) ).setScale( 0, RoundingMode.HALF_UP );

        long inteiro = parteInteiraBD.longValue();
        int centavos = parteFracionariaBD.intValue();

        String vlrS = String.valueOf( inteiro );
        if ( vlrS.length() > 15 )
        {
            return "Erro: valor superior a 999 trilhões.";
        }

        String s = "", saux, vlrP;
        String[] unidade =
        {
            "", "Um", "Dois", "Três", "Quatro", "Cinco",
            "Seis", "Sete", "Oito", "Nove", "Dez", "Onze",
            "Doze", "Treze", "Catorze", "Quinze", "Dezasseis",
            "Dezassete", "Dezoito", "Dezanove"
        };
        String[] centena =
        {
            "", "Cento", "Duzentos", "Trezentos",
            "Quatrocentos", "Quinhentos", "Seiscentos",
            "Setecentos", "Oitocentos", "Novecentos"
        };
        String[] dezena =
        {
            "", "", "Vinte", "Trinta", "Quarenta", "Cinquenta",
            "Sessenta", "Setenta", "Oitenta", "Noventa"
        };
        String[] qualificaS =
        {
            "", "Mil", "Milhão", "Bilhão", "Trilhão"
        };
        String[] qualificaP =
        {
            "", "Mil", "Milhões", "Bilhões", "Trilhões"
        };

        int n, unid, dez, cent, tam, i = 0;
        boolean umReal = false, tem = false;

        while ( !vlrS.equals( "0" ) )
        {
            tam = vlrS.length();
            if ( tam > 3 )
            {
                vlrP = vlrS.substring( tam - 3 );
                vlrS = vlrS.substring( 0, tam - 3 );
            }
            else
            {
                vlrP = vlrS;
                vlrS = "0";
            }

            if ( !vlrP.equals( "000" ) )
            {
                saux = "";
                if ( vlrP.equals( "100" ) )
                {
                    saux = "Cem";
                }
                else
                {
                    n = Integer.parseInt( vlrP );
                    cent = n / 100;
                    dez = ( n % 100 ) / 10;
                    unid = n % 10;

                    if ( cent != 0 )
                    {
                        saux = centena[ cent ];
                    }
                    if ( ( n % 100 ) <= 19 )
                    {
                        saux = ( saux.length() > 0 ? saux + " e " : "" ) + unidade[ n % 100 ];
                    }
                    else
                    {
                        saux = ( saux.length() > 0 ? saux + " e " : "" ) + dezena[ dez ];
                        if ( unid != 0 )
                        {
                            saux = saux + " e " + unidade[ unid ];
                        }
                    }
                }

                if ( vlrP.equals( "1" ) || vlrP.equals( "001" ) )
                {
                    if ( i == 0 )
                    {
                        umReal = true;
                    }
                    else
                    {
                        saux += " " + qualificaS[ i ];
                    }
                }
                else if ( i != 0 )
                {
                    saux += " " + qualificaP[ i ];
                }

                if ( s.length() > 0 )
                {
                    s = saux + ", " + s;
                }
                else
                {
                    s = saux;
                }
            }

            if ( ( i == 0 || i == 1 ) && s.length() > 0 )
            {
                tem = true;
            }

            i++;
        }

        if ( s.length() > 0 )
        {
            if ( umReal )
            {
                s += " " + moeda;
            }
            else if ( tem )
            {
                s += " " + moeda + "(s)";
            }
            else
            {
                s += " de " + moeda + "(s)";
            }
        }

        // Centavos
        if ( centavos > 0 )
        {
            if ( s.length() > 0 )
            {
                s += " e ";
            }

            if ( centavos == 1 )
            {
                s += "Um Centimo";
            }
            else
            {
                if ( centavos <= 19 )
                {
                    s += unidade[ centavos ];
                }
                else
                {
                    dez = centavos / 10;
                    unid = centavos % 10;
                    s += dezena[ dez ];
                    if ( unid != 0 )
                    {
                        s += " e " + unidade[ unid ];
                    }
                }
                s += " Centimos";
            }
        }

        return s;
    }

    public static String getValor( String valor )
    {
        JFormattedTextField valor_marcarado;
        //Defindo uma Mascará
        DecimalFormat decimalFormat = new DecimalFormat( "#,###,###,###.00" );
        NumberFormatter numberFormatter = new NumberFormatter( decimalFormat );
        numberFormatter.setFormat( decimalFormat );
        numberFormatter.setAllowsInvalid( false );

        valor_marcarado = new JFormattedTextField();
        valor_marcarado.setFormatterFactory( new DefaultFormatterFactory( numberFormatter ) );
        //Convertendo o salrio base em uma String

        //Retirando o ponto por causa da conversão do double.
        // String valor_subsidio = valor.substring(0, valor.length() - 2).replace(".", "").trim();
        valor_marcarado.setText( valor );

        return valor_marcarado.getText();

    }

    public static Object getObj( int cod_usuario, Object obj )
    {
        if ( obj == null )
        {
            try
            {
                obj = new Object();
            }
            catch ( Exception e )
            {
            }

        }

        return obj;
    }

    public static void adiciona_quantidade( int cod, double quantidade, int idArmazem, BDConexao conexao )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod, idArmazem, conexao ) + quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
        System.out.println( sql );
        try
        {
            //System.out.println("Qtd: " +( getQuantidadeProduto(cod , idArmazem) + quantidade) );
            conexao.executeUpdate( sql );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

//    public static void adiciona_quantidade(int cod, double quantidade, int idArmazem, BDConexao conexao) {
//
//        System.out.println("%Qtd " + quantidade);
//        System.out.println("%Qtd Existente: " + getQuantidadeProduto(cod, idArmazem, conexao));
//        double qtd_actualizada = getQuantidadeProduto(cod, idArmazem, conexao) + quantidade;
//        System.out.println("%Qtd Ectualizada: " + qtd_actualizada);
//
//        String sql = "UPDATE tb_stock SET quantidade_existente =  " + qtd_actualizada + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
//        System.out.println(sql);
//        try {
//            //System.out.println("Qtd: " +( getQuantidadeProduto(cod , idArmazem) + quantidade) );
//            conexao.executeUpdate(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    public static void subtrai_quantidade( int cod, double quantidade, int idArmazem, BDConexao conexao )
    {

        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod, idArmazem, conexao ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
        conexao.executeUpdate( sql );

    }

    public static String getMesPagarU( int id_mes )
    {

        switch ( id_mes )
        {

            case 1:
            {
                return "Janeiro";
            }
            case 2:
            {
                return "Fevereiro";
            }
            case 3:
            {
                return "Março";
            }
            case 4:
            {
                return "Abril";
            }
            case 5:
            {
                return "Maio";
            }
            case 6:
            {
                return "Junho";
            }

            case 7:
            {
                return "JUlho";
            }
            case 8:
            {
                return "Agosto";
            }
            case 9:
            {
                return "Setembro";
            }
            case 10:
            {
                return "Outubro";
            }
            case 11:
            {
                return "Novembro";
            }
            case 12:
            {
                return "Dezembro";
            }

            default:
                return "";
        }

    }

    public static void remover_item_carrinho( JTable tabela )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount( 0 );

    }

//    public double calculo_irt( double salario_base, List<TbItemSubsidiosFuncionario> subsidios, double outro_desconto )
//    {
//
//        //seta os dados de taxa de tributação
//        DadosIrt taxas_tributacao = taxas_tributacao( salario_base );
//
//        final int CONSTANTE = 30000; // para os subsidios que nao sao tributados na totalidade.
//
//        double sub_alimentacao = 0,
//                sub_transporte = 0,
//                sub_atavio = 0,
//                horas_extra = 0,
//                outro_sub = 0;
//        double ss = 0, mcirt = 0, irt = 0, diferenca = 0, salario_liquido = 0;
//
//        try
//        {
//
//            if ( subsidios != null )
//            {
//                /*busca todos os subsídeos */
//                for ( TbItemSubsidiosFuncionario object : subsidios )
//                {
//
//                    if ( object.getIdSubsidioFK().getDescricao().equals( "Sub. Alimentação" ) )
//                    {
//                        sub_alimentacao = object.getValorSubsidio();
//                    }
//                    else if ( object.getIdSubsidioFK().getDescricao().equals( "Sub. Transporte" ) )
//                    {
//                        sub_transporte = object.getValorSubsidio();
//                    }
//                    else if ( object.getIdSubsidioFK().getDescricao().equals( "Sub. Atavio" ) )
//                    {
//                        sub_atavio = object.getValorSubsidio();
//                    }
//                    else if ( object.getIdSubsidioFK().getDescricao().equals( "Horas Extra" ) )
//                    {
//                        horas_extra = object.getValorSubsidio();
//                    }
//                    else
//                    {
//                        outro_sub = outro_sub + object.getValorSubsidio();
//                    }
//                }
//            }
//
//        }
//        catch ( Exception e )
//        {
//        }
//
//        //calcula o segurança social - que é 3% do salario base. 
//        ss = ( ( salario_base * 3 ) / 100 );
//
//        //calcular o mcirt - matéria coletável de imposto de rendimento de trabalho.
//        mcirt = ( ( salario_base + sub_atavio + horas_extra ) - ss );
//
//        diferenca = ( sub_alimentacao + sub_transporte );
//
//        if ( diferenca > CONSTANTE )
//        {
//            diferenca = diferenca - CONSTANTE;
//            mcirt = ( mcirt + diferenca );
//        }
//
//        /*cáculo do irt*/
//        irt = ( mcirt - taxas_tributacao.getExcesso() );
//        irt = ( ( irt * taxas_tributacao.getPercentagem() ) / 100 );
//        irt = ( irt + taxas_tributacao.getParcela_fixa() );
//
//        /*Cáculo do salário líquido*/
//        salario_liquido = salario_base + sub_alimentacao + sub_transporte + sub_atavio + horas_extra + outro_sub;
//        salario_liquido = ( salario_liquido - ( irt + ss + outro_desconto ) );
//
//        return irt;
//
//    }
    public double getTotalSubsidio( List<TbItemSubsidiosFuncionario> subsidios )
    {
        double total = 0;
        for ( TbItemSubsidiosFuncionario subsidio : subsidios )
        {
            total += subsidio.getValorSubsidio();
        }
        return total;
    }

    public double getTotalSubsidioTributavel( List<TbItemSubsidiosFuncionario> subsidios )
    {

        double total = 0, CONSTANTE = 30000;
        for ( TbItemSubsidiosFuncionario subsidio : subsidios )
        {
            if ( !subsidio.getIdSubsidioFK().getIncidencia_inss() ) // subsidio que nao sao tributavel na totalidade.
            {
                if ( subsidio.getValorSubsidio() > CONSTANTE )
                {
                    total += ( subsidio.getValorSubsidio() - CONSTANTE );
                }
            }
            else
            {
                total += subsidio.getValorSubsidio();
            }
        }
        return total;
    }

    public double calculo_irt( double salario_base, List<TbItemSubsidiosFuncionario> subsidios, double outro_desconto )
    {

        double salario_bruto = salario_base + getTotalSubsidio( subsidios );
        double seguranca_social = ( salario_bruto * 0.03 );
        double material_coletavel = salario_base - seguranca_social + getTotalSubsidioTributavel( subsidios );
        //seta os dados de taxa de tributação
        DadosIrt taxas_tributacao = taxas_tributacao( material_coletavel );

        double parcela_fixa = taxas_tributacao.parcela_fixa;
        double excesso = taxas_tributacao.excesso;
        double taxa = ( taxas_tributacao.percentagem / 100 );
        double irt = ( parcela_fixa + ( ( material_coletavel - excesso ) * taxa ) );

        return irt;

    }
//    NOVA TABELA IRT

    public static DadosIrt taxas_tributacao( double material_coletavel )
    {

        DadosIrt dados = new DadosIrt();
//        if ( salario_base <= 70000 )
        if ( material_coletavel <= 70000 )
        {
            dados.setParcela_fixa( 0 );
            dados.setPercentagem( 0 );
            dados.setExcesso( 0 );
            return dados;

        }
        else if ( material_coletavel >= 70001 && material_coletavel <= 100000 )
        {
            dados.setParcela_fixa( 3000 );
            dados.setPercentagem( 10 );
            dados.setExcesso( 70000 );
            return dados;

        }
        else if ( material_coletavel >= 100001 && material_coletavel <= 150000 )
        {
            dados.setParcela_fixa( 6000 );
            dados.setPercentagem( 13 );
            dados.setExcesso( 100000 );
            return dados;

        }
        else if ( material_coletavel >= 150001 && material_coletavel <= 200000 )
        {
            dados.setParcela_fixa( 12500 );
            dados.setPercentagem( 16 );
            dados.setExcesso( 150000 );
            return dados;

        }
        else if ( material_coletavel >= 200001 && material_coletavel <= 300000 )
        {
            dados.setParcela_fixa( 31250 );
            dados.setPercentagem( 16 );
            dados.setExcesso( 200000 );
            return dados;

        }
        else if ( material_coletavel >= 300001 && material_coletavel <= 500000 )
        {
            dados.setParcela_fixa( 49250 );
            dados.setPercentagem( 19 );
            dados.setExcesso( 300000 );
            return dados;

        }
        else if ( material_coletavel >= 500001 && material_coletavel <= 1000000 )
        {
            dados.setParcela_fixa( 87250 );
            dados.setPercentagem( 20 );
            dados.setExcesso( 500000 );
            return dados;

        }
        else if ( material_coletavel >= 1000001 && material_coletavel <= 1500000 )
        {
            dados.setParcela_fixa( 187250 );
            dados.setPercentagem( 21 );
            dados.setExcesso( 1000000 );
            return dados;

        }
        else if ( material_coletavel >= 1500001 && material_coletavel <= 2000000 )
        {
            dados.setParcela_fixa( 292000 );
            dados.setPercentagem( 22 );
            dados.setExcesso( 1500000 );
            return dados;

        }
        else if ( material_coletavel >= 2000001 && material_coletavel <= 2500000 )
        {
            dados.setParcela_fixa( 402250 );
            dados.setPercentagem( 23 );
            dados.setExcesso( 2000000 );
            return dados;

        }
        else if ( material_coletavel >= 2500001 && material_coletavel <= 5000000 )
        {
            dados.setParcela_fixa( 517250 );
            dados.setPercentagem( 24 );
            dados.setExcesso( 2500000 );
            return dados;

        }
        else if ( material_coletavel >= 5000001 && material_coletavel <= 10000000 )
        {
            dados.setParcela_fixa( 1117250 );
            dados.setPercentagem( 24 );
            dados.setExcesso( 5000000 );
            return dados;

        }
        else if ( material_coletavel > 10000001 )
        {
            dados.setParcela_fixa( 2342250 );
            dados.setPercentagem( 25 );
            dados.setExcesso( 10000000 );
            return dados;

        }

        return dados;

    }
//    public static DadosIrt taxas_tributacao( double salario_base )
//    {
//
//        DadosIrt dados = new DadosIrt();
////        if ( salario_base <= 70000 )
//        if ( salario_base <= 34450 )
//        {
//            dados.setParcela_fixa( 0 );
//            dados.setPercentagem( 0 );
//            dados.setExcesso( 0 );
//            return dados;
//
//        }
//        else if ( salario_base >= 34451 && salario_base <= 35000 )
//        {
//            dados.setParcela_fixa( 0 );
//            dados.setPercentagem( 0 );
//            dados.setExcesso( 34450 );
//            return dados;
//
//        }
//        else if ( salario_base >= 35001 && salario_base <= 40000 )
//        {
//            dados.setParcela_fixa( 550 );
//            dados.setPercentagem( 7 );
//            dados.setExcesso( 35000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 40001 && salario_base <= 45000 )
//        {
//            dados.setParcela_fixa( 900 );
//            dados.setPercentagem( 8 );
//            dados.setExcesso( 40000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 45001 && salario_base <= 50000 )
//        {
//            dados.setParcela_fixa( 1300 );
//            dados.setPercentagem( 9 );
//            dados.setExcesso( 45000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 50001 && salario_base <= 70000 )
//        {
//            dados.setParcela_fixa( 1750 );
//            dados.setPercentagem( 10 );
//            dados.setExcesso( 50000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 70001 && salario_base <= 90000 )
//        {
//            dados.setParcela_fixa( 3750 );
//            dados.setPercentagem( 11 );
//            dados.setExcesso( 70000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 90001 && salario_base <= 110000 )
//        {
//            dados.setParcela_fixa( 5950 );
//            dados.setPercentagem( 12 );
//            dados.setExcesso( 90000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 110001 && salario_base <= 140000 )
//        {
//            dados.setParcela_fixa( 8350 );
//            dados.setPercentagem( 13 );
//            dados.setExcesso( 110000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 140001 && salario_base <= 170000 )
//        {
//            dados.setParcela_fixa( 12250 );
//            dados.setPercentagem( 14 );
//            dados.setExcesso( 140000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 170001 && salario_base <= 200000 )
//        {
//            dados.setParcela_fixa( 16450 );
//            dados.setPercentagem( 15 );
//            dados.setExcesso( 170000 );
//            return dados;
//
//        }
//        else if ( salario_base >= 200001 && salario_base <= 230000 )
//        {
//            dados.setParcela_fixa( 20950 );
//            dados.setPercentagem( 16 );
//            dados.setExcesso( 200000 );
//            return dados;
//
//        }
//        else if ( salario_base > 230000 )
//        {
//            dados.setParcela_fixa( 25750 );
//            dados.setPercentagem( 17 );
//            dados.setExcesso( 230000 );
//            return dados;
//
//        }
//
//        return dados;
//
//    }

    public static class DadosIrt
    {

        private double parcela_fixa, excesso;
        private int percentagem;

        public DadosIrt()
        {
        }

        public double getParcela_fixa()
        {
            return parcela_fixa;
        }

        public void setParcela_fixa( double parcela_fixa )
        {
            this.parcela_fixa = parcela_fixa;
        }

        public double getExcesso()
        {
            return excesso;
        }

        public void setExcesso( double excesso )
        {
            this.excesso = excesso;
        }

        public int getPercentagem()
        {
            return percentagem;
        }

        public void setPercentagem( int percentagem )
        {
            this.percentagem = percentagem;
        }

    }

    public static double getQuantidadeProduto( int cod_produto, int id_armazem, BDConexao conexao )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + id_armazem;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static int getUltimoPreco( int cod_produto, BDConexao conexao )
    {

        String sql = "SELECT MAX(pk_preco) AS PK_PRECO FROM  tb_preco WHERE  fk_produto = " + cod_produto;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "PK_PRECO" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double getPrecoCompraProduto( int cod_preco, BDConexao conexao )
    {

        String sql = "SELECT preco_compra FROM  tb_preco WHERE  pk_preco = " + cod_preco;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "preco_compra" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static void adicionar_saldo_banco( double valor, int idBanco, BDConexao conexao )
    {

        String sql = "UPDATE tb_banco SET saldo_banco =  " + ( getSaldoByBanco( idBanco ) + valor ) + " WHERE idBanco = " + idBanco;
        conexao.executeUpdate( sql );
    }

    public static void subtrair_saldo_banco( double valor, int idBanco, BDConexao conexao )
    {
        String sql = "UPDATE tb_banco SET saldo_banco =  " + ( getSaldoByBanco( idBanco ) - valor ) + " WHERE idBanco = " + idBanco;
        conexao.executeUpdate( sql );

    }

    public static double getSaldoByBanco( int idBanco )
    {

        String sql = "SELECT saldo_banco FROM  tb_banco WHERE  idBanco = " + idBanco;
        ResultSet rs = new BDConexao().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "saldo_banco" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double getSaldoByBanco( int idBanco, BDConexao conexao )
    {

        String sql = "SELECT saldo_banco FROM  tb_banco WHERE  idBanco = " + idBanco;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "saldo_banco" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static String getDescricaoMes( int codMes )
    {

        switch ( codMes )
        {
            case 0:
                return "Janeiro";
            case 1:
                return "Fevereiro";

            case 2:
                return "Março";
            case 3:
                return "Abril";

            case 4:
                return "Maio";
            case 5:
                return "Junho";
            case 6:
                return "Julho";
            case 7:
                return "Agosto";

            case 8:
                return "Setembro";
            case 9:
                return "Outubro";
            case 10:
                return "Novembro";
            default:
                return "Dezembro";

        }

    }

    public static double getAllSaldoExceptoCaixa()
    {

        String sql = "SELECT SUM(saldo_banco) AS TOTAL FROM  tb_banco WHERE  idBanco <> " + DVML.COD_BANCO_CAIXA;
        ResultSet rs = new BDConexao().executeQuery( sql );

        try
        {
            rs.close();
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

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

    public static double getAllSaldoExceptoCaixa( BDConexao conexao )
    {

        String sql = "SELECT SUM(saldo_banco) AS TOTAL FROM  tb_banco WHERE  idBanco <> " + DVML.COD_BANCO_CAIXA;
        ResultSet rs = conexao.executeQuery( sql );

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

    public static List<TbVenda> getAllVendasByIdVenda( String data, int id_armazem, BDConexao conexao, int id_documento )
    {

//        String sql = "SELECT  DISTINCT   v.* FROM  tb_item_venda i, tb_venda v WHERE  i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND v.idArmazemFK = " + id_armazem + " AND v.fk_documento = " + id_documento + " AND v.status_eliminado = 'false'  AND v.credito = 'false'  ORDER BY v.codigo DESC";
        String sql = "SELECT  DISTINCT   v.* FROM tb_venda v WHERE    DATE(v.dataVenda) = '" + data + "' AND v.idArmazemFK = " + id_armazem + " AND v.fk_documento = " + id_documento + " AND v.status_eliminado = 'false'  AND v.credito = 'false'  ORDER BY v.codigo DESC";
        ResultSet rs = conexao.executeQuery( sql );

        List<TbVenda> lista = new ArrayList<>();
        TbVenda object;
        try
        {
            while ( rs.next() )
            {

                object = new TbVenda();
//                object.setCodigo( rs.getInt( "v.codigo" ) );
                object.setCodFact( rs.getString( "v.cod_fact" ) );
                object.setNomeCliente( rs.getString( "v.nome_cliente" ) );
                object.setDataVenda( rs.getDate( "v.dataVenda" ) );
                object.setHora( rs.getTime( "v.hora" ) );
                object.setTotalVenda( rs.getBigDecimal( "v.total_venda" ) );

                lista.add( object );

            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            lista.clear();
            return lista;
        }
        return lista;
    }

    public static List<TbStock> getAllProdutoByIdArmazem( int id_armazem, BDConexao conexao )
    {

        String sql = "SELECT  DISTINCT   s.*  FROM  tb_stock s WHERE s.cod_armazem = " + id_armazem;
        ResultSet rs = conexao.executeQuery( sql );

        List<TbStock> lista = new ArrayList<>();
        TbStock object;
        try
        {
            while ( rs.next() )
            {

                object = stockDao.findTbStock( rs.getInt( "codigo" ) );
                lista.add( object );

            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            lista.clear();
            return lista;
        }
        return lista;
    }

    public static double procedimento_valor_stock( BDConexao conexao )
    {

        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ArmazemDao armazemDao = new ArmazemDao( emf );
        double saldo = 0;

        List<TbArmazem> lista_armazem = armazemDao.getArmazens();
        for ( TbArmazem armazem : lista_armazem )
        {
            System.err.println( "Cheguei aqui" );
            saldo = saldo + getSaldoStock( conexao, armazem.getDesignacao() );
        }
        return saldo;

    }

    public static double getSaldoStock( BDConexao conexao, String armazem )
    {

        double saldo = 0, preco_compra = 0;
        int qtd = 0, pk_preco = 0;

        String sql = "     SELECT DISTINCT "
                + "     tb_produto.`designacao` AS tb_produto_designacao, "
                + "     tb_stock.`quantidade_existente` AS QTD , "
                + "     tb_tipo_produto.`codigo` AS tb_tipo_produto_codigo ,"
                + "     tb_tipo_produto.`designacao` AS tb_tipo_produto_designacao , "
                + "     tb_produto.`codigo` AS tb_produto_codigo , "
                + "     tb_armazem.`designacao` AS tb_armazem_designacao , "
                + "     ( SELECT  MAX(tb_preco.`pk_preco`) AS MAX_PRECO FROM `tb_preco` tb_preco WHERE tb_preco.`fk_produto`  =  tb_produto.`codigo`) "
                + "      AS MAX_COD_PRECO , "
                + "     ( SELECT  tb_preco.`preco_compra` AS M_P FROM `tb_preco` tb_preco WHERE tb_preco.`pk_preco`  =  MAX_COD_PRECO)  "
                + "      AS MAX_PRECO_COMPRA , "
                + "     ( SELECT  MAX(tb_preco.`preco_venda`) AS M_V FROM `tb_preco` tb_preco WHERE tb_preco.`pk_preco`  =  MAX_COD_PRECO)  "
                + "      AS MAX_PRECO_VENDA ,"
                + "     ( SELECT  MAX(tb_preco.`percentagem_ganho`) AS MAX_PRECO_COMPRA FROM `tb_preco` tb_preco WHERE tb_preco.`pk_preco`  =  MAX_COD_PRECO) "
                + "      AS MAX_PERCENTAGEM_GANHO "
                + " FROM  "
                + "     `tb_produto` tb_produto INNER JOIN `tb_stock` tb_stock ON tb_produto.`codigo` = tb_stock.`cod_produto_codigo` "
                + "     INNER JOIN `tb_tipo_produto` tb_tipo_produto ON tb_produto.`cod_Tipo_Produto` = tb_tipo_produto.`codigo` "
                + "     INNER JOIN `tb_armazem` tb_armazem ON tb_stock.`cod_armazem` = tb_armazem.`codigo` "
                + " WHERE "
                + "     tb_armazem.`designacao` =   '" + armazem + "' "
                + " ORDER BY "
                + "     tb_produto.`codigo` ASC";

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            while ( rs.next() )
            {
                qtd = rs.getInt( "QTD" );
                preco_compra = rs.getDouble( "MAX_PRECO_COMPRA" );
                saldo = saldo + ( qtd * preco_compra );

            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
        }

        return saldo;

    }

    public static void fechar_todas_janelas()
    {
        System.gc();
        for ( Window window : Window.getWindows() )
        {
            window.dispose();
            // por vezes pode ser melhor usar setVisivel(false);
        }
    }

    public static void actualizar_status( int idUsuario, String status_OFF_ON )
    {
        BDConexao conexao = new BDConexao();
        String sql = "UPDATE tb_usuario SET estado_log =  '" + status_OFF_ON + "' WHERE codigo = " + idUsuario;
        conexao.executeUpdate( sql );
        conexao.close();
    }

    public static void actualizar_ip_address( int idUsuario, String ip_address )
    {
        BDConexao conexao = new BDConexao();
        String sql = "UPDATE tb_usuario SET ip_address =  '" + ip_address + "' WHERE codigo = " + idUsuario;
        conexao.executeUpdate( sql );
        conexao.close();
    }

    public List<Extrato> getListOrdenada( List<Extrato> lista_desordenada )
    {

        Extrato aux;

        for ( int i = 0; i < lista_desordenada.size(); i++ )
        {

            for ( int j = i + 1; j < lista_desordenada.size(); j++ )
            {

                if ( getNumero( lista_desordenada.get( i ).getData() ) > getNumero( lista_desordenada.get( j ).getData() ) )
                {

                    aux = lista_desordenada.get( j );
                    lista_desordenada.set( j, lista_desordenada.get( i ) );
                    lista_desordenada.set( i, aux );

                }

            }

        }

        return lista_desordenada;

    }

    public static long getNumero( Date date )
    {

        int dia = date.getDate();
        int mes = date.getMonth() + 1;
        int ano = date.getYear() + 1900;

        String data_string = ano + "" + getNumeroFormatado( mes ) + "" + getNumeroFormatado( dia );
        System.err.println( "DATA : " + data_string );
        return Long.parseLong( data_string );

    }

    public static String getNumeroFormatado( int numero )
    {

        if ( numero <= 9 )
        {
            return "0" + numero;
        }
        else
        {
            return String.valueOf( numero );
        }

    }

    public static void logo_out( int id_user, int id_empresa )
    {
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        UsuarioDao usuarioDao = new UsuarioDao( emf );
        EmpresaDao empresaDao = new EmpresaDao( emf );
        TbUsuario usuario = usuarioDao.findTbUsuario( id_user );
        Empresa empresa = empresaDao.findEmpresa( id_empresa );
        fechar_todas_janelas();

        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() != 1 )
        {
//            MetodosUtil.actualizarEstadoLog( "OFF" );
            new LoginVisao( id_user );
        }
        else
        {
            new RootVisao( id_user, id_empresa, true, new BDConexao() ).show();
        }

    }

    public static String getDataString( Date date )
    {
        Integer dia = date.getDate();
        Integer mes = ( date.getMonth() + 1 );
        Integer ano = ( date.getYear() + 1900 );

        String data = "";

        if ( dia < 10 )
        {
            data += "0" + dia + "-";
        }
        else
        {
            data += dia + "-";
        }

        if ( mes < 10 )
        {
            data += "0" + mes + "-";
        }
        else
        {
            data += mes + "-";
        }

        data += ano;

        return data;
    }

    public static String getDataBanco( Date date )
    {
        Date date_local = new Date();
        if ( !Objects.isNull( date ) )
        {
            return ( date.getYear() + 1900 ) + "-" + getNumero( date.getMonth() + 1 ) + "-" + getNumero( date.getDate() );
        }

        return ( date_local.getYear() + 1900 ) + "-" + getNumero( date_local.getMonth() + 1 ) + "-" + getNumero( date_local.getDate() );
    }

    public static String getDataString2( Date date )
    {
        return ( date.getYear() + 1900 ) + "-" + ( date.getMonth() + 1 ) + "-" + date.getDate();
    }

    public static String getHoraBanco( Date hora )
    {
        return getNumero( hora.getHours() ) + ":" + getNumero( hora.getMinutes() ) + ":" + getNumero( hora.getSeconds() );
    }

    private static String getNumero( int numero )
    {
        if ( numero < 9 )
        {
            return "0" + numero;
        }
        return String.valueOf( numero );
    }

    public int media_idade( List<Integer> lista_idade )
    {

        Integer soma_idade = 0;

        if ( !lista_idade.isEmpty() )
        {
            for ( int i = 0; i < lista_idade.size(); i++ )
            {
                soma_idade = soma_idade + lista_idade.get( i );
            }
            return ( soma_idade / lista_idade.size() );
        }
        else
        {
            return 0;
        }

    }

    public static String getNumeroTransformado( double numero )
    {

        BigDecimal bigDecimal = new BigDecimal( numero );
        bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_FLOOR );
        return String.valueOf( bigDecimal );

    }

    public static void actualizar_promocao( Promocao promocao, int id_user, BDConexao conexao )
    {

        String sql = "UPDATE promocao SET "
                + "data = '" + getDataBanco( promocao.getData() ) + "',"
                + "hora = '" + getHoraBanco( promocao.getHora() ) + "',"
                + "percentagem = " + promocao.getPercentagem() + ","
                + "retalho_groso = " + promocao.getRetalhoGroso()
                + " WHERE pk_promocao = 1";

        System.err.println( sql );
        conexao.executeUpdate( sql );

    }

    public static void salvar_preco( TbPreco preco, int id_user, BDConexao conexao )
    {

        String sql = "INSERT INTO tb_preco("
                + "data,"
                + "hora,"
                + "preco_compra,"
                + "percentagem_ganho,"
                + "fk_produto,"
                + "fk_usuario,"
                + "preco_venda,"
                + "qtd_baixo,"
                + "qtd_alto,"
                + "preco_anterior"
                + ")"
                + "VALUES("
                + "'" + MetodosUtil.getDataBanco( new Date() ) + "' ,"
                + "'" + MetodosUtil.getHoraBanco( new Date() ) + "' ,"
                + preco.getPrecoCompra() + " ,"
                + preco.getPercentagemGanho() + " ,"
                + preco.getFkProduto().getCodigo() + " ,"
                + id_user + " ,"
                + preco.getPrecoVenda() + " ,"
                + preco.getQtdBaixo() + " ,"
                + preco.getQtdAlto() + ","
                + preco.getPrecoAnterior()
                + ")";

        System.err.println( sql );
        conexao.executeUpdate( sql );

    }

    public static void salvar_stock( TbStock stock, int id_user, BDConexao conexao )
    {

        String sql = "INSERT INTO tb_stock("
                + "cod_produto_codigo,"
                + "dataEntrada,"
                + "quantidade_existente,"
                + "status,"
                + "preco_venda,"
                + "quant_critica,"
                + "quant_baixa,"
                + "quantidade_antiga,"
                + "cod_armazem,"
                + "preco_venda_grosso ,"
                + "qtd_grosso"
                + ")"
                + " VALUES("
                + stock.getCodProdutoCodigo().getCodigo() + " ,"
                + "'" + MetodosUtil.getDataBanco( stock.getDataEntrada() ) + "' ,"
                + stock.getQuantidadeExistente() + " ,"
                + "'" + stock.getStatus() + "' ,"
                + stock.getPrecoVenda() + " ,"
                + stock.getQuantCritica() + " ,"
                + stock.getQuantBaixa() + " ,"
                + stock.getQuantidadeAntiga() + " ,"
                + stock.getQuantidadeAntiga() + " ,"
                + stock.getCodArmazem().getCodigo() + " ,"
                + stock.getPrecoVendaGrosso() + " ,"
                + stock.getQtdGrosso()
                + ")";

        System.err.println( sql );
        conexao.executeUpdate( sql );

    }

    public static double retirar_dizimas( double valor )
    {

        BigDecimal bigDecimal = new BigDecimal( valor );
        bigDecimal = bigDecimal.setScale( 0, BigDecimal.ROUND_HALF_UP );
        String value = String.valueOf( bigDecimal );
        return Double.parseDouble( value );

    }

    public static String getValorTransformadoString( double valor )
    {
        return MetodosUtil.getNumeroTransformado( valor ).substring( 0, MetodosUtil.getNumeroTransformado( valor ).length() - 2 );

    }

    public static boolean getStatusPromocao( BDConexao conexao )
    {

        String sql = "SELECT retalho_groso FROM  promocao WHERE  pk_promocao = 1";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getBoolean( "retalho_groso" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return false;
        }

        return false;
    }

    public static int getContVenda( BDConexao conexao )
    {

        String sql = "SELECT cont FROM  tb_dados_instituicao WHERE idDadosInsitiuicao = 1 ";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "cont" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return -1;
        }

        return -1;
    }

    public static String getCodFact( int pk_venda, BDConexao conexao )
    {

        String sql = "SELECT cod_fact FROM  tb_venda WHERE codigo  =  " + pk_venda;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "cod_fact" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return null;
        }

        return null;
    }

    public static void actualizar_cont_venda( int cont, BDConexao conexao )
    {

        int cont_actual = cont + 1;
        String sql = "UPDATE tb_dados_instituicao SET "
                + "cont = " + cont_actual
                + " WHERE idDadosInsitiuicao = 1";
        System.err.println( sql );
        conexao.executeUpdate( sql );

    }

    public static double getColunaPromocao( String coluna, BDConexao conexao )
    {

        String sql = "SELECT " + coluna + " FROM  promocao WHERE  pk_promocao = 1";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( coluna );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double percentagem_ganho( double preco_compra, double preco_venda )
    {

        double lucro = ( preco_venda - preco_compra );
        double percetagem = ( ( lucro * 100 ) / preco_compra );
        return MetodosUtil.retirar_dizimas( percetagem );

    }

    public static double preco_venda( double percentagem_ganho, double preco_compra )
    {
        double preco_venda = ( ( ( ( percentagem_ganho * preco_compra ) ) / 100 ) + preco_compra );
        return MetodosUtil.retirar_dizimas( preco_venda );
    }

    public static double convertToDouble( String texto )
    {
        double valor = 0;
        //retira todo o ponto da string
        String valor_string = texto.replace( ".", "" );
        //transforma a vírgula em ponto
        valor_string = valor_string.replace( ",", "." );
        //converte em double finalmente
        valor = Double.parseDouble( valor_string );
        return valor;

    }

    public static float convertToFloat( String texto )
    {
        float valor = 0;
        //retira todo o ponto da string
        String valor_string = texto.replace( ".", "" );
        //transforma a vírgula em ponto
        valor_string = valor_string.replace( ",", "." );
        //converte em double finalmente
        valor = Float.parseFloat( valor_string );
        return valor;

    }

    public static void main( String[] args ) throws ParseException
    {

        fechoAutomatico();
    }

    public static String criptografia_hash( Object documento, double parmGrossTotal, BDConexao conexao )
    {
        String final_hash = "";

        if ( documento instanceof TbVenda )
        {
            System.err.println( " **********************DOC VENDA******************************" );
            final_hash = criptografia_hash_venda( (TbVenda) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Notas )
        {
            System.err.println( " **********************DOC NOTAS******************************" );
            final_hash = criptografia_hash_nota( (Notas) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Compras )
        {
            System.err.println( " **********************DOC COMPRAS ******************************" );
            final_hash = criptografia_hash_compras( (Compras) documento, parmGrossTotal, conexao );
        }

        System.err.println( "final_hash: " + final_hash );
        System.err.println( "final_hash:LENGTH: " + final_hash.length() );
        System.err.println( "****************************************************" );
        return final_hash;
    }

    public static String criptografia_hash( String formato_documento )
    {
        System.out.println( "formato_documento: " + formato_documento );
        String valor = formato_documento.trim().replace( " ", "" ) + DVML.CHAVE_PRIVADA;

        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance( "SHA-512" );
            byte[] bs = messageDigest.digest( valor.getBytes() );
            BigInteger no = new BigInteger( 1, bs );
            String hashNext = no.toString( 16 );
            while ( hashNext.length() < 32 )
            {
                hashNext = "0" + hashNext;
            }
            System.out.println( "codhash: " + hashNext + " tamanho: " + hashNext.length() );
            // return hashNext.toUpperCase();
            return hashNext;
        }
        catch ( Exception e )
        {
        }

        return "";

    }

    public static String assinatura_doc( String hash )
    {
        String assinatura = "" + hash.charAt( 0 ) + "" + hash.charAt( 10 ) + "" + hash.charAt( 20 ) + "" + hash.charAt( 30 );
        return assinatura;
    }

    public static void escreverNoDocumento( String texto, File documentoDeTexto, boolean... mudarDeLinha )
    {
        try ( FileWriter fileWriter = new FileWriter( documentoDeTexto ) )
        {
            try ( BufferedWriter bw = new BufferedWriter( fileWriter ) )
            {
                if ( mudarDeLinha.length > 0 )
                {
                    bw.newLine();
                }

                bw.write( texto );
                JOptionPane.showMessageDialog( null, "Ficheiro SAF-T criado com sucesso!..." );
            }
            catch ( FileNotFoundException ex )
            {
                Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        catch ( IOException ex )
        {
            JOptionPane.showMessageDialog( null, "Erro ao criar o ficheiro", "ERRO", JOptionPane.ERROR_MESSAGE );
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static void escreverNoDocumento( String texto, File documentoDeTexto, String mensagem, boolean... mudarDeLinha )
    {
        try ( FileWriter fileWriter = new FileWriter( documentoDeTexto ) )
        {
            try ( BufferedWriter bw = new BufferedWriter( fileWriter ) )
            {
                if ( mudarDeLinha.length > 0 )
                {
                    bw.newLine();
                }

                bw.write( texto );
                //JOptionPane.showMessageDialog( null, "Ficheiro SAF-T criado com sucesso!..." );
            }
            catch ( FileNotFoundException ex )
            {
                Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        catch ( IOException ex )
        {
            JOptionPane.showMessageDialog( null, "Erro ao criar o ficheiro", "ERRO", JOptionPane.ERROR_MESSAGE );
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static String formateDate( String pattern, Date date )
    {
        return new SimpleDateFormat( pattern ).format( date );
    }

    public static String formateDateSAFT( Date date )
    {
        return new SimpleDateFormat( CfConstantes.YYYY_MM_DD_T_HH_MM_SS ).format( date );
    }

    public static double getTaxaPercantagem( int idProduto )
    {
        return produtoImpostoDao.getTaxaByIdProduto( idProduto );
    }

    public static String getMotivoIsensao( int idProduto )
    {
        return produtoIsentoDao.getRegimeIsensaoByIdProduto( idProduto );
    }

    public static String getCodigoRegime( int idProduto )
    {
        return produtoIsentoDao.getCodigoRegimeByIdProduto( idProduto );
    }

    public static String getTaxType( int idProduto )
    {
        if ( produtoImpostoDao.exist( idProduto ) )
        {
            return "IVA";
        }
        return "NS";
    }

    public static String getTaxCode( int idProduto )
    {
        if ( produtoImpostoDao.exist( idProduto ) )
        {
            return "NOR";
        }
        return "NS";
    }

    public static String getValorCasasDecimais( double valor, int casas_dezimas )
    {

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits( casas_dezimas );
        String valor_string = decimalFormat.format( valor ).replace( ".", "" );
        return valor_string.replaceFirst( ",", "." );

    }

//    public static double getTotalSemIva( List<TbVenda> list, int pk_documento )
//    {
//
//        double soma = 0;
//        for ( TbVenda venda : list )
//        {
//            // soma += (venda.getTotalGeral() - venda.getTotalIva());
//            //TOTAL ILÍQUIDO
//            soma += getNetTotal( venda.getTbItemVendaList() );
////            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
////                soma += (venda.getTotalGeral() - venda.getTotalIva());
////            }
//
//        }
//        return soma;
//    }
    public static double getTotalIliquido( List<TbItemVenda> list )
    {

        double soma = 0, preco = 0;
        double qtd = 0d;
        for ( TbItemVenda linha : list )
        {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            soma += ( preco * qtd );
        }
        return soma;
    }

    /*não desconta o 'desconto'*/
    public static double getTotalIliquidoMaisIVA( List<TbItemVenda> list, double imposto )
    {
        double taxa = 0, total = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for ( TbItemVenda linha : list )
        {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            sub_total_iliquido = ( preco * qtd );
            taxa = ( linha.getValorIva() / 100 );
            total += sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
        }

        return total;
    }

    public static BigDecimal getTotalVendaIVASemIncluirDesconto( List<TbItemVenda> list )
    {
        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
        BigDecimal qtd;

        total_iva = new BigDecimal( 0 );

        for ( TbItemVenda linha : list )
        {
            if ( linha.getValorIva().doubleValue() > 0 )
            {
                preco = linha.getFkPreco().getPrecoVenda();
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );

//                taxa = ( linha.getValorIva() / 100 );
                taxa = new BigDecimal( linha.getValorIva().doubleValue() ).divide( new BigDecimal( 100 ) );
//                total_iva += ( ( sub_total_iliquido * taxa ) );
                total_iva = total_iva.add( sub_total_iliquido.multiply( taxa ) );
            }
            else
            {
                BigDecimal valorIva = new BigDecimal( linha.getValorIva().doubleValue() );
//                total_iva += linha.getValorIva();
                total_iva = total_iva.add( valorIva );

            }

        }
        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_iva;
    }

    public static BigDecimal getTotalSemIvaForPayment( List<TbVenda> recibos, int pk_documento )
    {

        BigDecimal soma = new BigDecimal( 0 );
        for ( TbVenda recibo : recibos )
        {
            // soma += (venda.getTotalGeral() - venda.getTotalIva());
            //TOTAL ILÍQUIDO
            if ( recibo.getFkDocumento().getPkDocumento() == pk_documento )
            {
                soma = soma.add( getNetTotalForPayment( recibo.getAmortizacaoDividaList() ) );
            }
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += (venda.getTotalGeral() - venda.getTotalIva());
//            }

        }

        soma = soma.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return soma;
    }

    /*Vizualizar Taxa do Iva*/
//    public static double getTotalVendaIVASemIncluirDesconto( List<TbItemVenda> list )
//    {
//        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
//        double qtd = 0d;
//
//        for ( TbItemVenda linha : list )
//        {
//            if ( linha.getValorIva() > 0 )
//            {
//                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
//                qtd = linha.getQuantidade();
//                sub_total_iliquido = ( preco * qtd );
//                taxa = ( linha.getValorIva() / 100 );
//                total_iva += ( ( sub_total_iliquido * taxa ) );
//            }
//            else
//            {
//                total_iva += linha.getValorIva();
//
//            }
//
//        }
//
//        return total_iva;
//    }
//    public static double getGrossTotal( List<TbItemVenda> list )
//    {
//        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
//        double qtd = 0d;
//
//        for ( TbItemVenda linha : list )
//        {
//            if ( linha.getValorIva() > 0 )
//            {
//                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
//                qtd = linha.getQuantidade();
//                sub_total_iliquido = ( preco * qtd );
//                taxa = ( linha.getValorIva() / 100 );
//                total_iva += sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
//            }
//            else
//            {
//                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
//                qtd = linha.getQuantidade();
//                sub_total_iliquido = ( preco * qtd );
//                total_iva += sub_total_iliquido;
//
//            }
//
//        }
//
//        return total_iva;
//    }
    public static BigDecimal getGrossTotal( List<TbItemVenda> list )
    {
        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
        BigDecimal qtd;

        total_iva = new BigDecimal( 0 );
        for ( TbItemVenda linha : list )
        {
            if ( linha.getValorIva().doubleValue() > 0 )
            {
                preco = linha.getFkPreco().getPrecoVenda();
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );
                BigDecimal valorIvaLinha = new BigDecimal( linha.getValorIva().doubleValue() );
//                taxa = ( linha.getValorIva() / 100 );
                taxa = valorIvaLinha.divide( new BigDecimal( 100 ) );
//                total_iva  =   total_iva +  sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
                BigDecimal valorImposto = sub_total_iliquido.multiply( taxa );
                total_iva = total_iva.add( sub_total_iliquido.add( valorImposto ) );
            }
            else
            {
                preco = linha.getFkPreco().getPrecoVenda();
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );
                total_iva = total_iva.add( sub_total_iliquido );

            }

        }

        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_iva;
    }

//    public static double getNetTotal( List<TbItemVenda> list )
//    {
//        double taxa = 0, total_net_total = 0, preco = 0, sub_total_iliquido = 0;
//        double qtd = 0d;
//
//        for ( TbItemVenda linha : list )
//        {
//
//            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
//            qtd = linha.getQuantidade();
//            sub_total_iliquido = ( preco * qtd );
//            total_net_total += sub_total_iliquido;
//
//        }
//
//        return total_net_total;
//    }
    public static BigDecimal getNetTotal( List<TbItemVenda> list )
    {
        BigDecimal total_net_total, preco, sub_total_iliquido;
        BigDecimal qtd;
        total_net_total = new BigDecimal( 0 );

        for ( TbItemVenda linha : list )
        {

            preco = linha.getFkPreco().getPrecoVenda();
            qtd = new BigDecimal( linha.getQuantidade() );
            sub_total_iliquido = preco.multiply( qtd );
            total_net_total = total_net_total.add( sub_total_iliquido );
        }
        total_net_total = total_net_total.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_net_total;
    }

    public static double getGetGrossTotalNotas( List<NotasItem> list )
    {
        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for ( NotasItem linha : list )
        {
            if ( linha.getValorIva() > 0 )
            {
                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
                qtd = linha.getQuantidade();
                sub_total_iliquido = ( preco * qtd );
                taxa = ( linha.getValorIva() / 100 );
                total_iva += sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
            }
            else
            {
                total_iva += linha.getValorIva();

            }

        }

        return total_iva;
    }

    /*sem eventual 'desconto'*/
    public static double getTotalNotaIVASemIncluirDesconto( List<NotasItem> list )
    {
        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for ( NotasItem linha : list )
        {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            sub_total_iliquido = ( preco * qtd );
            taxa = ( linha.getValorIva() / 100 );
            total_iva += ( ( sub_total_iliquido * taxa ) );
        }

        return total_iva;
    }

    public static double getTotalIVASobreTotalIliquido( List<TbItemVenda> list, double imposto )
    {
        double taxa = ( imposto / 100 );
        return ( getTotalIliquido( list ) * taxa );
    }

    public static double getTotalNotasCreditoSemIva( List<Notas> list, int pk_documento )
    {

        double soma = 0;
        if ( !Objects.isNull( list ) )
        {
            for ( Notas nota : list )
            {
//            //soma += (venda.getTotalGeral() - venda.getTotalIva());
//            if ( nota.getFkDocumento().getPkDocumento() == pk_documento )
//            {
//                soma += ( nota.getTotalGeral() - nota.getTotalIva() );
//            }
                if ( nota.getFkDocumento().getPkDocumento() == pk_documento && !nota.getEstado().equalsIgnoreCase( DVML.ESTADO_NOTA.ANULADO.toString() ) )
                {
                    soma += nota.getTotalGeral();
                }

            }

        }

        return soma;
    }

//    public static double getTotalComIva( List<TbVenda> list, int pk_documento )
//    {
//
//        double soma = 0;
//        for ( TbVenda venda : list )
//        {
//            soma += venda.getTotalGeral();
////            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
////                soma += venda.getTotalGeral();
////            }
//
//        }
//        return soma;
//    }
    public static double getTotalComIva( List<TbVenda> list, int pk_documento )
    {

        double soma = 0;
        for ( TbVenda venda : list )
        {
            soma += venda.getTotalGeral().doubleValue();
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += venda.getTotalGeral();
//            }

        }
        return soma;
    }

    public static String[] listarPalavras( String nomeCompleto )
    {
        if ( nomeCompleto.contains( " " ) )
        {
            return nomeCompleto.split( " " );
        }

        return new String[]
        {
            nomeCompleto
        };
    }

    public static double valorCasasDecimaisNovo( double valor, int casas_dezimas )
    {

        //  DecimalFormat decimalFormat = new DecimalFormat();
        String valor_format = String.valueOf( valor ).replace( ",", "." );

        double valor_convertido = Double.parseDouble( valor_format );

        //0,07700000000000001
        //%.2f
        String formato = "%." + casas_dezimas + "f";
        String resultado = String.format( formato, valor_convertido );
        System.out.println( "ANTES RESULTADO: " + resultado );
        resultado = resultado.replace( ",", "." );
        System.out.println( "DEPOIS RESULTADO: " + resultado );
        //System.out.println(resultado);
        return Double.parseDouble( resultado );

    }

    public static int comparar_datas( Date data_1, Date data_2 )
    {
//        System.out.println( "Data 1: " + data_1.toString() );
//        System.out.println( "Data 2: " + data_2.toString() );

        Calendar calendar_1 = dateToCalendar( data_1, true );
        //calendar_1.setTime( data_1 );

        Calendar calendar_2 = dateToCalendar( data_2, true );
        //calendar_2.setTime( data_2 );

        return calendar_1.compareTo( calendar_2 );

    }

    public static boolean maior_data_1_data_2( Date data_1, Date data_2 )
    {
        if ( data_2 == null )
        {
            return true;
        }
        return comparar_datas( data_1, data_2 ) > 0;
    }

    public static boolean menor_data_1_data_2( Date data_1, Date data_2 )
    {
        if ( data_2 == null )
        {
            return true;
        }
        return comparar_datas( data_1, data_2 ) < 0;
    }

    public static boolean menor_data_1_data_2_ignore_time( Date data_1, Date data_2 )
    {

        if ( data_2 == null )
        {
            return true;
        }

        return comparar_datas( data_1, data_2 ) < 0;
    }

    public static boolean igual_data_1_data_2( Date data_1, Date data_2 )
    {
        if ( data_2 == null )
        {
            return true;
        }
        return comparar_datas( data_1, data_2 ) == 0;
    }

    public static boolean igual_data_1_data_2_ignore_time( Date data_1, Date data_2 )
    {
        if ( data_2 == null )
        {
            return true;
        }
        return data_1.equals( data_2 );
    }

    private static void controlarProcess( Process process_rodar_bat_Stream )
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                while ( process_rodar_bat_Stream.isAlive() )
                {
                    try
                    {
                        Thread.sleep( 500 );
                    }
                    catch ( InterruptedException ex )
                    {
                        Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                }
            }
        } ).run();
    }

    public static void exibirSaida( InputStream inputStream )
    {
        try
        {
            BufferedReader input = new BufferedReader( new InputStreamReader( inputStream ) );

            String linha = "";

            while ( ( linha = input.readLine() ) != null )
            {
                System.err.println( linha );
            }
        }
        catch ( IOException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static File findDirOrFile( File dir, String filename, int itarations )
    {

        do
        {
            for ( File dirOrFile : dir.listFiles() )
            {

                if ( dirOrFile.toString().contains( filename ) || dirOrFile.toString().endsWith( filename ) )
                {
                    return dirOrFile;
                }
            }

            dir = dir.getParentFile();
        }
        while ( --itarations > 0 );

        return null;
    }

    public static File findDirOrFile( String filename )
    {
        try
        {
            File dir = new File( MetodosUtil.class.getResource( "." ).toURI() );
            return findDirOrFile( dir, filename, BUSCA_DIRECTORIOS_NIVEIS_A_PERCORRER );
        }
        catch ( URISyntaxException ex )
        {
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;
    }

    public static boolean startRegukarBackupScheduledExecutorService()
    {
        try
        {
            TimeUnit timeUnit = TimeUnit.SECONDS;

            DbBackupScheduleJpaController controller = new DbBackupScheduleJpaController( em );

            DbBackupSchedule dbBackupSchedule = controller.findDbBackupSchedule( 1 );

            long anos = TimeUnit.HOURS.toSeconds( dbBackupSchedule.getAno() * ( 365 * 24 ) );
            long meses = TimeUnit.HOURS.toSeconds( dbBackupSchedule.getMes() * ( 30 * 24 ) );
            long dias = TimeUnit.HOURS.toSeconds( dbBackupSchedule.getDia() * 24 );
            long horas = TimeUnit.HOURS.toSeconds( dbBackupSchedule.getHora() );
            long minutos = TimeUnit.MINUTES.toSeconds( dbBackupSchedule.getMinuto() );
            long segundos = dbBackupSchedule.getSegundo();

            long periodo = anos + meses + dias + horas + minutos + segundos;
            System.err.println( "periodo:  " + periodo );

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool( 1 );
            executorService.scheduleAtFixedRate( fazerBackup(), 8, periodo, timeUnit );

            return true;
        }
        catch ( Exception exception )
        {
            exception.printStackTrace();
            return false;
        }

    }

    public static TimerTask fazerBackupTimerTask()
    {
        return new TimerTask()
        {

            @Override
            public void run()
            {
                fazerBackupAgora();
            }
        };
    }

//    public static void fazerBackupAgora()
//    {
//        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
////        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
////String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );
//
////        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
//        System.err.println( "Backup realizado com sucesso! " );
//
//    }
    public static void fazerBackupAgora()
    {
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

    public static Process rodarComandoWindows( String comando, boolean waitFor )
    {
        Process process_rodar_bat_Stream = null;

        try
        {
            Runtime terminal = Runtime.getRuntime();

            process_rodar_bat_Stream = terminal.exec( comando );
            InputStream inputStream = process_rodar_bat_Stream.getInputStream();

            if ( waitFor )
            {
                process_rodar_bat_Stream.waitFor();
            }

            exibirSaida( inputStream );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( BDBackupJFrame.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( InterruptedException ex )
        {
            Logger.getLogger( BDBackupJFrame.class.getName() ).log( Level.SEVERE, null, ex );
        }
        finally
        {
            return process_rodar_bat_Stream;
        }

    }

    public static boolean startBackGroundProcesses()
    {

        //START PERIODIC BACKUPS
        System.err.println( "STARTING BACKGROUND PROCESSES...." );
        System.err.println( "STARTING PERIODIC BACKUPS...." );
        boolean resposta = startRegukarBackupScheduledExecutorService();
        System.err.println( "PERIODIC BACKUPS TARTED...." );
        System.err.println( "BACKGROUND PROCESSES STARTED...." );

        return resposta;
    }

    public void startRegukarBackup1()
    {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();

        calendar.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
        calendar.set( Calendar.HOUR_OF_DAY, 13 );
        calendar.set( Calendar.MINUTE, 40 );
        calendar.set( Calendar.MILLISECOND, 0 );

        timer.schedule( fazerBackupTimerTask(), calendar.getTime(), TimeUnit.SECONDS.toMillis( 8 ) );

    }

    public static Runnable fazerBackup()
    {
        return new Runnable()
        {

            @Override
            public void run()
            {
                fazerBackupAgora();
            }
        };
    }

    public static List<TbProduto> getProdutosIsentos( List<TbItemVenda> linhas )
    {

        List<TbProduto> produtos = new ArrayList<>();
        for ( TbItemVenda linha : linhas )
        {
            if ( linha.getValorIva() == 0d )
            {
                produtos.add( linha.getCodigoProduto() );
            }
        }

        return produtos;
    }

    public static List<TbProduto> getProdutosIsentosCompras1( List<ItemCompras> linhas )
    {

        List<TbProduto> produtos = new ArrayList<>();
        for ( ItemCompras linha : linhas )
        {
            if ( linha.getValorIva() == 0d )
            {
                produtos.add( linha.getFkProduto() );
            }
        }

        return produtos;
    }

    public static List<TbProduto> getProdutosIsentosNotas( List<NotasItem> linhas )
    {

        List<TbProduto> produtos = new ArrayList<>();
        for ( NotasItem linha : linhas )
        {
            if ( linha.getValorIva() == 0d )
            {
                produtos.add( linha.getFkProduto() );
            }
        }

        return produtos;
    }

    public static List<TbProduto> getProdutosIsentos1( List<NotasItem> linhas )
    {

        List<TbProduto> produtos = new ArrayList<>();
        for ( NotasItem linha : linhas )
        {
            if ( linha.getValorIva() == 0d )
            {
                produtos.add( linha.getFkProduto() );
            }
        }

        return produtos;
    }

    public static List<TbProduto> getProdutosIsentosCompras( List<ItemCompras> linhas )
    {

        List<TbProduto> produtos = new ArrayList<>();
        for ( ItemCompras linha : linhas )
        {
            if ( linha.getValorIva() == 0d )
            {
                produtos.add( linha.getFkProduto() );
            }
        }

        return produtos;
    }

    public static String getMotivoIsensaoProdutos( List<TbProduto> list_produto )
    {
        String motivo = "", motivo_linha, linha;;
//        String motivo = "IVA-Regime Geral", motivo_linha, linha;;
//        String motivo = "Regime Transitório", motivo_linha, linha;;
//        if ( list_produto != null )
//        {
//            motivo = DVML.ISENTO_COM_BASE_NO_ARTIGO;
//            motivo += " ";
//            lista_alienas.clear();
//            /*adicionando todas as alíenas na lista alíneas*/
//            for ( TbProduto produto : list_produto )
//            {
//                motivo_linha = getMotivoIsensao( produto.getCodigo() );
//                linha = getLinhaArtigo( motivo_linha );
//                if ( !lista_alienas.contains( linha ) )
//                {
//                    lista_alienas.add( linha );
//                }
//                
//            }
//            //ordenar as alíneas
//            ordenar_lista( lista_alienas );
//
//            /*construindo a linha dos motivos para exportar no documento.*/
//            for ( int j = 0; j < lista_alienas.size(); j++ )
//            {
//                linha = lista_alienas.get( j );
//                if ( lista_alienas.size() == 1 )
//                {
//                    motivo += "<b>" + linha + ")</b> do CIVA. ";
//                }
//                else if ( ( j + 1 ) < lista_alienas.size() )
//                {
//                    motivo += "<b>" + linha + ")</b> , ";
//                }
//                else
//                {
//                    motivo += " e <b>" + linha + ")</b> do CIVA. ";
//                }
//                
//            }
//            //esporta a linha
//        }
//        
        return motivo;
    }

    private static String getLinhaArtigo( String motivo )
    {
        int id_parenteses = 0;
        id_parenteses = motivo.indexOf( ")" );
        return "" + motivo.charAt( id_parenteses - 1 );
    }

    public static void mostrar()
    {
        lista_alienas.add( "b)" );
        lista_alienas.add( "c)" );
        lista_alienas.add( "a)" );
        lista_alienas.add( "e)" );
        ordenar_lista( lista_alienas );

        System.err.println( lista_alienas.toString() );

//        String text_mensagem = "Isento com base no artigo nº 12º. a) do CIVA";
//        System.err.println( "Endereço: " + text_mensagem.indexOf( ")" ) );
//        System.err.println( "Caracter: " + text_mensagem.substring( text_mensagem.indexOf( ")" ) ) );
    }

    private static List<String> ordenar_lista( List<String> list_data )
    {
        Collections.sort( list_data, new Comparator<String>()
        {
            @Override
            public int compare( String o1, String o2 )
            {
                return o1.compareTo( o2 );
            }
        } );

        return list_data;

    }

    public static boolean acesso_sistema()
    {
        AnoEconomico anoEconomico = anoEconomicoDao.getLastObject();
        int ano_economico = Integer.parseInt( anoEconomico.getDesignacao() );
        int ano_sistema_operativo = ( new Date().getYear() + 1900 );

        return ( ano_economico <= ano_sistema_operativo );
    }

    public static String getHash( String formato_documento )
    {
        String valor = "" + DVML.CHAVE_PRIVADA + formato_documento.trim().replace( " ", "" );
        String hexa = "";
        //String s = "FR 2020/2";
        try
        {
            //MessageDigest m = MessageDigest.getInstance( "MD5" );
            MessageDigest m = MessageDigest.getInstance( "SHA-256" );

            m.update( valor.getBytes(), 0, valor.length() );

            byte[] digest = m.digest();

            hexa = new BigInteger( 1, digest ).toString( 16 ).toUpperCase();

            System.out.println( "MD5: " + hexa );
            System.out.println( "TAMANHO: " + hexa.length() );
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        return hexa;

    }

    public static byte[] gerarHash( String frase, String algoritmo )
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance( algoritmo );
            md.update( frase.getBytes() );
            return md.digest();
        }
        catch ( NoSuchAlgorithmException e )
        {
            return null;
        }
    }

    private static String stringHexa( byte[] bytes )
    {
        StringBuilder s = new StringBuilder();
        for ( int i = 0; i < bytes.length; i++ )
        {
            int parteAlta = ( ( bytes[ i ] >> 4 ) & 0xf ) << 4;
            int parteBaixa = bytes[ i ] & 0xf;
            if ( parteAlta == 0 )
            {
                s.append( '0' );
            }
            s.append( Integer.toHexString( parteAlta | parteBaixa ) );
        }
        return s.toString();
    }

    private static void inserir_produtos_all_regime_transitorio( BDConexao conexao )
    {

        List<TbProduto> list_produto = stockDao.getAllProdutosByIdArmazem( 1 );
        for ( TbProduto produto : list_produto )
        {
            inserir_regimetransitorio( produto.getCodigo(), conexao );
        }

    }

    private static boolean inserir_regimetransitorio( int pk_produto, BDConexao conexao )
    {
        String sql = "INSERT INTO produto_isento(fk_produto, fk_produtos_motivos_isensao) VALUES(" + pk_produto + ", 3)";
        System.out.println( sql );
        return conexao.executeUpdate( sql );
    }
//    private static boolean inserir_regimetransitorio( int pk_produto, BDConexao conexao )
//    {
//        String sql = "INSERT INTO produto_isento(fk_produto, fk_produtos_motivos_isensao) VALUES(" + pk_produto + ", 3)";
//        System.out.println( sql );
//        return conexao.executeUpdate( sql );
//    }

    public static boolean exist_produto_tabela_formulario( JTable tabela, int codigo )
    {

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( tabela.getValueAt( i, 0 ) ) ) == codigo )
            {
                CompraVisao.linha_actual = i;
                return true;
            }
        }
        return false;

    }

//    public static double getValorComIVA( double qtd, double taxa, double preco, double desconto )
//    {
//        double subtotal_linha = (preco * qtd);
//        double desconto_valor = (subtotal_linha * ( desconto / 100 ));
//        double valor_iva = 1 + ( taxa / 100 );//
//        return ( ( subtotal_linha - desconto_valor ) * valor_iva );
//    }
    public static double getValorComIVA( double qtd, double taxa, double preco, double desconto )
    {
        BigDecimal precoBD = BigDecimal.valueOf( preco );
        BigDecimal qtdBD = BigDecimal.valueOf( qtd );
        BigDecimal subtotal = precoBD.multiply( qtdBD );
        BigDecimal descontoBD = subtotal.multiply( BigDecimal.valueOf( desconto ).divide( BigDecimal.valueOf( 100 ) ) );
        BigDecimal base = subtotal.subtract( descontoBD );
        BigDecimal iva = BigDecimal.ONE.add( BigDecimal.valueOf( taxa ).divide( BigDecimal.valueOf( 100 ) ) );
        BigDecimal total = base.multiply( iva );
        return total.setScale( 2, RoundingMode.HALF_UP ).doubleValue();
    }

    public static BigDecimal getValorIliquido( BigDecimal qtd, BigDecimal precoVenda, BigDecimal desconto )
    {
        // subtotal = preço × quantidade
        BigDecimal subtotal = precoVenda.multiply( qtd );

        // desconto = subtotal × (desconto / 100)
        BigDecimal descontoValor = subtotal.multiply( desconto )
                .divide( BigDecimal.valueOf( 100 ), 2, RoundingMode.HALF_UP );

        // valor ilíquido = subtotal - desconto
        BigDecimal valorIliquido = subtotal.subtract( descontoValor );
        // retorna com 2 casas decimais
        return valorIliquido.setScale( 2, RoundingMode.HALF_UP );
    }

    public static double getValorComRetencao( double qtd, double ret, double preco_venda, double desconto )
    {
        double subtotal_linha = ( preco_venda * qtd );
        double desconto_valor = ( subtotal_linha * ( desconto / 100 ) );
        double valor_ret = ( ( ( subtotal_linha - desconto_valor ) * ret ) / 100 );//
        return ( valor_ret );
    }

    public static String iniciais_extenso( int idDocumento, DocumentoDao documentoDao )
    {

        Documento documento_local = documentoDao.findDocumento( idDocumento );
        String abreviacao_local = documento_local.getAbreviacao();

        switch ( abreviacao_local )
        {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            case "CO":
                return "Compramos no valor de: ";
            default:
                return "São: ";
        }
    }

    public static String iniciais_extenso_comercial( int idDocumento, DocumentosController documentosController )
    {

        Documento documento_local = (Documento) documentosController.findById( idDocumento );
        String abreviacao_local = documento_local.getAbreviacao();

        switch ( abreviacao_local )
        {
            case "FT":
                return "Facturamos o valor de: ";
            case "FR":
                return "Recebemos a quantia de: ";
            case "CO":
                return "Compramos no valor de: ";
            default:
                return "São: ";
        }
    }

    public static void precedimento_actualizar_produto()
    {
        List<TbProduto> lista_produto = produtoDao.buscaTodosProdutos();
        BDConexao conexao = new BDConexao();
        System.err.println( "PRODUTO A ADICONADOS" );
        int cont = 1;
        for ( TbProduto produto : lista_produto )
        {
            System.err.println( "Nº: " + cont + " de " + lista_produto.size() );
            System.err.println( "ID: " + produto.getCodigo() );
            System.err.println( "Designacao: " + produto.getDesignacao() );
            System.err.print( "Stock: " );
            if ( !stockDao.exist_produto_stock( produto.getCodigo(), DVML.ARMAZEM_DEFAUTL ) )
            {
                adicionar_produto_stock( produto, conexao );
                System.err.println( "adicionado" );
            }
            else
            {
                System.err.println( "existente" );
            }
            System.err.print( "Preco: " );
            if ( precoDao.exist_preco_produto( produto.getCodigo() ) )
            {
                adicionar_preco_produto( produto, conexao );
                System.err.println( "adicionado" );
            }
            else
            {
                System.err.println( "existente" );
            }
            cont++;
        }

    }

    private static void adicionar_produto_stock( TbProduto produto, BDConexao conexao )
    {
        int idUser = 15;
        TbStock stock = new TbStock();
        stock.setDataEntrada( new Date() );
        stock.setQuantidadeExistente( 0d );
        stock.setStatus( "Activo" );
        stock.setPrecoVenda( new BigDecimal( "100" ) );
        stock.setPrecoVendaGrosso( new BigDecimal( stock.getPrecoVenda().doubleValue() ) );
        stock.setQtdGrosso( DVML.QTD_DEFAULT );
        stock.setQuantCritica( 10 );
        stock.setQuantBaixa( 0 );
        stock.setQuantidadeAntiga( 0d );
        stock.setCodArmazem( armazemDao.findTbArmazem( DVML.ARMAZEM_DEFAUTL ) );
        stock.setCodProdutoCodigo( produto );

        try
        {

            salvar_stock( stock, idUser, conexao );
        }
        catch ( Exception e )
        {
        }

    }

    private static void adicionar_preco_produto( TbProduto produto, BDConexao conexao )
    {
        int idUser = 15;

        TbPreco preco_local_1 = new TbPreco();
        TbPreco preco_local_2 = new TbPreco();

        //PRIMEIRO PRECO
        preco_local_1.setPrecoCompra( new BigDecimal( "100" ) );
        preco_local_1.setPercentagemGanho( new BigDecimal( "0" ) );
        preco_local_1.setData( new Date() );
        preco_local_1.setHora( new Date() );
        preco_local_1.setFkProduto( produto );
        preco_local_1.setFkUsuario( usuarioDao.findTbUsuario( 15 ) );
        preco_local_1.setQtdBaixo( 1 );
        preco_local_1.setQtdAlto( (int) DVML.QTD_DEFAULT - 1 );
        preco_local_1.setPrecoVenda( new BigDecimal( "150" ) );

        try
        {

            salvar_preco( preco_local_1, idUser, conexao );
        }
        catch ( Exception e )
        {
        }
        //Criar o primeiro preço

        //SECGUNDO PRECO PRECO
        preco_local_2.setPrecoCompra( new BigDecimal( "100" ) );
        preco_local_2.setPercentagemGanho( new BigDecimal( "0" ) );
        preco_local_2.setData( new Date() );
        preco_local_2.setHora( new Date() );
        preco_local_2.setFkProduto( produto );
        preco_local_2.setFkUsuario( usuarioDao.findTbUsuario( 15 ) );
        preco_local_2.setQtdBaixo( 1 );
        preco_local_2.setQtdAlto( (int) DVML.QTD_DEFAULT - 1 );
        preco_local_2.setPrecoVenda( new BigDecimal( "150" ) );
        preco_local_2.setQtdBaixo( (int) DVML.QTD_DEFAULT );
        preco_local_2.setQtdAlto( 2147483647 );
        preco_local_2.setPrecoVenda( new BigDecimal( "150" ) );

        try
        {
            //Criar o segundo preço
            salvar_preco( preco_local_2, idUser, conexao );

        }
        catch ( Exception e )
        {
        }

    }

    public static String iniciais_maiuscula( String nome )
    {

        //trimAll(nome)
        String nome_trasformado = "";

        nome_trasformado = nome.substring( 0, 1 ).toUpperCase();
        int i = 1;
        while ( i < nome.length() )
        {

            if ( nome.substring( i, i + 1 ).equals( " " ) )
            {
                nome_trasformado += nome.substring( i, i + 2 ).toUpperCase();
                i = i + 2;
            }
            else
            {
                nome_trasformado += nome.substring( i, i + 1 ).toLowerCase();
                i = i + 1;
            }

        }

        return nome_trasformado;

    }

    public static String trimAll( String text )
    {
        String string = text.trim();
        while ( string.contains( "  " ) )
        {
            string = string.replaceAll( "  ", " " );
        }
        return string;
    }

    public static void precedimento_preenchecer_campos_vazios_produto()
    {
        List<TbProduto> lista_produto = produtoDao.buscaTodosProdutosComCampoVazios();
        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
        ProdutoDao produtoLocalDao = new ProdutoDao( emf );
        BDConexao conexao = new BDConexao();
        System.err.println( "TAMANHO: " + lista_produto.size() );
        int cont = 1;
        String designacao = "Produto ";
        for ( TbProduto produto : lista_produto )
        {
            produto.setDesignacao( designacao + " " + cont );
            try
            {
                produtoLocalDao.edit( produto );
                System.out.println( "Produto: " + produto.getDesignacao() );
            }
            catch ( Exception e )
            {
            }

            cont++;
        }

    }

    public static int getDias()
    {
        int dias = 0;

        String FORMATO_DATA = "dd/MM/yyyy";
        String DATA_STRING_1 = "09/07/2020";
        String DATA_STRING_2 = "10/07/2020";

        SimpleDateFormat sdf = new SimpleDateFormat( FORMATO_DATA );
        Calendar data_1 = Calendar.getInstance();
        Calendar data_2 = Calendar.getInstance();

        try
        {
            data_1.setTime( sdf.parse( DATA_STRING_1 ) );
            data_2.setTime( sdf.parse( DATA_STRING_2 ) );
        }
        catch ( Exception e )
        {
        }

        dias = data_2.get( Calendar.DAY_OF_YEAR ) - data_1.get( Calendar.DAY_OF_YEAR );
        System.err.println( "Data 1: " + DATA_STRING_1 );
        System.err.println( "Data 2: " + DATA_STRING_2 );
        System.err.println( "Direrença: " + dias );
        return dias;

    }

    public static int getDias( Date date_1, Date date_2 )
    {
        int dias = 0;

        String FORMATO_DATA = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat( FORMATO_DATA );
        Calendar data_1 = Calendar.getInstance();
        Calendar data_2 = Calendar.getInstance();

        try
        {
            data_1.setTime( date_1 );
            data_2.setTime( date_2 );
        }
        catch ( Exception e )
        {
        }

        dias = data_2.get( Calendar.DAY_OF_YEAR ) - data_1.get( Calendar.DAY_OF_YEAR );
        System.err.println( "Direrença: " + dias );
        return dias;

    }

    public static Date stringToDate( String stringDate )
    {
        Date date = null;
        String FORMATO_DATA = "dd/MM/yyyy";
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat( FORMATO_DATA );
            date = sdf.parse( stringDate );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return date;

    }

    public static Date stringToDate( String stringDate, String formato )
    {
        Date date = null;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat( formato );
            date = sdf.parse( stringDate );
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        return date;

    }

    public static int getTotalDiasNormal( Date initialDate, Date finalDate )
    {
        if ( initialDate == null || finalDate == null )
        {
            return 0;
        }
        int days = (int) ( ( finalDate.getTime() - initialDate.getTime() ) / ( 24 * 60 * 60 * 1000 ) );

        return ( days > 0 ? days : 0 );
    }

    public static int getDiasUteis( Date initialDate, Date finalDate, int idModalidade )
    {
        int diasTrabalho = 0;
        // int totalDias = getTotalDiasNormal( initialDate, finalDate );
        Calendar calendar = new GregorianCalendar( finalDate.getYear(), finalDate.getMonth(), finalDate.getDay() );
        //System.err.println( "************ INICIO CONTAGEM ***********" );
        Date dateInicio = initialDate;
        Date dateFim = finalDate;
        while ( menor_data_1_data_2( dateInicio, dateFim ) || igual_data_1_data_2( dateInicio, dateFim ) )
        {
            calendar.setTime( dateInicio );
            switch ( idModalidade )
            {
                case 1:
                    if ( !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ) && !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) )
                    {
                        diasTrabalho++;
                    }
                    break;
                case 2:
                    if ( !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ) )
                    {
                        diasTrabalho++;
                    }
                    break;
                default:
                    diasTrabalho++;
                    break;
            }

            calendar.add( Calendar.DATE, 1 );
            dateInicio = calendar.getTime();
        }
        //System.err.println( "************ FIM CONTAGEM ***********" );
        return diasTrabalho;

    }

    public static int getDiasUteisAviso( Date initialDate, Date finalDate, int idModalidade )
    {
        int diasTrabalho = 0;
        // int totalDias = getTotalDiasNormal( initialDate, finalDate );
        Calendar calendar = new GregorianCalendar( finalDate.getYear(), finalDate.getMonth(), finalDate.getDay() );
        //System.err.println( "************ INICIO CONTAGEM ***********" );
        Date dateInicio = initialDate;
        Date dateFim = finalDate;
        while ( menor_data_1_data_2( dateInicio, dateFim ) || igual_data_1_data_2( dateInicio, dateFim ) )
        {
            calendar.setTime( dateInicio );
            switch ( idModalidade )
            {
                case 1:
                    if ( !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ) && !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ) )
                    {
                        diasTrabalho++;
                    }
                    break;
                case 2:
                    if ( !( calendar.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ) )
                    {
                        diasTrabalho++;
                    }
                    break;
                default:
                    diasTrabalho++;
                    break;
            }

            calendar.add( Calendar.DATE, 1 );
            dateInicio = calendar.getTime();
        }
        //System.err.println( "************ FIM CONTAGEM ***********" );
        return diasTrabalho;

    }

    public static Calendar dateToCalendar( Date date, boolean setTimeToZero )
    {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime( date );
        if ( setTimeToZero )
        {
            calendario.set( Calendar.HOUR_OF_DAY, 0 );
            calendario.set( Calendar.MINUTE, 0 );
            calendario.set( Calendar.SECOND, 0 );
            calendario.set( Calendar.MILLISECOND, 0 );
        }
        return calendario;
    }

    public static Date adicionarUmDiasNaData( Date date, int numero_dias )
    {
        Calendar calendar_comeco = Calendar.getInstance();
        calendar_comeco.setTime( date );
        calendar_comeco.add( Calendar.DATE, +numero_dias );
        return calendar_comeco.getTime();

    }

    public static void showMessageUtil( String mensagen, String tipo_mensagem )
    {

        if ( tipo_mensagem.equals( TIPO_MENSAGEM_AVISO ) )
        {
            JOptionPane.showMessageDialog( null, mensagen, TIPO_MENSAGEM_AVISO, JOptionPane.WARNING_MESSAGE );
        }
        else if ( tipo_mensagem.equals( TIPO_MENSAGEM_ERRO ) )
        {
            JOptionPane.showMessageDialog( null, mensagen, TIPO_MENSAGEM_ERRO, JOptionPane.ERROR_MESSAGE );
        }
        else if ( tipo_mensagem.equals( TIPO_MENSAGEM_INFOR ) )
        {
            JOptionPane.showMessageDialog( null, mensagen, TIPO_MENSAGEM_INFOR, JOptionPane.INFORMATION_MESSAGE );
        }

    }

    /**
     * @autor: Domingos Dala Vunge
     * @param emf
     * @param idFuncionario
     * @return
     */
    public static double getTotalDiasTrabalhado( EntityManagerFactory emf, int idFuncionario, BDConexao conexao )
    {
        FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
        FaltaDao faltaDao = new FaltaDao( emf );
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( idFuncionario );
        Date dataActual = new Date();
        double diasUteisTrabalho = 0, diasDeFalta = 0;

        /**
         * 1º Caso: CONTRATO DETERMINADO
         */
        if ( funcionario.getTipoContrato().equals( CONTRATRO_DETERMINADO ) )
        {

            Date dataInicioContrato = funcionario.getDataInicioContrato();
            Date dataFimContrato = funcionario.getDataFimContrato();
            /*Verifica se a data actual está no intervalo da data do inicio do contrato e do fim*/
            if ( menor_data_1_data_2( dataInicioContrato, dataActual ) || igual_data_1_data_2( dataInicioContrato, dataActual ) )
            {
                if ( menor_data_1_data_2( dataActual, dataFimContrato ) || igual_data_1_data_2( dataActual, dataFimContrato ) )
                {
                    // diasDeFalta = conversaoHoraEmDia( faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataInicioContrato, dataActual, false ) );

                    System.err.println( "Data de Inicio:::: " + getDataString2( dataInicioContrato ) );
                    System.err.println( "Data de Fim:::: " + getDataString2( dataActual ) );
                    double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataInicioContrato, dataActual, false, conexao );
                    System.err.println( "Nº Horas de Falta: " + numerosHorasFaltadas );
                    diasDeFalta = conversaoHoraEmDia( numerosHorasFaltadas );
                    System.err.println( "Nº de Horas de Convertidas de faltas: " + diasDeFalta );
                    diasUteisTrabalho = getDiasUteis( dataInicioContrato, dataActual, funcionario.getFkModalidade().getPkModalidade() ) - diasDeFalta;
                }

            }

        }
        /**
         * 2º Caso: CONTRATO INDERTERMINADO
         */
        else
        {
            Date dataContrato = funcionario.getDataContrato();
            int anoActual = ( dataActual.getYear() + 1900 );
            int anoContrato = ( dataContrato.getYear() + 1900 );

//
            Date dataComeco = ( anoActual == anoContrato ) ? funcionario.getDataContrato() : stringToDate( "01/01/" + anoActual );
            System.err.println( "Data de Inicio:::: " + getDataString2( dataComeco ) );
            System.err.println( "Data de Fim:::: " + getDataString2( dataActual ) );
            double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataContrato, dataActual, false, conexao );
            System.err.println( "Nº Horas de Falta: " + numerosHorasFaltadas );
            diasDeFalta = conversaoHoraEmDia( numerosHorasFaltadas );
            diasUteisTrabalho = getDiasUteis( dataComeco, dataActual, funcionario.getFkModalidade().getPkModalidade() ) - diasDeFalta;

        }

        return diasUteisTrabalho;
    }

    public static double conversaoHoraEmDia( double horas )
    {
        return ( horas / DVML.HORA_UTIL_TRABALHO );
    }

    public double getRemurecaoDiario( EntityManagerFactory emf, int idFuncionario )
    {
        SalarioDao salarioDao = new SalarioDao( emf );
        FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( idFuncionario );
        double salarioBase = salarioDao.getSalarioByIdFuncionario( idFuncionario );
        //dias possíveis de trabalho do funcionário  na instituição.
        int diasPossiveisTrabalho = funcionario.getFkModalidade().getDiasUteisTrabalho();
        //valor a receber mensal 
        double remunuracaoMensal = ( salarioBase / 12 );
        //valor a receber diário
        double remuneracaoDiario = ( remunuracaoMensal / diasPossiveisTrabalho );

        return remuneracaoDiario;

    }

    public static int getTotalQtdEntrada( BDConexao conexao, int id_armazem, int cod_produto, String data )
    {

//        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
//        String sql = "SELECT SUM(quantidade) AS TOTAL FROM  compras WHERE  idArmazemFK = " + id_armazem + " AND data_compra = '" + data + "' AND idProduto = " + cod_produto;
        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  item_compras i, compras v WHERE  v.idArmazemFK = " + id_armazem + " AND i.fk_compra = v.pk_compra AND DATE(v.data_compra) = '" + data + "' AND fk_produto = " + cod_produto + " AND v.status_eliminado = 'false' ";
//        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static int getTotalQtdTransferidos( BDConexao conexao, int id_armazem, int cod_produto, String data )
    {

//        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
//        String sql = "SELECT SUM(quantidade) AS TOTAL FROM  compras WHERE  idArmazemFK = " + id_armazem + " AND data_compra = '" + data + "' AND idProduto = " + cod_produto;
        String sql = "SELECT SUM(l.qtd) AS TOTAL FROM  linha_tranferencia l, transferencia_armazem t WHERE  l.fk_armazem_destino = " + id_armazem + " AND l.fk_transferencia_armazem = t.pk_transferencia_armazem AND DATE(t.data_hora) = '" + data + "' AND fk_produto = " + cod_produto + " AND v.status_eliminado = 'false' ";
//        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static int getTotalQtdVendida( BDConexao conexao, int id_armazem, int id_produto, String data )
    {

        String sql = "SELECT SUM(i.quantidade) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND DATE(v.dataVenda) = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static int getTotalQtdAnteiror( BDConexao conexao, int id_produto, String data )
    {

        String sql = "SELECT SUM(quantidade_existente) AS TOTAL FROM  tb_stock_mirrow WHERE data_feicho = '" + data + "' AND cod_produto_codigo = " + id_produto;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "TOTAL" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    public static double getDescontoByProduto( BDConexao conexao, int id_armazem, int id_produto, String data )
    {

        String sql = "SELECT SUM(i.desconto) AS TOTAL FROM  tb_item_venda i, tb_venda v WHERE  v.idArmazemFK = " + id_armazem + " AND i.codigo_venda = v.codigo AND v.dataVenda = '" + data + "' AND codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' ";
        ResultSet rs = conexao.executeQuery( sql );

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

    public static boolean actualizar_existencia_anterior( BDConexao conexao )
    {

        List<TbStock> list = getAllStock( conexao );
        TbStockMirrow stockMirrow;

        for ( TbStock stock : list )
        {

            stockMirrow = new TbStockMirrow();
//            stockMirrow.setCodArmazem( stock.getCodArmazem() );
//            stockMirrow.setCodProdutoCodigo( stock.getCodProdutoCodigo() );
////            stockMirrow.setDataEntrada( stock.getDataEntrada() );
//            stockMirrow.setQuantidadeExistente( stock.getQuantidadeExistente() );
//            stockMirrow.setStatus( stock.getStatus() );
////            stockMirrow.setPrecoVenda( stock.getPrecoVenda().doubleValue() );
//            stockMirrow.setPrecoVenda( stock.getPrecoVenda() );
//            stockMirrow.setQuantCritica( stock.getQuantCritica() );
//            stockMirrow.setQuantBaixa( stock.getQuantBaixa() );
//            stockMirrow.setQuantidadeAntiga( stock.getQuantidadeAntiga() );
//            stockMirrow.setPrecoVendaGrosso( (float) stock.getPrecoVendaGrosso().doubleValue() );
////            stockMirrow.setPrecoVendaFabrica( stock.getPrecoVendaFabrica().doubleValue() );
//            stockMirrow.setPrecoVendaFabrica( stock.getPrecoVendaFabrica().doubleValue() );
//            stockMirrow.setDataEntrada( stock.getDataEntrada() );
            stockMirrow.setDataFeicho( new Date() );

            try
            {
                stockMirrowDao.create( stockMirrow );
            }
            catch ( Exception e )
            {
                return false;
            }
        }
        return true;

    }

    public static List<TbStock> getAllStock( BDConexao conexao )
    {

        String sql = "SELECT  DISTINCT   *  FROM  tb_stock s";
        ResultSet rs = conexao.executeQuery( sql );

        System.err.println( "CHEGUEI AQUI" );
        List<TbStock> lista = new ArrayList<>();
        TbStock object;
        try
        {
            while ( rs.next() )
            {

                object = new TbStock();

//                  stockMirrow.setCodArmazem( stock.getCodArmazem()  );
//                 stockMirrow.setCodProdutoCodigo( stock.getCodProdutoCodigo());
//                 stockMirrow.setDataEntrada(  stock.getDataEntrada() );
//                 stockMirrow.setQuantidadeExistente( stock.getQuantidadeExistente());
//                 stockMirrow.setStatus( stock.getStatus());
//                 stockMirrow.setPrecoVenda( stock.getPrecoVenda());
//                 stockMirrow.setQuantCritica( stock.getQuantCritica());
//                 stockMirrow.setQuantBaixa( stock.getQuantBaixa());
//                 stockMirrow.setQuantidadeAntiga( stock.getQuantidadeAntiga());
//                 stockMirrow.setPrecoVendaGrosso( stock.getPrecoVendaGrosso());
//                 stockMirrow.setQtdGrosso( stock.getQtdGrosso());
//                 stockMirrow.setPrecoVendaFabrica( stock.getPrecoVendaFabrica());
//                 stockMirrow.setDataEntrada( stock.getDataEntrada());
//                 stockMirrow.setDataFeicho( new Date() );
                object.setCodigo( rs.getInt( "codigo" ) );
                object.setCodArmazem( armazemDao.findTbArmazem( rs.getInt( "cod_armazem" ) ) );
                object.setCodProdutoCodigo( produtoDao.findTbProduto( rs.getInt( "cod_produto_codigo" ) ) );
                object.setDataEntrada( rs.getDate( "dataEntrada" ) );
                object.setQuantidadeExistente( rs.getDouble( "quantidade_existente" ) );
                object.setStatus( rs.getString( "status" ) );
                object.setPrecoVenda( new BigDecimal( rs.getDouble( "preco_venda" ) ) );
                object.setQuantCritica( rs.getInt( "quant_critica" ) );
                object.setQuantBaixa( rs.getInt( "quant_baixa" ) );
                object.setQuantidadeAntiga( rs.getDouble( "quantidade_antiga" ) );
                object.setPrecoVendaGrosso( new BigDecimal( rs.getDouble( "preco_venda_grosso" ) ) );
                object.setQtdGrosso( rs.getDouble( "qtd_grosso" ) );
                object.setPrecoVendaFabrica( new BigDecimal( rs.getDouble( "preco_venda_fabrica" ) ) );

                lista.add( object );

            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            lista.clear();
            return lista;
        }
        return lista;
    }

    public static List<TbProduto> getAllProdutosByIdClienteAndIdArmazem( String data_1, String data_2, int id_armazem, int id_cliente, BDConexao conexao )
    {

        String sql = "SELECT i.codigo_produto FROM  tb_item_venda i, tb_venda v WHERE  i.codigo_venda = v.codigo AND v.dataVenda BETWEEN '" + data_1 + "' AND '" + data_2 + "' AND v.idArmazemFK = " + id_armazem + " AND v.codigo_cliente = " + id_cliente + " AND v.status_eliminado = 'false' GROUP BY i.codigo_produto ASC";
        ResultSet rs = conexao.executeQuery( sql );

        List<TbProduto> lista = new ArrayList<>();
        TbProduto object;
        try
        {
            while ( rs.next() )
            {
                object = produtoDao.findTbProduto( rs.getInt( "i.codigo_produto" ) );
                lista.add( object );
            }

        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            lista.clear();
            return lista;
        }
        return lista;

    }

    public static double getPreco( String data_1, String data_2, int id_armazem, int id_cliente, int id_produto, BDConexao conexao )
    {

        String sql = "SELECT i.fk_preco AS id_preco FROM  tb_item_venda i, tb_venda v "
                + "WHERE  i.codigo_venda = v.codigo AND v.dataVenda BETWEEN '" + data_1 + "' AND '" + data_2 + "' "
                + "AND v.idArmazemFK = " + id_armazem
                + " AND v.codigo_cliente = " + id_cliente + " AND i.codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' GROUP BY i.fk_preco ASC";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return precoDao.findTbPreco( rs.getInt( "id_preco" ) ).getPrecoVenda().doubleValue();
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }
        return 0;

    }

    public static int getQTD( String data_1, String data_2, int id_armazem, int id_cliente, int id_produto, BDConexao conexao )
    {

        String sql = "SELECT SUM(i.quantidade) AS QTD FROM  tb_item_venda i, tb_venda v "
                + "WHERE  i.codigo_venda = v.codigo AND v.dataVenda BETWEEN '" + data_1 + "' AND '" + data_2 + "' "
                + "AND v.idArmazemFK = " + id_armazem
                + " AND v.codigo_cliente = " + id_cliente + " AND i.codigo_produto = " + id_produto + " AND v.status_eliminado = 'false' GROUP BY i.codigo_produto ASC";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "QTD" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }
        return 0;

    }

    public static void limparCampoForm( Object campo )
    {
        if ( campo instanceof JLabel )
        {
            ( (JLabel) campo ).setText( "" );
        }
        else if ( campo instanceof JTextField )
        {
            ( (JTextField) campo ).setText( "" );
        }

    }

    public static boolean tabela_vazia( JTable tabela )
    {
        DefaultTableModel modelo = preparar_tabela( tabela );
        return modelo.getRowCount() == 0;
    }

    public static DefaultTableModel preparar_tabela( JTable tabela )
    {
        return (DefaultTableModel) tabela.getModel();
    }

    public static boolean remover_item_tabela( JTable tabela, int linha )
    {
        try
        {
            preparar_tabela( tabela ).removeRow( linha );
            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public static int getConfirmacaoDialog( String message )
    {
        return JOptionPane.showConfirmDialog( null, message, "Aviso", JOptionPane.YES_OPTION );
    }

    public static void remover_itens_carrinho( JTable tabela )
    {
        DefaultTableModel modelo = preparar_tabela( tabela );
        modelo.setRowCount( 0 );

    }

    public static void FUNCAO_F1( JFrame frame, Boolean modal, int idArmazem, int idJanela )
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher( new KeyEventDispatcher()
                {
                    @Override
                    public boolean dispatchKeyEvent( KeyEvent e )
                    {
                        if ( e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F1 )
                        {
                            try
                            {
//                                new BuscaProdutoVisao( frame, modal, idArmazem, idJanela m, ).show();
                            }
                            catch ( Exception ex )
                            {
                                ex.printStackTrace();
                            }
                            return true;

                        }
                        return false;
                    }
                } );
    }

    public static double percentagemGanho( double pc, double pv )
    {
        double pg = ( ( pv - pc ) / ( pc * 0.01 ) );
        return pg;
    }

    public static double precoVendaMargem( double pc, double mg )
    {
        double pvm = ( ( pc / 100 + mg * 0.01 ) );
        return pvm;
    }

    public static double factor_correcao( double qtd_bruto, double qtd_liquido )
    {
        double fc = ( qtd_bruto / qtd_liquido );
        return fc;
    }

    public static double custo_total( double preco_compra, double qtd_bruto, double qtd_liquido, double unidade_compra )
    {
        double custo = ( qtd_bruto * preco_compra );
        double custo_total = ( custo / unidade_compra ) * 0.1;
        return custo_total;
    }

    public static double precoVenda( double pc, double pg )
    {
        System.out.println( "PC: " + pc );
        System.out.println( "PG: " + pg );
        double pv = ( pc + ( pc * pg * 0.01 ) );
        System.out.println( "PV: " + pv );
        return pv;
    }

    public static void exportReportToPdf( String name_file_retport, String name_file_pdf, HashMap parametros, Connection connection ) throws SQLException
    {

        String caminho_and_file = CAMINHO_REPORT + name_file_retport;
        File file = new File( caminho_and_file ).getAbsoluteFile();
        String caminhoAbsoluto = file.getAbsolutePath();

        try
        {
            JasperFillManager.fillReport( caminhoAbsoluto, parametros, connection );
            JasperPrint jasperPrint = JasperFillManager.fillReport( caminhoAbsoluto, parametros, connection );
            if ( jasperPrint.getPages().size() >= 1 )
            {
                //exporta o ficheiro como pdf no directorio 'anexos'
                JasperExportManager.exportReportToPdfFile( jasperPrint, "anexos/" + name_file_pdf );
            }

        }
        catch ( JRException jex )
        {
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + jex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
        catch ( Exception ex )
        {
            JOptionPane.showMessageDialog( null, "Falha ao converter o report em pdf.\nErro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE );
        }
    }

    public static void actualizaResolucaoTela( JFrame janela )
    {

        Toolkit tela = Toolkit.getDefaultToolkit();
        Dimension dimensao = tela.getScreenSize();
        int largura = dimensao.width;
        int altura = dimensao.height;
        janela.setSize( largura, altura );

    }

    public static String valorPorExtensos( double vlr )
    {
        if ( vlr == 0 )
        {
            return ( "zero" );
        }

        long inteiro = (long) Math.abs( vlr ); // parte inteira do valor
        double resto = vlr - inteiro;       // parte fracionária do valor

        String vlrS = String.valueOf( inteiro );
        if ( vlrS.length() > 15 )
        {
            return ( "Erro: valor superior a 999 trilhões." );
        }

        String s = "", saux, vlrP;
        String centavos = String.valueOf( (int) Math.round( resto * 100 ) );

        String[] unidade
                =
                {
                    "", " Um", "Dois", "Três", "Quatro", "Cinco",
                    "Seis", "Sete", "Oito", "Nove", "Dez", "Onze",
                    "Doze", "Treze", "Catorze", "Quinze", "Dezasseis",
                    "Dezassete", "Dezoito", "Dezanove"
                };
        String[] centena
                =
                {
                    "", "Cento", "Duzentos", "Trezentos",
                    "Quatrocentos", "Quinhentos", "Seiscentos",
                    "Setecentos", "Oitocentos", "Novecentos"
                };
        String[] dezena
                =
                {
                    "", "", "Vinte", "Trinta", "Quarenta", "Cinquenta",
                    "Sessenta", "Setenta", "Oitenta", "Noventa"
                };
        String[] qualificaS
                =
                {
                    "", "Mil", "Milhão", "Bilhão", "Trilhão"
                };
        String[] qualificaP
                =
                {
                    "", "Mil", "Milhões", "Bilhões", "Trilhões"
                };

// definindo o extenso da parte inteira do valor
        int n, unid, dez, cent, tam, i = 0;
        boolean umReal = false, tem = false;
        while ( !vlrS.equals( "0" ) )
        {
            tam = vlrS.length();
// retira do valor a 1a. parte, 2a. parte, por exemplo, para 123456789:
// 1a. parte = 789 (centena)
// 2a. parte = 456 (mil)
// 3a. parte = 123 (milhões)
            if ( tam > 3 )
            {
                vlrP = vlrS.substring( tam - 3, tam );
                vlrS = vlrS.substring( 0, tam - 3 );
            }
            else
            { // última parte do valor
                vlrP = vlrS;
                vlrS = "0";
            }
            if ( !vlrP.equals( "000" ) )
            {
                saux = "";
                if ( vlrP.equals( "100" ) )
                {
                    saux = "Cem";
                }
                else
                {
                    n = Integer.parseInt( vlrP, 10 );  // para n = 371, tem-se:
                    cent = n / 100;                  // cent = 3 (centena trezentos)
                    dez = ( n % 100 ) / 10;            // dez  = 7 (dezena setenta)
                    unid = ( n % 100 ) % 10;           // unid = 1 (unidade um)
                    if ( cent != 0 )
                    {
                        saux = centena[ cent ];
                    }
                    if ( ( n % 100 ) <= 19 )
                    {
                        if ( saux.length() != 0 )
                        {
                            saux = saux + unidade[ n % 100 ];
                        }
                        else
                        {
                            saux = unidade[ n % 100 ];
                        }
                    }
                    else
                    {
                        if ( saux.length() != 0 )
                        {
                            saux = saux + " e " + dezena[ dez ];
                        }
                        else
                        {
                            saux = dezena[ dez ];
                        }
                        if ( unid != 0 )
                        {
                            if ( saux.length() != 0 )
                            {
                                saux = saux + " e " + unidade[ unid ];
                            }
                            else
                            {
                                saux = unidade[ unid ];
                            }
                        }
                    }
                }
                if ( vlrP.equals( "1" ) || vlrP.equals( "001" ) )
                {
                    if ( i == 0 ) // 1a. parte do valor (um real)
                    {
                        umReal = true;
                    }
                    else
                    {
                        saux = saux + " " + qualificaS[ i ];
                    }
                }
                else if ( i != 0 )
                {
                    saux = saux + " " + qualificaP[ i ];
                }
                if ( s.length() != 0 )
                {
                    s = saux + ", " + s;
                }
                else
                {
                    s = saux;
                }
            }
            if ( ( ( i == 0 ) || ( i == 1 ) ) && s.length() != 0 )
            {
                tem = true; // tem centena ou mil no valor
            }
            i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão, ...
        }

        if ( s.length() != 0 )
        {
            if ( umReal )
            {
                s = s + " Kwanzas.";
            }
            else if ( tem )
            {
                s = s + " Kwanzas.";
            }
            else
            {
                s = s + " de Kwanzas.";
            }
        }

// definindo o extenso dos centavos do valor
        if ( !centavos.equals( "0" ) )
        { // valor com centavos
            if ( s.length() != 0 ) // se não é valor somente com centavos
            {
                s = s + " e ";
            }
            if ( centavos.equals( "1" ) )
            {
                s = s + "Um Centimo";
            }
            else
            {
                n = Integer.parseInt( centavos, 10 );
                if ( n <= 19 )
                {
                    s = s + unidade[ n ];
                }
                else
                {             // para n = 37, tem-se:
                    unid = n % 10;   // unid = 37 % 10 = 7 (unidade sete)
                    dez = n / 10;    // dez  = 37 / 10 = 3 (dezena trinta)
                    s = s + dezena[ dez ];
                    if ( unid != 0 )
                    {
                        s = s + " e " + unidade[ unid ];
                    }
                }
                s = s + " Centimos";
            }
        }
        return ( s );
    }

    public static double getValorImposto( int idProduto, double preco )
    {
        double taxaByIdProduto = produtoImpostoDao.getTaxaByIdProduto( idProduto );
//        return   preco +    ( preco * ( taxaByIdProduto / 100 ) );
        return ( preco * ( taxaByIdProduto / 100 ) );
    }

    public static double getValorImpostoUTIL( int idProduto, double preco )
    {
        double taxaByIdProduto = produtoImpostoDao.getTaxaByIdProduto( idProduto );
        return ( preco * ( taxaByIdProduto / 100 ) );
    }

    public static double getValorComIva( int idProduto, double preco )
    {

        double taxaByIdProduto = produtoImpostoDao.getTaxaByIdProduto( idProduto );
        if ( taxaByIdProduto != 0 )
        {
            return ( preco * 1.14 );
        }
        return 0;

    }

    public static double getValorComIvaDesconto( int idProduto, double preco, double qtd, double desconto )
    {

        double taxaByIdProduto = produtoImpostoDao.getTaxaByIdProduto( idProduto );
        double valor = ( ( preco * qtd ) - desconto );
        if ( taxaByIdProduto != 0 )
        {
            return ( valor * 1.14 );
        }
        return 0;

    }

    public static int getDiferencaDias( Date data_fim, Date data_inicio, BDConexao conexao )
    {

        String sql = "SELECT  datediff( '" + MetodosUtil.getDataBanco( data_fim ) + "' , '" + MetodosUtil.getDataBanco( data_inicio ) + "') as diferenca";

        ResultSet result = conexao.executeQuery( sql );
        int diferenca = 0;
        try
        {
            if ( result.next() )
            {
                diferenca = result.getInt( "diferenca" );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return diferenca;
    }

    public static boolean licencaValidada( BDConexao conexao )
    {

        Date dataComputador = new Date();

        String query = "SELECT data_fecho FROM tb_dados_instituicao";

        ResultSet rs = conexao.executeQuery( query );

        Date dataFecho = null;
        try
        {
            if ( rs.next() )
            {
                dataFecho = rs.getDate( "data_fecho" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        System.out.println( "DATA FECHO: " + getDataBanco( dataFecho ) );

        avisoLicenca( dataFecho, conexao );

        if ( Objects.isNull( dataFecho ) )
        {
            return true;
        }
        else
        {
            if ( maior_data_1_data_2( dataComputador, dataFecho ) )
            {
                JOptionPane.showMessageDialog( null, "Licença expirada.\nContacte a DVML para a renovação.\nTel: 921 73 41 26", "Aviso", JOptionPane.WARNING_MESSAGE );
                return false;
            }
            return true;
        }

    }

    public static void avisoLicenca( Date dataFecho, BDConexao conexao )
    {

        String query = "SELECT datediff('" + MetodosUtil.getDataBanco( dataFecho ) + "','" + MetodosUtil.getDataBanco( new Date() ) + "') AS dif";

        ResultSet rs = conexao.executeQuery( query );

        int diferenca = 0;
        try
        {
            if ( rs.next() )
            {
                diferenca = rs.getInt( "dif" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            Logger.getLogger( MetodosUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        if ( diferenca <= 30 && diferenca > 0 )
        {
            JOptionPane.showMessageDialog( null, "Faltam " + diferenca + " dia(s) para a sua licença expirar.\nContacte a DVML.-Tel: 921 73 41 26" );
        }
        else
        {
            System.out.println( "DIFERENCA: " + diferenca );
        }

    }

    public static String criptografia_hash1( Object documento, double parmGrossTotal, BDConexao conexao )
    {
        String final_hash = "";

        if ( documento instanceof TbVenda )
        {
            System.err.println( " **********************DOC VENDA******************************" );
            final_hash = criptografia_hash_venda( (TbVenda) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Notas )
        {
            System.err.println( " **********************DOC NOTAS******************************" );
            final_hash = criptografia_hash_nota( (Notas) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Compras )
        {
            System.err.println( " **********************DOC COMPRAS ******************************" );
            final_hash = criptografia_hash_compras( (Compras) documento, parmGrossTotal, conexao );
        }

        System.err.println( "final_hash: " + final_hash );
        System.err.println( "final_hash:LENGTH: " + final_hash.length() );
        System.err.println( "****************************************************" );
        return final_hash;
    }

    public static String criptografia_hash2( Object documento, double parmGrossTotal, BDConexao conexao )
    {
        String final_hash = "";

        if ( documento instanceof TbVenda )
        {
            System.err.println( " **********************DOC VENDA******************************" );
            final_hash = criptografia_hash_venda2( (TbVenda) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Notas )
        {
            System.err.println( " **********************DOC NOTAS******************************" );
            final_hash = criptografia_hash_nota( (Notas) documento, parmGrossTotal, conexao );
        }
        else if ( documento instanceof Compras )
        {
            System.err.println( " **********************DOC COMPRAS ******************************" );
            final_hash = criptografia_hash_compras( (Compras) documento, parmGrossTotal, conexao );
        }

        System.err.println( "final_hash: " + final_hash );
        System.err.println( "final_hash:LENGTH: " + final_hash.length() );
        System.err.println( "****************************************************" );
        return final_hash;
    }

    public static String getCodFactCompra( int pk_compra, BDConexao conexao )
    {

        String sql = "SELECT cod_fact FROM  compras WHERE pk_compra  =  " + pk_compra;
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( "cod_fact" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return null;
        }

        return null;
    }

    public double getTotalFaltasSalarial( int idFuncionario )
    {

        BDConexao conexaoLocal = new BDConexao();
        FechoPeriodo fechoPeriodo = fechoPeriodoDao.getFechoPeriodoByIdAnoAndPerido( getIdAno(), getIdPeriodo() );
        List<TbFalta> itens = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario(
                idFuncionario, fechoPeriodo.getDataAbertura(),
                fechoPeriodo.getDataFecho(), conexaoLocal, faltaDao );
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( idFuncionario );
        double numero_faltas = 0d;
        Iterator<TbFalta> iterator = itens.iterator();
        double valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( emf,
                funcionario,
                conexaoLocal );

        while ( iterator.hasNext() )
        {
            TbFalta next = iterator.next();
            numero_faltas += next.getNFalta();
        }
        conexaoLocal.close();
        return ( valor_hora * numero_faltas );

    }

    public double getTotalSubsidio( TbFuncionario funcionario_local, List<TbItemSubsidiosFuncionario> subsidios )
    {
        BDConexao conexaoLocal = new BDConexao();
        double total = 0;

        if ( Objects.nonNull( subsidios ) )
        {
//            TbFuncionario funcionario_local = subsidios.get( 0 ).getIdFuncionarioFK();
            int dias_uteis_trabalho = funcionario_local.getFkModalidade().getDiasUteisTrabalho();
            double horas_de_faltas = FaltaDao.getNHoraIntervaloDatasFalta( funcionario_local,
                    RecibosVisao.fechoPeriodo.getDataAbertura(),
                    RecibosVisao.fechoPeriodo.getDataFecho(), conexaoLocal, faltaDao );

            double valor = 0;

            for ( TbItemSubsidiosFuncionario subsidio : subsidios )
            {
                valor = subsidio.getValorSubsidio();
                if ( subsidio.getIdSubsidioFK().getIncidencia_inss() )
                {
                    total += valor - FaltaDao.getDescontoSubsidio( valor,
                            dias_uteis_trabalho,
                            horas_de_faltas );
                }
                else
                {
                    total += valor;
                }
            }
        }

        return total;
    }

    public double getDescontoSubsidiosTributavel( TbFuncionario funcionario_local, List<TbItemSubsidiosFuncionario> subsidios )
    {

        double total = 0, CONSTANTE = 30000;
        double valor;

        BDConexao conexaoLocal = new BDConexao();
        int dias_uteis_trabalho = funcionario_local.getFkModalidade().getDiasUteisTrabalho();
        double horas_de_faltas = FaltaDao.getNHoraIntervaloDatasFalta( funcionario_local,
                RecibosVisao.fechoPeriodo.getDataAbertura(),
                RecibosVisao.fechoPeriodo.getDataFecho(), conexaoLocal, faltaDao );

        for ( TbItemSubsidiosFuncionario itemSubsidios : subsidios )
        {
            valor = itemSubsidios.getValorSubsidio();
            if ( itemSubsidios.getIdSubsidioFK().getIncidencia_inss()
                    && ( itemSubsidios.getIdSubsidioFK().getIdSubsidios() == 1
                    || itemSubsidios.getIdSubsidioFK().getIdSubsidios() == 2 ) ) // subsidio que nao sao tributavel na totalidade.
            {
                if ( valor <= CONSTANTE )
                {
                    total += valor - FaltaDao.getDescontoSubsidio( valor,
                            dias_uteis_trabalho,
                            horas_de_faltas );
                }
            }

        }

        conexaoLocal.close();
        return total;
    }

    public double calculo_irt( TbFuncionario funcionario, double salario_base, List<TbItemSubsidiosFuncionario> subsidios, double agregado_familiar )
    {

        double valor_total_faltas = getTotalFaltasSalarial( funcionario.getIdFuncionario() );
        double remuneracoesEspecies = RecibosVisao.getRemuneracoesEspecies( funcionario );
        double total_subsidios = getTotalSubsidio( funcionario, subsidios );
        double abono_familia = ( agregado_familiar > ( salario_base * 0.05 ) ) ? 0 : agregado_familiar;

        double salario_iliquido = salario_base - valor_total_faltas + total_subsidios + remuneracoesEspecies;
        double seguranca_social = ( salario_iliquido * 0.03 ); //O agregado por lei nao poder ser adicionado para calcular o inss
        salario_iliquido += agregado_familiar;
//        double material_coletavel = salario_iliquido - seguranca_social - abono_familia + getTotalSubsidioTributavel( subsidios );
        double desconto_susidios_tributaveis = getDescontoSubsidiosTributavel( funcionario, subsidios ); //alimentacao e transporte

        double material_coletavel = salario_iliquido - seguranca_social - abono_familia - desconto_susidios_tributaveis;
        //seta os dados de taxa de tributação
        RecibosVisao.taxas_tributacao = taxas_tributacao( material_coletavel );

        double parcela_fixa = RecibosVisao.taxas_tributacao.parcela_fixa;
        double excesso = RecibosVisao.taxas_tributacao.excesso;
        double taxa = RecibosVisao.taxas_tributacao.percentagem;
        taxa = taxa / 100;
        double irt = ( parcela_fixa + ( ( material_coletavel - excesso ) * taxa ) );

        System.out.println( "*** SALARIO BASE: " + salario_base );
        System.out.println( "*** ABONO DE FAMILIA: " + abono_familia );
        System.out.println( "*** SALARIO ILIQUIDO: " + salario_iliquido );
        System.out.println( "*** DESCONTO SUBSIDIOS TRIBUTAVEL: " + desconto_susidios_tributaveis );
        System.out.println( "*** MATERIAL COLETAVEL: " + material_coletavel );
        System.out.println( "*** SEGURANCA SOCIAL: " + seguranca_social );
        System.out.println( "*** PARCELA FIXA: " + parcela_fixa );
        System.out.println( "*** EXCESSO: " + excesso );
        System.out.println( "*** TAXA: " + RecibosVisao.taxas_tributacao.percentagem );

        return irt;

    }

    public static BigDecimal getTotalVendaIVASemIncluirDescontoForPayment( List<AmortizacaoDivida> list )
    {
        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
        BigDecimal qtd;

        total_iva = new BigDecimal( 0 );

        for ( AmortizacaoDivida linha : list )
        {

            if ( linha.getTax().doubleValue() > 0 )
            {
                preco = new BigDecimal( linha.getNetTotal().doubleValue() );
                qtd = new BigDecimal( 1d );
                sub_total_iliquido = preco.multiply( qtd );

//                taxa = ( linha.getValorIva() / 100 );
                taxa = new BigDecimal( linha.getTax().doubleValue() ).divide( new BigDecimal( 100 ) );
//                total_iva += ( ( sub_total_iliquido * taxa ) );
                total_iva = total_iva.add( sub_total_iliquido.multiply( taxa ) );
            }
            else
            {
                BigDecimal valorIva = new BigDecimal( 0d );
//                total_iva += linha.getValorIva();
                total_iva = total_iva.add( valorIva );

            }

        }
        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_iva;
    }

    public static BigDecimal getNetTotalForPayment( List<AmortizacaoDivida> list )
    {
        BigDecimal total_net_total, preco, sub_total_iliquido;
        BigDecimal qtd;
        total_net_total = new BigDecimal( 0 );

        for ( AmortizacaoDivida linha : list )
        {

//            preco = new BigDecimal( linha.getValorEntregue() );
//            qtd = new BigDecimal( 1 );
//            sub_total_iliquido = preco.multiply( qtd );
            total_net_total = total_net_total.add( linha.getNetTotal() );
        }
        total_net_total = total_net_total.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_net_total;
    }

//    public static BigDecimal getNetTotal( List<TbItemVenda> list )
//    {
//        BigDecimal total_net_total, preco, sub_total_iliquido;
//        BigDecimal qtd;
//        total_net_total = new BigDecimal( 0 );
//
//        for ( TbItemVenda linha : list )
//        {
//
//            preco = linha.getFkPreco().getPrecoVenda();
//            qtd = new BigDecimal( linha.getQuantidade() );
//            sub_total_iliquido = preco.multiply( qtd );
//            total_net_total = total_net_total.add( sub_total_iliquido );
//        }
//        total_net_total = total_net_total.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
//        return total_net_total;
//    }
    public static BigDecimal getTotalSemIva( List<TbVenda> list, int pk_documento )
    {

        BigDecimal soma = new BigDecimal( 0 );
        for ( TbVenda venda : list )
        {
            // soma += (venda.getTotalGeral() - venda.getTotalIva());
            //TOTAL ILÍQUIDO
            if ( venda.getFkDocumento().getPkDocumento() == pk_documento )
            {
                soma = soma.add( getNetTotal( venda.getTbItemVendaList() ) );
            }
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += (venda.getTotalGeral() - venda.getTotalIva());
//            }

        }

        soma = soma.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return soma;
    }

    public static double getTaxaIva( String regime )
    {
        if ( regime.equals( DVML.REGIME_EXCLUSAO ) )
        {
            return 0d;
        }
        else if ( regime.equals( DVML.REGIME_SIMPLIFICADO ) )
        {
            return 7;
        }
        else if ( regime.equals( DVML.REGIME_GERAL ) )
        {
            return 7;
        }
        else
        {
            return 0d;
        }

    }

    public static double getValorComIva( int idProduto, double preco, String regime )
    {

        double taxaByIdProduto = produtoImpostoDao.getTaxaByIdProduto( idProduto );
        if ( taxaByIdProduto != 0 )
        {

            double taxa = ( MetodosUtil.getTaxaIva( regime ) / 100 );
            double valor_final = ( preco + ( preco * taxa ) );
            System.out.println( "TAXA: " + taxa );
            System.out.println( "VALOR FINAL: " + valor_final );
//            return ( preco * taxa );
            return valor_final;
        }
        return preco;

    }

    public static BigDecimal getTotalSemIvaForPayment( List<TbVenda> recibos, int pk_documento, AmortizacaoDividasController amortizacaoDividasController )
    {

        BigDecimal soma = new BigDecimal( 0 );
        for ( TbVenda recibo : recibos )
        {
            // soma += (venda.getTotalGeral() - venda.getTotalIva());
            //TOTAL ILÍQUIDO
            if ( recibo.getFkDocumento().getPkDocumento() == pk_documento )
            {
                System.out.println( "RECIBO REF: " + recibo.getCodFact() );
                List<AmortizacaoDivida> lista = amortizacaoDividasController.findAllByCodRef( recibo.getCodFact() ); //linhas
                System.err.println( "SIZE AMORTIZACAO: " + lista.size() );
                soma = soma.add( getNetTotalForPayment( lista ) );

            }
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += (venda.getTotalGeral() - venda.getTotalIva());
//            }

        }

        soma = soma.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return soma;
    }

    public static boolean estado_critico( int idProduto, int idArmazem, int qtdMinima, int qtdExistente, int qtdCritica, BDConexao conexao )
    {
        return qtdMinima
                < qtdExistente
                && qtdExistente
                <= qtdCritica;
    }

    public static BigDecimal getTotalVendaIVASemIncluirDescontoCompra( List<ItemCompras> list )
    {
        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
        BigDecimal qtd;

        total_iva = new BigDecimal( 0 );

        for ( ItemCompras linha : list )
        {
            if ( linha.getValorIva().doubleValue() > 0 )
            {
                preco = new BigDecimal( linha.getPrecoCompra() );
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );

//                taxa = ( linha.getValorIva() / 100 );
                taxa = new BigDecimal( linha.getValorIva().doubleValue() ).divide( new BigDecimal( 100 ) );
//                total_iva += ( ( sub_total_iliquido * taxa ) );
                total_iva = total_iva.add( sub_total_iliquido.multiply( taxa ) );
            }
            else
            {
                BigDecimal valorIva = new BigDecimal( linha.getValorIva().doubleValue() );
//                total_iva += linha.getValorIva();
                total_iva = total_iva.add( valorIva );

            }

        }
        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_iva;
    }

    public static BigDecimal getNetTotalCompra( List<ItemCompras> list )
    {
        BigDecimal total_net_total, preco, sub_total_iliquido;
        BigDecimal qtd;
        total_net_total = new BigDecimal( 0 );

        for ( ItemCompras linha : list )
        {

            preco = new BigDecimal( linha.getPrecoCompra() );
            qtd = new BigDecimal( linha.getQuantidade() );
            sub_total_iliquido = preco.multiply( qtd );
            total_net_total = total_net_total.add( sub_total_iliquido );
        }
        total_net_total = total_net_total.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_net_total;
    }

    public static BigDecimal getGrossTotalCompra( List<ItemCompras> list )
    {
        BigDecimal taxa, total_iva, preco, sub_total_iliquido;
        BigDecimal qtd;

        total_iva = new BigDecimal( 0 );
        for ( ItemCompras linha : list )
        {
            if ( linha.getValorIva().doubleValue() > 0 )
            {
                preco = new BigDecimal( linha.getPrecoCompra() );
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );
                BigDecimal valorIvaLinha = new BigDecimal( linha.getValorIva().doubleValue() );
//                taxa = ( linha.getValorIva() / 100 );
                taxa = valorIvaLinha.divide( new BigDecimal( 100 ) );
//                total_iva  =   total_iva +  sub_total_iliquido + ( ( sub_total_iliquido * taxa ) );
                BigDecimal valorImposto = sub_total_iliquido.multiply( taxa );
                total_iva = total_iva.add( sub_total_iliquido.add( valorImposto ) );
            }
            else
            {
                preco = new BigDecimal( linha.getPrecoCompra() );
                qtd = new BigDecimal( linha.getQuantidade() );
                sub_total_iliquido = preco.multiply( qtd );
                total_iva = total_iva.add( sub_total_iliquido );

            }

        }

        total_iva = total_iva.setScale( 2, BigDecimal.ROUND_HALF_EVEN );
        return total_iva;
    }

    public static void imprimir_cozinha( TbProduto produto, int id, String mesa, String lugar, String usuario, String condicao, int qtd, DadosInstituicaoController dadosInstituicaoController )
    {
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        HashMap hashMap = new HashMap();
        hashMap.put( "designacao", produto.getDesignacao() );
        hashMap.put( "condicao", condicao );
        hashMap.put( "qtd", qtd );
        hashMap.put( "mesa", mesa );
        hashMap.put( "lugar", lugar );
        hashMap.put( "usuario", usuario );
        AnyReport anyReport = new AnyReport( hashMap, "cozinha_print_temp.jasper", true, dadosInstituicao.getImpressoraCozinha() );
//        AnyReport anyReport1 = new AnyReport( hashMap, "cozinha_print_temp.jasper", true, dadosInstituicao.getImpressoraSala() );
//        AnyReport anyReport = new AnyReport( hashMap, "cozinha_print_temp.jasper" );
    }

    public static void imprimir_sala( TbProduto produto, int id, String mesa, String lugar, String usuario, String condicao, int qtd, DadosInstituicaoController dadosInstituicaoController )
    {
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        HashMap hashMap = new HashMap();
        hashMap.put( "designacao", produto.getDesignacao() );
        hashMap.put( "condicao", condicao );
        hashMap.put( "qtd", qtd );
        hashMap.put( "mesa", mesa );
        hashMap.put( "lugar", lugar );
        hashMap.put( "usuario", usuario );
        AnyReport anyReport = new AnyReport( hashMap, "cozinha_print_temp.jasper", true, dadosInstituicao.getImpressoraSala() );
//        AnyReport anyReport1 = new AnyReport( hashMap, "cozinha_print_temp.jasper", true, dadosInstituicao.getImpressoraSala() );
//        AnyReport anyReport = new AnyReport( hashMap, "cozinha_print_temp.jasper" );
    }

//    public static void imprimir_geral_cozinha( TbProduto produto, int id, String mesa, String lugar, String usuario, String condicao, int qtd, DadosInstituicaoController dadosInstituicaoController )
    public static void imprimir_geral_cozinha( int pk_pedido, DadosInstituicaoController dadosInstituicaoController )
    {
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        HashMap hashMap = new HashMap();
        hashMap.put( "ID_PEDIDO", pk_pedido );
        hashMap.put( "AREA_ENVIO", DVML.ENVIAR_TICKET );
        System.out.println( "ID_PEDIDO " + pk_pedido );
        AnyReport anyReport = new AnyReport( hashMap, "ticket_geral.jasper", true, dadosInstituicao.getImpressoraCozinha() );
    }

    public static void imprimir_geral_sala( int pk_pedido, DadosInstituicaoController dadosInstituicaoController )
    {
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        HashMap hashMap = new HashMap();
        hashMap.put( "ID_PEDIDO", pk_pedido );
        hashMap.put( "AREA_ENVIO", DVML.ENVIAR_SALA );
        AnyReport anyReport = new AnyReport( hashMap, "ticket_geral.jasper", true, dadosInstituicao.getImpressoraSala() );
    }

    public static void actualizarPrecoVendaManual( int idProduto, Double precoVenda, PrecosController precosControllerLocal, BDConexao conexao )
    {
        BDConexao conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );

        System.out.println( "ID PRODUTO: " + idProduto );

        int idPrecoRetalho = PrecosController.getLastIdPrecoByIdProdutoIntAndQTD( idProduto, 0d, conexao );
        System.out.println( "ID RETALHO: " + idPrecoRetalho );
        TbPreco precoAntigoRetalho = (TbPreco) precosControllerLocal.findById( idPrecoRetalho );
        System.out.println( "PRECO RETALHO: " + precoAntigoRetalho );

        int idPrecoGrosso = PrecosController.getLastIdPrecoByIdProdutoIntAndPrecoAntigoQtdAlto( idProduto, precoAntigoRetalho.getQtdAlto() + 1, conexao );
        TbPreco precoAntigoGrosso = (TbPreco) precosControllerLocal.findById( idPrecoGrosso );
        System.out.println( "ID GROSSO: " + idPrecoGrosso );
        System.out.println( "PRECO GROSSO: " + precoAntigoGrosso );

        TbPreco preco_novo_retalho = new TbPreco();

        preco_novo_retalho.setData( precoAntigoRetalho.getData() );
        preco_novo_retalho.setHora( precoAntigoRetalho.getHora() );
        preco_novo_retalho.setPercentagemGanho( precoAntigoRetalho.getPercentagemGanho() );

        preco_novo_retalho.setFkProduto( precoAntigoRetalho.getFkProduto() );
        preco_novo_retalho.setFkUsuario( precoAntigoRetalho.getFkUsuario() );
        preco_novo_retalho.setPrecoCompra( precoAntigoRetalho.getPrecoCompra() );
        preco_novo_retalho.setPrecoVenda( new BigDecimal( precoVenda ) );
        preco_novo_retalho.setQtdBaixo( precoAntigoRetalho.getQtdBaixo() );
        preco_novo_retalho.setQtdAlto( precoAntigoRetalho.getQtdAlto() );
        preco_novo_retalho.setPrecoAnterior( precoAntigoRetalho.getPrecoAnterior() );
        preco_novo_retalho.setRetalho( precoAntigoRetalho.getRetalho() );

        System.out.println( "HORA RETALHO: " + preco_novo_retalho.getHora() );
        System.out.println( "DATA RETAHO: " + preco_novo_retalho.getData() );

        try
        {
            precosControllerLocal.salvar( preco_novo_retalho );
            DocumentosController.commitTransaction( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_retalho);
            System.out.println( "Preco de compra retalho actualizado na venda" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollBackTransaction( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço retalho na venda" );
        }

        conexaoTransaction = new BDConexao();
        DocumentosController.startTransaction( conexaoTransaction );
        try
        {
            //        preco_novo_grosso = precoAntigoGrosso;
            TbPreco preco_novo_grosso = new TbPreco();

            preco_novo_grosso.setData( precoAntigoGrosso.getData() );
            preco_novo_grosso.setHora( precoAntigoGrosso.getHora() );
            preco_novo_grosso.setPercentagemGanho( precoAntigoGrosso.getPercentagemGanho() );

            preco_novo_grosso.setFkProduto( precoAntigoGrosso.getFkProduto() );
            preco_novo_grosso.setFkUsuario( precoAntigoGrosso.getFkUsuario() );
            preco_novo_grosso.setPrecoCompra( precoAntigoGrosso.getPrecoCompra() );
            preco_novo_grosso.setPrecoVenda( new BigDecimal( precoVenda ) );
            preco_novo_grosso.setQtdBaixo( precoAntigoGrosso.getQtdBaixo() );
            preco_novo_grosso.setQtdAlto( precoAntigoGrosso.getQtdAlto() );
            preco_novo_grosso.setPrecoAnterior( precoAntigoGrosso.getPrecoAnterior() );
            preco_novo_grosso.setRetalho( precoAntigoGrosso.getRetalho() );

            System.out.println( "HORA GROSSO: " + precoAntigoGrosso.getHora() );
            System.out.println( "DATA GROSSO: " + precoAntigoGrosso.getData() );

            precosControllerLocal.salvar( preco_novo_grosso );

            DocumentosController.commitTransaction( conexaoTransaction );
            conexaoTransaction.close();
//            precoDao.create(preco_novo_grosso);
            System.out.println( "Preco de compra grosso actualizado na compra" );
        }
        catch ( Exception e )
        {
            DocumentosController.rollBackTransaction( conexaoTransaction );
            e.printStackTrace();
            System.err.println( "Falha ao actualizar o preço grosso na compra" );
        }

    }

    public static int getIdArmazemByCampoConfigArmazem( String designacao )
    {
        return designacao.equals( DVML.UNI_ARMAZEM ) ? 2 : 1;
    }

    public static void setArmazemByCampoConfigCompraArmazem( JComboBox cmb, BDConexao conexao, int cod_usuario )
    {

        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexao );
        ArmazensAccessoController armazensAccessoController = new ArmazensAccessoController( conexao );
        ArmazensController armazensController = new ArmazensController( conexao );

        TbDadosInstituicao dados = dadosInstituicaoController.findByCodigo( 1 );
        String designacao = dados.getConfigArmazens();
        cmb.setModel( new DefaultComboBoxModel( armazensController.getVector() ) );

        if ( designacao.equals( DVML.UNI_ARMAZEM ) )
        {
            cmb.setSelectedIndex( 1 );
            cmb.setEnabled( false );

        }
        else
        {
            cmb.setSelectedIndex( 0 );
            cmb.setEnabled( true );
//            cmb.setModel( new DefaultComboBoxModel( armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario( cod_usuario ) ) );
        }
    }

    public static void setArmazemByCampoConfigArmazem( JComboBox cmb, BDConexao conexao, int cod_usuario )
    {

        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexao );
        ArmazensAccessoController armazensAccessoController = new ArmazensAccessoController( conexao );
        ArmazensController armazensController = new ArmazensController( conexao );

        TbDadosInstituicao dados = dadosInstituicaoController.findByCodigo( 1 );
        String designacao = dados.getConfigArmazens();
        cmb.setModel( new DefaultComboBoxModel( armazensController.getVector() ) );

        if ( designacao.equals( DVML.UNI_ARMAZEM ) )
        {
            cmb.setSelectedIndex( 1 );
            cmb.setEnabled( false );
        }
        else
        {
            cmb.setModel( new DefaultComboBoxModel( armazensAccessoController.getAllArmazemExceptoEconomatoByIdUSuario( cod_usuario ) ) );
        }
    }

    public static void verificarCaixa( CaixasController caixasController, int cod_utilizador, JButton btnAberturaCaixa, JButton btnFechoCaixa, JButton... lista )
    {
        if ( !caixasController.existeCaixas( cod_utilizador ) )
        {

            btnAberturaCaixa.setEnabled( true );
            btnFechoCaixa.setEnabled( false );
            activarButton( lista, false );
        }
        else if ( caixasController.existe_abertura( cod_utilizador )
                && caixasController.existe_fecho( cod_utilizador ) )
        {
            btnAberturaCaixa.setEnabled( true );
            btnFechoCaixa.setEnabled( false );
            activarButton( lista, false );
        }
        else
        {
            btnAberturaCaixa.setEnabled( false );
            btnFechoCaixa.setEnabled( true );
            activarButton( lista, true );
        }
    }

    private static void activarButton( JButton[] lista, boolean status )
    {
        for ( JButton jButton : lista )
        {
            jButton.setEnabled( status );
        }
    }

    public static void procedimento_abrir_caixa( int idUser, BDConexao conexao )
    {
        UsuariosController usuariosController = new UsuariosController( conexao );
        CaixasController caixa_controller = new CaixasController( conexao );

        System.out.println( "Cheguei antes do existe caixa" );
        if ( !caixa_controller.existeCaixas( idUser, new Date() ) )
        {
            try
            {
                abrir_caixa_automatica( idUser, usuariosController, caixa_controller );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
    }

    public static void abrir_caixa_automatica( int idUser,
            UsuariosController usuariosController, CaixasController caixa_controller )
    {

        BDConexao conexaoTransaction = new BDConexao();

        DocumentosController.startTransaction( conexaoTransaction );
        TbUsuario usuario = (TbUsuario) usuariosController.findById( idUser );
        Caixa caixa = new Caixa();
        caixa.setDataAbertura( new Date() );
        caixa.setDataFecho( null );
        caixa.setTotalVendas( 0d );
        caixa.setNumeroVendas( 0 );
        caixa.setValorInicial( 0d );
        caixa.setUsuarioAbertura( usuario.getNome() );
        caixa.setUsuarioFecho( "" );
        caixa.setCodUsuarioAbertura( usuario.getCodigo() );
        caixa.setCodUsuarioFecho( 0 );
        caixa.setTotalDesconto( new BigDecimal( 0d ) );
        caixa.setTotalIva( new BigDecimal( 0d ) );
        caixa.setTotaIIliquido( new BigDecimal( 0d ) );

        try
        {
            if ( caixa_controller.salvar( caixa ) )
            {
                System.out.println( "Caixa aberto com sucesso!" );
                DocumentosController.commitTransaction( conexaoTransaction );
                conexaoTransaction.close();
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Falha ao abrir o caixa", "Aviso", JOptionPane.WARNING_MESSAGE );
            }

//            RootVisao.alterar_status_botao();
//            JanelaFrontOfficeLavandariaVisao.alterar_status_botao();
        }
        catch ( HeadlessException e )
        {
            JOptionPane.showMessageDialog( null, "Falha ao abrir o caixa: " + e.getLocalizedMessage(), "Falha", JOptionPane.ERROR_MESSAGE );
            DocumentosController.rollBackTransaction( conexaoTransaction );
            conexaoTransaction.close();
        }
    }

//    public static void fechoAutomatico()
//    {
//        BDConexao conexaoTransaction = new BDConexao();
//        CaixasController caixa_controller = new CaixasController( conexaoTransaction );
//        VendasController vendasController = new VendasController( conexaoTransaction );
//        FormaPagamentoItemController formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransaction );
//        FormaPagamentoController formaPagamentoController = new FormaPagamentoController( conexaoTransaction );
//        ItemCaixaController item_caixa_controller = new ItemCaixaController( conexaoTransaction );
//        boolean sucesso = true;
//        List<FormaPagamento> formasPagamentos = formaPagamentoController.listarTodosExeceptoOrdemSacEGorjet();
//        List<TbUsuario> usuarios = vendasController.listarUsuario( new Date() );
//
//        for ( TbUsuario usuario : usuarios )
//        {
//            if ( !caixa_controller.existe_fecho( usuario.getCodigo() ) )
//            {
//                Caixa caixa_actual = caixa_controller.caixa_actual( usuario.getCodigo() );
//                caixa_actual.setDataFecho( new Date() );
//
//                int idUser = usuario.getCodigo();
//                System.out.println( "DATA ABERTURA: " + caixa_actual.getDataAbertura() );
//                caixa_actual.setUsuarioFecho( usuario.getNome() );
//                caixa_actual.setCodUsuarioFecho( idUser );
//                caixa_actual.setNumeroVendas(
//                        formaPagamentoItemController.getNumeroVendasDiario( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() ) );
//                double valor = formaPagamentoItemController.getValorRealDiario( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() );
//                double troco = formaPagamentoItemController.getTrocoRealDiario( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() );
//                BigDecimal totalDesconto = formaPagamentoItemController.getTotalDesconto( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() );
//                BigDecimal totalIva = formaPagamentoItemController.getTotalIva( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() );
//                BigDecimal totalIliquido = formaPagamentoItemController.getTotalIliquido( conexaoTransaction, idUser, caixa_actual.getDataAbertura(), caixa_actual.getDataFecho() );
//
//                caixa_actual.setTotalVendas( valor - troco );
//                caixa_actual.setTotalDesconto( totalDesconto );
//                caixa_actual.setTotalIva( totalIva );
//                caixa_actual.setTotaIIliquido( totalIliquido );
//
//                try
//                {
//                    caixa_controller.actualizar( caixa_actual );
//
//                    for ( int j = 0; j < formasPagamentos.size(); j++ )
//                    {
//                        Integer idFormaPagamento = formasPagamentos.get( j ).getPkFormaPagamento();
//                        if ( formaPagamentoItemController.existeVendaDiarioByFormaPagamento(
//                                idUser, idFormaPagamento,
//                                caixa_actual.getDataAbertura(),
//                                caixa_actual.getDataFecho(), conexaoTransaction ) )
//                        {
//                            Double valor_declarado = Double.valueOf( "0" );
//                            Double valor_real = formaPagamentoItemController.getValorRealDiarioByFormaPagamento(
//                                    usuario.getCodigo(), idFormaPagamento,
//                                    caixa_actual.getDataAbertura(),
//                                    caixa_actual.getDataFecho(), conexaoTransaction );
//
//                            ItemCaixa itemCaixa = new ItemCaixa();
//                            itemCaixa.setValorDeclarado( valor_declarado );
//                            itemCaixa.setValorReal( valor_real );
//                            itemCaixa.setFkFormaPagamento( formaPagamentoController.findByCodigo( idFormaPagamento ) );
//                            itemCaixa.setFkCaixa( caixa_actual );
//
//                            try
//                            {
//                                item_caixa_controller.salvar( itemCaixa );
//                            }
//                            catch ( Exception e )
//                            {
//                                sucesso = false;
//                                System.out.println( "Erro: " + e.getLocalizedMessage() );
//                                DocumentosController.rollBackTransaction( conexaoTransaction );
//                                break;
//
//                            }
//                        }
//                    }
//
//                }
//                catch ( Exception e )
//                {
//                    e.printStackTrace();
//                    sucesso = false;
//                }
//
//            }
//        }//FIM DA LISTA USUARIOS
//
//        if ( sucesso )
//        {
//
//            try
//            {
//                DocumentosController.commitTransaction( conexaoTransaction );
//                /**
//                 * CONSTRUIR .PDFs
//                 */
//                NLExporToPdfForSandEmailReport exporToPdfForSandEmailReport = new NLExporToPdfForSandEmailReport( conexaoTransaction );
//                conexaoTransaction.close();
//                System.out.println( "Fecho realizado com sucesso!..." );
//            }
//            catch ( Exception e )
//            {
//
//                JOptionPane.showMessageDialog( null, "Falha ao processar os relatórios.\nFalha: " + e.getLocalizedMessage(),
//                        "Falha", JOptionPane.ERROR_MESSAGE );
//                DocumentosController.rollBackTransaction( conexaoTransaction );
//                conexaoTransaction.close();
//            }
//
//        }
//        else
//        {
//            conexaoTransaction.close();
//        }
//
//    }
    public static void fechoAutomatico()
    {
        BDConexao conexaoTransaction = new BDConexao();
        CaixasController caixa_controller = new CaixasController( conexaoTransaction );
        VendasController vendasController = new VendasController( conexaoTransaction );
        FormaPagamentoItemController formaPagamentoItemController = new FormaPagamentoItemController( conexaoTransaction );
        FormaPagamentoController formaPagamentoController = new FormaPagamentoController( conexaoTransaction );
        ItemCaixaController item_caixa_controller = new ItemCaixaController( conexaoTransaction );

        boolean sucesso = true;

        List<FormaPagamento> formasPagamentos = formaPagamentoController.listarTodosExeceptoOrdemSacEGorjet();
        List<TbUsuario> usuarios = vendasController.listarUsuario( new Date() );

        for ( TbUsuario usuario : usuarios )
        {
            int idUser = usuario.getCodigo();

            if ( !caixa_controller.existe_fecho( idUser ) )
            {
                Caixa caixa_actual = caixa_controller.caixa_actual( idUser );
                caixa_actual.setDataFecho( new Date() );

                Date dataAbertura = caixa_actual.getDataAbertura();
                Date dataFecho = caixa_actual.getDataFecho();

                caixa_actual.setUsuarioFecho( usuario.getNome() );
                caixa_actual.setCodUsuarioFecho( idUser );
                caixa_actual.setNumeroVendas(
                        formaPagamentoItemController.getNumeroVendasDiario( conexaoTransaction, idUser, dataAbertura, dataFecho )
                );

                BigDecimal valor = formaPagamentoItemController.getValorRealDiario( conexaoTransaction, idUser, dataAbertura, dataFecho );
                BigDecimal troco = formaPagamentoItemController.getTrocoRealDiario( conexaoTransaction, idUser, dataAbertura, dataFecho );
                BigDecimal totalDesconto = formaPagamentoItemController.getTotalDesconto( conexaoTransaction, idUser, dataAbertura, dataFecho );
                BigDecimal totalIva = formaPagamentoItemController.getTotalIva( conexaoTransaction, idUser, dataAbertura, dataFecho );
                BigDecimal totalIliquido = formaPagamentoItemController.getTotalIliquido( conexaoTransaction, idUser, dataAbertura, dataFecho );

                BigDecimal totalVendas = valor.subtract( troco );

                caixa_actual.setTotalVendas( totalVendas.doubleValue() );

                caixa_actual.setTotalDesconto( totalDesconto );
                caixa_actual.setTotalIva( totalIva );
                caixa_actual.setTotaIIliquido( totalIliquido );

                try
                {
                    caixa_controller.actualizar( caixa_actual );

                    for ( FormaPagamento formaPagamento : formasPagamentos )
                    {
                        int idFormaPagamento = formaPagamento.getPkFormaPagamento();

                        if ( formaPagamentoItemController.existeVendaDiarioByFormaPagamento(
                                idUser, idFormaPagamento, dataAbertura, dataFecho, conexaoTransaction ) )
                        {

                            BigDecimal valor_declarado = BigDecimal.ZERO;
                            BigDecimal valor_real = formaPagamentoItemController.getValorRealDiarioByFormaPagamento(
                                    idUser, idFormaPagamento, dataAbertura, dataFecho, conexaoTransaction );

                            ItemCaixa itemCaixa = new ItemCaixa();
                            itemCaixa.setValorDeclarado( valor_declarado.doubleValue() );
                            itemCaixa.setValorReal( valor_real.doubleValue() );
                            itemCaixa.setFkFormaPagamento( formaPagamentoController.findByCodigo( idFormaPagamento ) );
                            itemCaixa.setFkCaixa( caixa_actual );

                            try
                            {
                                item_caixa_controller.salvar( itemCaixa );
                            }
                            catch ( Exception e )
                            {
                                sucesso = false;
                                System.out.println( "Erro ao salvar ItemCaixa: " + e.getLocalizedMessage() );
                                DocumentosController.rollBackTransaction( conexaoTransaction );
                                break;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    sucesso = false;
                }
            }
        }

        if ( sucesso )
        {
            try
            {
                DocumentosController.commitTransaction( conexaoTransaction );
                NLExporToPdfForSandEmailReport exporToPdf = new NLExporToPdfForSandEmailReport( conexaoTransaction );
                conexaoTransaction.close();
                System.out.println( "Fecho realizado com sucesso!..." );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao processar os relatórios.\nFalha: " + e.getLocalizedMessage(),
                        "Falha", JOptionPane.ERROR_MESSAGE );
                DocumentosController.rollBackTransaction( conexaoTransaction );
                conexaoTransaction.close();
            }
        }
        else
        {
            conexaoTransaction.close();
        }
    }

    public static String getIpMaquina()
    {

        String ipDaMaquina = "";
        try
        {
            //pegamos o ip da maquina.
            ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
        }
        catch ( Exception e )
        {
            return "";
        }

        return ipDaMaquina;

    }

    public static String getNomeMaquina()
    {

        String nomeDaMaquina = "";
        try
        {
            //nome da maquina.
            nomeDaMaquina = InetAddress.getLocalHost().getHostName();

        }
        catch ( Exception e )
        {
        }

        return nomeDaMaquina;

    }

    public static void actualizarEstadoLog( String estado )
    {
        BDConexao conexaoLocal = new BDConexao();
        LogController logController = new LogController( conexaoLocal );
        String nomeMaquina = MetodosUtil.getNomeMaquina();
        Log log = logController.getLogByNomeMaquina( nomeMaquina );

        log.setEstado( estado );
        logController.actualizar( log );
        conexaoLocal.close();

    }

}
