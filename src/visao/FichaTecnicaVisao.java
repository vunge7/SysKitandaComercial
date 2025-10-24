/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package visao;

import comercial.controller.AnoEconomicoController;
import comercial.controller.ArmazensAccessoController;
import comercial.controller.ArmazensController;
import comercial.controller.ComprasController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.FichaTecnicaController;
import comercial.controller.FornecedoresController;
import comercial.controller.ItemComprasController;
import comercial.controller.LinhaFichaTecnicaController;
import comercial.controller.LugaresController;
import comercial.controller.PrecosController;
import comercial.controller.ProdutosController;
import comercial.controller.StoksController;
import comercial.controller.TipoProdutosController;
import comercial.controller.UnidadesController;
import comercial.controller.UsuariosController;
import entity.AnoEconomico;
import entity.Compras;
import entity.FichaTecnica;
import entity.ItemCompras;
import entity.LinhaFichaTecnica;
import entity.TbArmazem;
import entity.TbFornecedor;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbTipoProduto;
import entity.TbUsuario;
import entity.Unidade;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;
import util.DVML;
import static util.DVML.CAMINHO_REPORT;
import util.MetodosUtil;
import util.PictureChooser;

/**
 *
 * @author marti
 */
public class FichaTecnicaVisao extends javax.swing.JFrame
{

    private ItemCompras itemCompras;
    private static PrecosController precosController;
    private static LugaresController lugaresController;
    private static ProdutosController produtosController;
    private static TipoProdutosController tipoProdutosController;
    private static StoksController stocksController;
    private static ArmazensController armazensController;
    private static FornecedoresController fornecedoresController;
    private static AnoEconomicoController anoEconomicoController;
    private static DocumentosController documentosController;
    private static ComprasController comprasController;
    private static ItemComprasController itemComprasController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static ArmazensAccessoController armazensAccessoController;
    private static FichaTecnicaController fichaTecnicaController;
    private static LinhaFichaTecnicaController linhafichaTecnicaController;
    private static UnidadesController unidadesController;
    private static TbFornecedor fornecedor;
    private static TbArmazem armazem;
    private static TbStock stock_local;
    private static Compras compra;
    private static TbPreco preco;
    private static Unidade unidade;
    private static TbTipoProduto categoria;
    private static TbProduto produto;
    private static TbUsuario usuario;
    private static FichaTecnica fichaTecnica;
    private static AnoEconomico anoEconomico;
    private static BDConexao conexao;
    private static BDConexao conexaoTransaction;
    private static int cod_usuario;
    private static TbStock stock;
    private static ImageIcon imageIcon;
    private static PictureChooser image_View = new PictureChooser();

