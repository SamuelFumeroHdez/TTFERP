package com.ttf.tallertornofumeroerp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TTFINVOICE_LINE")
public class InvoiceLine {

    @Id
    @Column(name = "INVOICE_LINE_NUMBER")
    private String invoiceLineNumber;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TOTAL")
    private Double total;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Transient
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "INVOICE_NUMBER", referencedColumnName = "INVOICE_NUMBER")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "items"})
    @JsonIgnore
    private Invoice invoice;

}
