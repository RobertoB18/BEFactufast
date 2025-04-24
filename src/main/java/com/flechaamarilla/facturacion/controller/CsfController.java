package com.flechaamarilla.facturacion.controller;

import com.flechaamarilla.facturacion.pojo.Response;
import com.flechaamarilla.facturacion.service.LectorPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
public class CsfController extends BaseController {

    @Autowired
    private LectorPdfService lectorPdfService;

    @PostMapping("/csf/upload")
    public ResponseEntity<?> subirConstancia(@RequestParam("archivo") MultipartFile archivo) {

        if (archivo.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Por favor, selecciona un archivo para subir." , null), HttpStatus.OK);
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return new ResponseEntity<Response>(new Response(false, "El archivo debe ser de tipo PDF." , null), HttpStatus.OK);
        }

        try {
            String textoExtraido = lectorPdfService.extraerTextoDePdf(archivo);

            if (textoExtraido != null && !textoExtraido.isBlank()) {
                Map<String, Object> datosExtraidos = parsearDatosCsf(textoExtraido);

                if (datosExtraidos.isEmpty() || !datosExtraidos.containsKey("rfc") || !datosExtraidos.containsKey("zip")) {
                    return new ResponseEntity<Response>(new Response(false, "No se pudieron extraer los datos fiscales clave (RFC, CP) del documento. Verifique el archivo." , null), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Response>(new Response(true, "Success" , datosExtraidos), HttpStatus.OK);
                }

            } else {
                return new ResponseEntity<Response>(new Response(false, "No se pudo extraer el texto del archivo PDF." , null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<Response>(new Response(false, "Ocurrio un error al extraer el PDF." , null), HttpStatus.OK);
        }
    }


    private Map<String, Object> parsearDatosCsf(String textoCompleto) {
        Map<String, Object> datos = new HashMap<>();
        String textoNormalizado = textoCompleto.replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n")
                .replaceAll("\\s{2,}", " ");

        // 1. RFC
        Pattern rfcPattern = Pattern.compile("RFC:\\s*([A-Z&Ñ]{3,4}\\d{6}[A-Z\\d]{3})", Pattern.CASE_INSENSITIVE);
        Matcher rfcMatcher = rfcPattern.matcher(textoNormalizado);
        if (rfcMatcher.find()) {
            datos.put("rfc", rfcMatcher.group(1).trim());
        }

        // 2. Razón Social / Nombre Completo
        Pattern razonSocialPattern = Pattern.compile("(?:Denominación/Razón Social:)\\s*([^\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher razonSocialMatcher = razonSocialPattern.matcher(textoNormalizado);
        if (razonSocialMatcher.find()) {
            datos.put("companyName", razonSocialMatcher.group(1).trim());
        } else {
            Pattern nombreCompletoPattern = Pattern.compile(
                    "Nombre \\(s\\):\\s*([^\n]+)\n.*Primer Apellido:\\s*([^\n]+)\n.*Segundo Apellido:\\s*([^\n]+)",
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL
            );
            Matcher nombreCompletoMatcher = nombreCompletoPattern.matcher(textoNormalizado);
            if (nombreCompletoMatcher.find()) {
                String nombre = nombreCompletoMatcher.group(1).trim();
                String apPaterno = nombreCompletoMatcher.group(2).trim();
                String apMaterno = nombreCompletoMatcher.group(3).trim();
                datos.put("companyname", nombre + " " + apPaterno + " " + apMaterno);
            }
        }

        // Código Postal
        Pattern cpPattern = Pattern.compile("Código Postal:\\s*(\\d{5})", Pattern.CASE_INSENSITIVE);
        Matcher cpMatcher = cpPattern.matcher(textoNormalizado);
        if (cpMatcher.find()) {
            datos.put("zip", cpMatcher.group(1).trim());
        }

        return datos;
    }
}