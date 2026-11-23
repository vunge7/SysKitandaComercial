/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.cronjob;


import java.sql.Connection;
import entity.TbDadosInstituicao;
import comercial.controller.DadosInstituicaoController;
import java.util.Date;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class QuartzApp
{
    
    /**
     * Agenda o fecho automático + envio do e-mail (ValidadorJob). O trigger verifica
     * a cada minuto; o job só corre à hora do fecho de hoje: hora_fecho_geral, ou a
     * reagendada no aviso "Faltam x minutos para o fecho automático".
     */
    public static void executarCronjob( Class classname, TbDadosInstituicao d )
    {
        ValidadorJob.horaTerminoVenda = d == null ? null : d.getHoraTerminoVenda(); // recurso se a BD falhar
        SchedulerFactory shedFact = new StdSchedulerFactory();
        try
        {
            deleteJob( "validadorJOB" );

            Scheduler scheduler = shedFact.getScheduler();
            scheduler.start();

            JobDetail job = JobBuilder.newJob( classname )
                    .withIdentity( "validadorJOB", "grupo01" )
                    .build();

            //<segundo> <minuto> <hora> <dia-do-mes> <mes> <dia-da-semana>: no segundo 0 de cada minuto
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity( "validadorTRIGGER", "grupo01" )
                    .withSchedule( CronScheduleBuilder.cronSchedule( "0 * * * * ?" ) )
                    .build();
            scheduler.scheduleJob( job, trigger );
            System.out.println( "[Cronjob] fecho automático agendado (hora_fecho_geral)" );
        }
        catch ( SchedulerException e )
        {
            System.err.println( "[Cronjob] falha ao agendar: " + e );
        }
    }

    private static void deleteJob( String nome )
    {
        try
        {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.deleteJob( new JobKey( nome, "grupo01" ) );
            System.out.println( "ELIMINEI O CRONJOB " + nome );
            
        }
        catch ( SchedulerException ex )
        {
            System.out.println( ex );
        }
    }
    
    public static void main( String[] args )
    {
        
        BDConexao conexao = BDConexao.getInstancia();
        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexao );
        QuartzApp.executarCronjob( ValidadorJob.class, ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 ) );
    }
    
}
