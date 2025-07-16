/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.math.MathContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.DadosInstituicaoModelo;

import util.BDConexao;


/**
 *
 * @author Domingos Dala Vunge
 */
public class DadoInstituicaoController {
    
   // (nome_escola, telefone1, telefone2, conta_bacaria_bfa1, conta_bacaria_bfa2, conta_bacaria_bic1,conta_bacaria_bic2,endereco)

  
    private static String insert = "INSERT INTO tb_dados_instituicao (nome, telefone, enderecos, email, nif) VALUES (?,?,?,?,?)", 
                          update = "UPDATE tb_dados_instituicao SET nome = ?, telefone =? ,  enderecos  = ?,   email = ?,  nif = ?   WHERE idDadosInsitiuicao = ?",
                          delete = "DELETE FROM tb_dados_instituicao  WHERE idDadosInsitiuicao = ?",
                          getAll = "SELECT * FROM tb_dados_instituicao",
                          getAllDesignacao = "SELECT * FROM tb_dados_instituicao",
                          getDadosInstituicaoByidDadosInstituicao = "SELECT * FROM tb_dados_instituicao WHERE idDadosInsitiuicao = ? " ;
    
    private DadosInstituicaoModelo dadosInstituicaoModelo = null;
    private PreparedStatement comando = null;
    private Connection conexao = null;
    private Vector<DadosInstituicaoModelo> vectorDadosInstituicaoModelo = new Vector();
    
    
    public DadoInstituicaoController() {
    }
    
  
    public boolean guardar(DadosInstituicaoModelo dadosInstituicaoModelo) {
       
        this.dadosInstituicaoModelo = dadosInstituicaoModelo;
 
        try {
            
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement(insert);
            
            //nome, telefone, enderecos, email, nif
            comando.setString(1, this.dadosInstituicaoModelo .getNome());
            comando.setString(2, this.dadosInstituicaoModelo .getTelefone());
            comando.setString(3, this.dadosInstituicaoModelo .getEnderecos());
            comando.setString(4, this.dadosInstituicaoModelo .getEmail());
            comando.setString(5, this.dadosInstituicaoModelo .getNif());
            
        
            
           // comando.setString(5, this.dadosInstituicaoModelo .getDescricao());
            
            
            comando.execute();
            conexao.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

  
    public boolean alterar(DadosInstituicaoModelo dadosInstituicaoModelo) {
        
         this.dadosInstituicaoModelo = dadosInstituicaoModelo;
      
        try {
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement(update);
            
            comando.setString(1, this.dadosInstituicaoModelo .getNome());
            comando.setString(2, this.dadosInstituicaoModelo .getTelefone());
            comando.setString(3, this.dadosInstituicaoModelo .getEnderecos());
            comando.setString(4, this.dadosInstituicaoModelo .getEmail());
            comando.setString(5, this.dadosInstituicaoModelo.getNif());
      
            comando.setInt(6,this.dadosInstituicaoModelo.getIdDadosInstituicaoModelo());
            comando.execute();
            conexao.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
       
        return true;
    }
    

    public boolean eliminar(DadosInstituicaoModelo dadosInstituicaoModelo) {

      this.dadosInstituicaoModelo = dadosInstituicaoModelo;
       
        try {
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement(delete);
            comando.setInt(1, dadosInstituicaoModelo.getIdDadosInstituicaoModelo());
           
            comando.execute();
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    
    
    public Vector<DadosInstituicaoModelo> listar() 
    {
        ResultSet resultado = null;
       
        try {
            
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement(getAll);
            
            resultado = comando.executeQuery();

            while (resultado.next()) {
                
                dadosInstituicaoModelo = new DadosInstituicaoModelo();
            
                
                
                
                vectorDadosInstituicaoModelo.add(dadosInstituicaoModelo);
            }
            conexao.close();

        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        return vectorDadosInstituicaoModelo;
    }
  
    
     public Vector<DadosInstituicaoModelo> listarAllDesignacao() 
    {
        ResultSet resultado = null;
       
        try {
            
            conexao = BDConexao.getConnection();
            comando = conexao.prepareStatement(getAllDesignacao);
            
            resultado = comando.executeQuery();
   
            while (resultado.next()) {
                
                dadosInstituicaoModelo = new DadosInstituicaoModelo();
                dadosInstituicaoModelo.setNome(resultado.getString(2) );
                dadosInstituicaoModelo.setTelefone(resultado.getString(3) );
                dadosInstituicaoModelo.setEnderecos(resultado.getString(4) );
                dadosInstituicaoModelo.setEmail(resultado.getString(5) );
                dadosInstituicaoModelo.setNif(resultado.getString(6) );
                
                vectorDadosInstituicaoModelo.add(dadosInstituicaoModelo);
            }
            conexao.close();

        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        return vectorDadosInstituicaoModelo;
    }
  
 //Retorna o Objecto por Numero do Processo que neste caso e o codigo
    public DadosInstituicaoModelo getDadosInstituicaoByCodigo(int codigo) 
    {
        ResultSet resultado = null;

        try {
            
             conexao = BDConexao.getConnection();
             comando = conexao.prepareStatement(getDadosInstituicaoByidDadosInstituicao);
             comando.setInt(1, codigo);
             resultado = comando.executeQuery();

            if (resultado.next()) {
                preencher_objecto(resultado);
            }
            conexao.close();

        } catch (SQLException ex) {
            Logger.getLogger(DadoInstituicaoController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        return  dadosInstituicaoModelo;
    }
    
    
     private void preencher_objecto(ResultSet resultado) throws SQLException{
    
                dadosInstituicaoModelo = new DadosInstituicaoModelo();
            
                dadosInstituicaoModelo.setIdDadosInstituicaoModelo(resultado.getInt(1) );
                 dadosInstituicaoModelo.setNome(resultado.getString(2) );
                dadosInstituicaoModelo.setTelefone(resultado.getString(3) );
                dadosInstituicaoModelo.setEnderecos(resultado.getString(4) );
                dadosInstituicaoModelo.setEmail(resultado.getString(5) );
                dadosInstituicaoModelo.setNif(resultado.getString(6) );
    }
       
    
     
     public static void main(String[] args) {
         
//        DadosInstituicaoModelo dadosInstituicaoModelo = new DadosInstituicaoModelo();
//        dadosInstituicaoModelo.setNomeEscola("janeiro");
//        DadoInstituicaoController dadoInstituicaoController = new DadoInstituicaoController();
//        
//         if (dadoInstituicaoController.guardar(dadosInstituicaoModelo)) {
//             JOptionPane.showMessageDialog(null, "Dados salvos com sucesso");
//         } else {
//             JOptionPane.showMessageDialog(null, "erro ao salvar os dados");
//         }
//    }
//     
     }
}

