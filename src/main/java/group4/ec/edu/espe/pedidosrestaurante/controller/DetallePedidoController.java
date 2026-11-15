package group4.ec.edu.espe.pedidosrestaurante.controller;

import group4.ec.edu.espe.pedidosrestaurante.dto.*;
import group4.ec.edu.espe.pedidosrestaurante.service.DetallePedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DetallePedidoController {

    private final DetallePedidoService service;

    public DetallePedidoController(DetallePedidoService service) {
        this.service = service;
    }

    @PostMapping("/pedidos/{pedidoId}/detalles")
    public ResponseEntity<DetalleResponse> agregar(@PathVariable Long pedidoId,
                                                   @Valid @RequestBody DetalleRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agregar(pedidoId, req));
    }

    @GetMapping("/pedidos/{pedidoId}/detalles")
    public List<DetalleResponse> listar(@PathVariable Long pedidoId) {
        return service.listar(pedidoId);
    }

    @PutMapping("/detalles/{id}")
    public DetalleResponse actualizar(@PathVariable Long id,
                                      @Valid @RequestBody ActualizarCantidadRequest req) {
        return service.actualizarCantidad(id, req);
    }

    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/pedidos/{pedidoId}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long pedidoId,
                                              @Valid @RequestBody CambioEstadoRequest req) {
        service.cambiarEstado(pedidoId, req.estado());
        return ResponseEntity.noContent().build();
    }
}
