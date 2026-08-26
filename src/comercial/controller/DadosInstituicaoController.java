/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comercial.controller;

import java.sql.Connection;
import entity.TbDadosInstituicao;
import java.sql.PreparedStatement;
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
public class DadosInstituicaoController implements EntidadeFactory {

    private BDConexao conexao;

    public DadosInstituicaoController(BDConexao conexao) {
        this.conexao = conexao;
    }

    @Override
    public boolean salvar(Object object) {

        TbDadosInstituicao dados = (TbDadosInstituicao) object;

        String INSERT = "INSERT INTO tb_dados_instituicao ("
                + "nome, telefone, enderecos, email, nif, cont, "
                + "conta_bancaria1, conta_bancaria2, conta_bancaria3, conta_bancaria4, "
                + "conta_bancaria5, conta_bancaria6, director_geral, numero_vias, "
                + "impressora, foco, docpadrao, desactivarvias, desconto_financeiro, "
                + "ano_economico, vizualisar_stock, transtorno, correio_caixa, negocio, "
                + "obs_ft, prazo_ft, local_carregamento, conta_bancaria7, conta_bancaria8, "
                + "slogan, obs_devolucao, teclado, data_licenca, regime, "
                + "regime_contrato, config_armazens, usar_dois_precos, impressora_cozinha, "
                + "chave_mestre, data_fecho, tesouraria, rh, comercial, janela_servico, "
                + "impressora_sala, prazo_proforma, desactivar_lugares, tipo_fecho_caixa, "
                + "enviar_email, stock_consulta, tipo_ficha_tecnica, posto, "
                + "impressora_caixa, segundo_monitor, alterar_preco "
//                + "hora_comeco_venda, hora_termino_venda"
                + ") VALUES ("
                + "'" + dados.getNome() + "', "
                + "'" + dados.getTelefone() + "', "
                + "'" + dados.getEnderecos() + "', "
                + "'" + dados.getEmail() + "', "
                + "'" + dados.getNif() + "', "
                + dados.getCont() + ", "
                + "'" + dados.getContaBancaria1() + "', "
                + "'" + dados.getContaBancaria2() + "', "
                + "'" + dados.getContaBancaria3() + "', "
                + "'" + dados.getContaBancaria4() + "', "
                + "'" + dados.getContaBancaria5() + "', "
                + "'" + dados.getContaBancaria6() + "', "
                + "'" + dados.getDirectorGeral() + "', "
                + dados.getNumeroVias() + ", "
                + "'" + dados.getImpressora() + "', "
                + "'" + dados.getFoco() + "', "
                + "'" + dados.getDocpadrao() + "', "
                + "'" + dados.getDesactivarvias() + "', "
                + "'" + dados.getDescontoFinanceiro() + "', "
                + "'" + dados.getAnoEconomico() + "', "
                + "'" + dados.getVizualisarStock() + "', "
                + "'" + dados.getTranstorno() + "', "
                + "'" + dados.getCorreioCaixa() + "', "
                + "'" + dados.getNegocio() + "', "
                + "'" + dados.getObsFt() + "', "
                + "'" + dados.getPrazoFt() + "', "
                + "'" + dados.getLocalCarregamento() + "', "
                + "'" + dados.getContaBancaria7() + "', "
                + "'" + dados.getContaBancaria8() + "', "
                + "'" + dados.getSlogan() + "', "
                + "'" + dados.getObsDevolucao() + "', "
                + "'" + dados.getTeclado() + "', "
                + "'" + MetodosUtil.getDataBanco(dados.getDataLicenca()) + "' , "
                + "'" + dados.getRegime() + "', "
                + "'" + dados.getRegimeContrato() + "', "
                + "'" + dados.getConfigArmazens() + "', "
                + "'" + dados.getUsarDoisPrecos() + "', "
                + "'" + dados.getImpressoraCozinha() + "', "
                + "'" + dados.getChaveMestre() + "', "
                + "'" + MetodosUtil.getDataBanco(dados.getDataFecho()) + "' , "
                + "'" + dados.getTesouraria() + "', "
                + "'" + dados.getRh() + "', "
                + "'" + dados.getComercial() + "', "
                + "'" + dados.getJanelaServico() + "', "
                + "'" + dados.getImpressoraSala() + "', "
                + dados.getPrazoProforma() + ", "
                + "'" + dados.getDesactivarLugares() + "', "
                + "'" + dados.getTipoFechoCaixa() + "', "
                + "'" + dados.getEnviarEmail() + "', "
                + "'" + dados.getStockConsulta() + "', "
                + "'" + dados.getTipoFichaTecnica() + "', "
                + "'" + dados.getPosto() + "', "
                + "'" + dados.getImpressoraCaixa() + "', "
                + "'" + dados.getSegundoMonitor() + "', "
                + "'" + dados.getAlterarPreco() + "'"
//                + "'" + MetodosUtil.getHoraBanco(dados.getHoraComecoVenda()) + "' , "
//                + "'" + MetodosUtil.getHoraBanco(dados.getHoraTerminoVenda()) + "'"
                + ")";

        return conexao.executeUpdate(INSERT);
    }
//    public boolean salvar( Object object )
//    {
//        TbDadosInstituicao dadosInstiuicao = (TbDadosInstituicao) object;
//        String INSERT = "INSERT INTO tb_dados_instituicao( nome , senha , status , dataNascimento , telefone, email, endereco, "
//                + ")"
//                + " VALUES("
//                + "'" + dadosInstiuicao.getNome() + "' , "
//                //                + "'" + dadosInstiuicao.getMorada() + "' , "
//                + "'" + dadosInstiuicao.getTelefone() + "' , "
//                //                + "'" + dadosInstiuicao.getNif() + "' , "
//                + "'" + dadosInstiuicao.getEmail()
//                + " ) ";
//
//        return conexao.executeUpdate( INSERT );
//
//    }

