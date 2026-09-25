package util.fe;

import comercial.controller.PrecosController;
import entity.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import javax.swing.JTable;
import kitanda.util.CfMethods;
import util.*;
import util.fe.dto.*;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

/**
 * Refatorado para eliminar redundância e isolar responsabilidades.
 *
 * @author Engº Domingos Dala Vunge (Refactored)
 */
public class FacturaElectronicaUtil {

    // --- MÉTODOS PÚBLICOS (API) ---
    public static boolean criarFE(TbVenda venda, TbDadosInstituicao inst, Documento doc, TbCliente cli, JTable table, TableColumIdUtil cols) {
        List<LineDTO> lines = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            lines.add(mapTableLineToDTO(i, table, cols, venda));
        }
        return processarEnvioFE(venda, inst, doc, cli, lines);
    }

    public static boolean criarFE(TbVenda venda, TbDadosInstituicao inst, Documento doc, TbCliente cli, List<TbItemVenda> itens, BDConexao conexao) {
        PrecosController precosCtrl = new PrecosController(conexao);
        List<LineDTO> lines = new ArrayList<>();
        for (int i = 0; i < itens.size(); i++) {
            lines.add(mapItemVendaToDTO(i, itens.get(i), precosCtrl, venda));
        }
        return processarEnvioFE(venda, inst, doc, cli, lines);
    }

    // --- LÓGICA DE MAPEAMENTO (ISOLAMENTO) ---
    private static LineDTO mapTableLineToDTO(int index, JTable table, TableColumIdUtil cols, TbVenda venda) {
        String idProd = table.getValueAt(index, cols.COLUMN_PRODUTO_ID).toString();
        String desc = table.getValueAt(index, cols.getCOLUMN_DESIGNACAO()).toString();
        BigDecimal unitPrice = BigDecimal.valueOf(CfMethods.parseMoedaFormatada(table.getValueAt(index, cols.getCOLUMN_PRECO_UNITARIO()).toString()));
        BigDecimal qtd = BigDecimal.valueOf(Double.parseDouble(table.getValueAt(index, cols.getCOLUMN_QTD()).toString()));
        BigDecimal descVal = BigDecimal.valueOf(Double.parseDouble(table.getValueAt(index, cols.getCOLUMN_DESCONTO()).toString()));
        BigDecimal taxa = BigDecimal.valueOf(Double.parseDouble(table.getValueAt(index, cols.getCOLUMN_TAXA()).toString()));

        return construirLineDTO(index + 1, idProd, desc, unitPrice, qtd, descVal, taxa, venda);
    }

    private static LineDTO mapItemVendaToDTO(int index, TbItemVenda item, PrecosController ctrl, TbVenda venda) {
        TbPreco preco = (TbPreco) ctrl.findById(item.getFkPreco().getPkPreco());
        return construirLineDTO(
                index + 1,
                String.valueOf(item.getCodigoProduto().getCodigo()),
                item.getDesignacaoItem(),
                preco.getPrecoVenda(),
                BigDecimal.valueOf(item.getQuantidade()),
                BigDecimal.valueOf(item.getDesconto()),
                BigDecimal.valueOf(item.getValorIva()),
                venda
        );
    }

//    private static LineDTO construirLineDTO(int lineNo, String cod, String desc, BigDecimal price, BigDecimal qtd, BigDecimal discount, BigDecimal taxPerc, TbVenda venda) {
//        BigDecimal unitPriceBase = price.subtract(discount);
//        BigDecimal base = unitPriceBase.multiply(qtd).setScale(2, RoundingMode.CEILING);
//        BigDecimal iva = FinanceUtils.getValorIVABigDecimal(qtd, taxPerc, unitPriceBase, discount);
//
//        LineDTO line = new LineDTO();
//        line.setLineNumber(lineNo);
//        line.setProductCode(cod);
//        line.setProductDescription(desc);
//        line.setQuantity(qtd.toString());
//        line.setUnitOfMeasure("UN");
//        line.setUnitPrice(price);
//        line.setUnitPriceBase(unitPriceBase);
//
//        // Lógica de Notas de Crédito vs Débito
//        if (venda.getFkDocumento().getPkDocumento() == DVML.DOC_NOTA_CREDITO_NC) {
//            ReferenceInfoDTO ref = new ReferenceInfoDTO();
//            ref.setReferenceItemLineNo(String.valueOf(lineNo));
//            ref.setReference(venda.getRefCodFact());
//            ref.setReason(venda.getObs());
//            line.setReferenceInfoDTOs(Collections.singletonList(ref));
//            line.setDebitAmount(base);
//            line.setCreditAmount(BigDecimal.ONE);
//        } else {
//            line.setDebitAmount(BigDecimal.ONE);
//            line.setCreditAmount(base);
//        }
//
//        if (taxPerc.doubleValue() > 0) {
//            TaxDTO tax = new TaxDTO();
//            tax.setTaxType("IVA");
//            tax.setTaxCountryRegion("AO");
//            tax.setTaxCode("NOR");
//            tax.setTaxPercentage(taxPerc.toString());
//            tax.setTaxContribution(iva.doubleValue());
//            line.setTaxes(Collections.singletonList(tax));
//        }
//
//        // Atributos temporários para cálculo de totais facilitado
    ////        line.setTotalIvaTemp(iva);
