/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabela;


import java.sql.Connection;
import dao.AdiantamentoDao;
import dao.BancoDao;
import dao.FaltaDao;
import dao.FuncionarioDao;
import dao.ItemSubsidioFuncionarioDao;
import dao.ReciboRhDao;
import dao.SalarioDao;
import entity.TbFuncionario;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import render.Render;
import rh.visao.RecibosVisao;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class TabelaFuncionario
{

    private boolean[] editable =
    {
        true, false, false, false, false, false
    };

    FuncionarioDao dao;

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private SalarioDao salarioDao = new SalarioDao( emf );
    private ItemSubsidioFuncionarioDao itemSubsidioFuncionarioDao = new ItemSubsidioFuncionarioDao( emf );
    private ReciboRhDao reciboRhDao = new ReciboRhDao( emf );
    private FaltaDao faltaDao = new FaltaDao( emf );
    private BancoDao bancoDao = new BancoDao( emf );
    private AdiantamentoDao adiantamentoDao = new AdiantamentoDao( emf );
    private BDConexao conexao;

    public void visualizar( JTable tabela, Date data_inicio, Date data_fim, BDConexao conexao )
    {
        tabela.setDefaultRenderer( Object.class, new Render() );

        DefaultTableModel dt = new DefaultTableModel( new String[]
        {
            "activo", "numero_funcionario", "nome", "Remunueração", "Desconto", "Valor Receber", "Detalhes"
        }, 0 )
        {

            Class[] types = new Class[]
            {
                java.lang.Boolean.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class
            };

            public Class getColumnClass( int columnIndex )
            {
                return types[columnIndex];
            }

            public boolean isCellEditable( int row, int column )
            {
                return editable[column];
            }

        };

        JButton btn_visualizar = new JButton( "Detalhe" );
        btn_visualizar.setName( "v" );

//        JComboBox cmb = new JComboBox();
//        cmb.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaModalidades() ) );
        TbFuncionario funcionario;
        List<TbFuncionario> listFuncionarios = funcionarioDao.buscaTodos();

        if ( listFuncionarios.size() > 0 )
        {

            for ( int i = 0; i < listFuncionarios.size(); i++ )
            {
                Object fila[] = new Object[8];
                funcionario = listFuncionarios.get( i );
                //senão existe mostra
                if ( !reciboRhDao.existeReciboSalarioFuncionario( funcionario.getIdFuncionario(), RecibosVisao.getIdAno(), RecibosVisao.getIdPeriodo() ) )
                {
                    double remunuracao_liquida = RecibosVisao.getSalarioBase( funcionario ) 
                            - FaltaDao.getDescontoRemuneracaoSalarialHora( funcionario, data_inicio, data_fim, conexao, faltaDao );
                    double salario_base = RecibosVisao.getSalarioBase( funcionario ) ;
                    double remuneracoes = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( getRemuneracoes( funcionario, salario_base ) ) );

                    double descontos = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( getDesconto( funcionario, remunuracao_liquida ) ) );
                    double valor_receber = ( remuneracoes - descontos );

                    if ( funcionario.getNumeroFuncionario().equals( "10" ) )
                    {
                        System.out.println( "Total Remunerações: " + remuneracoes );
                        System.out.println( "Total Desconto: " + descontos );
                    }

                    int ac = (int) funcionario.getActivo();
                    if ( ac == 1 )
                    {
                        fila[0] = true;
                    }
                    else
                    {
                        fila[0] = false;
                    }
                    fila[1] = funcionario.getNumeroFuncionario();
                    fila[2] = funcionario.getNome();
//                fila[3] = FuncionarioDao.calcular_remuneracao_funcionario( funcionario, salarioDao, faltaDao, adiantamentoDao, data_inicio, data_fim, conexao );
//                fila[4] = FuncionarioDao.calcular_desconto_funcionario( funcionario, salarioDao, faltaDao, adiantamentoDao, data_inicio, data_fim, conexao );
//                fila[5] = FuncionarioDao.calcular_remuneracao_funcionario( funcionario, salarioDao, faltaDao, adiantamentoDao, data_inicio, data_fim, conexao ) -
//                          FuncionarioDao.calcular_desconto_funcionario( funcionario, salarioDao, faltaDao, adiantamentoDao, data_inicio, data_fim, conexao );
//                fila[7] = cmb;

                    fila[3] = CfMethods.formatarComoMoeda( remuneracoes );
                    fila[4] = CfMethods.formatarComoMoeda( descontos );
                    fila[5] = CfMethods.formatarComoMoeda( valor_receber );

                    fila[6] = btn_visualizar;

                    dt.addRow( fila );
                }
                
            }

        }

        tabela.setModel( dt );

    }

    private double getDesconto( TbFuncionario funcionario, double remunuracao_liquida )
    {
        double descontos = 0d;

        try
        {
            descontos += RecibosVisao.adicionar_desconto( funcionario, false );
        }
        catch ( Exception e )
        {
            descontos += 0;
        }
        try
        {
            descontos += RecibosVisao.adicionar_faltas( funcionario, false );
        }
        catch ( Exception e )
        {
            descontos += 0;
        }
        try
        {
            descontos += RecibosVisao.adicionar_irt( funcionario, remunuracao_liquida, false );
        }
        catch ( Exception e )
        {
            descontos += 0;
        }
        try
        {
            descontos += RecibosVisao.adicionar_inss( funcionario, remunuracao_liquida,false );
        }
        catch ( Exception e )
        {
            descontos += 0;
        }

        return descontos;

    }

    private double getRemuneracoes( TbFuncionario funcionario, double remuneracao_liquida )
    {
        double remunuracoes = 0d;
        remunuracoes += remuneracao_liquida;

        try
        {
            remunuracoes += RecibosVisao.adicionar_subsidios( funcionario.getTbItemSubsidiosFuncionarioList(), false );
        }
        catch ( Exception e )
        {
            remunuracoes += 0;
        }
        try
        {
            remunuracoes += RecibosVisao.adicionar_remuneracoes_or_bonos( funcionario, DVML.MASTER_TABLE_REMUNERACAO, false );
        }
        catch ( Exception e )
        {
            remunuracoes += 0;
        }
        try
        {
            remunuracoes += RecibosVisao.adicionar_remuneracoes_or_bonos( funcionario, DVML.MASTER_TABLE_ABONOS, false );
        }
        catch ( Exception e )
        {

        }
        try
        {
            remunuracoes += RecibosVisao.adicionar_agregado_familiar( funcionario, false );
        }
        catch ( Exception e )
        {

        }

        return remunuracoes;
    }

}
