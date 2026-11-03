package util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regista na tabela log_actividade as acções dos utilizadores.
 *
 * Há duas camadas de registo:
 * <ul>
 * <li><b>Semântica</b>: chamadas explícitas a registar(...) nas operações de negócio
 * (produtos, preços, vendas, caixa...), com descrições legíveis.</li>
 * <li><b>Automática</b>: AuditoriaJdbc (SQL via BDConexao), AuditoriaJpaListener (DAOs JPA)
 * e AuditoriaJanelas (abertura de ecrãs/relatórios) registam tudo o resto, com
 * modulo = MODULO_AUTOMATICO / MODULO_ECRAS / MODULO_RELATORIOS.</li>
 * </ul>
 *
 * Regra principal: o registo de log NUNCA pode interromper a operação de
 * negócio. Qualquer falha é apenas escrita na consola.
 */
public class LogActividadeUtil
{

    // ----------------------- Acções -----------------------
    public static final String CRIAR = "CRIAR";
    public static final String ALTERAR = "ALTERAR";
    public static final String ELIMINAR = "ELIMINAR";
    public static final String ANULAR = "ANULAR";
    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_FALHADO = "LOGIN_FALHADO";
    public static final String LOGOUT = "LOGOUT";
    public static final String IMPRIMIR = "IMPRIMIR";
    public static final String ABERTURA_CAIXA = "ABERTURA_CAIXA";
    public static final String FECHO_CAIXA = "FECHO_CAIXA";
    public static final String ABRIR = "ABRIR";
    public static final String VISUALIZAR = "VISUALIZAR";

    // ----------------------- Módulos ----------------------
    public static final String MODULO_SISTEMA = "Sistema";
    public static final String MODULO_PRODUTOS = "Produtos";
    public static final String MODULO_PRECOS = "Preços";
    public static final String MODULO_VENDAS = "Vendas";
    public static final String MODULO_CAIXA = "Caixa";
    public static final String MODULO_STOCK = "Stock";
    public static final String MODULO_ECRAS = "Ecrãs";
    public static final String MODULO_RELATORIOS = "Relatórios";
    /** Registos técnicos gerados automaticamente (INSERT/UPDATE/DELETE em qualquer tabela). */
    public static final String MODULO_AUTOMATICO = "Automático";

    /** Tabelas que nunca são registadas pela camada automática. */
    private static final Set<String> TABELAS_IGNORADAS = new HashSet<>( Arrays.asList(
            "log_actividade", "log"
    ) );

    private static final String INSERT = "INSERT INTO log_actividade( "
            + " data_hora, cod_usuario, nome_usuario, modulo, formulario, accao, entidade, id_registo, "
            + " descricao, valor_anterior, valor_novo, nome_maquina, ip_maquina "
            + ") VALUES ( NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    private static final int TAMANHO_MAX_VALORES = 20000;

    // pacotes onde vivem os formulários: usados para descobrir de que ecrã veio a acção
    private static final String[] PACOTES_FORMULARIOS =
    {
        "visao.", "lista.", "rh.visao.", "tesouraria.", "hotel."
    };

    // evita que o próprio INSERT em log_actividade seja auditado (recursão)
    private static final ThreadLocal<Boolean> A_REGISTAR = new ThreadLocal<>();

    private static boolean falhaReportada = false;

    /**
     * Regista uma acção do utilizador da sessão actual (SessaoUtil).
     */
    public static void registar( String modulo, String accao, String entidade, Object idRegisto,
            String descricao, String valorAnterior, String valorNovo )
    {
        inserir( SessaoUtil.getCodUsuario(), SessaoUtil.getNomeUsuario(), modulo, null, accao,
                entidade, idRegisto, descricao, valorAnterior, valorNovo );
    }

    public static void registar( String modulo, String accao, String entidade, Object idRegisto, String descricao )
    {
        registar( modulo, accao, entidade, idRegisto, descricao, null, null );
    }

    /**
     * Regista uma acção indicando explicitamente o utilizador (ex.: tentativa de login
     * falhada, onde ainda não existe sessão).
     */
    public static void registar( Integer codUsuario, String nomeUsuario, String modulo, String accao,
            String entidade, Object idRegisto, String descricao, String valorAnterior, String valorNovo )
    {
        inserir( codUsuario, nomeUsuario, modulo, null, accao, entidade, idRegisto, descricao, valorAnterior, valorNovo );
    }

    /**
     * Regista indicando explicitamente o formulário (usado quando a origem não está
     * na pilha de chamadas, ex.: eventos de janelas).
     */
    public static void registarComFormulario( String modulo, String formulario, String accao, String entidade,
            Object idRegisto, String descricao )
    {
        inserir( SessaoUtil.getCodUsuario(), SessaoUtil.getNomeUsuario(), modulo, formulario, accao,
                entidade, idRegisto, descricao, null, null );
    }

