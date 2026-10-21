/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import java.sql.Connection;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialOceanicIJTheme;

import comercial.controller.DadosInstituicaoController;
import dao.CaixaDao;
import dao.DadosInstituicaoDao;
import dao.DocumentoDao;
import dao.EmpresaDao;
import dao.UsuarioDao;
import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
//import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import entity.TbDadosInstituicao;
import entity.TbUsuario;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
//import javax.swing.UIManager;
import util.BDConexao;
import util.BackupUsb;
import util.JPAEntityMannagerFactoryUtil;
//import util.LicensaUtil;
import util.MetodosUtil;
import static util.MetodosUtil.startBackGroundProcesses;
import util.SingleInstanceLock;
import util.SingleInstanceManager;
import util.cronjob.QuartzApp;
import util.cronjob.StockAutoCheckService;
import util.cronjob.ValidadorJob;

/**
 *
 * @author Domingos Dala Vunge
 */
public class LoginVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static DadosInstituicaoController dadosInstituicaoController;
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private EmpresaDao empresaDao = new EmpresaDao( emf );
    private static CaixaDao caixaDao = new CaixaDao( emf );

    private DocumentoDao documentoDao = new DocumentoDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    public Date horaTerminoVenda;
//    private LicensaUtil licensaUtil;
    private static BDConexao conexao;
    private TbDadosInstituicao d;
//    private int idUser;
    private boolean contagem = true;
    private Timer logoutTimer;
    private final int TIMEOUT = 60 * 60 * 1000; // 60 minutos em milissegundos
    private int id_user, id_empresa;
    private javax.swing.Timer piscarTimer;

    public LoginVisao( BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );

// Obtém a instância única e garante que a conexão está ativa
        conexao = BDConexao.getInstancia();
        conexao.getConnectionAtiva(); // força a ativação da conexão, se ainda não estiver

// Cria o controller com a instância de BDConexao já inicializada
        dadosInstituicaoController = new DadosInstituicaoController( conexao );

// Busca os dados da instituição (id = 1)
        d = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );

        mostrar_empresas();

        prazo();


// Mostrar o painel de produtos a expirar apenas para Farmácia
if (d != null && "Farmacia".equalsIgnoreCase(d.getNegocio())) {
    criarPainelExpirar();
}

LoginVisao.setDefaultLookAndFeelDecorated(true);
        
//        criarPainelExpirar();   // <-- nova linha

//        LoginVisao.setDefaultLookAndFeelDecorated( true );
        try
        {
            System.out.println( "Data Ultimo: " + documentoDao.getMAxDataDoc().toString() );
        }
        catch ( Exception e )
        {
        }
//        verificar_data_sistema();
        txtUserName.requestFocus();
        //Envio de Email
        try
        {
            verificarEnvioEmail();
        }
        catch ( Exception e )
        {
        }
        iniciarMonitoramento();
        iniciarPiscarLabelJLDataFecho();

    }

    public void iniciarPiscarLabelJLDataFecho()
    {
        piscarTimer = new javax.swing.Timer( 500, new ActionListener()
        {
            private boolean ligado = false;

            @Override
            public void actionPerformed( ActionEvent e )
            {
                if ( ligado )
                {
                    JLDataFecho.setForeground( Color.BLACK );
                }
                else
                {
                    JLDataFecho.setForeground( Color.RED );
                }
                ligado = !ligado;
            }
        } );
        piscarTimer.start();
    }

    public void pararPiscarLabelJLDataFecho()
    {
        if ( piscarTimer != null && piscarTimer.isRunning() )
        {
            piscarTimer.stop();
            JLDataFecho.setForeground( Color.BLACK );  // Garante a cor normal
        }
    }

    public LoginVisao( int idUser )
    {

        initComponents();
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );

        conexao = BDConexao.getInstancia();
//        setTitle( "BEM VINDO AO " + DVML.NAME_SOFTWARE + " " + DVML.VERSION_SOFTWARE );

//        if (Objects.nonNull(conexao)) {
//            conexao.close();
//        }
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        d = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );
        horaTerminoVenda = d.getHoraTerminoVenda();
//        this.idUser = idUser;
//        licensaUtil = new LicensaUtil( conexao );
//        registerLog( conexao );
        mostrar_empresas();
        
        prazo();

// Mostrar o painel de produtos a expirar apenas para Farmácia
if (d != null && "Farmacia".equalsIgnoreCase(d.getNegocio())) {
    criarPainelExpirar();
}

LoginVisao.setDefaultLookAndFeelDecorated(true);
//        prazo();
        
//        criarPainelExpirar();   // <-- nova linha

//        LoginVisao.setDefaultLookAndFeelDecorated( true );
        try
        {
            System.out.println( "Data Ultimo: " + documentoDao.getMAxDataDoc().toString() );
        }
        catch ( Exception e )
        {
        }
