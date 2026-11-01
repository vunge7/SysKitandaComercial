/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import visao.produto.ProdutosModernoVisao;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.ProdutosImpostoController;
import comercial.controller.StoksController;
import enties.util.BuscaModeloProduto;
import entity.TbDadosInstituicao;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
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
public class BuscaProdutoVisao extends javax.swing.JDialog {

    private Vector<BuscaModeloProduto> fonte_dados = null;
    private StoksController stoksController;
    private static ProdutosImpostoController produtosImpostoController;
    private int cod_armazem = 0, cod_janela = 0;
    private static DadosInstituicaoController dadosInstituicaoController;
    public static TbDadosInstituicao dadosInstituicao;

    public BuscaProdutoVisao(java.awt.Frame parent, boolean modal, int cod_armazem, int cod_janela, BDConexao conexao) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        this.cod_armazem = cod_armazem;
        this.cod_janela = cod_janela;

        stoksController = new StoksController(conexao);
        produtosImpostoController = new ProdutosImpostoController(conexao);
        // Carregar os dados da instituição (estava a ficar null)
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        fonte_dados = stoksController.getFonte(cod_armazem, cod_janela);

        tabela_busca.setDefaultRenderer(Object.class, new RenderBuscaModerno());
        tabela_busca.setDefaultRenderer(Double.class, new RenderBuscaModerno());
        tabela_busca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela_busca.setRowSelectionAllowed(true);
        tabela_busca.setFillsViewportHeight(true);

        txtDesignacao.requestFocus();
        txtDesignacao.addKeyListener(new TratarEvento());

        setBackGroundLegenda();

        try {
            adicionar_tabela();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Selecionar a primeira linha ao abrir
        if (tabela_busca.getRowCount() > 0) {
            tabela_busca.setRowSelectionInterval(0, 0);
        }

        // Atalho F4 e ENTER para tratar seleção
        InputMap im = tabela_busca.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = tabela_busca.getActionMap();

// F4
        im.put(KeyStroke.getKeyStroke("F4"), "acaoF4");
// ENTER
        im.put(KeyStroke.getKeyStroke("ENTER"), "acaoEnter");

// Mesma ação para F4 e ENTER
        AbstractAction acaoSelecionar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = tabela_busca.getSelectedRow();
                if (linha >= 0) {
                    tratarSelecaoTabela(linha);
                }
            }
        };

        am.put("acaoF4", acaoSelecionar);
        am.put("acaoEnter", acaoSelecionar);

        // Atalho DOWN no próprio JTable
        tabela_busca.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        tabela_busca.getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabela_busca.getSelectedRow();
                int rowCount = tabela_busca.getRowCount();
                if (selectedRow < rowCount - 1) {
                    int next = selectedRow + 1;
                    tabela_busca.setRowSelectionInterval(next, next);
                    tabela_busca.scrollRectToVisible(tabela_busca.getCellRect(next, 0, true));
                }
            }
        });

        // Atalho DOWN também quando o foco está no txtDesignacao
        txtDesignacao.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                    tabela_busca.requestFocus();
                    int selectedRow = tabela_busca.getSelectedRow();
                    int rowCount = tabela_busca.getRowCount();
                    if (selectedRow < rowCount - 1) {
                        int next = selectedRow + 1;
                        tabela_busca.setRowSelectionInterval(next, next);
                        tabela_busca.scrollRectToVisible(tabela_busca.getCellRect(next, 0, true));
                    }
                }
            }
        });

        boolean ehFarmacia = dadosInstituicao != null
                && TemaNegocioUtil.ehFarmacia(dadosInstituicao.getNegocio());

        // Campos do kit visual: tom suave da cor do negócio e anel de foco.
        Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia));
    }

//    public BuscaProdutoVisao( java.awt.Frame parent, boolean modal, int cod_armazem, int cod_janela, BDConexao conexao )
//    {
//        super( parent, modal );
//        initComponents();
//        setLocationRelativeTo( null );
//        this.cod_armazem = cod_armazem;
//        this.cod_janela = cod_janela;
//        txtDesignacao.requestFocus();
//        stoksController = new StoksController( conexao );
//        produtosImpostoController = new ProdutosImpostoController( conexao );
//        fonte_dados = stoksController.getFonte( cod_armazem, cod_janela );
//
//        tabela_busca.setDefaultRenderer( Object.class, new RenderBusca( cod_armazem, conexao ) );
//        txtDesignacao.requestFocus();
//
//        txtDesignacao.addKeyListener( new TratarEvento() );
//
//        setBackGroundLegenda();
    ////        updatePrecoIvaTabela();
