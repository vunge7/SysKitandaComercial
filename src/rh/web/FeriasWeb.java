package rh.web;

import java.awt.Dimension;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;

/**
 * Férias: marcar (ou registar o pedido do funcionário), aprovar ou recusar,
 * e ver o saldo de cada um no ano. Os dias são dias úteis (sem fins de
 * semana nem feriados). Grava em pedido_feria, que o salário já conhece.
 */
public class FeriasWeb extends JFrame {

    private final int idUser;
    private final JComboBox<Integer> cbAno = new JComboBox<>();
    private final List<DadosRH.Funcionario> funcionarios = new ArrayList<>();
    private final List<DadosRH.Ferias> ferias = new ArrayList<>();
    private final Map<Integer, Integer> direitos = new HashMap<>();
    private Map<LocalDate, String> feriados = new LinkedHashMap<>();

    // marcação
    private final JComboBox<DadosRH.Funcionario> cbFunc = UiRH.seletor(funcionarios);
    private final com.toedter.calendar.JDateChooser dInicio = UiRH.escolhaData(null);
    private final com.toedter.calendar.JDateChooser dFim = UiRH.escolhaData(null);
    private final JToggleButton rbAprovado = new JToggleButton("Marcar (aprovadas)", true), rbPedido = new JToggleButton("Pedido do funcionário");
    private final JTextField txtObs = new JTextField();
    private final javax.swing.JLabel lbResumo = FormularioWeb.dica(" ");
    private final javax.swing.JLabel lbSaldo = FormularioWeb.dica(" ");
    private DadosRH.Ferias emEdicao;
    private final JButton gravar = UiRH.botao("Gravar férias", ComponentesWeb.Variante.SUCESSO, null, this::gravar);
    private final JButton limpar = UiRH.botao("Limpar", ComponentesWeb.Variante.SECUNDARIO, "Começar uma marcação nova", this::limpar);

    // listas
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Todas", "Por aprovar", "Aprovadas", "Recusadas / canceladas"});
    private final JTextField filtro = new JTextField();
    private final DefaultTableModel mFerias = UiRH.modelo("Nº", "Funcionário", "Departamento", "Início", "Fim", "Dias úteis", "Situação", "Observação");
    private final JTable tFerias = UiRH.tabela(mFerias, 60, 220, 140, 95, 95, 80, 110, 220);
    private final DefaultTableModel mSaldos = UiRH.modelo("Nº", "Funcionário", "Departamento", "Admissão", "Direito", "Marcadas", "Gozadas", "Saldo");
    private final JTable tSaldos = UiRH.tabela(mSaldos, 60, 230, 150, 95, 75, 80, 75, 75);
    private final JTabbedPane abas = new JTabbedPane();
    private final List<DadosRH.Ferias> visiveis = new ArrayList<>();
    private final List<DadosRH.Funcionario> visiveisSaldo = new ArrayList<>();

