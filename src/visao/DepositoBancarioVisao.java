/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProvinciaVisao.java
 *
 * Created on 27/12/2013, 12:24:14
 */

package visao;


import java.sql.Connection;
import dao.BancoDao;
import dao.DepositoBancariaDao;
import dao.TransferenciaBancariaDao;
import dao.UsuarioDao;
import entity.TbBanco;
import entity.DepositoBancario;
import entity.TransferenciaBancaria;
import java.util.Date;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.PermitirNumeros;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DepositoBancarioVisao extends javax.swing.JFrame {

    /** Creates new form ProvinciaVisao */
    
   
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DepositoBancario depositoBancario;
    private TbBanco banco;
    private BancoDao  bancoDao = new BancoDao(emf);
    private DepositoBancariaDao depositoBancariaDao = new DepositoBancariaDao(emf);
    private UsuarioDao usuarioDao = new UsuarioDao(emf);
    private final static String DVML_COMERCIAL_LDA = " DVML-COMERCIAL, Lda";
//    private TransferenciaBancariaDao tranferenciaBancariaDao = new TransferenciaBancariaDao(emf);
     private Integer idUser = 0;
     private BDConexao conexao;
    
    
    public DepositoBancarioVisao( Integer idUser) {
        
        initComponents();
        this.idUser = idUser;
        setLocationRelativeTo(null);
        conexao = BDConexao.getInstancia();
        txtValorDeposito.setDocument( new PermitirNumeros() );
        actualizar();
        txtValorDeposito.setText("0");
           
    
    }
  
    private void actualizar()
    {
       cmbBancoDestino.setModel(   new DefaultComboBoxModel( (Vector) bancoDao.buscaTodosExceptoCaixaLocal1() ) );
//       cmbBancoDestino.setModel(   new DefaultComboBoxModel( (Vector) bancoDao.buscaTodos() ) );
    }
    
    public boolean vazioValorDepositar(){
            return txtValorDeposito.getText().equals("");
    }
    
    private void limpar(){
        
        txtNumeroBorderaux.setText("");
        txtValorDeposito.setText("0");
   
    }
    
        public boolean selecione(){
        
                
        if( cmbBancoDestino.getSelectedItem().equals("--Seleccione--")){
            JOptionPane.showMessageDialog(null, "Atenção\nSeleccione o TbBanco!");
            return false;
        }return true;
                
    }
        
            public boolean validar_descricao(){
    //        if(txtValorTranferencia.getText() != null){
            if(taDescricao.getText().equals("") ){
                JOptionPane.showMessageDialog(null, "Atenção\nDigite a descrição do depósito bancário!");
                taDescricao.requestFocus();
                return false;
            }return true;

        }
    
            public boolean validar_numero_menor(){
    //        if(txtValorTranferencia.getText() != null){
            if(Double.parseDouble(txtValorDeposito.getText().trim() ) == 0){
                JOptionPane.showMessageDialog(null, "Atenção\nInsira o valor a depositar na Conta!");
                txtValorDeposito.requestFocus();
                return false;
            }return true;

        }

        public boolean validar_borderaux(){
    //        if(txtValorTranferencia.getText() != null){
            if(txtNumeroBorderaux.getText().equals("") ){
                JOptionPane.showMessageDialog(null, "Atenção\nDigite o número do borderaux!");
                txtNumeroBorderaux.requestFocus();
                return false;
            }return true;

        }
    
    public void enviar()
    {
            if(selecione()){
                
              if(validar_borderaux()){
                
                if(validar_numero_menor()){
                    
                    if(validar_descricao()){

                try {
                            depositoBancario = new DepositoBancario();
                            setDadosDepositoBancario();
                            depositoBancariaDao.create(depositoBancario);
                            MetodosUtil.adicionar_saldo_banco(depositoBancario.getValor(), depositoBancario.getFkBanco().getIdBanco(), conexao  );  
                            JOptionPane.showMessageDialog(null, "Valor depositado com sucesso!...");   
                            setDados();
                            actualizar();
                            limpar();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao depositar valores ", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
                
              }
                    
           }
                
          }      
                
                
        }
                
      
    }
    
    
    public void setDadosDepositoBancario(){
                    
                depositoBancario = new DepositoBancario();
                depositoBancario.setData(new Date()    );
                depositoBancario.setHora(new Date()    );
                depositoBancario.setNborderaux(txtNumeroBorderaux.getText());
                depositoBancario.setDescricao( taDescricao.getText());
                depositoBancario.setValor(Double.parseDouble(txtValorDeposito.getText()));
                depositoBancario.setFkBanco(bancoDao.findTbBanco(getIdBancoDestino()));
                depositoBancario.setFkUsuario(usuarioDao.findTbUsuario(idUser));
           
        
    }
    
    
    private void setTransferencia()
    {
//            banco = bancoDao.findTbBanco( getIdBanco() );
//            
//            txtDescricao.setText(   banco.getDescrisao() );
//            txtContaNumero.setText(   banco.getNumero()   );
       
    }
    
    
    
    
    
//    public void alterar()
//    {
//       
//        if( bancoDao.exist_banco(  String.valueOf(  cmbBancoOrigem.getSelectedItem() ) ) )
//        {
//                try {
//                    
//                            banco = bancoDao.findTbBanco( getIdBanco() );
//                            setDadosBanco();
//                            bancoDao.edit(banco);
//                            actualizar();
//                            limpar();
//                            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!...");  
//                            
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, "Erro ao alterar o tipo de pagamento ", "ERRO", JOptionPane.ERROR_MESSAGE);
//                }
//        
//        }else  JOptionPane.showMessageDialog(null, "Este tipo de pagamento não ja existe ", "AVISO", JOptionPane.WARNING_MESSAGE);
//    
//    }
    
//     public void limpar() {
//            txtDescricao.setText("");
//            txtContaNumero.setText("");
//     }
        
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbBancoDestino = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtValorDeposito = new javax.swing.JTextField();
        txtNumeroBorderaux = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lbValorBanco = new javax.swing.JLabel();
        lb10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescricao = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Depósitos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel3.setText("TbBanco Beneficiário");

        cmbBancoDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBancoDestinoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel5.setText("Valor a Depositar :");

        txtValorDeposito.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel6.setText("Nº. do Borderaux :");

        lbValorBanco.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        lbValorBanco.setForeground(new java.awt.Color(0, 102, 102));

        lb10.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        taDescricao.setColumns(20);
        taDescricao.setRows(5);
        jScrollPane1.setViewportView(taDescricao);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNumeroBorderaux, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(txtValorDeposito))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbBancoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbValorBanco, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(lb10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(lb10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbBancoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbValorBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroBorderaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtValorDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnCancelar.setBackground(new java.awt.Color(255, 0, 51));
        btnCancelar.setText("Saír");
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
        btnAlterar.setEnabled(false);
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btnSalvar.setText("Depositar");
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
                .addGap(91, 91, 91)
                .addComponent(btnSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAlterar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addContainerGap(161, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setText("DEPÓSITOS  BANCÁRIOS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbBancoDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBancoDestinoActionPerformed
        // TODO add your handling code here:
        setDados();
        
    }//GEN-LAST:event_cmbBancoDestinoActionPerformed
//    banco = bancoDao.findTbBanco( getIdBanco() );
    private void setDados(){

            banco = bancoDao.findTbBanco( getIdBancoDestino() );
            lbValorBanco.setText(  banco.getSaldoBanco().toString() );
            lb10.setText("Saldo Actual");
        
    }
    
    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        enviar();
 
        
        
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
//        alterar();

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DepositoBancarioVisao(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cmbBancoDestino;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb10;
    private javax.swing.JLabel lbValorBanco;
    private javax.swing.JTextArea taDescricao;
    private javax.swing.JTextField txtNumeroBorderaux;
    private javax.swing.JTextField txtValorDeposito;
    // End of variables declaration//GEN-END:variables
    
    private int getIdBancoDestino()
    {
    
             return bancoDao.getIdByDescricao(String.valueOf(cmbBancoDestino.getSelectedItem() )  );
    
    }
    
    
}
