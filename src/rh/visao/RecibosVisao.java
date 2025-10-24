/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rh.visao;


import java.sql.Connection;
import dao.AdiantamentoDao;
import dao.AgregadoFamiliarDao;
import dao.AnoDao;
import dao.BancoDao;
import dao.FaltaDao;
import dao.FechoPeriodoDao;
import dao.FormaPagamentoDao;
import dao.FuncionarioDao;
import dao.ItemReciboRhDao;
import dao.ItemSalarioExtraDao;
import dao.ItemSubsidioFuncionarioDao;
import dao.MesRhDao;
import dao.ReciboRhDao;
import java.util.Vector;
import dao.SalarioDao;
import entity.AgregadoFamiliar;
import entity.FechoPeriodo;
import entity.ItemReciboRh;
import entity.ItemSalarioExtra;
import entity.ReciboRh;
import entity.TbFalta;
import entity.TbFuncionario;
import entity.TbItemSubsidiosFuncionario;
import java.sql.SQLException;
import static util.MetodosUtil.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kitanda.util.CfMethods;
import lista.ListaRecibo;
import lista.ReciboSalarioColectivo;
import tabela.TabelaFuncionario;
import util.BDConexao;
import util.DVML;
import static util.DVML.DVML_COMERCIAL;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import util.ResumoTrabalhoUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RecibosVisao extends javax.swing.JFrame
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private static SalarioDao salarioDao = new SalarioDao( emf );
//    private FormaPagamentoDao salarioDao = new SalarioDao( emf );
    private ItemSubsidioFuncionarioDao itemSubsidioFuncionarioDao = new ItemSubsidioFuncionarioDao( emf );
    private static AgregadoFamiliarDao agregadoFamiliarDao = new AgregadoFamiliarDao( emf );
    private FechoPeriodoDao fechoPeriodoDao = new FechoPeriodoDao( emf );
    public static FechoPeriodo fechoPeriodo = null;
    private ReciboRh reciboRh;
    private TbFuncionario funcionario;
    private ItemReciboRh itemReciboRh;
    private static ItemReciboRhDao itemReciboRhDao = new ItemReciboRhDao( emf );
    private static FaltaDao faltaDao = new FaltaDao( emf );
    private BancoDao bancoDao = new BancoDao( emf );
    private static MesRhDao mesRhDao = new MesRhDao( emf );
    private ReciboRhDao reciboRhDao = new ReciboRhDao( emf );
    private static AnoDao anoDao = new AnoDao( emf );
    private AdiantamentoDao adiantamentoDao = new AdiantamentoDao( emf );
    private ItemSalarioExtraDao itemSalarioExtraDao = new ItemSalarioExtraDao( emf );
    private FormaPagamentoDao formaPagamentoDao = new FormaPagamentoDao( emf );
    private static BDConexao conexao;
    private static Vector<TbItemSubsidiosFuncionario> subsidioList = new Vector<>();
    private static DefaultTableModel modelo;
    private int idUsuario = 0;
    private int idEmpresa = 0;
    private static double vencimento = 0, diasTrabalhado = 0;
    public static DadosIrt taxas_tributacao;
    TabelaFuncionario t = new TabelaFuncionario();
    int rown = -1;

    public RecibosVisao( int idUsuario, int idEmpresa, BDConexao conexao )
    {
        initComponents();
        setLocationRelativeTo( null );
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.conexao = conexao;
        setComBoBoxFuncionario();
        try
        {
            fechoPeriodo = getFechoPeriodo();

            processar_salario_colectivo();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

//        txtNumeroFuncionario.getDocument().addDocumentListener( new DocumentListener()
//        {
//
//            @Override
//            public void insertUpdate( DocumentEvent e )
//            {
//               
//            }
//
//            @Override
//            public void removeUpdate( DocumentEvent e )
//            {
//                //atualizarBtnProcessar();
//            }
//
//            @Override
//            public void changedUpdate( DocumentEvent e )
//            {
//                 atualizarBtnProcessar();
//            }
//
//        } );
    }

    private void setComBoBoxFuncionario()
    {
        cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNome() ) );
        cmbPeriodo.setModel( new DefaultComboBoxModel( ( Vector ) mesRhDao.buscaTodos() ) );
        cmbAno.setModel( new DefaultComboBoxModel( ( Vector ) anoDao.buscaTodos() ) );
        cmbFormaPagamento.setModel( new DefaultComboBoxModel( ( Vector ) formaPagamentoDao.buscaTodos() ) );

        cmbPeriodo.setSelectedItem( MetodosUtil.getDescricaoMes( new Date().getMonth() ) );
        Date date = new Date();
        String ano_lectivo = String.valueOf( ( 1900 + date.getYear() ) );
        cmbAno.setSelectedItem( ano_lectivo );

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cmbPeriodo = new javax.swing.JComboBox<>();
        cmbAno = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lbDataTermino = new javax.swing.JLabel();
        lbDataComeco = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbFormaPagamento = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        lbDataComeco1 = new javax.swing.JLabel();
        lbDataTermino1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtFuncao = new javax.swing.JTextField();
        txtNumeroFuncionario = new javax.swing.JTextField();
        cmbFuncionarios = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        txtSalario = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_detalhes = new javax.swing.JTable();
        txtTotalRemuneracao = new javax.swing.JTextField();
        txtTotalDescontos = new javax.swing.JTextField();
        txtTotalReceber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnProcessarReciboIndividual = new javax.swing.JButton();
        btnReimprimirIndividual = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        lbEmpresa = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setBackground(new java.awt.Color(0, 51, 102));
        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PROCESSAMENTO DE RECIBOS");
        jLabel1.setOpaque(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/find/seach_32x32.png"))); // NOI18N
        jButton1.setText("Carregar");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - CINZA/Logout-32x32.png"))); // NOI18N
        jButton2.setText("Sair");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        cmbPeriodo.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13)); // NOI18N
        cmbPeriodo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbPeriodoActionPerformed(evt);
            }
        });

        cmbAno.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13)); // NOI18N
        cmbAno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbAnoActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Lucida Sans Typewriter", 2, 13)); // NOI18N
        jLabel19.setText("Período");

        jLabel20.setFont(new java.awt.Font("Lucida Sans Typewriter", 2, 13)); // NOI18N
        jLabel20.setText("Ano");

        lbDataTermino.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino.setText("Data de Término:");

        lbDataComeco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataComeco.setText("Data de Começo:");

        jLabel10.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel10.setText("Forma Pagamento:");

        cmbFormaPagamento.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13)); // NOI18N
        cmbFormaPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Depósito", "Transferência", "Numerário" }));
        cmbFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFormaPagamentoActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton4.setText("Processar");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        lbDataComeco1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataComeco1.setText("Data da Abertura:");

        lbDataTermino1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino1.setText("Data do Fecho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDataComeco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbDataComeco1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(cmbPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(9, 9, 9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDataTermino1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbDataComeco1, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(lbDataTermino1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbDataTermino))
                    .addComponent(lbDataComeco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton4});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 1, 13))); // NOI18N
        jPanel14.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13)); // NOI18N

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Funcionário", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel3.setText("Número Func. :");

        jLabel12.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel12.setText("Função:");

        jLabel15.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel15.setText("Nome completo:");

        txtFuncao.setEditable(false);
        txtFuncao.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        txtNumeroFuncionario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNumeroFuncionarioActionPerformed(evt);
            }
        });

        cmbFuncionarios.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFuncionariosActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Lucida Sans Typewriter", 3, 12)); // NOI18N
        jLabel14.setText("Salário Base:");

        txtSalario.setEditable(false);
        txtSalario.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalhes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        tb_detalhes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Descrição", "Remuneração", "Desconto"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tb_detalhes);
        if (tb_detalhes.getColumnModel().getColumnCount() > 0)
        {
            tb_detalhes.getColumnModel().getColumn(0).setMinWidth(250);
        }

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        txtTotalRemuneracao.setEditable(false);
        txtTotalRemuneracao.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        txtTotalDescontos.setEditable(false);
        txtTotalDescontos.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        txtTotalReceber.setEditable(false);
        txtTotalReceber.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13)); // NOI18N
        jLabel2.setText("Total Remuneracao");

        jLabel4.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13)); // NOI18N
        jLabel4.setText("Total Descontos");

        jLabel5.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13)); // NOI18N
        jLabel5.setText("Total a Receber");

        btnProcessarReciboIndividual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagem/salvar/save_32x32.png"))); // NOI18N
        btnProcessarReciboIndividual.setText("Processar");
        btnProcessarReciboIndividual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProcessarReciboIndividualActionPerformed(evt);
            }
        });

        btnReimprimirIndividual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/proucura.png"))); // NOI18N
        btnReimprimirIndividual.setText("Reimprimir");
        btnReimprimirIndividual.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnReimprimirIndividualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtTotalRemuneracao, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtTotalDescontos, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                        .addComponent(txtTotalReceber)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProcessarReciboIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReimprimirIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTotalRemuneracao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTotalDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnProcessarReciboIndividual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnReimprimirIndividual))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Salário-Individual", jPanel4);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalhes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Sans Typewriter", 0, 14))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Salário-Colectivo", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 989, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Subsídios Anuais", jPanel6);

        lb_seja_bem_vindo1.setFont(new java.awt.Font("Times New Roman", 3, 10)); // NOI18N
        lb_seja_bem_vindo1.setText("EMPRESA:");

        lbEmpresa.setFont(new java.awt.Font("Times New Roman", 3, 10)); // NOI18N
        lbEmpresa.setText("BEM VINDO!....");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - CINZA/Logout-32x32.png"))); // NOI18N
        jButton3.setText("Sair");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1010, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(59, 59, 59))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_seja_bem_vindo1)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1019, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cmbFuncionariosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFuncionariosActionPerformed
    {//GEN-HEADEREND:event_cmbFuncionariosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFuncionariosActionPerformed

    private void txtNumeroFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioActionPerformed

        buscaFuncionario( true, false );
        atualizarBtnProcessar();
    }//GEN-LAST:event_txtNumeroFuncionarioActionPerformed

    private void btnProcessarReciboIndividualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarReciboIndividualActionPerformed
    {//GEN-HEADEREND:event_btnProcessarReciboIndividualActionPerformed

        procedimento_salvar();
        atualizarBtnProcessar();
        processar_salario_colectivo();

    }//GEN-LAST:event_btnProcessarReciboIndividualActionPerformed

    private void cmbPeriodoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbPeriodoActionPerformed
    {//GEN-HEADEREND:event_cmbPeriodoActionPerformed
        procedimento_busca();

    }//GEN-LAST:event_cmbPeriodoActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTable1MouseClicked
    {//GEN-HEADEREND:event_jTable1MouseClicked
        // TODO add your handling code here:

        rown = jTable1.rowAtPoint( evt.getPoint() );
        int column = jTable1.getColumnModel().getColumnIndexAtX( evt.getX() );
        int row = evt.getY() / jTable1.getRowHeight();

        //        if ( row < jTable1.getRowCount() && row >= 0 && column < jTable1.getColumnCount() && column < jTable1.getRowCount() )
        if ( row < jTable1.getRowCount() && row >= 0 && column < jTable1.getColumnCount() && column >= 0 )
        {
            Object value = jTable1.getValueAt( row, column );
            if ( value instanceof JButton )
            {
                ( ( JButton ) value ).doClick();
                JButton boton = ( JButton ) value;

                String va1 = "" + jTable1.getValueAt( rown, 0 );
                String va2 = "" + jTable1.getValueAt( rown, 1 );
                String va3 = "" + jTable1.getValueAt( rown, 2 );
                String va4 = "" + jTable1.getValueAt( rown, 3 );
                String va5 = "" + jTable1.getValueAt( rown, 4 );

                if ( boton.getName().equals( "v" ) )
                {
                    try
                    {
//                        this.jTextArea1.setText("");
//                        this.jTextArea1.append("Numero Funcionario: "+va1);
//                        this.jTextArea1.append("\nNome: "+va2);
//                        this.jTextArea1.append("\nTelefone: "+va3);
//                        this.jTextArea1.append("\nMorada: "+va4);
//                        this.jTextArea1.append("\nActivo: "+va5);

                    }
                    catch ( Exception ex )
                    {
                        System.out.println( ex.getMessage() );
                    }

                }

            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        processar_salario_colectivo();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        procedimento_salvar_colectivo();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cmbAnoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbAnoActionPerformed
    {//GEN-HEADEREND:event_cmbAnoActionPerformed
        // TODO add your handling code here:
//        buscaFechoPeriodo();
        procedimento_busca();
    }//GEN-LAST:event_cmbAnoActionPerformed

    private void btnReimprimirIndividualActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnReimprimirIndividualActionPerformed
    {//GEN-HEADEREND:event_btnReimprimirIndividualActionPerformed
        // TODO add your handling code here:
        reimprimirReciboIndividual();
    }//GEN-LAST:event_btnReimprimirIndividualActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cmbFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_cmbFormaPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFormaPagamentoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void mcmbFormaPagamentoain( String args[] )
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
            {
                if ( "Windows".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger( RecibosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( RecibosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( RecibosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( RecibosVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new RecibosVisao( 15, 2, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcessarReciboIndividual;
    private javax.swing.JButton btnReimprimirIndividual;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public static javax.swing.JComboBox<String> cmbAno;
    private javax.swing.JComboBox<String> cmbFormaPagamento;
    private javax.swing.JComboBox cmbFuncionarios;
    public static javax.swing.JComboBox<String> cmbPeriodo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbDataComeco;
    private javax.swing.JLabel lbDataComeco1;
    private javax.swing.JLabel lbDataTermino;
    private javax.swing.JLabel lbDataTermino1;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    public static javax.swing.JTable tb_detalhes;
    private javax.swing.JTextField txtFuncao;
    private javax.swing.JTextField txtNumeroFuncionario;
    private javax.swing.JTextField txtSalario;
    public static javax.swing.JTextField txtTotalDescontos;
    public static javax.swing.JTextField txtTotalReceber;
    public static javax.swing.JTextField txtTotalRemuneracao;
    // End of variables declaration//GEN-END:variables

    private void preencher_dados_funcionario( int idFuncionario )
    {
        TbFuncionario funcionario_local = funcionarioDao.findTbFuncionario( idFuncionario );
        cmbFuncionarios.setSelectedItem( funcionario_local.getNome() );
        txtFuncao.setText( funcionario_local.getFkFuncao().getDesignacao() );
        txtSalario.setText( CfMethods.formatarComoMoeda(
                ( salarioDao.getLastSalario( funcionario_local.getIdFuncionario() ) != null ) ? salarioDao.getLastSalario( funcionario_local.getIdFuncionario() ).getValorTempo() : 0d
        ) );

    }

    private void limpar_dados_formulario()
    {
        txtNumeroFuncionario.setText( "" );
        txtFuncao.setText( "" );
        txtSalario.setText( "" );
        txtTotalRemuneracao.setText( "-" );
        txtTotalDescontos.setText( "-" );
        txtTotalReceber.setText( "-" );
        esvaziar_tabela( tb_detalhes );

    }

    public static double adicionar_irt( TbFuncionario funcionario_local, double remunueracao_salarial, boolean individual )
    {
        double desconto_irt = 0d;
        double irt = getIRT( funcionario_local, remunueracao_salarial );
        if ( individual )
        {
            modelo.addRow( new Object[]
            {
                "IRT (" + taxas_tributacao.getPercentagem() + "%)",
                CfMethods.formatarComoMoeda( 0d ),
                CfMethods.formatarComoMoeda( irt )

            } );
        }
        else
        {
            desconto_irt += getIRT( funcionario_local, remunueracao_salarial );
        }

        return desconto_irt;

    }

    public static double getIRT( TbFuncionario funcionario_local, double remunuracao_salarial )
    {
        List<AgregadoFamiliar> filhos = agregadoFamiliarDao.getAllAgregadosFamiliarByIdFuncionario(
                funcionario_local.getIdFuncionario() );
        double totalAbono = 0;

        if ( Objects.nonNull( filhos ) )
        {
            for ( AgregadoFamiliar filho : filhos )
            {
                totalAbono += filho.getValor();
            }
        }

        return new MetodosUtil().calculo_irt( funcionario_local, remunuracao_salarial,
                funcionario_local.getTbItemSubsidiosFuncionarioList(), totalAbono );

    }

    private void adicionar_remuneracao_salarial( double remuneracao_liquida )
    {
        preparar_tabela();
        modelo.addRow( new Object[]
        {
            "Vencimento",
            CfMethods.formatarComoMoeda( remuneracao_liquida ),
            CfMethods.formatarComoMoeda( 0d )
        } );

    }

    public static double adicionar_faltas( TbFuncionario funcionario, boolean individual )
    {

//        List<TbFalta> itens = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao, faltaDao );
        List<TbFalta> itens = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao, faltaDao );
        double total_desconto_hora = 0d;
        if ( itens != null )
        {

            double numero_faltas = 0d, valor_hora = 0d, desconto_hora = 0d;
            valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( emf, funcionario, conexao );
            System.out.println( "Valor Hora: " + valor_hora );
            String descricao_falta = "Faltas" + "(";
            Iterator<TbFalta> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                TbFalta next = iterator.next();
                numero_faltas += next.getNFalta();

            }
            System.out.println( "Numero de falta: " + numero_faltas );
            desconto_hora = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor_hora * numero_faltas ) );
            descricao_falta += numero_faltas + ") - " + CfMethods.formatarComoMoeda( valor_hora );

            if ( !itens.isEmpty() )
            {
                if ( individual )
                {
                    //adiciona na tabela.
                    modelo.addRow( new Object[]
                    {
                        descricao_falta,
                        CfMethods.formatarComoMoeda( 0d ),
                        CfMethods.formatarComoMoeda( desconto_hora )

                    } );
                }
                else
                {
                    total_desconto_hora += desconto_hora;
                }
            }

        }
        return total_desconto_hora;

    }

    public static double getValorFaltas( TbFuncionario funcionario, boolean individual )
    {

        List<TbFalta> itensFalta = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario( funcionario.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao, faltaDao );
        double desconto_hora = 0d;
        if ( !Objects.isNull( itensFalta ) )
        {
            double numero_faltas = 0d, valor_hora = 0d;
            valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( emf, funcionario, conexao );
            Iterator<TbFalta> iterator = itensFalta.iterator();

            while ( iterator.hasNext() )
            {
                TbFalta next = iterator.next();
                numero_faltas += next.getNFalta();

            }
            desconto_hora = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor_hora * numero_faltas ) );

        }
        return desconto_hora;

    }

    private void preparar_tabela()
    {
        this.modelo = ( DefaultTableModel ) tb_detalhes.getModel();

    }

    public static double adicionar_subsidios( List<TbItemSubsidiosFuncionario> itemSubsidiosFuncionario, boolean individual )
    {

        String descricao;
        double valor;
        double total_subsidio = 0d;

        if ( itemSubsidiosFuncionario.size() > 0 )
        {
            System.err.println( "TAMANHO SUBSIDIO: " + itemSubsidiosFuncionario.size() );
            TbFuncionario funcionario_local = itemSubsidiosFuncionario.get( 0 ).getIdFuncionarioFK();
            int dias_uteis_trabalho = funcionario_local.getFkModalidade().getDiasUteisTrabalho();
            double horas_de_faltas = FaltaDao.getNHoraIntervaloDatasFalta( funcionario_local,
                    fechoPeriodo.getDataAbertura(),
                    fechoPeriodo.getDataFecho(), conexao, faltaDao );
            double aux_valor, desconto;

            for ( int i = 0; i < itemSubsidiosFuncionario.size(); i++ )
            {
                TbItemSubsidiosFuncionario itemSubsidio = itemSubsidiosFuncionario.get( i );

                descricao = itemSubsidio.getIdSubsidioFK().getDescricao();
                valor = itemSubsidio.getValorSubsidio();
                aux_valor = valor;

                desconto = itemSubsidio.getIdSubsidioFK().getIncidencia_inss()
                        ? FaltaDao.getDescontoSubsidio( valor, dias_uteis_trabalho, horas_de_faltas )
                        : 0d;

                valor = valor - desconto;

                if ( individual )
                {
                    modelo.addRow( new Object[]
                    {
                        descricao + "(" + CfMethods.formatarComoMoeda( aux_valor ) + ")",
                        CfMethods.formatarComoMoeda( valor ),
                        CfMethods.formatarComoMoeda( 0d )

                    } );
                }
                else
                {
                    total_subsidio += valor;
                }

            }
        }

        subsidioList = ( Vector<TbItemSubsidiosFuncionario> ) itemSubsidiosFuncionario;
        return total_subsidio;

    }

    public static double getTotalSubsidiosIncidenciaINSS( List<TbItemSubsidiosFuncionario> itemSubsidiosFuncionario, boolean individual )
    {

        double valor;
        double total_subsidio = 0d;

        if ( itemSubsidiosFuncionario.size() > 0 )
        {
            TbFuncionario funcionario_local = itemSubsidiosFuncionario.get( 0 ).getIdFuncionarioFK();
            for ( int i = 0; i < itemSubsidiosFuncionario.size(); i++ )
            {
                TbItemSubsidiosFuncionario itemSubsidios = itemSubsidiosFuncionario.get( i );
                if ( itemSubsidios.getIdSubsidioFK().getIncidencia_inss() )
                {
                    int dias_uteis_trabalho = funcionario_local.getFkModalidade().getDiasUteisTrabalho();
                    double horas_de_faltas = FaltaDao.getNHoraIntervaloDatasFalta( funcionario_local,
                            fechoPeriodo.getDataAbertura(),
                            fechoPeriodo.getDataFecho(), conexao, faltaDao );
                    valor = itemSubsidios.getValorSubsidio();
                    valor = ( valor - FaltaDao.getDescontoSubsidio( valor,
                            dias_uteis_trabalho,
                            horas_de_faltas ) );

                    total_subsidio += valor;
                }
                else
                {
                    valor = itemSubsidios.getValorSubsidio();
                    total_subsidio += valor;
                }
            }
        }

        subsidioList = ( Vector<TbItemSubsidiosFuncionario> ) itemSubsidiosFuncionario;
        return total_subsidio;

    }

    private void limpar_tabela()
    {
        modelo.setRowCount( 0 );
    }

    private int getIdFuncionario()
    {
        try
        {
            return funcionarioDao.getIdFuncionarioByNome( ( String ) cmbFuncionarios.getSelectedItem() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }

    public boolean validar_combos()
    {

//        if ( cmbFormaPagamento.getSelectedIndex() == 0 )
//        {
//            JOptionPane.showMessageDialog( null, "Pf. Seleccione uma forma de pagamento!..." );
//            return false;
//        }
        return true;
    }

    public static double getRemuneracoesEspecies( TbFuncionario funcionario_local )
    {
        if ( Objects.isNull( funcionario_local ) )
        {
            return 0;
        }
        List<ItemSalarioExtra> allRemuneracoes = ItemSalarioExtraDao.getAllByData(
                fechoPeriodo.getDataAbertura(),
                fechoPeriodo.getDataFecho(), DVML.MASTER_TABLE_REMUNERACAO,
                funcionario_local.getIdFuncionario(), emf.createEntityManager() );

        double total = 0;
        for ( ItemSalarioExtra itemSalarioExtra : allRemuneracoes )
        {
            total += itemSalarioExtra.getValor();
        }

        return total;

    }

    public static double adicionar_inss( TbFuncionario funcionario,
            double remuneracao_liquida, boolean individual )
    {

        double remuneracoes_especies = getRemuneracoesEspecies( funcionario );
        double desconto_inss = 0d;
        double salario_mais_subsidios = ( remuneracao_liquida
                + getTotalSubsidiosIncidenciaINSS( funcionario.getTbItemSubsidiosFuncionarioList(),
                        individual ) )
                + remuneracoes_especies; // O Abono por lei não é incluido.

        if ( funcionario.getDescontoSegurancaSocial().equals( "Sim" ) )
        {

            if ( individual )
            {
                modelo.addRow( new Object[]
                {
                    "Inss(3%)",
                    CfMethods.formatarComoMoeda( 0d ),
                    //                    CfMethods.formatarComoMoeda( getSalarioBase( funcionario ) * 0.03 )
                    CfMethods.formatarComoMoeda( salario_mais_subsidios * 0.03 )

                } );
            }
            else
            {
//                desconto_inss += getSalarioBase( funcionario ) * 0.03;
                desconto_inss += salario_mais_subsidios * 0.03;
            }

        }
        return desconto_inss;
    }

    public static double getSalarioBase( TbFuncionario funcionario )
    {

        try
        {

            return salarioDao.getLastSalario( funcionario.getIdFuncionario() ).getValorTempo();
        }
        catch ( Exception e )
        {
            return 0d;
        }

    }

    public static double getTotalRemuneracao()
    {
        double total_remuneracao = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) tb_detalhes.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            //total_remuneracao += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 1 ) ) );
            total_remuneracao += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 1 ) ) );

        }
        txtTotalRemuneracao.setText( CfMethods.formatarComoMoeda( total_remuneracao ) );
        return total_remuneracao;

    }

    public static double getTotalDescontos()
    {
        double total_descontos = 0;

        DefaultTableModel modelo = ( DefaultTableModel ) tb_detalhes.getModel();

        for ( int i = 0; i < modelo.getRowCount(); i++ )
        {
            // total_descontos += Double.parseDouble( String.valueOf( modelo.getValueAt( i, 2 ) ) );
            total_descontos += CfMethods.parseMoedaFormatada( String.valueOf( modelo.getValueAt( i, 2 ) ) );
        }
        txtTotalDescontos.setText( CfMethods.formatarComoMoeda( total_descontos ) );
        return total_descontos;

    }

    //
    public double getTotalReceber()
    {
        //double total_remuneracao = Double.parseDouble( txtTotalRemuneracao.getText() );
        double total_remuneracao = CfMethods.parseMoedaFormatada( txtTotalRemuneracao.getText() );
        // double total_descontos = Double.parseDouble( txtTotalDescontos.getText() );
        double total_descontos = CfMethods.parseMoedaFormatada( txtTotalDescontos.getText() );
        double total_receber = total_remuneracao - total_descontos;
        txtTotalReceber.setText( CfMethods.formatarComoMoeda( total_receber ) );

        return total_receber;

    }

    private void mostrar_totais()
    {
        getTotalRemuneracao();
        getTotalDescontos();
        getTotalReceber();
    }

    private void buscaFuncionario( boolean isNumeroFuncionario, boolean isComboSeleccionado )
    {

        fechoPeriodo = getFechoPeriodo();
        if ( fechoPeriodo != null )
        {
            if ( fechoPeriodoDao.existe_abertura( fechoPeriodo ) && fechoPeriodoDao.existe_fecho( fechoPeriodo ) )
            {
                TbFuncionario funcionario_local = null;
                String numeroFuncionario = "";
                String nomeFuncionario = "";

                /**
                 * Faz a busca do funcionario pelo seu numero ou pela caixa de
                 * seleccao
                 */
                if ( isNumeroFuncionario )
                {
                    numeroFuncionario = txtNumeroFuncionario.getText();
                    if ( !numeroFuncionario.equals( "" ) )
                    {
                        funcionario_local = funcionarioDao.getFuncionarioByNumeroFuncionario( numeroFuncionario );
                        procedimento_preencher_tabela( funcionario_local );
                    }
                }
                else if ( isComboSeleccionado )
                {
                    nomeFuncionario = cmbFuncionarios.getSelectedItem().toString();
                    funcionario_local = funcionarioDao.getFuncionarioByNome( nomeFuncionario );
                }

                /**
                 * Verifica se esta nulo o funcionario
                 */
                if ( funcionario_local != null )
                {
                    cmbFuncionarios.setSelectedItem( funcionario_local.getNome() );
                    preencher_dados_funcionario( funcionario_local.getIdFuncionario() );

                }
                else
                {
                    limpar_dados_formulario();
                }
            }
            else
            {
//                if ( !fechoPeriodoDao.existe_abertura( getFechoPeriodo() ) )
//                {
//                    showMessageUtil( "Caro usuário necessita de abrir o periódo", DVML.TIPO_MENSAGEM_INFOR );
//                }
//                if ( !fechoPeriodoDao.existe_fecho( getFechoPeriodo() ) )
//                {
//                    showMessageUtil( "Caro usuário deve fechar o periódo", DVML.TIPO_MENSAGEM_INFOR );
//                }

            }
        }
        else
        {
            lbDataComeco.setText( " - - " );
            lbDataTermino.setText( " - - " );
        }

    }

    private void processar_salario_colectivo()
    {
        MetodosUtil.esvaziar_tabela( jTable1 );
        buscaFechoPeriodo();
        if ( fechoPeriodoDao.existe_abertura( fechoPeriodo ) && fechoPeriodoDao.existe_fecho( fechoPeriodo ) )
        {
            t.visualizar( jTable1, fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao );
        }

    }

    private Integer getIdFormaPagamento()
    {
        return formaPagamentoDao.getIdByDescricao( cmbFormaPagamento.getSelectedItem().toString() );
    }

    public static double adicionar_remuneracoes_or_bonos( TbFuncionario funcionario_local, int idMasterTable, boolean individual )
    {

        List<ItemSalarioExtra> allRemuneracoes = ItemSalarioExtraDao.getAllByData(
                fechoPeriodo.getDataAbertura(),
                fechoPeriodo.getDataFecho(), idMasterTable,
                funcionario_local.getIdFuncionario(), emf.createEntityManager() );

        double total_remuneracao = 0d;

        for ( int i = 0; i < allRemuneracoes.size(); i++ )
        {
            if ( individual )
            {
                modelo.addRow( new Object[]
                {
                    allRemuneracoes.get( i ).getFkMasterTable().getDesigncao(),
                    CfMethods.formatarComoMoeda( allRemuneracoes.get( i ).getValor() ),
                    CfMethods.formatarComoMoeda( 0d )

                } );
            }
            else
            {
                total_remuneracao += allRemuneracoes.get( i ).getValor();
            }

        }
        return total_remuneracao;
    }

//            TbStock stockByCodInterno = stockDao.getStockByCodInterno( produto_parm.getCodigo(), getCodigoArmazem1() );
//        if ( !Objects.isNull( stockByCodInterno ) )
//        {
//            txtArmazen1.setText( String.valueOf( stockByCodInterno.getQuantidadeExistente() ) );
//        }
//        else
//        {
//            txtArmazen1.setText( "0" );
//
//        }
    public static double adicionar_agregado_familiar( TbFuncionario funcionario_local, boolean individual )
    {

        List<AgregadoFamiliar> filhos = agregadoFamiliarDao.getAllAgregadosFamiliarByIdFuncionario( funcionario_local.getIdFuncionario() );

        if ( !Objects.isNull( filhos ) )
        {

            double total_remuneracao = 0d, total = 0d;

            for ( int i = 0; i < filhos.size(); i++ )
            {
                total += filhos.get( i ).getValor();
            }

            if ( individual )
            {
                modelo.addRow( new Object[]
                {
                    "Abono Familiar",
                    CfMethods.formatarComoMoeda( total ),
                    CfMethods.formatarComoMoeda( 0d )

                } );
            }
            else
            {
                total_remuneracao += total;
            }

            return total_remuneracao;
        }
        return 0d;

    }

    public static double adicionar_desconto( TbFuncionario funcionario_local, boolean individual )
    {
        List<ItemSalarioExtra> allRemuneracoes = ItemSalarioExtraDao.getAllByData(
                fechoPeriodo.getDataAbertura(),
                fechoPeriodo.getDataFecho(), DVML.MASTER_TABLE_DESCONTO,
                funcionario_local.getIdFuncionario(), emf.createEntityManager() );

        double total_desconto = 0d;
        for ( int i = 0; i < allRemuneracoes.size(); i++ )
        {
            if ( individual )
            {
                modelo.addRow( new Object[]
                {
                    allRemuneracoes.get( i ).getFkMasterTable().getDesigncao(),
                    CfMethods.formatarComoMoeda( 0d ),
                    CfMethods.formatarComoMoeda( allRemuneracoes.get( i ).getValor() )

                } );
            }
            else
            {
                total_desconto += allRemuneracoes.get( i ).getValor();
            }

        }

        return total_desconto;
    }

    private void procedimento_salvar()
    {
        try
        {
            if ( validar_combos() )
            {
                if ( fechoPeriodoDao.existe_periodo( getIdAno(), getIdPeriodo() ) )
                {
                    fechoPeriodo = getFechoPeriodo();
                    if ( fechoPeriodoDao.existe_abertura( fechoPeriodo ) )
                    {
                        if ( fechoPeriodoDao.existe_fecho( fechoPeriodo ) )
                        {
                            salvar_recibo_rh();
                        }
                        else
                        {
                            showMessageUtil( "Caro usuário não existe fecho para este periódo", DVML.TIPO_MENSAGEM_INFOR );
                        }
                    }
                    else
                    {
                        showMessageUtil( "Caro usuário não existe abertura para este periódo", DVML.TIPO_MENSAGEM_INFOR );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar o Recibo", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }
    }

    private void procedimento_salvar_colectivo()
    {

        try
        {

            if ( validar_combos() )
            {

                if ( fechoPeriodoDao.existe_periodo( getIdAno(), getIdPeriodo() ) )
                {
                    fechoPeriodo = getFechoPeriodo();

                    if ( fechoPeriodoDao.existe_abertura( fechoPeriodo ) )
                    {
                        if ( fechoPeriodoDao.existe_fecho( fechoPeriodo ) )
                        {
                            salvar_recibos_funcionario();
                            fechoPeriodo = null;
                            vencimento = 0;
                            diasTrabalhado = 0;
                        }
                        else
                        {
                            showMessageUtil( "Caro usuário não existe fecho para este periódo", DVML.TIPO_MENSAGEM_INFOR );
                        }
                    }
                    else
                    {
                        showMessageUtil( "Caro usuário não existe abertura para este periódo", DVML.TIPO_MENSAGEM_INFOR );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, "Falha ao Processar o Recibo", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
        }

    }

    private void salvar_recibo_rh()
    {
        ReciboRh reciboRh_local = new ReciboRh();

        reciboRh_local.setDataHora( new Date() );
        reciboRh_local.setTotalRemuneracao( CfMethods.parseMoedaFormatada( txtTotalRemuneracao.getText() ) );
        reciboRh_local.setTotalDesconto( CfMethods.parseMoedaFormatada( txtTotalDescontos.getText() ) );
        reciboRh_local.setTotalPago( CfMethods.parseMoedaFormatada( txtTotalReceber.getText() ) );
        reciboRh_local.setFkFuncionario( funcionarioDao.findTbFuncionario( getIdFuncionario() ) );
        reciboRh_local.setFkFormaPagamento( formaPagamentoDao.findFormaPagamento( getIdFormaPagamento() ) );
        reciboRh_local.setPeriodo( cmbPeriodo.getSelectedItem().toString() );
        reciboRh_local.setFkFechoPeriodo( fechoPeriodo );
        reciboRh_local.setVencimento( vencimento );
        reciboRh_local.setDiasTrabalhado( diasTrabalhado );
        try
        {
            reciboRhDao.create( reciboRh_local );
            salvar_item_recibo_rh_individual();
            this.fechoPeriodo = null;
            vencimento = 0;
            diasTrabalhado = 0;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void salvar_recibo_rh_colectivo( TbFuncionario funcionario_local, double remunuracoes, double desconto, double valor_receber )
    {
        ReciboRh reciboRh_local = new ReciboRh();

        reciboRh_local.setDataHora( new Date() );
        reciboRh_local.setTotalRemuneracao( remunuracoes );
        reciboRh_local.setTotalDesconto( desconto );
        reciboRh_local.setTotalPago( valor_receber );
        reciboRh_local.setFkFuncionario( funcionario_local );
        reciboRh_local.setFkFormaPagamento( formaPagamentoDao.findFormaPagamento( getIdFormaPagamento() ) );
        reciboRh_local.setPeriodo( cmbPeriodo.getSelectedItem().toString() );
        reciboRh_local.setFkFechoPeriodo( fechoPeriodo );
        reciboRh_local.setVencimento( vencimento );
        reciboRh_local.setDiasTrabalhado( diasTrabalhado );
        try
        {
            reciboRhDao.create( reciboRh_local );
            salvar_item_recibo_rh( reciboRhDao.findReciboRh( reciboRhDao.getLastRecibo() ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }

    }

    public void salvar_item_recibo_rh_individual()
    {

        int cod_recibo_rh = reciboRhDao.getLastRecibo();
        this.reciboRh = reciboRhDao.findReciboRh( cod_recibo_rh );

        for ( int i = 0; i < tb_detalhes.getRowCount(); i++ )
        {
            try
            {

                itemReciboRh = new ItemReciboRh();
                itemReciboRh.setFkReciboRh( this.reciboRh );
                itemReciboRh.setDescricao( String.valueOf( tb_detalhes.getModel().getValueAt( i, 0 ) ) );
                itemReciboRh.setRemuneracao( CfMethods.parseMoedaFormatada( String.valueOf( tb_detalhes.getModel().getValueAt( i, 1 ) ) ) );
                itemReciboRh.setDesconto( CfMethods.parseMoedaFormatada( String.valueOf( tb_detalhes.getModel().getValueAt( i, 2 ) ) ) );
                itemReciboRhDao.create( itemReciboRh );

            }
            catch ( Exception e )
            {
                e.printStackTrace();

                JOptionPane.showMessageDialog( null, "Falha ao registrar o recibo: " );
                break;
            }
        }
        JOptionPane.showMessageDialog( null, "Recibo efectuado com sucesso!.." );

        try
        {
            ListaRecibo listaRecibo = new ListaRecibo( Integer.parseInt( cmbAno.getSelectedItem().toString() ), cmbPeriodo.getSelectedItem().toString(), this.reciboRh.getFkFuncionario().getIdFuncionario() );
        }
        catch ( SQLException e )
        {
        }

    }

    public void salvar_recibos_funcionario() throws SQLException
    {

        TbFuncionario funcionario_local;
        double remuneracoes;
        double descontos;
        double valor_receber;
        String numero_funcioanrio;
        boolean successo = true;
        for ( int i = 0; i < jTable1.getRowCount(); i++ )
        {
            try
            {
                boolean processar = Boolean.parseBoolean( jTable1.getModel().getValueAt( i, 0 ).toString() );
                System.out.println( "activo: " + processar );
                if ( processar )
                {

                    numero_funcioanrio = jTable1.getModel().getValueAt( i, 1 ).toString();
                    funcionario_local = funcionarioDao.getFuncionarioByNumeroFuncionario( numero_funcioanrio );
                    System.out.println( "Funcionario: " + funcionario_local.getNome() );
                    remuneracoes = CfMethods.parseMoedaFormatada( String.valueOf( jTable1.getModel().getValueAt( i, 3 ) ) );
                    descontos = CfMethods.parseMoedaFormatada( String.valueOf( jTable1.getModel().getValueAt( i, 4 ) ) );
                    valor_receber = CfMethods.parseMoedaFormatada( String.valueOf( jTable1.getModel().getValueAt( i, 5 ) ) );
                    vencimento = ( salarioDao.getLastSalario( funcionario_local.getIdFuncionario() ) != null ) ? salarioDao.getLastSalario( funcionario_local.getIdFuncionario() ).getValorTempo() : 0d;
                    double horas_de_falta = faltaDao.getNumeroHorasFaltasByIdFuncionario( funcionario_local.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao );
                    diasTrabalhado = funcionario_local.getFkModalidade().getDiasUteisTrabalho() - MetodosUtil.conversaoHoraEmDia( horas_de_falta );

                    salvar_recibo_rh_colectivo( funcionario_local, remuneracoes, descontos, valor_receber );
                }

            }
            catch ( Exception e )
            {
                successo = false;
                e.printStackTrace();
                JOptionPane.showMessageDialog( null, "Falha ao registrar o recibo: " );
                break;
            }

        }

        if ( successo )
        {
            showMessageUtil( "Folha processada cum sucesso.", DVML.TIPO_MENSAGEM_INFOR );
            ReciboSalarioColectivo reciboEntradas = new ReciboSalarioColectivo( Integer.parseInt( cmbAno.getSelectedItem().toString() ), cmbPeriodo.getSelectedItem().toString() );
            processar_salario_colectivo();
        }
        else
        {
            showMessageUtil( "Falha ao processar a folha de salário coectivo.", DVML.TIPO_MENSAGEM_INFOR );
        }

    }

    private void procedimento_preencher_tabela( TbFuncionario funcionario_local )
    {
        if ( funcionario_local != null )
        {
            preparar_tabela();
            limpar_tabela();

            double salario_base = getSalarioBase( funcionario_local );
            boolean recibo_individual = true;

            /*COLUNA REMUNUERAÇÕES*/
            //remuneração líquida
            double remuneracao_liquida = ( salario_base
                    - FaltaDao.getDescontoRemuneracaoSalarialHora( funcionario_local,
                            fechoPeriodo.getDataAbertura(),
                            fechoPeriodo.getDataFecho(), conexao, faltaDao ) );

            adicionar_remuneracao_salarial( salario_base );
            //susbsidios
            adicionar_subsidios( funcionario_local.getTbItemSubsidiosFuncionarioList(), recibo_individual );
            //outras remunuerções
            adicionar_remuneracoes_or_bonos( funcionario_local, DVML.MASTER_TABLE_REMUNERACAO, recibo_individual );
            //abonos
            adicionar_remuneracoes_or_bonos( funcionario_local, DVML.MASTER_TABLE_ABONOS, recibo_individual );
            //agregado_familiar
            adicionar_agregado_familiar( funcionario_local, recibo_individual );

            /*COLUNA DESCONTO*/
            adicionar_desconto( funcionario_local, recibo_individual );
            adicionar_faltas( funcionario_local, recibo_individual );
            adicionar_irt( funcionario_local, remuneracao_liquida, recibo_individual );
            adicionar_inss( funcionario_local, remuneracao_liquida, recibo_individual );

            mostrar_totais();

            vencimento = salario_base;
            double horas_de_falta = faltaDao.getNumeroHorasFaltasByIdFuncionario( funcionario_local.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao );
            diasTrabalhado = funcionario_local.getFkModalidade().getDiasUteisTrabalho() - MetodosUtil.conversaoHoraEmDia( horas_de_falta );

        }

    }

    public void salvar_item_recibo_rh_colectivo() throws SQLException
    {

        int cod_recibo_rh = reciboRhDao.getLastRecibo();
        this.reciboRh = reciboRhDao.findReciboRh( cod_recibo_rh );

        try
        {

            itemReciboRh = new ItemReciboRh();
            itemReciboRh.setFkReciboRh( this.reciboRh );
            itemReciboRh.setDescricao( "" );
            itemReciboRh.setRemuneracao( 0d );
            itemReciboRh.setDesconto( 0d );

            itemReciboRhDao.create( itemReciboRh );

        }
        catch ( Exception e )
        {
            e.printStackTrace();

            JOptionPane.showMessageDialog( null, "Falha ao registrar o item no recibo: " );
//                break;
        }

        JOptionPane.showMessageDialog( null, "Recibo efectuado com sucesso!.." );

//        ListaRecibo listaRecibo = new ListaRecibo( cod_recibo_rh );
    }

    public static void salvar_item_recibo_rh( ReciboRh reciboRh )
    {

        double valor = 0d, remuneracao_liquida = 0d;
        String descricao;
        ItemReciboRh item;
        TbFuncionario funcionario_local = reciboRh.getFkFuncionario();
        /**
         * REMUNUERAÇÕES
         */
        {
            /**
             * Registra a remuneração líquida
             */

            try
            {
                item = new ItemReciboRh();

                valor = getSalarioBase( funcionario_local );
                valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );

                descricao = "Vencimento";
                item.setDescricao( descricao );
                item.setRemuneracao( valor );
                item.setDesconto( 0d );
                item.setFkReciboRh( reciboRh );
                itemReciboRhDao.create( item );
            }
            catch ( Exception e )
            {
                System.err.println( "falha ao processar o item remunueração." );
            }

            /**
             * Registra os subsídios
             */
            List<TbItemSubsidiosFuncionario> list_subsidio = funcionario_local.getTbItemSubsidiosFuncionarioList();
            if ( !list_subsidio.isEmpty() )
            {
                for ( TbItemSubsidiosFuncionario item_subsidio : list_subsidio )
                {
                    valor = item_subsidio.getValorSubsidio();

                    descricao = item_subsidio.getIdSubsidioFK().getDescricao();
                    int dias_uteis_trabalho = funcionario_local.getFkModalidade().getDiasUteisTrabalho();
                    double horas_de_faltas = FaltaDao.getNHoraIntervaloDatasFalta( funcionario_local, fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao, faltaDao );

//                    if ( item_subsidio.getIdSubsidioFK().getIdSubsidios() == DVML.ID_SUBSIDIO_TRANSPORTE
//                            || item_subsidio.getIdSubsidioFK().getIdSubsidios() == DVML.ID_SUBSIDIO_ALIMENTACAO )
//                    {
                    valor -= FaltaDao.getDescontoSubsidio( valor, dias_uteis_trabalho, horas_de_faltas );

                    //}
                    try
                    {
                        item = new ItemReciboRh();
                        item.setDescricao( descricao );
                        valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );
                        item.setRemuneracao( valor );
                        item.setDesconto( 0d );
                        item.setFkReciboRh( reciboRh );
                        itemReciboRhDao.create( item );
                    }
                    catch ( Exception e )
                    {
                        System.err.println( "falha ao processar o item subsídio" );
                    }

                }
            }

            /**
             * Registra outas remunerações
             */
            List<ItemSalarioExtra> allRemuneracoes = ItemSalarioExtraDao.getAllByData(
                    fechoPeriodo.getDataAbertura(),
                    fechoPeriodo.getDataFecho(), DVML.MASTER_TABLE_REMUNERACAO,
                    funcionario_local.getIdFuncionario(), emf.createEntityManager() );

            if ( !allRemuneracoes.isEmpty() )
            {
                for ( int i = 0; i < allRemuneracoes.size(); i++ )
                {
                    try
                    {
                        item = new ItemReciboRh();
                        descricao = allRemuneracoes.get( i ).getFkMasterTable().getDesigncao();
                        valor = allRemuneracoes.get( i ).getValor();

                        item.setDescricao( descricao );
                        valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );
                        item.setRemuneracao( valor );
                        item.setDesconto( 0d );
                        item.setFkReciboRh( reciboRh );
                        itemReciboRhDao.create( item );

                    }
                    catch ( Exception e )
                    {
                        System.err.println( "falha ao processar outras remunuerações." );
                    }

                }

            }

            /**
             * Registra abonos
             */
            List<ItemSalarioExtra> allAbonos = ItemSalarioExtraDao.getAllByData(
                    fechoPeriodo.getDataAbertura(),
                    fechoPeriodo.getDataFecho(), DVML.MASTER_TABLE_ABONOS,
                    funcionario_local.getIdFuncionario(), emf.createEntityManager() );

            if ( !allAbonos.isEmpty() )
            {
                for ( int i = 0; i < allAbonos.size(); i++ )
                {
                    try
                    {
                        item = new ItemReciboRh();
                        descricao = allAbonos.get( i ).getFkMasterTable().getDesigncao();
                        valor = allAbonos.get( i ).getValor();

                        item.setDescricao( descricao );
                        valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );
                        item.setRemuneracao( valor );
                        item.setDesconto( 0d );
                        item.setFkReciboRh( reciboRh );
                        itemReciboRhDao.create( item );

                    }
                    catch ( Exception e )
                    {
                        System.err.println( "falha ao processar outras remunuerações." );
                    }

                }

            }

            //Abono familiar
            {

                List<AgregadoFamiliar> filhos = agregadoFamiliarDao.getAllAgregadosFamiliarByIdFuncionario( funcionario_local.getIdFuncionario() );

                double total = 0d;

                if ( Objects.nonNull( filhos ) )
                {
                    for ( int i = 0; i < filhos.size(); i++ )
                    {
                        total += filhos.get( i ).getValor();
                    }

                    try
                    {
                        item = new ItemReciboRh();
                        descricao = "Abono Familiar";

                        item.setDescricao( descricao );
                        valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( total ) );
                        item.setRemuneracao( valor );
                        item.setDesconto( 0d );
                        item.setFkReciboRh( reciboRh );
                        itemReciboRhDao.create( item );

                    }
                    catch ( Exception e )
                    {
                        System.err.println( "falha ao processar o abono familiar." );
                    }
                }

            }

        }

        /**
         * DESCONTOS
         */
        {
            /**
             * Descontos (Outros)
             */
            List<ItemSalarioExtra> allAbonos = ItemSalarioExtraDao.getAllByData(
                    fechoPeriodo.getDataAbertura(),
                    fechoPeriodo.getDataFecho(), DVML.MASTER_TABLE_DESCONTO,
                    funcionario_local.getIdFuncionario(), emf.createEntityManager() );

            if ( !allAbonos.isEmpty() )
            {
                for ( int i = 0; i < allAbonos.size(); i++ )
                {
                    try
                    {
                        item = new ItemReciboRh();
                        descricao = allAbonos.get( i ).getFkMasterTable().getDesigncao();
                        valor = allAbonos.get( i ).getValor();
                        valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );
                        item.setDescricao( descricao );
                        item.setRemuneracao( 0d );
                        item.setDesconto( valor );
                        item.setFkReciboRh( reciboRh );
                        itemReciboRhDao.create( item );

                    }
                    catch ( Exception e )
                    {
                        System.err.println( "falha ao processar outras remunuerações." );
                    }

                }

            }

            /**
             * Faltas(valor em falta)
             */
            List<TbFalta> itens = FaltaDao.getAllFaltasByBetweenDatasByIdFncionario( funcionario_local.getIdFuncionario(), fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), false, conexao, faltaDao );
            double valor_hora = ResumoTrabalhoUtil.getValorHoraBySalario( emf, funcionario_local, conexao );
            double numero_faltas = 0d, desconto_hora = 0d;
            String descricao_falta = "Faltas" + "(";
            if ( !itens.isEmpty() )
            {
                Iterator<TbFalta> iterator = itens.iterator();
                while ( iterator.hasNext() )
                {
                    TbFalta next = iterator.next();
                    numero_faltas += next.getNFalta();

                }

                desconto_hora = ( valor_hora * numero_faltas );
                valor_hora = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor_hora ) );
                descricao_falta += numero_faltas + ") - " + valor_hora;

                try
                {
                    item = new ItemReciboRh();
                    valor = desconto_hora;
                    valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );

                    item.setDescricao( descricao_falta );
                    item.setRemuneracao( 0d );
                    item.setDesconto( valor );
                    item.setFkReciboRh( reciboRh );
                    itemReciboRhDao.create( item );

                }
                catch ( Exception e )
                {
                    System.err.println( "falha ao processar o item desconto." );
                }
            }

            /**
             * IRT (Imposto de Rendimento de Trabalho)
             */
            remuneracao_liquida = getSalarioBase( funcionario_local ) - FaltaDao.getDescontoRemuneracaoSalarialHora( funcionario_local, fechoPeriodo.getDataAbertura(), fechoPeriodo.getDataFecho(), conexao, faltaDao );
            int percentagem = taxas_tributacao( remuneracao_liquida ).getPercentagem();

            double irt = getIRT( funcionario_local, remuneracao_liquida );

            if ( taxas_tributacao.getPercentagem() > 0 )
            {
                descricao = "IRT (" + taxas_tributacao.getPercentagem() + "%)";
//                valor = getIRT( funcionario_local, remuneracao_salarial );
//                valor = getIRT( funcionario_local, remuneracao_salarial );
                valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( irt ) );

                try
                {
                    item = new ItemReciboRh();

                    item.setDescricao( descricao );
                    item.setRemuneracao( 0d );
                    item.setDesconto( valor );
                    item.setFkReciboRh( reciboRh );
                    itemReciboRhDao.create( item );

                }
                catch ( Exception e )
                {
                    System.err.println( "falha ao processar o item IRT." );
                }

            }

            if ( funcionario_local.getDescontoSegurancaSocial().equals( "Sim" ) )
            {
                double remuneracoes_especies = getRemuneracoesEspecies( funcionario_local );
                double salario_mais_subsidios = ( remuneracao_liquida
                        + getTotalSubsidiosIncidenciaINSS(
                                funcionario_local.getTbItemSubsidiosFuncionarioList(),
                                false ) )
                        + remuneracoes_especies; // O Abono por lei não é incluido.

                descricao = "Inss(3%)";
                valor = salario_mais_subsidios * 0.03;
                valor = CfMethods.parseMoedaFormatada( CfMethods.formatarComoMoeda( valor ) );

                try
                {
                    item = new ItemReciboRh();

                    item.setDescricao( descricao );
                    item.setRemuneracao( 0d );
                    item.setDesconto( valor );
                    item.setFkReciboRh( reciboRh );
                    itemReciboRhDao.create( item );

                }
                catch ( Exception e )
                {
                    System.err.println( "falha ao processar o item Inss(3%)." );
                }

            }

        }

    }

    public static int getIdAno()
    {
        return anoDao.getIdByDescricao( cmbAno.getSelectedItem().toString() );
    }

    public static int getIdPeriodo()
    {
        return mesRhDao.getIdByDescricao( cmbPeriodo.getSelectedItem().toString() );
    }

    private FechoPeriodo getFechoPeriodo()
    {
        return fechoPeriodoDao.getFechoPeriodoByIdAnoAndPerido( getIdAno(), getIdPeriodo() );
    }

    private void buscaFechoPeriodo()
    {
        fechoPeriodo = getFechoPeriodo();
        if ( fechoPeriodo != null )
        {
            try
            {
                lbDataComeco.setText( MetodosUtil.getDataBanco( fechoPeriodo.getDataAbertura() ) );
            }
            catch ( Exception e )
            {
                lbDataComeco.setText( " - - " );
            }

            try
            {
                lbDataTermino.setText( MetodosUtil.getDataBanco( fechoPeriodo.getDataFecho() ) );
            }
            catch ( Exception e )
            {
                lbDataTermino.setText( " - - " );
            }
        }
        else
        {
            lbDataComeco.setText( " - - " );
            lbDataTermino.setText( " - - " );
        }
    }

    private void procedimento_busca()
    {
        buscaFuncionario( true, false );
        buscaFechoPeriodo();
        limpar_dados_formulario();
        esvaziar_tabela( jTable1 );
    }

    private void reimprimirReciboIndividual()
    {

        if ( !txtNumeroFuncionario.getText().equals( "" ) )
        {
            try
            {
                TbFuncionario funcionarioLocal = funcionarioDao.getFuncionarioByNumeroFuncionario( txtNumeroFuncionario.getText() );
                ListaRecibo listaRecibo = new ListaRecibo( Integer.parseInt( cmbAno.getSelectedItem().toString() ), cmbPeriodo.getSelectedItem().toString(), funcionarioLocal.getIdFuncionario(), DVML.SEGUNDA_VIA_CONFORMIDADE_COM_ORIGINAL );
            }
            catch ( SQLException e )
            {
            }
        }

    }

    private void atualizarBtnProcessar()
    {
        if ( !txtNumeroFuncionario.getText().equals( "" ) )
        {
            System.out.println( "Entrei aqui dallassssssssssssssss" );
            TbFuncionario funcionarioLocal = funcionarioDao.getFuncionarioByNumeroFuncionario( txtNumeroFuncionario.getText() );
            reciboRhDao.existeReciboSalarioFuncionario( funcionarioLocal.getIdFuncionario(), getIdAno(), getIdPeriodo() );
            btnProcessarReciboIndividual.setVisible( !reciboRhDao.existeReciboSalarioFuncionario( funcionarioLocal.getIdFuncionario(), getIdAno(), getIdPeriodo() ) );
            btnReimprimirIndividual.setVisible( reciboRhDao.existeReciboSalarioFuncionario( funcionarioLocal.getIdFuncionario(), getIdAno(), getIdPeriodo() ) );
        }
        else
        {
            btnProcessarReciboIndividual.setVisible( true );
            btnReimprimirIndividual.setVisible( false );
        }

    }

    public static void main( String[] args )
    {
        new RecibosVisao( 15, 1, BDConexao.getInstancia() ).setVisible( true );
    }

}
