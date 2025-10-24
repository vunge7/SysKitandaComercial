/**
 * *
 * @author Domingos Dala Vunge
 */
package rh.visao;


import java.sql.Connection;
import dao.AnoDao;
import dao.FaltaDao;
import dao.PedidoFeriaDao;
import dao.FuncionarioDao;
import dao.MesRhDao;
import dao.UsuarioDao;
import entity.PedidoFeria;
import entity.TbFalta;
import entity.TbFuncionario;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import static util.DVML.*;
import static util.MetodosUtil.*;

import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;

public class JustificacaoFaltaVisao extends javax.swing.JFrame
{

    private final EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private final FuncionarioDao funcionarioDao = new FuncionarioDao( emf );
    private final PedidoFeriaDao pedidoFeriaDao = new PedidoFeriaDao( emf );
    private final FaltaDao faltaDao = new FaltaDao( emf );
    private final AnoDao anoDao = new AnoDao( emf );
    private final MesRhDao mesRhDao = new MesRhDao( emf );
    private TbFalta falta;

    private DefaultTableModel modelo;
    private int codFalta = 0;
    private int idUser = 0;
    private int idEmpresa = 0;
    private BDConexao conexao;
    private int linhaSeleccionada = -1;

    public JustificacaoFaltaVisao( int idUser, int idEmpresa, BDConexao conexao )
    {

        initComponents();
        setLocationRelativeTo( null );
        this.idUser = idUser;
        this.conexao = conexao;
        this.idEmpresa = idEmpresa;
        setComBoBox();
        limpar_dados_formulario();
        txtIniciaisProfessores.addKeyListener( new TratarEventoTecladoProfessores() );

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
        jLabel8 = new javax.swing.JLabel();
        txtNumeroFuncionario = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableInjustificada = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        lbDataComeco = new javax.swing.JLabel();
        cmbAno = new javax.swing.JComboBox<>();
        lbDataComeco1 = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        lbDataTermino1 = new javax.swing.JLabel();
        lbDataTermino2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableJustificada = new javax.swing.JTable();
        lbDataTermino3 = new javax.swing.JLabel();
        lbDataTermino4 = new javax.swing.JLabel();
        lbDataTermino5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtJustificacao = new javax.swing.JTextArea();
        btnProcessar = new javax.swing.JButton();
        lbCodFalta = new javax.swing.JLabel();
        lbData = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestão de Faltas");

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

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Cod .Funcionário:");

        txtNumeroFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                                    .addComponent(txtNumeroFuncionario, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtIniciaisProfessores, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
        jLabel19.setText("JUSTIFICAÇÃO DE FALTAS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(641, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jTableInjustificada.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTableInjustificada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.Falta", "Data", "nº de Hora(s)", "dia", "descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableInjustificada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableInjustificadaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableInjustificada);
        if (jTableInjustificada.getColumnModel().getColumnCount() > 0) {
            jTableInjustificada.getColumnModel().getColumn(0).setMaxWidth(100);
            jTableInjustificada.getColumnModel().getColumn(1).setMaxWidth(250);
            jTableInjustificada.getColumnModel().getColumn(2).setMaxWidth(250);
            jTableInjustificada.getColumnModel().getColumn(3).setMaxWidth(250);
        }

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbDataComeco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataComeco.setText("Ano:");

        cmbAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbDataComeco1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataComeco1.setText("Mês:");

        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDataComeco1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDataComeco, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDataComeco1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        lbDataTermino1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino1.setText("Faltas Justificadas");

        lbDataTermino2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino2.setText("Faltas Injustificadas");

        jTableJustificada.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTableJustificada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "nº de Hora(s)", "dia", "Justificativa", "Data Just."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableJustificada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableJustificadaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableJustificada);
        if (jTableJustificada.getColumnModel().getColumnCount() > 0) {
            jTableJustificada.getColumnModel().getColumn(0).setMaxWidth(250);
            jTableJustificada.getColumnModel().getColumn(1).setMaxWidth(250);
            jTableJustificada.getColumnModel().getColumn(2).setMaxWidth(250);
            jTableJustificada.getColumnModel().getColumn(4).setMaxWidth(200);
        }

        lbDataTermino3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino3.setText("Cod. Falta:");

        lbDataTermino4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino4.setText("Data:");

        lbDataTermino5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbDataTermino5.setText("Justificação:");

        txtJustificacao.setColumns(20);
        txtJustificacao.setRows(5);
        jScrollPane1.setViewportView(txtJustificacao);

        btnProcessar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/confirmacao.png"))); // NOI18N
        btnProcessar.setText("Justificar");
        btnProcessar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessarActionPerformed(evt);
            }
        });

        lbCodFalta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbCodFalta.setText("...");

        lbData.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbData.setText("....");

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
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbDataTermino2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(lbDataTermino3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(lbDataTermino4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lbCodFalta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGap(3, 3, 3))
                                                    .addComponent(lbData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGap(15, 15, 15)
                                        .addComponent(lbDataTermino5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbDataTermino1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lbDataTermino3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lbCodFalta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lbDataTermino4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lbData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(36, 36, 36))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbDataTermino5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(54, 54, 54)))
                                .addComponent(lbDataTermino2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnProcessar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(lbDataTermino1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void jTableInjustificadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInjustificadaMouseClicked
        // TODO add your handling code here:
        mostarDados();
    }//GEN-LAST:event_jTableInjustificadaMouseClicked

    private void btnProcessarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProcessarActionPerformed
    {//GEN-HEADEREND:event_btnProcessarActionPerformed
        // TODO add your handling code here:
        procedimentoJustificar();
    }//GEN-LAST:event_btnProcessarActionPerformed

    private void txtNumeroFuncionarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtNumeroFuncionarioActionPerformed
    {//GEN-HEADEREND:event_txtNumeroFuncionarioActionPerformed
        // TODO add your handling code here:
        buscaFuncionario( true, false );
    }//GEN-LAST:event_txtNumeroFuncionarioActionPerformed

    private void jTableJustificadaMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTableJustificadaMouseClicked
    {//GEN-HEADEREND:event_jTableJustificadaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableJustificadaMouseClicked

    private void cmbMesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbMesActionPerformed
    {//GEN-HEADEREND:event_cmbMesActionPerformed
        // TODO add your handling code here:
        adicionarFaltas();
    }//GEN-LAST:event_cmbMesActionPerformed

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
            java.util.logging.Logger.getLogger( JustificacaoFaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger( JustificacaoFaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger( JustificacaoFaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger( JustificacaoFaltaVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new JustificacaoFaltaVisao( 15, 1, BDConexao.getInstancia() ).setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcessar;
    private javax.swing.JComboBox<String> cmbAno;
    private static javax.swing.JComboBox cmbFuncionarios;
    private javax.swing.JComboBox<String> cmbMes;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableInjustificada;
    private javax.swing.JTable jTableJustificada;
    private javax.swing.JLabel lbCodFalta;
    private javax.swing.JLabel lbData;
    private javax.swing.JLabel lbDataComeco;
    private javax.swing.JLabel lbDataComeco1;
    private javax.swing.JLabel lbDataTermino1;
    private javax.swing.JLabel lbDataTermino2;
    private javax.swing.JLabel lbDataTermino3;
    private javax.swing.JLabel lbDataTermino4;
    private javax.swing.JLabel lbDataTermino5;
    private javax.swing.JLabel lb_data_nacimento;
    private javax.swing.JLabel lb_departamento;
    private javax.swing.JLabel lb_endereco;
    private javax.swing.JLabel lb_especialidade;
    private javax.swing.JLabel lb_estado_civil;
    private javax.swing.JLabel lb_funcao;
    private javax.swing.JLabel lb_grau_academico;
    private javax.swing.JLabel lb_modalidade;
    private javax.swing.JLabel lb_telefone;
    private javax.swing.JTextField txtIniciaisProfessores;
    private javax.swing.JTextArea txtJustificacao;
    private javax.swing.JTextField txtNumeroFuncionario;
    // End of variables declaration//GEN-END:variables

    private void setComBoBox()
    {
        cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.buscaTodosNome() ) );
        cmbMes.setModel( new DefaultComboBoxModel( ( Vector ) mesRhDao.buscaTodos() ) );
        cmbAno.setModel( new DefaultComboBoxModel( ( Vector ) anoDao.buscaTodos() ) );
        cmbMes.setSelectedItem( MetodosUtil.getDescricaoMes( new Date().getMonth() ) );
        Date date = new Date();
        String ano_lectivo = String.valueOf( ( 1900 + date.getYear() ) );
        cmbAno.setSelectedItem( ano_lectivo );
    }

    private int getAno()
    {
        return Integer.parseInt( cmbAno.getSelectedItem().toString() );
    }

    private int getIdMes()
    {
        return mesRhDao.getIdByDescricao( cmbMes.getSelectedItem().toString() );
    }

    private void adicionarFaltas()
    {
        /**
         * Adicionar faltas Injustificadas
         */
        DefaultTableModel modeloLocal = ( DefaultTableModel ) jTableInjustificada.getModel();
        modeloLocal.setRowCount( 0 );
        List<TbFalta> listaInJutificadas = faltaDao.getAllFaltasByIdFncionario( getIdFuncionario(), getAno(), getIdMes(), false, conexao );
        for ( int i = 0; i < listaInJutificadas.size(); i++ )
        {
            TbFalta faltaInjustificada = listaInJutificadas.get( i );
            modeloLocal.addRow( new Object[]
            {
                faltaInjustificada.getIdFaltaPK(),
                MetodosUtil.getDataBanco( faltaInjustificada.getData() ),
                getHora( false, faltaInjustificada.getData(), faltaInjustificada.getData() ),
                getDiaHora( getHora( false, faltaInjustificada.getData(), faltaInjustificada.getData() ) ),
                faltaInjustificada.getDescricaoFalta()

            } );
        }

        /**
         * Adicionar faltas Justificadas
         */
        DefaultTableModel modeloLocalJustificativa = ( DefaultTableModel ) jTableJustificada.getModel();
        modeloLocalJustificativa.setRowCount( 0 );
        List<TbFalta> listaJutificadas = faltaDao.getAllFaltasByIdFncionario( getIdFuncionario(), getAno(), getIdMes(), true, conexao );
        for ( int i = 0; i < listaJutificadas.size(); i++ )
        {
            System.out.println( "cheuguei aqui" );
            TbFalta faltaJustificada = listaJutificadas.get( i );
//            modeloLocalJustificativa.addRow( new Object[]
//            {
//                MetodosUtil.getDataBanco( faltaJustificada.getData() ),
//                getHora( true, faltaJustificada.getData(), faltaJustificada.getData() ),
//                getDiaHora( getHora( true, faltaJustificada.getData(), faltaJustificada.getData() ) ),
//                faltaJustificada.getDescricaoJustificativa(),
//                getDataBanco( faltaJustificada.getDataJustificativa() )
//
//            } );
        }
    }

    private void esvaziar_tabela()
    {
        this.modelo = ( DefaultTableModel ) jTableInjustificada.getModel();
        this.modelo.setRowCount( 0 );
    }

    private void procedimentoJustificar()
    {

        if ( camposValidos() )
        {
            int opcao = JOptionPane.showConfirmDialog( null, "Este proccesso é irreversível caro usuário  deseja continuar?" );

            if ( opcao == JOptionPane.YES_OPTION )
            {
                this.falta = faltaDao.findTbFalta( this.codFalta );
                prepararJustificacao();
                try
                {
                    if ( actualizar() )
                    {
                        adicionarFaltas();
                        showMessageUtil( "Falta Justificada com sucesso!...", TIPO_MENSAGEM_INFOR );
                        limparDadosFalta();
                        linhaSeleccionada = -1;
                    }
                    else
                    {
                        showMessageUtil( "Erro ao justificar a falta do funcionário", TIPO_MENSAGEM_ERRO );
                    }

                }
                catch ( Exception e )
                {
                    showMessageUtil( "Erro ao justificar a falta do funcionário", TIPO_MENSAGEM_ERRO );
                }
            }

        }

    }

    private boolean actualizar()
    {

        try
        {
            faltaDao.edit( this.falta );
            return true;
        }
        catch ( Exception e )
        {
        }
        return false;
    }

    private void prepararJustificacao()
    {
//        falta.setDataJustificativa( new Date() );
//        falta.setJustificadaInjustificada( true );
//        falta.setDescricaoJustificativa( txtJustificacao.getText() );
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

            pedidoFeriaDao.existe_pedido_feria( funcionario.getIdFuncionario() );

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
            DefaultTableModel modelo = ( DefaultTableModel ) jTableInjustificada.getModel();

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
            DefaultTableModel mdelo = ( DefaultTableModel ) jTableInjustificada.getModel();

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
        //busca_pedido_ferias( idFuncionario );
        /**
         * Adicionar as faltas na tabelas.
         */
        adicionarFaltas();

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
        esvaziar_tabela();

    }

    private void limparDadosFalta()
    {
        lbCodFalta.setText( "" );
        lbData.setText( "" );
        txtJustificacao.setText( "" );

    }

    private boolean camposValidos()
    {

        if ( linhaSeleccionada == -1 )
        {
            showMessageUtil( "Caro usuário seleccione a falta na tabela para jsutificar", TIPO_MENSAGEM_INFOR );
            return false;
        }
        else if ( txtJustificacao.getText().equals( "" ) )
        {

            showMessageUtil( "Caro usuário insira o motivo", TIPO_MENSAGEM_INFOR );
            txtJustificacao.requestFocus();
            return false;
        }

        return true;
    }

    private double getHora( boolean justificadas, Date dataInicio, Date dataFim )
    {
//        String data = getAno() + "-" + getIdMes() + "-01";
//        Date dataInicio = MetodosUtil.stringToDate( data, "yyyy-MM-dd" );
//        Date dataFim = new Date();

        return faltaDao.getNumeroHorasFaltasByIdFuncionario( getIdFuncionario(), dataInicio, dataFim, justificadas, conexao );

    }

    private double getDiaHora( double horas )
    {
        return MetodosUtil.conversaoHoraEmDia( horas );
    }

    private void mostarDados()
    {
        this.linhaSeleccionada = jTableInjustificada.getSelectedRow();
        this.codFalta = Integer.parseInt( jTableInjustificada.getValueAt( linhaSeleccionada, 0 ).toString() );
        String data = jTableInjustificada.getValueAt( linhaSeleccionada, 1 ).toString();

        /**
         * exibe os dados da tabela para o formulário
         */
        lbCodFalta.setText( String.valueOf( this.codFalta ) );
        lbData.setText( data );

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
                    cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENome( prefixo ) ) );
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
                    cmbFuncionarios.setModel( new DefaultComboBoxModel( ( Vector ) funcionarioDao.getFuncionarioLIKENome( prefixo ) ) );
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
