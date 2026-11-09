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
    private JComponent conteudo;

    /** perfil pode ser null. */
    public PaginaMenuWeb(String area, String subtitulo, JLabel lbNome, String perfil) {
        this.area = area;
        this.subtitulo = subtitulo;
        this.lbNome = lbNome;
        this.perfil = perfil;
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 8));
    }

    public void grupo(String titulo) {
        if (menu.getComponentCount() > 0) {
            menu.add(Box.createVerticalStrut(10));
        }
        JLabel l = MenuWeb.rotuloGrupo(titulo, MenuWeb.DOURADO);
        l.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 0));
        menu.add(l);
    }

    /** rotulo é o que aparece; o getText() do botão fica como está. */
    public void item(JButton b, String rotulo) {
        MenuWeb.itemLateral(b, rotulo);
        menu.add(b);
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
        marca.add(new MenuWeb.Logotipo(42), BorderLayout.WEST);
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
        lateral.add(marca, BorderLayout.NORTH);

        JPanel topoMenu = ComponentesWeb.transparente(new BorderLayout());
        topoMenu.add(menu, BorderLayout.NORTH);
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
