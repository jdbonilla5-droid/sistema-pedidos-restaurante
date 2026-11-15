
package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.ClienteRequest;
import com.grupo4.restaurante.dto.ClienteResponse;
import com.grupo4.restaurante.entity.Cliente;
import com.grupo4.restaurante.exception.RecursoEnUsoException;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.ClienteRepository;
import com.grupo4.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            PedidoRepository pedidoRepository) {

        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {

        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre().strip());
        cliente.setTelefono(request.telefono() == null
                ? null : request.telefono().strip());
        cliente.setCorreo(request.correo() == null
                ? null : request.correo().strip());

        Cliente guardado = clienteRepository.save(cliente);

        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con el ID " + id
                ));

        return convertirAResponse(cliente);
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con el ID " + id
                ));

        cliente.setNombre(request.nombre().strip());
        cliente.setTelefono(request.telefono() == null
                ? null : request.telefono().strip());
        cliente.setCorreo(request.correo() == null
                ? null : request.correo().strip());

        Cliente actualizado = clienteRepository.save(cliente);

        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con el ID " + id
                ));

        if (pedidoRepository.existsByCliente_Id(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar el cliente porque tiene pedidos asociados"
            );
        }

        clienteRepository.delete(cliente);
    }

    private ClienteResponse convertirAResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getCorreo()
        );
    }
}
