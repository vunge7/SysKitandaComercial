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
import modelo.ItemEncomendaModelo;

import util.BDConexao;

/*----------------------------------------------
 *project: GSHOLL
 *file:	ItemEncomendaController.java
 *Desenvolvidor e Analista: Domingos Dala Vunge
 *----------------------------------------------*/
public class ItemEncomendaController {
    
     private static String
             
                          /*QUERYS PRINCIPAIS*/
             
                          insert = "INSERT INTO tb_item_encomenda ( idEncomenda , idProduto , total , quantidade )  "
                                                            + " VALUES (?, ?, ?, ?)", 
            
                          //actualiza o status do aluno para eliminado
                          delete = "DELETE FROM tb_item_encomenda WHERE idCliente = ?",
            
                          getAll = "SELECT * FROM tb_item_encomenda",
                          
                          /* OUTRAS QUERYS */
                          getItemEcomendaByIdEncomenda = "SELECT * FROM tb_item_encomenda  WHERE idEncomenda = ?",
                          getIdClienteBynome_cliente = "SELECT idCliente FROM tb_clientes_encomenda  WHERE nome_cliente = ?";
                     

    
    private ItemEncomendaModelo itemEncomendaModelo = null;
    private PreparedStatement comando = null;
    private BDConexao conexao = BDConexao.getInstancia();
//    private Connection conexao = null;

    public ItemEncomendaController() {
        
    }

     public boolean guardar(ItemEncomendaModelo itemEncomendaModelo) {
    this.itemEncomendaModelo = itemEncomendaModelo;
    PreparedStatement comando = null;

    try {
        // Usa a conexão activa (não cria nova)
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(insert);

        /*
            idEncomenda , idProduto , total , quantidade
        */
        comando.setInt(1, itemEncomendaModelo.getIdEncomenda());
        comando.setInt(2, itemEncomendaModelo.getIdPrdouto());
        comando.setDouble(3, itemEncomendaModelo.getTotal());
        comando.setInt(4, itemEncomendaModelo.getQauntidade());

        comando.execute();

        System.out.println("Item da encomenda salvo com sucesso.");

        return true;
    } catch (SQLException ex) {
        Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    } finally {
        // Fecha apenas o PreparedStatement (não fecha a conexão activa)
        try {
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

    
    
   private void preencher_objecto(ResultSet resultado) throws SQLException
   {
    
                itemEncomendaModelo = new ItemEncomendaModelo();
                  //idEncomenda , idProduto , total , quantidade
                itemEncomendaModelo.setIdEncomenda(resultado.getInt(1) );
                itemEncomendaModelo.setIdPrdouto(resultado.getInt(2) );
                itemEncomendaModelo.setTotal(resultado.getLong(3) );
                itemEncomendaModelo.setQauntidade(resultado.getInt(4) );
                
   }
       
    //Retorna o Objecto por Numero do Processo que neste caso e o codigo
   public ItemEncomendaModelo getItemEcomendaByIdEncomenda(int codigo) {
    ResultSet resultado = null;
    PreparedStatement comando = null;

    try {
        // Usa a conexão activa (sem abrir nova)
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(getItemEcomendaByIdEncomenda);
        comando.setInt(1, codigo);
        resultado = comando.executeQuery();

        if (resultado.next()) {
            preencher_objecto(resultado);
        }

    } catch (SQLException ex) {
        Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return itemEncomendaModelo;
}

    

    
    
     //Retorna o Objecto por Numero do Processo que neste caso e o codigo
    public Vector<ItemEncomendaModelo> getAllItemEncomenda(int codigo) {
    ResultSet resultado = null;
    PreparedStatement comando = null;
    Vector<ItemEncomendaModelo> vector = new Vector<>();

    try {
        // Usa a conexão activa (sem abrir nova)
        Connection conn = conexao.getConnectionAtiva();

        comando = conn.prepareStatement(getItemEcomendaByIdEncomenda);
        comando.setInt(1, codigo);
        resultado = comando.executeQuery();

        while (resultado.next()) {
            preencher_objecto(resultado);
            vector.add(itemEncomendaModelo);
        }

    } catch (SQLException ex) {
        Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (resultado != null) resultado.close();
            if (comando != null) comando.close();
        } catch (SQLException e) {
            Logger.getLogger(ItemEncomendaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return vector;
}

    
    

}
