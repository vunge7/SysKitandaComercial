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
    private BDConexao conexao = BDConexao.getInstancia();

//    private Connection conexao = null;

    public EncomendaController() {
        
    }

     public boolean guardar(EncomendaModelo encomendaModelo) {
    this.encomendaModelo = encomendaModelo;
    PreparedStatement comando = null;

    try {
        // Usa a conexão activa
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(insert);
        System.out.println(insert);

        /* Campos:
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
        return true;

    } catch (SQLException ex) {
        Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
        return false;

    } finally {
        try {
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}


public boolean alterar(EncomendaModelo encomendaModelo) {
    this.encomendaModelo = encomendaModelo;
    PreparedStatement comando = null;

    try {
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(update);

        comando.setString(1, encomendaModelo.getData_encomenda());
        comando.setString(2, encomendaModelo.getData_entrega_prevista());
        comando.setInt(3, encomendaModelo.getIdCliente());
        comando.setDouble(4, encomendaModelo.getTotal_encomenda());
        comando.setBoolean(5, encomendaModelo.isStatus_entrega());
        comando.setInt(6, encomendaModelo.getIdUsuario());
        comando.setString(7, encomendaModelo.getObs());
        comando.setInt(8, encomendaModelo.getIdEncomenda());

        comando.execute();
        return true;

    } catch (SQLException ex) {
        Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
        return false;

    } finally {
        try {
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}


public boolean eliminar(EncomendaModelo encomendaModelo) {
    this.encomendaModelo = encomendaModelo;
    PreparedStatement comando = null;

    try {
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(delete);
        comando.setInt(1, encomendaModelo.getIdEncomenda());
        comando.execute();

        return true;

    } catch (SQLException ex) {
        Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
        return false;

    } finally {
        try {
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
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
public EncomendaModelo getEncomendaByCodigo(int codigo) {
    PreparedStatement comando = null;
    ResultSet resultado = null;

    try {
        // Usa a conexão ativa (não abre nova)
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(getEncomendaByIdEncomenda);
        comando.setInt(1, codigo);
        resultado = comando.executeQuery();

        if (resultado.next()) {
            preencher_objecto(resultado);
        }

    } catch (SQLException ex) {
        Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        // Fecha apenas os recursos auxiliares, não a conexão ativa
        try {
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return encomendaModelo;
}

    

    
      //Retorna o Objecto por Numero do Processo que neste caso e o codigo
public Integer getCodigoByNome(String nome_cliente) {
    Integer codigo = 0;
    PreparedStatement comando = null;
    ResultSet resultado = null;

    try {
        // Usa a conexão ativa sem abrir nova
        Connection conn = conexao.getConnectionAtiva();

        // Consulta que retorna o ID da encomenda com base no nome do cliente
        String sql = "SELECT idEncomenda FROM tb_encomenda e "
                   + "INNER JOIN tb_cliente c ON e.idCliente = c.idCliente "
                   + "WHERE c.nome_cliente = ?";

        comando = conn.prepareStatement(sql);
        comando.setString(1, nome_cliente);
        resultado = comando.executeQuery();

        if (resultado.next()) {
            codigo = resultado.getInt(1);
        }

    } catch (SQLException ex) {
        Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(EncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return codigo;
}

    
    
 

    
    

}
