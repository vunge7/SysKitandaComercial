/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import comercial.controller.CaixasController;
import comercial.controller.ClientesController;
import comercial.controller.FormaPagamentoController;
import comercial.controller.FormaPagamentoItemController;
import comercial.controller.ProdutosController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UsuariosController;
import controller.TipoProdutoController;
import dao.AnoEconomicoDao;
import dao.ArmazemDao;
import dao.BancoDao;
import dao.CaixaDao;
import dao.CambioDao;
import dao.ClienteDao;
import dao.DadosInstituicaoDao;
import dao.DocumentoDao;
import dao.FormaPagamentoItemDao;
import dao.ItemVendaDao;
import dao.LugarDao;
import dao.MesasDao;
import dao.MoedaDao;
import dao.PrecoDao;
import dao.ProdutoDao;
import dao.ProdutoImpostoDao;
import dao.ProdutoIsentoDao;
import dao.StockDao;
import dao.UsuarioDao;
import dao.VendaDao;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Contas;
import entity.Documento;
import entity.FormaPagamento;
import entity.FormaPagamentoItem;
import entity.Moeda;
import entity.TbArmazem;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import entity.TbVenda;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import lista.ListaVenda1;
import tesouraria.novo.controller.ContaController;
import tesouraria.novo.controller.ContaMovimentosController;
import tesouraria.novo.util.MetodosUtilTS;
import util.BDConexao;
import util.DVML;
import util.DVML.Abreviacao;
import static util.DVML.DOC_FACTURA_FT;
import static util.DVML.DOC_FACTURA_RECIBO_FR;
import util.FinanceUtils;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;
import static visao.VendaUsuarioVisao.cmbTipoDocumento;

/**
 *
 * @author Domingos Dala Vunge
 */
public class VendaPOSVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static FormaPagamentoItemController formaPagamentoItemController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutoController;
    private static StockDao stockDao;
    public static ProdutoDao produtoDao;
    private static PrecoDao precoDao;
    private static VendaDao vendaDao;
    private static BancoDao bancoDao;
    private static ArmazemDao armazemDao;
    private static DadosInstituicaoDao dadosInstituicaoDao;
    private static UsuarioDao usuarioDao;
    private static ClienteDao clienteDao;
    private static CaixaDao caixaDao;
    private static Cambio cambio;
    private static AnoEconomicoDao anoEconomicoDao;
    private static DocumentoDao documentoDao;
    private static CambioDao cambioDao;
    private static ProdutoImpostoDao produtoImpostoDao;
    private static int linha = 0, linha_existente_produto = -1;
    private static int id_armazem, id_usuario;
    private static BDConexao conexao;
    private static Documento documento;
    private static AnoEconomico anoEconomico;
    private static int doc_prox_cod;
    private static String prox_doc;
    private static Moeda moeda;
    private static MoedaDao moedaDao;
    private static LugarDao lugarDao;
    private static MesasDao mesasDao;
    private static TbVenda venda;
    private static TbItemVenda itemVenda;
    private static ItemVendaDao itemVendaDao;
    private static ProdutoIsentoDao produtoIsentoDao;
    private static TbStock stock_local;
    private static DVML.Abreviacao abreviacao;
    public static double gorjeta = 0;
    private static FormaPagamentoController formaPagamentoController;
    private static UsuariosController usuariosController;
    private static ContaController contaController;
    private static CaixasController caixa_controller;
    private static ClientesController clientesController;
    private static ContaMovimentosController cmc;
