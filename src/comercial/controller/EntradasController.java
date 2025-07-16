/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;
import entity.TbArmazem;
import entity.TbEntrada;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Martinho Luis
 */
public class EntradasController implements EntidadeFactory
{

    private BDConexao conexao;

    public EntradasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbEntrada entradas = (TbEntrada) object;
        String INSERT = "INSERT INTO tb_entrada( data_entrada , idUsuario , idArmazemFK , status_eliminado "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( entradas.getDataEntrada() ) + "' , "
                + entradas.getIdUsuario().getCodigo() + ", "
                + entradas.getIdArmazemFK().getCodigo() + ", "
                + "'" + entradas.getStatusEliminado() + "' "
                + " ) ";

        System.out.println( "Modo INSERT" + INSERT );
        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        TbEntrada entradas = (TbEntrada) object;

        String sql = "UPDATE tb_entrada SET "
                + " data_entrada = '" + MetodosUtil.getDataBancoFull( entradas.getDataEntrada() ) + "'"
                + " , idUsuario = " + entradas.getIdUsuario().getCodigo()
                + ", idArmazemFK = " + entradas.getIdArmazemFK().getCodigo()
                + ", status_eliminado = '" + entradas.getStatusEliminado()
                + " WHERE idEntrada = ?";
        System.out.println( sql );
        return conexao.executeUpdate( sql );
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_entrada WHERE idEntrada = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbEntrada> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_entrada ORDER BY idEntrada ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbEntrada> lista_entradas = new ArrayList<>();
        TbEntrada entradas;
        try
        {
            while ( result.next() )
            {
                entradas = new TbEntrada();
                entradas.setIdEntrada(result.getInt( "idEntrada" ) );
                entradas.setDataEntrada(    result.getDate( "data_entrada" ) );
                entradas.setIdUsuario(  new TbUsuario (result.getInt( "idUsuario" ) ));
                entradas.setIdArmazemFK( new TbArmazem(result.getInt( "idArmazemFK" ) ));
                entradas.setStatusEliminado(result.getString("status_eliminado" ) );

                lista_entradas.add( entradas );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_entradas;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_entrada WHERE idEntrada = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbEntrada entradas = null;
        try
        {

            if ( result.next() )
            {
                entradas = new TbEntrada();
                entradas.setIdEntrada(result.getInt( "idEntrada" ) );
                entradas.setDataEntrada(    result.getDate( "data_entrada" ) );
                entradas.setIdUsuario(  new TbUsuario (result.getInt( "idUsuario" ) ));
                entradas.setIdArmazemFK( new TbArmazem(result.getInt( "idArmazemFK" ) ));
                entradas.setStatusEliminado(result.getString("status_eliminado" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return entradas;

    }

    public TbEntrada getLastEntrada()
    {

        String FIND__BY_CODIGO = "   SELECT * FROM tb_entrada WHERE idEntrada = ( SELECT MAX(idEntrada) as maximo_id   FROM tb_entrada f ) ";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbEntrada entradas = null;
        try
        {

            if ( result.next() )
            {
                entradas = new TbEntrada();
                entradas.setIdEntrada(result.getInt( "idEntrada" ) );
                entradas.setDataEntrada(    result.getDate( "data_entrada" ) );
                entradas.setIdUsuario(  new TbUsuario (result.getInt( "idUsuario" ) ));
                entradas.setIdArmazemFK( new TbArmazem(result.getInt( "idArmazemFK" ) ));
                entradas.setStatusEliminado(result.getString("status_eliminado" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return entradas;

    }

    private TbEntrada getEntradaResultSet( TbEntrada entradas, ResultSet result )
    {
        try
        {
                entradas.setIdEntrada(result.getInt( "idEntrada" ) );
                entradas.setDataEntrada(    result.getDate( "data_entrada" ) );
                entradas.setIdUsuario(  new TbUsuario (result.getInt( "idUsuario" ) ));
                entradas.setIdArmazemFK( new TbArmazem(result.getInt( "idArmazemFK" ) ));
                entradas.setStatusEliminado(result.getString("status_eliminado" ) );

        }
        catch ( Exception e )
        {
        }

        return entradas;
    }

    public List<TbEntrada> listarTodasEntradasByData1AndData2( Date data_1, Date data_2 )
    {
        String FIND_ALL = "SELECT * FROM tb_entrada WHERE DATE(data_entrada) BETWEEN '" + MetodosUtil.getDataBanco( data_1 ) + "' AND '" + MetodosUtil.getDataBanco( data_2 ) + "'";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbEntrada> lista_entradas = new ArrayList<>();
        TbEntrada entradas;
        try
        {
            while ( result.next() )
            {
                entradas = new TbEntrada();
                entradas = getEntradaResultSet( entradas, result );
                lista_entradas.add( entradas );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_entradas;
    }

    /**
     *
     * @param cod_fact
     * @decricao busca todos os recibos de uma determnada factura.
     * @return
     */

    public TbEntrada getEntradas( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT f.idEntrada FROM tb_entrada f WHERE idEntrada = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbEntrada entradas = null;
        try
        {

            if ( result.next() )
            {

                entradas = new TbEntrada();
                entradas = getEntradaResultSet( entradas, result );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return entradas;

    }

    public boolean existeEntradas( int codigo )
    {
        String sql = "SELECT *  FROM tb_entrada WHERE idEntrada = " + codigo;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        return false;

    }

    public static void main( String[] args )
    {
        BDConexao conexao = new BDConexao();
        EntradasController f = new EntradasController( conexao );

    }

}
