/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;

import comercial.controller.CaixasController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.ItemCaixaController;
import dao.CaixaDao;
import dao.DadosInstituicaoDao;
import dao.ItemPermissaoDao;
import dao.OperacaoSistemaDao;
import dao.UsuarioDao;
import entity.TbDadosInstituicao;
import entity.TbItemPermissao;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import static kitanda.util.CfMethodsSwing.resizeJButtonIcon;
import rh.visao.ConfiguracaoSistemaVisao;
//import rh.visao.MenuPrincipalNovaRHVisao;
import rh.visao.MenuPrincipalRHNovoVisao;
import tesouraria.novo.visao.NovoMenuPrincipalTesourariaVisao;
//import rh.visao.MenuPrincipalRHVisao;
import util.BDConexao;
import util.BackupService;
import util.DVML;
import static util.DVML.CAMINHO_BK;
import util.GerarScriptUpdateVisao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;
import util.OperacaoSistemaUtil;

/**
 * @author Domingos Dala Vunge
 */
public class RootVisao extends javax.swing.JFrame
{

    private Integer idUser;
    private boolean administrador = false;
    public static TbDadosInstituicao dadosInstituicao;
    private static DadosInstituicaoController dadosInstituicaoController;
    private static CaixasController caixa_controller;
    private static ItemCaixaController item_caixa_controller;
    private ItemPermissaoDao itemPermissaoDao = new ItemPermissaoDao( emf );
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private static UsuarioDao usuarioDao = new UsuarioDao( emf );
    private OperacaoSistemaDao operacaoSistemaDao = new OperacaoSistemaDao( emf );
    private static CaixaDao caixaDao = new CaixaDao( emf );
    private static BDConexao conexao;
    private int codigo = 0;
    private int idEmpresa = 0;
    private float percentagem_seja_bem_vindo = 0;
    Thread t;
//    private OperacaoSistemaUtil osu = new OperacaoSistemaUtil();

    public RootVisao( int idUser, int idEmpresa, boolean administrador, BDConexao conexao )
    {

        this.administrador = administrador;
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;

        initComponents();
////        btn_reabertura_dia.setVisible( false);
//        t = new Thread(this);
//        t.start();
        setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        setLocationRelativeTo( null );
        this.setExtendedState( this.getExtendedState() | RootVisao.MAXIMIZED_BOTH );
//        procedimento_activar_botao_reabertura();
        this.conexao = conexao;
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );
        caixa_controller = new CaixasController( conexao );
        item_caixa_controller = new ItemCaixaController( conexao );
//        alterar_status_botao();
        // ActualizarHora.start(lb_horario);       
        //lbNome.setText(  usuarioDao.findTbUsuario(idUser).getNome()  +"  " +usuarioDao.findTbUsuario(idUser).getSobreNome()      );

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension tamTela = kit.getScreenSize();

        fundo_principal.setSize( tamTela.width, tamTela.height );
        percentagem_seja_bem_vindo = ( 62 * tamTela.width ) / 100;

        postInitComponents();
//        mostrarBotaoEsvaziar();
//        mostrarBotaoActualizaHash();
        activar_opcoes();

//        try
//        {
//            getMenuPrincipalByNegocio( dadosInstituicao.getRegime() );
//        }
//        catch ( Exception e )
//        {
//        }
        setTesouraria( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getTesouraria() );
        setRh( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getRh() );
        setComercial( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getComercial() );
        busca_permissao();
        
        
        // Atalho F4 para chamar getMenuPrincipalByNegocio(...)
getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke("F4"), "abrirMenuPrincipal");

getRootPane().getActionMap().put("abrirMenuPrincipal", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            getMenuPrincipalByNegocio(dadosInstituicao.getNegocio());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
});


        setWindowsListener();
