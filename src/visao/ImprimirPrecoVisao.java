/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import dao.DadosInstituicaoDao;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.ProdutoImpostoDao;
import dao.StockDao;
import entity.TbProduto;
import entity.TbStock;
import javax.persistence.EntityManagerFactory;
import lista.ImprimirPrecos;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

public class ImprimirPrecoVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static StockDao stockDao = new StockDao( emf );
    private static DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private static PrecoDao precoDao = new PrecoDao( emf );
    private static TbStock stock_local;
    public static ProdutoDao produtoDao;
    private static TbProduto produto;
    private BDConexao conexao;

    public ImprimirPrecoVisao( BDConexao conexao )
    {
        this.conexao = conexao;
        initComponents();
        setLocationRelativeTo( null );
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        lbFactura = new javax.swing.JLabel();
        txtCampo = new javax.swing.JTextField();
        rbCodBarra = new javax.swing.JRadioButton();
        rbCodInterno = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbFactura.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbFactura.setText("Cod Barra");

        txtCampo.setBackground(new java.awt.Color(51, 153, 0));
        txtCampo.setForeground(new java.awt.Color(255, 255, 255));
        txtCampo.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCampo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCampoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCodBarra);
        rbCodBarra.setSelected(true);
        rbCodBarra.setText("CodBarra");

        buttonGroup1.add(rbCodInterno);
        rbCodInterno.setText("CodInterno");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(rbCodBarra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCodInterno))
                    .addComponent(txtCampo, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbCodBarra)
                    .addComponent(rbCodInterno))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFactura)
                    .addComponent(txtCampo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCampoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCampoActionPerformed
        // TODO add your handling code here:        

        procedimento_imprimir();

    }//GEN-LAST:event_txtCampoActionPerformed

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
            java.util.logging.Logger.getLogger( ImprimirPrecoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( ImprimirPrecoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( ImprimirPrecoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( ImprimirPrecoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                ImprimirPrecoVisao dialog = new ImprimirPrecoVisao( new BDConexao() );
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbFactura;
    private static javax.swing.JRadioButton rbCodBarra;
    private static javax.swing.JRadioButton rbCodInterno;
    private static javax.swing.JTextField txtCampo;
    // End of variables declaration//GEN-END:variables
    private void procedimento_imprimir()
    {

        if ( rbCodBarra.isSelected() )
        {
            String cod_barra = txtCampo.getText().trim();
            TbStock stock = stockDao.getStockByCodBarraSemArmazem(cod_barra );
            Double taxa = new ProdutoImpostoDao( emf ).getTaxaByCodBarraProduto( cod_barra );

            double valor_iva = 0;
            valor_iva = ( 1 + ( taxa / 100 ) );
            System.out.println( "TAXA PROD:" + taxa );

            double qtd = precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo() ).getQtdBaixo();

            double preco_grosso = MetodosUtil.retirar_dizimas( precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo()
            ).getPrecoVenda().doubleValue() * valor_iva );

            double preco_retalho = MetodosUtil.retirar_dizimas(
                    precoDao.findTbPreco(
                            precoDao.getUltimoIdPrecoByIdProduto(
                                    stock.getCodProdutoCodigo().getCodigo(),
                                    qtd - 1 )
                    ).getPrecoVenda().doubleValue() * valor_iva );

            String designacao = stock.getCodProdutoCodigo().getDesignacao();
            String dados_instiuicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome();
            int cod_produto = stock.getCodProdutoCodigo().getCodigo();
            new ImprimirPrecos( cod_barra, preco_retalho, preco_grosso, qtd, cod_produto, designacao, dados_instiuicao );

        }
        else
        {

            int cod_produto = Integer.parseInt( txtCampo.getText() );
            TbStock stock = stockDao.get_stock_by_id_produto_and_sem_armazem(cod_produto );
            Double taxa = new ProdutoImpostoDao( emf ).getTaxaByCodInternoProduto( cod_produto );
//            boolean temIva = new ProdutoImpostoDao( emf ).exist( cod_produto );
            double valor_iva = ( 1 + ( taxa / 100 ) );

            double qtd = precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo() ).getQtdBaixo();

            double preco_grosso = MetodosUtil.retirar_dizimas( precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo()
            ).getPrecoVenda().doubleValue() * valor_iva );

            double preco_retalho = MetodosUtil.retirar_dizimas(
                    precoDao.findTbPreco(
                            precoDao.getUltimoIdPrecoByIdProduto(
                                    stock.getCodProdutoCodigo().getCodigo(),
                                    qtd - 1 )
                    ).getPrecoVenda().doubleValue() * valor_iva );

            String designacao = stock.getCodProdutoCodigo().getDesignacao();
            String dados_instiuicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome();
            String cod_barra = String.valueOf( stock.getCodProdutoCodigo().getCodBarra() );
            new ImprimirPrecos( cod_barra, preco_retalho, preco_grosso, qtd, cod_produto, designacao, dados_instiuicao );

        }

        txtCampo.setText( "" );
        txtCampo.requestFocus();

    }

    private static void procedimento_imprimir_lupa()
    {

        if ( rbCodBarra.isSelected() )
        {
            String cod_barra = txtCampo.getText().trim();
            TbStock stock = stockDao.getStockByCodBarraSemArmazem(cod_barra );
            Double taxa = new ProdutoImpostoDao( emf ).getTaxaByCodBarraProduto( cod_barra );

            double valor_iva = 0;
            valor_iva = ( 1 + ( taxa / 100 ) );
            System.out.println( "TAXA PROD:" + taxa );
            double preco_grosso = MetodosUtil.retirar_dizimas( precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo()
            ).getPrecoVenda().doubleValue() );

            double qtd = precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo() ).getQtdBaixo();

            double preco_retalho = MetodosUtil.retirar_dizimas(
                    precoDao.findTbPreco(
                            precoDao.getUltimoIdPrecoByIdProduto(
                                    stock.getCodProdutoCodigo().getCodigo(),
                                    qtd - 1 )
                    ).getPrecoVenda().doubleValue() * valor_iva
            );

            String designacao = stock.getCodProdutoCodigo().getDesignacao();
            String dados_instiuicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome();
            int cod_produto = stock.getCodProdutoCodigo().getCodigo();
            new ImprimirPrecos( cod_barra, preco_retalho, preco_grosso, qtd, cod_produto, designacao, dados_instiuicao );

        }
        else
        {

            int cod_produto = Integer.parseInt( txtCampo.getText() );
            TbStock stock = stockDao.get_stock_by_id_produto_and_sem_armazem(cod_produto );
            Double taxa = new ProdutoImpostoDao( emf ).getTaxaByCodInternoProduto( cod_produto );
            boolean temIva = new ProdutoImpostoDao( emf ).exist( cod_produto );
            double valor_iva = ( 1 + ( taxa / 100 ) );
            double preco_grosso = MetodosUtil.retirar_dizimas( precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo()
            ).getPrecoVenda().doubleValue() );

            double qtd = precoDao.getLastPrecoByIdProduto(
                    stock.getCodProdutoCodigo().getCodigo() ).getQtdBaixo();

            double preco_retalho = MetodosUtil.retirar_dizimas(
                    precoDao.findTbPreco(
                            precoDao.getUltimoIdPrecoByIdProduto(
                                    stock.getCodProdutoCodigo().getCodigo(),
                                    qtd - 1 )
                    ).getPrecoVenda().doubleValue() * valor_iva
            );

            String designacao = stock.getCodProdutoCodigo().getDesignacao();
            String dados_instiuicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNome();

            String cod_barra = String.valueOf( stock.getCodProdutoCodigo().getCodBarra() );
            new ImprimirPrecos( cod_barra, preco_retalho, preco_grosso, qtd, cod_produto, designacao, dados_instiuicao );

        }

        txtCampo.setText( "" );
        txtCampo.requestFocus();

    }

}
