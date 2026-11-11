
package com.example.taller_reparacion.service;

import com.example.taller_reparacion.entity.EstadoReparacion;
import com.example.taller_reparacion.entity.Vehiculo;
import com.example.taller_reparacion.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // Listar todos los vehiculos
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    // Buscar un vehiculo por su ID
    public Optional<Vehiculo> buscarPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    // Crear un vehiculo
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    // Modificar un vehiculo
    public Optional<Vehiculo> modificarVehiculo(Long id, Vehiculo datos) {

        return vehiculoRepository.findById(id).map(vehiculo -> {

            vehiculo.setMatricula(datos.getMatricula());
            vehiculo.setModelo(datos.getModelo());
            vehiculo.setFechaMatriculacion(datos.getFechaMatriculacion());

            return vehiculoRepository.save(vehiculo);
        });
    }

    // Eliminar un vehiculo
    public int eliminarVehiculo(Long id) {

        Optional<Vehiculo> resultado = vehiculoRepository.findById(id);

        if (resultado.isEmpty()) {
            return 0; // No existe
        }

        Vehiculo vehiculo = resultado.get();

        // Comprobar si tiene averias sin terminar
        boolean tieneAveriasPendientes = vehiculo.getAverias().stream()
                .anyMatch(averia ->
                        averia.getEstado() != EstadoReparacion.TERMINADO);

        if (tieneAveriasPendientes) {
            return 2; // No se puede eliminar
        }

        // CascadeType.ALL eliminara tambien sus averias
        vehiculoRepository.delete(vehiculo);

        return 1; // Eliminado correctamente
    }
}
