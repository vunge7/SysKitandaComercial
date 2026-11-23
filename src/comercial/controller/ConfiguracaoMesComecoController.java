/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

import comercial.mensalidade.Mensalidades;
import entity.ConfiguracaoMesComeco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Contratos de mensalidade (serviço + mês/ano de começo + duração) de cada
 * cliente. Duração 0 = contínuo, sem data de fim.
 *
 * As mensagens de sucesso ficam com quem chama (antes apareciam duas vezes);
 * aqui só se avisa dos erros.
 *
 * @author Engº
 * @created 4/nov/2025
 * @lastModified 22/nov/2026
 */
public class ConfiguracaoMesComecoController
{

    private final Connection conexao;

    public ConfiguracaoMesComecoController( Connection conexao )
    {
        this.conexao = conexao;
        Mensalidades.garantirEstrutura();
    }

    // 🔹 Inserir nova configuração
    public boolean salvar( ConfiguracaoMesComeco config )
    {
        String sql = "INSERT INTO configuracao_mes_comeco (data_cadastro, mes_id, produto_id, usuario_id, duracao, cliente_id, ano_inicio, dia_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setTimestamp( 1, new Timestamp( config.getDataCadastro().getTime() ) );
            ps.setInt( 2, config.getMesId() );
            ps.setInt( 3, config.getProdutoId() );
            ps.setInt( 4, config.getUsuarioId() );
            ps.setInt( 5, config.getDuracao() );
            ps.setInt( 6, config.getClienteId() );
            ps.setInt( 7, anoOuCorrente( config ) );
            ps.setInt( 8, Math.max( 0, Math.min( 31, config.getDiaPagamento() ) ) );
            ps.executeUpdate();
            return true;
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao salvar: " + e.getMessage() );
            return false;
        }
    }

    // 🔹 Atualizar configuração existente (a data de cadastro mantém-se)
    public boolean atualizar( ConfiguracaoMesComeco config )
    {
        String sql = "UPDATE configuracao_mes_comeco SET data_cadastro=data_cadastro, mes_id=?, produto_id=?, usuario_id=?, duracao=?, cliente_id=?, ano_inicio=?, dia_pagamento=? WHERE id=?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, config.getMesId() );
            ps.setInt( 2, config.getProdutoId() );
            ps.setInt( 3, config.getUsuarioId() );
            ps.setInt( 4, config.getDuracao() );
            ps.setInt( 5, config.getClienteId() );
            ps.setInt( 6, anoOuCorrente( config ) );
            ps.setInt( 7, Math.max( 0, Math.min( 31, config.getDiaPagamento() ) ) );
            ps.setInt( 8, config.getId() );
            return ps.executeUpdate() > 0;
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
            return ps.executeUpdate() > 0;
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
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return ler( rs );
                }
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar: " + e.getMessage() );
        }
        return null;
    }

    /** O contrato do cliente para o serviço (o mais recente, se houver vários); null se não houver. */
    public ConfiguracaoMesComeco buscarDoCliente( int clienteId, int produtoId )
    {
        String sql = "SELECT * FROM configuracao_mes_comeco WHERE cliente_id = ? AND produto_id = ? ORDER BY id DESC LIMIT 1";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, clienteId );
            ps.setInt( 2, produtoId );
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return ler( rs );
                }
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
                lista.add( ler( rs ) );
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao listar: " + e.getMessage() );
        }
        return lista;
    }

    // 🔹 Listar as configurações de um cliente
    public List<ConfiguracaoMesComeco> listarTodos( int clienteId )
    {
        List<ConfiguracaoMesComeco> lista = new ArrayList<>();
        String sql = "SELECT * FROM configuracao_mes_comeco WHERE cliente_id = ? ORDER BY id";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, clienteId );
            try ( ResultSet rs = ps.executeQuery() )
            {
                while ( rs.next() )
                {
                    lista.add( ler( rs ) );
                }
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
            try ( ResultSet rs = ps.executeQuery() )
            {
                if ( rs.next() )
                {
                    return rs.getInt( 1 ) > 0;
                }
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao verificar configuração: " + e.getMessage() );
        }
        return false;
    }

    private static ConfiguracaoMesComeco ler( ResultSet rs ) throws SQLException
    {
        ConfiguracaoMesComeco config = new ConfiguracaoMesComeco();
        config.setId( rs.getInt( "id" ) );
        config.setDataCadastro( rs.getTimestamp( "data_cadastro" ) );
        config.setMesId( rs.getInt( "mes_id" ) );
        config.setProdutoId( rs.getInt( "produto_id" ) );
        config.setUsuarioId( rs.getInt( "usuario_id" ) );
        config.setDuracao( rs.getInt( "duracao" ) );
        config.setClienteId( rs.getInt( "cliente_id" ) );
        int ano = rs.getInt( "ano_inicio" );
        if ( ano <= 0 && config.getDataCadastro() != null )
        {
            ano = comercial.mensalidade.Periodo.de( config.getDataCadastro() ).getAno();
        }
        config.setAnoInicio( ano );
        try
        {
            config.setDiaPagamento( rs.getInt( "dia_pagamento" ) );
        }
        catch ( SQLException semColuna )
        {
            config.setDiaPagamento( 0 );
        }
        return config;
    }

    private static int anoOuCorrente( ConfiguracaoMesComeco config )
    {
        return config.getAnoInicio() > 0 ? config.getAnoInicio()
                : comercial.mensalidade.Periodo.actual().getAno();
    }

}
