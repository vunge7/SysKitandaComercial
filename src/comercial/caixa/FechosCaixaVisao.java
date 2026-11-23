package comercial.caixa;

import com.toedter.calendar.JDateChooser;
import comercial.email.EmailDados;
import comercial.whatsapp.WhatsAppDados;
import comercial.whatsapp.WhatsAppEnvio;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/**
 * Menu Relatórios → Gestão de Fechos de Caixa (pedido do utilizador em 2026-11-14):
 * - reimpressão do fecho de cada utilizador (filtros: utilizador, datas, nº do fecho);
 * - fecho geral por data (reimprimir, imprimir, reenviar) e o histórico dos automáticos;
 * - quem recebe os fechos: e-mails e números de WhatsApp dos responsáveis
 *   (os mesmos contactos de "Relatórios por E-mail / WhatsApp", com a opção
 *   "Recebe os fechos"), e a hora do fecho geral. Alterações só para o tipo 1.
 */
public final class FechosCaixaVisao extends JFrame {

    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat D = new SimpleDateFormat("dd/MM/yyyy");

    private final boolean administrador;

    // ---- fechos por utilizador
    private final JComboBox<Item> cbUtilizador = new JComboBox<>();
    private final JDateChooser dcDe = new JDateChooser();
    private final JDateChooser dcAte = new JDateChooser();
    private final JTextField tfNumero = new JTextField(8);
    private final JCheckBox chAbertas = new JCheckBox("Incluir abertas");
    private final DefaultTableModel mFechos = modelo("Nº fecho", "Caixa", "Utilizador", "Perfil", "Abertura", "Fecho", "Vendas FR (registado)");
    private final JTable tbFechos = new JTable(mFechos);
    private final List<Integer> pkFechos = new ArrayList<>();
    private final JLabel lbFechos = FormularioWeb.dica(" ");

    // ---- fecho geral
    private final JDateChooser dcGeral = new JDateChooser();
    private final DefaultTableModel mGeral = modelo("Data", "Período", "Modo", "Gerado em", "Origem", "Fechos", "Total", "Estado / detalhe");
    private final JTable tbGeral = new JTable(mGeral);
    private final List<Date> datasGeral = new ArrayList<>();
    private final JLabel lbHora = FormularioWeb.dica(" ");

    // ---- destinatários
    private final DefaultTableModel mEmail = modelo("Nome", "Cargo", "E-mail", "Activo", "Recebe fechos");
    private final JTable tbEmail = new JTable(mEmail);
    private final List<EmailDados.Contacto> emails = new ArrayList<>();
    private final DefaultTableModel mWhats = modelo("Nome", "Cargo", "WhatsApp", "Activo", "Recebe fechos");
    private final JTable tbWhats = new JTable(mWhats);
    private final List<WhatsAppDados.Contacto> whats = new ArrayList<>();

    public FechosCaixaVisao() {
        super("Gestão de Fechos de Caixa");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        administrador = sessaoAdministrador();
        montar();
        carregarUtilizadores();
        pesquisarFechos();
        carregarGeral();
        carregarContactos();
    }

    private static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    private static final class Item {

        final int id;
        final String texto;

        Item(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return texto;
        }
    }

