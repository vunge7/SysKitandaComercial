package visao.log;

import comercial.controller.DadosInstituicaoController;
import comercial.controller.LogActividadeController;
import entity.TbDadosInstituicao;
import java.awt.Cursor;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import modelo.LogActividade;
import tesouraria.novo.util.AnyReport;
import util.BDConexao;
import util.LogActividadeUtil;
import util.SessaoUtil;
import util.TemaNegocioUtil;
import visao.design.CampoData;
import visao.design.CampoPesquisa;
import visao.design.ComboModerno;
import visao.design.Selo;
import visao.design.TabelaModerna;
import visao.design.Tema;

/**
 * Consulta do registo de actividades do sistema (tabela log_actividade):
 * filtros, paginação, detalhe "antes / depois" e impressão do relatório
 * relatorio_log_actividade.jasper.
 * <p>
 * Interface construída em código com o kit {@code visao.design}: os
 * componentes são criados em {@link LogComponentes}, os eventos ligados em
 * {@link LogEventos} e a disposição responsiva feita em {@link LogLayout}.
 * <p>
 * Aberta em MenuPrincipalVisao &rarr; Sistema &rarr; Ver Logs.
 */
public class LogActividadeVisao extends JFrame {

    private static final String RELATORIO = "relatorio_log_actividade.jasper";
    private static final int TAMANHO_PAGINA = 200;
    private static final int LIMITE_AVISO_IMPRESSAO = 5000;
    private static final String TODOS = "Todos";
    private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");

    private final LogActividadeController controller;
    private final Map<String, Integer> usuarios = new HashMap<>();

    // ---- componentes (criados em LogComponentes) ----
    Selo lbTotal;
    CampoData dcInicio;
    CampoData dcFim;
    ButtonGroup grupoPeriodo;
    JToggleButton tgHoje;
    JToggleButton tg7Dias;
    JToggleButton tg30Dias;
    JToggleButton tgEsteMes;
    ComboModerno<String> cbUsuario;
    ComboModerno<String> cbModulo;
    ComboModerno<String> cbAccao;
    CampoPesquisa txtPesquisa;
    JCheckBox ckAutomaticos;
    JButton btLimpar;
    JButton btPesquisar;

    LogTabela.Modelo modelo;
    TabelaModerna tabela;

    JLabel lbDetUtilizador;
    JLabel lbDetMaquina;
    JLabel lbDetEcra;
    JLabel lbDetEntidade;
    JTextArea txtDetDescricao;
    JTextArea txtAntes;
    JTextArea txtDepois;

    JLabel lbEstado;
    JLabel lbPagina;
    JButton btPrimeira;
    JButton btAnterior;
    JButton btSeguinte;
    JButton btUltima;
    JCheckBox ckValoresRelatorio;
    JButton btImprimir;
    JButton btFechar;

    // ---- estado ----
    int pagina = 0;
    private int totalRegistos = 0;
    private LogActividadeController.Filtro filtroActual;

