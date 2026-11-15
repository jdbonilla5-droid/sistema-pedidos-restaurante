
package com.grupo4.restaurante.controller;

import com.grupo4.restaurante.dto.PedidoRequest;
import com.grupo4.restaurante.dto.PedidoResponse;
import com.grupo4.restaurante.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(
            @Valid @RequestBody PedidoRequest request) {

        PedidoResponse respuesta = pedidoService.crear(request);

        return ResponseEntity
                .created(URI.create("/api/pedidos/" + respuesta.id()))
                .body(respuesta);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PedidoResponse> listarPorCliente(
            @PathVariable Long clienteId) {

        return pedidoService.listarPorCliente(clienteId);
    }
}
