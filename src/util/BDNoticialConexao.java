package util;


import java.sql.Connection;
/*----------------------------------------------
 *project: GPRODUTSS
 *fle:	BDConexao.java
 *Desenvolvido por: Osvaldo Ramos
 *alterado: Domingos Dala Vunge
 *----------------------------------------------*/

import javax.swing.*;
import java.sql.*;
import java.util.*;
import modelo.*;


public class BDNoticialConexao {

       private  Connection connection;
    private  Statement statement;
    private  PreparedStatement comando = null; //private static String ip_address = "localhost";
    private static String ip_address = "192.168.0.7";
    //private static final String JDBC_URL = "jdbc:mysql://localhost:3306/gestao_produto";
    private static final String JDBC_URL = "jdbc:mysql://"  +ip_address +":3306/gestao_produto";
    private static final String USER_NAME = "root", PASS_WORD = "root";
    public static int tipoUser = 0;

    
    public BDNoticialConexao() {
        try {
            //Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USER_NAME, PASS_WORD);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Falhou a Conexao com a Base de Dados");
        }
    }

    public static BDNoticialConexao getBDConetion() {
        return new BDNoticialConexao();
    }
    // metodo criado para a conexao a utilizar na geracao dos relatorios

    public static Connection conectar()//throws Exception
    {
        Connection mtdoConectar = null;

        try {
           // Class.forName(JDBC_DRIVER);
            mtdoConectar = DriverManager.getConnection(JDBC_URL, USER_NAME, PASS_WORD);

        } catch (SQLException sqlex) {
            showMessage("Erro ao Estabelecer a Conexao:" + sqlex.getMessage());
        }

        return mtdoConectar;
    }
    

    public static com.mysql.jdbc.Connection getConnection() {
       
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return (com.mysql.jdbc.Connection) DriverManager.getConnection( JDBC_URL , USER_NAME, PASS_WORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void finalized() {
        close();
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);

            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Falha ao Carregar os Dados");
        }

        return resultSet;
    }

    public boolean executeUpdate(String query) {
        ResultSet result = null;
        try {
            statement.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Falha ao Actualizar a BD");

            return false;
        }
        return true;
    }

    
    public void close() {
        
        try {
            statement.close();
            connection.close();
        } catch (SQLException sqlException) {
            //sqlException.printStackTrace();
            showMessage("Erro ao Fechar a Conexao");
        }
    }

    
    public static void showMessage(String mensagen) 
    {
            JOptionPane.showMessageDialog(null, mensagen);
    
    }
    
    
    
