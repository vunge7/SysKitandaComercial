/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.BalancoDao;
import dao.OperacaoSistemaDao;
import dao.UsuarioDao;
import entity.TbBalanco;
import entity.TbOperacaoSistema;

import entity.TbUsuario;
import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import tesouraria.visao.EntradaTesourariaVisao;
import tesouraria.visao.TransferenciasBancarias;
import visao.AnulamentoEntradaVisao;
import visao.AnulamentoFacturaVisao;
import visao.PagamentoCreditoVisao;
import visao.ReciclagemVisao;
import visao.*;

/**
 *
 * @author Domingos Dala Vunge
 *
 */
public class OperacaoSistemaUtil implements Runnable
{

    private static EntityManagerFactory emf = JPAEntityMannagerFactoryUtil.em;
    private static UsuarioDao usuarioDao = new UsuarioDao ( emf );
    private static BalancoDao balancoDao = new BalancoDao ( emf );
    private static OperacaoSistemaDao operacaoSistemaDao = new OperacaoSistemaDao ( emf );

    public static Thread t;

    public OperacaoSistemaUtil ()
    {
        t = new Thread ( this );
        t.start ();
    }

    @Override
    public void run ()
    {

      
    }

    private void activar_componentes ( boolean status )
    {

        /* ATERAR OS COMPONENTES NO FORMULARIO DAS VENDAS */
        try
        {
//            VendaUsuarioVisao.txtValorEntregue.setEnabled ( status );
//            VendaUsuarioVisao.txtClienteNome.setEnabled(status);
            VendaUsuarioVisao.cmbCliente.setEnabled ( status );
            VendaUsuarioVisao.txtQuatindade.setEnabled ( status );
            VendaUsuarioVisao.txtCodigoProduto.setEnabled ( status );
            VendaUsuarioVisao.txtCodigoBarra.setEnabled ( status );
            VendaUsuarioVisao.txtCodigoManual.setEnabled ( status );
            VendaUsuarioVisao.btn_adicionar.setEnabled ( status );
            VendaUsuarioVisao.btn_remover.setEnabled ( status );
//            VendaUsuarioVisao.btn_imprimir_factura.setEnabled ( status );
        }
        catch ( Exception e )
        {
        }

        try
        {
            /*ENTRADA DE PRODUTOS*/
            EntradaVisao.txtCodigoProduto.setEnabled ( status );
            EntradaVisao.txtCodigoManual.setEnabled ( status );
            EntradaVisao.txtCodigoBarra.setEnabled ( status );
            EntradaVisao.txtQuatindade.setEnabled ( status );
            EntradaVisao.btn_adicionar.setEnabled ( status );
            EntradaVisao.btn_remover.setEnabled ( status );
            EntradaVisao.btn_salvar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*SAIDA DE PRODUTOS*/
            SaidaUsuarioVisao.txtCodigoProduto.setEnabled ( status );
            SaidaUsuarioVisao.txtCodigoBarra.setEnabled ( status );
            SaidaUsuarioVisao.txtQuatindade.setEnabled ( status );
            SaidaUsuarioVisao.btn_adicionar.setEnabled ( status );
            SaidaUsuarioVisao.btn_remover.setEnabled ( status );
            SaidaUsuarioVisao.btn_imprimir_factura.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*ANULAMENTO DE FACTURA*/
            AnulamentoFacturaVisao.txtFactura.setEnabled ( status );
            AnulamentoFacturaVisao.btnEliminar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*ANULAMENTO DE SAIDAS*/
            AnulamentoSaidasProdutosVisao.txtCodigoSaida.setEnabled ( status );
            AnulamentoSaidasProdutosVisao.btnEliminar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*ANULAMENTO DE ENTRADA DE PRODUTOS*/
            AnulamentoEntradaVisao.btn_eliminar.setEnabled ( status );
            AnulamentoEntradaVisao.txtCodigoEntrada.setEnabled ( status );
        }
        catch ( Exception e )
        {
        }

        try
        {
            /*RECICLAGEM*/
            ReciclagemVisao.tabela_reciclagem.setEnabled ( status );
        }
        catch ( Exception e )
        {
        }

        try
        {
            /*PAGAMENTOS CRÉDITOS*/
            PagamentoCreditoVisao.txtFactura.setEnabled ( status );
            PagamentoCreditoVisao.btn_salvar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*ENTRADA TESOURARIA*/
            EntradaTesourariaVisao.btn_enviar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*SAIDA TESOURARIA*/
            tesouraria.visao.SaidaTesourariaVisao.btn_enviar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*TRANSFERÊNCIA*/
            TransferenciasBancarias.btn_enviar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }

        try
        {
            /*LEVANTAMENTO*/
            tesouraria.visao.LevantamentoBancarioVisao.btn_levantar.setEnabled ( status );
        }
        catch ( Exception e )
        {
        }

        try
        {
            /*DEPOSITAR*/
            tesouraria.visao.DepositoBancarioVisao.btn_depositar.setEnabled ( status );

        }
        catch ( Exception e )
        {
        }
        try
        {
            /*TRANSFERÊNCIA*/
//            AcertoStockVisao.btn_actualizar.setEnabled(status);

        }
        catch ( Exception e )
        {
        }

    }