////        line.setTotalBaseTemp(base);
//        return line;
//    }
    
    
      private static LineDTO construirLineDTO(int lineNo,
            String cod,
            String desc,
            BigDecimal unitPriceBase,
            BigDecimal qtd,
            BigDecimal discount,
            BigDecimal taxPerc,
            TbVenda venda) {

        /**
         * FT / FR
         *
         * unitPriceBase = 1000 unitPrice = 900; discount = 100; quantity = 2;
         * taxPercentage = 7/100
         *
         * creditAmount = 900 * 2 = 1.800; //Para calcBaseValue = 1.800; // 10
         * casas decimais taxContibution = 1.800 * 0.07 = 126; "Valor total de
         * impostos da factura “taxPayable” (4,939.252), não corresponde à soma
         * dos impostos de todas as linhas (4,939.27)."
         */
        BigDecimal unitPrice = unitPriceBase.subtract(discount);
//        BigDecimal base = unitPriceBase.multiply(qtd).setScale(2, RoundingMode.CEILING);
        BigDecimal settlementAmount = unitPriceBase.subtract(unitPrice);
        //novo teste

//        System.out.println(base);
        LineDTO line = new LineDTO();
        line.setLineNumber(lineNo);
        line.setProductCode(cod);
        line.setProductDescription(desc);
        line.setQuantity(qtd.toString());
        line.setUnitOfMeasure("UN");

        line.setUnitPrice(unitPrice);
        line.setUnitPriceBase(unitPriceBase);
        line.setSettlementAmount(settlementAmount);

        BigDecimal calcBaseValue = unitPrice.multiply(qtd).setScale(10, RoundingMode.DOWN);

//        BigDecimal taxContribuition = FinanceUtils.getValorIVABigDecimal(qtd, taxPerc, unitPriceBase, discount);
        BigDecimal taxContribuition = calcBaseValue.multiply(
                taxPerc.divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UP)
        );
// Lógica de Notas de Crédito vs Débito
        if (venda.getFkDocumento().getPkDocumento() == DVML.DOC_NOTA_CREDITO_NC) {
            ReferenceInfoDTO ref = new ReferenceInfoDTO();

            ref.setReferenceItemLineNo(String.valueOf(lineNo));
            ref.setReference(venda.getRefCodFact());
            ref.setReason(venda.getObs());
            line.setReferenceInfoDTOs(Collections.singletonList(ref));
            BigDecimal debitAmount = unitPrice.multiply(qtd).setScale(2, RoundingMode.DOWN);
            line.setDebitAmount(debitAmount);
            line.setCreditAmount(BigDecimal.ZERO);
        } else {
            line.setDebitAmount(BigDecimal.ZERO);
            BigDecimal creditAmount = unitPrice.multiply(qtd).setScale(2, RoundingMode.DOWN);
            line.setCreditAmount(creditAmount);
        }

        if (taxPerc.doubleValue() > 0) {
            TaxDTO tax = new TaxDTO();
            tax.setTaxType("IVA");
            tax.setTaxCountryRegion("AO");
            tax.setTaxCode("NOR");
            tax.setTaxPercentage(taxPerc.toString());
            tax.setTaxContribution(taxContribuition.doubleValue());
            line.setTaxes(Collections.singletonList(tax));
        }

        // Atributos temporários para cálculo de totais facilitado
//        line.setTotalIvaTemp(iva);
//        line.setTotalBaseTemp(base);
        return line;
    }

    // --- PROCESSAMENTO CENTRALIZADO ---
    private static boolean processarEnvioFE(TbVenda venda, TbDadosInstituicao inst, Documento documento, TbCliente cliente, List<LineDTO> lines) {
        DocumentDTO docDTO = new DocumentDTO();
        docDTO.setDocumentNo(venda.getCodFact());
        docDTO.setDocumentStatus("N");
        docDTO.setDocumentDate(DataUtil.converterNormal(venda.getDataVenda()));
        docDTO.setDocumentType(documento.getAbreviacao());
        docDTO.setEacCode("12345");
        docDTO.setSystemEntryDate(DataUtil.converter(venda.getDataVenda()));
        docDTO.setCustomerTaxID(cliente.getNif());
        docDTO.setCustomerCountry(cliente.getPaisISO());
        docDTO.setCompanyName(cliente.getNome());
        docDTO.setLines(lines);

        // Totais
        BigDecimal totalBase = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;
        for (LineDTO l : lines) {
            // Nota: Você pode precisar adicionar esses getters no seu DTO ou calcular aqui
            // Usando a lógica que já estava no loop original:
            totalBase = totalBase.add(l.getCreditAmount().equals(BigDecimal.ONE) ? l.getDebitAmount() : l.getCreditAmount());
            if (l.getTaxes() != null && !l.getTaxes().isEmpty()) {
                totalIva = totalIva.add(BigDecimal.valueOf(l.getTaxes().get(0).getTaxContribution()));
            }
        }

        DocumentTotalsDTO totals = new DocumentTotalsDTO();
        totals.setNetTotal(totalBase);
        totals.setTaxPayable(totalIva);
        totals.setGrossTotal(totalBase.add(totalIva));
        docDTO.setDocumentTotals(totals);

        venda.setTotalIva(totalIva);
        venda.setTotalGeral(totalBase);

        // Retenção (Simulando a lógica original baseada no objeto venda se necessário)
        // Se a retenção vier dos itens, ela deve ser somada durante o loop de construção das linhas.
        return enviarParaAPI(inst.getNif(), docDTO, venda);
    }

    private static boolean enviarParaAPI(String nif, DocumentDTO doc, TbVenda venda) {
        try {
            Map<String, Object> payloadMap = PayloadFactory.criarPayloadCriarDocumento(nif, Collections.singletonList(doc));
            String uuid = (String) payloadMap.get("submissionUUID");
            venda.setSubmissionUUID(uuid);

            String json = JsonUtil.toJson(payloadMap);
            String auth = BasicAuthUtil.gerarAuthorizationHeader(FEConfig.getUsername(), FEConfig.getPassword());

            String response = HttpClientUtil.postJson(FEConfig.getEndpointRegistrarFactura(), json, auth);
            return PayloadFactory.obterEstadoFactura(nif, response, venda);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
