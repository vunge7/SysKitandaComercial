/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import dao.FaltaDao;
import dao.FechoPeriodoDao;
import entity.FechoPeriodo;
import entity.TbFalta;
import entity.TbFuncionario;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import rh.visao.ExtraSalarioFuncionarioVisao;
import rh.visao.JustificarFaltaVisao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class FaltaInjusticadaUtil
{

    public static String[] getCampos()
    {
        String[] campos =
        {
            "Cod.Falta", "Data", "Hora(s)", "Dia(Valor)", "Acção", ""
        };
        return campos;
    }

    public static Boolean[] getColumnEditable()
    {
        Boolean[] editable =
        {
            false, false, false, false, false, false, false
        };
        return editable;
    }

    public static Class[] getClasses()
    {
        Class[] classes =
        {
            Integer.class,
            String.class,
            Integer.class,
            Double.class,
            JButton.class,
            JButton.class
        };

        return classes;

    }

    public static Vector<Vector> dados( FechoPeriodoDao fechoPeriodoDao, FaltaDao faltaDao, int idAno, int idPeriodo, BDConexao conexao, TbFuncionario funcionario )
    {

        System.out.println( "IdAno: " + idAno );
        System.out.println( "IdPeriodo: " + idPeriodo );
        FechoPeriodo fechoPeriodo = fechoPeriodoDao.getFechoPeriodoByIdAnoAndPerido( idAno, idPeriodo );
        System.out.println( "Periodo: " + fechoPeriodo.getFkPeriodo().getDescricao() );
        System.out.println( "Data Abertura: " + MetodosUtil.getDataBanco( fechoPeriodo.getDataAbertura() ) );
        System.out.println( "Data Fecho: " + MetodosUtil.getDataBanco( fechoPeriodo.getDataFecho() ) );

        Vector<Vector> fonte_dados = new Vector<>();
        List<TbFalta> listaInJutificadas = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), false, conexao, faltaDao );

        System.out.println( "TAMANHO LISTA INJUSTIFICADAS: " + listaInJutificadas.size() );
        for ( TbFalta faltaInjustificada : listaInJutificadas )
        {
            Vector vector = new Vector();
            double horas = getHora( false, faltaInjustificada.getData(), faltaInjustificada.getData(), faltaDao, conexao, funcionario );
            double valor_hora = getValorHora( funcionario, conexao );

            vector.add( faltaInjustificada.getIdFaltaPK() );
            vector.add( MetodosUtil.getDataBanco( faltaInjustificada.getData() ) );
            vector.add( new BigDecimal( horas ).setScale( 2, RoundingMode.DOWN ) );
            vector.add( new BigDecimal( valor_hora * horas ).setScale( 2, RoundingMode.DOWN ) );
            vector.add( new JButton( DVML.JUSTIFICAR ) );
            vector.add( new JButton( DVML.REMOVER ) );
            fonte_dados.add( vector );

        }

        return fonte_dados;

    }

    private static double getDiaHora( double horas )
    {
        return MetodosUtil.conversaoHoraEmDia( horas );
    }

    private static double getValorHora( TbFuncionario funcionario, BDConexao conexao )
    {
        double valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( JPAEntityMannagerFactoryUtil.em, funcionario, conexao );
        return valor_hora;
    }

    private static double getHora( boolean justificadas, Date dataInicio, Date dataFim, FaltaDao faltaDao, BDConexao conexao, TbFuncionario funcionario )
    {
//        String data = getAno() + "-" + getIdMes() + "-01";
//        Date dataInicio = MetodosUtil.stringToDate( data, "yyyy-MM-dd" );
//        Date dataFim = new Date();

        return faltaDao.getNumeroHorasFaltasByIdFuncionario( funcionario.getIdFuncionario(), dataInicio, dataFim, justificadas, conexao );

    }

    public static void accao( JTable tabela )
    {
        tabela.addMouseListener( new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked( java.awt.event.MouseEvent evt )
            {
                if ( evt.getClickCount() == 1 )
                {

                    jTable1MouseClicked( evt, tabela );
                }
                else
                {
                    System.out.println( "" );
                }
            }

        } );

    }

    private static void jTable1MouseClicked( MouseEvent evt, JTable tabela )
    {

        int coluna = tabela.getSelectedColumn();
        int linha = tabela.getSelectedRow();
        Object value;
        try
        {
            value = tabela.getValueAt( linha, coluna );
        }
        catch ( Exception e )
        {
            value = null;
        }

        if ( value instanceof JButton && value != null )
        {

            if ( ( ( JButton ) value ).getText().equals( DVML.REMOVER ) )
            {
                // System.err.println( "Remover" );
                //int codFalta = ( Integer ) tabela.getValueAt( rown, 0 );
                //System.err.println( "Cod. Falta: " +codFalta );

                if ( evt.getClickCount() == 1 )
                {
                    Integer opcao = JOptionPane.showConfirmDialog( null,
                            "Caro usuário tem a certeza que pretendes remover a falta?", "Aviso", JOptionPane.YES_OPTION );
                    if ( opcao == JOptionPane.YES_OPTION )
                    {
                        int codFalta = ( Integer ) tabela.getValueAt( linha, 0 );
                        try
                        {
                            ExtraSalarioFuncionarioVisao.remover_falta( codFalta );
                        }
                        catch ( Exception e )
                        {
                        }

                    }
                    else if ( opcao == JOptionPane.NO_OPTION )
                    {
                        ExtraSalarioFuncionarioVisao.desenhar_tabelas_justificadas_e_injustificadas();
                    }
                }

            }
            else if ( ( ( JButton ) value ).getText().equals( DVML.JUSTIFICAR ) )
            {
                Integer codFalta = ( Integer ) tabela.getValueAt( linha, 0 );
                //System.err.println( "Justiciação de falta." );
                if ( evt.getClickCount() == 1 )
                {
                    ExtraSalarioFuncionarioVisao.desenhar_tabelas_justificadas_e_injustificadas();
                    new JustificarFaltaVisao( codFalta, true ).setVisible(true);
                }

            }

//                String va1 = "" + tabela.getValueAt( rown, 0 );
//                String va2 = "" + tabela.getValueAt( rown, 1 );
//                String va3 = "" + tabela.getValueAt( rown, 2 );
//                String va3 = "" + tabela.getValueAt( rown, 2 );
//                String va4 = "" + tabela.getValueAt( rown, 3 );
//                Boolean va5 = ( Boolean ) tabela.getValueAt( rown, 4 );
//                String va6 = ( String ) tabela.getValueAt( rown, 5 );
            // jTextArea1.setText( va1 + "\n" + va5 + "\n" + va6 );
        }

    }

}
