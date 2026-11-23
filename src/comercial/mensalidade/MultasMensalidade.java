package comercial.mensalidade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
import util.BDConexao;

/**
 * Facturação e registo das multas das mensalidades.
 * <ul>
 * <li>Cada multa é uma <b>linha própria</b> na factura, logo a seguir ao mês,
 * com a designação "Multa por atraso: Musculação - Novembro/2026 (25 dias · 10%)"
 * e o <b>valor calculado na própria linha</b> — já não se gravam preços novos
 * num produto (antes: código fixo 1, que noutras bases de dados é outro produto).</li>
 * <li>A linha usa o <b>serviço da multa</b> escolhido nas configurações; se não
 * houver, cria-se um serviço "Multa por atraso" com o IVA/isenção do próprio
 * serviço da mensalidade (não stocável).</li>
 * <li>Cada multa (cobrada, reduzida ou perdoada, com o motivo e o utilizador)
 * fica em {@code mensalidade_multa} quando a venda é gravada; a anulação da
 * venda marca-a como anulada.</li>
 * </ul>
 */
public final class MultasMensalidade {

    public static final String PREFIXO = "Multa por atraso: ";

    /**
     * O que foi decidido na janela dos meses e ainda não está gravado
     * (chave: cliente:serviço:ano:mês — sem o cliente, um carrinho abandonado
     * passava a decisão de um cliente para o seguinte).
     */
    private static final Map<String, Pendente> PENDENTES = new ConcurrentHashMap<>();
    /** Decisões de carrinhos abandonados deixam de valer ao fim deste tempo. */
    private static final long VALIDADE_MS = 12L * 60 * 60 * 1000;

    static final class Pendente {

        int clienteId;
        long criado = System.currentTimeMillis();
        int servicoId;
        String servico;
        Periodo periodo;
        RegrasMulta.Calculo calculo;
        BigDecimal cobrado;
        String motivo;
    }

    private MultasMensalidade() {
    }

