/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visao;


import java.sql.Connection;
import dao.DadosInstituicaoDao;
import dao.ItemPermissaoDao;
import dao.ProdutoDao;
import dao.UsuarioDao;
import entity.TbDadosInstituicao;
import entity.TbItemPermissao;
import entity.TbProduto;
import entity.TbUsuario;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static kitanda.util.CfConstantes.YYYYMMDD_HHMMSS;
import lista.*;
import util.BDConexao;
import util.DVML;
import util.JPAEntityMannagerFactoryUtil;
import util.MetodosUtil;
import static util.MetodosUtil.rodarComandoWindows;
import util.OperacaoSistemaUtil;

/**
 *
 * @author Domingos Dala vunge
 *
 */
public class MenuPrincipalTransporteVisao extends javax.swing.JFrame
{

    private EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private ItemPermissaoDao itemPermissaoDao = new ItemPermissaoDao(emf );
    private UsuarioDao usuarioDao = new UsuarioDao( emf );
    private DadosInstituicaoDao dadosInstituicaoDao = new DadosInstituicaoDao( emf );
    private int cod_utilizador;
    private int idEmpresa;
    private boolean administrador = false;
    private int id_armazem = 0;
    private BDConexao conexao;
    private OperacaoSistemaUtil osu = new OperacaoSistemaUtil();

    public MenuPrincipalTransporteVisao( int cod, int id_empresa, boolean administrador, BDConexao conexao )
    {

        this.conexao = conexao;
        initComponents();
        btnProdutosEspirados.setVisible( false );
        jmFrontOffice.setVisible( false );
        setLocationRelativeTo( null );
//        jMenuItem11.setVisible( false);
        jmFamilia.setVisible( false );
        jMenuItem12.setVisible( false );
        jmFamilia.setVisible( false );

        setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        setResizable( true );

        //DESATIVR ENTRADAS
        //        jmEntradaProdutos.setVisible ( true );
        //DESATIVAR ITENS DO MENU
        //jmRelatorioVendasPorUsuarioData.setVisible ( false);
        //jmRelatorioVendasPorUsuarioData.setVisible ( false);
        setArmazem( dadosInstituicaoDao.findTbDadosInstituicao( 1 ).getConfigArmazens() );
        jmVendaDetalhadasClientes.setVisible( false );
        jmRelatorioVendasPorClienteData.setVisible( true );
//        jmAnulamentoEntradaProdutos.setVisible( false );
        jmListagensTodosProdutoComDesconto.setVisible( false );

        this.setExtendedState(this.getExtendedState() | MenuPrincipalTransporteVisao.MAXIMIZED_BOTH );

        cod_utilizador = cod;
        idEmpresa = id_empresa;
        this.administrador = administrador;

        jmSolicitacaoCompras.setVisible( false );
        jmAutorizacaoCompras.setVisible( false );
        jmEncomendas.setVisible( false );

        boas_vinda();
        busca_permissao();
        mostrarNumeroDeprodutosExpirados();
        jMenuItemGestaoCreditos.setVisible( false );
        //jmRelatorioDiario.setEnabled(false);
    }

    public void busca_permissao()
    {

        try
        {
            // TODO add your handling code here
            setStatusUsuario( ( Vector ) itemPermissaoDao.getAllPermissoesByIdUsuarioAndModulo( this.cod_utilizador, DVML.MODULO_GESTAO_COMERCIAL ) );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

    }

    private void boas_vinda()
    {
        TbUsuario usuario = usuarioDao.findTbUsuario( this.cod_utilizador );

        System.err.println( "usuario: " + this.cod_utilizador );
        if ( usuario.getCodigoSexo().getCodigo() == 1 )
        {
            lb_nome_usuario.setText( "Seja bem vindo! Sr.º  " + usuario.getNome() );
        }
        else
        {
            lb_nome_usuario.setText( "Seja bem vinda! Sr.ª  " + usuario.getNome() );
        }

    }

    public void setStatusUsuario( Vector<TbItemPermissao> vector )
    {

        String permissao = "";
        //limpa
        setPermissoes( false );

        for ( int i = 0; i < vector.size(); i++ )
        {

            permissao = vector.get( i ).getIdPermissao().getDescricao();

            System.out.println( "PERMISSAO PRINCIPAL " + permissao );

            if ( permissao.equals( "Venda" ) )
            {
                jmVenda.setEnabled( true );
            } //            else if ( permissao.equals ( "Gestao Credito" ) )
            //            {
            //                jmGestaoCredito.setEnabled ( true );
            //            }
            else if ( permissao.equals( "Relatorio por Fornecedor" ) )
            {
                jmRelatorioPorFonecedor.setEnabled( true );
            }
            else if ( permissao.equals( jmListarUsuario.getText() ) )
            {
                jmListarUsuario.setEnabled( true );
            }
            else if ( permissao.equals( jmReactivarProdutos.getText() ) )
            {
                jmReactivarProdutos.setEnabled( true );
            }
            else if ( permissao.equals( jmListarProdutosStock.getText() ) )
            {
                jmListarProdutosStock.setEnabled( true );
            }
            else if ( permissao.equals( jMenuItemRelatorioQtdDetalhado.getText() ) )
            {
                jMenuItemRelatorioQtdDetalhado.setEnabled( true );
            }
            else if ( permissao.equals( jMenuItemRelatorioQuebras.getText() ) )
            {
                jMenuItemRelatorioQuebras.setEnabled( true );
            }
//            else if ( permissao.equals( jmListagemVasilhames.getText() ) )
//            {
//                jmListagemVasilhames.setEnabled( true );
//            }
            else if ( permissao.equals( jmTodasVendas.getText() ) )
            {
                jmTodasVendas.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioVendasPorUsuarioData.getText() ) )
            {
                jmRelatorioVendasPorUsuarioData.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioDiarioTodasVendasTempoReal.getText() ) )
            {
                jmRelatorioDiarioTodasVendasTempoReal.setEnabled( true );
            } //            else if ( permissao.equals ( jmRelatorioPreForma.getText () ) )
            //            {
            //                jmRelatorioPreForma.setEnabled ( true );
            //            }
            //            else if ( permissao.equals ( jmVendaCredito.getText () ) )
            //            {
            //                jmVendaCredito.setEnabled ( true );
            //            }
            //            else if ( permissao.equals ( jmRegistroEntradas.getText () ) )
            //            {
            //                jmRegistroEntradas.setEnabled ( true );
            //            }
            else if ( permissao.equals( jmRelatorioDiario.getText() ) )
            {
                jmRelatorioDiario.setEnabled( true );
            }
            else if ( permissao.equals( jmListagensTodosProdutos.getText() ) )
            {
                jmListagensTodosProdutos.setEnabled( true );
            }
            else if ( permissao.equals( jmListagensTodosProdutoComDesconto.getText() ) )
            {
                jmListagensTodosProdutoComDesconto.setEnabled( true );
            }
            else if ( permissao.equals( jmProdutosActualizar.getText() ) )
            {
                jmProdutosActualizar.setEnabled( true );
            }
            else if ( permissao.equals( jmVendaDetalhadasUsuarios.getText() ) )
            {
                jmVendaDetalhadasUsuarios.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioFormaPagamento.getText() ) )
            {
                jmRelatorioFormaPagamento.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioTodosServicos.getText() ) )
            {
                jmRelatorioTodosServicos.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioNotasCredito.getText() ) )
            {
                jmRelatorioNotasCredito.setEnabled( true );
            } //            else if ( permissao.equals ( jmRelatorioVendasPorUsuarioData.getText () ) )
            //            {
            //                jmRelatorioVendasPorUsuarioData.setEnabled ( true );
            //            }
            else if ( permissao.equals( jmRelatorioAcertoStock.getText() ) )
            {
                jmRelatorioAcertoStock.setEnabled( true );
            }
            else if ( permissao.equals( jmHistoricoBonusEmpresa.getText() ) )
            {
                jmHistoricoBonusEmpresa.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioComprasPorFornecedor.getText() ) )
            {
                jmRelatorioComprasPorFornecedor.setEnabled( true );
            }
            else if ( permissao.equals( jmReeprmirCompra.getText() ) )
            {
                jmReeprmirCompra.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioVendasPorClienteData.getText() ) )
            {
                jmRelatorioVendasPorClienteData.setEnabled( true );
            }
            else if ( permissao.equals( jmVendaDetalhadasClientes.getText() ) )
            {
                jmVendaDetalhadasClientes.setEnabled( true );
            }
            else if ( permissao.equals( jmNotasCreditoCompra.getText() ) )
            {
                jmNotasCreditoCompra.setEnabled( true );
            } //SISTEMA
            else if ( permissao.equals( "Cadastrar Usuario" ) )
            {
                jmCadastroUsuario.setEnabled( true );
            }
            else if ( permissao.equals( jmAcertoStock.getText() ) )
            {
                jmAcertoStock.setEnabled( true );
            }

            else if ( permissao.equals( jmTransferenciaArmazem.getText() ) )
            {
                jmTransferenciaArmazem.setEnabled( true );
            }

