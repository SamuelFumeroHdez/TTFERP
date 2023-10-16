package com.ttf.tallertornofumeroerp.model;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.List;

public class CustomDataSource implements JRDataSource {
    private List<InvoiceLine> lines;
    private int index = -1;

    public CustomDataSource(List<InvoiceLine> lines) {
        this.lines = lines;
    }

    @Override
    public boolean next() throws JRException {
        index++;
        return index < lines.size();
    }



    @Override
    public Object getFieldValue(JRField field) throws JRException {
        Object value = null;
        String fieldName = field.getName();

        if ("invoiceLineNumber".equals(fieldName) && index < lines.size()) {
            value = lines.get(index).getInvoiceLineNumber();
        } else if ("description".equals(fieldName) && index < lines.size()) {
            value = lines.get(index).getDescription();
        } else if ("total".equals(fieldName) && index < lines.size()) {
            value = lines.get(index).getTotal();
        }

        return value;
    }
}
