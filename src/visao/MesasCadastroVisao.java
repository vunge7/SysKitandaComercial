package visao;

import comercial.controller.MesasController;
import comercial.controller.MesasController.EstadoMesa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;
import util.ui.MesasWeb;

/**
 * Cadastro das mesas da sala (tb_mesas). As mesas criadas aqui aparecem logo
 * no plano de mesas (PrincipalPedidosVisao), com o mesmo desenho, sem mexer
 * no código: o plano lê as mesas da base de dados.
 *
 * Formulário feito à mão (sem .form), no estilo web dos outros formulários.
 */
public class MesasCadastroVisao extends JDialog {

    private static final int MAX_POR_VEZ = 100;

    private final MesasController mesasController;
    private final String areaPedido;
    private final Runnable aoAlterar;

    private final JRadioButton rbUma = new JRadioButton("Uma mesa", true);
    private final JRadioButton rbVarias = new JRadioButton("Várias seguidas");
    private final JSpinner spNumero = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JSpinner spAte = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JPanel campoAte;
    private final JLabel lbResumo = new JLabel(" ");
    private final MesasWeb.CartaoMesa preVisualizacao;
    private final MesasWeb.PainelGrelha grelha = new MesasWeb.PainelGrelha(118, 10);
    private final ComponentesWeb.Cartao cartaoLista;
    private final JButton btSalvar = new JButton("Salvar mesa");
    private final JButton btEliminar = new JButton("Eliminar mesa");
    private final JButton btFechar = new JButton("Fechar");
    private final List<MesasWeb.CartaoMesa> cartoes = new ArrayList<>();
    private MesasWeb.CartaoMesa escolhida;

