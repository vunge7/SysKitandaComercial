/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.AnoEconomico;
import entity.Caixa;
import entity.Cambio;
import entity.Documento;
import entity.FormaPagamento;
import entity.ItemCaixa;
import entity.TbArmazem;
import entity.TbBanco;
import entity.TbCliente;
import entity.TbItemVenda;
import entity.TbLugares;
import entity.TbMesas;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbVenda;
import entity.TbUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class ItemCaixaController implements EntidadeFactory
{

    private BDConexao conexao;

    public ItemCaixaController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar(Object object) {
    ItemCaixa itemcaixa = (ItemCaixa) object;
    
    String INSERT = "INSERT INTO item_caixa (valor_declarado, valor_real, fk_caixa, fk_forma_pagamento) " +
                    "VALUES (?, ?, ?, ?)";

    try {
        PreparedStatement ps = conexao.getConnection1().prepareStatement(INSERT);
        ps.setDouble(1, itemcaixa.getValorDeclarado());
        ps.setDouble(2, itemcaixa.getValorReal());
        ps.setInt(3, itemcaixa.getFkCaixa().getPkCaixa());
        ps.setInt(4, itemcaixa.getFkFormaPagamento().getPkFormaPagamento());

        int result = ps.executeUpdate();

        System.out.println("ItemCaixa salvo com sucesso. Result: " + result);
        return result > 0;
    } catch (SQLException e) {
        System.err.println("Erro ao salvar ItemCaixa: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

//    public boolean salvar( Object object )
//    {
//        ItemCaixa itemcaixa = ( ItemCaixa ) object;
//        String INSERT = "INSERT INTO item_caixa( valor_declarado , valor_real , fk_caixa , fk_forma_pagamento "
//                + ")"
//                + " VALUES("
//                + itemcaixa.getValorDeclarado() + " , "
//                + itemcaixa.getValorReal() + " , "
//                + itemcaixa.getFkCaixa().getPkCaixa() + " , "
//                + itemcaixa.getFkFormaPagamento().getPkFormaPagamento()
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    @Override
    public boolean actualizar( Object object )
    {
        ItemCaixa itemcaixa = ( ItemCaixa ) object;
        String query = "UPDATE item_caixa SET item =  ";
              
        return conexao.executeUpdate( query );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM item_caixa WHERE pk_item_caixa = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ItemCaixa> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM item_caixa ORDER BY pk_item_caixa ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ItemCaixa> lista_item_caixa = new ArrayList<>();
        ItemCaixa item_caixa;
        try
        {

            while ( result.next() )
            {
                item_caixa = new ItemCaixa();
                item_caixa.setPkItemCaixa(result.getInt( "pk_item_caixa" ) );
                item_caixa.setValorDeclarado(result.getDouble( "valor_declarado" ) );
                item_caixa.setValorReal(result.getDouble( "valor_real" ) );
                item_caixa.setFkCaixa(new Caixa( result.getInt( "fk_caixa" ) ) );
                item_caixa.setFkFormaPagamento(new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );

                lista_item_caixa.add( item_caixa );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_item_caixa;
    }

    public boolean existeItemByCodCaixa( int pk_item_caixa )
    {

        String FIND_ALL = "SELECT * FROM item_caixa where pk_item_caixa = " + pk_item_caixa;
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ItemCaixa> lista_item_venda = new ArrayList<>();
        try
        {
            if ( result.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM item_caixa WHERE pk_item_caixa = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ItemCaixa item_caixa = null;
        try
        {

            if ( result.next() )
            {
                item_caixa = new ItemCaixa();
                item_caixa.setPkItemCaixa(result.getInt( "pk_item_caixa" ) );
                item_caixa.setValorDeclarado(result.getDouble( "valor_declarado" ) );
                item_caixa.setValorReal(result.getDouble( "valor_real" ) );
                item_caixa.setFkCaixa(new Caixa( result.getInt( "fk_caixa" ) ) );
                item_caixa.setFkFormaPagamento(new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );


            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_caixa;

    }
    
    

    public ItemCaixa getLastItem()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, i.*  FROM item_caixa i";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        ItemCaixa item_caixa = null;
        try
        {
            if ( result.next() )
            {
                item_caixa = new ItemCaixa();
                item_caixa.setPkItemCaixa(result.getInt( "maximo_id" ) );
                item_caixa.setValorDeclarado(result.getDouble( "valor_declarado" ) );
                item_caixa.setValorReal(result.getDouble( "valor_real" ) );
                item_caixa.setFkCaixa(new Caixa( result.getInt( "fk_caixa" ) ) );
                item_caixa.setFkFormaPagamento(new FormaPagamento( result.getInt( "fk_forma_pagamento" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return item_caixa;

    }


}
