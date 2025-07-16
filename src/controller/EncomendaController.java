/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.EncomendaModelo;

import util.BDConexao;

/*----------------------------------------------
 *project: GPRODUT
 *file:	EncomendaController.java
 *Desenvolvidor e Analista: Domingos Dala Vunge
 *----------------------------------------------*/
public class EncomendaController {
    
     private static String
             
                          /*QUERYS PRINCIPAIS*/
             
                          insert = "INSERT INTO tb_encomenda ( data_encomenda , data_entrega_prevista , idCliente , total_encomenda, status_entrega, idUsuario, obs )  "
                                                            + " VALUES (?, ?, ?, ?, ?, ?, ?)", 
            
                          update = "UPDATE tb_encomenda SET data_encomenda = ? , data_entrega_prevista = ? , idCliente = ? , total_encomenda = ? , status_entrega = ?, idUsuario = ?, obs = ? WHERE idEncomenda = ?",
            
                          //actualiza o status do aluno para eliminado
                          delete = "DELETE FROM tb_encomenda WHERE idEncomenda = ?",
            
                          getAll = "SELECT * FROM tb_encomenda",
                          
                          /* OUTRAS QUERYS */
                          getEncomendaByIdEncomenda = "SELECT * FROM tb_encomenda  WHERE idEncomenda = ?";
                         
                     

    
    private EncomendaModelo encomendaModelo = null;
    private PreparedStatement comando = null;
    private Connection conexao = null;

    public EncomendaController() {
        
    }

     public boolean guardar(EncomendaModelo encomendaModelo) {
       
        this.encomendaModelo =  encomendaModelo;
 
        try {
            
            conexao = BDConexao.conectar();
            System.out.println(insert);
            comando = conexao.prepareStatement(insert);

               /*       
           data_encomenda , data_entrega_prevista , idCliente , total_encomenda, status_entrega
                */      

            comando.setString(1, encomendaModelo.getData_encomenda());
            comando.setString(2, encomendaModelo.getData_entrega_prevista());
            comando.setInt(3, encomendaModelo.getIdCliente());
            comando.setDouble(4, encomendaModelo.getTotal_encomenda());
            comando.setBoolean(5, encomendaModelo.isStatus_entrega());
            comando.setInt(6, encomendaModelo.getIdUsuario());
            comando.setString(7, encomendaModelo.getObs());
            comando.execute();
            conexao.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

     return true;
     
    }
    
    
      public boolean alterar(EncomendaModelo encomendaModelo) {
       
        this.encomendaModelo = encomendaModelo;
 
        try {
            
            conexao = BDConexao.conectar();
            comando = conexao.prepareStatement(update);

            /*       
             descrisao , idTurno , idCurso , idSala , valor_propina
                */      

            comando.setString(1, encomendaModelo.getData_encomenda());
            comando.setString(2, encomendaModelo.getData_entrega_prevista());
            comando.setInt(3, encomendaModelo.getIdCliente());
            comando.setDouble(4, encomendaModelo.getTotal_encomenda());
            comando.setBoolean(5, encomendaModelo.isStatus_entrega());
            comando.setInt(6, encomendaModelo.getIdUsuario());
            comando.setString(7, encomendaModelo.getObs());
            comando.setInt(8, encomendaModelo.getIdEncomenda());
            
            comando.execute();
            conexao.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

     return true;
     
    }
    
       public boolean eliminar(EncomendaModelo encomendaModelo) {

        this.encomendaModelo = encomendaModelo;
       
        try {
            conexao = BDConexao.conectar();
            comando = conexao.prepareStatement(delete);
            comando.setInt(1, encomendaModelo.getIdEncomenda());
            comando.execute();
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
 
   private void preencher_objecto(ResultSet resultado) throws SQLException
   {
    
                encomendaModelo = new EncomendaModelo();
             // data_encomenda , data_entrega_prevista , idCliente , total_encomenda, status_entrega
                encomendaModelo.setIdEncomenda(resultado.getInt(1) );
                encomendaModelo.setData_encomenda(resultado.getString(2) );
                encomendaModelo.setData_entrega_prevista(resultado.getString(3) );
                encomendaModelo.setIdCliente(resultado.getInt(4) );
                encomendaModelo.setTotal_encomenda(resultado.getLong(5) );
                encomendaModelo.setStatus_entrega( resultado.getBoolean(6) );
                encomendaModelo.setIdUsuario(resultado.getInt(6) );
    }
       
    //Retorna o Objecto por Numero do Processo que neste caso e o codigo
    public EncomendaModelo getEncomendaByCodigo(int codigo) 
    {
        ResultSet resultado = null;

        try {
            
             conexao = BDConexao.conectar();
             comando = conexao.prepareStatement(getEncomendaByIdEncomenda);
             comando.setInt(1, codigo);
             resultado = comando.executeQuery();

            if (resultado.next()) {
                preencher_objecto(resultado);
            }
            conexao.close();

        } catch (SQLException ex) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        return encomendaModelo;
    }
    

    
      //Retorna o Objecto por Numero do Processo que neste caso e o codigo
    public Integer getCodigoByNome(String nome_cliente) 
    {
        ResultSet resultado = null;

        Integer codigo = 0;
        try {
            
             conexao = BDConexao.conectar();
             comando = conexao.prepareStatement("");
             comando.setString(1, nome_cliente);
             resultado = comando.executeQuery();

            if (resultado.next()) {
                
                codigo =  resultado.getInt(1);
            }
            conexao.close();

        } catch (SQLException ex) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
            return codigo;
        }
        
        return codigo;
    }
    
    
 

    
    

}
