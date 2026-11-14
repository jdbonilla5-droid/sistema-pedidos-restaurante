package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.PlatoRequest;
import com.grupo4.restaurante.dto.PlatoResponse;
import com.grupo4.restaurante.entity.Categoria;
import com.grupo4.restaurante.entity.Plato;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.CategoriaRepository;
import com.grupo4.restaurante.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;

    public PlatoService(
            PlatoRepository platoRepository,
            CategoriaRepository categoriaRepository) {

        this.platoRepository = platoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public PlatoResponse crear(PlatoRequest request) {
        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el ID "
                                + request.categoriaId()
                ));

        Plato plato = new Plato();
        plato.setNombre(request.nombre().strip());
        plato.setDescripcion(request.descripcion());
        plato.setPrecio(request.precio());
        plato.setDisponible(request.disponible());
        plato.setCategoria(categoria);

        Plato guardado = platoRepository.save(plato);

        return convertirAResponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<PlatoResponse> listar() {
        return platoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlatoResponse buscarPorId(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un plato con el ID " + id
                ));

        return convertirAResponse(plato);
    }

    @Transactional(readOnly = true)
    public List<PlatoResponse> listarPorCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new RecursoNoEncontradoException(
                    "No existe una categoría con el ID " + categoriaId
            );
        }

        return platoRepository.findByCategoria_Id(categoriaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlatoResponse> listarPorDisponibilidad(boolean disponible) {
        return platoRepository.findByDisponible(disponible)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }
    @Transactional
    public PlatoResponse actualizar(Long id, PlatoRequest request) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un plato con el ID " + id
                ));

        Categoria categoria = categoriaRepository
                .findById(request.categoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el ID "
                                + request.categoriaId()
                ));

        plato.setNombre(request.nombre().strip());
        plato.setDescripcion(request.descripcion());
        plato.setPrecio(request.precio());
        plato.setDisponible(request.disponible());
        plato.setCategoria(categoria);

        Plato guardado = platoRepository.save(plato);

        return convertirAResponse(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un plato con el ID " + id
                ));

        platoRepository.delete(plato);
    }
    private PlatoResponse convertirAResponse(Plato plato) {
        return new PlatoResponse(
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                plato.getPrecio(),
                plato.isDisponible(),
                plato.getCategoria().getId(),
                plato.getCategoria().getNombre()
        );
    }
}