    private static TbOperacaoSistema preparar_abertura_dia ( TbUsuario usuario )
    {

        TbOperacaoSistema os = new TbOperacaoSistema ();

        os.setDataAbertura ( new Date () );
        os.setHoraAbertura ( new Date () );
        os.setDataFeicho ( null );
        os.setHoraFeicho ( null );
        os.setFkUsuario ( usuario );
        return os;

    }
    
    private static TbOperacaoSistema preparar_reabertura_dia ( TbUsuario usuario )
    {

        TbOperacaoSistema os = new TbOperacaoSistema ();

        os.setDataAbertura ( null );
        os.setHoraAbertura ( null );
        os.setDataFeicho ( null );
        os.setHoraFeicho ( null );
        os.setFkUsuario ( usuario );
        return os;

    }

    private static TbOperacaoSistema preparar_feicho_dia ( long last_pk )
    {

        TbOperacaoSistema os = operacaoSistemaDao.findTbOperacaoSistema ( last_pk );
        os.setDataFeicho ( new Date () );
        os.setHoraFeicho ( new Date () );

        return os;

    }

    public static void abrir_dia ( int pk_user )
    {

        try
        {
            operacaoSistemaDao.create ( preparar_abertura_dia ( usuarioDao.findTbUsuario ( pk_user ) ) );
            JOptionPane.showMessageDialog ( null, "Operações aberta com sucesso", "INFO", JOptionPane.INFORMATION_MESSAGE );
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog ( null, "Falha ao abrir as operações", "ERRO", JOptionPane.ERROR_MESSAGE );
        }

    }
    
    public static void reabrir_dia ( int pk_user )
    {

        try
        {
            operacaoSistemaDao.create(preparar_reabertura_dia ( usuarioDao.findTbUsuario ( pk_user ) ) );
            JOptionPane.showMessageDialog ( null, "Operações reabertas com sucesso", "INFO", JOptionPane.INFORMATION_MESSAGE );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog ( null, "Falha ao reabrir as operações", "ERRO", JOptionPane.ERROR_MESSAGE );
        }

    }

