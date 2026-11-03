package util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Camada automática de auditoria para JDBC.
 *
 * Envolve a Connection da BDConexao num proxy: todos os Statement/PreparedStatement
 * criados a partir dela registam em log_actividade cada INSERT/UPDATE/DELETE/REPLACE
 * (e DDL: TRUNCATE/DROP/ALTER/CREATE) executado com sucesso.
 *
 * Os registos ficam com modulo = LogActividadeUtil.MODULO_AUTOMATICO, acção legível
 * (CRIAR, ALTERAR, ELIMINAR, ESTRUTURA) e descrição do tipo "Alterou registo em tb_x".
 * O comando SQL nunca é gravado: revelaria a estrutura da base de dados.
 */
public class AuditoriaJdbc
{

    private static final Pattern TIPO = Pattern.compile(
            "^\\s*(INSERT|UPDATE|DELETE|REPLACE|TRUNCATE|DROP|ALTER|CREATE)\\b", Pattern.CASE_INSENSITIVE );

    private static final Pattern TABELA = Pattern.compile(
            "^\\s*(?:INSERT(?:\\s+(?:LOW_PRIORITY|DELAYED|HIGH_PRIORITY|IGNORE))*\\s+(?:INTO\\s+)?"
            + "|REPLACE(?:\\s+(?:LOW_PRIORITY|DELAYED))*\\s+(?:INTO\\s+)?"
            + "|UPDATE(?:\\s+(?:LOW_PRIORITY|IGNORE))*\\s+"
            + "|DELETE(?:\\s+(?:LOW_PRIORITY|QUICK|IGNORE))*\\s+FROM\\s+"
            + "|TRUNCATE\\s+(?:TABLE\\s+)?"
            + "|DROP\\s+(?:TEMPORARY\\s+)?(?:TABLE|VIEW|TRIGGER|PROCEDURE|FUNCTION|DATABASE)\\s+(?:IF\\s+EXISTS\\s+)?"
            + "|ALTER\\s+(?:TABLE|VIEW)\\s+"
            + "|CREATE\\s+(?:TEMPORARY\\s+)?(?:TABLE|VIEW|TRIGGER|PROCEDURE|FUNCTION|DATABASE)\\s+(?:IF\\s+NOT\\s+EXISTS\\s+)?)"
            + "`?([\\w.]+)`?",
            Pattern.CASE_INSENSITIVE );

    // primeira condição do WHERE: "codigo = 5", "cod_fact = 'FR 1'"
    private static final Pattern WHERE_ID = Pattern.compile(
            "\\bWHERE\\s*\\(?\\s*`?[\\w.]+`?\\s*=\\s*('[^']*'|[\\w.-]+)", Pattern.CASE_INSENSITIVE );

    // rotinas que gravam dados: funções "SELECT VENDA_CRIAR(...)" e procedimentos "CALL x(...)"
    private static final Pattern ROTINA = Pattern.compile(
            "^\\s*(?:SELECT\\s+`?(\\w+_CRIAR)`?\\s*\\(|\\{?\\s*CALL\\s+`?(\\w+)`?)", Pattern.CASE_INSENSITIVE );

    // procedimentos só de leitura (não são actividade)
    private static final Pattern ROTINA_LEITURA = Pattern.compile( "GET|EXIST|LOGIN", Pattern.CASE_INSENSITIVE );

    // rotina -> tabela afectada (as restantes usam o próprio nome)
    private static final Map<String, String> TABELA_DA_ROTINA = new java.util.HashMap<>();

    static
    {
        TABELA_DA_ROTINA.put( "venda_criar", "tb_venda" );
        TABELA_DA_ROTINA.put( "item_venda_criar", "tb_item_venda" );
        TABELA_DA_ROTINA.put( "nota_venda_criar", "notas" );
        TABELA_DA_ROTINA.put( "pedido_criar", "tb_pedido" );
        TABELA_DA_ROTINA.put( "item_pedido_criar", "tb_item_pedidos" );
        TABELA_DA_ROTINA.put( "dm_procedimento_usuario", "tb_usuario" );
        TABELA_DA_ROTINA.put( "dm_procedimento_cliente", "tb_cliente" );
        TABELA_DA_ROTINA.put( "criar_forma_pagamento_proc", "forma_pagamento_item" );
    }

    // têm registo próprio e legível (ver MetodosUtil.esvaziaBaseDados)
    private static final java.util.Set<String> ROTINAS_COM_REGISTO_PROPRIO = new java.util.HashSet<>(
            java.util.Arrays.asList( "esvazia_banco_dados" ) );

    public static Connection envolver( Connection connection )
    {
        if ( connection == null || Proxy.isProxyClass( connection.getClass() ) )
        {
            return connection;
        }
        return (Connection) Proxy.newProxyInstance( AuditoriaJdbc.class.getClassLoader(),
                new Class<?>[]
                {
                    Connection.class
                }, new ConnectionHandler( connection ) );
    }