    static void garantirTabelas(Statement st) throws SQLException {
        st.executeUpdate("CREATE TABLE IF NOT EXISTS mensalidade_multa ("
                + " id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + " venda_id INT NOT NULL,"
                + " cliente_id INT NOT NULL,"
                + " servico_id INT NOT NULL,"
                + " servico VARCHAR(200) NULL,"
                + " ano INT NOT NULL, mes INT NOT NULL,"
                + " dias_atraso INT NOT NULL DEFAULT 0,"
                + " regra VARCHAR(60) NULL,"
                + " valor_calculado DECIMAL(14,2) NOT NULL DEFAULT 0,"
                + " valor_cobrado DECIMAL(14,2) NOT NULL DEFAULT 0,"
                + " perdoada TINYINT(1) NOT NULL DEFAULT 0,"
                + " motivo VARCHAR(255) NULL,"
                + " usuario_id INT NOT NULL DEFAULT 0,"
                + " data DATETIME NOT NULL,"
                + " anulada TINYINT(1) NOT NULL DEFAULT 0,"
                + " KEY idx_venda (venda_id), KEY idx_cliente (cliente_id), KEY idx_data (data)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
        Connection cn = st.getConnection();
        if (!Mensalidades.existeColuna(cn, "multa_servico", "tipo")) {
            st.executeUpdate("ALTER TABLE multa_servico ADD COLUMN tipo VARCHAR(12) NOT NULL DEFAULT 'PERCENTAGEM'");
        }
    }

    // =====================================================================
    // NA VENDA
    // =====================================================================
    private static String chave(int clienteId, int servicoId, Periodo p) {
        return clienteId + ":" + servicoId + ":" + p.getAno() + ":" + p.getMes();
    }

    /** "Multa por atraso: Musculação - Novembro/2026 (…)" → {serviço, período}; null se não for uma multa. */
    private static Object[] lerDesignacao(String designacao) {
        if (designacao == null || !designacao.startsWith(PREFIXO)) {
            return null;
        }
        String resto = designacao.substring(PREFIXO.length());
        int par = resto.lastIndexOf(" (");
        String semDetalhe = par > 0 ? resto.substring(0, par) : resto;
        int traco = semDetalhe.lastIndexOf(" - ");
        if (traco <= 0) {
            return null;
        }
        Periodo p = Periodo.ler(semDetalhe.substring(traco + 3), Periodo.actual().getAno());
        return p == null ? null : new Object[]{semDetalhe.substring(0, traco), p};
    }

    /** A decisão pendente da linha de multa da venda aberta (cliente actual); null se não houver. */
    private static Pendente daLinha(String designacao) {
        Object[] d = lerDesignacao(designacao);
        if (d == null) {
            return null;
        }
        int cliente = Mensalidades.clienteDaVenda();
        for (Pendente pe : PENDENTES.values()) {
            if (pe.clienteId == cliente && pe.periodo.equals(d[1]) && String.valueOf(d[0]).equals(pe.servico)) {
                return pe;
            }
        }
        return null;
    }

    private static void limparAntigos() {
        long limite = System.currentTimeMillis() - VALIDADE_MS;
        PENDENTES.values().removeIf(pe -> pe.criado < limite);
    }

    /**
     * Antes de tirar uma linha do carrinho. Uma multa tirada sozinha é um
     * perdão: só um administrador, com o motivo (fica registada como perdoada).
     *
     * @return false se não for autorizado (a linha fica)
     */
    public static boolean aoRemoverLinha(java.awt.Component pai, String designacao) {
        Pendente pe = daLinha(designacao);
        if (pe == null || pe.cobrado.signum() <= 0) {
            return true;
        }
        String motivo = MesesMensalidadeDialog.autorizarReducao(pai);
        if (motivo == null) {
            return false;
        }
        pe.cobrado = BigDecimal.ZERO;
        pe.motivo = motivo;
        return true;
    }

    /**
     * Antes de gravar a venda: uma linha de multa com valor abaixo do decidido
     * (preço, quantidade ou desconto alterados no carrinho) é uma redução — só
     * um administrador, com o motivo.
     *
     * @return false se a venda não deve ser gravada
     */
    public static boolean validarVenda(javax.swing.JTable tabela) {
        javax.swing.table.TableModel m = tabela.getModel();
        java.util.List<Object[]> reduzidas = new java.util.ArrayList<>();
        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < m.getRowCount(); i++) {
            String designacao = String.valueOf(m.getValueAt(i, 1));
            Pendente pe = daLinha(designacao);
            if (pe == null) {
                continue;
            }
            BigDecimal valor;
            try {
                valor = BigDecimal.valueOf(kitanda.util.CfMethods.parseMoedaFormatada(String.valueOf(m.getValueAt(i, 9))))
                        .setScale(2, java.math.RoundingMode.HALF_UP);
            } catch (Exception e) {
                continue;
            }
            if (valor.compareTo(pe.cobrado) < 0) {
                reduzidas.add(new Object[]{pe, valor});
                lista.append("\n• ").append(pe.servico).append(" - ").append(pe.periodo.texto()).append(": ")
                        .append(kitanda.util.CfMethods.formatarComoMoeda(pe.cobrado.doubleValue())).append(" → ")
                        .append(kitanda.util.CfMethods.formatarComoMoeda(valor.doubleValue()));
            }
        }
        if (reduzidas.isEmpty()) {
            return true;
        }
        JOptionPane.showMessageDialog(tabela, "O valor destas multas foi alterado no carrinho:" + lista
                + "\n\nReduzir uma multa precisa de um administrador e de um motivo.", "Multas", JOptionPane.WARNING_MESSAGE);
        String motivo = MesesMensalidadeDialog.autorizarReducao(tabela);
        if (motivo == null) {
            return false;
        }
        for (Object[] r : reduzidas) {
            Pendente pe = (Pendente) r[0];
            pe.cobrado = (BigDecimal) r[1];
            pe.motivo = motivo;
        }
        return true;
    }

    /** "Multa por atraso: Musculação - Novembro/2026" (o início da designação: serve para a encontrar). */
    public static String prefixo(String servico, Periodo p) {
        return PREFIXO + servico + " - " + p.texto();
    }

    /** A multa do mês que está nesta linha da venda ("Pgt. Ref. de X de #Mês/Ano"); null se não for um mês. */
    public static String prefixoDaMultaDoMes(String designacaoMes) {
        Periodo p = Periodo.daDesignacao(designacaoMes);
        if (p == null || designacaoMes == null) {
            return null;
        }
        String d = designacaoMes;
        int i = d.indexOf("Pgt. Ref. de ");
        int j = d.lastIndexOf(" de #");
        if (i < 0 || j <= i) {
            return null;
        }
        return prefixo(d.substring(i + "Pgt. Ref. de ".length(), j), p);
    }

