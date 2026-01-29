/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.cronjob;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import util.BDConexao;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 24/out/2025
 * @lastModified 24/out/2025
 */
public class StockAutoCheckService
{

    private Connection conn;

    public StockAutoCheckService( Connection conn )
    {
//        this.conn = conn;
        this.conn = new BDConexao().getConnectionAtiva();
    }

    public void verificarOuSalvarStockDiario() throws SQLException
    {
        LocalDate hoje = LocalDate.now();

        // 🔍 Descobre a última data registrada
        String lastDateSql = "SELECT MAX(data_registro) FROM stock_diario";
        LocalDate ultimaData = null;

        try ( Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery( lastDateSql ) )
        {
            if ( rs.next() && rs.getDate( 1 ) != null )
            {
                ultimaData = rs.getDate( 1 ).toLocalDate();
            }
        }

        // 🗓️ Se nunca houve registros, começa do dia atual
        if ( ultimaData == null )
        {
            ultimaData = hoje.minusDays( 1 );
        }

        // ⏳ Preenche todos os dias até hoje
        LocalDate dataAtual = ultimaData.plusDays( 1 );
        while ( !dataAtual.isAfter( hoje ) )
        {
            if ( !existeRegistroParaData( dataAtual ) )
            {
                System.out.println( "🕓 Gerando stock diário para " + dataAtual + "..." );
                salvarStockDiario( dataAtual );
            }
            else
            {
                System.out.println( "✅ Stock diário de " + dataAtual + " já existe." );
            }
            dataAtual = dataAtual.plusDays( 1 );
        }
    }

    private boolean existeRegistroParaData( LocalDate data ) throws SQLException
    {
        String checkSql = "SELECT COUNT(*) FROM stock_diario WHERE data_registro = ?";
        try ( PreparedStatement stmt = conn.prepareStatement( checkSql ) )
        {
            stmt.setDate( 1, Date.valueOf( data ) );
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt( 1 ) > 0;
        }
    }

    private void salvarStockDiario( LocalDate data ) throws SQLException
    {
        String sql = "INSERT INTO stock_diario (produto_id, quantidade, data_registro, armazem_id) "
                + "SELECT "
                + "    s.cod_produto_codigo AS produto_id, "
                + "    s.quantidade_existente AS quantidade, "
                + "    ? AS data_registro, "
                + "    s.cod_armazem AS armazem_id "
                + "FROM tb_stock s "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 FROM stock_diario sd "
                + "    WHERE sd.data_registro = ? "
                + "      AND sd.produto_id = s.cod_produto_codigo "
                + "      AND sd.armazem_id = s.cod_armazem "
                + ")";

        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            stmt.setDate( 1, Date.valueOf( data ) );
            stmt.setDate( 2, Date.valueOf( data ) );

            int linhas = stmt.executeUpdate();
            System.out.println( "📦 Stock diário salvo para " + data + " — Registros adicionados: " + linhas );
        }
        
        conn.close();
    }
    
    
}
