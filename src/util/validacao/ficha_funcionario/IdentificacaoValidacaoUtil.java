/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.validacao.ficha_funcionario;

import dao.FuncionarioDao;
import javax.persistence.EntityManagerFactory;
import javax.swing.table.TableModel;
import static rh.visao.FichaFuncionarioVisao.*;
import static util.DVML.*;
import static util.MetodosUtil.*;

/**
 *
 * @author Domingos Dala Vunge
 */
public class IdentificacaoValidacaoUtil
{
    
    private final static int PAINEL_IDENTIFICACAO = 0;
    private final static int PAINEL_SALARIO = 1;
    private final static int PAINEL_FISCALIDADE = 2;
    private final static int PAINEL_SUBSIDIO = 3;
    private final static int PAINEL_DESCRICAO = 4;
    private final static int PAINEL_OPCAO_BANCO = 5;
    private final static int PAINEL_FECHO_CONTA = 6;
    private final static int PAINEL_ANEXO = 7;
    
    private static FuncionarioDao funcionarioDao;
    
    public IdentificacaoValidacaoUtil( FuncionarioDao funcionarioDao )
    {
        this.funcionarioDao = funcionarioDao;
    }
    
    public static boolean camposValidos()
    {
        
        if ( !existeFuncionarioNumero() && !existeFuncionarioNome() )
        {
            return camposValidosPainelIdentificacao()
                    && camposValidosPaienlSalario()
                    && camposValidosPainelFiscalidade()
                    && camposValidosDescricao()
                    && camposValidosOpcaoBanco();
        }
        return false;
        
    }
    
    public static boolean camposValidosEdit()
    {

        // if ( existeFuncionarioNumero() && existeFuncionarioNome() )
        if ( true )
        {
            return camposValidosPainelIdentificacao()
                    && camposValidosPaienlSalario()
                    && camposValidosPainelFiscalidade()
                    && camposValidosDescricao()
                    && camposValidosOpcaoBanco();
        }
        return false;
        
    }
    
    public static boolean existeFuncionarioNumero()
    {
        
        if ( funcionarioDao.exist_funcionario_numero_func( txtNumeroFuncionario.getText() ) )
        {
            showMessageUtil( "Caro usuário, já existe um funcionário com este número.", TIPO_MENSAGEM_AVISO );
            return true;
        }
        
        return false;
        
    }
    
    public static boolean existeFuncionarioNome()
    {
        
        if ( funcionarioDao.exist_funcionario_nome( txtNomeCompleto.getText() ) )
        {
            showMessageUtil( "Caro usuário, já existe um funcionário com este nome", TIPO_MENSAGEM_AVISO );
            return true;
        }
        
        return false;
        
    }
    