    @Override
    public boolean actualizar(Object object) {

        TbDadosInstituicao dados = (TbDadosInstituicao) object;

        String UPDATE = "UPDATE tb_dados_instituicao SET "
                + "nome = '" + dados.getNome() + "', "
                + "telefone = '" + dados.getTelefone() + "', "
                + "enderecos = '" + dados.getEnderecos() + "', "
                + "email = '" + dados.getEmail() + "', "
                + "nif = '" + dados.getNif() + "', "
                + "cont = " + dados.getCont() + ", "
                + "conta_bancaria1 = '" + dados.getContaBancaria1() + "', "
                + "conta_bancaria2 = '" + dados.getContaBancaria2() + "', "
                + "conta_bancaria3 = '" + dados.getContaBancaria3() + "', "
                + "conta_bancaria4 = '" + dados.getContaBancaria4() + "', "
                + "conta_bancaria5 = '" + dados.getContaBancaria5() + "', "
                + "conta_bancaria6 = '" + dados.getContaBancaria6() + "', "
                + "director_geral = '" + dados.getDirectorGeral() + "', "
                + "numero_vias = " + dados.getNumeroVias() + ", "
                + "impressora = '" + dados.getImpressora() + "', "
                + "foco = '" + dados.getFoco() + "', "
                + "docpadrao = '" + dados.getDocpadrao() + "', "
                + "desactivarvias = '" + dados.getDesactivarvias() + "', "
                + "desconto_financeiro = '" + dados.getDescontoFinanceiro() + "', "
                + "ano_economico = '" + dados.getAnoEconomico() + "', "
                + "vizualisar_stock = '" + dados.getVizualisarStock() + "', "
                + "transtorno = '" + dados.getTranstorno() + "', "
                + "correio_caixa = '" + dados.getCorreioCaixa() + "', "
                + "negocio = '" + dados.getNegocio() + "', "
                + "obs_ft = '" + dados.getObsFt() + "', "
                + "prazo_ft = '" + dados.getPrazoFt() + "', "
                + "local_carregamento = '" + dados.getLocalCarregamento() + "', "
                + "conta_bancaria7 = '" + dados.getContaBancaria7() + "', "
                + "conta_bancaria8 = '" + dados.getContaBancaria8() + "', "
                + "slogan = '" + dados.getSlogan() + "', "
                + "obs_devolucao = '" + dados.getObsDevolucao() + "', "
                + "teclado = '" + dados.getTeclado() + "', "
                + "data_licenca = '" + MetodosUtil.getDataBanco(dados.getDataLicenca()) + "', "
                + "regime = '" + dados.getRegime() + "', "
                + "regime_contrato = '" + dados.getRegimeContrato() + "', "
                + "config_armazens = '" + dados.getConfigArmazens() + "', "
                + "usar_dois_precos = '" + dados.getUsarDoisPrecos() + "', "
                + "impressora_cozinha = '" + dados.getImpressoraCozinha() + "', "
                + "chave_mestre = '" + dados.getChaveMestre() + "', "
                + "data_fecho = '" + MetodosUtil.getDataBanco(dados.getDataFecho()) + "', "
                + "tesouraria = '" + dados.getTesouraria() + "', "
                + "rh = '" + dados.getRh() + "', "
                + "comercial = '" + dados.getComercial() + "', "
                + "janela_servico = '" + dados.getJanelaServico() + "', "
                + "impressora_sala = '" + dados.getImpressoraSala() + "', "
                + "prazo_proforma = " + dados.getPrazoProforma() + ", "
                + "desactivar_lugares = '" + dados.getDesactivarLugares() + "', "
                + "tipo_fecho_caixa = '" + dados.getTipoFechoCaixa() + "', "
                + "enviar_email = '" + dados.getEnviarEmail() + "', "
                + "stock_consulta = '" + dados.getStockConsulta() + "', "
                + "tipo_ficha_tecnica = '" + dados.getTipoFichaTecnica() + "', "
                + "posto = '" + dados.getPosto() + "', "
                + "impressora_caixa = '" + dados.getImpressoraCaixa() + "', "
                + "segundo_monitor = '" + dados.getSegundoMonitor() + "', "
                + "alterar_preco = '" + dados.getAlterarPreco() + "'"
//                + "hora_comeco_venda = '" + MetodosUtil.getHoraBanco(dados.getHoraComecoVenda()) + "', "
//                + "hora_termino_venda = '" + MetodosUtil.getHoraBanco(dados.getHoraTerminoVenda()) + "' "
                + "WHERE idDadosInsitiuicao = " + dados.getIdDadosInsitiuicao();

        return conexao.executeUpdate(UPDATE);

    }

