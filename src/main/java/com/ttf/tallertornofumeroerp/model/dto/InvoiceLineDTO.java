package com.ttf.tallertornofumeroerp.model.dto;

import com.ttf.tallertornofumeroerp.model.InvoiceLine;

import java.text.DecimalFormat;

public class InvoiceLineDTO {

    private String invoiceLineNumber;
    private String description;
    private String total;

    public InvoiceLineDTO convertInvoiceLinetoDTO(InvoiceLine invoiceLine){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        this.invoiceLineNumber = invoiceLine.getInvoiceLineNumber();
        this.description = invoiceLine.getDescription();
        this.total = decimalFormat.format(invoiceLine.getTotal()) + " €";

        return this;
    }

    public String getInvoiceLineNumber() {
        return invoiceLineNumber;
    }

    public void setInvoiceLineNumber(String invoiceLineNumber) {
        this.invoiceLineNumber = invoiceLineNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
