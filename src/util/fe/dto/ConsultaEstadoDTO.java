/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.fe.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultado da consulta de estado (obterEstado) - sem lógica de UI.
 *
 * @author Engº Domingos Dala Vunge
 */
public class ConsultaEstadoDTO {

    private String codFact;
    private String requestID;
    private int resultCode = -1;
    private String documentStatus;   // V / I (devolvido pela AGT)
    private String estadoLocal;      // V / I ou null quando NÃO há nada a actualizar
    private boolean estadoAlterado;  // true se a BD local foi actualizada
    private boolean cancelado;       // resultCode 9
    private boolean podeTentarMaisTarde;
    private String errorCode;        // erros HTTP (E95, E96, E97, E98)
    private String mensagem;
    private List<String> erros = new ArrayList<>();
    private String respostaBruta;

    public String getCodFact() {
        return codFact;
    }

    public void setCodFact(String codFact) {
        this.codFact = codFact;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getEstadoLocal() {
        return estadoLocal;
    }

    public void setEstadoLocal(String estadoLocal) {
        this.estadoLocal = estadoLocal;
    }

    public boolean isEstadoAlterado() {
        return estadoAlterado;
    }

    public void setEstadoAlterado(boolean estadoAlterado) {
        this.estadoAlterado = estadoAlterado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isPodeTentarMaisTarde() {
        return podeTentarMaisTarde;
    }

    public void setPodeTentarMaisTarde(boolean podeTentarMaisTarde) {
        this.podeTentarMaisTarde = podeTentarMaisTarde;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<String> getErros() {
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }

    public String getRespostaBruta() {
        return respostaBruta;
    }

    public void setRespostaBruta(String respostaBruta) {
        this.respostaBruta = respostaBruta;
    }
}
