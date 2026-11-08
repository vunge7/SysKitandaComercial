package util.fe;

import comercial.controller.ClientesController;
import comercial.controller.DadosInstituicaoController;
import comercial.controller.DocumentosController;
import comercial.controller.ItemVendasController;
import comercial.controller.VendasController;
import entity.Documento;
import entity.TbCliente;
import entity.TbDadosInstituicao;
import entity.TbItemVenda;
import entity.TbVenda;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import util.BDConexao;
import util.DVML;
import util.fe.dto.ConsultaEstadoDTO;
import util.fe.dto.SourceDocumentDTO;
import util.fe.http.HttpClientUtil;
import util.fe.payloads.PayloadFactory;

/**
 * MODO CONTINGÊNCIA da Facturação Electrónica.
 *
 * Quando não há internet (ou a AGT não responde) a venda é gravada com
 * tb_venda.estado = 'P' e sem requestID. Esta classe comunica essas facturas
 * à AGT mais tarde, por ordem cronológica, e consulta o estado das que já
 * foram submetidas (P com requestID) até ficarem 'V' ou 'I'.
 *
 * Sem UI: quem chama decide como mostrar o resultado.
 *
 * @author Engº Domingos Dala Vunge
 */
public final class ComunicacaoPendentesFE {

    /** Intervalo do envio automático. */
    public static final long PRIMEIRA_EXECUCAO_SEG = 60;
    public static final long INTERVALO_SEG = 5 * 60;

    private static final long PAUSA_ENTRE_ENVIOS_MS = 800;
    private static final long PAUSA_ENTRE_CONSULTAS_MS = 1000;

    /** Impede dois ciclos ao mesmo tempo (automático + botão). */
    private static final AtomicBoolean A_CORRER = new AtomicBoolean(false);

    /**
     * Nºs que a AGT recusou (duplicado ou dados inválidos) nesta sessão: o
     * ciclo automático não os volta a enviar de 5 em 5 minutos. O botão
     * manual tenta-os sempre.
     */
    private static final Set<String> IGNORAR_NO_AUTOMATICO
            = Collections.synchronizedSet(new HashSet<>());

    private static ScheduledExecutorService agendador;

    private ComunicacaoPendentesFE() {
    }

    // =====================================================
    // RESULTADOS
    // =====================================================
    public enum Tipo {
        ENVIADA, DUPLICADA, ERRO, OFFLINE
    }

    public static class ResultadoEnvio {

        public final TbVenda venda;
        public final Tipo tipo;
        public final String mensagem;

        ResultadoEnvio(TbVenda venda, Tipo tipo, String mensagem) {
            this.venda = venda;
            this.tipo = tipo;
            this.mensagem = mensagem;
        }
    }

    public static class Resumo {

        public int porComunicar;      // encontradas no início
        public int enviadas;
        public int duplicadas;
        public int erros;
        public int validadas;         // V obtidos (no envio ou na consulta)
        public int invalidadas;       // I obtidos
        public boolean semLigacao;    // parou (ou nem começou) por falta de ligação
        public boolean ocupado;       // já havia outro ciclo a correr
        public final List<String> registo = new ArrayList<>();

        /** true se alguma factura mudou na BD. */
        public boolean houveAlteracoes() {
            return enviadas > 0 || validadas > 0 || invalidadas > 0;
        }

        @Override
        public String toString() {
            return "Enviadas: " + enviadas + "  Já existiam: " + duplicadas + "  Erro: " + erros
                    + "  Validadas: " + validadas + "  Inválidas: " + invalidadas
                    + (semLigacao ? "  (sem ligação à AGT)" : "");
        }
    }

    /** Progresso (opcional) para barras de progresso / registo. */
    public interface Ouvinte {

        void progresso(int feitos, int total, String mensagem);
    }

