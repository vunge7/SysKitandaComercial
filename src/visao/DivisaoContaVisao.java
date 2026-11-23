package visao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.ui.ComponentesWeb;
import util.ui.EstiloUI;
import util.ui.FormularioWeb;
import util.ui.MesasWeb;

/**
 * "Dividir a conta" (como nos POS de restauração): um cartão por lugar
 * ocupado, com os itens e o total, e a escolha de como cobrar — lugar a
 * lugar (uma factura para cada, em sequência), só um lugar, ou a mesa toda
 * numa factura (mesmo ecrã do SysKitandaHotel, sem as partes iguais). Só escolhe: a cobrança é feita pelo ecrã de pedidos.
 * Feito à mão, sem .form.
 */
public class DivisaoContaVisao extends JDialog {

    public enum Escolha {
        LUGAR_A_LUGAR, UM_LUGAR, MESA_TODA, CANCELAR
    }

    private Escolha escolha = Escolha.CANCELAR;
    private int lugarEscolhido;

    /** Item de um lugar (para os cartões). */
    private static class Linha {

        String produto;
        int qtd;
        double total;
    }

    /**
     * @param lugares código do lugar -> designação, pela ordem dos lugares
     */
    public DivisaoContaVisao(Frame pai, BDConexao conexao, String mesa, int pedido, Map<Integer, String> lugares) {
        super(pai, "Dividir a conta · " + mesa, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Map<Integer, List<Linha>> porLugar = itensPorLugar(conexao, pedido);
        double totalMesa = 0;
        for (List<Linha> itens : porLugar.values()) {
            for (Linha x : itens) {
                totalMesa += x.total;
            }
        }
        JPanel grelha = new MesasWeb.PainelGrelha(230, 12);
        // cartões por lugar só fazem sentido com 2 ou mais lugares ocupados
        int n = 0;
        for (Map.Entry<Integer, String> l : lugares.entrySet()) {
            List<Linha> itens = porLugar.get(l.getKey());
            if (itens != null && !itens.isEmpty()) {
                n++;
            }
        }
        if (n >= 2) {
            for (Map.Entry<Integer, String> l : lugares.entrySet()) {
                List<Linha> itens = porLugar.get(l.getKey());
                if (itens == null || itens.isEmpty()) {
                    continue;
                }
                double total = 0;
                for (Linha x : itens) {
                    total += x.total;
                }
                grelha.add(cartaoLugar(l.getKey(), l.getValue(), itens, total));
            }
        }
        JScrollPane sp = new JScrollPane(grelha, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(24);

        JButton btSequencia = new JButton("Cobrar lugar a lugar");
        ComponentesWeb.botao(btSequencia, ComponentesWeb.Variante.SUCESSO, null,
                "Uma factura para cada lugar, um a seguir ao outro (cada um com o seu cliente e pagamento)");
        btSequencia.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14.5f));
        btSequencia.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btSequencia.addActionListener(e -> fechar(Escolha.LUGAR_A_LUGAR, 0));
        JButton btMesa = new JButton("Mesa toda numa factura");
        ComponentesWeb.botao(btMesa, ComponentesWeb.Variante.PRIMARIO, null, "Uma só factura com todos os lugares (como antes)");
        btMesa.addActionListener(e -> fechar(Escolha.MESA_TODA, 0));
        JButton btCancelar = new JButton("Cancelar");
        FormularioWeb.botaoSair(btCancelar, "Voltar ao pedido sem cobrar (Esc)");
        btCancelar.addActionListener(e -> fechar(Escolha.CANCELAR, 0));

        JLabel dica = FormularioWeb.dica("<html>Total da mesa: <b>" + CfMethods.formatarComoMoeda(totalMesa) + "</b>"
                + (n >= 2 ? " em " + n + " lugares" : "") + "</html>");
        JPanel cabecalho = FormularioWeb.cabecalho("Dividir a conta da " + mesa,
                "Cada lugar paga a sua parte, com factura própria", null);
        javax.swing.AbstractButton[] botoes = n >= 2
                ? new javax.swing.AbstractButton[]{btCancelar, btMesa, btSequencia}
                : new javax.swing.AbstractButton[]{btCancelar, btMesa};
        FormularioWeb.montar(this, cabecalho, sp, FormularioWeb.barraAccoes(dica, botoes), "Divisão da conta");
        if (n >= 2) {
            getRootPane().setDefaultButton(btSequencia);
        }
        getRootPane().registerKeyboardAction(e -> fechar(Escolha.CANCELAR, 0),
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
                javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        int colunas = Math.max(1, Math.min(4, n));
        FormularioWeb.ajustar(this, Math.max(900, colunas * 262 + 60), 560, 720, 420);
    }

    public Escolha getEscolha() {
        return escolha;
    }

    /** Código do lugar escolhido em "Cobrar" num cartão (UM_LUGAR). */
    public int getLugarEscolhido() {
        return lugarEscolhido;
    }

    private void fechar(Escolha e, int lugar) {
        escolha = e;
        lugarEscolhido = lugar;
        dispose();
    }

    private JPanel cartaoLugar(int codigo, String nome, List<Linha> itens, double total) {
        JPanel lista = ComponentesWeb.transparente(null);
        lista.setLayout(new javax.swing.BoxLayout(lista, javax.swing.BoxLayout.Y_AXIS));
        for (Linha x : itens) {
            JPanel linha = ComponentesWeb.transparente(new BorderLayout(8, 0));
            JLabel p = new JLabel(x.qtd + " × " + x.produto);
            p.setFont(EstiloUI.FONTE.deriveFont(13f));
            p.setForeground(ComponentesWeb.TEXTO);
            JLabel v = new JLabel(CfMethods.formatarComoMoeda(x.total), SwingConstants.RIGHT);
            v.setFont(EstiloUI.FONTE.deriveFont(13f));
            v.setForeground(ComponentesWeb.TEXTO_SUAVE);
            linha.add(p, BorderLayout.CENTER);
            linha.add(v, BorderLayout.EAST);
            linha.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            linha.setAlignmentX(0f);
            linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, linha.getPreferredSize().height));
            lista.add(linha);
        }
        JPanel topoLista = ComponentesWeb.transparente(new BorderLayout());
        topoLista.add(lista, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(topoLista, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setPreferredSize(new Dimension(200, Math.min(150, itens.size() * 26 + 6)));

        ComponentesWeb.CaixaSuave totalCx = new ComponentesWeb.CaixaSuave(new BorderLayout(), new Color(15, 23, 42), null);
        JLabel lbTotal = new JLabel("TOTAL");
        lbTotal.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11f));
        lbTotal.setForeground(new Color(148, 163, 184));
        JLabel valor = new JLabel(CfMethods.formatarComoMoeda(total), SwingConstants.RIGHT);
        valor.setFont(MesasWeb.fonteVisor(20));
        valor.setForeground(new Color(134, 239, 172));
        totalCx.add(lbTotal, BorderLayout.WEST);
        totalCx.add(valor, BorderLayout.CENTER);

