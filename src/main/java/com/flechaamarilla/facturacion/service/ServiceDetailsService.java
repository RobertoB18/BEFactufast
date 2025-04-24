package com.flechaamarilla.facturacion.service;

import com.flechaamarilla.facturacion.model.ServiceDetails;
import com.flechaamarilla.facturacion.repository.ServiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceDetailsService {
    @Autowired
    private ServiceDetailsRepository repository;

    public List<ServiceDetails> buscarPorIdServicio(int idServicio) {
        return repository.findByIdServicio(idServicio);
    }
}
