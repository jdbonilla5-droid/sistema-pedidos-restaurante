package group4.ec.edu.espe.pedidosrestaurante.service;

import group4.ec.edu.espe.pedidosrestaurante.dto.DetalleRequest;
import group4.ec.edu.espe.pedidosrestaurante.dto.DetalleResponse;
import group4.ec.edu.espe.pedidosrestaurante.entity.*;
import group4.ec.edu.espe.pedidosrestaurante.exception.*;
import group4.ec.edu.espe.pedidosrestaurante.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetallePedidoServiceTest {

    @Mock DetallePedidoRepository detalleRepo;
    @Mock PedidoRepository pedidoRepo;
    @Mock PlatoRepository platoRepo;
    @InjectMocks DetallePedidoService service;

    private Pedido pedido;
    private Plato plato;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        plato = new Plato();
        plato.setId(10L);
        plato.setNombre("Encebollado");
        plato.setPrecio(new BigDecimal("5.00"));
        plato.setDisponible(true);
    }

    @Test
    void agregarPlatoDisponible_calculaSubtotalYTotal() {
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(platoRepo.findById(10L)).thenReturn(Optional.of(plato));
        when(detalleRepo.findByPedidoIdAndPlatoId(1L, 10L)).thenReturn(Optional.empty());
        when(detalleRepo.save(any(DetallePedido.class))).thenAnswer(i -> i.getArgument(0));
        when(detalleRepo.calcularTotalPorPedido(1L)).thenReturn(new BigDecimal("10.00"));

        DetalleResponse r = service.agregar(1L, new DetalleRequest(10L, 2));

        assertEquals(new BigDecimal("10.00"), r.subtotal());
        assertEquals(new BigDecimal("10.00"), pedido.getTotal());
    }

    @Test
    void agregarPlatoNoDisponible_lanzaExcepcion() {
        plato.setDisponible(false);
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(platoRepo.findById(10L)).thenReturn(Optional.of(plato));

        assertThrows(PlatoNoDisponibleException.class,
                () -> service.agregar(1L, new DetalleRequest(10L, 1)));
        verify(detalleRepo, never()).save(any());
    }

    @Test
    void agregarAPedidoInexistente_lanza404() {
        when(pedidoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.agregar(99L, new DetalleRequest(10L, 1)));
    }

    @Test
    void cambioDeEstadoInvalido_lanzaExcepcion() {
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(EstadoPedidoInvalidoException.class,
                () -> service.cambiarEstado(1L, EstadoPedido.ENTREGADO));
    }

    @Test
    void cambioDeEstadoValido_actualizaPedido() {
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(detalleRepo.countByPedidoId(1L)).thenReturn(1L);

        service.cambiarEstado(1L, EstadoPedido.EN_PREPARACION);

        assertEquals(EstadoPedido.EN_PREPARACION, pedido.getEstado());
    }

    @Test
    void confirmarPedidoSinDetalles_lanzaExcepcion() {
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));
        when(detalleRepo.countByPedidoId(1L)).thenReturn(0L);

        assertThrows(EstadoPedidoInvalidoException.class,
                () -> service.cambiarEstado(1L, EstadoPedido.EN_PREPARACION));
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    @Test
    void agregarAPedidoEntregado_lanzaExcepcion() {
        pedido.setEstado(EstadoPedido.ENTREGADO);
        when(pedidoRepo.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(EstadoPedidoInvalidoException.class,
                () -> service.agregar(1L, new DetalleRequest(10L, 1)));
    }
}
