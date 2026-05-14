package visao;

import entity.ProdutoImposto;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import entity.TbUsuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import util.JPAEntityMannagerFactoryUtil;

public class FrmProdutosRepetidos extends JDialog {

    private JTable tabela;
    private DefaultTableModel model;

    private JButton btActualizar;
    private JButton btGuardarAlteracoes;
    private JButton btSair;

    private JRadioButton rbActivos;
    private JRadioButton rbTodos;
    private JRadioButton rbOcultos;

    private JTextField txtPesquisa;

    private ButtonGroup grupoStatus;

    private boolean carregandoTabela = false;
    private javax.swing.Timer timerPesquisa;

    public FrmProdutosRepetidos(
            java.awt.Frame parent,
            boolean modal
    ) {

        super(parent, modal);

        initComponents();

        carregarTabela();

        activarEdicaoPreco();
    }

    private void initComponents() {

        setTitle("Produtos");

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

        rbTodos =
                new JRadioButton(
                        "Todos Produtos Activos"
                );

        rbOcultos =
                new JRadioButton(
                        "Produtos Ocultos"
                );

        grupoStatus = new ButtonGroup();

        grupoStatus.add(rbActivos);

        grupoStatus.add(rbTodos);

        grupoStatus.add(rbOcultos);

        /*
         PESQUISA
         */
        txtPesquisa =
                new JTextField(30);

        JPanel painelTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        painelTop.add(rbActivos);

        painelTop.add(rbTodos);

        painelTop.add(rbOcultos);

        painelTop.add(txtPesquisa);

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

                return column == 0
                        || column == 2
                        || column == 4
                        || column == 5
                        || column == 7
                        || column == 8;
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

        rbTodos.addActionListener(e -> {

            btActualizar.setText("Ocultar");

            carregarTabela();
        });

        rbOcultos.addActionListener(e -> {

            btActualizar.setText("Reactivar");

            carregarTabela();
        });

        /*
         PESQUISA
         */
/*
 PESQUISA COM TIMER
 */
timerPesquisa =
        new javax.swing.Timer(
                500,
                e -> carregarTabela()
        );

timerPesquisa.setRepeats(false);

txtPesquisa.getDocument().addDocumentListener(
        new DocumentListener() {

    private void pesquisar() {

        timerPesquisa.restart();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

        pesquisar();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

        pesquisar();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

        pesquisar();
    }
});
    }

