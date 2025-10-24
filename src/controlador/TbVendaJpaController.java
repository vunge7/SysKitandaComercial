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
import entity.TbBanco;
import entity.TbArmazem;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
import entity.TbUsuario;
import entity.TbCliente;
import entity.Amortizacao;
import java.util.ArrayList;
import java.util.List;
import entity.TbPagamentoCredito;
import entity.TbItemVenda;
import entity.FormaPagamentoItem;
import entity.NotasItem;
import entity.AmortizacaoDivida;
import entity.TbVenda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbVendaJpaController implements Serializable
{

    public TbVendaJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbVenda tbVenda )
    {
        if ( tbVenda.getAmortizacaoList() == null )
        {
            tbVenda.setAmortizacaoList( new ArrayList<Amortizacao>() );
        }
        if ( tbVenda.getTbPagamentoCreditoList() == null )
        {
            tbVenda.setTbPagamentoCreditoList( new ArrayList<TbPagamentoCredito>() );
        }
        if ( tbVenda.getTbItemVendaList() == null )
        {
            tbVenda.setTbItemVendaList( new ArrayList<TbItemVenda>() );
        }
        if ( tbVenda.getFormaPagamentoItemList() == null )
        {
            tbVenda.setFormaPagamentoItemList( new ArrayList<FormaPagamentoItem>() );
        }
        if ( tbVenda.getNotasItemList() == null )
        {
            tbVenda.setNotasItemList( new ArrayList<NotasItem>() );
        }
        if ( tbVenda.getAmortizacaoDividaList() == null )
        {
            tbVenda.setAmortizacaoDividaList( new ArrayList<AmortizacaoDivida>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco idBanco = tbVenda.getIdBanco();
            if ( idBanco != null )
            {
                idBanco = em.getReference( idBanco.getClass(), idBanco.getIdBanco() );
                tbVenda.setIdBanco( idBanco );
            }
            TbArmazem idArmazemFK = tbVenda.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK = em.getReference( idArmazemFK.getClass(), idArmazemFK.getCodigo() );
                tbVenda.setIdArmazemFK( idArmazemFK );
            }
            AnoEconomico fkAnoEconomico = tbVenda.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico = em.getReference( fkAnoEconomico.getClass(), fkAnoEconomico.getPkAnoEconomico() );
                tbVenda.setFkAnoEconomico( fkAnoEconomico );
            }
            Cambio fkCambio = tbVenda.getFkCambio();
            if ( fkCambio != null )
            {
                fkCambio = em.getReference( fkCambio.getClass(), fkCambio.getPkCambio() );
                tbVenda.setFkCambio( fkCambio );
            }
            Documento fkDocumento = tbVenda.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento = em.getReference( fkDocumento.getClass(), fkDocumento.getPkDocumento() );
                tbVenda.setFkDocumento( fkDocumento );
            }
            TbUsuario codigoUsuario = tbVenda.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario = em.getReference( codigoUsuario.getClass(), codigoUsuario.getCodigo() );
                tbVenda.setCodigoUsuario( codigoUsuario );
            }
            TbCliente codigoCliente = tbVenda.getCodigoCliente();
            if ( codigoCliente != null )
            {
                codigoCliente = em.getReference( codigoCliente.getClass(), codigoCliente.getCodigo() );
                tbVenda.setCodigoCliente( codigoCliente );
            }
            List<Amortizacao> attachedAmortizacaoList = new ArrayList<Amortizacao>();
            for ( Amortizacao amortizacaoListAmortizacaoToAttach : tbVenda.getAmortizacaoList() )
            {
                amortizacaoListAmortizacaoToAttach = em.getReference( amortizacaoListAmortizacaoToAttach.getClass(), amortizacaoListAmortizacaoToAttach.getPkAmortizacao() );
                attachedAmortizacaoList.add( amortizacaoListAmortizacaoToAttach );
            }
            tbVenda.setAmortizacaoList( attachedAmortizacaoList );
            List<TbPagamentoCredito> attachedTbPagamentoCreditoList = new ArrayList<TbPagamentoCredito>();
            for ( TbPagamentoCredito tbPagamentoCreditoListTbPagamentoCreditoToAttach : tbVenda.getTbPagamentoCreditoList() )
            {
                tbPagamentoCreditoListTbPagamentoCreditoToAttach = em.getReference( tbPagamentoCreditoListTbPagamentoCreditoToAttach.getClass(), tbPagamentoCreditoListTbPagamentoCreditoToAttach.getCodigo() );
                attachedTbPagamentoCreditoList.add( tbPagamentoCreditoListTbPagamentoCreditoToAttach );
            }
            tbVenda.setTbPagamentoCreditoList( attachedTbPagamentoCreditoList );
            List<TbItemVenda> attachedTbItemVendaList = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListTbItemVendaToAttach : tbVenda.getTbItemVendaList() )
            {
                tbItemVendaListTbItemVendaToAttach = em.getReference( tbItemVendaListTbItemVendaToAttach.getClass(), tbItemVendaListTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaList.add( tbItemVendaListTbItemVendaToAttach );
            }
            tbVenda.setTbItemVendaList( attachedTbItemVendaList );
            List<FormaPagamentoItem> attachedFormaPagamentoItemList = new ArrayList<FormaPagamentoItem>();
            for ( FormaPagamentoItem formaPagamentoItemListFormaPagamentoItemToAttach : tbVenda.getFormaPagamentoItemList() )
            {
                formaPagamentoItemListFormaPagamentoItemToAttach = em.getReference( formaPagamentoItemListFormaPagamentoItemToAttach.getClass(), formaPagamentoItemListFormaPagamentoItemToAttach.getPkFormaPagamentoItem() );
                attachedFormaPagamentoItemList.add( formaPagamentoItemListFormaPagamentoItemToAttach );
            }
            tbVenda.setFormaPagamentoItemList( attachedFormaPagamentoItemList );
            List<NotasItem> attachedNotasItemList = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNotasItemToAttach : tbVenda.getNotasItemList() )
            {
                notasItemListNotasItemToAttach = em.getReference( notasItemListNotasItemToAttach.getClass(), notasItemListNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemList.add( notasItemListNotasItemToAttach );
            }
            tbVenda.setNotasItemList( attachedNotasItemList );
            List<AmortizacaoDivida> attachedAmortizacaoDividaList = new ArrayList<AmortizacaoDivida>();
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDividaToAttach : tbVenda.getAmortizacaoDividaList() )
            {
                amortizacaoDividaListAmortizacaoDividaToAttach = em.getReference( amortizacaoDividaListAmortizacaoDividaToAttach.getClass(), amortizacaoDividaListAmortizacaoDividaToAttach.getPkAmortizacaoDivida() );
                attachedAmortizacaoDividaList.add( amortizacaoDividaListAmortizacaoDividaToAttach );
            }
            tbVenda.setAmortizacaoDividaList( attachedAmortizacaoDividaList );
            em.persist( tbVenda );
            if ( idBanco != null )
            {
                idBanco.getTbVendaList().add( tbVenda );
                idBanco = em.merge( idBanco );
            }
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbVendaList().add( tbVenda );
                idArmazemFK = em.merge( idArmazemFK );
            }
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getTbVendaList().add( tbVenda );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            if ( fkCambio != null )
            {
                fkCambio.getTbVendaList().add( tbVenda );
                fkCambio = em.merge( fkCambio );
            }
            if ( fkDocumento != null )
            {
                fkDocumento.getTbVendaList().add( tbVenda );
                fkDocumento = em.merge( fkDocumento );
            }
            if ( codigoUsuario != null )
            {
                codigoUsuario.getTbVendaList().add( tbVenda );
                codigoUsuario = em.merge( codigoUsuario );
            }
            if ( codigoCliente != null )
            {
                codigoCliente.getTbVendaList().add( tbVenda );
                codigoCliente = em.merge( codigoCliente );
            }
            for ( Amortizacao amortizacaoListAmortizacao : tbVenda.getAmortizacaoList() )
            {
                TbVenda oldFkVendaOfAmortizacaoListAmortizacao = amortizacaoListAmortizacao.getFkVenda();
                amortizacaoListAmortizacao.setFkVenda( tbVenda );
                amortizacaoListAmortizacao = em.merge( amortizacaoListAmortizacao );
                if ( oldFkVendaOfAmortizacaoListAmortizacao != null )
                {
                    oldFkVendaOfAmortizacaoListAmortizacao.getAmortizacaoList().remove( amortizacaoListAmortizacao );
                    oldFkVendaOfAmortizacaoListAmortizacao = em.merge( oldFkVendaOfAmortizacaoListAmortizacao );
                }
            }
            for ( TbPagamentoCredito tbPagamentoCreditoListTbPagamentoCredito : tbVenda.getTbPagamentoCreditoList() )
            {
                TbVenda oldCodigoVendaOfTbPagamentoCreditoListTbPagamentoCredito = tbPagamentoCreditoListTbPagamentoCredito.getCodigoVenda();
                tbPagamentoCreditoListTbPagamentoCredito.setCodigoVenda( tbVenda );
                tbPagamentoCreditoListTbPagamentoCredito = em.merge( tbPagamentoCreditoListTbPagamentoCredito );
                if ( oldCodigoVendaOfTbPagamentoCreditoListTbPagamentoCredito != null )
                {
                    oldCodigoVendaOfTbPagamentoCreditoListTbPagamentoCredito.getTbPagamentoCreditoList().remove( tbPagamentoCreditoListTbPagamentoCredito );
                    oldCodigoVendaOfTbPagamentoCreditoListTbPagamentoCredito = em.merge( oldCodigoVendaOfTbPagamentoCreditoListTbPagamentoCredito );
                }
            }
            for ( TbItemVenda tbItemVendaListTbItemVenda : tbVenda.getTbItemVendaList() )
            {
                TbVenda oldCodigoVendaOfTbItemVendaListTbItemVenda = tbItemVendaListTbItemVenda.getCodigoVenda();
                tbItemVendaListTbItemVenda.setCodigoVenda( tbVenda );
                tbItemVendaListTbItemVenda = em.merge( tbItemVendaListTbItemVenda );
                if ( oldCodigoVendaOfTbItemVendaListTbItemVenda != null )
                {
                    oldCodigoVendaOfTbItemVendaListTbItemVenda.getTbItemVendaList().remove( tbItemVendaListTbItemVenda );
                    oldCodigoVendaOfTbItemVendaListTbItemVenda = em.merge( oldCodigoVendaOfTbItemVendaListTbItemVenda );
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListFormaPagamentoItem : tbVenda.getFormaPagamentoItemList() )
            {
                TbVenda oldFkVendaOfFormaPagamentoItemListFormaPagamentoItem = formaPagamentoItemListFormaPagamentoItem.getFkVenda();
                formaPagamentoItemListFormaPagamentoItem.setFkVenda( tbVenda );
                formaPagamentoItemListFormaPagamentoItem = em.merge( formaPagamentoItemListFormaPagamentoItem );
                if ( oldFkVendaOfFormaPagamentoItemListFormaPagamentoItem != null )
                {
                    oldFkVendaOfFormaPagamentoItemListFormaPagamentoItem.getFormaPagamentoItemList().remove( formaPagamentoItemListFormaPagamentoItem );
                    oldFkVendaOfFormaPagamentoItemListFormaPagamentoItem = em.merge( oldFkVendaOfFormaPagamentoItemListFormaPagamentoItem );
                }
            }
            for ( NotasItem notasItemListNotasItem : tbVenda.getNotasItemList() )
            {
                TbVenda oldFkVendaOfNotasItemListNotasItem = notasItemListNotasItem.getFkVenda();
                notasItemListNotasItem.setFkVenda( tbVenda );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
                if ( oldFkVendaOfNotasItemListNotasItem != null )
                {
                    oldFkVendaOfNotasItemListNotasItem.getNotasItemList().remove( notasItemListNotasItem );
                    oldFkVendaOfNotasItemListNotasItem = em.merge( oldFkVendaOfNotasItemListNotasItem );
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDivida : tbVenda.getAmortizacaoDividaList() )
            {
                TbVenda oldFkVendaOfAmortizacaoDividaListAmortizacaoDivida = amortizacaoDividaListAmortizacaoDivida.getFkVenda();
                amortizacaoDividaListAmortizacaoDivida.setFkVenda( tbVenda );
                amortizacaoDividaListAmortizacaoDivida = em.merge( amortizacaoDividaListAmortizacaoDivida );
                if ( oldFkVendaOfAmortizacaoDividaListAmortizacaoDivida != null )
                {
                    oldFkVendaOfAmortizacaoDividaListAmortizacaoDivida.getAmortizacaoDividaList().remove( amortizacaoDividaListAmortizacaoDivida );
                    oldFkVendaOfAmortizacaoDividaListAmortizacaoDivida = em.merge( oldFkVendaOfAmortizacaoDividaListAmortizacaoDivida );
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

    public void edit( TbVenda tbVenda ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbVenda persistentTbVenda = em.find( TbVenda.class, tbVenda.getCodigo() );
            TbBanco idBancoOld = persistentTbVenda.getIdBanco();
            TbBanco idBancoNew = tbVenda.getIdBanco();
            TbArmazem idArmazemFKOld = persistentTbVenda.getIdArmazemFK();
            TbArmazem idArmazemFKNew = tbVenda.getIdArmazemFK();
            AnoEconomico fkAnoEconomicoOld = persistentTbVenda.getFkAnoEconomico();
            AnoEconomico fkAnoEconomicoNew = tbVenda.getFkAnoEconomico();
            Cambio fkCambioOld = persistentTbVenda.getFkCambio();
            Cambio fkCambioNew = tbVenda.getFkCambio();
            Documento fkDocumentoOld = persistentTbVenda.getFkDocumento();
            Documento fkDocumentoNew = tbVenda.getFkDocumento();
            TbUsuario codigoUsuarioOld = persistentTbVenda.getCodigoUsuario();
            TbUsuario codigoUsuarioNew = tbVenda.getCodigoUsuario();
            TbCliente codigoClienteOld = persistentTbVenda.getCodigoCliente();
            TbCliente codigoClienteNew = tbVenda.getCodigoCliente();
            List<Amortizacao> amortizacaoListOld = persistentTbVenda.getAmortizacaoList();
            List<Amortizacao> amortizacaoListNew = tbVenda.getAmortizacaoList();
            List<TbPagamentoCredito> tbPagamentoCreditoListOld = persistentTbVenda.getTbPagamentoCreditoList();
            List<TbPagamentoCredito> tbPagamentoCreditoListNew = tbVenda.getTbPagamentoCreditoList();
            List<TbItemVenda> tbItemVendaListOld = persistentTbVenda.getTbItemVendaList();
            List<TbItemVenda> tbItemVendaListNew = tbVenda.getTbItemVendaList();
            List<FormaPagamentoItem> formaPagamentoItemListOld = persistentTbVenda.getFormaPagamentoItemList();
            List<FormaPagamentoItem> formaPagamentoItemListNew = tbVenda.getFormaPagamentoItemList();
            List<NotasItem> notasItemListOld = persistentTbVenda.getNotasItemList();
            List<NotasItem> notasItemListNew = tbVenda.getNotasItemList();
            List<AmortizacaoDivida> amortizacaoDividaListOld = persistentTbVenda.getAmortizacaoDividaList();
            List<AmortizacaoDivida> amortizacaoDividaListNew = tbVenda.getAmortizacaoDividaList();
            List<String> illegalOrphanMessages = null;
            for ( Amortizacao amortizacaoListOldAmortizacao : amortizacaoListOld )
            {
                if ( !amortizacaoListNew.contains( amortizacaoListOldAmortizacao ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain Amortizacao " + amortizacaoListOldAmortizacao + " since its fkVenda field is not nullable." );
                }
            }
            for ( TbPagamentoCredito tbPagamentoCreditoListOldTbPagamentoCredito : tbPagamentoCreditoListOld )
            {
                if ( !tbPagamentoCreditoListNew.contains( tbPagamentoCreditoListOldTbPagamentoCredito ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbPagamentoCredito " + tbPagamentoCreditoListOldTbPagamentoCredito + " since its codigoVenda field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbItemVenda " + tbItemVendaListOldTbItemVenda + " since its codigoVenda field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( idBancoNew != null )
            {
                idBancoNew = em.getReference( idBancoNew.getClass(), idBancoNew.getIdBanco() );
                tbVenda.setIdBanco( idBancoNew );
            }
            if ( idArmazemFKNew != null )
            {
                idArmazemFKNew = em.getReference( idArmazemFKNew.getClass(), idArmazemFKNew.getCodigo() );
                tbVenda.setIdArmazemFK( idArmazemFKNew );
            }
            if ( fkAnoEconomicoNew != null )
            {
                fkAnoEconomicoNew = em.getReference( fkAnoEconomicoNew.getClass(), fkAnoEconomicoNew.getPkAnoEconomico() );
                tbVenda.setFkAnoEconomico( fkAnoEconomicoNew );
            }
            if ( fkCambioNew != null )
            {
                fkCambioNew = em.getReference( fkCambioNew.getClass(), fkCambioNew.getPkCambio() );
                tbVenda.setFkCambio( fkCambioNew );
            }
            if ( fkDocumentoNew != null )
            {
                fkDocumentoNew = em.getReference( fkDocumentoNew.getClass(), fkDocumentoNew.getPkDocumento() );
                tbVenda.setFkDocumento( fkDocumentoNew );
            }
            if ( codigoUsuarioNew != null )
            {
                codigoUsuarioNew = em.getReference( codigoUsuarioNew.getClass(), codigoUsuarioNew.getCodigo() );
                tbVenda.setCodigoUsuario( codigoUsuarioNew );
            }
            if ( codigoClienteNew != null )
            {
                codigoClienteNew = em.getReference( codigoClienteNew.getClass(), codigoClienteNew.getCodigo() );
                tbVenda.setCodigoCliente( codigoClienteNew );
            }
            List<Amortizacao> attachedAmortizacaoListNew = new ArrayList<Amortizacao>();
            for ( Amortizacao amortizacaoListNewAmortizacaoToAttach : amortizacaoListNew )
            {
                amortizacaoListNewAmortizacaoToAttach = em.getReference( amortizacaoListNewAmortizacaoToAttach.getClass(), amortizacaoListNewAmortizacaoToAttach.getPkAmortizacao() );
                attachedAmortizacaoListNew.add( amortizacaoListNewAmortizacaoToAttach );
            }
            amortizacaoListNew = attachedAmortizacaoListNew;
            tbVenda.setAmortizacaoList( amortizacaoListNew );
            List<TbPagamentoCredito> attachedTbPagamentoCreditoListNew = new ArrayList<TbPagamentoCredito>();
            for ( TbPagamentoCredito tbPagamentoCreditoListNewTbPagamentoCreditoToAttach : tbPagamentoCreditoListNew )
            {
                tbPagamentoCreditoListNewTbPagamentoCreditoToAttach = em.getReference( tbPagamentoCreditoListNewTbPagamentoCreditoToAttach.getClass(), tbPagamentoCreditoListNewTbPagamentoCreditoToAttach.getCodigo() );
                attachedTbPagamentoCreditoListNew.add( tbPagamentoCreditoListNewTbPagamentoCreditoToAttach );
            }
            tbPagamentoCreditoListNew = attachedTbPagamentoCreditoListNew;
            tbVenda.setTbPagamentoCreditoList( tbPagamentoCreditoListNew );
            List<TbItemVenda> attachedTbItemVendaListNew = new ArrayList<TbItemVenda>();
            for ( TbItemVenda tbItemVendaListNewTbItemVendaToAttach : tbItemVendaListNew )
            {
                tbItemVendaListNewTbItemVendaToAttach = em.getReference( tbItemVendaListNewTbItemVendaToAttach.getClass(), tbItemVendaListNewTbItemVendaToAttach.getCodigo() );
                attachedTbItemVendaListNew.add( tbItemVendaListNewTbItemVendaToAttach );
            }
            tbItemVendaListNew = attachedTbItemVendaListNew;
            tbVenda.setTbItemVendaList( tbItemVendaListNew );
            List<FormaPagamentoItem> attachedFormaPagamentoItemListNew = new ArrayList<FormaPagamentoItem>();
            for ( FormaPagamentoItem formaPagamentoItemListNewFormaPagamentoItemToAttach : formaPagamentoItemListNew )
            {
                formaPagamentoItemListNewFormaPagamentoItemToAttach = em.getReference( formaPagamentoItemListNewFormaPagamentoItemToAttach.getClass(), formaPagamentoItemListNewFormaPagamentoItemToAttach.getPkFormaPagamentoItem() );
                attachedFormaPagamentoItemListNew.add( formaPagamentoItemListNewFormaPagamentoItemToAttach );
            }
            formaPagamentoItemListNew = attachedFormaPagamentoItemListNew;
            tbVenda.setFormaPagamentoItemList( formaPagamentoItemListNew );
            List<NotasItem> attachedNotasItemListNew = new ArrayList<NotasItem>();
            for ( NotasItem notasItemListNewNotasItemToAttach : notasItemListNew )
            {
                notasItemListNewNotasItemToAttach = em.getReference( notasItemListNewNotasItemToAttach.getClass(), notasItemListNewNotasItemToAttach.getPkNotasItem() );
                attachedNotasItemListNew.add( notasItemListNewNotasItemToAttach );
            }
            notasItemListNew = attachedNotasItemListNew;
            tbVenda.setNotasItemList( notasItemListNew );
            List<AmortizacaoDivida> attachedAmortizacaoDividaListNew = new ArrayList<AmortizacaoDivida>();
            for ( AmortizacaoDivida amortizacaoDividaListNewAmortizacaoDividaToAttach : amortizacaoDividaListNew )
            {
                amortizacaoDividaListNewAmortizacaoDividaToAttach = em.getReference( amortizacaoDividaListNewAmortizacaoDividaToAttach.getClass(), amortizacaoDividaListNewAmortizacaoDividaToAttach.getPkAmortizacaoDivida() );
                attachedAmortizacaoDividaListNew.add( amortizacaoDividaListNewAmortizacaoDividaToAttach );
            }
            amortizacaoDividaListNew = attachedAmortizacaoDividaListNew;
            tbVenda.setAmortizacaoDividaList( amortizacaoDividaListNew );
            tbVenda = em.merge( tbVenda );
            if ( idBancoOld != null && !idBancoOld.equals( idBancoNew ) )
            {
                idBancoOld.getTbVendaList().remove( tbVenda );
                idBancoOld = em.merge( idBancoOld );
            }
            if ( idBancoNew != null && !idBancoNew.equals( idBancoOld ) )
            {
                idBancoNew.getTbVendaList().add( tbVenda );
                idBancoNew = em.merge( idBancoNew );
            }
            if ( idArmazemFKOld != null && !idArmazemFKOld.equals( idArmazemFKNew ) )
            {
                idArmazemFKOld.getTbVendaList().remove( tbVenda );
                idArmazemFKOld = em.merge( idArmazemFKOld );
            }
            if ( idArmazemFKNew != null && !idArmazemFKNew.equals( idArmazemFKOld ) )
            {
                idArmazemFKNew.getTbVendaList().add( tbVenda );
                idArmazemFKNew = em.merge( idArmazemFKNew );
            }
            if ( fkAnoEconomicoOld != null && !fkAnoEconomicoOld.equals( fkAnoEconomicoNew ) )
            {
                fkAnoEconomicoOld.getTbVendaList().remove( tbVenda );
                fkAnoEconomicoOld = em.merge( fkAnoEconomicoOld );
            }
            if ( fkAnoEconomicoNew != null && !fkAnoEconomicoNew.equals( fkAnoEconomicoOld ) )
            {
                fkAnoEconomicoNew.getTbVendaList().add( tbVenda );
                fkAnoEconomicoNew = em.merge( fkAnoEconomicoNew );
            }
            if ( fkCambioOld != null && !fkCambioOld.equals( fkCambioNew ) )
            {
                fkCambioOld.getTbVendaList().remove( tbVenda );
                fkCambioOld = em.merge( fkCambioOld );
            }
            if ( fkCambioNew != null && !fkCambioNew.equals( fkCambioOld ) )
            {
                fkCambioNew.getTbVendaList().add( tbVenda );
                fkCambioNew = em.merge( fkCambioNew );
            }
            if ( fkDocumentoOld != null && !fkDocumentoOld.equals( fkDocumentoNew ) )
            {
                fkDocumentoOld.getTbVendaList().remove( tbVenda );
                fkDocumentoOld = em.merge( fkDocumentoOld );
            }
            if ( fkDocumentoNew != null && !fkDocumentoNew.equals( fkDocumentoOld ) )
            {
                fkDocumentoNew.getTbVendaList().add( tbVenda );
                fkDocumentoNew = em.merge( fkDocumentoNew );
            }
            if ( codigoUsuarioOld != null && !codigoUsuarioOld.equals( codigoUsuarioNew ) )
            {
                codigoUsuarioOld.getTbVendaList().remove( tbVenda );
                codigoUsuarioOld = em.merge( codigoUsuarioOld );
            }
            if ( codigoUsuarioNew != null && !codigoUsuarioNew.equals( codigoUsuarioOld ) )
            {
                codigoUsuarioNew.getTbVendaList().add( tbVenda );
                codigoUsuarioNew = em.merge( codigoUsuarioNew );
            }
            if ( codigoClienteOld != null && !codigoClienteOld.equals( codigoClienteNew ) )
            {
                codigoClienteOld.getTbVendaList().remove( tbVenda );
                codigoClienteOld = em.merge( codigoClienteOld );
            }
            if ( codigoClienteNew != null && !codigoClienteNew.equals( codigoClienteOld ) )
            {
                codigoClienteNew.getTbVendaList().add( tbVenda );
                codigoClienteNew = em.merge( codigoClienteNew );
            }
            for ( Amortizacao amortizacaoListNewAmortizacao : amortizacaoListNew )
            {
                if ( !amortizacaoListOld.contains( amortizacaoListNewAmortizacao ) )
                {
                    TbVenda oldFkVendaOfAmortizacaoListNewAmortizacao = amortizacaoListNewAmortizacao.getFkVenda();
                    amortizacaoListNewAmortizacao.setFkVenda( tbVenda );
                    amortizacaoListNewAmortizacao = em.merge( amortizacaoListNewAmortizacao );
                    if ( oldFkVendaOfAmortizacaoListNewAmortizacao != null && !oldFkVendaOfAmortizacaoListNewAmortizacao.equals( tbVenda ) )
                    {
                        oldFkVendaOfAmortizacaoListNewAmortizacao.getAmortizacaoList().remove( amortizacaoListNewAmortizacao );
                        oldFkVendaOfAmortizacaoListNewAmortizacao = em.merge( oldFkVendaOfAmortizacaoListNewAmortizacao );
                    }
                }
            }
            for ( TbPagamentoCredito tbPagamentoCreditoListNewTbPagamentoCredito : tbPagamentoCreditoListNew )
            {
                if ( !tbPagamentoCreditoListOld.contains( tbPagamentoCreditoListNewTbPagamentoCredito ) )
                {
                    TbVenda oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito = tbPagamentoCreditoListNewTbPagamentoCredito.getCodigoVenda();
                    tbPagamentoCreditoListNewTbPagamentoCredito.setCodigoVenda( tbVenda );
                    tbPagamentoCreditoListNewTbPagamentoCredito = em.merge( tbPagamentoCreditoListNewTbPagamentoCredito );
                    if ( oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito != null && !oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito.equals( tbVenda ) )
                    {
                        oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito.getTbPagamentoCreditoList().remove( tbPagamentoCreditoListNewTbPagamentoCredito );
                        oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito = em.merge( oldCodigoVendaOfTbPagamentoCreditoListNewTbPagamentoCredito );
                    }
                }
            }
            for ( TbItemVenda tbItemVendaListNewTbItemVenda : tbItemVendaListNew )
            {
                if ( !tbItemVendaListOld.contains( tbItemVendaListNewTbItemVenda ) )
                {
                    TbVenda oldCodigoVendaOfTbItemVendaListNewTbItemVenda = tbItemVendaListNewTbItemVenda.getCodigoVenda();
                    tbItemVendaListNewTbItemVenda.setCodigoVenda( tbVenda );
                    tbItemVendaListNewTbItemVenda = em.merge( tbItemVendaListNewTbItemVenda );
                    if ( oldCodigoVendaOfTbItemVendaListNewTbItemVenda != null && !oldCodigoVendaOfTbItemVendaListNewTbItemVenda.equals( tbVenda ) )
                    {
                        oldCodigoVendaOfTbItemVendaListNewTbItemVenda.getTbItemVendaList().remove( tbItemVendaListNewTbItemVenda );
                        oldCodigoVendaOfTbItemVendaListNewTbItemVenda = em.merge( oldCodigoVendaOfTbItemVendaListNewTbItemVenda );
                    }
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListOldFormaPagamentoItem : formaPagamentoItemListOld )
            {
                if ( !formaPagamentoItemListNew.contains( formaPagamentoItemListOldFormaPagamentoItem ) )
                {
                    formaPagamentoItemListOldFormaPagamentoItem.setFkVenda( null );
                    formaPagamentoItemListOldFormaPagamentoItem = em.merge( formaPagamentoItemListOldFormaPagamentoItem );
                }
            }
            for ( FormaPagamentoItem formaPagamentoItemListNewFormaPagamentoItem : formaPagamentoItemListNew )
            {
                if ( !formaPagamentoItemListOld.contains( formaPagamentoItemListNewFormaPagamentoItem ) )
                {
                    TbVenda oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem = formaPagamentoItemListNewFormaPagamentoItem.getFkVenda();
                    formaPagamentoItemListNewFormaPagamentoItem.setFkVenda( tbVenda );
                    formaPagamentoItemListNewFormaPagamentoItem = em.merge( formaPagamentoItemListNewFormaPagamentoItem );
                    if ( oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem != null && !oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem.equals( tbVenda ) )
                    {
                        oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem.getFormaPagamentoItemList().remove( formaPagamentoItemListNewFormaPagamentoItem );
                        oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem = em.merge( oldFkVendaOfFormaPagamentoItemListNewFormaPagamentoItem );
                    }
                }
            }
            for ( NotasItem notasItemListOldNotasItem : notasItemListOld )
            {
                if ( !notasItemListNew.contains( notasItemListOldNotasItem ) )
                {
                    notasItemListOldNotasItem.setFkVenda( null );
                    notasItemListOldNotasItem = em.merge( notasItemListOldNotasItem );
                }
            }
            for ( NotasItem notasItemListNewNotasItem : notasItemListNew )
            {
                if ( !notasItemListOld.contains( notasItemListNewNotasItem ) )
                {
                    TbVenda oldFkVendaOfNotasItemListNewNotasItem = notasItemListNewNotasItem.getFkVenda();
                    notasItemListNewNotasItem.setFkVenda( tbVenda );
                    notasItemListNewNotasItem = em.merge( notasItemListNewNotasItem );
                    if ( oldFkVendaOfNotasItemListNewNotasItem != null && !oldFkVendaOfNotasItemListNewNotasItem.equals( tbVenda ) )
                    {
                        oldFkVendaOfNotasItemListNewNotasItem.getNotasItemList().remove( notasItemListNewNotasItem );
                        oldFkVendaOfNotasItemListNewNotasItem = em.merge( oldFkVendaOfNotasItemListNewNotasItem );
                    }
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListOldAmortizacaoDivida : amortizacaoDividaListOld )
            {
                if ( !amortizacaoDividaListNew.contains( amortizacaoDividaListOldAmortizacaoDivida ) )
                {
                    amortizacaoDividaListOldAmortizacaoDivida.setFkVenda( null );
                    amortizacaoDividaListOldAmortizacaoDivida = em.merge( amortizacaoDividaListOldAmortizacaoDivida );
                }
            }
            for ( AmortizacaoDivida amortizacaoDividaListNewAmortizacaoDivida : amortizacaoDividaListNew )
            {
                if ( !amortizacaoDividaListOld.contains( amortizacaoDividaListNewAmortizacaoDivida ) )
                {
                    TbVenda oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida = amortizacaoDividaListNewAmortizacaoDivida.getFkVenda();
                    amortizacaoDividaListNewAmortizacaoDivida.setFkVenda( tbVenda );
                    amortizacaoDividaListNewAmortizacaoDivida = em.merge( amortizacaoDividaListNewAmortizacaoDivida );
                    if ( oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida != null && !oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida.equals( tbVenda ) )
                    {
                        oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida.getAmortizacaoDividaList().remove( amortizacaoDividaListNewAmortizacaoDivida );
                        oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida = em.merge( oldFkVendaOfAmortizacaoDividaListNewAmortizacaoDivida );
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
                Integer id = tbVenda.getCodigo();
                if ( findTbVenda( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbVenda with id " + id + " no longer exists." );
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
            TbVenda tbVenda;
            try
            {
                tbVenda = em.getReference( TbVenda.class, id );
                tbVenda.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbVenda with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<Amortizacao> amortizacaoListOrphanCheck = tbVenda.getAmortizacaoList();
            for ( Amortizacao amortizacaoListOrphanCheckAmortizacao : amortizacaoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbVenda (" + tbVenda + ") cannot be destroyed since the Amortizacao " + amortizacaoListOrphanCheckAmortizacao + " in its amortizacaoList field has a non-nullable fkVenda field." );
            }
            List<TbPagamentoCredito> tbPagamentoCreditoListOrphanCheck = tbVenda.getTbPagamentoCreditoList();
            for ( TbPagamentoCredito tbPagamentoCreditoListOrphanCheckTbPagamentoCredito : tbPagamentoCreditoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbVenda (" + tbVenda + ") cannot be destroyed since the TbPagamentoCredito " + tbPagamentoCreditoListOrphanCheckTbPagamentoCredito + " in its tbPagamentoCreditoList field has a non-nullable codigoVenda field." );
            }
            List<TbItemVenda> tbItemVendaListOrphanCheck = tbVenda.getTbItemVendaList();
            for ( TbItemVenda tbItemVendaListOrphanCheckTbItemVenda : tbItemVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbVenda (" + tbVenda + ") cannot be destroyed since the TbItemVenda " + tbItemVendaListOrphanCheckTbItemVenda + " in its tbItemVendaList field has a non-nullable codigoVenda field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbBanco idBanco = tbVenda.getIdBanco();
            if ( idBanco != null )
            {
                idBanco.getTbVendaList().remove( tbVenda );
                idBanco = em.merge( idBanco );
            }
            TbArmazem idArmazemFK = tbVenda.getIdArmazemFK();
            if ( idArmazemFK != null )
            {
                idArmazemFK.getTbVendaList().remove( tbVenda );
                idArmazemFK = em.merge( idArmazemFK );
            }
            AnoEconomico fkAnoEconomico = tbVenda.getFkAnoEconomico();
            if ( fkAnoEconomico != null )
            {
                fkAnoEconomico.getTbVendaList().remove( tbVenda );
                fkAnoEconomico = em.merge( fkAnoEconomico );
            }
            Cambio fkCambio = tbVenda.getFkCambio();
            if ( fkCambio != null )
            {
                fkCambio.getTbVendaList().remove( tbVenda );
                fkCambio = em.merge( fkCambio );
            }
            Documento fkDocumento = tbVenda.getFkDocumento();
            if ( fkDocumento != null )
            {
                fkDocumento.getTbVendaList().remove( tbVenda );
                fkDocumento = em.merge( fkDocumento );
            }
            TbUsuario codigoUsuario = tbVenda.getCodigoUsuario();
            if ( codigoUsuario != null )
            {
                codigoUsuario.getTbVendaList().remove( tbVenda );
                codigoUsuario = em.merge( codigoUsuario );
            }
            TbCliente codigoCliente = tbVenda.getCodigoCliente();
            if ( codigoCliente != null )
            {
                codigoCliente.getTbVendaList().remove( tbVenda );
                codigoCliente = em.merge( codigoCliente );
            }
            List<FormaPagamentoItem> formaPagamentoItemList = tbVenda.getFormaPagamentoItemList();
            for ( FormaPagamentoItem formaPagamentoItemListFormaPagamentoItem : formaPagamentoItemList )
            {
                formaPagamentoItemListFormaPagamentoItem.setFkVenda( null );
                formaPagamentoItemListFormaPagamentoItem = em.merge( formaPagamentoItemListFormaPagamentoItem );
            }
            List<NotasItem> notasItemList = tbVenda.getNotasItemList();
            for ( NotasItem notasItemListNotasItem : notasItemList )
            {
                notasItemListNotasItem.setFkVenda( null );
                notasItemListNotasItem = em.merge( notasItemListNotasItem );
            }
            List<AmortizacaoDivida> amortizacaoDividaList = tbVenda.getAmortizacaoDividaList();
            for ( AmortizacaoDivida amortizacaoDividaListAmortizacaoDivida : amortizacaoDividaList )
            {
                amortizacaoDividaListAmortizacaoDivida.setFkVenda( null );
                amortizacaoDividaListAmortizacaoDivida = em.merge( amortizacaoDividaListAmortizacaoDivida );
            }
            em.remove( tbVenda );
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

    public List<TbVenda> findTbVendaEntities()
    {
        return findTbVendaEntities( true, -1, -1 );
    }

    public List<TbVenda> findTbVendaEntities( int maxResults, int firstResult )
    {
        return findTbVendaEntities( false, maxResults, firstResult );
    }

    private List<TbVenda> findTbVendaEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbVenda.class ) );
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

    public TbVenda findTbVenda( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbVenda.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbVendaCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbVenda> rt = cq.from( TbVenda.class );
            cq.select( em.getCriteriaBuilder().count( rt ) );
            Query q = em.createQuery( cq );
            return ( ( Long ) q.getSingleResult() ).intValue();
        }
        finally
        {
            em.close();
        }
    }
    
}