//    private static int id_usuario;

    public VendaPOSVisao( BDConexao conexao, int id_armazem, int id_usuario )
    {
        initComponents();
        setLocationRelativeTo( null );
        this.setExtendedState( VendaPOSVisao.MAXIMIZED_BOTH );
        this.id_armazem = id_armazem;
        this.id_usuario = id_usuario;
        this.conexao = conexao;
        txt_qtd.setText( "1" );
        formaPagamentoItemController = new FormaPagamentoItemController( VendaPOSVisao.conexao );
        clientesController = new ClientesController( VendaPOSVisao.conexao );
        produtosController = new ProdutosController( VendaPOSVisao.conexao );
        tipoProdutoController = new TipoProdutosController( VendaPOSVisao.conexao );
        formaPagamentoController = new FormaPagamentoController( VendaPOSVisao.conexao );
        contaController = new ContaController( VendaPOSVisao.conexao );
        usuariosController = new UsuariosController( VendaPOSVisao.conexao );
        caixa_controller = new CaixasController( conexao );

        cmc = new ContaMovimentosController( conexao );
//        dadosInstituicao = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );
        cmbTipoDocumento.setVisible( false );
        setWindowsListener();
        init();
        setDocpadrao( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getDocpadrao() );
        actualizar_moeda();
        actualizar_abreviacao();
        txt_cod_barra.requestFocus();

    }

    private void mostrar_proximo_codigo_documento()
    {
        try
        {
            this.documento = documentoDao.findDocumento( getIdDocumento() );
            this.anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            this.doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + this.anoEconomico.getSerie() + "/" + this.doc_prox_cod;
            lb_proximo_documento.setText( "PRÓX.DOC. :" + prox_doc );
        }
        catch ( Exception e )
        {
            this.documento = null;
            lb_proximo_documento.setText( "" );
        }
    }

    private static String getCodDocActualizador()
    {
        try
        {
            documento = documentoDao.findDocumento( getIdDocumento() );
            anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
            prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    private void mostra_consumidor_final()
    {

        if ( cmbCliente.getSelectedItem().equals( "Consumidor Final" ) )
        {
//            lbClienteConsumidorFinal.setVisible( true );
            txtNomeConsumidorFinal.setVisible( true );
        }
        else
        {
//            lbClienteConsumidorFinal.setVisible( false );
            txtNomeConsumidorFinal.setVisible( false );
        }

    }

//        private static String getCodDocActualizador()
//    {
//        try
//        {
//
//            documento = documentoDao.findDocumento( DVML.DOC_FACTURA_RECIBO_FR );
//            anoEconomico = anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() );
//            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
//            doc_prox_cod = vendaDao.getUltimaContagemByIdDocumentoAndAnoEconomico( getIdDocumento(), getIdAnoEconomico(), conexao ) + 1;
//            prox_doc = documento.getAbreviacao();
//            //FA Série / codigo
//            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
//            return prox_doc;
//        }
//        catch ( Exception e )
//        {
//            return "";
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btn_abertura_dia = new javax.swing.JButton();
        btn_feicho_dia = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();
        lb_armazem = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        dc_data_documento = new com.toedter.calendar.JDateChooser();
        cmbAnoEconomico = new javax.swing.JComboBox<>();
        cmbTipoDocumento = new javax.swing.JComboBox();
        jButton5 = new javax.swing.JButton();
        cmbCliente = new javax.swing.JComboBox<>();
        txtIniciaisCliente = new javax.swing.JTextField();
        txtCodClientePesquisa = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        txt_qtd = new javax.swing.JTextField();
        btn_lupa = new javax.swing.JButton();
        txt_cod_barra = new javax.swing.JTextField();
        btn_remover = new javax.swing.JButton();
        lb_proximo_documento = new javax.swing.JLabel();
        lb_total_geral = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtNomeConsumidorFinal = new javax.swing.JTextField();
        txtNifClientePesquisa = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 551));
        jPanel1.setLayout(new java.awt.GridLayout(5, 1));

        jButton1.setText("G. Mesas");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        btn_abertura_dia.setText("Abrir Cx.");
        btn_abertura_dia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_abertura_diaActionPerformed(evt);
            }
        });
        jPanel1.add(btn_abertura_dia);

        btn_feicho_dia.setText("Fechar Cx.");
        btn_feicho_dia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_feicho_diaActionPerformed(evt);
            }
        });
        jPanel1.add(btn_feicho_dia);

        btnFinalizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btnFinalizar.setText("Faturar");
        btnFinalizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFinalizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnFinalizar);

        lb_armazem.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        lb_armazem.setText("Armazem");
        jPanel1.add(lb_armazem);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setPreferredSize(new java.awt.Dimension(1264, 80));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 76));

        dc_data_documento.setEnabled(false);
        dc_data_documento.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N

        cmbAnoEconomico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAnoEconomico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoEconomicoActionPerformed(evt);
            }
        });

        cmbTipoDocumento.setBackground(new java.awt.Color(4, 154, 3));
        cmbTipoDocumento.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 10)); // NOI18N
        cmbTipoDocumento.setEnabled(false);
        cmbTipoDocumento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbTipoDocumentoActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/usuario.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbClienteActionPerformed(evt);
            }
        });

        txtCodClientePesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodClientePesquisaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(txtCodClientePesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAnoEconomico, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIniciaisCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtIniciaisCliente)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbAnoEconomico)
                    .addComponent(txtCodClientePesquisa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCliente)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(dc_data_documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(100, 76));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jButton4.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton4.setText("Sair");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4);

        jPanel2.add(jPanel5, java.awt.BorderLayout.LINE_START);

        jPanel6.setLayout(new java.awt.GridLayout(1, 6));

        txt_qtd.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        txt_qtd.setText("1");
        txt_qtd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txt_qtdActionPerformed(evt);
            }
        });
        jPanel6.add(txt_qtd);

        btn_lupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        btn_lupa.setToolTipText("click para remover produtos do carrinho");
        btn_lupa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_lupaActionPerformed(evt);
            }
        });
        jPanel6.add(btn_lupa);

        txt_cod_barra.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txt_cod_barra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txt_cod_barraActionPerformed(evt);
            }
        });
        jPanel6.add(txt_cod_barra);

        btn_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        btn_remover.setToolTipText("click para remover produtos do carrinho");
        btn_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_removerActionPerformed(evt);
            }
        });
        jPanel6.add(btn_remover);

        lb_proximo_documento.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_proximo_documento.setText("FR 2021/1");
        jPanel6.add(lb_proximo_documento);

        lb_total_geral.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lb_total_geral.setText("0.0 AOA");
        jPanel6.add(lb_total_geral);

        jPanel7.setLayout(new java.awt.GridLayout(2, 0));
        jPanel7.add(txtNomeConsumidorFinal);
        jPanel7.add(txtNifClientePesquisa);

        jPanel6.add(jPanel7);

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.Barra", "Cod.Int.", "Designacao", "Preco", "Qtd", "Taxa", "Desconto", "Total"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(40);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTable1MouseClicked
    {//GEN-HEADEREND:event_jTable1MouseClicked
        // TODO add your handling code here:
        linha = jTable1.getSelectedRow() + 1;
        accao_quantidade();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTable1KeyReleased
    {//GEN-HEADEREND:event_jTable1KeyReleased
        // TODO add your handling code here:
        int key = evt.getKeyCode();
        if ( key == KeyEvent.VK_ENTER )
        {
            actualizar_linha();

        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void btn_removerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_removerActionPerformed
    {//GEN-HEADEREND:event_btn_removerActionPerformed

        try
        {
            remover_item_carrinho();
        }
        catch ( Exception ex )
        {
            //Logger.getLogger(VendaUsuarioVisao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog( null, "Possivelmente não selecionaste \n nenhuma linha ou não existe dados na tabela", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }//GEN-LAST:event_btn_removerActionPerformed

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFinalizarActionPerformed
    {//GEN-HEADEREND:event_btnFinalizarActionPerformed
        if ( verifica_ano_documento_igual_economico() )
        {
            if ( data_documento_superior_ou_igual_ao_ultimo_doc() )
            {
                if ( MetodosUtil.licencaValidada( conexao ) )
                {
                    new FormaPagamentoVisao( this, rootPaneCheckingEnabled, emf, DVML.VENDA_POS, conexao ).setVisible( true );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( null, "O documento não pode ser processado porque possui uma data inferior ao úlimo documento efectuado", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "A data do documento a ser emitido deve estar no intervalo do ano economico", "Aviso", JOptionPane.WARNING_MESSAGE );
        }
    }//GEN-LAST:event_btnFinalizarActionPerformed

    private void txt_cod_barraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txt_cod_barraActionPerformed
    {//GEN-HEADEREND:event_txt_cod_barraActionPerformed
        // TODO add your handling code here:

        actualizar_linha();
        scrolltable();
    }//GEN-LAST:event_txt_cod_barraActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
//        fazerBackupAgora();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btn_abertura_diaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_abertura_diaActionPerformed
    {//GEN-HEADEREND:event_btn_abertura_diaActionPerformed
        // TODO add your handling code here:
        new CaixaAberturaVisao( id_usuario, conexao, true ).setVisible( true );
    }//GEN-LAST:event_btn_abertura_diaActionPerformed

    private void btn_feicho_diaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_feicho_diaActionPerformed
    {//GEN-HEADEREND:event_btn_feicho_diaActionPerformed
        // TODO add your handling code here:
        new CaixaFechoVisao( id_usuario, conexao, true ).setVisible( true );
    }//GEN-LAST:event_btn_feicho_diaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new PrincipalPedidosVisao( id_usuario, "", id_armazem, conexao ).setVisible( true );
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbAnoEconomicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoEconomicoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoEconomicoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
    }//GEN-LAST:event_cmbAnoEconomicoActionPerformed

    private void cmbTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbTipoDocumentoActionPerformed
    {//GEN-HEADEREND:event_cmbTipoDocumentoActionPerformed
        // TODO add your handling code here:
        mostrar_proximo_codigo_documento();
        actualizar_abreviacao();
//        desabilitar_campos();
//        atualizarCliente1();
    }//GEN-LAST:event_cmbTipoDocumentoActionPerformed

    private void txt_qtdActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txt_qtdActionPerformed
    {//GEN-HEADEREND:event_txt_qtdActionPerformed
        txt_cod_barra.requestFocus();
    }//GEN-LAST:event_txt_qtdActionPerformed

    private void btn_lupaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_lupaActionPerformed
    {//GEN-HEADEREND:event_btn_lupaActionPerformed

        try
        {
//            if ( validar() )
//            {

            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, 2, DVML.JANELA_VENDA_POS, conexao ).show();
//            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_lupaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, conexao ).show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtCodClientePesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodClientePesquisaActionPerformed
    {//GEN-HEADEREND:event_txtCodClientePesquisaActionPerformed
        pesquisa_cliente_by_cod();
    }//GEN-LAST:event_txtCodClientePesquisaActionPerformed

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbClienteActionPerformed
    {//GEN-HEADEREND:event_cmbClienteActionPerformed
        accao_cliente();
        mostra_consumidor_final();
    }//GEN-LAST:event_cmbClienteActionPerformed

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {
            System.out.println( "ID PRODUTO EXTERIOR: " + codigo );
            TbProduto produtoLocal = (TbProduto) produtosController.findById( codigo );
            procedimentoAdicionarTabela( produtoLocal );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger( VendaPOSVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + 2, DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private static void procedimentoAdicionarTabela( TbProduto produto )
    {
        try
        {
            if ( !Objects.isNull( produto ) )
            {

                actualizar_linha( produto );
//                 scrolltable();

                txt_cod_barra.setText( "" );
//                txtCodigoBarra.setText( "" );
                txt_qtd.setText( "1" );
                txt_qtd.requestFocus();

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nao existe produto/servico relacionado a esta referencia" );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

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
            java.util.logging.Logger.getLogger( VendaPOSVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( VendaPOSVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( VendaPOSVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( VendaPOSVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new VendaPOSVisao( BDConexao.getInstancia(), DVML.ARMAZEM_DEFAUTL, 15 ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinalizar;
    private static javax.swing.JButton btn_abertura_dia;
    private static javax.swing.JButton btn_feicho_dia;
    public static javax.swing.JButton btn_lupa;
    public static javax.swing.JButton btn_remover;
    private static javax.swing.JComboBox<String> cmbAnoEconomico;
    public static javax.swing.JComboBox<String> cmbCliente;
    public static javax.swing.JComboBox cmbTipoDocumento;
    private static com.toedter.calendar.JDateChooser dc_data_documento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTable1;
    private javax.swing.JLabel lb_armazem;
    private static javax.swing.JLabel lb_proximo_documento;
    public static javax.swing.JLabel lb_total_geral;
    private static javax.swing.JTextField txtCodClientePesquisa;
    private javax.swing.JTextField txtIniciaisCliente;
    private static javax.swing.JTextField txtNifClientePesquisa;
    private static javax.swing.JTextField txtNomeConsumidorFinal;
    private static javax.swing.JTextField txt_cod_barra;
    private static javax.swing.JTextField txt_qtd;
    // End of variables declaration//GEN-END:variables

    private void init()
    {
        txtIniciaisCliente.addKeyListener( new TratarEventoTeclado() );
        cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVector() ) );
        cmbCliente.setSelectedItem( DVML._CLIENTE_CONSUMIDOR_FINAL );
        produtoDao = new ProdutoDao( emf );
        stockDao = new StockDao( emf );
        precoDao = new PrecoDao( emf );
        produtoImpostoDao = new ProdutoImpostoDao( emf );
        vendaDao = new VendaDao( emf );
        bancoDao = new BancoDao( emf );
        armazemDao = new ArmazemDao( emf );
        usuarioDao = new UsuarioDao( emf );
        clienteDao = new ClienteDao( emf );
        anoEconomicoDao = new AnoEconomicoDao( emf );
        documentoDao = new DocumentoDao( emf );
        cambioDao = new CambioDao( emf );
        moedaDao = new MoedaDao( emf );
        lugarDao = new LugarDao( emf );
        mesasDao = new MesasDao( emf );
        itemVendaDao = new ItemVendaDao( emf );
        produtoIsentoDao = new ProdutoIsentoDao( emf );
        caixaDao = new CaixaDao( emf );
        dadosInstituicaoDao = new DadosInstituicaoDao( emf );
        dc_data_documento.setDate( new Date() );
        cmbTipoDocumento.setModel( new DefaultComboBoxModel( (Vector) documentoDao.buscaTodos() ) );
        cmbAnoEconomico.setModel( new DefaultComboBoxModel( (Vector) anoEconomicoDao.buscaTodos() ) );
        mostrar_ano_economico_serie();
        lb_proximo_documento.setText( "" );

        mostrar_armazem();

    }

    class TratarEventoTeclado implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                try
                {
                    prefixo = txtIniciaisCliente.getText().trim() + key;
                    cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorByIinciais( prefixo ) ) );

                }
                catch ( Exception e )
                {
                    cmbCliente.setSelectedIndex( 0 );
                }

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {
                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    cmbCliente.setModel( new DefaultComboBoxModel( clientesController.getVectorByIinciais( prefixo ) ) );
                }
                catch ( Exception e )
                {
                    cmbCliente.setSelectedIndex( 0 );
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

    private void pesquisa_cliente_by_cod()
    {

        Integer codCliente = Integer.parseInt( txtCodClientePesquisa.getText() );
        try
        {

            TbCliente cliente = (TbCliente) clientesController.findById( codCliente );
            String nome_cliente = cliente.getNome();
            cmbCliente.setSelectedItem( nome_cliente.trim() );
            accao_cliente();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Não existe cliente com código" );
            cmbCliente.setSelectedItem( "Consumidor Final" );
        }
        txtCodClientePesquisa.setText( "" );
        txtCodClientePesquisa.requestFocus();
    }

    private static void setLine()
    {
//        System.out.println( "LINHA: " + linha );
//        System.out.println( "ROW COUNT: " + jTable1.getRowCount() );

        if ( jTable1.getRowCount() > 0 )
        {
            // vefica se a linha esta vazia
            if ( !jTable1.getValueAt( jTable1.getRowCount() - 1, 2 ).toString().equals( "" ) )
            {
                if ( linha == jTable1.getRowCount() )
                {
                    adicionar_linha_em_branco();
                }
                jTable1.clearSelection();
                jTable1.setCellSelectionEnabled( true );
                jTable1.requestFocus();
                jTable1.editCellAt( linha, 0 );
                jTable1.getEditorComponent().requestFocus();
                linha++;
            }

        }
        else
        {
            System.out.println( "CHEGUEI AQUI." );
            adicionar_linha_em_branco();
            jTable1.clearSelection();
            jTable1.setCellSelectionEnabled( true );
            jTable1.requestFocus();
            jTable1.editCellAt( linha, 0 );
            jTable1.getEditorComponent().requestFocus();
            linha++;
        }

    }

    private static void accao_cliente()
    {
        String nomeCliente = (String) cmbCliente.getSelectedItem();

        txtNomeConsumidorFinal.setText( nomeCliente );
        String nif = clientesController.findByNome( nomeCliente ).getNif();
        txtNifClientePesquisa.setText( nif );

//        txtClienteNome.setEditable (  ! clienteDiverso );
//        txtClienteNome.setText ( clienteDiverso ? "Consumidor Final" : nomeCliente );
//        txtClienteNome.setVisible ( cmbCliente.getSelectedItem ().equals ( "DIVERSOS" ) );
//        if ( clienteDiverso )
//        {
//            txtClienteNome.requestFocus ();
//        }
    }

//    private void setCursosLinha( int linha_parm )
//    {
//        try
//        {
//            jTable1.clearSelection();
//            jTable1.setCellSelectionEnabled( true );
//            jTable1.requestFocus();
//            jTable1.editCellAt( linha_parm, 0 );
//            jTable1.getEditorComponent().requestFocus();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
//
//    }
    public static int getIdCliente()
    {
        try
        {
            TbCliente cliente = clientesController.getClienteByNome( cmbCliente.getSelectedItem().toString() );
            return cliente.getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private static void adicionar_linha_em_branco()
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        modelo.addRow( new Object[]
        {
            "",
            "",
            "",
            0d,
            0d,
            0d,
            0d,
            0d

        } );

    }

    private void setDocpadrao( String documentos )
    {
        if ( documentos.equalsIgnoreCase( "Factura/Recibo" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 1 );

        }
        else if ( documentos.equalsIgnoreCase( "Factura" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 2 );

        }
        else if ( documentos.equalsIgnoreCase( "Factura-Proforma" ) )
        {
            cmbTipoDocumento.setSelectedIndex( 3 );

        }
    }

    private static void actualizar_linha()
    {

        String cod_barra = getCodBarra( txt_cod_barra.getText() );
        if ( !cod_barra.trim().equals( "" ) )
        {
//            verifcar_codigo_barra( cod_barra );

            if ( !existProduto( cod_barra ) )
            {
                TbStock stock_local_1 = stockDao.getStockByCodBarra( cod_barra, id_armazem );

                if ( !Objects.isNull( stock_local_1 ) )
                {
                    int cod_interno = stock_local_1.getCodProdutoCodigo().getCodigo();
                    String designacao = stock_local_1.getCodProdutoCodigo().getDesignacao();
                    Double preco_linha = getPreco( cod_interno );
                    double qtd = getQtd( txt_cod_barra.getText() ), desconto = 0d, taxa = 0d, total_linha = 0d;

                    if ( possivel_quantidade( cod_interno, 1 ) && isStocavel( stock_local_1.getCodProdutoCodigo().getStocavel() ) )
                    {
                        if ( estado_critico( cod_interno ) && isStocavel( stock_local_1.getCodProdutoCodigo().getStocavel() ) )
                        {
                            JOptionPane.showMessageDialog( null, "O Produto precisa ser actualizado no stock." );
                        }

                        taxa = getTaxaImposto( cod_interno );
                        total_linha = FinanceUtils.getValorComIVA( qtd, taxa, preco_linha, desconto );

                        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

                        modelo.addRow( new Object[]
                        {
                            cod_barra,
                            cod_interno,
                            designacao,
                            preco_linha,
                            qtd,
                            taxa,
                            desconto,
                            total_linha

                        } );
//                        txt_cod_barra.setText( "" );
//                        txt_cod_barra.requestFocus();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O Produto nao pode ser vendido para esta quantidade" );
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Este produto nao esta registrado neste armazem." );
                }

            }
            else
            {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                int cod_interno = Integer.parseInt( modelo.getValueAt( linha_existente_produto, 1 ).toString() );
                double preco = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 3 ).toString() );
                double qtd = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 4 ).toString() ) + 1;
                double taxa = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 5 ).toString() );
                double desconto = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 6 ).toString() );

                if ( possivel_quantidade( cod_interno, qtd ) )
                {
                    modelo.setValueAt( qtd, linha_existente_produto, 4 );
                    modelo.setValueAt( FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ), linha_existente_produto, 7 );

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O Produto nao pode ser vendido para esta quantidade" );
                }

            }

        }
        else
        {
//            jTable1.clearSelection();
//            jTable1.setCellSelectionEnabled( true );
//            jTable1.requestFocus();
//            jTable1.editCellAt( linha - 1, 0 );
//            jTable1.getEditorComponent().requestFocus();

        }
        setTotalGeral();
        txt_cod_barra.setText( "" );
        txt_cod_barra.requestFocus();

    }

    private static void actualizar_linha( TbProduto produto )
    {

        String cod_barra = produto.getCodBarra();
        if ( !cod_barra.trim().equals( "" ) )
        {
//            verifcar_codigo_barra( cod_barra );

            if ( !existProduto( cod_barra ) )
            {
                TbStock stock_local_1 = stockDao.getStockByCodBarra( cod_barra, id_armazem );

                if ( !Objects.isNull( stock_local_1 ) )
                {
                    int cod_interno = stock_local_1.getCodProdutoCodigo().getCodigo();
                    String designacao = stock_local_1.getCodProdutoCodigo().getDesignacao();
                    Double preco_linha = getPreco( cod_interno );
                    double qtd = getQtd( txt_cod_barra.getText() ), desconto = 0d, taxa = 0d, total_linha = 0d;

                    if ( possivel_quantidade( cod_interno, 1 ) && isStocavel( stock_local_1.getCodProdutoCodigo().getStocavel() ) )
                    {
                        if ( estado_critico( cod_interno ) && isStocavel( stock_local_1.getCodProdutoCodigo().getStocavel() ) )
                        {
                            JOptionPane.showMessageDialog( null, "O Produto precisa ser actualizado no stock." );
                        }

                        taxa = getTaxaImposto( cod_interno );
                        total_linha = FinanceUtils.getValorComIVA( qtd, taxa, preco_linha, desconto );

                        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

                        modelo.addRow( new Object[]
                        {
                            cod_barra,
                            cod_interno,
                            designacao,
                            preco_linha,
                            qtd,
                            taxa,
                            desconto,
                            total_linha

                        } );
//                        txt_cod_barra.setText( "" );
//                        txt_cod_barra.requestFocus();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null, "O Produto nao pode ser vendido para esta quantidade" );
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Este produto nao esta registrado neste armazem." );
                }

            }
            else
            {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                int cod_interno = Integer.parseInt( modelo.getValueAt( linha_existente_produto, 1 ).toString() );
                double preco = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 3 ).toString() );
                double qtd = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 4 ).toString() ) + 1;
                double taxa = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 5 ).toString() );
                double desconto = Double.parseDouble( modelo.getValueAt( linha_existente_produto, 6 ).toString() );

                if ( possivel_quantidade( cod_interno, qtd ) )
                {
                    modelo.setValueAt( qtd, linha_existente_produto, 4 );
                    modelo.setValueAt( FinanceUtils.getValorComIVA( qtd, taxa, preco, desconto ), linha_existente_produto, 7 );

                }
                else
                {
                    JOptionPane.showMessageDialog( null, "O Produto nao pode ser vendido para esta quantidade" );
                }

            }

        }
        else
        {
//            jTable1.clearSelection();
//            jTable1.setCellSelectionEnabled( true );
//            jTable1.requestFocus();
//            jTable1.editCellAt( linha - 1, 0 );
//            jTable1.getEditorComponent().requestFocus();

        }
        setTotalGeral();
        txt_cod_barra.setText( "" );
        txt_cod_barra.requestFocus();

    }

    private void mostrar_ano_economico_serie()
    {
        this.anoEconomico = anoEconomicoDao.getLastObject();
        //lb_ano_academico.setText( "ANO ECONÔMICO: " + this.anoEconomico.getSerie() );

    }

    public static void setTotalGeral()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double total_geral = 0;
        for ( int i = 0; i <= modelo.getRowCount() - 1; i++ )
        {
            total_geral += Double.parseDouble( modelo.getValueAt( i, 7 ).toString() );
        }
        lb_total_geral.setText( CfMethods.formatarComoMoeda( total_geral ) );

    }

    public static boolean existProduto( String cod_barra )
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        if ( modelo.getRowCount() > 0 )
        {
            System.out.println( "Tamanho: " + modelo.getRowCount() );
            for ( int i = 0; i < modelo.getRowCount(); i++ )
            {
                if ( modelo.getValueAt( i, 0 ).toString().equals( cod_barra ) )
                {
                    linha_existente_produto = i;
                    return true;
                }
            }
        }

        linha_existente_produto = -1;
        return false;
    }

    public static Double getPreco( int cod_produto )
    {

        Moeda moeda = getMoeda();
        if ( moeda == null )
        {
            System.out.println( "Moeda Nula." );
            return null;
        }

        Cambio lastCambio = new CambioDao( emf ).getLastObject( moeda.getPkMoeda() );
        try
        {
            Double valorCambio = lastCambio.getValor();
            System.out.println( "Valor Cambio: " + valorCambio );
            double precoVenda = precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( cod_produto, 1d ) ).getPrecoVenda().doubleValue();

            return ( precoVenda / valorCambio );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return 0.0;
        }

    }

    private static Moeda getMoeda()
    {
        return new MoedaDao( emf ).getByDescricao( "Kwanza" );
    }

    public void remover_item_carrinho()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.removeRow( jTable1.getSelectedRow() );
        VendaPOSVisao.setTotalGeral();
    }

    public static boolean estado_critico( int codProduto )
    {
        TbStock stock = stockDao.getStockByDescricao( codProduto, id_armazem );
        double qtd_minima = stock.getQuantBaixa(),
                qtd_existente = stock.getQuantidadeExistente(),
                qtd_critica = stock.getQuantCritica();

        return qtd_minima < qtd_existente
                && qtd_existente <= qtd_critica;

    }

    private void accao_quantidade()
    {
        int linha_selecionada = jTable1.getSelectedRow();
        int coluna_selecionada = jTable1.getSelectedColumn();

//        if ( coluna_selecionada == 4 || coluna_selecionada == 6 )
        if ( coluna_selecionada == 4 )
        {
            new TecladoNumeroPOSVisao( this, rootPaneCheckingEnabled, jTable1, linha_selecionada, coluna_selecionada, DVML.NUMERO_TECLADO_VENDA_POS ).setVisible( true );
        }
    }

    private static double getTaxaImposto( int idProduto )
    {
        return produtoImpostoDao.getTaxaByIdProduto( idProduto );
    }

    public static boolean isStocavel( String status )
    {
        try
        {
            if ( status.equals( "true" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            return true;
        }

    }

    public static boolean possivel_quantidade( int codPrdouto, double qtd )
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( codPrdouto, id_armazem ) - conexao.getQuantidade_minima_publico( codPrdouto, id_armazem );
        return quant_possivel >= qtd;

    }

    private void setCursosLinha( int linha_parm )
    {
        jTable1.addKeyListener( new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyReleased( java.awt.event.KeyEvent evt )
            {
                int key = evt.getKeyCode();
                if ( key == KeyEvent.VK_ENTER )
                {
                    try
                    {
                        jTable1.clearSelection();
                        jTable1.setCellSelectionEnabled( true );
                        jTable1.requestFocus();
                        jTable1.editCellAt( linha_parm, 0 );
                        jTable1.getEditorComponent().requestFocus();
                    }
                    catch ( Exception e )
                    {
                        e.printStackTrace();
                    }

                }
            }
        } );

    }

    public static void salvar_venda()
    {
        Date data_documento = new Date();

        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.
        calendar.add( Calendar.DATE, 15 );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
//        venda_local.setNomeCliente( getNomeCliente() );
//        venda_local.setClienteNif( getClienteNif() );
                venda_local.setNomeCliente( txtNomeConsumidorFinal.getText() );
        venda_local.setClienteNif( txtNifClientePesquisa.getText() );
        //Total Ilíquido
        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        //imposto
//        calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //calculaTotalRetencao();
        venda_local.setTotalRetencao( new BigDecimal( getTotalRetencao() ) );
        //desconto global
        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
        venda_local.setTroco( new BigDecimal( getTroco() ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );

        /*outros campos*/
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda_local.setIdBanco( bancoDao.findTbBanco( 1 ) );
        venda_local.setIdArmazemFK( armazemDao.findTbArmazem( id_armazem ) );
                venda_local.setCodigoUsuario( new TbUsuario( id_usuario ) );
        venda_local.setCodigoCliente( new TbCliente( getIdCliente() ) );
//        venda_local.setCodigoUsuario( usuarioDao.findTbUsuario( id_usuario ) );
//        venda_local.setCodigoCliente( clienteDao.findTbCliente( 1 ) ); //Consomidor final
        venda_local.setFkAnoEconomico( new AnoEconomico( getIdAnoEconomico() ) );
        venda_local.setReferencia( "" );
        venda_local.setNomeConsumidorFinal( txtNomeConsumidorFinal.getText() );
//        venda_local.setNomeConsumidorFinal( DVML._CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setFkDocumento( new Documento( DVML.DOC_FACTURA_RECIBO_FR ) );
        venda_local.setCodFact( prox_doc );
        //this.prox_doc = !vendaDao.existe_codFact( prox_doc, conexao ) ? prox_doc : getCodDocActualizador();
//        venda_local.setCodFact( getCodDocActualizador() );

        venda_local.setHashCod( MetodosUtil.criptografia_hash( prox_doc ) );
//        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexao ) );

        venda_local.setTotalPorExtenso( "Facturamos o valor de " + MetodosUtil.valorPorExtenso( CfMethods.parseMoedaFormatada( lb_total_geral.getText() ), "Kwanza" ) );
        venda_local.setMatricula( "" );
        venda_local.setModelo( "" );
        venda_local.setNumMotor( "" );
        venda_local.setNumChassi( "" );
        venda_local.setKilometro( "" );

        venda_local.setNomeMotorista( "" );
        venda_local.setMarcaCarro( "" );
        venda_local.setCorCarro( "" );
        venda_local.setNDocMotorista( "" );
        System.out.println( "STATUS:hash cod processado." );

        // venda_local.setAssinatura( this.prox_doc );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
//        venda_local.setRefDataFact( CfMethods.fullDateToText( venda_local.getDataVenda() ) );

        //System.out.println( "STATUS:documento assinado com sucesso." );
        if ( cambio.getFkMoeda().getAbreviacao().equals( DVML.MOEDA_KWANZA ) )
        {
            venda_local.setFkCambio( cambio );
        }
        else
        {
            Cambio cambio_nacional = cambioDao.getLastObject( getIdMoeda() );
            venda_local.setFkCambio( cambio_nacional );
        }

        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( gorjeta ) );

        try
        {
            //vendaDao.create( venda_local );
            Integer last_venda = VendaDao.criarVendaComProcedu( venda_local, conexao );
            System.err.println( "last_venda: " + last_venda );
            System.out.println( "STATUS:factura criada com sucesso." );
            // salvarItemvenda();
            if ( last_venda != null )
            {

//                MetodosUtil.adicionar_saldo_banco( venda_local.getTotalVenda(), venda_local.getIdBanco().getIdBanco(), conexao );
                if ( !vendaDao.existeItensVenda( last_venda, conexao ) )
                {
                    salvarItemvenda( last_venda );

                }
                else
                {
                    System.out.println( "ERROR: Já existe venda relacionada." );
                }

            }

//            System.out.println( "STATUS:itens adicionado na facrtura com sucesso." );
        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );
        }

    }

    private static String getNomeCliente()
    {
        return txtNomeConsumidorFinal.getText();
    }

    private static String getClienteNif()
    {
        return txtNifClientePesquisa.getText();
    }

    private static double getTotalIliquido()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double qtd = 0d;
        double total_iliquido = 0d, preco_unitario = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

    private static double getDescontoComercial()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double desconto_comercial = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            desconto_valor_linha = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            desconto_comercial += desconto_valor_linha;
        }

        return desconto_comercial;
    }

    private static double getTotalImposto()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double qtd = 0d;
        double imposto = 0d, preco_unitario = 0d, desconto_valor_linha = 0d;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            desconto_valor_linha = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = (preco_unitario * qtd);
                imposto += ( ( valor_unitario - desconto_valor_linha ) * ( taxa / 100 ) );

            }

        }

        return imposto;
    }

    private static double getTotalRetencao()
    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
        double imposto = 0d;
        return imposto;
    }

    private static double getDescontoFinanceiro()
    {
        double desconto_economico = 0d;
        return desconto_economico;
    }

    private static double getTotalAOALiquido()
    {
        double valores = (getTotalIliquido() + getTotalImposto() + getTotalRetencao());
//        double valores = ( getTotalIliquido() + getTotalImposto()  );
        double descontos = (getDescontoComercial() + getDescontoFinanceiro());
        System.out.println( "TotalIliquido: " + getTotalIliquido() );
        System.out.println( "TotalImposto: " + getTotalImposto() );
        System.out.println( "TotalDescontoComercial: " + getDescontoComercial() );
        System.out.println( "TotalDescontoFinanceiro: " + getDescontoFinanceiro() );
        System.out.println( "Total Liquido: " + ( valores - descontos ) );
        return ( valores - descontos );
    }

    public static double getValor_entregue()
    {
        return FormaPagamentoVisao.get_total_valor().doubleValue();
    }

    public static double getTroco()
    {
        return CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
    }

    private static double getTotalIncidencia()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double qtd = 0;
        double incidencia = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            desconto_valor_linha = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência só é aplicável ao produtos sujeitos a iva 
            if ( taxa != 0 )
            {
                double valor_unitario = (preco_unitario * qtd);
                incidencia += ( ( valor_unitario ) - ( desconto_valor_linha ) );

            }

        }

        return incidencia;
    }

    private static double getTotalIncidenciaIsento()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double qtd = 0d;
        double incidencia_isento = 0d, preco_unitario = 0d, desconto_valor_linha = 0;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            preco_unitario = Double.parseDouble( modelo.getValueAt( i, 3 ).toString() );
            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
            double taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            desconto_valor_linha = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
            // a incidência também é aplicável à produtos isentos do iva 
            if ( taxa == 0 )
            {
                double valor_unitario = (preco_unitario * qtd);
                incidencia_isento += ( ( valor_unitario ) - ( desconto_valor_linha ) );

            }

        }

        return incidencia_isento;
    }

