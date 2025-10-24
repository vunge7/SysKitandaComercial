/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;
import java.awt.AWTEvent;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import visao.LoginVisao;

/**
 *
 * @author hp
 */
public class AutoLoginUtil
{

    private Timer logoutTimer;
    private final int TIMEOUT = 15 * 60 * 1000; // 15 minutos em milissegundos

    private void iniciarMonitoramento()
    {
        resetTimer();

        // Adiciona um listener global para detectar atividades do usuário
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( ( KeyEvent e ) ->
        {
            resetTimer();
            return false;
        } );

        Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
        {
            @Override
            public void eventDispatched( AWTEvent event )
            {
                if ( event.getID() == MouseEvent.MOUSE_MOVED
                        || event.getID() == MouseEvent.MOUSE_CLICKED
                        || event.getID() == MouseEvent.MOUSE_PRESSED )
                {
                    resetTimer();
                }
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
        logoutTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                SwingUtilities.invokeLater( () ->
                {
                    JOptionPane.showMessageDialog( null, "Sessão expirada! Voltando para o login." );
                    new LoginVisao().setVisible( true ); // Substitua pelo seu login
                } );
            }
        }, TIMEOUT );
    }

}
