/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.sql.Connection;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
interface  EntidadeFactory
{
    
    public boolean salvar(Object object);
    public boolean actualizar(Object object);
    public boolean eliminar(int codigo);
    public Object findById(int codigo);
    public  List<?> listarTodos();
    public Vector<String> getVector();
    
}
