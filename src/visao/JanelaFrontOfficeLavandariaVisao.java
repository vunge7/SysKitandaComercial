/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package visao;


import comercial.controller.CaixasController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.ItemCaixaController;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import comercial.controller.UsuariosController;
import dao.UsuarioDao;
import entity.TbDadosInstituicao;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbUsuario;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;

/**
 *
 * @author Domingos Dala Vunge
 */
public class JanelaFrontOfficeLavandariaVisao extends javax.swing.JFrame
{

    private MesasController mesasController;
    private LugaresController lugaresController;
    public static TbDadosInstituicao dadosInstituicao;
    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
//    private static CaixaDao caixaDao = new CaixaDao(emf );
    private static CaixasController caixa_controller;
    private static ItemCaixaController item_caixa_controller;
    private UsuariosController usuariosController;
    private static DadosInstituicaoController dadosInstituicaoController;
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private int idUser;
    private BDConexao conexao;

    public JanelaFrontOfficeLavandariaVisao( int idUser, BDConexao conexao )
    {
        initComponents();
        setLocationRelativeTo( null );
        this.idUser = idUser;
        this.conexao = conexao;
        mesasController = new MesasController( conexao );
        lugaresController = new LugaresController( conexao );
        caixa_controller = new CaixasController( conexao );
        item_caixa_controller = new ItemCaixaController( conexao );
        usuariosController = new UsuariosController( conexao );
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        dadosInstituicao = (TbDadosInstituicao) dadosInstituicaoController.findById( 1 );

        setWindowsListener();
        // envios automáticos (fechos, e-mail e WhatsApp agendados) também a partir da lavandaria
        comercial.caixa.MenuFechos.iniciarAgendadores();
        // aspecto Kitanda (barra lateral + página com cartões); se falhar, abre o desenho antigo
        util.ui.LayoutSeguro.aplicar( this, "Lavandaria", this::montarLayoutWeb );
    }

