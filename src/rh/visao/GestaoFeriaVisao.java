/**
 * *
 * @author Domingos Dala Vunge
 */
package rh.visao;

import dao.EmpresaDao;
import dao.PedidoFeriaDao;
import dao.FuncionarioDao;
import dao.UsuarioDao;
import entity.PedidoFeria;
import entity.TbFuncionario;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

public class GestaoFeriaVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private PedidoFeriaDao pedidoFeriaDao = new PedidoFeriaDao( emf );
    private EmpresaDao empresaDao = new EmpresaDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private PedidoFeria pedidoFeria;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_pedido_feria = 0;
    private int linha_actual = -1;
    private int idUser = 0;
    private int idEmpresa = 0;
    private Date data_actual = new Date();
    private boolean primeira_chamada_data_comeco = true;

    public GestaoFeriaVisao( int idUser, int idEmpresa )
    {

        initComponents();
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        setLocationRelativeTo( null );
        lbEmpresa.setText( empresaDao.findEmpresa(idEmpresa).getNome()      );
        setComBoBox();
        limpar_dados_formulario();
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores() );

    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel3 = new javax.swing.JPanel();
        txtIniciaisProfessores = new javax.swing.JTextField();
        cmbFuncionarios = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lb_data_nacimento = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lb_telefone = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lb_estado_civil = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lb_endereco = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lb_departamento = new javax.swing.JLabel();
        lb_funcao = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lb_grau_academico = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lb_especialidade = new javax.swing.JLabel();
        lb_modalidade = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNumeroFuncionario = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        lbEmpresa = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        dcDataComeco = new com.toedter.calendar.JDateChooser();
        lbDataComeco = new javax.swing.JLabel();
        lbDataTermino = new javax.swing.JLabel();
        dcDataTermino = new com.toedter.calendar.JDateChooser();
        btnProcessar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lbDiasUteis = new javax.swing.JLabel();
        lb_dias_uteis_ferias = new javax.swing.JLabel();
        lbDiasNormal = new javax.swing.JLabel();
        lb_dias_ferias_normal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Faltas");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIniciaisProfessores.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtIniciaisProfessoresActionPerformed(evt);
            }
        });

        cmbFuncionarios.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbFuncionariosActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Iniciais Funcionário:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Data de Nascimento: ");

        lb_data_nacimento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_data_nacimento.setText("dd/MM/AAAA");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Telefone: ");

        lb_telefone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_telefone.setText("(+244) 999 999 999");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Estado Civil: ");

        lb_estado_civil.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_estado_civil.setText("Solteiro(a)");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Endereço: ");

        lb_endereco.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_endereco.setText("N/A");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("*** DADOS PESSOAIS ***");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("*** OUTROS DADOS ***");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Departamento: ");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Função: ");

        lb_departamento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_departamento.setText("N/A");

        lb_funcao.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_funcao.setText("N/A");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Grau Acadêmico: ");

        lb_grau_academico.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_grau_academico.setText("N/A");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Especialidade: ");

        lb_especialidade.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_especialidade.setText("N/A");

        lb_modalidade.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_modalidade.setText("N/A");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setText("Modalidade: ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Cod .Funcionário:");

        txtNumeroFuncionario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtNumeroFuncionarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_especialidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_modalidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_funcao, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_data_nacimento, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_estado_civil, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lb_endereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_grau_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNumeroFuncionario))
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_data_nacimento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_estado_civil, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_funcao, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_grau_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_especialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_modalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel19.setText("PLANO DE FÉRIAS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(817, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(43, 43, 43));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 16x16.png"))); // NOI18N
        jButton4.setText("sair");
        jButton4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton4ActionPerformed(evt);
            }
        });

        lb_seja_bem_vindo1.setBackground(new java.awt.Color(0, 0, 0));
        lb_seja_bem_vindo1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lb_seja_bem_vindo1.setForeground(new java.awt.Color(255, 255, 255));
        lb_seja_bem_vindo1.setText("EMPRESA:");

        lbEmpresa.setFont(new java.awt.Font("Times New Roman", 3, 13)); // NOI18N
        lbEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        lbEmpresa.setText("BEM VINDO!....");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(691, 691, 691)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "CodPedido", "D. Começo", "D. Término", "Diás de Férias"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0)
        {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(250);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(250);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(200);
        }

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dcDataComeco.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dcDataComecoPropertyChange(evt);
            }
        });
        dcDataComeco.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                dcDataComecoVetoableChange(evt);
            }
        });

        lbDataComeco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataComeco.setText("Data de Começo:");

        lbDataTermino.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino.setText("Data de Término:");

        dcDataTermino.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                dcDataTerminoPropertyChange(evt);
            }
        });
        dcDataTermino.addVetoableChangeListener(new java.beans.VetoableChangeListener()
        {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException
            {
                dcDataTerminoVetoableChange(evt);
            }
        });

        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        btnProcessar.setText("Processar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProcessarActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/eliminar_16x16.png"))); // NOI18N
        jButton3.setText("eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        lbDiasUteis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDiasUteis.setText("Dias Uteis:");

        lb_dias_uteis_ferias.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_dias_uteis_ferias.setText("0");

        lbDiasNormal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDiasNormal.setText("Dias Normal:");

        lb_dias_ferias_normal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_dias_ferias_normal.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDiasUteis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_dias_uteis_ferias, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDiasNormal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_dias_ferias_normal, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addComponent(btnProcessar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcDataTermino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbDataTermino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDiasNormal)
                    .addComponent(lb_dias_ferias_normal))
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDiasUteis)
                    .addComponent(lb_dias_uteis_ferias))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2)))
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIniciaisProfessoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIniciaisProfessoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIniciaisProfessoresActionPerformed

    private void cmbFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFuncionariosActionPerformed
        // TODO add your handling code here:
        try
        {
            buscaFuncionario( false, true );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbFuncionariosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        procedimento_elimniar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1MouseClicked

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarActionPerformed
    {//GEN-HEADEREND:event_btnProcessarActionPerformed
        // TODO add your handling code here:
        procedimento_salvar();
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void dcDataComecoPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataComecoPropertyChange
    {//GEN-HEADEREND:event_dcDataComecoPropertyChange
        // TODO add your handling code here:
        try
        {
            diferencaDatasMaiorDiasUteis();

        }
        catch ( Exception e )
        {
        }

    }//GEN-LAST:event_dcDataComecoPropertyChange

    private void dcDataTerminoPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataTerminoPropertyChange
    {//GEN-HEADEREND:event_dcDataTerminoPropertyChange
        // TODO add your handling code here:

        try
        {
            verificarDataTermino();

        }
        catch ( Exception e )
        {
        }
    }//GEN-LAST:event_dcDataTerminoPropertyChange

    private void dcDataComecoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataComecoVetoableChange
    {//GEN-HEADEREND:event_dcDataComecoVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataComecoVetoableChange

    private void dcDataTerminoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataTerminoVetoableChange
    {//GEN-HEADEREND:event_dcDataTerminoVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataTerminoVetoableChange

    private void txtNumeroFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioActionPerformed
        // TODO add your handling code here:
        buscaFuncionario( true, false );
    }//GEN-LAST:event_txtNumeroFuncionarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main( String args[] )
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
            java.util.logging.Logger.getLogger( GestaoFeriaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( GestaoFeriaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( GestaoFeriaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( GestaoFeriaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new GestaoFeriaVisao( 15, 1 ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcessar;
    private static javax.swing.JComboBox cmbFuncionarios;
    private com.toedter.calendar.JDateChooser dcDataComeco;
    private com.toedter.calendar.JDateChooser dcDataTermino;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbDataComeco;
    private javax.swing.JLabel lbDataTermino;
    private javax.swing.JLabel lbDiasNormal;
    private javax.swing.JLabel lbDiasUteis;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_data_nacimento;
    private javax.swing.JLabel lb_departamento;
    private javax.swing.JLabel lb_dias_ferias_normal;
    private javax.swing.JLabel lb_dias_uteis_ferias;
    private javax.swing.JLabel lb_endereco;
    private javax.swing.JLabel lb_especialidade;
    private javax.swing.JLabel lb_estado_civil;
    private javax.swing.JLabel lb_funcao;
    private javax.swing.JLabel lb_grau_academico;
    private javax.swing.JLabel lb_modalidade;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    private javax.swing.JLabel lb_telefone;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JTextField txtNumeroFuncionario;
    // End of variables declaration//GEN-END:variables

    private void setComBoBox()
    {
        cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNomeByIdEmpresaCombo(idEmpresa) ) );
    }

    private void esvaziar_tabela()
    {
        this.modelo = ( DefaultTableModel ) jTable1.getModel();
        this.modelo.setRowCount( 0 );
    }

    private void procedimento_salvar()
    {

        if ( campos_validos() )
        {
            this.pedidoFeria = new PedidoFeria();
            preparar_pedido_ferias();
            try
            {
                salvar_pedido_ferias();
                busca_pedido_ferias();
                JOptionPane.showMessageDialog( null, "Pedido Efectuado Com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Registrar o Pedido de Férias", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }

    }

    private void procedimento_elimniar()
    {
        if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, "Tens a Plena Certeza que Pretendes Eliminar o Registro ?  " ) )
        {
            try
            {
                int linha_selecionada = jTable1.getSelectedRow();
                this.cod_pedido_feria = Integer.parseInt( jTable1.getModel().getValueAt( linha_selecionada, 0 ).toString() );
                eliminar_pedido_feria();
                busca_pedido_ferias();
                JOptionPane.showMessageDialog( null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Eliminar o Registro", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }

        }

    }

    private void salvar_pedido_ferias()
    {
        pedidoFeriaDao.create( this.pedidoFeria );
    }

    private void eliminar_pedido_feria()
    {
        try
        {
            pedidoFeriaDao.destroy( this.cod_pedido_feria );
        }
        catch ( Exception e )
        {
        }

    }

    private void preparar_pedido_ferias()
    {

        this.pedidoFeria.setDataInicio( dcDataComeco.getDate() );
        this.pedidoFeria.setDataFim( dcDataTermino.getDate() );
        this.pedidoFeria.setDataRegistro( new Date() );
        this.pedidoFeria.setDiasFerias( getDiasUteisFerias() );
        this.pedidoFeria.setFkFuncionario( funcionarioDao.findTbFuncionario( getIdFuncionario() ) );
        this.pedidoFeria.setFkUsuario( usuarioDao.findTbUsuario( this.idUser ) );

    }

    private void buscaFuncionario( boolean isNumeroFuncionario, boolean isComboSeleccionado )
    {
        TbFuncionario funcionario = null;
        String numeroFuncionario = "";
        String nomeFuncionario = "";
        if ( isNumeroFuncionario )
        {
            numeroFuncionario = txtNumeroFuncionario.getText();
            if ( !numeroFuncionario.equals( "" ) )
            {
                funcionario = funcionarioDao.getFuncionarioByNumeroFuncionario( numeroFuncionario );
            }
        }
        else if ( isComboSeleccionado )
        {
            nomeFuncionario = cmbFuncionarios.getSelectedItem().toString();
            funcionario = funcionarioDao.getFuncionarioByNome( nomeFuncionario );
        }

        if ( funcionario != null )
        {
            cmbFuncionarios.setSelectedItem( funcionario.getNome() );
            preencher_dados_funcionario( funcionario.getIdFuncionario() );

            if ( pedidoFeriaDao.existe_pedido_feria( funcionario.getIdFuncionario() ) )
            {
                estadoVisblidadeComponetes( false );
            }
            else
            {
                estadoVisblidadeComponetes( true );
            }
        }
        else
        {
            limpar_dados_formulario();
            JOptionPane.showMessageDialog( null, "Não existe funcionário associado a este número de conta.", "Aviso", JOptionPane.WARNING_MESSAGE );
        }

    }

    private void busca_pedido_ferias()
    {
        List<PedidoFeria> itens = pedidoFeriaDao.getAllPedidoFeriasByIdFuncionario( getIdFuncionario() );
        esvaziar_tabela();

        if ( itens != null )
        {
            DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();

            Iterator<PedidoFeria> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                PedidoFeria next = iterator.next();

                modelo.addRow( new Object[]
                {

                    next.getPkPedidoFeria(),
                    MetodosUtil.getDataString( next.getDataInicio() ),
                    MetodosUtil.getDataString( next.getDataFim() ),
                    next.getDiasFerias()

                } );
            }

        }

    }

    private void busca_pedido_ferias( int idFuncionario )
    {
        List<PedidoFeria> itens = pedidoFeriaDao.getAllPedidoFeriasByIdFuncionario( idFuncionario );
        esvaziar_tabela();

        if ( itens != null )
        {
            DefaultTableModel mdelo = ( DefaultTableModel ) jTable1.getModel();

            Iterator<PedidoFeria> iterator = itens.iterator();

            while ( iterator.hasNext() )
            {
                PedidoFeria next = iterator.next();

                modelo.addRow( new Object[]
                {

                    next.getPkPedidoFeria(),
                    MetodosUtil.getDataString( next.getDataInicio() ),
                    MetodosUtil.getDataString( next.getDataFim() ),
                    next.getDiasFerias()

                } );
            }

        }

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

    private void preencher_dados_funcionario( int idFuncionario )
    {
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( idFuncionario );

        /**
         * DADOS PESSOAIS
         */
        lb_data_nacimento.setText( MetodosUtil.getDataString( funcionario.getDataNascimento() ) );
        lb_telefone.setText( funcionario.getTelefone() );
        lb_estado_civil.setText( funcionario.getFkEstadoCivil().getDesignacao() );
        //Morada é o mesmo que o endereço - infelizmente colocamos 'Morada', possivelmente nas outras versões futura do kitanda irá se rever esta questão 
        lb_endereco.setText( funcionario.getMorada() );

        /**
         * OUTROS DADOS
         */
        lb_departamento.setText( funcionario.getFkDepartamento().getDesignacao() );
        lb_grau_academico.setText( funcionario.getFkGrauAcademico().getDesignacao() );
        lb_especialidade.setText( funcionario.getFkEspecialidade().getDesignacao() );
        lb_modalidade.setText( funcionario.getFkModalidade().getDesignacao() );

        /**
         * ADICIONA OS PEDIDOS DE FÉRIAS DO FUNCIONÁRIO
         */
        busca_pedido_ferias( idFuncionario );

        initDatas( idFuncionario );
    }

    private void limpar_dados_formulario()
    {
        lb_data_nacimento.setText( "" );
        lb_telefone.setText( "" );
        lb_estado_civil.setText( "" );
        lb_endereco.setText( "" );

        /**
         * OUTROS DADOS
         */
        lb_departamento.setText( "" );
        lb_grau_academico.setText( "" );
        lb_especialidade.setText( "" );
        lb_modalidade.setText( "" );

        /**
         * CAMPOS DOS DE INSERÇÃO DOS DADOS
         */
        lb_dias_ferias_normal.setText( "0" );

        esvaziar_tabela();

    }

    private boolean campos_validos()
    {

        if ( dcDataComeco.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário seleccione a data de começo.", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( dcDataTermino.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário seleccione a data de termino.", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }

        else if ( getDiasUteisFerias() != getDiasModalidade() )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário, o periódo não pode ser inferiror ou maior do que" + getDiasModalidade() + " dias úteis de trabalho", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        return true;
    }

    private void verificarDataTermino()
    {

        if ( dcDataTermino.getCalendar() != null )
        {
            Date data_1 = dcDataTermino.getDate();
            //a data de inserção do começo não pode ser menor do que a data actual do sistema.
            if ( MetodosUtil.menor_data_1_data_2_ignore_time( data_1, data_actual ) )
            {
                dcDataTermino.setDate( null );
                JOptionPane.showMessageDialog( null, "Caro usário a data de termino não pooder menor do que a data actual do sistema", "Aviso", JOptionPane.WARNING_MESSAGE );
            }
            else
            {
                if ( dcDataComeco.getCalendar() != null )
                {
                    Date data_2 = dcDataComeco.getDate();

                    if ( MetodosUtil.menor_data_1_data_2( data_1, data_2 ) || MetodosUtil.igual_data_1_data_2( data_1, data_2 ) )
                    // if ( MetodosUtil.menor_data_1_data_2( data_1, data_2 ) )
                    {
                        dcDataTermino.setDate( null );
                        JOptionPane.showMessageDialog( null, "Caro usário a data de término não poder ser menor ou igual do que a data de começo.", "Aviso", JOptionPane.WARNING_MESSAGE );
                    }

                    limiteMaximoDataComeco();
                    diferencaDatasMaiorDiasUteis();

                }

            }

        }

    }

    private boolean diferencaDatasMaiorDiasUteis()
    {
        int diferencaDiasNormal = MetodosUtil.getTotalDiasNormal( dcDataComeco.getDate(), dcDataTermino.getDate() );
        TbFuncionario funcionarioLocal = funcionarioDao.findTbFuncionario( getIdFuncionario() );

        int diferencaDiasUteis = MetodosUtil.getDiasUteis( MetodosUtil.adicionarUmDiasNaData( dcDataComeco.getDate(), 1 ), dcDataTermino.getDate(), funcionarioLocal.getFkModalidade().getPkModalidade() );

        Long diasUteisFeriasGozadas;
        try
        {
            diasUteisFeriasGozadas = pedidoFeriaDao.getDiasUteisFeriasGozadosByIdFuncionario( getIdFuncionario() );
        }
        catch ( Exception e )
        {
            diasUteisFeriasGozadas = 0l;
        }

        int diasUteisModadalidade = funcionarioLocal.getFkModalidade().getDiasUteisTrabalho();
        Long possiveisDiasUteis;

        try
        {
            possiveisDiasUteis = ( diasUteisModadalidade - diasUteisFeriasGozadas );
        }
        catch ( Exception e )
        {
            possiveisDiasUteis = 0l;
        }

        lb_dias_ferias_normal.setText( String.valueOf( diferencaDiasNormal ) );
        lb_dias_uteis_ferias.setText( String.valueOf( diferencaDiasUteis ) );

        return diferencaDiasUteis > possiveisDiasUteis;

    }

    private void initDatas()
    {

        Date ultimaData = pedidoFeriaDao.getLastDataFimPedidoByFuncionario( getIdFuncionario() );

        Calendar calendar_comeco = Calendar.getInstance();
        Calendar calendar_termino = Calendar.getInstance();
        if ( ultimaData == null )
        {

            calendar_comeco.setTime( new Date() );
            dcDataComeco.setMinSelectableDate( new Date() );
            dcDataComeco.setCalendar( calendar_comeco );
            calendar_comeco.add( Calendar.DATE, 5 );
            dcDataComeco.setMaxSelectableDate( calendar_comeco.getTime() );

            //adiciona mais 5 dias na data de começo para o termino
            calendar_termino.add( dcDataComeco.getCalendar().DATE, 6 );
            dcDataTermino.setMinSelectableDate( calendar_termino.getTime() );

            //calendar_termino.add( );
            dcDataTermino.setDate( calendar_termino.getTime() );
            dcDataTermino.setMaxSelectableDate( MetodosUtil.stringToDate( "31/03/2021" ) );

        }
        else
        {
            calendar_comeco.setTime( ultimaData );
            dcDataComeco.setMinSelectableDate( new Date() );
            calendar_comeco.add( Calendar.DATE, 1 );
            dcDataComeco.setCalendar( calendar_comeco );
        }

    }

    private void initDatas( int idFuncionario )
    {

        Date ultimaData = pedidoFeriaDao.getLastDataFimPedidoByFuncionario( idFuncionario );

        Calendar calendar_comeco = Calendar.getInstance();
        Calendar calendar_termino = Calendar.getInstance();
        if ( ultimaData == null )
        {

            calendar_comeco.setTime( new Date() );
            dcDataComeco.setMinSelectableDate( new Date() );
            dcDataComeco.setCalendar( calendar_comeco );
            calendar_comeco.add( Calendar.DATE, 5 );
            dcDataComeco.setMaxSelectableDate( calendar_comeco.getTime() );

            //adiciona mais 30 dias na data de começo para o término
            calendar_termino.add( dcDataComeco.getCalendar().DATE, getDiasModalidade() );
            dcDataTermino.setMinSelectableDate( calendar_termino.getTime() );
            dcDataTermino.setDate( calendar_termino.getTime() );
            dcDataTermino.setMaxSelectableDate( MetodosUtil.stringToDate( "31/03/2021" ) );

        }

    }

    private void limiteMaximoDataComeco()
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( dcDataTermino.getDate() );
            calendar.add( Calendar.DATE, -1 );

            dcDataComeco.setMaxSelectableDate( calendar.getTime() );

        }
        catch ( Exception e )
        {
        }
    }

    private int getDiasUteisFerias()
    {
        return Integer.parseInt( lb_dias_uteis_ferias.getText() );

    }

    private void estadoVisblidadeComponetes( boolean status )
    {

        lbDataComeco.setVisible( status );
        dcDataComeco.setVisible( status );
        lbDataTermino.setVisible( status );
        dcDataTermino.setVisible( status );
        lbDiasNormal.setVisible( status );
        lb_dias_ferias_normal.setVisible( status );
        lbDiasUteis.setVisible( status );
        lb_dias_uteis_ferias.setVisible( status );
        btnProcessar.setVisible( status );

    }

    private int getDiasModalidade()
    {
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( getIdFuncionario() );
        return funcionario.getFkModalidade().getDiasUteisTrabalho();
    }

    class TratarEventoTecladoProfessores implements KeyListener
    {

        String prefixo = "";

        public void keyPressed( KeyEvent evt )
        {

            if ( evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && evt.getKeyCode() != KeyEvent.VK_ENTER )
            {
                char key = evt.getKeyChar();
                try
                {
                    prefixo = txtIniciaisProfessores.getText().trim() + key;
                    cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa ) ) );
                    buscaFuncionario( false, true );
                }
                catch ( Exception e )
                {
                    limpar_dados_formulario();
                }

            }
            else if ( evt.getKeyCode() == KeyEvent.VK_BACK_SPACE )
            {
                try
                {

                    prefixo = prefixo.toString().trim().substring( 0, prefixo.length() - 1 );
                    System.out.println( "NOME VOLTAR " + prefixo );
                    cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENomeByIdEmpresa(prefixo, idEmpresa ) ) );
                    buscaFuncionario( false, true );

                }
                catch ( Exception e )
                {
                    limpar_dados_formulario();
                }

            }
        }

        public void keyReleased( KeyEvent evt )
        {
        }

        public void keyTyped( KeyEvent evt )
        {
        }

    }

}
