
package com.example.taller_reparacion.service;

import com.example.taller_reparacion.entity.Averia;
import com.example.taller_reparacion.entity.Vehiculo;
import com.example.taller_reparacion.repository.AveriaRepository;
import com.example.taller_reparacion.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AveriaService {

    private final AveriaRepository averiaRepository;
    private final VehiculoRepository vehiculoRepository;

    public AveriaService(AveriaRepository averiaRepository,
                         VehiculoRepository vehiculoRepository) {
        this.averiaRepository = averiaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    // Listar todas las averias
    public List<Averia> listarAverias() {
        return averiaRepository.findAll();
    }

    // Buscar una averia por su ID
    public Optional<Averia> buscarPorId(Long id) {
        return averiaRepository.findById(id);
    }

    // Buscar las averias de un vehiculo
    public Optional<List<Averia>> listarAveriasPorVehiculo(Long vehiculoId) {

        if (!vehiculoRepository.existsById(vehiculoId)) {
            return Optional.empty();
        }

        return Optional.of(
                averiaRepository.findByVehiculo_Id(vehiculoId)
        );
    }

    // Crear una averia asociada a un vehiculo
    public Averia crearAveria(Averia averia) {

        // Comprobar que se ha indicado un vehiculo
        if (averia.getVehiculo() == null ||
                averia.getVehiculo().getId() == null) {
            throw new IllegalArgumentException(
                    "La averia debe tener un vehiculo"
            );
        }

        // Buscar el vehiculo en la base de datos
        Vehiculo vehiculo = vehiculoRepository.findById(
                averia.getVehiculo().getId()
        ).orElseThrow(() ->
                new NoSuchElementException("Vehiculo no encontrado")
        );

        // Asignar el vehiculo real a la averia
        averia.setVehiculo(vehiculo);

        // Sincronizar la lista de averias del vehiculo
        vehiculo.getAverias().add(averia);

        // Guardar la averia
        return averiaRepository.save(averia);
    }

    // Modificar una averia
    public Optional<Averia> modificarAveria(Long id, Averia datos) {

        return averiaRepository.findById(id).map(averia -> {

            if (datos.getDescripcion() != null) {
                averia.setDescripcion(datos.getDescripcion());
            }

            if (datos.getEstado() != null) {
                averia.setEstado(datos.getEstado());
            }

            return averiaRepository.save(averia);
        });
    }

    // Eliminar una averia
    public boolean eliminarAveria(Long id) {

        Optional<Averia> resultado = averiaRepository.findById(id);

        if (resultado.isEmpty()) {
            return false;
        }

        Averia averia = resultado.get();

        // Quitar la averia de la lista del vehiculo
        Vehiculo vehiculo = averia.getVehiculo();

        if (vehiculo != null) {
            vehiculo.getAverias().remove(averia);
        }

        // Guardar el vehiculo para aplicar orphanRemoval
        if (vehiculo != null) {
            vehiculoRepository.save(vehiculo);
        } else {
            averiaRepository.delete(averia);
        }

        return true;
    }
}
