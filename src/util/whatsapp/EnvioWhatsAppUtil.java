package util.whatsapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Envio de relatórios por WhatsApp (WhatsApp Business Cloud API da Meta).
 *
 * Estrutura equivalente a EnvioEmailUtil: inicio() e inicio(String file_name).
 *
 * Configuração em "whatsapp.properties" (pasta de trabalho do programa).
 *
 * MODO TESTE: envia o template "hello_world" (já existe na conta de teste). 
 * MODO PRODUÇÃO: faz upload do PDF e envia o template com documento no cabeçalho.
 *
 * Compatível com Java 8, sem bibliotecas externas.
 */
public class EnvioWhatsAppUtil {

    private static final String API = "https://graph.facebook.com/v20.0/";
    private static final String CAMINHO = "anexos/";
    private static final String CONFIG = "whatsapp.properties";

    private static String PARA;
    private static String PHONE_NUMBER_ID;
    private static String TOKEN;
    private static String TEMPLATE;
    private static String IDIOMA;
    private static String DESTINO;
    private static boolean MODO_TESTE;

    private static Vector<File> files = new Vector<>();

    // =====================================================================
    // Métodos públicos (mesma lógica do EnvioEmailUtil)
    // =====================================================================
    /**
     * Envia os dois relatórios padrão.
     */
    public static boolean inicio() {
        files.clear();
        adicionarFicheiros();
        return enviar();
    }

    /**
     * Envia apenas um ficheiro.
     */
    public static boolean inicio(String file_name) {
        files.clear();
        adicionarFicheiros(file_name);
        return enviar();
    }

    // =====================================================================
    // Fluxo principal
    // =====================================================================
    private static boolean enviar() {
        if (!existeConexaoInternet()) {
            System.err.println("WhatsApp: sem conexão com a internet.");
            return false;
        }

        try {
            carregarConfiguracao();
            setNumero();

            // ---- MODO TESTE: só confirma que a ligação Java <-> Meta funciona ----
            if (MODO_TESTE) {
                enviarHelloWorld();
                return true;
            }

            // ---- MODO PRODUÇÃO: um PDF por mensagem ----
            String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            boolean tudoOk = true;

            for (File f : files) {
                if (!f.exists()) {
                    System.err.println("WhatsApp: ficheiro não encontrado: " + f.getAbsolutePath());
                    tudoOk = false;
                    continue;
                }
                try {
                    String mediaId = uploadPdf(f);
                    enviarTemplateComDocumento(mediaId, f.getName(), data);
                    System.out.println("WhatsApp enviado: " + f.getName());
                } catch (IOException ex) {
                    System.err.println("WhatsApp: falha ao enviar " + f.getName() + " -> " + ex.getMessage());
                    tudoOk = false;
                }
            }
            return tudoOk;

        } catch (Exception e) {
            System.err.println("Erro WhatsApp: " + e.getMessage());
            return false;
        }
    }

    // =====================================================================
    // MODO TESTE
    // =====================================================================
    private static void enviarHelloWorld() throws IOException {
        String json = "{"
                + "\"messaging_product\":\"whatsapp\","
                + "\"to\":\"" + PARA + "\","
                + "\"type\":\"template\","
                + "\"template\":{\"name\":\"hello_world\",\"language\":{\"code\":\"en_US\"}}"
                + "}";
        String resposta = postJson(API + PHONE_NUMBER_ID + "/messages", json);
        System.out.println("WhatsApp (teste) resposta: " + resposta);
    }

    // =====================================================================
    // MODO PRODUÇÃO
    // =====================================================================
    // 1º passo: upload do PDF para a Meta
    private static String uploadPdf(File ficheiro) throws IOException {
        String boundary = "----DVML" + System.currentTimeMillis();
        URL url = new URL(API + PHONE_NUMBER_ID + "/media");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + TOKEN);
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setConnectTimeout(20000);
        con.setReadTimeout(60000);
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            escreverCampo(os, boundary, "messaging_product", "whatsapp");
            escreverCampo(os, boundary, "type", "application/pdf");

