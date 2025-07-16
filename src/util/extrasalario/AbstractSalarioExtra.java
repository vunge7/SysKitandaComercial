/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.extrasalario;

import entity.ItemSalarioExtra;
import entity.MasterTable;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethodsSwing;
import rh.visao.ExtraSalarioFuncionarioVisao;
import static rh.visao.ExtraSalarioFuncionarioVisao.funcionario_global;
import static rh.visao.ExtraSalarioFuncionarioVisao.itemSalarioExtraDao;
import static rh.visao.ExtraSalarioFuncionarioVisao.usuario_global;
import static util.DVML.TIPO_MENSAGEM_ERRO;
import static util.DVML.TIPO_MENSAGEM_INFOR;
import static util.MetodosUtil.showMessageUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public abstract class AbstractSalarioExtra implements ExtraSalarioInteface
{

    private final JTable tabela;
    private final Integer extra;
    private final JComboBox combo;
    private final JSpinner spinner;

    public AbstractSalarioExtra( JTable tabela, int extra, JComboBox combo, JSpinner spinner )
    {
        this.tabela = tabela;
        this.extra = extra;
        this.combo = combo;
        this.spinner = spinner;

        this.spinner.setModel( CfMethodsSwing.criarSpinnerDoubleModel( 0.00, 100000000000.00, 0.00 ) );
    }

    @Override
    public void registrar_item()
    {
        if ( combo.getSelectedIndex() != 0 )
        {
            double valor_remuneracao = ( Double ) spinner.getValue();
            if ( valor_remuneracao != 0 )
            {

                String remunueracao = combo.getSelectedItem().toString();
                MasterTable masterTable = getDesignacao( remunueracao );

                ItemSalarioExtra itemSalarioExtra = new ItemSalarioExtra();
                itemSalarioExtra.setDataHora( getDate() );
                itemSalarioExtra.setValor( valor_remuneracao );
                itemSalarioExtra.setFkMasterTable( masterTable );
                itemSalarioExtra.setFkTbFuncionario( funcionario_global );
                itemSalarioExtra.setFkTbUsuario( usuario_global );
                System.out.println( "Data: " + getDate() );
                try
                {
                    itemSalarioExtraDao.create( itemSalarioExtra );
                    spinner.setValue( 0d );
                    spinner.requestFocus();
                    adicionar_tabela();
                }
                catch ( Exception e )
                {
                    showMessageUtil( "Falha ao adicionar o item.", TIPO_MENSAGEM_ERRO );
                }
            }
            else
            {
                showMessageUtil( "A insira o valor do item", TIPO_MENSAGEM_ERRO );
            }

        }
        else
        {
            showMessageUtil( "Seleccione o item", TIPO_MENSAGEM_ERRO );
        }
    }

    @Override
    public Date getDate()
    {
        Date date = new Date();
        date.setMonth( getIdMes() - 1 );
        date.setYear( getAno() - 1900 );

        return date;
    }

    @Override
    public int getAno()
    {
        return Integer.parseInt( ExtraSalarioFuncionarioVisao.cmbAno.getSelectedItem().toString() );
    }

    @Override
    public int getIdMes()
    {
        return ExtraSalarioFuncionarioVisao.mesRhDao.getIdByDescricao( ExtraSalarioFuncionarioVisao.cmbPeriodo.getSelectedItem().toString() );
    }

    @Override
    public MasterTable getDesignacao( String designacao )
    {
        int id = ExtraSalarioFuncionarioVisao.masterTableDao.getIdByDescricao( designacao );
        return ExtraSalarioFuncionarioVisao.masterTableDao.findMasterTable( id );
    }

    @Override
    public void adicionar_tabela()
    {

        List<ItemSalarioExtra> item = ExtraSalarioFuncionarioVisao.itemSalarioExtraDao.getAllItemSalarioExtraByAnoAndMesAndIdFuncionario(
                getAno(),
                getIdMes(),
                extra,
                funcionario_global.getIdFuncionario()
        );

        System.err.println( "Tamanho: " + item.size() );
        DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
        modelo.setRowCount( 0 );
        if ( item != null )
        {

            for ( ItemSalarioExtra object : item )
            {
                modelo.addRow( new Object[]
                {
                    object.getPkItemSalarioExtra(),
                    object.getFkMasterTable().getDesigncao(),
                    object.getValor()
                } );
            }
        }
    }

    @Override
    public void remover_item()
    {
        int linha_selecionada = 0;
        linha_selecionada = tabela.getSelectedRow();

        if ( linha_selecionada >= 0 )
        {
            int opcao = JOptionPane.showConfirmDialog( null, "Caro usuário tens a certeza que pretendes eliminar este item?" );
            if ( JOptionPane.YES_OPTION == opcao )
            {
                DefaultTableModel modelo = ( DefaultTableModel ) tabela.getModel();
                Integer cod = ( Integer ) modelo.getValueAt( linha_selecionada, 0 );

                try
                {
                    itemSalarioExtraDao.destroy( cod );
                    adicionar_tabela();
                    showMessageUtil( "Item removida com sucesso.", TIPO_MENSAGEM_INFOR );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    showMessageUtil( "Falha ao remover a item.", TIPO_MENSAGEM_ERRO );
                }
            }
        }
    }

}