//    public static int getIdAnoEconomico()
//    {
//        try
//        {
//            return anoEconomicoDao.getLastAnoEconomico();
//        }
//        catch ( Exception e )
//        {
//            return 0;
//        }
//    }
    public static int getIdAnoEconomico()
    {
        try
        {
            return anoEconomicoDao.getIdByDescricao( cmbAnoEconomico.getSelectedItem().toString() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

//    public static int getIdDocumento()
//    {
//        try
//        {
//            return DVML.DOC_FACTURA_RECIBO_FR;
//        }
//        catch ( Exception e )
//        {
//            return 0;
//        }
//    }
    public static int getIdDocumento()
    {
        try
        {
            return documentoDao.getIdByDescricao( cmbTipoDocumento.getSelectedItem().toString() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    private static int getIdMoeda()
    {
        return DVML.MOEDA_NACIONAL;
    }

    private void mostrar_abreviacao_moeda_cambio()
    {
        try
        {
            this.moeda = moedaDao.findMoeda( getIdMoeda() );
            this.cambio = cambioDao.getLastObject( getIdMoeda() );
//            lb_cambio.setText( "CAMBIO: " + String.valueOf( this.cambio.getValor() ) + " " + this.moeda.getAbreviacao() );

        }
        catch ( Exception e )
        {
            this.cambio = null;
            e.printStackTrace();
//            4lb_cambio.setText( "" );
        }
    }

    private void actualizar_moeda()
    {
        CfMethods.MOEDA = getMoeda().getAbreviacao();
        mostrar_abreviacao_moeda_cambio();
    }

    private void setWindowsListener()
    {

        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                mostrar_proximo_codigo_documento();

                MetodosUtil.verificarCaixa(
                        caixa_controller,
                        id_usuario,
                        btn_abertura_dia,
                        btn_feicho_dia,
                        btnFinalizar );
            }

        } );

    }

    public static void salvarItemvenda( Integer cod_venda )
    {

        //int cod_venda = vendaDao.getLastVenda();
        int last_cod = cod_venda;
        boolean efectuada = true;
        venda = vendaDao.findTbVenda( last_cod );

        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            try
            {

                itemVenda = new TbItemVenda();
                System.out.println( "CODIGO PRODUTO : " + jTable1.getModel().getValueAt( i, 1 ).toString() );
                itemVenda.setCodigoProduto( produtoDao.findTbProduto( Integer.parseInt( String.valueOf( jTable1.getModel().getValueAt( i, 1 ) ) ) ) );
                itemVenda.setCodigoVenda( venda );
                itemVenda.setQuantidade( Double.parseDouble( String.valueOf( jTable1.getModel().getValueAt( i, 4 ) ) ) );
                itemVenda.setValorIva( Double.parseDouble( String.valueOf( jTable1.getModel().getValueAt( i, 5 ) ) ) );
                itemVenda.setDesconto( Double.parseDouble( String.valueOf( jTable1.getModel().getValueAt( i, 6 ) ) ) );
                itemVenda.setValorRetencao( 0d );
                itemVenda.setMotivoIsensao( getMotivoIsensao( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setCodigoIsensao( MetodosUtil.getCodigoRegime( itemVenda.getCodigoProduto().getCodigo() ) );
                itemVenda.setTotal( new BigDecimal( Double.parseDouble( String.valueOf( jTable1.getModel().getValueAt( i, 7 ) ) ) ) );
                itemVenda.setFkPreco( precoDao.findTbPreco( precoDao.getUltimoIdPrecoByIdProduto( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() ) ) );
                itemVenda.setDataServico( new Date() );
                /*setando a mesa e lugar para cunprir a formalidade só aplica-se somente para resstauração*/
                itemVenda.setFkLugares( lugarDao.findTbLugares( DVML.LUGAR_BALCAO ) );
                itemVenda.setFkMesas( mesasDao.findTbMesas( DVML.MESA_BALCAO ) );

                //cria o item venda
                //itemVendaDao.create( itemVenda );
                itemVendaDao.criarComProcedimentos( itemVenda, conexao );
                stock_local = stockDao.get_stock_by_id_produto_and_id_armazem( itemVenda.getCodigoProduto().getCodigo(), id_armazem );

                if ( getIdDocumento() == DOC_FACTURA_RECIBO_FR || getIdDocumento() == DOC_FACTURA_FT )
                {
                    System.out.println( "passei quando é FR ou FT" );
                    //so retirar caso existir mesmo no armazém em questão.
                    if ( stock_local.getCodigo() != 0 && itemVenda.getCodigoProduto().getStocavel().equals( "true" ) )
                    {
                        actualizar_quantidade( itemVenda.getCodigoProduto().getCodigo(), itemVenda.getQuantidade() );
                    }
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                efectuada = false;
                JOptionPane.showMessageDialog( null, "Falha ao registrar o produto: " + itemVenda.getCodigoProduto().getCodigo() + " na Factura" );
                break;
            }
        }

        if ( efectuada )
        {
            registrar_forma_pagamento( last_cod );
            JOptionPane.showMessageDialog( null, "Factura efectuada com sucesso!.." );

            List<TbProduto> lista_produto_isentos = new ArrayList<>();
            lista_produto_isentos = getProdutosIsentos();
            String motivos_isentos = MetodosUtil.getMotivoIsensaoProdutos( lista_produto_isentos );
            System.err.println( "MOTIVOS: " + motivos_isentos );
            try
            {

                remover_all_produto();
                //adicionar_preco_quantidade_anitgo();

            }
            catch ( Exception e )
            {
            }
            gorjeta = 0;
            // actualizar_cod_doc();
//            txtClienteNome.setText ( "" );
            // txtClienteNome.requestFocus();
            //Chama a factura e imprime directamente para a imprissora que estiver devenidade no sistema operativo
            //ListaVenda1 triplicado = new ListaVenda1( cod_venda, this.abreviacao, false, ck_simplificada.isSelected(), "Triplicado", motivos_isentos );
            //ListaVenda1 duplicado = new ListaVenda1( cod_venda, this.abreviacao, false, ck_simplificada.isSelected(), "Duplicado", motivos_isentos );
            System.out.println( "Passou Actualizar codFac" );
//            ListaVenda1 duplicado = new ListaVenda1( last_cod, this.abreviacao, false, ck_simplificada.isSelected(), "Duplicado", motivos_isentos );

            txt_cod_barra.setText( "" );
            txtNomeConsumidorFinal.setText( "" );
            txt_cod_barra.requestFocus();
            lb_total_geral.setText( CfMethods.formatarComoMoeda( 0d ) );
//            lb_total_geral2.setText( CfMethods.formatarComoMoeda( 0d ) );
            for ( int i = 1; i <= getNumeroVias(); i++ )
            {

                switch (i)
                {
                    case 1:
                        ListaVenda1 original = new ListaVenda1( last_cod, abreviacao, false, true, "Original", motivos_isentos, "" );
                        break;
                    case 2:
                        ListaVenda1 original_duplicado = new ListaVenda1( last_cod, abreviacao, false, true, "Duplicado", motivos_isentos, "" );
                        break;
                    case 3:
                        ListaVenda1 original_triplicado = new ListaVenda1( last_cod, abreviacao, false, true, "Triplicado", motivos_isentos, "" );
                        break;
                }

            }

        }

    }

    private static String getMotivoIsensao( int idProduto )
    {
        return produtoIsentoDao.getRegimeIsensaoByIdProduto( idProduto );
    }

    public static void actualizar_quantidade( int cod, double quantidade )
    {

        System.err.println( "Entrei no actualizar quantidade" );
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) - quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND  cod_armazem = " + id_armazem;
        System.out.println( "Quantidade   : " + quantidade );
        conexao.executeUpdate( sql );

    }

    public static double getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + id_armazem;

        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getDouble( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

    private static List<TbProduto> getProdutosIsentos()
    {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        double taxa = 0.0;
        int codigo_produto = 0;
        List<TbProduto> lista_produtos_isentos = new ArrayList<>();
        for ( int i = 0; i < modelo.getRowCount() - 1; i++ )
        {
            codigo_produto = Integer.parseInt( modelo.getValueAt( i, 1 ).toString() );
            taxa = Double.parseDouble( modelo.getValueAt( i, 5 ).toString() );
            if ( taxa == 0.0 )
            {
                lista_produtos_isentos.add( produtoDao.findTbProduto( codigo_produto ) );
            }
        }

        return lista_produtos_isentos;

    }

    public static void remover_all_produto()
    {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        for ( int i = modelo.getRowCount() - 1; i >= 0; i-- )
        {
            modelo.removeRow( i );
        }

    }

    private void actualizar_abreviacao()
    {

        switch (getIdDocumento())
        {
            case DVML.DOC_FACTURA_RECIBO_FR:
                this.abreviacao = Abreviacao.FR_A6;
                break;

            default:
                break;
        }

    }

    private static int getNumeroVias()
    {
        return dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getNumeroVias();

    }

    private void mostrar_armazem()
    {
        TbArmazem armazem = armazemDao.findTbArmazem( id_armazem );

        if ( !Objects.isNull( armazem ) )
        {
            lb_armazem.setText( armazem.getDesignacao() );
        }
    }

    private static double getQtd( String cod_barra )
    {
        int tamanho = cod_barra.length();
        double qtd = Double.parseDouble( txt_qtd.getText() );
//        double qtd = 1d;
        if ( tamanho == 13 )
        {
            int cod_flag; // 0 - 1
            int cod_produto; // 2 - 7
            double peso; // 8 - 11
            double preco; // 

            cod_flag = Integer.parseInt( cod_barra.substring( 0, 2 ) );

            if ( cod_flag == 28 ) // se trata de um produto na balanca
            {
                cod_produto = Integer.parseInt( cod_barra.substring( 2, 8 ) );
                peso = Double.parseDouble( cod_barra.substring( 8, 12 ) );
                qtd = ( peso / 1000 ); // que e a quantidade do produto

            }

        }
        return qtd;

    }

    private static String getCodBarra( String text )
    {
        //2810121002883
        int tamanho = text.length();
        System.out.println( "Tamanho: " + tamanho );
        if ( tamanho == 13 )
        {

            int cod_flag = Integer.parseInt( text.substring( 0, 2 ) );
            System.out.println( "Cod Flag: " + cod_flag );

            if ( cod_flag == 28 ) // se trata de um produto na balanca
            {
                System.out.println( "Cod Barra produto: " + text.substring( 2, 8 ) );
                return text.substring( 2, 8 );
//                peso = Double.parseDouble( cod_barra.substring( 8, 11 ) );
//                qtd = ( peso / 1000 ); // que e a quantidade do produto

            }

        }
        return text;
    }

    private boolean verifica_ano_documento_igual_economico()
    {
        int ano_economico = Integer.parseInt( anoEconomicoDao.findAnoEconomico( getIdAnoEconomico() ).getDesignacao() );
        int ano_documento = dc_data_documento.getDate().getYear() + 1900;
        return ano_documento == ano_economico;

    }

    private static boolean data_documento_superior_ou_igual_ao_ultimo_doc()
    {
        //buscando o id do documento.
        int pk_documento = getIdDocumento();
        //buscando o id do ano ecoonomico.
        int pk_ano_economico = getIdAnoEconomico();

        //busca o último documento da série em questão.
        // Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento );
        Integer cod_ultima_venda = vendaDao.getLastVenda( pk_documento, pk_ano_economico );
        if ( cod_ultima_venda != 0 )
        {

            //busca o objecto para retirar apenas a data do seu procesamento
            TbVenda venda_local = vendaDao.findTbVenda( cod_ultima_venda );
            //retirando a data do documebto
            Date data_ultimo_documento = venda_local.getDataVenda();
            //pegando a data do documento (data actual do sistema)
            Date data_actual = dc_data_documento.getDate();
            return MetodosUtil.maior_data_1_data_2( data_actual, data_ultimo_documento )
                    || MetodosUtil.igual_data_1_data_2( data_actual, data_ultimo_documento );

        }
        else
        {
            return true;
        }

    }

    public static boolean registrar_forma_pagamento( int id_venda )
    {

        DefaultTableModel modelo = (DefaultTableModel) FormaPagamentoVisao.tabela_forma_pagamento.getModel();

        FormaPagamentoItem formaPagamentoItem;
        Contas contas;
        double troco = CfMethods.parseMoedaFormatada( FormaPagamentoVisao.lb_troco.getText() );
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            formaPagamentoItem = new FormaPagamentoItem();
            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            FormaPagamento formaPagamento = formaPagamentoController.findByDescrisao( modelo.getValueAt( i, 1 ).toString() );
            contas = (Contas) contaController.findById( formaPagamento.getFkContaAssociada() );

            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
//            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";

            formaPagamentoItem.setValor( new BigDecimal( valor ) );
            formaPagamentoItem.setReferencia( referencia );
            formaPagamentoItem.setTroco( new BigDecimal( troco ) );
            formaPagamentoItem.setValor_real(
                    formaPagamentoItem.getValor().subtract( formaPagamentoItem.getTroco() ) );
            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );

            try
            {
                if ( !valor.equals( "0" ) )
                {
                    formaPagamentoItemController.salvar( formaPagamentoItem );

                    if ( Objects.nonNull( contas ) )
                    {
                        MetodosUtilTS.entradaTesouraria( contas,
                                lb_proximo_documento.getText(),
                                formaPagamento,
                                referencia,
                                new BigDecimal( valor ),
                                id_usuario,
                                usuariosController,
                                cmc,
                                conexao
                        );
                    }
                    troco = 0;

                }
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        return true;
    }

//    public static boolean registrar_forma_pagamento( int id_venda )
//    {
//
//        DefaultTableModel modelo = ( DefaultTableModel ) FormaPagamentoVisao.tabela_forma_pagamento.getModel();
//
//        FormaPagamentoItem formaPagamentoItem;
//
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//
//            formaPagamentoItem = new FormaPagamentoItem();
//            Integer id_forma_pagamento = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
//            String referencia = ( modelo.getValueAt( i, 2 ) != null ) ? modelo.getValueAt( i, 2 ).toString() : "n/a";
////            String valor = ( modelo.getValueAt( i, 3 ) != null ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//            String valor = ( !modelo.getValueAt( i, 3 ).toString().equals( "" ) ) ? modelo.getValueAt( i, 3 ).toString() : "0";
//
//            formaPagamentoItem.setValor( new BigDecimal( valor ) );
//            formaPagamentoItem.setReferencia( referencia );
//            formaPagamentoItem.setFkVenda( new TbVenda( id_venda ) );
//            formaPagamentoItem.setFkFormaPagamento( new FormaPagamento( id_forma_pagamento ) );
//
//            try
//            {
//
//                if ( !valor.equals( "0" ) )
//                {
//                    formaPagamentoItemController.salvar( formaPagamentoItem );
////                    FormaPagamentoItemDao.insert( formaPagamentoItem, conexao );
//                }
//
//            }
//            catch ( Exception e )
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }
    public static void fazerBackupAgora()
    {
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

    public void scrolltable()
    {

        jTable1.scrollRectToVisible( jTable1.getCellRect( jTable1.getRowCount() - 1, jTable1.getColumnCount(), true ) );

    }

}