//            else if ( permissao.equals ( "Anulamento de Factura" ) )
            //            {
            //                jmAnulamentoFactura.setEnabled ( true );
            //            }
            //            else if ( permissao.equals ( "Reciclagem" ) )
            //            {
            //                jmReciclagem.setEnabled ( true );
            //            }
//            else if ( permissao.equals( "Anulamento de Entrada de Produtos" ) )
//            {
//                jmAnulamentoEntradaProdutos.setEnabled( true );
//            }
            else if ( permissao.equals( jmCadastroArmazem.getText() ) )
            {
                jmCadastroArmazem.setEnabled( true );
            }
            else if ( permissao.equals( "Produto" ) )
            {
                jmProduto.setEnabled( true );
            }
//            else if ( permissao.equals( "Fornecedor" ) )
//            {
//                jmFornecedor.setEnabled( true );
//            }
            else if ( permissao.equals( "Reemprimir Factura" ) )
            {
                jmReeprmirFacura.setEnabled( true );
            } //                else if(permissao.equals("Pagamento Credito"))
            //                        jmPagamentoCredito.setEnabled(true);
            //            else if ( permissao.equals ( "Entrada dos Produto" ) )
            //            {
            //                jmEntradaProdutos.setEnabled ( true );
            //            }
            else if ( permissao.equals( "Permissao" ) )
            {
                jmPermissao.setEnabled( true );
            }
            else if ( permissao.equals( "Percentagem de Desconto" ) )
            {
                jPercentagemDesconto.setEnabled( true );
            }
            else if ( permissao.equals( "Dados da Empresa" ) )
            {
                jDadosEmpresa.setEnabled( true );
            }
            else if ( permissao.equals( "Cadastro Cliente F" ) )
            {
                jmCadastroCliente.setEnabled( true );
            }
//            else if ( permissao.equals( "Vasilhame" ) )
//            {
//                jmVasilhame.setEnabled( true );
//            }
            else if ( permissao.equals( jmCadastroCliente.getText() ) )
            {
                jmCadastroCliente.setEnabled( true );
            }
            else if ( permissao.equals( jmImprimirPrecos.getText() ) )
            {
                jmImprimirPrecos.setEnabled( true );
            }
