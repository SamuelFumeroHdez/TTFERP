package com.ttf.tallertornofumeroerp.repository;

import com.ttf.tallertornofumeroerp.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceRepository extends JpaRepository<Invoice, String> {

}
