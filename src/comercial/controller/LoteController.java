/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

import entity.EstadoLote;
import entity.Lote;
import entity.LoteMovimento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.BDConexao;

/**
 *
 * @author DELL
 */
public class LoteController implements EntidadeFactory {

    /**
     * Tolerância para comparar quantidades decimais (3 casas decimais).
     */
    private static final double EPS = 0.0005;

    private BDConexao conexao;

    public LoteController(BDConexao conexao) {
        this.conexao = conexao;
    }

    // ------------------------------------------------------------------
    // CRUD
    // ------------------------------------------------------------------
    @Override
    public boolean salvar(Object object) {
        Lote lote = (Lote) object;
        String sql = "INSERT INTO lote( fk_produto, fk_armazem, numero_lote, data_entrada, data_fabrica, data_validade, qtd, estado ) "
                + "VALUES( ?, ?, ?, ?, ?, ?, ?, ? )";

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, lote.getFkProduto());
            ps.setInt(2, lote.getFkArmazem());
            ps.setString(3, lote.getNumeroLote());
            ps.setDate(4, toSqlDate(lote.getDataEntrada()));
            if (lote.getDataFabrica() != null) {
                ps.setDate(5, toSqlDate(lote.getDataFabrica()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setDate(6, toSqlDate(lote.getDataValidade()));
            ps.setDouble(7, lote.getQtd());
            ps.setString(8, lote.getEstado().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(LoteController.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Não altera fk_produto nem fk_armazem: mudar de armazém deve passar por
     * transferência.
     */
    @Override
    public boolean actualizar(Object object) {
        Lote lote = (Lote) object;
        String sql = "UPDATE lote SET numero_lote = ?, data_entrada = ?, data_fabrica = ?, "
                + "data_validade = ?, qtd = ?, estado = ? WHERE id = ?";

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setString(1, lote.getNumeroLote());
            ps.setDate(2, toSqlDate(lote.getDataEntrada()));
            if (lote.getDataFabrica() != null) {
                ps.setDate(3, toSqlDate(lote.getDataFabrica()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setDate(4, toSqlDate(lote.getDataValidade()));
            ps.setDouble(5, lote.getQtd());
            ps.setString(6, lote.getEstado().name());
            ps.setInt(7, lote.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(LoteController.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public boolean eliminar(int codigo) {
        // Só elimina se nunca teve movimentos; caso contrário use DESACTIVO
        String sql = "DELETE FROM lote WHERE id = ?";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Lote> listarTodos() {
        return executarLista("SELECT * FROM lote ORDER BY data_validade ASC, id ASC");
    }

    @Override
    public Vector<String> getVector() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object findById(int codigo) {
        String sql = "SELECT * FROM lote WHERE id = ?";
        Lote lote = null;

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lote = new Lote();
                    setResultSetLote(rs, lote);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lote;
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------
    /**
     * Todos os lotes do produto (todos os armazéns).
     */
    public List<Lote> listarPorProduto(int fkProduto) {
        return executarLista("SELECT * FROM lote WHERE fk_produto = ? ORDER BY data_validade ASC, id ASC", fkProduto);
    }

    /**
     * Lotes do produto num armazém.
     */
    public List<Lote> listarPorProduto(int fkProduto, int fkArmazem) {
        return executarLista("SELECT * FROM lote WHERE fk_produto = ? AND fk_armazem = ? "
                + "ORDER BY data_validade ASC, id ASC", fkProduto, fkArmazem);
    }

    /**
     * Todos os lotes de um armazém.
     */
    public List<Lote> listarPorArmazem(int fkArmazem) {
        return executarLista("SELECT * FROM lote WHERE fk_armazem = ? ORDER BY data_validade ASC, id ASC", fkArmazem);
    }

    /**
     * Lotes activos com stock que vencem nos próximos 'dias' dias (todos os
     * armazéns).
     */
    public List<Lote> listarAVencer(int dias) {
        return executarLista("SELECT * FROM lote WHERE estado = 'ACTIVO' AND qtd > 0 "
                + "AND data_validade BETWEEN CURDATE() AND DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
                + "ORDER BY data_validade ASC", dias);
    }

    /**
     * Lotes activos com stock que vencem nos próximos 'dias' dias, num armazém.
     */
    public List<Lote> listarAVencer(int dias, int fkArmazem) {
        return executarLista("SELECT * FROM lote WHERE estado = 'ACTIVO' AND qtd > 0 AND fk_armazem = ? "
                + "AND data_validade BETWEEN CURDATE() AND DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
                + "ORDER BY data_validade ASC", fkArmazem, dias);
    }

    /**
     * Lotes expirados que ainda têm stock (precisam de abate).
     */
    public List<Lote> listarExpiradosComStock() {
        return executarLista("SELECT * FROM lote WHERE estado = 'EXPIRADO' AND qtd > 0 ORDER BY data_validade ASC");
    }

    /**
     * Stock vendável do produto em todos os armazéns (lotes activos e dentro
     * da validade).
     */
    public double getStock(int fkProduto) {
        String sql = "SELECT COALESCE( SUM( qtd ), 0 ) AS stock FROM lote "
                + "WHERE fk_produto = ? AND estado = 'ACTIVO' AND data_validade >= CURDATE()";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("stock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Stock vendável do produto num armazém (lotes activos e dentro da
     * validade).
     */
    public double getStock(int fkProduto, int fkArmazem) {
        String sql = "SELECT COALESCE( SUM( qtd ), 0 ) AS stock FROM lote "
                + "WHERE fk_produto = ? AND fk_armazem = ? AND estado = 'ACTIVO' AND data_validade >= CURDATE()";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            ps.setInt(2, fkArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("stock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Passa a EXPIRADO os lotes activos com validade ultrapassada. Chamar ao
     * iniciar o sistema e uma vez por dia.
     */
    public int actualizarExpirados() {
        String sql = "UPDATE lote SET estado = 'EXPIRADO' WHERE estado = 'ACTIVO' AND data_validade < CURDATE()";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ------------------------------------------------------------------
    // Baixa FEFO (venda)
    // ------------------------------------------------------------------
    /**
     * Retira a quantidade dos lotes do armazém que vencem primeiro (FEFO). Se
     * um lote não chega, continua no seguinte.
     *
     * Equivalente ao @Transactional: NÃO faz commit nem rollback. Usa a
     * Connection (e a transacção) de quem chama.
     *
     * @param fkArmazem armazém de onde sai o stock
     * @throws IllegalStateException se o stock for insuficiente
     */
    public List<LoteMovimento> baixarStock(Connection conn, int fkProduto, int fkArmazem, double qtdPedida, int fkOperacao) throws SQLException {
        String sql = "SELECT * FROM lote WHERE fk_produto = ? AND fk_armazem = ? AND estado = 'ACTIVO' "
                + "AND qtd > 0 AND data_validade >= CURDATE() "
                + "ORDER BY data_validade ASC, id ASC FOR UPDATE"; // FEFO, com lock

        List<Lote> lotes = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            ps.setInt(2, fkArmazem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lote lote = new Lote();
                    setResultSetLote(rs, lote);
                    lotes.add(lote);
                }
            }
        }

        double disponivel = arredondar(lotes.stream().mapToDouble(Lote::getQtd).sum());
        if (disponivel + EPS < qtdPedida) {
            throw new IllegalStateException("Stock insuficiente: " + disponivel);
        }

        double restante = arredondar(qtdPedida);
        List<LoteMovimento> movimentos = new ArrayList<>();

        for (Lote lote : lotes) {
            if (restante <= EPS) {
                break;
            }

            double retirar = Math.min(lote.getQtd(), restante);
            double novaQtd = arredondar(lote.getQtd() - retirar);

            actualizarQtdLote(conn, lote.getId(), novaQtd, novaQtd <= EPS ? "ESGOTADO" : "ACTIVO");
            movimentos.add(inserirMovimento(conn, lote.getId(), fkOperacao, "VENDA", retirar));

            restante = arredondar(restante - retirar);
        }
        return movimentos;
    }

    /**
     * Versão com transacção própria (uso isolado). Faz commit se correr bem e
     * rollback se falhar.
     *
     * @throws IllegalStateException se o stock for insuficiente
     */
    public List<LoteMovimento> baixarStock(int fkProduto, int fkArmazem, double qtdPedida, int fkOperacao) throws SQLException {
        Connection conn = conexao.getConnection();
        try {
            conn.setAutoCommit(false);
            List<LoteMovimento> movimentos = baixarStock(conn, fkProduto, fkArmazem, qtdPedida, fkOperacao);
            conn.commit();
            return movimentos;
        } catch (SQLException | RuntimeException e) {
            rollbackSilencioso(conn);
            throw e;
        } finally {
            autoCommitSilencioso(conn);
        }
    }

    // ------------------------------------------------------------------
    // Devolução (NC)
    // ------------------------------------------------------------------
    /**
     * Repõe nos lotes de onde a venda original retirou, do último lote retirado
     * para o primeiro. Como repõe no próprio lote, o armazém é o mesmo da
     * venda original.
     *
     * NÃO faz commit nem rollback. Usa a Connection (e a transacção) de quem
     * chama.
     *
     * @param fkOperacaoOrigem venda (FR/FT) a que a NC se refere
     * @throws IllegalStateException se a qtd exceder o vendido
     */
    public List<LoteMovimento> devolverStock(Connection conn, int fkOperacaoOrigem, int fkProduto, double qtdDevolver) throws SQLException {
        String sql = "SELECT m.id, m.fk_lote, m.qtd, m.qtd_devolvida "
                + "FROM lote_movimento m INNER JOIN lote l ON l.id = m.fk_lote "
                + "WHERE m.fk_operacao = ? AND m.tipo = 'VENDA' AND l.fk_produto = ? "
                + "ORDER BY m.id DESC FOR UPDATE";

        // 1) lê os movimentos da venda original (e bloqueia as linhas)
        List<MovOrigem> origem = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fkOperacaoOrigem);
            ps.setInt(2, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double podeDevolver = arredondar(rs.getDouble("qtd") - rs.getDouble("qtd_devolvida"));
                    if (podeDevolver > EPS) {
                        origem.add(new MovOrigem(rs.getInt("id"), rs.getInt("fk_lote"), podeDevolver));
                    }
                }
            }
        }

        double totalDevolvivel = 0;
        for (MovOrigem o : origem) {
            totalDevolvivel += o.podeDevolver;
        }
        totalDevolvivel = arredondar(totalDevolvivel);
        if (totalDevolvivel + EPS < qtdDevolver) {
            throw new IllegalStateException("NC excede o vendido. Máximo a devolver: " + totalDevolvivel);
        }

        // 2) repõe do último lote retirado para o primeiro
        double restante = arredondar(qtdDevolver);
        List<LoteMovimento> movimentos = new ArrayList<>();

        for (MovOrigem o : origem) {
            if (restante <= EPS) {
                break;
            }

            double repor = Math.min(o.podeDevolver, restante);
            reporQtdLote(conn, o.fkLote, repor);
            marcarDevolvido(conn, o.idMov, repor);
            movimentos.add(inserirMovimento(conn, o.fkLote, fkOperacaoOrigem, "DEVOLUCAO", repor));

            restante = arredondar(restante - repor);
        }
        return movimentos;
    }

    /**
     * Versão com transacção própria (uso isolado). Faz commit se correr bem e
     * rollback se falhar.
     *
     * @throws IllegalStateException se a qtd exceder o vendido
     */
    public List<LoteMovimento> devolverStock(int fkOperacaoOrigem, int fkProduto, double qtdDevolver) throws SQLException {
        Connection conn = conexao.getConnection();
        try {
            //conn.setAutoCommit(false);
            List<LoteMovimento> movimentos = devolverStock(conn, fkOperacaoOrigem, fkProduto, qtdDevolver);
            //conn.commit();
            return movimentos;
        } catch (SQLException | RuntimeException e) {
            rollbackSilencioso(conn);
            throw e;
        } finally {
           // autoCommitSilencioso(conn);
        }
    }

    // ------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------
    /**
     * Movimento de venda de onde se vai repor (uso interno).
     */
    private static class MovOrigem {

        final int idMov;
        final int fkLote;
        final double podeDevolver;

        MovOrigem(int idMov, int fkLote, double podeDevolver) {
            this.idMov = idMov;
            this.fkLote = fkLote;
            this.podeDevolver = podeDevolver;
        }
    }

    /**
     * Arredonda a 3 casas decimais para evitar erros de vírgula flutuante
     * (ex.: 0.1 + 0.2).
     */
    private static double arredondar(double valor) {
        return Math.round(valor * 1000.0) / 1000.0;
    }

    private void actualizarQtdLote(Connection conn, int idLote, double novaQtd, String estado) throws SQLException {
        String sql = "UPDATE lote SET qtd = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, novaQtd);
            ps.setString(2, estado);
            ps.setInt(3, idLote);
            ps.executeUpdate();
        }
    }

    private void reporQtdLote(Connection conn, int idLote, double repor) throws SQLException {
        // Mantém DESACTIVO; senão recalcula EXPIRADO/ACTIVO pela validade
        String sql = "UPDATE lote SET qtd = qtd + ?, estado = CASE "
                + "WHEN estado = 'DESACTIVO' THEN 'DESACTIVO' "
                + "WHEN data_validade < CURDATE() THEN 'EXPIRADO' "
                + "ELSE 'ACTIVO' END WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, repor);
            ps.setInt(2, idLote);
            ps.executeUpdate();
        }
    }

    private void marcarDevolvido(Connection conn, int idMovimento, double qtd) throws SQLException {
        String sql = "UPDATE lote_movimento SET qtd_devolvida = qtd_devolvida + ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, qtd);
            ps.setInt(2, idMovimento);
            ps.executeUpdate();
        }
    }

    private LoteMovimento inserirMovimento(Connection conn, int fkLote, int fkOperacao, String tipo, double qtd) throws SQLException {
        String sql = "INSERT INTO lote_movimento( fk_lote, fk_operacao, tipo, qtd, data_mov ) "
                + "VALUES( ?, ?, ?, ?, NOW() )";

        LoteMovimento mov = new LoteMovimento(fkLote, fkOperacao, tipo, qtd);
        mov.setDataMov(new Date());

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, fkLote);
            ps.setInt(2, fkOperacao);
            ps.setString(3, tipo);
            ps.setDouble(4, qtd);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    mov.setId(keys.getInt(1));
                }
            }
        }
        return mov;
    }

    private List<Lote> executarLista(String sql, Object... params) {
        List<Lote> lista = new ArrayList<>();
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lote lote = new Lote();
                    setResultSetLote(rs, lote);
                    lista.add(lote);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private void setResultSetLote(ResultSet rs, Lote lote) throws SQLException {
        lote.setId(rs.getInt("id"));
        lote.setFkProduto(rs.getInt("fk_produto"));
        lote.setFkArmazem(rs.getInt("fk_armazem"));
        lote.setNumeroLote(rs.getString("numero_lote"));
        lote.setDataEntrada(rs.getDate("data_entrada"));
        lote.setDataFabrica(rs.getDate("data_fabrica"));
        lote.setDataValidade(rs.getDate("data_validade"));
        lote.setQtd(rs.getDouble("qtd"));
        lote.setEstado(EstadoLote.valueOf(rs.getString("estado")));
    }

    private java.sql.Date toSqlDate(Date data) {
        return data == null ? null : new java.sql.Date(data.getTime());
    }

    private void rollbackSilencioso(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Indica se o produto tem pelo menos um lote registado (qualquer estado).
     * Útil, por exemplo, antes de eliminar ou alterar um produto.
     */
    public boolean existeLotes(int fkProduto) {
        String sql = "SELECT EXISTS( SELECT 1 FROM lote WHERE fk_produto = ? ) AS existe";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("existe");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LoteController.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    /**
     * Indica se o produto tem lotes vendáveis: activos, com stock e dentro da
     * validade (mesmo critério usado em getStock e baixarStock). Se
     * fkArmazem for null, considera todos os armazéns.
     */
    public boolean existeLotesDisponiveis(int fkProduto, Integer fkArmazem) {
        String sql = "SELECT EXISTS( SELECT 1 FROM lote "
                + "WHERE fk_produto = ? AND estado = 'ACTIVO' "
                + "AND qtd > 0 AND data_validade >= CURDATE() "
                + ( fkArmazem != null ? "AND fk_armazem = ? " : "" )
                + ") AS existe";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            if (fkArmazem != null) {
                ps.setInt(2, fkArmazem);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("existe");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LoteController.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean existeLotesDisponiveis(int fkProduto) {
        return existeLotesDisponiveis(fkProduto, null);
    }

    private void autoCommitSilencioso(Connection conn) {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}