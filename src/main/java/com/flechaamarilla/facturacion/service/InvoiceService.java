package com.flechaamarilla.facturacion.service;

import com.flechaamarilla.facturacion.model.Invoice;
import com.flechaamarilla.facturacion.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Optional<Invoice> getInvoiceByFolio(Integer folio) {
        return invoiceRepository.findByFolio(folio);
    }

    public Optional<Invoice> getInvoice(String invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    public Optional<Invoice> getInvoiceByServiceId(Integer serviceId) {
        return invoiceRepository.getInvoiceByServiceId(serviceId);
    }

    public void deleteInvoiceByIdFactura(String invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }
}
