package visao;

import entity.TbPreco;
import comercial.controller.PrecosController;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.MetodosUtil;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;
import util.ui.MesasWeb;

/**
 * Imagens dos produtos (pratos, bebidas, ...) para os botões do ecrã de
 * pedidos. Arrastar, colar (Ctrl+V) ou escolher um ficheiro; ajustar com o
 * rato (arrastar = mover, roda = zoom); a imagem é recortada já na proporção
 * do botão e gravada pequena (JPEG ~560x320) em tb_produto.photo.
 * Feito à mão, sem .form.
 */
public class ProdutoImagemVisao extends JDialog {

    /** Proporção do espaço da imagem no botão de produto (largura / altura). */
    public static final double PROPORCAO = 1.75;
    private static final int SAIDA_L = 560, SAIDA_A = 320;

    private final BDConexao conexao;
    private final Runnable aoGravar;
    private final PrecosController precosController;
    private final DefaultListModel<Produto> modeloLista = new DefaultListModel<>();
    private final JList<Produto> lista = new JList<>(modeloLista);
    private final List<Produto> todos = new ArrayList<>();
    private final JTextField txtPesquisa = new JTextField();
    private final JRadioButton rbTodos = new JRadioButton("Todos", true);
    private final JRadioButton rbSem = new JRadioButton("Sem foto");
    private final JRadioButton rbCom = new JRadioButton("Com foto");
    private final EditorRecorte editor = new EditorRecorte();
    private final JSlider zoom = new JSlider(25, 400, 100);
    /** WrapLayout: a pré-visualização grande passa para baixo quando não cabe ao lado. */
    private final JPanel previa = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.CENTER, 14, 10));
    private final JLabel lbProduto = new JLabel("Escolha um produto à esquerda");
    private final JButton btGravar = new JButton("Gravar imagem");
    private final JButton btRemover = new JButton("Remover imagem");
    private MesasWeb.CartaoProduto previaPequena;
    private MesasWeb.CartaoProduto previaGrande;
    private Produto actual;

    /** Linha da lista de produtos. */
    private static class Produto {

        int codigo;
        String nome;
        String categoria;
        boolean temImagem;

        @Override
        public String toString() {
            return nome;
        }
    }

    /**
     * @param codigoProduto produto a abrir já escolhido (pode ser null)
     * @param aoGravar chamado depois de gravar ou remover (pode ser null)
     */
    public ProdutoImagemVisao(Frame pai, BDConexao conexao, Integer codigoProduto, Runnable aoGravar) {
        super(pai, "Imagens dos produtos", true);
        this.conexao = conexao;
        this.aoGravar = aoGravar;
        this.precosController = new PrecosController(conexao);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ------------------------------------------------ produtos
        FormularioWeb.campoTexto(txtPesquisa);
        txtPesquisa.setToolTipText("Procurar pelo nome do produto");
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });
        ButtonGroup g = new ButtonGroup();
        g.add(rbTodos);
        g.add(rbSem);
        g.add(rbCom);
        for (JRadioButton rb : new JRadioButton[]{rbTodos, rbSem, rbCom}) {
            rb.addActionListener(e -> filtrar());
        }
        lista.setFixedCellHeight(42);
        lista.setCellRenderer(new RendererProduto());
        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && lista.getSelectedValue() != null) {
                escolher(lista.getSelectedValue());
            }
        });
        JScrollPane spLista = new JScrollPane(lista);
        spLista.setBorder(new javax.swing.border.LineBorder(ComponentesWeb.LINHA, 1, true));
        spLista.setPreferredSize(new Dimension(260, 300));
        JPanel topoLista = ComponentesWeb.transparente(new BorderLayout(0, 6));
        topoLista.add(ComponentesWeb.campo("Procurar produto", txtPesquisa), BorderLayout.NORTH);
        topoLista.add(FormularioWeb.pilulas(rbTodos, rbSem, rbCom), BorderLayout.CENTER);
        JPanel corpoLista = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoLista.add(topoLista, BorderLayout.NORTH);
        corpoLista.add(spLista, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoLista = FormularioWeb.cartao("Produtos do restaurante", ComponentesWeb.AZUL_VIVO, corpoLista);

        // ------------------------------------------------ editor
        JButton btFicheiro = new JButton("Escolher ficheiro…");
        ComponentesWeb.botao(btFicheiro, ComponentesWeb.Variante.PRIMARIO, null, "Abrir uma fotografia do computador");
        btFicheiro.addActionListener(e -> escolherFicheiro());
        JButton btColar = new JButton("Colar (Ctrl+V)");
        ComponentesWeb.botao(btColar, ComponentesWeb.Variante.SECUNDARIO, null, "Colar uma imagem copiada (ex.: do navegador)");
        btColar.addActionListener(e -> colar());
        JButton btInteira = new JButton("Mostrar inteira");
        ComponentesWeb.botao(btInteira, ComponentesWeb.Variante.SECUNDARIO, null,
                "Encaixar a imagem toda no botão (bom para garrafas e copos altos)");
        btInteira.addActionListener(e -> {
            editor.mostrarInteira();
            sincronizarZoom();
        });
        JButton btPreencher = new JButton("Preencher");
        ComponentesWeb.botao(btPreencher, ComponentesWeb.Variante.SECUNDARIO, null, "Encher o botão todo com a imagem (recorta as margens)");
        btPreencher.addActionListener(e -> {
            editor.preencher();
            sincronizarZoom();
        });
        JButton btRodar = new JButton("Rodar");
        ComponentesWeb.botao(btRodar, ComponentesWeb.Variante.SECUNDARIO, null, "Rodar 90° para a direita");
        btRodar.addActionListener(e -> {
            editor.rodar();
            sincronizarZoom();
        });
        JPanel ferramentas = ComponentesWeb.transparente(new EstiloUI.WrapLayout(FlowLayout.LEFT, 6, 4));
        ferramentas.add(btFicheiro);
        ferramentas.add(btColar);
        ferramentas.add(btPreencher);
        ferramentas.add(btInteira);
        ferramentas.add(btRodar);
        zoom.setOpaque(false);
        zoom.setToolTipText("Zoom (também com a roda do rato sobre a imagem)");
        zoom.addChangeListener(e -> {
            // só se o valor mudou de facto (evita arredondar o zoom exacto de "Mostrar inteira")
            if (Math.abs(zoom.getValue() / 100.0 - editor.getZoom()) >= 0.01) {
                editor.setZoom(zoom.getValue() / 100.0);
            }
        });
        JLabel lbZoom = new JLabel("Zoom");
        ComponentesWeb.rotulo(lbZoom);
        JPanel linhaZoom = ComponentesWeb.transparente(new BorderLayout(8, 0));
        linhaZoom.add(lbZoom, BorderLayout.WEST);
        linhaZoom.add(zoom, BorderLayout.CENTER);
        editor.aoMudar = this::actualizarPrevia;
        editor.aoZoomRato = this::sincronizarZoom;
        JPanel corpoEditor = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoEditor.add(ferramentas, BorderLayout.NORTH);
        corpoEditor.add(editor, BorderLayout.CENTER);
        corpoEditor.add(linhaZoom, BorderLayout.SOUTH);
        ComponentesWeb.Cartao cartaoEditor = FormularioWeb.cartao("Ajustar a imagem", ComponentesWeb.LARANJA, corpoEditor);

        // ------------------------------------------------ pré-visualização
        lbProduto.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        lbProduto.setForeground(ComponentesWeb.TEXTO);
        JLabel dicaPrevia = FormularioWeb.dica("<html>É assim que o botão aparece no ecrã de pedidos (tamanho real e maior).</html>");
        JPanel corpoPrevia = ComponentesWeb.transparente(new BorderLayout(0, 10));
        JPanel topoPrevia = ComponentesWeb.transparente(new BorderLayout(0, 4));
        topoPrevia.add(lbProduto, BorderLayout.NORTH);
        topoPrevia.add(dicaPrevia, BorderLayout.CENTER);
        corpoPrevia.add(topoPrevia, BorderLayout.NORTH);
        JPanel previaTopo = ComponentesWeb.transparente(new BorderLayout());
        previaTopo.add(previa, BorderLayout.NORTH);
        corpoPrevia.add(previaTopo, BorderLayout.CENTER);
        ComponentesWeb.Cartao cartaoPrevia = FormularioWeb.cartao("Pré-visualização", ComponentesWeb.VERDE, corpoPrevia);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.25, 0.45, 0.30}, new int[]{250, 380, 300}, 10));
        colunas.add(cartaoLista);
        colunas.add(cartaoEditor);
        colunas.add(cartaoPrevia);

        // ------------------------------------------------ acções
        ComponentesWeb.botao(btGravar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", "Gravar a imagem no produto (Ctrl+S)");
        btGravar.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        btGravar.addActionListener(e -> gravar());
        ComponentesWeb.botao(btRemover, ComponentesWeb.Variante.AVISO, "/imagens/eliminar_16x16.png", "Tirar a imagem (o botão volta a mostrar as iniciais)");
        btRemover.addActionListener(e -> remover());
        JButton btFechar = new JButton("Fechar");
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        btFechar.addActionListener(e -> dispose());
        JLabel dica = FormularioWeb.dica("Arraste uma fotografia para a área de ajuste, ou cole-a com Ctrl+V. Arraste para mover, roda do rato para o zoom.");
        JPanel cabecalho = FormularioWeb.cabecalho("Imagens dos produtos",
                "Fotografias de pratos, bebidas e outros produtos, já à medida dos botões", null);
        FormularioWeb.montar(this, cabecalho, colunas, FormularioWeb.barraAccoes(dica, btFechar, btRemover, btGravar),
                "Imagens dos produtos");

        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> colar(), KeyStroke.getKeyStroke(KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> gravar(), KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // largar ficheiros ou imagens em qualquer ponto do editor (e da janela)
        TransferHandler largar = new LargarImagem();
        editor.setTransferHandler(largar);
        lista.setTransferHandler(largar);

        carregarProdutos();
        filtrar();
        if (codigoProduto != null) {
            for (int i = 0; i < modeloLista.size(); i++) {
                if (modeloLista.get(i).codigo == codigoProduto) {
                    lista.setSelectedIndex(i);
                    lista.ensureIndexIsVisible(i);
                    break;
                }
            }
        }
        actualizarBotoes();
        FormularioWeb.ajustar(this, 1280, 740, 820, 560);
    }

    // =====================================================================
    // PRODUTOS
    // =====================================================================
    private void carregarProdutos() {
        todos.clear();
        String sql = "SELECT p.codigo, p.designacao, t.designacao AS categoria, (p.photo IS NOT NULL AND LENGTH(p.photo) > 0) AS tem"
                + " FROM tb_produto p JOIN tb_tipo_produto t ON t.codigo = p.cod_Tipo_Produto"
                + " ORDER BY p.designacao"; // o comercial não separa as categorias por área: todos os produtos
        try (PreparedStatement ps = conexao.getPreparedStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.codigo = rs.getInt(1);
                p.nome = rs.getString(2);
                p.categoria = rs.getString(3);
                p.temImagem = rs.getBoolean(4);
                todos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrar() {
        String t = txtPesquisa.getText().trim().toUpperCase();
        Produto antes = lista.getSelectedValue();
        modeloLista.clear();
        for (Produto p : todos) {
            boolean ok = (t.isEmpty() || p.nome.toUpperCase().contains(t)
                    || (p.categoria != null && p.categoria.toUpperCase().contains(t)))
                    && (rbTodos.isSelected() || (rbSem.isSelected() && !p.temImagem) || (rbCom.isSelected() && p.temImagem));
            if (ok) {
                modeloLista.addElement(p);
            }
        }
        if (antes != null) {
            lista.setSelectedValue(antes, true);
        }
    }

    private void escolher(Produto p) {
        actual = p;
        lbProduto.setText(p.nome);
        BufferedImage img = null;
        try (PreparedStatement ps = conexao.getPreparedStatement("SELECT photo FROM tb_produto WHERE codigo = ?")) {
            ps.setInt(1, p.codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] b = rs.getBytes(1);
                    if (b != null && b.length > 0) {
                        img = ImageIO.read(new ByteArrayInputStream(b));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.setImagem(img);
        editor.alterado = false;
        sincronizarZoom();
        montarPrevia();
        actualizarBotoes();
    }

    private void montarPrevia() {
        previa.removeAll();
        if (actual == null) {
            return;
        }
        String preco = "—";
        try {
            TbPreco tp = precosController.getLastIdPrecoByIdProdutos(actual.codigo);
            double pv = tp != null && tp.getPrecoVenda() != null ? tp.getPrecoVenda().doubleValue() : 0;
            preco = CfMethods.formatarComoMoeda(util.FinanceUtils.getValorComIVA(1, MetodosUtil.getTaxaPercantagem(actual.codigo), pv, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Color cor = MesasWeb.corCategoria(actual.categoria);
        previaPequena = new MesasWeb.CartaoProduto(actual.nome, preco, null, false, null, cor, null);
        previaGrande = new MesasWeb.CartaoProduto(actual.nome, preco, null, false, null, cor, null);
        previaPequena.setPreferredSize(new Dimension(160, 176));
        previaGrande.setPreferredSize(new Dimension(230, 250));
        previa.add(previaPequena);
        previa.add(previaGrande);
        actualizarPrevia();
        previa.revalidate();
        previa.repaint();
    }

    private void actualizarPrevia() {
        Image saida = editor.temImagem() ? editor.recorte(SAIDA_L, SAIDA_A) : null;
        if (previaPequena != null) {
            previaPequena.setFoto(saida);
            previaGrande.setFoto(saida);
        }
        actualizarBotoes();
    }

    private void actualizarBotoes() {
        btGravar.setEnabled(actual != null && editor.temImagem());
        btRemover.setEnabled(actual != null && actual.temImagem);
    }

    private void sincronizarZoom() {
        zoom.setValue((int) Math.round(editor.getZoom() * 100));
    }

    // =====================================================================
    // ENTRADA DE IMAGENS
    // =====================================================================
    private void escolherFicheiro() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Imagens (jpg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            abrirFicheiro(fc.getSelectedFile());
        }
    }

    private boolean abrirFicheiro(File f) {
        try {
            BufferedImage img = ImageIO.read(f);
            if (img == null) {
                JOptionPane.showMessageDialog(this, "O ficheiro não é uma imagem que se consiga abrir:\n" + f.getName(),
                        "Imagens dos produtos", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            receber(img);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível abrir a imagem:\n" + e.getMessage(),
                    "Imagens dos produtos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void colar() {
        try {
            Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
            if (!new LargarImagem().importar(cb.getContents(null))) {
                JOptionPane.showMessageDialog(this, "A área de transferência não tem nenhuma imagem.\n"
                        + "No navegador: botão direito na fotografia → \"Copiar imagem\".",
                        "Imagens dos produtos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Recebe uma imagem nova (ficheiro, colar, largar). */
    public void receber(Image img) {
        if (actual == null) {
            JOptionPane.showMessageDialog(this, "Escolha primeiro o produto na lista à esquerda.",
                    "Imagens dos produtos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        editor.setImagem(paraBuffered(img));
        editor.alterado = true;
        sincronizarZoom();
        actualizarPrevia();
    }

    private class LargarImagem extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport s) {
            return s.isDataFlavorSupported(DataFlavor.javaFileListFlavor) || s.isDataFlavorSupported(DataFlavor.imageFlavor);
        }

        @Override
        public boolean importData(TransferSupport s) {
            return importar(s.getTransferable());
        }

        @SuppressWarnings("unchecked")
        boolean importar(Transferable t) {
            try {
                if (t == null) {
                    return false;
                }
                if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    receber((Image) t.getTransferData(DataFlavor.imageFlavor));
                    return true;
                }
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    List<File> fs = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    return !fs.isEmpty() && abrirFicheiro(fs.get(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    static BufferedImage paraBuffered(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        new javax.swing.ImageIcon(img); // espera pelo carregamento
        BufferedImage b = new BufferedImage(Math.max(1, img.getWidth(null)), Math.max(1, img.getHeight(null)), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = b.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return b;
    }

    // =====================================================================
    // GRAVAR
    // =====================================================================
    private void gravar() {
        if (actual == null || !editor.temImagem()) {
            return;
        }
        try {
            byte[] jpeg = jpeg(editor.recorte(SAIDA_L, SAIDA_A), 0.88f);
            gravarFoto(conexao, actual.codigo, jpeg);
            actual.temImagem = true;
            editor.alterado = false;
            lista.repaint();
            actualizarBotoes();
            if (aoGravar != null) {
                aoGravar.run();
            }
            util.ui.ConfirmacaoPositiva.mostrar("Imagens dos produtos", "Imagem gravada",
                    actual.nome + " já aparece com a fotografia no ecrã de pedidos.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível gravar a imagem:\n" + e.getMessage(),
                    "Imagens dos produtos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remover() {
        if (actual == null || !actual.temImagem) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Tirar a imagem de " + actual.nome + "?", "Imagens dos produtos",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            gravarFoto(conexao, actual.codigo, null);
            actual.temImagem = false;
            editor.setImagem(null);
            actualizarPrevia();
            lista.repaint();
            if (aoGravar != null) {
                aoGravar.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível remover a imagem:\n" + e.getMessage(),
                    "Imagens dos produtos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Grava (ou apaga, com null) a fotografia do produto. */
    public static void gravarFoto(BDConexao conexao, int codigoProduto, byte[] jpeg) throws Exception {
        try (PreparedStatement ps = conexao.getPreparedStatement("UPDATE tb_produto SET photo = ? WHERE codigo = ?")) {
            if (jpeg == null) {
                ps.setNull(1, java.sql.Types.BLOB);
            } else {
                ps.setBytes(1, jpeg);
            }
            ps.setInt(2, codigoProduto);
            if (ps.executeUpdate() != 1) {
                throw new Exception("O produto " + codigoProduto + " não foi encontrado.");
            }
        }
    }

    /** JPEG com a qualidade indicada (0..1). */
    public static byte[] jpeg(BufferedImage img, float qualidade) throws Exception {
        BufferedImage rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();
        ImageWriter w = ImageIO.getImageWritersByFormatName("jpg").next();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
            w.setOutput(ios);
            ImageWriteParam p = w.getDefaultWriteParam();
            p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            p.setCompressionQuality(qualidade);
            w.write(null, new IIOImage(rgb, null, null), p);
        } finally {
            w.dispose();
        }
        return out.toByteArray();
    }

    /**
     * Prepara uma imagem já recortada para o botão (sem editor): encaixa a
     * imagem toda (fundo branco) e devolve o JPEG. Usado em testes e
     * importações.
     */
    public static byte[] imagemParaBotao(BufferedImage img, boolean inteira) throws Exception {
        EditorRecorte e = new EditorRecorte();
        e.setSize(SAIDA_L + 40, SAIDA_A + 40);
        e.setImagem(img);
        if (inteira) {
            e.mostrarInteira();
        }
        return jpeg(e.recorte(SAIDA_L, SAIDA_A), 0.88f);
    }

    // =====================================================================
    // LISTA
    // =====================================================================
    private class RendererProduto extends JComponent implements javax.swing.ListCellRenderer<Produto> {

        private Produto p;
        private boolean sel;

        @Override
        public Component getListCellRendererComponent(JList<? extends Produto> l, Produto v, int i, boolean s, boolean f) {
            p = v;
            sel = s;
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth(), h = getHeight();
            if (sel) {
                g2.setColor(new Color(219, 234, 254));
                g2.fillRoundRect(2, 2, w - 4, h - 4, 10, 10);
            }
            Color cor = MesasWeb.corCategoria(p.categoria);
            g2.setColor(p.temImagem ? ComponentesWeb.VERDE : ComponentesWeb.alfa(cor, 60));
            g2.fillRoundRect(8, 9, 34, h - 18, 8, 8);
            if (p.temImagem) {
                // visto desenhado (o carácter ✓ não existe em todas as letras do Windows)
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int mx = 8 + 17, my = h / 2;
                g2.drawPolyline(new int[]{mx - 7, mx - 2, mx + 7}, new int[]{my, my + 5, my - 5}, 3);
                g2.setStroke(new BasicStroke(1f));
            } else {
                g2.setColor(cor.darker());
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
                String s = iniciais(p.nome);
                g2.drawString(s, 8 + (34 - g2.getFontMetrics().stringWidth(s)) / 2, h / 2 + 5);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(12.5f));
            g2.setColor(ComponentesWeb.TEXTO);
            g2.drawString(p.nome, 52, 18);
            g2.setFont(EstiloUI.FONTE.deriveFont(11f));
            g2.setColor(ComponentesWeb.TEXTO_SUAVE);
            g2.drawString((p.categoria == null ? "" : p.categoria) + (p.temImagem ? " · com imagem" : ""), 52, 33);
            g2.dispose();
        }

        private String iniciais(String n) {
            StringBuilder sb = new StringBuilder();
            for (String x : n.trim().split("\\s+")) {
                if (!x.isEmpty() && Character.isLetterOrDigit(x.charAt(0)) && sb.length() < 2) {
                    sb.append(Character.toUpperCase(x.charAt(0)));
                }
            }
            return sb.toString();
        }
    }

    // =====================================================================
    // EDITOR DE RECORTE
    // =====================================================================
    /**
     * Mostra a imagem com uma moldura na proporção do botão: o que está
     * dentro da moldura é o que fica no botão. Arrastar move, a roda do rato
     * (ou o controlo de zoom) aproxima. Com zoom abaixo de "preencher" a
     * imagem cabe toda e as margens ficam brancas.
     */
    static class EditorRecorte extends JComponent {

        private BufferedImage img;
        /** Centro da moldura, em coordenadas da imagem. */
        private double cx, cy;
        /** 1 = preencher a moldura (cover). */
        private double zoom = 1;
        private int rx, ry;
        boolean alterado;
        Runnable aoMudar;
        Runnable aoZoomRato;

        EditorRecorte() {
            setPreferredSize(new Dimension(420, 300));
            setMinimumSize(new Dimension(260, 180));
            setFocusable(true);
            MouseAdapter m = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    rx = e.getX();
                    ry = e.getY();
                    requestFocusInWindow();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (img == null) {
                        return;
                    }
                    double s = escala();
                    cx -= (e.getX() - rx) / s;
                    cy -= (e.getY() - ry) / s;
                    rx = e.getX();
                    ry = e.getY();
                    mudou();
                }

                @Override
                public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                    if (img == null) {
                        return;
                    }
                    setZoom(zoom * (e.getWheelRotation() < 0 ? 1.08 : 1 / 1.08));
                    if (aoZoomRato != null) {
                        aoZoomRato.run();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (img == null && e.getClickCount() == 1) {
                        // vazio: o clique abre o ficheiro (o botão "Escolher ficheiro" faz o mesmo)
                        Component c = EditorRecorte.this;
                        while (c != null && !(c instanceof ProdutoImagemVisao)) {
                            c = c.getParent();
                        }
                        if (c != null) {
                            ((ProdutoImagemVisao) c).escolherFicheiro();
                        }
                    }
                }
            };
            addMouseListener(m);
            addMouseMotionListener(m);
            addMouseWheelListener(m);
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        boolean temImagem() {
            return img != null;
        }

        void setImagem(BufferedImage nova) {
            // fotografias muito grandes ficam com no máximo 1600 px (desenho rápido e boa qualidade)
            if (nova != null && Math.max(nova.getWidth(), nova.getHeight()) > 1600) {
                double k = 1600.0 / Math.max(nova.getWidth(), nova.getHeight());
                nova = reduzir(nova, (int) Math.round(nova.getWidth() * k), (int) Math.round(nova.getHeight() * k));
            }
            img = nova;
            if (img != null) {
                cx = img.getWidth() / 2.0;
                cy = img.getHeight() / 2.0;
                zoom = 1;
            }
            mudou();
        }

        double getZoom() {
            return zoom;
        }

        void setZoom(double z) {
            zoom = Math.max(0.25, Math.min(4, z));
            mudou();
        }

        void preencher() {
            if (img != null) {
                cx = img.getWidth() / 2.0;
                cy = img.getHeight() / 2.0;
                setZoom(1);
            }
        }

        void mostrarInteira() {
            if (img == null) {
                return;
            }
            Rectangle2D f = moldura();
            double base = Math.max(f.getWidth() / img.getWidth(), f.getHeight() / img.getHeight());
            double cabe = Math.min(f.getWidth() / img.getWidth(), f.getHeight() / img.getHeight()) * 0.94;
            cx = img.getWidth() / 2.0;
            cy = img.getHeight() / 2.0;
            setZoom(cabe / base);
        }

        void rodar() {
            if (img == null) {
                return;
            }
            BufferedImage r = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = r.createGraphics();
            g.translate(img.getHeight(), 0);
            g.rotate(Math.PI / 2);
            g.drawImage(img, 0, 0, null);
            g.dispose();
            setImagem(r);
            alterado = true;
        }

        private void mudou() {
            repaint();
            if (aoMudar != null) {
                aoMudar.run();
            }
        }

        /** Moldura (proporção do botão) centrada no componente. */
        Rectangle2D moldura() {
            int w = Math.max(getWidth(), 100), h = Math.max(getHeight(), 80);
            double mw = w - 40, mh = mw / PROPORCAO;
            if (mh > h - 40) {
                mh = h - 40;
                mw = mh * PROPORCAO;
            }
            return new Rectangle2D.Double((w - mw) / 2, (h - mh) / 2, mw, mh);
        }

        /** Píxeis do ecrã por píxel da imagem. */
        double escala() {
            Rectangle2D f = moldura();
            return Math.max(f.getWidth() / img.getWidth(), f.getHeight() / img.getHeight()) * zoom;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(241, 245, 249));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);
            Rectangle2D f = moldura();
            if (img == null) {
                g2.setColor(ComponentesWeb.CINZA);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{8f, 6f}, 0f));
                g2.draw(new RoundRectangle2D.Double(f.getX(), f.getY(), f.getWidth(), f.getHeight(), 16, 16));
                g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
                g2.setColor(ComponentesWeb.TEXTO_SUAVE);
                String[] linhas = {"Arraste uma fotografia para aqui", "ou cole com Ctrl+V", "ou clique para escolher o ficheiro"};
                int y = (int) f.getCenterY() - 22;
                for (String l : linhas) {
                    g2.drawString(l, (w - g2.getFontMetrics().stringWidth(l)) / 2, y);
                    y += 22;
                    g2.setFont(EstiloUI.FONTE.deriveFont(13f));
                }
                g2.dispose();
                return;
            }
            double s = escala();
            double x = f.getCenterX() - cx * s, y = f.getCenterY() - cy * s;
            // fundo branco da moldura (aparece se a imagem não a encher)
            g2.setColor(Color.WHITE);
            g2.fill(f);
            g2.drawImage(img, (int) Math.round(x), (int) Math.round(y),
                    (int) Math.round(img.getWidth() * s), (int) Math.round(img.getHeight() * s), null);
            // escurece o que fica fora do botão
            Area fora = new Area(new Rectangle(0, 0, w, h));
            fora.subtract(new Area(f));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
            g2.setColor(new Color(15, 23, 42));
            g2.fill(fora);
            g2.setComposite(AlphaComposite.SrcOver);
            // moldura e terços
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(f);
            g2.setColor(new Color(255, 255, 255, 110));
            g2.setStroke(new BasicStroke(1f));
            for (int i = 1; i < 3; i++) {
                double gx = f.getX() + f.getWidth() * i / 3, gy = f.getY() + f.getHeight() * i / 3;
                g2.drawLine((int) gx, (int) f.getY(), (int) gx, (int) f.getMaxY());
                g2.drawLine((int) f.getX(), (int) gy, (int) f.getMaxX(), (int) gy);
            }
            g2.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
            g2.setColor(Color.WHITE);
            g2.drawString("Área do botão", (int) f.getX() + 6, (int) f.getY() - 6);
            g2.dispose();
        }

        /** O que está dentro da moldura, com o tamanho pedido. */
        BufferedImage recorte(int largura, int altura) {
            BufferedImage out = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = out.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, largura, altura);
            if (img != null) {
                Rectangle2D f = moldura();
                double k = largura / f.getWidth();
                double s = escala() * k;
                double x = largura / 2.0 - cx * s, y = altura / 2.0 - cy * s;
                int dw = (int) Math.round(img.getWidth() * s), dh = (int) Math.round(img.getHeight() * s);
                BufferedImage fonte = dw < img.getWidth() / 2 ? reduzir(img, Math.max(1, dw), Math.max(1, dh)) : img;
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.drawImage(fonte, (int) Math.round(x), (int) Math.round(y), dw, dh, null);
            }
            g.dispose();
            return out;
        }

        /** Redução em passos de metade (evita o serrilhado numa redução grande). */
        static BufferedImage reduzir(BufferedImage src, int w, int h) {
            BufferedImage atual = src;
            int cw = src.getWidth(), ch = src.getHeight();
            do {
                cw = Math.max(w, cw / 2);
                ch = Math.max(h, ch / 2);
                BufferedImage tmp = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = tmp.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(atual, 0, 0, cw, ch, null);
                g.dispose();
                atual = tmp;
            } while (cw != w || ch != h);
            return atual;
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ProdutoImagemVisao(null, BDConexao.getInstancia(), 12, null).setVisible(true));
    }
}
