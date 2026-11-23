package rh.web;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;

/**
 * Faltas e assiduidade: registar (um ou vários funcionários, um ou vários
 * dias úteis, dia inteiro ou horas, com o tipo de falta) e justificar num
 * clique. Grava em tb_falta, em horas, tal como o processamento de salários
 * já lê (falta injustificada desconta; justificada não).
 */
public class FaltasWeb extends JFrame {

    private final int idUser;
    private List<DadosRH.Funcionario> funcionarios = new ArrayList<>();
    private List<DadosRH.TipoFalta> tipos = new ArrayList<>();
    private final List<DadosRH.Falta> faltas = new ArrayList<>();

    // registo
    private final JTextField pesquisaFunc = new JTextField();
    private final DefaultListModel<DadosRH.Funcionario> mFunc = new DefaultListModel<>();
    private final JList<DadosRH.Funcionario> lFunc = new JList<>(mFunc);
    private final com.toedter.calendar.JDateChooser dDe = UiRH.escolhaData(LocalDate.now());
    private final com.toedter.calendar.JDateChooser dAte = UiRH.escolhaData(LocalDate.now());
    private final JToggleButton rbDia = new JToggleButton("Dia inteiro", true), rbHoras = new JToggleButton("Horas");
    private final JSpinner spHoras = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 24.0, 0.5));
    private final JComboBox<DadosRH.TipoFalta> cbTipo = new JComboBox<>();
    private final JTextField txtObs = new JTextField();
    private final javax.swing.JLabel lbResumo = FormularioWeb.dica(" ");

    // lista
    private final JComboBox<String> cbMes = new JComboBox<>();
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Todas", "Injustificadas", "Justificadas"});
    private final JTextField filtro = new JTextField();
    private final DefaultTableModel modelo = UiRH.modelo("Data", "Nº", "Funcionário", "Departamento", "Tipo", "Duração", "Situação", "Observação");
    private final JTable tabela = UiRH.tabela(modelo, 90, 60, 200, 130, 200, 80, 110, 200);
    private final ComponentesWeb.CartaoKpi kTotal = new ComponentesWeb.CartaoKpi("F", "Faltas no período", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kInj = new ComponentesWeb.CartaoKpi("!", "Injustificadas (descontam)", ComponentesWeb.VERMELHO).compacto();
    private final ComponentesWeb.CartaoKpi kJust = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Justificadas", ComponentesWeb.VERDE).compacto();
    private final List<LocalDate[]> periodos = new ArrayList<>();

    public FaltasWeb(int idUser) {
        this.idUser = idUser;
        FormularioWeb.campoTexto(pesquisaFunc, txtObs, filtro);
        FormularioWeb.combo(cbTipo, cbMes, cbEstado);
        FormularioWeb.spinner(spHoras);
        spHoras.setEnabled(false);
        ButtonGroup g = new ButtonGroup();
        g.add(rbDia);
        g.add(rbHoras);
        rbDia.addActionListener(e -> {
            spHoras.setEnabled(false);
            resumo();
        });
        rbHoras.addActionListener(e -> {
            spHoras.setEnabled(true);
            resumo();
        });
        lFunc.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lFunc.setVisibleRowCount(9);
        lFunc.setCellRenderer(new ComponentesWeb.RendererLista(null));
        lFunc.setFixedCellHeight(28);
        lFunc.addListSelectionListener(e -> resumo());
        pesquisaFunc.setToolTipText("Escreva para filtrar; Ctrl+clique para escolher vários");
        pesquisaFunc.getDocument().addDocumentListener(documento(this::filtrarFuncionarios));
        filtro.getDocument().addDocumentListener(documento(this::mostrar));
        dDe.addPropertyChangeListener("date", e -> {
            if (dAte.getDate() == null || (dDe.getDate() != null && dAte.getDate().before(dDe.getDate()))) {
                dAte.setDate(dDe.getDate());
            }
            resumo();
        });
        dAte.addPropertyChangeListener("date", e -> resumo());
        cbMes.addActionListener(e -> carregarFaltas());
        cbEstado.addActionListener(e -> mostrar());
        tabela.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new UiRH.RendererEstado());

        // meses: o actual e os 12 anteriores
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 13; i++) {
            LocalDate m = inicio.minusMonths(i);
            periodos.add(new LocalDate[]{m, m.withDayOfMonth(m.lengthOfMonth())});
            String nome = m.getMonth().getDisplayName(java.time.format.TextStyle.FULL, UiRH.PT);
            cbMes.addItem(Character.toUpperCase(nome.charAt(0)) + nome.substring(1) + " " + m.getYear());
        }

        JScrollPane rolFunc = new JScrollPane(lFunc);
        rolFunc.setPreferredSize(new Dimension(260, 230));
        JButton registar = UiRH.botao("Registar falta", ComponentesWeb.Variante.SUCESSO, "Grava uma falta por funcionário e por dia útil", this::registar);
        JPanel formulario = FormularioWeb.pilha(9,
                ComponentesWeb.campo("Funcionário(s) — Ctrl+clique para vários", pesquisaFunc),
                rolFunc,
                FormularioWeb.grelha(120, 2, ComponentesWeb.campo("De", dDe), ComponentesWeb.campo("Até", dAte)),
                ComponentesWeb.campo("Duração", UiRH.linha(FormularioWeb.pilulas(rbDia, rbHoras), spHoras)),
                ComponentesWeb.campo("Tipo de falta", cbTipo),
                ComponentesWeb.campo("Observação (opcional)", txtObs),
                lbResumo,
                registar);
        JPanel esquerda = FormularioWeb.cartao("Registar falta", ComponentesWeb.VERMELHO, formulario);

        JPanel filtros = FormularioWeb.grelha(170, 3, ComponentesWeb.campo("Mês", cbMes), ComponentesWeb.campo("Mostrar", cbEstado),
                ComponentesWeb.campo("Pesquisar", filtro));
        JPanel lista = UiRH.bloco(FormularioWeb.pilha(8, FormularioWeb.grelha(150, 3, kTotal, kInj, kJust), filtros), UiRH.rolagem(tabela));
        JPanel direita = FormularioWeb.cartao("Faltas do mês", UiRH.COR_RH, lista);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(new double[]{0.32, 0.68}, new int[]{300, 520}, 10));
        colunas.add(esquerda);
        colunas.add(direita);

        JButton justificar = UiRH.botao("Justificar", ComponentesWeb.Variante.PRIMARIO, "Justificar as faltas seleccionadas", this::justificar);
        JButton eliminar = UiRH.botao("Eliminar", ComponentesWeb.Variante.PERIGO, "Apagar as faltas seleccionadas (registo errado)", this::eliminar);
        JButton tiposBt = UiRH.botao("Tipos de falta", ComponentesWeb.Variante.SECUNDARIO, "Códigos de falta (como no Primavera)", this::editarTipos);
        JButton excel = UiRH.botao("Excel", ComponentesWeb.Variante.SECUNDARIO, null, () -> UiRH.exportarExcel(this, tabela, "Faltas " + cbMes.getSelectedItem()));
        UiRH.janela(this, "Faltas e Assiduidade", "Registe e justifique faltas sem dar voltas: o salário já as considera", null, colunas,
                FormularioWeb.dica("As injustificadas descontam no salário do mês"), justificar, eliminar, tiposBt, excel, UiRH.sair(this));
        carregar();
    }

    private static javax.swing.event.DocumentListener documento(Runnable r) {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                r.run();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                r.run();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        };
    }

    private void carregar() {
        try (Connection cn = DadosRH.ligacao()) {
            funcionarios = DadosRH.funcionarios(cn, true);
            tipos = DadosRH.tiposFalta(cn, true);
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(tipos.toArray(new DadosRH.TipoFalta[0])));
        filtrarFuncionarios();
        carregarFaltas();
    }

    private void filtrarFuncionarios() {
        String q = UiRH.normalizar(pesquisaFunc.getText());
        List<DadosRH.Funcionario> sel = lFunc.getSelectedValuesList();
        mFunc.clear();
        for (DadosRH.Funcionario f : funcionarios) {
            if (q.isEmpty() || UiRH.normalizar(f.rotulo() + " " + f.departamento).contains(q)) {
                mFunc.addElement(f);
            }
        }
        for (DadosRH.Funcionario f : sel) {
            int i = mFunc.indexOf(f);
            if (i >= 0) {
                lFunc.addSelectionInterval(i, i);
            }
        }
    }

    private List<LocalDate> dias() {
        LocalDate de = UiRH.data(dDe.getDate()), ate = UiRH.data(dAte.getDate());
        if (de == null || ate == null || ate.isBefore(de)) {
            return new ArrayList<>();
        }
        Map<LocalDate, String> feriados = new LinkedHashMap<>();
        for (int a = de.getYear(); a <= ate.getYear(); a++) {
            feriados.putAll(DadosRH.feriados(a)); // em memória: isto corre a cada clique
        }
        return CalendarioRH.diasUteis(de, ate, feriados);
    }

    private void resumo() {
        int n = lFunc.getSelectedValuesList().size();
        int d = dias().size();
        lbResumo.setText(n == 0 ? "Escolha o(s) funcionário(s)"
                : n + (n == 1 ? " funcionário" : " funcionários") + " × " + d + (d == 1 ? " dia útil" : " dias úteis")
                + (rbHoras.isSelected() ? " de " + spHoras.getValue() + " h" : ""));
    }

    private void registar() {
        List<DadosRH.Funcionario> sel = lFunc.getSelectedValuesList();
        DadosRH.TipoFalta tipo = (DadosRH.TipoFalta) cbTipo.getSelectedItem();
        List<LocalDate> dias = dias();
        if (sel.isEmpty()) {
            UiRH.aviso(this, "Escolha pelo menos um funcionário.");
            return;
        }
        if (tipo == null) {
            UiRH.aviso(this, "Escolha o tipo de falta.");
            return;
        }
        if (dias.isEmpty()) {
            UiRH.aviso(this, "O intervalo não tem dias úteis (fins de semana e feriados não contam).");
            return;
        }
        double horas = rbDia.isSelected() ? UiRH.DIA_HORAS : ((Number) spHoras.getValue()).doubleValue();
        List<Integer> ids = new ArrayList<>();
        for (DadosRH.Funcionario f : sel) {
            ids.add(f.id);
        }
        try (Connection cn = DadosRH.ligacao()) {
            int n = DadosRH.registarFaltas(cn, ids, dias, horas, tipo, txtObs.getText().trim());
            int saltadas = ids.size() * dias.size() - n;
            UiRH.info(this, n + (n == 1 ? " falta registada." : " faltas registadas.")
                    + (saltadas > 0 ? "\n" + saltadas + " já estavam registadas e não foram repetidas." : ""));
            txtObs.setText("");
            cbMes.setSelectedIndex(indicePeriodo(dias.get(0)));
            carregarFaltas();
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
    }

    private int indicePeriodo(LocalDate d) {
        for (int i = 0; i < periodos.size(); i++) {
            if (!d.isBefore(periodos.get(i)[0]) && !d.isAfter(periodos.get(i)[1])) {
                return i;
            }
        }
        return Math.max(0, cbMes.getSelectedIndex());
    }

    private void carregarFaltas() {
        int i = Math.max(0, cbMes.getSelectedIndex());
        if (periodos.isEmpty()) {
            return;
        }
        faltas.clear();
        try (Connection cn = DadosRH.ligacao()) {
            faltas.addAll(DadosRH.faltas(cn, periodos.get(i)[0], periodos.get(i)[1], null));
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        mostrar();
    }

    private final List<DadosRH.Falta> visiveis = new ArrayList<>();

    private void mostrar() {
        String q = UiRH.normalizar(filtro.getText());
        int estado = cbEstado.getSelectedIndex();
        modelo.setRowCount(0);
        visiveis.clear();
        double total = 0, inj = 0, just = 0;
        for (DadosRH.Falta f : faltas) {
            if (estado == 1 && f.justificada || estado == 2 && !f.justificada) {
                continue;
            }
            if (!q.isEmpty() && !UiRH.normalizar(f.numero + " " + f.funcionario + " " + f.departamento + " " + f.tipo).contains(q)) {
                continue;
            }
            visiveis.add(f);
            total += f.horas;
            if (f.justificada) {
                just += f.horas;
            } else {
                inj += f.horas;
            }
            modelo.addRow(new Object[]{UiRH.data(f.data), f.numero, f.funcionario, f.departamento, f.tipo, UiRH.horas(f.horas),
                f.justificada ? "Justificada" : "Injustificada", f.justificada && !f.justificacao.isEmpty() ? f.justificacao : f.descricao});
        }
        kTotal.setValor(UiRH.horas(total), visiveis.size() + " registos");
        kInj.setValor(UiRH.horas(inj), "descontam no salário");
        kJust.setValor(UiRH.horas(just), "não descontam");
    }

    private List<Integer> seleccionadas() {
        List<Integer> l = new ArrayList<>();
        for (int i : UiRH.linhas(tabela)) {
            l.add(visiveis.get(i).id);
        }
        if (l.isEmpty()) {
            UiRH.aviso(this, "Seleccione uma ou mais faltas na lista.");
        }
        return l;
    }

    private void justificar() {
        List<Integer> ids = seleccionadas();
        if (ids.isEmpty()) {
            return;
        }
        List<DadosRH.TipoFalta> justificadas = new ArrayList<>();
        for (DadosRH.TipoFalta t : tipos) {
            if (t.justificada) {
                justificadas.add(t);
            }
        }
        JComboBox<DadosRH.TipoFalta> cb = new JComboBox<>(justificadas.toArray(new DadosRH.TipoFalta[0]));
        JTextField motivo = new JTextField();
        JPanel p = FormularioWeb.pilha(8, ComponentesWeb.campo("Motivo (tipo)", cb), ComponentesWeb.campo("Documento / nota (opcional)", motivo));
        p.setPreferredSize(new Dimension(420, 120));
        if (JOptionPane.showConfirmDialog(this, p, "Justificar " + ids.size() + (ids.size() == 1 ? " falta" : " faltas"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION || cb.getSelectedItem() == null) {
            return;
        }
        try (Connection cn = DadosRH.ligacao()) {
            DadosRH.justificar(cn, ids, (DadosRH.TipoFalta) cb.getSelectedItem(), motivo.getText());
            carregarFaltas();
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
    }

    private void eliminar() {
        List<Integer> ids = seleccionadas();
        if (ids.isEmpty() || !ComponentesWeb.confirmarOperacao(this, "Eliminar faltas",
                "Apagar " + ids.size() + (ids.size() == 1 ? " falta" : " faltas") + " (registo errado).")) {
            return;
        }
        try (Connection cn = DadosRH.ligacao()) {
            DadosRH.eliminarFaltas(cn, ids);
            carregarFaltas();
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
    }

    /** Tabela dos tipos de falta: código, designação, justificada, remunerada, activo. */
    private void editarTipos() {
        DefaultTableModel m = new DefaultTableModel(new String[]{"Código", "Designação", "Justificada", "Remunerada", "Activo"}, 0) {
            @Override
            public Class<?> getColumnClass(int c) {
                return c >= 2 ? Boolean.class : String.class;
            }
        };
        List<DadosRH.TipoFalta> lista;
        try (Connection cn = DadosRH.ligacao()) {
            lista = DadosRH.tiposFalta(cn, false);
        } catch (Exception e) {
            UiRH.erro(this, e);
            return;
        }
        for (DadosRH.TipoFalta t : lista) {
            m.addRow(new Object[]{t.codigo, t.designacao, t.justificada, t.remunerada, t.activo});
        }
        JTable t = new JTable(m);
        comercial.caixa.UiCaixa.tabela(t, new int[]{70, 300, 90, 90, 70}, ComponentesWeb.AZUL);
        JDialog d = new JDialog(this, "Tipos de falta", true);
        JButton novo = UiRH.botao("Novo tipo", ComponentesWeb.Variante.SECUNDARIO, null, () -> m.addRow(new Object[]{"", "", true, false, true}));
        JButton gravar = UiRH.botao("Gravar", ComponentesWeb.Variante.SUCESSO, null, () -> {
            if (t.isEditing()) {
                t.getCellEditor().stopCellEditing();
            }
            try (Connection cn = DadosRH.ligacao()) {
                for (int i = 0; i < m.getRowCount(); i++) {
                    String cod = String.valueOf(m.getValueAt(i, 0)).trim(), des = String.valueOf(m.getValueAt(i, 1)).trim();
                    if (cod.isEmpty() || des.isEmpty()) {
                        continue;
                    }
                    DadosRH.TipoFalta tf = i < lista.size() ? lista.get(i) : new DadosRH.TipoFalta();
                    tf.codigo = cod.toUpperCase(UiRH.PT);
                    tf.designacao = des;
                    tf.justificada = Boolean.TRUE.equals(m.getValueAt(i, 2));
                    tf.remunerada = Boolean.TRUE.equals(m.getValueAt(i, 3));
                    tf.activo = Boolean.TRUE.equals(m.getValueAt(i, 4));
                    DadosRH.gravarTipoFalta(cn, tf);
                }
            } catch (Exception e) {
                throw UiRH.falha(e);
            }
            d.dispose();
            carregar();
        });
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(ComponentesWeb.FUNDO_PAGINA);
        p.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        p.add(FormularioWeb.dica("Justificada = não desconta no salário. Desactive em vez de apagar os que já foram usados."), BorderLayout.NORTH);
        p.add(UiRH.rolagem(t), BorderLayout.CENTER);
        p.add(UiRH.linha(novo, gravar, UiRH.botao("Fechar", ComponentesWeb.Variante.PERIGO, null, d::dispose)), BorderLayout.SOUTH);
        d.setContentPane(p);
        d.setSize(720, 460);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    /** Não usado: evita o aviso de campo por usar quando o Primavera-like "Todos" for preciso. */
    @SuppressWarnings("unused")
    private static JCheckBox caixa(String t) {
        return new JCheckBox(t);
    }
}
