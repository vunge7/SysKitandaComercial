/**
 * *
 * @author Domingos Dala Vunge
 */
package rh.visao;


import java.sql.Connection;
import dao.EmpresaDao;
import dao.PedidoFeriaDao;
import dao.FuncionarioDao;
import dao.PrevioAvisoDao;
import dao.UsuarioDao;
import entity.PedidoFeria;
import entity.PrevioAviso;
import entity.TbFuncionario;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lista.RelatorioPrevioAviso;
import util.DVML;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

public class PrevioAvisoVisao extends javax.swing.JFrame
{
    
    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private TbFuncionario funcionario;
    private PrevioAviso previoAviso;
    private FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private PrevioAvisoDao previoAvisoDao = new PrevioAvisoDao( emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private EmpresaDao empresaDao = new EmpresaDao( emf );
//    private PedidoFeria pedidoFeria;
    private static String DVML_COMERCIAL = "DVML-COMERCIA, Lda";
    private DefaultTableModel modelo;
    private int cod_pedido_feria = 0;
    private int linha_actual = -1;
    private int idUser = 0;
    private Date data_actual = new Date();
    private boolean primeira_chamada_data_comeco = true;
    private int cod_funcionario = 0;
    private int idEmpresa = 0;
    
    public PrevioAvisoVisao( int idUser, int idEmpresa )
    {
        
        initComponents();
        this.idUser = idUser;
        this.idEmpresa = idEmpresa;
        setLocationRelativeTo( null );
        lbEmpresa.setText(  empresaDao.findEmpresa(idEmpresa).getNome()      );
        setComBoBox();
        lbEmpresa.setText(  empresaDao.findEmpresa(idEmpresa).getNome()      );
        limpar_dados_formulario();
        try
        {
//            busca_pedido_ferias();
        }
        catch ( Exception e )
        {
        }
        
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores() );
        preencher_dados_funcionario();
//        initDatas();
        
    }
    
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        lb_seja_bem_vindo1 = new javax.swing.JLabel();
        lbEmpresa = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        dcDataInicioContrato = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        dcDataFimContrato = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        cmbTipoContrato = new javax.swing.JComboBox<>();
        cmbDuracaoContrato = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDescricao = new javax.swing.JTextArea();
        txtNumeroDiasAviso = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Prévio Aviso");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIniciaisProfessores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIniciaisProfessoresActionPerformed(evt);
            }
        });

        cmbFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_grau_academico, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 153, 51));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel19.setText("PRÉVIO AVISO DE DEMISSÃO");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
                .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_seja_bem_vindo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dcDataInicioContrato.setEnabled(false);
        dcDataInicioContrato.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcDataInicioContratoPropertyChange(evt);
            }
        });
        dcDataInicioContrato.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                dcDataInicioContratoVetoableChange(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Data Renovação Contrato:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Data Fim Contrato:");

        dcDataFimContrato.setEnabled(false);
        dcDataFimContrato.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcDataFimContratoPropertyChange(evt);
            }
        });
        dcDataFimContrato.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                dcDataFimContratoVetoableChange(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/actualizar_1_32x32.png"))); // NOI18N
        jButton5.setText("Processar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Tipo Contrato:");

        cmbTipoContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Determinado", "Indeterminado" }));
        cmbTipoContrato.setEnabled(false);

        cmbDuracaoContrato.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Mes", "3 Meses", "6 Meses", "9 Meses", "12 Meses", "Indeterminado" }));
        cmbDuracaoContrato.setEnabled(false);

        jLabel22.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(43, 43, 43));
        jLabel22.setText("Duração Contrato");

        txtAreaDescricao.setColumns(20);
        txtAreaDescricao.setRows(5);
        txtAreaDescricao.setBorder(javax.swing.BorderFactory.createTitledBorder("Descrição"));
        jScrollPane1.setViewportView(txtAreaDescricao);

        txtNumeroDiasAviso.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Dias de Antecedência");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcDataInicioContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcDataFimContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(cmbDuracaoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtNumeroDiasAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dcDataInicioContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dcDataFimContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbTipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cmbDuracaoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroDiasAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
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
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        preencher_dados_funcionario();

        //limiteDataTermino();
    }//GEN-LAST:event_cmbFuncionariosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try {
            
             procedimento_salvar_dados();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void dcDataInicioContratoPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataInicioContratoPropertyChange
    {//GEN-HEADEREND:event_dcDataInicioContratoPropertyChange
        // TODO add your handling code here:


    }//GEN-LAST:event_dcDataInicioContratoPropertyChange

    private void dcDataFimContratoPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_dcDataFimContratoPropertyChange
    {//GEN-HEADEREND:event_dcDataFimContratoPropertyChange

    }//GEN-LAST:event_dcDataFimContratoPropertyChange

    private void dcDataInicioContratoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataInicioContratoVetoableChange
    {//GEN-HEADEREND:event_dcDataInicioContratoVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataInicioContratoVetoableChange

    private void dcDataFimContratoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException//GEN-FIRST:event_dcDataFimContratoVetoableChange
    {//GEN-HEADEREND:event_dcDataFimContratoVetoableChange
        // TODO add your handling code here:
        //verificarDataComeco();
    }//GEN-LAST:event_dcDataFimContratoVetoableChange

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
            java.util.logging.Logger.getLogger(PrevioAvisoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(PrevioAvisoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(PrevioAvisoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(PrevioAvisoVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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
                new PrevioAvisoVisao( 15, 1 ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbDuracaoContrato;
    private javax.swing.JComboBox cmbFuncionarios;
    private javax.swing.JComboBox<String> cmbTipoContrato;
    private com.toedter.calendar.JDateChooser dcDataFimContrato;
    private com.toedter.calendar.JDateChooser dcDataInicioContrato;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbEmpresa;
    private javax.swing.JLabel lb_data_nacimento;
    private javax.swing.JLabel lb_departamento;
    private javax.swing.JLabel lb_endereco;
    private javax.swing.JLabel lb_especialidade;
    private javax.swing.JLabel lb_estado_civil;
    private javax.swing.JLabel lb_funcao;
    private javax.swing.JLabel lb_grau_academico;
    private javax.swing.JLabel lb_modalidade;
    private javax.swing.JLabel lb_seja_bem_vindo1;
    private javax.swing.JLabel lb_telefone;
    private javax.swing.JTextArea txtAreaDescricao;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JTextField txtNumeroDiasAviso;
    // End of variables declaration//GEN-END:variables

//    public void setDadosFaltaModelo()
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        
//        String professor = String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 1 ) );
//        String falta = String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 3 ) );
//        
//        this.cod_pedido_feria = Integer.parseInt( String.valueOf( modelo.getValueAt( jTable1.getSelectedRow(), 0 ) ) );
//        
//        cmbProfessores.setSelectedItem( professor );
//        
//    }
    
    private boolean vazios_campos()
    {
        return false;
    }
    
    private void setComBoBox()
    {
        cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNomeByIdEmpresaCombo(idEmpresa)) );
//        cmbProfessores.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNome() ) );
    }
    
    public void alterar()
    {
        
    }
    
    public void limpar()
    {
        
    }
    
//    private boolean tabela_vazia()
//    {
//        DefaultTableModel modelo = ( DefaultTableModel ) jTable1.getModel();
//        return modelo.getRowCount() == 0;
//    }
//    
//    private void esvaziar_tabela()
//    {
//        this.modelo = ( DefaultTableModel ) jTable1.getModel();
//        this.modelo.setRowCount( 0 );
//    }
    
    private void procedimento_salvar()
    {
        int idRecibo = previoAvisoDao.getUltimoPrevioAviso();
        if ( campos_validos() )
        {
            this.previoAviso = new PrevioAviso();
            preparar_previo_aviso();
            try
            {
                salvar_previo_aviso();
//                actualizarDatas();
                limpar();
//                busca_pedido_ferias();
                JOptionPane.showMessageDialog( null, "Contrato Renovado Com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
           
                new PrevioAviso(idRecibo );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Renovar Contrato", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }
            
        }
        
    }
    
    private void procedimento_salvar_dados()
    {          
//        if ( campos_validos() )
//        {
            int idRecibo = previoAvisoDao.getUltimoPrevioAviso();
            this.previoAviso = new PrevioAviso();
            preparar_previo_aviso();
            try
            {
                salvar_previo_aviso();
//                actualizarDatas();
                limpar();
//                busca_pedido_ferias();
                JOptionPane.showMessageDialog( null, "Prévio Aviso Efectuado Com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
                new RelatorioPrevioAviso(idRecibo );
            
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Falha ao Registrar o Prévio Aviso", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
            }
            
//        }
          
    }
    
    public void processar()
    {
        
        if (!vazios_campos() ) {
            
           // if (funcionarioDao.exist_professor(this.cod_funcionario) ) {
                
           
                try {
                        this.funcionario = funcionarioDao.findTbFuncionario( this.cod_funcionario);
                        preparar_renovacao();
                        this.funcionarioDao.edit(funcionario);
                        //actualizar();
                        limpar();
                        JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!...");

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao alterar o funcionário", DVML.DVML_COMECIAL_LDA, JOptionPane.ERROR_MESSAGE);
                }

             //}else   JOptionPane.showMessageDialog(null, "Este funcionário não existe, impossivel alterar", DVML.DVML_COMECIAL_LDA, JOptionPane.WARNING_MESSAGE);
            
        }
        
    }
    
    public void preparar_renovacao(){

            
            funcionario.setTipoContrato(cmbTipoContrato.getSelectedItem().toString());
            funcionario.setFkUsuario(usuarioDao.findTbUsuario(idUser ) );
            funcionario.setDataInicioContrato(dcDataInicioContrato.getDate());
            funcionario.setDataFimContrato(dcDataFimContrato.getDate());
            funcionario.setDuracaoContrato(cmbDuracaoContrato.getSelectedItem().toString());
            funcionario.setFkEmpresa(empresaDao.findEmpresa(idEmpresa ) );
                
    }
    
//    private void procedimento_elimniar()
//    {
//        if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, "Tens a Plena Certeza que Pretendes Eliminar Falta ?  " ) )
//        {
//            try
//            {
//                int linha_selecionada = jTable1.getSelectedRow();
//                this.cod_pedido_feria = Integer.parseInt( jTable1.getModel().getValueAt( linha_selecionada, 0 ).toString() );
//                eliminar_pedido_feria();
//                busca_pedido_ferias();
//                limpar();
//                JOptionPane.showMessageDialog( null, "Registro Eliminado com Sucesso!...", DVML_COMERCIAL, JOptionPane.INFORMATION_MESSAGE );
//            }
//            catch ( Exception e )
//            {
//                JOptionPane.showMessageDialog( null, "Falha ao Eliminar o Falta", DVML_COMERCIAL, JOptionPane.ERROR_MESSAGE );
//            }
//            
//        }
//        
//    }
    
    private void salvar_previo_aviso()
    {
        previoAvisoDao.create( this.previoAviso );
    }
    
    
    private void preparar_previo_aviso()
    {
        
        this.previoAviso.setDataPrevio( new Date() );
        this.previoAviso.setDescricao(txtAreaDescricao.getText() );
        this.previoAviso.setNumeroDiasAviso(Integer.parseInt(txtNumeroDiasAviso.getText()));
        this.previoAviso.setFkFuncionario( funcionarioDao.findTbFuncionario( getIdFuncionario() ) );
        this.previoAviso.setFkUsuario( usuarioDao.findTbUsuario( this.idUser ) );
        
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
                    preencher_dados_funcionario();
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
                    preencher_dados_funcionario();
                    
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
    
//    private void busca_pedido_ferias()
//    {
//        List<PedidoFeria> itens = pedidoFeriaDao.getAllFaltaByIdFuncionario( getIdFuncionario() );
//        esvaziar_tabela();
//        
//        if ( itens != null )
//        {
//            DefaultTableModel mdelo = ( DefaultTableModel ) jTable1.getModel();
//            
//            Iterator<PedidoFeria> iterator = itens.iterator();
//            
//            while ( iterator.hasNext() )
//            {
//                PedidoFeria next = iterator.next();
//                
//                modelo.addRow( new Object[]
//                {
//                    
//                    next.getPkPedidoFeria(),
//                    MetodosUtil.getDataString( next.getDataInicio() ),
//                    MetodosUtil.getDataString( next.getDataFim() ),
//                    next.getDiasFerias()
//                
//                } );
//            }
//            
//        }
//        
//    }
    
    private int getIdFuncionario()
    {
        try
        {
            return funcionarioDao.getIdFuncionarioByNome(( String ) cmbFuncionarios.getSelectedItem() );
        }
        catch ( Exception e )
        {
            return 0;
        }
    }
    
    private void preencher_dados_funcionario()
    {
        TbFuncionario funcionario = funcionarioDao.findTbFuncionario( getIdFuncionario() );

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
//        lb_dias_uteis.setText( String.valueOf( funcionario.getFkModalidade().getDiasUteisTrabalho() ) );

        /**
         * ADICIONA OS PEDIDOS DE FÉRIAS DO FUNCIONÁRIO
         */
//        busca_pedido_ferias();
            cmbTipoContrato.setSelectedItem( funcionario.getTipoContrato());
            dcDataInicioContrato.setDate( funcionario.getDataInicioContrato());
            dcDataFimContrato.setDate( funcionario.getDataFimContrato());
            cmbDuracaoContrato.setSelectedItem( funcionario.getDuracaoContrato());
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
//        lb_dias_uteis.setText( "0" );

        /**
         * CAMPOS DOS DE INSERÇÃO DOS DADOS
         */
//        lb_dias_ferias_normal.setText( "0" );
//        lb_dias_uteis_planificados.setText( "0" );
//        lb_dias_uteis_ferias_gozados.setText( "0" );
//        
//        esvaziar_tabela();
        
    }
    
    private boolean campos_validos()
    {
        
        if ( dcDataInicioContrato.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário seleccione a data de começo.", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        else if ( dcDataFimContrato.getCalendar() == null )
        {
            JOptionPane.showMessageDialog( null, "Caro usuário seleccione a data de termino.", "Aviso", JOptionPane.WARNING_MESSAGE );
            return false;
        }
        
        return true;
    }

    
    
//    private void initDatas()
//    {
//        
//        Date ultimaData = pedidoFeriaDao.getLastDataFimPedidoByFuncionario( getIdFuncionario() );
//        
//        Calendar calendar_comeco = Calendar.getInstance();
//        if ( ultimaData == null )
//        {
//            
//            calendar_comeco.setTime( new Date() );
//            dcDataInicioContrato.setMinSelectableDate( new Date() );
//            dcDataInicioContrato.setCalendar( calendar_comeco );
//            calendar_comeco.add( Calendar.DATE, 5 );
//            dcDataInicioContrato.setMaxSelectableDate( calendar_comeco.getTime() );
//            
//            
//            
//            
//            
//            
//            
//        }
//        else
//        {
//            calendar_comeco.setTime( ultimaData );
//            dcDataInicioContrato.setMinSelectableDate( new Date() );
//            calendar_comeco.add( Calendar.DATE, 1 );
//            dcDataInicioContrato.setCalendar( calendar_comeco );
//        }
//        
//    }
    
    private void limiteMaximoDataComeco()
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dcDataFimContrato.getDate() );
            calendar.add( Calendar.DATE, -1 );
            
            dcDataInicioContrato.setMaxSelectableDate( calendar.getTime() );
        }
        catch ( Exception e )
        {
        }
    }
    

    
//    private int getDiasUteisGozados()
//    {
//        
//        return Integer.parseInt( lb_dias_uteis_ferias_gozados.getText() );
//        
//    }
//    
//    private int getDiasUteisModalidade()
//    {
//        return Integer.parseInt( lb_dias_uteis.getText() );
//        
//    }
    
//    private int getDiasUteisPlanificados()
//    {
//        return Integer.parseInt( lb_dias_uteis_planificados.getText() );
//        
//    }
    
}
