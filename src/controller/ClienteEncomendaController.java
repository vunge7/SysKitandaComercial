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
import modelo.ClienteEncomendaModelo;

import util.BDConexao;

/*----------------------------------------------
 *project: GSHOLL
 *file:	TurmaController.java
 *Desenvolvidor e Analista: Domingos Dala Vunge
 *----------------------------------------------*/
public class ClienteEncomendaController {
    
     private static String
             
                          /*QUERYS PRINCIPAIS*/
             
                          insert = "INSERT INTO tb_clientes_encomenda ( nome_cliente , telefone , endereco , saldo, email, credito )  "
                                                            + " VALUES (?, ?, ?, ?, ?, ?)", 
            
                          update = "UPDATE tb_clientes_encomenda SET nome_cliente = ? , telefone = ? , endereco = ? , saldo = ? , email = ?, credito = ? WHERE idCliente = ?",
            
                          //actualiza o status do aluno para eliminado
                          delete = "DELETE FROM tb_clientes_encomenda WHERE idCliente = ?",
            
                          getAll = "SELECT * FROM tb_clientes_encomenda",
                          
                          /* OUTRAS QUERYS */
                          getClienteByIdCliente = "SELECT * FROM tb_clientes_encomenda  WHERE idCliente = ?",
                          getIdClienteBynome_cliente = "SELECT idCliente FROM tb_clientes_encomenda  WHERE nome_cliente = ?";
                     

    
    private ClienteEncomendaModelo clienteEncomendaModelo = null;
    private PreparedStatement comando = null;
//    private Connection conexao = null;
    private final BDConexao conexao = BDConexao.getInstancia();


    public ClienteEncomendaController() {
        
    }
    
    public boolean guardar(ClienteEncomendaModelo clienteEncomendaModelo) {
    this.clienteEncomendaModelo = clienteEncomendaModelo;

    String sql = "INSERT INTO cliente_encomenda (nome_cliente, telefone, endereco, saldo, email, credito) VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement comando = BDConexao.getInstancia().getConnectionAtiva().prepareStatement(sql)) {
        comando.setString(1, clienteEncomendaModelo.getNome_cliente());
        comando.setString(2, clienteEncomendaModelo.getTelefone());
        comando.setString(3, clienteEncomendaModelo.getEndereco());
        comando.setDouble(4, clienteEncomendaModelo.getSaldo());
        comando.setString(5, clienteEncomendaModelo.getEmail());
        comando.setDouble(6, clienteEncomendaModelo.getCredito());

        comando.executeUpdate();
        return true;

    } catch (SQLException ex) {
        Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, "Erro ao guardar cliente", ex);
        return false;
    }
}

    
    public boolean alterar(ClienteEncomendaModelo clienteEncomendaModelo) {
    this.clienteEncomendaModelo = clienteEncomendaModelo;

    String sql = "UPDATE cliente_encomenda SET nome_cliente=?, telefone=?, endereco=?, saldo=?, email=?, credito=? WHERE idCliente=?";

    try (PreparedStatement comando = BDConexao.getInstancia().getConnectionAtiva().prepareStatement(sql)) {
        comando.setString(1, clienteEncomendaModelo.getNome_cliente());
        comando.setString(2, clienteEncomendaModelo.getTelefone());
        comando.setString(3, clienteEncomendaModelo.getEndereco());
        comando.setDouble(4, clienteEncomendaModelo.getSaldo());
        comando.setString(5, clienteEncomendaModelo.getEmail());
        comando.setDouble(6, clienteEncomendaModelo.getCredito());
        comando.setInt(7, clienteEncomendaModelo.getIdCliente());

        comando.executeUpdate();
        return true;

    } catch (SQLException ex) {
        Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, "Erro ao alterar cliente", ex);
        return false;
    }
}

    
    public boolean eliminar(ClienteEncomendaModelo clienteEncomendaModelo) {
    this.clienteEncomendaModelo = clienteEncomendaModelo;

    String sql = "DELETE FROM cliente_encomenda WHERE idCliente=?";

    try (PreparedStatement comando = BDConexao.getInstancia().getConnectionAtiva().prepareStatement(sql)) {
        comando.setInt(1, clienteEncomendaModelo.getIdCliente());
        comando.executeUpdate();
        return true;

    } catch (SQLException ex) {
        Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, "Erro ao eliminar cliente", ex);
        return false;
    }
}


