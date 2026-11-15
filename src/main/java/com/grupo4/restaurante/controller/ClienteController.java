
package com.grupo4.restaurante.controller;

import com.grupo4.restaurante.dto.ClienteRequest;
import com.grupo4.restaurante.dto.ClienteResponse;
import com.grupo4.restaurante.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse respuesta = clienteService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/clientes/" + respuesta.id()))
                .body(respuesta);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {

        return clienteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
