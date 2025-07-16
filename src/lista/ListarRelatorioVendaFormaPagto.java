/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import dao.ItemVendaDao;
import dao.PrecoDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.TbItemVenda;
import entity.TbPreco;
import entity.TbUsuario;
import entity.TbVenda;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.VendaUsuarioUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ListarRelatorioVendaFormaPagto extends javax.swing.JFrame {

    /**
     * Creates new form ListaUsuarioVisao
     */
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private VendaDao vendaDao = new VendaDao(emf);
    private UsuarioDao usuarioDao = new UsuarioDao(emf);    
    private ItemVendaDao itemVendaDao = new ItemVendaDao(emf);
    private PrecoDao precoDao = new PrecoDao(emf);
    private  double total_geral = 0;
    private BDConexao conexao;
    
    public ListarRelatorioVendaFormaPagto(BDConexao conexao)  {
        
        
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        this.conexao = conexao;
        dcDataInicio.setDate( new Date() );
        dcDataFim.setDate( new Date() );
        
        
       
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnCancelar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbData = new javax.swing.JLabel();
        dcDataInicio = new com.toedter.calendar.JDateChooser();
        lbData1 = new javax.swing.JLabel();
        dcDataFim = new com.toedter.calendar.JDateChooser();
        lbData2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::: DVML - RELATORIO DIARIO::...");

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/impressora1.png"))); // NOI18N
        btnCancelar.setText("Imprimir");
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar1.setText("Sair");
        btnCancelar1.setAlignmentX(0.5F);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelar1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbData.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData.setText("De");

        lbData1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbData1.setText("à");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbData1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dcDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcDataInicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbData1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataFim, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        lbData2.setBackground(new java.awt.Color(0, 51, 102));
        lbData2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 18)); // NOI18N
        lbData2.setForeground(new java.awt.Color(255, 255, 255));
        lbData2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbData2.setText("RELATÓRIO DE VENDAS POR FORMAS DE PAGAMENTOS");
        lbData2.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lbData2, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(169, 169, 169))
            .addGroup(layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lbData2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnCancelar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
      
        //procedimento_imprimir();
        new ResumoVendaFormaPagto(dcDataInicio.getDate(), dcDataFim.getDate());

    }//GEN-LAST:event_btnCancelarActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendaFormaPagto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendaFormaPagto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendaFormaPagto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarRelatorioVendaFormaPagto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ListarRelatorioVendaFormaPagto( new BDConexao()).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(ListarRelatorioVendaFormaPagto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar1;
    private com.toedter.calendar.JDateChooser dcDataFim;
    private com.toedter.calendar.JDateChooser dcDataInicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbData1;
    private javax.swing.JLabel lbData2;
    // End of variables declaration//GEN-END:variables


