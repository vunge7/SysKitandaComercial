/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.tabela_manual.tabela;


import java.sql.Connection;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import util.tabela_manual.render.Render;

/**
 *
 * @author Domingos Dala Vunge
 */
public final class Tabela
{

    private Boolean[] editable_cells = null;
    private JTable tabela = null;
    private String[] campos = null;
    private Class[] classes_campos = null;
    private Vector<Vector> matrix = new Vector<>();

    public Tabela( JTable tabela, String[] campos, Class[] classes_campos, Boolean[] editable_cells, Vector<Vector> matrix )
    {
        this.tabela = tabela;
        this.editable_cells = editable_cells;
        this.campos = campos;
        this.classes_campos = classes_campos;
        this.matrix = matrix;

        visualizar();
    }

    public void visualizar()
    {
        this.tabela.setDefaultRenderer( Object.class, new Render() );
 
        DefaultTableModel dt = new DefaultTableModel( this.campos, 0 )
        {
            Class[] types = classes_campos;
            @Override
            public Class getColumnClass( int columnIndex )
            {
                return types[ columnIndex ];
            }
            @Override
            public boolean isCellEditable( int row, int column )
            {
                return editable_cells[ column ];
            }
        };

        for ( int i = 0; i < matrix.size(); i++ )
        {
            dt.addRow( matrix.get( i ) );
        }
        tabela.setModel( dt );
    }

    public static void main( String[] args )
    {
      
    }
}
