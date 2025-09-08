/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.ArmazensController;
import comercial.controller.ImpostosController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.ProdutosImpostoController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import entity.Compras;
import entity.Imposto;
import entity.ProdutoImposto;
import entity.TbArmazem;
import entity.TbDadosInstituicao;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.FinanceUtils;
import util.PrecosUtil;
import util.TextFieldUtils;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DefinirPrecoEmMassaVisao extends javax.swing.JFrame
{

    final int COL_ID_PRODUTO = 0;
    final int COL_PRECO_COMPRA = 3;
    final int COL_PRECO_MEDIO = 4;
    final int COL_PRECO_VENDA = 5;
    final int COL_IVA = 6;
    final int COL_PRECO_COM_IVA = 7;

    private static PrecosController precosController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutosController;
    private static StoksController stoksController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static UsuariosController usuariosController;
    private static ImpostosController impostosController;
    private static ProdutosImpostoController produtosImpostoController;
    private static TbArmazem armazem;
    private static TbStock stock_local;
    private static Compras compra;
    private static TbPreco preco;
    private static TbProduto produto;
    private static TbUsuario usuario;
    public static TbDadosInstituicao dadosInstituicao;
    private static BDConexao conexao;
    private static BDConexao conexaoTransaction;
    private static TbStock stock;
    private TbTipoProduto tipoProduto;
    private int codigo = 0;
    private String usuarioNome;
    private List<Object[]> lista;

    private final int usuarioId;

    public DefinirPrecoEmMassaVisao( java.awt.Frame parent, boolean modal, int usuarioId, String usuarioNome )
    {

        initComponents();
        setLocationRelativeTo( null );
        conexao = new BDConexao();
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        cmbCategoria.setModel( new DefaultComboBoxModel<>() );
        precosController = new PrecosController( conexao );
        produtosController = new ProdutosController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        usuariosController = new UsuariosController( conexao );
        impostosController = new ImpostosController( conexao );
        produtosImpostoController = new ProdutosImpostoController( conexao );
        stocksController = new StoksController( conexao );

        init();

//        cmbCategoria.setRenderer( new DefaultListCellRenderer()
//        {
//            @Override
//            public Component getListCellRendererComponent( JList<?> list, Object value, int index,
//                    boolean isSelected, boolean cellHasFocus )
//            {
//                super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
//                if ( value instanceof TbArmazem )
//                {
//                    TbArmazem armazem = (TbArmazem) value;
//                    setText( armazem.getDesignacao() ); // mostra só o nome
//                }
//                return this;
//            }
//        } );
        carregarTipoProduto();
        tabela.setRowHeight( 30 );

        carregarProdutos();

        initTable();

    }

    private void init()
    {
        cmbCategoria.setModel( new DefaultComboBoxModel<>( tipoProdutosController.getVector() ) );
        cmbImposto.setModel( new DefaultComboBoxModel( impostosController.getVector() ) );

        btnAplicar.addActionListener( e ->
        {
            // Desativa o botão imediatamente
            btnAplicar.setEnabled( false );

            // Executa em outra thread para não travar a interface
            new Thread( () ->
            {
                try
                {
                    procedimentoAplicar(); // aqui roda sua lógica pesada
                }
                finally
                {
                    // Reativa o botão no EDT (Event Dispatch Thread)
                    SwingUtilities.invokeLater( () -> btnAplicar.setEnabled( true ) );
                }
            } ).start();
        } );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        ivaTaxaJLabel = new javax.swing.JLabel();
        cmbImposto = new javax.swing.JComboBox<>();
        btnAplicar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::ACERTO STOCK::::...");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - CINZA/Logout-32x32.png"))); // NOI18N
        btnCancelar.setText("Sair");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Designação", "Tipo", "P. Compra", "P. Medio", "P. Venda", "IVA", "Preco + IVA"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tabela.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela);
        if (tabela.getColumnModel().getColumnCount() > 0)
        {
            tabela.getColumnModel().getColumn(0).setMaxWidth(35);
            tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
            tabela.getColumnModel().getColumn(3).setMaxWidth(75);
            tabela.getColumnModel().getColumn(5).setPreferredWidth(75);
            tabela.getColumnModel().getColumn(6).setPreferredWidth(50);
            tabela.getColumnModel().getColumn(7).setPreferredWidth(75);
        }

        jLabel1.setText("Categoria:");

        cmbCategoria.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCategoriaActionPerformed(evt);
            }
        });

        ivaTaxaJLabel.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 10)); // NOI18N
        ivaTaxaJLabel.setForeground(new java.awt.Color(255, 0, 0));
        ivaTaxaJLabel.setText("Taxa IVA ( % )");

        cmbImposto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cmbImposto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbImposto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbImpostoActionPerformed(evt);
            }
        });

        btnAplicar.setText("Aplicar");
        btnAplicar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAplicarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(ivaTaxaJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbImposto, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnAplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ivaTaxaJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbImposto)
                                    .addComponent(btnAplicar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
    }//GEN-LAST:event_tabelaMouseClicked

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCategoriaActionPerformed
    {//GEN-HEADEREND:event_cmbCategoriaActionPerformed

        try
        {

            if ( cmbCategoria.getSelectedIndex() == 0 )
            {
                carregarProdutos();
            }
            else
            {
                System.err.println( "INIT" );
                List<Object[]> listaPorCategoria = produtosController.listarProdutosByCategoria( BDConexao.conectar(), cmbCategoria.getSelectedItem().toString() );
                System.err.println( "LISTA SIZE : " + listaPorCategoria.size() );
                carregarProdutos( listaPorCategoria );
            }

        }
        catch ( Exception e )
        {
        }

    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void cmbImpostoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbImpostoActionPerformed
    {//GEN-HEADEREND:event_cmbImpostoActionPerformed
        // TODO add your handling code here:
        prepararUpdateIva();

    }//GEN-LAST:event_cmbImpostoActionPerformed

    private void btnAplicarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAplicarActionPerformed
    {//GEN-HEADEREND:event_btnAplicarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAplicarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
            {
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( DefinirPrecoEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( DefinirPrecoEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( DefinirPrecoEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( DefinirPrecoEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new DefinirPrecoEmMassaVisao( null, true, 15, "" ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAplicar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbCategoria;
    private static javax.swing.JComboBox<String> cmbImposto;
    private static javax.swing.JLabel ivaTaxaJLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabela;
    // End of variables declaration//GEN-END:variables

    private void carregarTipoProduto()
    {
        Vector<String> lista = tipoProdutosController.getVector();
        cmbCategoria.removeAllItems();
        lista.add( 0, "--TODOS--" );
        for ( String nome : lista )
        {
            cmbCategoria.addItem( nome );
        }
    }

    private void carregarProdutos()
    {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount( 0 ); // limpa a tabela

        List<Object[]> lista = produtosController.listarProdutos( BDConexao.getConnection() ); // <-- preencher a lista
        for ( Object[] linha : lista )
        {
            model.addRow( linha );
        }
    }

    private void carregarProdutos( List<Object[]> listaParm )
    {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount( 0 ); // limpa a tabela

        for ( Object[] linha : listaParm )
        {
            model.addRow( linha );
        }
    }

    private void prepararUpdateIva()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            actualizar( modelo, i );

        }
    }

    private void actualizar( DefaultTableModel modelo, int linha )
    {
        double taxa = Double.parseDouble( cmbImposto.getSelectedItem().toString() );
        double qtd = 1;
        double precoVenda = Double.parseDouble( modelo.getValueAt( linha, COL_PRECO_VENDA ).toString() );
        double desconto = 0;
        double valorComIVA = FinanceUtils.getValorComIVA( qtd, taxa, precoVenda, desconto );
        modelo.setValueAt( taxa, linha, COL_IVA );
        modelo.setValueAt( valorComIVA, linha, COL_PRECO_COM_IVA );
    }

    private void actualizarENTER( DefaultTableModel modelo, int linha, int colunaIva, int colunaPrecoComIva )
    {
        double taxa = parseDouble( modelo.getValueAt( linha, colunaIva ) );
        double qtd = 1.0;
        double precoSemIva = parseDouble( modelo.getValueAt( linha, COL_PRECO_VENDA ) ); // COL_PRECO_VENDA
        double desconto = 0.0;

        double valorComIVA = FinanceUtils.getValorComIVA( qtd, taxa, precoSemIva, desconto );

        // NÃO redefina o IVA aqui — ele já foi digitado na célula e gravado no modelo
        modelo.setValueAt( valorComIVA, linha, colunaPrecoComIva );
    }

// Helper para número com vírgula/ponto
    private double parseDouble( Object o )
    {
        if ( o == null )
        {
            return 0.0;
        }
        String s = o.toString().trim().replace( ",", "." );
        try
        {
            return Double.parseDouble( s );
        }
        catch ( Exception e )
        {
            return 0.0;
        }
    }

    private void initTable()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();

        // Editor customizado para IVA
        JTextField editorIvaField = new JTextField();
        DefaultCellEditor editorIva = new DefaultCellEditor( editorIvaField );
        editorIva.setClickCountToStart( 1 );
        tabela.getColumnModel().getColumn( COL_IVA ).setCellEditor( editorIva );
        tabela.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );

        // Salto de linha no IVA
        editorIva.addCellEditorListener( new javax.swing.event.CellEditorListener()
        {
            @Override
            public void editingStopped( javax.swing.event.ChangeEvent e )
            {
                int linha = tabela.getSelectedRow();
                if ( linha >= 0 )
                {
                    int proximaLinha = linha + 1;
                    if ( proximaLinha < tabela.getRowCount() )
                    {
                        tabela.changeSelection( proximaLinha, COL_IVA, false, false );
                        tabela.editCellAt( proximaLinha, COL_IVA );
                        Component editor = tabela.getEditorComponent();
                        if ( editor != null )
                        {
                            editor.requestFocus();
                        }
                    }
                }
            }

            @Override
            public void editingCanceled( javax.swing.event.ChangeEvent e )
            {
            }
        } );

        // TableModelListener
        modelo.addTableModelListener( ev ->
        {
            if ( ev.getType() != javax.swing.event.TableModelEvent.UPDATE )
            {
                return;
            }

            int linha = ev.getFirstRow();
            int coluna = ev.getColumn();
            if ( linha < 0 )
            {
                return;
            }

            int idProduto = (int) modelo.getValueAt( linha, COL_ID_PRODUTO );
            BigDecimal precoCompra = new BigDecimal( modelo.getValueAt( linha, COL_PRECO_COMPRA ).toString() );

            // Atualiza Preço Médio se preco_compra mudou
            if ( coluna == COL_PRECO_COMPRA )
            {
                BigDecimal precoMedio = PrecosUtil.calculaPrecoMedio( idProduto, precoCompra, 0 );

                modelo.setValueAt( precoMedio, linha, COL_PRECO_MEDIO );
            }

            // Atualiza Preço + IVA se preco_compra, preco_venda ou IVA mudou
            if ( coluna == COL_PRECO_COMPRA || coluna == COL_PRECO_VENDA || coluna == COL_IVA )
            {
                actualizarENTER( modelo, linha, COL_IVA, COL_PRECO_COM_IVA );
            }
        } );

        TextFieldUtils.configurarColunaDecimal( tabela, COL_PRECO_COMPRA, 6 );
        TextFieldUtils.configurarColunaDecimal( tabela, COL_PRECO_VENDA, 6 );

    }

    private void aplicarIvaMassa( int idProduto, String iva )
    {
        int idImposto = impostosController.getImpostoByDesignacao( iva ).getPkImposto();
        ProdutoImposto produtoImposto = new ProdutoImposto();
        produtoImposto.setFkProduto( new TbProduto( idProduto ) );
        produtoImposto.setFkImposto( new Imposto( idImposto ) );
        if ( produtosImpostoController.existeProdutoImposto( idProduto ) )
        {
            if ( !produtosImpostoController.actualizar( produtoImposto ) )
            {
                JOptionPane.showMessageDialog( null, "Falha ao actualizar o IVA no produto", "Falha", JOptionPane.WARNING_MESSAGE );
                return;
            }
        }
        else if ( !produtosImpostoController.salvar( produtoImposto ) )
        {
            JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
            return;
        }
    }

    private void procedimentoAplicar()
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            int idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            String taxa = modelo.getValueAt( i, COL_IVA ).toString();
            BigDecimal precoCompra = new BigDecimal( modelo.getValueAt( i, COL_PRECO_COMPRA ).toString() );
            BigDecimal precoVenda = new BigDecimal( modelo.getValueAt( i, COL_PRECO_VENDA ).toString() );
            if ( Double.parseDouble( taxa ) > 0 )
            {
                aplicarIvaMassa( idProduto, taxa );
                PrecosUtil.actualizarPreco(
                        usuarioId,
                        idProduto,
                        precoCompra,
                        precoVenda,
                        precosController, conexao );
            }
        }
        JOptionPane.showMessageDialog( null, "IVA aplicado com sucesso!..." );
    }

// Método usando Java 8+ streams
    public List<Object[]> getListaPorCategoria( String categoria )
    {
        if ( categoria == null || categoria.isEmpty() )
        {
            return new ArrayList<>(); // retorna lista vazia se categoria inválida
        }

        final int COL_CATEGORIA = 2; // índice da coluna categoria

        return lista.stream()
                .filter( arr -> arr[ COL_CATEGORIA ] != null
                && categoria.equals( arr[ COL_CATEGORIA ].toString() ) )
                .collect( Collectors.toList() );
    }

// Alternativa usando loop tradicional
    public List<Object[]> getListaPorCategoriaLoop( String categoria )
    {
        List<Object[]> resultado = new ArrayList<>();
        final int COL_CATEGORIA = 2;

        if ( categoria == null || categoria.isEmpty() )
        {
            return resultado;
        }

        for ( Object[] item : lista )
        {
            if ( item[ COL_CATEGORIA ] != null && categoria.equals( item[ COL_CATEGORIA ].toString() ) )
            {
                resultado.add( item );
            }
        }
        return resultado;
    }

}