//
//        try
//        {
//            adicionar_tabela();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//        // Adicione no construtor ou método initComponents()
//        tabela_busca.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
//                .put( KeyStroke.getKeyStroke( "F4" ), "acaoF4" );
//
//        tabela_busca.getActionMap().put( "acaoF4", new AbstractAction()
//        {
//            @Override
//            public void actionPerformed( ActionEvent e )
//            {
//                // Simula o duplo clique chamando o mesmo método
//                int linha = tabela_busca.getSelectedRow();
//                if ( linha >= 0 )
//                {
//                    tratarSelecaoTabela( linha );
//                }
//            }
//        } );
//
//        tabela_busca.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
//
//        // Pintar a linha selecionada
//        tabela_busca.setRowSelectionAllowed( true );
//        tabela_busca.setFillsViewportHeight( true );
//
//        // Adicionar Key Binding para a tecla DOWN
//        InputMap im = tabela_busca.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );
//        ActionMap am = tabela_busca.getActionMap();
//
//        im.put( KeyStroke.getKeyStroke( "DOWN" ), "moveDown" );
//        am.put( "moveDown", new AbstractAction()
//        {
//            @Override
//            public void actionPerformed( ActionEvent e )
//            {
//                int selectedRow = tabela_busca.getSelectedRow();
//                int rowCount = tabela_busca.getRowCount();
//
//                if ( selectedRow < rowCount - 1 )
//                {
//                    tabela_busca.setRowSelectionInterval( selectedRow + 1, selectedRow + 1 );
//                    tabela_busca.scrollRectToVisible( tabela_busca.getCellRect( selectedRow + 1, 0, true ) );
//                }
//            }
//        } );
//
//    }
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

        jLabel1 = new javax.swing.JLabel("Pesquisa de Produtos");
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
                .titulo("P", jLabel1.getText(), "Escreva para filtrar a lista e escolha o produto pretendido")
                .campo(txtDesignacao)
                .legenda(jPanel3)
                .tabela(jScrollPane1)
                .total(jLabel2, lbTamnho)
                .fechar(jButton1)
                .atalhos("↑ ↓ navegar     •     Enter, F4 ou duplo clique: seleccionar     •     Esc: fechar")
                .aoSeleccionar(() -> {
                    int linha = tabela_busca.getSelectedRow();
                    if (linha >= 0) {
                        tratarSelecaoTabela(linha);
                    }
                })
                .montar();
    }

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_buscaMouseClicked
        // TODO add your handling code here:

        // Simula o duplo clique chamando o mesmo método
        int linha = tabela_busca.getSelectedRow();
        if (linha >= 0) {
            tratarSelecaoTabela(linha);
        }

//        if ( evt.getClickCount() == 2 )
//        {
//            if ( DVML.JANELA_ENTRADA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                EntradaPratosCompostosVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_ENTRADA_STOCK == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                EntradaProdutoVisao.busca_produto_by_cod_interno_entrada(codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_VENDA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                VendaUsuarioVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_CARDEX == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
    

    ////                CardexVisao.procedimentoBuscaLupa();
