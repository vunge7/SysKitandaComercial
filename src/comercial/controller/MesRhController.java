/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comercial.controller;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 11/nov/2025
 * @lastModified 11/nov/2025
 */
/*
 * @author Domingos Dala
 */
import entity.PagamentoMensalidade;
import entity.TbMesRh;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MesRhController
{

    private Connection conexao;

    public MesRhController( Connection conexao )
    {
        this.conexao = conexao;
    }

    // Buscar todos os meses
    public List<TbMesRh> buscaTodos()
    {
        List<TbMesRh> lista = new ArrayList<>();
        String sql = "SELECT pk_mes_rh, descricao FROM tb_mes_rh ORDER BY pk_mes_rh ASC";

        try ( PreparedStatement ps = conexao.prepareStatement( sql ); ResultSet rs = ps.executeQuery() )
        {

            while ( rs.next() )
            {
                TbMesRh mes = new TbMesRh();
                mes.setPkMesRh( rs.getInt( "pk_mes_rh" ) );
                mes.setDescricao( rs.getString( "descricao" ) );
                lista.add( mes );
            }

        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar meses: " + e.getMessage() );
        }

        return lista;
    }

    public Vector<String> getVector()
    {
        Vector<String> lista = new Vector<>();

        String FIND_ALL = "SELECT descricao FROM tb_mes_rh";

        try ( PreparedStatement ps = conexao.prepareStatement( FIND_ALL ); ResultSet rs = ps.executeQuery() )
        {

            while ( rs.next() )
            {
                lista.add( rs.getString( "descricao" ) );
            }

        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar produtos: " + e.getMessage() );
        }

        return lista;
    }

    // Buscar descrição pelo ID do mês
    public String getDescricaoByIdMes( long pkMesRh )
    {
        String sql = "SELECT descricao FROM tb_mes_rh WHERE pk_mes_rh = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setLong( 1, pkMesRh );
            ResultSet rs = ps.executeQuery();

            if ( rs.next() )
            {
                return rs.getString( "descricao" );
            }

        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar descrição: " + e.getMessage() );
        }
        return "";
    }

    // Buscar ID do mês pela descrição
    public int getIdByDescricao( String descricao )
    {
        String sql = "SELECT pk_mes_rh FROM tb_mes_rh WHERE descricao = ?";
        try ( PreparedStatement ps = conexao.prepareStatement( sql ) )
        {
            ps.setString( 1, descricao );
            ResultSet rs = ps.executeQuery();

            if ( rs.next() )
            {
                return rs.getInt( "pk_mes_rh" );
            }

        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao buscar ID do mês: " + e.getMessage() );
        }
        return 0;
    }

