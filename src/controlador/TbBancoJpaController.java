/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.NotasCompras;
import java.util.ArrayList;
import java.util.List;
import entity.Notas;
import entity.TransferenciaBancaria;
import entity.LevantamentoBancario;
import entity.SaidasTesouraria;
import entity.DepositoBancario;
import entity.TbConta;
import entity.TbVenda;
import entity.EntradaTesouraria;
import entity.TbBanco;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbBancoJpaController implements Serializable
{

    public TbBancoJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbBanco tbBanco )
    {
        if ( tbBanco.getNotasComprasList() == null )
        {
            tbBanco.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( tbBanco.getNotasList() == null )
        {
            tbBanco.setNotasList( new ArrayList<Notas>() );
        }
        if ( tbBanco.getTransferenciaBancariaList() == null )
        {
            tbBanco.setTransferenciaBancariaList( new ArrayList<TransferenciaBancaria>() );
        }
        if ( tbBanco.getTransferenciaBancariaList1() == null )
        {
            tbBanco.setTransferenciaBancariaList1( new ArrayList<TransferenciaBancaria>() );
        }
        if ( tbBanco.getLevantamentoBancarioList() == null )
        {
            tbBanco.setLevantamentoBancarioList( new ArrayList<LevantamentoBancario>() );
        }
        if ( tbBanco.getSaidasTesourariaList() == null )
        {
            tbBanco.setSaidasTesourariaList( new ArrayList<SaidasTesouraria>() );
        }
        if ( tbBanco.getDepositoBancarioList() == null )
        {
            tbBanco.setDepositoBancarioList( new ArrayList<DepositoBancario>() );
        }
        if ( tbBanco.getTbContaList() == null )
        {
            tbBanco.setTbContaList( new ArrayList<TbConta>() );
        }
        if ( tbBanco.getTbVendaList() == null )
        {
            tbBanco.setTbVendaList( new ArrayList<TbVenda>() );
        }
        if ( tbBanco.getEntradaTesourariaList() == null )
        {
            tbBanco.setEntradaTesourariaList( new ArrayList<EntradaTesouraria>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : tbBanco.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            tbBanco.setNotasComprasList( attachedNotasComprasList );
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : tbBanco.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            tbBanco.setNotasList( attachedNotasList );
            List<TransferenciaBancaria> attachedTransferenciaBancariaList = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancariaToAttach : tbBanco.getTransferenciaBancariaList() )
            {
                transferenciaBancariaListTransferenciaBancariaToAttach = em.getReference( transferenciaBancariaListTransferenciaBancariaToAttach.getClass(), transferenciaBancariaListTransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaList.add( transferenciaBancariaListTransferenciaBancariaToAttach );
            }
            tbBanco.setTransferenciaBancariaList( attachedTransferenciaBancariaList );
            List<TransferenciaBancaria> attachedTransferenciaBancariaList1 = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaList1TransferenciaBancariaToAttach : tbBanco.getTransferenciaBancariaList1() )
            {
                transferenciaBancariaList1TransferenciaBancariaToAttach = em.getReference( transferenciaBancariaList1TransferenciaBancariaToAttach.getClass(), transferenciaBancariaList1TransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaList1.add( transferenciaBancariaList1TransferenciaBancariaToAttach );
            }
            tbBanco.setTransferenciaBancariaList1( attachedTransferenciaBancariaList1 );
            List<LevantamentoBancario> attachedLevantamentoBancarioList = new ArrayList<LevantamentoBancario>();
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancarioToAttach : tbBanco.getLevantamentoBancarioList() )
            {
                levantamentoBancarioListLevantamentoBancarioToAttach = em.getReference( levantamentoBancarioListLevantamentoBancarioToAttach.getClass(), levantamentoBancarioListLevantamentoBancarioToAttach.getPkLevantamento() );
                attachedLevantamentoBancarioList.add( levantamentoBancarioListLevantamentoBancarioToAttach );
            }
            tbBanco.setLevantamentoBancarioList( attachedLevantamentoBancarioList );
            List<SaidasTesouraria> attachedSaidasTesourariaList = new ArrayList<SaidasTesouraria>();
            for ( SaidasTesouraria saidasTesourariaListSaidasTesourariaToAttach : tbBanco.getSaidasTesourariaList() )
            {
                saidasTesourariaListSaidasTesourariaToAttach = em.getReference( saidasTesourariaListSaidasTesourariaToAttach.getClass(), saidasTesourariaListSaidasTesourariaToAttach.getPkSaidasTesouraria() );
                attachedSaidasTesourariaList.add( saidasTesourariaListSaidasTesourariaToAttach );
            }
            tbBanco.setSaidasTesourariaList( attachedSaidasTesourariaList );
            List<DepositoBancario> attachedDepositoBancarioList = new ArrayList<DepositoBancario>();
            for ( DepositoBancario depositoBancarioListDepositoBancarioToAttach : tbBanco.getDepositoBancarioList() )
            {
                depositoBancarioListDepositoBancarioToAttach = em.getReference( depositoBancarioListDepositoBancarioToAttach.getClass(), depositoBancarioListDepositoBancarioToAttach.getPkDeposito() );
                attachedDepositoBancarioList.add( depositoBancarioListDepositoBancarioToAttach );
            }
            tbBanco.setDepositoBancarioList( attachedDepositoBancarioList );
            List<TbConta> attachedTbContaList = new ArrayList<TbConta>();
            for ( TbConta tbContaListTbContaToAttach : tbBanco.getTbContaList() )
            {
                tbContaListTbContaToAttach = em.getReference( tbContaListTbContaToAttach.getClass(), tbContaListTbContaToAttach.getIdContaPK() );
                attachedTbContaList.add( tbContaListTbContaToAttach );
            }
            tbBanco.setTbContaList( attachedTbContaList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : tbBanco.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            tbBanco.setTbVendaList( attachedTbVendaList );
            List<EntradaTesouraria> attachedEntradaTesourariaList = new ArrayList<EntradaTesouraria>();
            for ( EntradaTesouraria entradaTesourariaListEntradaTesourariaToAttach : tbBanco.getEntradaTesourariaList() )
            {
                entradaTesourariaListEntradaTesourariaToAttach = em.getReference( entradaTesourariaListEntradaTesourariaToAttach.getClass(), entradaTesourariaListEntradaTesourariaToAttach.getPkEntradaTesouraria() );
                attachedEntradaTesourariaList.add( entradaTesourariaListEntradaTesourariaToAttach );
            }
            tbBanco.setEntradaTesourariaList( attachedEntradaTesourariaList );
            em.persist( tbBanco );
            for ( NotasCompras notasComprasListNotasCompras : tbBanco.getNotasComprasList() )
            {
                TbBanco oldIdBancoOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getIdBanco();
                notasComprasListNotasCompras.setIdBanco( tbBanco );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldIdBancoOfNotasComprasListNotasCompras != null )
                {
                    oldIdBancoOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldIdBancoOfNotasComprasListNotasCompras = em.merge( oldIdBancoOfNotasComprasListNotasCompras );
                }
            }
            for ( Notas notasListNotas : tbBanco.getNotasList() )
            {
                TbBanco oldIdBancoOfNotasListNotas = notasListNotas.getIdBanco();
                notasListNotas.setIdBanco( tbBanco );
                notasListNotas = em.merge( notasListNotas );
                if ( oldIdBancoOfNotasListNotas != null )
                {
                    oldIdBancoOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldIdBancoOfNotasListNotas = em.merge( oldIdBancoOfNotasListNotas );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancaria : tbBanco.getTransferenciaBancariaList() )
            {
                TbBanco oldFkBancoDestinoOfTransferenciaBancariaListTransferenciaBancaria = transferenciaBancariaListTransferenciaBancaria.getFkBancoDestino();
                transferenciaBancariaListTransferenciaBancaria.setFkBancoDestino( tbBanco );
                transferenciaBancariaListTransferenciaBancaria = em.merge( transferenciaBancariaListTransferenciaBancaria );
                if ( oldFkBancoDestinoOfTransferenciaBancariaListTransferenciaBancaria != null )
                {
                    oldFkBancoDestinoOfTransferenciaBancariaListTransferenciaBancaria.getTransferenciaBancariaList().remove( transferenciaBancariaListTransferenciaBancaria );
                    oldFkBancoDestinoOfTransferenciaBancariaListTransferenciaBancaria = em.merge( oldFkBancoDestinoOfTransferenciaBancariaListTransferenciaBancaria );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaList1TransferenciaBancaria : tbBanco.getTransferenciaBancariaList1() )
            {
                TbBanco oldFkBancoOrigemOfTransferenciaBancariaList1TransferenciaBancaria = transferenciaBancariaList1TransferenciaBancaria.getFkBancoOrigem();
                transferenciaBancariaList1TransferenciaBancaria.setFkBancoOrigem( tbBanco );
                transferenciaBancariaList1TransferenciaBancaria = em.merge( transferenciaBancariaList1TransferenciaBancaria );
                if ( oldFkBancoOrigemOfTransferenciaBancariaList1TransferenciaBancaria != null )
                {
                    oldFkBancoOrigemOfTransferenciaBancariaList1TransferenciaBancaria.getTransferenciaBancariaList1().remove( transferenciaBancariaList1TransferenciaBancaria );
                    oldFkBancoOrigemOfTransferenciaBancariaList1TransferenciaBancaria = em.merge( oldFkBancoOrigemOfTransferenciaBancariaList1TransferenciaBancaria );
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancario : tbBanco.getLevantamentoBancarioList() )
            {
                TbBanco oldFkBancoOfLevantamentoBancarioListLevantamentoBancario = levantamentoBancarioListLevantamentoBancario.getFkBanco();
                levantamentoBancarioListLevantamentoBancario.setFkBanco( tbBanco );
                levantamentoBancarioListLevantamentoBancario = em.merge( levantamentoBancarioListLevantamentoBancario );
                if ( oldFkBancoOfLevantamentoBancarioListLevantamentoBancario != null )
                {
                    oldFkBancoOfLevantamentoBancarioListLevantamentoBancario.getLevantamentoBancarioList().remove( levantamentoBancarioListLevantamentoBancario );
                    oldFkBancoOfLevantamentoBancarioListLevantamentoBancario = em.merge( oldFkBancoOfLevantamentoBancarioListLevantamentoBancario );
                }
            }
            for ( SaidasTesouraria saidasTesourariaListSaidasTesouraria : tbBanco.getSaidasTesourariaList() )
            {
                TbBanco oldFkBancoOfSaidasTesourariaListSaidasTesouraria = saidasTesourariaListSaidasTesouraria.getFkBanco();
                saidasTesourariaListSaidasTesouraria.setFkBanco( tbBanco );
                saidasTesourariaListSaidasTesouraria = em.merge( saidasTesourariaListSaidasTesouraria );
                if ( oldFkBancoOfSaidasTesourariaListSaidasTesouraria != null )
                {
                    oldFkBancoOfSaidasTesourariaListSaidasTesouraria.getSaidasTesourariaList().remove( saidasTesourariaListSaidasTesouraria );
                    oldFkBancoOfSaidasTesourariaListSaidasTesouraria = em.merge( oldFkBancoOfSaidasTesourariaListSaidasTesouraria );
                }
            }
            for ( DepositoBancario depositoBancarioListDepositoBancario : tbBanco.getDepositoBancarioList() )
            {
                TbBanco oldFkBancoOfDepositoBancarioListDepositoBancario = depositoBancarioListDepositoBancario.getFkBanco();
                depositoBancarioListDepositoBancario.setFkBanco( tbBanco );
                depositoBancarioListDepositoBancario = em.merge( depositoBancarioListDepositoBancario );
                if ( oldFkBancoOfDepositoBancarioListDepositoBancario != null )
                {
                    oldFkBancoOfDepositoBancarioListDepositoBancario.getDepositoBancarioList().remove( depositoBancarioListDepositoBancario );
                    oldFkBancoOfDepositoBancarioListDepositoBancario = em.merge( oldFkBancoOfDepositoBancarioListDepositoBancario );
                }
            }
            for ( TbConta tbContaListTbConta : tbBanco.getTbContaList() )
            {
                TbBanco oldIdBancoFKOfTbContaListTbConta = tbContaListTbConta.getIdBancoFK();
                tbContaListTbConta.setIdBancoFK( tbBanco );
                tbContaListTbConta = em.merge( tbContaListTbConta );
                if ( oldIdBancoFKOfTbContaListTbConta != null )
                {
                    oldIdBancoFKOfTbContaListTbConta.getTbContaList().remove( tbContaListTbConta );
                    oldIdBancoFKOfTbContaListTbConta = em.merge( oldIdBancoFKOfTbContaListTbConta );
                }
            }
            for ( TbVenda tbVendaListTbVenda : tbBanco.getTbVendaList() )
            {
                TbBanco oldIdBancoOfTbVendaListTbVenda = tbVendaListTbVenda.getIdBanco();
                tbVendaListTbVenda.setIdBanco( tbBanco );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldIdBancoOfTbVendaListTbVenda != null )
                {
                    oldIdBancoOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldIdBancoOfTbVendaListTbVenda = em.merge( oldIdBancoOfTbVendaListTbVenda );
                }
            }
            for ( EntradaTesouraria entradaTesourariaListEntradaTesouraria : tbBanco.getEntradaTesourariaList() )
            {
                TbBanco oldFkBancoOfEntradaTesourariaListEntradaTesouraria = entradaTesourariaListEntradaTesouraria.getFkBanco();
                entradaTesourariaListEntradaTesouraria.setFkBanco( tbBanco );
                entradaTesourariaListEntradaTesouraria = em.merge( entradaTesourariaListEntradaTesouraria );
                if ( oldFkBancoOfEntradaTesourariaListEntradaTesouraria != null )
                {
                    oldFkBancoOfEntradaTesourariaListEntradaTesouraria.getEntradaTesourariaList().remove( entradaTesourariaListEntradaTesouraria );
                    oldFkBancoOfEntradaTesourariaListEntradaTesouraria = em.merge( oldFkBancoOfEntradaTesourariaListEntradaTesouraria );
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

    public void edit( TbBanco tbBanco ) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco persistentTbBanco = em.find( TbBanco.class, tbBanco.getIdBanco() );
            List<NotasCompras> notasComprasListOld = persistentTbBanco.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = tbBanco.getNotasComprasList();
            List<Notas> notasListOld = persistentTbBanco.getNotasList();
            List<Notas> notasListNew = tbBanco.getNotasList();
            List<TransferenciaBancaria> transferenciaBancariaListOld = persistentTbBanco.getTransferenciaBancariaList();
            List<TransferenciaBancaria> transferenciaBancariaListNew = tbBanco.getTransferenciaBancariaList();
            List<TransferenciaBancaria> transferenciaBancariaList1Old = persistentTbBanco.getTransferenciaBancariaList1();
            List<TransferenciaBancaria> transferenciaBancariaList1New = tbBanco.getTransferenciaBancariaList1();
            List<LevantamentoBancario> levantamentoBancarioListOld = persistentTbBanco.getLevantamentoBancarioList();
            List<LevantamentoBancario> levantamentoBancarioListNew = tbBanco.getLevantamentoBancarioList();
            List<SaidasTesouraria> saidasTesourariaListOld = persistentTbBanco.getSaidasTesourariaList();
            List<SaidasTesouraria> saidasTesourariaListNew = tbBanco.getSaidasTesourariaList();
            List<DepositoBancario> depositoBancarioListOld = persistentTbBanco.getDepositoBancarioList();
            List<DepositoBancario> depositoBancarioListNew = tbBanco.getDepositoBancarioList();
            List<TbConta> tbContaListOld = persistentTbBanco.getTbContaList();
            List<TbConta> tbContaListNew = tbBanco.getTbContaList();
            List<TbVenda> tbVendaListOld = persistentTbBanco.getTbVendaList();
            List<TbVenda> tbVendaListNew = tbBanco.getTbVendaList();
            List<EntradaTesouraria> entradaTesourariaListOld = persistentTbBanco.getEntradaTesourariaList();
            List<EntradaTesouraria> entradaTesourariaListNew = tbBanco.getEntradaTesourariaList();
            List<NotasCompras> attachedNotasComprasListNew = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNewNotasComprasToAttach : notasComprasListNew )
            {
                notasComprasListNewNotasComprasToAttach = em.getReference( notasComprasListNewNotasComprasToAttach.getClass(), notasComprasListNewNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasListNew.add( notasComprasListNewNotasComprasToAttach );
            }
            notasComprasListNew = attachedNotasComprasListNew;
            tbBanco.setNotasComprasList( notasComprasListNew );
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            tbBanco.setNotasList( notasListNew );
            List<TransferenciaBancaria> attachedTransferenciaBancariaListNew = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaListNewTransferenciaBancariaToAttach : transferenciaBancariaListNew )
            {
                transferenciaBancariaListNewTransferenciaBancariaToAttach = em.getReference( transferenciaBancariaListNewTransferenciaBancariaToAttach.getClass(), transferenciaBancariaListNewTransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaListNew.add( transferenciaBancariaListNewTransferenciaBancariaToAttach );
            }
            transferenciaBancariaListNew = attachedTransferenciaBancariaListNew;
            tbBanco.setTransferenciaBancariaList( transferenciaBancariaListNew );
            List<TransferenciaBancaria> attachedTransferenciaBancariaList1New = new ArrayList<TransferenciaBancaria>();
            for ( TransferenciaBancaria transferenciaBancariaList1NewTransferenciaBancariaToAttach : transferenciaBancariaList1New )
            {
                transferenciaBancariaList1NewTransferenciaBancariaToAttach = em.getReference( transferenciaBancariaList1NewTransferenciaBancariaToAttach.getClass(), transferenciaBancariaList1NewTransferenciaBancariaToAttach.getPkTransferencia() );
                attachedTransferenciaBancariaList1New.add( transferenciaBancariaList1NewTransferenciaBancariaToAttach );
            }
            transferenciaBancariaList1New = attachedTransferenciaBancariaList1New;
            tbBanco.setTransferenciaBancariaList1( transferenciaBancariaList1New );
            List<LevantamentoBancario> attachedLevantamentoBancarioListNew = new ArrayList<LevantamentoBancario>();
            for ( LevantamentoBancario levantamentoBancarioListNewLevantamentoBancarioToAttach : levantamentoBancarioListNew )
            {
                levantamentoBancarioListNewLevantamentoBancarioToAttach = em.getReference( levantamentoBancarioListNewLevantamentoBancarioToAttach.getClass(), levantamentoBancarioListNewLevantamentoBancarioToAttach.getPkLevantamento() );
                attachedLevantamentoBancarioListNew.add( levantamentoBancarioListNewLevantamentoBancarioToAttach );
            }
            levantamentoBancarioListNew = attachedLevantamentoBancarioListNew;
            tbBanco.setLevantamentoBancarioList( levantamentoBancarioListNew );
            List<SaidasTesouraria> attachedSaidasTesourariaListNew = new ArrayList<SaidasTesouraria>();
            for ( SaidasTesouraria saidasTesourariaListNewSaidasTesourariaToAttach : saidasTesourariaListNew )
            {
                saidasTesourariaListNewSaidasTesourariaToAttach = em.getReference( saidasTesourariaListNewSaidasTesourariaToAttach.getClass(), saidasTesourariaListNewSaidasTesourariaToAttach.getPkSaidasTesouraria() );
                attachedSaidasTesourariaListNew.add( saidasTesourariaListNewSaidasTesourariaToAttach );
            }
            saidasTesourariaListNew = attachedSaidasTesourariaListNew;
            tbBanco.setSaidasTesourariaList( saidasTesourariaListNew );
            List<DepositoBancario> attachedDepositoBancarioListNew = new ArrayList<DepositoBancario>();
            for ( DepositoBancario depositoBancarioListNewDepositoBancarioToAttach : depositoBancarioListNew )
            {
                depositoBancarioListNewDepositoBancarioToAttach = em.getReference( depositoBancarioListNewDepositoBancarioToAttach.getClass(), depositoBancarioListNewDepositoBancarioToAttach.getPkDeposito() );
                attachedDepositoBancarioListNew.add( depositoBancarioListNewDepositoBancarioToAttach );
            }
            depositoBancarioListNew = attachedDepositoBancarioListNew;
            tbBanco.setDepositoBancarioList( depositoBancarioListNew );
            List<TbConta> attachedTbContaListNew = new ArrayList<TbConta>();
            for ( TbConta tbContaListNewTbContaToAttach : tbContaListNew )
            {
                tbContaListNewTbContaToAttach = em.getReference( tbContaListNewTbContaToAttach.getClass(), tbContaListNewTbContaToAttach.getIdContaPK() );
                attachedTbContaListNew.add( tbContaListNewTbContaToAttach );
            }
            tbContaListNew = attachedTbContaListNew;
            tbBanco.setTbContaList( tbContaListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            tbBanco.setTbVendaList( tbVendaListNew );
            List<EntradaTesouraria> attachedEntradaTesourariaListNew = new ArrayList<EntradaTesouraria>();
            for ( EntradaTesouraria entradaTesourariaListNewEntradaTesourariaToAttach : entradaTesourariaListNew )
            {
                entradaTesourariaListNewEntradaTesourariaToAttach = em.getReference( entradaTesourariaListNewEntradaTesourariaToAttach.getClass(), entradaTesourariaListNewEntradaTesourariaToAttach.getPkEntradaTesouraria() );
                attachedEntradaTesourariaListNew.add( entradaTesourariaListNewEntradaTesourariaToAttach );
            }
            entradaTesourariaListNew = attachedEntradaTesourariaListNew;
            tbBanco.setEntradaTesourariaList( entradaTesourariaListNew );
            tbBanco = em.merge( tbBanco );
            for ( NotasCompras notasComprasListOldNotasCompras : notasComprasListOld )
            {
                if ( !notasComprasListNew.contains( notasComprasListOldNotasCompras ) )
                {
                    notasComprasListOldNotasCompras.setIdBanco( null );
                    notasComprasListOldNotasCompras = em.merge( notasComprasListOldNotasCompras );
                }
            }
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    TbBanco oldIdBancoOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getIdBanco();
                    notasComprasListNewNotasCompras.setIdBanco( tbBanco );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldIdBancoOfNotasComprasListNewNotasCompras != null && !oldIdBancoOfNotasComprasListNewNotasCompras.equals( tbBanco ) )
                    {
                        oldIdBancoOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldIdBancoOfNotasComprasListNewNotasCompras = em.merge( oldIdBancoOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Notas notasListOldNotas : notasListOld )
            {
                if ( !notasListNew.contains( notasListOldNotas ) )
                {
                    notasListOldNotas.setIdBanco( null );
                    notasListOldNotas = em.merge( notasListOldNotas );
                }
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    TbBanco oldIdBancoOfNotasListNewNotas = notasListNewNotas.getIdBanco();
                    notasListNewNotas.setIdBanco( tbBanco );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldIdBancoOfNotasListNewNotas != null && !oldIdBancoOfNotasListNewNotas.equals( tbBanco ) )
                    {
                        oldIdBancoOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldIdBancoOfNotasListNewNotas = em.merge( oldIdBancoOfNotasListNewNotas );
                    }
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListOldTransferenciaBancaria : transferenciaBancariaListOld )
            {
                if ( !transferenciaBancariaListNew.contains( transferenciaBancariaListOldTransferenciaBancaria ) )
                {
                    transferenciaBancariaListOldTransferenciaBancaria.setFkBancoDestino( null );
                    transferenciaBancariaListOldTransferenciaBancaria = em.merge( transferenciaBancariaListOldTransferenciaBancaria );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaListNewTransferenciaBancaria : transferenciaBancariaListNew )
            {
                if ( !transferenciaBancariaListOld.contains( transferenciaBancariaListNewTransferenciaBancaria ) )
                {
                    TbBanco oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria = transferenciaBancariaListNewTransferenciaBancaria.getFkBancoDestino();
                    transferenciaBancariaListNewTransferenciaBancaria.setFkBancoDestino( tbBanco );
                    transferenciaBancariaListNewTransferenciaBancaria = em.merge( transferenciaBancariaListNewTransferenciaBancaria );
                    if ( oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria != null && !oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria.equals( tbBanco ) )
                    {
                        oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria.getTransferenciaBancariaList().remove( transferenciaBancariaListNewTransferenciaBancaria );
                        oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria = em.merge( oldFkBancoDestinoOfTransferenciaBancariaListNewTransferenciaBancaria );
                    }
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaList1OldTransferenciaBancaria : transferenciaBancariaList1Old )
            {
                if ( !transferenciaBancariaList1New.contains( transferenciaBancariaList1OldTransferenciaBancaria ) )
                {
                    transferenciaBancariaList1OldTransferenciaBancaria.setFkBancoOrigem( null );
                    transferenciaBancariaList1OldTransferenciaBancaria = em.merge( transferenciaBancariaList1OldTransferenciaBancaria );
                }
            }
            for ( TransferenciaBancaria transferenciaBancariaList1NewTransferenciaBancaria : transferenciaBancariaList1New )
            {
                if ( !transferenciaBancariaList1Old.contains( transferenciaBancariaList1NewTransferenciaBancaria ) )
                {
                    TbBanco oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria = transferenciaBancariaList1NewTransferenciaBancaria.getFkBancoOrigem();
                    transferenciaBancariaList1NewTransferenciaBancaria.setFkBancoOrigem( tbBanco );
                    transferenciaBancariaList1NewTransferenciaBancaria = em.merge( transferenciaBancariaList1NewTransferenciaBancaria );
                    if ( oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria != null && !oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria.equals( tbBanco ) )
                    {
                        oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria.getTransferenciaBancariaList1().remove( transferenciaBancariaList1NewTransferenciaBancaria );
                        oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria = em.merge( oldFkBancoOrigemOfTransferenciaBancariaList1NewTransferenciaBancaria );
                    }
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListOldLevantamentoBancario : levantamentoBancarioListOld )
            {
                if ( !levantamentoBancarioListNew.contains( levantamentoBancarioListOldLevantamentoBancario ) )
                {
                    levantamentoBancarioListOldLevantamentoBancario.setFkBanco( null );
                    levantamentoBancarioListOldLevantamentoBancario = em.merge( levantamentoBancarioListOldLevantamentoBancario );
                }
            }
            for ( LevantamentoBancario levantamentoBancarioListNewLevantamentoBancario : levantamentoBancarioListNew )
            {
                if ( !levantamentoBancarioListOld.contains( levantamentoBancarioListNewLevantamentoBancario ) )
                {
                    TbBanco oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario = levantamentoBancarioListNewLevantamentoBancario.getFkBanco();
                    levantamentoBancarioListNewLevantamentoBancario.setFkBanco( tbBanco );
                    levantamentoBancarioListNewLevantamentoBancario = em.merge( levantamentoBancarioListNewLevantamentoBancario );
                    if ( oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario != null && !oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario.equals( tbBanco ) )
                    {
                        oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario.getLevantamentoBancarioList().remove( levantamentoBancarioListNewLevantamentoBancario );
                        oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario = em.merge( oldFkBancoOfLevantamentoBancarioListNewLevantamentoBancario );
                    }
                }
            }
            for ( SaidasTesouraria saidasTesourariaListOldSaidasTesouraria : saidasTesourariaListOld )
            {
                if ( !saidasTesourariaListNew.contains( saidasTesourariaListOldSaidasTesouraria ) )
                {
                    saidasTesourariaListOldSaidasTesouraria.setFkBanco( null );
                    saidasTesourariaListOldSaidasTesouraria = em.merge( saidasTesourariaListOldSaidasTesouraria );
                }
            }
            for ( SaidasTesouraria saidasTesourariaListNewSaidasTesouraria : saidasTesourariaListNew )
            {
                if ( !saidasTesourariaListOld.contains( saidasTesourariaListNewSaidasTesouraria ) )
                {
                    TbBanco oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria = saidasTesourariaListNewSaidasTesouraria.getFkBanco();
                    saidasTesourariaListNewSaidasTesouraria.setFkBanco( tbBanco );
                    saidasTesourariaListNewSaidasTesouraria = em.merge( saidasTesourariaListNewSaidasTesouraria );
                    if ( oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria != null && !oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria.equals( tbBanco ) )
                    {
                        oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria.getSaidasTesourariaList().remove( saidasTesourariaListNewSaidasTesouraria );
                        oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria = em.merge( oldFkBancoOfSaidasTesourariaListNewSaidasTesouraria );
                    }
                }
            }
            for ( DepositoBancario depositoBancarioListOldDepositoBancario : depositoBancarioListOld )
            {
                if ( !depositoBancarioListNew.contains( depositoBancarioListOldDepositoBancario ) )
                {
                    depositoBancarioListOldDepositoBancario.setFkBanco( null );
                    depositoBancarioListOldDepositoBancario = em.merge( depositoBancarioListOldDepositoBancario );
                }
            }
            for ( DepositoBancario depositoBancarioListNewDepositoBancario : depositoBancarioListNew )
            {
                if ( !depositoBancarioListOld.contains( depositoBancarioListNewDepositoBancario ) )
                {
                    TbBanco oldFkBancoOfDepositoBancarioListNewDepositoBancario = depositoBancarioListNewDepositoBancario.getFkBanco();
                    depositoBancarioListNewDepositoBancario.setFkBanco( tbBanco );
                    depositoBancarioListNewDepositoBancario = em.merge( depositoBancarioListNewDepositoBancario );
                    if ( oldFkBancoOfDepositoBancarioListNewDepositoBancario != null && !oldFkBancoOfDepositoBancarioListNewDepositoBancario.equals( tbBanco ) )
                    {
                        oldFkBancoOfDepositoBancarioListNewDepositoBancario.getDepositoBancarioList().remove( depositoBancarioListNewDepositoBancario );
                        oldFkBancoOfDepositoBancarioListNewDepositoBancario = em.merge( oldFkBancoOfDepositoBancarioListNewDepositoBancario );
                    }
                }
            }
            for ( TbConta tbContaListOldTbConta : tbContaListOld )
            {
                if ( !tbContaListNew.contains( tbContaListOldTbConta ) )
                {
                    tbContaListOldTbConta.setIdBancoFK( null );
                    tbContaListOldTbConta = em.merge( tbContaListOldTbConta );
                }
            }
            for ( TbConta tbContaListNewTbConta : tbContaListNew )
            {
                if ( !tbContaListOld.contains( tbContaListNewTbConta ) )
                {
                    TbBanco oldIdBancoFKOfTbContaListNewTbConta = tbContaListNewTbConta.getIdBancoFK();
                    tbContaListNewTbConta.setIdBancoFK( tbBanco );
                    tbContaListNewTbConta = em.merge( tbContaListNewTbConta );
                    if ( oldIdBancoFKOfTbContaListNewTbConta != null && !oldIdBancoFKOfTbContaListNewTbConta.equals( tbBanco ) )
                    {
                        oldIdBancoFKOfTbContaListNewTbConta.getTbContaList().remove( tbContaListNewTbConta );
                        oldIdBancoFKOfTbContaListNewTbConta = em.merge( oldIdBancoFKOfTbContaListNewTbConta );
                    }
                }
            }
            for ( TbVenda tbVendaListOldTbVenda : tbVendaListOld )
            {
                if ( !tbVendaListNew.contains( tbVendaListOldTbVenda ) )
                {
                    tbVendaListOldTbVenda.setIdBanco( null );
                    tbVendaListOldTbVenda = em.merge( tbVendaListOldTbVenda );
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    TbBanco oldIdBancoOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getIdBanco();
                    tbVendaListNewTbVenda.setIdBanco( tbBanco );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldIdBancoOfTbVendaListNewTbVenda != null && !oldIdBancoOfTbVendaListNewTbVenda.equals( tbBanco ) )
                    {
                        oldIdBancoOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldIdBancoOfTbVendaListNewTbVenda = em.merge( oldIdBancoOfTbVendaListNewTbVenda );
                    }
                }
            }
            for ( EntradaTesouraria entradaTesourariaListOldEntradaTesouraria : entradaTesourariaListOld )
            {
                if ( !entradaTesourariaListNew.contains( entradaTesourariaListOldEntradaTesouraria ) )
                {
                    entradaTesourariaListOldEntradaTesouraria.setFkBanco( null );
                    entradaTesourariaListOldEntradaTesouraria = em.merge( entradaTesourariaListOldEntradaTesouraria );
                }
            }
            for ( EntradaTesouraria entradaTesourariaListNewEntradaTesouraria : entradaTesourariaListNew )
            {
                if ( !entradaTesourariaListOld.contains( entradaTesourariaListNewEntradaTesouraria ) )
                {
                    TbBanco oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria = entradaTesourariaListNewEntradaTesouraria.getFkBanco();
                    entradaTesourariaListNewEntradaTesouraria.setFkBanco( tbBanco );
                    entradaTesourariaListNewEntradaTesouraria = em.merge( entradaTesourariaListNewEntradaTesouraria );
                    if ( oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria != null && !oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria.equals( tbBanco ) )
                    {
                        oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria.getEntradaTesourariaList().remove( entradaTesourariaListNewEntradaTesouraria );
                        oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria = em.merge( oldFkBancoOfEntradaTesourariaListNewEntradaTesouraria );
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
                Integer id = tbBanco.getIdBanco();
                if ( findTbBanco( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbBanco with id " + id + " no longer exists." );
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

    public void destroy( Integer id ) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbBanco tbBanco;
            try
            {
                tbBanco = em.getReference( TbBanco.class, id );
                tbBanco.getIdBanco();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbBanco with id " + id + " no longer exists.", enfe );
            }
            List<NotasCompras> notasComprasList = tbBanco.getNotasComprasList();
            for ( NotasCompras notasComprasListNotasCompras : notasComprasList )
            {
                notasComprasListNotasCompras.setIdBanco( null );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
            }
            List<Notas> notasList = tbBanco.getNotasList();
            for ( Notas notasListNotas : notasList )
            {
                notasListNotas.setIdBanco( null );
                notasListNotas = em.merge( notasListNotas );
            }
            List<TransferenciaBancaria> transferenciaBancariaList = tbBanco.getTransferenciaBancariaList();
            for ( TransferenciaBancaria transferenciaBancariaListTransferenciaBancaria : transferenciaBancariaList )
            {
                transferenciaBancariaListTransferenciaBancaria.setFkBancoDestino( null );
                transferenciaBancariaListTransferenciaBancaria = em.merge( transferenciaBancariaListTransferenciaBancaria );
            }
            List<TransferenciaBancaria> transferenciaBancariaList1 = tbBanco.getTransferenciaBancariaList1();
            for ( TransferenciaBancaria transferenciaBancariaList1TransferenciaBancaria : transferenciaBancariaList1 )
            {
                transferenciaBancariaList1TransferenciaBancaria.setFkBancoOrigem( null );
                transferenciaBancariaList1TransferenciaBancaria = em.merge( transferenciaBancariaList1TransferenciaBancaria );
            }
            List<LevantamentoBancario> levantamentoBancarioList = tbBanco.getLevantamentoBancarioList();
            for ( LevantamentoBancario levantamentoBancarioListLevantamentoBancario : levantamentoBancarioList )
            {
                levantamentoBancarioListLevantamentoBancario.setFkBanco( null );
                levantamentoBancarioListLevantamentoBancario = em.merge( levantamentoBancarioListLevantamentoBancario );
            }
            List<SaidasTesouraria> saidasTesourariaList = tbBanco.getSaidasTesourariaList();
            for ( SaidasTesouraria saidasTesourariaListSaidasTesouraria : saidasTesourariaList )
            {
                saidasTesourariaListSaidasTesouraria.setFkBanco( null );
                saidasTesourariaListSaidasTesouraria = em.merge( saidasTesourariaListSaidasTesouraria );
            }
            List<DepositoBancario> depositoBancarioList = tbBanco.getDepositoBancarioList();
            for ( DepositoBancario depositoBancarioListDepositoBancario : depositoBancarioList )
            {
                depositoBancarioListDepositoBancario.setFkBanco( null );
                depositoBancarioListDepositoBancario = em.merge( depositoBancarioListDepositoBancario );
            }
            List<TbConta> tbContaList = tbBanco.getTbContaList();
            for ( TbConta tbContaListTbConta : tbContaList )
            {
                tbContaListTbConta.setIdBancoFK( null );
                tbContaListTbConta = em.merge( tbContaListTbConta );
            }
            List<TbVenda> tbVendaList = tbBanco.getTbVendaList();
            for ( TbVenda tbVendaListTbVenda : tbVendaList )
            {
                tbVendaListTbVenda.setIdBanco( null );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
            }
            List<EntradaTesouraria> entradaTesourariaList = tbBanco.getEntradaTesourariaList();
            for ( EntradaTesouraria entradaTesourariaListEntradaTesouraria : entradaTesourariaList )
            {
                entradaTesourariaListEntradaTesouraria.setFkBanco( null );
                entradaTesourariaListEntradaTesouraria = em.merge( entradaTesourariaListEntradaTesouraria );
            }
            em.remove( tbBanco );
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

    public List<TbBanco> findTbBancoEntities()
    {
        return findTbBancoEntities( true, -1, -1 );
    }

    public List<TbBanco> findTbBancoEntities( int maxResults, int firstResult )
    {
        return findTbBancoEntities( false, maxResults, firstResult );
    }

    private List<TbBanco> findTbBancoEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbBanco.class ) );
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

    public TbBanco findTbBanco( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbBanco.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbBancoCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbBanco> rt = cq.from( TbBanco.class );
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
