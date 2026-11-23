package comercial.caixa;

import comercial.email.EmailDados;
import comercial.email.EmailEnvio;
import comercial.whatsapp.RelatorioWhatsApp;
import comercial.whatsapp.WhatsAppDados;
import comercial.whatsapp.WhatsAppEnvio;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import util.BDConexao;
import util.DVML;

/**
 * Depois de cada fecho de caixa (Recepção, Restaurante, Bar, Hospedaria, POS):
 * o PDF da reconciliação (o mesmo talão que se imprime) e o envio automático aos
 * contactos que recebem os fechos — "Relatórios por E-mail" e "Relatórios por
 * WhatsApp", caixa "Recebe os fechos de caixa" — e ao e-mail do fecho dos dados
 * da instituição (que já o recebia, com o relatório mensal).
 * Pedido do utilizador em 2026-11-14.
 *
 * O envio corre numa thread própria com ligação própria: a recepção não fica à
 * espera da internet. Tudo fica no histórico de cada canal.
 */
public final class EnvioFechoCaixa {

    public static final File PASTA = new File("anexos" + File.separator + "fechos");
    private static final Locale PT = new Locale("pt", "PT");
    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private EnvioFechoCaixa() {
    }

    /**
     * O talão do fecho (relatorios/talao_fecho_caixa): pagamentos FR, recibos RC,
     * documentos, consumos lançados nos quartos, facturas a crédito, produtos e QR.
     * Substitui o reconciliacao_caixa*.jasper, que só lia FR (com só recibos saía vazio).
     */
    public static JasperPrint gerar(Connection cn, FechoCaixa.Resumo r) throws Exception {
        return TalaoFecho.gerar(cn, TalaoFecho.linhasFecho(cn, r), TalaoFecho.qrFecho(cn, r),
                "QR de controlo do fecho" + (r.numeroFecho == null ? "" : " Nº " + r.numeroFecho));
    }

    /** Guarda o talão em anexos/fechos/Fecho_Caixa_N_utilizador_data.pdf. */
    public static File guardarPdf(JasperPrint jp, FechoCaixa.Resumo r, String utilizador) throws Exception {
        PASTA.mkdirs();
        String nome = "Fecho_Caixa_" + (r.numeroFecho != null ? String.valueOf(r.numeroFecho) : "caixa" + r.pkCaixa) + "_" + utilizador.replaceAll("[^A-Za-z0-9]+", "_") + "_"
                + new SimpleDateFormat("yyyy-MM-dd_HHmm").format(r.fecho) + ".pdf";
        File f = new File(PASTA, nome);
        JasperExportManager.exportReportToPdfFile(jp, f.getPath());
        return f;
    }

    /** Texto do fecho (corpo do e-mail / legenda do WhatsApp). */
    public static String texto(FechoCaixa.Resumo r, String utilizador, String area, String hotel) {
        StringBuilder s = new StringBuilder();
        s.append("*").append(hotel).append("* · Fecho de caixa Nº ").append(r.numero())
                .append(area == null || area.isEmpty() ? "" : " · " + area).append("\n");
        s.append("Utilizador: ").append(utilizador)
                .append(r.tipoUtilizador.isEmpty() ? "" : " (" + r.tipoUtilizador + ")").append("\n");
        s.append("Aberta: ").append(DH.format(r.abertura)).append(r.fechada() ? " · Fechada: " : " · Até: ").append(DH.format(r.ate)).append("\n");
        s.append("Vendas (FR): ").append(r.numeroVendas).append(" · ").append(moeda(r.totalVendas)).append(" Kz\n");
        if (r.numeroRecibos > 0) {
            s.append("Recibos (RC): ").append(r.numeroRecibos).append(" · ").append(moeda(r.totalRecibos)).append(" Kz\n");
        }
        for (FechoCaixa.Linha l : r.linhas) {
            if (l.esperado().signum() == 0 && l.contado == null) {
                continue;
            }
            s.append("· ").append(l.nome).append(": ").append(moeda(l.esperado()));
            if (l.contado != null) {
                s.append(" (contado ").append(moeda(l.contado));
                BigDecimal d = l.diferenca();
                s.append(d.signum() == 0 ? ", certo)" : ", diferença " + (d.signum() > 0 ? "+" : "") + moeda(d) + ")");
            }
            s.append("\n");
        }
        if (r.totalQuartos.signum() != 0) {
            s.append("Lançado na conta dos quartos (não entra no total): ").append(moeda(r.totalQuartos)).append(" Kz\n");
        }
        if (!r.notasCredito.isEmpty()) {
            s.append("Notas de crédito (NC): ").append(r.notasCredito.size()).append(" · ").append(moeda(r.totalNotasCredito)).append(" Kz");
            for (FechoCaixa.Documento d : r.notasCredito) {
                s.append("\n  ").append(d.numero).append(" anula ").append(d.origem);
            }
            s.append("\n");
        }
        s.append("Total em caixa: ").append(moeda(r.totalEsperado())).append(" Kz");
        BigDecimal contado = r.totalContado();
        if (contado != null) {
            BigDecimal d = contado.subtract(r.totalEsperado());
            s.append(" · contado ").append(moeda(contado)).append(" Kz")
                    .append(d.signum() == 0 ? " (certo)" : " (diferença " + (d.signum() > 0 ? "+" : "") + moeda(d) + " Kz)");
        }
        return s.toString();
    }

