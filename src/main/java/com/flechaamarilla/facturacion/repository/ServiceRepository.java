package com.flechaamarilla.facturacion.repository;

import com.flechaamarilla.facturacion.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, Integer> {

}
