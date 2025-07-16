/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kitanda.util;

import java.util.HashMap;
import static kitanda.util.CfDefs.Mensagem.*;
import static kitanda.util.CfDefs.Titulo.*;

/**
 *
 * @author tagif
 */
public class CfDefs
{
    public static HashMap<Object, String> mensagens = new HashMap<> ();

    public static HashMap<Object, String> titulosJanelas = new HashMap<> ();

    static int t = 0;
    static int m = 0;

    public static enum Titulo
    {

        TITULO_AVISO,
        TITULO_REGISTRO_CRIAR,
        TITULO_REGISTRO_ALTERAR,
        TITULO_JANELA_FECHAR,
        CfPedidoMenuCriarJFrame,
        CfAluguerAlterarJFrame,
        CfAluguerCriarJFrame,
        CfSalasJFrame,
        CfMenuItemJFrame,
        CfMenuJFrame,
        CfAluguerPagarJFrame,
    }
    //TIPOS DE MENSAGENS
    //ERRO, AVISO, SUCESSO, CONFIRMACAO
    public static enum Mensagem
    {
        MSG_SUCESSO_CRIAR_1,
        MSG_ERRO_CRIAR_1,
        MSG_SUCESSO_ALTERAR_1,
        MSG_ERRO_ALTERAR_1,
        MSG_CONFIRMACAO_JANELA_FECHAR,
        MSG_AVISO_NENHUMA_TOPOLOGIA,
        MSG_AVISO_NENHUM_PRATO_OU_BEBIDA,
    }
    
    static
    {
        //MENSAGENS
        mensagens.put ( MSG_SUCESSO_CRIAR_1, "REGISTRO CRIADO COM SUCESSO!" );
        mensagens.put ( MSG_ERRO_CRIAR_1, "ERRO AO CRIAR O REGISTRO!" );
        mensagens.put ( MSG_SUCESSO_ALTERAR_1, "REGISTRO ALETRADO COM SUCESSO!" );
        mensagens.put ( MSG_ERRO_ALTERAR_1, "ERRO AO ALTERAR O REGISTRO!" );
        mensagens.put ( MSG_CONFIRMACAO_JANELA_FECHAR, "ESTA PRESTES A FECHAR A JANELA, DESEJA MESMO CONTINUA?" );
        mensagens.put ( MSG_AVISO_NENHUMA_TOPOLOGIA, "NÃO SELECIONOU NENHUM FORMATO PARA A SALA" );
        mensagens.put ( MSG_AVISO_NENHUM_PRATO_OU_BEBIDA, "NÃO SELECIONOU NENHUM PRATO OU BEBIDA" );

        //TITULOS DAS JANELAS
        titulosJanelas.put ( TITULO_REGISTRO_CRIAR, "CRIAR NOVO REGISTRO" );
        titulosJanelas.put ( TITULO_REGISTRO_ALTERAR, "ATULIZAR NOVO REGISTRO" );
        titulosJanelas.put ( TITULO_JANELA_FECHAR, "FECHAR A JANELA" );
        titulosJanelas.put ( CfPedidoMenuCriarJFrame.name (), "W I N M A R K E T - Pedidos de menus [ SOLICITAÇÃO DE BUFÊ ]" );
        titulosJanelas.put ( CfAluguerAlterarJFrame.name (), "W I N M A R K E T - Reservas de salas de conferencias [ EFECTUAR PAGAMENTOS ]" );
        titulosJanelas.put ( CfAluguerCriarJFrame.name (), "W I N M A R K E T - Reservas de salas de conferencias  [ RESERVAR SALAS ]" );
        titulosJanelas.put ( CfSalasJFrame.name (), "W I N M A R K E T - Gestão de salas de conferencias [ RESERVAR SALAS ]" );
        titulosJanelas.put ( CfMenuItemJFrame.name (), "W I N M A R K E T - Gestão de pratos e bebidas que compõem o menus [ GERIR PRATOS E BEBIDAS DO MENU ]" );
        titulosJanelas.put ( CfMenuJFrame.name (), "W I N M A R K E T - Gestão de menus [ ENCOMENDA DE BUFFETS ]" );
        titulosJanelas.put ( CfAluguerPagarJFrame.name (), "W I N M A R K E T - Reservas de salas de conferencias  [ RESERVAR SALAS ]" );
        titulosJanelas.put ( TITULO_AVISO, "AVISO!" );
    }
}
