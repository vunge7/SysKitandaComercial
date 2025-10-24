/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;


import java.sql.Connection;
import entity.TbDadosInstituicao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class DadosInstituicaoController implements EntidadeFactory
{

    private BDConexao conexao;

    public DadosInstituicaoController( BDConexao conexao )
    {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar( Object object )
    {
        TbDadosInstituicao dadosInstiuicao = (TbDadosInstituicao) object;
        String INSERT = "INSERT INTO tb_dados_instituicao( nome , senha , status , dataNascimento , telefone, email, endereco, "
                + ")"
                + " VALUES("
                + "'" + dadosInstiuicao.getNome() + "' , "
                //                + "'" + dadosInstiuicao.getMorada() + "' , "
                + "'" + dadosInstiuicao.getTelefone() + "' , "
                //                + "'" + dadosInstiuicao.getNif() + "' , "
                + "'" + dadosInstiuicao.getEmail()
                + " ) ";

        return conexao.executeUpdate( INSERT );

    }

    @Override
    public boolean actualizar( Object object )
    {
        return true;
    }

    @Override
    public boolean eliminar( int codigo )
    {
        String DELETE = "DELETE FROM tb_dados_instituicao WHERE codigo = " + codigo;
        return conexao.executeUpdate( DELETE );
    }

    @Override
    public List<TbDadosInstituicao> listarTodos()
    {

        String FIND_ALL = "SELECT * FROM tb_dados_instituicao ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        List<TbDadosInstituicao> list = new ArrayList<>();
        TbDadosInstituicao dadosIntituicao;
        try
        {

            while ( result.next() )
            {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao( result.getInt( "idDadosInsitiuicao" ) );
                dadosIntituicao.setNome( result.getString( "nome" ) );
                dadosIntituicao.setDocpadrao( result.getString( "docpadrao" ) );
                dadosIntituicao.setNegocio( result.getString( "negocio" ) );
                dadosIntituicao.setConfigArmazens( result.getString( "config_armazens" ) );
                dadosIntituicao.setTranstorno( result.getString( "transtorno" ) );
                dadosIntituicao.setDescontoFinanceiro( result.getString( "desconto_financeiro" ) );
                dadosIntituicao.setAnoEconomico( result.getString( "ano_economico" ) );
                dadosIntituicao.setVizualisarStock( result.getString( "vizualisar_stock" ) );
                dadosIntituicao.setNumeroVias( result.getInt( "numero_vias" ) );
                dadosIntituicao.setImpressora( result.getString( "impressora" ) );
                dadosIntituicao.setFoco( result.getString( "foco" ) );
                dadosIntituicao.setDesactivarvias( result.getString( "desactivarvias" ) );
                dadosIntituicao.setPrazoProforma( result.getInt( "prazo_proforma" ) );
                dadosIntituicao.setTipoFechoCaixa( result.getString( "tipo_fecho_caixa" ) );
                dadosIntituicao.setEnviarEmail( result.getString( "enviar_email" ) );
                list.add( dadosIntituicao );
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
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        vector.add( 0, "--Seleccione o Cliente--" );

        return vector;
    }

    public Vector<String> getVectorExecptoConsumidorFinal()
    {
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao  WHERE codigo <> 1 ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        vector.add( 0, "--Seleccione o Cliente--" );

        return vector;
    }

    public Vector<String> getVectorByIinciais( String prefixo )
    {
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao  WHERE  nome LIKE '%" + prefixo + "%'  ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery( FIND_ALL );
        Vector<String> vector = new Vector();
        try
        {
            while ( result.next() )
            {
                vector.add( result.getString( "nome" ) );
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return vector;
    }

    @Override
    public Object findById( int codigo )
    {

        String FIND__BY_CODIGO = "SELECT * FROM tb_dados_instituicao WHERE idDadosInsitiuicao = " + codigo;
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbDadosInstituicao dadosIntituicao = null;
        try
        {

            if ( result.next() )
            {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao( result.getInt( "idDadosInsitiuicao" ) );
                dadosIntituicao.setNome( result.getString( "nome" ) );
                dadosIntituicao.setDocpadrao( result.getString( "docpadrao" ) );
                dadosIntituicao.setNegocio( result.getString( "negocio" ) );
                dadosIntituicao.setConfigArmazens( result.getString( "config_armazens" ) );
                dadosIntituicao.setTranstorno( result.getString( "transtorno" ) );
                dadosIntituicao.setDescontoFinanceiro( result.getString( "desconto_financeiro" ) );
                dadosIntituicao.setAnoEconomico( result.getString( "ano_economico" ) );
                dadosIntituicao.setVizualisarStock( result.getString( "vizualisar_stock" ) );
                dadosIntituicao.setNumeroVias( result.getInt( "numero_vias" ) );
                dadosIntituicao.setImpressora( result.getString( "impressora" ) );
                dadosIntituicao.setImpressoraCozinha( result.getString( "impressora_cozinha" ) );
                dadosIntituicao.setImpressoraSala( result.getString( "impressora_sala" ) );
                dadosIntituicao.setFoco( result.getString( "foco" ) );
                dadosIntituicao.setRegime( result.getString( "regime" ) );
                dadosIntituicao.setUsarDoisPrecos( result.getString( "usar_dois_precos" ) );
                dadosIntituicao.setDesactivarvias( result.getString( "desactivarvias" ) );
                dadosIntituicao.setTeclado( result.getString( "teclado" ) );
                dadosIntituicao.setPrazoProforma( result.getInt( "prazo_proforma" ) );
                dadosIntituicao.setDesactivarLugares( result.getString( "desactivar_lugares" ) );
                dadosIntituicao.setHoraTerminoVenda( result.getTimestamp( "hora_termino_venda" ) );
                dadosIntituicao.setTipoFechoCaixa( result.getString( "tipo_fecho_caixa" ) );
                dadosIntituicao.setEnviarEmail( result.getString( "enviar_email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return dadosIntituicao;

    }

    public TbDadosInstituicao getLastUsuario()
    {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, c.*  FROM tb_dados_instituicao c";
        ResultSet result = conexao.executeQuery( FIND__BY_CODIGO );
        TbDadosInstituicao dadosIntituicao = null;
        try
        {

            if ( result.next() )
            {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao( result.getInt( "idDadosInsitiuicao" ) );
                dadosIntituicao.setNome( result.getString( "nome" ) );
                dadosIntituicao.setDocpadrao( result.getString( "docpadrao" ) );
                dadosIntituicao.setNegocio( result.getString( "negocio" ) );
                dadosIntituicao.setConfigArmazens( result.getString( "config_armazens" ) );
                dadosIntituicao.setTranstorno( result.getString( "transtorno" ) );
                dadosIntituicao.setDescontoFinanceiro( result.getString( "desconto_financeiro" ) );
                dadosIntituicao.setAnoEconomico( result.getString( "ano_economico" ) );
                dadosIntituicao.setVizualisarStock( result.getString( "vizualisar_stock" ) );
                dadosIntituicao.setNumeroVias( result.getInt( "numero_vias" ) );
                dadosIntituicao.setImpressora( result.getString( "impressora" ) );
                dadosIntituicao.setFoco( result.getString( "foco" ) );
                dadosIntituicao.setDesactivarvias( result.getString( "desactivarvias" ) );
                dadosIntituicao.setPrazoProforma( result.getInt( "prazo_proforma" ) );
                dadosIntituicao.setTipoFechoCaixa( result.getString( "tipo_fecho_caixa" ) );
                dadosIntituicao.setEnviarEmail( result.getString( "enviar_email" ) );

            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return dadosIntituicao;

    }

    public TbDadosInstituicao findByCodigo( int idDadosInsitiuicao )
    {

        String FIND_BY_CODIGO = "SELECT * FROM tb_dados_instituicao WHERE idDadosInsitiuicao = " + idDadosInsitiuicao + "";
        ResultSet result = conexao.executeQuery( FIND_BY_CODIGO );
        TbDadosInstituicao dadosInstituicao = null;
        try
        {

            if ( result.next() )
            {
                dadosInstituicao = new TbDadosInstituicao();
                dadosInstituicao.setIdDadosInsitiuicao( result.getInt( "idDadosInsitiuicao" ) );
                dadosInstituicao.setNome( result.getString( "nome" ) );
                dadosInstituicao.setDocpadrao( result.getString( "docpadrao" ) );
                dadosInstituicao.setNegocio( result.getString( "negocio" ) );
                dadosInstituicao.setConfigArmazens( result.getString( "config_armazens" ) );
                dadosInstituicao.setTranstorno( result.getString( "transtorno" ) );
                dadosInstituicao.setDescontoFinanceiro( result.getString( "desconto_financeiro" ) );
                dadosInstituicao.setAnoEconomico( result.getString( "ano_economico" ) );
                dadosInstituicao.setVizualisarStock( result.getString( "vizualisar_stock" ) );
                dadosInstituicao.setNumeroVias( result.getInt( "numero_vias" ) );
                dadosInstituicao.setImpressora( result.getString( "impressora" ) );
                dadosInstituicao.setImpressoraCozinha( result.getString( "impressora_cozinha" ) );
                dadosInstituicao.setFoco( result.getString( "foco" ) );
                dadosInstituicao.setRegime( result.getString( "regime" ) );
//                dadosInstituicao.setCpReserva(result.getString( "cp_reserva" ) );
                dadosInstituicao.setChaveMestre( result.getString( "chave_mestre" ) );
//                dadosIntituicao.setUsarDoisPrecos(result.getString( "usar_dois_precos" ) );
                dadosInstituicao.setDesactivarvias( result.getString( "desactivarvias" ) );
                dadosInstituicao.setTeclado( result.getString( "teclado" ) );
                dadosInstituicao.setPrazoProforma( result.getInt( "prazo_proforma" ) );
                dadosInstituicao.setTipoFechoCaixa( result.getString( "tipo_fecho_caixa" ) );
                dadosInstituicao.setEnviarEmail( result.getString( "enviar_email" ) );
                dadosInstituicao.setTipoFichaTecnica( result.getString( "tipo_ficha_tecnica" ) );
            }

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return dadosInstituicao;

    }

}
