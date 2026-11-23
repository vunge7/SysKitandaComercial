package rh.web;

import java.awt.Window;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import util.ui.ComponentesWeb;
import util.ui.FormularioWeb;

/**
 * Tabelas auxiliares do RH, partilhadas pelos ecrãs: feriados do ano (com as
 * pontes e tolerâncias decretadas) e as definições (dias de férias, prazos
 * do processo disciplinar).
 */
public final class TabelasRH {

    private TabelasRH() {
    }

    // ---------------------------------------------------------------- feriados
    /** Edita os feriados de um ano. Sem nada gravado, mostra os nacionais para confirmar. */
    public static void feriados(Window dono) {
        int anoActual = LocalDate.now().getYear();
        JComboBox<Integer> cbAno = new JComboBox<>(new Integer[]{anoActual - 1, anoActual, anoActual + 1});
        cbAno.setSelectedItem(anoActual);
        FormularioWeb.combo(cbAno);
        DefaultTableModel m = new DefaultTableModel(new String[]{"Data (dd/mm/aaaa)", "Designação"}, 0);
        JTable t = new JTable(m);
        comercial.caixa.UiCaixa.tabela(t, new int[]{140, 420}, ComponentesWeb.AZUL);
        Runnable carregar = () -> {
            m.setRowCount(0);
            try (Connection cn = DadosRH.ligacao()) {
                for (Map.Entry<LocalDate, String> e : DadosRH.feriados(cn, (Integer) cbAno.getSelectedItem()).entrySet()) {
                    m.addRow(new Object[]{UiRH.data(e.getKey()), e.getValue()});
                }
            } catch (Exception e) {
                UiRH.erro(dono, e);
            }
        };
        cbAno.addActionListener(e -> carregar.run());
        carregar.run();

        JButton novo = UiRH.botao("Acrescentar", ComponentesWeb.Variante.SECUNDARIO, "Ponte, tolerância ou feriado municipal",
                () -> m.addRow(new Object[]{"", ""}));
        JButton tirar = UiRH.botao("Retirar", ComponentesWeb.Variante.SECUNDARIO, "Retirar a linha seleccionada", () -> {
            int l = t.getSelectedRow();
            if (l >= 0) {
                m.removeRow(t.convertRowIndexToModel(l));
            }
        });
        JButton nacionais = UiRH.botao("Repor nacionais", ComponentesWeb.Variante.SECUNDARIO, "Voltar à lista de feriados nacionais de Angola", () -> {
            m.setRowCount(0);
            for (Map.Entry<LocalDate, String> e : CalendarioRH.feriadosNacionais((Integer) cbAno.getSelectedItem()).entrySet()) {
                m.addRow(new Object[]{UiRH.data(e.getKey()), e.getValue()});
            }
        });
        JDialog[] d = new JDialog[1];
        JButton gravar = UiRH.botao("Gravar", ComponentesWeb.Variante.SUCESSO, null, () -> {
            if (t.isEditing()) {
                t.getCellEditor().stopCellEditing();
            }
            int ano = (Integer) cbAno.getSelectedItem();
            Map<LocalDate, String> f = new LinkedHashMap<>();
            for (int i = 0; i < m.getRowCount(); i++) {
                String dt = String.valueOf(m.getValueAt(i, 0)).trim(), des = String.valueOf(m.getValueAt(i, 1)).trim();
                if (dt.isEmpty() && des.isEmpty()) {
                    continue;
                }
                LocalDate data;
                try {
                    data = LocalDate.parse(dt, UiRH.DATA);
                } catch (DateTimeParseException ex) {
                    UiRH.aviso(d[0], "Data inválida na linha " + (i + 1) + ": \"" + dt + "\" (use dd/mm/aaaa).");
                    return;
                }
                if (data.getYear() != ano) {
                    UiRH.aviso(d[0], "A data " + dt + " não é de " + ano + ".");
                    return;
                }
                f.put(data, des.isEmpty() ? "Feriado" : des);
            }
            try (Connection cn = DadosRH.ligacao()) {
                DadosRH.gravarFeriados(cn, ano, f);
            } catch (Exception e) {
                throw UiRH.falha(e);
            }
            UiRH.info(d[0], f.size() + " feriados gravados para " + ano + ".");
        });
        JButton fechar = UiRH.botao("Fechar", ComponentesWeb.Variante.PERIGO, null, () -> d[0].dispose());
        d[0] = UiRH.dialogo(dono, "Feriados", FormularioWeb.pilha(8,
                FormularioWeb.grelha(140, 2, ComponentesWeb.campo("Ano", cbAno),
                        FormularioWeb.dica("Fins de semana e feriados não contam como dias úteis (férias e faltas).")),
                FormularioWeb.dica("Acrescente as pontes e tolerâncias decretadas; o que gravar substitui a lista do ano.")),
                UiRH.rolagem(t), 680, 560, novo, tirar, nacionais, gravar, fechar);
        d[0].setVisible(true);
    }

