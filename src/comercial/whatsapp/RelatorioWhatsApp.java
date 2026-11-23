package comercial.whatsapp;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import util.DVML;

/**
 * Relatórios que se podem enviar por WhatsApp e por e-mail (comercial: vendas,
 * documentos, stock e validades). Cada um gera-se sem janelas
 * (para o envio automático) e dá um PDF e/ou uma mensagem de texto.
 *
 * O código (name()) é o que fica gravado em tb_whatsapp_agendamento.relatorios:
 * não mudar os nomes das constantes.
 */
public enum RelatorioWhatsApp {

    RESUMO_DIA("Resumo do dia (só mensagem)", "Vendas de ontem e de hoje, facturas emitidas, caixas abertas, os mais vendidos e alertas de stock, numa mensagem curta"),
    VENDAS_ONTEM("Vendas de ontem", "Todas as facturas (FR, FT, RC e NC) do dia anterior"),
    VENDAS_HOJE("Vendas de hoje (até à hora do envio)", "Todas as facturas do dia, até ao momento do envio"),
    MAIS_VENDIDOS("Produtos mais vendidos (ontem)", "Os 15 produtos com mais valor facturado no dia anterior (só mensagem)"),
    STOCK_BAIXO("Stock baixo (só mensagem)", "Produtos com a quantidade igual ou abaixo do mínimo (quantidade crítica) em cada armazém"),
    PRODUTOS_EXPIRAR("Produtos a expirar (só mensagem)", "Lotes com stock que expiram nos próximos 30 dias ou já expirados (farmácias e minimercados)");

    public final String titulo;
    public final String descricao;

    RelatorioWhatsApp(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return titulo;
    }

    public static List<RelatorioWhatsApp> daLista(String csv) {
        List<RelatorioWhatsApp> l = new ArrayList<>();
        if (csv != null) {
            for (String s : csv.split(",")) {
                try {
                    l.add(valueOf(s.trim()));
                } catch (IllegalArgumentException e) {
                    // relatório que deixou de existir: ignora
                }
            }
        }
        return l;
    }

    public static String paraLista(List<RelatorioWhatsApp> l) {
        StringBuilder sb = new StringBuilder();
        for (RelatorioWhatsApp r : l) {
            sb.append(sb.length() == 0 ? "" : ",").append(r.name());
        }
        return sb.toString();
    }

    public static String titulos(List<RelatorioWhatsApp> l) {
        StringBuilder sb = new StringBuilder();
        for (RelatorioWhatsApp r : l) {
            sb.append(sb.length() == 0 ? "" : " · ").append(r.titulo);
        }
        return sb.toString();
    }

    // =====================================================================
    // GERAÇÃO
    // =====================================================================
    /** O que se envia: PDF (pode faltar) e texto (legenda ou mensagem). */
    public static class Resultado {

        public final RelatorioWhatsApp relatorio;
        public final File pdf;
        public final String texto;
        /** Nome do envio (assunto do e-mail e histórico): o do relatório, ou um próprio (ex.: com o período). */
        public final String titulo;

        Resultado(RelatorioWhatsApp relatorio, File pdf, String texto) {
            this(relatorio, pdf, texto, relatorio.titulo);
        }

        Resultado(RelatorioWhatsApp relatorio, File pdf, String texto, String titulo) {
            this.relatorio = relatorio;
            this.pdf = pdf;
            this.texto = texto;
            this.titulo = titulo == null || titulo.trim().isEmpty() ? relatorio.titulo : titulo;
        }
    }

    /** Só texto, sem PDF (mensagens de teste; usado também pelo envio por e-mail). */
    public static Resultado mensagem(RelatorioWhatsApp relatorio, String texto) {
        return new Resultado(relatorio, null, texto);
    }

    /**
     * Um PDF já gerado fora daqui (ex.: o Relatório Estatístico de um intervalo escolhido
     * no diálogo, enviado na hora por e-mail ou WhatsApp), com título próprio.
     */
    public static Resultado documento(RelatorioWhatsApp tipo, File pdf, String texto, String titulo) {
        return new Resultado(tipo, pdf, texto, titulo);
    }

    private static final Locale PT = new Locale("pt", "PT");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final File PASTA = new File("anexos" + File.separator + "whatsapp");

    /** Gera o relatório agora. Sem dados: só texto a dizer isso. */
    public Resultado gerar(Connection cn) throws Exception {
        String hotel = nomeHotel(cn);
        String quando = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        LocalDate hoje = LocalDate.now();
        JasperPrint jp;
        String legenda = "*" + hotel + "* · " + titulo + " · " + quando;
        switch (this) {
            case RESUMO_DIA:
                return new Resultado(this, null, resumoDoDia(cn, hotel, quando));
            case VENDAS_ONTEM:
            case VENDAS_HOJE:
                LocalDate dia = this == VENDAS_ONTEM ? hoje.minusDays(1) : hoje;
                jp = vendas(cn, dia);
                if (jp == null) {
                    return new Resultado(this, null, legenda + "\nSem vendas em " + dia.format(DATA) + ".");
                }
                legenda += "\nTotal facturado (FR + FT): " + kz(totalVendas(cn, dia));
                break;
            case MAIS_VENDIDOS:
                return new Resultado(this, null, legenda + "\n" + maisVendidos(cn, hoje.minusDays(1), 15));
            case STOCK_BAIXO:
                return new Resultado(this, null, legenda + "\n" + stockBaixo(cn, 40));
            case PRODUTOS_EXPIRAR:
                return new Resultado(this, null, legenda + "\n" + aExpirar(cn, 30, 40));
            default:
                throw new IllegalStateException(name());
        }
        return new Resultado(this, exportar(jp), legenda);
    }

