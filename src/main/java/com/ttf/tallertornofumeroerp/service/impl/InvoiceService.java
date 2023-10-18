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
import com.ttf.tallertornofumeroerp.utils.EmailSender;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class InvoiceService implements IInvoiceService {

    @Autowired
    IInvoiceRepository invoiceRepository;

    @Autowired
    IInvoiceLineRepository invoiceLineRepository;

    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    EmailSender emailSender;

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

        List<InvoiceLine> lines = invoice.getInvoiceLines();
        List<InvoiceLineDTO> linesDTO = new ArrayList<>();

        InvoiceLineDTO invoiceLineDTO = null;
        linesDTO.add(new InvoiceLineDTO()); //Esto se hace porque jasper no coge el primer elemento del Array
        for(InvoiceLine line : lines){
            invoiceLineDTO = new InvoiceLineDTO();
            linesDTO.add(invoiceLineDTO.convertInvoiceLinetoDTO(line));
        }

        LocalDate currentDate = LocalDate.now();
        String day = String.valueOf(currentDate.getDayOfMonth());
        String month = String.valueOf(currentDate.getMonthValue());
        String year = String.valueOf(currentDate.getYear());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(year + "-" + month + "-" + day);
        String fileName = "Invoice_" + invoiceNumber + ".pdf";

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

            createPathToSaveInvoice(year, month, customer.getCustomerName());

            String path = System.getProperty("user.home") + "/Desktop/InformesFacturas/" + customer.getCustomerName() +
                    "/" + year + "/" + month + "/" + fileName;

            JasperExportManager.exportReportToPdfFile(jasperPrint, path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendInvoiceEmail(String invoiceNumber) throws IOException {

        Invoice invoice = retrieveInvoice(invoiceNumber);
        Customer customer = customerRepository.findById(invoice.getCustomerNif()).orElse(null);
        String fileName = "Invoice_" + invoiceNumber + ".pdf";

        if(customer==null){
            throw new CustomerNotFoundException("The customer doesn't exists");
        }

        String emailTemplate = getEmailTemplate();
        emailTemplate = emailTemplate.replace("{{INVOICE_NUMBER}}", invoiceNumber);

        String subject = "Factura " + invoiceNumber + " - " + customer.getCustomerName();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(invoice.getUpdateDate());

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);

        String path = System.getProperty("user.home") + "/Desktop/InformesFacturas/" + customer.getCustomerName() +
                "/" + year + "/" + month + "/" + fileName;

        emailSender.sendEmail(customer.getEmails(), subject, emailTemplate, path);
    }

    private String getFormattedPrice(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedPrice = String.valueOf(decimalFormat.format(number)) + " €";
        return formattedPrice;
    }

    private void createPathToSaveInvoice(String year, String month, String customerName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home") + "/Desktop/InformesFacturas/" + customerName + "/" + year + "/" +
                month);

        if(!Files.exists(path)){
            Files.createDirectories(path);
            System.out.println("Directory created succesfully");
        }else{
            System.out.println("This directory already exists");
        }

    }

    private String getEmailTemplate() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("templates/emailTemplate.html");
        if (inputStream != null) {
            try {
                // Lee el contenido del archivo y conviértelo en una cadena
                return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                inputStream.close();
            }
        } else {
            throw new IOException("No se pudo encontrar el archivo HTML en la ubicación especificada.");
        }
        return null;
    }


}
