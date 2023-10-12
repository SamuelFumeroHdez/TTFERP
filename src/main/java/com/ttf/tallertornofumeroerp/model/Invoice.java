package com.ttf.tallertornofumeroerp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TTFINVOICE")
public class Invoice {

    @Id
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "CUSTOMER_NIF")
    private String customerNif;

    @Column(name = "SUBTOTAL")
    private Double subtotal;
    @Column(name = "TAX_PERCENT")
    private Double taxPercent;
    @Column(name = "TOTAL_TAX")
    private Double totalTax;
    @Column(name = "TOTAL")
    private Double total;
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @OneToMany(mappedBy = "invoice", cascade = {CascadeType.ALL})
    private List<InvoiceLine> invoiceLines;

    public Invoice(){
        invoiceLines = new ArrayList<>();
    }

    public void calculateTotal(){
        this.setTotalTax(this.getSubtotal()*this.getTaxPercent());
        this.setTotal(this.getSubtotal()+this.getTotalTax());
    }



}
