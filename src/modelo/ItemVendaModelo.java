/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ItemVendaModelo {
    
    private int codigo, codigo_venda, codigo_produto,quantidade;
    private long fk_preco;
    
    float total;

    public ItemVendaModelo() {
    }

    public ItemVendaModelo(int codigo_venda, int codigo_produto, float total, int quantidade) {
        this.codigo_venda = codigo_venda;
        this.codigo_produto = codigo_produto;
        this.total = total;
        this.quantidade = quantidade;
        
    }

    public ItemVendaModelo(int codigo, int codigo_venda, int codigo_produto, float total,int quantidade) {
        this.codigo = codigo;
        this.codigo_venda = codigo_venda;
        this.codigo_produto = codigo_produto;
        this.total = total;
        this.quantidade = quantidade;
    }

    
    
    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the codigo_venda
     */
    public int getCodigo_venda() {
        return codigo_venda;
    }

    /**
     * @param codigo_venda the codigo_venda to set
     */
    public void setCodigo_venda(int codigo_venda) {
        this.codigo_venda = codigo_venda;
    }

    /**
     * @return the codigo_produto
     */
    public int getCodigo_produto() {
        return codigo_produto;
    }

    /**
     * @param codigo_produto the codigo_produto to set
     */
    public void setCodigo_produto(int codigo_produto) {
        this.codigo_produto = codigo_produto;
    }

    /**
     * @return the total
     */
    public float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public long getFk_preco() {
        return fk_preco;
    }

    public void setFk_preco(long fk_preco) {
        this.fk_preco = fk_preco;
    }
    
    
    
    
    
    
}
