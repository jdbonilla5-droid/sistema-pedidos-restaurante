package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.CategoriaRequest;
import com.grupo4.restaurante.dto.CategoriaResponse;
import com.grupo4.restaurante.entity.Categoria;
import com.grupo4.restaurante.exception.RecursoDuplicadoException;
import com.grupo4.restaurante.exception.RecursoEnUsoException;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.CategoriaRepository;
import com.grupo4.restaurante.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final PlatoRepository platoRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository,
            PlatoRepository platoRepository) {

        this.categoriaRepository = categoriaRepository;
        this.platoRepository = platoRepository;
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        String nombre = request.nombre().strip();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una categoría con ese nombre"
            );
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(request.descripcion());

        Categoria guardada = categoriaRepository.save(categoria);

        return convertirAResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el ID " + id
                ));

        return convertirAResponse(categoria);
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el ID " + id
                ));

        String nombre = request.nombre().strip();

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe otra categoría con ese nombre"
            );
        }

        categoria.setNombre(nombre);
        categoria.setDescripcion(request.descripcion());

        Categoria guardada = categoriaRepository.save(categoria);

        return convertirAResponse(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el ID " + id
                ));

        if (platoRepository.existsByCategoria_Id(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar la categoría porque tiene platos asociados"
            );
        }

        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse convertirAResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}