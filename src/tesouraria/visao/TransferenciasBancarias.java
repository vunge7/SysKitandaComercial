/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ProvinciaVisao.java
 *
 * Created on 27/12/2013, 12:24:14
 */
package tesouraria.visao;


import java.sql.Connection;
import visao.*;
//import dao.BancoDao;
import dao.TransferenciaBancariaDao;
import dao.UsuarioDao;
//import entity.TbBanco;
import entity.TransferenciaBancaria;
import java.util.Date;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.OperacaoSistemaUtil;
import util.PermitirNumeros;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TransferenciasBancarias extends javax.swing.JFrame
{

    /**
     * Creates new form ProvinciaVisao
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private TransferenciaBancaria transferenciaBancaria;
//    private BancoDao  bancoDao = new BancoDao(emf);
//    private TbBanco banco;
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private TransferenciaBancariaDao tranferenciaBancariaDao = new TransferenciaBancariaDao( emf );
    private Integer idUser = 0;
    private BDConexao conexao;
    private OperacaoSistemaUtil osu = new OperacaoSistemaUtil();

    public TransferenciasBancarias( Integer idUser )
    {

        initComponents();
        this.idUser = idUser;
        setLocationRelativeTo( null );
        conexao = BDConexao.getInstancia();
        txtValorTranferencia.setDocument( new PermitirNumeros() );
        actualizar();
        txtValorTranferencia.setText( "0" );

    }

    private void setDados1()
    {

//            banco = bancoDao.findTbBanco( getIdBancoOrigem());
//            lb1.setText(  banco.getSaldoBanco().toString() );
//            lb10.setText("Saldo Actual");
    }

    private void setDados2()
    {

//        banco = bancoDao.findTbBanco( getIdBancoDestino() );
//        lb2.setText( banco.getSaldoBanco().toString() );
//        lb11.setText( "Saldo Actual" );

    }

    private void actualizar()
    {
//        cmbBancoOrigem.setModel( new DefaultComboBoxModel( (Vector) bancoDao.buscaTodosExceptoCaixaLocal1() ) );
//        cmbBancoDestino.setModel( new DefaultComboBoxModel( (Vector) bancoDao.buscaTodosExceptoCaixaLocal1() ) );
    }

    public boolean validar_combos()
    {

        if ( cmbBancoOrigem.getSelectedItem().toString().equals( cmbBancoDestino.getSelectedItem().toString() ) )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nImpossivel transferir valores monetários no mesmo banco da Empresa!" );
            return false;
        }
        return true;

    }

    public boolean validar_maximo_minimo()
    {

//        if(Double.parseDouble(lb1.getText() ) < ( Double.parseDouble(lb2.getText()))){
        if ( Double.parseDouble( txtValorTranferencia.getText() ) > ( Double.parseDouble( lb1.getText() ) ) )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO valor a transferir não pode ser superior ao saldo do banco de origem!" );
            return false;
        }
        return true;

    }

    public boolean validar_numero_menor()
    {
//        if(txtValorTranferencia.getText() != null){
        if ( Double.parseDouble( txtValorTranferencia.getText().trim() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nInsira o valor a transferir!" );
            txtValorTranferencia.requestFocus();
            return false;
        }
        return true;

    }

    public boolean selecione()
    {

        if ( cmbBancoOrigem.getSelectedItem().equals( "--Seleccione--" ) || cmbBancoDestino.getSelectedItem().equals( "--Seleccione--" ) )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nSeleccione o Banco!" );
            return false;
        }
        return true;

    }

    public boolean validar_descricao()
    {
        if ( taDescricao.getText().equals( "" ) )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nDigite a descrição da transferência do Valor!" );
            taDescricao.requestFocus();
            return false;
        }
        return true;

    }

    public boolean validar_borderaux()
    {
        if ( txtNumeroBorderaux.getText().equals( "" ) )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nDigite o número do borderaux!" );
            txtNumeroBorderaux.requestFocus();
            return false;
        }
        return true;

    }

    private void limpar()
    {

        txtValorTranferencia.setText( "0" );
    }

    public void enviar()
    {
        if ( selecione() )
        {

            if ( validar_combos() )
            {

                if ( validar_numero_menor() )
                {

                    if ( validar_maximo_minimo() )
                    {

                        if ( validar_borderaux() )
                        {
                            if ( validar_descricao() )
                            {

                                try
                                {
                                    transferenciaBancaria = new TransferenciaBancaria();
                                    setDadosTransferencia();
                                    tranferenciaBancariaDao.create( transferenciaBancaria );
//                            actualizar();
//                            MetodosUtil.subtrair_saldo_banco(transferenciaBancaria.getValor(), transferenciaBancaria.getFkBancoOrigem().getIdBanco(), conexao );
//                            MetodosUtil.adicionar_saldo_banco( transferenciaBancaria.getValor(), transferenciaBancaria.getFkBancoDestino().getIdBanco(), conexao  );

//                            limpar();
                                    JOptionPane.showMessageDialog( null, "Valor transferido com sucesso!..." );

                                    setDados1();
                                    setDados2();
                                    actualizar();
                                    limpar();
                                }
                                catch ( Exception e )
                                {
                                    JOptionPane.showMessageDialog( null, "Erro ao transferir valores ", "ERRO", JOptionPane.ERROR_MESSAGE );
                                }

                            }

                        }

                    }

                }

            }

        }

    }

    public void setDadosTransferencia()
    {

        transferenciaBancaria = new TransferenciaBancaria();
        transferenciaBancaria.setData( new Date() );
        transferenciaBancaria.setHora( new Date() );
        transferenciaBancaria.setValor( Double.parseDouble( txtValorTranferencia.getText() ) );
//        transferenciaBancaria.setFkBancoOrigem( bancoDao.findTbBanco( getIdBancoOrigem() ) );
//        transferenciaBancaria.setFkBancoDestino( bancoDao.findTbBanco( getIdBancoDestino() ) );
        transferenciaBancaria.setFkUsuario( usuarioDao.findTbUsuario( idUser ) );

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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbBancoOrigem = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbBancoDestino = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtValorTranferencia = new javax.swing.JTextField();
        lb10 = new javax.swing.JLabel();
        lb1 = new javax.swing.JLabel();
        lb11 = new javax.swing.JLabel();
        lb2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescricao = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        txtNumeroBorderaux = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btn_enviar = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        status_mensagem_secundaria = new javax.swing.JLabel();
        status_mensagem_primaria = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TRANSFERENCIAS BANCARIAS");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel3.setText("Banco de Origem");

        cmbBancoOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBancoOrigemActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel4.setText("Banco de Destino");

        cmbBancoDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBancoDestinoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel5.setText("Valor a Transferir :");

        txtValorTranferencia.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        txtValorTranferencia.setText("0");

        lb1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lb1.setForeground(new java.awt.Color(0, 102, 102));

        lb2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lb2.setForeground(new java.awt.Color(0, 102, 102));

        taDescricao.setColumns(20);
        taDescricao.setRows(5);
        jScrollPane1.setViewportView(taDescricao);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel6.setText("Nº. do Borderaux :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(75, 75, 75)
                            .addComponent(lb10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(cmbBancoOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lb1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                            .addComponent(txtValorTranferencia))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroBorderaux))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbBancoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb11, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbBancoOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(lb11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lb2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbBancoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValorTranferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroBorderaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
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
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btn_enviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btn_enviar.setText("Enviar");
        btn_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_enviarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(btn_enviar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAlterar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel19.setText("TRANSFERÊNCIAS BANCÁRIAS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_secundaria.setText("...");

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 841, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(status_mensagem_primaria)
                .addGap(12, 12, 12)
                .addComponent(status_mensagem_secundaria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbBancoOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBancoOrigemActionPerformed
        // TODO add your handling code here:
        setDados1();

    }//GEN-LAST:event_cmbBancoOrigemActionPerformed

    private void btn_enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_enviarActionPerformed
        // TODO add your handling code here:
        enviar();


    }//GEN-LAST:event_btn_enviarActionPerformed

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

    private void cmbBancoDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBancoDestinoActionPerformed
        setDados2();
    }//GEN-LAST:event_cmbBancoDestinoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new TransferenciasBancarias( 1 ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    public static javax.swing.JButton btn_enviar;
    private javax.swing.JComboBox cmbBancoDestino;
    private javax.swing.JComboBox cmbBancoOrigem;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb1;
    private javax.swing.JLabel lb10;
    private javax.swing.JLabel lb11;
    private javax.swing.JLabel lb2;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private javax.swing.JTextArea taDescricao;
    private javax.swing.JTextField txtNumeroBorderaux;
    private javax.swing.JTextField txtValorTranferencia;
    // End of variables declaration//GEN-END:variables

}
