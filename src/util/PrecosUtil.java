/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import comercial.controller.PrecosController;
import comercial.controller.StoksController;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbUsuario;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 7/set/2025
 * @lastModified 7/set/2025
 */
public class PrecosUtil
{

    public static void actualizarPreco( int idUsuario, int idProduto,
            BigDecimal precoCompra,
            BigDecimal precoVenda,
            PrecosController precosControllerLocal,
            BDConexao conexao )
    {
        System.out.println( "ID PRODUTO: " + idProduto );

        // Função auxiliar para garantir que percentagemGanho não seja null
        BigDecimal ganhoRetalho = BigDecimal.ZERO;

        BigDecimal ganhoGrosso = BigDecimal.ZERO;

        // ---------------- RETALHO ----------------
        TbPreco preco_novo_retalho = new TbPreco();
        preco_novo_retalho.setData( new Date() );
        preco_novo_retalho.setHora( new Date() );
        preco_novo_retalho.setPercentagemGanho( ganhoRetalho );
        preco_novo_retalho.setFkProduto( new TbProduto( idProduto ) );
        preco_novo_retalho.setFkUsuario( new TbUsuario( idUsuario ) );
        preco_novo_retalho.setPrecoCompra( precoCompra );
        preco_novo_retalho.setPrecoVenda( precoVenda );
        preco_novo_retalho.setQtdBaixo( 0 );
        preco_novo_retalho.setQtdAlto( (int) DVML.QTD_DEFAULT - 1 );
        preco_novo_retalho.setPrecoAnterior( 0d );
        preco_novo_retalho.setRetalho( true );

        try
        {
            precosControllerLocal.salvar( preco_novo_retalho );
            System.out.println( "Preco de compra/venda retalho atualizado na compra" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao atualizar o preço retalho na compra" );
        }

        // ---------------- GROSSO ----------------
        try
        {
            TbPreco preco_novo_grosso = new TbPreco();
            preco_novo_grosso.setData( new Date() );
            preco_novo_grosso.setHora( new Date() );
            preco_novo_grosso.setPercentagemGanho( ganhoRetalho );
            preco_novo_grosso.setFkProduto( new TbProduto( idProduto ) );
            preco_novo_grosso.setFkUsuario( new TbUsuario( idUsuario ) );
            preco_novo_grosso.setPrecoCompra( precoCompra );
            preco_novo_grosso.setPrecoVenda( precoVenda );
            preco_novo_grosso.setQtdBaixo( (int) DVML.QTD_DEFAULT );
            preco_novo_grosso.setQtdAlto( 214748364 );
            preco_novo_grosso.setPrecoAnterior( 0d );
            preco_novo_grosso.setRetalho( true );

            precosControllerLocal.salvar( preco_novo_grosso );
            System.out.println( "Preco de compra/venda grosso atualizado na compra" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.err.println( "Falha ao atualizar o preço grosso na compra" );
        }
    }

    public static BigDecimal getPrecoCompraMedio(
            int codProduto,
            BigDecimal qtdEntrada,
            PrecosController precosController,
            StoksController stocksController,
            BigDecimal precoNovo,
            int idArmazem
    )
    {
        // Pega o último preço de compra do produto
        TbPreco preco = (TbPreco) precosController.getLastIdPrecoByIdProdutos( codProduto );
        BigDecimal precoActual = ( preco != null ) ? preco.getPrecoCompra() : BigDecimal.ZERO;

        // Pega a quantidade atual em estoque
        BigDecimal qtdExistenteActual = new BigDecimal( stocksController.getQuantidadeProduto( codProduto, idArmazem ) );

        // Debug
        System.err.println( "Preco Antigo: " + precoActual );
        System.err.println( "Preco Novo: " + precoNovo );
        System.err.println( "Qtd Existente: " + qtdExistenteActual );
        System.err.println( "Qtd Entrada: " + qtdEntrada );

        // Se não há estoque antigo, o preço médio é simplesmente o preço do novo lote
        if ( qtdExistenteActual.compareTo( BigDecimal.ZERO ) == 0 )
        {
            System.err.println( "Preco Médio (sem estoque anterior): " + precoNovo );
            return precoNovo;
        }

        // Calcula preço médio ponderado
        BigDecimal totalQuantidade = qtdExistenteActual.add( qtdEntrada );
        BigDecimal precoMedio = precoActual.multiply( qtdExistenteActual )
                .add( precoNovo.multiply( qtdEntrada ) )
                .divide( totalQuantidade, 4, RoundingMode.HALF_UP ); // 4 casas decimais

        System.err.println( "Preco Médio: " + precoMedio );

        return precoMedio;
    }

    public static BigDecimal calculaPrecoMedio( int produtoId, BigDecimal precoNovo, int qtdEntrar )
    {
        BigDecimal novoPrecoMedio = BigDecimal.ZERO;

        BDConexao conexaoLocal = BDConexao.getInstancia();
        String sql = "SELECT fn_calcula_preco_medio(?, ?, ?) AS preco_medio";

        try ( Connection conn = conexaoLocal.getConnection(); PreparedStatement stmt = conn.prepareStatement( sql ) )
        {

            stmt.setInt( 1, produtoId );
            stmt.setBigDecimal( 2, precoNovo );
            stmt.setInt( 3, qtdEntrar );

            ResultSet rs = stmt.executeQuery();
            if ( rs.next() )
            {
                novoPrecoMedio = rs.getBigDecimal( "preco_medio" );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        conexaoLocal.close();

        return novoPrecoMedio;
    }

}
