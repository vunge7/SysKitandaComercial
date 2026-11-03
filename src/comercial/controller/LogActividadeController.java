package comercial.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.LogActividade;
import util.BDConexao;
import util.LogActividadeUtil;

/**
 * Consultas (só leitura) à tabela log_actividade, usadas pelo ecrã LogActividadeVisao.
 * A gravação é feita por util.LogActividadeUtil.
 */
public class LogActividadeController
{

    private final BDConexao conexao;

    public LogActividadeController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    /**
     * Critérios de pesquisa. Campos null/vazios/0 significam "todos".
     */
    public static class Filtro
    {

        public Date dataInicio;
        public Date dataFim;
        public int codUsuario;
        public String modulo;
        public String accao;
        public String texto;
        public boolean incluirAutomaticos = true;
    }

    public List<LogActividade> listar( Filtro filtro, int offset, int limite ) throws SQLException
    {
        List<Object> parametros = new ArrayList<>();
        String sql = "SELECT * FROM log_actividade " + where( filtro, parametros )
                + " ORDER BY data_hora DESC, pk_log_actividade DESC LIMIT ? OFFSET ?";
        parametros.add( limite );
        parametros.add( offset );

        List<LogActividade> lista = new ArrayList<>();
        try ( PreparedStatement ps = preparar( sql, parametros ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                lista.add( mapear( rs ) );
            }
        }
        return lista;
    }

    public int contar( Filtro filtro ) throws SQLException
    {
        List<Object> parametros = new ArrayList<>();
        String sql = "SELECT COUNT(*) FROM log_actividade " + where( filtro, parametros );
        try ( PreparedStatement ps = preparar( sql, parametros ); ResultSet rs = ps.executeQuery() )
        {
            return rs.next() ? rs.getInt( 1 ) : 0;
        }
    }

    /** Valores distintos de uma coluna (módulos ou acções) para os filtros. */
    public List<String> listarDistintos( String coluna ) throws SQLException
    {
        if ( !"modulo".equals( coluna ) && !"accao".equals( coluna ) )
        {
            throw new IllegalArgumentException( coluna );
        }
        // TreeSet: ordena e junta valores antigos (INSERT) com os actuais (CRIAR)
        java.util.TreeSet<String> valores = new java.util.TreeSet<>();
        String sql = "SELECT DISTINCT " + coluna + " FROM log_actividade WHERE " + coluna + " IS NOT NULL";
        try ( PreparedStatement ps = conexao.getConnectionAtiva().prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                String valor = rs.getString( 1 );
                valores.add( "accao".equals( coluna ) ? LogActividadeUtil.accaoVisivel( valor ) : valor );
            }
        }
        return new ArrayList<>( valores );
    }

    /** Utilizadores (codigo -> nome) para o filtro. */
    public Map<Integer, String> listarUsuarios() throws SQLException
    {
        Map<Integer, String> usuarios = new LinkedHashMap<>();
        String sql = "SELECT codigo, nome, userName FROM tb_usuario ORDER BY nome";
        try ( PreparedStatement ps = conexao.getConnectionAtiva().prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                String nome = rs.getString( "nome" );
                if ( nome == null || nome.trim().isEmpty() )
                {
                    nome = rs.getString( "userName" );
                }
                usuarios.put( rs.getInt( "codigo" ), nome );
            }
        }
        return usuarios;
    }

    // ---------------------------------------------------------------------

    private String where( Filtro f, List<Object> parametros )
    {
        StringBuilder sb = new StringBuilder( " WHERE 1 = 1 " );
        if ( f.dataInicio != null )
        {
            sb.append( " AND data_hora >= ? " );
            parametros.add( new Timestamp( f.dataInicio.getTime() ) );
        }
        if ( f.dataFim != null )
        {
            sb.append( " AND data_hora <= ? " );
            parametros.add( new Timestamp( f.dataFim.getTime() ) );
        }
        if ( f.codUsuario > 0 )
        {
            sb.append( " AND cod_usuario = ? " );
            parametros.add( f.codUsuario );
        }
        if ( !vazio( f.modulo ) )
        {
            sb.append( " AND modulo = ? " );
            parametros.add( f.modulo );
        }
        if ( !vazio( f.accao ) )
        {
            // inclui os registos antigos gravados com o nome do comando (ex.: CRIAR -> também INSERT)
            List<String> variantes = variantesAccao( f.accao );
            sb.append( " AND accao IN (" );
            for ( int i = 0; i < variantes.size(); i++ )
            {
                sb.append( i == 0 ? "?" : ", ?" );
                parametros.add( variantes.get( i ) );
            }
            sb.append( ") " );
        }
        if ( !f.incluirAutomaticos )
        {
            sb.append( " AND ( modulo IS NULL OR modulo <> ? ) " );
            parametros.add( LogActividadeUtil.MODULO_AUTOMATICO );
        }
        if ( !vazio( f.texto ) )
        {
            sb.append( " AND ( descricao LIKE ? OR formulario LIKE ? OR entidade LIKE ? "
                    + " OR id_registo LIKE ? OR nome_usuario LIKE ? OR nome_maquina LIKE ? ) " );
            String like = "%" + f.texto.trim() + "%";
            for ( int i = 0; i < 6; i++ )
            {
                parametros.add( like );
            }
        }
        return sb.toString();
    }

    private static List<String> variantesAccao( String accao )
    {
        List<String> variantes = new ArrayList<>();
        variantes.add( accao );
        for ( String comando : new String[]
        {
            "INSERT", "UPDATE", "REPLACE", "DELETE", "TRUNCATE", "DROP", "ALTER", "CREATE"
        } )
        {
            if ( accao.equals( LogActividadeUtil.accaoDeComando( comando ) ) )
            {
                variantes.add( comando );
            }
        }
        return variantes;
    }

    private PreparedStatement preparar( String sql, List<Object> parametros ) throws SQLException
    {
        PreparedStatement ps = conexao.getConnectionAtiva().prepareStatement( sql );
        for ( int i = 0; i < parametros.size(); i++ )
        {
            ps.setObject( i + 1, parametros.get( i ) );
        }
        return ps;
    }

    private static boolean vazio( String texto )
    {
        return texto == null || texto.trim().isEmpty();
    }

    private static LogActividade mapear( ResultSet rs ) throws SQLException
    {
        LogActividade log = new LogActividade();
        log.setPkLogActividade( rs.getLong( "pk_log_actividade" ) );
        log.setDataHora( rs.getTimestamp( "data_hora" ) );
        int codUsuario = rs.getInt( "cod_usuario" );
        log.setCodUsuario( rs.wasNull() ? null : codUsuario );
        log.setNomeUsuario( rs.getString( "nome_usuario" ) );
        log.setModulo( rs.getString( "modulo" ) );
        log.setFormulario( rs.getString( "formulario" ) );
        log.setAccao( rs.getString( "accao" ) );
        log.setEntidade( rs.getString( "entidade" ) );
        log.setIdRegisto( rs.getString( "id_registo" ) );
        log.setDescricao( rs.getString( "descricao" ) );
        log.setValorAnterior( rs.getString( "valor_anterior" ) );
        log.setValorNovo( rs.getString( "valor_novo" ) );
        log.setNomeMaquina( rs.getString( "nome_maquina" ) );
        log.setIpMaquina( rs.getString( "ip_maquina" ) );
        return log;
    }

}
