package com.flechaamarilla.facturacion.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "detallesservicio")
public class ServiceDetails {
    @Id
    private Integer idDetalleservicio;

    @Column(name = "id_servicio", insertable = false, updatable = false)
    private Integer idServicio;
    @Column(name = "id_productoalimento", insertable = false, updatable = false)
    private Integer idProductoAlimento;
    private Integer cantidad;
    private Integer Total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio")
    private Services servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_productoalimento")
    private Products products;


}
