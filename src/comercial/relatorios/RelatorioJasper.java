package comercial.relatorios;

import comercial.controller.DadosInstituicaoController;
import entity.TbDadosInstituicao;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import util.BDConexao;

/**
 * Peças comuns dos relatórios em iReport/JasperReports feitos a partir dos
 * ecrãs (os dados vêm já filtrados da tabela): cabeçalho com os dados da
 * empresa, compilação do .jrxml quando é preciso e o visualizador.
 * <p>
 * Parâmetros que os .jrxml recebem: EMPRESA, EMPRESA_INFO, TITULO_INFO
 * (período), FILTROS e IMPRESSO.
 */
public final class RelatorioJasper {

    private RelatorioJasper() {
    }

    public static Map<String, Object> parametros(BDConexao conexao, String periodo, String filtros) {
        Map<String, Object> p = new HashMap<>();
        String empresa = "", info = "";
        try {
            TbDadosInstituicao d = (TbDadosInstituicao) new DadosInstituicaoController(
                    conexao != null ? conexao : BDConexao.getInstancia()).findById(1);
            if (d != null) {
                empresa = d.getNome() == null ? "" : d.getNome();
                StringBuilder s = new StringBuilder();
                juntar(s, vazio(d.getNif()) ? null : "NIF " + d.getNif());
                juntar(s, vazio(d.getTelefone()) ? null : "Tel. " + d.getTelefone());
                juntar(s, d.getEnderecos());
                juntar(s, d.getEmail());
                info = s.toString();
            }
        } catch (Exception e) {
            // sem dados da empresa: o relatório sai na mesma
        }
        p.put("EMPRESA", empresa);
        p.put("EMPRESA_INFO", info);
        p.put("TITULO_INFO", periodo == null ? "" : periodo);
        p.put("FILTROS", filtros == null || filtros.isEmpty() ? "" : "Filtros: " + filtros);
        p.put("IMPRESSO", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        p.put("REPORT_LOCALE", new java.util.Locale("pt", "AO"));
        return p;
    }

    public static JasperPrint preencher(String nome, Map<String, Object> param, List<?> linhas) throws Exception {
        return JasperFillManager.fillReport(caminho(nome), param, new JRBeanCollectionDataSource(linhas));
    }

    public static void mostrar(String nome, Map<String, Object> param, List<?> linhas, String titulo) {
        try {
            JasperViewer v = new JasperViewer(preencher(nome, param, linhas), false);
            v.setTitle(titulo);
            v.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            v.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possível gerar o relatório:\n" + e.getMessage());
        }
    }

    /** relatorios/nome.jasper (compilado agora se faltar ou estiver mais velho que o .jrxml). */
    static String caminho(String nome) throws Exception {
        File jasper = new File("relatorios/" + nome + ".jasper").getAbsoluteFile();
        File jrxml = new File("relatorios/" + nome + ".jrxml").getAbsoluteFile();
        if (jrxml.exists() && (!jasper.exists() || jrxml.lastModified() > jasper.lastModified())) {
            try {
                JasperCompileManager.compileReportToFile(jrxml.getPath(), jasper.getPath());
            } catch (Exception e) {
                if (!jasper.exists()) {
                    throw e;
                }
            }
        }
        return jasper.getPath();
    }

    private static boolean vazio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static void juntar(StringBuilder s, String t) {
        if (t != null && !t.trim().isEmpty()) {
            s.append(s.length() > 0 ? "  ·  " : "").append(t.trim());
        }
    }
}
