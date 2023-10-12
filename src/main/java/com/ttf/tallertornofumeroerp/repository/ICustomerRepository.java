package com.ttf.tallertornofumeroerp.repository;

import com.ttf.tallertornofumeroerp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, String> {
}
