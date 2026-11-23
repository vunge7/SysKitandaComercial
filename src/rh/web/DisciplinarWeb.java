package rh.web;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;

/**
 * Processos disciplinares, passo a passo: instaurar (factos), nota de culpa
 * com a convocatória para a entrevista, defesa do trabalhador, decisão com a
 * sanção. Cada passo gera o documento em Word (editável) e fica na linha do
 * tempo do processo; o painel avisa quando um prazo está a passar.
 */
public class DisciplinarWeb extends JFrame {

    private final int idUser;
    private final List<DadosRH.Funcionario> funcionarios = new ArrayList<>();
    private final List<DadosRH.Processo> processos = new ArrayList<>();
    private final List<DadosRH.Processo> visiveis = new ArrayList<>();
    private DadosRH.Processo actual;

    // lista
    private final JToggleButton rbAbertos = new JToggleButton("Abertos", true), rbTodos = new JToggleButton("Todos");
    private final JTextField filtro = new JTextField();
    private final DefaultTableModel modelo = UiRH.modelo("Processo", "Funcionário", "Aberto em", "Fase", "Estado");
    private final JTable tabela = UiRH.tabela(modelo, 90, 190, 90, 150, 90);

    // ficha
    private final JLabel lbTitulo = UiRH.rotuloSecao("Novo processo");
    private final JLabel lbPrazo = FormularioWeb.dica(" ");
    private final JComboBox<DadosRH.Funcionario> cbFunc = UiRH.seletor(funcionarios);
    private final com.toedter.calendar.JDateChooser dOcorrencia = UiRH.escolhaData(null);
    private final com.toedter.calendar.JDateChooser dConhecimento = UiRH.escolhaData(null);
    private final JTextArea txtFactos = UiRH.areaTexto(4);
    private final JTextField txtTestemunhas = new JTextField();
    private final com.toedter.calendar.JDateChooser dNotaCulpa = UiRH.escolhaData(null);
    private final com.toedter.calendar.JDateChooser dEntrevista = UiRH.escolhaData(null);
    private final JSpinner spHora = UiRH.escolhaHora(null);
    private final JTextArea txtDefesa = UiRH.areaTexto(4);
    private final JComboBox<String> cbSancao = new JComboBox<>(DadosRH.SANCOES);
    private final JSpinner spDias = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
    private final com.toedter.calendar.JDateChooser dDecisao = UiRH.escolhaData(null);
    private final JTextArea txtDecisao = UiRH.areaTexto(4);
    private final DefaultTableModel mEventos = UiRH.modelo("Quando", "Acontecimento", "Detalhe", "Utilizador");
    private final JTable tEventos = UiRH.tabela(mEventos, 120, 170, 300, 110);
    private final JTabbedPane passos = new JTabbedPane();

    private final ComponentesWeb.CartaoKpi kAbertos = new ComponentesWeb.CartaoKpi("PD", "Processos abertos", ComponentesWeb.LARANJA).compacto();
    private final ComponentesWeb.CartaoKpi kAtraso = new ComponentesWeb.CartaoKpi("!", "Com prazo a passar", ComponentesWeb.VERMELHO).compacto();
    private final ComponentesWeb.CartaoKpi kAno = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Concluídos este ano", ComponentesWeb.VERDE).compacto();

