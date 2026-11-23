package comercial.mensalidade;

import comercial.controller.DadosInstituicaoController;
import entity.TbDadosInstituicao;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 * Mapas (iReport / JasperReports, A4 paisagem) do controlo de devedores:
 * <ul>
 * <li>{@code relatorios/mapaDevedores} — geral, a lista tal como está filtrada;</li>
 * <li>{@code relatorios/mapaDevedoresCliente} — mês a mês, um cliente por página.</li>
 * </ul>
 * Se faltar o .jasper (ou o .jrxml for mais recente) compila o .jrxml.
 */
public final class MapaDevedores {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Linha do mapa geral (getters para o JasperReports). */
    public static final class LinhaGeral {

        String cliente, telefone, servico, situacao, vence, desde, proximo, ultimo;
        int meses, dias;
        BigDecimal divida = BigDecimal.ZERO, multa = BigDecimal.ZERO, total = BigDecimal.ZERO;

        public String getCliente() { return cliente; }
        public String getTelefone() { return telefone; }
        public String getServico() { return servico; }
        public String getSituacao() { return situacao; }
        public String getVence() { return vence; }
        public Integer getMeses() { return meses; }
        public String getDesde() { return desde; }
        public Integer getDias() { return dias; }
        public BigDecimal getDivida() { return divida; }
        public BigDecimal getMulta() { return multa; }
        public BigDecimal getTotal() { return total; }
        public String getProximo() { return proximo; }
        public String getUltimo() { return ultimo; }
    }

    /** Linha do mapa por cliente: um mês de um contrato. */
    public static final class LinhaCliente {

        Integer clienteId;
        String cliente, telefone, nif, inscricao, ultimoPagamento, contratos;
        String servico, mes, vencimento, estado;
        Integer dias;
        BigDecimal mensalidade = BigDecimal.ZERO, multa = BigDecimal.ZERO, total = BigDecimal.ZERO;

        public Integer getClienteId() { return clienteId; }
        public String getCliente() { return cliente; }
        public String getTelefone() { return telefone; }
        public String getNif() { return nif; }
        public String getInscricao() { return inscricao; }
        public String getUltimoPagamento() { return ultimoPagamento; }
        public String getContratos() { return contratos; }
        public String getServico() { return servico; }
        public String getMes() { return mes; }
        public String getVencimento() { return vencimento; }
        public String getEstado() { return estado; }
        public Integer getDias() { return dias; }
        public BigDecimal getMensalidade() { return mensalidade; }
        public BigDecimal getMulta() { return multa; }
        public BigDecimal getTotal() { return total; }
    }

    private MapaDevedores() {
    }

    // =====================================================================
    // MAPA GERAL
    // =====================================================================
    public static void geral(BDConexao conexao, List<LinhaGeral> linhas, LocalDate ref, String filtros) {
        mostrar("mapaDevedores", parametros(conexao, ref, filtros), linhas, "Mapa de controlo de devedores");
    }

    // =====================================================================
    // MAPA POR CLIENTE
    // =====================================================================
    public static void porCliente(BDConexao conexao, List<Devedores.Linha> contratos, LocalDate ref, boolean comMulta) {
        mostrar("mapaDevedoresCliente", parametros(conexao, ref, null), linhasPorCliente(contratos, comMulta),
                "Mapa de mensalidades do cliente");
    }

    /** As linhas do mapa por cliente: os meses em atraso e o que vai vencer, de cada contrato. */
    static List<LinhaCliente> linhasPorCliente(List<Devedores.Linha> contratos, boolean comMulta) {
        // contratos de cada cliente, pela ordem em que chegaram
        Map<Integer, List<Devedores.Linha>> porCliente = new LinkedHashMap<>();
        for (Devedores.Linha l : contratos) {
            porCliente.computeIfAbsent(l.clienteId, k -> new ArrayList<>()).add(l);
        }
        List<LinhaCliente> linhas = new ArrayList<>();
        for (List<Devedores.Linha> cs : porCliente.values()) {
            Devedores.Linha p = cs.get(0);
            StringBuilder resumo = new StringBuilder();
            LocalDate ultimo = null;
            for (Devedores.Linha l : cs) {
                resumo.append(resumo.length() > 0 ? "   ·   " : "").append(l.servico).append(" (")
                        .append(Mensalidades.textoVigencia(l.contrato)).append(", vence ")
                        .append(Mensalidades.textoDiaPagamento(l.contrato.getDiaPagamento())).append(", ")
                        .append(dinheiro(l.preco)).append("/mês)");
                if (l.ultimoPagamento != null && (ultimo == null || l.ultimoPagamento.isAfter(ultimo))) {
                    ultimo = l.ultimoPagamento;
                }
            }
            String inscricao = p.estadoCliente.estado.texto()
                    + (p.inactivo() && p.estadoCliente.data != null ? " desde " + p.estadoCliente.data.toLocalDate().format(DATA) : "");
            int antes = linhas.size();
            for (Devedores.Linha l : cs) {
                for (int i = 0; i < l.emAtraso.size(); i++) {
                    BigDecimal multa = comMulta ? l.multaPorMes.get(i) : BigDecimal.ZERO;
                    linhas.add(linha(p, inscricao, ultimo, resumo, l.servico, l.emAtraso.get(i).texto(),
                            l.vencimentoPorMes.get(i).format(DATA), (int) (long) l.diasPorMes.get(i), "Em atraso",
                            l.preco, multa));
                }
                if (l.proximoPeriodo != null && (l.situacao == Devedores.Situacao.VENCE_HOJE
                        || l.situacao == Devedores.Situacao.A_VENCER)) {
                    linhas.add(linha(p, inscricao, ultimo, resumo, l.servico, l.proximoPeriodo.texto(),
                            l.proximoVencimento.format(DATA), 0,
                            l.diasParaVencer == 0 ? "Vence hoje" : "A vencer (" + l.diasParaVencer + " dias)",
                            l.preco, BigDecimal.ZERO));
                }
            }
            if (linhas.size() == antes) {
                // sem nada por pagar: a página do cliente diz que está em dia
                LinhaCliente v = linha(p, inscricao, ultimo, resumo, "—", "", "", null,
                        p.inactivo() ? "Sem dívida" : "Em dia", BigDecimal.ZERO, BigDecimal.ZERO);
                linhas.add(v);
            }
        }
        return linhas;
    }

