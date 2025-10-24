/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.cronjob;


import java.sql.Connection;
import entity.TbDadosInstituicao;
import comercial.controller.DadosInstituicaoController;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.BDConexao;
import util.MetodosUtil;
import util.email.EnvioEmailUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ValidadorJob implements Job
{

    @Override
    public void execute( JobExecutionContext jec ) throws JobExecutionException
    {
//        BDConexao conexaoTemp = BDConexao.getInstancia();
        System.out.println( "Enviar o relatório mensal no email " + new Date() );
        Date data_sistema = new Date();
        System.out.println( "HORA_SISTEMA: " + data_sistema.getHours() );
        System.out.println( "MINUTO_SISTEMA: " + data_sistema.getMinutes() );
        System.out.println( "SEGUNDO_SISTEMA: " + data_sistema.getSeconds() );
//        DadosInstituicaoController dadosInstituicaoController = new DadosInstituicaoController( conexaoTemp );
//        TbDadosInstituicao d = ( TbDadosInstituicao ) dadosInstituicaoController.findById( 1 );
        MetodosUtil.fechoAutomatico();
        EnvioEmailUtil.inicio();
        MetodosUtil.actualizarEstadoLog( "OFF" );
//        conexaoTemp.close();
        MetodosUtil.desligarPc();

    }

}
