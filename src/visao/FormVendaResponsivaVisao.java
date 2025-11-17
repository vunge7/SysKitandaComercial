/*
 * FormVendaResponsivaVisao.java
 * JFrame totalmente responsivo, compatível com NetBeans GUI Builder.
 */
package visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormVendaResponsivaVisao extends JFrame {

    public FormVendaResponsivaVisao() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // abre maximizado
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // Painéis principais
        painelTopo = new JPanel();
        painelEsq = new JPanel();
        painelDir = new JPanel();
        painelTabela = new JPanel();

        // Componentes Painel Esquerdo
        txtEsq1 = new JTextField();
        txtEsq2 = new JTextField();
        txtEsq3 = new JTextField();
        txtEsq4 = new JTextField();
        txtEsq5 = new JTextField();
        comboEsq = new JComboBox<>(new String[]{"Item 1","Item 2"});

        // Componentes Painel Direito
        btn1 = new JButton("Confirmar");
        btn2 = new JButton("Cancelar");
        txtDir1 = new JTextField();
        txtDir2 = new JTextField();
        comboDir1 = new JComboBox<>(new String[]{"A","B"});
        comboDir2 = new JComboBox<>(new String[]{"C","D"});
        comboDir3 = new JComboBox<>(new String[]{"E","F"});
        comboDir4 = new JComboBox<>(new String[]{"G","H"});

        // Tabela com JScrollPane
        tabela = new JTable();
        tabela.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Código","Descrição","Qtd","Preço","Desc","Imposto","Total","Obs","Outro"}
        ));
        scroll = new JScrollPane(tabela);

        // Layout Painel Esquerdo
        GroupLayout layoutEsq = new GroupLayout(painelEsq);
        painelEsq.setLayout(layoutEsq);
        layoutEsq.setAutoCreateGaps(true);
        layoutEsq.setAutoCreateContainerGaps(true);

        layoutEsq.setHorizontalGroup(layoutEsq.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(txtEsq1)
                .addComponent(txtEsq2)
                .addComponent(txtEsq3)
                .addComponent(txtEsq4)
                .addComponent(txtEsq5)
                .addComponent(comboEsq)
        );
        layoutEsq.setVerticalGroup(layoutEsq.createSequentialGroup()
                .addComponent(txtEsq1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(txtEsq2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(txtEsq3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(txtEsq4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(txtEsq5, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboEsq, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        );

        // Layout Painel Direito
        GroupLayout layoutDir = new GroupLayout(painelDir);
        painelDir.setLayout(layoutDir);
        layoutDir.setAutoCreateGaps(true);
        layoutDir.setAutoCreateContainerGaps(true);

        layoutDir.setHorizontalGroup(layoutDir.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(txtDir1)
                .addComponent(txtDir2)
                .addComponent(comboDir1)
                .addComponent(comboDir2)
                .addComponent(comboDir3)
                .addComponent(comboDir4)
                .addGroup(layoutDir.createSequentialGroup().addComponent(btn1).addComponent(btn2))
        );
        layoutDir.setVerticalGroup(layoutDir.createSequentialGroup()
                .addComponent(txtDir1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(txtDir2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboDir1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboDir2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboDir3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboDir4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addGroup(layoutDir.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btn1).addComponent(btn2))
        );

        // Layout Painel Topo
        painelTopo.setLayout(new GridLayout(1,2));
        painelTopo.add(painelEsq);
        painelTopo.add(painelDir);

        // Painel Tabela
        painelTabela.setLayout(new BorderLayout());
        painelTabela.add(scroll, BorderLayout.CENTER);

        // Layout Principal
        GroupLayout layoutPrincipal = new GroupLayout(getContentPane());
        getContentPane().setLayout(layoutPrincipal);

        layoutPrincipal.setHorizontalGroup(layoutPrincipal.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(painelTopo)
                .addComponent(painelTabela)
        );
        layoutPrincipal.setVerticalGroup(layoutPrincipal.createSequentialGroup()
                .addComponent(painelTopo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(painelTabela)
        );

        pack();
    }

    private JPanel painelTopo, painelEsq, painelDir, painelTabela;
    private JTextField txtEsq1, txtEsq2, txtEsq3, txtEsq4, txtEsq5;
    private JComboBox<String> comboEsq;
    private JButton btn1, btn2;
    private JTextField txtDir1, txtDir2;
    private JComboBox<String> comboDir1, comboDir2, comboDir3, comboDir4;
    private JTable tabela;
    private JScrollPane scroll;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormVendaResponsivaVisao().setVisible(true));
    }
}