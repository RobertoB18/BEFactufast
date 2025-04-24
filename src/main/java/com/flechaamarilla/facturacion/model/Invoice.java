package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "facturas")
public class Invoice {
    @Id
    private String id_factura;

    private Integer folio;

    private Integer id_servicio;

    private String rfc_usuario;
}
