
package com.example.taller_reparacion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricula;

    private String modelo;

    private LocalDate fechaMatriculacion;

    @OneToMany(mappedBy = "vehiculo",
            cascade = CascadeType.ALL,
            orphanRemoval = true)

    private List<Averia> averias = new ArrayList<>();
}
