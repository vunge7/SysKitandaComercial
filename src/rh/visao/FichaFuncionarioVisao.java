/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rh.visao;

//import com.sun.media.sound.InvalidFormatException;
import controlador.exceptions.NonexistentEntityException;
import controller.AnexoController;
import dao.AgregadoFamiliarDao;
import dao.AnexosDao;
import dao.BancoDao;
import dao.ContaDao;
import dao.DepartamentoDao;
import dao.EmpresaDao;
import dao.EspecialidadeDao;
import dao.EstadoCivilDao;
import dao.FuncaoDao;
import dao.FuncionarioDao;
import dao.GrauAcademicoDao;
import dao.ItemSubsidioFuncionarioDao;
import dao.ModalidadeDao;
import dao.SalarioDao;
import dao.StatusDao;
import dao.SubsidioDao;
import dao.UsuarioDao;
import entity.AgregadoFamiliar;
import entity.Anexos;
import entity.Empresa;
import entity.Modalidade;
import entity.TbConta;
import entity.TbDepartamento;
import entity.TbEspecialidade;
import entity.TbEstadoCivil;
import entity.TbFuncao;
import entity.TbFuncionario;
import entity.TbGrauAcademico;
import entity.TbItemSubsidiosFuncionario;
import entity.TbSalario;
import entity.TbStatus;
import entity.TbSubsidios;
import entity.TbUsuario;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethodsSwing;
import util.BDConexao;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import static util.MetodosUtil.*;
import static util.DVML.*;
import util.ManipularImagemUtil;
import util.MetodosUtil;
import static util.MetodosUtil.limparCampoForm;
import util.PictureChooserFuncionario;
import util.validacao.ficha_funcionario.IdentificacaoValidacaoUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FichaFuncionarioVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private int idUsuario = 0;
    private int idFuncionario = 0;
    private int idEmpresa = 0;
    private static GrauAcademicoDao grauAcademicoDao = new GrauAcademicoDao( emf );
    private final DepartamentoDao departamentoDao = new DepartamentoDao( emf );
    private final EmpresaDao empresaDao = new EmpresaDao( emf );
    private final UsuarioDao usuarioDao = new UsuarioDao( emf );
    private final EspecialidadeDao especialidadeDao = new EspecialidadeDao( emf );
    private final ItemSubsidioFuncionarioDao itemSubsidioDao = new ItemSubsidioFuncionarioDao( emf );
    private final SubsidioDao subsidioDao = new SubsidioDao( emf );
    private final SalarioDao salarioDao = new SalarioDao( emf );
    private final StatusDao statusDao = new StatusDao( emf );
    private final EstadoCivilDao estadoCivilDao = new EstadoCivilDao( emf );
    private final FuncaoDao funcaoDao = new FuncaoDao( emf );
    private final ModalidadeDao modalidadeDao = new ModalidadeDao( emf );
    private final AgregadoFamiliarDao agregadoFamiliarDao = new AgregadoFamiliarDao( emf );
    private final FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private final ContaDao contaDao = new ContaDao( emf );
    private final BancoDao bancoDao = new BancoDao( emf );
    private IdentificacaoValidacaoUtil ivu = new IdentificacaoValidacaoUtil( funcionarioDao );
//    private final StatusDao statusDao = new StatusDao( emf );
    private final AnexosDao anexosDao = new AnexosDao( emf );
    private TbFuncionario funcionario;
    private TbUsuario usuario;
    private BufferedImage imagem;
    private ImageIcon imageIcon;
    private BDConexao conexao;
    private TbConta conta;
    private TbSalario salario;
    private String log = "";
    private static AnexoController anexoController;
    private final PictureChooserFuncionario image_View = new PictureChooserFuncionario();
//    private DefaultListModel listModelFuncionario = new DefaultListModel();
    private DefaultListModel listModelAnexo = new DefaultListModel();
