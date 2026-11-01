/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao.produto;

import controller.ProdutoController;
import comercial.controller.*;
import dao.DocumentoDao;
import dao.ItemPermissaoDao;
import entity.*;
import exemplos.PermitirNumeros;
import java.awt.Color;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import visao.*;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import kitanda.util.CfMethods;
import util.BDConexao;
import util.CalculoPrecoVenda;
import util.DVML;
import util.FinanceUtils;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.normalizarCodigoBarra;
import static util.MetodosUtil.normalizarCodigoManual;
import static util.MetodosUtil.normalizarDesignacao;
import static util.MetodosUtil.rodarComandoWindows;
import util.PictureChooser;
import util.TemaNegocioUtil;
import util.TextFieldUtils;

public class ProdutosModernoVisao extends javax.swing.JFrame {

    /**
     * CONTROLLER COMERCIAL
     */
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemPermissaoDao itemPermissaoDao = new ItemPermissaoDao(emf);
    private static ProdutosController produtosController;
    private static ModelosController modelosController;
    private static MarcasController marcasController;
    private static GruposController gruposController;
    private static FamiliasController familiasController;
    private static TipoProdutosController tipoProdutosController;
    private static LocalController localController;
    private static UnidadesController unidadesController;
    private static PrecosController precosController;
    private static ArmazensController armazensController;
    private static ProdutosMotivosIsensaoController produtosMotivosIsensaoController;
    private static ProdutosIsentoController produtosIsentoController;
    private static ProdutosImpostoController produtosImpostoController;
    private static ImpostosController impostosController;
    private static ServicosRetencaoController servicosRetencaoController;
    private static RetencaoController retencaoController;
    private static StoksController stoksController;
    private static UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static ReferenciasController referenciasController;

    /**
     * ENTIDADES
     */
    private static TbProduto produto;
    private static Retencao retencao;
    private static TbStock stock;
    public static TbDadosInstituicao dadosInstituicao;
    private static TbPreco preco;

    /**
     * UTILITARIOS
     */
    private PictureChooser image_View = new PictureChooser();
    private static BDConexao conexao, conexaoTransaction;
    private Frame parent;
    private static ImageIcon imageIcon;

    /**
     * OUTROS
     */
    private static int codigo = 0, idUser = 0;
    private boolean stocavel = true;
    private double preco_venda_anterior = 0;

    /**
     * Janela actualmente aberta: as lupas de pesquisa (BuscaProdutoVisao)
     * usam-na para saber para onde devolver o produto escolhido.
     */
    private static ProdutosModernoVisao instanciaAberta;

    public static boolean estaAberta() {
        return instanciaAberta != null && instanciaAberta.isDisplayable();
    }

    public ProdutosModernoVisao(java.awt.Frame parent, boolean modal, int idUSer, BDConexao conexao) {

        this.parent = parent;
        initComponents();
        rbJanelaServico.setVisible(false);

        instanciaAberta = this;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (instanciaAberta == ProdutosModernoVisao.this) {
                    instanciaAberta = null;
                }
            }
        });

        /**
         * INSTANCIAS DOS CONTROLLER
         */
        produtosController = new ProdutosController(conexao);
        modelosController = new ModelosController(conexao);
        marcasController = new MarcasController(conexao);
        gruposController = new GruposController(conexao);
        familiasController = new FamiliasController(conexao);
        tipoProdutosController = new TipoProdutosController(conexao);
        localController = new LocalController(conexao);
        unidadesController = new UnidadesController(conexao);
        precosController = new PrecosController(conexao);
        armazensController = new ArmazensController(conexao);
        produtosIsentoController = new ProdutosIsentoController(conexao);
        produtosMotivosIsensaoController = new ProdutosMotivosIsensaoController(conexao);
        produtosImpostoController = new ProdutosImpostoController(conexao);
        servicosRetencaoController = new ServicosRetencaoController(conexao);
        impostosController = new ImpostosController(conexao);
        retencaoController = new RetencaoController(conexao);
        usuariosController = new UsuariosController(conexao);
        stoksController = new StoksController(conexao);
        dadosInstituicaoController = new DadosInstituicaoController(conexao);
        referenciasController = new ReferenciasController(conexao);

        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById(1);

// Configurar o controlo de lotes conforme o negócio da instituição.
        configurarControloLotes();

        confiLabel();
        setWindowsListener();

        setLocationRelativeTo(null);
        ProdutosModernoVisao.idUser = idUSer;

        set_combos();

        setRegime(dadosInstituicao.getRegime());
        txtPrecoDeVendaComIva.setVisible(false);
        TotalIvaLabel.setVisible(false);
        cmbImposto.setVisible(false);
        this.conexao = conexao;
        jPanel_retencao.setVisible(true);
        ck_produto.setSelected(true);