//        btn_abertura_dia_root.setVisible( false);
//        btn_feicho_dia_root.setVisible( false);

    }

    public void busca_permissao()
    {

        try
        {
            // TODO add your handling code here
            setStatusUsuario( (Vector) itemPermissaoDao.getAllPermissoesByIdUsuarioAndModulo( this.idUser, DVML.MODULO_GESTAO_COMERCIAL ) );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

    }

    public void setStatusUsuario( Vector<TbItemPermissao> vector )
    {

        String permissao = "";
        //limpa
        setPermissoes( false );

        for ( int i = 0; i < vector.size(); i++ )
        {

            permissao = vector.get( i ).getIdPermissao().getDescricao();

            System.out.println( "PERMISSAO PRINCIPAL " + permissao );

            if ( permissao.equals( btn_abertura_dia_root.getText() ) )
            {
                btn_abertura_dia_root.setEnabled( true );
            }
            else if ( permissao.equals( btn_feicho_dia_root.getText() ) )
            {
                btn_feicho_dia_root.setEnabled( true );
            }

        }

    }

    public void setPermissoes( boolean status )
    {
        btn_abertura_dia_root.setEnabled( status );
        btn_feicho_dia_root.setEnabled( status );
    }

    private void setTesouraria( String ano_economico )
    {

        if ( ano_economico.equalsIgnoreCase( "Mostrar" ) )
        {

            btnTesouraria.setVisible( true );
            btTesouraria.setVisible( true );
        }
        else
        {
            btnTesouraria.setVisible( false );
            btTesouraria.setVisible( false );

        }

    }

    private void setRh( String ano_economico )
    {

        if ( ano_economico.equalsIgnoreCase( "Mostrar" ) )
        {

            btnRH.setVisible( true );
            btRh.setVisible( true );
        }
        else
        {
            btnRH.setVisible( false );
            btRh.setVisible( false );

        }

    }

    private void setComercial( String ano_economico )
    {

        if ( ano_economico.equalsIgnoreCase( "Mostrar" ) )
        {

            btnComercial.setVisible( true );
            btComercial.setVisible( true );
        }
        else
        {
            btnComercial.setVisible( false );
            btComercial.setVisible( false );

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

        jMenuItem6 = new javax.swing.JMenuItem();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenu5 = new javax.swing.JMenu();
        fundo_principal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnComercial = new javax.swing.JButton();
        btnRH = new javax.swing.JButton();
        btnTesouraria = new javax.swing.JButton();
        btRh = new javax.swing.JButton();
        btComercial = new javax.swing.JButton();
        btTesouraria = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        bdBackupJButton = new javax.swing.JButton();
        btn_abertura_dia_root = new javax.swing.JButton();
        btn_feicho_dia_root = new javax.swing.JButton();
        btnEsvaziaBD = new javax.swing.JButton();
        btn_actualiza_hash = new javax.swing.JButton();
        btConfiguracaoSistema = new javax.swing.JButton();
        btnGerarScriptActualizacao = new javax.swing.JButton();
        bt_desencriptar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jMenuItem6.setText("jMenuItem6");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jMenu5.setText("jMenu5");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(null);

        fundo_principal.setBackground(new java.awt.Color(35, 62, 92));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Seja bem vindo ao Erp Kitanda 1.1");

        jPanel1.setBackground(new java.awt.Color(112, 171, 199));
        jPanel1.setForeground(new java.awt.Color(0, 102, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnComercial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pagamentos diversos.png"))); // NOI18N
        btnComercial.setText("GESTÃO COMERCIAL");
        btnComercial.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnComercialActionPerformed(evt);
            }
        });
        jPanel1.add(btnComercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, 210));

        btnRH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/rheste.png"))); // NOI18N
        btnRH.setText("RH");
        btnRH.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRHActionPerformed(evt);
            }
        });
        jPanel1.add(btnRH, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 220, 210));

        btnTesouraria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/tesoueste.jpg"))); // NOI18N
        btnTesouraria.setText("TESOURARIA");
        btnTesouraria.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTesourariaActionPerformed(evt);
            }
        });
        jPanel1.add(btnTesouraria, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 220, 210));

        btRh.setText("Gestão Recursos Humanos");
        btRh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btRhActionPerformed(evt);
            }
        });
        jPanel1.add(btRh, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 220, 30));

        btComercial.setText("Gestão Comercial");
        btComercial.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btComercialActionPerformed(evt);
            }
        });
        jPanel1.add(btComercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 220, 30));

        btTesouraria.setText("Gestão Tesouraria");
        btTesouraria.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btTesourariaActionPerformed(evt);
            }
        });
        jPanel1.add(btTesouraria, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 220, 30));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton4.setText("Saír");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        bdBackupJButton.setText("Backup");
        bdBackupJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bdBackupJButtonActionPerformed(evt);
            }
        });

        btn_abertura_dia_root.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/caixa_32x_32.png"))); // NOI18N
        btn_abertura_dia_root.setText("Abertura de Caixa");
        btn_abertura_dia_root.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_abertura_dia_rootActionPerformed(evt);
            }
        });

        btn_feicho_dia_root.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btn_feicho_dia_root.setText("Fecho de Caixa");
        btn_feicho_dia_root.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_feicho_dia_rootActionPerformed(evt);
            }
        });

        btnEsvaziaBD.setText("Esvazia BD");
        btnEsvaziaBD.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEsvaziaBDActionPerformed(evt);
            }
        });

        btn_actualiza_hash.setText("Act_Hash");
        btn_actualiza_hash.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_actualiza_hashActionPerformed(evt);
            }
        });

        btConfiguracaoSistema.setText("Configuração Módulos");
        btConfiguracaoSistema.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btConfiguracaoSistemaActionPerformed(evt);
            }
        });

        btnGerarScriptActualizacao.setText("Gerar Sript de Actualizacao");
        btnGerarScriptActualizacao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnGerarScriptActualizacaoActionPerformed(evt);
            }
        });

        bt_desencriptar.setText("Desencriptar");
        bt_desencriptar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                bt_desencriptarActionPerformed(evt);
            }
        });

        jButton1.setText("BK");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fundo_principalLayout = new javax.swing.GroupLayout(fundo_principal);
        fundo_principal.setLayout(fundo_principalLayout);
        fundo_principalLayout.setHorizontalGroup(
            fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fundo_principalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(fundo_principalLayout.createSequentialGroup()
                        .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(fundo_principalLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(btn_actualiza_hash, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEsvaziaBD, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnGerarScriptActualizacao, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bdBackupJButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_desencriptar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btn_abertura_dia_root, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                                .addComponent(btn_feicho_dia_root, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btConfiguracaoSistema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        fundo_principalLayout.setVerticalGroup(
            fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fundo_principalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(fundo_principalLayout.createSequentialGroup()
                        .addComponent(btConfiguracaoSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_abertura_dia_root, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btn_feicho_dia_root, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fundo_principalLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_actualiza_hash)
                            .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnEsvaziaBD, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bdBackupJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnGerarScriptActualizacao, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(fundo_principalLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(fundo_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                            .addComponent(bt_desencriptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fundo_principalLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(232, 288, Short.MAX_VALUE))
        );

        getContentPane().add(fundo_principal);
        fundo_principal.setBounds(0, 0, 1100, 730);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnComercialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComercialActionPerformed

        try
        {
            getMenuPrincipalByNegocio( dadosInstituicao.getNegocio() );
        }
        catch ( Exception e )
        {
        }

    }//GEN-LAST:event_btnComercialActionPerformed


    private void btnRHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRHActionPerformed

        dispose();
        new MenuPrincipalRHNovoVisao( this.idUser, this.idEmpresa, true, conexao ).show();

//                dispose();
//        new MenuPrincipalNovaRHVisao (idUser, administrador, new BDConexao ()).setVisible(true);
//        dispose();
//        new MenuPrincipalTesourariaVisao(this.idUser).show();
    }//GEN-LAST:event_btnRHActionPerformed

    private void btnTesourariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTesourariaActionPerformed
        // TODO add your handling code here:
        dispose();
//        new MenuPrincipalTesourariaVisao( this.idUser, this.idEmpresa, conexao ).show();
        new NovoMenuPrincipalTesourariaVisao( idUser, true, conexao ).setVisible( true );
    }//GEN-LAST:event_btnTesourariaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        OperacaoSistemaUtil.operacao_fechar_run();
        dispose();
        new LoginVisao( idUser ).setVisible( true );
    }//GEN-LAST:event_jButton4ActionPerformed

    private void bdBackupJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bdBackupJButtonActionPerformed
    {//GEN-HEADEREND:event_bdBackupJButtonActionPerformed
        new BDBackupJFrame();
    }//GEN-LAST:event_bdBackupJButtonActionPerformed

    private void btn_abertura_dia_rootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_abertura_dia_rootActionPerformed
        // TODO add your handling code here:
//        OperacaoSistemaUtil.procedimento_abrir_dia( this.idUser );
//        procedimento_abrir_contagem( this.idUser );
        new CaixaAberturaVisao( idUser, conexao, false ).setVisible( true );


    }//GEN-LAST:event_btn_abertura_dia_rootActionPerformed

    private void btn_feicho_dia_rootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_feicho_dia_rootActionPerformed
        // TODO add your handling code here:
//        procedimento_fechar_contagem( this.idUser );
//        new CaixaVisao( this.idUser, conexao ).setVisible( true );
//        btn_reabertura_dia.setEnabled( true);

        if ( dadosInstituicao.getTipoFechoCaixa().equals( "Normal" ) )
        {
            new CaixaFechoVisao( idUser, conexao, false ).setVisible( true );
        }
        else
        {
            new CaixaFechoGoldVisao( idUser, conexao, false ).setVisible( true );
        }

//        fazerBackupAgora();
    }//GEN-LAST:event_btn_feicho_dia_rootActionPerformed

    private void btnEsvaziaBDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEsvaziaBDActionPerformed
    {//GEN-HEADEREND:event_btnEsvaziaBDActionPerformed
        // TODO add your handling code here:
        fazerBackupAgora();
        if ( JOptionPane.showConfirmDialog( null, "Caro administrador, este processo é puramente irreversível, deseja continuar?" ) == JOptionPane.YES_OPTION )
        {
            if ( MetodosUtil.esvaziaBaseDados( conexao ) )
            {
                JOptionPane.showMessageDialog( null, "Banco de dados esvaziado com sucesso!..." );
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Falha ao esvaziar o banco de dados", "Falha", JOptionPane.ERROR_MESSAGE );
            }
        }


    }//GEN-LAST:event_btnEsvaziaBDActionPerformed

    private void btn_actualiza_hashActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_actualiza_hashActionPerformed
    {//GEN-HEADEREND:event_btn_actualiza_hashActionPerformed
//        new FicheiroSAFThashVisao( conexao ).setVisible( true );
    }//GEN-LAST:event_btn_actualiza_hashActionPerformed

    private void btConfiguracaoSistemaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btConfiguracaoSistemaActionPerformed
    {//GEN-HEADEREND:event_btConfiguracaoSistemaActionPerformed
        new ConfiguracaoSistemaVisao( this, rootPaneCheckingEnabled ).setVisible( true );
    }//GEN-LAST:event_btConfiguracaoSistemaActionPerformed

    private void btComercialActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btComercialActionPerformed
    {//GEN-HEADEREND:event_btComercialActionPerformed
        try
        {
            getMenuPrincipalByNegocio( dadosInstituicao.getNegocio() );
        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_btComercialActionPerformed

    private void btTesourariaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btTesourariaActionPerformed
    {//GEN-HEADEREND:event_btTesourariaActionPerformed
        dispose();
//        new MenuPrincipalTesourariaVisao( this.idUser, this.idEmpresa, conexao ).show();
        new NovoMenuPrincipalTesourariaVisao( idUser, true, conexao ).setVisible( true );
    }//GEN-LAST:event_btTesourariaActionPerformed

    private void btRhActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btRhActionPerformed
    {//GEN-HEADEREND:event_btRhActionPerformed
        dispose();
        new MenuPrincipalRHNovoVisao( this.idUser, this.idEmpresa, true, conexao ).show();
    }//GEN-LAST:event_btRhActionPerformed

    private void btnGerarScriptActualizacaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnGerarScriptActualizacaoActionPerformed
    {//GEN-HEADEREND:event_btnGerarScriptActualizacaoActionPerformed
        // TODO add your handling code here:
        new GerarScriptUpdateVisao( conexao ).setVisible( true );
    }//GEN-LAST:event_btnGerarScriptActualizacaoActionPerformed

    private void bt_desencriptarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_desencriptarActionPerformed
    {//GEN-HEADEREND:event_bt_desencriptarActionPerformed
        BackupService backupService = new BackupService();
        backupService.restaurarBackup(); // aqui pedirá a senha
    }//GEN-LAST:event_bt_desencriptarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        new BackupRestoreJDBC().setVisible( true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger( RootVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( RootVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( RootVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( RootVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new RootVisao( 1, 1, true, null ).setVisible( true );
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bdBackupJButton;
    private javax.swing.JButton btComercial;
    private javax.swing.JButton btConfiguracaoSistema;
    private javax.swing.JButton btRh;
    private javax.swing.JButton btTesouraria;
    private javax.swing.JButton bt_desencriptar;
    private javax.swing.JButton btnComercial;
    private javax.swing.JButton btnEsvaziaBD;
    private javax.swing.JButton btnGerarScriptActualizacao;
    private javax.swing.JButton btnRH;
    private javax.swing.JButton btnTesouraria;
    public static javax.swing.JButton btn_abertura_dia_root;
    private javax.swing.JButton btn_actualiza_hash;
    public static javax.swing.JButton btn_feicho_dia_root;
    private javax.swing.JPanel fundo_principal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    // End of variables declaration//GEN-END:variables

    public static void procedimento_abrir_contagem( int pk_user )
    {

        System.err.println( " CONT: " + MetodosUtil.getContVenda( conexao ) );
        if ( MetodosUtil.getContVenda( conexao ) == 0 )
        {

            MetodosUtil.actualizar_cont_venda( 0, conexao );

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Falha: Ja existe uma abertura para a contagem para este dia", "FALHA", JOptionPane.ERROR_MESSAGE );
        }

        alterar_status_botao();
    }

    public static void procedimento_abrir_contagem( int pk_user, JButton button )
    {

        System.err.println( " CONT: " + MetodosUtil.getContVenda( conexao ) );
        if ( MetodosUtil.getContVenda( conexao ) == 0 )
        {

            int opcao = JOptionPane.showConfirmDialog( null, "Caro Usuario: " + usuarioDao.findTbUsuario( pk_user ).getNome() + " "
                    + usuarioDao.findTbUsuario( pk_user ).getSobreNome() + "\n" + "Tens a plena certeza que pretendes abrir a contagem das vendas ?" );
            if ( opcao == JOptionPane.YES_OPTION )
            {
                MetodosUtil.actualizar_cont_venda( 0, conexao );
                button.setEnabled( false );
                JOptionPane.showMessageDialog( null, "Abertura efectuada com sucesso.", DVML.DVML_COMECIAL_LDA, JOptionPane.WARNING_MESSAGE );
            }
            else if ( opcao == JOptionPane.NO_OPTION )
            {
                JOptionPane.showMessageDialog( null, "Abertura cancelada", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog( null, "Falha: Ja existe uma abertura para a contagem para este dia", "FALHA", JOptionPane.ERROR_MESSAGE );
        }

    }

    public static void procedimento_fechar_contagem( int pk_user )
    {

        System.err.println( " CONT: " + MetodosUtil.getContVenda( conexao ) );
        //if( MetodosUtil.getContVenda(conexao) == 0 ){

//                    int opcao = JOptionPane.showConfirmDialog(null, "Caro Usuario: "  +usuarioDao.findTbUsuario(pk_user).getNome() + " " 
//                            +usuarioDao.findTbUsuario(pk_user).getSobreNome() +"\n" +"Tens a plena certeza que pretendes fechar a contagem das vendas ?");
//                    if(  opcao == JOptionPane.YES_OPTION ){
        MetodosUtil.actualizar_cont_venda( -1, conexao );
//                    }else if(opcao == JOptionPane.NO_OPTION){
//                            JOptionPane.showMessageDialog(null, "Fecho cancelado", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE);
//                    }

        // }else JOptionPane.showMessageDialog(null, "Falha: Ja existe o feicho", "FALHA", JOptionPane.ERROR_MESSAGE);
        alterar_status_botao();
    }

    public static void procedimento_fechar_contagem( int pk_user, JButton button )
    {

        System.err.println( " CONT: " + MetodosUtil.getContVenda( conexao ) );
        //if( MetodosUtil.getContVenda(conexao) == 0 ){

        int opcao = JOptionPane.showConfirmDialog( null, "Caro Usuario: " + usuarioDao.findTbUsuario( pk_user ).getNome() + " "
                + usuarioDao.findTbUsuario( pk_user ).getSobreNome() + "\n" + "Tens a plena certeza que pretendes fechar a contagem das vendas ?" );
        if ( opcao == JOptionPane.YES_OPTION )
        {
            MetodosUtil.actualizar_cont_venda( -1, conexao );
            button.setEnabled( false );
        }
        else if ( opcao == JOptionPane.NO_OPTION )
        {
            JOptionPane.showMessageDialog( null, "Fecho cancelado", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE );
        }

        // }else JOptionPane.showMessageDialog(null, "Falha: Ja existe o feicho", "FALHA", JOptionPane.ERROR_MESSAGE);
    }

    public static void alterar_status_botao()
    {

        if ( !caixa_controller.existeCaixas() )
        {
            btn_abertura_dia_root.setEnabled( true );
            btn_feicho_dia_root.setEnabled( false );
        }

        else if ( caixa_controller.existe_abertura() && caixa_controller.existe_fecho() )
        {
            btn_abertura_dia_root.setEnabled( true );
            btn_feicho_dia_root.setEnabled( false );
        }
        else
        {
            btn_abertura_dia_root.setEnabled( false );
            btn_feicho_dia_root.setEnabled( true );
        }
    }

    private void postInitComponents()
    {
        int tamanho = 45;
        resizeJButtonIcon( tamanho, tamanho, getClass().getResource( "/imagens/img32x32/bd_backup.png" ), bdBackupJButton );
    }

    private void mostrarBotaoEsvaziar()
    {
        btnEsvaziaBD.setVisible( ( DVML.ID_USUARIO_FORNECEDOR_SOFTWARE == idUser ) );
    }
    
    private void mostrarBotaoBACKUPr()
    {
        bdBackupJButton.setVisible( ( DVML.ID_USUARIO_FORNECEDOR_SOFTWARE == idUser ) );
    }

    private void mostrarBotaoActualizaHash()
    {
        btnEsvaziaBD.setVisible( ( DVML.ID_USUARIO_FORNECEDOR_SOFTWARE == idUser ) );
    }

    private void activar_opcoes()
    {

        if ( idUser == 15 )
        {
            btConfiguracaoSistema.setVisible( true );
            btnEsvaziaBD.setVisible( true );
            btn_actualiza_hash.setVisible( true );
            btnGerarScriptActualizacao.setVisible( true );
            bt_desencriptar.setVisible( true );
     
        }
        else
        {
            btConfiguracaoSistema.setVisible( false );
            btnEsvaziaBD.setVisible( false );
            btn_actualiza_hash.setVisible( false );
            btnGerarScriptActualizacao.setVisible( false );
            bt_desencriptar.setVisible( false );
        }

    }

    public void getMenuPrincipalByNegocio( String negocio )
    {
        if ( negocio.equalsIgnoreCase( "Oficina" ) )
        {
            new MenuPrincipalOficinaVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();
        }
        else if ( negocio.equalsIgnoreCase( "Transportes" ) )
        {
            new MenuPrincipalTransporteVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();
        }
        else if ( negocio.equalsIgnoreCase( "Comercial" ) )
        {
            new MenuPrincipalVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();

        }
        else if ( negocio.equalsIgnoreCase( "Farmacia" ) )
        {
            new MenuPrincipalFarmaciaVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();

        }
        else if ( negocio.equalsIgnoreCase( "Lavandaria" ) )
        {
            new MenuPrincipalLavandariaVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();

        }
        else if ( negocio.equalsIgnoreCase( "Layout" ) )
        {
            new MenuPrincipalLogoVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();

        }
        else if ( negocio.equalsIgnoreCase( "Restaurante" ) )
        {
            new MenuPrincipalRestauranteVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();
        }
        else
        {
            new MenuPrincipalVisao( this.idUser, this.idEmpresa, administrador, conexao ).show();
        }
    }

    public static void fazerBackupAgora()
    {
        File ficheiro = new File( CAMINHO_BK + "/" );
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \".."+ficheiro+"_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

    public void getFechoCaixaByNegocio( String negocio )
    {
        switch (negocio)
        {
            case "Transportes":
            case "Comercial":
            case "Farmacia":
            case "Layout":
            case "Restaurante":
                new CaixaFechoVisao( idUser, conexao, false ).setVisible( true );
                break;
            default:
                new CaixaFechoGoldVisao( idUser, conexao, false ).setVisible( true );
        }

    }

    private void setWindowsListener()
    {
        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                MetodosUtil.verificarCaixa(
                        caixa_controller,
                        idUser,
                        RootVisao.btn_abertura_dia_root,
                        RootVisao.btn_feicho_dia_root );
            }

        } );

    }

}
