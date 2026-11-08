package util.fe;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 * Verificação rápida se o servidor da AGT está acessível (modo contingência).
 * O resultado fica em cache alguns segundos para não atrasar cada venda.
 *
 * @author Engº Domingos Dala Vunge
 */
public final class AgtConectividade {

    private static final int TIMEOUT_TESTE_MS = 3000;
    private static final long CACHE_DISPONIVEL_MS = 30_000;
    private static final long CACHE_INDISPONIVEL_MS = 30_000;

    private static volatile long ultimaVerificacao = 0;
    private static volatile boolean ultimoResultado = false;

    private AgtConectividade() {
    }

    /**
     * true se for possível abrir ligação TCP ao servidor da AGT.
     */
    public static boolean agtDisponivel() {
        long agora = System.currentTimeMillis();
        long validade = ultimoResultado ? CACHE_DISPONIVEL_MS : CACHE_INDISPONIVEL_MS;
        if (ultimaVerificacao > 0 && agora - ultimaVerificacao < validade) {
            return ultimoResultado;
        }
        boolean ok = testarLigacao();
        ultimoResultado = ok;
        ultimaVerificacao = System.currentTimeMillis();
        return ok;
    }

    /**
     * Força nova verificação na próxima chamada.
     */
    public static void invalidarCache() {
        ultimaVerificacao = 0;
    }

    /**
     * Chamado quando um envio falha por erro de rede.
     */
    public static void marcarIndisponivel() {
        ultimoResultado = false;
        ultimaVerificacao = System.currentTimeMillis();
    }

    /**
     * Chamado quando um envio teve resposta da AGT.
     */
    public static void marcarDisponivel() {
        ultimoResultado = true;
        ultimaVerificacao = System.currentTimeMillis();
    }

    /**
     * true se a excepção (ou a causa) for de rede: sem internet, DNS, timeout,
     * ligação recusada/terminada.
     */
    public static boolean isErroDeRede(Throwable e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof java.net.UnknownHostException
                    || t instanceof java.net.SocketTimeoutException
                    || t instanceof java.net.ConnectException
                    || t instanceof java.net.NoRouteToHostException
                    || t instanceof java.net.SocketException
                    || t instanceof javax.net.ssl.SSLException
                    || t instanceof java.io.InterruptedIOException
                    || t instanceof util.fe.http.HttpClientUtil.ServidorIndisponivelException) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static boolean testarLigacao() {
        try {
            URL url = new URL(FEConfig.getEndpointRegistrarFactura());
            int porta = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();
            try (Socket s = new Socket()) {
                s.connect(new InetSocketAddress(url.getHost(), porta), TIMEOUT_TESTE_MS);
                return true;
            }
        } catch (Exception e) {
            System.err.println("AGT indisponível: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
}
