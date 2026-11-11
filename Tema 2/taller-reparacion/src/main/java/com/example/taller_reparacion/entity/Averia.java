package com.example.taller_reparacion.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Averia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoReparacion estado = EstadoReparacion.PENDIENTE;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaUltimaActualizacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonIgnoreProperties("averias")
    private Vehiculo vehiculo;

    @PrePersist
    public void antesDeGuardar() {
        LocalDateTime ahora = LocalDateTime.now();

        fechaCreacion = ahora;
        fechaUltimaActualizacion = ahora;

        if (estado == null) {
            estado = EstadoReparacion.PENDIENTE;
        }
    }

    @PreUpdate
    public void antesDeActualizar() {
        fechaUltimaActualizacion = LocalDateTime.now();
    }
}
