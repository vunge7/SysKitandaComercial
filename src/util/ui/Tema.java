package util.ui;

import java.awt.Color;
import java.text.Normalizer;

/**
 * Tema do negócio: cores e símbolos de todo o sistema conforme a coluna
 * "negocio" da tb_dados_instituicao (Comercial, Farmacia, Lavandaria,
 * Restaurante, Oficina...). Com "Farmacia" tudo fica verde e com a cruz da
 * farmácia; com "Restaurante" bordô e talheres; com "Oficina" grafite, laranja
 * e a chave-inglesa; com "Lavandaria" azul-água e a máquina de lavar. Os outros
 * negócios ficam com o azul do comercial.
 *
 * <p>
 * Aplica-se no arranque (antes de abrir qualquer janela) e de novo ao entrar no
 * sistema, para uma mudança nos dados da instituição valer logo. As cores de
 * estado (verde = livre/sucesso, vermelho = erro/ocupado, laranja = aviso) não
 * mudam: têm significado em qualquer negócio.
 */
public final class Tema {

    public enum Negocio {
        COMERCIAL, FARMACIA, LAVANDARIA, RESTAURANTE, OFICINA
    }

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

    /** Símbolo do negócio (logótipos, cabeçalhos). */
    public static MenuWeb.Simbolo simbolo() {
        switch (actual) {
            case FARMACIA:
                return MenuWeb.Simbolo.FARMACIA;
            case LAVANDARIA:
                return MenuWeb.Simbolo.LAVANDARIA;
            case RESTAURANTE:
                return MenuWeb.Simbolo.TALHERES;
            case OFICINA:
                return MenuWeb.Simbolo.FERRAMENTA;
            default:
                return MenuWeb.Simbolo.CARTEIRA;
        }
    }

    /** Nome a mostrar ("Gestão Comercial", "Farmácia"...). */
    public static String area() {
        switch (actual) {
            case FARMACIA:
                return "Farmácia";
            case LAVANDARIA:
                return "Lavandaria";
            case RESTAURANTE:
                return "Restaurante";
            case OFICINA:
                return "Oficina";
            default:
                return "Gestão Comercial";
        }
    }

    /**
     * Amostra das cores de um negócio sem o aplicar (pré-visualização):
     * {escuro, vivo, destaque}.
     */
    public static Color[] amostra(String negocio) {
        switch (deTexto(negocio)) {
            case FARMACIA:
                return new Color[]{new Color(4, 90, 52), new Color(16, 163, 104), new Color(196, 160, 92)};
            case LAVANDARIA:
                return new Color[]{new Color(8, 76, 97), new Color(6, 182, 212), new Color(196, 160, 92)};
            case RESTAURANTE:
                return new Color[]{new Color(120, 28, 40), new Color(225, 29, 72), new Color(201, 162, 39)};
            case OFICINA:
                return new Color[]{new Color(39, 48, 61), new Color(234, 88, 12), new Color(234, 140, 40)};
            default:
                return new Color[]{new Color(0, 51, 102), new Color(37, 132, 235), new Color(196, 160, 92)};
        }
    }

    /** Lê o negócio da instituição (id 1) e aplica o tema; em caso de erro fica o actual. */
    public static void aplicarDaInstituicao() {
        try {
            // JDBC (leve): no arranque o JPA ainda não precisa de estar iniciado
            entity.TbDadosInstituicao d = (entity.TbDadosInstituicao) new comercial.controller.DadosInstituicaoController(
                    util.BDConexao.getInstancia()).findById(1);
            aplicar(d == null ? null : d.getNegocio());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static Negocio deTexto(String negocio) {
        String n = negocio == null ? "" : Normalizer.normalize(negocio.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
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
        return Negocio.COMERCIAL;
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
}
