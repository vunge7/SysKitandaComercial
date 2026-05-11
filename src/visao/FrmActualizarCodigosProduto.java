/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

import entity.TbProduto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Martinho
 */
public class FrmActualizarCodigosProduto extends JDialog {

    private JTable tabela;

    private DefaultTableModel model;

    /*
     RADIO BUTTONS
     */
    private JRadioButton rbCodigoBarra;

    private JRadioButton rbCodigoManual;

    private ButtonGroup grupo;

    /*
     BOTÕES
     */
    private JButton btActualizar;

    private JButton btMarcarTodos;

    private JButton btDesmarcarTodos;

    private JButton btSair;

    /*
     LINHAS EDITADAS
     */
    private Set<Integer> linhasEditadas =
            new HashSet<>();

    public FrmActualizarCodigosProduto(
            java.awt.Frame parent,
            boolean modal
    ) {

        super(parent, modal);

        initComponents();

        carregarTabela();
    }

    private void initComponents() {

        setTitle("Actualizar Códigos Produto");

        setSize(950, 600);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        /*
         RADIO BUTTONS
         */
        rbCodigoBarra =
                new JRadioButton(
                        "Actualizar Código Barra",
                        true
                );

        rbCodigoManual =
                new JRadioButton(
                        "Actualizar Código Manual"
                );

        grupo = new ButtonGroup();

        grupo.add(rbCodigoBarra);

        grupo.add(rbCodigoManual);

        JPanel painelTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        painelTop.add(rbCodigoBarra);

        painelTop.add(rbCodigoManual);

        /*
         AVISO
         */
        JLabel lbAviso =
                new JLabel(
                        "Atenção: Se editar manualmente, "
                        + "serão actualizados apenas os editados. "
                        + "Caso contrário serão actualizados apenas os seleccionados."
                );

        lbAviso.setForeground(Color.RED);

        lbAviso.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        12
                )
        );

        JPanel painelAviso =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        painelAviso.add(lbAviso);

        /*
         PAINEL NORTE
         */
        JPanel painelNorte =
                new JPanel(
                        new BorderLayout()
                );

        painelNorte.add(
                painelTop,
                BorderLayout.NORTH
        );

        painelNorte.add(
                painelAviso,
                BorderLayout.SOUTH
        );

        add(
                painelNorte,
                BorderLayout.NORTH
        );

        /*
         MODEL
         */
        model = new DefaultTableModel(
                new Object[]{
                    "OK",
                    "Código Produto",
                    "Designação",
                    "Código Barra",
                    "Código Manual"
                }, 0
        ) {

            @Override
            public Class<?> getColumnClass(
                    int columnIndex
            ) {

                switch (columnIndex) {

                    case 0:
                        return Boolean.class;

                    default:
                        return Object.class;
                }
            }

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                /*
                 EDITÁVEL:
                 
                 0 = checkbox
                 3 = código barra
                 4 = código manual
                 */
                return column == 0
                        || column == 3
                        || column == 4;
            }
        };

        /*
         TABELA
         */
        tabela = new JTable(model);

        /*
         LISTENER:
         DETECTA LINHAS EDITADAS
         */
        model.addTableModelListener(
                new TableModelListener() {

            @Override
            public void tableChanged(
                    TableModelEvent e
            ) {

                if (
                        e.getType()
                        == TableModelEvent.UPDATE
                ) {

                    int coluna =
                            e.getColumn();

                    int linha =
                            e.getFirstRow();

                    /*
                     3 = código barra
                     4 = código manual
                     */
                    if (
                            coluna == 3
                            || coluna == 4
                    ) {

                        /*
                         GUARDA LINHA EDITADA
                         */
                        linhasEditadas.add(linha);
                    }
                }
            }
        });

        JScrollPane scroll =
                new JScrollPane(tabela);

        add(scroll, BorderLayout.CENTER);

        /*
         BOTÕES
         */
        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btActualizar =
                new JButton("Actualizar");

        btMarcarTodos =
                new JButton("Marcar Todos");

        btDesmarcarTodos =
                new JButton("Desmarcar Todos");

        btSair =
                new JButton("Sair");

        painelBotoes.add(btActualizar);

        painelBotoes.add(btMarcarTodos);

        painelBotoes.add(btDesmarcarTodos);

        painelBotoes.add(btSair);

        add(painelBotoes, BorderLayout.SOUTH);

        /*
         EVENTOS
         */
        btActualizar.addActionListener(
                e -> actualizarCodigos()
        );

        btMarcarTodos.addActionListener(
                e -> marcarTodos()
        );

        btDesmarcarTodos.addActionListener(
                e -> desmarcarTodos()
        );

        btSair.addActionListener(
                e -> dispose()
        );
    }

    private void carregarTabela() {

        model.setRowCount(0);

        linhasEditadas.clear();

        EntityManager em =
                JPAEntityMannagerFactoryUtil
                        .createEntityManager();

        try {

            String jpql =
                    "SELECT p " +
                    "FROM TbProduto p " +
                    "ORDER BY p.designacao ASC";

            List<TbProduto> lista =
                    em.createQuery(
                            jpql,
                            TbProduto.class
                    )
                            .getResultList();

            for (TbProduto p : lista) {

                /*
                 CHECKBOX POR PADRÃO:
                 TRUE
                 */
                model.addRow(new Object[]{
                    true,
                    p.getCodigo(),
                    p.getDesignacao(),
                    p.getCodBarra(),
                    p.getCodigoManual()
                });
            }

            /*
             VISUAL
             */
            tabela.setRowHeight(25);

            tabela.getColumnModel()
                    .getColumn(0)
                    .setPreferredWidth(40);

            tabela.getColumnModel()
                    .getColumn(1)
                    .setPreferredWidth(100);

            tabela.getColumnModel()
                    .getColumn(2)
                    .setPreferredWidth(350);

            tabela.getColumnModel()
                    .getColumn(3)
                    .setPreferredWidth(200);

            tabela.getColumnModel()
                    .getColumn(4)
                    .setPreferredWidth(200);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar tabela:\n"
                    + e.getMessage()
            );

        } finally {

            em.close();
        }
    }

    /*
     MARCAR TODOS
     */
    private void marcarTodos() {

        for (int i = 0;
                i < tabela.getRowCount();
                i++) {

            tabela.setValueAt(
                    true,
                    i,
                    0
            );
        }
    }

    /*
     DESMARCAR TODOS
     */
    private void desmarcarTodos() {

        for (int i = 0;
                i < tabela.getRowCount();
                i++) {

            tabela.setValueAt(
                    false,
                    i,
                    0
            );
        }
    }

    private void actualizarCodigos() {

        String mensagem = "";

        /*
         EXISTEM EDIÇÕES?
         */
        boolean existeEdicao =
                !linhasEditadas.isEmpty();

        if (existeEdicao) {

            mensagem =
                    "Deseja actualizar os valores editados manualmente?";

        } else {

            if (rbCodigoBarra.isSelected()) {

                mensagem =
                        "Deseja copiar o Código Produto "
                        + "para o Código Barra "
                        + "dos produtos seleccionados?";

            } else {

                mensagem =
                        "Deseja copiar o Código Produto "
                        + "para o Código Manual "
                        + "dos produtos seleccionados?";
            }
        }

        int confirmar =
                JOptionPane.showConfirmDialog(
                        this,
                        mensagem,
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmar != JOptionPane.YES_OPTION) {

            return;
        }

        EntityManager em =
                JPAEntityMannagerFactoryUtil
                        .createEntityManager();

        try {

            em.getTransaction().begin();

            int totalActualizados = 0;

            /*
             =====================================
             ACTUALIZA EDITADOS MANUALMENTE
             =====================================
             */
            if (existeEdicao) {

                for (Integer linha
                        : linhasEditadas) {

                    Integer codigoProduto =
                            (Integer)
                            tabela.getValueAt(
                                    linha,
                                    1
                            );

                    TbProduto produto =
                            em.find(
                                    TbProduto.class,
                                    codigoProduto
                            );

                    if (produto != null) {

                        /*
                         VALORES DIGITADOS
                         */
                        String codigoBarra =
                                tabela.getValueAt(
                                        linha,
                                        3
                                ) != null
                                ? tabela.getValueAt(
                                        linha,
                                        3
                                ).toString().trim()
                                : "";

                        String codigoManual =
                                tabela.getValueAt(
                                        linha,
                                        4
                                ) != null
                                ? tabela.getValueAt(
                                        linha,
                                        4
                                ).toString().trim()
                                : "";

                        /*
                         ACTUALIZA
                         */
                        produto.setCodBarra(
                                codigoBarra
                        );

                        produto.setCodigoManual(
                                codigoManual
                        );

                        em.merge(produto);

                        totalActualizados++;
                    }
                }

            } /*
             =====================================
             ACTUALIZA SELECCIONADOS
             =====================================
             */ else {

                for (int i = 0;
                        i < tabela.getRowCount();
                        i++) {

                    Boolean marcado =
                            (Boolean)
                            tabela.getValueAt(i, 0);

                    if (marcado != null && marcado) {

                        Integer codigoProduto =
                                (Integer)
                                tabela.getValueAt(i, 1);

                        TbProduto produto =
                                em.find(
                                        TbProduto.class,
                                        codigoProduto
                                );

                        if (produto != null) {

                            String codigo =
                                    produto.getCodigo()
                                            .toString();

                            /*
                             ACTUALIZA
                             */
                            if (rbCodigoBarra.isSelected()) {

                                produto.setCodBarra(
                                        codigo
                                );

                            } else {

                                produto.setCodigoManual(
                                        codigo
                                );
                            }

                            em.merge(produto);

                            totalActualizados++;
                        }
                    }
                }
            }

            em.getTransaction().commit();

            /*
             LIMPA EDIÇÕES
             */
            linhasEditadas.clear();

            JOptionPane.showMessageDialog(
                    this,
                    totalActualizados
                    + " produtos actualizados com sucesso!"
            );

            carregarTabela();

        } catch (Exception e) {

            e.printStackTrace();

            em.getTransaction().rollback();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro:\n" + e.getMessage()
            );

        } finally {

            em.close();
        }
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(
                new Runnable() {

            @Override
            public void run() {

                FrmActualizarCodigosProduto dialog =
                        new FrmActualizarCodigosProduto(
                                new javax.swing.JFrame(),
                                true
                        );

                dialog.setVisible(true);
            }
        });
    }
}