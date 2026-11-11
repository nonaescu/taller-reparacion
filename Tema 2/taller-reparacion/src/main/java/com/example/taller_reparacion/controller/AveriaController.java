package com.example.taller_reparacion.controller;

import com.example.taller_reparacion.entity.Averia;
import com.example.taller_reparacion.service.AveriaService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/averias")
public class AveriaController {

    private final AveriaService averiaService;

    public AveriaController(AveriaService averiaService) {
        this.averiaService = averiaService;
    }

    // GET: listar todas las averias
    @GetMapping
    public ResponseEntity<List<Averia>> listarAverias() {
        return ResponseEntity.ok(averiaService.listarAverias());
    }

    // GET: obtener una averia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Averia> obtenerAveria(@PathVariable Long id) {

        return averiaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: crear una averia
    @PostMapping
    public ResponseEntity<?> crearAveria(@RequestBody Averia averia) {

        try {
            Averia nueva = averiaService.crearAveria(averia);

            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (NoSuchElementException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // PUT: modificar una averia
    @PutMapping("/{id}")
    public ResponseEntity<Averia> modificarAveria(
            @PathVariable Long id,
            @RequestBody Averia datos) {

        return averiaService.modificarAveria(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: eliminar una averia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAveria(@PathVariable Long id) {

        boolean eliminado = averiaService.eliminarAveria(id);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
