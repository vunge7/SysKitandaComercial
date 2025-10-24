/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rh.visao;

import dao.EmpresaDao;
import dao.ItemPermissaoDao;
import dao.UsuarioDao;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import util.BDConexao;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import visao.DadosInstituicaoVisao;
import visao.LoginVisao;
import visao.RootVisao;
import visao.UsuarioVisao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MenuPrincipalRHNovoVisao extends javax.swing.JFrame
{

    private final EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private final UsuarioDao usuarioDao = new UsuarioDao( emf );
    private final EmpresaDao empresaDao = new EmpresaDao( emf );
    private final ItemPermissaoDao itemPermissaoDao = new ItemPermissaoDao( emf );
    private final BDConexao conexao;
    private int idUser = 0;
    private boolean administrador = false;
    private int idEmpresa = 0;

    public MenuPrincipalRHNovoVisao( int id_user, int id_empresa, boolean administrador, BDConexao conexao )
    {
        initComponents();
        setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        setLocationRelativeTo( null );
        this.setExtendedState( this.getExtendedState() | MenuPrincipalRHNovoVisao.MAXIMIZED_BOTH );
//        System.out.println( "Tamanho Form"+this.getSize() );
//        System.out.println( "Tamanho Form"+JFrame.get );
        this.idUser = id_user;
        this.administrador = administrador;
        this.idEmpresa = id_empresa;
        this.conexao = conexao;
        try
        {
            lbNome.setText( usuarioDao.findTbUsuario( this.idUser ).getNome() );
//            lb_nome_professor.setText( funcionarioDao.findFuncionario( this.idProfessor ).getNome() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
//        lbNome.setText( empresaDao.findEmpresa( idEmpresa ).getNome() );
        busca_permissao();
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        lbNome = new javax.swing.JLabel();
        lb_nome_software = new javax.swing.JLabel();
        lb_descricao_software = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        fundo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_seja_bem_vindo1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lb_seja_bem_vindo1.setForeground(new java.awt.Color(255, 255, 255));
        lb_seja_bem_vindo1.setText("USUARIO:");
        getContentPane().add(lb_seja_bem_vindo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 710, 82, 50));

        lbNome.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        lbNome.setForeground(new java.awt.Color(255, 255, 255));
        lbNome.setText("Sede");
        getContentPane().add(lbNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 710, 400, 50));

        lb_nome_software.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        lb_nome_software.setForeground(new java.awt.Color(255, 255, 255));
        lb_nome_software.setText("RH");
        getContentPane().add(lb_nome_software, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 610, -1, 50));

        lb_descricao_software.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lb_descricao_software.setForeground(new java.awt.Color(255, 255, 255));
        lb_descricao_software.setText("Kitanda - Gestão de Recursos Humanos");
        getContentPane().add(lb_descricao_software, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 670, 310, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/calendario 32x32.png"))); // NOI18N
        jButton1.setText("Mapas/Relatórios");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 280, 50));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1588_32x32.png"))); // NOI18N
        jButton2.setText("Ficha do Funcionário");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 280, 50));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        jButton3.setText("Recibos");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 280, 50));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/9442_32x32.png"))); // NOI18N
        jButton4.setText("Outras Operações");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 280, 50));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        jButton5.setText("Tabelas Dinâmicas");
        jButton5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 280, 50));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2215_32x32.png"))); // NOI18N
        jButton6.setText("Definições");
        jButton6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 280, 50));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 32x32.png"))); // NOI18N
        jButton8.setText("Terminar Sessão");
        jButton8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton8ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 280, 50));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuario.png"))); // NOI18N
        jButton9.setText("Usuario");
        jButton9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, 280, 50));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/alterar_32x32.png"))); // NOI18N
        jButton10.setText("Dados da Empresa");
        jButton10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 280, 50));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pauta academica 32x32.png"))); // NOI18N
        jButton7.setText("Configurações");
        jButton7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 280, 50));

        fundo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/fundo_rh.png"))); // NOI18N
        getContentPane().add(fundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -70, 1920, 1130));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new FichaFuncionarioVisao( idUser, idEmpresa, conexao ).show();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        new RecibosVisao( idUser, idEmpresa, conexao ).show();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new ExtraSalarioFuncionarioVisao( idUser, idEmpresa, conexao ).show();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        // TODO add your handling code here:
        new TabelasDinamicasVisao( conexao ).show();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
    {//GEN-HEADEREND:event_jButton6ActionPerformed
        // TODO add your handling code here:
        new DefinicoesVisao( idUser, idEmpresa, conexao ).show();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton9ActionPerformed
    {//GEN-HEADEREND:event_jButton9ActionPerformed
        new UsuarioVisao( this, rootPaneCheckingEnabled, this.conexao ).show();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
    {//GEN-HEADEREND:event_jButton8ActionPerformed
        MetodosUtil.fechar_todas_janelas();
        MetodosUtil.actualizar_status( this.idUser, "OFF" );
//        MetodosUtil.actualizar_ip_address( this.idUser, "" );
        //new LoginVisao().show();
        new RootVisao( this.idUser, 1, administrador, this.conexao ).show();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        try
        {
            new ListarMapaSalario( idUser, this.conexao ).show();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton10ActionPerformed
    {//GEN-HEADEREND:event_jButton10ActionPerformed
        // TODO add your handling code here:
        new DadosInstituicaoVisao( this, rootPaneCheckingEnabled ).setVisible( true );
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton7ActionPerformed
    {//GEN-HEADEREND:event_jButton7ActionPerformed
        new Configuracoes( idUser ).show();
    }//GEN-LAST:event_jButton7ActionPerformed

    public void busca_permissao()
    {

        try
        {
            // TODO add your handling code here
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

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
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( MenuPrincipalRHNovoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( MenuPrincipalRHNovoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( MenuPrincipalRHNovoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( MenuPrincipalRHNovoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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
                new MenuPrincipalRHNovoVisao( 15, 2, true, new BDConexao() ).show();
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fundo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel lbNome;
    private javax.swing.JLabel lb_descricao_software;
    private javax.swing.JLabel lb_nome_software;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    // End of variables declaration//GEN-END:variables

    private void sair()
    {
        dispose();
        new RootVisao( this.idUser, this.idEmpresa, true, this.conexao ).show();
    }

    private void terminarSessao()
    {
        dispose();
        new LoginVisao( new BDConexao() ).setVisible( true );
    }

}
