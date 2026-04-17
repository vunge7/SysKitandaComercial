package util;

import comercial.controller.PrecosController;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaftInventarioService {

    public static void gerarSAFTInventario(
            List<TbProduto> produtos,
            String caminho,
            LocalDate dataInicio,
            LocalDate dataFim) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 🔹 ROOT
        Element root = doc.createElement("SAFTInventario");
        doc.appendChild(root);

        // 🔹 HEADER
        Element header = doc.createElement("StockHeader");
        root.appendChild(header);

        add(doc, header, "TaxRegistrationNumber", "123456");
        add(doc, header, "FiscalYear", String.valueOf(dataInicio.getYear()));
        add(doc, header, "StartDate", dataInicio.format(formatter));
        add(doc, header, "EndDate", dataFim.format(formatter));
        add(doc, header, "CurrencyCode", "AOA");
        add(doc, header, "DateCreated", LocalDate.now().format(formatter));
        add(doc, header, "ProductID", "Kitanda");
        add(doc, header, "ProductVersion", "1.1");

        // 🔹 STOCK LINES
        Element stockLines = doc.createElement("StockLines");
        root.appendChild(stockLines);

        boolean temStock = false;

        // 🔥 CONTROLLER DE PREÇOS
        BDConexao conexao = BDConexao.getInstancia();
        PrecosController precosController = new PrecosController(conexao);

        // 🔥 CACHE (evita várias queries)
        Map<Integer, BigDecimal> cachePrecos = new HashMap<>();

        if (produtos != null) {
            for (TbProduto p : produtos) {

                if (p.getTbStockList() != null && !p.getTbStockList().isEmpty()) {

                    for (TbStock s : p.getTbStockList()) {

                        if (s.getQuantidadeExistente() > 0) {

                            temStock = true;

                            Element stockLine = doc.createElement("StockLine");

                            add(doc, stockLine, "ProductCode", String.valueOf(p.getCodigo()));
                            add(doc, stockLine, "ProductDescription", p.getDesignacao());
                            add(doc, stockLine, "ProductType", "P");

                            // 🔹 Quantidade formatada
                            BigDecimal qtd = BigDecimal.valueOf(s.getQuantidadeExistente())
                                    .setScale(2, RoundingMode.HALF_UP);

                            add(doc, stockLine, "ClosingStockQuantity", qtd.toString());

                            // 🔥 BUSCAR PREÇO (COM CACHE)
                            BigDecimal valor;

                            if (cachePrecos.containsKey(p.getCodigo())) {
                                valor = cachePrecos.get(p.getCodigo());
                            } else {

                                TbPreco preco = precosController.getLastIdPrecoByIdProduto1(
                                        p.getCodigo(),
                                        qtd
                                );

                                if (preco != null && preco.getPrecoCompra() != null) {
                                    valor = preco.getPrecoCompra();
                                } else {
                                    valor = BigDecimal.ZERO;
                                }

                                valor = valor.setScale(2, RoundingMode.HALF_UP);

                                cachePrecos.put(p.getCodigo(), valor);
                            }

                            add(doc, stockLine, "UnitPrice", valor.toString());

                            stockLines.appendChild(stockLine);
                        }
                    }
                }
            }
        }

        // 🔥 SEM STOCK → COMENTÁRIO
        if (!temStock) {
            stockLines.appendChild(doc.createTextNode("\n"));
            Comment comentario = doc.createComment(" vazio porque não há inventário ");
            stockLines.appendChild(comentario);
            stockLines.appendChild(doc.createTextNode("\n"));
        }

        // 🔹 INVENTORY VALUATION
        Element valuation = doc.createElement("InventoryValuation");
        root.appendChild(valuation);

        add(doc, valuation, "CostMethod", "CMP");

        // 🔹 ESCREVER XML
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(caminho));

        transformer.transform(source, result);
    }

    // 🔹 MÉTODO AUXILIAR
    private static void add(Document doc, Element parent, String tag, String value) {
        Element e = doc.createElement(tag);
        e.setTextContent(value);
        parent.appendChild(e);
    }
}