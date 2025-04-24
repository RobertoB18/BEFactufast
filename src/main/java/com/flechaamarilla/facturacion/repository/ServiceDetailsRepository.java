package com.flechaamarilla.facturacion.repository;

import com.flechaamarilla.facturacion.model.ServiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDetailsRepository extends JpaRepository<ServiceDetails, Integer> {
    List<ServiceDetails> findByIdServicio(int idServicio);
}

