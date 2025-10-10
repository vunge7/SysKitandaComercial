/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.VendasController;
import static dao.UtilDao.emf;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbUsuario;
import entity.TbVenda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DVML
{

    // <editor-fold defaultstate="collapsed" desc="Chave Privada">
    public static String CHAVE_PRIVADA = "MIICXQIBAAKBgQDlLGwx5Kv4lWstpIGKtrQ+q4eFxQScbAmbaq4dCyQpUdi6h4BE\n"
            + "gkV+xFBDUUFnGcgiYOPYehB2R4TUsHJPYHeC/E0vSCByF3VYLkyiqypXgPNeJIiJ\n"
            + "BUkUL/GXsOpS3AtXiQCYxCjtZjIEYGcQ6TFDYV8vCPufGbZqn/7jaWmYewIDAQAB\n"
            + "AoGAKz0XaM55N9yRLTplipNwLIbWegGzHgQwR9d35FjuyLD9kCvyonplvEYz0o+p\n"
            + "JFez7/CR7I0TQA28XRixaJKN2ZhRRnv/f8GqL+4mj8ZKaoK5RYiIg4kwRR0Ej88y\n"
            + "NT29i/1hSMFeCAOw/3ZEnAyJWS1Yh7K1PwxoHiP3lXlb43ECQQD/2M4r0ccamFbk\n"
            + "/8439OvZeVsM6t3Vw527n/qGo+PCQNQeG1DzMrRlfUlUkUgVUf3lt5Spr1vyBlVF\n"
            + "LVZT+89ZAkEA5U+H8eLOqBqbSxyiKf8uY61E1IkGmSJpEupD4CKbkrmlP655rYA+\n"
            + "42j+Y7hrk1n80dmPVRJMGy3t5FZq7Igf8wJBAIwy+V/H1+x5+HwKta2yOB/3QW7F\n"
            + "sYR3apMranSnkKKdNd+9plFuwq4uxqVh63dLc++S9xM5NhBdahK7cmgZQBkCQCeo\n"
            + "N3SHqwG7iDvXOdxVe2pVnt+6yt9U6iMRTKfIlGstm/yFahmtYjLmB4irbj9Pgr+w\n"
            + "Bm/J+EuDB+9DWr6JVC0CQQCCOTOYWbQxIGQJL1I/j7LJlqEbDwjK4QcRIM7CEedg\n"
            + "QA7gtsdjQoeRcLExXzA6tmcgy9LR8ZSNjSyS1URt+ZSF";
    // </editor-fold> 
    // <editor-fold defaultstate="collapsed" desc="Chave Publica">
    public static String CHAVE_PUBLICA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDlLGwx5Kv4lWstpIGKtrQ+q4eF\n"
            + "xQScbAmbaq4dCyQpUdi6h4BEgkV+xFBDUUFnGcgiYOPYehB2R4TUsHJPYHeC/E0v\n"
            + "SCByF3VYLkyiqypXgPNeJIiJBUkUL/GXsOpS3AtXiQCYxCjtZjIEYGcQ6TFDYV8v\n"
            + "CPufGbZqn/7jaWmYewIDAQAB";
    // </editor-fold> 

    public static final String PERSISTENCE_UNIT;
    public static final String MYSQL_DATABASE_NAME;
    public static final String MYSQL_USER_NAME;
    public static final String MYSQL_PASSWORD;
    public static final String MYSQL_HOST;
    public static final String MYSQL_JDBC_URL;
    public static final String MYSQL_JDBC_DRIVER;
    public static Integer MOEDA_NACIONAL = 1;
    public static double LIMITE_PERCENTAGEM = 100.00;
    public static final String NUMBER_NIF_GENERICO = "999999999";
    public static final double AGT_IMPOSTO = 14.00;
    public static Integer BUSCA_DIRECTORIOS_NIVEIS_A_PERCORRER = 5;
    public static String RECTIFICACAO = "Rectificação";
    public static Object CHAVE_MESTRE = "1234dvml4321";
    public static String ANULACAO = "Anulação";
    public static String _NAO_INCLUIR = null;
    public static Integer ARMAZEM_DEFAUTL = 1;
    public static Integer ARMAZEM_LOJA = 2;
    public final static String PRIMEIRO_TURNO = "1-Turno";
    public final static String SEGUNDO_TURNO = "2-Turno";
    public final static String SUBSIDIO_FERIA = "Feria";
    public final static String SUBSIDIO_NATAL = "Natal";
    public final static String TIPO_MENSAGEM_AVISO = "Aviso";
    public final static String TIPO_MENSAGEM_ERRO = "Erro";
    public final static String TIPO_MENSAGEM_ALERTA = "Alerta";
    public final static String TIPO_MENSAGEM_INFOR = "Info";
    public final static String LIMITE_DATA_INICIO = "2020-01-01";
    public final static String LIMITE_DATA_FIM = "2021-03-31";
    public final static String CONTRATRO_DETERMINADO = "Determinado";
    public final static String CONTRATRO_INDETERMINADO = "Indeterminado";
    public static String AGENDA_FUNCIONARIO_ANEXO_PATH = "funcionario_anexos/";
    public static final String PATH_DOCS_UTIL = "DocsUtil/";
    public final static String REMOVER = "R.";
    public final static String JUSTIFICAR = "J.";
    public final static String VISUALIZAR = "V.";
    public final static String ADICIONAR = "A.";
    public final static String DESALOJAR = "Des.";
    public final static String SEXO_MASCULINO = "Masculino";
    public final static String SEXO_FEMENINO = "Feminino";
    public static final int ID_CONSUMIDOR_FINAL = 1;
    public static String _CLIENTE_CONSUMIDOR_FINAL = "Consumidor Final";
    public static String _FORNECEDOR_DIFERENCIADO = "Diferenciado";
    //public static final String PATH = "/Users/mac/Chaves/";
    public static final String PATH = "Chaves/";
    public static String FORMA_PAGAMENTO_TRANSFERENCIA = "Transferência";
    public static Integer COD_SERVICO = 1;
    public static Integer COD_PRODUTO = 2;
    public static String FORMA_PAGAMENTO_DEPOSITO = "Depósito";
    public static String FORMA_PAGAMENTO_CASH = "Cash";
    public static String FORMA_PAGAMENTO_CARTAO = "Cartão";
    public final static String ENVIAR_TICKET = "Enviar Ticket";
    public final static String ENVIAR_SALA = "Enviar Sala";
    public final static String NAO_ENVIAR_TICKET = "Nao Enviar Ticket";
    public static int SOLTEIRO = 1;
    public static int CASADO = 2;
    public static int DIVORCIADO = 3;
    public static int VIUVO = 4;
    public static String SELECCIONE = "--Seleccione--";
    public static int MASTER_TABLE_REMUNERACAO = 1;
    public static int MASTER_TABLE_DESCONTO = 2;
    public static int MASTER_TABLE_ABONOS = 3;
    public static double HORA_UTIL_TRABALHO = 8;
    public static int ID_SUBSIDIO_ALIMENTACAO = 1;
    public static int ID_SUBSIDIO_TRANSPORTE = 2;
    public static int ID_USUARIO_FORNECEDOR_SOFTWARE = 15;
    public static int JANEIRO = 1;
    public static int FEVEREIRO = 2;
    public static int MARCO = 3;
    public static int ABRIL = 4;
    public static int MAIO = 5;
    public static int JUNHO = 6;
    public static int JULHO = 7;
    public static int AGOSTO = 8;
    public static int SETEMBRO = 9;
    public static int OUTUBRO = 10;
    public static int NOVEMBRO = 11;
    public static int DEZEMBRO = 12;
    public static Integer MESA_BALCAO = 16;
    public static Integer LUGAR_BALCAO = 1;
    public static Integer COD_TIPO_USUARIO_ADMINISTRADOR = 1;
    public static Integer COD_TIPO_USUARIO_SUB_ADMINISTRADOR = 2;
    public static String CLIENTE_SINGULAR = "Singular";
    public static String CLIENTE_ENTIDADE = "Entidade";
    public static String QUARTO_DISPONIVEL = "Disponível";
    public static String QUARTO_OUCUPADO = "Oucupado";
    public static String QUARTO_MAL_ESTADO = "Mal Estado";
    public static String QUARTO_RESERVADO = "Mal Estado";
    public static String QUARTO_EM_REPARACAO = "Em Reparação";
    public static String QUARTO_BLOQUEADO = "Bloqueado";
    public static String TIPOLOGIA_SOLTEIRO = "Solteiro";
    public static String TIPOLOGIA_MEIA_OCUPACAO = "Duplo Meia Ocupação";
    public static String TIPOLOGIA_DUPLO = "Duplo";
    public static String TIPOLOGIA_CASAL = "Casal";
    public static String TIPOLOGIA_CASAL_LIGACAO = "Casal/Ligação";
    public static String TIPOLOGIA_CAMA_EXTTRA = "Casal Extra";
    public static String ESTADO_ALOJAMENTO_ACTIVO = "Activo";
    public static String ESTADO_ALOJAMENTO_DESACTIVO = "Desactivo";
    public static String ESTADO_ALOJAMENTO_CANCELAMENTO = "Cancelamento";
    public static String ESTADO_DIARIA_PAGO = "Pago";
    public static String ESTADO_DIARIA_EM_PROCESSO = "Em Processo";
    public static String ESTADO_DIARIA_NAO_PAGO = "Não Pago";
    public static String ESTADO_CONSUMO_PAGO = "Pago";
    public static String ESTADO_CONSUMO_NAO_PAGO = "Não Pago";
    public static String TIPO_IMPRESSORA = "POS-80C";
    public static int COD_GRUPO_DIFERENCIADO = 1;
    public static int COD_GRUPO_INGREDIENTES = 2;
    public static int COD_GRUPO_REFEICAO = 3;
    public static int COD_GRUPO_FRIGOBAR = 4;
    public static int COD_GRUPO_BAR = 5;
    public static Integer CODIGO_DIARIA = 3;
//    public static Object CHAVE_MESTRE = "1234dvml4321";
    public static int NUMERARIO = 1;
    public static int FECHO_ALOJAMENTO = 1;
    public static int VENDA_RESTAURANTE = 5;
    public static int VENDA_LAVANDARIA = 10;
    public static int JANELA_ASSOCIACAO = 12;
    public static int EMISSAO_RECIBOS = 6;
    public static int VENDA_PONTUAL = 3;
    public static int VENDA_OFICINA = 13;
    public static int VENDA_EXPRESSO = 14;
    public static int CONVERSAO_PROFORMA_FACTURA_RECIBO = 8;
    public static int CONVERSAO_GUIA_TRANSPORTE = 9;
    public static int VENDA_POS = 7;
    public static int ALOJAMENTO = 4;
    public static int ALOJAMENTO_PAINEL_0 = 0;
    public static int ALOJAMENTO_PAINEL_2 = 2;
    public static Integer COD_USUARIO_MASTER = 1;

    public static String ESTADO_DOCUMENTO_NENHUM = "Nenhum";
    public static String ESTADO_DOCUMENTO_ANULADO = "Anulado";
    public static String ESTADO_DOCUMENTO_FR = "FR";
    public static String ESTADO_DOCUMENTO_FT = "FT";
    public static Object REMOVER_LINHA = "Remover";
    public static int NUMERO_TECLADO_VENDA_POS = 1;
    public static int NUMERO_TECLADO_FORMA_PAGAMENTO = 2;
    public static int NUMERO_TECLADO_ABERTURA_CAIXA = 3;
    public static int NUMERO_TECLADO_FECHO_CAIXA = 4;
    public static int NUMERO_TECLADO_LUGAR = 5;
    public static int COD_FORMA_PAGAMENTO_CASH = 1;
    public static int COD_FORMA_PAGAMENTO_CARTAO = 2;
    public final static String CAMINHO_IMAGEM = "imagem/";
    public final static String CAMINHO_DOCUMENTO = "documentos/";
    public final static String FORMATO_IMAGEM_ANEXO = "jpg";
    public static String REGIME_EXCLUSAO = "Regime de Exclusao";
    public static String REGIME_SIMPLIFICADO = "Regime Simplificado";
    public static String REGIME_GERAL = "Regime Geral";
    public final static String CAMINHO_SCRIP_TO_UPDATE = "../SCRIPT_TO_UPDATE/";
    public final static String FILE_TO_UPDATE = "script_to_update.sql";
    public final static int ID_GRUPO_LAVAR = 1;
    public final static int ID_GRUPO_ENGOMAR = 2;
    public static int COD_CATEGORIA_URGENCIA = 20;
    public static String TAXA_EXPRESSO_100 = "Taxa Expresso 100";
    public static String TAXA_URGENCIA_50 = "Taxa de Urgência 50";
    public static String BD = "kitanda_db";
    public static int SEGUNDA_SEXTA_HORA = 11;
    public static int SEGUNDA_SEXTA_MINUTO = 56;
    public final static String STATUS_CONVERTIDO = "Convertido";
    public final static String STATUS_CONVERTIDO_PAGO = "Pago";
    private List<TbProduto> listaProdutosAleatoris = new ArrayList<>();

    static
    {
        PERSISTENCE_UNIT = "SGCMINIMERCADOPU";
        System.err.println( "emf: " + emf );
        MYSQL_USER_NAME = "";
        MYSQL_PASSWORD = "";
        MYSQL_HOST = "";
        MYSQL_DATABASE_NAME = "";
        MYSQL_JDBC_URL = "";
        MYSQL_JDBC_DRIVER = "";

    }

    public static String DVML_COMECIAL_LDA = "DVML-COMERCIAL, Lda";
    public static String DVML_COMERCIAL = "DVML-COMERCIAL, Lda";
    public static String MODULO_GESTAO_COMERCIAL = "1";
    public static String MODULO_RH_TESOURARIA = "2";
    public static String select_armazem = "RETALHO";
    public static String CAMINHO_REPORT = "relatorios/";
    public static String NAME_SOFTWARE = "Kitanda";
    public static String NAME_COMPANY = "DVML-COMERCIAL, LDA";
    public static String VERSION_SOFTWARE = "1.1";
    public final static String CAMINHO_SAFT = "../FICHEIRO_FAFT/";
    public final static String CAMINHO_BK = "../BD_BACKUP/";
    public final static String FICHEIRO_SAFT = "SAFT_FACTURACAO.xml";
    public final static String FICHEIRO_SAFT_COMPRA = "SAFT_COMPRA.xml";
//    public final static String FICHEIRO_SAFT = "SAF-T.xml";
    public final static String MOEDA_KWANZA = "AOA";
    public final static String MOEDA_DOLAR = "USD";
    public final static String MOEDA_EURO = "EUR";
    public final static String ISENTO_COM_BASE_NO_ARTIGO = "Isento com base no artigo 12º na(s) alínea(s) ";
    public final static String SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL = "ORIGINAL";
//    public final static String SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL = "2ª via em conformidade com a original.";
    public final static Integer ID_CAMBIO_NACIONAL = 1;

    public static int COD_BANCO_CAIXA = 1;
//    public static int GROSSO = 214748364;
    public static int GROSSO = 214748364;
    public static double QTD_EXISTENTE_PADRAO_SERVICO = 1000000;
    public static double QTD_DEFAULT = 6;
    public static int JANELA_VENDA = 1;
    public static int JANELA_SAIDA = 16;
    public static int JANELA_VENDA_POS = 13;
    public static int JANELA_VENDA_OFICINA = 12;
    public static int JANELA_COMPRA = 4;
    public static int JANELA_CARDEX = 18;
    public static int JANELA_TRANSFERENCIA = 6;
    public static int ACERTO_STOCK = 7;
    public static int IMPRIMIR_PRECOS = 8;
    public static int JANELA_ENTRADA = 2;
    public static int JANELA_ENTRADA_STOCK = 17;
    public static int JANELA_PRODUTO = 3;
    public static int JANELA_ESTORNO = 9;
    public static int JANELA_ALOJAMENTO_RETAURANTE = 5;
    public static int JANELA_RETAURANTE = 10;
    public static int JANELA_VENDA_EXPRESSO = 15;
    public static int JANELA_RECOLHA = 11;
    public static int JANELA_FICHA_TECNICA = 14;
    public static final int CASAS_DECIMAIS = 2;
    public static final int DOC_FACTURA_RECIBO_FR = 1;
    public static final int DOC_FACTURA_FT = 2;
    public static final int DOC_FACTURA_PROFORMA_PP = 3;
    public static final int DOC_NOTA_DEBITO_ND = 4;
    public static final int DOC_NOTA_CREDITO_NC = 5;
    public static final int DOC_NOTA_CREDITO_NC_COMPRAS = 12;
    public static final int DOC_RECIBO_RC = 6;
    public static final int DOC_GUIA_TRANSPORTE_GT = 7;
    public static final int DOC_ENTREGA_LAVANDARIA = 14;
    public static final int DOC_RECIBO_SO = 8;
    public static final int DOC_COMPRA_CO = 9;
    public static final int DOC_NOTA_ENCOMENDA_NE = 10;
    public static final int DOC_NOTA_LEVANTAMENTO = 11;
    public static final int DOC_FACTURA_CONSULTA_MESA = 13;
    public static final int DOC_VISTORIA = 15;
    public static final int DOC_DESPACHO = 16;

    public static final String DOC_PADRAO = "Factura-Recibo";
    public static final String STATUS_FACTURADO_PROCESSADO = "Processado";
    public static final String STATUS_FACTURADO_ESPERA = "Espera";
    public static final String MOEDA_PADRAO = "Kwanza";
    public static final String FORMULARIO_PEDIDO_RESTAURANTE = "Formulario Pedido Restaurante";
    public static final String FORMULARIO_VENDA_EXPRESSO = "Formulario Venda Expresso";
    public static final String FORMULARIO_RECOLHA_LAVANDARIA = "Formulario Recolha Lavandaria";
    public static final String UNI_ARMAZEM = "Uni_armazem";

    public static enum ESTADO_NOTA
    {
        RETIFICADO,
        TOTALMENTE_RETIFICADO,
        ANULADO
    }

    public static Abreviacao getAbreviacao( int doc )
    {

        switch (doc)
        {
            case 1:
                return Abreviacao.FR_A4;
            case 2:
                return Abreviacao.FA;
            case 3:
                return Abreviacao.PP;
            case 4:
                return Abreviacao.ND;
            case 5:
                return Abreviacao.NC;
            case 6:
                return Abreviacao.RC;
            case 7:
                return Abreviacao.GT;
            case 8:
                return Abreviacao.SO;
            case 9:
                return Abreviacao.CO;
            case 10:
                return Abreviacao.NE;
            case 11:
                return Abreviacao.NL;
            case 12:
                return Abreviacao.NCCO;
            case 13:
                return Abreviacao.CM;
            case 14:
                return Abreviacao.EL;
            case 15:
                return Abreviacao.VI;
            case 16:
                return Abreviacao.DE;
            default:
                return null;

        }
    }

    public static enum Abreviacao
    {
        FR_A4,
        FR_A6,
        FR_A6_O,
        FA,
        PP,
        ND,
        NC,
        RC,
        GT,
        SO,
        NE,
        NL,
        EL,
        CO,
        NCCO,
        CM,
        FR_A4_Duplicado,
        FR_SA7,
        FT_A4_Duplicado,
        FR_S_A6,
        FR_S_A6_O,
        FR_A6_Com_Virgula,
        VI,
        DE
    }

    public static String AGT_SAFT_AUDIT_FILE_VERSION;//Versão a utilizar do schema xml
    public static String AGT_SAFT_COMPANY_ID;//Registro comercial (NIF)
    public static String AGT_SAFT_TAX_REGISTRATION_NUMBER;//NIF Angolano sem espaço e sem qualquer prefixo do pais
    public static String AGT_SAFT_TAX_ACCOUNTING_BASIS;// ---
    public static String AGT_SAFT_COMPANY_NAME;// ---
    public static String AGT_SAFT_BUSINESS_NAME;// ---
    public static String AGT_SAFT_ADDRESS_DETAIL;// ---
    public static String AGT_SAFT_CITY;// ---
    public static String AGT_SAFT_COUNTRY;// ---
    public static String AGT_SAFT_FISCAL_YEAR;// ---
    public static String AGT_SAFT_START_DATE;// ---
    public static String AGT_SAFT_END_DATE;// ---
    public static String AGT_SAFT_CURRENCY_CODE;// ---
    public static String AGT_SAFT_DATE_CREATED;// ---
    public static String AGT_SAFT_TAX_ENTITY;// ---
    public static String AGT_SAFT_PRODUCT_COMPANY_TAX_ID;// ---
    public static String AGT_SAFT_SOFTWARE_VALIDATION_NUMBER;// ---
    public static String AGT_SAFT_PRODUCT_ID;// ---
    public static String AGT_SAFT_PRODUCT_VERSION;// ---
    public static String AGT_SAFT_TELEPHONE;// ---
    public static String AGT_SAFT_FAX;// ---
    public static String AGT_SAFT_EMAIL;// ---
    public static String AGT_SAFT_WEBSITE;// ---

    public static String AGT_SAFT_CUSTOMER_ID;// ---
    public static String AGT_SAFT_CUSTOMER_ACCOUNT_ID;// ---
    public static String AGT_SAFT_CUSTOMER_TAX_ID;// ---
    public static String AGT_SAFT_CUSTUMER_COMPANY_NAME;// ---
    public static String AGT_SAFT_CUSTUMER_ADDRESS_DETAIL;// ---
    public static String AGT_SAFT_CUSTUMER_CITY;// ---
    public static String AGT_SAFT_CUSTUMER_POSTAL_CODE;// ---
    public static String AGT_SAFT_CUSTUMER_COUNTRY;// ---
    public static String AGT_SAFT_SELF_BILLING_INDICATOR;// ---

    //PRODUTOS
    public static String AGT_SAFT_PRODUCT_TYPE;// ---
    public static String AGT_SAFT_PRODUCT_CODE;// ---
    public static String AGT_SAFT_PRODUCT_GROUP;// ---
    public static String AGT_SAFT_PRODUCT_DESCRIPTION;// ---
    public static String AGT_SAFT_PRODUCT_NUMBER_CODE;// ---

    //IMPOSTOS ( TAXAS )
    public static String AGT_SAFT_TAX_TYPE;// ---
    public static String AGT_SAFT_TAX_COUNTRY_REGION;// ---
    public static String AGT_SAFT_TAX_CODE;// ---
    public static String AGT_SAFT_TAX_DESCRIPTION;// ---
    public static String AGT_SAFT_TAX_PERCENTAGE;// ---
    public static String AGT_SAFT_TAX_AMOUNT;// ---

    //DOCUMENTOS FINANCEIROS
    public static String AGT_SAFT_NUMBER_OF_ENTRIES;// ---
    public static String AGT_SAFT_TOTAL_DEBIT;// ---
    public static String AGT_SAFT_TOTAL_CREDIT;// ---
    public static String AGT_SAFT_INVOICE_NO;// ---
    public static String AGT_SAFT_INVOICE_STATUS;// ---
    public static String AGT_SAFT_INVOICE_STATUS_DATE;// ---
    public static String AGT_SAFT_SOURCE_ID;// ---
    public static String AGT_SAFT_SOURCE_BILLING;// ---
    public static String AGT_SAFT_HASH;// ---
    public static String AGT_SAFT_HASH_CONTROL;// ---
    public static String AGT_SAFT_PERIOD;// ---
    public static String AGT_SAFT_INVOICE_DATE;// ---
    public static String AGT_SAFT_INVOICE_TYPE;// ---
    public static String AGT_SAFT_STATUS_SELF_BILLING_INDICATOR;// ---
    public static String AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR;// ---
    public static String AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR;// ---
    public static String AGT_SAFT_INVOICE_SOURCE_ID;// ---
    public static String AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE;// ---
    public static String AGT_SAFT_INVOICE_CUSTOMER_ID;// ---
    public static String AGT_SAFT_LINE_NUMBER;// ---
    public static String AGT_SAFT_LINE_ORIGINATION_ON;// ---
    public static String AGT_SAFT_LINE_ORDER_DATE;// ---
    public static String AGT_SAFT_LINE_PRODUCT_CODE;// ---
    public static String AGT_SAFT_LINE_QUANTITY;// ---
    public static String AGT_SAFT_LINE_UNIT_OF_MEASURE;// ---
    public static String AGT_SAFT_LINE_UNIT_PRICE;// ---
    public static String AGT_SAFT_LINE_TAX_POINT_DATE;// ---
    public static String AGT_SAFT_LINE_DESCRIPTION;// ---
    public static String AGT_SAFT_LINE_PRODUCT_DESCRIPTION;// ---
    public static String AGT_SAFT_LINE_CREDIT_AMOUNT;// ---
    public static String AGT_SAFT_LINE_DEBIT_AMOUNT;// ---
    public static String AGT_SAFT_LINE_TAX_TYPE;// ---
    public static String AGT_SAFT_LINE_TAX_COUNTRY_REGION;// ---
    public static String AGT_SAFT_LINE_TAX_CODE;// ---
    public static String AGT_SAFT_LINE_TAX_PERCENTAGE;// ---
    public static String AGT_SAFT_LINE_SETTLEMENT_AMOUNT;// ---
    public static String AGT_SAFT_LINE_TAX_EXEMPTION_REASON;// ---
    public static String AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE;// ---
    public static String AGT_SAFT_LINE_TAX_PAYABLE;// ---
    public static String AGT_SAFT_LINE_NET_TOTAL;// ---
    public static String AGT_SAFT_LINE_GROSS_TOTAL;// ---
    public static String AGT_SAFT_TOTAL_DESCONTO_FACTURA;// ---
    public static String AGT_SAFT_MOEDA_ESTRANGEIRA;// ---
    public static String AGT_SAFT_MOEDA_CAMBIO;// ---
    public static String AGT_SAFT_PAYMENT_REF_NO;// ---
    public static String AGT_SAFT_TRANSACTION_ID;// ---
    public static String AGT_SAFT_TRANSACTION_DATE;// ---
    public static String AGT_SAFT_PAYMENT_TYPE;// ---
    public static String AGT_SAFT_DESCRIPTION;// ---
    public static String AGT_SAFT_SYSTEM_ID;// ---
    public static String AGT_PAYMENT_STATUS;// ---
    public static String AGT_PAYMENT_STATUS_DATE;// ---
    public static String AGT_REASON;// ---
    public static String AGT_PAYMENT_SOURCE_ID;// ---
    public static String AGT_SOURCE_PAYMENT;// ---
    public static String AGT_SYSTEM_ENTRY_DATE;// ---
    public static String AGT_CUSTOMER_ID;// ---
    public static String AGT_TAX_PAYABLE;// ---
    public static String AGT_NET_TOTAL;// ---
    public static String AGT_GROSS_TOTAL;// ---
    public static String AGT_SETTLEMENT_AMOUNT;// ---
    public static String AGT_CREDIT_AMOUNT;// ---
    public static String AGT_EXCHANGE_RATE;// ---
    public static String AGT_WITH_HOLDING_TAX_TYPE;// ---
    public static String AGT_WITH_HOLDING_TAX_DESCIPTION;// ---
    public static String AGT_WITH_HOLDING_TAX_AMOUNT;// ---

    public static String AGT_LINE_NUMBER;// ---
    public static String AGT_LINE_SOURCE_DOCUMENT_ID_ORIGINATING_ON;// ---
    public static String AGT_LINE_SOURCE_DOCUMENT_INVOICE_DATE;// ---
    public static String AGT_LINE_SOURCE_DOCUMENT_DESCRIPTION;// ---
    public static String AGT_LINE_SATTLEMENT_AMOUNT;// ---
    public static String AGT_LINE_DEBIT_AMOUNT;// ---
    public static String AGT_LINE_CREDIT_AMOUNT;// ---
    public static String AGT_LINE_TAX_TYPE;// ---
    public static String AGT_LINE_TAX_COUNTRY_REGION;// ---
    public static String AGT_LINE_TAX_CODE;// ---
    public static String AGT_LINE_TAX_PERCENTAGE;// ---
    public static String AGT_LINE_TAX_AMOUNT;// ---
    public static String AGT_LINE_TAX_EXCEPTION_REASON;// ---
    public static String AGT_LINE_TAX_EXCEPTION_CODE;// ---
    public static String AGT_LINE_REFERENCES_REFERENCE;// ---
    public static String AGT_LINE_REFERENCES_REASON;// ---

    /**
     * PARTE FORNECEDOR
     */
    public static String AGT_SAFT_SUPPLIER_ID;// ---
    public static String AGT_SAFT_SUPPLIER_ACCOUNT_ID;// ---
    public static String AGT_SAFT_SUPPLIER_TAX_ID;// ---
    public static String AGT_SAFT_SUPPLIER_COMPANY_NAME;// ---
    public static String AGT_SAFT_SUPPLIER_CONTACT;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_NUMBER;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_STREET_NAME;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_DETAIL;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_CITY;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_POSTAL_CODE;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_PROVINCE;// ---
    public static String AGT_SAFT_SUPPLIER_BULLING_ADDRESS_COUNTRY;// ---
    public static String AGT_SAFT_SUPPLIER_TELEPHONE;// ---
    public static String AGT_SAFT_SUPPLIER_FAX;// ---
    public static String AGT_SAFT_SUPPLIER_EMAIL;// ---
    public static String AGT_SAFT_SUPPLIER_SELFBELLIN_INDICATOR;// ---

    public static String AGT_SAFT_PURCHASE_INVOICES_NUMBER_OF_ENTRIES;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_NO;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_REFERENCES_REFERENCE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_REFERENCES_INVOICE_DATE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_HASH;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_SOURCE_ID;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_PERIOD;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DATE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_PURCHASE_TYPE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_SUPPLIER_ID;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_TAX_PAYBLE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_NET_TOTAL;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_GROSS_TOTAL;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_TAX_BASE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_AMOUNT;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_PERCENTAGE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_DEDUCTIBLE_DEDUTABLE_TAX_TYPE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_CODDE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_CURRENCY_AMOUNT;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_DOCUMENTS_TOTAL_MOEDA_EXCHANCE_RATE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_TYPE;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_DESCRIPTION;// ---
    public static String AGT_SAFT_PURCHASE_INVOICES_INVOICE_WITHOLDING_TAX_WITHOLDING_TAX_AMOUNT;// ---

    /*
         <!-- Por adicionar -->
                
                <SourceID></SourceID>
                <SystemEntryDate></SystemEntryDate>
                <CustomerID></CustomerID>
                <Line>
                    <LineNumber></LineNumber>
                    <SourceDocumentID>
                        <OriginatingON></OriginatingON>
                        <InvoiceDate></InvoiceDate>
                        <Description></Description>
                    </SourceDocumentID>
                    <SettlementAmount></SettlementAmount>
                    <DebitAmount></DebitAmount>
                    <CreditAmount></CreditAmount>
                    <Tax>
                        <TaxType></TaxType>
                        <TaxCountryRegion></TaxCountryRegion>
                        <TaxCode></TaxCode>
                        <TaxPercentage></TaxPercentage>
                        <TaxAmount></TaxAmount>
                        <TaxExemptionReason></TaxExemptionReason>
                        <TaxExemptionCode></TaxExemptionCode>
                    </Tax>
                </Line>
                
                <!-- Fim adicionar -->
    
     */
    static
    {
        //recuperar dados da empresa apartir da bd

        //CABEÇALHO
        AGT_SAFT_AUDIT_FILE_VERSION = "1.01_01";
        AGT_SAFT_COMPANY_ID = "5417221951";
        AGT_SAFT_TAX_REGISTRATION_NUMBER = "5417221951";
        AGT_SAFT_TAX_ACCOUNTING_BASIS = "F";
        AGT_SAFT_COMPANY_NAME = "DVML";
        AGT_SAFT_BUSINESS_NAME = "DVML";
        AGT_SAFT_ADDRESS_DETAIL = "Talatona";
        AGT_SAFT_CITY = "Luanda";
        AGT_SAFT_COUNTRY = "AO";
        AGT_SAFT_FISCAL_YEAR = "2019";
        AGT_SAFT_START_DATE = "2019-01-04";
        AGT_SAFT_END_DATE = "2019-10-08";
        AGT_SAFT_CURRENCY_CODE = "AOA";
        AGT_SAFT_DATE_CREATED = "2019-10-08";
        AGT_SAFT_TAX_ENTITY = "Global";
        AGT_SAFT_PRODUCT_COMPANY_TAX_ID = "AO503140600";
        AGT_SAFT_SOFTWARE_VALIDATION_NUMBER = "9999";
        AGT_SAFT_PRODUCT_ID = "Kitanda. 1.1/DVML-COMERCIAL, LDA";
        AGT_SAFT_PRODUCT_VERSION = "10.1.0.0";
        AGT_SAFT_TELEPHONE = "923409284-940537124";
        AGT_SAFT_FAX = "0000000";
        AGT_SAFT_EMAIL = "dvml.comercial@gmail.com";
        AGT_SAFT_WEBSITE = "www.dvml.com";

        //CLIENTES
        AGT_SAFT_CUSTOMER_ID = "1";
        AGT_SAFT_CUSTOMER_ACCOUNT_ID = "Desconhecido";
        AGT_SAFT_CUSTOMER_TAX_ID = "Consumidor ";
        AGT_SAFT_CUSTUMER_COMPANY_NAME = "DIVERSOS";
        AGT_SAFT_CUSTUMER_ADDRESS_DETAIL = "LUANDA";
        AGT_SAFT_CUSTUMER_CITY = "Luanda";
        AGT_SAFT_CUSTUMER_POSTAL_CODE = "*";
        AGT_SAFT_CUSTUMER_COUNTRY = "AO";
        AGT_SAFT_SELF_BILLING_INDICATOR = "0";

        //PRODUTOS
        AGT_SAFT_PRODUCT_TYPE = "P";
        AGT_SAFT_PRODUCT_CODE = "0005";
        AGT_SAFT_PRODUCT_GROUP = "N/A";
        AGT_SAFT_PRODUCT_DESCRIPTION = "COMPUTADOR HP";
        AGT_SAFT_PRODUCT_NUMBER_CODE = "1";

        //IMPOSTOS ( TAXAS )
        AGT_SAFT_TAX_TYPE = "IVA";
        AGT_SAFT_TAX_CODE = "NOR";
        AGT_SAFT_TAX_COUNTRY_REGION = "AO";
        AGT_SAFT_TAX_DESCRIPTION = "IMPOSTO SOBRE VALOR ACRESCENTADO";
        AGT_SAFT_TAX_PERCENTAGE = "0.14";
        AGT_SAFT_TAX_AMOUNT = "14";

        //DOCUMENTOS FINANCEIROS
        AGT_SAFT_NUMBER_OF_ENTRIES = "1";
        AGT_SAFT_TOTAL_DEBIT = "0.00";
        AGT_SAFT_TOTAL_CREDIT = "1000.00";
        AGT_SAFT_INVOICE_NO = "8 F/3";
        AGT_SAFT_INVOICE_STATUS = "N";
        AGT_SAFT_INVOICE_STATUS_DATE = "2019-05-03T09:21:27";
        AGT_SAFT_SOURCE_ID = "1";
        AGT_SAFT_SOURCE_BILLING = "P";
        AGT_SAFT_HASH = " INSIRA O HASH";
        AGT_SAFT_HASH_CONTROL = "NS";
        AGT_SAFT_PERIOD = "10";
        AGT_SAFT_INVOICE_DATE = "2019-05-08";
        AGT_SAFT_INVOICE_TYPE = "FT";
        AGT_SAFT_STATUS_SELF_BILLING_INDICATOR = "0";
        AGT_SAFT_STATUS_CASH_VAT_SCHEME_INDICATOR = "0";
        AGT_SAFT_STATUS_THIRD_PARTIES_BILLING_INDICATOR = "0";
        AGT_SAFT_INVOICE_SOURCE_ID = "Operador Demostração";
        AGT_SAFT_INVOICE_SYSTEM_ENTRY_DATE = "2019-05-03T09:21:27";
        AGT_SAFT_INVOICE_CUSTOMER_ID = "1";
        AGT_SAFT_LINE_NUMBER = "1";
        AGT_SAFT_LINE_PRODUCT_CODE = "0005";
        AGT_SAFT_LINE_QUANTITY = "1";
        AGT_SAFT_LINE_UNIT_OF_MEASURE = "Div";
        AGT_SAFT_LINE_UNIT_PRICE = "1000.00";
        AGT_SAFT_LINE_CREDIT_AMOUNT = "1000.00";
        AGT_SAFT_LINE_TAX_POINT_DATE = "2019-05-08";
        AGT_SAFT_LINE_DESCRIPTION = "Artigo Exemplo 1";
        AGT_SAFT_LINE_PRODUCT_DESCRIPTION = "Computador HP";
        AGT_SAFT_LINE_TAX_TYPE = "IVA";
        AGT_SAFT_LINE_TAX_CODE = "NOR";
        AGT_SAFT_LINE_TAX_COUNTRY_REGION = "AO";
        AGT_SAFT_LINE_TAX_PERCENTAGE = "14.00";
        AGT_SAFT_LINE_SETTLEMENT_AMOUNT = "0";
        AGT_SAFT_LINE_TAX_EXEMPTION_REASON = "IVA-Regime de não Sujeição";
        AGT_SAFT_LINE_TAX_TAX_EXEMPTION_CODE = "M04";
        AGT_SAFT_LINE_TAX_PAYABLE = "140.00";
        AGT_SAFT_LINE_NET_TOTAL = "1000.00";
        AGT_SAFT_LINE_GROSS_TOTAL = "0.00";
    }

    public static void activar_cmb_armazem( JComboBox cmb )
    {

        cmb.setEnabled( true );
//            if(select_armazem.equals("GERAL") ){
//                    cmb.setEnabled(false);
//                    cmb.setSelectedIndex(1);
//            }
//            else  if(select_armazem.equals("GROSSO") ){
//                    cmb.setEnabled(false);
//                    cmb.setSelectedItem(select_armazem);
//            }else  if(select_armazem.equals("RETALHO") ){
//                    cmb.setEnabled(false);
//                    cmb.setSelectedItem(select_armazem);
//            }

    }

    private static int fact( int n )
    {

        if ( n == 0 || n == 1 )
        {
            return 1;
        }
        else
        {
            return n * fact( n - 1 );
        }
    }

    private static int soma( int posicao, int tamanho, int[] v )
    {
        if ( posicao == tamanho )
        {
            return 0;
        }
        else
        {
            int meio = tamanho / 2;
            return v[ posicao ] + soma( ++posicao, meio, v ) + soma( ++meio, tamanho, v );
        }
    }

    private static int soma_dividir( int posicao, int tamanho, int[] v )
    {

        if ( ( posicao + 1 ) == tamanho )
        {
            return v[ posicao ];
        }
        else
        {
            int meio = (( tamanho + posicao ) / 2);
            System.out.println( "MEIO:" + meio );
            return soma_dividir( posicao, meio, v ) + soma_dividir( meio, tamanho, v );

        }

    }

    private static int soma_dividir_vector( int posicao, int tamanho, Vector vector )
    {

        if ( ( posicao + 1 ) == tamanho )
        {
            return (int) vector.get( posicao );
        }
        else
        {
            int meio = (( tamanho + posicao ) / 2);
            return soma_dividir_vector( posicao, meio, vector ) + soma_dividir_vector( meio, tamanho, vector );

        }

    }

    public static Vector< Vector<TbPreco>> actualizar_vector_preco( int posicao, int tamanho, Vector<Vector<TbPreco>> vector )
    {

        if ( ( posicao + 1 ) == tamanho )
        {
            vector.get( posicao ).get( 0 ).setPrecoCompra( new BigDecimal( "100" ) );

            return vector;
        }
        else
        {
            int meio = (( tamanho + posicao ) / 2);
            System.err.println( "MEIO: " + meio );

            return actualizar_vector_preco( meio, tamanho, vector );

        }

    }

    private static Vector< Vector<TbPreco>> unicao( Vector< Vector<TbPreco>> a, Vector< Vector<TbPreco>> b )
    {
        a.addAll( b );
        return a;

    }

    public static double soma_dividir_vector_preco( int posicao, int tamanho, Vector<Vector<TbPreco>> vector )
    {

        if ( ( posicao + 1 ) == tamanho )
        {
            return vector.get( posicao ).get( 0 ).getPrecoCompra().doubleValue();
        }
        else
        {
            int meio = (( tamanho + posicao ) / 2);
            return soma_dividir_vector_preco( posicao, meio, vector ) + soma_dividir_vector_preco( meio, tamanho, vector );

        }

    }

    private static Vector multiplicacao( int p, int u, Vector v )
    {

        if ( p < u )
        {
            v.set( p, 2 );
            return v;
        }
        else
        {
            //int meio = (p+u)/2;
            multiplicacao( p + 1, u, v );
            //return uniao_vector(  multiplicacao(p, meio, v), multiplicacao(meio, u, v) ); 

        }

        return v;

    }

    public static Vector uniao_vector( Vector vector_x, Vector vector_y )
    {

        Vector vector = new Vector();
        vector.addAll( vector_x );
        vector.addAll( vector_y );

        System.out.println( "Size X: " + vector_x.size() );
        System.out.println( "Size Y: " + vector_y.size() );
        System.out.println( "Size Total: " + vector.size() );

        return vector;
    }

    public static int getConfirmacaoDialog( String message )
    {
        return JOptionPane.showConfirmDialog( null, message, "Aviso", JOptionPane.YES_OPTION );
    }

    public static void main( String[] args )
    {
//        int vector_x[] = {4,6,10,14,2}; 
//        int vector_y[] =
//        {
//            3, 10, 28, 30, 31
//        };
//
//        //System.out.println("SOMA:"  +soma_dividir(0, vector_y.length, vector_y));
////        
////        int vector[] = uniao(vector_x, vector_y);
//        Vector vector_x = new Vector();
//        vector_x.add( 4 );
//        vector_x.add( 6 );
//        vector_x.add( 10 );
//        vector_x.add( 14 );
//        vector_x.add( 2 );
//        vector_x.add( 3 );
//        vector_x.add( 10 );
//        vector_x.add( 28 );
//        vector_x.add( 30 );
//        vector_x.add( 31 );
//
//        System.out.println( "SOMA:" + soma_dividir_vector( 0, vector_x.size(), vector_x ) );
//       Vector vector =  multiplicacao(0, vector_x.size(), vector_x);
//       
        //int meio = (vector.size()/2);

//       List  sub1 =  vector.subList(0, meio);
//       List  sub2 = vector.subList( meio, vector.size() );
        // System.out.println("SUB_1");
//       for (int i = 0; i < vector.size(); i++) {
//           System.out.println( vector.get(i) );
//       }
//       
        //System.out.println("Máximo: " +soma(0,vector.length,vector) );
        //System.out.println("Máximo: " +soma_dividir(0,vector.length,vector) ); 
//        ProgressBarDemo obj = new ProgressBarDemo();
//        obj.setVisible(true);
//        MetodosUtil.getDias();
        System.out.println( "Dias Úteis de Trabalho: " + MetodosUtil.getDiasUteis( MetodosUtil.stringToDate( "09/07/2020" ), MetodosUtil.stringToDate( "10/07/2020" ), 1 ) );

        JOptionPane.showMessageDialog( null, "Apenas para fim de teste." );

    }

    private static int getDiasAnos( Date data_inicio, Date data_fim )
    {

        int dia_mes_entrada = getDias( data_inicio.getMonth(), data_inicio.getYear() );
        int diferenca_entrada = dia_mes_entrada - data_inicio.getDate() + 1;//1 - porque o ultimo dia  conta.

        int dia_actual = 0;
        int dias_intervalo = 0;
        int total_dias = 0;

        if ( data_fim.getMonth() == data_inicio.getMonth() && data_fim.getDate() == data_inicio.getDate() )
        {
            total_dias = 1;

        }
        else if ( data_inicio.getMonth() == data_fim.getMonth() )
        {
            dia_actual = 0;
            total_dias = data_fim.getDate() - data_inicio.getDate() + 1;
        }
        else
        {

            dia_actual = data_fim.getDate();
            dias_intervalo = soma_dia_intervalo( data_inicio, data_fim );

            total_dias = diferenca_entrada + dias_intervalo + dia_actual;

        }

        return total_dias;

    }

    private static int soma_dia_intervalo( Date data_entrada, Date data_saida )
    {
        int soma_dia = 0;
        for ( int i = data_entrada.getMonth() + 1; i < data_saida.getMonth(); i++ )
        {
            soma_dia = soma_dia + getDias( i, data_entrada.getYear() );

        }
        return soma_dia;

    }

    private static int getDias( int mes, int ano )
    {

        switch (mes)
        {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
            {
                return getDiaBissesto( 1900 + ano );
            }
        }

    }

    private static int getDiaBissesto( int ano )
    {
        switch (ano)
        {
            case 2016:
            case 2020:
            case 2024:
            case 2028:
            case 2032:
                return 29;
            default:
                return 28;
        }

    }

    private void salvar_venda_comercial_automatico( Date dataDocumento )
    {
//        mostrar_proximo_codigo_documento();
        BDConexao conexaoTransaction = new BDConexao();
        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexaoTransaction );
        VendasController vendasController = new VendasController( conexaoTransaction );
        DocumentosController documentosController = new DocumentosController( conexaoTransaction );
        DocumentosController.startTransaction( conexaoTransaction );
        int prazo_proforma = dadosInstituicaoController.findByCodigo( 1 ).getPrazoProforma();
        Date data_documento = dataDocumento;

        TbVenda venda_local = new TbVenda();
        venda_local.setDataVenda( data_documento );
        venda_local.setRefDataFact( data_documento );
        venda_local.setRefCodFact( "" );

        Calendar calendar = Calendar.getInstance();
        calendar.setTime( data_documento );
        //adicionar 15 dias na data do documento.

        calendar.add( Calendar.DATE, prazo_proforma );
        venda_local.setDataVencimento( calendar.getTime() );
        venda_local.setHora( data_documento );
        venda_local.setNomeCliente( _CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setClienteNif( _CLIENTE_CONSUMIDOR_FINAL );

        //Total Ilíquido
        venda_local.setTotalGeral( new BigDecimal( getTotalIliquido() ) );
        //desconto por linha
        venda_local.setDescontoComercial( new BigDecimal( getDescontoComercial() ) );
        //imposto
        //calculaTotalIVA();
        venda_local.setTotalIva( new BigDecimal( getTotalImposto() ) );
        //calculaTotalRetencao();
        venda_local.setTotalRetencao( new BigDecimal( getTotalRetencaoLiquido() ) );
        //desconto global
        venda_local.setDescontoFinanceiro( new BigDecimal( getDescontoFinanceiro() ) );
        //Total(AOA) <=> Total Líquido
        venda_local.setTotalVenda( new BigDecimal( getTotalAOALiquido() ) );
        venda_local.setValorEntregue( new BigDecimal( getValor_entregue() ) );
        venda_local.setTroco( new BigDecimal( getTroco() ) );
        venda_local.setTotalIncidencia( new BigDecimal( getTotalIncidencia() ) );
        venda_local.setTotalIncidenciaIsento( new BigDecimal( getTotalIncidenciaIsento() ) );

        /*outros campos*/
        venda_local.setDescontoTotal( new BigDecimal( getDescontoComercial() + getDescontoFinanceiro() ) );
        venda_local.setIdBanco( new TbBanco( 1 ) );
        venda_local.setIdArmazemFK( new TbArmazem( ARMAZEM_DEFAUTL ) );

        venda_local.setCodigoUsuario( new TbUsuario( 1 ) );
        venda_local.setCodigoCliente( new TbCliente( 1 ) );
        venda_local.setFkAnoEconomico( new AnoEconomico( 4 ) );
        venda_local.setReferencia( "" );
        venda_local.setNomeConsumidorFinal( _CLIENTE_CONSUMIDOR_FINAL );
        venda_local.setFkDocumento( new Documento( 1 ) );

        String prox_doc = getCodDocActualizador( documentosController, vendasController );

        venda_local.setCodFact( prox_doc );
        venda_local.setCont( 0 );

        venda_local.setHashCod( MetodosUtil.criptografia_hash( venda_local, getGrossTotal(), conexaoTransaction ) );

        venda_local.setTotalPorExtenso( "" );
        venda_local.setModelo( "" );
        venda_local.setNumMotor( "" );

        venda_local.setNomeMotorista( "" );
        venda_local.setMatricula( "" );
        venda_local.setMarcaCarro( "" );
        venda_local.setKilometro( "" );
        venda_local.setNumChassi( "" );
        venda_local.setObs( "" );
        venda_local.setCorCarro( "" );
        venda_local.setNDocMotorista( "" );
        System.out.println( "STATUS:hash cod processado." );

        // venda_local.setAssinatura( this.prox_doc );
        venda_local.setAssinatura( MetodosUtil.assinatura_doc( venda_local.getHashCod() ) );
//        venda_local.setRefDataFact( CfMethods.fullDateToText( venda_local.getDataVenda() ) );

        //System.out.println( "STATUS:documento assinado com sucesso." );\\
        venda_local.setFkCambio( new Cambio( 1 ) );


        /*status documento*/
        venda_local.setStatusEliminado( "false" );
        venda_local.setPerformance( "false" );
        venda_local.setCredito( "false" );
        venda_local.setGorjeta( new BigDecimal( 0d ) );

        try
        {

            if ( vendasController.salvar( venda_local ) )
            {
                Integer last_venda = vendasController.getLastVenda().getCodigo();
                if ( Objects.isNull( last_venda ) || last_venda == 0 )
                {
                    DocumentosController.rollBackTransaction( conexaoTransaction );
                    conexaoTransaction.close();
                    return;
                }
                System.err.println( "last_venda: " + last_venda );
                System.out.println( "STATUS:factura criada com sucesso." );

                if ( last_venda != null )
                {

// salvar_item_venda_comercial( last_venda );
                }
            }
            else
            {
                System.out.println( "ERROR: Já existe venda relacionada." );
            }

        }
        catch ( Exception e )
        {
            System.err.println( "STATUS: falha ao actualizar a factura" );
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar a Factura", "FALHA", JOptionPane.ERROR_MESSAGE );

            DocumentosController.rollBackTransaction( conexaoTransaction );

            conexaoTransaction.close();
        }

    }

    public double getTotalIliquido()
    {
        double qtd = 0d;
        double total_iliquido = 0d, preco_unitario = 0d;
        for ( int i = 0; i < listaProdutosAleatoris.size(); i++ )
        {

            preco_unitario = 0;
            qtd = 1;
            total_iliquido += ( preco_unitario * qtd );

        }

        return total_iliquido;
    }

    public double getDescontoComercial()
    {
        return 0d;
    }

    public double getTotalImposto()
    {
        return 0d;
    }

    public double getTotalRetencaoLiquido()
    {
        return 0d;
    }

    public double getDescontoFinanceiro()
    {
        return 0d;
    }

    public double getTotalAOALiquido()
    {
        return 0d;
    }

    public double getValor_entregue()
    {
        return 0d;
    }

    public double getTroco()
    {
        return 0d;
    }

    public double getTotalIncidencia()
    {
        return 0d;
    }

    public double getTotalIncidenciaIsento()
    {
        return 0d;
    }

    private static String getCodDocActualizador( DocumentosController documentosController, VendasController vendasController )
    {
        try
        {
            Documento documento = (Documento) documentosController.findById( 1 );
            AnoEconomico anoEconomico = new AnoEconomico( 4 );
            // this.doc_prox_cod = documento.getCodUltimoDoc() + 1;
            int doc_prox_cod = vendasController.getUltimaContagemByIdDocumentoAndAnoEconomico(
                    1, 4 ) + 1;
            String prox_doc = documento.getAbreviacao();
            //FA Série / codigo
            prox_doc += " " + anoEconomico.getSerie() + "/" + doc_prox_cod;
            return prox_doc;
        }
        catch ( Exception e )
        {
            return "";
        }
    }

    private double getTotalVendaIVASemIncluirDesconto()
    {
        double taxa = 0, total_iva_local = 0, preco_unitario = 0, sub_total_iliquido = 0;
        double qtd = 0d;

//        DefaultTableModel modelo = ( DefaultTableModel ) table.getModel();
//        for ( int i = 0; i < modelo.getRowCount(); i++ )
//        {
//            preco_unitario = CfMethods.parseMoedaFormatada( modelo.getValueAt( i, 3 ).toString() );
//            qtd = Double.parseDouble( modelo.getValueAt( i, 4 ).toString() );
//            sub_total_iliquido = ( preco_unitario * qtd );
//            taxa = Double.parseDouble( modelo.getValueAt( i, 6 ).toString() );
//            total_iva_local += ( ( ( sub_total_iliquido ) * ( taxa / 100 ) ) );
//        }
        return total_iva_local;
    }

    private double getGrossTotal()
    {
        System.out.println( "TOTALILIQUIDO: " + getTotalVendaIVASemIncluirDesconto() );
        System.out.println( "TOTALVENDAIVASEMDESCONTO: " + getTotalVendaIVASemIncluirDesconto() );
        return getTotalIliquido() + getTotalVendaIVASemIncluirDesconto();
    }

}
