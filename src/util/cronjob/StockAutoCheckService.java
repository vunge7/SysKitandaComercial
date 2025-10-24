/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.cronjob;


import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;

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
        this.conn = conn;
    }

    public void verificarOuSalvarStockDiario() throws SQLException
    {
        LocalDate hoje = LocalDate.now();

        // Verifica se já existe registo para hoje
        String checkSql = "SELECT COUNT(*) FROM stock_diario WHERE data_registro = ?";
        try ( PreparedStatement checkStmt = conn.prepareStatement( checkSql ) )
        {
            checkStmt.setDate( 1, Date.valueOf( hoje ) );

            ResultSet rs = checkStmt.executeQuery();
            if ( rs.next() && rs.getInt( 1 ) == 0 )
            {
                System.out.println( "🕓 Nenhum stock diário encontrado para " + hoje + ". Gravando agora..." );
                salvarStockDiario();
            }
            else
            {
                System.out.println( "✅ Stock diário de hoje já está registado." );
            }
        }
    }

    private void salvarStockDiario() throws SQLException
    {
        String sql
                = "INSERT INTO stock_diario (produto_id, quantidade, data_registro, armazem_id) "
                + "SELECT "
                + "    s.cod_produto_codigo AS produto_id, "
                + "    s.quantidade_existente AS quantidade, "
                + "    CURDATE() AS data_registro, "
                + "    s.cod_armazem AS armazem_id "
                + "FROM tb_stock s "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 FROM stock_diario sd "
                + "    WHERE sd.data_registro = CURDATE() "
                + "      AND sd.produto_id = s.cod_produto_codigo "
                + "      AND sd.armazem_id = s.cod_armazem "
                + ")";

        try ( PreparedStatement stmt = conn.prepareStatement( sql ) )
        {
            int linhas = stmt.executeUpdate();
            System.out.println( "📦 Stock diário salvo com sucesso! Registros adicionados: " + linhas );
        }
    }

}