    /**
     * @param areaPedido área dos pedidos (para mostrar quais estão ocupadas)
     * @param aoAlterar chamado depois de criar ou eliminar mesas (pode ser null)
     */
    public MesasCadastroVisao(Frame pai, BDConexao conexao, String areaPedido, Runnable aoAlterar) {
        super(pai, "Mesas da sala", true);
        this.mesasController = new MesasController(conexao);
        this.areaPedido = areaPedido;
        this.aoAlterar = aoAlterar;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        EstadoMesa exemplo = new EstadoMesa();
        exemplo.designacao = "MESA 1";
        preVisualizacao = new MesasWeb.CartaoMesa(exemplo);
        preVisualizacao.setPreferredSize(new Dimension(170, 190));
        preVisualizacao.setFocusable(false);

        // ------------------------------------------------ nova mesa
        javax.swing.ButtonGroup grupo = new javax.swing.ButtonGroup();
        grupo.add(rbUma);
        grupo.add(rbVarias);
        JPanel modo = FormularioWeb.pilulas(rbUma, rbVarias);
        FormularioWeb.spinner(spNumero);
        FormularioWeb.spinner(spAte);
        spNumero.setPreferredSize(new Dimension(110, 34));
        spAte.setPreferredSize(new Dimension(110, 34));
        ((JSpinner.DefaultEditor) spNumero.getEditor()).getTextField().setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));
        ((JSpinner.DefaultEditor) spAte.getEditor()).getTextField().setFont(EstiloUI.FONTE_NEGRITO.deriveFont(16f));

        JPanel numeros = ComponentesWeb.transparente(new GridLayout(1, 2, 10, 0));
        numeros.add(ComponentesWeb.campo("Número da mesa", spNumero));
        campoAte = ComponentesWeb.campo("Até à mesa", spAte);
        numeros.add(campoAte);

        lbResumo.setFont(EstiloUI.FONTE.deriveFont(12.5f));
        lbResumo.setForeground(ComponentesWeb.TEXTO_SUAVE);

        JLabel tituloPrevia = new JLabel("Assim vai aparecer no plano de mesas");
        ComponentesWeb.rotulo(tituloPrevia);
        JPanel previa = ComponentesWeb.transparente(new BorderLayout(0, 6));
        previa.add(tituloPrevia, BorderLayout.NORTH);
        JPanel centroPrevia = ComponentesWeb.transparente(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centroPrevia.add(preVisualizacao);
        previa.add(centroPrevia, BorderLayout.CENTER);

        ComponentesWeb.botao(btSalvar, ComponentesWeb.Variante.SUCESSO, "/imagens/salvar_16x16.png", "Gravar a mesa nova (Enter)");
        JPanel linhaSalvar = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaSalvar.add(btSalvar);

        JPanel formulario = FormularioWeb.pilha(12, modo, numeros, lbResumo, linhaSalvar, previa);
        ComponentesWeb.Cartao cartaoNova = FormularioWeb.cartao("Nova mesa", ComponentesWeb.VERDE, formulario);

        // ------------------------------------------------ mesas existentes
        JScrollPane rolagem = new JScrollPane(grelha, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagem.setBorder(null);
        rolagem.setOpaque(false);
        rolagem.getViewport().setOpaque(false);
        rolagem.getVerticalScrollBar().setUnitIncrement(24);
        JPanel legenda = ComponentesWeb.transparente(new FlowLayout(FlowLayout.LEFT, 14, 0));
        legenda.add(MesasWeb.legenda("Livre", MesasWeb.LIVRE));
        legenda.add(MesasWeb.legenda("Ocupada", MesasWeb.OCUPADA));
        legenda.add(FormularioWeb.dica("Clique numa mesa para a escolher"));
        JPanel corpoLista = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpoLista.add(legenda, BorderLayout.NORTH);
        corpoLista.add(rolagem, BorderLayout.CENTER);
        cartaoLista = new ComponentesWeb.Cartao("Mesas existentes", ComponentesWeb.AZUL_VIVO, new BorderLayout());
        cartaoLista.getCorpo().add(corpoLista, BorderLayout.CENTER);
        ComponentesWeb.botao(btEliminar, ComponentesWeb.Variante.PERIGO, "/imagens/eliminar_16x16.png",
                "Eliminar a mesa escolhida (só mesas que nunca tiveram pedidos)");
        btEliminar.putClientProperty("web.pilula", Boolean.TRUE);
        btEliminar.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 14));
        btEliminar.setEnabled(false);
        cartaoLista.adicionarAccao(btEliminar);

        JPanel colunas = ComponentesWeb.transparente(new ComponentesWeb.ColunasResponsivas(
                new double[]{0.0, 1.0}, new int[]{300, 360}, 10));
        colunas.add(cartaoNova);
        colunas.add(cartaoLista);

        // ------------------------------------------------ janela
        FormularioWeb.botaoSair(btFechar, "Fechar (Esc)");
        JPanel cabecalho = FormularioWeb.cabecalho("Mesas da sala",
                "As mesas criadas aqui aparecem logo no plano de mesas", null);
        JPanel accoes = FormularioWeb.barraAccoes(
                FormularioWeb.dica("Uma mesa só pode ser eliminada se nunca teve pedidos nem vendas."), btFechar);
        FormularioWeb.montar(this, cabecalho, colunas, accoes, "Gestão de mesas");

        // ------------------------------------------------ eventos
        rbUma.addActionListener(e -> actualizarModo());
        rbVarias.addActionListener(e -> actualizarModo());
        spNumero.addChangeListener(e -> {
            if ((Integer) spAte.getValue() < (Integer) spNumero.getValue()) {
                spAte.setValue(spNumero.getValue());
            }
            actualizarPrevia();
        });
        spAte.addChangeListener(e -> actualizarPrevia());
        btSalvar.addActionListener(e -> salvar());
        btEliminar.addActionListener(e -> eliminar());
        btFechar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btSalvar);
        getRootPane().registerKeyboardAction(e -> dispose(),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        carregar();
        spNumero.setValue(proximoNumero());
        actualizarModo();
        FormularioWeb.ajustar(this, 1080, 700, 720, 520);
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private void carregar() {
        String designacaoEscolhida = escolhida == null ? null : escolhida.getEstado().designacao;
        escolhida = null;
        btEliminar.setEnabled(false);
        grelha.removeAll();
        cartoes.clear();
        List<EstadoMesa> lista;
        try {
            lista = mesasController.estadoMesas();
        } catch (Exception e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            for (entity.TbMesas m : mesasController.listarOrdenadas()) {
                EstadoMesa em = new EstadoMesa();
                em.codigo = m.getPkMesas();
                em.designacao = m.getDesignacao();
                lista.add(em);
            }
        }
        for (EstadoMesa em : lista) {
            MesasWeb.CartaoMesa c = new MesasWeb.CartaoMesa(em);
            c.setPreferredSize(new Dimension(118, 140));
            c.setTextoLivre("Clique para escolher");
            c.setToolTipText(em.designacao + " · clique para a escolher");
            c.addAccao(() -> escolher(c));
            cartoes.add(c);
            grelha.add(c);
            if (em.designacao.equalsIgnoreCase(designacaoEscolhida)) {
                escolher(c);
            }
        }
        cartaoLista.getTitulo().setText("Mesas existentes (" + lista.size() + ")");
        grelha.revalidate();
        grelha.repaint();
    }

    private int proximoNumero() {
        int maior = 0;
        for (MesasWeb.CartaoMesa c : cartoes) {
            try {
                maior = Math.max(maior, Integer.parseInt(c.getNumero()));
            } catch (NumberFormatException ignorado) {
                // designação livre (ex.: "MESA VIP"): não conta
            }
        }
        return maior + 1;
    }

    private boolean existe(int numero) {
        for (MesasWeb.CartaoMesa c : cartoes) {
            if (c.getNumero().equals(String.valueOf(numero))) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // INTERFACE
    // =====================================================================
    private void actualizarModo() {
        boolean varias = rbVarias.isSelected();
        campoAte.setVisible(varias);
        btSalvar.setText(varias ? "Salvar mesas" : "Salvar mesa");
        actualizarPrevia();
        revalidate();
    }

    private void actualizarPrevia() {
        int de = (Integer) spNumero.getValue();
        int ate = rbVarias.isSelected() ? Math.max(de, (Integer) spAte.getValue()) : de;
        EstadoMesa e = new EstadoMesa();
        e.designacao = "MESA " + de;
        preVisualizacao.setEstado(e);
        int novas = 0, repetidas = 0;
        for (int n = de; n <= ate; n++) {
            if (existe(n)) {
                repetidas++;
            } else {
                novas++;
            }
        }
        String texto;
        Color cor = ComponentesWeb.TEXTO_SUAVE;
        if (ate - de + 1 > MAX_POR_VEZ) {
            texto = "No máximo " + MAX_POR_VEZ + " mesas de cada vez.";
            cor = ComponentesWeb.VERMELHO;
        } else if (novas == 0) {
            texto = de == ate ? "A MESA " + de + " já existe." : "Todas estas mesas já existem.";
            cor = ComponentesWeb.VERMELHO;
        } else if (de == ate) {
            texto = "Vai ser criada a MESA " + de + ".";
        } else {
            texto = "Vão ser criadas " + novas + " mesas (MESA " + de + " a MESA " + ate + ")"
                    + (repetidas > 0 ? "; " + repetidas + " já existem e ficam como estão." : ".");
        }
        lbResumo.setText("<html>" + texto + "</html>");
        lbResumo.setForeground(cor);
        btSalvar.setEnabled(novas > 0 && ate - de + 1 <= MAX_POR_VEZ);
    }

    private void escolher(MesasWeb.CartaoMesa c) {
        if (escolhida != null) {
            escolhida.setSeleccionado(false);
        }
        escolhida = c;
        c.setSeleccionado(true);
        btEliminar.setEnabled(true);
        btEliminar.setText("Eliminar " + c.getEstado().designacao);
    }

    // =====================================================================
    // ACÇÕES
    // =====================================================================
    private void salvar() {
        int de = (Integer) spNumero.getValue();
        int ate = rbVarias.isSelected() ? Math.max(de, (Integer) spAte.getValue()) : de;
        if (ate - de + 1 > MAX_POR_VEZ) {
            return;
        }
        int criadas = 0;
        List<String> falhas = new ArrayList<>();
        for (int n = de; n <= ate; n++) {
            if (existe(n)) {
                continue;
            }
            if (mesasController.criarMesa("MESA " + n)) {
                criadas++;
            } else {
                falhas.add("MESA " + n);
            }
        }
        carregar();
        if (criadas > 0) {
            notificar();
        }
        if (!falhas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não foi possível criar: " + String.join(", ", falhas)
                    + "\n(pode já existir uma mesa com esse nome).", "Mesas", JOptionPane.WARNING_MESSAGE);
        }
        spNumero.setValue(proximoNumero());
        actualizarPrevia();
        if (criadas > 0) {
            util.ui.ConfirmacaoPositiva.mostrar("Mesas", criadas == 1 ? "Mesa criada" : criadas + " mesas criadas",
                    criadas == 1 ? "Já está no plano de mesas." : "Já estão no plano de mesas.");
        }
    }

    private void eliminar() {
        if (escolhida == null) {
            return;
        }
        EstadoMesa em = escolhida.getEstado();
        int usos = mesasController.contarUtilizacoes(em.codigo);
        if (usos > 0) {
            JOptionPane.showMessageDialog(this,
                    em.designacao + " já tem pedidos ou vendas registados e não pode ser eliminada:\n"
                    + "o histórico (facturas, relatórios) continua a apontar para ela.",
                    "Mesas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Eliminar a " + em.designacao + "?",
                "Mesas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        if (mesasController.eliminarSeLivre(em.codigo)) {
            escolhida = null;
            carregar();
            notificar();
            spNumero.setValue(proximoNumero());
            actualizarPrevia();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível eliminar a " + em.designacao + ".",
                    "Mesas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void notificar() {
        if (aoAlterar != null) {
            aoAlterar.run();
        }
    }
}
