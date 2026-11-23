package comercial.relatorios;

import java.math.BigDecimal;

/** Linhas (beans) dos relatórios de vendas por cliente no iReport. */
public final class VendasClientesMapa {

    private VendasClientesMapa() {
    }

    /** relatorios/vendasClientes: um documento por linha, agrupado por cliente. */
    public static final class Documento {

        String cliente, nif, data, documento, utilizador, estado;
        int itens;
        BigDecimal desconto = BigDecimal.ZERO, iva = BigDecimal.ZERO, total = BigDecimal.ZERO;

        public String getCliente() { return cliente; }
        public String getNif() { return nif; }
        public String getData() { return data; }
        public String getDocumento() { return documento; }
        public String getUtilizador() { return utilizador; }
        public String getEstado() { return estado; }
        public Integer getItens() { return itens; }
        public BigDecimal getDesconto() { return desconto; }
        public BigDecimal getIva() { return iva; }
        public BigDecimal getTotal() { return total; }
    }

    /** relatorios/vendasClientesResumo: um cliente por linha. */
    public static final class Resumo {

        String cliente, nif, primeira, ultima;
        int documentos;
        BigDecimal desconto = BigDecimal.ZERO, iva = BigDecimal.ZERO, total = BigDecimal.ZERO;
        double percentagem;

        public String getCliente() { return cliente; }
        public String getNif() { return nif; }
        public String getPrimeira() { return primeira; }
        public String getUltima() { return ultima; }
        public Integer getDocumentos() { return documentos; }
        public BigDecimal getDesconto() { return desconto; }
        public BigDecimal getIva() { return iva; }
        public BigDecimal getTotal() { return total; }
        public Double getPercentagem() { return percentagem; }
    }

    /** relatorios/vendasClientesArtigos: um artigo por linha, agrupado por cliente. */
    public static final class Artigo {

        String cliente, nif, data, documento, artigo;
        double quantidade, ivaPercentagem;
        BigDecimal preco = BigDecimal.ZERO, desconto = BigDecimal.ZERO, total = BigDecimal.ZERO;

        public String getCliente() { return cliente; }
        public String getNif() { return nif; }
        public String getData() { return data; }
        public String getDocumento() { return documento; }
        public String getArtigo() { return artigo; }
        public Double getQuantidade() { return quantidade; }
        public Double getIvaPercentagem() { return ivaPercentagem; }
        public BigDecimal getPreco() { return preco; }
        public BigDecimal getDesconto() { return desconto; }
        public BigDecimal getTotal() { return total; }
    }
}
