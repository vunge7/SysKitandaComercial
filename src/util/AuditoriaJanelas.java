package util;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * Regista em log_actividade a abertura de ecrãs (JFrame/JDialog da aplicação)
 * e de relatórios (JasperViewer), através de um listener global de eventos AWT.
 *
 * Instalado em LoginVisao após a autenticação.
 */
public class AuditoriaJanelas
{

    private static boolean instalado = false;

    // último ecrã da aplicação activo: indica de onde foi aberto um relatório
    private static volatile String ultimoEcra;

    public static synchronized void instalar()
    {
        if ( instalado )
        {
            return;
        }
        instalado = true;
        Toolkit.getDefaultToolkit().addAWTEventListener( evento ->
        {
            try
            {
                tratar( (WindowEvent) evento );
            }
            catch ( Throwable t )
            {
                System.err.println( "[AuditoriaJanelas] " + t.getMessage() );
            }
        }, AWTEvent.WINDOW_EVENT_MASK );
    }

    private static void tratar( WindowEvent evento )
    {
        Window janela = evento.getWindow();
        if ( janela == null || janela instanceof JWindow || SessaoUtil.getCodUsuario() == null )
        {
            return;
        }
        String classe = janela.getClass().getName();
        String nome = janela.getClass().getSimpleName();

        boolean relatorio = classe.startsWith( "net.sf.jasperreports." );
        // JOptionPane e diálogos genéricos do Swing não são ecrãs da aplicação
        boolean generica = janela.getClass() == JDialog.class || janela.getClass() == JFrame.class
                || classe.startsWith( "javax." ) || classe.startsWith( "java." ) || classe.startsWith( "sun." )
                || nome.isEmpty() || "LoginVisao".equals( nome );

        if ( evento.getID() == WindowEvent.WINDOW_ACTIVATED && !relatorio && !generica )
        {
            ultimoEcra = nome;
            return;
        }
        if ( evento.getID() != WindowEvent.WINDOW_OPENED )
        {
            return;
        }

        if ( relatorio )
        {
            String titulo = titulo( janela );
            LogActividadeUtil.registarComFormulario( LogActividadeUtil.MODULO_RELATORIOS, ultimoEcra,
                    LogActividadeUtil.VISUALIZAR, null, null,
                    "Abriu o relatório" + ( titulo.isEmpty() ? "" : ": " + titulo )
                    + ( ultimoEcra != null ? " (a partir de " + ultimoEcra + ")" : "" ) );
        }
        else if ( !generica )
        {
            ultimoEcra = nome;
            String titulo = titulo( janela );
            LogActividadeUtil.registarComFormulario( LogActividadeUtil.MODULO_ECRAS, nome,
                    LogActividadeUtil.ABRIR, null, null,
                    "Abriu o ecrã: " + ( titulo.isEmpty() ? nome : titulo ) );
        }
    }

    private static String titulo( Window janela )
    {
        String titulo = null;
        if ( janela instanceof Frame )
        {
            titulo = ( (Frame) janela ).getTitle();
        }
        else if ( janela instanceof Dialog )
        {
            titulo = ( (Dialog) janela ).getTitle();
        }
        return titulo == null ? "" : titulo.trim();
    }

}
