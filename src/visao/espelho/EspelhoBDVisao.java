package visao.espelho;

import comercial.controller.DadosInstituicaoController;
import entity.TbDadosInstituicao;
import java.awt.Cursor;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import util.BDConexao;
import util.DVML;
import util.EspelhoBaseDados;
import util.TemaNegocioUtil;
import visao.design.BotaoModerno;
import visao.design.CampoModerno;
import visao.design.Formularios;
import visao.design.Selo;
import visao.design.Tema;

/**
 * Configurações &rarr; Espelho da Base de Dados.
 * <p>
 * Mantém uma segunda base (por omissão {@code <base>_1}) igual à base em uso,
 * através dos triggers gerados por {@link EspelhoBaseDados}. "Analisar" só lê;
 * "Actualizar espelho" prepara a base espelho, copia os dados e liga os
 * triggers. Os dados da base principal nunca são alterados.
 * <p>
 * Componentes e lógica aqui; a disposição está em {@link EspelhoBDLayout}.
 */
public class EspelhoBDVisao extends JFrame {

    private static EspelhoBDVisao aberta;

    private final BDConexao conexao;
    private boolean ocupado;

    // ---- componentes (usados por EspelhoBDLayout) ----
    final Selo lbEstado = new Selo("Por analisar");
    final JTextField txtOrigem = new CampoModerno();
    final JTextField txtDestino = new CampoModerno();
    final JCheckBox ckBackup = new JCheckBox("Fazer cópia de segurança da base espelho antes de a actualizar");
    final JTextArea txtRegisto = new JTextArea();
    final JButton btAnalisar = new BotaoModerno("Analisar", Tema.PRIMARIA, true);
    final JButton btActualizar = new BotaoModerno("Actualizar espelho", Tema.SUCESSO, false);
    final JButton btFechar = new BotaoModerno("Fechar", Tema.NEUTRA, true);