    /** Reimpressão (2ª via) de um documento de venda: ponto sensível, fica sempre registada. */
    public static void registarReimpressao( String codFact, Object idVenda )
    {
        registar( MODULO_VENDAS, IMPRIMIR, "tb_venda", idVenda != null ? idVenda : codFact,
                "Reimprimiu (2ª via) o documento: " + codFact );
    }

    /** true enquanto este thread está a gravar um log (para a camada automática se ignorar). */
    public static boolean isARegistar()
    {
        return Boolean.TRUE.equals( A_REGISTAR.get() );
    }

    public static boolean isTabelaIgnorada( String tabela )
    {
        return tabela == null || TABELAS_IGNORADAS.contains( tabela.toLowerCase() );
    }

    private static void inserir( Integer codUsuario, String nomeUsuario, String modulo, String formulario,
            String accao, String entidade, Object idRegisto, String descricao, String valorAnterior, String valorNovo )
    {
        if ( isARegistar() )
        {
            return;
        }
        A_REGISTAR.set( Boolean.TRUE );
        try ( PreparedStatement stmt = BDConexao.getInstancia().getConnectionAtiva().prepareStatement( INSERT ) )
        {
            int i = 1;
            if ( codUsuario == null )
            {
                stmt.setNull( i++, Types.INTEGER );
            }
            else
            {
                stmt.setInt( i++, codUsuario );
            }
            stmt.setString( i++, cortar( nomeUsuario, 100 ) );
            stmt.setString( i++, cortar( modulo, 50 ) );
            stmt.setString( i++, cortar( formulario != null ? formulario : detectarFormulario(), 100 ) );
            stmt.setString( i++, cortar( accao, 30 ) );
            stmt.setString( i++, cortar( entidade, 50 ) );
            stmt.setString( i++, idRegisto == null ? null : cortar( String.valueOf( idRegisto ), 50 ) );
            stmt.setString( i++, cortar( descricao, 500 ) );
            stmt.setString( i++, cortar( valorAnterior, TAMANHO_MAX_VALORES ) );
            stmt.setString( i++, cortar( valorNovo, TAMANHO_MAX_VALORES ) );
            stmt.setString( i++, cortar( SessaoUtil.getNomeMaquina(), 75 ) );
            stmt.setString( i++, cortar( SessaoUtil.getIpMaquina(), 75 ) );
            stmt.executeUpdate();
        }
        catch ( Exception e )
        {
            // só mostra o erro completo uma vez (ex.: tabela ainda não criada)
            if ( !falhaReportada )
            {
                falhaReportada = true;
                System.err.println( "[LogActividade] Falha ao registar actividade: " + e.getMessage() );
                e.printStackTrace();
            }
        }
        finally
        {
            A_REGISTAR.remove();
        }
    }

    /**
     * Compara dois "retratos" de um registo (campo -> valor) e devolve
     * { valorAnterior, valorNovo } apenas com os campos que mudaram,
     * no formato "campo=valor; campo=valor". Devolve null se nada mudou.
     */
    public static String[] diferencas( Map<String, Object> antes, Map<String, Object> depois )
    {
        StringBuilder anterior = new StringBuilder();
        StringBuilder novo = new StringBuilder();

        for ( Map.Entry<String, Object> campo : depois.entrySet() )
        {
            String valorAntes = normalizar( antes == null ? null : antes.get( campo.getKey() ) );
            String valorDepois = normalizar( campo.getValue() );

            if ( !Objects.equals( valorAntes, valorDepois ) )
            {
                juntar( anterior, campo.getKey(), valorAntes );
                juntar( novo, campo.getKey(), valorDepois );
            }
        }

        if ( novo.length() == 0 )
        {
            return null;
        }
        return new String[]
        {
            anterior.toString(), novo.toString()
        };
    }

    /**
     * Converte um "retrato" (campo -> valor) em texto "campo=valor; ...".
     */
    public static String formatar( Map<String, Object> campos )
    {
        StringBuilder sb = new StringBuilder();
        for ( Map.Entry<String, Object> campo : campos.entrySet() )
        {
            juntar( sb, campo.getKey(), normalizar( campo.getValue() ) );
        }
        return sb.toString();
    }

    public static Map<String, Object> novoRetrato()
    {
        return new LinkedHashMap<>();
    }

    // ------------------------------------------------------------------
    // Nunca expor SQL ao utilizador (revela a estrutura da base de dados).
    // A camada automática grava acções/descrições legíveis; os métodos *Visivel
    // traduzem também registos antigos que ainda contenham SQL.
    // ------------------------------------------------------------------
    private static final Pattern COMANDO_SQL = Pattern.compile(
            "^\\s*(INSERT|UPDATE|DELETE|REPLACE|TRUNCATE|DROP|ALTER|CREATE)\\b", Pattern.CASE_INSENSITIVE );

    private static final Pattern DESCRICAO_TECNICA = Pattern.compile(
            "^\\s*(INSERT|UPDATE|DELETE|REPLACE|TRUNCATE|DROP|ALTER|CREATE) em (.*)$",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL );

