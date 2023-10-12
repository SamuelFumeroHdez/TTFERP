package com.ttf.tallertornofumeroerp.repository;

import com.ttf.tallertornofumeroerp.model.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceLineRepository extends JpaRepository<InvoiceLine, String> {
}
