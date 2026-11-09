package visao.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.ValidadorNifAGT;
import util.ValidadorNifAGT.Resultado;
import visao.design.BotaoModerno;
import visao.design.Formularios;
import visao.design.Tema;

/**
 * Faixa que mostra o resultado da validação do NIF na AGT: um ponto
 * colorido, um título curto e o detalhe (nome do contribuinte, tipo,
 * regime de IVA, situação...).
 */
public class PainelEstadoNif extends JPanel {

    private static final int RAIO = 12;

    private static final Color NEUTRO = new Color(100, 116, 139);
    private static final Color AVISO_TEXTO = new Color(180, 83, 9);
    private static final Color ERRO_TEXTO = new Color(185, 28, 28);
    private static final Color INFO_TEXTO = new Color(14, 116, 144);

    private final JLabel lbTitulo = new JLabel();
    private final JLabel lbDetalhe = new JLabel();
    private final BotaoModerno btnUsarDados = new BotaoModerno("Usar dados da AGT", Tema.SUCESSO, true);

    private Color cor = NEUTRO;
    private Color fundo = Tema.CAIXA;
    private Color borda = Tema.BORDA;

    public PainelEstadoNif() {
        super(new BorderLayout(12, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 34, 10, 12));

        lbTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbDetalhe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbDetalhe.setForeground(Tema.ROTULO);

        JPanel textos = Formularios.transparente(new BorderLayout(0, 2));
        textos.add(lbTitulo, BorderLayout.NORTH);
        textos.add(lbDetalhe, BorderLayout.CENTER);
        add(textos, BorderLayout.CENTER);

        btnUsarDados.setToolTipText("Preenche o nome, o endereço e os contactos com os dados registados na AGT");
        btnUsarDados.setVisible(false);
        JPanel direita = Formularios.transparente(new BorderLayout());
        direita.add(btnUsarDados, BorderLayout.NORTH);
        add(direita, BorderLayout.EAST);

        porValidar();
    }

    public BotaoModerno getBotaoUsarDados() {
        return btnUsarDados;
    }

    // ------------------------------------------------------------------
    // Estados
    // ------------------------------------------------------------------
    public void porValidar() {
        aplicar(NEUTRO, Tema.CAIXA, Tema.BORDA, "NIF por validar",
                "Prima Enter no NIF ou clique em «Validar na AGT».\nO cliente só é guardado depois de o NIF ser validado.");
    }

    public void aConsultar() {
        aplicar(INFO_TEXTO, new Color(236, 254, 255), new Color(165, 243, 252),
                "A consultar a AGT…", "A confirmar o registo do contribuinte no serviço da AGT.");
    }

    public void mostrar(Resultado r) {
        switch (r.getEstado()) {
            case VALIDO:
                if (r.getTipo() == ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL) {
                    sucesso("Consumidor final", "NIF genérico " + ValidadorNifAGT.NIF_CONSUMIDOR_FINAL
                            + "\ndefinido pela AGT para clientes sem NIF.");
                } else if (r.isActivo()) {
                    sucesso("NIF válido · registado na AGT", detalheContribuinte(r));
                } else {
                    aviso("NIF registado, mas INACTIVO na AGT", detalheContribuinte(r));
                }
                btnUsarDados.setVisible(r.getTipo() != ValidadorNifAGT.Tipo.CONSUMIDOR_FINAL && r.getNome() != null);
                break;
            case ESTRANGEIRO:
                aplicar(INFO_TEXTO, new Color(236, 254, 255), new Color(165, 243, 252),
                        "Contribuinte estrangeiro", r.getMensagem());
                break;
            case SEM_LIGACAO:
                aviso("Sem ligação à AGT", r.getTipo().getDescricao() + " · formato correcto, mas o registo não pôde ser confirmado.\n"
                        + "Verifique a ligação à internet e valide de novo.");
                break;
            case NAO_REGISTADO:
                erro("NIF não registado na AGT", r.getMensagem());
                break;
            default:
                erro("NIF inválido", r.getMensagem());
                break;
        }
    }

    private static String detalheContribuinte(Resultado r) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(html(r.getNome() == null ? "—" : r.getNome())).append("</b><br>");
        sb.append(r.getTipo().getDescricao());
        if (r.getRegimeIva() != null) {
            sb.append(" · IVA: ").append(html(descreverRegime(r.getRegimeIva())));
        }
        sb.append(" · Situação: ").append(r.isActivo() ? "Activo" : "<b>Inactivo</b>");
        return sb.toString();
    }

    private static String descreverRegime(String codigo) {
        switch (codigo.toUpperCase()) {
            case "GNAD":
                return "Regime geral";
            case "SIMP":
                return "Regime simplificado";
            case "EXCL":
                return "Regime de exclusão";
            default:
                return codigo;
        }
    }

    // ------------------------------------------------------------------
    private void sucesso(String titulo, String detalhe) {
        aplicar(Tema.SUCESSO_TEXTO, Tema.SUCESSO_FUNDO, Tema.SUCESSO_BORDA, titulo, detalhe);
    }

    private void aviso(String titulo, String detalhe) {
        aplicar(AVISO_TEXTO, new Color(255, 251, 235), new Color(253, 230, 138), titulo, detalhe);
    }

    private void erro(String titulo, String detalhe) {
        aplicar(ERRO_TEXTO, new Color(254, 242, 242), new Color(254, 202, 202), titulo, detalhe);
    }

    private void aplicar(Color cor, Color fundo, Color borda, String titulo, String detalhe) {
        this.cor = cor;
        this.fundo = fundo;
        this.borda = borda;
        btnUsarDados.setVisible(false);
        lbTitulo.setForeground(cor);
        lbTitulo.setText(titulo);
        String corpo = detalhe.startsWith("<b>") ? detalhe : html(detalhe).replace("\n", "<br>");
        lbDetalhe.setText("<html>" + corpo + "</html>");
        revalidate();
        repaint();
    }

    private static String html(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(200, super.getMinimumSize().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Formularios.suavizar(g);
        g2.setColor(fundo);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RAIO, RAIO);
        g2.setColor(borda);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RAIO, RAIO);
        // ponto de estado
        int d = 12;
        int y = 10 + (lbTitulo.getPreferredSize().height - d) / 2;
        g2.setColor(Tema.comAlfa(cor, 60));
        g2.fillOval(13, y - 3, d + 6, d + 6);
        g2.setColor(cor);
        g2.fillOval(16, y, d, d);
        g2.dispose();
    }
}
