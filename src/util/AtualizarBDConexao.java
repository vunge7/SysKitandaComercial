package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AtualizarBDConexao {

    public static void main(String[] args) throws IOException {
        Path pastaRaiz = Paths.get("C:/Users/marti/Documents/Projectos_2025/DeskTop/Comercial/KITANDA_NOVO_GIT/SysKitandaComercial/src");
        System.out.println("🔍 Procurando arquivos em: " + pastaRaiz);

        AtomicInteger filesUpdated = new AtomicInteger();
        AtomicInteger totalReplacements = new AtomicInteger();

        try (Stream<Path> stream = Files.walk(pastaRaiz)) {
            stream.filter(p -> p.toString().endsWith(".java"))
                  .forEach(file -> {
                      try {
                          int replaced = atualizarArquivo(file);
                          if (replaced > 0) {
                              filesUpdated.incrementAndGet();
                              totalReplacements.addAndGet(replaced);
                              System.out.println("✅ Atualizado: " + file + "  (substituições: " + replaced + ")");
                          }
                      } catch (IOException e) {
                          System.err.println("❌ Erro ao processar " + file + ": " + e.getMessage());
                      }
                  });
        }

        System.out.println("✨ Atualização concluída! Ficheiros alterados: " + filesUpdated + ", substituições: " + totalReplacements);
    }

    private static int atualizarArquivo(Path file) throws IOException {
        String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        String original = content;
        int count = 0;

        // 1️⃣ Correções de conexão antigas
        content = content.replaceAll("\\bconexao\\.getConnection1\\s*\\(\\s*\\)", "conexao.getConnectionAtiva()");
        content = content.replaceAll("\\bBDConexao\\.conectar\\s*\\(\\s*\\)", "conexao.getConnectionAtiva()");
        content = content.replaceAll("\\bconexao\\.conectar\\s*\\(\\s*\\)", "conexao.getConnectionAtiva()");
        content = content.replaceAll("import\\s+com\\.mysql\\.jdbc\\.Connection\\s*;\\s*", "");

        // 2️⃣ Adiciona import java.sql.Connection se não existir
        if (!content.contains("import java.sql.Connection;")) {
            content = content.replaceFirst("(package\\s+[\\w\\.]+;\\s*)", "$1\nimport java.sql.Connection;\n");
        }

        // 3️⃣ Substitui new ...(arg, BDConexao.getInstancia()).setVisible(true) -> new ... (arg, BDConexao.getInstancia()).setVisible(true)
        Pattern p1 = Pattern.compile(
            "new\\s+([A-Za-z0-9_$.]+)\\s*\\(\\s*([^,()]+?)\\s*,\\s*this\\.conexao\\s*\\)\\s*\\.show\\s*\\(\\s*\\)",
            Pattern.MULTILINE
        );
        Matcher m1 = p1.matcher(content);
        StringBuffer sb1 = new StringBuffer();
        int c1 = 0;
        while (m1.find()) {
            String classe = m1.group(1);
            String primeiroArg = m1.group(2).trim();
            String replacement = "new " + classe + "(" + primeiroArg + ", BDConexao.getInstancia()).setVisible(true)";
            m1.appendReplacement(sb1, Matcher.quoteReplacement(replacement));
            c1++;
        }
        m1.appendTail(sb1);
        if (c1 > 0) {
            content = sb1.toString();
            count += c1;
        }

        // 4️⃣ Variante: múltiplos args antes do this.conexao
        Pattern p2 = Pattern.compile(
            "new\\s+([A-Za-z0-9_$.]+)\\s*\\((.*?)\\,\\s*this\\.conexao\\s*\\)\\s*\\.show\\s*\\(\\s*\\)",
            Pattern.DOTALL
        );
        Matcher m2 = p2.matcher(content);
        StringBuffer sb2 = new StringBuffer();
        int c2 = 0;
        while (m2.find()) {
            String classe = m2.group(1);
            String priorArgs = m2.group(2).trim();
            if (priorArgs.endsWith(",")) priorArgs = priorArgs.substring(0, priorArgs.length() - 1).trim();
            String replacement = "new " + classe + "(" + priorArgs + ", BDConexao.getInstancia()).setVisible(true)";
            m2.appendReplacement(sb2, Matcher.quoteReplacement(replacement));
            c2++;
        }
        m2.appendTail(sb2);
        if (c2 > 0) {
            content = sb2.toString();
            count += c2;
        }

        // 5️⃣ Substitui chamadas .setVisible(true) genéricas após construtores
        Pattern p3 = Pattern.compile(
            "(new\\s+[A-Za-z0-9_$.]+\\s*\\(.*?\\))\\s*\\.show\\s*\\(\\s*\\)\\s*;",
            Pattern.DOTALL
        );
        Matcher m3 = p3.matcher(content);
        StringBuffer sb3 = new StringBuffer();
        int c3 = 0;
        while (m3.find()) {
            String constructorExpr = m3.group(1);
            String replacement = constructorExpr + ".setVisible(true);";
            m3.appendReplacement(sb3, Matcher.quoteReplacement(replacement));
            c3++;
        }
        m3.appendTail(sb3);
        if (c3 > 0) {
            content = sb3.toString();
            count += c3;
        }

        // 6️⃣ Nova substituição pedida:
        // ( ..., BDConexao.getInstancia()).setVisible(true) -> ( ..., BDConexao.getInstancia()).setVisible(true)
        Pattern p4 = Pattern.compile(
            "\\(\\s*(.*?)\\s*,\\s*conexao\\s*\\)\\s*\\.setVisible\\s*\\(\\s*true\\s*\\)",
            Pattern.DOTALL
        );
        Matcher m4 = p4.matcher(content);
        StringBuffer sb4 = new StringBuffer();
        int c4 = 0;
        while (m4.find()) {
            String argsAntes = m4.group(1).trim();
            String replacement = "(" + argsAntes + ", BDConexao.getInstancia()).setVisible(true)";
            m4.appendReplacement(sb4, Matcher.quoteReplacement(replacement));
            c4++;
        }
        m4.appendTail(sb4);
        if (c4 > 0) {
            content = sb4.toString();
            count += c4;
        }

        // 7️⃣ Correções finais
        content = content.replaceAll("\\.show\\s*\\(\\s*\\)\\s*;", ".setVisible(true);");
        content = content.replaceAll("\\.show\\s*\\(\\s*\\)", ".setVisible(true)");

        if (!original.equals(content) && count > 0) {
            Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        }

        return count;
    }
}
