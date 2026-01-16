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
public class FEConfig
{

    private static final AmbienteFE AMBIENTE_ATUAL = AmbienteFE.HML; // <-- só muda aqui

    public static String getEndpointSolicitarSerie()
    {
        if ( AMBIENTE_ATUAL == AmbienteFE.HML )
        {
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
        }
        else
        {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/solicitarSerie";
        }
    }
    public static String getEndpointRegistrarFactura()
    {
        if ( AMBIENTE_ATUAL == AmbienteFE.HML )
        {
            return "https://sifphml.minfin.gov.ao/sigt/fe/v1/registarFactura";
        }
        else
        {
            return "https://sifp.minfin.gov.ao/sigt/fe/v1/registarFactura";
        }
    }

    public static String getUsername()
    {
        if ( AMBIENTE_ATUAL == AmbienteFE.HML )
        {
            return "ws.hml.kitanda";
        }
        else
        {
            return "ws.kitanda";
        }
    }

    public static String getPassword()
    {
        if ( AMBIENTE_ATUAL == AmbienteFE.HML )
        {
            return "mfn538052025";
        }
        else
        {
            return "mfn27942026";
        }
    }

    public static String getSoftwareValidationNumber()
    {
        if ( AMBIENTE_ATUAL == AmbienteFE.HML )
        {
            return "FE/196/AGT/2025";
        }
        else
        {
            return "FE/110/AGT/2025";
        }
    }

    public static String getSofwareName()
    {
        return "Kitanda";
    }

    public static String getProductionVersion()
    {
        return "1.1";
    }
}