        JButton cobrar = new JButton("Cobrar só este lugar");
        ComponentesWeb.botao(cobrar, ComponentesWeb.Variante.SECUNDARIO, null, "Facturar agora só o " + nome.toLowerCase());
        cobrar.addActionListener(e -> fechar(Escolha.UM_LUGAR, codigo));

        JPanel corpo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        corpo.add(sp, BorderLayout.CENTER);
        JPanel baixo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        baixo.add(totalCx, BorderLayout.NORTH);
        baixo.add(cobrar, BorderLayout.SOUTH);
        corpo.add(baixo, BorderLayout.SOUTH);
        ComponentesWeb.Cartao c = FormularioWeb.cartao(nome + "  ·  " + itens.size() + (itens.size() == 1 ? " item" : " itens"),
                MesasWeb.OCUPADA, corpo);
        return c;
    }

    /** Itens por facturar do pedido, agrupados pelo lugar. */
    private static Map<Integer, List<Linha>> itensPorLugar(BDConexao conexao, int pedido) {
        Map<Integer, List<Linha>> m = new LinkedHashMap<>();
        String sql = "SELECT i.fk_lugares, p.designacao, i.qtd, i.total_item FROM tb_item_pedidos i"
                + " JOIN tb_produto p ON p.codigo = i.fk_produtos"
                + " WHERE i.fk_pedidos = ? AND i.status_convertido = 0 ORDER BY i.fk_lugares, i.pk_item_pedidos";
        try (java.sql.PreparedStatement ps = conexao.getPreparedStatement(sql)) {
            ps.setInt(1, pedido);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Linha l = new Linha();
                    l.produto = rs.getString(2);
                    l.qtd = rs.getInt(3);
                    l.total = rs.getDouble(4);
                    List<Linha> lista = m.get(rs.getInt(1));
                    if (lista == null) {
                        lista = new ArrayList<>();
                        m.put(rs.getInt(1), lista);
                    }
                    lista.add(l);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }
}
