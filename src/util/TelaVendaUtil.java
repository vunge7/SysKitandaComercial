package util;

import comercial.controller.DadosInstituicaoController;
import entity.TbDadosInstituicao;
import java.sql.SQLException;
import visao.FormVendaResponsivaVisaoTop;
import visao.venda.VendaModernaVisao;

/**
 * Abre a tela de vendas configurada em tb_dados_instituicao.tela_venda.
 * NORMAL (ou valor nulo/desconhecido) -> FormVendaResponsivaVisaoTop;
 * LATEST -> VendaModernaVisao.
 */
public final class TelaVendaUtil {

    private TelaVendaUtil() {
    }

    public static TelaVenda getTelaConfigurada(BDConexao conexao) {
        try {
            TbDadosInstituicao dados = (TbDadosInstituicao) new DadosInstituicaoController(conexao).findById(1);
            return TelaVenda.fromString(dados == null ? null : dados.getTelaVenda());
        } catch (Exception e) {
            e.printStackTrace();
            return TelaVenda.NORMAL;
        }
    }

    public static void abrir(int codUtilizador) throws SQLException {
        BDConexao conexao = BDConexao.getInstancia();
        if (getTelaConfigurada(conexao) == TelaVenda.LATEST) {
            new VendaModernaVisao(codUtilizador, conexao).setVisible(true);
        } else {
            new FormVendaResponsivaVisaoTop(codUtilizador, conexao).setVisible(true);
        }
    }
}