    /*
            1. Colocar todas as categorias num vector
            2. Inserir os produtos no vector_noticia vindo do localhost ordenadamento com a mesma categoria
            3. Guardar todos os produtos que estao no vector_noticia para a BD noticia
            4.     
    */
    
    
    public Vector<TipoProdutoModelo> getAllCategoria() {
       
        String sql = "SELECT * FROM tb_tipo_produto";
        ResultSet rs = executeQuery(sql);
        Vector<TipoProdutoModelo> vector = new Vector();

        TipoProdutoModelo tipoProdutoModelo;
        
        try {
            while (rs.next()) {
                
                tipoProdutoModelo = new TipoProdutoModelo();

                tipoProdutoModelo.setCodigo(   rs.getInt("codigo_")   );
                tipoProdutoModelo.setDesignacao(rs.getString("designacao")   );
                vector.add( tipoProdutoModelo );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return vector;
        }

        return vector;
        
    }
 
   
    
      public  void guardar_produto_noticia_comercial(Vector<ProdutoModelo> lita) 
     {

        try 
        {
               for (int i = 0; i < lita.size(); i++) 
               {
                     comando = connection.prepareStatement("INSERT INTO tb_produto ( designacao, preco, cod_Tipo_Produto, cod_fornecedores, data_fabrico"
                             + ", data_expiracao, codBarra, data_entrada, stocavel, status )"
                             + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ? )");
                     
                     comando.setString(1,  lita.get(i).getDesignacao());
                     comando.setDouble( 2,  lita.get(i).getPreco()  );
                     comando.setInt( 3,  lita.get(i).getCod_Tipo_Produto()  );
                     comando.setFloat( 4,  lita.get(i).getCod_fornecedores()  );
                     comando.setString( 5,  lita.get(i).getData_fabrico()  );
                     comando.setString( 6,  lita.get(i).getData_expiracao()  );
                     comando.setString( 7,  lita.get(i).getCod_barra()  );
                     comando.setString( 8,  lita.get(i).getData_entrada()  );
                     comando.setString( 9,  lita.get(i).getStocavel()  );
                     comando.setString( 10,  lita.get(i).getStatus()  );
                     
                     comando.execute();
                     comando.close();

                }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao tentar fazer a transferencia");
        }

     }
    
      
      
      
      
     public  void guardar_produto_stock_noticia_comercial(Vector<ProdutoModelo> lita) 
     {

        try 
        {
               for (int i = 0; i < lita.size(); i++) 
               {
                     comando = connection.prepareStatement("INSERT INTO tb_stock "
                             + "( cod_produto_codigo, quantidade_existente, status, preco_venda, quant_critica"
                             + ", quant_baixa, quantidade_antiga, cod_armazem )"
                             + " VALUES (?, ?, ?, ?, ?, ?, ?, ? )");
                     
                     comando.setInt(1,  lita.get(i).getCodigo() );
                     comando.setInt( 2,  0  );
                     comando.setString( 3,  "Activo"  );
                     comando.setFloat(4,  0  );
                     comando.setInt( 5,  50  );
                     comando.setInt( 6,  1  );
                     comando.setInt( 7,  0  );
                     comando.setInt( 8,  1  );
                     
                     comando.execute();
                     comando.close();

                }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao tentar fazer a transferencia");
        }

     }
    
      
      
    
      
       public Vector<ProdutoModelo> getAll_produtos_noticia_comercial() 
       {
       
            String sql = "SELECT * FROM tb_produto";
            ResultSet rs = executeQuery(sql);
            Vector<ProdutoModelo> vector = new Vector();

            ProdutoModelo produtoModelo;

            try {
                while (rs.next()) {

                    produtoModelo = new ProdutoModelo();
                    produtoModelo.setCodigo( rs.getInt("codigo")   );
                    produtoModelo.setDesignacao( rs.getString("designacao")   );
                    produtoModelo.setPreco( rs.getFloat("preco")   );
                    produtoModelo.setCod_Tipo_Produto( rs.getInt("cod_Tipo_Produto")   );
                    produtoModelo.setCod_fornecedores( rs.getInt("cod_fornecedores")   );
                    produtoModelo.setData_fabrico( rs.getString("data_fabrico")   );
                    produtoModelo.setData_expiracao( rs.getString("data_expiracao")   );
                    produtoModelo.setCod_barra( rs.getString("codBarra")   );
                    produtoModelo.setData_entrada( rs.getString("data_entrada")   );
                    produtoModelo.setStocavel( rs.getString("stocavel")   );
                    produtoModelo.setStatus(  rs.getString("status")   );

                    vector.add( produtoModelo );
                }



            } catch (SQLException ex) {
                ex.printStackTrace();
                return vector;
            }

            return vector;

        }

      
      
   
      
      
    
   
    public static void main(String args[])
    {
       
       BDNoticialConexao conexao_noticia_comercial = new BDNoticialConexao();
        
        
        //Busca todos os produtos por categorias
        Vector<ProdutoModelo> vector_produtos_noticia_comercial = conexao_noticia_comercial.getAll_produtos_noticia_comercial();
       
       
        conexao_noticia_comercial.guardar_produto_stock_noticia_comercial(vector_produtos_noticia_comercial);
        
        
      
        
    }
    
    

}
