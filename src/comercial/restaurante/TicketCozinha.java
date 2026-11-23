package comercial.restaurante;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import util.BDConexao;
import util.DVML;

/**
 * Talão da cozinha / bar da gestão de mesas.
 *
 * <p>
 * Um só ponto para tudo o que sai na cozinha: o item acabado de lançar (com a
 * observação do cliente), o cancelamento de um item e o pedido completo da
 * mesa (botão "Enviar ticket"). Os produtos marcados "Enviar Ticket" vão para a
 * impressora da cozinha e os "Enviar Sala" para a impressora da sala/bar, as
 * duas escolhidas em Configurações. Sem impressora configurada (ou se o nome
 * não existir neste computador) usa a impressora predefinida do Windows e avisa.
 *
 * <p>
 * Os dados são lidos logo (o item pode ser apagado a seguir, no cancelamento);
 * a impressão corre numa fila própria, para o empregado não ficar à espera da
 * impressora. Modelo: relatorios/talao_cozinha.jrxml.
 * 2026-11-17.
 */
public final class TicketCozinha {

    public static final String NOVO = "NOVO PEDIDO";
    public static final String CANCELADO = "*** CANCELADO ***";
    public static final String COMPLETO = "PEDIDO DA MESA";

    private static final String MODELO = "talao_cozinha";
    private static final SimpleDateFormat HORA = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final DecimalFormat QTD = new DecimalFormat("0.##");
    /** Uma só fila: os talões saem pela ordem em que foram lançados. */
    private static final ExecutorService FILA = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "talao-cozinha");
        t.setDaemon(true);
        return t;
    });
    private static volatile boolean avisouPredefinida;

    private TicketCozinha() {
    }

    /** Uma linha do talão. */
    static final class Item {

        String prato, obs, lugar, mesa, area;
        double qtd;
        int pkLugar, pkPedido;
    }

    // =====================================================================
    // API
    // =====================================================================
    /** Talão de um item da mesa (lançado ou cancelado). Não faz nada se o produto não vai à cozinha/bar. */
    public static void item(int pkItemPedido, String estado, String garcom) {
        try {
            List<Item> l = ler("i.pk_item_pedidos = ?", pkItemPedido);
            enviar(l, estado, garcom);
        } catch (Exception e) {
            erro(e);
        }
    }

    /** Talão com todos os pratos/bebidas do pedido da mesa, separado por cozinha e bar. */
    public static int pedido(int pkPedido, String garcom) {
        try {
            List<Item> l = ler("i.fk_pedidos = ?", pkPedido);
            enviar(l, COMPLETO, garcom);
            return l.size();
        } catch (Exception e) {
            erro(e);
            return -1;
        }
    }

    /**
     * Talão de teste (Configuração dos talões): confirma que a impressora
     * escolhida imprime. Não usa a base de dados.
     */
    public static void teste(boolean bar, String impressora) {
        Item it = new Item();
        it.prato = "Talão de teste";
        it.obs = "Se está a ler isto, a impressora " + (bar ? "do bar" : "da cozinha") + " está bem configurada";
        it.qtd = 1;
        it.lugar = "";
        it.mesa = "TESTE";
        it.area = bar ? DVML.ENVIAR_SALA : DVML.ENVIAR_TICKET;
        List<Item> l = new ArrayList<>();
        l.add(it);
        final List<Map<String, ?>> linhas = linhas(bar ? "BAR / SALA" : "COZINHA", "TESTE DE IMPRESSÃO", "", new Date(), l);
        final String nome = impressora == null ? "" : impressora.trim();
        FILA.submit(() -> imprimir(linhas, nome));
    }

    // =====================================================================
    // DADOS
    // =====================================================================
    private static List<Item> ler(String filtro, int id) throws Exception {
        String sql = "SELECT i.qtd, i.obs, p.designacao, p.cozinha, l.designacao AS lugar, l.pk_lugares, "
                + " m.designacao AS mesa, pe.pk_pedido "
                + "FROM tb_item_pedidos i "
                + " INNER JOIN tb_produto p ON p.codigo = i.fk_produtos "
                + " INNER JOIN tb_pedido pe ON pe.pk_pedido = i.fk_pedidos "
                + " LEFT JOIN tb_lugares l ON l.pk_lugares = i.fk_lugares "
                + " LEFT JOIN tb_mesas m ON m.pk_mesas = pe.fk_mesas "
                + "WHERE " + filtro + " AND p.cozinha IN (?, ?) "
                + "ORDER BY p.cozinha, l.designacao, i.pk_item_pedidos";
        List<Item> l = new ArrayList<>();
        // ligação partilhada da aplicação: só leitura e nunca se fecha
        Connection cn = BDConexao.getConexao();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, DVML.ENVIAR_TICKET);
            ps.setString(3, DVML.ENVIAR_SALA);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item it = new Item();
                    it.qtd = rs.getDouble("qtd");
                    it.obs = limpo(rs.getString("obs"));
                    it.prato = limpo(rs.getString("designacao"));
                    it.area = rs.getString("cozinha");
                    it.lugar = limpo(rs.getString("lugar"));
                    it.pkLugar = rs.getInt("pk_lugares");
                    it.mesa = limpo(rs.getString("mesa"));
                    it.pkPedido = rs.getInt("pk_pedido");
                    l.add(it);
                }
            }
        }
        return l;
    }

    private static String[] impressoras() {
        String[] r = {"", ""};
        try (PreparedStatement ps = BDConexao.getConexao().prepareStatement(
                "SELECT impressora_cozinha, impressora_sala FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                r[0] = limpo(rs.getString(1));
                r[1] = limpo(rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    // =====================================================================
    // TALÃO
    // =====================================================================
    private static void enviar(List<Item> itens, String estado, String garcom) {
        if (itens.isEmpty()) {
            return;
        }
        Map<String, List<Item>> porArea = new LinkedHashMap<>();
        for (Item it : itens) {
            porArea.computeIfAbsent(DVML.ENVIAR_SALA.equals(it.area) ? DVML.ENVIAR_SALA : DVML.ENVIAR_TICKET,
                    k -> new ArrayList<>()).add(it);
        }
        String[] imp = impressoras();
        Date agora = new Date();
        for (Map.Entry<String, List<Item>> e : porArea.entrySet()) {
            boolean bar = DVML.ENVIAR_SALA.equals(e.getKey());
            final List<Map<String, ?>> linhas = linhas(bar ? "BAR / SALA" : "COZINHA", estado, garcom, agora, e.getValue());
            final String impressora = bar ? (imp[1].isEmpty() ? imp[0] : imp[1]) : imp[0];
            FILA.submit(() -> imprimir(linhas, impressora));
        }
    }

    static List<Map<String, ?>> linhas(String area, String estado, String garcom, Date quando, List<Item> itens) {
        List<Map<String, ?>> l = new ArrayList<>();
        Item p = itens.get(0);
        add(l, "A", area, null);
        add(l, "E", estado, null);
        add(l, "M", p.mesa.isEmpty() ? "BALCÃO" : p.mesa.toUpperCase(), null);
        add(l, "L", "Hora", HORA.format(quando));
        if (!limpo(garcom).isEmpty()) {
            add(l, "L", "Empregado", limpo(garcom));
        }
        if (p.pkPedido > 0) {
            add(l, "L", "Pedido Nº", String.valueOf(p.pkPedido));
        }
        add(l, "R", "", null);

        boolean variosLugares = false;
        for (Item it : itens) {
            if (it.pkLugar != p.pkLugar) {
                variosLugares = true;
                break;
            }
        }
        String lugarActual = null;
        double total = 0;
        for (Item it : itens) {
            boolean temLugar = !it.lugar.isEmpty() && it.pkLugar != DVML.LUGAR_BALCAO;
            if (temLugar && (variosLugares || itens.size() == 1) && !it.lugar.equals(lugarActual)) {
                add(l, "L", "Lugar", it.lugar);
                lugarActual = it.lugar;
            }
            add(l, "I", it.prato, QTD.format(it.qtd) + "x");
            if (!it.obs.isEmpty()) {
                add(l, "O", it.obs, null);
            }
            total += it.qtd;
        }
        add(l, "R", "", null);
        if (itens.size() > 1) {
            add(l, "N", itens.size() + " linhas · " + QTD.format(total) + " unidades", null);
        }
        return l;
    }

    private static void add(List<Map<String, ?>> l, String tipo, String esq, String dir) {
        Map<String, Object> m = new HashMap<>();
        m.put("tipo", tipo);
        m.put("esq", esq == null ? "" : esq);
        m.put("dir", dir);
        l.add(m);
    }

    // =====================================================================
    // IMPRESSÃO
    // =====================================================================
    private static void imprimir(List<Map<String, ?>> linhas, String nomeImpressora) {
        try {
            Map<String, Object> par = new HashMap<>();
            par.put("RODAPE", "Kitanda · " + DH.format(new Date()));
            JasperPrint jp = JasperFillManager.fillReport(caminhoModelo(), par, new JRMapCollectionDataSource(linhas));

            PrintService ps = procurar(nomeImpressora);
            if (ps == null) {
                ps = PrintServiceLookup.lookupDefaultPrintService();
                if (ps == null) {
                    throw new Exception("Não há nenhuma impressora instalada neste computador.");
                }
                if (!avisouPredefinida) {
                    avisouPredefinida = true;
                    final String usada = ps.getName();
                    aviso((nomeImpressora.isEmpty()
                            ? "Não está configurada a impressora da cozinha/bar."
                            : "A impressora \"" + nomeImpressora + "\" não foi encontrada neste computador.")
                            + "\nO talão foi enviado para a impressora predefinida (" + usada + ")."
                            + "\n\nEscolha a impressora em Configurações → Configuração Sistema.");
                }
            }
            PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
            attrs.add(new Copies(1));
            attrs.add(new MediaPrintableArea(0, 0, 80, 297, MediaPrintableArea.MM));

            JRPrintServiceExporter ex = new JRPrintServiceExporter();
            ex.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            ex.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, ps);
            ex.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, attrs);
            ex.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            ex.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            ex.exportReport();
        } catch (Throwable e) {
            erro(e);
        }
    }

    /** Impressora pelo nome (exacto, depois parcial); null se o nome estiver vazio ou não existir. */
    public static PrintService procurar(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        String n = nome.trim();
        PrintService[] todas = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService s : todas) {
            if (s.getName().equalsIgnoreCase(n)) {
                return s;
            }
        }
        for (PrintService s : todas) {
            if (s.getName().toLowerCase().contains(n.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    /** Nomes das impressoras instaladas (para escolher em Configurações). */
    public static List<String> impressorasInstaladas() {
        List<String> l = new ArrayList<>();
        for (PrintService s : PrintServiceLookup.lookupPrintServices(null, null)) {
            l.add(s.getName());
        }
        return l;
    }

    /** O .jasper do modelo; compila o .jrxml se faltar ou estiver desactualizado. */
    private static synchronized String caminhoModelo() throws Exception {
        File jasper = new File(DVML.CAMINHO_REPORT + MODELO + ".jasper");
        File jrxml = new File(DVML.CAMINHO_REPORT + MODELO + ".jrxml");
        if (jrxml.exists() && (!jasper.exists() || jasper.lastModified() < jrxml.lastModified())) {
            try {
                JasperCompileManager.compileReportToFile(jrxml.getPath(), jasper.getPath());
            } catch (Throwable e) {
                if (!jasper.exists()) {
                    throw e instanceof Exception ? (Exception) e : new Exception(e);
                }
                System.err.println("[Talão cozinha] não foi possível recompilar " + jrxml.getName() + " (" + e + ")");
            }
        }
        return jasper.getAbsolutePath();
    }

    private static String limpo(String s) {
        return s == null ? "" : s.trim();
    }

    private static void aviso(String msg) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, "Talão da cozinha", JOptionPane.WARNING_MESSAGE));
    }

    private static void erro(Throwable e) {
        e.printStackTrace();
        aviso("Não foi possível imprimir o talão da cozinha/bar.\n" + e.getMessage());
    }
}
