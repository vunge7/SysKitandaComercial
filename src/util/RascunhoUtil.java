/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import java.sql.Connection;
import dao.PrecoDao;
import entity.TbPreco;
import entity.TbProduto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Domingos Dala Vunge
 */
public class RascunhoUtil
{

    private static BDConexao conexao;

    public RascunhoUtil( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    private static void eliminar_numero_repetidos()
    {
        //Pegar a lista dos numeros repetidos
        //Para cada numero repeditido buscar as ocorrências 
        //pegar a ultima ocirrencia e eliminar
    }

    private void imprimir( Vector<String> vector )
    {
        for ( int i = 0; i < vector.size(); i++ )
        {
            System.out.println( vector.get( i ) );
            System.out.println( "MAXIMO: " + getMaximoDocumentoRespetido( vector.get( i ) ) );
            Integer codigo_venda = getMaximoDocumentoRespetido( vector.get( i ) ).get( 0 );
            eliminar_venda( codigo_venda );

        }

    }

//    private void procedimento_eliminar( Vector<Integer> vector )
//    {
//        
//        for ( int i = 0; i < vector.size(); i++ )
//        {
//            Integer elementAt = vector.elementAt( i );
//            
//            eliminar_venda( elementAt);
//            
//        }
//        
//    }
    private Vector<Integer> getMaximoDocumentoRespetido( String cod_fact )
    {
        String sql = "SELECT MAX(codigo) as MAXIMO FROM tb_venda WHERE cod_fact = '" + cod_fact + "'";
        Vector<Integer> vector = new Vector<>();

        ResultSet result = conexao.executeQuery( sql );

        try
        {
            while ( result.next() )
            {
                Integer campo = result.getInt( "MAXIMO" );
                vector.add( campo );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( RascunhoUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return vector;

    }

    public Vector<String> getDcoumentosRepetidos()
    {
        String sql = "SELECT cod_fact FROM tb_venda group by cod_fact having Count(cod_fact)>1";

        ResultSet result = conexao.executeQuery( sql );

        Vector<String> list_documento = new Vector<>();
        try
        {
            while ( result.next() )
            {
                String campo = result.getString( "cod_fact" );
                list_documento.add( campo );
            }
        }
        catch ( SQLException ex )
        {
            Logger.getLogger( RascunhoUtil.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return list_documento;

    }

    private boolean eliminar_venda( int cod_venda )
    {
        String sql = "DELETE FROM tb_venda WHERE codigo = " + cod_venda;
        return conexao.executeUpdate( sql );
    }

    public static void main( String[] args )
    {
//        RascunhoUtil rascunhoUtil = new RascunhoUtil( BDConexao.getInstancia() );
//
//        Vector<String> dcoumentosRepetidos = rascunhoUtil.getDcoumentosRepetidos();
//        rascunhoUtil.imprimir( dcoumentosRepetidos );

        inserir_precos_grosso( JPAEntityMannagerFactoryUtil.em );

    }

    public static void inserir_precos_grosso( EntityManagerFactory emf )
    {
        PrecoDao precoDao = new PrecoDao( emf );
        BDConexao conexao = BDConexao.getInstancia();
        List<TbPreco> lista_preco = precoDao.getAllPrecos();
        boolean sucesso = true;

        for ( int i = 0; i < lista_preco.size(); i++ )
        {
            TbPreco get = lista_preco.get( i );

            System.out.println( "Produto: " + get.getFkProduto().getCodigo() );
            System.out.println( "Designacão: " + get.getFkProduto().getDesignacao() );
            TbPreco preco_grosso = get;

            preco_grosso.setQtdBaixo( 6 );
            preco_grosso.setQtdAlto( Integer.MAX_VALUE );

            String sql = "INSERT INTO tb_preco( "
                    + " data , hora, preco_compra , percentagem_ganho , "
                    + " fk_produto, fk_usuario , preco_venda , qtd_baixo , "
                    + " qtd_alto, preco_anterior, retalho )"
                    + ""
                    + " VALUES( "
                    + " '" + MetodosUtil.getDataBanco( preco_grosso.getData() ) + "' , "
                    + " '" + MetodosUtil.getHoraBanco( preco_grosso.getHora() ) + "' , "
                    + preco_grosso.getPrecoCompra() + " , "
                    + preco_grosso.getPercentagemGanho() + " , "
                    + preco_grosso.getFkProduto().getCodigo() + " , "
                    + preco_grosso.getFkUsuario().getCodigo() + " , "
                    + preco_grosso.getPrecoVenda() + " , "
                    + preco_grosso.getQtdBaixo() + " , "
                    + preco_grosso.getQtdAlto() + " , "
                    + preco_grosso.getPrecoAnterior() + " , "
                    + preco_grosso.getRetalho()
                    + ")";

            try
            {
                conexao.executeUpdate( sql );
            }
            catch ( Exception e )
            {
                sucesso = false;
                e.printStackTrace();
                break;
            }

        }

        if ( sucesso )
        {
            System.out.println( "Sucesso na inserção dos dados (actualização)" );
        }
        else
        {
            System.err.println( "Falha na actualização." );
        }

    }

}
