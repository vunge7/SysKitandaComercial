/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;


import java.sql.Connection;
import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TbStatus;
import entity.TbTipoUsuario;
import entity.TbSexo;
import entity.ItemSalarioExtra;
import java.util.ArrayList;
import java.util.List;
import entity.NotasCompras;
import entity.Notas;
import entity.Turno;
import entity.Compras;
import entity.PedidoFeria;
import entity.TransferenciaBancaria;
import entity.TbEncomenda;
import entity.TbEstorno;
import entity.LevantamentoBancario;
import entity.PagamentoSubsidioFeriaNatal;
import entity.SaidasTesouraria;
import entity.TbAcerto;
import entity.TbProForma;
import entity.FechoPeriodo;
import entity.Amortizacao;
import entity.Anexos;
import entity.FechoContrato;
import entity.DepositoBancario;
import entity.TbFuncionario;
import entity.TbDesconto;
import entity.TbOperacaoSistema;
import entity.TbSaidasProdutos;
import entity.TbPreco;
import entity.TbEntrada;
import entity.TbEntradaVasilhame;
import entity.TbVenda;
import entity.Promocao;
import entity.EntradaTesouraria;
import entity.TbItemPermissao;
import entity.AccessoArmazem;
import entity.PrevioAviso;
import entity.TbSaidaVasilhame;
import entity.AmortizacaoDivida;
import entity.TbUsuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbUsuarioJpaController1 implements Serializable
{

    public TbUsuarioJpaController1( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbUsuario tbUsuario )
    {
        if ( tbUsuario.getItemSalarioExtraList() == null )
        {
            tbUsuario.setItemSalarioExtraList( new ArrayList<ItemSalarioExtra>() );
        }
        if ( tbUsuario.getNotasComprasList() == null )
        {
            tbUsuario.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( tbUsuario.getNotasList() == null )
        {
            tbUsuario.setNotasList( new ArrayList<Notas>() );
        }
        if ( tbUsuario.getTurnoList() == null )
        {
            tbUsuario.setTurnoList( new ArrayList<Turno>() );
        }
        if ( tbUsuario.getComprasList() == null )
        {
            tbUsuario.setComprasList( new ArrayList<Compras>() );
        }
        if ( tbUsuario.getPedidoFeriaList() == null )
        {
            tbUsuario.setPedidoFeriaList( new ArrayList<PedidoFeria>() );
        }
        if ( tbUsuario.getTransferenciaBancariaList() == null )
        {
            tbUsuario.setTransferenciaBancariaList( new ArrayList<TransferenciaBancaria>() );
        }
        if ( tbUsuario.getTbEncomendaList() == null )
        {
            tbUsuario.setTbEncomendaList( new ArrayList<TbEncomenda>() );
        }
        if ( tbUsuario.getTbEstornoList() == null )
        {
            tbUsuario.setTbEstornoList( new ArrayList<TbEstorno>() );
        }
        if ( tbUsuario.getLevantamentoBancarioList() == null )
        {
            tbUsuario.setLevantamentoBancarioList( new ArrayList<LevantamentoBancario>() );
        }
        if ( tbUsuario.getPagamentoSubsidioFeriaNatalList() == null )
        {
            tbUsuario.setPagamentoSubsidioFeriaNatalList( new ArrayList<PagamentoSubsidioFeriaNatal>() );
        }
        if ( tbUsuario.getSaidasTesourariaList() == null )
        {
            tbUsuario.setSaidasTesourariaList( new ArrayList<SaidasTesouraria>() );
        }
        if ( tbUsuario.getTbAcertoList() == null )
        {
            tbUsuario.setTbAcertoList( new ArrayList<TbAcerto>() );
        }
        if ( tbUsuario.getTbProFormaList() == null )
        {
            tbUsuario.setTbProFormaList( new ArrayList<TbProForma>() );
        }
        if ( tbUsuario.getFechoPeriodoList() == null )
        {
            tbUsuario.setFechoPeriodoList( new ArrayList<FechoPeriodo>() );
        }
        if ( tbUsuario.getAmortizacaoList() == null )
        {
            tbUsuario.setAmortizacaoList( new ArrayList<Amortizacao>() );
        }
        if ( tbUsuario.getAnexosList() == null )
        {
            tbUsuario.setAnexosList( new ArrayList<Anexos>() );
        }
        if ( tbUsuario.getFechoContratoList() == null )
        {
            tbUsuario.setFechoContratoList( new ArrayList<FechoContrato>() );
        }
        if ( tbUsuario.getDepositoBancarioList() == null )
        {
            tbUsuario.setDepositoBancarioList( new ArrayList<DepositoBancario>() );
        }
        if ( tbUsuario.getTbFuncionarioList() == null )
        {
            tbUsuario.setTbFuncionarioList( new ArrayList<TbFuncionario>() );
        }
        if ( tbUsuario.getTbDescontoList() == null )
        {
            tbUsuario.setTbDescontoList( new ArrayList<TbDesconto>() );
        }
        if ( tbUsuario.getTbOperacaoSistemaList() == null )
        {
            tbUsuario.setTbOperacaoSistemaList( new ArrayList<TbOperacaoSistema>() );
        }
        if ( tbUsuario.getTbSaidasProdutosList() == null )
        {
            tbUsuario.setTbSaidasProdutosList( new ArrayList<TbSaidasProdutos>() );
        }
        if ( tbUsuario.getTbPrecoList() == null )
        {
            tbUsuario.setTbPrecoList( new ArrayList<TbPreco>() );
        }
        if ( tbUsuario.getTbEntradaList() == null )
        {
            tbUsuario.setTbEntradaList( new ArrayList<TbEntrada>() );
        }
        if ( tbUsuario.getTbEntradaVasilhameList() == null )
        {
            tbUsuario.setTbEntradaVasilhameList( new ArrayList<TbEntradaVasilhame>() );
        }
        if ( tbUsuario.getTbVendaList() == null )
        {
            tbUsuario.setTbVendaList( new ArrayList<TbVenda>() );
        }
        if ( tbUsuario.getPromocaoList() == null )
        {
            tbUsuario.setPromocaoList( new ArrayList<Promocao>() );
        }
        if ( tbUsuario.getEntradaTesourariaList() == null )
        {
            tbUsuario.setEntradaTesourariaList( new ArrayList<EntradaTesouraria>() );
        }
        if ( tbUsuario.getTbItemPermissaoList() == null )
        {
            tbUsuario.setTbItemPermissaoList( new ArrayList<TbItemPermissao>() );
        }
        if ( tbUsuario.getAccessoArmazemList() == null )
        {
            tbUsuario.setAccessoArmazemList( new ArrayList<AccessoArmazem>() );
        }
        if ( tbUsuario.getPrevioAvisoList() == null )
        {
            tbUsuario.setPrevioAvisoList( new ArrayList<PrevioAviso>() );
        }
        if ( tbUsuario.getTbSaidaVasilhameList() == null )
        {
            tbUsuario.setTbSaidaVasilhameList( new ArrayList<TbSaidaVasilhame>() );
        }
        if ( tbUsuario.getAmortizacaoDividaList() == null )
        {
            tbUsuario.setAmortizacaoDividaList( new ArrayList<AmortizacaoDivida>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbStatus idStatus = tbUsuario.getIdStatus();
            if ( idStatus != null )
            {
                idStatus = em.getReference( idStatus.getClass(), idStatus.getIdStatus() );
                tbUsuario.setIdStatus( idStatus );
            }
            TbTipoUsuario idTipoUsuario = tbUsuario.getIdTipoUsuario();
            if ( idTipoUsuario != null )
            {
                idTipoUsuario = em.getReference( idTipoUsuario.getClass(), idTipoUsuario.getIdTipoUsuario() );
                tbUsuario.setIdTipoUsuario( idTipoUsuario );
            }
            TbSexo codigoSexo = tbUsuario.getCodigoSexo();
            if ( codigoSexo != null )
            {
                codigoSexo = em.getReference( codigoSexo.getClass(), codigoSexo.getCodigo() );
                tbUsuario.setCodigoSexo( codigoSexo );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraList = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtraToAttach : tbUsuario.getItemSalarioExtraList() )
            {
                itemSalarioExtraListItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListItemSalarioExtraToAttach.getClass(), itemSalarioExtraListItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraList.add( itemSalarioExtraListItemSalarioExtraToAttach );
            }
            tbUsuario.setItemSalarioExtraList( attachedItemSalarioExtraList );
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : tbUsuario.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            tbUsuario.setNotasComprasList( attachedNotasComprasList );
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : tbUsuario.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            tbUsuario.setNotasList( attachedNotasList );
            List<Turno> attachedTurnoList = new ArrayList<Turno>();
            for ( Turno turnoListTurnoToAttach : tbUsuario.getTurnoList() )
            {
                turnoListTurnoToAttach = em.getReference( turnoListTurnoToAttach.getClass(), turnoListTurnoToAttach.getPkTurno() );
                attachedTurnoList.add( turnoListTurnoToAttach );
            }
            tbUsuario.setTurnoList( attachedTurnoList );
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for ( Compras comprasListComprasToAttach : tbUsuario.getComprasList() )
            {
                comprasListComprasToAttach = em.getReference( comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getPkCompra() );
                attachedComprasList.add( comprasListComprasToAttach );
            }
            tbUsuario.setComprasList( attachedComprasList );
            List<PedidoFeria> attachedPedidoFeriaList = new ArrayList<PedidoFeria>();
            for ( PedidoFeria pedidoFeriaListPedidoFeriaToAttach : tbUsuario.getPedidoFeriaList() )
            {
                pedidoFeriaListPedidoFeriaToAttach = em.getReference( pedidoFeriaListPedidoFeriaToAttach.getClass(), pedidoFeriaListPedidoFeriaToAttach.getPkPedidoFeria() );
                attachedPedidoFeriaList.add( pedidoFeriaListPedidoFeriaToAttach );
            }
            tbUsuario.setPedidoFeriaList( attachedPedidoFeriaList );
            List<TransferenciaBancaria> attachedTransferenciaBancariaList = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancariaToAttach : tbUsuario.getTransferenciaBancariaList() )
            {
                transferenciaBancariaListTransferenciaBancariaToAttach = em.getReference( transferenciaBancariaListTransferenciaBancariaToAttach.getClass(), transferenciaBancariaListTransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaList.add( transferenciaBancariaListTransferenciaBancariaToAttach );
            }
            tbUsuario.setTransferenciaBancariaList( attachedTransferenciaBancariaList );
            List<TbEncomenda> attachedTbEncomendaList = new ArrayList<TbEncomenda>();
            for ( TbEncomenda tbEncomendaListTbEncomendaToAttach : tbUsuario.getTbEncomendaList() )
            {
                tbEncomendaListTbEncomendaToAttach = em.getReference( tbEncomendaListTbEncomendaToAttach.getClass(), tbEncomendaListTbEncomendaToAttach.getIdEncomenda() );
                attachedTbEncomendaList.add( tbEncomendaListTbEncomendaToAttach );
            }
            tbUsuario.setTbEncomendaList( attachedTbEncomendaList );
            List<TbEstorno> attachedTbEstornoList = new ArrayList<TbEstorno>();
            for ( TbEstorno tbEstornoListTbEstornoToAttach : tbUsuario.getTbEstornoList() )
            {
                tbEstornoListTbEstornoToAttach = em.getReference( tbEstornoListTbEstornoToAttach.getClass(), tbEstornoListTbEstornoToAttach.getPkEstorno() );
                attachedTbEstornoList.add( tbEstornoListTbEstornoToAttach );
            }
            tbUsuario.setTbEstornoList( attachedTbEstornoList );
            List<LevantamentoBancario> attachedLevantamentoBancarioList = new ArrayList<LevantamentoBancario>();
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancarioToAttach : tbUsuario.getLevantamentoBancarioList() )
            {
                levantamentoBancarioListLevantamentoBancarioToAttach = em.getReference( levantamentoBancarioListLevantamentoBancarioToAttach.getClass(), levantamentoBancarioListLevantamentoBancarioToAttach.getPkLevantamento() );
                attachedLevantamentoBancarioList.add( levantamentoBancarioListLevantamentoBancarioToAttach );
            }
            tbUsuario.setLevantamentoBancarioList( attachedLevantamentoBancarioList );
            List<PagamentoSubsidioFeriaNatal> attachedPagamentoSubsidioFeriaNatalList = new ArrayList<PagamentoSubsidioFeriaNatal>();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach : tbUsuario.getPagamentoSubsidioFeriaNatalList() )
            {
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach = em.getReference( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach.getClass(), pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach.getPkPagamentoSubsidioFeriaNatal() );
                attachedPagamentoSubsidioFeriaNatalList.add( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach );
            }
            tbUsuario.setPagamentoSubsidioFeriaNatalList( attachedPagamentoSubsidioFeriaNatalList );
            List<SaidasTesouraria> attachedSaidasTesourariaList = new ArrayList<SaidasTesouraria>();
            for ( SaidasTesouraria saidasTesourariaListSaidasTesourariaToAttach : tbUsuario.getSaidasTesourariaList() )
            {
                saidasTesourariaListSaidasTesourariaToAttach = em.getReference( saidasTesourariaListSaidasTesourariaToAttach.getClass(), saidasTesourariaListSaidasTesourariaToAttach.getPkSaidasTesouraria() );
                attachedSaidasTesourariaList.add( saidasTesourariaListSaidasTesourariaToAttach );
            }
            tbUsuario.setSaidasTesourariaList( attachedSaidasTesourariaList );
            List<TbAcerto> attachedTbAcertoList = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListTbAcertoToAttach : tbUsuario.getTbAcertoList() )
            {
                tbAcertoListTbAcertoToAttach = em.getReference( tbAcertoListTbAcertoToAttach.getClass(), tbAcertoListTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoList.add( tbAcertoListTbAcertoToAttach );
            }
            tbUsuario.setTbAcertoList( attachedTbAcertoList );
            List<TbProForma> attachedTbProFormaList = new ArrayList<TbProForma>();
            for ( TbProForma tbProFormaListTbProFormaToAttach : tbUsuario.getTbProFormaList() )
            {
                tbProFormaListTbProFormaToAttach = em.getReference( tbProFormaListTbProFormaToAttach.getClass(), tbProFormaListTbProFormaToAttach.getPkProForma() );
                attachedTbProFormaList.add( tbProFormaListTbProFormaToAttach );
            }
            tbUsuario.setTbProFormaList( attachedTbProFormaList );
            List<FechoPeriodo> attachedFechoPeriodoList = new ArrayList<FechoPeriodo>();
            for ( FechoPeriodo fechoPeriodoListFechoPeriodoToAttach : tbUsuario.getFechoPeriodoList() )
            {
                fechoPeriodoListFechoPeriodoToAttach = em.getReference( fechoPeriodoListFechoPeriodoToAttach.getClass(), fechoPeriodoListFechoPeriodoToAttach.getPkFechoPeriodo() );
                attachedFechoPeriodoList.add( fechoPeriodoListFechoPeriodoToAttach );
            }
            tbUsuario.setFechoPeriodoList( attachedFechoPeriodoList );
            List<Amortizacao> attachedAmortizacaoList = new ArrayList<Amortizacao>();
            for ( Amortizacao amortizacaoListAmortizacaoToAttach : tbUsuario.getAmortizacaoList() )
            {
                amortizacaoListAmortizacaoToAttach = em.getReference( amortizacaoListAmortizacaoToAttach.getClass(), amortizacaoListAmortizacaoToAttach.getPkAmortizacao() );
                attachedAmortizacaoList.add( amortizacaoListAmortizacaoToAttach );
            }
            tbUsuario.setAmortizacaoList( attachedAmortizacaoList );
            List<Anexos> attachedAnexosList = new ArrayList<Anexos>();
            for ( Anexos anexosListAnexosToAttach : tbUsuario.getAnexosList() )
            {
                anexosListAnexosToAttach = em.getReference( anexosListAnexosToAttach.getClass(), anexosListAnexosToAttach.getPkAnexos() );
                attachedAnexosList.add( anexosListAnexosToAttach );
            }
            tbUsuario.setAnexosList( attachedAnexosList );
            List<FechoContrato> attachedFechoContratoList = new ArrayList<FechoContrato>();
            for ( FechoContrato fechoContratoListFechoContratoToAttach : tbUsuario.getFechoContratoList() )
            {
                fechoContratoListFechoContratoToAttach = em.getReference( fechoContratoListFechoContratoToAttach.getClass(), fechoContratoListFechoContratoToAttach.getPkFechoContrato() );
                attachedFechoContratoList.add( fechoContratoListFechoContratoToAttach );
            }
            tbUsuario.setFechoContratoList( attachedFechoContratoList );
            List<DepositoBancario> attachedDepositoBancarioList = new ArrayList<DepositoBancario>();
            for ( DepositoBancario depositoBancarioListDepositoBancarioToAttach : tbUsuario.getDepositoBancarioList() )
            {
                depositoBancarioListDepositoBancarioToAttach = em.getReference( depositoBancarioListDepositoBancarioToAttach.getClass(), depositoBancarioListDepositoBancarioToAttach.getPkDeposito() );
                attachedDepositoBancarioList.add( depositoBancarioListDepositoBancarioToAttach );
            }
            tbUsuario.setDepositoBancarioList( attachedDepositoBancarioList );
            List<TbFuncionario> attachedTbFuncionarioList = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListTbFuncionarioToAttach : tbUsuario.getTbFuncionarioList() )
            {
                tbFuncionarioListTbFuncionarioToAttach = em.getReference( tbFuncionarioListTbFuncionarioToAttach.getClass(), tbFuncionarioListTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioList.add( tbFuncionarioListTbFuncionarioToAttach );
            }
            tbUsuario.setTbFuncionarioList( attachedTbFuncionarioList );
            List<TbDesconto> attachedTbDescontoList = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListTbDescontoToAttach : tbUsuario.getTbDescontoList() )
            {
                tbDescontoListTbDescontoToAttach = em.getReference( tbDescontoListTbDescontoToAttach.getClass(), tbDescontoListTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoList.add( tbDescontoListTbDescontoToAttach );
            }
            tbUsuario.setTbDescontoList( attachedTbDescontoList );
            List<TbOperacaoSistema> attachedTbOperacaoSistemaList = new ArrayList<TbOperacaoSistema>();
            for ( TbOperacaoSistema tbOperacaoSistemaListTbOperacaoSistemaToAttach : tbUsuario.getTbOperacaoSistemaList() )
            {
                tbOperacaoSistemaListTbOperacaoSistemaToAttach = em.getReference( tbOperacaoSistemaListTbOperacaoSistemaToAttach.getClass(), tbOperacaoSistemaListTbOperacaoSistemaToAttach.getPkOperacaoSistema() );
                attachedTbOperacaoSistemaList.add( tbOperacaoSistemaListTbOperacaoSistemaToAttach );
            }
            tbUsuario.setTbOperacaoSistemaList( attachedTbOperacaoSistemaList );
            List<TbSaidasProdutos> attachedTbSaidasProdutosList = new ArrayList<TbSaidasProdutos>();
            for ( TbSaidasProdutos tbSaidasProdutosListTbSaidasProdutosToAttach : tbUsuario.getTbSaidasProdutosList() )
            {
                tbSaidasProdutosListTbSaidasProdutosToAttach = em.getReference( tbSaidasProdutosListTbSaidasProdutosToAttach.getClass(), tbSaidasProdutosListTbSaidasProdutosToAttach.getPkSaidasProdutos() );
                attachedTbSaidasProdutosList.add( tbSaidasProdutosListTbSaidasProdutosToAttach );
            }
            tbUsuario.setTbSaidasProdutosList( attachedTbSaidasProdutosList );
            List<TbPreco> attachedTbPrecoList = new ArrayList<TbPreco>();
            for ( TbPreco tbPrecoListTbPrecoToAttach : tbUsuario.getTbPrecoList() )
            {
                tbPrecoListTbPrecoToAttach = em.getReference( tbPrecoListTbPrecoToAttach.getClass(), tbPrecoListTbPrecoToAttach.getPkPreco() );
                attachedTbPrecoList.add( tbPrecoListTbPrecoToAttach );
            }
            tbUsuario.setTbPrecoList( attachedTbPrecoList );
            List<TbEntrada> attachedTbEntradaList = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListTbEntradaToAttach : tbUsuario.getTbEntradaList() )
            {
                tbEntradaListTbEntradaToAttach = em.getReference( tbEntradaListTbEntradaToAttach.getClass(), tbEntradaListTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaList.add( tbEntradaListTbEntradaToAttach );
            }
            tbUsuario.setTbEntradaList( attachedTbEntradaList );
            List<TbEntradaVasilhame> attachedTbEntradaVasilhameList = new ArrayList<TbEntradaVasilhame>();
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhameToAttach : tbUsuario.getTbEntradaVasilhameList() )
            {
                tbEntradaVasilhameListTbEntradaVasilhameToAttach = em.getReference( tbEntradaVasilhameListTbEntradaVasilhameToAttach.getClass(), tbEntradaVasilhameListTbEntradaVasilhameToAttach.getPkEntradaVasilhame() );
                attachedTbEntradaVasilhameList.add( tbEntradaVasilhameListTbEntradaVasilhameToAttach );
            }
            tbUsuario.setTbEntradaVasilhameList( attachedTbEntradaVasilhameList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : tbUsuario.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            tbUsuario.setTbVendaList( attachedTbVendaList );
            List<Promocao> attachedPromocaoList = new ArrayList<Promocao>();
            for ( Promocao promocaoListPromocaoToAttach : tbUsuario.getPromocaoList() )
            {
                promocaoListPromocaoToAttach = em.getReference( promocaoListPromocaoToAttach.getClass(), promocaoListPromocaoToAttach.getPkPromocao() );
                attachedPromocaoList.add( promocaoListPromocaoToAttach );
            }
            tbUsuario.setPromocaoList( attachedPromocaoList );
            List<EntradaTesouraria> attachedEntradaTesourariaList = new ArrayList<EntradaTesouraria>();
            for ( EntradaTesouraria entradaTesourariaListEntradaTesourariaToAttach : tbUsuario.getEntradaTesourariaList() )
            {
                entradaTesourariaListEntradaTesourariaToAttach = em.getReference( entradaTesourariaListEntradaTesourariaToAttach.getClass(), entradaTesourariaListEntradaTesourariaToAttach.getPkEntradaTesouraria() );
                attachedEntradaTesourariaList.add( entradaTesourariaListEntradaTesourariaToAttach );
            }
            tbUsuario.setEntradaTesourariaList( attachedEntradaTesourariaList );
            List<TbItemPermissao> attachedTbItemPermissaoList = new ArrayList<TbItemPermissao>();
            for ( TbItemPermissao tbItemPermissaoListTbItemPermissaoToAttach : tbUsuario.getTbItemPermissaoList() )
            {
                tbItemPermissaoListTbItemPermissaoToAttach = em.getReference( tbItemPermissaoListTbItemPermissaoToAttach.getClass(), tbItemPermissaoListTbItemPermissaoToAttach.getIdItemPermissao() );
                attachedTbItemPermissaoList.add( tbItemPermissaoListTbItemPermissaoToAttach );
            }
            tbUsuario.setTbItemPermissaoList( attachedTbItemPermissaoList );
            List<AccessoArmazem> attachedAccessoArmazemList = new ArrayList<AccessoArmazem>();
            for ( AccessoArmazem accessoArmazemListAccessoArmazemToAttach : tbUsuario.getAccessoArmazemList() )
            {
                accessoArmazemListAccessoArmazemToAttach = em.getReference( accessoArmazemListAccessoArmazemToAttach.getClass(), accessoArmazemListAccessoArmazemToAttach.getPkAccessoArmazem() );
                attachedAccessoArmazemList.add( accessoArmazemListAccessoArmazemToAttach );
            }
            tbUsuario.setAccessoArmazemList( attachedAccessoArmazemList );
            List<PrevioAviso> attachedPrevioAvisoList = new ArrayList<PrevioAviso>();
            for ( PrevioAviso previoAvisoListPrevioAvisoToAttach : tbUsuario.getPrevioAvisoList() )
            {
                previoAvisoListPrevioAvisoToAttach = em.getReference( previoAvisoListPrevioAvisoToAttach.getClass(), previoAvisoListPrevioAvisoToAttach.getPkPrevioAviso() );
                attachedPrevioAvisoList.add( previoAvisoListPrevioAvisoToAttach );
            }
            tbUsuario.setPrevioAvisoList( attachedPrevioAvisoList );
            List<TbSaidaVasilhame> attachedTbSaidaVasilhameList = new ArrayList<TbSaidaVasilhame>();
            for ( TbSaidaVasilhame tbSaidaVasilhameListTbSaidaVasilhameToAttach : tbUsuario.getTbSaidaVasilhameList() )
            {
                tbSaidaVasilhameListTbSaidaVasilhameToAttach = em.getReference( tbSaidaVasilhameListTbSaidaVasilhameToAttach.getClass(), tbSaidaVasilhameListTbSaidaVasilhameToAttach.getPkSaidaVasilhame() );
                attachedTbSaidaVasilhameList.add( tbSaidaVasilhameListTbSaidaVasilhameToAttach );
            }
            tbUsuario.setTbSaidaVasilhameList( attachedTbSaidaVasilhameList );
            List<AmortizacaoDivida> attachedAmortizacaoDividaList = new ArrayList<AmortizacaoDivida>();
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDividaToAttach : tbUsuario.getAmortizacaoDividaList() )
            {
                amortizacaoDividaListAmortizacaoDividaToAttach = em.getReference( amortizacaoDividaListAmortizacaoDividaToAttach.getClass(), amortizacaoDividaListAmortizacaoDividaToAttach.getPkAmortizacaoDivida() );
                attachedAmortizacaoDividaList.add( amortizacaoDividaListAmortizacaoDividaToAttach );
            }
            tbUsuario.setAmortizacaoDividaList( attachedAmortizacaoDividaList );
            em.persist( tbUsuario );
            if ( idStatus != null )
            {
                idStatus.getTbUsuarioList().add( tbUsuario );
                idStatus = em.merge( idStatus );
            }
            if ( idTipoUsuario != null )
            {
                idTipoUsuario.getTbUsuarioList().add( tbUsuario );
                idTipoUsuario = em.merge( idTipoUsuario );
            }
            if ( codigoSexo != null )
            {
                codigoSexo.getTbUsuarioList().add( tbUsuario );
                codigoSexo = em.merge( codigoSexo );
            }
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtra : tbUsuario.getItemSalarioExtraList() )
            {
                TbUsuario oldFkTbUsuarioOfItemSalarioExtraListItemSalarioExtra = itemSalarioExtraListItemSalarioExtra.getFkTbUsuario();
                itemSalarioExtraListItemSalarioExtra.setFkTbUsuario( tbUsuario );
                itemSalarioExtraListItemSalarioExtra = em.merge( itemSalarioExtraListItemSalarioExtra );
                if ( oldFkTbUsuarioOfItemSalarioExtraListItemSalarioExtra != null )
                {
                    oldFkTbUsuarioOfItemSalarioExtraListItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListItemSalarioExtra );
                    oldFkTbUsuarioOfItemSalarioExtraListItemSalarioExtra = em.merge( oldFkTbUsuarioOfItemSalarioExtraListItemSalarioExtra );
                }
            }
            for ( NotasCompras notasComprasListNotasCompras : tbUsuario.getNotasComprasList() )
            {
                TbUsuario oldCodigoUsuarioOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getCodigoUsuario();
                notasComprasListNotasCompras.setCodigoUsuario( tbUsuario );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldCodigoUsuarioOfNotasComprasListNotasCompras != null )
                {
                    oldCodigoUsuarioOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldCodigoUsuarioOfNotasComprasListNotasCompras = em.merge( oldCodigoUsuarioOfNotasComprasListNotasCompras );
                }
            }
            for ( Notas notasListNotas : tbUsuario.getNotasList() )
            {
                TbUsuario oldCodigoUsuarioOfNotasListNotas = notasListNotas.getCodigoUsuario();
                notasListNotas.setCodigoUsuario( tbUsuario );
                notasListNotas = em.merge( notasListNotas );
                if ( oldCodigoUsuarioOfNotasListNotas != null )
                {
                    oldCodigoUsuarioOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldCodigoUsuarioOfNotasListNotas = em.merge( oldCodigoUsuarioOfNotasListNotas );
                }
            }
            for ( Turno turnoListTurno : tbUsuario.getTurnoList() )
            {
                TbUsuario oldFkUsuarioOfTurnoListTurno = turnoListTurno.getFkUsuario();
                turnoListTurno.setFkUsuario( tbUsuario );
                turnoListTurno = em.merge( turnoListTurno );
                if ( oldFkUsuarioOfTurnoListTurno != null )
                {
                    oldFkUsuarioOfTurnoListTurno.getTurnoList().remove( turnoListTurno );
                    oldFkUsuarioOfTurnoListTurno = em.merge( oldFkUsuarioOfTurnoListTurno );
                }
            }
            for ( Compras comprasListCompras : tbUsuario.getComprasList() )
            {
                TbUsuario oldCodigoUsuarioOfComprasListCompras = comprasListCompras.getCodigoUsuario();
                comprasListCompras.setCodigoUsuario( tbUsuario );
                comprasListCompras = em.merge( comprasListCompras );
                if ( oldCodigoUsuarioOfComprasListCompras != null )
                {
                    oldCodigoUsuarioOfComprasListCompras.getComprasList().remove( comprasListCompras );
                    oldCodigoUsuarioOfComprasListCompras = em.merge( oldCodigoUsuarioOfComprasListCompras );
                }
            }
            for ( PedidoFeria pedidoFeriaListPedidoFeria : tbUsuario.getPedidoFeriaList() )
            {
                TbUsuario oldFkUsuarioOfPedidoFeriaListPedidoFeria = pedidoFeriaListPedidoFeria.getFkUsuario();
                pedidoFeriaListPedidoFeria.setFkUsuario( tbUsuario );
                pedidoFeriaListPedidoFeria = em.merge( pedidoFeriaListPedidoFeria );
                if ( oldFkUsuarioOfPedidoFeriaListPedidoFeria != null )
                {
                    oldFkUsuarioOfPedidoFeriaListPedidoFeria.getPedidoFeriaList().remove( pedidoFeriaListPedidoFeria );
                    oldFkUsuarioOfPedidoFeriaListPedidoFeria = em.merge( oldFkUsuarioOfPedidoFeriaListPedidoFeria );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancaria : tbUsuario.getTransferenciaBancariaList() )
            {
                TbUsuario oldFkUsuarioOfTransferenciaBancariaListTransferenciaBancaria = transferenciaBancariaListTransferenciaBancaria.getFkUsuario();
                transferenciaBancariaListTransferenciaBancaria.setFkUsuario( tbUsuario );
                transferenciaBancariaListTransferenciaBancaria = em.merge( transferenciaBancariaListTransferenciaBancaria );
                if ( oldFkUsuarioOfTransferenciaBancariaListTransferenciaBancaria != null )
                {
                    oldFkUsuarioOfTransferenciaBancariaListTransferenciaBancaria.getTransferenciaBancariaList().remove( transferenciaBancariaListTransferenciaBancaria );
                    oldFkUsuarioOfTransferenciaBancariaListTransferenciaBancaria = em.merge( oldFkUsuarioOfTransferenciaBancariaListTransferenciaBancaria );
                }
            }
            for ( TbEncomenda tbEncomendaListTbEncomenda : tbUsuario.getTbEncomendaList() )
            {
                TbUsuario oldIdUsuarioOfTbEncomendaListTbEncomenda = tbEncomendaListTbEncomenda.getIdUsuario();
                tbEncomendaListTbEncomenda.setIdUsuario( tbUsuario );
                tbEncomendaListTbEncomenda = em.merge( tbEncomendaListTbEncomenda );
                if ( oldIdUsuarioOfTbEncomendaListTbEncomenda != null )
                {
                    oldIdUsuarioOfTbEncomendaListTbEncomenda.getTbEncomendaList().remove( tbEncomendaListTbEncomenda );
                    oldIdUsuarioOfTbEncomendaListTbEncomenda = em.merge( oldIdUsuarioOfTbEncomendaListTbEncomenda );
                }
            }
            for ( TbEstorno tbEstornoListTbEstorno : tbUsuario.getTbEstornoList() )
            {
                TbUsuario oldFkUsuarioOfTbEstornoListTbEstorno = tbEstornoListTbEstorno.getFkUsuario();
                tbEstornoListTbEstorno.setFkUsuario( tbUsuario );
                tbEstornoListTbEstorno = em.merge( tbEstornoListTbEstorno );
                if ( oldFkUsuarioOfTbEstornoListTbEstorno != null )
                {
                    oldFkUsuarioOfTbEstornoListTbEstorno.getTbEstornoList().remove( tbEstornoListTbEstorno );
                    oldFkUsuarioOfTbEstornoListTbEstorno = em.merge( oldFkUsuarioOfTbEstornoListTbEstorno );
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancario : tbUsuario.getLevantamentoBancarioList() )
            {
                TbUsuario oldFkUsuarioOfLevantamentoBancarioListLevantamentoBancario = levantamentoBancarioListLevantamentoBancario.getFkUsuario();
                levantamentoBancarioListLevantamentoBancario.setFkUsuario( tbUsuario );
                levantamentoBancarioListLevantamentoBancario = em.merge( levantamentoBancarioListLevantamentoBancario );
                if ( oldFkUsuarioOfLevantamentoBancarioListLevantamentoBancario != null )
                {
                    oldFkUsuarioOfLevantamentoBancarioListLevantamentoBancario.getLevantamentoBancarioList().remove( levantamentoBancarioListLevantamentoBancario );
                    oldFkUsuarioOfLevantamentoBancarioListLevantamentoBancario = em.merge( oldFkUsuarioOfLevantamentoBancarioListLevantamentoBancario );
                }
            }
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal : tbUsuario.getPagamentoSubsidioFeriaNatalList() )
            {
                TbUsuario oldFkUsuarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.getFkUsuario();
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.setFkUsuario( tbUsuario );
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = em.merge( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                if ( oldFkUsuarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal != null )
                {
                    oldFkUsuarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                    oldFkUsuarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = em.merge( oldFkUsuarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                }
            }
            for ( SaidasTesouraria saidasTesourariaListSaidasTesouraria : tbUsuario.getSaidasTesourariaList() )
            {
                TbUsuario oldFkUsuarioOfSaidasTesourariaListSaidasTesouraria = saidasTesourariaListSaidasTesouraria.getFkUsuario();
                saidasTesourariaListSaidasTesouraria.setFkUsuario( tbUsuario );
                saidasTesourariaListSaidasTesouraria = em.merge( saidasTesourariaListSaidasTesouraria );
                if ( oldFkUsuarioOfSaidasTesourariaListSaidasTesouraria != null )
                {
                    oldFkUsuarioOfSaidasTesourariaListSaidasTesouraria.getSaidasTesourariaList().remove( saidasTesourariaListSaidasTesouraria );
                    oldFkUsuarioOfSaidasTesourariaListSaidasTesouraria = em.merge( oldFkUsuarioOfSaidasTesourariaListSaidasTesouraria );
                }
            }
            for ( TbAcerto tbAcertoListTbAcerto : tbUsuario.getTbAcertoList() )
            {
                TbUsuario oldIdUsuarioOfTbAcertoListTbAcerto = tbAcertoListTbAcerto.getIdUsuario();
                tbAcertoListTbAcerto.setIdUsuario( tbUsuario );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
                if ( oldIdUsuarioOfTbAcertoListTbAcerto != null )
                {
                    oldIdUsuarioOfTbAcertoListTbAcerto.getTbAcertoList().remove( tbAcertoListTbAcerto );
                    oldIdUsuarioOfTbAcertoListTbAcerto = em.merge( oldIdUsuarioOfTbAcertoListTbAcerto );
                }
            }
            for ( TbProForma tbProFormaListTbProForma : tbUsuario.getTbProFormaList() )
            {
                TbUsuario oldFkUsuarioOfTbProFormaListTbProForma = tbProFormaListTbProForma.getFkUsuario();
                tbProFormaListTbProForma.setFkUsuario( tbUsuario );
                tbProFormaListTbProForma = em.merge( tbProFormaListTbProForma );
                if ( oldFkUsuarioOfTbProFormaListTbProForma != null )
                {
                    oldFkUsuarioOfTbProFormaListTbProForma.getTbProFormaList().remove( tbProFormaListTbProForma );
                    oldFkUsuarioOfTbProFormaListTbProForma = em.merge( oldFkUsuarioOfTbProFormaListTbProForma );
                }
            }
            for ( FechoPeriodo fechoPeriodoListFechoPeriodo : tbUsuario.getFechoPeriodoList() )
            {
                TbUsuario oldFkUsuarioOfFechoPeriodoListFechoPeriodo = fechoPeriodoListFechoPeriodo.getFkUsuario();
                fechoPeriodoListFechoPeriodo.setFkUsuario( tbUsuario );
                fechoPeriodoListFechoPeriodo = em.merge( fechoPeriodoListFechoPeriodo );
                if ( oldFkUsuarioOfFechoPeriodoListFechoPeriodo != null )
                {
                    oldFkUsuarioOfFechoPeriodoListFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListFechoPeriodo );
                    oldFkUsuarioOfFechoPeriodoListFechoPeriodo = em.merge( oldFkUsuarioOfFechoPeriodoListFechoPeriodo );
                }
            }
            for ( Amortizacao amortizacaoListAmortizacao : tbUsuario.getAmortizacaoList() )
            {
                TbUsuario oldFkUsuarioOfAmortizacaoListAmortizacao = amortizacaoListAmortizacao.getFkUsuario();
                amortizacaoListAmortizacao.setFkUsuario( tbUsuario );
                amortizacaoListAmortizacao = em.merge( amortizacaoListAmortizacao );
                if ( oldFkUsuarioOfAmortizacaoListAmortizacao != null )
                {
                    oldFkUsuarioOfAmortizacaoListAmortizacao.getAmortizacaoList().remove( amortizacaoListAmortizacao );
                    oldFkUsuarioOfAmortizacaoListAmortizacao = em.merge( oldFkUsuarioOfAmortizacaoListAmortizacao );
                }
            }
            for ( Anexos anexosListAnexos : tbUsuario.getAnexosList() )
            {
                TbUsuario oldFkUsuarioOfAnexosListAnexos = anexosListAnexos.getFkUsuario();
                anexosListAnexos.setFkUsuario( tbUsuario );
                anexosListAnexos = em.merge( anexosListAnexos );
                if ( oldFkUsuarioOfAnexosListAnexos != null )
                {
                    oldFkUsuarioOfAnexosListAnexos.getAnexosList().remove( anexosListAnexos );
                    oldFkUsuarioOfAnexosListAnexos = em.merge( oldFkUsuarioOfAnexosListAnexos );
                }
            }
            for ( FechoContrato fechoContratoListFechoContrato : tbUsuario.getFechoContratoList() )
            {
                TbUsuario oldFkUsuarioOfFechoContratoListFechoContrato = fechoContratoListFechoContrato.getFkUsuario();
                fechoContratoListFechoContrato.setFkUsuario( tbUsuario );
                fechoContratoListFechoContrato = em.merge( fechoContratoListFechoContrato );
                if ( oldFkUsuarioOfFechoContratoListFechoContrato != null )
                {
                    oldFkUsuarioOfFechoContratoListFechoContrato.getFechoContratoList().remove( fechoContratoListFechoContrato );
                    oldFkUsuarioOfFechoContratoListFechoContrato = em.merge( oldFkUsuarioOfFechoContratoListFechoContrato );
                }
            }
            for ( DepositoBancario depositoBancarioListDepositoBancario : tbUsuario.getDepositoBancarioList() )
            {
                TbUsuario oldFkUsuarioOfDepositoBancarioListDepositoBancario = depositoBancarioListDepositoBancario.getFkUsuario();
                depositoBancarioListDepositoBancario.setFkUsuario( tbUsuario );
                depositoBancarioListDepositoBancario = em.merge( depositoBancarioListDepositoBancario );
                if ( oldFkUsuarioOfDepositoBancarioListDepositoBancario != null )
                {
                    oldFkUsuarioOfDepositoBancarioListDepositoBancario.getDepositoBancarioList().remove( depositoBancarioListDepositoBancario );
                    oldFkUsuarioOfDepositoBancarioListDepositoBancario = em.merge( oldFkUsuarioOfDepositoBancarioListDepositoBancario );
                }
            }
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbUsuario.getTbFuncionarioList() )
            {
                TbUsuario oldFkUsuarioOfTbFuncionarioListTbFuncionario = tbFuncionarioListTbFuncionario.getFkUsuario();
                tbFuncionarioListTbFuncionario.setFkUsuario( tbUsuario );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
                if ( oldFkUsuarioOfTbFuncionarioListTbFuncionario != null )
                {
                    oldFkUsuarioOfTbFuncionarioListTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListTbFuncionario );
                    oldFkUsuarioOfTbFuncionarioListTbFuncionario = em.merge( oldFkUsuarioOfTbFuncionarioListTbFuncionario );
                }
            }
            for ( TbDesconto tbDescontoListTbDesconto : tbUsuario.getTbDescontoList() )
            {
                TbUsuario oldFkUsuarioOfTbDescontoListTbDesconto = tbDescontoListTbDesconto.getFkUsuario();
                tbDescontoListTbDesconto.setFkUsuario( tbUsuario );
                tbDescontoListTbDesconto = em.merge( tbDescontoListTbDesconto );
                if ( oldFkUsuarioOfTbDescontoListTbDesconto != null )
                {
                    oldFkUsuarioOfTbDescontoListTbDesconto.getTbDescontoList().remove( tbDescontoListTbDesconto );
                    oldFkUsuarioOfTbDescontoListTbDesconto = em.merge( oldFkUsuarioOfTbDescontoListTbDesconto );
                }
            }
            for ( TbOperacaoSistema tbOperacaoSistemaListTbOperacaoSistema : tbUsuario.getTbOperacaoSistemaList() )
            {
                TbUsuario oldFkUsuarioOfTbOperacaoSistemaListTbOperacaoSistema = tbOperacaoSistemaListTbOperacaoSistema.getFkUsuario();
                tbOperacaoSistemaListTbOperacaoSistema.setFkUsuario( tbUsuario );
                tbOperacaoSistemaListTbOperacaoSistema = em.merge( tbOperacaoSistemaListTbOperacaoSistema );
                if ( oldFkUsuarioOfTbOperacaoSistemaListTbOperacaoSistema != null )
                {
                    oldFkUsuarioOfTbOperacaoSistemaListTbOperacaoSistema.getTbOperacaoSistemaList().remove( tbOperacaoSistemaListTbOperacaoSistema );
                    oldFkUsuarioOfTbOperacaoSistemaListTbOperacaoSistema = em.merge( oldFkUsuarioOfTbOperacaoSistemaListTbOperacaoSistema );
                }
            }
            for ( TbSaidasProdutos tbSaidasProdutosListTbSaidasProdutos : tbUsuario.getTbSaidasProdutosList() )
            {
                TbUsuario oldFkUsuarioOfTbSaidasProdutosListTbSaidasProdutos = tbSaidasProdutosListTbSaidasProdutos.getFkUsuario();
                tbSaidasProdutosListTbSaidasProdutos.setFkUsuario( tbUsuario );
                tbSaidasProdutosListTbSaidasProdutos = em.merge( tbSaidasProdutosListTbSaidasProdutos );
                if ( oldFkUsuarioOfTbSaidasProdutosListTbSaidasProdutos != null )
                {
                    oldFkUsuarioOfTbSaidasProdutosListTbSaidasProdutos.getTbSaidasProdutosList().remove( tbSaidasProdutosListTbSaidasProdutos );
                    oldFkUsuarioOfTbSaidasProdutosListTbSaidasProdutos = em.merge( oldFkUsuarioOfTbSaidasProdutosListTbSaidasProdutos );
                }
            }
            for ( TbPreco tbPrecoListTbPreco : tbUsuario.getTbPrecoList() )
            {
                TbUsuario oldFkUsuarioOfTbPrecoListTbPreco = tbPrecoListTbPreco.getFkUsuario();
                tbPrecoListTbPreco.setFkUsuario( tbUsuario );
                tbPrecoListTbPreco = em.merge( tbPrecoListTbPreco );
                if ( oldFkUsuarioOfTbPrecoListTbPreco != null )
                {
                    oldFkUsuarioOfTbPrecoListTbPreco.getTbPrecoList().remove( tbPrecoListTbPreco );
                    oldFkUsuarioOfTbPrecoListTbPreco = em.merge( oldFkUsuarioOfTbPrecoListTbPreco );
                }
            }
            for ( TbEntrada tbEntradaListTbEntrada : tbUsuario.getTbEntradaList() )
            {
                TbUsuario oldIdUsuarioOfTbEntradaListTbEntrada = tbEntradaListTbEntrada.getIdUsuario();
                tbEntradaListTbEntrada.setIdUsuario( tbUsuario );
                tbEntradaListTbEntrada = em.merge( tbEntradaListTbEntrada );
                if ( oldIdUsuarioOfTbEntradaListTbEntrada != null )
                {
                    oldIdUsuarioOfTbEntradaListTbEntrada.getTbEntradaList().remove( tbEntradaListTbEntrada );
                    oldIdUsuarioOfTbEntradaListTbEntrada = em.merge( oldIdUsuarioOfTbEntradaListTbEntrada );
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhame : tbUsuario.getTbEntradaVasilhameList() )
            {
                TbUsuario oldFkUsuarioOfTbEntradaVasilhameListTbEntradaVasilhame = tbEntradaVasilhameListTbEntradaVasilhame.getFkUsuario();
                tbEntradaVasilhameListTbEntradaVasilhame.setFkUsuario( tbUsuario );
                tbEntradaVasilhameListTbEntradaVasilhame = em.merge( tbEntradaVasilhameListTbEntradaVasilhame );
                if ( oldFkUsuarioOfTbEntradaVasilhameListTbEntradaVasilhame != null )
                {
                    oldFkUsuarioOfTbEntradaVasilhameListTbEntradaVasilhame.getTbEntradaVasilhameList().remove( tbEntradaVasilhameListTbEntradaVasilhame );
                    oldFkUsuarioOfTbEntradaVasilhameListTbEntradaVasilhame = em.merge( oldFkUsuarioOfTbEntradaVasilhameListTbEntradaVasilhame );
                }
            }
            for ( TbVenda tbVendaListTbVenda : tbUsuario.getTbVendaList() )
            {
                TbUsuario oldCodigoUsuarioOfTbVendaListTbVenda = tbVendaListTbVenda.getCodigoUsuario();
                tbVendaListTbVenda.setCodigoUsuario( tbUsuario );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldCodigoUsuarioOfTbVendaListTbVenda != null )
                {
                    oldCodigoUsuarioOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldCodigoUsuarioOfTbVendaListTbVenda = em.merge( oldCodigoUsuarioOfTbVendaListTbVenda );
                }
            }
            for ( Promocao promocaoListPromocao : tbUsuario.getPromocaoList() )
            {
                TbUsuario oldFkUsuarioOfPromocaoListPromocao = promocaoListPromocao.getFkUsuario();
                promocaoListPromocao.setFkUsuario( tbUsuario );
                promocaoListPromocao = em.merge( promocaoListPromocao );
                if ( oldFkUsuarioOfPromocaoListPromocao != null )
                {
                    oldFkUsuarioOfPromocaoListPromocao.getPromocaoList().remove( promocaoListPromocao );
                    oldFkUsuarioOfPromocaoListPromocao = em.merge( oldFkUsuarioOfPromocaoListPromocao );
                }
            }
            for ( EntradaTesouraria entradaTesourariaListEntradaTesouraria : tbUsuario.getEntradaTesourariaList() )
            {
                TbUsuario oldFkUsuarioOfEntradaTesourariaListEntradaTesouraria = entradaTesourariaListEntradaTesouraria.getFkUsuario();
                entradaTesourariaListEntradaTesouraria.setFkUsuario( tbUsuario );
                entradaTesourariaListEntradaTesouraria = em.merge( entradaTesourariaListEntradaTesouraria );
                if ( oldFkUsuarioOfEntradaTesourariaListEntradaTesouraria != null )
                {
                    oldFkUsuarioOfEntradaTesourariaListEntradaTesouraria.getEntradaTesourariaList().remove( entradaTesourariaListEntradaTesouraria );
                    oldFkUsuarioOfEntradaTesourariaListEntradaTesouraria = em.merge( oldFkUsuarioOfEntradaTesourariaListEntradaTesouraria );
                }
            }
            for ( TbItemPermissao tbItemPermissaoListTbItemPermissao : tbUsuario.getTbItemPermissaoList() )
            {
                TbUsuario oldIdUsuarioOfTbItemPermissaoListTbItemPermissao = tbItemPermissaoListTbItemPermissao.getIdUsuario();
                tbItemPermissaoListTbItemPermissao.setIdUsuario( tbUsuario );
                tbItemPermissaoListTbItemPermissao = em.merge( tbItemPermissaoListTbItemPermissao );
                if ( oldIdUsuarioOfTbItemPermissaoListTbItemPermissao != null )
                {
                    oldIdUsuarioOfTbItemPermissaoListTbItemPermissao.getTbItemPermissaoList().remove( tbItemPermissaoListTbItemPermissao );
                    oldIdUsuarioOfTbItemPermissaoListTbItemPermissao = em.merge( oldIdUsuarioOfTbItemPermissaoListTbItemPermissao );
                }
            }
            for ( AccessoArmazem accessoArmazemListAccessoArmazem : tbUsuario.getAccessoArmazemList() )
            {
                TbUsuario oldFkUsuarioOfAccessoArmazemListAccessoArmazem = accessoArmazemListAccessoArmazem.getFkUsuario();
                accessoArmazemListAccessoArmazem.setFkUsuario( tbUsuario );
                accessoArmazemListAccessoArmazem = em.merge( accessoArmazemListAccessoArmazem );
                if ( oldFkUsuarioOfAccessoArmazemListAccessoArmazem != null )
                {
                    oldFkUsuarioOfAccessoArmazemListAccessoArmazem.getAccessoArmazemList().remove( accessoArmazemListAccessoArmazem );
                    oldFkUsuarioOfAccessoArmazemListAccessoArmazem = em.merge( oldFkUsuarioOfAccessoArmazemListAccessoArmazem );
                }
            }
            for ( PrevioAviso previoAvisoListPrevioAviso : tbUsuario.getPrevioAvisoList() )
            {
                TbUsuario oldFkUsuarioOfPrevioAvisoListPrevioAviso = previoAvisoListPrevioAviso.getFkUsuario();
                previoAvisoListPrevioAviso.setFkUsuario( tbUsuario );
                previoAvisoListPrevioAviso = em.merge( previoAvisoListPrevioAviso );
                if ( oldFkUsuarioOfPrevioAvisoListPrevioAviso != null )
                {
                    oldFkUsuarioOfPrevioAvisoListPrevioAviso.getPrevioAvisoList().remove( previoAvisoListPrevioAviso );
                    oldFkUsuarioOfPrevioAvisoListPrevioAviso = em.merge( oldFkUsuarioOfPrevioAvisoListPrevioAviso );
                }
            }
            for ( TbSaidaVasilhame tbSaidaVasilhameListTbSaidaVasilhame : tbUsuario.getTbSaidaVasilhameList() )
            {
                TbUsuario oldFkUsuarioOfTbSaidaVasilhameListTbSaidaVasilhame = tbSaidaVasilhameListTbSaidaVasilhame.getFkUsuario();
                tbSaidaVasilhameListTbSaidaVasilhame.setFkUsuario( tbUsuario );
                tbSaidaVasilhameListTbSaidaVasilhame = em.merge( tbSaidaVasilhameListTbSaidaVasilhame );
                if ( oldFkUsuarioOfTbSaidaVasilhameListTbSaidaVasilhame != null )
                {
                    oldFkUsuarioOfTbSaidaVasilhameListTbSaidaVasilhame.getTbSaidaVasilhameList().remove( tbSaidaVasilhameListTbSaidaVasilhame );
                    oldFkUsuarioOfTbSaidaVasilhameListTbSaidaVasilhame = em.merge( oldFkUsuarioOfTbSaidaVasilhameListTbSaidaVasilhame );
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDivida : tbUsuario.getAmortizacaoDividaList() )
            {
                TbUsuario oldFkUsuarioOfAmortizacaoDividaListAmortizacaoDivida = amortizacaoDividaListAmortizacaoDivida.getFkUsuario();
                amortizacaoDividaListAmortizacaoDivida.setFkUsuario( tbUsuario );
                amortizacaoDividaListAmortizacaoDivida = em.merge( amortizacaoDividaListAmortizacaoDivida );
                if ( oldFkUsuarioOfAmortizacaoDividaListAmortizacaoDivida != null )
                {
                    oldFkUsuarioOfAmortizacaoDividaListAmortizacaoDivida.getAmortizacaoDividaList().remove( amortizacaoDividaListAmortizacaoDivida );
                    oldFkUsuarioOfAmortizacaoDividaListAmortizacaoDivida = em.merge( oldFkUsuarioOfAmortizacaoDividaListAmortizacaoDivida );
                }
            }
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void edit( TbUsuario tbUsuario ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario persistentTbUsuario = em.find( TbUsuario.class, tbUsuario.getCodigo() );
            TbStatus idStatusOld = persistentTbUsuario.getIdStatus();
            TbStatus idStatusNew = tbUsuario.getIdStatus();
            TbTipoUsuario idTipoUsuarioOld = persistentTbUsuario.getIdTipoUsuario();
            TbTipoUsuario idTipoUsuarioNew = tbUsuario.getIdTipoUsuario();
            TbSexo codigoSexoOld = persistentTbUsuario.getCodigoSexo();
            TbSexo codigoSexoNew = tbUsuario.getCodigoSexo();
            List<ItemSalarioExtra> itemSalarioExtraListOld = persistentTbUsuario.getItemSalarioExtraList();
            List<ItemSalarioExtra> itemSalarioExtraListNew = tbUsuario.getItemSalarioExtraList();
            List<NotasCompras> notasComprasListOld = persistentTbUsuario.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = tbUsuario.getNotasComprasList();
            List<Notas> notasListOld = persistentTbUsuario.getNotasList();
            List<Notas> notasListNew = tbUsuario.getNotasList();
            List<Turno> turnoListOld = persistentTbUsuario.getTurnoList();
            List<Turno> turnoListNew = tbUsuario.getTurnoList();
            List<Compras> comprasListOld = persistentTbUsuario.getComprasList();
            List<Compras> comprasListNew = tbUsuario.getComprasList();
            List<PedidoFeria> pedidoFeriaListOld = persistentTbUsuario.getPedidoFeriaList();
            List<PedidoFeria> pedidoFeriaListNew = tbUsuario.getPedidoFeriaList();
            List<TransferenciaBancaria> transferenciaBancariaListOld = persistentTbUsuario.getTransferenciaBancariaList();
            List<TransferenciaBancaria> transferenciaBancariaListNew = tbUsuario.getTransferenciaBancariaList();
            List<TbEncomenda> tbEncomendaListOld = persistentTbUsuario.getTbEncomendaList();
            List<TbEncomenda> tbEncomendaListNew = tbUsuario.getTbEncomendaList();
            List<TbEstorno> tbEstornoListOld = persistentTbUsuario.getTbEstornoList();
            List<TbEstorno> tbEstornoListNew = tbUsuario.getTbEstornoList();
            List<LevantamentoBancario> levantamentoBancarioListOld = persistentTbUsuario.getLevantamentoBancarioList();
            List<LevantamentoBancario> levantamentoBancarioListNew = tbUsuario.getLevantamentoBancarioList();
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListOld = persistentTbUsuario.getPagamentoSubsidioFeriaNatalList();
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListNew = tbUsuario.getPagamentoSubsidioFeriaNatalList();
            List<SaidasTesouraria> saidasTesourariaListOld = persistentTbUsuario.getSaidasTesourariaList();
            List<SaidasTesouraria> saidasTesourariaListNew = tbUsuario.getSaidasTesourariaList();
            List<TbAcerto> tbAcertoListOld = persistentTbUsuario.getTbAcertoList();
            List<TbAcerto> tbAcertoListNew = tbUsuario.getTbAcertoList();
            List<TbProForma> tbProFormaListOld = persistentTbUsuario.getTbProFormaList();
            List<TbProForma> tbProFormaListNew = tbUsuario.getTbProFormaList();
            List<FechoPeriodo> fechoPeriodoListOld = persistentTbUsuario.getFechoPeriodoList();
            List<FechoPeriodo> fechoPeriodoListNew = tbUsuario.getFechoPeriodoList();
            List<Amortizacao> amortizacaoListOld = persistentTbUsuario.getAmortizacaoList();
            List<Amortizacao> amortizacaoListNew = tbUsuario.getAmortizacaoList();
            List<Anexos> anexosListOld = persistentTbUsuario.getAnexosList();
            List<Anexos> anexosListNew = tbUsuario.getAnexosList();
            List<FechoContrato> fechoContratoListOld = persistentTbUsuario.getFechoContratoList();
            List<FechoContrato> fechoContratoListNew = tbUsuario.getFechoContratoList();
            List<DepositoBancario> depositoBancarioListOld = persistentTbUsuario.getDepositoBancarioList();
            List<DepositoBancario> depositoBancarioListNew = tbUsuario.getDepositoBancarioList();
            List<TbFuncionario> tbFuncionarioListOld = persistentTbUsuario.getTbFuncionarioList();
            List<TbFuncionario> tbFuncionarioListNew = tbUsuario.getTbFuncionarioList();
            List<TbDesconto> tbDescontoListOld = persistentTbUsuario.getTbDescontoList();
            List<TbDesconto> tbDescontoListNew = tbUsuario.getTbDescontoList();
            List<TbOperacaoSistema> tbOperacaoSistemaListOld = persistentTbUsuario.getTbOperacaoSistemaList();
            List<TbOperacaoSistema> tbOperacaoSistemaListNew = tbUsuario.getTbOperacaoSistemaList();
            List<TbSaidasProdutos> tbSaidasProdutosListOld = persistentTbUsuario.getTbSaidasProdutosList();
            List<TbSaidasProdutos> tbSaidasProdutosListNew = tbUsuario.getTbSaidasProdutosList();
            List<TbPreco> tbPrecoListOld = persistentTbUsuario.getTbPrecoList();
            List<TbPreco> tbPrecoListNew = tbUsuario.getTbPrecoList();
            List<TbEntrada> tbEntradaListOld = persistentTbUsuario.getTbEntradaList();
            List<TbEntrada> tbEntradaListNew = tbUsuario.getTbEntradaList();
            List<TbEntradaVasilhame> tbEntradaVasilhameListOld = persistentTbUsuario.getTbEntradaVasilhameList();
            List<TbEntradaVasilhame> tbEntradaVasilhameListNew = tbUsuario.getTbEntradaVasilhameList();
            List<TbVenda> tbVendaListOld = persistentTbUsuario.getTbVendaList();
            List<TbVenda> tbVendaListNew = tbUsuario.getTbVendaList();
            List<Promocao> promocaoListOld = persistentTbUsuario.getPromocaoList();
            List<Promocao> promocaoListNew = tbUsuario.getPromocaoList();
            List<EntradaTesouraria> entradaTesourariaListOld = persistentTbUsuario.getEntradaTesourariaList();
            List<EntradaTesouraria> entradaTesourariaListNew = tbUsuario.getEntradaTesourariaList();
            List<TbItemPermissao> tbItemPermissaoListOld = persistentTbUsuario.getTbItemPermissaoList();
            List<TbItemPermissao> tbItemPermissaoListNew = tbUsuario.getTbItemPermissaoList();
            List<AccessoArmazem> accessoArmazemListOld = persistentTbUsuario.getAccessoArmazemList();
            List<AccessoArmazem> accessoArmazemListNew = tbUsuario.getAccessoArmazemList();
            List<PrevioAviso> previoAvisoListOld = persistentTbUsuario.getPrevioAvisoList();
            List<PrevioAviso> previoAvisoListNew = tbUsuario.getPrevioAvisoList();
            List<TbSaidaVasilhame> tbSaidaVasilhameListOld = persistentTbUsuario.getTbSaidaVasilhameList();
            List<TbSaidaVasilhame> tbSaidaVasilhameListNew = tbUsuario.getTbSaidaVasilhameList();
            List<AmortizacaoDivida> amortizacaoDividaListOld = persistentTbUsuario.getAmortizacaoDividaList();
            List<AmortizacaoDivida> amortizacaoDividaListNew = tbUsuario.getAmortizacaoDividaList();
            List<String> illegalOrphanMessages = null;
            for ( ItemSalarioExtra itemSalarioExtraListOldItemSalarioExtra : itemSalarioExtraListOld )
            {
                if ( !itemSalarioExtraListNew.contains( itemSalarioExtraListOldItemSalarioExtra ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemSalarioExtra " + itemSalarioExtraListOldItemSalarioExtra + " since its fkTbUsuario field is not nullable." );
                }
            }
            for ( NotasCompras notasComprasListOldNotasCompras : notasComprasListOld )
            {
                if ( !notasComprasListNew.contains( notasComprasListOldNotasCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain NotasCompras " + notasComprasListOldNotasCompras + " since its codigoUsuario field is not nullable." );
                }
            }
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its codigoUsuario field is not nullable." );
                }
            }
            for ( Turno turnoListOldTurno : turnoListOld )
            {
                if ( !turnoListNew.contains( turnoListOldTurno ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Turno " + turnoListOldTurno + " since its fkUsuario field is not nullable." );
                }
            }
            for ( PedidoFeria pedidoFeriaListOldPedidoFeria : pedidoFeriaListOld )
            {
                if ( !pedidoFeriaListNew.contains( pedidoFeriaListOldPedidoFeria ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain PedidoFeria " + pedidoFeriaListOldPedidoFeria + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbEncomenda tbEncomendaListOldTbEncomenda : tbEncomendaListOld )
            {
                if ( !tbEncomendaListNew.contains( tbEncomendaListOldTbEncomenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbEncomenda " + tbEncomendaListOldTbEncomenda + " since its idUsuario field is not nullable." );
                }
            }
            for ( TbEstorno tbEstornoListOldTbEstorno : tbEstornoListOld )
            {
                if ( !tbEstornoListNew.contains( tbEstornoListOldTbEstorno ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbEstorno " + tbEstornoListOldTbEstorno + " since its fkUsuario field is not nullable." );
                }
            }
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListOldPagamentoSubsidioFeriaNatal : pagamentoSubsidioFeriaNatalListOld )
            {
                if ( !pagamentoSubsidioFeriaNatalListNew.contains( pagamentoSubsidioFeriaNatalListOldPagamentoSubsidioFeriaNatal ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain PagamentoSubsidioFeriaNatal " + pagamentoSubsidioFeriaNatalListOldPagamentoSubsidioFeriaNatal + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbProForma tbProFormaListOldTbProForma : tbProFormaListOld )
            {
                if ( !tbProFormaListNew.contains( tbProFormaListOldTbProForma ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbProForma " + tbProFormaListOldTbProForma + " since its fkUsuario field is not nullable." );
                }
            }
            for ( FechoPeriodo fechoPeriodoListOldFechoPeriodo : fechoPeriodoListOld )
            {
                if ( !fechoPeriodoListNew.contains( fechoPeriodoListOldFechoPeriodo ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain FechoPeriodo " + fechoPeriodoListOldFechoPeriodo + " since its fkUsuario field is not nullable." );
                }
            }
            for ( Amortizacao amortizacaoListOldAmortizacao : amortizacaoListOld )
            {
                if ( !amortizacaoListNew.contains( amortizacaoListOldAmortizacao ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Amortizacao " + amortizacaoListOldAmortizacao + " since its fkUsuario field is not nullable." );
                }
            }
            for ( Anexos anexosListOldAnexos : anexosListOld )
            {
                if ( !anexosListNew.contains( anexosListOldAnexos ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Anexos " + anexosListOldAnexos + " since its fkUsuario field is not nullable." );
                }
            }
            for ( FechoContrato fechoContratoListOldFechoContrato : fechoContratoListOld )
            {
                if ( !fechoContratoListNew.contains( fechoContratoListOldFechoContrato ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain FechoContrato " + fechoContratoListOldFechoContrato + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbDesconto tbDescontoListOldTbDesconto : tbDescontoListOld )
            {
                if ( !tbDescontoListNew.contains( tbDescontoListOldTbDesconto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbDesconto " + tbDescontoListOldTbDesconto + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbSaidasProdutos tbSaidasProdutosListOldTbSaidasProdutos : tbSaidasProdutosListOld )
            {
                if ( !tbSaidasProdutosListNew.contains( tbSaidasProdutosListOldTbSaidasProdutos ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbSaidasProdutos " + tbSaidasProdutosListOldTbSaidasProdutos + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbPreco tbPrecoListOldTbPreco : tbPrecoListOld )
            {
                if ( !tbPrecoListNew.contains( tbPrecoListOldTbPreco ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbPreco " + tbPrecoListOldTbPreco + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbEntrada tbEntradaListOldTbEntrada : tbEntradaListOld )
            {
                if ( !tbEntradaListNew.contains( tbEntradaListOldTbEntrada ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbEntrada " + tbEntradaListOldTbEntrada + " since its idUsuario field is not nullable." );
                }
            }
            for ( TbVenda tbVendaListOldTbVenda : tbVendaListOld )
            {
                if ( !tbVendaListNew.contains( tbVendaListOldTbVenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its codigoUsuario field is not nullable." );
                }
            }
            for ( Promocao promocaoListOldPromocao : promocaoListOld )
            {
                if ( !promocaoListNew.contains( promocaoListOldPromocao ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Promocao " + promocaoListOldPromocao + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbItemPermissao tbItemPermissaoListOldTbItemPermissao : tbItemPermissaoListOld )
            {
                if ( !tbItemPermissaoListNew.contains( tbItemPermissaoListOldTbItemPermissao ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemPermissao " + tbItemPermissaoListOldTbItemPermissao + " since its idUsuario field is not nullable." );
                }
            }
            for ( AccessoArmazem accessoArmazemListOldAccessoArmazem : accessoArmazemListOld )
            {
                if ( !accessoArmazemListNew.contains( accessoArmazemListOldAccessoArmazem ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain AccessoArmazem " + accessoArmazemListOldAccessoArmazem + " since its fkUsuario field is not nullable." );
                }
            }
            for ( PrevioAviso previoAvisoListOldPrevioAviso : previoAvisoListOld )
            {
                if ( !previoAvisoListNew.contains( previoAvisoListOldPrevioAviso ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain PrevioAviso " + previoAvisoListOldPrevioAviso + " since its fkUsuario field is not nullable." );
                }
            }
            for ( TbSaidaVasilhame tbSaidaVasilhameListOldTbSaidaVasilhame : tbSaidaVasilhameListOld )
            {
                if ( !tbSaidaVasilhameListNew.contains( tbSaidaVasilhameListOldTbSaidaVasilhame ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbSaidaVasilhame " + tbSaidaVasilhameListOldTbSaidaVasilhame + " since its fkUsuario field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idStatusNew != null )
            {
                idStatusNew = em.getReference( idStatusNew.getClass(), idStatusNew.getIdStatus() );
                tbUsuario.setIdStatus( idStatusNew );
            }
            if ( idTipoUsuarioNew != null )
            {
                idTipoUsuarioNew = em.getReference( idTipoUsuarioNew.getClass(), idTipoUsuarioNew.getIdTipoUsuario() );
                tbUsuario.setIdTipoUsuario( idTipoUsuarioNew );
            }
            if ( codigoSexoNew != null )
            {
                codigoSexoNew = em.getReference( codigoSexoNew.getClass(), codigoSexoNew.getCodigo() );
                tbUsuario.setCodigoSexo( codigoSexoNew );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraListNew = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtraToAttach : itemSalarioExtraListNew )
            {
                itemSalarioExtraListNewItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListNewItemSalarioExtraToAttach.getClass(), itemSalarioExtraListNewItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraListNew.add( itemSalarioExtraListNewItemSalarioExtraToAttach );
            }
            itemSalarioExtraListNew = attachedItemSalarioExtraListNew;
            tbUsuario.setItemSalarioExtraList( itemSalarioExtraListNew );
            List<NotasCompras> attachedNotasComprasListNew = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNewNotasComprasToAttach : notasComprasListNew )
            {
                notasComprasListNewNotasComprasToAttach = em.getReference( notasComprasListNewNotasComprasToAttach.getClass(), notasComprasListNewNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasListNew.add( notasComprasListNewNotasComprasToAttach );
            }
            notasComprasListNew = attachedNotasComprasListNew;
            tbUsuario.setNotasComprasList( notasComprasListNew );
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            tbUsuario.setNotasList( notasListNew );
            List<Turno> attachedTurnoListNew = new ArrayList<Turno>();
            for ( Turno turnoListNewTurnoToAttach : turnoListNew )
            {
                turnoListNewTurnoToAttach = em.getReference( turnoListNewTurnoToAttach.getClass(), turnoListNewTurnoToAttach.getPkTurno() );
                attachedTurnoListNew.add( turnoListNewTurnoToAttach );
            }
            turnoListNew = attachedTurnoListNew;
            tbUsuario.setTurnoList( turnoListNew );
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for ( Compras comprasListNewComprasToAttach : comprasListNew )
            {
                comprasListNewComprasToAttach = em.getReference( comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getPkCompra() );
                attachedComprasListNew.add( comprasListNewComprasToAttach );
            }
            comprasListNew = attachedComprasListNew;
            tbUsuario.setComprasList( comprasListNew );
            List<PedidoFeria> attachedPedidoFeriaListNew = new ArrayList<PedidoFeria>();
            for ( PedidoFeria pedidoFeriaListNewPedidoFeriaToAttach : pedidoFeriaListNew )
            {
                pedidoFeriaListNewPedidoFeriaToAttach = em.getReference( pedidoFeriaListNewPedidoFeriaToAttach.getClass(), pedidoFeriaListNewPedidoFeriaToAttach.getPkPedidoFeria() );
                attachedPedidoFeriaListNew.add( pedidoFeriaListNewPedidoFeriaToAttach );
            }
            pedidoFeriaListNew = attachedPedidoFeriaListNew;
            tbUsuario.setPedidoFeriaList( pedidoFeriaListNew );
            List<TransferenciaBancaria> attachedTransferenciaBancariaListNew = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaListNewTransferenciaBancariaToAttach : transferenciaBancariaListNew )
            {
                transferenciaBancariaListNewTransferenciaBancariaToAttach = em.getReference( transferenciaBancariaListNewTransferenciaBancariaToAttach.getClass(), transferenciaBancariaListNewTransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaListNew.add( transferenciaBancariaListNewTransferenciaBancariaToAttach );
            }
            transferenciaBancariaListNew = attachedTransferenciaBancariaListNew;
            tbUsuario.setTransferenciaBancariaList( transferenciaBancariaListNew );
            List<TbEncomenda> attachedTbEncomendaListNew = new ArrayList<TbEncomenda>();
            for ( TbEncomenda tbEncomendaListNewTbEncomendaToAttach : tbEncomendaListNew )
            {
                tbEncomendaListNewTbEncomendaToAttach = em.getReference( tbEncomendaListNewTbEncomendaToAttach.getClass(), tbEncomendaListNewTbEncomendaToAttach.getIdEncomenda() );
                attachedTbEncomendaListNew.add( tbEncomendaListNewTbEncomendaToAttach );
            }
            tbEncomendaListNew = attachedTbEncomendaListNew;
            tbUsuario.setTbEncomendaList( tbEncomendaListNew );
            List<TbEstorno> attachedTbEstornoListNew = new ArrayList<TbEstorno>();
            for ( TbEstorno tbEstornoListNewTbEstornoToAttach : tbEstornoListNew )
            {
                tbEstornoListNewTbEstornoToAttach = em.getReference( tbEstornoListNewTbEstornoToAttach.getClass(), tbEstornoListNewTbEstornoToAttach.getPkEstorno() );
                attachedTbEstornoListNew.add( tbEstornoListNewTbEstornoToAttach );
            }
            tbEstornoListNew = attachedTbEstornoListNew;
            tbUsuario.setTbEstornoList( tbEstornoListNew );
            List<LevantamentoBancario> attachedLevantamentoBancarioListNew = new ArrayList<LevantamentoBancario>();
            for ( LevantamentoBancario levantamentoBancarioListNewLevantamentoBancarioToAttach : levantamentoBancarioListNew )
            {
                levantamentoBancarioListNewLevantamentoBancarioToAttach = em.getReference( levantamentoBancarioListNewLevantamentoBancarioToAttach.getClass(), levantamentoBancarioListNewLevantamentoBancarioToAttach.getPkLevantamento() );
                attachedLevantamentoBancarioListNew.add( levantamentoBancarioListNewLevantamentoBancarioToAttach );
            }
            levantamentoBancarioListNew = attachedLevantamentoBancarioListNew;
            tbUsuario.setLevantamentoBancarioList( levantamentoBancarioListNew );
            List<PagamentoSubsidioFeriaNatal> attachedPagamentoSubsidioFeriaNatalListNew = new ArrayList<PagamentoSubsidioFeriaNatal>();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach : pagamentoSubsidioFeriaNatalListNew )
            {
                pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach = em.getReference( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach.getClass(), pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach.getPkPagamentoSubsidioFeriaNatal() );
                attachedPagamentoSubsidioFeriaNatalListNew.add( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach );
            }
            pagamentoSubsidioFeriaNatalListNew = attachedPagamentoSubsidioFeriaNatalListNew;
            tbUsuario.setPagamentoSubsidioFeriaNatalList( pagamentoSubsidioFeriaNatalListNew );
            List<SaidasTesouraria> attachedSaidasTesourariaListNew = new ArrayList<SaidasTesouraria>();
            for ( SaidasTesouraria saidasTesourariaListNewSaidasTesourariaToAttach : saidasTesourariaListNew )
            {
                saidasTesourariaListNewSaidasTesourariaToAttach = em.getReference( saidasTesourariaListNewSaidasTesourariaToAttach.getClass(), saidasTesourariaListNewSaidasTesourariaToAttach.getPkSaidasTesouraria() );
                attachedSaidasTesourariaListNew.add( saidasTesourariaListNewSaidasTesourariaToAttach );
            }
            saidasTesourariaListNew = attachedSaidasTesourariaListNew;
            tbUsuario.setSaidasTesourariaList( saidasTesourariaListNew );
            List<TbAcerto> attachedTbAcertoListNew = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListNewTbAcertoToAttach : tbAcertoListNew )
            {
                tbAcertoListNewTbAcertoToAttach = em.getReference( tbAcertoListNewTbAcertoToAttach.getClass(), tbAcertoListNewTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoListNew.add( tbAcertoListNewTbAcertoToAttach );
            }
            tbAcertoListNew = attachedTbAcertoListNew;
            tbUsuario.setTbAcertoList( tbAcertoListNew );
            List<TbProForma> attachedTbProFormaListNew = new ArrayList<TbProForma>();
            for ( TbProForma tbProFormaListNewTbProFormaToAttach : tbProFormaListNew )
            {
                tbProFormaListNewTbProFormaToAttach = em.getReference( tbProFormaListNewTbProFormaToAttach.getClass(), tbProFormaListNewTbProFormaToAttach.getPkProForma() );
                attachedTbProFormaListNew.add( tbProFormaListNewTbProFormaToAttach );
            }
            tbProFormaListNew = attachedTbProFormaListNew;
            tbUsuario.setTbProFormaList( tbProFormaListNew );
            List<FechoPeriodo> attachedFechoPeriodoListNew = new ArrayList<FechoPeriodo>();
            for ( FechoPeriodo fechoPeriodoListNewFechoPeriodoToAttach : fechoPeriodoListNew )
            {
                fechoPeriodoListNewFechoPeriodoToAttach = em.getReference( fechoPeriodoListNewFechoPeriodoToAttach.getClass(), fechoPeriodoListNewFechoPeriodoToAttach.getPkFechoPeriodo() );
                attachedFechoPeriodoListNew.add( fechoPeriodoListNewFechoPeriodoToAttach );
            }
            fechoPeriodoListNew = attachedFechoPeriodoListNew;
            tbUsuario.setFechoPeriodoList( fechoPeriodoListNew );
            List<Amortizacao> attachedAmortizacaoListNew = new ArrayList<Amortizacao>();
            for ( Amortizacao amortizacaoListNewAmortizacaoToAttach : amortizacaoListNew )
            {
                amortizacaoListNewAmortizacaoToAttach = em.getReference( amortizacaoListNewAmortizacaoToAttach.getClass(), amortizacaoListNewAmortizacaoToAttach.getPkAmortizacao() );
                attachedAmortizacaoListNew.add( amortizacaoListNewAmortizacaoToAttach );
            }
            amortizacaoListNew = attachedAmortizacaoListNew;
            tbUsuario.setAmortizacaoList( amortizacaoListNew );
            List<Anexos> attachedAnexosListNew = new ArrayList<Anexos>();
            for ( Anexos anexosListNewAnexosToAttach : anexosListNew )
            {
                anexosListNewAnexosToAttach = em.getReference( anexosListNewAnexosToAttach.getClass(), anexosListNewAnexosToAttach.getPkAnexos() );
                attachedAnexosListNew.add( anexosListNewAnexosToAttach );
            }
            anexosListNew = attachedAnexosListNew;
            tbUsuario.setAnexosList( anexosListNew );
            List<FechoContrato> attachedFechoContratoListNew = new ArrayList<FechoContrato>();
            for ( FechoContrato fechoContratoListNewFechoContratoToAttach : fechoContratoListNew )
            {
                fechoContratoListNewFechoContratoToAttach = em.getReference( fechoContratoListNewFechoContratoToAttach.getClass(), fechoContratoListNewFechoContratoToAttach.getPkFechoContrato() );
                attachedFechoContratoListNew.add( fechoContratoListNewFechoContratoToAttach );
            }
            fechoContratoListNew = attachedFechoContratoListNew;
            tbUsuario.setFechoContratoList( fechoContratoListNew );
            List<DepositoBancario> attachedDepositoBancarioListNew = new ArrayList<DepositoBancario>();
            for ( DepositoBancario depositoBancarioListNewDepositoBancarioToAttach : depositoBancarioListNew )
            {
                depositoBancarioListNewDepositoBancarioToAttach = em.getReference( depositoBancarioListNewDepositoBancarioToAttach.getClass(), depositoBancarioListNewDepositoBancarioToAttach.getPkDeposito() );
                attachedDepositoBancarioListNew.add( depositoBancarioListNewDepositoBancarioToAttach );
            }
            depositoBancarioListNew = attachedDepositoBancarioListNew;
            tbUsuario.setDepositoBancarioList( depositoBancarioListNew );
            List<TbFuncionario> attachedTbFuncionarioListNew = new ArrayList<TbFuncionario>();
            for ( TbFuncionario tbFuncionarioListNewTbFuncionarioToAttach : tbFuncionarioListNew )
            {
                tbFuncionarioListNewTbFuncionarioToAttach = em.getReference( tbFuncionarioListNewTbFuncionarioToAttach.getClass(), tbFuncionarioListNewTbFuncionarioToAttach.getIdFuncionario() );
                attachedTbFuncionarioListNew.add( tbFuncionarioListNewTbFuncionarioToAttach );
            }
            tbFuncionarioListNew = attachedTbFuncionarioListNew;
            tbUsuario.setTbFuncionarioList( tbFuncionarioListNew );
            List<TbDesconto> attachedTbDescontoListNew = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListNewTbDescontoToAttach : tbDescontoListNew )
            {
                tbDescontoListNewTbDescontoToAttach = em.getReference( tbDescontoListNewTbDescontoToAttach.getClass(), tbDescontoListNewTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoListNew.add( tbDescontoListNewTbDescontoToAttach );
            }
            tbDescontoListNew = attachedTbDescontoListNew;
            tbUsuario.setTbDescontoList( tbDescontoListNew );
            List<TbOperacaoSistema> attachedTbOperacaoSistemaListNew = new ArrayList<TbOperacaoSistema>();
            for ( TbOperacaoSistema tbOperacaoSistemaListNewTbOperacaoSistemaToAttach : tbOperacaoSistemaListNew )
            {
                tbOperacaoSistemaListNewTbOperacaoSistemaToAttach = em.getReference( tbOperacaoSistemaListNewTbOperacaoSistemaToAttach.getClass(), tbOperacaoSistemaListNewTbOperacaoSistemaToAttach.getPkOperacaoSistema() );
                attachedTbOperacaoSistemaListNew.add( tbOperacaoSistemaListNewTbOperacaoSistemaToAttach );
            }
            tbOperacaoSistemaListNew = attachedTbOperacaoSistemaListNew;
            tbUsuario.setTbOperacaoSistemaList( tbOperacaoSistemaListNew );
            List<TbSaidasProdutos> attachedTbSaidasProdutosListNew = new ArrayList<TbSaidasProdutos>();
            for ( TbSaidasProdutos tbSaidasProdutosListNewTbSaidasProdutosToAttach : tbSaidasProdutosListNew )
            {
                tbSaidasProdutosListNewTbSaidasProdutosToAttach = em.getReference( tbSaidasProdutosListNewTbSaidasProdutosToAttach.getClass(), tbSaidasProdutosListNewTbSaidasProdutosToAttach.getPkSaidasProdutos() );
                attachedTbSaidasProdutosListNew.add( tbSaidasProdutosListNewTbSaidasProdutosToAttach );
            }
            tbSaidasProdutosListNew = attachedTbSaidasProdutosListNew;
            tbUsuario.setTbSaidasProdutosList( tbSaidasProdutosListNew );
            List<TbPreco> attachedTbPrecoListNew = new ArrayList<TbPreco>();
            for ( TbPreco tbPrecoListNewTbPrecoToAttach : tbPrecoListNew )
            {
                tbPrecoListNewTbPrecoToAttach = em.getReference( tbPrecoListNewTbPrecoToAttach.getClass(), tbPrecoListNewTbPrecoToAttach.getPkPreco() );
                attachedTbPrecoListNew.add( tbPrecoListNewTbPrecoToAttach );
            }
            tbPrecoListNew = attachedTbPrecoListNew;
            tbUsuario.setTbPrecoList( tbPrecoListNew );
            List<TbEntrada> attachedTbEntradaListNew = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListNewTbEntradaToAttach : tbEntradaListNew )
            {
                tbEntradaListNewTbEntradaToAttach = em.getReference( tbEntradaListNewTbEntradaToAttach.getClass(), tbEntradaListNewTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaListNew.add( tbEntradaListNewTbEntradaToAttach );
            }
            tbEntradaListNew = attachedTbEntradaListNew;
            tbUsuario.setTbEntradaList( tbEntradaListNew );
            List<TbEntradaVasilhame> attachedTbEntradaVasilhameListNew = new ArrayList<TbEntradaVasilhame>();
            for ( TbEntradaVasilhame tbEntradaVasilhameListNewTbEntradaVasilhameToAttach : tbEntradaVasilhameListNew )
            {
                tbEntradaVasilhameListNewTbEntradaVasilhameToAttach = em.getReference( tbEntradaVasilhameListNewTbEntradaVasilhameToAttach.getClass(), tbEntradaVasilhameListNewTbEntradaVasilhameToAttach.getPkEntradaVasilhame() );
                attachedTbEntradaVasilhameListNew.add( tbEntradaVasilhameListNewTbEntradaVasilhameToAttach );
            }
            tbEntradaVasilhameListNew = attachedTbEntradaVasilhameListNew;
            tbUsuario.setTbEntradaVasilhameList( tbEntradaVasilhameListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            tbUsuario.setTbVendaList( tbVendaListNew );
            List<Promocao> attachedPromocaoListNew = new ArrayList<Promocao>();
            for ( Promocao promocaoListNewPromocaoToAttach : promocaoListNew )
            {
                promocaoListNewPromocaoToAttach = em.getReference( promocaoListNewPromocaoToAttach.getClass(), promocaoListNewPromocaoToAttach.getPkPromocao() );
                attachedPromocaoListNew.add( promocaoListNewPromocaoToAttach );
            }
            promocaoListNew = attachedPromocaoListNew;
            tbUsuario.setPromocaoList( promocaoListNew );
            List<EntradaTesouraria> attachedEntradaTesourariaListNew = new ArrayList<EntradaTesouraria>();
            for ( EntradaTesouraria entradaTesourariaListNewEntradaTesourariaToAttach : entradaTesourariaListNew )
            {
                entradaTesourariaListNewEntradaTesourariaToAttach = em.getReference( entradaTesourariaListNewEntradaTesourariaToAttach.getClass(), entradaTesourariaListNewEntradaTesourariaToAttach.getPkEntradaTesouraria() );
                attachedEntradaTesourariaListNew.add( entradaTesourariaListNewEntradaTesourariaToAttach );
            }
            entradaTesourariaListNew = attachedEntradaTesourariaListNew;
            tbUsuario.setEntradaTesourariaList( entradaTesourariaListNew );
            List<TbItemPermissao> attachedTbItemPermissaoListNew = new ArrayList<TbItemPermissao>();
            for ( TbItemPermissao tbItemPermissaoListNewTbItemPermissaoToAttach : tbItemPermissaoListNew )
            {
                tbItemPermissaoListNewTbItemPermissaoToAttach = em.getReference( tbItemPermissaoListNewTbItemPermissaoToAttach.getClass(), tbItemPermissaoListNewTbItemPermissaoToAttach.getIdItemPermissao() );
                attachedTbItemPermissaoListNew.add( tbItemPermissaoListNewTbItemPermissaoToAttach );
            }
            tbItemPermissaoListNew = attachedTbItemPermissaoListNew;
            tbUsuario.setTbItemPermissaoList( tbItemPermissaoListNew );
            List<AccessoArmazem> attachedAccessoArmazemListNew = new ArrayList<AccessoArmazem>();
            for ( AccessoArmazem accessoArmazemListNewAccessoArmazemToAttach : accessoArmazemListNew )
            {
                accessoArmazemListNewAccessoArmazemToAttach = em.getReference( accessoArmazemListNewAccessoArmazemToAttach.getClass(), accessoArmazemListNewAccessoArmazemToAttach.getPkAccessoArmazem() );
                attachedAccessoArmazemListNew.add( accessoArmazemListNewAccessoArmazemToAttach );
            }
            accessoArmazemListNew = attachedAccessoArmazemListNew;
            tbUsuario.setAccessoArmazemList( accessoArmazemListNew );
            List<PrevioAviso> attachedPrevioAvisoListNew = new ArrayList<PrevioAviso>();
            for ( PrevioAviso previoAvisoListNewPrevioAvisoToAttach : previoAvisoListNew )
            {
                previoAvisoListNewPrevioAvisoToAttach = em.getReference( previoAvisoListNewPrevioAvisoToAttach.getClass(), previoAvisoListNewPrevioAvisoToAttach.getPkPrevioAviso() );
                attachedPrevioAvisoListNew.add( previoAvisoListNewPrevioAvisoToAttach );
            }
            previoAvisoListNew = attachedPrevioAvisoListNew;
            tbUsuario.setPrevioAvisoList( previoAvisoListNew );
            List<TbSaidaVasilhame> attachedTbSaidaVasilhameListNew = new ArrayList<TbSaidaVasilhame>();
            for ( TbSaidaVasilhame tbSaidaVasilhameListNewTbSaidaVasilhameToAttach : tbSaidaVasilhameListNew )
            {
                tbSaidaVasilhameListNewTbSaidaVasilhameToAttach = em.getReference( tbSaidaVasilhameListNewTbSaidaVasilhameToAttach.getClass(), tbSaidaVasilhameListNewTbSaidaVasilhameToAttach.getPkSaidaVasilhame() );
                attachedTbSaidaVasilhameListNew.add( tbSaidaVasilhameListNewTbSaidaVasilhameToAttach );
            }
            tbSaidaVasilhameListNew = attachedTbSaidaVasilhameListNew;
            tbUsuario.setTbSaidaVasilhameList( tbSaidaVasilhameListNew );
            List<AmortizacaoDivida> attachedAmortizacaoDividaListNew = new ArrayList<AmortizacaoDivida>();
            for ( AmortizacaoDivida amortizacaoDividaListNewAmortizacaoDividaToAttach : amortizacaoDividaListNew )
            {
                amortizacaoDividaListNewAmortizacaoDividaToAttach = em.getReference( amortizacaoDividaListNewAmortizacaoDividaToAttach.getClass(), amortizacaoDividaListNewAmortizacaoDividaToAttach.getPkAmortizacaoDivida() );
                attachedAmortizacaoDividaListNew.add( amortizacaoDividaListNewAmortizacaoDividaToAttach );
            }
            amortizacaoDividaListNew = attachedAmortizacaoDividaListNew;
            tbUsuario.setAmortizacaoDividaList( amortizacaoDividaListNew );
            tbUsuario = em.merge( tbUsuario );
            if ( idStatusOld != null && !idStatusOld.equals( idStatusNew ) )
            {
                idStatusOld.getTbUsuarioList().remove( tbUsuario );
                idStatusOld = em.merge( idStatusOld );
            }
            if ( idStatusNew != null && !idStatusNew.equals( idStatusOld ) )
            {
                idStatusNew.getTbUsuarioList().add( tbUsuario );
                idStatusNew = em.merge( idStatusNew );
            }
            if ( idTipoUsuarioOld != null && !idTipoUsuarioOld.equals( idTipoUsuarioNew ) )
            {
                idTipoUsuarioOld.getTbUsuarioList().remove( tbUsuario );
                idTipoUsuarioOld = em.merge( idTipoUsuarioOld );
            }
            if ( idTipoUsuarioNew != null && !idTipoUsuarioNew.equals( idTipoUsuarioOld ) )
            {
                idTipoUsuarioNew.getTbUsuarioList().add( tbUsuario );
                idTipoUsuarioNew = em.merge( idTipoUsuarioNew );
            }
            if ( codigoSexoOld != null && !codigoSexoOld.equals( codigoSexoNew ) )
            {
                codigoSexoOld.getTbUsuarioList().remove( tbUsuario );
                codigoSexoOld = em.merge( codigoSexoOld );
            }
            if ( codigoSexoNew != null && !codigoSexoNew.equals( codigoSexoOld ) )
            {
                codigoSexoNew.getTbUsuarioList().add( tbUsuario );
                codigoSexoNew = em.merge( codigoSexoNew );
            }
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtra : itemSalarioExtraListNew )
            {
                if ( !itemSalarioExtraListOld.contains( itemSalarioExtraListNewItemSalarioExtra ) )
                {
                    TbUsuario oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra = itemSalarioExtraListNewItemSalarioExtra.getFkTbUsuario();
                    itemSalarioExtraListNewItemSalarioExtra.setFkTbUsuario( tbUsuario );
                    itemSalarioExtraListNewItemSalarioExtra = em.merge( itemSalarioExtraListNewItemSalarioExtra );
                    if ( oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra != null && !oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra.equals( tbUsuario ) )
                    {
                        oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListNewItemSalarioExtra );
                        oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra = em.merge( oldFkTbUsuarioOfItemSalarioExtraListNewItemSalarioExtra );
                    }
                }
            }
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    TbUsuario oldCodigoUsuarioOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getCodigoUsuario();
                    notasComprasListNewNotasCompras.setCodigoUsuario( tbUsuario );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldCodigoUsuarioOfNotasComprasListNewNotasCompras != null && !oldCodigoUsuarioOfNotasComprasListNewNotasCompras.equals( tbUsuario ) )
                    {
                        oldCodigoUsuarioOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldCodigoUsuarioOfNotasComprasListNewNotasCompras = em.merge( oldCodigoUsuarioOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    TbUsuario oldCodigoUsuarioOfNotasListNewNotas = notasListNewNotas.getCodigoUsuario();
                    notasListNewNotas.setCodigoUsuario( tbUsuario );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldCodigoUsuarioOfNotasListNewNotas != null && !oldCodigoUsuarioOfNotasListNewNotas.equals( tbUsuario ) )
                    {
                        oldCodigoUsuarioOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldCodigoUsuarioOfNotasListNewNotas = em.merge( oldCodigoUsuarioOfNotasListNewNotas );
                    }
                }
            }
            for ( Turno turnoListNewTurno : turnoListNew )
            {
                if ( !turnoListOld.contains( turnoListNewTurno ) )
                {
                    TbUsuario oldFkUsuarioOfTurnoListNewTurno = turnoListNewTurno.getFkUsuario();
                    turnoListNewTurno.setFkUsuario( tbUsuario );
                    turnoListNewTurno = em.merge( turnoListNewTurno );
                    if ( oldFkUsuarioOfTurnoListNewTurno != null && !oldFkUsuarioOfTurnoListNewTurno.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTurnoListNewTurno.getTurnoList().remove( turnoListNewTurno );
                        oldFkUsuarioOfTurnoListNewTurno = em.merge( oldFkUsuarioOfTurnoListNewTurno );
                    }
                }
            }
            for ( Compras comprasListOldCompras : comprasListOld )
            {
                if ( !comprasListNew.contains( comprasListOldCompras ) )
                {
                    comprasListOldCompras.setCodigoUsuario( null );
                    comprasListOldCompras = em.merge( comprasListOldCompras );
                }
            }
            for ( Compras comprasListNewCompras : comprasListNew )
            {
                if ( !comprasListOld.contains( comprasListNewCompras ) )
                {
                    TbUsuario oldCodigoUsuarioOfComprasListNewCompras = comprasListNewCompras.getCodigoUsuario();
                    comprasListNewCompras.setCodigoUsuario( tbUsuario );
                    comprasListNewCompras = em.merge( comprasListNewCompras );
                    if ( oldCodigoUsuarioOfComprasListNewCompras != null && !oldCodigoUsuarioOfComprasListNewCompras.equals( tbUsuario ) )
                    {
                        oldCodigoUsuarioOfComprasListNewCompras.getComprasList().remove( comprasListNewCompras );
                        oldCodigoUsuarioOfComprasListNewCompras = em.merge( oldCodigoUsuarioOfComprasListNewCompras );
                    }
                }
            }
            for ( PedidoFeria pedidoFeriaListNewPedidoFeria : pedidoFeriaListNew )
            {
                if ( !pedidoFeriaListOld.contains( pedidoFeriaListNewPedidoFeria ) )
                {
                    TbUsuario oldFkUsuarioOfPedidoFeriaListNewPedidoFeria = pedidoFeriaListNewPedidoFeria.getFkUsuario();
                    pedidoFeriaListNewPedidoFeria.setFkUsuario( tbUsuario );
                    pedidoFeriaListNewPedidoFeria = em.merge( pedidoFeriaListNewPedidoFeria );
                    if ( oldFkUsuarioOfPedidoFeriaListNewPedidoFeria != null && !oldFkUsuarioOfPedidoFeriaListNewPedidoFeria.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfPedidoFeriaListNewPedidoFeria.getPedidoFeriaList().remove( pedidoFeriaListNewPedidoFeria );
                        oldFkUsuarioOfPedidoFeriaListNewPedidoFeria = em.merge( oldFkUsuarioOfPedidoFeriaListNewPedidoFeria );
                    }
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListOldTransferenciaBancaria : transferenciaBancariaListOld )
            {
                if ( !transferenciaBancariaListNew.contains( transferenciaBancariaListOldTransferenciaBancaria ) )
                {
                    transferenciaBancariaListOldTransferenciaBancaria.setFkUsuario( null );
                    transferenciaBancariaListOldTransferenciaBancaria = em.merge( transferenciaBancariaListOldTransferenciaBancaria );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListNewTransferenciaBancaria : transferenciaBancariaListNew )
            {
                if ( !transferenciaBancariaListOld.contains( transferenciaBancariaListNewTransferenciaBancaria ) )
                {
                    TbUsuario oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria = transferenciaBancariaListNewTransferenciaBancaria.getFkUsuario();
                    transferenciaBancariaListNewTransferenciaBancaria.setFkUsuario( tbUsuario );
                    transferenciaBancariaListNewTransferenciaBancaria = em.merge( transferenciaBancariaListNewTransferenciaBancaria );
                    if ( oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria != null && !oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria.getTransferenciaBancariaList().remove( transferenciaBancariaListNewTransferenciaBancaria );
                        oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria = em.merge( oldFkUsuarioOfTransferenciaBancariaListNewTransferenciaBancaria );
                    }
                }
            }
            for ( TbEncomenda tbEncomendaListNewTbEncomenda : tbEncomendaListNew )
            {
                if ( !tbEncomendaListOld.contains( tbEncomendaListNewTbEncomenda ) )
                {
                    TbUsuario oldIdUsuarioOfTbEncomendaListNewTbEncomenda = tbEncomendaListNewTbEncomenda.getIdUsuario();
                    tbEncomendaListNewTbEncomenda.setIdUsuario( tbUsuario );
                    tbEncomendaListNewTbEncomenda = em.merge( tbEncomendaListNewTbEncomenda );
                    if ( oldIdUsuarioOfTbEncomendaListNewTbEncomenda != null && !oldIdUsuarioOfTbEncomendaListNewTbEncomenda.equals( tbUsuario ) )
                    {
                        oldIdUsuarioOfTbEncomendaListNewTbEncomenda.getTbEncomendaList().remove( tbEncomendaListNewTbEncomenda );
                        oldIdUsuarioOfTbEncomendaListNewTbEncomenda = em.merge( oldIdUsuarioOfTbEncomendaListNewTbEncomenda );
                    }
                }
            }
            for ( TbEstorno tbEstornoListNewTbEstorno : tbEstornoListNew )
            {
                if ( !tbEstornoListOld.contains( tbEstornoListNewTbEstorno ) )
                {
                    TbUsuario oldFkUsuarioOfTbEstornoListNewTbEstorno = tbEstornoListNewTbEstorno.getFkUsuario();
                    tbEstornoListNewTbEstorno.setFkUsuario( tbUsuario );
                    tbEstornoListNewTbEstorno = em.merge( tbEstornoListNewTbEstorno );
                    if ( oldFkUsuarioOfTbEstornoListNewTbEstorno != null && !oldFkUsuarioOfTbEstornoListNewTbEstorno.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbEstornoListNewTbEstorno.getTbEstornoList().remove( tbEstornoListNewTbEstorno );
                        oldFkUsuarioOfTbEstornoListNewTbEstorno = em.merge( oldFkUsuarioOfTbEstornoListNewTbEstorno );
                    }
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListOldLevantamentoBancario : levantamentoBancarioListOld )
            {
                if ( !levantamentoBancarioListNew.contains( levantamentoBancarioListOldLevantamentoBancario ) )
                {
                    levantamentoBancarioListOldLevantamentoBancario.setFkUsuario( null );
                    levantamentoBancarioListOldLevantamentoBancario = em.merge( levantamentoBancarioListOldLevantamentoBancario );
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListNewLevantamentoBancario : levantamentoBancarioListNew )
            {
                if ( !levantamentoBancarioListOld.contains( levantamentoBancarioListNewLevantamentoBancario ) )
                {
                    TbUsuario oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario = levantamentoBancarioListNewLevantamentoBancario.getFkUsuario();
                    levantamentoBancarioListNewLevantamentoBancario.setFkUsuario( tbUsuario );
                    levantamentoBancarioListNewLevantamentoBancario = em.merge( levantamentoBancarioListNewLevantamentoBancario );
                    if ( oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario != null && !oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario.getLevantamentoBancarioList().remove( levantamentoBancarioListNewLevantamentoBancario );
                        oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario = em.merge( oldFkUsuarioOfLevantamentoBancarioListNewLevantamentoBancario );
                    }
                }
            }
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal : pagamentoSubsidioFeriaNatalListNew )
            {
                if ( !pagamentoSubsidioFeriaNatalListOld.contains( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal ) )
                {
                    TbUsuario oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.getFkUsuario();
                    pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.setFkUsuario( tbUsuario );
                    pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = em.merge( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                    if ( oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal != null && !oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                        oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = em.merge( oldFkUsuarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                    }
                }
            }
            for ( SaidasTesouraria saidasTesourariaListOldSaidasTesouraria : saidasTesourariaListOld )
            {
                if ( !saidasTesourariaListNew.contains( saidasTesourariaListOldSaidasTesouraria ) )
                {
                    saidasTesourariaListOldSaidasTesouraria.setFkUsuario( null );
                    saidasTesourariaListOldSaidasTesouraria = em.merge( saidasTesourariaListOldSaidasTesouraria );
                }
            }
            for ( SaidasTesouraria saidasTesourariaListNewSaidasTesouraria : saidasTesourariaListNew )
            {
                if ( !saidasTesourariaListOld.contains( saidasTesourariaListNewSaidasTesouraria ) )
                {
                    TbUsuario oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria = saidasTesourariaListNewSaidasTesouraria.getFkUsuario();
                    saidasTesourariaListNewSaidasTesouraria.setFkUsuario( tbUsuario );
                    saidasTesourariaListNewSaidasTesouraria = em.merge( saidasTesourariaListNewSaidasTesouraria );
                    if ( oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria != null && !oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria.getSaidasTesourariaList().remove( saidasTesourariaListNewSaidasTesouraria );
                        oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria = em.merge( oldFkUsuarioOfSaidasTesourariaListNewSaidasTesouraria );
                    }
                }
            }
            for ( TbAcerto tbAcertoListOldTbAcerto : tbAcertoListOld )
            {
                if ( !tbAcertoListNew.contains( tbAcertoListOldTbAcerto ) )
                {
                    tbAcertoListOldTbAcerto.setIdUsuario( null );
                    tbAcertoListOldTbAcerto = em.merge( tbAcertoListOldTbAcerto );
                }
            }
            for ( TbAcerto tbAcertoListNewTbAcerto : tbAcertoListNew )
            {
                if ( !tbAcertoListOld.contains( tbAcertoListNewTbAcerto ) )
                {
                    TbUsuario oldIdUsuarioOfTbAcertoListNewTbAcerto = tbAcertoListNewTbAcerto.getIdUsuario();
                    tbAcertoListNewTbAcerto.setIdUsuario( tbUsuario );
                    tbAcertoListNewTbAcerto = em.merge( tbAcertoListNewTbAcerto );
                    if ( oldIdUsuarioOfTbAcertoListNewTbAcerto != null && !oldIdUsuarioOfTbAcertoListNewTbAcerto.equals( tbUsuario ) )
                    {
                        oldIdUsuarioOfTbAcertoListNewTbAcerto.getTbAcertoList().remove( tbAcertoListNewTbAcerto );
                        oldIdUsuarioOfTbAcertoListNewTbAcerto = em.merge( oldIdUsuarioOfTbAcertoListNewTbAcerto );
                    }
                }
            }
            for ( TbProForma tbProFormaListNewTbProForma : tbProFormaListNew )
            {
                if ( !tbProFormaListOld.contains( tbProFormaListNewTbProForma ) )
                {
                    TbUsuario oldFkUsuarioOfTbProFormaListNewTbProForma = tbProFormaListNewTbProForma.getFkUsuario();
                    tbProFormaListNewTbProForma.setFkUsuario( tbUsuario );
                    tbProFormaListNewTbProForma = em.merge( tbProFormaListNewTbProForma );
                    if ( oldFkUsuarioOfTbProFormaListNewTbProForma != null && !oldFkUsuarioOfTbProFormaListNewTbProForma.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbProFormaListNewTbProForma.getTbProFormaList().remove( tbProFormaListNewTbProForma );
                        oldFkUsuarioOfTbProFormaListNewTbProForma = em.merge( oldFkUsuarioOfTbProFormaListNewTbProForma );
                    }
                }
            }
            for ( FechoPeriodo fechoPeriodoListNewFechoPeriodo : fechoPeriodoListNew )
            {
                if ( !fechoPeriodoListOld.contains( fechoPeriodoListNewFechoPeriodo ) )
                {
                    TbUsuario oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo = fechoPeriodoListNewFechoPeriodo.getFkUsuario();
                    fechoPeriodoListNewFechoPeriodo.setFkUsuario( tbUsuario );
                    fechoPeriodoListNewFechoPeriodo = em.merge( fechoPeriodoListNewFechoPeriodo );
                    if ( oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo != null && !oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo.getFechoPeriodoList().remove( fechoPeriodoListNewFechoPeriodo );
                        oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo = em.merge( oldFkUsuarioOfFechoPeriodoListNewFechoPeriodo );
                    }
                }
            }
            for ( Amortizacao amortizacaoListNewAmortizacao : amortizacaoListNew )
            {
                if ( !amortizacaoListOld.contains( amortizacaoListNewAmortizacao ) )
                {
                    TbUsuario oldFkUsuarioOfAmortizacaoListNewAmortizacao = amortizacaoListNewAmortizacao.getFkUsuario();
                    amortizacaoListNewAmortizacao.setFkUsuario( tbUsuario );
                    amortizacaoListNewAmortizacao = em.merge( amortizacaoListNewAmortizacao );
                    if ( oldFkUsuarioOfAmortizacaoListNewAmortizacao != null && !oldFkUsuarioOfAmortizacaoListNewAmortizacao.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfAmortizacaoListNewAmortizacao.getAmortizacaoList().remove( amortizacaoListNewAmortizacao );
                        oldFkUsuarioOfAmortizacaoListNewAmortizacao = em.merge( oldFkUsuarioOfAmortizacaoListNewAmortizacao );
                    }
                }
            }
            for ( Anexos anexosListNewAnexos : anexosListNew )
            {
                if ( !anexosListOld.contains( anexosListNewAnexos ) )
                {
                    TbUsuario oldFkUsuarioOfAnexosListNewAnexos = anexosListNewAnexos.getFkUsuario();
                    anexosListNewAnexos.setFkUsuario( tbUsuario );
                    anexosListNewAnexos = em.merge( anexosListNewAnexos );
                    if ( oldFkUsuarioOfAnexosListNewAnexos != null && !oldFkUsuarioOfAnexosListNewAnexos.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfAnexosListNewAnexos.getAnexosList().remove( anexosListNewAnexos );
                        oldFkUsuarioOfAnexosListNewAnexos = em.merge( oldFkUsuarioOfAnexosListNewAnexos );
                    }
                }
            }
            for ( FechoContrato fechoContratoListNewFechoContrato : fechoContratoListNew )
            {
                if ( !fechoContratoListOld.contains( fechoContratoListNewFechoContrato ) )
                {
                    TbUsuario oldFkUsuarioOfFechoContratoListNewFechoContrato = fechoContratoListNewFechoContrato.getFkUsuario();
                    fechoContratoListNewFechoContrato.setFkUsuario( tbUsuario );
                    fechoContratoListNewFechoContrato = em.merge( fechoContratoListNewFechoContrato );
                    if ( oldFkUsuarioOfFechoContratoListNewFechoContrato != null && !oldFkUsuarioOfFechoContratoListNewFechoContrato.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfFechoContratoListNewFechoContrato.getFechoContratoList().remove( fechoContratoListNewFechoContrato );
                        oldFkUsuarioOfFechoContratoListNewFechoContrato = em.merge( oldFkUsuarioOfFechoContratoListNewFechoContrato );
                    }
                }
            }
            for ( DepositoBancario depositoBancarioListOldDepositoBancario : depositoBancarioListOld )
            {
                if ( !depositoBancarioListNew.contains( depositoBancarioListOldDepositoBancario ) )
                {
                    depositoBancarioListOldDepositoBancario.setFkUsuario( null );
                    depositoBancarioListOldDepositoBancario = em.merge( depositoBancarioListOldDepositoBancario );
                }
            }
            for ( DepositoBancario depositoBancarioListNewDepositoBancario : depositoBancarioListNew )
            {
                if ( !depositoBancarioListOld.contains( depositoBancarioListNewDepositoBancario ) )
                {
                    TbUsuario oldFkUsuarioOfDepositoBancarioListNewDepositoBancario = depositoBancarioListNewDepositoBancario.getFkUsuario();
                    depositoBancarioListNewDepositoBancario.setFkUsuario( tbUsuario );
                    depositoBancarioListNewDepositoBancario = em.merge( depositoBancarioListNewDepositoBancario );
                    if ( oldFkUsuarioOfDepositoBancarioListNewDepositoBancario != null && !oldFkUsuarioOfDepositoBancarioListNewDepositoBancario.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfDepositoBancarioListNewDepositoBancario.getDepositoBancarioList().remove( depositoBancarioListNewDepositoBancario );
                        oldFkUsuarioOfDepositoBancarioListNewDepositoBancario = em.merge( oldFkUsuarioOfDepositoBancarioListNewDepositoBancario );
                    }
                }
            }
            for ( TbFuncionario tbFuncionarioListOldTbFuncionario : tbFuncionarioListOld )
            {
                if ( !tbFuncionarioListNew.contains( tbFuncionarioListOldTbFuncionario ) )
                {
                    tbFuncionarioListOldTbFuncionario.setFkUsuario( null );
                    tbFuncionarioListOldTbFuncionario = em.merge( tbFuncionarioListOldTbFuncionario );
                }
            }
            for ( TbFuncionario tbFuncionarioListNewTbFuncionario : tbFuncionarioListNew )
            {
                if ( !tbFuncionarioListOld.contains( tbFuncionarioListNewTbFuncionario ) )
                {
                    TbUsuario oldFkUsuarioOfTbFuncionarioListNewTbFuncionario = tbFuncionarioListNewTbFuncionario.getFkUsuario();
                    tbFuncionarioListNewTbFuncionario.setFkUsuario( tbUsuario );
                    tbFuncionarioListNewTbFuncionario = em.merge( tbFuncionarioListNewTbFuncionario );
                    if ( oldFkUsuarioOfTbFuncionarioListNewTbFuncionario != null && !oldFkUsuarioOfTbFuncionarioListNewTbFuncionario.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbFuncionarioListNewTbFuncionario.getTbFuncionarioList().remove( tbFuncionarioListNewTbFuncionario );
                        oldFkUsuarioOfTbFuncionarioListNewTbFuncionario = em.merge( oldFkUsuarioOfTbFuncionarioListNewTbFuncionario );
                    }
                }
            }
            for ( TbDesconto tbDescontoListNewTbDesconto : tbDescontoListNew )
            {
                if ( !tbDescontoListOld.contains( tbDescontoListNewTbDesconto ) )
                {
                    TbUsuario oldFkUsuarioOfTbDescontoListNewTbDesconto = tbDescontoListNewTbDesconto.getFkUsuario();
                    tbDescontoListNewTbDesconto.setFkUsuario( tbUsuario );
                    tbDescontoListNewTbDesconto = em.merge( tbDescontoListNewTbDesconto );
                    if ( oldFkUsuarioOfTbDescontoListNewTbDesconto != null && !oldFkUsuarioOfTbDescontoListNewTbDesconto.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbDescontoListNewTbDesconto.getTbDescontoList().remove( tbDescontoListNewTbDesconto );
                        oldFkUsuarioOfTbDescontoListNewTbDesconto = em.merge( oldFkUsuarioOfTbDescontoListNewTbDesconto );
                    }
                }
            }
            for ( TbOperacaoSistema tbOperacaoSistemaListOldTbOperacaoSistema : tbOperacaoSistemaListOld )
            {
                if ( !tbOperacaoSistemaListNew.contains( tbOperacaoSistemaListOldTbOperacaoSistema ) )
                {
                    tbOperacaoSistemaListOldTbOperacaoSistema.setFkUsuario( null );
                    tbOperacaoSistemaListOldTbOperacaoSistema = em.merge( tbOperacaoSistemaListOldTbOperacaoSistema );
                }
            }
            for ( TbOperacaoSistema tbOperacaoSistemaListNewTbOperacaoSistema : tbOperacaoSistemaListNew )
            {
                if ( !tbOperacaoSistemaListOld.contains( tbOperacaoSistemaListNewTbOperacaoSistema ) )
                {
                    TbUsuario oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema = tbOperacaoSistemaListNewTbOperacaoSistema.getFkUsuario();
                    tbOperacaoSistemaListNewTbOperacaoSistema.setFkUsuario( tbUsuario );
                    tbOperacaoSistemaListNewTbOperacaoSistema = em.merge( tbOperacaoSistemaListNewTbOperacaoSistema );
                    if ( oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema != null && !oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema.getTbOperacaoSistemaList().remove( tbOperacaoSistemaListNewTbOperacaoSistema );
                        oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema = em.merge( oldFkUsuarioOfTbOperacaoSistemaListNewTbOperacaoSistema );
                    }
                }
            }
            for ( TbSaidasProdutos tbSaidasProdutosListNewTbSaidasProdutos : tbSaidasProdutosListNew )
            {
                if ( !tbSaidasProdutosListOld.contains( tbSaidasProdutosListNewTbSaidasProdutos ) )
                {
                    TbUsuario oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos = tbSaidasProdutosListNewTbSaidasProdutos.getFkUsuario();
                    tbSaidasProdutosListNewTbSaidasProdutos.setFkUsuario( tbUsuario );
                    tbSaidasProdutosListNewTbSaidasProdutos = em.merge( tbSaidasProdutosListNewTbSaidasProdutos );
                    if ( oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos != null && !oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos.getTbSaidasProdutosList().remove( tbSaidasProdutosListNewTbSaidasProdutos );
                        oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos = em.merge( oldFkUsuarioOfTbSaidasProdutosListNewTbSaidasProdutos );
                    }
                }
            }
            for ( TbPreco tbPrecoListNewTbPreco : tbPrecoListNew )
            {
                if ( !tbPrecoListOld.contains( tbPrecoListNewTbPreco ) )
                {
                    TbUsuario oldFkUsuarioOfTbPrecoListNewTbPreco = tbPrecoListNewTbPreco.getFkUsuario();
                    tbPrecoListNewTbPreco.setFkUsuario( tbUsuario );
                    tbPrecoListNewTbPreco = em.merge( tbPrecoListNewTbPreco );
                    if ( oldFkUsuarioOfTbPrecoListNewTbPreco != null && !oldFkUsuarioOfTbPrecoListNewTbPreco.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbPrecoListNewTbPreco.getTbPrecoList().remove( tbPrecoListNewTbPreco );
                        oldFkUsuarioOfTbPrecoListNewTbPreco = em.merge( oldFkUsuarioOfTbPrecoListNewTbPreco );
                    }
                }
            }
            for ( TbEntrada tbEntradaListNewTbEntrada : tbEntradaListNew )
            {
                if ( !tbEntradaListOld.contains( tbEntradaListNewTbEntrada ) )
                {
                    TbUsuario oldIdUsuarioOfTbEntradaListNewTbEntrada = tbEntradaListNewTbEntrada.getIdUsuario();
                    tbEntradaListNewTbEntrada.setIdUsuario( tbUsuario );
                    tbEntradaListNewTbEntrada = em.merge( tbEntradaListNewTbEntrada );
                    if ( oldIdUsuarioOfTbEntradaListNewTbEntrada != null && !oldIdUsuarioOfTbEntradaListNewTbEntrada.equals( tbUsuario ) )
                    {
                        oldIdUsuarioOfTbEntradaListNewTbEntrada.getTbEntradaList().remove( tbEntradaListNewTbEntrada );
                        oldIdUsuarioOfTbEntradaListNewTbEntrada = em.merge( oldIdUsuarioOfTbEntradaListNewTbEntrada );
                    }
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListOldTbEntradaVasilhame : tbEntradaVasilhameListOld )
            {
                if ( !tbEntradaVasilhameListNew.contains( tbEntradaVasilhameListOldTbEntradaVasilhame ) )
                {
                    tbEntradaVasilhameListOldTbEntradaVasilhame.setFkUsuario( null );
                    tbEntradaVasilhameListOldTbEntradaVasilhame = em.merge( tbEntradaVasilhameListOldTbEntradaVasilhame );
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListNewTbEntradaVasilhame : tbEntradaVasilhameListNew )
            {
                if ( !tbEntradaVasilhameListOld.contains( tbEntradaVasilhameListNewTbEntradaVasilhame ) )
                {
                    TbUsuario oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame = tbEntradaVasilhameListNewTbEntradaVasilhame.getFkUsuario();
                    tbEntradaVasilhameListNewTbEntradaVasilhame.setFkUsuario( tbUsuario );
                    tbEntradaVasilhameListNewTbEntradaVasilhame = em.merge( tbEntradaVasilhameListNewTbEntradaVasilhame );
                    if ( oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame != null && !oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame.getTbEntradaVasilhameList().remove( tbEntradaVasilhameListNewTbEntradaVasilhame );
                        oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame = em.merge( oldFkUsuarioOfTbEntradaVasilhameListNewTbEntradaVasilhame );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    TbUsuario oldCodigoUsuarioOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getCodigoUsuario();
                    tbVendaListNewTbVenda.setCodigoUsuario( tbUsuario );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldCodigoUsuarioOfTbVendaListNewTbVenda != null && !oldCodigoUsuarioOfTbVendaListNewTbVenda.equals( tbUsuario ) )
                    {
                        oldCodigoUsuarioOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldCodigoUsuarioOfTbVendaListNewTbVenda = em.merge( oldCodigoUsuarioOfTbVendaListNewTbVenda );
                    }
                }
            }
            for ( Promocao promocaoListNewPromocao : promocaoListNew )
            {
                if ( !promocaoListOld.contains( promocaoListNewPromocao ) )
                {
                    TbUsuario oldFkUsuarioOfPromocaoListNewPromocao = promocaoListNewPromocao.getFkUsuario();
                    promocaoListNewPromocao.setFkUsuario( tbUsuario );
                    promocaoListNewPromocao = em.merge( promocaoListNewPromocao );
                    if ( oldFkUsuarioOfPromocaoListNewPromocao != null && !oldFkUsuarioOfPromocaoListNewPromocao.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfPromocaoListNewPromocao.getPromocaoList().remove( promocaoListNewPromocao );
                        oldFkUsuarioOfPromocaoListNewPromocao = em.merge( oldFkUsuarioOfPromocaoListNewPromocao );
                    }
                }
            }
            for ( EntradaTesouraria entradaTesourariaListOldEntradaTesouraria : entradaTesourariaListOld )
            {
                if ( !entradaTesourariaListNew.contains( entradaTesourariaListOldEntradaTesouraria ) )
                {
                    entradaTesourariaListOldEntradaTesouraria.setFkUsuario( null );
                    entradaTesourariaListOldEntradaTesouraria = em.merge( entradaTesourariaListOldEntradaTesouraria );
                }
            }
            for ( EntradaTesouraria entradaTesourariaListNewEntradaTesouraria : entradaTesourariaListNew )
            {
                if ( !entradaTesourariaListOld.contains( entradaTesourariaListNewEntradaTesouraria ) )
                {
                    TbUsuario oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria = entradaTesourariaListNewEntradaTesouraria.getFkUsuario();
                    entradaTesourariaListNewEntradaTesouraria.setFkUsuario( tbUsuario );
                    entradaTesourariaListNewEntradaTesouraria = em.merge( entradaTesourariaListNewEntradaTesouraria );
                    if ( oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria != null && !oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria.getEntradaTesourariaList().remove( entradaTesourariaListNewEntradaTesouraria );
                        oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria = em.merge( oldFkUsuarioOfEntradaTesourariaListNewEntradaTesouraria );
                    }
                }
            }
            for ( TbItemPermissao tbItemPermissaoListNewTbItemPermissao : tbItemPermissaoListNew )
            {
                if ( !tbItemPermissaoListOld.contains( tbItemPermissaoListNewTbItemPermissao ) )
                {
                    TbUsuario oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao = tbItemPermissaoListNewTbItemPermissao.getIdUsuario();
                    tbItemPermissaoListNewTbItemPermissao.setIdUsuario( tbUsuario );
                    tbItemPermissaoListNewTbItemPermissao = em.merge( tbItemPermissaoListNewTbItemPermissao );
                    if ( oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao != null && !oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao.equals( tbUsuario ) )
                    {
                        oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao.getTbItemPermissaoList().remove( tbItemPermissaoListNewTbItemPermissao );
                        oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao = em.merge( oldIdUsuarioOfTbItemPermissaoListNewTbItemPermissao );
                    }
                }
            }
            for ( AccessoArmazem accessoArmazemListNewAccessoArmazem : accessoArmazemListNew )
            {
                if ( !accessoArmazemListOld.contains( accessoArmazemListNewAccessoArmazem ) )
                {
                    TbUsuario oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem = accessoArmazemListNewAccessoArmazem.getFkUsuario();
                    accessoArmazemListNewAccessoArmazem.setFkUsuario( tbUsuario );
                    accessoArmazemListNewAccessoArmazem = em.merge( accessoArmazemListNewAccessoArmazem );
                    if ( oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem != null && !oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem.getAccessoArmazemList().remove( accessoArmazemListNewAccessoArmazem );
                        oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem = em.merge( oldFkUsuarioOfAccessoArmazemListNewAccessoArmazem );
                    }
                }
            }
            for ( PrevioAviso previoAvisoListNewPrevioAviso : previoAvisoListNew )
            {
                if ( !previoAvisoListOld.contains( previoAvisoListNewPrevioAviso ) )
                {
                    TbUsuario oldFkUsuarioOfPrevioAvisoListNewPrevioAviso = previoAvisoListNewPrevioAviso.getFkUsuario();
                    previoAvisoListNewPrevioAviso.setFkUsuario( tbUsuario );
                    previoAvisoListNewPrevioAviso = em.merge( previoAvisoListNewPrevioAviso );
                    if ( oldFkUsuarioOfPrevioAvisoListNewPrevioAviso != null && !oldFkUsuarioOfPrevioAvisoListNewPrevioAviso.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfPrevioAvisoListNewPrevioAviso.getPrevioAvisoList().remove( previoAvisoListNewPrevioAviso );
                        oldFkUsuarioOfPrevioAvisoListNewPrevioAviso = em.merge( oldFkUsuarioOfPrevioAvisoListNewPrevioAviso );
                    }
                }
            }
            for ( TbSaidaVasilhame tbSaidaVasilhameListNewTbSaidaVasilhame : tbSaidaVasilhameListNew )
            {
                if ( !tbSaidaVasilhameListOld.contains( tbSaidaVasilhameListNewTbSaidaVasilhame ) )
                {
                    TbUsuario oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame = tbSaidaVasilhameListNewTbSaidaVasilhame.getFkUsuario();
                    tbSaidaVasilhameListNewTbSaidaVasilhame.setFkUsuario( tbUsuario );
                    tbSaidaVasilhameListNewTbSaidaVasilhame = em.merge( tbSaidaVasilhameListNewTbSaidaVasilhame );
                    if ( oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame != null && !oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame.getTbSaidaVasilhameList().remove( tbSaidaVasilhameListNewTbSaidaVasilhame );
                        oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame = em.merge( oldFkUsuarioOfTbSaidaVasilhameListNewTbSaidaVasilhame );
                    }
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListOldAmortizacaoDivida : amortizacaoDividaListOld )
            {
                if ( !amortizacaoDividaListNew.contains( amortizacaoDividaListOldAmortizacaoDivida ) )
                {
                    amortizacaoDividaListOldAmortizacaoDivida.setFkUsuario( null );
                    amortizacaoDividaListOldAmortizacaoDivida = em.merge( amortizacaoDividaListOldAmortizacaoDivida );
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListNewAmortizacaoDivida : amortizacaoDividaListNew )
            {
                if ( !amortizacaoDividaListOld.contains( amortizacaoDividaListNewAmortizacaoDivida ) )
                {
                    TbUsuario oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida = amortizacaoDividaListNewAmortizacaoDivida.getFkUsuario();
                    amortizacaoDividaListNewAmortizacaoDivida.setFkUsuario( tbUsuario );
                    amortizacaoDividaListNewAmortizacaoDivida = em.merge( amortizacaoDividaListNewAmortizacaoDivida );
                    if ( oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida != null && !oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida.equals( tbUsuario ) )
                    {
                        oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida.getAmortizacaoDividaList().remove( amortizacaoDividaListNewAmortizacaoDivida );
                        oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida = em.merge( oldFkUsuarioOfAmortizacaoDividaListNewAmortizacaoDivida );
                    }
                }
            }
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            String msg = ex.getLocalizedMessage();
            if ( msg == null || msg.length() == 0 )
            {
                Integer id = tbUsuario.getCodigo();
                if ( findTbUsuario( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbUsuario with id " + id + " no longer exists." );
                }
            }
            throw ex;
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public void destroy( Integer id ) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbUsuario tbUsuario;
            try
            {
                tbUsuario = em.getReference( TbUsuario.class, id );
                tbUsuario.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbUsuario with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemSalarioExtra> itemSalarioExtraListOrphanCheck = tbUsuario.getItemSalarioExtraList();
            for ( ItemSalarioExtra itemSalarioExtraListOrphanCheckItemSalarioExtra : itemSalarioExtraListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the ItemSalarioExtra " + itemSalarioExtraListOrphanCheckItemSalarioExtra + " in its itemSalarioExtraList field has a non-nullable fkTbUsuario field." );
            }
            List<NotasCompras> notasComprasListOrphanCheck = tbUsuario.getNotasComprasList();
            for ( NotasCompras notasComprasListOrphanCheckNotasCompras : notasComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the NotasCompras " + notasComprasListOrphanCheckNotasCompras + " in its notasComprasList field has a non-nullable codigoUsuario field." );
            }
            List<Notas> notasListOrphanCheck = tbUsuario.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable codigoUsuario field." );
            }
            List<Turno> turnoListOrphanCheck = tbUsuario.getTurnoList();
            for ( Turno turnoListOrphanCheckTurno : turnoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the Turno " + turnoListOrphanCheckTurno + " in its turnoList field has a non-nullable fkUsuario field." );
            }
            List<PedidoFeria> pedidoFeriaListOrphanCheck = tbUsuario.getPedidoFeriaList();
            for ( PedidoFeria pedidoFeriaListOrphanCheckPedidoFeria : pedidoFeriaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the PedidoFeria " + pedidoFeriaListOrphanCheckPedidoFeria + " in its pedidoFeriaList field has a non-nullable fkUsuario field." );
            }
            List<TbEncomenda> tbEncomendaListOrphanCheck = tbUsuario.getTbEncomendaList();
            for ( TbEncomenda tbEncomendaListOrphanCheckTbEncomenda : tbEncomendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbEncomenda " + tbEncomendaListOrphanCheckTbEncomenda + " in its tbEncomendaList field has a non-nullable idUsuario field." );
            }
            List<TbEstorno> tbEstornoListOrphanCheck = tbUsuario.getTbEstornoList();
            for ( TbEstorno tbEstornoListOrphanCheckTbEstorno : tbEstornoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbEstorno " + tbEstornoListOrphanCheckTbEstorno + " in its tbEstornoList field has a non-nullable fkUsuario field." );
            }
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListOrphanCheck = tbUsuario.getPagamentoSubsidioFeriaNatalList();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListOrphanCheckPagamentoSubsidioFeriaNatal : pagamentoSubsidioFeriaNatalListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the PagamentoSubsidioFeriaNatal " + pagamentoSubsidioFeriaNatalListOrphanCheckPagamentoSubsidioFeriaNatal + " in its pagamentoSubsidioFeriaNatalList field has a non-nullable fkUsuario field." );
            }
            List<TbProForma> tbProFormaListOrphanCheck = tbUsuario.getTbProFormaList();
            for ( TbProForma tbProFormaListOrphanCheckTbProForma : tbProFormaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbProForma " + tbProFormaListOrphanCheckTbProForma + " in its tbProFormaList field has a non-nullable fkUsuario field." );
            }
            List<FechoPeriodo> fechoPeriodoListOrphanCheck = tbUsuario.getFechoPeriodoList();
            for ( FechoPeriodo fechoPeriodoListOrphanCheckFechoPeriodo : fechoPeriodoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the FechoPeriodo " + fechoPeriodoListOrphanCheckFechoPeriodo + " in its fechoPeriodoList field has a non-nullable fkUsuario field." );
            }
            List<Amortizacao> amortizacaoListOrphanCheck = tbUsuario.getAmortizacaoList();
            for ( Amortizacao amortizacaoListOrphanCheckAmortizacao : amortizacaoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the Amortizacao " + amortizacaoListOrphanCheckAmortizacao + " in its amortizacaoList field has a non-nullable fkUsuario field." );
            }
            List<Anexos> anexosListOrphanCheck = tbUsuario.getAnexosList();
            for ( Anexos anexosListOrphanCheckAnexos : anexosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the Anexos " + anexosListOrphanCheckAnexos + " in its anexosList field has a non-nullable fkUsuario field." );
            }
            List<FechoContrato> fechoContratoListOrphanCheck = tbUsuario.getFechoContratoList();
            for ( FechoContrato fechoContratoListOrphanCheckFechoContrato : fechoContratoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the FechoContrato " + fechoContratoListOrphanCheckFechoContrato + " in its fechoContratoList field has a non-nullable fkUsuario field." );
            }
            List<TbDesconto> tbDescontoListOrphanCheck = tbUsuario.getTbDescontoList();
            for ( TbDesconto tbDescontoListOrphanCheckTbDesconto : tbDescontoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbDesconto " + tbDescontoListOrphanCheckTbDesconto + " in its tbDescontoList field has a non-nullable fkUsuario field." );
            }
            List<TbSaidasProdutos> tbSaidasProdutosListOrphanCheck = tbUsuario.getTbSaidasProdutosList();
            for ( TbSaidasProdutos tbSaidasProdutosListOrphanCheckTbSaidasProdutos : tbSaidasProdutosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbSaidasProdutos " + tbSaidasProdutosListOrphanCheckTbSaidasProdutos + " in its tbSaidasProdutosList field has a non-nullable fkUsuario field." );
            }
            List<TbPreco> tbPrecoListOrphanCheck = tbUsuario.getTbPrecoList();
            for ( TbPreco tbPrecoListOrphanCheckTbPreco : tbPrecoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbPreco " + tbPrecoListOrphanCheckTbPreco + " in its tbPrecoList field has a non-nullable fkUsuario field." );
            }
            List<TbEntrada> tbEntradaListOrphanCheck = tbUsuario.getTbEntradaList();
            for ( TbEntrada tbEntradaListOrphanCheckTbEntrada : tbEntradaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbEntrada " + tbEntradaListOrphanCheckTbEntrada + " in its tbEntradaList field has a non-nullable idUsuario field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = tbUsuario.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable codigoUsuario field." );
            }
            List<Promocao> promocaoListOrphanCheck = tbUsuario.getPromocaoList();
            for ( Promocao promocaoListOrphanCheckPromocao : promocaoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the Promocao " + promocaoListOrphanCheckPromocao + " in its promocaoList field has a non-nullable fkUsuario field." );
            }
            List<TbItemPermissao> tbItemPermissaoListOrphanCheck = tbUsuario.getTbItemPermissaoList();
            for ( TbItemPermissao tbItemPermissaoListOrphanCheckTbItemPermissao : tbItemPermissaoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbItemPermissao " + tbItemPermissaoListOrphanCheckTbItemPermissao + " in its tbItemPermissaoList field has a non-nullable idUsuario field." );
            }
            List<AccessoArmazem> accessoArmazemListOrphanCheck = tbUsuario.getAccessoArmazemList();
            for ( AccessoArmazem accessoArmazemListOrphanCheckAccessoArmazem : accessoArmazemListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the AccessoArmazem " + accessoArmazemListOrphanCheckAccessoArmazem + " in its accessoArmazemList field has a non-nullable fkUsuario field." );
            }
            List<PrevioAviso> previoAvisoListOrphanCheck = tbUsuario.getPrevioAvisoList();
            for ( PrevioAviso previoAvisoListOrphanCheckPrevioAviso : previoAvisoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the PrevioAviso " + previoAvisoListOrphanCheckPrevioAviso + " in its previoAvisoList field has a non-nullable fkUsuario field." );
            }
            List<TbSaidaVasilhame> tbSaidaVasilhameListOrphanCheck = tbUsuario.getTbSaidaVasilhameList();
            for ( TbSaidaVasilhame tbSaidaVasilhameListOrphanCheckTbSaidaVasilhame : tbSaidaVasilhameListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbUsuario (" + tbUsuario + ") cannot be destroyed since the TbSaidaVasilhame " + tbSaidaVasilhameListOrphanCheckTbSaidaVasilhame + " in its tbSaidaVasilhameList field has a non-nullable fkUsuario field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbStatus idStatus = tbUsuario.getIdStatus();
            if ( idStatus != null )
            {
                idStatus.getTbUsuarioList().remove( tbUsuario );
                idStatus = em.merge( idStatus );
            }
            TbTipoUsuario idTipoUsuario = tbUsuario.getIdTipoUsuario();
            if ( idTipoUsuario != null )
            {
                idTipoUsuario.getTbUsuarioList().remove( tbUsuario );
                idTipoUsuario = em.merge( idTipoUsuario );
            }
            TbSexo codigoSexo = tbUsuario.getCodigoSexo();
            if ( codigoSexo != null )
            {
                codigoSexo.getTbUsuarioList().remove( tbUsuario );
                codigoSexo = em.merge( codigoSexo );
            }
            List<Compras> comprasList = tbUsuario.getComprasList();
            for ( Compras comprasListCompras : comprasList )
            {
                comprasListCompras.setCodigoUsuario( null );
                comprasListCompras = em.merge( comprasListCompras );
            }
            List<TransferenciaBancaria> transferenciaBancariaList = tbUsuario.getTransferenciaBancariaList();
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancaria : transferenciaBancariaList )
            {
                transferenciaBancariaListTransferenciaBancaria.setFkUsuario( null );
                transferenciaBancariaListTransferenciaBancaria = em.merge( transferenciaBancariaListTransferenciaBancaria );
            }
            List<LevantamentoBancario> levantamentoBancarioList = tbUsuario.getLevantamentoBancarioList();
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancario : levantamentoBancarioList )
            {
                levantamentoBancarioListLevantamentoBancario.setFkUsuario( null );
                levantamentoBancarioListLevantamentoBancario = em.merge( levantamentoBancarioListLevantamentoBancario );
            }
            List<SaidasTesouraria> saidasTesourariaList = tbUsuario.getSaidasTesourariaList();
            for ( SaidasTesouraria saidasTesourariaListSaidasTesouraria : saidasTesourariaList )
            {
                saidasTesourariaListSaidasTesouraria.setFkUsuario( null );
                saidasTesourariaListSaidasTesouraria = em.merge( saidasTesourariaListSaidasTesouraria );
            }
            List<TbAcerto> tbAcertoList = tbUsuario.getTbAcertoList();
            for ( TbAcerto tbAcertoListTbAcerto : tbAcertoList )
            {
                tbAcertoListTbAcerto.setIdUsuario( null );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
            }
            List<DepositoBancario> depositoBancarioList = tbUsuario.getDepositoBancarioList();
            for ( DepositoBancario depositoBancarioListDepositoBancario : depositoBancarioList )
            {
                depositoBancarioListDepositoBancario.setFkUsuario( null );
                depositoBancarioListDepositoBancario = em.merge( depositoBancarioListDepositoBancario );
            }
            List<TbFuncionario> tbFuncionarioList = tbUsuario.getTbFuncionarioList();
            for ( TbFuncionario tbFuncionarioListTbFuncionario : tbFuncionarioList )
            {
                tbFuncionarioListTbFuncionario.setFkUsuario( null );
                tbFuncionarioListTbFuncionario = em.merge( tbFuncionarioListTbFuncionario );
            }
            List<TbOperacaoSistema> tbOperacaoSistemaList = tbUsuario.getTbOperacaoSistemaList();
            for ( TbOperacaoSistema tbOperacaoSistemaListTbOperacaoSistema : tbOperacaoSistemaList )
            {
                tbOperacaoSistemaListTbOperacaoSistema.setFkUsuario( null );
                tbOperacaoSistemaListTbOperacaoSistema = em.merge( tbOperacaoSistemaListTbOperacaoSistema );
            }
            List<TbEntradaVasilhame> tbEntradaVasilhameList = tbUsuario.getTbEntradaVasilhameList();
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhame : tbEntradaVasilhameList )
            {
                tbEntradaVasilhameListTbEntradaVasilhame.setFkUsuario( null );
                tbEntradaVasilhameListTbEntradaVasilhame = em.merge( tbEntradaVasilhameListTbEntradaVasilhame );
            }
            List<EntradaTesouraria> entradaTesourariaList = tbUsuario.getEntradaTesourariaList();
            for ( EntradaTesouraria entradaTesourariaListEntradaTesouraria : entradaTesourariaList )
            {
                entradaTesourariaListEntradaTesouraria.setFkUsuario( null );
                entradaTesourariaListEntradaTesouraria = em.merge( entradaTesourariaListEntradaTesouraria );
            }
            List<AmortizacaoDivida> amortizacaoDividaList = tbUsuario.getAmortizacaoDividaList();
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDivida : amortizacaoDividaList )
            {
                amortizacaoDividaListAmortizacaoDivida.setFkUsuario( null );
                amortizacaoDividaListAmortizacaoDivida = em.merge( amortizacaoDividaListAmortizacaoDivida );
            }
            em.remove( tbUsuario );
            em.getTransaction().commit();
        }
        finally
        {
            if ( em != null )
            {
                em.close();
            }
        }
    }

    public List<TbUsuario> findTbUsuarioEntities()
    {
        return findTbUsuarioEntities( true, -1, -1 );
    }

    public List<TbUsuario> findTbUsuarioEntities( int maxResults, int firstResult )
    {
        return findTbUsuarioEntities( false, maxResults, firstResult );
    }

    private List<TbUsuario> findTbUsuarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbUsuario.class ) );
            Query q = em.createQuery( cq );
            if ( !all )
            {
                q.setMaxResults( maxResults );
                q.setFirstResult( firstResult );
            }
            return q.getResultList();
        }
        finally
        {
            em.close();
        }
    }

    public TbUsuario findTbUsuario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbUsuario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbUsuarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbUsuario> rt = cq.from( TbUsuario.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( (Long) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