    /**
     * Envia o PDF do fecho, em segundo plano, a quem recebe os fechos.
     *
     * @param mensal relatório mensal (só para o e-mail do fecho da instituição, como antes); pode ser null
     */
    public static void enviarEmSegundoPlano(FechoCaixa.Resumo r, String utilizador, String area, File pdf, File mensal, Integer usuario) {
        Thread t = new Thread(() -> enviar(r, utilizador, area, pdf, mensal, usuario), "Envio-Fecho-" + r.numero());
        t.setDaemon(false); // termina o envio mesmo que fechem o formulário
        t.start();
    }

    static void enviar(FechoCaixa.Resumo r, String utilizador, String area, File pdf, File mensal, Integer usuario) {
        try (Connection cn = BDConexao.novaLigacao()) {
            String corpo = texto(r, utilizador, area, nomeHotel(cn));
            enviarPdf(cn, "Fecho de caixa Nº " + r.numero() + " · " + utilizador, corpo, pdf, mensal != null ? mensal : gerarMensal(cn), usuario);
        } catch (Throwable e) {
            log("envio do fecho " + r.numero() + " falhou: " + e);
        }
    }

    /**
     * Envia um PDF de fecho (de um utilizador ou o fecho geral) a quem recebe os
     * fechos: e-mail (contactos + e-mail do fecho da instituição) e WhatsApp.
     *
     * @return quantos envios foram entregues
     */
    public static int enviarPdf(Connection cn, String titulo, String corpo, File pdf, File mensal, Integer usuario) {
        int ok = 0;
        try {
            EmailDados.garantirTabelas(cn);
            WhatsAppDados.garantirTabelas(cn);
            RelatorioWhatsApp.Resultado fecho = RelatorioWhatsApp.documento(RelatorioWhatsApp.RESUMO_DIA, pdf, corpo, titulo);

            // ---- e-mail: contactos que recebem os fechos + e-mail do fecho da instituição
            EmailEnvio email = EmailEnvio.daBase(cn);
            List<String> enviados = new ArrayList<>();
            if (email.problemaConfiguracao() == null) {
                for (EmailDados.Contacto c : EmailDados.contactosFecho(cn)) {
                    ok += enviarEmail(cn, email, c.id, c.nome, c.email, lista(fecho), titulo, usuario);
                    enviados.add(c.email);
                }
                String emailFecho = emailFechoInstituicao(cn);
                if (emailFecho != null && !enviados.contains(emailFecho)) {
                    List<RelatorioWhatsApp.Resultado> anexos = lista(fecho);
                    if (mensal != null && mensal.exists()) {
                        anexos.add(RelatorioWhatsApp.documento(RelatorioWhatsApp.RESUMO_DIA, mensal,
                                "Relatório mensal (em anexo).", "Relatório mensal"));
                    }
                    ok += enviarEmail(cn, email, null, "E-mail do fecho (dados da instituição)", emailFecho, anexos, titulo, usuario);
                }
            } else {
                log("e-mail não configurado: " + email.problemaConfiguracao());
            }

            // ---- WhatsApp: contactos que recebem os fechos
            WhatsAppEnvio whats = WhatsAppEnvio.daBase(cn);
            List<WhatsAppDados.Contacto> paraWhats = WhatsAppDados.contactosFecho(cn);
            if (!paraWhats.isEmpty()) {
                String problema = whats.problemaConfiguracao();
                for (WhatsAppDados.Contacto c : paraWhats) {
                    try {
                        if (problema != null) {
                            throw new IllegalStateException(problema);
                        }
                        String detalhe = whats.enviar(c.numero, fecho);
                        WhatsAppDados.registarEnvio(cn, c.id, c.nome, c.numero, titulo, WhatsAppDados.MODO_AUTOMATICO,
                                whats.simulacao() ? WhatsAppDados.ESTADO_SIMULADO : WhatsAppDados.ESTADO_ENVIADO, detalhe, usuario);
                        ok++;
                        log("WhatsApp \"" + titulo + "\" para " + c.nome + ": enviado");
                    } catch (Exception e) {
                        String erro = e.getMessage() == null ? e.toString() : e.getMessage();
                        WhatsAppDados.registarEnvio(cn, c.id, c.nome, c.numero, titulo, WhatsAppDados.MODO_AUTOMATICO,
                                WhatsAppDados.ESTADO_FALHOU, erro, usuario);
                        log("WhatsApp \"" + titulo + "\" para " + c.nome + " falhou: " + erro);
                    }
                }
            }
        } catch (Throwable e) {
            log("envio de \"" + titulo + "\" falhou: " + e);
        }
        return ok;
    }

