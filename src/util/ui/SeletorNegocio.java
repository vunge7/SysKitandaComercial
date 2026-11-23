package util.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Escolha do tipo de negócio nos Dados da Empresa: um cartão por negócio
 * (desenho, nome, frase e as cores do negócio) numa grelha responsiva e, por
 * baixo, uma pré-visualização ao vivo de como fica o sistema (barra lateral,
 * cabeçalho, cartões dos módulos, campo e botão com as cores do negócio).
 *
 * <p>
 * Só mostra: o tema do sistema muda quando os dados são gravados (e por
 * completo na próxima entrada no sistema). 2026-11-21.
 */
public final class SeletorNegocio extends JPanel {

    /** Uma opção: valor da coluna negocio, textos, tema e desenho. */
    public static final class Opcao {

        public final String valor;
        public final String titulo;
        public final String descricao;
        public final Tema.Negocio tema;
        public final MenuWeb.Simbolo simbolo;

        public Opcao(String valor, String titulo, String descricao, Tema.Negocio tema, MenuWeb.Simbolo simbolo) {
            this.valor = valor;
            this.titulo = titulo;
            this.descricao = descricao;
            this.tema = tema;
            this.simbolo = simbolo;
        }
    }

    /** Os negócios que o sistema conhece, pela ordem dos cartões. */
    public static List<Opcao> opcoesPadrao() {
        List<Opcao> l = new ArrayList<>();
        l.add(new Opcao("Comercial", "Comercial", "Lojas, boutiques, armazéns, garrafeiras e salões de beleza",
                Tema.Negocio.COMERCIAL, MenuWeb.Simbolo.CARRINHO));
        l.add(new Opcao("Farmacia", "Farmácia", "Medicamentos, lotes, validades e receitas",
                Tema.Negocio.FARMACIA, MenuWeb.Simbolo.FARMACIA));
        l.add(new Opcao("Restaurante", "Restaurante", "Restaurantes e bares: mesas, pedidos e cozinha",
                Tema.Negocio.RESTAURANTE, MenuWeb.Simbolo.TALHERES));
        l.add(new Opcao("Lavandaria", "Lavandaria", "Recolhas, entregas e tratamento de peças",
                Tema.Negocio.LAVANDARIA, MenuWeb.Simbolo.LAVANDARIA));
        l.add(new Opcao("Oficina", "Oficina", "Viaturas, reparações, mão-de-obra e peças",
                Tema.Negocio.OFICINA, MenuWeb.Simbolo.FERRAMENTA));
        l.add(new Opcao("Transportes", "Transportes", "Logística, fábricas, estações de serviço e guias",
                Tema.Negocio.TRANSPORTES, MenuWeb.Simbolo.CAMIAO));
        l.add(new Opcao("Prestação de Serviço", "Serviços", "Prestação de serviços: contratos, avenças e consultoria",
                Tema.Negocio.PRESTACAO_SERVICO, MenuWeb.Simbolo.PASTA));
        l.add(new Opcao("Ginásio", "Ginásio", "Academias e health clubs: sócios, mensalidades e aulas",
                Tema.Negocio.GINASIO, MenuWeb.Simbolo.HALTERE));
        l.add(new Opcao("Centro Infantil", "Centro Infantil", "Creches e jardins de infância: crianças, turmas e propinas",
                Tema.Negocio.CENTRO_INFANTIL, MenuWeb.Simbolo.URSO));
        l.add(new Opcao("Layout", "Layout", "Menu comercial com o logótipo da empresa",
                Tema.Negocio.COMERCIAL, MenuWeb.Simbolo.GRAFICO));
        return l;
    }

    private final List<Cartao> cartoes = new ArrayList<>();
    private final Previsualizacao previsualizacao = new Previsualizacao();
    private Opcao seleccionada;
    private Consumer<Opcao> aoMudar;

    public SeletorNegocio(List<Opcao> opcoes) {
        super(new BorderLayout(0, 14));
        setOpaque(false);
        JPanel grelha = FormularioWeb.grelha(205, 5);
        for (Opcao o : opcoes) {
            Cartao c = new Cartao(o);
            cartoes.add(c);
            grelha.add(c);
        }
        add(grelha, BorderLayout.CENTER);
        add(previsualizacao, BorderLayout.SOUTH);
        if (!opcoes.isEmpty()) {
            seleccionar(opcoes.get(0), false);
        }
    }

    /** Chamado quando o utilizador escolhe outro negócio. */
    public void aoMudar(Consumer<Opcao> accao) {
        this.aoMudar = accao;
    }

    public Opcao getSeleccionada() {
        return seleccionada;
    }

    /** Valor a gravar na coluna negocio. */
    public String getValor() {
        return seleccionada == null ? null : seleccionada.valor;
    }

