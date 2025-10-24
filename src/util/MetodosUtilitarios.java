/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import controlador.DbBackupScheduleJpaController;
import dao.*;
import entity.AnoEconomico;
import entity.Compras;
import entity.DbBackupSchedule;
import entity.Documento;
import entity.Empresa;
import entity.ItemCompras;
import entity.Notas;
import entity.NotasItem;
import entity.TbArmazem;
import entity.TbItemSubsidiosFuncionario;
import entity.TbPreco;
import entity.Promocao;
import entity.TbFuncionario;
import entity.TbItemVenda;
import entity.TbProduto;
import entity.TbStock;
import entity.TbStockMirrow;
import entity.TbUsuario;
import entity.TbValidade;
import entity.TbVenda;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import kitanda.util.CfConstantes;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import static util.DVML.*;
import static util.JPAEntityMannagerFactoryUtil.em;
import visao.BDBackupJFrame;
import visao.BuscaProdutoVisao;
import visao.CompraVisao;
import visao.FicheiroSAFTVisao;
import visao.LoginVisao;
import visao.RootVisao;

/**
 *
 * @author DVML COMERCIAL LDA
 */
public class MetodosUtilitarios {

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ValidadeDao validadeDao = new ValidadeDao(emf);
    private static ArmazemDao armazemDao = new ArmazemDao(emf);
    private static ProdutoDao produtoDao = new ProdutoDao(emf);
    private static StockMirrowDao stockMirrowDao = new StockMirrowDao(emf);
    private static StockDao stockDao = new StockDao(emf);
    private static PrecoDao precoDao = new PrecoDao(emf);
    private static UsuarioDao usuarioDao = new UsuarioDao(emf);
    private static AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao(emf);
    private static ProdutoImpostoDao produtoImpostoDao = new ProdutoImpostoDao(emf);
    private static ProdutoIsentoDao produtoIsentoDao = new ProdutoIsentoDao(emf);
    private static List<String> lista_alienas = new ArrayList<>();

    private static String criptografia_hash_venda(TbVenda venda, double parmGrossTotal, BDConexao conexao) {

        String final_hash = "";
        try {
            String invoiceDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD).format(venda.getDataVenda());
            String systemEntryDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD_T_HH_MM_SS).format(venda.getDataVenda());
            String invoiceNo = venda.getCodFact();
            System.err.println("InvoiceNO: " + invoiceNo);

//            double grossoTotal = venda.getTotalGeral () + getTotalVendaIVASemIncluirDesconto ( venda.getTbItemVendaList () );
            String grossoTotal = getValorCasasDecimais(parmGrossTotal, CASAS_DECIMAIS);
            int cod_venda = (Objects.isNull(venda.getCodigo()) || venda.getCodigo() == 0) ? VendaDao.getLastVendaByTipoDocumento(venda.getFkDocumento().getPkDocumento(), conexao) + 1 : venda.getCodigo();
            String hash = new VendaDao(emf).getHashAnterior(cod_venda, invoiceNo, venda.getFkDocumento().getPkDocumento(), venda.getFkAnoEconomico().getPkAnoEconomico(), conexao).trim();
            System.out.println("COD HASH ANTERIOR: " + hash);