            os.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            os.write(("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + ficheiro.getName() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
            os.write("Content-Type: application/pdf\r\n\r\n".getBytes(StandardCharsets.UTF_8));

            try (FileInputStream in = new FileInputStream(ficheiro)) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) != -1) {
                    os.write(buf, 0, n);
                }
            }
            os.write(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }

        String resposta = lerResposta(con);
        Matcher m = Pattern.compile("\"id\"\\s*:\\s*\"(\\d+)\"").matcher(resposta);
        if (!m.find()) {
            throw new IOException("Upload falhou: " + resposta);
        }
        return m.group(1);
    }

    private static void escreverCampo(OutputStream os, String boundary, String nome, String valor) throws IOException {
        String parte = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + nome + "\"\r\n\r\n"
                + valor + "\r\n";
        os.write(parte.getBytes(StandardCharsets.UTF_8));
    }

    // 2º passo: enviar o template com o documento no cabeçalho
    private static void enviarTemplateComDocumento(String mediaId, String nomeFicheiro, String data) throws IOException {
        String json = "{"
                + "\"messaging_product\":\"whatsapp\","
                + "\"to\":\"" + PARA + "\","
                + "\"type\":\"template\","
                + "\"template\":{"
                + "\"name\":\"" + esc(TEMPLATE) + "\","
                + "\"language\":{\"code\":\"" + esc(IDIOMA) + "\"},"
                + "\"components\":["
                + "{\"type\":\"header\",\"parameters\":[{\"type\":\"document\",\"document\":{"
                + "\"id\":\"" + mediaId + "\",\"filename\":\"" + esc(nomeFicheiro) + "\"}}]},"
                + "{\"type\":\"body\",\"parameters\":[{\"type\":\"text\",\"text\":\"" + esc(data) + "\"}]}"
                + "]"
                + "}}";
        postJson(API + PHONE_NUMBER_ID + "/messages", json);
    }

    // =====================================================================
    // Auxiliares
    // =====================================================================
    private static String postJson(String endereco, String json) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(endereco).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + TOKEN);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setConnectTimeout(20000);
        con.setReadTimeout(30000);
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return lerResposta(con); // lança IOException se o código HTTP não for 2xx
    }

    private static String lerResposta(HttpURLConnection con) throws IOException {
        int codigo = con.getResponseCode();
        InputStream is = (codigo >= 200 && codigo < 300) ? con.getInputStream() : con.getErrorStream();
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    sb.append(linha);
                }
            }
        }
        if (codigo < 200 || codigo >= 300) {
            throw new IOException("HTTP " + codigo + ": " + sb);
        }
        return sb.toString();
    }

    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", " ").replace("\r", " ").replace("\t", " ");
    }

    private static void carregarConfiguracao() throws IOException {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG)) {
            p.load(in);
        }
        PHONE_NUMBER_ID = p.getProperty("whatsapp.phone.number.id", "").trim();
        TOKEN = p.getProperty("whatsapp.token", "").trim();
        TEMPLATE = p.getProperty("whatsapp.template", "relatorio_diario").trim();
        IDIOMA = p.getProperty("whatsapp.idioma", "pt_PT").trim();
        DESTINO = p.getProperty("whatsapp.destino", "").trim();
        MODO_TESTE = !"producao".equalsIgnoreCase(p.getProperty("whatsapp.modo", "teste").trim());

        if (PHONE_NUMBER_ID.isEmpty() || TOKEN.isEmpty()) {
            throw new IOException("whatsapp.phone.number.id ou whatsapp.token em falta no " + CONFIG);
        }
        if (!PHONE_NUMBER_ID.matches("\\d{10,20}")) {
            throw new IOException("whatsapp.phone.number.id inválido: [" + PHONE_NUMBER_ID
                    + "]. Deve conter apenas dígitos (ex.: 123456789012345), sem espaços, aspas ou texto.");
        }
        if (!TOKEN.matches("[A-Za-z0-9_\\-]+")) {
            throw new IOException("whatsapp.token inválido: contém espaços, aspas ou caracteres estranhos. "
                    + "Cole só o token, numa única linha. Tamanho lido: " + TOKEN.length());
        }
        System.out.println("WhatsApp config OK. Phone ID=" + PHONE_NUMBER_ID
                + " | token com " + TOKEN.length() + " caracteres | destino=" + DESTINO
                + " | modo=" + (MODO_TESTE ? "teste" : "producao"));
    }

    /**
     * Número que recebe a mensagem, lido do whatsapp.properties.
     * Formato final: 244XXXXXXXXX (sem + nem espaços).
     *
     * Para usar o telefone do gerente guardado na BD, altere aqui:
     * ler o valor da entidade TbDadosInstituicao (getter do telefone)
     * e passar por normalizarNumero(...).
     */
    private static void setNumero() throws IOException {
        PARA = normalizarNumero(DESTINO);
        if (PARA.isEmpty()) {
            throw new IOException("whatsapp.destino em falta no " + CONFIG);
        }
    }

    private static String normalizarNumero(String tel) {
        String n = tel == null ? "" : tel.replaceAll("[^0-9]", "");
        if (n.startsWith("00")) {
            n = n.substring(2);
        }
        if (n.length() == 9) {
            n = "244" + n;
        }
        return n;
    }

    private static void adicionarFicheiros() {
        files.add(new File(CAMINHO, "relatorio_diario_mes_caixa.pdf"));
        files.add(new File(CAMINHO, "relatorio_mensal.pdf"));
    }

    private static void adicionarFicheiros(String file_name) {
        files.add(new File(CAMINHO, file_name));
    }

    private static boolean existeConexaoInternet() {
        try {
            HttpURLConnection c = (HttpURLConnection) new URL("https://www.google.com").openConnection();
            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);
            c.connect();
            return c.getResponseCode() == 200;
        } catch (IOException e) {
            System.err.println("Não existe conexão com a internet: " + e.getLocalizedMessage());
            return false;
        }
    }

    // =====================================================================
    // Teste rápido (executar este ficheiro com Shift+F6 no NetBeans)
    // =====================================================================
    public static void main(String[] args) {
        boolean ok = inicio();
        System.out.println(ok ? "Concluído com sucesso." : "Concluído com falhas (veja as mensagens acima).");
    }
}