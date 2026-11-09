/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import java.sql.Connection;
import entity.AnoEconomico;
import entity.Cambio;
import entity.Documento;
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
public class MesasController implements EntidadeFactory
{

    private BDConexao conexao;

    public MesasController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbMesas mesas = (TbMesas) object;
        String INSERT = "INSERT INTO tb_mesas( designacao , "
                + ")"
                + " VALUES("
                + "'" + mesas.getDesignacao() + "' , "
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_mesas WHERE pk_mesas = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbMesas> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_mesas ORDER BY pk_mesas ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbMesas> lista_mesas = new ArrayList<>();
        TbMesas mesas;
        try
        {

            while ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setDesignacao( result.getString( "designacao" ) );
                lista_mesas.add( mesas );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return lista_mesas;
    }

    @Override
    public Vector<String> getVector()
    {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE pk_mesas = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public Object findByDesignacao( String designacao )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_mesas WHERE designacao = '" + designacao + "'";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "pk_mesas" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    public TbMesas getLastMesa()
    {

        String FIND__BY_CODIGO = "SELECT MAX(pk_mesas) as maximo_id, m.*  FROM tb_mesas m";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbMesas mesas = null;
        try
        {

            if ( result.next() )
            {
                mesas = new TbMesas();
                mesas.setPkMesas( result.getInt( "maximo_id" ) );
                mesas.setDesignacao( result.getString( "designacao" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return mesas;

    }

    // Retorna o ID da mesa a partir da designação
    public int getIdByDescricao( String designacao ) throws SQLException
    {
        String sql = "SELECT pk_mesas FROM tb_mesas WHERE designacao = ?";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setString( 1, designacao );

            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getInt( "pk_mesas" );
                }
            }
        }
        return 0; // retorna 0 se não encontrar
    }


    // =====================================================================
    // MESAS DINÂMICAS (plano de sala e cadastro de mesas, como no SysKitandaHotel)
    // =====================================================================
    /** Ordem natural: "MESA 2" antes de "MESA 10"; designações livres no fim. */
    private static final String ORDEM_NATURAL
            = " ORDER BY (m.designacao REGEXP '^MESA [0-9]+$') DESC,"
            + " CAST(TRIM(SUBSTRING(m.designacao, 5)) AS UNSIGNED), m.designacao";

    /** Estado de uma mesa no plano de sala (lido de uma só vez para todas). */
    public static class EstadoMesa
    {

        public int codigo;
        public String designacao;
        public long itens;
        public double total;
        /** Hora do primeiro item ainda por facturar (null com a mesa livre). */
        public java.util.Date desde;
        /** Vendas de hoje da mesa (não usado no comercial: fica a 0). */
        public int vendasHoje;
        public double valorVendasHoje;

        /** O que vem depois de "MESA " (é o que o formulário de pedidos recebe). */
        public String getNumero()
        {
            return numeroDe( designacao );
        }
    }

    public static String numeroDe( String designacao )
    {
        String d = designacao == null ? "" : designacao.trim();
        return d.toUpperCase().startsWith( "MESA " ) ? d.substring( 5 ).trim() : d;
    }

    /** Todas as mesas, com o código, por ordem natural. */
    public List<TbMesas> listarOrdenadas()
    {
        List<TbMesas> lista = new ArrayList<>();
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT m.pk_mesas, m.designacao FROM tb_mesas m" + ORDEM_NATURAL );
                ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                TbMesas m = new TbMesas();
                m.setPkMesas( rs.getInt( "pk_mesas" ) );
                m.setDesignacao( rs.getString( "designacao" ) );
                lista.add( m );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Todas as mesas e o pedido em aberto de cada uma, numa só consulta. A
     * regra é a do plano antigo do comercial (último pedido da mesa e itens
     * com status_convertido = 0), que fazia duas consultas por mesa.
     */
    public List<EstadoMesa> estadoMesas() throws SQLException
    {
        String sql = "SELECT m.pk_mesas, m.designacao,"
                + " (SELECT COUNT(*) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS itens,"
                + " (SELECT COALESCE(SUM(i.total_item), 0) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS total,"
                + " (SELECT MIN(i.data_entrega) FROM tb_item_pedidos i WHERE i.fk_pedidos = p.ultimo AND i.status_convertido = 0) AS desde"
                + " FROM tb_mesas m"
                + " LEFT JOIN (SELECT fk_mesas, MAX(pk_pedido) AS ultimo FROM tb_pedido GROUP BY fk_mesas) p"
                + " ON p.fk_mesas = m.pk_mesas"
                + ORDEM_NATURAL;
        List<EstadoMesa> lista = new ArrayList<>();
        try ( PreparedStatement ps = conexao.getPreparedStatement( sql );
                ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                EstadoMesa e = new EstadoMesa();
                e.codigo = rs.getInt( "pk_mesas" );
                e.designacao = rs.getString( "designacao" );
                e.itens = rs.getLong( "itens" );
                // a 2 casas: há itens gravados com resíduos (ex.: 1500.0009)
                e.total = Math.round( rs.getDouble( "total" ) * 100 ) / 100.0;
                java.sql.Timestamp t = null;
                try
                {
                    t = rs.getTimestamp( "desde" );
                }
                catch ( SQLException semData )
                {
                    // data_entrega inválida numa linha: a mesa continua, sem a hora
                }
                e.desde = t == null ? null : new java.util.Date( t.getTime() );
                lista.add( e );
            }
        }
        return lista;
    }

    public boolean existeDesignacao( String designacao )
    {
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "SELECT 1 FROM tb_mesas WHERE UPPER(TRIM(designacao)) = UPPER(TRIM(?)) LIMIT 1" ) )
        {
            ps.setString( 1, designacao );
            try ( ResultSet rs = ps.executeQuery() )
            {
                return rs.next();
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return true; // na dúvida não se cria um duplicado
        }
    }

    /** Cria a mesa ("MESA N"); false se já existir ou der erro. */
    public boolean criarMesa( String designacao )
    {
        if ( designacao == null || designacao.trim().isEmpty() || existeDesignacao( designacao ) )
        {
            return false;
        }
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "INSERT INTO tb_mesas (designacao) VALUES (?)" ) )
        {
            ps.setString( 1, designacao.trim() );
            return ps.executeUpdate() == 1;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    /** Pedidos e vendas que apontam para a mesa (com algum, não se elimina). */
    public int contarUtilizacoes( int codigo )
    {
        int total = 0;
        for ( String tabela : new String[]
        {
            "tb_pedido", "tb_item_venda"
        } )
        {
            try ( PreparedStatement ps = conexao.getPreparedStatement(
                    "SELECT COUNT(*) FROM " + tabela + " WHERE fk_mesas = ?" ) )
            {
                ps.setInt( 1, codigo );
                try ( ResultSet rs = ps.executeQuery() )
                {
                    if ( rs.next() )
                    {
                        total += rs.getInt( 1 );
                    }
                }
            }
            catch ( SQLException e )
            {
                e.printStackTrace();
                return Integer.MAX_VALUE;
            }
        }
        return total;
    }

    /** Elimina só se a mesa nunca foi usada: o histórico de pedidos e vendas fica intacto. */
    public boolean eliminarSeLivre( int codigo )
    {
        if ( contarUtilizacoes( codigo ) > 0 )
        {
            return false;
        }
        try ( PreparedStatement ps = conexao.getPreparedStatement(
                "DELETE FROM tb_mesas WHERE pk_mesas = ?" ) )
        {
            ps.setInt( 1, codigo );
            return ps.executeUpdate() == 1;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

}
