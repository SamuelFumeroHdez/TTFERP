package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;

import java.util.List;

public interface IInvoiceLineService {

    List<InvoiceLine> retrieveAllInvoiceLines();
    InvoiceLine retrieveInvoiceLine(String invoiceLineNumber);
    InvoiceLine createInvoiceLine(InvoiceLine invoiceLine);
    InvoiceLine updateInvoiceLine(String invoiceLineNumber, InvoiceLine invoiceLine);
    void deleteInvoiceLine(String invoiceLineNumber);
}