    public static final String ESTRUTURA = "ESTRUTURA";

    /** INSERT -> CRIAR, UPDATE/REPLACE -> ALTERAR, DELETE/TRUNCATE/DROP -> ELIMINAR, ALTER/CREATE -> ESTRUTURA. */
    public static String accaoDeComando( String comando )
    {
        if ( comando == null )
        {
            return null;
        }
        switch ( comando.trim().toUpperCase() )
        {
            case "INSERT":
                return CRIAR;
            case "UPDATE":
            case "REPLACE":
                return ALTERAR;
            case "DELETE":
            case "TRUNCATE":
            case "DROP":
                return ELIMINAR;
            case "ALTER":
            case "CREATE":
                return ESTRUTURA;
            default:
                return comando;
        }
    }

    /** "Criou registo em", "Alterou registo em", ... para a camada automática. */
    public static String verboDeAccao( String accao )
    {
        switch ( accao == null ? "" : accao )
        {
            case CRIAR:
                return "Criou registo em";
            case ALTERAR:
                return "Alterou registo em";
            case ELIMINAR:
                return "Eliminou registo em";
            case ESTRUTURA:
                return "Alterou a estrutura de";
            default:
                return "Operação em";
        }
    }

    public static boolean isComandoSql( String texto )
    {
        return texto != null && COMANDO_SQL.matcher( texto ).find();
    }

    /** Acção a apresentar (registos antigos podem ter INSERT/UPDATE/...). */
    public static String accaoVisivel( String accao )
    {
        return isComandoSql( accao ) ? accaoDeComando( accao.trim() ) : accao;
    }

    /** Descrição a apresentar: converte "INSERT em tb_x (1 linha) | WHERE ..." e remove o WHERE. */
    public static String descricaoVisivel( String descricao )
    {
        if ( descricao == null )
        {
            return null;
        }
        Matcher m = DESCRICAO_TECNICA.matcher( descricao );
        if ( !m.matches() )
        {
            return descricao;
        }
        String resto = m.group( 2 );
        int where = resto.indexOf( " | WHERE" );
        if ( where >= 0 )
        {
            resto = resto.substring( 0, where );
        }
        resto = resto.replace( "(JPA, id ", "(id " ).replace( " (JPA)", "" );
        return verboDeAccao( accaoDeComando( m.group( 1 ) ) ) + " " + resto.trim();
    }

    /** Valores a apresentar: oculta qualquer comando SQL gravado em registos antigos. */
    public static String valoresVisiveis( String valores )
    {
        return isComandoSql( valores ) ? null : valores;
    }

    /** Campos cujo valor nunca deve ir para o log (senhas). */
    public static boolean isCampoSensivel( String nome )
    {
        if ( nome == null )
        {
            return false;
        }
        String n = nome.toLowerCase();
        return n.contains( "senha" ) || n.contains( "password" ) || n.contains( "pass_word" ) || n.equals( "pass" );
    }

    // -------------------------------------------------------------------

    private static void juntar( StringBuilder sb, String campo, String valor )
    {
        if ( sb.length() > 0 )
        {
            sb.append( "; " );
        }
        sb.append( campo ).append( "=" ).append( valor == null ? "" : valor );
    }

    // 500, 500.0 e 500.00 devem ser considerados iguais
    public static String normalizar( Object valor )
    {
        if ( valor == null )
        {
            return null;
        }
        if ( valor instanceof Number )
        {
            try
            {
                return new BigDecimal( valor.toString() ).stripTrailingZeros().toPlainString();
            }
            catch ( NumberFormatException e )
            {
                return valor.toString();
            }
        }
        if ( valor instanceof byte[] )
        {
            return "<bytes:" + ( (byte[]) valor ).length + ">";
        }
        return valor.toString().trim();
    }

    /**
     * Nome simples do formulário (pacotes visao, lista, ...) na pilha de chamadas,
     * ou "[nome do thread]" quando a acção não veio de um ecrã (ex.: tarefas agendadas).
     */
    static String detectarFormulario()
    {
        for ( StackTraceElement elemento : new Throwable().getStackTrace() )
        {
            String classe = elemento.getClassName();
            for ( String pacote : PACOTES_FORMULARIOS )
            {
                if ( classe.startsWith( pacote ) )
                {
                    // remove classes internas/anónimas: visao.ProdutosVisao$3 -> ProdutosVisao
                    String simples = classe.substring( classe.lastIndexOf( '.' ) + 1 );
                    int interna = simples.indexOf( '$' );
                    return interna > 0 ? simples.substring( 0, interna ) : simples;
                }
            }
        }
        return "[" + Thread.currentThread().getName() + "]";
    }

    static String cortar( String texto, int tamanho )
    {
        if ( texto == null || texto.length() <= tamanho )
        {
            return texto;
        }
        return texto.substring( 0, tamanho );
    }

}
