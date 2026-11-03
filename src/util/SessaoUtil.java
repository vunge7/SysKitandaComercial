package util;

import entity.TbUsuario;

/**
 * Guarda o utilizador autenticado durante a execução da aplicação, para que
 * controllers e utilitários (ex.: LogActividadeUtil) saibam quem está a operar
 * sem ser necessário passar o id do utilizador por todos os métodos.
 *
 * Preenchido em LoginVisao.entrar_sistema().
 */
public class SessaoUtil
{

    private static Integer codUsuario;
    private static String nomeUsuario;

    // calculados uma única vez: a resolução de rede pode ser lenta
    private static final String NOME_MAQUINA = MetodosUtil.getNomeMaquina();
    private static final String IP_MAQUINA = MetodosUtil.getIpMaquina();

    public static void iniciar( TbUsuario usuario )
    {
        if ( usuario == null )
        {
            terminar();
            return;
        }
        codUsuario = usuario.getCodigo();
        String nome = usuario.getNome();
        if ( nome == null || nome.trim().isEmpty() )
        {
            nome = usuario.getUserName();
        }
        nomeUsuario = nome;
    }

    public static void terminar()
    {
        codUsuario = null;
        nomeUsuario = null;
    }

    public static Integer getCodUsuario()
    {
        return codUsuario;
    }

    public static String getNomeUsuario()
    {
        return nomeUsuario;
    }

    public static String getNomeMaquina()
    {
        return NOME_MAQUINA;
    }

    public static String getIpMaquina()
    {
        return IP_MAQUINA;
    }

}
