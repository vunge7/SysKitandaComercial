/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import controller.TipoProdutoController;
import dao.ArmazemDao;
import dao.EntradaDao;
import dao.ProdutoDao;
import dao.StockDao;
import dao.TipoProdutoDao;
import dao.UsuarioDao;
import entity.TbEntrada;
import entity.TbStock;
import exemplos.PermitirNumeros;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

public class EntradaVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static ProdutoDao produtoDao = new ProdutoDao( emf );
    private EntradaDao entradaDao = new EntradaDao( emf );
    private static StockDao stockDao = new StockDao( emf );
    private TipoProdutoDao tipoProdutoDao = new TipoProdutoDao( emf );
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private TbEntrada entrada;
    private static BDConexao conexao;
    private String armazem = "";
    private int codigo = 0;
    private Frame parent;
    private boolean stocavel = true;
    private int idUser;
    private static int id_armzem = 1;
    private int linha_actual = -1;
    private String cod_barra, cod_produto;
    // private OperacaoSistemaUtil osu =  new OperacaoSistemaUtil();

    public EntradaVisao( java.awt.Frame parent, boolean modal, int idUser, String cod_barra, BDConexao conexao ) throws SQLException
    {

        //super(parent, modal);
        initComponents();
        setLocationRelativeTo( null );
        this.conexao = conexao;
        this.idUser = idUser;
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );
//        DVML.activar_cmb_armazem(cmbArmazem);
        cmbCategoria.setModel( new DefaultComboBoxModel( tipoProdutoDao.getAllCategoria() ) );
        cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtQuantidadeEntrar.setDocument( new PermitirNumeros() );
        txtCodigoBarra.setText( cod_barra );
        accao_combo_categoria();
        lbFornecedor.setVisible( false );
        MetodosUtil.FUNCAO_F1( this, modal, getCodigoArmazem(), DVML.JANELA_ENTRADA );
    }

    public EntradaVisao( java.awt.Frame parent, boolean modal, int idUser, String codigo, BDConexao conexao, String armazem, int chamada )
    {

        //super(parent, modal);
        initComponents();
        setLocationRelativeTo( null );
        this.conexao = conexao;
        this.idUser = idUser;
        this.cod_produto = codigo;

        cmbCategoria.setModel( new DefaultComboBoxModel( tipoProdutoDao.getAllCategoria() ) );
        cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getAllDesingnacaoProduto() ) );
        if ( chamada == 1 )
        {
            txtCodigoBarra.setText( codigo );
            cmbCategoria.setSelectedItem( produtoDao.getProdutoByCodigoBarra( codigo  ).getCodTipoProduto().getDesignacao() );
            cmbProduto.setSelectedItem( produtoDao.getProdutoByCodigoBarra(  codigo  ).getDesignacao() );
            accao_enter_cod_barra();
            txtQuantidadeEntrar.requestFocus();
        }
        else
        {
            System.out.println( "CHEGUEI AQUI 1111111" );
            //System.out.println("CODIGO: ++++++++ = " +cod_produto);
            //txtCodigoProduto.setText("22222");
            cmbCategoria.setSelectedItem( produtoDao.getProdutoByCodigoProduto( Integer.parseInt( codigo ) ).getCodTipoProduto().getDesignacao() );
            cmbProduto.setSelectedItem( produtoDao.getProdutoByCodigoProduto( Integer.parseInt( codigo ) ).getDesignacao() );

            txtQuantidadeEntrar.requestFocus();

        }
        cmbArmazem.setModel( new DefaultComboBoxModel( armazemDao.buscaTodos1() ) );

        txtQuatidadeExistente.setText( String.valueOf( conexao.getQtdExistenteStock( getCodigoProduto(), getIdArmazem() ) ) );
        txtCodigoProduto.setDocument( new PermitirNumeros() );
        txtQuantidadeEntrar.setDocument( new PermitirNumeros() );

        MetodosUtil.FUNCAO_F1( this, modal, getCodigoArmazem(), DVML.JANELA_ENTRADA );

    }

    public void confiLabel()
    {

        lbArmazem.setHorizontalAlignment( JLabel.RIGHT );
        lbCodigoProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbCategoria.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidadeExistente.setHorizontalAlignment( JLabel.RIGHT );
        lbProduto.setHorizontalAlignment( JLabel.RIGHT );
        lbQuantidadeEntrar.setHorizontalAlignment( JLabel.RIGHT );

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btn_salvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbArmazem = new javax.swing.JLabel();
        lbCodigoProduto = new javax.swing.JLabel();
        lbCategoria = new javax.swing.JLabel();
        lbProduto = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        txtCodigoProduto = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox();
        cmbProduto = new javax.swing.JComboBox();
        txtQuatidadeExistente = new javax.swing.JTextField();
        lbQuantidadeExistente = new javax.swing.JLabel();
        lbQuantidadeEntrar = new javax.swing.JLabel();
        txtQuantidadeEntrar = new javax.swing.JTextField();
        btn_adicionar = new javax.swing.JButton();
        btn_remover = new javax.swing.JButton();
        txtCodigoBarra = new javax.swing.JTextField();
        lbCategoria2 = new javax.swing.JLabel();
        txtCodigoManual = new javax.swing.JTextField();
        lbCategoria3 = new javax.swing.JLabel();
        lbFornecedor = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        status_mensagem_primaria = new javax.swing.JLabel();
        status_mensagem_secundaria = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: DVML- ENTRADA_PRODUTO ::::...");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btn_salvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/salvar_16x16.png"))); // NOI18N
        btn_salvar.setText("Efectuar Entrada");
        btn_salvar.setAlignmentX(0.5F);
        btn_salvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_salvarActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_salvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 13), new java.awt.Color(51, 153, 0))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Showcard Gothic", 0, 24)); // NOI18N

        lbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbArmazem.setText("Escolha o Ármazem");

        lbCodigoProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCodigoProduto.setText("Codigo Produto");

        lbCategoria.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria.setText("Categoria:");
        lbCategoria.setEnabled(false);

        lbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbProduto.setText("Designação:");

        cmbArmazem.setBackground(new java.awt.Color(51, 153, 0));
        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbArmazem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Activo", "Desactivo" }));
        cmbArmazem.setEnabled(false);
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        txtCodigoProduto.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoProduto.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoProdutoActionPerformed(evt);
            }
        });

        cmbCategoria.setBackground(new java.awt.Color(51, 153, 0));
        cmbCategoria.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbCategoria.setEnabled(false);
        cmbCategoria.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbCategoriaActionPerformed(evt);
            }
        });

        cmbProduto.setBackground(new java.awt.Color(51, 153, 0));
        cmbProduto.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        cmbProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbProdutoActionPerformed(evt);
            }
        });

        txtQuatidadeExistente.setEditable(false);
        txtQuatidadeExistente.setBackground(new java.awt.Color(51, 153, 0));
        txtQuatidadeExistente.setForeground(new java.awt.Color(255, 255, 255));
        txtQuatidadeExistente.setCaretColor(new java.awt.Color(255, 255, 255));

        lbQuantidadeExistente.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidadeExistente.setText("Qtd. Existente");

        lbQuantidadeEntrar.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbQuantidadeEntrar.setText("Qtd. a entrar");

        txtQuantidadeEntrar.setBackground(new java.awt.Color(51, 153, 0));
        txtQuantidadeEntrar.setForeground(new java.awt.Color(255, 255, 255));
        txtQuantidadeEntrar.setCaretColor(new java.awt.Color(255, 255, 255));
        txtQuantidadeEntrar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtQuantidadeEntrarActionPerformed(evt);
            }
        });

        btn_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        btn_adicionar.setToolTipText("click para adicionar no carrinho");
        btn_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_adicionarActionPerformed(evt);
            }
        });

        btn_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        btn_remover.setToolTipText("click para remover produtos do carrinho");
        btn_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_removerActionPerformed(evt);
            }
        });

        txtCodigoBarra.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoBarra.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoBarra.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoBarra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoBarraActionPerformed(evt);
            }
        });

        lbCategoria2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria2.setText("Cód. Barra:");

        txtCodigoManual.setBackground(new java.awt.Color(51, 153, 0));
        txtCodigoManual.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigoManual.setForeground(new java.awt.Color(255, 255, 255));
        txtCodigoManual.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCodigoManual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtCodigoManualActionPerformed(evt);
            }
        });

        lbCategoria3.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 16)); // NOI18N
        lbCategoria3.setText("Código Manual");

        lbFornecedor.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 2, 16)); // NOI18N
        lbFornecedor.setText("FORNECEDOR: XXXXXXX");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(lbCategoria2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(145, 145, 145))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtCodigoBarra)
                                        .addGap(60, 60, 60)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbQuantidadeEntrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtQuantidadeEntrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 235, Short.MAX_VALUE)
                                .addComponent(btn_adicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_remover, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbCategoria3, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lbCodigoProduto))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 393, Short.MAX_VALUE)
                                .addComponent(lbQuantidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbArmazem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbProduto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lbFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lbFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbQuantidadeExistente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuatidadeExistente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbCategoria3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbQuantidadeEntrar)
                            .addComponent(lbCategoria2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantidadeEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_adicionar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_remover, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        table.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod Prod", "Designacao", "Qtd. Existente", "Qtd. Entrar", "Armazém", "Qtd. Actualizada"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
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
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        status_mensagem_primaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_primaria.setForeground(new java.awt.Color(204, 153, 0));
        status_mensagem_primaria.setText("sahdbvhsdva");

        status_mensagem_secundaria.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        status_mensagem_secundaria.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(status_mensagem_secundaria, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(status_mensagem_primaria, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(status_mensagem_primaria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(status_mensagem_secundaria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarActionPerformed

        procedimento_salvar();
    }//GEN-LAST:event_btn_salvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed

//      accao_combo_categoria();
//         accao_codigo_produto(getIdCodigoProduto());

    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void cmbProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdutoActionPerformed

        accao_codigo_produto( getIdCodigoProduto() );
        //accao_key_pressed_codigo_produto();
    }//GEN-LAST:event_cmbProdutoActionPerformed

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbArmazemActionPerformed
        accao_codigo_produto( getIdCodigoProduto() );
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void btn_adicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_adicionarActionPerformed
        adicionar_produto();
    }//GEN-LAST:event_btn_adicionarActionPerformed

    private void btn_removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removerActionPerformed
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

    private void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarraActionPerformed
        // TODO add your handling code here:
        accao_enter_cod_barra();

    }//GEN-LAST:event_txtCodigoBarraActionPerformed

    private void txtQuantidadeEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeEntrarActionPerformed
        // TODO add your handling code here:
        txtCodigoProduto.requestFocus();
        adicionar_produto();
    }//GEN-LAST:event_txtQuantidadeEntrarActionPerformed

    private void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProdutoActionPerformed
        // TODO add your handling code here:
        accao_key_pressed_codigo_produto();
    }//GEN-LAST:event_txtCodigoProdutoActionPerformed

    private void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoManualActionPerformed
        // TODO add your handling code here:
        accao_enter_cod_manual();
    }//GEN-LAST:event_txtCodigoManualActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new BuscaProdutoVisao( this, rootPaneCheckingEnabled, getCodigoArmazem(), DVML.JANELA_ENTRADA, conexao ).show();
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger( EntradaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( EntradaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( EntradaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( EntradaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    EntradaVisao dialog = new EntradaVisao( new javax.swing.JFrame(), true, 15, "", new BDConexao() );
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
                catch ( SQLException ex )
                {
                    Logger.getLogger( EntradaVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );
    }

    /* CRIACAO DOS GETS  */
    public String getNome()
    {
        return "";
    }

    public String getProduto()
    {
        return "";
    }

    public String getCodBarra()
    {
        //return txtCodigoBarra.getText();
        return "";
    }

    public int getCodigoTipoProduto() throws SQLException
    {
        return BDConexao.getCodigo( "tb_tipo_produto", String.valueOf( cmbCategoria.getSelectedItem() ) );
    }

    public int getCodigoFornecedor() throws SQLException
    {
        // return BDConexao.getCodigoByName("tb_fornecedor", "nome", String.valueOf(  cmbFornecedor.getSelectedItem()));
        return 0;
    }

    public static int getCodigoProduto()
    {
        return produtoDao.getProdutoByDescricao( cmbProduto.getSelectedItem().toString() ).getCodigo();
    }

    private boolean tabela_cheia()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        return modelo.getRowCount() == 0;
    }

    //CLASSE EVENTO TECLADO     
    //----------- evento do teclado ---------------------------------------
    class TratarEventoTeclado implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {
            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() == KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtCodigoProduto.getText().trim() + key;

            }

        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }
    }

    public void ver_dados_produtos( int codigo )
    {

    }

    private String getDesignacaoTipoProutoByCodigo( int codigo )
    {
        try
        {
            return new TipoProdutoController( conexao ).getTipoProdutoModelo( codigo ).getDesignacao();
        }
        catch ( Exception e )
        {
        }
        return "";

    }

    public static int getCodigoArmazem()
    {
        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    public void adicionar_produto()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem( getCodigoProduto(), getCodigoArmazem() );

        if ( stock.getCodigo() != 0 )
        {

            try
            {

                if ( !exist_produto( getCodigoProduto(), armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo() ) )
                {

                    modelo.addRow( new Object[]
                    {
                        getCodigoProduto(),
                        stock.getCodProdutoCodigo().getDesignacao(),
                        getQuantidadeProduto( getCodigoProduto() ),
                        txtQuantidadeEntrar.getText(),
                        cmbArmazem.getSelectedItem().toString(),
                        //                        stock.getQuantidadeExistente()
                        //                        + Integer.parseInt(txtQuantidadeEntrar.getText())

                        conexao.getQtdExistenteStock( getCodigoProduto(), getIdArmazem() )
                        + Integer.parseInt( txtQuantidadeEntrar.getText() ),

                    } );
                }
                else
                {
                    actuazlizar_quantidade( txtQuantidadeEntrar.getText() );
                }

                txtQuantidadeEntrar.setText( "" );
                txtCodigoProduto.setText( "" );

            }
            catch ( Exception e )
            {
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "O Produto selecionado  nao se encontra no  " + cmbArmazem.getSelectedItem().toString() + " convém cadastrar", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    private boolean exist_produto( int codigo, int codigo_armazem )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            if ( Integer.parseInt( String.valueOf( table.getValueAt( i, 0 ) ) ) == codigo && armazemDao.getArmazemByDescricao( modelo.getValueAt( i, 4 ).toString() ).getCodigo() == codigo_armazem )
            {
                this.linha_actual = i;
                return true;
            }
        }
        return false;
    }

    private void actuazlizar_quantidade( String quantidade )
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();

        int qtd_existente = Integer.parseInt( String.valueOf( modelo.getValueAt( linha_actual, 2 ) ) );
        int qtd_entrar = Integer.parseInt( quantidade );
        int qtd_actualizada = qtd_existente + qtd_entrar;

        modelo.setValueAt( quantidade, linha_actual, 3 );
        modelo.setValueAt( qtd_actualizada, linha_actual, 5 );

        this.linha_actual = -1;

    }

    private void procedimento_salvar()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        int qtd = 0;
        boolean certeza = true;
        int id_armazem_local = 0;
        TbStock stock;

        if ( !tabela_cheia() )
        {

            for ( int i = 0; i < modelo.getRowCount(); i++ )
            {
                try
                {

                    this.codigo = Integer.parseInt( String.valueOf( modelo.getValueAt( i, 0 ) ) );
                    qtd = Integer.parseInt( String.valueOf( modelo.getValueAt( i, 3 ) ) );
                    id_armazem_local = armazemDao.getArmazemByDescricao( modelo.getValueAt( i, 4 ).toString() ).getCodigo();

                    this.entrada = new TbEntrada();
                    set_dados_entrada( codigo, qtd, id_armazem_local );
                    entradaDao.create( this.entrada );

                    //1. se existe produto no stock
                    if ( stockDao.exist_produto_stock( codigo, id_armazem_local ) )
                    {

                        //stockDao.repor_qtd(qtd, stockDao.get_stock_by_id_produto_and_id_armazem(codigo, id_armazem_local));
                        actualizar_quantidade( codigo, qtd, id_armazem_local );

                    }//else JOptionPane.showMessageDialog(null, "Possivelmente este produto não esta cadastrado neste armazém", "AVISO", JOptionPane.WARNING_MESSAGE);

                }
                catch ( Exception ex )
                {
                    certeza = false;
                }
            }

        }
        else
        {
            certeza = false;
            JOptionPane.showMessageDialog( null, "Impossivel dar entrada porque a tabela esta vazia!\nConvém dar entrada de produtos", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

        if ( certeza )
        {
            remover_all_item_carrinho();
            procedimento_actualizar_quantidade();
            JOptionPane.showMessageDialog( null, "Entrada efectuada com sucesso!...", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
        }

    }

    private void remover_all_item_carrinho()
    {
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount( 0 );
    }

    private void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.removeRow( table.getSelectedRow() );

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btn_adicionar;
    public static javax.swing.JButton btn_remover;
    public static javax.swing.JButton btn_salvar;
    public static javax.swing.JComboBox cmbArmazem;
    public static javax.swing.JComboBox cmbCategoria;
    public static javax.swing.JComboBox cmbProduto;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbArmazem;
    private javax.swing.JLabel lbCategoria;
    private javax.swing.JLabel lbCategoria2;
    private javax.swing.JLabel lbCategoria3;
    private javax.swing.JLabel lbCodigoProduto;
    public static javax.swing.JLabel lbFornecedor;
    private javax.swing.JLabel lbProduto;
    private javax.swing.JLabel lbQuantidadeEntrar;
    private javax.swing.JLabel lbQuantidadeExistente;
    public static javax.swing.JLabel status_mensagem_primaria;
    public static javax.swing.JLabel status_mensagem_secundaria;
    private javax.swing.JTable table;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoProduto;
    public static javax.swing.JTextField txtQuantidadeEntrar;
    public static javax.swing.JTextField txtQuatidadeExistente;
    // End of variables declaration//GEN-END:variables

    private static int getIdArmazem()
    {

        return armazemDao.getArmazemByDescricao( cmbArmazem.getSelectedItem().toString() ).getCodigo();

    }

    private int getIdCodigoProduto()
    {
        try
        {
            return produtoDao.getProdutoByDescricao( cmbProduto.getSelectedItem().toString() ).getCodigo();
        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private void accao_combo()
    {

    }

    private static void procedimento_actualizar_quantidade()
    {
//        TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem(getIdCodigoProduto(), getIdArmazem());
//        txtQuatidadeExistente.setText(stock.getQuantidadeExistente().toString());
        System.out.println( "  " + getCodigoProduto() + " - " + getCodigoArmazem() );
        System.out.println( "CHEGUEI AQUI ENG." );
        txtQuatidadeExistente.setText( String.valueOf( conexao.getQtdExistenteStock( getCodigoProduto(), getCodigoArmazem() ) ) );

    }

    private static void accao_codigo_produto( int parm_codigo )
    {

        try
        {

            id_armzem = getIdArmazem();
            id_armzem = getIdArmazem();
            System.err.println( "ACCAO CODIGO PRODUTO ID ARMAZEM : " + id_armzem );
            System.err.println( "ACCAO CODIGO PRODUTO FORA" );
            if ( stockDao.exist_produto_stock( parm_codigo, id_armzem ) )
            {
                System.err.println( "ACCAO CODIGO PRODUTO DENTRO" );
                TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem( parm_codigo, id_armzem );
                cmbCategoria.setSelectedItem( stock.getCodProdutoCodigo().getCodTipoProduto().getDesignacao() );
                accao_combo_categoria();
                cmbProduto.setSelectedItem( stock.getCodProdutoCodigo().getDesignacao() );
                txtCodigoProduto.setText( String.valueOf( stock.getCodProdutoCodigo().getCodigo() ) );
                //txtQuatidadeExistente.setText(String.valueOf(stock.getQuantidadeExistente()));
                lbFornecedor.setText( "FORNECEDOR: " + stock.getCodProdutoCodigo().getCodFornecedores().getNome() );

                procedimento_actualizar_quantidade();

                txtQuantidadeEntrar.requestFocus();

            }
            else
            {
                txtQuatidadeExistente.setText( "" );
                JOptionPane.showMessageDialog( null, "O Produto selecionado ainda nao se encontra no  " + cmbArmazem.getSelectedItem().toString() + "\n convem cadastrar!...", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
            }

        }
        catch ( Exception ex )
        {
            txtQuatidadeExistente.setText( "" );
        }

    }

    private void accao_enter_cod_barra()
    {

        try
        {

            String codigo_barra =  txtCodigoBarra.getText().trim();
            TbStock stock_local = stockDao.getStockByCodBarra( codigo_barra, getIdArmazem() );
            System.err.println( " ******** COD PRODUTO :  " + stock_local.getCodProdutoCodigo().getCodigo() );
            accao_codigo_produto( stock_local.getCodProdutoCodigo().getCodigo() );

        }
        catch ( Exception ex )
        {

        }

    }

    private void accao_enter_cod_produto()
    {

        try
        {

            int codigo = Integer.parseInt( txtCodigoProduto.getText() );
//            TbStock stock_local = stockDao.getStockByCodBarra(codigo_barra, getIdArmazem());
//            System.err.println(" ******** COD PRODUTO :  " +stock_local.getCodProdutoCodigo().getCodigo());
            accao_codigo_produto( codigo );

        }
        catch ( Exception ex )
        {

        }

    }

//    
//     private void accao_enter_cod_produto() {
//
//        try {
//
//            Long codigo_barra = Long.parseLong(txtCodigoProduto.getText());
//            TbStock stock_local = stockDao.getStockByCodBarra(codigo_barra, getIdArmazem());
//            System.err.println(" ******** COD PRODUTO :  " +stock_local.getCodProdutoCodigo().getCodigo());
//            accao_codigo_produto(stock_local.getCodProdutoCodigo().getCodigo());
//
//
//        } catch (Exception ex) {
//
//        }
//
//    }
    private void accao_enter_cod_manual()
    {

        try
        {

            String codigo_manual = txtCodigoManual.getText();
            TbStock stock_local = stockDao.getStockByCodManual( codigo_manual, getIdArmazem() );
//            cmbCategoria.setSelectedItem( stock_local.getCodProdutoCodigo().getCodTipoProduto().getDesignacao() );
//            cmbProduto.setSelectedItem( stock_local.getCodProdutoCodigo().getDesignacao() );
//            txtQuatidadeExistente.setText( String.valueOf( stock_local.getQuantidadeExistente() ) );
//            txtQuantidadeEntrar.requestFocus();
            accao_codigo_produto( stock_local.getCodProdutoCodigo().getCodigo() );

        }
        catch ( Exception ex )
        {

        }

    }

    private void accao_combo_produto()
    {
        id_armzem = getIdArmazem();

        if ( stockDao.exist_produto_stock( codigo, id_armzem ) )
        {
            TbStock stock = stockDao.get_stock_by_id_produto_and_id_armazem( codigo, id_armzem );
            cmbProduto.setSelectedItem( stock.getCodProdutoCodigo().getDesignacao() );
            txtQuantidadeEntrar.requestFocus();

        }
        else
        {
            JOptionPane.showMessageDialog( null, "O Produto selecionado ainda nao se encontra no stock \n convém dar a entrada!...", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE );
        }

    }

    private static void accao_combo_categoria()
    {
        try
        {

            cmbProduto.setModel( new DefaultComboBoxModel( produtoDao.getProdutoByCategoria( cmbCategoria.getSelectedItem().toString() ) ) );

        }
        catch ( Exception ex )
        {
            txtCodigoProduto.setText( "" );
        }

    }

    private void accao_key_pressed_codigo_produto()
    {
        try
        {
            codigo = Integer.parseInt( txtCodigoProduto.getText() );
        }
        catch ( Exception e )
        {
            codigo = 0;
        }

        if ( codigo != 0 )
        {
            accao_codigo_produto( codigo );
        }
    }

    public static void accao_key_pressed_codigo_produto( int codigo )
    {

        if ( codigo != 0 )
        {
            accao_codigo_produto( codigo );
        }
    }

    private void set_dados_entrada( int pk_codigo, int qtd, int pk_armazem )
    {
        this.entrada.setDataEntrada( new Date() );
        this.entrada.setIdProduto( produtoDao.findTbProduto( pk_codigo ) );
        this.entrada.setQuantidade( qtd );
        this.entrada.setIdArmazemFK( armazemDao.findTbArmazem( pk_armazem ) );
        this.entrada.setIdUsuario( usuarioDao.findTbUsuario( this.idUser ) );
    }

    public void actualizar_quantidade( int cod, int quantidade, int idArmazem )
    {
        String sql = "UPDATE tb_stock SET quantidade_existente =  " + ( getQuantidadeProduto( cod ) + quantidade ) + " WHERE cod_produto_codigo = " + cod + " AND cod_armazem = " + idArmazem;
        this.conexao.executeUpdate( sql );

    }

    public int getQuantidadeProduto( int cod_produto )
    {

        String sql = "SELECT quantidade_existente FROM  tb_stock WHERE  cod_produto_codigo = " + cod_produto + " AND cod_armazem = " + getCodigoArmazem();
        ResultSet rs = new BDConexao().executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "quantidade_existente" );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            return 0;
        }

        return 0;
    }

}
