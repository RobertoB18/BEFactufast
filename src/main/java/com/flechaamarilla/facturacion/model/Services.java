package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "servicios")
public class Services {
    @Id
    private Integer id_servicio;

    private LocalDateTime fecha_hora;

    private Integer id_tiposervicio;

    private Boolean status;

    private String nombre;

    private String formapago;
}
