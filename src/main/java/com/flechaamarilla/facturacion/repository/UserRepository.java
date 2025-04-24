package com.flechaamarilla.facturacion.repository;

import com.flechaamarilla.facturacion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
