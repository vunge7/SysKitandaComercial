package util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import util.BDConexao;

/**
 * Pequena estatística da existência de um produto num armazém: existência
 * actual, saídas dos últimos 30 dias (vendas FR/FT não anuladas e
 * transferências enviadas), entradas (compras e transferências recebidas), média diária,
 * cobertura em dias, gráfico das vendas dos últimos 14 dias, compras, última
 * venda e último acerto de stock. Só leituras; uma falha deixa o painel vazio
 * e nunca impede a busca.
 */
public class EstatisticaExistencia extends JPanel {

    private static final int DIAS_GRAFICO = 14;
    private static final int DIAS_PERIODO = 30;
    private static final DecimalFormat QTD = new DecimalFormat("#,##0.##");
    private static final SimpleDateFormat DATA = new SimpleDateFormat("dd-MM-yyyy");

    private final BDConexao conexao;
    private final int armazem;

    private final JLabel lbProduto = new JLabel();
    private final JLabel lbCategoria = new JLabel();
    private final JLabel vExistencia = new JLabel();
    private final JLabel vVendido = new JLabel();
    private final JLabel vMedia = new JLabel();
    private final JLabel vCobertura = new JLabel();
    private final JLabel lbCompras = new JLabel();
    private final JLabel lbSaidas = new JLabel();
    private String nomeArmazem;
    private final JLabel lbUltimaVenda = new JLabel();
    private final JLabel lbAcerto = new JLabel();
    private final JLabel lbCritica = new JLabel();
    private final Grafico grafico = new Grafico();
    private final JPanel conteudo;
    private final JLabel vazio;

    public EstatisticaExistencia(BDConexao conexao, int armazem) {
        super(new BorderLayout());
        this.conexao = conexao;
        this.armazem = armazem;
        setOpaque(false);

        ComponentesWeb.CaixaSuave caixa = new ComponentesWeb.CaixaSuave(new BorderLayout(0, 8), Color.WHITE, MenuWeb.LINHA_QUENTE);
        caixa.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel titulo = new JLabel("Existência do produto");
        titulo.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        titulo.setForeground(MenuWeb.DOURADO);

        lbProduto.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(14f));
        lbProduto.setForeground(MenuWeb.AZUL_NOITE);
        lbCategoria.setFont(EstiloUI.FONTE.deriveFont(11.5f));
        lbCategoria.setForeground(MenuWeb.TEXTO_SUAVE);
        JPanel cabeca = ComponentesWeb.transparente(new GridLayout(3, 1, 0, 0));
        cabeca.add(titulo);
        cabeca.add(lbProduto);
        cabeca.add(lbCategoria);

        JPanel kpis = ComponentesWeb.transparente(new GridLayout(2, 2, 6, 6));
        kpis.add(mosaico("Existência", vExistencia, true));
        kpis.add(mosaico("Saídas 30 dias", vVendido, false));
        kpis.add(mosaico("Média diária", vMedia, false));
        kpis.add(mosaico("Cobertura", vCobertura, false));

