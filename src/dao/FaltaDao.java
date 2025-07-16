/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controlador.TbFaltaJpaController;
import entity.TbFalta;
import entity.TbFuncionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.ResumoTrabalhoUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FaltaDao extends TbFaltaJpaController
{

    public FaltaDao( EntityManagerFactory emf )
    {
        super( emf );
    }

    public List<TbFalta> getAllFalta()
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  TbFalta a  ORDER BY a.idFuncionarioFK.nome ASC" );

        List<TbFalta> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

    public List<TbFalta> getAllFalta( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT a FROM  TbFalta a   WHERE a.idFuncionarioFK.idFuncionario = :idFuncionario  ORDER BY a.idFuncionarioFK.nome ASC" )
                .setParameter( "idFuncionario", idFuncionario );

        List<TbFalta> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result;
        }
        return null;

    }

//    public List<TbFalta> getAllFaltaByIdEmpresa( int idFuncionario, int pkEmpresa )
//    {
//
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT a FROM  TbFalta a   WHERE a.idFuncionarioFK.fkEmpresa.pkEmpresa =:pkEmpresa a.idFuncionarioFK.idFuncionario = :idFuncionario  ORDER BY a.idFuncionarioFK.nome ASC" )
//                .setParameter( "idFuncionario", idFuncionario )
//                .setParameter( "pkEmpresa", pkEmpresa );
//
//        List<TbFalta> result = query.getResultList();
//        em.close();
//
//        if ( !result.isEmpty() )
//        {
//            return result;
//        }
//        return null;
//
//    }
    public double getNumeroFaltasByIdFuncionario( int idFuncionario, Date data_inicio, Date data_fim )
    {

        System.out.println( "DATA INICIO " + data_inicio.getDate() + "/" + data_inicio.getMonth() + "/" + ( data_inicio.getYear() + 1900 ) );
        System.out.println( "DATA FIM " + data_fim.getDate() + "/" + data_fim.getMonth() + "/" + ( data_fim.getYear() + 1900 ) );
        System.out.println( "ID PROFESSOR " + idFuncionario );

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(a.nFalta) as falta FROM  TbFalta a   WHERE a.idFuncionarioFK.idFuncionario = :idFuncionario  AND a.data BETWEEN :data_inicio AND :data_fim GROUP BY a.idFuncionarioFK" )
                .setParameter( "idFuncionario", idFuncionario )
                .setParameter( "data_inicio", data_inicio )
                .setParameter( "data_fim", data_fim );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    /**
     * @autor: Domingos Dala Vunge
     * @decrição: Retorna as horas não injustificadas de um determinado
     * funcionário
     * @param idFuncionario
     * @param data_inicio
     * @param data_fim
     * @param justificada
     * @return
     */
    public double getNumeroHorasFaltasByIdFuncionario( int idFuncionario, Date data_inicio, Date data_fim, boolean justificada )
    {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery( "SELECT SUM(n_falta) AS HORAS FROM tb_falta WHERE data BETWEEN ? AND ? AND idFuncionarioFK = ?  AND justificada_injustificada = ? GROUP BY idFuncionarioFK",
                TbFalta.class );
        query.setParameter( 1, data_inicio );
        query.setParameter( 2, data_fim );
        query.setParameter( 3, idFuncionario );
        query.setParameter( 4, 0 );

        List<Double> result = query.getResultList();
        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0d;

    }

    public double getNumeroHorasFaltasByIdFuncionario( int idFuncionario, Date data_inicio, Date data_fim, boolean justificada, BDConexao conexao )
    {

        String dataStringIncio = MetodosUtil.getDataString2( data_inicio );
        String dataStringFim = MetodosUtil.getDataString2( data_fim );
        String query = "SELECT SUM(n_falta) AS HORAS FROM tb_falta WHERE data BETWEEN '" + dataStringIncio + "' AND '" + dataStringFim + "' AND idFuncionarioFK = " + idFuncionario + " AND justificada_injustificada = " + ( justificada == true ? 1 : 0 ) + "  GROUP BY idFuncionarioFK";

        ResultSet result = conexao.executeQuery( query );
        double horas = 0d;

        try
        {
            if ( result.next() )
            {
                horas = result.getDouble( "HORAS" );
            }
        }
        catch ( Exception e )
        {
        }

        return horas;
    }

    public double getNumeroHorasFaltasByIdFuncionario( int idFuncionario, Date data_inicio, Date data_fim, BDConexao conexao )
    {

        String dataStringIncio = MetodosUtil.getDataString2( data_inicio );
        String dataStringFim = MetodosUtil.getDataString2( data_fim );
        String query = "SELECT SUM(n_falta) AS HORAS FROM tb_falta WHERE data BETWEEN '" + dataStringIncio + "' AND '" + dataStringFim + "' AND idFuncionarioFK = " + idFuncionario + "  GROUP BY idFuncionarioFK";

        ResultSet result = conexao.executeQuery( query );
        double horas = 0d;

        try
        {
            if ( result.next() )
            {
                horas = result.getDouble( "HORAS" );
            }
        }
        catch ( Exception e )
        {
        }

        return horas;
    }

    public List<TbFalta> getAllFaltasByIdFncionario( int idFuncionario, int ano, int mes, boolean justificada, BDConexao conexao )
    {

        String query = "SELECT * FROM tb_falta WHERE YEAR(data) = " + ano + " AND MONTH(data) = " + mes + " AND idFuncionarioFK = " + idFuncionario + " AND justificada_injustificada = " + ( justificada == true ? 1 : 0 ) + " ORDER BY data DESC";

        System.err.println( query );
        ResultSet result = conexao.executeQuery( query );
        List<TbFalta> listaFaltas = new ArrayList<>();
        TbFalta falta;
        try
        {
            while ( result.next() )
            {
                falta = findTbFalta( result.getInt( "idFaltaPK" ) );
                listaFaltas.add( falta );

            }
        }
        catch ( SQLException e )
        {
        }

        return listaFaltas;

    }

    public static List<TbFalta> getAllFaltasByBetweenDatasByIdFncionario( int idFuncionario, Date data_inicio, Date data_fim, boolean justificada, BDConexao conexao, FaltaDao faltaDao )
    {

        String query = "SELECT * FROM tb_falta WHERE DATE(data)  BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND  '" + MetodosUtil.getDataBanco( data_fim ) + "' AND idFuncionarioFK = " + idFuncionario + " AND justificada_injustificada = " + ( justificada == true ? 1 : 0 ) + " ORDER BY data DESC";

        System.err.println( query );
        ResultSet result = conexao.executeQuery( query );
        List<TbFalta> listaFaltas = new ArrayList<>();
        TbFalta falta;
        try
        {
            while ( result.next() )
            {
                falta = faltaDao.findTbFalta( result.getInt( "idFaltaPK" ) );
                listaFaltas.add( falta );

            }
        }
        catch ( SQLException e )
        {
        }

        return listaFaltas;

    }

    public static List<TbFalta> getAllFaltasByBetweenDatasByIdFncionario( int idFuncionario, Date data_inicio, Date data_fim, BDConexao conexao, FaltaDao faltaDao )
    {

        String query = "SELECT * FROM tb_falta WHERE DATE(data)  BETWEEN '" + MetodosUtil.getDataBanco( data_inicio ) + "' AND  '" + MetodosUtil.getDataBanco( data_fim ) + "' AND idFuncionarioFK = " + idFuncionario + " ORDER BY data DESC ";

        System.err.println( query );
        ResultSet result = conexao.executeQuery( query );
        List<TbFalta> listaFaltas = new ArrayList<>();
        TbFalta falta;
        try
        {
            while ( result.next() )
            {
                falta = faltaDao.findTbFalta( result.getInt( "idFaltaPK" ) );
                listaFaltas.add( falta );

            }
        }
        catch ( SQLException e )
        {
        }

        return listaFaltas;

    }

    public double getNumeroFaltasByIdFuncionarios( int idFuncionario )
    {

        EntityManager em = getEntityManager();
        Query query = em.createQuery( "SELECT SUM(a.nFalta) FROM  TbFalta a   WHERE a.idFuncionarioFK.idFuncionario = :idFuncionario  AND a.data BETWEEN :data_inicio AND :data_fim GROUP BY a.idFuncionarioFK" )
                .setParameter( "idFuncionario", idFuncionario );

        List<Double> result = query.getResultList();
        em.close();

        if ( !result.isEmpty() )
        {
            return result.get( 0 );
        }
        return 0;

    }

    public static double getDescontoRemuneracaoSalarialHora( TbFuncionario funcionario, Date data_inicio, Date data_fim, BDConexao conexao, FaltaDao faltaDao )
    {
        //List<TbFalta> itens = getAllFaltasByIdFncionario( funcionario.getIdFuncionario(), idAno, idMes, false, conexao );

        List<TbFalta> itens = getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), data_inicio, data_fim, conexao, faltaDao );
        double numero_faltas = 0d, valor_hora = 0d, desconto_hora = 0d;
        if ( itens != null )
        {

            valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( JPAEntityMannagerFactoryUtil.em, funcionario, conexao );
            Iterator<TbFalta> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                TbFalta next = iterator.next();
                numero_faltas += next.getNFalta();

            }

            desconto_hora = ( valor_hora * numero_faltas );

        }
        System.out.println( "DESCONTO HORA: " + desconto_hora );
        return desconto_hora;

    }

    public double getNHoraFalta( TbFuncionario funcionario, int idAno, int idMes, BDConexao conexao )
    {
        List<TbFalta> itens = getAllFaltasByIdFncionario( funcionario.getIdFuncionario(), idAno, idMes, false, conexao );
        double numero_faltas = 0d;
        if ( itens != null )
        {

            Iterator<TbFalta> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                TbFalta next = iterator.next();
                numero_faltas += next.getNFalta();

            }

        }
        return numero_faltas;

    }

    public static double getNHoraIntervaloDatasFalta( TbFuncionario funcionario, Date data_inicio, Date data_fim, BDConexao conexao, FaltaDao faltaDao )
    {
       // List<TbFalta> itens = getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), data_inicio, data_fim, false, conexao, faltaDao );
        List<TbFalta> itens = getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), data_inicio, data_fim, conexao, faltaDao );
        double numero_faltas = 0d;
        if ( itens != null )
        {

            Iterator<TbFalta> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                TbFalta next = iterator.next();
                numero_faltas += next.getNFalta();

            }

        }
        return numero_faltas;

    }

    public static double getDescontoSubsidio( double valor_subsidio, int dias_uteis, double horas_de_falta )
    {

        System.out.println( "Valor Subsídio: " + valor_subsidio );
        System.out.println( "Dias Úteis Trabalho: " + dias_uteis );
        System.out.println( "horas de Falta: " + horas_de_falta );
        double valor_diario = ( valor_subsidio / dias_uteis );
        double valor_hora = ( valor_diario / DVML.HORA_UTIL_TRABALHO );
        double desconto = ( horas_de_falta * valor_hora );
        System.out.println( "Valor Diário Sub.: " + valor_diario );
        System.out.println( "Valor Hora Sub.: " + valor_hora );
        System.out.println( "Desconto Subsídio: " + desconto );
        return desconto;
    }

}
