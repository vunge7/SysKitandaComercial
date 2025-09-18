/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import entity.TbArmazem;
import entity.TbSaidasProdutos;
import entity.TbUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Domingos Dala Vunge
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaidasProdutosController
{

    private Connection connection;

    public SaidasProdutosController( Connection conexao )
    {
        this.connection = conexao;
    }

    public void inserir( TbSaidasProdutos sp ) throws SQLException
    {
        String sql = "INSERT INTO tb_saidas_produtos (data_saida, fk_usuario, idArmazemFK, hora_saida, status_eliminado, obs, nome_funcionario, documento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ) )
        {
            stmt.setDate( 1, new java.sql.Date( sp.getDataSaida().getTime() ) );
            stmt.setInt( 2, sp.getFkUsuario().getCodigo() );
            stmt.setInt( 3, sp.getIdArmazemFK().getCodigo() );
            stmt.setTime( 4, new java.sql.Time( sp.getHoraSaida().getTime() ) );
            stmt.setString( 5, sp.getStatusEliminado() );
            stmt.setString( 6, sp.getObs() );
            stmt.setString( 7, sp.getNomeFuncionario() );
            stmt.setString( 8, sp.getDocumento() );
            stmt.executeUpdate();
        }
    }

    public void atualizar( TbSaidasProdutos sp ) throws SQLException
    {
        String sql = "UPDATE tb_saidas_produtos "
                + " SET data_saida=?, "
                + " fk_usuario=?, "
                + " idArmazemFK=?, "
                + " hora_saida=?, "
                + " status_eliminado=?, "
                + " obs=?, "
                + " nome_funcionario=?, "
                + " documento=? "
                + "WHERE pk_saidas_produtos=?";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ) )
        {

            stmt.setDate( 1, new java.sql.Date( sp.getDataSaida().getTime() ) );
            stmt.setInt( 2, sp.getFkUsuario().getCodigo() );
            stmt.setInt( 3, sp.getIdArmazemFK().getCodigo() );
            stmt.setTime( 4, new java.sql.Time( sp.getHoraSaida().getTime() ) );
            stmt.setString( 5, sp.getStatusEliminado() );
            stmt.setString( 6, sp.getObs() );
            stmt.setString( 7, sp.getNomeFuncionario() );
            stmt.setString( 8, sp.getDocumento() );
            stmt.setInt( 9, sp.getPkSaidasProdutos() );
            stmt.executeUpdate();
        }
    }

    public void deletar( int id ) throws SQLException
    {
        String sql = "DELETE FROM tb_saidas_produtos WHERE pk_saidas_produtos=?";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ) )
        {
            stmt.setInt( 1, id );
            stmt.executeUpdate();
        }
    }

    public TbSaidasProdutos buscarPorId( int id ) throws SQLException
    {
        String sql = "SELECT * FROM tb_saidas_produtos WHERE pk_saidas_produtos=?";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ) )
        {
            stmt.setInt( 1, id );
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() )
            {
                return mapear( rs );
            }
        }
        return null;
    }

    public List<TbSaidasProdutos> listarTodos() throws SQLException
    {

        List<TbSaidasProdutos> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_saidas_produtos";
        try ( Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery( sql ) )
        {
            while ( rs.next() )
            {
                lista.add( mapear( rs ) );
            }
        }
        return lista;
    }

    private TbSaidasProdutos mapear( ResultSet rs ) throws SQLException
    {
        TbSaidasProdutos sp = new TbSaidasProdutos();
        sp.setPkSaidasProdutos( rs.getInt( "pk_saidas_produtos" ) );
        sp.setDataSaida( rs.getDate( "data_saida" ) );
        sp.setFkUsuario( new TbUsuario( rs.getInt( "fk_usuario" ) ) );
        sp.setIdArmazemFK( new TbArmazem( rs.getInt( "idArmazemFK" ) ) );
        sp.setHoraSaida( rs.getTime( "hora_saida" ) );
        sp.setStatusEliminado( rs.getString( "status_eliminado" ) );
        sp.setObs( rs.getString( "obs" ) );
        sp.setNomeFuncionario( rs.getString( "nome_funcionario" ) );
        sp.setDocumento( rs.getString( "documento" ) );
        return sp;
    }

    public TbSaidasProdutos buscarUltimaSaida() throws SQLException
    {
        String sql = "SELECT * FROM tb_saidas_produtos ORDER BY pk_saidas_produtos DESC LIMIT 1";
        try ( PreparedStatement stmt = connection.prepareStatement( sql ); ResultSet rs = stmt.executeQuery() )
        {
            if ( rs.next() )
            {
                return mapear( rs );
            }
        }
        return null;
    }

  

}
