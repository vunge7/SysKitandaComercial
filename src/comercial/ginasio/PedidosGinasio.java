package comercial.ginasio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Pedidos em aberto dos clientes do ginásio: o que o sócio vai pedindo durante
 * o treino (água, toalha, iogurte...) e paga de uma só vez no fim. Substitui o
 * caderno: cada pedido fica gravado em {@code ginasio_pedido} com a hora e o
 * operador, até entrar numa factura (estado FACTURADO, com o id da venda) ou
 * ser anulado (ANULADO, fica para consulta).
 *
 * <p>
 * Cada operação usa uma ligação própria ({@code BDConexao.novaLigacao()}),
 * para não interferir com as transacções da janela de vendas; a única
 * excepção é {@link #marcarFacturados}, que corre dentro da transacção da
 * venda, para a factura e a baixa dos pedidos serem uma coisa só.
 * 2026-11-21.
 */
public final class PedidosGinasio {

    public static final String ABERTO = "ABERTO";
    public static final String FACTURADO = "FACTURADO";
    public static final String ANULADO = "ANULADO";

    private static volatile boolean estruturaVerificada;

    private PedidosGinasio() {
    }

    // =====================================================================
    // MODELOS
    // =====================================================================
    /** Um cliente com pedidos em aberto (cartões no topo do diálogo). */
    public static final class ResumoCliente {

        public final int idCliente;
        public final String nome;
        public final int linhas;
        public final double total;
        public final Date desde;

        public ResumoCliente(int idCliente, String nome, int linhas, double total, Date desde) {
            this.idCliente = idCliente;
            this.nome = nome == null ? "" : nome;
            this.linhas = linhas;
            this.total = total;
            this.desde = desde;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    /** Uma linha de pedido. */
    public static final class Pedido {

        public final int id;
        public final int idCliente;
        public final String nomeCliente;
        public final int idProduto;
        public final String designacao;
        public final double quantidade;
        /** Preço unitário sem IVA no momento do pedido. */
        public final double preco;
        public final double taxaIva;
        public final Date data;
        public final String operador;

        public Pedido(int id, int idCliente, String nomeCliente, int idProduto, String designacao, double quantidade,
                double preco, double taxaIva, Date data, String operador) {
            this.id = id;
            this.idCliente = idCliente;
            this.nomeCliente = nomeCliente;
            this.idProduto = idProduto;
            this.designacao = designacao;
            this.quantidade = quantidade;
            this.preco = preco;
            this.taxaIva = taxaIva;
            this.data = data;
            this.operador = operador;
        }

        public double precoComIva() {
            return preco * (1 + taxaIva / 100.0);
        }

        public double total() {
            return quantidade * precoComIva();
        }
    }

    /** Produto para os cartões do catálogo. */
    public static final class Produto {

        public final int codigo;
        public final String designacao;
        public final String categoria;
        public final boolean stocavel;
        public final boolean servico;
        public final double stock;
        public final double preco;
        public final double taxaIva;
        public final byte[] foto;

        public Produto(int codigo, String designacao, String categoria, boolean stocavel, boolean servico, double stock,
                double preco, double taxaIva, byte[] foto) {
            this.codigo = codigo;
            this.designacao = designacao;
            this.categoria = categoria;
            this.stocavel = stocavel;
            this.servico = servico;
            this.stock = stock;
            this.preco = preco;
            this.taxaIva = taxaIva;
            this.foto = foto;
        }

        public double precoComIva() {
            return preco * (1 + taxaIva / 100.0);
        }

        /** Sem stock para mais uma unidade (os serviços nunca ficam sem stock). */
        public boolean esgotado() {
            return stocavel && !servico && stock <= 0;
        }
    }

    /** Categoria (tipo de produto) com o número de produtos vendáveis. */
    public static final class Categoria {

        public final int codigo;
        public final String designacao;
        public final int produtos;

        public Categoria(int codigo, String designacao, int produtos) {
            this.codigo = codigo;
            this.designacao = designacao;
            this.produtos = produtos;
        }
    }

    /** Cliente encontrado na pesquisa. */
    public static final class Cliente {

        public final int codigo;
        public final String nome;
        public final String telefone;
        public final String nif;

        public Cliente(int codigo, String nome, String telefone, String nif) {
            this.codigo = codigo;
            this.nome = nome;
            this.telefone = telefone;
            this.nif = nif;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    // =====================================================================
    // ESTRUTURA
    // =====================================================================
    /** Cria a tabela ginasio_pedido na primeira vez (as seguintes não fazem nada). */
    public static void garantirEstrutura(Connection cn) throws SQLException {
        if (estruturaVerificada) {
            return;
        }
        try (Statement st = cn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS ginasio_pedido ("
                    + " id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " fk_cliente INT NOT NULL,"
                    + " nome_cliente VARCHAR(200) NULL,"
                    + " fk_produto INT NOT NULL,"
                    + " designacao VARCHAR(700) NULL,"
                    + " quantidade DOUBLE NOT NULL DEFAULT 1,"
                    + " preco DECIMAL(30,6) NOT NULL DEFAULT 0 COMMENT 'preço unitário sem IVA no momento do pedido',"
                    + " taxa_iva DOUBLE NOT NULL DEFAULT 0,"
                    + " fk_usuario INT NULL,"
                    + " operador VARCHAR(150) NULL,"
                    + " data_pedido DATETIME NOT NULL,"
                    + " estado VARCHAR(15) NOT NULL DEFAULT 'ABERTO' COMMENT 'ABERTO, FACTURADO, ANULADO',"
                    + " fk_venda INT NULL,"
                    + " data_fecho DATETIME NULL,"
                    + " KEY idx_ginasio_pedido_estado (estado, fk_cliente),"
                    + " KEY idx_ginasio_pedido_venda (fk_venda)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8");
        }
        estruturaVerificada = true;
    }

    /** Ligação nova com a tabela garantida; quem chama fecha-a. */
    private static Connection ligar() throws SQLException {
        Connection cn = util.BDConexao.novaLigacao();
        garantirEstrutura(cn);
        return cn;
    }

    // =====================================================================
    // CONSULTAS
    // =====================================================================
    /** Clientes com pedidos em aberto (sem os que já estão na venda), pelo primeiro pedido. */
    public static List<ResumoCliente> clientesEmAberto(Collection<Integer> excluirIds) throws SQLException {
        String sql = "SELECT g.fk_cliente, COALESCE(MAX(c.nome), MAX(g.nome_cliente)), COUNT(*),"
                + " SUM(g.quantidade * g.preco * (1 + g.taxa_iva / 100)), MIN(g.data_pedido)"
                + " FROM ginasio_pedido g LEFT JOIN tb_cliente c ON c.codigo = g.fk_cliente"
                + " WHERE g.estado = 'ABERTO'" + excluir(excluirIds)
                + " GROUP BY g.fk_cliente ORDER BY MIN(g.data_pedido)";
        List<ResumoCliente> l = new ArrayList<>();
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                l.add(new ResumoCliente(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getTimestamp(5)));
            }
        }
        return l;
    }

    /** Número de clientes com pedidos em aberto (o contador do botão). */
    public static int contarClientes(Collection<Integer> excluirIds) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT g.fk_cliente) FROM ginasio_pedido g WHERE g.estado = 'ABERTO'" + excluir(excluirIds);
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /** Pedidos em aberto de um cliente, do mais antigo para o mais recente. */
    public static List<Pedido> pedidosDoCliente(int idCliente, Collection<Integer> excluirIds) throws SQLException {
        String sql = "SELECT g.id, g.fk_cliente, COALESCE(c.nome, g.nome_cliente), g.fk_produto, g.designacao, g.quantidade,"
                + " g.preco, g.taxa_iva, g.data_pedido, g.operador"
                + " FROM ginasio_pedido g LEFT JOIN tb_cliente c ON c.codigo = g.fk_cliente"
                + " WHERE g.estado = 'ABERTO' AND g.fk_cliente = ?" + excluir(excluirIds)
                + " ORDER BY g.data_pedido, g.id";
        List<Pedido> l = new ArrayList<>();
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new Pedido(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getString(5),
                            rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getTimestamp(9), rs.getString(10)));
                }
            }
        }
        return l;
    }

    // =====================================================================
    // ALTERAÇÕES
    // =====================================================================
    /** Regista um pedido (quantidade 1 ou outra) ao preço e IVA actuais do produto. */
    /** O "Consumidor Final" não é um cliente: um pedido tem de ficar em nome de alguém cadastrado. */
    public static boolean ehConsumidorFinal(String nome) {
        return nome == null || nome.trim().isEmpty()
                || nome.trim().equalsIgnoreCase(util.DVML._CLIENTE_CONSUMIDOR_FINAL);
    }

    /**
     * Confirma que o cliente existe no cadastro e não é o Consumidor Final.
     * Devolve a mensagem para o operador, ou null se estiver tudo bem.
     */
    public static String validarCliente(int idCliente) throws SQLException {
        if (idCliente <= 0) {
            return "Escolha o cliente do pedido.";
        }
        String nome = nomeCliente(idCliente);
        if (nome == null) {
            return "O cliente n.º " + idCliente + " não existe no cadastro.";
        }
        if (ehConsumidorFinal(nome)) {
            return "Os pedidos não podem ficar em nome do Consumidor Final.\n"
                    + "Escolha o cliente ou cadastre-o agora.";
        }
        return null;
    }

    public static int registar(int idCliente, String nomeCliente, Produto p, double quantidade, int idUsuario,
            String operador) throws SQLException {
        String invalido = validarCliente(idCliente);
        if (invalido != null) {
            throw new SQLException(invalido);
        }
        String sql = "INSERT INTO ginasio_pedido (fk_cliente, nome_cliente, fk_produto, designacao, quantidade, preco,"
                + " taxa_iva, fk_usuario, operador, data_pedido, estado) VALUES (?,?,?,?,?,?,?,?,?,NOW(),'ABERTO')";
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setString(2, nomeCliente);
            ps.setInt(3, p.codigo);
            ps.setString(4, p.designacao);
            ps.setDouble(5, quantidade);
            ps.setDouble(6, p.preco);
            ps.setDouble(7, p.taxaIva);
            ps.setInt(8, idUsuario);
            ps.setString(9, operador);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** Muda a quantidade de um pedido em aberto. */
    public static void alterarQuantidade(int idPedido, double quantidade) throws SQLException {
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(
                "UPDATE ginasio_pedido SET quantidade = ? WHERE id = ? AND estado = 'ABERTO'")) {
            ps.setDouble(1, quantidade);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    /** Anula um pedido (fica na tabela, como ANULADO, para consulta). */
    public static void anular(int idPedido) throws SQLException {
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(
                "UPDATE ginasio_pedido SET estado = 'ANULADO', data_fecho = NOW() WHERE id = ? AND estado = 'ABERTO'")) {
            ps.setInt(1, idPedido);
            ps.executeUpdate();
        }
    }

    /**
     * Dá baixa dos pedidos facturados, na ligação (e transacção) da venda:
     * se a venda for desfeita (rollback), os pedidos continuam em aberto.
     */
    public static int marcarFacturados(Connection transaccaoVenda, Collection<Integer> ids, int idVenda) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        garantirEstrutura(transaccaoVenda);
        StringBuilder in = new StringBuilder();
        for (Integer id : ids) {
            in.append(in.length() == 0 ? "" : ",").append(id.intValue());
        }
        try (PreparedStatement ps = transaccaoVenda.prepareStatement("UPDATE ginasio_pedido SET estado = 'FACTURADO',"
                + " fk_venda = ?, data_fecho = NOW() WHERE estado = 'ABERTO' AND id IN (" + in + ")")) {
            ps.setInt(1, idVenda);
            return ps.executeUpdate();
        }
    }

    // =====================================================================
    // CATÁLOGO E CLIENTES
    // =====================================================================
    /** Produtos vendáveis no armazém: serviços (sempre) ou com registo de stock no armazém. */
    private static final String VENDAVEL = " AND (p.status IS NULL OR p.status <> 'Desactivo')"
            + " AND (t.fk_familia = ? OR EXISTS (SELECT 1 FROM tb_stock s WHERE s.cod_produto_codigo = p.codigo AND s.cod_armazem = ?))";

    public static List<Categoria> categorias(int armazem) throws SQLException {
        String sql = "SELECT t.codigo, t.designacao, COUNT(*) FROM tb_tipo_produto t"
                + " JOIN tb_produto p ON p.cod_Tipo_Produto = t.codigo WHERE 1 = 1" + VENDAVEL
                + " GROUP BY t.codigo, t.designacao ORDER BY t.designacao";
        List<Categoria> l = new ArrayList<>();
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, util.DVML.COD_SERVICO);
            ps.setInt(2, armazem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new Categoria(rs.getInt(1), rs.getString(2), rs.getInt(3)));
                }
            }
        }
        return l;
    }

    private static final String COLUNAS_PRODUTO = "SELECT p.codigo, p.designacao, t.designacao, p.stocavel, t.fk_familia,"
            + " (SELECT s.quantidade_existente FROM tb_stock s WHERE s.cod_produto_codigo = p.codigo AND s.cod_armazem = ? LIMIT 1),"
            + " (SELECT pr.preco_venda FROM tb_preco pr WHERE pr.fk_produto = p.codigo ORDER BY pr.pk_preco DESC LIMIT 1),"
            + " (SELECT i.taxa FROM produto_imposto pi JOIN imposto i ON i.pk_imposto = pi.fk_imposto WHERE pi.fk_produto = p.codigo LIMIT 1),"
            + " %s"
            + " FROM tb_produto p JOIN tb_tipo_produto t ON t.codigo = p.cod_Tipo_Produto";

    /** Produtos de uma categoria (com fotografia). */
    public static List<Produto> produtosDaCategoria(int categoria, int armazem) throws SQLException {
        String sql = String.format(COLUNAS_PRODUTO, "p.photo") + " WHERE p.cod_Tipo_Produto = ?" + VENDAVEL
                + " ORDER BY p.designacao LIMIT 400";
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, armazem);
            ps.setInt(2, categoria);
            ps.setInt(3, util.DVML.COD_SERVICO);
            ps.setInt(4, armazem);
            return lerProdutos(ps);
        }
    }

    /** Lupa: pelo nome, em todas as categorias (com fotografia). */
    public static List<Produto> procurarPorNome(String texto, int armazem) throws SQLException {
        String sql = String.format(COLUNAS_PRODUTO, "p.photo") + " WHERE p.designacao LIKE ?" + VENDAVEL
                + " ORDER BY p.designacao LIMIT 60";
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, armazem);
            ps.setString(2, "%" + texto.trim() + "%");
            ps.setInt(3, util.DVML.COD_SERVICO);
            ps.setInt(4, armazem);
            return lerProdutos(ps);
        }
    }

    /** Código interno, código de barras ou código manual (exactos); null se não existir. */
    public static Produto procurarPorCodigo(String codigo, int armazem) throws SQLException {
        String c = codigo.trim();
        if (c.isEmpty()) {
            return null;
        }
        String sql = String.format(COLUNAS_PRODUTO, "NULL")
                + " WHERE (p.codBarra = ? OR p.codigo_manual = ? OR CAST(p.codigo AS CHAR) = ?)" + VENDAVEL
                + " ORDER BY (CAST(p.codigo AS CHAR) = ?) DESC LIMIT 1";
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, armazem);
            ps.setString(2, c);
            ps.setString(3, c);
            ps.setString(4, c);
            ps.setInt(5, util.DVML.COD_SERVICO);
            ps.setInt(6, armazem);
            ps.setString(7, c);
            List<Produto> l = lerProdutos(ps);
            return l.isEmpty() ? null : l.get(0);
        }
    }

    private static List<Produto> lerProdutos(PreparedStatement ps) throws SQLException {
        List<Produto> l = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                boolean servico = rs.getInt(5) == util.DVML.COD_SERVICO;
                l.add(new Produto(rs.getInt(1), rs.getString(2), rs.getString(3), "true".equals(rs.getString(4)),
                        servico, rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getBytes(9)));
            }
        }
        return l;
    }

    /** Clientes pelo nome, telefone ou NIF (sem o Consumidor Final). */
    public static List<Cliente> procurarClientes(String texto) throws SQLException {
        String t = "%" + (texto == null ? "" : texto.trim()) + "%";
        String sql = "SELECT codigo, nome, telefone, nif FROM tb_cliente WHERE nome <> ?"
                + " AND (nome LIKE ? OR telefone LIKE ? OR nif LIKE ?) ORDER BY nome LIMIT 30";
        List<Cliente> l = new ArrayList<>();
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, util.DVML._CLIENTE_CONSUMIDOR_FINAL);
            ps.setString(2, t);
            ps.setString(3, t);
            ps.setString(4, t);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    l.add(new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        }
        return l;
    }

    /** Nome actual de um cliente (null se não existir). */
    public static String nomeCliente(int idCliente) throws SQLException {
        try (Connection cn = ligar(); PreparedStatement ps = cn.prepareStatement("SELECT nome FROM tb_cliente WHERE codigo = ?")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    private static String excluir(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" AND g.id NOT IN (");
        boolean primeiro = true;
        for (Integer id : ids) {
            sb.append(primeiro ? "" : ",").append(id.intValue());
            primeiro = false;
        }
        return sb.append(')').toString();
    }

    /** Data para a coluna "Hora": só as horas se for hoje, senão dia e hora. */
    public static String hora(Date d) {
        if (d == null) {
            return "";
        }
        java.util.Calendar c = java.util.Calendar.getInstance();
        java.util.Calendar h = java.util.Calendar.getInstance();
        c.setTime(d);
        boolean hoje = c.get(java.util.Calendar.YEAR) == h.get(java.util.Calendar.YEAR)
                && c.get(java.util.Calendar.DAY_OF_YEAR) == h.get(java.util.Calendar.DAY_OF_YEAR);
        return new java.text.SimpleDateFormat(hoje ? "HH:mm" : "dd/MM HH:mm").format(d);
    }
}
