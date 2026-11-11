package com.example.taller_reparacion.repository;

import com.example.taller_reparacion.entity.Averia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AveriaRepository extends JpaRepository<Averia, Long> {

    List<Averia> findByVehiculo_Id(Long vehiculoId);

}