    // =====================================================
    // ENVIO DE UMA VENDA (usado pelo lote e pelo FormularioConsultaEstadoFE)
    // =====================================================
    /**
     * Envia uma venda já gravada (FT, FR, NC ou RC) para a AGT e grava na BD o
     * requestID / estado / submissionUUID devolvidos. Não cria nem altera
     * mais nada na venda. Deve correr fora da EDT e com
     * PayloadFactory.setModoSilencioso(true).
     */
    public static ResultadoEnvio enviarVenda(TbVenda venda, TbDadosInstituicao inst, BDConexao conexao,
            VendasController vendasCtrl, DocumentosController docsCtrl,
            ClientesController clientesCtrl, ItemVendasController itensCtrl) {

        String num = venda.getCodFact();
        try {
            Documento documento = docsCtrl.findDocumentoById(venda.getFkDocumento().getPkDocumento());
            TbCliente cliente = clientesCtrl.findByCodigo(venda.getCodigoCliente().getCodigo());
            boolean recibo = venda.getFkDocumento().getPkDocumento() == DVML.DOC_RECIBO_RC;

            // RC: documentos pagos (amortizacao_divida); FT/FR/NC: itens
            List<SourceDocumentDTO> pagos = null;
            List<TbItemVenda> itens = null;
            int linhas;
            if (recibo) {
                pagos = vendasCtrl.findDocumentosPagosPorRecibo(conexao.getConnectionAtiva(), num);
                linhas = pagos.size();
            } else {
                itens = itensCtrl.listarTodosByCodigoVenda(venda.getCodigo());
                linhas = itens == null ? 0 : itens.size();
            }
            if (linhas == 0) {
                return new ResultadoEnvio(venda, Tipo.ERRO, "✗ " + num + (recibo
                        ? " - recibo sem documentos pagos (amortizações) na base de dados"
                        : " - sem itens na base de dados"));
            }

            PayloadFactory.limparUltimosErros();
            String estadoAnterior = venda.getEstado();
            venda.setRequestID(null); // só em memória: saber se a AGT devolveu um novo
            venda.setEstado(null);    // só em memória: um estado antigo não vale para o novo envio
            boolean enviada = recibo
                    ? FacturaElectronicaUtil.criarFERecibo(venda, inst, documento, cliente, pagos)
                    : FacturaElectronicaUtil.criarFE(venda, inst, documento, cliente, itens, conexao);

            if (FacturaElectronicaUtil.isUltimoEnvioOffline()) {
                venda.setEstado(estadoAnterior);
                return new ResultadoEnvio(venda, Tipo.OFFLINE, "… " + num + " - sem ligação à AGT, fica por comunicar");
            }

            // Não grava se a AGT recusou por nº duplicado: esse RequestID é do
            // pedido recusado, não do documento que já está no repositório
            boolean duplicado = PayloadFactory.ultimoErroNumeroDuplicado();
            String gravacao = "";
            if (!duplicado && venda.getRequestID() != null && !venda.getRequestID().trim().isEmpty()
                    && !venda.getRequestID().equals(num)) {
                if (venda.getEstado() == null || venda.getEstado().trim().isEmpty()) {
                    venda.setEstado(DVML.ESTADO_PENDENTE);
                }
                boolean gravado = vendasCtrl.actualizarEstadoFE(venda, conexao.getConnectionAtiva());
                gravacao = gravado ? " [gravado na BD]" : " [ATENÇÃO: não foi possível gravar na BD]";
            } else {
                venda.setEstado(estadoAnterior);
            }

            if (duplicado) {
                return new ResultadoEnvio(venda, Tipo.DUPLICADA,
                        "= " + num + " - já constava no repositório da AGT (não repetida)");
            }
            if (enviada && venda.getRequestID() != null) {
                return new ResultadoEnvio(venda, Tipo.ENVIADA, "✓ " + num + " (" + linhas
                        + (recibo ? " doc. pagos" : " itens") + ") - RequestID: " + venda.getRequestID()
                        + ", Estado: " + textoEstado(venda.getEstado()) + gravacao);
            }
            if (!gravacao.isEmpty()) {
                // A AGT aceitou a submissão (tem requestID) mas a validação ainda não veio
                return new ResultadoEnvio(venda, Tipo.ENVIADA, "✓ " + num + " - RequestID: "
                        + venda.getRequestID() + ", Estado: " + textoEstado(venda.getEstado()) + gravacao);
            }
            List<String> erros = PayloadFactory.getUltimosErros();
            return new ResultadoEnvio(venda, Tipo.ERRO, "✗ " + num + " - " + (erros.isEmpty()
                    ? "sem resposta válida da AGT" : String.join(" | ", erros)));

        } catch (Exception e) {
            e.printStackTrace();
            if (AgtConectividade.isErroDeRede(e)) {
                AgtConectividade.marcarIndisponivel();
                return new ResultadoEnvio(venda, Tipo.OFFLINE, "… " + num + " - sem ligação à AGT");
            }
            return new ResultadoEnvio(venda, Tipo.ERRO, "✗ " + num + ": " + e.getClass().getSimpleName()
                    + " - " + (e.getMessage() != null ? e.getMessage() : "erro desconhecido"));
        }
    }

