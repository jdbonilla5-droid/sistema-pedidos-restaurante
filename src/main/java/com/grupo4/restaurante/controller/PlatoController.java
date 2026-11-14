package com.grupo4.restaurante.controller;

import com.grupo4.restaurante.dto.PlatoRequest;
import com.grupo4.restaurante.dto.PlatoResponse;
import com.grupo4.restaurante.service.PlatoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    @PostMapping
    public ResponseEntity<PlatoResponse> crear(
            @Valid @RequestBody PlatoRequest request) {

        PlatoResponse respuesta = platoService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/platos/" + respuesta.id()))
                .body(respuesta);
    }

    @GetMapping
    public List<PlatoResponse> listar() {
        return platoService.listar();
    }

    @GetMapping("/{id}")
    public PlatoResponse buscarPorId(@PathVariable Long id) {
        return platoService.buscarPorId(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<PlatoResponse> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return platoService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/disponibilidad")
    public List<PlatoResponse> listarPorDisponibilidad(
            @RequestParam boolean disponible) {

        return platoService.listarPorDisponibilidad(disponible);
    }

    @PutMapping("/{id}")
    public PlatoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequest request) {

        return platoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}