    /**
     * Creates new form FichaTecnicaVisao
     */
    public FichaTecnicaVisao( int cod_usuario, BDConexao conexao )
    {
        initComponents();
        setLocationRelativeTo( null );
        setResizable( false );
        FichaTecnicaVisao.conexao = conexao;
        this.cod_usuario = cod_usuario;

        precosController = new PrecosController( conexao );
        lugaresController = new LugaresController( conexao );
        produtosController = new ProdutosController( conexao );
        tipoProdutosController = new TipoProdutosController( conexao );
        stocksController = new StoksController( conexao );
        armazensController = new ArmazensController( conexao );
        fornecedoresController = new FornecedoresController( conexao );
        anoEconomicoController = new AnoEconomicoController( conexao );
        documentosController = new DocumentosController( conexao );
        comprasController = new ComprasController( conexao );
        itemComprasController = new ItemComprasController( conexao );
        usuariosController = new UsuariosController( conexao );
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        armazensAccessoController = new ArmazensAccessoController( conexao );
        fichaTecnicaController = new FichaTecnicaController( conexao );
        linhafichaTecnicaController = new LinhaFichaTecnicaController( conexao );
        unidadesController = new UnidadesController( conexao );
        cmb_designacao_refeicao.setModel( new DefaultComboBoxModel( produtosController.getVectorRefeicao() ) );
        cmb_categoria.setModel( new DefaultComboBoxModel<>( tipoProdutosController.getVector() ) );
        proximo_codigo( fichaTecnicaController );
        MetodosUtil.setArmazemByCampoConfigCompraArmazem( cmbArmazem, conexao, cod_usuario );

        txt_qtd_liquido.addKeyListener( new FactorCorrecao() );
        txt_qtd_liquido.addKeyListener( new CustoTotal() );
        txtPrecoVendaRetalho.addKeyListener( new PrecoVenda() );
        txtMargem_lucro.addKeyListener( new PrecoMargemLucro() );
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroFicha = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bt_pesquisa_refeicao = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmb_categoria = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cmbArmazem = new javax.swing.JComboBox();
        cmb_designacao_refeicao = new javax.swing.JComboBox<>();
        txtCustoTotal = new javax.swing.JTextField();
        txtMargem_lucro = new javax.swing.JTextField();
        txtPrecoVendaRetalho = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lbPhoto = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtQTD_COMPOSTO = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela_ficha_tecnica = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        bt_adicionar = new javax.swing.JButton();
        bt_pesquisa = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lb_produto = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtUnidadeCompra = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtUnidade = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtQtdExistente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPrecoCompra = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_custo_total = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_factor_correcao = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_qtd_liquido = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_qtd_bruta = new javax.swing.JTextField();
        bt_remover = new javax.swing.JButton();
        bt_sair = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        ingredientes = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Pratos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FICHA TÉCNICA");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("Número Ficha");

        txtNumeroFicha.setEditable(false);
        txtNumeroFicha.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N

        jLabel2.setText("Prato/Composto");

        bt_pesquisa_refeicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/proucura.png"))); // NOI18N
        bt_pesquisa_refeicao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_pesquisa_refeicaoActionPerformed(evt);
            }
        });

        jLabel4.setText("Categoria");

        cmb_categoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_categoria.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmb_categoriaActionPerformed(evt);
            }
        });

        jLabel9.setText("Custo Total");

        jLabel11.setText("Margem de Lucro");

        jLabel12.setText("Preco de Venda");

        cmbArmazem.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        cmbArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbArmazemActionPerformed(evt);
            }
        });

        cmb_designacao_refeicao.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        cmb_designacao_refeicao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_designacao_refeicao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmb_designacao_refeicaoActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbPhoto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbPhoto.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel21.setText("Armazém:");

        jPanel2.setBackground(new java.awt.Color(0, 51, 153));
        jPanel2.setForeground(new java.awt.Color(0, 51, 153));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("                           FORMULÁRIO DE COMPOSIÇÃO DE FICHAS TÉCNICAS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel23.setText("Qtd. Composto");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmb_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmb_designacao_refeicao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_pesquisa_refeicao, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNumeroFicha, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCustoTotal)
                                    .addComponent(txtPrecoVendaRetalho)
                                    .addComponent(txtMargem_lucro, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtQTD_COMPOSTO, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbArmazem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmbArmazem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtCustoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(txtMargem_lucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtPrecoVendaRetalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(txtQTD_COMPOSTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNumeroFicha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmb_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cmb_designacao_refeicao, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bt_pesquisa_refeicao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                        .addGap(12, 12, 12))))
        );

        tabela_ficha_tecnica.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Cod.", "Ingrediente", "Unidade", "PU", "Qtd. Bruto", "Qtd. Liquido", "Factor Correcao", "Unidade Compra", "Custo Total"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                true, true, false, false, true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabela_ficha_tecnica);

        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        bt_adicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Button-Add-icon.png"))); // NOI18N
        bt_adicionar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_adicionarActionPerformed(evt);
            }
        });

        bt_pesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/proucura.png"))); // NOI18N
        bt_pesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_pesquisaActionPerformed(evt);
            }
        });

        jLabel3.setText("Ingrediente");

        lb_produto.setFont(new java.awt.Font("SansSerif", 3, 16)); // NOI18N
        lb_produto.setForeground(new java.awt.Color(0, 153, 0));
        lb_produto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_produto.setText("Produto/Artigo");
        lb_produto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 2));

        jLabel20.setText("Unidade Compra");

        txtUnidadeCompra.setEditable(false);

        jLabel19.setText("Unidade");

        txtUnidade.setEditable(false);

        jLabel18.setText("Qtd. Existente");

        txtQtdExistente.setEditable(false);

        jLabel13.setText("Preço Unitário");

        txtPrecoCompra.setEditable(false);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel17.setText("Custo Total");

        txt_custo_total.setEditable(false);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel16.setText("Factor Correção");

        txt_factor_correcao.setEditable(false);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel15.setText("Qtd. Líquido");

        txt_qtd_liquido.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txt_qtd_liquidoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel14.setText("Qtd. Bruta");

        txt_qtd_bruta.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txt_qtd_brutaActionPerformed(evt);
            }
        });

        bt_remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/2934_32x32.png"))); // NOI18N
        bt_remover.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_removerActionPerformed(evt);
            }
        });

        bt_sair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        bt_sair.setText("SAIR");
        bt_sair.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_sairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_adicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_pesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_remover, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_produto, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtUnidadeCompra)
                        .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel14)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_qtd_bruta, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txt_qtd_liquido, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtQtdExistente)
                        .addComponent(jLabel18))
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txt_factor_correcao, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_custo_total, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtPrecoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2))
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_sair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_pesquisa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(8, 8, 8)
                        .addComponent(lb_produto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_remover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_adicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(bt_sair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtUnidadeCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(6, 6, 6)
                .addComponent(txt_qtd_bruta, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtPrecoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel17)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(txt_custo_total, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(txtUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel20))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtQtdExistente)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel16))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_qtd_liquido, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_factor_correcao, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ingredientes e Quantidades", jPanel3);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel5.setText("Tempo de Preparo");

        jTextField2.setText("jTextField2");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Modo de Preparação", jPanel4);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 996, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informacoes Adicionais", jPanel6);

        jLabel6.setText("Custo total da receita");

        jTextField3.setText("jTextField3");

        jLabel7.setText("Margem de Lucro");

        jTextField4.setText("jTextField3");

        jLabel8.setText("%");

        jLabel10.setText("Preco de Venda");

        jTextField6.setText("jTextField3");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addContainerGap(868, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(291, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Calculos de Custos", jPanel5);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/alterar_32x32.png"))); // NOI18N
        jButton1.setText("PROCESSAR FICHA");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img32x32/_ok.png"))); // NOI18N
        jToggleButton1.setText("Entrada de Pratos");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jToggleButton1ActionPerformed(evt);
            }
        });

        ingredientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/1588_32x32.png"))); // NOI18N
        ingredientes.setText("Ingredientes");
        ingredientes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ingredientesActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        jButton3.setText("Actualizar");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        Pratos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/1323287592_stock_task.png"))); // NOI18N
        Pratos.setText("Pratos");
        Pratos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                PratosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ingredientes, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Pratos, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ingredientes)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Pratos))
                    .addComponent(jToggleButton1))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ingredientes, jToggleButton1});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //----------- evento do teclado ---------------------------------------
    class FactorCorrecao implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double qtd_bruto = 0;
            double qtd_liquido = 0;
