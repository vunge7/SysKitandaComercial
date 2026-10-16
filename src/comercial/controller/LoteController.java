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
        String sql = "INSERT INTO lote( fk_produto, numero_lote, data_entrada, data_fabrica, data_validade, qtd, estado ) "
                + "VALUES( ?, ?, ?, ?, ?, ?, ? )";

        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, lote.getFkProduto());
            ps.setString(2, lote.getNumeroLote());
            ps.setDate(3, toSqlDate(lote.getDataEntrada()));
            if (lote.getDataFabrica() != null) {
                ps.setDate(4, toSqlDate(lote.getDataFabrica()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setDate(5, toSqlDate(lote.getDataValidade()));
            ps.setInt(6, lote.getQtd());
            ps.setString(7, lote.getEstado().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(LoteController.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

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
            ps.setInt(5, lote.getQtd());
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
    public List<Lote> listarPorProduto(int fkProduto) {
        return executarLista("SELECT * FROM lote WHERE fk_produto = ? ORDER BY data_validade ASC, id ASC", fkProduto);
    }

    /**
     * Lotes activos com stock que vencem nos próximos 'dias' dias.
     */
    public List<Lote> listarAVencer(int dias) {
        return executarLista("SELECT * FROM lote WHERE estado = 'ACTIVO' AND qtd > 0 "
                + "AND data_validade BETWEEN CURDATE() AND DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
                + "ORDER BY data_validade ASC", dias);
    }

    /**
     * Lotes expirados que ainda têm stock (precisam de abate).
     */
    public List<Lote> listarExpiradosComStock() {
        return executarLista("SELECT * FROM lote WHERE estado = 'EXPIRADO' AND qtd > 0 ORDER BY data_validade ASC");
    }

    /**
     * Stock vendável do produto (lotes activos e dentro da validade).
     */
    public int getStock(int fkProduto) {
        String sql = "SELECT COALESCE( SUM( qtd ), 0 ) AS stock FROM lote "
                + "WHERE fk_produto = ? AND estado = 'ACTIVO' AND data_validade >= CURDATE()";
        try (PreparedStatement ps = conexao.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
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
     * Retira a quantidade dos lotes que vencem primeiro (FEFO). Se um lote não
     * chega, continua no seguinte.
     *
     * Equivalente ao @Transactional: NÃO faz commit nem rollback. Usa a
     * Connection (e a transacção) de quem chama.
     *
     * @throws IllegalStateException se o stock for insuficiente
     */
    public List<LoteMovimento> baixarStock(Connection conn, int fkProduto, int qtdPedida, int fkVenda) throws SQLException {
        String sql = "SELECT * FROM lote WHERE fk_produto = ? AND estado = 'ACTIVO' "
                + "AND qtd > 0 AND data_validade >= CURDATE() "
                + "ORDER BY data_validade ASC, id ASC FOR UPDATE"; // FEFO, com lock

        List<Lote> lotes = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lote lote = new Lote();
                    setResultSetLote(rs, lote);
                    lotes.add(lote);
                }
            }
        }

        int disponivel = lotes.stream().mapToInt(Lote::getQtd).sum();
        if (disponivel < qtdPedida) {
            throw new IllegalStateException("Stock insuficiente: " + disponivel);
        }

        int restante = qtdPedida;
        List<LoteMovimento> movimentos = new ArrayList<>();

        for (Lote lote : lotes) {
            if (restante == 0) {
                break;
            }

            int retirar = Math.min(lote.getQtd(), restante);
            int novaQtd = lote.getQtd() - retirar;

            actualizarQtdLote(conn, lote.getId(), novaQtd, novaQtd == 0 ? "ESGOTADO" : "ACTIVO");
            movimentos.add(inserirMovimento(conn, lote.getId(), fkVenda, "VENDA", retirar));

            restante -= retirar;
        }
        return movimentos;
    }

    /**
     * Versão com transacção própria (uso isolado). Faz commit se correr bem e
     * rollback se falhar.
     *
     * @throws IllegalStateException se o stock for insuficiente
     */
    public List<LoteMovimento> baixarStock(int fkProduto, int qtdPedida, int fkVenda) throws SQLException {
        Connection conn = conexao.getConnection();
        try {
            conn.setAutoCommit(false);
            List<LoteMovimento> movimentos = baixarStock(conn, fkProduto, qtdPedida, fkVenda);
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
     * para o primeiro.
     *
     * NÃO faz commit nem rollback. Usa a Connection (e a transacção) de quem
     * chama.
     *
     * @param fkVendaOrigem venda (FR/FT) a que a NC se refere
     * @throws IllegalStateException se a qtd exceder o vendido
     */
    public List<LoteMovimento> devolverStock(Connection conn, int fkVendaOrigem, int fkProduto, int qtdDevolver) throws SQLException {
        String sql = "SELECT m.id, m.fk_lote, m.qtd, m.qtd_devolvida "
                + "FROM lote_movimento m INNER JOIN lote l ON l.id = m.fk_lote "
                + "WHERE m.fk_venda = ? AND m.tipo = 'VENDA' AND l.fk_produto = ? "
                + "ORDER BY m.id DESC FOR UPDATE";

        // 1) lê os movimentos da venda original (e bloqueia as linhas)
        List<int[]> origem = new ArrayList<>(); // {idMov, fkLote, podeDevolver}
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fkVendaOrigem);
            ps.setInt(2, fkProduto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int podeDevolver = rs.getInt("qtd") - rs.getInt("qtd_devolvida");
                    if (podeDevolver > 0) {
                        origem.add(new int[]{
                            rs.getInt("id"), rs.getInt("fk_lote"), podeDevolver
                        });
                    }
                }
            }
        }

        int totalDevolvivel = 0;
        for (int[] o : origem) {
            totalDevolvivel += o[2];
        }
        if (totalDevolvivel < qtdDevolver) {
            throw new IllegalStateException("NC excede o vendido. Máximo a devolver: " + totalDevolvivel);
        }

        // 2) repõe do último lote retirado para o primeiro
        int restante = qtdDevolver;
        List<LoteMovimento> movimentos = new ArrayList<>();

        for (int[] o : origem) {
            if (restante == 0) {
                break;
            }

            int repor = Math.min(o[2], restante);
            reporQtdLote(conn, o[1], repor);
            marcarDevolvido(conn, o[0], repor);
            movimentos.add(inserirMovimento(conn, o[1], fkVendaOrigem, "DEVOLUCAO", repor));

            restante -= repor;
        }
        return movimentos;
    }

    /**
     * Versão com transacção própria (uso isolado). Faz commit se correr bem e
     * rollback se falhar.
     *
     * @throws IllegalStateException se a qtd exceder o vendido
     */
    public List<LoteMovimento> devolverStock(int fkVendaOrigem, int fkProduto, int qtdDevolver) throws SQLException {
        Connection conn = conexao.getConnection();
        try {
            //conn.setAutoCommit(false);
            List<LoteMovimento> movimentos = devolverStock(conn, fkVendaOrigem, fkProduto, qtdDevolver);
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
    private void actualizarQtdLote(Connection conn, int idLote, int novaQtd, String estado) throws SQLException {
        String sql = "UPDATE lote SET qtd = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, novaQtd);
            ps.setString(2, estado);
            ps.setInt(3, idLote);
            ps.executeUpdate();
        }
    }

    private void reporQtdLote(Connection conn, int idLote, int repor) throws SQLException {
        // Mantém DESACTIVO; senão recalcula EXPIRADO/ACTIVO pela validade
        String sql = "UPDATE lote SET qtd = qtd + ?, estado = CASE "
                + "WHEN estado = 'DESACTIVO' THEN 'DESACTIVO' "
                + "WHEN data_validade < CURDATE() THEN 'EXPIRADO' "
                + "ELSE 'ACTIVO' END WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, repor);
            ps.setInt(2, idLote);
            ps.executeUpdate();
        }
    }

    private void marcarDevolvido(Connection conn, int idMovimento, int qtd) throws SQLException {
        String sql = "UPDATE lote_movimento SET qtd_devolvida = qtd_devolvida + ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qtd);
            ps.setInt(2, idMovimento);
            ps.executeUpdate();
        }
    }

    private LoteMovimento inserirMovimento(Connection conn, int fkLote, int fkVenda, String tipo, int qtd) throws SQLException {
        String sql = "INSERT INTO lote_movimento( fk_lote, fk_venda, tipo, qtd, data_mov ) "
                + "VALUES( ?, ?, ?, ?, NOW() )";

        LoteMovimento mov = new LoteMovimento(fkLote, fkVenda, tipo, qtd);
        mov.setDataMov(new Date());

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, fkLote);
            ps.setInt(2, fkVenda);
            ps.setString(3, tipo);
            ps.setInt(4, qtd);
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
        lote.setNumeroLote(rs.getString("numero_lote"));
        lote.setDataEntrada(rs.getDate("data_entrada"));
        lote.setDataFabrica(rs.getDate("data_fabrica"));
        lote.setDataValidade(rs.getDate("data_validade"));
        lote.setQtd(rs.getInt("qtd"));
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
     * validade (mesmo critério usado em getStock e baixarStock).
     */
    public boolean existeLotesDisponiveis(int fkProduto) {
        String sql = "SELECT EXISTS( SELECT 1 FROM lote "
                + "WHERE fk_produto = ? AND estado = 'ACTIVO' "
                + "AND qtd > 0 AND data_validade >= CURDATE() ) AS existe";
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

    private void autoCommitSilencioso(Connection conn) {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
