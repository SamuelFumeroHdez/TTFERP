package com.ttf.tallertornofumeroerp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "TTFCUSTOMER_EMAIL")
public class Email {

    @Id
    @Column(name = "EMAIL")
    private String email;

    @Transient
    private String customerNif;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "CUSTOMER_NIF", referencedColumnName = "NIF")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "items"})
    @JsonIgnore
    private Customer customer;
}
