package util;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import kitanda.util.CfMethods;

/**
 * Liga três campos (preço de compra, percentagem de ganho e preço de venda) e
 * mantém-nos sincronizados:
 * <ul>
 * <li>mudou a percentagem ou o preço de compra -> recalcula o preço de venda</li>
 * <li>mudou o preço de venda -> recalcula a percentagem</li>
 * </ul>
 *
 * O preço de compra pode ser um {@link JSpinner} ou um {@link JTextField}.
 * Cada instância tem a sua própria flag anti-loop, por isso podes ter vários
 * grupos (retalho, grosso, ...) no mesmo formulário.
 *
 * Opções (encadeáveis antes do ligar()):
 * <ul>
 * <li>{@link #pontoDecimal()}: lê e escreve números como 1234.56 (sem
 * separador de milhares), o formato de Double.parseDouble / Double.toString.
 * Usa isto em formulários que guardam com Double.parseDouble.</li>
 * <li>{@link #somenteComFoco()}: só recalcula quando o utilizador está a editar
 * o campo. Evita que setText() feito por código (carregar produto, limpar
 * campos) dispare cálculos e altere preços guardados.</li>
 * </ul>
 *
 * Exemplo:
 * <pre>
 * new CalculoPrecoVenda(txtPrecoCompra, txtPercentagem, txtPrecoVenda)
 *         .pontoDecimal().somenteComFoco().ligar();
 * </pre>
 */
public class CalculoPrecoVenda {

