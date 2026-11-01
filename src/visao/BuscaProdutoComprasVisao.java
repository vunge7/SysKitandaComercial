/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.DadosInstituicaoController;
import java.sql.Connection;
import comercial.controller.StoksController;
import enties.util.BuscaModeloProduto;
import entity.TbDadosInstituicao;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Objects;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.DVML;
import util.Definicoes;
import util.TemaNegocioUtil;
import util.tabela_manual.render.RenderBuscaModerno;
import visao.design.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BuscaProdutoComprasVisao extends javax.swing.JDialog {

    private Vector<BuscaModeloProduto> fonte_dados = null;
    private StoksController stoksController;
    private static DadosInstituicaoController dadosInstituicaoController;
    public static TbDadosInstituicao dadosInstituicao;

    private int cod_armazem = 0, cod_janela = 0;

    public BuscaProdutoComprasVisao(java.awt.Frame parent, boolean modal, int cod_armazem, int cod_janela, BDConexao conexao) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.cod_armazem = cod_armazem;
        this.cod_janela = cod_janela;
        txtDesignacao.requestFocus();
        stoksController = new StoksController(conexao);
        // Carregar os dados da instituição (estava a ficar null)
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        fonte_dados = stoksController.getFonteComprasSemRefeicao(cod_armazem, cod_janela);

        tabela_busca.setDefaultRenderer(Object.class, new RenderBuscaModerno());
        tabela_busca.setDefaultRenderer(Double.class, new RenderBuscaModerno());
        txtDesignacao.requestFocus();
        try {
            adicionar_tabela();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtDesignacao.addKeyListener(new TratarEvento());

        setBackGroundLegenda();

        boolean ehFarmacia = dadosInstituicao != null
                && TemaNegocioUtil.ehFarmacia(dadosInstituicao.getNegocio());

        // Campos do kit visual: tom suave da cor do negócio e anel de foco.
        Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia));

    }

    /**
     * Interface construída em código com o kit {@code visao.design} (sem
     * .form), no layout padrão dos formulários de pesquisa.
     */
    private void initComponents() {
        setUndecorated(true);

        txtDesignacao = new CampoPesquisa("Pesquisar produto pela designação...");

        // Legenda de estados (as cores dos pontos vêm de setBackGroundLegenda)
        jPanelStockVazio = Indicador.ponto();
        jPanelStockCritico = Indicador.ponto();
        jPanelStockNormal = Indicador.ponto();
        jPanelServico = Indicador.ponto();
        lbTotalStockVazio = new javax.swing.JLabel("0");
        lbTotalStockCritico = new javax.swing.JLabel("0");
        lbTotalStockNormal = new javax.swing.JLabel("0");
        lbTotalServico = new javax.swing.JLabel("0");
        jLabel4 = new javax.swing.JLabel("Stock vazio");
        jLabel5 = new javax.swing.JLabel("Stock crítico");
        jLabel6 = new javax.swing.JLabel("Stock normal");
        lbTiTulo = new javax.swing.JLabel("Serviço");
        jPanel3 = Formularios.transparente(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        jPanel3.add(new Indicador(jPanelStockNormal, jLabel6.getText(), lbTotalStockNormal));
        jPanel3.add(new Indicador(jPanelStockCritico, jLabel5.getText(), lbTotalStockCritico));
        jPanel3.add(new Indicador(jPanelStockVazio, jLabel4.getText(), lbTotalStockVazio));
        jPanel3.add(new Indicador(jPanelServico, lbTiTulo.getText(), lbTotalServico));

        tabela_busca = new TabelaModerna(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Cód.", "Designação", "Categoria", "Qtd", "Preço Venda", "Crítico"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tabela_busca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabela_buscaMouseClicked(evt);
            }
        });
        javax.swing.table.TableColumnModel colunas = tabela_busca.getColumnModel();
        colunas.getColumn(0).setPreferredWidth(70);
        colunas.getColumn(1).setPreferredWidth(420);
        colunas.getColumn(2).setPreferredWidth(160);
        colunas.getColumn(3).setPreferredWidth(70);
        colunas.getColumn(4).setPreferredWidth(120);
        colunas.getColumn(5).setPreferredWidth(70);
        jScrollPane1 = new RolagemModerna(tabela_busca);

        jLabel1 = new javax.swing.JLabel("Pesquisa de Produtos (Compras)");
        jLabel2 = new javax.swing.JLabel("TOTAL");
        lbTamnho = new Selo("0");
        jButton1 = new BotaoFechar();
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1 = new javax.swing.JPanel();

        LayoutPesquisa.para(this)
                .titulo("C", jLabel1.getText(), "Escreva para filtrar e faça duplo clique no produto a comprar")
                .campo(txtDesignacao)
                .legenda(jPanel3)
                .tabela(jScrollPane1)
                .total(jLabel2, lbTamnho)
                .fechar(jButton1)
                .atalhos("Duplo clique ou \"Seleccionar\": escolher     •     Esc: fechar")
                .aoSeleccionar(() -> {
                    // Reutiliza a lógica do duplo clique na linha seleccionada
                    if (tabela_busca.getSelectedRow() >= 0) {
                        tabela_buscaMouseClicked(new java.awt.event.MouseEvent(tabela_busca,
                                java.awt.event.MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(),
                                0, 0, 0, 2, false));
                    }
                })
                .montar();
    }

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_buscaMouseClicked
        // TODO add your handling code here:

        if (evt.getClickCount() == 2) {

            if (DVML.JANELA_ENTRADA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                EntradaPratosCompostosVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_VENDA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                VendaUsuarioVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_VENDA_POS == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                VendaPOSVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_VENDA_OFICINA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                VendaOficinaUsuarioVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_FICHA_TECNICA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                FichaTecnicaVisao.busca_produto_by_cod_interno(codigo);
                dispose();

            } else if (DVML.JANELA_COMPRA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                try {

                    NovaEncomendaVisao.busca_produto_by_cod_interno(codigo);

                } catch (Exception e) {
                }

                try {

                    CompraInformalVisao.busca_produto_by_cod_interno_compra(codigo);
                } catch (Exception e) {
                }

                try {
                    TranferenciaArmazemVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                } catch (Exception e) {
                }

                try {
                    AcertoStockVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                } catch (Exception e) {
                }

                dispose();

            } //            else if ( DVML.JANELA_TRANSFERENCIA == cod_janela )
            //            {
            //
            //                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
            //                int linha = tabela_busca.getSelectedRow();
            //                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
            //                TranferenciaArmazemVisao.accao_codigo_interno_enter_busca_exterior( codigo );
            //                dispose();
            //
            //            }
            //            else if ( DVML.ACERTO_STOCK == cod_janela )
            //            {
            //
            //                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
            //                int linha = tabela_busca.getSelectedRow();
            //                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
            //                AcertoStockVisao.accao_codigo_interno_enter_busca_exterior( codigo );
            //                dispose();
            //
            //            }
            else if (DVML.JANELA_ESTORNO == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                EstornoVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_PRODUTO == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                // ProdutosVisao.ver_dados(codigo);
                visao.produto.ProdutosModernoVisao.ver_dados(codigo);
                dispose();

            } else if ((Objects.nonNull(NovaGestaoPedidosVisao.rbNao_lugar) && NovaGestaoPedidosVisao.rbNao_lugar.isSelected())
                    && DVML.JANELA_RETAURANTE == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                NovaGestaoPedidosVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_VENDA_EXPRESSO == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                VendasPraticasVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_RECOLHA == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                RecolhaPedidosVisao.accao_codigo_interno_enter_busca_exterior(codigo);
                dispose();

            } else if (DVML.JANELA_ASSOCIACAO == cod_janela) {

                DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());
                AssociacaoServicoVisao.adcionarItem(codigo);
                dispose();

            }

        }
    }//GEN-LAST:event_tabela_buscaMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoComprasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoComprasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoComprasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoComprasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscaProdutoComprasVisao dialog = new BuscaProdutoComprasVisao(new javax.swing.JFrame(), true, 1, 0, BDConexao.getInstancia());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelServico;
    private javax.swing.JPanel jPanelStockCritico;
    private javax.swing.JPanel jPanelStockNormal;
    private javax.swing.JPanel jPanelStockVazio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbTamnho;
    private javax.swing.JLabel lbTiTulo;
    private javax.swing.JLabel lbTotalServico;
    private javax.swing.JLabel lbTotalStockCritico;
    private javax.swing.JLabel lbTotalStockNormal;
    private javax.swing.JLabel lbTotalStockVazio;
    private javax.swing.JTable tabela_busca;
    private javax.swing.JTextField txtDesignacao;
    // End of variables declaration//GEN-END:variables

    class TratarEvento implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtDesignacao.getText().trim() + key;
                //adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                adicionar(prefixo);

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {

                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    // adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                    adicionar(prefixo);

                } catch (Exception e) {

                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }

    }

    private void adicionar(String designacao) {
        int contTotal = 0, contTotalStockVazio = 0, contTotalStockCritco = 0, contTotalStockNormal = 0, contTotalServico = 0;
        Iterator<BuscaModeloProduto> iterator = fonte_dados.iterator();
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        modelo.setRowCount(0);

        while (iterator.hasNext()) {

            BuscaModeloProduto next = iterator.next();
            if (next.getDesignacao().toLowerCase().contains(designacao.toLowerCase())) {
                contTotal++;

                if (next.getQtd().equals("0")) {
                    contTotalStockVazio++;
                }

                if (next.getEstadoCritico().equals("true")) {
                    contTotalStockCritco++;
                }

                if (next.getQtd().equals("-")) {
                    contTotalServico++;
                } else if (Double.parseDouble(next.getQtd()) != 0 && next.getEstadoCritico().equals("false")) {
                    contTotalStockNormal++;
                }

                modelo.addRow(
                        new Object[]{
                            next.getCodigo(),
                            next.getDesignacao(),
                            next.getCategoria(),
                            next.getQtd(),
                            next.getPrecoVenda(),
                            next.getEstadoCritico()

                        }
                );

            }

        }

        lbTotalStockVazio.setText(String.valueOf(contTotalStockVazio));

        lbTotalServico.setText(String.valueOf(contTotalServico));
        lbTotalStockCritico.setText(String.valueOf(contTotalStockCritco));
        lbTotalStockNormal.setText(String.valueOf(contTotalStockNormal));
        lbTamnho.setText(String.valueOf(contTotal));
    }

    private void adicionar_tabela() {

        int contTotal = 0, contTotalStockVazio = 0, contTotalStockCritco = 0, contTotalStockNormal = 0, contTotalServico = 0;
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        modelo.setRowCount(0);
        Iterator<BuscaModeloProduto> iterator = fonte_dados.iterator();
        System.err.println("SIZE-ITERATOR: " + fonte_dados.size());
        while (iterator.hasNext()) {
            contTotal++;

            BuscaModeloProduto next = iterator.next();

            if (next.getQtd().equals("0")) {
                contTotalStockVazio++;
            }

            if (next.getEstadoCritico().equals("true")) {
                contTotalStockCritco++;
            }

            if (next.getQtd().equals("-")) {
                contTotalServico++;
            } else if (Double.parseDouble(next.getQtd()) != 0 && next.getEstadoCritico().equals("false")) {
                contTotalStockNormal++;
            }

            modelo.addRow(
                    new Object[]{
                        next.getCodigo(),
                        next.getDesignacao(),
                        next.getCategoria(),
                        next.getQtd(),
                        next.getPrecoVenda(),
                        next.getEstadoCritico()

                    }
            );

        }
        lbTotalStockVazio.setText(String.valueOf(contTotalStockVazio));
        lbTotalServico.setText(String.valueOf(contTotalServico));
        lbTotalStockCritico.setText(String.valueOf(contTotalStockCritco));
        lbTotalStockNormal.setText(String.valueOf(contTotalStockNormal));
        lbTamnho.setText(String.valueOf(contTotal));

    }