    /**
     * Acrescenta "Espelho da Base de Dados" ao fim do menu indicado (ex.:
     * Configurações), só para utilizadores do tipo Administrador. Chamado nos
     * construtores dos menus principais, fora do código gerado pelo NetBeans.
     */
    public static void adicionarAoMenu(javax.swing.JMenu menu, int codUsuario, BDConexao conexao) {
        if (!ehAdministrador(codUsuario)) {
            return;
        }
        javax.swing.JMenuItem item = new javax.swing.JMenuItem("Espelho da Base de Dados");
        item.addActionListener(e -> {
            try {
                abrir(conexao);
            } catch (Throwable ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Não foi possível abrir o Espelho da Base de Dados:\n" + ex,
                        "Espelho da Base de Dados", JOptionPane.ERROR_MESSAGE);
            }
        });
        menu.addSeparator();
        menu.add(item);
    }

    private static boolean ehAdministrador(int codUsuario) {
        try {
            entity.TbUsuario usuario = new dao.UsuarioDao(util.JPAEntityMannagerFactoryUtil.em).findTbUsuario(codUsuario);
            return usuario != null && usuario.getIdTipoUsuario() != null
                    && DVML.COD_TIPO_USUARIO_ADMINISTRADOR.equals(usuario.getIdTipoUsuario().getIdTipoUsuario());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Abre a janela (uma só instância). */
    public static void abrir(BDConexao conexao) {
        if (aberta == null || !aberta.isDisplayable()) {
            aberta = new EspelhoBDVisao(conexao);
        }
        aberta.setVisible(true);
        aberta.toFront();
    }

    public EspelhoBDVisao(BDConexao conexao) {
        this.conexao = conexao != null ? conexao : BDConexao.getInstancia();
        Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia(this.conexao)));
        setTitle("Espelho da Base de Dados");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        prepararComponentes();
        new EspelhoBDLayout(this).montar();
        setLocationRelativeTo(null);
        ligarEventos();
        analisar();
    }

    private static boolean ehFarmacia(BDConexao conexao) {
        try {
            TbDadosInstituicao dados = (TbDadosInstituicao) new DadosInstituicaoController(conexao).findById(1);
            return dados != null && TemaNegocioUtil.ehFarmacia(dados.getNegocio());
        } catch (Exception e) {
            return false;
        }
    }

    private void prepararComponentes() {
        String origem = "";
        try {
            origem = conexao.getConnectionAtiva().getCatalog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtOrigem.setText(origem);
        txtOrigem.setEditable(false);
        txtOrigem.setFont(Tema.CAMPO);
        txtDestino.setText(origem + "_1");
        txtDestino.setFont(Tema.CAMPO);
        Formularios.dimensionar(txtOrigem, 260, Tema.ALTURA_CAMPO);
        Formularios.dimensionar(txtDestino, 260, Tema.ALTURA_CAMPO);

        Formularios.estiloOpcao(ckBackup);
        ckBackup.setSelected(true);

        txtRegisto.setUI(new javax.swing.plaf.basic.BasicTextAreaUI());
        txtRegisto.setEditable(false);
        txtRegisto.setLineWrap(true);
        txtRegisto.setWrapStyleWord(true);
        txtRegisto.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 12));
        txtRegisto.setForeground(Tema.TEXTO);
        txtRegisto.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    private void ligarEventos() {
        btAnalisar.addActionListener(e -> analisar());
        btActualizar.addActionListener(e -> actualizar());
        btFechar.addActionListener(e -> fechar());
        Formularios.atalho(getRootPane(), "ESCAPE", this::fechar);
        Formularios.atalho(getRootPane(), "F5", this::analisar);
    }

    private void fechar() {
        if (ocupado) {
            JOptionPane.showMessageDialog(this, "Aguarde que a operação termine.", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        dispose();
    }

    // ------------------------------------------------------------------
    // Acções
    // ------------------------------------------------------------------
    private void analisar() {
        executar("A analisar...", false, espelho -> {
            EspelhoBaseDados.Analise a = espelho.analisar(true);
            registar("Base em uso: " + txtOrigem.getText() + " (" + a.tabelasOrigem + " tabelas)");
            if (!a.destinoExiste) {
                registar("A base espelho " + txtDestino.getText() + " ainda não existe; será criada ao actualizar.");
            } else {
                registar("Tabelas em falta no espelho: " + descrever(a.emFaltaNoDestino));
                registar("Tabelas com estrutura diferente: " + descrever(a.estruturaDiferente));
                registar("Tabelas com dados diferentes: " + descrever(a.dadosDiferentes));
            }
            if (!a.semChavePrimaria.isEmpty()) {
                registar("ATENÇÃO - tabelas sem chave primária (não podem ser espelhadas): " + a.semChavePrimaria);
            }
            registar("Triggers do espelho: " + a.triggersEspelho + " de " + (a.tabelasOrigem * 3)
                    + " | triggers antigos a substituir: " + a.triggersAfterAntigos
                    + " | triggers na base espelho (a remover): " + a.triggersNoDestino);
            boolean activo = a.espelhoActivo();
            registar(activo ? "Resultado: espelho ACTIVO e as duas bases estão iguais."
                    : "Resultado: o espelho precisa de ser actualizado.");
            return activo ? "Espelho activo" : "Precisa de actualização";
        });
    }

    private void actualizar() {
        String origem = txtOrigem.getText().trim();
        String destino = txtDestino.getText().trim();
        if (destino.isEmpty() || destino.equalsIgnoreCase(origem) || !destino.matches("[A-Za-z0-9_]+")) {
            JOptionPane.showMessageDialog(this, "Indique um nome válido para a base espelho (letras, números e _), diferente de "
                    + origem + ".", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        String mensagem = "<html><b>Antes de continuar, feche o sistema em todos os outros terminais.</b><br><br>"
                + "Os dados de <b>" + origem + "</b> não são alterados.<br>"
                + "A base <b>" + destino + "</b> vai ficar igual a " + origem + ": o que só existir lá será substituído"
                + (ckBackup.isSelected() ? " (será feita uma cópia de segurança antes)." : " <b>sem cópia de segurança</b>.")
                + "<br><br>Deseja actualizar o espelho agora?</html>";
        if (JOptionPane.showConfirmDialog(this, mensagem, getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        final boolean backup = ckBackup.isSelected();
        executar("A actualizar...", true, espelho -> {
            espelho.actualizar(backup, new File(DVML.CAMINHO_BK, "espelho"));
            return "Espelho activo";
        });
    }

    // ------------------------------------------------------------------
    // Execução em segundo plano (ligação própria, sem auditoria)
    // ------------------------------------------------------------------
    private interface Tarefa {

        String correr(EspelhoBaseDados espelho) throws Exception;
    }

    private void executar(String estado, boolean limparRegisto, Tarefa tarefa) {
        if (ocupado) {
            return;
        }
        ocupado = true;
        btAnalisar.setEnabled(false);
        btActualizar.setEnabled(false);
        txtDestino.setEditable(false);
        lbEstado.setText(estado);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (limparRegisto) {
            txtRegisto.setText("");
        }
        final String origem = txtOrigem.getText().trim();
        final String destino = txtDestino.getText().trim();

        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() throws Exception {
                try (Connection ligacao = conexao.novaLigacaoManutencao()) {
                    EspelhoBaseDados espelho = new EspelhoBaseDados(ligacao, origem, destino);
                    espelho.setRegisto(this::publish);
                    return tarefa.correr(espelho);
                }
            }

            @Override
            protected void process(List<String> linhas) {
                for (String l : linhas) {
                    registar(l);
                }
            }

            @Override
            protected void done() {
                ocupado = false;
                btAnalisar.setEnabled(true);
                btActualizar.setEnabled(true);
                txtDestino.setEditable(true);
                setCursor(Cursor.getDefaultCursor());
                try {
                    lbEstado.setText(get());
                } catch (Exception e) {
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    causa.printStackTrace();
                    lbEstado.setText("Erro");
                    registar("ERRO: " + causa.getMessage());
                    JOptionPane.showMessageDialog(EspelhoBDVisao.this, "A operação falhou:\n" + causa.getMessage()
                            + "\n\nPode repetir a actualização depois de corrigir o problema.", getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void registar(String linha) {
        Runnable r = () -> {
            txtRegisto.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "  " + linha + "\n");
            txtRegisto.setCaretPosition(txtRegisto.getDocument().getLength());
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    private static String descrever(List<String> lista) {
        if (lista == null) {
            return "-";
        }
        return lista.isEmpty() ? "nenhuma" : lista.size() + " " + lista;
    }
}