    private final ComponentesWeb.CartaoKpi kGozo = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "De férias hoje", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kPedidos = new ComponentesWeb.CartaoKpi("?", "Pedidos por aprovar", ComponentesWeb.LARANJA).compacto();
    private final ComponentesWeb.CartaoKpi kMarcadas = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.VISTO, "Dias marcados no ano", ComponentesWeb.VERDE).compacto();
    private final ComponentesWeb.CartaoKpi kPorMarcar = new ComponentesWeb.CartaoKpi("!", "Sem férias marcadas", ComponentesWeb.VERMELHO).compacto();

    public FeriasWeb(int idUser) {
        this.idUser = idUser;
        int ano = LocalDate.now().getYear();
        for (int a = ano - 2; a <= ano + 1; a++) {
            cbAno.addItem(a);
        }
        cbAno.setSelectedItem(ano);
        FormularioWeb.combo(cbAno, cbEstado);
        FormularioWeb.campoTexto(txtObs, filtro);
        ButtonGroup g = new ButtonGroup();
        g.add(rbAprovado);
        g.add(rbPedido);
        dInicio.addPropertyChangeListener("date", e -> {
            if (dInicio.getDate() != null && (dFim.getDate() == null || dFim.getDate().before(dInicio.getDate()))) {
                dFim.setDate(dInicio.getDate());
            }
            resumo();
        });
        dFim.addPropertyChangeListener("date", e -> resumo());
        cbAno.addActionListener(e -> carregar());
        cbEstado.addActionListener(e -> mostrar());
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
        tFerias.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tFerias.getColumnModel().getColumn(6).setCellRenderer(new UiRH.RendererEstado());
        tFerias.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editar();
                }
            }
        });
        tSaldos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int l = UiRH.linha(tSaldos);
                    if (l >= 0) {
                        limpar();
                        cbFunc.setSelectedItem(visiveisSaldo.get(l));
                        resumo();
                    }
                }
            }
        });

        JPanel formulario = FormularioWeb.pilha(9,
                ComponentesWeb.campo("Funcionário (escreva para procurar)", cbFunc),
                FormularioWeb.grelha(120, 2, ComponentesWeb.campo("Primeiro dia", dInicio), ComponentesWeb.campo("Último dia", dFim)),
                ComponentesWeb.campo("Situação", FormularioWeb.pilulas(rbAprovado, rbPedido)),
                ComponentesWeb.campo("Observação (opcional)", txtObs),
                lbResumo, lbSaldo,
                UiRH.linha(gravar, limpar));
        JPanel esquerda = FormularioWeb.cartao("Marcar férias", ComponentesWeb.VERDE, formulario);
        cbFunc.addActionListener(e -> resumo());

        JPanel filtros = FormularioWeb.grelha(170, 3, ComponentesWeb.campo("Ano", cbAno), ComponentesWeb.campo("Mostrar", cbEstado),
                ComponentesWeb.campo("Pesquisar", filtro));
        abas.addTab("Marcações e pedidos", UiRH.rolagem(tFerias));
        abas.addTab("Saldo por funcionário", UiRH.rolagem(tSaldos));
        abas.setFont(util.ui.EstiloUI.FONTE_NEGRITO.deriveFont(13f));
        JPanel lista = UiRH.bloco(FormularioWeb.pilha(8, FormularioWeb.grelha(150, 4, kGozo, kPedidos, kMarcadas, kPorMarcar), filtros), abas);
        JPanel direita = FormularioWeb.cartao("Férias do ano", UiRH.COR_RH, lista);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(new double[]{0.3, 0.7}, new int[]{300, 560}, 10));
        colunas.add(esquerda);
        colunas.add(direita);

        JButton aprovar = UiRH.botao("Aprovar", ComponentesWeb.Variante.SUCESSO, "Aprovar os pedidos seleccionados", () -> mudarEstado(DadosRH.APROVADO));
        JButton recusar = UiRH.botao("Recusar", ComponentesWeb.Variante.AVISO, "Recusar os pedidos seleccionados", () -> mudarEstado(DadosRH.RECUSADO));
        JButton cancelar = UiRH.botao("Cancelar férias", ComponentesWeb.Variante.SECUNDARIO, "Anular férias marcadas (ficam no histórico)",
                () -> mudarEstado(DadosRH.CANCELADO));
        JButton eliminar = UiRH.botao("Eliminar", ComponentesWeb.Variante.PERIGO, "Apagar um registo errado", this::eliminar);
        JButton direito = UiRH.botao("Alterar direito", ComponentesWeb.Variante.SECUNDARIO,
                "Dias a que o funcionário seleccionado (no saldo) tem direito neste ano", this::alterarDireito);
        JButton feriadosBt = UiRH.botao("Feriados", ComponentesWeb.Variante.SECUNDARIO, "Feriados, pontes e tolerâncias do ano", () -> {
            TabelasRH.feriados(this);
            carregar();
        });
        JButton mapa = UiRH.botao("Mapa (Excel)", ComponentesWeb.Variante.SECUNDARIO, "Exporta a lista que está à vista",
                () -> UiRH.exportarExcel(this, abas.getSelectedIndex() == 0 ? tFerias : tSaldos,
                        (abas.getSelectedIndex() == 0 ? "Mapa de ferias " : "Saldo de ferias ") + cbAno.getSelectedItem()));
        UiRH.janela(this, "Férias", "Marque, aprove e acompanhe o saldo de cada funcionário", null, colunas,
                FormularioWeb.dica("Duplo clique numa marcação para a alterar"), aprovar, recusar, cancelar, eliminar, direito, feriadosBt, mapa,
                UiRH.sair(this));
        carregar();
    }

    private int ano() {
        return (Integer) cbAno.getSelectedItem();
    }

    private void carregar() {
        Object escolhido = cbFunc.getSelectedItem();
        ferias.clear();
        direitos.clear();
        try (Connection cn = DadosRH.ligacao()) {
            funcionarios.clear();
            funcionarios.addAll(DadosRH.funcionarios(cn, true));
            ferias.addAll(DadosRH.ferias(cn, ano(), null));
            direitos.putAll(DadosRH.direitos(cn, funcionarios, ano()));
            feriados = new LinkedHashMap<>(DadosRH.feriados(cn, ano()));
            feriados.putAll(DadosRH.feriados(cn, ano() + 1));
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        cbFunc.setModel(new javax.swing.DefaultComboBoxModel<>(funcionarios.toArray(new DadosRH.Funcionario[0])));
        cbFunc.setSelectedItem(null);
        if (escolhido instanceof DadosRH.Funcionario) {
            for (DadosRH.Funcionario f : funcionarios) {
                if (f.id == ((DadosRH.Funcionario) escolhido).id) {
                    cbFunc.setSelectedItem(f);
                }
            }
        }
        mostrar();
        resumo();
    }

    /** Dias já marcados (contam para o saldo) por funcionário, sem a marcação em edição. */
    private int marcados(int idFuncionario, DadosRH.Ferias excepto) {
        int n = 0;
        for (DadosRH.Ferias f : ferias) {
            if (f.idFuncionario == idFuncionario && f.conta() && f != excepto) {
                n += f.dias;
            }
        }
        return n;
    }

    private List<LocalDate> diasUteis() {
        LocalDate de = UiRH.data(dInicio.getDate()), ate = UiRH.data(dFim.getDate());
        if (de == null || ate == null || ate.isBefore(de)) {
            return new ArrayList<>();
        }
        return CalendarioRH.diasUteis(de, ate, feriados);
    }

    private void resumo() {
        DadosRH.Funcionario f = UiRH.escolhido(cbFunc);
        int dias = diasUteis().size();
        lbResumo.setText(dInicio.getDate() == null ? "Escolha o primeiro e o último dia"
                : dias + (dias == 1 ? " dia útil" : " dias úteis") + " (sem fins de semana nem feriados)");
        if (f == null) {
            lbSaldo.setText(" ");
            return;
        }
        int direito = direitos.getOrDefault(f.id, 22), usados = marcados(f.id, emEdicao);
        int saldo = direito - usados - dias;
        lbSaldo.setText("Direito " + direito + " · já marcados " + usados + " · ficam " + saldo);
        lbSaldo.setForeground(saldo < 0 ? ComponentesWeb.VERMELHO : ComponentesWeb.TEXTO_SUAVE);
    }

    private void mostrar() {
        String q = UiRH.normalizar(filtro.getText());
        int estado = cbEstado.getSelectedIndex();
        LocalDate hoje = LocalDate.now();
        mFerias.setRowCount(0);
        visiveis.clear();
        int emGozo = 0, pedidos = 0, diasMarcados = 0;
        java.util.Set<Integer> comFerias = new java.util.HashSet<>();
        for (DadosRH.Ferias f : ferias) {
            if (DadosRH.PEDIDO.equals(f.estado)) {
                pedidos++;
            }
            if (f.conta()) {
                diasMarcados += f.dias;
                comFerias.add(f.idFuncionario);
            }
            if ("Em gozo".equals(f.situacao())) {
                emGozo++;
            }
            if (estado == 1 && !DadosRH.PEDIDO.equals(f.estado) || estado == 2 && !DadosRH.APROVADO.equals(f.estado)
                    || estado == 3 && f.conta()) {
                continue;
            }
            if (!q.isEmpty() && !UiRH.normalizar(f.numero + " " + f.funcionario + " " + f.departamento).contains(q)) {
                continue;
            }
            visiveis.add(f);
            mFerias.addRow(new Object[]{f.numero, f.funcionario, f.departamento, UiRH.data(f.inicio), UiRH.data(f.fim), f.dias,
                f.situacao(), f.observacao});
        }
        mSaldos.setRowCount(0);
        visiveisSaldo.clear();
        int semFerias = 0;
        for (DadosRH.Funcionario f : funcionarios) {
            int direito = direitos.getOrDefault(f.id, 0);
            if (!comFerias.contains(f.id) && direito > 0) {
                semFerias++;
            }
            if (!q.isEmpty() && !UiRH.normalizar(f.rotulo() + " " + f.departamento).contains(q)) {
                continue;
            }
            int marcadas = 0, gozadas = 0;
            for (DadosRH.Ferias x : ferias) {
                if (x.idFuncionario != f.id || !x.conta()) {
                    continue;
                }
                marcadas += x.dias;
                if (DadosRH.APROVADO.equals(x.estado) && x.inicio != null) {
                    LocalDate ate = x.fim.isBefore(hoje) ? x.fim : hoje.minusDays(1);
                    if (!ate.isBefore(x.inicio)) {
                        gozadas += CalendarioRH.diasUteis(x.inicio, ate, feriados).size();
                    }
                }
            }
            visiveisSaldo.add(f);
            mSaldos.addRow(new Object[]{f.numero, f.nome, f.departamento, UiRH.data(f.admissao), direito, marcadas, Math.min(gozadas, marcadas),
                direito - marcadas});
        }
        kGozo.setValor(String.valueOf(emGozo), emGozo == 0 ? "todos ao serviço" : "ausentes hoje");
        kPedidos.setValor(String.valueOf(pedidos), pedidos == 0 ? "nada pendente" : "seleccione e aprove");
        kMarcadas.setValor(String.valueOf(diasMarcados), "dias úteis em " + ano());
        kPorMarcar.setValor(String.valueOf(semFerias), semFerias == 0 ? "todos com férias" : "funcionários");
    }

    private void limpar() {
        emEdicao = null;
        cbFunc.setEnabled(true);
        cbFunc.setSelectedItem(null);
        dInicio.setDate(null);
        dFim.setDate(null);
        txtObs.setText("");
        rbAprovado.setSelected(true);
        gravar.setText("Gravar férias");
        resumo();
    }

    private void editar() {
        int l = UiRH.linha(tFerias);
        if (l < 0) {
            return;
        }
        DadosRH.Ferias f = visiveis.get(l);
        emEdicao = f;
        for (DadosRH.Funcionario x : funcionarios) {
            if (x.id == f.idFuncionario) {
                cbFunc.setSelectedItem(x);
            }
        }
        cbFunc.setEnabled(false);
        dInicio.setDate(UiRH.paraDate(f.inicio));
        dFim.setDate(UiRH.paraDate(f.fim));
        txtObs.setText(f.observacao);
        (DadosRH.PEDIDO.equals(f.estado) ? rbPedido : rbAprovado).setSelected(true);
        gravar.setText("Gravar alteração");
        resumo();
    }

    private void gravar() {
        DadosRH.Funcionario func = UiRH.escolhido(cbFunc);
        LocalDate de = UiRH.data(dInicio.getDate()), ate = UiRH.data(dFim.getDate());
        if (func == null) {
            UiRH.aviso(this, "Escolha o funcionário.");
            return;
        }
        if (de == null || ate == null || ate.isBefore(de)) {
            UiRH.aviso(this, "Indique o primeiro e o último dia de férias.");
            return;
        }
        int dias = diasUteis().size();
        if (dias == 0) {
            UiRH.aviso(this, "O período escolhido não tem dias úteis.");
            return;
        }
        for (DadosRH.Ferias x : ferias) {
            if (x != emEdicao && x.idFuncionario == func.id && x.conta() && !x.fim.isBefore(de) && !x.inicio.isAfter(ate)) {
                UiRH.aviso(this, func.nome + " já tem férias de " + UiRH.data(x.inicio) + " a " + UiRH.data(x.fim)
                        + " (" + x.situacao().toLowerCase(UiRH.PT) + ") que se sobrepõem a este período.");
                return;
            }
        }
        int saldo = direitos.getOrDefault(func.id, 22) - marcados(func.id, emEdicao) - dias;
        if (saldo < 0 && JOptionPane.showConfirmDialog(this, "Com esta marcação " + func.nome + " fica com " + (-saldo)
                + (saldo == -1 ? " dia" : " dias") + " a mais do que o direito do ano.\nGravar mesmo assim?", "Férias",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        DadosRH.Ferias f = emEdicao != null ? emEdicao : new DadosRH.Ferias();
        f.idFuncionario = func.id;
        f.inicio = de;
        f.fim = ate;
        f.dias = dias;
        f.ano = ano();
        f.observacao = txtObs.getText().trim();
        f.estado = rbPedido.isSelected() ? DadosRH.PEDIDO : DadosRH.APROVADO;
        try (Connection cn = DadosRH.ligacao()) {
            int id = DadosRH.gravarFerias(cn, f, idUser);
            if (DadosRH.APROVADO.equals(f.estado) && (emEdicao == null || !DadosRH.APROVADO.equals(emEdicao.estado))) {
                DadosRH.mudarEstadoFerias(cn, id, DadosRH.APROVADO, idUser);
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
            return;
        }
        limpar();
        carregar();
    }

    private List<DadosRH.Ferias> seleccionadas() {
        List<DadosRH.Ferias> l = new ArrayList<>();
        if (abas.getSelectedIndex() == 0) {
            for (int i : UiRH.linhas(tFerias)) {
                l.add(visiveis.get(i));
            }
        }
        if (l.isEmpty()) {
            abas.setSelectedIndex(0);
            UiRH.aviso(this, "Seleccione uma ou mais marcações na lista \"Marcações e pedidos\".");
        }
        return l;
    }

    private void mudarEstado(String estado) {
        List<DadosRH.Ferias> sel = seleccionadas();
        if (sel.isEmpty()) {
            return;
        }
        if (!DadosRH.CANCELADO.equals(estado)) {
            sel.removeIf(f -> !DadosRH.PEDIDO.equals(f.estado));
            if (sel.isEmpty()) {
                UiRH.aviso(this, "Só os pedidos (ainda por aprovar) podem ser aprovados ou recusados.");
                return;
            }
        } else if (!ComponentesWeb.confirmarOperacao(this, "Cancelar férias",
                "Cancelar " + sel.size() + (sel.size() == 1 ? " marcação" : " marcações") + ". Os dias voltam ao saldo.")) {
            return;
        }
        try (Connection cn = DadosRH.ligacao()) {
            for (DadosRH.Ferias f : sel) {
                DadosRH.mudarEstadoFerias(cn, f.id, estado, idUser);
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        carregar();
    }

    private void eliminar() {
        List<DadosRH.Ferias> sel = seleccionadas();
        if (sel.isEmpty() || !ComponentesWeb.confirmarOperacao(this, "Eliminar férias",
                "Apagar " + sel.size() + (sel.size() == 1 ? " registo" : " registos") + " (só para registos errados; para anular use Cancelar).")) {
            return;
        }
        try (Connection cn = DadosRH.ligacao()) {
            for (DadosRH.Ferias f : sel) {
                DadosRH.eliminarFerias(cn, f.id);
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        if (sel.contains(emEdicao)) {
            limpar();
        }
        carregar();
    }

    private void alterarDireito() {
        int l = abas.getSelectedIndex() == 1 ? UiRH.linha(tSaldos) : -1;
        if (l < 0) {
            abas.setSelectedIndex(1);
            UiRH.aviso(this, "Seleccione o funcionário na lista \"Saldo por funcionário\".");
            return;
        }
        DadosRH.Funcionario f = visiveisSaldo.get(l);
        JSpinner sp = new JSpinner(new SpinnerNumberModel((int) direitos.getOrDefault(f.id, 22), 0, 60, 1));
        FormularioWeb.spinner(sp);
        JPanel p = FormularioWeb.pilha(8, ComponentesWeb.campo("Dias úteis de férias em " + ano(), sp),
                FormularioWeb.dica("Ex.: dias que transitaram do ano anterior ou acordo especial."));
        p.setPreferredSize(new Dimension(360, 100));
        if (JOptionPane.showConfirmDialog(this, p, "Direito a férias · " + f.nome, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        try (Connection cn = DadosRH.ligacao()) {
            DadosRH.gravarDireito(cn, f.id, ano(), (Integer) sp.getValue());
        } catch (Exception e) {
            UiRH.erro(this, e);
        }
        carregar();
    }
}
