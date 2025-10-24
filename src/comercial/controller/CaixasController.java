/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.Caixa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JOptionPane;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class CaixasController implements EntidadeFactory
{

    private BDConexao conexao;

    public CaixasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        Caixa caixa = ( Caixa ) object;
        String INSERT = "INSERT INTO caixa( data_abertura , data_fecho , total_vendas ,"
                + " numero_vendas , valor_inicial , usuario_fecho , usuario_abertura, "
                + " cod_usuario_abertura, cod_usuario_fecho, total_desconto, total_iva, total_iliquido "
                + ")"
                + " VALUES("
                + "'" + MetodosUtil.getDataBancoFull( caixa.getDataAbertura() ) + "', "
                + getDataFecho( caixa.getDataFecho() ) + " , "
                + caixa.getTotalVendas() + " , "
                + "'" + caixa.getNumeroVendas() + "' , "
                + caixa.getValorInicial() + " , "
                + "'" + caixa.getUsuarioFecho() + "' , "
                + "'" + caixa.getUsuarioAbertura() + "', "
                + caixa.getCodUsuarioAbertura() + ","
                + caixa.getCodUsuarioFecho() + ","
                + caixa.getTotalDesconto() + ","
                + caixa.getTotalIva() + ","
                + caixa.getTotaIIliquido()
                + ") ";

        System.out.println( "Modo INSERT" + INSERT );
        return conexao.executeUpdate( INSERT );
    }

    @Override
    public boolean actualizar(Object object) {
    Caixa caixa = (Caixa) object;

    String sql = "UPDATE caixa SET "
            + "data_abertura = ?, "
            + "data_fecho = ?, "
            + "total_vendas = ?, "
            + "numero_vendas = ?, "
            + "valor_inicial = ?, "
            + "usuario_fecho = ?, "
            + "usuario_abertura = ?, "
            + "cod_usuario_abertura = ?, "
            + "cod_usuario_fecho = ?, "
            + "total_desconto = ?, "
            + "total_iva = ?, "
            + "total_iliquido = ? "
            + "WHERE pk_caixa = ?";

    try {
        
        PreparedStatement ps = conexao.getConnectionAtiva().prepareStatement(sql);

        ps.setTimestamp(1, new java.sql.Timestamp(caixa.getDataAbertura().getTime()));
        ps.setTimestamp(2, new java.sql.Timestamp(caixa.getDataFecho().getTime()));
        ps.setDouble(3, caixa.getTotalVendas());
        ps.setInt(4, caixa.getNumeroVendas());
        ps.setDouble(5, caixa.getValorInicial());
        ps.setString(6, caixa.getUsuarioFecho());
        ps.setString(7, caixa.getUsuarioAbertura());
        ps.setInt(8, caixa.getCodUsuarioAbertura());
        ps.setInt(9, caixa.getCodUsuarioFecho());
        ps.setBigDecimal(10, caixa.getTotalDesconto());
        ps.setBigDecimal(11, caixa.getTotalIva());
        ps.setBigDecimal(12, caixa.getTotaIIliquido());
        ps.setInt(13, caixa.getPkCaixa());

        int resultado = ps.executeUpdate();
        return resultado > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

//    public boolean actualizar( Object object )
//    {
//        Caixa caixa = ( Caixa ) object;
//        String sql = "UPDATE caixa SET "
//                + " data_abertura = '" + MetodosUtil.getDataBancoFull( caixa.getDataAbertura() ) + "'"
//                + ", data_fecho = '" + MetodosUtil.getDataBancoFull( caixa.getDataFecho() ) + "'"
//                + ", total_vendas = " + caixa.getTotalVendas()
//                + ", numero_vendas = " + caixa.getNumeroVendas()
//                + ", valor_inicial = " + caixa.getValorInicial()
//                + ", usuario_fecho = '" + caixa.getUsuarioFecho() + "'"
//                + ", usuario_abertura = '" + caixa.getUsuarioAbertura() + "'"
//                + ", cod_usuario_abertura = " + caixa.getCodUsuarioAbertura()
//                + ", cod_usuario_fecho = " + caixa.getCodUsuarioFecho()
//                + ", total_desconto = " + caixa.getTotalDesconto()
//                + ", total_iva = " + caixa.getTotalIva()
//                + ", total_iliquido = " + caixa.getTotaIIliquido()
//                + " WHERE pk_caixa = " + caixa.getPkCaixa();
//        System.out.println( sql );
//        return conexao.executeUpdate( sql );
//    }

    @Override
    public boolean eliminar( int codigo )
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {
        String FIND__BY_CODIGO = "SELECT * FROM caixa WHERE pk_caixa = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        Caixa caixa = null;
        try
        {

            if ( result.next() )
            {
                caixa = new Caixa();
                caixa.setPkCaixa( result.getInt( "pk_caixa" ) );
                caixa.setDataAbertura( result.getDate( "data_abertura" ) );
                caixa.setDataFecho( result.getDate( "data_fecho" ) );
                caixa.setTotalVendas( result.getDouble( "pk_caixa" ) );
                caixa.setNumeroVendas( result.getInt( "numero_vendas" ) );
                caixa.setValorInicial( result.getDouble( "valor_inicial" ) );
                caixa.setUsuarioFecho( result.getString( "usuario_fecho" ) );
                caixa.setCodUsuarioAbertura( result.getInt( "cod_usuario_abertura" ) );
                caixa.setCodUsuarioFecho( result.getInt( "cod_usuario_fecho" ) );
                caixa.setUsuarioAbertura( result.getString( "usuario_abertura" ) );
                caixa.setTotalDesconto( result.getBigDecimal( "total_desconto" ) );
                caixa.setTotalIva( result.getBigDecimal( "total_iva" ) );
                caixa.setTotaIIliquido( result.getBigDecimal( "total_iliquido" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return caixa;
    }

    public Caixa findByCodigo( int codigo )
    {
        String FIND_BY_CODIGO = "SELECT * FROM caixa c WHERE pk_caixa = " + codigo;

        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        Caixa caixa = null;
        try
        {

            if ( result.next() )
            {
                caixa = new Caixa();
                caixa.setPkCaixa( result.getInt( "pk_caixa" ) );
                caixa.setDataAbertura( result.getTimestamp( "data_abertura" ) );
                caixa.setDataFecho( result.getTimestamp( "data_fecho" ) );
                caixa.setTotalVendas( result.getDouble( "total_vendas" ) );
                caixa.setNumeroVendas( result.getInt( "numero_vendas" ) );
                caixa.setValorInicial( result.getDouble( "valor_inicial" ) );
                caixa.setUsuarioFecho( result.getString( "usuario_fecho" ) );
                caixa.setUsuarioAbertura( result.getString( "usuario_abertura" ) );
                caixa.setCodUsuarioAbertura( result.getInt( "cod_usuario_abertura" ) );
                caixa.setCodUsuarioFecho( result.getInt( "cod_usuario_fecho" ) );
                caixa.setTotalDesconto( result.getBigDecimal( "total_desconto" ) );
                caixa.setTotalIva( result.getBigDecimal( "total_iva" ) );
                caixa.setTotaIIliquido( result.getBigDecimal( "total_iliquido" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return caixa;

    }

    public Caixa caixa_actual()
    {
        return findByCodigo( getLastCaixa() );
    }

    public Caixa caixa_actual_data( Date data_abertura )
    {
        return findByCodigo( getLastCaixaByDataAbertura( data_abertura ) );
    }

    @Override
    public List<?> listarTodos()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean existeCaixas()
    {
        String sql = "SELECT * FROM caixa";
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }

        }
        catch ( SQLException e )
        {
        }

        return false;
    }

    public int getLastCaixa()
    {
        String sql = "SELECT MAX(pk_caixa) AS maximo_id FROM caixa";

        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Integer last_id = rs.getInt( "maximo_id" );
                return last_id;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0;

    }

    public boolean existe_fecho()
    {
        try
        {
            Caixa caixa_actual = ( Caixa ) findById( getLastCaixa() );
            return !Objects.isNull( caixa_actual.getDataFecho() );
        }
        catch ( Exception e )
        {
            return false;
        }

    }

//    public boolean existe_fecho()
//    {
//        try
//        {
//            Caixa caixa_actual = findByCodigo( getLastCaixa() );
//            return !Objects.isNull( caixa_actual.getDataFecho() );
//        }
//        catch ( Exception e )
//        {
//            return false;
//        }
//
//    }
    public boolean existe_abertura()
    {
        try
        {
            Caixa caixa_actual = ( Caixa ) findById( getLastCaixa() );
            return !Objects.isNull( caixa_actual.getDataAbertura() );
        }
        catch ( Exception e )
        {
            return false;
        }

    }

    public boolean existe_abertura( Date date, BDConexao conexao )
    {
        String query = "SELECT * FROM caixa WHERE DATE(data_abertura) =   '" + MetodosUtil.getDataBanco( date ) + "'";
        ResultSet executeQuery = conexao.executeQuery( query );
        try
        {
            return executeQuery.next();
        }
        catch ( SQLException e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public boolean existe_fecho( Date date, BDConexao conexao )
    {

        String query = "SELECT * FROM caixa WHERE   DATE(data_fecho) =   '" + MetodosUtil.getDataBanco( date ) + "'";
        ResultSet executeQuery = conexao.executeQuery( query );
        try
        {
            return executeQuery.next();
        }
        catch ( SQLException e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public boolean existe_fecho( Caixa caixa, BDConexao conexao )
    {
        String query = "SELECT * FROM caixa WHERE   DATE(data_fecho) =   '" + MetodosUtil.getDataBancoFull( caixa.getDataFecho() ) + "'";
        ResultSet executeQuery = conexao.executeQuery( query );
        try
        {
            return executeQuery.next();
        }
        catch ( SQLException e )
        {
            System.out.println( "Erro: " + e.getLocalizedMessage() );
        }

        return false;

    }

    public int getLastCaixaByDataAbertura( Date data_abertura )
    {
        String sql = "SELECT MAX(pk_caixa) AS maximo_id FROM caixa WHERE date(data_abertura) = '" + MetodosUtil.getDataBanco( data_abertura ) + "'";

        System.err.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Integer last_id = rs.getInt( "maximo_id" );
                return last_id;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0;

    }

    public int getFirstIdCaixaByDataAbertura( Date data_abertura )
    {
        String sql = "SELECT MIN(pk_caixa) AS minimo_id FROM caixa WHERE date(data_abertura) = '" + MetodosUtil.getDataBanco( data_abertura ) + "'";

        System.err.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Integer minimo_id = rs.getInt( "minimo_id" );
                return minimo_id;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0;

    }

    public int getIdUsusarioByIdCaixa( int idCaixa )
    {
        String sql = "SELECT cod_usuario_fecho AS maximo_id FROM caixa WHERE pk_caixa = " + idCaixa;

        System.err.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Integer last_id = rs.getInt( "maximo_id" );
                return last_id;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return 0;

    }

    public Date buscar_data_feicho_of_last_id()
    {
        int lastId = getLastCaixaByDataAbertura( new Date() );

        String sql = "SELECT DATE(data_fecho) AS data_fecho FROM caixa WHERE  pk_caixa = " + lastId;

        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Date last_data_fecho = rs.getTimestamp( "data_fecho" );
                return last_data_fecho;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return null;
    }

    public Date buscar_data_feicho_by_id( int idCaixa )
    {

        String sql = "SELECT data_fecho AS data_fecho FROM caixa WHERE  pk_caixa = " + idCaixa;
        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Date last_data_fecho = rs.getTimestamp( "data_fecho" );
                return last_data_fecho;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return null;
    }

    public Date buscar_data_abertura_by_id( int idCaixa )
    {

        String sql = "SELECT data_abertura AS data_abertura FROM caixa WHERE  pk_caixa = " + idCaixa;
        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                Date last_data_abertura = rs.getTimestamp( "data_abertura" );
                return last_data_abertura;
//                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }

        return null;
    }

    public boolean possivelVendaByCaixaAberto()
    {
        int lastId = getLastCaixaByDataAbertura( new Date() );
        Date data = buscar_data_feicho_of_last_id();
        Caixa caixa = ( Caixa ) findById( lastId );

//        if ( Objects.isNull( data ) && lastId > 1 )
//        {
//            JOptionPane.showMessageDialog( null, "Caro usuario deve realizar o fecho do caixa n:(" + lastId + ")\n"
//                    + "do dia " + MetodosUtil.getDataBanco( caixa.getDataAbertura() ) + ".", "Aviso", JOptionPane.WARNING_MESSAGE );
//            return false;
//        }
        return true;
    }

    private String getDataFecho( Date data )
    {
        return Objects.isNull( data ) ? "null" : " '" + MetodosUtil.getDataBancoFull( data ) + "'";
    }

    public static void main( String[] args )
    {
        CaixasController caixa = new CaixasController( BDConexao.getInstancia() );
        int firstId = caixa.getFirstIdCaixaByDataAbertura( new Date() );
        int lastId = caixa.getLastCaixaByDataAbertura( new Date() );

        System.out.println( "FIRST ID: " + firstId );
        System.out.println( "LAST ID: " + lastId );
    }

    public boolean existeCaixas( int idUser )
    {
        String sql = "SELECT * FROM caixa WHERE cod_usuario_abertura = " + idUser;
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
        }
        return false;
    }

    public boolean existeCaixas( int idUser, Date data )
    {
        String sql = "SELECT * FROM caixa WHERE cod_usuario_abertura = " + idUser + " AND date(data_abertura) = '" + MetodosUtil.getDataBanco( data ) + "'";
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return true;
            }
        }
        catch ( SQLException e )
        {
        }
        return false;
    }

    public int getLastCaixa( int idUser )
    {
        String sql = "SELECT MAX(pk_caixa) AS maximo_id FROM caixa WHERE cod_usuario_abertura = " + idUser;
        System.out.println( sql );
        ResultSet rs = conexao.executeQuery( sql );
        try
        {
            if ( rs.next() )
            {
                return rs.getInt( "maximo_id" );
            }
        }
        catch ( SQLException e )
        {
        }
        return 0;

    }

    public boolean existe_fecho( int idUser )
    {
        try
        {
            Caixa caixa_actual = ( Caixa ) findById( getLastCaixa( idUser ) );
            return !Objects.isNull( caixa_actual.getDataFecho() );
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    public Caixa caixa_actual( int idUser )
    {
        return findByCodigo( getLastCaixa( idUser ) );
    }

    public boolean existe_abertura( int cod_utilizador )
    {
        try
        {
            Caixa caixa_actual = ( Caixa ) findById( getLastCaixa() );
            return !Objects.isNull( caixa_actual.getDataAbertura() );
        }
        catch ( Exception e )
        {
            return false;
        }
    }

}
