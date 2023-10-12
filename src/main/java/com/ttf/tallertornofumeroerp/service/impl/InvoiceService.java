package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import com.ttf.tallertornofumeroerp.repository.ICustomerRepository;
import com.ttf.tallertornofumeroerp.repository.IInvoiceLineRepository;
import com.ttf.tallertornofumeroerp.repository.IInvoiceRepository;
import com.ttf.tallertornofumeroerp.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceService implements IInvoiceService {

    @Autowired
    IInvoiceRepository invoiceRepository;

    @Autowired
    IInvoiceLineRepository invoiceLineRepository;

    @Autowired
    ICustomerRepository customerRepository;

    @Override
    public List<Invoice> retrieveAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice retrieveInvoice(String invoiceNumber) {
        return invoiceRepository.findById(invoiceNumber).orElse(null);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findById(invoice.getInvoiceNumber()).orElse(null);
        if (invoiceDB !=null){
            return  invoiceDB;
        }

        Customer customerDB = customerRepository.findById(invoice.getCustomerNif()).orElse(null);
        if(customerDB==null){
            throw new CustomerNotFoundException("Customer not found");
        }
        List<InvoiceLine> lines = invoice.getInvoiceLines();
        for(InvoiceLine invoiceLine : lines){
            invoiceLine.setInvoice(invoice);
            invoiceLine.setCreationDate(new Date());
            invoiceLineRepository.save(invoiceLine);
        }

        invoice.setSubtotal(0.00);
        invoice.setTaxPercent(0.21);
        invoice.setTotalTax(0.00);
        invoice.setTotal(0.00);

        invoice.setCreationDate(new Date());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(String invoiceNumber, Invoice invoice) {
        Invoice invoiceDB = retrieveInvoice(invoiceNumber);
        if (invoiceDB == null){
            return  null;
        }
        invoiceDB.setCustomerNif(invoice.getCustomerNif());
        invoiceDB.setInvoiceNumber(invoiceNumber);
        invoiceDB.setSubtotal(invoice.getSubtotal());
        invoiceDB.setTaxPercent(invoice.getTaxPercent());
        invoiceDB.setTotalTax(invoice.getTotalTax());
        invoiceDB.setTotal(invoice.getTotal());
        invoiceDB.setUpdateDate(new Date());

        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public void deleteInvoice(String invoiceNumber) {
        Invoice invoice = retrieveInvoice(invoiceNumber);

        if(invoice == null){
            throw new InvoiceNotFoundException("Can't delete a non-existent invoice");
        }
        invoiceRepository.delete(invoice);
    }
}
