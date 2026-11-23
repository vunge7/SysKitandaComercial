package util.ui;

import java.awt.Component;
import javax.swing.JOptionPane;
import util.BDConexao;

/**
 * Formulários só para administradores (tipo de utilizador 1), pedido do
 * utilizador em 2026-11-14: Séries dos Documentos,
 * Relatórios por E-mail e por WhatsApp. A verificação está na abertura do próprio
 * formulário: vale para qualquer caminho (menu, pesquisa de formulários...).
 * A Abertura e o Fecho de Caixa continuam para todos (são obrigatórios para
 * quem recebe pagamentos).
 */
public final class SoAdministrador {

    private SoAdministrador() {
    }

    /** true se o utilizador da sessão é administrador; senão avisa e devolve false. */
    public static boolean verificar(Component pai, String formulario) {
        if (administrador()) {
            return true;
        }
        JOptionPane.showMessageDialog(pai, "Só os administradores podem abrir \"" + formulario + "\".\n"
                + "Peça a um administrador do sistema.", formulario, JOptionPane.WARNING_MESSAGE);
        return false;
    }

    /** Para os botões dos menus: desactivado e com a explicação na dica, se não for administrador. */
    public static void soParaAdministrador(javax.swing.AbstractButton b) {
        if (b != null && !administrador()) {
            b.setEnabled(false);
            b.setToolTipText("Só para administradores");
        }
    }

    /** O utilizador da sessão (util.SessaoUtil) é do tipo 1 (Administrador). Sem sessão: não. */
    public static boolean administrador() {
        Integer codigo = util.SessaoUtil.getCodUsuario();
        if (codigo == null || codigo <= 0) {
            return false;
        }
        try (java.sql.PreparedStatement ps = BDConexao.getInstancia().getConnectionAtiva()
                .prepareStatement("SELECT idTipoUsuario FROM tb_usuario WHERE codigo = ?")) {
            ps.setInt(1, codigo);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