    public LogActividadeVisao(BDConexao conexao) {
        BDConexao ligacao = conexao != null ? conexao : BDConexao.getInstancia();
        this.controller = new LogActividadeController(ligacao);

        // Os campos do kit usam um tom suave da cor do negócio.
        Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia(ligacao)));

        initComponents();
        setLocationRelativeTo(null);

        carregarListas();
        tg7Dias.setSelected(true);
        definirPeriodo(7);
        limparDetalhe();
        pesquisar();
    }

    /**
     * Constrói a interface em código (sem .form do NetBeans).
     */
    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("...::::: KITANDA - REGISTO DE ACTIVIDADES ::::...");

        new LogComponentes(this).criar();
        new LogEventos(this).ligar();
        new LogLayout(this).montar();
    }

    private static boolean ehFarmacia(BDConexao conexao) {
        try {
            TbDadosInstituicao dados = (TbDadosInstituicao) new DadosInstituicaoController(conexao).findById(1);
            return dados != null && TemaNegocioUtil.ehFarmacia(dados.getNegocio());
        } catch (Exception e) {
            return false;
        }
    }

    // =====================================================================
    // Dados
    // =====================================================================
    private void carregarListas() {
        try {
            DefaultComboBoxModel<String> mUsuarios = new DefaultComboBoxModel<>();
            mUsuarios.addElement(TODOS);
            for (Map.Entry<Integer, String> u : controller.listarUsuarios().entrySet()) {
                String rotulo = u.getValue() + "  (" + u.getKey() + ")";
                usuarios.put(rotulo, u.getKey());
                mUsuarios.addElement(rotulo);
            }
            cbUsuario.setModel(mUsuarios);
            cbModulo.setModel(comboCom(controller.listarDistintos("modulo")));
            cbAccao.setModel(comboCom(controller.listarDistintos("accao")));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível carregar os filtros:\n" + e.getMessage(),
                    "Registo de Actividades", JOptionPane.ERROR_MESSAGE);
        }
    }

    void pesquisar() {
        LogActividadeController.Filtro filtro = lerFiltro();
        if (filtro == null) {
            return;
        }
        filtroActual = filtro;
        pagina = 0;
        carregar(true);
    }

    void irParaPagina(int novaPagina) {
        if (filtroActual == null || novaPagina < 0 || novaPagina >= totalPaginas() || novaPagina == pagina) {
            return;
        }
        pagina = novaPagina;
        carregar(false);
    }

    private void carregar(final boolean recontar) {
        final LogActividadeController.Filtro filtro = filtroActual;
        final int paginaPedida = pagina;
        ocupado(true);

        new SwingWorker<List<LogActividade>, Void>() {
            private int total = totalRegistos;

            @Override
            protected List<LogActividade> doInBackground() throws Exception {
                if (recontar) {
                    total = controller.contar(filtro);
                }
                return controller.listar(filtro, paginaPedida * TAMANHO_PAGINA, TAMANHO_PAGINA);
            }

            @Override
            protected void done() {
                ocupado(false);
                try {
                    List<LogActividade> registos = get();
                    totalRegistos = total;
                    modelo.setRegistos(registos);
                    actualizarRodape();
                    if (!registos.isEmpty()) {
                        tabela.setRowSelectionInterval(0, 0);
                        tabela.scrollRectToVisible(tabela.getCellRect(0, 0, true));
                    } else {
                        limparDetalhe();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    JOptionPane.showMessageDialog(LogActividadeVisao.this,
                            "Falha ao consultar os registos:\n" + causa.getMessage(),
                            "Registo de Actividades", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private LogActividadeController.Filtro lerFiltro() {
        if (dcInicio.isInvalida() || dcFim.isInvalida()) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/mm/aaaa.",
                    "Registo de Actividades", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        LogActividadeController.Filtro f = new LogActividadeController.Filtro();
        f.dataInicio = inicioDoDia(dcInicio.getDate());
        f.dataFim = fimDoDia(dcFim.getDate());
        if (f.dataInicio != null && f.dataFim != null && f.dataInicio.after(f.dataFim)) {
            JOptionPane.showMessageDialog(this, "A data inicial não pode ser posterior à data final.",
                    "Registo de Actividades", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Integer codUsuario = usuarios.get((String) cbUsuario.getSelectedItem());
        f.codUsuario = codUsuario != null ? codUsuario : 0;
        f.modulo = seleccionado(cbModulo);
        f.accao = seleccionado(cbAccao);
        f.texto = txtPesquisa.getText();
        f.incluirAutomaticos = ckAutomaticos.isSelected();
        return f;
    }

    void limparFiltros() {
        tg7Dias.setSelected(true);
        definirPeriodo(7);
        cbUsuario.setSelectedIndex(0);
        cbModulo.setSelectedIndex(0);
        cbAccao.setSelectedIndex(0);
        txtPesquisa.setText("");
        ckAutomaticos.setSelected(true);
        pesquisar();
    }

    void aplicarPeriodo(int dias) {
        definirPeriodo(dias);
        pesquisar();
    }

    void aplicarEsteMes() {
        Calendar cal = Calendar.getInstance();
        dcFim.setDate(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dcInicio.setDate(cal.getTime());
        pesquisar();
    }

    private void definirPeriodo(int dias) {
        Calendar cal = Calendar.getInstance();
        dcFim.setDate(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -dias);
        dcInicio.setDate(cal.getTime());
    }

    // =====================================================================
    // Impressão (relatorio_log_actividade.jasper)
    // =====================================================================
    void imprimir() {
        LogActividadeController.Filtro f = lerFiltro();
        if (f == null) {
            return;
        }
        if (totalRegistos == 0) {
            JOptionPane.showMessageDialog(this, "Não existem registos para imprimir com os filtros actuais.",
                    "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (totalRegistos > LIMITE_AVISO_IMPRESSAO && JOptionPane.showConfirmDialog(this,
                "O relatório terá " + totalRegistos + " registos e pode demorar alguns minutos.\n"
                + "Sugestão: restrinja o período ou desmarque os registos técnicos.\n\nContinuar mesmo assim?",
                "Imprimir", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        // Timestamp (e não java.util.Date): o Jasper 5 envia java.util.Date sem a hora,
        // o que excluía do relatório todos os registos do último dia
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("DATA_INICIO", new Timestamp((f.dataInicio != null ? f.dataInicio : new Date(0)).getTime()));
        parametros.put("DATA_FIM", new Timestamp((f.dataFim != null ? f.dataFim : fimDoDia(new Date())).getTime()));
        parametros.put("COD_USUARIO", f.codUsuario);
        parametros.put("MODULO", f.modulo != null ? f.modulo : "");
        parametros.put("ACCAO", f.accao != null ? f.accao : "");
        parametros.put("TEXTO", f.texto != null ? f.texto.trim() : "");
        parametros.put("INCLUIR_AUTOMATICO", f.incluirAutomaticos ? 1 : 0);
        parametros.put("MOSTRAR_VALORES", ckValoresRelatorio.isSelected() ? 1 : 0);
        parametros.put("FILTROS_TXT", resumoFiltros(f));
        parametros.put("EMITIDO_POR", SessaoUtil.getNomeUsuario() != null ? SessaoUtil.getNomeUsuario() : "");

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            new AnyReport(parametros, RELATORIO);
            LogActividadeUtil.registar(LogActividadeUtil.MODULO_RELATORIOS, LogActividadeUtil.IMPRIMIR,
                    "log_actividade", null, "Imprimiu o relatório de actividades | " + resumoFiltros(f));
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private String resumoFiltros(LogActividadeController.Filtro f) {
        StringBuilder sb = new StringBuilder();
        sb.append("Período: ")
                .append(f.dataInicio != null ? FORMATO_DATA.format(f.dataInicio) : "início")
                .append(" a ")
                .append(f.dataFim != null ? FORMATO_DATA.format(f.dataFim) : "hoje");
        sb.append("   Utilizador: ").append(cbUsuario.getSelectedItem());
        sb.append("   Módulo: ").append(f.modulo != null ? f.modulo : TODOS);
        sb.append("   Acção: ").append(f.accao != null ? f.accao : TODOS);
        if (f.texto != null && !f.texto.trim().isEmpty()) {
            sb.append("   Pesquisa: \"").append(f.texto.trim()).append("\"");
        }
        if (!f.incluirAutomaticos) {
            sb.append("   (sem registos técnicos)");
        }
        return sb.toString();
    }

    // =====================================================================
    // Apresentação
    // =====================================================================
    private void actualizarRodape() {
        int paginas = totalPaginas();
        boolean vazio = totalRegistos == 0;
        lbPagina.setText("Página " + (vazio ? 0 : pagina + 1) + " de " + (vazio ? 0 : paginas));
        int de = vazio ? 0 : pagina * TAMANHO_PAGINA + 1;
        int ate = Math.min((pagina + 1) * TAMANHO_PAGINA, totalRegistos);
        lbEstado.setText(vazio ? "Nenhum registo encontrado com estes filtros"
                : "A mostrar " + de + " – " + ate + " de " + formatarNumero(totalRegistos) + " registos");
        lbTotal.setText(formatarNumero(totalRegistos) + (totalRegistos == 1 ? " registo" : " registos"));
        btPrimeira.setEnabled(pagina > 0);
        btAnterior.setEnabled(pagina > 0);
        btSeguinte.setEnabled(pagina < paginas - 1);
        btUltima.setEnabled(pagina < paginas - 1);
        btImprimir.setEnabled(!vazio);
    }

    // 12345 -> "12 345"
    private static String formatarNumero(int numero) {
        return String.format(java.util.Locale.US, "%,d", numero).replace(',', ' ');
    }

    int totalPaginas() {
        return Math.max(1, (int) Math.ceil(totalRegistos / (double) TAMANHO_PAGINA));
    }

    private void ocupado(boolean ocupado) {
        setCursor(ocupado ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        btPesquisar.setEnabled(!ocupado);
        if (ocupado) {
            lbEstado.setText("A carregar registos...");
            btPrimeira.setEnabled(false);
            btAnterior.setEnabled(false);
            btSeguinte.setEnabled(false);
            btUltima.setEnabled(false);
        }
    }

    void mostrarDetalhe() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            limparDetalhe();
            return;
        }
        LogActividade log = modelo.getRegisto(tabela.convertRowIndexToModel(linha));
        lbDetUtilizador.setText((log.getNomeUsuario() != null ? log.getNomeUsuario() : "(sem sessão)")
                + (log.getCodUsuario() != null ? "  (cód. " + log.getCodUsuario() + ")" : ""));
        lbDetMaquina.setText(valor(log.getNomeMaquina()) + "  ·  " + valor(log.getIpMaquina()));
        lbDetEcra.setText(valor(log.getFormulario()));
        lbDetEntidade.setText(valor(log.getEntidade())
                + (log.getIdRegisto() != null ? "  ·  nº " + log.getIdRegisto() : ""));
        txtDetDescricao.setText(LogTabela.FORMATO_DATA_HORA.format(log.getDataHora()) + "\n"
                + valor(LogActividadeUtil.descricaoVisivel(log.getDescricao())));
        txtAntes.setText(formatarValores(LogActividadeUtil.valoresVisiveis(log.getValorAnterior())));
        txtDepois.setText(formatarValores(LogActividadeUtil.valoresVisiveis(log.getValorNovo())));
        txtDetDescricao.setCaretPosition(0);
        txtAntes.setCaretPosition(0);
        txtDepois.setCaretPosition(0);
    }

    private void limparDetalhe() {
        lbDetUtilizador.setText("—");
        lbDetMaquina.setText("—");
        lbDetEcra.setText("—");
        lbDetEntidade.setText("—");
        txtDetDescricao.setText("Seleccione um registo na tabela para ver os detalhes.");
        txtAntes.setText("");
        txtDepois.setText("");
    }

    // "campo=valor; campo=valor" -> uma linha por campo
    private static String formatarValores(String valores) {
        if (valores == null || valores.trim().isEmpty()) {
            return "(sem valores)";
        }
        return valores.replace("; ", "\n");
    }

    private static String valor(String texto) {
        return texto == null || texto.trim().isEmpty() ? "—" : texto;
    }

    private static DefaultComboBoxModel<String> comboCom(List<String> valores) {
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        m.addElement(TODOS);
        for (String v : valores) {
            m.addElement(v);
        }
        return m;
    }

    private static String seleccionado(ComboModerno<String> combo) {
        Object item = combo.getSelectedItem();
        return item == null || TODOS.equals(item) ? null : item.toString();
    }

    private static Date inicioDoDia(Date data) {
        if (data == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static Date fimDoDia(Date data) {
        if (data == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
