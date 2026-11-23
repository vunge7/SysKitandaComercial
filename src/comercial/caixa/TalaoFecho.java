package comercial.caixa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import util.DVML;

/**
 * Talão térmico (80 mm) dos fechos de caixa: o fecho de cada utilizador e o
 * fecho geral do dia. O modelo é relatorios/talao_fecho_caixa.jrxml — genérico:
 * as linhas (título, secções, valores, documentos) são montadas aqui. No fim
 * leva um código QR com os números do fecho e um código de controlo, para se
 * confirmar mais tarde que um talão em papel não foi alterado.
 * 2026-11-14.
 */
public final class TalaoFecho {

    static final String MODELO = "talao_fecho_caixa";
    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat H = new SimpleDateFormat("dd/MM HH:mm");
    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private TalaoFecho() {
    }

    // =====================================================================
    // LINHAS
    // =====================================================================
    /** Linhas do talão (tipo, esq, dir). */
    public static final class Linhas {

        final List<Map<String, ?>> l = new ArrayList<>();

        public Linhas add(String tipo, String esq, String dir) {
            Map<String, Object> m = new HashMap<>();
            m.put("tipo", tipo);
            m.put("esq", esq == null ? "" : esq);
            m.put("dir", dir);
            l.add(m);
            return this;
        }

        public Linhas titulo(String t) {
            return add("T", t, null);
        }

        public Linhas centro(String t) {
            return add("C", t, null);
        }

        public Linhas seccao(String t, String dir) {
            return add("S", t, dir);
        }

        public Linhas linha(String e, String d) {
            return add("L", e, d);
        }

        public Linhas negrito(String e, String d) {
            return add("B", e, d);
        }

        public Linhas destaque(String e, String d) {
            return add("K", e, d);
        }

        public Linhas doc(String e, String d) {
            return add("D", e, d);
        }

        public Linhas nota(String t) {
            return add("N", t, null);
        }

        public Linhas separador() {
            return add("R", "", null);
        }
    }