    // =====================================================
    // CICLO COMPLETO: enviar as por comunicar + consultar as pendentes
    // =====================================================
    /**
     * @param conexao ligação própria (não a da caixa)
     * @param automatico true no ciclo de 5 em 5 min (salta as recusadas na sessão)
     * @param ouvinte opcional
     */
    public static Resumo comunicarPorEnviar(BDConexao conexao, boolean automatico, Ouvinte ouvinte) {
        Resumo resumo = new Resumo();
        if (!A_CORRER.compareAndSet(false, true)) {
            resumo.ocupado = true;
            resumo.registo.add("Já está a decorrer uma comunicação com a AGT.");
            return resumo;
        }
        PayloadFactory.setModoSilencioso(true);
        HttpClientUtil.setTimeoutsThread(10000, 20000);
        try {
            if (!automatico) {
                AgtConectividade.invalidarCache();
            }
            if (!AgtConectividade.agtDisponivel()) {
                resumo.semLigacao = true;
                resumo.registo.add("Sem ligação à AGT. As facturas continuam por comunicar.");
                return resumo;
            }

            VendasController vendasCtrl = new VendasController(conexao);
            DocumentosController docsCtrl = new DocumentosController(conexao);
            ClientesController clientesCtrl = new ClientesController(conexao);
            ItemVendasController itensCtrl = new ItemVendasController(conexao);
            TbDadosInstituicao inst = new DadosInstituicaoController(conexao).findByCodigo(1);

            // ---- 1. Enviar as que a AGT nunca recebeu (ordem cronológica)
            List<Integer> codigos = vendasCtrl.findCodigosVendasPorComunicarAGT(conexao.getConnectionAtiva());
            List<Integer> aguardam = vendasCtrl.findCodigosVendasAguardandoAGT(conexao.getConnectionAtiva());
            resumo.porComunicar = codigos.size();
            int total = codigos.size() + aguardam.size();
            int feitos = 0;

            for (Integer cod : codigos) {
                TbVenda v = (TbVenda) vendasCtrl.findById(cod);
                feitos++;
                if (v == null || v.getCodFact() == null) {
                    continue;
                }
                if (automatico && IGNORAR_NO_AUTOMATICO.contains(v.getCodFact().trim())) {
                    continue;
                }
                ResultadoEnvio r = enviarVenda(v, inst, conexao, vendasCtrl, docsCtrl, clientesCtrl, itensCtrl);
                resumo.registo.add(r.mensagem);
                notificar(ouvinte, feitos, total, r.mensagem);

                if (r.tipo == Tipo.OFFLINE) {
                    resumo.semLigacao = true;
                    return resumo; // a ligação caiu: as seguintes ficam para o próximo ciclo
                }
                switch (r.tipo) {
                    case ENVIADA:
                        resumo.enviadas++;
                        contarEstado(resumo, r.venda.getEstado());
                        break;
                    case DUPLICADA:
                        resumo.duplicadas++;
                        IGNORAR_NO_AUTOMATICO.add(v.getCodFact().trim());
                        break;
                    default:
                        resumo.erros++;
                        IGNORAR_NO_AUTOMATICO.add(v.getCodFact().trim());
                        break;
                }
                dormir(PAUSA_ENTRE_ENVIOS_MS);
            }

            // ---- 2. Consultar as já submetidas que aguardam validação
            String nif = inst.getNif();
            for (Integer cod : aguardam) {
                TbVenda v = (TbVenda) vendasCtrl.findById(cod);
                feitos++;
                if (v == null) {
                    continue;
                }
                ConsultaEstadoDTO dto = ConsultaEstadoFacturaUtil.consultarEActualizar(
                        v, nif, vendasCtrl, conexao.getConnectionAtiva());
                String msg = v.getCodFact() + " - " + (dto.getMensagem() == null ? "" : dto.getMensagem());
                resumo.registo.add(msg);
                notificar(ouvinte, feitos, total, msg);
                if (dto.isEstadoAlterado()) {
                    contarEstado(resumo, dto.getEstadoLocal());
                }
                if ("E98".equals(dto.getErrorCode())) {
                    break; // demasiados pedidos: tentar mais tarde
                }
                dormir(PAUSA_ENTRE_CONSULTAS_MS);
            }
            return resumo;
        } catch (Exception e) {
            e.printStackTrace();
            resumo.registo.add("Erro na comunicação com a AGT: " + e.getMessage());
            return resumo;
        } finally {
            HttpClientUtil.limparTimeoutsThread();
            PayloadFactory.setModoSilencioso(false);
            A_CORRER.set(false);
        }
    }

