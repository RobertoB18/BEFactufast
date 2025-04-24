package com.flechaamarilla.facturacion.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LectorPdfService {

    public String extraerTextoDePdf(MultipartFile archivoPdf) {
        if (archivoPdf == null || archivoPdf.isEmpty()) {
            return null;
        }

        byte[] pdfBytes;
        try (InputStream inputStream = archivoPdf.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            inputStream.transferTo(baos);
            pdfBytes = baos.toByteArray();
        } catch (IOException e) {
            return null;
        }

        try (PDDocument documento = Loader.loadPDF(pdfBytes)) {

            if (documento.isEncrypted()) {
                return null;
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(documento);

            return texto;

        } catch (Exception e) {
            return null;
        }
    }
}