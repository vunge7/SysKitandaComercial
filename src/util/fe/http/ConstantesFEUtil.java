/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.http;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 12/jan/2026
 * @lastModified 12/jan/2026
 */
public class ConstantesFEUtil
{

    /**
     * ENDPOINST
     */
    public static String ENDPOINT_SOLCITACAO_SERIE = "https://sifp.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
    public static String ENDPOINT_SOLCITACAO_HML_SERIE = "https://sifphml.minfin.gov.ao/sigt/fe/v1/solicitarSerie";

    /**
     * BASIC AUTH
     */
    private static String HML_USERNAME = "ws.hml.kitanda";
    private static String HML_PASSWORD = "mfn538052025";

    private static String PROD_USERNAME = "ws.kitanda";
    private static String PROD_PASSWORD = "mfn27942026";

    public static String USERNAME = HML_USERNAME;
    public static String PASSWORD = HML_PASSWORD;

    /**
     * SOFTWARE INFORMATION
     */
    public static String SOFTWARE_NAME = "Kitanda";
    public static String SOFTWARE_VERSION = "1.1";
    public static String SOFTWARE_NUMBER = "FE/110/AGT/2025";
    public static String SOFTWARE_HML_NUMBER = "FE/196/AGT/2025";

}
