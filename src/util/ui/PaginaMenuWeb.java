package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 * Página de menu igual à do menu principal do SysKitandaHotel: barra lateral azul com as
 * operações por grupos e área principal com saudação e cartões. Os botões vêm
 * do initComponents de cada menu, com os seus eventos e permissões: o getText()
 * não muda, porque é com ele que as permissões são comparadas.
 */
public final class PaginaMenuWeb {

    private static final int CARTAO_LARGURA = 190;
    private static final int CARTAO_ALTURA_MIN = 158;
    private static final int CARTAO_ALTURA_MAX = 300;
    private static final int GRELHA_ESPACO = 14;

    private final String area;
    private final String subtitulo;
    private final JLabel lbNome;
    private final String perfil;
    private final JPanel menu = ComponentesWeb.transparente(null);
    private final List<JButton> cartoes = new ArrayList<>();
    private JButton botaoSair;
    private MenuWeb.Simbolo simboloMarca = Tema.simbolo();
    private JComponent conteudo;

    /** perfil pode ser null. */
    public PaginaMenuWeb(String area, String subtitulo, JLabel lbNome, String perfil) {
        this.area = area;
        this.subtitulo = subtitulo;
        this.lbNome = lbNome;
        this.perfil = perfil;
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 8));
        resultados.setLayout(new BoxLayout(resultados, BoxLayout.Y_AXIS));
        resultados.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 8));
        resultados.setVisible(false);

        busca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });
        busca.addActionListener(e -> abrirPrimeiroResultado()); // Enter
        busca.registerKeyboardAction(e -> busca.setText(""),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
    }

    /** Símbolo do logótipo da barra lateral (o desenho do negócio). */
    public void simboloMarca(MenuWeb.Simbolo s) {
        if (s != null) {
            simboloMarca = s;
        }
    }

    /**
     * Grupo da barra lateral: um cabeçalho que, ao clicar, expande / recolhe
     * os itens do grupo (começa recolhido). Os itens ficam dentro de um painel
     * próprio: a visibilidade de cada botão (permissões, submenus) não é tocada.
     */
    public void grupo(String titulo) {
        if (menu.getComponentCount() > 0) {
            menu.add(Box.createVerticalStrut(4));
        }
        final Grupo g = new Grupo(titulo);
        g.cabecalho.addActionListener(e -> abrirGrupo(g, !g.aberto));
        menu.add(g.cabecalho);
        menu.add(g.corpo);
        grupos.add(g);
        grupoActual = g;
    }

    /** rotulo é o que aparece; o getText() do botão fica como está. */
    public void item(JButton b, String rotulo) {
        MenuWeb.itemLateral(b, rotulo);
        if (grupoActual == null) {
            // item antes de qualquer grupo: fica sempre visível
            if (semGrupo == null) {
                semGrupo = new Grupo(null);
                grupos.add(0, semGrupo);
            }
            semGrupo.itens.add(b);
            semGrupo.rotulos.add(rotulo);
            menu.add(b);
        } else {
            grupoActual.itens.add(b);
            grupoActual.rotulos.add(rotulo);
            grupoActual.corpo.add(b);
        }
    }

    // =====================================================================
    // GRUPOS EXPANSÍVEIS E BUSCA
    // =====================================================================
    private final List<Grupo> grupos = new ArrayList<>();
    private Grupo grupoActual;
    private Grupo semGrupo;
    private final JPanel resultados = ComponentesWeb.transparente(null);
    private final CampoBusca busca = new CampoBusca("Pesquisar no menu (ex.: CE)");

    private static final class Grupo {

        final String titulo;
        final JButton cabecalho = new JButton();
        final JPanel corpo = ComponentesWeb.transparente(null);
        final List<JButton> itens = new ArrayList<>();
        final List<String> rotulos = new ArrayList<>();
        boolean aberto;

        Grupo(String titulo) {
            this.titulo = titulo;
            if (titulo != null) {
                MenuWeb.cabecalhoGrupo(cabecalho, titulo);
            }
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 0)); // itens ligeiramente recuados
            corpo.setVisible(false);
        }
    }

    private void abrirGrupo(Grupo g, boolean aberto) {
        if (g.titulo == null) {
            return;
        }
        g.aberto = aberto;
        g.corpo.setVisible(aberto);
        MenuWeb.cabecalhoGrupoAberto(g.cabecalho, aberto);
        menu.revalidate();
        menu.repaint();
        if (aberto) {
            // mostrar o grupo aberto (cabeçalho + itens) se estiver fora de vista
            SwingUtilities.invokeLater(() -> {
                Rectangle r = g.cabecalho.getBounds().union(g.corpo.getBounds());
                menu.scrollRectToVisible(r);
            });
        }
    }

    /** Mostra os itens que correspondem ao texto da busca (ou os grupos, sem texto). */
    private void filtrar() {
        String q = normalizar(busca.getText()).replaceAll("\\s+", " ").trim();
        resultados.removeAll();
        if (q.isEmpty()) {
            resultados.setVisible(false);
            menu.setVisible(true);
        } else {
            boolean algum = false;
            for (Grupo g : grupos) {
                boolean cabecalhoPosto = false;
                for (int i = 0; i < g.itens.size(); i++) {
                    final JButton original = g.itens.get(i);
                    String rotulo = g.rotulos.get(i);
                    if (!original.isVisible() || !corresponde(rotulo, q)) {
                        continue;
                    }
                    if (!cabecalhoPosto && g.titulo != null) {
                        if (algum) {
                            resultados.add(Box.createVerticalStrut(8));
                        }
                        JLabel l = MenuWeb.rotuloGrupo(g.titulo, MenuWeb.DOURADO);
                        l.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 0));
                        resultados.add(l);
                        cabecalhoPosto = true;
                    }
                    JButton r = new JButton(original.getText());
                    MenuWeb.itemLateral(r, rotulo);
                    r.setEnabled(original.isEnabled());
                    final Grupo dono = g;
                    r.addActionListener(e -> {
                        busca.setText(""); // volta aos grupos, com o grupo do item aberto
                        abrirGrupo(dono, true);
                        original.doClick();
                    });
                    resultados.add(r);
                    algum = true;
                }
            }
            if (!algum) {
                JLabel nada = new JLabel("Nenhuma opção encontrada");
                nada.setFont(EstiloUI.FONTE.deriveFont(12.5f));
                nada.setForeground(new Color(180, 195, 215));
                nada.setBorder(BorderFactory.createEmptyBorder(6, 12, 0, 0));
                resultados.add(nada);
            }
            menu.setVisible(false);
            resultados.setVisible(true);
        }
        resultados.revalidate();
        resultados.repaint();
    }

    /** Enter na busca: abre a primeira opção activa encontrada. */
    private void abrirPrimeiroResultado() {
        for (Component c : resultados.getComponents()) {
            if (c instanceof JButton && c.isEnabled()) {
                ((JButton) c).doClick();
                return;
            }
        }
    }

    private static final java.util.Set<String> PALAVRAS_LIGACAO = new java.util.HashSet<>(
            java.util.Arrays.asList("de", "da", "do", "das", "dos", "e", "a", "o", "as", "os",
                    "em", "na", "no", "por", "para", "com", "ao", "aos"));

    /**
     * A busca encontra a opção pelas iniciais ou pelo início das palavras:
     * "ce" ou "cef" -> "Consultar Estado das Facturas"; "fact" -> "... Facturas";
     * "cons est" -> cada parte é o início de uma palavra. Sem acentos nem
     * maiúsculas. Com 3 letras ou mais também serve qualquer parte do texto.
     */
    static boolean corresponde(String rotulo, String q) {
        String n = normalizar(rotulo);
        if (n.startsWith(q) || (q.length() >= 3 && n.contains(q))) {
            return true;
        }
        List<String> palavras = new ArrayList<>();
        for (String p : n.split("[^a-z0-9]+")) {
            if (!p.isEmpty()) {
                palavras.add(p);
            }
        }
        if (q.contains(" ")) {
            for (String parte : q.split(" ")) {
                boolean achou = false;
                for (String p : palavras) {
                    if (p.startsWith(parte)) {
                        achou = true;
                        break;
                    }
                }
                if (!achou) {
                    return false;
                }
            }
            return true;
        }
        StringBuilder todas = new StringBuilder();
        StringBuilder principais = new StringBuilder();
        for (String p : palavras) {
            if (p.startsWith(q)) {
                return true;
            }
            todas.append(p.charAt(0));
            if (!PALAVRAS_LIGACAO.contains(p)) {
                principais.append(p.charAt(0));
            }
        }
        return principais.toString().startsWith(q) || todas.toString().startsWith(q);
    }

    private static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

    /** Campo de busca para o fundo azul: lupa, texto branco e dica quando vazio. */
    private static final class CampoBusca extends javax.swing.JTextField {

        private final String dica;

        CampoBusca(String dica) {
            this.dica = dica;
            // fundo branco sólido e texto escuro: bem legível com qualquer tema
            setOpaque(false);
            setForeground(ComponentesWeb.TEXTO);
            setCaretColor(ComponentesWeb.TEXTO);
            setSelectionColor(MenuWeb.DOURADO_CLARO);
            setSelectedTextColor(MenuWeb.AZUL_NOITE);
            setFont(EstiloUI.FONTE.deriveFont(13.5f));
            setBorder(BorderFactory.createEmptyBorder(7, 32, 7, 10));
            setToolTipText("Escreva as iniciais ou parte do nome da opção (Enter abre a primeira; Esc limpa)");
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
            g2.setColor(isFocusOwner() ? MenuWeb.DOURADO : new Color(255, 255, 255, 140));
            g2.setStroke(new java.awt.BasicStroke(isFocusOwner() ? 2f : 1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
            // lupa
            g2.setColor(isFocusOwner() ? MenuWeb.AZUL_NOITE : ComponentesWeb.TEXTO_SUAVE);
            g2.setStroke(new java.awt.BasicStroke(1.7f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            int cy = h / 2;
            g2.drawOval(11, cy - 7, 10, 10);
            g2.drawLine(19, cy + 1, 23, cy + 5);
            if (getText().isEmpty() && !isFocusOwner()) {
                g2.setFont(getFont());
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                java.awt.FontMetrics fm = g2.getFontMetrics();
                g2.drawString(dica, getInsets().left, (h - fm.getHeight()) / 2 + fm.getAscent());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Item lateral que desliza a área principal até à secção indicada. */
    public void ancora(String rotulo, JComponent seccao) {
        JButton b = new JButton(rotulo);
        b.addActionListener(e -> {
            JViewport vista = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, seccao);
            int altura = vista == null ? seccao.getHeight() : vista.getHeight();
            seccao.scrollRectToVisible(new Rectangle(0, 0, seccao.getWidth(), altura));
        });
        item(b, rotulo);
    }

    public void cartao(JButton b, MenuWeb.Simbolo simbolo, String titulo, String descricao) {
        MenuWeb.cartaoModulo(b, simbolo, titulo, descricao, CARTAO_LARGURA, CARTAO_ALTURA_MIN);
        cartoes.add(b);
    }

    public void sair(JButton b, String dica) {
        MenuWeb.botaoContorno(b, "Sair", dica);
        botaoSair = b;
    }

    /** Conteúdo próprio por baixo da saudação, em vez da grelha de cartões. */
    public void conteudo(JComponent c) {
        conteudo = c;
    }

    /**
     * Depois do MetodosUtil.verificarCaixa: os botões que ele desactivou estão
     * assim por causa da caixa, não por falta de permissão.
     */
    public static void dicasCaixa(JButton abertura, JButton... dependentes) {
        if (!abertura.isEnabled()) {
            abertura.setToolTipText("A caixa já está aberta");
        }
        for (JButton b : dependentes) {
            if (!b.isEnabled()) {
                b.setToolTipText("Disponível com a caixa aberta: faça primeiro a Abertura de Caixa");
            }
        }
    }

    public void montar(JFrame janela, String textoEstado, int maxColunas) {
        janela.setIconImages(MenuWeb.iconesAplicacao());

        // com um só grupo não há nada a escolher: fica aberto
        List<Grupo> comTitulo = new ArrayList<>();
        for (Grupo g : grupos) {
            if (g.titulo != null) {
                comTitulo.add(g);
            }
        }
        if (comTitulo.size() == 1) {
            abrirGrupo(comTitulo.get(0), true);
        }

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(MenuWeb.MARFIM);
        raiz.add(barraLateral(), BorderLayout.WEST);
        raiz.add(areaPrincipal(maxColunas), BorderLayout.CENTER);
        raiz.add(EstiloUI.barraEstadoComRelogio(janela, textoEstado), BorderLayout.SOUTH);
        janela.setContentPane(raiz);
        janela.setMinimumSize(new Dimension(1024, 640));
        janela.setResizable(true);
        janela.setLocationRelativeTo(null);
        janela.setExtendedState(janela.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        // sem isto o foco inicial cai no primeiro item, que parecia seleccionado
        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                janela.getRootPane().requestFocusInWindow();
            }
        });
    }

    private JPanel barraLateral() {
        JPanel lateral = new ComponentesWeb.PainelGradiente(new BorderLayout(), MenuWeb.AZUL_NOITE, MenuWeb.AZUL_MARINHO);
        lateral.setPreferredSize(new Dimension(258, 100));

        JPanel marca = ComponentesWeb.transparente(new BorderLayout(12, 0));
        marca.setBorder(BorderFactory.createEmptyBorder(18, 18, 14, 14));
        marca.add(new MenuWeb.Logotipo(42, simboloMarca), BorderLayout.WEST);
        String hotel = nomeEmpresa();
        JLabel lbHotel = new JLabel(hotel);
        lbHotel.setToolTipText(hotel);
        lbHotel.setFont(new java.awt.Font("Georgia", java.awt.Font.BOLD, 15));
        lbHotel.setForeground(Color.WHITE);
        JLabel lbArea = new JLabel(area);
        lbArea.setFont(EstiloUI.FONTE.deriveFont(12f));
        lbArea.setForeground(MenuWeb.DOURADO_CLARO);
        JPanel textosMarca = ComponentesWeb.transparente(new GridLayout(2, 1));
        textosMarca.add(lbHotel);
        textosMarca.add(lbArea);
        marca.add(textosMarca, BorderLayout.CENTER);

        // busca por iniciais / nome da opção, por baixo da marca
        JPanel painelBusca = ComponentesWeb.transparente(new BorderLayout());
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 16, 10, 14));
        painelBusca.add(busca, BorderLayout.CENTER);
        JPanel topo = ComponentesWeb.transparente(new BorderLayout());
        topo.add(marca, BorderLayout.NORTH);
        topo.add(painelBusca, BorderLayout.CENTER);
        lateral.add(topo, BorderLayout.NORTH);

        // grupos (sem busca) ou resultados da busca
        JPanel listas = ComponentesWeb.transparente(null);
        listas.setLayout(new BoxLayout(listas, BoxLayout.Y_AXIS));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultados.setAlignmentX(Component.LEFT_ALIGNMENT);
        listas.add(menu);
        listas.add(resultados);
        JPanel topoMenu = ComponentesWeb.transparente(new BorderLayout());
        topoMenu.add(listas, BorderLayout.NORTH);
        JScrollPane rolagem = ComponentesWeb.rolagemVertical(topoMenu);
        MenuWeb.barraFina(rolagem.getVerticalScrollBar());
        lateral.add(rolagem, BorderLayout.CENTER);

        lateral.add(utilizador(), BorderLayout.SOUTH);
        return lateral;
    }

    private JPanel utilizador() {
        JPanel utilizador = ComponentesWeb.transparente(new BorderLayout(0, 12));
        utilizador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 40)),
                BorderFactory.createEmptyBorder(12, 16, 14, 16)));
        JPanel identidade = ComponentesWeb.transparente(new BorderLayout(10, 0));
        identidade.add(new MenuWeb.Avatar(lbNome, 36), BorderLayout.WEST);
        lbNome.setFont(EstiloUI.FONTE_NEGRITO);
        lbNome.setForeground(Color.WHITE);
        lbNome.setToolTipText(lbNome.getText());
        JLabel lbSessao = new JLabel(perfil == null || perfil.trim().isEmpty() ? "Sessão iniciada" : perfil);
        lbSessao.setFont(EstiloUI.FONTE.deriveFont(11.5f));
        lbSessao.setForeground(new Color(180, 195, 215));
        JPanel textos = ComponentesWeb.transparente(new GridLayout(2, 1));
        textos.add(lbNome);
        textos.add(lbSessao);
        identidade.add(textos, BorderLayout.CENTER);
        utilizador.add(identidade, BorderLayout.NORTH);
        utilizador.add(botaoSair, BorderLayout.CENTER);
        return utilizador;
    }

    private Component areaPrincipal(int maxColunas) {
        JLabel lbSaudacao = new JLabel(saudacao());
        lbSaudacao.setFont(MenuWeb.FONTE_TITULO);
        lbSaudacao.setForeground(MenuWeb.AZUL_NOITE);
        JLabel lbSub = new JLabel(subtitulo);
        lbSub.setFont(EstiloUI.FONTE.deriveFont(14f));
        lbSub.setForeground(MenuWeb.TEXTO_SUAVE);

        JPanel cabecalho = ComponentesWeb.transparente(null);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(lbSaudacao);
        cabecalho.add(Box.createVerticalStrut(2));
        cabecalho.add(lbSub);
        cabecalho.add(Box.createVerticalStrut(18));
        if (conteudo == null) {
            cabecalho.add(MenuWeb.rotuloGrupo("Operações principais", MenuWeb.DOURADO.darker()));
            cabecalho.add(Box.createVerticalStrut(8));
        }

        JPanel grelha = null;
        JPanel corpo = ComponentesWeb.transparente(new BorderLayout());
        corpo.add(cabecalho, BorderLayout.NORTH);
        if (conteudo != null) {
            corpo.add(conteudo, BorderLayout.CENTER);
        } else {
            grelha = ComponentesWeb.transparente(new ComponentesWeb.GrelhaResponsiva(CARTAO_LARGURA, GRELHA_ESPACO, maxColunas));
            for (JButton c : cartoes) {
                grelha.add(c);
            }
            corpo.add(grelha, BorderLayout.CENTER);
        }

        // o conteúdo começa no topo da página, como numa web app
        JPanel pagina = ComponentesWeb.transparente(new GridBagLayout());
        pagina.setBorder(BorderFactory.createEmptyBorder(20, 32, 16, 32));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        pagina.add(corpo, gbc);

        JScrollPane rolagem = ComponentesWeb.rolagemVertical(pagina);
        if (grelha != null) {
            final JPanel g = grelha;
            ComponentAdapter ajustar = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    ajustarCartoes(rolagem, cabecalho, g, maxColunas);
                }
            };
            rolagem.addComponentListener(ajustar);
            grelha.addComponentListener(ajustar);
        }
        return rolagem;
    }

    /**
     * Todos os cartões ficam com a mesma altura, a maior que caiba na janela
     * dentro dos limites; se nem a mínima couber, a página desliza.
     */
    private void ajustarCartoes(JScrollPane rolagem, JComponent cabecalho, JPanel grelha, int maxColunas) {
        int largura = grelha.getWidth();
        int visiveis = 0;
        for (JButton c : cartoes) {
            if (c.isVisible()) {
                visiveis++;
            }
        }
        if (largura <= 0 || visiveis == 0) {
            return;
        }
        int colunas = Math.max(1, Math.min(Math.min(maxColunas, visiveis),
                (largura + GRELHA_ESPACO) / (CARTAO_LARGURA + GRELHA_ESPACO)));
        int linhas = (visiveis + colunas - 1) / colunas;
        int livre = rolagem.getHeight() - cabecalho.getPreferredSize().height - 36 - (linhas - 1) * GRELHA_ESPACO;
        int altura = Math.max(CARTAO_ALTURA_MIN, Math.min(CARTAO_ALTURA_MAX, livre / linhas));
        for (JButton c : cartoes) {
            c.setPreferredSize(new Dimension(CARTAO_LARGURA, altura));
            c.setMinimumSize(new Dimension(CARTAO_LARGURA, altura));
        }
        grelha.revalidate();
    }

    private String saudacao() {
        int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String s = hora < 12 ? "Bom dia" : hora < 19 ? "Boa tarde" : "Boa noite";
        String nome = lbNome.getText() == null ? "" : lbNome.getText().trim();
        return nome.isEmpty() ? s : s + ", " + nome.split("\\s+")[0];
    }

    private static String nomeEmpresa() {
        try {
            String nome = new dao.DadosInstituicaoDao(util.JPAEntityMannagerFactoryUtil.em).findTbDadosInstituicao(1).getNome();
            if (nome != null && !nome.trim().isEmpty()) {
                return nome.trim();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return "SysKitanda Comercial";
    }
}
