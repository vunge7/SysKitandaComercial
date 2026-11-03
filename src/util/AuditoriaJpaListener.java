package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Camada automática de auditoria para JPA (EclipseLink).
 *
 * Registado como listener por omissão de TODAS as entidades em META-INF/orm.xml,
 * regista em log_actividade cada persist/merge/remove feito pelos DAOs/JpaControllers.
 */
public class AuditoriaJpaListener
{

    public void aposInserir( Object entidade )
    {
        registar( "INSERT", entidade );
    }

    public void aposActualizar( Object entidade )
    {
        registar( "UPDATE", entidade );
    }

    public void aposEliminar( Object entidade )
    {
        registar( "DELETE", entidade );
    }

    private static void registar( String accao, Object entidade )
    {
        try
        {
            if ( entidade == null || LogActividadeUtil.isARegistar() )
            {
                return;
            }
            String tabela = nomeTabela( entidade.getClass() );
            if ( LogActividadeUtil.isTabelaIgnorada( tabela ) )
            {
                return;
            }
            Object id = identificador( entidade );
            String accaoLegivel = LogActividadeUtil.accaoDeComando( accao );
            String descricao = LogActividadeUtil.verboDeAccao( accaoLegivel ) + " " + tabela
                    + ( id != null ? " (id " + id + ")" : "" );

            LogActividadeUtil.registar( LogActividadeUtil.MODULO_AUTOMATICO, accaoLegivel, tabela, id, descricao,
                    null, LogActividadeUtil.formatar( retrato( entidade ) ) );
        }
        catch ( Throwable t )
        {
            System.err.println( "[AuditoriaJpa] " + t.getMessage() );
        }
    }

    private static String nomeTabela( Class<?> classe )
    {
        Table table = classe.getAnnotation( Table.class );
        if ( table != null && !table.name().isEmpty() )
        {
            return table.name();
        }
        return classe.getSimpleName();
    }

    static Object identificador( Object entidade )
    {
        if ( entidade == null )
        {
            return null;
        }
        for ( Class<?> c = entidade.getClass(); c != null && c != Object.class; c = c.getSuperclass() )
        {
            for ( Field campo : c.getDeclaredFields() )
            {
                if ( campo.isAnnotationPresent( Id.class ) || campo.isAnnotationPresent( EmbeddedId.class ) )
                {
                    try
                    {
                        campo.setAccessible( true );
                        return campo.get( entidade );
                    }
                    catch ( Exception e )
                    {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // campos simples da entidade; relações (@ManyToOne) aparecem pelo id; listas são ignoradas
    private static Map<String, Object> retrato( Object entidade )
    {
        Map<String, Object> retrato = LogActividadeUtil.novoRetrato();
        for ( Class<?> c = entidade.getClass(); c != null && c != Object.class; c = c.getSuperclass() )
        {
            for ( Field campo : c.getDeclaredFields() )
            {
                int mod = campo.getModifiers();
                if ( Modifier.isStatic( mod ) || Modifier.isTransient( mod ) || campo.getName().startsWith( "_" ) )
                {
                    continue; // inclui campos internos do EclipseLink (_persistence_*)
                }
                Class<?> tipo = campo.getType();
                if ( Collection.class.isAssignableFrom( tipo ) || Map.class.isAssignableFrom( tipo ) )
                {
                    continue;
                }
                try
                {
                    campo.setAccessible( true );
                    Object valor = campo.get( entidade );
                    if ( LogActividadeUtil.isCampoSensivel( campo.getName() ) )
                    {
                        valor = valor == null ? null : "***";
                    }
                    else if ( valor != null && tipo.isAnnotationPresent( Entity.class ) )
                    {
                        valor = identificador( valor );
                    }
                    else if ( valor != null && !simples( valor ) )
                    {
                        continue;
                    }
                    retrato.put( campo.getName(), valor );
                }
                catch ( Exception ignored )
                {
                }
            }
        }
        return retrato;
    }

    private static boolean simples( Object valor )
    {
        return valor instanceof String || valor instanceof Number || valor instanceof Boolean
                || valor instanceof Character || valor instanceof Date || valor instanceof Enum
                || valor instanceof byte[] || valor instanceof BigDecimal || valor instanceof BigInteger;
    }

}
