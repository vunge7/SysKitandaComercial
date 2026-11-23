package comercial.mensalidade;

import entity.ConfiguracaoMesComeco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.BDConexao;

/**
 * Regras das mensalidades, num só sítio:
 * <ul>
 * <li>um contrato = cliente + serviço + mês/ano de começo + duração (0 = contínuo);</li>
 * <li>os meses a facturar são os períodos (mês/ano) do contrato que ainda não foram pagos;</li>
 * <li>paga-se por ordem: escolher um mês leva também os meses anteriores em falta.</li>
 * </ul>
 */
public final class Mensalidades {

    /** Num contrato contínuo, quantos meses à frente se pode adiantar. */
    public static final int MESES_ADIANTADOS_CONTINUO = 12;

    public enum Estado {
        PAGO("Pago"), ATRASO("Em atraso"), ACTUAL("Vence este mês"), FUTURO("Por vencer");

        private final String texto;

        Estado(String texto) {
            this.texto = texto;
        }

        public String texto() {
            return texto;
        }
    }

    /** Um mês do contrato e o seu estado. */
    public static final class Mes {

        public final Periodo periodo;
        public final Estado estado;
        public final int vendaId; // 0 se não pago
        /** Dia em que este mês vence (dia de pagamento do contrato ou fim do mês). */
        public final java.time.LocalDate vencimento;

        Mes(Periodo periodo, Estado estado, int vendaId, java.time.LocalDate vencimento) {
            this.periodo = periodo;
            this.estado = estado;
            this.vendaId = vendaId;
            this.vencimento = vencimento;
        }

        public boolean pago() {
            return estado == Estado.PAGO;
        }
    }

    /** Resumo de um contrato para as listagens. */
    public static final class Resumo {

        public final ConfiguracaoMesComeco contrato;
        public final List<Mes> meses;
        public int pagos, emAtraso, porPagar;
        public Periodo proximo; // primeiro mês por pagar (null = tudo pago)

        Resumo(ConfiguracaoMesComeco contrato, List<Mes> meses) {
            this.contrato = contrato;
            this.meses = meses;
            for (Mes m : meses) {
                if (m.pago()) {
                    pagos++;
                } else {
                    porPagar++;
                    if (m.estado == Estado.ATRASO) {
                        emAtraso++;
                    }
                    if (proximo == null) {
                        proximo = m.periodo;
                    }
                }
            }
        }

        public String estadoTexto() {
            if (emAtraso > 0) {
                return emAtraso + (emAtraso == 1 ? " mês em atraso" : " meses em atraso");
            }
            if (proximo == null) {
                return "Tudo pago";
            }
            return proximo.equals(Periodo.actual()) ? "Vence este mês" : "Em dia";
        }
    }

    private static volatile boolean estruturaVerificada = false;

    // =====================================================================
    // NEGÓCIOS COM MENSALIDADES
    // =====================================================================
    /** As mensalidades só existem nestes negócios: Ginásio, Centro Infantil e Prestação de Serviço. */
    public static boolean activo() {
        return usaMensalidades(util.ui.Tema.actual());
    }

    public static boolean usaMensalidades(util.ui.Tema.Negocio n) {
        return n == util.ui.Tema.Negocio.GINASIO
                || n == util.ui.Tema.Negocio.CENTRO_INFANTIL
                || n == util.ui.Tema.Negocio.PRESTACAO_SERVICO;
    }

    private Mensalidades() {
    }

    // =====================================================================
    // CONTRATO
    // =====================================================================
    public static Periodo inicio(ConfiguracaoMesComeco c) {
        int ano = c.getAnoInicio() > 0 ? c.getAnoInicio() : Periodo.actual().getAno();
        int mes = c.getMesId() >= 1 && c.getMesId() <= 12 ? c.getMesId() : 1;
        return new Periodo(ano, mes);
    }

    /** Último mês do contrato; null se for contínuo. */
    public static Periodo fim(ConfiguracaoMesComeco c) {
        return c.getDuracao() > 0 ? inicio(c).mais(c.getDuracao() - 1) : null;
    }

    public static String textoDuracao(int duracao) {
        if (duracao <= 0) {
            return "Contínuo";
        }
        return duracao + (duracao == 1 ? " mês" : " meses");
    }

    /** "Mar/2026 → Ago/2026" ou "Mar/2026 → sem fim". */
    public static String textoVigencia(ConfiguracaoMesComeco c) {
        Periodo i = inicio(c);
        Periodo f = fim(c);
        return i.texto() + "  →  " + (f == null ? "sem fim" : f.texto());
    }

