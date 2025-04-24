package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usuario")
public class User {

    @Id
    private String rfc;

    @Column(name = "codigo_postal", nullable = false)
    private Integer zip;

    @Column(name = "regimen_fiscal", nullable = false, columnDefinition = "varchar(255)")
    private String taxregime;

    @Column(name = "razon_social", nullable = false, columnDefinition = "varchar(255)")
    private String companyName;

    private String email;
}
