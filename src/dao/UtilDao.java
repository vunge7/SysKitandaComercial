/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.sql.Connection;
import controlador.TbAcertoJpaController;
import entity.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static util.JPAEntityMannagerFactoryUtil.em;

/**
 *
 * @author Toshiba
 */
public class UtilDao extends TbAcertoJpaController
{

    public static EntityManagerFactory emf = null;

    static
    {
        emf = em;
    }

    public static EntityManager getEntityManager1 ()
    {
        return emf.createEntityManager ();
    }

    public UtilDao ()
    {
        this ( emf );
    }

    public UtilDao ( EntityManagerFactory emf )
    {
        super ( emf );
    }

    public static List<Object[]> getExtratoBonusCliente (
            String armazem,
            String cliente,
            Date dataInicio,
            Date dataFim,
            String tipoDocumento,
            Double percent
    )
    {
        TbArmazem tbArmazem = ArmazemDao.findByDesigncao ( armazem );
        String fkArmazem = Objects.isNull ( tbArmazem ) ? "'%%'" : String.valueOf ( tbArmazem.getCodigo () );
        TbCliente tbCliente = ClienteDao.findByNome ( cliente );
        String fkCliente = Objects.isNull ( tbCliente ) ? "'%%'" : String.valueOf ( tbCliente.getCodigo () );

        SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        String dateInicioString = Objects.isNull ( dataInicio ) ? "NULL" : String.format ( "'%s'", dateFormat.format ( dataInicio ) );
        String dateFimString = Objects.isNull ( dataFim ) ? "now()" : String.format ( "'%s'", dateFormat.format ( dataFim ) );
        tipoDocumento = String.format ( "'%s'", tipoDocumento );
        percent = Objects.isNull ( percent ) ? 0 : percent;
        int fieldCount = 1;
        final int idArmazemFK = fieldCount ++;
        final int codigo_cliente = fieldCount ++;
        final int data_inicio = fieldCount ++;
        final int data_fim = fieldCount ++;
        final int abreviacao = fieldCount ++;

        String queryString = String.format ( "select  distinct "
                + "codigo_produto fk_produto, "
                + "p.designacao produto, "
                + "quantidade, "
                + "	(select preco_compra from item_compras inner join compras c on pk_compra = fk_compra where fk_produto = codigo_produto "
                + "order by c.data_compra desc limit 1) "
                + " preco_fabrica, "
                + "codigo_cliente fk_cliente, "
                + "c.nome cliente, "
                + "v.dataVenda data, "
                + "idArmazemFK fk_armazem, "
                + "a.designacao armazem, "
                + "fk_documento, "
                + "d.abreviacao documento, "
                + "v.codigo fk_venda, "
                + "v.cod_fact cod_fact, "
                + "(quantidade*"
                + "(select preco_compra from item_compras inner join compras c on pk_compra = fk_compra where fk_produto = codigo_produto "
                + "order by c.data_compra desc limit 1 )"
                + ") sub_total, "
                + "((quantidade*"
                + "(select preco_compra from item_compras inner join compras c on pk_compra = fk_compra where fk_produto = codigo_produto " +
"order by c.data_compra desc limit 1) "
                + ")*%s) bonus  "
                + "from  "
                + "tb_item_venda "
                + "inner join tb_produto p on codigo_produto = p.codigo "
                + "inner join tb_preco pr on fk_produto = codigo_produto "
                + "inner join tb_venda v on v.codigo = codigo_venda "
                + "inner join tb_cliente c on c.codigo = codigo_cliente "
                + "inner join tb_armazem a on a.codigo = idArmazemFK "
                + "inner join documento d on d.pk_documento = fk_documento "
                + "WHERE "
                + "idArmazemFK like %s "
                + "and "
                + "codigo_cliente like %s "
                + "and "
                + "data between DATE(%s) and DATE( %s ) "
                + "and  "
                + "d.abreviacao like %s"
                + "",
                percent, fkArmazem, fkCliente, dateInicioString, dateFimString, tipoDocumento
        );
        EntityManager em = getEntityManager1 ();
        Query query = em.createNativeQuery ( queryString );

        System.err.println ( "fkArmazem: " + fkArmazem );
        System.err.println ( "fkCliente: " + fkCliente );
        System.err.println ( "dateInicioString: " + dateInicioString );
        System.err.println ( "dateFimString: " + dateFimString );
        System.err.println ( "tipoDocumento: " + tipoDocumento );

        System.err.println ( String.format ( "=================================== %s", " " ) );
        System.err.println ( String.format ( "=================================== %s", " " ) );
        System.err.println ( String.format ( "Query: %s", queryString ) );

        List<Object[]> result = query.getResultList ();
        System.err.println ( "result: " + result );
        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;

    }

