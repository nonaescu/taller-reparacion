package com.example.taller_reparacion.controller;

import com.example.taller_reparacion.entity.Vehiculo;
import com.example.taller_reparacion.entity.Averia;
import com.example.taller_reparacion.service.VehiculoService;
import com.example.taller_reparacion.service.AveriaService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;
    private final AveriaService averiaService;

    public VehiculoController(VehiculoService vehiculoService,
                              AveriaService averiaService) {
        this.vehiculoService = vehiculoService;
        this.averiaService = averiaService;
    }

    // GET: listar todos los vehiculos
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listarVehiculos() {
        return ResponseEntity.ok(vehiculoService.listarVehiculos());
    }

    // GET: obtener un vehiculo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable Long id) {

        return vehiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: crear un vehiculo
    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(
            @RequestBody Vehiculo vehiculo) {

        Vehiculo nuevo = vehiculoService.crearVehiculo(vehiculo);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // PUT: modificar un vehiculo
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> modificarVehiculo(
            @PathVariable Long id,
            @RequestBody Vehiculo datos) {

        return vehiculoService.modificarVehiculo(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: eliminar un vehiculo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {

        int resultado = vehiculoService.eliminarVehiculo(id);

        if (resultado == 0) {
            return ResponseEntity.notFound().build();
        }

        if (resultado == 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.noContent().build();
    }

    // GET: listar las averias de un vehiculo
    @GetMapping("/{id}/averias")
    public ResponseEntity<List<Averia>> listarAveriasVehiculo(
            @PathVariable Long id) {

        return averiaService.listarAveriasPorVehiculo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
