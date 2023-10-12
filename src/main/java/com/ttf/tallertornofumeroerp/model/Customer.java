package com.ttf.tallertornofumeroerp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TTFCUSTOMER")
public class Customer {

    @Id
    @Column(name = "NIF")
    private String nif;

    @Column(name = "LEGAL_NAME")
    private String legalName;

    @Column(name = "NAME")
    private String customerName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL})
    private List<Email> emails;
}
