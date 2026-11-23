package comercial.caixa;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Window;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Fecho de caixa (Recepção, Restaurante, Bar, Hospedaria, POS e administradores
 * que receberam pagamentos). Substitui o CaixaFechoGoldVisao / CaixaFechoVisao:
 * mostra o que entrou por forma de pagamento (vendas FR + recibos RC), deixa
 * indicar o valor contado e mostra a diferença; o que foi lançado na conta dos
 * quartos e as facturas a crédito aparecem à parte (não são pagamento).
 * Ao fechar: grava (FechoCaixa.fechar), imprime o talão, guarda o PDF e envia-o
 * por e-mail e WhatsApp em segundo plano.
 * 2026-11-14.
 */
public final class FechoCaixaWeb extends JDialog {

    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final String[] COLUNAS = {"Forma de pagamento", "Vendas (FR)", "Recibos (RC)", "Esperado", "Contado", "Diferença"};

    private final int idUser;
    private final String area;
    private final boolean touch;
    private FechoCaixa.Resumo resumo;
    private final Map<Integer, BigDecimal> contados = new HashMap<>();
    private boolean fechou;

    private final ComponentesWeb.CartaoKpi kVendas = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Vendas pagas (FR)", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kRecibos = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.ENTRADA, "Recibos (RC)", ComponentesWeb.CIANO).compacto();
    private final ComponentesWeb.CartaoKpi kTotal = new ComponentesWeb.CartaoKpi("Kz", "Total em caixa", ComponentesWeb.VERDE).compacto();
    private final ComponentesWeb.CartaoKpi kDiferenca = new ComponentesWeb.CartaoKpi("±", "Contado / diferença", ComponentesWeb.LARANJA).compacto();
    private final ComponentesWeb.CartaoKpi kQuartos = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.CAMA, "Lançado nos quartos", ComponentesWeb.ROXO).compacto();
    private final Modelo modelo = new Modelo();
    private final JTable tabela = new JTable(modelo);
    private final JLabel lbSubtitulo = new JLabel(" ");
    private final JLabel lbNotas = new JLabel(" ");
    private JPanel cabecalho;
    private final JButton btFechar = new JButton("Fechar caixa");

    private FechoCaixaWeb(Window dono, int idUser, String area, boolean touch, FechoCaixa.Resumo r) {
        super(dono, "Fecho de caixa", Dialog.ModalityType.APPLICATION_MODAL);
        this.idUser = idUser;
        this.area = area == null ? "" : area;
        this.touch = touch;
        this.resumo = r;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        montar();
        mostrarResumo();
    }

    /**
     * Abre o fecho da caixa do utilizador.
     *
     * @param area "Recepção", "Restaurante", "Bar", "Hospedaria", "POS"...
     * @return true se a caixa foi fechada
     */
    public static boolean mostrar(Component pai, int idUser, String area, boolean touch) {
        Window dono = pai == null ? null : (pai instanceof Window ? (Window) pai : SwingUtilities.getWindowAncestor(pai));
        FechoCaixa.Resumo r;
        try (Connection cn = BDConexao.novaLigacao()) {
            r = FechoCaixa.ler(cn, idUser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dono, "Não foi possível ler a caixa: " + e.getMessage(), "Fecho de caixa", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (r == null) {
            JOptionPane.showMessageDialog(dono, "Ainda não abriu nenhuma caixa.\nNão há nada para fechar.", "Fecho de caixa",
                    JOptionPane.INFORMATION_MESSAGE, new IconeCaixa(IconeCaixa.Tipo.FECHO, 48));
            return false;
        }
        if (r.fechada()) {
            Object[] op = {"Reimprimir o último fecho", "OK"};
            int e = JOptionPane.showOptionDialog(dono, "A sua última caixa (Nº " + r.pkCaixa + ") já foi fechada em "
                    + DH.format(r.fecho) + " — Fecho Nº " + r.numero() + ".\nNão tem caixa aberta.\n\n"
                    + "Os fechos anteriores podem ser reimpressos no Menu Relatórios → Gestão de Fechos de Caixa.",
                    "Fecho de caixa", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, new IconeCaixa(IconeCaixa.Tipo.FECHOS, 48), op, op[1]);
            if (e == 0) {
                reimprimir(dono, r);
            }
            return false;
        }
        FechoCaixaWeb d = new FechoCaixaWeb(dono, idUser, area, touch, r);
        d.setVisible(true);
        return d.fechou;
    }

    /** Mostra (e deixa imprimir) o talão de um fecho já feito. */
    static void reimprimir(Component pai, FechoCaixa.Resumo r) {
        try (Connection cn = BDConexao.novaLigacao()) {
            JasperPrint jp = EnvioFechoCaixa.gerar(cn, r);
            JasperViewer v = new JasperViewer(jp, false);
            v.setTitle("Fecho de caixa Nº " + r.numero() + " · " + r.nomeUtilizador);
            v.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pai, "Não foi possível gerar o talão: " + e.getMessage(), "Fecho de caixa", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // INTERFACE
    // =====================================================================
    private void montar() {
        cabecalho = FormularioWeb.cabecalho("Fecho de caixa", " ", null);
        IconeCaixa.noCabecalho(cabecalho, IconeCaixa.Tipo.FECHO);
        // o subtítulo do cabeçalho é actualizado com os dados da caixa
        lbSubtitulo.setFont(EstiloUI.FONTE.deriveFont(13f));

        JPanel kpis = FormularioWeb.grelha(150, 4, kVendas, kRecibos, kTotal, kDiferenca);

        UiCaixa.tabela(tabela, new int[]{190, 120, 120, 130, 130, 120}, ComponentesWeb.AZUL);
        for (int i = 1; i < COLUNAS.length; i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(new UiCaixa.RendererValor(i == 5));
        }
        tabela.setRowHeight(36);
        tabela.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        javax.swing.JTextField editor = new javax.swing.JTextField();
        editor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        editor.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        tabela.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(editor));
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int l = tabela.rowAtPoint(e.getPoint()), c = tabela.columnAtPoint(e.getPoint());
                if (l >= 0 && c == 4 && modelo.isCellEditable(l, 4) && (touch || e.getClickCount() == 2)) {
                    if (tabela.isEditing()) {
                        tabela.getCellEditor().cancelCellEditing();
                    }
                    FechoCaixa.Linha linha = modelo.linha(l);
                    String v = UiCaixa.teclado(FechoCaixaWeb.this, "Contado em " + linha.nome, "");
                    if (v != null) {
                        modelo.setValueAt(v, l, 4);
                    }
                }
            }
        });
        JScrollPane sp = new JScrollPane(tabela);
        sp.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        sp.setPreferredSize(new java.awt.Dimension(800, 250));

        lbNotas.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        lbNotas.setForeground(ComponentesWeb.TEXTO_SUAVE);
        JPanel corpoTabela = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoTabela.add(sp, BorderLayout.CENTER);
        corpoTabela.add(lbNotas, BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartao = FormularioWeb.cartao("Dinheiro recebido por forma de pagamento", ComponentesWeb.AZUL_VIVO, corpoTabela);

        JPanel conteudo = ComponentesWeb.transparente(new BorderLayout(0, 10));
        conteudo.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        conteudo.add(kpis, BorderLayout.NORTH);
        conteudo.add(cartao, BorderLayout.CENTER);

        JButton actualizar = new JButton("Actualizar");
        ComponentesWeb.botao(actualizar, ComponentesWeb.Variante.SECUNDARIO, null, "Volta a ler as vendas e os recibos até agora");
        actualizar.addActionListener(e -> actualizar());
        JButton previa = new JButton("Pré-visualizar talão");
        ComponentesWeb.botao(previa, ComponentesWeb.Variante.PRIMARIO, null, "Mostra o talão sem fechar a caixa");
        previa.addActionListener(e -> previsualizar());
        ComponentesWeb.botao(btFechar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png",
                "Fecha a caixa, imprime o talão e envia-o aos responsáveis");
        btFechar.setIcon(new IconeCaixa(IconeCaixa.Tipo.FECHO, 22));
        btFechar.addActionListener(e -> fechar());
        util.ui.BotoesSair.naoESair(btFechar);
        JButton sair = new JButton("Sair");
        FormularioWeb.botaoSair(sair, "Sai sem fechar a caixa");
        sair.addActionListener(e -> dispose());
        JComponent accoes = FormularioWeb.barraAccoes(FormularioWeb.dica(touch
                ? "Toque em \"Contado\" para indicar o valor contado (opcional)."
                : "Clique duas vezes em \"Contado\" (ou escreva) para indicar o valor contado (opcional)."),
                actualizar, previa, btFechar, sair);
        FormularioWeb.montar(this, cabecalho, conteudo, accoes, "Fecho de caixa");
        FormularioWeb.ajustar(this, 1040, 690, 760, 560);
    }

    private void mostrarResumo() {
        FechoCaixa.Resumo r = resumo;
        for (FechoCaixa.Linha l : r.linhas) {
            l.contado = contados.get(l.forma);
        }
        modelo.fireTableDataChanged();
        String perfil = r.tipoUtilizador.isEmpty() ? "" : " (" + r.tipoUtilizador + ")";
        actualizarSubtitulo("Caixa Nº " + r.pkCaixa + " · " + r.nomeUtilizador + perfil + (area.isEmpty() ? "" : " · " + area)
                + " · aberta em " + DH.format(r.abertura) + " (há " + TalaoFecho.duracao(r.abertura, r.ate) + ")");
        kVendas.setValor(UiCaixa.moeda(r.totalVendas), r.numeroVendas + " documento(s)");
        kRecibos.setValor(UiCaixa.moeda(r.totalRecibos), r.numeroRecibos + " recibo(s)");
        kTotal.setValor(UiCaixa.moeda(r.totalEsperado()), "vendas + recibos");
        BigDecimal c = r.totalContado();
        if (c == null) {
            kDiferenca.setValor("—", "não indicado");
        } else {
            BigDecimal d = c.subtract(r.totalEsperado());
            kDiferenca.setValor((d.signum() > 0 ? "+" : "") + UiCaixa.moeda(d),
                    d.signum() == 0 ? "certo" : d.signum() > 0 ? "sobra" : "falta");
        }
        kQuartos.setValor(UiCaixa.moeda(r.totalQuartos), r.consumosQuartos.size() + " · fora do total");
        StringBuilder n = new StringBuilder("<html>");
        if (r.totalQuartos.signum() != 0) {
            n.append("<b>Lançado na conta dos quartos:</b> ").append(UiCaixa.moeda(r.totalQuartos))
                    .append(" Kz — não é pagamento, não entra no total.&nbsp;&nbsp; ");
        }
        if (!r.notasCredito.isEmpty()) {
            n.append("<b>Notas de crédito (NC):</b> ").append(r.notasCredito.size()).append(" · ")
                    .append(UiCaixa.moeda(r.totalNotasCredito)).append(" Kz — anulam FR/RC (fora do total) e devolvem o stock.&nbsp;&nbsp; ");
        }
        if (r.valorInicial != 0) {
            n.append("<b>Fundo de caixa:</b> ").append(UiCaixa.moeda(BigDecimal.valueOf(r.valorInicial))).append(" Kz.");
        }
        n.append("</html>");
        lbNotas.setText(n.length() > 13 ? n.toString() : " ");
        btFechar.setEnabled(!r.fechada());
    }

    private void actualizarSubtitulo(String texto) {
        // o FormularioWeb.cabecalho não guarda o subtítulo: procura a 2.ª etiqueta do bloco de títulos
        JLabel l = segundaEtiqueta(cabecalho);
        if (l != null) {
            l.setText(texto);
        }
    }

    private static JLabel segundaEtiqueta(java.awt.Container c) {
        int n = 0;
        java.util.Deque<Component> fila = new java.util.ArrayDeque<>();
        fila.add(c);
        while (!fila.isEmpty()) {
            Component x = fila.poll();
            if (x instanceof JLabel && ((JLabel) x).getIcon() == null) {
                if (++n == 2) {
                    return (JLabel) x;
                }
            }
            if (x instanceof java.awt.Container) {
                for (Component f : ((java.awt.Container) x).getComponents()) {
                    fila.add(f);
                }
            }
        }
        return null;
    }

    private void actualizar() {
        pararEdicao();
        try (Connection cn = BDConexao.novaLigacao()) {
            FechoCaixa.Resumo r = FechoCaixa.lerPorId(cn, resumo.pkCaixa);
            if (r != null) {
                resumo = r;
                if (r.fechada()) {
                    JOptionPane.showMessageDialog(this, "Esta caixa já foi fechada (Fecho Nº " + r.numero() + ").",
                            "Fecho de caixa", JOptionPane.WARNING_MESSAGE);
                }
                mostrarResumo();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao actualizar: " + e.getMessage(), "Fecho de caixa", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararEdicao() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }
    }

    private void previsualizar() {
        pararEdicao();
        actualizar();
        for (FechoCaixa.Linha l : resumo.linhas) {
            l.contado = contados.get(l.forma);
        }
        reimprimir(this, resumo);
    }

    // =====================================================================
    // FECHO
    // =====================================================================
    private void fechar() {
        pararEdicao();
        actualizar();
        if (resumo.fechada()) {
            return;
        }
        FechoCaixa.Resumo r = resumo;
        StringBuilder det = new StringBuilder();
        det.append("Caixa Nº ").append(r.pkCaixa).append(" de ").append(r.nomeUtilizador).append("\n\n");
        det.append("Vendas (FR): ").append(r.numeroVendas).append(" · ").append(UiCaixa.moeda(r.totalVendas)).append(" Kz\n");
        det.append("Recibos (RC): ").append(r.numeroRecibos).append(" · ").append(UiCaixa.moeda(r.totalRecibos)).append(" Kz\n");
        det.append("TOTAL EM CAIXA: ").append(UiCaixa.moeda(r.totalEsperado())).append(" Kz\n");
        BigDecimal c = r.totalContado();
        if (c != null) {
            BigDecimal d = c.subtract(r.totalEsperado());
            det.append("Contado: ").append(UiCaixa.moeda(c)).append(" Kz — ")
                    .append(d.signum() == 0 ? "certo" : (d.signum() > 0 ? "sobra " : "FALTA ") + UiCaixa.moeda(d.abs()) + " Kz").append("\n");
        }
        if (r.totalQuartos.signum() != 0) {
            det.append("Lançado nos quartos (não entra): ").append(UiCaixa.moeda(r.totalQuartos)).append(" Kz\n");
        }
        det.append("\nO talão é impresso e enviado aos responsáveis (e-mail / WhatsApp).");
        if (!ComponentesWeb.confirmarOperacao(this, "Fechar a caixa", det.toString())) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btFechar.setEnabled(false);
        String nome = r.nomeUtilizador;
        Map<Integer, BigDecimal> copia = new HashMap<>(contados);
        // barra de progresso (pintada numa thread própria): gravar, talão, impressão
        util.ui.ProgressoEmissao.iniciar("Fecho de caixa", "A gravar o fecho da caixa Nº " + r.pkCaixa + "...", new String[]{
            "A apurar vendas, recibos e notas de crédito.",
            "O talão vai ser impresso e enviado aos responsáveis.",
            "Por favor aguarde, não feche o sistema."});
        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                try (Connection cn = BDConexao.novaLigacao()) {
                    FechoCaixa.Resumo f = FechoCaixa.fechar(cn, idUser, nome, area, copia);
                    JasperPrint jp = null;
                    File pdf = null;
                    String aviso = null;
                    try {
                        util.ui.ProgressoEmissao.texto("Fecho Nº " + f.numero() + " gravado. A preparar o talão...");
                        jp = EnvioFechoCaixa.gerar(cn, f);
                        pdf = EnvioFechoCaixa.guardarPdf(jp, f, nome);
                    } catch (Exception e) {
                        aviso = "A caixa foi fechada, mas o talão não foi gerado: " + e.getMessage();
                    }
                    return new Object[]{f, jp, pdf, aviso};
                }
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                Object[] res;
                try {
                    res = get();
                } catch (Exception e) {
                    util.ui.ProgressoEmissao.cancelar();
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    btFechar.setEnabled(true);
                    JOptionPane.showMessageDialog(FechoCaixaWeb.this, "O fecho não foi gravado: " + causa.getMessage(),
                            "Fecho de caixa", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                FechoCaixa.Resumo f = (FechoCaixa.Resumo) res[0];
                JasperPrint jp = (JasperPrint) res[1];
                File pdf = (File) res[2];
                fechou = true;
                if (jp != null) {
                    util.ui.ProgressoEmissao.texto("A imprimir o talão do fecho Nº " + f.numero() + "...");
                    try {
                        JasperPrintManager.printReport(jp, false);
                        util.ui.ProgressoEmissao.concluirAgora();
                    } catch (Exception e) {
                        util.ui.ProgressoEmissao.cancelar();
                        JOptionPane.showMessageDialog(FechoCaixaWeb.this, "Não foi possível imprimir o talão: " + e.getMessage()
                                + "\nPode reimprimi-lo no Menu Relatórios → Gestão de Fechos de Caixa.", "Fecho de caixa", JOptionPane.WARNING_MESSAGE);
                    }
                    JasperViewer v = new JasperViewer(jp, false);
                    v.setTitle("Fecho de caixa Nº " + f.numero() + " · " + nome);
                    v.setVisible(true);
                }
                util.ui.ProgressoEmissao.concluirAgora(); // sem talão a barra ainda está aberta (sem efeito se já fechou)
                if (pdf != null) {
                    EnvioFechoCaixa.enviarEmSegundoPlano(f, nome, area, pdf, null, idUser);
                }
                String msg = "Caixa fechada com sucesso.\nFecho Nº " + f.numero() + " · total "
                        + UiCaixa.moeda(f.totalEsperado()) + " Kz."
                        + (pdf != null ? "\nO talão está a ser enviado aos responsáveis." : "")
                        + (res[3] != null ? "\n\n" + res[3] : "");
                JOptionPane.showMessageDialog(FechoCaixaWeb.this, msg, "Fecho de caixa",
                        res[3] == null ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE, new IconeCaixa(IconeCaixa.Tipo.FECHO, 48));
                dispose();
            }
        }.execute();
    }

    // =====================================================================
    // TABELA
    // =====================================================================
    private final class Modelo extends AbstractTableModel {

        FechoCaixa.Linha linha(int i) {
            return resumo.linhas.get(i);
        }

        @Override
        public int getRowCount() {
            return resumo == null ? 0 : resumo.linhas.size();
        }

        @Override
        public int getColumnCount() {
            return COLUNAS.length;
        }

        @Override
        public String getColumnName(int c) {
            return COLUNAS[c];
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return c == 0 ? String.class : Object.class;
        }

        @Override
        public boolean isCellEditable(int l, int c) {
            return c == 4 && !resumo.fechada() && linha(l).forma != FechoCaixa.FORMA_NAO_REGISTADA;
        }

        @Override
        public Object getValueAt(int l, int c) {
            FechoCaixa.Linha x = linha(l);
            switch (c) {
                case 0:
                    return x.nome;
                case 1:
                    return x.vendas;
                case 2:
                    return x.recibos;
                case 3:
                    return x.esperado();
                case 4:
                    return x.contado == null ? "" : x.contado;
                default:
                    return x.diferenca() == null ? "" : x.diferenca();
            }
        }

        @Override
        public void setValueAt(Object v, int l, int c) {
            if (c != 4) {
                return;
            }
            FechoCaixa.Linha x = linha(l);
            try {
                BigDecimal b = v instanceof BigDecimal ? (BigDecimal) v : UiCaixa.valor(String.valueOf(v));
                if (b == null) {
                    contados.remove(x.forma);
                } else {
                    contados.put(x.forma, b);
                }
            } catch (java.text.ParseException e) {
                JOptionPane.showMessageDialog(FechoCaixaWeb.this, "Valor inválido: " + v, "Fecho de caixa", JOptionPane.WARNING_MESSAGE);
                return;
            }
            mostrarResumo();
        }
    }
}
