/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 12/jan/2026
 * @lastModified 12/jan/2026
 */
import comercial.controller.DadosInstituicaoController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.fe.BasicAuthUtil;
import util.fe.FEConfig;
import util.fe.JsonUtil;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

public class FormularioSeries extends JFrame
{

    private JComboBox<String> comboDocumentos;
    private JTextField txtPesquisa;
    private JButton btnAdicionar;
    private JButton btnPesquisar;
    private JTable tabelaSeries;
    private DefaultTableModel modeloTabela;
    private BDConexao conexao;
    private DadosInstituicaoController dadosInstituicaoController;

    // Simula lista de documentos: código - descrição
// Lista completa de documentos baseada no decreto e práticas contábeis
    // Lista de documentos conforme anexo
    private String[][] documentos =
    {
        {
            "FA", "Factura de Adiantamento"
        },
        {
            "FT", "Factura"
        },
        {
            "FR", "Factura/Recibo"
        },
        {
            "FG", "Factura Global"
        },
        {
            "AC", "Aviso de Cobrança"
        },
        {
            "AR", "Aviso de Cobrança/Recibo"
        },
        {
            "TV", "Talão de Venda"
        },
        {
            "RC", "Recibo Emitido"
        },
        {
            "RG", "Recibo"
        },
        {
            "RE", "Estorno ou Recibo de Estorno"
        },
        {
            "ND", "Nota de Débito"
        },
        {
            "NC", "Nota de Crédito"
        },
        {
            "AF", "Factura/Recibo de Autofacturação"
        },
        {
            "RP", "Prémio ou Recibo de Prémio"
        },
        {
            "RA", "Resseguro Aceite"
        },
        {
            "CS", "Imputação a Co-seguradoras"
        },
        {
            "LD", "Imputação a Co-seguradora Líder"
        }
    };

    public FormularioSeries( BDConexao conexao )
    {
        setTitle( "Solicitação de Séries" );
        setSize( 700, 400 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo( null );
        this.conexao = conexao;
        dadosInstituicaoController = new DadosInstituicaoController( conexao );

        JPanel painel = new JPanel( new BorderLayout() );

        // Painel topo: pesquisa e seleção
        JPanel painelTopo = new JPanel( new FlowLayout() );

        comboDocumentos = new JComboBox<>();
        carregarCombo();

        txtPesquisa = new JTextField( 15 );
        btnAdicionar = new JButton( "Adicionar" );
        btnPesquisar = new JButton( "Pesquisar" );

        painelTopo.add( new JLabel( "Escolha o Documento:" ) );
        painelTopo.add( comboDocumentos );
        painelTopo.add( btnPesquisar );
        painelTopo.add( txtPesquisa );
        painelTopo.add( btnAdicionar );

        // Tabela
        modeloTabela = new DefaultTableModel( new Object[]
        {
            "Código", "Documento", "Série", "Data Solicitação"
        }, 0 );
        tabelaSeries = new JTable( modeloTabela );
        JScrollPane scroll = new JScrollPane( tabelaSeries );

        painel.add( painelTopo, BorderLayout.NORTH );
        painel.add( scroll, BorderLayout.CENTER );

        add( painel );

        // Eventos
        btnAdicionar.addActionListener( e -> adicionarDocumento() );
        btnPesquisar.addActionListener( e -> solictarSerie() );
        txtPesquisa.addKeyListener( new KeyAdapter()
        {
            @Override
            public void keyReleased( KeyEvent e )
            {
                filtrarDocumentos( txtPesquisa.getText() );
            }
        } );
    }

    private void carregarCombo()
    {
        comboDocumentos.removeAllItems();
        for ( String[] doc : documentos )
        {
            comboDocumentos.addItem( doc[ 0 ] + " - " + doc[ 1 ] );
        }
    }

    private void adicionarDocumento()
    {
        System.out.println( "Adicionado..." );
    }

    private void filtrarDocumentos( String filtro )
    {
        comboDocumentos.removeAllItems();
        for ( String[] doc : documentos )
        {
            if ( doc[ 0 ].toLowerCase().contains( filtro.toLowerCase() )
                    || doc[ 1 ].toLowerCase().contains( filtro.toLowerCase() ) )
            {
                comboDocumentos.addItem( doc[ 0 ] + " - " + doc[ 1 ] );
            }
        }
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new FormularioSeries( new BDConexao() ).setVisible( true ) );
    }

    private void solictarSerie()
    {
//        String taxRegistrationNumber = dadosInstituicaoController.findByCodigo( 1 ).getNif();;
        String taxRegistrationNumber = "5000413178";
        String seriesYear = "2026";
        String documentType = comboDocumentos.getSelectedItem().toString().replaceAll( " ", "" ).split( "-" )[ 0 ];
        System.out.println( "TaxRegistrationNumber" + taxRegistrationNumber );
        System.out.println( "Tipo de Documento " + documentType );
        Map<String, Object> jsonPayload = PayloadFactory.criarPayloadCriarSerie( taxRegistrationNumber, seriesYear, documentType );
        String payLoad = JsonUtil.toJson( jsonPayload );

        String basicAuth = BasicAuthUtil.gerarAuthorizationHeader( FEConfig.getUsername(), FEConfig.getPassword() );
        String resposta;
        try
        {
            resposta = HttpClientUtil.postJson( FEConfig.getEndpointSolicitarSerie(),
                    payLoad, // o JSON que já tens
                    basicAuth // SOMENTE o base64 (sem "Basic ")
            );

            JsonUtil.print( resposta );
//            String r = JsonUtil.toJson( resposta );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
}
