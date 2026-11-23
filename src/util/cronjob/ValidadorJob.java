/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.cronjob;


import comercial.caixa.FechoGeral;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.BDConexao;
import util.MetodosUtil;
import util.email.EnvioEmailUtil;

/**
 * Fecho automático das caixas + envio do e-mail, uma vez por dia à hora do fecho:
 * tb_dados_instituicao.hora_fecho_geral, ou a hora reagendada para hoje no aviso
 * "Faltam x minutos para o fecho automático". O Quartz chama-o a cada minuto.
 * O computador já não é desligado no fim.
 *
 * @author Domingos Dala Vunge
 */
@DisallowConcurrentExecution
public class ValidadorJob implements Job
{

    /** Só corre se o computador estiver ligado nos primeiros minutos depois da hora. */
    private static final long JANELA_MS = 5 * 60000L;

    /** Recurso (comportamento antigo) se não for possível ler a hora da BD. */
    static volatile Date horaTerminoVenda;

    /** Data (00:00) do último fecho automático feito neste computador. */
    private static volatile long ultimoDia;

    @Override
    public void execute( JobExecutionContext jec ) throws JobExecutionException
    {
        Date agora = new Date();
        long hoje = inicioDoDia( agora );
        if ( hoje == ultimoDia )
        {
            return; // já feito hoje
        }
        Date fim = horaDoFecho();
        if ( fim == null || agora.before( fim ) || agora.getTime() >= fim.getTime() + JANELA_MS )
        {
            return; // não é a hora
        }
        ultimoDia = hoje; // antes de correr: não repete mesmo que demore ou falhe a meio
        // os relatórios diários (PDF + e-mail "Relatório Diário") são os da lavandaria:
        // só saem quando o negócio nos dados da instituição é "Lavandaria"
        boolean lavandaria = negocioLavandaria();
        System.out.println( "[Cronjob] fecho automático " + agora + ( lavandaria ? " + relatórios da lavandaria" : "" ) );
        MetodosUtil.fechoAutomatico( lavandaria );
        if ( lavandaria )
        {
            EnvioEmailUtil.inicio();
        }
        MetodosUtil.actualizarEstadoLog( "OFF" );
        // Desligamento automático removido: o computador continua ligado após o fecho geral.
    }

    /** tb_dados_instituicao.negocio = "Lavandaria"? */
    private static boolean negocioLavandaria()
    {
        try ( Connection cn = BDConexao.novaLigacao();
                java.sql.Statement st = cn.createStatement();
                java.sql.ResultSet rs = st.executeQuery( "SELECT negocio FROM tb_dados_instituicao ORDER BY idDadosInsitiuicao LIMIT 1" ) )
        {
            return rs.next() && "Lavandaria".equalsIgnoreCase( rs.getString( 1 ) == null ? "" : rs.getString( 1 ).trim() );
        }
        catch ( Exception e )
        {
            System.err.println( "[Cronjob] negócio não lido: " + e );
            return false;
        }
    }

    /** Hora do fecho de hoje (reagendada ou hora_fecho_geral); recurso: hora de término da venda. */
    private static Date horaDoFecho()
    {
        try ( Connection cn = BDConexao.novaLigacao() )
        {
            return FechoGeral.config( cn ).fimHoje();
        }
        catch ( Exception e )
        {
            System.err.println( "[Cronjob] hora_fecho_geral não lida: " + e );
        }
        Date h = horaTerminoVenda;
        if ( h == null )
        {
            return null;
        }
        Calendar k = Calendar.getInstance();
        k.set( Calendar.HOUR_OF_DAY, h.getHours() );
        k.set( Calendar.MINUTE, h.getMinutes() );
        k.set( Calendar.SECOND, 0 );
        k.set( Calendar.MILLISECOND, 0 );
        return k.getTime();
    }

    private static long inicioDoDia( Date d )
    {
        Calendar k = Calendar.getInstance();
        k.setTime( d );
        k.set( Calendar.HOUR_OF_DAY, 0 );
        k.set( Calendar.MINUTE, 0 );
        k.set( Calendar.SECOND, 0 );
        k.set( Calendar.MILLISECOND, 0 );
        return k.getTimeInMillis();
    }

}
