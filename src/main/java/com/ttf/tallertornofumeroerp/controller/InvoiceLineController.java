package com.ttf.tallertornofumeroerp.controller;

import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoiceline.InvoiceLineNotFoundException;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import com.ttf.tallertornofumeroerp.service.impl.InvoiceLineService;
import com.ttf.tallertornofumeroerp.service.impl.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/invoiceLines")
public class InvoiceLineController {

    @Autowired
    private InvoiceLineService invoiceLineService;

    @GetMapping(
            value = "/",
            produces = "application/json")
    public ResponseEntity<List<InvoiceLine>> retrieveAllInvoices(){
        List<InvoiceLine> invoiceLineList = invoiceLineService.retrieveAllInvoiceLines();
        return invoiceLineList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(invoiceLineList);
    }

    @GetMapping(
            value = "/{invoiceLineNumber}",
            produces = "application/json")
    public ResponseEntity<InvoiceLine> retrieveInvoice(@PathVariable("invoiceLineNumber") String invoiceNumber){
        InvoiceLine invoiceLineFound = invoiceLineService.retrieveInvoiceLine(invoiceNumber);
        return invoiceLineFound == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(invoiceLineFound);
    }

    @PostMapping(
            value = "/create",
            produces = "application/json")
    public ResponseEntity<InvoiceLine> createInvoice(@RequestBody InvoiceLine invoiceLine){
        InvoiceLine invoiceLineCreated = invoiceLineService.createInvoiceLine(invoiceLine);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceLineCreated);
    }

    @PutMapping(value = "/{invoiceLineNumber}")
    public ResponseEntity<InvoiceLine> updateInvoice(@PathVariable("invoiceLineNumber") String invoiceLineNumber, @RequestBody InvoiceLine invoiceLine){
        InvoiceLine invoiceLineDB = invoiceLineService.updateInvoiceLine(invoiceLineNumber, invoiceLine);
        return invoiceLineDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(invoiceLineDB);
    }

    @DeleteMapping(value = "/{invoiceLineNumber}")
    public void deleteInvoice(@PathVariable("invoiceLineNumber") String invoiceLineNumber) throws InvoiceLineNotFoundException {
        invoiceLineService.deleteInvoiceLine(invoiceLineNumber);
    }
}
