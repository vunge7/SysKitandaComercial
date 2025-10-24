/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import controller.ClienteEncomendaController;
import controller.EncomendaController;
import controller.ItemEncomendaController;
import controller.ProdutoController;
import controller.StockController;
import exemplos.PermitirNumeros;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import lista.FacturaEncomenda;
import lista.ListaVenda;
import modelo.ClienteEncomendaModelo;
import modelo.ClienteModelo;
import modelo.EncomendaModelo;
import modelo.ItemEncomendaModelo;
import modelo.ItemVendaModelo;
import modelo.ProdutoModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RemoverEncomendasVisao extends javax.swing.JFrame {

    private BDConexao conexao;
    private Integer codigo_cliente = 0, codigo_produto = 0, idUser = 0, idEncomenda = 0;
    private ClienteEncomendaModelo clienteEncomendaModelo;
    private String percentagem_desconto_global = "";
    private static int id_armzem = 2;
    /**
     * Creates new form EfectuarEncomendasVisao
     */
    public RemoverEncomendasVisao() {

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

    
        txtNFactura.setDocument(new PermitirNumeros());

        this.idUser = idUser;
   
        conexao = BDConexao.getInstancia();
      
        txtNFactura.addKeyListener(new TratarEventoTecladoCodigoFactura());

       

    }
    
    
    

    public Integer getCodigo(String nome_cliente) {

        codigo_cliente = new ClienteEncomendaController().getCodigoByNome(nome_cliente);
        return codigo_cliente;

    }

   
   
    public double getDesconto(){
             
          ResultSet resultado = conexao.executeQuery("SELECT percentagem_desconto FROM tb_produto WHERE codigo = " +getCodigoProduto());       
         
          try{
              
             if(resultado.next())
                   return  resultado.getDouble("percentagem_desconto");
             
          }catch(SQLException ex){          
                ex.printStackTrace();
          }
           
        return 0;
    }
    
    
    
    

    
    public void buscar_fatura(Integer idEncomenda) throws SQLException
    {
                    
        EncomendaModelo encomendaModelo = new EncomendaController().getEncomendaByCodigo(idEncomenda);

         this.clienteEncomendaModelo = new ClienteEncomendaController().getClienteByCodigo( encomendaModelo.getIdCliente() );

        String data_prevista = String.valueOf(encomendaModelo.getData_entrega_prevista());

        //2012-06-26

        int dia = Integer.parseInt(data_prevista.substring(8, 10));;
        int mes = Integer.parseInt(data_prevista.substring(5, 7));;
        int ano = Integer.parseInt(data_prevista.substring(0, 4));

      
        Vector<ItemEncomendaModelo> vector = new ItemEncomendaController().getAllItemEncomenda(encomendaModelo.getIdEncomenda());
        
  
        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
      

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        for (int i = 0; i < vector.size(); i++) {

            ItemEncomendaModelo itemEncomendaModelo = vector.elementAt(i);
             
            this.codigo_produto = itemEncomendaModelo.getIdPrdouto();
            
             modelo.addRow(new Object[]{
                 
                itemEncomendaModelo.getIdPrdouto(),
                new ProdutoController(conexao).getProduto(itemEncomendaModelo.getIdPrdouto()).getDesignacao(),
                itemEncomendaModelo.getQauntidade(),
                new StockController(conexao).getStockProduto( itemEncomendaModelo.getIdPrdouto(), 2 ).getPreco_venda(),
                itemEncomendaModelo.getTotal()
                     
            });

        }

        setTotalBusca();


    }
    
    
   
    
    public int getCodigoProduto() {
        return this.codigo_produto;
    }

    public String getDescricao() throws SQLException {
        return new ProdutoController(conexao).getProduto(getCodigoProduto()).getDesignacao();
    }

    public double getPreco() throws SQLException {
        return new StockController(conexao).getStockProduto(getCodigoProduto(), 2).getPreco_venda();
    }

    public long getTotalPagar(){
       
            return   Long.parseLong( txtTotalPagar.getText().substring(0 , txtTotalPagar.getText().length() - 2 ) );  
    }
  
    
     
  
    
    
    public void actualizar_saldo_credito(double saldo, double credito){
    
           String sql = " UPDATE tb_clientes_encomenda SET saldo = " +saldo + " , credito = " +credito +" WHERE idCliente = " +codigo_cliente;
           conexao.executeUpdate(sql);
           
    }
    
    
    public String getDataActualComHora()
    {
       Calendar calendario = Calendar.getInstance();
        
        //buscar data
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int ano = calendario.get(Calendar.YEAR);

        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int segundo = calendario.get(Calendar.SECOND);
        
        
        String data = ano + "-" + (mes + 1) + "-" + dia + " " + hora + ":" + minuto + ":" + segundo;

        return data;
        

      
    }
    
    public String getDataActual()
    {
        Calendar calendario = Calendar.getInstance();
        
        //buscar data
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int ano = calendario.get(Calendar.YEAR);

        String data = ano + "-" + (mes + 1) + "-" + dia;

        return data;
        

      
    }
    
    
  
    
    public void setTotalPagar() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
   

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        double total_pagar = 0, saldo_restante = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_pagar += Double.parseDouble(String.valueOf(modelo.getValueAt(i, 4)));
        }
        
        

        txtTotalPagar.setText(String.valueOf(total_pagar));

        
       
       

    }
    
    
    
    
    public void setTotalBusca() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
    

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        double total_pagar = 0, saldo_restante = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_pagar += Double.parseDouble(String.valueOf(modelo.getValueAt(i, 4)));
        }

        txtTotalPagar.setText(String.valueOf(total_pagar));

        
        //calculo_saldo( total_pagar );
        
       

    }

    
    
    public void eliminar() throws SQLException{
          
        EncomendaModelo  encomendaModelo = new EncomendaModelo();
        
        EncomendaModelo encomendaModeloBusca = new EncomendaController().getEncomendaByCodigo(idEncomenda);
        
        encomendaModelo = encomendaModeloBusca;
        
        
        int opcao = new JOptionPane().showConfirmDialog(null, "Tens a certeza que pretendes eliminar essa encomenda ? ");
        
        if(opcao == JOptionPane.YES_OPTION)
        {
            
            if( new EncomendaController().getEncomendaByCodigo(  idEncomenda).isStatus_entrega() )
            {
                actualizar_quantidade(idEncomenda);
            }
            
            conexao.executeUpdate("DELETE FROM tb_item_encomenda WHERE idEncomenda = " +idEncomenda);

            if( new EncomendaController().eliminar(encomendaModelo) );
            {


                 System.err.println("TOTAL ENCOMENDA " +encomendaModelo.getTotal_encomenda() ); 
                 adicionar_saldo(    encomendaModelo.getTotal_encomenda()  );



                 remover_items();
                 JOptionPane.showMessageDialog(null, "Encomenda eliminada com sucesso!...");

            }//else {JOptionPane.showMessageDialog(null, "Erro ao eliminar a encomenda" , "ERRO", JOptionPane.ERROR);  }

        }//else JOptionPane.showMessageDialog(null, "Encomenda nao eliminda");
    
    }
   
 

    public void remover_items() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        for (int i = modelo.getRowCount() - 1; i >=0; i--) {
                    modelo.removeRow(i);
        }

    }
    
    
      public void repor_encomenda(){
            
                    actualizar_quantidade(  idEncomenda );
               
    }
    
    public  boolean  eliminar_item_produto(int idEncomenda) {
        return conexao.executeUpdate("DELETE FROM tb_item_encomenda WHERE idEncomenda = " +idEncomenda );
    }
      
     
  
   public void actualizar_quantidade(Integer idEncomenda)
   {
    
             Vector<ItemEncomendaModelo> lista = new ItemEncomendaController().getAllItemEncomenda(idEncomenda);
             
             ItemEncomendaModelo itemEncomenda;
       
             int quantidade = 0;
             for (int i = 0; i < lista.size(); i++) 
             {
                itemEncomenda = lista.elementAt(i);
                quantidade =   BDConexao.getQuantidade_Existente(  itemEncomenda.getIdPrdouto(), id_armzem) +  itemEncomenda.getQauntidade();
                conexao.executeUpdate("UPDATE tb_stock SET quantidade_existente = " + quantidade +" WHERE cod_produto_codigo = " +itemEncomenda.getIdPrdouto() + " AND cod_armazem = " +id_armzem);
       
               }
             
    }
    

    
    public void adicionar_saldo( double saldo_adicionar ) throws SQLException{

        double saldo_actual = 0, diferenca = 0;
        String sql = "";

        ResultSet resultado = conexao.executeQuery("SELECT saldo FROM tb_clientes_encomenda WHERE idCliente = " + clienteEncomendaModelo.getIdCliente());

        if (resultado.next()) {

            saldo_actual = resultado.getLong("saldo");

        }

        System.out.println("SALDO ANTES " + saldo_actual);
        System.out.println("SALDO ADICIONAR " + saldo_adicionar);


        saldo_actual = saldo_actual + saldo_adicionar;

        System.out.println("SALDO ACTUAL " + saldo_actual);

        diferenca = ((saldo_actual) + (clienteEncomendaModelo.getCredito()));


        if (diferenca < 0) {
            sql = "UPDATE tb_clientes_encomenda SET saldo = 0 , credito = " + diferenca + " WHERE idCliente = " + clienteEncomendaModelo.getIdCliente();
        } else {
            sql = "UPDATE tb_clientes_encomenda SET saldo = " + diferenca + " ,  credito = 0 WHERE idCliente = " + clienteEncomendaModelo.getIdCliente();
        }


        if (conexao.executeUpdate(sql)) {
            JOptionPane.showMessageDialog(null, "Saldo adicionado na conta do cliente !..");
        } else {
            JOptionPane.showMessageDialog(null, "ERRO AO ADICONAR O SALDO!..", "ERROR", JOptionPane.ERROR_MESSAGE);
        }


    
    }
    
    
     //----------- evento do teclado ---------------------------------------
    class TratarEventoTecladoCodigoFactura implements KeyListener {

        String prefixo = "";
        int codigo_factura = 0;

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                char key = evt.getKeyChar();

                prefixo = txtNFactura.getText().trim() + key;

                System.out.println("Codigo: " + prefixo);

                codigo_factura = Integer.parseInt(prefixo.trim());
                idEncomenda = codigo_factura;
                
                
             
                try {
                    
                    remover_items();
                 
                    buscar_fatura(codigo_factura);
                    
                } catch (SQLException ex) {
                    
                    Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null, "FATURA NAO EXISTENTE", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtNFactura = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCarrinho = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EFECTUAR ENCOMENDA");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Total da Encomenda");

        txtTotalPagar.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Codigo Factura");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTableCarrinho.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jTableCarrinho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.Prod", "Descrição", "Quantidade", "Preco", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableCarrinho);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_32x32.png"))); // NOI18N
        jButton1.setText("Eliminar  Encomenda");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(459, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
           // jCBEntregue.setSelected (false );
             eliminar();
        } catch (SQLException ex) {
            Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RemoverEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RemoverEncomendasVisao().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCarrinho;
    private javax.swing.JTextField txtNFactura;
    private javax.swing.JTextField txtTotalPagar;
    // End of variables declaration//GEN-END:variables
}
