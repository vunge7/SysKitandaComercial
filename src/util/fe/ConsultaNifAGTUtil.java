package util.fe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Consulta um NIF (cliente ou empresa) na AGT. NÃO tem UI e NÃO toca na BD.
 *
 * Fonte principal: o Portal do Contribuinte (o mesmo que se usa no browser em
 * portaldocontribuinte.minfin.gov.ao/consultar-nif-do-contribuinte). É uma
 * página JSF/PrimeFaces: faz-se um GET para obter o cookie de sessão e o
 * ViewState e depois o POST ajax do botão "Pesquisar". Devolve Nome, Tipo,
 * Estado e Regime de IVA (não traz morada, telefone nem e-mail).
 *
 * Alternativa: o serviço JSON invoice.minfin.gov.ao/.../taxpayer/get/{nif},
 * usado só se o portal não responder (em 2026-11 esse serviço responde "no nif
 * message" para qualquer NIF).
 */
public class ConsultaNifAGTUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String PORTAL = "https://portaldocontribuinte.minfin.gov.ao";
    private static final String PORTAL_PAGINA = PORTAL + "/consultar-nif-do-contribuinte";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) KitandaHotel";

    public static class Contribuinte {

        private String nif;
        private String nome;
        private String tipo;
        private String morada;
        private String estado;
        private String regimeIva;
        private String telefone;
        private String email;
        private String mensagemErro;
        private String respostaBruta;

        public String getNif() {
            return nif;
        }

        public String getNome() {
            return nome;
        }

        /** Ex.: "COLECTIVO - Empresa" ou "SINGULAR - ..." (só pelo portal). */
        public String getTipo() {
            return tipo;
        }

        public String getMorada() {
            return morada;
        }

        public String getEstado() {
            return estado;
        }

        public String getRegimeIva() {
            return regimeIva;
        }

        public String getTelefone() {
            return telefone;
        }

        public String getEmail() {
            return email;
        }

        public String getMensagemErro() {
            return mensagemErro;
        }

        public String getRespostaBruta() {
            return respostaBruta;
        }

        public boolean isEncontrado() {
            return mensagemErro == null && nome != null;
        }

        /**
         * true = não se conseguiu falar com a AGT (sem internet, portal lento
         * ou em baixo): NÃO quer dizer que o NIF esteja errado.
         */
        public boolean isFalhaComunicacao() {
            return falhaComunicacao;
        }

        private boolean falhaComunicacao;
    }

    /**
     * Para verificar muitos NIFs seguidos (ex.: verificação em massa): abre a
     * sessão no portal uma vez e reutiliza-a, o que reduz o tempo para metade.
     * Não é thread-safe: use um Verificador por thread.
     */
    public static class Verificador {

        private SessaoPortal sessao;

        public Contribuinte consultar(String nif) {
            Contribuinte c = new Contribuinte();
            c.nif = nif == null ? "" : nif.trim().replaceAll("\\s+", "");
            if (c.nif.isEmpty()) {
                c.mensagemErro = "NIF vazio.";
                return c;
            }
            for (int tentativa = 1; tentativa <= 2; tentativa++) {
                try {
                    if (sessao == null) {
                        sessao = abrirSessao();
                    }
                    consultarNaSessao(sessao, c);
                    return c;
                } catch (Exception e) {
                    sessao = null; // sessão expirada ou falha: abre outra na próxima tentativa
                    c.nome = null;
                    if (isSemLigacao(e)) {
                        c.mensagemErro = MSG_SEM_INTERNET;
                        c.falhaComunicacao = true;
                        return c;
                    }
                    if (tentativa == 2) {
                        c.mensagemErro = e instanceof java.net.SocketTimeoutException
                                ? "O portal da AGT não respondeu a tempo."
                                : "Falha ao consultar a AGT: " + e.getMessage();
                        c.falhaComunicacao = true;
                    }
                }
            }
            return c;
        }
    }

    /** Cookie de sessão + dados do formulário JSF do portal. */
    private static class SessaoPortal {

        String cookies;
        String formId;
        String campoNif;
        String botao;
        String viewState;
        String acao;
        String render;
    }

    public static Contribuinte consultar(String nif) {

        Contribuinte c = new Contribuinte();
        c.nif = nif == null ? "" : nif.trim().replaceAll("\\s+", "");
        if (c.nif.isEmpty()) {
            c.mensagemErro = "Indique o NIF.";
            return c;
        }

        String erroPortal = null;
        for (int tentativa = 1; tentativa <= 2; tentativa++) {
            try {
                consultarPortal(c);
                return c; // encontrado ou "NIF não encontrado" - resposta definitiva
            } catch (Exception e) {
                if (isSemLigacao(e)) {
                    // sem internet não vale a pena repetir nem tentar o serviço alternativo
                    c.nome = null;
                    c.mensagemErro = MSG_SEM_INTERNET;
                    c.falhaComunicacao = true;
                    return c;
                }
                if (e instanceof java.net.SocketTimeoutException) {
                    // ligou ao portal mas ele demorou a responder: tenta mais uma vez
                    if (tentativa < 2) {
                        continue;
                    }
                    c.mensagemErro = "O portal da AGT está lento e não respondeu a tempo.\n"
                            + "Aguarde um pouco e tente pesquisar o NIF " + c.nif + " novamente.";
                    c.falhaComunicacao = true;
                    return c;
                }
                e.printStackTrace();
                erroPortal = e.getMessage();
                break;
            }
        }

        // O portal falhou (rede, mudança na página...): tenta o serviço JSON
        Contribuinte alt = new Contribuinte();
        alt.nif = c.nif;
        consultarServicoJson(alt);
        if (!alt.isEncontrado()) {
            alt.falhaComunicacao = true;
            alt.mensagemErro = "Não foi possível consultar o NIF " + c.nif + " na AGT.\n"
                    + "Portal do Contribuinte: " + erroPortal + "\n"
                    + "Serviço alternativo: " + alt.mensagemErro;
        }
        return alt;
    }

    private static final String MSG_SEM_INTERNET
            = "Sem ligação à Internet: não foi possível contactar a AGT para consultar o NIF.\n"
            + "Verifique a ligação e tente novamente.";

    /**
     * Falhas que significam "não há rede / não chega ao servidor": nome do
     * servidor não resolvido (sem DNS), ligação recusada, sem rota, ou tempo
     * esgotado ainda a estabelecer a ligação ("connect timed out").
     */
    private static boolean isSemLigacao(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof java.net.UnknownHostException
                    || t instanceof java.net.ConnectException
                    || t instanceof java.net.NoRouteToHostException) {
                return true;
            }
            if (t instanceof java.net.SocketTimeoutException
                    && t.getMessage() != null && t.getMessage().toLowerCase().contains("connect")) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Portal do Contribuinte (JSF / PrimeFaces)
    // ------------------------------------------------------------------
    /**
     * Preenche c com os dados do portal, ou c.mensagemErro se a AGT disser que
     * o NIF não existe. Lança exceção se não for possível obter uma resposta
     * reconhecível.
     */
    private static void consultarPortal(Contribuinte c) throws Exception {
        consultarNaSessao(abrirSessao(), c);
    }

    /** GET da página: cookie de sessão + ViewState + nomes dos campos. */
    private static SessaoPortal abrirSessao() throws Exception {
        HttpURLConnection get = abrir(PORTAL_PAGINA);
        get.setRequestMethod("GET");
        get.setRequestProperty("Accept", "text/html");
        int status = get.getResponseCode();
        String cookies = cookiesDe(get);
        String pagina = ler(get, status);
        get.disconnect();
        if (status != 200) {
            throw new Exception("página de consulta indisponível (HTTP " + status + ").");
        }

        int iCampo = pagina.indexOf(":txtNIFNumber\"");
        if (iCampo < 0) {
            throw new Exception("campo do NIF não encontrado na página do portal.");
        }
        int iForm = pagina.lastIndexOf("<form", iCampo);
        int fForm = pagina.indexOf("</form>", iCampo);
        if (iForm < 0 || fForm < 0) {
            throw new Exception("formulário de consulta não encontrado no portal.");
        }
        String form = pagina.substring(iForm, fForm);

        String formId = grupo(form, "<form[^>]*\\sid=\"([^\"]+)\"");
        String campoNif = grupo(form, "<input[^>]*\\sname=\"([^\"]*:txtNIFNumber)\"");
        String botao = grupo(form, "<button[^>]*\\sname=\"([^\"]+)\"");
        String viewState = grupo(form, "name=\"javax\\.faces\\.ViewState\"[^>]*\\svalue=\"([^\"]*)\"");
        String acao = grupo(form, "<form[^>]*\\saction=\"([^\"]+)\"");
        String render = grupo(form, "u:&quot;([^&]+)&quot;");
        if (formId == null || campoNif == null || botao == null || viewState == null) {
            throw new Exception("estrutura da página do portal desconhecida.");
        }
        if (acao == null) {
            acao = "/consultar-headNifId-do-contribuinte";
        }
        if (render == null) {
            render = "showpanelNIF";
        }
        SessaoPortal s = new SessaoPortal();
        s.cookies = cookies;
        s.formId = formId;
        s.campoNif = campoNif;
        s.botao = botao;
        s.viewState = decodificarHtml(viewState);
        s.acao = acao;
        s.render = render;
        return s;
    }

    /**
     * POST ajax do botão "Pesquisar" numa sessão já aberta. Preenche c, ou
     * c.mensagemErro se a AGT disser que o NIF não existe; lança exceção se a
     * resposta não for reconhecível (ex.: sessão expirada).
     */
    private static void consultarNaSessao(SessaoPortal s, Contribuinte c) throws Exception {
        String cookies = s.cookies;
        String acao = s.acao;
        StringBuilder corpo = new StringBuilder();
        param(corpo, "javax.faces.partial.ajax", "true");
        param(corpo, "javax.faces.source", s.botao);
        param(corpo, "javax.faces.partial.execute", s.formId);
        param(corpo, "javax.faces.partial.render", s.render);
        param(corpo, s.botao, s.botao);
        param(corpo, s.campoNif, c.nif);
        param(corpo, s.formId + "_SUBMIT", "1");
        param(corpo, "javax.faces.ViewState", s.viewState);
        byte[] bytes = corpo.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection post = abrir(acao.startsWith("http") ? acao : PORTAL + acao);
        post.setRequestMethod("POST");
        post.setDoOutput(true);
        post.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setRequestProperty("Faces-Request", "partial/ajax");
        post.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        post.setRequestProperty("Accept", "application/xml, text/xml, */*");
        post.setRequestProperty("Referer", PORTAL_PAGINA);
        if (!cookies.isEmpty()) {
            post.setRequestProperty("Cookie", cookies);
        }
        try (OutputStream out = post.getOutputStream()) {
            out.write(bytes);
        }
        int status = post.getResponseCode();
        String resposta = ler(post, status);
        post.disconnect();
        c.respostaBruta = resposta;
        if (status != 200) {
            throw new Exception("o portal respondeu HTTP " + status + ".");
        }

        // 3) Interpretar a resposta
        c.nome = valorDoCampo(resposta, "Nome");
        if (c.nome != null) {
            c.tipo = valorDoCampo(resposta, "Tipo");
            c.estado = valorDoCampo(resposta, "Estado");
            c.regimeIva = valorDoCampo(resposta, "Regime de IVA");
            return;
        }

        // Mensagens do Growl: msgs:[{summary:"",detail:"NIF não encontrado",severity:'warn'}]
        String detalhe = grupo(resposta, "detail:\"([^\"]+)\"");
        if (detalhe != null) {
            detalhe = decodificarHtml(detalhe);
            c.mensagemErro = detalhe.toLowerCase().contains("encontrad")
                    ? "NIF " + c.nif + " não encontrado na AGT."
                    : "AGT - NIF " + c.nif + ": " + detalhe;
            return;
        }
        throw new Exception("resposta do portal não reconhecida.");
    }

    /**
     * No resultado cada campo é:
     * &lt;label ...&gt; Nome: &lt;/label&gt; &lt;div ...&gt;&lt;label ...&gt;VALOR&lt;/label&gt;
     */
    private static String valorDoCampo(String html, String rotulo) {
        Pattern p = Pattern.compile(">\\s*" + Pattern.quote(rotulo) + "\\s*:\\s*</label>\\s*<div[^>]*>\\s*<label[^>]*>([^<]*)</label>");
        Matcher m = p.matcher(html);
        if (m.find()) {
            String v = decodificarHtml(m.group(1)).trim();
            return v.isEmpty() ? null : v;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Serviço JSON (alternativa)
    // ------------------------------------------------------------------
    private static void consultarServicoJson(Contribuinte c) {
        try {
            String endpoint = FEConfig.getEndpointConsultarNif(URLEncoder.encode(c.nif, "UTF-8"));
            HttpURLConnection conn = abrir(endpoint);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            int status = conn.getResponseCode();
            c.respostaBruta = ler(conn, status);
            conn.disconnect();

            String r = c.respostaBruta.trim();
            if (!r.startsWith("{")) {
                c.mensagemErro = "resposta inválida (HTTP " + status + ").";
                return;
            }
            JsonNode root = MAPPER.readTree(r);
            JsonNode data = root.path("data");
            if (!root.path("success").asBoolean(false) || !data.isObject()) {
                // "error" é um objecto: {"message": "...", "code": 10001}
                String erro = texto(root.path("error"), "message");
                if (erro == null) {
                    erro = texto(root, "error", "message");
                }
                c.mensagemErro = "NIF não encontrado" + (erro == null ? "." : " (" + erro + ").");
                return;
            }
            c.nome = texto(data, "companyName", "gsmc", "nameAbb");
            c.morada = texto(data, "nsrdz", "addressDbb");
            c.estado = texto(data, "hdzt");
            c.regimeIva = texto(data, "regimeIva");
            c.telefone = texto(data, "lxfs");
            c.email = texto(data, "email");
            if (c.nome == null) {
                c.mensagemErro = "NIF não encontrado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            c.mensagemErro = "falha de comunicação: " + e.getMessage();
        }
    }

    // ------------------------------------------------------------------
    // Utilitários
    // ------------------------------------------------------------------
    private static HttpURLConnection abrir(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        if (conn instanceof javax.net.ssl.HttpsURLConnection) {
            ((javax.net.ssl.HttpsURLConnection) conn).setSSLSocketFactory(socketFactoryAGT());
        }
        conn.setConnectTimeout(8000); // sem internet, desiste depressa
        conn.setReadTimeout(20000);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        return conn;
    }

    /**
     * Os servidores *.minfin.gov.ao enviam uma cadeia SSL incompleta: o
     * certificado é emitido pela "DigiCert Global G2 TLS RSA SHA256 2020 CA1",
     * mas o servidor envia outro intermediário (antigo e expirado). O browser
     * resolve isto sozinho; o Java dá "PKIX path building failed". Aqui confia-se
     * nas CAs normais do Java e, além delas, nesse intermediário da DigiCert
     * (válido até 2031-03-29). Só afecta as ligações desta classe.
     */
    private static final String DIGICERT_G2_TLS_RSA_2020_CA1
            = "-----BEGIN CERTIFICATE-----\n"
            + "MIIEyDCCA7CgAwIBAgIQDPW9BitWAvR6uFAsI8zwZjANBgkqhkiG9w0BAQsFADBh\n"
            + "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n"
            + "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBH\n"
            + "MjAeFw0yMTAzMzAwMDAwMDBaFw0zMTAzMjkyMzU5NTlaMFkxCzAJBgNVBAYTAlVT\n"
            + "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxMzAxBgNVBAMTKkRpZ2lDZXJ0IEdsb2Jh\n"
            + "bCBHMiBUTFMgUlNBIFNIQTI1NiAyMDIwIENBMTCCASIwDQYJKoZIhvcNAQEBBQAD\n"
            + "ggEPADCCAQoCggEBAMz3EGJPprtjb+2QUlbFbSd7ehJWivH0+dbn4Y+9lavyYEEV\n"
            + "cNsSAPonCrVXOFt9slGTcZUOakGUWzUb+nv6u8W+JDD+Vu/E832X4xT1FE3LpxDy\n"
            + "FuqrIvAxIhFhaZAmunjZlx/jfWardUSVc8is/+9dCopZQ+GssjoP80j812s3wWPc\n"
            + "3kbW20X+fSP9kOhRBx5Ro1/tSUZUfyyIxfQTnJcVPAPooTncaQwywa8WV0yUR0J8\n"
            + "osicfebUTVSvQpmowQTCd5zWSOTOEeAqgJnwQ3DPP3Zr0UxJqyRewg2C/Uaoq2yT\n"
            + "zGJSQnWS+Jr6Xl6ysGHlHx+5fwmY6D36g39HaaECAwEAAaOCAYIwggF+MBIGA1Ud\n"
            + "EwEB/wQIMAYBAf8CAQAwHQYDVR0OBBYEFHSFgMBmx9833s+9KTeqAx2+7c0XMB8G\n"
            + "A1UdIwQYMBaAFE4iVCAYlebjbuYP+vq5Eu0GF485MA4GA1UdDwEB/wQEAwIBhjAd\n"
            + "BgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwdgYIKwYBBQUHAQEEajBoMCQG\n"
            + "CCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wQAYIKwYBBQUHMAKG\n"
            + "NGh0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEdsb2JhbFJvb3RH\n"
            + "Mi5jcnQwQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL2NybDMuZGlnaWNlcnQuY29t\n"
            + "L0RpZ2lDZXJ0R2xvYmFsUm9vdEcyLmNybDA9BgNVHSAENjA0MAsGCWCGSAGG/WwC\n"
            + "ATAHBgVngQwBATAIBgZngQwBAgEwCAYGZ4EMAQICMAgGBmeBDAECAzANBgkqhkiG\n"
            + "9w0BAQsFAAOCAQEAkPFwyyiXaZd8dP3A+iZ7U6utzWX9upwGnIrXWkOH7U1MVl+t\n"
            + "wcW1BSAuWdH/SvWgKtiwla3JLko716f2b4gp/DA/JIS7w7d7kwcsr4drdjPtAFVS\n"
            + "slme5LnQ89/nD/7d+MS5EHKBCQRfz5eeLjJ1js+aWNJXMX43AYGyZm0pGrFmCW3R\n"
            + "bpD0ufovARTFXFZkAdl9h6g4U5+LXUZtXMYnhIHUfoyMo5tS58aI7Dd8KvvwVVo4\n"
            + "chDYABPPTHPbqjc1qCmBaZx2vN4Ye5DUys/vZwP9BFohFrH/6j/f3IL16/RZkiMN\n"
            + "JCqVJUzKoZHm1Lesh3Sz8W2jmdv51b2EQJ8HmA==\n"
            + "-----END CERTIFICATE-----\n";

    private static volatile javax.net.ssl.SSLSocketFactory socketFactory;

    private static javax.net.ssl.SSLSocketFactory socketFactoryAGT() throws Exception {
        if (socketFactory != null) {
            return socketFactory;
        }
        synchronized (ConsultaNifAGTUtil.class) {
            if (socketFactory == null) {
                // Confiança normal do Java (cacerts)
                javax.net.ssl.TrustManagerFactory tmfJava = javax.net.ssl.TrustManagerFactory
                        .getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
                tmfJava.init((java.security.KeyStore) null);
                final javax.net.ssl.X509TrustManager padrao = x509(tmfJava);

                // Intermediário DigiCert usado pela AGT
                java.security.KeyStore ks = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
                ks.load(null, null);
                ks.setCertificateEntry("digicert-g2-tls-rsa-2020-ca1",
                        java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(
                                new java.io.ByteArrayInputStream(DIGICERT_G2_TLS_RSA_2020_CA1.getBytes(StandardCharsets.US_ASCII))));
                javax.net.ssl.TrustManagerFactory tmfAgt = javax.net.ssl.TrustManagerFactory
                        .getInstance(javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm());
                tmfAgt.init(ks);
                final javax.net.ssl.X509TrustManager agt = x509(tmfAgt);

                javax.net.ssl.X509TrustManager combinado = new javax.net.ssl.X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                            throws java.security.cert.CertificateException {
                        padrao.checkClientTrusted(chain, authType);
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                            throws java.security.cert.CertificateException {
                        try {
                            padrao.checkServerTrusted(chain, authType);
                        } catch (java.security.cert.CertificateException e) {
                            // só o certificado do servidor: o intermediário enviado está errado
                            agt.checkServerTrusted(new java.security.cert.X509Certificate[]{chain[0]}, authType);
                        }
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return padrao.getAcceptedIssuers();
                    }
                };

                javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLS");
                ctx.init(null, new javax.net.ssl.TrustManager[]{combinado}, null);
                socketFactory = ctx.getSocketFactory();
            }
        }
        return socketFactory;
    }

    private static javax.net.ssl.X509TrustManager x509(javax.net.ssl.TrustManagerFactory tmf) {
        for (javax.net.ssl.TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof javax.net.ssl.X509TrustManager) {
                return (javax.net.ssl.X509TrustManager) tm;
            }
        }
        throw new IllegalStateException("Sem X509TrustManager disponível.");
    }

    private static String ler(HttpURLConnection conn, int status) throws Exception {
        InputStream in = status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream();
        if (in == null) {
            return "";
        }
        try (InputStream is = in) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            byte[] b = new byte[8192];
            int n;
            while ((n = is.read(b)) != -1) {
                buf.write(b, 0, n);
            }
            return new String(buf.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    /** "JSESSIONID=...; Path=/; HttpOnly" -> "JSESSIONID=..." (todos juntos). */
    private static String cookiesDe(HttpURLConnection conn) {
        List<String> pares = new ArrayList<>();
        for (Map.Entry<String, List<String>> h : conn.getHeaderFields().entrySet()) {
            if (h.getKey() != null && h.getKey().equalsIgnoreCase("Set-Cookie")) {
                for (String v : h.getValue()) {
                    String par = v.split(";", 2)[0].trim();
                    if (!par.isEmpty()) {
                        pares.add(par);
                    }
                }
            }
        }
        return String.join("; ", pares);
    }

    private static void param(StringBuilder sb, String nome, String valor) throws Exception {
        if (sb.length() > 0) {
            sb.append('&');
        }
        sb.append(URLEncoder.encode(nome, "UTF-8")).append('=').append(URLEncoder.encode(valor, "UTF-8"));
    }

    private static String grupo(String texto, String regex) {
        Matcher m = Pattern.compile(regex).matcher(texto);
        return m.find() ? m.group(1) : null;
    }

    private static String decodificarHtml(String s) {
        if (s == null) {
            return null;
        }
        Matcher m = Pattern.compile("&#(x?)([0-9a-fA-F]+);").matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            int cp = Integer.parseInt(m.group(2), m.group(1).isEmpty() ? 10 : 16);
            m.appendReplacement(sb, Matcher.quoteReplacement(new String(Character.toChars(cp))));
        }
        m.appendTail(sb);
        return sb.toString()
                .replace("&quot;", "\"").replace("&apos;", "'")
                .replace("&lt;", "<").replace("&gt;", ">")
                .replace("&nbsp;", " ").replace("&amp;", "&");
    }

    /**
     * Devolve o primeiro campo existente e não vazio entre os nomes dados.
     */
    private static String texto(JsonNode node, String... campos) {
        for (String campo : campos) {
            JsonNode n = node.get(campo);
            if (n != null && !n.isNull() && !n.isContainerNode() && !n.asText().trim().isEmpty()) {
                return n.asText().trim();
            }
        }
        return null;
    }
}
