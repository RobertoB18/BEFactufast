package com.flechaamarilla.facturacion.controller;

import com.flechaamarilla.facturacion.model.Invoice;
import com.flechaamarilla.facturacion.model.Services;
import com.flechaamarilla.facturacion.pojo.Response;
import com.flechaamarilla.facturacion.service.InvoiceService;
import com.flechaamarilla.facturacion.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class ServiceController extends BaseController{
    @Autowired
    ServicioService servicioService;

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/buscarFolio/{folio}")
    public ResponseEntity<Response> buscarPorFolio(@PathVariable("folio") Integer folio) {
        try{

           Optional<Invoice> invoice = invoiceService.getInvoiceByFolio(folio);
           if (invoice.isPresent()) {
                return new ResponseEntity<Response>(new Response(true, "Factura " , invoice.get()), HttpStatus.OK);
           }

            Optional<Services> service = servicioService.buscarPorFolio(folio);

            if (service.isPresent()) {
                Optional<Invoice> invoiceWithService = invoiceService.getInvoiceByServiceId(service.get().getId_servicio());

                if (invoiceWithService.isPresent() ){
                    return new ResponseEntity<Response>(new Response(true, "Factura " , invoiceWithService.get()), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Response>(new Response(true, "Servicio " , service.get()), HttpStatus.OK);
                }
            }

            return new ResponseEntity<Response>(new Response(true, "Sin factura " , null), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<Response>(new Response(false, e.getMessage() , null), HttpStatus.OK);
        }
    }
}