    // ---------------------------------------------------------------- definições
    /** Dias de férias por ano e prazos do processo disciplinar. */
    public static void definicoes(Window dono) {
        JSpinner spFerias, spAntecedencia, spDecisao, spInstauracao;
        try (Connection cn = DadosRH.ligacao()) {
            spFerias = numero(DadosRH.configInt(cn, DadosRH.C_DIAS_FERIAS, 22), 0, 60);
            spAntecedencia = numero(DadosRH.configInt(cn, DadosRH.C_ANTECEDENCIA_ENTREVISTA, 3), 0, 30);
            spDecisao = numero(DadosRH.configInt(cn, DadosRH.C_PRAZO_DECISAO, 30), 1, 180);
            spInstauracao = numero(DadosRH.configInt(cn, DadosRH.C_PRAZO_INSTAURACAO, 30), 1, 365);
        } catch (Exception e) {
            UiRH.erro(dono, e);
            return;
        }
        JDialog[] d = new JDialog[1];
        JButton gravar = UiRH.botao("Gravar", ComponentesWeb.Variante.SUCESSO, null, () -> {
            try (Connection cn = DadosRH.ligacao()) {
                DadosRH.gravarConfig(cn, DadosRH.C_DIAS_FERIAS, String.valueOf(spFerias.getValue()));
                DadosRH.gravarConfig(cn, DadosRH.C_ANTECEDENCIA_ENTREVISTA, String.valueOf(spAntecedencia.getValue()));
                DadosRH.gravarConfig(cn, DadosRH.C_PRAZO_DECISAO, String.valueOf(spDecisao.getValue()));
                DadosRH.gravarConfig(cn, DadosRH.C_PRAZO_INSTAURACAO, String.valueOf(spInstauracao.getValue()));
            } catch (Exception e) {
                throw UiRH.falha(e);
            }
            d[0].dispose();
        });
        JButton fechar = UiRH.botao("Cancelar", ComponentesWeb.Variante.PERIGO, null, () -> d[0].dispose());
        d[0] = UiRH.dialogo(dono, "Definições do RH",
                FormularioWeb.dica("Valores por omissão; confirme-os com o jurista da empresa."),
                FormularioWeb.pilha(10,
                        UiRH.rotuloSecao("Férias"),
                        ComponentesWeb.campo("Dias úteis de férias por ano (ano completo)", spFerias),
                        UiRH.rotuloSecao("Processo disciplinar"),
                        ComponentesWeb.campo("Prazo para instaurar, a contar do conhecimento dos factos (dias)", spInstauracao),
                        ComponentesWeb.campo("Antecedência mínima da entrevista após a convocatória (dias úteis)", spAntecedencia),
                        ComponentesWeb.campo("Prazo para a decisão, a contar da entrevista (dias)", spDecisao)),
                560, 460, gravar, fechar);
        d[0].setVisible(true);
    }

    private static JSpinner numero(int valor, int min, int max) {
        JSpinner s = new JSpinner(new SpinnerNumberModel(Math.max(min, Math.min(max, valor)), min, max, 1));
        FormularioWeb.spinner(s);
        return s;
    }
}
