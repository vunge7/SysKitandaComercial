package rh.web;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;

/**
 * Funcionários: uma só lista, com pesquisa instantânea e filtros (como as
 * listas do Primavera). Daqui abre-se a ficha completa, o histórico, a
 * declaração de serviço (Word) e a exportação para Excel.
 */
public class FuncionariosWeb extends JFrame {

    private final int idUser, idEmpresa;
    private final DefaultTableModel modelo = UiRH.modelo("Nº", "Nome", "Função", "Departamento", "Telefone",
            "Admissão", "Contrato", "Fim do contrato", "Situação");
    private final JTable tabela = UiRH.tabela(modelo, 70, 230, 150, 150, 110, 95, 110, 110, 170);
    private final JTextField pesquisa = new JTextField();
    private final JComboBox<String> departamento = new JComboBox<>();
    private final JToggleButton rbActivos = new JToggleButton("Activos", true), rbInactivos = new JToggleButton("Inactivos"),
            rbTodos = new JToggleButton("Todos");
    private final ComponentesWeb.CartaoKpi kAtivos = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "Activos", ComponentesWeb.AZUL_VIVO).compacto();
    private final ComponentesWeb.CartaoKpi kContratos = new ComponentesWeb.CartaoKpi("!", "Contratos a terminar (30 dias)", ComponentesWeb.LARANJA).compacto();
    private final ComponentesWeb.CartaoKpi kDocs = new ComponentesWeb.CartaoKpi("ID", "Documentos a caducar (60 dias)", ComponentesWeb.VERMELHO).compacto();
    private final ComponentesWeb.CartaoKpi kSexo = new ComponentesWeb.CartaoKpi("♀", "Homens / Mulheres", ComponentesWeb.ROXO).compacto();
    private List<DadosRH.Funcionario> todos = new ArrayList<>();
    private final List<DadosRH.Funcionario> visiveis = new ArrayList<>();

    public FuncionariosWeb(int idUser, int idEmpresa) {
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        FormularioWeb.campoTexto(pesquisa);
        pesquisa.setToolTipText("Nome, número, função, departamento ou telefone");
        FormularioWeb.combo(departamento);
        ButtonGroup g = new ButtonGroup();
        g.add(rbActivos);
        g.add(rbInactivos);
        g.add(rbTodos);
        for (JToggleButton b : new JToggleButton[]{rbActivos, rbInactivos, rbTodos}) {
            b.addActionListener(e -> filtrar());
        }
        pesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
        departamento.addActionListener(e -> filtrar());
        tabela.getColumnModel().getColumn(8).setCellRenderer(new UiRH.RendererEstado());
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirFicha();
                }
            }
        });

        JPanel filtros = FormularioWeb.grelha(220, 3,
                ComponentesWeb.campo("Pesquisar", pesquisa),
                ComponentesWeb.campo("Departamento", departamento),
                ComponentesWeb.campo("Mostrar", FormularioWeb.pilulas(rbActivos, rbInactivos, rbTodos)));
        JPanel kpis = FormularioWeb.grelha(170, 4, kAtivos, kContratos, kDocs, kSexo);
        JPanel lista = ComponentesWeb.transparente(new BorderLayout());
        lista.add(UiRH.rolagem(tabela), BorderLayout.CENTER);
        JPanel conteudo = UiRH.bloco(FormularioWeb.pilha(10, kpis, FormularioWeb.cartao("Filtros", ComponentesWeb.AZUL_VIVO, filtros)),
                FormularioWeb.cartao("Funcionários", UiRH.COR_RH, lista));

        JButton novo = UiRH.botao("Novo funcionário", ComponentesWeb.Variante.SUCESSO, "Abrir a ficha em branco", () -> UiRH.abrirFicha(idUser, idEmpresa, null));
        JButton ficha = UiRH.botao("Abrir ficha", ComponentesWeb.Variante.PRIMARIO, "Ficha completa (duplo clique na linha)", this::abrirFicha);
        JButton historico = UiRH.botao("Histórico", ComponentesWeb.Variante.SECUNDARIO, "Tudo o que aconteceu com o funcionário", this::historico);
        JButton declaracao = UiRH.botao("Declaração de serviço (Word)", ComponentesWeb.Variante.SECUNDARIO,
                "Declaração de que o funcionário trabalha na empresa, pronta a editar no Word", this::declaracao);
        JButton excel = UiRH.botao("Excel", ComponentesWeb.Variante.SECUNDARIO, "Exportar a lista (como está filtrada)",
                () -> UiRH.exportarExcel(this, tabela, "Funcionários"));
        JButton actualizar = UiRH.botao("Actualizar", ComponentesWeb.Variante.SECUNDARIO, null, this::carregar);
        UiRH.janela(this, "Funcionários", "Pesquise, filtre e abra a ficha com um duplo clique", null, conteudo,
                FormularioWeb.dica("Duplo clique abre a ficha completa"), novo, ficha, historico, declaracao, excel, actualizar, UiRH.sair(this));
        carregar();
    }

    private void carregar() {
        try (Connection cn = DadosRH.ligacao()) {
            todos = DadosRH.funcionarios(cn, false);
        } catch (Exception e) {
            UiRH.erro(this, e);
            return;
        }
        Object escolhido = departamento.getSelectedItem();
        java.util.TreeSet<String> deps = new java.util.TreeSet<>();
        for (DadosRH.Funcionario f : todos) {
            if (!f.departamento.isEmpty()) {
                deps.add(f.departamento);
            }
        }
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Todos os departamentos");
        opcoes.addAll(deps);
        departamento.setModel(new javax.swing.DefaultComboBoxModel<>(opcoes.toArray(new String[0])));
        if (escolhido != null) {
            departamento.setSelectedItem(escolhido);
        }
        LocalDate hoje = LocalDate.now();
        int activos = 0, contratos = 0, docs = 0, h = 0, m = 0;
        for (DadosRH.Funcionario f : todos) {
            if (!f.activo) {
                continue;
            }
            activos++;
            if (f.fimContrato != null && "Determinado".equalsIgnoreCase(f.tipoContrato) && ChronoUnit.DAYS.between(hoje, f.fimContrato) <= 30) {
                contratos++;
            }
            if (f.validadeDoc != null && ChronoUnit.DAYS.between(hoje, f.validadeDoc) <= 60) {
                docs++;
            }
            if ("Masculino".equalsIgnoreCase(f.sexo)) {
                h++;
            } else {
                m++;
            }
        }
        kAtivos.setValor(String.valueOf(activos), (todos.size() - activos) + " inactivos");
        kContratos.setValor(String.valueOf(contratos), contratos == 0 ? "nenhum" : "ver a coluna Situação");
        kDocs.setValor(String.valueOf(docs), docs == 0 ? "em dia" : "renovar o BI / passaporte");
        kSexo.setValor(h + " / " + m, "");
        filtrar();
    }

    private void filtrar() {
        String q = UiRH.normalizar(pesquisa.getText());
        String dep = departamento.getSelectedIndex() <= 0 ? null : (String) departamento.getSelectedItem();
        modelo.setRowCount(0);
        visiveis.clear();
        LocalDate hoje = LocalDate.now();
        for (DadosRH.Funcionario f : todos) {
            if (rbActivos.isSelected() && !f.activo || rbInactivos.isSelected() && f.activo) {
                continue;
            }
            if (dep != null && !dep.equals(f.departamento)) {
                continue;
            }
            if (!q.isEmpty() && !UiRH.normalizar(f.numero + " " + f.nome + " " + f.funcao + " " + f.departamento + " " + f.telefone).contains(q)) {
                continue;
            }
            visiveis.add(f);
            modelo.addRow(new Object[]{f.numero, f.nome, f.funcao, f.departamento, f.telefone, UiRH.data(f.admissao),
                f.tipoContrato, UiRH.data(f.fimContrato), situacao(f, hoje)});
        }
    }

    private static String situacao(DadosRH.Funcionario f, LocalDate hoje) {
        if (!f.activo) {
            return "Inactivo";
        }
        if (f.fimContrato != null && "Determinado".equalsIgnoreCase(f.tipoContrato)) {
            long d = ChronoUnit.DAYS.between(hoje, f.fimContrato);
            if (d < 0) {
                return "Contrato terminou";
            }
            if (d <= 30) {
                return "Contrato a terminar (" + d + " d)";
            }
        }
        if (f.validadeDoc != null && f.validadeDoc.isBefore(hoje)) {
            return "BI caducado";
        }
        return "Activo";
    }

    private DadosRH.Funcionario seleccionado() {
        int l = UiRH.linha(tabela);
        if (l < 0) {
            UiRH.aviso(this, "Seleccione um funcionário na lista.");
            return null;
        }
        return visiveis.get(l);
    }

    private void abrirFicha() {
        DadosRH.Funcionario f = seleccionado();
        if (f != null) {
            UiRH.abrirFicha(idUser, idEmpresa, f.numero);
        }
    }

    private void historico() {
        DadosRH.Funcionario f = seleccionado();
        if (f == null) {
            return;
        }
        DefaultTableModel m = UiRH.modelo("Data", "Acontecimento", "Detalhe");
        try (Connection cn = DadosRH.ligacao()) {
            for (String[] h : DadosRH.historico(cn, f)) {
                m.addRow(new Object[]{UiRH.data(LocalDate.parse(h[0])), h[1], h[2]});
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
            return;
        }
        JTable t = UiRH.tabela(m, 100, 190, 420);
        t.getColumnModel().getColumn(1).setCellRenderer(new UiRH.RendererEstado());
        JDialog d = new JDialog(this, "Histórico · " + f.nome, true);
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(ComponentesWeb.FUNDO_PAGINA);
        p.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        String antiguidade = f.admissao == null ? "" : " · na empresa há " + anos(Period.between(f.admissao, LocalDate.now()));
        p.add(UiRH.rotuloSecao(f.rotulo() + " · " + f.funcao + antiguidade), BorderLayout.NORTH);
        p.add(UiRH.rolagem(t), BorderLayout.CENTER);
        JButton excel = UiRH.botao("Excel", ComponentesWeb.Variante.SECUNDARIO, null, () -> UiRH.exportarExcel(d, t, "Histórico " + f.nome));
        JButton fechar = UiRH.botao("Fechar", ComponentesWeb.Variante.PERIGO, null, d::dispose);
        p.add(UiRH.linha(excel, fechar), BorderLayout.SOUTH);
        d.setContentPane(p);
        d.setSize(820, 520);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    static String anos(Period p) {
        String s = p.getYears() > 0 ? p.getYears() + (p.getYears() == 1 ? " ano" : " anos") : "";
        if (p.getMonths() > 0) {
            s += (s.isEmpty() ? "" : " e ") + p.getMonths() + (p.getMonths() == 1 ? " mês" : " meses");
        }
        return s.isEmpty() ? "menos de um mês" : s;
    }

    /** Declaração de serviço: texto pronto, aberto no Word para editar e imprimir. */
    private void declaracao() {
        DadosRH.Funcionario f = seleccionado();
        if (f == null) {
            return;
        }
        String empresa = "", nif = "", morada = "";
        try (Connection cn = DadosRH.ligacao(); java.sql.Statement st = cn.createStatement();
                java.sql.ResultSet rs = st.executeQuery("SELECT nome, nif, enderecos FROM tb_dados_instituicao LIMIT 1")) {
            if (rs.next()) {
                empresa = DadosRH.t(rs.getString(1));
                nif = DadosRH.t(rs.getString(2));
                morada = DadosRH.t(rs.getString(3));
            }
        } catch (Exception e) {
            UiRH.erro(this, e);
            return;
        }
        String hoje = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", UiRH.PT));
        String texto = "## " + empresa + "\n"
                + (nif.isEmpty() ? "" : "NIF: " + nif + "\n") + (morada.isEmpty() ? "" : morada + "\n")
                + "\n\n# DECLARAÇÃO DE SERVIÇO\n\n"
                + "Para os devidos efeitos, declara-se que " + f.nome + ", portador(a) do documento de identificação"
                + " em vigor, é trabalhador(a) desta empresa desde " + (f.admissao == null ? "____/____/______" : UiRH.data(f.admissao))
                + ", exercendo a função de " + (f.funcao.isEmpty() ? "________________" : f.funcao)
                + (f.departamento.isEmpty() ? "" : " no departamento de " + f.departamento)
                + ", com contrato por tempo " + (f.tipoContrato.isEmpty() ? "________" : f.tipoContrato.toLowerCase(UiRH.PT)) + ".\n\n"
                + "Por ser verdade e nos ter sido solicitada, passa-se a presente declaração, que vai assinada e autenticada"
                + " com o carimbo em uso nesta empresa.\n\n\n"
                + (morada.isEmpty() ? "" : morada.split(",")[0].trim() + ", ") + hoje + "\n\n\n\n"
                + "_______________________________\n"
                + "Os Recursos Humanos";
        UiRH.exportarWord(this, "Declaracao de servico " + f.nome, texto);
    }
}