    // -------------------------------------------------------------------

    private static class ConnectionHandler implements InvocationHandler
    {

        private final Connection alvo;

        ConnectionHandler( Connection alvo )
        {
            this.alvo = alvo;
        }

        @Override
        public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
        {
            String nome = method.getName();
            if ( "equals".equals( nome ) && args != null && args.length == 1 )
            {
                return proxy == args[ 0 ];
            }
            if ( "unwrap".equals( nome ) && args != null && args[ 0 ] == Connection.class )
            {
                return proxy;
            }

            Object resultado = chamar( alvo, method, args );

            if ( resultado instanceof PreparedStatement && "prepareStatement".equals( nome ) )
            {
                return Proxy.newProxyInstance( AuditoriaJdbc.class.getClassLoader(),
                        new Class<?>[]
                        {
                            PreparedStatement.class
                        }, new StatementHandler( (Statement) resultado, (String) args[ 0 ] ) );
            }
            if ( resultado instanceof Statement && "createStatement".equals( nome ) )
            {
                return Proxy.newProxyInstance( AuditoriaJdbc.class.getClassLoader(),
                        new Class<?>[]
                        {
                            Statement.class
                        }, new StatementHandler( (Statement) resultado, null ) );
            }
            return resultado;
        }

    }

    private static class StatementHandler implements InvocationHandler
    {

        private final Statement alvo;
        private final String sqlPreparado;
        private final Map<Integer, Object> parametros = new TreeMap<>();
        private int lote = 0;

        StatementHandler( Statement alvo, String sqlPreparado )
        {
            this.alvo = alvo;
            this.sqlPreparado = sqlPreparado;
        }

        @Override
        public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
        {
            String nome = method.getName();

            // guarda os parâmetros: setString(1, "x"), setInt(2, 5), setNull(3, ...) ...
            if ( sqlPreparado != null && nome.startsWith( "set" ) && args != null && args.length >= 2
                    && args[ 0 ] instanceof Integer )
            {
                parametros.put( (Integer) args[ 0 ], "setNull".equals( nome ) ? null : args[ 1 ] );
            }
            else if ( "clearParameters".equals( nome ) )
            {
                parametros.clear();
            }
            else if ( "addBatch".equals( nome ) )
            {
                lote++;
            }
            else if ( "equals".equals( nome ) && args != null && args.length == 1 )
            {
                return proxy == args[ 0 ];
            }

            Object resultado = chamar( alvo, method, args );

            if ( "executeQuery".equals( nome ) )
            {
                // leituras não são auditadas, excepto funções que gravam (SELECT VENDA_CRIAR(...))
                String sql = ( args != null && args.length > 0 && args[ 0 ] instanceof String )
                        ? (String) args[ 0 ] : sqlPreparado;
                if ( sql != null && ROTINA.matcher( sql ).find() )
                {
                    auditar( sql, null, -1 );
                }
            }
            else if ( nome.startsWith( "execute" ) )
            {
                String sql = ( args != null && args.length > 0 && args[ 0 ] instanceof String )
                        ? (String) args[ 0 ] : sqlPreparado;
                long linhas = linhasAfectadas( nome, resultado );
                if ( "executeBatch".equals( nome ) )
                {
                    lote = 0;
                }
                auditar( sql, sql == sqlPreparado ? parametros : null, linhas );
            }
            return resultado;
        }

        private long linhasAfectadas( String nome, Object resultado )
        {
            try
            {
                if ( resultado instanceof Integer || resultado instanceof Long )
                {
                    return ( (Number) resultado ).longValue();
                }
                if ( resultado instanceof int[] )
                {
                    long total = 0;
                    for ( int n : (int[]) resultado )
                    {
                        total += Math.max( n, 1 ); // SUCCESS_NO_INFO = -2
                    }
                    return total;
                }
                if ( resultado instanceof long[] )
                {
                    long total = 0;
                    for ( long n : (long[]) resultado )
                    {
                        total += Math.max( n, 1 );
                    }
                    return total;
                }
                if ( "execute".equals( nome ) )
                {
                    return alvo.getUpdateCount();
                }
            }
            catch ( Exception ignored )
            {
            }
            return -1;
        }

    }

    private static Object chamar( Object alvo, Method method, Object[] args ) throws Throwable
    {
        try
        {
            return method.invoke( alvo, args );
        }
        catch ( InvocationTargetException e )
        {
            throw e.getCause();
        }
    }

