/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import comercial.controller.LugaresController;
import comercial.controller.MesasController;
import dao.AccessoArmazemDao;
import dao.ArmazemDao;
import dao.UsuarioDao;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import static util.MetodosUtil.fechar_todas_janelas;
import static util.MetodosUtil.rodarComandoWindows;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FrontOfficeRestauranteVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private AccessoArmazemDao accessoArmazemDao = new AccessoArmazemDao(emf );
    private MesasController mesasController;
    private LugaresController lugaresController;
    private static ArmazemDao armazemDao = new ArmazemDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private static int id_usuario = 0;
    private static int id_armazem = 0;
    private static BDConexao conexao;

    public FrontOfficeRestauranteVisao( int id_usuario, BDConexao conexao )
    {
        initComponents();
        this.setExtendedState( VendaPOSVisao.MAXIMIZED_BOTH );
        this.id_usuario = id_usuario;
        this.conexao = conexao;
        mesasController = new MesasController( conexao );
        lugaresController = new LugaresController( conexao );
//        jButton3.setVisible( false);
        adicionar_radio_button();
        // aspecto dos menus do SysKitandaHotel (marca Kitanda), sobre o initComponents
        util.ui.LayoutSeguro.aplicar( this, "Front Office", this::montarLayoutWeb );
    }

    // =====================================================================
    // LAYOUT WEB (o mesmo do FrontOfficeVisao: barra lateral azul, armazém em
    // pílulas e as operações em cartões). Os botões e eventos são os do
    // initComponents; o .form fica com o desenho antigo.
    // =====================================================================
    private final java.util.List<javax.swing.JToggleButton> pilulasArmazem = new java.util.ArrayList<>();
    private final util.ui.ComponentesWeb.Etiqueta etiquetaArmazem
            = new util.ui.ComponentesWeb.Etiqueta( "Nenhum armazém escolhido", util.ui.ComponentesWeb.LARANJA );

    private void montarLayoutWeb()
    {
        String nome = "Utilizador", perfil = null;
        try
        {
            TbUsuario u = usuarioDao.findTbUsuario( id_usuario );
            nome = u.getNome();
            perfil = u.getIdTipoUsuario() == null ? null : u.getIdTipoUsuario().getDescricao();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        util.ui.PaginaMenuWeb pagina = new util.ui.PaginaMenuWeb( "Front Office",
                "Escolha o armazém e depois a operação.", new javax.swing.JLabel( nome ), perfil );

        // ---- barra lateral: atalhos para as operações
        pagina.grupo( "Operações" );
        javax.swing.JButton atalhoMesas = new javax.swing.JButton( "Gestão de Mesas" );
        atalhoMesas.addActionListener( e -> jButton2.doClick() );
        pagina.item( atalhoMesas, "Gestão de Mesas" );
        javax.swing.JButton atalhoExpresso = new javax.swing.JButton( "Venda Expresso" );
        atalhoExpresso.addActionListener( e -> jButton5.doClick() );
        pagina.item( atalhoExpresso, "Venda Expresso" );

        // ---- 1. armazém: pílulas (carregam nos botões originais, mesma regra)
        javax.swing.ButtonGroup grupo = new javax.swing.ButtonGroup();
        javax.swing.JPanel pilulas = util.ui.ComponentesWeb.transparente(
                new util.ui.EstiloUI.WrapLayout( java.awt.FlowLayout.LEFT, 8, 6 ) );
        for ( java.awt.Component c : painel_armazem.getComponents() )
        {
            if ( !( c instanceof javax.swing.JButton ) )
            {
                continue;
            }
            final javax.swing.JButton original = ( javax.swing.JButton ) c;
            javax.swing.JToggleButton t = new javax.swing.JToggleButton( original.getText() );
            util.ui.ComponentesWeb.pilula( t, false );
            t.setFont( util.ui.EstiloUI.FONTE_NEGRITO.deriveFont( 14f ) );
            t.setBorder( javax.swing.BorderFactory.createEmptyBorder( 9, 16, 9, 20 ) );
            t.setToolTipText( "Trabalhar com o armazém " + original.getText() );
            t.addActionListener( e ->
            {
                original.doClick(); // Evento: lb_armazem_selecionado + actualizar_armazem
                etiquetaArmazem.setText( "Armazém: " + original.getText() );
                etiquetaArmazem.setCor( util.ui.ComponentesWeb.VERDE );
            } );
            grupo.add( t );
            pilulas.add( t );
            pilulasArmazem.add( t );
        }
        if ( pilulasArmazem.isEmpty() )
        {
            etiquetaArmazem.setText( "Sem armazéns disponíveis" );
            etiquetaArmazem.setCor( util.ui.ComponentesWeb.VERMELHO );
        }
        else if ( pilulasArmazem.size() == 1 )
        {
            pilulasArmazem.get( 0 ).doClick(); // um só armazém: fica logo escolhido
        }
        javax.swing.JPanel linhaArmazem = util.ui.ComponentesWeb.transparente( new java.awt.BorderLayout( 0, 6 ) );
        linhaArmazem.add( pilulas, java.awt.BorderLayout.CENTER );
        javax.swing.JPanel etiq = util.ui.ComponentesWeb.transparente( new java.awt.FlowLayout( java.awt.FlowLayout.LEFT, 0, 0 ) );
        etiq.add( etiquetaArmazem );
        linhaArmazem.add( etiq, java.awt.BorderLayout.SOUTH );

        // ---- 2. operação: cartões grandes, como nos menus
        util.ui.MenuWeb.cartaoModulo( jButton2, util.ui.MenuWeb.Simbolo.TALHERES, "Gestão de Mesas",
                "Plano da sala, pedidos por lugar, contas e divisão", 270, 170 );
        util.ui.MenuWeb.cartaoModulo( jButton5, util.ui.MenuWeb.Simbolo.PRANCHETA, "Venda Expresso",
                "Venda rápida ao balcão", 270, 170 );
        javax.swing.JPanel cartoes = util.ui.FormularioWeb.grelha( 270, 3, jButton2, jButton5 );

        javax.swing.JPanel conteudo = util.ui.FormularioWeb.pilha( 10,
                util.ui.MenuWeb.rotuloGrupo( "1 · Armazém", util.ui.MenuWeb.DOURADO.darker() ),
                linhaArmazem,
                javax.swing.Box.createVerticalStrut( 6 ),
                util.ui.MenuWeb.rotuloGrupo( "2 · Operação", util.ui.MenuWeb.DOURADO.darker() ),
                cartoes );
        pagina.conteudo( conteudo );
        pagina.sair( jButton4, "Voltar ao menu principal ou ao início de sessão" );
        pagina.montar( this, "SysKitanda Comercial  ·  Front Office", 4 );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.new
     *
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lb_armazem_selecionado = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        painel_armazem = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(835, 120));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 48)); // NOI18N
        jLabel3.setText("FRONT OFFICE");
        jPanel1.add(jLabel3);

        lb_armazem_selecionado.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        lb_armazem_selecionado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_armazem_selecionado.setText(".");
        jPanel1.add(lb_armazem_selecionado);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("SELECCIONE O ARMAZEM:   ");
        jPanel2.add(jLabel2);

        painel_armazem.setLayout(new java.awt.GridLayout(2, 2));
        jPanel2.add(painel_armazem);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/mesa8.png"))); // NOI18N
        jButton2.setText("Gestao de Mesas");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 256x256.png"))); // NOI18N
        jButton4.setText("Sair");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens_mesas/icone.png"))); // NOI18N
        jButton5.setText("VENDA EXPRESSO");
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Atalho do Menu Restaurante: com um só armazém abre logo a gestão de mesas
     * (sem passar pelo Front Office); com vários, mostra o Front Office para se
     * escolher o armazém.
     */
    public static void abrirGestaoMesas( int idUsuario )
    {
        FrontOfficeRestauranteVisao f = new FrontOfficeRestauranteVisao( idUsuario, BDConexao.getInstancia() );
        if ( id_armazem > 0 && f.pilulasArmazem.size() == 1 )
        {
            new PrincipalPedidosVisao( idUsuario, "", id_armazem, BDConexao.getInstancia() ).setVisible( true );
            f.dispose();
        }
        else
        {
            f.setVisible( true );
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed

        if ( id_armazem > 0 )
        {
            new PrincipalPedidosVisao( id_usuario, "", id_armazem, BDConexao.getInstancia()).setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Caro usuario seleccione o armazem." );
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        fechar_todas_janelas();
        retorno();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        if ( id_armazem > 0 )
        {
            new VendasPraticasVisao( getMesa(), getLugar(), this.id_usuario, id_armazem, BDConexao.getInstancia()).setVisible(true);;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Caro usuario seleccione o armazem." );
        }
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger( FrontOfficeRestauranteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( FrontOfficeRestauranteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( FrontOfficeRestauranteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( FrontOfficeRestauranteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new FrontOfficeRestauranteVisao( 15, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JLabel lb_armazem_selecionado;
    private javax.swing.JPanel painel_armazem;
    // End of variables declaration//GEN-END:variables

    private void adicionar_radio_button()
    {

//        Vector<String> allArmazemByIdUSuario = accessoArmazemDao.getAllArmazemByIdUSuario( id_usuario );
//        Vector<String> allArmazemByIdUSuario = accessoArmazemDao.getAllArmazemExceptoEconomatoByIdUSuario( id_usuario );
        Vector<String> allArmazemByIdUSuario = armazemDao.buscaTodosExceptoEconomato();
        ButtonGroup buttonGroup = new ButtonGroup();
        if ( !Objects.isNull( allArmazemByIdUSuario ) )
        {
            for ( String valor : allArmazemByIdUSuario )
            {
                JButton button = new JButton( valor );
                buttonGroup.add( button );
                button.addActionListener( new Evento( button, valor ) );
                painel_armazem.add( button );
            }
        }

    }

    private void retorno()
    {

        TbUsuario usuario = usuarioDao.findTbUsuario( id_usuario );
        if ( usuario.getIdTipoUsuario().getIdTipoUsuario() == 1 )
        {
            new RootVisao( id_usuario, 1, true, BDConexao.getInstancia()).setVisible(true);
//            new MenuPrincipalVisao(id_usuario, 1, true, BDConexao.getInstancia()).setVisible(true);
        }
        else
        {
            new LoginVisao(BDConexao.getInstancia()).setVisible(true);
        }
    }

    private class Evento implements ActionListener
    {

        private String designacao = "";
        private JButton button = null;

        public Evento( JButton button, String designacao )
        {
            this.button = button;
            this.designacao = designacao;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            lb_armazem_selecionado.setText( designacao );
            actualizar_armazem( designacao );
        }

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

    private String getMesa()
    {
        TbMesas mesa = (TbMesas) mesasController.findById( DVML.MESA_BALCAO );
        return mesa.getDesignacao();
    }

    private String getLugar()
    {
        TbLugares lugares = (TbLugares) lugaresController.findById( DVML.LUGAR_BALCAO );
        return lugares.getDesignacao();
    }

    public static void actualizar_armazem( String valor )
    {
        id_armazem = armazemDao.getArmazemByDescricao( valor ).getCodigo();
    }

}
