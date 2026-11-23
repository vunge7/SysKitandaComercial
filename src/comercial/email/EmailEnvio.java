package comercial.email;

import comercial.whatsapp.RelatorioWhatsApp;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Envio dos relatórios por e-mail (SMTP). Gratuito com uma conta Gmail e uma
 * "palavra-passe de aplicação" (Conta Google > Segurança > Palavras-passe de
 * aplicações). Por omissão usa a conta da DVML que o sistema já usava no
 * fecho de caixa (util.email.EnvioEmailUtilFecho); tudo editável no formulário.
 *
 * Limite do Gmail gratuito: cerca de 500 destinatários por dia por conta. Por
 * isso cada contacto recebe UM e-mail por horário, com todos os PDFs anexados.
 */
public final class EmailEnvio {

    public static final String C_SERVIDOR = "smtp.servidor";
    public static final String C_PORTA = "smtp.porta";
    public static final String C_SEGURANCA = "smtp.seguranca";
    public static final String C_UTILIZADOR = "smtp.utilizador";
    public static final String C_SENHA = "smtp.senha";
    public static final String C_NOME_REMETENTE = "remetente.nome";
    public static final String C_AUTOMATICO = "automatico";
    public static final String C_TOLERANCIA = "tolerancia_min";

    public static final String SSL = "SSL";
    public static final String STARTTLS = "STARTTLS";

    /** Conta gratuita da DVML (Gmail) — a mesma do envio do fecho de caixa. */
    public static final String SERVIDOR_PADRAO = "smtp.gmail.com";
    public static final String PORTA_PADRAO = "465";
    public static final String CONTA_PADRAO = "dvml.app@gmail.com";
    static final String SENHA_PADRAO = "lezd hdjp zjtf qgaq";

    private final Map<String, String> cfg;

    public EmailEnvio(Map<String, String> cfg) {
        this.cfg = cfg;
    }

    public static EmailEnvio daBase(Connection cn) {
        return new EmailEnvio(EmailDados.lerConfig(cn));
    }

    public String get(String chave, String omissao) {
        String v = cfg.get(chave);
        return v == null || v.trim().isEmpty() ? omissao : v.trim();
    }

    public String servidor() {
        return get(C_SERVIDOR, SERVIDOR_PADRAO);
    }

    public int porta() {
        try {
            return Integer.parseInt(get(C_PORTA, PORTA_PADRAO));
        } catch (NumberFormatException e) {
            return 465;
        }
    }

    public String seguranca() {
        return STARTTLS.equals(get(C_SEGURANCA, SSL)) ? STARTTLS : SSL;
    }

    public String utilizador() {
        return get(C_UTILIZADOR, CONTA_PADRAO);
    }

    /** A senha gravada; sem nada gravado e com a conta da DVML, a senha desta. */
    String senha() {
        String s = get(C_SENHA, null);
        return s != null ? s : CONTA_PADRAO.equalsIgnoreCase(utilizador()) ? SENHA_PADRAO : "";
    }

    public boolean usaContaPadrao() {
        return CONTA_PADRAO.equalsIgnoreCase(utilizador()) && get(C_SENHA, null) == null;
    }

    public String nomeRemetente(Connection cn) {
        String n = get(C_NOME_REMETENTE, null);
        return n != null ? n : nomeHotel(cn);
    }

    public boolean automaticoLigado() {
        return !"0".equals(get(C_AUTOMATICO, "1"));
    }

    public int toleranciaMin() {
        try {
            return Math.max(5, Math.min(720, Integer.parseInt(get(C_TOLERANCIA, "240"))));
        } catch (NumberFormatException e) {
            return 240;
        }
    }

    /** O que falta configurar; null se estiver pronto. */
    public String problemaConfiguracao() {
        if (EmailDados.normalizarEmail(utilizador()) == null) {
            return "A conta de e-mail que envia não é válida.";
        }
        if (senha().isEmpty()) {
            return "Falta a palavra-passe (de aplicação) da conta " + utilizador() + ".";
        }
        return null;
    }

