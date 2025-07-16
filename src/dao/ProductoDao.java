/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Productos;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ProductoDao  {
    
    
    
    
    
    
    public static boolean insert(Productos productos, BDConexao conexao)
    {
        String sql = "INSERT INTO productos(consumo, preco, lugar, qtd, mesa, usuario, data_hora )"
                + " VALUES('" +   productos.getConsumo()  + "', " + productos.getPreco() + ", '" + productos.getLugar()    + "',"
                + productos.getQtd() + ", '" + productos.getMesa() + "', '" + productos.getUsuario() + "', '" + MetodosUtil.getDataBancoFull(productos.getDataHora()) +"'"
                + ")";
        return conexao.executeUpdate(sql);
    }
            
    
}
