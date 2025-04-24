package com.flechaamarilla.facturacion.service;


import com.flechaamarilla.facturacion.model.Services;
import com.flechaamarilla.facturacion.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    ServiceRepository serviceRepository;

    public Optional<Services> buscarPorFolio(Integer id) {
        return serviceRepository.findById(id);
    }


    public Services save(Services service) {
        return serviceRepository.save(service);
    }

}