//    private List<TbFuncionario> lista_Funcionario = new ArrayList<TbFuncionario>();
//    private DefaultListModel lista_model = new DefaultListModel();
    private static String EXTENSAO = "jpg";

    public FichaFuncionarioVisao( int idUsuario, int idEmpresa, BDConexao conexao )
    {
        initComponents();
        setLocationRelativeTo( null );

        imageIcon = new ImageIcon();
        imageIcon = new ImageIcon();
        setMenuPopout();
        this.conexao = conexao;
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
//        funcionarioController = new FuncionarioController( conexao );;
        anexoController = new AnexoController( conexao );

        init();
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

        buttonGroupSexo = new javax.swing.ButtonGroup();
        buttonGroupEstadoCivil = new javax.swing.ButtonGroup();
        buttonGroupSegurançaSocial = new javax.swing.ButtonGroup();
        buttonGroupNIF = new javax.swing.ButtonGroup();
        buttonGroupFormaPagamento = new javax.swing.ButtonGroup();
        buttonDescontoSegurancaSocial = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lb_anexo_doc = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroFuncionarioPesquisa = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lb_nome_funcionario = new javax.swing.JLabel();
        btn_salvar = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cmbFuncionario = new javax.swing.JComboBox();
        txtIniciaisNome = new javax.swing.JTextField();
        painel = new javax.swing.JPanel();
        painel_principal = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lbDataEmitidoDoc = new javax.swing.JLabel();
        dcDataValidadeDoc = new com.toedter.calendar.JDateChooser();
        lbDocID = new javax.swing.JLabel();
        dcDataEmitidoDoc = new com.toedter.calendar.JDateChooser();
        lbDataValidadeDoc = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbDocID = new javax.swing.JComboBox<>();
        lbAjudaDataEmitido = new javax.swing.JLabel();
        lbAjudaDataValidade = new javax.swing.JLabel();
        txtDocID = new javax.swing.JFormattedTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNumeroFuncionario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rbSexoMasculino = new javax.swing.JRadioButton();
        rbSexoFemenino = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        rbSolteiro = new javax.swing.JRadioButton();
        rbCasado = new javax.swing.JRadioButton();
        rbViuvo = new javax.swing.JRadioButton();
        rbDivorciado = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        dcDataNascimento = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNomeCompleto4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMorada = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ftfTelefone1 = new javax.swing.JFormattedTextField();
        lbTelefone2 = new javax.swing.JLabel();
        ftfTelefone2 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        txtNomeCompleto = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_agregado_familiar = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jsSalarioBase = new javax.swing.JSpinner();
        jsSubsidioFeria = new javax.swing.JSpinner();
        jsSubsidioNatal = new javax.swing.JSpinner();
        jsSubsidioOutros = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        rbNIFSim = new javax.swing.JRadioButton();
        rbNIFNao = new javax.swing.JRadioButton();
        txtNumeroSegurancaSocial = new javax.swing.JTextField();
        lbNumeroSegurancaSocial = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        rbSegurancaoSocialNao = new javax.swing.JRadioButton();
        rbSegurancaoSocialSim = new javax.swing.JRadioButton();
        txtNIF = new javax.swing.JTextField();
        lbNumeroNIF = new javax.swing.JLabel();
        rbDescontoSeguracaoSocialSIM = new javax.swing.JRadioButton();
        rbDescontoSeguracaoSocialNao = new javax.swing.JRadioButton();
        lbDescontoSegurancaSocial = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabela_subsidio = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        cmbSubsidios = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jsValorSubsidio = new javax.swing.JSpinner();
        jButton12 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        cmbGrauAcademico = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        cmbEspecialidade = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        cmbDepartamento = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        cmbFuncao = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        cmbContrato = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        lbDataEmitidoDoc1 = new javax.swing.JLabel();
        dcDataAdmissao = new com.toedter.calendar.JDateChooser();
        lbAjudaDataEmitido1 = new javax.swing.JLabel();
        lbDuracao = new javax.swing.JLabel();
        cmbDuracao = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        cmbRegime = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtNumeroConta = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        rbFPTransferencia = new javax.swing.JRadioButton();
        rbFPDeposito = new javax.swing.JRadioButton();
        rbFPTCash = new javax.swing.JRadioButton();
        ftfNumeroIBAN = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        lbDataEmitidoDoc2 = new javax.swing.JLabel();
        dcDataEmitidoDoc2 = new com.toedter.calendar.JDateChooser();
        lbAjudaDataEmitido2 = new javax.swing.JLabel();
        lbDataEmitidoDoc3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel15 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        txtNomeAnexo = new javax.swing.JTextField();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        lbEmpresa = new javax.swing.JLabel();

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_anexo_doc.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
        lb_anexo_doc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_anexo_doc.setText("..");
        jScrollPane5.setViewportView(lb_anexo_doc);

        jPanel16.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 860, 410));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("FICHA DO FUNCIONÁRIO");

        txtNumeroFuncionarioPesquisa.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 14)); // NOI18N
        txtNumeroFuncionarioPesquisa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNumeroFuncionarioPesquisaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel4.setText("Pesq. por Nº Func:*");

        lb_nome_funcionario.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lb_nome_funcionario.setText("Nome do Funcionário");

        btn_salvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/save_32x32.png"))); // NOI18N
        btn_salvar.setText("Salvar");
        btn_salvar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_salvarActionPerformed(evt);
            }
        });

        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/editar/edit_32x32.png"))); // NOI18N
        btn_actualizar.setText("Actualizar");
        btn_actualizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_actualizarActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - CINZA/Logout-32x32.png"))); // NOI18N
        jButton2.setText("Sair");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        cmbFuncionario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFuncionarioActionPerformed(evt);
            }
        });

        txtIniciaisNome.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisNomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroFuncionarioPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_nome_funcionario, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtIniciaisNome, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_salvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_nome_funcionario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_actualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_salvar)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNumeroFuncionarioPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIniciaisNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        painel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 1, 13))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13)); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Documento de Idêntificação", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        lbDataEmitidoDoc.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDataEmitidoDoc.setText("Emitido aos:");

        dcDataValidadeDoc.setAutoscrolls(true);
        dcDataValidadeDoc.setDateFormatString("dd-MM-yyyy");
        dcDataValidadeDoc.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        lbDocID.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDocID.setText("Nº Doc. Idêntificação:");

        dcDataEmitidoDoc.setDateFormatString("dd-MM-yyyy");
        dcDataEmitidoDoc.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        lbDataValidadeDoc.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDataValidadeDoc.setText("Validade:");

        jLabel9.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel9.setText("Doc. Idêntificação:");

        cmbDocID.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbDocID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbDocID.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDocIDActionPerformed(evt);
            }
        });

        lbAjudaDataEmitido.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        lbAjudaDataEmitido.setText("(Dia-Mês-Ano)");

        lbAjudaDataValidade.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        lbAjudaDataValidade.setText("(Dia-Mês-Ano)");

        txtDocID.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDataEmitidoDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbDataValidadeDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addComponent(dcDataValidadeDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbAjudaDataValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addComponent(dcDataEmitidoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbAjudaDataEmitido, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(106, 106, 106))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbDocID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbDocID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDocID))
                        .addGap(108, 108, 108))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDocID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDocID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDocID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lbDataEmitidoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataEmitidoDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbAjudaDataEmitido, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcDataValidadeDoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbDataValidadeDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAjudaDataValidade, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados Pessoais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel2.setText("Nº Func:*");

        txtNumeroFuncionario.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        txtNumeroFuncionario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNumeroFuncionarioActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel7.setText("Sexo:*");

        buttonGroupSexo.add(rbSexoMasculino);
        rbSexoMasculino.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSexoMasculino.setSelected(true);
        rbSexoMasculino.setText("Masculino");

        buttonGroupSexo.add(rbSexoFemenino);
        rbSexoFemenino.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSexoFemenino.setText("Femenino");

        jLabel8.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel8.setText("Estado Civil:*");

        buttonGroupEstadoCivil.add(rbSolteiro);
        rbSolteiro.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSolteiro.setSelected(true);
        rbSolteiro.setText("Solteiro(a)");

        buttonGroupEstadoCivil.add(rbCasado);
        rbCasado.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbCasado.setText("Casado(a)");

        buttonGroupEstadoCivil.add(rbViuvo);
        rbViuvo.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbViuvo.setText("Viúvo(a)");

        buttonGroupEstadoCivil.add(rbDivorciado);
        rbDivorciado.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbDivorciado.setText("Divórciado(a)");

        jLabel6.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel6.setText("Data de Nascimento:*");

        dcDataNascimento.setDateFormatString("dd-MM-yyyy");
        dcDataNascimento.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        jLabel16.setText("(Dia-Mês-Ano)");

        jLabel13.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel13.setText("Cidade:");

        jLabel14.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/find/seach_32x32.png"))); // NOI18N

        txtNomeCompleto4.setEditable(false);
        txtNomeCompleto4.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel5.setText("Morada:*");

        txtMorada.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel18.setText("Telefone 1:*");

        try
        {
            ftfTelefone1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(+244) ###-###-###")));
        } catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        ftfTelefone1.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        lbTelefone2.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbTelefone2.setText("Telefone 2:");

        try
        {
            ftfTelefone2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(+244) ###-###-###")));
        } catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        ftfTelefone2.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel17.setText("Nome completo:*");

        txtNomeCompleto.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtNomeCompleto4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMorada, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addComponent(ftfTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lbTelefone2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ftfTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(rbSexoMasculino)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbSexoFemenino))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(rbSolteiro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbCasado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbViuvo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbDivorciado))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(dcDataNascimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(190, 190, 190))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbSexoMasculino)
                    .addComponent(rbSexoFemenino))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbSolteiro)
                    .addComponent(rbCasado)
                    .addComponent(rbViuvo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbDivorciado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dcDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNomeCompleto4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMorada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ftfTelefone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftfTelefone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Abono Familiar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        tabela_agregado_familiar.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 11)); // NOI18N
        tabela_agregado_familiar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Nome do Filho", "Data de Nascimento", "Idade", "Valor do Abono", "Acção"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, true, false
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
        tabela_agregado_familiar.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tabela_agregado_familiarMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabela_agregado_familiar);
        if (tabela_agregado_familiar.getColumnModel().getColumnCount() > 0)
        {
            tabela_agregado_familiar.getColumnModel().getColumn(0).setMinWidth(300);
            tabela_agregado_familiar.getColumnModel().getColumn(2).setMaxWidth(70);
        }

        jButton5.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/add_16x16.png"))); // NOI18N
        jButton5.setText("Novo Membro");
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painel_principal.addTab("Identificação", jPanel3);

        jLabel3.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel3.setText("Salário Base:*");

        jLabel10.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel10.setText("Percentagem Subsídio de Féria (%)");

        jLabel11.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel11.setText("Percentagem Subsídio de Natal (%)");

        jLabel12.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel12.setText("Outros (%)");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsSalarioBase, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jsSubsidioOutros, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jsSubsidioNatal, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jsSubsidioFeria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(634, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsSalarioBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsSubsidioFeria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsSubsidioNatal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsSubsidioOutros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painel_principal.addTab("Salário", jPanel4);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel15.setText("Segurança Social:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 24));

        buttonGroupNIF.add(rbNIFSim);
        rbNIFSim.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbNIFSim.setText("Sim");
        rbNIFSim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbNIFSimActionPerformed(evt);
            }
        });
        jPanel5.add(rbNIFSim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, -1, -1));

        buttonGroupNIF.add(rbNIFNao);
        rbNIFNao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbNIFNao.setText("Não");
        rbNIFNao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbNIFNaoActionPerformed(evt);
            }
        });
        jPanel5.add(rbNIFNao, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, -1, -1));

        txtNumeroSegurancaSocial.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jPanel5.add(txtNumeroSegurancaSocial, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 320, -1));

        lbNumeroSegurancaSocial.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbNumeroSegurancaSocial.setText("Nº Cartão INSS");
        jPanel5.add(lbNumeroSegurancaSocial, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 170, 24));

        jLabel19.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel19.setText("NIF");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 142, 24));

        buttonGroupSegurançaSocial.add(rbSegurancaoSocialNao);
        rbSegurancaoSocialNao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSegurancaoSocialNao.setText("Não");
        rbSegurancaoSocialNao.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                rbSegurancaoSocialNaoMouseClicked(evt);
            }
        });
        rbSegurancaoSocialNao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbSegurancaoSocialNaoActionPerformed(evt);
            }
        });
        jPanel5.add(rbSegurancaoSocialNao, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

        buttonGroupSegurançaSocial.add(rbSegurancaoSocialSim);
        rbSegurancaoSocialSim.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbSegurancaoSocialSim.setText("Sim");
        rbSegurancaoSocialSim.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                rbSegurancaoSocialSimMouseClicked(evt);
            }
        });
        rbSegurancaoSocialSim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbSegurancaoSocialSimActionPerformed(evt);
            }
        });
        rbSegurancaoSocialSim.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                rbSegurancaoSocialSimKeyPressed(evt);
            }
        });
        jPanel5.add(rbSegurancaoSocialSim, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, -1));

        txtNIF.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jPanel5.add(txtNIF, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 320, -1));

        lbNumeroNIF.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbNumeroNIF.setText("Nº Cartão Contribuinte");
        jPanel5.add(lbNumeroNIF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 170, 24));

        buttonDescontoSegurancaSocial.add(rbDescontoSeguracaoSocialSIM);
        rbDescontoSeguracaoSocialSIM.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbDescontoSeguracaoSocialSIM.setSelected(true);
        rbDescontoSeguracaoSocialSIM.setText("Sim");
        rbDescontoSeguracaoSocialSIM.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbDescontoSeguracaoSocialSIMActionPerformed(evt);
            }
        });
        jPanel5.add(rbDescontoSeguracaoSocialSIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 40, -1, -1));

        buttonDescontoSegurancaSocial.add(rbDescontoSeguracaoSocialNao);
        rbDescontoSeguracaoSocialNao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbDescontoSeguracaoSocialNao.setText("Não");
        rbDescontoSeguracaoSocialNao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbDescontoSeguracaoSocialNaoActionPerformed(evt);
            }
        });
        jPanel5.add(rbDescontoSeguracaoSocialNao, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 40, -1, -1));

        lbDescontoSegurancaSocial.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDescontoSegurancaSocial.setText("Desconto Segurança:*");
        jPanel5.add(lbDescontoSegurancaSocial, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, -1, 20));

        painel_principal.addTab("Fiscalidade", jPanel5);

        tabela_subsidio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Subsídio", "Valor", "Acção"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
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
        jScrollPane3.setViewportView(tabela_subsidio);
        if (tabela_subsidio.getColumnModel().getColumnCount() > 0)
        {
            tabela_subsidio.getColumnModel().getColumn(0).setMinWidth(300);
        }

        jLabel21.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel21.setText("Subsídio:");

        cmbSubsidios.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbSubsidios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbSubsidios.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbSubsidiosActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel22.setText("Valor:");

        jButton6.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/confirmacao.png"))); // NOI18N
        jButton6.setText("Adicionar");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });

        jsValorSubsidio.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jsValorSubsidioMouseClicked(evt);
            }
        });
        jsValorSubsidio.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jsValorSubsidioKeyPressed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2934_32x32.png"))); // NOI18N
        jButton12.setText("Remover");
        jButton12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSubsidios, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jsValorSubsidio, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(313, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSubsidios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsValorSubsidio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        painel_principal.addTab("Subsídios", jPanel6);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel23.setText("Grau Acadêmico:");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 27, 140, 30));

        cmbGrauAcademico.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbGrauAcademico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbGrauAcademico.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbGrauAcademicoActionPerformed(evt);
            }
        });
        jPanel7.add(cmbGrauAcademico, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 240, 30));

        jLabel24.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel24.setText("Especialidade:");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 106, 24));

        cmbEspecialidade.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbEspecialidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbEspecialidade.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbEspecialidadeActionPerformed(evt);
            }
        });
        jPanel7.add(cmbEspecialidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 208, 30));

        jLabel25.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel25.setText("Departamento:");
        jPanel7.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 97, 140, 40));

        cmbDepartamento.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbDepartamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbDepartamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDepartamentoActionPerformed(evt);
            }
        });
        jPanel7.add(cmbDepartamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 240, 30));

        jLabel26.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel26.setText("Função:");
        jPanel7.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 106, 24));

        cmbFuncao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbFuncao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbFuncao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFuncaoActionPerformed(evt);
            }
        });
        jPanel7.add(cmbFuncao, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, 208, 30));

        jLabel27.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel27.setText("Contrato:");
        jPanel7.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 199, 120, 30));

        cmbContrato.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Determinado", "Indeterminado" }));
        cmbContrato.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbContratoActionPerformed(evt);
            }
        });
        jPanel7.add(cmbContrato, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 240, 30));

        cmbStatus.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Activo", "Desactivo" }));
        cmbStatus.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbStatusActionPerformed(evt);
            }
        });
        jPanel7.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 30, 200, -1));

        jLabel28.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel28.setText("Status:");
        jPanel7.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 30, 70, 24));

        lbDataEmitidoDoc1.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDataEmitidoDoc1.setText("Data de Admissão:");
        jPanel7.add(lbDataEmitidoDoc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 447, 127, 24));

        dcDataAdmissao.setDateFormatString("dd-MM-yyyy");
        dcDataAdmissao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jPanel7.add(dcDataAdmissao, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 442, 136, -1));

        lbAjudaDataEmitido1.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        lbAjudaDataEmitido1.setText("(Dia-Mês-Ano)");
        jPanel7.add(lbAjudaDataEmitido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 445, 65, 26));

        lbDuracao.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDuracao.setText("Duração:");
        jPanel7.add(lbDuracao, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 106, 24));

        cmbDuracao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbDuracao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "1 Mês", "3 Meses", "6 Meses", "12 Meses" }));
        cmbDuracao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbDuracaoActionPerformed(evt);
            }
        });
        jPanel7.add(cmbDuracao, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 200, 210, 30));

        jLabel34.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel34.setText("Regime:");
        jPanel7.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 110, 70, 24));

        cmbRegime.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        cmbRegime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione --", "Bilhete de Idêntidade", "Passaporte", "Cédula", "Outros" }));
        cmbRegime.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbRegimeActionPerformed(evt);
            }
        });
        jPanel7.add(cmbRegime, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 110, 200, -1));

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton13.setText("+");
        jButton13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, 50, 30));

        jButton14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton14.setText("+");
        jButton14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 110, 50, 30));

        jButton16.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton16.setText("+");
        jButton16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 50, 30));

        jButton17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton17.setText("+");
        jButton17.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 50, 30));

        painel_principal.addTab("Descrição", jPanel7);

        jLabel30.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel30.setText("Nº de Conta:");

        txtNumeroConta.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel31.setText("IBAN:");

        jLabel32.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel32.setText("Forma de Pagamento");

        buttonGroupFormaPagamento.add(rbFPTransferencia);
        rbFPTransferencia.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbFPTransferencia.setSelected(true);
        rbFPTransferencia.setText("Transferência");

        buttonGroupFormaPagamento.add(rbFPDeposito);
        rbFPDeposito.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbFPDeposito.setText("Depósito");

        buttonGroupFormaPagamento.add(rbFPTCash);
        rbFPTCash.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        rbFPTCash.setText("Cash/Dinheiro");

        try
        {
            ftfNumeroIBAN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("'A'O06.####.####.####.####.####.#")));
        } catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        ftfNumeroIBAN.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNumeroConta, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                        .addComponent(ftfNumeroIBAN))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(rbFPTransferencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbFPDeposito)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbFPTCash, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(596, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ftfNumeroIBAN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbFPTransferencia)
                    .addComponent(rbFPDeposito)
                    .addComponent(rbFPTCash))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painel_principal.addTab("Opção Banco", jPanel8);

        lbDataEmitidoDoc2.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDataEmitidoDoc2.setText("Data Término:");

        dcDataEmitidoDoc2.setDateFormatString("dd-MM-yyyy");
        dcDataEmitidoDoc2.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        lbAjudaDataEmitido2.setFont(new java.awt.Font("Lucida Grande", 0, 8)); // NOI18N
        lbAjudaDataEmitido2.setText("(Dia-Mês-Ano)");

        lbDataEmitidoDoc3.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        lbDataEmitidoDoc3.setText("Motivo:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton7.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/add_16x16.png"))); // NOI18N
        jButton7.setText("Declaração de Trabalho");

        jButton8.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/add_16x16.png"))); // NOI18N
        jButton8.setText("Recibo Fecho Contas");

        jButton9.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/add_16x16.png"))); // NOI18N
        jButton9.setText("Termo Quitação");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbDataEmitidoDoc3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataEmitidoDoc2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(dcDataEmitidoDoc2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbAjudaDataEmitido2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbDataEmitidoDoc2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcDataEmitidoDoc2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbAjudaDataEmitido2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbDataEmitidoDoc3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addGap(33, 33, 33))
        );

        painel_principal.addTab("Fecho de Contas", jPanel9);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Anexos"));

        jList2.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jList2MouseClicked(evt);
            }
        });
        jList2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jList2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                jList2KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jList2);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton4.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/find/seach_32x32.png"))); // NOI18N
        jButton4.setText("Carregar");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/adicionar.png"))); // NOI18N
        jButton10.setText("Remover");
        jButton10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Button-Add-icon.png"))); // NOI18N
        jButton11.setText("Adicionar");
        jButton11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jLabel33.setText("Nome do anexo:");

        txtNomeAnexo.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtNomeAnexo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(5, 5, 5)
                        .addComponent(txtNomeAnexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11))
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        painel_principal.addTab("Anexos", jPanel12);

        lb_seja_bem_vindo1.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 10)); // NOI18N
        lb_seja_bem_vindo1.setText("EMPRESA:");

        lbEmpresa.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 10)); // NOI18N
        lbEmpresa.setText("BEM VINDO!....");

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painel_principal)
                    .addGroup(painelLayout.createSequentialGroup()
                        .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painel_principal, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(painel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDocIDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDocIDActionPerformed
    {//GEN-HEADEREND:event_cmbDocIDActionPerformed
        // TODO add your handling code here:
        accaoDocID();
    }//GEN-LAST:event_cmbDocIDActionPerformed

    private void cmbSubsidiosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSubsidiosActionPerformed
    {//GEN-HEADEREND:event_cmbSubsidiosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSubsidiosActionPerformed

    private void cmbGrauAcademicoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbGrauAcademicoActionPerformed
    {//GEN-HEADEREND:event_cmbGrauAcademicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGrauAcademicoActionPerformed

    private void cmbEspecialidadeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbEspecialidadeActionPerformed
    {//GEN-HEADEREND:event_cmbEspecialidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEspecialidadeActionPerformed

    private void cmbDepartamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDepartamentoActionPerformed
    {//GEN-HEADEREND:event_cmbDepartamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDepartamentoActionPerformed

    private void cmbFuncaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFuncaoActionPerformed
    {//GEN-HEADEREND:event_cmbFuncaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFuncaoActionPerformed

    private void cmbContratoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbContratoActionPerformed
    {//GEN-HEADEREND:event_cmbContratoActionPerformed
        // TODO add your handling code here:
        habilitarCampoDuracao();
    }//GEN-LAST:event_cmbContratoActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbStatusActionPerformed
    {//GEN-HEADEREND:event_cmbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void cmbDuracaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDuracaoActionPerformed
    {//GEN-HEADEREND:event_cmbDuracaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDuracaoActionPerformed

    private void jList2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jList2MouseClicked
    {//GEN-HEADEREND:event_jList2MouseClicked
        // TODO add your handling code here:
        mostrar_anexo_visao();
    }//GEN-LAST:event_jList2MouseClicked

    private void jList2KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jList2KeyPressed
    {//GEN-HEADEREND:event_jList2KeyPressed
        mostrar_anexo_visao();
    }//GEN-LAST:event_jList2KeyPressed

    private void jList2KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jList2KeyReleased
    {//GEN-HEADEREND:event_jList2KeyReleased
        // TODO add your handling code here:
        mostrar_anexo_visao();
    }//GEN-LAST:event_jList2KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        // TODO add your handling code here:
        accao_selecionar_imagem();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton10ActionPerformed
    {//GEN-HEADEREND:event_jButton10ActionPerformed
        // TODO add your handling code here:
        remover_anexo();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton11ActionPerformed
    {//GEN-HEADEREND:event_jButton11ActionPerformed
        // TODO add your handling code here:
        criar_anexo();
//        adicionar_anexo();

    }//GEN-LAST:event_jButton11ActionPerformed

    private void rbSegurancaoSocialSimActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbSegurancaoSocialSimActionPerformed
    {//GEN-HEADEREND:event_rbSegurancaoSocialSimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbSegurancaoSocialSimActionPerformed

    private void rbNIFSimActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbNIFSimActionPerformed
    {//GEN-HEADEREND:event_rbNIFSimActionPerformed
        // TODO add your handling code here:
        habilitarCampoNIF();
    }//GEN-LAST:event_rbNIFSimActionPerformed

    private void rbSegurancaoSocialNaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbSegurancaoSocialNaoActionPerformed
    {//GEN-HEADEREND:event_rbSegurancaoSocialNaoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_rbSegurancaoSocialNaoActionPerformed

    private void rbNIFNaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbNIFNaoActionPerformed
    {//GEN-HEADEREND:event_rbNIFNaoActionPerformed
        // TODO add your handling code here:
        habilitarCampoNIF();
    }//GEN-LAST:event_rbNIFNaoActionPerformed

    private void rbSegurancaoSocialSimKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_rbSegurancaoSocialSimKeyPressed
    {//GEN-HEADEREND:event_rbSegurancaoSocialSimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbSegurancaoSocialSimKeyPressed

    private void rbSegurancaoSocialSimMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_rbSegurancaoSocialSimMouseClicked
    {//GEN-HEADEREND:event_rbSegurancaoSocialSimMouseClicked
        // TODO add your handling code here:
        habilitarCampoSegurancaSocial();
    }//GEN-LAST:event_rbSegurancaoSocialSimMouseClicked

    private void rbSegurancaoSocialNaoMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_rbSegurancaoSocialNaoMouseClicked
    {//GEN-HEADEREND:event_rbSegurancaoSocialNaoMouseClicked
        // TODO add your handling code here:
        habilitarCampoSegurancaSocial();
    }//GEN-LAST:event_rbSegurancaoSocialNaoMouseClicked

    private void cmbRegimeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbRegimeActionPerformed
    {//GEN-HEADEREND:event_cmbRegimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbRegimeActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        // TODO add your handling code here:
        procedimento_adicionar_subsidio();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        // TODO add your handling code here:
        new AgregadoFamiliarVisao( this, rootPaneCheckingEnabled ).show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jsValorSubsidioMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jsValorSubsidioMouseClicked
    {//GEN-HEADEREND:event_jsValorSubsidioMouseClicked
        // TODO add your handling code here:
        System.out.println( "MouseCliked" );
    }//GEN-LAST:event_jsValorSubsidioMouseClicked

    private void jsValorSubsidioKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jsValorSubsidioKeyPressed
    {//GEN-HEADEREND:event_jsValorSubsidioKeyPressed
        // TODO add your handling code here:
        System.out.println( "Keypressed" );
    }//GEN-LAST:event_jsValorSubsidioKeyPressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton12ActionPerformed
    {//GEN-HEADEREND:event_jButton12ActionPerformed
        // TODO add your handling code here:
        retirar_item_subsidios();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_salvarActionPerformed
    {//GEN-HEADEREND:event_btn_salvarActionPerformed
        // TODO add your handling code here:

        procedimento_salvar_funcionario();
    }//GEN-LAST:event_btn_salvarActionPerformed

    private void rbDescontoSeguracaoSocialSIMActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbDescontoSeguracaoSocialSIMActionPerformed
    {//GEN-HEADEREND:event_rbDescontoSeguracaoSocialSIMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbDescontoSeguracaoSocialSIMActionPerformed

    private void rbDescontoSeguracaoSocialNaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbDescontoSeguracaoSocialNaoActionPerformed
    {//GEN-HEADEREND:event_rbDescontoSeguracaoSocialNaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbDescontoSeguracaoSocialNaoActionPerformed

    private void txtNumeroFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioActionPerformed
        // TODO add your handling code here:
        busca_funcionario( txtNumeroFuncionario.getText() );
    }//GEN-LAST:event_txtNumeroFuncionarioActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_actualizarActionPerformed
    {//GEN-HEADEREND:event_btn_actualizarActionPerformed
        // TODO add your handling code here:
        procedimento_actualizar_funcionario();
    }//GEN-LAST:event_btn_actualizarActionPerformed

    private void txtNumeroFuncionarioPesquisaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioPesquisaActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioPesquisaActionPerformed
        // TODO add your handling code here:
        busca_funcionario( txtNumeroFuncionarioPesquisa.getText() );
    }//GEN-LAST:event_txtNumeroFuncionarioPesquisaActionPerformed

    private void tabela_agregado_familiarMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tabela_agregado_familiarMouseClicked
    {//GEN-HEADEREND:event_tabela_agregado_familiarMouseClicked
        // TODO add your handling code here:

        if ( evt.getClickCount() >= 2 )
        {
            remover_agregado_familiar_item();
        }
    }//GEN-LAST:event_tabela_agregado_familiarMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton13ActionPerformed
    {//GEN-HEADEREND:event_jButton13ActionPerformed
        new EspecilidadeVisao().show();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton16ActionPerformed
    {//GEN-HEADEREND:event_jButton16ActionPerformed
        new GrauAcademicoVisao().show();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton17ActionPerformed
    {//GEN-HEADEREND:event_jButton17ActionPerformed
        new DepartamentoVisao().show();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton14ActionPerformed
    {//GEN-HEADEREND:event_jButton14ActionPerformed
        new FuncaoVisao().show();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void cmbFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFuncionarioActionPerformed
    {//GEN-HEADEREND:event_cmbFuncionarioActionPerformed
        // TODO add your handling code here:
        try
        {

            busca_funcionario();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_cmbFuncionarioActionPerformed

//    public void setCampos()
//    {
//        if ( getIdFuncionario()!= 0 )
//        {
//            funcionario = funcionarioDao.getFuncionarioByCodigo( getIdFuncionario());
//            txtNome.setText( aluno.getNome() );
//            txtNomePai.setText( aluno.getPai() );
//            txtNomeMae.setText( aluno.getMae() );
//            txtTelefonePai.setText( aluno.getTelefonePai() );
//            txtTelefoneMae.setText( aluno.getTelefoneMae() );
//            txtBi.setText( aluno.getNbi() );
//            txtObs.setText( aluno.getObs() );
//            txtLocalEmissao.setText( aluno.getArquivoIdentificacao() );
//            dcDataEmissaoBI.setDate( aluno.getDataEmissaoBi() );
//            dcDataValidadeBI.setDate( aluno.getDataValidadeBi() );
//            cmbAnoLectivo.setSelectedItem( anoDao.findAno( aluno.getIdAno().getIdAno() ).getDescrisao() );
//            try
//            {
//                imageIcon = new ImageIcon( aluno.getPhoto() );
//                imageIcon.setImage( imageIcon.getImage().getScaledInstance( 133, 122, 100 ) );
//                lbPhoto.setIcon( imageIcon );
//            }
//            catch ( Exception e )
//            {
//                lbPhoto.setIcon( null );
//            }
//            try
//            {
//                dcDataNascimento.setDate( aluno.getDataNascimento() );
//                dcDataInscrisao.setDate( aluno.getDataIncrisao() );
//            }
//            catch ( Exception e )
//            {
//                dcDataNascimento.setDate( new Date() );
//                dcDataInscrisao.setDate( new Date() );
//            }
//            try
//            {
//                cmbMunicipio.setSelectedItem( municipioDao.findMunicipio( aluno.getIdMunicipio().getIdMunicipio() ).getDescrisao() );
//            }
//            catch ( Exception e )
//            {
//            }
//            cmbSexo.setSelectedItem( sexoDao.findSexo( aluno.getIdSexo().getIdSexo() ).getDescrisao() );
//            cmbStatus.setSelectedItem( statusDao.findStatus( aluno.getIdStatusFK().getIdStatus() ).getDescrisao() );
//            Encarregado encarregado = encarregadoDao.findEncarregado( aluno.getIdEncarregado().getIdEncarregado() );
//            txtNomeEncarreado.setText( encarregado.getNome() );
//            txtTelefoneEncarregado.setText( encarregado.getTelefone() );
//            txtEnderecoEncarregado.setText( encarregado.getEndereco() );
//            cmbProfissao.setSelectedItem( encarregado.getIdProfissaoFK().getDescricao() );
//            cmbReligiao.setSelectedItem( encarregado.getIdReligiaoFK().getDesricao() );
//            cmbGrauParentesco.setSelectedItem( encarregado.getIdGrauParentescoFK().getDescricao() );
//        }
//        else
//        {
//            limpar();
//        }
//    }

    private void txtIniciaisNomeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtIniciaisNomeActionPerformed
    {//GEN-HEADEREND:event_txtIniciaisNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisNomeActionPerformed

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
            java.util.logging.Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new FichaFuncionarioVisao( 15, 2, new BDConexao() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_salvar;
    private javax.swing.ButtonGroup buttonDescontoSegurancaSocial;
    private javax.swing.ButtonGroup buttonGroupEstadoCivil;
    private javax.swing.ButtonGroup buttonGroupFormaPagamento;
    private javax.swing.ButtonGroup buttonGroupNIF;
    private javax.swing.ButtonGroup buttonGroupSegurançaSocial;
    private javax.swing.ButtonGroup buttonGroupSexo;
    public static javax.swing.JComboBox<String> cmbContrato;
    public static javax.swing.JComboBox<String> cmbDepartamento;
    public static javax.swing.JComboBox<String> cmbDocID;
    public static javax.swing.JComboBox<String> cmbDuracao;
    public static javax.swing.JComboBox<String> cmbEspecialidade;
    public static javax.swing.JComboBox<String> cmbFuncao;
    private javax.swing.JComboBox cmbFuncionario;
    public static javax.swing.JComboBox<String> cmbGrauAcademico;
    public static javax.swing.JComboBox<String> cmbRegime;
    public static javax.swing.JComboBox<String> cmbStatus;
    public static javax.swing.JComboBox<String> cmbSubsidios;
    public static com.toedter.calendar.JDateChooser dcDataAdmissao;
    public static com.toedter.calendar.JDateChooser dcDataEmitidoDoc;
    private com.toedter.calendar.JDateChooser dcDataEmitidoDoc2;
    public static com.toedter.calendar.JDateChooser dcDataNascimento;
    public static com.toedter.calendar.JDateChooser dcDataValidadeDoc;
    public static javax.swing.JFormattedTextField ftfNumeroIBAN;
    public static javax.swing.JFormattedTextField ftfTelefone1;
    public static javax.swing.JFormattedTextField ftfTelefone2;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTextArea1;
    public static javax.swing.JSpinner jsSalarioBase;
    public static javax.swing.JSpinner jsSubsidioFeria;
    public static javax.swing.JSpinner jsSubsidioNatal;
    public static javax.swing.JSpinner jsSubsidioOutros;
    public static javax.swing.JSpinner jsValorSubsidio;
    private javax.swing.JLabel lbAjudaDataEmitido;
    private javax.swing.JLabel lbAjudaDataEmitido1;
    private javax.swing.JLabel lbAjudaDataEmitido2;
    private javax.swing.JLabel lbAjudaDataValidade;
    private javax.swing.JLabel lbDataEmitidoDoc;
    private javax.swing.JLabel lbDataEmitidoDoc1;
    private javax.swing.JLabel lbDataEmitidoDoc2;
    private javax.swing.JLabel lbDataEmitidoDoc3;
    private javax.swing.JLabel lbDataValidadeDoc;
    private javax.swing.JLabel lbDescontoSegurancaSocial;
    private javax.swing.JLabel lbDocID;
    private javax.swing.JLabel lbDuracao;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lbNumeroNIF;
    private javax.swing.JLabel lbNumeroSegurancaSocial;
    private javax.swing.JLabel lbTelefone2;
    private javax.swing.JLabel lb_anexo_doc;
    private javax.swing.JLabel lb_nome_funcionario;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    public static javax.swing.JPanel painel;
    public static javax.swing.JTabbedPane painel_principal;
    public static javax.swing.JRadioButton rbCasado;
    public static javax.swing.JRadioButton rbDescontoSeguracaoSocialNao;
    public static javax.swing.JRadioButton rbDescontoSeguracaoSocialSIM;
    public static javax.swing.JRadioButton rbDivorciado;
    private javax.swing.JRadioButton rbFPDeposito;
    private javax.swing.JRadioButton rbFPTCash;
    private javax.swing.JRadioButton rbFPTransferencia;
    public static javax.swing.JRadioButton rbNIFNao;
    public static javax.swing.JRadioButton rbNIFSim;
    public static javax.swing.JRadioButton rbSegurancaoSocialNao;
    public static javax.swing.JRadioButton rbSegurancaoSocialSim;
    public static javax.swing.JRadioButton rbSexoFemenino;
    public static javax.swing.JRadioButton rbSexoMasculino;
    public static javax.swing.JRadioButton rbSolteiro;
    public static javax.swing.JRadioButton rbViuvo;
    public static javax.swing.JTable tabela_agregado_familiar;
    public static javax.swing.JTable tabela_subsidio;
    public static javax.swing.JFormattedTextField txtDocID;
    private javax.swing.JTextField txtIniciaisNome;
    public static javax.swing.JTextField txtMorada;
    public static javax.swing.JTextField txtNIF;
    private javax.swing.JTextField txtNomeAnexo;
    public static javax.swing.JTextField txtNomeCompleto;
    private javax.swing.JTextField txtNomeCompleto4;
    public static javax.swing.JTextField txtNumeroConta;
    public static javax.swing.JTextField txtNumeroFuncionario;
    public static javax.swing.JTextField txtNumeroFuncionarioPesquisa;
    public static javax.swing.JTextField txtNumeroSegurancaSocial;
    // End of variables declaration//GEN-END:variables

    private void accaoDocID()
    {
        //habilita os campos as data de emissão e de validade do documento nos caso:  BI ou PassPort
        if ( cmbDocID.getSelectedIndex() == 1 || cmbDocID.getSelectedIndex() == 2 )
        {
            hablitarCamposDocID( true );
            dcDataEmitidoDoc.setDate( null );
            dcDataValidadeDoc.setDate( null );
        }
        else
        {
            hablitarCamposDocID( false );
        }
    }

    private void hablitarCamposDocID( boolean b )
    {
        /**
         * Data de emissão
         */
        lbDataEmitidoDoc.setVisible( b );
        dcDataEmitidoDoc.setVisible( b );
        lbAjudaDataEmitido.setVisible( b );

        /**
         * Data de validade
         */
        lbDataValidadeDoc.setVisible( b );
        dcDataValidadeDoc.setVisible( b );
        lbAjudaDataValidade.setVisible( b );
    }

    public void habilitarCampoSegurancaSocial()
    {
        if ( rbSegurancaoSocialSim.isSelected() )
        {

            lbNumeroSegurancaSocial.setVisible( true );
            txtNumeroSegurancaSocial.setVisible( true );
            limparCampoForm( txtNumeroSegurancaSocial );
            txtNumeroSegurancaSocial.requestFocus();
        }
        else
        {
            lbNumeroSegurancaSocial.setVisible( false );
            txtNumeroSegurancaSocial.setVisible( false );
            rbDescontoSeguracaoSocialNao.setSelected( true );

        }

    }

    private String getFormaPagamento()
    {
        if ( rbFPTransferencia.isSelected() )
        {
            return DVML.FORMA_PAGAMENTO_TRANSFERENCIA;
        }
        else if ( rbFPDeposito.isSelected() )
        {
            return DVML.FORMA_PAGAMENTO_DEPOSITO;
        }
        else
        {
            return DVML.FORMA_PAGAMENTO_CASH;
        }

    }

    public void habilitarCampoNIF()
    {
        if ( rbNIFSim.isSelected() )
        {
            lbNumeroNIF.setVisible( true );
            txtNIF.setVisible( true );

            /**
             * limpa os campos
             */
            limparCampoForm( txtNIF );
            txtNIF.requestFocus();

        }
        else
        {
            lbNumeroNIF.setVisible( false );
            txtNIF.setVisible( false );
        }

    }

    private void init()
    {
//        setMenuPopout();
//        txtIniciaisNome.addKeyListener( new TratarEventoTeclado() );
        txtIniciaisNome.addKeyListener( new TratarEventoTeclado() );
        jList2.setModel( listModelAnexo );
//        txtIniciaisNome.addKeyListener( new TratarEvento() );
//        jList2.setModel( lista_model );
//        try
//        {
//            listar_anexo_funcionario();
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//        }
        proximo_codigo();

        //configura o mínimo de dadas
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.DATE, 1 );

        dcDataNascimento.setMaxSelectableDate( new Date() );
        //configura a data máxima da emissão do documento
        dcDataEmitidoDoc.setMaxSelectableDate( new Date() );
        dcDataValidadeDoc.setMinSelectableDate( calendar.getTime() );

        /**
         * Configuração da Painel Salário
         */
        jsSalarioBase.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.00, Double.MAX_VALUE, 0.00 ) );
        jsSubsidioFeria.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.00, 100.00, 0.00 ) );
        jsSubsidioNatal.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.00, 100.00, 0.00 ) );
        jsSubsidioOutros.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.00, 100.00, 0.00 ) );

        /**
         * Configuração das descrições
         */
        cmbGrauAcademico.setModel( new DefaultComboBoxModel( ( Vector ) grauAcademicoDao.buscaTodosSeleccione() ) );
        cmbDepartamento.setModel( new DefaultComboBoxModel( ( Vector ) departamentoDao.buscaTodosSeleccione() ) );
        cmbEspecialidade.setModel( new DefaultComboBoxModel( ( Vector ) especialidadeDao.buscaTodosSeleccione() ) );
        cmbFuncao.setModel( new DefaultComboBoxModel( ( Vector ) funcaoDao.buscaTodosSeleccione() ) );
        cmbSubsidios.setModel( new DefaultComboBoxModel( ( Vector ) subsidioDao.buscaTodosSeleccione() ) );
        cmbRegime.setModel( new DefaultComboBoxModel( ( Vector ) modalidadeDao.buscaTodosSeleccione() ) );
//        cmbStatus.setModel( new DefaultComboBoxModel( (Vector) statusDao.buscaTodosSeleccione() ) );

        //set por default Activo como status
        cmbStatus.setSelectedIndex( 1 );
        //set por default o Remimo de Turno
        cmbRegime.setSelectedIndex( 3 );

        /**
         * Configuração painel subsídios
         */
        jsValorSubsidio.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.0, Double.MAX_VALUE, 0.00 ) );

    }

    private void habilitarCampoDuracao()
    {
        if ( cmbContrato.getSelectedIndex() == 1 )
        {
            lbDuracao.setVisible( true );
            cmbDuracao.setVisible( true );
        }
        else
        {
            lbDuracao.setVisible( false );
            cmbDuracao.setVisible( false );
        }
    }

    private void setStatus( Integer activo )
    {
        if ( activo == 1 )
        {
            cmbStatus.setSelectedIndex( 1 );
        }
        else
        {
            cmbStatus.setSelectedIndex( 2 );
        }
    }

    //----------- evento do teclado ---------------------------------------
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

                    prefixo = txtIniciaisNome.getText().trim() + key;
                    cmbFuncionario.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getALLFuncionarioByNome20( prefixo ) ) );
                    busca_funcionario();
                    listar_anexo_funcionario();

                }
                catch ( Exception e )
                {
                    btn_actualizar.setEnabled( false );
                    limpar_campos_funcionario();
                }

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    cmbFuncionario.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getALLFuncionarioByNome20( prefixo ) ) );
                    busca_funcionario();
                    listar_anexo_funcionario();

                }
                catch ( Exception e )
                {
                    btn_actualizar.setEnabled( false );
                    limpar_campos_funcionario();
                }

            }

            activar_btn_alterar( prefixo );
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }

    }

    private void activar_btn_alterar( String prefixo )
    {

        if ( prefixo.equals( "" ) )
        {
            btn_actualizar.setEnabled( false );
            btn_salvar.setEnabled( true );
        }
        else
        {
            btn_actualizar.setEnabled( true );
            btn_salvar.setEnabled( false );
        }

    }

    private void procedimento_adicionar_subsidio()
    {

        if ( IdentificacaoValidacaoUtil.campoValidosSubsidio() )
        {
            DefaultTableModel modelo = ( DefaultTableModel ) tabela_subsidio.getModel();

            String subsidio = cmbSubsidios.getSelectedItem().toString();

            if ( !IdentificacaoValidacaoUtil.existe_subsidio( subsidio ) )
            {  //System.out.println( "Valor Subsidio: " + ( Double ) jsValorSubsidio.getValue());
                Double valor = ( Double ) jsValorSubsidio.getValue();

                modelo.addRow( new Object[]
                {
                    subsidio,
                    valor,
                    ""

                } );

            }
            else
            {
                showMessageUtil( "O funcionário ja tem este subsídio.", TIPO_MENSAGEM_INFOR );
            }

        }

    }

    private void mostrar_subsidios( List<TbItemSubsidiosFuncionario> lista )
    {

        DefaultTableModel modelo = ( DefaultTableModel ) tabela_subsidio.getModel();
        modelo.setRowCount( 0 );
        if ( !Objects.isNull( lista ) )
        {
            for ( TbItemSubsidiosFuncionario item : lista )
            {
                String subsidio = item.getIdSubsidioFK().getDescricao();
                //System.out.println( "Valor Subsidio: " + ( Double ) jsValorSubsidio.getValue());
                Double valor = item.getValorSubsidio();

                modelo.addRow( new Object[]
                {
                    subsidio,
                    valor,
                    ""

                } );

            }
        }

    }

    private String getNumeroDocID()
    {

        if ( cmbDocID.getSelectedIndex() == 1 || cmbDocID.getSelectedIndex() == 2 )
        {
            return txtDocID.getText();
        }
        return "N/A";

    }

    private Date getDataEmissaoDocID()
    {

        if ( cmbDocID.getSelectedIndex() == 1 || cmbDocID.getSelectedIndex() == 2 )
        {
            return dcDataEmitidoDoc.getDate();
        }
        return new Date();

    }

    private Date getDataValidadeDocID()
    {

        if ( cmbDocID.getSelectedIndex() == 1 || cmbDocID.getSelectedIndex() == 2 )
        {
            return dcDataValidadeDoc.getDate();
        }
        return new Date();

    }

    private String getSexo()
    {
        if ( rbSexoMasculino.isSelected() )
        {
            return SEXO_MASCULINO;
        }
        return SEXO_FEMENINO;
    }

    private void setSexo( String sexo )
    {
        if ( SEXO_MASCULINO.equals( sexo ) )
        {
            rbSexoMasculino.setSelected( true );
            rbSexoFemenino.setSelected( false );

        }
        else
        {
            rbSexoFemenino.setSelected( true );
            rbSexoMasculino.setSelected( false );

        }

    }

    private void setEstadoCivil( int estadoCivil )
    {
        if ( estadoCivil == SOLTEIRO )
        {
            rbSolteiro.setSelected( true );
            rbCasado.setSelected( false );
            rbDivorciado.setSelected( false );
            rbViuvo.setSelected( false );

        }
        else if ( estadoCivil == CASADO )
        {
            rbSolteiro.setSelected( false );
            rbCasado.setSelected( true );
            rbDivorciado.setSelected( false );
            rbViuvo.setSelected( false );

        }
        else if ( estadoCivil == DIVORCIADO )
        {
            rbSolteiro.setSelected( false );
            rbCasado.setSelected( false );
            rbDivorciado.setSelected( true );
            rbViuvo.setSelected( false );

        }
        else if ( estadoCivil == VIUVO )
        {
            rbSolteiro.setSelected( false );
            rbCasado.setSelected( false );
            rbDivorciado.setSelected( false );
            rbViuvo.setSelected( true );

        }
        else
        {
            rbSolteiro.setSelected( false );
            rbCasado.setSelected( false );
            rbDivorciado.setSelected( false );
            rbViuvo.setSelected( false );
        }

    }

    /**
     * Paienel 1: Idêntificação
     */
    private void preparar_funcionario()
    {

        /**
         * Dados pessoais
         */
        //Numero do funcionario
        this.funcionario.setNumeroFuncionario( txtNumeroFuncionario.getText() );
        //Nome do funcionário
        this.funcionario.setNome( iniciais_maiuscula( txtNomeCompleto.getText() ) );
        //Sexo do funcionário
        this.funcionario.setSexo( getSexo() );
        //Data de nascimento fo funcionário
        this.funcionario.setDataNascimento( dcDataNascimento.getDate() );
        //Cidade do Funcionário
        //Morada 
        this.funcionario.setMorada( iniciais_maiuscula( txtMorada.getText() ) );
        //Telefone 1 do funcionário
        this.funcionario.setTelefone1( ftfTelefone1.getText() );
        // Telefone 2 do funcionário
        this.funcionario.setTelefone2( ftfTelefone2.getText() );
        //  concatena o telefone 1 e o telefone 2 e guarda na coluna 'telefone'
        this.funcionario.setTelefone( this.funcionario.getTelefone1() + "/" + this.funcionario.getTelefone2() );
        this.funcionario.setFkEstadoCivil( getEstadoCivil() );
        this.funcionario.setActivo( getIdStatus() );

        /**
         * Documento de idêntificação
         */
        this.funcionario.setDocID( cmbDocID.getSelectedItem().toString() );
        this.funcionario.setNdocID( getNumeroDocID() );
        this.funcionario.setDataemissaodocID( getDataEmissaoDocID() );
        this.funcionario.setDatavalidadedocID( getDataValidadeDocID() );
        /**
         * Painel 3: Fiscalidade
         */
        this.funcionario.setNSegurancaSocial( rbSegurancaoSocialSim.isSelected() ? txtNumeroSegurancaSocial.getText() : "N/A" );
        this.funcionario.setDescontoSegurancaSocial( rbDescontoSeguracaoSocialSIM.isSelected() ? "Sim" : "Não" );
        this.funcionario.setNif( txtNIF.getText() );
//        this.funcionario.setDescontoSegurancaSocial( getDescontoSegurancaSocial() );

        /**
         * Painel 5: Descrição
         */
        this.funcionario.setFkGrauAcademico( grauAcademicoDao.findTbGrauAcademico( getIdGrau() ) );
        this.funcionario.setFkEspecialidade( especialidadeDao.findTbEspecialidade( getIdEspecialidades() ) );
        this.funcionario.setFkDepartamento( departamentoDao.findTbDepartamento( getIdDepartamentos() ) );
        this.funcionario.setFkFuncao( funcaoDao.findTbFuncao( getIdFuncoes() ) );
//        this.funcionario.setFkGrauAcademico( getGrauAcademico() );
//        this.funcionario.setFkDepartamento( getDepartamento() );
//        this.funcionario.setFkFuncao( getFuncao() );
//        this.funcionario.setFkEspecialidade( getEspecialidade() );
        this.funcionario.setIdStatusFK( new TbStatus( 1 ) );

        //regime = modalidade
        this.funcionario.setFkModalidade( getRegime() );
        this.funcionario.setTipoContrato( getContrato() );
        this.funcionario.setDuracaoContrato( getDuracaoContrato() );

        //data de admissão
        this.funcionario.setDataContrato( dcDataAdmissao.getDate() );
        this.funcionario.setDataInicioContrato( dcDataAdmissao.getDate() );
        this.funcionario.setDataFimContrato( getDataFimContratro() );

        /**
         * Painel 6: Opção Banco
         */
        this.funcionario.setFormaPagamento( getFormaPagamento() );
        /**
         * Outros dados
         */
//        this.funcionario.setContaFechada( false );
        this.funcionario.setMotivoFecho( null );
        this.funcionario.setFkEmpresa( getEmpresa() );
        this.funcionario.setFkUsuario( getUsuario() );
    }

    /**
     * Painel 1: Idêntificação -> Agregado Familiar
     */
    private void procedimento_salvar_agregado_familiar()
    {

    }

    /**
     * Painel 2: Salário
     *
     * @param funcionario_parm
     * @return
     */
    private TbSalario preparar_salario( TbFuncionario funcionario_parm )
    {

        salario.setValorTempo( ( double ) jsSalarioBase.getValue() );
//        salario.setPercentagemSubFeria( (double) jsSubsidioFeria.getValue() );
//        salario.setPercentagemSubNatal( (double) jsSubsidioNatal.getValue() );
//        salario.setPercentagemSubOutros( (double) jsSubsidioOutros.getValue() );
        salario.setIdFuncionarioFK( funcionario_parm );

        return salario;

    }

    /**
     * Painel 3: subsídios
     */
    private void salvar_item_subsidios( TbFuncionario funcionario )
    {
        DefaultTableModel modelo = MetodosUtil.preparar_tabela( tabela_subsidio );
        TbItemSubsidiosFuncionario item_subsidio;
        TbSubsidios subsidio;
        int idSubsidio = 0;
        boolean sucesso = true;
        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            item_subsidio = new TbItemSubsidiosFuncionario();
            String descricao = modelo.getValueAt( i, 0 ).toString();
            Double valor = ( Double ) modelo.getValueAt( i, 1 );

            idSubsidio = subsidioDao.getIdByDescricao( descricao );
            subsidio = subsidioDao.findTbSubsidios( idSubsidio );

            item_subsidio.setIdFuncionarioFK( funcionario );
            item_subsidio.setIdSubsidioFK( subsidio );
            item_subsidio.setValorSubsidio( valor );

            try
            {
                itemSubsidioDao.create( item_subsidio );

            }
            catch ( Exception e )
            {
                sucesso = false;
                break;
            }

        }

        log += ( sucesso ) ? "Subsídios adicionado com sucesso." : "";

    }

    /**
     * Painel 3: subsídios
     */
    private void actualizar_item_subsidios( TbFuncionario funcionario )
    {

        if ( itemSubsidioDao.remover_all_subsidios( funcionario.getIdFuncionario() ) )
        {

            DefaultTableModel modelo = MetodosUtil.preparar_tabela( tabela_subsidio );
            TbItemSubsidiosFuncionario item_subsidio;
            TbSubsidios subsidio;
            int idSubsidio = 0;
            boolean sucesso = true;
            for ( int i = 0; i < modelo.getRowCount(); i++ )
            {
                item_subsidio = new TbItemSubsidiosFuncionario();
                String descricao = modelo.getValueAt( i, 0 ).toString();
                Double valor = ( Double ) modelo.getValueAt( i, 1 );

                idSubsidio = subsidioDao.getIdByDescricao( descricao );
                subsidio = subsidioDao.findTbSubsidios( idSubsidio );

                item_subsidio.setIdFuncionarioFK( funcionario );
                item_subsidio.setIdSubsidioFK( subsidio );
                item_subsidio.setValorSubsidio( valor );

                try
                {
                    itemSubsidioDao.create( item_subsidio );

                }
                catch ( Exception e )
                {
                    sucesso = false;
                    break;
                }

            }

            log += ( sucesso ) ? "Subsídios adicionado com sucesso." : "";
        }
        else
        {
            log += "Falha ao actualizar os subsídios";
        }

    }

    private List<AgregadoFamiliar> preparar_agregado_familiar( TbFuncionario funcionario )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_agregado_familiar.getModel();

        String nome_filho = "";
        Date data_nascimento = null;
        Double valor_bono = 0d;
        List<AgregadoFamiliar> list = new ArrayList<>();
        AgregadoFamiliar agregadoFamiliar;

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            nome_filho = modelo.getValueAt( i, 0 ).toString();
            data_nascimento = MetodosUtil.stringToDate( modelo.getValueAt( i, 1 ).toString(), "dd-MM-yyyy" );
            valor_bono = ( Double ) modelo.getValueAt( i, 3 );

            agregadoFamiliar = new AgregadoFamiliar();
            agregadoFamiliar.setNomeFilho( nome_filho );
            agregadoFamiliar.setDataNascimento( data_nascimento );
            agregadoFamiliar.setValor( valor_bono );
            agregadoFamiliar.setFkFuncionario( funcionario );

            list.add( agregadoFamiliar );

        }
        return list;

    }

    private void salvar_agregado_familiar( List<AgregadoFamiliar> list )
    {
        if ( list != null )
        {
            list.forEach( ( agregadoFamiliar ) ->
            {
                try
                {
                    agregadoFamiliarDao.create( agregadoFamiliar );
                }
                catch ( Exception e )
                {
                    showMessageUtil( "Falha ao adicionar o agregado.", TIPO_MENSAGEM_ERRO );
                }
            } );

            log += "Filhos adicionado com sucesso; \n";
        }
    }

    private void actualizar_agregado_familiar( List<AgregadoFamiliar> list, int idFuncionario )
    {
        if ( list != null )
        {
            /**
             * Destroi todos os itens ou seja todos os filhos do funcionário
             */

            agregadoFamiliarDao.removerAllAgregadoFamiliar( idFuncionario );

            //resalva os filhos do funcionário.
            salvar_agregado_familiar( list );
            //registra o log.
            log += "Filhos actualizados com sucesso; \n";
        }
    }

    public static void adicionar_filho_agregado( String nome, Date date, double valor_bono )
    {
        DefaultTableModel modelo = ( DefaultTableModel ) tabela_agregado_familiar.getModel();

        String data_nascimento = getDataString( date );
        int idade = new Date().getYear() - date.getYear();
        //adicionar uma linha em branco na tabela.
        modelo.addRow( new Object[]
        {
            nome,
            data_nascimento,
            idade,
            valor_bono

        } );
    }

    private double getMaximoValorSubisidioOutros()
    {
        double maximo_valor = 0d,
                percentagem_outros = ( Double ) jsSubsidioOutros.getValue(),
                salario_base = ( Double ) jsSalarioBase.getValue();

        maximo_valor = ( ( salario_base * percentagem_outros ) / 100 );

        return maximo_valor;

    }

    private void retirar_item_subsidios()
    {
        int opcao = MetodosUtil.getConfirmacaoDialog( "Caro usuário tens a certeza que pretendes eliminar este subsídio?" );

        if ( opcao == JOptionPane.YES_OPTION )
        {
            int linha = tabela_subsidio.getSelectedRow();
            MetodosUtil.remover_item_tabela( tabela_subsidio, linha );
        }
    }

    private int getIdGrauAcademico()
    {
        return grauAcademicoDao.getIdByDescricao( cmbGrauAcademico.getSelectedItem().toString() );

    }

    private int getIdEspecialidade()
    {
        return especialidadeDao.getIdByDescricao( cmbEspecialidade.getSelectedItem().toString() );

    }

    private int getIdDepartamento()
    {
        return departamentoDao.getIdByDescricao( cmbDepartamento.getSelectedItem().toString() );

    }

    private int getIdFuncao()
    {
        return funcaoDao.getIdByDescricao( cmbFuncao.getSelectedItem().toString() );

    }

    private int getIdStatus()
    {
        return ( cmbStatus.getSelectedIndex() == 1 ) ? 1 : 2;

    }

    private TbGrauAcademico getGrauAcademico()
    {
        return grauAcademicoDao.findTbGrauAcademico( getIdGrauAcademico() );
    }

