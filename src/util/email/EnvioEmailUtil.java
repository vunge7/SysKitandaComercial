/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.email;


import java.sql.Connection;
import dao.DadosInstituicaoDao;
import java.io.File;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class EnvioEmailUtil
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );

    private static final String DE = "dvml.app@gmail.com";
    private static final String SENHA = "lezd hdjp zjtf qgaq";
    private static String PARA;
    private static String ASSUNTO = "Relatório Diário";
//    private static String MENSAGEM = "Córdiais saudações , em anexo enviamos-lhe  o relatório diário da aplicação.\nAtenciosamente:\nDVML-COMERCIAL, LDA";
    private static String MENSAGEM = "";
    private static final String CAMINHO = "anexos/";

    private static Vector<File> files = new Vector<>();

    public EnvioEmailUtil()
    {
        setEmail();
    }

    public static void inicio()
    {

        setEmail();
        if ( existeConexaoInternet() )
        {
            adicionarFicheiros();
            try
            {
                MimeMessage message = new MimeMessage( getSessao() );
                //Remetente
                message.setFrom( new InternetAddress( DE ) );

                //Destinatário(s)
                Address[] toUser = InternetAddress
                        .parse( PARA );

                message.setRecipients( Message.RecipientType.TO, toUser );

                //Ficheiro Text
                MimeBodyPart mimeBodyPart = new MimeBodyPart();

                //Assunto
                message.setSubject( ASSUNTO );
                //Corpo do email.
                mimeBodyPart.setText( MENSAGEM );

                //anexar
                message.setContent( configurarCorpoMensagemEAnexos( mimeBodyPart ) );
                /**
                 * Método para enviar a mensagem criada
                 */
                Transport.send( message );

//                JOptionPane.showMessageDialog( null, "Email enviado com sucesso." );
                System.out.println( "Email enviado com sucesso." );
            }
            catch ( MessagingException e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao enviar o email.", "Falha", JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
                System.err.println( "Erro: " + e.getLocalizedMessage() );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Falha ao enviar o email.\nVerifique a conexão com a internet.", "Falha", JOptionPane.ERROR_MESSAGE );
        }

    }

    public static void inicio( String file_name )
    {

        setEmail();
        if ( existeConexaoInternet() )
        {
            adicionarFicheiros( file_name );
            try
            {
                MimeMessage message = new MimeMessage( getSessao() );
                //Remetente
                message.setFrom( new InternetAddress( DE ) );

                //Destinatário(s)
                Address[] toUser = InternetAddress
                        .parse( PARA );

                message.setRecipients( Message.RecipientType.TO, toUser );

                //Ficheiro Text
                MimeBodyPart mimeBodyPart = new MimeBodyPart();

                //Assunto
                message.setSubject( ASSUNTO );
                //Corpo do email.
                mimeBodyPart.setText( MENSAGEM );

                //anexar
                message.setContent( configurarCorpoMensagemEAnexos( mimeBodyPart ) );
                /**
                 * Método para enviar a mensagem criada
                 */
                Transport.send( message );

                JOptionPane.showMessageDialog( null, "Fecho realizado com sucesso!\nEmail enviado com sucesso." );
                System.out.println( "Feito!!!" );
            }
            catch ( MessagingException e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao enviar o email.", "Falha", JOptionPane.ERROR_MESSAGE );
                System.err.println( "Erro: " + e.getLocalizedMessage() );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( null, "Falha ao enviar o email.", "Falha", JOptionPane.ERROR_MESSAGE );
        }

    }

    private static Session getSessao()
    {

        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Gmail
         */
        props.put( "mail.smtp.host", "smtp.gmail.com" );
//        props.put("mail.smtp.ssl.enable", "true");
        props.put( "mail.smtp.starttls.enable", "true" );
//        props.put( "mail.smtp.socketFactory.port", "465" );
        props.put( "mail.smtp.socketFactory.port", "465" );
        props.put( "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory" );
        props.put( "mail.smtp.auth", "true" );
//        props.put( "mail.smtp.port", "465" );
        props.put( "mail.smtp.port", "465" );

        Session session = Session.getDefaultInstance( props,
                new javax.mail.Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication( DE,
                        SENHA );
            }
        } );
        /**
         * Ativa Debug para sessão
         */
        session.setDebug( true );

        return session;

    }

    private static Multipart configurarCorpoMensagemEAnexos( MimeBodyPart mimeBodyPart )
    {

        Multipart mp = new MimeMultipart();
        try
        {
            for ( int i = 0; i < files.size(); i++ )
            {
                try
                {
                    MimeBodyPart mbp = new MimeBodyPart();
                    File elementAt = files.elementAt( i );
                    DataSource fds = new FileDataSource( elementAt );
                    mbp.setDisposition( Part.ATTACHMENT );
                    mbp.setDataHandler( new DataHandler( fds ) );
                    mbp.setFileName( fds.getName() );
                    mp.addBodyPart( mbp );
                }
                catch ( MessagingException ex )
                {
                    Logger.getLogger( EnvioEmailUtil.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
            mp.addBodyPart( mimeBodyPart );

        }
        catch ( MessagingException ex )
        {
            Logger.getLogger( EnvioEmailUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return mp;
    }

    private static boolean existeConexaoInternet()
    {
        try
        {
            java.net.URL url = new java.net.URL( "http://www.google.com.br" );
            java.net.URLConnection conn = url.openConnection();

            java.net.HttpURLConnection httpConn = ( java.net.HttpURLConnection ) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if ( x == 200 )
            {
                System.out.println( "Existe conexão com a internet" );
                return true;
            }
            else
            {
                System.err.println( "Não existe conexão com a internet" );
            }
        }
        catch ( java.net.MalformedURLException urlmal )
        {
            System.err.println( "Não existe conexão com a internet: " + urlmal.getLocalizedMessage() );
        }
        catch ( java.io.IOException ioexcp )
        {
            System.err.println( "Não existe conexão com a internet: " + ioexcp.getLocalizedMessage() );
        }

        return false;

    }

    private static void setEmail()
    {
        PARA = dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getEmail();
    }

    private static void adicionarFicheiros()
    {
        File file_1 = new File( CAMINHO, "relatorio_diario_mes_caixa.pdf" );
        File file_2 = new File( CAMINHO, "relatorio_diario_resumo_caixa_lavandaria.pdf" );

        files.add( file_1 );
        files.add( file_2 );
    }

    private static void adicionarFicheiros( String file_name )
    {
        File file_1 = new File( CAMINHO, file_name );
        files.add( file_1 );

    }

    public static void main( String[] args )
    {
        existeConexaoInternet();
        inicio();
    }

}
