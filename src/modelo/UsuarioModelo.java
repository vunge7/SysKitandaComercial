/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Domingos Dala Vunge
 */
public class UsuarioModelo {
    
    private int codigo = 0, codigo_sexo, idTipousuario;
    private String nome ,senha, status, dataNascimento, telefone, email, endereco, userName;

    public UsuarioModelo() {
    }

    public UsuarioModelo(int codigo_sexo, int idTipousuario, String nome ,String senha, String status, String dataNascimento, String telefone, String email, String endereco, String userName) {
        this.codigo_sexo = codigo_sexo;
        this.nome = nome;
         this.idTipousuario = idTipousuario;
        this.senha = senha;
        this.status = status;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.userName = userName;
    }

    public UsuarioModelo(int codigo, int codigo_sexo, int idTipousuario, String nome, String senha, String status, String dataNascimento, String telefone, String email, String endereco, String userName) {
        this.codigo = codigo;
        this.codigo_sexo = codigo_sexo;
        this.idTipousuario = idTipousuario;
        this.nome = nome;
        this.senha = senha;
        this.status = status;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.userName = userName;
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
     * @return the codigo_sexo
     */
    public int getCodigo_sexo() {
        return codigo_sexo;
    }

    /**
     * @param codigo_sexo the codigo_sexo to set
     */
    public void setCodigo_sexo(int codigo_sexo) {
        this.codigo_sexo = codigo_sexo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
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
     * @return the dataNascimento
     */
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the idTipousuario
     */
    public int getIdTipousuario() {
        return idTipousuario;
    }

    /**
     * @param idTipousuario the idTipousuario to set
     */
    public void setIdTipousuario(int idTipousuario) {
        this.idTipousuario = idTipousuario;
    }
    
    
    
    
    
}
