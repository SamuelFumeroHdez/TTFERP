package com.ttf.tallertornofumeroerp.repository;

import com.ttf.tallertornofumeroerp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, String> {
}