//            else if ( permissao.equals( jmPrecosPorPercentagem.getText() ) )
//            {
//                jmPrecosPorPercentagem.setEnabled( true );
//            }
            else if ( permissao.equals( jmConverterProforma.getText() ) )
            {
                jmConverterProforma.setEnabled( true );
            }
            else if ( permissao.equals( jmProcessarRecibo.getText() ) )
            {
                jmProcessarRecibo.setEnabled( true );
            }
            else if ( permissao.equals( jmNotas.getText() ) )
            {
                jmNotas.setEnabled( true );
            }
            else if ( permissao.equals( jmGerarSaft.getText() ) )
            {
                jmGerarSaft.setEnabled( true );
            }
            else if ( permissao.equals( jmEstornos.getText() ) )
            {
                jmEstornos.setEnabled( true );
            }
            else if ( permissao.equals( jmTurno.getText() ) )
            {
                jmTurno.setEnabled( true );
            } //LOGISTICA
            else if ( permissao.equals( jmSolicitacaoCompras.getText() ) )
            {
                jmSolicitacaoCompras.setEnabled( true );
            }
            else if ( permissao.equals( jmAutorizacaoCompras.getText() ) )
            {
                jmAutorizacaoCompras.setEnabled( true );
            }
            else if ( permissao.equals( jmEncomendas.getText() ) )
            {
                jmEncomendas.setEnabled( true );
            }
            else if ( permissao.equals( jmCompras.getText() ) )
            {
                jmCompras.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatorioQTDComprados.getText() ) )
            {
                jmRelatorioQTDComprados.setEnabled( true );
            } //TABELAS
            else if ( permissao.equals( jmFamilia.getText() ) )
            {
                jmFamilia.setEnabled( true );
            }
            else if ( permissao.equals( jmSubFamilia.getText() ) )
            {
                jmSubFamilia.setEnabled( true );
            }
            else if ( permissao.equals( jmMarca.getText() ) )
            {
                jmMarca.setEnabled( true );
            }
            else if ( permissao.equals( jmModelo.getText() ) )
            {
                jmModelo.setEnabled( true );
            }
            else if ( permissao.equals( jmGrupo.getText() ) )
            {
                jmGrupo.setEnabled( true );
            }
            else if ( permissao.equals( jmUnidades.getText() ) )
            {
                jmUnidades.setEnabled( true );
            }

            else if ( permissao.equals( jmReimprimirSaidasProdutos.getText() ) )
            {
                jmReimprimirSaidasProdutos.setEnabled( true );
            }
            else if ( permissao.equals( jmRelatoriosSaidasProdutosPorDatas.getText() ) )
            {
                jmRelatoriosSaidasProdutosPorDatas.setEnabled( true );
            }
            else if ( permissao.equals( jmMapaExistencia.getText() ) )
            {
                jmMapaExistencia.setEnabled( true );
            }
            else if ( permissao.equals( jmAnulamentoSaidasProdutos.getText() ) )
            {
                jmAnulamentoSaidasProdutos.setEnabled( true );
            }
            else if ( permissao.equals( jmGavetasPrateleiras.getText() ) )
            {
                jmGavetasPrateleiras.setEnabled( true );
            }
            else if ( permissao.equals( jmFrontOffice.getText() ) )
            {
                jmFrontOffice.setEnabled( true );
            }
            else if ( permissao.equals( jmSaidasProdutos.getText() ) )
            {
                jmSaidasProdutos.setEnabled( true );
            }
            else if ( permissao.equals( jmNotaLevantamento.getText() ) )
            {
                jmNotaLevantamento.setEnabled( true );
            }
            else if ( permissao.equals( jmReeprmirNota.getText() ) )
            {
                jmReeprmirNota.setEnabled( true );
            }
            else if ( permissao.equals( jmListaClientes.getText() ) )
            {
                jmListaClientes.setEnabled( true );
            }
            else if ( permissao.equals( jMenuItemRelatorioQtdDetalhado.getText() ) )
            {
                jMenuItemRelatorioQtdDetalhado.setEnabled( true );
            }
            else if ( permissao.equals( jMenuConfiguracoesSistema.getText() ) )
            {
                jMenuConfiguracoesSistema.setEnabled( true );
            }
            else if ( permissao.equals( jMenuRelatorioTransferencia.getText() ) )
            {
                jMenuRelatorioTransferencia.setEnabled( true );
            }
            else if ( permissao.equals( jMenuItemRelatorioMensal.getText() ) )
            {
                jMenuItemRelatorioMensal.setEnabled( true );
            }

        }

    }

    public void setPermissoes( boolean status )
    {

        //FICHEIROS
        jmVenda.setEnabled( status );
//        jmGestaoCredito.setEnabled ( status );

        //LISTAGENS
        jmListarUsuario.setEnabled( status );
        jmRelatorioPorFonecedor.setEnabled( status );
        jmAcertoStock.setEnabled( status );
        jmListarProdutosStock.setEnabled( status );
//        jmListagemVasilhames.setEnabled( status );
        jmTodasVendas.setEnabled( status );
        jmReactivarProdutos.setEnabled( status );
//        jmRelatorioVendasPorUsuarioData.setEnabled( status );
//        jmVendaPorUsuario.setEnabled ( status );
//        jmVendaCredito.setEnabled ( status );
//        jmRelatorioPreForma.setEnabled ( status );
//        jmRegistroEntradas.setEnabled ( status );
        jmRelatorioDiario.setEnabled( status );
        jmListagensTodosProdutos.setEnabled( status );
        jmListagensTodosProdutoComDesconto.setEnabled( status );
        jmProdutosActualizar.setEnabled( status );
        jmVenda.setEnabled( status );
//        jmEstornos.setEnabled(status);

        //SISTEMA      
        jmTurno.setEnabled( status );
        jmCadastroUsuario.setEnabled( status );
        jmListagensTodosProdutos.setEnabled( status );
//        jmAnulamentoFactura.setEnabled ( status );
//        jmReciclagem.setEnabled ( status );
//        jmAnulamentoEntradaProdutos.setEnabled( status );
        //jmStock.setEnabled(status);
        jmProduto.setEnabled( status );
        jmCadastroArmazem.setEnabled( status );
//        jmFornecedor.setEnabled( status );
        jmReeprmirFacura.setEnabled( status );
        //jmPagamentoCredito.setEnabled(status);
//        jmEntradaProdutos.setEnabled ( status );
        jmPermissao.setEnabled( status );
        jPercentagemDesconto.setEnabled( status );
        jDadosEmpresa.setEnabled( status );
        jmCadastroCliente.setEnabled( status );
//        jmVasilhame.setEnabled( status );
        jmImprimirPrecos.setEnabled( status );
//        jmPrecosPorPercentagem.setEnabled( status );
//        jmRelatorioPreForma.setEnabled ( status );
        jmVendaDetalhadasUsuarios.setEnabled( status );
//        jmPromocao.setEnabled ( status );
        jmConverterProforma.setEnabled( status );
        jmProcessarRecibo.setEnabled( status );
        jmNotas.setEnabled( status );
        jmGerarSaft.setEnabled( status );
        jmSolicitacaoCompras.setEnabled( status );
        jmAutorizacaoCompras.setEnabled( status );
        jmEncomendas.setEnabled( status );
        jmCompras.setEnabled( status );
        jmRelatorioQTDComprados.setEnabled( status );

        //TABELAS
        jmFamilia.setEnabled( status );
        jmSubFamilia.setEnabled( status );
        jmMarca.setEnabled( status );
        jmModelo.setEnabled( status );
        jmGrupo.setEnabled( status );

        //OUTROS
        jmUnidades.setEnabled( status );
        jmEstornos.setEnabled( status );
        jmSaidasProdutos.setEnabled( status );
//        jmGestaoMesas.setEnabled( status );
        jmReimprimirSaidasProdutos.setEnabled( status );
        jmRelatoriosSaidasProdutosPorDatas.setEnabled( status );
        jmMapaExistencia.setEnabled( status );
        jmAnulamentoSaidasProdutos.setEnabled( status );
        jmGavetasPrateleiras.setEnabled( status );
        jmRelatorioFormaPagamento.setEnabled( status );
        jmRelatorioTodosServicos.setEnabled( status );
        jmRelatorioNotasCredito.setEnabled( status );
        jmRelatorioVendasPorUsuarioData.setEnabled( status );
        jmRelatorioAcertoStock.setEnabled( status );
        jmHistoricoBonusEmpresa.setEnabled( status );
        jmRelatorioComprasPorFornecedor.setEnabled( status );
        jmReeprmirCompra.setEnabled( status );
        jmRelatorioVendasPorClienteData.setEnabled( status );
        jmVendaDetalhadasClientes.setEnabled( status );
        jmRelatorioDiarioTodasVendasTempoReal.setEnabled( status );
        jmNotasCreditoCompra.setEnabled( status );
        jmTransferenciaArmazem.setEnabled( status );
        jmFrontOffice.setEnabled( status );
        jmNotaLevantamento.setEnabled( status );
        jmReeprmirNota.setEnabled( status );
        jmListaClientes.setEnabled( status );
        jMenuConfiguracoesSistema.setEnabled( status );
        jMenuItemRelatorioQtdDetalhado.setEnabled( status );
        jMenuRelatorioTransferencia.setEnabled( status );
        jMenuItemRelatorioMensal.setEnabled( status );

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

        jLabel2 = new javax.swing.JLabel();
        btnProdutosEspirados = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lb_nome_usuario = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmVenda = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItemAlteracaoGuia = new javax.swing.JMenuItem();
        jMenuItemConverterGuia = new javax.swing.JMenuItem();
        jmNotaLevantamento = new javax.swing.JMenuItem();
        jmFrontOffice = new javax.swing.JMenuItem();
        jmConverterProforma = new javax.swing.JMenuItem();
        jmProcessarRecibo = new javax.swing.JMenuItem();
        jmNotas = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItemRectificacao = new javax.swing.JMenuItem();
        jMenuItemGestaoCreditos = new javax.swing.JMenuItem();
        jmEstornos = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jmCompras = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jmCadastroCliente = new javax.swing.JMenuItem();
        jPercentagemDesconto = new javax.swing.JMenuItem();
        jmListaClientes = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmTodasVendas = new javax.swing.JMenuItem();
        jmRelatorioVendasPorUsuarioData = new javax.swing.JMenuItem();
        jmRelatorioVendasPorClienteData = new javax.swing.JMenuItem();
        jmRelatorioDiario = new javax.swing.JMenuItem();
        jMenuItemRelatorioQtdDetalhado = new javax.swing.JMenuItem();
        jmRelatorioFormaPagamento = new javax.swing.JMenuItem();
        jmVendaDetalhadasClientes = new javax.swing.JMenuItem();
        jmVendaDetalhadasUsuarios = new javax.swing.JMenuItem();
        jmRelatorioDiarioTodasVendasTempoReal = new javax.swing.JMenuItem();
        jmListagensTodosProdutos = new javax.swing.JMenuItem();
        jmRelatorioTodosServicos = new javax.swing.JMenuItem();
        jmListagensTodosProdutoComDesconto = new javax.swing.JMenuItem();
        jmProdutosActualizar = new javax.swing.JMenuItem();
        jmListarUsuario = new javax.swing.JMenuItem();
        jmRelatorioPorFonecedor = new javax.swing.JMenuItem();
        jmReeprmirFacura = new javax.swing.JMenuItem();
        jmReeprmirNota = new javax.swing.JMenuItem();
        jmReimprimirSaidasProdutos = new javax.swing.JMenuItem();
        jmRelatoriosSaidasProdutosPorDatas = new javax.swing.JMenuItem();
        jmRelatorioNotasCredito = new javax.swing.JMenuItem();
        jmMapaExistencia = new javax.swing.JMenuItem();
        jmRelatorioAcertoStock = new javax.swing.JMenuItem();
        jmHistoricoBonusEmpresa = new javax.swing.JMenuItem();
        jmRelatorioQTDComprados = new javax.swing.JMenuItem();
        jMenuRelatorioTransferencia = new javax.swing.JMenuItem();
        jMenuItemRelatorioQuebras = new javax.swing.JMenuItem();
        jMenuItemRelatorioMensal = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jmListarProdutosStock = new javax.swing.JMenuItem();
        jmTransferenciaArmazem = new javax.swing.JMenuItem();
        jmCadastroArmazem = new javax.swing.JMenuItem();
        jmSaidasProdutos = new javax.swing.JMenuItem();
        jmAnulamentoSaidasProdutos = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jmSolicitacaoCompras = new javax.swing.JMenuItem();
        jmAutorizacaoCompras = new javax.swing.JMenuItem();
        jmEncomendas = new javax.swing.JMenuItem();
        jmReeprmirCompra = new javax.swing.JMenuItem();
        jmNotasCreditoCompra = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jmRelatorioComprasPorFornecedor = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jDadosEmpresa = new javax.swing.JMenuItem();
        jmUnidades = new javax.swing.JMenuItem();
        jmGavetasPrateleiras = new javax.swing.JMenuItem();
        jmAcertoStock = new javax.swing.JMenuItem();
        jmReactivarProdutos = new javax.swing.JMenuItem();
        jmCadastroUsuario = new javax.swing.JMenuItem();
        jmPermissao = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuConfiguracoesSistema = new javax.swing.JMenuItem();
        jmTurno = new javax.swing.JMenuItem();
        jmProduto = new javax.swing.JMenuItem();
        jmImprimirPrecos = new javax.swing.JMenuItem();
        jmGerarSaft = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jmFamilia = new javax.swing.JMenuItem();
        jmSubFamilia = new javax.swing.JMenuItem();
        jmMarca = new javax.swing.JMenuItem();
        jmModelo = new javax.swing.JMenuItem();
        jmGrupo = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("....::: KITANDA -  MENU PRINCIPAL :::....");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("KITANDA v1.1");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 180, -1));

        btnProdutosEspirados.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        btnProdutosEspirados.setForeground(new java.awt.Color(255, 0, 0));
        btnProdutosEspirados.setText("...");
        btnProdutosEspirados.setToolTipText("Produtos Expirados");
        btnProdutosEspirados.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnProdutosEspiradosActionPerformed(evt);
            }
        });
        getContentPane().add(btnProdutosEspirados, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 50, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/img70.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 900));

        lb_nome_usuario.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        lb_nome_usuario.setText("seja bem vindo! nome_completo");
        getContentPane().add(lb_nome_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1415, 907, 306, 32));

        jMenuBar1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 102, 0)));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 12)); // NOI18N

        jMenu1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/3863_32x32.png"))); // NOI18N
        jMenu1.setText("Vendas");

        jmVenda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jmVenda.setText("Venda");
        jmVenda.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmVendaActionPerformed(evt);
            }
        });
        jMenu1.add(jmVenda);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jMenuItem12.setText("Guia Transporte");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItemAlteracaoGuia.setText("Alteração da Guia Transporte");
        jMenuItemAlteracaoGuia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemAlteracaoGuiaActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemAlteracaoGuia);

        jMenuItemConverterGuia.setText("Converter Guia Transporte Em Documento");
        jMenuItemConverterGuia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemConverterGuiaActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemConverterGuia);

        jmNotaLevantamento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmNotaLevantamento.setText("Nota de Levantamento");
        jmNotaLevantamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmNotaLevantamentoActionPerformed(evt);
            }
        });
        jMenu1.add(jmNotaLevantamento);

        jmFrontOffice.setText("Front Office");
        jmFrontOffice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmFrontOfficeActionPerformed(evt);
            }
        });
        jMenu1.add(jmFrontOffice);

        jmConverterProforma.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmConverterProforma.setText("Converter Proformas em Facturas");
        jmConverterProforma.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmConverterProformaActionPerformed(evt);
            }
        });
        jMenu1.add(jmConverterProforma);

        jmProcessarRecibo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmProcessarRecibo.setText("Processar Recibos de Factura");
        jmProcessarRecibo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmProcessarReciboActionPerformed(evt);
            }
        });
        jMenu1.add(jmProcessarRecibo);

        jmNotas.setText("Notas de Credito e Debito");

        jMenuItem7.setText("Emitir nota de credito para anulação de facturas");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jmNotas.add(jMenuItem7);

        jMenuItemRectificacao.setText("Emitir nota de credito para rectificação de facturas");
        jMenuItemRectificacao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemRectificacaoActionPerformed(evt);
            }
        });
        jmNotas.add(jMenuItemRectificacao);

        jMenu1.add(jmNotas);

        jMenuItemGestaoCreditos.setText("Gestao de Creditos");
        jMenuItemGestaoCreditos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemGestaoCreditosActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemGestaoCreditos);

        jmEstornos.setText("Quebras");
        jmEstornos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmEstornosActionPerformed(evt);
            }
        });
        jMenu1.add(jmEstornos);
        jMenu1.add(jSeparator9);

        jMenuItem3.setText("Log Out");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LOGOUT - VERMELHO/Logout 16x16.png"))); // NOI18N
        jMenuItem1.setText("Sair");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuario.png"))); // NOI18N
        jMenu9.setText("Fornecedores");

        jmCompras.setText("Compras");
        jmCompras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmComprasActionPerformed(evt);
            }
        });
        jMenu9.add(jmCompras);

        jMenuBar1.add(jMenu9);

        jMenu8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuario.png"))); // NOI18N
        jMenu8.setText("Clientes");

        jmCadastroCliente.setText("Cadastro de Cliente");
        jmCadastroCliente.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmCadastroClienteActionPerformed(evt);
            }
        });
        jMenu8.add(jmCadastroCliente);

        jPercentagemDesconto.setText("Percentagem de Desconto");
        jPercentagemDesconto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jPercentagemDescontoActionPerformed(evt);
            }
        });
        jMenu8.add(jPercentagemDesconto);

        jmListaClientes.setText("Lista de Clientes");
        jmListaClientes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmListaClientesActionPerformed(evt);
            }
        });
        jMenu8.add(jmListaClientes);

        jMenuBar1.add(jMenu8);

        jMenu2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323287592_stock_task.png"))); // NOI18N
        jMenu2.setText("Relatorio/Mapa");

        jmTodasVendas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmTodasVendas.setText("Relatorio de Vendas por Intervalo de Datas");
        jmTodasVendas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmTodasVendasActionPerformed(evt);
            }
        });
        jMenu2.add(jmTodasVendas);

        jmRelatorioVendasPorUsuarioData.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmRelatorioVendasPorUsuarioData.setText("Relatorio de Vendas Por Usuario e Data");
        jmRelatorioVendasPorUsuarioData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioVendasPorUsuarioDataActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioVendasPorUsuarioData);

        jmRelatorioVendasPorClienteData.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmRelatorioVendasPorClienteData.setText("Relatorio de Vendas por Cliente e Data");
        jmRelatorioVendasPorClienteData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioVendasPorClienteDataActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioVendasPorClienteData);

        jmRelatorioDiario.setText("Relatorio de Quatidades de Produtos Vendidos");
        jmRelatorioDiario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioDiarioActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioDiario);

        jMenuItemRelatorioQtdDetalhado.setText("Relatorio Detalhado de Quantidades Vendidos");
        jMenuItemRelatorioQtdDetalhado.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemRelatorioQtdDetalhadoActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemRelatorioQtdDetalhado);

        jmRelatorioFormaPagamento.setText("Relatorio de Vendas por Formas de Pagamentos");
        jmRelatorioFormaPagamento.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioFormaPagamentoActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioFormaPagamento);

        jmVendaDetalhadasClientes.setText("Vendas Detalhadas por Clientes");
        jmVendaDetalhadasClientes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmVendaDetalhadasClientesActionPerformed(evt);
            }
        });
        jMenu2.add(jmVendaDetalhadasClientes);

        jmVendaDetalhadasUsuarios.setText("Vendas Detalhadas por Usuarios");
        jmVendaDetalhadasUsuarios.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmVendaDetalhadasUsuariosActionPerformed(evt);
            }
        });
        jMenu2.add(jmVendaDetalhadasUsuarios);

        jmRelatorioDiarioTodasVendasTempoReal.setText("Relatorio Diario de Todas Vendas em Tempo Real");
        jmRelatorioDiarioTodasVendasTempoReal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioDiarioTodasVendasTempoRealActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioDiarioTodasVendasTempoReal);

        jmListagensTodosProdutos.setText("Relatorio de Todos Produtos");
        jmListagensTodosProdutos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmListagensTodosProdutosActionPerformed(evt);
            }
        });
        jMenu2.add(jmListagensTodosProdutos);

        jmRelatorioTodosServicos.setText("Relatorio de Todos Servicos");
        jmRelatorioTodosServicos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioTodosServicosActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioTodosServicos);

        jmListagensTodosProdutoComDesconto.setText("Listagens de Todos os Produtos com Desconto");
        jmListagensTodosProdutoComDesconto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmListagensTodosProdutoComDescontoActionPerformed(evt);
            }
        });
        jMenu2.add(jmListagensTodosProdutoComDesconto);

        jmProdutosActualizar.setText("Compra Por Fazer");
        jmProdutosActualizar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmProdutosActualizarActionPerformed(evt);
            }
        });
        jMenu2.add(jmProdutosActualizar);

        jmListarUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmListarUsuario.setText("Listar Usuarios");
        jmListarUsuario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmListarUsuarioActionPerformed(evt);
            }
        });
        jMenu2.add(jmListarUsuario);

        jmRelatorioPorFonecedor.setText("Relatório por Fornecedor");
        jmRelatorioPorFonecedor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioPorFonecedorActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioPorFonecedor);

        jmReeprmirFacura.setText("Reimprimir Factura");
        jmReeprmirFacura.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmReeprmirFacuraActionPerformed(evt);
            }
        });
        jMenu2.add(jmReeprmirFacura);

        jmReeprmirNota.setText("Reimprimir Nota Credito");
        jmReeprmirNota.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmReeprmirNotaActionPerformed(evt);
            }
        });
        jMenu2.add(jmReeprmirNota);

        jmReimprimirSaidasProdutos.setText("Reimprimir Saidas de Produtos");
        jmReimprimirSaidasProdutos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmReimprimirSaidasProdutosActionPerformed(evt);
            }
        });
        jMenu2.add(jmReimprimirSaidasProdutos);

        jmRelatoriosSaidasProdutosPorDatas.setText("Relatorios Saidas de Produtos por Datas");
        jmRelatoriosSaidasProdutosPorDatas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatoriosSaidasProdutosPorDatasActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatoriosSaidasProdutosPorDatas);

        jmRelatorioNotasCredito.setText("Relatorio de Notas de Creditos");
        jmRelatorioNotasCredito.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioNotasCreditoActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioNotasCredito);

        jmMapaExistencia.setText("Mapa de Existencia");
        jmMapaExistencia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmMapaExistenciaActionPerformed(evt);
            }
        });
        jMenu2.add(jmMapaExistencia);

        jmRelatorioAcertoStock.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmRelatorioAcertoStock.setText("Relatorio de Acerto de Stock");
        jmRelatorioAcertoStock.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioAcertoStockActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioAcertoStock);

        jmHistoricoBonusEmpresa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmHistoricoBonusEmpresa.setText("Historico de Bonus da Empresa");
        jmHistoricoBonusEmpresa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmHistoricoBonusEmpresaActionPerformed(evt);
            }
        });
        jMenu2.add(jmHistoricoBonusEmpresa);

        jmRelatorioQTDComprados.setText("Relatorio de Quantidades de Produtos Comprados");
        jmRelatorioQTDComprados.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioQTDCompradosActionPerformed(evt);
            }
        });
        jMenu2.add(jmRelatorioQTDComprados);

        jMenuRelatorioTransferencia.setText("Relatorio Transferencias Armazem");
        jMenuRelatorioTransferencia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuRelatorioTransferenciaActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuRelatorioTransferencia);

        jMenuItemRelatorioQuebras.setText("Relatorio Quebras");
        jMenuItemRelatorioQuebras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemRelatorioQuebrasActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemRelatorioQuebras);

        jMenuItemRelatorioMensal.setText("Relatorio Mensal");
        jMenuItemRelatorioMensal.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemRelatorioMensalActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemRelatorioMensal);

        jMenuBar1.add(jMenu2);

        jMenu3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/2215_32x32.png"))); // NOI18N
        jMenu3.setText("Armazém");

        jmListarProdutosStock.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmListarProdutosStock.setText("Inventario");
        jmListarProdutosStock.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmListarProdutosStockActionPerformed(evt);
            }
        });
        jMenu3.add(jmListarProdutosStock);

        jmTransferenciaArmazem.setText("Transferencia de Armazem");
        jmTransferenciaArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmTransferenciaArmazemActionPerformed(evt);
            }
        });
        jMenu3.add(jmTransferenciaArmazem);

        jmCadastroArmazem.setText("Cadastro de Armazem");
        jmCadastroArmazem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmCadastroArmazemActionPerformed(evt);
            }
        });
        jMenu3.add(jmCadastroArmazem);

        jmSaidasProdutos.setText("Saidas Produtos");
        jmSaidasProdutos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmSaidasProdutosActionPerformed(evt);
            }
        });
        jMenu3.add(jmSaidasProdutos);

        jmAnulamentoSaidasProdutos.setText("Anulamentos Saidas Produtos");
        jmAnulamentoSaidasProdutos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmAnulamentoSaidasProdutosActionPerformed(evt);
            }
        });
        jMenu3.add(jmAnulamentoSaidasProdutos);

        jMenuBar1.add(jMenu3);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1323444801_currency_dollar red.png"))); // NOI18N
        jMenu5.setText("Compras");

        jmSolicitacaoCompras.setText("Solicitacao Compras");
        jmSolicitacaoCompras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmSolicitacaoComprasActionPerformed(evt);
            }
        });
        jMenu5.add(jmSolicitacaoCompras);

        jmAutorizacaoCompras.setText("Autorizacao Compras");
        jmAutorizacaoCompras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmAutorizacaoComprasActionPerformed(evt);
            }
        });
        jMenu5.add(jmAutorizacaoCompras);

        jmEncomendas.setText("Encomendas");
        jmEncomendas.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmEncomendasActionPerformed(evt);
            }
        });
        jMenu5.add(jmEncomendas);

        jmReeprmirCompra.setText("Reimprimir Compra");
        jmReeprmirCompra.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmReeprmirCompraActionPerformed(evt);
            }
        });
        jMenu5.add(jmReeprmirCompra);

        jmNotasCreditoCompra.setText("Notas Credito Compra");

        jMenuItem13.setText("Emitir nota de credito para anulação de compras");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jmNotasCreditoCompra.add(jMenuItem13);

        jMenuItem6.setText("Relatorio de Notas de Credito de Compras");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jmNotasCreditoCompra.add(jMenuItem6);

        jMenu5.add(jmNotasCreditoCompra);

        jmRelatorioComprasPorFornecedor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        jmRelatorioComprasPorFornecedor.setText("Relatorio de Compras por Fornecedores");
        jmRelatorioComprasPorFornecedor.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmRelatorioComprasPorFornecedorActionPerformed(evt);
            }
        });
        jMenu5.add(jmRelatorioComprasPorFornecedor);

        jMenuBar1.add(jMenu5);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens32x32/operation32x32.png"))); // NOI18N
        jMenu6.setText("Configurações");

        jDadosEmpresa.setText("Dados da Empresa");
        jDadosEmpresa.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jDadosEmpresaActionPerformed(evt);
            }
        });
        jMenu6.add(jDadosEmpresa);

        jmUnidades.setText("Unidades");
        jmUnidades.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmUnidadesActionPerformed(evt);
            }
        });
        jMenu6.add(jmUnidades);

        jmGavetasPrateleiras.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jmGavetasPrateleiras.setText("Gavetas/Prateleiras");
        jmGavetasPrateleiras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmGavetasPrateleirasActionPerformed(evt);
            }
        });
        jMenu6.add(jmGavetasPrateleiras);

        jmAcertoStock.setText("Acerto Stock");
        jmAcertoStock.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmAcertoStockActionPerformed(evt);
            }
        });
        jMenu6.add(jmAcertoStock);

        jmReactivarProdutos.setText("Reactivar Produtos");
        jmReactivarProdutos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmReactivarProdutosActionPerformed(evt);
            }
        });
        jMenu6.add(jmReactivarProdutos);

        jmCadastroUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jmCadastroUsuario.setText("Cadastrar Usuario");
        jmCadastroUsuario.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmCadastroUsuarioActionPerformed(evt);
            }
        });
        jMenu6.add(jmCadastroUsuario);

        jmPermissao.setText("Permissões");
        jmPermissao.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmPermissaoActionPerformed(evt);
            }
        });
        jMenu6.add(jmPermissao);

        jMenuItem8.setText("Alteração da Senha");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem8);

        jMenuConfiguracoesSistema.setText("Configuração Sistema");
        jMenuConfiguracoesSistema.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuConfiguracoesSistemaActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuConfiguracoesSistema);

        jmTurno.setText("Gestao de Turno");
        jmTurno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmTurnoActionPerformed(evt);
            }
        });
        jMenu6.add(jmTurno);

        jmProduto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jmProduto.setText("Produto");
        jmProduto.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmProdutoActionPerformed(evt);
            }
        });
        jMenu6.add(jmProduto);

        jmImprimirPrecos.setText("Imprimir Precos");
        jmImprimirPrecos.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmImprimirPrecosActionPerformed(evt);
            }
        });
        jMenu6.add(jmImprimirPrecos);

        jmGerarSaft.setText("Gerar SAFT");
        jmGerarSaft.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmGerarSaftActionPerformed(evt);
            }
        });
        jMenu6.add(jmGerarSaft);

        jMenuBar1.add(jMenu6);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/1588_32x32.png"))); // NOI18N
        jMenu7.setText("Tabelas");

        jmFamilia.setText("Familia");
        jmFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmFamiliaActionPerformed(evt);
            }
        });
        jMenu7.add(jmFamilia);

        jmSubFamilia.setText("SubFamilia");
        jmSubFamilia.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmSubFamiliaActionPerformed(evt);
            }
        });
        jMenu7.add(jmSubFamilia);

        jmMarca.setText("Marca");
        jmMarca.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmMarcaActionPerformed(evt);
            }
        });
        jMenu7.add(jmMarca);

        jmModelo.setText("Modelo");
        jmModelo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmModeloActionPerformed(evt);
            }
        });
        jMenu7.add(jmModelo);

        jmGrupo.setText("Grupo");
        jmGrupo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jmGrupoActionPerformed(evt);
            }
        });
        jMenu7.add(jmGrupo);

        jMenuBar1.add(jMenu7);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens32x32/actualizar_32x32.png"))); // NOI18N
        jMenu4.setText("Ajuda");

        jMenuItem9.setText("Sobre o Autor");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmVendaActionPerformed
        try
        {
            // TODO add your handling code here

            new VendaUsuarioVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
            //VendaUsuarioVisao.getObj(cod_utilizador).setVisible(true);
            // MetodosUtil.getObj(cod_utilizador, (VendaUsuarioVisao)).setVisible(true);

        }
        catch ( Exception ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }//GEN-LAST:event_jmVendaActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit( 0 );
        fazerBackupAgora();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jDadosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDadosEmpresaActionPerformed
        // TODO add your handling code here:
        new DadosInstituicaoVisao( this, rootPaneCheckingEnabled ).setVisible(true);
    }//GEN-LAST:event_jDadosEmpresaActionPerformed

    private void jmPermissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmPermissaoActionPerformed
        // TODO add your handling code here:

        new PermissaoVisao( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmPermissaoActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:

        MetodosUtil.logo_out( this.cod_utilizador, this.idEmpresa );
//        fazerBackupAgora();

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        new InformacoesEmpresaVisao().setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jmAcertoStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAcertoStockActionPerformed
        // TODO add your handling code here:
        try
        {

            new AcertoStockVisao(this.cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmAcertoStockActionPerformed

    private void jmConverterProformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmConverterProformaActionPerformed

        try
        {
            // TODO add your handling code here:
            new ConverterProformaFacturaVisao(this.cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }


    }//GEN-LAST:event_jmConverterProformaActionPerformed

    private void jmGerarSaftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmGerarSaftActionPerformed
        // TODO add your handling code here:
        new FicheiroSAFTVisao( conexao ).setVisible( true );
    }//GEN-LAST:event_jmGerarSaftActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem7ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem7ActionPerformed
        iniciarProcessoAnulacao();

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jmProcessarReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmProcessarReciboActionPerformed

//        new ReciboVisao( cod_utilizador, this.conexao ).setVisible( true );
        new EmissaoRecibos( cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmProcessarReciboActionPerformed

    private void jmUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmUnidadesActionPerformed
        new UnidadeMedidasVisao( this, rootPaneCheckingEnabled ).setVisible(true);
    }//GEN-LAST:event_jmUnidadesActionPerformed

    private void jmGavetasPrateleirasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmGavetasPrateleirasActionPerformed
        new LocalVisao( this, rootPaneCheckingEnabled ).setVisible(true);
    }//GEN-LAST:event_jmGavetasPrateleirasActionPerformed

    private void jmSolicitacaoComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmSolicitacaoComprasActionPerformed
        try
        {

            new SolicitacaoComprasVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmSolicitacaoComprasActionPerformed

    private void jmAutorizacaoComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAutorizacaoComprasActionPerformed
        try
        {

            new AprovacaoCompraVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmAutorizacaoComprasActionPerformed

    private void jmEncomendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmEncomendasActionPerformed
        try
        {

            new EncomendaVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmEncomendasActionPerformed

    private void jmComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmComprasActionPerformed
        try
        {

            new FornecedorOutroVisao( this, cod_utilizador, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmComprasActionPerformed

    private void jmFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmFamiliaActionPerformed
    {//GEN-HEADEREND:event_jmFamiliaActionPerformed
        // TODO add your handling code here:
        new FamiliaVisao().setVisible(true);
    }//GEN-LAST:event_jmFamiliaActionPerformed

    private void jmSubFamiliaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmSubFamiliaActionPerformed
    {//GEN-HEADEREND:event_jmSubFamiliaActionPerformed
        // TODO add your handling code here:
        new CategoriasVisao(this.conexao).setVisible(true);
    }//GEN-LAST:event_jmSubFamiliaActionPerformed

    private void jmMarcaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmMarcaActionPerformed
    {//GEN-HEADEREND:event_jmMarcaActionPerformed
        // TODO add your handling code here:
        new MarcaVisao().setVisible(true);
    }//GEN-LAST:event_jmMarcaActionPerformed

    private void jmModeloActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmModeloActionPerformed
    {//GEN-HEADEREND:event_jmModeloActionPerformed
        // TODO add your handling code here:
        new ModeloVisao().setVisible(true);
    }//GEN-LAST:event_jmModeloActionPerformed

    private void jmGrupoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmGrupoActionPerformed
    {//GEN-HEADEREND:event_jmGrupoActionPerformed
        // TODO add your handling code here:
        new GrupoVisao().setVisible(true);
    }//GEN-LAST:event_jmGrupoActionPerformed

    private void jmImprimirPrecosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmImprimirPrecosActionPerformed
    {//GEN-HEADEREND:event_jmImprimirPrecosActionPerformed
        // TODO add your handling code here:
        new ImprimirPrecoVisao( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmImprimirPrecosActionPerformed

    private void jPercentagemDescontoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jPercentagemDescontoActionPerformed
    {//GEN-HEADEREND:event_jPercentagemDescontoActionPerformed

        try
        {
            new PercentagemDesconto( this, rootPaneCheckingEnabled, cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jPercentagemDescontoActionPerformed

    private void jmProdutoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmProdutoActionPerformed
    {//GEN-HEADEREND:event_jmProdutoActionPerformed
        try
        {
            // TODO add your handling code here:

            new ProdutosVisao(this, rootPaneCheckingEnabled, cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( Exception ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jmProdutoActionPerformed

    private void jmCadastroClienteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmCadastroClienteActionPerformed
    {//GEN-HEADEREND:event_jmCadastroClienteActionPerformed
        new ClienteVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmCadastroClienteActionPerformed

    private void jmCadastroArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmCadastroArmazemActionPerformed
    {//GEN-HEADEREND:event_jmCadastroArmazemActionPerformed
        // TODO add your handling code here:
        new ArmazemVisao(this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmCadastroArmazemActionPerformed

    private void jmCadastroUsuarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmCadastroUsuarioActionPerformed
    {//GEN-HEADEREND:event_jmCadastroUsuarioActionPerformed
        // TODO add your handling code here:
        new UsuarioVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmCadastroUsuarioActionPerformed

    private void jmTurnoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmTurnoActionPerformed
    {//GEN-HEADEREND:event_jmTurnoActionPerformed
        // TODO add your handling code here:
        new TurnoVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmTurnoActionPerformed

    private void jmReeprmirFacuraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmReeprmirFacuraActionPerformed
    {//GEN-HEADEREND:event_jmReeprmirFacuraActionPerformed
        // TODO add your handling code here:

        new ReemprimirDocumentoVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);


    }//GEN-LAST:event_jmReeprmirFacuraActionPerformed

    private void jmRelatorioPorFonecedorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioPorFonecedorActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioPorFonecedorActionPerformed
        // TODO add your handling code here:
        new FornecedorRelatorioDiario(this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmRelatorioPorFonecedorActionPerformed

    private void jmVendaDetalhadasUsuariosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmVendaDetalhadasUsuariosActionPerformed
    {//GEN-HEADEREND:event_jmVendaDetalhadasUsuariosActionPerformed
        new ListarVendasDetalhadas( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmVendaDetalhadasUsuariosActionPerformed

    private void jmListarUsuarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmListarUsuarioActionPerformed
    {//GEN-HEADEREND:event_jmListarUsuarioActionPerformed
        // TODO add your handling code here:

        new ListaUsuarioVisao().setVisible(true);
    }//GEN-LAST:event_jmListarUsuarioActionPerformed

    private void jmProdutosActualizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmProdutosActualizarActionPerformed
    {//GEN-HEADEREND:event_jmProdutosActualizarActionPerformed

        try
        {
            // TODO add your handling code here
            new ListarProdutosAcomprar(cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }//GEN-LAST:event_jmProdutosActualizarActionPerformed

    private void jmListagensTodosProdutosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmListagensTodosProdutosActionPerformed
    {//GEN-HEADEREND:event_jmListagensTodosProdutosActionPerformed
        // TODO add your handling code here:

        new ListarProdutosVisao( cod_utilizador ).setVisible(true);
    }//GEN-LAST:event_jmListagensTodosProdutosActionPerformed

    private void jmRelatorioDiarioTodasVendasTempoRealActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioDiarioTodasVendasTempoRealActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioDiarioTodasVendasTempoRealActionPerformed
        // TODO add your handling code here:
        new RelatorioVendaEmTempoRealVisao( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmRelatorioDiarioTodasVendasTempoRealActionPerformed

    private void jmRelatorioDiarioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioDiarioActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioDiarioActionPerformed

        try
        {
            // TODO add your handling code here:
            new ListarRelatorioDiario( cod_utilizador, this.conexao ).setVisible( administrador );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }//GEN-LAST:event_jmRelatorioDiarioActionPerformed

    private void jmRelatorioVendasPorUsuarioDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioVendasPorUsuarioDataActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioVendasPorUsuarioDataActionPerformed
        // TODO add your handling code here:
        new ListarVendasByUsuarios(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmRelatorioVendasPorUsuarioDataActionPerformed

    private void jmTodasVendasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmTodasVendasActionPerformed
    {//GEN-HEADEREND:event_jmTodasVendasActionPerformed

        try
        {
            new ListarRelatorioVenda( this.conexao, cod_utilizador ).setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jmTodasVendasActionPerformed

    private void jmListarProdutosStockActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmListarProdutosStockActionPerformed
    {//GEN-HEADEREND:event_jmListarProdutosStockActionPerformed

        // TODO add your handling code here:
        try
        {
            new InventarioVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jmListarProdutosStockActionPerformed

    private void jmMapaExistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmMapaExistenciaActionPerformed
        // TODO add your handling code here:
        new MapaExistenciaVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmMapaExistenciaActionPerformed

    private void jmRelatoriosSaidasProdutosPorDatasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmRelatoriosSaidasProdutosPorDatasActionPerformed
        // TODO add your handling code here:
        //        new ListarSaidasByUsuarios().setVisible(true);
        new ListarRelatorioSaidaByIntervalo( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmRelatoriosSaidasProdutosPorDatasActionPerformed

    private void jmAnulamentoSaidasProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAnulamentoSaidasProdutosActionPerformed
        new AnulamentoSaidasProdutosVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmAnulamentoSaidasProdutosActionPerformed

    private void jmReimprimirSaidasProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmReimprimirSaidasProdutosActionPerformed
        new ReimprimirSaidasProdutos(this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmReimprimirSaidasProdutosActionPerformed

    private void jmRelatorioNotasCreditoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioNotasCreditoActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioNotasCreditoActionPerformed
        new ListarRelatorioNotasCredito( this.conexao ).setVisible( true );
    }//GEN-LAST:event_jmRelatorioNotasCreditoActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem8ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem8ActionPerformed
        new UsuarioAlterarSenhaVissao( usuarioDao.findTbUsuario( cod_utilizador ) ).setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jmRelatorioFormaPagamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioFormaPagamentoActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioFormaPagamentoActionPerformed

        try
        {
            new ListarRelatorioFormaPagamento( BDConexao.getInstancia(), this.cod_utilizador ).setVisible(true);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmRelatorioFormaPagamentoActionPerformed

    private void jmRelatorioVendasPorClienteDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioVendasPorClienteDataActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioVendasPorClienteDataActionPerformed
        new ListarVendasByClientes(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmRelatorioVendasPorClienteDataActionPerformed

    private void jmVendaDetalhadasClientesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmVendaDetalhadasClientesActionPerformed
    {//GEN-HEADEREND:event_jmVendaDetalhadasClientesActionPerformed
        new ListarVendasDetalhadasClientes( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jmVendaDetalhadasClientesActionPerformed

    private void jmRelatorioTodosServicosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioTodosServicosActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioTodosServicosActionPerformed
        new ListarServicosVisao(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmRelatorioTodosServicosActionPerformed

    private void jmRelatorioAcertoStockActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioAcertoStockActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioAcertoStockActionPerformed
//        relatorioAcertoStockVisualizar();
        new RelatorioAcertoStock( cod_utilizador, this.conexao ).setVisible( true );
    }//GEN-LAST:event_jmRelatorioAcertoStockActionPerformed

    private void jmHistoricoBonusEmpresaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmHistoricoBonusEmpresaActionPerformed
    {//GEN-HEADEREND:event_jmHistoricoBonusEmpresaActionPerformed
        exibirRElatorioBonus();
    }//GEN-LAST:event_jmHistoricoBonusEmpresaActionPerformed

    private void jmRelatorioComprasPorFornecedorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioComprasPorFornecedorActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioComprasPorFornecedorActionPerformed
        new ListarRelatorioCompra( this.conexao ).setVisible( true );
    }//GEN-LAST:event_jmRelatorioComprasPorFornecedorActionPerformed

    private void jmReeprmirCompraActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmReeprmirCompraActionPerformed
    {//GEN-HEADEREND:event_jmReeprmirCompraActionPerformed
        new ReemprimirCompraVisao( this, rootPaneCheckingEnabled ).setVisible(true);
    }//GEN-LAST:event_jmReeprmirCompraActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem13ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem13ActionPerformed
        iniciarProcessoAnulacaoCompras();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem6ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem6ActionPerformed
        new ListarRelatorioNotasCreditosCompra().setVisible( true );
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jmRelatorioQTDCompradosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmRelatorioQTDCompradosActionPerformed
    {//GEN-HEADEREND:event_jmRelatorioQTDCompradosActionPerformed
        try
        {
            // TODO add your handling code here:
            new ListarRelatorioComprasDiario(cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jmRelatorioQTDCompradosActionPerformed

    private void btnProdutosEspiradosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnProdutosEspiradosActionPerformed
    {//GEN-HEADEREND:event_btnProdutosEspiradosActionPerformed
        // TODO add your handling code here:
        mostrarNumeroDeprodutosExpirados();
        new ProdutosExpiradosVisao( this, true ).setVisible( true );
    }//GEN-LAST:event_btnProdutosEspiradosActionPerformed

    private void jmNotaLevantamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmNotaLevantamentoActionPerformed
        // TODO add your handling code here:
        new NotaLevantamentoVisao( cod_utilizador, this.conexao ).setVisible( true );
    }//GEN-LAST:event_jmNotaLevantamentoActionPerformed

    private void jmFrontOfficeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmFrontOfficeActionPerformed
    {//GEN-HEADEREND:event_jmFrontOfficeActionPerformed
        // TODO add your handling code here:
        new FrontOfficeVisao( cod_utilizador, BDConexao.getInstancia()).setVisible(true);
//        new PosVisao("", cod_utilizador, this.conexao ).co( true );
    }//GEN-LAST:event_jmFrontOfficeActionPerformed

    private void jmTransferenciaArmazemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmTransferenciaArmazemActionPerformed
    {//GEN-HEADEREND:event_jmTransferenciaArmazemActionPerformed
        // TODO add your handling code here:
        new TranferenciaArmazemVisao( cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmTransferenciaArmazemActionPerformed

    private void jMenuItemGestaoCreditosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemGestaoCreditosActionPerformed
    {//GEN-HEADEREND:event_jMenuItemGestaoCreditosActionPerformed
        new GestaoCreditos( this.cod_utilizador ).setVisible(true);
    }//GEN-LAST:event_jMenuItemGestaoCreditosActionPerformed

    private void jmSaidasProdutosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmSaidasProdutosActionPerformed
    {//GEN-HEADEREND:event_jmSaidasProdutosActionPerformed
        try
        {

            new SaidaProdutoVisao( cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch (Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jmSaidasProdutosActionPerformed

    private void jMenuItemRectificacaoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemRectificacaoActionPerformed
    {//GEN-HEADEREND:event_jMenuItemRectificacaoActionPerformed
                try
        {
            new NotasCreditoParcialVisao( cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemRectificacaoActionPerformed

    private void jmListagensTodosProdutoComDescontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmListagensTodosProdutoComDescontoActionPerformed
        // TODO add your handling code here:

        new ListagemTodosProdutosComDesconto( this.conexao );
    }//GEN-LAST:event_jmListagensTodosProdutoComDescontoActionPerformed

    private void jmListaClientesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmListaClientesActionPerformed
    {//GEN-HEADEREND:event_jmListaClientesActionPerformed

        // TODO add your handling code here
        new ListaClientes( this.conexao );

    }//GEN-LAST:event_jmListaClientesActionPerformed

    private void jmReeprmirNotaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmReeprmirNotaActionPerformed
    {//GEN-HEADEREND:event_jmReeprmirNotaActionPerformed
        new ReemprimirNotaVisao( this, rootPaneCheckingEnabled, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jmReeprmirNotaActionPerformed

    private void jMenuConfiguracoesSistemaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuConfiguracoesSistemaActionPerformed
    {//GEN-HEADEREND:event_jMenuConfiguracoesSistemaActionPerformed
        new ConfiguracoesVisao( this, rootPaneCheckingEnabled ).setVisible(true);
    }//GEN-LAST:event_jMenuConfiguracoesSistemaActionPerformed

    private void jmEstornosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmEstornosActionPerformed
    {//GEN-HEADEREND:event_jmEstornosActionPerformed
        try
        {

            new EstornoVisao( cod_utilizador, BDConexao.getInstancia()).setVisible(true);

        }
        catch (Exception ex )
        {
            ex.printStackTrace();
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jmEstornosActionPerformed

    private void jMenuItemRelatorioQuebrasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemRelatorioQuebrasActionPerformed
    {//GEN-HEADEREND:event_jMenuItemRelatorioQuebrasActionPerformed
        new ListarRelatorioQuebras( this.conexao ).setVisible(true);
    }//GEN-LAST:event_jMenuItemRelatorioQuebrasActionPerformed

    private void jmReactivarProdutosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jmReactivarProdutosActionPerformed
    {//GEN-HEADEREND:event_jmReactivarProdutosActionPerformed
        try
        {
            // TODO add your handling code here:

            new ReactivarProdutosVisao( this, rootPaneCheckingEnabled, cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( Exception ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jmReactivarProdutosActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem12ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem12ActionPerformed
        new GuiaTransportesVisao( cod_utilizador, this.conexao ).setVisible( true );
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItemAlteracaoGuiaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemAlteracaoGuiaActionPerformed
    {//GEN-HEADEREND:event_jMenuItemAlteracaoGuiaActionPerformed
        try
        {
            // TODO add your handling code here:
            new AlteracaoGuiaTransporteVisao(this.cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jMenuItemAlteracaoGuiaActionPerformed

    private void jMenuItemConverterGuiaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemConverterGuiaActionPerformed
    {//GEN-HEADEREND:event_jMenuItemConverterGuiaActionPerformed
        try
        {
            // TODO add your handling code here:
            new ConverterGuiaFacturaVisao(this.cod_utilizador, BDConexao.getInstancia()).setVisible(true);
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event_jMenuItemConverterGuiaActionPerformed

    private void jMenuItemRelatorioQtdDetalhadoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemRelatorioQtdDetalhadoActionPerformed
    {//GEN-HEADEREND:event_jMenuItemRelatorioQtdDetalhadoActionPerformed
        try
        {
            new ListarRelatorioDiarioByQtdDetalhado( cod_utilizador, this.conexao ).setVisible( administrador );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemRelatorioQtdDetalhadoActionPerformed

    private void jMenuRelatorioTransferenciaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuRelatorioTransferenciaActionPerformed
    {//GEN-HEADEREND:event_jMenuRelatorioTransferenciaActionPerformed
        new RelatorioTransferenciaArmazem( cod_utilizador, BDConexao.getInstancia()).setVisible(true);
    }//GEN-LAST:event_jMenuRelatorioTransferenciaActionPerformed

    private void jMenuItemRelatorioMensalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemRelatorioMensalActionPerformed
    {//GEN-HEADEREND:event_jMenuItemRelatorioMensalActionPerformed
        new RelatorioMensal( conexao ).setVisible( true );
    }//GEN-LAST:event_jMenuItemRelatorioMensalActionPerformed

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
                if ( "Nimbus".equals( info.getName() ) )
                {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        }
        catch ( ClassNotFoundException ex )
        {
            java.util.logging.Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( InstantiationException ex )
        {
            java.util.logging.Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( IllegalAccessException ex )
        {
            java.util.logging.Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        catch ( javax.swing.UnsupportedLookAndFeelException ex )
        {
            java.util.logging.Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                //new MenuPrincipalOficinaVisao().setVisible(true);
            }
        } );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProdutosEspirados;
    private javax.swing.JMenuItem jDadosEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuConfiguracoesSistema;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemAlteracaoGuia;
    private javax.swing.JMenuItem jMenuItemConverterGuia;
    private javax.swing.JMenuItem jMenuItemGestaoCreditos;
    private javax.swing.JMenuItem jMenuItemRectificacao;
    private javax.swing.JMenuItem jMenuItemRelatorioMensal;
    private javax.swing.JMenuItem jMenuItemRelatorioQtdDetalhado;
    private javax.swing.JMenuItem jMenuItemRelatorioQuebras;
    private javax.swing.JMenuItem jMenuRelatorioTransferencia;
    private javax.swing.JMenuItem jPercentagemDesconto;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JMenuItem jmAcertoStock;
    private javax.swing.JMenuItem jmAnulamentoSaidasProdutos;
    private javax.swing.JMenuItem jmAutorizacaoCompras;
    private javax.swing.JMenuItem jmCadastroArmazem;
    private javax.swing.JMenuItem jmCadastroCliente;
    private javax.swing.JMenuItem jmCadastroUsuario;
    private javax.swing.JMenuItem jmCompras;
    private javax.swing.JMenuItem jmConverterProforma;
    private javax.swing.JMenuItem jmEncomendas;
    private javax.swing.JMenuItem jmEstornos;
    private javax.swing.JMenuItem jmFamilia;
    public static javax.swing.JMenuItem jmFrontOffice;
    private javax.swing.JMenuItem jmGavetasPrateleiras;
    private javax.swing.JMenuItem jmGerarSaft;
    private javax.swing.JMenuItem jmGrupo;
    private javax.swing.JMenuItem jmHistoricoBonusEmpresa;
    private javax.swing.JMenuItem jmImprimirPrecos;
    private javax.swing.JMenuItem jmListaClientes;
    private javax.swing.JMenuItem jmListagensTodosProdutoComDesconto;
    private javax.swing.JMenuItem jmListagensTodosProdutos;
    private javax.swing.JMenuItem jmListarProdutosStock;
    private javax.swing.JMenuItem jmListarUsuario;
    private javax.swing.JMenuItem jmMapaExistencia;
    private javax.swing.JMenuItem jmMarca;
    private javax.swing.JMenuItem jmModelo;
    private javax.swing.JMenuItem jmNotaLevantamento;
    private javax.swing.JMenu jmNotas;
    private javax.swing.JMenu jmNotasCreditoCompra;
    private javax.swing.JMenuItem jmPermissao;
    private javax.swing.JMenuItem jmProcessarRecibo;
    private javax.swing.JMenuItem jmProduto;
    private javax.swing.JMenuItem jmProdutosActualizar;
    private javax.swing.JMenuItem jmReactivarProdutos;
    private javax.swing.JMenuItem jmReeprmirCompra;
    private javax.swing.JMenuItem jmReeprmirFacura;
    private javax.swing.JMenuItem jmReeprmirNota;
    private javax.swing.JMenuItem jmReimprimirSaidasProdutos;
    private javax.swing.JMenuItem jmRelatorioAcertoStock;
    private javax.swing.JMenuItem jmRelatorioComprasPorFornecedor;
    private javax.swing.JMenuItem jmRelatorioDiario;
    private javax.swing.JMenuItem jmRelatorioDiarioTodasVendasTempoReal;
    private javax.swing.JMenuItem jmRelatorioFormaPagamento;
    private javax.swing.JMenuItem jmRelatorioNotasCredito;
    private javax.swing.JMenuItem jmRelatorioPorFonecedor;
    private javax.swing.JMenuItem jmRelatorioQTDComprados;
    private javax.swing.JMenuItem jmRelatorioTodosServicos;
    private javax.swing.JMenuItem jmRelatorioVendasPorClienteData;
    private javax.swing.JMenuItem jmRelatorioVendasPorUsuarioData;
    private javax.swing.JMenuItem jmRelatoriosSaidasProdutosPorDatas;
    private javax.swing.JMenuItem jmSaidasProdutos;
    private javax.swing.JMenuItem jmSolicitacaoCompras;
    private javax.swing.JMenuItem jmSubFamilia;
    private javax.swing.JMenuItem jmTodasVendas;
    private javax.swing.JMenuItem jmTransferenciaArmazem;
    private javax.swing.JMenuItem jmTurno;
    private javax.swing.JMenuItem jmUnidades;
    private javax.swing.JMenuItem jmVenda;
    private javax.swing.JMenuItem jmVendaDetalhadasClientes;
    private javax.swing.JMenuItem jmVendaDetalhadasUsuarios;
    private javax.swing.JLabel lb_nome_usuario;
    // End of variables declaration//GEN-END:variables

    private void emitirNotaCreditoDebito()
    {
        try
        {
            new NotasCreditoDebitoVisao( cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void iniciarProcessoAnulacao()
    {
        try
        {
            new NotasCreditoDebitoAnulacaoVisao( cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void iniciarProcessoRectificacao()
    {
        try
        {
            new NotasCreditoRetificacaoVisao( cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void iniciarProcessoAnulacaoCompras()
    {
        try
        {
            new NotasCreditoDebitoAnulacaoComprasVisao( cod_utilizador, this.conexao ).setVisible( true );
        }
        catch ( SQLException ex )
        {
            Logger.getLogger(MenuPrincipalTransporteVisao.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void relatorioAcertoStockVisualizar()
    {
        new AcertoStockHistoricoVisualizarJFrame().setVisible( true );
    }

    private void exibirRElatorioBonus()
    {
        new RelatorioBonusVisao( cod_utilizador ).setVisible( true );
    }

    private void mostrarNumeroDeprodutosExpirados()
    {
        List<TbProduto> listProdutos = new ProdutoDao( emf ).getAllProdutosExpirados();
        btnProdutosEspirados.setText( ( listProdutos.isEmpty() ? "0" : String.valueOf( listProdutos.size() ) ) );
    }

    private void setArmazem( String armazem )
    {
        if ( !armazem.equalsIgnoreCase( "Multi_armazem" ) )
        {
            jmTransferenciaArmazem.setVisible( false );
            jmFrontOffice.setVisible( false );

        }
        else
        {
            jmTransferenciaArmazem.setVisible( true );
            jmFrontOffice.setVisible( true );

        }
    }

    public static void fazerBackupAgora()
    {
        String data = new SimpleDateFormat( YYYYMMDD_HHMMSS ).format( new Date() );
//        String rodar_camando = "cmd /c mysqldump -uroot -pDoV90x?# --dump-date --triggers --tables --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        String rodar_camando = "cmd /c mysqldump --single-transaction -uroot -pDoV90x?# --dump-date --triggers --add-drop-database --routines --skip-quote-names --skip-set-charset --add-locks --disable-keys --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
//String rodar_camando = "cmd /c mysqldump --single-transaction=TRUE -uroot -pDoV90x?# --dump-date --triggers --add-drop-database  --routines --skip-quote-names --compact --skip-opt --skip-set-charset --hex-blob --add-locks --disable-keys --lock-tables  --databases kitanda_db > \"..\\BD_BACKUP\\_database_backup_" + data + ".sql\"";
        Process rodarComandoWindows = rodarComandoWindows( rodar_camando, true );

//        JOptionPane.showMessageDialog ( null, "Backup realizado com sucesso! ", "Notificação", JOptionPane.INFORMATION_MESSAGE );
        System.err.println( "Backup realizado com sucesso! " );

    }

}