//    // Carregar meses pagos e por pagar
//    public void carregarMesesPagosEPorPagar( Connection conexao, int clienteId, int produtoId,
//            JTable tabelaMesesPagos, JComboBox<String> cmbMesesPorPagar )
//    {
//        try
//        {
//            PagamentoMensalidadeController pagamentoController = new PagamentoMensalidadeController( conexao );
//            List<TbMesRh> todosMeses = buscaTodos();
//
//            List<PagamentoMensalidade> pagamentos = pagamentoController.listarPorClienteEProduto( clienteId, produtoId );
//
//            // IDs dos meses pagos
//            List<Integer> mesesPagosIds = new ArrayList<>();
//            for ( PagamentoMensalidade pag : pagamentos )
//            {
//                if ( pag.getClienteId() == clienteId && pag.getProdutoId() == produtoId )
//                {
//                    mesesPagosIds.add( (int) pag.getMesId() );
//                }
//            }
//
//            // Preparar tabela
//            DefaultTableModel model = (DefaultTableModel) tabelaMesesPagos.getModel();
//            if ( model.getColumnCount() == 0 )
//            {
//                model.addColumn( "Cod." );
//                model.addColumn( "Meses" );
//            }
//
//            model.setRowCount( 0 );
//            cmbMesesPorPagar.removeAllItems();
//
//            System.out.println( "========== DEBUG MÊSES ==========" );
//            System.out.println( "Meses pagos IDs: " + mesesPagosIds );
//
//            for ( TbMesRh mes : todosMeses )
//            {
//                int idMes = mes.getPkMesRh().intValue();
//                String descricao = mes.getDescricao();
//                boolean pago = mesesPagosIds.contains( idMes );
//
//                if ( pago )
//                {
//                    model.addRow( new Object[]
//                    {
//                        idMes, descricao
//                    } );
//                }
//                else
//                {
//                    cmbMesesPorPagar.addItem( descricao );
//                }
//            }
//
//            tabelaMesesPagos.setModel( model );
//
//        }
//        catch ( Exception e )
//        {
//            JOptionPane.showMessageDialog( null, "Erro ao carregar meses: " + e.getMessage() );
//            e.printStackTrace();
//        }
//    }
    public void carregarMesesPagosEPorPagar( Connection conexao, int clienteId, int produtoId,
            JTable tabelaMesesPagos, JComboBox<String> cmbMesesPorPagar )
    {
        try
        {
            PagamentoMensalidadeController pagamentoController = new PagamentoMensalidadeController( conexao );
            List<TbMesRh> todosMeses = buscaTodos(); // lista completa dos meses (1 a 12)

            // Buscar configuração
            String sql = "SELECT mes_id, duracao FROM configuracao_mes_comeco WHERE cliente_id = ? AND produto_id = ?";
            int mesInicio = 1;
            int duracao = 12;

            try ( PreparedStatement pst = conexao.prepareStatement( sql ) )
            {
                pst.setInt( 1, clienteId );
                pst.setInt( 2, produtoId );
                ResultSet rs = pst.executeQuery();

                if ( rs.next() )
                {
                    mesInicio = rs.getInt( "mes_id" );
                    duracao = rs.getInt( "duracao" );
                }
            }

            // Calcular intervalo de meses válidos
            List<Integer> mesesValidos = new ArrayList<>();
            for ( int i = 0; i < duracao; i++ )
            {
                int mesCalculado = ( ( mesInicio - 1 + i ) % 12 ) + 1; // garante que volte para janeiro após dezembro
                mesesValidos.add( mesCalculado );
            }

            // Buscar pagamentos existentes
            List<PagamentoMensalidade> pagamentos = pagamentoController.listarPorClienteEProduto( clienteId, produtoId );
            List<Integer> mesesPagosIds = new ArrayList<>();
            for ( PagamentoMensalidade pag : pagamentos )
            {
                if ( pag.getClienteId() == clienteId && pag.getProdutoId() == produtoId )
                {
                    mesesPagosIds.add( (int) pag.getMesId() );
                }
            }

            // Preparar tabela
            DefaultTableModel model = (DefaultTableModel) tabelaMesesPagos.getModel();
            if ( model.getColumnCount() == 0 )
            {
                model.addColumn( "Cod." );
                model.addColumn( "Meses" );
            }

            model.setRowCount( 0 );
            cmbMesesPorPagar.removeAllItems();

            System.out.println( "========== DEBUG MÊSES ==========" );
            System.out.println( "Mes inicial: " + mesInicio + " | Duração: " + duracao );
            System.out.println( "Meses válidos: " + mesesValidos );
            System.out.println( "Meses pagos IDs: " + mesesPagosIds );

            // Preencher tabela e combobox com base nos meses válidos
            for ( TbMesRh mes : todosMeses )
            {
                int idMes = mes.getPkMesRh().intValue();
                String descricao = mes.getDescricao();

                if ( mesesValidos.contains( idMes ) )
                {
                    boolean pago = mesesPagosIds.contains( idMes );

                    if ( pago )
                    {
                        model.addRow( new Object[]
                        {
                            idMes, descricao
                        } );
                    }
                    else
                    {
                        cmbMesesPorPagar.addItem( descricao );
                    }
                }
            }

            tabelaMesesPagos.setModel( model );
        }
        catch ( Exception e )
        {
            JOptionPane.showMessageDialog( null, "Erro ao carregar meses: " + e.getMessage() );
            e.printStackTrace();
        }
    }

}
