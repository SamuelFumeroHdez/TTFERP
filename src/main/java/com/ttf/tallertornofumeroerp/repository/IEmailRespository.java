package com.ttf.tallertornofumeroerp.repository;

import com.ttf.tallertornofumeroerp.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailRespository extends JpaRepository<Email, String> {
}