    public static List<Object[]> getExtratoBonusEmpresa ( String armazem, String fornecedor, Date dataInicio, Date dataFim, String tipoDocumento, double percent )
    {
        TbArmazem tbArmazem = ArmazemDao.findByDesigncao ( armazem );
        String fkArmazem = Objects.isNull ( tbArmazem ) ? "'%%'" : String.valueOf ( tbArmazem.getCodigo () );
        TbFornecedor tbFornecedor = FornecedorDao.findByNome ( fornecedor );
        String fkFornecedor = Objects.isNull ( tbFornecedor ) ? "'%%'" : String.valueOf ( tbFornecedor.getCodigo () );

        SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        String dateInicioString = Objects.isNull ( dataInicio ) ? "NULL" : String.format ( "'%s'", dateFormat.format ( dataInicio ) );
        String dateFimString = Objects.isNull ( dataFim ) ? "now()" : String.format ( "'%s'", dateFormat.format ( dataFim ) );
        tipoDocumento = String.format ( "'%s'", tipoDocumento );
        percent = Objects.isNull ( percent ) ? 0 : percent;
        int fieldCount = 1;
        final int idArmazemFK = fieldCount ++;
        final int codigo_cliente = fieldCount ++;
        final int data_inicio = fieldCount ++;
        final int data_fim = fieldCount ++;
        final int abreviacao = fieldCount ++;

        String queryString = String.format ( "select  distinct "
                + "ic.fk_produto, "
                + "p.designacao produto, "
                + "quantidade, "
                + "ic.preco_compra preco_fabrica , "
                + " "
                + "fk_fornecedor , "
                + "c.nome fornecedor, "
                + "v.data_compra data, "
                + "idArmazemFK fk_armazem, "
                + " "
                + "a.designacao armazem, "
                + "fk_documento, "
                + "d.abreviacao documento, "
                + "v.pk_compra fk_compra, "
                + " "
                + "v.cod_fact cod_fact, "
                + "(quantidade*ic.preco_compra) sub_total, "
                + "((quantidade*ic.preco_compra)*%s) bonus "
                + "from  "
                + "item_compras ic "
                + "inner join tb_produto p on p.codigo = ic.fk_produto "
                + "inner join tb_preco pr on pr.fk_produto = ic.fk_produto "
                + "inner join compras v on v.pk_compra = fk_compra "
                + "inner join tb_fornecedor c on c.codigo = fk_fornecedor "
                + "inner join tb_armazem a on a.codigo = idArmazemFK "
                + "inner join documento d on d.pk_documento = fk_documento "
                + " "
                + "WHERE "
                + "idArmazemFK like %s "
                + "and "
                + "c.codigo like %s "
                + "and "
                + "data between DATE( %s ) and DATE( %s ) "
                + "and  "
                + "d.abreviacao like %s"
                + "",
                percent, fkArmazem, fkFornecedor, dateInicioString, dateFimString, tipoDocumento
        );
        EntityManager em = getEntityManager1 ();
        Query query = em.createNativeQuery ( queryString );

        System.err.println ( "fkArmazem: " + fkArmazem );
        System.err.println ( "fkFornecedor: " + fkFornecedor );
        System.err.println ( "dateInicioString: " + dateInicioString );
        System.err.println ( "dateFimString: " + dateFimString );
        System.err.println ( "tipoDocumento: " + tipoDocumento );

        System.err.println ( String.format ( "=================================== %s", " " ) );
        System.err.println ( String.format ( "=================================== %s", " " ) );
        System.err.println ( String.format ( "Query: %s", queryString ) );

        List<Object[]> result = query.getResultList ();
        System.err.println ( "result: " + result );
        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;

    }