//     public boolean guardar(ClienteEncomendaModelo clienteEncomendaModelo) {
//       
//        this.clienteEncomendaModelo =  clienteEncomendaModelo;
// 
//        try {
//            
//            conexao = conexao.getConnectionAtiva();
//            System.out.println(insert);
//            comando = conexao.prepareStatement(insert);
//
//               /*       
//            nome_cliente , telefone , endereco , saldo, email 
//                */      
//
//            comando.setString(1, clienteEncomendaModelo.getNome_cliente());
//            comando.setString(2, clienteEncomendaModelo.getTelefone());
//            comando.setString(3, clienteEncomendaModelo.getEndereco());
//            comando.setDouble(4, clienteEncomendaModelo.getSaldo());
//            comando.setString(5, clienteEncomendaModelo.getEmail());
//            comando.setDouble(6, clienteEncomendaModelo.getCredito());
//            
//            
//            comando.execute();
//            conexao.close();
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//
//     return true;
//     
//    }
//    
//    
//      public boolean alterar(ClienteEncomendaModelo clienteEncomendaModelo) {
//       
//        this.clienteEncomendaModelo = clienteEncomendaModelo;
// 
//        try {
//            
//            conexao = conexao.getConnectionAtiva();
//            comando = conexao.prepareStatement(update);
//
//            /*       
//             descrisao , idTurno , idCurso , idSala , valor_propina
//                */      
//
//            comando.setString(1, clienteEncomendaModelo.getNome_cliente());
//            comando.setString(2, clienteEncomendaModelo.getTelefone());
//            comando.setString(3, clienteEncomendaModelo.getEndereco());
//            comando.setDouble(4, clienteEncomendaModelo.getSaldo());
//            comando.setString(5, clienteEncomendaModelo.getEmail());
//            comando.setDouble(6, clienteEncomendaModelo.getCredito());
//            
//            comando.setInt(7, clienteEncomendaModelo.getIdCliente());
//            
//            comando.execute();
//            conexao.close();
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//
//     return true;
//     
//    }
//    
//       public boolean eliminar(ClienteEncomendaModelo clienteEncomendaModelo) {
//
//        this.clienteEncomendaModelo = clienteEncomendaModelo;
//       
//        try {
//            conexao = conexao.getConnectionAtiva();
//            comando = conexao.prepareStatement(delete);
//            comando.setInt(1, clienteEncomendaModelo.getIdCliente());
//            comando.execute();
//            conexao.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//        
//        return true;
//    }
 
   private void preencher_objecto(ResultSet resultado) throws SQLException
   {
    
                clienteEncomendaModelo = new ClienteEncomendaModelo();
            
                
                System.out.println("ID CLIENTE "   +resultado.getInt(1) );
                System.out.println("NOME DO CLIENTE "   +resultado.getString(2) );
                System.out.println("TELEFONE "   +resultado.getString(3) );
                System.out.println("ENDERECO "   +resultado.getString(4) );
             
              
                
                clienteEncomendaModelo.setIdCliente(resultado.getInt(1) );
                clienteEncomendaModelo.setNome_cliente(resultado.getString(2) );
                clienteEncomendaModelo.setTelefone(resultado.getString(3) );
                clienteEncomendaModelo.setEndereco(resultado.getString(4) );
                clienteEncomendaModelo.setSaldo (resultado.getDouble(5) );
                clienteEncomendaModelo.setEmail(resultado.getString(6) );
                clienteEncomendaModelo.setCredito( resultado.getDouble(7) );
                
                System.out.println("SALDO  "   +clienteEncomendaModelo.getSaldo() );
                System.out.println("CREDITO  "   +clienteEncomendaModelo.getCredito() );
                System.out.println("EMAIL "   +resultado.getString(6) );
    }
       
    //Retorna o Objecto por Numero do Processo que neste caso e o codigo
public ClienteEncomendaModelo getClienteByCodigo(int codigo) {
    ResultSet resultado = null;
    PreparedStatement comando = null;

    try {
        // Usa a conexão activa (sem criar nova)
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(getClienteByIdCliente);
        comando.setInt(1, codigo);
        resultado = comando.executeQuery();

        if (resultado.next()) {
            preencher_objecto(resultado);
        }

    } catch (SQLException ex) {
        Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            // Fecha apenas recursos auxiliares, não a conexão activa
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return clienteEncomendaModelo;
}

    

    
      //Retorna o Objecto por Numero do Processo que neste caso e o codigo
    public Integer getCodigoByNome(String nome_cliente) {
    Integer codigo = 0;
    PreparedStatement comando = null;
    ResultSet resultado = null;

    try {
        Connection conn = conexao.getConnectionAtiva();



        comando = conn.prepareStatement(getIdClienteBynome_cliente);
        comando.setString(1, nome_cliente);
        resultado = comando.executeQuery();

        if (resultado.next()) {
            codigo = resultado.getInt(1);
        }

    } catch (SQLException ex) {
        Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(ClienteEncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return codigo;
}


    
    
 

    
    

}
