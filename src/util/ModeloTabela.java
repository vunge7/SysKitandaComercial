/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Domingos Dala Vunge
 */


public final class ModeloTabela  extends AbstractTableModel{

    
    private ArrayList linhas = null;
    private String[] colunas = null;

    public ModeloTabela(ArrayList linhas, String [] colunas) 
    {
        setLinhas(linhas);
        setColunas(colunas);
    }

    public ArrayList getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList linhas) {
        this.linhas = linhas;
    }

    public String[] getColunas() {
        return colunas;
    }

    public void setColunas(String[] colunas) {
        this.colunas = colunas;
    }
  
    @Override
    public int getRowCount() {
        return this.linhas.size();
    }

    @Override
    public int getColumnCount() {
        return this.colunas.length;
    }

    public String getColumnName(int columnIndex){
        return colunas[columnIndex];
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {        
        Object[] linha = (Object[]) getLinhas().get(rowIndex);
        try {
             return linha[columnIndex];
        } catch (Exception e) {
            return null;
        }
       
        
    }
    
}