        JLabel tituloGrafico = new JLabel("Saídas por dia · últimos " + DIAS_GRAFICO + " dias");
        tituloGrafico.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(11.5f));
        tituloGrafico.setForeground(MenuWeb.TEXTO_SUAVE);
        JPanel blocoGrafico = ComponentesWeb.transparente(new BorderLayout(0, 4));
        blocoGrafico.add(tituloGrafico, BorderLayout.NORTH);
        blocoGrafico.add(grafico, BorderLayout.CENTER);

        JPanel linhas = ComponentesWeb.transparente(new GridLayout(5, 1, 0, 1));
        for (JLabel l : new JLabel[]{lbSaidas, lbCompras, lbUltimaVenda, lbAcerto, lbCritica}) {
            l.setFont(EstiloUI.FONTE.deriveFont(11.5f));
            l.setForeground(MenuWeb.TEXTO);
            linhas.add(l);
        }

        JPanel topo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        topo.add(cabeca, BorderLayout.NORTH);
        topo.add(kpis, BorderLayout.CENTER);

        conteudo = ComponentesWeb.transparente(new BorderLayout(0, 8));
        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(blocoGrafico, BorderLayout.CENTER);
        conteudo.add(linhas, BorderLayout.SOUTH);

        vazio = new JLabel("<html><div style='text-align:center'>Seleccione um produto na lista<br>"
                + "para ver a existência e o histórico de vendas.</div></html>", SwingConstants.CENTER);
        vazio.setFont(EstiloUI.FONTE.deriveFont(12f));
        vazio.setForeground(MenuWeb.TEXTO_SUAVE);

        caixa.add(vazio, BorderLayout.CENTER);
        add(caixa, BorderLayout.CENTER);
    }

    private static JComponent mosaico(String legenda, JLabel valor, boolean destaque) {
        JPanel m = new ComponentesWeb.CaixaSuave(new GridLayout(2, 1, 0, 0),
                destaque ? MenuWeb.AZUL_NOITE : MenuWeb.MARFIM,
                destaque ? MenuWeb.DOURADO : MenuWeb.LINHA_QUENTE);
        m.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        JLabel l = new JLabel(legenda);
        l.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(10.5f));
        l.setForeground(destaque ? MenuWeb.DOURADO_CLARO : MenuWeb.TEXTO_SUAVE);
        valor.setFont(EstiloUI.FONTE_NEGRITO.deriveFont(15f));
        valor.setForeground(destaque ? Color.WHITE : MenuWeb.AZUL_NOITE);
        m.add(l);
        m.add(valor);
        return m;
    }

    /** Volta ao texto de ajuda (nenhum produto seleccionado). */
    public void limpar() {
        mostrar(false);
    }

    private void mostrar(boolean comDados) {
        JPanel caixa = (JPanel) getComponent(0);
        caixa.removeAll();
        caixa.add(comDados ? conteudo : vazio, BorderLayout.CENTER);
        caixa.revalidate();
        caixa.repaint();
    }

    /** Pedido mais recente: resultados de seleções anteriores são ignorados. */
    private int sequencia = 0;

    /** Valores lidos da base de dados (fora do ecrã). */
    private static final class Dados {

        double existencia;
        Double critica;
        double vendido;
        double comprado;
        double transferido;
        double recebido;
        Date ultimaVenda;
        String acerto;
        double[] porDia;
    }

    /**
     * Carrega a estatística. qtdTexto é a quantidade tal como aparece na busca
     * ("-" nos serviços, que não têm existência). As consultas correm em
     * segundo plano (SwingWorker): a lista da busca não prende ao seleccionar.
     */
    public void mostrar(int codigo, String designacao, String categoria, String qtdTexto) {
        lbProduto.setText(designacao == null ? "" : designacao);
        lbProduto.setToolTipText(designacao);
        lbCategoria.setText(categoria == null ? "" : categoria);
        boolean servico = "-".equals(qtdTexto);

        final int pedido = ++sequencia;
        new javax.swing.SwingWorker<Dados, Void>() {
            @Override
            protected Dados doInBackground() throws Exception {
                return ler(codigo);
            }

            @Override
            protected void done() {
                if (pedido != sequencia) {
                    return; // entretanto escolheu-se outro produto
                }
                try {
                    aplicar(get(), categoria, servico);
                } catch (Exception e) {
                    e.printStackTrace();
                    limpar();
                }
            }
        }.execute();
    }

    private Dados ler(int codigo) throws Exception {
        double existencia = 0;
        Double critica = null;
        double vendido = 0;
        double comprado = 0;
        double transferido = 0;
        double recebido = 0;
        Date ultimaVenda = null;
        String acerto = "sem acertos";
        double[] porDia = new double[DIAS_GRAFICO];
        {
            Connection c = conexao.getConnectionAtiva();
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT quantidade_existente, quant_critica FROM tb_stock WHERE cod_produto_codigo = ? AND cod_armazem = ? LIMIT 1")) {
                ps.setInt(1, codigo);
                ps.setInt(2, armazem);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existencia = rs.getDouble(1);
                        critica = rs.getObject(2) == null ? null : rs.getDouble(2);
                    }
                }
            }

            Calendar inicio = hoje();
            inicio.add(Calendar.DAY_OF_MONTH, -(DIAS_PERIODO - 1));
            Map<String, Double> vendasDia = new HashMap<>();
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT DATE(v.dataVenda), SUM(i.quantidade) FROM tb_item_venda i "
                    + "JOIN tb_venda v ON v.codigo = i.codigo_venda "
                    + "JOIN documento d ON d.pk_documento = v.fk_documento "
                    + "WHERE i.codigo_produto = ? AND v.idArmazemFK = ? AND d.abreviacao IN ('FR','FT') "
                    + "AND (v.status_eliminado IS NULL OR v.status_eliminado <> 'true') AND v.dataVenda >= ? "
                    + "GROUP BY DATE(v.dataVenda)")) {
                ps.setInt(1, codigo);
                ps.setInt(2, armazem);
                ps.setDate(3, new java.sql.Date(inicio.getTimeInMillis()));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        double q = rs.getDouble(2);
                        vendido += q;
                        vendasDia.put(String.valueOf(rs.getDate(1)), q);
                    }
                }
            }
            // transferências: as enviadas são saídas (é assim que o economato
            // abastece os outros armazéns), as recebidas são entradas
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT DATE(t.data_hora), "
                    + "SUM(CASE WHEN l.fk_armazem_origem = ? THEN l.qtd ELSE 0 END), "
                    + "SUM(CASE WHEN l.fk_armazem_destino = ? THEN l.qtd ELSE 0 END) "
                    + "FROM linha_transferencia l "
                    + "JOIN transferencia_armazem t ON t.pk_transferencia_armazem = l.fk_transferencia_armazem "
                    + "WHERE l.fk_produto = ? AND (l.fk_armazem_origem = ? OR l.fk_armazem_destino = ?) "
                    + "AND t.data_hora >= ? GROUP BY DATE(t.data_hora)")) {
                ps.setInt(1, armazem);
                ps.setInt(2, armazem);
                ps.setInt(3, codigo);
                ps.setInt(4, armazem);
                ps.setInt(5, armazem);
                ps.setDate(6, new java.sql.Date(inicio.getTimeInMillis()));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        double enviado = rs.getDouble(2);
                        transferido += enviado;
                        recebido += rs.getDouble(3);
                        String chave = String.valueOf(rs.getDate(1));
                        Double antes = vendasDia.get(chave);
                        vendasDia.put(chave, (antes == null ? 0 : antes) + enviado);
                    }
                }
            }
            if (nomeArmazem == null) {
                try (PreparedStatement ps = c.prepareStatement("SELECT designacao FROM tb_armazem WHERE codigo = ?")) {
                    ps.setInt(1, armazem);
                    try (ResultSet rs = ps.executeQuery()) {
                        nomeArmazem = rs.next() ? rs.getString(1) : "";
                    }
                }
            }

            Calendar dia = hoje();
            dia.add(Calendar.DAY_OF_MONTH, -(DIAS_GRAFICO - 1));
            for (int i = 0; i < DIAS_GRAFICO; i++) {
                Double q = vendasDia.get(String.valueOf(new java.sql.Date(dia.getTimeInMillis())));
                porDia[i] = q == null ? 0 : q;
                dia.add(Calendar.DAY_OF_MONTH, 1);
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT SUM(ic.quantidade) FROM item_compras ic JOIN compras co ON co.pk_compra = ic.fk_compra "
                    + "WHERE ic.fk_produto = ? AND co.idArmazemFK = ? "
                    + "AND (co.status_eliminado IS NULL OR co.status_eliminado <> 'true') AND co.data_compra >= ?")) {
                ps.setInt(1, codigo);
                ps.setInt(2, armazem);
                ps.setDate(3, new java.sql.Date(inicio.getTimeInMillis()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        comprado = rs.getDouble(1);
                    }
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT MAX(v.dataVenda) FROM tb_item_venda i JOIN tb_venda v ON v.codigo = i.codigo_venda "
                    + "JOIN documento d ON d.pk_documento = v.fk_documento "
                    + "WHERE i.codigo_produto = ? AND v.idArmazemFK = ? AND d.abreviacao IN ('FR','FT') "
                    + "AND (v.status_eliminado IS NULL OR v.status_eliminado <> 'true')")) {
                ps.setInt(1, codigo);
                ps.setInt(2, armazem);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ultimaVenda = rs.getTimestamp(1);
                    }
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT data_hora, qtd_anterior, qtd_depois FROM acerto_stock "
                    + "WHERE cod_produto = ? AND cod_armazem = ? ORDER BY data_hora DESC LIMIT 1")) {
                ps.setInt(1, codigo);
                ps.setInt(2, armazem);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getTimestamp(1) != null) {
                        acerto = DATA.format(rs.getTimestamp(1)) + "  (" + QTD.format(rs.getDouble(2))
                                + " → " + QTD.format(rs.getDouble(3)) + ")";
                    }
                }
            }
        }
        Dados d = new Dados();
        d.existencia = existencia;
        d.critica = critica;
        d.vendido = vendido;
        d.comprado = comprado;
        d.transferido = transferido;
        d.recebido = recebido;
        d.ultimaVenda = ultimaVenda;
        d.acerto = acerto;
        d.porDia = porDia;
        return d;
    }

    private void aplicar(Dados d, String categoria, boolean servico) {
        double existencia = d.existencia;
        Double critica = d.critica;
        double vendido = d.vendido;
        double comprado = d.comprado;
        double transferido = d.transferido;
        double recebido = d.recebido;
        Date ultimaVenda = d.ultimaVenda;
        String acerto = d.acerto;
        double[] porDia = d.porDia;

        double saidas = vendido + transferido;
        double media = saidas / DIAS_PERIODO;
        lbCategoria.setText((categoria == null ? "" : categoria)
                + (nomeArmazem == null || nomeArmazem.isEmpty() ? "" : "  ·  " + nomeArmazem));
        vExistencia.setText(servico ? "Serviço" : QTD.format(existencia));
        vVendido.setText(QTD.format(saidas));
        vMedia.setText(media == 0 ? "0" : new DecimalFormat("#,##0.0").format(media));
        if (servico) {
            vCobertura.setText("—");
        } else if (existencia <= 0) {
            vCobertura.setText("Esgotado");
        } else if (media == 0) {
            vCobertura.setText("Sem saídas");
        } else {
            vCobertura.setText("≈ " + Math.round(existencia / media) + " dias");
        }
        vCobertura.setForeground(!servico && existencia <= 0 ? ComponentesWeb.VERMELHO : MenuWeb.AZUL_NOITE);

        lbSaidas.setText("<html>Saídas: vendido <b>" + QTD.format(vendido) + "</b> · transferido <b>"
                + QTD.format(transferido) + "</b></html>");
        lbCompras.setText("<html>Entradas: comprado <b>" + QTD.format(comprado) + "</b> · recebido <b>"
                + QTD.format(recebido) + "</b></html>");
        lbUltimaVenda.setText("<html>Última venda: <b>" + (ultimaVenda == null ? "nunca" : DATA.format(ultimaVenda)) + "</b></html>");
        lbAcerto.setText("<html>Último acerto: <b>" + acerto + "</b></html>");
        lbCritica.setText(servico ? "<html>Serviço: não controla existência</html>"
                : "<html>Quantidade crítica: <b>" + (critica == null ? "—" : QTD.format(critica)) + "</b></html>");
        grafico.setDados(porDia);
        mostrar(true);
    }

    private static Calendar hoje() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /** Barras das saídas diárias; o dia de hoje fica a dourado. */
    private static class Grafico extends JComponent {

        private double[] dados = new double[0];

        Grafico() {
            setPreferredSize(new Dimension(200, 80));
            setMinimumSize(new Dimension(100, 50));
        }

        void setDados(double[] dados) {
            this.dados = dados;
            double max = 0;
            for (double d : dados) {
                max = Math.max(max, d);
            }
            setToolTipText("Máximo diário: " + QTD.format(max));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = ComponentesWeb.suave(g);
            int w = getWidth();
            int h = getHeight();
            int rodape = 13;
            int area = h - rodape - 12;
            double max = 0;
            for (double d : dados) {
                max = Math.max(max, d);
            }
            g2.setColor(MenuWeb.LINHA_QUENTE);
            g2.drawLine(0, h - rodape, w, h - rodape);
            int n = Math.max(1, dados.length);
            double passo = (double) w / n;
            int largura = Math.max(3, (int) (passo * 0.62));
            g2.setFont(EstiloUI.FONTE.deriveFont(Font.BOLD, 9.5f));
            FontMetrics fm = g2.getFontMetrics();
            for (int i = 0; i < dados.length; i++) {
                int x = (int) (i * passo + (passo - largura) / 2);
                int alt = max == 0 ? 0 : (int) Math.round(dados[i] / max * area);
                if (dados[i] > 0 && alt < 2) {
                    alt = 2;
                }
                g2.setColor(i == dados.length - 1 ? MenuWeb.DOURADO : MenuWeb.AZUL_MARINHO);
                g2.fillRoundRect(x, h - rodape - alt, largura, alt, 3, 3);
                if (dados[i] > 0 && dados[i] == max) {
                    String t = QTD.format(max);
                    g2.setColor(MenuWeb.AZUL_NOITE);
                    g2.drawString(t, Math.max(0, Math.min(w - fm.stringWidth(t), x + largura / 2 - fm.stringWidth(t) / 2)),
                            h - rodape - alt - 2);
                }
            }
            g2.setColor(MenuWeb.TEXTO_SUAVE);
            g2.setFont(EstiloUI.FONTE.deriveFont(9.5f));
            fm = g2.getFontMetrics();
            Calendar c = hoje();
            String fim = "hoje";
            c.add(Calendar.DAY_OF_MONTH, -(dados.length - 1));
            String ini = new SimpleDateFormat("dd/MM").format(c.getTime());
            g2.drawString(ini, 0, h - 2);
            g2.drawString(fim, w - fm.stringWidth(fim), h - 2);
            if (max == 0) {
                String t = "sem saídas no período";
                g2.drawString(t, (w - fm.stringWidth(t)) / 2, h / 2);
            }
            g2.dispose();
        }
    }
}
