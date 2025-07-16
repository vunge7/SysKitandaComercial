/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.extrasalario;

import entity.MasterTable;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

/**
 *
 * @author Domingos Dala Vunge
 */
public interface ExtraSalarioInteface
{

    public void registrar_item();

    public Date getDate();

    public int getIdMes();

    public int getAno();

    public MasterTable getDesignacao( String designacao );

    public void adicionar_tabela();

    public void remover_item();

}