//    private String getGrauAcademico()
//    {
//        return cmbGrauAcademico.getSelectedItem().toString();
//    }
    private TbDepartamento getDepartamento()
    {
        return departamentoDao.findTbDepartamento( getIdDepartamento() );
    }

    private TbEspecialidade getEspecialidade()
    {
        return especialidadeDao.findTbEspecialidade( getIdEspecialidade() );
    }

    private String getContrato()
    {
        return cmbContrato.getSelectedItem().toString();
    }

    private String getDuracaoContrato()
    {
        if ( cmbContrato.getSelectedIndex() == 2 )
        {
            return DVML.CONTRATRO_INDETERMINADO;
        }
        return cmbDuracao.getSelectedItem().toString();
    }

    private Modalidade getRegime()
    {
        return modalidadeDao.getModalidadeByDescricao( cmbRegime.getSelectedItem().toString() );
    }

    private TbFuncao getFuncao()
    {
        return funcaoDao.findTbFuncao( getIdFuncao() );
    }

    private TbConta preparar_conta( TbFuncionario funcionario )
    {

        this.conta.setNumeroConta( txtNumeroConta.getText() );
        this.conta.setIban( ftfNumeroIBAN.getText() );
        this.conta.setIdFuncionarioFK( funcionario );
        //por enquanto
        this.conta.setIdBancoFK( bancoDao.findTbBanco( 1 ) );
        return this.conta;

    }

    private void procedimento_salvar_funcionario()
    {

        if ( IdentificacaoValidacaoUtil.camposValidos() )
        {
            log = "";
            if ( salvar_funcionario() )
            {
                //busca o último funcionário.
                TbFuncionario funcionarioLast = funcionarioDao.getLastObject();
                //registra a conta do funcionário.
                salvar_conta( funcionarioLast );
                //registra o agregado familiar.
                salvar_agregado_familiar( preparar_agregado_familiar( funcionarioLast ) );
                //registrar o salário do funcionário.
                salvar_salario_funcionario( funcionarioLast );
                //registrar os subsídios do funcionário
                salvar_item_subsidios( funcionarioLast );

                showMessageUtil( log, TIPO_MENSAGEM_INFOR );

            }
        }

    }

    private void procedimento_actualizar_funcionario()
    {

        //if ( IdentificacaoValidacaoUtil.camposValidos() )
        //verifica se existe o funcionario
        if ( funcionarioDao.exist_funcionario_numero_func( txtNumeroFuncionario.getText() ) )
        {
            if ( IdentificacaoValidacaoUtil.camposValidosEdit() )
            {
                log = "";
                if ( actualizar_funcionario() )
                {
                    //busca o último funcionário.
                    TbFuncionario funcionarioEdit = funcionarioDao.getFuncionarioByNumeroFuncionario( txtNumeroFuncionario.getText() );
                    //registra a conta do funcionário.
                    actualizar_conta( funcionarioEdit );
                    //registra o agregado familiar.
                    actualizar_agregado_familiar( preparar_agregado_familiar( funcionarioEdit ), funcionarioEdit.getIdFuncionario() );
                    //registrar o salário do funcionário.
                    actualizar_salario_funcionario( funcionarioEdit );
                    //registrar os subsídios do funcionário
                    actualizar_item_subsidios( funcionarioEdit );
                    showMessageUtil( log, TIPO_MENSAGEM_INFOR );

                }
            }
        }

    }

    private String getDescontoSegurancaSocial()
    {

        if ( rbDescontoSeguracaoSocialSIM.isSelected() )
        {
            return "Sim";
        }
        return "Não";

    }

    private boolean salvar_funcionario()
    {
        this.funcionario = new TbFuncionario();
        preparar_funcionario();
        try
        {
            funcionarioDao.create( this.funcionario );
            log += "Funcionário registrado com sucesso \n";
            return true;
        }
        catch ( Exception e )
        {
            System.err.println( "Erro: " + e.getMessage() );
            showMessageUtil( "Falha ao registrar o funcionário", TIPO_MENSAGEM_ERRO );
            return false;

        }
    }

    private boolean actualizar_funcionario()
    {
        this.funcionario = funcionarioDao.getFuncionarioByNumeroFuncionario( txtNumeroFuncionario.getText() );
        preparar_funcionario();
        try
        {
            funcionarioDao.edit( this.funcionario );
            log += "Funcionário editado com sucesso \n";
            return true;
        }
        catch ( Exception e )
        {
            System.err.println( "Erro: " + e.getMessage() );
            showMessageUtil( "Falha ao editar o funcionário", TIPO_MENSAGEM_ERRO );
            return false;

        }
    }

    private Empresa getEmpresa()
    {
        return empresaDao.findEmpresa( this.idEmpresa );
    }

    private TbUsuario getUsuario()
    {
        return usuarioDao.findTbUsuario( this.idUsuario );
    }

    private Date getDataFimContratro()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( dcDataAdmissao.getDate() );
        calendar.add( Calendar.DATE, 30 );

        return calendar.getTime();
    }

    private int getIdEstadoCivil()
    {
        if ( rbSolteiro.isSelected() )
        {
            return DVML.SOLTEIRO;
        }
        else if ( rbCasado.isSelected() )
        {
            return DVML.CASADO;
        }
        else if ( rbViuvo.isSelected() )
        {
            return DVML.VIUVO;
        }
        else
        {
            return DVML.DIVORCIADO;
        }

    }

    private TbEstadoCivil getEstadoCivil()
    {
        return estadoCivilDao.findTbEstadoCivil( getIdEstadoCivil() );
    }

    /**
     * BUSCA DO FUNCIONARIO
     */
    private void busca_funcionario( String parm_numero_func )
    {

        TbFuncionario funcionario_local = funcionarioDao.getFuncionarioByNumeroFuncionario( parm_numero_func );

        limpar_campos_funcionario();
        if ( funcionario_local != null )
        {
            /**
             * Dados pessoais
             */

            lb_nome_funcionario.setText( "Nome: " + funcionario_local.getNome() );
            //Numero do funcionario
            txtNumeroFuncionario.setText( funcionario_local.getNumeroFuncionario() );
            //Nome do funcionário
            txtNomeCompleto.setText( funcionario_local.getNome() );
            setSexo( funcionario_local.getSexo() );
            dcDataNascimento.setDate( funcionario_local.getDataNascimento() );
            txtMorada.setText( funcionario_local.getMorada() );
            ftfTelefone1.setText( funcionario_local.getTelefone1() );
            ftfTelefone2.setText( funcionario_local.getTelefone2() );
            setEstadoCivil( funcionario_local.getFkEstadoCivil().getPkEstadoCivil() );

            /**
             * Documento de idêntificação
             */
            cmbDocID.setSelectedItem( funcionario_local.getDocID() );
            txtDocID.setText( funcionario_local.getNdocID() );
            dcDataEmitidoDoc.setDate( funcionario_local.getDataemissaodocID() );
            dcDataValidadeDoc.setDate( funcionario_local.getDatavalidadedocID() );

            /**
             * Painel 3: Fiscalidade
             */
            txtNumeroSegurancaSocial.setText( funcionario_local.getNSegurancaSocial() );
            setNumeroSegurancaSocial( funcionario_local.getNSegurancaSocial() );
            setRadioDescontoSeguracaoSocial( funcionario_local.getDescontoSegurancaSocial() );
            txtNIF.setText( funcionario_local.getNif() );
            setNif( funcionario_local.getNif() );

            /**
             * Painel 5: Descrição
             */
            cmbGrauAcademico.setSelectedItem( funcionario_local.getFkGrauAcademico().getDesignacao() );
            cmbEspecialidade.setSelectedItem( funcionario_local.getFkEspecialidade().getDesignacao() );
            setStatus( funcionario_local.getActivo() );
            cmbDepartamento.setSelectedItem( funcionario_local.getFkDepartamento().getDesignacao() );
            cmbFuncao.setSelectedItem( funcionario_local.getFkFuncao().getDesignacao() );
            cmbRegime.setSelectedItem( funcionario_local.getFkModalidade().getDesignacao() );
            cmbContrato.setSelectedItem( funcionario_local.getTipoContrato() );
            cmbDuracao.setSelectedItem( funcionario_local.getDuracaoContrato() );
            cmbFuncionario.setSelectedItem( funcionario_local.getNome() );

            dcDataAdmissao.setDate( funcionario_local.getDataContrato() );

            /**
             * Painel 6: Opção Banco
             */
//            setFormaPagamento( funcionario_local.getFormaPagamento() );
            // mostrar_agregado_familiar( funcionario_local.getAgregadoFamiliarList() );
            mostrar_agregado_familiar( agregadoFamiliarDao.getAllAgregadosFamiliarByIdFuncionario( funcionario_local.getIdFuncionario() ) );

            /**
             * Painel 2: Salário
             */
            mostrar_salario( funcionario_local );
            /**
             * Painel 4: Subsídios
             */
            mostrar_subsidios( itemSubsidioDao.getAllItemSubsidiosByIdFuncionario( funcionario_local.getIdFuncionario() ) );

            /**
             * Painel 6: Opção Banco
             */
            mostrar_conta( funcionario_local );

        }
        else
        {
//            limpar_campos_funcionario();
            showMessageUtil( "Não existe funcionário com este número.", TIPO_MENSAGEM_INFOR );
        }

    }

    /**
     * BUSCA DO FUNCIONARIO
     */
    private void busca_funcionario()
    {

        funcionario = funcionarioDao.getFuncionarioByCodigo( getIdFuncionario() );

        limpar_campos_funcionario();
        if ( funcionario != null )
        {
            /**
             * Dados pessoais
             */

            lb_nome_funcionario.setText( "Nome: " + funcionario.getNome() );
            //Numero do funcionario
            txtNumeroFuncionario.setText( funcionario.getNumeroFuncionario() );
            //Nome do funcionário
            txtNomeCompleto.setText( funcionario.getNome() );
            setSexo( funcionario.getSexo() );
            dcDataNascimento.setDate( funcionario.getDataNascimento() );
            txtMorada.setText( funcionario.getMorada() );
            ftfTelefone1.setText( funcionario.getTelefone1() );
            ftfTelefone2.setText( funcionario.getTelefone2() );
            setEstadoCivil( funcionario.getFkEstadoCivil().getPkEstadoCivil() );

            /**
             * Documento de idêntificação
             */
            cmbDocID.setSelectedItem( funcionario.getDocID() );
            txtDocID.setText( funcionario.getNdocID() );
            dcDataEmitidoDoc.setDate( funcionario.getDataemissaodocID() );
            dcDataValidadeDoc.setDate( funcionario.getDatavalidadedocID() );

            /**
             * Painel 3: Fiscalidade
             */
            txtNumeroSegurancaSocial.setText( funcionario.getNSegurancaSocial() );
            setNumeroSegurancaSocial( funcionario.getNSegurancaSocial() );
            setRadioDescontoSeguracaoSocial( funcionario.getDescontoSegurancaSocial() );
            txtNIF.setText( funcionario.getNif() );
            setNif( funcionario.getNif() );

            /**
             * Painel 5: Descrição
             */
            cmbGrauAcademico.setSelectedItem( funcionario.getFkGrauAcademico().getDesignacao() );
            cmbEspecialidade.setSelectedItem( funcionario.getFkEspecialidade().getDesignacao() );
            setStatus( funcionario.getActivo() );
            cmbDepartamento.setSelectedItem( funcionario.getFkDepartamento().getDesignacao() );
            cmbFuncao.setSelectedItem( funcionario.getFkFuncao().getDesignacao() );
            cmbRegime.setSelectedItem( funcionario.getFkModalidade().getDesignacao() );
            cmbContrato.setSelectedItem( funcionario.getTipoContrato() );
            cmbDuracao.setSelectedItem( funcionario.getDuracaoContrato() );
            cmbFuncionario.setSelectedItem( funcionario.getNome() );

            dcDataAdmissao.setDate( funcionario.getDataContrato() );

            /**
             * Painel 6: Opção Banco
             */
//            setFormaPagamento( funcionario_local.getFormaPagamento() );
            // mostrar_agregado_familiar( funcionario_local.getAgregadoFamiliarList() );
            mostrar_agregado_familiar( agregadoFamiliarDao.getAllAgregadosFamiliarByIdFuncionario( funcionario.getIdFuncionario() ) );

            /**
             * Painel 2: Salário
             */
            mostrar_salario( funcionario );
            /**
             * Painel 4: Subsídios
             */
            mostrar_subsidios( itemSubsidioDao.getAllItemSubsidiosByIdFuncionario( funcionario.getIdFuncionario() ) );

            /**
             * Painel 6: Opção Banco
             */
            mostrar_conta( funcionario );

            listar_anexo_funcionario();

        }
        else
        {
//            limpar_campos_funcionario();
            showMessageUtil( "Não existe funcionário com este número.", TIPO_MENSAGEM_INFOR );
        }

    }

    private void limpar_campos_funcionario()
    {

        /**
         * Dados pessoais
         */
        lb_nome_funcionario.setText( "" );
        //Numero do funcionario
        txtNumeroFuncionario.setText( "" );
        //Nome do funcionário
        txtNomeCompleto.setText( "" );
        setSexo( "" );
        dcDataNascimento.setDate( null );
        txtMorada.setText( "" );
        ftfTelefone1.setText( "" );
        ftfTelefone2.setText( "" );
        setEstadoCivil( 0 );

        /**
         * Documento de idêntificação
         */
        cmbDocID.setSelectedIndex( 0 );
        txtDocID.setText( "" );
        dcDataEmitidoDoc.setDate( null );
        dcDataValidadeDoc.setDate( null );

        /**
         * Painel 2; Salário
         */
        jsSalarioBase.setValue( 0d );
        jsSubsidioFeria.setValue( 0d );
        jsSubsidioNatal.setValue( 0d );
        jsSubsidioOutros.setValue( 0d );

        /**
         * Painel 3: Fiscalidade
         */
        txtNumeroSegurancaSocial.setText( "" );
        setNumeroSegurancaSocial( "" );
        txtNIF.setText( "" );
        setNif( "" );

        /**
         * Painel 4: Subsídios
         */
        esvaziar_tabela( tabela_subsidio );

        /**
         * Painel 5: Descrição
         */
        cmbGrauAcademico.setSelectedIndex( 0 );
        cmbEspecialidade.setSelectedIndex( 0 );
        cmbStatus.setSelectedIndex( 0 );
        cmbDepartamento.setSelectedIndex( 0 );
        cmbFuncao.setSelectedIndex( 0 );
        cmbRegime.setSelectedIndex( 0 );
        cmbContrato.setSelectedIndex( 0 );
        cmbDuracao.setSelectedIndex( 0 );
//        cmbFuncionario.setSelectedIndex( 0 );

        dcDataAdmissao.setDate( null );

        /**
         * Painel 6: Opção Banco
         */
        esvaziar_opcao_banco();

        esvaziar_tabela( tabela_agregado_familiar );

    }

    private void setNumeroSegurancaSocial( String numero_seguranca_social )
    {

        System.err.println( "NS:" + numero_seguranca_social );

        if ( numero_seguranca_social.equalsIgnoreCase( "N/A" ) )
        {
            rbSegurancaoSocialSim.setSelected( false );
            rbSegurancaoSocialNao.setSelected( true );
            lbNumeroSegurancaSocial.setVisible( false );
            txtNumeroSegurancaSocial.setVisible( false );

        }
        else
        {
            rbSegurancaoSocialSim.setSelected( true );
            rbSegurancaoSocialNao.setSelected( false );
            lbNumeroSegurancaSocial.setVisible( true );
            txtNumeroSegurancaSocial.setVisible( true );
        }

        //habilitarCampoSegurancaSocial();
    }

    private void setFormaPagamento( String formaPagamento )
    {
        if ( formaPagamento.equals( FORMA_PAGAMENTO_TRANSFERENCIA ) )
        {
            rbFPTransferencia.setSelected( true );
            rbFPDeposito.setSelected( false );
            rbFPTCash.setSelected( false );
        }
        else if ( formaPagamento.equals( FORMA_PAGAMENTO_DEPOSITO ) )
        {
            rbFPTransferencia.setSelected( false );
            rbFPDeposito.setSelected( true );
            rbFPTCash.setSelected( false );
        }
        else if ( formaPagamento.equals( FORMA_PAGAMENTO_CASH ) )
        {
            rbFPTransferencia.setSelected( false );
            rbFPDeposito.setSelected( false );
            rbFPTCash.setSelected( true );

        }
        else
        {
            rbFPTransferencia.setSelected( false );
            rbFPDeposito.setSelected( false );
            rbFPTCash.setSelected( false );

        }
    }

    private void setNif( String nif )
    {
        if ( !nif.equals( "" ) )
        {
            rbNIFSim.setSelected( true );
            rbNIFNao.setSelected( false );

        }
        else
        {
            rbNIFNao.setSelected( true );
            rbNIFSim.setSelected( false );

        }

    }

    private void setRadioDescontoSeguracaoSocial( String desconto_seguranca_social )
    {
        if ( desconto_seguranca_social.equals( "Sim" ) )
        {
            rbDescontoSeguracaoSocialSIM.setSelected( true );
            rbDescontoSeguracaoSocialNao.setSelected( false );

        }
        else
        {
            rbDescontoSeguracaoSocialNao.setSelected( true );
            rbDescontoSeguracaoSocialSIM.setSelected( false );

        }

    }

    private void mostrar_agregado_familiar( List<AgregadoFamiliar> agregadoFamiliarList )
    {

        DefaultTableModel modelo = preparar_tabela( tabela_agregado_familiar );
        esvaziar_tabela( tabela_agregado_familiar );
        if ( agregadoFamiliarList != null )
        {
            for ( AgregadoFamiliar agregadoFamiliar : agregadoFamiliarList )
            {
                modelo.addRow( new Object[]
                {
                    agregadoFamiliar.getNomeFilho(),
                    getDataString( agregadoFamiliar.getDataNascimento() ),
                    0,
                    agregadoFamiliar.getValor(),
                    ""

                } );
            }
        }

    }

    private void salvar_salario_funcionario( TbFuncionario funcionarioLast )
    {
        salario = new TbSalario();
        preparar_salario( funcionarioLast );

        try
        {
            salarioDao.create( salario );
            log += "Salário registrado com sucesso \n";
        }
        catch ( Exception e )
        {
            e.getMessage();
            e.printStackTrace();
            showMessageUtil( "Falha ao registrar o salário", TIPO_MENSAGEM_ERRO );
        }

    }

    private void actualizar_salario_funcionario( TbFuncionario funcionarioLast )
    {
        System.err.println( "Salário: " + funcionarioLast.getIdFuncionario() );
        salario = salarioDao.getSalarioObjectByIdFuncionario( funcionarioLast.getIdFuncionario() );
        if ( salario == null )
        {
            salario = new TbSalario();
        }
        preparar_salario( funcionarioLast );

        try
        {
            salarioDao.create( salario );
            log += "Salário actualizado com sucesso \n";
        }
        catch ( Exception e )
        {
            e.getMessage();
            e.printStackTrace();
            showMessageUtil( "Falha ao actualizar o salário", TIPO_MENSAGEM_ERRO );
        }

    }

    private void mostrar_salario( TbFuncionario funcionario_local )
    {
        TbSalario salario_local = salarioDao.getLastSalario( funcionario_local.getIdFuncionario() );

        if ( salario_local != null )
        {
            jsSalarioBase.setValue( salario_local.getValorTempo() );
//            jsSubsidioFeria.setValue( salario_local.getPercentagemSubFeria() );
//            jsSubsidioNatal.setValue( salario_local.getPercentagemSubNatal() );
//            jsSubsidioOutros.setValue( salario_local.getPercentagemSubOutros() );

        }
        else
        {
            jsSalarioBase.setValue( 0d );
            jsSubsidioFeria.setValue( 0d );
            jsSubsidioNatal.setValue( 0d );
            jsSubsidioOutros.setValue( 0d );
        }

    }

    private void salvar_conta( TbFuncionario funcionarioLast )
    {
        this.conta = new TbConta();
        this.conta = preparar_conta( funcionarioLast );

        try
        {
            contaDao.create( this.conta );
            log += "Conta adicionada com sucesso \n";
        }
        catch ( Exception e )
        {
            log += "Falha ao adicionar a conta do funcionário";
        }

    }

    private void actualizar_conta( TbFuncionario funcionarioLast )
    {
        this.conta = contaDao.getContaByIdFuncionario( funcionarioLast.getIdFuncionario() );

        if ( this.conta != null )
        {
            preparar_conta( funcionarioLast );
            try
            {
                contaDao.edit( this.conta );
                log += "Conta altraca com sucesso \n";
            }
            catch ( Exception e )
            {
                log += "Falha ao alterar a conta do funcionário";
            }
        }
        else
        {
            this.conta = new TbConta();
            salvar_conta( funcionarioLast );
            log += "Conta adicionadA com sucesso \n";
        }

    }

    private void mostrar_conta( TbFuncionario funcionario )
    {

        TbConta conta_local = contaDao.getContaByIdFuncionario( funcionario.getIdFuncionario() );

        if ( conta_local != null )
        {
            txtNumeroConta.setText( conta_local.getNumeroConta() );
            ftfNumeroIBAN.setText( conta_local.getIban() );
        }

    }

    private void esvaziar_opcao_banco()
    {
        txtNumeroConta.setText( "" );
        ftfNumeroIBAN.setText( "" );

    }

    private void proximo_codigo()
    {
        try
        {
//            jLabelCodProduto.setText( "Próximo Código: " + String.valueOf( produtoDao.getUltimaCodProduto() + 1 ) );
            txtNumeroFuncionario.setText( String.valueOf( funcionarioDao.getUltimoCodFuncionario() + 1 ) );
        }
        catch ( Exception e )
        {
        }

    }

    private int getIdGrau()
    {
        return grauAcademicoDao.getGrauAcademicoByDesignacao( String.valueOf( cmbGrauAcademico.getSelectedItem() ) ).getPkGrauAcademico();
    }

    private int getIdEspecialidades()
    {
        return especialidadeDao.getEspecialidadeByDesignacao( String.valueOf( cmbEspecialidade.getSelectedItem() ) ).getPkEspecialidade();
    }

    private int getIdDepartamentos()
    {
        return departamentoDao.getDepartamentoByDesignacao( String.valueOf( cmbDepartamento.getSelectedItem() ) ).getPkDepartamento();
    }

    private int getIdFuncoes()
    {
        return funcaoDao.getFuncaoByDesignacao( String.valueOf( cmbFuncao.getSelectedItem() ) ).getPkFuncao();
    }