    /**
     * Regista um comando executado. Nunca lança excepções.
     */
    private static void auditar( String sql, Map<Integer, Object> parametros, long linhas )
    {
        try
        {
            if ( sql == null || LogActividadeUtil.isARegistar() )
            {
                return;
            }
            Matcher rotina = ROTINA.matcher( sql );
            if ( rotina.find() )
            {
                auditarRotina( rotina.group( 1 ) != null ? rotina.group( 1 ) : rotina.group( 2 ), rotina.group( 1 ) != null );
                return;
            }
            Matcher tipo = TIPO.matcher( sql );
            if ( !tipo.find() )
            {
                return; // SELECT, SET, CALL, ...
            }
            String accao = tipo.group( 1 ).toUpperCase();

            // UPDATE/DELETE que não afectaram nenhuma linha não são actividade
            if ( linhas == 0 && ( "UPDATE".equals( accao ) || "DELETE".equals( accao ) ) )
            {
                return;
            }

            String tabela = null;
            Matcher mTabela = TABELA.matcher( sql );
            if ( mTabela.find() )
            {
                tabela = mTabela.group( 1 );
                tabela = tabela.substring( tabela.lastIndexOf( '.' ) + 1 );
            }
            if ( LogActividadeUtil.isTabelaIgnorada( tabela ) )
            {
                return;
            }

            // o SQL serve apenas para obter o nº do registo; NUNCA é gravado (expõe a estrutura da BD)
            String idRegisto = null;
            Matcher mId = WHERE_ID.matcher( substituirParametros( sql, parametros ) );
            if ( mId.find() )
            {
                idRegisto = mId.group( 1 ).replace( "'", "" );
            }

            String accaoLegivel = LogActividadeUtil.accaoDeComando( accao );
            String descricao = LogActividadeUtil.verboDeAccao( accaoLegivel ) + " " + tabela
                    + ( linhas > 1 ? " (" + linhas + " registos)" : "" );

            LogActividadeUtil.registar( LogActividadeUtil.MODULO_AUTOMATICO, accaoLegivel, tabela, idRegisto,
                    descricao, null, null );
        }
        catch ( Throwable t )
        {
            System.err.println( "[AuditoriaJdbc] " + t.getMessage() );
        }
    }

    /**
     * Regista a execução de uma rotina que grava dados. Funções *_CRIAR criam registos;
     * procedimentos (CALL) podem criar ou alterar, por isso ficam como "Gravou dados em".
     */
    private static void auditarRotina( String nomeRotina, boolean funcaoCriar )
    {
        String rotina = nomeRotina.toLowerCase();
        if ( ROTINAS_COM_REGISTO_PROPRIO.contains( rotina ) || ( !funcaoCriar && ROTINA_LEITURA.matcher( rotina ).find() ) )
        {
            return;
        }
        String tabela = TABELA_DA_ROTINA.containsKey( rotina )
                ? TABELA_DA_ROTINA.get( rotina )
                : rotina.replaceFirst( "_criar$", "" ).replaceFirst( "^dm_procedimento_", "" );
        if ( LogActividadeUtil.isTabelaIgnorada( tabela ) )
        {
            return;
        }
        String accao = funcaoCriar ? LogActividadeUtil.CRIAR : LogActividadeUtil.ALTERAR;
        String descricao = funcaoCriar ? LogActividadeUtil.verboDeAccao( accao ) + " " + tabela : "Gravou dados em " + tabela;
        LogActividadeUtil.registar( LogActividadeUtil.MODULO_AUTOMATICO, accao, tabela, null, descricao, null, null );
    }

    // troca cada "?" (fora de aspas) pelo valor do parâmetro correspondente
    private static String substituirParametros( String sql, Map<Integer, Object> parametros )
    {
        if ( parametros == null || parametros.isEmpty() || sql.indexOf( '?' ) < 0 )
        {
            return sql;
        }
        StringBuilder sb = new StringBuilder( sql.length() + 64 );
        int indice = 1;
        char aspas = 0;
        for ( int i = 0; i < sql.length(); i++ )
        {
            char c = sql.charAt( i );
            if ( aspas != 0 )
            {
                if ( c == aspas )
                {
                    aspas = 0;
                }
                sb.append( c );
            }
            else if ( c == '\'' || c == '"' || c == '`' )
            {
                aspas = c;
                sb.append( c );
            }
            else if ( c == '?' )
            {
                sb.append( literal( parametros.get( indice++ ) ) );
            }
            else
            {
                sb.append( c );
            }
        }
        return sb.toString();
    }

    private static String literal( Object valor )
    {
        if ( valor == null )
        {
            return "NULL";
        }
        if ( valor instanceof Number || valor instanceof Boolean )
        {
            return LogActividadeUtil.normalizar( valor );
        }
        if ( valor instanceof byte[] )
        {
            return LogActividadeUtil.normalizar( valor );
        }
        return "'" + LogActividadeUtil.cortar( valor.toString(), 500 ).replace( "'", "''" ) + "'";
    }

}