    /*
     ACTIVA EDIÇÃO PREÇO
     */
    /*
 ACTIVA EDIÇÃO PREÇO
 */
/*
 ACTIVA EDIÇÃO PREÇO
 */
private void activarEdicaoPreco() {

    tabela.putClientProperty(
            "terminateEditOnFocusLost",
            true
    );

    model.addTableModelListener(e -> {

        try {

            if (carregandoTabela) {
                return;
            }

            if (e.getType() != TableModelEvent.UPDATE) {
                return;
            }

            if (e.getColumn() != 8) {
                return;
            }

            int linha = e.getFirstRow();

            if (linha < 0) {
                return;
            }

            Object objCodigo =
                    tabela.getValueAt(linha, 1);

            if (objCodigo == null) {
                return;
            }

            Integer codigoProduto =
                    Integer.parseInt(
                            objCodigo.toString()
                    );

            Object objPreco =
                    tabela.getValueAt(linha, 8);

            if (objPreco == null) {
                return;
            }

            BigDecimal precoComIVA =
                    new BigDecimal(
                            objPreco.toString()
                    );

            EntityManager em =
                    JPAEntityMannagerFactoryUtil
                            .createEntityManager();

            try {

                em.getTransaction().begin();

                TbProduto produto =
                        em.find(
                                TbProduto.class,
                                codigoProduto
                        );

                if (produto != null) {

                    /*
                     IVA
                     */
                    BigDecimal taxaIVA =
                            BigDecimal.ZERO;

                    if (
                            produto.getProdutoImpostoList() != null
                            && !produto.getProdutoImpostoList().isEmpty()
                    ) {

                        for (ProdutoImposto pi
                                : produto.getProdutoImpostoList()) {

                            if (
                                    pi.getFkImposto() != null
                                    && pi.getFkImposto().getTaxa() != null
                            ) {

                                taxaIVA =
                                        BigDecimal.valueOf(
                                                pi.getFkImposto().getTaxa()
                                        );

                                break;
                            }
                        }
                    }

                    BigDecimal divisor =
                            BigDecimal.ONE.add(
                                    taxaIVA.divide(
                                            new BigDecimal("100"),
                                            6,
                                            RoundingMode.HALF_UP
                                    )
                            );

                    BigDecimal precoSemIVA =
                            precoComIVA.divide(
                                    divisor,
                                    2,
                                    RoundingMode.HALF_UP
                            ).setScale(
                                    6,
                                    RoundingMode.HALF_UP
                            );

                    /*
                     UTILIZADOR
                     */
                    TbUsuario usuario = null;

                    /*
                     PEGA DO HISTÓRICO
                     */
                    if (
                            produto.getTbPrecoList() != null
                            && !produto.getTbPrecoList().isEmpty()
                    ) {

                        usuario =
                                produto.getTbPrecoList()
                                        .get(0)
                                        .getFkUsuario();
                    }

                    /*
                     PRODUTO SEM PREÇO
                     */
                    if (usuario == null) {

                        usuario =
                                em.createQuery(
                                        "SELECT u FROM TbUsuario u",
                                        TbUsuario.class
                                )
                                        .setMaxResults(1)
                                        .getSingleResult();
                    }

                    /*
                     RETALHO
                     */
                    TbPreco precoRetalho =
                            new TbPreco();
                                        TbPreco precoGrosso =
                            new TbPreco();

                    precoRetalho.setPrecoVenda(
                            precoSemIVA
                    );

                    precoRetalho.setPrecoCompra(
                            precoSemIVA
                    );

                    precoRetalho.setPercentagemGanho(
                            BigDecimal.ZERO
                    );
                    

                    precoRetalho.setQtdBaixo(0);

                    precoRetalho.setQtdAlto(5);

                    precoRetalho.setRetalho(true);

                    precoRetalho.setData(
                            new Date()
                    );

                    precoRetalho.setHora(
                            new Date()
                    );

                    precoRetalho.setFkProduto(
                            produto
                    );

                    precoRetalho.setFkUsuario(
                            usuario
                    );
                    
                                        em.persist(
                            precoGrosso
                    );

//                    em.persist(
//                            precoRetalho
//                    );

                    /*
                     GROSSO
                     */
//                    TbPreco precoGrosso =
//                            new TbPreco();

                    precoGrosso.setPrecoVenda(
                            precoSemIVA
                    );

                    precoGrosso.setPrecoCompra(
                            precoSemIVA
                    );

                    precoGrosso.setPercentagemGanho(
                            BigDecimal.ZERO
                    );

                    precoGrosso.setQtdBaixo(6);

                    precoGrosso.setQtdAlto(214748364);

                    precoGrosso.setRetalho(false);

                    precoGrosso.setData(
                            new Date()
                    );

                    precoGrosso.setHora(
                            new Date()
                    );

                    precoGrosso.setFkProduto(
                            produto
                    );

                    precoGrosso.setFkUsuario(
                            usuario
                    );
                    
                                        em.persist(
                            precoRetalho
                    );

//                    em.persist(
//                            precoGrosso
//                    );
                }

                em.getTransaction().commit();

            } catch (Exception ex) {

                ex.printStackTrace();

                if (em.getTransaction().isActive()) {

                    em.getTransaction().rollback();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao actualizar preço:\n"
                        + ex.getMessage()
                );

            } finally {

                em.close();
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    });
}

    private void carregarTabela() {

        carregandoTabela = true;

        model.setRowCount(0);

        EntityManager em =
                JPAEntityMannagerFactoryUtil
                        .createEntityManager();

        try {

            /*
 PESQUISA
 */
String pesquisa =
        txtPesquisa.getText()
                .trim();

/*
 QUERY
 */
String jpql =
        "SELECT p "
        + "FROM TbProduto p "
        + "WHERE 1 = 1 ";

/*
 SOMENTE ACTIVOS REPETIDOS
 */
if (rbActivos.isSelected()) {

    jpql +=
            "AND p.status = 'Activo' ";
}

/*
 TODOS ACTIVOS
 */
if (rbTodos.isSelected()) {

    jpql +=
            "AND p.status = 'Activo' ";
}

/*
 OCULTOS
 */
if (rbOcultos.isSelected()) {

    jpql +=
            "AND p.status = 'false' ";
}

            /*
             PESQUISA
             */
            if (!pesquisa.isEmpty()) {

                jpql +=
 "AND ( "
+ "p.designacao LIKE :pesquisa "
+ "OR p.codBarra LIKE :pesquisa "
+ ") ";
            }

            jpql +=
                    "ORDER BY p.designacao ASC";

            javax.persistence.TypedQuery<TbProduto> query =
                    em.createQuery(
                            jpql,
                            TbProduto.class
                    );

//            if (!rbTodos.isSelected()) {
//
//                query.setParameter(
//                        "status",
//                        status
//                );
//            }

            if (!pesquisa.isEmpty()) {

                query.setParameter(
                        "pesquisa",
                        "%" + pesquisa + "%"
                );
            }

            List<TbProduto> lista =
                    query.getResultList();

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
                 FILTRO
                 */
                if (
                        rbTodos.isSelected()
                        || rbOcultos.isSelected()
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
                                        stock.getQuantidadeExistente();
                            }
                        }
                    }

                    /*
                     PREÇO
                     */
                    double ultimoPreco = 0;

                    if (
                            p.getTbPrecoList() != null
                            && !p.getTbPrecoList().isEmpty()
                    ) {

//                        TbPreco ultimoPrecoObj = null;
//
//                        for (TbPreco pr
//                                : p.getTbPrecoList()) {
//
//                            if (ultimoPrecoObj == null) {
//
//                                ultimoPrecoObj = pr;
//
//                            } else if (
//                                    pr.getPkPreco()
//                                    > ultimoPrecoObj.getPkPreco()
//                            ) {
//
//                                ultimoPrecoObj = pr;
//                            }
//                        }

TbPreco ultimoPrecoObj = null;

/*
 PEGA ÚLTIMO PREÇO RETALHO
 */
for (TbPreco pr : p.getTbPrecoList()) {

    if (
            pr.getRetalho() != null
            && pr.getRetalho()
    ) {

        if (ultimoPrecoObj == null) {

            ultimoPrecoObj = pr;

        } else if (
                pr.getPkPreco()
                > ultimoPrecoObj.getPkPreco()
        ) {

            ultimoPrecoObj = pr;
        }
    }
}

                        if (
                                ultimoPrecoObj != null
                                && ultimoPrecoObj.getPrecoVenda() != null
                        ) {

                            ultimoPreco =
                                    ultimoPrecoObj
                                            .getPrecoVenda()
                                            .doubleValue();

                            /*
                             IVA
                             */
                            if (
                                    p.getProdutoImpostoList() != null
                                    && !p.getProdutoImpostoList().isEmpty()
                            ) {

                                for (ProdutoImposto pi
                                        : p.getProdutoImpostoList()) {

                                    if (
                                            pi.getFkImposto() != null
                                            && pi.getFkImposto().getTaxa() != null
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
                                                ultimoPreco
                                                + valorIVA;

                                        ultimoPreco =
                                                Math.round(
                                                        ultimoPreco * 100.0
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

            tabela.setRowHeight(25);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar tabela:\n"
                    + e.getMessage()
            );

        } finally {

            carregandoTabela = false;

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

                    String designacao =
                            tabela.getValueAt(i, 2)
                                    .toString()
                                    .trim();

                    String codBarra =
                            tabela.getValueAt(i, 4)
                                    .toString()
                                    .trim();

                    String codigoManual =
                            tabela.getValueAt(i, 5)
                                    .toString()
                                    .trim();

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

        if (rbActivos.isSelected()
                || rbTodos.isSelected()) {

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

                        if (rbOcultos.isSelected()) {

                            produto.setStatus("Activo");

                        } else {

                            produto.setStatus("false");
                        }

                        em.merge(produto);
                    }
                }
            }

            em.getTransaction().commit();

            JOptionPane.showMessageDialog(
                    this,
                    rbOcultos.isSelected()
                    ? "Produtos reactivados com sucesso!"
                    : "Produtos ocultados com sucesso!"
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