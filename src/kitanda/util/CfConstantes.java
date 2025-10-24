/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.util;


import java.sql.Connection;
import java.util.Locale;

/**
 *
 * @author tagif
 */
public class CfConstantes
{

    //AAAA-MM-DDThh:mm:ss
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YY_HHMMSS = "dd-MM-yy HH:mm:ss";
    public static final String DATE_FORMAT_EEE_DD_DE_MMM_DE_YY = "EEE dd 'de' MMM 'de' yy";
    public static final String DATE_FORMAT_EEE_DD_DE_MMM_DE_YYYY = "EEE dd 'de' MMM 'de' yyyy";
    public static final String EEE_DD_MM_YY = "EEE dd/MM/yy";
    public static final String EEE_DD_MM_YYYY = "EEE dd/MM/yyyy";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String YYYYMMDD_HHMMSS = "yyyy_MM_dd_HH_mm_ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DD_MM_YYYY_SEPERADOR_HIFFEN = "dd-MM-yyyy";
    public static final String MMM_YYYY = "MMM 'de' yyyy";
    public static final String MMM = "MMM";
    public static final String TIME_FORMAT_hh_mm_ss = "HH:mm:ss";
    public static final String TIME_FORMAT_hh_mm = "HH:mm";
    public static final Locale PORTUGAL_LOCALE = new Locale ( "pt", "pt" );
    public static final int CODIGO_RECURSOS_EXTRA = 2;

    public static enum PainelDados
    {

        d0,
        d0_1,
        d0_2,
        d1,
        d1_1,
        d1_2,
        d2,
        d2_1,
        d2_2,
        d3,
        d3_1,
        d3_2,
        d4,
        d4_1,
        d4_2,
        d5,
        d5_1,
        d5_2,
        d6,
        d6_1,
        d6_2,
    }

    public static enum Operacao
    {

        CRIAR,
        CRIAR_2,
        ALTERAR,
        ALTERAR_2,
        ELIMINAR,
        LISTAR,
        ULTIMO_IR_PARA,
        PRIMEIRO_IR_PARA,
        PROXIMO_IR_PARA,
        ANTERIOR_IR_PARA,

    }

    public static enum Recurso
    {

        NORMAL,
        EXTRA
    }

    public static final String REL_PATH_IMAGENS = "/winmarketpro2/salas_conf/imagens/";
}
