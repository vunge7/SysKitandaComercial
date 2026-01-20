package util.fe;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QrCodeUtil
{

    public static BufferedImage gerarQrComLogo(
            String nifEmissor,
            String documentNo
    ) throws Exception
    {
        int size = 350;

        // 👉 URL conforme especificação da AGT
        String baseUrl = "https://quiosqueagt.minfin.gov.ao/facturacao-eletronica/consultar-fe";
        String query = "?emissor=" + URLEncoder.encode( nifEmissor, StandardCharsets.UTF_8.toString() )
                                + "&document=" +   URLEncoder.encode( documentNo.replaceAll( " ", "%20"), StandardCharsets.UTF_8.toString() );
//                + "&document=" + documentNo.replaceAll( " ", "%20" );

        String fullUrl = baseUrl + query;

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put( EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M ); // ⚠️ Nível M conforme AGT
        hints.put( EncodeHintType.CHARACTER_SET, "UTF-8" );
        hints.put( EncodeHintType.MARGIN, 1 );

        BitMatrix matrix = new QRCodeWriter().encode(
                fullUrl,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
        );

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage( matrix );

        // 👉 Carregar logo AGT
        InputStream logoStream = QrCodeUtil.class
                .getResourceAsStream( "/imagens/agt_logo.png" );

        BufferedImage logo = ImageIO.read( logoStream );

        // ⚠️ Logo deve ocupar < 20% da imagem total
        int logoSize = size / 5;
        int x = ( size - logoSize ) / 2;
        int y = ( size - logoSize ) / 2;

        Graphics2D g = qrImage.createGraphics();
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.drawImage( logo, x, y, logoSize, logoSize, null );
        g.dispose();

        return qrImage;
    }
}
