/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import comercial.controller.ArmazensController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import entity.Compras;
import entity.TbArmazem;
import entity.TbDadosInstituicao;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import java.awt.Component;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class AcertoStockEmMassaVisao extends javax.swing.JFrame
{

    private static PrecosController precosController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static UsuariosController usuariosController;
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
    private List<Object[]> listaFonte;
    private Connection conn = null;

    private final int usuarioId;

    public AcertoStockEmMassaVisao( java.awt.Frame parent, boolean modal, int usuarioId, String usuarioNome, BDConexao conexao )
    {
        {

            initComponents();
            configurarEditorNumerico();
            setLocationRelativeTo( null );
            this.conexao = conexao;
            conn = conexao.getConnection();
            this.usuarioId = usuarioId;
            this.usuarioNome = usuarioNome;
            cmbArmazem.setModel( new DefaultComboBoxModel<>() );
            precosController = new PrecosController( conexao );
            produtosController = new ProdutosController( conexao );
            tipoProdutosController = new TipoProdutosController( conexao );
            stocksController = new StoksController( conexao );
            armazensController = new ArmazensController( conexao );
            usuariosController = new UsuariosController( conexao );

            init();

            cmbArmazem.setRenderer( new DefaultListCellRenderer()
            {
                @Override
                public Component getListCellRendererComponent( JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus )
                {
                    super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                    if ( value instanceof TbArmazem )
                    {
                        TbArmazem armazem = (TbArmazem) value;
                        setText( armazem.getDesignacao() ); // mostra só o nome
                    }
                    return this;
                }
            } );

            carregarArmazens();
        }

        calcular_qtd();

//            carregarArmazens();       // carrega a combobox de armazéns
//    carregarTabelaStock(codArmazemSelecionado); // carrega dados do stock
//        configurarEditorQtdAcerto(); //
//        configurarEditorAcerto();
//        configurarEditorJTable();
        configurarAcertoCellEditor();
//        configurarColunaAcerto();
//        configurarEditorColunaAcertoFluido();
        // Define altura de 30 pixels para todas as linhas
        tabela_acerto.setRowHeight( 30 );

        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
        model.setRowCount( 0 ); // limpa a tabela

        int codArmazem = armazensController.getCodigoPorDesignacao(
                cmbArmazem.getSelectedItem().toString()
        );

        List<Object[]> lista = produtosController.listarStockPorArmazem( BDConexao.getConnection(), codArmazem ); // <-- preencher a lista

        for ( Object[] linha : lista )
        {
            linha[ 6 ] = null; // penúltima coluna (acerto) vazia
            model.addRow( linha );
        }

    }

    private void init()
    {
        cmbArmazem.setModel( new DefaultComboBoxModel<>( armazensController.getVector() ) );

        txtIniciais.getDocument().addDocumentListener( new DocumentListener()
        {

            public void insertUpdate( DocumentEvent e )
            {
                filtrar();
            }

            public void removeUpdate( DocumentEvent e )
            {
                filtrar();
            }

            public void changedUpdate( DocumentEvent e )
            {
                filtrar();
            }

            private void filtrar()
            {
                String texto = txtIniciais.getText().trim();
                int codArmazem = armazensController.getCodigoPorDesignacao( cmbArmazem.getSelectedItem().toString() ); // 👈 aqui
                carregarTabelaStockFiltrado( codArmazem, texto );
            }
        } );

        listaFonte = produtosController.listarStockPorArmazem( conexao.getConnection1(), getIdArmazem() );

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
        tabela_acerto = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox<>();
        txtIniciais = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

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
            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        tabela_acerto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Designação", "Tipo", "P. Compra", "P. Venda", "QTD Existente", "QTD Acerto", "QTD Final"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tabela_acerto.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_acertoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela_acerto);
        if (tabela_acerto.getColumnModel().getColumnCount() > 0)
        {
            tabela_acerto.getColumnModel().getColumn(0).setMaxWidth(35);
            tabela_acerto.getColumnModel().getColumn(1).setMaxWidth(350);
            tabela_acerto.getColumnModel().getColumn(2).setMaxWidth(120);
            tabela_acerto.getColumnModel().getColumn(3).setMaxWidth(120);
            tabela_acerto.getColumnModel().getColumn(4).setMaxWidth(75);
            tabela_acerto.getColumnModel().getColumn(5).setMaxWidth(95);
            tabela_acerto.getColumnModel().getColumn(6).setMaxWidth(70);
            tabela_acerto.getColumnModel().getColumn(7).setMaxWidth(65);
        }

        jLabel1.setText("Armazém:");

        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        txtIniciais.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisActionPerformed(evt);
            }
        });

        jLabel2.setText("Digite as Inicais do Artigo");

        txtCodigoManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualActionPerformed(evt);
            }
        });

        jLabel3.setText("Digite o Código Manual");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtIniciais, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(txtCodigoManual))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIniciais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tabela_acertoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_acertoMouseClicked
    }//GEN-LAST:event_tabela_acertoMouseClicked

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed

        String nomeArmazem = (String) cmbArmazem.getSelectedItem();
        if ( nomeArmazem != null )
        {
            int codArmazem = armazensController.getCodigoPorDesignacao( nomeArmazem ); // 👈 aqui
            if ( codArmazem > 0 )
            {
                carregarTabelaStock( codArmazem );
            }
            else
            {
                JOptionPane.showMessageDialog( this,
                        "Não foi possível encontrar o código do armazém: " + nomeArmazem );
            }
        }

    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void txtIniciaisActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisActionPerformed

    private void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoManualActionPerformed
    {//GEN-HEADEREND:event_txtCodigoManualActionPerformed
    
        carregarTabelaPorCodigoManual(txtCodigoManual.getText().trim(), getIdArmazem());
    }//GEN-LAST:event_txtCodigoManualActionPerformed

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
            java.util.logging.Logger.getLogger( AcertoStockEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( AcertoStockEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( AcertoStockEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( AcertoStockEmMassaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new AcertoStockEmMassaVisao( null, true, 15, "", new BDConexao() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbArmazem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabela_acerto;
    private javax.swing.JTextField txtCodigoManual;
    private javax.swing.JTextField txtIniciais;
    // End of variables declaration//GEN-END:variables

//    private void carregarTabelaStock( int codArmazem )
//    {
//        BDConexao bd = BDConexao.getBDConetion();
//        Connection conn = bd.getConnection(); // já vamos criar este getter
//
//        List<Object[]> lista = produtosController.listarStockPorArmazem( conn, codArmazem );
//
//        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
//        model.setRowCount( 0 );
//
//        for ( Object[] linha : lista )
//        {
//            model.addRow( linha );
//        }
//    }
    private void carregarTabelaStock( int codArmazem )
    {

        // Obter lista do controller
        listaFonte = produtosController.listarStockPorArmazem( conn, codArmazem );

        // Obter modelo da tabela
        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
        model.setRowCount( 0 ); // limpa tabela antes de preencher

        for ( Object[] linha : listaFonte )
        {
            // Deixar a penúltima coluna (acerto) vazia
            if ( linha.length > 6 )
            { // garante que existe a coluna 6
                linha[ 6 ] = null;
            }
            model.addRow( linha );
        }
    }

    private void carregarArmazens()
    {
        Vector<String> lista = armazensController.getVector();

        cmbArmazem.removeAllItems();
        for ( String nome : lista )
        {
            cmbArmazem.addItem( nome );
        }
    }

    private void salvarAcertos()
    {
        BDConexao bd = BDConexao.getBDConetion();
        Connection conn = bd.getConnection();

        // 1. Pega o armazém selecionado
        int codArmazem = armazensController.getCodigoPorDesignacao(
                cmbArmazem.getSelectedItem().toString()
        );

        // 2. Monta a lista de linhas
        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
        List<Object[]> linhas = new ArrayList<>();

        for ( int i = 0; i < model.getRowCount(); i++ )
        {
            Object qtdAcertoObj = model.getValueAt( i, 6 );

            // só adiciona se houver acerto diferente de 0
            if ( qtdAcertoObj != null && !qtdAcertoObj.toString().trim().isEmpty() )
            {
                double qtdAcerto = Double.parseDouble( qtdAcertoObj.toString() );
                if ( qtdAcerto != 0 )
                {
                    linhas.add( new Object[]
                    {
                        model.getValueAt( i, 0 ), // codProduto
                        model.getValueAt( i, 5 ), // qtdAntes
                        model.getValueAt( i, 6 ), // qtdAcerto
                        model.getValueAt( i, 7 )  // qtdDepois
                    } );
                }
            }
        }

        // 3. Só salva se houver linhas válidas
        if ( linhas.isEmpty() )
        {
            JOptionPane.showMessageDialog( this, "⚠️ Nenhum acerto para salvar." );
            return;
        }

        // 4. Chama o controller corretamente
        try
        {
            stocksController.salvarAcertos( conn, codArmazem, linhas, this.usuarioId );
            JOptionPane.showMessageDialog( this, "✅ Acertos salvos com sucesso!" );
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( this, "❌ Erro ao salvar acertos: " + e.getMessage() );
        }
    }

    private void configurarEditorNumerico()
    {
        JTextField txtField = new JTextField();
        ( (AbstractDocument) txtField.getDocument() ).setDocumentFilter( new DocumentFilter()
        {
            @Override
            public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr ) throws BadLocationException
            {
                if ( string.matches( "[0-9\\-\\.]+" ) )
                {
                    super.insertString( fb, offset, string, attr );
                }
            }

            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
            {
                if ( text.matches( "[0-9\\-\\.]+" ) )
                {
                    super.replace( fb, offset, length, text, attrs );
                }
            }
        } );

        DefaultCellEditor editor = new DefaultCellEditor( txtField );

        // Listener para validar valor final após edição
        editor.addCellEditorListener( new CellEditorListener()
        {
            @Override
            public void editingStopped( ChangeEvent e )
            {
                int row = tabela_acerto.getSelectedRow();
                int col = 6; // coluna QtdAcerto
                tabela_acerto.editingCanceled( null ); // força terminar edição antes de mostrar mensagem

                Object value = tabela_acerto.getValueAt( row, col );
                try
                {
                    double acerto = Double.parseDouble( value.toString() );
                    double qtdAntes = Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() );
                    double qtdDepois = qtdAntes + acerto;

                    if ( qtdDepois < 0 )
                    {
                        JOptionPane.showMessageDialog( null, "O stock final não pode ser negativo!" );
                        tabela_acerto.setValueAt( 0, row, col );
                        tabela_acerto.setValueAt( qtdAntes, row, 7 );
                    }
                    else
                    {
                        tabela_acerto.setValueAt( qtdDepois, row, 7 );
                    }
                }
                catch ( NumberFormatException ex )
                {
                    tabela_acerto.setValueAt( 0, row, col );
                    tabela_acerto.setValueAt( Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() ), row, 7 );
                }
            }

            @Override
            public void editingCanceled( ChangeEvent e )
            {
            }
        } );

        tabela_acerto.getColumnModel().getColumn( 6 ).setCellEditor( editor );
    }

    private void configurarEditorQtdAcerto()
    {
        JTextField txtField = new JTextField();
        ( (AbstractDocument) txtField.getDocument() ).setDocumentFilter( new DocumentFilter()
        {
            @Override
            public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr ) throws BadLocationException
            {
                if ( string.matches( "[0-9\\-\\.]+" ) )
                {
                    super.insertString( fb, offset, string, attr );
                }
            }

            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
            {
                if ( text.matches( "[0-9\\-\\.]+" ) )
                {
                    super.replace( fb, offset, length, text, attrs );
                }
            }
        } );

        DefaultCellEditor editor = new DefaultCellEditor( txtField );

        editor.addCellEditorListener( new CellEditorListener()
        {
            @Override
            public void editingStopped( ChangeEvent e )
            {
                int row = tabela_acerto.getSelectedRow();
                int col = 6; // QtdAcerto

                // força terminar a edição antes de mostrar mensagens
                if ( tabela_acerto.isEditing() )
                {
                    tabela_acerto.getCellEditor().stopCellEditing();
                }

                Object value = tabela_acerto.getValueAt( row, col );
                try
                {
                    double acerto = Double.parseDouble( value.toString() );
                    double qtdAntes = Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() );
                    double qtdDepois = qtdAntes + acerto;

                    if ( qtdDepois < 0 )
                    {
                        JOptionPane.showMessageDialog( null, "O stock final não pode ser negativo!" );
                        tabela_acerto.setValueAt( 0, row, col );
                        tabela_acerto.setValueAt( qtdAntes, row, 7 );
                    }
                    else
                    {
                        tabela_acerto.setValueAt( qtdDepois, row, 7 );
                    }

                    // move o foco para a próxima linha na mesma coluna
                    int nextRow = row + 1;
                    if ( nextRow < tabela_acerto.getRowCount() )
                    {
                        tabela_acerto.changeSelection( nextRow, col, false, false );
                        tabela_acerto.editCellAt( nextRow, col );
                        Component comp = tabela_acerto.getEditorComponent();
                        if ( comp != null )
                        {
                            comp.requestFocusInWindow();
                        }
                    }

                }
                catch ( NumberFormatException ex )
                {
                    tabela_acerto.setValueAt( 0, row, col );
                    tabela_acerto.setValueAt( Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() ), row, 7 );
                }
            }

            @Override
            public void editingCanceled( ChangeEvent e )
            {
            }
        } );

        tabela_acerto.getColumnModel().getColumn( 6 ).setCellEditor( editor );
    }

    private void configurarAcertoCellEditor()
    {
        int colAcerto = 6; // coluna da quantidade a acertar

        TableColumn column = tabela_acerto.getColumnModel().getColumn( colAcerto );
        JTextField textField = new JTextField();
        column.setCellEditor( new DefaultCellEditor( textField ) );

        textField.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyPressed( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    int row = tabela_acerto.getEditingRow();
                    if ( row == -1 )
                    {
                        return;
                    }

                    SwingUtilities.invokeLater( () ->
                    {
                        try
                        {
                            String valorStr = textField.getText().trim();
                            if ( valorStr.isEmpty() )
                            {
                                return;
                            }

                            double acerto = Double.parseDouble( valorStr );
                            double qtdAntes = Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() );
                            double qtdDepois = qtdAntes + acerto;

                            if ( qtdDepois < 0 )
                            {
                                JOptionPane.showMessageDialog( null, "O stock final não pode ser negativo!" );
                                tabela_acerto.setValueAt( 0, row, colAcerto );
                                tabela_acerto.setValueAt( qtdAntes, row, 7 );
                            }
                            else
                            {
                                tabela_acerto.setValueAt( acerto, row, colAcerto );
                                tabela_acerto.setValueAt( qtdDepois, row, 7 );

                                // salvar no banco
                                int codProduto = Integer.parseInt( tabela_acerto.getValueAt( row, 0 ).toString() );
                                int codArmazem = armazensController.getCodigoPorDesignacao(
                                        cmbArmazem.getSelectedItem().toString()
                                );
                                stocksController.salvarAcertoLinha(
                                        BDConexao.getBDConetion().getConnection(),
                                        codProduto, codArmazem, usuarioId, usuarioNome,
                                        tabela_acerto.getValueAt( row, 1 ).toString(),
                                        cmbArmazem.getSelectedItem().toString(),
                                        qtdAntes, acerto, qtdDepois
                                );
                                listaFonte = produtosController.listarStockPorArmazem( conexao.getConnection1(), getIdArmazem() );
                            }

                            // mover foco para próxima linha
                            int nextRow = row + 1;
                            if ( nextRow < tabela_acerto.getRowCount() )
                            {
                                tabela_acerto.changeSelection( nextRow, colAcerto, false, false );
                                tabela_acerto.editCellAt( nextRow, colAcerto );
                                Component comp = tabela_acerto.getEditorComponent();
                                if ( comp != null )
                                {
                                    comp.requestFocusInWindow();
                                }
                            }

                        }
                        catch ( NumberFormatException | SQLException ex )
                        {
                            JOptionPane.showMessageDialog( null, "Erro ao processar acerto: " + ex.getMessage() );
                            tabela_acerto.setValueAt( 0, row, colAcerto );
                            tabela_acerto.setValueAt( Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() ), row, 7 );
                        }
                    } );
                }
            }
        } );
    }

    private void calcular_qtd()
    {

        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();

        model.addTableModelListener( e ->
        {
            if ( e.getType() == TableModelEvent.UPDATE )
            {
                int row = e.getFirstRow();
                int col = e.getColumn();

                if ( col == 6 )
                {
                    Object qtdAntesObj = model.getValueAt( row, 5 );
                    Object qtdAcertoObj = model.getValueAt( row, 6 );

                    if ( qtdAntesObj != null && qtdAcertoObj != null )
                    {
                        try
                        {
                            double qtdAntes = Double.parseDouble( qtdAntesObj.toString() );
                            double qtdAcerto = Double.parseDouble( qtdAcertoObj.toString() );
                            double qtdDepois = qtdAntes + qtdAcerto;

                            if ( qtdDepois < 0 )
                            {
                                JOptionPane.showMessageDialog( this,
                                        "❌ Não é permitido stock negativo!" );
                                model.setValueAt( "", row, 6 );
                                model.setValueAt( qtdAntes, row, 7 );
                            }
                            else
                            {
                                model.setValueAt( qtdDepois, row, 7 );
                            }
                        }
                        catch ( NumberFormatException ex )
                        {
//                            JOptionPane.showMessageDialog( this,
//                                    "Digite apenas números." );
//                            model.setValueAt( "", row, 6 );
                        }
                    }
                }
            }
        } );

    }

    private void configurarEditorAcerto()
    {
        // Pega o editor da coluna de acertos (coluna 6)
        TableColumn acertoColumn = tabela_acerto.getColumnModel().getColumn( 6 );
        DefaultCellEditor editor = (DefaultCellEditor) acertoColumn.getCellEditor();

        if ( editor == null )
        {
            // se não tiver editor definido, criar um
            JTextField textField = new JTextField();
            editor = new DefaultCellEditor( textField );
            acertoColumn.setCellEditor( editor );
        }

        editor.addCellEditorListener( new CellEditorListener()
        {
            @Override
            public void editingStopped( ChangeEvent e )
            {
                int row = tabela_acerto.getSelectedRow();
                int col = 6; // QtdAcerto

                if ( tabela_acerto.isEditing() )
                {
                    tabela_acerto.getCellEditor().stopCellEditing();
                }

                Object value = tabela_acerto.getValueAt( row, col );
                try
                {
                    double acerto = Double.parseDouble( value.toString() );
                    double qtdAntes = Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() );
                    double qtdDepois = qtdAntes + acerto;

                    if ( qtdDepois < 0 )
                    {
                        JOptionPane.showMessageDialog( null, "O stock final não pode ser negativo!" );
                        tabela_acerto.setValueAt( 0, row, col );
                        tabela_acerto.setValueAt( qtdAntes, row, 7 );
                        return;
                    }
                    else
                    {
                        tabela_acerto.setValueAt( qtdDepois, row, 7 );
                    }

                    // --- SALVA NA BASE DE DADOS ---
                    int codProduto = Integer.parseInt( tabela_acerto.getValueAt( row, 0 ).toString() );
                    int codArmazem = armazensController.getCodigoPorDesignacao(
                            cmbArmazem.getSelectedItem().toString()
                    );

                    // usuarioId e usuarioNome devem ser atributos do formulário
                    BDConexao bd = BDConexao.getBDConetion();
                    Connection conn = bd.getConnection();

                    stocksController.salvarAcertoLinha( conn, codProduto, codArmazem,
                            usuarioId, usuarioNome,
                            tabela_acerto.getValueAt( row, 1 ).toString(),
                            cmbArmazem.getSelectedItem().toString(),
                            qtdAntes, acerto, qtdDepois
                    );

                    // mover foco para próxima linha
                    int nextRow = row + 1;
                    if ( nextRow < tabela_acerto.getRowCount() )
                    {
                        tabela_acerto.changeSelection( nextRow, col, false, false );
                        tabela_acerto.editCellAt( nextRow, col );
                        Component comp = tabela_acerto.getEditorComponent();
                        if ( comp != null )
                        {
                            comp.requestFocusInWindow();
                        }
                    }

                }
                catch ( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao processar acerto: " + ex.getMessage() );
                    tabela_acerto.setValueAt( 0, row, col );
                    tabela_acerto.setValueAt( Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() ), row, 7 );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( AcertoStockEmMassaVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }

            @Override
            public void editingCanceled( ChangeEvent e )
            {
            }
        } );
    }

    private void configurarEditorJTable()
    {
        // Obtém o editor da coluna de acerto (coluna 6)
        TableColumn acertoColumn = tabela_acerto.getColumnModel().getColumn( 6 );
        JTextField editor = new JTextField();

        // Aceita apenas números positivos/negativos e ponto
        ( (AbstractDocument) editor.getDocument() ).setDocumentFilter( new DocumentFilter()
        {
            @Override
            public void insertString( FilterBypass fb, int offset, String string, AttributeSet attr ) throws BadLocationException
            {
                if ( string.matches( "[0-9\\-\\.]+" ) )
                {
                    super.insertString( fb, offset, string, attr );
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException
            {
                if ( text.matches( "[0-9\\-\\.]+" ) )
                {
                    super.replace( fb, offset, length, text, attrs );
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } );

        acertoColumn.setCellEditor( new DefaultCellEditor( editor ) );

        acertoColumn.getCellEditor().addCellEditorListener( new CellEditorListener()
        {
            @Override
            public void editingStopped( ChangeEvent e )
            {
                int row = tabela_acerto.getSelectedRow();
                int col = 6; // coluna de acerto

                Object value = tabela_acerto.getValueAt( row, col );
                try
                {
                    double acerto = Double.parseDouble( value.toString() );
                    double qtdAntes = Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() );
                    double qtdDepois = qtdAntes + acerto;

                    if ( qtdDepois < 0 )
                    {
                        JOptionPane.showMessageDialog( null, "O stock final não pode ser negativo!" );
                        tabela_acerto.setValueAt( 0, row, col );
                        tabela_acerto.setValueAt( qtdAntes, row, 7 );
                        return;
                    }
                    else
                    {
                        tabela_acerto.setValueAt( qtdDepois, row, 7 );
                    }

                    // --- SALVA NA BASE DE DADOS ---
                    int codProduto = Integer.parseInt( tabela_acerto.getValueAt( row, 0 ).toString() );
                    int codArmazem = armazensController.getCodigoPorDesignacao( cmbArmazem.getSelectedItem().toString() );
                    String designacaoProduto = tabela_acerto.getValueAt( row, 1 ).toString();
                    String designacaoArmazem = cmbArmazem.getSelectedItem().toString();

                    BDConexao bd = BDConexao.getBDConetion();
                    try ( Connection conn = bd.getConnection() )
                    {
                        stocksController.salvarAcertoLinha( conn,
                                codProduto,
                                codArmazem,
                                usuarioId, // passado no construtor do formulário
                                usuarioNome, // passado no construtor do formulário
                                designacaoProduto,
                                designacaoArmazem,
                                qtdAntes,
                                acerto,
                                qtdDepois
                        );
                    }

                    // Move foco para próxima linha
                    int nextRow = row + 1;
                    if ( nextRow < tabela_acerto.getRowCount() )
                    {
                        tabela_acerto.changeSelection( nextRow, col, false, false );
                        tabela_acerto.editCellAt( nextRow, col );
                        Component comp = tabela_acerto.getEditorComponent();
                        if ( comp != null )
                        {
                            comp.requestFocusInWindow();
                        }
                    }

                }
                catch ( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog( null, "Digite apenas números!" );
                    tabela_acerto.setValueAt( 0, row, col );
                    tabela_acerto.setValueAt( Double.parseDouble( tabela_acerto.getValueAt( row, 5 ).toString() ), row, 7 );
                }
                catch ( SQLException ex )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao salvar acerto: " + ex.getMessage() );
                }
            }

            @Override
            public void editingCanceled( ChangeEvent e )
            {
            }
        } );
    }

    private void configurarColunaAcerto()
    {
        TableColumn column = tabela_acerto.getColumnModel().getColumn( 6 ); // coluna QtdAcerto
        JTextField textField = new JTextField();

        column.setCellEditor( new DefaultCellEditor( textField )
        {
            @Override
            public boolean stopCellEditing()
            {
                String value = (String) getCellEditorValue();
                try
                {
                    if ( value != null && !value.trim().isEmpty() )
                    {
                        Double.parseDouble( value ); // tenta converter
                    }
                    return super.stopCellEditing();
                }
                catch ( NumberFormatException e )
                {
                    // valor inválido, resetar e manter foco
                    SwingUtilities.invokeLater( () ->
                    {
                        tabela_acerto.setValueAt( 0, tabela_acerto.getSelectedRow(), 6 );
                        tabela_acerto.editCellAt( tabela_acerto.getSelectedRow(), 6 );
                        Component comp = tabela_acerto.getEditorComponent();
                        if ( comp != null )
                        {
                            comp.requestFocusInWindow();
                        }
                    } );
                    return false; // não permite sair da edição
                }
            }
        } );
    }

    private void configurarEditorColunaAcertoFluido()
    {
        int colAcerto = 6; // coluna de QtdAcerto
        TableColumn column = tabela_acerto.getColumnModel().getColumn( colAcerto );
        JTextField tf = new JTextField();

        // Permitir apenas números, +, -, e .
        tf.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyTyped( KeyEvent e )
            {
                char c = e.getKeyChar();
                if ( !Character.isDigit( c ) && c != KeyEvent.VK_BACK_SPACE
                        && c != KeyEvent.VK_DELETE && c != '-' && c != '.' )
                {
                    e.consume(); // ignora tecla inválida
                }
            }
        } );

        column.setCellEditor( new DefaultCellEditor( tf )
        {
            @Override
            public boolean stopCellEditing()
            {
                int row = tabela_acerto.getSelectedRow();
                String val = (String) getCellEditorValue();
                double acerto = 0;

                try
                {
                    if ( val != null && !val.trim().isEmpty() )
                    {
                        acerto = Double.parseDouble( val );
                    }
                }
                catch ( NumberFormatException ex )
                {
                    acerto = 0; // reseta valor inválido
                }

                double qtdAntes = 0;
                Object objQtdAntes = tabela_acerto.getValueAt( row, 5 );
                if ( objQtdAntes != null )
                {
                    qtdAntes = Double.parseDouble( objQtdAntes.toString() );
                }

                double qtdDepois = qtdAntes + acerto;

                // Impede stock negativo
                if ( qtdDepois < 0 )
                {
                    acerto = 0;
                    qtdDepois = qtdAntes;
                }

                // Atualiza a tabela
                tabela_acerto.setValueAt( acerto, row, colAcerto );
                tabela_acerto.setValueAt( qtdDepois, row, 7 );

                // Salvar automaticamente
                try
                {
                    int codProduto = Integer.parseInt( tabela_acerto.getValueAt( row, 0 ).toString() );
                    int codArmazem = armazensController.getCodigoPorDesignacao(
                            cmbArmazem.getSelectedItem().toString() );
                    stocksController.salvarAcertoLinha( conexao.getConnection1(), codProduto, codArmazem,
                            usuarioId, usuarioNome,
                            tabela_acerto.getValueAt( row, 1 ).toString(),
                            cmbArmazem.getSelectedItem().toString(),
                            qtdAntes, acerto, qtdDepois );
                }
                catch ( SQLException e )
                {
                    e.printStackTrace();
                }

                // Move o foco para a próxima linha na mesma coluna
                int nextRow = row + 1;
                if ( nextRow < tabela_acerto.getRowCount() )
                {
                    tabela_acerto.changeSelection( nextRow, colAcerto, false, false );
                    tabela_acerto.editCellAt( nextRow, colAcerto );
                    Component comp = tabela_acerto.getEditorComponent();
                    if ( comp != null )
                    {
                        comp.requestFocusInWindow();
                    }
                }

                return super.stopCellEditing();
            }
        } );
    }

    // Método para carregar com filtro (por ocorrência)
    private void carregarTabelaStockFiltrado( int codArmazem, String filtro )
    {
        DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
        model.setRowCount( 0 ); // limpa antes de preencher

        for ( Object[] linha : listaFonte )
        {
            String designacao = linha[ 1 ].toString(); // coluna 1 = designação

            if ( filtro == null || filtro.isEmpty() || designacao.toLowerCase().contains( filtro.toLowerCase() ) )
            {
                // Penúltima coluna (acerto) vazia
                if ( linha.length > 6 )
                {
                    linha[ 6 ] = null;
                }
                model.addRow( linha );
            }
        }
    }
    
    private void carregarTabelaPorCodigoManual(String codigoManual, int codArmazem) {
    if (codigoManual.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Digite o código manual do produto!",
                                      "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obter lista do controller
    List<Object[]> lista = produtosController.listarStockPorCodigoManual(conn, codigoManual, codArmazem);

    DefaultTableModel model = (DefaultTableModel) tabela_acerto.getModel();
    model.setRowCount(0); // limpa tabela antes de preencher

    if (lista.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nenhum produto encontrado com o código manual: " + codigoManual,
                                      "Aviso", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    for (Object[] linha : lista) {
        // Penúltima coluna (acerto) começa vazia
        if (linha.length > 6) {
            linha[6] = null;
        }
        model.addRow(linha);
    }
}


    private int getIdArmazem()
    {
        String nomeArmazem = (String) cmbArmazem.getSelectedItem();
        return armazensController.getCodigoPorDesignacao( nomeArmazem ); // 👈 aqui
    }

}