    private static boolean camposValidosPainelIdentificacao()
    {

        /**
         * @DADOSPESSOAIS
         */
        //validar o número do funcionário está vazio.
        if ( txtNumeroFuncionario.getText().equals( "" ) || txtNumeroFuncionario.getText() == null )
        {
            
            showMessageUtil( "Caro usuário digite o número do funcionário", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            txtNumeroFuncionario.requestFocus();
            return false;
        }
        //validar o nome do funcionário está vazio.
        else if ( txtNomeCompleto.getText().equals( "" ) || txtNomeCompleto.getText() == null )
        {
            showMessageUtil( "Caro usuário digite o nome funcionário", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            txtNomeCompleto.requestFocus();
            return false;
        }
        //verifica se seleccionou a data de nascimento. 
        else if ( dcDataNascimento.getCalendar() == null )
        {
            showMessageUtil( "Caro usuário seleccione a data de nascimento do funcionário", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            return false;
        }
        //verifica se a morada do funcionário está vazio.
        else if ( txtMorada.getText().equals( "" ) || txtMorada.getText() == null )
        {
            showMessageUtil( "Caro usuário, digite a morada do funcionário.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            txtMorada.requestFocus();
            return false;
        }
        //verifica se o telefone 1 do funcionário está vazio.
        else if ( ftfTelefone1.getText().equals( "" ) || ftfTelefone1.getText() == null )
        {
            showMessageUtil( "Caro usuário, o telefone 1 é obrigatório.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            ftfTelefone1.requestFocus();
            return false;
        }

        /**
         * Documento de Identificação
         */
        //verifica se o telefone 1 do funcionário está vazio.
        else if ( cmbDocID.getSelectedIndex() == -1 )
        {
            showMessageUtil( "Caro usuário, seleccione o documento de idêntificação.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            return false;
        }
        else if ( txtDocID.getText().equals( "" ) || txtDocID.getText() == null )
        {
            showMessageUtil( "Caro usuário, digite o número do documento de idêntificação.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
            txtDocID.requestFocus();
            return false;
        }
        
        if ( cmbDocID.getSelectedIndex() == 1 || cmbDocID.getSelectedIndex() == 2 )
        {
            //verifica se seleccionou a data de emissão. 
            if ( dcDataEmitidoDoc.getCalendar() == null )
            {
                showMessageUtil( "Caro usuário seleccione a data de emissão do documento", TIPO_MENSAGEM_INFOR );
                painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
                return false;
            }
            //verifica se seleccionou a data de emissão. 
            if ( dcDataValidadeDoc.getCalendar() == null )
            {
                showMessageUtil( "Caro usuário seleccione a data de validade do documento", TIPO_MENSAGEM_INFOR );
                painel_principal.setSelectedIndex( PAINEL_IDENTIFICACAO );
                return false;
            }
        }
        
        return true;
        
    }

    /**
     * @SALÁRIO
     */
    private static boolean camposValidosPaienlSalario()
    {
        
        if ( (Double) jsSalarioBase.getValue() == 0 )
        {
            showMessageUtil( "Caro usuário o salário do funcionário não pode ser 0(Zero)", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_SALARIO );
            return false;
        }
        
//        else if ( jsSubsidioFeria.getValue() == null )
//        {
//            showMessageUtil( "Caro usuário digite a percentagem do subsídio de féria.", TIPO_MENSAGEM_INFOR );
//            painel_principal.setSelectedIndex( PAINEL_SALARIO );
//            return false;
//        }
//        else if ( (Double) jsSubsidioFeria.getValue() == 0 )
//        {
//            showMessageUtil( "Caro usuário percentagem do subídio de féria do funcionário não pode ser 0(Zero)", TIPO_MENSAGEM_INFOR );
//            painel_principal.setSelectedIndex( PAINEL_SALARIO );
//            return false;
//        }
//        else if ( jsSubsidioNatal.getValue() == null )
//        {
//            showMessageUtil( "Caro usuário digite a percentagem subsídio de natal.", TIPO_MENSAGEM_INFOR );
//            painel_principal.setSelectedIndex( PAINEL_SALARIO );
//            return false;
//        }
//        else if ( (Double) jsSubsidioNatal.getValue() == 0 )
//        {
//            showMessageUtil( "Caro usuário percentagem do subsídio de naltal do funcionário não pode ser 0(Zero)", TIPO_MENSAGEM_INFOR );
//            painel_principal.setSelectedIndex( PAINEL_SALARIO );
//            return false;
//        }
        
        return true;
    }

    /**
     * @FISCALIDADE @return
     */
    private static boolean camposValidosPainelFiscalidade()
    {
        if ( rbSegurancaoSocialSim.isSelected() )
        {
            if ( txtNumeroSegurancaSocial.getText().equals( "" ) )
            {
                showMessageUtil( "Caro usuário digite o número de sergurança social", TIPO_MENSAGEM_INFOR );
                painel_principal.setSelectedIndex( PAINEL_FISCALIDADE );
                txtNumeroSegurancaSocial.requestFocus();
                return false;
            }
        }
        
        if ( rbNIFSim.isSelected() )
        {
            if ( txtNIF.getText().equals( "" ) )
            {
                showMessageUtil( "Caro usuário digite o NIF do funcionário", TIPO_MENSAGEM_INFOR );
                painel_principal.setSelectedIndex( PAINEL_FISCALIDADE );
                txtNIF.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * @return @SUBSIDIOS
     */
    public static boolean campoValidosSubsidio()
    {
        
        if ( cmbSubsidios.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário, seleccione o subsídio.", TIPO_MENSAGEM_INFOR );
            return false;
        }
        
        return true;
    }
    
    public static boolean existe_subsidio( String subsidio )
    {
        
        TableModel modelo = tabela_subsidio.getModel();
       
        for ( int i = 0; i < tabela_subsidio.getRowCount(); i++ )
        {
            
            if ( modelo.getValueAt( i, 0 ).toString().equals( subsidio ) )
            {
                return true;
            }
            
        }
        
        return false;
    }

    /**
     * DESCRIÇÃO
     *
     * @return
     */
    public static boolean camposValidosDescricao()
    {
        
        if ( cmbGrauAcademico.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione o grau acadêmico.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbEspecialidade.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione a especialidade.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbDepartamento.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione o departamento.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbFuncao.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione a função.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        
        else if ( dcDataAdmissao.getCalendar()== null)
        {
            showMessageUtil( "Caro usuário seleccione a data de admissão.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbContrato.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione o contrato do funcionario.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbStatus.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione o status do funcionário.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        else if ( cmbRegime.getSelectedIndex() == 0 )
        {
            showMessageUtil( "Caro usuário seleccione o regime do funcionário.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
            return false;
            
        }
        
        if ( cmbContrato.getSelectedIndex() == 1 )
        {
            
            if ( cmbDuracao.getSelectedIndex() == 0 )
            {
                showMessageUtil( "Caro usuário seleccione a duração do contrato.", TIPO_MENSAGEM_INFOR );
                painel_principal.setSelectedIndex( PAINEL_DESCRICAO );
                return false;
            }
            
        }
        
        return true;
    }

    /**
     * @return @OPCAO BANCO
     */
    public static boolean camposValidosOpcaoBanco()
    {
        
        if ( txtNumeroConta.getText().equals( "" ) || txtNumeroConta.getText() == null )
        {
            
            showMessageUtil( "Caro usuário insira o número de conta do funcionário.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_OPCAO_BANCO );
            txtNumeroConta.requestFocus();
            return false;
        }
        else if ( ftfNumeroIBAN.getText().equals( "" ) || ftfNumeroIBAN.getText() == null )
        {
            
            showMessageUtil( "Caro usuário insira o IBAN do funcionário.", TIPO_MENSAGEM_INFOR );
            painel_principal.setSelectedIndex( PAINEL_OPCAO_BANCO );
            txtNumeroConta.requestFocus();
            return false;
        }
        
        int tamanho_iban = ftfNumeroIBAN.getText().trim().replaceAll( " ", "" ).length();
        
        if ( tamanho_iban < 30 )
        {
            showMessageUtil( "Caro Usuário o número do IBAN está incorrecto.", TIPO_MENSAGEM_ERRO );
            painel_principal.setSelectedIndex( PAINEL_OPCAO_BANCO );
            return false;
        }
        
        return true;
    }
    
    public static void main( String[] args )
    {
        new IdentificacaoValidacaoUtil( funcionarioDao );
    }
}
