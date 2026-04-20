package visao;

import comercial.controller.ItemPedidosController;
import enties.util.CozinhaPedido;
import util.BDConexao;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCozinhaKDS extends JFrame {

    private JPanel painelCozinha;
    private ItemPedidosController controller;
    private int ultimaQuantidadePedidos = 0;

    public TelaCozinhaKDS(ItemPedidosController controller) {
        this.controller = controller;

        setTitle("Kitchen Display System - Cozinha");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarInterface();
        carregarCozinhaVisual();

        // Atualiza a cada 4 segundos
        new Timer(4000, e -> carregarCozinhaVisual()).start();
    }

    // ================= INTERFACE =================
    private void criarInterface() {

        // Barra superior
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(45, 45, 45));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("PEDIDOS DA COZINHA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        JLabel data = new JLabel(java.time.LocalDate.now().toString());
        data.setForeground(Color.LIGHT_GRAY);

        topo.add(titulo, BorderLayout.WEST);
        topo.add(data, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);

        // Painel dos cartões
        painelCozinha = new JPanel();
        painelCozinha.setLayout(new GridLayout(0, 4, 20, 20));
        painelCozinha.setBackground(new Color(30, 30, 30));
        painelCozinha.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(painelCozinha);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    // ================= CARTÃO =================
    private JPanel criarCartao(CozinhaPedido c) {

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 190));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3));

        // Topo
        JLabel pedido = new JLabel("PEDIDO #" + c.getPkItemPedidos());
        pedido.setFont(new Font("Segoe UI", Font.BOLD, 25));
        pedido.setForeground(new Color(30, 60, 120));

        JLabel mesa = new JLabel(c.getMesa() + " | " + c.getLugar());
        mesa.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.setOpaque(false);
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        topo.add(pedido);
        topo.add(mesa);

        // Produtos
        JTextArea itens = new JTextArea();
        itens.setEditable(false);
        itens.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        itens.setOpaque(false);
        itens.setLineWrap(true);
        itens.setWrapStyleWord(true);
        itens.setText("• " + c.getProduto() + "   x" + c.getQuantidade());
        itens.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Rodapé
        JLabel status = new JLabel("AGUARDANDO PREPARO");
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        status.setForeground(new Color(0, 120, 215));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        rodape.add(status);

        card.add(topo, BorderLayout.NORTH);
        card.add(itens, BorderLayout.CENTER);
        card.add(rodape, BorderLayout.SOUTH);

        return card;
    }

    // ================= CARREGAMENTO =================
//    private void carregarCozinhaVisual()
//    {
//
//        painelCozinha.removeAll();
//
//        List<CozinhaPedido> lista = controller.listarPedidosCozinha();
//
//        for ( CozinhaPedido c : lista )
//        {
//            painelCozinha.add( criarCartao( c ) );
//        }
//
//        painelCozinha.revalidate();
//        painelCozinha.repaint();
//    }

    private void carregarCozinhaVisual() {
        List<CozinhaPedido> lista = controller.listarPedidosCozinha();

        // 🔊 Verifica se chegou pedido novo
        if (lista.size() > ultimaQuantidadePedidos) {
            SomAlerta.tocar();
        }

        ultimaQuantidadePedidos = lista.size();

        painelCozinha.removeAll();

        for (CozinhaPedido c : lista) {
            painelCozinha.add(criarCartao(c));
        }

        painelCozinha.revalidate();
        painelCozinha.repaint();
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(()
                -> {
            ItemPedidosController controller
                    = new ItemPedidosController(new BDConexao().getConnectionAtiva());

            new TelaCozinhaKDS(controller).setVisible(true);
        });
    }
}
