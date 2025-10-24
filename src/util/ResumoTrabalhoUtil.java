/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import dao.FaltaDao;
import dao.FuncionarioDao;
import dao.SalarioDao;
import entity.TbFuncionario;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import static util.DVML.CONTRATRO_DETERMINADO;
import static util.MetodosUtil.conversaoHoraEmDia;
import static util.MetodosUtil.getDataString2;
import static util.MetodosUtil.getDiasUteis;
import static util.MetodosUtil.igual_data_1_data_2;
import static util.MetodosUtil.menor_data_1_data_2;
import static util.MetodosUtil.stringToDate;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ResumoTrabalhoUtil
{

    private String tipoContrato;
    private Date dataInicio, dataFim;
    private double diasPossveisTrabalho, diasUteisTrabalho,
            diasNaoJustificadosFalta,
            horasNaoJustificadasFalta,
            salarioBase,
            remuneracaoMensal,
            remuneracaoDiaria,
            remuneracaoHora;

    public ResumoTrabalhoUtil()
    {
    }

    public Date getDataInicio()
    {
        return dataInicio;
    }

    public void setDataInicio( Date dataInicio )
    {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim()
    {
        return dataFim;
    }

    public void setDataFim( Date dataFim )
    {
        this.dataFim = dataFim;
    }

    public double getDiasPossveisTrabalho()
    {
        return diasPossveisTrabalho;
    }

    public void setDiasPossveisTrabalho( double diasPossveisTrabalho )
    {
        this.diasPossveisTrabalho = diasPossveisTrabalho;
    }

    public double getDiasUteisTrabalho()
    {
        return diasUteisTrabalho;
    }

    public static double getDiasUteisTrabalho( FaltaDao faltaDao, TbFuncionario funcionario, Date dataInicio, Date dataFim, BDConexao conexao )
    {

        double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( funcionario.getIdFuncionario(), dataInicio, dataFim, false, conexao );
        double diasDeFalta = conversaoHoraEmDia( numerosHorasFaltadas );
        //double dpt = getDiasUteis( dataInicio, dataFim, funcionario.getFkModalidade().getPkModalidade() );
        double dpt = funcionario.getFkModalidade().getDiasUteisTrabalho();
        double dut = dpt - diasDeFalta;
        return dut;
    }

    public void setDiasUteisTrabalho( double diasUteisTrabalho )
    {
        this.diasUteisTrabalho = diasUteisTrabalho;
    }

    public double getDiasNaoJustificadosFalta()
    {
        return diasNaoJustificadosFalta;
    }

    public void setDiasNaoJustificadosFalta( double diasNaoJustificadosFalta )
    {
        this.diasNaoJustificadosFalta = diasNaoJustificadosFalta;
    }

    public double getHorasNaoJustificadasFalta()
    {
        return horasNaoJustificadasFalta;
    }

    public void setHorasNaoJustificadasFalta( double horasNaoJustificadasFalta )
    {
        this.horasNaoJustificadasFalta = horasNaoJustificadasFalta;
    }

    public String getTipoContrato()
    {
        return tipoContrato;
    }

    public void setTipoContrato( String tipoContrato )
    {
        this.tipoContrato = tipoContrato;
    }

    public double getSalarioBase()
    {
        return salarioBase;
    }

    public void setSalarioBase( double salarioBase )
    {
        this.salarioBase = salarioBase;
    }

    public double getRemuneracaoMensal()
    {
        return remuneracaoMensal;
    }

    public void setRemuneracaoMensal( double remuneracaoMensal )
    {
        this.remuneracaoMensal = remuneracaoMensal;
    }

    public double getRemuneracaoDiaria()
    {
        return remuneracaoDiaria;
    }

    public void setRemuneracaoDiaria( double remuneracaoDiaria )
    {
        this.remuneracaoDiaria = remuneracaoDiaria;
    }

    /**
     * @param conexao
     * @autor: Domingos Dala Vunge
     * @param emf
     * @param idFuncionario
     * @return
     */
    public static ResumoTrabalhoUtil getTotalDiasTrabalhado( EntityManagerFactory emf, int idFuncionario, BDConexao conexao )
    {
        ResumoTrabalhoUtil resumoTrabalho = new ResumoTrabalhoUtil();
        FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
        FaltaDao faltaDao = new FaltaDao( emf );
        SalarioDao salarioDao = new SalarioDao( emf );
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( idFuncionario );
        Date dataActual = new Date();
        double diasUteisTrabalho = 0, diasDeFalta = 0, diasPossveisTrabalho = 0;

        resumoTrabalho.setTipoContrato( funcionario.getTipoContrato() );
        resumoTrabalho.setSalarioBase( salarioDao.getSalarioByIdFuncionario( idFuncionario ) );
        resumoTrabalho.setRemuneracaoMensal( ( resumoTrabalho.getSalarioBase() / 12 ) );
        //valor a receber diário
        resumoTrabalho.setRemuneracaoDiaria( resumoTrabalho.getRemuneracaoMensal() / funcionario.getFkModalidade().getDiasUteisTrabalho() );
        resumoTrabalho.setRemuneracaoHora( resumoTrabalho.getRemuneracaoDiaria() / 8 );
        /**
         * 1º Caso: CONTRATO DETERMINADO
         */
        if ( funcionario.getTipoContrato().equals( CONTRATRO_DETERMINADO ) )
        {

            Date dataInicioContrato = funcionario.getDataInicioContrato();
            Date dataFimContrato = funcionario.getDataFimContrato();
            /*Verifica se a data actual está no intervalo da data do inicio do contrato e do fim*/
            if ( menor_data_1_data_2( dataInicioContrato, dataActual ) || igual_data_1_data_2( dataInicioContrato, dataActual ) )
            {
                if ( menor_data_1_data_2( dataActual, dataFimContrato ) || igual_data_1_data_2( dataActual, dataFimContrato ) )
                {
                    // diasDeFalta = conversaoHoraEmDia( faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataInicioContrato, dataActual, false ) );

                    System.err.println( "Data de Inicio:::: " + getDataString2( dataInicioContrato ) );
                    System.err.println( "Data de Fim:::: " + getDataString2( dataActual ) );
                    double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataInicioContrato, dataActual, false, conexao );
                    System.err.println( "Nº Horas de Falta: " + numerosHorasFaltadas );
                    diasDeFalta = conversaoHoraEmDia( numerosHorasFaltadas );
                    System.err.println( "Nº de Horas de Convertidas de faltas: " + diasDeFalta );
                    diasPossveisTrabalho = getDiasUteis( dataInicioContrato, dataActual, funcionario.getFkModalidade().getPkModalidade() );
                    diasUteisTrabalho = diasPossveisTrabalho - diasDeFalta;

                    resumoTrabalho.setDataInicio( dataInicioContrato );
                    resumoTrabalho.setDataFim( dataActual );
                    resumoTrabalho.setDiasNaoJustificadosFalta( diasDeFalta );
                    resumoTrabalho.setHorasNaoJustificadasFalta( numerosHorasFaltadas );
                    resumoTrabalho.setDiasPossveisTrabalho( diasPossveisTrabalho );
                    //Total Geral
                    resumoTrabalho.setDiasUteisTrabalho( diasUteisTrabalho );

                }

            }

        }
        /**
         * 2º Caso: CONTRATO INDERTERMINADO
         */
        else
        {
            Date dataContrato = funcionario.getDataContrato();
            int anoActual = ( dataActual.getYear() + 1900 );
            int anoContrato = ( dataContrato.getYear() + 1900 );

//
            Date dataComeco = ( anoActual == anoContrato ) ? funcionario.getDataContrato() : stringToDate( "01/01/" + anoActual );
            System.err.println( "Data de Inicio:::: " + getDataString2( dataComeco ) );
            System.err.println( "Data de Fim:::: " + getDataString2( dataActual ) );
            double numerosHorasFaltadas = faltaDao.getNumeroHorasFaltasByIdFuncionario( idFuncionario, dataContrato, dataActual, false, conexao );
            System.err.println( "Nº Horas de Falta: " + numerosHorasFaltadas );
            diasDeFalta = conversaoHoraEmDia( numerosHorasFaltadas );
            diasPossveisTrabalho = getDiasUteis( dataComeco, dataActual, funcionario.getFkModalidade().getPkModalidade() );
            diasUteisTrabalho = diasPossveisTrabalho - diasDeFalta;

            resumoTrabalho.setDataInicio( dataComeco );
            resumoTrabalho.setDataFim( dataActual );
            resumoTrabalho.setDiasNaoJustificadosFalta( diasDeFalta );
            resumoTrabalho.setHorasNaoJustificadasFalta( numerosHorasFaltadas );
            resumoTrabalho.setDiasPossveisTrabalho( diasPossveisTrabalho );
            //Total Geral
            resumoTrabalho.setDiasUteisTrabalho( diasUteisTrabalho );

        }

        return resumoTrabalho;
    }

    public static double getValorHoraBySalario( EntityManagerFactory emf, TbFuncionario funcionario, BDConexao conexao )
    {
        double valor_hora = 0d;

        SalarioDao salarioDao = new SalarioDao( emf );

        double salario_base = ( salarioDao.getLastSalario( funcionario.getIdFuncionario() ) != null ) ? salarioDao.getLastSalario( funcionario.getIdFuncionario() ).getValorTempo() : 0d;

        System.out.println( "SALARIO BASE: " +salario_base );
        double salario_diario = ( salario_base / funcionario.getFkModalidade().getDiasUteisTrabalho() );
        System.out.println( "SALARIO DIARIO: " +salario_diario );
        
        valor_hora = ( salario_diario / DVML.HORA_UTIL_TRABALHO );

        return valor_hora;

    }

    public double getRemuneracaoHora()
    {
        return remuneracaoHora;
    }

    public void setRemuneracaoHora( double remuneracaoHora )
    {
        this.remuneracaoHora = remuneracaoHora;
    }

}
