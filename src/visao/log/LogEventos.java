package visao.log;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import visao.design.Formularios;

/**
 * Liga os eventos dos componentes aos métodos de tratamento da janela de
 * registo de actividades.
 */
final class LogEventos {

    private final LogActividadeVisao j;

    LogEventos(LogActividadeVisao janela) {
        this.j = janela;
    }

    void ligar() {
        // Período
        j.tgHoje.addActionListener(e -> j.aplicarPeriodo(0));
        j.tg7Dias.addActionListener(e -> j.aplicarPeriodo(7));
        j.tg30Dias.addActionListener(e -> j.aplicarPeriodo(30));
        j.tgEsteMes.addActionListener(e -> j.aplicarEsteMes());
        // mudar as datas à mão deixa de corresponder a um atalho
        j.dcInicio.aoAlterarPeloUtilizador(() -> j.grupoPeriodo.clearSelection());
        j.dcFim.aoAlterarPeloUtilizador(() -> j.grupoPeriodo.clearSelection());

        // Filtros
        j.txtPesquisa.addActionListener(e -> j.pesquisar());
        j.dcInicio.getCampo().addActionListener(e -> j.pesquisar());
        j.dcFim.getCampo().addActionListener(e -> j.pesquisar());
        j.btPesquisar.addActionListener(e -> j.pesquisar());
        j.btLimpar.addActionListener(e -> j.limparFiltros());

        // Tabela
        j.tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                j.mostrarDetalhe();
            }
        });
        j.tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    j.txtDepois.requestFocusInWindow();
                }
            }
        });

        // Paginação e rodapé
        j.btPrimeira.addActionListener(e -> j.irParaPagina(0));
        j.btAnterior.addActionListener(e -> j.irParaPagina(j.pagina - 1));
        j.btSeguinte.addActionListener(e -> j.irParaPagina(j.pagina + 1));
        j.btUltima.addActionListener(e -> j.irParaPagina(j.totalPaginas() - 1));
        j.btImprimir.addActionListener(e -> j.imprimir());
        j.btFechar.addActionListener(e -> j.dispose());

        // Atalhos de teclado
        Formularios.atalho(j.getRootPane(), "ESCAPE", j::dispose);
        Formularios.atalho(j.getRootPane(), "F5", j::pesquisar);
        j.getRootPane().setDefaultButton(j.btPesquisar);
    }
}