    /**
     * Formatador por omissão (modo vírgula): 1.234,56. Para usar o teu, chama
     * CalculoPrecoVenda.setFormatador(valor -> formatarMoeda(valor)).
     */
    private static Function<Double, String> formatador = valor -> {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.US);
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        return new DecimalFormat("#,##0.00", simbolos).format(valor);
    };

    private final JComponent precoCompra;
    private final JTextComponent percentagemGanho;
    private final JTextComponent precoVenda;
    private boolean atualizando = false;
    private boolean pontoDecimal = false;
    private boolean somenteComFoco = false;

    public CalculoPrecoVenda(JComponent precoCompra,
            JTextComponent percentagemGanho,
            JTextComponent precoVenda) {
        this.precoCompra = precoCompra;
        this.percentagemGanho = percentagemGanho;
        this.precoVenda = precoVenda;
    }

    public static void setFormatador(Function<Double, String> novoFormatador) {
        formatador = novoFormatador;
    }

    /**
     * Números no formato 1234.56 (ponto decimal, sem milhares).
     */
    public CalculoPrecoVenda pontoDecimal() {
        this.pontoDecimal = true;
        return this;
    }

    /**
     * Só recalcula quando o campo alterado tem o foco (edição do utilizador).
     */
    public CalculoPrecoVenda somenteComFoco() {
        this.somenteComFoco = true;
        return this;
    }

    /**
     * Regista os listeners nos três campos.
     */
    public void ligar() {
        aoAlterar(percentagemGanho, () -> aoEditar(percentagemGanho, this::calcularPrecoVenda));
        aoAlterar(precoCompra, () -> aoEditar(precoCompra, this::calcularPrecoVenda));
        aoAlterar(precoVenda, () -> aoEditar(precoVenda, this::calcularPercentagemGanho));
    }

    private void aoEditar(JComponent origem, Runnable acao) {
        if (!somenteComFoco || temFoco(origem)) {
            acao.run();
        }
    }

    private static boolean temFoco(JComponent componente) {
        Component foco = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        return foco != null && (foco == componente || SwingUtilities.isDescendingFrom(foco, componente));
    }

    /**
     * Preço de venda = compra * (1 + percentagem / 100).
     */
    public void calcularPrecoVenda() {
        if (atualizando) {
            return;
        }
        atualizando = true;
        try {
            double compra = ler(precoCompra);
            double percentagem = ler(percentagemGanho);
            double venda = compra * (1 + (percentagem / 100.0));

            precoVenda.setText(formatar(venda));
        } catch (Exception e) {
            e.printStackTrace(); // remover depois de testar
            precoVenda.setText(formatar(0));
        } finally {
            atualizando = false;
        }
    }

    /**
     * Percentagem = (venda - compra) / compra * 100.
     */
    public void calcularPercentagemGanho() {
        if (atualizando) {
            return;
        }
        atualizando = true;
        try {
            double compra = ler(precoCompra);
            double venda = ler(precoVenda);

            if (compra <= 0) {
                percentagemGanho.setText(formatar(0));
            } else {
                double percentagem = ((venda - compra) / compra) * 100.0;
                percentagemGanho.setText(formatar(percentagem));
            }
        } catch (Exception e) {
            e.printStackTrace(); // remover depois de testar
            percentagemGanho.setText(formatar(0));
        } finally {
            atualizando = false;
        }
    }

    private String formatar(double valor) {
        if (pontoDecimal) {
            return String.format(Locale.US, "%.2f", valor);
        }
        return formatador.apply(valor);
    }

    private double ler(JComponent componente) {
        if (!pontoDecimal) {
            return lerValor(componente);
        }
        if (componente instanceof JSpinner) {
            Object valor = ((JSpinner) componente).getValue();
            if (valor instanceof Number) {
                return ((Number) valor).doubleValue();
            }
            return parsePonto(String.valueOf(valor));
        }
        if (componente instanceof JTextComponent) {
            return parsePonto(((JTextComponent) componente).getText());
        }
        throw new IllegalArgumentException("Componente não suportado: " + componente.getClass().getName());
    }

    /**
     * Aceita "1234.56", "1234,56" e texto vazio (= 0).
     */
    private static double parsePonto(String texto) {
        String t = texto == null ? "" : texto.trim().replace(" ", "").replace(",", ".");
        if (t.isEmpty() || t.equals(".")) {
            return 0;
        }
        int ultimo = t.lastIndexOf('.');
        if (ultimo != t.indexOf('.')) { // vários pontos: só o último é decimal
            t = t.substring(0, ultimo).replace(".", "") + t.substring(ultimo);
        }
        return Double.parseDouble(t);
    }

    /**
     * Lê o valor numérico de um JSpinner ou de um JTextComponent (modo vírgula,
     * via CfMethods.parseMoedaFormatadaNovo).
     */
    public static double lerValor(JComponent componente) {
        if (componente instanceof JSpinner) {
            Object valor = ((JSpinner) componente).getValue();
            return (valor instanceof Number)
                    ? ((Number) valor).doubleValue()
                    : CfMethods.parseMoedaFormatadaNovo(String.valueOf(valor));
        }
        if (componente instanceof JTextComponent) {
            return CfMethods.parseMoedaFormatadaNovo(((JTextComponent) componente).getText());
        }
        throw new IllegalArgumentException("Componente não suportado: " + componente.getClass().getName());
    }

    /**
     * Liga um ChangeListener (JSpinner) ou um DocumentListener (JTextComponent).
     * Se o documento for trocado depois (setDocument), o listener acompanha.
     */
    public static void aoAlterar(JComponent componente, Runnable acao) {
        if (componente instanceof JSpinner) {
            ((JSpinner) componente).addChangeListener(e -> acao.run());
        } else if (componente instanceof JTextComponent) {
            JTextComponent campo = (JTextComponent) componente;
            DocumentListener listener = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    acao.run();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    acao.run();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    acao.run();
                }
            };
            campo.getDocument().addDocumentListener(listener);

            campo.addPropertyChangeListener("document", evt -> {
                Document antigo = (Document) evt.getOldValue();
                Document novo = (Document) evt.getNewValue();
                if (antigo != null) {
                    antigo.removeDocumentListener(listener);
                }
                if (novo != null) {
                    novo.addDocumentListener(listener);
                }
            });
        } else {
            throw new IllegalArgumentException("Componente não suportado: " + componente.getClass().getName());
        }
    }
}