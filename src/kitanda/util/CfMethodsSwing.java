/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.swing.*;
import kitanda.util.CfDefs.Mensagem;
import kitanda.util.CfDefs.Titulo;
import static kitanda.util.CfDefs.*;

/**
 *
 * @author tagif
 */
public class CfMethodsSwing
{

    public static JLabel cfMsgJLabel;

    static
    {

    }

    public static boolean showConfirmDialog ( Icon icon, String titulo, String msg )
    {
        return JOptionPane.showOptionDialog ( null, msg, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, new Object[]
        {
            "Sim", "Não"
        }, "Não" ) == 0;
    }

    public static void showMessageDialog ( String titulo, String msg )
    {
        JOptionPane.showMessageDialog ( null, msg, titulo, JOptionPane.INFORMATION_MESSAGE );
    }

    public static void showMessageDialog ( Titulo titulo, Mensagem msg )
    {
        showMessageDialog ( titulosJanelas.get ( titulo ), mensagens.get ( msg ) );
    }

    public static void resizeJLableIcon ( int width, int height, URL imagePath, JLabel label )
    {

       
        label.setIcon (resizeIcon (  width,  height,  imagePath) );
    }

    public static ImageIcon resizeIcon ( int width, int height, URL imagePath)
    {

        width = ( width <= 0 ) ? 1 : width;
        height = ( height <= 0 ) ? 1 : height;
        ImageIcon imageIcon = new ImageIcon ( imagePath );
        Image image = imageIcon.getImage ();
        Image newImage = image.getScaledInstance ( width, height, Image.SCALE_SMOOTH );
        ImageIcon newImageIcon = new ImageIcon ( newImage );

        return newImageIcon;
    }

    public static void resizeJButtonIcon ( int width, int height, URL imagePath, JButton label )
    {
        label.setIcon ( resizeIcon (  width,  height,  imagePath) );
    }

    public static ArrayList<String> converterJListEmDados ( JList lista )
    {
        ArrayList<String> resultados = new ArrayList<> ();

        DefaultListModel defaultListModel = ( DefaultListModel ) lista.getModel ();
        defaultListModel.getSize ();

        for ( int i = 0; i < defaultListModel.getSize (); i ++ )
        {
            resultados.add ( ( String ) defaultListModel.get ( i ) );
        }

        return resultados;
    }

    public static DefaultListModel converterDadosToListModel ( ArrayList<String> dados )
    {
        DefaultListModel defaultListModel = new DefaultListModel ();

        for ( String dado : dados )
        {
            defaultListModel.addElement ( dado );
        }

        return defaultListModel;
    }

    public static ArrayList<String> converterJComboBoxEmDados ( JComboBox lista )
    {
        ArrayList<String> resultados = new ArrayList<> ();

        DefaultComboBoxModel defaultListModel = ( DefaultComboBoxModel ) lista.getModel ();
        defaultListModel.getSize ();

        for ( int i = 0; i < defaultListModel.getSize (); i ++ )
        {
            resultados.add ( ( String ) defaultListModel.getElementAt ( i ) );
        }

        return resultados;
    }

    public static DefaultComboBoxModel converterDadosToComboBoxModel ( java.util.List<String> dados )
    {
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel ();

        for ( String dado : dados )
        {
            comboBoxModel.addElement ( dado );
        }

        return comboBoxModel;
    }

    public static void showLayoutCard ( Container container, String panelName )
    {
        CardLayout cardLayout = ( CardLayout ) container.getLayout ();
        cardLayout.show ( container, panelName );
        container.repaint ();
    }

    public static void showButtonCard ( Container container, ActionEvent enevto )
    {
        JButton button = ( JButton ) enevto.getSource ();
        showLayoutCard ( container, button.getName () );
    }

    public static SpinnerNumberModel criarSpinnerNumberModel ( int min, int max, int value )
    {

        int step = 1;

        if ( max == 0 )
        {
            value = 0;
            min = 0;
        }

        return new SpinnerNumberModel ( value, min, max, step );
    }
    
    

    public static SpinnerNumberModel criarSpinnerDoubleModel ( double min, double max, double value )
    {

        double step = 1.0;

        if ( max == 0 )
        {
            value = 0.0;
            min = 0.0;
        }

        return new SpinnerNumberModel ( value, min, max, step );
    }

    public static SpinnerListModel criarSpinnerListModel ( Object... values )
    {
        return new SpinnerListModel ( values );
    }

    public static int getSpinnerListSelectedIndex ( SpinnerListModel spinnerListModel )
    {
        java.util.List list = ( ( SpinnerListModel ) spinnerListModel ).getList ();
        return list.indexOf ( spinnerListModel.getValue () );
    }

    public static Object getSpinnerListValueAt ( int index, SpinnerListModel spinnerListModel )
    {
        java.util.List list = ( ( SpinnerListModel ) spinnerListModel ).getList ();

        return list.get ( index );
    }

    // displayNotification ( "Registro criado com sucesso!", MSG_SUCESSO_CRIAR_1 );
    public static void displayNotification ( String msg, JLabel commponente, Icon icon )
    {
        cfMsgJLabel = commponente;

        cfMsgJLabel.setText ( msg );
        cfMsgJLabel.setIcon ( icon );
        cfMsgJLabel.setForeground ( Color.GREEN );
        cfMsgJLabel.setVisible ( true );

        java.util.Timer t = new java.util.Timer ( "doubleclickTimer", false );

        t.schedule ( new TimerTask ()
        {

            @Override
            public void run ()
            {
                System.err.println ( "Resetou . . . " );
                cfMsgJLabel.setText ( "" );
                cfMsgJLabel.setIcon ( null );
                cfMsgJLabel.setVisible ( false );
            }
        }, 5 * 1000 );
    }

    // displayNotification ( "Registro criado com sucesso!", MSG_SUCESSO_CRIAR_1 );
    public static void displayNotification ( Mensagem type, JLabel commponente, Icon icon )
    {
        displayNotification ( mensagens.get ( type ), commponente, icon );
    }
}