    // =====================================================================
    // INTERFACE
    // =====================================================================
    private void montar() {
        JPanel cab = FormularioWeb.cabecalho("Gestão de Fechos de Caixa",
                "Reimpressão dos fechos de cada utilizador e do fecho geral · quem recebe os fechos", null);
        IconeCaixa.noCabecalho(cab, IconeCaixa.Tipo.FECHOS);
        IconeCaixa.noCabecalho(cab, IconeCaixa.Tipo.FECHOS);
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13.5f));
        abas.addTab("Fechos por utilizador", abaFechos());
        abas.addTab("Fecho geral (diário)", abaGeral());
        abas.addTab("Quem recebe os fechos", abaDestinatarios());

        JButton sair = new JButton("Sair");
        FormularioWeb.botaoSair(sair, "Fechar");
        sair.addActionListener(e -> dispose());
        JComponent accoes = FormularioWeb.barraAccoes(FormularioWeb.dica(administrador
                ? "Os talões são gerados de novo a partir da base de dados (com QR de controlo)."
                : "Só o Administrador altera os destinatários e a hora do fecho geral."), sair);
        JPanel conteudo = ComponentesWeb.transparente(new BorderLayout());
        conteudo.add(abas, BorderLayout.CENTER);
        FormularioWeb.montar(this, cab, conteudo, accoes, "Gestão de Fechos de Caixa");
        FormularioWeb.ajustar(this, 1180, 740, 860, 560);
    }

    private JPanel abaFechos() {
        Calendar k = Calendar.getInstance();
        dcAte.setDate(k.getTime());
        k.add(Calendar.DAY_OF_MONTH, -7);
        dcDe.setDate(k.getTime());
        dcDe.setDateFormatString("dd/MM/yyyy");
        dcAte.setDateFormatString("dd/MM/yyyy");
        FormularioWeb.data(dcDe);
        FormularioWeb.data(dcAte);
        FormularioWeb.combo(cbUtilizador);
        FormularioWeb.campoTexto(tfNumero);
        chAbertas.setOpaque(false);
        tfNumero.addActionListener(e -> pesquisarFechos());
        cbUtilizador.addActionListener(e -> pesquisarFechos());
        chAbertas.addActionListener(e -> pesquisarFechos());
        JButton pesquisar = new JButton("Pesquisar");
        ComponentesWeb.botao(pesquisar, ComponentesWeb.Variante.PRIMARIO, null, "Aplica os filtros");
        pesquisar.addActionListener(e -> pesquisarFechos());

        JPanel filtros = FormularioWeb.grelha(150, 6,
                ComponentesWeb.campo("Utilizador", cbUtilizador),
                ComponentesWeb.campo("Fecho de", dcDe),
                ComponentesWeb.campo("até", dcAte),
                ComponentesWeb.campo("Nº do fecho (ignora o resto)", tfNumero),
                ComponentesWeb.campo(" ", chAbertas),
                ComponentesWeb.campo(" ", pesquisar));

        UiCaixa.tabela(tbFechos, new int[]{80, 70, 210, 130, 135, 135, 140}, ComponentesWeb.AZUL);
        tbFechos.getColumnModel().getColumn(6).setCellRenderer(new UiCaixa.RendererValor(false));
        tbFechos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verFecho(false);
                }
            }
        });
        JButton ver = new JButton("Reimprimir / ver talão");
        ComponentesWeb.botao(ver, ComponentesWeb.Variante.SUCESSO, null, "Mostra o talão do fecho seleccionado");
        ver.addActionListener(e -> verFecho(false));
        ver.setIcon(new IconeCaixa(IconeCaixa.Tipo.FECHOS, 22));
        ver.setIcon(new IconeCaixa(IconeCaixa.Tipo.FECHOS, 22));
        JButton imprimir = new JButton("Imprimir directo");
        ComponentesWeb.botao(imprimir, ComponentesWeb.Variante.SECUNDARIO, null, "Envia o talão para a impressora");
        imprimir.addActionListener(e -> verFecho(true));
        JButton reenviar = new JButton("Reenviar aos responsáveis");
        ComponentesWeb.botao(reenviar, ComponentesWeb.Variante.SECUNDARIO, null, "Envia de novo o talão por e-mail e WhatsApp");
        reenviar.addActionListener(e -> reenviarFecho());
        JPanel botoes = ComponentesWeb.transparente(new BorderLayout());
        botoes.add(lbFechos, BorderLayout.WEST);
        JPanel bd = ComponentesWeb.transparente(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bd.add(ver);
        bd.add(imprimir);
        bd.add(reenviar);
        botoes.add(bd, BorderLayout.EAST);

        JPanel lista = ComponentesWeb.transparente(new BorderLayout(0, 8));
        JScrollPane spFechos = new JScrollPane(tbFechos);
        spFechos.setPreferredSize(new java.awt.Dimension(800, 300));
        lista.add(spFechos, BorderLayout.CENTER);
        lista.add(botoes, BorderLayout.SOUTH);

        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 6, 6, 6));
        p.add(FormularioWeb.cartao("Filtros", ComponentesWeb.AZUL_VIVO, filtros), BorderLayout.NORTH);
        p.add(FormularioWeb.cartao("Fechos de caixa", ComponentesWeb.VERDE, lista), BorderLayout.CENTER);
        return p;
    }

    private JPanel abaGeral() {
        dcGeral.setDate(new Date());
        dcGeral.setDateFormatString("dd/MM/yyyy");
        FormularioWeb.data(dcGeral);
        JButton ver = new JButton("Ver / reimprimir");
        ComponentesWeb.botao(ver, ComponentesWeb.Variante.SUCESSO, null, "Gera o fecho geral da data escolhida");
        ver.addActionListener(e -> verGeral(dcGeral.getDate(), false));
        ver.setIcon(new IconeCaixa(IconeCaixa.Tipo.FECHOS, 22));
        ver.setIcon(new IconeCaixa(IconeCaixa.Tipo.FECHOS, 22));
        JButton imprimir = new JButton("Imprimir directo");
        ComponentesWeb.botao(imprimir, ComponentesWeb.Variante.SECUNDARIO, null, "Imprime o fecho geral da data escolhida");
        imprimir.addActionListener(e -> verGeral(dcGeral.getDate(), true));
        JButton enviar = new JButton("Enviar aos responsáveis");
        ComponentesWeb.botao(enviar, ComponentesWeb.Variante.SECUNDARIO, null, "Envia o fecho geral da data por e-mail e WhatsApp");
        enviar.addActionListener(e -> enviarGeral(dcGeral.getDate()));

        JPanel escolha = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 10, 0));
        escolha.add(ComponentesWeb.campo("Data do fecho geral", dcGeral));
        dcGeral.setPreferredSize(new java.awt.Dimension(160, FormularioWeb.ALTURA_CAMPO));
        JPanel bts = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 18));
        bts.add(ver);
        bts.add(imprimir);
        bts.add(enviar);
        escolha.add(bts);
        JPanel topo = ComponentesWeb.transparente(new BorderLayout(0, 6));
        topo.add(escolha, BorderLayout.NORTH);
        topo.add(lbHora, BorderLayout.SOUTH);

        UiCaixa.tabela(tbGeral, new int[]{90, 230, 90, 130, 170, 60, 120, 330}, ComponentesWeb.AZUL);
        tbGeral.getColumnModel().getColumn(6).setCellRenderer(new UiCaixa.RendererValor(false));
        tbGeral.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int l = tbGeral.getSelectedRow();
                if (l >= 0) {
                    dcGeral.setDate(datasGeral.get(l));
                    if (e.getClickCount() == 2) {
                        verGeral(datasGeral.get(l), false);
                    }
                }
            }
        });

        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 6, 6, 6));
        p.add(FormularioWeb.cartao("Fecho geral de um dia", ComponentesWeb.AZUL_VIVO, topo), BorderLayout.NORTH);
        p.add(FormularioWeb.cartao("Fechos gerais já gerados", ComponentesWeb.VERDE, rolagem(tbGeral, 300)), BorderLayout.CENTER);
        return p;
    }

    private JPanel abaDestinatarios() {
        UiCaixa.tabela(tbEmail, new int[]{160, 120, 220, 60, 100}, new java.awt.Color(21, 128, 61));
        UiCaixa.tabela(tbWhats, new int[]{160, 120, 160, 60, 100}, new java.awt.Color(21, 128, 61));
        JPanel email = blocoContactos(tbEmail, true);
        JPanel whatsapp = blocoContactos(tbWhats, false);
        JPanel duas = new JPanel(new GridLayout(1, 2, 12, 0));
        duas.setOpaque(false);
        duas.add(FormularioWeb.cartao("E-mail dos responsáveis", ComponentesWeb.AZUL_VIVO, email));
        duas.add(FormularioWeb.cartao("WhatsApp dos responsáveis", ComponentesWeb.IconeWhatsApp.VERDE_WHATSAPP, whatsapp));

        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 6, 6, 6));
        JLabel info = new JLabel("<html>Quem tem <b>\"Recebe fechos\" = Sim</b> (e está activo) recebe o talão de cada fecho de caixa"
                + " no momento do fecho e o <b>fecho geral</b> diário. São os mesmos contactos dos formulários"
                + " \"Relatórios por E-mail\" e \"Relatórios por WhatsApp\" (onde se agendam os mapas);"
                + " um contacto criado aqui só recebe os fechos.</html>");
        info.setFont(EstiloUI.FONTE.deriveFont(13f));
        info.setForeground(ComponentesWeb.TEXTO);
        p.add(FormularioWeb.pilha(10, info, duas,
                FormularioWeb.cartao("Fecho geral automático", ComponentesWeb.LARANJA, painelConfig())), BorderLayout.NORTH);
        return p;
    }

    private JPanel blocoContactos(JTable t, boolean email) {
        JButton novo = new JButton("Novo");
        JButton editar = new JButton("Editar");
        JButton remover = new JButton("Remover");
        ComponentesWeb.botao(novo, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", "Novo destinatário");
        ComponentesWeb.botao(editar, ComponentesWeb.Variante.PRIMARIO, "/imagens/alterar_16x16.png", "Alterar o seleccionado");
        ComponentesWeb.botao(remover, ComponentesWeb.Variante.AVISO, "/imagens/eliminar_16x16.png", "Remover o seleccionado");
        novo.addActionListener(e -> editarContacto(email, -1));
        editar.addActionListener(e -> editarContacto(email, t.getSelectedRow()));
        remover.addActionListener(e -> removerContacto(email, t.getSelectedRow()));
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarContacto(email, t.getSelectedRow());
                }
            }
        });
        for (JButton b : new JButton[]{novo, editar, remover}) {
            b.setEnabled(administrador);
        }
        JPanel bts = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bts.add(novo);
        bts.add(editar);
        bts.add(remover);
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new java.awt.Dimension(420, 130));
        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 8));
        p.add(sp, BorderLayout.CENTER);
        p.add(bts, BorderLayout.SOUTH);
        return p;
    }

    private JPanel painelConfig() {
        JSpinner spHora = new JSpinner(new SpinnerDateModel());
        spHora.setEditor(new JSpinner.DateEditor(spHora, "HH:mm"));
        JCheckBox chActivo = new JCheckBox("Gerar automaticamente");
        JCheckBox chImprimir = new JCheckBox("Imprimir no computador que o gera");
        JCheckBox chEnviar = new JCheckBox("Enviar por e-mail e WhatsApp");
        for (JCheckBox c : new JCheckBox[]{chActivo, chImprimir, chEnviar}) {
            c.setOpaque(false);
            c.setEnabled(administrador);
        }
        spHora.setEnabled(administrador);
        FormularioWeb.spinner(spHora);
        try (Connection cn = BDConexao.novaLigacao()) {
            FechoGeral.Config c = FechoGeral.config(cn);
            Calendar k = Calendar.getInstance();
            k.set(Calendar.HOUR_OF_DAY, c.hora);
            k.set(Calendar.MINUTE, c.minuto);
            spHora.setValue(k.getTime());
            chActivo.setSelected(c.activo);
            chImprimir.setSelected(c.imprimir);
            chEnviar.setSelected(c.enviar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JButton gravar = new JButton("Gravar");
        FormularioWeb.botaoSalvar(gravar, "Grava a hora e as opções do fecho geral");
        gravar.setEnabled(administrador);
        gravar.addActionListener(e -> {
            FechoGeral.Config c = new FechoGeral.Config();
            Calendar k = Calendar.getInstance();
            k.setTime((Date) spHora.getValue());
            c.hora = k.get(Calendar.HOUR_OF_DAY);
            c.minuto = k.get(Calendar.MINUTE);
            c.activo = chActivo.isSelected();
            c.imprimir = chImprimir.isSelected();
            c.enviar = chEnviar.isSelected();
            try (Connection cn = BDConexao.novaLigacao()) {
                FechoGeral.gravarConfig(cn, c);
                JOptionPane.showMessageDialog(this, "Gravado. O fecho geral passa a abranger das " + c.horaTexto()
                        + " do dia anterior às " + c.horaTexto() + " do dia.", "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
                carregarGeral();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Falha ao gravar: " + ex.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
            }
        });
        spHora.setPreferredSize(new java.awt.Dimension(90, FormularioWeb.ALTURA_CAMPO));
        JPanel p = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.add(new JLabel("Hora do fecho geral:"));
        p.add(spHora);
        p.add(chActivo);
        p.add(chImprimir);
        p.add(chEnviar);
        p.add(gravar);
        return p;
    }

    // =====================================================================
    // FECHOS POR UTILIZADOR
    // =====================================================================
    private void carregarUtilizadores() {
        cbUtilizador.removeAllItems();
        cbUtilizador.addItem(new Item(0, "Todos os utilizadores"));
        try (Connection cn = BDConexao.novaLigacao();
                PreparedStatement ps = cn.prepareStatement("SELECT u.codigo, u.nome, t.descricao FROM tb_usuario u"
                        + " LEFT JOIN tb_tipo_usuario t ON t.idTipoUsuario = u.idTipoUsuario"
                        + " WHERE EXISTS (SELECT 1 FROM caixa c WHERE c.cod_usuario_abertura = u.codigo) ORDER BY u.nome");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String t = FechoCaixa.texto(rs.getString(3));
                cbUtilizador.addItem(new Item(rs.getInt(1), FechoCaixa.texto(rs.getString(2)) + (t.isEmpty() ? "" : " (" + t + ")")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pesquisarFechos() {
        if (cbUtilizador.getItemCount() == 0) {
            return;
        }
        mFechos.setRowCount(0);
        pkFechos.clear();
        String numero = tfNumero.getText().trim();
        StringBuilder sql = new StringBuilder("SELECT c.pk_caixa, c.numero_fecho, COALESCE(u.nome, c.usuario_abertura), t.descricao,"
                + " c.data_abertura, c.data_fecho, c.total_vendas FROM caixa c LEFT JOIN tb_usuario u ON u.codigo = c.cod_usuario_abertura"
                + " LEFT JOIN tb_tipo_usuario t ON t.idTipoUsuario = u.idTipoUsuario WHERE 1 = 1");
        List<Object> p = new ArrayList<>();
        if (!numero.isEmpty()) {
            sql.append(" AND (c.numero_fecho = ? OR c.pk_caixa = ?)");
            int n;
            try {
                n = Integer.parseInt(numero.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                n = -1;
            }
            p.add(n);
            p.add(n);
        } else {
            Item u = (Item) cbUtilizador.getSelectedItem();
            if (u != null && u.id > 0) {
                sql.append(" AND c.cod_usuario_abertura = ?");
                p.add(u.id);
            }
            Date de = inicio(dcDe.getDate()), ate = fim(dcAte.getDate());
            sql.append(" AND ((c.data_fecho BETWEEN ? AND ?)");
            p.add(new Timestamp(de.getTime()));
            p.add(new Timestamp(ate.getTime()));
            if (chAbertas.isSelected()) {
                sql.append(" OR c.data_fecho IS NULL");
            }
            sql.append(")");
        }
        sql.append(" ORDER BY COALESCE(c.data_fecho, NOW()) DESC, c.pk_caixa DESC LIMIT 1000");
        BigDecimal total = BigDecimal.ZERO;
        try (Connection cn = BDConexao.novaLigacao(); PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < p.size(); i++) {
                ps.setObject(i + 1, p.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pkFechos.add(rs.getInt(1));
                    int nf = rs.getInt(2);
                    boolean semNumero = rs.wasNull();
                    Timestamp f = rs.getTimestamp(6);
                    BigDecimal v = BigDecimal.valueOf(rs.getDouble(7)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    total = total.add(v);
                    mFechos.addRow(new Object[]{semNumero ? "—" : String.valueOf(nf), rs.getInt(1), FechoCaixa.texto(rs.getString(3)),
                        FechoCaixa.texto(rs.getString(4)), fmt(rs.getTimestamp(5)), f == null ? "ABERTA" : DH.format(f), v});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha na pesquisa: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
        }
        lbFechos.setText(mFechos.getRowCount() + " fecho(s) · vendas FR registadas: " + UiCaixa.moeda(total)
                + " Kz · o talão recalcula FR + RC a partir dos documentos");
    }

    private FechoCaixa.Resumo seleccionado() {
        int l = tbFechos.getSelectedRow();
        if (l < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione um fecho na lista.", "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        try (Connection cn = BDConexao.novaLigacao()) {
            return FechoCaixa.lerPorId(cn, pkFechos.get(tbFechos.convertRowIndexToModel(l)));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao ler o fecho: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void verFecho(boolean imprimir) {
        FechoCaixa.Resumo r = seleccionado();
        if (r == null) {
            return;
        }
        try (Connection cn = BDConexao.novaLigacao()) {
            JasperPrint jp = EnvioFechoCaixa.gerar(cn, r);
            mostrar(jp, imprimir, r.fechada() ? "Fecho de caixa Nº " + r.numero() + " · " + r.nomeUtilizador
                    : "Caixa Nº " + r.pkCaixa + " (aberta) · " + r.nomeUtilizador);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao gerar o talão: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reenviarFecho() {
        FechoCaixa.Resumo r = seleccionado();
        if (r == null) {
            return;
        }
        if (!r.fechada()) {
            JOptionPane.showMessageDialog(this, "Esta caixa ainda está aberta: só se enviam fechos feitos.", "Gestão de Fechos de Caixa",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String quem = destinatarios();
        if (quem == null) {
            return;
        }
        if (!ComponentesWeb.confirmarOperacao(this, "Reenviar fecho", "Enviar o fecho Nº " + r.numero() + " de "
                + r.nomeUtilizador + " para:\n" + quem)) {
            return;
        }
        try (Connection cn = BDConexao.novaLigacao()) {
            File pdf = EnvioFechoCaixa.guardarPdf(EnvioFechoCaixa.gerar(cn, r), r, r.nomeUtilizador);
            EnvioFechoCaixa.enviarEmSegundoPlano(r, r.nomeUtilizador, r.area, pdf, null, codigoSessao());
            JOptionPane.showMessageDialog(this, "O fecho está a ser enviado (o resultado fica no histórico de cada canal).",
                    "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // FECHO GERAL
    // =====================================================================
    private void carregarGeral() {
        mGeral.setRowCount(0);
        datasGeral.clear();
        try (Connection cn = BDConexao.novaLigacao()) {
            FechoGeral.Config c = FechoGeral.config(cn);
            lbHora.setText("Período: das " + c.horaTexto() + " do dia anterior às " + c.horaTexto() + " da data escolhida."
                    + (c.activo ? " Gerado automaticamente às " + c.horaTexto() + (c.imprimir ? ", impresso" : "")
                            + (c.enviar ? " e enviado aos responsáveis." : ".") : " (Geração automática desligada.)"));
            try (java.sql.Statement st = cn.createStatement();
                    ResultSet rs = st.executeQuery("SELECT data_referencia, inicio, fim, modo, gerado_em, origem, numero_fechos, total,"
                            + " estado, detalhe FROM tb_fecho_geral ORDER BY data_referencia DESC LIMIT 400")) {
                while (rs.next()) {
                    datasGeral.add(rs.getDate(1));
                    String det = FechoCaixa.texto(rs.getString(10));
                    mGeral.addRow(new Object[]{D.format(rs.getDate(1)), DH.format(rs.getTimestamp(2)) + " a " + DH.format(rs.getTimestamp(3)),
                        rs.getString(4), DH.format(rs.getTimestamp(5)), FechoCaixa.texto(rs.getString(6)), rs.getInt(7),
                        rs.getBigDecimal(8), rs.getString(9) + (det.isEmpty() ? "" : " · " + det)});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FechoGeral.Resultado calcularGeral(Connection cn, Date data) throws Exception {
        FechoGeral.Config c = FechoGeral.config(cn);
        Date[] p = FechoGeral.periodoDe(cn, data, c); // o período gravado, se o dia foi reagendado
        return FechoGeral.calcular(cn, p[0], p[1]);
    }

    private void verGeral(Date data, boolean imprimir) {
        if (data == null) {
            JOptionPane.showMessageDialog(this, "Escolha a data.", "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (Connection cn = BDConexao.novaLigacao()) {
            FechoGeral.Resultado g = calcularGeral(cn, data);
            mostrar(FechoGeral.gerar(cn, g), imprimir, "Fecho geral de " + D.format(data));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao gerar o fecho geral: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void enviarGeral(Date data) {
        if (data == null) {
            return;
        }
        String quem = destinatarios();
        if (quem == null) {
            return;
        }
        if (!ComponentesWeb.confirmarOperacao(this, "Enviar fecho geral", "Enviar o fecho geral de " + D.format(data) + " para:\n" + quem)) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                try (Connection cn = BDConexao.novaLigacao()) {
                    FechoGeral.Resultado g = calcularGeral(cn, data);
                    File pdf = FechoGeral.guardarPdf(FechoGeral.gerar(cn, g), g);
                    return FechoGeral.enviar(cn, g, pdf, codigoSessao());
                }
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    int n = get();
                    JOptionPane.showMessageDialog(FechosCaixaVisao.this, n + " envio(s) entregue(s). O detalhe fica no histórico"
                            + " de \"Relatórios por E-mail / WhatsApp\".", "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(FechosCaixaVisao.this, "Falha: " + e.getMessage(), "Gestão de Fechos de Caixa",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // =====================================================================
    // DESTINATÁRIOS
    // =====================================================================
    private void carregarContactos() {
        mEmail.setRowCount(0);
        mWhats.setRowCount(0);
        emails.clear();
        whats.clear();
        try (Connection cn = BDConexao.novaLigacao()) {
            for (EmailDados.Contacto c : EmailDados.contactos(cn, false)) {
                emails.add(c);
                mEmail.addRow(new Object[]{c.nome, c.cargo, c.email, c.activo ? "Sim" : "Não", c.recebeFecho ? "Sim" : "Não"});
            }
            for (WhatsAppDados.Contacto c : WhatsAppDados.contactos(cn, false)) {
                whats.add(c);
                mWhats.addRow(new Object[]{c.nome, c.cargo, WhatsAppDados.numeroLegivel(c.numero), c.activo ? "Sim" : "Não",
                    c.recebeFecho ? "Sim" : "Não"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Lista de quem vai receber (para confirmar); null se não houver ninguém. */
    private String destinatarios() {
        StringBuilder s = new StringBuilder();
        try (Connection cn = BDConexao.novaLigacao()) {
            for (EmailDados.Contacto c : EmailDados.contactosFecho(cn)) {
                s.append("  E-mail: ").append(c.nome).append(" <").append(c.email).append(">\n");
            }
            for (WhatsAppDados.Contacto c : WhatsAppDados.contactosFecho(cn)) {
                s.append("  WhatsApp: ").append(c.nome).append(" ").append(WhatsAppDados.numeroLegivel(c.numero)).append("\n");
            }
            try (PreparedStatement ps = cn.prepareStatement("SELECT email FROM tb_dados_instituicao LIMIT 1");
                    ResultSet rs = ps.executeQuery()) {
                String e = rs.next() ? EmailDados.normalizarEmail(rs.getString(1)) : null;
                if (e != null && s.indexOf("<" + e + ">") < 0) {
                    s.append("  E-mail do fecho (dados da instituição): ").append(e).append("\n");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha: " + e.getMessage(), "Gestão de Fechos de Caixa", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (s.length() == 0) {
            JOptionPane.showMessageDialog(this, "Não há ninguém a receber os fechos.\nIndique os responsáveis no separador"
                    + " \"Quem recebe os fechos\".", "Gestão de Fechos de Caixa", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return s.toString();
    }

    private void editarContacto(boolean email, int linha) {
        if (!administrador) {
            return;
        }
        boolean novo = linha < 0;
        JTextField tfNome = new JTextField(), tfCargo = new JTextField(), tfEndereco = new JTextField();
        JCheckBox chActivo = new JCheckBox("Activo", true), chFecho = new JCheckBox("Recebe os fechos de caixa e o fecho geral", true);
        if (!novo) {
            if (email) {
                EmailDados.Contacto c = emails.get(linha);
                tfNome.setText(c.nome);
                tfCargo.setText(c.cargo);
                tfEndereco.setText(c.email);
                chActivo.setSelected(c.activo);
                chFecho.setSelected(c.recebeFecho);
            } else {
                WhatsAppDados.Contacto c = whats.get(linha);
                tfNome.setText(c.nome);
                tfCargo.setText(c.cargo);
                tfEndereco.setText(WhatsAppDados.numeroLegivel(c.numero));
                chActivo.setSelected(c.activo);
                chFecho.setSelected(c.recebeFecho);
            }
        }
        FormularioWeb.campoTexto(tfNome, tfCargo, tfEndereco);
        JPanel f = new JPanel(new GridLayout(0, 1, 0, 6));
        f.add(ComponentesWeb.campo("Nome", tfNome));
        f.add(ComponentesWeb.campo("Cargo (ex.: Director, Gerente, Chefe de Loja)", tfCargo));
        f.add(ComponentesWeb.campo(email ? "E-mail" : "Número de WhatsApp (ex.: 923 456 789)", tfEndereco));
        f.add(chActivo);
        f.add(chFecho);
        f.setPreferredSize(new java.awt.Dimension(420, f.getPreferredSize().height));
        while (true) {
            if (JOptionPane.showConfirmDialog(this, f, novo ? "Novo destinatário" : "Alterar destinatário",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
                return;
            }
            if (tfNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Indique o nome.", "Destinatário", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            try (Connection cn = BDConexao.novaLigacao()) {
                if (email) {
                    String e = EmailDados.normalizarEmail(tfEndereco.getText());
                    if (e == null) {
                        JOptionPane.showMessageDialog(this, "E-mail inválido.", "Destinatário", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }
                    EmailDados.Contacto c = novo ? new EmailDados.Contacto() : emails.get(linha);
                    c.nome = tfNome.getText().trim();
                    c.cargo = tfCargo.getText().trim();
                    c.email = e;
                    c.activo = chActivo.isSelected();
                    c.recebeFecho = chFecho.isSelected();
                    EmailDados.gravarContacto(cn, c);
                } else {
                    String n = WhatsAppDados.normalizarNumero(tfEndereco.getText(), WhatsAppEnvio.daBase(cn).indicativo());
                    if (n == null) {
                        JOptionPane.showMessageDialog(this, "Número inválido.", "Destinatário", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }
                    WhatsAppDados.Contacto c = novo ? new WhatsAppDados.Contacto() : whats.get(linha);
                    c.nome = tfNome.getText().trim();
                    c.cargo = tfCargo.getText().trim();
                    c.numero = n;
                    c.activo = chActivo.isSelected();
                    c.recebeFecho = chFecho.isSelected();
                    WhatsAppDados.gravarContacto(cn, c);
                }
                carregarContactos();
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Falha ao gravar: " + ex.getMessage(), "Destinatário", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private void removerContacto(boolean email, int linha) {
        if (!administrador || linha < 0) {
            return;
        }
        String nome = email ? emails.get(linha).nome : whats.get(linha).nome;
        if (!ComponentesWeb.confirmarOperacao(this, "Remover destinatário", "Remover " + nome
                + "?\nDeixa também de receber os mapas agendados neste canal.")) {
            return;
        }
        try (Connection cn = BDConexao.novaLigacao()) {
            if (email) {
                EmailDados.removerContacto(cn, emails.get(linha).id);
            } else {
                WhatsAppDados.removerContacto(cn, whats.get(linha).id);
            }
            carregarContactos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha: " + e.getMessage(), "Destinatário", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // AUXILIARES
    // =====================================================================
    private void mostrar(JasperPrint jp, boolean imprimir, String titulo) throws Exception {
        if (imprimir) {
            JasperPrintManager.printReport(jp, true);
            return;
        }
        JasperViewer v = new JasperViewer(jp, false);
        v.setTitle(titulo);
        v.setVisible(true);
    }

    private static JScrollPane rolagem(JTable t, int altura) {
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new java.awt.Dimension(800, altura));
        return sp;
    }

    private static String fmt(Date d) {
        return d == null ? "" : DH.format(d);
    }

    private static Date inicio(Date d) {
        Calendar k = Calendar.getInstance();
        k.setTime(d == null ? new Date() : d);
        k.set(Calendar.HOUR_OF_DAY, 0);
        k.set(Calendar.MINUTE, 0);
        k.set(Calendar.SECOND, 0);
        k.set(Calendar.MILLISECOND, 0);
        return k.getTime();
    }

    private static Date fim(Date d) {
        Calendar k = Calendar.getInstance();
        k.setTime(d == null ? new Date() : d);
        k.set(Calendar.HOUR_OF_DAY, 23);
        k.set(Calendar.MINUTE, 59);
        k.set(Calendar.SECOND, 59);
        k.set(Calendar.MILLISECOND, 999);
        return k.getTime();
    }


    /** Utilizador da sessão (util.SessaoUtil do comercial); 0 se não houver. */
    static int codigoSessao() {
        Integer c = util.SessaoUtil.getCodUsuario();
        return c == null ? 0 : c;
    }

    /** O utilizador da sessão é do tipo 1 (Administrador). Sem sessão: não. */
    private static boolean sessaoAdministrador() {
        int codigo = codigoSessao();
        if (codigo <= 0) {
            return false;
        }
        try (java.sql.PreparedStatement ps = BDConexao.getInstancia().getConnectionAtiva()
                .prepareStatement("SELECT idTipoUsuario FROM tb_usuario WHERE codigo = ?")) {
            ps.setInt(1, codigo);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
