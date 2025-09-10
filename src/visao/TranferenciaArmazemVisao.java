/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.ArmazensController;
import comercial.controller.LinhasTransferenciasController;
import comercial.controller.MovimentacaoController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TransferenciasArmazemController;
import comercial.controller.UsuariosController;
import dao.DocumentoDao;
import entity.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import tesouraria.novo.util.AnyReport;
import util.BDConexao;
import util.DVML;
import util.MetodosUtil;
import util.PermitirNumeros;
import static visao.CompraVisao.cmbArmazem;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TranferenciaArmazemVisao extends javax.swing.JFrame implements Runnable
{

    private static ProdutosController produtosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static LinhasTransferenciasController linhasTransferenciasController;
    private static UsuariosController usuariosController;
    private static TransferenciasArmazemController transferenciasArmazemController;
    private static TbStock stock_local;
    private static TbProduto produto;
    private static TbUsuario usuario;
    private static BDConexao conexao, conexaoTransacao;
    private int cod_usuario;
    private static int linha = 0, coordenada = 1, doc_prox_cod = 0;
    private static DefaultListModel lista_model_compras_apuradas = new DefaultListModel();
    private static double total_iliquido = 0, total_desconto_linha = 0;
    public static int linha_actual = -1;
    private Vector<String> lista_armazem = new Vector<>();
    private Vector<String> lista_armazem_origem = new Vector<>();
    private Vector<String> lista_armazem_destino = new Vector<>();

    public TranferenciaArmazemVisao( int cod_usuario, BDConexao conexao )
    {

        initComponents();

        produtosController = new ProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        linhasTransferenciasController = new LinhasTransferenciasController( conexao );
        usuariosController = new UsuariosController( conexao );
        transferenciasArmazemController = new TransferenciasArmazemController( conexao );

        cmbArmazemDestino.setModel( new DefaultComboBoxModel<>( armazensController.getVector() ) );
        cmbArmazemOrigem.setModel( new DefaultComboBoxModel<>( armazensController.getVector() ) );
        setLocationRelativeTo( null );
        setResizable( false );

        txtQtdEntrar.setDocument( new PermitirNumeros() );

        this.cod_usuario = cod_usuario;
        TranferenciaArmazemVisao.conexao = conexao;
        init();
        setWindowsListener();
        try
        {
            getVectorDestino();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private static void mostrar_dados_stock_origem( TbProduto produto_parm )
    {

//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getCodigoArmazem() );
        TbStock stockByCodBarra = stocksController.getStockByCodBarraAndIdArmazem( produto_parm.getCodBarra(), getCodigoArmazemOrigem() );
        if ( !Objects.isNull( stockByCodBarra ) )
        {
            txtQtdExistente.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
        }
        else
        {
            txtQtdExistente.setText( "0" );
        }
    }

    private static void mostrar_dados_stock_destino( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodBarra = stocksController.getStockByCodBarraAndIdArmazem( produto_parm.getCodBarra(), getCodigoArmazemDestino() );
        if ( !Objects.isNull( stockByCodBarra ) )
        {
            txtQtdExistente1.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
        }
        else
        {
            txtQtdExistente1.setText( "0" );
        }
    }

    private static void mostrar_cod_dados_stock_origem( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stocksController.getStockByIdProdutoAndIdArmazem( produto_parm.getCodigo(), getCodigoArmazemOrigem() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtQtdExistente.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtQtdExistente.setText( "0" );
        }
    }

    private static void mostrar_cod_dados_stock_destino( TbProduto produto_parm )
    {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra( produto_parm.getCodBarra(), getIdArmazem() );
        TbStock stockByCodInterno = stocksController.getStockByIdProdutoAndIdArmazem( produto_parm.getCodigo(), getCodigoArmazemDestino() );
        if ( !Objects.isNull( stockByCodInterno ) )
        {
            txtQtdExistente1.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
        }
        else
        {
            txtQtdExistente1.setText( "0" );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jMenuItem1 = new javax.swing.JMenuItem();
        buttonGroup3 = new javax.swing.ButtonGroup();
        produtoPesquisaJDialog = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        txtDesignacao = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_busca = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtCodBarra = new javax.swing.JTextField();
        lb_produto = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lb_data_apuramento5 = new javax.swing.JLabel();
        txtCodigoInterno = new javax.swing.JTextField();
        lbProdutos2 = new javax.swing.JLabel();
        lb_data_apuramento2 = new javax.swing.JLabel();
        txtQtdEntrar = new javax.swing.JTextField();
        cmbArmazemOrigem = new javax.swing.JComboBox();
        lbPreco1 = new javax.swing.JLabel();
        lbPreco2 = new javax.swing.JLabel();
        cmbArmazemDestino = new javax.swing.JComboBox();
        lb_data_apuramento1 = new javax.swing.JLabel();
        txtQtdExistente = new javax.swing.JTextField();
        lb_data_apuramento3 = new javax.swing.JLabel();
        txtQtdExistente1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        lb_usuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        tabela_busca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Designacao", "Categoria", "Qtd"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
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
        tabela_busca.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_buscaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_busca);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtDesignacao, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtDesignacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton6.setText("Sair");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setText("BUSCA DE PRODUTO");

        javax.swing.GroupLayout produtoPesquisaJDialogLayout = new javax.swing.GroupLayout(produtoPesquisaJDialog.getContentPane());
        produtoPesquisaJDialog.getContentPane().setLayout(produtoPesquisaJDialogLayout);
        produtoPesquisaJDialogLayout.setHorizontalGroup(
            produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(produtoPesquisaJDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(produtoPesquisaJDialogLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        produtoPesquisaJDialogLayout.setVerticalGroup(
            produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, produtoPesquisaJDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(produtoPesquisaJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...:::::  TRANFERENCIA DE ARMAZEM ::::...");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.Art", "Designacao", "Origem(-)", "Destino(+)", "Qtd", "Qtd Final(Origem)", "Qtd Final(Destino)"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false
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
        table.setCellSelectionEnabled(true);
        table.setGridColor(new java.awt.Color(51, 153, 0));
        table.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                tablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0)
        {
            table.getColumnModel().getColumn(0).setPreferredWidth(10);
            table.getColumnModel().getColumn(1).setPreferredWidth(250);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 910, 350));

        jPanel6.setBackground(new java.awt.Color(4, 41, 144));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TRANSFERENCIA DE ARMAZEM");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCodBarra.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        txtCodBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodBarraActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodBarra, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 190, -1));

        lb_produto.setFont(new java.awt.Font("SansSerif", 3, 16)); // NOI18N
        lb_produto.setForeground(new java.awt.Color(4, 41, 144));
        lb_produto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_produto.setText("Produto/Artigo");
        lb_produto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));
        jPanel3.add(lb_produto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 470, 30));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 50, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 50, -1));

        lb_data_apuramento5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lb_data_apuramento5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_data_apuramento5.setText("Cod. Barra");
        jPanel3.add(lb_data_apuramento5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, -1));

        txtCodigoInterno.setBackground(new java.awt.Color(4, 41, 144));
        txtCodigoInterno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoInterno.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoInterno.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoInterno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoInternoActionPerformed(evt);
            }
        });
        jPanel3.add(txtCodigoInterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 70, 30));

        lbProdutos2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lbProdutos2.setText("Cód. Interno");
        jPanel3.add(lbProdutos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 100, 20));

        lb_data_apuramento2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lb_data_apuramento2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_apuramento2.setText("Qtd. a Transferir");
        jPanel3.add(lb_data_apuramento2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 120, 20));

        txtQtdEntrar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQtdEntrar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQtdEntrarActionPerformed(evt);
            }
        });
        jPanel3.add(txtQtdEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 150, 30));

        cmbArmazemOrigem.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazemOrigem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazemOrigem.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbArmazemOrigemMouseClicked(evt);
            }
        });
        cmbArmazemOrigem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemOrigemActionPerformed(evt);
            }
        });
        jPanel3.add(cmbArmazemOrigem, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 220, -1));

        lbPreco1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco1.setText("Armazem Origem(-)");
        jPanel3.add(lbPreco1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 170, -1));

        lbPreco2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lbPreco2.setText("Armazem Destino(+)");
        jPanel3.add(lbPreco2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 200, -1));

        cmbArmazemDestino.setBackground(new java.awt.Color(4, 154, 3));
        cmbArmazemDestino.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazemDestino.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmbArmazemDestinoMouseClicked(evt);
            }
        });
        cmbArmazemDestino.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemDestinoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbArmazemDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 30, 240, -1));

        lb_data_apuramento1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lb_data_apuramento1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_apuramento1.setText("Qtd. Existente:");
        jPanel3.add(lb_data_apuramento1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 90, 20));

        txtQtdExistente.setEditable(false);
        txtQtdExistente.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        txtQtdExistente.setForeground(new java.awt.Color(204, 0, 0));
        jPanel3.add(txtQtdExistente, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 110, 50));

        lb_data_apuramento3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lb_data_apuramento3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_data_apuramento3.setText("Qtd. Existente:");
        jPanel3.add(lb_data_apuramento3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 150, 20));

        txtQtdExistente1.setEditable(false);
        txtQtdExistente1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        txtQtdExistente1.setForeground(new java.awt.Color(0, 102, 51));
        jPanel3.add(txtQtdExistente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 70, 110, 50));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 40, 40));

        lb_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        lb_usuario.setText("Conta:");

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_32x32.png"))); // NOI18N
        jButton4.setText("Registrar Transferencia");
        jButton4.setToolTipText("Efectuar Venda");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        btnCancelar.setAlignmentX(0.5F);
        btnCancelar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(182, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lb_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );

        getAccessibleContext().setAccessibleName("...:::::  KITANDA - FACTURAÃO ::::...");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

