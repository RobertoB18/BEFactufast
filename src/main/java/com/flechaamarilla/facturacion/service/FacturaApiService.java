package com.flechaamarilla.facturacion.service;

import com.flechaamarilla.facturacion.config.GeneralConfig;
import com.flechaamarilla.facturacion.model.Products;
import com.flechaamarilla.facturacion.model.ServiceDetails;
import com.flechaamarilla.facturacion.model.Services;
import com.flechaamarilla.facturacion.model.User;
import com.flechaamarilla.facturacion.pojo.InvoiceBody;
import com.flechaamarilla.facturacion.pojo.PostInvoiceApiResponse;
import com.flechaamarilla.facturacion.pojo.PostMailBody;
import com.flechaamarilla.facturacion.pojo.SendEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FacturaApiService {
    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private ServiceDetailsService serviceDetailsService;

    private static final String FORMAT_XML = "xml";
    private static final String FORMAT_PDF = "pdf";
    private static final String FORMAT_ZIP = "zip";
    private static final List<String> ALLOWED_FORMATS = Arrays.asList(FORMAT_XML, FORMAT_PDF, FORMAT_ZIP);
    private static final MediaType APPLICATION_ZIP = new MediaType("application", "zip");

    public PostInvoiceApiResponse createInvoice(User userDb, String cfdi, Services serviceDb, Integer folio) {
        String endpoint = generalConfig.getFacturapi() + "/invoices";

        InvoiceBody.InvoiceBodyCustomer invoiceBodyCustomer = new InvoiceBody.InvoiceBodyCustomer();
        invoiceBodyCustomer.setLegal_name(userDb.getCompanyname());
        invoiceBodyCustomer.setTax_system("601");
        invoiceBodyCustomer.setTax_id(userDb.getRfc());

        InvoiceBody.InvoiceBodyCustomer.InvoiceBodyAddress invoiceBodyAddress = new InvoiceBody.InvoiceBodyCustomer.InvoiceBodyAddress();
        invoiceBodyAddress.setZip(userDb.getZip().toString());

        invoiceBodyCustomer.setAddress(invoiceBodyAddress);

        ArrayList<InvoiceBody.InvoiceBodyItem> invoiceItems = new ArrayList<>();
        List<ServiceDetails> detailsServices = this.serviceDetailsService.buscarPorIdServicio(folio);

        if (detailsServices != null) {
            for (ServiceDetails detail : detailsServices) {
                if (detail != null && detail.getProducts() != null) {
                    Products product = detail.getProducts();

                    InvoiceBody.InvoiceBodyItem.InvoiceBodyProduct productInfo = new InvoiceBody.InvoiceBodyItem.InvoiceBodyProduct();

                    productInfo.setDescription(Objects.toString(product.getDescripcion(), ""));

                    productInfo.setProduct_key(Objects.toString(product.getCodeSat(), ""));

                    productInfo.setPrice(product.getPrecioUnitario() != null ? product.getPrecioUnitario().floatValue() : 0.0f);

                    InvoiceBody.InvoiceBodyItem invoiceItem = new InvoiceBody.InvoiceBodyItem();

                    invoiceItem.setQuantity(detail.getCantidad() != null ? detail.getCantidad().floatValue() : 0.0f);

                    invoiceItem.setProduct(productInfo);

                    invoiceItems.add(invoiceItem);
                }
            }
        }

        InvoiceBody invoiceBody = new InvoiceBody();
        invoiceBody.setCustomer(invoiceBodyCustomer);
        invoiceBody.setItems(invoiceItems);
        invoiceBody.setPayment_form(serviceDb.getFormapago());
        invoiceBody.setUse(cfdi);

        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(generalConfig.getFacturapiToken());

            HttpEntity<InvoiceBody> requestEntity = new HttpEntity<>(invoiceBody, headers);

            ResponseEntity<PostInvoiceApiResponse> responseEntity = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    requestEntity,
                    PostInvoiceApiResponse.class
            );

            return responseEntity.getBody();
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public SendEmailResponse sendMail(String email, String invoiceId) {
        String endpoint = generalConfig.getFacturapi() + "/invoices/" + invoiceId + "/email";

        PostMailBody postMailBody = new PostMailBody();
        postMailBody.setEmail(email);

        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(generalConfig.getFacturapiToken());

            HttpEntity<PostMailBody> requestEntity = new HttpEntity<>(postMailBody, headers);

            ResponseEntity<SendEmailResponse> responseEntity = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    requestEntity,
                    SendEmailResponse.class
            );

            return responseEntity.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public byte[] getInvoiceFile(String invoiceId, String format) {
        if (invoiceId == null || invoiceId.trim().isEmpty()) {
            return null;
        }
        if (format == null || !ALLOWED_FORMATS.contains(format.toLowerCase().trim())) {
            return null;
        }

        String cleanFormat = format.toLowerCase().trim();
        String endpoint = generalConfig.getFacturapi() + "/invoices/" + invoiceId + "/" + cleanFormat;

        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            switch (cleanFormat) {
                case FORMAT_PDF:
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
                    break;
                case FORMAT_XML:
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
                    break;
                case FORMAT_ZIP:
                    headers.setAccept(Collections.singletonList(APPLICATION_ZIP));
                    break;
                default:
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
            }

            headers.setBearerAuth(generalConfig.getFacturapiToken());

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody();
            } else {
                return null;
            }

        }  catch (RestClientException e) {
            return null;
        }
    }

    public PostInvoiceApiResponse cancelInvoice(String invoiceId) {
        if (invoiceId == null || invoiceId.trim().isEmpty()) {
            return null;
        }

        String endpoint = generalConfig.getFacturapi() + "/invoices/" + invoiceId;

        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(generalConfig.getFacturapiToken());

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<PostInvoiceApiResponse> responseEntity = restTemplate.exchange(
                    endpoint,
                    HttpMethod.DELETE,
                    requestEntity,
                    PostInvoiceApiResponse.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                PostInvoiceApiResponse canceledInvoice = responseEntity.getBody();
                return canceledInvoice;
            } else {
                return null;
            }

        } catch (RestClientException e) {
            return null;
        }
    }
}