    // =====================================================================
    // FECHO DE UM UTILIZADOR
    // =====================================================================
    public static Linhas linhasFecho(Connection cn, FechoCaixa.Resumo r) {
        Linhas t = new Linhas();
        cabecalho(cn, t);
        t.titulo("FECHO DE CAIXA");
        if (r.fechada()) {
            t.centro("Fecho Nº " + (r.numeroFecho == null ? "—" : r.numeroFecho) + "  ·  Caixa Nº " + r.pkCaixa);
        } else {
            t.centro("Caixa Nº " + r.pkCaixa + " — AINDA ABERTA (situação às " + DH.format(r.ate) + ")");
        }

        t.seccao("OPERADOR", null);
        t.linha("Utilizador", r.nomeUtilizador);
        t.linha("Perfil", r.tipoUtilizador.isEmpty() ? "—" : r.tipoUtilizador);
        if (!r.area.isEmpty()) {
            t.linha("Área", r.area);
        }
        t.linha("Abertura", DH.format(r.abertura));
        t.linha(r.fechada() ? "Fecho" : "Até", DH.format(r.ate));
        t.linha("Duração", duracao(r.abertura, r.ate));
        if (r.valorInicial != 0) {
            t.linha("Fundo de caixa inicial", m(BigDecimal.valueOf(r.valorInicial)));
        }

        t.seccao("RESUMO", null);
        t.linha("Vendas pagas (FR): " + r.numeroVendas, m(r.totalVendas));
        t.linha("Recibos emitidos (RC): " + r.numeroRecibos, m(r.totalRecibos));
        if (r.numeroVendas > 0) {
            t.linha("  IVA das vendas", m(r.totalIva));
            if (r.totalDesconto.signum() != 0) {
                t.linha("  Descontos", m(r.totalDesconto));
            }
        }
        t.destaque("TOTAL EM CAIXA", m(r.totalEsperado()));
        BigDecimal contado = r.totalContado();
        if (contado != null) {
            BigDecimal d = contado.subtract(r.totalEsperado());
            t.negrito("Contado pelo operador", m(contado));
            t.negrito(d.signum() == 0 ? "Diferença (certo)" : d.signum() > 0 ? "Sobra" : "Falta", sinal(d));
        }

        t.seccao("FORMAS DE PAGAMENTO", null);
        boolean alguma = false;
        for (FechoCaixa.Linha l : r.linhas) {
            if (l.esperado().signum() == 0 && l.contado == null) {
                continue;
            }
            alguma = true;
            t.negrito(l.nome, m(l.esperado()));
            if (l.recibos.signum() != 0 || l.devolucoes.signum() != 0 || l.caucoes.signum() != 0) {
                t.linha("  vendas FR", m(l.vendas));
                if (l.recibos.signum() != 0) {
                    t.linha("  recibos RC", m(l.recibos));
                }
                if (l.caucoes.signum() != 0) {
                    t.linha("  cauções recebidas", m(l.caucoes));
                }
                if (l.devolucoes.signum() != 0) {
                    t.linha("  devoluções de saldo", "-" + m(l.devolucoes));
                }
            }
            if (l.contado != null) {
                t.linha("  contado", m(l.contado));
                BigDecimal d = l.diferenca();
                t.linha(d.signum() == 0 ? "  diferença" : d.signum() > 0 ? "  sobra" : "  falta", sinal(d));
            }
        }
        if (!alguma) {
            t.nota("Sem pagamentos nesta caixa.");
        }

        if (!r.documentos.isEmpty()) {
            t.seccao("DOCUMENTOS DE PAGAMENTO", String.valueOf(r.documentos.size()));
            for (FechoCaixa.Documento d : r.documentos) {
                t.doc(d.numero + "  " + H.format(d.data) + (d.cliente.isEmpty() ? "" : "\n" + corta(d.cliente, 34))
                        + (d.formas.isEmpty() ? "" : " · " + d.formas)
                        + (d.tipo == FechoCaixa.DOC_RC ? "\nPaga: " + (d.origem.isEmpty() ? "FT não indicada no recibo" : compactar(d.origem)) : ""),
                        m(d.valor));
            }
        }
        if (r.numeroInvalidos > 0) {
            t.nota(r.numeroInvalidos + " documento(s) inválido(s) na AGT (" + m(r.totalInvalidos)
                    + " Kz) não contam neste fecho.");
        }

        if (!r.anulados.isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            t.seccao("FR / RC ANULADOS POR NC", String.valueOf(r.anulados.size()));
            t.nota("Emitidos nesta caixa e anulados por nota de crédito: já não contam no total em caixa.");
            for (FechoCaixa.Documento d : r.anulados) {
                t.doc(d.numero + "  " + H.format(d.data) + (d.cliente.isEmpty() ? "" : "\n" + corta(d.cliente, 34))
                        + (d.tipo == FechoCaixa.DOC_RC && !d.origem.isEmpty() ? "\nPagava: " + compactar(d.origem) : "")
                        + "\nAnulado pela " + (d.anuladaPor.isEmpty() ? "NC (número não encontrado)" : d.anuladaPor), m(d.valor));
                total = total.add(d.valor);
            }
            t.negrito("Total anulado", m(total));
        }

        if (!r.notasCredito.isEmpty()) {
            t.seccao("NOTAS DE CRÉDITO (NC)", String.valueOf(r.notasCredito.size()));
            t.nota("Notas de crédito emitidas por este operador nesta caixa: anulam a FR / RC de origem"
                    + " (que deixa de contar no total) e devolvem o stock.");
            for (FechoCaixa.Documento d : r.notasCredito) {
                t.doc(d.numero + "  " + H.format(d.data) + "\nAnula: " + d.origem
                        + (d.operador.isEmpty() ? "" : " (" + d.operador + ")")
                        + (d.cliente.isEmpty() ? "" : "\n" + corta(d.cliente, 34)), m(d.valor));
            }
            t.negrito("Total das notas de crédito", m(r.totalNotasCredito));
        }

        if (!r.consumosQuartos.isEmpty()) {
            t.seccao("LANÇADO NA CONTA DOS QUARTOS", String.valueOf(r.consumosQuartos.size()));
            t.nota("Consumos lançados nas contas dos quartos: não são pagamento, não entram no total em caixa.");
            for (String[] c : r.consumosQuartos) {
                t.doc(c[4] + " · Qto " + c[0] + "\n" + c[2] + " x " + corta(c[1], 34), m(new BigDecimal(c[3])));
            }
            t.negrito("Total lançado nos quartos", m(r.totalQuartos));
        }

        // as facturas a crédito (FT) não entram no fecho (pedido do utilizador em 2026-11-14)
        if (!r.produtos.isEmpty()) {
            t.seccao("PRODUTOS VENDIDOS (FR)", null);
            for (String[] p : r.produtos) {
                t.doc(p[0] + " x " + corta(p[1], 34), m(new BigDecimal(p[2])));
            }
        }

        t.separador();
        t.linha("Operador:", null);
        t.linha(" ", null);
        t.linha("____________________", null);
        t.linha("Conferido por:", null);
        t.linha(" ", null);
        t.linha("____________________", null);
        return t;
    }

