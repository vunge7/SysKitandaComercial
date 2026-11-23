package comercial.email;

import comercial.whatsapp.RelatorioWhatsApp;

import comercial.email.EmailDados.Agendamento;
import comercial.email.EmailDados.Contacto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;

/**
 * "Relatórios por E-mail": contactos dos responsáveis do hotel, horários de
 * envio automático (um ou mais relatórios por horário) e envio manual.
 * Pedido do utilizador em 2026-11-13. Regras em EmailDados/EmailAgendador.
 */
public class EnvioRelatoriosEmailVisao extends JDialog {

    static final Color VERDE = new Color(37, 99, 235);
    static final Color VERDE_ESCURO = new Color(30, 58, 138);
    private static final Color COR_OK = new Color(21, 128, 61);
    private static final Color COR_FALHA = new Color(185, 28, 28);
    private static final Color COR_SIMULADO = new Color(29, 78, 216);
    private static final SimpleDateFormat DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final BDConexao conexao;
    private final Integer usuario;

    private final List<Contacto> contactos = new ArrayList<>();
    private final List<Agendamento> horarios = new ArrayList<>();
    private final List<Object[]> historico = new ArrayList<>();

    private final ModeloContactos modeloContactos = new ModeloContactos();
    private final ModeloHorarios modeloHorarios = new ModeloHorarios();
    private final ModeloHistorico modeloHistorico = new ModeloHistorico();
    private final JTable tbContactos = new TabelaComDica(modeloContactos);
    private final JTable tbHorarios = new TabelaComDica(modeloHorarios);
    private final JTable tbHistorico = new TabelaComDica(modeloHistorico);
    private final JLabel lbHorarios = new JLabel();
    private final JLabel lbEstado = new JLabel();
    private final JComboBox<String> cbPeriodoHistorico = new JComboBox<>(new String[]{"Hoje", "Últimos 7 dias", "Últimos 30 dias", "Últimos 90 dias"});
    private final JButton btEditarContacto = new JButton("Editar");
    private final JButton btRemoverContacto = new JButton("Remover");
    private final JButton btNovoHorario = new JButton("Novo horário");
    private final JButton btEditarHorario = new JButton("Editar");
    private final JButton btRemoverHorario = new JButton("Remover");

    public EnvioRelatoriosEmailVisao(Window dono, BDConexao conexao, Integer usuario) {
        super(dono, "Relatórios por E-mail", ModalityType.APPLICATION_MODAL);
        this.conexao = conexao;
        this.usuario = usuario;
        EmailDados.garantirTabelas(cn());
        montarLayout();
        carregarContactos(0);
        carregarHistorico();
        actualizarEstado();
        java.awt.Rectangle ecra = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setSize(Math.min(1280, ecra.width - 20), Math.min(720, ecra.height - 20));
        setMinimumSize(new Dimension(980, 560));
        setLocationRelativeTo(dono);
    }

    public static void abrir(Window dono, BDConexao conexao, Integer usuario) {
        if (!util.ui.SoAdministrador.verificar(dono, "Relatórios por E-mail")) {
            return; // só para administradores (2026-11-14)
        }
        EmailAgendador.iniciar();
        new EnvioRelatoriosEmailVisao(dono, conexao, usuario).setVisible(true);
    }

    private Connection cn() {
        return conexao.getConnectionAtiva();
    }