//    public static int getIdGrau()
//    {
//        try
//        {
//            return grauAcademicoDao.getGrauAcademicoByDesignacao( cmbGrauAcademico.getSelectedItem().toString() ).getPkGrauAcademico();
//        }
//        catch ( Exception e )
//        {
//            return 0;
//        }
//
//    }
//    private static String getGrauAcademico()
//    {
//        return cmbGrauAcademico.getSelectedItem().toString();
//    }
//    private void accao_selecionar_imagem()
//    {
//        JFileChooser fc = new JFileChooser();
//        int res = fc.showOpenDialog( null );
//
//        if ( res == JFileChooser.APPROVE_OPTION )
//        {
//            File arquivo = fc.getSelectedFile();
//
//            try
//            {
//                imagem = ManipularImagemUtil.setImagemDimensao( arquivo.getAbsolutePath(), 600, 500 );
//                lb_anexo_doc.setText( "" );
//                lb_anexo_doc.setIcon( new ImageIcon( imagem ) );
//            }
//            catch ( Exception e )
//            {
//            }
//        }
//
//    }
//    private void enviarImagemToServer()
//    {
//        try
//        {
//            //String caminho = getClass().getResource( "imagem/" ).toString().substring( 5);
//            String nome_anexo = txtNomeAnexo.getText();
//            File outputfile = new File( CAMINHO_IMAGEM + nome_anexo + "-" + getIdFuncionario() + "." + FORMATO_IMAGEM_ANEXO );
//            ImageIO.write( imagem, FORMATO_IMAGEM_ANEXO, outputfile );
//            JOptionPane.showMessageDialog( null, "Imagem  enviada com sucesso!..." );
//        }
//        catch ( IOException e )
//        {
//            JOptionPane.showMessageDialog( null, "Falha ao enviar a imagem no servidor", "Falha", JOptionPane.ERROR_MESSAGE );
//        }
//
//    }
    private void criar_anexo()
    {
        Anexos anexo = new Anexos();
//        anexo.setNomeFicheiro( txtNomeAnexo.getText() + "-" + funcionario.getIdFuncionario() + "." + EXTENSAO );
        anexo.setNomeFicheiro( txtNomeAnexo.getText() + "-" + funcionario.getIdFuncionario() + "." + EXTENSAO );
        anexo.setCaminhoFicheiro( DVML.AGENDA_FUNCIONARIO_ANEXO_PATH );
        anexo.setFkFuncionario( funcionarioDao.findTbFuncionario( idFuncionario ) );
        anexo.setFkUsuario( usuarioDao.findTbUsuario( idUsuario ) );
//        anexo.setFkUsuario(us );
        if ( anexoController.salvar( anexo ) )
        {
            enviarImagemToServer();
            lb_anexo_doc.setIcon( null );
            listar_anexo_funcionario();
        }

    }

    private void adicionar_anexo()
    {
        String nomeAnexo = txtNomeAnexo.getText();
        if ( !nomeAnexo.equals( "" ) )
        {
            int idFuncionario = getIdFuncionario();
            if ( funcionarioDao.exist_funcionario( idFuncionario ) )
            {
                Anexos anexos = new Anexos();
                anexos.setDataHora( new Date() );
                anexos.setNomeFicheiro( nomeAnexo + "-" + getIdFuncionario() + "." + FORMATO_IMAGEM_ANEXO );
                anexos.setDescricao( "N/A" );
                anexos.setFkFuncionario( funcionarioDao.findTbFuncionario( idFuncionario ) );
                anexos.setFkUsuario( usuarioDao.findTbUsuario( idUsuario ) );
                try
                {
                    anexosDao.create( anexos );
                    enviarImagemToServer();
                    listar_anexo_funcionario();
                    lb_anexo_doc.setIcon( null );
                    JOptionPane.showMessageDialog( null, "Anexo adicionado com sucesso!" );
                }
                catch ( HeadlessException e )
                {
                    JOptionPane.showMessageDialog( null, "Falha ao adicionar o anexo", "FALHA", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Insira o nome do anexo", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

//    private void listar_anexo_funcionario()
//    {
//
//        List<String> lista_anexo_local = anexosDao.getAnexoByIdFuncionario( getIdFuncionario() );
//        lista_model.clear();
//        lb_anexo_doc.setIcon( null );
//        if ( lista_anexo_local != null )
//        {
//            for ( String anexo : lista_anexo_local )
//            {
//                lista_model.addElement( anexo );
//            }
//        }
//
//    }
//    private void listar_anexo_funcionario()
//    {
//        List<Anexo> lista_anexo_local = anexoController.listarTodosByIdFuncionario(funcionario.getIdFuncionario() );
//
//        listModelAnexo.clear();
//
//        if ( !Objects.isNull( lista_anexo_local ) )
//        {
//            for ( Anexo anexo : lista_anexo_local )
//            {
//                listModelAnexo.addElement( anexo.getNomeFicheiro() );
//            }
//        }
//
//    }
//    private void mostrar_anexo_visao()
//    {
//        String nomeFicheiro = jList1.getSelectedValue();
//        ManipularImagemUtil.mostrar_anexo( CAMINHO_IMAGEM + nomeFicheiro, lb_anexo_doc );
//
//    }
    private void remover_anexo()
    {
        if ( jList2.getSelectedIndex() > -1 )
        {
            int opcao = JOptionPane.showConfirmDialog( null, "Caro usuário deseja realmente eliminar o anexo deste Funcionario?" );

            if ( opcao == JOptionPane.YES_OPTION )
            {
                String nomeFicheiro = jList2.getSelectedValue();

                try
                {
                    File file = new File( CAMINHO_IMAGEM + nomeFicheiro );
                    file.delete();
                    Anexos anexo = anexosDao.getAnexoByIdFuncionarioAndNome( getIdFuncionario(), nomeFicheiro );
                    anexosDao.destroy( anexo.getPkAnexos() );
                    listar_anexo_funcionario();
                    JOptionPane.showMessageDialog( null, "Anexo removido com sucesso" );
                }
                catch ( HeadlessException e )
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog( null, "Falha ao remover o anexo", "FALHA", JOptionPane.ERROR_MESSAGE );
                }
                catch ( NonexistentEntityException ex )
                {
                    Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
                }

            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Caro usuário seleccione o anexo", "AVISO", JOptionPane.WARNING_MESSAGE );
        }

    }

//    private void setMenuPopout()
//    {
//        JMenuItem item_visualizar_word = new JMenuItem();
//        JMenuItem item_visualizar_anexo = new JMenuItem();
//
//        item_visualizar_word.addActionListener( new ActionListener()
//        {
//            @Override
//            public void actionPerformed( ActionEvent e )
//            {
//                abrir_anexo_word();
//            }
//        } );
//
//        item_visualizar_anexo.addActionListener( new ActionListener()
//        {
//            @Override
//            public void actionPerformed( ActionEvent e )
//            {
//                visualizar_anexo();
//            }
//        } );
//
//        item_visualizar_word.setText( "Visualizar anexo no word" );
//        item_visualizar_anexo.setText( "Visualizar anexo no editor de imagem padrão" );
//
////        jPopupMenu1.add( item_visualizar_word );
////        jPopupMenu1.add( item_visualizar_anexo );
////        lb_anexo_doc.setComponentPopupMenu( jPopupMenu1 );
//    }
//    private void visualizar_anexo()
//    {
//        try
//        {
//            String ficheiro_imagem = CAMINHO_IMAGEM + jListaAnexo.getSelectedValue();
//            File myFile = new File( ficheiro_imagem );
//            Desktop.getDesktop().open( myFile );
//        }
//        catch ( Exception ex )
//        {
//            Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
//
//    }
//    private void abrir_anexo_word()
//    {
//
//        try
//        {
//            String nome = cmbFuncionario.getSelectedItem().toString();
//            String ficheiro_docx = CAMINHO_DOCUMENTO + "anexo.docx";
//            String ficheiro_imagem = CAMINHO_IMAGEM + jListaAnexo.getSelectedValue();
//            MetodosUtil.inserir_imagem_docx( nome, ficheiro_docx, ficheiro_imagem );
//
//            File myFile = new File( ficheiro_docx );
//            Desktop.getDesktop().open( myFile );
//        }
//        catch ( IOException ex )
//        {
//            Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
//        }
////        catch ( InvalidFormatException | IOException ex )
////        {
////            Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
////        }
//
//    }
//    private void criar_anexo()
//    {
//        Anexo anexo = new Anexo();
//        anexo.setNomeFicheiro( txtNomeAnexo.getText() + "-" + funcionario.getIdFuncionario() + "." + EXTENSAO );
//        anexo.setCaminhoFicheiro( DVML.AGENDA_FUNCIONARIO_ANEXO_PATH );
//        anexo.setFkFuncionario( funcionario.getIdFuncionario() );
//
//        if ( anexoController.salvar( anexo ) )
//        {
//            enviarImagemToServer();
//            lb_anexo_doc.setIcon( null );
//            listar_anexo_jogador();
//        }
//
//    }
    private void listar_anexo_funcionario()
    {
        List<Anexos> lista_anexo_local = anexoController.listarTodosByIdFuncionario( funcionario.getIdFuncionario() );

        listModelAnexo.clear();

        if ( !Objects.isNull( lista_anexo_local ) )
        {
            for ( Anexos anexo : lista_anexo_local )
            {
                listModelAnexo.addElement( anexo.getNomeFicheiro() );
            }
        }

    }

    private void accao_selecionar_imagem()
    {
        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog( null );

        if ( res == JFileChooser.APPROVE_OPTION )
        {
            File arquivo = fc.getSelectedFile();

            System.out.println( "NOME ARQUIVO: " + arquivo.getName() );

            String nome = arquivo.getName();
            String extensao = getExtensao( nome );

            if ( extensao.equals( "jpg" ) || extensao.equals( "png" ) )
            {
                try
                {
                    imagem = ManipularImagemUtil.setImagemDimensao( arquivo.getAbsolutePath(), 400, 400 );
                    lb_anexo_doc.setText( "" );
                    lb_anexo_doc.setIcon( new ImageIcon( imagem ) );
                }
                catch ( Exception e )
                {
                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Arquivo nao suportado", "Aviso", JOptionPane.WARNING_MESSAGE );
            }

        }

    }

    private String getExtensao( String file )
    {
        String fe = "";
        if ( file.contains( "." ) )
        {
            int i = file.lastIndexOf( '.' );
            fe = i > 0 ? file.substring( i + 1 ) : "";
        }
        return fe;

    }

    private void enviarImagemToServer()
    {
        try
        {
            String nome_anexo = txtNomeAnexo.getText();
            File outPutFile = new File( DVML.AGENDA_FUNCIONARIO_ANEXO_PATH + nome_anexo + "-" + funcionario.getIdFuncionario() + "." + EXTENSAO );
            ImageIO.write( imagem, EXTENSAO, outPutFile );
            JOptionPane.showMessageDialog( null, "Anexo adicionado com sucesso" );
        }
        catch ( IOException e )
        {
            JOptionPane.showMessageDialog( null, "Falha ao adicinar o anexo" );
        }

    }

    private void mostrar_anexo_visao()
    {
        String nomeFicheiro = jList2.getSelectedValue();
        ManipularImagemUtil.mostrar_anexo( DVML.AGENDA_FUNCIONARIO_ANEXO_PATH + nomeFicheiro, lb_anexo_doc );
    }

    private void setMenuPopout()
    {
        JMenuItem item_visualizar_word = new JMenuItem();
        JMenuItem item_visualizar_anexo = new JMenuItem();

//        item_visualizar_word.addActionListener( new ActionListener()
//        {
//            @Override
//            public void actionPerformed( ActionEvent e )
//            {
//                abrir_anexo_word();
//            }
//        } );
        item_visualizar_anexo.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                visualizar_anexo();
            }

        } );

//        item_visualizar_word.setText( "Visualizar anexo no word" );
        item_visualizar_anexo.setText( "Visualizar anexo no editor de imagem padrão" );

//        jPopupMenu1.add( item_visualizar_word );
        jPopupMenu1.add( item_visualizar_anexo );
        lb_anexo_doc.setComponentPopupMenu( jPopupMenu1 );

    }

    private void visualizar_anexo()
    {
        try
        {
            String ficheiro_imagem = DVML.AGENDA_FUNCIONARIO_ANEXO_PATH + jList2.getSelectedValue();
            File myFile = new File( ficheiro_imagem );
            Desktop.getDesktop().open( myFile );
        }
        catch ( Exception ex )
        {
            Logger.getLogger( FichaFuncionarioVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    class TratarEvento implements KeyListener
    {

        String prefixo = "";

        @Override
        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                prefixo = txtIniciaisNome.getText().trim() + key;
//                adicionar( prefixo );

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {
                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
//                    adicionar( prefixo );
                }
                catch ( Exception e )
                {

                }
            }
        }

        @Override
        public void keyReleased( KeyEvent evt )
        {

        }

        @Override
        public void keyTyped( KeyEvent evt )
        {
        }

    }

//    private void adicionar( String nome )
//    {
//
//        Iterator<TbFuncionario> iterator = lista_jogador.iterator();
//
//        listModelJogador.clear();
//        while ( iterator.hasNext() )
//        {
//            Jogador next = iterator.next();
//
//            if ( next.getNome().toLowerCase().contains( nome.toLowerCase() ) )
//            {
//                listModelJogador.addElement( next.getNome() );
//            }
//
//        }
//
//    }
    private int getIdFuncionario()
    {
        return funcionarioDao.getFuncionarioByNomes( String.valueOf( cmbFuncionario.getSelectedItem() ) );
    }

    private void remover_agregado_familiar_item()
    {
        DefaultTableModel modelo = MetodosUtil.preparar_tabela( tabela_agregado_familiar );
        int linha = tabela_agregado_familiar.getSelectedRow();

        modelo.removeRow( linha );

    }

}
