package com.ttf.tallertornofumeroerp.service;

import com.ttf.tallertornofumeroerp.model.Invoice;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.List;

public interface IInvoiceService {
    List<Invoice> retrieveAllInvoices();
    Invoice retrieveInvoice(String invoiceNumber);
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(String invoiceNumber, Invoice invoice);
    void deleteInvoice(String invoiceNumber);
    void generateReport(String invoiceNumber, HttpServletResponse response) throws ParseException;
}