    // =====================================================================
    // LAYOUT WEB: os botões do initComponents (com os eventos e o estado que o
    // verificarCaixa lhes dá) passam a cartões; junta-se o grupo dos fechos e
    // do envio de relatórios por e-mail e WhatsApp.
    // =====================================================================
    private void montarLayoutWeb()
    {
        String nome = "", perfil = null;
        try
        {
            entity.TbUsuario u = usuarioDao.findTbUsuario( idUser );
            nome = u.getNome();
            perfil = u.getIdTipoUsuario() == null ? null : u.getIdTipoUsuario().getDescricao();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        util.ui.PaginaMenuWeb pagina = new util.ui.PaginaMenuWeb( "Lavandaria",
                "Recolha, entrega e caixa. Os fechos seguem por e-mail e WhatsApp.", new javax.swing.JLabel( nome ), perfil );
        pagina.simboloMarca( util.ui.MenuWeb.Simbolo.LAVANDARIA );

        // ---- cartões das operações (os próprios botões do formulário)
        int L = 230, A = 150;
        util.ui.MenuWeb.cartaoModulo( btnRecolha, util.ui.MenuWeb.Simbolo.LAVANDARIA, "Recolha",
                "Receber as peças do cliente e cobrar", L, A );
        util.ui.MenuWeb.cartaoModulo( jButton2, util.ui.MenuWeb.Simbolo.CAMISA, "Entrega",
                "Entregar as peças prontas", L, A );
        util.ui.MenuWeb.cartaoModulo( jButton3, util.ui.MenuWeb.Simbolo.FERRO, "Serviços",
                "Lavar, engomar, limpar a seco: preços", L, A );
        util.ui.MenuWeb.cartaoModulo( btnRelatorio, util.ui.MenuWeb.Simbolo.GRAFICO, "Relatório",
                "Recolhas e entregas por período", L, A );
        util.ui.MenuWeb.cartaoModulo( btn_feicho_dia_lav1, util.ui.MenuWeb.Simbolo.PRANCHETA, "Factura Proforma",
                "Orçamento para o cliente", L, A );
        util.ui.MenuWeb.cartaoModulo( btn_abertura_dia_lav, util.ui.MenuWeb.Simbolo.CAIXA_ABRIR, "Abertura de Caixa",
                "Valor inicial do dia", L, A );
        util.ui.MenuWeb.cartaoModulo( btn_feicho_dia_lav, util.ui.MenuWeb.Simbolo.CAIXA_FECHAR, "Fecho de Caixa",
                "Contar, imprimir o talão e enviar", L, A );
        util.ui.MenuWeb.cartaoModulo( btn_feicho_dia_lav2, util.ui.MenuWeb.Simbolo.CHAVE, "Alterar Senha",
                "A sua palavra-passe", L, A );

        // ---- fechos e envios (cartões novos)
        javax.swing.JButton fechos = accao( "Gestão de Fechos", () -> new comercial.caixa.FechosCaixaVisao().setVisible( true ) );
        javax.swing.JButton email = accao( "Envio por E-mail", () -> comercial.email.EnvioRelatoriosEmailVisao.abrir( this,
                BDConexao.getInstancia(), idUser ) );
        javax.swing.JButton whatsapp = accao( "Envio por WhatsApp", () -> comercial.whatsapp.EnvioRelatoriosWhatsAppVisao.abrir( this,
                BDConexao.getInstancia(), idUser ) );
        util.ui.MenuWeb.cartaoModulo( fechos, util.ui.MenuWeb.Simbolo.TALAO_FECHO, "Fechos de Caixa",
                "Consultar, reimprimir e reenviar fechos", L, A );
        util.ui.MenuWeb.cartaoModulo( email, util.ui.MenuWeb.Simbolo.SINO, "Envio por E-mail",
                "Destinatários, horários e envio agora", L, A );
        util.ui.MenuWeb.cartaoModulo( whatsapp, util.ui.MenuWeb.Simbolo.SINO, "Envio por WhatsApp",
                "Números, horários e envio agora", L, A );

        // ---- barra lateral: atalhos (cada um carrega no cartão; um botão só pode estar num sítio)
        pagina.grupo( "Operações" );
        pagina.item( atalho( btnRecolha, "Recolha" ), "Recolha" );
        pagina.item( atalho( jButton2, "Entrega" ), "Entrega" );
        pagina.item( atalho( jButton3, "Serviços" ), "Serviços" );
        pagina.item( atalho( btnRelatorio, "Relatório" ), "Relatório" );
        pagina.item( atalho( btn_feicho_dia_lav1, "Factura Proforma" ), "Factura Proforma" );
        pagina.grupo( "Caixa" );
        pagina.item( atalho( btn_abertura_dia_lav, "Abertura de Caixa" ), "Abertura de Caixa" );
        pagina.item( atalho( btn_feicho_dia_lav, "Fecho de Caixa" ), "Fecho de Caixa" );
        comercial.caixa.MenuFechos.adicionarAPagina( pagina, this, idUser );

        java.awt.Component cartoesOperacao = util.ui.FormularioWeb.grelha( L, 4, btnRecolha, jButton2, jButton3, btnRelatorio, btn_feicho_dia_lav1 );
        java.awt.Component cartoesCaixa = util.ui.FormularioWeb.grelha( L, 4, btn_abertura_dia_lav, btn_feicho_dia_lav, fechos, email, whatsapp,
                btn_feicho_dia_lav2 );
        // ---- painel do dia (números e peças por entregar; duplo clique abre a entrega)
        painel = new comercial.lavandaria.PainelLavandaria( codFact ->
        {
            EntregaLavandariaVisao e = new EntregaLavandariaVisao( idUser, BDConexao.getInstancia() );
            e.setVisible( true );
            e.procurarRecolha( codFact );
        } );
        painel.actualizar( true );
        // ao voltar de uma recolha / entrega, os números acompanham
        addWindowListener( new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowActivated( java.awt.event.WindowEvent e )
            {
                painel.actualizar( false );
            }
        } );

        javax.swing.JPanel conteudo = util.ui.FormularioWeb.pilha( 12,
                util.ui.MenuWeb.rotuloGrupo( "Hoje na lavandaria", util.ui.MenuWeb.DOURADO.darker() ),
                painel,
                javax.swing.Box.createVerticalStrut( 4 ),
                util.ui.MenuWeb.rotuloGrupo( "Operações", util.ui.MenuWeb.DOURADO.darker() ),
                cartoesOperacao,
                javax.swing.Box.createVerticalStrut( 4 ),
                util.ui.MenuWeb.rotuloGrupo( "Caixa, fechos e envios", util.ui.MenuWeb.DOURADO.darker() ),
                cartoesCaixa,
                javax.swing.Box.createVerticalStrut( 4 ),
                util.ui.MenuWeb.rotuloGrupo( "O circuito da roupa", util.ui.MenuWeb.DOURADO.darker() ),
                util.ui.FormularioWeb.cartao( "Do balcão à entrega", util.ui.ComponentesWeb.CIANO, util.ui.FluxoWeb.lavandaria( 0 ) ) );
        pagina.conteudo( conteudo );
        pagina.sair( jButton4, "Fechar a lavandaria" );
        pagina.montar( this, "Kitanda  ·  Lavandaria", 4 );
    }

