package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Validação do NIF de clientes conforme a AGT (Administração Geral
 * Tributária de Angola).
 * <p>
 * A validação é feita em duas etapas:
 * <ol>
 * <li><b>Formato</b> (sem rede): para clientes angolanos o NIF tem de ser
 * <ul>
 * <li>{@code 999999999} &mdash; NIF genérico de "Consumidor final";</li>
 * <li>10 dígitos &mdash; pessoa colectiva / NIF atribuído pela AGT
 * (ex.: {@code 5417033855});</li>
 * <li>9 dígitos + 2 letras + 3 dígitos &mdash; pessoa singular, número do
 * Bilhete de Identidade (ex.: {@code 000067259ME013}).</li>
 * </ul>
 * Para clientes estrangeiros aceita-se o número fiscal do país de origem
 * (3 a 20 letras/dígitos), que não pode ser verificado na AGT.</li>
 * <li><b>Registo na AGT</b> (com rede): consulta o serviço público de
 * contribuintes da AGT e devolve o nome, morada, telefone e a situação
 * (activo/inactivo) do contribuinte.</li>
 * </ol>
 */
public final class ValidadorNifAGT {

    /** NIF genérico definido pela AGT para o consumidor final. */
    public static final String NIF_CONSUMIDOR_FINAL = "999999999";

    /** Serviço público de consulta de contribuintes da AGT. */
    private static final String URL_CONSULTA
            = "https://invoice.minfin.gov.ao/commonServer/common/taxpayer/get/";

    private static final int TEMPO_LIGACAO_MS = 6000;
    private static final int TEMPO_LEITURA_MS = 8000;

    private static final Pattern COLECTIVO = Pattern.compile("\\d{10}");
    private static final Pattern SINGULAR = Pattern.compile("\\d{9}[A-Z]{2}\\d{3}");
    private static final Pattern ESTRANGEIRO = Pattern.compile("[A-Z0-9]{3,20}");

    private static final ObjectMapper JSON = new ObjectMapper();

    /** Respostas definitivas da AGT já obtidas nesta sessão. */
    private static final Map<String, Resultado> CACHE = new ConcurrentHashMap<>();

    public enum Tipo {
        CONSUMIDOR_FINAL("Consumidor final"),
        COLECTIVO("Pessoa colectiva"),
        SINGULAR("Pessoa singular (BI)"),
        ESTRANGEIRO("Contribuinte estrangeiro"),
        DESCONHECIDO("—");

        private final String descricao;

