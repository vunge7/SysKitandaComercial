package visao.log;

import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import visao.design.BotaoModerno;
import visao.design.CampoData;
import visao.design.CampoPesquisa;
import visao.design.ComboModerno;
import visao.design.Selo;
import visao.design.TabelaModerna;
import visao.design.Tema;
import static visao.design.Formularios.dimensionar;
import static visao.design.Formularios.estiloCombo;
import static visao.design.Formularios.estiloOpcao;

/**
 * Cria e configura os componentes da janela de registo de actividades. A
 * disposição no ecrã fica a cargo de {@link LogLayout}.
 */
final class LogComponentes {

    private final LogActividadeVisao j;

    LogComponentes(LogActividadeVisao janela) {
        this.j = janela;
    }

    void criar() {
        criarCabecalho();
        criarFiltros();
        criarTabela();
        criarDetalhe();
        criarRodape();
    }

    private void criarCabecalho() {
        j.lbTotal = new Selo("0 registos");
    }

    private void criarFiltros() {
        j.dcInicio = new CampoData();
        j.dcFim = new CampoData();

        // Atalhos de período (controlo segmentado)
        j.grupoPeriodo = new ButtonGroup();
        j.tgHoje = new JToggleButton("Hoje");
        j.tg7Dias = new JToggleButton("7 dias");
        j.tg30Dias = new JToggleButton("30 dias");
        j.tgEsteMes = new JToggleButton("Este mês");
        for (JToggleButton b : new JToggleButton[]{j.tgHoje, j.tg7Dias, j.tg30Dias, j.tgEsteMes}) {
            j.grupoPeriodo.add(b);
        }

        j.cbUsuario = new ComboModerno<>();
        j.cbModulo = new ComboModerno<>();
        j.cbAccao = new ComboModerno<>();
        for (ComboModerno<String> c : new ComboModerno[]{j.cbUsuario, j.cbModulo, j.cbAccao}) {
            estiloCombo(c);
            c.setMaximumRowCount(16);
        }

        j.txtPesquisa = new CampoPesquisa("Pesquisar na descrição, ecrã, tabela, nº do registo, utilizador ou máquina");
        dimensionar(j.txtPesquisa, 200, Tema.ALTURA_CAMPO);

        j.ckAutomaticos = new JCheckBox("Registos técnicos", true);
        estiloOpcao(j.ckAutomaticos);
        j.ckAutomaticos.setToolTipText("Incluir os registos gerados automaticamente em cada gravação na base de dados");

        j.btLimpar = new BotaoModerno("Limpar", "/imagens/lixo.png", Tema.NEUTRA, true);
        j.btLimpar.setToolTipText("Repor os filtros (últimos 7 dias, todos)");
        j.btPesquisar = new BotaoModerno("Pesquisar", "/imagens/proucura.png", Tema.PRIMARIA, false);
        j.btPesquisar.setToolTipText("Pesquisar (Enter / F5)");
        for (JButton b : new JButton[]{j.btLimpar, j.btPesquisar}) {
            dimensionar(b, Math.max(118, b.getPreferredSize().width), 40);
        }
    }

    private void criarTabela() {
        j.modelo = new LogTabela.Modelo();
        j.tabela = new TabelaModerna(j.modelo) {
            // colunas com a largura preferida; se não couberem, aparece rolagem horizontal
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getParent() == null || getPreferredSize().width <= getParent().getWidth();
            }
        };
        j.tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        j.tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        j.tabela.setAutoResizeMode(TabelaModerna.AUTO_RESIZE_LAST_COLUMN);
        j.tabela.setRowSorter(new TableRowSorter<>(j.modelo));

        LogTabela.Renderizador texto = new LogTabela.Renderizador();
        j.tabela.setDefaultRenderer(Object.class, texto);
        j.tabela.setDefaultRenderer(String.class, texto);
        j.tabela.setDefaultRenderer(java.util.Date.class, texto);
        j.tabela.getColumnModel().getColumn(LogTabela.COL_ACCAO).setCellRenderer(new LogTabela.RenderizadorAccao());

        TableColumnModel colunas = j.tabela.getColumnModel();
        for (int i = 0; i < LogTabela.LARGURAS.length; i++) {
            colunas.getColumn(i).setPreferredWidth(LogTabela.LARGURAS[i]);
        }
        colunas.getColumn(LogTabela.COL_DATA).setMinWidth(150);
    }

    private void criarDetalhe() {
        j.lbDetUtilizador = valorDetalhe();
        j.lbDetMaquina = valorDetalhe();
        j.lbDetEcra = valorDetalhe();
        j.lbDetEntidade = valorDetalhe();

        j.txtDetDescricao = areaLeitura(new Font("Segoe UI", Font.BOLD, 13));
        j.txtAntes = areaLeitura(new Font("Consolas", Font.PLAIN, 12));
        j.txtDepois = areaLeitura(new Font("Consolas", Font.PLAIN, 12));
    }

    private void criarRodape() {
        j.lbEstado = new JLabel(" ");
        j.lbEstado.setFont(Tema.SUBTITULO_CARTAO);
        j.lbEstado.setForeground(Tema.TEXTO_SUAVE);

        j.btPrimeira = new BotaoModerno("«", Tema.PRIMARIA, true);
        j.btAnterior = new BotaoModerno("‹  Anterior", Tema.PRIMARIA, true);
        j.btSeguinte = new BotaoModerno("Seguinte  ›", Tema.PRIMARIA, true);
        j.btUltima = new BotaoModerno("»", Tema.PRIMARIA, true);
        j.btPrimeira.setToolTipText("Primeira página");
        j.btUltima.setToolTipText("Última página");
        for (JButton b : new JButton[]{j.btPrimeira, j.btAnterior, j.btSeguinte, j.btUltima}) {
            dimensionar(b, b.getPreferredSize().width, 38);
        }
        j.lbPagina = new JLabel("Página 0 de 0");
        j.lbPagina.setFont(Tema.OPCAO_FORTE);
        j.lbPagina.setForeground(Tema.TEXTO);

        j.ckValoresRelatorio = new JCheckBox("Incluir antes/depois no relatório");
        estiloOpcao(j.ckValoresRelatorio);

        j.btImprimir = new BotaoModerno("Imprimir relatório", "/imagens/impressora1.png", Tema.SUCESSO, false);
        j.btFechar = new BotaoModerno("Fechar", "/imagens/LOGOUT - VERMELHO/Logout 32x32.png", Tema.NEUTRA, false);
        for (JButton b : new JButton[]{j.btImprimir, j.btFechar}) {
            dimensionar(b, Math.max(118, b.getPreferredSize().width), 40);
        }
    }

    private static JLabel valorDetalhe() {
        JLabel l = new JLabel("—");
        l.setFont(Tema.OPCAO_FORTE);
        l.setForeground(Tema.TEXTO);
        return l;
    }

    // texto só de leitura, sem moldura: a moldura é a Caixa onde fica
    private static JTextArea areaLeitura(Font fonte) {
        JTextArea area = new JTextArea();
        area.setUI(new javax.swing.plaf.basic.BasicTextAreaUI());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFont(fonte);
        area.setForeground(Tema.TEXTO);
        area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        return area;
    }
}
