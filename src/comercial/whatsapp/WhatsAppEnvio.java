package comercial.whatsapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Envio pela API do WhatsApp. Fornecedores (escolhidos na configuração):
 *
 * - SIMULACAO: não envia; gera os PDFs na pasta anexos/whatsapp e regista
 *   no histórico. Para testar horários e relatórios sem conta.
 * - META: WhatsApp Business Cloud API (oficial, graph.facebook.com). Mensagens
 *   iniciadas pelo hotel só chegam com MODELOS (templates) aprovados pela Meta:
 *   . meta.modelo_documento: cabeçalho DOCUMENTO + corpo com {{1}};
 *   . meta.modelo_texto: corpo com {{1}} (para o "Resumo do dia").
 *   Sem modelo, a mensagem vai livre: só chega se o responsável tiver escrito
 *   para o número do hotel nas últimas 24 horas (regra da Meta).
 * - ULTRAMSG: gateway UltraMsg (api.ultramsg.com), ligado a um número normal
 *   de WhatsApp por QR code. Sem modelos.
 */
public final class WhatsAppEnvio {

    public static final String SIMULACAO = "SIMULACAO";
    public static final String META = "META";
    public static final String ULTRAMSG = "ULTRAMSG";

    public static final String C_FORNECEDOR = "fornecedor";
    public static final String C_INDICATIVO = "indicativo";
    public static final String C_AUTOMATICO = "automatico";
    public static final String C_TOLERANCIA = "tolerancia_min";
    public static final String C_META_TOKEN = "meta.token";
    public static final String C_META_TELEFONE = "meta.phone_id";
    public static final String C_META_MODELO_DOC = "meta.modelo_documento";
    public static final String C_META_MODELO_TEXTO = "meta.modelo_texto";
    public static final String C_META_IDIOMA = "meta.idioma";
    public static final String C_ULTRA_INSTANCIA = "ultramsg.instancia";
    public static final String C_ULTRA_TOKEN = "ultramsg.token";
    /** Número que envia (o do hotel ou, por omissão, o da DVML) — editável no formulário. */
    public static final String C_REMETENTE = "remetente";
    /** Remetente por omissão para todos os clientes: DVML Comercial (+244 921 734 126). */
    public static final String REMETENTE_PADRAO = "244921734126";

    private static final String META_VERSAO = "v21.0";

    private final Map<String, String> cfg;

    public WhatsAppEnvio(Map<String, String> cfg) {
        this.cfg = cfg;
    }

    public static WhatsAppEnvio daBase(Connection cn) {
        return new WhatsAppEnvio(WhatsAppDados.lerConfig(cn));
    }

    public String get(String chave, String omissao) {
        String v = cfg.get(chave);
        return v == null || v.trim().isEmpty() ? omissao : v.trim();
    }

    public String fornecedor() {
        return get(C_FORNECEDOR, SIMULACAO);
    }

    public String indicativo() {
        return get(C_INDICATIVO, "244");
    }

    /**
     * Número remetente (só dígitos, com indicativo). Na Meta e na UltraMsg é a
     * conta configurada que define o número que envia: este campo tem de ser o
     * mesmo número dessa conta (serve para conferir e para o envio sem API).
     */
    public String remetente() {
        return get(C_REMETENTE, REMETENTE_PADRAO);
    }

    public boolean automaticoLigado() {
        return !"0".equals(get(C_AUTOMATICO, "1"));
    }

    public int toleranciaMin() {
        try {
            return Math.max(5, Math.min(720, Integer.parseInt(get(C_TOLERANCIA, "120"))));
        } catch (NumberFormatException e) {
            return 120;
        }
    }

    public boolean simulacao() {
        return SIMULACAO.equals(fornecedor());
    }

    public static String rotuloFornecedor(String f) {
        return META.equals(f) ? "WhatsApp Business Cloud API (Meta)"
                : ULTRAMSG.equals(f) ? "UltraMsg (gateway)" : "Simulação (não envia)";
    }

    /** O que falta configurar; null se estiver pronto. */
    public String problemaConfiguracao() {
        switch (fornecedor()) {
            case META:
                if (get(C_META_TOKEN, null) == null || get(C_META_TELEFONE, null) == null) {
                    return "Falta o token de acesso ou o ID do número de telefone da Meta.";
                }
                return null;
            case ULTRAMSG:
                if (get(C_ULTRA_INSTANCIA, null) == null || get(C_ULTRA_TOKEN, null) == null) {
                    return "Falta a instância ou o token da UltraMsg.";
                }
                return null;
            default:
                return null;
        }
    }