        Tipo(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum Estado {
        /** Formato correcto e NIF registado na AGT (ou consumidor final). */
        VALIDO,
        /** Cliente estrangeiro: formato aceite, não verificável na AGT. */
        ESTRANGEIRO,
        /** O NIF não respeita o formato exigido. */
        FORMATO_INVALIDO,
        /** Formato correcto, mas a AGT não tem nenhum contribuinte com este NIF. */
        NAO_REGISTADO,
        /** Formato correcto, mas não foi possível contactar a AGT. */
        SEM_LIGACAO
    }

    /** Resultado de uma validação. */
    public static final class Resultado {

        private final Estado estado;
        private final Tipo tipo;
        private final String nif;
        private final String mensagem;
        private String nome;
        private String morada;
        private String telefone;
        private String email;
        private String situacao;
        private String regimeIva;

        Resultado(Estado estado, Tipo tipo, String nif, String mensagem) {
            this.estado = estado;
            this.tipo = tipo;
            this.nif = nif;
            this.mensagem = mensagem;
        }

        public Estado getEstado() {
            return estado;
        }

        public Tipo getTipo() {
            return tipo;
        }

        /** NIF normalizado (maiúsculas, sem espaços nem separadores). */
        public String getNif() {
            return nif;
        }

        public String getMensagem() {
            return mensagem;
        }

        /** Nome do contribuinte registado na AGT (ou null). */
        public String getNome() {
            return nome;
        }

        public String getMorada() {
            return morada;
        }

        public String getTelefone() {
            return telefone;
        }

        /** E-mail registado na AGT, apenas se for um endereço válido. */
        public String getEmail() {
            return email;
        }

        /** Situação na AGT, tal como devolvida ("Active", "inactive"...). */
        public String getSituacao() {
            return situacao;
        }

        public String getRegimeIva() {
            return regimeIva;
        }

        /** O contribuinte está activo na AGT? (true se a AGT não indicar o contrário) */
        public boolean isActivo() {
            return situacao == null || situacao.trim().isEmpty()
                    || "ACTIVE".equalsIgnoreCase(situacao.trim())
                    || "ACTIVO".equalsIgnoreCase(situacao.trim());
        }

        /** O NIF foi confirmado (consumidor final ou registado na AGT). */
        public boolean isConfirmado() {
            return estado == Estado.VALIDO;
        }

        /** Nunca se pode guardar o cliente com este resultado. */
        public boolean isBloqueante() {
            return estado == Estado.FORMATO_INVALIDO || estado == Estado.NAO_REGISTADO;
        }
    }

    private ValidadorNifAGT() {
    }

    // ------------------------------------------------------------------
    // Formato
    // ------------------------------------------------------------------
    /**
     * Normaliza o NIF: maiúsculas, sem espaços, pontos, hífens ou barras.
     */
    public static String normalizar(String nif) {
        if (nif == null) {
            return "";
        }
        return nif.toUpperCase(Locale.ROOT).replaceAll("[\\s.\\-/]", "");
    }

    /** O cliente é angolano? (país vazio conta como Angola) */
    public static boolean ehAngola(String paisIso) {
        return paisIso == null || paisIso.trim().isEmpty() || "AO".equalsIgnoreCase(paisIso.trim());
    }

    /** Identifica o tipo de NIF a partir do formato. */
    public static Tipo tipoDe(String nif, String paisIso) {
        String n = normalizar(nif);
        if (NIF_CONSUMIDOR_FINAL.equals(n)) {
            return Tipo.CONSUMIDOR_FINAL;
        }
        if (!ehAngola(paisIso)) {
            return ESTRANGEIRO.matcher(n).matches() ? Tipo.ESTRANGEIRO : Tipo.DESCONHECIDO;
        }
        if (COLECTIVO.matcher(n).matches()) {
            return Tipo.COLECTIVO;
        }
        if (SINGULAR.matcher(n).matches()) {
            return Tipo.SINGULAR;
        }
        return Tipo.DESCONHECIDO;
    }

    /**
     * Verifica apenas o formato (sem rede).
     *
     * @return null se o formato estiver correcto; caso contrário a mensagem
     * de erro a mostrar ao utilizador.
     */
    public static String validarFormato(String nif, String paisIso) {
        String n = normalizar(nif);
        if (n.isEmpty()) {
            return "Introduza o NIF do cliente.\nSe o cliente não tiver NIF, use "
                    + NIF_CONSUMIDOR_FINAL + " (Consumidor final).";
        }
        if (tipoDe(n, paisIso) != Tipo.DESCONHECIDO) {
            return null;
        }
        if (!ehAngola(paisIso)) {
            return "O NIF de um cliente estrangeiro deve ter entre 3 e 20 letras ou dígitos.";
        }
        if (n.matches("\\d+")) {
            return "O NIF \"" + n + "\" tem " + n.length() + " dígitos.\n"
                    + "Para a AGT, o NIF de uma empresa tem 10 dígitos (ex.: 5417033855).";
        }
        return "O NIF \"" + n + "\" não está no formato exigido pela AGT:\n"
                + " • Empresa / NIF da AGT: 10 dígitos (ex.: 5417033855)\n"
                + " • Pessoa singular: nº do BI, 9 dígitos + 2 letras + 3 dígitos (ex.: 000067259ME013)\n"
                + " • Consumidor final: " + NIF_CONSUMIDOR_FINAL;
    }

    // ------------------------------------------------------------------
    // Validação completa (formato + AGT)
    // ------------------------------------------------------------------
    /**
     * Valida o NIF: formato e, para clientes angolanos, registo na AGT.
     * <b>Faz um pedido de rede</b>: não chamar na thread do Swing sem
     * necessidade.
     */
    public static Resultado validar(String nif, String paisIso) {
        String n = normalizar(nif);
        String erro = validarFormato(n, paisIso);
        Tipo tipo = tipoDe(n, paisIso);
        if (erro != null) {
            return new Resultado(Estado.FORMATO_INVALIDO, tipo, n, erro);
        }
        if (tipo == Tipo.CONSUMIDOR_FINAL) {
            Resultado r = new Resultado(Estado.VALIDO, tipo, n,
                    "NIF genérico da AGT para Consumidor final.");
            r.nome = "Consumidor final";
            return r;
        }
        if (tipo == Tipo.ESTRANGEIRO) {
            return new Resultado(Estado.ESTRANGEIRO, tipo, n,
                    "Cliente estrangeiro: o NIF não é verificável na AGT.");
        }
        return consultarAGT(n, tipo);
    }

    private static Resultado consultarAGT(String nif, Tipo tipo) {
        Resultado emCache = CACHE.get(nif);
        if (emCache != null) {
            return emCache;
        }

        HttpURLConnection ligacao = null;
        try {
            URL url = new URL(URL_CONSULTA + URLEncoder.encode(nif, "UTF-8"));
            ligacao = (HttpURLConnection) url.openConnection();
            ligacao.setConnectTimeout(TEMPO_LIGACAO_MS);
            ligacao.setReadTimeout(TEMPO_LEITURA_MS);
            ligacao.setRequestProperty("Accept", "application/json");
            ligacao.setRequestProperty("User-Agent", "DVML-Comercial");

            if (ligacao.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return semLigacao(nif, tipo, "a AGT respondeu com o código " + ligacao.getResponseCode());
            }

            JsonNode raiz;
            try (InputStream in = ligacao.getInputStream()) {
                raiz = JSON.readTree(in);
            }

            JsonNode dados = raiz.path("data");
            Resultado resultado;
            if (raiz.path("success").asBoolean(false) && dados.isObject()) {
                resultado = new Resultado(Estado.VALIDO, tipo, nif, "NIF registado na AGT.");
                resultado.nome = primeiroPreenchido(texto(dados, "gsmc"),
                        texto(dados, "nameAbb"), texto(dados, "companyName"));
                resultado.morada = primeiroPreenchido(texto(dados, "nsrdz"), texto(dados, "addressDbb"));
                resultado.telefone = texto(dados, "lxfs");
                String email = texto(dados, "email");
                resultado.email = email != null && email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+") ? email : null;
                resultado.situacao = texto(dados, "hdzt");
                resultado.regimeIva = texto(dados, "regimeIva");
            } else if (raiz.path("error").path("code").asInt() == 10001) {
                resultado = new Resultado(Estado.NAO_REGISTADO, tipo, nif,
                        "O NIF " + nif + " não está registado na AGT.\n"
                        + "Confirme o número junto do cliente (cartão de contribuinte ou BI).");
            } else {
                String detalhe = raiz.path("error").path("message").asText("");
                return semLigacao(nif, tipo, detalhe.isEmpty() ? "resposta inesperada da AGT" : detalhe);
            }
            CACHE.put(nif, resultado);
            return resultado;

        } catch (IOException | RuntimeException e) {
            return semLigacao(nif, tipo, e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage()));
        } finally {
            if (ligacao != null) {
                ligacao.disconnect();
            }
        }
    }

    private static Resultado semLigacao(String nif, Tipo tipo, String causa) {
        return new Resultado(Estado.SEM_LIGACAO, tipo, nif,
                "Não foi possível contactar a AGT para confirmar o NIF (" + causa + ").\n"
                + "O formato do NIF está correcto.");
    }

    private static String texto(JsonNode no, String campo) {
        JsonNode valor = no.get(campo);
        if (valor == null || valor.isNull()) {
            return null;
        }
        String s = valor.asText().trim();
        return s.isEmpty() ? null : s;
    }

    private static String primeiroPreenchido(String... valores) {
        for (String v : valores) {
            if (v != null && !v.isEmpty()) {
                return v;
            }
        }
        return null;
    }
}
