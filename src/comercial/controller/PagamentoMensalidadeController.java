/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

import entity.PagamentoMensalidade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Engº
 * @created 4/nov/2025
 * @lastModified 4/nov/2025
 */
public class PagamentoMensalidadeController
{

    private final Connection conexao;

    public PagamentoMensalidadeController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // 🔹 Salvar novo pagamento
    public boolean salvar( PagamentoMensalidade pagamento )
    {
        String sql = "INSERT INTO pagamento_mensalidade (produto_id, cliente_id, mes_id, venda_id, data_cadastro) VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, pagamento.getProdutoId() );
            ps.setInt( 2, pagamento.getClienteId() );
            ps.setShort( 3, pagamento.getMesId() );
            ps.setInt( 4, pagamento.getVendaId() );
            ps.setTimestamp( 5, new Timestamp( pagamento.getDataCadastro().getTime() ) );
            ps.executeUpdate();
            JOptionPane.showMessageDialog( null, "Pagamento salvo com sucesso!" );
            return true;
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao salvar pagamento: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Atualizar pagamento existente
    public boolean atualizar( PagamentoMensalidade pagamento )
    {
        String sql = "UPDATE pagamento_mensalidade SET produto_id=?, cliente_id=?, mes_id=?, venda_id=?, data_cadastro=? WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, pagamento.getProdutoId() );
            ps.setInt( 2, pagamento.getClienteId() );
            ps.setShort( 3, pagamento.getMesId() );
            ps.setInt( 4, pagamento.getVendaId() );
            ps.setTimestamp( 5, new Timestamp( pagamento.getDataCadastro().getTime() ) );
            ps.setInt( 6, pagamento.getId() );
            int linhas = ps.executeUpdate();
            if ( linhas > 0 )
            {
                JOptionPane.showMessageDialog( null, "Pagamento atualizado com sucesso!" );
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nenhum registro encontrado para atualizar." );
                return false;
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao atualizar pagamento: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Excluir pagamento
    public boolean deletar( int id )
    {
        String sql = "DELETE FROM pagamento_mensalidade WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            int linhas = ps.executeUpdate();
            if ( linhas > 0 )
            {
                JOptionPane.showMessageDialog( null, "Pagamento excluído com sucesso!" );
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog( null, "Nenhum registro encontrado para excluir." );
                return false;
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao excluir pagamento: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Buscar pagamento por ID
    public PagamentoMensalidade buscarPorId( int id )
    {
        String sql = "SELECT * FROM pagamento_mensalidade WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                PagamentoMensalidade p = new PagamentoMensalidade();
                p.setId( rs.getInt( "id" ) );
                p.setProdutoId( rs.getInt( "produto_id" ) );
                p.setClienteId( rs.getInt( "cliente_id" ) );
                p.setMesId( rs.getShort( "mes_id" ) );
                p.setVendaId( rs.getInt( "venda_id" ) );
                p.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
                return p;
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar pagamento: " + e.getMessage() );
        }
        return null;
    }

    // 🔹 Listar todos os pagamentos
    public List<PagamentoMensalidade> listarTodos()
    {
        List<PagamentoMensalidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagamento_mensalidade ORDER BY id DESC";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                PagamentoMensalidade p = new PagamentoMensalidade();
                p.setId( rs.getInt( "id" ) );
                p.setProdutoId( rs.getInt( "produto_id" ) );
                p.setClienteId( rs.getInt( "cliente_id" ) );
                p.setMesId( rs.getShort( "mes_id" ) );
                p.setVendaId( rs.getInt( "venda_id" ) );
                p.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
                lista.add( p );
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao listar pagamentos: " + e.getMessage() );
        }
        return lista;
    }
}