    /** O relatório mensal (anexos/relatorio_mensal.pdf) que o e-mail do fecho da instituição já recebia. null se falhar. */
    static File gerarMensal(Connection cn) {
        try {
            java.util.Calendar k = java.util.Calendar.getInstance();
            Map<String, Object> p = new HashMap<>();
            p.put("ano", k.get(java.util.Calendar.YEAR));
            p.put("mes", k.get(java.util.Calendar.MONTH) + 1);
            p.put("mes_designacao", util.MetodosUtil.getMesPagarU(k.get(java.util.Calendar.MONTH) + 1));
            JasperPrint jp = JasperFillManager.fillReport(new File(DVML.CAMINHO_REPORT + "relatorio_mensal.jasper").getAbsolutePath(), p, cn);
            File f = new File("anexos" + File.separator + "relatorio_mensal.pdf");
            f.getParentFile().mkdirs();
            JasperExportManager.exportReportToPdfFile(jp, f.getPath());
            return f;
        } catch (Throwable e) {
            log("relatório mensal não gerado: " + e);
            return null;
        }
    }

    private static int enviarEmail(Connection cn, EmailEnvio email, Integer id, String nome, String para,
            List<RelatorioWhatsApp.Resultado> anexos, String titulo, Integer usuario) {
        try {
            String detalhe = email.enviar(cn, para, anexos);
            EmailDados.registarEnvio(cn, id, nome, para, titulo, EmailDados.MODO_AUTOMATICO, EmailDados.ESTADO_ENVIADO, detalhe, usuario);
            log("e-mail do fecho para " + para + ": enviado");
            return 1;
        } catch (Exception e) {
            String erro = e.getMessage() == null ? e.toString() : e.getMessage();
            EmailDados.registarEnvio(cn, id, nome, para, titulo, EmailDados.MODO_AUTOMATICO, EmailDados.ESTADO_FALHOU, erro, usuario);
            log("e-mail do fecho para " + para + " falhou: " + erro);
            return 0;
        }
    }

    private static List<RelatorioWhatsApp.Resultado> lista(RelatorioWhatsApp.Resultado r) {
        List<RelatorioWhatsApp.Resultado> l = new ArrayList<>();
        l.add(r);
        return l;
    }

    private static String emailFechoInstituicao(Connection cn) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT email FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? EmailDados.normalizarEmail(rs.getString(1)) : null;
        } catch (Exception e) {
            return null;
        }
    }

    static String nomeHotel(Connection cn) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT nome, nome FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String n = rs.getString(1);
                if (n == null || n.trim().isEmpty()) {
                    n = rs.getString(2);
                }
                if (n != null && !n.trim().isEmpty()) {
                    return n.trim();
                }
            }
        } catch (Exception e) {
            // nome por omissão
        }
        return "Kitanda";
    }

    static String moeda(BigDecimal v) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT)).format(v == null ? BigDecimal.ZERO : v);
    }

    private static void log(String m) {
        System.out.println("[Fecho] " + m);
    }
}
