package group4.pedidosrestaurante.service;

import group4.ec.edu.espe.pedidosrestaurante.dto.*;
import group4.ec.edu.espe.pedidosrestaurante.entity.*;
import group4.ec.edu.espe.pedidosrestaurante.exception.*;
import group4.ec.edu.espe.pedidosrestaurante.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detalleRepo;
    private final PedidoRepository pedidoRepo;
    private final PlatoRepository platoRepo;

    public DetallePedidoService(DetallePedidoRepository detalleRepo,
                                PedidoRepository pedidoRepo,
                                PlatoRepository platoRepo) {
        this.detalleRepo = detalleRepo;
        this.pedidoRepo = pedidoRepo;
        this.platoRepo = platoRepo;
    }

    @Transactional
    public DetalleResponse agregar(Long pedidoId, DetalleRequest req) {
        Pedido pedido = buscarPedido(pedidoId);
        validarModificable(pedido);                                   // RN-5

        Plato plato = platoRepo.findById(req.platoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Plato " + req.platoId() + " no encontrado"));
        if (!Boolean.TRUE.equals(plato.getDisponible()))              // RN-1
            throw new PlatoNoDisponibleException("El plato '" + plato.getNombre() + "' no está disponible");

        DetallePedido d = detalleRepo.findByPedidoIdAndPlatoId(pedidoId, plato.getId())
                .orElseGet(() -> {
                    DetallePedido nuevo = new DetallePedido();
                    nuevo.setPedido(pedido);
                    nuevo.setPlato(plato);
                    nuevo.setCantidad(0);
                    nuevo.setPrecioUnitario(plato.getPrecio());       // precio historico: se copia del catalogo
                    return nuevo;
                });
        d.setCantidad(d.getCantidad() + req.cantidad());
        d = detalleRepo.save(d);
        detalleRepo.flush();
        recalcularTotal(pedido);                                      // RN-3
        return toResponse(d);
    }

    @Transactional
    public DetalleResponse actualizarCantidad(Long detalleId, ActualizarCantidadRequest req) {
        DetallePedido d = buscarDetalle(detalleId);
        validarModificable(d.getPedido());
        d.setCantidad(req.cantidad());
        d = detalleRepo.save(d);
        detalleRepo.flush();
        recalcularTotal(d.getPedido());
        return toResponse(d);
    }

    @Transactional
    public void eliminar(Long detalleId) {
        DetallePedido d = buscarDetalle(detalleId);
        Pedido pedido = d.getPedido();
        validarModificable(pedido);
        detalleRepo.delete(d);
        detalleRepo.flush();
        recalcularTotal(pedido);
    }

    @Transactional(readOnly = true)
    public List<DetalleResponse> listar(Long pedidoId) {
        if (!pedidoRepo.existsById(pedidoId))
            throw new RecursoNoEncontradoException("Pedido " + pedidoId + " no encontrado");
        return detalleRepo.findByPedidoId(pedidoId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public void cambiarEstado(Long pedidoId, EstadoPedido nuevo) {
        Pedido pedido = buscarPedido(pedidoId);
        if (!pedido.getEstado().puedeCambiarA(nuevo))                 // RN-4
            throw new EstadoPedidoInvalidoException(
                    "No se puede pasar de " + pedido.getEstado() + " a " + nuevo);
        if (nuevo == EstadoPedido.EN_PREPARACION && detalleRepo.countByPedidoId(pedidoId) == 0)   // RN-6
            throw new EstadoPedidoInvalidoException(
                    "El pedido debe tener al menos un detalle antes de confirmarse");
        pedido.setEstado(nuevo);
        pedidoRepo.save(pedido);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido " + id + " no encontrado"));
    }

    private DetallePedido buscarDetalle(Long id) {
        return detalleRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle " + id + " no encontrado"));
    }

    private void validarModificable(Pedido p) {
        if (!p.getEstado().admiteModificaciones())
            throw new EstadoPedidoInvalidoException(
                    "No se puede modificar un pedido en estado " + p.getEstado());
    }

    private void recalcularTotal(Pedido pedido) {
        BigDecimal total = detalleRepo.calcularTotalPorPedido(pedido.getId());
        pedido.setTotal(total == null ? BigDecimal.ZERO : total);
        pedidoRepo.save(pedido);
    }

    private DetalleResponse toResponse(DetallePedido d) {
        return new DetalleResponse(d.getId(), d.getPlato().getId(), d.getPlato().getNombre(),
                d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
    }
}
