
package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.PedidoDetalleRequest;
import com.grupo4.restaurante.dto.PedidoDetalleResponse;
import com.grupo4.restaurante.entity.Pedido;
import com.grupo4.restaurante.entity.PedidoDetalle;
import com.grupo4.restaurante.entity.Plato;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.PedidoDetalleRepository;
import com.grupo4.restaurante.repository.PedidoRepository;
import com.grupo4.restaurante.repository.PlatoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoDetalleService {

    private final PedidoDetalleRepository detalleRepository;
    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;

    public PedidoDetalleService(
            PedidoDetalleRepository detalleRepository,
            PedidoRepository pedidoRepository,
            PlatoRepository platoRepository) {

        this.detalleRepository = detalleRepository;
        this.pedidoRepository = pedidoRepository;
        this.platoRepository = platoRepository;
    }

    @Transactional
    public PedidoDetalleResponse agregar(
            Long pedidoId,
            PedidoDetalleRequest request) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un pedido con el ID " + pedidoId));

        Plato plato = platoRepository.findById(request.platoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un plato con el ID " + request.platoId()));

        PedidoDetalle detalle = new PedidoDetalle();

        detalle.setPedido(pedido);
        detalle.setPlato(plato);
        detalle.setCantidad(request.cantidad());

        BigDecimal precio = plato.getPrecio();

        BigDecimal subtotal = precio.multiply(
                BigDecimal.valueOf(request.cantidad())
        );

        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(subtotal);

        PedidoDetalle guardado = detalleRepository.save(detalle);

        pedido.setTotal(pedido.getTotal().add(subtotal));

        pedidoRepository.save(pedido);

        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoDetalleResponse> listarPorPedido(Long pedidoId) {

        if (!pedidoRepository.existsById(pedidoId)) {
            throw new RecursoNoEncontradoException(
                    "No existe un pedido con el ID " + pedidoId);
        }

        return detalleRepository.findByPedido_Id(pedidoId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private PedidoDetalleResponse convertirAResponse(
            PedidoDetalle detalle) {

        return new PedidoDetalleResponse(
                detalle.getId(),
                detalle.getPedido().getId(),
                detalle.getPlato().getId(),
                detalle.getPlato().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
