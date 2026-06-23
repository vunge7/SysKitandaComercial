/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.plu;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 8/dez/2025
 * @lastModified 8/dez/2025
 */
import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DibalSenderForm extends JFrame
{

    private JTextField txtIp, txtPorta, txtCodigo, txtPreco, txtNome;
    private JComboBox<String> cbOperacao;

    public DibalSenderForm()
    {
        setTitle( "Enviar Artigos para Balança DIBAL BH-P4860" );
        setSize( 460, 300 );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setLocationRelativeTo( null );

        JPanel painel = new JPanel();
        painel.setLayout( new GridLayout( 6, 2, 5, 5 ) );

        cbOperacao = new JComboBox<>( new String[]
        {
            "A - Adicionar / Modificar", "B - Remover"
        } );
        txtCodigo = new JTextField();
        txtPreco = new JTextField();
        txtNome = new JTextField();
        txtIp = new JTextField( "192.168.1.90" );
        txtPorta = new JTextField( "3000" );

        painel.add( new JLabel( "Operação:" ) );
        painel.add( cbOperacao );

        painel.add( new JLabel( "Código PLU (1-999999):" ) );
        painel.add( txtCodigo );

        painel.add( new JLabel( "Nome (max 24 chars):" ) );
        painel.add( txtNome );

        painel.add( new JLabel( "Preço (ex: 0700 = 700Kz):" ) );
        painel.add( txtPreco );

        painel.add( new JLabel( "IP da Balança:" ) );
        painel.add( txtIp );

        painel.add( new JLabel( "Porta:" ) );
        painel.add( txtPorta );

        JButton btnEnviar = new JButton( "Enviar para DIBAL" );
        btnEnviar.addActionListener( e -> enviarParaDibal() );

        add( painel, BorderLayout.CENTER );
        add( btnEnviar, BorderLayout.SOUTH );
    }

    private void enviarParaDibal()
    {
        try
        {
            String operacao = cbOperacao.getSelectedItem().toString().substring( 0, 1 );

            // PLU = 6 dígitos obrigatórios
            String codigo = String.format( "%06d", Integer.parseInt( txtCodigo.getText().trim() ) );

            // Nome máximo 24 caracteres
            String nome = txtNome.getText().trim();
            if ( nome.length() > 24 )
            {
                nome = nome.substring( 0, 24 );
            }

            // Preço no formato DIBAL (4 dígitos)
            String preco = txtPreco.getText().trim();
            if ( preco.length() < 4 )
            {
                preco = String.format( "%04d", Integer.parseInt( preco ) );
            }

            char STX = 0x02; // Início do comando
            char ETX = 0x03; // Fim do comando

            String comando;
            if ( operacao.equals( "A" ) )
            {
                comando = "0201|PLU|" + codigo + "|" + nome + "|" + preco + "|0";
            }
            else
            {
                comando = "0201|DPLU|" + codigo;
            }

            // Cria array de bytes com STX e ETX
            byte[] linhaBytes = new byte[ comando.length() + 2 ];
            linhaBytes[ 0 ] = ( byte ) STX;
            System.arraycopy( comando.getBytes( StandardCharsets.ISO_8859_1 ), 0, linhaBytes, 1, comando.length() );
            linhaBytes[ linhaBytes.length - 1 ] = ( byte ) ETX;

            String ip = txtIp.getText().trim();
            int porta = Integer.parseInt( txtPorta.getText().trim() );

            // Envio via Socket
            try ( Socket socket = new Socket( ip, porta ); OutputStream out = socket.getOutputStream() )
            {

                out.write( linhaBytes );
                out.flush();
            }

            JOptionPane.showMessageDialog( this,
                    "Comando enviado:\n" + comando,
                    "SUCESSO",
                    JOptionPane.INFORMATION_MESSAGE );

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( this,
                    "Erro: " + ex.getMessage(),
                    "ERRO AO ENVIAR",
                    JOptionPane.ERROR_MESSAGE );
        }
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new DibalSenderForm().setVisible( true ) );
    }
}
