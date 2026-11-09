package util.permissoes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;
import util.BDConexao;

/**
 * Permissões dinâmicas: um formulário novo fica logo preparado nas Permissões,
 * sem desenhar caixas à mão.
 *
 * <p>
 * Como funciona: ao abrir o menu principal, cada item de menu que não faz
 * parte da {@link #BASE_MENU_COMERCIAL base} (os itens que já existiam e têm
 * as suas regras próprias) é tratado como um formulário novo:
 * <ol>
 * <li>é registado sozinho como permissão (tb_permissao) e no catálogo
 * tb_permissao_formulario, com o grupo = nome do menu onde está;</li>
 * <li>só fica activo para quem tiver essa permissão;</li>
 * <li>o ecrã de Permissões mostra-o num cartão gerado a partir do catálogo, e
 * grava/lê como as outras permissões (tb_item_permissao).</li>
 * </ol>
 * Para um formulário aberto noutro sítio (botão, outro menu) basta chamar
 * {@link #registar(String, String, String)} uma vez e usar
 * {@link #temPermissao(int, String, String)}.
 * <p>
 * Tudo o que mexe na base de dados corre fora do ecrã (SwingWorker) e, se
 * falhar, falha em silêncio no registo: nunca impede de abrir uma janela.
 */
public final class RegistoPermissoes {

    private static final Logger LOG = Logger.getLogger(RegistoPermissoes.class.getName());