    public DisciplinarWeb(int idUser) {
        this.idUser = idUser;
        FormularioWeb.campoTexto(filtro, txtTestemunhas);
        FormularioWeb.combo(cbSancao);
        FormularioWeb.spinner(spDias);
        ButtonGroup g = new ButtonGroup();
        g.add(rbAbertos);
        g.add(rbTodos);
        rbAbertos.addActionListener(e -> mostrar());
        rbTodos.addActionListener(e -> mostrar());
        filtro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                mostrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                mostrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
        tabela.getColumnModel().getColumn(3).setCellRenderer(new UiRH.RendererEstado());
        tabela.getColumnModel().getColumn(4).setCellRenderer(new UiRH.RendererEstado());
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int l = UiRH.linha(tabela);
                if (l >= 0) {
                    abrir(visiveis.get(l));
                }
            }
        });
        cbSancao.addActionListener(e -> spDias.setEnabled(String.valueOf(cbSancao.getSelectedItem()).contains("temporária")));

        // ---- lista à esquerda
        JPanel filtros = FormularioWeb.pilha(8, FormularioWeb.pilulas(rbAbertos, rbTodos), ComponentesWeb.campo("Pesquisar", filtro));
        JButton novo = UiRH.botao("Novo processo", ComponentesWeb.Variante.SUCESSO, "Instaurar um processo", this::novo);
        JPanel esquerda = FormularioWeb.cartao("Processos", UiRH.COR_RH,
                UiRH.bloco(FormularioWeb.pilha(8, filtros, novo), UiRH.rolagem(tabela)));

        // ---- ficha à direita, por passos
        JPanel p1 = FormularioWeb.pilha(9,
                ComponentesWeb.campo("Trabalhador (escreva para procurar)", cbFunc),
                FormularioWeb.grelha(150, 2, ComponentesWeb.campo("Data dos factos", dOcorrencia),
                        ComponentesWeb.campo("Data em que a empresa soube", dConhecimento)),
                ComponentesWeb.campo("Factos (o que aconteceu, quando, onde e o dever violado)", UiRH.rolagem(txtFactos)),
                ComponentesWeb.campo("Testemunhas (opcional)", txtTestemunhas));
        JPanel p2 = FormularioWeb.pilha(9,
                FormularioWeb.dica("A nota de culpa leva os factos e convoca o trabalhador para a entrevista, onde se pode defender."),
                FormularioWeb.grelha(150, 3, ComponentesWeb.campo("Data da nota de culpa", dNotaCulpa),
                        ComponentesWeb.campo("Data da entrevista", dEntrevista), ComponentesWeb.campo("Hora", spHora)),
                UiRH.linha(UiRH.botao("Nota de culpa e convocatória (Word)", ComponentesWeb.Variante.PRIMARIO,
                        "Gera o documento e passa o processo para \"" + DadosRH.FASE_CONVOCADO + "\"", this::notaCulpa)));
        JPanel p3 = FormularioWeb.pilha(9,
                ComponentesWeb.campo("Defesa do trabalhador (o que disse na entrevista ou entregou por escrito)", UiRH.rolagem(txtDefesa)),
                UiRH.linha(UiRH.botao("Registar defesa", ComponentesWeb.Variante.PRIMARIO,
                        "Passa o processo para \"" + DadosRH.FASE_DEFESA + "\"", this::registarDefesa),
                        UiRH.botao("Acta da entrevista (Word)", ComponentesWeb.Variante.SECUNDARIO, null, this::acta)));
        JPanel p4 = FormularioWeb.pilha(9,
                FormularioWeb.grelha(150, 3, ComponentesWeb.campo("Sanção", cbSancao), ComponentesWeb.campo("Dias (se temporária)", spDias),
                        ComponentesWeb.campo("Data da decisão", dDecisao)),
                ComponentesWeb.campo("Fundamentação da decisão", UiRH.rolagem(txtDecisao)),
                UiRH.linha(UiRH.botao("Decidir e gerar decisão (Word)", ComponentesWeb.Variante.SUCESSO,
                        "Conclui o processo e gera a comunicação da decisão", this::decidir)));
        passos.addTab("1 · Factos", p1);
        passos.addTab("2 · Nota de culpa", p2);
        passos.addTab("3 · Defesa", p3);
        passos.addTab("4 · Decisão", p4);
        passos.addTab("Linha do tempo", UiRH.rolagem(tEventos));
        passos.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        for (int i = 0; i < 4; i++) {
            ((JPanel) passos.getComponentAt(i)).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        JPanel topo = FormularioWeb.pilha(4, lbTitulo, lbPrazo);
        JPanel ficha = ComponentesWeb.transparente(new BorderLayout(0, 8));
        ficha.add(topo, BorderLayout.NORTH);
        ficha.add(passos, BorderLayout.CENTER);
        JPanel direita = FormularioWeb.cartao("Processo", ComponentesWeb.LARANJA, ficha);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(new double[]{0.36, 0.64}, new int[]{340, 520}, 10));
        colunas.add(esquerda);
        colunas.add(direita);
        JPanel conteudo = UiRH.bloco(FormularioWeb.grelha(170, 3, kAbertos, kAtraso, kAno), colunas);

        JButton gravar = UiRH.botao("Gravar", ComponentesWeb.Variante.SUCESSO, "Gravar o que escreveu, sem mudar de fase", () -> gravar(null));
        JButton arquivar = UiRH.botao("Arquivar", ComponentesWeb.Variante.AVISO, "Encerrar sem sanção (ex.: factos não provados)", this::arquivar);
        JButton historico = UiRH.botao("Processo completo (Word)", ComponentesWeb.Variante.SECUNDARIO,
                "Todo o processo num documento, para o arquivo", this::processoCompleto);
        JButton excel = UiRH.botao("Excel", ComponentesWeb.Variante.SECUNDARIO, null, () -> UiRH.exportarExcel(this, tabela, "Processos disciplinares"));
        JButton prazos = UiRH.botao("Prazos", ComponentesWeb.Variante.SECUNDARIO, "Prazos usados nos avisos", () -> {
            TabelasRH.definicoes(this);
            lerPrazos();
            mostrar();
            prazo();
        });
        UiRH.janela(this, "Processos Disciplinares", "Do auto de ocorrência à decisão, com os documentos prontos e os prazos à vista",
                null, conteudo, FormularioWeb.dica("Seleccione um processo na lista ou crie um novo"),
                gravar, arquivar, historico, excel, prazos, UiRH.sair(this));
        carregar();
        novo();
    }

    // ---------------------------------------------------------------- dados
    private void carregar() {
        int idActual = actual == null ? 0 : actual.id;
        processos.clear();
        try (Connection cn = DadosRH.ligacao()) {
            funcionarios.clear();
            funcionarios.addAll(DadosRH.funcionarios(cn, false));
            processos.addAll(DadosRH.processos(cn, false));
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        lerPrazos();
        Object f = cbFunc.getSelectedItem();
        cbFunc.setModel(new javax.swing.DefaultComboBoxModel<>(funcionarios.toArray(new DadosRH.Funcionario[0])));
        cbFunc.setSelectedItem(f);
        mostrar();
        for (DadosRH.Processo p : processos) {
            if (p.id == idActual) {
                abrir(p);
            }
        }
    }

    private int prazoDecisao, prazoInstauracao, antecedencia;

    private void lerPrazos() {
        try (Connection cn = DadosRH.ligacao()) {
            prazoDecisao = DadosRH.configInt(cn, DadosRH.C_PRAZO_DECISAO, 30);
            prazoInstauracao = DadosRH.configInt(cn, DadosRH.C_PRAZO_INSTAURACAO, 30);
            antecedencia = DadosRH.configInt(cn, DadosRH.C_ANTECEDENCIA_ENTREVISTA, 3);
        } catch (Exception e) {
            prazoDecisao = 30;
            prazoInstauracao = 30;
            antecedencia = 3;
        }
    }

    /** Aviso de prazo de um processo aberto (vazio se estiver em dia). */
    private String alertaPrazo(DadosRH.Processo p) {
        if (!DadosRH.ABERTO.equals(p.estado)) {
            return "";
        }
        LocalDate hoje = LocalDate.now();
        if (DadosRH.FASE_INSTAURADO.equals(p.fase) && p.conhecimento != null) {
            long d = ChronoUnit.DAYS.between(p.conhecimento, hoje);
            if (d > prazoInstauracao) {
                return "Já passaram " + d + " dias desde que a empresa soube dos factos (prazo: " + prazoInstauracao + "): envie a nota de culpa.";
            }
            if (d > prazoInstauracao - 7) {
                return "Restam " + (prazoInstauracao - d) + " dias para enviar a nota de culpa.";
            }
        }
        if (p.entrevista != null && !DadosRH.FASE_DECIDIDO.equals(p.fase)) {
            long d = ChronoUnit.DAYS.between(p.entrevista.toLocalDate(), hoje);
            if (d > prazoDecisao) {
                return "Já passaram " + d + " dias desde a entrevista (prazo: " + prazoDecisao + "): falta a decisão.";
            }
            if (d >= 0 && d > prazoDecisao - 7) {
                return "Restam " + (prazoDecisao - d) + " dias para a decisão.";
            }
        }
        return "";
    }

    private void mostrar() {
        String q = UiRH.normalizar(filtro.getText());
        modelo.setRowCount(0);
        visiveis.clear();
        int abertos = 0, atraso = 0, concluidos = 0;
        int ano = LocalDate.now().getYear();
        for (DadosRH.Processo p : processos) {
            if (DadosRH.ABERTO.equals(p.estado)) {
                abertos++;
                if (!alertaPrazo(p).isEmpty()) {
                    atraso++;
                }
            } else if ((p.decisaoData != null ? p.decisaoData : p.abertura.toLocalDate()).getYear() == ano) {
                concluidos++;
            }
            if (rbAbertos.isSelected() && !DadosRH.ABERTO.equals(p.estado)) {
                continue;
            }
            if (!q.isEmpty() && !UiRH.normalizar(p.numero + " " + p.funcionario + " " + p.numeroFuncionario + " " + p.sancao).contains(q)) {
                continue;
            }
            visiveis.add(p);
            modelo.addRow(new Object[]{p.numero, p.funcionario, UiRH.data(p.abertura.toLocalDate()), p.fase, p.estado});
        }
        kAbertos.setValor(String.valueOf(abertos), abertos == 0 ? "nenhum em curso" : "em curso");
        kAtraso.setValor(String.valueOf(atraso), atraso == 0 ? "tudo em dia" : "ver o aviso no processo");
        kAno.setValor(String.valueOf(concluidos), "concluídos / arquivados em " + ano);
    }

    // ---------------------------------------------------------------- ficha
    private void novo() {
        actual = null;
        tabela.clearSelection();
        cbFunc.setEnabled(true);
        cbFunc.setSelectedItem(null);
        dOcorrencia.setDate(null);
        dConhecimento.setDate(UiRH.paraDate(LocalDate.now()));
        txtFactos.setText("");
        txtTestemunhas.setText("");
        dNotaCulpa.setDate(null);
        dEntrevista.setDate(null);
        txtDefesa.setText("");
        cbSancao.setSelectedIndex(0);
        spDias.setValue(0);
        dDecisao.setDate(null);
        txtDecisao.setText("");
        mEventos.setRowCount(0);
        lbTitulo.setText("Novo processo — descreva os factos e grave");
        lbPrazo.setText(" ");
        passos.setSelectedIndex(0);
        editavel(true);
    }

    private void abrir(DadosRH.Processo p) {
        actual = p;
        DadosRH.Funcionario escolhido = null;
        for (DadosRH.Funcionario f : funcionarios) {
            if (f.id == p.idFuncionario) {
                escolhido = f;
            }
        }
        cbFunc.setSelectedItem(escolhido);
        cbFunc.setEnabled(false);
        dOcorrencia.setDate(UiRH.paraDate(p.ocorrencia));
        dConhecimento.setDate(UiRH.paraDate(p.conhecimento));
        txtFactos.setText(p.factos);
        txtTestemunhas.setText(p.testemunhas);
        dNotaCulpa.setDate(UiRH.paraDate(p.notaCulpa));
        dEntrevista.setDate(p.entrevista == null ? null : UiRH.paraDate(p.entrevista.toLocalDate()));
        if (p.entrevista != null) {
            spHora.setValue(java.util.Date.from(p.entrevista.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        txtDefesa.setText(p.defesa);
        cbSancao.setSelectedItem(p.sancao.isEmpty() ? DadosRH.SANCOES[0] : p.sancao);
        spDias.setValue(p.diasSancao == null ? 0 : p.diasSancao);
        dDecisao.setDate(UiRH.paraDate(p.decisaoData));
        txtDecisao.setText(p.decisao);
        lbTitulo.setText(p.numero + " · " + p.funcionario + " · " + p.fase + " · " + p.estado);
        prazo();
        mEventos.setRowCount(0);
        try (Connection cn = DadosRH.ligacao()) {
            for (DadosRH.Evento e : DadosRH.eventos(cn, p.id)) {
                mEventos.addRow(new Object[]{e.quando == null ? "" : e.quando.format(UiRH.DATA_HORA), e.tipo, e.descricao, e.utilizador});
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        int passo = DadosRH.FASE_INSTAURADO.equals(p.fase) ? 1 : DadosRH.FASE_CONVOCADO.equals(p.fase) ? 2 : 3;
        passos.setSelectedIndex(DadosRH.ABERTO.equals(p.estado) ? passo : 4);
        editavel(DadosRH.ABERTO.equals(p.estado));
    }

    private void prazo() {
        String a = actual == null ? "" : alertaPrazo(actual);
        lbPrazo.setText(a.isEmpty() ? (actual != null && DadosRH.ABERTO.equals(actual.estado) ? "Prazos em dia." : " ") : "⚠ " + a);
        lbPrazo.setForeground(a.isEmpty() ? ComponentesWeb.VERDE.darker() : ComponentesWeb.VERMELHO);
    }

    private void editavel(boolean sim) {
        for (javax.swing.JComponent c : new javax.swing.JComponent[]{dOcorrencia, dConhecimento, txtFactos, txtTestemunhas, dNotaCulpa,
            dEntrevista, spHora, txtDefesa, cbSancao, dDecisao, txtDecisao}) {
            c.setEnabled(sim);
        }
        spDias.setEnabled(sim && String.valueOf(cbSancao.getSelectedItem()).contains("temporária"));
    }

    /** Copia a ficha para o processo e grava; evento = "tipo|descrição" para a linha do tempo. */
    private boolean gravar(String evento) {
        DadosRH.Funcionario f = UiRH.escolhido(cbFunc);
        if (f == null) {
            UiRH.aviso(this, "Escolha o trabalhador.");
            passos.setSelectedIndex(0);
            return false;
        }
        if (txtFactos.getText().trim().isEmpty()) {
            UiRH.aviso(this, "Descreva os factos (passo 1).");
            passos.setSelectedIndex(0);
            return false;
        }
        boolean novo = actual == null;
        DadosRH.Processo p = novo ? new DadosRH.Processo() : actual;
        if (!novo && !DadosRH.ABERTO.equals(p.estado) && evento == null) {
            UiRH.aviso(this, "Este processo está " + p.estado.toLowerCase(UiRH.PT) + " e já não se altera.");
            return false;
        }
        p.idFuncionario = f.id;
        p.ocorrencia = UiRH.data(dOcorrencia.getDate());
        p.conhecimento = UiRH.data(dConhecimento.getDate());
        p.factos = txtFactos.getText().trim();
        p.testemunhas = txtTestemunhas.getText().trim();
        p.notaCulpa = UiRH.data(dNotaCulpa.getDate());
        LocalDate de = UiRH.data(dEntrevista.getDate());
        p.entrevista = de == null ? null : LocalDateTime.of(de, UiRH.hora(spHora));
        p.defesa = txtDefesa.getText().trim();
        p.decisao = txtDecisao.getText().trim();
        p.decisaoData = UiRH.data(dDecisao.getDate());
        if (!p.decisao.isEmpty() || p.decisaoData != null) {
            p.sancao = String.valueOf(cbSancao.getSelectedItem());
            p.diasSancao = spDias.isEnabled() ? (Integer) spDias.getValue() : null;
        }
        try (Connection cn = DadosRH.ligacao()) {
            actual = DadosRH.gravarProcesso(cn, p, idUser, novo && evento == null ? "Processo instaurado|" + resumo(p.factos) : evento);
        } catch (Exception e) {
            UiRH.erro(this, e);
            carregar(); // repõe a fase e o estado gravados
            return false;
        }
        carregar();
        return true;
    }

    private static String resumo(String s) {
        String x = s.replaceAll("\\s+", " ").trim();
        return x.length() > 160 ? x.substring(0, 157) + "..." : x;
    }

    private boolean exigeProcesso() {
        if (actual == null) {
            if (!gravar(null)) {
                return false;
            }
        }
        if (!DadosRH.ABERTO.equals(actual.estado)) {
            UiRH.aviso(this, "Este processo está " + actual.estado.toLowerCase(UiRH.PT) + ".");
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------------- passos
    private void notaCulpa() {
        if (!exigeProcesso()) {
            return;
        }
        LocalDate hoje = LocalDate.now();
        if (dNotaCulpa.getDate() == null) {
            dNotaCulpa.setDate(UiRH.paraDate(hoje));
        }
        LocalDate nota = UiRH.data(dNotaCulpa.getDate());
        Map<LocalDate, String> fer;
        try (Connection cn = DadosRH.ligacao()) {
            fer = DadosRH.feriados(cn, nota.getYear());
            fer.putAll(DadosRH.feriados(cn, nota.getYear() + 1));
        } catch (Exception e) {
            fer = CalendarioRH.feriadosNacionais(nota.getYear());
        }
        // a entrevista não pode ser antes de passarem os dias úteis de antecedência
        LocalDate minimo = CalendarioRH.somarDiasUteis(nota.plusDays(1), Math.max(1, antecedencia), fer);
        if (dEntrevista.getDate() == null) {
            dEntrevista.setDate(UiRH.paraDate(minimo));
        } else if (UiRH.data(dEntrevista.getDate()).isBefore(minimo)) {
            UiRH.aviso(this, "A entrevista tem de ser pelo menos " + antecedencia + " dias úteis depois da nota de culpa: "
                    + "a primeira data possível é " + UiRH.data(minimo) + ".");
            dEntrevista.setDate(UiRH.paraDate(minimo));
            return;
        }
        LocalDateTime entrevista = LocalDateTime.of(UiRH.data(dEntrevista.getDate()), UiRH.hora(spHora));
        actual.fase = DadosRH.FASE_CONVOCADO;
        if (!gravar("Nota de culpa enviada|Entrevista marcada para " + entrevista.format(UiRH.DATA_HORA))) {
            return;
        }
        DadosRH.Funcionario f = UiRH.escolhido(cbFunc);
        String texto = UiRH.cabecalhoDocumento()
                + "\n\nExmo(a). Senhor(a)\n" + actual.funcionario + "\n"
                + (f != null && !f.funcao.isEmpty() ? f.funcao + "\n" : "")
                + "\n# NOTA DE CULPA E CONVOCATÓRIA\n"
                + "## Processo disciplinar " + actual.numero + "\n\n"
                + "Pela presente, comunica-se a V. Exa. que lhe foi instaurado processo disciplinar, com base nos factos seguintes:\n\n"
                + (actual.ocorrencia == null ? "" : "Data dos factos: " + UiRH.data(actual.ocorrencia) + "\n")
                + actual.factos + "\n\n"
                + (actual.testemunhas.isEmpty() ? "" : "Testemunhas: " + actual.testemunhas + "\n\n")
                + "Os factos descritos são susceptíveis de constituir infracção disciplinar. Nos termos da lei, fica V. Exa. convocado(a)"
                + " para a entrevista, a realizar no dia " + UiRH.data(entrevista.toLocalDate()) + ", às "
                + entrevista.toLocalTime().toString() + ", nas instalações da empresa, onde poderá apresentar a sua defesa,"
                + " oralmente ou por escrito, juntar documentos e indicar testemunhas, podendo fazer-se acompanhar por um representante.\n\n"
                + UiRH.localEData() + "\n\n\n"
                + "_______________________________\nA Entidade Empregadora\n\n\n"
                + "Recebi o original em ____/____/______\n\n_______________________________\nO(A) Trabalhador(a)";
        UiRH.exportarWord(this, "Nota de culpa " + actual.numero + " " + actual.funcionario, texto);
    }

    private void registarDefesa() {
        if (!exigeProcesso()) {
            return;
        }
        if (txtDefesa.getText().trim().isEmpty()) {
            UiRH.aviso(this, "Escreva a defesa do trabalhador (ou \"Não compareceu nem apresentou defesa\").");
            return;
        }
        actual.fase = DadosRH.FASE_DEFESA;
        gravar("Defesa registada|" + resumo(txtDefesa.getText()));
    }

    private void acta() {
        if (actual == null) {
            UiRH.aviso(this, "Grave primeiro o processo.");
            return;
        }
        String quando = actual.entrevista == null ? "____/____/______, às ____:____" : UiRH.data(actual.entrevista.toLocalDate())
                + ", às " + actual.entrevista.toLocalTime();
        String texto = UiRH.cabecalhoDocumento()
                + "\n# ACTA DA ENTREVISTA\n## Processo disciplinar " + actual.numero + "\n\n"
                + "No dia " + quando + ", nas instalações da empresa, realizou-se a entrevista do processo disciplinar movido a "
                + actual.funcionario + ".\n\n"
                + "Presentes: ______________________________________________\n\n"
                + "## Defesa do trabalhador\n"
                + (txtDefesa.getText().trim().isEmpty() ? "\n\n\n\n" : txtDefesa.getText().trim()) + "\n\n"
                + "Nada mais havendo a tratar, lavrou-se a presente acta, que vai assinada pelos presentes.\n\n\n"
                + "_______________________________\nPela Entidade Empregadora\n\n\n"
                + "_______________________________\nO(A) Trabalhador(a)";
        UiRH.exportarWord(this, "Acta entrevista " + actual.numero, texto);
    }

    private void decidir() {
        if (!exigeProcesso()) {
            return;
        }
        if (txtDecisao.getText().trim().isEmpty()) {
            UiRH.aviso(this, "Escreva a fundamentação da decisão.");
            return;
        }
        String sancao = String.valueOf(cbSancao.getSelectedItem());
        boolean temporaria = sancao.contains("temporária");
        if (temporaria && ((Integer) spDias.getValue()) <= 0) {
            UiRH.aviso(this, "Indique os dias da sanção temporária.");
            return;
        }
        if (!ComponentesWeb.confirmarOperacao(this, "Decidir o processo",
                actual.numero + " · " + actual.funcionario + "\nSanção: " + sancao + (temporaria ? " (" + spDias.getValue() + " dias)" : "")
                + "\nO processo fica concluído e deixa de se poder alterar.")) {
            return;
        }
        if (dDecisao.getDate() == null) {
            dDecisao.setDate(UiRH.paraDate(LocalDate.now()));
        }
        actual.fase = DadosRH.FASE_DECIDIDO;
        actual.estado = sancao.startsWith("Sem sanção") ? DadosRH.ARQUIVADO : DadosRH.CONCLUIDO;
        if (!gravar("Decisão|" + sancao + (temporaria ? " (" + spDias.getValue() + " dias)" : ""))) {
            return;
        }
        String texto = UiRH.cabecalhoDocumento()
                + "\n\nExmo(a). Senhor(a)\n" + actual.funcionario + "\n"
                + "\n# COMUNICAÇÃO DA DECISÃO DISCIPLINAR\n## Processo disciplinar " + actual.numero + "\n\n"
                + "Concluída a instrução do processo disciplinar acima referido, e ponderada a defesa apresentada,"
                + " comunica-se a V. Exa. a decisão seguinte:\n\n"
                + "## Fundamentação\n" + actual.decisao + "\n\n"
                + "## Decisão\n"
                + (sancao.startsWith("Sem sanção") ? "O processo é arquivado, sem aplicação de sanção."
                : "É aplicada a sanção de " + sancao.toLowerCase(UiRH.PT) + (temporaria ? ", por " + spDias.getValue() + " dias" : "") + ".")
                + "\n\n" + UiRH.localEData() + "\n\n\n"
                + "_______________________________\nA Entidade Empregadora\n\n\n"
                + "Recebi o original em ____/____/______\n\n_______________________________\nO(A) Trabalhador(a)";
        UiRH.exportarWord(this, "Decisao " + actual.numero + " " + actual.funcionario, texto);
    }

    private void arquivar() {
        if (actual == null || !DadosRH.ABERTO.equals(actual.estado)) {
            UiRH.aviso(this, "Seleccione um processo aberto.");
            return;
        }
        String motivo = javax.swing.JOptionPane.showInputDialog(this, "Motivo do arquivamento:", "Arquivar " + actual.numero,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (motivo == null || motivo.trim().isEmpty()) {
            return;
        }
        actual.estado = DadosRH.ARQUIVADO;
        actual.sancao = DadosRH.SANCOES[DadosRH.SANCOES.length - 1];
        cbSancao.setSelectedItem(actual.sancao);
        gravar("Arquivado|" + motivo.trim());
    }

    private void processoCompleto() {
        if (actual == null) {
            UiRH.aviso(this, "Seleccione um processo.");
            return;
        }
        StringBuilder t = new StringBuilder(UiRH.cabecalhoDocumento());
        t.append("\n# PROCESSO DISCIPLINAR ").append(actual.numero).append("\n\n")
                .append("Trabalhador: ").append(actual.funcionario)
                .append(actual.numeroFuncionario.isEmpty() ? "" : " (nº " + actual.numeroFuncionario + ")").append("\n")
                .append(actual.funcao.isEmpty() ? "" : "Função: " + actual.funcao + "\n")
                .append("Aberto em: ").append(actual.abertura.format(UiRH.DATA_HORA)).append("\n")
                .append("Situação: ").append(actual.fase).append(" · ").append(actual.estado).append("\n\n")
                .append("## Factos\n")
                .append(actual.ocorrencia == null ? "" : "Data dos factos: " + UiRH.data(actual.ocorrencia) + "\n")
                .append(actual.conhecimento == null ? "" : "Conhecimento pela empresa: " + UiRH.data(actual.conhecimento) + "\n")
                .append(actual.factos).append("\n")
                .append(actual.testemunhas.isEmpty() ? "" : "Testemunhas: " + actual.testemunhas + "\n").append("\n");
        if (actual.notaCulpa != null || actual.entrevista != null) {
            t.append("## Nota de culpa\n")
                    .append(actual.notaCulpa == null ? "" : "Enviada em " + UiRH.data(actual.notaCulpa) + "\n")
                    .append(actual.entrevista == null ? "" : "Entrevista: " + actual.entrevista.format(UiRH.DATA_HORA) + "\n").append("\n");
        }
        if (!actual.defesa.isEmpty()) {
            t.append("## Defesa\n").append(actual.defesa).append("\n\n");
        }
        if (!actual.decisao.isEmpty() || !actual.sancao.isEmpty()) {
            t.append("## Decisão\n")
                    .append(actual.decisaoData == null ? "" : "Data: " + UiRH.data(actual.decisaoData) + "\n")
                    .append(actual.sancao.isEmpty() ? "" : "Sanção: " + actual.sancao
                            + (actual.diasSancao == null ? "" : " (" + actual.diasSancao + " dias)") + "\n")
                    .append(actual.decisao).append("\n\n");
        }
        t.append("## Linha do tempo\n");
        for (int i = 0; i < mEventos.getRowCount(); i++) {
            t.append(mEventos.getValueAt(i, 0)).append(" — ").append(mEventos.getValueAt(i, 1));
            String d = String.valueOf(mEventos.getValueAt(i, 2));
            t.append(d.isEmpty() ? "" : ": " + d);
            String u = String.valueOf(mEventos.getValueAt(i, 3));
            t.append(u.isEmpty() ? "" : " (" + u + ")").append("\n");
        }
        UiRH.exportarWord(this, "Processo " + actual.numero + " " + actual.funcionario, t.toString());
    }
}
