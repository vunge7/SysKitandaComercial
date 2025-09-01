/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Grupo;
import entity.Modelo;
import entity.TbFornecedor;
import entity.TbLocal;
import entity.TbTipoProduto;
import entity.Unidade;
import entity.ProdutoIsento;
import java.util.ArrayList;
import java.util.List;
import entity.TbStockMirrow;
import entity.ProdutoImposto;
import entity.TbItemPedidos;
import entity.TbAcerto;
import entity.TbItemEncomenda;
import entity.ItemCompras;
import entity.TbVasilhame;
import entity.TbItemSaidas;
import entity.TbDesconto;
import entity.TbItemProForma;
import entity.TbPreco;
import entity.ServicoRetencao;
import entity.TbItemVenda;
import entity.TbItemEstorno;
import entity.TbEntrada;
import entity.TbStock;
import entity.NotasItem;
import entity.NotasItemCompras;
import entity.TbItemEntradas;
import entity.TbProduto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbProdutoJpaController implements Serializable
{

    public TbProdutoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbProduto tbProduto )
    {
        if ( tbProduto.getProdutoIsentoList() == null )
        {
            tbProduto.setProdutoIsentoList( new ArrayList<ProdutoIsento>() );
        }
        if ( tbProduto.getTbStockMirrowList() == null )
        {
            tbProduto.setTbStockMirrowList( new ArrayList<TbStockMirrow>() );
        }
        if ( tbProduto.getProdutoImpostoList() == null )
        {
            tbProduto.setProdutoImpostoList( new ArrayList<ProdutoImposto>() );
        }
        if ( tbProduto.getTbItemPedidosList() == null )
        {
            tbProduto.setTbItemPedidosList( new ArrayList<TbItemPedidos>() );
        }
        if ( tbProduto.getTbAcertoList() == null )
        {
            tbProduto.setTbAcertoList( new ArrayList<TbAcerto>() );
        }
        if ( tbProduto.getTbItemEncomendaList() == null )
        {
            tbProduto.setTbItemEncomendaList( new ArrayList<TbItemEncomenda>() );
        }
        if ( tbProduto.getItemComprasList() == null )
        {
            tbProduto.setItemComprasList( new ArrayList<ItemCompras>() );
        }
        if ( tbProduto.getTbVasilhameList() == null )
        {
            tbProduto.setTbVasilhameList( new ArrayList<TbVasilhame>() );
        }
        if ( tbProduto.getTbItemSaidasList() == null )
        {
            tbProduto.setTbItemSaidasList( new ArrayList<TbItemSaidas>() );
        }
        if ( tbProduto.getTbDescontoList() == null )
        {
            tbProduto.setTbDescontoList( new ArrayList<TbDesconto>() );
        }
        if ( tbProduto.getTbItemProFormaList() == null )
        {
            tbProduto.setTbItemProFormaList( new ArrayList<TbItemProForma>() );
        }
        if ( tbProduto.getTbPrecoList() == null )
        {
            tbProduto.setTbPrecoList( new ArrayList<TbPreco>() );
        }
        if ( tbProduto.getServicoRetencaoList() == null )
        {
            tbProduto.setServicoRetencaoList( new ArrayList<ServicoRetencao>() );
        }
        if ( tbProduto.getTbItemVendaList() == null )
        {
            tbProduto.setTbItemVendaList( new ArrayList<TbItemVenda>() );
        }
        if ( tbProduto.getTbItemEstornoList() == null )
        {
            tbProduto.setTbItemEstornoList( new ArrayList<TbItemEstorno>() );
        }
        if ( tbProduto.getTbEntradaList() == null )
        {
            tbProduto.setTbEntradaList( new ArrayList<TbEntrada>() );
        }
        if ( tbProduto.getTbStockList() == null )
        {
            tbProduto.setTbStockList( new ArrayList<TbStock>() );
        }
        if ( tbProduto.getNotasItemList() == null )
        {
            tbProduto.setNotasItemList( new ArrayList<NotasItem>() );
        }
        if ( tbProduto.getNotasItemComprasList() == null )
        {
            tbProduto.setNotasItemComprasList( new ArrayList<NotasItemCompras>() );
        }
        if ( tbProduto.getTbItemEntradasList() == null )
        {
            tbProduto.setTbItemEntradasList( new ArrayList<TbItemEntradas>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo fkGrupo = tbProduto.getFkGrupo();
            if ( fkGrupo != null )
            {
                fkGrupo = em.getReference( fkGrupo.getClass(), fkGrupo.getPkGrupo() );
                tbProduto.setFkGrupo( fkGrupo );
            }
            Modelo fkModelo = tbProduto.getFkModelo();
            if ( fkModelo != null )
            {
                fkModelo = em.getReference( fkModelo.getClass(), fkModelo.getPkModelo() );
                tbProduto.setFkModelo( fkModelo );
            }
            TbFornecedor codFornecedores = tbProduto.getCodFornecedores();
            if ( codFornecedores != null )
            {
                codFornecedores = em.getReference( codFornecedores.getClass(), codFornecedores.getCodigo() );
                tbProduto.setCodFornecedores( codFornecedores );
            }
            TbLocal codLocal = tbProduto.getCodLocal();
            if ( codLocal != null )
            {
                codLocal = em.getReference( codLocal.getClass(), codLocal.getCodigo() );
                tbProduto.setCodLocal( codLocal );
            }
            TbTipoProduto codTipoProduto = tbProduto.getCodTipoProduto();
            if ( codTipoProduto != null )
            {
                codTipoProduto = em.getReference( codTipoProduto.getClass(), codTipoProduto.getCodigo() );
                tbProduto.setCodTipoProduto( codTipoProduto );
            }
            Unidade codUnidade = tbProduto.getCodUnidade();
            if ( codUnidade != null )
            {
                codUnidade = em.getReference( codUnidade.getClass(), codUnidade.getPkUnidade() );
                tbProduto.setCodUnidade( codUnidade );
            }
            List<ProdutoIsento> attachedProdutoIsentoList = new ArrayList<ProdutoIsento>();
            for ( ProdutoIsento produtoIsentoListProdutoIsentoToAttach : tbProduto.getProdutoIsentoList() )
            {
                produtoIsentoListProdutoIsentoToAttach = em.getReference( produtoIsentoListProdutoIsentoToAttach.getClass(), produtoIsentoListProdutoIsentoToAttach.getPkProdutoIsento() );
                attachedProdutoIsentoList.add( produtoIsentoListProdutoIsentoToAttach );
            }
            tbProduto.setProdutoIsentoList( attachedProdutoIsentoList );
            List<TbStockMirrow> attachedTbStockMirrowList = new ArrayList<TbStockMirrow>();
            for ( TbStockMirrow tbStockMirrowListTbStockMirrowToAttach : tbProduto.getTbStockMirrowList() )
            {
                tbStockMirrowListTbStockMirrowToAttach = em.getReference( tbStockMirrowListTbStockMirrowToAttach.getClass(), tbStockMirrowListTbStockMirrowToAttach.getCodigo() );
                attachedTbStockMirrowList.add( tbStockMirrowListTbStockMirrowToAttach );
            }
            tbProduto.setTbStockMirrowList( attachedTbStockMirrowList );
            List<ProdutoImposto> attachedProdutoImpostoList = new ArrayList<ProdutoImposto>();
            for ( ProdutoImposto produtoImpostoListProdutoImpostoToAttach : tbProduto.getProdutoImpostoList() )
            {
                produtoImpostoListProdutoImpostoToAttach = em.getReference( produtoImpostoListProdutoImpostoToAttach.getClass(), produtoImpostoListProdutoImpostoToAttach.getPkProdutoImposto() );
                attachedProdutoImpostoList.add( produtoImpostoListProdutoImpostoToAttach );
            }
            tbProduto.setProdutoImpostoList( attachedProdutoImpostoList );
            List<TbItemPedidos> attachedTbItemPedidosList = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListTbItemPedidosToAttach : tbProduto.getTbItemPedidosList() )
            {
                tbItemPedidosListTbItemPedidosToAttach = em.getReference( tbItemPedidosListTbItemPedidosToAttach.getClass(), tbItemPedidosListTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosList.add( tbItemPedidosListTbItemPedidosToAttach );
            }
            tbProduto.setTbItemPedidosList( attachedTbItemPedidosList );
            List<TbAcerto> attachedTbAcertoList = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListTbAcertoToAttach : tbProduto.getTbAcertoList() )
            {
                tbAcertoListTbAcertoToAttach = em.getReference( tbAcertoListTbAcertoToAttach.getClass(), tbAcertoListTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoList.add( tbAcertoListTbAcertoToAttach );
            }
            tbProduto.setTbAcertoList( attachedTbAcertoList );
            List<TbItemEncomenda> attachedTbItemEncomendaList = new ArrayList<TbItemEncomenda>();
            for ( TbItemEncomenda tbItemEncomendaListTbItemEncomendaToAttach : tbProduto.getTbItemEncomendaList() )
            {
                tbItemEncomendaListTbItemEncomendaToAttach = em.getReference( tbItemEncomendaListTbItemEncomendaToAttach.getClass(), tbItemEncomendaListTbItemEncomendaToAttach.getCodigo() );
                attachedTbItemEncomendaList.add( tbItemEncomendaListTbItemEncomendaToAttach );
            }
            tbProduto.setTbItemEncomendaList( attachedTbItemEncomendaList );
            List<ItemCompras> attachedItemComprasList = new ArrayList<ItemCompras>();
            for ( ItemCompras itemComprasListItemComprasToAttach : tbProduto.getItemComprasList() )
            {
                itemComprasListItemComprasToAttach = em.getReference( itemComprasListItemComprasToAttach.getClass(), itemComprasListItemComprasToAttach.getPkItmCompras() );
                attachedItemComprasList.add( itemComprasListItemComprasToAttach );
            }
            tbProduto.setItemComprasList( attachedItemComprasList );
            List<TbVasilhame> attachedTbVasilhameList = new ArrayList<TbVasilhame>();
            for ( TbVasilhame tbVasilhameListTbVasilhameToAttach : tbProduto.getTbVasilhameList() )
            {
                tbVasilhameListTbVasilhameToAttach = em.getReference( tbVasilhameListTbVasilhameToAttach.getClass(), tbVasilhameListTbVasilhameToAttach.getPkVasilhame() );
                attachedTbVasilhameList.add( tbVasilhameListTbVasilhameToAttach );
            }
            tbProduto.setTbVasilhameList( attachedTbVasilhameList );
            List<TbItemSaidas> attachedTbItemSaidasList = new ArrayList<TbItemSaidas>();
            for ( TbItemSaidas tbItemSaidasListTbItemSaidasToAttach : tbProduto.getTbItemSaidasList() )
            {
                tbItemSaidasListTbItemSaidasToAttach = em.getReference( tbItemSaidasListTbItemSaidasToAttach.getClass(), tbItemSaidasListTbItemSaidasToAttach.getCodigo() );
                attachedTbItemSaidasList.add( tbItemSaidasListTbItemSaidasToAttach );
            }
            tbProduto.setTbItemSaidasList( attachedTbItemSaidasList );
            List<TbDesconto> attachedTbDescontoList = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListTbDescontoToAttach : tbProduto.getTbDescontoList() )
            {
                tbDescontoListTbDescontoToAttach = em.getReference( tbDescontoListTbDescontoToAttach.getClass(), tbDescontoListTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoList.add( tbDescontoListTbDescontoToAttach );
            }
            tbProduto.setTbDescontoList( attachedTbDescontoList );
            List<TbItemProForma> attachedTbItemProFormaList = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListTbItemProFormaToAttach : tbProduto.getTbItemProFormaList() )
            {
                tbItemProFormaListTbItemProFormaToAttach = em.getReference( tbItemProFormaListTbItemProFormaToAttach.getClass(), tbItemProFormaListTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaList.add( tbItemProFormaListTbItemProFormaToAttach );
            }
            tbProduto.setTbItemProFormaList( attachedTbItemProFormaList );
            List<TbPreco> attachedTbPrecoList = new ArrayList<TbPreco>();
            for ( TbPreco tbPrecoListTbPrecoToAttach : tbProduto.getTbPrecoList() )
            {
                tbPrecoListTbPrecoToAttach = em.getReference( tbPrecoListTbPrecoToAttach.getClass(), tbPrecoListTbPrecoToAttach.getPkPreco() );
                attachedTbPrecoList.add( tbPrecoListTbPrecoToAttach );
            }
            tbProduto.setTbPrecoList( attachedTbPrecoList );
            List<ServicoRetencao> attachedServicoRetencaoList = new ArrayList<ServicoRetencao>();
            for ( ServicoRetencao servicoRetencaoListServicoRetencaoToAttach : tbProduto.getServicoRetencaoList() )
            {
                servicoRetencaoListServicoRetencaoToAttach = em.getReference( servicoRetencaoListServicoRetencaoToAttach.getClass(), servicoRetencaoListServicoRetencaoToAttach.getPkServicoRetencao() );
                attachedServicoRetencaoList.add( servicoRetencaoListServicoRetencaoToAttach );
            }
            tbProduto.setServicoRetencaoList( attachedServicoRetencaoList );
            List<TbItemVenda> attachedTbItemVendaList = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListTbItemVendaToAttach : tbProduto.getTbItemVendaList() )
            {
                tbItemVendaListTbItemVendaToAttach = em.getReference( tbItemVendaListTbItemVendaToAttach.getClass(), tbItemVendaListTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaList.add( tbItemVendaListTbItemVendaToAttach );
            }
            tbProduto.setTbItemVendaList( attachedTbItemVendaList );
            List<TbItemEstorno> attachedTbItemEstornoList = new ArrayList<TbItemEstorno>();
            for ( TbItemEstorno tbItemEstornoListTbItemEstornoToAttach : tbProduto.getTbItemEstornoList() )
            {
                tbItemEstornoListTbItemEstornoToAttach = em.getReference( tbItemEstornoListTbItemEstornoToAttach.getClass(), tbItemEstornoListTbItemEstornoToAttach.getCodigo() );
                attachedTbItemEstornoList.add( tbItemEstornoListTbItemEstornoToAttach );
            }
            tbProduto.setTbItemEstornoList( attachedTbItemEstornoList );
            List<TbEntrada> attachedTbEntradaList = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListTbEntradaToAttach : tbProduto.getTbEntradaList() )
            {
                tbEntradaListTbEntradaToAttach = em.getReference( tbEntradaListTbEntradaToAttach.getClass(), tbEntradaListTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaList.add( tbEntradaListTbEntradaToAttach );
            }
            tbProduto.setTbEntradaList( attachedTbEntradaList );
            List<TbStock> attachedTbStockList = new ArrayList<TbStock>();
            for ( TbStock tbStockListTbStockToAttach : tbProduto.getTbStockList() )
            {
                tbStockListTbStockToAttach = em.getReference( tbStockListTbStockToAttach.getClass(), tbStockListTbStockToAttach.getCodigo() );
                attachedTbStockList.add( tbStockListTbStockToAttach );
            }
            tbProduto.setTbStockList( attachedTbStockList );
            List<NotasItem> attachedNotasItemList = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNotasItemToAttach : tbProduto.getNotasItemList() )
            {
                notasItemListNotasItemToAttach = em.getReference( notasItemListNotasItemToAttach.getClass(), notasItemListNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemList.add( notasItemListNotasItemToAttach );
            }
            tbProduto.setNotasItemList( attachedNotasItemList );
            List<NotasItemCompras> attachedNotasItemComprasList = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNotasItemComprasToAttach : tbProduto.getNotasItemComprasList() )
            {
                notasItemComprasListNotasItemComprasToAttach = em.getReference( notasItemComprasListNotasItemComprasToAttach.getClass(), notasItemComprasListNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasList.add( notasItemComprasListNotasItemComprasToAttach );
            }
            tbProduto.setNotasItemComprasList( attachedNotasItemComprasList );
            List<TbItemEntradas> attachedTbItemEntradasList = new ArrayList<TbItemEntradas>();
            for ( TbItemEntradas tbItemEntradasListTbItemEntradasToAttach : tbProduto.getTbItemEntradasList() )
            {
                tbItemEntradasListTbItemEntradasToAttach = em.getReference( tbItemEntradasListTbItemEntradasToAttach.getClass(), tbItemEntradasListTbItemEntradasToAttach.getIdItemEntradas() );
                attachedTbItemEntradasList.add( tbItemEntradasListTbItemEntradasToAttach );
            }
            tbProduto.setTbItemEntradasList( attachedTbItemEntradasList );
            em.persist( tbProduto );
            if ( fkGrupo != null )
            {
                fkGrupo.getTbProdutoList().add( tbProduto );
                fkGrupo = em.merge( fkGrupo );
            }
            if ( fkModelo != null )
            {
                fkModelo.getTbProdutoList().add( tbProduto );
                fkModelo = em.merge( fkModelo );
            }
            if ( codFornecedores != null )
            {
                codFornecedores.getTbProdutoList().add( tbProduto );
                codFornecedores = em.merge( codFornecedores );
            }
            if ( codLocal != null )
            {
                codLocal.getTbProdutoList().add( tbProduto );
                codLocal = em.merge( codLocal );
            }
            if ( codTipoProduto != null )
            {
                codTipoProduto.getTbProdutoList().add( tbProduto );
                codTipoProduto = em.merge( codTipoProduto );
            }
            if ( codUnidade != null )
            {
                codUnidade.getTbProdutoList().add( tbProduto );
                codUnidade = em.merge( codUnidade );
            }
            for ( ProdutoIsento produtoIsentoListProdutoIsento : tbProduto.getProdutoIsentoList() )
            {
                TbProduto oldFkProdutoOfProdutoIsentoListProdutoIsento = produtoIsentoListProdutoIsento.getFkProduto();
                produtoIsentoListProdutoIsento.setFkProduto( tbProduto );
                produtoIsentoListProdutoIsento = em.merge( produtoIsentoListProdutoIsento );
                if ( oldFkProdutoOfProdutoIsentoListProdutoIsento != null )
                {
                    oldFkProdutoOfProdutoIsentoListProdutoIsento.getProdutoIsentoList().remove( produtoIsentoListProdutoIsento );
                    oldFkProdutoOfProdutoIsentoListProdutoIsento = em.merge( oldFkProdutoOfProdutoIsentoListProdutoIsento );
                }
            }
            for ( TbStockMirrow tbStockMirrowListTbStockMirrow : tbProduto.getTbStockMirrowList() )
            {
                TbProduto oldCodProdutoCodigoOfTbStockMirrowListTbStockMirrow = tbStockMirrowListTbStockMirrow.getCodProdutoCodigo();
                tbStockMirrowListTbStockMirrow.setCodProdutoCodigo( tbProduto );
                tbStockMirrowListTbStockMirrow = em.merge( tbStockMirrowListTbStockMirrow );
                if ( oldCodProdutoCodigoOfTbStockMirrowListTbStockMirrow != null )
                {
                    oldCodProdutoCodigoOfTbStockMirrowListTbStockMirrow.getTbStockMirrowList().remove( tbStockMirrowListTbStockMirrow );
                    oldCodProdutoCodigoOfTbStockMirrowListTbStockMirrow = em.merge( oldCodProdutoCodigoOfTbStockMirrowListTbStockMirrow );
                }
            }
            for ( ProdutoImposto produtoImpostoListProdutoImposto : tbProduto.getProdutoImpostoList() )
            {
                TbProduto oldFkProdutoOfProdutoImpostoListProdutoImposto = produtoImpostoListProdutoImposto.getFkProduto();
                produtoImpostoListProdutoImposto.setFkProduto( tbProduto );
                produtoImpostoListProdutoImposto = em.merge( produtoImpostoListProdutoImposto );
                if ( oldFkProdutoOfProdutoImpostoListProdutoImposto != null )
                {
                    oldFkProdutoOfProdutoImpostoListProdutoImposto.getProdutoImpostoList().remove( produtoImpostoListProdutoImposto );
                    oldFkProdutoOfProdutoImpostoListProdutoImposto = em.merge( oldFkProdutoOfProdutoImpostoListProdutoImposto );
                }
            }
            for ( TbItemPedidos tbItemPedidosListTbItemPedidos : tbProduto.getTbItemPedidosList() )
            {
                TbProduto oldFkProdutosOfTbItemPedidosListTbItemPedidos = tbItemPedidosListTbItemPedidos.getFkProdutos();
                tbItemPedidosListTbItemPedidos.setFkProdutos( tbProduto );
                tbItemPedidosListTbItemPedidos = em.merge( tbItemPedidosListTbItemPedidos );
                if ( oldFkProdutosOfTbItemPedidosListTbItemPedidos != null )
                {
                    oldFkProdutosOfTbItemPedidosListTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListTbItemPedidos );
                    oldFkProdutosOfTbItemPedidosListTbItemPedidos = em.merge( oldFkProdutosOfTbItemPedidosListTbItemPedidos );
                }
            }
            for ( TbAcerto tbAcertoListTbAcerto : tbProduto.getTbAcertoList() )
            {
                TbProduto oldIdProdutoOfTbAcertoListTbAcerto = tbAcertoListTbAcerto.getIdProduto();
                tbAcertoListTbAcerto.setIdProduto( tbProduto );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
                if ( oldIdProdutoOfTbAcertoListTbAcerto != null )
                {
                    oldIdProdutoOfTbAcertoListTbAcerto.getTbAcertoList().remove( tbAcertoListTbAcerto );
                    oldIdProdutoOfTbAcertoListTbAcerto = em.merge( oldIdProdutoOfTbAcertoListTbAcerto );
                }
            }
            for ( TbItemEncomenda tbItemEncomendaListTbItemEncomenda : tbProduto.getTbItemEncomendaList() )
            {
                TbProduto oldIdProdutoOfTbItemEncomendaListTbItemEncomenda = tbItemEncomendaListTbItemEncomenda.getIdProduto();
                tbItemEncomendaListTbItemEncomenda.setIdProduto( tbProduto );
                tbItemEncomendaListTbItemEncomenda = em.merge( tbItemEncomendaListTbItemEncomenda );
                if ( oldIdProdutoOfTbItemEncomendaListTbItemEncomenda != null )
                {
                    oldIdProdutoOfTbItemEncomendaListTbItemEncomenda.getTbItemEncomendaList().remove( tbItemEncomendaListTbItemEncomenda );
                    oldIdProdutoOfTbItemEncomendaListTbItemEncomenda = em.merge( oldIdProdutoOfTbItemEncomendaListTbItemEncomenda );
                }
            }
            for ( ItemCompras itemComprasListItemCompras : tbProduto.getItemComprasList() )
            {
                TbProduto oldFkProdutoOfItemComprasListItemCompras = itemComprasListItemCompras.getFkProduto();
                itemComprasListItemCompras.setFkProduto( tbProduto );
                itemComprasListItemCompras = em.merge( itemComprasListItemCompras );
                if ( oldFkProdutoOfItemComprasListItemCompras != null )
                {
                    oldFkProdutoOfItemComprasListItemCompras.getItemComprasList().remove( itemComprasListItemCompras );
                    oldFkProdutoOfItemComprasListItemCompras = em.merge( oldFkProdutoOfItemComprasListItemCompras );
                }
            }
            for ( TbVasilhame tbVasilhameListTbVasilhame : tbProduto.getTbVasilhameList() )
            {
                TbProduto oldFkProdutoOfTbVasilhameListTbVasilhame = tbVasilhameListTbVasilhame.getFkProduto();
                tbVasilhameListTbVasilhame.setFkProduto( tbProduto );
                tbVasilhameListTbVasilhame = em.merge( tbVasilhameListTbVasilhame );
                if ( oldFkProdutoOfTbVasilhameListTbVasilhame != null )
                {
                    oldFkProdutoOfTbVasilhameListTbVasilhame.getTbVasilhameList().remove( tbVasilhameListTbVasilhame );
                    oldFkProdutoOfTbVasilhameListTbVasilhame = em.merge( oldFkProdutoOfTbVasilhameListTbVasilhame );
                }
            }
            for ( TbItemSaidas tbItemSaidasListTbItemSaidas : tbProduto.getTbItemSaidasList() )
            {
                TbProduto oldFkProdutosOfTbItemSaidasListTbItemSaidas = tbItemSaidasListTbItemSaidas.getFkProdutos();
                tbItemSaidasListTbItemSaidas.setFkProdutos( tbProduto );
                tbItemSaidasListTbItemSaidas = em.merge( tbItemSaidasListTbItemSaidas );
                if ( oldFkProdutosOfTbItemSaidasListTbItemSaidas != null )
                {
                    oldFkProdutosOfTbItemSaidasListTbItemSaidas.getTbItemSaidasList().remove( tbItemSaidasListTbItemSaidas );
                    oldFkProdutosOfTbItemSaidasListTbItemSaidas = em.merge( oldFkProdutosOfTbItemSaidasListTbItemSaidas );
                }
            }
            for ( TbDesconto tbDescontoListTbDesconto : tbProduto.getTbDescontoList() )
            {
                TbProduto oldFkProdutoOfTbDescontoListTbDesconto = tbDescontoListTbDesconto.getFkProduto();
                tbDescontoListTbDesconto.setFkProduto( tbProduto );
                tbDescontoListTbDesconto = em.merge( tbDescontoListTbDesconto );
                if ( oldFkProdutoOfTbDescontoListTbDesconto != null )
                {
                    oldFkProdutoOfTbDescontoListTbDesconto.getTbDescontoList().remove( tbDescontoListTbDesconto );
                    oldFkProdutoOfTbDescontoListTbDesconto = em.merge( oldFkProdutoOfTbDescontoListTbDesconto );
                }
            }
            for ( TbItemProForma tbItemProFormaListTbItemProForma : tbProduto.getTbItemProFormaList() )
            {
                TbProduto oldFkProdutoOfTbItemProFormaListTbItemProForma = tbItemProFormaListTbItemProForma.getFkProduto();
                tbItemProFormaListTbItemProForma.setFkProduto( tbProduto );
                tbItemProFormaListTbItemProForma = em.merge( tbItemProFormaListTbItemProForma );
                if ( oldFkProdutoOfTbItemProFormaListTbItemProForma != null )
                {
                    oldFkProdutoOfTbItemProFormaListTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListTbItemProForma );
                    oldFkProdutoOfTbItemProFormaListTbItemProForma = em.merge( oldFkProdutoOfTbItemProFormaListTbItemProForma );
                }
            }
            for ( TbPreco tbPrecoListTbPreco : tbProduto.getTbPrecoList() )
            {
                TbProduto oldFkProdutoOfTbPrecoListTbPreco = tbPrecoListTbPreco.getFkProduto();
                tbPrecoListTbPreco.setFkProduto( tbProduto );
                tbPrecoListTbPreco = em.merge( tbPrecoListTbPreco );
                if ( oldFkProdutoOfTbPrecoListTbPreco != null )
                {
                    oldFkProdutoOfTbPrecoListTbPreco.getTbPrecoList().remove( tbPrecoListTbPreco );
                    oldFkProdutoOfTbPrecoListTbPreco = em.merge( oldFkProdutoOfTbPrecoListTbPreco );
                }
            }
            for ( ServicoRetencao servicoRetencaoListServicoRetencao : tbProduto.getServicoRetencaoList() )
            {
                TbProduto oldFkProdutoOfServicoRetencaoListServicoRetencao = servicoRetencaoListServicoRetencao.getFkProduto();
                servicoRetencaoListServicoRetencao.setFkProduto( tbProduto );
                servicoRetencaoListServicoRetencao = em.merge( servicoRetencaoListServicoRetencao );
                if ( oldFkProdutoOfServicoRetencaoListServicoRetencao != null )
                {
                    oldFkProdutoOfServicoRetencaoListServicoRetencao.getServicoRetencaoList().remove( servicoRetencaoListServicoRetencao );
                    oldFkProdutoOfServicoRetencaoListServicoRetencao = em.merge( oldFkProdutoOfServicoRetencaoListServicoRetencao );
                }
            }
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbProduto.getTbItemVendaList() )
            {
                TbProduto oldCodigoProdutoOfTbItemVendaListTbItemVenda = tbItemVendaListTbItemVenda.getCodigoProduto();
                tbItemVendaListTbItemVenda.setCodigoProduto( tbProduto );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
                if ( oldCodigoProdutoOfTbItemVendaListTbItemVenda != null )
                {
                    oldCodigoProdutoOfTbItemVendaListTbItemVenda.getTbItemVendaList().remove( tbItemVendaListTbItemVenda );
                    oldCodigoProdutoOfTbItemVendaListTbItemVenda = em.merge( oldCodigoProdutoOfTbItemVendaListTbItemVenda );
                }
            }
            for ( TbItemEstorno tbItemEstornoListTbItemEstorno : tbProduto.getTbItemEstornoList() )
            {
                TbProduto oldFkProdutosOfTbItemEstornoListTbItemEstorno = tbItemEstornoListTbItemEstorno.getFkProdutos();
                tbItemEstornoListTbItemEstorno.setFkProdutos( tbProduto );
                tbItemEstornoListTbItemEstorno = em.merge( tbItemEstornoListTbItemEstorno );
                if ( oldFkProdutosOfTbItemEstornoListTbItemEstorno != null )
                {
                    oldFkProdutosOfTbItemEstornoListTbItemEstorno.getTbItemEstornoList().remove( tbItemEstornoListTbItemEstorno );
                    oldFkProdutosOfTbItemEstornoListTbItemEstorno = em.merge( oldFkProdutosOfTbItemEstornoListTbItemEstorno );
                }
            }
            for ( TbEntrada tbEntradaListTbEntrada : tbProduto.getTbEntradaList() )
            {
                TbProduto oldIdProdutoOfTbEntradaListTbEntrada = tbEntradaListTbEntrada.getIdProduto();
                tbEntradaListTbEntrada.setIdProduto( tbProduto );
                tbEntradaListTbEntrada = em.merge( tbEntradaListTbEntrada );
                if ( oldIdProdutoOfTbEntradaListTbEntrada != null )
                {
                    oldIdProdutoOfTbEntradaListTbEntrada.getTbEntradaList().remove( tbEntradaListTbEntrada );
                    oldIdProdutoOfTbEntradaListTbEntrada = em.merge( oldIdProdutoOfTbEntradaListTbEntrada );
                }
            }
            for ( TbStock tbStockListTbStock : tbProduto.getTbStockList() )
            {
                TbProduto oldCodProdutoCodigoOfTbStockListTbStock = tbStockListTbStock.getCodProdutoCodigo();
                tbStockListTbStock.setCodProdutoCodigo( tbProduto );
                tbStockListTbStock = em.merge( tbStockListTbStock );
                if ( oldCodProdutoCodigoOfTbStockListTbStock != null )
                {
                    oldCodProdutoCodigoOfTbStockListTbStock.getTbStockList().remove( tbStockListTbStock );
                    oldCodProdutoCodigoOfTbStockListTbStock = em.merge( oldCodProdutoCodigoOfTbStockListTbStock );
                }
            }
            for ( NotasItem notasItemListNotasItem : tbProduto.getNotasItemList() )
            {
                TbProduto oldFkProdutoOfNotasItemListNotasItem = notasItemListNotasItem.getFkProduto();
                notasItemListNotasItem.setFkProduto( tbProduto );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
                if ( oldFkProdutoOfNotasItemListNotasItem != null )
                {
                    oldFkProdutoOfNotasItemListNotasItem.getNotasItemList().remove( notasItemListNotasItem );
                    oldFkProdutoOfNotasItemListNotasItem = em.merge( oldFkProdutoOfNotasItemListNotasItem );
                }
            }
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : tbProduto.getNotasItemComprasList() )
            {
                TbProduto oldFkProdutoOfNotasItemComprasListNotasItemCompras = notasItemComprasListNotasItemCompras.getFkProduto();
                notasItemComprasListNotasItemCompras.setFkProduto( tbProduto );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
                if ( oldFkProdutoOfNotasItemComprasListNotasItemCompras != null )
                {
                    oldFkProdutoOfNotasItemComprasListNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNotasItemCompras );
                    oldFkProdutoOfNotasItemComprasListNotasItemCompras = em.merge( oldFkProdutoOfNotasItemComprasListNotasItemCompras );
                }
            }
            for ( TbItemEntradas tbItemEntradasListTbItemEntradas : tbProduto.getTbItemEntradasList() )
            {
                TbProduto oldIdProdutoOfTbItemEntradasListTbItemEntradas = tbItemEntradasListTbItemEntradas.getIdProduto();
                tbItemEntradasListTbItemEntradas.setIdProduto( tbProduto );
                tbItemEntradasListTbItemEntradas = em.merge( tbItemEntradasListTbItemEntradas );
                if ( oldIdProdutoOfTbItemEntradasListTbItemEntradas != null )
                {
                    oldIdProdutoOfTbItemEntradasListTbItemEntradas.getTbItemEntradasList().remove( tbItemEntradasListTbItemEntradas );
                    oldIdProdutoOfTbItemEntradasListTbItemEntradas = em.merge( oldIdProdutoOfTbItemEntradasListTbItemEntradas );
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

    public void edit( TbProduto tbProduto ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbProduto persistentTbProduto = em.find( TbProduto.class, tbProduto.getCodigo() );
            Grupo fkGrupoOld = persistentTbProduto.getFkGrupo();
            Grupo fkGrupoNew = tbProduto.getFkGrupo();
            Modelo fkModeloOld = persistentTbProduto.getFkModelo();
            Modelo fkModeloNew = tbProduto.getFkModelo();
            TbFornecedor codFornecedoresOld = persistentTbProduto.getCodFornecedores();
            TbFornecedor codFornecedoresNew = tbProduto.getCodFornecedores();
            TbLocal codLocalOld = persistentTbProduto.getCodLocal();
            TbLocal codLocalNew = tbProduto.getCodLocal();
            TbTipoProduto codTipoProdutoOld = persistentTbProduto.getCodTipoProduto();
            TbTipoProduto codTipoProdutoNew = tbProduto.getCodTipoProduto();
            Unidade codUnidadeOld = persistentTbProduto.getCodUnidade();
            Unidade codUnidadeNew = tbProduto.getCodUnidade();
            List<ProdutoIsento> produtoIsentoListOld = persistentTbProduto.getProdutoIsentoList();
            List<ProdutoIsento> produtoIsentoListNew = tbProduto.getProdutoIsentoList();
            List<TbStockMirrow> tbStockMirrowListOld = persistentTbProduto.getTbStockMirrowList();
            List<TbStockMirrow> tbStockMirrowListNew = tbProduto.getTbStockMirrowList();
            List<ProdutoImposto> produtoImpostoListOld = persistentTbProduto.getProdutoImpostoList();
            List<ProdutoImposto> produtoImpostoListNew = tbProduto.getProdutoImpostoList();
            List<TbItemPedidos> tbItemPedidosListOld = persistentTbProduto.getTbItemPedidosList();
            List<TbItemPedidos> tbItemPedidosListNew = tbProduto.getTbItemPedidosList();
            List<TbAcerto> tbAcertoListOld = persistentTbProduto.getTbAcertoList();
            List<TbAcerto> tbAcertoListNew = tbProduto.getTbAcertoList();
            List<TbItemEncomenda> tbItemEncomendaListOld = persistentTbProduto.getTbItemEncomendaList();
            List<TbItemEncomenda> tbItemEncomendaListNew = tbProduto.getTbItemEncomendaList();
            List<ItemCompras> itemComprasListOld = persistentTbProduto.getItemComprasList();
            List<ItemCompras> itemComprasListNew = tbProduto.getItemComprasList();
            List<TbVasilhame> tbVasilhameListOld = persistentTbProduto.getTbVasilhameList();
            List<TbVasilhame> tbVasilhameListNew = tbProduto.getTbVasilhameList();
            List<TbItemSaidas> tbItemSaidasListOld = persistentTbProduto.getTbItemSaidasList();
            List<TbItemSaidas> tbItemSaidasListNew = tbProduto.getTbItemSaidasList();
            List<TbDesconto> tbDescontoListOld = persistentTbProduto.getTbDescontoList();
            List<TbDesconto> tbDescontoListNew = tbProduto.getTbDescontoList();
            List<TbItemProForma> tbItemProFormaListOld = persistentTbProduto.getTbItemProFormaList();
            List<TbItemProForma> tbItemProFormaListNew = tbProduto.getTbItemProFormaList();
            List<TbPreco> tbPrecoListOld = persistentTbProduto.getTbPrecoList();
            List<TbPreco> tbPrecoListNew = tbProduto.getTbPrecoList();
            List<ServicoRetencao> servicoRetencaoListOld = persistentTbProduto.getServicoRetencaoList();
            List<ServicoRetencao> servicoRetencaoListNew = tbProduto.getServicoRetencaoList();
            List<TbItemVenda> tbItemVendaListOld = persistentTbProduto.getTbItemVendaList();
            List<TbItemVenda> tbItemVendaListNew = tbProduto.getTbItemVendaList();
            List<TbItemEstorno> tbItemEstornoListOld = persistentTbProduto.getTbItemEstornoList();
            List<TbItemEstorno> tbItemEstornoListNew = tbProduto.getTbItemEstornoList();
            List<TbEntrada> tbEntradaListOld = persistentTbProduto.getTbEntradaList();
            List<TbEntrada> tbEntradaListNew = tbProduto.getTbEntradaList();
            List<TbStock> tbStockListOld = persistentTbProduto.getTbStockList();
            List<TbStock> tbStockListNew = tbProduto.getTbStockList();
            List<NotasItem> notasItemListOld = persistentTbProduto.getNotasItemList();
            List<NotasItem> notasItemListNew = tbProduto.getNotasItemList();
            List<NotasItemCompras> notasItemComprasListOld = persistentTbProduto.getNotasItemComprasList();
            List<NotasItemCompras> notasItemComprasListNew = tbProduto.getNotasItemComprasList();
            List<TbItemEntradas> tbItemEntradasListOld = persistentTbProduto.getTbItemEntradasList();
            List<TbItemEntradas> tbItemEntradasListNew = tbProduto.getTbItemEntradasList();
            List<String> illegalOrphanMessages = null;
            for ( ProdutoIsento produtoIsentoListOldProdutoIsento : produtoIsentoListOld )
            {
                if ( !produtoIsentoListNew.contains( produtoIsentoListOldProdutoIsento ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ProdutoIsento " + produtoIsentoListOldProdutoIsento + " since its fkProduto field is not nullable." );
                }
            }
            for ( TbStockMirrow tbStockMirrowListOldTbStockMirrow : tbStockMirrowListOld )
            {
                if ( !tbStockMirrowListNew.contains( tbStockMirrowListOldTbStockMirrow ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbStockMirrow " + tbStockMirrowListOldTbStockMirrow + " since its codProdutoCodigo field is not nullable." );
                }
            }
            for ( ProdutoImposto produtoImpostoListOldProdutoImposto : produtoImpostoListOld )
            {
                if ( !produtoImpostoListNew.contains( produtoImpostoListOldProdutoImposto ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ProdutoImposto " + produtoImpostoListOldProdutoImposto + " since its fkProduto field is not nullable." );
                }
            }
            for ( TbItemPedidos tbItemPedidosListOldTbItemPedidos : tbItemPedidosListOld )
            {
                if ( !tbItemPedidosListNew.contains( tbItemPedidosListOldTbItemPedidos ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemPedidos " + tbItemPedidosListOldTbItemPedidos + " since its fkProdutos field is not nullable." );
                }
            }
            for ( TbItemEncomenda tbItemEncomendaListOldTbItemEncomenda : tbItemEncomendaListOld )
            {
                if ( !tbItemEncomendaListNew.contains( tbItemEncomendaListOldTbItemEncomenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEncomenda " + tbItemEncomendaListOldTbItemEncomenda + " since its idProduto field is not nullable." );
                }
            }
            for ( ItemCompras itemComprasListOldItemCompras : itemComprasListOld )
            {
                if ( !itemComprasListNew.contains( itemComprasListOldItemCompras ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemCompras " + itemComprasListOldItemCompras + " since its fkProduto field is not nullable." );
                }
            }
            for ( TbItemSaidas tbItemSaidasListOldTbItemSaidas : tbItemSaidasListOld )
            {
                if ( !tbItemSaidasListNew.contains( tbItemSaidasListOldTbItemSaidas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemSaidas " + tbItemSaidasListOldTbItemSaidas + " since its fkProdutos field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbDesconto " + tbDescontoListOldTbDesconto + " since its fkProduto field is not nullable." );
                }
            }
            for ( TbItemProForma tbItemProFormaListOldTbItemProForma : tbItemProFormaListOld )
            {
                if ( !tbItemProFormaListNew.contains( tbItemProFormaListOldTbItemProForma ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemProForma " + tbItemProFormaListOldTbItemProForma + " since its fkProduto field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbPreco " + tbPrecoListOldTbPreco + " since its fkProduto field is not nullable." );
                }
            }
            for ( TbItemVenda tbItemVendaListOldTbItemVenda : tbItemVendaListOld )
            {
                if ( !tbItemVendaListNew.contains( tbItemVendaListOldTbItemVenda ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemVenda " + tbItemVendaListOldTbItemVenda + " since its codigoProduto field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbEntrada " + tbEntradaListOldTbEntrada + " since its idProduto field is not nullable." );
                }
            }
            for ( TbStock tbStockListOldTbStock : tbStockListOld )
            {
                if ( !tbStockListNew.contains( tbStockListOldTbStock ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbStock " + tbStockListOldTbStock + " since its codProdutoCodigo field is not nullable." );
                }
            }
            for ( TbItemEntradas tbItemEntradasListOldTbItemEntradas : tbItemEntradasListOld )
            {
                if ( !tbItemEntradasListNew.contains( tbItemEntradasListOldTbItemEntradas ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbItemEntradas " + tbItemEntradasListOldTbItemEntradas + " since its idProduto field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkGrupoNew != null )
            {
                fkGrupoNew = em.getReference( fkGrupoNew.getClass(), fkGrupoNew.getPkGrupo() );
                tbProduto.setFkGrupo( fkGrupoNew );
            }
            if ( fkModeloNew != null )
            {
                fkModeloNew = em.getReference( fkModeloNew.getClass(), fkModeloNew.getPkModelo() );
                tbProduto.setFkModelo( fkModeloNew );
            }
            if ( codFornecedoresNew != null )
            {
                codFornecedoresNew = em.getReference( codFornecedoresNew.getClass(), codFornecedoresNew.getCodigo() );
                tbProduto.setCodFornecedores( codFornecedoresNew );
            }
            if ( codLocalNew != null )
            {
                codLocalNew = em.getReference( codLocalNew.getClass(), codLocalNew.getCodigo() );
                tbProduto.setCodLocal( codLocalNew );
            }
            if ( codTipoProdutoNew != null )
            {
                codTipoProdutoNew = em.getReference( codTipoProdutoNew.getClass(), codTipoProdutoNew.getCodigo() );
                tbProduto.setCodTipoProduto( codTipoProdutoNew );
            }
            if ( codUnidadeNew != null )
            {
                codUnidadeNew = em.getReference( codUnidadeNew.getClass(), codUnidadeNew.getPkUnidade() );
                tbProduto.setCodUnidade( codUnidadeNew );
            }
            List<ProdutoIsento> attachedProdutoIsentoListNew = new ArrayList<ProdutoIsento>();
            for ( ProdutoIsento produtoIsentoListNewProdutoIsentoToAttach : produtoIsentoListNew )
            {
                produtoIsentoListNewProdutoIsentoToAttach = em.getReference( produtoIsentoListNewProdutoIsentoToAttach.getClass(), produtoIsentoListNewProdutoIsentoToAttach.getPkProdutoIsento() );
                attachedProdutoIsentoListNew.add( produtoIsentoListNewProdutoIsentoToAttach );
            }
            produtoIsentoListNew = attachedProdutoIsentoListNew;
            tbProduto.setProdutoIsentoList( produtoIsentoListNew );
            List<TbStockMirrow> attachedTbStockMirrowListNew = new ArrayList<TbStockMirrow>();
            for ( TbStockMirrow tbStockMirrowListNewTbStockMirrowToAttach : tbStockMirrowListNew )
            {
                tbStockMirrowListNewTbStockMirrowToAttach = em.getReference( tbStockMirrowListNewTbStockMirrowToAttach.getClass(), tbStockMirrowListNewTbStockMirrowToAttach.getCodigo() );
                attachedTbStockMirrowListNew.add( tbStockMirrowListNewTbStockMirrowToAttach );
            }
            tbStockMirrowListNew = attachedTbStockMirrowListNew;
            tbProduto.setTbStockMirrowList( tbStockMirrowListNew );
            List<ProdutoImposto> attachedProdutoImpostoListNew = new ArrayList<ProdutoImposto>();
            for ( ProdutoImposto produtoImpostoListNewProdutoImpostoToAttach : produtoImpostoListNew )
            {
                produtoImpostoListNewProdutoImpostoToAttach = em.getReference( produtoImpostoListNewProdutoImpostoToAttach.getClass(), produtoImpostoListNewProdutoImpostoToAttach.getPkProdutoImposto() );
                attachedProdutoImpostoListNew.add( produtoImpostoListNewProdutoImpostoToAttach );
            }
            produtoImpostoListNew = attachedProdutoImpostoListNew;
            tbProduto.setProdutoImpostoList( produtoImpostoListNew );
            List<TbItemPedidos> attachedTbItemPedidosListNew = new ArrayList<TbItemPedidos>();
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidosToAttach : tbItemPedidosListNew )
            {
                tbItemPedidosListNewTbItemPedidosToAttach = em.getReference( tbItemPedidosListNewTbItemPedidosToAttach.getClass(), tbItemPedidosListNewTbItemPedidosToAttach.getPkItemPedidos() );
                attachedTbItemPedidosListNew.add( tbItemPedidosListNewTbItemPedidosToAttach );
            }
            tbItemPedidosListNew = attachedTbItemPedidosListNew;
            tbProduto.setTbItemPedidosList( tbItemPedidosListNew );
            List<TbAcerto> attachedTbAcertoListNew = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListNewTbAcertoToAttach : tbAcertoListNew )
            {
                tbAcertoListNewTbAcertoToAttach = em.getReference( tbAcertoListNewTbAcertoToAttach.getClass(), tbAcertoListNewTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoListNew.add( tbAcertoListNewTbAcertoToAttach );
            }
            tbAcertoListNew = attachedTbAcertoListNew;
            tbProduto.setTbAcertoList( tbAcertoListNew );
            List<TbItemEncomenda> attachedTbItemEncomendaListNew = new ArrayList<TbItemEncomenda>();
            for ( TbItemEncomenda tbItemEncomendaListNewTbItemEncomendaToAttach : tbItemEncomendaListNew )
            {
                tbItemEncomendaListNewTbItemEncomendaToAttach = em.getReference( tbItemEncomendaListNewTbItemEncomendaToAttach.getClass(), tbItemEncomendaListNewTbItemEncomendaToAttach.getCodigo() );
                attachedTbItemEncomendaListNew.add( tbItemEncomendaListNewTbItemEncomendaToAttach );
            }
            tbItemEncomendaListNew = attachedTbItemEncomendaListNew;
            tbProduto.setTbItemEncomendaList( tbItemEncomendaListNew );
            List<ItemCompras> attachedItemComprasListNew = new ArrayList<ItemCompras>();
            for ( ItemCompras itemComprasListNewItemComprasToAttach : itemComprasListNew )
            {
                itemComprasListNewItemComprasToAttach = em.getReference( itemComprasListNewItemComprasToAttach.getClass(), itemComprasListNewItemComprasToAttach.getPkItmCompras() );
                attachedItemComprasListNew.add( itemComprasListNewItemComprasToAttach );
            }
            itemComprasListNew = attachedItemComprasListNew;
            tbProduto.setItemComprasList( itemComprasListNew );
            List<TbVasilhame> attachedTbVasilhameListNew = new ArrayList<TbVasilhame>();
            for ( TbVasilhame tbVasilhameListNewTbVasilhameToAttach : tbVasilhameListNew )
            {
                tbVasilhameListNewTbVasilhameToAttach = em.getReference( tbVasilhameListNewTbVasilhameToAttach.getClass(), tbVasilhameListNewTbVasilhameToAttach.getPkVasilhame() );
                attachedTbVasilhameListNew.add( tbVasilhameListNewTbVasilhameToAttach );
            }
            tbVasilhameListNew = attachedTbVasilhameListNew;
            tbProduto.setTbVasilhameList( tbVasilhameListNew );
            List<TbItemSaidas> attachedTbItemSaidasListNew = new ArrayList<TbItemSaidas>();
            for ( TbItemSaidas tbItemSaidasListNewTbItemSaidasToAttach : tbItemSaidasListNew )
            {
                tbItemSaidasListNewTbItemSaidasToAttach = em.getReference( tbItemSaidasListNewTbItemSaidasToAttach.getClass(), tbItemSaidasListNewTbItemSaidasToAttach.getCodigo() );
                attachedTbItemSaidasListNew.add( tbItemSaidasListNewTbItemSaidasToAttach );
            }
            tbItemSaidasListNew = attachedTbItemSaidasListNew;
            tbProduto.setTbItemSaidasList( tbItemSaidasListNew );
            List<TbDesconto> attachedTbDescontoListNew = new ArrayList<TbDesconto>();
            for ( TbDesconto tbDescontoListNewTbDescontoToAttach : tbDescontoListNew )
            {
                tbDescontoListNewTbDescontoToAttach = em.getReference( tbDescontoListNewTbDescontoToAttach.getClass(), tbDescontoListNewTbDescontoToAttach.getIdDesconto() );
                attachedTbDescontoListNew.add( tbDescontoListNewTbDescontoToAttach );
            }
            tbDescontoListNew = attachedTbDescontoListNew;
            tbProduto.setTbDescontoList( tbDescontoListNew );
            List<TbItemProForma> attachedTbItemProFormaListNew = new ArrayList<TbItemProForma>();
            for ( TbItemProForma tbItemProFormaListNewTbItemProFormaToAttach : tbItemProFormaListNew )
            {
                tbItemProFormaListNewTbItemProFormaToAttach = em.getReference( tbItemProFormaListNewTbItemProFormaToAttach.getClass(), tbItemProFormaListNewTbItemProFormaToAttach.getPkItemProForma() );
                attachedTbItemProFormaListNew.add( tbItemProFormaListNewTbItemProFormaToAttach );
            }
            tbItemProFormaListNew = attachedTbItemProFormaListNew;
            tbProduto.setTbItemProFormaList( tbItemProFormaListNew );
            List<TbPreco> attachedTbPrecoListNew = new ArrayList<TbPreco>();
            for ( TbPreco tbPrecoListNewTbPrecoToAttach : tbPrecoListNew )
            {
                tbPrecoListNewTbPrecoToAttach = em.getReference( tbPrecoListNewTbPrecoToAttach.getClass(), tbPrecoListNewTbPrecoToAttach.getPkPreco() );
                attachedTbPrecoListNew.add( tbPrecoListNewTbPrecoToAttach );
            }
            tbPrecoListNew = attachedTbPrecoListNew;
            tbProduto.setTbPrecoList( tbPrecoListNew );
            List<ServicoRetencao> attachedServicoRetencaoListNew = new ArrayList<ServicoRetencao>();
            for ( ServicoRetencao servicoRetencaoListNewServicoRetencaoToAttach : servicoRetencaoListNew )
            {
                servicoRetencaoListNewServicoRetencaoToAttach = em.getReference( servicoRetencaoListNewServicoRetencaoToAttach.getClass(), servicoRetencaoListNewServicoRetencaoToAttach.getPkServicoRetencao() );
                attachedServicoRetencaoListNew.add( servicoRetencaoListNewServicoRetencaoToAttach );
            }
            servicoRetencaoListNew = attachedServicoRetencaoListNew;
            tbProduto.setServicoRetencaoList( servicoRetencaoListNew );
            List<TbItemVenda> attachedTbItemVendaListNew = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListNewTbItemVendaToAttach : tbItemVendaListNew )
            {
                tbItemVendaListNewTbItemVendaToAttach = em.getReference( tbItemVendaListNewTbItemVendaToAttach.getClass(), tbItemVendaListNewTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaListNew.add( tbItemVendaListNewTbItemVendaToAttach );
            }
            tbItemVendaListNew = attachedTbItemVendaListNew;
            tbProduto.setTbItemVendaList( tbItemVendaListNew );
            List<TbItemEstorno> attachedTbItemEstornoListNew = new ArrayList<TbItemEstorno>();
            for ( TbItemEstorno tbItemEstornoListNewTbItemEstornoToAttach : tbItemEstornoListNew )
            {
                tbItemEstornoListNewTbItemEstornoToAttach = em.getReference( tbItemEstornoListNewTbItemEstornoToAttach.getClass(), tbItemEstornoListNewTbItemEstornoToAttach.getCodigo() );
                attachedTbItemEstornoListNew.add( tbItemEstornoListNewTbItemEstornoToAttach );
            }
            tbItemEstornoListNew = attachedTbItemEstornoListNew;
            tbProduto.setTbItemEstornoList( tbItemEstornoListNew );
            List<TbEntrada> attachedTbEntradaListNew = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListNewTbEntradaToAttach : tbEntradaListNew )
            {
                tbEntradaListNewTbEntradaToAttach = em.getReference( tbEntradaListNewTbEntradaToAttach.getClass(), tbEntradaListNewTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaListNew.add( tbEntradaListNewTbEntradaToAttach );
            }
            tbEntradaListNew = attachedTbEntradaListNew;
            tbProduto.setTbEntradaList( tbEntradaListNew );
            List<TbStock> attachedTbStockListNew = new ArrayList<TbStock>();
            for ( TbStock tbStockListNewTbStockToAttach : tbStockListNew )
            {
                tbStockListNewTbStockToAttach = em.getReference( tbStockListNewTbStockToAttach.getClass(), tbStockListNewTbStockToAttach.getCodigo() );
                attachedTbStockListNew.add( tbStockListNewTbStockToAttach );
            }
            tbStockListNew = attachedTbStockListNew;
            tbProduto.setTbStockList( tbStockListNew );
            List<NotasItem> attachedNotasItemListNew = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNewNotasItemToAttach : notasItemListNew )
            {
                notasItemListNewNotasItemToAttach = em.getReference( notasItemListNewNotasItemToAttach.getClass(), notasItemListNewNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemListNew.add( notasItemListNewNotasItemToAttach );
            }
            notasItemListNew = attachedNotasItemListNew;
            tbProduto.setNotasItemList( notasItemListNew );
            List<NotasItemCompras> attachedNotasItemComprasListNew = new ArrayList<NotasItemCompras>();
            for ( NotasItemCompras notasItemComprasListNewNotasItemComprasToAttach : notasItemComprasListNew )
            {
                notasItemComprasListNewNotasItemComprasToAttach = em.getReference( notasItemComprasListNewNotasItemComprasToAttach.getClass(), notasItemComprasListNewNotasItemComprasToAttach.getPkNotasItem() );
                attachedNotasItemComprasListNew.add( notasItemComprasListNewNotasItemComprasToAttach );
            }
            notasItemComprasListNew = attachedNotasItemComprasListNew;
            tbProduto.setNotasItemComprasList( notasItemComprasListNew );
            List<TbItemEntradas> attachedTbItemEntradasListNew = new ArrayList<TbItemEntradas>();
            for ( TbItemEntradas tbItemEntradasListNewTbItemEntradasToAttach : tbItemEntradasListNew )
            {
                tbItemEntradasListNewTbItemEntradasToAttach = em.getReference( tbItemEntradasListNewTbItemEntradasToAttach.getClass(), tbItemEntradasListNewTbItemEntradasToAttach.getIdItemEntradas() );
                attachedTbItemEntradasListNew.add( tbItemEntradasListNewTbItemEntradasToAttach );
            }
            tbItemEntradasListNew = attachedTbItemEntradasListNew;
            tbProduto.setTbItemEntradasList( tbItemEntradasListNew );
            tbProduto = em.merge( tbProduto );
            if ( fkGrupoOld != null && !fkGrupoOld.equals( fkGrupoNew ) )
            {
                fkGrupoOld.getTbProdutoList().remove( tbProduto );
                fkGrupoOld = em.merge( fkGrupoOld );
            }
            if ( fkGrupoNew != null && !fkGrupoNew.equals( fkGrupoOld ) )
            {
                fkGrupoNew.getTbProdutoList().add( tbProduto );
                fkGrupoNew = em.merge( fkGrupoNew );
            }
            if ( fkModeloOld != null && !fkModeloOld.equals( fkModeloNew ) )
            {
                fkModeloOld.getTbProdutoList().remove( tbProduto );
                fkModeloOld = em.merge( fkModeloOld );
            }
            if ( fkModeloNew != null && !fkModeloNew.equals( fkModeloOld ) )
            {
                fkModeloNew.getTbProdutoList().add( tbProduto );
                fkModeloNew = em.merge( fkModeloNew );
            }
            if ( codFornecedoresOld != null && !codFornecedoresOld.equals( codFornecedoresNew ) )
            {
                codFornecedoresOld.getTbProdutoList().remove( tbProduto );
                codFornecedoresOld = em.merge( codFornecedoresOld );
            }
            if ( codFornecedoresNew != null && !codFornecedoresNew.equals( codFornecedoresOld ) )
            {
                codFornecedoresNew.getTbProdutoList().add( tbProduto );
                codFornecedoresNew = em.merge( codFornecedoresNew );
            }
            if ( codLocalOld != null && !codLocalOld.equals( codLocalNew ) )
            {
                codLocalOld.getTbProdutoList().remove( tbProduto );
                codLocalOld = em.merge( codLocalOld );
            }
            if ( codLocalNew != null && !codLocalNew.equals( codLocalOld ) )
            {
                codLocalNew.getTbProdutoList().add( tbProduto );
                codLocalNew = em.merge( codLocalNew );
            }
            if ( codTipoProdutoOld != null && !codTipoProdutoOld.equals( codTipoProdutoNew ) )
            {
                codTipoProdutoOld.getTbProdutoList().remove( tbProduto );
                codTipoProdutoOld = em.merge( codTipoProdutoOld );
            }
            if ( codTipoProdutoNew != null && !codTipoProdutoNew.equals( codTipoProdutoOld ) )
            {
                codTipoProdutoNew.getTbProdutoList().add( tbProduto );
                codTipoProdutoNew = em.merge( codTipoProdutoNew );
            }
            if ( codUnidadeOld != null && !codUnidadeOld.equals( codUnidadeNew ) )
            {
                codUnidadeOld.getTbProdutoList().remove( tbProduto );
                codUnidadeOld = em.merge( codUnidadeOld );
            }
            if ( codUnidadeNew != null && !codUnidadeNew.equals( codUnidadeOld ) )
            {
                codUnidadeNew.getTbProdutoList().add( tbProduto );
                codUnidadeNew = em.merge( codUnidadeNew );
            }
            for ( ProdutoIsento produtoIsentoListNewProdutoIsento : produtoIsentoListNew )
            {
                if ( !produtoIsentoListOld.contains( produtoIsentoListNewProdutoIsento ) )
                {
                    TbProduto oldFkProdutoOfProdutoIsentoListNewProdutoIsento = produtoIsentoListNewProdutoIsento.getFkProduto();
                    produtoIsentoListNewProdutoIsento.setFkProduto( tbProduto );
                    produtoIsentoListNewProdutoIsento = em.merge( produtoIsentoListNewProdutoIsento );
                    if ( oldFkProdutoOfProdutoIsentoListNewProdutoIsento != null && !oldFkProdutoOfProdutoIsentoListNewProdutoIsento.equals( tbProduto ) )
                    {
                        oldFkProdutoOfProdutoIsentoListNewProdutoIsento.getProdutoIsentoList().remove( produtoIsentoListNewProdutoIsento );
                        oldFkProdutoOfProdutoIsentoListNewProdutoIsento = em.merge( oldFkProdutoOfProdutoIsentoListNewProdutoIsento );
                    }
                }
            }
            for ( TbStockMirrow tbStockMirrowListNewTbStockMirrow : tbStockMirrowListNew )
            {
                if ( !tbStockMirrowListOld.contains( tbStockMirrowListNewTbStockMirrow ) )
                {
                    TbProduto oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow = tbStockMirrowListNewTbStockMirrow.getCodProdutoCodigo();
                    tbStockMirrowListNewTbStockMirrow.setCodProdutoCodigo( tbProduto );
                    tbStockMirrowListNewTbStockMirrow = em.merge( tbStockMirrowListNewTbStockMirrow );
                    if ( oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow != null && !oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow.equals( tbProduto ) )
                    {
                        oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow.getTbStockMirrowList().remove( tbStockMirrowListNewTbStockMirrow );
                        oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow = em.merge( oldCodProdutoCodigoOfTbStockMirrowListNewTbStockMirrow );
                    }
                }
            }
            for ( ProdutoImposto produtoImpostoListNewProdutoImposto : produtoImpostoListNew )
            {
                if ( !produtoImpostoListOld.contains( produtoImpostoListNewProdutoImposto ) )
                {
                    TbProduto oldFkProdutoOfProdutoImpostoListNewProdutoImposto = produtoImpostoListNewProdutoImposto.getFkProduto();
                    produtoImpostoListNewProdutoImposto.setFkProduto( tbProduto );
                    produtoImpostoListNewProdutoImposto = em.merge( produtoImpostoListNewProdutoImposto );
                    if ( oldFkProdutoOfProdutoImpostoListNewProdutoImposto != null && !oldFkProdutoOfProdutoImpostoListNewProdutoImposto.equals( tbProduto ) )
                    {
                        oldFkProdutoOfProdutoImpostoListNewProdutoImposto.getProdutoImpostoList().remove( produtoImpostoListNewProdutoImposto );
                        oldFkProdutoOfProdutoImpostoListNewProdutoImposto = em.merge( oldFkProdutoOfProdutoImpostoListNewProdutoImposto );
                    }
                }
            }
            for ( TbItemPedidos tbItemPedidosListNewTbItemPedidos : tbItemPedidosListNew )
            {
                if ( !tbItemPedidosListOld.contains( tbItemPedidosListNewTbItemPedidos ) )
                {
                    TbProduto oldFkProdutosOfTbItemPedidosListNewTbItemPedidos = tbItemPedidosListNewTbItemPedidos.getFkProdutos();
                    tbItemPedidosListNewTbItemPedidos.setFkProdutos( tbProduto );
                    tbItemPedidosListNewTbItemPedidos = em.merge( tbItemPedidosListNewTbItemPedidos );
                    if ( oldFkProdutosOfTbItemPedidosListNewTbItemPedidos != null && !oldFkProdutosOfTbItemPedidosListNewTbItemPedidos.equals( tbProduto ) )
                    {
                        oldFkProdutosOfTbItemPedidosListNewTbItemPedidos.getTbItemPedidosList().remove( tbItemPedidosListNewTbItemPedidos );
                        oldFkProdutosOfTbItemPedidosListNewTbItemPedidos = em.merge( oldFkProdutosOfTbItemPedidosListNewTbItemPedidos );
                    }
                }
            }
            for ( TbAcerto tbAcertoListOldTbAcerto : tbAcertoListOld )
            {
                if ( !tbAcertoListNew.contains( tbAcertoListOldTbAcerto ) )
                {
                    tbAcertoListOldTbAcerto.setIdProduto( null );
                    tbAcertoListOldTbAcerto = em.merge( tbAcertoListOldTbAcerto );
                }
            }
            for ( TbAcerto tbAcertoListNewTbAcerto : tbAcertoListNew )
            {
                if ( !tbAcertoListOld.contains( tbAcertoListNewTbAcerto ) )
                {
                    TbProduto oldIdProdutoOfTbAcertoListNewTbAcerto = tbAcertoListNewTbAcerto.getIdProduto();
                    tbAcertoListNewTbAcerto.setIdProduto( tbProduto );
                    tbAcertoListNewTbAcerto = em.merge( tbAcertoListNewTbAcerto );
                    if ( oldIdProdutoOfTbAcertoListNewTbAcerto != null && !oldIdProdutoOfTbAcertoListNewTbAcerto.equals( tbProduto ) )
                    {
                        oldIdProdutoOfTbAcertoListNewTbAcerto.getTbAcertoList().remove( tbAcertoListNewTbAcerto );
                        oldIdProdutoOfTbAcertoListNewTbAcerto = em.merge( oldIdProdutoOfTbAcertoListNewTbAcerto );
                    }
                }
            }
            for ( TbItemEncomenda tbItemEncomendaListNewTbItemEncomenda : tbItemEncomendaListNew )
            {
                if ( !tbItemEncomendaListOld.contains( tbItemEncomendaListNewTbItemEncomenda ) )
                {
                    TbProduto oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda = tbItemEncomendaListNewTbItemEncomenda.getIdProduto();
                    tbItemEncomendaListNewTbItemEncomenda.setIdProduto( tbProduto );
                    tbItemEncomendaListNewTbItemEncomenda = em.merge( tbItemEncomendaListNewTbItemEncomenda );
                    if ( oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda != null && !oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda.equals( tbProduto ) )
                    {
                        oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda.getTbItemEncomendaList().remove( tbItemEncomendaListNewTbItemEncomenda );
                        oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda = em.merge( oldIdProdutoOfTbItemEncomendaListNewTbItemEncomenda );
                    }
                }
            }
            for ( ItemCompras itemComprasListNewItemCompras : itemComprasListNew )
            {
                if ( !itemComprasListOld.contains( itemComprasListNewItemCompras ) )
                {
                    TbProduto oldFkProdutoOfItemComprasListNewItemCompras = itemComprasListNewItemCompras.getFkProduto();
                    itemComprasListNewItemCompras.setFkProduto( tbProduto );
                    itemComprasListNewItemCompras = em.merge( itemComprasListNewItemCompras );
                    if ( oldFkProdutoOfItemComprasListNewItemCompras != null && !oldFkProdutoOfItemComprasListNewItemCompras.equals( tbProduto ) )
                    {
                        oldFkProdutoOfItemComprasListNewItemCompras.getItemComprasList().remove( itemComprasListNewItemCompras );
                        oldFkProdutoOfItemComprasListNewItemCompras = em.merge( oldFkProdutoOfItemComprasListNewItemCompras );
                    }
                }
            }
            for ( TbVasilhame tbVasilhameListOldTbVasilhame : tbVasilhameListOld )
            {
                if ( !tbVasilhameListNew.contains( tbVasilhameListOldTbVasilhame ) )
                {
                    tbVasilhameListOldTbVasilhame.setFkProduto( null );
                    tbVasilhameListOldTbVasilhame = em.merge( tbVasilhameListOldTbVasilhame );
                }
            }
            for ( TbVasilhame tbVasilhameListNewTbVasilhame : tbVasilhameListNew )
            {
                if ( !tbVasilhameListOld.contains( tbVasilhameListNewTbVasilhame ) )
                {
                    TbProduto oldFkProdutoOfTbVasilhameListNewTbVasilhame = tbVasilhameListNewTbVasilhame.getFkProduto();
                    tbVasilhameListNewTbVasilhame.setFkProduto( tbProduto );
                    tbVasilhameListNewTbVasilhame = em.merge( tbVasilhameListNewTbVasilhame );
                    if ( oldFkProdutoOfTbVasilhameListNewTbVasilhame != null && !oldFkProdutoOfTbVasilhameListNewTbVasilhame.equals( tbProduto ) )
                    {
                        oldFkProdutoOfTbVasilhameListNewTbVasilhame.getTbVasilhameList().remove( tbVasilhameListNewTbVasilhame );
                        oldFkProdutoOfTbVasilhameListNewTbVasilhame = em.merge( oldFkProdutoOfTbVasilhameListNewTbVasilhame );
                    }
                }
            }
            for ( TbItemSaidas tbItemSaidasListNewTbItemSaidas : tbItemSaidasListNew )
            {
                if ( !tbItemSaidasListOld.contains( tbItemSaidasListNewTbItemSaidas ) )
                {
                    TbProduto oldFkProdutosOfTbItemSaidasListNewTbItemSaidas = tbItemSaidasListNewTbItemSaidas.getFkProdutos();
                    tbItemSaidasListNewTbItemSaidas.setFkProdutos( tbProduto );
                    tbItemSaidasListNewTbItemSaidas = em.merge( tbItemSaidasListNewTbItemSaidas );
                    if ( oldFkProdutosOfTbItemSaidasListNewTbItemSaidas != null && !oldFkProdutosOfTbItemSaidasListNewTbItemSaidas.equals( tbProduto ) )
                    {
                        oldFkProdutosOfTbItemSaidasListNewTbItemSaidas.getTbItemSaidasList().remove( tbItemSaidasListNewTbItemSaidas );
                        oldFkProdutosOfTbItemSaidasListNewTbItemSaidas = em.merge( oldFkProdutosOfTbItemSaidasListNewTbItemSaidas );
                    }
                }
            }
            for ( TbDesconto tbDescontoListNewTbDesconto : tbDescontoListNew )
            {
                if ( !tbDescontoListOld.contains( tbDescontoListNewTbDesconto ) )
                {
                    TbProduto oldFkProdutoOfTbDescontoListNewTbDesconto = tbDescontoListNewTbDesconto.getFkProduto();
                    tbDescontoListNewTbDesconto.setFkProduto( tbProduto );
                    tbDescontoListNewTbDesconto = em.merge( tbDescontoListNewTbDesconto );
                    if ( oldFkProdutoOfTbDescontoListNewTbDesconto != null && !oldFkProdutoOfTbDescontoListNewTbDesconto.equals( tbProduto ) )
                    {
                        oldFkProdutoOfTbDescontoListNewTbDesconto.getTbDescontoList().remove( tbDescontoListNewTbDesconto );
                        oldFkProdutoOfTbDescontoListNewTbDesconto = em.merge( oldFkProdutoOfTbDescontoListNewTbDesconto );
                    }
                }
            }
            for ( TbItemProForma tbItemProFormaListNewTbItemProForma : tbItemProFormaListNew )
            {
                if ( !tbItemProFormaListOld.contains( tbItemProFormaListNewTbItemProForma ) )
                {
                    TbProduto oldFkProdutoOfTbItemProFormaListNewTbItemProForma = tbItemProFormaListNewTbItemProForma.getFkProduto();
                    tbItemProFormaListNewTbItemProForma.setFkProduto( tbProduto );
                    tbItemProFormaListNewTbItemProForma = em.merge( tbItemProFormaListNewTbItemProForma );
                    if ( oldFkProdutoOfTbItemProFormaListNewTbItemProForma != null && !oldFkProdutoOfTbItemProFormaListNewTbItemProForma.equals( tbProduto ) )
                    {
                        oldFkProdutoOfTbItemProFormaListNewTbItemProForma.getTbItemProFormaList().remove( tbItemProFormaListNewTbItemProForma );
                        oldFkProdutoOfTbItemProFormaListNewTbItemProForma = em.merge( oldFkProdutoOfTbItemProFormaListNewTbItemProForma );
                    }
                }
            }
            for ( TbPreco tbPrecoListNewTbPreco : tbPrecoListNew )
            {
                if ( !tbPrecoListOld.contains( tbPrecoListNewTbPreco ) )
                {
                    TbProduto oldFkProdutoOfTbPrecoListNewTbPreco = tbPrecoListNewTbPreco.getFkProduto();
                    tbPrecoListNewTbPreco.setFkProduto( tbProduto );
                    tbPrecoListNewTbPreco = em.merge( tbPrecoListNewTbPreco );
                    if ( oldFkProdutoOfTbPrecoListNewTbPreco != null && !oldFkProdutoOfTbPrecoListNewTbPreco.equals( tbProduto ) )
                    {
                        oldFkProdutoOfTbPrecoListNewTbPreco.getTbPrecoList().remove( tbPrecoListNewTbPreco );
                        oldFkProdutoOfTbPrecoListNewTbPreco = em.merge( oldFkProdutoOfTbPrecoListNewTbPreco );
                    }
                }
            }
            for ( ServicoRetencao servicoRetencaoListOldServicoRetencao : servicoRetencaoListOld )
            {
                if ( !servicoRetencaoListNew.contains( servicoRetencaoListOldServicoRetencao ) )
                {
                    servicoRetencaoListOldServicoRetencao.setFkProduto( null );
                    servicoRetencaoListOldServicoRetencao = em.merge( servicoRetencaoListOldServicoRetencao );
                }
            }
            for ( ServicoRetencao servicoRetencaoListNewServicoRetencao : servicoRetencaoListNew )
            {
                if ( !servicoRetencaoListOld.contains( servicoRetencaoListNewServicoRetencao ) )
                {
                    TbProduto oldFkProdutoOfServicoRetencaoListNewServicoRetencao = servicoRetencaoListNewServicoRetencao.getFkProduto();
                    servicoRetencaoListNewServicoRetencao.setFkProduto( tbProduto );
                    servicoRetencaoListNewServicoRetencao = em.merge( servicoRetencaoListNewServicoRetencao );
                    if ( oldFkProdutoOfServicoRetencaoListNewServicoRetencao != null && !oldFkProdutoOfServicoRetencaoListNewServicoRetencao.equals( tbProduto ) )
                    {
                        oldFkProdutoOfServicoRetencaoListNewServicoRetencao.getServicoRetencaoList().remove( servicoRetencaoListNewServicoRetencao );
                        oldFkProdutoOfServicoRetencaoListNewServicoRetencao = em.merge( oldFkProdutoOfServicoRetencaoListNewServicoRetencao );
                    }
                }
            }
            for ( TbItemVenda tbItemVendaListNewTbItemVenda : tbItemVendaListNew )
            {
                if ( !tbItemVendaListOld.contains( tbItemVendaListNewTbItemVenda ) )
                {
                    TbProduto oldCodigoProdutoOfTbItemVendaListNewTbItemVenda = tbItemVendaListNewTbItemVenda.getCodigoProduto();
                    tbItemVendaListNewTbItemVenda.setCodigoProduto( tbProduto );
                    tbItemVendaListNewTbItemVenda = em.merge( tbItemVendaListNewTbItemVenda );
                    if ( oldCodigoProdutoOfTbItemVendaListNewTbItemVenda != null && !oldCodigoProdutoOfTbItemVendaListNewTbItemVenda.equals( tbProduto ) )
                    {
                        oldCodigoProdutoOfTbItemVendaListNewTbItemVenda.getTbItemVendaList().remove( tbItemVendaListNewTbItemVenda );
                        oldCodigoProdutoOfTbItemVendaListNewTbItemVenda = em.merge( oldCodigoProdutoOfTbItemVendaListNewTbItemVenda );
                    }
                }
            }
            for ( TbItemEstorno tbItemEstornoListOldTbItemEstorno : tbItemEstornoListOld )
            {
                if ( !tbItemEstornoListNew.contains( tbItemEstornoListOldTbItemEstorno ) )
                {
                    tbItemEstornoListOldTbItemEstorno.setFkProdutos( null );
                    tbItemEstornoListOldTbItemEstorno = em.merge( tbItemEstornoListOldTbItemEstorno );
                }
            }
            for ( TbItemEstorno tbItemEstornoListNewTbItemEstorno : tbItemEstornoListNew )
            {
                if ( !tbItemEstornoListOld.contains( tbItemEstornoListNewTbItemEstorno ) )
                {
                    TbProduto oldFkProdutosOfTbItemEstornoListNewTbItemEstorno = tbItemEstornoListNewTbItemEstorno.getFkProdutos();
                    tbItemEstornoListNewTbItemEstorno.setFkProdutos( tbProduto );
                    tbItemEstornoListNewTbItemEstorno = em.merge( tbItemEstornoListNewTbItemEstorno );
                    if ( oldFkProdutosOfTbItemEstornoListNewTbItemEstorno != null && !oldFkProdutosOfTbItemEstornoListNewTbItemEstorno.equals( tbProduto ) )
                    {
                        oldFkProdutosOfTbItemEstornoListNewTbItemEstorno.getTbItemEstornoList().remove( tbItemEstornoListNewTbItemEstorno );
                        oldFkProdutosOfTbItemEstornoListNewTbItemEstorno = em.merge( oldFkProdutosOfTbItemEstornoListNewTbItemEstorno );
                    }
                }
            }
            for ( TbEntrada tbEntradaListNewTbEntrada : tbEntradaListNew )
            {
                if ( !tbEntradaListOld.contains( tbEntradaListNewTbEntrada ) )
                {
                    TbProduto oldIdProdutoOfTbEntradaListNewTbEntrada = tbEntradaListNewTbEntrada.getIdProduto();
                    tbEntradaListNewTbEntrada.setIdProduto( tbProduto );
                    tbEntradaListNewTbEntrada = em.merge( tbEntradaListNewTbEntrada );
                    if ( oldIdProdutoOfTbEntradaListNewTbEntrada != null && !oldIdProdutoOfTbEntradaListNewTbEntrada.equals( tbProduto ) )
                    {
                        oldIdProdutoOfTbEntradaListNewTbEntrada.getTbEntradaList().remove( tbEntradaListNewTbEntrada );
                        oldIdProdutoOfTbEntradaListNewTbEntrada = em.merge( oldIdProdutoOfTbEntradaListNewTbEntrada );
                    }
                }
            }
            for ( TbStock tbStockListNewTbStock : tbStockListNew )
            {
                if ( !tbStockListOld.contains( tbStockListNewTbStock ) )
                {
                    TbProduto oldCodProdutoCodigoOfTbStockListNewTbStock = tbStockListNewTbStock.getCodProdutoCodigo();
                    tbStockListNewTbStock.setCodProdutoCodigo( tbProduto );
                    tbStockListNewTbStock = em.merge( tbStockListNewTbStock );
                    if ( oldCodProdutoCodigoOfTbStockListNewTbStock != null && !oldCodProdutoCodigoOfTbStockListNewTbStock.equals( tbProduto ) )
                    {
                        oldCodProdutoCodigoOfTbStockListNewTbStock.getTbStockList().remove( tbStockListNewTbStock );
                        oldCodProdutoCodigoOfTbStockListNewTbStock = em.merge( oldCodProdutoCodigoOfTbStockListNewTbStock );
                    }
                }
            }
            for ( NotasItem notasItemListOldNotasItem : notasItemListOld )
            {
                if ( !notasItemListNew.contains( notasItemListOldNotasItem ) )
                {
                    notasItemListOldNotasItem.setFkProduto( null );
                    notasItemListOldNotasItem = em.merge( notasItemListOldNotasItem );
                }
            }
            for ( NotasItem notasItemListNewNotasItem : notasItemListNew )
            {
                if ( !notasItemListOld.contains( notasItemListNewNotasItem ) )
                {
                    TbProduto oldFkProdutoOfNotasItemListNewNotasItem = notasItemListNewNotasItem.getFkProduto();
                    notasItemListNewNotasItem.setFkProduto( tbProduto );
                    notasItemListNewNotasItem = em.merge( notasItemListNewNotasItem );
                    if ( oldFkProdutoOfNotasItemListNewNotasItem != null && !oldFkProdutoOfNotasItemListNewNotasItem.equals( tbProduto ) )
                    {
                        oldFkProdutoOfNotasItemListNewNotasItem.getNotasItemList().remove( notasItemListNewNotasItem );
                        oldFkProdutoOfNotasItemListNewNotasItem = em.merge( oldFkProdutoOfNotasItemListNewNotasItem );
                    }
                }
            }
            for ( NotasItemCompras notasItemComprasListOldNotasItemCompras : notasItemComprasListOld )
            {
                if ( !notasItemComprasListNew.contains( notasItemComprasListOldNotasItemCompras ) )
                {
                    notasItemComprasListOldNotasItemCompras.setFkProduto( null );
                    notasItemComprasListOldNotasItemCompras = em.merge( notasItemComprasListOldNotasItemCompras );
                }
            }
            for ( NotasItemCompras notasItemComprasListNewNotasItemCompras : notasItemComprasListNew )
            {
                if ( !notasItemComprasListOld.contains( notasItemComprasListNewNotasItemCompras ) )
                {
                    TbProduto oldFkProdutoOfNotasItemComprasListNewNotasItemCompras = notasItemComprasListNewNotasItemCompras.getFkProduto();
                    notasItemComprasListNewNotasItemCompras.setFkProduto( tbProduto );
                    notasItemComprasListNewNotasItemCompras = em.merge( notasItemComprasListNewNotasItemCompras );
                    if ( oldFkProdutoOfNotasItemComprasListNewNotasItemCompras != null && !oldFkProdutoOfNotasItemComprasListNewNotasItemCompras.equals( tbProduto ) )
                    {
                        oldFkProdutoOfNotasItemComprasListNewNotasItemCompras.getNotasItemComprasList().remove( notasItemComprasListNewNotasItemCompras );
                        oldFkProdutoOfNotasItemComprasListNewNotasItemCompras = em.merge( oldFkProdutoOfNotasItemComprasListNewNotasItemCompras );
                    }
                }
            }
            for ( TbItemEntradas tbItemEntradasListNewTbItemEntradas : tbItemEntradasListNew )
            {
                if ( !tbItemEntradasListOld.contains( tbItemEntradasListNewTbItemEntradas ) )
                {
                    TbProduto oldIdProdutoOfTbItemEntradasListNewTbItemEntradas = tbItemEntradasListNewTbItemEntradas.getIdProduto();
                    tbItemEntradasListNewTbItemEntradas.setIdProduto( tbProduto );
                    tbItemEntradasListNewTbItemEntradas = em.merge( tbItemEntradasListNewTbItemEntradas );
                    if ( oldIdProdutoOfTbItemEntradasListNewTbItemEntradas != null && !oldIdProdutoOfTbItemEntradasListNewTbItemEntradas.equals( tbProduto ) )
                    {
                        oldIdProdutoOfTbItemEntradasListNewTbItemEntradas.getTbItemEntradasList().remove( tbItemEntradasListNewTbItemEntradas );
                        oldIdProdutoOfTbItemEntradasListNewTbItemEntradas = em.merge( oldIdProdutoOfTbItemEntradasListNewTbItemEntradas );
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
                Integer id = tbProduto.getCodigo();
                if ( findTbProduto( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbProduto with id " + id + " no longer exists." );
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
            TbProduto tbProduto;
            try
            {
                tbProduto = em.getReference( TbProduto.class, id );
                tbProduto.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbProduto with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ProdutoIsento> produtoIsentoListOrphanCheck = tbProduto.getProdutoIsentoList();
            for ( ProdutoIsento produtoIsentoListOrphanCheckProdutoIsento : produtoIsentoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the ProdutoIsento " + produtoIsentoListOrphanCheckProdutoIsento + " in its produtoIsentoList field has a non-nullable fkProduto field." );
            }
            List<TbStockMirrow> tbStockMirrowListOrphanCheck = tbProduto.getTbStockMirrowList();
            for ( TbStockMirrow tbStockMirrowListOrphanCheckTbStockMirrow : tbStockMirrowListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbStockMirrow " + tbStockMirrowListOrphanCheckTbStockMirrow + " in its tbStockMirrowList field has a non-nullable codProdutoCodigo field." );
            }
            List<ProdutoImposto> produtoImpostoListOrphanCheck = tbProduto.getProdutoImpostoList();
            for ( ProdutoImposto produtoImpostoListOrphanCheckProdutoImposto : produtoImpostoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the ProdutoImposto " + produtoImpostoListOrphanCheckProdutoImposto + " in its produtoImpostoList field has a non-nullable fkProduto field." );
            }
            List<TbItemPedidos> tbItemPedidosListOrphanCheck = tbProduto.getTbItemPedidosList();
            for ( TbItemPedidos tbItemPedidosListOrphanCheckTbItemPedidos : tbItemPedidosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemPedidos " + tbItemPedidosListOrphanCheckTbItemPedidos + " in its tbItemPedidosList field has a non-nullable fkProdutos field." );
            }
            List<TbItemEncomenda> tbItemEncomendaListOrphanCheck = tbProduto.getTbItemEncomendaList();
            for ( TbItemEncomenda tbItemEncomendaListOrphanCheckTbItemEncomenda : tbItemEncomendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemEncomenda " + tbItemEncomendaListOrphanCheckTbItemEncomenda + " in its tbItemEncomendaList field has a non-nullable idProduto field." );
            }
            List<ItemCompras> itemComprasListOrphanCheck = tbProduto.getItemComprasList();
            for ( ItemCompras itemComprasListOrphanCheckItemCompras : itemComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the ItemCompras " + itemComprasListOrphanCheckItemCompras + " in its itemComprasList field has a non-nullable fkProduto field." );
            }
            List<TbItemSaidas> tbItemSaidasListOrphanCheck = tbProduto.getTbItemSaidasList();
            for ( TbItemSaidas tbItemSaidasListOrphanCheckTbItemSaidas : tbItemSaidasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemSaidas " + tbItemSaidasListOrphanCheckTbItemSaidas + " in its tbItemSaidasList field has a non-nullable fkProdutos field." );
            }
            List<TbDesconto> tbDescontoListOrphanCheck = tbProduto.getTbDescontoList();
            for ( TbDesconto tbDescontoListOrphanCheckTbDesconto : tbDescontoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbDesconto " + tbDescontoListOrphanCheckTbDesconto + " in its tbDescontoList field has a non-nullable fkProduto field." );
            }
            List<TbItemProForma> tbItemProFormaListOrphanCheck = tbProduto.getTbItemProFormaList();
            for ( TbItemProForma tbItemProFormaListOrphanCheckTbItemProForma : tbItemProFormaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemProForma " + tbItemProFormaListOrphanCheckTbItemProForma + " in its tbItemProFormaList field has a non-nullable fkProduto field." );
            }
            List<TbPreco> tbPrecoListOrphanCheck = tbProduto.getTbPrecoList();
            for ( TbPreco tbPrecoListOrphanCheckTbPreco : tbPrecoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbPreco " + tbPrecoListOrphanCheckTbPreco + " in its tbPrecoList field has a non-nullable fkProduto field." );
            }
            List<TbItemVenda> tbItemVendaListOrphanCheck = tbProduto.getTbItemVendaList();
            for ( TbItemVenda tbItemVendaListOrphanCheckTbItemVenda : tbItemVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemVenda " + tbItemVendaListOrphanCheckTbItemVenda + " in its tbItemVendaList field has a non-nullable codigoProduto field." );
            }
            List<TbEntrada> tbEntradaListOrphanCheck = tbProduto.getTbEntradaList();
            for ( TbEntrada tbEntradaListOrphanCheckTbEntrada : tbEntradaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbEntrada " + tbEntradaListOrphanCheckTbEntrada + " in its tbEntradaList field has a non-nullable idProduto field." );
            }
            List<TbStock> tbStockListOrphanCheck = tbProduto.getTbStockList();
            for ( TbStock tbStockListOrphanCheckTbStock : tbStockListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbStock " + tbStockListOrphanCheckTbStock + " in its tbStockList field has a non-nullable codProdutoCodigo field." );
            }
            List<TbItemEntradas> tbItemEntradasListOrphanCheck = tbProduto.getTbItemEntradasList();
            for ( TbItemEntradas tbItemEntradasListOrphanCheckTbItemEntradas : tbItemEntradasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbProduto (" + tbProduto + ") cannot be destroyed since the TbItemEntradas " + tbItemEntradasListOrphanCheckTbItemEntradas + " in its tbItemEntradasList field has a non-nullable idProduto field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            Grupo fkGrupo = tbProduto.getFkGrupo();
            if ( fkGrupo != null )
            {
                fkGrupo.getTbProdutoList().remove( tbProduto );
                fkGrupo = em.merge( fkGrupo );
            }
            Modelo fkModelo = tbProduto.getFkModelo();
            if ( fkModelo != null )
            {
                fkModelo.getTbProdutoList().remove( tbProduto );
                fkModelo = em.merge( fkModelo );
            }
            TbFornecedor codFornecedores = tbProduto.getCodFornecedores();
            if ( codFornecedores != null )
            {
                codFornecedores.getTbProdutoList().remove( tbProduto );
                codFornecedores = em.merge( codFornecedores );
            }
            TbLocal codLocal = tbProduto.getCodLocal();
            if ( codLocal != null )
            {
                codLocal.getTbProdutoList().remove( tbProduto );
                codLocal = em.merge( codLocal );
            }
            TbTipoProduto codTipoProduto = tbProduto.getCodTipoProduto();
            if ( codTipoProduto != null )
            {
                codTipoProduto.getTbProdutoList().remove( tbProduto );
                codTipoProduto = em.merge( codTipoProduto );
            }
            Unidade codUnidade = tbProduto.getCodUnidade();
            if ( codUnidade != null )
            {
                codUnidade.getTbProdutoList().remove( tbProduto );
                codUnidade = em.merge( codUnidade );
            }
            List<TbAcerto> tbAcertoList = tbProduto.getTbAcertoList();
            for ( TbAcerto tbAcertoListTbAcerto : tbAcertoList )
            {
                tbAcertoListTbAcerto.setIdProduto( null );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
            }
            List<TbVasilhame> tbVasilhameList = tbProduto.getTbVasilhameList();
            for ( TbVasilhame tbVasilhameListTbVasilhame : tbVasilhameList )
            {
                tbVasilhameListTbVasilhame.setFkProduto( null );
                tbVasilhameListTbVasilhame = em.merge( tbVasilhameListTbVasilhame );
            }
            List<ServicoRetencao> servicoRetencaoList = tbProduto.getServicoRetencaoList();
            for ( ServicoRetencao servicoRetencaoListServicoRetencao : servicoRetencaoList )
            {
                servicoRetencaoListServicoRetencao.setFkProduto( null );
                servicoRetencaoListServicoRetencao = em.merge( servicoRetencaoListServicoRetencao );
            }
            List<TbItemEstorno> tbItemEstornoList = tbProduto.getTbItemEstornoList();
            for ( TbItemEstorno tbItemEstornoListTbItemEstorno : tbItemEstornoList )
            {
                tbItemEstornoListTbItemEstorno.setFkProdutos( null );
                tbItemEstornoListTbItemEstorno = em.merge( tbItemEstornoListTbItemEstorno );
            }
            List<NotasItem> notasItemList = tbProduto.getNotasItemList();
            for ( NotasItem notasItemListNotasItem : notasItemList )
            {
                notasItemListNotasItem.setFkProduto( null );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
            }
            List<NotasItemCompras> notasItemComprasList = tbProduto.getNotasItemComprasList();
            for ( NotasItemCompras notasItemComprasListNotasItemCompras : notasItemComprasList )
            {
                notasItemComprasListNotasItemCompras.setFkProduto( null );
                notasItemComprasListNotasItemCompras = em.merge( notasItemComprasListNotasItemCompras );
            }
            em.remove( tbProduto );
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

    public List<TbProduto> findTbProdutoEntities()
    {
        return findTbProdutoEntities( true, -1, -1 );
    }

    public List<TbProduto> findTbProdutoEntities( int maxResults, int firstResult )
    {
        return findTbProdutoEntities( false, maxResults, firstResult );
    }

    private List<TbProduto> findTbProdutoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbProduto.class ) );
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

    public TbProduto findTbProduto( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbProduto.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbProdutoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbProduto> rt = cq.from( TbProduto.class );
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