//                CardexVisao.procedimentoBusca( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_SAIDA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                SaidaProdutoVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_VENDA_POS == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                VendaPOSVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            
//            else if ( DVML.JANELA_VENDA_OFICINA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                VendaOficinaUsuarioVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            
//            else if ( DVML.JANELA_FICHA_TECNICA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                FichaTecnicaVisao.busca_produto_by_cod_interno( codigo );
//                dispose();
//
//            }
//            
//            else if ( DVML.JANELA_COMPRA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                try
//                {
//
//                    NovaEncomendaVisao.busca_produto_by_cod_interno( codigo );
//
//                }
//                catch ( Exception e )
//                {
//                }
//
//                try
//                {
//                    CompraInformalVisao.busca_produto_by_cod_interno_compra( codigo );
//                }
//                catch ( Exception e )
//                {
//                }
//
//                try
//                {
//                    TranferenciaArmazemVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                }
//                catch ( Exception e )
//                {
//                }
//
//                try
//                {
//                    AcertoStockVisao.accao_codigo_interno_enter_busca_exterior(codigo );
//                }
//                catch ( Exception e )
//                {
//                }
//
//                dispose();
//
//            }
//
//            else if ( DVML.JANELA_ESTORNO == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                EstornoVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_PRODUTO == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                ProdutosVisao.ver_dados( codigo );
//                dispose();
//
//            }
//            else if ( ( Objects.nonNull( NovaGestaoPedidosVisao.rbNao_lugar ) && NovaGestaoPedidosVisao.rbNao_lugar.isSelected() ) 
//                    && DVML.JANELA_RETAURANTE == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                NovaGestaoPedidosVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_VENDA_EXPRESSO == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                VendasPraticasVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_RECOLHA == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                RecolhaPedidosVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//                dispose();
//
//            }
//            else if ( DVML.JANELA_ASSOCIACAO == cod_janela )
//            {
//
//                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
//                int linha = tabela_busca.getSelectedRow();
//                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//                AssociacaoServicoVisao.adcionarItem( codigo );
//                dispose();
//
//            }
//        }
    }//GEN-LAST:event_tabela_buscaMouseClicked

    private void tratarSelecaoTabela(int linha) {
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        Integer codigo = Integer.parseInt(modelo.getValueAt(linha, 0).toString());

        if (DVML.JANELA_ENTRADA == cod_janela) {

            EntradaPratosCompostosVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_ENTRADA_STOCK == cod_janela) {

            EntradaProdutoVisao.busca_produto_by_cod_interno_entrada(codigo);
            dispose();

        } else if (DVML.JANELA_VENDA == cod_janela) {

//            try
//            {
//                VendaUsuarioVisao.accao_codigo_interno_enter_busca_exterior( codigo );
//
//            }
//            catch ( Exception e )
//            {
//                e.printStackTrace();
//            }
            try {
                double qtd = Double.parseDouble(FormVendaResponsivaVisaoTop.txtQuatindade.getText());
                FormVendaResponsivaVisaoTop.accao_codigo_interno_enter_busca_exterior(codigo, qtd);

            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose();

        } else if (DVML.JANELA_CARDEX == cod_janela) {

            CardexVisao.procedimentoBusca(codigo);
            dispose();

        } else if (DVML.JANELA_SAIDA == cod_janela) {

            SaidaProdutoVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_VENDA_POS == cod_janela) {

            VendaPOSVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_VENDA_OFICINA == cod_janela) {

            VendaOficinaUsuarioVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_FICHA_TECNICA == cod_janela) {

            FichaTecnicaVisao.busca_produto_by_cod_interno(codigo);
            dispose();

        } else if (DVML.JANELA_COMPRA == cod_janela) {

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

        } else if (DVML.JANELA_ESTORNO == cod_janela) {

            EstornoVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_PRODUTO == cod_janela) {

            // ProdutosVisao.ver_dados(codigo);  (janela antiga, substituída)
            ProdutosModernoVisao.ver_dados(codigo);
            dispose();

        } else if (DVML.JANELA_RETAURANTE == cod_janela) //        else if ( ( Objects.nonNull( NovaGestaoPedidosVisao.rbNao_lugar ) && NovaGestaoPedidosVisao.rbNao_lugar.isSelected() )
        //                && DVML.JANELA_RETAURANTE == cod_janela )
        {

            NovaGestaoPedidosVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_VENDA_EXPRESSO == cod_janela) {

            VendasPraticasVisao.accao_codigo_interno_enter_busca_exterior(codigo);
            dispose();

        } else if (DVML.JANELA_RECOLHA == cod_janela) {

//                RecolhaPedidosVisao.accao_codigo_interno_enter_busca_exterior( codigo );
            dispose();

        } else if (DVML.JANELA_ASSOCIACAO == cod_janela) {

            AssociacaoServicoVisao.adcionarItem(codigo);
            dispose();

        } else if (DVML.JANELA_REFERENCIAS == cod_janela) {

            ReferenciasVisao.adicionar_dados(codigo);
            dispose();

        } else if (DVML.JANELA_MULTAS == cod_janela) {

            ConfiguracaoMultasVisao.adicionar_dados(codigo);
            dispose();

        }

    }

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
            java.util.logging.Logger.getLogger(BuscaProdutoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscaProdutoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                BuscaProdutoVisao dialog = new BuscaProdutoVisao(new javax.swing.JFrame(), true, 1, 0, BDConexao.getInstancia());
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

//    class TratarEvento implements KeyListener
//    {
//
//        String prefixo = "";
//
//        public void keyPressed( KeyEvent evt )
//        {
//
//            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
//            {
//                char key = evt.getKeyChar();
//                prefixo = txtDesignacao.getText().trim() + key;
//                //adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
//                adicionar( prefixo );
//
//            }
//            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
//            {
//                try
//                {
//
//                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
//                    // adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
//                    adicionar( prefixo );
//
//                }
//                catch ( Exception e )
//                {
//
//                }
//
//            }
//        }
//
//        public void keyReleased( KeyEvent evt )
//        {
//        }
//
//        public void keyTyped( KeyEvent evt )
//        {
//        }
//
//    }
//
//    private void adicionar( String designacao )
//    {
//        int contTotal = 0, contTotalStockVazio = 0, contTotalStockCritco = 0, contTotalStockNormal = 0, contTotalServico = 0;
//        Iterator<BuscaModeloProduto> iterator = fonte_dados.iterator();
//        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
//        modelo.setRowCount( 0 );
//
//        while ( iterator.hasNext() )
//        {
//
//            BuscaModeloProduto next = iterator.next();
//            if ( next.getDesignacao().toLowerCase().contains( designacao.toLowerCase() ) )
//            {
//                contTotal++;
//
//                if ( next.getQtd().equals( "0" ) )
//                {
//                    contTotalStockVazio++;
//                }
//
//                if ( next.getEstadoCritico().equals( "true" ) )
//                {
//                    contTotalStockCritco++;
//                }
//
//                if ( next.getQtd().equals( "-" ) )
//                {
//                    contTotalServico++;
//                }
//
//                else if ( Double.parseDouble( next.getQtd() ) != 0 && next.getEstadoCritico().equals( "false" ) )
//                {
//                    contTotalStockNormal++;
//                }
//
//                modelo.addRow(
//                        new Object[]
//                        {
//                            next.getCodigo(),
//                            next.getDesignacao(),
//                            next.getCategoria(),
//                            next.getQtd(),
//                            next.getPrecoVenda(),
//                            next.getEstadoCritico()
//
//                        }
//                );
//
//            }
//
//        }
//
//        lbTotalStockVazio.setText( String.valueOf( contTotalStockVazio ) );
//
//        lbTotalServico.setText( String.valueOf( contTotalServico ) );
//        lbTotalStockCritico.setText( String.valueOf( contTotalStockCritco ) );
//        lbTotalStockNormal.setText( String.valueOf( contTotalStockNormal ) );
//        lbTamnho.setText( String.valueOf( contTotal ) );
//    }
    class TratarEvento implements KeyListener {

        String prefixo = "";

        @Override
        public void keyPressed(KeyEvent evt) {
            int keyCode = evt.getKeyCode();

            // Teclas de navegação
            if (keyCode == KeyEvent.VK_DOWN) {
                if (tabela_busca.getRowCount() > 0) {
                    int selected = tabela_busca.getSelectedRow();
                    int next = (selected < 0) ? 0 : Math.min(selected + 1, tabela_busca.getRowCount() - 1);
                    tabela_busca.requestFocus();
                    tabela_busca.setRowSelectionInterval(next, next);
                    tabela_busca.scrollRectToVisible(tabela_busca.getCellRect(next, 0, true));
                }
                return; // interrompe processamento normal
            } else if (keyCode == KeyEvent.VK_UP) {
                if (tabela_busca.getRowCount() > 0) {
                    int selected = tabela_busca.getSelectedRow();
                    int prev = (selected > 0) ? selected - 1 : 0;
                    tabela_busca.requestFocus();
                    tabela_busca.setRowSelectionInterval(prev, prev);
                    tabela_busca.scrollRectToVisible(tabela_busca.getCellRect(prev, 0, true));
                }
                return;
            }

            // Teclas de busca
            if (keyCode != KeyEvent.VK_BACK_SPACE && keyCode != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtDesignacao.getText().trim() + key;
                adicionar(prefixo);
            } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                try {
                    prefixo = prefixo.trim().substring(0, Math.max(0, prefixo.length() - 1));
                    adicionar(prefixo);
                } catch (Exception e) {
                    // ignorar
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent evt) {
        }

        @Override
        public void keyTyped(KeyEvent evt) {
        }
    }

    private void adicionar(String designacao) {
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        modelo.setRowCount(0);

        int contTotal = 0, contTotalStockVazio = 0, contTotalStockCritco = 0, contTotalStockNormal = 0, contTotalServico = 0;

        for (BuscaModeloProduto next : fonte_dados) {
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
                } else if (!next.getQtd().equals("0") && next.getEstadoCritico().equals("false")) {
                    contTotalStockNormal++;
                }

                modelo.addRow(new Object[]{
                    next.getCodigo(),
                    next.getDesignacao(),
                    next.getCategoria(),
                    next.getQtd(),
                    next.getPrecoVenda(),
                    next.getEstadoCritico()
                });
            }
        }

        lbTotalStockVazio.setText(String.valueOf(contTotalStockVazio));
        lbTotalServico.setText(String.valueOf(contTotalServico));
        lbTotalStockCritico.setText(String.valueOf(contTotalStockCritco));
        lbTotalStockNormal.setText(String.valueOf(contTotalStockNormal));
        lbTamnho.setText(String.valueOf(contTotal));

        // Selecionar a primeira linha após atualização
        if (tabela_busca.getRowCount() > 0) {
            tabela_busca.setRowSelectionInterval(0, 0);
        }
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