//    private void adicionar_tabela()
//    {
//
//        int contTotal = 0, contTotalStockVazio = 0, contTotalStockCritco = 0, contTotalStockNormal = 0, contTotalServico = 0;
//        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//        modelo.setRowCount( 0 );
//        Iterator<BuscaModeloProduto> iterator = fonte_dados.iterator();
//        System.err.println( "SIZE-ITERATOR: " +iterator.le );
//        while ( iterator.hasNext() )
//        {
//            contTotal++;
//
//            BuscaModeloProduto next = iterator.next();
//
//            if ( next.getQtd().equals( "0" ) )
//            {
//                contTotalStockVazio++;
//            }
//
//            if ( next.getEstadoCritico().equals( "true" ) )
//            {
//                contTotalStockCritco++;
//            }
//
//            if ( next.getQtd().equals( "-" ) )
//            {
//                contTotalServico++;
//            }
//            else if ( Double.parseDouble( next.getQtd() ) != 0 && next.getEstadoCritico().equals( "false" ) )
//            {
//                contTotalStockNormal++;
//            }
//
//            modelo.addRow(
//                    new Object[]
//                    {
//                        next.getCodigo(),
//                        next.getDesignacao(),
//                        next.getCategoria(),
//                        next.getQtd(),
//                        next.getPrecoVenda(),
//                        next.getEstadoCritico()
//
//                    }
//            );
//
//        }
//        lbTotalStockVazio.setText( String.valueOf( contTotalStockVazio ) );
//        lbTotalServico.setText( String.valueOf( contTotalServico ) );
//        lbTotalStockCritico.setText( String.valueOf( contTotalStockCritco ) );
//        lbTotalStockNormal.setText( String.valueOf( contTotalStockNormal ) );
//        lbTamnho.setText( String.valueOf( contTotal ) );
//
//    }

    private void setBackGroundLegenda() {
        jPanelStockCritico.setBackground(new Color(Definicoes.PRODUTO_CRITICO[0],
                Definicoes.PRODUTO_CRITICO[1], Definicoes.PRODUTO_CRITICO[2]));
        jPanelStockNormal.setBackground(new Color(Definicoes.PRODUTO[0],
                Definicoes.PRODUTO[1], Definicoes.PRODUTO[2]));
        jPanelStockVazio.setBackground(new Color(Definicoes.STOCK_VAZIO[0],
                Definicoes.STOCK_VAZIO[1], Definicoes.STOCK_VAZIO[2]));
        jPanelServico.setBackground(new Color(Definicoes.SERVICOS[0],
                Definicoes.SERVICOS[1], Definicoes.SERVICOS[2]));
    }

}
