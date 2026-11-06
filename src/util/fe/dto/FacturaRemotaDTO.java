package util.fe.dto;

/**
 * Uma factura devolvida pelo serviço listarFacturas da AGT
 * (documentResultList[i]).
 *
 * Os totais ficam como texto porque a AGT não documenta o formato de
 * documentTotals; são mostrados tal como chegam.
 */
public class FacturaRemotaDTO {

    private String documentNo;
    private String documentType;
    private String documentDate;
    private String systemEntryDate;
    private String documentStatus;      // N normal / C correcção de documento rejeitado
    private String rejectedDocumentNo;
    private String customerAcceptance;  // V confirmada / N por confirmar / R rejeitada
    private String supplierTaxRegistrationNumber;
    private String customerTaxID;       // NIF do cliente, se a AGT o devolver
    private String customerName;
    private String netTotal;
    private String taxPayable;
    private String grossTotal;
    private int numReferencias;
    private int numPagamentos;
    private String jsonBruto;           // item completo, para o detalhe
    private Integer codigoLocal;        // código da venda na BD local, ou null se não existir
    private String estadoLocal;         // estado da venda na BD local (P/V/I), se existir

    public String getEstadoLocal() {
        return estadoLocal;
    }

    public void setEstadoLocal(String estadoLocal) {
        this.estadoLocal = estadoLocal;
    }

    public Integer getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(Integer codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getSystemEntryDate() {
        return systemEntryDate;
    }

    public void setSystemEntryDate(String systemEntryDate) {
        this.systemEntryDate = systemEntryDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getRejectedDocumentNo() {
        return rejectedDocumentNo;
    }

    public void setRejectedDocumentNo(String rejectedDocumentNo) {
        this.rejectedDocumentNo = rejectedDocumentNo;
    }

    public String getCustomerAcceptance() {
        return customerAcceptance;
    }

    public void setCustomerAcceptance(String customerAcceptance) {
        this.customerAcceptance = customerAcceptance;
    }

    public String getSupplierTaxRegistrationNumber() {
        return supplierTaxRegistrationNumber;
    }

    public void setSupplierTaxRegistrationNumber(String supplierTaxRegistrationNumber) {
        this.supplierTaxRegistrationNumber = supplierTaxRegistrationNumber;
    }

    public String getCustomerTaxID() {
        return customerTaxID;
    }

    public void setCustomerTaxID(String customerTaxID) {
        this.customerTaxID = customerTaxID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getTaxPayable() {
        return taxPayable;
    }

    public void setTaxPayable(String taxPayable) {
        this.taxPayable = taxPayable;
    }

    public String getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(String grossTotal) {
        this.grossTotal = grossTotal;
    }

    public int getNumReferencias() {
        return numReferencias;
    }

    public void setNumReferencias(int numReferencias) {
        this.numReferencias = numReferencias;
    }

    public int getNumPagamentos() {
        return numPagamentos;
    }

    public void setNumPagamentos(int numPagamentos) {
        this.numPagamentos = numPagamentos;
    }

    public String getJsonBruto() {
        return jsonBruto;
    }

    public void setJsonBruto(String jsonBruto) {
        this.jsonBruto = jsonBruto;
    }
}