    /**
     * Põe a multa do mês na venda (a seguir ao mês) e guarda a decisão para o
     * registo. cobrado = 0 com calculado &gt; 0: perdoada (não entra linha).
     */
    static void adicionar(BDConexao conexao, int servicoId, String servico, Periodo p, RegrasMulta.Calculo calculo,
            BigDecimal cobrado, String motivo) {
        limparAntigos();
        Pendente pe = new Pendente();
        pe.clienteId = Mensalidades.clienteDaVenda();
        pe.servicoId = servicoId;
        pe.servico = servico;
        pe.periodo = p;
        pe.calculo = calculo;
        pe.cobrado = cobrado == null ? calculo.valor : cobrado.max(BigDecimal.ZERO);
        pe.motivo = motivo;
        PENDENTES.put(chave(pe.clienteId, servicoId, p), pe);
        if (pe.cobrado.signum() <= 0) {
            return;
        }
        int produto = produtoDaMulta(conexao, servicoId);
        if (produto <= 0) {
            JOptionPane.showMessageDialog(null, "Não foi possível preparar o serviço da multa.\n"
                    + "Escolha-o em Controlo de devedores › Multas.", "Multa", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String designacao = prefixo(servico, p) + " (" + calculo.descricao()
                + (pe.cobrado.compareTo(calculo.valor) != 0 ? ", ajustada" : "") + ")";
        if (visao.venda.VendaModernaVisao.estaAberta()) {
            visao.venda.VendaModernaVisao.adicionarLinhaComValor(produto, designacao, pe.cobrado);
        } else {
            visao.FormVendaResponsivaVisaoTop.adicionarLinhaComValor(produto, designacao, pe.cobrado);
        }
    }

    // =====================================================================
    // AO GRAVAR A VENDA (mesma transacção da venda)
    // =====================================================================
    /** Linha de um mês gravada: regista a multa desse mês (se houve), ainda com o valor cobrado a 0. */
    public static void aoGravarMes(Connection cn, int vendaId, int clienteId, int servicoId, String mes, int usuarioId) {
        Periodo p = Periodo.ler(mes, Periodo.actual().getAno());
        if (p == null) {
            return;
        }
        Pendente pe = PENDENTES.remove(chave(clienteId, servicoId, p));
        if (pe == null || (pe.calculo.valor.signum() <= 0 && pe.cobrado.signum() <= 0)) {
            return;
        }
        String sql = "INSERT INTO mensalidade_multa (venda_id, cliente_id, servico_id, servico, ano, mes, dias_atraso, regra,"
                + " valor_calculado, valor_cobrado, perdoada, motivo, usuario_id, data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, vendaId);
            ps.setInt(2, clienteId);
            ps.setInt(3, servicoId);
            ps.setString(4, pe.servico);
            ps.setInt(5, p.getAno());
            ps.setInt(6, p.getMes());
            ps.setLong(7, pe.calculo.dias);
            ps.setString(8, pe.calculo.faixa == null ? "" : pe.calculo.faixa.texto());
            ps.setBigDecimal(9, pe.calculo.valor);
            ps.setInt(10, pe.cobrado.signum() <= 0 ? 1 : 0);
            ps.setString(11, pe.motivo == null ? "" : cortar(pe.motivo));
            ps.setInt(12, usuarioId);
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[Multas] registo: " + e.getMessage());
        }
    }

    /**
     * Linha gravada: se for uma multa, fica com o valor que foi mesmo cobrado.
     *
     * @param valor o valor da linha sem IVA (a mesma base do valor calculado)
     */
    public static void aoGravarLinha(Connection cn, int vendaId, String designacao, BigDecimal valor) {
        Object[] d = lerDesignacao(designacao);
        if (d == null) {
            return;
        }
        Periodo p = (Periodo) d[1];
        BigDecimal cobrado = valor == null ? BigDecimal.ZERO : valor.setScale(2, java.math.RoundingMode.HALF_UP);
        String sql = "UPDATE mensalidade_multa SET valor_cobrado = ?, perdoada = ?"
                + " WHERE venda_id = ? AND servico = ? AND ano = ? AND mes = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBigDecimal(1, cobrado);
            ps.setInt(2, cobrado.signum() <= 0 ? 1 : 0);
            ps.setInt(3, vendaId);
            ps.setString(4, String.valueOf(d[0]));
            ps.setInt(5, p.getAno());
            ps.setInt(6, p.getMes());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[Multas] valor cobrado: " + e.getMessage());
        }
    }

