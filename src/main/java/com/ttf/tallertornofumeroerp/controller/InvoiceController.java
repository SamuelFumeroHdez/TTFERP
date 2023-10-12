package com.ttf.tallertornofumeroerp.controller;
import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.service.impl.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(
            value = "/",
            produces = "application/json")
    public ResponseEntity<List<Invoice>> retrieveAllInvoices(){
        List<Invoice> invoiceList = invoiceService.retrieveAllInvoices();
        return invoiceList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(invoiceList);
    }

    @GetMapping(
            value = "/{invoiceNumber}",
            produces = "application/json")
    public ResponseEntity<Invoice> retrieveInvoice(@PathVariable("invoiceNumber") String invoiceNumber){
        Invoice invoiceFound = invoiceService.retrieveInvoice(invoiceNumber);
        return invoiceFound == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(invoiceFound);
    }

    @PostMapping(
            value = "/create",
            produces = "application/json")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) throws CustomerNotFoundException {
        Invoice invoiceCreated = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceCreated);
    }

    @PutMapping(value = "/{invoiceNumber}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("invoiceNumber") String invoiceNumber, @RequestBody Invoice invoice){
        Invoice invoiceDB = invoiceService.updateInvoice(invoiceNumber, invoice);
        return invoiceDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(invoice);
    }

    @DeleteMapping(value = "/{invoiceNumber}")
    public void deleteInvoice(@PathVariable("invoiceNumber") String invoiceNumber) throws InvoiceNotFoundException {
        invoiceService.deleteInvoice(invoiceNumber);
    }
}
