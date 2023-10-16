package com.ttf.tallertornofumeroerp.service.impl;

import com.ttf.tallertornofumeroerp.exception.customer.CustomerNotFoundException;
import com.ttf.tallertornofumeroerp.exception.invoice.InvoiceNotFoundException;
import com.ttf.tallertornofumeroerp.model.CustomDataSource;
import com.ttf.tallertornofumeroerp.model.Customer;
import com.ttf.tallertornofumeroerp.model.Invoice;
import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import com.ttf.tallertornofumeroerp.model.dto.InvoiceLineDTO;
import com.ttf.tallertornofumeroerp.model.dto.InvoiceReportDTO;
import com.ttf.tallertornofumeroerp.repository.ICustomerRepository;
import com.ttf.tallertornofumeroerp.repository.IInvoiceLineRepository;
import com.ttf.tallertornofumeroerp.repository.IInvoiceRepository;
import com.ttf.tallertornofumeroerp.service.IInvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public void generateReport(String invoiceNumber, HttpServletResponse response) throws ParseException {
        Invoice invoice = retrieveInvoice(invoiceNumber);
        Customer customer = customerRepository.findById(invoice.getCustomerNif()).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Can't find the customer with NIF " + invoice.getCustomerNif());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Puedes personalizar el formato según tu necesidad
        Date date = dateFormat.parse("2023-10-15");

        List<InvoiceLine> lines = invoice.getInvoiceLines();
        List<InvoiceLineDTO> linesDTO = new ArrayList<>();

        InvoiceLineDTO invoiceLineDTO = null;
        for(InvoiceLine line : lines){
            invoiceLineDTO = new InvoiceLineDTO();
            linesDTO.add(invoiceLineDTO.convertInvoiceLinetoDTO(line));
        }



        try {
            // Cargar el informe Jasper
            File file = ResourceUtils.getFile("src/main/resources/InvoiceTTFERP.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanArrayDataSource dataSourceP = new JRBeanArrayDataSource(linesDTO.toArray());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("legalName", customer.getLegalName());
            parameters.put("nif", customer.getNif());
            parameters.put("address", customer.getAddress());
            parameters.put("invoiceNumber", invoice.getInvoiceNumber());
            parameters.put("subtotal", getFormattedPrice(invoice.getSubtotal()));
            parameters.put("taxDetail", invoice.getTaxDetail());
            parameters.put("totalTax", getFormattedPrice(invoice.getTotalTax()));
            parameters.put("total", getFormattedPrice(invoice.getTotal()));
            parameters.put("dateField", date);
            parameters.put("dsInvoice", dataSourceP);

            // Rellenar el informe con los datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourceP);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "invoice.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFormattedPrice(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPrice = String.valueOf(decimalFormat.format(number)) + " €";
        return formattedPrice;
    }
}
