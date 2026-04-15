package util;

import comercial.controller.DadosInstituicaoController;
import comercial.controller.PrecosController;
import entity.TbDadosInstituicao;
import entity.TbPreco;
import entity.TbProduto;
import entity.TbStock;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class SaftInventarioService {

    public static void gerarSAFTInventario(
            List<TbProduto> produtos,
            String caminho,
            LocalDate dataInicio,
            LocalDate dataFim) {

        try {

            if (produtos == null || produtos.isEmpty()) {
                throw new RuntimeException("Sem produtos para gerar SAFT!");
            }

            // 🔥 Controller de preços
            PrecosController precosController = new PrecosController(BDConexao.getInstancia());

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("SAFTInventario");
            doc.appendChild(root);

            // ================= HEADER =================
            TbDadosInstituicao dados = DadosInstituicaoController.getDados();

            Element header = doc.createElement("StockHeader");
            root.appendChild(header);

            add(doc, header, "TaxRegistrationNumber", dados.getNif());
            add(doc, header, "FiscalYear", String.valueOf(dataInicio.getYear()));
            add(doc, header, "StartDate", dataInicio.toString());
            add(doc, header, "EndDate", dataFim.toString());
            add(doc, header, "CurrencyCode", "AOA");
            add(doc, header, "DateCreated", LocalDate.now().toString());
            add(doc, header, "ProductID", "Kitanda");
            add(doc, header, "ProductVersion", "1.1");

            // ================= STOCK =================
            Element stock = doc.createElement("Stock");
            root.appendChild(stock);

            int totalProdutos = 0;

            for (TbProduto p : produtos) {

                System.out.println("Produto: " + p.getDesignacao());

                // 🔥 Validar stocável
                if (!isStocavel(p.getStocavel())) {
                    System.out.println("❌ Ignorado (não stocável)");
                    continue;
                }

                if (p.getTbStockList() == null || p.getTbStockList().isEmpty()) {
                    System.out.println("❌ Ignorado (sem stock)");
                    continue;
                }

                // 🔹 Quantidade total
                double qtdTotal = p.getTbStockList().stream()
                        .mapToDouble(s -> s.getQuantidadeExistente() != null ? s.getQuantidadeExistente() : 0)
                        .sum();

                if (qtdTotal <= 0) {
                    System.out.println("❌ Ignorado (quantidade zero)");
                    continue;
                }

                // ================= PREÇO REAL =================
                BigDecimal preco = BigDecimal.ZERO;

                try {
                    TbPreco ultimoPreco = precosController.getLastIdPrecoByIdProduto1(
                            p.getCodigo(),
                            BigDecimal.valueOf(qtdTotal)
                    );

                    if (ultimoPreco != null && ultimoPreco.getPrecoVenda() != null) {
                        preco = ultimoPreco.getPrecoVenda();
                        System.out.println("✔ Preço encontrado: " + preco);
                    } else {
                        System.out.println("⚠ Preço não encontrado, usando 0");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                BigDecimal total = preco.multiply(BigDecimal.valueOf(qtdTotal))
                        .setScale(2, RoundingMode.HALF_UP);

                // ================= PRODUCT =================
                Element prod = doc.createElement("Product");

                add(doc, prod, "ProductType", "M");
                add(doc, prod, "ProductCode", "P" + p.getCodigo());

                add(doc, prod, "ProductDescription",
                        p.getDesignacao() != null ? p.getDesignacao() : "SEM DESCRIÇÃO");

                BigDecimal qtd = BigDecimal.valueOf(qtdTotal)
                        .setScale(2, RoundingMode.HALF_UP);

                add(doc, prod, "Quantity", qtd.toString());

                add(doc, prod, "UnitOfMeasure", getUnidade(
                        p.getCodUnidade() != null ? p.getCodUnidade().getDescricao() : null
                ));

                add(doc, prod, "Value", total.toString());

                stock.appendChild(prod);
                totalProdutos++;
            }

            if (totalProdutos == 0) {
                System.out.println("⚠ Nenhum produto foi adicionado ao SAFT!");
            } else {
                System.out.println("✔ Total produtos no SAFT: " + totalProdutos);
            }

            // ================= INVENTORY =================
            Element inv = doc.createElement("InventoryValuation");
            root.appendChild(inv);

            add(doc, inv, "CostMethod", "CMP");

            // ================= SALVAR =================
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc), new StreamResult(new File(caminho)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= AUXILIAR =================
    private static void add(Document doc, Element parent, String tag, String value) {
        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(value != null ? value : ""));
        parent.appendChild(el);
    }

    private static boolean isStocavel(String st) {
        if (st == null) return false;
        st = st.trim().toLowerCase();
        return st.equals("true") || st.equals("s") || st.equals("1");
    }

    private static String getUnidade(String desc) {

        if (desc == null) return "UN";

        desc = desc.toLowerCase();

        if (desc.contains("kg")) return "KG";
        if (desc.contains("lit")) return "LT";
        if (desc.contains("g")) return "G";
        if (desc.contains("un")) return "UN";

        return "UN";
    }
}
