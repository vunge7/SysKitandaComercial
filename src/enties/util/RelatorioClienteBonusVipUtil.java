/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enties.util;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RelatorioClienteBonusVipUtil {
    
    private int cod, qtd;
    private String produto;
    private double preco_venda_fabrica, bonus;

    public RelatorioClienteBonusVipUtil() {
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public double getPreco_venda_fabrica() {
        return preco_venda_fabrica;
    }

    public void setPreco_venda_fabrica(double preco_venda_fabrica) {
        this.preco_venda_fabrica = preco_venda_fabrica;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
    public static void main(String[] args) {
        new RelatorioClienteBonusVipUtil();
    }
}
