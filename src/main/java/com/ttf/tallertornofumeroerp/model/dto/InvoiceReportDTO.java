package com.ttf.tallertornofumeroerp.model.dto;

import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;

import java.util.List;

public class InvoiceReportDTO {

    private Double subtotal;
    private String taxDetail;
    private Double totalTax;
    private Double total;
    private List<InvoiceLineDTO> invoiceLines;

    public InvoiceReportDTO convertInvoiceReportDTO(Invoice invoice, List<InvoiceLineDTO> invoiceLines){
        this.subtotal = invoice.getSubtotal();
        this.taxDetail = invoice.getTaxDetail();
        this.totalTax = invoice.getTotalTax();
        this.total = invoice.getTotal();
        this.invoiceLines = invoiceLines;

        return this;
    }
}