    // =====================================================================
    // ENVIO
    // =====================================================================
    /**
     * Envia o relatório já gerado. Devolve o detalhe para o histórico (id da
     * mensagem); lança excepção com a explicação se falhar.
     */
    public String enviar(String numero, RelatorioWhatsApp.Resultado r) throws Exception {
        String problema = problemaConfiguracao();
        if (problema != null) {
            throw new IllegalStateException(problema);
        }
        switch (fornecedor()) {
            case META:
                return r.pdf == null ? metaTexto(numero, r.texto) : metaDocumento(numero, r.pdf, r.texto);
            case ULTRAMSG:
                return r.pdf == null ? ultraTexto(numero, r.texto) : ultraDocumento(numero, r.pdf, r.texto);
            default:
                return "simulação: " + (r.pdf == null ? "só mensagem" : r.pdf.getName());
        }
    }

    /**
     * Imagem com legenda (ex.: postal de aniversário, PNG). Na Meta, fora da
     * janela de 24 h da conversa, a mensagem livre é recusada (só modelos
     * aprovados); na UltraMsg segue sempre. Devolve o detalhe para o histórico.
     */
    public String enviarImagem(String numero, File imagem, String legenda) throws Exception {
        String problema = problemaConfiguracao();
        if (problema != null) {
            throw new IllegalStateException(problema);
        }
        switch (fornecedor()) {
            case META: {
                String media = campo(metaUpload(imagem, "image/png"), "id");
                String json = "{\"messaging_product\":\"whatsapp\",\"to\":\"" + numero + "\",\"type\":\"image\","
                        + "\"image\":{\"id\":" + js(media) + ",\"caption\":" + js(legenda) + "}}";
                return "id " + campo(metaPost(metaUrl("messages"), json), "id");
            }
            case ULTRAMSG: {
                String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(imagem.toPath()));
                return ultraPost(ultraUrl("image"), "token=" + url(get(C_ULTRA_TOKEN, "")) + "&to=" + url("+" + numero)
                        + "&image=" + url(base64) + "&caption=" + url(legenda));
            }
            default:
                return "simulação: " + imagem.getName();
        }
    }

    // ---------------------------------------------------------------- Meta
    private String metaUrl(String recurso) {
        return "https://graph.facebook.com/" + META_VERSAO + "/" + get(C_META_TELEFONE, "") + "/" + recurso;
    }

    private String metaTexto(String numero, String texto) throws Exception {
        String modelo = get(C_META_MODELO_TEXTO, null);
        String json;
        if (modelo != null) {
            json = "{\"messaging_product\":\"whatsapp\",\"to\":\"" + numero + "\",\"type\":\"template\",\"template\":{"
                    + "\"name\":" + js(modelo) + ",\"language\":{\"code\":" + js(get(C_META_IDIOMA, "pt_PT")) + "},"
                    + "\"components\":[{\"type\":\"body\",\"parameters\":[{\"type\":\"text\",\"text\":" + js(parametroModelo(texto)) + "}]}]}}";
        } else {
            json = "{\"messaging_product\":\"whatsapp\",\"to\":\"" + numero + "\",\"type\":\"text\","
                    + "\"text\":{\"preview_url\":false,\"body\":" + js(texto) + "}}";
        }
        return "id " + campo(metaPost(metaUrl("messages"), json), "id");
    }

    private String metaDocumento(String numero, File pdf, String legenda) throws Exception {
        String media = campo(metaUpload(pdf), "id");
        String modelo = get(C_META_MODELO_DOC, null);
        String doc = "{\"id\":" + js(media) + ",\"filename\":" + js(pdf.getName());
        String json;
        if (modelo != null) {
            json = "{\"messaging_product\":\"whatsapp\",\"to\":\"" + numero + "\",\"type\":\"template\",\"template\":{"
                    + "\"name\":" + js(modelo) + ",\"language\":{\"code\":" + js(get(C_META_IDIOMA, "pt_PT")) + "},"
                    + "\"components\":[{\"type\":\"header\",\"parameters\":[{\"type\":\"document\",\"document\":" + doc + "}}]},"
                    + "{\"type\":\"body\",\"parameters\":[{\"type\":\"text\",\"text\":" + js(parametroModelo(legenda)) + "}]}]}}";
        } else {
            json = "{\"messaging_product\":\"whatsapp\",\"to\":\"" + numero + "\",\"type\":\"document\","
                    + "\"document\":" + doc + ",\"caption\":" + js(legenda) + "}}";
        }
        return "id " + campo(metaPost(metaUrl("messages"), json), "id");
    }

    /** A Meta recusa parâmetros de modelo com mudanças de linha ou mais de 4 espaços seguidos. */
    private static String parametroModelo(String s) {
        return s.replaceAll("\\s*\\n+\\s*", " · ").replaceAll("[\\t ]{2,}", " ").trim();
    }

    private String metaPost(String url, String json) throws Exception {
        HttpURLConnection c = abrir(url);
        c.setRequestProperty("Authorization", "Bearer " + get(C_META_TOKEN, ""));
        c.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        try (OutputStream os = c.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return resposta(c);
    }

    private String metaUpload(File pdf) throws Exception {
        return metaUpload(pdf, "application/pdf");
    }

    private String metaUpload(File pdf, String tipo) throws Exception {
        String limite = "----KitandaComercial" + System.nanoTime();
        HttpURLConnection c = abrir(metaUrl("media"));
        c.setRequestProperty("Authorization", "Bearer " + get(C_META_TOKEN, ""));
        c.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + limite);
        try (OutputStream os = c.getOutputStream()) {
            parte(os, limite, "messaging_product", "whatsapp");
            parte(os, limite, "type", tipo);
            os.write(("--" + limite + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"" + pdf.getName()
                    + "\"\r\nContent-Type: " + tipo + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            Files.copy(pdf.toPath(), os);
            os.write(("\r\n--" + limite + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }
        return resposta(c);
    }

    private static void parte(OutputStream os, String limite, String nome, String valor) throws Exception {
        os.write(("--" + limite + "\r\nContent-Disposition: form-data; name=\"" + nome + "\"\r\n\r\n" + valor + "\r\n")
                .getBytes(StandardCharsets.UTF_8));
    }

    // ------------------------------------------------------------- UltraMsg
    private String ultraUrl(String recurso) {
        return "https://api.ultramsg.com/" + get(C_ULTRA_INSTANCIA, "") + "/messages/" + recurso;
    }

    private String ultraTexto(String numero, String texto) throws Exception {
        return ultraPost(ultraUrl("chat"), "token=" + url(get(C_ULTRA_TOKEN, "")) + "&to=" + url("+" + numero)
                + "&body=" + url(texto));
    }

    private String ultraDocumento(String numero, File pdf, String legenda) throws Exception {
        String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(pdf.toPath()));
        return ultraPost(ultraUrl("document"), "token=" + url(get(C_ULTRA_TOKEN, "")) + "&to=" + url("+" + numero)
                + "&filename=" + url(pdf.getName()) + "&document=" + url(base64) + "&caption=" + url(legenda));
    }

    private String ultraPost(String endereco, String corpo) throws Exception {
        HttpURLConnection c = abrir(endereco);
        c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try (OutputStream os = c.getOutputStream()) {
            os.write(corpo.getBytes(StandardCharsets.UTF_8));
        }
        String r = resposta(c);
        if (r.contains("\"error\"")) {
            throw new IllegalStateException("UltraMsg: " + r);
        }
        String id = campo(r, "id");
        return "id " + id;
    }

    // ------------------------------------------------------------- comuns
    private static HttpURLConnection abrir(String endereco) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(endereco).openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setConnectTimeout(15000);
        c.setReadTimeout(90000);
        c.setRequestProperty("User-Agent", "SysKitandaComercial");
        return c;
    }

    private static String resposta(HttpURLConnection c) throws Exception {
        int codigo = c.getResponseCode();
        InputStream in = codigo >= 400 ? c.getErrorStream() : c.getInputStream();
        String corpo = "";
        if (in != null) {
            try (InputStream i = in; ByteArrayOutputStream b = new ByteArrayOutputStream()) {
                byte[] buf = new byte[8192];
                int n;
                while ((n = i.read(buf)) > 0) {
                    b.write(buf, 0, n);
                }
                corpo = new String(b.toByteArray(), StandardCharsets.UTF_8);
            }
        }
        if (codigo >= 400) {
            String msg = campo(corpo, "message");
            throw new IllegalStateException("HTTP " + codigo + (msg.isEmpty() ? ": " + WhatsAppDados.cortar(corpo, 300) : ": " + msg));
        }
        return corpo;
    }

    /** Primeiro valor de "campo":"..." ou "campo":123 na resposta JSON (chega para id e message). */
    static String campo(String json, String nome) {
        Matcher m = Pattern.compile("\"" + Pattern.quote(nome) + "\"\\s*:\\s*(\"((?:\\\\.|[^\"\\\\])*)\"|[-\\w.]+)").matcher(json == null ? "" : json);
        if (!m.find()) {
            return "";
        }
        return m.group(2) != null ? m.group(2).replace("\\\"", "\"").replace("\\/", "/") : m.group(1);
    }

    static String js(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char ch : (s == null ? "" : s).toCharArray()) {
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (ch < 0x20) {
                        sb.append(String.format("\\u%04x", (int) ch));
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.append('"').toString();
    }

    private static String url(String s) throws Exception {
        return URLEncoder.encode(s == null ? "" : s, "UTF-8");
    }

    /** Ligação "clique para conversar" (envio manual sem API): abre o WhatsApp com o texto escrito. */
    public static String ligacaoManual(String numero, String texto) {
        try {
            return "https://wa.me/" + numero + "?text=" + URLEncoder.encode(texto == null ? "" : texto, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            return "https://wa.me/" + numero;
        }
    }
}