    /** Conteúdo do QR do fecho de um utilizador. */
    public static String qrFecho(Connection cn, FechoCaixa.Resumo r) {
        StringBuilder s = new StringBuilder("KITANDA-FECHO");
        s.append("|NIF=").append(nif(cn));
        s.append("|FECHO=").append(r.numeroFecho == null ? "ABERTA" : String.valueOf(r.numeroFecho));
        s.append("|CAIXA=").append(r.pkCaixa);
        s.append("|USER=").append(r.idUsuario).append(':').append(r.nomeUtilizador);
        s.append("|DE=").append(ISO.format(r.abertura)).append("|ATE=").append(ISO.format(r.ate));
        s.append("|FR=").append(r.numeroVendas).append(':').append(r.totalVendas.toPlainString());
        s.append("|RC=").append(r.numeroRecibos).append(':').append(r.totalRecibos.toPlainString());
        s.append("|TOTAL=").append(r.totalEsperado().toPlainString());
        BigDecimal c = r.totalContado();
        if (c != null) {
            s.append("|CONTADO=").append(c.toPlainString());
        }
        if (r.totalQuartos.signum() != 0) {
            s.append("|QUARTOS=").append(r.totalQuartos.toPlainString());
        }
        if (!r.notasCredito.isEmpty()) {
            s.append("|NC=").append(r.notasCredito.size()).append(':').append(r.totalNotasCredito.toPlainString());
        }
        return comControlo(s.toString());
    }

    // =====================================================================
    // RELATÓRIO
    // =====================================================================
    public static JasperPrint gerar(Connection cn, Linhas linhas, String qr, String legenda) throws Exception {
        Map<String, Object> p = new HashMap<>();
        if (qr != null) {
            p.put("QR", qr(qr));
        }
        p.put("QR_LEGENDA", legenda);
        p.put("RODAPE", "Processado por computador · " + DH.format(new Date()) + " · Kitanda / DVML");
        return JasperFillManager.fillReport(caminhoModelo(), p, new JRMapCollectionDataSource(linhas.l));
    }

