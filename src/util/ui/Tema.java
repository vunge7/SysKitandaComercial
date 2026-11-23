package util.ui;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tema do negócio: cores e símbolos de todo o sistema conforme a coluna
 * "negocio" da tb_dados_instituicao. Com "Farmacia" tudo fica verde e com a
 * cruz da farmácia; com "Restaurante" bordô e talheres; com "Oficina" grafite,
 * laranja e a chave-inglesa; com "Lavandaria" azul-água e a máquina de lavar;
 * com "Ginásio" carvão e verde-lima com o haltere; com "Centro Infantil"
 * violeta, rosa e amarelo-sol com o ursinho; com "Transportes" azul-estrada e
 * âmbar com o camião; com "Prestação de Serviço" índigo com a pasta.
 * "Comercial" e "Layout" ficam com o azul do comercial.
 *
 * <p>
 * Aplica-se no arranque (antes de abrir qualquer janela) e de novo ao entrar no
 * sistema, para uma mudança nos dados da instituição valer logo. As cores de
 * estado (verde = livre/sucesso, vermelho = erro/ocupado, laranja = aviso) não
 * mudam: têm significado em qualquer negócio.
 */
public final class Tema {

    public enum Negocio {
        COMERCIAL, FARMACIA, LAVANDARIA, RESTAURANTE, OFICINA,
        TRANSPORTES, PRESTACAO_SERVICO, GINASIO, CENTRO_INFANTIL
    }

    /**
     * Valores da coluna tb_dados_instituicao.negocio (ENUM), pela ordem em que
     * aparecem nos Dados da Empresa. Ginásio e Centro Infantil entraram em
     * 2026-11-21: ver {@link #garantirValoresNegocio(Connection)}.
     */
    public static final String[] VALORES_BD = {"Comercial", "Farmacia", "Restaurante", "Lavandaria", "Oficina",
        "Transportes", "Prestação de Serviço", "Ginásio", "Centro Infantil", "Layout"};

    private static volatile Negocio actual = Negocio.COMERCIAL;
    private static Color gradienteFim = new Color(0, 96, 168);
    private static Color claro = new Color(196, 218, 245);

    private Tema() {
    }

    public static Negocio actual() {
        return actual;
    }

    /** Fim do degradê dos cabeçalhos. */
    public static Color gradienteFim() {
        return gradienteFim;
    }

    /** Texto claro sobre os fundos escuros (cabeçalhos, barras laterais). */
    public static Color claro() {
        return claro;
    }

    // =====================================================================
    // SÍMBOLOS E TEXTOS
    // =====================================================================
    /** Símbolo do negócio (logótipos, cabeçalhos). */
    public static MenuWeb.Simbolo simbolo() {
        return simbolo(actual);
    }

