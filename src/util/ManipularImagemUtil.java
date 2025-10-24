/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ManipularImagemUtil
{

    public static BufferedImage setImagemDimensao( String caminhoImag, Integer imgLargura, Integer imgAltura )
    {
        Double novaImgLargura = null;
        Double novaImgAltura = null;
        Double imgProporcao = null;
        Graphics2D g2d = null;
        BufferedImage imagem = null, novaImagem = null;
        try
        {
            imagem = ImageIO.read( new File( caminhoImag ) );
        }
        catch ( IOException e )
        {
            System.out.println( e.getMessage() );
        }

        novaImgLargura = ( double ) imagem.getWidth();
        novaImgAltura = ( double ) imagem.getHeight();

//        if ( novaImgLargura >= imgLargura )
//        {
//            imgProporcao = ( novaImgAltura / novaImgLargura );
//            novaImgLargura = ( double ) imgLargura;
//            novaImgAltura = ( novaImgLargura * imgProporcao );
//
//            while ( novaImgAltura > imgAltura )
//            {
//                novaImgLargura = ( double ) ( --imgLargura );
//                novaImgAltura = ( novaImgLargura * imgProporcao );
//
//            }
//
//        }
//        else if ( novaImgAltura >= imgAltura )
//        {
//            imgProporcao = ( novaImgLargura / novaImgAltura );
//            novaImgAltura = ( double ) imgAltura;
//
//            while ( novaImgLargura > imgLargura )
//            {
//                novaImgAltura = ( double ) ( --imgAltura );
//                novaImgLargura = (novaImgAltura * imgProporcao);
//            }
//        }
        novaImagem = new BufferedImage( novaImgLargura.intValue(), novaImgAltura.intValue(), BufferedImage.TYPE_INT_RGB );
        g2d = novaImagem.createGraphics();
        g2d.drawImage( imagem, 0, 0, novaImgLargura.intValue(), novaImgAltura.intValue(), null );
        return novaImagem;

    }

    public static byte[] getImgByte( BufferedImage image )
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            ImageIO.write( image, "JPEG", baos );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( ManipularImagemUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }
        InputStream is = new ByteArrayInputStream( baos.toByteArray() );

        return baos.toByteArray();

    }

    public static void exibirImagemLabel( byte[] minhaImagem, JLabel label )
    {

        if ( minhaImagem != null )
        {
            InputStream input = new ByteArrayInputStream( minhaImagem );

            try
            {
                BufferedImage image = ImageIO.read( input );
                label.setIcon( new ImageIcon( image ) );
            }
            catch ( IOException e )
            {
            }

        }
        else
        {
            label.setIcon( null );
        }

    }

    public static void mostrar_anexo( String nome_ficheiro, JLabel label )
    {
        File arquivo = new File( nome_ficheiro );

        try
        {
            BufferedImage imagem = setImagemDimensao( arquivo.getAbsolutePath(), 600, 500 );
            label.setIcon( new ImageIcon( imagem ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            label.setIcon( null);
        }

    }

}
