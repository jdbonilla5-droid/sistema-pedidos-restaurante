package group4.ec.edu.espe.pedidosrestaurante.repository;

import group4.ec.edu.espe.pedidosrestaurante.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);

    Optional<DetallePedido> findByPedidoIdAndPlatoId(Long pedidoId, Long platoId);

    long countByPedidoId(Long pedidoId);

    @Query("SELECT SUM(d.cantidad * d.precioUnitario) FROM DetallePedido d WHERE d.pedido.id = :pedidoId")
    BigDecimal calcularTotalPorPedido(@Param("pedidoId") Long pedidoId);
}
