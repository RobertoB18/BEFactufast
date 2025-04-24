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
public class InvoiceBody {
    InvoiceBodyCustomer customer;
    ArrayList<InvoiceBodyItem> items = new ArrayList<InvoiceBodyItem>();
    private String use;
    private String payment_form;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InvoiceBodyCustomer {
        private String legal_name;
        private String email;
        private String tax_id;
        private String tax_system;
        InvoiceBodyAddress address;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class InvoiceBodyAddress {
            private String zip;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InvoiceBodyItem {
        private float quantity;
        InvoiceBodyProduct product;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class InvoiceBodyProduct {
            private String description;
            private String product_key;
            private float price;
        }
    }
}
