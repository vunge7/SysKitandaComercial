package visao;

import comercial.controller.ItemPedidosController;
import enties.util.CozinhaPedido;
import util.BDConexao;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class TelaCozinhaKDS1 extends JFrame
{

    private JPanel painelCozinha;
    private ItemPedidosController controller;

    public TelaCozinhaKDS1( ItemPedidosController controller )
    {
        this.controller = controller;

        setTitle( "Kitchen Display System - Cozinha" );
        setExtendedState( JFrame.MAXIMIZED_BOTH );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLayout( new BorderLayout() );

        criarInterface();
        carregarCozinhaVisual();

        new Timer( 4000, e -> carregarCozinhaVisual() ).start();
    }

    // ================= INTERFACE =================
    private void criarInterface()
    {

        JPanel topo = new JPanel( new BorderLayout() );
        topo.setBackground( new Color( 45, 45, 45 ) );
        topo.setBorder( BorderFactory.createEmptyBorder( 10, 20, 10, 20 ) );

        JLabel titulo = new JLabel( "PEDIDOS DA COZINHA" );
        titulo.setFont( new Font( "Segoe UI", Font.BOLD, 26 ) );
        titulo.setForeground( Color.WHITE );

        topo.add( titulo, BorderLayout.WEST );
        add( topo, BorderLayout.NORTH );

        painelCozinha = new JPanel();
        painelCozinha.setLayout( new GridLayout( 0, 4, 20, 20 ) );
        painelCozinha.setBackground( new Color( 30, 30, 30 ) );
        painelCozinha.setBorder( BorderFactory.createEmptyBorder( 20, 20, 20, 20 ) );

        JScrollPane scroll = new JScrollPane( painelCozinha );
        scroll.setBorder( null );
        scroll.getVerticalScrollBar().setUnitIncrement( 16 );

        add( scroll, BorderLayout.CENTER );
    }

    // ================= AGRUPAMENTO =================
    private Map<String, List<CozinhaPedido>> agrupar( List<CozinhaPedido> lista )
    {

        Map<String, List<CozinhaPedido>> mapa = new LinkedHashMap<>();

        for ( CozinhaPedido c : lista )
        {
            String chave = c.getPedido() + "-" + c.getMesa() + "-" + c.getLugar();

            if ( !mapa.containsKey( chave ) )
            {
                mapa.put( chave, new ArrayList<>() );
            }
            mapa.get( chave ).add( c );
        }
        return mapa;
    }

    // ================= CARTÃO =================
    private JPanel criarCartao( List<CozinhaPedido> grupo )
    {

        CozinhaPedido primeiro = grupo.get( 0 );

        JPanel card = new JPanel( new BorderLayout() );
        card.setPreferredSize( new Dimension( 300, 230 ) );
        card.setBackground( Color.WHITE );
        card.setBorder( BorderFactory.createLineBorder( new Color( 70, 130, 180 ), 3 ) );

        JLabel pedido = new JLabel( "PEDIDO #" + primeiro.getPedido() );
        pedido.setFont( new Font( "Segoe UI", Font.BOLD, 25 ) );
        pedido.setForeground( new Color( 30, 60, 120 ) );

        JLabel mesa = new JLabel( primeiro.getMesa() + "  |   " + primeiro.getLugar() );
        mesa.setFont( new Font( "Segoe UI", Font.PLAIN, 25 ) );

        JPanel topo = new JPanel( new GridLayout( 2, 1 ) );
        topo.setOpaque( false );
        topo.setBorder( BorderFactory.createEmptyBorder( 10, 10, 5, 10 ) );
        topo.add( pedido );
        topo.add( mesa );

        JTextArea itens = new JTextArea();
        itens.setEditable( false );
        itens.setFont( new Font( "Segoe UI", Font.PLAIN, 25 ) );
        itens.setOpaque( false );

        StringBuilder sb = new StringBuilder();
        for ( CozinhaPedido c : grupo )
        {
            sb.append( "• " ).append( c.getProduto() )
                    .append( "   x" ).append( c.getQuantidade() )
                    .append( "\n" );
        }
        itens.setText( sb.toString() );
        itens.setBorder( BorderFactory.createEmptyBorder( 5, 10, 10, 10 ) );

        JLabel status = new JLabel( "AGUARDANDO PREPARO" );
        status.setFont( new Font( "Segoe UI", Font.BOLD, 12 ) );
        status.setForeground( new Color( 0, 120, 215 ) );

        JPanel rodape = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
        rodape.setOpaque( false );
        rodape.setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10 ) );
        rodape.add( status );

        card.add( topo, BorderLayout.NORTH );
        card.add( itens, BorderLayout.CENTER );
        card.add( rodape, BorderLayout.SOUTH );

        return card;
    }

    // ================= CARREGAMENTO =================
    private void carregarCozinhaVisual()
    {

        painelCozinha.removeAll();

        List<CozinhaPedido> lista = controller.listarPedidosCozinha();

        Map<String, List<CozinhaPedido>> pedidos = agrupar( lista );

        for ( List<CozinhaPedido> grupo : pedidos.values() )
        {
            painelCozinha.add( criarCartao( grupo ) );
        }

        painelCozinha.revalidate();
        painelCozinha.repaint();
    }

    // ================= MAIN =================
    public static void main( String[] args )
    {

        SwingUtilities.invokeLater(() ->
        {
            ItemPedidosController controller
                    = new ItemPedidosController( new BDConexao().getConnectionAtiva() );

            new TelaCozinhaKDS1( controller ).setVisible( true );
        } );
    }
}
