package visao.espelho;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import visao.design.AreaModerna;
import visao.design.Cabecalho;
import visao.design.Cartao;
import visao.design.FundoJanela;
import visao.design.PainelResponsivo;
import visao.design.PainelRolavel;
import visao.design.RolagemModerna;
import visao.design.Rodape;
import visao.design.Tema;
import static visao.design.Formularios.adicionarCampo;
import static visao.design.Formularios.gbc;
import static visao.design.Formularios.nota;
import static visao.design.Formularios.rotulo;
import static visao.design.Formularios.transparente;

/**
 * Disposição da janela do espelho: configuração em cima, registo da operação
 * a ocupar o resto. Os dois campos passam a empilhar-se abaixo de 640px.
 */
final class EspelhoBDLayout {

    private static final int QUEBRA_CAMPOS = 640;
    private static final int QUEBRA_RODAPE = 620;
    private static final int LARGURA_MINIMA = 520;

    private final EspelhoBDVisao j;

    EspelhoBDLayout(EspelhoBDVisao janela) {
        this.j = janela;
    }

    void montar() {
        FundoJanela raiz = new FundoJanela();
        Cabecalho cabecalho = new Cabecalho("BD", "Espelho da Base de Dados",
                "Mantém uma segunda base sempre igual à base em uso");
        cabecalho.adicionarDireita(j.lbEstado);
        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarRodape(), BorderLayout.SOUTH);
        j.setContentPane(raiz);

        Rectangle ecra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        j.setSize(Math.min(960, ecra.width - 40), Math.min(720, ecra.height - 40));
        j.setMinimumSize(new Dimension(Math.min(600, ecra.width), Math.min(520, ecra.height)));
    }

    private JComponent criarConteudo() {
        PainelRolavel conteudo = new PainelRolavel(new GridBagLayout(), LARGURA_MINIMA);
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        conteudo.add(criarCartaoConfiguracao(), gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH, new Insets(0, 0, Tema.ESPACO, 0)));
        conteudo.add(criarCartaoRegisto(), gbc(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0)));
        return new RolagemModerna(conteudo);
    }

    private JPanel criarCartaoConfiguracao() {
        Cartao cartao = new Cartao("Bases de dados", "origem (em uso) e espelho", Tema.PRIMARIA);
        cartao.setLayout(new BorderLayout());

        PainelResponsivo grelha = new PainelResponsivo(new int[]{QUEBRA_CAMPOS}, (p, modo) -> {
            if (modo == 0) {
                adicionarCampo(p, 0, 0, 1, rotulo("Base em uso (origem)"), j.txtOrigem);
                adicionarCampo(p, 0, 1, 1, rotulo("Base espelho (destino)"), j.txtDestino);
                p.add(j.ckBackup, gbc(0, 4, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0)));
                p.add(avisos(), gbc(0, 5, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
            } else {
                adicionarCampo(p, 0, 0, 1, rotulo("Base em uso (origem)"), j.txtOrigem);
                adicionarCampo(p, 1, 0, 1, rotulo("Base espelho (destino)"), j.txtDestino);
                p.add(j.ckBackup, gbc(0, 2, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 8, 0)));
                p.add(avisos(), gbc(0, 3, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
            }
        });
        cartao.add(grelha, BorderLayout.CENTER);
        return cartao;
    }

    private JPanel avisos() {
        JPanel p = transparente(new GridBagLayout());
        p.add(nota("Os dados da base em uso nunca são alterados; a base espelho fica igual a ela."),
                gbc(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0)));
        p.add(nota("Antes de actualizar, feche o sistema em todos os outros terminais."),
                gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0)));
        p.add(nota("Depois de alterar a estrutura da BD (novas tabelas/colunas), actualize de novo o espelho."),
                gbc(0, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0)));
        return p;
    }

    private JPanel criarCartaoRegisto() {
        Cartao cartao = new Cartao("Registo", "resultado da análise e passos da actualização", Tema.INFO);
        cartao.setLayout(new BorderLayout());
        AreaModerna area = new AreaModerna(j.txtRegisto);
        area.setPreferredSize(new Dimension(400, 260));
        cartao.add(area, BorderLayout.CENTER);
        return cartao;
    }

    private JPanel criarRodape() {
        JPanel esquerda = transparente(new FlowLayout(FlowLayout.LEFT, 0, 0));
        esquerda.add(j.btFechar);
        JPanel direita = transparente(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        direita.add(j.btAnalisar);
        direita.add(j.btActualizar);
        return new Rodape(esquerda, direita, QUEBRA_RODAPE);
    }
}