//        servico_produto();
        actualizarRetencaoForm();
        proximo_codigo(produtosController);
        proximo_codigo_manual(produtosController);
        busca_permissao();
        txtCodigoProduto.setDocument(new PermitirNumeros());
        setFocus(dadosInstituicao.getFoco());
        txtPrecoCompra.setHorizontalAlignment(JTextField.RIGHT);
        txtPrecoVendaRetalho.setHorizontalAlignment(JTextField.RIGHT);
        txtPercentagemGanhoRetalho.setHorizontalAlignment(JTextField.RIGHT);
        txtPrecoVendaGrosso.setHorizontalAlignment(JTextField.RIGHT);
        popularComponentes();
        configurar_dois_precos();
        setArmazem(dadosInstituicaoController.findByCodigo(1).getConfigArmazens());
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == e.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_TAB) {

                            txtPrecoVendaRetalho.requestFocus();
                            return true;

                        }
                        return false;
                    }
                });

        try {

            setActivarNegocio(dadosInstituicao.getNegocio());
            setActivarJanelaServico(dadosInstituicao.getJanelaServico());

        } catch (Exception e) {
            e.printStackTrace();
        }

        activarCampoPreco();

        TextFieldUtils.configurarCampoDecimal(txtPrecoCompra, 7);
        TextFieldUtils.configurarCampoDecimal(txtPrecoVendaRetalho, 7);
        new CalculoPrecoVenda(txtPrecoCompra, txtPercentagemGanhoRetalho, txtPrecoVendaRetalho)
                .pontoDecimal()
                .somenteComFoco()
                .ligar();

        boolean ehFarmacia = dadosInstituicao != null
                && TemaNegocioUtil.ehFarmacia(dadosInstituicao.getNegocio());

        // Os campos do kit visual usam um tom suave desta cor e o anel de foco.
        visao.design.Tema.definirCorNegocio(TemaNegocioUtil.corDoNegocio(ehFarmacia));
        repaint();

    }

    @SuppressWarnings("unchecked")

    public void busca_permissao() {

        try {
            // TODO add your handling code here
            setStatusUsuario((Vector) itemPermissaoDao.getAllPermissoesByIdUsuarioAndModulo(idUser, DVML.MODULO_GESTAO_COMERCIAL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setArmazem(String armazem) {
        if (!armazem.equalsIgnoreCase("Multi_armazem")) {
            btLupaEconomato.setVisible(false);
//            jmFrontOffice.setVisible( false );

        } else {
            btLupaEconomato.setVisible(true);
//            jmFrontOffice.setVisible( true );

        }
    }

    public void setStatusUsuario(Vector<TbItemPermissao> vector) {

        String permissao = "";
        //limpa
        setPermissoes(false);

        for (int i = 0; i < vector.size(); i++) {

            permissao = vector.get(i).getIdPermissao().getDescricao();
            if (permissao.equals(jButtonCompras.getText())) {
                jButtonCompras.setVisible(true);
            }

        }

    }

    public void setPermissoes(boolean status) {
        jButtonCompras.setVisible(status);
    }

    /**
     * Constrói a interface em código (sem .form do NetBeans): os componentes
     * são criados em {@link ProdutoComponentes}, os eventos ligados em
     * {@link ProdutoEventos} e a disposição responsiva feita em
     * {@link ProdutoLayout}.
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("...::::: KITANDA - PRODUTO/SERVIÇO ::::...");

        new ProdutoComponentes(this).criar();
        new ProdutoEventos(this).ligar();
        new ProdutoLayout(this).montar();
    }

    void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {

        procedimento_salvar_produto();


    }

    void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        dispose();
    }

    void cmbTipoProdutoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // TODO add your handling code here:
            System.out.println("CODIGO: " + getCodigoTipoProduto());
        } catch (SQLException ex) {
            Logger.getLogger(ProdutosModernoVisao.class.getName()).log(Level.SEVERE, null, ex);
        }

        //DDRH

    }

    void txtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            btnSalvar.setEnabled(false);
            btnAlterar2.setEnabled(true);
            String codInternoString = txtCodigoProduto.getText();
            Integer codigoInternoInt = (codInternoString.isEmpty() ? 0 : Integer.parseInt(codInternoString));
            produto = (TbProduto) produtosController.findById(codigoInternoInt);

            ver_dados_produtos(codigoInternoInt);
            if (ivaAplicarJRadioButton.isSelected()) {

//                calcularTotalComIva();
//                txtPrecoDeVendaComIva.setVisible(false);
//                TotalIvaLabel.setVisible(false);
//                calcularSemIva();
//                atualizarIvaForm();
                calcularIva();
            } else {
                txtPrecoDeVendaComIva.setVisible(false);
                ivaTaxaJLabel.setVisible(false);
                TotalIvaLabel.setVisible(false);
            }

            mostrar_qtd_stock_armazem(txtArmazen1, 1);
            mostrar_qtd_stock_armazem(txtArmazen2, 2);
            mostrar_qtd_stock_armazem(txtArmazen3, 3);
            mostrar_qtd_stock_armazem(txtArmazen4, 4);

            setButtonAssociar();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void calcularTotalComIva() {

        double totalPrecoVenda, resultado, taxa_iva = 0;
        taxa_iva = Double.parseDouble(cmbImposto.getSelectedItem().toString());

        totalPrecoVenda = MetodosUtil.convertToDouble(txtPrecoVendaRetalho.getText());
        double valor_iva = 1 + (taxa_iva) / 100;

        resultado = totalPrecoVenda * valor_iva;

        txtPrecoDeVendaComIva.setText(
                String.valueOf(CfMethods.formatarComoPorcoes(resultado)));

    }

    void txtCodigoBarraProcuraActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        btnSalvar.setEnabled(false);
        btnAlterar2.setEnabled(true);

        buscar_by_cod_barra();
//        txtCodigoBarraProcura.setText( "" );
    }

    void ck_produtoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        nao_ver_retencao();
        servico_produto();
        txtDesignacao.requestFocus();
    }

    void txtPrecoCompraActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        if (!ck_produto.isSelected()) {
            procedimento_salvar_produto();
        } else {
            txtPrecoVendaRetalho.requestFocus();
        }


    }

    void txtCodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        txtPrecoCompra.requestFocus();
    }

    void txtPrecoVendaRetalhoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //txtQtdGrosso.requestFocus();
        procedimento_salvar_produto();
    }

    void ivaMotivoJComboBoxItemStateChanged(java.awt.event.ItemEvent evt)
    {

    }

    void ivaAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
    {

        atualizarIvaForm();
        txtPrecoDeVendaComIva.setVisible(true);
        TotalIvaLabel.setVisible(true);
        calcularSemIva();
    }

    void ivaNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
    {

        atualizarIvaForm();
        txtPrecoDeVendaComIva.setVisible(false);
        TotalIvaLabel.setVisible(false);
        calcularSemIva();
    }

    void ivaMotivoJComboBoxActionPerformed(java.awt.event.ActionEvent evt)
    {
        calcularSemIva();
    }

    void txtCodigoManualProcuraActionPerformed(java.awt.event.ActionEvent evt) {
        btnSalvar.setEnabled(false);
        btnAlterar2.setEnabled(true);
        buscar_by_cod_manual1();
//        buscar_by_cod_manual();
//        txtCodigoManualProcura.setText( "" );
    }

    void ck_servicoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // TODO add your handling code here:
//        cmbFamilia.setModel( new DefaultComboBoxModel( (Vector) familiaDao.buscaTodasFamiliaServico() ) );
//        cmbFamilia.setSelectedIndex(1);
        rbJanelaServico.setSelected(false);
        procedimento_limpar();
        ver_retencao();
        actualizarRetencaoForm();
        servico_produto();
        txtDesignacao.requestFocus();
//        lbCusto.setText("Preço");
        mostrarPreco();
    }

    private void mostrarPreco() {

        if (ck_servico.isSelected()) {

            lbCusto.setText("Preço:");
        }

    }


    void btnAlterar2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        procedimento_alterar_servico_produto();


    }

    void cmbFamiliaActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            servico_produto();
            cmbTipoProduto.setModel(new DefaultComboBoxModel(tipoProdutosController.getVectorByIdFamilia(getIdFamilia())));

        } catch (Exception e) {
        }
    }

    void cmbMarcaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            cmbModelo.setModel(new DefaultComboBoxModel(modelosController.getVectorByIdMarca(getIdMarca())));

        } catch (Exception e) {
        }
    }

    void cmbModeloActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    void btnCarregarActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        image_View.chooseImg(lbPhoto);
    }

    void retencaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        actualizarRetencaoForm();
    }

    void retencaoNaoAplicarJRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        actualizarRetencaoForm();
    }

    void retencaoTaxaJTextFieldActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void cmbImpostoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        calcularIva();
        calcularSemIva();
    }

    void retencaoZeroTaxaJTextFieldActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void jButton4ActionPerformed(java.awt.event.ActionEvent evt)
    {

        btnSalvar.setEnabled(false);
        btnAlterar2.setEnabled(true);
        try {

            new BuscaProdutoVisao(this, rootPaneCheckingEnabled, getCodigoArmazem(cmbarmazem2.getSelectedItem().toString()), DVML.JANELA_PRODUTO, BDConexao.getInstancia()).setVisible(true);
            mostrar_qtd_stock_armazem(txtArmazen1, 1);
            mostrar_qtd_stock_armazem(txtArmazen2, 2);
            mostrar_qtd_stock_armazem(txtArmazen3, 3);
            mostrar_qtd_stock_armazem(txtArmazen4, 4);

            if (ivaAplicarJRadioButton.isSelected()) {
//                calcularTotalComIva();
                calcularIva();
            } else {
                txtPrecoDeVendaComIva.setVisible(false);
                ivaTaxaJLabel.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void btnLimparActionPerformed(java.awt.event.ActionEvent evt)
    {
        procedimento_limpar_campos();

//        txtCodigoBarra.setText( String.valueOf( produtosController.getLastProduto().getCodigo() + 1 ) );
        btnAlterar2.setEnabled(false);
    }

    void cmbUnidadeActionPerformed(java.awt.event.ActionEvent evt) {
        txtPrecoVendaRetalho.requestFocus();
    }

    void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriasLugarVisao().setVisible(true);
    }

    void jButtonComprasActionPerformed(java.awt.event.ActionEvent evt) {
        try {

            new CompraInformalVisao(idUser, BDConexao.getInstancia()).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void txtPrecoVendaGrossoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void txtQtdGrossoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void jButton3ActionPerformed(java.awt.event.ActionEvent evt)
    {
        procedimento_alterar_servico_produto_eliminar();
    }

    void rbJanelaServicoActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void txtArmazen4ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void btnAssociarActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
        new AssociacaoServicoVisao(idUser,
                getCodigoProduto(),
                getCodigoArmazem(cmbarmazem1.getSelectedItem().toString()), BDConexao.getInstancia()).setVisible(true);
    }

    void jButton2ActionPerformed(java.awt.event.ActionEvent evt)
    {
        novo();
        txtDesignacao.requestFocus();
        txtCodigoBarra.setText("0");
        txtCodigoBarra.setText(String.valueOf(produtosController.getLastProduto().getCodigo() + 1));
    }

    void btLupaEconomatoActionPerformed(java.awt.event.ActionEvent evt)
    {
        btnSalvar.setEnabled(false);
        btnAlterar2.setEnabled(true);
        try {

            new BuscaProdutoVisao(this, rootPaneCheckingEnabled, getCodigoArmazem(cmbarmazem1.getSelectedItem().toString()), DVML.JANELA_PRODUTO, BDConexao.getInstancia()).setVisible(true);
            mostrar_qtd_stock_armazem(txtArmazen1, 1);
            mostrar_qtd_stock_armazem(txtArmazen2, 2);
            mostrar_qtd_stock_armazem(txtArmazen3, 3);
            mostrar_qtd_stock_armazem(txtArmazen4, 4);

            if (ivaAplicarJRadioButton.isSelected()) {
                calcularTotalComIva();
            } else {
                txtPrecoDeVendaComIva.setVisible(false);
                ivaTaxaJLabel.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void txtUnidadeCompraActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void txtCodigoManualActionPerformed(java.awt.event.ActionEvent evt)
    {
        // TODO add your handling code here:
    }

    void jButton5ActionPerformed(java.awt.event.ActionEvent evt)
    {

        if (getCodigoProduto() > 0) {
            new ReferenciasVisao(idUser,
                    getCodigoProduto(),
                    getCodigoArmazem(cmbarmazem1.getSelectedItem().toString()),
                    BDConexao.getInstancia()).setVisible(true);

        }

    }

    void txtFactorConversaoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    void txtPercentagemGanhoRetalhoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // procedimento_salvar_servico_produto();
        txtPrecoVendaRetalho.requestFocus();
    }

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
            java.util.logging.Logger.getLogger(ProdutosModernoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProdutosModernoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProdutosModernoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProdutosModernoVisao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProdutosModernoVisao dialog = new ProdutosModernoVisao(new javax.swing.JFrame(), true, 15, BDConexao.getInstancia());
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(ProdutosModernoVisao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Componentes da interface (criados em ProdutoComponentes)
    static javax.swing.JLabel TotalIvaLabel;
    javax.swing.JButton btLupaEconomato;
    javax.swing.JButton btnAlterar2;
    public static javax.swing.JButton btnAssociar;
    javax.swing.JButton btnCancelar;
    javax.swing.JButton btnCarregar;
    javax.swing.JButton btnLimpar;
    static javax.swing.JButton btnSalvar;
    javax.swing.ButtonGroup buttonGroup1;
    javax.swing.ButtonGroup buttonGroup2;
    javax.swing.ButtonGroup buttonGroup3;
    javax.swing.ButtonGroup buttonGroup4;
    javax.swing.ButtonGroup buttonGroup5;
    javax.swing.ButtonGroup buttonGroup6;
    public static javax.swing.JCheckBox ck_produto;
    public static javax.swing.JCheckBox ck_servico;
    public static javax.swing.JComboBox<String> cmbFamilia;
    public static javax.swing.JComboBox<String> cmbGrupo;
    static javax.swing.JComboBox<String> cmbImposto;
    public static javax.swing.JComboBox<String> cmbLocal;
    public static javax.swing.JComboBox<String> cmbMarca;
    public static javax.swing.JComboBox cmbModelo;
    public static javax.swing.JComboBox cmbTipoProduto;
    public static javax.swing.JComboBox<String> cmbUnidade;
    static javax.swing.JComboBox<String> cmbarmazem1;
    static javax.swing.JComboBox<String> cmbarmazem2;
    static javax.swing.JComboBox<String> cmbarmazem3;
    static javax.swing.JComboBox<String> cmbarmazem4;
    static javax.swing.JRadioButton ivaAplicarJRadioButton;
    javax.swing.JPanel ivaJPanel;
    static javax.swing.JComboBox ivaMotivoJComboBox;
    static javax.swing.JLabel ivaMotivoJLabel;
    static javax.swing.JRadioButton ivaNaoAplicarJRadioButton;
    static javax.swing.JLabel ivaTaxaJLabel;
    javax.swing.JButton jButton1;
    javax.swing.JButton jButton2;
    javax.swing.JButton jButton3;
    javax.swing.JButton jButton4;
    javax.swing.JButton jButton5;
    static javax.swing.JButton jButtonCompras;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JLabel jLabel3;
    javax.swing.JLabel jLabelCodProduto;
    javax.swing.JPanel jPanel1;
    javax.swing.JPanel jPanel3;
    javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    javax.swing.JPanel jPanel6;
    static javax.swing.JPanel jPanel_retencao;
    static javax.swing.JRadioButton jRadioButtonComLotes;
    static javax.swing.JRadioButton jRadioButtonSemLotes;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JLabel label_factor_conversao;
    javax.swing.JLabel label_qtd_bruta;
    javax.swing.JLabel lbBarra;
    javax.swing.JLabel lbBarra1;
    javax.swing.JLabel lbCategoria3;
    javax.swing.JLabel lbCusto;
    static javax.swing.JLabel lbPhoto;
    javax.swing.JLabel lbPreco;
    javax.swing.JLabel lbPrecoVenda;
    javax.swing.JLabel lbPrecoVenda1;
    javax.swing.JLabel lbProduto;
    javax.swing.JLabel lbProdutos2;
    javax.swing.JLabel lbProdutos3;
    javax.swing.JLabel lbProdutos4;
    javax.swing.JLabel lbTicket;
    javax.swing.JLabel lbTipoProduto;
    javax.swing.JLabel lbTipoProduto2;
    javax.swing.JLabel lbTipoProduto3;
    javax.swing.JLabel lbTipoProduto4;
    javax.swing.JLabel lbTipoProduto5;
    javax.swing.JLabel lbUnidade;
    public static javax.swing.JPanel painel_stock;
    javax.swing.JPanel pnPainelAplicar;
    public static javax.swing.JRadioButton rbEnviarCozinha;
    static javax.swing.JRadioButton rbEnviarSala;
    static javax.swing.JRadioButton rbJanelaServico;
    public static javax.swing.JRadioButton rbNaoEnviarCozinha;
    static javax.swing.JRadioButton retencaoAplicarJRadioButton;
    static javax.swing.JRadioButton retencaoNaoAplicarJRadioButton;
    static javax.swing.JLabel retencaoTaxaJLabel;
    static javax.swing.JTextField retencaoTaxaJTextField;
    static javax.swing.JTextField retencaoZeroTaxaJTextField;
    static javax.swing.JTextField txtArmazen1;
    static javax.swing.JTextField txtArmazen2;
    static javax.swing.JTextField txtArmazen3;
    static javax.swing.JTextField txtArmazen4;
    public static javax.swing.JTextField txtCodigoBarra;
    public static javax.swing.JTextField txtCodigoBarraProcura;
    public static javax.swing.JTextField txtCodigoManual;
    public static javax.swing.JTextField txtCodigoManualProcura;
    static javax.swing.JTextField txtCodigoProduto;
    static javax.swing.JTextArea txtDesignacao;
    static javax.swing.JTextField txtFactorConversao;
    public static javax.swing.JTextField txtPercentagemGanhoRetalho;
    public static javax.swing.JTextField txtPrecoCompra;
    static javax.swing.JTextField txtPrecoDeVendaComIva;
    public static javax.swing.JTextField txtPrecoVendaGrosso;
    public static javax.swing.JTextField txtPrecoVendaRetalho;
    public static javax.swing.JTextField txtQtdGrosso;
    static javax.swing.JTextField txtUnidadeCompra;
    // Fim dos componentes da interface

    public void confiLabel() {

        lbTipoProduto.setHorizontalAlignment(JLabel.RIGHT);
        lbCusto.setHorizontalAlignment(JLabel.RIGHT);
        lbProduto.setHorizontalAlignment(JLabel.RIGHT);
        lbUnidade.setHorizontalAlignment(JLabel.RIGHT);

    }

    /* CRIACAO DOS GETS  */
    public String getProduto() {
        return txtDesignacao.getText();
    }

    public int getCodigoTipoProduto() throws SQLException {
        return conexao.getCodigoPublico("tb_tipo_produto", String.valueOf(cmbTipoProduto.getSelectedItem()));
    }

    public static int getCodigoProduto() {

        try {
            return Integer.parseInt(txtCodigoProduto.getText());
        } catch (Exception e) {
            return 0;
        }

    }

    public int getCodigoBarras() {

        try {
            return Integer.parseInt(txtCodigoBarraProcura.getText());
        } catch (Exception e) {
            return 0;
        }

    }

    public Integer lastProduto() {

        String sql = ("select MAX(codigo) from tb_produto");

        ResultSet rs = conexao.executeQuery(sql);

        try {
            if (rs.next()) {
                return rs.getInt("MAX(codigo)");
            }
        } catch (SQLException ex) {
            return 0;
        }

        return 0;
    }

    private void buscar_by_cod_manual() {

        TbProduto produto_local = produtosController.findByCodManual(txtCodigoManualProcura.getText());

        if (!Objects.isNull(produto_local)) {
            ver_dados(stock.getCodProdutoCodigo().getCodigo());
            txtCodigoManualProcura.requestFocus();
        } else {
            procedimento_limpar();
        }

    }

    public float getPreco() {

        try {
            return Float.parseFloat(txtPrecoCompra.getText());
        } catch (Exception e) {
            return 0;
        }

    }

    public String getDataFabrico() {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());

        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-"
                + (gc.get(GregorianCalendar.MONTH) + 1) + "-"
                + gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }

    public String getDataExpiraco() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());

        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-"
                + (gc.get(GregorianCalendar.MONTH) + 1) + "-"
                + gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }

    public String getDataEntrada() {
        GregorianCalendar gc = new GregorianCalendar();

        String dataSelecionada = gc.get(GregorianCalendar.YEAR) + "-"
                + (gc.get(GregorianCalendar.MONTH) + 1) + "-"
                + gc.get(GregorianCalendar.DATE);
        return dataSelecionada;
    }

    public boolean campos_invalidos() {
        Color backGround = Color.WHITE;
        Color foreGround = Color.BLACK;
        Color caretColor = Color.BLACK;

        if (getProduto().equals("")) {
            txtDesignacao.setBackground(backGround);
            txtDesignacao.setForeground(foreGround);
            txtDesignacao.setCaretColor(caretColor);
            JOptionPane.showMessageDialog(null, "Pf. Preêncha o campo Produto: ");
            return true;
        }

        return false;
    }

    public boolean exist_produto() throws SQLException {
        return new ProdutoController(conexao).exist(txtDesignacao.getText());
    }

    public static void ver_dados_produtos(int codigo) {
        try {
            TbProduto produto_local = (TbProduto) produtosController.findByIdStatus(codigo);
            if (!Objects.isNull(produto_local)) {
                ver_dados(produto_local.getCodigo());
            } else {
                procedimento_limpar();
                JOptionPane.showMessageDialog(null, "Não existe produto com este código", "Alerta", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            procedimento_limpar();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Falha na busca do produto\n"
                    + "ou produto desactivado.", JOptionPane.WARNING_MESSAGE);
        }

    }

    private static void servico_produto() {

        if (ck_produto.isSelected()) {
            painel_stock.setVisible(true);
            cmbFamilia.setSelectedItem("Produtos");
            cmbFamilia.setEnabled(false);

        } else {
            painel_stock.setVisible(false);
            jButtonCompras.setVisible(false);
            cmbFamilia.setSelectedItem("Serviços");
            cmbFamilia.setEnabled(false);

        }

    }

    private static void servico_produto1() {

        if (cmbFamilia.getSelectedItem().equals("Serviços")) {
            painel_stock.setVisible(false);
            cmbFamilia.setEnabled(false);
            ck_servico.setSelected(true);
        } else {
            painel_stock.setVisible(true);
            cmbFamilia.setEnabled(true);
        }

    }

    private static void mostrar_painel(TbProduto produto) {

        if (produto.getStocavel().equalsIgnoreCase("true")) {
            ck_produto.setSelected(true);
        } else {
            ck_servico.setSelected(true);
        }

    }

    private void preparar_produto() {
        boolean isStocavel = ck_produto.isSelected();

        double factor_conversao = Double.parseDouble(txtFactorConversao.getText());
        String designacao_produto = normalizarDesignacao(txtDesignacao.getText());
        produto.setDesignacao(designacao_produto);

        produto.setPreco(new BigDecimal(MetodosUtil.convertToDouble(txtPrecoCompra.getText())));
        produto.setDataFabrico(new Date());
        produto.setDataExpiracao(new Date());

        String codBarra = normalizarCodigoBarra(txtCodigoBarra.getText());
        produto.setCodBarra(isStocavel ? codBarra : "2147483647");
        produto.setStatus("Activo");
        produto.setDataEntrada(new Date());
        produto.setStocavel(isStocavel ? "true" : "false");
        produto.setPrecoVenda(MetodosUtil.convertToDouble(txtPrecoCompra.getText()));
        produto.setQuantidadeDesconto(0);

        String codigoManual = normalizarCodigoManual(txtCodigoManual.getText());
        produto.setCodigoManual(isStocavel ? codigoManual : "");
        produto.setCodUnidade(new Unidade(getIdUnidade()));
        produto.setCodLocal(new TbLocal(getIdLocal()));
        produto.setCodFornecedores(new TbFornecedor(1));
        produto.setCodTipoProduto(new TbTipoProduto(getIdTipoPrdouto()));
        produto.setFkModelo(new Modelo(getIdModelo()));
        produto.setFkGrupo(new Grupo(getIdGrupo()));
        produto.setStatusIva(ivaAplicarJRadioButton.isSelected() ? "true" : "false");
        produto.setCozinha(getCozinha());

        // 🔹 Sempre grava 1 na unidade de compra
        produto.setUnidadeCompra(1.0);
        produto.setFactorConversao(factor_conversao);
        produto.setControlaLote(jRadioButtonComLotes.isSelected());

        try {
            if (Objects.nonNull(image_View.getBystebyteImg())) {
                produto.setPhoto(image_View.getBystebyteImg());
            }
        } catch (Exception e) {
        }
    }

    private String getNomeUsuario() {
        TbUsuario usuario = (TbUsuario) usuariosController.findById(idUser);
        return usuario.getNome();
    }

//    private void preparar_produto()
//    {
//
//        boolean isStocavel = ck_produto.isSelected();
//
//        String designacao_produto = getDesignacaoText();
//        produto.setDesignacao( designacao_produto );
//        produto.setPreco( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
//        produto.setDataFabrico( isStocavel ? jcDataFabrico.getDate() : new Date() );
//        produto.setDataExpiracao( isStocavel ? jcDataExpiracao.getDate() : new Date() );
//        produto.setCodBarra( isStocavel ? txtCodigoBarra.getText().trim() : "2147483647" );
//        produto.setStatus( "Activo" );
//        produto.setDataEntrada( new Date() );
//        produto.setStocavel( isStocavel ? "true" : "false" );
//        produto.setPrecoVenda( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
//        produto.setQuantidadeDesconto( 0 );
//        produto.setCodigoManual( isStocavel ? txtCodigoManual.getText() : "" );
//        produto.setCodUnidade( new Unidade( getIdUnidade() ) );
//        produto.setCodLocal( new TbLocal( getIdLocal() ) );
//        produto.setCodFornecedores( new TbFornecedor( 1 ) );
//        produto.setCodTipoProduto( new TbTipoProduto( getIdTipoPrdouto() ) );
//        produto.setFkModelo( new Modelo( getIdModelo() ) );
//        produto.setFkGrupo( new Grupo( getIdGrupo() ) );
//        produto.setStatusIva( ivaAplicarJRadioButton.isSelected() ? "true" : "false" );
    ////        produto.setPercentagemDesconto( 0d );
//        produto.setCozinha( getCozinha() );
//        produto.setUnidadeCompra( Double.valueOf( txtUnidadeCompra.getText() ) );
//
//        try
//        {
//            if ( Objects.nonNull( image_View.getBystebyteImg() ) )
//            {
//                produto.setPhoto( image_View.getBystebyteImg() );
//            }
//        }
//        catch ( Exception e )
//        {
//        }
//
//    }
    private void set_dados_produto() {
        try {
            preparar_produto();
            if (Objects.nonNull(image_View.getBystebyteImg())) {
                produto.setPhoto(image_View.getBystebyteImg());
            } else {
                produto.setPhoto(null);
            }
        } catch (Exception e) {
        }

    }

    private boolean campos_validos_alterar() {

        String codigoBarraText = txtCodigoBarra.getText();
        boolean isProduto = ck_produto.isSelected();

        if (produtosController.existProdutoByCodigoBarraExcepto(produto.getCodigo(), codigoBarraText) && isProduto) {
            JOptionPane.showMessageDialog(null, "Já existe um produto com este codigo de barra.");
            txtCodigoBarra.requestFocus();
            return false;
        } else if (produtosController.existProdutoByDesignacaoExcepto(produto.getCodigo(), getDesignacaoText())) {
            JOptionPane.showMessageDialog(null, "Já existe um produto na base de dados com esta designacao!");
            txtDesignacao.requestFocus();
            return false;
        }

        if (dadosInstituicao.getUsarDoisPrecos().equals("sim")) {
            if (!camposValidosQtdGrossoPrecoGrosso()) {
                return false;
            }

        }

        return true;

    }

    private void set_dados_produto_alterar() {

    }

    private void set_dados_eliminar() {

        String codigoBarraText = txtCodigoBarra.getText();

        try {
            produto.setStatus("Desactivo");
            produtosController.desactivar(conexao, produto);
            JOptionPane.showMessageDialog(null, "Produto desactivado com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
            procedimento_limpar_campos();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void retencaoAtualizar(int fkProduto) {

        servicosRetencaoController.eliminarByIdProduto(fkProduto);
        if (retencaoAplicarJRadioButton.isSelected() && ck_servico.isSelected()) {
            ServicoRetencao servicoRetencao = new ServicoRetencao();
            servicoRetencao.setFkProduto(new TbProduto(fkProduto));
            servicoRetencao.setFkRetencao(new Retencao(retencao.getPkRetencao()));

            servicosRetencaoController.salvar(servicoRetencao);
            retencaoTaxaJTextField.getText();
        }

    }

    private void procedimento_salvar_produto() {
        if (campos_validos()) {
            conexaoTransaction = BDConexao.getInstancia();
            DocumentoDao.startTransaction(conexaoTransaction);
            String designacao_produto = getDesignacaoText();
            ProdutosController produtosControllerLocal = new ProdutosController(conexaoTransaction);
            ProdutosMotivosIsensaoController produtosMotivosIsensaoControllerLocal = new ProdutosMotivosIsensaoController(conexaoTransaction);
            ProdutosIsentoController produtosIsentoControllerLocal = new ProdutosIsentoController(conexaoTransaction);
            ProdutosImpostoController produtosImpostoControllerLocal = new ProdutosImpostoController(conexaoTransaction);
            ServicosRetencaoController servicosRetencaoControllerLocal = new ServicosRetencaoController(conexaoTransaction);
            PrecosController precosControllerLocal = new PrecosController(conexaoTransaction);

            System.out.println("CHEGUEI AQUI!....... INICIAL");
            //ALTERAR PRODUTO
            if (produtosControllerLocal.exist_designacao_produto(conexao, designacao_produto)) {

                try {
                    produto = produtosControllerLocal.findByDesignacao(designacao_produto);
                    set_dados_produto();
                    produto.setCodUserUpdate(ProdutosModernoVisao.idUser);
                    produto.setUserUpdate(getNomeUsuario());
                    produtosControllerLocal.actualizar(produto);
                    DocumentoDao.commitTransaction(conexaoTransaction);
                    proximo_codigo(produtosControllerLocal);
                    proximo_codigo_manual(produtosControllerLocal);
                    conexaoTransaction.close();
                    JOptionPane.showMessageDialog(null, "Produto actualizado com sucesso!");
//                    procedimento_limpar();
                } catch (Exception e) {
                    DocumentoDao.rollBackTransaction(conexaoTransaction);
                    conexaoTransaction.close();
                    JOptionPane.showMessageDialog(null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            } //CRIAR NOVO PRODUTO
            else {

                produto = new TbProduto();
                set_dados_produto();
                produto.setCodUserCreate(ProdutosModernoVisao.idUser);
                produto.setUserCreate(getNomeUsuario());
                if (produtosControllerLocal.salvar(produto)) {

                    System.err.println("Produto Salvo");
//                    DocumentoDao.commitTransaction(conexaoTransaction); // aceita a transacao
                    produto = produtosControllerLocal.getLastProduto();
                    DocumentoDao.startTransaction(conexaoTransaction); // inicia uma nova transacao
                    if (!ivaAplicarJRadioButton.isSelected())//APLICAR ISENÇÃO
                    {
                        String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
                        ProdutosMotivosIsensao motivosIsensao = produtosMotivosIsensaoControllerLocal.getRegime(regimeIsencao);

                        if (Objects.nonNull(motivosIsensao)) {
                            ProdutoIsento produtoIsento = new ProdutoIsento();
                            produtoIsento.setFkProdutosMotivosIsensao(motivosIsensao);
                            produtoIsento.setFkProduto(new TbProduto(produto.getCodigo()));
                            if (!produtosIsentoControllerLocal.salvar(produtoIsento)) {
                                DocumentoDao.rollBackTransaction(conexaoTransaction);
                                conexaoTransaction.close();
                                JOptionPane.showMessageDialog(null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                                return;

                            }
                            System.err.println("Produto Isento Salvo");

                        }

                    } else //APLICAR IVA
                    {

                        System.err.println("$$$$$$$$$$$$$$$$Consegui entrar no Aplicar IVA");

//                        ivaAtualizar(produto.getCodigo(), produtosImpostoControllerLocal, produtosIsentoControllerLocal);
//                        retencaoAtualizar(produto.getCodigo());
                        ProdutoImposto produtoImposto = new ProdutoImposto();
                        produtoImposto.setFkProduto(new TbProduto(produto.getCodigo()));
                        produtoImposto.setFkImposto(new Imposto(getIdImposto()));
                        if (!produtosImpostoControllerLocal.salvar(produtoImposto)) {
                            DocumentoDao.rollBackTransaction(conexaoTransaction);
                            conexaoTransaction.close();
                            JOptionPane.showMessageDialog(null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        DocumentoDao.commitTransaction(conexaoTransaction); // aceita a transacao
                    }

//                    if ( !retencaoAplicarJRadioButton.isSelected() && ck_servico.isSelected() )
//                    if ( !retencaoAplicarJRadioButton.isSelected() )
//                    {
//                        ServicoRetencao produtoRetencao = new ServicoRetencao();
//                        produtoRetencao.setFkProduto( new TbProduto( produto.getCodigo() ) );
//                        produtoRetencao.setFkRetencao( new Retencao( 2 ) );
//                        if ( !servicosRetencaoControllerLocal.salvar( produtoRetencao ) )
//                        {
//                            DocumentoDao.rollBackTransaction( conexaoTransaction );
//                            conexaoTransaction.close();
//                            JOptionPane.showMessageDialog( null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE );
//                            return;
//                        }
//
//                    }
//                    else 
                    //APLICAR RETENCAO
                    if (retencaoAplicarJRadioButton.isSelected()) {
                        ServicoRetencao produtoRetencao = new ServicoRetencao();
                        produtoRetencao.setFkProduto(new TbProduto(produto.getCodigo()));
                        produtoRetencao.setFkRetencao(new Retencao(1));

                        if (!servicosRetencaoControllerLocal.salvar(produtoRetencao)) {
                            DocumentoDao.rollBackTransaction(conexaoTransaction);
                            conexaoTransaction.close();
                            JOptionPane.showMessageDialog(null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                            return;

                        }

                    }

                    //Adicionar no stock caso o artigo for stocável
                    if (produto.getStocavel().equals("true")) {

                        Vector<String> armazens = armazensController.getVector();
                        for (String designacaoArmazem : armazens) {
                            TbArmazem armazem = (TbArmazem) armazensController.findByName(designacaoArmazem);
                            procedimento_registrar_stock(produto.getCodigo(), armazem.getCodigo());
                        }
                    }

                    if (registrar_preco(precosControllerLocal)) {

                        DocumentoDao.commitTransaction(conexaoTransaction);
                        proximo_codigo(produtosControllerLocal);
                        proximo_codigo_manual(produtosControllerLocal);

                        conexaoTransaction.close();
                        JOptionPane.showMessageDialog(null, "Dados salvos com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                        setActivarNegocio(dadosInstituicao.getNegocio());
                        if (rbJanelaServico.isSelected()) {
                            painel_stock.setVisible(false);
                        } else {
                            painel_stock.setVisible(true);
                            procedimento_limpar();
                            txtDesignacao.requestFocus();
                            txtCodigoBarra.setText(String.valueOf(produtosController.getLastProduto().getCodigo() + 1));

                        }

                        servico_produto();
                    }
                    detalhe_produto();

                } else {
                    DocumentoDao.rollBackTransaction(conexaoTransaction);
                    JOptionPane.showMessageDialog(null, "Falha ao registrar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                    return;

                }

            }

        }

    }

    private void procedimento_alterar_servico_produto() {

        if (!Objects.isNull(produto)) {

            if (campos_validos_alterar()) {

                try {

                    conexaoTransaction = BDConexao.getInstancia();
                    ProdutosController produtosControllerLocal = new ProdutosController(conexaoTransaction);
                    PrecosController precosControllerLocal = new PrecosController(conexaoTransaction);
                    ProdutosImpostoController produtosImpostoControllerLocal = new ProdutosImpostoController(conexaoTransaction);
                    ProdutosIsentoController produtosIsentoControllerLocal = new ProdutosIsentoController(conexaoTransaction);
                    UsuariosController usuariosController = new UsuariosController(conexaoTransaction);
                    DocumentoDao.startTransaction(conexaoTransaction);
                    preparar_produto();
                    produto.setCodUserCreate(idUser);
                    produto.setUserCreate(getNomeUsuario());
                    produtosControllerLocal.actualizar(produto);

                    ivaAtualizar(produto.getCodigo(), produtosImpostoControllerLocal, produtosIsentoControllerLocal);
                    retencaoAtualizar(produto.getCodigo());
                    if (registrar_preco(precosControllerLocal)) {
                        DocumentoDao.commitTransaction(conexaoTransaction);
                        JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);
                        proximo_codigo(produtosControllerLocal);
                        proximo_codigo_manual(produtosControllerLocal);
                        conexaoTransaction.close();
                    } else {
                        DocumentoDao.rollBackTransaction(conexaoTransaction);
                        conexaoTransaction.close();
                        JOptionPane.showMessageDialog(null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                        return;

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    DocumentoDao.rollBackTransaction(conexaoTransaction);
                    JOptionPane.showMessageDialog(null, "Falha ao actualizar o produto", "Falha", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            }

        }

    }

    private void procedimento_alterar_servico_produto_eliminar() {

        try {
            set_dados_eliminar();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Falha ao desactivar produto", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void set_combos() {
        cmbMarca.setModel(new DefaultComboBoxModel(marcasController.getVector()));
        cmbGrupo.setModel(new DefaultComboBoxModel(gruposController.getVector()));
        try {
            cmbModelo.setModel(new DefaultComboBoxModel(modelosController.getVectorByIdMarca(getIdMarca())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cmbFamilia.setModel(new DefaultComboBoxModel(familiasController.getVector()));
            cmbTipoProduto.setModel(new DefaultComboBoxModel(tipoProdutosController.getVectorByIdFamilia(getIdFamilia())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        cmbLocal.setModel(new DefaultComboBoxModel(localController.getVector()));
        cmbUnidade.setModel(new DefaultComboBoxModel(unidadesController.getVector()));

        try {
            cmbarmazem1.setModel(new DefaultComboBoxModel(armazensController.getVector(1)));
            cmbarmazem2.setModel(new DefaultComboBoxModel(armazensController.getVector(2)));
            cmbarmazem3.setModel(new DefaultComboBoxModel(armazensController.getVector(3)));
            cmbarmazem4.setModel(new DefaultComboBoxModel(armazensController.getVector(4)));

        } catch (Exception e) {
        }

    }

    private void mostrar_qtd_stock_armazem(JTextField text, int idArmazem) {
        if (Objects.nonNull(produto) && idArmazem > 0) {
            TbStock stockLocal1 = stoksController.getStockByIdProdutoAndIdArmazem(getCodigoProduto(), idArmazem);
//            TbStock stockLocal = stoksController.getStockByIdProdutoAndIdArmazem( produto.getCodigo(), idArmazem );

            if (!Objects.isNull(stockLocal1)) {
//                txtQuantidadeStock.setText( String.valueOf( stockLocal.getQuantidadeExistente() ) );
                text.setText(String.valueOf(stockLocal1.getQuantidadeExistente()));
            } else {
                text.setText("0.0");
            }

        }

    }

    //actualizar
    private int getIdLocal() {
        try {
            return localController
                    .getTbLocalByDesignacao(cmbLocal.getSelectedItem().toString())
                    .getCodigo();
        } catch (Exception e) {
        }
        return 0;
    }

    private int getIdUnidade() {
        try {
            return unidadesController
                    .getUnidadeByDesignacao(cmbUnidade.getSelectedItem().toString())
                    .getPkUnidade();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private int getIdImposto() {

        try {
            return impostosController
                    .getImpostoByDesignacao(cmbImposto.getSelectedItem().toString())
                    .getPkImposto();

        } catch (Exception e) {
        }
        return 0;

    }

    private int getIdGrupo() {
        try {
            System.out.println("ESTADO GRUPO: " + Objects.nonNull(gruposController));
            return gruposController
                    .getGrupoByDesignacao(cmbGrupo.getSelectedItem().toString())
                    .getPkGrupo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getIdTipoPrdouto() {
        try {
            return tipoProdutosController
                    .getTipoFamiliaByDesignacao(cmbTipoProduto.getSelectedItem().toString())
                    .getCodigo();
        } catch (Exception e) {
        }
        return 0;
    }

    private static int getIdFamilia() {
        try {
            return familiasController
                    .getFamiliaByDesignacao(cmbFamilia.getSelectedItem().toString())
                    .getPkFamilia();
        } catch (Exception e) {
        }

        return 0;
    }

    private static int getIdMarca() {
        try {
            return marcasController
                    .getMarcaByDesignacao(cmbMarca.getSelectedItem().toString())
                    .getPkMarca();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    private static int getIdModelo() {
        try {
            return modelosController
                    .getModeloByDesignacao(cmbMarca.getSelectedItem().toString())
                    .getPkModelo();
        } catch (Exception e) {
        }
        return 0;
    }

    public static void ver_dados(int codigo) {

        TbProduto produto_local = (TbProduto) produtosController.findById(codigo);
        produto = produto_local;
        if (!Objects.isNull(produto_local)) {
            mostrar_painel(produto_local);

            preco = (TbPreco) precosController.getLastIdPrecoByIdProduto(codigo, 1);

            if (Objects.isNull(preco)) {
                return;
            }

            Integer pkModelo = produto_local.getFkModelo().getPkModelo();
            Modelo modeloLocal = (Modelo) modelosController.findById(pkModelo);

            Integer pkGrupo = produto_local.getFkGrupo().getPkGrupo();
            Grupo grupoLocal = (Grupo) gruposController.findById(pkGrupo);

            Integer pkMarca = modeloLocal.getFkMarca().getPkMarca();
            Marca marcaLocal = (Marca) marcasController.findById(pkMarca);

            Integer idTipoProduto = produto_local.getCodTipoProduto().getCodigo();
            TbTipoProduto tipoProdutoLocal = (TbTipoProduto) tipoProdutosController.findById(idTipoProduto);

            Integer pkFamilia = tipoProdutoLocal.getFkFamilia().getPkFamilia();
            Familia familiaLocal = (Familia) familiasController.findById(pkFamilia);

            String marca = marcaLocal.getDesignacao();
            String modelo = modeloLocal.getDesignacao();
            String grupo = grupoLocal.getDesignacao();

            String familia = familiaLocal.getDesignacao();
            String tipoProduto = tipoProdutoLocal.getDesignacao();

            cmbMarca.setSelectedItem(marca);
            cmbModelo.setSelectedItem(modelo);
            cmbGrupo.setSelectedItem(grupo);

            cmbFamilia.setSelectedItem(familia);
            cmbTipoProduto.setSelectedItem(tipoProduto);

            try {
                if (!Objects.isNull(produto_local.getPhoto())) {
                    imageIcon = new ImageIcon(produto_local.getPhoto());
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(112, 109, 100));
                    lbPhoto.setIcon(imageIcon);
                } else {
                    lbPhoto.setIcon(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                lbPhoto.setIcon(null);
            }

            Unidade unidadeLocal = (Unidade) unidadesController.findById(produto_local.getCodUnidade().getPkUnidade());
            cmbUnidade.setSelectedItem(unidadeLocal.getDescricao());
            txtDesignacao.setText(produto_local.getDesignacao());

            Double preco_compra = (!Objects.isNull(preco)) ? preco.getPrecoCompra().doubleValue() : 0;

//            txtPrecoCompra.setText( CfMethods.formatarComoPorcoes( preco_compra ) );
            txtPrecoCompra.setText(Double.toString(preco_compra));

            txtCodigoBarra.setText(String.valueOf(produto_local.getCodBarra()));
            txtCodigoManual.setText(String.valueOf(produto_local.getCodigoManual()));
            txtCodigoBarraProcura.setText(String.valueOf(produto_local.getCodBarra()));
            txtCodigoManualProcura.setText(String.valueOf(produto_local.getCodigoManual()));
            txtUnidadeCompra.setText(String.valueOf(produto_local.getUnidadeCompra()));
            txtFactorConversao.setText(String.valueOf(produto_local.getFactorConversao()));

            Integer pkProduto = produto_local.getCodigo();
            boolean temIva = produtosImpostoController.existeProdutoImposto(pkProduto);

            if (!temIva) {
                String regime = produtosIsentoController.getRegimeIsensaoByIdProduto(pkProduto);
                ivaMotivoJComboBox.setSelectedItem(regime);

            } else {

            }

            ivaAplicarJRadioButton.setSelected(temIva);
            ivaNaoAplicarJRadioButton.setSelected(!temIva);

            boolean temRetencao = servicosRetencaoController.existeRetencao(pkProduto);

            if (!temRetencao) {

                String retencao = servicosRetencaoController.getRetensaoByIdProduto(pkProduto);
//                ivaMotivoJComboBox.setSelectedItem(retencao);

            }

            retencaoAplicarJRadioButton.setSelected(temRetencao);
            retencaoNaoAplicarJRadioButton.setSelected(!temRetencao);

//            atualizarIvaForm();
            Double taxa = produtosImpostoController.getTaxaByIdProduto(pkProduto);
            cmbImposto.setSelectedItem(String.valueOf(taxa));
            System.out.println("TAXA REAL: " + taxa);

            actualizarRetencaoForm();
            /**
             * A IDEIA BUSCAR O ULTIMO PRECO(GROSSO) E DEPOIS BUSCAR O PRECO
             * RETALHO EM FUNCAO DO PRECO GROSSO
             */
            TbPreco precoGrosso = precosController.getLastPreco(produto.getCodigo());
            TbPreco precoRetalhoLocal = precosController.getLastIdPrecoByIdProduto(produto_local.getCodigo(), precoGrosso.getQtdBaixo() - 1);
            double preco_venda_retalho = precoRetalhoLocal.getPrecoVenda().doubleValue();
            double preco_venda_grosso = precoGrosso.getPrecoVenda().doubleValue();
            double percentagem_ganho = precoRetalhoLocal.getPercentagemGanho().doubleValue();

//            txtPrecoVendaRetalho.setText( String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_retalho ) ));
            txtPrecoVendaRetalho.setText(Double.toString(preco_venda_retalho));
//            txtPrecoDeVendaSemIva.setText(String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_retalho ) ) );

            txtQtdGrosso.setText(String.valueOf(precoGrosso.getQtdBaixo()));
//            txtPrecoVendaGrosso.setText(String.valueOf( CfMethods.formatarComoPorcoes( preco_venda_grosso ) )
            txtPrecoVendaGrosso.setText(Double.toString(preco_venda_grosso));

//            txtPercentagemGanhoRetalho.setText( CfMethods.formatarComoPorcoes( percentagem_ganho ) );
            txtPercentagemGanhoRetalho.setText(Double.toString(percentagem_ganho));
            txtCodigoProduto.setText(String.valueOf(produto_local.getCodigo()));

            if (produto_local.getStocavel().equals("true")) {
                ck_produto.setSelected(true);
            } else {
                ck_produto.setSelected(false);
            }

            if (produto_local.getControlaLote().equals(true)) {
                jRadioButtonComLotes.setSelected(true);
                jRadioButtonSemLotes.setSelected(false);

            } else {
                jRadioButtonSemLotes.setSelected(true);
                jRadioButtonComLotes.setSelected(false);
            }

            setCozinha(produto_local.getCozinha());
        } else {
            limpar_direito();
            JOptionPane.showMessageDialog(null, "O Produto ainda não existe. ", DVML.DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE);

        }

    }

    private static void procedimento_limpar() {
        limpar_esquerdo();
        limpar_direito();
        //actualizar
        cmbLocal.setModel(new DefaultComboBoxModel(localController.getVector()));
        cmbUnidade.setModel(new DefaultComboBoxModel(unidadesController.getVector()));
//        ck_produto.setSelected( true );

    }

    private static void alterar_estrutura_janela() {

        if (rbJanelaServico.isSelected()) {
            painel_stock.setVisible(false);
        } else {
            painel_stock.setVisible(true);
        }
    }

    private static void procedimento_limpar_campos() {
        limpar_esquerdo();
        limpar_direito_campos();
        //actualizar
        cmbLocal.setModel(new DefaultComboBoxModel(localController.getVector()));
        cmbUnidade.setModel(new DefaultComboBoxModel(unidadesController.getVector()));
//        ck_produto.setSelected( true );
//        servico_produto();
        btnSalvar.setEnabled(true);
        txtDesignacao.requestFocus();
    }

    private static void limpar_esquerdo() {
        lbPhoto.setIcon(null);
        txtDesignacao.setText("");
        txtPrecoCompra.setText("");

    }

    private static void limpar_direito() {

//        txtCodigoBarra.setText( "0" );
        txtPrecoVendaRetalho.setText("");
        txtPercentagemGanhoRetalho.setText("0");
        txtCodigoManual.setText("");
        txtPrecoVendaGrosso.setText("");
        txtQtdGrosso.setText("");
        txtUnidadeCompra.setText("");

    }

    private static void limpar_direito_campos() {

        txtPrecoVendaRetalho.setText("");
        txtPrecoVendaRetalho.setText("");
        txtCodigoProduto.setText("");
        txtCodigoBarraProcura.setText("");
        txtPercentagemGanhoRetalho.setText("0");
        txtPrecoDeVendaComIva.setText("");
        txtCodigoManual.setText("");
        txtUnidadeCompra.setText("");

    }

    public boolean exist_produto_stock(int codigo_produto) {
        try {
            return stoksController.existeStock(codigo_produto, DVML.ARMAZEM_DEFAUTL, conexao);
        } catch (Exception e) {
            return false;
        }
    }

    private void buscar_by_cod_barra() {
        TbProduto produto_local
                = produtosController.findByCodBarra(txtCodigoBarraProcura.getText().trim());

        if (Objects.isNull(produto_local)) {
            int idProdutoByCodigoBarra = referenciasController.getIdProdutoByCodigoBarra(txtCodigoBarraProcura.getText().trim());
            produto_local = produtosController.findByCodInterno(idProdutoByCodigoBarra);
        }

        if (produto_local != null) {
            ver_dados(produto_local.getCodigo());

            txtCodigoBarraProcura.requestFocus();

            if (ivaAplicarJRadioButton.isSelected()) {
                calcularTotalComIva();
            } else {
                txtPrecoDeVendaComIva.setVisible(false);
                ivaTaxaJLabel.setVisible(false);
            }
        } else {
            procedimento_limpar();
            JOptionPane.showMessageDialog(
                    null,
                    "Não existe produto com este código",
                    "Alerta",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

//    private void buscar_by_cod_barra()
//    {
//
//        TbProduto produto_local = produtosController.findByCodBarra( txtCodigoBarraProcura.getText().trim() );
//        if ( !Objects.isNull( produto_local ) )
//        {
//
//            if ( !Objects.isNull( produto_local ) )
//            {
//                ver_dados( produto_local.getCodigo() );
//            }
//            else
//            {
//                procedimento_limpar();
//                JOptionPane.showMessageDialog( null, "Não existe produto com este código", "Alerta", JOptionPane.WARNING_MESSAGE );
//            }
//
//            txtCodigoBarraProcura.requestFocus();
//            if ( ivaAplicarJRadioButton.isSelected() )
//            {
//                calcularTotalComIva();
//            }
//            else
//            {
//                txtPrecoDeVendaComIva.setVisible( false );
//                ivaTaxaJLabel.setVisible( false );
//
//            }
//
//        }
//        else
//        {
//            procedimento_limpar();
//        }
//    }
    private void buscar_by_cod_manual1() {

        //            if ( !Objects.isNull( produto_local ) )
//            {
//                ver_dados( produto_local.getCodigo() );
//            }
//            else
//            {
//                procedimento_limpar();
//                JOptionPane.showMessageDialog( null, "Não existe produto com este código", "Alerta", JOptionPane.WARNING_MESSAGE );
//            }
        TbProduto produto_local = produtosController.findByCodManual1(txtCodigoManualProcura.getText().trim());
        if (!Objects.isNull(produto_local)) {
            ver_dados(produto_local.getCodigo());
            txtCodigoManualProcura.requestFocus();
            if (ivaAplicarJRadioButton.isSelected()) {
                calcularTotalComIva();
            } else {
                txtPrecoDeVendaComIva.setVisible(false);
                ivaTaxaJLabel.setVisible(false);

            }

        } else {
            procedimento_limpar();
            JOptionPane.showMessageDialog(null, "Não existe produto com este código", "Alerta", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void detalhe_produto() {

        Unidade unidade = (Unidade) unidadesController.findById(produto.getCodUnidade().getPkUnidade());

        String output = "";
        TbPreco preco_local = precosController.getLastIdPrecoByIdProduto(produto.getCodigo(), 1);
        output += "CodInterno           : " + produto.getCodigo() + "\n";
        output += "CodManual           : " + produto.getCodigoManual() + "\n";
        output += "Designação           : " + produto.getDesignacao() + "\n";
        output += "Unidade           : " + unidade.getDescricao() + "\n";
        if (produto.getStocavel().equals("true")) {
            output += "Preço da Compra      : " + preco_local.getPrecoCompra() + "\n";
            output += "Percentagem Ganho    : " + preco_local.getPercentagemGanho() + "  % \n";
            output += "Preço de Venda       : " + preco_local.getPrecoVenda() + "\n";
        } else {
            output += "Preço do Serviço      : " + preco_local.getPrecoCompra() + "\n";
        }

        JOptionPane.showMessageDialog(null, output);

    }

    private void setDadosPreco() {

        preco = new TbPreco();
//        preco.setPrecoCompra( new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) ) );
        preco.setPrecoCompra(new BigDecimal(Double.parseDouble(txtPrecoCompra.getText())));

        if (ck_produto.isSelected()) {
            preco.setPercentagemGanho(new BigDecimal(txtPercentagemGanhoRetalho.getText()));

        } else {
            preco.setPercentagemGanho(new BigDecimal("0"));
        }

        preco.setData(new Date());
        preco.setHora(new Date());
        preco.setFkProduto(new TbProduto(produto.getCodigo()));
        preco.setRetalho(true);

        preco.setFkUsuario(new TbUsuario(idUser));

    }

    private boolean campos_validos() {
        boolean isProduto = ck_produto.isSelected();

        System.out.println("Status Produto: " + isProduto);
        MetodosUtil.getValorTransformadoString(preco_venda_anterior);

        String designacao_produto = getDesignacaoText();

        TbUsuario usuarioLocal = (TbUsuario) usuariosController.findById(idUser);

        if (designacao_produto.equals("")) {

            JOptionPane.showMessageDialog(null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " por favor digite a designação do produto");
            txtPrecoCompra.requestFocus();
            return false;

        } else if (produtosController.exist_designacao_produto(conexao, getDesignacaoText())) {

            TbProduto produtoExistente = produtosController.findProdutoByDesignacao(getDesignacaoText());

            String nomeProduto = getDesignacaoText();
            String codigoInterno = "-";

            if (produtoExistente != null) {
                nomeProduto = produtoExistente.getDesignacao();
                codigoInterno = String.valueOf(produtoExistente.getCodigo());
            }

            JOptionPane.showMessageDialog(null,
                    "Atenção: O produto '" + nomeProduto + "' já foi cadastrado no sistema"
                    + "\ncujo código interno é: " + codigoInterno + ".",
                    DVML.DVML_COMERCIAL,
                    JOptionPane.WARNING_MESSAGE);
            txtDesignacao.requestFocus();
            return false;

        } else if (isProduto && produtosController.existProdutoByCodigoBarra(txtCodigoBarra.getText())) {

            TbProduto produtoExistente = produtosController.findProdutoByCodigoBarra(txtCodigoBarra.getText());
            String nomeProduto = (produtoExistente != null) ? produtoExistente.getDesignacao() : "desconhecido";

            String codigoInterno = "-";

            if (produtoExistente != null) {
                nomeProduto = produtoExistente.getDesignacao();
                codigoInterno = String.valueOf(produtoExistente.getCodigo());
            }

            JOptionPane.showMessageDialog(null,
                    "Atenção: Este código de barras já foi utilizado no produto: " + nomeProduto
                    + "\ncujo código interno é: " + codigoInterno + ".",
                    DVML.DVML_COMERCIAL,
                    JOptionPane.WARNING_MESSAGE);
            txtCodigoBarra.requestFocus();
            return false;

        } else if (produtosController.existProdutoByCodigoManual(txtCodigoManual.getText()) && isProduto) {
            JOptionPane.showMessageDialog(null, "Este codigo manual já existe!!!", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            txtCodigoManual.requestFocus();
//            txtCodigoManual.setBackground( Color.YELLOW );
            return false;
        } else if (txtPrecoCompra.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " , insira o preço da compra do produto");
            txtPrecoCompra.requestFocus();
            return false;

        } else if (txtPrecoCompra.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + ", o preço da compra não pode ser igual a zero(0).");
            txtPrecoCompra.requestFocus();
            return false;
        } else if (txtPrecoVendaRetalho.getText().equals("") && isProduto) {

            JOptionPane.showMessageDialog(null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + " , insira o preço da venda do produto");
            txtPrecoVendaRetalho.requestFocus();
            return false;

        } else if (txtPrecoVendaRetalho.getText().equals("0") && isProduto) {
            JOptionPane.showMessageDialog(null, "Caro usuário " + usuarioLocal.getNome() + " " + usuarioLocal.getSobreNome() + ",  o preço da venda não pode ser igual a zero(0).");
            txtPrecoVendaRetalho.requestFocus();
            return false;
        } else if (cmbUnidade.getSelectedItem().equals("--SELECIONE--")) {
            JOptionPane.showMessageDialog(null, "Pf. Selecione a unidade de medida do produto", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (ivaAplicarJRadioButton.isSelected() && cmbImposto.getSelectedItem().equals("--SELECIONE--")) {
            JOptionPane.showMessageDialog(null, "Pf. Selecione a taxa do IVA", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (cmbImposto.getSelectedItem().equals("--SELECIONE--") && ivaAplicarJRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(null, "Pf. Selecione a taxa do IVA do produto recomendado pela AGT", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (dadosInstituicao.getUsarDoisPrecos().equals("sim")) {
            if (!camposValidosQtdGrossoPrecoGrosso()) {
                return false;
            }

        }

        return true;
    }

    private boolean camposValidosQtdGrossoPrecoGrosso() {
        if (txtQtdGrosso.getText().equals("")) {
            txtQtdGrosso.requestFocus();
            JOptionPane.showMessageDialog(null, "Pf. digite a quantidade grosso.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (Double.parseDouble(txtQtdGrosso.getText()) <= 1) {
            txtQtdGrosso.requestFocus();
            JOptionPane.showMessageDialog(null, "A quantidade grosso, nao pode ser menor do que 2.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;

        } else if (txtPrecoVendaGrosso.getText().equals("")) {
            txtPrecoVendaGrosso.requestFocus();
            JOptionPane.showMessageDialog(null, "Pf. Seleccione o preco de venda a grosso.", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (MetodosUtil.convertToDouble(txtPrecoVendaGrosso.getText()) == 0) {
            txtPrecoVendaGrosso.requestFocus();
            JOptionPane.showMessageDialog(null, "O preco grosso nao pode ser zero..", DVML.DVML_COMERCIAL, JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (MetodosUtil.convertToDouble(txtPrecoVendaGrosso.getText()) < MetodosUtil.convertToDouble(txtPrecoCompra.getText())) {
            txtPrecoVendaGrosso.requestFocus();
            int opcao = JOptionPane.showConfirmDialog(null, "O preco grosso e menor que o preco de compra ..");

            if (opcao == JOptionPane.YES_OPTION) {
                return true;

            }
            return false;
        }

        return true;
    }

    private void popularComponentes() {
        System.err.println("listarMotivos (): " + listarMotivos());
        cmbImposto.setModel(new DefaultComboBoxModel(impostosController.getVector()));
        ivaMotivoJComboBox.setModel(listarMotivos());
        ivaMotivoJComboBox.setSelectedIndex(-1);

        if (!Objects.isNull(dadosInstituicao)) {
            ivaMotivoJComboBox.setSelectedItem(dadosInstituicao.getRegime());
        }

        atualizarIvaForm();

        MetodosUtil.FUNCAO_F1(this, rootPaneCheckingEnabled, DVML.ARMAZEM_DEFAUTL, DVML.JANELA_PRODUTO);

    }

    private DefaultComboBoxModel listarMotivos() {
        DefaultComboBoxModel boxModel = new DefaultComboBoxModel();

        List<ProdutosMotivosIsensao> ProdutosMotivosIsensao = produtosMotivosIsensaoController.listarTodos();

        for (ProdutosMotivosIsensao isensao : ProdutosMotivosIsensao) {
            // boxModel.addElement ( isensao.getRegime ().substring(0, 4) );
            boxModel.addElement(isensao.getRegime());
        }

        return boxModel;
    }

    private static void atualizarIvaForm() {
        try {
            ver_retencao();
            actualizar_campos();

        } catch (Exception e) {
            e.printStackTrace();
        }
        cmbImposto.setVisible(ivaAplicarJRadioButton.isSelected());
        ivaMotivoJComboBox.setVisible(!cmbImposto.isVisible());
        ivaMotivoJLabel.setVisible(ivaMotivoJComboBox.isVisible());

        if (!Objects.isNull(dadosInstituicao)) {
            ivaMotivoJComboBox.setSelectedItem(dadosInstituicao.getRegime());

        }

    }

    public static void actualizar_campos() {

        if (!ivaAplicarJRadioButton.isSelected()) {

            TotalIvaLabel.setVisible(false);
            txtPrecoDeVendaComIva.setVisible(false);
        } else {
            TotalIvaLabel.setVisible(true);
            txtPrecoDeVendaComIva.setVisible(true);

        }

    }

    private void ivaAtualizar(int fkProduto, ProdutosImpostoController produtosImpostoControllerLocal, ProdutosIsentoController produtosIsentoControllerLocal) {

        if (produtosImpostoControllerLocal.existeProdutoImposto(fkProduto)) {
            produtosImpostoControllerLocal.eliminar(fkProduto);
        }

        if (ivaAplicarJRadioButton.isSelected()) {

            ProdutoImposto produtoImposto = new ProdutoImposto();
            produtoImposto.setFkProduto(new TbProduto(fkProduto));
            produtoImposto.setFkImposto(new Imposto(getIdImposto()));
            produtosImpostoControllerLocal.salvar(produtoImposto);

        } else {

            String regimeIsencao = (String) ivaMotivoJComboBox.getSelectedItem();
            ProdutosMotivosIsensao isensao = produtosMotivosIsensaoController.getRegime(regimeIsencao);
            if (!Objects.isNull(isensao)) {
                produtosIsentoControllerLocal.eliminarByIdProduto(fkProduto);
            }
            ProdutoIsento produtoIsento = new ProdutoIsento();
            produtoIsento.setFkProduto(new TbProduto(fkProduto));
            produtoIsento.setFkProdutosMotivosIsensao(new ProdutosMotivosIsensao(isensao.getPkProdutosMotivosIsensao()));
            produtosIsentoControllerLocal.salvar(produtoIsento);

        }
    }

    private String getDesignacaoText() {

        String designacao_produto = txtDesignacao.getText();

        if (designacao_produto.contains("-")) {
            return designacao_produto.replaceAll("-", " ");
        }
        return designacao_produto;
    }

    private void proximo_codigo(ProdutosController produtosControllerLocal) {
        try {
            TbProduto lastProduto = produtosControllerLocal.getLastProduto();
            int codProduto = 1;
            if (Objects.nonNull(lastProduto)) {
                codProduto = produtosControllerLocal.getLastProduto().getCodigo() + 1;
//                txtCodigoBarra.setText( String.valueOf( codProduto ) );
            }

            jLabelCodProduto.setText("Cod. Prod: " + codProduto);
            txtCodigoBarra.setText(String.valueOf(codProduto));
        } catch (Exception e) {
            jLabelCodProduto.setText("Cod. Prod: ");
            txtCodigoBarra.setText("");
        }
    }

    private void proximo_codigo_manual(ProdutosController produtosControllerLocal) {
        try {
            TbProduto lastProduto = produtosControllerLocal.getLastProduto();
            int codProduto = 1;
            if (Objects.nonNull(lastProduto)) {
                codProduto = produtosControllerLocal.getLastProduto().getCodigo() + 1;
//                txtCodigoBarra.setText( String.valueOf( codProduto ) );
            }

            jLabelCodProduto.setText("Cod. Prod: " + codProduto);
            txtCodigoManual.setText(String.valueOf(codProduto));
        } catch (Exception e) {
            jLabelCodProduto.setText("Cod. Prod: ");
            txtCodigoManual.setText("");
        }
    }

    private void setFocus(String focus) {
        if (focus.equalsIgnoreCase("Codigo Interno")) {
            txtCodigoProduto.requestFocus();
            txtCodigoBarra.setEnabled(true);

        } else {
            txtCodigoBarraProcura.requestFocus();

        }
    }

    private boolean registrar_preco(PrecosController precosControllerLocal) {

        if (dadosInstituicao.getUsarDoisPrecos().equals("nao")) {
            txtQtdGrosso.setText(String.valueOf((int) DVML.QTD_DEFAULT));
            txtPrecoVendaGrosso.setText(txtPrecoVendaRetalho.getText());
        }

        boolean isStocavel = ck_produto.isSelected();
        /*PRIMEIRO PRECO RETALHO*/
        setDadosPreco();
        preco.setQtdBaixo(0);
        preco.setQtdAlto(Integer.parseInt(txtQtdGrosso.getText()) - 1);
//        BigDecimal precoVenda = new BigDecimal( MetodosUtil.convertToDouble( isStocavel ? txtPrecoVendaRetalho.getText() : "0.0" ) );
        BigDecimal precoVenda = new BigDecimal(Double.parseDouble(isStocavel ? txtPrecoVendaRetalho.getText() : "0.0"));
//        BigDecimal precoVendaGrosso = new BigDecimal( MetodosUtil.convertToDouble( isStocavel ? txtPrecoVendaGrosso.getText() : "0.0" ) );
        BigDecimal precoVendaGrosso = new BigDecimal(Double.parseDouble(isStocavel ? txtPrecoVendaGrosso.getText() : "0.0"));
//        BigDecimal precoCompra = new BigDecimal( MetodosUtil.convertToDouble( txtPrecoCompra.getText() ) );
        BigDecimal precoCompra = new BigDecimal(Double.parseDouble(txtPrecoCompra.getText()));

        try {
            preco.setPrecoVenda(isStocavel ? precoVenda : precoCompra);
            precosControllerLocal.salvar(preco);
            /*SEGUNDO PRECO GROSSO*/
            setDadosPreco();
//        preco.setQtdBaixo( (int) DVML.QTD_DEFAULT );
            preco.setQtdBaixo(Integer.parseInt(txtQtdGrosso.getText()));
            preco.setQtdAlto(214748364);

            preco.setPrecoVenda(isStocavel ? precoVendaGrosso : precoCompra);
            precosControllerLocal.salvar(preco);

            return true;

        } catch (Exception e) {
        }
        return false;

    }

    private void calcularIva() {

        if (!txtPrecoCompra.getText().equals("")) {
            try {
                if (ck_servico.isSelected()) {
                    txtPrecoVendaRetalho.setText(txtPrecoCompra.getText());
                }

                double qtd = 1d;
                double taxa = Double.parseDouble(cmbImposto.getSelectedItem().toString());
//                double precoLocal = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() );

                double precoLocal = Double.parseDouble(txtPrecoVendaRetalho.getText());
                double desconto = 0d;
                double valorComIVA = FinanceUtils.getValorComIVA(qtd, taxa, precoLocal, desconto);

                DecimalFormat df = new DecimalFormat("#,##0.00");
                txtPrecoDeVendaComIva.setText(df.format(valorComIVA));

//                txtPrecoDeVendaComIva.setText( String.valueOf( valorComIVA ) );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void calcularSemIva() {

        if (!txtPrecoCompra.getText().equals("")) {
            try {
//                double precoLocal = MetodosUtil.convertToDouble( txtPrecoVendaRetalho.getText() );
                double precoLocal = Double.parseDouble(txtPrecoVendaRetalho.getText());
                BigDecimal bd = new BigDecimal(precoLocal).setScale(2, RoundingMode.HALF_UP);

            } catch (Exception e) {
            }

        }

    }

    //----------- evento do teclado ---------------------------------------
    // <editor-fold defaultstate="collapsed" desc="Generated Code">       
    class BuscaProduto implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();
                prefixo = txtCodigoProduto.getText().trim() + key;
                codigo = Integer.parseInt(prefixo);

                try {
                    ver_dados(codigo);
                    ver_dados_produtos(codigo);
                } catch (Exception e) {
                }

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

                prefixo = prefixo.toString().trim().substring(0, prefixo.length());
                codigo = Integer.parseInt(prefixo);

                try {
                    ver_dados(codigo);
                    ver_dados_produtos(codigo);
                } catch (Exception e) {
                    procedimento_limpar();
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }

    //----------- evento do teclado ---------------------------------------
    class PercentagemGanhoRetalho implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();

                prefixo = txtPercentagemGanhoRetalho.getText().trim() + key;
                preco_compra = MetodosUtil.convertToDouble(txtPrecoCompra.getText());
                percentagem = Double.parseDouble(prefixo) / 10;
                preco_venda = ((percentagem * preco_compra) / 100) + preco_compra;
                txtPrecoVendaRetalho.setText(String.valueOf(MetodosUtil.preco_venda(percentagem, preco_compra)));

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {

                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    preco_compra = MetodosUtil.convertToDouble(txtPrecoCompra.getText());
                    percentagem = Double.parseDouble(prefixo);
                    preco_venda = ((percentagem * preco_compra) / 100) + preco_compra;
                    txtPrecoVendaRetalho.setText(String.valueOf(MetodosUtil.preco_venda(percentagem, preco_compra)));

                } catch (Exception e) {
                    txtPrecoVendaRetalho.setText("");
                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }

    //----------- evento do teclado ---------------------------------------
    class PrecoVendaPercentagem implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();

                System.err.println("KEY: " + key);
                prefixo = txtPrecoVendaRetalho.getText().trim() + key;
//                preco_venda = MetodosUtil.convertToDouble( prefixo );
//                preco_venda = preco_venda * 10;
//                preco_compra = MetodosUtil.convertToDouble( txtPrecoCompra.getText() );
//                percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
//                BigDecimal bigDecimal = new BigDecimal( percentagem / 10 );
//                bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
//                txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );

                preco_venda = MetodosUtil.convertToDouble(prefixo) * 10;
                preco_compra = MetodosUtil.convertToDouble(txtPrecoCompra.getText());
                txtPercentagemGanhoRetalho.setText(CfMethods.formatarComoPorcoes(MetodosUtil.percentagemGanho(preco_compra, preco_venda)));

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {

                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
//                    char key = evt.getKeyChar();
//                    prefixo = txtPrecoVenda.getText().trim() + key;
                    prefixo = txtPrecoVendaRetalho.getText().trim();
                    System.out.println("PREFIXO: " + prefixo);

                    preco_venda = MetodosUtil.convertToDouble(prefixo.replace(",", "."));

//                    preco_venda = MetodosUtil.convertToDouble( prefixo ) * 10;
                    preco_compra = MetodosUtil.convertToDouble(txtPrecoCompra.getText());
                    txtPercentagemGanhoRetalho.setText(CfMethods.formatarComoPorcoes(MetodosUtil.percentagemGanho(preco_compra, preco_venda)));

                    System.out.println("PRECO VENDA BACK " + preco_venda);
//                    preco_venda = preco_venda * 10;
//                    percentagem = ( ( preco_venda - preco_compra ) * 100 ) / preco_compra;
//                    BigDecimal bigDecimal = new BigDecimal( percentagem );
//                    bigDecimal = bigDecimal.setScale( 2, BigDecimal.ROUND_UP );
//                    txtPercentagemGanhoRetalho.setText( String.valueOf( bigDecimal ) );

                } catch (Exception e) {
                    txtPercentagemGanhoRetalho.setText("");

                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }

    class PrecoCompraPercentagem implements KeyListener {

        String prefixo = "";

        public void keyPressed(KeyEvent evt) {

            double preco_compra = 0;
            double percentagem = 0;
            double preco_venda = 0;

            if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                char key = evt.getKeyChar();

                System.err.println("KEY: " + key);
                prefixo = txtPrecoCompra.getText().trim() + key;
                preco_compra = MetodosUtil.convertToDouble(prefixo);
                preco_compra = preco_compra * 10;

                preco_venda = MetodosUtil.convertToDouble((txtPrecoVendaRetalho.getText().equals("") || txtPrecoVendaRetalho.getText() == null) ? "0.0" : txtPrecoVendaRetalho.getText());

                percentagem = ((preco_venda - preco_compra) * 100) / preco_compra;
                BigDecimal bigDecimal = new BigDecimal(percentagem / 10);
                bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
                txtPercentagemGanhoRetalho.setText(String.valueOf(bigDecimal));

            } else if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                try {

                    prefixo = prefixo.toString().trim().substring(0, prefixo.length() - 1);
                    System.out.println("PREFIXO: " + prefixo);
                    preco_compra = MetodosUtil.convertToDouble(prefixo);
                    preco_compra = preco_compra * 10;

                    percentagem = ((preco_venda - preco_compra) * 100) / preco_compra;
                    BigDecimal bigDecimal = new BigDecimal(percentagem);
                    bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);
                    txtPercentagemGanhoRetalho.setText(String.valueOf(bigDecimal));

                } catch (Exception e) {
                    txtPercentagemGanhoRetalho.setText("");

                }

            }
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
        }
    }
    // </editor-fold>

    public static int getCodigoArmazem(String designacao) {
        try {
            return armazensController.getArmazemByDesignacao(designacao).getCodigo();
        } catch (Exception e) {
            return 0;
        }
    }

    private static void actualizarRetencaoForm() {

        if (retencaoAplicarJRadioButton.isSelected()) {
            retencao = (Retencao) retencaoController.findById(1);
            retencaoTaxaJTextField.setText(MetodosUtil.formatarComoPercentagem(retencao.getTaxa()));
        } else {
            retencao = (Retencao) retencaoController.findById(2);
            retencaoZeroTaxaJTextField.setText(MetodosUtil.formatarComoPercentagem(0.0));
        }
        retencaoTaxaJTextField.setVisible(retencaoAplicarJRadioButton.isSelected());
        retencaoTaxaJLabel.setVisible(retencaoTaxaJTextField.isVisible());
        retencaoZeroTaxaJTextField.setVisible(!retencaoTaxaJTextField.isVisible());

    }

    public static void ver_retencao() {

        if (ck_servico.isSelected()) {
            jPanel_retencao.setVisible(true);
        } else {
            jPanel_retencao.setVisible(false);
        }

    }

    public static void nao_ver_retencao() {
        if (!ck_servico.isSelected()) {
            jPanel_retencao.setVisible(false);
        }

    }

    private void setRegime(String regime) {
        if (regime.equalsIgnoreCase("Regime Geral")) {
            ivaAplicarJRadioButton.setSelected(true);
            txtPrecoDeVendaComIva.setVisible(true);
            TotalIvaLabel.setVisible(true);

        } else {
            ivaNaoAplicarJRadioButton.setSelected(true);
            txtPrecoDeVendaComIva.setVisible(false);
            TotalIvaLabel.setVisible(false);

        }
    }

    public static void bloquearCampoCodBarra() {

        txtCodigoBarra.setEnabled(true);

    }

    private void setFocusCodigo(String focus) {
        if (focus.equalsIgnoreCase("Codigo Interno")) {

            txtCodigoProduto.requestFocus();

        } else {

            txtCodigoBarra.requestFocus();

        }
    }

    public static void getDescricao() {

        txtDesignacao.requestFocus();

    }

    public static void fazerBackupAgora() {
        String data = new SimpleDateFormat(YYYYMMDD_HHMMSS).format(new Date());
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows(rodar_camando, true);

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println("Backup realizado com sucesso! ");

    }

    public void configurar_dois_precos() {
        try {
            if (dadosInstituicao.getUsarDoisPrecos().equals("nao")) {
                jLabel1.setVisible(false);
                txtQtdGrosso.setVisible(false);
                lbPrecoVenda1.setVisible(false);
                txtPrecoVendaGrosso.setVisible(false);

            } else {
                jLabel1.setVisible(true);
                txtQtdGrosso.setVisible(true);
                lbPrecoVenda1.setVisible(true);
                txtPrecoVendaGrosso.setVisible(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setActivarNegocio(String negocio) {
        if (negocio.equalsIgnoreCase("Comercial")) {

            ck_produto.setSelected(true);
            ck_servico.setSelected(false);
            lbTicket.setVisible(false);
            rbNaoEnviarCozinha.setVisible(false);
            rbEnviarCozinha.setVisible(false);
            rbEnviarSala.setVisible(false);
            btnAssociar.setVisible(false);
            servico_produto();
        } else if (negocio.equalsIgnoreCase("Transportes")) {

            ck_produto.setSelected(true);
            ck_servico.setSelected(false);
            lbTicket.setVisible(false);
            rbNaoEnviarCozinha.setVisible(false);
            rbEnviarCozinha.setVisible(false);
            rbEnviarSala.setVisible(false);
            btnAssociar.setVisible(false);
            servico_produto();
        } else if (negocio.equalsIgnoreCase("Restaurante")) {

            ck_produto.setSelected(true);
            ck_servico.setSelected(false);
//            lbTicket.setVisible( false );
            btnAssociar.setVisible(false);
//            rbEnviarCozinha.setVisible(false );
            servico_produto();
        } else if (negocio.equalsIgnoreCase("Farmacia")) {

            ck_produto.setSelected(true);
            ck_servico.setSelected(false);
            lbTicket.setVisible(false);
            rbNaoEnviarCozinha.setVisible(false);
            rbEnviarSala.setVisible(false);
            rbEnviarCozinha.setVisible(false);
            btnAssociar.setVisible(false);
            servico_produto();
        } else if (negocio.equalsIgnoreCase("Lavandaria")) {

            ck_produto.setSelected(false);
            ck_produto.setVisible(false);
            ck_servico.setSelected(true);
//            lbTipoProduto1.setVisible( false );
//            rbNaoEnviarCozinha.setVisible( false );
//            rbEnviarCozinha.setVisible( false );
            servico_produto();
//            ck_servico.setSelected( true);
        } else {

            ck_servico.setSelected(true);
            ck_produto.setSelected(false);
            lbTicket.setVisible(false);
            rbNaoEnviarCozinha.setVisible(false);
            rbEnviarCozinha.setVisible(false);
            rbEnviarSala.setVisible(false);
            btnAssociar.setVisible(false);
            servico_produto();
//            ivaNaoAplicarJRadioButton.setSelected( true);
            ver_retencao();
            actualizarRetencaoForm();
//            servico_produto();
//            txtDesignacao.requestFocus();

        
    

    ////        lbCusto.setText("Preço");
//            mostrarPreco();

        }
    }

    private void setActivarJanelaServico(String negocio) {
        try {
            if (negocio.equalsIgnoreCase("Manter fixa")) {
                rbJanelaServico.setSelected(true);
//            alterar_estrutura_janela();
            } else {
                rbJanelaServico.setSelected(false);
//            alterar_estrutura_janela();
            }
        } catch (Exception e) {
        }

    }

    private String getCozinha() {
//        return rbComercial.isSelected() ? "Comercial" : "Oficina";
        return rbNaoEnviarCozinha.isSelected() ? "Nao Enviar Ticket"
                : rbEnviarCozinha.isSelected() ? "Enviar Ticket"
                : "Enviar Sala";

    }

    private String getCozinha1() {
        return rbNaoEnviarCozinha.isSelected() ? "Nao Enviar Ticket" : "Enviar Ticket";
    }

    private static void setCozinha(String cozinha) {
        if (cozinha.equalsIgnoreCase("Enviar Ticket")) {
            rbEnviarCozinha.setSelected(true);
            rbNaoEnviarCozinha.setSelected(false);
            rbEnviarSala.setSelected(false);
        } else if (cozinha.equalsIgnoreCase("Nao Enviar Ticket")) {
            rbNaoEnviarCozinha.setSelected(true);
            rbEnviarCozinha.setSelected(false);
            rbEnviarSala.setSelected(false);
        } else {
            rbNaoEnviarCozinha.setSelected(false);
            rbEnviarCozinha.setSelected(false);
            rbEnviarSala.setSelected(true);
        }
    }

    private static void setButtonAssociar() {
        boolean status = !(txtCodigoProduto.getText().equals(""));
        btnAssociar.setVisible(status);

    }

    private void novo() {

        limpar_esquerdo();
        limpar_direito();
        btnSalvar.setEnabled(true);
        btnAlterar2.setEnabled(false);
        //actualizar
        cmbLocal.setModel(new DefaultComboBoxModel(localController.getVector()));
        cmbUnidade.setModel(new DefaultComboBoxModel(unidadesController.getVector()));
        txtCodigoProduto.setText("");
        txtCodigoBarraProcura.setText("");
        txtCodigoManualProcura.setText("");

    }

    private void setWindowsListener() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
//                proximo_codigo( produtosController );
            }

        });

    }

    private void activarCampoPreco() {
        TbUsuario usuario = (TbUsuario) usuariosController.findById(idUser);

        if (usuario.getIdTipoUsuario().getIdTipoUsuario() == 1) {
            txtPrecoVendaRetalho.setEnabled(true);
            txtPrecoVendaGrosso.setEnabled(true);
            txtPrecoCompra.setEnabled(true);
        } else {
            txtPrecoCompra.setEnabled(false);
            txtPrecoVendaRetalho.setEnabled(false);
            txtPrecoVendaGrosso.setEnabled(false);
        }
    }

    private static void procedimento_registrar_stock(int idProduto, int idArmazem) {
        if (!stoksController.existe_stock(idProduto, idArmazem)) {
            TbProduto produto_local = produtosController.findByCod(idProduto);
            boolean registrar_stock = registrar_stock(idArmazem,
                    produto_local,
                    0,
                    0,
                    0,
                    stoksController);
            if (registrar_stock) {
                MovimentacaoController.registrarMovimento(
                        idProduto,
                        idArmazem,
                        idUser,
                        new BigDecimal(0),
                        "CRIAÇÃO DE ARTIGO",
                        "ENTRADA",
                        conexao
                );

            }
        }
    }

    private static boolean registrar_stock(int idArmazem,
            TbProduto produto_local,
            double qtd,
            double qtdCritica,
            double qtdbaixa,
            StoksController stocksControllerLocal
    ) {
        TbStock stockLocal = new TbStock();
        stockLocal.setDataEntrada(new Date());
        stockLocal.setQuantidadeExistente(qtd);
        stockLocal.setStatus("true");
        stockLocal.setPrecoVenda(new BigDecimal(MetodosUtil.convertToDouble("0.0")));
        stockLocal.setPrecoVendaGrosso(new BigDecimal(stockLocal.getPrecoVenda().doubleValue()));
        stockLocal.setQtdGrosso(DVML.QTD_DEFAULT);
        stockLocal.setQuantCritica((int) qtdCritica);
        stockLocal.setQuantBaixa((int) qtdbaixa);
        stockLocal.setQuantidadeAntiga(0d);
        stockLocal.setCodArmazem(new TbArmazem(idArmazem));
        stockLocal.setCodProdutoCodigo(new TbProduto(produto_local.getCodigo()));
        System.out.println("Produto Registrado no Stock.");
        return stocksControllerLocal.salvar(stockLocal);

    }

    private void configurarControloLotes() {

        boolean farmacia = dadosInstituicao != null
                && "Farmacia".equalsIgnoreCase(
                        dadosInstituicao.getNegocio()
                );

        if (farmacia) {
            // Farmácia: permitir escolher se o produto tem lotes.
            jRadioButtonSemLotes.setEnabled(true);
            jRadioButtonComLotes.setEnabled(true);
            jLabel3.setVisible(true);

        } else {
            // Outros negócios: desativar completamente o controlo de lotes.
            jRadioButtonSemLotes.setSelected(true);
            jRadioButtonComLotes.setSelected(false);

            jRadioButtonSemLotes.setEnabled(false);
            jRadioButtonComLotes.setEnabled(false);
            jLabel3.setVisible(false);
        }
    }


}