    private static LinhaCliente linha(Devedores.Linha p, String inscricao, LocalDate ultimo, StringBuilder resumo,
            String servico, String mes, String vencimento, Integer dias, String estado, BigDecimal mensalidade, BigDecimal multa) {
        LinhaCliente c = new LinhaCliente();
        c.clienteId = p.clienteId;
        c.cliente = p.cliente;
        c.telefone = p.telefone;
        c.nif = p.nif;
        c.inscricao = inscricao;
        c.ultimoPagamento = ultimo == null ? "" : ultimo.format(DATA);
        c.contratos = resumo.toString();
        c.servico = servico;
        c.mes = mes;
        c.vencimento = vencimento;
        c.dias = dias;
        c.estado = estado;
        c.mensalidade = mensalidade;
        c.multa = multa;
        c.total = mensalidade.add(multa);
        return c;
    }

    // =====================================================================
    // COMUM
    // =====================================================================
    private static Map<String, Object> parametros(BDConexao conexao, LocalDate ref, String filtros) {
        Map<String, Object> p = new HashMap<>();
        String empresa = "", info = "";
        try {
            TbDadosInstituicao d = (TbDadosInstituicao) new DadosInstituicaoController(
                    conexao != null ? conexao : BDConexao.getInstancia()).findById(1);
            if (d != null) {
                empresa = nvl(d.getNome());
                StringBuilder s = new StringBuilder();
                juntar(s, d.getNif() == null || d.getNif().trim().isEmpty() ? null : "NIF " + d.getNif());
                juntar(s, d.getTelefone() == null || d.getTelefone().trim().isEmpty() ? null : "Tel. " + d.getTelefone());
                juntar(s, d.getEnderecos());
                juntar(s, d.getEmail());
                info = s.toString();
            }
        } catch (Exception e) {
            // sem dados da empresa: o mapa sai na mesma
        }
        p.put("EMPRESA", empresa);
        p.put("EMPRESA_INFO", info);
        p.put("DATA_REF", ref.format(DATA));
        p.put("FILTROS", filtros == null ? "" : "Filtros: " + filtros);
        p.put("IMPRESSO", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        p.put("REPORT_LOCALE", new java.util.Locale("pt", "AO"));
        return p;
    }

    private static void juntar(StringBuilder s, String t) {
        if (t != null && !t.trim().isEmpty()) {
            s.append(s.length() > 0 ? "  ·  " : "").append(t.trim());
        }
    }

    private static void mostrar(String nome, Map<String, Object> param, List<?> linhas, String titulo) {
        try {
            JasperPrint print = preencher(nome, param, linhas);
            JasperViewer v = new JasperViewer(print, false);
            v.setTitle(titulo);
            v.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            v.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível gerar o mapa:\n" + e.getMessage());
        }
    }

    static JasperPrint preencher(String nome, Map<String, Object> param, List<?> linhas) throws Exception {
        return JasperFillManager.fillReport(caminho(nome), param, new JRBeanCollectionDataSource(linhas));
    }

    /** relatorios/nome.jasper (compilado agora se faltar ou estiver mais velho que o .jrxml). */
    static String caminho(String nome) throws Exception {
        File jasper = new File("relatorios/" + nome + ".jasper").getAbsoluteFile();
        File jrxml = new File("relatorios/" + nome + ".jrxml").getAbsoluteFile();
        if (jrxml.exists() && (!jasper.exists() || jrxml.lastModified() > jasper.lastModified())) {
            try {
                JasperCompileManager.compileReportToFile(jrxml.getPath(), jasper.getPath());
            } catch (Exception e) {
                if (!jasper.exists()) {
                    throw e;
                }
                // sem compilador no posto: fica o .jasper que vem com o sistema
            }
        }
        return jasper.getPath();
    }

    private static String dinheiro(BigDecimal v) {
        return kitanda.util.CfMethods.formatarComoMoeda(v == null ? 0 : v.doubleValue());
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}