    // =====================================================================
    // LAYOUT
    // =====================================================================
    private void montarLayout() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel cabecalho = new ComponentesWeb.PainelGradiente(new BorderLayout(14, 2), VERDE_ESCURO, VERDE);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        JLabel titulo = new JLabel("Relatórios por E-mail");
        titulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(20f));
        titulo.setForeground(Color.WHITE);
        titulo.setIcon(new IconeEmail(26, Color.WHITE));
        titulo.setIconTextGap(10);
        JLabel subtitulo = new JLabel("<html>Registe os e-mails de quem deve receber (qualquer conta: Gmail, Outlook, empresa...) e escolha os relatórios,"
                + " as horas e os dias.<br>Serviço gratuito: cada pessoa recebe um e-mail por horário com os PDFs em anexo;"
                + " \"Enviar agora\" faz um envio manual.</html>");
        subtitulo.setFont(EstiloUI.FONTE);
        subtitulo.setForeground(new Color(219, 234, 254));
        lbEstado.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
        lbEstado.setForeground(Color.WHITE);
        JPanel textos = ComponentesWeb.transparente(new BorderLayout(0, 3));
        textos.add(titulo, BorderLayout.NORTH);
        textos.add(subtitulo, BorderLayout.CENTER);
        textos.add(lbEstado, BorderLayout.SOUTH);
        cabecalho.add(textos, BorderLayout.CENTER);
        JButton btConfig = new JButton("Configurar e-mail");
        ComponentesWeb.botao(btConfig, ComponentesWeb.Variante.SECUNDARIO, null,
                "Conta de e-mail que envia (por omissão o Gmail gratuito da DVML) e envio automático");
        btConfig.addActionListener(e -> configurar());
        JPanel direita = ComponentesWeb.transparente(new GridBagLayout());
        direita.add(btConfig);
        cabecalho.add(direita, BorderLayout.EAST);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_NEGRITO);
        abas.addTab("Contactos e horários", painelContactos());
        abas.addTab("Histórico de envios", painelHistorico());
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) {
                carregarHistorico();
            }
        });
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setBackground(ComponentesWeb.FUNDO_PAGINA);
        corpo.setBorder(BorderFactory.createEmptyBorder(10, 14, 6, 14));
        corpo.add(abas, BorderLayout.CENTER);

        JButton btEnviar = new JButton("Enviar agora…");
        ComponentesWeb.botao(btEnviar, ComponentesWeb.Variante.SUCESSO, null,
                "Envio manual: escolha os contactos e os relatórios");
        btEnviar.setIcon(new IconeEmail(18, Color.WHITE));
        btEnviar.addActionListener(e -> enviarAgora());
        JButton btFechar = new JButton("Fechar");
        ComponentesWeb.botao(btFechar, ComponentesWeb.Variante.SECUNDARIO, null, null);
        btFechar.addActionListener(e -> dispose());
        JLabel nota = new JLabel("Os PDFs enviados ficam 15 dias em " + RelatorioWhatsApp.PASTA.getAbsolutePath());
        nota.setFont(EstiloUI.FONTE.deriveFont(12f));
        nota.setForeground(ComponentesWeb.TEXTO_SUAVE);
        JPanel botoes = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.add(btEnviar);
        botoes.add(btFechar);
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(ComponentesWeb.FUNDO_PAGINA);
        rodape.setBorder(BorderFactory.createEmptyBorder(6, 14, 12, 14));
        rodape.add(nota, BorderLayout.CENTER);
        rodape.add(botoes, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cabecalho, BorderLayout.NORTH);
        getContentPane().add(corpo, BorderLayout.CENTER);
        getContentPane().add(rodape, BorderLayout.SOUTH);
    }

    private JComponent painelContactos() {
        // --- contactos
        JButton btNovo = new JButton("Novo contacto");
        ComponentesWeb.botao(btNovo, ComponentesWeb.Variante.PRIMARIO, null, "Registar um responsável da empresa");
        btNovo.addActionListener(e -> editarContacto(null));
        ComponentesWeb.botao(btEditarContacto, ComponentesWeb.Variante.SECUNDARIO, null, null);
        btEditarContacto.addActionListener(e -> editarContacto(contactoSeleccionado()));
        ComponentesWeb.botao(btRemoverContacto, ComponentesWeb.Variante.PERIGO, null, "Remove o contacto e os horários dele (o histórico fica)");
        btRemoverContacto.addActionListener(e -> removerContacto());
        prepararTabela(tbContactos, new int[]{160, 110, 140, 55, 55});
        tbContactos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarHorarios();
            }
        });
        duploClique(tbContactos, () -> editarContacto(contactoSeleccionado()));
        JPanel esquerda = bloco("Responsáveis", tbContactos, btNovo, btEditarContacto, btRemoverContacto);

        // --- horários
        ComponentesWeb.botao(btNovoHorario, ComponentesWeb.Variante.PRIMARIO, null, "Novo horário de envio automático para este contacto");
        btNovoHorario.addActionListener(e -> editarHorario(null));
        ComponentesWeb.botao(btEditarHorario, ComponentesWeb.Variante.SECUNDARIO, null, null);
        btEditarHorario.addActionListener(e -> editarHorario(horarioSeleccionado()));
        ComponentesWeb.botao(btRemoverHorario, ComponentesWeb.Variante.PERIGO, null, null);
        btRemoverHorario.addActionListener(e -> removerHorario());
        prepararTabela(tbHorarios, new int[]{55, 105, 470, 55, 125});
        tbHorarios.getSelectionModel().addListSelectionListener(e -> actualizarBotoes());
        duploClique(tbHorarios, () -> editarHorario(horarioSeleccionado()));
        JPanel direita = bloco(null, tbHorarios, btNovoHorario, btEditarHorario, btRemoverHorario);
        lbHorarios.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        lbHorarios.setForeground(ComponentesWeb.TEXTO);
        lbHorarios.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));
        direita.add(lbHorarios, BorderLayout.NORTH);

        JSplitPane divisao = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, esquerda, direita);
        divisao.setResizeWeight(0.38);
        divisao.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        divisao.setOpaque(false);
        divisao.setDividerSize(10);
        return divisao;
    }

    private JComponent painelHistorico() {
        prepararTabela(tbHistorico, new int[]{120, 150, 130, 230, 80, 85, 380});
        tbHistorico.setDefaultRenderer(Object.class, new RendererHistorico());
        cbPeriodoHistorico.setSelectedIndex(1);
        cbPeriodoHistorico.setFont(EstiloUI.FONTE);
        cbPeriodoHistorico.addActionListener(e -> carregarHistorico());
        JButton btActualizar = new JButton("Actualizar");
        ComponentesWeb.botao(btActualizar, ComponentesWeb.Variante.SECUNDARIO, null, null);
        btActualizar.addActionListener(e -> carregarHistorico());
        JPanel barra = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        JLabel l = new JLabel("Período:");
        l.setFont(EstiloUI.FONTE);
        barra.add(l);
        barra.add(cbPeriodoHistorico);
        barra.add(btActualizar);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        p.add(barra, BorderLayout.NORTH);
        JScrollPane r = new JScrollPane(tbHistorico);
        r.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        p.add(r, BorderLayout.CENTER);
        return p;
    }

    private JPanel bloco(String titulo, JTable tabela, JButton... botoes) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        if (titulo != null) {
            JLabel l = new JLabel(titulo);
            l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
            l.setForeground(ComponentesWeb.TEXTO);
            l.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));
            p.add(l, BorderLayout.NORTH);
        }
        JScrollPane r = new JScrollPane(tabela);
        r.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        r.getViewport().setBackground(Color.WHITE);
        p.add(r, BorderLayout.CENTER);
        JPanel barra = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 6, 0));
        barra.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        for (JButton b : botoes) {
            barra.add(b);
        }
        p.add(barra, BorderLayout.SOUTH);
        return p;
    }

    private static void prepararTabela(JTable t, int[] larguras) {
        EstiloUI.estilizarTabela(t, true);
        t.setRowHeight(30);
        DefaultTableCellRenderer rc = new ComponentesWeb.RendererCabecalho();
        rc.setBackground(VERDE_ESCURO);
        t.getTableHeader().setDefaultRenderer(rc);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (int i = 0; i < larguras.length && i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    private static void duploClique(JTable t, Runnable r) {
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && t.getSelectedRow() >= 0) {
                    r.run();
                }
            }
        });
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregarContactos(int seleccionar) {
        contactos.clear();
        try {
            contactos.addAll(EmailDados.contactos(cn(), false));
        } catch (SQLException e) {
            erro("ler os contactos", e);
        }
        modeloContactos.fireTableDataChanged();
        int linha = contactos.isEmpty() ? -1 : 0;
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).id == seleccionar) {
                linha = i;
            }
        }
        if (linha >= 0) {
            tbContactos.setRowSelectionInterval(linha, linha);
        }
        carregarHorarios();
    }

    private void carregarHorarios() {
        horarios.clear();
        Contacto c = contactoSeleccionado();
        if (c != null) {
            try {
                horarios.addAll(EmailDados.agendamentos(cn(), c.id));
            } catch (SQLException e) {
                erro("ler os horários", e);
            }
        }
        modeloHorarios.fireTableDataChanged();
        lbHorarios.setText(c == null ? "Horários de envio automático"
                : "Horários de envio automático de " + c.nome + "  ·  " + EmailDados.legivel(c.email));
        actualizarBotoes();
    }

    private void carregarHistorico() {
        historico.clear();
        int[] dias = {0, 7, 30, 90};
        try {
            historico.addAll(EmailDados.historico(cn(), dias[cbPeriodoHistorico.getSelectedIndex()]));
        } catch (SQLException e) {
            erro("ler o histórico", e);
        }
        modeloHistorico.fireTableDataChanged();
    }

    private void actualizarEstado() {
        EmailEnvio envio = EmailEnvio.daBase(cn());
        String problema = envio.problemaConfiguracao();
        lbEstado.setText("<html>Conta que envia: " + envio.utilizador() + (envio.usaContaPadrao() ? " (Gmail gratuito da DVML)" : "")
                + "   ·   Servidor: " + envio.servidor() + ":" + envio.porta()
                + "   ·   Envio automático: " + (envio.automaticoLigado() ? "ligado" : "DESLIGADO")
                + (problema != null ? "   ·   Atenção: " + problema : "")
                + "<br>" + EmailDados.estadoSinal(cn()) + "</html>");
    }

    private void actualizarBotoes() {
        boolean c = contactoSeleccionado() != null;
        boolean h = horarioSeleccionado() != null;
        btEditarContacto.setEnabled(c);
        btRemoverContacto.setEnabled(c);
        btNovoHorario.setEnabled(c);
        btEditarHorario.setEnabled(h);
        btRemoverHorario.setEnabled(h);
    }

    private Contacto contactoSeleccionado() {
        int r = tbContactos.getSelectedRow();
        return r < 0 || r >= contactos.size() ? null : contactos.get(r);
    }

    private Agendamento horarioSeleccionado() {
        int r = tbHorarios.getSelectedRow();
        return r < 0 || r >= horarios.size() ? null : horarios.get(r);
    }

    private void erro(String accao, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Não foi possível " + accao + ":\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // =====================================================================
    // CONTACTOS
    // =====================================================================
    private void editarContacto(Contacto original) {
        Contacto c = new Contacto();
        if (original != null) {
            c.id = original.id;
            c.nome = original.nome;
            c.cargo = original.cargo;
            c.email = original.email;
            c.activo = original.activo;
            c.recebeFecho = original.recebeFecho;
        }
        JTextField tfNome = campo(c.nome);
        JTextField tfCargo = campo(c.cargo);
        JTextField tfNumero = campo(c.email.isEmpty() ? "" : EmailDados.legivel(c.email));
        JCheckBox chActivo = new JCheckBox("Activo (recebe os envios)", c.activo);
        chActivo.setOpaque(false);
        JCheckBox chFecho = new JCheckBox("Recebe os fechos de caixa (no momento do fecho) e o fecho geral diário", c.recebeFecho);
        chFecho.setOpaque(false);
        JPanel f = formulario(new Object[][]{
            {"Nome *", tfNome},
            {"Cargo", tfCargo},
            {"E-mail *", tfNumero},
            {"", nota("Qualquer conta: ex.: gerente@empresa.co.ao, nome@gmail.com, nome@outlook.com")},
            {"", chActivo},
            {"", chFecho}
        });
        while (true) {
            if (JOptionPane.showConfirmDialog(this, f, original == null ? "Novo contacto" : "Editar contacto",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
                return;
            }
            String numero = EmailDados.normalizarEmail(tfNumero.getText());
            if (tfNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Indique o nome.", "Contacto", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            if (numero == null) {
                JOptionPane.showMessageDialog(this, "O endereço de e-mail não é válido.", "Contacto", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            c.nome = tfNome.getText().trim();
            c.cargo = tfCargo.getText().trim();
            c.email = numero;
            c.activo = chActivo.isSelected();
            c.recebeFecho = chFecho.isSelected();
            try {
                EmailDados.gravarContacto(cn(), c);
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(this, "Já existe um contacto com o e-mail " + EmailDados.legivel(numero) + ".",
                            "Contacto", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                erro("gravar o contacto", e);
                return;
            }
            carregarContactos(c.id);
            if (original == null && JOptionPane.showConfirmDialog(this, "Quer já marcar um horário de envio para " + c.nome + "?",
                    "Novo contacto", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                editarHorario(null);
            }
            return;
        }
    }

    private void removerContacto() {
        Contacto c = contactoSeleccionado();
        if (c == null || JOptionPane.showConfirmDialog(this, "Remover " + c.nome + " e todos os horários dele?\n"
                + "(O histórico de envios fica.)", "Remover contacto", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            EmailDados.removerContacto(cn(), c.id);
        } catch (SQLException e) {
            erro("remover o contacto", e);
        }
        carregarContactos(0);
    }

    // =====================================================================
    // HORÁRIOS
    // =====================================================================
    private void editarHorario(Agendamento original) {
        Contacto c = contactoSeleccionado();
        if (c == null) {
            return;
        }
        Agendamento a = new Agendamento();
        a.contacto = c.id;
        if (original != null) {
            a.id = original.id;
            a.hora = original.hora;
            a.dias = original.dias;
            a.relatorios = new ArrayList<>(original.relatorios);
            a.activo = original.activo;
        } else {
            a.relatorios.add(RelatorioWhatsApp.RESUMO_DIA);
        }
        JSpinner spHora = spinnerHora(a.hora);
        JCheckBox[] chDias = new JCheckBox[7];
        JPanel pDias = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 4, 0));
        for (int i = 0; i < 7; i++) {
            chDias[i] = new JCheckBox(EmailDados.DIAS_CURTOS[i], a.dias.charAt(i) == '1');
            chDias[i].setOpaque(false);
            chDias[i].setFont(EstiloUI.FONTE);
            pDias.add(chDias[i]);
        }
        JButton btTodos = atalhoDias("Todos", chDias, "1111111");
        JButton btUteis = atalhoDias("Seg a Sex", chDias, "1111100");
        pDias.add(btTodos);
        pDias.add(btUteis);
        Map<RelatorioWhatsApp, JCheckBox> chRel = new EnumMap<>(RelatorioWhatsApp.class);
        JPanel pRel = escolhaRelatorios(chRel, a.relatorios);
        JCheckBox chActivo = new JCheckBox("Activo", a.activo);
        chActivo.setOpaque(false);
        JPanel f = formulario(new Object[][]{
            {"Para", new JLabel(c.nome + "  ·  " + EmailDados.legivel(c.email))},
            {"Hora *", spHora},
            {"Dias *", pDias},
            {"Relatórios *", pRel},
            {"", chActivo}
        });
        while (true) {
            if (JOptionPane.showConfirmDialog(this, f, original == null ? "Novo horário de envio" : "Editar horário",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
                return;
            }
            StringBuilder dias = new StringBuilder();
            for (JCheckBox ch : chDias) {
                dias.append(ch.isSelected() ? '1' : '0');
            }
            List<RelatorioWhatsApp> rels = escolhidos(chRel);
            if (dias.indexOf("1") < 0 || rels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escolha pelo menos um dia e um relatório.", "Horário", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            a.hora = new SimpleDateFormat("HH:mm").format((Date) spHora.getValue());
            a.dias = dias.toString();
            a.relatorios = rels;
            a.activo = chActivo.isSelected();
            try {
                EmailDados.gravarAgendamento(cn(), a);
            } catch (SQLException e) {
                erro("gravar o horário", e);
                return;
            }
            carregarHorarios();
            return;
        }
    }

    private void removerHorario() {
        Agendamento a = horarioSeleccionado();
        if (a == null || JOptionPane.showConfirmDialog(this, "Remover o envio das " + a.hora + "?", "Remover horário",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            EmailDados.removerAgendamento(cn(), a.id);
        } catch (SQLException e) {
            erro("remover o horário", e);
        }
        carregarHorarios();
    }

    private static JButton atalhoDias(String texto, JCheckBox[] dias, String mascara) {
        JButton b = new JButton(texto);
        ComponentesWeb.botao(b, ComponentesWeb.Variante.SECUNDARIO, null, null);
        b.setMargin(new Insets(2, 8, 2, 8));
        b.addActionListener(e -> {
            for (int i = 0; i < 7; i++) {
                dias[i].setSelected(mascara.charAt(i) == '1');
            }
        });
        return b;
    }

    private static JSpinner spinnerHora(String hora) {
        Calendar cal = Calendar.getInstance();
        String[] hm = hora.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hm[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(hm[1]));
        JSpinner sp = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE));
        sp.setEditor(new JSpinner.DateEditor(sp, "HH:mm"));
        sp.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
        sp.setPreferredSize(new Dimension(90, 32));
        return sp;
    }

    /** Uma caixa por relatório, com a descrição por baixo. */
    private static JPanel escolhaRelatorios(Map<RelatorioWhatsApp, JCheckBox> caixas, List<RelatorioWhatsApp> marcados) {
        JPanel p = ComponentesWeb.transparente(null);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (RelatorioWhatsApp r : RelatorioWhatsApp.values()) {
            JCheckBox ch = new JCheckBox("<html><b>" + r.titulo + "</b><br><span style='color:#64748b'>" + r.descricao + "</span></html>",
                    marcados.contains(r));
            ch.setOpaque(false);
            ch.setFont(EstiloUI.FONTE);
            ch.setAlignmentX(Component.LEFT_ALIGNMENT);
            caixas.put(r, ch);
            p.add(ch);
            p.add(Box.createVerticalStrut(2));
        }
        return p;
    }

    private static List<RelatorioWhatsApp> escolhidos(Map<RelatorioWhatsApp, JCheckBox> caixas) {
        List<RelatorioWhatsApp> l = new ArrayList<>();
        for (Map.Entry<RelatorioWhatsApp, JCheckBox> e : caixas.entrySet()) {
            if (e.getValue().isSelected()) {
                l.add(e.getKey());
            }
        }
        return l;
    }

    // =====================================================================
    // ENVIO MANUAL
    // =====================================================================
    private void enviarAgora() {
        List<Contacto> activos = new ArrayList<>();
        for (Contacto c : contactos) {
            if (c.activo) {
                activos.add(c);
            }
        }
        if (activos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registe primeiro um contacto activo.", "Enviar agora", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Contacto foco = contactoSeleccionado();
        Map<Contacto, JCheckBox> chContactos = new LinkedHashMap<>();
        JPanel pContactos = ComponentesWeb.transparente(null);
        pContactos.setLayout(new BoxLayout(pContactos, BoxLayout.Y_AXIS));
        for (Contacto c : activos) {
            JCheckBox ch = new JCheckBox(c.nome + (c.cargo.isEmpty() ? "" : " · " + c.cargo) + "  (" + EmailDados.legivel(c.email) + ")",
                    foco == null || foco.id == c.id);
            ch.setOpaque(false);
            ch.setFont(EstiloUI.FONTE);
            chContactos.put(c, ch);
            pContactos.add(ch);
        }
        Agendamento h = horarioSeleccionado();
        List<RelatorioWhatsApp> marcados = h != null ? h.relatorios : new ArrayList<>();
        if (marcados.isEmpty()) {
            marcados.add(RelatorioWhatsApp.RESUMO_DIA);
        }
        Map<RelatorioWhatsApp, JCheckBox> chRel = new EnumMap<>(RelatorioWhatsApp.class);
        JPanel pRel = escolhaRelatorios(chRel, marcados);
        JScrollPane rc = new JScrollPane(pContactos);
        rc.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rc.setPreferredSize(new Dimension(520, Math.min(160, 26 * activos.size() + 8)));
        JPanel f = formulario(new Object[][]{
            {"Para", rc},
            {"Relatórios", pRel}
        });
        Object[] opcoes = {"Enviar", "Cancelar"};
        int op = JOptionPane.showOptionDialog(this, f, "Enviar agora", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opcoes, opcoes[0]);
        if (op != 0) {
            return;
        }
        List<Contacto> para = new ArrayList<>();
        for (Map.Entry<Contacto, JCheckBox> e : chContactos.entrySet()) {
            if (e.getValue().isSelected()) {
                para.add(e.getKey());
            }
        }
        List<RelatorioWhatsApp> rels = escolhidos(chRel);
        if (para.isEmpty() || rels.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escolha pelo menos um contacto e um relatório.", "Enviar agora", JOptionPane.WARNING_MESSAGE);
            return;
        }
        EmailEnvio envio = EmailEnvio.daBase(cn());
        String problema = envio.problemaConfiguracao();
        if (problema != null) {
            JOptionPane.showMessageDialog(this, problema + "\nUse \"Configurar e-mail\".", "Enviar agora", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new ProgressoEnvio(para, rels, envio).iniciar();
    }

    /** Janela com o progresso do envio pela API (corre fora da interface). */
    private class ProgressoEnvio extends JDialog {

        private final List<Contacto> para;
        private final List<RelatorioWhatsApp> rels;
        private final EmailEnvio envio;
        private final JTextArea area = new JTextArea(14, 60);
        private final JButton btFechar = new JButton("A enviar…");

        ProgressoEnvio(List<Contacto> para, List<RelatorioWhatsApp> rels, EmailEnvio envio) {
            super(EnvioRelatoriosEmailVisao.this, "A enviar por e-mail", true);
            this.para = para;
            this.rels = rels;
            this.envio = envio;
            area.setEditable(false);
            area.setFont(EstiloUI.FONTE);
            area.setMargin(new Insets(8, 10, 8, 10));
            ComponentesWeb.botao(btFechar, ComponentesWeb.Variante.SECUNDARIO, null, null);
            btFechar.setEnabled(false);
            btFechar.addActionListener(e -> dispose());
            JPanel sul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            sul.add(btFechar);
            getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
            getContentPane().add(sul, BorderLayout.SOUTH);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            pack();
            setLocationRelativeTo(EnvioRelatoriosEmailVisao.this);
        }

        void iniciar() {
            new SwingWorker<Integer, String>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    try (Connection cn = BDConexao.novaLigacao()) {
                        return EmailAgendador.enviar(cn, envio, para, rels, EmailDados.MODO_MANUAL, usuario, null, this::publish);
                    }
                }

                @Override
                protected void process(List<String> linhas) {
                    for (String l : linhas) {
                        area.append(l + "\n");
                    }
                }

                @Override
                protected void done() {
                    int total = para.size(); // um e-mail por contacto, com todos os relatórios
                    try {
                        int ok = get();
                        area.append("\n" + (ok == total ? "Concluído: " : "Terminado com falhas: ") + ok + " de " + total + " e-mail(s)"
                                + " com " + rels.size() + " relatório(s) cada.");
                    } catch (Exception e) {
                        area.append("\nFalhou: " + e.getMessage());
                    }
                    btFechar.setText("Fechar");
                    btFechar.setEnabled(true);
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    carregarHistorico();
                }
            }.execute();
            setVisible(true);
        }
    }

    // =====================================================================
    // CONFIGURAÇÃO
    // =====================================================================
    private void configurar() {
        EmailEnvio actual = EmailEnvio.daBase(cn());
        JTextField tfConta = campo(actual.utilizador());
        JPasswordField tfSenha = new JPasswordField(actual.get(EmailEnvio.C_SENHA, ""), 24);
        JTextField tfNome = campo(actual.get(EmailEnvio.C_NOME_REMETENTE, ""));
        JTextField tfServidor = campo(actual.servidor());
        JTextField tfPorta = campo(String.valueOf(actual.porta()));
        JComboBox<String> cbSeguranca = new JComboBox<>(new String[]{"SSL (porta 465)", "STARTTLS (porta 587)"});
        cbSeguranca.setSelectedIndex(EmailEnvio.STARTTLS.equals(actual.seguranca()) ? 1 : 0);
        cbSeguranca.addActionListener(e -> tfPorta.setText(cbSeguranca.getSelectedIndex() == 1 ? "587" : "465"));
        JCheckBox chAuto = new JCheckBox("Envio automático ligado (nas horas agendadas)", actual.automaticoLigado());
        chAuto.setOpaque(false);
        JTextField tfTolerancia = campo(String.valueOf(actual.toleranciaMin()));
        JButton btPadrao = new JButton("Usar o Gmail gratuito da DVML");
        ComponentesWeb.botao(btPadrao, ComponentesWeb.Variante.SECUNDARIO, null, "Volta à conta " + EmailEnvio.CONTA_PADRAO);
        btPadrao.addActionListener(e -> {
            tfConta.setText(EmailEnvio.CONTA_PADRAO);
            tfSenha.setText("");
            tfServidor.setText(EmailEnvio.SERVIDOR_PADRAO);
            cbSeguranca.setSelectedIndex(0);
            tfPorta.setText(EmailEnvio.PORTA_PADRAO);
        });
        JButton btTeste = new JButton("Enviar e-mail de teste…");
        ComponentesWeb.botao(btTeste, ComponentesWeb.Variante.SECUNDARIO, null, "Envia um e-mail curto com os dados acima (sem gravar)");

        JPanel tudo = formulario(new Object[][]{
            {"Conta que envia *", tfConta},
            {"Palavra-passe", tfSenha},
            {"", nota("<html>Gmail: use uma \"palavra-passe de aplicação\" (Conta Google > Segurança > Verificação em 2 passos ><br>"
                    + "Palavras-passe de aplicações). Vazio com a conta da DVML = usa a da DVML.</html>")},
            {"Nome do remetente", tfNome},
            {"", nota("Vazio = o nome da empresa (aparece como remetente no e-mail)")},
            {"Servidor SMTP", tfServidor},
            {"Segurança", cbSeguranca},
            {"Porta", tfPorta},
            {"", nota("<html>Gmail: smtp.gmail.com, SSL 465 · Outlook/Hotmail: smtp-mail.outlook.com, STARTTLS 587<br>"
                    + "Conta da empresa: peça os dados SMTP ao fornecedor do e-mail</html>")},
            {"", chAuto},
            {"Tolerância (min)", tfTolerancia},
            {"", nota("Em caso de falha (sem internet), o sistema tenta de novo de 5 em 5 min durante este tempo")},
            {"", btPadrao},
            {"", btTeste}
        });

        java.util.function.Supplier<Map<String, String>> valores = () -> {
            Map<String, String> m = new LinkedHashMap<>();
            m.put(EmailEnvio.C_UTILIZADOR, tfConta.getText().trim().toLowerCase(java.util.Locale.ROOT));
            m.put(EmailEnvio.C_SENHA, new String(tfSenha.getPassword()).trim());
            m.put(EmailEnvio.C_NOME_REMETENTE, tfNome.getText().trim());
            m.put(EmailEnvio.C_SERVIDOR, tfServidor.getText().trim());
            m.put(EmailEnvio.C_PORTA, tfPorta.getText().replaceAll("[^0-9]", ""));
            m.put(EmailEnvio.C_SEGURANCA, cbSeguranca.getSelectedIndex() == 1 ? EmailEnvio.STARTTLS : EmailEnvio.SSL);
            m.put(EmailEnvio.C_AUTOMATICO, chAuto.isSelected() ? "1" : "0");
            m.put(EmailEnvio.C_TOLERANCIA, tfTolerancia.getText().replaceAll("[^0-9]", ""));
            return m;
        };
        btTeste.addActionListener(e -> testar(new EmailEnvio(valores.get()), tudo));

        if (JOptionPane.showConfirmDialog(this, tudo, "Configurar e-mail", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        if (EmailDados.normalizarEmail(tfConta.getText()) == null) {
            JOptionPane.showMessageDialog(this, "A conta que envia não é um e-mail válido. Nada foi gravado.", "Configurar e-mail",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            EmailDados.gravarConfig(cn(), valores.get());
        } catch (SQLException e) {
            erro("gravar a configuração", e);
        }
        actualizarEstado();
    }

    private void testar(EmailEnvio envio, Component pai) {
        String problema = envio.problemaConfiguracao();
        if (problema != null) {
            JOptionPane.showMessageDialog(pai, problema, "Teste", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Contacto c = contactoSeleccionado();
        String sugestao = c == null ? "" : c.email;
        String n = (String) JOptionPane.showInputDialog(pai, "E-mail para o teste:", "Teste", JOptionPane.PLAIN_MESSAGE, null, null, sugestao);
        String para = EmailDados.normalizarEmail(n);
        if (para == null) {
            if (n != null) {
                JOptionPane.showMessageDialog(pai, "O endereço de e-mail não é válido.", "Teste", JOptionPane.WARNING_MESSAGE);
            }
            return;
        }
        RelatorioWhatsApp.Resultado r = RelatorioWhatsApp.mensagem(RelatorioWhatsApp.RESUMO_DIA,
                "Teste do Kitanda · " + DATA_HORA.format(new Date())
                + "\nSe recebeu este e-mail, o envio de relatórios por e-mail está a funcionar.");
        Component janela = SwingUtilities.getWindowAncestor(pai);
        janela.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        try {
            List<RelatorioWhatsApp.Resultado> um = new ArrayList<>();
            um.add(r);
            String detalhe = envio.enviar(cn(), para, um);
            EmailDados.registarEnvio(cn(), null, "Teste", para, "Mensagem de teste", EmailDados.MODO_MANUAL,
                    EmailDados.ESTADO_ENVIADO, detalhe, usuario);
            JOptionPane.showMessageDialog(pai, "E-mail de teste enviado de " + envio.utilizador() + " para " + para
                    + ".\nConfirme na caixa de entrada (e no Spam, na primeira vez).", "Teste", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            EmailDados.registarEnvio(cn(), null, "Teste", para, "Mensagem de teste", EmailDados.MODO_MANUAL,
                    EmailDados.ESTADO_FALHOU, e.getMessage(), usuario);
            JOptionPane.showMessageDialog(pai, "O teste falhou:\n" + e.getMessage()
                    + "\n\nGmail: confirme a palavra-passe de aplicação; outras contas: servidor, porta e segurança.",
                    "Teste", JOptionPane.ERROR_MESSAGE);
        } finally {
            janela.setCursor(null);
        }
    }

    // =====================================================================
    // AUXILIARES DE FORMULÁRIO
    // =====================================================================
    private static JTextField campo(String valor) {
        JTextField t = new JTextField(valor, 24);
        t.setFont(EstiloUI.FONTE);
        return t;
    }

    private static JLabel nota(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(EstiloUI.FONTE.deriveFont(11.5f));
        l.setForeground(ComponentesWeb.TEXTO_SUAVE);
        return l;
    }

    /** Linhas {rótulo, componente}. */
    private static JPanel formulario(Object[][] linhas) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 4, 3, 4);
        for (int i = 0; i < linhas.length; i++) {
            g.gridy = i;
            g.gridx = 0;
            g.weightx = 0;
            g.anchor = GridBagConstraints.NORTHEAST;
            g.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel((String) linhas[i][0]);
            l.setFont(EstiloUI.FONTE_NEGRITO);
            l.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            p.add(l, g);
            g.gridx = 1;
            g.weightx = 1;
            g.anchor = GridBagConstraints.WEST;
            g.fill = linhas[i][1] instanceof JTextField || linhas[i][1] instanceof JScrollPane
                    ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
            p.add((Component) linhas[i][1], g);
        }
        return p;
    }

    // =====================================================================
    // MODELOS DAS TABELAS
    // =====================================================================
    private class ModeloContactos extends AbstractTableModel {

        private final String[] colunas = {"Nome", "Cargo", "E-mail", "Activo", "Fechos"};

        @Override
        public int getRowCount() {
            return contactos.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int c) {
            return colunas[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            Contacto x = contactos.get(r);
            switch (c) {
                case 0:
                    return x.nome;
                case 1:
                    return x.cargo;
                case 2:
                    return EmailDados.legivel(x.email);
                case 3:
                    return x.activo ? "Sim" : "Não";
                default:
                    return x.recebeFecho ? "Sim" : "Não";
            }
        }
    }

    private class ModeloHorarios extends AbstractTableModel {

        private final String[] colunas = {"Hora", "Dias", "Relatórios", "Activo", "Último envio"};

        @Override
        public int getRowCount() {
            return horarios.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int c) {
            return colunas[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            Agendamento a = horarios.get(r);
            switch (c) {
                case 0:
                    return a.hora;
                case 1:
                    return a.textoDias();
                case 2:
                    return RelatorioWhatsApp.titulos(a.relatorios);
                case 3:
                    return a.activo ? "Sim" : "Não";
                default:
                    Timestamp t = a.ultimoEnvio;
                    return t == null ? "—" : DATA_HORA.format(t);
            }
        }
    }

    private class ModeloHistorico extends AbstractTableModel {

        private final String[] colunas = {"Data", "Para", "E-mail", "Relatório", "Modo", "Estado", "Detalhe"};

        @Override
        public int getRowCount() {
            return historico.size();
        }

        @Override
        public int getColumnCount() {
            return colunas.length;
        }

        @Override
        public String getColumnName(int c) {
            return colunas[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            Object v = historico.get(r)[c];
            if (c == 0 && v instanceof Date) {
                return DATA_HORA.format((Date) v);
            }
            if (c == 4) {
                return EmailDados.MODO_AUTOMATICO.equals(v) ? "Automático" : "Manual";
            }
            if (c == 5) {
                return EmailDados.ESTADO_ENVIADO.equals(v) ? "Enviado"
                        : EmailDados.ESTADO_SIMULADO.equals(v) ? "Simulado" : "Falhou";
            }
            return v;
        }
    }

    /** Mostra o texto completo da célula ao passar o rato (relatórios e detalhes longos). */
    private static class TabelaComDica extends JTable {

        TabelaComDica(javax.swing.table.TableModel m) {
            super(m);
        }

        @Override
        public String getToolTipText(java.awt.event.MouseEvent e) {
            int r = rowAtPoint(e.getPoint()), c = columnAtPoint(e.getPoint());
            Object v = r < 0 || c < 0 ? null : getValueAt(r, c);
            return v == null || v.toString().isEmpty() ? null : v.toString();
        }
    }

    private static class RendererHistorico extends EstiloUI.ZebraRenderer {

        RendererHistorico() {
            super(javax.swing.SwingConstants.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int r, int c) {
            Component comp = super.getTableCellRendererComponent(t, v, sel, foco, r, c);
            if (!sel && c == 5) {
                comp.setForeground("Enviado".equals(v) ? COR_OK : "Simulado".equals(v) ? COR_SIMULADO : COR_FALHA);
                comp.setFont(EstiloUI.FONTE_NEGRITO);
            } else if (!sel) {
                comp.setForeground(ComponentesWeb.TEXTO);
            }
            if (c == 6 && comp instanceof JLabel) {
                ((JLabel) comp).setToolTipText(v == null ? null : v.toString());
            }
            return comp;
        }
    }
}