//            double factor_correcao = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txt_qtd_liquido.getText().trim() + key;
                qtd_liquido = MetodosUtil.convertToDouble( prefixo ) * 1;
//                factor_correcao = MetodosUtil.convertToDouble( prefixo ) * 10;
                qtd_bruto = MetodosUtil.convertToDouble( txt_qtd_bruta.getText() );
                txt_factor_correcao.setText( String.valueOf( MetodosUtil.factor_correcao( qtd_bruto, qtd_liquido ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    prefixo = txt_qtd_liquido.getText().trim();
                    qtd_liquido = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
//                    factor_correcao = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
                    qtd_bruto = MetodosUtil.convertToDouble( txt_qtd_bruta.getText() );
                    txt_factor_correcao.setText( String.valueOf( MetodosUtil.factor_correcao( qtd_bruto, qtd_liquido ) ) );

                }
                catch ( Exception e )
                {
                    txt_qtd_liquido.setText( "" );
                    txt_factor_correcao.setText( "" );

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

    //----------- evento do teclado ---------------------------------------
    class CustoTotal implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double qtd_bruto = 0;
            double qtd_liquido = 0;
            double unidade_compra = 0;
            double preco_compra = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txt_qtd_liquido.getText().trim() + key;
                qtd_liquido = MetodosUtil.convertToDouble( prefixo ) * 1;
//                factor_correcao = MetodosUtil.convertToDouble( prefixo ) * 10;
                qtd_bruto = MetodosUtil.convertToDouble( txt_qtd_bruta.getText() );
                unidade_compra = MetodosUtil.convertToDouble( txtUnidadeCompra.getText() );
                preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                txt_custo_total.setText( String.valueOf( MetodosUtil.custo_total( preco_compra, qtd_bruto, qtd_liquido, unidade_compra ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    prefixo = txt_qtd_liquido.getText().trim();
                    qtd_liquido = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
//                    factor_correcao = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
                    qtd_bruto = MetodosUtil.convertToDouble( txt_qtd_bruta.getText() );
                    unidade_compra = MetodosUtil.convertToDouble( txtUnidadeCompra.getText() );
                    preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
                    txt_custo_total.setText( String.valueOf( MetodosUtil.custo_total( preco_compra, qtd_bruto, qtd_liquido, unidade_compra ) ) );

                }
                catch ( Exception e )
                {
                    txt_qtd_liquido.setText( "" );
                    txt_custo_total.setText( "" );

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

    //----------- evento do teclado ---------------------------------------
    class PrecoVenda implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double custo_total = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txtPrecoVendaRetalho.getText().trim() + key;
                preco_venda = MetodosUtil.convertToDouble( prefixo ) * 100;
                custo_total = MetodosUtil.convertToDouble( txtCustoTotal.getText() );
                txtMargem_lucro.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( custo_total, preco_venda ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    prefixo = txtPrecoVendaRetalho.getText().trim();
                    preco_venda = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
                    custo_total = MetodosUtil.convertToDouble( txtCustoTotal.getText() );
                    txtMargem_lucro.setText( CfMethods.formatarComoPorcoes( MetodosUtil.percentagemGanho( custo_total, preco_venda ) ) );

                }
                catch ( Exception e )
                {
                    txtMargem_lucro.setText( "" );

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
    //----------- evento do teclado ---------------------------------------

    class PrecoMargemLucro implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            double custo_total = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();

                System.err.println( "KEY: " + key );
                prefixo = txtMargem_lucro.getText().trim() + key;
                percentagem = MetodosUtil.convertToDouble( prefixo ) * 1;
                custo_total = MetodosUtil.convertToDouble( txtCustoTotal.getText() );
                txtPrecoVendaRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.precoVendaMargem( custo_total, percentagem ) ) );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    prefixo = txtMargem_lucro.getText().trim();
                    percentagem = MetodosUtil.convertToDouble( prefixo.replace( ",", "." ) );
                    custo_total = MetodosUtil.convertToDouble( txtCustoTotal.getText() );
                    txtPrecoVendaRetalho.setText( CfMethods.formatarComoPorcoes( MetodosUtil.precoVendaMargem( custo_total, percentagem ) ) );

                }
                catch ( Exception e )
                {
                    txtPrecoVendaRetalho.setText( "" );

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

    private void bt_pesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_pesquisaActionPerformed
    {//GEN-HEADEREND:event_bt_pesquisaActionPerformed

        try
        {
            txt_qtd_bruta.setText( "" );
            txt_qtd_liquido.setText( "" );
            txt_factor_correcao.setText( "" );
            txt_custo_total.setText( "" );
            new BuscaProdutoIngredientesVisao( this, rootPaneCheckingEnabled, getIdArmazens(), DVML.JANELA_FICHA_TECNICA, conexao ).show();
            txt_qtd_bruta.requestFocus();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_bt_pesquisaActionPerformed

    public static void busca_produto_by_cod_interno_refeicao( int codProduto )
    {
        produto = (TbProduto) produtosController.findById( codProduto );

        preco = (TbPreco) precosController.getLastIdPrecoByIdProdutos( codProduto );
        categoria = (TbTipoProduto) tipoProdutosController.findById( produto.getCodTipoProduto().getCodigo() );

        try
        {
            if ( !Objects.isNull( produto.getPhoto() ) )
            {
                imageIcon = new ImageIcon( produto.getPhoto() );
                imageIcon.setImage( imageIcon.getImage().getScaledInstance( 112, 109, 100 ) );
                lbPhoto.setIcon( imageIcon );
            }
            else
            {
                lbPhoto.setIcon( null );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            lbPhoto.setIcon( null );
        }

        if ( produto.getCodigo() != 0 )
        {
            cmb_categoria.setSelectedItem( categoria.getDesignacao() );
            cmb_designacao_refeicao.setSelectedItem( produto.getDesignacao() );

            if ( fichaTecnicaController.existeFichaTecnicaRefeicao( produto.getCodigo() ) )
            {
                visualizarLinhasFichatecnica( produto.getCodigo() );
            }
            else
            {
                MetodosUtil.esvaziar_tabela( tabela_ficha_tecnica );
            }
        }
        else
        {

        }
        mostrar_dados_stock( produto );

    }

    public static void busca_produto_by_cod_interno( int codProduto )
    {
        produto = (TbProduto) produtosController.findById( codProduto );
        preco = (TbPreco) precosController.getLastIdPrecoByIdProdutos( codProduto );
        unidade = (Unidade) unidadesController.findById( produto.getCodUnidade().getPkUnidade() );

        if ( produto.getCodigo() != 0 )
        {
            txtPrecoCompra.setText( String.valueOf( preco.getPrecoCompra() ) );
            lb_produto.setText( produto.getDesignacao() );
            txtUnidade.setText( unidade.getDescricao() );
            txtUnidadeCompra.setText( String.valueOf( produto.getUnidadeCompra() ) );

        }
        else
        {

        }
        mostrar_dados_stock( produto );

    }

    private static void mostrar_dados_stock( TbProduto produto_parm )
    {
        TbStock stockByCodBarra = stocksController.getStockByCodBarraAndIdArmazem( produto_parm.getCodBarra(), getIdArmazens() );
        if ( !Objects.isNull( stockByCodBarra ) )
        {
            txtQtdExistente.setText( String.valueOf( stockByCodBarra.getQuantidadeExistente() ) );
        }
        else
        {
            txtQtdExistente.setText( "0" );
        }
    }

    private void cmbArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbArmazemActionPerformed
    {//GEN-HEADEREND:event_cmbArmazemActionPerformed
        //        cmbArmazem.setSelectedIndex( 0 );
    }//GEN-LAST:event_cmbArmazemActionPerformed

    private void bt_sairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_sairActionPerformed
    {//GEN-HEADEREND:event_bt_sairActionPerformed
        dispose();
    }//GEN-LAST:event_bt_sairActionPerformed

    private void txt_qtd_brutaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txt_qtd_brutaActionPerformed
    {//GEN-HEADEREND:event_txt_qtd_brutaActionPerformed
        txt_qtd_liquido.requestFocus();
    }//GEN-LAST:event_txt_qtd_brutaActionPerformed

    private void bt_pesquisa_refeicaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_pesquisa_refeicaoActionPerformed
    {//GEN-HEADEREND:event_bt_pesquisa_refeicaoActionPerformed
        try
        {

            new BuscaProdutoRefeicoesVisao( this, rootPaneCheckingEnabled, getIdArmazens(), DVML.JANELA_FICHA_TECNICA, conexao ).show();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_bt_pesquisa_refeicaoActionPerformed

    private void cmb_categoriaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmb_categoriaActionPerformed
    {//GEN-HEADEREND:event_cmb_categoriaActionPerformed
        cmb_designacao_refeicao.setModel( new DefaultComboBoxModel( ( produtosController.getVectorByIdTipoProdutoRefeicao( getIdTipoProduto() ) ) ) );
    }//GEN-LAST:event_cmb_categoriaActionPerformed

    private void bt_adicionarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_adicionarActionPerformed
    {//GEN-HEADEREND:event_bt_adicionarActionPerformed
        adicionarProdutoATabelaPrincipalPeloBotao();
        scrolltable();
    }//GEN-LAST:event_bt_adicionarActionPerformed

    private void txt_qtd_liquidoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txt_qtd_liquidoActionPerformed
    {//GEN-HEADEREND:event_txt_qtd_liquidoActionPerformed
        adicionarProdutoATabelaPrincipalPeloBotao();
        scrolltable();
    }//GEN-LAST:event_txt_qtd_liquidoActionPerformed

    private void bt_removerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_removerActionPerformed
    {//GEN-HEADEREND:event_bt_removerActionPerformed
        remover_item_carrinho();
    }//GEN-LAST:event_bt_removerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        procedimento_salvar_ficha_tecnica();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmb_designacao_refeicaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmb_designacao_refeicaoActionPerformed
    {//GEN-HEADEREND:event_cmb_designacao_refeicaoActionPerformed
        // TODO add your handling code here:

        int codigo = produtosController.getIdProduto( cmb_designacao_refeicao.getSelectedItem().toString() );
        busca_produto_by_cod_interno_refeicao( codigo );

    }//GEN-LAST:event_cmb_designacao_refeicaoActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton1ActionPerformed
    {//GEN-HEADEREND:event_jToggleButton1ActionPerformed
        try
        {
            new EntradaPratosCompostosVisao( cod_usuario, this.conexao ).setVisible( true );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( MenuPrincipalVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void ingredientesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ingredientesActionPerformed
    {//GEN-HEADEREND:event_ingredientesActionPerformed
                        try
        {
            // TODO add your handling code here:

            new ProdutosIngredientesVisao( this, rootPaneCheckingEnabled, cod_usuario, this.conexao ).setVisible( true );
        }
        catch ( Exception ex )
        {
            Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_ingredientesActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        // TODO add your handling code here:
        procedimento_actualizar_ficha_tecnica();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void PratosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_PratosActionPerformed
    {//GEN-HEADEREND:event_PratosActionPerformed
                try
        {
            // TODO add your handling code here:

            new ProdutosPratosVisao( this, rootPaneCheckingEnabled, cod_usuario, this.conexao ).setVisible( true );
        }
        catch ( Exception ex )
        {
            Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_PratosActionPerformed

    private void procedimento_salvar_ficha_tecnica()
    {

        if ( !MetodosUtil.tabela_vazia( tabela_ficha_tecnica ) )
        {
            try
            {
                salvar_ficha_tecnica();
            }
            catch ( HeadlessException e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao salvar", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adiciona item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void procedimento_actualizar_ficha_tecnica()
    {

        if ( !MetodosUtil.tabela_vazia( tabela_ficha_tecnica ) )
        {
            conexaoTransaction = BDConexao.getInstancia();
            DocumentosController.start( conexaoTransaction );
//            fichaTecnicaController = new FichaTecnicaController( conexaoTransaction );
            try
            {
                int codigo = produtosController.getIdProduto( cmb_designacao_refeicao.getSelectedItem().toString() );
                FichaTecnica fichaByRefeicao = fichaTecnicaController.getFichaByRefeicao( codigo );
                System.out.println( "ID REFEICAO " + produto.getCodigo() );
                System.out.println( "ID FICHA " + fichaByRefeicao.getId() );
                if ( linhafichaTecnicaController.eliminarAllItensByIdFicha( fichaByRefeicao.getId() ) )
                {

                    salvar_item_ficha_tecnica( fichaByRefeicao.getId() );
                }

            }
            catch ( HeadlessException e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao salvar", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário adiciona item na tabela", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    public static boolean salvar_ficha_tecnica()
    {
        conexaoTransaction = BDConexao.getInstancia();
        DocumentosController.start( conexaoTransaction );

        Date data_documento = new Date();

        FichaTecnica ficha_tecnica_local = new FichaTecnica();
//produto_id
        System.err.println( "#salvar_ficha: " );

        ficha_tecnica_local.setProdutoId( produtosController.getProdutoByDesignacao( cmb_designacao_refeicao.getSelectedItem().toString() ).getCodigo() );
        ficha_tecnica_local.setDataCriacao( data_documento );

        ficha_tecnica_local.setUsuarioIdCriacao( cod_usuario );
        ficha_tecnica_local.setDataActualizacao( data_documento );

        ficha_tecnica_local.setUsuarioIdActualizacao( cod_usuario );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );

        ficha_tecnica_local.setPrato( cmb_designacao_refeicao.getSelectedItem().toString() );
//        ficha_tecnica_local.setCustoProduto( new BigDecimal( txtCustoTotal.getText() ) );
//        ficha_tecnica_local.setPercentagemGanho( new BigDecimal( txtMargem_lucro.getText() ) );
//        ficha_tecnica_local.setCustoVenda( new BigDecimal( txtPrecoVendaRetalho.getText() ) );
        ficha_tecnica_local.setQtdComposto( Double.parseDouble( txtQTD_COMPOSTO.getText() ) );
        try
        {
            if ( Objects.nonNull( image_View.getBystebyteImg() ) )
            {
                ficha_tecnica_local.setPhoto( image_View.getBystebyteImg() );
            }
        }
        catch ( Exception e )
        {
        }

        try
        {

            if ( fichaTecnicaController.salvar( ficha_tecnica_local ) )
            {
//                System.err.println( "Entrei no salvar" );
                Integer last_ficha = fichaTecnicaController.getLastFicha().getId();

                if ( Objects.isNull( last_ficha ) || last_ficha == 0 )
                {
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }
                if ( !Objects.isNull( last_ficha ) )
                {
                    return salvar_item_ficha_tecnica( last_ficha );
                }
                else
                {
                    return false;
                }

            }
            else
            {
                System.out.println( "ERROR: Já existe ficha tecnica relacionada." );
            }

            return true;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "STATUS: falha ao actualizar a ficha tecnica" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Ficha Técnica", "FALHA", JOptionPane.ERROR_MESSAGE );
            DocumentosController.rollback( conexaoTransaction );
            conexaoTransaction.close();

        }
        return false;

    }

    private static boolean salvar_item_ficha_tecnica( Integer cod_ficha_tecnica )
    {
        System.out.println( "Cod ficha no Item" + cod_ficha_tecnica );
        boolean sucesso = true;
        if ( !MetodosUtil.tabela_vazia( tabela_ficha_tecnica ) )
        {
            ItemComprasController itemComprasControllerLocal = new ItemComprasController( conexaoTransaction );
            LinhaFichaTecnica linhaFichaTecnica;
            for ( int i = 0; i < tabela_ficha_tecnica.getRowCount(); i++ )
            {
                try
                {
                    linhaFichaTecnica = new LinhaFichaTecnica();
                    linhaFichaTecnica.setIgrendienteId( Integer.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 0 ).toString() ) );
                    linhaFichaTecnica.setIgrendienteDesignacao( tabela_ficha_tecnica.getModel().getValueAt( i, 1 ).toString() );
                    linhaFichaTecnica.setUnidade( tabela_ficha_tecnica.getModel().getValueAt( i, 2 ).toString() );
                    linhaFichaTecnica.setPrecoUnitario( new BigDecimal( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 3 ) ) ) );
                    linhaFichaTecnica.setQtdBruto( Double.valueOf( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 4 ) ) ) );
                    linhaFichaTecnica.setQtdLiquido( Double.valueOf( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 5 ) ) ) );
                    linhaFichaTecnica.setFactorCorrecao( Double.valueOf( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 6 ) ) ) );
                    linhaFichaTecnica.setUnidade_compra( Double.valueOf( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 7 ) ) ) );
                    linhaFichaTecnica.setCustoTotal( new BigDecimal( String.valueOf( tabela_ficha_tecnica.getModel().getValueAt( i, 8 ) ) ) );
                    linhaFichaTecnica.setFichaTecnicaId( cod_ficha_tecnica );

                    //cria o item ficha
                    if ( !linhafichaTecnicaController.salvar( linhaFichaTecnica ) )
                    {
                        DocumentosController.rollback( conexaoTransaction );
                        conexaoTransaction.close();
                        return false;
                    }
                }
                catch ( Exception e )
                {
                    sucesso = false;
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao registrar a ficha", "Falha", JOptionPane.ERROR_MESSAGE );
                    DocumentosController.rollback( conexaoTransaction );
                    conexaoTransaction.close();
                    return false;
                }
            }
            if ( sucesso )
            {
                DocumentosController.commit( conexaoTransaction );
                //esvaziar_tabela();
                fichaTecnica = null;
                JOptionPane.showMessageDialog( null, "Ficha Técnica efectuada com sucesso!.." );
//                exibirRecibo();
                conexaoTransaction.close();
                return true;
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Adiciona itens na tabela caro usuário", "AVISO", JOptionPane.WARNING_MESSAGE );
        }
        return false;
    }

    public void remover_item_carrinho()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabela_ficha_tecnica.getModel();
        modelo.removeRow( tabela_ficha_tecnica.getSelectedRow() );
        actualizar_total_custo();

    }

    private static void exibirRecibo()
    {
        try
        {
            Integer last_ficha = fichaTecnicaController.getLastFicha().getId();
            FichaTecnica ficha_tecnica_local = (FichaTecnica) fichaTecnicaController.findById( last_ficha );

            Connection connection = (Connection) BDConexao.getConexao();
            HashMap hashMap = new HashMap();
            hashMap.put( "PK_FICHA", 1 );
            String relatorio = CAMINHO_REPORT + "ficha.jasper";;
//            String relatorio = CAMINHO_REPORT + "ficha_recibo.jasper";;

            File file = new File( relatorio ).getAbsoluteFile();
            String obterCaminho = file.getAbsolutePath();

            try
            {
                JasperFillManager.fillReport( obterCaminho, hashMap, connection );
                JasperPrint jasperPrint = JasperFillManager.fillReport( obterCaminho, hashMap, connection );
                if ( jasperPrint.getPages().size() >= 1 )
                {
                    JasperViewer jasperViewer = new JasperViewer( jasperPrint, false );
                    jasperViewer.setVisible( true );
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "Nao Existem Operações!..." );
                }
            }
            catch ( JRException jex )
            {
                jex.printStackTrace();
                //System.out.println("aqui");
                JOptionPane.showMessageDialog( null, "FALHA AO TENTAR MOSTRAR A FICHA TECNICA!..." );
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog( null, "ERRO AO EFECTUAR A FICHA TECNICA!..." );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( CompraVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public static double getTotalCusto()
    {

        DefaultTableModel modelo = (DefaultTableModel) tabela_ficha_tecnica.getModel();

        double total_custo = 0;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            total_custo += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 7 ) ) );

        }
        return total_custo;

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
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FichaTecnicaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {

                new FichaTecnicaVisao( 15, BDConexao.getInstancia() ).show();
            }
        } );
    }

    private void proximo_codigo( FichaTecnicaController fichaTecnicaController )
    {
        try
        {
            FichaTecnica lastFicha = fichaTecnicaController.getLastFicha();
            int codFicha = 1;
            if ( Objects.nonNull( lastFicha ) )
            {
                codFicha = fichaTecnicaController.getLastFicha().getId() + 1;
//                txtCodigoBarra.setText( String.valueOf( codProduto ) );
            }

            txtNumeroFicha.setText( "Cod. Prod: " + codFicha );
            txtNumeroFicha.setText( String.valueOf( codFicha ) );
        }
        catch ( Exception e )
        {
            txtNumeroFicha.setText( "Cod. Prod: " );
            txtNumeroFicha.setText( "" );
        }
    }

    public void adicionarProdutoATabelaPrincipalPeloBotao()
    {

        TbProduto produto_local = (TbProduto) produtosController.findByDesignacao( lb_produto.getText() );

        adicionar_produto( produto_local );

    }

    public static void adicionar_produto( TbProduto produto )
    {

        System.err.println( "Adicionar: " + produto.getCodigo() );
        System.err.println( "produto: " + produto.getDesignacao() );
        DefaultTableModel modelo = (DefaultTableModel) tabela_ficha_tecnica.getModel();

        if ( !exist_produto_tabela_formulario( tabela_ficha_tecnica, produto.getCodigo() ) )
        {
            if ( validar_zero_qtd_preco() )
            {
                modelo.addRow( new Object[]
                {
                    produto.getCodigo(),
                    produto.getDesignacao(),
                    getUnidade(),
                    getPrecoCompra(),
                    getQtdBruta(),
                    getQtdLiquida(),
                    getFactorCorrecao(),
                    getUnidadeCompra(),
                    getCustoTotal()

                } );

            }

        }
        else
        {
//            actuazlizar_quantidade_tabela_formulario( String.valueOf( getQuantidade() ), 0.0 );
        }
        actualizar_total_custo();

    }

    private static void actualizar_total_custo()
    {
        DefaultTableModel tabela = (DefaultTableModel) tabela_ficha_tecnica.getModel();

        double custo_total = 0;
        double valor = 0;

        for ( int i = 0; i < tabela.getRowCount(); i++ )
        {

            valor = Double.parseDouble( String.valueOf( tabela.getValueAt( i, 8 ) ) );
//            valor = CfMethods.parseMoedaFormatada( String.valueOf( tabela.getValueAt( i, 7 ) ) );
            custo_total += valor;

        }

//        txtCustoTotal.setText(custo_total );
        txtCustoTotal.setText( String.valueOf( CfMethods.formatarComoPorcoes( custo_total ) ) );

    }

    private static boolean exist_produto_tabela_formulario( JTable tabela_busca, Integer codigo )
    {
        DefaultTableModel modelo = (DefaultTableModel) tabela_busca.getModel();

        if ( modelo.getRowCount() == 0 )
        {
            return false;
        }

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            System.err.println( "Codigo: " + modelo.getValueAt( i, 0 ) );
            System.err.println( "Designação: " + modelo.getValueAt( i, 1 ) );
            if ( modelo.getValueAt( i, 0 ) == codigo )
            {
                return true;
            }
        }

        return false;
    }

    public JFrame getInstance()
    {
        return this;
    }

    public static boolean validar_zero_qtd_preco()
    {
        if ( Double.parseDouble( txt_qtd_liquido.getText() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nA quantidade liquida deve ser preenchida!" );

            return false;
        }
        else if ( Double.parseDouble( txtPrecoCompra.getText() ) == 0 )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO preço não pode ser igual a zero!" );

            return false;
        }
        return true;

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Pratos;
    private javax.swing.JButton bt_adicionar;
    private javax.swing.JButton bt_pesquisa;
    private javax.swing.JButton bt_pesquisa_refeicao;
    private javax.swing.JButton bt_remover;
    private javax.swing.JButton bt_sair;
    public static javax.swing.JComboBox cmbArmazem;
    private static javax.swing.JComboBox<String> cmb_categoria;
    private static javax.swing.JComboBox<String> cmb_designacao_refeicao;
    private javax.swing.JButton ingredientes;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JToggleButton jToggleButton1;
    private static javax.swing.JLabel lbPhoto;
    public static javax.swing.JLabel lb_produto;
    private static javax.swing.JTable tabela_ficha_tecnica;
    private static javax.swing.JTextField txtCustoTotal;
    private static javax.swing.JTextField txtMargem_lucro;
    private javax.swing.JTextField txtNumeroFicha;
    private static javax.swing.JTextField txtPrecoCompra;
    private static javax.swing.JTextField txtPrecoVendaRetalho;
    private static javax.swing.JTextField txtQTD_COMPOSTO;
    private static javax.swing.JTextField txtQtdExistente;
    private static javax.swing.JTextField txtUnidade;
    private static javax.swing.JTextField txtUnidadeCompra;
    private static javax.swing.JTextField txt_custo_total;
    private static javax.swing.JTextField txt_factor_correcao;
    private static javax.swing.JTextField txt_qtd_bruta;
    private static javax.swing.JTextField txt_qtd_liquido;
    // End of variables declaration//GEN-END:variables

    public void scrolltable()
    {

        tabela_ficha_tecnica.scrollRectToVisible( tabela_ficha_tecnica.getCellRect( tabela_ficha_tecnica.getRowCount() - 1, tabela_ficha_tecnica.getColumnCount(), true ) );

    }

    private static int getIdArmazens()
    {
        return armazensController.findByDesignacao( cmbArmazem.getSelectedItem().toString() ).getCodigo();
    }

    private static double getPrecoCompra()
    {
        return Double.parseDouble( txtPrecoCompra.getText() );
    }

    private static double getQtdBruta()
    {
        return Double.parseDouble( txt_qtd_bruta.getText() );
    }

    private static double getQtdLiquida()
    {
        return Double.parseDouble( txt_qtd_liquido.getText() );
    }

    private static double getFactorCorrecao()
    {
        return Double.parseDouble( txt_factor_correcao.getText() );
    }

    private static double getCustoTotal()
    {
        return Double.parseDouble( txt_custo_total.getText() );
    }

    private static String getUnidade()
    {
        return txtUnidade.getText();
    }

    private int getIdTipoProduto()
    {
        try
        {
            return tipoProdutosController.getTipoFamiliaByDesignacao( String.valueOf( cmb_categoria.getSelectedItem() ) ).getCodigo();

        }
        catch ( Exception e )
        {
            return 0;
        }

    }

    private static double getUnidadeCompra()
    {
        return Double.parseDouble( txtUnidadeCompra.getText() );
    }

    private static void visualizarLinhasFichatecnica( Integer codRefeicao )
    {
        FichaTecnica fichaByRefeicao = fichaTecnicaController.getFichaByRefeicao( codRefeicao );
        List<LinhaFichaTecnica> listaLinhas = linhafichaTecnicaController.listarTodosByCodigoFichaTecnica( fichaByRefeicao.getId() );

        if ( Objects.nonNull( listaLinhas ) )
        {
            DefaultTableModel modelo = (DefaultTableModel) tabela_ficha_tecnica.getModel();
            modelo.setRowCount( 0 );
            for ( int i = 0; i < listaLinhas.size(); i++ )
            {
                LinhaFichaTecnica linha = listaLinhas.get( i );
                modelo.addRow( new Object[]
                {
                    linha.getIgrendienteId(),
                    linha.getIgrendienteDesignacao(),
                    linha.getUnidade(),
                    linha.getPrecoUnitario(),
                    linha.getQtdBruto(),
                    linha.getQtdLiquido(),
                    linha.getFactorCorrecao(),
                    linha.getUnidade_compra(),
                    linha.getCustoTotal()
                } );
            }
        }

    }

}
