/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 4/nov/2025
 * @lastModified 4/nov/2025
 */
import entity.ConfiguracaoMesComeco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * @author Engº
 * @created 4/nov/2025
 * @lastModified 4/nov/2025
 */
public class ConfiguracaoMesComecoController
{

    private final Connection conexao;

    public ConfiguracaoMesComecoController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // 🔹 Inserir nova configuração
    public boolean salvar( ConfiguracaoMesComeco config )
    {
        String sql = "INSERT INTO configuracao_mes_comeco (data_cadastro, mes_id, produto_id, usuario_id, duracao, cliente_id) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setTimestamp( 1, new Timestamp( config.getDataCadastro().getTime() ) );
            ps.setInt( 2, config.getMesId() );
            ps.setInt( 3, config.getProdutoId() );
            ps.setInt( 4, config.getUsuarioId() );
            ps.setInt( 5, config.getDuracao() );
            ps.setInt( 6, config.getClienteId() );
            ps.executeUpdate();
            JOptionPane.showMessageDialog( null, "Configuração salva com sucesso!" );
            return true;
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao salvar: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Atualizar configuração existente
    public boolean atualizar( ConfiguracaoMesComeco config )
    {
        String sql = "UPDATE configuracao_mes_comeco SET data_cadastro=?, mes_id=?, produto_id=?, usuario_id=?, duracao=?, cliente_id=? WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setTimestamp( 1, new Timestamp( config.getDataCadastro().getTime() ) );
            ps.setInt( 2, config.getMesId() );
            ps.setInt( 3, config.getProdutoId() );
            ps.setInt( 4, config.getDuracao() );
            ps.setInt( 5, config.getUsuarioId() );
            ps.setInt( 6, config.getClienteId() );
            ps.setInt( 7, config.getId() );
            int linhas = ps.executeUpdate();
            if ( linhas > 0 )
            {
                JOptionPane.showMessageDialog( null, "Configuração atualizada com sucesso!" );
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
            JOptionPane.showMessageDialog( null, "Erro ao atualizar: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Apagar configuração
    public boolean deletar( int id )
    {
        String sql = "DELETE FROM configuracao_mes_comeco WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            int linhas = ps.executeUpdate();
            if ( linhas > 0 )
            {
                JOptionPane.showMessageDialog( null, "Configuração removida com sucesso!" );
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
            JOptionPane.showMessageDialog( null, "Erro ao excluir: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Buscar por ID
    public ConfiguracaoMesComeco buscarPorId( int id )
    {
        String sql = "SELECT * FROM configuracao_mes_comeco WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, id );
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                ConfiguracaoMesComeco config = new ConfiguracaoMesComeco();
                config.setId( rs.getInt( "id" ) );
                config.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
                config.setMesId( rs.getShort( "mes_id" ) );
                config.setProdutoId( rs.getInt( "produto_id" ) );
                config.setUsuarioId( rs.getInt( "usuario_id" ) );
                config.setDuracao( rs.getInt( "duracao" ) );
                config.setClienteId( rs.getInt( "cliente_id" ) );
                return config;
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar: " + e.getMessage() );
        }
        return null;
    }

    // 🔹 Listar todas as configurações
    public List<ConfiguracaoMesComeco> listarTodos()
    {
        List<ConfiguracaoMesComeco> lista = new ArrayList<>();
        String sql = "SELECT * FROM configuracao_mes_comeco ORDER BY id DESC";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {
            while ( rs.next() )
            {
                ConfiguracaoMesComeco config = new ConfiguracaoMesComeco();
                config.setId( rs.getInt( "id" ) );
                config.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
                config.setMesId( rs.getShort( "mes_id" ) );
                config.setProdutoId( rs.getInt( "produto_id" ) );
                config.setUsuarioId( rs.getInt( "usuario_id" ) );
                config.setDuracao( rs.getInt( "duracao" ) );
                config.setClienteId( rs.getInt( "cliente_id" ) );
                lista.add( config );
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao listar: " + e.getMessage() );
        }
        return lista;
    }

    // 🔹 Listar todas as configurações
    public List<ConfiguracaoMesComeco> listarTodos( int clienteId )
    {
        List<ConfiguracaoMesComeco> lista = new ArrayList<>();
        String sql = "SELECT * FROM configuracao_mes_comeco WHERE cliente_id = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ); )
        {
            ps.setInt( 1, clienteId );
            ResultSet rs = ps.executeQuery();
            while ( rs.next() )
            {
                ConfiguracaoMesComeco config = new ConfiguracaoMesComeco();
                config.setId( rs.getInt( "id" ) );
                config.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
                config.setMesId( rs.getShort( "mes_id" ) );
                config.setProdutoId( rs.getInt( "produto_id" ) );
                config.setUsuarioId( rs.getInt( "usuario_id" ) );
                config.setDuracao( rs.getInt( "duracao" ) );
                config.setClienteId( rs.getInt( "cliente_id" ) );
                lista.add( config );
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao listar: " + e.getMessage() );
        }
        return lista;
    }

    // 🔹 Verificar se o cliente já possui uma configuração para um produto
    public boolean existeConfiguracaoDoCliente( int clienteId, int produtoId )
    {
        String sql = "SELECT COUNT(*) FROM configuracao_mes_comeco WHERE cliente_id = ? AND produto_id = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, clienteId );
            ps.setInt( 2, produtoId );
            ResultSet rs = ps.executeQuery();
            if ( rs.next() )
            {
                int total = rs.getInt( 1 );
                return total > 0; // Retorna true se já existir
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao verificar configuração: " + e.getMessage() );
        }
        return false;
    }

}
