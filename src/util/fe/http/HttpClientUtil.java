/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.http;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 23/dez/2025
 * @lastModified 23/dez/2025
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.fe.BasicAuthUtil;
import util.fe.JsonUtil;
import util.fe.payloads.PayloadFactory;

public class HttpClientUtil {

    public static final int TIMEOUT_PADRAO_MS = 20000;

    /** HTTP 5xx: a AGT não está a atender (modo contingência). */
    public static class ServidorIndisponivelException extends java.io.IOException {

        public ServidorIndisponivelException(String msg) {
            super(msg);
        }
    }

    /**
     * Timeouts por thread: na emissão de uma venda usam-se timeouts curtos
     * (modo contingência) sem mudar as chamadas internas da PayloadFactory.
     */
    private static final ThreadLocal<int[]> TIMEOUTS = new ThreadLocal<>();

    public static void setTimeoutsThread(int connectMs, int readMs) {
        TIMEOUTS.set(new int[]{connectMs, readMs});
    }

    public static void limparTimeoutsThread() {
        TIMEOUTS.remove();
    }

    public static boolean temTimeoutsThread() {
        return TIMEOUTS.get() != null;
    }

    public static String postJson(String endpoint, String json, String basicAuthBase64) throws Exception {
        int[] t = TIMEOUTS.get();
        if (t != null) {
            return postJson(endpoint, json, basicAuthBase64, t[0], t[1]);
        }
        return postJson(endpoint, json, basicAuthBase64, TIMEOUT_PADRAO_MS, TIMEOUT_PADRAO_MS);
    }

    public static String postJson(String endpoint, String json, String basicAuthBase64,
            int connectTimeoutMs, int readTimeoutMs) throws Exception {

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // MÉTODO
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // HEADERS (IGUAL AO POSTMAN)
        conn.setRequestProperty("Authorization", basicAuthBase64);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // Timeout em milissegundos
        conn.setConnectTimeout(connectTimeoutMs); // timeout de conexão
        conn.setReadTimeout(readTimeoutMs);       // timeout de leitura/resposta

        // BODY
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        // STATUS
        int status = conn.getResponseCode();

        // 5xx = servidor da AGT em baixo/sobrecarregado: tratado como sem ligação
        if (status >= 500) {
            conn.disconnect();
            throw new ServidorIndisponivelException("AGT respondeu HTTP " + status);
        }

        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            java.io.InputStream err = conn.getErrorStream();
            if (err == null) {
                conn.disconnect();
                return "";
            }
            br = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));
        }

        StringBuilder resp = new StringBuilder();
        String linha;
        while ((linha = br.readLine()) != null) {
            resp.append(linha);
        }

        br.close();
        conn.disconnect();

        return resp.toString();
    }

    public static void main(String[] args) {

        String endpoint = "https://sifphml.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
        Map<String, Object> jsonPayload = PayloadFactory.criarPayloadCriarSerie("5000413178", "A", "2025", "GF");

//        System.out.println( "JSON PAYLOAD " + JsonUtil.toJson( jsonPayload ) );
        String basicAuth = BasicAuthUtil.gerarAuthorizationHeader(ConstantesFEUtil.USERNAME, ConstantesFEUtil.PASSWORD);

        System.out.println(basicAuth);

        try {
            String resposta = HttpClientUtil.postJson(
                    endpoint,
                    JsonUtil.toJson(jsonPayload), // o JSON que já tens
                    basicAuth // SOMENTE o base64 (sem "Basic ")
            );

            System.out.println(resposta);

        } catch (Exception ex) {
            Logger.getLogger(HttpClientUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
