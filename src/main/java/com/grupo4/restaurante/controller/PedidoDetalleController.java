
package com.grupo4.restaurante.controller;

import com.grupo4.restaurante.dto.PedidoDetalleRequest;
import com.grupo4.restaurante.dto.PedidoDetalleResponse;
import com.grupo4.restaurante.service.PedidoDetalleService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/detalles")
public class PedidoDetalleController {

    private final PedidoDetalleService detalleService;

    public PedidoDetalleController(
            PedidoDetalleService detalleService) {
        this.detalleService = detalleService;
    }

    @PostMapping
    public ResponseEntity<PedidoDetalleResponse> agregar(
            @PathVariable Long pedidoId,
            @Valid @RequestBody PedidoDetalleRequest request) {

        PedidoDetalleResponse respuesta =
                detalleService.agregar(pedidoId, request);

        return ResponseEntity
                .created(URI.create(
                        "/api/pedidos/" + pedidoId
                                + "/detalles/" + respuesta.id()))
                .body(respuesta);
    }

    @GetMapping
    public List<PedidoDetalleResponse> listar(
            @PathVariable Long pedidoId) {

        return detalleService.listarPorPedido(pedidoId);
    }
}
