/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.ServicoRetencao;
import entity.Moeda;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;
import util.MetodosUtil;

/**
 *
 * @author Domingos Dala Vunge
 */
public class ServicosRetencaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public ServicosRetencaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        ServicoRetencao mesas = ( ServicoRetencao ) object;
        String INSERT = "INSERT INTO servico_retencao( fk_produto , fk_retencao"
                + ")"
                + " VALUES("
                + mesas.getFkProduto().getCodigo() + ", "
                + mesas.getFkRetencao().getPkRetencao()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int pk_servico_retencao )
    {
        String DELETE = "DELETE FROM servico_retencao WHERE pk_servico_retencao = " + pk_servico_retencao;
        return conexao.executeUpdate( DELETE );
    }

    public boolean eliminarByIdProduto( int pk_produto )
    {
        String DELETE = "DELETE FROM servico_retencao WHERE fk_produto = " + pk_produto;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<ServicoRetencao> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM servico_retencao ORDER BY pk_servico_retencao ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<ServicoRetencao> list = new ArrayList<>();
        ServicoRetencao servico_retencao;
        try
        {
            while ( result.next() )
            {
                servico_retencao = new ServicoRetencao();
//                servico_retencao.setValor( result.getDouble( "valor" ) );
//                servico_retencao.setDataHora( result.getDate( "data_hora" ) );
//                servico_retencao.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
                list.add( servico_retencao );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Vector<String> getVector()
    {

        Vector<String> vector = new Vector<>();

        return vector;

    }

    @Override
    public Object findById( int pk_servico_retencao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM servico_retencao WHERE pk_servico_retencao = " + pk_servico_retencao;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        ServicoRetencao servico_retencao = null;
        try
        {
            if ( result.next() )
            {
                servico_retencao = new ServicoRetencao();
//                servico_retencao.setValor( result.getDouble( "valor" ) );
//                servico_retencao.setDataHora( result.getDate( "data_hora" ) );
//                servico_retencao.setFkMoeda( new Moeda( result.getInt( "fk_moeda" ) ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return servico_retencao;

    }

    public double getTaxaByIdProduto( int pkProduto )
    {
        String sql = "SELECT r.taxa AS taxa FROM servico_retencao p "
                + "INNER JOIN retencao r ON r.pk_retencao = p.fk_retencao WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( sql );
        double taxa = 0;
        try
        {
            if ( result.next() )
            {
                taxa = result.getDouble( "taxa" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return taxa;

    }

    public String getRetensaoByIdProduto( int pkProduto )
    {
        String FIND_BY_CODIGO = "SELECT fk_retencao AS retencao FROM servico_retencao s "
                + "WHERE s.fk_produto = " + pkProduto;
//                + "INNER JOIN produtos_motivos_isensao m ON m.pk_produtos_motivos_isensao = p.fk_produtos_motivos_isensao WHERE p.fk_produto = " + pkProduto;
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        String regime = "";
        try
        {
            if ( result.next() )
            {
                regime = result.getString( "retencao" );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return regime;

    }
    
//        public boolean exist_retencao( int pkProduto )
//    {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery( "SELECT s FROM ServicoRetencao s WHERE s.fkProduto.codigo = :pkProduto AND s.fkRetencao.taxa <> 0" )
//                .setParameter( "pkProduto", pkProduto );
//        return !query.getResultList().isEmpty();
//    }

    public boolean existeRetencao( int codProduto )
    {
        String query = "SELECT * FROM servico_retencao sr INNER JOIN retencao r ON r.pk_retencao = sr.fk_retencao WHERE fk_produto  = " + codProduto + " AND r.taxa <> 0 ";
//                String sql = "SELECT r.taxa AS taxa FROM servico_retencao p "
//                + "INNER JOIN retencao r ON r.pk_retencao = p.fk_retencao WHERE p.fk_produto = " + pkProduto;
        ResultSet rs = conexao.executeQuery( query );
        try
        {
            return rs.next();
        }
        catch ( SQLException e )
        {
        }
        return false;
    }

}