    /** O .jasper do modelo; compila o .jrxml se faltar ou estiver desactualizado. */
    static synchronized String caminhoModelo() throws Exception {
        File jasper = new File(DVML.CAMINHO_REPORT + MODELO + ".jasper");
        File jrxml = new File(DVML.CAMINHO_REPORT + MODELO + ".jrxml");
        if (jrxml.exists() && (!jasper.exists() || jasper.lastModified() < jrxml.lastModified())) {
            try {
                JasperCompileManager.compileReportToFile(jrxml.getPath(), jasper.getPath());
            } catch (Throwable e) {
                // pasta só de leitura (Program Files) ou datas trocadas pelo git/cópia: o .jasper distribuído serve
                if (!jasper.exists()) {
                    throw e instanceof Exception ? (Exception) e : new Exception(e);
                }
                System.err.println("[Talão de fecho] não foi possível recompilar " + jrxml.getName() + " (" + e + "); uso o .jasper existente");
            }
        }
        return jasper.getAbsolutePath();
    }

    static BufferedImage qr(String texto) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bm = new QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, 300, 300, hints);
        return MatrixToImageWriter.toBufferedImage(bm);
    }

    /** Acrescenta |CTRL=xxxxxxxx (8 primeiros caracteres do SHA-256 do texto). */
    static String comControlo(String s) {
        return s + "|CTRL=" + controlo(s);
    }

    public static String controlo(String s) {
        try {
            byte[] h = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder x = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                x.append(String.format("%02X", h[i]));
            }
            return x.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // =====================================================================
    // AUXILIARES
    // =====================================================================
    static void cabecalho(Connection cn, Linhas t) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT nome, nome, nif, enderecos, telefone FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String nome = FechoCaixa.texto(rs.getString(1));
                String empresa = FechoCaixa.texto(rs.getString(2));
                if (nome.isEmpty()) {
                    nome = empresa;
                }
                t.add("C", nome.toUpperCase(), null);
                if (!empresa.isEmpty() && !empresa.equalsIgnoreCase(nome)) {
                    t.centro(empresa);
                }
                String nif = FechoCaixa.texto(rs.getString(3));
                if (!nif.isEmpty()) {
                    t.centro("NIF: " + nif);
                }
                String morada = FechoCaixa.texto(rs.getString(4));
                if (!morada.isEmpty()) {
                    t.centro(morada);
                }
                String tel = FechoCaixa.texto(rs.getString(5));
                if (!tel.isEmpty()) {
                    t.centro("Tel.: " + tel);
                }
            }
        } catch (Exception e) {
            t.centro("Kitanda");
        }
    }

    static String nif(Connection cn) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT nif FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? FechoCaixa.texto(rs.getString(1)) : "";
        } catch (Exception e) {
            return "";
        }
    }

    static String m(BigDecimal v) {
        return EnvioFechoCaixa.moeda(v);
    }

    static String sinal(BigDecimal d) {
        return (d.signum() > 0 ? "+" : "") + m(d);
    }

    /**
     * "FT FT2526S53356N/335, FT FT2526S53356N/345" → "FT2526S53356N/335, 345":
     * a mesma série escreve-se uma vez.
     */
    static String compactar(String lista) {
        StringBuilder s = new StringBuilder();
        String serieAnterior = null;
        for (String p : lista.split(",")) {
            String n = p.trim();
            int esp = n.indexOf(' ');
            if (esp > 0 && esp <= 3) {
                n = n.substring(esp + 1).trim(); // tira o "FT " repetido
            }
            int barra = n.lastIndexOf('/');
            String serie = barra > 0 ? n.substring(0, barra) : null;
            s.append(s.length() == 0 ? "" : ", ");
            s.append(serie != null && serie.equals(serieAnterior) ? n.substring(barra + 1) : n);
            serieAnterior = serie;
        }
        return s.toString();
    }

    static String corta(String s, int n) {
        return s == null ? "" : s.length() <= n ? s : s.substring(0, n - 1) + "…";
    }

    static String duracao(Date de, Date ate) {
        long min = Math.max(0, (ate.getTime() - de.getTime()) / 60000);
        long dias = min / 1440, h = (min % 1440) / 60, mm = min % 60;
        return (dias > 0 ? dias + " d " : "") + h + " h " + String.format("%02d", mm) + " min";
    }
}
