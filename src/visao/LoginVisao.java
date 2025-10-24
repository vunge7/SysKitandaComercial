/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

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
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
//import javax.swing.UIManager;
import util.BDConexao;
import util.BackupUsb;
import util.JPAEntityMannagerFactoryUtil;
//import util.LicensaUtil;
import util.MetodosUtil;
import static util.MetodosUtil.startBackGroundProcesses;
import util.SingleInstanceLock;
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
//        setTitle( "BEM VINDO AO " + DVML.NAME_SOFTWARE + " " + DVML.VERSION_SOFTWARE );

//        if (Objects.nonNull(conexao)) {
//            conexao.close();
//        }
        this.conexao = conexao;

//        registerLog( conexao );
        dadosInstituicaoController = new DadosInstituicaoController( LoginVisao.conexao );
        d = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        horaTerminoVenda = d.getHoraTerminoVenda();
//        this.idUser = idUser;
//        licensaUtil = new LicensaUtil( conexao );

        mostrar_empresas();

        prazo();

        LoginVisao.setDefaultLookAndFeelDecorated( true );
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

        conexao = new BDConexao();
//        setTitle( "BEM VINDO AO " + DVML.NAME_SOFTWARE + " " + DVML.VERSION_SOFTWARE );

//        if (Objects.nonNull(conexao)) {
//            conexao.close();
//        }
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        d = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        horaTerminoVenda = d.getHoraTerminoVenda();
//        this.idUser = idUser;
//        licensaUtil = new LicensaUtil( conexao );
//        registerLog( conexao );
        mostrar_empresas();
        prazo();

        LoginVisao.setDefaultLookAndFeelDecorated( true );
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
            QuartzApp.executarCronjob( ValidadorJob.class, (TbDadosInstituicao) dadosInstituicaoController.findById( 1 ) );
        }
        else
        {
            BTnSair.setVisible( true );
        }

    }

    public void mostrar_empresas()
    {
        cmbEmpresa.setModel( new DefaultComboBoxModel( (Vector) empresaDao.getAllEmpresa() ) );
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
        jLabel1 = new javax.swing.JLabel();
        JLDataFecho = new javax.swing.JLabel();
        JLNif = new javax.swing.JLabel();
        JEmpresa = new javax.swing.JLabel();
        Soft = new javax.swing.JLabel();
        Soft1 = new javax.swing.JLabel();

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img30.png"))); // NOI18N

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
        Soft.setText("KITANDA 1.2.   Sistema Certificado pela AGT. Nº 258");

        Soft1.setFont(new java.awt.Font("American Typewriter", 1, 48)); // NOI18N
        Soft1.setForeground(new java.awt.Color(255, 255, 255));
        Soft1.setText("KITANDA 1.2");

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
        TbDadosInstituicao dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );

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
                switch ( usuario.getIdTipoUsuario().getIdTipoUsuario() )
                {

                    //1 - Administrador
                    case 1:
//                        dispose();

                        System.err.println( "getCodico_Utilizador (): " + getCodico_Utilizador() );
                        System.err.println( "getCodico_Empresa (): " + getIdEmpresa() );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new RootVisao( getCodico_Utilizador(), getIdEmpresa(), true, conexao ).setVisible( true );
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

                            new JanelaFrontOfficeLavandariaVisao( getCodico_Utilizador(), conexao ).setVisible( true );

                        }
                        else
                        {

                            new RootVisao( getCodico_Utilizador(), getIdEmpresa(), true, conexao ).setVisible( true );
                        }
                        limpar();
                        break;
                    //2 - Caixa
                    case 3:
//                        dispose();
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new VendaUsuarioVisao( getCodico_Utilizador(), conexao ).setVisible( true );
                        limpar();
                        break;
                    //3 - VendaPO
                    case 4:
//                        dispose();
//                        new VendaPOSVisao( new BDConexao(), DVML.ARMAZEM_LOJA, getCodico_Utilizador() ).setVisible( true );
//                        MetodosUtil.abrir_caixa_automatica( getCodico_Utilizador(), new UsuariosController( conexao ), new CaixasController( conexao ) );
                        new FrontOfficeVisao( getCodico_Utilizador(), conexao ).setVisible( true );
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
        if ( SingleInstanceLock.isAlreadyRunning() )
        {
            JOptionPane.showMessageDialog( null, "Atenção\nO sistema já está aberto!" );
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
                if ( startBackGroundProcesses() )
                {
                    BDConexao conexao = new BDConexao();
                    StockAutoCheckService service = new StockAutoCheckService( conexao.conectar() );

                    try
                    {
                        service.verificarOuSalvarStockDiario();
                    }
                    catch ( SQLException e )
                    {
                        e.printStackTrace();
                    }

                    if ( true )
                    { // <- aqui você pode colocar sua regra de licença
                        new LoginVisao( conexao ).setVisible( true );
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
        new LoginVisao( new BDConexao() );

    }

}