//    public int getCodigoProduto()
//    {
//        return produtoDao.getProdutoByDescricao( lb_produto.getText().toString() ).getCodigo();
//    }
    public static int getCodigoProduto()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( lb_produto.getText() ).getCodigo();

    }

    public static int getCodigoProduto1()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return produtosController.findByDesignacao( lb_produto.getText() ).getCodigo();

    }

    private void cmbArmazemOrigemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemOrigemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemOrigemActionPerformed
        getVectorDestino();
    }//GEN-LAST:event_cmbArmazemOrigemActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        // TODO add your handling code here:
        remover_item_carrinho();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed

        procedimento_adicionar();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtCodBarraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodBarraActionPerformed
    {//GEN-HEADEREND:event_txtCodBarraActionPerformed
        // TODO add your handling code here:
        busca_produto_by_cod_barra();
    }//GEN-LAST:event_txtCodBarraActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        procedimento_salvar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tablePropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_tablePropertyChange
    {//GEN-HEADEREND:event_tablePropertyChange
        // TODO add your handling code here:
//        try
//        {
//
//            actualizar_valor_tabela( evt );
//        }
//        catch ( Exception e )
//        {
//        }
    }//GEN-LAST:event_tablePropertyChange

    private void txtCodigoInternoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCodigoInternoActionPerformed
    {//GEN-HEADEREND:event_txtCodigoInternoActionPerformed
        // TODO add your handling code here:

        busca_produto_by_cod_interno();

    }//GEN-LAST:event_txtCodigoInternoActionPerformed

    private void tabela_buscaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_buscaMouseClicked
    {//GEN-HEADEREND:event_tabela_buscaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabela_buscaMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cmbArmazemDestinoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemDestinoActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemDestinoActionPerformed
        mostrar_dados_stock_destino( produto );

    }//GEN-LAST:event_cmbArmazemDestinoActionPerformed

    private void cmbArmazemOrigemMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_cmbArmazemOrigemMouseClicked
    {//GEN-HEADEREND:event_cmbArmazemOrigemMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbArmazemOrigemMouseClicked

    private void cmbArmazemDestinoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_cmbArmazemDestinoMouseClicked
    {//GEN-HEADEREND:event_cmbArmazemDestinoMouseClicked
        getVectorOrigem();
    }//GEN-LAST:event_cmbArmazemDestinoMouseClicked

    private void txtQtdEntrarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtQtdEntrarActionPerformed
    {//GEN-HEADEREND:event_txtQtdEntrarActionPerformed
        procedimento_adicionar();
    }//GEN-LAST:event_txtQtdEntrarActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed

        try
        {

            new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazemOrigem(), DVML.JANELA_COMPRA, conexao ).show();
            txtQtdEntrar.requestFocus();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup buttonGroup3;
    public static javax.swing.JComboBox cmbArmazemDestino;
    public static javax.swing.JComboBox cmbArmazemOrigem;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbPreco1;
    private javax.swing.JLabel lbPreco2;
    private javax.swing.JLabel lbProdutos2;
    private static javax.swing.JLabel lb_data_apuramento1;
    private static javax.swing.JLabel lb_data_apuramento2;
    private static javax.swing.JLabel lb_data_apuramento3;
    private static javax.swing.JLabel lb_data_apuramento5;
    public static javax.swing.JLabel lb_produto;
    private javax.swing.JLabel lb_usuario;
    private javax.swing.JDialog produtoPesquisaJDialog;
    private javax.swing.JTable tabela_busca;
    public static javax.swing.JTable table;
    private static javax.swing.JTextField txtCodBarra;
    private static javax.swing.JTextField txtCodigoInterno;
    private javax.swing.JTextField txtDesignacao;
    public static javax.swing.JTextField txtQtdEntrar;
    public static javax.swing.JTextField txtQtdExistente;
    public static javax.swing.JTextField txtQtdExistente1;
    // End of variables declaration//GEN-END:variables

    public static void main( String[] args )
    {
        new TranferenciaArmazemVisao( 15, new BDConexao() ).show();
    }

    @Override
    public void run()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    private void init()
    {

//        lista_armazem = armazemDao.buscaTodos1();
        lista_armazem = armazensController.getVector();

        cmbArmazemOrigem.setModel( new DefaultComboBoxModel( lista_armazem ) );
//        cmbArmazemDestino.setModel( new DefaultComboBoxModel( lista_armazem ) );

        txtCodBarra.requestFocus();
        usuario = (TbUsuario) usuariosController.findById( this.cod_usuario );

    }// </editor-fold>   

    private void setWindowsListener()
    {

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
            }

        } );

    }

    private static boolean salvar_linhas()
    {

        if ( !MetodosUtil.tabela_vazia( table ) )
        {

            return true;
        }
        JOptionPane.showMessageDialog( null, "Adiciona itens na tabela caro usuário", "AVISO", JOptionPane.WARNING_MESSAGE );
        return false;

    }

    private static int getIdArmazem( JComboBox cmBox )
    {
        return armazensController.getArmazemByDesignacao( cmBox.getSelectedItem().toString() ).getCodigo();
    }

    private static void esvaziar_tabela()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount( 0 );
    }

    private static void busca_produto_by_cod_barra()
    {

        String codBarra = txtCodBarra.getText().trim();
        if ( !codBarra.equals( "" ) )
        {
            System.err.println( "codBarra: " + codBarra );
//            Long cod_barra = Long.parseLong ( codBarra );
//            produto = produtoDao.getProdutoByCodigoBarra(codBarra);
            produto = produtosController.findByCodBarra( codBarra );
            if ( produto.getCodigo() != 0 )
            {
                procedimento_actualizar_quantidade();
                lb_produto.setText( produto.getDesignacao() );

            }
            else
            {
                lb_produto.setText( "" );
                txtCodBarra.requestFocus();

            }
            mostrar_dados_stock_origem( produto );
            mostrar_dados_stock_destino( produto );
            txtQtdEntrar.setText( "" );
            txtQtdEntrar.requestFocus();

        }
        else
        {
            lb_produto.setText( "" );
            txtCodBarra.requestFocus();
        }

    }

    private static void busca_produto_by_cod_interno()
    {

        String codInternoString = txtCodigoInterno.getText();
        Integer codigoInternoInt = ( codInternoString.isEmpty() ? 0 : Integer.parseInt( codInternoString ) );

//        produto = produtoDao.getProdutoByCodigoProduto(codigoInternoInt);
        produto = (TbProduto) produtosController.findById( codigoInternoInt );
        procedimento_actualizar_quantidade();
        if ( produto.getCodigo() != 0 )
        {
            lb_produto.setText( produto.getDesignacao() );
            txtQtdEntrar.setText( "" );
            txtQtdEntrar.requestFocus();
        }
        else
        {

            lb_produto.setText( "" );
            txtCodBarra.requestFocus();
        }
        mostrar_cod_dados_stock_origem( produto );
        mostrar_cod_dados_stock_destino( produto );

    }

    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
    {

        try
        {

            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtoDao.findTbProduto( codigo );
            busca_produto_by_cod_interno( codigo );

        }
        catch ( Exception ex )
        {
            Logger.getLogger( VendaUsuarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

//    public static int getCodigoArmazem() {
//        return armazemDao.getArmazemByDescricao(cmbArmazemOrigem.getSelectedItem().toString()).getCodigo();
//    }
    public static int getCodigoArmazemOrigem()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return armazensController.findByDesignacao( cmbArmazemOrigem.getSelectedItem().toString() ).getCodigo();

    }

    public static int getCodigoArmazem1()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return armazensController.findByDesignacao( cmbArmazemOrigem.getSelectedItem().toString() ).getCodigo();

    }

    public static int getCodigoArmazemDestino()
    {
        //return conexao.getCodigoPublico("tb_produto", String.valueOf(  cmbProduto.getSelectedItem()));   
        return armazensController.findByDesignacao( cmbArmazemDestino.getSelectedItem().toString() ).getCodigo();

    }

//    public static int getCodigoArmazem1() {
//        return armazemDao.getArmazemByDescricao(cmbArmazemDestino.getSelectedItem().toString()).getCodigo();
//    }
    private static void procedimento_actualizar_quantidade()
    {

    }

    public static void busca_produto_by_cod_interno( int codProduto )
    {

//        produto = produtoDao.getProdutoByCodigoProduto(codProduto);
        produto = (TbProduto) produtosController.findById( codProduto );

        if ( produto.getCodigo() != 0 )
        {
            lb_produto.setText( produto.getDesignacao() );
            txtCodBarra.setText( produto.getCodBarra() );
            txtCodigoInterno.setText( String.valueOf( produto.getCodigo() ) );
        }
        else
        {
            lb_produto.setText( "" );
            txtCodBarra.setText( "" );
            txtCodigoInterno.setText( "" );
            txtCodBarra.requestFocus();
        }
        mostrar_cod_dados_stock_origem( produto );
        mostrar_cod_dados_stock_destino( produto );

    }

    public void adicionar_produto_a_busca( TbStock stock )
    {

        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();

    }

    public static boolean validar_zero()
    {
        if ( Double.parseDouble( txtQtdEntrar.getText() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a sair não pode ser igual a zero!" );

            return false;
        }
        return true;
    }

    public static boolean validar_zero_preco()
    {

        return true;

    }

    public static boolean validar_zero_qtd_preco()
    {
        if ( Double.parseDouble( txtQtdEntrar.getText() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade a entrar não pode ser igual a zero!" );

            return false;
        }

        return true;

    }

    public static String getUnidade_Produto()
    {
        return produto.getCodUnidade().getAbreviacao();
    }

    public static Integer getQuantidade()
    {
        return (int) Double.parseDouble( txtQtdEntrar.getText() );
    }

    private void procedimento_salvar()
    {

        conexaoTransacao = new BDConexao();
        DocumentoDao.startTransaction( conexaoTransacao );

        TransferenciaArmazem transferenciaArmazem = new TransferenciaArmazem();

        transferenciaArmazem.setDataHora( new Date() );
        transferenciaArmazem.setFkUsuario( cod_usuario );
        transferenciaArmazem.setNomeUsuario( usuario.getNome() );
        try
        {
            transferenciasArmazemController.salvar( transferenciaArmazem );
            registar_linha( transferenciasArmazemController.findLastRow() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, e.getMessage(), "Falha ao efectuar a tranferencia.", JOptionPane.ERROR_MESSAGE );
            DocumentoDao.rollBackTransaction( conexaoTransacao );
            conexaoTransacao.close();

        }

    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.removeRow( table.getSelectedRow() );

    }

    public JFrame getInstance()
    {
        return this;
    }

    private static void procedimento_registrar_stock( TbProduto produto_local, int id_armazem, double qtdCritica, double qtdBaixa )
    {

//        if (!stockDao.exist_produto_stock(produto_local.getCodigo(), id_armazem)) {
        if ( !stocksController.existe_stock( produto_local.getCodigo(), id_armazem ) )
        {

            TbStock stock = new TbStock();
            stock.setDataEntrada( new Date() );
            stock.setQuantidadeExistente( 0d );
            stock.setStatus( "true" );
            stock.setPrecoVenda( new BigDecimal( MetodosUtil.convertToDouble( "0.0" ) ) );
            stock.setPrecoVendaGrosso( new BigDecimal( stock.getPrecoVenda().doubleValue() ) );
            stock.setQtdGrosso( DVML.QTD_DEFAULT );
            stock.setQuantidadeAntiga( 0d );
            stock.setQuantidadeExistente( 0d );
            stock.setCodArmazem( new TbArmazem( id_armazem ) );
            stock.setCodProdutoCodigo( produto_local );
            stock.setQuantCritica( (int) qtdCritica );
            stock.setQuantBaixa( (int) qtdBaixa );
            stocksController.salvar( stock );

        }

    }

//    private void mostrar_dados_stock(TbProduto produto_parm, int id_armazem) {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra(produto_parm.getCodBarra(), id_armazem);
//
//    }
//
//    private static void actualizar_dados_stock(TbProduto produto_parm, int id_armazem) {
//        TbStock stockByCodBarra = stockDao.getStockByCodBarra(produto_parm.getCodBarra(), id_armazem);
//        if (!Objects.isNull(stockByCodBarra)) {
//            try {
//                stockDao.edit(stock_local);
//                System.out.println("stock actualizado");
//            } catch (Exception e) {
//            }
//
//        }
//
//    }
    private void getVectorOrigem()
    {

        String armazem_destino = cmbArmazemDestino.getSelectedItem().toString();

        Vector<String> valor = new Vector<>();

        for ( int i = 0; i < lista_armazem.size(); i++ )
        {
            if ( !lista_armazem.get( i ).equals( armazem_destino ) )
            {
                valor.add( lista_armazem.get( i ) );
            }
        }
        System.out.println( "Origem: " + valor.toString() );
        cmbArmazemOrigem.setModel( new DefaultComboBoxModel( valor ) );

    }

    private void getVectorDestino()
    {

        String armazem_origem = cmbArmazemOrigem.getSelectedItem().toString();

        Vector<String> valor = new Vector<>();

        for ( int i = 0; i < lista_armazem.size(); i++ )
        {
            if ( !lista_armazem.get( i ).equals( armazem_origem ) )
            {
                valor.add( lista_armazem.get( i ) );
            }
        }
        System.out.println( valor.toString() );

        cmbArmazemDestino.setModel( new DefaultComboBoxModel( valor ) );

    }

    private static boolean exist_produto_tabela_formulario( JTable tabela_busca, Integer codigo, int id_armazem_origem, int id_armazem_destino )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();

        if ( modelo.getRowCount() == 0 )
        {
            return false;
        }

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            String valor_origem = modelo.getValueAt( i, 2 ).toString();
            String valor_destino = modelo.getValueAt( i, 3 ).toString();

            int fk_armazem_origem = Integer.parseInt( valor_origem.split( "-" )[ 0 ] );
            int fk_armazem_destino = Integer.parseInt( valor_destino.split( "-" )[ 0 ] );

            if ( modelo.getValueAt( i, 0 ) == codigo && fk_armazem_origem == id_armazem_origem && fk_armazem_destino == id_armazem_destino )
            {
                return true;
            }
        }

        return false;
    }

    private void adicionar_linha()
    {

        double qtdCriticaOrigem = conexao.getQtdCriticaStock( produto.getCodigo(), getIdArmazem( cmbArmazemOrigem ) );
        double qtdBaixaOrigem = conexao.getQtdBaixaStock( produto.getCodigo(), getIdArmazem( cmbArmazemOrigem ) );

        procedimento_registrar_stock( produto, getIdArmazem( cmbArmazemOrigem ), qtdCriticaOrigem, qtdBaixaOrigem ); //origem
        procedimento_registrar_stock( produto, getIdArmazem( cmbArmazemDestino ), qtdCriticaOrigem, qtdBaixaOrigem ); //origem

        double qtd = Double.parseDouble( txtQtdEntrar.getText() );

        double qtd_origem = conexao.getQtdExistenteStock( produto.getCodigo(), getIdArmazem( cmbArmazemOrigem ) ) - qtd;
        double qtd_destino = conexao.getQtdExistenteStock( produto.getCodigo(), getIdArmazem( cmbArmazemDestino ) ) + qtd;

        int idArmazemOrigem = getIdArmazem( cmbArmazemOrigem );
        int idArmazemDestino = getIdArmazem( cmbArmazemDestino );

        if ( !exist_produto_tabela_formulario( table, produto.getCodigo(), idArmazemOrigem, idArmazemDestino ) )
        {
            DefaultTableModel modelo = (DefaultTableModel) table.getModel();
            modelo.addRow( new Object[]
            {
                produto.getCodigo(),
                produto.getDesignacao(),
                getArmazemQtd( getIdArmazem( cmbArmazemOrigem ) ),
                getArmazemQtd( getIdArmazem( cmbArmazemDestino ) ),
                qtd,
                qtd_origem,
                qtd_destino

            } );

        }

        txtQtdEntrar.setText( "" );
        txtCodBarra.setText( "" );
        txtCodBarra.requestFocus();

    }

    private static String getArmazemQtd( int id_armazem )
    {
//        String designacao = armazemDao.findTbArmazem(id_armazem).getDesignacao();
        String designacao = armazensController.findByCodigo( id_armazem ).getDesignacao();
        double qtd = conexao.getQtdExistenteStock( produto.getCodigo(), id_armazem );
        return id_armazem + "-" + designacao.substring( 0, 3 ) + " # (" + qtd + ")";
    }

    private void registar_linha( TransferenciaArmazem transferencia )
    {
        int lastTransferencia = transferenciasArmazemController.getLastTransferencia();

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int idProduto = 0;
        double qtd_before = 0, qtd = 0, qtd_aflter = 0;
        String armazem_origem, armazem_destino = "", valor_origem, valor_destino, produto;
        int fk_armazem_origem = 0, fk_armazem_destino = 0;
        boolean efectuada = true;

        ArmazensController armazensControllerLocal = new ArmazensController( conexaoTransacao );
        LinhasTransferenciasController linhasTransferenciasControllerLocal = new LinhasTransferenciasController( conexaoTransacao );
        StoksController stocksControllerLocal = new StoksController( conexaoTransacao );

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {

            idProduto = Integer.parseInt( modelo.getValueAt( i, 0 ).toString() );
            produto = modelo.getValueAt( i, 1 ).toString();

            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );

            valor_origem = modelo.getValueAt( i, 2 ).toString();
            valor_destino = modelo.getValueAt( i, 3 ).toString();

            fk_armazem_origem = Integer.parseInt( valor_origem.split( "-" )[ 0 ] );
            fk_armazem_destino = Integer.parseInt( valor_destino.split( "-" )[ 0 ] );

            System.out.println( "id_armazem_origem = " + fk_armazem_origem );
            System.out.println( "id_armazem_destino = " + fk_armazem_destino );

//            armazem_origem = armazemDao.findTbArmazem(fk_armazem_origem).getDesignacao();
            armazem_origem = armazensControllerLocal.findByCodigo( fk_armazem_origem ).getDesignacao();
            armazem_destino = armazensControllerLocal.findByCodigo( fk_armazem_destino ).getDesignacao();

            LinhaTransferencia linhaTransferencia = new LinhaTransferencia();
            linhaTransferencia.setQtdBefore( qtd_before );
            linhaTransferencia.setQtd( qtd );
            linhaTransferencia.setQtdAfter( qtd_aflter );

            linhaTransferencia.setFkArmazemOrigem( fk_armazem_origem );
            linhaTransferencia.setArmazemOrigem( armazem_origem );
            linhaTransferencia.setArmazemDestino( armazem_destino );
            linhaTransferencia.setFkArmazemDestino( fk_armazem_destino );
            linhaTransferencia.setFkProduto( idProduto );
            linhaTransferencia.setProduto( produto );
            linhaTransferencia.setFkTransferenciaArmazem( transferencia );

            try
            {
                linhasTransferenciasControllerLocal.salvar( linhaTransferencia );
//                MetodosUtil.subtrai_quantidade( idProduto, qtd, fk_armazem_origem, conexaoTransacao );
//                MetodosUtil.adiciona_quantidade( idProduto, qtd, fk_armazem_destino, conexaoTransacao );

                if ( MovimentacaoController.registrarMovimento(
                        idProduto,
                        fk_armazem_origem,
                        cod_usuario,
                        new BigDecimal( qtd ),
                        "TRANSFERENCIA " + transferencia.getPkTransferenciaArmazem(),
                        "SAIDA",
                        conexao ) )
                {
                    stocksControllerLocal.subtrair_quantidades( idProduto, qtd, fk_armazem_origem );
                }

                if ( MovimentacaoController.registrarMovimento(
                        idProduto,
                        fk_armazem_destino,
                        cod_usuario,
                        new BigDecimal( qtd ),
                        "TRANSFERENCIA " + transferencia.getPkTransferenciaArmazem(),
                        "ENTRADA",
                        conexao
                ) )
                {
                    stocksControllerLocal.adicionar_quantidades( idProduto, qtd, fk_armazem_destino );
                }

            }
            catch ( Exception e )
            {
                efectuada = false;
            }

        }

//        if ( efectuada )
//        {
//            esvaziar_tabela();
//            JOptionPane.showMessageDialog( null, "Transferencia efectuada com sucesso." );
//            HashMap hashMap = new HashMap();
//            System.out.println( "ID_TRANSFERENCIA(1): " + lastTransferencia );
//            System.out.println( "ID_TRANSFERENCIA(2): " + transferencia.getPkTransferenciaArmazem() );
//            hashMap.put( "COD_TRANSFERENCIA", lastTransferencia );
//            txtQtdExistente.setText( "0" );
//            txtQtdExistente1.setText( "0" );
//            DocumentoDao.commitTransaction( conexaoTransacao );
//            new AnyReport( hashMap, "recibo_transferencia_armazem.jasper" );
//            conexaoTransacao.close();
//        }
//        else
//        {
//            DocumentoDao.rollBackTransaction( conexaoTransacao );
//            conexaoTransacao.close();
//        }
        if ( efectuada )
        {
            esvaziar_tabela();
            JOptionPane.showMessageDialog( null, "Transferencia efectuada com sucesso." );
            HashMap hashMap = new HashMap();
            System.out.println( "ID_TRANSFERENCIA(1): " + lastTransferencia );
            System.out.println( "ID_TRANSFERENCIA(2): " + transferencia.getPkTransferenciaArmazem() );
            hashMap.put( "COD_TRANSFERENCIA", lastTransferencia );
            txtQtdExistente.setText( "0" );
            txtQtdExistente1.setText( "0" );
            DocumentoDao.commitTransaction( conexaoTransacao );
            new AnyReport( hashMap, "recibo_transferencia_armazem.jasper" );
            conexaoTransacao.close();
        }
        else
        {
            DocumentoDao.rollBackTransaction( conexaoTransacao );
            conexaoTransacao.close();
        }

    }

    public static boolean possivel_quantidade() throws SQLException
    {

        double quant_possivel = conexao.getQuantidade_Existente_Publico( getCodigoProduto1(), getCodigoArmazemOrigem() ) - conexao.getQuantidade_minima_publico( getCodigoProduto1(), getCodigoArmazemOrigem() );
        //int quant_possivel = stock.getQuantidadeExistente() -  stock.getQuantBaixa();

        return quant_possivel >= getQuantidade();

    }

//    public static void accao_codigo_interno_enter_busca_exterior( int codigo )
//    {
//
//        try
//        {
//
//            //int   codigo = Integer.parseInt(txtCodigoProduto.getText() );
//            TbProduto produto = produtoDao.findTbProduto( codigo );
//            adicionar_linha( produto );
//
//        }
//        catch ( Exception ex )
//        {
//            //  ex.printStackTrace();
//            Logger.getLogger( TranferenciaArmazemVisao.class.getName() ).log( Level.SEVERE, null, ex );
//            JOptionPane.showMessageDialog( null, "Este produto não existe no armazém " + cmbArmazemOrigem.getSelectedItem(), DVML.DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    private void procedimento_adicionar()
    {

        if ( camposValidar() )
        {
            adicionar_linha();
        }

    }

    private boolean camposValidar()
    {

        double qtdExistenteOrigem = conexao.getQtdExistenteStock( produto.getCodigo(), getIdArmazem( cmbArmazemOrigem ) );
        double qtd = Double.parseDouble( txtQtdEntrar.getText() );

        if ( qtd > qtdExistenteOrigem )
        {

            JOptionPane.showMessageDialog( null, "A qtd a transferir nao poder ser maior do que existente", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }

        return true;

    }
}
