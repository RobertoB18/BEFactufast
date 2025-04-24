package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "usuario")
public class User {

    @Id
    private String rfc;

    @Column(name = "codigo_postal")
    private Integer zip;

    @Column(name = "regimen_fiscal", columnDefinition = "varchar(255)")
    private String taxregime;

    @Column(name = "razon_social", columnDefinition = "varchar(255)")
    private String companyname;

    private String email;
}