    /**
     * Dia em que o mês p vence. Ginásio: cada atleta tem o seu dia (o dia de
     * pagamento do contrato; num mês mais curto passa para o último dia).
     * Dia 0 = fecha no fim do mês (Centro Infantil, Prestação de Serviço).
     */
    public static java.time.LocalDate vencimento(ConfiguracaoMesComeco c, Periodo p) {
        java.time.YearMonth ym = java.time.YearMonth.of(p.getAno(), p.getMes());
        int dia = c.getDiaPagamento();
        return dia <= 0 ? ym.atEndOfMonth() : ym.atDay(Math.min(dia, ym.lengthOfMonth()));
    }

    public static String textoDiaPagamento(int dia) {
        return dia <= 0 ? "fim do mês" : "dia " + dia;
    }

    /** Dia de pagamento sugerido para um contrato novo: no ginásio o dia de hoje (o dia da inscrição); nos outros, fim do mês. */
    public static int diaPagamentoSugerido() {
        return util.ui.Tema.actual() == util.ui.Tema.Negocio.GINASIO
                ? java.time.LocalDate.now().getDayOfMonth() : 0;
    }

    // =====================================================================
    // MESES DO CONTRATO
    // =====================================================================
    /** Os meses do contrato com o estado de cada um (inclui pagos fora do intervalo, p.ex. após editar o contrato). */
    public static List<Mes> meses(Connection cn, ConfiguracaoMesComeco c) throws SQLException {
        return meses(c, pagamentos(cn, c.getClienteId(), c.getProdutoId()), java.time.LocalDate.now(),
                EstadoCliente.de(cn, c.getClienteId()).limiteDivida());
    }

    public static List<Mes> meses(ConfiguracaoMesComeco c, Map<Periodo, Integer> pagos, java.time.LocalDate dataRef) {
        return meses(c, pagos, dataRef, null);
    }

    /**
     * Igual, com os pagamentos já lidos e uma data de referência (relatórios:
     * sem ir à BD por contrato). limite = dia em que o cliente desistiu ou foi
     * desactivado: os meses que vencem depois disso não contam (se não estiverem pagos).
     */
    public static List<Mes> meses(ConfiguracaoMesComeco c, Map<Periodo, Integer> pagos, java.time.LocalDate dataRef,
            java.time.LocalDate limite) {
        Periodo hoje = new Periodo(dataRef.getYear(), dataRef.getMonthValue());
        Periodo inicio = inicio(c);
        Periodo fim = fim(c);
        if (fim == null) {
            Periodo base = hoje;
            for (Periodo p : pagos.keySet()) {
                if (p.compareTo(base) > 0) {
                    base = p;
                }
            }
            fim = (base.compareTo(inicio) < 0 ? inicio : base).mais(MESES_ADIANTADOS_CONTINUO);
        }

        TreeMap<Periodo, Mes> lista = new TreeMap<>();
        for (Periodo p = inicio; p.compareTo(fim) <= 0; p = p.mais(1)) {
            if (limite != null && !pagos.containsKey(p) && vencimento(c, p).isAfter(limite)) {
                continue; // inactivo: depois de sair não deve nada
            }
            lista.put(p, criar(c, p, pagos.get(p), dataRef));
        }
        for (Map.Entry<Periodo, Integer> e : pagos.entrySet()) {
            lista.putIfAbsent(e.getKey(), criar(c, e.getKey(), e.getValue(), dataRef));
        }
        return new ArrayList<>(lista.values());
    }

    public static Resumo resumo(Connection cn, ConfiguracaoMesComeco c) throws SQLException {
        return new Resumo(c, meses(cn, c));
    }

    /** Em atraso = passou o dia de vencimento; o mês corrente ainda por vencer é ACTUAL. */
    private static Mes criar(ConfiguracaoMesComeco c, Periodo p, Integer vendaId, java.time.LocalDate dataRef) {
        java.time.LocalDate venc = vencimento(c, p);
        if (vendaId != null) {
            return new Mes(p, Estado.PAGO, vendaId, venc);
        }
        Estado e;
        if (venc.isBefore(dataRef)) {
            e = Estado.ATRASO;
        } else if (p.getAno() == dataRef.getYear() && p.getMes() == dataRef.getMonthValue()) {
            e = Estado.ACTUAL;
        } else {
            e = Estado.FUTURO;
        }
        return new Mes(p, e, 0, venc);
    }