private void procedimento_imprimir()
    {
            
         List<VendaUsuarioUtil> lista = new ArrayList<>();          
         List<VendaUsuarioUtil> lista_resumo = new ArrayList<>();          
         double lucro =0;
         VendaUsuarioUtil vendaUsuarioUtil;
         List<TbVenda> venda;
        
         List<TbUsuario> usuarios = usuarioDao.getAllUsuario();
        
         for (int i = 0; i < usuarios.size(); i++) 
         {
          
             venda   = vendaDao.getAllVendaByUsuario(  usuarios.get(i).getCodigo() , dcDataInicio.getDate(), dcDataFim.getDate() );
             
             try {
            
                 if (venda.get(0).getCodigo() !=0 ) 
                 {
                     
                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setNumero_fact(usuarios.get(i).getNome().toUpperCase());
                     lista.add(vendaUsuarioUtil);

                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setNumero_fact("");
                     lista.add(vendaUsuarioUtil);
                     
                     for (int j = 0; j < venda.size(); j++) 
                     {
                         vendaUsuarioUtil = new VendaUsuarioUtil();
                         vendaUsuarioUtil.setNumero_fact(String.valueOf(venda.get(j).getCodigo()));
                         vendaUsuarioUtil.setCliente_nome(venda.get(j).getNomeCliente());
                         vendaUsuarioUtil.setData_venda(venda.get(j).getDataVenda().getDate() + "/"
                                 + (venda.get(j).getDataVenda().getMonth() + 1) + (venda.get(j).getDataVenda().getYear() + 1900)
                                 +"   " +venda.get(j).getHora().getHours() + ":" +venda.get(j).getHora().getMinutes()
                                 );
                         vendaUsuarioUtil.setTotal_venda(  getValor( String.valueOf(venda.get(j).getTotalVenda()) ) );

                         
                       
                         
                         lista.add(vendaUsuarioUtil);
                         
                         lucro =  lucro +  getLucroFactura(  venda.get(j).getCodigo() );

                     }
                     
                     //Adicionar espaço em branco
                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setNumero_fact("");
                     lista.add(vendaUsuarioUtil);

                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setData_venda("SUBTOTAL");
                     vendaUsuarioUtil.setTotal_venda(getValor(  String.valueOf(   getTotalVenda( venda )  ) ) );                     
                     lista.add(vendaUsuarioUtil);
                     
                     //lucro = getLucroFactura(  venda.get(i) );
                     
                     
                                   //Adicionar espaço em branco
                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setNumero_fact("");
                     lista.add(vendaUsuarioUtil);
                     
                     this.total_geral = this.total_geral + getTotalVenda( venda );     
                     
                     //RESUMO
                     vendaUsuarioUtil = new VendaUsuarioUtil();
                     vendaUsuarioUtil.setData_venda(usuarios.get(i).getNome().toUpperCase() );
                     vendaUsuarioUtil.setTotal_venda(getValor(  String.valueOf(   getTotalVenda( venda )  ) ) );
                     lista_resumo.add(vendaUsuarioUtil);
                    
                 }

             } catch (Exception e) {
             }
             
           
             
         }
         
         
        //Adicionar espaço em branco
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setNumero_fact("");
        lista.add(vendaUsuarioUtil);
         
        //Adicionar espaço em branco
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setNumero_fact("RESUMO");
        lista.add(vendaUsuarioUtil);
        
        //Adicionar espaço em branco
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setNumero_fact("");
        lista.add(vendaUsuarioUtil);
        
        for (int i = 0; i < lista_resumo.size(); i++) 
        {
            lista.add(lista_resumo.get(i));
        }

        //Adicionar espaço em branco
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setNumero_fact("");
        lista.add(vendaUsuarioUtil);
        
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setData_venda("TOTAL GERAL");
        vendaUsuarioUtil.setTotal_venda(  getValor(  String.valueOf(   total_geral) ) );
        lista.add(vendaUsuarioUtil);
        
        
        //Adicionar espaço em branco
        vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setNumero_fact("");
        lista.add(vendaUsuarioUtil);
        
        //Adicionar lucro
         vendaUsuarioUtil = new VendaUsuarioUtil();
        vendaUsuarioUtil.setData_venda("LUCRO");
        vendaUsuarioUtil.setTotal_venda(  getValor(  String.valueOf(   lucro) ) );
        lista.add(vendaUsuarioUtil);
        
        new ListarTodasVendas(lista, "De " + dcDataInicio.getDate().getDate() + "/" + (dcDataInicio.getDate().getMonth() + 1)  + "/"  + (dcDataInicio.getDate().getYear() + 1900) + " à "
                + dcDataFim.getDate().getDate() + "/" + (dcDataFim.getDate().getMonth() + 1)  + "/" + (dcDataFim.getDate().getYear() + 1900));

        this.total_geral = 0;
        
    }


    private double getTotalVenda(List<TbVenda> lista) 
    {
        double total = 0;
        for (int i = 0; i < lista.size(); i++) {
            total = total + lista.get(i).getTotalVenda().doubleValue();
        }        
           
        return total;

    }

    private String getValor(String valor)
    {
        JFormattedTextField valor_marcarado;
        //Defindo uma Mascará
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###.00");
        NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
        numberFormatter.setFormat(decimalFormat);
        numberFormatter.setAllowsInvalid(false);

        valor_marcarado = new JFormattedTextField();
        valor_marcarado.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
        //Convertendo o salrio base em uma String
       
        //Retirando o ponto por causa da conversão do double.
        String valor_subsidio = valor.substring(0, valor.length() - 2).replace(".", "").trim();
        valor_marcarado.setText(valor_subsidio);
        
        return valor_marcarado.getText();
    
    
    }

    private double getLucroFactura(int idFactura)
    {
        double soma_compra = 0, soma_venda = 0, ultimo_preco_compra = 0,  ultimo_preco_venda = 0, lucro = 0;
        double qtd = 0d;
        List<TbItemVenda> lista = itemVendaDao.getAllItemVendasByIdVenda(idFactura);
        TbItemVenda itemVenda;
        TbPreco preco;
        for (int i = 0; i < lista.size(); i++) 
        {
              itemVenda = lista.get(i);
              
              try {
                
            } catch (Exception e) {
            }
              preco = precoDao.getLastPrecoByIdProduto(  itemVenda.getCodigoProduto().getCodigo()  );
              
              qtd = itemVenda.getQuantidade();              
              ultimo_preco_compra = preco.getPrecoCompra().doubleValue();              
              ultimo_preco_venda = preco.getPrecoVenda().doubleValue();              
              soma_compra =  qtd*ultimo_preco_compra;
              soma_venda =  qtd*ultimo_preco_venda;    
              
              lucro = lucro + (soma_venda - soma_compra );
              
              soma_compra = 0;
              soma_venda = 0;
        }
        
        
        System.out.println("LUCRO :"   +lucro);
        
        return lucro;
    
    }





}
