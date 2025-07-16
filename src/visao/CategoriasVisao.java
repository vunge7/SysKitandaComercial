/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ModeloVisao.java
 *
 * Created on 27/12/2013, 12:24:14
 */
package visao;

import comercial.controller.DocumentosController;
import comercial.controller.FamiliasController;
import comercial.controller.GruposController;
import comercial.controller.TipoProdutosController;
import dao.CategoriasDao;
import dao.FamiliaDao;
import entity.Familia;
import entity.TbTipoProduto;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class CategoriasVisao extends javax.swing.JFrame {

    /**
     * Creates new form ModeloVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
//    private static TipoProdutosController tipoProdutosController;
//    private static GruposController gruposController;
//    private static FamiliasController familiasController;
//    private Modelo modelo;
    private CategoriasDao categoriasDao = new CategoriasDao(emf);
    private TbTipoProduto subFamilia;
    private Familia familia;
    private FamiliaDao familiaDao = new FamiliaDao(emf);
    private String descricao = "";
    private int cod_modelo = 0;
    private static BDConexao conexao, conexaoTransaction;

    public CategoriasVisao( BDConexao conexao ) {

        initComponents();
        setLocationRelativeTo(null);
        CategoriasVisao.conexao = conexao;
//        tipoProdutosController = new TipoProdutosController( conexao );
//        gruposController = new GruposController( conexao );
//        familiasController = new FamiliasController( conexao );
        actualizar();
        setComponentes();

    }

    private void setComponentes() {
        cmbFamilia.setModel(new DefaultComboBoxModel((Vector) familiaDao.buscaTodos()));
        //cmbSubFamilia.setModel( new DefaultComboBoxModel( ( Vector ) categoriasDao.getDescricaoByIdFamilia( getIdFamilia() ) ) );

    }

    private void actualizar() {
        adicionar_tabela(categoriasDao.buscaTodasCategorias());
    }

    public void salvar()
    {

        if ( !vazios_campos() )
        {
            this.descricao = MetodosUtil.iniciais_maiuscula( MetodosUtil.trimAll( txtDescricao.getText().trim() ) );
            if ( !categoriasDao.exist_categoria( descricao ) )
            {
                try
                {
                    subFamilia = new TbTipoProduto();
                    setDadosModelo();
                    categoriasDao.create( subFamilia );
                    actualizar();
                    setComponentes();
                    limpar();
                    JOptionPane.showMessageDialog( null, "Dados salvo com sucesso!..." );
                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Erro ao cadastrar o modelo ", "ERRO", JOptionPane.ERROR_MESSAGE );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Este modelo ja existe ", "AVISO", JOptionPane.WARNING_MESSAGE );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Pf digita o modelo ", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
    }
//    public void salvar() {
//        conexaoTransaction = new BDConexao();
//        DocumentosController.startTransaction(conexaoTransaction);
//
//        TbTipoProduto subFamilia_local = new TbTipoProduto();
//
//        if (vazios_campos()) {
//            this.descricao = MetodosUtil.iniciais_maiuscula(MetodosUtil.trimAll(txtDescricao.getText().trim()));
//
//            if (!tipoProdutosController.exist_tipo_produto(descricao)) {
////                setDadosModelo();
//
//                subFamilia_local.setDesignacao(txtDescricao.getText());
//                subFamilia_local.setFkFamilia(familiasController.getFamiliaById(getIdFamilia()));
////                subFamilia_local.setArea(String.valueOf(jLabelArea.getText()));
////                reserva.setNomeResponsavel( txtNomeResponsavel.getText() );
//                try {
//                    if (tipoProdutosController.salvar(subFamilia_local)) {
//
////                        categoriasDao.create( subFamilia );
//                        actualizar();
//                        setComponentes();
//                        limpar();
//                        JOptionPane.showMessageDialog(null, "Dados salvo com sucesso!...");
//
////                        if (!Objects.isNull(ProdutosVisao.cmbTipoProduto)) {
//////                            ProdutosVisao.cmbTipoProduto.setModel( new DefaultComboBoxModel( categoriasDao.buscaTodosExtra() ) );
////                            ProdutosVisao.cmbTipoProduto.setModel(new DefaultComboBoxModel(tipoProdutosController.getVector()));
////                            ProdutosVisao.cmbTipoProduto.setSelectedItem(subFamilia.getDesignacao());
////                            dispose();
////                            ProdutosVisao.getDescricao();
////                        }
//
//                    }
//
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar a subfamilia ", "ERRO", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Esta subfamilia já existe ", "AVISO", JOptionPane.WARNING_MESSAGE);
//            }
//        } else {
////            JOptionPane.showMessageDialog( null, "Pf digita a subfamilia ", "AVISO", JOptionPane.WARNING_MESSAGE );
//        }
//    }

    private boolean vazios_campos() {

        return txtDescricao.getText().equals("");

    }

    public void setDadosModelo() {
        subFamilia.setDesignacao(this.descricao);
        subFamilia.setFkFamilia(familiaDao.findFamilia(getIdFamilia()));
    }

    public void alterar() {

        if (!vazios_campos()) {
            try {
                this.descricao = MetodosUtil.iniciais_maiuscula(MetodosUtil.trimAll(txtDescricao.getText().trim()));
                subFamilia = categoriasDao.findTbTipoProduto(this.cod_modelo);
                setDadosModelo();
                categoriasDao.edit(subFamilia);
                actualizar();
                setComponentes();
                limpar();
                JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!...");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao alterar o modelo ", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pf. seleccione o modelo para alterar");
        }

    }

    public void limpar() {
        txtDescricao.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cmbFamilia = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRO DE SUB FAMÍLIAS");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("Nova Sub Família");

        txtDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescricaoActionPerformed(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N.º", "Descrição", "Família"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jLabel2.setText("Família");

        cmbFamilia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFamiliaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbFamilia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setBackground(new java.awt.Color(255, 0, 51));
        btnCancelar.setText("Sair");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_16x16.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_16x16.png"))); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescricaoActionPerformed
        salvar();
    }//GEN-LAST:event_txtDescricaoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        
        try {
            salvar();
        } catch (Exception e) {
        e.printStackTrace();
        }


    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        alterar();

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() >= 2) {

            setDadosModeloModelo();

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFamiliaActionPerformed
    {//GEN-HEADEREND:event_cmbFamiliaActionPerformed
        // TODO add your handling code here:
        //        cmbSubFamilia.setModel( new DefaultComboBoxModel( ( Vector ) categoriasDao.getDescricaoByIdFamilia( getIdFamilia() ) ) );
        //        txtDescricao.setText( String.valueOf( cmbSubFamilia.getSelectedItem() ) );
        // this.cod_modelo = categoriasDao.getIdByDescricao( txtDescricao.getText() );
    }//GEN-LAST:event_cmbFamiliaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CategoriasVisao( new BDConexao()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbFamilia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtDescricao;
    // End of variables declaration//GEN-END:variables

//    
//    
//    private int getIdTipoPagamento()
//    {
//    
//             return tipoPagamentoDao.getIdByDescricao(  String.valueOf( 1 )  );
//    
//    }
//    
//    
    private void adicionar_tabela(List<TbTipoProduto> tipoProduto) {

        DefaultTableModel modelos = (DefaultTableModel) jTable1.getModel();

        modelos.setRowCount(0);
        for (int i = 0; i < tipoProduto.size(); i++) {
            modelos.addRow(new Object[]{
                tipoProduto.get(i).getCodigo(),
                tipoProduto.get(i).getDesignacao(),
                tipoProduto.get(i).getFkFamilia().getDesignacao()

            });
        }

    }

    public void setDadosModeloModelo() {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        this.cod_modelo = Integer.parseInt(String.valueOf(modelo.getValueAt(jTable1.getSelectedRow(), 0)));
        subFamilia = categoriasDao.getCategoriasByDesignacao(modelo.getValueAt(jTable1.getSelectedRow(), 1).toString());
        familia = familiaDao.getFamiliaByDescricao(modelo.getValueAt(jTable1.getSelectedRow(), 2).toString());
        txtDescricao.setText(subFamilia.getDesignacao());
        cmbFamilia.setSelectedItem(familia.getDesignacao());

    }

    private int getIdFamilia() {
        return familiaDao.getIdByDescricao(String.valueOf(cmbFamilia.getSelectedItem()));
    }

}