    private comercial.lavandaria.PainelLavandaria painel;

    private static javax.swing.JButton atalho( javax.swing.JButton cartao, String texto )
    {
        javax.swing.JButton b = new javax.swing.JButton( texto );
        b.addActionListener( e -> cartao.doClick() );
        // segue o estado do cartão (o verificarCaixa liga e desliga a abertura / o fecho)
        cartao.addPropertyChangeListener( "enabled", e -> b.setEnabled( cartao.isEnabled() ) );
        b.setEnabled( cartao.isEnabled() );
        return b;
    }

    private javax.swing.JButton accao( String texto, Runnable r )
    {
        javax.swing.JButton b = new javax.swing.JButton( texto );
        b.addActionListener( e ->
        {
            try
            {
                r.run();
            }
            catch ( RuntimeException ex )
            {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog( this, "Não foi possível abrir " + texto + ":\n" + ex.getMessage() );
            }
        } );
        return b;
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

        jPanel1 = new javax.swing.JPanel();
        btnRecolha = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnRelatorio = new javax.swing.JButton();
        btn_feicho_dia_lav = new javax.swing.JButton();
        btn_abertura_dia_lav = new javax.swing.JButton();
        btn_feicho_dia_lav1 = new javax.swing.JButton();
        btn_feicho_dia_lav2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        btnRecolha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/lavandaria/recolha_01_01.png"))); // NOI18N
        btnRecolha.setText("Recolha");
        btnRecolha.setToolTipText("Aceda a Recolha");
        btnRecolha.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRecolhaActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/lavandaria/roupa_01_01.png"))); // NOI18N
        jButton2.setText("Entrega");
        jButton2.setToolTipText("Aceda a Entrega");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/servico_1.jpg"))); // NOI18N
        jButton3.setText("Serviços");
        jButton3.setToolTipText("Cadastre um novo Serviço");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 128x128.png"))); // NOI18N
        jButton4.setText("SAIR");
        jButton4.setToolTipText("Sair do Menu");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        btnRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/calendario 32x32.png"))); // NOI18N
        btnRelatorio.setText("Relatório");
        btnRelatorio.setToolTipText("Cadastre um novo Serviço");
        btnRelatorio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRelatorioActionPerformed(evt);
            }
        });

        btn_feicho_dia_lav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        btn_feicho_dia_lav.setText("Fecho Caixa");
        btn_feicho_dia_lav.setToolTipText("Sair do Menu");
        btn_feicho_dia_lav.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_feicho_dia_lavActionPerformed(evt);
            }
        });

        btn_abertura_dia_lav.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/caixa_32x_32.png"))); // NOI18N
        btn_abertura_dia_lav.setText("Abertura Caixa");
        btn_abertura_dia_lav.setToolTipText("Sair do Menu");
        btn_abertura_dia_lav.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_abertura_dia_lavActionPerformed(evt);
            }
        });

        btn_feicho_dia_lav1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_32x32.png"))); // NOI18N
        btn_feicho_dia_lav1.setText("Factura Proforma");
        btn_feicho_dia_lav1.setToolTipText("Sair do Menu");
        btn_feicho_dia_lav1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_feicho_dia_lav1ActionPerformed(evt);
            }
        });

        btn_feicho_dia_lav2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32_1.png"))); // NOI18N
        btn_feicho_dia_lav2.setText("Reset de Senha");
        btn_feicho_dia_lav2.setToolTipText("Sair do Menu");
        btn_feicho_dia_lav2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_feicho_dia_lav2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_feicho_dia_lav, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRecolha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addComponent(btnRelatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_abertura_dia_lav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_feicho_dia_lav1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_feicho_dia_lav2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRecolha, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_abertura_dia_lav)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_feicho_dia_lav, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_feicho_dia_lav1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_feicho_dia_lav2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1126, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRecolhaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRecolhaActionPerformed
    {//GEN-HEADEREND:event_btnRecolhaActionPerformed
        // ecrã de recolha restaurado (tinha sido apagado na troca pela venda moderna, que não tem a data de entrega)
        new RecolhaPedidosVisao( getMesa(), getLugar(), idUser, DVML.ARMAZEM_LOJA, BDConexao.getInstancia() ).setVisible( true );
    }//GEN-LAST:event_btnRecolhaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new EntregaLavandariaVisao( idUser, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        try
        {
            // TODO add your handling code here:

            // new ProdutosVisao( this, rootPaneCheckingEnabled, idUser, this.conexao ).setVisible( true );
            new visao.produto.ProdutosModernoVisao( this, rootPaneCheckingEnabled, idUser, this.conexao ).setVisible( true );
        }
        catch ( Exception ex )
        {
            Logger.getLogger( MenuPrincipalVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnRelatorioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRelatorioActionPerformed
    {//GEN-HEADEREND:event_btnRelatorioActionPerformed
        // TODO add your handling code here:
        new RelatorioLavandariaVisao( idUser, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_btnRelatorioActionPerformed

    private void btn_feicho_dia_lavActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_feicho_dia_lavActionPerformed
    {//GEN-HEADEREND:event_btn_feicho_dia_lavActionPerformed

        // fecho de caixa do SysKitandaHotel: valor contado por forma de pagamento, talão com QR,
        // PDF enviado por e-mail e WhatsApp em segundo plano (os fechos antigos tiravam o troco duas vezes)
        comercial.caixa.FechoCaixaWeb.mostrar(this, idUser, "Lavandaria", false);
    }//GEN-LAST:event_btn_feicho_dia_lavActionPerformed

    private void btn_abertura_dia_lavActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_abertura_dia_lavActionPerformed
    {//GEN-HEADEREND:event_btn_abertura_dia_lavActionPerformed
        // abertura do SysKitandaHotel (web): mostra o último fecho e não abre duas caixas com dois cliques
        if ( comercial.caixa.AberturaCaixaWeb.abrir( this, idUser, false ) && painel != null )
        {
            painel.actualizar( true );
        }
    }//GEN-LAST:event_btn_abertura_dia_lavActionPerformed

    private void btn_feicho_dia_lav1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_feicho_dia_lav1ActionPerformed
    {//GEN-HEADEREND:event_btn_feicho_dia_lav1ActionPerformed
        // TODO add your handling code here:
//        new UsuarioAlterarSenhaVissao( usuarioDao.findTbUsuario( idUser ) ).setVisible( true );
        try
        {
            new VendaUsuarioVisao(idUser, conexao, "Factura-Proforma").setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btn_feicho_dia_lav1ActionPerformed

    private void btn_feicho_dia_lav2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_feicho_dia_lav2ActionPerformed
    {//GEN-HEADEREND:event_btn_feicho_dia_lav2ActionPerformed
         new UsuarioAlterarSenhaVissao( usuarioDao.findTbUsuario( idUser ) ).setVisible( true );
    }//GEN-LAST:event_btn_feicho_dia_lav2ActionPerformed

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
            java.util.logging.Logger.getLogger( JanelaFrontOfficeLavandariaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( JanelaFrontOfficeLavandariaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( JanelaFrontOfficeLavandariaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( JanelaFrontOfficeLavandariaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new JanelaFrontOfficeLavandariaVisao( 15, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRecolha;
    private javax.swing.JButton btnRelatorio;
    private static javax.swing.JButton btn_abertura_dia_lav;
    private static javax.swing.JButton btn_feicho_dia_lav;
    private static javax.swing.JButton btn_feicho_dia_lav1;
    private static javax.swing.JButton btn_feicho_dia_lav2;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    private String getMesa()
    {
        TbMesas mesa = ( TbMesas ) mesasController.findById( DVML.MESA_BALCAO );
        return mesa.getDesignacao();
    }

    private String getLugar()
    {
        TbLugares lugares = ( TbLugares ) lugaresController.findById( DVML.LUGAR_BALCAO );
        return lugares.getDesignacao();
    }

    private void setWindowsListener()
    {
        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowActivated( WindowEvent e )
            {
                MetodosUtil.verificarCaixa( caixa_controller,
                        idUser,
                        btn_abertura_dia_lav,
                        btn_feicho_dia_lav,
                        btnRecolha );

                TbUsuario usuarioByCodigo = usuariosController.getUsuarioByCodigo( idUser );
                if ( Objects.equals( usuarioByCodigo.getIdTipoUsuario().getIdTipoUsuario(), DVML.COD_TIPO_USUARIO_ADMINISTRADOR ) )
                {
                    btnRelatorio.setVisible( true );
                }
                else
                {
                    btnRelatorio.setVisible( false );
                }
            }

        } );

    }

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

}
