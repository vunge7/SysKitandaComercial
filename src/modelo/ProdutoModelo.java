/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos dala Vunge
 */
public class ProdutoModelo {
    
    private int codigo, cod_Tipo_Produto, cod_fornecedores;
    private String designacao, data_fabrico, data_expiracao, cod_barra, data_entrada, status, stocavel;
    private float preco;
    
    
         
////       $designacao VARCHAR(45) ,
////     $preco FLOAT ,
////
////     $cod_Tipo_Produto INT,
////     $cod_fornecedores INT,
////     $data_fabrico DATE,
////     $data_expiracao DATE,
////     codBarra BIGINT,
////     $status VARCHAR(10),
////
////     $Codigo INT,
////     $operacao INT
////        
//    
    

    public ProdutoModelo() {
    }

    public ProdutoModelo(int codigo, int cod_Tipo_Produto, int cod_fornecedores, String designacao,String data_fabrico , String data_expiracao, String cod_barra, float preco) {
        this.codigo = codigo;
        this.cod_Tipo_Produto = cod_Tipo_Produto;
        this.cod_fornecedores = cod_fornecedores;
        this.designacao = designacao;
     
        this.data_fabrico = data_fabrico;
        this.data_expiracao = data_expiracao;
        this.cod_barra = cod_barra;
        this.data_entrada = data_entrada;
        this.status  = status;
        this.preco = preco;
    }

    public ProdutoModelo(int cod_Tipo_Produto, int cod_fornecedores, String designacao, String data_fabrico, 
        String data_expiracao , String data_entrada, String cod_barra, String status, float preco) {
        this.cod_Tipo_Produto = cod_Tipo_Produto;
        this.cod_fornecedores = cod_fornecedores;
        this.designacao = designacao;
      
        this.data_fabrico = data_fabrico;
        this.data_expiracao = data_expiracao;
        this.cod_barra = cod_barra;
        this. data_entrada = data_entrada;
        this.status  = status;
        this.preco = preco;
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
     * @return the cod_Tipo_Produto
     */
    public int getCod_Tipo_Produto() {
        return cod_Tipo_Produto;
    }

    /**
     * @param cod_Tipo_Produto the cod_Tipo_Produto to set
     */
    public void setCod_Tipo_Produto(int cod_Tipo_Produto) {
        this.cod_Tipo_Produto = cod_Tipo_Produto;
    }

    /**
     * @return the cod_fornecedores
     */
    public int getCod_fornecedores() {
        return cod_fornecedores;
    }

    /**
     * @param cod_fornecedores the cod_fornecedores to set
     */
    public void setCod_fornecedores(int cod_fornecedores) {
        this.cod_fornecedores = cod_fornecedores;
    }

    /**
     * @return the designacao
     */
    public String getDesignacao() {
        return designacao;
    }

    /**
     * @param designacao the designacao to set
     */
    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    /**
     * @return the data_fabrico
     */
    public String getData_fabrico() {
        return data_fabrico;
    }

    /**
     * @param data_fabrico the data_fabrico to set
     */
    public void setData_fabrico(String data_fabrico) {
        this.data_fabrico = data_fabrico;
    }

    /**
     * @return the data_expiracao
     */
    public String getData_expiracao() {
        return data_expiracao;
    }

    /**
     * @param data_expiracao the data_expiracao to set
     */
    public void setData_expiracao(String data_expiracao) {
        this.data_expiracao = data_expiracao;
    }

    /**
     * @return the cod_barra
     */
    public String getCod_barra() {
        return cod_barra;
    }

    /**
     * @param cod_barra the cod_barra to set
     */
    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    /**
     * @return the data_entrada
     */
    public String getData_entrada() {
        return data_entrada;
    }

    /**
     * @param data_entrada the data_entrada to set
     */
    public void setData_entrada(String data_entrada) {
        this.data_entrada = data_entrada;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the preco
     */
    public double getPreco() {
        return preco;
    }

    /**
     * @param preco the preco to set
     */
    public void setPreco(float preco) {
        this.preco = preco;
    }

    /**
     * @return the stocavel
     */
    public String getStocavel() {
        return stocavel;
    }

    /**
     * @param stocavel the stocavel to set
     */
    public void setStocavel(String stocavel) {
        this.stocavel = stocavel;
    }

    
    
    
    
    
    
}
