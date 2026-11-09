package util.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;

/**
 * Menu principal de um negócio (comercial, lavandaria, restaurante, farmácia,
 * oficina) com o aspecto do menu do SysKitandaHotel: barra lateral azul com
 * os grupos da barra de menus, saudação e cartões com desenhos do negócio.
 *
 * <p>
 * Os itens de menu do initComponents continuam a existir, com os seus
 * eventos e permissões: os botões da página só os "carregam" e seguem o estado
 * deles (activo/visível). A barra de menus sai de vista, mas os atalhos de
 * teclado continuam.
 */
public final class MenuNegocioWeb {

    /** Um cartão grande: o item do menu, o desenho e os textos. */
    public static final class Cartao {

        final JMenuItem item;
        final MenuWeb.Simbolo simbolo;
        final String titulo;
        final String descricao;

        public Cartao(JMenuItem item, MenuWeb.Simbolo simbolo, String titulo, String descricao) {
            this.item = item;
            this.simbolo = simbolo;
            this.titulo = titulo;
            this.descricao = descricao;
        }
    }

    private MenuNegocioWeb() {
    }

    /**
     * @param area        nome do negócio, por baixo do nome da empresa
     * @param simboloMarca desenho do logótipo (ex.: LAVANDARIA)
     * @param idUsuario   para o nome e o perfil na barra lateral
     * @param sair        item que termina a sessão (ex.: "Log Out")
     * @param excluir     itens que não vão para a barra lateral (ex.: "Sair" do sistema)
     */
    public static void montar(JFrame janela, JMenuBar barra, String area, MenuWeb.Simbolo simboloMarca,
            int idUsuario, JMenuItem sair, List<JMenuItem> excluir, Cartao... cartoes) {
        String nome = "", perfil = null;
        try {
            entity.TbUsuario u = new dao.UsuarioDao(util.JPAEntityMannagerFactoryUtil.em).findTbUsuario(idUsuario);
            nome = u.getNome();
            perfil = u.getIdTipoUsuario() == null ? null : u.getIdTipoUsuario().getDescricao();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        PaginaMenuWeb pagina = new PaginaMenuWeb(area, "Seleccione um módulo para começar.", new JLabel(nome), perfil);
        pagina.simboloMarca(simboloMarca);

        // ---- barra lateral: um grupo por menu, com os itens (e submenus)
        List<JMenuItem> fora = new ArrayList<>();
        if (excluir != null) {
            fora.addAll(excluir);
        }
        if (sair != null) {
            fora.add(sair);
        }
        for (int i = 0; i < barra.getMenuCount(); i++) {
            JMenu menu = barra.getMenu(i);
            if (menu == null || !menu.isVisible()) {
                continue;
            }
            List<JButton> itens = new ArrayList<>();
            List<String> rotulos = new ArrayList<>();
            recolher(menu, null, fora, itens, rotulos);
            if (itens.isEmpty()) {
                continue;
            }
            pagina.grupo(nomeGrupo(menu.getText()));
            for (int k = 0; k < itens.size(); k++) {
                pagina.item(itens.get(k), rotulos.get(k));
            }
        }

        // ---- cartões dos módulos principais
        for (Cartao c : cartoes) {
            if (c != null && c.item != null) {
                pagina.cartao(espelho(c.item), c.simbolo, c.titulo, c.descricao);
            }
        }

        // ---- sair
        if (sair != null) {
            JButton b = new JButton(sair.getText());
            b.addActionListener(e -> sair.doClick());
            pagina.sair(b, "Terminar a sessão e voltar ao início de sessão");
        }

        // a barra de menus sai de vista: os atalhos continuam
        JRootPane rp = janela.getRootPane();
        for (JMenuItem item : itens(barra)) {
            if (item.getAccelerator() != null) {
                final JMenuItem alvo = item;
                rp.registerKeyboardAction(e -> {
                    if (alvo.isEnabled() && alvo.isVisible()) {
                        alvo.doClick();
                    }
                }, alvo.getAccelerator(), JComponent.WHEN_IN_FOCUSED_WINDOW);
            }
        }
        janela.setJMenuBar(null);
        pagina.montar(janela, "SysKitanda  ·  " + area, 5);
    }

    /** Procura um item pelo texto (para escolher os cartões sem conhecer o nome da variável). */
    public static JMenuItem procurar(JMenuBar barra, String texto) {
        for (JMenuItem i : itens(barra)) {
            if (i.getText() != null && i.getText().trim().equalsIgnoreCase(texto)) {
                return i;
            }
        }
        return null;
    }

    public static List<JMenuItem> itens(JMenuBar barra) {
        List<JMenuItem> l = new ArrayList<>();
        for (int i = 0; i < barra.getMenuCount(); i++) {
            recolherTodos(barra.getMenu(i), l);
        }
        return l;
    }

    private static void recolherTodos(JMenu menu, List<JMenuItem> l) {
        if (menu == null) {
            return;
        }
        for (Component c : menu.getMenuComponents()) {
            if (c instanceof JMenu) {
                recolherTodos((JMenu) c, l);
            } else if (c instanceof JMenuItem) {
                l.add((JMenuItem) c);
            }
        }
    }

    private static void recolher(JMenu menu, String prefixo, List<JMenuItem> fora, List<JButton> itens, List<String> rotulos) {
        for (Component c : menu.getMenuComponents()) {
            if (c instanceof JMenu) {
                JMenu sub = (JMenu) c;
                recolher(sub, sub.getText(), fora, itens, rotulos);
            } else if (c instanceof JMenuItem && !fora.contains(c)) {
                JMenuItem item = (JMenuItem) c;
                JButton b = espelho(item);
                if (prefixo != null) {
                    seguirMenu(menu, b);
                }
                itens.add(b);
                rotulos.add(prefixo == null ? item.getText() : prefixo + " · " + item.getText());
            }
        }
    }

    private static String nomeGrupo(String t) {
        if (t == null) {
            return "";
        }
        switch (t.trim().toLowerCase()) {
            case "ficheiro":
                return "Operações";
            case "relatorio/mapa":
            case "relatório/mapa":
                return "Relatórios e Mapas";
            case "logistica":
                return "Logística";
            default:
                return t.trim();
        }
    }

    /** Botão que carrega no item de menu e segue o estado dele (permissões e visibilidade). */
    public static JButton espelho(final JMenuItem item) {
        final JButton b = new JButton(item.getText());
        b.setToolTipText(item.getText());
        b.addActionListener(e -> {
            if (item.isEnabled()) {
                item.doClick();
            }
        });
        b.setEnabled(item.isEnabled());
        b.setVisible(item.isVisible());
        item.addPropertyChangeListener("enabled", e -> b.setEnabled(item.isEnabled()));
        item.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                b.setVisible(true);
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                b.setVisible(false);
            }
        });
        return b;
    }

    private static void seguirMenu(final JMenu sub, final JButton b) {
        if (!sub.isVisible()) {
            b.setVisible(false);
        }
        if (!sub.isEnabled()) {
            b.setEnabled(false);
        }
        sub.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                b.setVisible(false);
            }
        });
        sub.addPropertyChangeListener("enabled", e -> {
            if (!sub.isEnabled()) {
                b.setEnabled(false);
            }
        });
    }
}