//        verificar_data_sistema();
        txtUserName.requestFocus();
        //Envio de Email
        try
        {
            verificarEnvioEmail();
        }
        catch ( Exception e )
        {
        }

        iniciarMonitoramento();

    }

    private void verificarEnvioEmail()
    {

        if ( d.getEnviarEmail().equals( "Sim" ) )
        {
            BTnSair.setVisible( false );
            QuartzApp.executarCronjob( ValidadorJob.class, ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 ) );
        }
        else
        {
            BTnSair.setVisible( true );
        }

    }

    public void mostrar_empresas()
    {
        cmbEmpresa.setModel( new DefaultComboBoxModel( ( Vector ) empresaDao.getAllEmpresa() ) );
    }

    private void prazo()
    {

        TbDadosInstituicao dadosInstituicao = dadosInstituicaoDao.findTbDadosInstituicao( 1 );
        JEmpresa.setText( "Empresa " + dadosInstituicao.getNome() );
        JLNif.setText( "NIF " + dadosInstituicao.getNif() );
        JLDataFecho.setText( "Contrato válido até " + ( dadosInstituicao.getDataFecho().getDate() ) + " / " + ( dadosInstituicao.getDataFecho().getMonth() + 1 ) + " / " + ( 1900 + dadosInstituicao.getDataFecho().getYear() ) );
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
        jLabel2 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pswSenha = new javax.swing.JPasswordField();
        cmbEmpresa = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        BTnSair = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JLDataFecho = new javax.swing.JLabel();
        JLNif = new javax.swing.JLabel();
        JEmpresa = new javax.swing.JLabel();
        Soft = new javax.swing.JLabel();
        Soft1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Adobe Garamond Pro Bold", 0, 24), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel2.setFont(new java.awt.Font("Trajan Pro", 1, 12)); // NOI18N
        jLabel2.setText("         User Name:");
        jLabel2.setToolTipText("User Name");

        txtUserName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        txtUserName.setForeground(new java.awt.Color(51, 153, 0));
        txtUserName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtUserNameActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Trajan Pro", 1, 12)); // NOI18N
        jLabel3.setText("               Senha:");

        pswSenha.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        pswSenha.setForeground(new java.awt.Color(51, 153, 0));
        pswSenha.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pswSenhaActionPerformed(evt);
            }
        });

        cmbEmpresa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbEmpresaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Trajan Pro", 1, 11)); // NOI18N
        jLabel6.setText("         Empresa:");
        jLabel6.setToolTipText("User Name");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/confirmacao.png"))); // NOI18N
        jButton1.setText("Confirmar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Noteworthy", 1, 11)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens100x100/funcionarioAtivado100x100.png"))); // NOI18N
        jButton3.setText("BEM VINDO");

        BTnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/sair32x32.png"))); // NOI18N
        BTnSair.setText("Sair");
        BTnSair.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                BTnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbEmpresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName)
                            .addComponent(pswSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(pswSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BTnSair)
                .addGap(23, 23, 23))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("desenvolvido pela DVML-COMERCIAL , Lda / Contactos: 940537124 /923409284");

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("email: dvml.comerciall@gmail.com");

        JLDataFecho.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JLDataFecho.setForeground(new java.awt.Color(255, 255, 255));
        JLDataFecho.setText("jLabel7");

        JLNif.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JLNif.setForeground(new java.awt.Color(255, 255, 255));
        JLNif.setText("jLabel7");

        JEmpresa.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        JEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        JEmpresa.setText("jLabel7");

        Soft.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        Soft.setForeground(new java.awt.Color(255, 255, 255));
        Soft.setText("KITANDA 1.1.   Sistema Certificado pela AGT. Nº 258");

        Soft1.setFont(new java.awt.Font("American Typewriter", 1, 48)); // NOI18N
        Soft1.setForeground(new java.awt.Color(255, 255, 255));
        Soft1.setText("KITANDA 1.1");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img30.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLNif, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLDataFecho, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Soft, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(517, 517, 517)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(Soft1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(201, 201, 201)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(160, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(JEmpresa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JLNif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Soft)
                .addGap(48, 48, 48)
                .addComponent(JLDataFecho)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 408, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(221, 221, 221))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Soft1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(137, 137, 137))))
            .addGroup(layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        Soft.getAccessibleContext().setAccessibleName("KITANDA 1.1.   Sistema Certificado pela AGT. Nº 258");
        Soft1.getAccessibleContext().setAccessibleName("KITANDA 1.1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:

        pswSenha.requestFocus();
//        try
//        {
//            Runtime.getRuntime().exec( "cmd /c osk" );
//            txtUserName.grabFocus();
//        }
//        catch ( IOException e )
//        {
//        }


    }//GEN-LAST:event_txtUserNameActionPerformed

    private void pswSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswSenhaActionPerformed
        // TODO add your handling code here:
        try
        {
            if ( MetodosUtil.acesso_sistema() )
            {
//                if ( licensaUtil.licenca_valida( new Date() ) )
//                {
                entrar_sistema();
//                fazerBackupAgora();
//                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Impossível aceder ao sistema.\nDeves abrir um novo ano económico.\nContacte o seu fornecedor.", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_pswSenhaActionPerformed

    private void cmbEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpresaActionPerformed
//        System.out.println("ID EMPRESA:"+getIdEmpresa());
    }//GEN-LAST:event_cmbEmpresaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try
        {
            if ( MetodosUtil.acesso_sistema() )
            {
//                if ( licensaUtil.licenca_valida( new Date() ) )
//                {

                entrar_sistema();
//                fazerBackupAgora();
//                }

            }
            else
            {
                JOptionPane.showMessageDialog( null, "Impossível aceder ao sistema.\nDeves abrir um novo ano económico.\nContacte o seu fornecedor.", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void BTnSairActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_BTnSairActionPerformed
    {//GEN-HEADEREND:event_BTnSairActionPerformed
        try
        {
            dispose();
            BackupUsb.realizarBackup();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        System.exit( 0 );
    }//GEN-LAST:event_BTnSairActionPerformed

//    public static void alterar_status_botao() {
//
//        if (!caixaDao.existeCaixas()) {
////            btn_abertura_dia.setEnabled(true);
//            jButton1.setEnabled(false);
//        } else if (caixaDao.existe_abertura() && caixaDao.existe_fecho()) {
////            btn_abertura_dia.setEnabled(true);
//            jButton1.setEnabled(false);
//        } else {
////            btn_abertura_dia.setEnabled(false);
//            jButton1.setEnabled(true);
//        }
//    }
    public void entrar_sistema() throws SQLException, UnknownHostException
    {

        TbUsuario usuario = usuarioDao.getUsuariowithEncriptedPass( getUserName(), getSenha() );
        TbDadosInstituicao dadosInstituicao = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );

        if ( usuario == null )
        {
            JOptionPane.showMessageDialog( null, "Erro Senha errada ou  User Name" );
        }
        else
        {
            if ( usuario.getDataUltimoAcesso() == null )
            {
                alterarSenha();
            }
            else
            {

                id_user = usuario.getCodigo();
                id_empresa = getIdEmpresa();
                try
                {
//                    BackupUtil.fazerBackupAgora();
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }

//                dispose();
                switch (usuario.getIdTipoUsuario().getIdTipoUsuario())
                {

                    //1 - Administrador
                    case 1:
//                        dispose();

                        System.err.println( "getCodico_Utilizador (): " + getCodico_Utilizador() );
                        System.err.println( "getCodico_Empresa (): " + getIdEmpresa() );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new RootVisao( getCodico_Utilizador(), getIdEmpresa(), true, BDConexao.getInstancia() ).setVisible( true );
                        limpar();
                        break;
//                    Sub Administrador
                    case 2:
//                        dispose();

                        System.err.println( "getCodico_Utilizador (): " + getCodico_Utilizador() );
                        System.err.println( "getCodico_Empresa (): " + getIdEmpresa() );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        if ( dadosInstituicao.getNegocio().equals( "Lavandaria" ) )
                        {

                            new JanelaFrontOfficeLavandariaVisao( getCodico_Utilizador(), BDConexao.getInstancia() ).setVisible( true );

                        }
                        else
                        {

                            new RootVisao( getCodico_Utilizador(), getIdEmpresa(), true, BDConexao.getInstancia() ).setVisible( true );
                        }
                        limpar();
                        break;
                    //2 - Caixa
                    case 3:
//                        dispose();
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new VendaUsuarioVisao( getCodico_Utilizador(), BDConexao.getInstancia() ).setVisible( true );
                        limpar();
                        break;
                    //3 - VendaPO
                    case 4:
//                        dispose();
//                        new VendaPOSVisao( BDConexao.getInstancia(), DVML.ARMAZEM_LOJA, getCodico_Utilizador() ).setVisible( true );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new FrontOfficeVisao( getCodico_Utilizador(), BDConexao.getInstancia() ).setVisible( true );
                        limpar();
                        break;

                    default:
                        JOptionPane.showMessageDialog( null, "Erro Senha errado ou  User Name" );
                        break;
                }
            }
        }
    }

    public int getCodico_Utilizador()
    {
        return BDConexao.getCodigoUSuario( getUserName(), getSenha() );
    }

    public int getIdEmpresa()
    {
        return empresaDao.getIdByDescricao( cmbEmpresa.getSelectedItem().toString() );
    }

    public String getUserName()
    {
        return txtUserName.getText().toLowerCase();
    }

    public String getSenha()
    {
        return pswSenha.getText();
    }

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
    {
        /* Set the Nimbus look and feel */
        try
        {

            Locale.setDefault( new Locale( "pt", "AO" ) );
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
            {
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( Exception ex )
        {
            java.util.logging.Logger.getLogger( LoginVisao.class.getName() )
                    .log( java.util.logging.Level.SEVERE, null, ex );
        }

        // 🔒 Verifica instância única ANTES de abrir interface
//        if ( SingleInstanceLock.isAlreadyRunning() )
//        {
//            JOptionPane.showMessageDialog( null, "Atenção\nO sistema já está aberto!" );
//            System.exit( 0 );
//        }
        if ( SingleInstanceManager.isInstanceRunning() )
        {
            // A instância antiga será fechada automaticamente.
            // Depois disto a nova abertura funciona normalmente.
            System.exit( 0 );
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {

                    UIManager.setLookAndFeel( new SyntheticaBlackStarLookAndFeel() );
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//                    FlatMTMaterialOceanicIJTheme.setup();
                    // JTextField
                    UIManager.put( "TextField.background", new Color( 0, 255, 255 ) ); // fundo
                    UIManager.put( "TextField.foreground", new Color( 0, 51, 102 ) );  // texto
                    UIManager.put( "TextField.caretForeground", Color.RED );         // cursor
                    UIManager.put( "TextField.selectionBackground", Color.YELLOW );  // seleção (amarelo só no JTextField)
                    UIManager.put( "TextField.selectionForeground", Color.BLUE );    // texto selecionado

                    //JComboBox
                    UIManager.put( "ComboBox.background", new Color( 0, 255, 255 ) );
                    UIManager.put( "ComboBox.foreground", new Color( 0, 51, 102 ) );
                    UIManager.put( "ComboBox.selectionBackground", new Color( 173, 216, 230 ) ); // azul claro
                    UIManager.put( "ComboBox.selectionForeground", Color.BLACK );

                    //JTable
                    UIManager.put( "Table.background", new Color( 0, 255, 255 ) );
                    UIManager.put( "Table.foreground", new Color( 0, 51, 102 ) );
                    UIManager.put( "Table.selectionBackground", new Color( 0, 120, 215 ) ); // azul estilo Windows
                    UIManager.put( "Table.selectionForeground", Color.WHITE );      // texto branco na seleção
                    UIManager.put( "Table.gridColor", Color.GRAY );
                    UIManager.put( "TableHeader.background", new Color( 0, 200, 200 ) );
                    UIManager.put( "TableHeader.foreground", Color.BLACK );

                }
                catch ( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Erro no Look and Feel: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE );
                }

                // INICIAR PROCESSOS EM BACKGROUND
               // if ( startBackGroundProcesses() )
                if ( true )
                {
                    BDConexao conexao = BDConexao.getInstancia();
                    StockAutoCheckService service = new StockAutoCheckService( conexao.getConnectionAtiva() );

//                    try
//                    {
//                        service.verificarOuSalvarStockDiario();
//                    }
//                    catch ( SQLException e )
//                    {
//                        e.printStackTrace();
//                    }

                    if ( true )
                    { // <- aqui você pode colocar sua regra de licença
                        new LoginVisao( BDConexao.getInstancia() ).setVisible( true );

                    }
                    else
                    {
                        JOptionPane.showMessageDialog( null,
                                "A sua licença expirou.\nPor favor contacte a DVML-COMERCIAL, Lda\nContactos: 923409284 / 940537124",
                                "AVISO", JOptionPane.WARNING_MESSAGE );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( null, "NÃO FOI POSSÍVEL ACEDER A BASE DE DADOS",
                            "ERRO NA BASE DE DADOS", JOptionPane.ERROR_MESSAGE );
                }
            }
        } );
    }
    
      // =====================================================================
    // SUBSTITUI, na classe LoginVisao, TODO o bloco do painel "Produtos a expirar"
    // (desde "private static final int EXP_X..." até ao fim da classe RendererExpirar).
    // Os imports e as chamadas criarPainelExpirar() nos construtores ficam como estão.
    //
    // Mostra apenas os lotes do SEGUNDO armazém (Farmácia). Se a empresa só tiver
    // um armazém, usa o primeiro. O nome do armazém aparece no título do painel.
    // =====================================================================

    // ------------------------------------------------------------------
    // Painel: produtos a expirar (visível antes do login)
    // ------------------------------------------------------------------
    // Posição e tamanho do painel (ajusta aqui se quiseres mexer na estética)
    private static final int EXP_X = 33, EXP_Y = 250, EXP_W = 540, EXP_H = 360;
    private static final int DIAS_INICIAIS = 60;

    // Índice do armazém mostrado, na lista ordenada por código: 1 = SEGUNDO (Farmácia)
    private static final int ARMAZEM_EXPIRAR_INDICE = 1;

    private static final Color COR_EXPIRADO = new Color( 255, 170, 170 ); // vermelho claro
    private static final Color COR_URGENTE = new Color( 255, 204, 153 );  // laranja claro (<= 30 dias)
    private static final Color COR_URGENTE_FORTE = new Color( 255, 120, 0 ); // laranja forte (fase "acesa" do piscar)

    // Alarme: toca ALARME_TOQUES vezes quando passam a existir linhas a piscar
    private static final int ALARME_TOQUES = 3;
    private static final int ALARME_FREQUENCIA_HZ = 880; // nota Lá
    private static final int ALARME_DURACAO_MS = 250;    // duração de cada toque
    private static final int ALARME_PAUSA_MS = 150;      // pausa entre toques
    private static final Color COR_AVISO = new Color( 255, 245, 170 );    // amarelo claro (> 30 dias)

    // ASSUMIDO: tb_armazem(codigo, designacao) e lote.fk_armazem
    private static final String SQL_ARMAZENS_EXPIRAR
            = "SELECT codigo, designacao FROM tb_armazem ORDER BY codigo ASC";

    private static final String SQL_A_EXPIRAR
            = "SELECT p.codigo, p.designacao, l.numero_lote, l.data_validade, "
            + "       DATEDIFF( l.data_validade, CURDATE() ) AS dias_restantes "
            + "FROM lote l "
            + "INNER JOIN tb_produto p ON p.codigo = l.fk_produto "
            + "WHERE l.qtd > 0 AND l.estado IN ('ACTIVO','EXPIRADO') "
            + "  AND l.fk_armazem = ? "
            + "  AND l.data_validade <= DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
            + "  AND ( ? = 1 OR l.data_validade >= CURDATE() ) "
            + "ORDER BY l.data_validade ASC, p.designacao ASC, l.id ASC";

    private int idArmazemExpirar = -1;
    private String nomeArmazemExpirar = "";

    private final JSpinner spDiasExpirar = new JSpinner( new SpinnerNumberModel( DIAS_INICIAIS, 1, 3650, 1 ) );
    private final JCheckBox chkExpirados = new JCheckBox( "Incluir já expirados com stock", true );
    private final DefaultTableModel modExpirar = new DefaultTableModel(
            new String[]
            {
                "Código", "Designação Produto", "Lote", "Data Validade"
            }, 0 )
    {
        @Override
        public boolean isCellEditable( int row, int column )
        {
            return false;
        }
    };
    private final JTable tblExpirar = new JTable( modExpirar );
    private final javax.swing.JLabel lblTotalExpirar = new javax.swing.JLabel( " " );
    private final List<Integer> diasExpirar = new ArrayList<>(); // dias restantes de cada linha (para a cor)
    private final SimpleDateFormat fmtDataExpirar = new SimpleDateFormat( "dd/MM/yyyy" );
    private javax.swing.Timer refreshExpirarTimer;

    // Piscar das linhas laranja (a expirar em 0 a 30 dias)
    private javax.swing.Timer piscarExpirarTimer;
    private boolean piscarExpirarLigado = false;
    private boolean temLinhasUrgentes = false;
    private volatile boolean alarmeATocar = false;

    /** Descobre o armazém a mostrar: o 2.º da lista (ou o 1.º, se só houver um). */
    private void resolverArmazemExpirar()
    {
        try
        {
            Connection cn = BDConexao.getInstancia().getConnectionAtiva();
            try ( PreparedStatement ps = cn.prepareStatement( SQL_ARMAZENS_EXPIRAR );  ResultSet rs = ps.executeQuery() )
            {
                int i = 0;
                while ( rs.next() )
                {
                    if ( i == 0 || i == ARMAZEM_EXPIRAR_INDICE )
                    {
                        idArmazemExpirar = rs.getInt( "codigo" );
                        nomeArmazemExpirar = rs.getString( "designacao" );
                    }
                    if ( i == ARMAZEM_EXPIRAR_INDICE )
                    {
                        break;
                    }
                    i++;
                }
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            idArmazemExpirar = -1;
        }
    }

    /** Cria o painel e coloca-o por cima do fundo, sem tocar no layout gerado. */
    private void criarPainelExpirar()
    {
        resolverArmazemExpirar();

        Font negrito = new Font( "Tahoma", Font.BOLD, 12 );

        javax.swing.JLabel lbl1 = new javax.swing.JLabel( "A expirar nos próximos" );
        javax.swing.JLabel lbl2 = new javax.swing.JLabel( "dias" );
        for ( javax.swing.JLabel l : new javax.swing.JLabel[]
        {
            lbl1, lbl2
        } )
        {
            l.setFont( negrito );
            l.setForeground( Color.WHITE );
        }
        spDiasExpirar.setPreferredSize( new Dimension( 75, 26 ) );
        chkExpirados.setOpaque( false );
        chkExpirados.setForeground( Color.WHITE );
        chkExpirados.setFont( negrito );

        JPanel topo = new JPanel( new FlowLayout( FlowLayout.LEFT, 6, 2 ) );
        topo.setOpaque( false );
        topo.add( lbl1 );
        topo.add( spDiasExpirar );
        topo.add( lbl2 );
        topo.add( chkExpirados );

        // Tabela
        tblExpirar.setRowHeight( 24 );
        tblExpirar.getTableHeader().setReorderingAllowed( false );
        tblExpirar.getTableHeader().setFont( negrito );
        tblExpirar.setFillsViewportHeight( true );
        tblExpirar.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        int[] alinhamentos =
        {
            SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.CENTER
        };
        int[] larguras =
        {
            70, 260, 100, 100
        };
        for ( int c = 0; c < alinhamentos.length; c++ )
        {
            tblExpirar.getColumnModel().getColumn( c ).setCellRenderer( new RendererExpirar( alinhamentos[c] ) );
            tblExpirar.getColumnModel().getColumn( c ).setPreferredWidth( larguras[c] );
        }
        JScrollPane scroll = new JScrollPane( tblExpirar );
        scroll.getViewport().setBackground( new Color( 20, 25, 50 ) );

        lblTotalExpirar.setForeground( Color.WHITE );
        lblTotalExpirar.setFont( new Font( "Tahoma", Font.ITALIC, 11 ) );

        // Painel com fundo escuro semi-transparente (deixa ver a imagem de fundo)
        JPanel painel = new JPanel( new BorderLayout( 0, 6 ) )
        {
            @Override
            protected void paintComponent( Graphics g )
            {
                g.setColor( getBackground() );
                g.fillRect( 0, 0, getWidth(), getHeight() );
                super.paintComponent( g );
            }
        };
        painel.setOpaque( false );
        painel.setBackground( new Color( 10, 15, 40, 190 ) );
        String titulo = nomeArmazemExpirar.isEmpty()
                ? "Produtos a expirar"
                : "Produtos a expirar - " + nomeArmazemExpirar;
        painel.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.WHITE ),
                        titulo, javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP, new Font( "Tahoma", Font.BOLD, 14 ), Color.WHITE ),
                BorderFactory.createEmptyBorder( 4, 6, 6, 6 ) ) );
        painel.add( topo, BorderLayout.NORTH );
        painel.add( scroll, BorderLayout.CENTER );
        painel.add( lblTotalExpirar, BorderLayout.SOUTH );
        painel.setBounds( EXP_X, EXP_Y, EXP_W, EXP_H );

        getLayeredPane().add( painel, JLayeredPane.PALETTE_LAYER );

        // Actualização dinâmica: setas do spinner, escrita manual e checkbox
        spDiasExpirar.addChangeListener( e -> agendarRefreshExpirar() );
        JFormattedTextField tf = ( ( JSpinner.DefaultEditor ) spDiasExpirar.getEditor() ).getTextField();
        tf.getDocument().addDocumentListener( new DocumentListener()
        {
            @Override
            public void insertUpdate( DocumentEvent e )
            {
                agendarRefreshExpirar();
            }

            @Override
            public void removeUpdate( DocumentEvent e )
            {
                agendarRefreshExpirar();
            }

            @Override
            public void changedUpdate( DocumentEvent e )
            {
                agendarRefreshExpirar();
            }
        } );
        chkExpirados.addActionListener( e -> carregarProdutosAExpirar() );

        carregarProdutosAExpirar();
        iniciarPiscarExpirar();
    }

    /** Faz piscar as linhas laranja (500 ms), para chamar a atenção do utilizador. */
    private void iniciarPiscarExpirar()
    {
        piscarExpirarTimer = new javax.swing.Timer( 500, e ->
        {
            if ( !isDisplayable() )
            {
                ( ( javax.swing.Timer ) e.getSource() ).stop(); // janela fechada: pára o timer
                return;
            }
            piscarExpirarLigado = !piscarExpirarLigado;
            if ( temLinhasUrgentes )
            {
                tblExpirar.repaint();
            }
        } );
        piscarExpirarTimer.start();
    }

    /** Espera 400 ms depois da última tecla antes de consultar a BD. */
    private void agendarRefreshExpirar()
    {
        if ( refreshExpirarTimer == null )
        {
            refreshExpirarTimer = new javax.swing.Timer( 400, e -> carregarProdutosAExpirar() );
            refreshExpirarTimer.setRepeats( false );
        }
        refreshExpirarTimer.restart();
    }

    /** Lê o nº de dias directamente do texto do spinner (mesmo antes de carregar Enter). */
    private int obterDiasExpirar()
    {
        String txt = ( ( JSpinner.DefaultEditor ) spDiasExpirar.getEditor() ).getTextField()
                .getText().replaceAll( "[^0-9]", "" );
        if ( txt.isEmpty() )
        {
            return -1;
        }
        try
        {
            int d = Integer.parseInt( txt );
            return ( d >= 1 && d <= 3650 ) ? d : -1;
        }
        catch ( NumberFormatException ex )
        {
            return -1;
        }
    }

    private void carregarProdutosAExpirar()
    {
        int dias = obterDiasExpirar();
        if ( dias < 0 )
        {
            return; // campo vazio ou valor inválido: mantém a lista anterior
        }

        if ( idArmazemExpirar < 0 )
        {
            resolverArmazemExpirar(); // tenta outra vez (ex.: BD indisponível no arranque)
        }

        modExpirar.setRowCount( 0 );
        boolean tinhaUrgentes = temLinhasUrgentes;
        diasExpirar.clear();
        temLinhasUrgentes = false;

        if ( idArmazemExpirar < 0 )
        {
            lblTotalExpirar.setText( " Não foi possível identificar o armazém." );
            return;
        }

        try
        {
            Connection cn = BDConexao.getInstancia().getConnectionAtiva();
            try ( PreparedStatement ps = cn.prepareStatement( SQL_A_EXPIRAR ) )
            {
                ps.setInt( 1, idArmazemExpirar );
                ps.setInt( 2, dias );
                ps.setInt( 3, chkExpirados.isSelected() ? 1 : 0 );
                try ( ResultSet rs = ps.executeQuery() )
                {
                    while ( rs.next() )
                    {
                        int diasRestantes = rs.getInt( "dias_restantes" );
                        diasExpirar.add( diasRestantes );
                        if ( diasRestantes >= 0 && diasRestantes <= 30 )
                        {
                            temLinhasUrgentes = true;
                        }
                        modExpirar.addRow( new Object[]
                        {
                            rs.getInt( "codigo" ),
                            rs.getString( "designacao" ),
                            rs.getString( "numero_lote" ),
                            rs.getDate( "data_validade" ) == null ? "" : fmtDataExpirar.format( rs.getDate( "data_validade" ) )
                        } );
                    }
                }
            }
            // Alarme: só quando as linhas a piscar APARECEM (não repete a cada actualização)
            if ( temLinhasUrgentes && !tinhaUrgentes )
            {
                tocarAlarmeExpirar();
            }
            lblTotalExpirar.setText( " " + modExpirar.getRowCount() + " lote(s)   |   Vermelho: expirado   "
                    + "Laranja: até 30 dias   Amarelo: mais de 30 dias" );
        }
        catch ( Exception ex )
        {
            // Nunca bloquear o login por causa desta lista
            ex.printStackTrace();
            lblTotalExpirar.setText( " Não foi possível carregar a lista: " + ex.getMessage() );
        }
    }

    /** Toca ALARME_TOQUES toques, numa thread própria (não bloqueia o ecrã de login). */
    private void tocarAlarmeExpirar()
    {
        if ( alarmeATocar )
        {
            return; // já está a tocar
        }
        alarmeATocar = true;

        Thread t = new Thread( () ->
        {
            try
            {
                for ( int i = 0; i < ALARME_TOQUES; i++ )
                {
                    tocarTomExpirar( ALARME_FREQUENCIA_HZ, ALARME_DURACAO_MS );
                    Thread.sleep( ALARME_PAUSA_MS );
                }
            }
            catch ( Exception ex )
            {
                // sem placa de som / linha de áudio indisponível: usa o beep do sistema
                try
                {
                    for ( int i = 0; i < ALARME_TOQUES; i++ )
                    {
                        Toolkit.getDefaultToolkit().beep();
                        Thread.sleep( ALARME_DURACAO_MS + ALARME_PAUSA_MS );
                    }
                }
                catch ( Exception ignorar )
                {
                }
            }
            finally
            {
                alarmeATocar = false;
            }
        }, "alarme-expirar" );
        t.setDaemon( true );
        t.start();
    }

    /** Gera e toca um tom (onda sinusoidal) com fade curto para não estalar. */
    private static void tocarTomExpirar( int frequenciaHz, int duracaoMs ) throws Exception
    {
        float taxa = 44100f;
        javax.sound.sampled.AudioFormat formato = new javax.sound.sampled.AudioFormat( taxa, 16, 1, true, false );
        try ( javax.sound.sampled.SourceDataLine linha = javax.sound.sampled.AudioSystem.getSourceDataLine( formato ) )
        {
            linha.open( formato );
            linha.start();

            int amostras = (int) ( taxa * duracaoMs / 1000 );
            byte[] buf = new byte[ amostras * 2 ];
            for ( int i = 0; i < amostras; i++ )
            {
                double fade = Math.min( 1.0, Math.min( i, amostras - i ) / ( taxa * 0.01 ) ); // 10 ms
                short v = (short) ( Math.sin( 2 * Math.PI * frequenciaHz * i / taxa ) * 0.6 * Short.MAX_VALUE * fade );
                buf[2 * i] = (byte) ( v & 0xff );
                buf[2 * i + 1] = (byte) ( ( v >> 8 ) & 0xff );
            }
            linha.write( buf, 0, buf.length );
            linha.drain();
        }
    }

    /** Pinta a linha conforme os dias restantes. */
    private class RendererExpirar extends DefaultTableCellRenderer
    {

        RendererExpirar( int alinhamento )
        {
            setHorizontalAlignment( alinhamento );
            setOpaque( true );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column )
        {
            Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            if ( !isSelected )
            {
                int dias = row < diasExpirar.size() ? diasExpirar.get( row ) : 0;
                Color fundo;
                if ( dias < 0 )
                {
                    fundo = COR_EXPIRADO;
                }
                else if ( dias <= 30 )
                {
                    fundo = piscarExpirarLigado ? COR_URGENTE_FORTE : COR_URGENTE; // pisca
                }
                else
                {
                    fundo = COR_AVISO;
                }
                c.setBackground( fundo );
                c.setForeground( Color.BLACK );
            }
            return c;
        }
    }



//    // ------------------------------------------------------------------
//    // Painel: produtos a expirar (visível antes do login)
//    // ------------------------------------------------------------------
//    // Posição e tamanho do painel (ajusta aqui se quiseres mexer na estética)
//    private static final int EXP_X = 33, EXP_Y = 250, EXP_W = 540, EXP_H = 360;
//    private static final int DIAS_INICIAIS = 60;
//
//    private static final Color COR_EXPIRADO = new Color( 255, 170, 170 ); // vermelho claro
//    private static final Color COR_URGENTE = new Color( 255, 204, 153 );  // laranja claro (<= 30 dias)
//    private static final Color COR_AVISO = new Color( 255, 245, 170 );    // amarelo claro (> 30 dias)
//
//    // ASSUMIDO: tb_armazem(codigo, designacao)  -> ajusta se o nome da tabela for diferente
//    // LEFT JOIN: se um lote tiver um armazém inexistente, continua a aparecer (com "-")
//    private static final String SQL_A_EXPIRAR
//            = "SELECT p.codigo, p.designacao, COALESCE( a.designacao, '-' ) AS armazem, "
//            + "       l.numero_lote, l.data_validade, "
//            + "       DATEDIFF( l.data_validade, CURDATE() ) AS dias_restantes "
//            + "FROM lote l "
//            + "INNER JOIN tb_produto p ON p.codigo = l.fk_produto "
//            + "LEFT JOIN tb_armazem a ON a.codigo = l.fk_armazem "
//            + "WHERE l.qtd > 0 AND l.estado IN ('ACTIVO','EXPIRADO') "
//            + "  AND l.data_validade <= DATE_ADD( CURDATE(), INTERVAL ? DAY ) "
//            + "  AND ( ? = 1 OR l.data_validade >= CURDATE() ) "
//            + "ORDER BY p.designacao ASC, l.data_validade ASC, a.designacao ASC, l.id ASC";
//
//    private final JSpinner spDiasExpirar = new JSpinner( new SpinnerNumberModel( DIAS_INICIAIS, 1, 3650, 1 ) );
//    private final JCheckBox chkExpirados = new JCheckBox( "Incluir já expirados com stock", true );
//    private final DefaultTableModel modExpirar = new DefaultTableModel(
//            new String[]
//            {
//                "Código", "Designação Produto", "Armazém", "Lote", "Data Validade"
//            }, 0 )
//    {
//        @Override
//        public boolean isCellEditable( int row, int column )
//        {
//            return false;
//        }
//    };
//    private final JTable tblExpirar = new JTable( modExpirar );
//    private final javax.swing.JLabel lblTotalExpirar = new javax.swing.JLabel( " " );
//    private final List<Integer> diasExpirar = new ArrayList<>(); // dias restantes de cada linha (para a cor)
//    private final SimpleDateFormat fmtDataExpirar = new SimpleDateFormat( "dd/MM/yyyy" );
//    private javax.swing.Timer refreshExpirarTimer;
//
//    /** Cria o painel e coloca-o por cima do fundo, sem tocar no layout gerado. */
//    private void criarPainelExpirar()
//    {
//        Font negrito = new Font( "Tahoma", Font.BOLD, 12 );
//
//        javax.swing.JLabel lbl1 = new javax.swing.JLabel( "A expirar nos próximos" );
//        javax.swing.JLabel lbl2 = new javax.swing.JLabel( "dias" );
//        for ( javax.swing.JLabel l : new javax.swing.JLabel[]
//        {
//            lbl1, lbl2
//        } )
//        {
//            l.setFont( negrito );
//            l.setForeground( Color.WHITE );
//        }
//        spDiasExpirar.setPreferredSize( new Dimension( 75, 26 ) );
//        chkExpirados.setOpaque( false );
//        chkExpirados.setForeground( Color.WHITE );
//        chkExpirados.setFont( negrito );
//
//        JPanel topo = new JPanel( new FlowLayout( FlowLayout.LEFT, 6, 2 ) );
//        topo.setOpaque( false );
//        topo.add( lbl1 );
//        topo.add( spDiasExpirar );
//        topo.add( lbl2 );
//        topo.add( chkExpirados );
//
//        // Tabela
//        tblExpirar.setRowHeight( 24 );
//        tblExpirar.getTableHeader().setReorderingAllowed( false );
//        tblExpirar.getTableHeader().setFont( negrito );
//        tblExpirar.setFillsViewportHeight( true );
//        tblExpirar.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
//        int[] alinhamentos =
//        {
//            SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.CENTER
//        };
//        int[] larguras =
//        {
//            55, 190, 85, 85, 85
//        };
//        for ( int c = 0; c < alinhamentos.length; c++ )
//        {
//            tblExpirar.getColumnModel().getColumn( c ).setCellRenderer( new RendererExpirar( alinhamentos[c] ) );
//            tblExpirar.getColumnModel().getColumn( c ).setPreferredWidth( larguras[c] );
//        }
//        JScrollPane scroll = new JScrollPane( tblExpirar );
//        scroll.getViewport().setBackground( new Color( 20, 25, 50 ) );
//
//        lblTotalExpirar.setForeground( Color.WHITE );
//        lblTotalExpirar.setFont( new Font( "Tahoma", Font.ITALIC, 11 ) );
//
//        // Painel com fundo escuro semi-transparente (deixa ver a imagem de fundo)
//        JPanel painel = new JPanel( new BorderLayout( 0, 6 ) )
//        {
//            @Override
//            protected void paintComponent( Graphics g )
//            {
//                g.setColor( getBackground() );
//                g.fillRect( 0, 0, getWidth(), getHeight() );
//                super.paintComponent( g );
//            }
//        };
//        painel.setOpaque( false );
//        painel.setBackground( new Color( 10, 15, 40, 190 ) );
//        painel.setBorder( BorderFactory.createCompoundBorder(
//                BorderFactory.createTitledBorder( BorderFactory.createLineBorder( Color.WHITE ),
//                        "Produtos a expirar", javax.swing.border.TitledBorder.LEFT,
//                        javax.swing.border.TitledBorder.TOP, new Font( "Tahoma", Font.BOLD, 14 ), Color.WHITE ),
//                BorderFactory.createEmptyBorder( 4, 6, 6, 6 ) ) );
//        painel.add( topo, BorderLayout.NORTH );
//        painel.add( scroll, BorderLayout.CENTER );
//        painel.add( lblTotalExpirar, BorderLayout.SOUTH );
//        painel.setBounds( EXP_X, EXP_Y, EXP_W, EXP_H );
//
//        getLayeredPane().add( painel, JLayeredPane.PALETTE_LAYER );
//
//        // Actualização dinâmica: setas do spinner, escrita manual e checkbox
//        spDiasExpirar.addChangeListener( e -> agendarRefreshExpirar() );
//        JFormattedTextField tf = ( ( JSpinner.DefaultEditor ) spDiasExpirar.getEditor() ).getTextField();
//        tf.getDocument().addDocumentListener( new DocumentListener()
//        {
//            @Override
//            public void insertUpdate( DocumentEvent e )
//            {
//                agendarRefreshExpirar();
//            }
//
//            @Override
//            public void removeUpdate( DocumentEvent e )
//            {
//                agendarRefreshExpirar();
//            }
//
//            @Override
//            public void changedUpdate( DocumentEvent e )
//            {
//                agendarRefreshExpirar();
//            }
//        } );
//        chkExpirados.addActionListener( e -> carregarProdutosAExpirar() );
//
//        carregarProdutosAExpirar();
//    }
//
//    /** Espera 400 ms depois da última tecla antes de consultar a BD. */
//    private void agendarRefreshExpirar()
//    {
//        if ( refreshExpirarTimer == null )
//        {
//            refreshExpirarTimer = new javax.swing.Timer( 400, e -> carregarProdutosAExpirar() );
//            refreshExpirarTimer.setRepeats( false );
//        }
//        refreshExpirarTimer.restart();
//    }
//
//    /** Lê o nº de dias directamente do texto do spinner (mesmo antes de carregar Enter). */
//    private int obterDiasExpirar()
//    {
//        String txt = ( ( JSpinner.DefaultEditor ) spDiasExpirar.getEditor() ).getTextField()
//                .getText().replaceAll( "[^0-9]", "" );
//        if ( txt.isEmpty() )
//        {
//            return -1;
//        }
//        try
//        {
//            int d = Integer.parseInt( txt );
//            return ( d >= 1 && d <= 3650 ) ? d : -1;
//        }
//        catch ( NumberFormatException ex )
//        {
//            return -1;
//        }
//    }
//
//    private void carregarProdutosAExpirar()
//    {
//        int dias = obterDiasExpirar();
//        if ( dias < 0 )
//        {
//            return; // campo vazio ou valor inválido: mantém a lista anterior
//        }
//
//        modExpirar.setRowCount( 0 );
//        diasExpirar.clear();
//
//        try
//        {
//            Connection cn = BDConexao.getInstancia().getConnectionAtiva();
//            try ( PreparedStatement ps = cn.prepareStatement( SQL_A_EXPIRAR ) )
//            {
//                ps.setInt( 1, dias );
//                ps.setInt( 2, chkExpirados.isSelected() ? 1 : 0 );
//                try ( ResultSet rs = ps.executeQuery() )
//                {
//                    while ( rs.next() )
//                    {
//                        diasExpirar.add( rs.getInt( "dias_restantes" ) );
//                        modExpirar.addRow( new Object[]
//                        {
//                            rs.getInt( "codigo" ),
//                            rs.getString( "designacao" ),
//                            rs.getString( "armazem" ),
//                            rs.getString( "numero_lote" ),
//                            rs.getDate( "data_validade" ) == null ? "" : fmtDataExpirar.format( rs.getDate( "data_validade" ) )
//                        } );
//                    }
//                }
//            }
//            lblTotalExpirar.setText( " " + modExpirar.getRowCount() + " lote(s)   |   Vermelho: expirado   "
//                    + "Laranja: até 30 dias   Amarelo: mais de 30 dias" );
//        }
//        catch ( Exception ex )
//        {
//            // Nunca bloquear o login por causa desta lista
//            ex.printStackTrace();
//            lblTotalExpirar.setText( " Não foi possível carregar a lista: " + ex.getMessage() );
//        }
//    }
//
//    /** Pinta a linha conforme os dias restantes. */
//    private class RendererExpirar extends DefaultTableCellRenderer
//    {
//
//        RendererExpirar( int alinhamento )
//        {
//            setHorizontalAlignment( alinhamento );
//            setOpaque( true );
//        }
//
//        @Override
//        public Component getTableCellRendererComponent( JTable table, Object value,
//                boolean isSelected, boolean hasFocus, int row, int column )
//        {
//            Component c = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
//            if ( !isSelected )
//            {
//                int dias = row < diasExpirar.size() ? diasExpirar.get( row ) : 0;
//                c.setBackground( dias < 0 ? COR_EXPIRADO : ( dias <= 30 ? COR_URGENTE : COR_AVISO ) );
//                c.setForeground( Color.BLACK );
//            }
//            return c;
//        }
//    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTnSair;
    private javax.swing.JLabel JEmpresa;
    private javax.swing.JLabel JLDataFecho;
    private javax.swing.JLabel JLNif;
    private javax.swing.JLabel Soft;
    private javax.swing.JLabel Soft1;
    private javax.swing.JComboBox<String> cmbEmpresa;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField pswSenha;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

    private void limpar()
    {
        txtUserName.setText( "" );
        pswSenha.setText( "" );

        
        
    }

    private void alterarSenha()
    {
        JOptionPane.showMessageDialog( null, "A senha que está a usar Atualmente não é segura, deve mudar a senha!" );
        dispose();
        new UsuarioAlterarSenhaVissao( usuarioDao.getUsuariowithEncriptedPass( txtUserName.getText(), new String( pswSenha.getPassword() ) ) );
    }

//    public static void fazerBackupAgora() {
//        String data = new SimpleDateFormat(YYYYMMDD_HHMMSS).format(new Date());
////        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
////String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        Process rodarComandoWindows = rodarComandoWindows(rodar_camando, true);
//
////        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
//        System.err.println("Backup realizado com sucesso! ");
//
//    }
    private void verificar_data_sistema()
    {
        Date data_maxima_ultimo_doc = documentoDao.getMAxDataDoc();
        Date data_actual = new Date();

        if ( data_maxima_ultimo_doc != null )
        {
            if ( MetodosUtil.menor_data_1_data_2( data_actual, data_maxima_ultimo_doc ) )
            {
                JOptionPane.showMessageDialog( null, "Caro usário verifique a data do sistema", "AVISO", JOptionPane.WARNING_MESSAGE );
            }

        }

    }

//    public static void fazerBackupAgora()
//    {
//        String data = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
//        String caminhoBackup = System.getProperty( "user.dir" ) + "\\BK\\_database_backup_" + data + ".sql";
//        String rodar_comando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# "
//                + "--dump-date --triggers --add-drop-database --routines "
//                + "--skip-quote-names --skip-set-charset --add-locks --disable-keys "
//                + "--databases kitanda_db > \"" + caminhoBackup + "\"";
//        Process rodarComandoWindows = rodarComandoWindows( caminhoBackup, true );
//        System.err.println( "Backup realizado com sucesso! " );
//    }
//    private void registerLog( BDConexao conexao )
//    {
//        Log log = new Log();
//        String ipMaquina = MetodosUtil.getIpMaquina();
//        String nomeMaquina = MetodosUtil.getNomeMaquina();
//
//        log.setIpMaquina( ipMaquina );
//        log.setNomeMaquina( nomeMaquina );
//        log.setEstado( "ON" );
//
//        LogController logController = new LogController( conexao );
//
//        Log logByIpMaquina = logController.getLogByNomeMaquina( nomeMaquina );
//
//        if ( Objects.nonNull( logByIpMaquina ) )
//        {
//            System.out.println( "MAQUINA JA CADASTRADA" );
//            if ( "ON".equals( logByIpMaquina.getEstado() ) )
//            {
//                int opcao = JOptionPane.showConfirmDialog( null, "Provavelmente o sistema foi encerrado de forma inesperada. Clica em Sim para corrigir." );
//
//                if ( opcao == JOptionPane.YES_NO_OPTION )
//                {
//                    MetodosUtil.actualizarEstadoLog( "OFF" );
//                    LoginVisao.conexao.close();
//                    MetodosUtil.rodarComandoWindows( "taskkill /im javaw.exe -f", false );
//                    System.exit( 0 );
//                }
//                else
//                {
//                    MetodosUtil.rodarComandoWindows( "taskkill /im javaw.exe -f", false );
//                }
//
//            }
//            else
//            {
//                System.out.println( "ENTREI para actualizar o ON" );
//                MetodosUtil.actualizarEstadoLog( "ON" );
//            }
//        }
//        else
//        {
//            if ( !logController.existe( nomeMaquina ) )
//            {
//                logController.salvar( log );
//            }
//
//        }
//
//    }
    private void iniciarMonitoramento()
    {
        resetTimer();

        // Adiciona um listener global para detectar atividades do usuário
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( ( KeyEvent e ) ->
        {
            resetTimer();
            return false;
        } );

        Toolkit.getDefaultToolkit().addAWTEventListener( ( AWTEvent event ) ->
        {
            if ( event.getID() == MouseEvent.MOUSE_MOVED
                    || event.getID() == MouseEvent.MOUSE_CLICKED
                    || event.getID() == MouseEvent.MOUSE_PRESSED )
            {
                resetTimer();
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK );
    }

    private void resetTimer()
    {
        if ( logoutTimer != null )
        {
            logoutTimer.cancel();
        }

        logoutTimer = new Timer();
        logoutTimer.schedule( new TimerTask()
        {
            @Override
            public void run()
            {
                SwingUtilities.invokeLater( () ->
                {
                    //JOptionPane.showMessageDialog( null, "Sessão expirada! Voltando para o login." );
                    conexao.close();
                    logo_out();
                } );
            }
        }, TIMEOUT );
    }

    public static void logo_out()
    {
//        EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
//        UsuarioDao usuarioDao = new UsuarioDao( emf );
//        EmpresaDao empresaDao = new EmpresaDao( emf );
//        TbUsuario usuario = usuarioDao.findTbUsuario( id_user );
//        Empresa empresa = empresaDao.findEmpresa( id_empresa );
        MetodosUtil.fechar_todas_janelas();
        MetodosUtil.actualizarEstadoLog( "OFF" );
        conexao.close();
        new LoginVisao( BDConexao.getInstancia() ).setVisible( true );

//        new LoginVisao(BDConexao.getInstancia().getConnectionAtiva()).setVisible(true);
    }

}
