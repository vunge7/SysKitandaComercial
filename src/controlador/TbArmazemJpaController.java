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
import entity.TbStockMirrow;
import java.util.ArrayList;
import java.util.List;
import entity.NotasCompras;
import entity.Notas;
import entity.Compras;
import entity.TbEstorno;
import entity.TbAcerto;
import entity.TbVasilhame;
import entity.TbSaidasProdutos;
import entity.TbEntrada;
import entity.TbEntradaVasilhame;
import entity.TbVenda;
import entity.TbStock;
import entity.AccessoArmazem;
import entity.TbArmazem;
import entity.TbSaidaVasilhame;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author marti
 */
public class TbArmazemJpaController implements Serializable
{

    public TbArmazemJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbArmazem tbArmazem )
    {
        if ( tbArmazem.getTbStockMirrowList() == null )
        {
            tbArmazem.setTbStockMirrowList( new ArrayList<TbStockMirrow>() );
        }
        if ( tbArmazem.getNotasComprasList() == null )
        {
            tbArmazem.setNotasComprasList( new ArrayList<NotasCompras>() );
        }
        if ( tbArmazem.getNotasList() == null )
        {
            tbArmazem.setNotasList( new ArrayList<Notas>() );
        }
        if ( tbArmazem.getComprasList() == null )
        {
            tbArmazem.setComprasList( new ArrayList<Compras>() );
        }
        if ( tbArmazem.getTbEstornoList() == null )
        {
            tbArmazem.setTbEstornoList( new ArrayList<TbEstorno>() );
        }
        if ( tbArmazem.getTbAcertoList() == null )
        {
            tbArmazem.setTbAcertoList( new ArrayList<TbAcerto>() );
        }
        if ( tbArmazem.getTbVasilhameList() == null )
        {
            tbArmazem.setTbVasilhameList( new ArrayList<TbVasilhame>() );
        }
        if ( tbArmazem.getTbSaidasProdutosList() == null )
        {
            tbArmazem.setTbSaidasProdutosList( new ArrayList<TbSaidasProdutos>() );
        }
        if ( tbArmazem.getTbEntradaList() == null )
        {
            tbArmazem.setTbEntradaList( new ArrayList<TbEntrada>() );
        }
        if ( tbArmazem.getTbEntradaVasilhameList() == null )
        {
            tbArmazem.setTbEntradaVasilhameList( new ArrayList<TbEntradaVasilhame>() );
        }
        if ( tbArmazem.getTbVendaList() == null )
        {
            tbArmazem.setTbVendaList( new ArrayList<TbVenda>() );
        }
        if ( tbArmazem.getTbStockList() == null )
        {
            tbArmazem.setTbStockList( new ArrayList<TbStock>() );
        }
        if ( tbArmazem.getAccessoArmazemList() == null )
        {
            tbArmazem.setAccessoArmazemList( new ArrayList<AccessoArmazem>() );
        }
        if ( tbArmazem.getTbSaidaVasilhameList() == null )
        {
            tbArmazem.setTbSaidaVasilhameList( new ArrayList<TbSaidaVasilhame>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TbStockMirrow> attachedTbStockMirrowList = new ArrayList<TbStockMirrow>();
            for ( TbStockMirrow tbStockMirrowListTbStockMirrowToAttach : tbArmazem.getTbStockMirrowList() )
            {
                tbStockMirrowListTbStockMirrowToAttach = em.getReference( tbStockMirrowListTbStockMirrowToAttach.getClass(), tbStockMirrowListTbStockMirrowToAttach.getCodigo() );
                attachedTbStockMirrowList.add( tbStockMirrowListTbStockMirrowToAttach );
            }
            tbArmazem.setTbStockMirrowList( attachedTbStockMirrowList );
            List<NotasCompras> attachedNotasComprasList = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNotasComprasToAttach : tbArmazem.getNotasComprasList() )
            {
                notasComprasListNotasComprasToAttach = em.getReference( notasComprasListNotasComprasToAttach.getClass(), notasComprasListNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasList.add( notasComprasListNotasComprasToAttach );
            }
            tbArmazem.setNotasComprasList( attachedNotasComprasList );
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for ( Notas notasListNotasToAttach : tbArmazem.getNotasList() )
            {
                notasListNotasToAttach = em.getReference( notasListNotasToAttach.getClass(), notasListNotasToAttach.getPkNota() );
                attachedNotasList.add( notasListNotasToAttach );
            }
            tbArmazem.setNotasList( attachedNotasList );
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for ( Compras comprasListComprasToAttach : tbArmazem.getComprasList() )
            {
                comprasListComprasToAttach = em.getReference( comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getPkCompra() );
                attachedComprasList.add( comprasListComprasToAttach );
            }
            tbArmazem.setComprasList( attachedComprasList );
            List<TbEstorno> attachedTbEstornoList = new ArrayList<TbEstorno>();
            for ( TbEstorno tbEstornoListTbEstornoToAttach : tbArmazem.getTbEstornoList() )
            {
                tbEstornoListTbEstornoToAttach = em.getReference( tbEstornoListTbEstornoToAttach.getClass(), tbEstornoListTbEstornoToAttach.getPkEstorno() );
                attachedTbEstornoList.add( tbEstornoListTbEstornoToAttach );
            }
            tbArmazem.setTbEstornoList( attachedTbEstornoList );
            List<TbAcerto> attachedTbAcertoList = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListTbAcertoToAttach : tbArmazem.getTbAcertoList() )
            {
                tbAcertoListTbAcertoToAttach = em.getReference( tbAcertoListTbAcertoToAttach.getClass(), tbAcertoListTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoList.add( tbAcertoListTbAcertoToAttach );
            }
            tbArmazem.setTbAcertoList( attachedTbAcertoList );
            List<TbVasilhame> attachedTbVasilhameList = new ArrayList<TbVasilhame>();
            for ( TbVasilhame tbVasilhameListTbVasilhameToAttach : tbArmazem.getTbVasilhameList() )
            {
                tbVasilhameListTbVasilhameToAttach = em.getReference( tbVasilhameListTbVasilhameToAttach.getClass(), tbVasilhameListTbVasilhameToAttach.getPkVasilhame() );
                attachedTbVasilhameList.add( tbVasilhameListTbVasilhameToAttach );
            }
            tbArmazem.setTbVasilhameList( attachedTbVasilhameList );
            List<TbSaidasProdutos> attachedTbSaidasProdutosList = new ArrayList<TbSaidasProdutos>();
            for ( TbSaidasProdutos tbSaidasProdutosListTbSaidasProdutosToAttach : tbArmazem.getTbSaidasProdutosList() )
            {
                tbSaidasProdutosListTbSaidasProdutosToAttach = em.getReference( tbSaidasProdutosListTbSaidasProdutosToAttach.getClass(), tbSaidasProdutosListTbSaidasProdutosToAttach.getPkSaidasProdutos() );
                attachedTbSaidasProdutosList.add( tbSaidasProdutosListTbSaidasProdutosToAttach );
            }
            tbArmazem.setTbSaidasProdutosList( attachedTbSaidasProdutosList );
            List<TbEntrada> attachedTbEntradaList = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListTbEntradaToAttach : tbArmazem.getTbEntradaList() )
            {
                tbEntradaListTbEntradaToAttach = em.getReference( tbEntradaListTbEntradaToAttach.getClass(), tbEntradaListTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaList.add( tbEntradaListTbEntradaToAttach );
            }
            tbArmazem.setTbEntradaList( attachedTbEntradaList );
            List<TbEntradaVasilhame> attachedTbEntradaVasilhameList = new ArrayList<TbEntradaVasilhame>();
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhameToAttach : tbArmazem.getTbEntradaVasilhameList() )
            {
                tbEntradaVasilhameListTbEntradaVasilhameToAttach = em.getReference( tbEntradaVasilhameListTbEntradaVasilhameToAttach.getClass(), tbEntradaVasilhameListTbEntradaVasilhameToAttach.getPkEntradaVasilhame() );
                attachedTbEntradaVasilhameList.add( tbEntradaVasilhameListTbEntradaVasilhameToAttach );
            }
            tbArmazem.setTbEntradaVasilhameList( attachedTbEntradaVasilhameList );
            List<TbVenda> attachedTbVendaList = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListTbVendaToAttach : tbArmazem.getTbVendaList() )
            {
                tbVendaListTbVendaToAttach = em.getReference( tbVendaListTbVendaToAttach.getClass(), tbVendaListTbVendaToAttach.getCodigo() );
                attachedTbVendaList.add( tbVendaListTbVendaToAttach );
            }
            tbArmazem.setTbVendaList( attachedTbVendaList );
            List<TbStock> attachedTbStockList = new ArrayList<TbStock>();
            for ( TbStock tbStockListTbStockToAttach : tbArmazem.getTbStockList() )
            {
                tbStockListTbStockToAttach = em.getReference( tbStockListTbStockToAttach.getClass(), tbStockListTbStockToAttach.getCodigo() );
                attachedTbStockList.add( tbStockListTbStockToAttach );
            }
            tbArmazem.setTbStockList( attachedTbStockList );
            List<AccessoArmazem> attachedAccessoArmazemList = new ArrayList<AccessoArmazem>();
            for ( AccessoArmazem accessoArmazemListAccessoArmazemToAttach : tbArmazem.getAccessoArmazemList() )
            {
                accessoArmazemListAccessoArmazemToAttach = em.getReference( accessoArmazemListAccessoArmazemToAttach.getClass(), accessoArmazemListAccessoArmazemToAttach.getPkAccessoArmazem() );
                attachedAccessoArmazemList.add( accessoArmazemListAccessoArmazemToAttach );
            }
            tbArmazem.setAccessoArmazemList( attachedAccessoArmazemList );
            List<TbSaidaVasilhame> attachedTbSaidaVasilhameList = new ArrayList<TbSaidaVasilhame>();
            for ( TbSaidaVasilhame tbSaidaVasilhameListTbSaidaVasilhameToAttach : tbArmazem.getTbSaidaVasilhameList() )
            {
                tbSaidaVasilhameListTbSaidaVasilhameToAttach = em.getReference( tbSaidaVasilhameListTbSaidaVasilhameToAttach.getClass(), tbSaidaVasilhameListTbSaidaVasilhameToAttach.getPkSaidaVasilhame() );
                attachedTbSaidaVasilhameList.add( tbSaidaVasilhameListTbSaidaVasilhameToAttach );
            }
            tbArmazem.setTbSaidaVasilhameList( attachedTbSaidaVasilhameList );
            em.persist( tbArmazem );
            for ( TbStockMirrow tbStockMirrowListTbStockMirrow : tbArmazem.getTbStockMirrowList() )
            {
                TbArmazem oldCodArmazemOfTbStockMirrowListTbStockMirrow = tbStockMirrowListTbStockMirrow.getCodArmazem();
                tbStockMirrowListTbStockMirrow.setCodArmazem( tbArmazem );
                tbStockMirrowListTbStockMirrow = em.merge( tbStockMirrowListTbStockMirrow );
                if ( oldCodArmazemOfTbStockMirrowListTbStockMirrow != null )
                {
                    oldCodArmazemOfTbStockMirrowListTbStockMirrow.getTbStockMirrowList().remove( tbStockMirrowListTbStockMirrow );
                    oldCodArmazemOfTbStockMirrowListTbStockMirrow = em.merge( oldCodArmazemOfTbStockMirrowListTbStockMirrow );
                }
            }
            for ( NotasCompras notasComprasListNotasCompras : tbArmazem.getNotasComprasList() )
            {
                TbArmazem oldIdArmazemFKOfNotasComprasListNotasCompras = notasComprasListNotasCompras.getIdArmazemFK();
                notasComprasListNotasCompras.setIdArmazemFK( tbArmazem );
                notasComprasListNotasCompras = em.merge( notasComprasListNotasCompras );
                if ( oldIdArmazemFKOfNotasComprasListNotasCompras != null )
                {
                    oldIdArmazemFKOfNotasComprasListNotasCompras.getNotasComprasList().remove( notasComprasListNotasCompras );
                    oldIdArmazemFKOfNotasComprasListNotasCompras = em.merge( oldIdArmazemFKOfNotasComprasListNotasCompras );
                }
            }
            for ( Notas notasListNotas : tbArmazem.getNotasList() )
            {
                TbArmazem oldIdArmazemFKOfNotasListNotas = notasListNotas.getIdArmazemFK();
                notasListNotas.setIdArmazemFK( tbArmazem );
                notasListNotas = em.merge( notasListNotas );
                if ( oldIdArmazemFKOfNotasListNotas != null )
                {
                    oldIdArmazemFKOfNotasListNotas.getNotasList().remove( notasListNotas );
                    oldIdArmazemFKOfNotasListNotas = em.merge( oldIdArmazemFKOfNotasListNotas );
                }
            }
            for ( Compras comprasListCompras : tbArmazem.getComprasList() )
            {
                TbArmazem oldIdArmazemFKOfComprasListCompras = comprasListCompras.getIdArmazemFK();
                comprasListCompras.setIdArmazemFK( tbArmazem );
                comprasListCompras = em.merge( comprasListCompras );
                if ( oldIdArmazemFKOfComprasListCompras != null )
                {
                    oldIdArmazemFKOfComprasListCompras.getComprasList().remove( comprasListCompras );
                    oldIdArmazemFKOfComprasListCompras = em.merge( oldIdArmazemFKOfComprasListCompras );
                }
            }
            for ( TbEstorno tbEstornoListTbEstorno : tbArmazem.getTbEstornoList() )
            {
                TbArmazem oldIdArmazemFKOfTbEstornoListTbEstorno = tbEstornoListTbEstorno.getIdArmazemFK();
                tbEstornoListTbEstorno.setIdArmazemFK( tbArmazem );
                tbEstornoListTbEstorno = em.merge( tbEstornoListTbEstorno );
                if ( oldIdArmazemFKOfTbEstornoListTbEstorno != null )
                {
                    oldIdArmazemFKOfTbEstornoListTbEstorno.getTbEstornoList().remove( tbEstornoListTbEstorno );
                    oldIdArmazemFKOfTbEstornoListTbEstorno = em.merge( oldIdArmazemFKOfTbEstornoListTbEstorno );
                }
            }
            for ( TbAcerto tbAcertoListTbAcerto : tbArmazem.getTbAcertoList() )
            {
                TbArmazem oldIdArmazemFKOfTbAcertoListTbAcerto = tbAcertoListTbAcerto.getIdArmazemFK();
                tbAcertoListTbAcerto.setIdArmazemFK( tbArmazem );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
                if ( oldIdArmazemFKOfTbAcertoListTbAcerto != null )
                {
                    oldIdArmazemFKOfTbAcertoListTbAcerto.getTbAcertoList().remove( tbAcertoListTbAcerto );
                    oldIdArmazemFKOfTbAcertoListTbAcerto = em.merge( oldIdArmazemFKOfTbAcertoListTbAcerto );
                }
            }
            for ( TbVasilhame tbVasilhameListTbVasilhame : tbArmazem.getTbVasilhameList() )
            {
                TbArmazem oldFkArmazemOfTbVasilhameListTbVasilhame = tbVasilhameListTbVasilhame.getFkArmazem();
                tbVasilhameListTbVasilhame.setFkArmazem( tbArmazem );
                tbVasilhameListTbVasilhame = em.merge( tbVasilhameListTbVasilhame );
                if ( oldFkArmazemOfTbVasilhameListTbVasilhame != null )
                {
                    oldFkArmazemOfTbVasilhameListTbVasilhame.getTbVasilhameList().remove( tbVasilhameListTbVasilhame );
                    oldFkArmazemOfTbVasilhameListTbVasilhame = em.merge( oldFkArmazemOfTbVasilhameListTbVasilhame );
                }
            }
            for ( TbSaidasProdutos tbSaidasProdutosListTbSaidasProdutos : tbArmazem.getTbSaidasProdutosList() )
            {
                TbArmazem oldIdArmazemFKOfTbSaidasProdutosListTbSaidasProdutos = tbSaidasProdutosListTbSaidasProdutos.getIdArmazemFK();
                tbSaidasProdutosListTbSaidasProdutos.setIdArmazemFK( tbArmazem );
                tbSaidasProdutosListTbSaidasProdutos = em.merge( tbSaidasProdutosListTbSaidasProdutos );
                if ( oldIdArmazemFKOfTbSaidasProdutosListTbSaidasProdutos != null )
                {
                    oldIdArmazemFKOfTbSaidasProdutosListTbSaidasProdutos.getTbSaidasProdutosList().remove( tbSaidasProdutosListTbSaidasProdutos );
                    oldIdArmazemFKOfTbSaidasProdutosListTbSaidasProdutos = em.merge( oldIdArmazemFKOfTbSaidasProdutosListTbSaidasProdutos );
                }
            }
            for ( TbEntrada tbEntradaListTbEntrada : tbArmazem.getTbEntradaList() )
            {
                TbArmazem oldIdArmazemFKOfTbEntradaListTbEntrada = tbEntradaListTbEntrada.getIdArmazemFK();
                tbEntradaListTbEntrada.setIdArmazemFK( tbArmazem );
                tbEntradaListTbEntrada = em.merge( tbEntradaListTbEntrada );
                if ( oldIdArmazemFKOfTbEntradaListTbEntrada != null )
                {
                    oldIdArmazemFKOfTbEntradaListTbEntrada.getTbEntradaList().remove( tbEntradaListTbEntrada );
                    oldIdArmazemFKOfTbEntradaListTbEntrada = em.merge( oldIdArmazemFKOfTbEntradaListTbEntrada );
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhame : tbArmazem.getTbEntradaVasilhameList() )
            {
                TbArmazem oldFkAmazemOfTbEntradaVasilhameListTbEntradaVasilhame = tbEntradaVasilhameListTbEntradaVasilhame.getFkAmazem();
                tbEntradaVasilhameListTbEntradaVasilhame.setFkAmazem( tbArmazem );
                tbEntradaVasilhameListTbEntradaVasilhame = em.merge( tbEntradaVasilhameListTbEntradaVasilhame );
                if ( oldFkAmazemOfTbEntradaVasilhameListTbEntradaVasilhame != null )
                {
                    oldFkAmazemOfTbEntradaVasilhameListTbEntradaVasilhame.getTbEntradaVasilhameList().remove( tbEntradaVasilhameListTbEntradaVasilhame );
                    oldFkAmazemOfTbEntradaVasilhameListTbEntradaVasilhame = em.merge( oldFkAmazemOfTbEntradaVasilhameListTbEntradaVasilhame );
                }
            }
            for ( TbVenda tbVendaListTbVenda : tbArmazem.getTbVendaList() )
            {
                TbArmazem oldIdArmazemFKOfTbVendaListTbVenda = tbVendaListTbVenda.getIdArmazemFK();
                tbVendaListTbVenda.setIdArmazemFK( tbArmazem );
                tbVendaListTbVenda = em.merge( tbVendaListTbVenda );
                if ( oldIdArmazemFKOfTbVendaListTbVenda != null )
                {
                    oldIdArmazemFKOfTbVendaListTbVenda.getTbVendaList().remove( tbVendaListTbVenda );
                    oldIdArmazemFKOfTbVendaListTbVenda = em.merge( oldIdArmazemFKOfTbVendaListTbVenda );
                }
            }
            for ( TbStock tbStockListTbStock : tbArmazem.getTbStockList() )
            {
                TbArmazem oldCodArmazemOfTbStockListTbStock = tbStockListTbStock.getCodArmazem();
                tbStockListTbStock.setCodArmazem( tbArmazem );
                tbStockListTbStock = em.merge( tbStockListTbStock );
                if ( oldCodArmazemOfTbStockListTbStock != null )
                {
                    oldCodArmazemOfTbStockListTbStock.getTbStockList().remove( tbStockListTbStock );
                    oldCodArmazemOfTbStockListTbStock = em.merge( oldCodArmazemOfTbStockListTbStock );
                }
            }
            for ( AccessoArmazem accessoArmazemListAccessoArmazem : tbArmazem.getAccessoArmazemList() )
            {
                TbArmazem oldFkArmazemOfAccessoArmazemListAccessoArmazem = accessoArmazemListAccessoArmazem.getFkArmazem();
                accessoArmazemListAccessoArmazem.setFkArmazem( tbArmazem );
                accessoArmazemListAccessoArmazem = em.merge( accessoArmazemListAccessoArmazem );
                if ( oldFkArmazemOfAccessoArmazemListAccessoArmazem != null )
                {
                    oldFkArmazemOfAccessoArmazemListAccessoArmazem.getAccessoArmazemList().remove( accessoArmazemListAccessoArmazem );
                    oldFkArmazemOfAccessoArmazemListAccessoArmazem = em.merge( oldFkArmazemOfAccessoArmazemListAccessoArmazem );
                }
            }
            for ( TbSaidaVasilhame tbSaidaVasilhameListTbSaidaVasilhame : tbArmazem.getTbSaidaVasilhameList() )
            {
                TbArmazem oldFkArmazemOfTbSaidaVasilhameListTbSaidaVasilhame = tbSaidaVasilhameListTbSaidaVasilhame.getFkArmazem();
                tbSaidaVasilhameListTbSaidaVasilhame.setFkArmazem( tbArmazem );
                tbSaidaVasilhameListTbSaidaVasilhame = em.merge( tbSaidaVasilhameListTbSaidaVasilhame );
                if ( oldFkArmazemOfTbSaidaVasilhameListTbSaidaVasilhame != null )
                {
                    oldFkArmazemOfTbSaidaVasilhameListTbSaidaVasilhame.getTbSaidaVasilhameList().remove( tbSaidaVasilhameListTbSaidaVasilhame );
                    oldFkArmazemOfTbSaidaVasilhameListTbSaidaVasilhame = em.merge( oldFkArmazemOfTbSaidaVasilhameListTbSaidaVasilhame );
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

    public void edit( TbArmazem tbArmazem ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbArmazem persistentTbArmazem = em.find( TbArmazem.class, tbArmazem.getCodigo() );
            List<TbStockMirrow> tbStockMirrowListOld = persistentTbArmazem.getTbStockMirrowList();
            List<TbStockMirrow> tbStockMirrowListNew = tbArmazem.getTbStockMirrowList();
            List<NotasCompras> notasComprasListOld = persistentTbArmazem.getNotasComprasList();
            List<NotasCompras> notasComprasListNew = tbArmazem.getNotasComprasList();
            List<Notas> notasListOld = persistentTbArmazem.getNotasList();
            List<Notas> notasListNew = tbArmazem.getNotasList();
            List<Compras> comprasListOld = persistentTbArmazem.getComprasList();
            List<Compras> comprasListNew = tbArmazem.getComprasList();
            List<TbEstorno> tbEstornoListOld = persistentTbArmazem.getTbEstornoList();
            List<TbEstorno> tbEstornoListNew = tbArmazem.getTbEstornoList();
            List<TbAcerto> tbAcertoListOld = persistentTbArmazem.getTbAcertoList();
            List<TbAcerto> tbAcertoListNew = tbArmazem.getTbAcertoList();
            List<TbVasilhame> tbVasilhameListOld = persistentTbArmazem.getTbVasilhameList();
            List<TbVasilhame> tbVasilhameListNew = tbArmazem.getTbVasilhameList();
            List<TbSaidasProdutos> tbSaidasProdutosListOld = persistentTbArmazem.getTbSaidasProdutosList();
            List<TbSaidasProdutos> tbSaidasProdutosListNew = tbArmazem.getTbSaidasProdutosList();
            List<TbEntrada> tbEntradaListOld = persistentTbArmazem.getTbEntradaList();
            List<TbEntrada> tbEntradaListNew = tbArmazem.getTbEntradaList();
            List<TbEntradaVasilhame> tbEntradaVasilhameListOld = persistentTbArmazem.getTbEntradaVasilhameList();
            List<TbEntradaVasilhame> tbEntradaVasilhameListNew = tbArmazem.getTbEntradaVasilhameList();
            List<TbVenda> tbVendaListOld = persistentTbArmazem.getTbVendaList();
            List<TbVenda> tbVendaListNew = tbArmazem.getTbVendaList();
            List<TbStock> tbStockListOld = persistentTbArmazem.getTbStockList();
            List<TbStock> tbStockListNew = tbArmazem.getTbStockList();
            List<AccessoArmazem> accessoArmazemListOld = persistentTbArmazem.getAccessoArmazemList();
            List<AccessoArmazem> accessoArmazemListNew = tbArmazem.getAccessoArmazemList();
            List<TbSaidaVasilhame> tbSaidaVasilhameListOld = persistentTbArmazem.getTbSaidaVasilhameList();
            List<TbSaidaVasilhame> tbSaidaVasilhameListNew = tbArmazem.getTbSaidaVasilhameList();
            List<String> illegalOrphanMessages = null;
            for ( TbStockMirrow tbStockMirrowListOldTbStockMirrow : tbStockMirrowListOld )
            {
                if ( !tbStockMirrowListNew.contains( tbStockMirrowListOldTbStockMirrow ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain TbStockMirrow " + tbStockMirrowListOldTbStockMirrow + " since its codArmazem field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain NotasCompras " + notasComprasListOldNotasCompras + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain Notas " + notasListOldNotas + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbEstorno " + tbEstornoListOldTbEstorno + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbSaidasProdutos " + tbSaidasProdutosListOldTbSaidasProdutos + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbEntrada " + tbEntradaListOldTbEntrada + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbVenda " + tbVendaListOldTbVenda + " since its idArmazemFK field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbStock " + tbStockListOldTbStock + " since its codArmazem field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain AccessoArmazem " + accessoArmazemListOldAccessoArmazem + " since its fkArmazem field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain TbSaidaVasilhame " + tbSaidaVasilhameListOldTbSaidaVasilhame + " since its fkArmazem field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<TbStockMirrow> attachedTbStockMirrowListNew = new ArrayList<TbStockMirrow>();
            for ( TbStockMirrow tbStockMirrowListNewTbStockMirrowToAttach : tbStockMirrowListNew )
            {
                tbStockMirrowListNewTbStockMirrowToAttach = em.getReference( tbStockMirrowListNewTbStockMirrowToAttach.getClass(), tbStockMirrowListNewTbStockMirrowToAttach.getCodigo() );
                attachedTbStockMirrowListNew.add( tbStockMirrowListNewTbStockMirrowToAttach );
            }
            tbStockMirrowListNew = attachedTbStockMirrowListNew;
            tbArmazem.setTbStockMirrowList( tbStockMirrowListNew );
            List<NotasCompras> attachedNotasComprasListNew = new ArrayList<NotasCompras>();
            for ( NotasCompras notasComprasListNewNotasComprasToAttach : notasComprasListNew )
            {
                notasComprasListNewNotasComprasToAttach = em.getReference( notasComprasListNewNotasComprasToAttach.getClass(), notasComprasListNewNotasComprasToAttach.getPkNotaCompras() );
                attachedNotasComprasListNew.add( notasComprasListNewNotasComprasToAttach );
            }
            notasComprasListNew = attachedNotasComprasListNew;
            tbArmazem.setNotasComprasList( notasComprasListNew );
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for ( Notas notasListNewNotasToAttach : notasListNew )
            {
                notasListNewNotasToAttach = em.getReference( notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getPkNota() );
                attachedNotasListNew.add( notasListNewNotasToAttach );
            }
            notasListNew = attachedNotasListNew;
            tbArmazem.setNotasList( notasListNew );
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for ( Compras comprasListNewComprasToAttach : comprasListNew )
            {
                comprasListNewComprasToAttach = em.getReference( comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getPkCompra() );
                attachedComprasListNew.add( comprasListNewComprasToAttach );
            }
            comprasListNew = attachedComprasListNew;
            tbArmazem.setComprasList( comprasListNew );
            List<TbEstorno> attachedTbEstornoListNew = new ArrayList<TbEstorno>();
            for ( TbEstorno tbEstornoListNewTbEstornoToAttach : tbEstornoListNew )
            {
                tbEstornoListNewTbEstornoToAttach = em.getReference( tbEstornoListNewTbEstornoToAttach.getClass(), tbEstornoListNewTbEstornoToAttach.getPkEstorno() );
                attachedTbEstornoListNew.add( tbEstornoListNewTbEstornoToAttach );
            }
            tbEstornoListNew = attachedTbEstornoListNew;
            tbArmazem.setTbEstornoList( tbEstornoListNew );
            List<TbAcerto> attachedTbAcertoListNew = new ArrayList<TbAcerto>();
            for ( TbAcerto tbAcertoListNewTbAcertoToAttach : tbAcertoListNew )
            {
                tbAcertoListNewTbAcertoToAttach = em.getReference( tbAcertoListNewTbAcertoToAttach.getClass(), tbAcertoListNewTbAcertoToAttach.getIdAcerto() );
                attachedTbAcertoListNew.add( tbAcertoListNewTbAcertoToAttach );
            }
            tbAcertoListNew = attachedTbAcertoListNew;
            tbArmazem.setTbAcertoList( tbAcertoListNew );
            List<TbVasilhame> attachedTbVasilhameListNew = new ArrayList<TbVasilhame>();
            for ( TbVasilhame tbVasilhameListNewTbVasilhameToAttach : tbVasilhameListNew )
            {
                tbVasilhameListNewTbVasilhameToAttach = em.getReference( tbVasilhameListNewTbVasilhameToAttach.getClass(), tbVasilhameListNewTbVasilhameToAttach.getPkVasilhame() );
                attachedTbVasilhameListNew.add( tbVasilhameListNewTbVasilhameToAttach );
            }
            tbVasilhameListNew = attachedTbVasilhameListNew;
            tbArmazem.setTbVasilhameList( tbVasilhameListNew );
            List<TbSaidasProdutos> attachedTbSaidasProdutosListNew = new ArrayList<TbSaidasProdutos>();
            for ( TbSaidasProdutos tbSaidasProdutosListNewTbSaidasProdutosToAttach : tbSaidasProdutosListNew )
            {
                tbSaidasProdutosListNewTbSaidasProdutosToAttach = em.getReference( tbSaidasProdutosListNewTbSaidasProdutosToAttach.getClass(), tbSaidasProdutosListNewTbSaidasProdutosToAttach.getPkSaidasProdutos() );
                attachedTbSaidasProdutosListNew.add( tbSaidasProdutosListNewTbSaidasProdutosToAttach );
            }
            tbSaidasProdutosListNew = attachedTbSaidasProdutosListNew;
            tbArmazem.setTbSaidasProdutosList( tbSaidasProdutosListNew );
            List<TbEntrada> attachedTbEntradaListNew = new ArrayList<TbEntrada>();
            for ( TbEntrada tbEntradaListNewTbEntradaToAttach : tbEntradaListNew )
            {
                tbEntradaListNewTbEntradaToAttach = em.getReference( tbEntradaListNewTbEntradaToAttach.getClass(), tbEntradaListNewTbEntradaToAttach.getIdEntrada() );
                attachedTbEntradaListNew.add( tbEntradaListNewTbEntradaToAttach );
            }
            tbEntradaListNew = attachedTbEntradaListNew;
            tbArmazem.setTbEntradaList( tbEntradaListNew );
            List<TbEntradaVasilhame> attachedTbEntradaVasilhameListNew = new ArrayList<TbEntradaVasilhame>();
            for ( TbEntradaVasilhame tbEntradaVasilhameListNewTbEntradaVasilhameToAttach : tbEntradaVasilhameListNew )
            {
                tbEntradaVasilhameListNewTbEntradaVasilhameToAttach = em.getReference( tbEntradaVasilhameListNewTbEntradaVasilhameToAttach.getClass(), tbEntradaVasilhameListNewTbEntradaVasilhameToAttach.getPkEntradaVasilhame() );
                attachedTbEntradaVasilhameListNew.add( tbEntradaVasilhameListNewTbEntradaVasilhameToAttach );
            }
            tbEntradaVasilhameListNew = attachedTbEntradaVasilhameListNew;
            tbArmazem.setTbEntradaVasilhameList( tbEntradaVasilhameListNew );
            List<TbVenda> attachedTbVendaListNew = new ArrayList<TbVenda>();
            for ( TbVenda tbVendaListNewTbVendaToAttach : tbVendaListNew )
            {
                tbVendaListNewTbVendaToAttach = em.getReference( tbVendaListNewTbVendaToAttach.getClass(), tbVendaListNewTbVendaToAttach.getCodigo() );
                attachedTbVendaListNew.add( tbVendaListNewTbVendaToAttach );
            }
            tbVendaListNew = attachedTbVendaListNew;
            tbArmazem.setTbVendaList( tbVendaListNew );
            List<TbStock> attachedTbStockListNew = new ArrayList<TbStock>();
            for ( TbStock tbStockListNewTbStockToAttach : tbStockListNew )
            {
                tbStockListNewTbStockToAttach = em.getReference( tbStockListNewTbStockToAttach.getClass(), tbStockListNewTbStockToAttach.getCodigo() );
                attachedTbStockListNew.add( tbStockListNewTbStockToAttach );
            }
            tbStockListNew = attachedTbStockListNew;
            tbArmazem.setTbStockList( tbStockListNew );
            List<AccessoArmazem> attachedAccessoArmazemListNew = new ArrayList<AccessoArmazem>();
            for ( AccessoArmazem accessoArmazemListNewAccessoArmazemToAttach : accessoArmazemListNew )
            {
                accessoArmazemListNewAccessoArmazemToAttach = em.getReference( accessoArmazemListNewAccessoArmazemToAttach.getClass(), accessoArmazemListNewAccessoArmazemToAttach.getPkAccessoArmazem() );
                attachedAccessoArmazemListNew.add( accessoArmazemListNewAccessoArmazemToAttach );
            }
            accessoArmazemListNew = attachedAccessoArmazemListNew;
            tbArmazem.setAccessoArmazemList( accessoArmazemListNew );
            List<TbSaidaVasilhame> attachedTbSaidaVasilhameListNew = new ArrayList<TbSaidaVasilhame>();
            for ( TbSaidaVasilhame tbSaidaVasilhameListNewTbSaidaVasilhameToAttach : tbSaidaVasilhameListNew )
            {
                tbSaidaVasilhameListNewTbSaidaVasilhameToAttach = em.getReference( tbSaidaVasilhameListNewTbSaidaVasilhameToAttach.getClass(), tbSaidaVasilhameListNewTbSaidaVasilhameToAttach.getPkSaidaVasilhame() );
                attachedTbSaidaVasilhameListNew.add( tbSaidaVasilhameListNewTbSaidaVasilhameToAttach );
            }
            tbSaidaVasilhameListNew = attachedTbSaidaVasilhameListNew;
            tbArmazem.setTbSaidaVasilhameList( tbSaidaVasilhameListNew );
            tbArmazem = em.merge( tbArmazem );
            for ( TbStockMirrow tbStockMirrowListNewTbStockMirrow : tbStockMirrowListNew )
            {
                if ( !tbStockMirrowListOld.contains( tbStockMirrowListNewTbStockMirrow ) )
                {
                    TbArmazem oldCodArmazemOfTbStockMirrowListNewTbStockMirrow = tbStockMirrowListNewTbStockMirrow.getCodArmazem();
                    tbStockMirrowListNewTbStockMirrow.setCodArmazem( tbArmazem );
                    tbStockMirrowListNewTbStockMirrow = em.merge( tbStockMirrowListNewTbStockMirrow );
                    if ( oldCodArmazemOfTbStockMirrowListNewTbStockMirrow != null && !oldCodArmazemOfTbStockMirrowListNewTbStockMirrow.equals( tbArmazem ) )
                    {
                        oldCodArmazemOfTbStockMirrowListNewTbStockMirrow.getTbStockMirrowList().remove( tbStockMirrowListNewTbStockMirrow );
                        oldCodArmazemOfTbStockMirrowListNewTbStockMirrow = em.merge( oldCodArmazemOfTbStockMirrowListNewTbStockMirrow );
                    }
                }
            }
            for ( NotasCompras notasComprasListNewNotasCompras : notasComprasListNew )
            {
                if ( !notasComprasListOld.contains( notasComprasListNewNotasCompras ) )
                {
                    TbArmazem oldIdArmazemFKOfNotasComprasListNewNotasCompras = notasComprasListNewNotasCompras.getIdArmazemFK();
                    notasComprasListNewNotasCompras.setIdArmazemFK( tbArmazem );
                    notasComprasListNewNotasCompras = em.merge( notasComprasListNewNotasCompras );
                    if ( oldIdArmazemFKOfNotasComprasListNewNotasCompras != null && !oldIdArmazemFKOfNotasComprasListNewNotasCompras.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfNotasComprasListNewNotasCompras.getNotasComprasList().remove( notasComprasListNewNotasCompras );
                        oldIdArmazemFKOfNotasComprasListNewNotasCompras = em.merge( oldIdArmazemFKOfNotasComprasListNewNotasCompras );
                    }
                }
            }
            for ( Notas notasListNewNotas : notasListNew )
            {
                if ( !notasListOld.contains( notasListNewNotas ) )
                {
                    TbArmazem oldIdArmazemFKOfNotasListNewNotas = notasListNewNotas.getIdArmazemFK();
                    notasListNewNotas.setIdArmazemFK( tbArmazem );
                    notasListNewNotas = em.merge( notasListNewNotas );
                    if ( oldIdArmazemFKOfNotasListNewNotas != null && !oldIdArmazemFKOfNotasListNewNotas.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfNotasListNewNotas.getNotasList().remove( notasListNewNotas );
                        oldIdArmazemFKOfNotasListNewNotas = em.merge( oldIdArmazemFKOfNotasListNewNotas );
                    }
                }
            }
            for ( Compras comprasListOldCompras : comprasListOld )
            {
                if ( !comprasListNew.contains( comprasListOldCompras ) )
                {
                    comprasListOldCompras.setIdArmazemFK( null );
                    comprasListOldCompras = em.merge( comprasListOldCompras );
                }
            }
            for ( Compras comprasListNewCompras : comprasListNew )
            {
                if ( !comprasListOld.contains( comprasListNewCompras ) )
                {
                    TbArmazem oldIdArmazemFKOfComprasListNewCompras = comprasListNewCompras.getIdArmazemFK();
                    comprasListNewCompras.setIdArmazemFK( tbArmazem );
                    comprasListNewCompras = em.merge( comprasListNewCompras );
                    if ( oldIdArmazemFKOfComprasListNewCompras != null && !oldIdArmazemFKOfComprasListNewCompras.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfComprasListNewCompras.getComprasList().remove( comprasListNewCompras );
                        oldIdArmazemFKOfComprasListNewCompras = em.merge( oldIdArmazemFKOfComprasListNewCompras );
                    }
                }
            }
            for ( TbEstorno tbEstornoListNewTbEstorno : tbEstornoListNew )
            {
                if ( !tbEstornoListOld.contains( tbEstornoListNewTbEstorno ) )
                {
                    TbArmazem oldIdArmazemFKOfTbEstornoListNewTbEstorno = tbEstornoListNewTbEstorno.getIdArmazemFK();
                    tbEstornoListNewTbEstorno.setIdArmazemFK( tbArmazem );
                    tbEstornoListNewTbEstorno = em.merge( tbEstornoListNewTbEstorno );
                    if ( oldIdArmazemFKOfTbEstornoListNewTbEstorno != null && !oldIdArmazemFKOfTbEstornoListNewTbEstorno.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfTbEstornoListNewTbEstorno.getTbEstornoList().remove( tbEstornoListNewTbEstorno );
                        oldIdArmazemFKOfTbEstornoListNewTbEstorno = em.merge( oldIdArmazemFKOfTbEstornoListNewTbEstorno );
                    }
                }
            }
            for ( TbAcerto tbAcertoListOldTbAcerto : tbAcertoListOld )
            {
                if ( !tbAcertoListNew.contains( tbAcertoListOldTbAcerto ) )
                {
                    tbAcertoListOldTbAcerto.setIdArmazemFK( null );
                    tbAcertoListOldTbAcerto = em.merge( tbAcertoListOldTbAcerto );
                }
            }
            for ( TbAcerto tbAcertoListNewTbAcerto : tbAcertoListNew )
            {
                if ( !tbAcertoListOld.contains( tbAcertoListNewTbAcerto ) )
                {
                    TbArmazem oldIdArmazemFKOfTbAcertoListNewTbAcerto = tbAcertoListNewTbAcerto.getIdArmazemFK();
                    tbAcertoListNewTbAcerto.setIdArmazemFK( tbArmazem );
                    tbAcertoListNewTbAcerto = em.merge( tbAcertoListNewTbAcerto );
                    if ( oldIdArmazemFKOfTbAcertoListNewTbAcerto != null && !oldIdArmazemFKOfTbAcertoListNewTbAcerto.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfTbAcertoListNewTbAcerto.getTbAcertoList().remove( tbAcertoListNewTbAcerto );
                        oldIdArmazemFKOfTbAcertoListNewTbAcerto = em.merge( oldIdArmazemFKOfTbAcertoListNewTbAcerto );
                    }
                }
            }
            for ( TbVasilhame tbVasilhameListOldTbVasilhame : tbVasilhameListOld )
            {
                if ( !tbVasilhameListNew.contains( tbVasilhameListOldTbVasilhame ) )
                {
                    tbVasilhameListOldTbVasilhame.setFkArmazem( null );
                    tbVasilhameListOldTbVasilhame = em.merge( tbVasilhameListOldTbVasilhame );
                }
            }
            for ( TbVasilhame tbVasilhameListNewTbVasilhame : tbVasilhameListNew )
            {
                if ( !tbVasilhameListOld.contains( tbVasilhameListNewTbVasilhame ) )
                {
                    TbArmazem oldFkArmazemOfTbVasilhameListNewTbVasilhame = tbVasilhameListNewTbVasilhame.getFkArmazem();
                    tbVasilhameListNewTbVasilhame.setFkArmazem( tbArmazem );
                    tbVasilhameListNewTbVasilhame = em.merge( tbVasilhameListNewTbVasilhame );
                    if ( oldFkArmazemOfTbVasilhameListNewTbVasilhame != null && !oldFkArmazemOfTbVasilhameListNewTbVasilhame.equals( tbArmazem ) )
                    {
                        oldFkArmazemOfTbVasilhameListNewTbVasilhame.getTbVasilhameList().remove( tbVasilhameListNewTbVasilhame );
                        oldFkArmazemOfTbVasilhameListNewTbVasilhame = em.merge( oldFkArmazemOfTbVasilhameListNewTbVasilhame );
                    }
                }
            }
            for ( TbSaidasProdutos tbSaidasProdutosListNewTbSaidasProdutos : tbSaidasProdutosListNew )
            {
                if ( !tbSaidasProdutosListOld.contains( tbSaidasProdutosListNewTbSaidasProdutos ) )
                {
                    TbArmazem oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos = tbSaidasProdutosListNewTbSaidasProdutos.getIdArmazemFK();
                    tbSaidasProdutosListNewTbSaidasProdutos.setIdArmazemFK( tbArmazem );
                    tbSaidasProdutosListNewTbSaidasProdutos = em.merge( tbSaidasProdutosListNewTbSaidasProdutos );
                    if ( oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos != null && !oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos.getTbSaidasProdutosList().remove( tbSaidasProdutosListNewTbSaidasProdutos );
                        oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos = em.merge( oldIdArmazemFKOfTbSaidasProdutosListNewTbSaidasProdutos );
                    }
                }
            }
            for ( TbEntrada tbEntradaListNewTbEntrada : tbEntradaListNew )
            {
                if ( !tbEntradaListOld.contains( tbEntradaListNewTbEntrada ) )
                {
                    TbArmazem oldIdArmazemFKOfTbEntradaListNewTbEntrada = tbEntradaListNewTbEntrada.getIdArmazemFK();
                    tbEntradaListNewTbEntrada.setIdArmazemFK( tbArmazem );
                    tbEntradaListNewTbEntrada = em.merge( tbEntradaListNewTbEntrada );
                    if ( oldIdArmazemFKOfTbEntradaListNewTbEntrada != null && !oldIdArmazemFKOfTbEntradaListNewTbEntrada.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfTbEntradaListNewTbEntrada.getTbEntradaList().remove( tbEntradaListNewTbEntrada );
                        oldIdArmazemFKOfTbEntradaListNewTbEntrada = em.merge( oldIdArmazemFKOfTbEntradaListNewTbEntrada );
                    }
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListOldTbEntradaVasilhame : tbEntradaVasilhameListOld )
            {
                if ( !tbEntradaVasilhameListNew.contains( tbEntradaVasilhameListOldTbEntradaVasilhame ) )
                {
                    tbEntradaVasilhameListOldTbEntradaVasilhame.setFkAmazem( null );
                    tbEntradaVasilhameListOldTbEntradaVasilhame = em.merge( tbEntradaVasilhameListOldTbEntradaVasilhame );
                }
            }
            for ( TbEntradaVasilhame tbEntradaVasilhameListNewTbEntradaVasilhame : tbEntradaVasilhameListNew )
            {
                if ( !tbEntradaVasilhameListOld.contains( tbEntradaVasilhameListNewTbEntradaVasilhame ) )
                {
                    TbArmazem oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame = tbEntradaVasilhameListNewTbEntradaVasilhame.getFkAmazem();
                    tbEntradaVasilhameListNewTbEntradaVasilhame.setFkAmazem( tbArmazem );
                    tbEntradaVasilhameListNewTbEntradaVasilhame = em.merge( tbEntradaVasilhameListNewTbEntradaVasilhame );
                    if ( oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame != null && !oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame.equals( tbArmazem ) )
                    {
                        oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame.getTbEntradaVasilhameList().remove( tbEntradaVasilhameListNewTbEntradaVasilhame );
                        oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame = em.merge( oldFkAmazemOfTbEntradaVasilhameListNewTbEntradaVasilhame );
                    }
                }
            }
            for ( TbVenda tbVendaListNewTbVenda : tbVendaListNew )
            {
                if ( !tbVendaListOld.contains( tbVendaListNewTbVenda ) )
                {
                    TbArmazem oldIdArmazemFKOfTbVendaListNewTbVenda = tbVendaListNewTbVenda.getIdArmazemFK();
                    tbVendaListNewTbVenda.setIdArmazemFK( tbArmazem );
                    tbVendaListNewTbVenda = em.merge( tbVendaListNewTbVenda );
                    if ( oldIdArmazemFKOfTbVendaListNewTbVenda != null && !oldIdArmazemFKOfTbVendaListNewTbVenda.equals( tbArmazem ) )
                    {
                        oldIdArmazemFKOfTbVendaListNewTbVenda.getTbVendaList().remove( tbVendaListNewTbVenda );
                        oldIdArmazemFKOfTbVendaListNewTbVenda = em.merge( oldIdArmazemFKOfTbVendaListNewTbVenda );
                    }
                }
            }
            for ( TbStock tbStockListNewTbStock : tbStockListNew )
            {
                if ( !tbStockListOld.contains( tbStockListNewTbStock ) )
                {
                    TbArmazem oldCodArmazemOfTbStockListNewTbStock = tbStockListNewTbStock.getCodArmazem();
                    tbStockListNewTbStock.setCodArmazem( tbArmazem );
                    tbStockListNewTbStock = em.merge( tbStockListNewTbStock );
                    if ( oldCodArmazemOfTbStockListNewTbStock != null && !oldCodArmazemOfTbStockListNewTbStock.equals( tbArmazem ) )
                    {
                        oldCodArmazemOfTbStockListNewTbStock.getTbStockList().remove( tbStockListNewTbStock );
                        oldCodArmazemOfTbStockListNewTbStock = em.merge( oldCodArmazemOfTbStockListNewTbStock );
                    }
                }
            }
            for ( AccessoArmazem accessoArmazemListNewAccessoArmazem : accessoArmazemListNew )
            {
                if ( !accessoArmazemListOld.contains( accessoArmazemListNewAccessoArmazem ) )
                {
                    TbArmazem oldFkArmazemOfAccessoArmazemListNewAccessoArmazem = accessoArmazemListNewAccessoArmazem.getFkArmazem();
                    accessoArmazemListNewAccessoArmazem.setFkArmazem( tbArmazem );
                    accessoArmazemListNewAccessoArmazem = em.merge( accessoArmazemListNewAccessoArmazem );
                    if ( oldFkArmazemOfAccessoArmazemListNewAccessoArmazem != null && !oldFkArmazemOfAccessoArmazemListNewAccessoArmazem.equals( tbArmazem ) )
                    {
                        oldFkArmazemOfAccessoArmazemListNewAccessoArmazem.getAccessoArmazemList().remove( accessoArmazemListNewAccessoArmazem );
                        oldFkArmazemOfAccessoArmazemListNewAccessoArmazem = em.merge( oldFkArmazemOfAccessoArmazemListNewAccessoArmazem );
                    }
                }
            }
            for ( TbSaidaVasilhame tbSaidaVasilhameListNewTbSaidaVasilhame : tbSaidaVasilhameListNew )
            {
                if ( !tbSaidaVasilhameListOld.contains( tbSaidaVasilhameListNewTbSaidaVasilhame ) )
                {
                    TbArmazem oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame = tbSaidaVasilhameListNewTbSaidaVasilhame.getFkArmazem();
                    tbSaidaVasilhameListNewTbSaidaVasilhame.setFkArmazem( tbArmazem );
                    tbSaidaVasilhameListNewTbSaidaVasilhame = em.merge( tbSaidaVasilhameListNewTbSaidaVasilhame );
                    if ( oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame != null && !oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame.equals( tbArmazem ) )
                    {
                        oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame.getTbSaidaVasilhameList().remove( tbSaidaVasilhameListNewTbSaidaVasilhame );
                        oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame = em.merge( oldFkArmazemOfTbSaidaVasilhameListNewTbSaidaVasilhame );
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
                Integer id = tbArmazem.getCodigo();
                if ( findTbArmazem( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbArmazem with id " + id + " no longer exists." );
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
            TbArmazem tbArmazem;
            try
            {
                tbArmazem = em.getReference( TbArmazem.class, id );
                tbArmazem.getCodigo();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbArmazem with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<TbStockMirrow> tbStockMirrowListOrphanCheck = tbArmazem.getTbStockMirrowList();
            for ( TbStockMirrow tbStockMirrowListOrphanCheckTbStockMirrow : tbStockMirrowListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbStockMirrow " + tbStockMirrowListOrphanCheckTbStockMirrow + " in its tbStockMirrowList field has a non-nullable codArmazem field." );
            }
            List<NotasCompras> notasComprasListOrphanCheck = tbArmazem.getNotasComprasList();
            for ( NotasCompras notasComprasListOrphanCheckNotasCompras : notasComprasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the NotasCompras " + notasComprasListOrphanCheckNotasCompras + " in its notasComprasList field has a non-nullable idArmazemFK field." );
            }
            List<Notas> notasListOrphanCheck = tbArmazem.getNotasList();
            for ( Notas notasListOrphanCheckNotas : notasListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable idArmazemFK field." );
            }
            List<TbEstorno> tbEstornoListOrphanCheck = tbArmazem.getTbEstornoList();
            for ( TbEstorno tbEstornoListOrphanCheckTbEstorno : tbEstornoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbEstorno " + tbEstornoListOrphanCheckTbEstorno + " in its tbEstornoList field has a non-nullable idArmazemFK field." );
            }
            List<TbSaidasProdutos> tbSaidasProdutosListOrphanCheck = tbArmazem.getTbSaidasProdutosList();
            for ( TbSaidasProdutos tbSaidasProdutosListOrphanCheckTbSaidasProdutos : tbSaidasProdutosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbSaidasProdutos " + tbSaidasProdutosListOrphanCheckTbSaidasProdutos + " in its tbSaidasProdutosList field has a non-nullable idArmazemFK field." );
            }
            List<TbEntrada> tbEntradaListOrphanCheck = tbArmazem.getTbEntradaList();
            for ( TbEntrada tbEntradaListOrphanCheckTbEntrada : tbEntradaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbEntrada " + tbEntradaListOrphanCheckTbEntrada + " in its tbEntradaList field has a non-nullable idArmazemFK field." );
            }
            List<TbVenda> tbVendaListOrphanCheck = tbArmazem.getTbVendaList();
            for ( TbVenda tbVendaListOrphanCheckTbVenda : tbVendaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbVenda " + tbVendaListOrphanCheckTbVenda + " in its tbVendaList field has a non-nullable idArmazemFK field." );
            }
            List<TbStock> tbStockListOrphanCheck = tbArmazem.getTbStockList();
            for ( TbStock tbStockListOrphanCheckTbStock : tbStockListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbStock " + tbStockListOrphanCheckTbStock + " in its tbStockList field has a non-nullable codArmazem field." );
            }
            List<AccessoArmazem> accessoArmazemListOrphanCheck = tbArmazem.getAccessoArmazemList();
            for ( AccessoArmazem accessoArmazemListOrphanCheckAccessoArmazem : accessoArmazemListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the AccessoArmazem " + accessoArmazemListOrphanCheckAccessoArmazem + " in its accessoArmazemList field has a non-nullable fkArmazem field." );
            }
            List<TbSaidaVasilhame> tbSaidaVasilhameListOrphanCheck = tbArmazem.getTbSaidaVasilhameList();
            for ( TbSaidaVasilhame tbSaidaVasilhameListOrphanCheckTbSaidaVasilhame : tbSaidaVasilhameListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbArmazem (" + tbArmazem + ") cannot be destroyed since the TbSaidaVasilhame " + tbSaidaVasilhameListOrphanCheckTbSaidaVasilhame + " in its tbSaidaVasilhameList field has a non-nullable fkArmazem field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            List<Compras> comprasList = tbArmazem.getComprasList();
            for ( Compras comprasListCompras : comprasList )
            {
                comprasListCompras.setIdArmazemFK( null );
                comprasListCompras = em.merge( comprasListCompras );
            }
            List<TbAcerto> tbAcertoList = tbArmazem.getTbAcertoList();
            for ( TbAcerto tbAcertoListTbAcerto : tbAcertoList )
            {
                tbAcertoListTbAcerto.setIdArmazemFK( null );
                tbAcertoListTbAcerto = em.merge( tbAcertoListTbAcerto );
            }
            List<TbVasilhame> tbVasilhameList = tbArmazem.getTbVasilhameList();
            for ( TbVasilhame tbVasilhameListTbVasilhame : tbVasilhameList )
            {
                tbVasilhameListTbVasilhame.setFkArmazem( null );
                tbVasilhameListTbVasilhame = em.merge( tbVasilhameListTbVasilhame );
            }
            List<TbEntradaVasilhame> tbEntradaVasilhameList = tbArmazem.getTbEntradaVasilhameList();
            for ( TbEntradaVasilhame tbEntradaVasilhameListTbEntradaVasilhame : tbEntradaVasilhameList )
            {
                tbEntradaVasilhameListTbEntradaVasilhame.setFkAmazem( null );
                tbEntradaVasilhameListTbEntradaVasilhame = em.merge( tbEntradaVasilhameListTbEntradaVasilhame );
            }
            em.remove( tbArmazem );
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

    public List<TbArmazem> findTbArmazemEntities()
    {
        return findTbArmazemEntities( true, -1, -1 );
    }

    public List<TbArmazem> findTbArmazemEntities( int maxResults, int firstResult )
    {
        return findTbArmazemEntities( false, maxResults, firstResult );
    }

    private List<TbArmazem> findTbArmazemEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbArmazem.class ) );
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

    public TbArmazem findTbArmazem( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbArmazem.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbArmazemCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbArmazem> rt = cq.from( TbArmazem.class );
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
