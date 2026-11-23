package rh.web;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;

/** Peças comuns dos ecrãs do RH novo (mesmo aspecto dos outros ecrãs Kitanda). */
public final class UiRH {

    public static final Locale PT = new Locale("pt", "PT");
    public static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final Color COR_RH = new Color(124, 58, 237);

    private UiRH() {
    }

    // ---------------------------------------------------------------- janela
    /** Monta a janela: cabeçalho, conteúdo e barra de acções; tamanho grande, redimensionável. */
    public static void janela(JFrame f, String titulo, String subtitulo, JComponent direitaCabecalho,
            JComponent conteudo, JLabel dica, AbstractButton... accoes) {
        f.setTitle(titulo);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FormularioWeb.montar(f, FormularioWeb.cabecalho(titulo, subtitulo, direitaCabecalho), conteudo,
                FormularioWeb.barraAccoes(dica, accoes), "Recursos Humanos  ·  " + titulo);
        FormularioWeb.ajustar(f, 1280, 780, 900, 560);
    }

    public static JButton botao(String texto, ComponentesWeb.Variante v, String dica, Runnable accao) {
        JButton b = new JButton(texto);
        ComponentesWeb.botao(b, v, null, dica);
        if (accao != null) {
            b.addActionListener(e -> executar(b, accao));
        }
        return b;
    }

    public static JButton sair(JFrame f) {
        JButton b = new JButton("Sair");
        FormularioWeb.botaoSair(b, "Fechar esta janela");
        b.addActionListener(e -> f.dispose());
        return b;
    }

    /** Corre uma acção mostrando o erro (sem rebentar a janela). */
    public static void executar(Component pai, Runnable accao) {
        try {
            accao.run();
        } catch (RuntimeException e) {
            erro(pai, e);
        }
    }

    public static void erro(Component pai, Throwable e) {
        e.printStackTrace();
        Throwable c = e.getCause() != null ? e.getCause() : e;
        JOptionPane.showMessageDialog(pai, "Não foi possível concluir:\n" + c.getMessage(), "Recursos Humanos", JOptionPane.ERROR_MESSAGE);
    }

    public static void aviso(Component pai, String texto) {
        JOptionPane.showMessageDialog(pai, texto, "Recursos Humanos", JOptionPane.WARNING_MESSAGE);
    }

