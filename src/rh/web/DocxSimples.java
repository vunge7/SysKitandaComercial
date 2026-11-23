package rh.web;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Documento Word (.docx) simples, sem bibliotecas: um parágrafo por linha.
 * Linhas começadas por "# " são títulos (negrito, maior), "## " subtítulos
 * (negrito); uma linha vazia é um parágrafo vazio. Abre no Word, LibreOffice
 * e WPS, e fica editável (cartas, notas de culpa, decisões, declarações).
 */
public final class DocxSimples {

    private DocxSimples() {
    }

    public static void gravar(File f, String texto) throws Exception {
        try (ZipOutputStream z = new ZipOutputStream(new FileOutputStream(f))) {
            entrada(z, "[Content_Types].xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                    + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                    + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                    + "<Override PartName=\"/word/document.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml\"/>"
                    + "</Types>");
            entrada(z, "_rels/.rels", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                    + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"word/document.xml\"/>"
                    + "</Relationships>");
            StringBuilder corpo = new StringBuilder();
            for (String linha : texto.replace("\r", "").split("\n", -1)) {
                boolean titulo = linha.startsWith("# "), sub = linha.startsWith("## ");
                String t = titulo ? linha.substring(2) : sub ? linha.substring(3) : linha;
                corpo.append("<w:p><w:pPr><w:spacing w:after=\"120\"/>")
                        .append(titulo ? "<w:jc w:val=\"center\"/>" : "<w:jc w:val=\"both\"/>").append("</w:pPr>");
                if (!t.isEmpty()) {
                    corpo.append("<w:r><w:rPr><w:rFonts w:ascii=\"Calibri\" w:hAnsi=\"Calibri\"/>")
                            .append(titulo || sub ? "<w:b/>" : "")
                            .append("<w:sz w:val=\"").append(titulo ? 30 : sub ? 24 : 22).append("\"/></w:rPr>")
                            .append("<w:t xml:space=\"preserve\">").append(xml(t)).append("</w:t></w:r>");
                }
                corpo.append("</w:p>");
            }
            entrada(z, "word/document.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:body>"
                    + corpo
                    + "<w:sectPr><w:pgSz w:w=\"11906\" w:h=\"16838\"/>"
                    + "<w:pgMar w:top=\"1417\" w:right=\"1417\" w:bottom=\"1417\" w:left=\"1417\" w:header=\"708\" w:footer=\"708\" w:gutter=\"0\"/>"
                    + "</w:sectPr></w:body></w:document>");
        }
    }

    private static void entrada(ZipOutputStream z, String nome, String conteudo) throws Exception {
        z.putNextEntry(new ZipEntry(nome));
        z.write(conteudo.getBytes(StandardCharsets.UTF_8));
        z.closeEntry();
    }

    private static String xml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    /** O mesmo texto em HTML, para pré-visualizar e imprimir. */
    public static String html(String texto) {
        StringBuilder h = new StringBuilder("<html><body style='font-family:Calibri,Arial;font-size:11pt'>");
        for (String linha : texto.replace("\r", "").split("\n", -1)) {
            if (linha.startsWith("# ")) {
                h.append("<p style='text-align:center;font-size:15pt'><b>").append(xml(linha.substring(2))).append("</b></p>");
            } else if (linha.startsWith("## ")) {
                h.append("<p><b>").append(xml(linha.substring(3))).append("</b></p>");
            } else {
                h.append("<p>").append(linha.isEmpty() ? "&nbsp;" : xml(linha)).append("</p>");
            }
        }
        return h.append("</body></html>").toString();
    }
}
