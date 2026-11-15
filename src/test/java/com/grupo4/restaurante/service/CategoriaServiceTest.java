package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.CategoriaRequest;
import com.grupo4.restaurante.entity.Categoria;
import com.grupo4.restaurante.exception.RecursoDuplicadoException;
import com.grupo4.restaurante.exception.RecursoEnUsoException;
import com.grupo4.restaurante.repository.CategoriaRepository;
import com.grupo4.restaurante.repository.PlatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private PlatoRepository platoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void crearCategoriaConNombreDuplicadoDebeRechazarla() {
        CategoriaRequest request = new CategoriaRequest(
                "  Bebidas  ",
                "Bebidas frías y calientes"
        );

        when(categoriaRepository.existsByNombreIgnoreCase("Bebidas"))
                .thenReturn(true);

        assertThrows(
                RecursoDuplicadoException.class,
                () -> categoriaService.crear(request)
        );

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void eliminarCategoriaConPlatosDebeRechazarla() {
        Categoria categoria = new Categoria();

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.existsByCategoria_Id(1L))
                .thenReturn(true);

        assertThrows(
                RecursoEnUsoException.class,
                () -> categoriaService.eliminar(1L)
        );

        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }

    @Test
    void eliminarCategoriaSinPlatosDebePermitirlo() {
        Categoria categoria = new Categoria();

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.existsByCategoria_Id(1L))
                .thenReturn(false);

        categoriaService.eliminar(1L);

        verify(categoriaRepository).delete(categoria);
    }
}