    private static void feicho_do_dia ( BDConexao conexao )
    {
        try
        {
            operacaoSistemaDao.edit ( preparar_feicho_dia ( operacaoSistemaDao.getLastIdOperacao () ) );
            JOptionPane.showMessageDialog ( null, "Operações fechadas com sucesso", "INFO", JOptionPane.INFORMATION_MESSAGE );
            MetodosUtil.actualizar_existencia_anterior ( conexao );
//            procedimento_balanco ( conexao );
            operacao_fechar_run ();
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
            //JOptionPane.showMessageDialog(null, "Falha ao fechar as operações", "ERRO", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void procedimento_abrir_dia ( int pk_user )
    {

        if (  ! operacaoSistemaDao.existe_abertura ( new Date () ) )
        {

            int opcao = JOptionPane.showConfirmDialog ( null, "Caro Usuario: " + usuarioDao.findTbUsuario ( pk_user ).getNome () + " "
                    + usuarioDao.findTbUsuario ( pk_user ).getSobreNome () + "\n" + "Tens a plena certeza que pretendes abrir o dia ?" );
            if ( opcao == JOptionPane.YES_OPTION )
            {
                abrir_dia ( pk_user );
            }
            else if ( opcao == JOptionPane.NO_OPTION )
            {
                JOptionPane.showMessageDialog ( null, "Abertura cancelada", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE );
            }

        }
        else
        {
            JOptionPane.showMessageDialog ( null, "Falha: Ja existe uma abertura para este dia", "FALHA", JOptionPane.ERROR_MESSAGE );
        }

    }
    
    public static void procedimento_reabrir_dia ( int pk_user )
    {


            int opcao = JOptionPane.showConfirmDialog ( null, "Caro Usuario: " + usuarioDao.findTbUsuario ( pk_user ).getNome () + " "
                    + usuarioDao.findTbUsuario ( pk_user ).getSobreNome () + "\n" + "Tens a plena certeza que pretendes reabrir o dia ?" );
            if ( opcao == JOptionPane.YES_OPTION )
            {
                reabrir_dia ( pk_user );
            }
            else if ( opcao == JOptionPane.NO_OPTION )
            {
                JOptionPane.showMessageDialog ( null, "Reabertura cancelada", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE );
            }


    }

    public static void procedimento_fechar_dia ( int pk_user, BDConexao conexao )
    {

        if ( operacaoSistemaDao.existe_abertura ( new Date () ) )
        {

            int opcao = JOptionPane.showConfirmDialog ( null, "Caro Usuario: " + usuarioDao.findTbUsuario ( pk_user ).getNome () + " "
                    + usuarioDao.findTbUsuario ( pk_user ).getSobreNome () + "\n" + "Tens a plena certeza que pretendes fechar o dia ?" );
            if ( opcao == JOptionPane.YES_OPTION )
            {
                feicho_do_dia ( conexao );
            }
            else if ( opcao == JOptionPane.NO_OPTION )
            {
                JOptionPane.showMessageDialog ( null, "Feicho cancelado", DVML.DVML_COMECIAL_LDA, JOptionPane.INFORMATION_MESSAGE );
            }

        }
        else
        {
//            JOptionPane.showMessageDialog(null, "Falha: Não existe nenhuma abertura para este dia\nImpossivel fechar!.", "FALHA", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void procedimento_balanco ( BDConexao conexao )
    {
        TbBalanco balanco = new TbBalanco ();
        preparar_balanco ( balanco, conexao );
        try
        {
            balancoDao.create ( balanco );
            JOptionPane.showMessageDialog ( null, "Balanço do Dia:  \n"
                    + "VALOR DINHEIRO: " + balanco.getValorBancoCaixa () + "\n"
                    + "OUTROS BANCOS: " + balanco.getValorBancoOutros () + "\n"
                    + "VALOR STOCK: " + balanco.getValorBancoStock () + "\n"
                    + "TOTAL: " + ( balanco.getValorBancoCaixa () + balanco.getValorBancoOutros () + balanco.getValorBancoStock () ) + "\n"
            );
        }
        catch ( Exception e )
        {
            e.printStackTrace ();
            JOptionPane.showMessageDialog ( null, "Falha ao criar o balanço do dia", "ERRO", JOptionPane.ERROR_MESSAGE );

        }

    }

    public static void operacao_fechar_run ()
    {
        try
        {
            OperacaoSistemaUtil.t.stop ();
        }
        catch ( Exception e )
        {
        }

    }

    private static void preparar_balanco ( TbBalanco balanco, BDConexao conexao )
    {

        balanco.setDataBalanco ( new Date () );
        balanco.setValorBancoCaixa ( MetodosUtil.getSaldoByBanco ( DVML.COD_BANCO_CAIXA, conexao ) );
        balanco.setValorBancoOutros ( MetodosUtil.getAllSaldoExceptoCaixa ( conexao ) );
        balanco.setValorBancoStock ( MetodosUtil.procedimento_valor_stock ( conexao ) );

    }

}
