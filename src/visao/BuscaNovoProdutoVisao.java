/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import visao.design.*;
import java.sql.Connection;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.StockDao;
import entity.TbProduto;
import entity.TbStock;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BuscaNovoProdutoVisao extends javax.swing.JDialog
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private StockDao stockDao = new StockDao( emf );
    private PrecoDao precoDao = new PrecoDao( emf );
    private ProdutoDao produtoDao = new ProdutoDao( emf );
    private Vector<TbProduto> fonte_dados = null;

    private int cod_janela = 0;

    public BuscaNovoProdutoVisao( java.awt.Frame parent, boolean modal, int cod_janela )
    {
        super( parent, modal );
        initComponents();
        setLocationRelativeTo( null );

        this.cod_janela = cod_janela;
        txtDesignacao.requestFocus();

//        fonte_dados = (Vector<TbStock>) stockDao.getProdutosPorIdArmazem( this.cod_armazem );
        fonte_dados = ( Vector<TbProduto> ) produtoDao.getProdutosExistentes();

        txtDesignacao.requestFocus();
        try
        {
            adicionar_tabela();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        txtDesignacao.addKeyListener( new TratarEvento() );

    }

    /**
     * Interface construída em código com o kit {@code visao.design} (sem
     * .form), no layout padrão dos formulários de pesquisa.
     */
    private void initComponents() {
        setUndecorated(true);

        txtDesignacao = new CampoPesquisa("Pesquisar produto pela designação...");
        txtDesignacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDesignacaoActionPerformed(evt);
            }
        });

        tabela_busca = new TabelaModerna(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Cód.", "Designação", "Categoria"
                }
        ) {
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
        colunas.getColumn(1).setPreferredWidth(460);
        colunas.getColumn(2).setPreferredWidth(220);
        jScrollPane1 = new RolagemModerna(tabela_busca);

        jLabel1 = new javax.swing.JLabel("Pesquisa de Produtos");
        jButton1 = new BotaoFechar();
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1 = new javax.swing.JPanel();

        LayoutPesquisa.para(this)
                .titulo("P", jLabel1.getText(), "Escreva para filtrar e faça duplo clique no produto pretendido")
                .campo(txtDesignacao)
                .tabela(jScrollPane1)
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
                .tamanho(980, 680)
                .montar();
    }

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_buscaMouseClicked
        // TODO add your handling code here:

        if ( evt.getClickCount() == 2 )
        {

            if ( DVML.JANELA_ENTRADA == cod_janela )
            {

                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
                EntradaProdutoVisao.accao_key_pressed_codigo_produto( codigo );
                dispose();

            }
            else if ( DVML.JANELA_VENDA == cod_janela )
            {

                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
                VendaUsuarioVisao.accao_codigo_interno_enter_busca_exterior( codigo );
                dispose();

            }
            else if ( DVML.JANELA_PRODUTO == cod_janela )
            {

                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
                
                TbProduto produto = produtoDao.findTbProduto( codigo);

                // ProdutosVisao.ver_dados( codigo );
                visao.produto.ProdutosModernoVisao.ver_dados( codigo );
//                
//                ProdutosVisao.mo( produto );
//                ProdutosVisao.mostrar_cod_dados_stock_armazem2ci( produto );
//                ProdutosVisao.mostrar_cod_dados_stock_armazem3ci( produto );
//                ProdutosVisao.mostrar_cod_dados_stock_armazem4ci( produto );
                
                dispose();

            }
            else if ( DVML.JANELA_COMPRA == cod_janela )
            {

                DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
                int linha = tabela_busca.getSelectedRow();
                Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
                CompraVisao.accao_codigo_interno_enter_busca_exterior( codigo );
                dispose();

            }

        }
    }//GEN-LAST:event_tabela_buscaMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtDesignacaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtDesignacaoActionPerformed
    {//GEN-HEADEREND:event_txtDesignacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDesignacaoActionPerformed

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
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( BuscaNovoProdutoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( BuscaNovoProdutoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( BuscaNovoProdutoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( BuscaNovoProdutoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                BuscaNovoProdutoVisao dialog = new BuscaNovoProdutoVisao( new javax.swing.JFrame(), true, 0 );
                dialog.addWindowListener( new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing( java.awt.event.WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                } );
                dialog.setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabela_busca;
    private javax.swing.JTextField txtDesignacao;
    // End of variables declaration//GEN-END:variables

    class TratarEvento implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtDesignacao.getText().trim() + key;
                //adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                adicionar( prefixo );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    // adicionar( stockDao.getStockLIKE_Nome( prefixo ) );
                    adicionar( prefixo );

                }
                catch ( Exception e )
                {

                }

            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }

    }

//    private void adicionar( List<TbStock> list )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
//        modelo.setRowCount( 0 );
//        for ( int i = 0; i < list.size(); i++ )
//        {
//
//            TbStock stock = list.get( i );
//            modelo.addRow(
//                    new Object[]
//                    {
//                        stock.getCodProdutoCodigo().getCodigo(),
//                        stock.getCodProdutoCodigo().getDesignacao(),
//                        stock.getCodProdutoCodigo().getCodTipoProduto().getDesignacao(),
//                        precoDao.getLastPrecoByIdProduto( stock.getCodProdutoCodigo().getCodigo() ).getPrecoVenda(),
//                        stock.getQuantidadeExistente()
//                    }
//            );
//        }
//
//    }
    private void adicionar( String designacao )
    {

        Iterator<TbProduto> iterator = fonte_dados.iterator();
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
        modelo.setRowCount( 0 );

        while ( iterator.hasNext() )
        {
            TbProduto next = iterator.next();
            if ( next.getDesignacao().toLowerCase().contains( designacao.toLowerCase() ) )
            {
                modelo.addRow(
                        new Object[]
                        {
                            next.getCodigo(),
                            next.getDesignacao(),
                            next.getCodTipoProduto().getDesignacao(),
                        }
                );
            }

        }

    }

    private void adicionar_tabela()
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_busca.getModel();
        modelo.setRowCount( 0 );
        System.out.println( "FONTE DADOS : " + fonte_dados.size() );
        Iterator<TbProduto> iterator = fonte_dados.iterator();

        while ( iterator.hasNext() )
        {
            TbProduto next = iterator.next();

            modelo.addRow(
                    new Object[]
                    {
                        next.getCodigo(),
                        next.getDesignacao(),
                        next.getCodTipoProduto().getDesignacao()

                    }
            );

        }

    }

}
