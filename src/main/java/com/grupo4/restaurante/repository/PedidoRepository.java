
package com.grupo4.restaurante.repository;

import com.grupo4.restaurante.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente_Id(Long clienteId);

    boolean existsByCliente_Id(Long clienteId);

}