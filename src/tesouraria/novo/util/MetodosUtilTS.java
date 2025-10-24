/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tesouraria.novo.util;


import java.sql.Connection;
import entity.Contas;
import entity.FormaPagamento;
import entity.TbUsuario;
import comercial.controller.UsuariosController;
import java.math.BigDecimal;
import tesouraria.novo.controller.ContaMovimentosController;
import util.BDConexao;

/**
 *
 * @author Domingos Dala Vunge
 */
public class MetodosUtilTS
{

    public static void entradaTesouraria( Contas contaLocal, String doc, FormaPagamento forma, String ref, BigDecimal valor, int cod_usuario, UsuariosController usuariosController, ContaMovimentosController cmc, BDConexao conexao )
    {
        TbUsuario usuario = ( TbUsuario ) usuariosController.findById( cod_usuario );
        String descricao = "Valor recebido do(a) " + forma.getDesignacao() + " apatir da facturação " + ( ref.equals( "" ) ? "." : "de ref. " + ref + "." );
        cmc.procedimentoEntradaContas( contaLocal, doc, valor, descricao,
                "Entrada", usuario.getCodigo(), usuario.getNome(), "n/a", conexao );

    }

}