    /** Escolhe o cartão do valor gravado (sem distinguir acentos nem maiúsculas); desconhecido = Comercial. */
    public void setValor(String valor) {
        String k = chave(valor);
        for (Cartao c : cartoes) {
            if (chave(c.opcao.valor).equals(k)) {
                seleccionar(c.opcao, false);
                return;
            }
        }
        for (Cartao c : cartoes) {
            if (c.opcao.tema == Tema.deTexto(valor)) {
                seleccionar(c.opcao, false);
                return;
            }
        }
        if (!cartoes.isEmpty()) {
            seleccionar(cartoes.get(0).opcao, false);
        }
    }

    private void seleccionar(Opcao o, boolean peloUtilizador) {
        boolean mudou = seleccionada != o;
        seleccionada = o;
        for (Cartao c : cartoes) {
            c.repaint();
        }
        previsualizacao.repaint();
        if (mudou && peloUtilizador && aoMudar != null) {
            aoMudar.accept(o);
        }
    }

    private static String chave(String t) {
        return Normalizer.normalize(t == null ? "" : t.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    // =====================================================================
    // CARTÃO DE UM NEGÓCIO
    // =====================================================================
    private final class Cartao extends JComponent {

        private static final int RAIO = 16;
        final Opcao opcao;
        private boolean sobre;

        Cartao(Opcao opcao) {
            this.opcao = opcao;
            setFocusable(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText(opcao.titulo + " — " + opcao.descricao);
            setPreferredSize(new Dimension(205, 116));
            setBorder(new EmptyBorder(3, 3, 5, 3));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    requestFocusInWindow();
                    seleccionar(opcao, true);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    sobre = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    sobre = false;
                    repaint();
                }
            });
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                        seleccionar(opcao, true);
                        e.consume();
                    }
                }
            });
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            Color[] cores = Tema.amostra(opcao.tema);
            Color escuro = cores[0], vivo = cores[1], destaque = cores[2];
            boolean activo = seleccionada == opcao;
            int x = 3, y = sobre && !activo ? 1 : 3, w = getWidth() - 6, h = getHeight() - 8;

            // sombra e fundo
            g2.setColor(new Color(15, 23, 42, activo || sobre ? 30 : 14));
            g2.fillRoundRect(x, y + 3, w, h, RAIO, RAIO);
            g2.setColor(activo ? ComponentesWeb.mistura(vivo, Color.WHITE, 0.93) : Color.WHITE);
            g2.fillRoundRect(x, y, w, h, RAIO, RAIO);
            // faixa de cor do negócio em cima
            g2.setPaint(new GradientPaint(x, 0, escuro, x + w, 0, vivo));
            g2.fillRoundRect(x, y, w, 8, RAIO, RAIO);
            g2.fillRect(x, y + 4, w, 4);
            // contorno
            g2.setStroke(new BasicStroke(activo ? 2.2f : 1f));
            g2.setColor(activo ? vivo : sobre ? ComponentesWeb.mistura(vivo, Color.WHITE, 0.45) : ComponentesWeb.LINHA);
            g2.drawRoundRect(x, y, w - 1, h - 1, RAIO, RAIO);
            if (isFocusOwner()) {
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{3f, 3f}, 0f));
                g2.setColor(ComponentesWeb.mistura(vivo, Color.WHITE, 0.2));
                g2.drawRoundRect(x + 4, y + 11, w - 9, h - 16, RAIO - 4, RAIO - 4);
            }

            // emblema: círculo em degradê com o desenho
            int d = 46, ex = x + 14, ey = y + 20;
            g2.setPaint(new GradientPaint(ex, ey, escuro, ex + d, ey + d, vivo));
            g2.fillOval(ex, ey, d, d);
            g2.setColor(ComponentesWeb.alfa(destaque, 200));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(ex + 1, ey + 1, d - 2, d - 2);
            MenuWeb.desenharSimbolo(g2, opcao.simbolo, ex + 10, ey + 10, d - 20, Color.WHITE);

            // textos
            int tx = ex + d + 12, larguraTexto = x + w - 12 - tx;
            g2.setColor(ComponentesWeb.TEXTO);
            // nomes compridos ("Prestação de Serviço"): letra menor antes de cortar
            int larguraTitulo = larguraTexto - (activo ? 22 : 0);
            float tamanho = 14.5f;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(tamanho));
            while (tamanho > 12f && g2.getFontMetrics().stringWidth(opcao.titulo) > larguraTitulo) {
                tamanho -= 0.5f;
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(tamanho));
            }
            FontMetrics fm = g2.getFontMetrics();
            String titulo = cortar(opcao.titulo, fm, larguraTitulo);
            g2.drawString(titulo, tx, ey + 16);
            g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            fm = g2.getFontMetrics();
            int ly = ey + 34;
            for (String linha : quebrar(opcao.descricao, fm, larguraTexto, 3)) {
                g2.drawString(linha, tx, ly);
                ly += fm.getHeight();
            }

            // visto do seleccionado
            if (activo) {
                int r = 20, cx = x + w - r - 9, cy = y + 15;
                g2.setColor(vivo);
                g2.fillOval(cx, cy, r, r);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D v = new Path2D.Double();
                v.moveTo(cx + 5.5, cy + 10.5);
                v.lineTo(cx + 8.8, cy + 13.8);
                v.lineTo(cx + 14.5, cy + 7);
                g2.draw(v);
            }
            g2.dispose();
        }
    }

    private static String cortar(String t, FontMetrics fm, int largura) {
        if (fm.stringWidth(t) <= largura) {
            return t;
        }
        while (t.length() > 1 && fm.stringWidth(t + "…") > largura) {
            t = t.substring(0, t.length() - 1);
        }
        return t + "…";
    }

    private static List<String> quebrar(String texto, FontMetrics fm, int largura, int maxLinhas) {
        List<String> linhas = new ArrayList<>();
        StringBuilder linha = new StringBuilder();
        String[] palavras = texto.split(" ");
        for (int i = 0; i < palavras.length; i++) {
            String tentativa = linha.length() == 0 ? palavras[i] : linha + " " + palavras[i];
            if (fm.stringWidth(tentativa) <= largura || linha.length() == 0) {
                linha.setLength(0);
                linha.append(tentativa);
            } else {
                linhas.add(linha.toString());
                linha.setLength(0);
                linha.append(palavras[i]);
                if (linhas.size() == maxLinhas - 1) {
                    StringBuilder resto = new StringBuilder(linha);
                    for (int j = i + 1; j < palavras.length; j++) {
                        resto.append(' ').append(palavras[j]);
                    }
                    linhas.add(cortar(resto.toString(), fm, largura));
                    return linhas;
                }
            }
        }
        if (linha.length() > 0) {
            linhas.add(cortar(linha.toString(), fm, largura));
        }
        return linhas;
    }

    // =====================================================================
    // PRÉ-VISUALIZAÇÃO
    // =====================================================================
    /** Miniatura do sistema com as cores e desenhos do negócio escolhido. */
    private final class Previsualizacao extends JComponent {

        Previsualizacao() {
            setPreferredSize(new Dimension(600, 178));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (seleccionada == null) {
                return;
            }
            Graphics2D g2 = ComponentesWeb.suave(g);
            Opcao o = seleccionada;
            Color[] cores = Tema.amostra(o.tema);
            Color escuro = cores[0], vivo = cores[1], destaque = cores[2];
            Color noite = ComponentesWeb.mistura(escuro, Color.BLACK, 0.35);
            Color campo = Tema.corCampo(o.tema);
            MenuWeb.Simbolo[] modulos = Tema.simbolosModulos(o.tema);
            int w = getWidth() - 2, h = getHeight() - 2, raio = 16;

            // moldura
            g2.setColor(new Color(248, 250, 252));
            g2.fillRoundRect(0, 0, w, h, raio, raio);
            java.awt.Shape antes = g2.getClip();
            g2.clip(new java.awt.geom.RoundRectangle2D.Double(0, 0, w, h, raio, raio));

            // barra lateral
            int lat = Math.min(170, Math.max(120, w / 6));
            g2.setPaint(new GradientPaint(0, 0, escuro, 0, h, noite));
            g2.fillRect(0, 0, lat, h);
            g2.setPaint(new GradientPaint(14, 14, ComponentesWeb.mistura(destaque, Color.WHITE, 0.4), 44, 44, destaque));
            g2.fillRoundRect(14, 14, 30, 30, 9, 9);
            MenuWeb.desenharSimbolo(g2, o.simbolo, 18, 18, 22, noite);
            g2.setColor(Color.WHITE);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
            g2.drawString(cortar(Tema.area(o.tema), g2.getFontMetrics(), lat - 60), 52, 27);
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 150));
            g2.setFont(EstiloUI.FONTE.deriveFont(10f));
            g2.drawString("Kitanda", 52, 40);
            String[] itens = {"Operações", "Clientes", "Relatórios", "Configurações"};
            for (int i = 0; i < itens.length; i++) {
                int iy = 62 + i * 26;
                if (i == 0) {
                    g2.setColor(ComponentesWeb.alfa(Color.WHITE, 34));
                    g2.fillRoundRect(8, iy - 15, lat - 16, 22, 8, 8);
                    g2.setColor(destaque);
                    g2.fillRoundRect(8, iy - 15, 3, 22, 3, 3);
                }
                MenuWeb.desenharSimbolo(g2, modulos[i], 16, iy - 11, 14, ComponentesWeb.alfa(Color.WHITE, 210));
                g2.setColor(ComponentesWeb.alfa(Color.WHITE, i == 0 ? 255 : 180));
                g2.setFont(EstiloUI.FONTE.deriveFont(11f));
                g2.drawString(itens[i], 38, iy);
            }

            // cabeçalho
            int cx = lat, cw = w - lat;
            g2.setPaint(new GradientPaint(cx, 0, escuro, cx + cw, 0, vivo));
            g2.fillRect(cx, 0, cw, 46);
            g2.setColor(Color.WHITE);
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
            g2.drawString("Bom dia!", cx + 18, 22);
            g2.setFont(EstiloUI.FONTE.deriveFont(11f));
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 200));
            g2.drawString(cortar(Tema.descricao(o.tema), g2.getFontMetrics(), cw - 36), cx + 18, 37);

            // cartões dos módulos
            String[] nomes = {"Venda", "Clientes", "Serviços", "Relatórios"};
            int n = cw > 520 ? 4 : cw > 380 ? 3 : 2;
            int espaco = 10, mx = cx + 16, my = 60, mh = 60;
            int larguraCartoes = Math.min(cw - 32, 560);
            int mw = (larguraCartoes - espaco * (n - 1)) / n;
            for (int i = 0; i < n; i++) {
                int kx = mx + i * (mw + espaco);
                g2.setColor(new Color(15, 23, 42, 16));
                g2.fillRoundRect(kx, my + 2, mw, mh, 12, 12);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(kx, my, mw, mh, 12, 12);
                g2.setColor(ComponentesWeb.mistura(vivo, Color.WHITE, 0.86));
                g2.fillRoundRect(kx + 10, my + 12, 34, 34, 10, 10);
                MenuWeb.desenharSimbolo(g2, modulos[i], kx + 16, my + 18, 22, vivo);
                g2.setColor(ComponentesWeb.TEXTO);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
                g2.drawString(cortar(nomes[i], g2.getFontMetrics(), mw - 56), kx + 52, my + 28);
                g2.setColor(ComponentesWeb.mistura(vivo, Color.WHITE, 0.7));
                g2.fillRoundRect(kx + 52, my + 36, Math.max(10, mw - 70), 5, 5, 5);
            }

            // campo de digitação e botão, à direita (ou por baixo, em janelas estreitas)
            int fx = mx + larguraCartoes + 18, fy = 66;
            boolean aoLado = cx + cw - fx >= 180;
            if (!aoLado) {
                fx = mx;
                fy = my + mh + 14;
            }
            int fw = aoLado ? Math.min(cx + cw - fx - 16, 240) : Math.min(cw - 32, 240);
            if (fy + 72 <= h) {
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(10.5f));
                g2.drawString("Código de barras", fx, fy);
                g2.setColor(campo);
                g2.fillRoundRect(fx, fy + 6, fw, 26, 8, 8);
                g2.setColor(ComponentesWeb.mistura(campo, Color.BLACK, 0.25));
                g2.drawRoundRect(fx, fy + 6, fw - 1, 25, 8, 8);
                g2.setColor(ComponentesWeb.TEXTO);
                g2.setFont(EstiloUI.FONTE.deriveFont(11.5f));
                g2.drawString("5601234567890", fx + 10, fy + 23);
                g2.setPaint(new GradientPaint(fx, 0, vivo, fx + fw, 0, ComponentesWeb.mistura(vivo, escuro, 0.35)));
                g2.fillRoundRect(fx, fy + 40, fw, 28, 14, 14);
                g2.setColor(Color.WHITE);
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12f));
                FontMetrics fm = g2.getFontMetrics();
                String b = "Finalizar venda";
                g2.drawString(b, fx + (fw - fm.stringWidth(b)) / 2, fy + 59);
            }

            // legenda
            g2.setClip(antes);
            g2.setColor(ComponentesWeb.LINHA);
            g2.drawRoundRect(0, 0, w, h, raio, raio);
            String legenda = "Pré-visualização · " + o.titulo;
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(10.5f));
            FontMetrics fm = g2.getFontMetrics();
            int lw = fm.stringWidth(legenda) + 18;
            g2.setColor(ComponentesWeb.alfa(Color.WHITE, 235));
            g2.fillRoundRect(w - lw - 10, h - 28, lw, 20, 20, 20);
            g2.setColor(escuro);
            g2.drawString(legenda, w - lw - 1, h - 14);
            g2.dispose();
        }
    }
}
