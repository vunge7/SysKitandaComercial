/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.AnoEconomicoDao;
import dao.DadosInstituicaoDao;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Domingos Dala Vunge & Eng. Simão
 */
public class LicencaUtil {

    private  static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private  static DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao(emf);
    private static AnoEconomicoDao anoEconomicoDao = new AnoEconomicoDao(emf);
    private final static String REGIME_ANUAL = "anual";

    public static boolean licenca_valida(Date data_actual, BDConexao conexao) {

        Date data_licenca = dadosInstituicaoDao.findTbDadosInstituicao(1).getDataLicenca();
        String regime = dadosInstituicaoDao.findTbDadosInstituicao(1).getRegimeContrato();
        String anoByData = getAnoByData(data_actual);
        if (anoEconomicoDao.existe_ano_economico(anoByData)) {

            if (regime.equals(REGIME_ANUAL)) {
                System.out.println("Entrei aqui: ano economico existente");
                int diferenca = MetodosUtil.getDiferencaDias(data_actual, data_licenca, conexao);

                System.out.println("Diferenca: " + diferenca);
                Integer diasDoAno = getDiasDoAno(data_licenca, conexao);
                System.out.println("Dias do ano: " + diasDoAno);

                if (diferenca <= diasDoAno) {
                    exibirDiasEmFalta(diferenca, data_licenca, conexao);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Licenca expirada. Contacte o seu fornecedor.");
                }
            }
            else return true;

        }
        return false;
    }

    private static String getAnoByData(Date data) {
        String format = new SimpleDateFormat("YYYY").format(data);
        System.out.println("ano: " + format);
        return "";
    }

    private static Integer getDiasDoAno(Date date, BDConexao conexao) {
        String sql = "SELECT getDiasAno('" + MetodosUtil.getDataBanco(date) + "') as total";

        System.out.println(sql);
        ResultSet result = conexao.executeQuery(sql);
        int dias = 0;
        try {
            if (result.next()) {
                dias = result.getInt("total");
            }
        } catch (Exception e) {
        }
        return dias;

    }

    public static void exibirDiasEmFalta(int diferenca, Date data_licenca, BDConexao conexao) {
        
        int dias_do_ano = getDiasDoAno(data_licenca, conexao);
        int dias_em_falta = dias_do_ano - diferenca;

        System.out.println("Dias Em Falta: " + dias_em_falta);
        if (dias_em_falta <= 30) {
            JOptionPane.showMessageDialog(null, "Faltam " + dias_em_falta + " para a sua licenca expirar.");
        }

    }

    public static void main(String[] args) {
//        getAnoByData( new Date() );
//        System.out.println( "Dias: " + licensaUtil.getDiasDoAno( new Date() ) );
    }

}
