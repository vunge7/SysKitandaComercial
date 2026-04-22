/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 12/jan/2026
 * @lastModified 12/jan/2026
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.SeriesController;
import entity.Series;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import util.BDConexao;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

public class FormularioSeries extends JFrame
{

    private JComboBox<String> comboDocumentos;
    private JTextField txtPesquisa;
    private JButton btnAdicionar;
    private JButton btnSolicitar;
    private JTable tabelaSeries;
    private DefaultTableModel modeloTabela;
    private BDConexao conexao;
    private DadosInstituicaoController dadosInstituicaoController;
    private SeriesController seriesController;
    private DocumentosController documentosController;

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
        },
        {
            "PP", "Factura Proforma"
        },
    };

    public FormularioSeries( BDConexao conexao )
    {
        setTitle( "Solicitação de Séries" );
        setSize( 900, 500 );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        
        this.conexao = conexao;
        dadosInstituicaoController = new DadosInstituicaoController( conexao );
        seriesController = new SeriesController( conexao );

        documentosController = new DocumentosController( conexao );

        JPanel painel = new JPanel( new BorderLayout() );

        // Painel topo: pesquisa e seleção
        JPanel painelTopo = new JPanel( new FlowLayout() );

        comboDocumentos = new JComboBox<>();
        carregarCombo();

        txtPesquisa = new JTextField( 15 );
        btnAdicionar = new JButton( "Adicionar" );
        btnSolicitar = new JButton( "Solicitar" );

        painelTopo.add( new JLabel( "Escolha o Documento:" ) );
        painelTopo.add( comboDocumentos );
        painelTopo.add( btnSolicitar );
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
        btnSolicitar.addActionListener( e -> solictarSerie() );

        comboDocumentos.addActionListener( e -> preencherSeriesTabela() );
//        txtPesquisa.addKeyListener( new KeyAdapter()
//        {
//            @Override
//            public void keyReleased( KeyEvent e )
//            {
//                filtrarDocumentos( txtPesquisa.getText() );
//            }
//        } );

        preencherSeriesTabela();

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

        Series series = new Series();
        series.setDesignacao( txtPesquisa.getText() );
        series.setFkAnoEconomico( 7 );
        series.setFkDocumento( getIdDocumento() );
        if ( seriesController.salvar( series ) )
        {
            JOptionPane.showMessageDialog( null, "Série adicionada com sucesso!.." );

            preencherSeriesTabela();

        }
    }

    private void preencherSeriesTabela()
    {

        try
        {
            DefaultTableModel modelo = ( DefaultTableModel ) tabelaSeries.getModel();
            modelo.setRowCount( 0 );
            List<Series> listarPorDocumentoEAno = seriesController.listarPorDocumentoEAno( getIdDocumento(), 7 );

            for ( Series series : listarPorDocumentoEAno )
            {
                
                String documento= documentosController.findDocumentoById( series.getFkDocumento()).getDesignacao();
                modelo.addRow( new Object[]
                {
                    series.getId(),
                    documento,
                    series.getDesignacao(),
                    ""
                } );
            }
        }
        catch ( Exception e )
        {
        }

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

    private int getIdDocumento()
    {
        String documentType = comboDocumentos.getSelectedItem()
                .toString().split( "-" )[ 1 ];
        System.out.println( "documento: " + documentType );
        return documentosController
                .getDocumentoByDesignacao( documentType )
                .getPkDocumento();
    }

    private void solictarSerie()
    {
        String taxRegistrationNumber = dadosInstituicaoController.findByCodigo( 1 ).getNif();;
//        String taxRegistrationNumber = "5000413178";
        String seriesYear = "2026";
        String documentType = comboDocumentos.getSelectedItem()
                .toString().replaceAll( " ", "" ).split( "-" )[ 0 ];
        System.out.println( "TaxRegistrationNumber: " + taxRegistrationNumber );
        System.out.println( "Tipo de Documento: " + documentType );
        Map<String, Object> jsonPayload = PayloadFactory.criarPayloadCriarSerie( taxRegistrationNumber, seriesYear, documentType );
        String payLoad = JsonUtil.toJson( jsonPayload );
        System.out.println( payLoad );
        String basicAuth = BasicAuthUtil.gerarAuthorizationHeader( FEConfig.getUsername(), FEConfig.getPassword() );
        String resposta;
        try
        {
            resposta = HttpClientUtil.postJson(
                    FEConfig.getEndpointSolicitarSerie(),
                    payLoad, // o JSON que já tens
                    basicAuth // SOMENTE o base64 (sem "Basic ")
            );

            JsonUtil.print( resposta );
            String seriesCode = getSeriesCode( resposta );
            txtPesquisa.setText( seriesCode );
//            String r = JsonUtil.toJson( resposta );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

//    public static String getSeriesCode( String jsonResponse )
//    {
//        try
//        {
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> root = mapper.readValue( jsonResponse, Map.class );
//            Map<String, Object> seriesFEResult
//                    = ( Map<String, Object> ) root.get( "seriesFEResult" );
//
//            return seriesFEResult != null
//                    ? ( String ) seriesFEResult.get( "seriesCode" )
//                    : null;
//
//        }
//        catch ( Exception e )
//        {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog( null, "Os dados constantes na assinatura da chamada do serviço “jwsSignature” "
//                    + " não estão de acordo com a informação constante na chamada do serviço." );
//            return null;
//        }
//    }
    public static String getSeriesCode( String jsonResponse )
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            
            System.out.println( jsonResponse );

            // 🔥 Caso venham múltiplos JSONs juntos
            String[] parts = jsonResponse.split( "(?<=\\})\\s*(?=\\{)" );

            for ( String part : parts )
            {
                JsonNode root = mapper.readTree( part );

                // 🔴 1. Tratar erros primeiro
                JsonNode errorList = root.path( "errorList" );

                if ( errorList.isArray() && errorList.size() > 0 )
                {
                    StringBuilder erros = new StringBuilder();

                    for ( JsonNode erro : errorList )
                    {
                        String desc = erro.path( "descriptionError" ).asText( null );

                        if ( desc != null && !desc.trim().isEmpty() )
                        {
                            erros.append( desc ).append( "\n" );
                        }
                    }

                    if ( erros.length() > 0 )
                    {
                        JOptionPane.showMessageDialog( null, erros.toString() );
                        return null;
                    }
                }

                // 🟢 2. Buscar seriesCode
                JsonNode seriesFEResult = root.path( "seriesFEResult" );

                if ( seriesFEResult.isObject() )
                {
                    String code = seriesFEResult.path( "seriesCode" ).asText( null );

                    if ( code != null && !code.trim().isEmpty() )
                    {
                        return code;
                    }
                }
            }

            return null;
        }
        catch ( Exception e )
        {
            e.printStackTrace();

            JOptionPane.showMessageDialog( null,
                    "Erro ao processar resposta do serviço (AGT)." );

            return null;
        }
    }
}
