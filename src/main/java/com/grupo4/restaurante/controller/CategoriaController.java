package com.grupo4.restaurante.controller;

import com.grupo4.restaurante.dto.CategoriaRequest;
import com.grupo4.restaurante.dto.CategoriaResponse;
import com.grupo4.restaurante.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(
            @Valid @RequestBody CategoriaRequest request) {

        CategoriaResponse respuesta = categoriaService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/categorias/" + respuesta.id()))
                .body(respuesta);
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {

        return categoriaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}