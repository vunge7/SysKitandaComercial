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
import modelo.ProdutoModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EfectuarEncomendasVisao extends javax.swing.JFrame {

    private BDConexao conexao;
    private Integer codigo_cliente = 0, codigo_produto = 0, idUser = 0, idEncomenda = 0;
    
    private ClienteEncomendaModelo clienteEncomendaModelo;
    private String percentagem_desconto_global = "";
    //ARMAZEM DE ENCOMENDAS
    private static int id_armzem = 2;
    /**
     * Creates new form EfectuarEncomendasVisao
     */
    public EfectuarEncomendasVisao(int idUser) {

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

        txtQuantidadeEncomendar.setDocument(new PermitirNumeros());
        txtCodigoproduto.setDocument(new PermitirNumeros());
        txtNFactura.setDocument(new PermitirNumeros());
        conexao = BDConexao.getInstancia();
           
           
           
           jdcPrevistaEntrega.setDate( new Date() );
        try {
            txtQuantidadeEncomendar.setText("1");
            txtSaldoCliente.setText("1");
            codigo_produto = 6;
            adicionar_no_carrinho();
            remover_items();
            txtQuantidadeEncomendar.setText("");
            txtSaldoCliente.setText("");
            txtCredito.setText("0");
           
            
        } catch (SQLException ex) {
            Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        this.idUser = idUser;
        txtQuantidadeEncomendar.requestFocus();
     
        getClientes();
        txtIniciasNome.addKeyListener(new TratarEventoTeclado());

        txtCodigoproduto.addKeyListener(new TratarEventoTecladoCodigoProduto());
        
        txtNFactura.addKeyListener(new TratarEventoTecladoCodigoFactura());

       // preencher_dados(new ClienteEncomendaController().getClienteByCodigo(getCodigo(String.valueOf(cmbClientes.getSelectedItem()))));

        
        btnEfectuarEncomenda.setEnabled(false);
        
    }

    public Integer getCodigo(String nome_cliente) {

        codigo_cliente = new ClienteEncomendaController().getCodigoByNome(nome_cliente);
        return codigo_cliente;

    }

    public void getClientes() {
        cmbClientes.setModel(new DefaultComboBoxModel(conexao.getElementos("tb_clientes_encomenda", "nome_cliente", false)));

    }

    public void preencher_dados(ClienteEncomendaModelo clienteEncomendaModelo) {

        txtSaldoCliente.setText( String.valueOf( clienteEncomendaModelo.getSaldo() ) );

    }

    //----------- evento do teclado ---------------------------------------
    class TratarEventoTeclado implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtIniciasNome.getText().trim() + key;

                cmbClientes.setModel(new DefaultComboBoxModel(conexao.getElementosLike("tb_clientes_encomenda", "nome_cliente", prefixo)));

                try {

                    codigo_cliente = getCodigo(String.valueOf(cmbClientes.getSelectedItem()));
                    preencher_dados(new ClienteEncomendaController().getClienteByCodigo(codigo_cliente));

                } catch (Exception ex) {
                    //Logger.getLogger(UsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
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
    
    
     public double getDesconto_produto(double preco_total_produto) throws SQLException{
          
           ResultSet resultado = conexao.executeQuery("SELECT percentagem_desconto , quantidade_desconto FROM tb_produto WHERE codigo = " +getCodigoProduto());
           Integer quantidade = 0;
           double percentagem_desconto = 0;
           
           if(resultado.next())
           {
               quantidade = resultado.getInt("quantidade_desconto");
               percentagem_desconto = resultado.getDouble("percentagem_desconto");
           
           }
     
           System.err.println("QUANTIDADE DESCONTO " +quantidade);
           System.err.println("PERCENTAGEM DESCONTO " +percentagem_desconto);
           System.err.println("TOTAL PRODUTO " +preco_total_produto);
           
          percentagem_desconto_global = String.valueOf( percentagem_desconto );
       
          if(   getQuantidade()  >=  quantidade  )
              return preco_total_produto * (  percentagem_desconto / 100 );
          else return 0.0;
      
      }
      
  
    
     
      public double getDesconto_produto(int codigo_produto, double preco_total_produto, int quantidade_analizar ) throws SQLException{
          
           ResultSet resultado = conexao.executeQuery("SELECT percentagem_desconto , quantidade_desconto FROM tb_produto WHERE codigo = " +codigo_produto);
           Integer quantidade = 0;
           double percentagem_desconto = 0;
           
           if(resultado.next())
           {
               quantidade = resultado.getInt("quantidade_desconto");
               percentagem_desconto = resultado.getDouble("percentagem_desconto");
           
           }
     
           System.err.println("QUANTIDADE DESCONTO " +quantidade);
           System.err.println("PERCENTAGEM DESCONTO " +percentagem_desconto);
           System.err.println("TOTAL PRODUTO " +preco_total_produto);
           
          percentagem_desconto_global = String.valueOf( percentagem_desconto );
       
          if(   quantidade_analizar  >=  quantidade  )
              return preco_total_produto * (  percentagem_desconto / 100 );
          else return 0.0;
      
      }
      
  
    
     
    
    
    public void salvar_encomenda(){
    
        EncomendaModelo encomendaModelo = new EncomendaModelo();
        
        
        // data_encomenda , data_entrega_prevista , idCliente , total_encomenda, status_entrega, idUsuario
        
         encomendaModelo.setData_encomenda( getDataActualComHora());
         encomendaModelo.setData_entrega_prevista(getDataPrevistaEncomenda());
         encomendaModelo.setIdCliente(codigo_cliente);
         encomendaModelo.setTotal_encomenda( getTotalPagar() );
         encomendaModelo.setStatus_entrega( getStatusEntregue() );
         encomendaModelo.setIdUsuario(this.idUser);
         encomendaModelo.setObs(  txtObs.getText() );
         
         new EncomendaController().guardar(encomendaModelo);
    
    }
    
    
    public void salvarItemvenda() throws SQLException {

        ItemEncomendaModelo itemEncomendaModelo = new ItemEncomendaModelo();

        int cod_venda = 0;//getLastCodigo("tb_venda");

        for (int i = 0; i < jTableCarrinho.getRowCount(); i++) {

            itemEncomendaModelo.setIdEncomenda( getLastIdEncomenda() );
            itemEncomendaModelo.setIdPrdouto(Integer.parseInt(String.valueOf(jTableCarrinho.getModel().getValueAt(i, 0))));
            itemEncomendaModelo.setQauntidade(Integer.parseInt(String.valueOf(jTableCarrinho.getModel().getValueAt(i, 2))));
            itemEncomendaModelo.setTotal( Double.parseDouble( String.valueOf(jTableCarrinho.getModel().getValueAt(i, 5) ) ) );

            new ItemEncomendaController().guardar(itemEncomendaModelo);

            System.out.println("Codigo Encomenda: " + itemEncomendaModelo.getIdEncomenda());
            System.out.println("Codigo Produto: " + itemEncomendaModelo.getIdPrdouto());
            System.out.println("Quantidade: " + itemEncomendaModelo.getQauntidade());
            System.out.println("Total: " + itemEncomendaModelo.getTotal());


        }


    }

    public void setEntregue(boolean status){
                if(status)
                        jCBEntregue.setSelected(status);
                else jCBEntregue.setSelected(status);
    }
    
    public boolean getStatusEntregue(){
        return jCBEntregue.isSelected();
    }
    
    
    public void buscar_fatura(Integer idEncomenda) throws SQLException
    {
                    
        EncomendaModelo encomendaModelo = new EncomendaController().getEncomendaByCodigo(idEncomenda);

        ClienteEncomendaModelo clienteEncomendaModelo = new ClienteEncomendaController().getClienteByCodigo( encomendaModelo.getIdCliente() );

        String data_prevista = String.valueOf(encomendaModelo.getData_entrega_prevista());

        //2012-06-26

        int dia = Integer.parseInt(data_prevista.substring(8, 10));;
        int mes = Integer.parseInt(data_prevista.substring(5, 7));;
        int ano = Integer.parseInt(data_prevista.substring(0, 4));

        jdcPrevistaEntrega.setDate(new Date(ano - 1900, mes - 1, dia));
        setEntregue(   encomendaModelo.isStatus_entrega()  );
        //busca todas encomendas
        Vector<ItemEncomendaModelo> vector = new ItemEncomendaController().getAllItemEncomenda(encomendaModelo.getIdEncomenda());

        cmbClientes.setSelectedItem(clienteEncomendaModelo.getNome_cliente());

        busca_cliente();

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
        jTableCarrinho.getColumnModel().getColumn(5);

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

         int quantidade = 0;
        for (int i = 0; i < vector.size(); i++) {

            ItemEncomendaModelo itemEncomendaModelo = vector.elementAt(i);
            
         
            
            modelo.addRow(new Object[]{
                itemEncomendaModelo.getIdPrdouto(),
                new ProdutoController(conexao).getProduto(itemEncomendaModelo.getIdPrdouto()).getDesignacao(),
                itemEncomendaModelo.getQauntidade(),
                new StockController(conexao).getStockProduto(itemEncomendaModelo.getIdPrdouto(), 2).getPreco_venda(),
                getDesconto_produto(itemEncomendaModelo.getIdPrdouto() ,  getPreco() *  itemEncomendaModelo.getQauntidade(),  itemEncomendaModelo.getQauntidade()),
                itemEncomendaModelo.getTotal()
            });

        }

        setTotalBusca();
       


    }
    
    
    
    
    public boolean realizar_encomenda () throws SQLException{
    
        
        // basta encontrar uma quantidade inferior de um produto para nao entregar a encomenda
        for (int i = 0; i <  jTableCarrinho.getRowCount(); i++ )
        {    
            this.codigo_produto =   Integer.parseInt( String.valueOf( jTableCarrinho.getModel().getValueAt(i, 0)  ) );
            
            if ( !validar_estado_produto() ){
                    this.codigo_produto = 0;
                    return  false;
            }
        }
        
        this.codigo_produto = 0;
        return true;
    
    }
    
    
    public void actualizar_quantidade_produto(){
    
        int codigo = 0, quantidade = 0;
        
        for (int i = 0; i <  jTableCarrinho.getRowCount(); i++ )
        {    
           codigo =   Integer.parseInt( String.valueOf( jTableCarrinho.getModel().getValueAt(i, 0)  ) );
           quantidade =  Integer.parseInt( String.valueOf( jTableCarrinho.getModel().getValueAt(i, 2)  ) );
           
           actualizar_quantidade(codigo, quantidade);
        }    
   
    }
    
    
    public void actualizar_factura() throws SQLException{
    
        if(! new EncomendaController().getEncomendaByCodigo(this.idEncomenda).isStatus_entrega() )
        {
           if( realizar_encomenda())
           {
                      if( actualizar_estato_factura() )
                      {
                          if( getStatusEntregue() )
                               actualizar_quantidade_produto();

                           JOptionPane.showMessageDialog(null, "Encomenda actualizada com sucesso!...");

                           jCBEntregue.setSelected(false);
                           new FacturaEncomenda( idEncomenda );


                      }else JOptionPane.showMessageDialog(null, "Erro ao actualzar a encomenda", "ERROR", JOptionPane.ERROR);

           }else JOptionPane.showMessageDialog(null, "Lamentamos a encomenda nao pode ser actualizada!..", "ERROR", JOptionPane.ERROR_MESSAGE);

       }else JOptionPane.showMessageDialog(null, "A encomenda ja foi entregue!..", "AVISO", JOptionPane.WARNING_MESSAGE);
        
    }
   
    
    public boolean actualizar_estato_factura(){
             return conexao.executeUpdate("UPDATE tb_encomenda SET status_entrega = " + getStatusEntregue() +" WHERE  idEncomenda = " +idEncomenda );
    }
    
    
    public int getLastIdEncomenda() {
         
        String sql = "SELECT max(idEncomenda) FROM tb_encomenda";
        
        ResultSet rs = BDConexao.getInstancia().executeQuery(sql);

        try 
        {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }
    
    public int getCodigoProduto() {
        return this.codigo_produto;
    }

    public String getDescricao() throws SQLException {
        return new ProdutoController(conexao).getProduto(getCodigoProduto()).getDesignacao();
    }

    public double getPreco() throws SQLException {
        return new StockController(conexao).getStockProduto( getCodigoProduto(), 2 ).getPreco_venda();
    }

    public double getTotalPagar(){
            return   Double.parseDouble( txtTotalPagar.getText() );  
    }
    public int getQuantidade() {
        return Integer.parseInt(txtQuantidadeEncomendar.getText());
    }

    public double getSaldo_Cliente(){
        return  Double.parseDouble(  txtSaldoCliente.getText() );
    }
   
     public double getSaldo_Restar_Cliente(){
        return  Double.parseDouble(  txtSaldoDescontar.getText() );
    }
    
     public double getCredito_Cliente(){
          return  Double.parseDouble(  txtCredito.getText().trim() );
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
    
    
     public  void actualizar_quantidade(int cod_produto, int quantidade_retirar) {
         
        String sql = "UPDATE tb_stock SET quantidade_existente =  "  + ( getQuantidadeProduto(cod_produto) - quantidade_retirar)     +" WHERE cod_produto_codigo = "   +cod_produto +" AND cod_armazem = " +id_armzem;
        System.out.println("   : "  +quantidade_retirar );
        conexao.executeUpdate(sql);
       
    }
     
    
     public  int getQuantidadeProduto(int cod_produto) {
         
        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = "  +cod_produto +" AND cod_armazem = " + id_armzem; 
        ResultSet rs = BDConexao.getInstancia().executeQuery(sql);

        try 
        {
            if (rs.next()) {
                return rs.getInt("quantidade_existente");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }
    
     
     
     public boolean validar_estado_produto() throws SQLException
     {
         
        int codigo = 0,quantidade = 0 ;
        
        for (int i = 0; i <  jTableCarrinho.getRowCount(); i++ )
        {    
           codigo =   Integer.parseInt( String.valueOf( jTableCarrinho.getModel().getValueAt(i, 0)  ) );
           quantidade =  Integer.parseInt( String.valueOf( jTableCarrinho.getModel().getValueAt(i, 2)  ) );
       
                if( possivel_quantidade(codigo ,quantidade ) )
                { 
                                if(estado_critico())
                                           JOptionPane.showMessageDialog(null, "O produto: "  +new ProdutoController(conexao).getProduto( codigo ).getDesignacao() +" precisa de ser actualizado no stock", "DM_SOFT", JOptionPane.WARNING_MESSAGE  );
                                //adicionar_no_carrinho();
                }else {
                    
                    System.err.println("QUANTIDADE A RETIRAR " +quantidade  );
                    JOptionPane.showMessageDialog(null, "O produto: "  +new ProdutoController(conexao).getProduto( codigo ).getDesignacao() +" nao pode ser entregue para esta quantidade", "DM_SOFT", JOptionPane.ERROR_MESSAGE  );
                    return false;
     
                 }
                
        }
        
         return true;
     
     }
    
     
     public boolean estado_critico() throws SQLException{
                return BDConexao.getQuantidade_minima(getCodigoProduto(), id_armzem ) < BDConexao.getQuantidade_Existente( getCodigoProduto(), id_armzem  ) && BDConexao.getQuantidade_Existente( getCodigoProduto(), id_armzem  )  <= BDConexao.getQuantidade_critica(getCodigoProduto(), id_armzem );
    }
    
    public boolean possivel_quantidade(int codigo_produto, int quantidade) throws SQLException{
        
          System.err.println(BDConexao.getQuantidade_Existente( codigo_produto, id_armzem ) );
          
          int quant_possivel = BDConexao.getQuantidade_Existente( codigo_produto , id_armzem  ) - BDConexao.getQuantidade_minima(codigo_produto, id_armzem );
         
          System.err.println( +quant_possivel +">="   +quantidade );
          
          return quant_possivel >= quantidade;
    }
 
    public String getDataPrevistaEncomenda()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(jdcPrevistaEntrega.getDate());
        
        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-" +
                        (gc.get(GregorianCalendar.MONTH) + 1) + "-" +
                        gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }
    
    
    
    
    public void setTotalPagar() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
        jTableCarrinho.getColumnModel().getColumn(5);

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        double total_pagar = 0, saldo_restante = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_pagar += Double.parseDouble(String.valueOf(modelo.getValueAt(i, 5)) ) - Double.parseDouble(String.valueOf(modelo.getValueAt(i, 4)) );
        }

        txtTotalPagar.setText(String.valueOf(total_pagar));

        
        calculo_saldo( total_pagar );
  
    }
    

    public void setTotalBusca() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
        jTableCarrinho.getColumnModel().getColumn(5);

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        double total_pagar = 0, saldo_restante = 0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            total_pagar += Double.parseDouble(String.valueOf(modelo.getValueAt(i, 5))) - Double.parseDouble(String.valueOf(modelo.getValueAt(i, 4)));
        }

        txtTotalPagar.setText(String.valueOf(total_pagar));


    }

    
    public void calculo_saldo( double total_pagar)
    {
        
        double descontar = 0;
        
        if(getSaldo_Cliente() > 0 )
        {
            descontar = getSaldo_Cliente() - total_pagar;
            if( descontar >  0)
                    txtSaldoDescontar.setText( String.valueOf(descontar) );
            else{
                txtSaldoDescontar.setText( String.valueOf(0) );
                txtCredito.setText( String.valueOf(descontar) );
            }
            
        }else  { 
             
             descontar = getCredito_Cliente() - total_pagar;
             txtSaldoDescontar.setText( String.valueOf(0) );
             txtCredito.setText( String.valueOf(descontar) );
        
        
        }
                
    }
    
    
    public void remover_item_carrinho() {

        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

        modelo.removeRow(jTableCarrinho.getSelectedRow());
         setTotalPagar();
         
        double actualizar_saldo = clienteEncomendaModelo.getCredito() + getTotalPagar();
        txtCredito.setText( String.valueOf( actualizar_saldo ) );
      


    }

    public void adicionar_no_carrinho() throws SQLException {
        
      
        
        jTableCarrinho.getColumnModel().getColumn(0);
        jTableCarrinho.getColumnModel().getColumn(1);
        jTableCarrinho.getColumnModel().getColumn(2);
        jTableCarrinho.getColumnModel().getColumn(3);
        jTableCarrinho.getColumnModel().getColumn(4);
        jTableCarrinho.getColumnModel().getColumn(5);
       

        DefaultTableModel modelo = (DefaultTableModel) jTableCarrinho.getModel();

       double total_deconto = Double.parseDouble(  String.valueOf( getPreco() )   ) * Double.parseDouble(  String.valueOf( getQuantidade() )  ) ;
        
        modelo.addRow(new Object[]{
            getCodigoProduto(),
            getDescricao(),
            getQuantidade(),
            getPreco(),
            getDesconto_produto( getPreco() * getQuantidade() ),
            getPreco() * getQuantidade()
        });


        setTotalPagar();

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
    
    
    
    
    

    //----------- evento do teclado ---------------------------------------
    class TratarEventoTecladoCodigoProduto implements KeyListener {

        String prefixo = "";
        int codigo = 0, codigo_categoria = 0, quatidade_produto = 0;

        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                char key = evt.getKeyChar();

                prefixo = txtCodigoproduto.getText().trim() + key;

                System.out.println("Codigo: " + prefixo);

                codigo_produto = Integer.parseInt(prefixo.trim());

                try {

                    if (!getDescricao().equals("") || !getDescricao().equals(null)) {

                        adicionar_no_carrinho();
                        txtQuantidadeEncomendar.setText("");
                        txtCodigoproduto.setText("");

                        txtQuantidadeEncomendar.requestFocus();

                    } else {
                        JOptionPane.showMessageDialog(null, "Este codigo nao pertence a nenhum produto ");
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Provavelmente o codigo inserido nao \n pertence a nenhum produto");
                    // Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }

    
    public void busca_cliente(){
    
                clienteEncomendaModelo = new ClienteEncomendaController().getClienteByCodigo(getCodigo(String.valueOf(cmbClientes.getSelectedItem())));
         preencher_dados(clienteEncomendaModelo);

        txtSaldoCliente.setText(String.valueOf( clienteEncomendaModelo.getSaldo()));
        txtCredito.setText( String.valueOf( clienteEncomendaModelo.getCredito() ) );
    
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
                       txtSaldoDescontar.setText("");
                    
                } catch (SQLException ex) {
                    
                    Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }catch(Exception e)
                {
                    e.printStackTrace();
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        txtSaldoCliente = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCredito = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSaldoDescontar = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtObs = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtQuantidadeEncomendar = new javax.swing.JTextField();
        txtCodigoproduto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNFactura = new javax.swing.JTextField();
        jCBEntregue = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jdcPrevistaEntrega = new com.toedter.calendar.JDateChooser();
        lbClientes = new javax.swing.JLabel();
        cmbClientes = new javax.swing.JComboBox();
        txtIniciasNome = new javax.swing.JTextField();
        lbInicaisNome = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jpfConfirmar = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCarrinho = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        btnEfectuarEncomenda = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnNovaEncomenda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EFECTUAR ENCOMENDA");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Saldo do Cliente");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Total a Pagar");

        txtTotalPagar.setEditable(false);

        txtSaldoCliente.setEditable(false);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Crédito");

        txtCredito.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Saldo à Restar");

        txtSaldoDescontar.setEditable(false);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setText("OBS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSaldoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                    .addComponent(txtTotalPagar)))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(txtCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSaldoDescontar, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addGap(236, 236, 236))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtObs, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(75, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSaldoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtSaldoDescontar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtObs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Quantidade à Encomendar");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Cod.Produto");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Codigo Factura");

        jCBEntregue.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jCBEntregue.setText("Entregue");
        jCBEntregue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEntregueActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("Data Prevista da Entrega");

        lbClientes.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbClientes.setText("Clientes");

        cmbClientes.setFont(new java.awt.Font("Vrinda", 0, 12)); // NOI18N
        cmbClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClientesActionPerformed(evt);
            }
        });

        lbInicaisNome.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lbInicaisNome.setText("Inicias do nome");

        jButton4.setText("-");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7)))
                        .addGap(18, 18, 18)
                        .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lbInicaisNome, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIniciasNome)
                                    .addComponent(txtQuantidadeEncomendar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbClientes, 0, 215, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(txtCodigoproduto, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jpfConfirmar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCBEntregue))
                                    .addComponent(jdcPrevistaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbInicaisNome)
                    .addComponent(txtIniciasNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbClientes)
                    .addComponent(cmbClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtQuantidadeEncomendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel3)
                        .addComponent(txtCodigoproduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4))
                    .addComponent(jdcPrevistaEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBEntregue)
                    .addComponent(jpfConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTableCarrinho.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jTableCarrinho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.Prod", "Descrição", "Quantidade", "TbPreco", "Desconto (%)", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
                .addComponent(jScrollPane1)
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

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton3.setText("Actualizar ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnEfectuarEncomenda.setBackground(new java.awt.Color(255, 255, 255));
        btnEfectuarEncomenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/encomenda_32x32.png"))); // NOI18N
        btnEfectuarEncomenda.setText("Efectuar Encomenda");
        btnEfectuarEncomenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEfectuarEncomendaActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnNovaEncomenda.setText("Nova Encomenda");
        btnNovaEncomenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaEncomendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNovaEncomenda, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnEfectuarEncomenda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(btnEfectuarEncomenda, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)))
                    .addComponent(btnNovaEncomenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(13, Short.MAX_VALUE))
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

    private void btnEfectuarEncomendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfectuarEncomendaActionPerformed
        // TODO add your handling code here:
       // jCBEntregue.setSelected (false );
      
        try {
            btnEfectuarEncomenda.setEnabled(false);
            btnNovaEncomenda.setEnabled(true);
            
            salvar_encomenda();
            salvarItemvenda(); 
            actualizar_saldo_credito( getSaldo_Restar_Cliente(), getCredito_Cliente() );
            
           
           JOptionPane.showMessageDialog(null, "Encomenda efectuada com sucesso!...");
           
           remover_items();
           jCBEntregue.setSelected (false );
           txtTotalPagar.setText("0");
           txtSaldoCliente.setText("0");
           txtSaldoDescontar.setText("0");
           txtCredito.setText("0");
           txtObs.setText("");
            //chama o relatorio
           new FacturaEncomenda(getLastIdEncomenda());
           
           
         
           
        } catch (SQLException ex) {
            Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnEfectuarEncomendaActionPerformed

    private void jCBEntregueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEntregueActionPerformed
        // TODO add your handling code here:
        if (jCBEntregue.isSelected() == true) {
          
         
            String codigo = jpfConfirmar.getText();
            
            
            if(codigo.equals("DoV90x?#")){
                    jCBEntregue.setSelected(true);
                    jpfConfirmar.setText("");
            }else{
                JOptionPane.showMessageDialog(null, "Lamentamos!... O codigo na corresponde!.. ", "AVISO", JOptionPane.WARNING_MESSAGE);
                jCBEntregue.setSelected(false);
            }
        }
    }//GEN-LAST:event_jCBEntregueActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:

                actualizar_factura();
        } catch (SQLException ex) {
            Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClientesActionPerformed
        // TODO add your handling code here:
        
         clienteEncomendaModelo = new ClienteEncomendaController().getClienteByCodigo(getCodigo(String.valueOf(cmbClientes.getSelectedItem())));
         preencher_dados(clienteEncomendaModelo);

        txtSaldoCliente.setText(String.valueOf( clienteEncomendaModelo.getSaldo()));
        txtCredito.setText( String.valueOf( clienteEncomendaModelo.getCredito() ) );
        
        setTotalPagar();
        
    }//GEN-LAST:event_cmbClientesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:


        try {
            remover_item_carrinho();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se existe produto na tabela seleccione para remover.\n"
                    + "senao ímpossivel remover!...", "AVISO", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnNovaEncomendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaEncomendaActionPerformed
        // TODO add your handling code here:
        
        btnEfectuarEncomenda.setEnabled(true);
        btnNovaEncomenda.setEnabled(false);
    }//GEN-LAST:event_btnNovaEncomendaActionPerformed

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
            java.util.logging.Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EfectuarEncomendasVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EfectuarEncomendasVisao(15).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEfectuarEncomenda;
    private javax.swing.JButton btnNovaEncomenda;
    private javax.swing.JComboBox cmbClientes;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCBEntregue;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCarrinho;
    private com.toedter.calendar.JDateChooser jdcPrevistaEntrega;
    private javax.swing.JPasswordField jpfConfirmar;
    private javax.swing.JLabel lbClientes;
    private javax.swing.JLabel lbInicaisNome;
    private javax.swing.JTextField txtCodigoproduto;
    private javax.swing.JTextField txtCredito;
    private javax.swing.JTextField txtIniciasNome;
    private javax.swing.JTextField txtNFactura;
    private javax.swing.JTextField txtObs;
    private javax.swing.JTextField txtQuantidadeEncomendar;
    private javax.swing.JTextField txtSaldoCliente;
    private javax.swing.JTextField txtSaldoDescontar;
    private javax.swing.JTextField txtTotalPagar;
    // End of variables declaration//GEN-END:variables
}
