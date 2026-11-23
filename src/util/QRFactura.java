package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Image;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Código QR das facturas (FR, FT, NC, recibos...) no modo SAFT.
 *
 * <p>
 * Os modelos (facturaA6, facturaA4...) chamam {@code util.QRFactura.imagem($P{CODIGO_VENDA})}
 * ao preencher: não é preciso mudar nenhuma das classes que imprimem. O QR leva
 * os dados que identificam o documento, legíveis por qualquer leitor:
 *
 * <pre>A:NIF emitente*B:NIF cliente*D:tipo*F:data*G:número*N:IVA*O:total*Q:assinatura impressa*R:Kitanda</pre>
 *
 * Não é o QR da facturação electrónica da AGT (esse fica no ramo FE).
 * Se faltar algum dado o QR sai na mesma; se a venda não existir, sai em branco.
 * 2026-11-17.
 */
public final class QRFactura {

    private static final SimpleDateFormat DATA = new SimpleDateFormat("yyyyMMdd");

    private QRFactura() {
    }

    /** Imagem do QR da venda (null se não houver venda: o modelo deixa o espaço em branco). */
    public static Image imagem(Integer codigoVenda) {
        try {
            String t = texto(codigoVenda);
            return t == null ? null : imagem(t);
        } catch (Throwable e) {
            System.err.println("[QR factura] " + e);
            return null;
        }
    }

    public static Image imagem(String texto) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bm = new QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, 360, 360, hints);
        return MatrixToImageWriter.toBufferedImage(bm);
    }

    public static String texto(Integer codigoVenda) throws Exception {
        if (codigoVenda == null || codigoVenda <= 0) {
            return null;
        }
        String sql = "SELECT v.cod_fact, v.dataVenda, v.total_venda, v.total_iva, v.hash_cod, v.cliente_nif, v.assinatura,"
                + " d.abreviacao, c.nif, (SELECT nif FROM tb_dados_instituicao LIMIT 1)" // 8: abreviacao 9: nif cliente 10: nif emitente
                + " FROM tb_venda v LEFT JOIN documento d ON d.pk_documento = v.fk_documento"
                + " LEFT JOIN tb_cliente c ON c.codigo = v.codigo_cliente WHERE v.codigo = ?";
        // ligação partilhada da aplicação: só leitura, não se fecha
        try (PreparedStatement ps = BDConexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codigoVenda);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String nifCliente = limpo(rs.getString(6));
                if (nifCliente.isEmpty()) {
                    nifCliente = limpo(rs.getString(9));
                }
                if (nifCliente.isEmpty()) {
                    nifCliente = "999999999"; // consumidor final
                }
                // os 4 caracteres impressos na factura (assinatura); sem ela, o início do hash
                String hash = limpo(rs.getString(7));
                if (hash.isEmpty()) {
                    hash = limpo(rs.getString(5));
                }
                StringBuilder s = new StringBuilder();
                s.append("A:").append(limpo(rs.getString(10)));
                s.append("*B:").append(nifCliente);
                s.append("*D:").append(limpo(rs.getString(8)));
                s.append("*F:").append(rs.getTimestamp(2) == null ? "" : DATA.format(rs.getTimestamp(2)));
                s.append("*G:").append(limpo(rs.getString(1)));
                s.append("*N:").append(valor(rs.getBigDecimal(4)));
                s.append("*O:").append(valor(rs.getBigDecimal(3)));
                s.append("*Q:").append(hash.length() >= 4 ? hash.substring(0, 4) : hash);
                s.append("*R:Kitanda");
                return s.toString();
            }
        }
    }

    private static String valor(BigDecimal v) {
        return v == null ? "0.00" : v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private static String limpo(String s) {
        return s == null ? "" : s.trim();
    }
}
