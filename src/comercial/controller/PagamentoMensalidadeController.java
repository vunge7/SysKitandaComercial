/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

import entity.PagamentoMensalidade;
import entity.TbItemVenda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            ps.setInt( 3, pagamento.getMesId() );
            ps.setInt( 4, pagamento.getVendaId() );
            ps.setTimestamp( 5, new Timestamp( pagamento.getDataCadastro().getTime() ) );
            ps.executeUpdate();
            System.out.println( "Pagamento salvo com sucesso!" );
            return true;
        }
        catch ( SQLException e )
        {
            System.err.println( "Erro ao salvar pagamento: " + e.getMessage() );
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
            ps.setInt( 3, pagamento.getMesId() );
            ps.setInt( 4, pagamento.getVendaId() );
            ps.setTimestamp( 5, new Timestamp( pagamento.getDataCadastro().getTime() ) );
            ps.setInt( 6, pagamento.getId() );
            int linhas = ps.executeUpdate();
            if ( linhas > 0 )
            {
                System.out.println( "Pagamento atualizado com sucesso!" );
                return true;
            }
            else
            {
                System.err.println( "Nenhum registro encontrado para atualizar." );
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

    public List<PagamentoMensalidade> listarPorClienteEProduto( int clienteId, int produtoId )
    {
        List<PagamentoMensalidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagamento_mensalidade WHERE cliente_id = ? AND produto_id = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, clienteId );
            ps.setInt( 2, produtoId );
            ResultSet rs = ps.executeQuery();
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

    public boolean removerItem( int vendaId, List<TbItemVenda> itens )
    {
        if ( itens == null || itens.isEmpty() )
        {
            JOptionPane.showMessageDialog( null, "Nenhum item encontrado para remover!" );
            return false;
        }

        String sql = "DELETE FROM pagamento_mensalidade WHERE venda_id = ? AND mes_id = ?";
        boolean sucesso = true;

        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            for ( TbItemVenda item : itens )
            {
                // Extrai produto e mês da designação
                String[] partes = extrairProdutoEMes( item.getDesignacaoItem() );
                if ( partes == null || partes.length < 2 )
                {
                    System.err.println( "Designação inválida: " + item.getDesignacaoItem() );
                    continue;
                }

                String mesNome = partes[ 1 ].trim();

                // Converte o nome do mês em ID numérico (1 = Janeiro, 2 = Fevereiro, etc.)
                int mesId = converterMesParaId( mesNome );
                if ( mesId == -1 )
                {
                    System.err.println( "Mês inválido: " + mesNome );
                    continue;
                }

                ps.setInt( 1, vendaId );
                ps.setInt( 2, mesId );
                ps.addBatch(); // adiciona à execução em lote
            }

            ps.executeBatch(); // executa todos os deletes de uma vez

        }
        catch ( SQLException e )
        {
            sucesso = false;
             System.err.println( "Erro ao remover itens: " + e.getMessage() );
        }

        if ( sucesso )
        {
            System.err.println( "Itens removidos com sucesso!" );
        }

        return sucesso;
    }

    private String[] extrairProdutoEMes( String designacao )
    {
        if ( designacao == null )
        {
            return null;
        }
        Pattern p = Pattern.compile( "^(.*?)#(.*)$" ); // grupo 1: tudo antes do #, grupo2: tudo depois
        Matcher m = p.matcher( designacao );
        if ( !m.find() )
        {
            return null;
        }
        String produto = m.group( 1 ).trim();
        String mes = m.group( 2 ).trim();
        return new String[]
        {
            produto, mes
        };
    }

    private int converterMesParaId( String mesNome )
    {
        mesNome = mesNome.toLowerCase();
        switch ( mesNome )
        {
            case "janeiro":
                return 1;
            case "fevereiro":
                return 2;
            case "março":
            case "marco":
                return 3;
            case "abril":
                return 4;
            case "maio":
                return 5;
            case "junho":
                return 6;
            case "julho":
                return 7;
            case "agosto":
                return 8;
            case "setembro":
                return 9;
            case "outubro":
                return 10;
            case "novembro":
                return 11;
            case "dezembro":
                return 12;
            default:
                return -1;
        }
    }

}