    public static List<Object[]> getDetalhesFatura ( Object codFactObject )
    {

        String pkFatura = Objects.isNull ( codFactObject ) ? "0" : ( String ) codFactObject;
        TbVenda tbVenda = VendaDao.findByRefCod ( pkFatura );
        Integer pkVendaBd = ( Objects.isNull ( tbVenda ) ? 0 : tbVenda.getCodigo () );

        String queryString = String.format ( "SELECT "
                + "tb_venda.codigo pk_venda, "
                + "documento.designacao,  "
                + "tb_venda.cod_fact, "
                + "moeda.abreviacao, "
//                + "tb_banco.descrisao, "
                + "tb_venda.dataVenda, "
                + "tb_venda.hora, "
                + "tb_produto.codigo, "
                + "tb_produto.designacao, "
                + "tb_item_venda.codigo_isensao, "
                + "tb_item_venda.quantidade, "
                + "tb_preco.preco_venda, "
                + "tb_item_venda.desconto, "
                + "tb_item_venda.valor_iva, "
                + "tb_item_venda.total, "
                + "tb_venda.total_geral, "
                + "tb_venda.desconto_comercial, "
                + "tb_venda.desconto_financeiro, "
                + "tb_venda.total_iva, "
                + "tb_venda.total_venda, "
                + "tb_venda.valor_entregue, "
                + "tb_venda.troco, "
                + "tb_venda.total_por_extenso "
                + "FROM "
                + "     tb_produto tb_produto INNER JOIN tb_item_venda tb_item_venda ON tb_produto.codigo = tb_item_venda.codigo_produto "
                + "     INNER JOIN tb_venda tb_venda ON tb_item_venda.codigo_venda = tb_venda.codigo "
                + "     INNER JOIN tb_preco tb_preco ON tb_item_venda.fk_preco = tb_preco.pk_preco "
                + "     INNER JOIN tb_cliente tb_cliente ON tb_venda.codigo_cliente = tb_cliente.codigo "
                + "     INNER JOIN tb_usuario tb_usuario ON tb_venda.codigo_usuario = tb_usuario.codigo "
                + "     INNER JOIN tb_armazem tb_armazem ON tb_venda.idArmazemFK = tb_armazem.codigo "
                + "     INNER JOIN cambio cambio ON tb_venda.fk_cambio = cambio.pk_cambio "
//                + "     INNER JOIN tb_banco tb_banco ON tb_venda.idBanco = tb_banco.idBanco "
                + "     INNER JOIN ano_economico ano_economico ON tb_venda.fk_ano_economico = ano_economico.pk_ano_economico "
                + "     INNER JOIN documento documento ON tb_venda.fk_documento = documento.pk_documento "
                + "     INNER JOIN moeda moeda ON cambio.fk_moeda = moeda.pk_moeda "
                + "     "
                + "WHERE "
                + "     tb_venda.codigo = %d AND status_eliminado = 'false' "
                + "",
                pkVendaBd
        );
        EntityManager em = getEntityManager1 ();
        Query query = em.createNativeQuery ( queryString );

        System.err.println ( "pkFatura: " + pkFatura );

        System.err.println ( String.format ( "Query: %s", queryString ) );

        List<Object[]> result = query.getResultList ();
        System.err.println ( "result: " + result );
        if (  ! result.isEmpty () )
        {
            return result;
        }
        return null;

    }
}
