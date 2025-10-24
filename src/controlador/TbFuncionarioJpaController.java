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
import entity.TbDepartamento;
import entity.TbEspecialidade;
import entity.TbEstadoCivil;
import entity.TbFuncao;
import entity.TbGrauAcademico;
import entity.TbStatus;
import entity.TbUsuario;
import entity.Empresa;
import entity.Modalidade;
import entity.ItemSalarioExtra;
import java.util.ArrayList;
import java.util.List;
import entity.PedidoFeria;
import entity.ReciboRh;
import entity.PagamentoSubsidioFeriaNatal;
import entity.TbFalta;
import entity.TbSalario;
import entity.Anexos;
import entity.FechoContrato;
import entity.TbItemSubsidiosFuncionario;
import entity.TbAdiantamento;
import entity.AgregadoFamiliar;
import entity.TbConta;
import entity.TbTempo;
import entity.PrevioAviso;
import entity.TbFuncionario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author lenovo
 */
public class TbFuncionarioJpaController implements Serializable
{

    public TbFuncionarioJpaController( EntityManagerFactory emf )
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create( TbFuncionario tbFuncionario )
    {
        if ( tbFuncionario.getItemSalarioExtraList() == null )
        {
            tbFuncionario.setItemSalarioExtraList( new ArrayList<ItemSalarioExtra>() );
        }
        if ( tbFuncionario.getPedidoFeriaList() == null )
        {
            tbFuncionario.setPedidoFeriaList( new ArrayList<PedidoFeria>() );
        }
        if ( tbFuncionario.getReciboRhList() == null )
        {
            tbFuncionario.setReciboRhList( new ArrayList<ReciboRh>() );
        }
        if ( tbFuncionario.getPagamentoSubsidioFeriaNatalList() == null )
        {
            tbFuncionario.setPagamentoSubsidioFeriaNatalList( new ArrayList<PagamentoSubsidioFeriaNatal>() );
        }
        if ( tbFuncionario.getTbFaltaList() == null )
        {
            tbFuncionario.setTbFaltaList( new ArrayList<TbFalta>() );
        }
        if ( tbFuncionario.getTbSalarioList() == null )
        {
            tbFuncionario.setTbSalarioList( new ArrayList<TbSalario>() );
        }
        if ( tbFuncionario.getAnexosList() == null )
        {
            tbFuncionario.setAnexosList( new ArrayList<Anexos>() );
        }
        if ( tbFuncionario.getFechoContratoList() == null )
        {
            tbFuncionario.setFechoContratoList( new ArrayList<FechoContrato>() );
        }
        if ( tbFuncionario.getTbItemSubsidiosFuncionarioList() == null )
        {
            tbFuncionario.setTbItemSubsidiosFuncionarioList( new ArrayList<TbItemSubsidiosFuncionario>() );
        }
        if ( tbFuncionario.getTbAdiantamentoList() == null )
        {
            tbFuncionario.setTbAdiantamentoList( new ArrayList<TbAdiantamento>() );
        }
        if ( tbFuncionario.getAgregadoFamiliarList() == null )
        {
            tbFuncionario.setAgregadoFamiliarList( new ArrayList<AgregadoFamiliar>() );
        }
        if ( tbFuncionario.getTbContaList() == null )
        {
            tbFuncionario.setTbContaList( new ArrayList<TbConta>() );
        }
        if ( tbFuncionario.getTbTempoList() == null )
        {
            tbFuncionario.setTbTempoList( new ArrayList<TbTempo>() );
        }
        if ( tbFuncionario.getPrevioAvisoList() == null )
        {
            tbFuncionario.setPrevioAvisoList( new ArrayList<PrevioAviso>() );
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbDepartamento fkDepartamento = tbFuncionario.getFkDepartamento();
            if ( fkDepartamento != null )
            {
                fkDepartamento = em.getReference( fkDepartamento.getClass(), fkDepartamento.getPkDepartamento() );
                tbFuncionario.setFkDepartamento( fkDepartamento );
            }
            TbEspecialidade fkEspecialidade = tbFuncionario.getFkEspecialidade();
            if ( fkEspecialidade != null )
            {
                fkEspecialidade = em.getReference( fkEspecialidade.getClass(), fkEspecialidade.getPkEspecialidade() );
                tbFuncionario.setFkEspecialidade( fkEspecialidade );
            }
            TbEstadoCivil fkEstadoCivil = tbFuncionario.getFkEstadoCivil();
            if ( fkEstadoCivil != null )
            {
                fkEstadoCivil = em.getReference( fkEstadoCivil.getClass(), fkEstadoCivil.getPkEstadoCivil() );
                tbFuncionario.setFkEstadoCivil( fkEstadoCivil );
            }
            TbFuncao fkFuncao = tbFuncionario.getFkFuncao();
            if ( fkFuncao != null )
            {
                fkFuncao = em.getReference( fkFuncao.getClass(), fkFuncao.getPkFuncao() );
                tbFuncionario.setFkFuncao( fkFuncao );
            }
            TbGrauAcademico fkGrauAcademico = tbFuncionario.getFkGrauAcademico();
            if ( fkGrauAcademico != null )
            {
                fkGrauAcademico = em.getReference( fkGrauAcademico.getClass(), fkGrauAcademico.getPkGrauAcademico() );
                tbFuncionario.setFkGrauAcademico( fkGrauAcademico );
            }
            TbStatus idStatusFK = tbFuncionario.getIdStatusFK();
            if ( idStatusFK != null )
            {
                idStatusFK = em.getReference( idStatusFK.getClass(), idStatusFK.getIdStatus() );
                tbFuncionario.setIdStatusFK( idStatusFK );
            }
            TbUsuario fkUsuario = tbFuncionario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario = em.getReference( fkUsuario.getClass(), fkUsuario.getCodigo() );
                tbFuncionario.setFkUsuario( fkUsuario );
            }
            Empresa fkEmpresa = tbFuncionario.getFkEmpresa();
            if ( fkEmpresa != null )
            {
                fkEmpresa = em.getReference( fkEmpresa.getClass(), fkEmpresa.getPkEmpresa() );
                tbFuncionario.setFkEmpresa( fkEmpresa );
            }
            Modalidade fkModalidade = tbFuncionario.getFkModalidade();
            if ( fkModalidade != null )
            {
                fkModalidade = em.getReference( fkModalidade.getClass(), fkModalidade.getPkModalidade() );
                tbFuncionario.setFkModalidade( fkModalidade );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraList = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtraToAttach : tbFuncionario.getItemSalarioExtraList() )
            {
                itemSalarioExtraListItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListItemSalarioExtraToAttach.getClass(), itemSalarioExtraListItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraList.add( itemSalarioExtraListItemSalarioExtraToAttach );
            }
            tbFuncionario.setItemSalarioExtraList( attachedItemSalarioExtraList );
            List<PedidoFeria> attachedPedidoFeriaList = new ArrayList<PedidoFeria>();
            for ( PedidoFeria pedidoFeriaListPedidoFeriaToAttach : tbFuncionario.getPedidoFeriaList() )
            {
                pedidoFeriaListPedidoFeriaToAttach = em.getReference( pedidoFeriaListPedidoFeriaToAttach.getClass(), pedidoFeriaListPedidoFeriaToAttach.getPkPedidoFeria() );
                attachedPedidoFeriaList.add( pedidoFeriaListPedidoFeriaToAttach );
            }
            tbFuncionario.setPedidoFeriaList( attachedPedidoFeriaList );
            List<ReciboRh> attachedReciboRhList = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListReciboRhToAttach : tbFuncionario.getReciboRhList() )
            {
                reciboRhListReciboRhToAttach = em.getReference( reciboRhListReciboRhToAttach.getClass(), reciboRhListReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhList.add( reciboRhListReciboRhToAttach );
            }
            tbFuncionario.setReciboRhList( attachedReciboRhList );
            List<PagamentoSubsidioFeriaNatal> attachedPagamentoSubsidioFeriaNatalList = new ArrayList<PagamentoSubsidioFeriaNatal>();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach : tbFuncionario.getPagamentoSubsidioFeriaNatalList() )
            {
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach = em.getReference( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach.getClass(), pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach.getPkPagamentoSubsidioFeriaNatal() );
                attachedPagamentoSubsidioFeriaNatalList.add( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatalToAttach );
            }
            tbFuncionario.setPagamentoSubsidioFeriaNatalList( attachedPagamentoSubsidioFeriaNatalList );
            List<TbFalta> attachedTbFaltaList = new ArrayList<TbFalta>();
            for ( TbFalta tbFaltaListTbFaltaToAttach : tbFuncionario.getTbFaltaList() )
            {
                tbFaltaListTbFaltaToAttach = em.getReference( tbFaltaListTbFaltaToAttach.getClass(), tbFaltaListTbFaltaToAttach.getIdFaltaPK() );
                attachedTbFaltaList.add( tbFaltaListTbFaltaToAttach );
            }
            tbFuncionario.setTbFaltaList( attachedTbFaltaList );
            List<TbSalario> attachedTbSalarioList = new ArrayList<TbSalario>();
            for ( TbSalario tbSalarioListTbSalarioToAttach : tbFuncionario.getTbSalarioList() )
            {
                tbSalarioListTbSalarioToAttach = em.getReference( tbSalarioListTbSalarioToAttach.getClass(), tbSalarioListTbSalarioToAttach.getIdSalarioFK() );
                attachedTbSalarioList.add( tbSalarioListTbSalarioToAttach );
            }
            tbFuncionario.setTbSalarioList( attachedTbSalarioList );
            List<Anexos> attachedAnexosList = new ArrayList<Anexos>();
            for ( Anexos anexosListAnexosToAttach : tbFuncionario.getAnexosList() )
            {
                anexosListAnexosToAttach = em.getReference( anexosListAnexosToAttach.getClass(), anexosListAnexosToAttach.getPkAnexos() );
                attachedAnexosList.add( anexosListAnexosToAttach );
            }
            tbFuncionario.setAnexosList( attachedAnexosList );
            List<FechoContrato> attachedFechoContratoList = new ArrayList<FechoContrato>();
            for ( FechoContrato fechoContratoListFechoContratoToAttach : tbFuncionario.getFechoContratoList() )
            {
                fechoContratoListFechoContratoToAttach = em.getReference( fechoContratoListFechoContratoToAttach.getClass(), fechoContratoListFechoContratoToAttach.getPkFechoContrato() );
                attachedFechoContratoList.add( fechoContratoListFechoContratoToAttach );
            }
            tbFuncionario.setFechoContratoList( attachedFechoContratoList );
            List<TbItemSubsidiosFuncionario> attachedTbItemSubsidiosFuncionarioList = new ArrayList<TbItemSubsidiosFuncionario>();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach : tbFuncionario.getTbItemSubsidiosFuncionarioList() )
            {
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach = em.getReference( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach.getClass(), tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach.getIdItemSubsidiosFuncionario() );
                attachedTbItemSubsidiosFuncionarioList.add( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionarioToAttach );
            }
            tbFuncionario.setTbItemSubsidiosFuncionarioList( attachedTbItemSubsidiosFuncionarioList );
            List<TbAdiantamento> attachedTbAdiantamentoList = new ArrayList<TbAdiantamento>();
            for ( TbAdiantamento tbAdiantamentoListTbAdiantamentoToAttach : tbFuncionario.getTbAdiantamentoList() )
            {
                tbAdiantamentoListTbAdiantamentoToAttach = em.getReference( tbAdiantamentoListTbAdiantamentoToAttach.getClass(), tbAdiantamentoListTbAdiantamentoToAttach.getIdAdiantamentoFK() );
                attachedTbAdiantamentoList.add( tbAdiantamentoListTbAdiantamentoToAttach );
            }
            tbFuncionario.setTbAdiantamentoList( attachedTbAdiantamentoList );
            List<AgregadoFamiliar> attachedAgregadoFamiliarList = new ArrayList<AgregadoFamiliar>();
            for ( AgregadoFamiliar agregadoFamiliarListAgregadoFamiliarToAttach : tbFuncionario.getAgregadoFamiliarList() )
            {
                agregadoFamiliarListAgregadoFamiliarToAttach = em.getReference( agregadoFamiliarListAgregadoFamiliarToAttach.getClass(), agregadoFamiliarListAgregadoFamiliarToAttach.getPkAgregadoFamiliar() );
                attachedAgregadoFamiliarList.add( agregadoFamiliarListAgregadoFamiliarToAttach );
            }
            tbFuncionario.setAgregadoFamiliarList( attachedAgregadoFamiliarList );
            List<TbConta> attachedTbContaList = new ArrayList<TbConta>();
            for ( TbConta tbContaListTbContaToAttach : tbFuncionario.getTbContaList() )
            {
                tbContaListTbContaToAttach = em.getReference( tbContaListTbContaToAttach.getClass(), tbContaListTbContaToAttach.getIdContaPK() );
                attachedTbContaList.add( tbContaListTbContaToAttach );
            }
            tbFuncionario.setTbContaList( attachedTbContaList );
            List<TbTempo> attachedTbTempoList = new ArrayList<TbTempo>();
            for ( TbTempo tbTempoListTbTempoToAttach : tbFuncionario.getTbTempoList() )
            {
                tbTempoListTbTempoToAttach = em.getReference( tbTempoListTbTempoToAttach.getClass(), tbTempoListTbTempoToAttach.getIdTempoPK() );
                attachedTbTempoList.add( tbTempoListTbTempoToAttach );
            }
            tbFuncionario.setTbTempoList( attachedTbTempoList );
            List<PrevioAviso> attachedPrevioAvisoList = new ArrayList<PrevioAviso>();
            for ( PrevioAviso previoAvisoListPrevioAvisoToAttach : tbFuncionario.getPrevioAvisoList() )
            {
                previoAvisoListPrevioAvisoToAttach = em.getReference( previoAvisoListPrevioAvisoToAttach.getClass(), previoAvisoListPrevioAvisoToAttach.getPkPrevioAviso() );
                attachedPrevioAvisoList.add( previoAvisoListPrevioAvisoToAttach );
            }
            tbFuncionario.setPrevioAvisoList( attachedPrevioAvisoList );
            em.persist( tbFuncionario );
            if ( fkDepartamento != null )
            {
                fkDepartamento.getTbFuncionarioList().add( tbFuncionario );
                fkDepartamento = em.merge( fkDepartamento );
            }
            if ( fkEspecialidade != null )
            {
                fkEspecialidade.getTbFuncionarioList().add( tbFuncionario );
                fkEspecialidade = em.merge( fkEspecialidade );
            }
            if ( fkEstadoCivil != null )
            {
                fkEstadoCivil.getTbFuncionarioList().add( tbFuncionario );
                fkEstadoCivil = em.merge( fkEstadoCivil );
            }
            if ( fkFuncao != null )
            {
                fkFuncao.getTbFuncionarioList().add( tbFuncionario );
                fkFuncao = em.merge( fkFuncao );
            }
            if ( fkGrauAcademico != null )
            {
                fkGrauAcademico.getTbFuncionarioList().add( tbFuncionario );
                fkGrauAcademico = em.merge( fkGrauAcademico );
            }
            if ( idStatusFK != null )
            {
                idStatusFK.getTbFuncionarioList().add( tbFuncionario );
                idStatusFK = em.merge( idStatusFK );
            }
            if ( fkUsuario != null )
            {
                fkUsuario.getTbFuncionarioList().add( tbFuncionario );
                fkUsuario = em.merge( fkUsuario );
            }
            if ( fkEmpresa != null )
            {
                fkEmpresa.getTbFuncionarioList().add( tbFuncionario );
                fkEmpresa = em.merge( fkEmpresa );
            }
            if ( fkModalidade != null )
            {
                fkModalidade.getTbFuncionarioList().add( tbFuncionario );
                fkModalidade = em.merge( fkModalidade );
            }
            for ( ItemSalarioExtra itemSalarioExtraListItemSalarioExtra : tbFuncionario.getItemSalarioExtraList() )
            {
                TbFuncionario oldFkTbFuncionarioOfItemSalarioExtraListItemSalarioExtra = itemSalarioExtraListItemSalarioExtra.getFkTbFuncionario();
                itemSalarioExtraListItemSalarioExtra.setFkTbFuncionario( tbFuncionario );
                itemSalarioExtraListItemSalarioExtra = em.merge( itemSalarioExtraListItemSalarioExtra );
                if ( oldFkTbFuncionarioOfItemSalarioExtraListItemSalarioExtra != null )
                {
                    oldFkTbFuncionarioOfItemSalarioExtraListItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListItemSalarioExtra );
                    oldFkTbFuncionarioOfItemSalarioExtraListItemSalarioExtra = em.merge( oldFkTbFuncionarioOfItemSalarioExtraListItemSalarioExtra );
                }
            }
            for ( PedidoFeria pedidoFeriaListPedidoFeria : tbFuncionario.getPedidoFeriaList() )
            {
                TbFuncionario oldFkFuncionarioOfPedidoFeriaListPedidoFeria = pedidoFeriaListPedidoFeria.getFkFuncionario();
                pedidoFeriaListPedidoFeria.setFkFuncionario( tbFuncionario );
                pedidoFeriaListPedidoFeria = em.merge( pedidoFeriaListPedidoFeria );
                if ( oldFkFuncionarioOfPedidoFeriaListPedidoFeria != null )
                {
                    oldFkFuncionarioOfPedidoFeriaListPedidoFeria.getPedidoFeriaList().remove( pedidoFeriaListPedidoFeria );
                    oldFkFuncionarioOfPedidoFeriaListPedidoFeria = em.merge( oldFkFuncionarioOfPedidoFeriaListPedidoFeria );
                }
            }
            for ( ReciboRh reciboRhListReciboRh : tbFuncionario.getReciboRhList() )
            {
                TbFuncionario oldFkFuncionarioOfReciboRhListReciboRh = reciboRhListReciboRh.getFkFuncionario();
                reciboRhListReciboRh.setFkFuncionario( tbFuncionario );
                reciboRhListReciboRh = em.merge( reciboRhListReciboRh );
                if ( oldFkFuncionarioOfReciboRhListReciboRh != null )
                {
                    oldFkFuncionarioOfReciboRhListReciboRh.getReciboRhList().remove( reciboRhListReciboRh );
                    oldFkFuncionarioOfReciboRhListReciboRh = em.merge( oldFkFuncionarioOfReciboRhListReciboRh );
                }
            }
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal : tbFuncionario.getPagamentoSubsidioFeriaNatalList() )
            {
                TbFuncionario oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.getFkFuncionario();
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.setFkFuncionario( tbFuncionario );
                pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = em.merge( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                if ( oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal != null )
                {
                    oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                    oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal = em.merge( oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListPagamentoSubsidioFeriaNatal );
                }
            }
            for ( TbFalta tbFaltaListTbFalta : tbFuncionario.getTbFaltaList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbFaltaListTbFalta = tbFaltaListTbFalta.getIdFuncionarioFK();
                tbFaltaListTbFalta.setIdFuncionarioFK( tbFuncionario );
                tbFaltaListTbFalta = em.merge( tbFaltaListTbFalta );
                if ( oldIdFuncionarioFKOfTbFaltaListTbFalta != null )
                {
                    oldIdFuncionarioFKOfTbFaltaListTbFalta.getTbFaltaList().remove( tbFaltaListTbFalta );
                    oldIdFuncionarioFKOfTbFaltaListTbFalta = em.merge( oldIdFuncionarioFKOfTbFaltaListTbFalta );
                }
            }
            for ( TbSalario tbSalarioListTbSalario : tbFuncionario.getTbSalarioList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbSalarioListTbSalario = tbSalarioListTbSalario.getIdFuncionarioFK();
                tbSalarioListTbSalario.setIdFuncionarioFK( tbFuncionario );
                tbSalarioListTbSalario = em.merge( tbSalarioListTbSalario );
                if ( oldIdFuncionarioFKOfTbSalarioListTbSalario != null )
                {
                    oldIdFuncionarioFKOfTbSalarioListTbSalario.getTbSalarioList().remove( tbSalarioListTbSalario );
                    oldIdFuncionarioFKOfTbSalarioListTbSalario = em.merge( oldIdFuncionarioFKOfTbSalarioListTbSalario );
                }
            }
            for ( Anexos anexosListAnexos : tbFuncionario.getAnexosList() )
            {
                TbFuncionario oldFkFuncionarioOfAnexosListAnexos = anexosListAnexos.getFkFuncionario();
                anexosListAnexos.setFkFuncionario( tbFuncionario );
                anexosListAnexos = em.merge( anexosListAnexos );
                if ( oldFkFuncionarioOfAnexosListAnexos != null )
                {
                    oldFkFuncionarioOfAnexosListAnexos.getAnexosList().remove( anexosListAnexos );
                    oldFkFuncionarioOfAnexosListAnexos = em.merge( oldFkFuncionarioOfAnexosListAnexos );
                }
            }
            for ( FechoContrato fechoContratoListFechoContrato : tbFuncionario.getFechoContratoList() )
            {
                TbFuncionario oldFkFuncionarioOfFechoContratoListFechoContrato = fechoContratoListFechoContrato.getFkFuncionario();
                fechoContratoListFechoContrato.setFkFuncionario( tbFuncionario );
                fechoContratoListFechoContrato = em.merge( fechoContratoListFechoContrato );
                if ( oldFkFuncionarioOfFechoContratoListFechoContrato != null )
                {
                    oldFkFuncionarioOfFechoContratoListFechoContrato.getFechoContratoList().remove( fechoContratoListFechoContrato );
                    oldFkFuncionarioOfFechoContratoListFechoContrato = em.merge( oldFkFuncionarioOfFechoContratoListFechoContrato );
                }
            }
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario : tbFuncionario.getTbItemSubsidiosFuncionarioList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.getIdFuncionarioFK();
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.setIdFuncionarioFK( tbFuncionario );
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
                if ( oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario != null )
                {
                    oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
                    oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
                }
            }
            for ( TbAdiantamento tbAdiantamentoListTbAdiantamento : tbFuncionario.getTbAdiantamentoList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbAdiantamentoListTbAdiantamento = tbAdiantamentoListTbAdiantamento.getIdFuncionarioFK();
                tbAdiantamentoListTbAdiantamento.setIdFuncionarioFK( tbFuncionario );
                tbAdiantamentoListTbAdiantamento = em.merge( tbAdiantamentoListTbAdiantamento );
                if ( oldIdFuncionarioFKOfTbAdiantamentoListTbAdiantamento != null )
                {
                    oldIdFuncionarioFKOfTbAdiantamentoListTbAdiantamento.getTbAdiantamentoList().remove( tbAdiantamentoListTbAdiantamento );
                    oldIdFuncionarioFKOfTbAdiantamentoListTbAdiantamento = em.merge( oldIdFuncionarioFKOfTbAdiantamentoListTbAdiantamento );
                }
            }
            for ( AgregadoFamiliar agregadoFamiliarListAgregadoFamiliar : tbFuncionario.getAgregadoFamiliarList() )
            {
                TbFuncionario oldFkFuncionarioOfAgregadoFamiliarListAgregadoFamiliar = agregadoFamiliarListAgregadoFamiliar.getFkFuncionario();
                agregadoFamiliarListAgregadoFamiliar.setFkFuncionario( tbFuncionario );
                agregadoFamiliarListAgregadoFamiliar = em.merge( agregadoFamiliarListAgregadoFamiliar );
                if ( oldFkFuncionarioOfAgregadoFamiliarListAgregadoFamiliar != null )
                {
                    oldFkFuncionarioOfAgregadoFamiliarListAgregadoFamiliar.getAgregadoFamiliarList().remove( agregadoFamiliarListAgregadoFamiliar );
                    oldFkFuncionarioOfAgregadoFamiliarListAgregadoFamiliar = em.merge( oldFkFuncionarioOfAgregadoFamiliarListAgregadoFamiliar );
                }
            }
            for ( TbConta tbContaListTbConta : tbFuncionario.getTbContaList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbContaListTbConta = tbContaListTbConta.getIdFuncionarioFK();
                tbContaListTbConta.setIdFuncionarioFK( tbFuncionario );
                tbContaListTbConta = em.merge( tbContaListTbConta );
                if ( oldIdFuncionarioFKOfTbContaListTbConta != null )
                {
                    oldIdFuncionarioFKOfTbContaListTbConta.getTbContaList().remove( tbContaListTbConta );
                    oldIdFuncionarioFKOfTbContaListTbConta = em.merge( oldIdFuncionarioFKOfTbContaListTbConta );
                }
            }
            for ( TbTempo tbTempoListTbTempo : tbFuncionario.getTbTempoList() )
            {
                TbFuncionario oldIdFuncionarioFKOfTbTempoListTbTempo = tbTempoListTbTempo.getIdFuncionarioFK();
                tbTempoListTbTempo.setIdFuncionarioFK( tbFuncionario );
                tbTempoListTbTempo = em.merge( tbTempoListTbTempo );
                if ( oldIdFuncionarioFKOfTbTempoListTbTempo != null )
                {
                    oldIdFuncionarioFKOfTbTempoListTbTempo.getTbTempoList().remove( tbTempoListTbTempo );
                    oldIdFuncionarioFKOfTbTempoListTbTempo = em.merge( oldIdFuncionarioFKOfTbTempoListTbTempo );
                }
            }
            for ( PrevioAviso previoAvisoListPrevioAviso : tbFuncionario.getPrevioAvisoList() )
            {
                TbFuncionario oldFkFuncionarioOfPrevioAvisoListPrevioAviso = previoAvisoListPrevioAviso.getFkFuncionario();
                previoAvisoListPrevioAviso.setFkFuncionario( tbFuncionario );
                previoAvisoListPrevioAviso = em.merge( previoAvisoListPrevioAviso );
                if ( oldFkFuncionarioOfPrevioAvisoListPrevioAviso != null )
                {
                    oldFkFuncionarioOfPrevioAvisoListPrevioAviso.getPrevioAvisoList().remove( previoAvisoListPrevioAviso );
                    oldFkFuncionarioOfPrevioAvisoListPrevioAviso = em.merge( oldFkFuncionarioOfPrevioAvisoListPrevioAviso );
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

    public void edit( TbFuncionario tbFuncionario ) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            TbFuncionario persistentTbFuncionario = em.find( TbFuncionario.class, tbFuncionario.getIdFuncionario() );
            TbDepartamento fkDepartamentoOld = persistentTbFuncionario.getFkDepartamento();
            TbDepartamento fkDepartamentoNew = tbFuncionario.getFkDepartamento();
            TbEspecialidade fkEspecialidadeOld = persistentTbFuncionario.getFkEspecialidade();
            TbEspecialidade fkEspecialidadeNew = tbFuncionario.getFkEspecialidade();
            TbEstadoCivil fkEstadoCivilOld = persistentTbFuncionario.getFkEstadoCivil();
            TbEstadoCivil fkEstadoCivilNew = tbFuncionario.getFkEstadoCivil();
            TbFuncao fkFuncaoOld = persistentTbFuncionario.getFkFuncao();
            TbFuncao fkFuncaoNew = tbFuncionario.getFkFuncao();
            TbGrauAcademico fkGrauAcademicoOld = persistentTbFuncionario.getFkGrauAcademico();
            TbGrauAcademico fkGrauAcademicoNew = tbFuncionario.getFkGrauAcademico();
            TbStatus idStatusFKOld = persistentTbFuncionario.getIdStatusFK();
            TbStatus idStatusFKNew = tbFuncionario.getIdStatusFK();
            TbUsuario fkUsuarioOld = persistentTbFuncionario.getFkUsuario();
            TbUsuario fkUsuarioNew = tbFuncionario.getFkUsuario();
            Empresa fkEmpresaOld = persistentTbFuncionario.getFkEmpresa();
            Empresa fkEmpresaNew = tbFuncionario.getFkEmpresa();
            Modalidade fkModalidadeOld = persistentTbFuncionario.getFkModalidade();
            Modalidade fkModalidadeNew = tbFuncionario.getFkModalidade();
            List<ItemSalarioExtra> itemSalarioExtraListOld = persistentTbFuncionario.getItemSalarioExtraList();
            List<ItemSalarioExtra> itemSalarioExtraListNew = tbFuncionario.getItemSalarioExtraList();
            List<PedidoFeria> pedidoFeriaListOld = persistentTbFuncionario.getPedidoFeriaList();
            List<PedidoFeria> pedidoFeriaListNew = tbFuncionario.getPedidoFeriaList();
            List<ReciboRh> reciboRhListOld = persistentTbFuncionario.getReciboRhList();
            List<ReciboRh> reciboRhListNew = tbFuncionario.getReciboRhList();
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListOld = persistentTbFuncionario.getPagamentoSubsidioFeriaNatalList();
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListNew = tbFuncionario.getPagamentoSubsidioFeriaNatalList();
            List<TbFalta> tbFaltaListOld = persistentTbFuncionario.getTbFaltaList();
            List<TbFalta> tbFaltaListNew = tbFuncionario.getTbFaltaList();
            List<TbSalario> tbSalarioListOld = persistentTbFuncionario.getTbSalarioList();
            List<TbSalario> tbSalarioListNew = tbFuncionario.getTbSalarioList();
            List<Anexos> anexosListOld = persistentTbFuncionario.getAnexosList();
            List<Anexos> anexosListNew = tbFuncionario.getAnexosList();
            List<FechoContrato> fechoContratoListOld = persistentTbFuncionario.getFechoContratoList();
            List<FechoContrato> fechoContratoListNew = tbFuncionario.getFechoContratoList();
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioListOld = persistentTbFuncionario.getTbItemSubsidiosFuncionarioList();
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioListNew = tbFuncionario.getTbItemSubsidiosFuncionarioList();
            List<TbAdiantamento> tbAdiantamentoListOld = persistentTbFuncionario.getTbAdiantamentoList();
            List<TbAdiantamento> tbAdiantamentoListNew = tbFuncionario.getTbAdiantamentoList();
            List<AgregadoFamiliar> agregadoFamiliarListOld = persistentTbFuncionario.getAgregadoFamiliarList();
            List<AgregadoFamiliar> agregadoFamiliarListNew = tbFuncionario.getAgregadoFamiliarList();
            List<TbConta> tbContaListOld = persistentTbFuncionario.getTbContaList();
            List<TbConta> tbContaListNew = tbFuncionario.getTbContaList();
            List<TbTempo> tbTempoListOld = persistentTbFuncionario.getTbTempoList();
            List<TbTempo> tbTempoListNew = tbFuncionario.getTbTempoList();
            List<PrevioAviso> previoAvisoListOld = persistentTbFuncionario.getPrevioAvisoList();
            List<PrevioAviso> previoAvisoListNew = tbFuncionario.getPrevioAvisoList();
            List<String> illegalOrphanMessages = null;
            for ( ItemSalarioExtra itemSalarioExtraListOldItemSalarioExtra : itemSalarioExtraListOld )
            {
                if ( !itemSalarioExtraListNew.contains( itemSalarioExtraListOldItemSalarioExtra ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ItemSalarioExtra " + itemSalarioExtraListOldItemSalarioExtra + " since its fkTbFuncionario field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain PedidoFeria " + pedidoFeriaListOldPedidoFeria + " since its fkFuncionario field is not nullable." );
                }
            }
            for ( ReciboRh reciboRhListOldReciboRh : reciboRhListOld )
            {
                if ( !reciboRhListNew.contains( reciboRhListOldReciboRh ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain ReciboRh " + reciboRhListOldReciboRh + " since its fkFuncionario field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain PagamentoSubsidioFeriaNatal " + pagamentoSubsidioFeriaNatalListOldPagamentoSubsidioFeriaNatal + " since its fkFuncionario field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain Anexos " + anexosListOldAnexos + " since its fkFuncionario field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain FechoContrato " + fechoContratoListOldFechoContrato + " since its fkFuncionario field is not nullable." );
                }
            }
            for ( AgregadoFamiliar agregadoFamiliarListOldAgregadoFamiliar : agregadoFamiliarListOld )
            {
                if ( !agregadoFamiliarListNew.contains( agregadoFamiliarListOldAgregadoFamiliar ) )
                {
                    if ( illegalOrphanMessages == null )
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add( "You must retain AgregadoFamiliar " + agregadoFamiliarListOldAgregadoFamiliar + " since its fkFuncionario field is not nullable." );
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
                    illegalOrphanMessages.add( "You must retain PrevioAviso " + previoAvisoListOldPrevioAviso + " since its fkFuncionario field is not nullable." );
                }
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            if ( fkDepartamentoNew != null )
            {
                fkDepartamentoNew = em.getReference( fkDepartamentoNew.getClass(), fkDepartamentoNew.getPkDepartamento() );
                tbFuncionario.setFkDepartamento( fkDepartamentoNew );
            }
            if ( fkEspecialidadeNew != null )
            {
                fkEspecialidadeNew = em.getReference( fkEspecialidadeNew.getClass(), fkEspecialidadeNew.getPkEspecialidade() );
                tbFuncionario.setFkEspecialidade( fkEspecialidadeNew );
            }
            if ( fkEstadoCivilNew != null )
            {
                fkEstadoCivilNew = em.getReference( fkEstadoCivilNew.getClass(), fkEstadoCivilNew.getPkEstadoCivil() );
                tbFuncionario.setFkEstadoCivil( fkEstadoCivilNew );
            }
            if ( fkFuncaoNew != null )
            {
                fkFuncaoNew = em.getReference( fkFuncaoNew.getClass(), fkFuncaoNew.getPkFuncao() );
                tbFuncionario.setFkFuncao( fkFuncaoNew );
            }
            if ( fkGrauAcademicoNew != null )
            {
                fkGrauAcademicoNew = em.getReference( fkGrauAcademicoNew.getClass(), fkGrauAcademicoNew.getPkGrauAcademico() );
                tbFuncionario.setFkGrauAcademico( fkGrauAcademicoNew );
            }
            if ( idStatusFKNew != null )
            {
                idStatusFKNew = em.getReference( idStatusFKNew.getClass(), idStatusFKNew.getIdStatus() );
                tbFuncionario.setIdStatusFK( idStatusFKNew );
            }
            if ( fkUsuarioNew != null )
            {
                fkUsuarioNew = em.getReference( fkUsuarioNew.getClass(), fkUsuarioNew.getCodigo() );
                tbFuncionario.setFkUsuario( fkUsuarioNew );
            }
            if ( fkEmpresaNew != null )
            {
                fkEmpresaNew = em.getReference( fkEmpresaNew.getClass(), fkEmpresaNew.getPkEmpresa() );
                tbFuncionario.setFkEmpresa( fkEmpresaNew );
            }
            if ( fkModalidadeNew != null )
            {
                fkModalidadeNew = em.getReference( fkModalidadeNew.getClass(), fkModalidadeNew.getPkModalidade() );
                tbFuncionario.setFkModalidade( fkModalidadeNew );
            }
            List<ItemSalarioExtra> attachedItemSalarioExtraListNew = new ArrayList<ItemSalarioExtra>();
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtraToAttach : itemSalarioExtraListNew )
            {
                itemSalarioExtraListNewItemSalarioExtraToAttach = em.getReference( itemSalarioExtraListNewItemSalarioExtraToAttach.getClass(), itemSalarioExtraListNewItemSalarioExtraToAttach.getPkItemSalarioExtra() );
                attachedItemSalarioExtraListNew.add( itemSalarioExtraListNewItemSalarioExtraToAttach );
            }
            itemSalarioExtraListNew = attachedItemSalarioExtraListNew;
            tbFuncionario.setItemSalarioExtraList( itemSalarioExtraListNew );
            List<PedidoFeria> attachedPedidoFeriaListNew = new ArrayList<PedidoFeria>();
            for ( PedidoFeria pedidoFeriaListNewPedidoFeriaToAttach : pedidoFeriaListNew )
            {
                pedidoFeriaListNewPedidoFeriaToAttach = em.getReference( pedidoFeriaListNewPedidoFeriaToAttach.getClass(), pedidoFeriaListNewPedidoFeriaToAttach.getPkPedidoFeria() );
                attachedPedidoFeriaListNew.add( pedidoFeriaListNewPedidoFeriaToAttach );
            }
            pedidoFeriaListNew = attachedPedidoFeriaListNew;
            tbFuncionario.setPedidoFeriaList( pedidoFeriaListNew );
            List<ReciboRh> attachedReciboRhListNew = new ArrayList<ReciboRh>();
            for ( ReciboRh reciboRhListNewReciboRhToAttach : reciboRhListNew )
            {
                reciboRhListNewReciboRhToAttach = em.getReference( reciboRhListNewReciboRhToAttach.getClass(), reciboRhListNewReciboRhToAttach.getPkReciboRh() );
                attachedReciboRhListNew.add( reciboRhListNewReciboRhToAttach );
            }
            reciboRhListNew = attachedReciboRhListNew;
            tbFuncionario.setReciboRhList( reciboRhListNew );
            List<PagamentoSubsidioFeriaNatal> attachedPagamentoSubsidioFeriaNatalListNew = new ArrayList<PagamentoSubsidioFeriaNatal>();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach : pagamentoSubsidioFeriaNatalListNew )
            {
                pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach = em.getReference( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach.getClass(), pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach.getPkPagamentoSubsidioFeriaNatal() );
                attachedPagamentoSubsidioFeriaNatalListNew.add( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatalToAttach );
            }
            pagamentoSubsidioFeriaNatalListNew = attachedPagamentoSubsidioFeriaNatalListNew;
            tbFuncionario.setPagamentoSubsidioFeriaNatalList( pagamentoSubsidioFeriaNatalListNew );
            List<TbFalta> attachedTbFaltaListNew = new ArrayList<TbFalta>();
            for ( TbFalta tbFaltaListNewTbFaltaToAttach : tbFaltaListNew )
            {
                tbFaltaListNewTbFaltaToAttach = em.getReference( tbFaltaListNewTbFaltaToAttach.getClass(), tbFaltaListNewTbFaltaToAttach.getIdFaltaPK() );
                attachedTbFaltaListNew.add( tbFaltaListNewTbFaltaToAttach );
            }
            tbFaltaListNew = attachedTbFaltaListNew;
            tbFuncionario.setTbFaltaList( tbFaltaListNew );
            List<TbSalario> attachedTbSalarioListNew = new ArrayList<TbSalario>();
            for ( TbSalario tbSalarioListNewTbSalarioToAttach : tbSalarioListNew )
            {
                tbSalarioListNewTbSalarioToAttach = em.getReference( tbSalarioListNewTbSalarioToAttach.getClass(), tbSalarioListNewTbSalarioToAttach.getIdSalarioFK() );
                attachedTbSalarioListNew.add( tbSalarioListNewTbSalarioToAttach );
            }
            tbSalarioListNew = attachedTbSalarioListNew;
            tbFuncionario.setTbSalarioList( tbSalarioListNew );
            List<Anexos> attachedAnexosListNew = new ArrayList<Anexos>();
            for ( Anexos anexosListNewAnexosToAttach : anexosListNew )
            {
                anexosListNewAnexosToAttach = em.getReference( anexosListNewAnexosToAttach.getClass(), anexosListNewAnexosToAttach.getPkAnexos() );
                attachedAnexosListNew.add( anexosListNewAnexosToAttach );
            }
            anexosListNew = attachedAnexosListNew;
            tbFuncionario.setAnexosList( anexosListNew );
            List<FechoContrato> attachedFechoContratoListNew = new ArrayList<FechoContrato>();
            for ( FechoContrato fechoContratoListNewFechoContratoToAttach : fechoContratoListNew )
            {
                fechoContratoListNewFechoContratoToAttach = em.getReference( fechoContratoListNewFechoContratoToAttach.getClass(), fechoContratoListNewFechoContratoToAttach.getPkFechoContrato() );
                attachedFechoContratoListNew.add( fechoContratoListNewFechoContratoToAttach );
            }
            fechoContratoListNew = attachedFechoContratoListNew;
            tbFuncionario.setFechoContratoList( fechoContratoListNew );
            List<TbItemSubsidiosFuncionario> attachedTbItemSubsidiosFuncionarioListNew = new ArrayList<TbItemSubsidiosFuncionario>();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach : tbItemSubsidiosFuncionarioListNew )
            {
                tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach = em.getReference( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach.getClass(), tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach.getIdItemSubsidiosFuncionario() );
                attachedTbItemSubsidiosFuncionarioListNew.add( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionarioToAttach );
            }
            tbItemSubsidiosFuncionarioListNew = attachedTbItemSubsidiosFuncionarioListNew;
            tbFuncionario.setTbItemSubsidiosFuncionarioList( tbItemSubsidiosFuncionarioListNew );
            List<TbAdiantamento> attachedTbAdiantamentoListNew = new ArrayList<TbAdiantamento>();
            for ( TbAdiantamento tbAdiantamentoListNewTbAdiantamentoToAttach : tbAdiantamentoListNew )
            {
                tbAdiantamentoListNewTbAdiantamentoToAttach = em.getReference( tbAdiantamentoListNewTbAdiantamentoToAttach.getClass(), tbAdiantamentoListNewTbAdiantamentoToAttach.getIdAdiantamentoFK() );
                attachedTbAdiantamentoListNew.add( tbAdiantamentoListNewTbAdiantamentoToAttach );
            }
            tbAdiantamentoListNew = attachedTbAdiantamentoListNew;
            tbFuncionario.setTbAdiantamentoList( tbAdiantamentoListNew );
            List<AgregadoFamiliar> attachedAgregadoFamiliarListNew = new ArrayList<AgregadoFamiliar>();
            for ( AgregadoFamiliar agregadoFamiliarListNewAgregadoFamiliarToAttach : agregadoFamiliarListNew )
            {
                agregadoFamiliarListNewAgregadoFamiliarToAttach = em.getReference( agregadoFamiliarListNewAgregadoFamiliarToAttach.getClass(), agregadoFamiliarListNewAgregadoFamiliarToAttach.getPkAgregadoFamiliar() );
                attachedAgregadoFamiliarListNew.add( agregadoFamiliarListNewAgregadoFamiliarToAttach );
            }
            agregadoFamiliarListNew = attachedAgregadoFamiliarListNew;
            tbFuncionario.setAgregadoFamiliarList( agregadoFamiliarListNew );
            List<TbConta> attachedTbContaListNew = new ArrayList<TbConta>();
            for ( TbConta tbContaListNewTbContaToAttach : tbContaListNew )
            {
                tbContaListNewTbContaToAttach = em.getReference( tbContaListNewTbContaToAttach.getClass(), tbContaListNewTbContaToAttach.getIdContaPK() );
                attachedTbContaListNew.add( tbContaListNewTbContaToAttach );
            }
            tbContaListNew = attachedTbContaListNew;
            tbFuncionario.setTbContaList( tbContaListNew );
            List<TbTempo> attachedTbTempoListNew = new ArrayList<TbTempo>();
            for ( TbTempo tbTempoListNewTbTempoToAttach : tbTempoListNew )
            {
                tbTempoListNewTbTempoToAttach = em.getReference( tbTempoListNewTbTempoToAttach.getClass(), tbTempoListNewTbTempoToAttach.getIdTempoPK() );
                attachedTbTempoListNew.add( tbTempoListNewTbTempoToAttach );
            }
            tbTempoListNew = attachedTbTempoListNew;
            tbFuncionario.setTbTempoList( tbTempoListNew );
            List<PrevioAviso> attachedPrevioAvisoListNew = new ArrayList<PrevioAviso>();
            for ( PrevioAviso previoAvisoListNewPrevioAvisoToAttach : previoAvisoListNew )
            {
                previoAvisoListNewPrevioAvisoToAttach = em.getReference( previoAvisoListNewPrevioAvisoToAttach.getClass(), previoAvisoListNewPrevioAvisoToAttach.getPkPrevioAviso() );
                attachedPrevioAvisoListNew.add( previoAvisoListNewPrevioAvisoToAttach );
            }
            previoAvisoListNew = attachedPrevioAvisoListNew;
            tbFuncionario.setPrevioAvisoList( previoAvisoListNew );
            tbFuncionario = em.merge( tbFuncionario );
            if ( fkDepartamentoOld != null && !fkDepartamentoOld.equals( fkDepartamentoNew ) )
            {
                fkDepartamentoOld.getTbFuncionarioList().remove( tbFuncionario );
                fkDepartamentoOld = em.merge( fkDepartamentoOld );
            }
            if ( fkDepartamentoNew != null && !fkDepartamentoNew.equals( fkDepartamentoOld ) )
            {
                fkDepartamentoNew.getTbFuncionarioList().add( tbFuncionario );
                fkDepartamentoNew = em.merge( fkDepartamentoNew );
            }
            if ( fkEspecialidadeOld != null && !fkEspecialidadeOld.equals( fkEspecialidadeNew ) )
            {
                fkEspecialidadeOld.getTbFuncionarioList().remove( tbFuncionario );
                fkEspecialidadeOld = em.merge( fkEspecialidadeOld );
            }
            if ( fkEspecialidadeNew != null && !fkEspecialidadeNew.equals( fkEspecialidadeOld ) )
            {
                fkEspecialidadeNew.getTbFuncionarioList().add( tbFuncionario );
                fkEspecialidadeNew = em.merge( fkEspecialidadeNew );
            }
            if ( fkEstadoCivilOld != null && !fkEstadoCivilOld.equals( fkEstadoCivilNew ) )
            {
                fkEstadoCivilOld.getTbFuncionarioList().remove( tbFuncionario );
                fkEstadoCivilOld = em.merge( fkEstadoCivilOld );
            }
            if ( fkEstadoCivilNew != null && !fkEstadoCivilNew.equals( fkEstadoCivilOld ) )
            {
                fkEstadoCivilNew.getTbFuncionarioList().add( tbFuncionario );
                fkEstadoCivilNew = em.merge( fkEstadoCivilNew );
            }
            if ( fkFuncaoOld != null && !fkFuncaoOld.equals( fkFuncaoNew ) )
            {
                fkFuncaoOld.getTbFuncionarioList().remove( tbFuncionario );
                fkFuncaoOld = em.merge( fkFuncaoOld );
            }
            if ( fkFuncaoNew != null && !fkFuncaoNew.equals( fkFuncaoOld ) )
            {
                fkFuncaoNew.getTbFuncionarioList().add( tbFuncionario );
                fkFuncaoNew = em.merge( fkFuncaoNew );
            }
            if ( fkGrauAcademicoOld != null && !fkGrauAcademicoOld.equals( fkGrauAcademicoNew ) )
            {
                fkGrauAcademicoOld.getTbFuncionarioList().remove( tbFuncionario );
                fkGrauAcademicoOld = em.merge( fkGrauAcademicoOld );
            }
            if ( fkGrauAcademicoNew != null && !fkGrauAcademicoNew.equals( fkGrauAcademicoOld ) )
            {
                fkGrauAcademicoNew.getTbFuncionarioList().add( tbFuncionario );
                fkGrauAcademicoNew = em.merge( fkGrauAcademicoNew );
            }
            if ( idStatusFKOld != null && !idStatusFKOld.equals( idStatusFKNew ) )
            {
                idStatusFKOld.getTbFuncionarioList().remove( tbFuncionario );
                idStatusFKOld = em.merge( idStatusFKOld );
            }
            if ( idStatusFKNew != null && !idStatusFKNew.equals( idStatusFKOld ) )
            {
                idStatusFKNew.getTbFuncionarioList().add( tbFuncionario );
                idStatusFKNew = em.merge( idStatusFKNew );
            }
            if ( fkUsuarioOld != null && !fkUsuarioOld.equals( fkUsuarioNew ) )
            {
                fkUsuarioOld.getTbFuncionarioList().remove( tbFuncionario );
                fkUsuarioOld = em.merge( fkUsuarioOld );
            }
            if ( fkUsuarioNew != null && !fkUsuarioNew.equals( fkUsuarioOld ) )
            {
                fkUsuarioNew.getTbFuncionarioList().add( tbFuncionario );
                fkUsuarioNew = em.merge( fkUsuarioNew );
            }
            if ( fkEmpresaOld != null && !fkEmpresaOld.equals( fkEmpresaNew ) )
            {
                fkEmpresaOld.getTbFuncionarioList().remove( tbFuncionario );
                fkEmpresaOld = em.merge( fkEmpresaOld );
            }
            if ( fkEmpresaNew != null && !fkEmpresaNew.equals( fkEmpresaOld ) )
            {
                fkEmpresaNew.getTbFuncionarioList().add( tbFuncionario );
                fkEmpresaNew = em.merge( fkEmpresaNew );
            }
            if ( fkModalidadeOld != null && !fkModalidadeOld.equals( fkModalidadeNew ) )
            {
                fkModalidadeOld.getTbFuncionarioList().remove( tbFuncionario );
                fkModalidadeOld = em.merge( fkModalidadeOld );
            }
            if ( fkModalidadeNew != null && !fkModalidadeNew.equals( fkModalidadeOld ) )
            {
                fkModalidadeNew.getTbFuncionarioList().add( tbFuncionario );
                fkModalidadeNew = em.merge( fkModalidadeNew );
            }
            for ( ItemSalarioExtra itemSalarioExtraListNewItemSalarioExtra : itemSalarioExtraListNew )
            {
                if ( !itemSalarioExtraListOld.contains( itemSalarioExtraListNewItemSalarioExtra ) )
                {
                    TbFuncionario oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra = itemSalarioExtraListNewItemSalarioExtra.getFkTbFuncionario();
                    itemSalarioExtraListNewItemSalarioExtra.setFkTbFuncionario( tbFuncionario );
                    itemSalarioExtraListNewItemSalarioExtra = em.merge( itemSalarioExtraListNewItemSalarioExtra );
                    if ( oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra != null && !oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra.equals( tbFuncionario ) )
                    {
                        oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra.getItemSalarioExtraList().remove( itemSalarioExtraListNewItemSalarioExtra );
                        oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra = em.merge( oldFkTbFuncionarioOfItemSalarioExtraListNewItemSalarioExtra );
                    }
                }
            }
            for ( PedidoFeria pedidoFeriaListNewPedidoFeria : pedidoFeriaListNew )
            {
                if ( !pedidoFeriaListOld.contains( pedidoFeriaListNewPedidoFeria ) )
                {
                    TbFuncionario oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria = pedidoFeriaListNewPedidoFeria.getFkFuncionario();
                    pedidoFeriaListNewPedidoFeria.setFkFuncionario( tbFuncionario );
                    pedidoFeriaListNewPedidoFeria = em.merge( pedidoFeriaListNewPedidoFeria );
                    if ( oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria != null && !oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria.getPedidoFeriaList().remove( pedidoFeriaListNewPedidoFeria );
                        oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria = em.merge( oldFkFuncionarioOfPedidoFeriaListNewPedidoFeria );
                    }
                }
            }
            for ( ReciboRh reciboRhListNewReciboRh : reciboRhListNew )
            {
                if ( !reciboRhListOld.contains( reciboRhListNewReciboRh ) )
                {
                    TbFuncionario oldFkFuncionarioOfReciboRhListNewReciboRh = reciboRhListNewReciboRh.getFkFuncionario();
                    reciboRhListNewReciboRh.setFkFuncionario( tbFuncionario );
                    reciboRhListNewReciboRh = em.merge( reciboRhListNewReciboRh );
                    if ( oldFkFuncionarioOfReciboRhListNewReciboRh != null && !oldFkFuncionarioOfReciboRhListNewReciboRh.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfReciboRhListNewReciboRh.getReciboRhList().remove( reciboRhListNewReciboRh );
                        oldFkFuncionarioOfReciboRhListNewReciboRh = em.merge( oldFkFuncionarioOfReciboRhListNewReciboRh );
                    }
                }
            }
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal : pagamentoSubsidioFeriaNatalListNew )
            {
                if ( !pagamentoSubsidioFeriaNatalListOld.contains( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal ) )
                {
                    TbFuncionario oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.getFkFuncionario();
                    pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.setFkFuncionario( tbFuncionario );
                    pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = em.merge( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                    if ( oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal != null && !oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal.getPagamentoSubsidioFeriaNatalList().remove( pagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                        oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal = em.merge( oldFkFuncionarioOfPagamentoSubsidioFeriaNatalListNewPagamentoSubsidioFeriaNatal );
                    }
                }
            }
            for ( TbFalta tbFaltaListOldTbFalta : tbFaltaListOld )
            {
                if ( !tbFaltaListNew.contains( tbFaltaListOldTbFalta ) )
                {
                    tbFaltaListOldTbFalta.setIdFuncionarioFK( null );
                    tbFaltaListOldTbFalta = em.merge( tbFaltaListOldTbFalta );
                }
            }
            for ( TbFalta tbFaltaListNewTbFalta : tbFaltaListNew )
            {
                if ( !tbFaltaListOld.contains( tbFaltaListNewTbFalta ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbFaltaListNewTbFalta = tbFaltaListNewTbFalta.getIdFuncionarioFK();
                    tbFaltaListNewTbFalta.setIdFuncionarioFK( tbFuncionario );
                    tbFaltaListNewTbFalta = em.merge( tbFaltaListNewTbFalta );
                    if ( oldIdFuncionarioFKOfTbFaltaListNewTbFalta != null && !oldIdFuncionarioFKOfTbFaltaListNewTbFalta.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbFaltaListNewTbFalta.getTbFaltaList().remove( tbFaltaListNewTbFalta );
                        oldIdFuncionarioFKOfTbFaltaListNewTbFalta = em.merge( oldIdFuncionarioFKOfTbFaltaListNewTbFalta );
                    }
                }
            }
            for ( TbSalario tbSalarioListOldTbSalario : tbSalarioListOld )
            {
                if ( !tbSalarioListNew.contains( tbSalarioListOldTbSalario ) )
                {
                    tbSalarioListOldTbSalario.setIdFuncionarioFK( null );
                    tbSalarioListOldTbSalario = em.merge( tbSalarioListOldTbSalario );
                }
            }
            for ( TbSalario tbSalarioListNewTbSalario : tbSalarioListNew )
            {
                if ( !tbSalarioListOld.contains( tbSalarioListNewTbSalario ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbSalarioListNewTbSalario = tbSalarioListNewTbSalario.getIdFuncionarioFK();
                    tbSalarioListNewTbSalario.setIdFuncionarioFK( tbFuncionario );
                    tbSalarioListNewTbSalario = em.merge( tbSalarioListNewTbSalario );
                    if ( oldIdFuncionarioFKOfTbSalarioListNewTbSalario != null && !oldIdFuncionarioFKOfTbSalarioListNewTbSalario.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbSalarioListNewTbSalario.getTbSalarioList().remove( tbSalarioListNewTbSalario );
                        oldIdFuncionarioFKOfTbSalarioListNewTbSalario = em.merge( oldIdFuncionarioFKOfTbSalarioListNewTbSalario );
                    }
                }
            }
            for ( Anexos anexosListNewAnexos : anexosListNew )
            {
                if ( !anexosListOld.contains( anexosListNewAnexos ) )
                {
                    TbFuncionario oldFkFuncionarioOfAnexosListNewAnexos = anexosListNewAnexos.getFkFuncionario();
                    anexosListNewAnexos.setFkFuncionario( tbFuncionario );
                    anexosListNewAnexos = em.merge( anexosListNewAnexos );
                    if ( oldFkFuncionarioOfAnexosListNewAnexos != null && !oldFkFuncionarioOfAnexosListNewAnexos.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfAnexosListNewAnexos.getAnexosList().remove( anexosListNewAnexos );
                        oldFkFuncionarioOfAnexosListNewAnexos = em.merge( oldFkFuncionarioOfAnexosListNewAnexos );
                    }
                }
            }
            for ( FechoContrato fechoContratoListNewFechoContrato : fechoContratoListNew )
            {
                if ( !fechoContratoListOld.contains( fechoContratoListNewFechoContrato ) )
                {
                    TbFuncionario oldFkFuncionarioOfFechoContratoListNewFechoContrato = fechoContratoListNewFechoContrato.getFkFuncionario();
                    fechoContratoListNewFechoContrato.setFkFuncionario( tbFuncionario );
                    fechoContratoListNewFechoContrato = em.merge( fechoContratoListNewFechoContrato );
                    if ( oldFkFuncionarioOfFechoContratoListNewFechoContrato != null && !oldFkFuncionarioOfFechoContratoListNewFechoContrato.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfFechoContratoListNewFechoContrato.getFechoContratoList().remove( fechoContratoListNewFechoContrato );
                        oldFkFuncionarioOfFechoContratoListNewFechoContrato = em.merge( oldFkFuncionarioOfFechoContratoListNewFechoContrato );
                    }
                }
            }
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioListOld )
            {
                if ( !tbItemSubsidiosFuncionarioListNew.contains( tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario ) )
                {
                    tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario.setIdFuncionarioFK( null );
                    tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListOldTbItemSubsidiosFuncionario );
                }
            }
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioListNew )
            {
                if ( !tbItemSubsidiosFuncionarioListOld.contains( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.getIdFuncionarioFK();
                    tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.setIdFuncionarioFK( tbFuncionario );
                    tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
                    if ( oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario != null && !oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario.getTbItemSubsidiosFuncionarioList().remove( tbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
                        oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario = em.merge( oldIdFuncionarioFKOfTbItemSubsidiosFuncionarioListNewTbItemSubsidiosFuncionario );
                    }
                }
            }
            for ( TbAdiantamento tbAdiantamentoListOldTbAdiantamento : tbAdiantamentoListOld )
            {
                if ( !tbAdiantamentoListNew.contains( tbAdiantamentoListOldTbAdiantamento ) )
                {
                    tbAdiantamentoListOldTbAdiantamento.setIdFuncionarioFK( null );
                    tbAdiantamentoListOldTbAdiantamento = em.merge( tbAdiantamentoListOldTbAdiantamento );
                }
            }
            for ( TbAdiantamento tbAdiantamentoListNewTbAdiantamento : tbAdiantamentoListNew )
            {
                if ( !tbAdiantamentoListOld.contains( tbAdiantamentoListNewTbAdiantamento ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento = tbAdiantamentoListNewTbAdiantamento.getIdFuncionarioFK();
                    tbAdiantamentoListNewTbAdiantamento.setIdFuncionarioFK( tbFuncionario );
                    tbAdiantamentoListNewTbAdiantamento = em.merge( tbAdiantamentoListNewTbAdiantamento );
                    if ( oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento != null && !oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento.getTbAdiantamentoList().remove( tbAdiantamentoListNewTbAdiantamento );
                        oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento = em.merge( oldIdFuncionarioFKOfTbAdiantamentoListNewTbAdiantamento );
                    }
                }
            }
            for ( AgregadoFamiliar agregadoFamiliarListNewAgregadoFamiliar : agregadoFamiliarListNew )
            {
                if ( !agregadoFamiliarListOld.contains( agregadoFamiliarListNewAgregadoFamiliar ) )
                {
                    TbFuncionario oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar = agregadoFamiliarListNewAgregadoFamiliar.getFkFuncionario();
                    agregadoFamiliarListNewAgregadoFamiliar.setFkFuncionario( tbFuncionario );
                    agregadoFamiliarListNewAgregadoFamiliar = em.merge( agregadoFamiliarListNewAgregadoFamiliar );
                    if ( oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar != null && !oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar.getAgregadoFamiliarList().remove( agregadoFamiliarListNewAgregadoFamiliar );
                        oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar = em.merge( oldFkFuncionarioOfAgregadoFamiliarListNewAgregadoFamiliar );
                    }
                }
            }
            for ( TbConta tbContaListOldTbConta : tbContaListOld )
            {
                if ( !tbContaListNew.contains( tbContaListOldTbConta ) )
                {
                    tbContaListOldTbConta.setIdFuncionarioFK( null );
                    tbContaListOldTbConta = em.merge( tbContaListOldTbConta );
                }
            }
            for ( TbConta tbContaListNewTbConta : tbContaListNew )
            {
                if ( !tbContaListOld.contains( tbContaListNewTbConta ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbContaListNewTbConta = tbContaListNewTbConta.getIdFuncionarioFK();
                    tbContaListNewTbConta.setIdFuncionarioFK( tbFuncionario );
                    tbContaListNewTbConta = em.merge( tbContaListNewTbConta );
                    if ( oldIdFuncionarioFKOfTbContaListNewTbConta != null && !oldIdFuncionarioFKOfTbContaListNewTbConta.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbContaListNewTbConta.getTbContaList().remove( tbContaListNewTbConta );
                        oldIdFuncionarioFKOfTbContaListNewTbConta = em.merge( oldIdFuncionarioFKOfTbContaListNewTbConta );
                    }
                }
            }
            for ( TbTempo tbTempoListOldTbTempo : tbTempoListOld )
            {
                if ( !tbTempoListNew.contains( tbTempoListOldTbTempo ) )
                {
                    tbTempoListOldTbTempo.setIdFuncionarioFK( null );
                    tbTempoListOldTbTempo = em.merge( tbTempoListOldTbTempo );
                }
            }
            for ( TbTempo tbTempoListNewTbTempo : tbTempoListNew )
            {
                if ( !tbTempoListOld.contains( tbTempoListNewTbTempo ) )
                {
                    TbFuncionario oldIdFuncionarioFKOfTbTempoListNewTbTempo = tbTempoListNewTbTempo.getIdFuncionarioFK();
                    tbTempoListNewTbTempo.setIdFuncionarioFK( tbFuncionario );
                    tbTempoListNewTbTempo = em.merge( tbTempoListNewTbTempo );
                    if ( oldIdFuncionarioFKOfTbTempoListNewTbTempo != null && !oldIdFuncionarioFKOfTbTempoListNewTbTempo.equals( tbFuncionario ) )
                    {
                        oldIdFuncionarioFKOfTbTempoListNewTbTempo.getTbTempoList().remove( tbTempoListNewTbTempo );
                        oldIdFuncionarioFKOfTbTempoListNewTbTempo = em.merge( oldIdFuncionarioFKOfTbTempoListNewTbTempo );
                    }
                }
            }
            for ( PrevioAviso previoAvisoListNewPrevioAviso : previoAvisoListNew )
            {
                if ( !previoAvisoListOld.contains( previoAvisoListNewPrevioAviso ) )
                {
                    TbFuncionario oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso = previoAvisoListNewPrevioAviso.getFkFuncionario();
                    previoAvisoListNewPrevioAviso.setFkFuncionario( tbFuncionario );
                    previoAvisoListNewPrevioAviso = em.merge( previoAvisoListNewPrevioAviso );
                    if ( oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso != null && !oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso.equals( tbFuncionario ) )
                    {
                        oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso.getPrevioAvisoList().remove( previoAvisoListNewPrevioAviso );
                        oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso = em.merge( oldFkFuncionarioOfPrevioAvisoListNewPrevioAviso );
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
                Integer id = tbFuncionario.getIdFuncionario();
                if ( findTbFuncionario( id ) == null )
                {
                    throw new NonexistentEntityException( "The tbFuncionario with id " + id + " no longer exists." );
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
            TbFuncionario tbFuncionario;
            try
            {
                tbFuncionario = em.getReference( TbFuncionario.class, id );
                tbFuncionario.getIdFuncionario();
            }
            catch ( EntityNotFoundException enfe )
            {
                throw new NonexistentEntityException( "The tbFuncionario with id " + id + " no longer exists.", enfe );
            }
            List<String> illegalOrphanMessages = null;
            List<ItemSalarioExtra> itemSalarioExtraListOrphanCheck = tbFuncionario.getItemSalarioExtraList();
            for ( ItemSalarioExtra itemSalarioExtraListOrphanCheckItemSalarioExtra : itemSalarioExtraListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the ItemSalarioExtra " + itemSalarioExtraListOrphanCheckItemSalarioExtra + " in its itemSalarioExtraList field has a non-nullable fkTbFuncionario field." );
            }
            List<PedidoFeria> pedidoFeriaListOrphanCheck = tbFuncionario.getPedidoFeriaList();
            for ( PedidoFeria pedidoFeriaListOrphanCheckPedidoFeria : pedidoFeriaListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the PedidoFeria " + pedidoFeriaListOrphanCheckPedidoFeria + " in its pedidoFeriaList field has a non-nullable fkFuncionario field." );
            }
            List<ReciboRh> reciboRhListOrphanCheck = tbFuncionario.getReciboRhList();
            for ( ReciboRh reciboRhListOrphanCheckReciboRh : reciboRhListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the ReciboRh " + reciboRhListOrphanCheckReciboRh + " in its reciboRhList field has a non-nullable fkFuncionario field." );
            }
            List<PagamentoSubsidioFeriaNatal> pagamentoSubsidioFeriaNatalListOrphanCheck = tbFuncionario.getPagamentoSubsidioFeriaNatalList();
            for ( PagamentoSubsidioFeriaNatal pagamentoSubsidioFeriaNatalListOrphanCheckPagamentoSubsidioFeriaNatal : pagamentoSubsidioFeriaNatalListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the PagamentoSubsidioFeriaNatal " + pagamentoSubsidioFeriaNatalListOrphanCheckPagamentoSubsidioFeriaNatal + " in its pagamentoSubsidioFeriaNatalList field has a non-nullable fkFuncionario field." );
            }
            List<Anexos> anexosListOrphanCheck = tbFuncionario.getAnexosList();
            for ( Anexos anexosListOrphanCheckAnexos : anexosListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the Anexos " + anexosListOrphanCheckAnexos + " in its anexosList field has a non-nullable fkFuncionario field." );
            }
            List<FechoContrato> fechoContratoListOrphanCheck = tbFuncionario.getFechoContratoList();
            for ( FechoContrato fechoContratoListOrphanCheckFechoContrato : fechoContratoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the FechoContrato " + fechoContratoListOrphanCheckFechoContrato + " in its fechoContratoList field has a non-nullable fkFuncionario field." );
            }
            List<AgregadoFamiliar> agregadoFamiliarListOrphanCheck = tbFuncionario.getAgregadoFamiliarList();
            for ( AgregadoFamiliar agregadoFamiliarListOrphanCheckAgregadoFamiliar : agregadoFamiliarListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the AgregadoFamiliar " + agregadoFamiliarListOrphanCheckAgregadoFamiliar + " in its agregadoFamiliarList field has a non-nullable fkFuncionario field." );
            }
            List<PrevioAviso> previoAvisoListOrphanCheck = tbFuncionario.getPrevioAvisoList();
            for ( PrevioAviso previoAvisoListOrphanCheckPrevioAviso : previoAvisoListOrphanCheck )
            {
                if ( illegalOrphanMessages == null )
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add( "This TbFuncionario (" + tbFuncionario + ") cannot be destroyed since the PrevioAviso " + previoAvisoListOrphanCheckPrevioAviso + " in its previoAvisoList field has a non-nullable fkFuncionario field." );
            }
            if ( illegalOrphanMessages != null )
            {
                throw new IllegalOrphanException( illegalOrphanMessages );
            }
            TbDepartamento fkDepartamento = tbFuncionario.getFkDepartamento();
            if ( fkDepartamento != null )
            {
                fkDepartamento.getTbFuncionarioList().remove( tbFuncionario );
                fkDepartamento = em.merge( fkDepartamento );
            }
            TbEspecialidade fkEspecialidade = tbFuncionario.getFkEspecialidade();
            if ( fkEspecialidade != null )
            {
                fkEspecialidade.getTbFuncionarioList().remove( tbFuncionario );
                fkEspecialidade = em.merge( fkEspecialidade );
            }
            TbEstadoCivil fkEstadoCivil = tbFuncionario.getFkEstadoCivil();
            if ( fkEstadoCivil != null )
            {
                fkEstadoCivil.getTbFuncionarioList().remove( tbFuncionario );
                fkEstadoCivil = em.merge( fkEstadoCivil );
            }
            TbFuncao fkFuncao = tbFuncionario.getFkFuncao();
            if ( fkFuncao != null )
            {
                fkFuncao.getTbFuncionarioList().remove( tbFuncionario );
                fkFuncao = em.merge( fkFuncao );
            }
            TbGrauAcademico fkGrauAcademico = tbFuncionario.getFkGrauAcademico();
            if ( fkGrauAcademico != null )
            {
                fkGrauAcademico.getTbFuncionarioList().remove( tbFuncionario );
                fkGrauAcademico = em.merge( fkGrauAcademico );
            }
            TbStatus idStatusFK = tbFuncionario.getIdStatusFK();
            if ( idStatusFK != null )
            {
                idStatusFK.getTbFuncionarioList().remove( tbFuncionario );
                idStatusFK = em.merge( idStatusFK );
            }
            TbUsuario fkUsuario = tbFuncionario.getFkUsuario();
            if ( fkUsuario != null )
            {
                fkUsuario.getTbFuncionarioList().remove( tbFuncionario );
                fkUsuario = em.merge( fkUsuario );
            }
            Empresa fkEmpresa = tbFuncionario.getFkEmpresa();
            if ( fkEmpresa != null )
            {
                fkEmpresa.getTbFuncionarioList().remove( tbFuncionario );
                fkEmpresa = em.merge( fkEmpresa );
            }
            Modalidade fkModalidade = tbFuncionario.getFkModalidade();
            if ( fkModalidade != null )
            {
                fkModalidade.getTbFuncionarioList().remove( tbFuncionario );
                fkModalidade = em.merge( fkModalidade );
            }
            List<TbFalta> tbFaltaList = tbFuncionario.getTbFaltaList();
            for ( TbFalta tbFaltaListTbFalta : tbFaltaList )
            {
                tbFaltaListTbFalta.setIdFuncionarioFK( null );
                tbFaltaListTbFalta = em.merge( tbFaltaListTbFalta );
            }
            List<TbSalario> tbSalarioList = tbFuncionario.getTbSalarioList();
            for ( TbSalario tbSalarioListTbSalario : tbSalarioList )
            {
                tbSalarioListTbSalario.setIdFuncionarioFK( null );
                tbSalarioListTbSalario = em.merge( tbSalarioListTbSalario );
            }
            List<TbItemSubsidiosFuncionario> tbItemSubsidiosFuncionarioList = tbFuncionario.getTbItemSubsidiosFuncionarioList();
            for ( TbItemSubsidiosFuncionario tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario : tbItemSubsidiosFuncionarioList )
            {
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario.setIdFuncionarioFK( null );
                tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario = em.merge( tbItemSubsidiosFuncionarioListTbItemSubsidiosFuncionario );
            }
            List<TbAdiantamento> tbAdiantamentoList = tbFuncionario.getTbAdiantamentoList();
            for ( TbAdiantamento tbAdiantamentoListTbAdiantamento : tbAdiantamentoList )
            {
                tbAdiantamentoListTbAdiantamento.setIdFuncionarioFK( null );
                tbAdiantamentoListTbAdiantamento = em.merge( tbAdiantamentoListTbAdiantamento );
            }
            List<TbConta> tbContaList = tbFuncionario.getTbContaList();
            for ( TbConta tbContaListTbConta : tbContaList )
            {
                tbContaListTbConta.setIdFuncionarioFK( null );
                tbContaListTbConta = em.merge( tbContaListTbConta );
            }
            List<TbTempo> tbTempoList = tbFuncionario.getTbTempoList();
            for ( TbTempo tbTempoListTbTempo : tbTempoList )
            {
                tbTempoListTbTempo.setIdFuncionarioFK( null );
                tbTempoListTbTempo = em.merge( tbTempoListTbTempo );
            }
            em.remove( tbFuncionario );
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

    public List<TbFuncionario> findTbFuncionarioEntities()
    {
        return findTbFuncionarioEntities( true, -1, -1 );
    }

    public List<TbFuncionario> findTbFuncionarioEntities( int maxResults, int firstResult )
    {
        return findTbFuncionarioEntities( false, maxResults, firstResult );
    }

    private List<TbFuncionario> findTbFuncionarioEntities( boolean all, int maxResults, int firstResult )
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select( cq.from( TbFuncionario.class ) );
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

    public TbFuncionario findTbFuncionario( Integer id )
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find( TbFuncionario.class, id );
        }
        finally
        {
            em.close();
        }
    }

    public int getTbFuncionarioCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TbFuncionario> rt = cq.from( TbFuncionario.class );
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