    /** Venda anulada (nota de crédito): as multas dela ficam anuladas. */
    public static void anular(Connection cn, int vendaId) {
        try (PreparedStatement ps = cn.prepareStatement("UPDATE mensalidade_multa SET anulada = 1 WHERE venda_id = ?")) {
            ps.setInt(1, vendaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // tabela ainda por criar: nada a anular
        }
    }

    // =====================================================================
    // SERVIÇO DA MULTA
    // =====================================================================
    /**
     * O serviço usado nas linhas de multa: o escolhido nas configurações; se não
     * houver (ou já não existir), cria "Multa por atraso" com a sub-família, a
     * unidade e o IVA/isenção do serviço da mensalidade, e guarda-o na configuração.
     */
    static synchronized int produtoDaMulta(BDConexao conexao, int servicoId) {
        ConfigMensalidade cfg = ConfigMensalidade.ler();
        try (Connection cn = BDConexao.novaLigacao()) {
            if (cfg.produtoMulta > 0 && existe(cn, cfg.produtoMulta)) {
                return cfg.produtoMulta;
            }
            int id = criar(cn, servicoId);
            if (id > 0) {
                cfg.produtoMulta = id;
                cfg.gravar();
            }
            return id;
        } catch (SQLException e) {
            System.err.println("[Multas] serviço da multa: " + e.getMessage());
            return -1;
        }
    }

    private static boolean existe(Connection cn, int produto) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT 1 FROM tb_produto WHERE codigo = ? AND stocavel = 'false'")) {
            ps.setInt(1, produto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static int criar(Connection cn, int servicoId) throws SQLException {
        int id;
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO tb_produto (designacao, preco, data_fabrico, data_expiracao, codBarra, status, data_entrada,"
                + " stocavel, preco_venda, codigo_manual, cod_unidade, cod_local, cod_fornecedores, cod_Tipo_Produto,"
                + " fk_modelo, fk_grupo, status_iva, user_create)"
                + " SELECT 'Multa por atraso', 0, CURDATE(), CURDATE(), '2147483647', 'Activo', CURDATE(), 'false', 0, '',"
                + " cod_unidade, cod_local, cod_fornecedores, cod_Tipo_Produto, fk_modelo, fk_grupo, status_iva, 'Mensalidades'"
                + " FROM tb_produto WHERE codigo = ?", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, servicoId);
            if (ps.executeUpdate() == 0) {
                return -1;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                id = rs.next() ? rs.getInt(1) : -1;
            }
        }
        if (id <= 0) {
            return -1;
        }
        Integer utilizador = util.SessaoUtil.getCodUsuario();
        // um preço 0 para todas as quantidades (a linha leva o valor da multa; a venda precisa de um preço associado)
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO tb_preco (data, preco_compra, percentagem_ganho, fk_produto,"
                + " fk_usuario, preco_venda, qtd_baixo, qtd_alto, preco_anterior, retalho)"
                + " VALUES (CURDATE(), 0, 0, ?, ?, 0, 0, 214748364, 0, 1)")) {
            ps.setInt(1, id);
            ps.setInt(2, utilizador == null || utilizador <= 0 ? 1 : utilizador);
            ps.executeUpdate();
        }
        // o mesmo tratamento fiscal do serviço da mensalidade
        for (String sql : new String[]{
            "INSERT INTO produto_imposto (fk_produto, fk_imposto) SELECT ?, fk_imposto FROM produto_imposto WHERE fk_produto = ?",
            "INSERT INTO produto_isento (fk_produto, fk_produtos_motivos_isensao) SELECT ?, fk_produtos_motivos_isensao FROM produto_isento WHERE fk_produto = ?"}) {
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, servicoId);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[Multas] fiscal: " + e.getMessage());
            }
        }
        return id;
    }

    private static String cortar(String s) {
        return s.length() > 250 ? s.substring(0, 250) : s;
    }
}
