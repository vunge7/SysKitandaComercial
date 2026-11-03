/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 13/jan/2026
 * @lastModified 13/jan/2026
 */
public class FEConfig {

    // O ambiente é lido da BD (tb_dados_instituicao.ambiente) quando o LoginVisao arranca.
    // Valor por defeito PROD, igual ao default da coluna.
    private static volatile AmbienteFE AMBIENTE_ATUAL = AmbienteFE.PROD;

    // Passa a true quando o ambiente foi carregado da BD
    private static volatile boolean ambienteCarregado = false;

    // Origens (classe.método:linha) que já foram avisadas, para não repetir o aviso
    private static final java.util.Set<String> AVISOS_EMITIDOS
            = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /**
     * Define o ambiente a partir do texto guardado na BD ("HML" ou "PROD").
     * Qualquer outro valor (ou null) assume PROD.
     */
    public static void carregarAmbiente(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            System.err.println("[FEConfig] AVISO: campo 'ambiente' vazio na BD. A assumir PROD.");
        }

        if (valor != null && "HML".equalsIgnoreCase(valor.trim())) {
            AMBIENTE_ATUAL = AmbienteFE.HML;
        } else {
            AMBIENTE_ATUAL = AmbienteFE.PROD;
        }
        ambienteCarregado = true;
        System.out.println("AMBIENTE FE: " + AMBIENTE_ATUAL);
    }

    public static AmbienteFE getAmbienteAtual() {
        avisarSeNaoCarregado();
        return AMBIENTE_ATUAL;
    }

    public static boolean isHomologacao() {
        return ambienteEmHml();
    }

    /**
     * Usado por todos os métodos get...() deste ficheiro.
     */
    private static boolean ambienteEmHml() {
        avisarSeNaoCarregado();
        return AmbienteFE.HML.equals(AMBIENTE_ATUAL);
    }

    /**
     * Se o ambiente ainda não foi carregado da BD, escreve na consola (a
     * vermelho no NetBeans) QUEM está a usar o FEConfig. Cada origem é avisada
     * só uma vez.
     */
    private static void avisarSeNaoCarregado() {
        if (ambienteCarregado) {
            return;
        }

        String origem = "desconhecida";
        for (StackTraceElement el : Thread.currentThread().getStackTrace()) {
            String classe = el.getClassName();
            if (!classe.equals(FEConfig.class.getName()) && !classe.startsWith("java.lang.Thread")) {
                origem = classe + "." + el.getMethodName() + " (linha " + el.getLineNumber() + ")";
                break;
            }
        }

        if (AVISOS_EMITIDOS.add(origem)) {
            System.err.println("[FEConfig] AVISO: ambiente usado ANTES de ser carregado da BD "
                    + "(a assumir " + AMBIENTE_ATUAL + "). Origem: " + origem);
        }
    }

    public static String getEndpointSolicitarSerie() {
        if (ambienteEmHml()) {
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
        } else {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
        }
    }

    public static String getEndpointRegistrarFactura() {
        if (ambienteEmHml()) {
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/registarFactura";
        } else {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/registarFactura";
        }
    }

    public static String getEndpointObterEstado() {
        if (ambienteEmHml()) {
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/obterEstado";
        } else {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/obterEstado";
        }
    }

    public static String getEndpointListarFacturas() {
        if (ambienteEmHml()) {
            // A documentação indica ".../fe/ws/v1/...", mas esse é o serviço SOAP
            // (só aceita text/xml). O serviço JSON segue o padrão dos restantes.
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/listarFacturas";
        } else {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/listarFacturas";
        }
    }

    /**
     * Serviço público de consulta de contribuintes (NIF) da AGT. GET sem
     * autenticação; é o mesmo em homologação e em produção.
     */
    public static String getEndpointConsultarNif(String nif) {
        return "https://invoice.minfin.gov.ao/commonServer/common/taxpayer/get/" + nif;
    }

    /**
     * Chave privada do cliente (jwsSignature / jwsDocumentSignature). É
     * diferente em homologação e em produção.
     */
    public static String getChaveClientePrivada() {
        if (ambienteEmHml()) {
            return "Chaves/chave_cliente/hml/ChavePrivada2048Cliente.pem";
        } else {
            return "Chaves/chave_cliente/prod/ChavePrivada2048Cliente.pem";
        }
    }

    public static String getUsername() {
        if (ambienteEmHml()) {
            return "ws.hml.kitanda";
//            return "ws.kitanda";
        } else {
            return "ws.kitanda";
        }
    }

    public static String getPassword() {
        if (ambienteEmHml()) {
            return "mfn538052025";
//            return "mfn27942026";
        } else {
            return "mfn27942026";
        }
    }

    public static String getSoftwareValidationNumber() {
        if (ambienteEmHml()) {
            return "FE/196/AGT/2025";
//            return "FE/110/AGT/2025";
        } else {
            return "FE/110/AGT/2025";
        }
    }

    public static String getSofwareName() {
        return "Kitanda";
    }

    public static String getProductionVersion() {
        return "1.1";
    }
}