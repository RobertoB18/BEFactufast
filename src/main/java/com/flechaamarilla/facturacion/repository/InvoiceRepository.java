package com.flechaamarilla.facturacion.repository;

import com.flechaamarilla.facturacion.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Optional<Invoice> findByFolio(Integer invoiceId);

    @Query("SELECT i FROM Invoice i WHERE i.id_servicio = ?1")
    Optional<Invoice> getInvoiceByServiceId(Integer serviceId);
}