    // =====================================================================
    // ENVIO
    // =====================================================================
    /**
     * Um e-mail para {@code para} com todos os relatórios: o texto de cada um no
     * corpo e os PDFs em anexo. Devolve o detalhe para o histórico; lança
     * excepção com a explicação se falhar.
     */
    public String enviar(Connection cn, String para, List<RelatorioWhatsApp.Resultado> relatorios) throws Exception {
        String problema = problemaConfiguracao();
        if (problema != null) {
            throw new IllegalStateException(problema);
        }
        String hotel = nomeHotel(cn);
        StringBuilder titulos = new StringBuilder();
        StringBuilder corpo = new StringBuilder();
        Multipart partes = new MimeMultipart();
        int anexos = 0;
        for (RelatorioWhatsApp.Resultado r : relatorios) {
            titulos.append(titulos.length() == 0 ? "" : " + ").append(r.titulo);
            corpo.append(corpo.length() == 0 ? "" : "\n\n────────────\n\n").append(r.texto == null ? r.titulo : r.texto.replace("*", ""));
            if (r.pdf != null && r.pdf.exists()) {
                corpo.append("\n(em anexo: ").append(r.pdf.getName()).append(")");
                MimeBodyPart anexo = new MimeBodyPart();
                anexo.setDisposition(Part.ATTACHMENT);
                anexo.setDataHandler(new DataHandler(new FileDataSource(r.pdf)));
                anexo.setFileName(r.pdf.getName());
                partes.addBodyPart(anexo);
                anexos++;
            }
        }
        corpo.append("\n\n--\n").append(hotel).append(" · enviado automaticamente pelo Kitanda em ")
                .append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append(".");
        MimeBodyPart texto = new MimeBodyPart();
        texto.setText(corpo.toString(), "UTF-8");
        partes.addBodyPart(texto, 0);

        MimeMessage m = new MimeMessage(sessao());
        m.setFrom(new InternetAddress(utilizador(), nomeRemetente(cn), "UTF-8"));
        m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
        m.setSubject(hotel + " · " + titulos + " · " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "UTF-8");
        m.setContent(partes);
        m.setSentDate(new Date());
        Transport t = sessao().getTransport("smtp");
        try {
            t.connect(servidor(), porta(), utilizador(), senha());
            m.saveChanges();
            t.sendMessage(m, m.getAllRecipients());
        } finally {
            try {
                t.close();
            } catch (Exception ignorado) {
            }
        }
        return "de " + utilizador() + (anexos > 0 ? " · " + anexos + " PDF" : "") + " · " + m.getMessageID();
    }

    /**
     * E-mail em HTML com uma imagem no corpo (cid:postal) e o texto simples
     * como alternativa (ex.: postal de aniversário).
     */
    public String enviarHtmlComImagem(Connection cn, String para, String assunto, String html, String textoSimples, File imagem)
            throws Exception {
        String problema = problemaConfiguracao();
        if (problema != null) {
            throw new IllegalStateException(problema);
        }
        MimeMultipart relacionado = new MimeMultipart("related");
        MimeBodyPart corpo = new MimeBodyPart();
        corpo.setContent(html, "text/html; charset=UTF-8");
        relacionado.addBodyPart(corpo);
        MimeBodyPart embutida = new MimeBodyPart();
        embutida.setDataHandler(new DataHandler(new FileDataSource(imagem)));
        embutida.setHeader("Content-ID", "<postal>");
        embutida.setDisposition(Part.INLINE);
        embutida.setFileName(imagem.getName());
        relacionado.addBodyPart(embutida);

        MimeMultipart alternativa = new MimeMultipart("alternative");
        MimeBodyPart simples = new MimeBodyPart();
        simples.setText(textoSimples, "UTF-8");
        alternativa.addBodyPart(simples);
        MimeBodyPart comHtml = new MimeBodyPart();
        comHtml.setContent(relacionado);
        alternativa.addBodyPart(comHtml);

        MimeMessage m = new MimeMessage(sessao());
        m.setFrom(new InternetAddress(utilizador(), nomeRemetente(cn), "UTF-8"));
        m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
        m.setSubject(assunto, "UTF-8");
        m.setContent(alternativa);
        m.setSentDate(new Date());
        Transport t = sessao().getTransport("smtp");
        try {
            t.connect(servidor(), porta(), utilizador(), senha());
            m.saveChanges();
            t.sendMessage(m, m.getAllRecipients());
        } finally {
            try {
                t.close();
            } catch (Exception ignorado) {
            }
        }
        return "de " + utilizador() + " · " + m.getMessageID();
    }

    private Session sessao() {
        Properties p = new Properties();
        p.put("mail.smtp.host", servidor());
        p.put("mail.smtp.port", String.valueOf(porta()));
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.connectiontimeout", "20000");
        p.put("mail.smtp.timeout", "60000");
        p.put("mail.smtp.writetimeout", "60000");
        if (SSL.equals(seguranca())) {
            p.put("mail.smtp.ssl.enable", "true");
        } else {
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.starttls.required", "true");
        }
        return Session.getInstance(p);
    }

    /** Nome comercial do hotel (tb_dados_instituicao). */
    static String nomeHotel(Connection cn) {
        try (PreparedStatement ps = cn.prepareStatement("SELECT nome, nome FROM tb_dados_instituicao LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String n = rs.getString(1);
                if (n == null || n.trim().isEmpty()) {
                    n = rs.getString(2);
                }
                if (n != null && !n.trim().isEmpty()) {
                    return n.trim();
                }
            }
        } catch (Exception e) {
            System.err.println("[E-mail] nome da empresa: " + e.getMessage());
        }
        return "Kitanda";
    }

    /** Usado na pasta dos PDFs (a mesma do WhatsApp). */
    public static File pasta() {
        return RelatorioWhatsApp.PASTA;
    }
}
