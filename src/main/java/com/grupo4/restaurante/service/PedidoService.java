
package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.PedidoRequest;
import com.grupo4.restaurante.dto.PedidoResponse;
import com.grupo4.restaurante.entity.Cliente;
import com.grupo4.restaurante.entity.Pedido;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.ClienteRepository;
import com.grupo4.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con el ID "
                                + request.clienteId()
                ));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        Pedido guardado = pedidoRepository.save(pedido);

        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un pedido con el ID " + id
                ));

        return convertirAResponse(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException(
                    "No existe un cliente con el ID " + clienteId
            );
        }

        return pedidoRepository.findByCliente_Id(clienteId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private PedidoResponse convertirAResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNombre()
        );
    }
}
