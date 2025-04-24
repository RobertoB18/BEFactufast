package com.flechaamarilla.facturacion.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostInvoiceApiResponse {
    private String id;
    private String created_at;
    private boolean livemode;
    private String status;
    private String cancellation_status;
    private String verification_url;
    private String date;
    Address AddressObject;
    private String type;
    Customer customer;
    private float total;
    private String uuid;
    private int folio_number;
    private String series;
    private String external_id;
    private String idempotency_key;
    private float payment_form;
    private boolean is_ready_to_stamp;
    ArrayList<Object> items = new ArrayList<Object>();
    ArrayList<Object> related_documents = new ArrayList<Object>();
    ArrayList<Object> received_payment_ids = new ArrayList<Object>();
    ArrayList<Object> target_invoice_ids = new ArrayList<Object>();
    private String currency;
    private float exchange;
    ArrayList<Object> complements = new ArrayList<Object>();
    private String pdf_custom_section;
    private String addenda;
    ArrayList<Object> namespaces = new ArrayList<Object>();
    Stamp stamp;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Stamp {
        private String signature;
        private String date;
        private String sat_cert_number;
        private String sat_signature;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Customer {
        private String id;
        private String legal_name;
        private String tax_id;
        Address adress;

        public class Address {
            private String country;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Address {
        private String street;
        private float exterior;
        private float interior;
        private String neighborhood;
        private String city;
        private String municipality;
        private float zip;
        private String state;
    }
}