    @Override
    public boolean eliminar(int codigo) {
        String DELETE = "DELETE FROM tb_dados_instituicao WHERE codigo = " + codigo;
        return conexao.executeUpdate(DELETE);
    }

    public static TbDadosInstituicao getDados() {

        TbDadosInstituicao dados = null;

        String sql = "SELECT * FROM tb_dados_instituicao LIMIT 1";

        try (Connection conn = BDConexao.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                dados = new TbDadosInstituicao();

                dados.setIdDadosInsitiuicao(rs.getInt("idDadosInsitiuicao"));
                dados.setNome(rs.getString("nome"));
                dados.setTelefone(rs.getString("telefone"));
                dados.setEnderecos(rs.getString("enderecos"));
                dados.setEmail(rs.getString("email"));
                dados.setNif(rs.getString("nif"));

                // Se quiseres podes mapear mais campos depois
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dados;
    }

    @Override
    public List<TbDadosInstituicao> listarTodos() {

        String FIND_ALL = "SELECT * FROM tb_dados_instituicao ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        List<TbDadosInstituicao> list = new ArrayList<>();
        TbDadosInstituicao dadosIntituicao;
        try {

            while (result.next()) {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao(result.getInt("idDadosInsitiuicao"));
                dadosIntituicao.setNome(result.getString("nome"));
                dadosIntituicao.setTelefone(result.getString("telefone"));
                dadosIntituicao.setEnderecos(result.getString("enderecos"));
                dadosIntituicao.setEmail(result.getString("email"));
                dadosIntituicao.setNif(result.getString("nif"));
                dadosIntituicao.setCont(result.getInt("cont"));

                dadosIntituicao.setContaBancaria1(result.getString("conta_bancaria1"));
                dadosIntituicao.setContaBancaria2(result.getString("conta_bancaria2"));
                dadosIntituicao.setContaBancaria3(result.getString("conta_bancaria3"));
                dadosIntituicao.setContaBancaria4(result.getString("conta_bancaria4"));
                dadosIntituicao.setContaBancaria5(result.getString("conta_bancaria5"));
                dadosIntituicao.setContaBancaria6(result.getString("conta_bancaria6"));
                dadosIntituicao.setContaBancaria7(result.getString("conta_bancaria7"));
                dadosIntituicao.setContaBancaria8(result.getString("conta_bancaria8"));

                dadosIntituicao.setDirectorGeral(result.getString("director_geral"));

                dadosIntituicao.setNumeroVias(result.getInt("numero_vias"));
                dadosIntituicao.setImpressora(result.getString("impressora"));
                dadosIntituicao.setFoco(result.getString("foco"));
                dadosIntituicao.setDocpadrao(result.getString("docpadrao"));
                dadosIntituicao.setDesactivarvias(result.getString("desactivarvias"));
                dadosIntituicao.setDescontoFinanceiro(result.getString("desconto_financeiro"));
                dadosIntituicao.setAnoEconomico(result.getString("ano_economico"));
                dadosIntituicao.setVizualisarStock(result.getString("vizualisar_stock"));
                dadosIntituicao.setTranstorno(result.getString("transtorno"));
                dadosIntituicao.setCorreioCaixa(result.getString("correio_caixa"));
                dadosIntituicao.setNegocio(result.getString("negocio"));
                dadosIntituicao.setObsFt(result.getString("obs_ft"));
                dadosIntituicao.setPrazoFt(result.getString("prazo_ft"));
                dadosIntituicao.setLocalCarregamento(result.getString("local_carregamento"));
                dadosIntituicao.setSlogan(result.getString("slogan"));
                dadosIntituicao.setObsDevolucao(result.getString("obs_devolucao"));
                dadosIntituicao.setTeclado(result.getString("teclado"));
                dadosIntituicao.setDataLicenca(result.getDate("data_licenca"));
                dadosIntituicao.setRegime(result.getString("regime"));
                dadosIntituicao.setRegimeContrato(result.getString("regime_contrato"));
                dadosIntituicao.setConfigArmazens(result.getString("config_armazens"));
                dadosIntituicao.setUsarDoisPrecos(result.getString("usar_dois_precos"));
                dadosIntituicao.setImpressoraCozinha(result.getString("impressora_cozinha"));
                dadosIntituicao.setChaveMestre(result.getString("chave_mestre"));
                dadosIntituicao.setDataFecho(result.getDate("data_fecho"));
                dadosIntituicao.setTesouraria(result.getString("tesouraria"));
                dadosIntituicao.setRh(result.getString("rh"));
                dadosIntituicao.setComercial(result.getString("comercial"));
                dadosIntituicao.setJanelaServico(result.getString("janela_servico"));
                dadosIntituicao.setImpressoraSala(result.getString("impressora_sala"));
                dadosIntituicao.setPrazoProforma(result.getInt("prazo_proforma"));
                dadosIntituicao.setDesactivarLugares(result.getString("desactivar_lugares"));
                dadosIntituicao.setTipoFechoCaixa(result.getString("tipo_fecho_caixa"));
                dadosIntituicao.setEnviarEmail(result.getString("enviar_email"));
                dadosIntituicao.setStockConsulta(result.getString("stock_consulta"));
                dadosIntituicao.setTipoFichaTecnica(result.getString("tipo_ficha_tecnica"));
                dadosIntituicao.setPosto(result.getString("posto"));
                dadosIntituicao.setImpressoraCaixa(result.getString("impressora_caixa"));
                dadosIntituicao.setSegundoMonitor(result.getString("segundo_monitor"));
                dadosIntituicao.setAlterarPreco(result.getString("alterar_preco"));
                dadosIntituicao.setHoraComecoVenda(result.getTime("hora_comeco_venda"));
                dadosIntituicao.setHoraTerminoVenda(result.getTime("hora_termino_venda"));
                list.add(dadosIntituicao);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Vector<String> getVector() {
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        Vector<String> vector = new Vector();
        try {
            while (result.next()) {
                vector.add(result.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vector.add(0, "--Seleccione o Cliente--");

        return vector;
    }

    public Vector<String> getVectorExecptoConsumidorFinal() {
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao  WHERE codigo <> 1 ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        Vector<String> vector = new Vector();
        try {
            while (result.next()) {
                vector.add(result.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vector.add(0, "--Seleccione o Cliente--");

        return vector;
    }

    public Vector<String> getVectorByIinciais(String prefixo) {
        String FIND_ALL = "SELECT nome FROM tb_dados_instituicao  WHERE  nome LIKE '%" + prefixo + "%'  ORDER BY codigo ASC";
        ResultSet result = conexao.executeQuery(FIND_ALL);
        Vector<String> vector = new Vector();
        try {
            while (result.next()) {
                vector.add(result.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vector;
    }

    @Override
    public Object findById(int codigo) {
        TbDadosInstituicao dadosIntituicao = null;

        // ✅ Verificação de segurança: conexão obrigatória
        if (conexao == null || conexao.getConnectionAtiva() == null) {
            System.err.println("[DadosInstituicaoController] ❌ Conexão não inicializada ou inativa!");
            return null;
        }

        String FIND_BY_CODIGO
                = "SELECT * FROM tb_dados_instituicao WHERE idDadosInsitiuicao = " + codigo;

        ResultSet result = null;

        try {
            // ✅ Executa a consulta
            result = conexao.executeQuery(FIND_BY_CODIGO);

            // ✅ Verifica se veio resultado
            if (result != null && result.next()) {
                dadosIntituicao = new TbDadosInstituicao();

                dadosIntituicao.setIdDadosInsitiuicao(result.getInt("idDadosInsitiuicao"));
                dadosIntituicao.setNome(result.getString("nome"));
                dadosIntituicao.setTelefone(result.getString("telefone"));
                dadosIntituicao.setEnderecos(result.getString("enderecos"));
                dadosIntituicao.setEmail(result.getString("email"));
                dadosIntituicao.setNif(result.getString("nif"));
                dadosIntituicao.setCont(result.getInt("cont"));

                dadosIntituicao.setContaBancaria1(result.getString("conta_bancaria1"));
                dadosIntituicao.setContaBancaria2(result.getString("conta_bancaria2"));
                dadosIntituicao.setContaBancaria3(result.getString("conta_bancaria3"));
                dadosIntituicao.setContaBancaria4(result.getString("conta_bancaria4"));
                dadosIntituicao.setContaBancaria5(result.getString("conta_bancaria5"));
                dadosIntituicao.setContaBancaria6(result.getString("conta_bancaria6"));
                dadosIntituicao.setContaBancaria7(result.getString("conta_bancaria7"));
                dadosIntituicao.setContaBancaria8(result.getString("conta_bancaria8"));

                dadosIntituicao.setDirectorGeral(result.getString("director_geral"));

                dadosIntituicao.setNumeroVias(result.getInt("numero_vias"));
                dadosIntituicao.setImpressora(result.getString("impressora"));
                dadosIntituicao.setFoco(result.getString("foco"));
                dadosIntituicao.setDocpadrao(result.getString("docpadrao"));
                dadosIntituicao.setDesactivarvias(result.getString("desactivarvias"));
                dadosIntituicao.setDescontoFinanceiro(result.getString("desconto_financeiro"));
                dadosIntituicao.setAnoEconomico(result.getString("ano_economico"));
                dadosIntituicao.setVizualisarStock(result.getString("vizualisar_stock"));
                dadosIntituicao.setTranstorno(result.getString("transtorno"));
                dadosIntituicao.setCorreioCaixa(result.getString("correio_caixa"));
                dadosIntituicao.setNegocio(result.getString("negocio"));
                dadosIntituicao.setObsFt(result.getString("obs_ft"));
                dadosIntituicao.setPrazoFt(result.getString("prazo_ft"));
                dadosIntituicao.setLocalCarregamento(result.getString("local_carregamento"));
                dadosIntituicao.setSlogan(result.getString("slogan"));
                dadosIntituicao.setObsDevolucao(result.getString("obs_devolucao"));
                dadosIntituicao.setTeclado(result.getString("teclado"));
                dadosIntituicao.setDataLicenca(result.getDate("data_licenca"));
                dadosIntituicao.setRegime(result.getString("regime"));
                dadosIntituicao.setRegimeContrato(result.getString("regime_contrato"));
                dadosIntituicao.setConfigArmazens(result.getString("config_armazens"));
                dadosIntituicao.setUsarDoisPrecos(result.getString("usar_dois_precos"));
                dadosIntituicao.setImpressoraCozinha(result.getString("impressora_cozinha"));
                dadosIntituicao.setChaveMestre(result.getString("chave_mestre"));
                dadosIntituicao.setDataFecho(result.getDate("data_fecho"));
                dadosIntituicao.setTesouraria(result.getString("tesouraria"));
                dadosIntituicao.setRh(result.getString("rh"));
                dadosIntituicao.setComercial(result.getString("comercial"));
                dadosIntituicao.setJanelaServico(result.getString("janela_servico"));
                dadosIntituicao.setImpressoraSala(result.getString("impressora_sala"));
                dadosIntituicao.setPrazoProforma(result.getInt("prazo_proforma"));
                dadosIntituicao.setDesactivarLugares(result.getString("desactivar_lugares"));
                dadosIntituicao.setTipoFechoCaixa(result.getString("tipo_fecho_caixa"));
                dadosIntituicao.setEnviarEmail(result.getString("enviar_email"));
                dadosIntituicao.setStockConsulta(result.getString("stock_consulta"));
                dadosIntituicao.setTipoFichaTecnica(result.getString("tipo_ficha_tecnica"));
                dadosIntituicao.setPosto(result.getString("posto"));
                dadosIntituicao.setImpressoraCaixa(result.getString("impressora_caixa"));
                dadosIntituicao.setSegundoMonitor(result.getString("segundo_monitor"));
                dadosIntituicao.setAlterarPreco(result.getString("alterar_preco"));
//                dadosIntituicao.setHoraComecoVenda(result.getTime("hora_comeco_venda"));
//                dadosIntituicao.setHoraTerminoVenda(result.getTime("hora_termino_venda"));
            } else {
                System.err.println("[DadosInstituicaoController] ⚠ Nenhum registo encontrado para o ID: " + codigo);
            }

        } catch (SQLException e) {
            System.err.println("[DadosInstituicaoController] ❌ Erro ao buscar dados da instituição: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("[DadosInstituicaoController] ⚠ Erro inesperado: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // ✅ Fecha o ResultSet corretamente para evitar vazamento de recursos
            try {
                if (result != null && !result.isClosed()) {
                    result.getStatement().close();
                    result.close();
                }
            } catch (SQLException e) {
                System.err.println("[DadosInstituicaoController] ⚠ Falha ao fechar ResultSet: " + e.getMessage());
            }
        }

        return dadosIntituicao;
    }

    public TbDadosInstituicao getLastUsuario() {

        String FIND__BY_CODIGO = "SELECT MAX(codigo) as maximo_id, c.*  FROM tb_dados_instituicao c";
        ResultSet result = conexao.executeQuery(FIND__BY_CODIGO);
        TbDadosInstituicao dadosIntituicao = null;
        try {

            if (result.next()) {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao(result.getInt("idDadosInsitiuicao"));
                dadosIntituicao.setNome(result.getString("nome"));
                dadosIntituicao.setTelefone(result.getString("telefone"));
                dadosIntituicao.setEnderecos(result.getString("enderecos"));
                dadosIntituicao.setEmail(result.getString("email"));
                dadosIntituicao.setNif(result.getString("nif"));
                dadosIntituicao.setCont(result.getInt("cont"));

                dadosIntituicao.setContaBancaria1(result.getString("conta_bancaria1"));
                dadosIntituicao.setContaBancaria2(result.getString("conta_bancaria2"));
                dadosIntituicao.setContaBancaria3(result.getString("conta_bancaria3"));
                dadosIntituicao.setContaBancaria4(result.getString("conta_bancaria4"));
                dadosIntituicao.setContaBancaria5(result.getString("conta_bancaria5"));
                dadosIntituicao.setContaBancaria6(result.getString("conta_bancaria6"));
                dadosIntituicao.setContaBancaria7(result.getString("conta_bancaria7"));
                dadosIntituicao.setContaBancaria8(result.getString("conta_bancaria8"));

                dadosIntituicao.setDirectorGeral(result.getString("director_geral"));

                dadosIntituicao.setNumeroVias(result.getInt("numero_vias"));
                dadosIntituicao.setImpressora(result.getString("impressora"));
                dadosIntituicao.setFoco(result.getString("foco"));
                dadosIntituicao.setDocpadrao(result.getString("docpadrao"));
                dadosIntituicao.setDesactivarvias(result.getString("desactivarvias"));
                dadosIntituicao.setDescontoFinanceiro(result.getString("desconto_financeiro"));
                dadosIntituicao.setAnoEconomico(result.getString("ano_economico"));
                dadosIntituicao.setVizualisarStock(result.getString("vizualisar_stock"));
                dadosIntituicao.setTranstorno(result.getString("transtorno"));
                dadosIntituicao.setCorreioCaixa(result.getString("correio_caixa"));
                dadosIntituicao.setNegocio(result.getString("negocio"));
                dadosIntituicao.setObsFt(result.getString("obs_ft"));
                dadosIntituicao.setPrazoFt(result.getString("prazo_ft"));
                dadosIntituicao.setLocalCarregamento(result.getString("local_carregamento"));
                dadosIntituicao.setSlogan(result.getString("slogan"));
                dadosIntituicao.setObsDevolucao(result.getString("obs_devolucao"));
                dadosIntituicao.setTeclado(result.getString("teclado"));
                dadosIntituicao.setDataLicenca(result.getDate("data_licenca"));
                dadosIntituicao.setRegime(result.getString("regime"));
                dadosIntituicao.setRegimeContrato(result.getString("regime_contrato"));
                dadosIntituicao.setConfigArmazens(result.getString("config_armazens"));
                dadosIntituicao.setUsarDoisPrecos(result.getString("usar_dois_precos"));
                dadosIntituicao.setImpressoraCozinha(result.getString("impressora_cozinha"));
                dadosIntituicao.setChaveMestre(result.getString("chave_mestre"));
                dadosIntituicao.setDataFecho(result.getDate("data_fecho"));
                dadosIntituicao.setTesouraria(result.getString("tesouraria"));
                dadosIntituicao.setRh(result.getString("rh"));
                dadosIntituicao.setComercial(result.getString("comercial"));
                dadosIntituicao.setJanelaServico(result.getString("janela_servico"));
                dadosIntituicao.setImpressoraSala(result.getString("impressora_sala"));
                dadosIntituicao.setPrazoProforma(result.getInt("prazo_proforma"));
                dadosIntituicao.setDesactivarLugares(result.getString("desactivar_lugares"));
                dadosIntituicao.setTipoFechoCaixa(result.getString("tipo_fecho_caixa"));
                dadosIntituicao.setEnviarEmail(result.getString("enviar_email"));
                dadosIntituicao.setStockConsulta(result.getString("stock_consulta"));
                dadosIntituicao.setTipoFichaTecnica(result.getString("tipo_ficha_tecnica"));
                dadosIntituicao.setPosto(result.getString("posto"));
                dadosIntituicao.setImpressoraCaixa(result.getString("impressora_caixa"));
                dadosIntituicao.setSegundoMonitor(result.getString("segundo_monitor"));
                dadosIntituicao.setAlterarPreco(result.getString("alterar_preco"));
                dadosIntituicao.setHoraComecoVenda(result.getTime("hora_comeco_venda"));
                dadosIntituicao.setHoraTerminoVenda(result.getTime("hora_termino_venda"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dadosIntituicao;

    }

    public TbDadosInstituicao findByCodigo(int idDadosInsitiuicao) {

        String FIND_BY_CODIGO = "SELECT * FROM tb_dados_instituicao WHERE idDadosInsitiuicao = " + idDadosInsitiuicao + "";
        ResultSet result = conexao.executeQuery(FIND_BY_CODIGO);
        TbDadosInstituicao dadosIntituicao = null;
        try {

            if (result.next()) {
                dadosIntituicao = new TbDadosInstituicao();
                dadosIntituicao.setIdDadosInsitiuicao(result.getInt("idDadosInsitiuicao"));
                dadosIntituicao.setNome(result.getString("nome"));
                dadosIntituicao.setTelefone(result.getString("telefone"));
                dadosIntituicao.setEnderecos(result.getString("enderecos"));
                dadosIntituicao.setEmail(result.getString("email"));
                dadosIntituicao.setNif(result.getString("nif"));
                dadosIntituicao.setCont(result.getInt("cont"));

                dadosIntituicao.setContaBancaria1(result.getString("conta_bancaria1"));
                dadosIntituicao.setContaBancaria2(result.getString("conta_bancaria2"));
                dadosIntituicao.setContaBancaria3(result.getString("conta_bancaria3"));
                dadosIntituicao.setContaBancaria4(result.getString("conta_bancaria4"));
                dadosIntituicao.setContaBancaria5(result.getString("conta_bancaria5"));
                dadosIntituicao.setContaBancaria6(result.getString("conta_bancaria6"));
                dadosIntituicao.setContaBancaria7(result.getString("conta_bancaria7"));
                dadosIntituicao.setContaBancaria8(result.getString("conta_bancaria8"));

                dadosIntituicao.setDirectorGeral(result.getString("director_geral"));

                dadosIntituicao.setNumeroVias(result.getInt("numero_vias"));
                dadosIntituicao.setImpressora(result.getString("impressora"));
                dadosIntituicao.setFoco(result.getString("foco"));
                dadosIntituicao.setDocpadrao(result.getString("docpadrao"));
                dadosIntituicao.setDesactivarvias(result.getString("desactivarvias"));
                dadosIntituicao.setDescontoFinanceiro(result.getString("desconto_financeiro"));
                dadosIntituicao.setAnoEconomico(result.getString("ano_economico"));
                dadosIntituicao.setVizualisarStock(result.getString("vizualisar_stock"));
                dadosIntituicao.setTranstorno(result.getString("transtorno"));
                dadosIntituicao.setCorreioCaixa(result.getString("correio_caixa"));
                dadosIntituicao.setNegocio(result.getString("negocio"));
                dadosIntituicao.setObsFt(result.getString("obs_ft"));
                dadosIntituicao.setPrazoFt(result.getString("prazo_ft"));
                dadosIntituicao.setLocalCarregamento(result.getString("local_carregamento"));
                dadosIntituicao.setSlogan(result.getString("slogan"));
                dadosIntituicao.setObsDevolucao(result.getString("obs_devolucao"));
                dadosIntituicao.setTeclado(result.getString("teclado"));
                dadosIntituicao.setDataLicenca(result.getDate("data_licenca"));
                dadosIntituicao.setRegime(result.getString("regime"));
                dadosIntituicao.setRegimeContrato(result.getString("regime_contrato"));
                dadosIntituicao.setConfigArmazens(result.getString("config_armazens"));
                dadosIntituicao.setUsarDoisPrecos(result.getString("usar_dois_precos"));
                dadosIntituicao.setImpressoraCozinha(result.getString("impressora_cozinha"));
                dadosIntituicao.setChaveMestre(result.getString("chave_mestre"));
                dadosIntituicao.setDataFecho(result.getDate("data_fecho"));
                dadosIntituicao.setTesouraria(result.getString("tesouraria"));
                dadosIntituicao.setRh(result.getString("rh"));
                dadosIntituicao.setComercial(result.getString("comercial"));
                dadosIntituicao.setJanelaServico(result.getString("janela_servico"));
                dadosIntituicao.setImpressoraSala(result.getString("impressora_sala"));
                dadosIntituicao.setPrazoProforma(result.getInt("prazo_proforma"));
                dadosIntituicao.setDesactivarLugares(result.getString("desactivar_lugares"));
                dadosIntituicao.setTipoFechoCaixa(result.getString("tipo_fecho_caixa"));
                dadosIntituicao.setEnviarEmail(result.getString("enviar_email"));
                dadosIntituicao.setStockConsulta(result.getString("stock_consulta"));
                dadosIntituicao.setTipoFichaTecnica(result.getString("tipo_ficha_tecnica"));
                dadosIntituicao.setPosto(result.getString("posto"));
                dadosIntituicao.setImpressoraCaixa(result.getString("impressora_caixa"));
                dadosIntituicao.setSegundoMonitor(result.getString("segundo_monitor"));
                dadosIntituicao.setAlterarPreco(result.getString("alterar_preco"));
                dadosIntituicao.setHoraComecoVenda(result.getTime("hora_comeco_venda"));
                dadosIntituicao.setHoraTerminoVenda(result.getTime("hora_termino_venda"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dadosIntituicao;

    }

}