    public static void info(Component pai, String texto) {
        JOptionPane.showMessageDialog(pai, texto, "Recursos Humanos", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Erros de base de dados dentro de lambdas. */
    public static RuntimeException falha(Exception e) {
        return e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e.getMessage(), e);
    }

    // ---------------------------------------------------------------- tabelas
    public static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int l, int c) {
                return false;
            }
        };
    }

    public static JTable tabela(DefaultTableModel m, int... larguras) {
        JTable t = new JTable(m);
        comercial.caixa.UiCaixa.tabela(t, larguras, ComponentesWeb.AZUL);
        t.setAutoCreateRowSorter(true);
        t.setFillsViewportHeight(true);
        return t;
    }

    public static JScrollPane rolagem(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(javax.swing.BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        s.getViewport().setBackground(Color.WHITE);
        s.setPreferredSize(new Dimension(400, 320));
        return s;
    }

    /** Linha do modelo seleccionada (tem em conta a ordenação); -1 sem selecção. */
    public static int linha(JTable t) {
        int v = t.getSelectedRow();
        return v < 0 ? -1 : t.convertRowIndexToModel(v);
    }

    public static int[] linhas(JTable t) {
        int[] v = t.getSelectedRows();
        int[] m = new int[v.length];
        for (int i = 0; i < v.length; i++) {
            m[i] = t.convertRowIndexToModel(v[i]);
        }
        return m;
    }

    /** Renderer de uma coluna de estado: pílula colorida conforme o texto. */
    public static class RendererEstado extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foco, int l, int c) {
            super.getTableCellRendererComponent(t, v, sel, foco, l, c);
            String s = v == null ? "" : v.toString();
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            if (!sel) {
                setForeground(corEstado(s));
            }
            return this;
        }
    }

    public static Color corEstado(String s) {
        String x = s.toLowerCase(PT);
        if (x.contains("recus") || x.contains("injust") || x.contains("despedimento") || x.contains("caduc") || x.contains("inactivo")
                || x.contains("terminou") || x.contains("atraso")) {
            return ComponentesWeb.VERMELHO;
        }
        if (x.contains("pedido") || x.contains("aberto") || x.contains("nota de culpa") || x.contains("a terminar")
                || x.contains("por processar")) {
            return new Color(194, 120, 3);
        }
        if (x.contains("em gozo") || x.contains("defesa")) {
            return ComponentesWeb.AZUL_VIVO;
        }
        if (x.contains("cancel") || x.contains("arquiv") || x.contains("gozadas")) {
            return ComponentesWeb.CINZA;
        }
        return ComponentesWeb.VERDE;
    }

    // ---------------------------------------------------------------- funcionários
    /** Escolha de funcionário com pesquisa: escrever filtra pelo nome ou número. */
    public static JComboBox<DadosRH.Funcionario> seletor(List<DadosRH.Funcionario> todos) {
        JComboBox<DadosRH.Funcionario> c = new JComboBox<>(new DefaultComboBoxModel<>(todos.toArray(new DadosRH.Funcionario[0])));
        FormularioWeb.combo(c);
        c.setMaximumRowCount(16);
        c.setEditable(true);
        final JTextField editor = (JTextField) c.getEditor().getEditorComponent();
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                int k = e.getKeyCode();
                if (k == java.awt.event.KeyEvent.VK_UP || k == java.awt.event.KeyEvent.VK_DOWN || k == java.awt.event.KeyEvent.VK_ENTER
                        || k == java.awt.event.KeyEvent.VK_ESCAPE) {
                    return;
                }
                String texto = editor.getText();
                String q = normalizar(texto);
                List<DadosRH.Funcionario> l = new ArrayList<>();
                for (DadosRH.Funcionario f : todos) {
                    if (q.isEmpty() || normalizar(f.rotulo()).contains(q)) {
                        l.add(f);
                    }
                }
                c.setModel(new DefaultComboBoxModel<>(l.toArray(new DadosRH.Funcionario[0])));
                c.setSelectedItem(null);
                editor.setText(texto);
                if (!l.isEmpty() && c.isShowing()) {
                    c.showPopup();
                }
            }
        });
        c.setSelectedItem(null);
        return c;
    }

    /** O funcionário escolhido (null se o texto não corresponder a nenhum). */
    public static DadosRH.Funcionario escolhido(JComboBox<DadosRH.Funcionario> c) {
        Object o = c.getSelectedItem();
        if (o instanceof DadosRH.Funcionario) {
            return (DadosRH.Funcionario) o;
        }
        if (o != null && c.getItemCount() == 1) {
            return c.getItemAt(0);
        }
        return null;
    }

    /** Abre a Ficha do Funcionário (formulário completo) já com o funcionário carregado. */
    public static void abrirFicha(int idUser, int idEmpresa, String numero) {
        rh.visao.FichaFuncionarioVisao ficha = new rh.visao.FichaFuncionarioVisao(idUser, idEmpresa, util.BDConexao.getInstancia());
        ficha.setVisible(true);
        if (numero != null && !numero.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                rh.visao.FichaFuncionarioVisao.txtNumeroFuncionarioPesquisa.setText(numero);
                rh.visao.FichaFuncionarioVisao.txtNumeroFuncionarioPesquisa.postActionEvent();
            });
        }
    }

    // ---------------------------------------------------------------- datas e números
    public static String data(LocalDate d) {
        return d == null ? "" : d.format(DATA);
    }

    public static LocalDate data(java.util.Date d) {
        return d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static java.util.Date paraDate(LocalDate d) {
        return d == null ? null : java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static com.toedter.calendar.JDateChooser escolhaData(LocalDate inicial) {
        com.toedter.calendar.JDateChooser d = new com.toedter.calendar.JDateChooser();
        d.setDateFormatString("dd/MM/yyyy");
        FormularioWeb.data(d);
        d.setDate(paraDate(inicial));
        return d;
    }

    public static String moeda(double v) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(PT)).format(v);
    }

    public static String horas(double h) {
        if (h % DIA_HORAS == 0) {
            int dias = (int) (h / DIA_HORAS);
            return dias + (dias == 1 ? " dia" : " dias");
        }
        return new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(PT)).format(h) + " h";
    }

    /** Horas de um dia de trabalho (as faltas guardam-se em horas, como o salário as lê). */
    public static final double DIA_HORAS = util.DVML.HORA_UTIL_TRABALHO;

    public static String normalizar(String s) {
        return s == null ? "" : java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .toLowerCase(PT).trim();
    }

    public static JLabel rotuloSecao(String texto) {
        return util.ui.MenuWeb.rotuloGrupo(texto, util.ui.MenuWeb.DOURADO.darker());
    }

    public static JPanel linha(Component... cs) {
        JPanel p = ComponentesWeb.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        for (Component c : cs) {
            p.add(c);
        }
        return p;
    }

    // ---------------------------------------------------------------- Office
    /** Pasta dos documentos exportados: Documentos\Kitanda RH. */
    public static File pastaDocumentos() {
        File base = javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory();
        File p = new File(base, "Kitanda RH");
        p.mkdirs();
        return p;
    }

    static String nomeFicheiro(String titulo, String extensao) {
        String n = normalizar(titulo).replaceAll("[^a-z0-9]+", "_").replaceAll("^_|_$", "");
        return n + "_" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + "." + extensao;
    }

    /** Exporta a tabela (como está à vista: filtros e ordenação) para Excel (.xls) e abre-a. */
    public static void exportarExcel(Component pai, JTable t, String titulo) {
        try {
            org.apache.poi.hssf.usermodel.HSSFWorkbook wb = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
            org.apache.poi.hssf.usermodel.HSSFSheet folha = wb.createSheet(titulo.length() > 30 ? titulo.substring(0, 30) : titulo);
            org.apache.poi.hssf.usermodel.HSSFCellStyle negrito = wb.createCellStyle();
            org.apache.poi.hssf.usermodel.HSSFFont fn = wb.createFont();
            fn.setBoldweight(org.apache.poi.hssf.usermodel.HSSFFont.BOLDWEIGHT_BOLD);
            negrito.setFont(fn);
            org.apache.poi.hssf.usermodel.HSSFRow r0 = folha.createRow(0);
            r0.createCell(0).setCellValue(new org.apache.poi.hssf.usermodel.HSSFRichTextString(titulo));
            r0.getCell(0).setCellStyle(negrito);
            org.apache.poi.hssf.usermodel.HSSFRow cab = folha.createRow(2);
            for (int c = 0; c < t.getColumnCount(); c++) {
                cab.createCell(c).setCellValue(new org.apache.poi.hssf.usermodel.HSSFRichTextString(t.getColumnName(c)));
                cab.getCell(c).setCellStyle(negrito);
            }
            for (int l = 0; l < t.getRowCount(); l++) {
                org.apache.poi.hssf.usermodel.HSSFRow r = folha.createRow(l + 3);
                for (int c = 0; c < t.getColumnCount(); c++) {
                    Object v = t.getValueAt(l, c);
                    if (v instanceof Number) {
                        r.createCell(c).setCellValue(((Number) v).doubleValue());
                    } else {
                        r.createCell(c).setCellValue(new org.apache.poi.hssf.usermodel.HSSFRichTextString(v == null ? "" : v.toString()));
                    }
                }
            }
            for (int c = 0; c < t.getColumnCount(); c++) {
                folha.setColumnWidth(c, Math.min(60, Math.max(10, t.getColumnModel().getColumn(c).getWidth() / 7)) * 256);
            }
            File f = new File(pastaDocumentos(), nomeFicheiro(titulo, "xls"));
            try (FileOutputStream out = new FileOutputStream(f)) {
                wb.write(out);
            }
            abrir(pai, f);
        } catch (Exception e) {
            erro(pai, e);
        }
    }

    /** Exporta um documento de texto para Word (.docx) e abre-o. Linhas que começam por "# " ficam a negrito. */
    public static void exportarWord(Component pai, String titulo, String texto) {
        try {
            File f = new File(pastaDocumentos(), nomeFicheiro(titulo, "docx"));
            DocxSimples.gravar(f, texto);
            abrir(pai, f);
        } catch (Exception e) {
            erro(pai, e);
        }
    }

    public static void abrir(Component pai, File f) {
        try {
            java.awt.Desktop.getDesktop().open(f);
        } catch (Exception e) {
            info(pai, "Documento gravado em:\n" + f.getAbsolutePath());
        }
    }

    /** Imprime um texto simples (com "# " para títulos) pela impressora escolhida. */
    public static void imprimir(Component pai, String titulo, String texto) {
        try {
            javax.swing.JEditorPane ep = new javax.swing.JEditorPane("text/html", DocxSimples.html(texto));
            ep.setSize(700, 10);
            ep.print(null, new java.text.MessageFormat(titulo + " — página {0}"), true, null, null, true);
        } catch (Exception e) {
            erro(pai, e);
        }
    }

    /** Uma janela já aberta deste tipo vem para a frente em vez de abrir outra. */
    public static boolean trazerParaFrente(Class<? extends Window> tipo) {
        for (Window w : Window.getWindows()) {
            if (tipo.isInstance(w) && w.isShowing()) {
                if (w instanceof JFrame) {
                    ((JFrame) w).setExtendedState(((JFrame) w).getExtendedState() & ~JFrame.ICONIFIED);
                }
                w.toFront();
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------------- campos
    /** Texto de várias linhas (factos, defesa, decisão), com quebra de linha. */
    public static javax.swing.JTextArea areaTexto(int linhas) {
        javax.swing.JTextArea a = new javax.swing.JTextArea(linhas, 20);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setFont(EstiloUI.FONTE.deriveFont(13f));
        a.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return a;
    }

    public static JScrollPane rolagem(javax.swing.JTextArea a) {
        JScrollPane s = new JScrollPane(a);
        s.setBorder(javax.swing.BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        return s;
    }

    /** Hora (HH:mm) num spinner. */
    public static javax.swing.JSpinner escolhaHora(java.time.LocalTime inicial) {
        javax.swing.SpinnerDateModel m = new javax.swing.SpinnerDateModel();
        javax.swing.JSpinner s = new javax.swing.JSpinner(m);
        s.setEditor(new javax.swing.JSpinner.DateEditor(s, "HH:mm"));
        java.time.LocalTime h = inicial == null ? java.time.LocalTime.of(10, 0) : inicial;
        s.setValue(java.util.Date.from(LocalDate.now().atTime(h).atZone(ZoneId.systemDefault()).toInstant()));
        FormularioWeb.spinner(s);
        return s;
    }

    public static java.time.LocalTime hora(javax.swing.JSpinner s) {
        return ((java.util.Date) s.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
    }

    /** Nome, NIF e morada da empresa (para o cabeçalho dos documentos). */
    public static String[] empresa() {
        String[] e = {"", "", ""};
        try (java.sql.Connection cn = DadosRH.ligacao(); java.sql.Statement st = cn.createStatement();
                java.sql.ResultSet rs = st.executeQuery("SELECT nome, nif, enderecos FROM tb_dados_instituicao LIMIT 1")) {
            if (rs.next()) {
                for (int i = 0; i < 3; i++) {
                    e[i] = DadosRH.t(rs.getString(i + 1));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    /** Cabeçalho de um documento: empresa, NIF e morada. */
    public static String cabecalhoDocumento() {
        String[] e = empresa();
        return "## " + e[0] + "\n" + (e[1].isEmpty() ? "" : "NIF: " + e[1] + "\n") + (e[2].isEmpty() ? "" : e[2] + "\n");
    }

    /** "Luanda, 17 de Novembro de 2026" (a cidade é a primeira parte da morada). */
    public static String localEData() {
        String morada = empresa()[2];
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", PT));
        return (morada.isEmpty() ? "" : morada.split(",")[0].trim() + ", ") + hoje;
    }

    /** Diálogo simples com conteúdo e botões em baixo (tabelas auxiliares). */
    public static javax.swing.JDialog dialogo(Window dono, String titulo, Component norte, Component centro, int largura, int altura,
            Component... botoes) {
        javax.swing.JDialog d = new javax.swing.JDialog(dono, titulo, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(ComponentesWeb.FUNDO_PAGINA);
        p.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        if (norte != null) {
            p.add(norte, BorderLayout.NORTH);
        }
        p.add(centro, BorderLayout.CENTER);
        p.add(linha(botoes), BorderLayout.SOUTH);
        d.setContentPane(p);
        d.setSize(largura, altura);
        d.setLocationRelativeTo(dono);
        return d;
    }

    static JPanel bloco(Component norte, Component centro) {
        JPanel p = ComponentesWeb.transparente(new BorderLayout(0, 10));
        if (norte != null) {
            p.add(norte, BorderLayout.NORTH);
        }
        p.add(centro, BorderLayout.CENTER);
        return p;
    }
}