    /** Itens do menu principal que já existiam (têm regras próprias no código). */
    public static final Set<String> BASE_MENU_COMERCIAL = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "Acerto Stock Em Massa", "Acerto Stock", "Actualização de Preços",
            "Alteração da Guia Transporte", "Alteração da Senha", "Anulamentos Entradas Produtos",
            "Anulamentos Saidas Produtos", "Autorizacao Compras", "Cadastrar Usuario",
            "Cadastro de Armazem", "Cadastro de Cliente", "Cadastro de Vasilhames",
            "Cancelamento de Entrada", "Cardex", "Compra Por Fazer",
            "Compras", "Configuração Sistema", "Consultar Estado",
            "Consultas", "Converter Guia Transporte Em Documento", "Dados da Empresa",
            "Emitir nota de credito para anulação de compras", "Emitir nota de credito para anulação de facturas", "Emitir nota de credito para rectificação de facturas",
            "Encomendas", "Entrada de Produtos", "Entrada de Vasilhames",
            "Familia", "Front Office", "Gavetas/Prateleiras",
            "Gerar SAFT de Compras", "Gerar SAFT de Vendas", "Gestao de Creditos",
            "Gestao de Turno", "Grupo", "Guia Transporte",
            "Historico de Bonus da Empresa", "Imprimir Precos", "Inventario",
            "Lista de Clientes", "Listagens de Todos os Produtos com Desconto", "Listar Usuarios",
            "Listar Vasilhames", "Mapa de Existencia", "Marca",
            "Modelo", "Nota de Levantamento", "Operação Inverter IVA",
            "Percentagem de Desconto", "Permissões", "Processar Recibos de Factura",
            "Produto", "Quebras", "Reactivar Produtos",
            "Reimprimir Compra", "Reimprimir Factura", "Reimprimir Nota Credito",
            "Reimprimir Saidas de Produtos", "Relatorio Detalhado de Quantidades Vendidos", "Relatorio Diario de Todas Vendas em Tempo Real",
            "Relatorio Mensal de Clientes Vip", "Relatorio Mensal", "Relatorio Quebras",
            "Relatorio Transferencias Armazem", "Relatorio de Acerto de Stock", "Relatorio de Compras por Fornecedores",
            "Relatorio de Entradas", "Relatorio de Notas de Credito de Compras", "Relatorio de Notas de Creditos",
            "Relatorio de Quantidades de Produtos Comprados", "Relatorio de Quatidades de Produtos Vendidos", "Relatorio de Todos Produtos",
            "Relatorio de Todos Servicos", "Relatorio de Vendas Por Usuario e Data", "Relatorio de Vendas por Cliente e Data",
            "Relatorio de Vendas por Formas de Pagamentos", "Relatorio de Vendas por Intervalo de Datas", "Relatorios Saidas de Produtos por Datas",
            "Relatório Mensalidades", "Relatório de Entradas", "Relatório de Saidas",
            "Relatório por Fornecedor", "Saida de Vasilhames", "Saidas Produtos",
            "Sair", "Sobre o Autor", "Solicitacao Compras",
            "Solicitar Séries", "SubFamilia", "Transferencia de Armazem",
            "Unidades", "Venda", "Vendas Detalhadas por Clientes",
            "Vendas Detalhadas por Usuarios"
    )));

    private static volatile boolean tabelaVerificada = false;

    private RegistoPermissoes() {
    }

    /** Uma permissão do catálogo: grupo (menu) e descrição (texto do item). */
    public static final class Entrada {

        public final String grupo;
        public final String descricao;

        Entrada(String grupo, String descricao) {
            this.grupo = grupo;
            this.descricao = descricao;
        }
    }

    // =====================================================================
    // BASE DE DADOS
    // =====================================================================
    private static BDConexao conexao() {
        return BDConexao.getInstancia();
    }

    /** Cria o catálogo se ainda não existir (uma vez por execução). */
    private static synchronized void garantirTabela() throws SQLException {
        if (tabelaVerificada) {
            return;
        }
        try (PreparedStatement ps = conexao().getPreparedStatement(
                "CREATE TABLE IF NOT EXISTS tb_permissao_formulario ("
                + " id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + " modulo VARCHAR(10) NOT NULL,"
                + " grupo VARCHAR(120) NULL,"
                + " descricao VARCHAR(255) NOT NULL,"
                + " criado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + " UNIQUE KEY uk_permissao_formulario (modulo, descricao)"
                + ")")) {
            ps.executeUpdate();
        }
        tabelaVerificada = true;
    }

    /**
     * Garante a permissão (tb_permissao) e a entrada no catálogo. Pode ser
     * chamado as vezes que se quiser: não duplica nada.
     */
    public static void registar(String modulo, String grupo, String descricao) throws SQLException {
        if (descricao == null || descricao.trim().isEmpty()) {
            return;
        }
        String d = descricao.trim();
        garantirTabela();
        boolean existe;
        try (PreparedStatement ps = conexao().getPreparedStatement(
                "SELECT 1 FROM tb_permissao WHERE descricao = ? LIMIT 1")) {
            ps.setString(1, d);
            try (ResultSet rs = ps.executeQuery()) {
                existe = rs.next();
            }
        }
        if (!existe) {
            try (PreparedStatement ps = conexao().getPreparedStatement(
                    "INSERT INTO tb_permissao (descricao) VALUES (?)")) {
                ps.setString(1, d);
                ps.executeUpdate();
            }
        }
        try (PreparedStatement ps = conexao().getPreparedStatement(
                "INSERT INTO tb_permissao_formulario (modulo, grupo, descricao) VALUES (?, ?, ?)"
                + " ON DUPLICATE KEY UPDATE grupo = VALUES(grupo)")) {
            ps.setString(1, modulo);
            ps.setString(2, grupo);
            ps.setString(3, d);
            ps.executeUpdate();
        }
    }

    /** Catálogo do módulo, por grupo e descrição. */
    public static List<Entrada> listar(String modulo) {
        List<Entrada> lista = new ArrayList<>();
        try {
            garantirTabela();
            try (PreparedStatement ps = conexao().getPreparedStatement(
                    "SELECT grupo, descricao FROM tb_permissao_formulario WHERE modulo = ? ORDER BY grupo, descricao")) {
                ps.setString(1, modulo);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String g = rs.getString("grupo");
                        lista.add(new Entrada(g == null || g.trim().isEmpty() ? "Outros" : g.trim(), rs.getString("descricao")));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Catálogo de permissões indisponível", e);
        }
        return lista;
    }

    /** Descrições das permissões que o utilizador tem no módulo. */
    public static Set<String> permissoesDoUtilizador(int idUsuario, String modulo) {
        Set<String> s = new HashSet<>();
        try (PreparedStatement ps = conexao().getPreparedStatement(
                "SELECT p.descricao FROM tb_item_permissao i JOIN tb_permissao p ON p.idPermissao = i.idPermissao"
                + " WHERE i.idUsuario = ? AND i.modulo = ?")) {
            ps.setInt(1, idUsuario);
            ps.setString(2, modulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    s.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Não foi possível ler as permissões do utilizador " + idUsuario, e);
        }
        return s;
    }

    public static boolean temPermissao(int idUsuario, String modulo, String descricao) {
        return permissoesDoUtilizador(idUsuario, modulo).contains(descricao);
    }

    // =====================================================================
    // MENU PRINCIPAL
    // =====================================================================
    /**
     * Itens novos do menu (fora da base): ficam desactivados até se saber se o
     * utilizador os pode usar; em segundo plano registam-se no catálogo e lêem-se
     * as permissões; depois activam-se os permitidos.
     */
    public static void protegerFormulariosNovos(JMenuBar barra, final Set<String> base, final String modulo,
            final int idUsuario) {
        final Map<JMenuItem, String> novos = new LinkedHashMap<>();
        for (int i = 0; i < barra.getMenuCount(); i++) {
            JMenu menu = barra.getMenu(i);
            if (menu != null) {
                recolherNovos(menu, menu.getText(), base, novos);
            }
        }
        if (novos.isEmpty()) {
            return;
        }
        for (JMenuItem item : novos.keySet()) {
            item.setEnabled(false);
            item.setToolTipText("Formulário novo: dê a permissão \"" + item.getText() + "\" em Permissões");
        }
        new SwingWorker<Set<String>, Void>() {
            @Override
            protected Set<String> doInBackground() {
                for (Map.Entry<JMenuItem, String> e : novos.entrySet()) {
                    try {
                        registar(modulo, e.getValue(), e.getKey().getText());
                    } catch (SQLException ex) {
                        LOG.log(Level.WARNING, "Não foi possível registar a permissão " + e.getKey().getText(), ex);
                    }
                }
                return permissoesDoUtilizador(idUsuario, modulo);
            }

            @Override
            protected void done() {
                try {
                    Set<String> tem = get();
                    for (JMenuItem item : novos.keySet()) {
                        boolean pode = tem.contains(item.getText().trim());
                        item.setEnabled(pode);
                        item.setToolTipText(pode ? null
                                : "Sem permissão: peça ao administrador a permissão \"" + item.getText() + "\"");
                    }
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Permissões dos formulários novos não aplicadas", ex);
                }
            }
        }.execute();
    }

    private static void recolherNovos(JMenu menu, String grupo, Set<String> base, Map<JMenuItem, String> novos) {
        for (java.awt.Component c : menu.getMenuComponents()) {
            if (c instanceof JMenu) {
                recolherNovos((JMenu) c, grupo, base, novos);
            } else if (c instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) c;
                String t = item.getText() == null ? "" : item.getText().trim();
                if (!t.isEmpty() && !base.contains(t)) {
                    novos.put(item, grupo);
                }
            }
        }
    }
}
