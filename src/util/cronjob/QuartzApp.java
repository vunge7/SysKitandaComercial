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
    
    public static void executarCronjob( Class classname, TbDadosInstituicao d )
    {
        Date horaTerminoVenda = d.getHoraTerminoVenda();
        int hora = horaTerminoVenda.getHours();
        int minuto = horaTerminoVenda.getMinutes();
        int segundo = horaTerminoVenda.getSeconds();

        //O formato do Cron Expression se resume em: <segundo> <minuto> <hora> <dia-do-mes> <mes> <ano>.
        SchedulerFactory shedFact = new StdSchedulerFactory();
        System.out.println( "HORA: " + hora );
        System.out.println( "MINUTO: " + minuto );
        System.out.println( "SEGUNDO: " + segundo );
        try
        {
            deleteJob( "validadorJOB" );
            
            Scheduler scheduler = shedFact.getScheduler();
            scheduler.start();
            
            JobDetail job = JobBuilder.newJob( classname )
                    .withIdentity( "validadorJOB", "grupo01" )
                    .build();
            
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity( "validadorTRIGGER", "grupo01" )
                    .withSchedule( CronScheduleBuilder.cronSchedule( "0/" + segundo + " 0/" + minuto + " 0/" + hora + " * * ?" ) )
                    .build();
            scheduler.scheduleJob( job, trigger );
            
        }
        catch ( SchedulerException e )
        {
            // TODO Auto-generated catch block
////            e.printStackTrace();
        }
    }
    
    private static void deleteJob( String nome )
    {
        try
        {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.deleteJob( new JobKey( nome ) );
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
