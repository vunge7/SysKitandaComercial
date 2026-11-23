package comercial.ginasio;

import comercial.ginasio.PedidosGinasio.Cliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.Tema;
import visao.design.CampoPesquisa;

/**
 * Campo de pesquisa de clientes (nome, telefone ou NIF) com sugestões numa
 * lista por baixo: ↑ ↓ para navegar, Enter ou clique para escolher, Esc para
 * fechar. O cliente escolhido vai para {@code aoEscolher}. 2026-11-21.
 */
public final class PesquisaClienteCampo extends CampoPesquisa {

    private final Consumer<Cliente> aoEscolher;
    private final DefaultListModel<Cliente> modelo = new DefaultListModel<>();
    private final JList<Cliente> lista = new JList<>(modelo);
    private final JLabel vazio = new JLabel();
    private final JPopupMenu popup = new JPopupMenu();
    private final Timer temporizador;
    private int pedido;          // descarta respostas antigas (escrita rápida)
    private boolean aEscolher;   // o texto muda ao escolher: não voltar a procurar

    public PesquisaClienteCampo(Consumer<Cliente> aoEscolher) {
        super("Pesquisar cliente…");
        this.aoEscolher = aoEscolher;
        setFont(getFont().deriveFont(14f));
        setToolTipText("<html><b>Pesquisar cliente</b><br>Nome, telefone ou NIF · ↑ ↓ e Enter para escolher</html>");

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer(new Renderer());
        lista.setFocusable(false);
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int i = lista.locationToIndex(e.getPoint());
                if (i >= 0) {
                    escolher(modelo.get(i));
                }
            }
        });
        JScrollPane rolagem = new JScrollPane(lista);
        rolagem.setBorder(null);
        rolagem.getVerticalScrollBar().setUnitIncrement(20);
        vazio.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        vazio.setForeground(ComponentesWeb.TEXTO_SUAVE);
        vazio.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        popup.setLayout(new BorderLayout());
        popup.setFocusable(false);
        popup.setBorder(BorderFactory.createLineBorder(ComponentesWeb.LINHA));
        popup.add(rolagem, BorderLayout.CENTER);
        popup.add(vazio, BorderLayout.SOUTH);

        temporizador = new Timer(220, e -> procurar());
        temporizador.setRepeats(false);
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mudou();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mudou();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mudou();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int n = modelo.getSize();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if (!popup.isVisible() && !getText().trim().isEmpty()) {
                            procurar();
                        } else if (n > 0) {
                            mover(Math.min(n - 1, lista.getSelectedIndex() + 1));
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_UP:
                        if (n > 0) {
                            mover(Math.max(0, lista.getSelectedIndex() - 1));
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ENTER:
                        temporizador.stop();
                        if (popup.isVisible() && n > 0) {
                            escolher(lista.getSelectedIndex() >= 0 ? lista.getSelectedValue() : modelo.get(0));
                        } else {
                            procurar();
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (popup.isVisible()) {
                            popup.setVisible(false);
                            e.consume();
                        }
                        break;
                    default:
                }
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (!e.isTemporary()) {
                    popup.setVisible(false);
                }
            }
        });
    }

    private void mudou() {
        if (aEscolher) {
            return;
        }
        if (getText().trim().isEmpty()) {
            temporizador.stop();
            popup.setVisible(false);
        } else {
            temporizador.restart();
        }
    }

    private void mover(int i) {
        lista.setSelectedIndex(i);
        lista.ensureIndexIsVisible(i);
    }

    private void procurar() {
        final String t = getText().trim();
        if (t.isEmpty()) {
            return;
        }
        final int este = ++pedido;
        new SwingWorker<List<Cliente>, Void>() {
            @Override
            protected List<Cliente> doInBackground() throws Exception {
                return PedidosGinasio.procurarClientes(t);
            }

            @Override
            protected void done() {
                if (este != pedido || !isShowing()) {
                    return;
                }
                try {
                    mostrar(get(), t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void mostrar(List<Cliente> clientes, String t) {
        modelo.clear();
        for (Cliente c : clientes) {
            modelo.addElement(c);
        }
        if (!clientes.isEmpty()) {
            lista.setSelectedIndex(0);
        }
        vazio.setText(clientes.isEmpty() ? "Nenhum cliente com “" + t + "”."
                : clientes.size() >= 30 ? "Mostram-se os primeiros 30: escreva mais para filtrar." : "");
        vazio.setVisible(!vazio.getText().isEmpty());
        int linhas = Math.min(7, Math.max(clientes.size(), 0));
        lista.setVisibleRowCount(Math.max(1, linhas));
        lista.getParent().getParent().setVisible(!clientes.isEmpty());
        int largura = Math.max(getWidth(), 300);
        popup.setPopupSize(largura, (clientes.isEmpty() ? 0 : linhas * 46 + 4)
                + (vazio.isVisible() ? vazio.getPreferredSize().height : 0) + 2);
        if (!popup.isVisible()) {
            popup.show(this, 0, getHeight() + 2);
        } else {
            popup.pack();
            popup.revalidate();
        }
        requestFocusInWindow();
    }

    private void escolher(Cliente c) {
        if (c == null) {
            return;
        }
        popup.setVisible(false);
        aEscolher = true;
        setText("");
        aEscolher = false;
        aoEscolher.accept(c);
    }

    /** Linha da sugestão: iniciais num círculo, nome e telefone/NIF. */
    private static final class Renderer extends JPanel implements ListCellRenderer<Cliente> {

        private final JLabel avatar = new JLabel("", JLabel.CENTER) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = ComponentesWeb.suave(g);
                g2.setColor(Tema.amostra(Tema.actual())[1]);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        private final JLabel nome = new JLabel();
        private final JLabel info = new JLabel();

        Renderer() {
            super(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            avatar.setPreferredSize(new Dimension(32, 32));
            avatar.setForeground(Color.WHITE);
            avatar.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
            nome.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(13f));
            info.setFont(EstiloUI.FONTE.deriveFont(11.5f));
            JPanel textos = new JPanel(new GridLayout(2, 1));
            textos.setOpaque(false);
            textos.add(nome);
            textos.add(info);
            JPanel a = new JPanel(new java.awt.GridBagLayout());
            a.setOpaque(false);
            a.add(avatar);
            add(a, BorderLayout.WEST);
            add(textos, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Cliente> l, Cliente c, int i, boolean sel, boolean foco) {
            avatar.setText(iniciais(c.nome));
            nome.setText(c.nome);
            String t = (c.telefone == null || c.telefone.trim().isEmpty()) ? "" : "Tel. " + c.telefone.trim();
            String n = (c.nif == null || c.nif.trim().isEmpty()) ? "" : "NIF " + c.nif.trim();
            info.setText(t.isEmpty() && n.isEmpty() ? "Cliente n.º " + c.codigo : t + (t.isEmpty() || n.isEmpty() ? "" : "  ·  ") + n);
            setOpaque(true);
            setBackground(sel ? ComponentesWeb.mistura(Tema.amostra(Tema.actual())[1], Color.WHITE, 0.82) : Color.WHITE);
            nome.setForeground(ComponentesWeb.TEXTO);
            info.setForeground(ComponentesWeb.TEXTO_SUAVE);
            return this;
        }

        private static String iniciais(String s) {
            String[] p = s == null ? new String[0] : s.trim().split("\\s+");
            if (p.length == 0 || p[0].isEmpty()) {
                return "?";
            }
            String r = p[0].substring(0, 1);
            if (p.length > 1) {
                r += p[p.length - 1].substring(0, 1);
            }
            return r.toUpperCase();
        }
    }
}
