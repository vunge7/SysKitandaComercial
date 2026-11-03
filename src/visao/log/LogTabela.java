package visao.log;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import modelo.LogActividade;
import util.LogActividadeUtil;
import visao.design.Formularios;
import visao.design.RenderizadorModerno;
import visao.design.Tema;

/**
 * Modelo e renderizadores da tabela de registos de actividade.
 * <p>
 * A acção e a descrição passam sempre por {@link LogActividadeUtil#accaoVisivel}
 * e {@link LogActividadeUtil#descricaoVisivel}: o utilizador nunca vê SQL.
 */
final class LogTabela {

    static final int COL_DATA = 0;
    static final int COL_ACCAO = 3;
    static final int COL_DESCRICAO = 7;

    static final SimpleDateFormat FORMATO_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private LogTabela() {
    }

    /** Larguras preferidas das colunas. */
    static final int[] LARGURAS = {160, 150, 100, 125, 175, 125, 90, 460, 140};

    static final class Modelo extends AbstractTableModel {

        private static final String[] COLUNAS = {
            "Data / Hora", "Utilizador", "Módulo", "Acção", "Ecrã", "Tabela", "Nº Reg.", "Descrição", "Máquina"
        };

        private List<LogActividade> registos = new ArrayList<>();

        void setRegistos(List<LogActividade> registos) {
            this.registos = registos;
            fireTableDataChanged();
        }

        LogActividade getRegisto(int linha) {
            return registos.get(linha);
        }

        @Override
        public int getRowCount() {
            return registos.size();
        }

        @Override
        public int getColumnCount() {
            return COLUNAS.length;
        }

        @Override
        public String getColumnName(int coluna) {
            return COLUNAS[coluna];
        }

        @Override
        public Class<?> getColumnClass(int coluna) {
            return coluna == COL_DATA ? Date.class : String.class;
        }

        @Override
        public Object getValueAt(int linha, int coluna) {
            LogActividade l = registos.get(linha);
            switch (coluna) {
                case 0:
                    return l.getDataHora();
                case 1:
                    return l.getNomeUsuario() != null ? l.getNomeUsuario() : "(sem sessão)";
                case 2:
                    return l.getModulo();
                case 3:
                    return LogActividadeUtil.accaoVisivel(l.getAccao());
                case 4:
                    return l.getFormulario();
                case 5:
                    return l.getEntidade();
                case 6:
                    return l.getIdRegisto();
                case 7:
                    return LogActividadeUtil.descricaoVisivel(l.getDescricao());
                default:
                    return l.getNomeMaquina();
            }
        }
    }

    /**
     * Células de texto: data formatada, colunas secundárias em tom suave e
     * barra de cor (da acção) à esquerda da primeira coluna.
     */
    static class Renderizador extends RenderizadorModerno {

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean seleccionado,
                boolean foco, int linha, int coluna) {
            Object texto = valor instanceof Date ? FORMATO_DATA_HORA.format((Date) valor) : valor;
            super.getTableCellRendererComponent(tabela, texto, seleccionado, foco, linha, coluna);
            if (!seleccionado && (coluna == 2 || coluna == 4 || coluna == 5 || coluna == 8)) {
                setForeground(Tema.TEXTO_SUAVE);
            }
            setToolTipText(coluna == COL_DESCRICAO && valor != null
                    ? "<html><div style='width:420px'>" + escapar(valor.toString()) + "</div></html>" : null);
            return this;
        }

        @Override
        protected Color corDoIndicador(JTable tabela, int linha) {
            Object accao = tabela.getValueAt(linha, COL_ACCAO);
            return coresAccao(accao == null ? null : accao.toString())[0];
        }

        private static String escapar(String s) {
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }

    /** Coluna "Acção": etiqueta arredondada com a cor da acção. */
    static final class RenderizadorAccao extends Renderizador {

        private String accao = "";
        private Color[] cores;

        @Override
        public Component getTableCellRendererComponent(JTable tabela, Object valor, boolean seleccionado,
                boolean foco, int linha, int coluna) {
            super.getTableCellRendererComponent(tabela, valor, seleccionado, foco, linha, coluna);
            accao = valor == null ? "" : valor.toString().replace('_', ' ');
            cores = coresAccao(valor == null ? null : valor.toString());
            setText("");
            return this;
        }

        @Override
        protected Color corDoIndicador(JTable tabela, int linha) {
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (accao.isEmpty()) {
                return;
            }
            Graphics2D g2 = Formularios.suavizar(g);
            g2.setFont(Tema.TITULO_CAIXA);
            FontMetrics fm = g2.getFontMetrics();
            int altura = 22;
            int largura = Math.min(fm.stringWidth(accao) + 20, getWidth() - 20);
            int x = 12;
            int y = (getHeight() - altura) / 2;
            g2.setColor(cores[1]);
            g2.fillRoundRect(x, y, largura, altura, altura, altura);
            g2.setColor(Tema.misturarComBranco(cores[0], 0.25f));
            g2.drawRoundRect(x, y, largura - 1, altura - 1, altura, altura);
            g2.setColor(cores[0]);
            g2.drawString(accao, x + 10, y + (altura + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }

    /** { cor do texto/indicador, cor de fundo da etiqueta } de cada acção. */
    static Color[] coresAccao(String accao) {
        Color cor;
        switch (accao == null ? "" : accao) {
            case LogActividadeUtil.CRIAR:
            case LogActividadeUtil.ABERTURA_CAIXA:
                cor = Tema.SUCESSO_TEXTO;
                break;
            case LogActividadeUtil.ALTERAR:
            case LogActividadeUtil.ESTRUTURA:
                cor = Tema.AVISO;
                break;
            case LogActividadeUtil.ELIMINAR:
            case LogActividadeUtil.ANULAR:
            case LogActividadeUtil.LOGIN_FALHADO:
                cor = Tema.PERIGO;
                break;
            case LogActividadeUtil.LOGIN:
            case LogActividadeUtil.LOGOUT:
            case LogActividadeUtil.FECHO_CAIXA:
                cor = Tema.PRIMARIA;
                break;
            case LogActividadeUtil.IMPRIMIR:
            case LogActividadeUtil.VISUALIZAR:
                cor = new Color(126, 34, 206);
                break;
            default:
                cor = Tema.NEUTRA;
        }
        return new Color[]{cor, Tema.misturarComBranco(cor, 0.10f)};
    }
}
