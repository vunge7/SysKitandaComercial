package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

public class AtualizarBDConexao {

    public static void main(String[] args) {
        // 📂 Caminho da tua pasta "src"
        Path pastaRaiz = Paths.get("C:/Users/marti/Documents/Projectos_2025/DeskTop/Comercial/KITANDA_NOVO_GIT/SysKitandaComercial/src");

        System.out.println("🔍 A procurar e atualizar classes em: " + pastaRaiz);

        try (Stream<Path> stream = Files.walk(pastaRaiz)) {
            stream.filter(p -> p.toString().endsWith(".java"))
                  .forEach(AtualizarBDConexao::atualizarArquivo);
        } catch (IOException e) {
            System.err.println("❌ Erro ao percorrer diretórios: " + e.getMessage());
        }

        System.out.println("✨ Atualização concluída!");
    }

    private static void atualizarArquivo(Path file) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            String original = content;

            // 🔄 1️⃣ Substituir conexao.getConnectionAtiva() por conexao.getConnectionAtiva()
            content = content.replaceAll(
                "\\bconexao\\.getConnection1\\s*\\(\\s*\\)",
                "conexao.getConnectionAtiva()"
            );

            // 🔄 2️⃣ Substituir conexao.getConnectionAtiva() por conexao.getConnectionAtiva()
            content = content.replaceAll(
                "\\bBDConexao\\.conectar\\s*\\(\\s*\\)",
                "conexao.getConnectionAtiva()"
            );
            content = content.replaceAll(
                "\\bconexao\\.conectar\\s*\\(\\s*\\)",
                "conexao.getConnectionAtiva()"
            );

            // 🔄 3️⃣ Remover import antigo de MySQL, se houver
            content = content.replaceAll(
                "import\\s+com\\.mysql\\.jdbc\\.Connection\\s*;\\s*",
                ""
            );

            // 🔄 4️⃣ Garantir import correto de java.sql.Connection
            if (!content.contains("import java.sql.Connection;")) {
                content = content.replaceFirst(
                    "(package\\s+[\\w\\.]+;\\s*)",
                    "$1\nimport java.sql.Connection;\n"
                );
            }

            // 💾 Só grava se houve mudanças
            if (!original.equals(content)) {
                Files.write(file, content.getBytes(StandardCharsets.UTF_8));
                System.out.println("✅ Atualizado: " + file.getFileName());
            }

        } catch (IOException e) {
            System.err.println("❌ Erro ao processar arquivo: " + file.getFileName() + " -> " + e.getMessage());
        }
    }
}
