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
        comercial.mensalidade.Mensalidades.garantirEstrutura();
    }

    // 🔹 Salvar novo pagamento
    public boolean salvar( PagamentoMensalidade pagamento )
    {
        String sql = "INSERT INTO pagamento_mensalidade (produto_id, cliente_id, mes_id, venda_id, data_cadastro, ano) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setInt( 1, pagamento.getProdutoId() );
            ps.setInt( 2, pagamento.getClienteId() );
            ps.setInt( 3, pagamento.getMesId() );
            ps.setInt( 4, pagamento.getVendaId() );
            ps.setTimestamp( 5, new Timestamp( pagamento.getDataCadastro().getTime() ) );
            // quem ainda não indica o ano (ecrãs antigos) paga o mês do ano corrente
            ps.setInt( 6, pagamento.getAno() > 0 ? pagamento.getAno() : comercial.mensalidade.Periodo.actual().getAno() );
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
                p.setAno( rs.getInt( "ano" ) );
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
                p.setAno( rs.getInt( "ano" ) );
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
                p.setAno( rs.getInt( "ano" ) );
                lista.add( p );
            }
        }
        catch ( SQLException e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao listar pagamentos: " + e.getMessage() );
        }
        return lista;
    }

    /**
     * Anulação da venda: os meses que ela pagou voltam a ficar por pagar.
     * O período (mês/ano) sai da designação do item ("... #Março/2026"); as
     * linhas antigas só com o mês apagam o mês dessa venda em qualquer ano.
     */
    public boolean removerItem( int vendaId, List<TbItemVenda> itens )
    {
        if ( itens == null || itens.isEmpty() )
        {
            return false;
        }

        comercial.mensalidade.MultasMensalidade.anular( conexao, vendaId );
        String comAno = "DELETE FROM pagamento_mensalidade WHERE venda_id = ? AND mes_id = ? AND (ano = ? OR ano = 0)";
        String semAno = "DELETE FROM pagamento_mensalidade WHERE venda_id = ? AND mes_id = ?";
        boolean sucesso = true;

        try ( PreparedStatement psAno = conexao.prepareStatement( comAno );
              PreparedStatement psMes = conexao.prepareStatement( semAno ) )
        {
            for ( TbItemVenda item : itens )
            {
                String designacao = item.getDesignacaoItem();
                comercial.mensalidade.Periodo periodo = comercial.mensalidade.Periodo.daDesignacao( designacao );
                if ( periodo == null )
                {
                    continue; // não é mensalidade
                }
                boolean temAno = designacao.substring( designacao.lastIndexOf( '#' ) + 1 ).matches( ".*\\d{4}\\s*$" );
                if ( temAno )
                {
                    psAno.setInt( 1, vendaId );
                    psAno.setInt( 2, periodo.getMes() );
                    psAno.setInt( 3, periodo.getAno() );
                    psAno.addBatch();
                }
                else
                {
                    psMes.setInt( 1, vendaId );
                    psMes.setInt( 2, periodo.getMes() );
                    psMes.addBatch();
                }
            }
            psAno.executeBatch();
            psMes.executeBatch();
        }
        catch ( SQLException e )
        {
            sucesso = false;
            System.err.println( "Erro ao remover itens: " + e.getMessage() );
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
