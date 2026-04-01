/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe;

import comercial.controller.DadosInstituicaoController;
import entity.Documento;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbVenda;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import kitanda.util.CfMethods;
import util.DVML;
import util.FinanceUtils;
import util.fe.dto.DocumentDTO;
import util.fe.dto.DocumentTotalsDTO;
import util.fe.dto.LineDTO;
import util.fe.dto.ReferenceInfoDTO;
import util.fe.dto.TaxDTO;
import util.fe.dto.WithholdingTaxDTO;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

/**
 *
 * @author Engº Domingos Dala Vunge
 * @created 16/mar/2026
 * @lastModified 16/mar/2026
 */
public class FacturaElectronicaUtil
{

    public static boolean criarFE(
            TbVenda venda,
            TbDadosInstituicao dadosInstituicao,
            Documento documento,
            TbCliente cliente,
            JTable table,
            TableColumIdUtil tablesColumnIds
    )
    {
        String taxRegistrationNumber = dadosInstituicao.getNif();

        List<DocumentDTO> documentDTOs = new ArrayList<>();
        DocumentDTO doc = new DocumentDTO();

        doc.setDocumentNo( venda.getCodFact() );
        doc.setDocumentStatus( "N" );
        doc.setDocumentDate( DataUtil.converterNormal( venda.getDataVenda() ) );
        doc.setDocumentType( documento.getAbreviacao() );
        doc.setEacCode( "12345" );
        doc.setSystemEntryDate( DataUtil.converter( venda.getDataVenda() ) );
        doc.setCustomerTaxID( cliente.getNif() );
        doc.setCustomerCountry( cliente.getPaisISO() );
        doc.setCompanyName( cliente.getNome() );

        List<LineDTO> lines = new ArrayList<>();

        BigDecimal totalBase = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;
        BigDecimal totalFinal = BigDecimal.ZERO;
        BigDecimal totalRetencao = BigDecimal.ZERO;

        for ( int i = 0; i < table.getRowCount(); i++ )
        {
            int idProduto = Integer.parseInt( table.getValueAt( i, tablesColumnIds.COLUMN_PRODUTO_ID ).toString() );
            String designacaoItem = table.getValueAt( i, tablesColumnIds.getCOLUMN_DESIGNACAO() ).toString();
            double unitPrice = CfMethods.parseMoedaFormatada( table.getValueAt( i, tablesColumnIds.getCOLUMN_PRECO_UNITARIO() ).toString() );
            double qtd = Double.parseDouble( table.getValueAt( i, tablesColumnIds.getCOLUMN_QTD() ).toString() );
            double desconto = Double.parseDouble( table.getValueAt( i, tablesColumnIds.getCOLUMN_DESCONTO() ).toString() );
            double taxa = Double.parseDouble( table.getValueAt( i, tablesColumnIds.getCOLUMN_TAXA() ).toString() );
            double retencaoLinha = CfMethods.parseMoedaFormatada( table.getValueAt( i, tablesColumnIds.getCOLUMN_RETENCAO() ).toString() );
            double subTotal = CfMethods.parseMoedaFormatada( table.getValueAt( i, tablesColumnIds.COLUMN_SUBTOTAL ).toString() );

            // Cálculos com BigDecimal e arredondamento
            BigDecimal bdUnitPrice = BigDecimal.valueOf( unitPrice );
            BigDecimal bdDesconto = BigDecimal.valueOf( desconto );
            BigDecimal bdQtd = BigDecimal.valueOf( qtd );
//            BigDecimal bdTaxa = BigDecimal.valueOf( taxa ).divide( BigDecimal.valueOf( 100 ) );

//            BigDecimal unitPriceBase = bdUnitPrice.subtract( bdDesconto ).setScale( 2, BigDecimal.ROUND_CEILING );
            BigDecimal unitPriceBase = bdUnitPrice.subtract( bdDesconto );
//            BigDecimal base = unitPriceBase.multiply( bdQtd ).setScale( 2, BigDecimal.ROUND_CEILING );
            BigDecimal base = unitPriceBase.multiply( bdQtd ).setScale( 2, RoundingMode.CEILING );;
//            BigDecimal iva = base.multiply( bdTaxa ).setScale( 2, BigDecimal.ROUND_CEILING );
            BigDecimal iva = FinanceUtils.getValorIVA( qtd, taxa, unitPriceBase.doubleValue(), desconto );
//            BigDecimal totalLinha = base.add( iva ).setScale( 2, BigDecimal.ROUND_CEILING );

            BigDecimal totalLinha = base.add( iva );

            LineDTO line = new LineDTO();
            line.setLineNumber( i + 1 );
            line.setProductCode( String.valueOf( idProduto ) );
            line.setProductDescription( designacaoItem );
            line.setQuantity( String.valueOf( qtd ) );
            line.setUnitOfMeasure( "UN" );
            line.setUnitPrice( unitPrice );
            line.setUnitPriceBase( unitPriceBase.doubleValue() );

            if ( venda.getFkDocumento().getPkDocumento()
                    == DVML.DOC_NOTA_CREDITO_NC )
            {
                ReferenceInfoDTO rDTO = new ReferenceInfoDTO();
                rDTO.setReferenceItemLineNo( String.valueOf( line.getLineNumber() ) );
                rDTO.setReference( venda.getRefCodFact() );
                rDTO.setReason( venda.getObs() );
                line.setReferenceInfoDTOs(
                        Collections.singletonList( rDTO )
                );
                line.setDebitAmount( base.doubleValue() );
                line.setCreditAmount( 0 );
            }
            else
            {
                line.setDebitAmount( 0 );
                line.setCreditAmount( base.doubleValue() );
            }

            line.setCreditAmount( base.doubleValue() );

            if ( taxa > 0 )
            {
                TaxDTO tax = new TaxDTO();
                tax.setTaxType( "IVA" );
                tax.setTaxCountryRegion( "AO" );
                tax.setTaxCode( "NOR" );
                tax.setTaxPercentage( String.valueOf( taxa ) );
                tax.setTaxContribution( iva.doubleValue() );

                line.setTaxes( Collections.singletonList( tax ) );
            }

            lines.add( line );

            totalBase = totalBase.add( base );
            totalIva = totalIva.add( iva );
            totalFinal = totalFinal.add( totalLinha );
            totalRetencao = totalRetencao.add( BigDecimal.valueOf( retencaoLinha ) );
        }

        doc.setLines( lines );

        DocumentTotalsDTO documentsTotals = new DocumentTotalsDTO();
        documentsTotals.setNetTotal( totalBase.doubleValue() );
        documentsTotals.setTaxPayable( totalIva.doubleValue() );
        documentsTotals.setGrossTotal( totalFinal.doubleValue() );
        doc.setDocumentTotals( documentsTotals );

        documentDTOs.add( doc );
        venda.setTotalIva( totalIva );
        venda.setTotalGeral( totalBase );

        if ( totalRetencao.compareTo( BigDecimal.ZERO ) > 0 )
        {
            WithholdingTaxDTO ret = new WithholdingTaxDTO();
            ret.setWithholdingTaxType( "IRT" );
            ret.setWithholdingTaxDescription( "Retenção na fonte" );
            ret.setWithholdingTaxAmount( totalRetencao.doubleValue() );

            doc.setWithholdingTaxList( Collections.singletonList( ret ) );
        }

        Map<String, Object> jsonPayload = PayloadFactory.criarPayloadCriarDocumento(
                taxRegistrationNumber,
                documentDTOs
        );

        String submissionUUID = ( String ) jsonPayload.get( "submissionUUID" );
        System.out.println( "UUID: " + submissionUUID );

        venda.setSubmissionUUID( submissionUUID );
        String payload = JsonUtil.toJson( jsonPayload );

        JsonUtil.print( payload );

        String basicAuth = BasicAuthUtil.gerarAuthorizationHeader( FEConfig.getUsername(), FEConfig.getPassword() );
        try
        {
            String resposta = HttpClientUtil.postJson( FEConfig.getEndpointRegistrarFactura(), payload, basicAuth );
            JsonUtil.print( resposta );
            return PayloadFactory.obterEstadoFactura( taxRegistrationNumber, resposta, venda );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return false;
    }

}
