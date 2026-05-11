/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import entity.ProdutoImposto;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import util.JPAEntityMannagerFactoryUtil;

/**
 *
 * @author Martinho
 */
public class FrmProdutosRepetidos extends JDialog {

    private JTable tabela;
    private DefaultTableModel model;

    /*
     BOTÕES
     */
    private JButton btActualizar;
    private JButton btGuardarAlteracoes;
    private JButton btSair;

    /*
     RADIO BUTTONS
     */
    private JRadioButton rbActivos;
    private JRadioButton rbOcultos;

    private ButtonGroup grupoStatus;

    public FrmProdutosRepetidos(
            java.awt.Frame parent,
            boolean modal
    ) {

        super(parent, modal);

        initComponents();

        carregarTabela();
    }

    private void initComponents() {

        setTitle("Produtos Repetidos");

        setSize(1200, 600);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        /*
         RADIO BUTTONS
         */
        rbActivos =
                new JRadioButton(
                        "Produtos Activos Repetidos",
                        true
                );

        rbOcultos =
                new JRadioButton(
                        "Produtos Ocultos"
                );

        grupoStatus = new ButtonGroup();

        grupoStatus.add(rbActivos);

        grupoStatus.add(rbOcultos);

        JPanel painelTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        painelTop.add(rbActivos);

        painelTop.add(rbOcultos);

        add(painelTop, BorderLayout.NORTH);

        /*
         MODEL
         */
        model = new DefaultTableModel(
                new Object[]{
                    "OK",
                    "Código",
                    "Designação",
                    "Tipo Produto",
                    "Código Barra",
                    "Código Manual",
                    "Qtd Stock",
                    "Factor Conversão",
                    "Último Preço Venda"
                }, 0
        ) {

            @Override
            public Class<?> getColumnClass(
                    int columnIndex
            ) {

                switch (columnIndex) {

                    case 0:
                        return Boolean.class;

                    case 6:
                    case 7:
                    case 8:
                        return Double.class;

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
                 EDITÁVEIS:
                 
                 0 = checkbox
                 2 = designação
                 4 = código barra
                 5 = código manual
                 7 = factor conversão
                 */
                return column == 0
                        || column == 2
                        || column == 4
                        || column == 5
                        || column == 7;
            }
        };

        /*
         TABELA
         */
        tabela = new JTable(model);

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
                new JButton("Ocultar");

        btGuardarAlteracoes =
                new JButton("Guardar Alterações");

        btSair =
                new JButton("Sair");

        painelBotoes.add(btActualizar);

        painelBotoes.add(btGuardarAlteracoes);

        painelBotoes.add(btSair);

        add(painelBotoes, BorderLayout.SOUTH);

        /*
         EVENTOS
         */
        btActualizar.addActionListener(
                e -> actualizarStatus()
        );

        btGuardarAlteracoes.addActionListener(
                e -> guardarAlteracoes()
        );

        btSair.addActionListener(
                e -> dispose()
        );

        rbActivos.addActionListener(e -> {

            btActualizar.setText("Ocultar");

            carregarTabela();
        });

        rbOcultos.addActionListener(e -> {

            btActualizar.setText("Reactivar");

            carregarTabela();
        });
    }

    private void carregarTabela() {

        model.setRowCount(0);

        EntityManager em =
                JPAEntityMannagerFactoryUtil
                        .createEntityManager();

        try {

            /*
             STATUS
             */
            String status = "Activo";

            if (rbOcultos.isSelected()) {

                status = "false";
            }

            /*
             QUERY
             */
            String jpql =
                    "SELECT p " +
                    "FROM TbProduto p " +
                    "WHERE p.status = :status " +
                    "ORDER BY p.designacao ASC";

            List<TbProduto> lista =
                    em.createQuery(
                            jpql,
                            TbProduto.class
                    )
                            .setParameter(
                                    "status",
                                    status
                            )
                            .getResultList();

            /*
             MAPA REPETIDOS
             */
            Map<String, Integer> mapa =
                    new HashMap<>();

            for (TbProduto p : lista) {

                String nome = "";

                if (p.getDesignacao() != null) {

                    nome =
                            p.getDesignacao()
                                    .trim()
                                    .toUpperCase();
                }

                if (mapa.containsKey(nome)) {

                    mapa.put(
                            nome,
                            mapa.get(nome) + 1
                    );

                } else {

                    mapa.put(nome, 1);
                }
            }

            /*
             PREENCHIMENTO
             */
            for (TbProduto p : lista) {

                String nome = "";

                if (p.getDesignacao() != null) {

                    nome =
                            p.getDesignacao()
                                    .trim()
                                    .toUpperCase();
                }

                /*
                 MOSTRA:
                 
                 - repetidos activos
                 - todos ocultos
                 */
                if (
                        rbOcultos.isSelected()
                        || mapa.get(nome) > 1
                ) {

                    /*
                     TIPO PRODUTO
                     */
                    String tipoProduto = "";

                    if (p.getCodTipoProduto() != null) {

                        tipoProduto =
                                p.getCodTipoProduto()
                                        .getDesignacao();
                    }

                    /*
                     STOCK
                     */
                    double stockTotal = 0;

                    if (p.getTbStockList() != null) {

                        for (TbStock stock
                                : p.getTbStockList()) {

                            if (
                                    stock.getQuantidadeExistente()
                                    != null
                            ) {

                                stockTotal +=
                                        stock
                                                .getQuantidadeExistente();
                            }
                        }
                    }

                    /*
                     PREÇO
                     */
                    double ultimoPreco = 0;

                    if (
                            p.getTbPrecoList() != null
                            && !p.getTbPrecoList()
                                    .isEmpty()
                    ) {

                        TbPreco ultimoPrecoObj = null;

                        for (TbPreco pr
                                : p.getTbPrecoList()) {

                            if (ultimoPrecoObj == null) {

                                ultimoPrecoObj = pr;

                            } else if (
                                    pr.getPkPreco()
                                    > ultimoPrecoObj
                                            .getPkPreco()
                            ) {

                                ultimoPrecoObj = pr;
                            }
                        }

                        if (
                                ultimoPrecoObj != null
                                && ultimoPrecoObj
                                        .getPrecoVenda()
                                != null
                        ) {

                            /*
                             PREÇO BASE
                             */
                            ultimoPreco =
                                    ultimoPrecoObj
                                            .getPrecoVenda()
                                            .doubleValue();

                            /*
                             IVA
                             */
                            if (
                                    p.getProdutoImpostoList()
                                    != null
                                    && !p.getProdutoImpostoList()
                                            .isEmpty()
                            ) {

                                for (ProdutoImposto pi
                                        : p.getProdutoImpostoList()) {

                                    if (
                                            pi.getFkImposto()
                                            != null
                                            && pi.getFkImposto()
                                                    .getTaxa()
                                            != null
                                    ) {

                                        double taxaIVA =
                                                pi.getFkImposto()
                                                        .getTaxa();

                                        double valorIVA =
                                                ultimoPreco
                                                * (
                                                taxaIVA / 100
                                                );

                                        ultimoPreco =
                                                Math.round(
                                                        (
                                                        ultimoPreco
                                                        + valorIVA
                                                        ) * 100.0
                                                ) / 100.0;

                                        break;
                                    }
                                }
                            }
                        }
                    }

                    /*
                     FACTOR CONVERSÃO
                     */
                    double factorConversao = 0;

                    if (
                            p.getFactorConversao()
                            != null
                    ) {

                        factorConversao =
                                p.getFactorConversao();
                    }

                    /*
                     LINHA
                     */
                    model.addRow(new Object[]{
                        false,
                        p.getCodigo(),
                        p.getDesignacao(),
                        tipoProduto,
                        p.getCodBarra(),
                        p.getCodigoManual(),
                        stockTotal,
                        factorConversao,
                        ultimoPreco
                    });
                }
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
                    .setPreferredWidth(70);

            tabela.getColumnModel()
                    .getColumn(2)
                    .setPreferredWidth(300);

            tabela.getColumnModel()
                    .getColumn(3)
                    .setPreferredWidth(150);

            tabela.getColumnModel()
                    .getColumn(4)
                    .setPreferredWidth(150);

            tabela.getColumnModel()
                    .getColumn(5)
                    .setPreferredWidth(150);

            tabela.getColumnModel()
                    .getColumn(6)
                    .setPreferredWidth(100);

            tabela.getColumnModel()
                    .getColumn(7)
                    .setPreferredWidth(100);

            tabela.getColumnModel()
                    .getColumn(8)
                    .setPreferredWidth(120);

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
     GUARDA ALTERAÇÕES
     */
    private void guardarAlteracoes() {

        int confirmar =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja guardar as alterações?",
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

            for (int i = 0;
                    i < tabela.getRowCount();
                    i++) {

                Integer codigo =
                        (Integer)
                        tabela.getValueAt(i, 1);

                TbProduto produto =
                        em.find(
                                TbProduto.class,
                                codigo
                        );

                if (produto != null) {

                    /*
                     DESIGNAÇÃO
                     */
                    String designacao =
                            tabela.getValueAt(i, 2)
                                    .toString()
                                    .trim();

                    /*
                     CÓDIGO BARRA
                     */
                    String codBarra =
                            tabela.getValueAt(i, 4)
                                    .toString()
                                    .trim();

                    /*
                     CÓDIGO MANUAL
                     */
                    String codigoManual =
                            tabela.getValueAt(i, 5)
                                    .toString()
                                    .trim();

                    /*
                     FACTOR CONVERSÃO
                     */
                    Double factorConversao = 0.0;

                    try {

                        factorConversao =
                                Double.parseDouble(
                                        tabela
                                                .getValueAt(i, 7)
                                                .toString()
                                );

                    } catch (Exception e) {

                        factorConversao = 0.0;
                    }

                    /*
                     ACTUALIZA ENTITY
                     */
                    produto.setDesignacao(
                            designacao
                    );

                    produto.setCodBarra(
                            codBarra
                    );

                    produto.setCodigoManual(
                            codigoManual
                    );

                    produto.setFactorConversao(
                            factorConversao
                    );

                    /*
                     SAVE
                     */
                    em.merge(produto);
                }
            }

            em.getTransaction().commit();

            JOptionPane.showMessageDialog(
                    this,
                    "Alterações guardadas com sucesso!"
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

    /*
     OCULTAR / REACTIVAR
     */
    private void actualizarStatus() {

        String mensagem = "";

        if (rbActivos.isSelected()) {

            mensagem =
                    "Deseja ocultar os produtos seleccionados?";

        } else {

            mensagem =
                    "Deseja reactivar os produtos seleccionados?";
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

            for (
                    int i = 0;
                    i < tabela.getRowCount();
                    i++
            ) {

                Boolean marcado =
                        (Boolean)
                        tabela.getValueAt(i, 0);

                if (marcado != null && marcado) {

                    Integer codigo =
                            (Integer)
                            tabela.getValueAt(i, 1);

                    TbProduto produto =
                            em.find(
                                    TbProduto.class,
                                    codigo
                            );

                    if (produto != null) {

                        /*
                         STATUS
                         */
                        if (rbActivos.isSelected()) {

                            produto.setStatus("false");

                        } else {

                            produto.setStatus("Activo");
                        }

                        em.merge(produto);
                    }
                }
            }

            em.getTransaction().commit();

            JOptionPane.showMessageDialog(
                    this,
                    rbActivos.isSelected()
                    ? "Produtos ocultados com sucesso!"
                    : "Produtos reactivados com sucesso!"
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

                FrmProdutosRepetidos dialog =
                        new FrmProdutosRepetidos(
                                new javax.swing.JFrame(),
                                true
                        );

                dialog.setVisible(true);
            }
        });
    }
}