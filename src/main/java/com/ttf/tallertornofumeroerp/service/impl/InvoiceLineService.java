package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoiceline.ExistingInvoiceLineException;
import com.ttf.tallertornofumeroerp.exception.invoiceline.InvoiceLineNotFoundException;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import com.ttf.tallertornofumeroerp.repository.IInvoiceLineRepository;
import com.ttf.tallertornofumeroerp.repository.IInvoiceRepository;
import com.ttf.tallertornofumeroerp.service.IInvoiceLineService;
import com.ttf.tallertornofumeroerp.service.IInvoiceService;
import com.ttf.tallertornofumeroerp.utils.DecimalFormater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceLineService implements IInvoiceLineService {

    @Autowired
    IInvoiceLineRepository invoiceLineRepository;

    @Autowired
    IInvoiceService invoiceService;

    @Override
    public List<InvoiceLine> retrieveAllInvoiceLines() {
        return invoiceLineRepository.findAll();
    }

    @Override
    public InvoiceLine retrieveInvoiceLine(String invoiceLineNumber) {
        return invoiceLineRepository.findById(invoiceLineNumber).orElse(null);
    }

    @Override
    public InvoiceLine createInvoiceLine(InvoiceLine invoiceLine) {
        InvoiceLine invoiceLineDB = retrieveInvoiceLine(invoiceLine.getInvoiceLineNumber());
        if(invoiceLineDB != null){
            throw new ExistingInvoiceLineException("This invoice line already exists");
        }
        //Actualizamos total de la factura
        Invoice invoice = invoiceService.retrieveInvoice(invoiceLine.getInvoiceNumber());
        if(invoice==null){
            throw new InvoiceNotFoundException("The invoice don't exist");
        }
        invoice.setSubtotal(DecimalFormater.numberFormatter(invoice.getSubtotal()+invoiceLine.getTotal()));
        invoice.calculateTotal();
        invoiceService.updateInvoice(invoiceLine.getInvoiceNumber(), invoice);

        invoiceLine.setCreationDate(new Date());
        invoiceLine.setInvoice(invoice);
        invoiceLine.setInvoiceNumber(invoice.getInvoiceNumber());
        return invoiceLineRepository.save(invoiceLine);
    }

    @Override
    public InvoiceLine updateInvoiceLine(String invoiceLineNumber, InvoiceLine invoiceLine) {
        InvoiceLine invoiceLineDB = retrieveInvoiceLine(invoiceLineNumber);
        if(invoiceLineDB == null){
            throw new InvoiceLineNotFoundException("Can't update a non-existent invoice line");
        }

        Invoice invoice = invoiceService.retrieveInvoice(invoiceLine.getInvoiceNumber());
        if(invoice == null){
            throw new InvoiceNotFoundException("Can't find the invoice");
        }

        invoice.setSubtotal(DecimalFormater.numberFormatter(invoice.getSubtotal()-invoiceLineDB.getTotal()+invoiceLine.getTotal()));
        invoice.calculateTotal();
        invoiceService.updateInvoice(invoiceLine.getInvoiceNumber(), invoice);

        invoiceLineDB.setInvoice(invoice);
        invoiceLineDB.setDescription(invoiceLine.getDescription());
        invoiceLineDB.setTotal(invoiceLine.getTotal());
        invoiceLineDB.setUpdateDate(new Date());

        return invoiceLineRepository.save(invoiceLineDB);

    }

    @Override
    public void deleteInvoiceLine(String invoiceLineNumber) {
        InvoiceLine invoiceLineDB = retrieveInvoiceLine(invoiceLineNumber);
        if(invoiceLineDB == null){
            throw new InvoiceLineNotFoundException("Can't delete a non-existent invoice line");
        }

        Invoice invoice = invoiceService.retrieveInvoice(invoiceLineDB.getInvoice().getInvoiceNumber());
        if(invoice==null){
            throw new InvoiceNotFoundException("The invoice don't exist");
        }
        invoice.setSubtotal(invoice.getSubtotal()-invoiceLineDB.getTotal());
        invoice.calculateTotal();
        invoiceService.updateInvoice(invoiceLineDB.getInvoice().getInvoiceNumber(), invoice);

        invoiceLineRepository.delete(invoiceLineDB);
    }
}