    public static MenuWeb.Simbolo simbolo(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return MenuWeb.Simbolo.FARMACIA;
            case LAVANDARIA:
                return MenuWeb.Simbolo.LAVANDARIA;
            case RESTAURANTE:
                return MenuWeb.Simbolo.TALHERES;
            case OFICINA:
                return MenuWeb.Simbolo.FERRAMENTA;
            case TRANSPORTES:
                return MenuWeb.Simbolo.CAMIAO;
            case PRESTACAO_SERVICO:
                return MenuWeb.Simbolo.PASTA;
            case GINASIO:
                return MenuWeb.Simbolo.HALTERE;
            case CENTRO_INFANTIL:
                return MenuWeb.Simbolo.URSO;
            default:
                return MenuWeb.Simbolo.CARTEIRA;
        }
    }

    /** Nome a mostrar ("Gestão Comercial", "Farmácia"...). */
    public static String area() {
        return area(actual);
    }

    public static String area(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return "Farmácia";
            case LAVANDARIA:
                return "Lavandaria";
            case RESTAURANTE:
                return "Restaurante";
            case OFICINA:
                return "Oficina";
            case TRANSPORTES:
                return "Transportes";
            case PRESTACAO_SERVICO:
                return "Prestação de Serviços";
            case GINASIO:
                return "Ginásio";
            case CENTRO_INFANTIL:
                return "Centro Infantil";
            default:
                return "Gestão Comercial";
        }
    }

    /** Frase curta do negócio (cartões dos Dados da Empresa, cabeçalhos). */
    public static String descricao(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return "Medicamentos, lotes e validades";
            case LAVANDARIA:
                return "Recolhas, entregas e peças";
            case RESTAURANTE:
                return "Mesas, pedidos e cozinha";
            case OFICINA:
                return "Viaturas, reparações e peças";
            case TRANSPORTES:
                return "Viagens, cargas e guias";
            case PRESTACAO_SERVICO:
                return "Serviços, contratos e avenças";
            case GINASIO:
                return "Sócios, mensalidades e aulas";
            case CENTRO_INFANTIL:
                return "Crianças, turmas e propinas";
            default:
                return "Loja, armazém e facturação";
        }
    }

    /**
     * Símbolos dos módulos mais usados de um negócio, para os cartões dos
     * menus: {venda, clientes, produtos/serviços, relatórios}.
     */
    public static MenuWeb.Simbolo[] simbolosModulos(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.FARMACIA, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.COMPRIMIDO, MenuWeb.Simbolo.GRAFICO};
            case LAVANDARIA:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.LAVANDARIA, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.CAMISA, MenuWeb.Simbolo.GRAFICO};
            case RESTAURANTE:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.TALHERES, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.CHAVENA, MenuWeb.Simbolo.GRAFICO};
            case OFICINA:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.FERRAMENTA, MenuWeb.Simbolo.CARRO,
                    MenuWeb.Simbolo.CAIXA, MenuWeb.Simbolo.GRAFICO};
            case TRANSPORTES:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.CAMIAO, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.CAIXA, MenuWeb.Simbolo.GRAFICO};
            case PRESTACAO_SERVICO:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.PASTA, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.PRANCHETA, MenuWeb.Simbolo.GRAFICO};
            case GINASIO:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.HALTERE, MenuWeb.Simbolo.PULSACAO,
                    MenuWeb.Simbolo.CRONOMETRO, MenuWeb.Simbolo.GRAFICO};
            case CENTRO_INFANTIL:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.URSO, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.CUBOS, MenuWeb.Simbolo.BALAO};
            default:
                return new MenuWeb.Simbolo[]{MenuWeb.Simbolo.CARRINHO, MenuWeb.Simbolo.PESSOAS,
                    MenuWeb.Simbolo.CAIXA, MenuWeb.Simbolo.GRAFICO};
        }
    }

    // =====================================================================
    // CORES
    // =====================================================================
    /**
     * Fundo dos campos de digitação dos formulários antigos (o ciano de
     * sempre no comercial, o verde nas farmácias...). Ver
     * {@link util.TemaNegocioUtil#corDoNegocio(boolean)}.
     */
    public static Color corCampo() {
        return corCampo(actual);
    }

    public static Color corCampo(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return new Color(140, 198, 63);
            case LAVANDARIA:
                return new Color(165, 243, 252);
            case RESTAURANTE:
                return new Color(254, 205, 211);
            case OFICINA:
                return new Color(254, 215, 170);
            case TRANSPORTES:
                return new Color(253, 230, 138);
            case PRESTACAO_SERVICO:
                return new Color(199, 210, 254);
            case GINASIO:
                return new Color(217, 249, 157);
            case CENTRO_INFANTIL:
                return new Color(233, 213, 255);
            default:
                return new Color(0, 255, 255);
        }
    }

    /**
     * Amostra das cores de um negócio sem o aplicar (pré-visualização):
     * {escuro, vivo, destaque}.
     */
    public static Color[] amostra(String negocio) {
        return amostra(deTexto(negocio));
    }

    public static Color[] amostra(Negocio negocio) {
        switch (negocio) {
            case FARMACIA:
                return new Color[]{new Color(4, 90, 52), new Color(16, 163, 104), new Color(196, 160, 92)};
            case LAVANDARIA:
                return new Color[]{new Color(8, 76, 97), new Color(6, 182, 212), new Color(196, 160, 92)};
            case RESTAURANTE:
                return new Color[]{new Color(120, 28, 40), new Color(225, 29, 72), new Color(201, 162, 39)};
            case OFICINA:
                return new Color[]{new Color(39, 48, 61), new Color(234, 88, 12), new Color(234, 140, 40)};
            case TRANSPORTES:
                return new Color[]{new Color(23, 37, 84), new Color(37, 99, 235), new Color(245, 158, 11)};
            case PRESTACAO_SERVICO:
                return new Color[]{new Color(49, 46, 129), new Color(99, 102, 241), new Color(196, 160, 92)};
            case GINASIO:
                return new Color[]{new Color(24, 24, 27), new Color(101, 163, 13), new Color(163, 230, 53)};
            case CENTRO_INFANTIL:
                return new Color[]{new Color(91, 33, 182), new Color(219, 39, 119), new Color(250, 204, 21)};
            default:
                return new Color[]{new Color(0, 51, 102), new Color(37, 132, 235), new Color(196, 160, 92)};
        }
    }

    /** Lê o negócio da instituição (id 1) e aplica o tema; em caso de erro fica o actual. */
    public static void aplicarDaInstituicao() {
        try {
            // Ginásio / Centro Infantil no ENUM (só altera a tabela na primeira vez);
            // ligação própria: um ALTER faz commit implícito, não pode tocar na partilhada
            try (Connection cn = util.BDConexao.novaLigacao()) {
                garantirValoresNegocio(cn);
            } catch (SQLException e) {
                System.err.println("[Tema] ENUM negocio: " + e.getMessage());
            }
            // JDBC (leve): no arranque o JPA ainda não precisa de estar iniciado
            entity.TbDadosInstituicao d = (entity.TbDadosInstituicao) new comercial.controller.DadosInstituicaoController(
                    util.BDConexao.getInstancia()).findById(1);
            aplicar(d == null ? null : d.getNegocio());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static Negocio deTexto(String negocio) {
        String n = chave(negocio);
        if (n.startsWith("farm")) {
            return Negocio.FARMACIA;
        }
        if (n.startsWith("lavand")) {
            return Negocio.LAVANDARIA;
        }
        if (n.startsWith("restaur") || n.startsWith("bar")) {
            return Negocio.RESTAURANTE;
        }
        if (n.startsWith("oficin")) {
            return Negocio.OFICINA;
        }
        if (n.startsWith("transport")) {
            return Negocio.TRANSPORTES;
        }
        if (n.startsWith("prestac") || n.startsWith("servic")) {
            return Negocio.PRESTACAO_SERVICO;
        }
        if (n.startsWith("ginas") || n.startsWith("gym") || n.startsWith("academia")) {
            return Negocio.GINASIO;
        }
        if (n.startsWith("centro infantil") || n.startsWith("infantil") || n.startsWith("creche")
                || n.startsWith("jardim")) {
            return Negocio.CENTRO_INFANTIL;
        }
        return Negocio.COMERCIAL;
    }

    /** Texto sem acentos, em minúsculas e sem espaços nas pontas. */
    private static String chave(String texto) {
        return Normalizer.normalize(texto == null ? "" : texto.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public static synchronized void aplicar(String negocio) {
        Negocio n = deTexto(negocio);
        switch (n) {
            case FARMACIA:
                paleta(new Color(4, 90, 52), new Color(2, 52, 31), new Color(5, 122, 85), new Color(16, 163, 104),
                        new Color(10, 135, 88), new Color(6, 78, 59), new Color(220, 245, 232), new Color(160, 215, 185),
                        new Color(240, 250, 244), new Color(8, 128, 84), new Color(200, 238, 220),
                        new Color(196, 160, 92), new Color(228, 204, 152));
                break;
            case LAVANDARIA:
                paleta(new Color(8, 76, 97), new Color(5, 46, 61), new Color(14, 116, 144), new Color(6, 182, 212),
                        new Color(8, 145, 178), new Color(12, 74, 110), new Color(224, 246, 250), new Color(160, 214, 228),
                        new Color(239, 250, 252), new Color(14, 116, 144), new Color(200, 236, 245),
                        new Color(196, 160, 92), new Color(228, 204, 152));
                break;
            case RESTAURANTE:
                paleta(new Color(120, 28, 40), new Color(68, 14, 22), new Color(159, 18, 57), new Color(225, 29, 72),
                        new Color(176, 36, 60), new Color(110, 24, 36), new Color(253, 235, 238), new Color(238, 180, 190),
                        new Color(254, 246, 247), new Color(165, 40, 60), new Color(250, 214, 220),
                        new Color(201, 162, 39), new Color(234, 206, 120));
                break;
            case OFICINA:
                paleta(new Color(39, 48, 61), new Color(20, 26, 34), new Color(71, 85, 105), new Color(234, 88, 12),
                        new Color(71, 85, 105), new Color(51, 65, 85), new Color(235, 238, 242), new Color(200, 206, 214),
                        new Color(246, 247, 249), new Color(62, 75, 94), new Color(214, 220, 228),
                        new Color(234, 140, 40), new Color(253, 186, 116));
                break;
            case TRANSPORTES:
                // azul-estrada com o âmbar dos sinais
                paleta(new Color(23, 37, 84), new Color(12, 20, 48), new Color(30, 64, 175), new Color(37, 99, 235),
                        new Color(37, 72, 160), new Color(23, 37, 84), new Color(232, 237, 250), new Color(180, 194, 230),
                        new Color(244, 247, 253), new Color(30, 64, 175), new Color(199, 210, 254),
                        new Color(245, 158, 11), new Color(252, 211, 77));
                break;
            case PRESTACAO_SERVICO:
                // índigo executivo
                paleta(new Color(49, 46, 129), new Color(30, 27, 75), new Color(67, 56, 202), new Color(99, 102, 241),
                        new Color(79, 70, 229), new Color(49, 46, 129), new Color(238, 242, 255), new Color(199, 210, 254),
                        new Color(245, 247, 255), new Color(67, 56, 202), new Color(199, 210, 254),
                        new Color(196, 160, 92), new Color(228, 204, 152));
                break;
            case GINASIO:
                // carvão e verde-lima (energia)
                paleta(new Color(24, 24, 27), new Color(9, 9, 11), new Color(63, 63, 70), new Color(101, 163, 13),
                        new Color(77, 124, 15), new Color(39, 39, 42), new Color(236, 252, 203), new Color(190, 227, 140),
                        new Color(247, 254, 231), new Color(54, 83, 20), new Color(217, 249, 157),
                        new Color(163, 230, 53), new Color(217, 249, 157));
                break;
            case CENTRO_INFANTIL:
                // violeta, rosa e amarelo-sol (alegre)
                paleta(new Color(91, 33, 182), new Color(59, 7, 100), new Color(124, 58, 237), new Color(219, 39, 119),
                        new Color(124, 58, 237), new Color(76, 29, 149), new Color(243, 232, 255), new Color(216, 180, 254),
                        new Color(250, 245, 255), new Color(192, 38, 211), new Color(233, 213, 255),
                        new Color(250, 204, 21), new Color(254, 240, 138));
                break;
            default:
                paleta(new Color(0, 51, 102), new Color(0, 33, 71), new Color(0, 98, 178), new Color(37, 132, 235),
                        new Color(51, 122, 183), new Color(21, 67, 120), new Color(226, 235, 247), new Color(170, 190, 220),
                        new Color(242, 246, 252), new Color(0, 96, 168), new Color(196, 218, 245),
                        new Color(196, 160, 92), new Color(228, 204, 152));
                break;
        }
        actual = n;
    }

    private static void paleta(Color escuro, Color noite, Color medio, Color vivo, Color seleccao, Color texto,
            Color claroFundo, Color borda, Color linhaImpar, Color fimGradiente, Color textoClaro,
            Color dourado, Color douradoClaro) {
        EstiloUI.AZUL_ESCURO = escuro;
        EstiloUI.AZUL_TEXTO = texto;
        EstiloUI.AZUL_CLARO = claroFundo;
        EstiloUI.AZUL_BORDA = borda;
        EstiloUI.LINHA_IMPAR = linhaImpar;
        EstiloUI.SELECCAO = seleccao;
        MenuWeb.AZUL_NOITE = noite;
        MenuWeb.AZUL_MARINHO = escuro;
        MenuWeb.DOURADO = dourado;
        MenuWeb.DOURADO_CLARO = douradoClaro;
        ComponentesWeb.AZUL = escuro;
        ComponentesWeb.AZUL_MEDIO = medio;
        ComponentesWeb.AZUL_VIVO = vivo;
        FormularioWeb.TEXTO_CABECALHO = textoClaro;
        gradienteFim = fimGradiente;
        claro = textoClaro;
    }

    // =====================================================================
    // BASE DE DADOS: valores do ENUM negocio
    // =====================================================================
    /**
     * Junta ao ENUM tb_dados_instituicao.negocio os valores de
     * {@link #VALORES_BD} que ainda lá não estão (Ginásio, Centro Infantil),
     * sem mexer nos que já existem, no valor por omissão nem no conjunto de
     * caracteres da coluna. Quando já está tudo, é só uma leitura do
     * information_schema. Corre no arranque e ao gravar os Dados da Empresa.
     *
     * @return os valores que a coluna aceita depois da verificação (vazio se
     *         não foi possível ler a estrutura)
     */
    public static synchronized List<String> garantirValoresNegocio(Connection cn) {
        List<String> valores = new ArrayList<>();
        if (cn == null) {
            return valores;
        }
        String tipo, nulo, padrao, charset, colacao;
        try (PreparedStatement ps = cn.prepareStatement("SELECT COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT,"
                + " CHARACTER_SET_NAME, COLLATION_NAME FROM information_schema.COLUMNS"
                + " WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'tb_dados_instituicao' AND COLUMN_NAME = 'negocio'");
                ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return valores;
            }
            tipo = rs.getString(1);
            nulo = rs.getString(2);
            padrao = rs.getString(3);
            charset = rs.getString(4);
            colacao = rs.getString(5);
        } catch (SQLException e) {
            System.err.println("[Tema] ler o ENUM negocio: " + e.getMessage());
            return valores;
        }
        if (tipo == null || !tipo.toLowerCase().startsWith("enum(")) {
            // VARCHAR ou outro tipo de texto: aceita qualquer valor
            valores.addAll(Arrays.asList(VALORES_BD));
            return valores;
        }
        String corpo = tipo.substring(tipo.indexOf('(') + 1, tipo.lastIndexOf(')')).trim();
        if (corpo.length() >= 2) {
            for (String v : corpo.substring(1, corpo.length() - 1).split("','", -1)) {
                valores.add(v.replace("''", "'"));
            }
        }
        Set<String> chaves = new HashSet<>();
        for (String v : valores) {
            chaves.add(chave(v));
        }
        List<String> novos = new ArrayList<>(valores);
        for (String v : VALORES_BD) {
            if (chaves.add(chave(v))) {
                novos.add(v);
            }
        }
        if (novos.size() == valores.size()) {
            return valores;
        }
        StringBuilder sql = new StringBuilder("ALTER TABLE tb_dados_instituicao MODIFY negocio ENUM(");
        for (int i = 0; i < novos.size(); i++) {
            sql.append(i == 0 ? "'" : ",'").append(novos.get(i).replace("'", "''")).append("'");
        }
        sql.append(")");
        if (charset != null) {
            sql.append(" CHARACTER SET ").append(charset);
        }
        if (colacao != null) {
            sql.append(" COLLATE ").append(colacao);
        }
        sql.append("YES".equalsIgnoreCase(nulo) ? " NULL" : " NOT NULL");
        if (padrao != null) {
            sql.append(" DEFAULT '").append(padrao.replace("'", "''")).append("'");
        }
        try (Statement st = cn.createStatement()) {
            st.execute(sql.toString());
            System.out.println("[Tema] ENUM negocio actualizado: " + novos);
            return novos;
        } catch (SQLException e) {
            System.err.println("[Tema] actualizar o ENUM negocio: " + e.getMessage());
            return valores;
        }
    }

    /**
     * O valor do ENUM tal como está na base (ex.: "Ginásio" quando se pede
     * "Ginasio"), ou null se a coluna não o aceita.
     */
    public static String valorAceite(List<String> aceites, String valor) {
        String k = chave(valor);
        for (String v : aceites) {
            if (chave(v).equals(k)) {
                return v;
            }
        }
        return null;
    }
}