    // =====================================================
    // ENVIO AUTOMÁTICO
    // =====================================================
    /**
     * Arranca o envio automático (uma só vez por aplicação). Cada ciclo usa
     * uma ligação à BD própria, para não interferir com as transacções da
     * caixa. aoTerminar (opcional) é chamado na EDT quando alguma factura
     * mudou de estado.
     */
    public static synchronized void iniciarAgendamento(Consumer<Resumo> aoTerminar) {
        if (agendador != null) {
            return;
        }
        agendador = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "FE-Contingencia");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
        agendador.scheduleWithFixedDelay(() -> {
            BDConexao conexao = null;
            try {
                conexao = new BDConexao();
                int porComunicar = new VendasController(conexao).contarPorComunicarAGT(conexao.getConnectionAtiva());
                int aguardam = new VendasController(conexao)
                        .findCodigosVendasAguardandoAGT(conexao.getConnectionAtiva()).size();
                if (porComunicar == 0 && aguardam == 0) {
                    return;
                }
                Resumo r = comunicarPorEnviar(conexao, true, null);
                System.out.println("[FE-Contingência] " + r);
                if (aoTerminar != null && r.houveAlteracoes()) {
                    SwingUtilities.invokeLater(() -> aoTerminar.accept(r));
                }
            } catch (Throwable t) {
                // nunca deixar o agendador morrer
                t.printStackTrace();
            } finally {
                if (conexao != null) {
                    conexao.close();
                }
            }
        }, PRIMEIRA_EXECUCAO_SEG, INTERVALO_SEG, TimeUnit.SECONDS);
        System.out.println("[FE-Contingência] Envio automático activo (de " + (INTERVALO_SEG / 60) + " em "
                + (INTERVALO_SEG / 60) + " min).");
    }

    public static synchronized void pararAgendamento() {
        if (agendador != null) {
            agendador.shutdownNow();
            agendador = null;
        }
    }

    // =====================================================
    // AUXILIARES
    // =====================================================
    private static void contarEstado(Resumo resumo, String estado) {
        if (DVML.ESTADO_VALIDO.equalsIgnoreCase(estado)) {
            resumo.validadas++;
        } else if (DVML.ESTADO_INVALIDO.equalsIgnoreCase(estado)) {
            resumo.invalidadas++;
        }
    }

    private static void notificar(Ouvinte o, int feitos, int total, String msg) {
        if (o != null) {
            o.progresso(feitos, total, msg);
        }
    }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String textoEstado(String estado) {
        if (DVML.ESTADO_VALIDO.equalsIgnoreCase(estado)) {
            return "Validado";
        }
        if (DVML.ESTADO_INVALIDO.equalsIgnoreCase(estado)) {
            return "Inválido";
        }
        return "Pendente";
    }
}