    /** Períodos já pagos → venda que os pagou. */
    public static Map<Periodo, Integer> pagamentos(Connection cn, int clienteId, int produtoId) throws SQLException {
        Map<Periodo, Integer> pagos = new HashMap<>();
        String sql = "SELECT mes_id, ano, venda_id, data_cadastro FROM pagamento_mensalidade "
                + "WHERE cliente_id = ? AND produto_id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setInt(2, produtoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int mes = rs.getInt("mes_id");
                    if (mes < 1 || mes > 12) {
                        continue;
                    }
                    int ano = rs.getInt("ano");
                    if (ano <= 0) {
                        java.sql.Timestamp t = rs.getTimestamp("data_cadastro");
                        ano = t != null ? Periodo.de(t).getAno() : Periodo.actual().getAno();
                    }
                    pagos.put(new Periodo(ano, mes), rs.getInt("venda_id"));
                }
            }
        }
        return pagos;
    }

    // =====================================================================
    // VENDA EM CURSO
    // =====================================================================
    /** Os períodos deste serviço que já estão no carrinho da venda aberta (ainda não gravada). */
    public static java.util.Set<Periodo> noCarrinho(int produtoId) {
        java.util.Set<Periodo> lista = new java.util.HashSet<>();
        javax.swing.JTable t = null;
        try {
            t = visao.venda.VendaModernaVisao.estaAberta()
                    ? visao.venda.VendaModernaVisao.table
                    : visao.FormVendaResponsivaVisaoTop.table;
        } catch (Throwable ignorar) {
            // a janela de venda ainda não foi criada
        }
        if (t == null) {
            return lista;
        }
        for (int r = 0; r < t.getRowCount(); r++) {
            Object cod = t.getModel().getValueAt(r, 0);
            Object des = t.getModel().getValueAt(r, 1);
            if (cod == null || des == null || !String.valueOf(cod).trim().equals(String.valueOf(produtoId))) {
                continue;
            }
            Periodo p = Periodo.daDesignacao(String.valueOf(des));
            if (p != null) {
                lista.add(p);
            }
        }
        return lista;
    }

    /** A janela de vendas aberta (moderna ou responsiva); null se não houver. */
    public static java.awt.Window janelaVendaAberta() {
        try {
            if (visao.venda.VendaModernaVisao.estaAberta()) {
                return javax.swing.SwingUtilities.getWindowAncestor(visao.venda.VendaModernaVisao.cmbCliente);
            }
            javax.swing.JComboBox c = visao.FormVendaResponsivaVisaoTop.cmbCliente;
            if (c != null && c.isShowing()) {
                return javax.swing.SwingUtilities.getWindowAncestor(c);
            }
        } catch (Throwable ignorar) {
            // janela ainda não criada
        }
        return null;
    }

    /** Escolhe o cliente na venda aberta (recarrega a lista se estiver filtrada). */
    @SuppressWarnings("unchecked")
    public static void seleccionarClienteNaVenda(String nome) {
        javax.swing.JComboBox cmb = visao.venda.VendaModernaVisao.estaAberta()
                ? visao.venda.VendaModernaVisao.cmbCliente : visao.FormVendaResponsivaVisaoTop.cmbCliente;
        if (cmb == null || nome == null) {
            return;
        }
        for (int i = 0; i < cmb.getItemCount(); i++) {
            if (String.valueOf(cmb.getItemAt(i)).trim().equalsIgnoreCase(nome.trim())) {
                cmb.setSelectedIndex(i);
                return;
            }
        }
        cmb.setModel(new javax.swing.DefaultComboBoxModel(
                new comercial.controller.ClientesController(BDConexao.getInstancia()).getVector()));
        cmb.setSelectedItem(nome);
    }

    /** Põe um mês (ou a multa, com mes "") na venda aberta. */
    public static void adicionarNaVenda(int codigoProduto, String mes) {
        if (visao.venda.VendaModernaVisao.estaAberta()) {
            visao.venda.VendaModernaVisao.accao_codigo_interno_enter_busca_exterior_2(codigoProduto, mes);
        } else {
            visao.FormVendaResponsivaVisaoTop.accao_codigo_interno_enter_busca_exterior_2(codigoProduto, mes);
        }
    }

    public static java.math.BigDecimal precoMensal(BDConexao conexao, int produtoId) {
        entity.TbPreco p = new comercial.controller.PrecosController(conexao).getLastIdPrecoByIdProduto(produtoId, 1);
        return p != null && p.getPrecoVenda() != null ? p.getPrecoVenda() : java.math.BigDecimal.ZERO;
    }

    // =====================================================================
    // MULTAS
    // =====================================================================
    /** Regras de multa lidas há pouco (as janelas recalculam a cada clique). */
    private static final java.util.Map<Integer, Object[]> CACHE_FAIXAS = new java.util.concurrent.ConcurrentHashMap<>();

    private static List<RegrasMulta.Faixa> faixas(int produtoId) {
        Object[] c = CACHE_FAIXAS.get(produtoId);
        if (c != null && System.currentTimeMillis() - (Long) c[0] < 30_000) {
            @SuppressWarnings("unchecked")
            List<RegrasMulta.Faixa> l = (List<RegrasMulta.Faixa>) c[1];
            return l;
        }
        List<RegrasMulta.Faixa> l;
        try (Connection cn = BDConexao.novaLigacao()) {
            l = RegrasMulta.doServico(cn, produtoId);
        } catch (SQLException e) {
            l = new ArrayList<>();
        }
        CACHE_FAIXAS.put(produtoId, new Object[]{System.currentTimeMillis(), l});
        return l;
    }

    /** As regras mudaram (ecrã das multas): a próxima conta volta a ler. */
    public static void esquecerRegrasMulta() {
        CACHE_FAIXAS.clear();
    }

    /**
     * A multa de um mês do contrato: regras do serviço ({@link RegrasMulta}) e a
     * base de contagem dos dias da configuração (dia 1 do mês ou dia de vencimento).
     */
    public static RegrasMulta.Calculo calculoMulta(ConfiguracaoMesComeco c, java.math.BigDecimal precoMensal,
            Periodo p, java.util.Date dataPagamento) {
        if (c == null || dataPagamento == null || p == null) {
            return RegrasMulta.Calculo.ZERO;
        }
        java.time.LocalDate pagamento = dataPagamento.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        return RegrasMulta.calcular(faixas(c.getProdutoId()), precoMensal, p, vencimento(c, p), pagamento,
                ConfigMensalidade.lerEmCache().multaBase);
    }

    /** Valor da multa (atalho de {@link #calculoMulta}). */
    public static java.math.BigDecimal multa(ConfiguracaoMesComeco c, java.math.BigDecimal precoMensal, Periodo p,
            java.util.Date dataPagamento) {
        return calculoMulta(c, precoMensal, p, dataPagamento).valor;
    }

    /** O cliente escolhido na venda aberta (0 se nenhuma). */
    static int clienteDaVenda() {
        try {
            return visao.venda.VendaModernaVisao.estaAberta() ? visao.venda.VendaModernaVisao.getIdCliente()
                    : visao.FormVendaResponsivaVisaoTop.getIdCliente();
        } catch (Throwable e) {
            return 0;
        }
    }

    /** Adiciona os meses à venda, cada um seguido da sua multa (se houver). */
    public static void enviarParaVenda(BDConexao conexao, int produtoId, List<Periodo> periodos, java.util.Date dataPagamento) {
        enviarParaVenda(conexao, produtoId, periodos, dataPagamento, null, null);
    }

    /**
     * Adiciona os meses à venda (por ordem), cada um seguido da sua multa.
     *
     * @param cobrado multa a cobrar por mês, quando foi ajustada ou perdoada
     *                (0 = perdoada); null ou sem o mês = a multa calculada
     * @param motivo  motivo do ajuste/perdão (fica no registo da multa)
     */
    public static void enviarParaVenda(BDConexao conexao, int produtoId, List<Periodo> periodos, java.util.Date dataPagamento,
            java.util.Map<Periodo, java.math.BigDecimal> cobrado, String motivo) {
        java.math.BigDecimal preco = precoMensal(conexao, produtoId);
        ConfiguracaoMesComeco contrato = new comercial.controller.ConfiguracaoMesComecoController(conexao.getConnectionAtiva())
                .buscarDoCliente(clienteDaVenda(), produtoId);
        String servico;
        try {
            entity.TbProduto pr = (entity.TbProduto) new comercial.controller.ProdutosController(conexao).findById(produtoId);
            servico = pr == null ? "Serviço " + produtoId : pr.getDesignacao();
        } catch (Exception e) {
            servico = "Serviço " + produtoId;
        }
        for (Periodo p : periodos) {
            adicionarNaVenda(produtoId, p.texto());
            RegrasMulta.Calculo k = calculoMulta(contrato, preco, p, dataPagamento);
            java.math.BigDecimal valor = cobrado != null && cobrado.containsKey(p) ? cobrado.get(p) : k.valor;
            if (k.valor.signum() > 0 || (valor != null && valor.signum() > 0)) {
                MultasMensalidade.adicionar(conexao, produtoId, servico, p, k, valor,
                        valor != null && valor.compareTo(k.valor) != 0 ? motivo : null);
            }
        }
    }

    /** Os meses por pagar (não pagos e fora do carrinho), por ordem. */
    public static List<Periodo> porPagar(List<Mes> meses, java.util.Set<Periodo> noCarrinho) {
        List<Periodo> l = new ArrayList<>();
        for (Mes m : meses) {
            if (!m.pago() && !noCarrinho.contains(m.periodo)) {
                l.add(m.periodo);
            }
        }
        return l;
    }

    /** Quantos dos meses por pagar já venceram (até ao mês corrente). */
    public static int vencidos(List<Periodo> porPagar) {
        Periodo hoje = Periodo.actual();
        int n = 0;
        for (Periodo p : porPagar) {
            if (p.compareTo(hoje) <= 0) {
                n++;
            }
        }
        return n;
    }

    // =====================================================================
    // ESTRUTURA DA BD
    // =====================================================================
    /**
     * Acrescenta (uma vez por arranque) o ano aos contratos e aos pagamentos e
     * acerta os registos antigos. Corre na sua própria ligação, porque um
     * ALTER faz commit implícito e não pode cair a meio de uma venda.
     */
    public static void garantirEstrutura() {
        if (estruturaVerificada || !activo()) {
            return;
        }
        synchronized (Mensalidades.class) {
            if (estruturaVerificada) {
                return;
            }
            try (Connection cn = BDConexao.novaLigacao(); Statement st = cn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS configuracao_mes_comeco ("
                        + " id int(11) NOT NULL AUTO_INCREMENT,"
                        + " data_cadastro timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                        + " mes_id int(11) NOT NULL, produto_id int(11) NOT NULL,"
                        + " usuario_id int(11) NOT NULL, duracao int(11) NOT NULL,"
                        + " cliente_id int(11) NOT NULL, PRIMARY KEY (id)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS pagamento_mensalidade ("
                        + " id int(11) NOT NULL AUTO_INCREMENT,"
                        + " produto_id int(11) NOT NULL, cliente_id int(11) NOT NULL,"
                        + " mes_id int(11) NOT NULL, venda_id int(11) NOT NULL,"
                        + " data_cadastro timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                        + " PRIMARY KEY (id)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=latin1");

                if (!existeColuna(cn, "configuracao_mes_comeco", "ano_inicio")) {
                    st.executeUpdate("ALTER TABLE configuracao_mes_comeco ADD COLUMN ano_inicio INT NOT NULL DEFAULT 0");
                }
                // data_cadastro tem ON UPDATE CURRENT_TIMESTAMP: repete-se o valor para não a perder
                st.executeUpdate("UPDATE configuracao_mes_comeco SET data_cadastro = data_cadastro,"
                        + " ano_inicio = YEAR(data_cadastro) WHERE ano_inicio = 0");

                if (!existeColuna(cn, "configuracao_mes_comeco", "dia_pagamento")) {
                    st.executeUpdate("ALTER TABLE configuracao_mes_comeco ADD COLUMN dia_pagamento INT NOT NULL DEFAULT 0");
                }
                ConfigMensalidade.garantirTabela(st);
                MultasMensalidade.garantirTabelas(st);
                EstadoCliente.garantirTabelas(st);
                if (!existeColuna(cn, "pagamento_mensalidade", "ano")) {
                    st.executeUpdate("ALTER TABLE pagamento_mensalidade ADD COLUMN ano INT NOT NULL DEFAULT 0");
                }
                // os contratos antigos tinham no máximo 12 meses: um mês antes do mês de
                // começo pertence ao ano seguinte ao do começo
                st.executeUpdate("UPDATE pagamento_mensalidade p JOIN configuracao_mes_comeco c"
                        + " ON c.cliente_id = p.cliente_id AND c.produto_id = p.produto_id"
                        + " SET p.data_cadastro = p.data_cadastro,"
                        + " p.ano = IF(p.mes_id >= c.mes_id, c.ano_inicio, c.ano_inicio + 1)"
                        + " WHERE p.ano = 0");
                st.executeUpdate("UPDATE pagamento_mensalidade SET data_cadastro = data_cadastro,"
                        + " ano = YEAR(data_cadastro) WHERE ano = 0");
                estruturaVerificada = true;
            } catch (SQLException e) {
                System.err.println("[Mensalidades] não foi possível actualizar a estrutura: " + e.getMessage());
            }
        }
    }

    static boolean existeColuna(Connection cn, String tabela, String coluna) throws SQLException {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS"
                + " WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tabela);
            ps.setString(2, coluna);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
