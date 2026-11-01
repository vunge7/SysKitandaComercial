/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.ClientesController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.StoksController;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.TemaNegocioUtil;
import visao.design.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class BuscaClienteVisao extends javax.swing.JDialog {

    private Vector<TbCliente> fonte_dados = null;
    private StoksController stoksController;
    private boolean clienteInterno = false;
    private static DadosInstituicaoController dadosInstituicaoController;
    public static TbDadosInstituicao dadosInstituicao;

    public BuscaClienteVisao(java.awt.Frame parent, boolean modal, ClientesController clientesController, BDConexao conexao) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        txtNome.requestFocus();
        stoksController = new StoksController(conexao);
        // Carregar os dados da instituição (estava a ficar null)
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);
        fonte_dados = clientesController.listarTodos2();

        txtNome.requestFocus();
        try {
            adicionar_tabela();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtNome.addKeyListener(new TratarEvento());
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

        txtNome = new CampoPesquisa("Pesquisar cliente pelo nome...");

        tabela_busca = new TabelaModerna(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Cód.", "Nome", "NIF", "Telefone"
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
        colunas.getColumn(1).setPreferredWidth(420);
        colunas.getColumn(2).setPreferredWidth(180);
        colunas.getColumn(3).setPreferredWidth(160);
        jScrollPane1 = new RolagemModerna(tabela_busca);

        jButton1 = new BotaoFechar();
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1 = new javax.swing.JPanel();

        LayoutPesquisa.para(this)
                .titulo("C", "Pesquisa de Clientes", "Escreva o nome para filtrar e clique no cliente pretendido")
                .campo(txtNome)
                .tabela(jScrollPane1)
                .fechar(jButton1)
                .atalhos("Clique no cliente para o seleccionar     •     Esc: fechar")
                .aoSeleccionar(() -> {
                    int linha = tabela_busca.getSelectedRow();
                    if (linha >= 0) {
                        tratarSelecaoTabela(linha);
                    }
                })
                .tamanho(980, 680)
                .montar();
    }

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabela_buscaMouseClicked
        // TODO add your handling code here:

        // Simula o duplo clique chamando o mesmo método
        int linha = tabela_busca.getSelectedRow();
        if (linha >= 0) {
            tratarSelecaoTabela(linha);
        }

    }//GEN-LAST:event_tabela_buscaMouseClicked

//    private void tratarSelecaoTabela( int linha )
//    {
//
//        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
//        Integer codigo = Integer.parseInt( modelo.getValueAt( linha, 0 ).toString() );
//        ClienteVisao.importarDados( buscarPorCodigo( fonte_dados, codigo ) );
//        dispose();
//
//    }
    private void tratarSelecaoTabela(int linha) {

        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();

        Integer codigo = Integer.parseInt(
                modelo.getValueAt(linha, 0).toString()
        );

        if (clienteInterno) {

            ClienteInternoVisao.importarDados(
                    buscarPorCodigo(fonte_dados, codigo)
            );

        } else {

            ClienteVisao.importarDados(
                    buscarPorCodigo(fonte_dados, codigo)
            );
        }

        dispose();
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
            java.util.logging.Logger.getLogger(BuscaClienteVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscaClienteVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscaClienteVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscaClienteVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscaClienteVisao dialog = new BuscaClienteVisao(new javax.swing.JFrame(), true, new ClientesController(BDConexao.getInstancia()), BDConexao.getInstancia());
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabela_busca;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

    class TratarEvento implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtNome.getText().trim() + key;
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
        Iterator<TbCliente> iterator = fonte_dados.iterator();
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        modelo.setRowCount(0);
        while (iterator.hasNext()) {
            TbCliente next = iterator.next();
            if (next.getNome().toLowerCase().contains(designacao.toLowerCase())) {

                modelo.addRow(
                        new Object[]{
                            next.getCodigo(),
                            next.getNome(),
                            next.getNif(),
                            next.getTelefone(),}
                );
            }
        }
    }

    public TbCliente buscarPorCodigo(Vector<TbCliente> fonteDados, int codigoBuscado) {
        Iterator<TbCliente> iterator = fonteDados.iterator();
        TbCliente clienteEncontrado = null;

        while (iterator.hasNext()) {
            TbCliente cliente = iterator.next();
            if (cliente.getCodigo() == codigoBuscado) {
                clienteEncontrado = cliente;
                break; // sai do loop assim que encontrar
            }
        }

        return clienteEncontrado;
    }

    private void adicionar_tabela() {

        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();
        modelo.setRowCount(0);
        Iterator<TbCliente> iterator = fonte_dados.iterator();
        System.err.println("SIZE-ITERATOR: " + fonte_dados.size());
        while (iterator.hasNext()) {
            TbCliente next = iterator.next();
            modelo.addRow(
                    new Object[]{
                        next.getCodigo(),
                        next.getNome(),
                        next.getNif(),
                        next.getTelefone(),}
            );
        }
    }

}