    private File exportar(JasperPrint jp) throws Exception {
        if (!PASTA.exists() && !PASTA.mkdirs()) {
            throw new IllegalStateException("não foi possível criar a pasta " + PASTA.getAbsolutePath());
        }
        limparAntigos();
        String nome = java.text.Normalizer.normalize(titulo.replaceAll("\\(.*?\\)", "").trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replaceAll("[^A-Za-z0-9]+", "_")
                + "_" + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date()) + ".pdf";
        File f = new File(PASTA, nome).getAbsoluteFile();
        JasperExportManager.exportReportToPdfFile(jp, f.getPath());
        return f;
    }

    /** Os PDFs ficam 15 dias na pasta (para o envio manual pelo WhatsApp Web) e depois apagam-se. */
    private static void limparAntigos() {
        File[] fs = PASTA.listFiles((d, n) -> n.toLowerCase().endsWith(".pdf"));
        long limite = System.currentTimeMillis() - 15L * 24 * 3600 * 1000;
        if (fs != null) {
            for (File f : fs) {
                if (f.lastModified() < limite) {
                    f.delete();
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    private static JasperPrint vendas(Connection cn, LocalDate dia) throws Exception {
        Map<String, Object> p = new HashMap<>();
        p.put("PARM_DATA_1", java.sql.Date.valueOf(dia));
        p.put("PARM_DATA_2", java.sql.Date.valueOf(dia));
        p.put("SUBREPORT_DIR", DVML.CAMINHO_REPORT);
        p.put("ARMAZEM", "Todos");
        p.put(JRParameter.REPORT_LOCALE, PT);
        File f = new File(DVML.CAMINHO_REPORT + "princ_relatorio_diario_geral.jasper").getAbsoluteFile();
        JasperPrint jp = JasperFillManager.fillReport(f.getPath(), p, cn);
        return jp.getPages().isEmpty() ? null : jp;
    }

    private static double totalVendas(Connection cn, LocalDate dia) {
        return numero(cn, "SELECT COALESCE(SUM(total_geral), 0) FROM tb_venda WHERE DATE(dataVenda) = '" + dia
                + "' AND fk_documento IN (" + DVML.DOC_FACTURA_RECIBO_FR + ", " + DVML.DOC_FACTURA_FT + ")"
                + " AND (status_eliminado IS NULL OR status_eliminado = 'false')");
    }

    /** Mensagem curta para os responsáveis (formatação do WhatsApp: *negrito*). */
    private static String resumoDoDia(Connection cn, String empresa, String quando) throws Exception {
        LocalDate hoje = LocalDate.now(), ontem = hoje.minusDays(1);
        int docsHoje = (int) numero(cn, "SELECT COUNT(*) FROM tb_venda WHERE DATE(dataVenda) = '" + hoje
                + "' AND fk_documento IN (" + DVML.DOC_FACTURA_RECIBO_FR + ", " + DVML.DOC_FACTURA_FT + ")"
                + " AND (status_eliminado IS NULL OR status_eliminado = 'false')");
        int caixasAbertas = (int) numero(cn, "SELECT COUNT(*) FROM caixa WHERE data_fecho IS NULL");
        int baixo = (int) numero(cn, "SELECT COUNT(*)" + SQL_STOCK_BAIXO);
        int expirar = (int) numero(cn, "SELECT COUNT(*) FROM lote WHERE qtd > 0 AND estado IN ('ACTIVO','EXPIRADO')"
                + " AND data_validade <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)");
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(empresa).append("*\n");
        sb.append("Resumo do dia · ").append(quando).append("\n\n");
        sb.append("💰 Vendas de ontem (").append(ontem.format(DATA)).append("): ").append(kz(totalVendas(cn, ontem))).append("\n");
        sb.append("💰 Vendas de hoje até agora: ").append(kz(totalVendas(cn, hoje))).append("\n");
        sb.append("🧾 Facturas emitidas hoje: ").append(docsHoje).append("\n");
        sb.append("🗄️ Caixas abertas: ").append(caixasAbertas).append("\n");
        if (baixo > 0) {
            sb.append("📦 Produtos com stock baixo: ").append(baixo).append("\n");
        }
        if (expirar > 0) {
            sb.append("⏳ Lotes a expirar (30 dias): ").append(expirar).append("\n");
        }
        sb.append("\n*Mais vendidos ontem*\n").append(maisVendidos(cn, ontem, 5));
        return sb.toString();
    }

    private static final String SQL_STOCK_BAIXO
            = " FROM tb_stock s JOIN tb_produto p ON p.codigo = s.cod_produto_codigo"
            + " LEFT JOIN tb_armazem a ON a.codigo = s.cod_armazem"
            + " WHERE p.stocavel = 'true' AND COALESCE(s.quant_critica, 0) > 0"
            + " AND s.quantidade_existente <= s.quant_critica";

    /** Os n produtos com mais valor facturado (FR + FT) num dia. */
    private static String maisVendidos(Connection cn, LocalDate dia, int n) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT COALESCE(p.designacao, '?'), SUM(i.quantidade), SUM(i.total) FROM tb_item_venda i"
                + " JOIN tb_venda v ON v.codigo = i.codigo_venda LEFT JOIN tb_produto p ON p.codigo = i.codigo_produto"
                + " WHERE DATE(v.dataVenda) = '" + dia + "' AND v.fk_documento IN (" + DVML.DOC_FACTURA_RECIBO_FR + ", " + DVML.DOC_FACTURA_FT + ")"
                + " AND (v.status_eliminado IS NULL OR v.status_eliminado = 'false')"
                + " GROUP BY i.codigo_produto ORDER BY 3 DESC LIMIT " + n;
        DecimalFormat q = new DecimalFormat("0.##", new DecimalFormatSymbols(PT));
        int pos = 0;
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sb.append(++pos).append(". ").append(rs.getString(1)).append(" · ").append(q.format(rs.getDouble(2)))
                        .append(" un · ").append(kz(rs.getDouble(3))).append("\n");
            }
        } catch (Exception e) {
            System.err.println("[Relatórios] " + e.getMessage());
        }
        return pos == 0 ? "Sem vendas em " + dia.format(DATA) + ".\n" : sb.toString();
    }

    /** Produtos com a quantidade igual ou abaixo da quantidade crítica, por armazém. */
    private static String stockBaixo(Connection cn, int max) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT p.designacao, COALESCE(a.designacao, ''), s.quantidade_existente, s.quant_critica" + SQL_STOCK_BAIXO
                + " ORDER BY a.designacao, s.quantidade_existente, p.designacao LIMIT " + (max + 1);
        DecimalFormat q = new DecimalFormat("0.##", new DecimalFormatSymbols(PT));
        int n = 0;
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                if (++n > max) {
                    sb.append("… e mais (ver o inventário no sistema)\n");
                    break;
                }
                sb.append("• ").append(rs.getString(1)).append(" (").append(rs.getString(2)).append("): ")
                        .append(q.format(rs.getDouble(3))).append(" / mín. ").append(q.format(rs.getDouble(4))).append("\n");
            }
        } catch (Exception e) {
            System.err.println("[Relatórios] " + e.getMessage());
        }
        return n == 0 ? "Nenhum produto abaixo do mínimo." : sb.toString();
    }

    /** Lotes com stock que expiram nos próximos {@code dias} (ou já expirados). */
    private static String aExpirar(Connection cn, int dias, int max) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT p.designacao, l.numero_lote, l.data_validade, DATEDIFF(l.data_validade, CURDATE()) FROM lote l"
                + " JOIN tb_produto p ON p.codigo = l.fk_produto WHERE l.qtd > 0 AND l.estado IN ('ACTIVO','EXPIRADO')"
                + " AND l.data_validade <= DATE_ADD(CURDATE(), INTERVAL " + dias + " DAY)"
                + " ORDER BY l.data_validade, p.designacao LIMIT " + (max + 1);
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        int n = 0;
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                if (++n > max) {
                    sb.append("… e mais (ver no sistema)\n");
                    break;
                }
                int d = rs.getInt(4);
                sb.append(d < 0 ? "❌ " : d <= 7 ? "⚠️ " : "• ").append(rs.getString(1)).append(" · lote ").append(rs.getString(2))
                        .append(" · ").append(f.format(rs.getDate(3)))
                        .append(d < 0 ? " (expirado)" : d == 0 ? " (expira hoje)" : " (" + d + " dias)").append("\n");
            }
        } catch (Exception e) {
            System.err.println("[Relatórios] " + e.getMessage());
        }
        return n == 0 ? "Nenhum lote a expirar nos próximos " + dias + " dias." : sb.toString();
    }

    private static String kz(double v) {
        return new DecimalFormat("#,##0.00", new DecimalFormatSymbols(PT)).format(v) + " Kz";
    }

    private static double numero(Connection cn, String sql) {
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (Exception e) {
            System.err.println("[WhatsApp] " + e.getMessage() + " :: " + sql);
            return 0;
        }
    }

    static String nomeHotel(Connection cn) {
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery("SELECT nome FROM tb_dados_instituicao LIMIT 1")) {
            if (rs.next() && rs.getString(1) != null && !rs.getString(1).trim().isEmpty()) {
                return rs.getString(1).trim();
            }
        } catch (Exception e) {
            // sem nome: usa o do software
        }
        return "Kitanda";
    }
}
