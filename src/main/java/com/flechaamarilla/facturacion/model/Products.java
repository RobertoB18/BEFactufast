package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "productos")
public class Products {

    @Id
    private Integer id_producto;

    private String nombre;

    private String descripcion;

    private Integer precioUnitario;

    @Column(name = "code_sat")
    private Integer codeSat;

    private String unidad;


}
