/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Domingos Dala Vunge
 */
public class Definicoes
{

    public static int[] PRODUTO =
    {
        144, 238, 144

    };
    public static int[] STOCK_VAZIO =
    {
        255, 180, 180
    };

    public static int[] SERVICOS =
    {
        255, 255, 255
    };

    public static int[] PRODUTO_CRITICO =
    {
        255, 239, 154
    };

    public static int[] COR_PRETA =
    {
        0, 0, 0
    };

    public static int[] COR_HORA =
    {
        220, 220, 220
    };
    public static int[] COR_BRANCA =
    {
        255, 255, 255
    };
    private BDConexao conexao;

    public Definicoes()
    {
        conexao = BDConexao.getInstancia();
    }

    public String getIpServido()
    {
        String sql = "select Designacao from tb_parametros where(codigo = 1)";
        ResultSet rs = conexao.executeQuery( sql );

        try
        {
            if ( rs.next() )
            {
                return rs.getString( 1 );
            }
        }
        catch ( SQLException ex )
        {
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Error!");
            ex.printStackTrace();
            return "localhost";
        }

        return "localhost";
    }

}
