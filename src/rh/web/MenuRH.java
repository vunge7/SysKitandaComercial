package rh.web;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;
import util.ui.MenuWeb;
import util.ui.PaginaMenuWeb;

/**
 * Menu do RH com o aspecto Kitanda (barra lateral + página): indicadores do
 * dia, alertas (contratos, documentos, férias, prazos disciplinares) e os
 * cartões dos ecrãs novos. Os botões antigos do formulário entram na barra
 * lateral com os seus eventos, tal como estão.
 */
public final class MenuRH {

    private MenuRH() {
    }

    /** Botões do formulário antigo (podem vir a null). */
    public static final class Antigos {

        public JButton ficha, recibos, outras, mapas, tabelas, definicoes, usuario, empresa, configuracoes, sair;
    }

    public static void montar(JFrame janela, int idUser, int idEmpresa, JLabel lbNome, String perfil, Antigos a) {
        PaginaMenuWeb pagina = new PaginaMenuWeb("Recursos Humanos",
                "Pessoas, assiduidade, férias e disciplina: o que precisa de atenção aparece primeiro.", lbNome, perfil);
        pagina.simboloMarca(MenuWeb.Simbolo.PESSOAS);

        JButton funcionarios = abre("Funcionários", () -> new FuncionariosWeb(idUser, idEmpresa), FuncionariosWeb.class);
        JButton faltas = abre("Faltas e Assiduidade", () -> new FaltasWeb(idUser), FaltasWeb.class);
        JButton ferias = abre("Férias", () -> new FeriasWeb(idUser), FeriasWeb.class);
        JButton disciplinar = abre("Processos Disciplinares", () -> new DisciplinarWeb(idUser), DisciplinarWeb.class);

        // ---- barra lateral
        pagina.grupo("Pessoas");
        pagina.item(lateral(funcionarios), "Funcionários");
        item(pagina, a.ficha, "Ficha do Funcionário");
        pagina.grupo("Assiduidade e disciplina");
        pagina.item(lateral(faltas), "Faltas e Assiduidade");
        pagina.item(lateral(ferias), "Férias");
        pagina.item(lateral(disciplinar), "Processos Disciplinares");
        pagina.item(accao("Feriados", () -> TabelasRH.feriados(janela)), "Feriados");
        pagina.grupo("Salários");
        item(pagina, a.recibos, "Recibos / Processamento");
        item(pagina, a.outras, "Outras Operações");
        item(pagina, a.mapas, "Mapas e Relatórios");
        pagina.grupo("Configuração");
        pagina.item(accao("Definições do RH", () -> TabelasRH.definicoes(janela)), "Definições do RH (prazos, férias)");
        item(pagina, a.tabelas, "Tabelas Dinâmicas");
        item(pagina, a.definicoes, "Definições (salários)");
        item(pagina, a.usuario, "Utilizadores");
        item(pagina, a.empresa, "Dados da Empresa");
        item(pagina, a.configuracoes, "Configurações");

        // ---- indicadores (carregados em segundo plano)
        ComponentesWeb.CartaoKpi kActivos = new ComponentesWeb.CartaoKpi(ComponentesWeb.CartaoKpi.PESSOA, "Funcionários activos", ComponentesWeb.AZUL_VIVO).compacto();
        ComponentesWeb.CartaoKpi kFerias = new ComponentesWeb.CartaoKpi("F", "De férias hoje", ComponentesWeb.VERDE).compacto();
        ComponentesWeb.CartaoKpi kFaltas = new ComponentesWeb.CartaoKpi("!", "Faltas este mês", ComponentesWeb.VERMELHO).compacto();
        ComponentesWeb.CartaoKpi kProcessos = new ComponentesWeb.CartaoKpi("PD", "Processos abertos", ComponentesWeb.LARANJA).compacto();
        ComponentesWeb.CartaoKpi kMassa = new ComponentesWeb.CartaoKpi("Kz", "Salários base (mês)", ComponentesWeb.ROXO).compacto();
        JPanel kpis = FormularioWeb.grelha(170, 5, kActivos, kFerias, kFaltas, kProcessos, kMassa);

        // ---- alertas
        DefaultListModel<DadosRH.Alerta> mAlertas = new DefaultListModel<>();
        JList<DadosRH.Alerta> lAlertas = new JList<>(mAlertas);
        lAlertas.setCellRenderer(new RendererAlerta());
        lAlertas.setVisibleRowCount(6);
        lAlertas.setToolTipText("Duplo clique abre o ecrã correspondente");
        lAlertas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DadosRH.Alerta al = lAlertas.getSelectedValue();
                if (e.getClickCount() == 2 && al != null) {
                    ("ferias".equals(al.area) ? ferias : "disciplinar".equals(al.area) ? disciplinar : funcionarios).doClick();
                }
            }
        });
        JScrollPane rolAlertas = new JScrollPane(lAlertas);
        rolAlertas.setBorder(javax.swing.BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        rolAlertas.setPreferredSize(new java.awt.Dimension(400, 190));
        JLabel lbAlertas = FormularioWeb.dica("A verificar...");
        JPanel corpoAlertas = ComponentesWeb.transparente(new BorderLayout(0, 6));
        corpoAlertas.add(lbAlertas, BorderLayout.NORTH);
        corpoAlertas.add(rolAlertas, BorderLayout.CENTER);
        JButton actualizar = UiRH.botao("Actualizar", ComponentesWeb.Variante.SECUNDARIO, null, null);
        corpoAlertas.add(UiRH.linha(actualizar), BorderLayout.SOUTH);

        Runnable carregar = () -> new SwingWorker<DadosRH.Indicadores, Void>() {
            final List<DadosRH.Alerta> alertas = new ArrayList<>();

            @Override
            protected DadosRH.Indicadores doInBackground() throws Exception {
                try (Connection cn = DadosRH.ligacao()) {
                    return DadosRH.indicadores(cn, alertas);
                }
            }

            @Override
            protected void done() {
                try {
                    DadosRH.Indicadores i = get();
                    kActivos.setValor(String.valueOf(i.activos), i.homens + " H · " + i.mulheres + " M");
                    kFerias.setValor(String.valueOf(i.emFerias), i.feriasProximas == 0 ? "ninguém a sair em breve"
                            : i.feriasProximas + " saem nos próximos 14 dias");
                    kFaltas.setValor(String.valueOf(i.faltasMes), "dias de falta registados");
                    kProcessos.setValor(String.valueOf(i.processosAbertos), i.processosAbertos == 0 ? "nenhum em curso" : "em curso");
                    kMassa.setValor(UiRH.moeda(i.massaSalarial), i.aniversariantesMes == 0 ? "" : i.aniversariantesMes
                            + (i.aniversariantesMes == 1 ? " aniversariante" : " aniversariantes") + " este mês");
                    mAlertas.clear();
                    for (DadosRH.Alerta al : alertas) {
                        mAlertas.addElement(al);
                    }
                    long urgentes = alertas.stream().filter(x -> x.gravidade == 2).count();
                    lbAlertas.setText(alertas.isEmpty() ? "Nada pendente. Tudo em dia."
                            : alertas.size() + (alertas.size() == 1 ? " aviso" : " avisos")
                            + (urgentes > 0 ? " · " + urgentes + (urgentes == 1 ? " urgente" : " urgentes") : ""));
                    lbAlertas.setForeground(urgentes > 0 ? ComponentesWeb.VERMELHO : ComponentesWeb.TEXTO_SUAVE);
                } catch (Exception e) {
                    e.printStackTrace();
                    lbAlertas.setText("Não foi possível ler os indicadores: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                    lbAlertas.setForeground(ComponentesWeb.VERMELHO);
                }
            }
        }.execute();
        actualizar.addActionListener(e -> carregar.run());
        // ao voltar de um ecrã do RH, os números acompanham (no máximo de 20 em 20 segundos)
        long[] ultimo = {System.currentTimeMillis()};
        carregar.run();
        janela.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                if (System.currentTimeMillis() - ultimo[0] > 20_000) {
                    ultimo[0] = System.currentTimeMillis();
                    carregar.run();
                }
            }
        });

        // ---- cartões dos ecrãs
        JButton cFunc = cartao(funcionarios), cFaltas = cartao(faltas), cFerias = cartao(ferias), cDisc = cartao(disciplinar);
        MenuWeb.cartaoModulo(cFunc, MenuWeb.Simbolo.PESSOAS, "Funcionários", "Lista, ficha, histórico e declarações", 230, 150);
        MenuWeb.cartaoModulo(cFaltas, MenuWeb.Simbolo.PRANCHETA, "Faltas", "Registar e justificar; o salário já as conta", 230, 150);
        MenuWeb.cartaoModulo(cFerias, MenuWeb.Simbolo.CALENDARIO, "Férias", "Marcações, pedidos e saldo de cada um", 230, 150);
        MenuWeb.cartaoModulo(cDisc, MenuWeb.Simbolo.SINO, "Disciplinar", "Nota de culpa, defesa e decisão, com prazos", 230, 150);
        List<Component> cartoes = new ArrayList<>(java.util.Arrays.asList(cFunc, cFaltas, cFerias, cDisc));
        if (a.recibos != null && a.recibos.isVisible()) {
            JButton cRecibos = cartao(a.recibos);
            MenuWeb.cartaoModulo(cRecibos, MenuWeb.Simbolo.RECIBO, "Salários", "Processamento e recibos", 230, 150);
            cartoes.add(cRecibos);
        }

        JPanel conteudo = FormularioWeb.pilha(10,
                MenuWeb.rotuloGrupo("Hoje", MenuWeb.DOURADO.darker()),
                kpis,
                FormularioWeb.cartao("Precisa de atenção", ComponentesWeb.VERMELHO, corpoAlertas),
                javax.swing.Box.createVerticalStrut(4),
                MenuWeb.rotuloGrupo("Ecrãs", MenuWeb.DOURADO.darker()),
                FormularioWeb.grelha(230, 5, cartoes.toArray(new Component[0])));
        pagina.conteudo(conteudo);
        if (a.sair != null) {
            pagina.sair(a.sair, "Voltar ao menu principal");
        }
        pagina.montar(janela, "SysKitanda  ·  Recursos Humanos", 4);
    }

    // ---------------------------------------------------------------- botões
    /** Botão que abre um ecrã (ou traz para a frente o que já estiver aberto). */
    private static JButton abre(String texto, Supplier<JFrame> ecra, Class<? extends JFrame> tipo) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> {
            if (UiRH.trazerParaFrente(tipo)) {
                return;
            }
            try {
                ecra.get().setVisible(true);
            } catch (RuntimeException ex) {
                UiRH.erro(b, ex);
            }
        });
        return b;
    }

    private static JButton accao(String texto, Runnable r) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> UiRH.executar(b, r));
        return b;
    }

    /** Na barra lateral vai uma cópia que carrega no botão do cartão (um botão só pode estar num sítio). */
    private static JButton lateral(JButton original) {
        JButton b = new JButton(original.getText());
        b.addActionListener(e -> original.doClick());
        return b;
    }

    private static JButton cartao(JButton original) {
        return lateral(original);
    }

    private static void item(PaginaMenuWeb pagina, JButton b, String rotulo) {
        if (b != null) {
            pagina.item(b, rotulo);
        }
    }

    /** Alerta com um ponto da cor da gravidade. */
    private static final class RendererAlerta extends javax.swing.DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foco) {
            super.getListCellRendererComponent(l, v, i, sel, foco);
            DadosRH.Alerta a = (DadosRH.Alerta) v;
            Color c = a.gravidade == 2 ? ComponentesWeb.VERMELHO : a.gravidade == 1 ? ComponentesWeb.LARANJA : ComponentesWeb.AZUL_VIVO;
            String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
            setText("<html><span style='color:" + hex + "'>●</span>&nbsp; " + a.texto.replace("&", "&amp;").replace("<", "&lt;") + "</html>");
            setFont(util.ui.EstiloUI.FONTE.deriveFont(13f));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 8, 5, 8));
            return this;
        }
    }
}