            String mensagem = String.format("%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash);

            System.out.println("Parm GrossTotal: " + parmGrossTotal);
            System.err.println("CodFact Anterior: " + hash);
            System.err.println("HashAnterior: " + hash);
            System.err.println("Mensagem: " + mensagem);

            //1º PASSO: Guardar a mensagem a assinar
            File file = new File(PATH + "Registo.txt");
            MetodosUtilitarios.escreverNoDocumento(mensagem, file, "");

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC(assinatura, true);

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC(encoding, true);

            Scanner scanner = new Scanner(new File(PATH + "Registo.b64"));

            if (scanner.hasNext()) {
                final_hash = scanner.nextLine();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetodosUtilitarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return final_hash;
    }
    
        public static Process rodarComandoMAC(String comando, boolean waitFor) {
        Process process_rodar_bat_Stream = null;

        try {
            Runtime terminal = Runtime.getRuntime();
            //System.err.println( "Espaço de memória: " + ( terminal.freeMemory() / 1024 ) );

            process_rodar_bat_Stream = terminal.exec(comando);
            InputStream inputStream = process_rodar_bat_Stream.getInputStream();

            if (waitFor) {
                process_rodar_bat_Stream.waitFor();
            }

            exibirSaida(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BDBackupJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(BDBackupJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return process_rodar_bat_Stream;
        }

    }
        
            public static void exibirSaida(InputStream inputStream) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

            String linha = "";

            while ((linha = input.readLine()) != null) {
                System.err.println(linha);
            }
        } catch (IOException ex) {
            Logger.getLogger(MetodosUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String criptografia_hash_nota(Notas notas, double parmGrossTotal, BDConexao conexao) {
        String final_hash = "";
        try {
            String invoiceDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD).format(notas.getDataNota());
            String systemEntryDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD_T_HH_MM_SS).format(notas.getDataNota());
            String invoiceNo = notas.getCodNota();
            System.err.println("InvoiceNO: " + invoiceNo);
//            double grossoTotal = venda.getTotalGeral () + getTotalVendaIVASemIncluirDesconto ( venda.getTbItemVendaList () );
            String grossoTotal = getValorCasasDecimais(parmGrossTotal, CASAS_DECIMAIS);
            int pk_nota = (Objects.isNull(notas.getPkNota()) || notas.getPkNota() == 0) ? NotasDao.getLastNota(notas.getFkDocumento().getPkDocumento(), conexao) + 1 : notas.getPkNota();
            String hash = new NotasDao(emf).getHashAnterior(pk_nota, conexao).trim();

            String mensagem = String.format("%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash);

            System.out.println("GrossTotal: " + parmGrossTotal);
            System.err.println("HashAnterior: " + hash);
            System.err.println("Mensagem: " + mensagem);
            System.err.println("\n");
            //1º PASSO: Guardar a mensagem a assinar
            File file = new File(PATH + "Registo.txt");
            MetodosUtilitarios.escreverNoDocumento(mensagem, file, "");

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC(assinatura, true);

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC(encoding, true);

            Scanner scanner = new Scanner(new File(PATH + "Registo.b64"));

            if (scanner.hasNext()) {
                final_hash = scanner.nextLine();
            }

        } catch (FileNotFoundException ex) {
        }

        return final_hash;
    }


    public static void main(String[] args) throws ParseException {

    }

    public static String criptografia_hash(Object documento, double parmGrossTotal, BDConexao conexao) {
        String final_hash = "";

        if (documento instanceof TbVenda) {
            System.err.println(" **********************DOC VENDA******************************");
            final_hash = criptografia_hash_venda((TbVenda) documento, parmGrossTotal, conexao);
        } else if (documento instanceof Notas) {
            System.err.println(" **********************DOC NOTAS******************************");
            final_hash = criptografia_hash_nota((Notas) documento, parmGrossTotal, conexao);
        } else if (documento instanceof Compras) {
            System.err.println(" **********************DOC COMPRAS ******************************");
            final_hash = criptografia_hash_compras((Compras) documento, parmGrossTotal, conexao);
        }

        System.err.println("final_hash: " + final_hash);
        System.err.println("final_hash:LENGTH: " + final_hash.length());
        System.err.println("****************************************************");
        return final_hash;
    }

    public static String criptografia_hash(String formato_documento) {
        System.out.println("formato_documento: " + formato_documento);
        String valor = formato_documento.trim().replace(" ", "") + DVML.CHAVE_PRIVADA;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] bs = messageDigest.digest(valor.getBytes());
            BigInteger no = new BigInteger(1, bs);
            String hashNext = no.toString(16);
            while (hashNext.length() < 32) {
                hashNext = "0" + hashNext;
            }
            System.out.println("codhash: " + hashNext + " tamanho: " + hashNext.length());
            return hashNext;
        } catch (Exception e) {
        }

        return "";

    }
    
    private static String criptografia_hash_compras(Compras compras, double parmGrossTotal, BDConexao conexao) {

        String final_hash = "";
        try {
            String invoiceDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD).format(compras.getDataCompra());
            String systemEntryDate = new SimpleDateFormat(CfConstantes.YYYY_MM_DD_T_HH_MM_SS).format(compras.getDataCompra());
            String invoiceNo = compras.getCodFact();
            System.err.println("InvoiceNO: " + invoiceNo);
            String grossoTotal = getValorCasasDecimais(parmGrossTotal, CASAS_DECIMAIS);
            String hash = new ComprasDao(emf).getHashAnterior(invoiceNo, conexao).trim();
            String mensagem = String.format("%s;%s;%s;%s;%s", invoiceDate, systemEntryDate, invoiceNo, grossoTotal, hash);
            System.out.println("Parm GrossTotal: " + parmGrossTotal);
            System.err.println("HashAnterior: " + hash);
            System.err.println("Mensagem: " + mensagem);

            //1º PASSO: Guardar a mensagem a assinar
            File file = new File(PATH + "Registo.txt");
            MetodosUtil.escreverNoDocumento(mensagem, file, "");

            //2º PASSO: Assinar a mensagem contida no ficheiro Registo.txt
            String assinatura = "openssl dgst -sha1 -sign " + PATH + "ChavePrivada.pem -out " + PATH + "Registo.shal " + PATH + "Registo.txt";
            rodarComandoMAC(assinatura, true);

            //3º PASSO: Gerar o encoding para base 64 do ficheiro Registo1.shal
            String encoding = "openssl enc -base64 -in " + PATH + "Registo.shal -out " + PATH + "Registo.b64 -A";
            rodarComandoMAC(encoding, true);

            Scanner scanner = new Scanner(new File(PATH + "Registo.b64"));

            if (scanner.hasNext()) {
                final_hash = scanner.nextLine();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetodosUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return final_hash;
    }

    public static String assinatura_doc(String hash) {
        String assinatura = "" + hash.charAt(0) + "" + hash.charAt(10) + "" + hash.charAt(20) + "" + hash.charAt(30);
        return assinatura;
    }

    public static void escreverNoDocumento(String texto, File documentoDeTexto, boolean... mudarDeLinha) {
        try (FileWriter fileWriter = new FileWriter(documentoDeTexto)) {
            try (BufferedWriter bw = new BufferedWriter(fileWriter)) {
                if (mudarDeLinha.length > 0) {
                    bw.newLine();
                }

                bw.write(texto);
                JOptionPane.showMessageDialog(null, "Ficheiro SAF-T criado com sucesso!...");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MetodosUtilitarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao criar o ficheiro", "ERRO", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MetodosUtilitarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void escreverNoDocumento(String texto, File documentoDeTexto, String mensagem, boolean... mudarDeLinha) {
        try (FileWriter fileWriter = new FileWriter(documentoDeTexto)) {
            try (BufferedWriter bw = new BufferedWriter(fileWriter)) {
                if (mudarDeLinha.length > 0) {
                    bw.newLine();
                }

                bw.write(texto);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MetodosUtilitarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao criar o ficheiro", "ERRO", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MetodosUtilitarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String formateDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formateDateSAFT(Date date) {
        return new SimpleDateFormat(CfConstantes.YYYY_MM_DD_T_HH_MM_SS).format(date);
    }

    public static double getTaxaPercantagem(int idProduto) {
        return produtoImpostoDao.getTaxaByIdProduto(idProduto);
    }

    public static String getMotivoIsensao(int idProduto) {
        return produtoIsentoDao.getRegimeIsensaoByIdProduto(idProduto);
    }

    public static String getCodigoRegime(int idProduto) {
        return produtoIsentoDao.getCodigoRegimeByIdProduto(idProduto);
    }

    public static String getTaxType(int idProduto) {
        if (produtoImpostoDao.exist(idProduto)) {
            return "IVA";
        }
        return "NS";
    }

    public static String getTaxCode(int idProduto) {
        if (produtoImpostoDao.exist(idProduto)) {
            return "NOR";
        }
        return "NS";
    }

    public static String getValorCasasDecimais(double valor, int casas_dezimas) {

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(casas_dezimas);
        String valor_string = decimalFormat.format(valor).replace(".", "");
        return valor_string.replaceFirst(",", ".");

    }

    public static double getTotalSemIva(List<TbVenda> list, int pk_documento) {

        double soma = 0;
        for (TbVenda venda : list) {
            // soma += (venda.getTotalGeral() - venda.getTotalIva());
            //TOTAL ILÍQUIDO
            soma += getNetTotal(venda.getTbItemVendaList());
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += (venda.getTotalGeral() - venda.getTotalIva());
//            }

        }
        return soma;
    }

    public static double getTotalIliquido(List<TbItemVenda> list) {

        double soma = 0, preco = 0;
        double qtd = 0d;
        for (TbItemVenda linha : list) {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            soma += (preco * qtd);
        }
        return soma;
    }

    /*não desconta o 'desconto'*/
    public static double getTotalIliquidoMaisIVA(List<TbItemVenda> list, double imposto) {
        double taxa = 0, total = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for (TbItemVenda linha : list) {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            sub_total_iliquido = (preco * qtd);
            taxa = (linha.getValorIva() / 100);
            total += sub_total_iliquido + ((sub_total_iliquido * taxa));
        }

        return total;
    }

    /*Vizualizar Taxa do Iva*/
    public static double getTotalVendaIVASemIncluirDesconto(List<TbItemVenda> list) {
        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for (TbItemVenda linha : list) {
            if (linha.getValorIva() > 0) {
                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
                qtd = linha.getQuantidade();
                sub_total_iliquido = (preco * qtd);
                taxa = (linha.getValorIva() / 100);
                total_iva += ((sub_total_iliquido * taxa));
            } else {
                total_iva += linha.getValorIva();

            }

        }

        return total_iva;
    }

    public static double getGrossTotal(List<TbItemVenda> list) {
        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for (TbItemVenda linha : list) {
            if (linha.getValorIva() > 0) {
                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
                qtd = linha.getQuantidade();
                sub_total_iliquido = (preco * qtd);
                taxa = (linha.getValorIva() / 100);
                total_iva += sub_total_iliquido + ((sub_total_iliquido * taxa));
            } else {
                preco = linha.getFkPreco().getPrecoVenda().doubleValue();
                qtd = linha.getQuantidade();
                sub_total_iliquido = (preco * qtd);
                total_iva += sub_total_iliquido;

            }

        }

        return total_iva;
    }

    public static double getNetTotal(List<TbItemVenda> list) {
        double taxa = 0, total_net_total = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for (TbItemVenda linha : list) {

            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            sub_total_iliquido = (preco * qtd);
            total_net_total += sub_total_iliquido;

        }

        return total_net_total;
    }
//

    /*sem eventual 'desconto'*/
    public static double getTotalNotaIVASemIncluirDesconto(List<NotasItem> list) {
        double taxa = 0, total_iva = 0, preco = 0, sub_total_iliquido = 0;
        double qtd = 0d;

        for (NotasItem linha : list) {
            preco = linha.getFkPreco().getPrecoVenda().doubleValue();
            qtd = linha.getQuantidade();
            sub_total_iliquido = (preco * qtd);
            taxa = (linha.getValorIva() / 100);
            total_iva += ((sub_total_iliquido * taxa));
        }

        return total_iva;
    }

    public static double getTotalIVASobreTotalIliquido(List<TbItemVenda> list, double imposto) {
        double taxa = (imposto / 100);
        return (getTotalIliquido(list) * taxa);
    }

    public static double getTotalNotasCreditoSemIva(List<Notas> list, int pk_documento) {

        double soma = 0;
        if (!Objects.isNull(list)) {
            for (Notas nota : list) {
//            //soma += (venda.getTotalGeral() - venda.getTotalIva());
//            if ( nota.getFkDocumento().getPkDocumento() == pk_documento )
//            {
//                soma += ( nota.getTotalGeral() - nota.getTotalIva() );
//            }
                if (nota.getFkDocumento().getPkDocumento() == pk_documento && !nota.getEstado().equalsIgnoreCase(DVML.ESTADO_NOTA.ANULADO.toString())) {
                    soma += nota.getTotalGeral();
                }

            }

        }

        return soma;
    }

    public static double getTotalComIva(List<TbVenda> list, int pk_documento) {

        double soma = 0;
        for (TbVenda venda : list) {
            soma += venda.getTotalGeral().doubleValue();
//            if (venda.getFkDocumento().getPkDocumento() == pk_documento) {
//                soma += venda.getTotalGeral();
//            }

        }
        return soma;
    }


    public static List<TbProduto> getProdutosIsentosNotas(List<NotasItem> linhas) {

        List<TbProduto> produtos = new ArrayList<>();
        for (NotasItem linha : linhas) {
            if (linha.getValorIva() == 0d) {
                produtos.add(linha.getFkProduto());
            }
        }

        return produtos;
    }

    public static String getMotivoIsensaoProdutos(List<TbProduto> list_produto) {
        String motivo = "", motivo_linha, linha;;
        
        return motivo;
    }

    public static String getHash(String formato_documento) {
        String valor = "" + DVML.CHAVE_PRIVADA + formato_documento.trim().replace(" ", "");
        String hexa = "";
        //String s = "FR 2020/2";
        try {
            //MessageDigest m = MessageDigest.getInstance( "MD5" );
            MessageDigest m = MessageDigest.getInstance("SHA-256");

            m.update(valor.getBytes(), 0, valor.length());

            byte[] digest = m.digest();

            hexa = new BigInteger(1, digest).toString(16).toUpperCase();

            System.out.println("MD5: " + hexa);
            System.out.println("TAMANHO: " + hexa.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexa;

    }

    public static byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    public static double getValorComIVA(double qtd, double taxa, double preco, double desconto) {

        double subtotal_linha = (preco * qtd);
        double desconto_valor = (subtotal_linha * (desconto / 100));
        double valor_iva = 1 + (taxa / 100);//
        return ((subtotal_linha - desconto_valor) * valor_iva);

    }

    public static Date stringToDate(String stringDate, String formato) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }



}
