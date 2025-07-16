/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.VendaModelo;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class VendaController {
    
    private BDConexao conexao;
    private StringBuffer sql;

    public VendaController(BDConexao conexao) {
        this.conexao = conexao;
    }
    
    
    public boolean operacao (int operacao,VendaModelo vendaModelo, boolean performance, boolean credito)
    {    
        
        sql = new StringBuffer();

        
        sql.append(" CALL DM_PROCEDIMENTO_VENDA('");
        sql.append(vendaModelo.getDataVenda() );
        sql.append("','");
        
         sql.append(vendaModelo.getHora() );
        sql.append("',");
       
        sql.append(vendaModelo.getCodigo());
        sql.append(",");
        sql.append(vendaModelo.getCodigo_usuario());
        sql.append(",");
        sql.append(vendaModelo.getCodigo_cliente());
        sql.append(",'");
        sql.append( vendaModelo.getNome_cliente() );
        sql.append("',");
        sql.append(vendaModelo.getTotal_venda());
        sql.append(",'");
        
        sql.append( isPefromance(performance) );
        sql.append("','");
        
        sql.append( isCredito(credito) );
        sql.append("',");
        
        
        sql.append( vendaModelo.getValor_entregue());
        sql.append(",");
        
        sql.append( vendaModelo.getTroco());
        sql.append(",");

        
        sql.append( vendaModelo.getIdArmazemFK() );
        sql.append(",");
        
        sql.append(operacao);
        sql.append(")");

        System.out.println(sql);

        return conexao.executeUpdate(sql.toString());
       
    }
    
    
    
    
    public String isPefromance(boolean performance)
    {
            if(performance)
                    return "true";
            return "false";
    }
    
     
    public String isCredito(boolean credito)
    {
            if(credito)
                    return "true";
            return "false";
    }
    
    public VendaModelo getVenda (int codigo) throws SQLException
    {   
      
        VendaModelo vendaModelo = new VendaModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append("CALL DM_PROCEDIMENTO_GET_VENDA(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery( sql.toString() );

        if (rs.next()) {
            
            vendaModelo.setCodigo( rs.getInt("codigo") );
            vendaModelo.setDataVenda( rs.getDate("dataVenda") );
           vendaModelo.setHora( rs.getTime("hora") );
            vendaModelo.setCodigo_usuario( rs.getInt("codigo_usuario") );
            vendaModelo.setCodigo_cliente( rs.getInt("codigo_cliente") );
            vendaModelo.setTotal_venda( rs.getInt("total_venda") );
            vendaModelo.setValor_entregue( rs.getInt("valor_entregue") );
            vendaModelo.setIdArmazemFK( rs.getInt("idArmazemFK") );
            vendaModelo.setPerformance( rs.getString("performance") );
                      
        }
        return  vendaModelo;
    }
    //
    
    
     public VendaModelo getVenda_Reeprimir (int codigo) throws SQLException
    {   
      
        VendaModelo vendaModelo = new VendaModelo();
        ResultSet rs = null;
      
        sql = new StringBuffer();
        sql.append("CALL DM_PROCEDIMENTO_GET_VENDA_REEPRIMIR(");
        sql.append(codigo);
        sql.append(")");
        
        System.out.println(sql);

        rs = conexao.executeQuery(sql.toString());

        if (rs.next()) {
           vendaModelo.setCodigo( rs.getInt("codigo"));
            vendaModelo.setDataVenda( rs.getDate("dataVenda") );
            vendaModelo.setHora ( rs.getTime("hora") );
            vendaModelo.setCodigo_usuario( rs.getInt("codigo_usuario") );
            vendaModelo.setCodigo_cliente( rs.getInt("codigo_cliente") );
            vendaModelo.setTotal_venda( rs.getInt("total_venda") );
            vendaModelo.setValor_entregue( rs.getInt("valor_entregue") );
                      
        }
        return  vendaModelo;
    }
    
    public boolean update_credito (int cod_venda, String credito)
    {    
        
        String sql = "";
        
        sql = "UPDATE tb_venda SET credito = '" +credito +"' WHERE codigo = " +cod_venda;
        
        System.out.println(sql);

        return conexao.executeUpdate(sql);
       
    }
    
    
    
}
