package com.flechaamarilla.facturacion.controller;

import com.flechaamarilla.facturacion.model.Invoice;
import com.flechaamarilla.facturacion.model.Services;
import com.flechaamarilla.facturacion.model.User;
import com.flechaamarilla.facturacion.pojo.PostInvoiceApiResponse;
import com.flechaamarilla.facturacion.pojo.Response;
import com.flechaamarilla.facturacion.pojo.SendEmailResponse;
import com.flechaamarilla.facturacion.service.FacturaApiService;
import com.flechaamarilla.facturacion.service.InvoiceService;
import com.flechaamarilla.facturacion.service.ServicioService;
import com.flechaamarilla.facturacion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class FacturaApiController extends BaseController {
    @Autowired
    private FacturaApiService facturaApiService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServicioService servicioService;

    @PostMapping("/facturapi")
    public ResponseEntity<Response> getUsuario(@RequestParam("rfc") String rfc, @RequestParam("cfdi") String cfdi, @RequestParam("folio") Integer folio){
        try {
            Optional<User> userDb = userService.getUser(rfc);

            if (!userDb.isPresent()) {
                return new ResponseEntity<Response>(new Response(false, "Error usuario no encontrado " , null), HttpStatus.OK);
            }

            Optional<Services> serviceDb = servicioService.buscarPorFolio(folio);
            if (!serviceDb.isPresent()) {
                return new ResponseEntity<Response>(new Response(false, "Error servicio no encontrado " , null), HttpStatus.OK);
            }

            PostInvoiceApiResponse postInvoiceApiResponse = facturaApiService.createInvoice(userDb.get(), cfdi, serviceDb.get(), folio);

            System.out.println(postInvoiceApiResponse.getFolio_number());
            if(postInvoiceApiResponse == null){
                return new ResponseEntity<Response>(new Response(false, "Error al hacer la factura" , null), HttpStatus.OK);
            }

            Services serviceUpdated = serviceDb.get();
            serviceUpdated.setStatus(true);
            servicioService.save(serviceUpdated);

            Invoice invoiceToSave = new Invoice();
            invoiceToSave.setId_factura(postInvoiceApiResponse.getId());
            invoiceToSave.setFolio(postInvoiceApiResponse.getFolio_number());
            invoiceToSave.setRfc_usuario(rfc);
            invoiceToSave.setId_servicio(folio);

            Invoice invoice = invoiceService.saveInvoice(invoiceToSave);

            return new ResponseEntity<Response>(new Response(true, "Success", invoice), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(false, "Error " + e.getMessage(), null), HttpStatus.OK);
        }
    }

    @PostMapping("/facturapi/email")
    public ResponseEntity<Response> sendMail(@RequestParam("email") String email, @RequestParam("invoice_id") String invoiceId) {
        try {
            Optional<Invoice> invoiceDb = this.invoiceService.getInvoice(invoiceId);
            if (!invoiceDb.isPresent()) {
                return new ResponseEntity<Response>(new Response(false, "Error factura no encontrada" , null), HttpStatus.OK);
            }

            SendEmailResponse sendEmailResponse = this.facturaApiService.sendMail(email, invoiceId);
            return new ResponseEntity<Response>(new Response(true, "Success", sendEmailResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(false, "Error " + e.getMessage(), null), HttpStatus.OK);
        }
    }

    @GetMapping("/facturapi/download/{invoice_id}/{format}")
    public ResponseEntity<?> downloadInvoiceFile(@PathVariable("invoice_id") String invoiceId,
                                                 @PathVariable("format") String format) {
        try {
            byte[] fileBytes = facturaApiService.getInvoiceFile(invoiceId, format);

            return new ResponseEntity<Response>(new Response(true, "Success", fileBytes), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(false, "Error interno del servidor al procesar la descarga.", null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/facturapi/cancel/{invoice_id}")
    public ResponseEntity<Response> cancelInvoice(@PathVariable("invoice_id") String invoiceId) {
            PostInvoiceApiResponse cancelApiResponse = facturaApiService.cancelInvoice(invoiceId);

            if (cancelApiResponse == null) {
                return new ResponseEntity<Response>(new Response(false, "La cancelación falló en el servicio externo. Verifique el ID o si la factura es cancelable.", null), HttpStatus.OK);
            }


            Optional<Invoice> invoiceOpt = invoiceService.getInvoice(invoiceId);
            if (!invoiceOpt.isPresent()) {
                return new ResponseEntity<Response>(new Response(true, "Factura cancelada en API, pero no se encontró registro local.", null), HttpStatus.OK);
            }

            Invoice invoiceToDelete = invoiceOpt.get();
            Integer servicioFolio = invoiceToDelete.getId_servicio();

            if (servicioFolio != null) {
                Optional<Services> serviceOpt = servicioService.buscarPorFolio(servicioFolio);
                if (serviceOpt.isPresent()) {
                    Services serviceToUpdate = serviceOpt.get();
                    if (serviceToUpdate.getStatus() != null && serviceToUpdate.getStatus()) {
                        serviceToUpdate.setStatus(false);
                        servicioService.save(serviceToUpdate);
                    }
                }
            }

            try {
                invoiceService.deleteInvoiceByIdFactura(invoiceId);
            }catch (Exception e) {
                return new ResponseEntity<Response>(new Response(true, "Error" +  e.getMessage(), null), HttpStatus.OK);
            }

            return new ResponseEntity<>(new Response(true, "Factura cancelada exitosamente en API y registros locales actualizados.", cancelApiResponse), HttpStatus.OK);

    }
}
