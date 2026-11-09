package visao.cliente;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JOptionPane;
import util.ValidadorNifAGT;
import util.ValidadorNifAGT.Estado;
import util.ValidadorNifAGT.Resultado;

/**
 * Regra única para gravar um cliente: o NIF tem de ser validado (formato
 * AGT + registo na AGT) antes de salvar ou alterar.
 * <ul>
 * <li>formato inválido ou NIF não registado na AGT: <b>bloqueia</b>;</li>
 * <li>sem ligação à AGT (formato correcto): pede confirmação;</li>
 * <li>contribuinte inactivo na AGT: pede confirmação;</li>
 * <li>NIF registado e activo, consumidor final ou cliente estrangeiro: aceita.</li>
 * </ul>
 */
public final class ConfirmacaoNif {

    private ConfirmacaoNif() {
    }

    /**
     * Valida o NIF na thread actual, com o cursor de espera na janela
     * (pode demorar alguns segundos se a AGT estiver lenta).
     */
    public static Resultado validarComEspera(Component pai, String nif, String paisIso) {
        Cursor anterior = pai == null ? null : pai.getCursor();
        if (pai != null) {
            pai.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        try {
            return ValidadorNifAGT.validar(nif, paisIso);
        } finally {
            if (pai != null) {
                pai.setCursor(anterior);
            }
        }
    }

    /**
     * Aplica a regra de gravação a um resultado, mostrando ao utilizador o
     * erro ou o pedido de confirmação quando for o caso.
     *
     * @return true se o cliente pode ser guardado
     */
    public static boolean permiteGuardar(Component pai, Resultado r) {
        if (r == null) {
            return false;
        }
        if (r.isBloqueante()) {
            JOptionPane.showMessageDialog(pai, r.getMensagem()
                    + "\n\nO cliente não foi guardado: corrija o NIF.",
                    "NIF não aceite pela AGT", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (r.getEstado() == Estado.SEM_LIGACAO) {
            return confirmar(pai, "Não foi possível confirmar o NIF na AGT (sem ligação).\n"
                    + "O formato do NIF está correcto.\n\n"
                    + "Deseja guardar o cliente mesmo assim?\n"
                    + "Volte a validar o NIF quando houver ligação à internet.",
                    "NIF não confirmado");
        }
        if (r.isConfirmado() && !r.isActivo()) {
            return confirmar(pai, "O contribuinte " + (r.getNome() == null ? "" : "\"" + r.getNome() + "\" ")
                    + "está INACTIVO na AGT.\n\nDeseja guardar o cliente mesmo assim?",
                    "Contribuinte inactivo");
        }
        return true;
    }

    /**
     * Extrai o código ISO de um item de país no formato "Angola - AO".
     * Itens sem código (p.ex. "--seleccione--") devolvem "" (= Angola).
     */
    public static String isoDoPais(Object item) {
        String p = item == null ? "" : item.toString();
        int i = p.lastIndexOf(" - ");
        return i < 0 ? "" : p.substring(i + 3).trim();
    }

    private static boolean confirmar(Component pai, String mensagem, String titulo) {
        return JOptionPane.showConfirmDialog(pai, mensagem, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
