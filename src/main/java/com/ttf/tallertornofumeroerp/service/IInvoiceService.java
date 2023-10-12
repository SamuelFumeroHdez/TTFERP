package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.Invoice;

import java.util.List;

public interface IInvoiceService {
    List<Invoice> retrieveAllInvoices();
    Invoice retrieveInvoice(String invoiceNumber);
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(String invoiceNumber, Invoice invoice);
    void deleteInvoice(String invoiceNumber);
}
